
package org.yarek.fasttest.spring.backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.yarek.fasttest.spring.backend.entities.QuizResult;

import java.util.List;
import java.util.Optional;

public interface QuizResultRepository extends JpaRepository<QuizResult, Long> {

    List<QuizResult> findByUserId(Long userId);

    List<QuizResult> findByQuizId(Long quizId);

    @Query("SELECT qr FROM QuizResult qr WHERE qr.userId = :userId AND qr.quizId = :quizId AND qr.finishTime IS NULL")
    Optional<QuizResult> findUnfinishedQuizByUserIdAndQuizId(
            @Param("userId") Long userId,
            @Param("quizId") Long quizId
    );

}
