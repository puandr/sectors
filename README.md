# Sectors App

A backend service for managing user inputs related to various sectors, built with Java Spring Boot. This project is part of an exercise to create a web application that allows users to input, edit, and retrieve information about different sectors, with functionality for form refilling, validation, and session management.

## Backend features

- User can submit their information along with selected sectors.
- Data persistence using a relational database (PostgreSQL).
- RESTful API with endpoints to create, read, update, and delete user inputs.
- Integration with OpenAPI for API documentation.
- Field-level validation for user inputs.
- Security considerations like input sanitization, whitelisting, and input size limitation.
- Session management for tracking user inputs during their session.

## Frontend features

- **User Information Input**: Users can enter their name and select the sectors they are involved in.
- **Session Management**: User data is stored in the backend, and a session identifier is saved locally for retrieving and updating user data.
- **Loading Indicator**: Displays a loading spinner while retrieving user data from the server.
- **Validation**: Ensures all required fields are filled before allowing form submission.

## Technologies Used

- **Java Spring Boot**: Backend framework for building RESTful APIs.
- **PostgreSQL**: Relational database for storing user inputs.
- **Lombok**: Java library to reduce boilerplate code.
- **JUnit**: For testing.
- **MockMvc**: For testing controllers.
- **Hibernate**: ORM for interacting with the database.
- **OpenAPI (Swagger)**: For API documentation.
- **React**: For building the user interface.
- **Axios**: For making HTTP requests to the backend.
- **CSS**: For styling the components.

## API Endpoints

- **POST /api/user-inputs**: Create a new user input.
- **GET /api/user-inputs**: Get all user inputs.
- **GET /api/user-inputs/{id}**: Get a user input by ID.
- **PUT /api/user-inputs/{id}**: Update a user input by ID.
- **GET /api/sectors**: Retrieve all sectors.
