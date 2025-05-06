package org.yarek.fasttest.spring.backend.controllers;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import org.yarek.fasttest.spring.backend.dto.SignupRequest;

@RestController
@RequestMapping("/signup")
public class RegisterController {

    @PostMapping
    public void signupUser(@Valid @RequestBody SignupRequest signupRequest) {
        // TODO: use service to save user
    }
}
