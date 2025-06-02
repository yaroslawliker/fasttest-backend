package org.yarek.fasttest.spring.backend.dto.quizRequest;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class AnswerDto {
    @NotBlank(message = "Answer text cannot be empty")
    @Size(max = 500, message = "Answer text is too long")
    private String text;

    private boolean isCorrect;
}
