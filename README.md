# Advanced-EX4
# Welcom To Our Netflix APP
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
### Create the MongoDB DataBase
1. Create a new connection with this connection string: 'mongodb://localhost:27017/'
2. Create a database called Youtube
3. Create three collections: 'users', 'categories', and 'movies'
4. Download the JSON files from the 'DB' directory:
   * 'Users.json'
   * 'Categories.json'
   * 'Movies.json'
5. Import them accordingly to the MongoDB collections.

### Create the MongoDB Database
1. Create a new connection with this connection string: `mongodb://localhost:27017/`
2. Create a database called `Youtube`.
3. Create three collections: `users`, `categories`, and `movies`.
4. Download the JSON files from the 'DB' directory:
   - `Users.json`
   - `Categories.json`
   - `Movies.json`
5. Import them accordingly to the MongoDB collections.
## Web App with docker-compose

