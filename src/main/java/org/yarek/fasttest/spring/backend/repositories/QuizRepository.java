
package org.yarek.fasttest.spring.backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.yarek.fasttest.spring.backend.entities.Quiz;
import java.util.List;

public interface QuizRepository extends JpaRepository<Quiz, Long> {
    
    List<Quiz> findByOwnerId(Long ownerId);
    

    void deleteById(Long id);
}