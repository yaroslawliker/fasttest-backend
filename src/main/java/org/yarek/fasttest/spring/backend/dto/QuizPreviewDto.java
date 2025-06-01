
package org.yarek.fasttest.spring.backend.dto;

import lombok.Value;
import org.yarek.fasttest.spring.backend.entities.Quiz;

import java.util.Date;

@Value
public class QuizPreviewDto {
    Long id;
    Long ownerId;
    String name;
    String description;
    Date creationDate;

    public static QuizPreviewDto from(Quiz quiz) {
        return new QuizPreviewDto(
            quiz.getId(),
            quiz.getOwnerId(),
            quiz.getName(),
            quiz.getDescription(),
            quiz.getCreationDate()
        );
    }
}