#ifndef PatchUserCommand_H
#define PatchUserCommand_H

#include "ICommand.h"
#include "BasicFunctions.h"
#include <iostream>
#include <vector>
#include <string>
#include <fstream>
#include <sstream>
#include <algorithm>
using namespace std;

class PatchUserCommand : public ICommand {
public:
    PatchUserCommand();
    string execute(string userInput) const override;
    string patch(string userid, vector<string> movies) const;
};

#endif 
