package com.buszta.cryptotracker.portfolio.historical;

import com.buszta.cryptotracker.user.User;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDate;

@Entity
@Data
@Table
public class HistoricalData {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @CreationTimestamp
    private LocalDate placedAt;

    public HistoricalData(User user, @NotNull Double value, @NotNull Double profitLoos) {
        this.user = user;
        this.dataValue = value;
        this.profitLoss = profitLoos;
    }

    @ManyToOne
    private User user;

    @NotNull
    private Double dataValue;
    @NotNull
    private Double profitLoss;

    public HistoricalData() {}
}
