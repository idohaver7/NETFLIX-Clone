const User = require('../models/User');

const createUser = async (email, password, profilePicture, name) => {
    const user = new User({ email, password, profilePicture, name });
    await user.save();
    return user;
}

const getUserById = async (id) => {
    try {
        const user = await User.findById(id);
        return user
    } catch (error) {
        console.error(error);
    }
}

module.exports = { createUser, getUserById };
