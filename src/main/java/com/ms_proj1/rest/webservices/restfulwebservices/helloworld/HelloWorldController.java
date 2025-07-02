package com.ms_proj1.rest.webservices.restfulwebservices.helloworld;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Locale;

//this should always be present in a sub-package where RestfulWebServiceApplication package is presnt
//@RestController is added to expose this method to a url
@RestController
public class HelloWorldController {

    private MessageSource messageSource;
    public HelloWorldController(MessageSource messageSource) {
        this.messageSource = messageSource;
    }
//    to expose
//    here to call this function we are mapping it to a request which is get and specifying the path or the url
//    in the browser type http://localhost:8080/hi and it will print the message
    @RequestMapping(method = RequestMethod.GET, path = "/hi")
    public String printMessage() {
        return "Hello-World";
    }
//    we can also use getmapping, in this request methode is not required
    @GetMapping(path = "/hi2")
    public String printMessage2() {
        return "message from printMessage2";
    }

    @GetMapping(path = "return-message-bean")
    public MessageBean returnMessageBean() {
        return new MessageBean("message-bean");
    }

//    {name} is a variable and rest are constants
//    http://localhost:8080/return-message/path-variable/{name}
//    {name is a path parameter and to catch it we use annotation @PathVariable}
    @GetMapping(path = "return-message/path-variable/{name}")
    public MessageBean returnMessageBeanPathVariable(@PathVariable String name) {
        return new MessageBean(String.format("returnMessageBeanPathVariable : %s",name));
    }

//    this function below is used to return internationalised versions as in return same response but in different languages
//    http://localhost:8080/hello_internationlised
    @GetMapping(path = "/hello_internationlised")
    public String printMessage3() {
        Locale locale = LocaleContextHolder.getLocale();
//        under resources there are two files created messaged.properties and messages_nl.properties which
//        define the configuration good.morning.message for it to return the correct response as per the language requested by consumer by setting header as Accept-Language and then nl for the dutch language
        return messageSource.getMessage("good.morning.message",null,"Default Message",locale);
//        return "message from hello_internationlised Good morning";
    }

}
