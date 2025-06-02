package org.yarek.fasttest.spring.backend.dto.quizRequest;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;

@Data
public class QuestionDto {
    @NotBlank(message = "Question text cannot be empty")
    @Size(max = 1000, message = "Question text is too long")
    private String text;

    @NotEmpty(message = "Question must have at least one answer")
    @Valid
    private List<AnswerDto> answers;

    @Min(value = 0, message = "Question score can't be negative")
    private float score;
}
