#ifndef HelpCommand_H
#define HelpCommand_H

#include "ICommand.h"
#include <iostream>
#include <vector>
#include <string>

class HelpCommand : public ICommand {
public:
    HelpCommand();
    string execute(string userInput) const override;
};

#endif 
