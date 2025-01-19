#ifndef PostUserCommand_H
#define PostUserCommand_H

#include "ICommand.h"
#include "BasicFunctions.h"
#include <iostream>
#include <vector>
#include <string>
#include <fstream>
#include <sstream>
#include <algorithm>

using namespace std;

class PostUserCommand : public ICommand {
public:
    PostUserCommand();
    string execute(string userInput) const override;
    string post(string userid, vector<string> movies) const;
};

#endif 
