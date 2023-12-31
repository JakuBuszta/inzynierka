package com.buszta.cryptotracker.portfolio.transaction;

import com.buszta.cryptotracker.portfolio.Coin;
import com.buszta.cryptotracker.portfolio.historical.HistoricalData;
import com.buszta.cryptotracker.portfolio.historical.HistoricalDataRepository;
import com.buszta.cryptotracker.ApiCallScheduler;
import com.buszta.cryptotracker.security.SecurityUtilis;
import com.buszta.cryptotracker.user.User;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepository transactionRepository;

    private final HistoricalDataRepository historicalDataRepository;
    private final ApiCallScheduler apiCallScheduler;


    public void deleteCoinFromPortfolio(String cryptoId){
        List<Transaction> transactions = findAllByRequestUser();

        for (Transaction transaction : transactions) {
            if (transaction.getCoinId().equals(cryptoId)) {
                deleteById(transaction.getId());
            }
        }
    }

    public void save(Transaction transaction) {
        transaction.setUser(SecurityUtilis.getUserFromSecurityContext());
        transactionRepository.save(transaction);
    }

    public List<Transaction> findAllByRequestUser() {
        User requestUser = SecurityUtilis.getUserFromSecurityContext();
        return transactionRepository.findAllByUser(requestUser);
    }

    public List<Transaction> findAllByUser(User user){
        return transactionRepository.findAllByUser(user);
    }

    private void deleteById(Integer transactionId) {
        transactionRepository.deleteById(transactionId);
    }

    public List<Coin> getListOfCoinsByCompressedTransaction(User user) {
        List<Transaction> listOfTransaction = findAllByUser(user);

        if (listOfTransaction.isEmpty()) {
            return List.of();
        }

        Map<String, Coin> coinMap = new HashMap<>();

        for (Transaction transaction : listOfTransaction) {
            String coinId = transaction.getCoinId();

            if (coinMap.containsKey(coinId)) {
                Coin coin = coinMap.get(coinId);
                if (coin.getQuantity() + transaction.getQuantity() == 0 || coin.getQuantity() + transaction.getQuantity() == 0.0){
                    coin.setPricePaid(0.0);
                }else{
                    coin.setPricePaid((coin.getPricePaid() * coin.getQuantity() + transaction.getPrice()
                            * transaction.getQuantity()) / (coin.getQuantity() + transaction.getQuantity()));
                }
                coin.setQuantity(coin.getQuantity() + transaction.getQuantity());
            } else {
                Coin newCoin = new Coin();
                newCoin.setCoinId(coinId);
                newCoin.setQuantity(transaction.getQuantity());
                newCoin.setPricePaid(transaction.getPrice());
                newCoin.setImg(apiCallScheduler.getImg(coinId));
                coinMap.put(coinId, newCoin);
            }
        }

        List<Coin> listOfCoins = new ArrayList<>(coinMap.values());

        for (int i = 0; i < listOfCoins.size(); i++) {
            if (listOfCoins.get(i).getQuantity() == 0 || listOfCoins.get(i).getQuantity() == 0.0){
                listOfCoins.remove(listOfCoins.get(i));
            }
        }


        return listOfCoins;
    }


    @Transactional
    public void calculateProfitLoss(List<Coin> listOfCoins, Model model, User requestUser){
        double profitLoss, percentagesOfTotalProfit, cost = 0.0, totalValue = 0.0;

        for (Coin listOfCoin : listOfCoins) {
            Double currentPrice = Double.valueOf(apiCallScheduler.getPrice(listOfCoin.getCoinId()).toString());
            listOfCoin.setPercentages((currentPrice - listOfCoin.getPricePaid()) / listOfCoin.getPricePaid() * 100);

            listOfCoin.setCurrentValue(Double.valueOf(apiCallScheduler.getPrice(listOfCoin.getCoinId()).toString()));

            cost += listOfCoin.getPricePaid() * listOfCoin.getQuantity();
            totalValue += listOfCoin.getCurrentValue() * listOfCoin.getQuantity();
        }
        profitLoss = totalValue - cost;
        percentagesOfTotalProfit = (totalValue - cost) / cost * 100;

        if (model != null){
            model.addAttribute("list", listOfCoins);
            model.addAttribute("profitLoss", profitLoss);
            model.addAttribute("totalValue", totalValue);
            model.addAttribute("percentagesOfTotalProfit", percentagesOfTotalProfit);
        }

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

    public double getUserCryptoQuantity(String coinName) {
        User requestUser = SecurityUtilis.getUserFromSecurityContext();

        List<Transaction> transactions = transactionRepository.findAllByUserAndCoinId(requestUser, coinName);

        double quantity = 0;
        for (Transaction transaction : transactions) {
            quantity += transaction.getQuantity();
        }

        return quantity;
    }
}
