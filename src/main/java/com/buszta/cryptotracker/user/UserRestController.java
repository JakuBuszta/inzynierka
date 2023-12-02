package com.buszta.cryptotracker.user;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserRestController {
    private final UserService userService;

    @PostMapping("/watchlist/{coinName}")
    public String addCoinToWatchList(@PathVariable String coinName){
        return userService.addCoinToWatchList(coinName);
    }

    @DeleteMapping("/watchlist/{coinName}")
    public String removeFromWatchList(@PathVariable String coinName){
        return userService.removeFromWatchList(coinName);
    }
}
