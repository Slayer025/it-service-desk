IT Service Desk - ITIL Compliant Ticket Management System
A full-stack, role-based IT Service Desk application built with Spring Boot 3.x, Thymeleaf, and MySQL. Implements core ITIL processes including SLA management, auto-escalation, audit logging, and multi-level support teams.




Key Features
Role-Based Access Control: Distinct views and permissions for End Users, Agents, and Admins
ITIL SLA Engine: Automatic deadline calculation based on priority (Critical=2h, High=4h, Medium=24h, Low=72h)
Auto-Escalation: Overdue tickets automatically escalate from L1 → L2 → L3 Support
Audit Trail: Immutable log of all ticket actions (creation, status changes, comments, escalations)
Full CRUD + Filtering: Create, view, update tickets; filter by priority, category, or status
Pagination & Search: Scalable dashboard with server-side pagination
REST API + Web UI: Dual interface - Thymeleaf frontend for users, JSON API for integrations
Swagger Documentation: Interactive API docs at /swagger-ui.html
Secure Authentication: BCrypt password hashing, CSRF protection, session management
Architecture
The application follows a layered MVC architecture enhanced with Spring Security:
Presentation Layer (WebViewController, HTML Templates) handles HTTP requests and renders Thymeleaf views. Business Logic Layer (TicketService, AuditLog) enforces ITIL rules like SLA calculation and escalation while maintaining audit logs. Data Access Layer (TicketRepository, JPA Entities) translates Java objects to SQL queries without containing business logic. Security Layer (SecurityConfig, CustomUserDetailsService) intercepts every request to authenticate users and enforce role-based access control before reaching any controller.
Both the Thymeleaf web interface and REST API controllers delegate to the same TicketService methods, ensuring business logic is never duplicated regardless of whether a ticket originates from a browser form or a JSON payload.
Tech Stack
Backend: Spring Boot 3.3.2, Java 17, Spring Security 6
Frontend: Thymeleaf, HTML5/CSS3, Chart.js
Database: MySQL 8.0, Spring Data JPA, Hibernate
API Docs: SpringDoc OpenAPI (Swagger UI)
Build Tool: Maven
Testing: JUnit 5, Spring Security Test
Getting Started
Prerequisites
Java 17+
MySQL 8.0+
Maven 3.8+
Git
Installation Steps
Clone the repository
git clone https://github.com/YOUR_USERNAME/it-service-desk.git
cd it-service-desk
Configure Database
Create a MySQL database named "it_service_desk" and update credentials in src/main/resources/application.properties:
spring.datasource.url=jdbc:mysql://localhost:3306/it_service_desk
spring.datasource.username=root
spring.datasource.password=YOUR_PASSWORD
Build and Run
mvn clean install
mvn spring-boot:run
Access the Application
Web UI: http://localhost:8080/login
Swagger API Docs: http://localhost:8080/swagger-ui.html
REST API Base: http://localhost:8080/api/tickets
Default Test Accounts
All accounts use password: password123
admin (ADMIN): Full access, manage categories, view audit logs
agent (AGENT): View/update tickets, run escalation, add comments
enduser (USER): Create tickets, view own tickets, add comments
Security Note: These are development-only accounts. Change passwords and disable the DataSeeder component before deploying to production.
Screenshots
Replace these placeholders with actual screenshots after uploading:
Login Page | Dashboard | Ticket Detail
Admin Panel | Swagger UI | Audit Trail
API Endpoints
POST /api/tickets - Create new ticket (Auth Required)
GET /api/tickets - List all tickets (Auth Required)
GET /api/tickets/{id} - Get ticket details (Auth Required)
PUT /api/tickets/{id}/status - Update ticket status (Agent/Admin Only)
POST /api/tickets/{id}/comments - Add comment (Auth Required)
GET /api/tickets/{id}/comments - Get ticket comments (Auth Required)
POST /api/tickets/escalate - Trigger SLA escalation (Agent/Admin Only)
GET /api/tickets/filter/priority - Filter by priority (Auth Required)
GET /api/tickets/filter/category - Filter by category (Auth Required)
Contributing
Pull requests are welcome! For major changes, please open an issue first to discuss what you would like to change.
License
This project is licensed under the MIT License - see the LICENSE file for details.