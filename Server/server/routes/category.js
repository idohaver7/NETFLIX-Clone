const express = require('express');
var router = express.Router();
const categoryController = require('../controllers/category');
const auth = require('../middleware/auth');

router
  .route('/')
  .get(auth.verifyToken, categoryController.getCategories)
  .post(auth.verifyManagerToken, categoryController.createCategory);

router
  .route('/:id')
  .get(auth.verifyToken, categoryController.getCategory)
  .patch(auth.verifyManagerToken, categoryController.updateCategory)
  .delete(auth.verifyManagerToken, categoryController.deleteCategory);

module.exports = router;