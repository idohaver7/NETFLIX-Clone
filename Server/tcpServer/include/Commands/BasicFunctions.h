#ifndef BasicFunctions_H
#define BasicFunctions_H

#include "ICommand.h"
#include <iostream>
#include <vector>
#include <string>
#include <fstream>
#include <sstream>

using namespace std;

class BasicFunctions  {
public:
    static bool searchIfUserExist(string userId,string dataFileName);
    static string extractUserId(string userInput);
    static vector<string> extractMovies(string userInput);
};


#endif 
