const express = require('express');
const bodyParser = require('body-parser');
const mongoose = require('mongoose');
const orderRoutes = require('./routes/orderRoutes');

const app = express();

mongoose.connect('mongodb://localhost/shop_service', {
    useNewUrlParser: true,
    useUnifiedTopology: true
});

app.use(bodyParser.json());
app.use('/orders', orderRoutes);

module.exports = app;
