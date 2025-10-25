# Task Manager Application with Cucumber Testing

A Spring Boot-based Task Manager application with BDD testing using Cucumber.

## Features

- User authentication (Sign up/Sign in)
- Task management (Create, Read, Update, Delete)
- H2 in-memory database
- RESTful API endpoints
- BDD testing with Cucumber
- Spring Security integration
- Thymeleaf templates for the frontend

## Prerequisites

- Java 17 or higher
- Maven 3.6.3 or higher
- Git (optional)

## Getting Started

### Clone the Repository
```bash
git clone https://github.com/ja390/Task-Manager-Cucumber-Testing.git
cd taskmanager-cucumber-demo/taskmanager-cucumber-demo
```

### Build the Application
```bash
mvn clean install
```

### Run the Application
```bash
mvn spring-boot:run
```

The application will be available at: http://localhost:8081

## Testing

### Run Unit Tests
```bash
mvn test
```

### Run Cucumber Tests
```bash
mvn test -Dcucumber.filter.tags="@Cucumber"
```

## Database
- The application uses H2 in-memory database
- Access the H2 Console at: http://localhost:8081/h2-console
- JDBC URL: jdbc:h2:mem:taskdb
- Username: sa
- Password: (leave empty)

## API Endpoints

### Authentication
- `POST /register` - Register a new user
- `POST /login` - Authenticate user
- `POST /logout` - Logout current user

### Tasks
- `GET /tasks` - Get all tasks for current user
- `POST /tasks` - Create a new task
- `GET /tasks/{id}` - Get a specific task
- `PUT /tasks/{id}` - Update a task
- `DELETE /tasks/{id}` - Delete a task

## Project Structure

```
src/
├── main/
│   ├── java/com/example/taskmanager/
│   │   ├── controller/    # Request handlers
│   │   ├── model/         # Data models
│   │   ├── repo/          # Repository interfaces
│   │   ├── TaskManagerApplication.java
│   │   └── WebConfig.java
│   └── resources/        # Configurations and static resources
└── test/
    └── java/com/example/taskmanager/
        ├── steps/        # Cucumber step definitions
        └── support/      # Test support classes
```

## Technologies Used

- **Backend**: Spring Boot 3.x
- **Frontend**: Thymeleaf, HTML, CSS, JavaScript
- **Database**: H2 Database
- **Testing**: JUnit 5, Cucumber, Spring Test
- **Build Tool**: Maven
- **Security**: Spring Security


