package com.buszta.cryptotracker.portfolio.transaction;

import com.buszta.cryptotracker.user.User;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDate;
import java.util.Date;

@Entity
@Data
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @CreationTimestamp
    private LocalDate placedAt;


    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @NotNull
    private Double quantity;
    @NotNull
    private Double price;
    @NotNull
    private String coinId;

    public Transaction() {}
}
