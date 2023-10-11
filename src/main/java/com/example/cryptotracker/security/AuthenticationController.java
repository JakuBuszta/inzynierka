package com.example.cryptotracker.security;

import com.example.cryptotracker.ApiCallScheduler;
import com.example.cryptotracker.common.CommonController;
import com.example.cryptotracker.user.User;
import com.example.cryptotracker.user.UserRepository;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class AuthenticationController extends CommonController {
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    public AuthenticationController(ApiCallScheduler apiCallScheduler, PasswordEncoder passwordEncoder, UserRepository userRepository) {
        super(apiCallScheduler);
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
    }

    @GetMapping("/login")
    public String viewLoginPage(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        System.out.println(authentication);
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

    @PostMapping("/register/save")
    public String registration(@ModelAttribute("user") User user,
                               BindingResult result, Model model){
        User existingUser = userRepository.findUserByEmail(user.getEmail());

        if(existingUser != null && existingUser.getEmail() != null && !existingUser.getEmail().isEmpty()){
            result.rejectValue("email", null,
                    "There is already an account registered with the same email");
        }

        if(result.hasErrors()){
            model.addAttribute("user", user);
            return "/register";
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        model.addAttribute("accountCreated", true);
        userRepository.save(user);
        return "login";
    }
}
