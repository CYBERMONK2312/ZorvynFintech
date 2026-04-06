FINANCE DASHBOARD API REFERENCE
================================================================================

Welcome to the API documentation for the Finance Dashboard backend. This RESTful API allows you to manage users, track financial records, and retrieve aggregated dashboard analytics.

--- BASE CONFIGURATION ---
Base URL: /api (e.g., http://localhost:8080/api for local development)
Content-Type: application/json

--- AUTHENTICATION ---
All endpoints (except open auth routes) are secured using JSON Web Tokens (JWT). You must include the token in the "Authorization" header of every request.
Format: Authorization: Bearer <your_jwt_token>


================================================================================
GETTING STARTED (LOCAL SETUP)
================================================================================

Prerequisites:
- Java 17 or higher
- Maven 3.8+
- (Optional) PostgreSQL if switching from the default H2 memory database.

Installation Steps:
1. Clone the repository: git clone https://github.com/yourusername/finance-dashboard.git
2. Navigate to the directory: cd finance-dashboard
3. Build the project: mvn clean install
4. Run the application: mvn spring-boot:run
5. The API will be available at http://localhost:8080/api

--- ENVIRONMENT VARIABLES ---
The application uses the following environment variables. You can set these in your application.yml or export them in your terminal.
- JWT_SECRET: The secret key used to sign JSON Web Tokens (must be at least 256 bits).
- JWT_EXPIRATION: Token lifespan in milliseconds (default is 86400000 for 24 hours).
- DB_URL: Database connection string (default: jdbc:h2:mem:financedb).
- DB_USER: Database username.
- DB_PASSWORD: Database password.


================================================================================
1. AUTHENTICATION & USER MANAGEMENT
================================================================================
Endpoints for managing user accounts, authentication, and system access.

[1] Register New User
Function: Creates a new user account.
Endpoint: POST /auth/register
Access: Public
Request Body: 
{
  "username": "john_doe", 
  "password": "securepassword"
}
Success Response: 201 Created (Returns JWT Access Token)

[2] Login User
Function: Authenticates a user and returns a JWT.
Endpoint: POST /auth/login
Access: Public
Request Body: 
{
  "username": "john_doe", 
  "password": "securepassword"
}
Success Response: 200 OK (Returns JWT Access Token)

[3] Get Current Profile
Function: Retrieves the profile details of the currently authenticated user.
Endpoint: GET /users/me
Access: ADMIN, ANALYST, VIEWER
Success Response: 200 OK (Returns User Object)

[4] Get All Users
Function: Retrieves a complete list of all registered users in the system.
Endpoint: GET /users
Access: ADMIN
Success Response: 200 OK (Returns Array of User objects)

[5] Toggle User Status
Function: Toggles a specific user's account access (activates or deactivates them).
Endpoint: PATCH /users/{id}/status
Access: ADMIN
Path Parameters: id (Long) - The unique ID of the user.
Query Parameters: isActive (Boolean, Required) - "true" to activate, "false" to deactivate.
Success Response: 204 No Content
Error Response: 404 Not Found


================================================================================
2. FINANCIAL RECORDS
================================================================================
Endpoints for creating and managing income and expense entries.

[1] Create Record
Function: Creates a new financial transaction (income or expense).
Endpoint: POST /records
Access: ADMIN
Request Body: RecordDto (omit the "id" field)
Success Response: 201 Created (Returns the created RecordDto with auto-generated ID)
Error Response: 400 Bad Request (Validation failure)

[2] Get Records (Feed)
Function: Fetches a user's transaction history. 
Endpoint: GET /records
Access: ADMIN, ANALYST, VIEWER
Query Parameters (Optional):
  - type (String): Filter by INCOME or EXPENSE.
  - category (String): Exact match for a category name.
  - page (Integer): Page index (default: 0).
  - size (Integer): Items per page (default: 20).
Success Response: 200 OK (Returns a paginated array of RecordDto)

[3] Get Single Record
Function: Fetches the details of a specific transaction by its ID.
Endpoint: GET /records/{id}
Access: ADMIN, ANALYST, VIEWER
Path Parameters: id (Long) - The unique ID of the record.
Success Response: 200 OK (Returns RecordDto Object)
Error Response: 404 Not Found

[4] Update Record
Function: Fully updates an existing transaction.
Endpoint: PUT /records/{id}
Access: ADMIN
Path Parameters: id (Long) - The unique ID of the record.
Request Body: RecordDto (All fields required for full update)
Success Response: 200 OK (Returns the updated RecordDto)

[5] Delete Record
Function: Permanently deletes a specific financial record from the database.
Endpoint: DELETE /records/{id}
Access: ADMIN
Path Parameters: id (Long) - The unique ID of the record.
Success Response: 204 No Content
Error Response: 404 Not Found


================================================================================
3. DASHBOARD ANALYTICS
================================================================================
Endpoints for data aggregation and visualization.

[1] Get Dashboard Summary
Function: Calculates and returns aggregated data for the UI dashboard.
Endpoint: GET /dashboard/summary
Access: ADMIN, ANALYST
Success Response: 200 OK (Returns DashboardSummary object)


================================================================================
PAGINATION & SORTING GUIDE
================================================================================
Endpoints that return lists (like GET /records) support Spring Data's pagination and sorting. 

Examples:
- Get the second page of records, 10 items per page:
  GET /records?page=1&size=10
  
- Sort records by amount, highest to lowest:
  GET /records?sort=amount,desc
  
- Sort by category alphabetically, then by date newest first:
  GET /records?sort=category,asc&sort=recordDate,desc


================================================================================
DATA MODELS (SCHEMAS)
================================================================================

[User Object]
- id (Long): Unique system identifier.
- username (String): Unique login credential.
- role (String): System access level (ADMIN, ANALYST, VIEWER).
- isActive (Boolean): Current account status.

[RecordDto Object]
- id (Long): Auto-generated unique identifier.
- amount (Decimal): Transaction value (Must be >= 0.01).
- type (String): INCOME or EXPENSE.
- category (String): Classification (e.g., "Salary", "Rent").
- recordDate (String): Date in YYYY-MM-DD format.
- notes (String, Optional): Context or description.

[DashboardSummary Object]
- totalIncome (Decimal): Gross sum of all INCOME records.
- totalExpense (Decimal): Gross sum of all EXPENSE records.
- netBalance (Decimal): totalIncome minus totalExpense.
- expensesByCategory (Object): Key-value map of categories and their totals.
- recentActivity (Array): List of the 5 most recently dated RecordDtos.


================================================================================
STANDARD ERROR RESPONSES
================================================================================
The API utilizes a global exception handler. All errors return a standardized JSON payload.

--- 400 Bad Request (Validation Error Example) ---
{
  "timestamp": "2024-05-14T10:30:00.000",
  "status": 400,
  "errors": {
    "amount": "must be greater than or equal to 0.01",
    "category": "must not be blank"
  }
}

--- 403 Forbidden (Role Access Violation Example) ---
{
  "timestamp": "2024-05-14T10:30:00.000",
  "status": 403,
  "error": "Forbidden",
  "message": "Access Denied"
}

--- 404 Not Found (Resource Missing Example) ---
{
  "timestamp": "2024-05-14T10:30:00.000",
  "status": 404,
  "error": "Not Found",
  "message": "User not found with ID: 99"
}
