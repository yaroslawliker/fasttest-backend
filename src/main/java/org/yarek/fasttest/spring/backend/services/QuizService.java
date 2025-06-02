
package org.yarek.fasttest.spring.backend.services;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.yarek.fasttest.spring.backend.dto.QuizPreviewDto;
import org.yarek.fasttest.spring.backend.entities.Answer;
import org.yarek.fasttest.spring.backend.entities.Question;
import org.yarek.fasttest.spring.backend.entities.Quiz;
import org.yarek.fasttest.spring.backend.entities.QuizResult;
import org.yarek.fasttest.spring.backend.exceptions.QuizNotFoundException;
import org.yarek.fasttest.spring.backend.repositories.QuizRepository;
import org.yarek.fasttest.spring.backend.repositories.QuizResultRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class QuizService {

    private final QuizRepository quizRepository;
    private final QuizResultRepository quizResultRepository;

    @Transactional
    public Quiz saveQuiz(Quiz quiz) {
        return quizRepository.save(quiz);
    }

    public Quiz getQuizById(Long id) {
        Quiz quiz = quizRepository.findById(id)
                .orElseThrow(() -> new QuizNotFoundException("Квіз з ID " + id + " не знайдено"));
        sortQuestions(quiz);
        return quiz;
    }

    private void sortQuestions(Quiz quiz) {
            List<Question> questions = quiz.getQuestions();
            questions.sort((q1, q2) -> {
                if (isStrBigger(q1.getContent(), q2.getContent())) {
                    return 1;
                }
                return -1;
            });
            for (Question question : questions) {
                sortAnswers(question);
            }
    }

    private void sortAnswers(Question question) {
        List<Answer> answers = question.getAnswers();
        answers.sort( (a1, a2) ->  {
            if (isStrBigger(a1.getContent(), a2.getContent())) {
                return 1;
            }
            return -1;
        });

    }

    private boolean isStrBigger(String str1, String str2) {
        if (str1.length() > str2.length()) {
            return true;
        } else if (str1.length() < str2.length()) {
            return false;
        } else {
            for (int i = 0; i < str1.length(); i++) {
                if (str1.charAt(i) > str2.charAt(i)) {
                    return true;
                }
            }
        }
        return false;

    }

    public List<QuizPreviewDto> getLatestQuizPreviews(int count) {
        return quizRepository.findAll(PageRequest.of(0, count))
                .stream()
                .map(QuizPreviewDto::from)
                .collect(Collectors.toList());
    }

    public List<QuizPreviewDto> getAuthorQuizPreviews(Long authorId, int count) {
        return quizRepository.findByOwnerId(authorId)
                .stream()
                .limit(count)
                .map(QuizPreviewDto::from)
                .collect(Collectors.toList());
    }

    public List<QuizResult> getQuizResults(Long quizId, int count) {
        return quizResultRepository.findByQuizId(quizId)
                .stream()
                .limit(count)
                .collect(Collectors.toList());
    }

    @Transactional
    public QuizResult startQuiz(Long quizId, Long userId) {
        Quiz quiz = getQuizById(quizId);
        
        quizResultRepository.findUnfinishedQuizByUserIdAndQuizId(userId, quizId)
                .ifPresent(result -> {
                    throw new IllegalStateException("User already has unfinished quiz");
                });

        QuizResult result = new QuizResult();
        result.setQuizId(quizId);
        result.setUserId(userId);
        result.setStartTime(LocalDateTime.now());
        result.setScore(0.0f);
        
        return quizResultRepository.save(result);
    }

    @Transactional
    public boolean isUserPassingQuiz(Long quizId, Long userId) {
        return quizResultRepository.findUnfinishedQuizByUserIdAndQuizId(userId, quizId).isPresent();
    }

    @Transactional
    public QuizResult finishQuiz(Long quizId, Long userId, float score) {
        QuizResult result = quizResultRepository.findUnfinishedQuizByUserIdAndQuizId(userId, quizId)
                .orElseThrow(() -> new IllegalStateException("Unfinished quiz not found"));

        result.setFinishTime(LocalDateTime.now());
        result.setScore(score);
        
        return quizResultRepository.save(result);
    }
}