package org.yarek.fasttest.spring.backend.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.yarek.fasttest.spring.backend.entities.User;

@Data
public class SignupRequest {

    @NotNull
    @Size(min = 3, max = 20)
    private String username;

    @NotNull
    @Size(min = 6, max = 20)
    private String password;

    @NotNull
    private User.Role role;
}
