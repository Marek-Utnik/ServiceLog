package com.bazakonserwacji.zeszyt.exceptions;

public class MachineNotFoundException extends RuntimeException{
    public MachineNotFoundException(String message) {
        super(message);
    }
}