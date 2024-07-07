import uuid
import jwt
import requests
import unittest

USER_EXAMPLE_COM = "user3@example.com"

NEW_USER_ = "New User 2"

BASE_URL = "https://127.0.0.1:8080/api"
cert_path = "www.dev.sosnovich.com_r3_.cer"
private_key = "private_key.key"


class CustomSession(requests.Session):
    pass


class TestUserProjectManagementAPI(unittest.TestCase):

    @classmethod
    def setUpClass(cls):
        cls.base_url = BASE_URL
        cls.auth_url = f"{cls.base_url}/auth/login"
        cls.users_url = f"{cls.base_url}/users"
        cls.session = CustomSession()
        cls.csrf_token = None
        cls.csrf_header_name = "X-XSRF-TOKEN"

        # Authenticate as admin user to get admin token and CSRF token
        cls.class_authenticate_admin()

    @classmethod
    def class_authenticate_admin(cls):
        admin_payload = {
            "email": "user3@example.com",
            "password": "SQDJok-aUB8BfmaN"
        }
        admin_response = cls.session.post(cls.auth_url, json=admin_payload, cert=(cert_path, private_key), verify=False)
        assert admin_response.status_code == 200
        cls.admin_token = admin_response.json()["token"]
        cls.csrf_token = admin_response.headers.get('X-XSRF-TOKEN')
        cls.admin_headers = {
            'Content-Type': 'application/json',
            'Authorization': f'Bearer {cls.admin_token}',
            cls.csrf_header_name: cls.csrf_token
        }

    def setUp(self):
        self.instance_authenticate_admin()

    def instance_authenticate_admin(self):
        payload = {
            "email": USER_EXAMPLE_COM,
            "password": "SQDJok-aUB8BfmaN"
        }
        response = self.session.post(self.auth_url, json=payload, cert=(cert_path, private_key), verify=False)
        self.assertEqual(200, response.status_code)
        self.admin_token = response.json()["token"]
        self.csrf_token = response.headers.get('X-XSRF-TOKEN')
        self.admin_headers = {
            'Content-Type': 'application/json',
            'Authorization': f'Bearer {self.admin_token}',
            self.csrf_header_name: self.csrf_token
        }

    def refresh_csrf_token(self):
        response = self.session.post(self.auth_url, json={"email": "user3@example.com", "password": "SQDJok-aUB8BfmaN"},
                                     cert=(cert_path, private_key), verify=False)
        if response.status_code == 200:
            self.csrf_token = response.headers.get('X-XSRF-TOKEN')
            self.admin_headers[self.csrf_header_name] = self.csrf_token

    def create_user(self, email, password, name):
        self.refresh_csrf_token()
        new_user_payload = {
            "email": email,
            "password": password,
            "name": name
        }
        response = self.session.post(self.users_url, json=new_user_payload, headers=self.admin_headers,
                                     cert=(cert_path, private_key), verify=False)
        self.assertEqual(201, response.status_code)
        user_id = response.json()["id"]
        return user_id

    def delete_user(self, user_id):
        self.refresh_csrf_token()
        user_url = f"{self.users_url}/{user_id}"
        response = self.session.delete(user_url, headers=self.admin_headers, cert=(cert_path, private_key),
                                       verify=False)
        self.assertEqual(204, response.status_code)

    def tearDown(self):
        # Ensure that only non-admin users created during the tests are deleted
        if hasattr(self, 'test_user_id') and self.test_user_id:
            self.delete_user(self.test_user_id)
            self.test_user_id = None

    def generate_unique_email(self):
        return f"testuser_{uuid.uuid4()}@example.com"

    def test_create_user_success(self):
        self.test_user_id = self.create_user(self.generate_unique_email(), "newpassword123", NEW_USER_)
        self.assertIsNotNone(self.test_user_id)

    def test_create_user_email_in_use(self):
        new_user_payload = {
            "email": "user1@example.com",
            "password": "newpassword123",
            "name": "New User 2"
        }
        response = self.session.post(self.users_url, json=new_user_payload, headers=self.admin_headers,
                                     cert=(cert_path, private_key), verify=False)
        self.assertEqual(409, response.status_code)
        self.assertEqual("Email [user1@example.com] already in use", response.json()["error"])

    def test_create_user_invalid_input(self):
        new_user_payload = {
            "email": "newuser22example.com",  # Invalid email format
            "password": "newpassword123",
            "name": "New User 2"
        }
        response = self.session.post(self.users_url, json=new_user_payload, headers=self.admin_headers,
                                     cert=(cert_path, private_key), verify=False)
        self.assertEqual(400, response.status_code)
        self.assertEqual("createUser.newUser.email", response.json()[0]["field"])
        self.assertEqual("must be a well-formed email address", response.json()[0]["message"])
        self.assertEqual("newuser22example.com", response.json()[0]["rejectedValue"])

    def test_authenticate_user(self):
        self.instance_authenticate_admin()

    def test_authenticate_user_invalid_credentials(self):
        payload = {
            "email": "user1@example.com",
            "password": "wrongpassword"
        }
        response = self.session.post(self.auth_url, json=payload, cert=(cert_path, private_key), verify=False)
        self.assertEqual(401, response.status_code)
        self.assertEqual("Bad credentials", response.json()["error"])

    def test_get_user_by_id(self):
        # Create a new user first
        new_user_email = self.generate_unique_email()
        self.test_user_id = self.create_user(new_user_email, "newpassword123", "New User 2")

        user_url = f"{self.users_url}/{self.test_user_id}"
        response = self.session.get(user_url, headers=self.admin_headers, cert=(cert_path, private_key), verify=False)
        self.assertEqual(200, response.status_code)
        self.assertEqual(new_user_email, response.json()["email"])

    def test_get_user_by_id_not_found(self):
        user_url = f"{self.users_url}/999999"  # Assuming this ID does not exist
        response = self.session.get(user_url, headers=self.admin_headers, cert=(cert_path, private_key), verify=False)
        self.assertEqual(404, response.status_code)
        self.assertEqual("User not found with ID: 999999", response.json()["error"])

    def test_delete_user_as_admin(self):
        # Create a new user first
        new_user_email = self.generate_unique_email()
        new_user_id = self.create_user(new_user_email, "newpassword123", "New User 2")

        # Delete the user as admin
        self.delete_user(new_user_id)

    def test_delete_user_not_found(self):
        user_url = f"{self.users_url}/999999"  # Assuming this ID does not exist
        response = self.session.delete(user_url, headers=self.admin_headers, cert=(cert_path, private_key),
                                       verify=False)
        self.assertEqual(404, response.status_code)
        self.assertEqual("Not Found", response.json()["error"])

    def test_add_external_project_to_user(self):
        self.test_user_id = self.create_user(self.generate_unique_email(), "newpassword123", "New User 3")
        project_payload = {
            "id": str(uuid.uuid4()),
            "name": f"New {uuid.uuid4().__str__().upper().replace('-', '')}"
        }
        projects_url = f"{self.users_url}/{self.test_user_id}/projects"
        self.refresh_csrf_token()
        response = self.session.post(projects_url, json=project_payload, headers=self.admin_headers,
                                     cert=(cert_path, private_key), verify=False)
        self.assertEqual(201, response.status_code)

    def test_add_external_project_invalid_input(self):
        self.test_user_id = self.create_user(self.generate_unique_email(), "newpassword123", "New User 4")
        project_payload = {
            "name": f"New {uuid.uuid4().__str__().upper().replace('-', '')}"
        }  # Missing 'id'
        projects_url = f"{self.users_url}/{self.test_user_id}/projects"
        self.refresh_csrf_token()
        response = self.session.post(projects_url, json=project_payload, headers=self.admin_headers,
                                     cert=(cert_path, private_key), verify=False)
        self.assertEqual(400, response.status_code)
        self.assertEqual(response.json()[0]["field"], "addExternalProject.newExternalProject.id")
        self.assertEqual(response.json()[0]["message"], "must not be null")

    def test_get_external_projects(self):
        # Create a new user and add a project first
        self.test_user_id = self.create_user(self.generate_unique_email(), "newpassword123", "New User 5")
        project_payload = {
            "id": str(uuid.uuid4()),
            "name": f"New {uuid.uuid4().__str__().upper().replace('-', '')}"
        }
        projects_url = f"{self.users_url}/{self.test_user_id}/projects"
        self.refresh_csrf_token()
        response = self.session.post(projects_url, json=project_payload, headers=self.admin_headers,
                                     cert=(cert_path, private_key), verify=False)
        self.assertEqual(201, response.status_code)

        response = self.session.get(projects_url, headers=self.admin_headers, cert=(cert_path, private_key),
                                    verify=False)
        self.assertEqual(200, response.status_code)
        self.assertIsInstance(response.json(), list)

    def test_get_external_projects_user_not_found(self):
        projects_url = f"{self.users_url}/999999/projects"  # Assuming this user ID does not exist
        response = self.session.get(projects_url, headers=self.admin_headers, cert=(cert_path, private_key),
                                    verify=False)
        self.assertEqual(404, response.status_code)
        self.assertEqual("User not found with ID: 999999", response.json()["error"])


    def test_update_user_success(self):
        new_user_email = self.generate_unique_email()
        self.test_user_id = self.create_user(new_user_email, "newpassword123", "New User")
        update_data = {
            "email": "updateduser@example.com",
            "password": "updatedpassword123",
            "name": "Updated User"
        }
        response = self.session.put(f"{self.users_url}/{self.test_user_id}", json=update_data, headers=self.admin_headers,
                                    cert=(cert_path, private_key), verify=False)
        self.assertEqual(response.status_code, 200)


    def test_update_user_not_found(self):
        update_data = {
            "email": "nonexistinguser@example.com",
            "password": "nonexistingpassword123",
            "name": "Non-existing User"
        }
        response = self.session.put(f"{self.users_url}/9999", json=update_data, headers=self.admin_headers,
                                    cert=(cert_path, private_key), verify=False)
        self.assertEqual(response.status_code, 404)
        error_message = response.json()
        self.assertEqual(error_message["error"], "User not found with ID: 9999")


    def test_update_user_invalid_input(self):
        new_user_email = self.generate_unique_email()
        self.test_user_id = self.create_user(new_user_email, "newpassword123", "New User")
        update_data = {
            "email": "invalid-email",
            "password": "updatedpassword123",
            "name": "Invalid User"
        }
        response = self.session.put(f"{self.users_url}/{self.test_user_id}", json=update_data, headers=self.admin_headers,
                                    cert=(cert_path, private_key), verify=False)
        self.assertEqual(400,response.status_code )
        error_message = response.json()
        self.assertIn("must be a well-formed email address", error_message[0]["message"])


if __name__ == '__main__':
    unittest.main()
