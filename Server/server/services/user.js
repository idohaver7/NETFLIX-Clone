const User = require('../models/User');

const createUser = async (email, password, profilePicture, name, age, membership) => {
    const user = new User({ email, password, profilePicture, name, age, membership });
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
