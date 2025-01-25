const express = require('express');
const router = express.Router();
const userController = require('../controllers/user');
const auth = require('../middleware/auth');

router.post('/', userController.createUser)
router.get('/', auth.verifyToken, userController.getUser)

module.exports = router;
