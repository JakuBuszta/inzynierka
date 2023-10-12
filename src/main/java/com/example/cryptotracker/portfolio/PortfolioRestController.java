package com.example.cryptotracker.portfolio;

import com.example.cryptotracker.security.SecurityUtilis;
import com.example.cryptotracker.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class PortfolioRestController {

    private final CoinHistoricalDataRepository coinHistoricalDataRepository;


    @GetMapping("/portfolio/{coin}")
    public List<CoinHistoricalData> getCoinHistoricalData(@PathVariable String coin){
        User requestUser = SecurityUtilis.getUserFromSecurityContext();
        return coinHistoricalDataRepository.findByUserAndCoinId(requestUser, coin);
    }
}
