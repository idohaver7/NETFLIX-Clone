const userService = require('../services/user');
const path = require('path');
const fs = require('fs');

const createUser = async (req, res) => {
  try {
      const { email, password, name } = req.body;
      let profilePicture = req.file ? req.file.path : null; // path where the image is saved

      if (!email || !password) {
          return res.status(400).json({ errors: ['Email and Password are required'] });
      }

      const user = await userService.createUser(email, password, profilePicture, name);
      res.status(201).json(user);
  } catch (error) {
      res.status(400).json({ errors: ['Bad Request'], details: error.message });
  }
};


const getUser = async (req, res) => {
    try {
        const id  = req.userId;
        const user = await userService.getUserById(id);
        if (!user) {
            return res.status(404).json({ errors: ['User not found'] });
        }
        const imagePath = path.join(__dirname, `../public/profile/${user.email}`);

        fs.readFile(imagePath, (err, data) => {
          if (err) {
            console.log("Failed to load image, sending user data without image");
            return res.status(200).json(user); 
          }
          const imageBase64 = Buffer.from(data).toString('base64');
          user.image = `data:image/jpg;base64,${imageBase64}`
    
          return res.status(200).json(user);
        });
    } catch (error) {
        res.status(400).json({ errors: ['Bad Request'], details: error.message });
    }
}

module.exports = { createUser, getUser }
