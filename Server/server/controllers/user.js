const userService = require('../services/user');
const path = require('path');
const fs = require('fs');

const createUser = async (req, res) => {
  try {
      const { email, password, name } = req.body;
      console.log(req.body)
      console.log(req.file)
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
        let user = await userService.getUserById(id);
        if (!user) {
            return res.status(404).json({ errors: ['User not found'] });
        }
        return res.status(200).json(user);

    } catch (error) {
        res.status(400).json({ errors: ['Bad Request'], details: error.message });
    }
}

module.exports = { createUser, getUser }
