package com.example.cryptotracker.arbitrage;

import com.example.cryptotracker.ApiCallScheduler;
import com.example.cryptotracker.common.CommonController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ArbitrageController extends CommonController {

    private final ArbitrageService arbitrageService;
    private final String[] cryptoCurrencySymbols = {"BTC", "ETH", "BNB", "XRP", "SOL",
            "ADA", "DOGE", "TRX", "TON", "MATIC", "DOT", "LTC", "MANA"};

    public ArbitrageController(ApiCallScheduler apiCallScheduler, ArbitrageService arbitrageService) {
        super(apiCallScheduler);
        this.arbitrageService = arbitrageService;
    }

    @GetMapping("/arbitrage")
    private String arbitrage(Model model){
        arbitrageService.calculateBestPricesFor(cryptoCurrencySymbols, model);
        return "arbitrage";
    }
}
