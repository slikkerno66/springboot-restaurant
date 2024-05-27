package com.codeacademy.showcase.exception;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@ControllerAdvice
public class GenericExceptionHandler /*extends ResponseEntityExceptionHandler*/ {

    @Autowired
    private MessageSource messageSource;

    @ExceptionHandler(RestaurantCustomException.class)
    public ResponseEntity<Object> handle(RestaurantCustomException e, Locale locale) {
        String errorCode = e.getMessageKey();

        String errorMessage = messageSource.getMessage(errorCode, new Object[]{}, locale);

        ErrorResponse errorResponse = ErrorResponse.builder().errorCode(errorCode).errorMessage(errorMessage).timeStamp(Instant.now().toString()).build();

        return new ResponseEntity<>(errorResponse, new HttpHeaders(), e.getHttpStatus());
    }

//    @Override
//    public ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
//                                                                  HttpHeaders headers,
//                                                                  HttpStatus status, WebRequest request) {
//        Map<String, String> errors = new HashMap<>();
//        ex.getBindingResult().getAllErrors().forEach((error) -> {
//            String fieldName = ((FieldError) error).getField();
//            String errorMessage = error.getDefaultMessage();
//            errors.put(fieldName, errorMessage);
//        });
//        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
//    }

//    @ExceptionHandler(MethodArgumentNotValidException.class)
//    @ResponseStatus(HttpStatus.BAD_REQUEST)
//    public ResponseEntity<List<ErrorResponse>> handleValidationExceptions(MethodArgumentNotValidException ex) {
//        List<ErrorResponse> errors = new ArrayList<>();
//
////        ex.getBindingResult().getAllErrors().forEach(error -> {
////            String fieldName = ((FieldError) error).getField();
////            String errorMessage = error.getDefaultMessage();
////            errors.add(new ValidationErrorResponse(fieldName, errorMessage));
////        });
//
//        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
//    }

//    @ResponseStatus(HttpStatus.BAD_REQUEST)
//    @ExceptionHandler(MethodArgumentNotValidException.class)
//    public Map<String, String> handleValidationException(MethodArgumentNotValidException ex) {
//        return null;
//    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleValidationException(MethodArgumentNotValidException ex) {
        return null;
    }

}
