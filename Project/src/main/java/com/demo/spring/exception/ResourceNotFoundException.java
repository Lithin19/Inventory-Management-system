package com.demo.spring.exception;

public class ResourceNotFoundException extends RuntimeException{
    public ResourceNotFoundException(String message){
        super(message);
    }
}

//Used when product is not found (GET / PUT / DELETE)