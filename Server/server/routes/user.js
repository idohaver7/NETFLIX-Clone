const express = require('express');
const router = express.Router();
const multer = require('multer');
const path = require('path');
const userController = require('../controllers/user');
const auth = require('../middleware/auth');

// Configure storage for multer
const storage = multer.diskStorage({
    destination: function(req, file, cb) {
        cb(null, './public/profile/'); // Adjust the path as necessary
    },
    filename: function(req, file, cb) {
            cb(null, req.body.email + path.extname(file.originalname)); // Use email as filename
        }
    
});

const upload = multer({ storage: storage });

router.post('/', upload.single('profilePicture'), userController.createUser);
router.get('/', auth.verifyToken, userController.getUser);

module.exports = router;

