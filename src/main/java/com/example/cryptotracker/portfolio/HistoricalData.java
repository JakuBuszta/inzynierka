package com.example.cryptotracker.portfolio;

import com.example.cryptotracker.user.User;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Data
@Entity
public class HistoricalData {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private LocalDate placedAt;

    @ManyToOne
//    @JoinColumn(name = "user_id")
    private User user;

    private Double value;
    private Double profitLoss;
}
