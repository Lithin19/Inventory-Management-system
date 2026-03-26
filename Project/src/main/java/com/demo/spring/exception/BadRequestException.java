package com.demo.spring.exception;

public class BadRequestException extends RuntimeException{
    public BadRequestException(String message){
        super(message);
    }
}

//Used for invalid inputs (negative quantity, missing fields)