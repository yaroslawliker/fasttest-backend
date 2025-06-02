
package org.yarek.fasttest.spring.backend.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "questions")
@Getter
@Setter
@NoArgsConstructor
public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private float score = 1.0f;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "question_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private List<Answer> answers = new ArrayList<>();

    public Question(String content) {
        this.content = content;
    }

    public Question(String content, float score) {
        this.content = content;
        this.score = score;
    }
}