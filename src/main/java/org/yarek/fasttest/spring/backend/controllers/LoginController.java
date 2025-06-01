package org.yarek.fasttest.spring.backend.controllers;

import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.yarek.fasttest.spring.backend.dto.LoginRequest;
import org.yarek.fasttest.spring.backend.entities.User;
import org.yarek.fasttest.spring.backend.exceptions.UserNotFoundException;
import org.yarek.fasttest.spring.backend.repositories.UserRepository;
import org.yarek.fasttest.spring.backend.services.TokenService;
import org.yarek.fasttest.spring.backend.services.UserService;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/token")
public class LoginController {

    public static final Logger logger = LoggerFactory.getLogger(LoginController.class);

    private final TokenService tokenService;
    private final UserService userService;

    public LoginController(final TokenService tokenService, final UserService userService) {
        this.tokenService = tokenService;
        this.userService = userService;
    }



    @PostMapping
    public String login(@Valid @RequestBody LoginRequest request, BindingResult result) {

        if (result.hasErrors()) {
            Map<String, String> errors = result.getFieldErrors().stream()
                    .collect(Collectors.toMap(
                            FieldError::getField,
                            fieldError -> fieldError.getDefaultMessage()
                    ));
            return "null";
        }

        User user = userService.getByUsername(request.getUsername());

        logger.debug("Token requested for user '{}'", user.getUsername());


        if (!user.getPassword().equals(request.getPassword())) {
            throw new UserNotFoundException("Invalid username or password");
        }

        Authentication auth = new UsernamePasswordAuthenticationToken(
                user.getUsername(),
                null,
                List.of(new SimpleGrantedAuthority("ROLE_" + user.getRole().name()))
        );

        String token = tokenService.generateToken(auth);

        logger.debug("Token generated '{}'", token);

        return token;
    }
}
