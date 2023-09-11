package com.example.cryptotracker;

import com.litesoftwares.coingecko.domain.Coins.CoinMarkets;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import retrofit2.http.Path;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class HomeController {

    private final ApiCallScheduler apiCallScheduler;

    @GetMapping("/")
    private String defaultHome(Model model){
        return home("USD", model);
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
