from locust import HttpUser, task, between
import uuid
import logging

class UserBehavior(HttpUser):
    wait_time = between(1, 5)  # Wait time between tasks

    def on_start(self):
        self.client.verify = False  # Disable SSL verification if needed
        self.cert = ("www.dev.sosnovich.com_r3_.cer",
                     "private_key.key")
        self.admin_auth()

    def admin_auth(self):
        response = self.client.post("/auth/login", json={
            "email": "user3@example.com",
            "password": "SQDJok-aUB8BfmaN"
        }, cert=self.cert)
        logging.info(f"Auth response status code: {response.status_code}")
        logging.info(f"Auth response content: {response.text}")
        if response.status_code == 200:
            self.admin_token = response.json()["token"]
            self.csrf_token = response.headers.get('X-XSRF-TOKEN')
            self.client.headers.update({
                "Authorization": f"Bearer {self.admin_token}",
                "X-XSRF-TOKEN": self.csrf_token
            })
        else:
            raise Exception("Failed to authenticate admin user")

    def refresh_csrf_token(self):
        response = self.client.post("/auth/login", json={
            "email": "user3@example.com",
            "password": "SQDJok-aUB8BfmaN"
        }, cert=self.cert)
        logging.info(f"CSRF refresh response status code: {response.status_code}")
        logging.info(f"CSRF refresh response content: {response.text}")
        if response.status_code == 200:
            self.csrf_token = response.headers.get('X-XSRF-TOKEN')
            self.client.headers.update({
                "Authorization": f"Bearer {self.admin_token}",
                "X-XSRF-TOKEN": self.csrf_token
            })
        else:
            raise Exception("Failed to refresh CSRF token")

    def generate_unique_email(self):
        return f"testuser_{uuid.uuid4()}@example.com"

    @task
    def create_user(self):
        self.refresh_csrf_token()
        email = self.generate_unique_email()
        payload = {
            "email": email,
            "password": "newpassword123",
            "name": "New User"
        }
        response = self.client.post("/users", json=payload, cert=self.cert)
        if response.status_code == 201:
            self.test_user_id = response.json()["id"]
        else:
            raise Exception("Failed to create user")

    @task
    def get_user(self):
        self.refresh_csrf_token()
        if hasattr(self, 'test_user_id'):
            user_url = f"/users/{self.test_user_id}"
            response = self.client.get(user_url, cert=self.cert)
            if response.status_code != 200:
                raise Exception("Failed to get user")

    @task
    def delete_user(self):
        self.refresh_csrf_token()
        if hasattr(self, 'test_user_id'):
            user_url = f"/users/{self.test_user_id}"
            response = self.client.delete(user_url, cert=self.cert)
            if response.status_code != 204:
                raise Exception("Failed to delete user")

    @task
    def add_external_project(self):
        self.refresh_csrf_token()
        if hasattr(self, 'test_user_id'):
            project_payload = {
                "id": str(uuid.uuid4()),
                "name": f"New {uuid.uuid4().__str__().upper().replace('-', '')}"
            }
            projects_url = f"/users/{self.test_user_id}/projects"
            response = self.client.post(projects_url, json=project_payload, cert=self.cert)
            if response.status_code != 201:
                raise Exception("Failed to add external project")

    @task
    def get_external_projects(self):
        self.refresh_csrf_token()
        if hasattr(self, 'test_user_id'):
            projects_url = f"/users/{self.test_user_id}/projects"
            response = self.client.get(projects_url, cert=self.cert)
            if response.status_code != 200:
                raise Exception("Failed to get external projects")

    @task
    def update_user(self):
        self.refresh_csrf_token()
        if hasattr(self, 'test_user_id'):
            update_data = {
                "email": "updateduser@example.com",
                "password": "updatedpassword123",
                "name": "Updated User"
            }
            response = self.client.put(f"/users/{self.test_user_id}", json=update_data, cert=self.cert)
            if response.status_code != 200:
                raise Exception("Failed to update user")

if __name__ == "__main__":
    import os
    os.system("locust -f locustfile.py --host=https://127.0.0.1:8080 --web-host=127.0.0.1 --web-port=8089")
