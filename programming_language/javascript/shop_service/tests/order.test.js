const request = require('supertest');
const mongoose = require('mongoose');
const app = require('../app');
const Product = require('../models/product');
const Order = require('../models/order');

beforeAll(async () => {
    await mongoose.connect('mongodb://localhost/shop_service_test', {
        useNewUrlParser: true,
        useUnifiedTopology: true
    });
});

afterAll(async () => {
    await mongoose.connection.close();
});

beforeEach(async () => {
    await Product.deleteMany({});
    await Order.deleteMany({});
    
    const product1 = new Product({ name: 'Laptop', price: 1500 });
    const product2 = new Product({ name: 'Smartphone', price: 700 });
    await product1.save();
    await product2.save();
});

describe('Order API', () => {
    it('should create an order', async () => {
        const product1 = await Product.findOne({ name: 'Laptop' });
        const product2 = await Product.findOne({ name: 'Smartphone' });
        
        const response = await request(app)
            .post('/orders')
            .send({ productIds: [product1._id, product2._id] })
            .expect(201);
        
        expect(response.body.products.length).toBe(2);
        expect(response.body.products[0].name).toBe('Laptop');
        expect(response.body.products[1].name).toBe('Smartphone');
    });

    it('should get an order', async () => {
        const product1 = await Product.findOne({ name: 'Laptop' });
        const product2 = await Product.findOne({ name: 'Smartphone' });
        
        let order = new Order({ products: [product1._id, product2._id] });
        order = await order.save();
        
        const response = await request(app)
            .get(`/orders/${order._id}`)
            .expect(200);
        
        expect(response.body.products.length).toBe(2);
        expect(response.body.products[0].name).toBe('Laptop');
        expect(response.body.products[1].name).toBe('Smartphone');
    });

    it('should return 404 for non-existent order', async () => {
        await request(app)
            .get('/orders/60c72b2f9b1e8a3a12345678')
            .expect(404);
    });
});
