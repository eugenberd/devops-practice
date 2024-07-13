const orderService = require('../services/ orderService');

const createOrder = async (req, res) => {
    try {
        const order = await orderService.createOrder(req.body.productIds);
        res.status(201).json(order);
    } catch (error) {
        res.status(500).json({ error: error.message });
    }
};

const getOrder = async (req, res) => {
    try {
        const order = await orderService.getOrder(req.params.orderId);
        if (!order) {
            return res.status(404).json({ error: 'Order not found' });
        }
        res.status(200).json(order);
    } catch (error) {
        res.status(500).json({ error: error.message });
    }
};

module.exports = {
    createOrder,
    getOrder
};
