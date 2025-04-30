package com.ms_proj1.rest.webservices.restfulwebservices.helloworld;

public class MessageBean {
    private String message;
    public MessageBean(String message) {
        this.message =  message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "MessageBean{" +
                "message='" + message + '\'' +
                '}';
    }
}
