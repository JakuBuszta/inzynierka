package com.example.cryptotracker.portfolio.historical;

import com.example.cryptotracker.portfolio.Coin;
import com.example.cryptotracker.user.User;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CoinHistoricalDataService {

    private final CoinHistoricalDataRepository coinHistoricalDataRepository;


    @Transactional
    public void saveCoinHistoricalData(List<Coin> listOfCoins, User requestUser){
        double cost, totalValue = 0.0, profitLoss = 0.0;

        List<CoinHistoricalData> compressCoinHistoricalData = coinHistoricalDataRepository.findByUser(requestUser);
        if (compressCoinHistoricalData.size() != 0) {
            for (Coin coin : listOfCoins) {
                cost = coin.getPricePaid() * coin.getQuantity();
                totalValue = coin.getCurrentValue() * coin.getQuantity();
                profitLoss = totalValue - cost;


                Optional<CoinHistoricalData> first = compressCoinHistoricalData
                        .stream()
                        .filter(coinHistoricalData -> coinHistoricalData.getCoinId().equals(coin.getCoinId()))
                        .findFirst();

                if (first.isPresent()) {
                    first.get().setDataValue(totalValue);
                    first.get().setProfitLoss(profitLoss);
                } else {
                    coinHistoricalDataRepository.save(new CoinHistoricalData(requestUser, totalValue, profitLoss, coin.getCoinId()));
                }
            }
        } else {
            coinHistoricalDataRepository.save(new CoinHistoricalData(requestUser, totalValue, profitLoss, listOfCoins.get(0).getCoinId()));
        }

        coinHistoricalDataRepository.saveAll(compressCoinHistoricalData);
    }
}
