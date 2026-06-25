# Smart-Tourist-Safety-Monitoring-System-
Java full-stack tourist safety monitoring project built with Spring Boot, Spring Security, JWT, Spring Data JPA, Hibernate, MySQL, Thymeleaf, Bootstrap 5, browser Geolocation, Google Maps, OpenWeather, WebSockets, and QR code generation.
Features
Tourist registration, login, logout, BCrypt password encryption, and role-based access control.
JWT authentication through /api/auth/register and /api/auth/login.
Tourist dashboard with current location capture, Google Maps view, SOS button, voice SOS, weather widget, risk prediction, nearby emergency services, incident report upload, notifications, and safety tips.
Admin dashboard with total users, active tourists, open SOS requests, incident reports, live tourist locations, user management, and websocket notifications.
MySQL integration for runtime and H2 test profile for local smoke testing.
Package Structure
com.touristsafety
├── config
├── controller
├── dto
├── entity
├── exception
├── repository
├── security
├── service
└── util
Setup
Install Java 17+ and Maven.
Create a MySQL database user, or use your existing root user.
Set environment variables as needed:
DB_URL=jdbc:mysql://localhost:3306/tourist_safety_db?createDatabaseIfNotExist=true&useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC
DB_USERNAME=root
DB_PASSWORD=your_password
JWT_SECRET=replace-with-a-long-random-secret-key
GOOGLE_MAPS_API_KEY=your_google_maps_key
OPENWEATHER_API_KEY=your_openweather_key
Run:
mvn spring-boot:run
Open http://localhost:8080.
Default admin account:
Email: admin@touristsafety.com
Password: admin123
REST APIs
Authentication:
POST /api/auth/register
POST /api/auth/login
Location:
POST /api/location/update
GET /api/location/history/{userId}
GET /api/location/latest
SOS:
POST /api/sos/send
GET /api/sos/all
PATCH /api/sos/{id}/status?status=ACKNOWLEDGED
Incidents:
POST /api/incidents/report
POST /api/incidents/report-with-evidence
GET /api/incidents/mine
GET /api/incidents/all
Users:
GET /api/users
GET /api/users/{id}
GET /api/users/me
PUT /api/users/profile
GET /api/users/me/qr
PATCH /api/users/{id}/enabled?enabled=false
Safety and dashboard:
GET /api/services/nearby?latitude=28.6139&longitude=77.2090
GET /api/weather/current?latitude=28.6139&longitude=77.2090
GET /api/risk/current?latitude=28.6139&longitude=77.2090
GET /api/safety-tips
GET /api/admin/stats
GET /api/notifications/admin
GET /api/notifications/me
Use the JWT token returned by login:
Authorization: Bearer <token>
Database
Hibernate can maintain the database with spring.jpa.hibernate.ddl-auto=update. A manual SQL reference is included at database/schema.sql.
Verification
Run the test profile:
mvn test
The test profile uses in-memory H2 and does not require MySQL, Google Maps, or OpenWeather keys.
