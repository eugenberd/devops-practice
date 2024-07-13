import pytest
from app import app

@pytest.fixture
def client():
    app.config['TESTING'] = True
    with app.test_client() as client:
        yield client

def test_index(client):
    """Тест главной страницы"""
    rv = client.get('/')
    assert rv.status_code == 200
    assert b'Products' in rv.data

def test_create_order(client):
    """Тест создания заказа"""
    rv = client.post('/orders', json={'product_ids': [1, 2]})
    assert rv.status_code == 201
    json_data = rv.get_json()
    assert 'order_id' in json_data

def test_create_order_invalid_product(client):
    """Тест создания заказа с неверным продуктом"""
    rv = client.post('/orders', json={'product_ids': [999]})
    assert rv.status_code == 400
    json_data = rv.get_json()
    assert 'error' in json_data

def test_get_order(client):
    """Тест получения информации о заказе"""
    rv = client.post('/orders', json={'product_ids': [1]})
    order_id = rv.get_json()['order_id']

    rv = client.get(f'/orders/{order_id}')
    assert rv.status_code == 200
    json_data = rv.get_json()
    assert json_data['order_id'] == order_id
    assert len(json_data['products']) == 1

def test_get_order_not_found(client):
    """Тест получения информации о несуществующем заказе"""
    rv = client.get('/orders/999')
    assert rv.status_code == 404
    json_data = rv.get_json()
    assert 'error' in json_data
