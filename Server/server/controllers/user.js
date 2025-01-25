const userService = require('../services/user');

const createUser = async (req, res) => {
    try {
      const { email, password, profilePicture, name } = req.body;
  
      if (!email || !password) {
        return res.status(400).json({ errors: ['Email, Password are required'] });
      }
  
      const user = await userService.createUser(email, password, profilePicture, name);
      res.status(201).json(user);
    } catch (error) {
      res.status(400).json({ errors: ['Bad Request'], details: error.message });
    }
}

const getUser = async (req, res) => {
    try {
        const id  = req.userId;
        const user = await userService.getUserById(id);
        if (!user) {
            return res.status(404).json({ errors: ['User not found'] });
        }
        res.status(200).json(user);
    } catch (error) {
        res.status(400).json({ errors: ['Bad Request'], details: error.message });
    }
}

module.exports = { createUser, getUser }
