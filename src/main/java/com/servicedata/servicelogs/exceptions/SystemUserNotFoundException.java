package com.servicedata.servicelogs.exceptions;

public class SystemUserNotFoundException extends RuntimeException{
	
    public SystemUserNotFoundException(String message) {
        super(message);
    }
}