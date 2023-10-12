package com.example.cryptotracker.exception;

public class CoinNotFoundException extends Throwable {
    public CoinNotFoundException() {
        super(ErrorMessages.COIN_NOT_FOUND);
    }
}