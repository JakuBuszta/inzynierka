package com.example.cryptotracker.user;

import com.example.cryptotracker.security.SecurityUtilis;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;


    public String saveCurrency(String currencySymbol, HttpSession session) {
        User requestUser = SecurityUtilis.getUserFromSecurityContext();

        if (requestUser != null) {
            requestUser.setCurrencySymbol(currencySymbol);
            userRepository.save(requestUser);
        } else {
            session.setAttribute("currencySymbol", currencySymbol);
        }

        return currencySymbol;
    }
}
