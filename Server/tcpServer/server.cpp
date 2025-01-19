// main.cpp
#include "include/App.h"

using namespace std;

int main() {
    // Map available commands
    map<string, ICommand*> commands;
    commands["help"] = new HelpCommand();           // Help command
    commands["POST"] = new PostUserCommand();        // Maps to user addition
    commands["GET"] = new GetMoviesCommand();       // Maps to movie recommendations
    commands["DELETE"] = new DeleteUserCommand();       // Placeholder for DELETE command
    commands["PATCH"] = new PatchUserCommand();         // Placeholder for PATCH command

    // Create the app
    App app(commands);
    app.run();

    return 0;
}
