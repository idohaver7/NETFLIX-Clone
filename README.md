# Welcome To Our Netflix APP
This project is a Netflix clone built using React for the web frontend, Android for the mobile application, and Node.js for the backend. It aims to replicate the core features of Netflix, providing a robust solution for streaming video content on both web and mobile platforms.

## Overview
Web Frontend (React): React is renowned for its efficient rendering and ease of state management, making it ideal for building dynamic and responsive user interfaces. The web frontend will handle user interactions, display video content, and provide a seamless browsing experience similar to Netflix.

Mobile App (Android): Android is the platform of choice for mobile development, offering wide reach and customization options. The app will provide a native mobile experience, allowing users to browse, watch, and download their favorite shows and movies on their devices.

Backend Server (Node.js): Node.js will serve as the backbone of the application, handling business logic, database operations, authentication, and server-side rendering. Its non-blocking I/O model ensures efficient handling of multiple requests, ideal for a high-traffic app like a Netflix clone.

## Key Features
- **User Authentication**: Secure login and registration system to manage user accounts.
- **Video Streaming**: Integration of a video streaming service to play movies and shows.
- **Search and Recommendations**: Feature to search for titles and algorithm-based recommendations.

## Technologies and Tools
- **React**: Utilize React hooks for state management and Context API for state distribution across components.
- **Android**: Use Android Studio with Java to create a user-friendly mobile interface.
- **Node.js**: Leverage Express.js for routing and efficient API development.
- **Database**: MongoDB to store user data, video metadata, and viewing preferences.
- **Authentication**: Implement JWT (JSON Web Tokens) for secure and scalable user authentication.

# Getting Started
## Android

## Server
```
  Docker:
    1. cd Server
    2. docker-compose build  
    3. docker-compose up -d
    4. docker-compose logs -f

  Without Docker:
    1. cd Server
        Terminal 1:
            1. cd server
            2. npm install
            3. NODE_ENV=local node app.js
        Terminal 2:
            1. cd tcpServer
            2. make
            3. ./server
```
            
## React Web App
    Docker:
      1. cd netflix-client
      2. docker build -t netflix-client .
      3. docker run -p 4000:3000 netflix-client

    Without Docker:
      1. cd netflix-client
      2. npm install
      3. npm start



## MongoDB DataBase
  Our MongoDB is online one mongo atlas, already filled with users, movies and categories
  Admin user - admin:123123

# Let's Move about our Project Phases:

# Server
Movie recommendation system as a API Server.
Allows users to add categories, movies and get recommendations, Netflix Backend.

## API DOCS
    http://foo.com/api/categories :
        GET - Return all categories
        POST - Create new category

    http://foo.com/api/categories/:id :
        GET - Return the :id category
        PATCH - Update the :id category
        DELETE - Delete the :id category

    http://foo.com/api/movies :
        GET - Return recommendations for movies by categories
        POST - Create New movie

    http://foo.com/api/movies/:id :
        GET - Return the :id movie
        PUT - Update the :id movie
        DELETE - Delete the :id movie

    http://foo.com/api/movies/:id/recommend/ :
        GET - Return recommendations
        POST - Add movie to user

    http://foo.com/api/movies/search/:query/ :
        GET - Return movies by query
# NETFLIX React Web App

## Available Scripts

### `npm start`

Runs the app in the development mode.\
Open [http://localhost:3000](http://localhost:3000) to view it in your browser.

The page will reload when you make changes.\
You may also see any lint errors in the console.

### `npm install`

Builds the app for production to the `build` folder.\
It correctly bundles React in production mode and optimizes the build for the best performance.

# Examples
### `Home Page`
![alt text](https://github.com/NirPerets/Advanced-EX4/blob/main/netflix-client/example/homepage.png)

### `Register`
![alt text](https://github.com/NirPerets/Advanced-EX4/blob/main/netflix-client/example/register.png)

### `Sign In`
![alt text](https://github.com/NirPerets/Advanced-EX4/blob/main/netflix-client/example/signin.png)

### `Logged Page`
![alt text](https://github.com/NirPerets/Advanced-EX4/blob/main/netflix-client/example/logged.png)

### `Search Page`
![alt text](https://github.com/NirPerets/Advanced-EX4/blob/main/netflix-client/example/search.png)

### `Movie Page`
![alt text](https://github.com/NirPerets/Advanced-EX4/blob/main/netflix-client/example/moviepage.png)

### `Movie Player`
![alt text](https://github.com/NirPerets/Advanced-EX4/blob/main/netflix-client/example/movieplayer.png)

### `Manager`
![alt text](https://github.com/NirPerets/Advanced-EX4/blob/main/netflix-client/example/manager.png)

### `Add Movie`
![alt text](https://github.com/NirPerets/Advanced-EX4/blob/main/netflix-client/example/addmovie.png)


# NETFLIX Android App

## About the Project

### Home Page
When you open the app, you will see the home page where you can navigate to either the sign-in page or sign-up page.
![Home Page](https://github.com/user-attachments/assets/35ae55ae-3029-4943-832d-0feaddf19031)

### Sign Up
Choose and enter your password and email, enter your details, and add a photo of yourself.
![Sign Up](https://github.com/user-attachments/assets/d729d088-8b70-4ac6-b325-c2e99330f1e4)

### Login Page
You will need to enter your email and password to enter the app.
![Login Page](https://github.com/user-attachments/assets/f7fc57b5-b198-477d-b3a5-d894261b0dbd)

### After Login Page
After you log in to the app, you will see all the movies sorted by categories. At the top, you will see a small menu with a dark mode button, search, and profile picture. There will be a random movie that will play under the menu bar.
![After Login](https://github.com/user-attachments/assets/f28e7d8d-a0e9-4fed-ac5c-a13e41ba4707)

### Movie Details Page
By clicking on a movie, you will enter the movie details page where you will see the recommended movies suggested by this movie.
![Movie Details](https://github.com/user-attachments/assets/8c49e68e-c1df-47f1-a17e-eae9cbac94ed)

### Play Movie Page
By clicking the play button, you will start watching the movie.
![Play Movie](https://github.com/user-attachments/assets/5c1352cc-f2d2-4c73-81c9-075fee4017a0)

### Search Page
By clicking the search logo in the top menu on the home page, you will enter the search page.
![Search Page](https://github.com/user-attachments/assets/e45b3da0-f210-45d8-9095-2086255c7f64)

### Management Page
If you log in as an admin user, you can see the management button by clicking the profile picture. There you have a lot of options: add a movie, delete a movie, add a category, delete a category, and more.
![Management Page](https://github.com/user-attachments/assets/c9adc28a-4674-4fd7-8ab4-e923dfdce410)
#### Add a Movie
![Add Movie](https://github.com/user-attachments/assets/84f0af9b-0c18-4ef8-93dd-652feb92c9a8)
### Delete a Movie
![Delete Movie](https://github.com/user-attachments/assets/ad99e1d9-7eca-495a-9189-b7e5fd2621b4)


# written and developed by :
Nir Perets
Daniel Gusakov
Ido Haver
