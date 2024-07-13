class Product:
    def __init__(self, id, name, price):
        self.id = id
        self.name = name
        self.price = price

class Order:
    def __init__(self, id, products):
        self.id = id
        self.products = products

# Храним товары и заказы в памяти (для простоты)
products = {
    1: Product(1, "Laptop", 1500),
    2: Product(2, "Smartphone", 700),
}

orders = {}
next_order_id = 1
