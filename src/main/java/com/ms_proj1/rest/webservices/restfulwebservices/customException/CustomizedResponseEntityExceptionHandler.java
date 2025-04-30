package com.ms_proj1.rest.webservices.restfulwebservices.customException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;

/*@ControllerAdvice is a special annotation in Spring Framework (Java)
used to define global exception handling, data binding,
or model attribute logic across multiple controllers — essentially,
it applies to all @Controller classes in your application.*/
@ControllerAdvice
public class CustomizedResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

//    this signature of function is take from handleException from
//    ResponseEntityExceptionHandler
    @ExceptionHandler(UserNotFoundException.class)
    public final ResponseEntity<Object> handleAllException(Exception ex, WebRequest request) throws Exception {
        ErrorDetails errorDetails = new ErrorDetails(LocalDateTime.now(),ex.getMessage(),request.getDescription(false));
        return new ResponseEntity(errorDetails, HttpStatus.NOT_FOUND);
    }

}
