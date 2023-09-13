package com.example.cryptotracker.user;

import com.example.cryptotracker.security.SecurityUtilis;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;


    public String saveCurrency(String currencySymbol) {
        User requestUser = SecurityUtilis.getUserFromSecurityContext();

        requestUser.setCurrencySymbol(currencySymbol);
        userRepository.save(requestUser);

        return currencySymbol;
    }
}
