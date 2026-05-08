# Pharmacy Ordering Website

A full-stack monolithic application for managing pharmacy operations including medicine inventory management, order processing, and prescription handling. Built with Spring Boot backend and React frontend.

## 📋 Table of Contents

- [Project Overview](#project-overview)
- [Technology Stack](#technology-stack)
- [Project Structure](#project-structure)
- [Database Schema](#database-schema)
- [API Endpoints](#api-endpoints)
- [Features](#features)
- [Installation & Setup](#installation--setup)
- [Running the Application](#running-the-application)
- [Configuration](#configuration)
- [Authentication](#authentication)
- [File Uploads](#file-uploads)

---

## 🎯 Project Overview

The Pharmacy Ordering Website is a comprehensive e-commerce platform designed specifically for pharmaceutical businesses. It allows:

- **Customers**: Browse medicines, manage shopping carts, place orders, and upload prescriptions
- **Admins**: Manage medicine inventory, categories, view orders, and approve prescriptions
- **Security**: JWT-based authentication and role-based access control

This is a monolithic architecture with a centralized backend managing all business logic and a responsive React frontend.

---

## 🛠 Technology Stack

### Backend
- **Framework**: Spring Boot 4.0.6
- **Java Version**: Java 17
- **Database**: MySQL
- **Authentication**: JWT (JSON Web Tokens) with Spring Security
- **ORM**: Spring Data JPA with Hibernate
- **Build Tool**: Maven
- **Rate Limiting**: Bucket4j 8.10.1

### Frontend
- **Library**: React 19.2.5
- **Build Tool**: Vite 8.0.10
- **Routing**: React Router 7.15.0
- **HTTP Client**: Axios 1.16.0
- **Styling**: CSS3
- **Linting**: ESLint 10.2.1

---

## 📁 Project Structure

```
pharmacy-ordering-website/
│
├── pharmacy-backend/                 # Spring Boot Backend
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/
│   │   │   │   └── com/pharmacy/
│   │   │   │       ├── controller/    # REST API Controllers
│   │   │   │       ├── service/       # Business Logic
│   │   │   │       ├── repository/    # Database Access
│   │   │   │       ├── entity/        # JPA Entities
│   │   │   │       ├── dto/           # Data Transfer Objects
│   │   │   │       ├── config/        # Spring Configuration
│   │   │   │       ├── security/      # Security Components
│   │   │   │       └── exception/     # Custom Exceptions
│   │   │   └── resources/
│   │   │       └── application.properties
│   │   └── test/                      # Unit Tests
│   ├── pom.xml                       # Maven Configuration
│   └── mvnw/mvnw.cmd                 # Maven Wrapper
│
├── pharmacy-frontend/                # React Frontend
│   ├── src/
│   │   ├── components/               # Reusable Components
│   │   ├── pages/                    # Page Components
│   │   ├── context/                  # React Context (Auth)
│   │   ├── services/                 # API Service Modules
│   │   ├── api/                      # Axios Configuration
│   │   ├── App.jsx                   # Main App Component
│   │   └── main.jsx                  # Entry Point
│   ├── public/                       # Static Assets
│   ├── package.json                  # NPM Dependencies
│   ├── vite.config.js                # Vite Configuration
│   └── eslint.config.js              # ESLint Configuration
│
└── README.md                         # This File
```

---

## 🗄️ Database Schema

### Database: `pharmacyodering_db`

#### **1. Users Table**
```sql
CREATE TABLE users (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    full_name VARCHAR(255) NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    phone VARCHAR(20),
    role ENUM('ADMIN', 'USER') DEFAULT 'USER',
    address TEXT,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);
```

**Columns**:
- `id`: Unique user identifier
- `full_name`: User's full name
- `email`: User's email (unique)
- `password`: Hashed password
- `phone`: Contact phone number
- `role`: User role (ADMIN or USER)
- `address`: Delivery address
- `created_at`: Account creation timestamp
- `updated_at`: Last update timestamp

---

#### **2. Medicine Categories Table**
```sql
CREATE TABLE medicine_categories (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) UNIQUE NOT NULL,
    description TEXT,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP
);
```

**Columns**:
- `id`: Unique category identifier
- `name`: Category name (e.g., Painkillers, Antibiotics)
- `description`: Category description
- `created_at`: Creation timestamp

---

#### **3. Medicines Table**
```sql
CREATE TABLE medicines (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    price DECIMAL(10, 2) NOT NULL,
    stock_quantity INT NOT NULL DEFAULT 0,
    category_id BIGINT NOT NULL,
    manufacturer VARCHAR(255),
    expiry_date DATE,
    is_active BOOLEAN DEFAULT TRUE,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (category_id) REFERENCES medicine_categories(id)
);
```

**Columns**:
- `id`: Unique medicine identifier
- `name`: Medicine name
- `description`: Medicine description
- `price`: Price per unit
- `stock_quantity`: Available stock
- `category_id`: Reference to MedicineCategory
- `manufacturer`: Manufacturing company name
- `expiry_date`: Medicine expiration date
- `is_active`: Soft delete flag
- `created_at`: Creation timestamp
- `updated_at`: Last update timestamp

---

#### **4. Carts Table**
```sql
CREATE TABLE carts (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT UNIQUE NOT NULL,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);
```

**Columns**:
- `id`: Unique cart identifier
- `user_id`: Reference to User (one-to-one)
- `created_at`: Creation timestamp
- `updated_at`: Last update timestamp

---

#### **5. Cart Items Table**
```sql
CREATE TABLE cart_items (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    cart_id BIGINT NOT NULL,
    medicine_id BIGINT NOT NULL,
    quantity INT NOT NULL DEFAULT 1,
    added_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (cart_id) REFERENCES carts(id) ON DELETE CASCADE,
    FOREIGN KEY (medicine_id) REFERENCES medicines(id)
);
```

**Columns**:
- `id`: Unique cart item identifier
- `cart_id`: Reference to Cart
- `medicine_id`: Reference to Medicine
- `quantity`: Quantity of medicine in cart
- `added_at`: Timestamp when added to cart

---

#### **6. Orders Table**
```sql
CREATE TABLE orders (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    order_date DATETIME DEFAULT CURRENT_TIMESTAMP,
    total_amount DECIMAL(10, 2) NOT NULL,
    status ENUM('PENDING', 'CONFIRMED', 'SHIPPED', 'DELIVERED', 'CANCELLED') DEFAULT 'PENDING',
    delivery_address TEXT,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id)
);
```

**Columns**:
- `id`: Unique order identifier
- `user_id`: Reference to User
- `order_date`: Date of order placement
- `total_amount`: Total order value
- `status`: Order status
- `delivery_address`: Delivery address
- `created_at`: Creation timestamp
- `updated_at`: Last update timestamp

---

#### **7. Order Items Table**
```sql
CREATE TABLE order_items (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    order_id BIGINT NOT NULL,
    medicine_id BIGINT NOT NULL,
    quantity INT NOT NULL,
    unit_price DECIMAL(10, 2) NOT NULL,
    subtotal DECIMAL(10, 2) NOT NULL,
    FOREIGN KEY (order_id) REFERENCES orders(id) ON DELETE CASCADE,
    FOREIGN KEY (medicine_id) REFERENCES medicines(id)
);
```

**Columns**:
- `id`: Unique order item identifier
- `order_id`: Reference to Order
- `medicine_id`: Reference to Medicine
- `quantity`: Quantity ordered
- `unit_price`: Price at time of order
- `subtotal`: Total for this item (quantity × unit_price)

---

#### **8. Prescriptions Table**
```sql
CREATE TABLE prescriptions (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    file_url VARCHAR(500) NOT NULL,
    status ENUM('PENDING', 'APPROVED', 'REJECTED') DEFAULT 'PENDING',
    uploaded_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    approved_at DATETIME,
    approved_by BIGINT,
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (approved_by) REFERENCES users(id)
);
```

**Columns**:
- `id`: Unique prescription identifier
- `user_id`: Reference to User who uploaded
- `file_url`: URL to prescription file
- `status`: Approval status
- `uploaded_at`: Upload timestamp
- `approved_at`: Approval timestamp
- `approved_by`: Admin who approved (Reference to User)

---

#### **9. Roles Table**
```sql
CREATE TABLE roles (
    id INT PRIMARY KEY AUTO_INCREMENT,
    role_name VARCHAR(50) UNIQUE NOT NULL
);
```

**Sample Data**:
```sql
INSERT INTO roles (role_name) VALUES ('ADMIN'), ('USER');
```

---

#### **10. Audit Logs Table**
```sql
CREATE TABLE audit_logs (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT,
    action VARCHAR(100) NOT NULL,
    entity_type VARCHAR(100),
    entity_id BIGINT,
    details TEXT,
    timestamp DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id)
);
```

**Columns**:
- `id`: Unique log identifier
- `user_id`: User performing action
- `action`: Action performed (CREATE, UPDATE, DELETE, etc.)
- `entity_type`: Type of entity affected
- `entity_id`: ID of affected entity
- `details`: Additional details
- `timestamp`: Action timestamp

---

## 🔌 API Endpoints

### Base URL
```
http://localhost:8080/api/v1
```

---

### **Authentication Endpoints** (`/auth`)

#### 1. **Register**
- **Method**: `POST`
- **Endpoint**: `/auth/register`
- **Description**: Register a new user
- **Request Body**:
```json
{
  "fullName": "John Doe",
  "email": "john@example.com",
  "password": "SecurePass@123",
  "phone": "9876543210",
  "address": "123 Main St, City"
}
```
- **Response**:
```json
{
  "message": "User registered successfully"
}
```
- **Status**: 200 OK

---

#### 2. **Login**
- **Method**: `POST`
- **Endpoint**: `/auth/login`
- **Description**: Authenticate user and receive JWT token
- **Request Body**:
```json
{
  "email": "john@example.com",
  "password": "SecurePass@123"
}
```
- **Response**:
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "userId": 1,
  "email": "john@example.com",
  "role": "USER"
}
```
- **Status**: 200 OK

---

### **Medicine Endpoints** (`/medicines`)

#### 3. **Get All Medicines**
- **Method**: `GET`
- **Endpoint**: `/medicines`
- **Description**: Retrieve all active medicines with pagination
- **Query Parameters**:
  - `page` (default: 0): Page number
  - `size` (default: 10): Items per page
- **Example**: `GET /medicines?page=0&size=10`
- **Response**:
```json
{
  "content": [
    {
      "id": 1,
      "name": "Aspirin",
      "description": "Pain reliever",
      "price": 10.50,
      "stockQuantity": 100,
      "category": "Painkillers",
      "manufacturer": "PharmaCorp",
      "expiryDate": "2025-12-31"
    }
  ],
  "pageNumber": 0,
  "pageSize": 10,
  "totalElements": 50
}
```
- **Status**: 200 OK

---

#### 4. **Get Medicine by ID**
- **Method**: `GET`
- **Endpoint**: `/medicines/{id}`
- **Description**: Retrieve a specific medicine
- **Path Parameters**:
  - `id`: Medicine ID
- **Example**: `GET /medicines/1`
- **Response**:
```json
{
  "id": 1,
  "name": "Aspirin",
  "description": "Pain reliever",
  "price": 10.50,
  "stockQuantity": 100,
  "category": "Painkillers",
  "manufacturer": "PharmaCorp",
  "expiryDate": "2025-12-31"
}
```
- **Status**: 200 OK

---

#### 5. **Search Medicines**
- **Method**: `GET`
- **Endpoint**: `/medicines/search`
- **Description**: Search medicines by keyword
- **Query Parameters**:
  - `keyword` (required): Search term
  - `page` (default: 0): Page number
  - `size` (default: 10): Items per page
- **Example**: `GET /medicines/search?keyword=aspirin&page=0&size=10`
- **Response**: Same as Get All Medicines
- **Status**: 200 OK

---

#### 6. **Get Medicines by Category**
- **Method**: `GET`
- **Endpoint**: `/medicines/category`
- **Description**: Get medicines filtered by category
- **Query Parameters**:
  - `name` (required): Category name
  - `page` (default: 0): Page number
  - `size` (default: 10): Items per page
- **Example**: `GET /medicines/category?name=Painkillers&page=0&size=10`
- **Response**: Same as Get All Medicines
- **Status**: 200 OK

---

#### 7. **Create Medicine** ⚙️ Admin Only
- **Method**: `POST`
- **Endpoint**: `/medicines`
- **Description**: Create a new medicine
- **Headers**: `Authorization: Bearer {token}`
- **Request Body**:
```json
{
  "name": "Aspirin",
  "description": "Pain reliever",
  "price": 10.50,
  "stockQuantity": 100,
  "categoryId": 1,
  "manufacturer": "PharmaCorp",
  "expiryDate": "2025-12-31"
}
```
- **Response**: Same as Get Medicine by ID
- **Status**: 200 OK

---

#### 8. **Update Medicine** ⚙️ Admin Only
- **Method**: `PUT`
- **Endpoint**: `/medicines/{id}`
- **Description**: Update medicine details
- **Headers**: `Authorization: Bearer {token}`
- **Path Parameters**:
  - `id`: Medicine ID
- **Request Body**: Same as Create Medicine
- **Response**: Updated medicine object
- **Status**: 200 OK

---

#### 9. **Deactivate/Delete Medicine** ⚙️ Admin Only
- **Method**: `DELETE`
- **Endpoint**: `/medicines/{id}`
- **Description**: Mark medicine as inactive
- **Headers**: `Authorization: Bearer {token}`
- **Path Parameters**:
  - `id`: Medicine ID
- **Response**:
```json
{
  "message": "Medicine marked as inactive successfully"
}
```
- **Status**: 200 OK

---

### **Category Endpoints** (`/categories`)

#### 10. **Get All Categories**
- **Method**: `GET`
- **Endpoint**: `/categories`
- **Description**: Retrieve all medicine categories
- **Response**:
```json
[
  {
    "id": 1,
    "name": "Painkillers",
    "description": "Pain relief medications"
  },
  {
    "id": 2,
    "name": "Antibiotics",
    "description": "Bacterial infection treatment"
  }
]
```
- **Status**: 200 OK

---

#### 11. **Create Category** ⚙️ Admin Only
- **Method**: `POST`
- **Endpoint**: `/categories`
- **Description**: Create a new medicine category
- **Headers**: `Authorization: Bearer {token}`
- **Request Body**:
```json
{
  "name": "Painkillers",
  "description": "Pain relief medications"
}
```
- **Response**:
```json
{
  "id": 1,
  "name": "Painkillers",
  "description": "Pain relief medications"
}
```
- **Status**: 200 OK

---

#### 12. **Create Category (Admin)** ⚙️ Admin Only
- **Method**: `POST`
- **Endpoint**: `/admin/categories`
- **Description**: Create category from admin panel
- **Headers**: `Authorization: Bearer {token}`
- **Request Body**: Same as Create Category
- **Response**: Same as Create Category
- **Status**: 200 OK

---

### **Cart Endpoints** (`/cart`)

#### 13. **Get Cart**
- **Method**: `GET`
- **Endpoint**: `/cart`
- **Description**: Retrieve user's shopping cart
- **Headers**: `Authorization: Bearer {token}`
- **Response**:
```json
{
  "id": 1,
  "userId": 1,
  "items": [
    {
      "id": 1,
      "medicineId": 1,
      "medicineName": "Aspirin",
      "quantity": 2,
      "price": 10.50,
      "subtotal": 21.00
    }
  ],
  "totalItems": 1,
  "grandTotal": 21.00
}
```
- **Status**: 200 OK

---

#### 14. **Add to Cart**
- **Method**: `POST`
- **Endpoint**: `/cart/add`
- **Description**: Add medicine to cart
- **Headers**: `Authorization: Bearer {token}`
- **Request Body**:
```json
{
  "medicineId": 1,
  "quantity": 2
}
```
- **Response**: Updated cart object (same as Get Cart)
- **Status**: 200 OK

---

#### 15. **Remove Item from Cart**
- **Method**: `DELETE`
- **Endpoint**: `/cart/remove/{itemId}`
- **Description**: Remove item from cart
- **Headers**: `Authorization: Bearer {token}`
- **Path Parameters**:
  - `itemId`: Cart item ID
- **Response**:
```json
{
  "message": "Item removed successfully"
}
```
- **Status**: 200 OK

---

### **Order Endpoints** (`/orders`)

#### 16. **Place Order**
- **Method**: `POST`
- **Endpoint**: `/orders/place`
- **Description**: Create and place a new order
- **Headers**: `Authorization: Bearer {token}`
- **Request Body**:
```json
{
  "items": [
    {
      "medicineId": 1,
      "quantity": 2
    }
  ],
  "deliveryAddress": "123 Main St, City"
}
```
- **Response**:
```json
{
  "id": 1,
  "userId": 1,
  "orderDate": "2025-05-08T10:30:00",
  "totalAmount": 150.00,
  "status": "PENDING",
  "deliveryAddress": "123 Main St, City",
  "items": [
    {
      "id": 1,
      "medicineId": 1,
      "medicineName": "Aspirin",
      "quantity": 2,
      "unitPrice": 10.50,
      "subtotal": 21.00
    }
  ]
}
```
- **Status**: 200 OK

---

#### 17. **Get My Orders**
- **Method**: `GET`
- **Endpoint**: `/orders/my-orders`
- **Description**: Get all orders for logged-in user
- **Headers**: `Authorization: Bearer {token}`
- **Response**:
```json
[
  {
    "id": 1,
    "userId": 1,
    "orderDate": "2025-05-08T10:30:00",
    "totalAmount": 150.00,
    "status": "PENDING",
    "deliveryAddress": "123 Main St, City"
  }
]
```
- **Status**: 200 OK

---

#### 18. **Get All Orders** ⚙️ Admin Only
- **Method**: `GET`
- **Endpoint**: `/admin/orders`
- **Description**: Get all orders in the system
- **Headers**: `Authorization: Bearer {token}`
- **Response**: Array of order objects (same as Get My Orders)
- **Status**: 200 OK

---

### **Prescription Endpoints** (`/prescriptions`)

#### 19. **Upload Prescription**
- **Method**: `POST`
- **Endpoint**: `/prescriptions/upload`
- **Description**: Upload prescription file
- **Headers**: `Authorization: Bearer {token}`
- **Request Type**: `multipart/form-data`
- **Form Parameters**:
  - `file` (required): Prescription image/document
- **Response**:
```json
{
  "id": 1,
  "userId": 1,
  "fileUrl": "uploads/prescriptions/prescription_12345.jpg",
  "status": "PENDING",
  "uploadedAt": "2025-05-08T10:30:00"
}
```
- **Status**: 200 OK

---

#### 20. **Get All Prescriptions** ⚙️ Admin Only
- **Method**: `GET`
- **Endpoint**: `/admin/prescriptions`
- **Description**: Get all prescriptions for review
- **Headers**: `Authorization: Bearer {token}`
- **Response**:
```json
[
  {
    "id": 1,
    "userId": 1,
    "userEmail": "john@example.com",
    "fileUrl": "uploads/prescriptions/prescription_12345.jpg",
    "status": "PENDING",
    "uploadedAt": "2025-05-08T10:30:00"
  }
]
```
- **Status**: 200 OK

---

#### 21. **Approve Prescription** ⚙️ Admin Only
- **Method**: `PUT`
- **Endpoint**: `/admin/prescriptions/{id}/approve`
- **Description**: Approve a prescription
- **Headers**: `Authorization: Bearer {token}`
- **Path Parameters**:
  - `id`: Prescription ID
- **Response**:
```json
{
  "id": 1,
  "userId": 1,
  "fileUrl": "uploads/prescriptions/prescription_12345.jpg",
  "status": "APPROVED",
  "uploadedAt": "2025-05-08T10:30:00",
  "approvedAt": "2025-05-08T11:00:00",
  "approvedBy": 2
}
```
- **Status**: 200 OK

---

## ✨ Features

### User Features
- ✅ **User Registration & Login** - Secure JWT-based authentication
- ✅ **Browse Medicines** - View all available medicines with pagination
- ✅ **Search & Filter** - Search medicines by name, filter by category
- ✅ **Shopping Cart** - Add/remove medicines to/from cart
- ✅ **Place Orders** - Complete order from cart
- ✅ **View Orders** - Track personal orders and status
- ✅ **Upload Prescriptions** - Upload prescription files for validation
- ✅ **Order History** - View past orders and details

### Admin Features
- ✅ **Manage Medicines** - Create, update, and deactivate medicines
- ✅ **Manage Categories** - Create and manage medicine categories
- ✅ **View All Orders** - Monitor all orders in the system
- ✅ **Review Prescriptions** - View and approve/reject prescriptions
- ✅ **Inventory Management** - Track stock levels
- ✅ **Audit Trail** - Track all system activities

### Security Features
- ✅ **JWT Authentication** - Secure token-based authentication
- ✅ **Role-Based Access Control** - Different permissions for USER and ADMIN roles
- ✅ **Password Encryption** - Secure password hashing
- ✅ **Rate Limiting** - API rate limiting using Bucket4j
- ✅ **CORS Support** - Cross-origin resource sharing configured

### Technical Features
- ✅ **Pagination** - Efficient data retrieval with page-based pagination
- ✅ **File Upload** - Secure prescription file upload
- ✅ **Error Handling** - Comprehensive exception handling
- ✅ **Validation** - Input validation using Jakarta Validation
- ✅ **Soft Delete** - Medicines are soft-deleted (is_active flag)

---

## 📦 Installation & Setup

### Prerequisites
- **Java 17** or higher
- **MySQL 8.0** or higher
- **Node.js 18+** and **npm 9+**
- **Git**

### Backend Setup

1. **Clone the repository**
```bash
git clone <repository-url>
cd "Pharmacy Ordering Website"
cd pharmacy-backend
```

2. **Create MySQL Database**
```sql
CREATE DATABASE pharmacyodering_db;
CREATE USER 'root'@'localhost' IDENTIFIED BY 'Shiv@5502';
GRANT ALL PRIVILEGES ON pharmacyodering_db.* TO 'root'@'localhost';
FLUSH PRIVILEGES;
```

3. **Configure application.properties**
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/pharmacyodering_db
spring.datasource.username=root
spring.datasource.password=Shiv@5502
```

4. **Build the project**
```bash
./mvnw clean install
```

---

### Frontend Setup

1. **Navigate to frontend directory**
```bash
cd pharmacy-frontend
```

2. **Install dependencies**
```bash
npm install
```

3. **Configure API endpoint**
Edit `src/api/axios.js`:
```javascript
const API_BASE_URL = 'http://localhost:8080/api/v1';
```

---

## 🚀 Running the Application

### Start Backend Server
```bash
cd pharmacy-backend
./mvnw spring-boot:run
```
Backend will start at: `http://localhost:8080`

### Start Frontend Development Server
```bash
cd pharmacy-frontend
npm run dev
```
Frontend will start at: `http://localhost:5173` (or specified by Vite)

### Build Frontend for Production
```bash
npm run build
```
Output files will be in `dist/` directory

---

## ⚙️ Configuration

### Backend Configuration (`application.properties`)

```properties
# Application Name
spring.application.name=pharmacy-backend

# Database Configuration
spring.datasource.url=jdbc:mysql://localhost:3306/pharmacyodering_db
spring.datasource.username=root
spring.datasource.password=Shiv@5502
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

# JPA/Hibernate Configuration
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQLDialect

# JWT Configuration
jwt.secret=THIS_IS_A_SUPER_SECRET_KEY_FOR_PHARMACY_PROJECT
jwt.expiration=86400000  # 24 hours in milliseconds

# File Upload Configuration
file.upload-dir=uploads/prescriptions
```

### Frontend Configuration

**Vite Config** (`vite.config.js`):
```javascript
export default {
  plugins: [react()],
  server: {
    proxy: {
      '/api': 'http://localhost:8080'
    }
  }
}
```

---

## 🔐 Authentication

### JWT Token Structure

```javascript
{
  "sub": "user@example.com",
  "iat": 1620000000,
  "exp": 1620086400,
  "role": "USER"
}
```

### How to Use JWT Token

1. **Register/Login** to get a token
2. **Include token** in request headers:
```
Authorization: Bearer <your_jwt_token>
```

3. **Token expires** after 24 hours (86400000 ms)

### Example Request with Token
```bash
curl -X GET http://localhost:8080/api/v1/orders/my-orders \
  -H "Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
```

---

## 📤 File Uploads

### Prescription File Upload

**Supported Formats**: JPG, JPEG, PNG, PDF

**Upload Location**: `uploads/prescriptions/`

**File Naming**: `prescription_<timestamp>.<extension>`

**Max File Size**: Configured in Spring Boot (default 10MB)

**Example Upload**:
```bash
curl -X POST http://localhost:8080/api/v1/prescriptions/upload \
  -H "Authorization: Bearer <token>" \
  -F "file=@prescription.jpg"
```

---

## 🔄 Request/Response Examples

### Example 1: User Registration

**Request**:
```bash
POST /api/v1/auth/register
Content-Type: application/json

{
  "fullName": "John Doe",
  "email": "john@example.com",
  "password": "SecurePass@123",
  "phone": "9876543210",
  "address": "123 Main St, Anytown, USA"
}
```

**Response** (200 OK):
```json
{
  "message": "User registered successfully"
}
```

---

### Example 2: User Login

**Request**:
```bash
POST /api/v1/auth/login
Content-Type: application/json

{
  "email": "john@example.com",
  "password": "SecurePass@123"
}
```

**Response** (200 OK):
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJqb2huQGV4YW1wbGUuY29tIiwiaWF0IjoxNjIwMDAwMDAwLCJleHAiOjE2MjAwODY0MDB9.signature",
  "userId": 1,
  "email": "john@example.com",
  "role": "USER"
}
```

---

### Example 3: Place Order

**Request**:
```bash
POST /api/v1/orders/place
Authorization: Bearer <jwt_token>
Content-Type: application/json

{
  "items": [
    {
      "medicineId": 1,
      "quantity": 2
    },
    {
      "medicineId": 3,
      "quantity": 1
    }
  ],
  "deliveryAddress": "123 Main St, Anytown, USA"
}
```

**Response** (200 OK):
```json
{
  "id": 1,
  "userId": 1,
  "orderDate": "2025-05-08T10:30:00",
  "totalAmount": 150.00,
  "status": "PENDING",
  "deliveryAddress": "123 Main St, Anytown, USA",
  "items": [
    {
      "id": 1,
      "medicineId": 1,
      "medicineName": "Aspirin",
      "quantity": 2,
      "unitPrice": 10.50,
      "subtotal": 21.00
    },
    {
      "id": 2,
      "medicineId": 3,
      "medicineName": "Ibuprofen",
      "quantity": 1,
      "unitPrice": 129.00,
      "subtotal": 129.00
    }
  ]
}
```

---

## 🐛 Troubleshooting

| Issue | Solution |
|-------|----------|
| Database connection refused | Ensure MySQL is running and credentials are correct |
| JWT token invalid | Token may have expired, re-login to get a new token |
| CORS errors | Ensure backend is running and CORS is configured |
| Port 8080 in use | Change port in `application.properties` |
| Module not found (frontend) | Run `npm install` in pharmacy-frontend directory |

---

## 📝 Environment Variables

### Backend (.env or application.properties)
```properties
SPRING_DATASOURCE_URL=jdbc:mysql://localhost:3306/pharmacyodering_db
SPRING_DATASOURCE_USERNAME=root
SPRING_DATASOURCE_PASSWORD=Shiv@5502
JWT_SECRET=THIS_IS_A_SUPER_SECRET_KEY_FOR_PHARMACY_PROJECT
JWT_EXPIRATION=86400000
FILE_UPLOAD_DIR=uploads/prescriptions
```

### Frontend (.env)
```
VITE_API_URL=http://localhost:8080/api/v1
```

---

## 📚 Additional Resources

- [Spring Boot Documentation](https://spring.io/projects/spring-boot)
- [React Documentation](https://react.dev)
- [Vite Documentation](https://vitejs.dev)
- [JWT.io](https://jwt.io) - JWT Debugger & Documentation
- [MySQL Documentation](https://dev.mysql.com/doc/)

---

## 👥 Team & Support

For issues, questions, or feature requests, please create an issue in the repository or contact the development team.

---

## 📄 License

This project is private and proprietary. Unauthorized copying or distribution is prohibited.

---

## 🎯 Future Enhancements

- [ ] Payment gateway integration (Stripe, PayPal)
- [ ] Email notifications for orders
- [ ] SMS alerts for order status
- [ ] Admin dashboard analytics
- [ ] Medicine reviews and ratings
- [ ] Loyalty program
- [ ] Mobile app (React Native)
- [ ] Real-time order tracking
- [ ] Advanced search filters
- [ ] Wishlist feature

---

**Last Updated**: May 8, 2026

---
