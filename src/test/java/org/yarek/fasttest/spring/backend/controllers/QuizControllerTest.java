package org.yarek.fasttest.spring.backend.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.jwt.Jwt;
import org.yarek.fasttest.spring.backend.dto.QuizPreviewDto;
import org.yarek.fasttest.spring.backend.dto.quizRequest.AnswerDto;
import org.yarek.fasttest.spring.backend.dto.quizRequest.CreateQuizDto;
import org.yarek.fasttest.spring.backend.dto.quizRequest.QuestionDto;
import org.yarek.fasttest.spring.backend.entities.Quiz;
import org.yarek.fasttest.spring.backend.entities.User;
import org.yarek.fasttest.spring.backend.services.QuizService;
import org.yarek.fasttest.spring.backend.services.UserService;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class QuizControllerTest {

    @Mock
    private QuizService quizService;

    @Mock
    private UserService userService;

    @Mock
    private Jwt jwt;

    @InjectMocks
    private QuizController quizController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createQuizShouldReturnCreatedQuiz() {

        CreateQuizDto createQuizDto = new CreateQuizDto();
        createQuizDto.setName("Test Quiz");
        createQuizDto.setDescription("Test Quiz Description");

        QuestionDto questionDto = new QuestionDto();
        questionDto.setText("Question 1");
        questionDto.setScore(1.0f);

        AnswerDto answerDto = new AnswerDto();
        answerDto.setText("Answer 1");
        answerDto.setCorrect(true);

        questionDto.setAnswers(Collections.singletonList(answerDto));
        createQuizDto.setQuestions(Collections.singletonList(questionDto));

        User user = new User();
        user.setId(1L);
        user.setUsername("test_user");

        Quiz savedQuiz = new Quiz();
        savedQuiz.setId(1L);
        savedQuiz.setName(createQuizDto.getName());

        when(jwt.getSubject()).thenReturn("test_user");
        when(userService.getByUsername("test_user")).thenReturn(user);
        when(quizService.saveQuiz(any(Quiz.class))).thenReturn(savedQuiz);

        ResponseEntity<Quiz> response = quizController.createQuiz(createQuizDto, jwt);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals(createQuizDto.getName(), response.getBody().getName());
    }

    @Test
    void getLatestQuizzesShouldReturnQuizList() {

        int count = 10;
        when(quizService.getLatestQuizPreviews(count)).thenReturn(Collections.emptyList());

        ResponseEntity<List<QuizPreviewDto>> response = quizController.getLatestQuizzes(count);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
    }
}