package com.buszta.cryptotracker;

import com.buszta.cryptotracker.portfolio.Coin;
import com.buszta.cryptotracker.portfolio.historical.HistoricalData;
import com.buszta.cryptotracker.portfolio.historical.HistoricalDataRepository;
import com.buszta.cryptotracker.portfolio.transaction.Transaction;
import com.buszta.cryptotracker.portfolio.transaction.TransactionRepository;
import com.buszta.cryptotracker.user.User;
import com.litesoftwares.coingecko.domain.Coins.CoinMarkets;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ApiCallSchedulerHelper {

    private final TransactionRepository transactionRepository;
    private final HistoricalDataRepository historicalDataRepository;

    public String getImg(String coinId, List<CoinMarkets> coinMarketsUSD) {
        for (CoinMarkets coin :
                coinMarketsUSD) {
            if (coin.getId().equals(coinId)) {
                return coin.getImage();
            }
        }
        return "";
    }

    public BigDecimal getPrice(String coinId, List<CoinMarkets> coinMarkets) {
        for (CoinMarkets coin :
                coinMarkets) {
            if (coin.getId().equals(coinId)) {
                return coin.getCurrentPrice();
            }
        }
        return BigDecimal.ZERO;
    }


    public List<Transaction> findAllByUser(User user){
        return transactionRepository.findAllByUser(user);
    }

    public List<Coin> getListOfCoinsByCompressedTransaction(User user, List<CoinMarkets> coinMarketsUSD) {
        List<Transaction> listOfTransaction = findAllByUser(user);

        if (listOfTransaction.isEmpty()) {
            return List.of();
        }

        Map<String, Coin> coinMap = new HashMap<>();

        for (Transaction transaction : listOfTransaction) {
            String coinId = transaction.getCoinId();

            if (coinMap.containsKey(coinId)) {
                Coin coin = coinMap.get(coinId);
                coin.setPricePaid((coin.getPricePaid() * coin.getQuantity() + transaction.getPrice() * transaction.getQuantity())
                        / (coin.getQuantity() + transaction.getQuantity()));
                coin.setQuantity(coin.getQuantity() + transaction.getQuantity());
            } else {
                Coin newCoin = new Coin();
                newCoin.setCoinId(coinId);
                newCoin.setQuantity(transaction.getQuantity());
                newCoin.setPricePaid(transaction.getPrice());
                newCoin.setImg(getImg(coinId, coinMarketsUSD));
                coinMap.put(coinId, newCoin);
            }
        }

        return new ArrayList<>(coinMap.values());
    }


    @Transactional
    public void calculateProfitLoss(List<Coin> listOfCoins, User requestUser, List<CoinMarkets> coinMarketsUSD){
        double profitLoss, cost = 0.0, totalValue = 0.0;

        for (Coin listOfCoin : listOfCoins) {
            Double currentPrice = Double.valueOf(getPrice(listOfCoin.getCoinId(), coinMarketsUSD).toString());
            listOfCoin.setPercentages((currentPrice - listOfCoin.getPricePaid()) / listOfCoin.getPricePaid() * 100);

            listOfCoin.setCurrentValue(Double.valueOf(getPrice(listOfCoin.getCoinId(), coinMarketsUSD).toString()));

            cost += listOfCoin.getPricePaid() * listOfCoin.getQuantity();
            totalValue += listOfCoin.getCurrentValue() * listOfCoin.getQuantity();
        }
        profitLoss = totalValue - cost;

        List<HistoricalData> compressHistoricalData = historicalDataRepository.findByUser(requestUser);

        if (compressHistoricalData.size() != 0) {
            if (compressHistoricalData.get(compressHistoricalData.size() - 1).getPlacedAt().equals(LocalDate.now())) {
                compressHistoricalData.get(compressHistoricalData.size() - 1).setDataValue(totalValue);
                compressHistoricalData.get(compressHistoricalData.size() - 1).setProfitLoss(profitLoss);
            } else {
                historicalDataRepository.save(new HistoricalData(requestUser, totalValue, profitLoss));
            }
        } else {
            historicalDataRepository.save(new HistoricalData(requestUser, totalValue, profitLoss));
        }
    }
}
