package com.buszta.cryptotracker.portfolio.historical;

import com.buszta.cryptotracker.user.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CoinHistoricalDataRepository extends CrudRepository<CoinHistoricalData, Long> {

    List<CoinHistoricalData> findByUser(User user);

    List<CoinHistoricalData> findByUserAndCoinId(User user, String coinId);
}
