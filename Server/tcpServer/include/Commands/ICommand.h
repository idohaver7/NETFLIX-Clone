#ifndef ICommand_H
#define ICommand_H
#include <string>
using namespace std; 
class ICommand {
public:
    virtual string execute(string userInput) const = 0;  // Pure virtual function
};

#endif 