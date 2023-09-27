package com.servicedata.servicelogs.exceptions;

public class MachineNotFoundException extends RuntimeException{
	
    public MachineNotFoundException(String message) {
        super(message);
    }
}