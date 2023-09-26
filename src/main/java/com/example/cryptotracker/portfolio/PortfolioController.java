package com.example.cryptotracker.portfolio;

import com.example.cryptotracker.ApiCallScheduler;
import com.example.cryptotracker.security.SecurityUtilis;
import com.example.cryptotracker.user.User;
import com.litesoftwares.coingecko.domain.Coins.CoinMarkets;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class PortfolioController {
    private final ApiCallScheduler apiCallScheduler;
    private final TransactionService transactionService;

    @GetMapping("/portfolio")
    private String viewPortfolioPage(Model model){
        User requestUser = SecurityUtilis.getUserFromSecurityContext();
        List<Transaction> listOfTransaction = transactionService.findAllByRequestUser();
        model.addAttribute("listOfCrypto", apiCallScheduler.getCoinMarketsUSD());

        if(listOfTransaction.isEmpty()){
            return "portfolio/portfolio_empty";
        }

        double currency = 0;
        if(requestUser.getCurrencySymbol().equals("USD")){
            currency = 1;
        }else if(requestUser.getCurrencySymbol().equals("PLN")){
            double valueInUSD = Double.parseDouble(apiCallScheduler.getPrice("bitcoin", "USD").toString());
            double valueInPLN = Double.parseDouble(apiCallScheduler.getPrice("bitcoin", "PLN").toString());
            currency = valueInPLN / valueInUSD;
        }

        List<Coin> listOfCoins = new ArrayList<>(listOfTransaction.size());

        listOfCoins.add(new Coin());
        listOfCoins.get(0).setCoinId(listOfTransaction.get(0).getCoinId());
        listOfCoins.get(0).setQuantity(listOfTransaction.get(0).getQuantity());
        listOfCoins.get(0).setPricePaid(currency * listOfTransaction.get(0).getPrice());
        listOfCoins.get(0).setImg(apiCallScheduler.getImg(listOfCoins.get(0).getCoinId()));
//        listOfCoins.get(0).setImg(coinsInfo.getImg(listOfCoins.get(0).getCoinId()));

        boolean newCrypto;
        for (int i = 1; i < listOfTransaction.size(); i++ ) {
            newCrypto = true;
            for(int j = 0; j < listOfCoins.size(); j++ ){
                if(listOfCoins.get(j).getCoinId().equals(listOfTransaction.get(i).getCoinId())){
                    newCrypto = false;
                    listOfCoins.get(j).setPricePaid(currency * (listOfCoins.get(j).getPricePaid()*listOfCoins.get(j).getQuantity()
                            +listOfTransaction.get(i).getPrice()*listOfTransaction.get(i).getQuantity())
                            /(listOfCoins.get(j).getQuantity()+listOfTransaction.get(i).getQuantity()));
                    listOfCoins.get(j).setQuantity(listOfTransaction.get(i).getQuantity() + listOfCoins.get(j).getQuantity());
                }
                if (newCrypto && listOfCoins.size()-1 == j) {
                    listOfCoins.add(new Coin());
                    listOfCoins.get(listOfCoins.size() - 1).setCoinId(listOfTransaction.get(i).getCoinId());
                    listOfCoins.get(listOfCoins.size() - 1).setQuantity(listOfTransaction.get(i).getQuantity());
                    listOfCoins.get(listOfCoins.size() - 1).setPricePaid(currency * listOfTransaction.get(i).getPrice());

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
            Double currentPrice = Double.valueOf(apiCallScheduler.getPrice(listOfCoin.getCoinId(), requestUser.getCurrencySymbol()).toString());
            listOfCoin.setPercentages((currentPrice - listOfCoin.getPricePaid()) / listOfCoin.getPricePaid() * 100);

            listOfCoin.setCurrentValue(Double.valueOf(apiCallScheduler.getPrice(listOfCoin.getCoinId(), requestUser.getCurrencySymbol()).toString()));

            cost += listOfCoin.getPricePaid() * listOfCoin.getQuantity();
            totalValue += listOfCoin.getCurrentValue() * listOfCoin.getQuantity();
        }

        profitLoss = totalValue - cost;
        percentagesOfTotalProfit = (totalValue - cost ) / cost * 100;

        model.addAttribute("list", listOfCoins);
        model.addAttribute("profitLoss", profitLoss);
        model.addAttribute("totalValue", totalValue);
        model.addAttribute("percentagesOfTotalProfit", percentagesOfTotalProfit);

        if(requestUser.getCurrencySymbol().equals("USD")){
            model.addAttribute("currencySymbol", "$");
        }else if(requestUser.getCurrencySymbol().equals("PLN")){
            model.addAttribute("currencySymbol", "zł");
        }

        return "portfolio/portfolio";
    }

    @PostMapping("/portfolio/add")
    private String addTransaction(Model model, @ModelAttribute CoinMarkets coin){
        User requestUser = SecurityUtilis.getUserFromSecurityContext();

        BigDecimal currentPriceOfClickedCrypto = apiCallScheduler.getPrice(coin.getId(), requestUser.getCurrencySymbol());

        model.addAttribute("crypto", coin);
        model.addAttribute("currentPriceOfClickedCrypto",currentPriceOfClickedCrypto);

        if(requestUser.getCurrencySymbol().equals("USD")){
            model.addAttribute("currencySymbol", "$");
        }else if(requestUser.getCurrencySymbol().equals("PLN")){
            model.addAttribute("currencySymbol", "zł");
        }

        return "transaction";
    }

    @PostMapping("/transaction")
    private String submitTransaction(@ModelAttribute Transaction transaction){
        User requestUser = SecurityUtilis.getUserFromSecurityContext();

        if (requestUser.getCurrencySymbol().equals("PLN")){
            double valueInUSD = Double.parseDouble(apiCallScheduler.getPrice(transaction.getCoinId(), "USD").toString());
            double valueInPLN = Double.parseDouble(apiCallScheduler.getPrice(transaction.getCoinId(), "PLN").toString());
            double plnVsUsd = valueInPLN / valueInUSD;

            transaction.setPrice(transaction.getPrice() / plnVsUsd);
        }

        transactionService.save(transaction);

//        List<Transaction> allByRequestUser = transactionService.findAllByRequestUser();
//
//        model.addAttribute("list", allByRequestUser);

        return "redirect:/portfolio";
    }

}
