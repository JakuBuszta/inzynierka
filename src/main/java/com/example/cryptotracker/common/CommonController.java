package com.example.cryptotracker.common;

import com.example.cryptotracker.ApiCallScheduler;
import com.example.cryptotracker.security.SecurityUtilis;
import com.example.cryptotracker.user.User;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.math.BigDecimal;

@RequiredArgsConstructor
public class CommonController {
    private final ApiCallScheduler apiCallScheduler;

    @ModelAttribute(value = "totalMarketCapValue")
    public BigDecimal setTotalMarketCapValue(HttpSession session) {
        User userFromSecurityContext = SecurityUtilis.getUserFromSecurityContext();
        if (userFromSecurityContext != null) {
            if (userFromSecurityContext.getCurrencySymbol().equals("usd")) {
                return apiCallScheduler.getTotalMarketCap("usd");
            } else if (userFromSecurityContext.getCurrencySymbol().equals("pln")) {
                return apiCallScheduler.getTotalMarketCap("pln");
            }
        }

        String currencySymbol = (String) session.getAttribute("currencySymbol");

        if (currencySymbol != null) {
            if (currencySymbol.equals("usd")) {
                return apiCallScheduler.getTotalMarketCap("usd");
            } else {
                return apiCallScheduler.getTotalMarketCap("pln");
            }
        }

        return apiCallScheduler.getTotalMarketCap("usd");
    }

    @ModelAttribute(value = "currencySymbol")
    public String setCurrencySymbol(HttpSession session) {
        User userFromSecurityContext = SecurityUtilis.getUserFromSecurityContext();
        if (userFromSecurityContext != null) {
            if (userFromSecurityContext.getCurrencySymbol().equals("usd")) {
                return "$";
            } else if (userFromSecurityContext.getCurrencySymbol().equals("pln")) {
                return "zł";
            }
        }

        String currencySymbol = (String) session.getAttribute("currencySymbol");

        if (currencySymbol != null) {
            if (currencySymbol.equals("usd")) {
                return "$";
            } else {
                return "zł";
            }
        }

        return "$";
    }
}
