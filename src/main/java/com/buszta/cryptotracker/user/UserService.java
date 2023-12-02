package com.buszta.cryptotracker.user;

import com.buszta.cryptotracker.ApiCallScheduler;
import com.buszta.cryptotracker.security.SecurityUtilis;
import com.litesoftwares.coingecko.domain.Coins.CoinMarkets;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final ApiCallScheduler apiCallScheduler;


    public String saveCurrency(String currencySymbol, HttpSession session) {
        User requestUser = SecurityUtilis.getUserFromSecurityContext();

        if (requestUser != null) {
//            requestUser.setCurrencySymbol(currencySymbol);
            userRepository.save(requestUser);
        } else {
            session.setAttribute("currencySymbol", currencySymbol);
        }

        return currencySymbol;
    }

    public String addCoinToWatchList(String coinName) {
        User requestUser = SecurityUtilis.getUserFromSecurityContext();

        requestUser.getWatchListCoinIds().add(coinName);
        userRepository.save(requestUser);

        return coinName;
    }

    public String removeFromWatchList(String coinName) {
        User requestUser = SecurityUtilis.getUserFromSecurityContext();

        requestUser.getWatchListCoinIds().remove(coinName);
        userRepository.save(requestUser);

        return coinName;
    }

    public List<CoinMarkets> getCoinsInWatchList() {
        User requestUser = SecurityUtilis.getUserFromSecurityContext();
        List<CoinMarkets> coinMarkets = apiCallScheduler.getCoinMarketsUSD();

        Set<String> watchListCoinIds = requestUser.getWatchListCoinIds();

        return coinMarkets.stream()
                .filter(coinMarkets1 -> watchListCoinIds.contains(coinMarkets1.getName()))
                .toList();
    }
}
