const express = require('express');
const router = express.Router();
const movieController = require('../controllers/movies');
const auth = require('../middleware/auth');
const path = require('path')
const multer = require('multer');

const storage = multer.diskStorage({
  destination: function(req, file, cb) {
    let dest = './public/'
    if (file.fieldname === 'image')
      dest += 'image/'
    else if (file.fieldname === 'video')
      dest += 'video/'
      cb(null, dest); 
  },
  filename: function(req, file, cb) {
          let title = req.body.title.replace(' ', '-')
          cb(null, title + path.extname(file.originalname)); 
      }
});

const upload = multer({ storage: storage });


router.route('/')
  .get(auth.verifyToken, movieController.getMovies)
  .post(auth.verifyManagerToken, upload.fields([{ name: 'image', maxCount: 1 }, { name: 'video', maxCount: 1 }]), movieController.createMovie);

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
