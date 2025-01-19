#include "Commands/PatchUserCommand.h"
const string badRequest="400 Bad Request";
const string notFound="404 Not Found";
const string errorResponse="500 Error Response";
const string noContent="204 No Content";
string databaseFile = "data/database.txt";

// Constructor
PatchUserCommand::PatchUserCommand() {}

// Execute
string PatchUserCommand::execute(string userInput) const {
    string userid = BasicFunctions::extractUserId(userInput);
    vector<string> movieIds = BasicFunctions::extractMovies(userInput);
    sort(movieIds.begin(),movieIds.end());

    //check if the input is correct
    if(userid.empty() || movieIds.empty())
        return badRequest;
        
    return patch(userid, movieIds);
}

string PatchUserCommand::patch(string userId, vector<string> movies) const{
    // Search if the user already exists
    if (!BasicFunctions::searchIfUserExist(userId, databaseFile)) {
        return notFound;
    }
    // Temporary file for writing
    ofstream tempFile("temp.txt", ios::trunc);
    if (!tempFile) {
        return errorResponse;
    }


    // Open the database file for reading
    ifstream dataFile(databaseFile);
    if (!dataFile) {
        return errorResponse;
    }

    string line;
    while (getline(dataFile, line)) {
        istringstream lineStream(line);
        string currentUserId;
        lineStream >> currentUserId;

        if (currentUserId == userId) {
            tempFile << currentUserId; // Write the user ID
            vector<string> existingMovies;
            string token;
            char delimiter;

            // Read and store existing movies
            while (lineStream >> delimiter >> token) {
                existingMovies.push_back(token);
            }
            //  search for duplicates
            for (const auto &movie : movies) {
                if (find(existingMovies.begin(), existingMovies.end(), movie) != existingMovies.end()) {
                    dataFile.close();
                    tempFile.close();
                    remove("temp.txt");
                    return notFound;
                }
                existingMovies.push_back(movie);
            }
            sort(existingMovies.begin(),existingMovies.end());

            // Write updated movies to the line
            for (const auto &movie : existingMovies) {
                tempFile << " , " << movie;
            }
            tempFile << endl;

        } else {
            // Write the line as-is for other users
            tempFile << line << endl;
        }
    }

    // Close both files
    dataFile.close();
    tempFile.close();

    // Replace the original file with the updated temporary file
    if (rename("temp.txt", databaseFile.c_str()) != 0) {
        remove("temp.txt");
        return errorResponse;
    }

    return noContent;
}