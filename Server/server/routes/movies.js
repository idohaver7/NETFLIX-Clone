const express = require('express');
const router = express.Router();
const movieController = require('../controllers/movies');
const auth = require('../middleware/auth');

router.route('/')
  .get(auth.isLoggedIn, movieController.getMovies)
  .post(movieController.createMovie);

router.route('/:id')
  .get(auth.isLoggedIn, movieController.getMovie)
  .put(auth.isLoggedIn, movieController.updateMovie)
  .delete(auth.isLoggedIn, movieController.deleteMovie);
  router.route('/:id/recommend')
  .get(auth.isLoggedIn, movieController.getRecommendedMovies)
  .post(auth.isLoggedIn, movieController.addToRecommendedMovies);


router.get('/search/:query', auth.isLoggedIn, movieController.searchMovies);

module.exports = router;
