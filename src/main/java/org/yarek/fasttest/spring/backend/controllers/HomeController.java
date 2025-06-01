package org.yarek.fasttest.spring.backend.controllers;


import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.Collection;
import java.util.List;
import java.util.Map;

@RestController
public class HomeController {


    @GetMapping("/")
    public String home(Principal principal) {
        return "Hello " + principal.getName();
    }

    @GetMapping("/me")
    public ResponseEntity<?> me(Authentication authentication) {
        String username = authentication.getName(); // логін
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();

        List<String> roles = authorities.stream()
                .map(GrantedAuthority::getAuthority)
                .toList();

        return ResponseEntity.ok(Map.of(
                "username", username,
                "roles", roles
        ));
    }
}
