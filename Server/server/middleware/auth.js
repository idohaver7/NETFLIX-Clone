const userService = require('../services/user');

const isLoggedIn = async (req, res, next) => {
    if (await userService.getUserById(req.headers['userid'])) { 
        next(); 
    } else {
        res.status(401).send('User is not logged in');
    }
}

module.exports = { isLoggedIn };