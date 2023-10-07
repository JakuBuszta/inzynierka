package com.example.cryptotracker.portfolio;

import com.example.cryptotracker.ApiCallScheduler;
import com.example.cryptotracker.common.CommonController;
import com.example.cryptotracker.security.SecurityUtilis;
import com.example.cryptotracker.user.User;
import com.litesoftwares.coingecko.domain.Coins.CoinMarkets;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Controller
public class PortfolioController extends CommonController {
    private final ApiCallScheduler apiCallScheduler;
    private final TransactionService transactionService;
    private final HistoricalDataRepository historicalDataRepository;

    public PortfolioController(ApiCallScheduler apiCallScheduler, ApiCallScheduler apiCallScheduler1, TransactionService transactionService, HistoricalDataRepository historicalDataRepository) {
        super(apiCallScheduler);
        this.apiCallScheduler = apiCallScheduler1;
        this.transactionService = transactionService;
        this.historicalDataRepository = historicalDataRepository;
    }

    @GetMapping("/portfolio")
    private String viewPortfolioPage(Model model){
        User requestUser = SecurityUtilis.getUserFromSecurityContext();
        List<Transaction> listOfTransaction = transactionService.findAllByRequestUser();
        model.addAttribute("listOfCrypto", apiCallScheduler.getCoinMarketsUSD());

        if(listOfTransaction.isEmpty()){
            return "portfolio/portfolio_empty";
        }

        List<Coin> listOfCoins = new ArrayList<>(listOfTransaction.size());

        listOfCoins.add(new Coin());
        listOfCoins.get(0).setCoinId(listOfTransaction.get(0).getCoinId());
        listOfCoins.get(0).setQuantity(listOfTransaction.get(0).getQuantity());
        listOfCoins.get(0).setPricePaid(listOfTransaction.get(0).getPrice());
        listOfCoins.get(0).setImg(apiCallScheduler.getImg(listOfCoins.get(0).getCoinId()));

        boolean newCrypto;
        for (int i = 1; i < listOfTransaction.size(); i++ ) {
            newCrypto = true;
            for(int j = 0; j < listOfCoins.size(); j++ ){
                if(listOfCoins.get(j).getCoinId().equals(listOfTransaction.get(i).getCoinId())){
                    newCrypto = false;
                    listOfCoins.get(j).setPricePaid((listOfCoins.get(j).getPricePaid()*listOfCoins.get(j).getQuantity()
                            +listOfTransaction.get(i).getPrice()*listOfTransaction.get(i).getQuantity())
                            /(listOfCoins.get(j).getQuantity()+listOfTransaction.get(i).getQuantity()));
                    listOfCoins.get(j).setQuantity(listOfTransaction.get(i).getQuantity() + listOfCoins.get(j).getQuantity());
                }
                if (newCrypto && listOfCoins.size()-1 == j) {
                    listOfCoins.add(new Coin());
                    listOfCoins.get(listOfCoins.size() - 1).setCoinId(listOfTransaction.get(i).getCoinId());
                    listOfCoins.get(listOfCoins.size() - 1).setQuantity(listOfTransaction.get(i).getQuantity());
                    listOfCoins.get(listOfCoins.size() - 1).setPricePaid(listOfTransaction.get(i).getPrice());

                    listOfCoins.get(listOfCoins.size() - 1).setImg(apiCallScheduler.getImg(listOfTransaction.get(i).getCoinId()));

                    break;
                }
            }
        }

        double profitLoss = 0.0;
        double cost = 0.0;
        double totalValue= 0.0;
        double percentagesOfTotalProfit = 0.0;

        for (Coin listOfCoin : listOfCoins) {
            Double currentPrice = Double.valueOf(apiCallScheduler.getPrice(listOfCoin.getCoinId()).toString());
            listOfCoin.setPercentages((currentPrice - listOfCoin.getPricePaid()) / listOfCoin.getPricePaid() * 100);

            listOfCoin.setCurrentValue(Double.valueOf(apiCallScheduler.getPrice(listOfCoin.getCoinId()).toString()));

            cost += listOfCoin.getPricePaid() * listOfCoin.getQuantity();
            totalValue += listOfCoin.getCurrentValue() * listOfCoin.getQuantity();
        }

        profitLoss = totalValue - cost;
        percentagesOfTotalProfit = (totalValue - cost ) / cost * 100;

        model.addAttribute("list", listOfCoins);
        model.addAttribute("profitLoss", profitLoss);
        model.addAttribute("totalValue", totalValue);
        model.addAttribute("percentagesOfTotalProfit", percentagesOfTotalProfit);

        List<HistoricalData> compressHistoricalData = historicalDataRepository.findByUser(requestUser);
        if (compressHistoricalData.size() != 0){
            if (compressHistoricalData.get(compressHistoricalData.size()-1).getPlacedAt().equals(LocalDate.now())){
                compressHistoricalData.get(compressHistoricalData.size()-1).setDataValue(totalValue);
                compressHistoricalData.get(compressHistoricalData.size()-1).setProfitLoss(profitLoss);
            }else {
                historicalDataRepository.save(new HistoricalData(requestUser, totalValue, profitLoss));
            }
        }else{
            historicalDataRepository.save(new HistoricalData(requestUser, totalValue, profitLoss));
        }

        model.addAttribute("historicalData", historicalDataRepository.findByUser(requestUser));

        return "portfolio/portfolio";
    }

    @PostMapping("/portfolio/add")
    private String addTransaction(Model model, @ModelAttribute CoinMarkets coin){
        BigDecimal currentPriceOfClickedCrypto = apiCallScheduler.getPrice(coin.getId());

        model.addAttribute("crypto", coin);
        model.addAttribute("currentPriceOfClickedCrypto",currentPriceOfClickedCrypto);

        return "transaction";
    }

    @PostMapping("/transaction")
    private String submitTransaction(@ModelAttribute Transaction transaction){
        transactionService.save(transaction);

        return "redirect:/portfolio";
    }

    @PostMapping("/transaction/delete")
    private String deleteTransaction(@ModelAttribute("deleteCryptoId") String cryptoId){
        List<Transaction> transactions = transactionService.findAllByRequestUser();

        for (Transaction transaction : transactions) {
            if (transaction.getCoinId().equals(cryptoId)){
                transactionService.deleteById(transaction.getId());
            }
        }

        return "redirect:/portfolio";
    }

}
