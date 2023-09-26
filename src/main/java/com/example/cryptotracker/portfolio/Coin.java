package com.example.cryptotracker.portfolio;

import lombok.Data;

@Data
public class Coin {
    private Double quantity;
    private Double pricePaid;
    private Double currentValue;
    private String coinId;
    private String img;
    private Double percentages;
    public Coin() {
    }
}
