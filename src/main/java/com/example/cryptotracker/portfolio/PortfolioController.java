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
import java.util.List;

@Controller
@RequiredArgsConstructor
public class PortfolioController {
    private final ApiCallScheduler apiCallScheduler;
    private final TransactionService transactionService;

    @GetMapping("/portfolio")
    private String viewPortfolioPage(Model model){
        List<Transaction> allByRequestUser = transactionService.findAllByRequestUser();

        model.addAttribute("listOfCrypto", apiCallScheduler.getCoinMarketsUSD());

        if(allByRequestUser.isEmpty()){
            return "portfolio/portfolio_empty";
        }
        return "portfolio/portfolio";
    }

    @PostMapping("/portfolio/add")
    private String addTransaction(Model model, @ModelAttribute CoinMarkets coin){
        User requestUser = SecurityUtilis.getUserFromSecurityContext();

        BigDecimal currentPriceOfClickedCrypto = apiCallScheduler.getPrice(coin.getId(), requestUser.getCurrencySymbol());

        model.addAttribute("crypto", coin);
        model.addAttribute("currentPriceOfClickedCrypto",currentPriceOfClickedCrypto);
        model.addAttribute("currencySymbol", requestUser.getCurrencySymbol());

        return "transaction";
    }

    @PostMapping("/transaction")
    private String submitTransaction(@ModelAttribute Transaction transaction){
        transactionService.save(transaction);


        return "redirect:/portfolio";
    }

}
