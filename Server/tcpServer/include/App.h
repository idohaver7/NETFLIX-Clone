// App.h
#ifndef APP_H
#define APP_H

#include <vector>
#include <map>
#include <string>

#include "Commands/PostUserCommand.h"
#include "Commands/HelpCommand.h"
#include "Commands/GetMoviesCommand.h"
#include "Commands/DeleteUserCommand.h"
#include "Commands/PatchUserCommand.h"
#include "Commands/ICommand.h"

using namespace std;

class App {
private:
    map<string, ICommand*> commands;
    void handle_client(int client_socket);

public:
    App(map<string, ICommand*> commands);
    void run();
};

#endif