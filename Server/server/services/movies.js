const Movie = require('../models/Movie');
const User = require('../models/User')
const Category = require('../models/category')

const net = require('net');

// Function to create a TCP connection and fetch data
const fetchFromTcpServer = (message) => {
  return new Promise((resolve, reject) => {
    const client = new net.Socket();
    const SERVER_HOST = '127.0.0.1'; // TCP Server address
    const SERVER_PORT = 8080;        // TCP Server port

    client.connect(SERVER_PORT, SERVER_HOST, () => {
      client.write(message);
    });

    client.on('data', (data) => {
      client.destroy(); // kill client after server's response
      resolve(data.toString());
    });

    client.on('error', (err) => {
      reject(err);
    });
  });
};
const createMovie = async (title, category, video, description, image) => {
  const movie = new Movie({ title, category, video, description, image });
  return await movie.save();
};


const getMovieById = async (id) => {
  try {
    return await Movie.findById(id).populate('category');
  } catch (error) {
    return null;
  }
};

const getMovies = async () => {
  return await Movie.find({}).populate('category');
};

const updateMovie = async (id, updates) => {
  try {
    const movie = await getMovieById(id);
    if (!movie) return null;

    Object.assign(movie, updates);
    return await movie.save();
  } catch (error) {
    return null;
  }
};

const deleteMovie = async (id) => {
  try {
    const movie = await getMovieById(id);
    if (!movie) return null;

    // Remove the movie reference from all users' watchedMovies arrays
    await User.updateMany(
      { watchedMovies: id }, 
      { $pull: { watchedMovies: id } }
    );
  
    const users = await User.find()
    for (const user of users) {
      await deleteRecommendedMovies(id, user._id)
    }

    await movie.deleteOne();
    return movie;
  } catch (error) {
    return null;
  }
};
const getRecommendedMovies = async (movieId,userId) => {
  try {
    const movie = await getMovieById(movieId);
    if (!movie){
      return null;
    }
    //send a request to the tcp server
    const response = await fetchFromTcpServer(`GET `+ userId + ' ' + movieId);
    return response;
  } catch (error) {
    return null;
  }
};
const addToRecommendedMovies = async (movieId,userId) => {
  try {
    const movie = await getMovieById(movieId);
    if (!movie) {
      return null;
    }

    //send a request to the tcp server
    const response = await fetchFromTcpServer(`POST `+ userId + ' ' + movieId);

    //if the user id already existed in the TCP server
    if(response=="404 Not Found") {
      const patch_response = await fetchFromTcpServer(`PATCH ` + userId + ' ' + movieId);
      // Add Movie to user watched array
      const updatedUser = await User.findByIdAndUpdate(
        userId,
        { $addToSet: { watchedMovies: movieId } },
        { new: true }
      );
      return patch_response;
    }

    // Add Movie to user watched array
    const updatedUser = await User.findByIdAndUpdate(
      userId,
      { $addToSet: { watchedMovies: movieId } },
      { new: true }
    );

    return response;
  } catch (error) {
    console.log(error)
    return null;
  }
};

const deleteRecommendedMovies = async (movieId,userId) => {
  try {
    const movie = await getMovieById(movieId);
    if (!movie) {
      return null;
    }
    //send a request to the tcp server
    const response = await fetchFromTcpServer(`DELETE `+ userId + ' ' + movieId);

    return response;
  } catch (error) {
    return null;
  }
};

const searchMovies = async (query) => {
  return await Movie.find({ title: query });
};


const getLastWatchedMovies = async (userId) => {
  return await Movie.find({ watchedBy: userId }) // Filter by watchedBy userId
    .sort({ _id: -1 }) // Sort by most recent
    .limit(20) // Limit to 20 movies
    .populate('category');
};


module.exports = { createMovie, getMovieById, getMovies, updateMovie, deleteMovie, searchMovies, getRecommendedMovies, addToRecommendedMovies, getLastWatchedMovies };
