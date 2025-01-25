const express = require('express');
const router = express.Router();
const movieController = require('../controllers/movies');
const auth = require('../middleware/auth');

router.route('/')
  .get(auth.verifyToken, movieController.getMovies)
  .post(auth.verifyManagerToken, movieController.createMovie);

router.route('/:id')
  .get(auth.verifyToken, movieController.getMovie)
  .put(auth.verifyManagerToken, movieController.updateMovie)
  .delete(auth.verifyManagerToken, movieController.deleteMovie);

router.route('/:id/recommend')
  .get(auth.verifyToken, movieController.getRecommendedMovies)
  .post(auth.verifyToken, movieController.addToRecommendedMovies);

router.get('/all/movies', auth.verifyToken, movieController.allMovies)

router.get('/search/:query', auth.verifyToken, movieController.searchMovies);

module.exports = router;
