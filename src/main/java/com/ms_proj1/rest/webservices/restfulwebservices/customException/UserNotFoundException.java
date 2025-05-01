package com.ms_proj1.rest.webservices.restfulwebservices.customException;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;


@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class UserNotFoundException extends RuntimeException{
    public UserNotFoundException(String message) {
        super(message);
/*when only super(message is sent we get a white label error with error code 500)
* we want to return a http response of not found
* to do this we use annotation of spring ResponseStatus for the class and return the http status
* now it will give the output as below
* Whitelabel Error Page
This application has no explicit mapping for /error, so you are seeing this as a fallback.

Tue Apr 29 11:16:33 IST 2025
There was an unexpected error (type=Not Found, status=404).
id:101
com.ms_proj1.rest.webservices.restfulwebservices.customException.UserNotFoundException: id:101
* Here still the error page has lot many other responses and if we don't want to see that then in dependency
* we can comment the devtools and this will now not show all the logs though in our code i did  not see it*/
    }
}
