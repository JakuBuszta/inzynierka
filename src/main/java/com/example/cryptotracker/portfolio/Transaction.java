package com.example.cryptotracker.portfolio;

import com.example.cryptotracker.user.User;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.util.Date;

@Entity
@Data
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @CreationTimestamp
    private Date placedAt;


    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private Double quantity;
    private Double price;
    private String coinId;
}
