# 📚 LMS Project — Learning Management System

A RESTful Learning Management System backend built with **Spring Boot**, featuring JWT authentication, role-based access control, course management, assessments, and more.

> 🎓 Group project by Aslan, Mikayil, Yunis [ItSkillSprint-LMS-Group-Project](https://github.com/ItSkillSprint-LMS-Group-Project)

---

## 🚀 Tech Stack

| Layer | Technology |
|-------|-----------|
| Language | Java 21 |
| Framework | Spring Boot 4.0.5 |
| Security | Spring Security + JWT (jjwt 0.11.5) |
| Persistence | Spring Data JPA + Hibernate |
| Database | MySQL |
| Validation | Spring Boot Validation (Jakarta) |
| API Docs | SpringDoc OpenAPI (Swagger UI) |
| Build Tool | Maven |
| Utilities | Lombok |

---

## 📐 Architecture

The project follows a layered architecture, organized into **domain modules**:

```
src/main/java/com/example/lmsproject/
├── assessment/       # Quizzes, questions, options, submissions
├── assignment/       # Teacher assignments & student submissions
├── content/          # Course content (videos, documents, etc.)
├── course/           # Course management
├── enrollment/       # Student enrollment logic
├── exception/        # Global exception handling
├── security/         # JWT auth filter, security config, user details
└── user/             # User registration, login, roles
```

Each module contains:
- `controller/` — REST endpoints
- `service/` — Business logic
- `repository/` — JPA repositories
- `entity/` — JPA entities
- `dto/` — Request & response DTOs
- `mapper/` — Entity ↔ DTO mappers

---

## 🔐 Authentication & Roles

The API uses **JWT Bearer Token** authentication. Three roles are supported:

| Role | Access |
|------|--------|
| `STUDENT` | Browse and enroll in courses, submit assignments & assessments |
| `TEACHER` | Create and manage courses, content, assignments, and assessments |
| `ADMIN` | Full access to all resources |

**Auth endpoints** (`/api/auth/**`) are publicly accessible. All other endpoints require a valid JWT.

---

## 📡 API Endpoints

### Authentication
| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/auth/register` | Register a new user |
| POST | `/api/auth/login` | Login and receive JWT |

### Courses
| Method | Endpoint | Role | Description |
|--------|----------|------|-------------|
| POST | `/api/courses` | TEACHER | Create a course |
| GET | `/api/courses/my` | TEACHER | Get own courses |
| GET | `/api/courses/{id}` | TEACHER/ADMIN | Get course by ID |
| GET | `/api/courses` | ADMIN | Get all courses |
| PATCH | `/api/courses/{id}` | TEACHER/ADMIN | Update a course |
| DELETE | `/api/courses/{id}` | TEACHER/ADMIN | Delete a course |

### Enrollment
| Method | Endpoint | Role | Description |
|--------|----------|------|-------------|
| POST | `/api/enrollments/enroll-by-code` | STUDENT | Enroll via course code |
| POST | `/api/enrollments/courses/{courseId}/enroll-by-email` | TEACHER | Enroll student by email |
| GET | `/api/enrollments/me` | STUDENT | Get own enrollments |
| GET | `/api/enrollments/students` | TEACHER | Get enrolled students |

### Content
| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/...` | Upload/create content for a course |
| GET | `/api/...` | Retrieve course content |
| PATCH | `/api/...` | Update content |
| DELETE | `/api/...` | Delete content |

### Assignments
| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/...` | Create assignment (TEACHER) |
| GET | `/api/...` | Get assignment details |
| PATCH | `/api/...` | Update assignment |
| DELETE | `/api/...` | Delete assignment |

### Assessments (Quizzes)
| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/assessments` | Create assessment (TEACHER) |
| POST | `/api/questions` | Add questions |
| POST | `/api/options` | Add answer options |
| POST | `/api/submissions` | Submit assessment (STUDENT) |
| GET | `/api/submissions` | View submission results |

> 📖 Full interactive API documentation available at **`/swagger-ui.html`** once the app is running.

---

## ⚙️ Getting Started

### Prerequisites

- Java 21+
- Maven 3.8+
- MySQL 8+

### 1. Clone the repository

```bash
git clone https://github.com/ItSkillSprint-LMS-Group-Project/lms-project.git
cd lms-project
```

### 2. Set up the database

```sql
CREATE DATABASE lmsprojectdb;
```

### 3. Configure environment

Edit `src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/lmsprojectdb
spring.datasource.username=<your_mysql_username>
spring.datasource.password=<your_mysql_password>

jwt.secret=<your_jwt_secret_key>
jwt.expiration=86400000
```

> ⚠️ **Never commit real credentials.** Use environment variables or a `.env` file in production.

### 4. Build and run

```bash
./mvnw spring-boot:run
```

The application will start at `http://localhost:8080`.

---

## 📖 API Documentation

Swagger UI is available at:

```
http://localhost:8080/swagger-ui.html
```

OpenAPI JSON spec:

```
http://localhost:8080/v3/api-docs
```

---

## 🗂️ Project Structure

```
lms-project/
├── src/
│   ├── main/
│   │   ├── java/com/example/lmsproject/
│   │   └── resources/
│   │       └── application.properties
│   └── test/
├── pom.xml
└── mvnw / mvnw.cmd
```

---

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/your-feature`)
3. Commit your changes (`git commit -m 'Add some feature'`)
4. Push to the branch (`git push origin feature/your-feature`)
5. Open a Pull Request

---

## 👥 Team

This project was built as a group project under the **ItSkillSprint** program.

## 🔗 LinkedIn

👤 [Aslan Mammadzada](https://www.linkedin.com/in/aslan-mammadzada/)  
👤 [Mikayil Guliyev](https://www.linkedin.com/in/mikay%C4%B1l-quliyev-341275306/)
👤 [Yunis Sadig](https://www.linkedin.com/in/yunis-sadiq-5a99b930b/)

## 📝 Task Management

We used Jira to manage tasks among ourselves.
https://mammadzadaaslan05.atlassian.net/jira/software/projects/LMS/boards/1
