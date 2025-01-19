const express    = require('express');
const bodyParser = require('body-parser');
const cors       = require('cors');
const mongoose   = require('mongoose');
const categories = require('./routes/category');
const movies     = require('./routes/movies')
const users      = require('./routes/user')

const User = require('./models/User');

require('custom-env').env(process.env.NODE_ENV, './config');
mongoose.connect(process.env.CONNECTION_STRING, {});

var app = express();
app.use(cors());
app.use(bodyParser.urlencoded({ extended: true }));
app.use(express.json());

app.use('/api/categories', categories)
app.use('/api/movies', movies)
app.use('/api/users', users)

app.post('/api/tokens', async (req, res) => {
    try {
        const user = await User.findOne({ email: req.body.email, password: req.body.password })
        if (!user) res.status(400).send('Incorrect credentials')
        else res.status(200).send(user._id)
    } catch (error) {
        console.log(error)
        res.status(400).send('Incorrect credentials')
    }
})

app.listen(process.env.PORT);
