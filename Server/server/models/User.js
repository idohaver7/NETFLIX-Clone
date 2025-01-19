const mongoose = require('mongoose');
const Schema = mongoose.Schema;

const userSchema = new Schema({
    email: { type: String, requireed: true, unique: true },
    password: { type: String, required: true },
    profilePicture: { type: String },
    name: { type: String },
    age: { type: Number },
    membership: { type: String },
    watchedMovies: [{ type: mongoose.Schema.Types.ObjectId, ref: 'Movie'},]
})

module.exports = mongoose.model('User', userSchema);
