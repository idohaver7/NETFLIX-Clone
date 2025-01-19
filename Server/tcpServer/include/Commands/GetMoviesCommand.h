#ifndef GetMoviesCommand_H
#define GetMoviesCommand_H

#include "ICommand.h"
#include <iostream>
#include <vector>
#include <string>
#include <iostream>
#include <fstream>
#include <sstream>
#include <map>
#include <set>
#include <algorithm>
#include "BasicFunctions.h"

class GetMoviesCommand : public ICommand {
public:
    GetMoviesCommand();
    string execute(string userInput) const override;
};

#endif 
