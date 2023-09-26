package com.example.cryptotracker;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.litesoftwares.coingecko.CoinGeckoApiClient;
import com.litesoftwares.coingecko.constant.Currency;
import com.litesoftwares.coingecko.domain.Coins.CoinMarkets;
import com.litesoftwares.coingecko.impl.CoinGeckoApiClientImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@EnableScheduling
@Component
@RequiredArgsConstructor
public class ApiCallScheduler {
    public CoinGeckoApiClient client = new CoinGeckoApiClientImpl();
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

    public String getImg(String coinId){
        for (CoinMarkets coin:
                coinMarketsUSD) {
            if(coin.getId().equals(coinId)){
                return coin.getImage();
            }
        }
        return "";
    }

    public BigDecimal getPrice(String coinId, String currencySymbol) {
        List<CoinMarkets> coinMarkets;

        if (currencySymbol.equals("USD")){
            coinMarkets = coinMarketsUSD;
        }else {
            coinMarkets = coinMarketsPLN;
        }

        for (CoinMarkets coin:
                coinMarkets) {
            if(coin.getId().equals(coinId)){
                return coin.getCurrentPrice();
            }
        }
        return BigDecimal.ZERO;
    }

}
