const mongoose = require('mongoose');
const Schema = mongoose.Schema;

const userSchema = new Schema({
    email: { type: String, requireed: true, unique: true },
    password: { type: String, required: true },
    profilePicture: { type: String, default: 'https://wallpapers.com/images/hd/netflix-profile-pictures-1000-x-1000-qo9h82134t9nv0j0.jpg' },
    name: { type: String, required: true },
    watchedMovies: [{ type: mongoose.Schema.Types.ObjectId, ref: 'Movie'}],
    isManager: { type: Boolean, default: false }
})

module.exports = mongoose.model('User', userSchema);
