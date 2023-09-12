package com.example.cryptotracker;

import com.example.cryptotracker.user.User;
import com.example.cryptotracker.user.UserRepository;
import com.litesoftwares.coingecko.domain.Coins.CoinMarkets;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import retrofit2.http.Path;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class HomeController {
    private final UserRepository userRepository;

    private final ApiCallScheduler apiCallScheduler;

    @GetMapping("/")
    private String defaultHome(Model model){
        return home("USD", model);
    }

    @GetMapping("/{symbol}")
    private String home(@PathVariable String symbol, Model model){

        if(symbol.equals("USD")){
            List<CoinMarkets> coinMarkets = apiCallScheduler.getCoinMarketsUSD();
            model.addAttribute("coinMarkets", coinMarkets);
            model.addAttribute("currencySymbol", "$");
        } else {
            List<CoinMarkets> coinMarkets = apiCallScheduler.getCoinMarketsPLN();
            model.addAttribute("coinMarkets", coinMarkets);
            model.addAttribute("currencySymbol", "zł");
        }

        return "home";
    }

    @GetMapping("/login")
    public String viewLoginPage(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication instanceof AnonymousAuthenticationToken){
            return "login";
        }

        return "redirect:/";
    }

    @GetMapping("/register")
    public String showRegistrationForm(Model model){
        User user = new User();
        model.addAttribute("user", user);
        return "register";
    }

    // handler method to handle user registration form submit request
    @PostMapping("/register/save")
    public String registration(@ModelAttribute("user") User user,
                               BindingResult result,
                               Model model){
        User existingUser = userRepository.findUserByEmail(user.getEmail());

        if(existingUser != null && existingUser.getEmail() != null && !existingUser.getEmail().isEmpty()){
            result.rejectValue("email", null,
                    "There is already an account registered with the same email");
        }

        if(result.hasErrors()){
            model.addAttribute("user", user);
            return "/register";
        }

        userRepository.save(user);
        return "redirect:/register?success";
    }
}
