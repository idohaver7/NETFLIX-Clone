#include "App.h"
#include "ThreadPool.h"
#include <sys/socket.h>
#include <netinet/in.h>
#include <arpa/inet.h>
#include <unistd.h>
#include <cstring>
#include <iostream>

#define DEFAULT_PORT 8000
#define BUFFER_SIZE 1024
int port = (getenv("SERVER_PORT") != nullptr) ? atoi(getenv("SERVER_PORT")) : DEFAULT_PORT;

using namespace std;

App::App(map<string, ICommand*> commands) : commands(commands) {}

void App::run() {
    // Set up server socket
    int server_socket = socket(AF_INET, SOCK_STREAM, 0);
    if (server_socket == -1) {
        cerr << "Error: Unable to create socket." << endl;
        return;
    }

    sockaddr_in server_address;
    server_address.sin_family = AF_INET;
    server_address.sin_port = htons(port);
    server_address.sin_addr.s_addr = INADDR_ANY;

    if (::bind(server_socket, (struct sockaddr*)&server_address, sizeof(server_address)) < 0) {
        cerr << "Error: Binding failed." << endl;
        close(server_socket);
        return;
    }

    if (listen(server_socket, 5) < 0) {
        cerr << "Error: Listening failed." << endl;
        close(server_socket);
        return;
    }

    cout << "Server is running on port " << port << "..." << endl;

    // Create a thread pool
    const size_t numThreads = thread::hardware_concurrency(); // Use the number of CPU cores
    ThreadPool threadPool(numThreads);

    while (true) {
        sockaddr_in client_address;
        socklen_t client_len = sizeof(client_address);
        int client_socket = accept(server_socket, (struct sockaddr*)&client_address, &client_len);

        if (client_socket < 0) {
            cerr << "Error: Failed to accept connection." << endl;
            continue;
        }

        cout << "New client connected: " << inet_ntoa(client_address.sin_addr) << endl;

        // Enqueue the client handling task in the thread pool
        threadPool.enqueue([this, client_socket]() {
            this->handle_client(client_socket);
        });
    }

    close(server_socket);
}

// Handles communication with a single client
void App::handle_client(int client_socket) {
    char buffer[BUFFER_SIZE] = {0};

    while (true) {
        // Clear buffer
        memset(buffer, 0, BUFFER_SIZE);

        // Receive data from client
        int bytes_received = recv(client_socket, buffer, BUFFER_SIZE, 0);
        if (bytes_received <= 0) {
            cout << "Client disconnected." << endl;
            close(client_socket);
            break;
        }

        string userInput(buffer);
        string response;

        try {
            // Parse the first word (command)
            istringstream stream(userInput);
            string command;
            stream >> command;

            // Check if the command exists
            if (this->commands.count(command)) {
                // Execute the command
                try {
                    response = this->commands[command]->execute(userInput);
                } catch (...) {
                    response = "400 Bad Request";
                }
            } else {
                // Invalid command
                response = "400 Bad Request";
            }
        } catch (...) {
            // Unexpected error
            response = "500 Internal Server Error";
        }

        // Send response back to the client
        send(client_socket, response.c_str(), response.size(), 0);
    }
}
