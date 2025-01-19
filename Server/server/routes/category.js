const express = require('express');
var router = express.Router();
const categoryController = require('../controllers/category');
const auth = require('../middleware/auth');

router
  .route('/')
  .get(auth.isLoggedIn, categoryController.getCategories)
  .post(auth.isLoggedIn, categoryController.createCategory);

router
  .route('/:id')
  .get(auth.isLoggedIn, categoryController.getCategory)
  .patch(auth.isLoggedIn, categoryController.updateCategory)
  .delete(auth.isLoggedIn, categoryController.deleteCategory);

module.exports = router;