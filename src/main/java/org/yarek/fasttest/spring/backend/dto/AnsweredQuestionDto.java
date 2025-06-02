package org.yarek.fasttest.spring.backend.dto;

import java.util.List;

public class AnsweredQuestionDto {
    private int questionIndex;
    private List<Integer> selectedAnswerIndexes;

    public int getQuestionIndex() {
        return questionIndex;
    }

    public void setQuestionIndex(int questionIndex) {
        this.questionIndex = questionIndex;
    }

    public List<Integer> getSelectedAnswerIndexes() {
        return selectedAnswerIndexes;
    }

    public void setSelectedAnswerIndexes(List<Integer> selectedAnswerIndexes) {
        this.selectedAnswerIndexes = selectedAnswerIndexes;
    }
}
