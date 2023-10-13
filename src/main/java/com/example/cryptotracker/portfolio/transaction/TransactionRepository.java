package com.example.cryptotracker.portfolio.transaction;

import com.example.cryptotracker.user.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepository extends CrudRepository<Transaction, Integer> {

    List<Transaction> findAllByUser(User user);
}
