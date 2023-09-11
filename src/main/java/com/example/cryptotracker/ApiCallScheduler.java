package com.example.cryptotracker;

import java.util.ArrayList;
import java.util.List;

import com.litesoftwares.coingecko.CoinGeckoApiClient;
import com.litesoftwares.coingecko.constant.Currency;
import com.litesoftwares.coingecko.domain.Coins.CoinMarkets;
import com.litesoftwares.coingecko.impl.CoinGeckoApiClientImpl;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@EnableScheduling
@Component
public class ApiCallScheduler {
    CoinGeckoApiClient client = new CoinGeckoApiClientImpl();
    List<CoinMarkets> coinMarketsUSD = new ArrayList<>();
    List<CoinMarkets> coinMarketsPLN = new ArrayList<>();

    @Scheduled(fixedRate = 60000) //60000 - 1 min 5000 - 1 sec
    public void reportCurrentTime() {
        coinMarketsUSD = client.getCoinMarkets(Currency.USD);
        coinMarketsPLN = client.getCoinMarkets(Currency.PLN);
    }

    public List<CoinMarkets> getCoinMarketsUSD(){
        return coinMarketsUSD;
    }

    public List<CoinMarkets> getCoinMarketsPLN(){
        return coinMarketsPLN;
    }
}
