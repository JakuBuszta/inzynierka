package com.example.cryptotracker.portfolio;

import com.example.cryptotracker.user.User;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;

@Entity
@Data
public class HistoricalData {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @CreationTimestamp
    private LocalDate placedAt;

    public HistoricalData(User user, Double value, Double profitLoos) {
        this.user = user;
        this.dataValue = value;
        this.profitLoss = profitLoos;
    }

    @ManyToOne
    private User user;

    private Double dataValue;
    private Double profitLoss;

    public HistoricalData() {}
}
