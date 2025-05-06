package org.yarek.fasttest.spring.backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.yarek.fasttest.spring.backend.entities.User;

public interface UserRepository extends JpaRepository<User, Long> {

    User findByUsername(String username);
}
