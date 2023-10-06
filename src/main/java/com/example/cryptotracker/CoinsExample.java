package com.example.cryptotracker;

import com.litesoftwares.coingecko.CoinGeckoApiClient;
import com.litesoftwares.coingecko.constant.Currency;
import com.litesoftwares.coingecko.domain.Coins.CoinData.DeveloperData;
import com.litesoftwares.coingecko.domain.Coins.CoinData.IcoData;
import com.litesoftwares.coingecko.domain.Coins.CoinFullData;
import com.litesoftwares.coingecko.domain.Coins.CoinList;
import com.litesoftwares.coingecko.domain.Coins.CoinMarkets;
import com.litesoftwares.coingecko.domain.Coins.CoinTickerById;
import com.litesoftwares.coingecko.domain.Exchanges.ExchangeById;
import com.litesoftwares.coingecko.domain.Exchanges.Exchanges;
import com.litesoftwares.coingecko.domain.Exchanges.ExchangesList;
import com.litesoftwares.coingecko.domain.Exchanges.ExchangesTickersById;
import com.litesoftwares.coingecko.domain.Shared.Ticker;
import com.litesoftwares.coingecko.impl.CoinGeckoApiClientImpl;

import java.math.BigDecimal;
import java.util.List;

public class CoinsExample {
    public static void main(String[] args) {
//        String OMGContract = "0xd26114cd6EE289AccF82350c8d8487fedB8A0C07";
//        String platform = "ethereum";
//
//        CoinGeckoApiClient client = new CoinGeckoApiClientImpl();
//
//        System.out.println(client.getGlobal().getData().getTotalMarketCap().get("pln"));
//
//        CoinFullData bitcoinInfo = client.getCoinById("ethereum");
//        System.out.println(bitcoinInfo);
        //        System.out.println(client.getGlobal().getData());
//
//        List<CoinList> coinList = client.getCoinList();
//        System.out.println(coinList);
//
//        long totalCoins = coinList.size();
//        System.out.println(totalCoins);
//
//        List<CoinMarkets> coinMarkets = client.getCoinMarkets(Currency.USD);
//        System.out.println(coinMarkets);
//
//        CoinFullData bitcoinInfo = client.getCoinById("bitcoin");
//        System.out.println(bitcoinInfo);
//
//        String genesisDate = bitcoinInfo.getGenesisDate();
//        System.out.println(genesisDate);
//
//        DeveloperData bitcoinDevData = bitcoinInfo.getDeveloperData();
//        System.out.println(bitcoinDevData);
//
//        long bitcoinGithubStars = bitcoinDevData.getStars();
//        System.out.println(bitcoinGithubStars);
//
//        CoinTickerById bitcoinTicker = client.getCoinTickerById("bitcoin");
//        System.out.println(bitcoinTicker);
//
//        CoinFullData omiseGoInfo = client.getCoinInfoByContractAddress(platform, OMGContract);
//        System.out.println(omiseGoInfo);
//
//        IcoData omiseGoIcoInfo = omiseGoInfo.getIcoData();
//        String icoStartDate = omiseGoIcoInfo.getIcoStartDate();
//        System.out.println(icoStartDate);
//
//        List<List<String>> coinOHLC = client.getCoinOHLC("bitcoin", "usd", 1);
//        System.out.println(coinOHLC);



        CoinGeckoApiClient client = new CoinGeckoApiClientImpl();

        ExchangeById binance = client.getExchangesById("binance");
//        ExchangeById kraken = client.getExchangesById("kraken");
//        ExchangeById crypto_com = client.getExchangesById("crypto_com");
//        ExchangeById kucoin = client.getExchangesById("kucoin");
//        ExchangeById bybit_spot = client.getExchangesById("bybit_spot");
//        ExchangeById okex = client.getExchangesById("okex");
//        ExchangeById bingx = client.getExchangesById("bingx");


        List<Ticker> binanceBTC = binance.getTickers().stream()
                .filter(ticker -> ticker.getBase().equals("BTC") && ticker.getTarget().equals("USDT")).toList();


        System.out.println("getTickers: ---------------");
        System.out.println(binance.getTickers());

        System.out.println("binanceBTC: ---------------");
        System.out.println(binanceBTC);
        System.out.println("BINANCE: ---------------");
        System.out.println(binance.getName());
        System.out.println(binanceBTC.get(0).getMarket().getName());
        System.out.println(binance.getName() == binanceBTC.get(0).getMarket().getName());
        System.out.println(binance.getName().equals(binanceBTC.get(0).getMarket().getName()));
        System.out.println(binanceBTC.get(0).getMarket().getIdentifier());

//        String country = binance.getCountry();
//        System.out.println(country);
//
//        long startYear = binance.getYearEstablished();
//        System.out.println(startYear);
//
//        String websiteUrl = binance.getUrl();
//        System.out.println(websiteUrl);
//
//        String logoUrl = binance.getImage();
//        System.out.println(logoUrl);
//
//        BigDecimal tradeVolume = binance.getTradeVolume24hBtc();
//        System.out.println(tradeVolume);
//
//        ExchangesTickersById binanceTickers = client.getExchangesTickersById("binance");
//        System.out.println(binanceTickers.getTickers());
//
//        List<Exchanges> exchanges = client.getExchanges();
//        System.out.println(exchanges);
//
//        List<ExchangesList> exchangesList = client.getExchangesList();
//        System.out.println(exchangesList);
//
//        long totalExchanges = exchangesList.size();
//        System.out.println(totalExchanges);
    }
}
