package org.yarek.fasttest.spring.backend.controllers;

import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.yarek.fasttest.spring.backend.dto.SignupRequest;
import org.yarek.fasttest.spring.backend.services.UserService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/signup")
public class RegisterController {

    private final UserService userService;
    private final Logger logger = LoggerFactory.getLogger(RegisterController.class);

    @Autowired
    public RegisterController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<?> signupUser(@Valid @RequestBody SignupRequest signupRequest, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            logger.warn("Validation errors: {}", bindingResult.getAllErrors());
            Map<String, List<String>> errors = new HashMap<>();

            for (FieldError error : bindingResult.getFieldErrors()) {
                errors.computeIfAbsent(error.getField(), key -> new ArrayList<>())
                        .add(error.getDefaultMessage());
            }

            return ResponseEntity.badRequest().body(errors);
        } else {
            logger.info("User registration requested for username '{}'", signupRequest.getUsername());
            userService.saveUser(signupRequest);
            logger.info("Registered '{}', '{}'", signupRequest.getUsername(), signupRequest.getRole());

            return ResponseEntity.ok("User registered");
        }
    }


}
