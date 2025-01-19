#include "Commands/PostUserCommand.h"

const string badRequest="400 Bad Request";
const string notFound="404 Not Found";
const string errorResponse="500 Error Response";
const string noContent="204 No Content";
const string created="201 Created";

// Constructor
PostUserCommand::PostUserCommand() {}

// Execute
 string PostUserCommand::execute(string userInput) const {
    string userid = BasicFunctions::extractUserId(userInput);
    vector<string> movieIds = BasicFunctions::extractMovies(userInput);
    sort(movieIds.begin(),movieIds.end());

    //check if the input is correct
    if(userid.empty() || movieIds.empty())
        return badRequest;

    return post(userid, movieIds);
}

string PostUserCommand::post(string userId, vector<string> movies) const {
    string databaseFile = "data/database.txt";

    //search if the user is already exist
    if(BasicFunctions::searchIfUserExist(userId, databaseFile)){
        return notFound;
    }
    
    // If the user ID was not found
    ofstream dataFile(databaseFile, ios::app ); // Open the file for writing
    if (!dataFile) {
        return errorResponse;
    }

    dataFile << userId; // Write the user ID
    for (const auto &movie : movies) {
        dataFile << " , " << movie; // Add movies with spaces
    }
    dataFile << endl; // End the new line

    dataFile.close();
    return created;
}
