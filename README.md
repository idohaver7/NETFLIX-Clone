# Advanced-EX4
# Welcome To Our Netflix APP
This project is a Netflix clone built using React for the web frontend, Android for the mobile application, and Node.js for the backend. It aims to replicate the core features of Netflix, providing a robust solution for streaming video content on both web and mobile platforms.

# Overview
Web Frontend (React): React is renowned for its efficient rendering and ease of state management, making it ideal for building dynamic and responsive user interfaces. The web frontend will handle user interactions, display video content, and provide a seamless browsing experience similar to Netflix.

Mobile App (Android): Android is the platform of choice for mobile development, offering wide reach and customization options. The app will provide a native mobile experience, allowing users to browse, watch, and download their favorite shows and movies on their devices.

Backend Server (Node.js): Node.js will serve as the backbone of the application, handling business logic, database operations, authentication, and server-side rendering. Its non-blocking I/O model ensures efficient handling of multiple requests, ideal for a high-traffic app like a Netflix clone.

# Key Features
- **User Authentication**: Secure login and registration system to manage user accounts.
- **Video Streaming**: Integration of a video streaming service to play movies and shows.
- **Search and Recommendations**: Feature to search for titles and algorithm-based recommendations.

# Technologies and Tools
- **React**: Utilize React hooks for state management and Context API for state distribution across components.
- **Android**: Use Android Studio with Java to create a user-friendly mobile interface.
- **Node.js**: Leverage Express.js for routing and efficient API development.
- **Database**: MongoDB to store user data, video metadata, and viewing preferences.
- **Authentication**: Implement JWT (JSON Web Tokens) for secure and scalable user authentication.

## Running the apps
## Android 
### TCP Server
```
cd Server
cd tcpserver
make
./server
```
### Node Js Server
```
cd Server

cd server
npm install
$env:NODE_ENV="local"
node app.js
```
### MongoDB DataBase
  Our MongoDB is online one mongo atlas, already filled with users, movies and categories
  Admin user - admin:123123
  
## Web App with docker-compose

