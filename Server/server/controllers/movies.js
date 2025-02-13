const movieService = require('../services/movies');
const categoryController = require('./category');
const userService = require('../services/user');

const createMovie = async (req, res) => {
  try {
    const { title, category, description } = req.body;
    const image = req.files['image'] ? req.files['image'][0].filename : null;  
    const video = req.files['video'] ? req.files['video'][0].filename : null;  

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
    // Fetch all movies (assuming movie.category is populated)
    const movies = await movieService.getMovies();

    // Filter movies where category.promoted === true
    const promotedMovies = movies.filter(movie => movie.category.promoted);

    // Group promoted movies by category and limit to 20 per category.
    let moviesByPromotedCategory = promotedMovies.reduce((result, movie) => {
      const categoryName = movie.category.name;
      if (!result[categoryName]) {
        result[categoryName] = [];
      }
      if (result[categoryName].length < 20) {
        result[categoryName].push(movie);
      }
      return result;
    }, {});

    // Fetch the last 20 movies the user has watched.
    let lastWatchedMovies = await movieService.getLastWatchedMovies(req.userId);
    if (lastWatchedMovies.length > 0) {
      moviesByPromotedCategory['Last Watched'] = lastWatchedMovies;
    }

    res.status(200).json(moviesByPromotedCategory);

  } catch (error) {
    console.error('Error fetching movies:', error.message);
    res.status(500).json({ error: 'Failed to fetch movies.' });
  }
};



const getMovie = async (req, res) => {
  try {
    let movie = await movieService.getMovieById(req.params.id);

    if (!movie) {
      return res.status(404).json({ errors: ['Movie not found'] });
    }

    return res.json(movie);
    
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
    const response = await movieService.getRecommendedMovies(req.params.id, req.userId);
    if (!response) {
      return res.status(404).json({ errors: ['Movie or UserId not found'] });
    }
    const lines = response.split('\n');
    console.log(lines);
    if (lines[0] === "200 OK") {
      let recommendedMovies = JSON.parse(lines[2]);
      return res.status(200).json(recommendedMovies);
    }
    return res.status(404).json();
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
      return res.status(404).json({ errors: ['the movie is alreade inside the server'] });
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
    let movies = await movieService.searchMovies(req.params.query);
    res.json(movies);
  } catch (error) {
    res.status(500).json({ errors: ['Internal Server Error'], details: error.message });
  }
};

const allMovies = async (req, res) => {
  const movies = await movieService.getMovies()
  res.status(200).json(movies)
}

module.exports = { createMovie,
                     getMovies, 
                     getMovie, 
                     updateMovie, 
                     deleteMovie,
                     getRecommendedMovies,
                     addToRecommendedMovies, 
                     searchMovies, 
                     allMovies,
                  };