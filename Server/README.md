# Advanced Programming - Project Part 3
Movie recommendation system as a API Server.
Allows users to add categories, movies and get recommendations, Netflix Backend.

# API DOCS
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

        
# How to run
    Docker: 
        1. docker-compose build  
        2. docker-compose up -d
        3. docker-compose logs -f

    Without Docker:
        Terminal 1:
            1. cd server
            2. npm install
            3. NODE_ENV=local node app.js
        Terminal 2:
            1. cd tcpServer
            2. make
            3. ./server
