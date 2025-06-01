package org.yarek.fasttest.spring.backend.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.yarek.fasttest.spring.backend.services.TokenService;

@RestController
@RequestMapping("/token")
public class LoginController {

    public static final Logger logger = LoggerFactory.getLogger(LoginController.class);

    private final TokenService tokenService;

    public LoginController(final TokenService tokenService) {
        this.tokenService = tokenService;
    }



    @PostMapping
    public String login(Authentication authentication) {
        logger.debug("Token requested for user '{}'", authentication.getName());
        String token = tokenService.generateToken(authentication);
        logger.debug("Token generated '{}'", token);
        return token;
    }
}
