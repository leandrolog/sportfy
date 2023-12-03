package com.sportfy.controller;

import com.sportfy.dto.LoginDto;
import com.sportfy.model.User;
import com.sportfy.service.TokenService;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
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
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Login successful"),
            @ApiResponse(responseCode = "400", description = "Invalid login credentials")
    })
    public String login(@RequestBody LoginDto login) {
        try {
            validateLoginRequest(login);

            UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                    new UsernamePasswordAuthenticationToken(login.getEmail(), login.getPassword());

            Authentication authenticate = this.authenticationManager.authenticate(usernamePasswordAuthenticationToken);

            var user = (User) authenticate.getPrincipal();

            return tokenService.createToken(user);

        } catch (Exception e) {
            throw new ConflictException("Invalid login credentials");
        }
    }
}
