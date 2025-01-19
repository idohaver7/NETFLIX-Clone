# Use Ubuntu as the base image
FROM ubuntu:20.04

# Set working directory
WORKDIR /app

# Install build tools
RUN apt-get update && apt-get install -y \
    build-essential \
    g++ \
    && rm -rf /var/lib/apt/lists/*

# Copy server files
COPY . .

# Build the server
RUN g++ -std=c++17 \
    -I./include -pthread \
    ./server.cpp ./src/App.cpp \
    ./src/Commands/BasicFunctions.cpp \
    ./src/Commands/DeleteUserCommand.cpp \
    ./src/Commands/GetMoviesCommand.cpp \
    ./src/Commands/HelpCommand.cpp \
    ./src/Commands/PostUserCommand.cpp \
    ./src/Commands/PatchUserCommand.cpp \
    ./src/ThreadPool.cpp \
    -o movie_server

# Expose the server port
EXPOSE 8080

# Start the server
CMD ["./movie_server"]
