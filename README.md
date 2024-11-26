# Task Management System

## Prerequisites

Before you begin, ensure you have the following installed on your machine:

- Java 17 or higher
- Maven 3.6.3 or higher
- An IDE such as IntelliJ IDEA
- MySQL 8.0 or higher

## Setup

1. **Clone the repository:**

   ```sh
   git clone https://github.com/TheMarvelFan/task-management-system.git
   cd task-management-system
   ```
   
2. **Create Database and Tables:**
    
   Create a database named `task_management` in MySQL and run the following SQL queries to create the tables.

   ```sql
   CREATE TABLE users (
       id BIGINT AUTO_INCREMENT PRIMARY KEY,
       email VARCHAR(255) NOT NULL UNIQUE,
       username VARCHAR(255) NOT NULL UNIQUE,
       password VARCHAR(255) NOT NULL
   );

   CREATE TABLE tasks (
       id BIGINT AUTO_INCREMENT PRIMARY KEY,
       title VARCHAR(255) NOT NULL,
       description TEXT,
       due_date DATE,
       priority ENUM('LOW', 'MEDIUM', 'HIGH') DEFAULT 'LOW',
       status ENUM('PENDING', 'COMPLETED') DEFAULT 'PENDING',
       created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
       updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
       user_id BIGINT, is_deleted TINYINT(1) DEFAULT 0,
       FOREIGN KEY (user_id) REFERENCES users(id)
   );

   CREATE TABLE subtasks (
       id BIGINT AUTO_INCREMENT PRIMARY KEY,
       description TEXT,
       status ENUM('PENDING', 'COMPLETED') DEFAULT 'PENDING',
       task_id BIGINT, is_deleted BIT DEFAULT 0,
       FOREIGN KEY (task_id) REFERENCES tasks(id)
   );
   ```

3. **Build the project:**

   ```sh
   mvn clean install
   ```

4. **Configure the application properties:**

   Update the `src/main/resources/application.properties` file with your database and JWT configurations. I am including my own application.properties file for reference.

   ```properties
   jwt.expiration=3600000
   jwt.secret=your_secret_key
   spring.datasource.password=yourpassword
   spring.datasource.url=jdbc:mysql://localhost:3306/task_management
   spring.datasource.username=root
   ```

5. **Run the application:**

   ```sh
   mvn spring-boot:run
   ```

## Endpoints

### Authentication

#### Register
- **URL:** `/api/auth/register`
- **Method:** `POST`
- **Headers:** `Content-Type: application/json`
- **Body:**
  ```json
  {
    "email": "user@example.com",
    "username": "user",
    "password": "password"
  }
  ```
  This will return a JWT token which you can use to task and subtask endpoints.


#### Login
- **URL:** `/api/auth/login`
- **Method:** `POST`
- **Headers:** `Content-Type: application/json`
- **Body:**
  ```json
  {
    "emailOrUsername": "user@example.com",
    "password": "password"
  }
  ```
  This will return a JWT token which you can use to task and subtask endpoints.

### Tasks

#### Create Task

- **URL:** `/api/tasks`
- **Method:** `POST`
- **Headers:** `Content-Type: application/json`, `Authorization: Bearer <token>`
- **Body:**
  ```json
  {
    "title": "New Task",
    "description": "Task description",
    "dueDate": "2023-12-31",
    "priority": "HIGH"
  }
  ```

#### Update Task

- **URL:** `/api/tasks/{taskId}`
- **Method:** `PUT`
- **Headers:** `Content-Type: application/json`, `Authorization: Bearer <token>`
- **Body:**
  ```json
  {
    "title": "Updated Task",
    "description": "Updated description",
    "dueDate": "2023-12-31",
    "priority": "MEDIUM"
  }
  ```

#### Delete Task

- **URL:** `/api/tasks/{taskId}`
- **Method:** `DELETE`
- **Headers:** `Authorization: Bearer <token>`

#### Get Task by ID

- **URL:** `/api/tasks/{taskId}`
- **Method:** `GET`
- **Headers:** `Authorization: Bearer <token>`

#### Get All Tasks

- **URL:** `/api/tasks`
- **Method:** `GET`
- **Headers:** `Authorization: Bearer <token>`
- **Params:**
    - `priority` (optional)
    - `dueDate` (optional)
    - `subtasksCount` (optional)
    - `title` (optional)
    - `status` (optional)
    - `createdAt` (optional)
    - `updatedAt` (optional)
    - `page` (default: 0)
    - `size` (default: 10)

### SubTasks

#### Create SubTask

- **URL:** `/api/subtasks/task/{taskId}`
- **Method:** `POST`
- **Headers:** `Content-Type: application/json`, `Authorization: Bearer <token>`
- **Body:**
  ```json
  {
    "description": "SubTask description"
  }
  ```

#### Update SubTask Status

- **URL:** `/api/subtasks/{subTaskId}/status/{status}`
- **Method:** `PUT`
- **Headers:** `Authorization: Bearer <token>`

#### Delete SubTask

- **URL:** `/api/subtasks/{subTaskId}`
- **Method:** `DELETE`
- **Headers:** `Authorization: Bearer <token>`

#### Get SubTask by ID

- **URL:** `/api/subtasks/{subTaskId}`
- **Method:** `GET`
- **Headers:** `Authorization: Bearer <token>`

#### Get All SubTasks

- **URL:** `/api/subtasks`
- **Method:** `GET`
- **Headers:** `Authorization: Bearer <token>`
- **Params:**
    - `taskId` (optional)
    - `page` (default: 0)
    - `size` (default: 10)

## Testing

You can use tools like Postman or cURL to test the endpoints. Make sure to include the `Authorization` header with the JWT token for protected endpoints.

### Note
Please do not put a trailing slash at the end of the endpoints. For example, use `http://localhost:8080/api/tasks` instead of `http://localhost:8080/api/tasks/`.
