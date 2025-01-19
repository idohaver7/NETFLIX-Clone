CXX = g++
CXXFLAGS = -std=c++17 -I./include -pthread
# Optionally add other compiler flags or include directories here

# All source files needed for command implementations
CMD_SRC = src/Commands/HelpCommand.cpp \
          src/Commands/PostUserCommand.cpp \
          src/Commands/PatchUserCommand.cpp \
          src/Commands/DeleteUserCommand.cpp \
          src/Commands/BasicFunctions.cpp \
          src/ThreadPool.cpp # Ensure this is included

SRC = server.cpp \
      src/App.cpp \
      src/Commands/GetMoviesCommand.cpp \
      $(CMD_SRC)

OBJ = $(SRC:.cpp=.o)
EXEC = server

$(EXEC): $(OBJ)
	$(CXX) $(OBJ) -o $(EXEC)

.cpp.o:
	$(CXX) $(CXXFLAGS) -c $< -o $@

clean:
	rm -f $(OBJ) $(EXEC)

