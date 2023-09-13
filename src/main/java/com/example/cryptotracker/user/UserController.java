package com.example.cryptotracker.user;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/{currencySymbol}")
    private ResponseEntity<String> changeCurrency(@PathVariable String currencySymbol){
      return ResponseEntity.ok(userService.saveCurrency(currencySymbol));
    }
}
