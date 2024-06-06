(Not Finish writing README.md)
This is the spring boot showcase project base on the final project requirement of this codeacademy course I studied.
https://www.codecademy.com/learn/paths/create-rest-apis-with-spring-and-java

    My project is not exactly the same as the final project's requirement of the course.
    I added basic spring security and some more complex business logic to the project.

## **About Project**
This project is the project to rate the restaurant in the system. There are 4 roles in this project. 

 1. ROLE_UNREGISTERED = This role will provide to the user who hasn't yet logged in.
 2. ROLE_REGISTERED = This role will provide whenever the user create the user to review the restaurant
 3. ROLE_OPERATOR = This role is the operator role and can only be created by ROLE_ADMIN
 4. ROLE_ADMIN = This is has full authority in the system

**Api authority**
ROLE_ADMIN can call to all apis in the system.
ROLE_OPERATOR can call to /operator/** and /user/**
ROLE_REGISTERED can only call to /user/**

**Error standardization**
Bean error (annotation validation) of this project has been standardized to be in this format.

    {
	    "type": "/user/create",
	    "timestamp": "2024-06-05T13:45:00.766125500Z",
	    "errors": [
	        {
	            "rejectedField": "zipCode",
	            "errorMessage": "zipCode must be in four digit format from 0001 to 9999. ex. 1234, 0001, 9999"
	        },
	        {
	            "rejectedField": "role",
	            "errorMessage": "role can only be ROLE_REGISTERED, ROLE_OPERATOR, or ROLE_ADMIN."
	        }
	    ]
    }
Other error in the code will be thrown using RestaurantCustomException class. It will be thrown in this format

    {
	    "errorCode": "REST-1",
	    "errorMessage": "username has already existed in the system.",
	    "timeStamp": "2024-06-05T13:49:17.956201500Z"
    }


    

**Postman Collection**
There is a postman collection in the project which you can access here 
[restaurnat.postman_collection.json](https://github.com/slikkerno66/springboot-restaurant/blob/main/restaurant.postman_collection.json)

> Please make sure you read complete documentation of the postman collection to Apis spec

**Swagger documentation**
The swagger document can be access using this link
http://localhost:8080/swagger-ui/index.html

## **Technical information**

These are tech stacks using in this project
 - Java
 - Spring boot 3.3.0
 - Embeded H2 database
 - Lombok *(installation required)*
 - Basic authentication
 - JPA hybernate
 - Swagger 3

**Design**

This project was design as a monolithic server.

**Database Design**

![Alt]([images/someimage.png](https://github.com/slikkerno66/springboot-restaurant/blob/main/restaurant-entity.drawio.png))
