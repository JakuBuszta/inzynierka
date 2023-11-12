package com.buszta.cryptotracker.arbitrage;

import com.buszta.cryptotracker.ApiCallScheduler;
import com.litesoftwares.coingecko.domain.Exchanges.ExchangeById;
import com.litesoftwares.coingecko.domain.Shared.Ticker;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ArbitrageService {
    private final ApiCallScheduler apiCallScheduler;

    public void calculateBestPricesFor(String[] cryptoCurrencySymbols, Model model) {
        List<ExchangeById> exchanges = apiCallScheduler.getExchanges();

        for (String cryptoCurrency : cryptoCurrencySymbols) {
            List<Ticker> tickers = calculateArbitrage(cryptoCurrency);

            model.addAttribute(cryptoCurrency + "Min", tickers.get(0));
            model.addAttribute(cryptoCurrency + "Max", tickers.get(tickers.size()-1));
        }

        model.addAttribute("exchanges", exchanges);
        model.addAttribute("cryptoCurrencySymbols", cryptoCurrencySymbols);
    }

    private List<Ticker> calculateArbitrage(String cryptoCurrencySymbol) {
        List<ExchangeById> exchangeList = apiCallScheduler.getExchanges();
        List<Ticker> allTickers = new ArrayList<>();

        for (ExchangeById exchange : exchangeList) {
            allTickers.addAll(exchange.getTickers().stream()
                    .filter(ticker -> ticker.getBase().equals(cryptoCurrencySymbol) && ticker.getTarget().equals("USDT"))
                    .toList());
        }
        Collections.sort(allTickers);

        return allTickers;
    }
}
