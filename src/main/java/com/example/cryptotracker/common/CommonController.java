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
    public BigDecimal setTotalMarketCapValue() {
        return apiCallScheduler.getTotalMarketCap();
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
