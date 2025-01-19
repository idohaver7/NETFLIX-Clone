#include "Commands/GetMoviesCommand.h"

using namespace std;

// Constructor
GetMoviesCommand::GetMoviesCommand() {}

// Function to split the movied list into a vextor ==> Helper function
vector<string> splitMovies(const string& line) {
    vector<string> movies;
    size_t start = 0, end;

    while ((end = line.find(',', start)) != string::npos) {
        string movieStr = line.substr(start, end - start);
        try {
            movies.push_back(movieStr); 
        } catch (...) {}
        start = end + 1; // Move past the comma
    }

    // Handle the last movie ID (if any)
    if (start < line.size()) {
        try {
            movies.push_back(line.substr(start));
        } catch (...) {}
    }
    return movies;
}


// Excute
string GetMoviesCommand::execute(string userInput) const {
    istringstream input(userInput); // Parse the user input string
    string userID, targetMovieID;
    string command;

    input >> command; // Extract the command

    // Try extracting userID
    if (!(input >> userID)) {
        return "400 Bad Request";
    }

    // Try extracting userID
    if (!(input >> targetMovieID)) {
        return "400 Bad Request";
    }

    string string_userID = userID;
    string string_targetMovieID = targetMovieID;


    string databaseFile = "data/database.txt";
    ifstream dataFile(databaseFile); // Open the file for reading
    if (!dataFile) {
        return "500 Internal Server Error";
    }

    string line;
    vector<string> targetUserMovies;
    bool userFound = false;

    // Search for the user ID
    while (getline(dataFile, line)) {
        istringstream lineStream(line);
        string currentUserID;
        lineStream >> currentUserID;
        string token;
        char delimiter;

        if (currentUserID == userID) {
            while (lineStream >> delimiter >> token) {
                targetUserMovies.push_back(token);
            }
            userFound = true;
            break;
        }
    }
    dataFile.close();


    if (!userFound) {
        return "404 Not Found";
    }

    set<string> targetMovieSet(targetUserMovies.begin(), targetUserMovies.end());

    // Reopen file to calculate similarity scores
    ifstream dataFile2(databaseFile);
    if (!dataFile2) {
        return "500 Internal Server Error";
    }

    map<string, int> similarityScoreAll;

    while (getline(dataFile2, line)) {
        istringstream lineStream(line);
        string currentUserID;
        lineStream >> currentUserID;
        string token;
        char delimiter;
        vector<string> currentUserMovies;

        if (currentUserID == userID) {
            continue;
        }
        while (lineStream >> delimiter >> token) {
                currentUserMovies.push_back(token);
            }
        vector<string> commonMovies;

        set_intersection(
            targetUserMovies.begin(), targetUserMovies.end(),
            currentUserMovies.begin(), currentUserMovies.end(),
            back_inserter(commonMovies)
        );

        int similarityScore = commonMovies.size();

        if (similarityScore > 0) {
            similarityScoreAll[currentUserID] = similarityScore;
        }
    }

    dataFile2.close();

    // Calculate movie weights
    map<string, int> moviesWeight;

    ifstream dataFile3(databaseFile);
    if (!dataFile3) {
        return "500 Internal Server Error";
    }

    while (getline(dataFile3, line)) {
        istringstream lineStream(line);
        string currentUserID;
        lineStream >> currentUserID;
        string token;
        char delimiter;
        vector<string> currentUserMovies;
        if (currentUserID == userID) {
            continue;
        }

        while (lineStream >> delimiter >> token) {
                currentUserMovies.push_back(token);
        }
        set<string> currentUserMovieSet(currentUserMovies.begin(), currentUserMovies.end());

        if (currentUserMovieSet.find(targetMovieID) == currentUserMovieSet.end()) {
            continue;
        }

        if (similarityScoreAll.find(currentUserID) == similarityScoreAll.end()) continue;

        for (string movie : currentUserMovies) {
            if (targetMovieSet.find(movie) == targetMovieSet.end() && movie != targetMovieID) {
                moviesWeight[movie] += similarityScoreAll[currentUserID];
            }
        }
    }
    

    dataFile3.close();

    // Sort recommendations
    vector<pair<string, int>> sortedMovies(moviesWeight.begin(), moviesWeight.end());

    sort(sortedMovies.begin(), sortedMovies.end(), [](const pair<string, int>& a, const pair<string, int>& b) {
    if (a.second != b.second)
        return a.second > b.second;  // Primary by weight, descending
    return a.first < b.first;  // Secondary by movie name, ascending
});

    // Build response
    string response = "200 OK\n\n";
    int count = 0;

    for (const auto& [movie, weight] : sortedMovies) {
        response += movie + " ";
        if (++count == 10) break;
    }

    //response += "\n";
    return response;
}