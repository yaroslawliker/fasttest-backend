package org.yarek.fasttest.spring.backend.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.yarek.fasttest.spring.backend.dto.SignupRequest;
import org.yarek.fasttest.spring.backend.entities.User;
import org.yarek.fasttest.spring.backend.exceptions.UserNotFoundException;
import org.yarek.fasttest.spring.backend.exceptions.UsernameAlreadyExistsException;
import org.yarek.fasttest.spring.backend.repositories.UserRepository;

@Service
public class UserService {

    @Autowired
    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void saveUser(SignupRequest signupRequest) {

        if (userRepository.findByUsername(signupRequest.getUsername()) != null) {
            throw new UsernameAlreadyExistsException(signupRequest.getUsername());
        }

        User user = new User(
                signupRequest.getUsername(),
                signupRequest.getPassword(),
                signupRequest.getRole()
        );

        userRepository.save(user);
    }

    public User getByUsername(String username) throws UserNotFoundException {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UserNotFoundException(username);
        }
        return user;
    }
}
