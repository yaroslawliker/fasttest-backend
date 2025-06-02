package org.yarek.fasttest.spring.backend.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import org.yarek.fasttest.spring.backend.dto.AnsweredQuestionDto;
import org.yarek.fasttest.spring.backend.dto.quizRequest.CreateQuizDto;
import org.yarek.fasttest.spring.backend.dto.QuizPreviewDto;
import org.yarek.fasttest.spring.backend.entities.*;
import org.yarek.fasttest.spring.backend.services.QuizService;
import org.yarek.fasttest.spring.backend.services.UserService;

import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/quizzes")
public class QuizController {

    private final QuizService quizService;
    private final UserService userService;

    Logger logger = Logger.getLogger(QuizController.class.getName());

    public QuizController(QuizService quizService, UserService userService) {
        this.quizService = quizService;
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<Quiz> createQuiz(
            @Valid @RequestBody CreateQuizDto request,
            @AuthenticationPrincipal Jwt jwt
    ) {
        String username = jwt.getSubject();
        User user = userService.getByUsername(username);
        Long userId = user.getId();

        Quiz quiz = new Quiz();
        quiz.setName(request.getName());
        quiz.setDescription(request.getDescription());
        quiz.setOwnerId(userId);

        List<Question> questions = request.getQuestions().stream()
                .map(questionDto -> {
                    Question question = new Question();
                    question.setContent(questionDto.getText());

                    List<Answer> answers = questionDto.getAnswers().stream()
                            .map(answerDto -> {
                                Answer answer = new Answer();
                                answer.setContent(answerDto.getText());
                                return answer;
                            })
                            .collect(Collectors.toList());

                    question.setAnswers(answers);
                    return question;
                })
                .collect(Collectors.toList());

        quiz.setQuestions(questions);

        Quiz savedQuiz = quizService.saveQuiz(quiz);
        return ResponseEntity.ok(savedQuiz);
    }

    @GetMapping
    public ResponseEntity<List<QuizPreviewDto>> getLatestQuizzes(
            @RequestParam(defaultValue = "10") int count
    ) {
        return ResponseEntity.ok(quizService.getLatestQuizPreviews(count));
    }

    @GetMapping("/by-author/{authorId}")
    public ResponseEntity<List<QuizPreviewDto>> getAuthorQuizzes(
            @PathVariable Long authorId,
            @RequestParam(defaultValue = "10") int count
    ) {
        return ResponseEntity.ok(quizService.getAuthorQuizPreviews(authorId, count));
    }

    @GetMapping("/{quizId}")
    public ResponseEntity<Quiz> getQuiz(@PathVariable Long quizId) {
        return ResponseEntity.ok(quizService.getQuizById(quizId));
    }

    @PostMapping("/{quizId}/start")
    public ResponseEntity<QuizResult> startQuiz(
            @PathVariable Long quizId,
            @AuthenticationPrincipal Jwt jwt
    ) {
        String username = jwt.getSubject();
        User user = userService.getByUsername(username);
        Long userId = user.getId();
        try {
            QuizResult quiz = quizService.startQuiz(quizId, userId);
            return ResponseEntity.ok(quiz);
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(null);
        }

    }

    @GetMapping("/{quizId}/in-progress")
    public ResponseEntity<Quiz> getInProgressQuiz(
            @PathVariable Long quizId,
            @AuthenticationPrincipal Jwt jwt
    ) {
        logger.info("Accessing in progress");
        User user = userService.getByUsername(jwt.getSubject());
        if (quizService.isUserPassingQuiz(quizId, user.getId())) {
            return ResponseEntity.ok(quizService.getQuizById(quizId));
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/{quizId}/finish")
    public ResponseEntity<QuizResult> finishQuiz(
            @RequestBody List<AnsweredQuestionDto> questions,
            @PathVariable Long quizId,
            @AuthenticationPrincipal Jwt jwt
    ) {
        logger.info("Quiz finish" + questions.toString());
        String username = jwt.getSubject();
        User user = userService.getByUsername(username);
        Long userId = user.getId();

        Quiz quiz = quizService.getQuizById(quizId);
        QuizBlank quizBlank = new QuizBlank(quiz);

        for (int i = 0; i < questions.size(); i++) {
            AnsweredQuestionDto answeredQuestionDto = questions.get(i);
            List<Integer> answers = answeredQuestionDto.getSelectedAnswerIndexes();
            for (Integer answer : answers) {
                quizBlank.registerAnswer(i, answer);
            }
        }

        float score = quizBlank.getScore();

        QuizResult quizResult = quizService.finishQuiz(quizId, userId, score);

        return ResponseEntity.ok(quizResult);
    }

    @GetMapping("/{quizId}/results")
    public ResponseEntity<List<QuizResult>> getQuizResults(
            @PathVariable Long quizId,
            @RequestParam(defaultValue = "10") int count
    ) {
        return ResponseEntity.ok(quizService.getQuizResults(quizId, count));
    }
}