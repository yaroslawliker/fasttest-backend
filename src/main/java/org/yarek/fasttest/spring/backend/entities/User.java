package org.yarek.fasttest.spring.backend.entities;

import jakarta.persistence.*;

@Entity
public class User {

    public enum Role {
        STUDENT,
        TEACHER
    }

    public User() {

    }

    public User(final String username, final String password, final Role role) {}

    @Id
    @SequenceGenerator(
            name = "user_id_seq",
            sequenceName = "user_id_seq",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "user_id_seq"
    )
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private Role role;

}
