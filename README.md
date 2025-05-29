Patient Medicine and Appointment System - Documentation
1. Introduction
This document provides detailed instructions on setting up, configuring, and running the Patient Medicine and Appointment System. It also documents the API endpoints, request/response formats, data validation rules, and unit testing details.
2. Setting Up the Project
Follow these steps to set up the project on your local system:
Prerequisites:
- Java 21 or later
- Maven 3.8+
- Spring Boot 3.4.2
- MySQL Database
- Postman (optional for API testing)
Installation Steps:
1. Clone the repository:
   `git clone https://github.com/example/demoPatientMgmt.git`
2. Navigate to the project directory:
   `cd demoPatientMgmt`
3. Configure the database in `application.properties`:
   ```
   spring.datasource.url=jdbc:mysql://localhost:3306/patient_mgmt
   spring.datasource.username=root
   spring.datasource.password=password
   ```
4. Run the application:
   `mvn spring-boot:run`
5. Open `http://localhost:8080` in your browser to access the application.
