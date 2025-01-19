#include "Commands/HelpCommand.h"

using namespace std;

// Constructor
HelpCommand::HelpCommand() {}

// Excute - Send HELP menu
string HelpCommand::execute(string userInput) const {
    if (userInput != "help") return "400 Bad Request";
    return "DELETE, arguments: [userid] [movieid1] [movieid2] …\n"
            "GET, arguments: [userid] [movieid]\n"
            "PATCH, arguments: [userid] [movieid]\n"
            "POST, arguments: [userid] [movieid1] [movieid2] …\n"
            "help\n"
            "200 OK";
}
