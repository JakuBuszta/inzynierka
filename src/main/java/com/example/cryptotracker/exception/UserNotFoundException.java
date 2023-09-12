package com.example.cryptotracker.exception;

public class UserNotFoundException extends Throwable {
    public UserNotFoundException() {
        super(ErrorMessages.USER_NOT_FOUND);
    }
}
