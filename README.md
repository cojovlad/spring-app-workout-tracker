# GymLog Application

GymLog is a secure and mobile-responsive workout logging web application designed for personal users, trainers, and administrators. This application enables users to log their daily workouts, trainers to assign and manage workouts, and administrators to manage users and roles. The app uses Spring Boot, Thymeleaf, and MySQL, and it is deployed on AWS with stringent security measures including JWT-based authentication and OAuth2 login integration.

---

## Table of Contents

- [Features](#features)
- [Architecture Overview](#architecture-overview)
- [Roles and Permissions](#roles-and-permissions)
- [Technology Stack](#technology-stack)
- [Installation and Setup](#installation-and-setup)
- [Implementation Details](#implementation-details)
- [API Endpoints](#api-endpoints)
- [End-to-End Flow](#end-to-end-flow)
- [Deployment](#deployment)
- [Contributing](#contributing)
- [License](#license)

---

## Features

- **Secure Authentication:** Local login using JWT and social login integration (Google, Facebook) via OAuth2.
- **Workout Logging:** Users can create personal workouts; trainers can assign workouts.
- **Role-Based Access:** Support for three roles (com.example.spring_app_workout_tracker.entity.User, Trainer, Administrator) with cumulative permissions.
- **Real-Time Notifications:** WebSocket-based notifications for workout creation, updates, or trainer removal.
- **Mobile Responsive UI:** Responsive design built with Thymeleaf and Bootstrap.
- **Internationalization (I18n):** Multilingual support with resource bundles (EN, DE, ES).
- **Admin Management:** Role assignment and user management via an Admin Panel.
- **Scalable Architecture:** Deployed on AWS using EC2, RDS, and CloudWatch monitoring.

---

## Architecture Overview

GymLog follows a layered architecture with clearly defined responsibilities:

1. **Domain Layer (Entities):**  
   Java classes annotated with JPA to represent data models such as com.example.spring_app_workout_tracker.entity.User, Role, UserTrainer, Workout, and WorkoutVersion.

2. **Repository Layer:**  
   Interfaces extending Spring Data JPA repositories for data access.

3. **Service Layer:**  
   Contains business logic for handling authentication, workout management, trainer assignments, and notifications.

4. **Controller Layer:**  
   REST controllers and Thymeleaf-based web controllers to handle HTTP requests and render UI views.

5. **Security and Support Layers:**  
   Configurations for JWT, OAuth2, CSRF, CORS, WebSocket messaging, internationalization, and AWS deployment.

---

## Roles and Permissions

- **Administrator:**
    - Manages users and assigns roles.
    - Has access to an Admin Panel with user management and activity audit logs.

- **Trainer:**
    - Can assign workouts to users.
    - Views a list of assigned trainees with day-based management of workout schedules.
    - May also act as a user.

- **com.example.spring_app_workout_tracker.entity.User:**
    - Can create personal workouts.
    - Views daily workouts combining personal and trainer-assigned routines.
    - May have a single active trainer assigned.

---

## Technology Stack

- **Backend:**
    - Java 21, Spring Boot 3.x, Spring Data JPA, Spring Security, OAuth2, JWT, WebSocket (STOMP)
- **Database:**
    - MySQL (RDS on AWS)
- **Frontend:**
    - Thymeleaf templates, Bootstrap 4.5.2
- **Deployment:**
    - AWS (EC2 for application, RDS for MySQL, S3 for assets, CloudWatch for monitoring)

---

## Installation and Setup

1. **Clone the Repository:**

    ```bash
    git clone https://github.com/yourusername/gymlog.git
    cd gymlog
    ```

2. **Configure Database:**

   Update the `application.properties` file with your MySQL connection settings:

    ```properties
    spring.datasource.url=jdbc:mysql://localhost:3306/yourdbname
    spring.datasource.username=yourusername
    spring.datasource.password=yourpassword
    spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
    spring.jpa.database-platform=org.hibernate.dialect.MySQL8Dialect
    spring.jpa.hibernate.ddl-auto=update
    ```

3. **Configure Security and OAuth2 Settings:**

   Uncomment and update security properties in the `application.properties` as needed for JWT and OAuth2 integration.

4. **Run the Application:**

   Use Maven to run the Spring Boot application.

    ```bash
    mvn spring-boot:run
    ```

5. **Access the Application:**

   Open your browser and navigate to `http://localhost:8080` to use the GymLog application.

---

## Implementation Details

### 1. Authentication Module

- **Entities:**
    - `com.example.spring_app_workout_tracker.entity.User` with fields: `Long id`, `String email`, `String passwordHash`, `String name`, `String timezone`, `Set<Role> roles`, `LocalDateTime createdAt`, `LocalDateTime updatedAt`.
    - `Role` with fields: `Long id`, `String roleName`.

- **Repositories:**
    - `UserRepository`: Extending `JpaRepository<com.example.spring_app_workout_tracker.entity.User, Long>`, with methods like `findByEmail(String email)`.
    - `RoleRepository`: Extending `JpaRepository<Role, Long>`.

- **Services:**
    - `UserService`: Methods to register users, retrieve by email, and update login timestamp.
    - `AuthService`: Methods for JWT generation (e.g., `generateToken(UserDetails)`), validation, and refresh.
    - Utility class `JwtUtil` to encapsulate JWT handling logic.

- **Controllers & Security:**
    - `AuthController`: Endpoints such as `POST /auth/login`, `POST /auth/refresh`, and `GET /auth/oauth2/redirect`.
    - `SecurityConfig`: Configures JWT filters, OAuth2 settings, CSRF protection, and CORS policies.

### 2. Role Management & Trainer Assignment

- **Entity:**
    - `UserTrainer` (Mapping): Fields - `Long id`, `com.example.spring_app_workout_tracker.entity.User user`, `com.example.spring_app_workout_tracker.entity.User trainer`, `LocalDate startDate`, `LocalDate endDate`, `boolean active`.

- **Repository:**
    - `UserTrainerRepository`: Extends `JpaRepository<UserTrainer, Long>`, with a method like `findByUserAndActiveTrue(com.example.spring_app_workout_tracker.entity.User user)`.

- **Service:**
    - `TrainerService`: Methods such as `assignTrainer(com.example.spring_app_workout_tracker.entity.User user, com.example.spring_app_workout_tracker.entity.User trainer)`, `removeTrainer(com.example.spring_app_workout_tracker.entity.User user)`, and `getTraineesForTrainer(com.example.spring_app_workout_tracker.entity.User trainer)`.

- **Controller:**
    - `TrainerController`: Endpoints like `POST /trainer/assign`, `POST /trainer/remove`, and `GET /trainer/my-trainees`.

### 3. Workout System

- **Entities:**
    - `Workout`: Fields - `Long id`, `com.example.spring_app_workout_tracker.entity.User user`, `com.example.spring_app_workout_tracker.entity.User createdBy`, `Enum WorkoutType { PERSONAL, TRAINER }`, `LocalDate scheduledDate`, `String content`, `LocalDateTime createdAt`, `LocalDateTime updatedAt`.
    - `WorkoutVersion`: Fields - `Long id`, `Workout workout`, `String diff`, `com.example.spring_app_workout_tracker.entity.User modifiedBy`, `LocalDateTime modifiedAt`.

- **Repositories:**
    - `WorkoutRepository`: Extending `JpaRepository<Workout, Long>` with custom queries such as `findByUserAndScheduledDate(com.example.spring_app_workout_tracker.entity.User, LocalDate)`.
    - `WorkoutVersionRepository`: Extending `JpaRepository<WorkoutVersion, Long>`.

- **Services:**
    - `WorkoutService`: Methods for creating, updating, deleting workouts, and retrieving daily workouts.
    - `WorkoutVersionService`: To log changes via method `saveWorkoutVersion(Workout, String, com.example.spring_app_workout_tracker.entity.User)`.

- **Controller:**
    - `WorkoutController`: REST endpoints – `POST /workouts`, `PUT /workouts/{id}`, `DELETE /workouts/{id}`, and `GET /workouts?date=...`.

### 4. Notification System

- **Configuration:**
    - `WebSocketConfig`: Annotated with `@EnableWebSocketMessageBroker` to define endpoints and allowed origins.

- **DTOs:**
    - Notification message classes (e.g., `NotificationDTO`, `WorkoutNotificationMessage`) to encapsulate message data.

- **Service:**
    - `NotificationService`: Methods such as `sendNotificationToUser(Long, NotificationDTO)` and `broadcastNotification(NotificationDTO)` using Spring’s `SimpMessagingTemplate`.

- **Controller:**
    - Optionally, a `NotificationController` for managing subscriptions, or integrate notifications within other controllers.

### 5. I18n and Mobile UI

- **Frontend:**
    - Thymeleaf templates for pages such as `login.html`, `dashboard.html`, `trainer-dashboard.html`, and partial fragments (e.g., `notification-fragment.html`).
    - A language selector in the header to switch languages.

- **Backend:**
    - Message resource bundles (`messages_en.properties`, `messages_de.properties`, `messages_es.properties`) located in the resources folder.
    - A configured `LocaleResolver` (e.g., SessionLocaleResolver or CookieLocaleResolver) with an interceptor for locale switching.

### 6. Deployment & Security

- **Environment Configuration:**
    - Profiles such as `application.properties` for local development and `application-prod.properties` for production on AWS.

- **Deployment:**
    - Deploy the Spring Boot application on an EC2 instance behind a load balancer.
    - Enforce HTTPS via ACM certificates.
    - Utilize CloudWatch for monitoring and logging.

- **Security Enhancements:**
    - Configure CSRF tokens, CORS settings, and optionally implement rate limiting.
    - Harden HTTP headers using Spring Security’s configuration.

---

## API Endpoints

Below is a sample list of API endpoints:

- **Authentication:**
    - `POST /auth/login`
    - `POST /auth/refresh`
    - `GET /auth/oauth2/redirect`

- **Trainer Management:**
    - `POST /trainer/assign`
    - `POST /trainer/remove`
    - `GET /trainer/my-trainees`

- **Workout Management:**
    - `POST /workouts`
    - `PUT /workouts/{id}`
    - `DELETE /workouts/{id}`
    - `GET /workouts?date=YYYY-MM-DD`

- **Notifications:**
    - (Handled via WebSocket endpoint `/ws`)

---

## End-to-End Flow

1. **com.example.spring_app_workout_tracker.entity.User Registration & Login:**
    - A new user registers via the registration form (e.g., `register.html`) which is processed by `AuthController`.
    - com.example.spring_app_workout_tracker.entity.User data is persisted via `UserService` and `UserRepository`.
    - During login, if credentials are valid, a JWT token is generated by `AuthService` and returned to the client.

2. **Role & Trainer Assignment:**
    - Trainees select a trainer through their profile page.
    - The `TrainerController` and `TrainerService` create or update a `UserTrainer` record, ensuring uniqueness of trainer assignment.

3. **Workout Creation & Management:**
    - Users add workouts through the daily dashboard (triggering the “+” icon) that calls `WorkoutController.createWorkout()`.
    - Trainer-assigned workouts are managed in the trainer dashboard with day-based actions.
    - All changes are versioned using `WorkoutVersionService`.

4. **Real-Time Notifications:**
    - After workouts are created or updated, `NotificationService.sendNotificationToUser()` is called.
    - WebSocket configuration in `WebSocketConfig` pushes notifications to the user interface.

5. **Internationalization & UI Responsiveness:**
    - A locale resolver configures message bundles for multiple languages.
    - Thymeleaf templates with Bootstrap ensure a responsive design across devices.

6. **Deployment & Security:**
    - The final build is deployed on AWS with HTTPS, rate limiting, CloudWatch logging, and enhanced security via Spring Security.

---

## Deployment

- Update the properties files for production (e.g., `application-prod.properties`).
- Build the application using Maven:

  ```bash
  mvn clean package
