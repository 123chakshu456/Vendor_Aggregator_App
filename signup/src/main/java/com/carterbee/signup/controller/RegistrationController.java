package com.carterbee.signup.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.carterbee.signup.events.RegistrationCompleteEvent;
import com.carterbee.signup.registration.RegistrationRequest;
import com.carterbee.signup.registration.token.VerificationToken;
import com.carterbee.signup.registration.token.VerificationTokenRepository;
import com.carterbee.signup.user.User;
import com.carterbee.signup.user.UserService;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequiredArgsConstructor
@RequestMapping("/register")
public class RegistrationController {

    private final UserService userService;
    private final ApplicationEventPublisher publisher;
    private final VerificationTokenRepository tokenRepository;

    @PostMapping
    public String registerUser(@RequestBody RegistrationRequest registrationRequest, final HttpServletRequest request) {
        
        User user = userService.registerUser(registrationRequest);
        //publish registration event
        publisher.publishEvent(new RegistrationCompleteEvent(user, applicationUrl(request)));
        return "Success!! Please check your email to complete verification";
    }

    @GetMapping("/verifyYourEmail")
    public String verifyEmail(@RequestParam("token") String token){
        VerificationToken verifyToken = tokenRepository.findByToken(token);
        if(verifyToken.getUser().isEnabled()){
            return "this account has already been verified, please login";
        }
        String verificationResult = userService.validateToken(token);
        if(verificationResult.equalsIgnoreCase("valid")){
            return "Email verified successfully. Now you can login to your account";
        }
        return "Invalid verification token";
    }

    
    private String applicationUrl(HttpServletRequest request) {
        return "http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
    }

    
    
}
