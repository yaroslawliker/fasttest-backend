package org.yarek.fasttest.spring.backend.controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.yarek.fasttest.spring.backend.dto.SignupRequest;
import org.yarek.fasttest.spring.backend.services.UserService;

@RestController
@RequestMapping("/signup")
public class RegisterController {

    private final UserService userService;

    @Autowired
    public RegisterController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public void signupUser(@Valid @RequestBody SignupRequest signupRequest) {
        userService.saveUser(signupRequest);
    }
}
