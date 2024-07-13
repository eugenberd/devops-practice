const Order = require('../models/order');
const Product = require('../models/product');

const createOrder = async (productIds) => {
    const products = await Product.find({ '_id': { $in: productIds } });
    const order = new Order({ products });
    return await order.save();
};

const getOrder = async (orderId) => {
    return await Order.findById(orderId).populate('products');
};

module.exports = {
    createOrder,
    getOrder
};
