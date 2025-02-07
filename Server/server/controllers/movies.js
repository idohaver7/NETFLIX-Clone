const movieService = require('../services/movies');
const categoryController = require('./category');
const userService = require('../services/user');
const Movie = require('../models/Movie');

const createMovie = async (req, res) => {
  try {
    const { title, category, video, description, image } = req.body;

    if (!title || !category) {
      return res.status(400).json({ errors: ['Title and Category are required'] });
    }

    const movie = await movieService.createMovie(title, category, video, description, image);
    res.status(201).json(movie);
  } catch (error) {
    console.error('Error creating movie:', error.message);
    res.status(500).json({ errors: ['Failed to create movie'], details: error.message });
  }
};


  const getMovies = async (req, res) => {
    try {
      // Fetch all movies with their categories populated
      const movies = await movieService.getMovies(); // Ensure category is populated
  
      // Filter movies where category.promoted = true
      const promotedMovies = movies.filter(movie => movie.category.promoted);
  
      // Group promoted movies by category and limit to 20 movies per category
      let moviesByPromotedCategory = promotedMovies.reduce((result, movie) => {
        const categoryName = movie.category.name; // Ensure category is populated
        if (!result[categoryName]) {
          result[categoryName] = []; // Initialize array for this category
        }
        if (result[categoryName].length < 20) {
          result[categoryName].push(movie); // Add movie if less than 20
        }
        return result;
      }, {});
  
      // Fetch the last 20 movies the user has watched
      const lastWatchedMovies = await movieService.getLastWatchedMovies(req.userId);
  
      // If the user has watched movies, add a special category
      if (lastWatchedMovies.length > 0) {
        moviesByPromotedCategory['Last Watched'] = lastWatchedMovies;
      }
  
      // Return the grouped promoted movies and last watched category
      res.status(200).json(moviesByPromotedCategory);

    } catch (error) {
      console.error('Error fetching movies:', error.message);
      res.status(500).json({ error: 'Failed to fetch movies.' });
    }
  };


const getMovie = async (req, res) => {
    try {
      const movie = await movieService.getMovieById(req.params.id);
  
      if (!movie) {
        return res.status(404).json({ errors: ['Movie not found'] });
      }
  
      res.json(movie);
    } catch (error) {
      res.status(500).json({ errors: ['Internal Server Error'], details: error.message });
    }
  };
  
  const updateMovie = async (req, res) => {
    try {
      const updates = req.body;
      const movie = await movieService.updateMovie(req.params.id, updates);
  
      if (!movie) {
        return res.status(404).json({ errors: ['Movie not found'] });
      }
  
      res.status(204).json();
    } catch (error) {
      res.status(500).json({ errors: ['Internal Server Error'], details: error.message });
    }
  };
  
  const deleteMovie = async (req, res) => {
    try {
      const movie = await movieService.deleteMovie(req.params.id);
  
      if (!movie) {
        return res.status(404).json({ errors: ['Movie not found'] });
      }
  
      res.status(204).json();
    } catch (error) {
      res.status(500).json({ errors: ['Internal Server Error'], details: error.message });
    }
  };
  const getRecommendedMovies = async (req, res) => {
    try {
      const movies = await movieService.getRecommendedMovies(req.params.id,req.userId);
      if (!movies) {
        return res.status(404).json({ errors: ['Movie or UserId not found'] });
      }
      res.status(200).json(movies);
    } catch (error) {
      res.status(500).json({ errors: ['Internal Server Error'], details: error.message });
    }
  };
  const addToRecommendedMovies = async (req, res) => {
    try {
      const response = await movieService.addToRecommendedMovies(req.params.id,req.userId);
      if (!response) {
        return res.status(404).json({ errors: ['Movie or UserID not found'] });
      }
      if(response=="404 Not Found"){
        return res.status(204).json();
      }
      //post response
      if(response=="201 Created"){
        return res.status(201).json();
      }
      if(response=="204 No Content"){
        return res.status(204).json();
      }
      return res.status(500).json();
    } catch (error) {
      res.status(500).json({ errors: ['Internal Server Error'], details: error.message });
    }
  };

  const searchMovies = async (req, res) => {
    try {
      const movies = await movieService.searchMovies(req.params.query);
      res.json(movies);
    } catch (error) {
      res.status(500).json({ errors: ['Internal Server Error'], details: error.message });
    }
  }

  const allMovies = async (req, res) => {
    const movies = await movieService.getMovies()
    res.status(200).json(movies)
  }

module.exports = { createMovie, getMovies, getMovie, updateMovie, deleteMovie,getRecommendedMovies,addToRecommendedMovies, searchMovies, allMovies };
