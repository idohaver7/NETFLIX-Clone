#include "Commands/BasicFunctions.h"

bool BasicFunctions::searchIfUserExist(string userId,string dataFileName) {
    ifstream dataFile(dataFileName);
     if (!dataFile) {
        return false;
    }
    string line;
    bool found = false;

    // Process each line in the file
    while (getline(dataFile, line)) {
        istringstream lineStream(line);
        string currentUserId;
        lineStream >> currentUserId;
        if (currentUserId == userId) { // If the user ID matches
            found = true;
            return found;
        }
    }
    
    return found;
}

string BasicFunctions::extractUserId(string userInput){
    istringstream stream(userInput); // Create a stream for parsing
    string command;
    string userid;
    // Extract the first word (command)
    stream >> command;

    // Extract the second word (userid)
    if (!(stream >> userid)) {
        return "";
    }

    return userid;
}
vector<string> BasicFunctions::extractMovies(string userInput){
    istringstream stream(userInput); // Create a stream for parsing
    string command;
    string userid;
    vector<string> movieIds;

    // Extract the first word (command)
    stream >> command;

    // Extract the second word (userid)
    if (!(stream >> userid)) {
        return {};
    }

    // Extract the remaining words (movie IDs) into an array
    string movieId;
    int movieCount = 0;

    while (stream >> movieId) {
        movieIds.push_back(movieId);
        // If a movie ID is valid, continue checking
        ++movieCount;
    }

    if (movieCount == 0) {
        return {};
    }

    if (!stream.eof() && stream.fail()) {
        return {};
    }

    movieCount = 0;
    return movieIds;
}