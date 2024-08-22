# Archive Management System

## Overview

This digital archive management system is designed to efficiently manage, store, and retrieve digital archives. The system integrates various technologies to provide robust features, including importing/exporting archives, asynchronous task processing, and real-time notifications.
![image](https://github.com/user-attachments/assets/eb25ff68-0b2a-43f7-97a4-88ce699a7372)



## Features

- **CRUD Operations**: Create, read, update, and delete digital archives.
- **Import/Export Archives**: Efficient import/export of archive data using Excel files.
- **Asynchronous Task Processing**: Handles long-running tasks such as import/export operations in the background.
- **Real-Time Notifications**: Uses Server-Sent Events (SSE) to notify the client of task completion.

## Technology Stack

- **Backend**: Spring Boot
- **Frontend**: Vue2 + element-ui
- **Database**: MySQL
- **Excel Processing**: Apache POI
- **Asynchronous Operations**: RabbitMQ
- **Real-Time Notifications**: SseEmitter
- **Database Management**: Navicat
- **Version Management**: Maven

## Development Process

### Initial Setup

1. **Project Initialization**: The project was initialized using Spring Boot for the backend and Vue 2 for the frontend. Maven was used as the build tool for managing dependencies.
2. **Database Configuration**: MySQL was chosen for its reliability and performance. The database schema was designed in Navicat, where tables for storing archive data were created.

### Feature Implementation

1. **CRUD Operations**: The core CRUD operations were implemented using Spring Boot RESTful APIs. Vue 2 was used for building the user interface, with components for creating, editing, and viewing archives.
2. **Excel Import/Export**: Apache POI was integrated to handle Excel file reading and writing. A custom `ExcelDatabase` class was created to interface with the database for importing/exporting archives.
3. **Asynchronous Task Processing**: RabbitMQ was introduced to handle long-running tasks, such as importing or exporting large Excel files. The `AsyncTaskProcessor` class was developed to manage these tasks asynchronously.
4. **Real-Time Notifications**: To improve user experience, `SseEmitter` was integrated to send real-time notifications to the client when tasks such as import/export operations were completed.

## Technology Versions

- **Java**: 17
- **Spring Boot**: 3.3.2
- **Vue**: 2.5.2
- **MySQL**: 8.0.13
- **Apache POI**: 5.2.3
- **RabbitMQ**: 3.17.6
- **Navicat**: 11.0.17

## Getting Started

### Prerequisites

- Java 8 or higher
- Node.js and npm
- MySQL Server
- RabbitMQ Server

### Database Setup

- Create a MySQL database for the application.
- Update the database configuration in `application.properties` (backend) with your MySQL credentials.

### Running the Application

- Access the frontend via `http://localhost:8080`.
- Use the provided API endpoints for managing archives via the backend.

## Contributing

Contributions are welcome! Please submit a pull request or open an issue for any bugs or feature requests.

## License

This project is licensed under the MIT License.
