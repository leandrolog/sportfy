package com.sportfy.controller;

import com.sportfy.dto.LoginDto;
import com.sportfy.model.User;
import com.sportfy.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private TokenService tokenService;
    private void validateLoginRequest(LoginDto login) {
        if (login.getEmail() == null || login.getPassword() == null) {
            throw new IllegalArgumentException("Email and password are required.");
        }
    }
    @PostMapping("/login")
    public String login(@RequestBody LoginDto login) {
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                new UsernamePasswordAuthenticationToken(login.getEmail(), login.getPassword());

        Authentication authenticate = this.authenticationManager
                .authenticate(usernamePasswordAuthenticationToken);

        var user = (User) authenticate.getPrincipal();

        return tokenService.createToken(user);

    }
}
