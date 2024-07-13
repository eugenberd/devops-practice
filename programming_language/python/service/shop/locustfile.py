from locust import HttpUser, TaskSet, task, between

class UserBehavior(TaskSet):

    @task(1)
    def index(self):
        self.client.get("/")

    @task(2)
    def create_order(self):
        self.client.post("/orders", json={"product_ids": [1, 2]})

    @task(1)
    def get_order(self):
        # Сначала создаем заказ, чтобы получить order_id
        response = self.client.post("/orders", json={"product_ids": [1]})
        order_id = response.json().get("order_id")
        if order_id:
            self.client.get(f"/orders/{order_id}")

class WebsiteUser(HttpUser):
    tasks = [UserBehavior]
    wait_time = between(1, 5)
