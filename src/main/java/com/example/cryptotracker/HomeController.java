package com.example.cryptotracker;

import com.example.cryptotracker.common.CommonController;
import com.litesoftwares.coingecko.domain.Coins.CoinMarkets;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class HomeController extends CommonController {
    private final ApiCallScheduler apiCallScheduler;

    public HomeController(ApiCallScheduler apiCallScheduler, ApiCallScheduler apiCallScheduler1) {
        super(apiCallScheduler);
        this.apiCallScheduler = apiCallScheduler1;
    }

    @GetMapping("/")
    private String home(Model model){
        List<CoinMarkets> coinMarkets = apiCallScheduler.getCoinMarketsUSD();
        model.addAttribute("coinMarkets", coinMarkets);
        return "home";
    }


}
