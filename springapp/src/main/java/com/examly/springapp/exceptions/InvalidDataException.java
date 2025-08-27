package com.examly.springapp.exceptions;

public class InvalidDataException extends RuntimeException{
    public InvalidDataException(String message){
        super(message);
    }
}
