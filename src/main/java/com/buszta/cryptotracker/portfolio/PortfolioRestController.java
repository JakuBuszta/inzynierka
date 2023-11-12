package com.buszta.cryptotracker.portfolio;

import com.buszta.cryptotracker.portfolio.historical.CoinHistoricalData;
import com.buszta.cryptotracker.portfolio.historical.CoinHistoricalDataRepository;
import com.buszta.cryptotracker.security.SecurityUtilis;
import com.buszta.cryptotracker.user.User;
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
