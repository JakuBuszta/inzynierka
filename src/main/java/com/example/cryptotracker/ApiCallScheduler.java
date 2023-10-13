package com.example.cryptotracker;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.example.cryptotracker.portfolio.*;
import com.example.cryptotracker.portfolio.historical.CoinHistoricalDataService;
import com.example.cryptotracker.portfolio.transaction.TransactionService;
import com.example.cryptotracker.user.User;
import com.example.cryptotracker.user.UserRepository;
import com.litesoftwares.coingecko.CoinGeckoApiClient;
import com.litesoftwares.coingecko.constant.Currency;
import com.litesoftwares.coingecko.domain.Coins.CoinMarkets;
import com.litesoftwares.coingecko.domain.Exchanges.ExchangeById;
import com.litesoftwares.coingecko.impl.CoinGeckoApiClientImpl;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@EnableScheduling
@Component
@RequiredArgsConstructor
public class ApiCallScheduler {
    private final CoinHistoricalDataService coinHistoricalDataService;
    private final UserRepository userRepository;
    private final ApiCallSchedulerHelper apiCallSchedulerHelper;
    public CoinGeckoApiClient client = new CoinGeckoApiClientImpl();
    private List<CoinMarkets> coinMarketsUSD = new ArrayList<>();
    private ExchangeById binance = new ExchangeById();
    private ExchangeById kraken = new ExchangeById();
    private ExchangeById crypto_com = new ExchangeById();
    private ExchangeById kucoin = new ExchangeById();
    private ExchangeById bybit_spot = new ExchangeById();
    private ExchangeById okex = new ExchangeById();
    private ExchangeById bingx = new ExchangeById();
    private Double totalMarketCapUSD;


    @Scheduled(fixedRate = 200000) //60000 - 1 min 5000 - 1 sec
    public void getMarketData() {
        coinMarketsUSD = client.getCoinMarkets(Currency.USD);
        totalMarketCapUSD = client.getGlobal().getData().getTotalMarketCap().get(Currency.USD);
    }

    @Scheduled(fixedRate = 300000)
    public void getDataFromExchanges(){
        binance = client.getExchangesById("binance");
        kraken = client.getExchangesById("kraken");
        crypto_com = client.getExchangesById("crypto_com");
        kucoin = client.getExchangesById("kucoin");
        bybit_spot = client.getExchangesById("bybit_spot");
        okex = client.getExchangesById("okex");
        bingx = client.getExchangesById("bingx");
    }


    public BigDecimal getTotalMarketCap() {
        return new BigDecimal(totalMarketCapUSD);
    }


    public List<ExchangeById> getExchanges() {
        List<ExchangeById> exchangeList = new ArrayList<>();

        exchangeList.add(binance);
        exchangeList.add(bingx);
        exchangeList.add(kucoin);
        exchangeList.add(kraken);
        exchangeList.add(bybit_spot);
        exchangeList.add(crypto_com);
        exchangeList.add(okex);

        return exchangeList;
    }

    @Scheduled(fixedRate = 60000)
    @Transactional
    public void saveHistoricalData() {
        Iterable<User> users = userRepository.findAll();
        for (User user :
                users) {
            List<Coin> listOfCoins = apiCallSchedulerHelper.getListOfCoinsByCompressedTransaction(user, coinMarketsUSD);

            if (listOfCoins.size() > 0) {
                apiCallSchedulerHelper.calculateProfitLoss(listOfCoins, user, coinMarketsUSD);
                coinHistoricalDataService.saveCoinHistoricalData(listOfCoins, user);
            }
        }
    }


    public String getImg(String coinId) {
        for (CoinMarkets coin :
                coinMarketsUSD) {
            if (coin.getId().equals(coinId)) {
                return coin.getImage();
            }
        }
        return "";
    }

    public BigDecimal getPrice(String coinId) {
        List<CoinMarkets> coinMarkets;
        coinMarkets = coinMarketsUSD;


        for (CoinMarkets coin :
                coinMarkets) {
            if (coin.getId().equals(coinId)) {
                return coin.getCurrentPrice();
            }
        }
        return BigDecimal.ZERO;
    }

    public List<CoinMarkets> getCoinMarketsUSD() {
        return coinMarketsUSD;
    }
}
