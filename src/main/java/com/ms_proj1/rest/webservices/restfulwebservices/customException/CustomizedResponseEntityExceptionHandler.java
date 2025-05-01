package com.ms_proj1.rest.webservices.restfulwebservices.customException;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
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

    /*the below function handles method arguments sent while calling it
    * it is present in ResponseEntityExceptionHandler
    * we have overridden it to make it return our custom error*/
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        ErrorDetails errorDetails = new ErrorDetails(LocalDateTime.now(),ex.getMessage(),request.getDescription(false));
        return new ResponseEntity(errorDetails, HttpStatus.BAD_REQUEST);
    }
}

/*Why does the exception handling go to CustomizedResponseEntityExceptionHandler instead of returning directly from the UserNotFoundException class, which already has @ResponseStatus(HttpStatus.NOT_FOUND)?

✅ Short Answer:
Spring prefers @ControllerAdvice global handlers over @ResponseStatus when both are present — and that’s exactly what’s happening.

🔍 Deep Dive:
You have two layers of exception handling for UserNotFoundException:
1. Inline exception metadata using @ResponseStatus:
java
Copy
Edit
@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class UserNotFoundException extends RuntimeException { ... }
This tells Spring:

“If this exception is thrown and no one catches it, return a 404 with a basic error response.”

This is useful for simple APIs or if you don’t need custom error structure (like timestamp, custom message, path, etc.).

2. Global Exception Handling with @ControllerAdvice:
java
Copy
Edit
@ExceptionHandler(UserNotFoundException.class)
public final ResponseEntity<Object> handleAllException(...) { ... }
This gives you full control over what the error response looks like — JSON body, timestamp, custom message, etc.

⚖️ Priority Order in Spring:
Spring will follow this priority when handling exceptions:

Priority	Mechanism	Used in your code?
1️⃣	@ExceptionHandler in @ControllerAdvice	✅ Yes
2️⃣	@ExceptionHandler in the controller itself	❌ No
3️⃣	@ResponseStatus on exception class	✅ Yes, but ignored if 1 exists

So in your case:

Spring sees that UserNotFoundException is thrown.

It checks for an exception handler.

Finds one in CustomizedResponseEntityExceptionHandler → uses that.

Ignores the @ResponseStatus on UserNotFoundException because a custom handler takes precedence.

✅ TL;DR:
Your @ResponseStatus(HttpStatus.NOT_FOUND) would only be used if no @ExceptionHandler existed.

But since you have a global @ControllerAdvice with an @ExceptionHandler(UserNotFoundException.class), that takes priority.

This lets you return structured JSON error details, not just a basic HTTP 404 with a string.

Would you like a quick demo on how to let both mechanisms coexist — using @ResponseStatus as a fallback when global handlers aren’t defined?*/
