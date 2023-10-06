package com.example.cryptotracker;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.example.cryptotracker.portfolio.*;
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
    private final HistoricalDataRepository historicalDataRepository;
    private final UserRepository userRepository;
    private final TransactionRepository transactionRepository;
    public CoinGeckoApiClient client = new CoinGeckoApiClientImpl();
    List<CoinMarkets> coinMarketsUSD = new ArrayList<>();
    List<CoinMarkets> coinMarketsPLN = new ArrayList<>();

    ExchangeById binance = new ExchangeById();
    ExchangeById kraken = new ExchangeById();
    ExchangeById crypto_com = new ExchangeById();
    ExchangeById kucoin = new ExchangeById();
    ExchangeById bybit_spot = new ExchangeById();
    ExchangeById okex = new ExchangeById();
    ExchangeById bingx = new ExchangeById();
    private Double totalMarketCapUSD;
    private Double totalMarketCapPLN;

    @Scheduled(fixedRate = 120000) //60000 - 1 min 5000 - 1 sec
    public void reportCurrentTime() {
        coinMarketsUSD = client.getCoinMarkets(Currency.USD);
        coinMarketsPLN = client.getCoinMarkets(Currency.PLN);
        totalMarketCapUSD = client.getGlobal().getData().getTotalMarketCap().get(Currency.USD);
        totalMarketCapPLN = client.getGlobal().getData().getTotalMarketCap().get(Currency.PLN);

        binance = client.getExchangesById("binance");
        kraken = client.getExchangesById("kraken");
        crypto_com = client.getExchangesById("crypto_com");
        kucoin = client.getExchangesById("kucoin");
        bybit_spot = client.getExchangesById("bybit_spot");
        okex = client.getExchangesById("okex");
        bingx = client.getExchangesById("bingx");
    }

    public BigDecimal getTotalMarketCap(String currency) {
        if (currency.equals("usd")) {
            return new BigDecimal(totalMarketCapUSD);
        } else {
            return new BigDecimal(totalMarketCapPLN);
        }
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

    @Scheduled(fixedRate = 5000)
    @Transactional
    public void saveHistoricalData() {
        Iterable<User> users = userRepository.findAll();
        for (User user :
                users) {
            List<Transaction> listOfTransaction = transactionRepository.findAllByUser(user);
            List<Coin> listOfCoins = new ArrayList<>(listOfTransaction.size());

            listOfCoins.add(new Coin());
            if (listOfTransaction.size() > 0) {
                listOfCoins.get(0).setCoinId(listOfTransaction.get(0).getCoinId());
                listOfCoins.get(0).setQuantity(listOfTransaction.get(0).getQuantity());
                listOfCoins.get(0).setPricePaid(listOfTransaction.get(0).getPrice());
                listOfCoins.get(0).setImg(getImg(listOfCoins.get(0).getCoinId()));

                boolean newCrypto;
                for (int i = 1; i < listOfTransaction.size(); i++) {
                    newCrypto = true;
                    for (int j = 0; j < listOfCoins.size(); j++) {
                        if (listOfCoins.get(j).getCoinId().equals(listOfTransaction.get(i).getCoinId())) {
                            newCrypto = false;
                            listOfCoins.get(j).setPricePaid((listOfCoins.get(j).getPricePaid() * listOfCoins.get(j).getQuantity()
                                    + listOfTransaction.get(i).getPrice() * listOfTransaction.get(i).getQuantity())
                                    / (listOfCoins.get(j).getQuantity() + listOfTransaction.get(i).getQuantity()));
                            listOfCoins.get(j).setQuantity(listOfTransaction.get(i).getQuantity() + listOfCoins.get(j).getQuantity());
                        }
                        if (newCrypto && listOfCoins.size() - 1 == j) {
                            listOfCoins.add(new Coin());
                            listOfCoins.get(listOfCoins.size() - 1).setCoinId(listOfTransaction.get(i).getCoinId());
                            listOfCoins.get(listOfCoins.size() - 1).setQuantity(listOfTransaction.get(i).getQuantity());
                            listOfCoins.get(listOfCoins.size() - 1).setPricePaid(listOfTransaction.get(i).getPrice());
                            listOfCoins.get(listOfCoins.size() - 1).setImg(getImg(listOfTransaction.get(i).getCoinId()));

                            break;
                        }
                    }
                }

                double profitLoss = 0.0;
                double cost = 0.0;
                double totalValue = 0.0;

                for (Coin listOfCoin : listOfCoins) {
                    listOfCoin.setPercentages((Double.parseDouble(getPrice(listOfCoin.getCoinId(), user.getCurrencySymbol()).toString())
                            - listOfCoin.getPricePaid()) / listOfCoin.getPricePaid() * 100);
                    listOfCoin.setCurrentValue(Double.parseDouble(getPrice(listOfCoin.getCoinId(), user.getCurrencySymbol()).toString()));

                    cost += listOfCoin.getPricePaid() * listOfCoin.getQuantity();
                    totalValue += listOfCoin.getCurrentValue() * listOfCoin.getQuantity();
                }

                profitLoss = totalValue - cost;

                List<HistoricalData> compressHistoricalData = historicalDataRepository.findByUser(user);
                if (compressHistoricalData.size() != 0) {
                    if (compressHistoricalData.get(compressHistoricalData.size() - 1).getPlacedAt().getDayOfYear() == (LocalDate.now().getDayOfYear())
                            && compressHistoricalData.get(compressHistoricalData.size() - 1).getPlacedAt().getYear() == (LocalDate.now().getYear())) {
                        compressHistoricalData.get(compressHistoricalData.size() - 1).setDataValue(totalValue);
                        compressHistoricalData.get(compressHistoricalData.size() - 1).setProfitLoss(profitLoss);
                        System.out.println("1");
                    } else {
                        System.out.println("2");
                        historicalDataRepository.save(new HistoricalData(user, totalValue, profitLoss));
                    }
                } else {
                    System.out.println("3");
                    historicalDataRepository.save(new HistoricalData(user, totalValue, profitLoss));
                }


            }
        }
    }


    public List<CoinMarkets> getCoinMarketsUSD() {
        return coinMarketsUSD;
    }

    public List<CoinMarkets> getCoinMarketsPLN() {
        return coinMarketsPLN;
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

    public BigDecimal getPrice(String coinId, String currencySymbol) {
        List<CoinMarkets> coinMarkets;

        if (currencySymbol.equals("USD")) {
            coinMarkets = coinMarketsUSD;
        } else {
            coinMarkets = coinMarketsPLN;
        }

        for (CoinMarkets coin :
                coinMarkets) {
            if (coin.getId().equals(coinId)) {
                return coin.getCurrentPrice();
            }
        }
        return BigDecimal.ZERO;
    }

}
