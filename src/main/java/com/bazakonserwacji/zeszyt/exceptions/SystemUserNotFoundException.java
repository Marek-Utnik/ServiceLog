package com.bazakonserwacji.zeszyt.exceptions;

public class SystemUserNotFoundException extends RuntimeException{
    public SystemUserNotFoundException(String message) {
        super(message);
    }
}