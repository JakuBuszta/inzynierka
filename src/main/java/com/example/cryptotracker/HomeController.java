package com.example.cryptotracker;

import com.example.cryptotracker.common.CommonController;
import com.example.cryptotracker.security.SecurityUtilis;
import com.example.cryptotracker.user.User;
import com.litesoftwares.coingecko.domain.Coins.CoinMarkets;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
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
    private String defaultHome(Model model, HttpSession session){
        User requestUser = SecurityUtilis.getUserFromSecurityContext();

        String currencySymbol = (String) session.getAttribute("currencySymbol");
        System.out.println(currencySymbol);
        if (requestUser == null){
            if (currencySymbol == null){
                return home("usd", model);
            }
            return home(currencySymbol, model);
        }

        return home(requestUser.getCurrencySymbol(), model);
    }

    @GetMapping("/{symbol}")
    private String home(@PathVariable String symbol, Model model){

        if(symbol.equals("usd")){
            List<CoinMarkets> coinMarkets = apiCallScheduler.getCoinMarketsUSD();
            model.addAttribute("coinMarkets", coinMarkets);
//            model.addAttribute("currencySymbol", "$");
//            model.addAttribute("totalMarketCapValue", apiCallScheduler.getTotalMarketCap("usd"));
        } else {
            List<CoinMarkets> coinMarkets = apiCallScheduler.getCoinMarketsPLN();
            model.addAttribute("coinMarkets", coinMarkets);
//            model.addAttribute("currencySymbol", "zł");
//            model.addAttribute("totalMarketCapValue", apiCallScheduler.getTotalMarketCap("pln"));
        }

        return "home";
    }
}
