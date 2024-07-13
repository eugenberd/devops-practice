# app.py
from flask import Flask, request, jsonify, render_template
from models import products, orders, Order, next_order_id

app = Flask(__name__)

@app.route('/')
def index():
    return render_template('index.html', products=products.values())

@app.route('/orders', methods=['POST'])
def create_order():
    global next_order_id

    product_ids = request.json.get('product_ids')
    if not product_ids:
        return jsonify({"error": "No product ids provided"}), 400

    selected_products = [products[id] for id in product_ids if id in products]

    if not selected_products:
        return jsonify({"error": "No valid products found"}), 400

    order = Order(next_order_id, selected_products)
    orders[next_order_id] = order
    next_order_id += 1

    return jsonify({"order_id": order.id}), 201

@app.route('/orders/<int:order_id>', methods=['GET'])
def get_order(order_id):
    order = orders.get(order_id)
    if not order:
        return jsonify({"error": "Order not found"}), 404

    return jsonify({
        "order_id": order.id,
        "products": [{"id": p.id, "name": p.name, "price": p.price} for p in order.products]
    })

if __name__ == '__main__':
    app.run(debug=True)