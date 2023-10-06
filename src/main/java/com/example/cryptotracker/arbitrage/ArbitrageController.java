package com.example.cryptotracker.arbitrage;

import com.example.cryptotracker.ApiCallScheduler;
import com.example.cryptotracker.common.CommonController;
import com.litesoftwares.coingecko.domain.Exchanges.ExchangeById;
import com.litesoftwares.coingecko.domain.Shared.Ticker;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class ArbitrageController extends CommonController {

    private final ArbitrageService arbitrageService;
    private final ApiCallScheduler apiCallScheduler;
    private final String[] cryptoCurrencySymbols = {"BTC", "ETH", "BNB", "XRP", "SOL",
            "ADA", "DOGE", "TRX", "TON", "MATIC", "DOT", "LTC"};

    public ArbitrageController(ApiCallScheduler apiCallScheduler, ArbitrageService arbitrageService, ApiCallScheduler apiCallScheduler1) {
        super(apiCallScheduler);
        this.arbitrageService = arbitrageService;
        this.apiCallScheduler = apiCallScheduler1;
    }

    @GetMapping("/arbitrage")
    private String arbitrage(Model model){
        List<ExchangeById> exchanges = apiCallScheduler.getExchanges();

        for (String cryptoCurrency : cryptoCurrencySymbols) {
            List<Ticker> tickers = arbitrageService.calculateArbitrage(cryptoCurrency);

            model.addAttribute(cryptoCurrency + "Min", tickers.get(0));
            model.addAttribute(cryptoCurrency + "Max", tickers.get(tickers.size()-1));
        }

        model.addAttribute("exchanges", exchanges);
        model.addAttribute("cryptoCurrencySymbols", cryptoCurrencySymbols);
        return "arbitrage";
    }
}
