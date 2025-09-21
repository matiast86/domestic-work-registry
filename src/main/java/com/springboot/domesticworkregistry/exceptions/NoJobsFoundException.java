package com.springboot.domesticworkregistry.exceptions;

public class NoJobsFoundException extends RuntimeException {
    public NoJobsFoundException(String message) {
        super(message);
    }
}
