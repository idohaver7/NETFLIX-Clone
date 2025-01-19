#include "Commands/DeleteUserCommand.h"


const string badRequest="400 Bad Request";
const string notFound="404 Not Found";
const string errorResponse="500 Error Response";
const string noContent="204 No Content";

// Constructor
DeleteUserCommand::DeleteUserCommand() {}

// Execute
string DeleteUserCommand::execute(string userInput) const {
    string userid = BasicFunctions::extractUserId(userInput);
    vector<string> moviesToDelete = BasicFunctions::extractMovies(userInput);

    //check if the input is correct
    if(userid.empty()||moviesToDelete.empty())
        return badRequest;

    return Delete(userid, moviesToDelete);
}

string DeleteUserCommand::Delete(string userId, vector<string> moviesToDelete) const {
    string databaseFile = "data/database.txt";
    // Search if the user already exists
    if (!BasicFunctions::searchIfUserExist(userId, databaseFile)) {
        return notFound;
    }
    ifstream dataFile(databaseFile); // Open file for reading

    if (!dataFile) {
        return errorResponse;
    }

    ofstream tempFile("temp.txt", ios::trunc); // Temporary file for writing
    if (!tempFile) {
        return errorResponse;
    }

    string line;

    // Process the file line by line
    while (getline(dataFile, line)) {
        istringstream lineStream(line);
        string currentUserId;
        lineStream >> currentUserId;

        if (currentUserId == userId) {
            // Extract movie IDs from the line
            vector<string> userMovies;
            string movieId;
            char comma;

            while (lineStream >> comma >> movieId) {
                userMovies.push_back(movieId);
            }

            unordered_set<string> moviesSet(moviesToDelete.begin(), moviesToDelete.end());
            
            // Check if all movies to delete exist in the user's movie list
            for (const auto &movieToDelete : moviesToDelete) {
                if (find(userMovies.begin(), userMovies.end(), movieToDelete) == userMovies.end()) {
                    tempFile.close();
                    remove("temp.txt");
                    return notFound;
                }
            }

            // Use remove_if and erase to filter userMovies
            userMovies.erase(
                std::remove_if(userMovies.begin(), userMovies.end(), [&](string movie) {
                    return moviesSet.find(movie) != moviesSet.end();
                }),
                userMovies.end()
            );

            // Write the updated user line to the temp file
            tempFile << currentUserId;
            for (const auto &movie : userMovies) {
                tempFile << " , " << movie;
            }
            tempFile << endl;

        } else {
            // Write the line as-is for other users
            tempFile << line << endl;
        }
    }

    dataFile.close();
    tempFile.close();

    // Replace the original file with the updated temporary file
    if (rename("temp.txt", databaseFile.c_str()) != 0) {
        remove("temp.txt");
        return errorResponse;
    }
    return noContent;
}
