package com.servicedata.servicelogs.exceptions;

public class CompanyNotFoundException extends RuntimeException{
	
	public CompanyNotFoundException(String message) {
        super(message);
    }
}