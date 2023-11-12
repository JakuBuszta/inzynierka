package com.buszta.cryptotracker.portfolio.historical;

import com.buszta.cryptotracker.user.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HistoricalDataRepository extends CrudRepository<HistoricalData, Long> {

    List<HistoricalData> findByUser(User user);

}
