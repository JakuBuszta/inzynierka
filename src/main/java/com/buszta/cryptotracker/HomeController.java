package com.buszta.cryptotracker;

import com.buszta.cryptotracker.common.CommonController;
import com.buszta.cryptotracker.security.SecurityUtilis;
import com.buszta.cryptotracker.user.User;
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

        User requestUser = SecurityUtilis.getUserFromSecurityContext();
        if (requestUser != null){
            model.addAttribute("watchlist", requestUser.getWatchListCoinIds());
        }

        model.addAttribute("coinMarkets", coinMarkets);
        return "home";
    }


}
