package com.example.cryptotracker.portfolio.historical;

import com.example.cryptotracker.user.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CoinHistoricalDataRepository extends CrudRepository<CoinHistoricalData, Long> {

    List<CoinHistoricalData> findByUser(User user);

    List<CoinHistoricalData> findByUserAndCoinId(User user, String coinId);
}
