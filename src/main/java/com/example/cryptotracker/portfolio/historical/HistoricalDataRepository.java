package com.example.cryptotracker.portfolio.historical;

import com.example.cryptotracker.user.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HistoricalDataRepository extends CrudRepository<HistoricalData, Long> {

    List<HistoricalData> findByUser(User user);

}
