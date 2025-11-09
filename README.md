Employee Management System
A full-stack web application for managing employees, departments, and project assignments within an organization.
Technology Stack

- Backend

Spring Boot 3.4.11 (Java 17)
PostgreSQL18 database
JPA/Hibernate for ORM
Maven for dependency management
Lombok for reducing boilerplate code

- Frontend

Angular 20 with standalone components
TypeScript
RxJS for reactive programming
HttpClient for API communication

- Features
 1- Department Management

Create, read, update, and delete departments
Track name, location, and budget
View employee and project counts
Prevent deletion of departments with assigned employees

2- Employee Management

Add and manage employee records
Store personal information (name, email, phone, hire date, salary)
Assign employees to departments
Filter employees by department
Email uniqueness validation

3- Project Management

Create and manage projects
Track project details (name, description, start/end dates)
Assign projects to departments
View employee count per project
Filter projects by department

4- Employee-Project Assignments

Assign multiple employees to projects
Track employee roles in projects (Developer, Manager, Analyst, etc.)
View all projects for a specific employee
View all employees on a specific project
Many-to-many relationship support

- Database Schema
Tables

departments: Stores department information
employees: Stores employee records
projects: Stores project information
employee_projects: Junction table for employee-project relationships

Relationships

One-to-Many: Department → Employees
One-to-Many: Department → Projects
Many-to-Many: Employees ↔ Projects (through employee_projects)

Prerequisites
Backend Requirements

Java Development Kit (JDK) 17 or higher
Maven 3.6 or higher
PostgreSQL 12 or higher

Frontend Requirements

Node.js 18 or higher
npm 9 or higher
Angular CLI 17

Installation & Setup
1. Database Setup
Create a PostgreSQL database:

sqlCREATE DATABASE ems_db;
CREATE USER postgres WITH PASSWORD 'root';
GRANT ALL PRIVILEGES ON DATABASE ems_db TO postgres;

2. Backend Setup

Update src/main/resources/application.properties if needed:

propertiesspring.datasource.url=jdbc:postgresql://localhost:5432/ems_db
spring.datasource.username=postgres
spring.datasource.password=rot

run the application: backend will start on http://localhost:8080

3. Frontend Setup

Navigate to the frontend directory:

Install dependencies:
npm install
Start the development server:
ng serve  or 
ng serve --port 4200  or
npm start
The frontend will start on http://localhost:4200
