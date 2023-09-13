package com.example.cryptotracker;

import com.example.cryptotracker.security.SecurityUtilis;
import com.example.cryptotracker.user.User;
import com.litesoftwares.coingecko.domain.Coins.CoinMarkets;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class HomeController {
    private final ApiCallScheduler apiCallScheduler;

    @GetMapping("/")
    private String defaultHome(Model model){
        User requestUser = SecurityUtilis.getUserFromSecurityContext();
        if (requestUser == null){
            return home("USD", model);
        }

        return home(requestUser.getCurrencySymbol(), model);
    }

    @GetMapping("/{symbol}")
    private String home(@PathVariable String symbol, Model model){

        if(symbol.equals("USD")){
            List<CoinMarkets> coinMarkets = apiCallScheduler.getCoinMarketsUSD();
            model.addAttribute("coinMarkets", coinMarkets);
            model.addAttribute("currencySymbol", "$");
        } else {
            List<CoinMarkets> coinMarkets = apiCallScheduler.getCoinMarketsPLN();
            model.addAttribute("coinMarkets", coinMarkets);
            model.addAttribute("currencySymbol", "zł");
        }

        return "home";
    }
}
