package com.buszta.cryptotracker.user;

import com.buszta.cryptotracker.ApiCallScheduler;
import com.buszta.cryptotracker.common.CommonController;
import com.litesoftwares.coingecko.domain.Coins.CoinMarkets;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class UserController extends CommonController {
    private final UserService userService;

    public UserController(ApiCallScheduler apiCallScheduler, UserService userService) {
        super(apiCallScheduler);
        this.userService = userService;
    }

    @GetMapping("/watchlist")
    private String getWatchlist(Model model){
        List<CoinMarkets> coinsInWatchList = userService.getCoinsInWatchList();

        model.addAttribute("coinMarkets", coinsInWatchList);
        return "watchlist";
    }
}
