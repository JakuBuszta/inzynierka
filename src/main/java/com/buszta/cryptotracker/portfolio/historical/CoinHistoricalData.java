package com.buszta.cryptotracker.portfolio.historical;

import com.buszta.cryptotracker.user.User;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.jetbrains.annotations.NotNull;

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

    @NotNull
    private Double dataValue;
    @NotNull
    private Double profitLoss;
    @NotNull
    private String coinId;

    public CoinHistoricalData(User user, @NotNull Double value, @NotNull Double profitLoos, @NotNull String coinId) {
        this.user = user;
        this.dataValue = value;
        this.profitLoss = profitLoos;
        this.coinId = coinId;
    }

    public CoinHistoricalData(){}
}
