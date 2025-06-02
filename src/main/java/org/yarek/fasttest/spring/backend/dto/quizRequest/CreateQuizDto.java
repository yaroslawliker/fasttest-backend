
package org.yarek.fasttest.spring.backend.dto.quizRequest;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;

@Data
public class CreateQuizDto {
    @NotBlank(message = "Quiz name cannot be empty")
    @Size(max = 255, message = "Quiz name is too long")
    private String name;

    @Size(max = 1000, message = "Quiz description is too long")
    private String description;

    @NotEmpty(message = "Quiz must contain at least one question")
    @Valid
    private List<QuestionDto> questions;
}

