package com.example.cryptotracker.portfolio.historical;

import com.example.cryptotracker.user.User;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;

@Entity
@Data
public class CoinHistoricalData {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @CreationTimestamp
    private LocalDate placedAt;

    @ManyToOne
    private User user;

    private Double dataValue;

    private Double profitLoss;

    private String coinId;

    public CoinHistoricalData(User user, Double value, Double profitLoos, String coinId) {
        this.user = user;
        this.dataValue = value;
        this.profitLoss = profitLoos;
        this.coinId = coinId;
    }

    public CoinHistoricalData(){}
}
