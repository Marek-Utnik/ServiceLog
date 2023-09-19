package com.bazakonserwacji.zeszyt.exceptions;

public class CompanyNotFoundException extends RuntimeException{
    public CompanyNotFoundException(String message) {
        super(message);
    }
}