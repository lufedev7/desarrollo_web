# API Documentation

Base URL: `http://localhost:8080/api`

## Authentication 🔐
All endpoints except Login and Register require a valid JWT token in the Authorization header:
```
Authorization: Bearer {your_jwt_token}
```

## Users Endpoints 👤

### Get All Users
```http
GET http://localhost:8080/api/users
Response: [
    {
        "id": 1,
        "userName": "string",
        "email": "string",
        "phoneNumber": "string",
        "userImage": "string"
    }
]
```

### Get User By ID
```http
GET http://localhost:8080/api/users/{id}
Response: {
    "id": 1,
    "userName": "string",
    "email": "string",
    "phoneNumber": "string",
    "userImage": "string"
}
```

### Register New User
```http
POST http://localhost:8080/api/auth/register
Body: {
    "userName": "string",
    "email": "string",
    "password": "string",
    "phoneNumber": "string",
    "userImage": "string"
}
Response: {
    "id": 1,
    "userName": "string",
    "token": "string"
}
```

### Login User
```http
POST http://localhost:8080/api/auth/login
Body: {
    "email": "string",
    "password": "string"
}
Response: {
    "id": 1,
    "userName": "string",
    "token": "string"
}
```

### Update User
```http
PUT http://localhost:8080/api/users/{id}
Body: {
    "userName": "string",
    "email": "string",
    "phoneNumber": "string",
    "userImage": "string"
}
```

### Delete User
```http
DELETE http://localhost:8080/api/users/{id}
Response: {
    "message": "User deleted successfully"
}
```

### Get My Products
```http
GET http://localhost:8080/api/users/my-products
Response: [
    {
        "productId": 1,
        "productName": "string",
        "productDescription": "string",
        "originalPrice": 0.00,
        "salePrice": 0.00,
        "isNew": true,
        "isSold": false
    }
]
```

## Products Endpoints 📦

### Get All Products
```http
GET http://localhost:8080/api/products
Query Parameters:
- page (optional): number
- size (optional): number
- sort (optional): string
Response: {
    "content": [
        {
            "productId": 1,
            "productName": "string",
            "categoryId": 1,
            "productDescription": "string",
            "originalPrice": 0.00,
            "salePrice": 0.00,
            "isNew": true,
            "isSold": false,
            "imageUrls": ["string"],
            "stockQuantity": 0
        }
    ],
    "totalPages": 1,
    "totalElements": 10,
    "size": 10,
    "number": 0
}
```

### Get Product By ID
```http
GET http://localhost:8080/api/products/{id}
Response: {
    "productId": 1,
    "productName": "string",
    "categoryId": 1,
    "productDescription": "string",
    "originalPrice": 0.00,
    "salePrice": 0.00,
    "isNew": true,
    "isSold": false,
    "imageUrls": ["string"],
    "stockQuantity": 0
}
```

### Create New Product
```http
POST http://localhost:8080/api/products/new
Body: {
    "productName": "string",
    "categoryId": 1,
    "productDescription": "string",
    "userId": 1,
    "originalPrice": 0.00,
    "salePrice": 0.00,
    "stockQuantity": 0
}
```

### Update Product
```http
PUT http://localhost:8080/api/products/{id}
Body: {
    "productName": "string",
    "categoryId": 1,
    "productDescription": "string",
    "originalPrice": 0.00,
    "salePrice": 0.00,
    "stockQuantity": 0
}
```

### Mark Product as Sold
```http
PATCH http://localhost:8080/api/products/{id}/sold
Response: {
    "productId": 1,
    "isSold": true
}
```

### Delete Product
```http
DELETE http://localhost:8080/api/products/{id}
Response: {
    "message": "Product deleted successfully"
}
```

### Get Products by Category
```http
GET http://localhost:8080/api/products/category/{categoryId}
Query Parameters:
- page (optional): number
- size (optional): number
Response: {
    "content": [Products],
    "totalPages": 1,
    "totalElements": 10
}
```

### Get Products by User
```http
GET http://localhost:8080/api/products/user/{userId}
Query Parameters:
- page (optional): number
- size (optional): number
Response: {
    "content": [Products],
    "totalPages": 1,
    "totalElements": 10
}
```

## Categories Endpoints 📑

### Get All Categories
```http
GET http://localhost:8080/api/categories
Response: [
    {
        "productCategoryId": 1,
        "categoryName": "string",
        "categoryImage": "string",
        "categoryDescription": "string"
    }
]
```

### Get Category By ID
```http
GET http://localhost:8080/api/categories/{id}
Response: {
    "productCategoryId": 1,
    "categoryName": "string",
    "categoryImage": "string",
    "categoryDescription": "string"
}
```

### Create New Category
```http
POST http://localhost:8080/api/categories
Body: {
    "categoryName": "string",
    "categoryImage": "string",
    "categoryDescription": "string"
}
```

### Update Category
```http
PUT http://localhost:8080/api/categories/{id}
Body: {
    "categoryName": "string",
    "categoryDescription": "string"
}
```

### Delete Category
```http
DELETE http://localhost:8080/api/categories/{id}
Response: {
    "message": "Category deleted successfully"
}
```

## Search Endpoint 🔍

### Search Products
```http
GET http://localhost:8080/api/products/search
Query Parameters:
- query: string
- page (optional): number
- size (optional): number
Response: {
    "content": [Products],
    "totalPages": 1,
    "totalElements": 10
}
```

## Common HTTP Status Codes
- 200: Success
- 201: Created
- 400: Bad Request
- 401: Unauthorized
- 403: Forbidden
- 404: Not Found
- 500: Internal Server Error
