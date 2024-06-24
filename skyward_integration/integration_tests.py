import uuid
import jwt
import requests
import unittest

BASE_URL = "http://127.0.0.1:8080/api"


class TestUserProjectManagementAPI(unittest.TestCase):

    @classmethod
    def setUpClass(cls):
        cls.base_url = BASE_URL
        cls.auth_url = f"{cls.base_url}/auth/login"
        cls.users_url = f"{cls.base_url}/users"
        cls.admin_token = None
        cls.admin_headers = {'Content-Type': 'application/json'}

        # Authenticate as admin user to get admin token
        admin_payload = {
            "email": "user3@example.com",
            "password": "SQDJok-aUB8BfmaN"
        }
        admin_response = requests.post(cls.auth_url, json=admin_payload)
        assert admin_response.status_code == 200
        cls.admin_token = admin_response.json()["token"]
        cls.admin_headers.update({'Authorization': f'Bearer {cls.admin_token}'})

    def setUp(self):
        # Authenticate as an admin user
        self.authenticate_admin()

    def authenticate_admin(self):
        payload = {
            "email": "user3@example.com",
            "password": "SQDJok-aUB8BfmaN"
        }
        response = requests.post(self.auth_url, json=payload)
        self.assertEqual(200, response.status_code)
        self.admin_token = response.json()["token"]
        self.admin_headers = {'Content-Type': 'application/json', 'Authorization': f'Bearer {self.admin_token}'}

    def create_user(self, email, password, name):
        new_user_payload = {
            "email": email,
            "password": password,
            "name": name
        }
        response = requests.post(self.users_url, json=new_user_payload, headers=self.admin_headers)
        self.assertEqual(201, response.status_code)
        user_id = response.json()["id"]
        return user_id

    def delete_user(self, user_id):
        user_url = f"{self.users_url}/{user_id}"
        response = requests.delete(user_url, headers=self.admin_headers)
        self.assertEqual(204, response.status_code)

    def tearDown(self):
        # Ensure that only non-admin users created during the tests are deleted
        if hasattr(self, 'test_user_id') and self.test_user_id:
            self.delete_user(self.test_user_id)
            self.test_user_id = None

    def test_create_user_success(self):
        self.test_user_id = self.create_user("newuser22@example.com", "newpassword123", "New User 2")
        self.assertIsNotNone(self.test_user_id)

    def test_create_user_email_in_use(self):
        new_user_payload = {
            "email": "user1@example.com",
            "password": "newpassword123",
            "name": "New User 2"
        }
        response = requests.post(self.users_url, json=new_user_payload, headers=self.admin_headers)
        self.assertEqual(409, response.status_code)
        self.assertEqual("Email [user1@example.com] already in use", response.json()["error"])

    def test_create_user_invalid_input(self):
        new_user_payload = {
            "email": "newuser22example.com",  # Invalid email format
            "password": "newpassword123",
            "name": "New User 2"
        }
        response = requests.post(self.users_url, json=new_user_payload, headers=self.admin_headers)
        self.assertEqual(400, response.status_code)
        self.assertEqual("Invalid input data", response.json()["error"])

    def test_authenticate_user(self):
        self.authenticate_admin()

    def test_authenticate_user_invalid_credentials(self):
        payload = {
            "email": "user1@example.com",
            "password": "wrongpassword"
        }
        response = requests.post(self.auth_url, json=payload)
        self.assertEqual(401, response.status_code)
        self.assertEqual("Unauthorized", response.json()["error"])

    def test_get_user_by_id(self):
        # Create a new user first
        new_user_email = "newuser22@example.com"
        self.test_user_id = self.create_user(new_user_email, "newpassword123", "New User 2")

        user_url = f"{self.users_url}/{self.test_user_id}"
        response = requests.get(user_url, headers=self.admin_headers)
        self.assertEqual(200, response.status_code)
        self.assertEqual(new_user_email, response.json()["email"])

    def test_get_user_by_id_not_found(self):
        user_url = f"{self.users_url}/999999"  # Assuming this ID does not exist
        response = requests.get(user_url, headers=self.admin_headers)
        self.assertEqual(404, response.status_code)
        self.assertEqual("User not found with ID: 999999", response.json()["error"])

    def test_delete_user_as_admin(self):
        # Create a new user first
        new_user_email = "newuser22@example.com"
        new_user_id = self.create_user(new_user_email, "newpassword123", "New User 2")

        # Delete the user as admin
        self.delete_user(new_user_id)

    def test_delete_user_not_found(self):
        user_url = f"{self.users_url}/999999"  # Assuming this ID does not exist
        response = requests.delete(user_url, headers=self.admin_headers)
        self.assertEqual(404, response.status_code)
        self.assertEqual("Not Found", response.json()["error"])

    def test_add_external_project_to_user(self):
        self.test_user_id = self.create_user("newuser23@example.com", "newpassword123", "New User 3")
        project_payload = {
            "id": str(uuid.uuid4()),
            "name": f"New {uuid.uuid4().__str__().upper().replace('-', '')}"
        }
        projects_url = f"{self.users_url}/{self.test_user_id}/projects"
        response = requests.post(projects_url, json=project_payload, headers=self.admin_headers)
        self.assertEqual(201, response.status_code)
        self.assertIn("id", response.json())

    def test_add_external_project_to_user_invalid_input(self):
        self.test_user_id = self.create_user("newuser24@example.com", "newpassword123", "New User 4")
        project_payload = {
            "name": ""  # Invalid project name
        }
        projects_url = f"{self.users_url}/{self.test_user_id}/projects"
        response = requests.post(projects_url, json=project_payload, headers=self.admin_headers)
        self.assertEqual(400, response.status_code)
        self.assertEqual(response.json()[0]["field"], "addExternalProject.newExternalProject.id")
        self.assertEqual(response.json()[0]["message"], "must not be null")

    def test_get_external_projects(self):
        # Create a new user and add a project first
        self.test_user_id = self.create_user("newuser25@example.com", "newpassword123", "New User 5")
        project_payload = {
            "id": str(uuid.uuid4()),
            "name": f"New {uuid.uuid4().__str__().upper().replace('-', '')}"
        }
        projects_url = f"{self.users_url}/{self.test_user_id}/projects"
        response = requests.post(projects_url, json=project_payload, headers=self.admin_headers)
        self.assertEqual(201, response.status_code)

        response = requests.get(projects_url, headers=self.admin_headers)
        self.assertEqual(200, response.status_code)
        self.assertIsInstance(response.json(), list)

    def test_get_external_projects_user_not_found(self):
        projects_url = f"{self.users_url}/999999/projects"  # Assuming this user ID does not exist
        response = requests.get(projects_url, headers=self.admin_headers)
        self.assertEqual(404, response.status_code)
        self.assertEqual("User not found with ID: 999999", response.json()["error"])


if __name__ == '__main__':
    unittest.main()
