package com.example.cryptotracker.security;

import com.example.cryptotracker.exception.UserNotFoundException;
import com.example.cryptotracker.user.User;
import com.example.cryptotracker.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class UserDataService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User userByEmail = userRepository.findUserByEmail(email);

        if(userByEmail != null){
            return userByEmail;
        }

        try {
            throw new UserNotFoundException();
        } catch (UserNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
