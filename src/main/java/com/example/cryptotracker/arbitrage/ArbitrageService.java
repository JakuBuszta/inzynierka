package com.example.cryptotracker.arbitrage;

import com.example.cryptotracker.ApiCallScheduler;
import com.litesoftwares.coingecko.domain.Exchanges.ExchangeById;
import com.litesoftwares.coingecko.domain.Shared.Ticker;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ArbitrageService {
    private final ApiCallScheduler apiCallScheduler;

    public List<Ticker> calculateArbitrage(String cryptoCurrencySymbol) {
        List<ExchangeById> exchangeList = apiCallScheduler.getExchanges();
        List<Ticker> allTickers = new ArrayList<>();

        for (ExchangeById exchange : exchangeList) {
            allTickers.addAll(exchange.getTickers().stream()
                    .filter(ticker -> ticker.getBase().equals(cryptoCurrencySymbol) && ticker.getTarget().equals("USDT")).toList());
        }

        Collections.sort(allTickers);

        System.out.println(allTickers.get(0)); //min
        System.out.println(allTickers.get(allTickers.size()-1)); //max

        System.out.println(allTickers);


        return allTickers;
//        List<Ticker> binanceBTC = binance.getTickers().stream()
//                .filter(ticker -> ticker.getBase().equals("BTC") && ticker.getTarget().equals("USDT")).toList();


    }

}
