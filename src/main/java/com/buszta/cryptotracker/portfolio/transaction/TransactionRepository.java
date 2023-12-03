package com.buszta.cryptotracker.portfolio.transaction;

import com.buszta.cryptotracker.user.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepository extends CrudRepository<Transaction, Integer> {

    List<Transaction> findAllByUser(User user);

    List<Transaction> findAllByUserAndCoinId(User user, String coinId);
}
