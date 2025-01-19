#ifndef DeleteUserCommand_H
#define DeleteUserCommand_H

#include "ICommand.h"
#include <iostream>
#include <vector>
#include <string>
#include <fstream>
#include <sstream>
#include "BasicFunctions.h"
#include <algorithm>
#include <unordered_set>

using namespace std;

class DeleteUserCommand : public ICommand {
public:
    DeleteUserCommand();
    string execute(string userInput) const override;
    string Delete(string userId, vector<string> movies) const;
};

#endif 
