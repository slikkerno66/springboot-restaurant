package com.codeacademy.showcase.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class RestaurantCustomException extends RuntimeException {

    public HttpStatus httpStatus;
    public String messageKey;

    public RestaurantCustomException(String messageKey, HttpStatus httpStatus) {
        this.messageKey = messageKey;
        this.httpStatus = httpStatus;
    }

}
