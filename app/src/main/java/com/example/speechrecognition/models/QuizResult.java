// QuizResult.java
package com.example.speechrecognition.models;

public class QuizResult {
    private String subject;
    private int correct;
    private int incorrect;
    private int score;
    private int totalQuestions;
    private long timestamp;
    private String resultId;

    // Default constructor required for Firebase No-argument constructor (needed for Firebase)
    public QuizResult() {
        // Required for Firebase
    }
    public QuizResult(String subject, int score, int correct, int incorrect, long timestamp, int totalQuestions) {
        this.subject = subject;
        this.score = score;
        this.correct = correct;
        this.incorrect = incorrect;
        this.timestamp = timestamp;
        this.totalQuestions = totalQuestions;
    }

    // Getters and Setters
    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public int getCorrect() {
        return correct;
    }

    public void setCorrect(int correct) {
        this.correct = correct;
    }

    public int getIncorrect() {
        return incorrect;
    }

    public void setIncorrect(int incorrect) {
        this.incorrect = incorrect;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getTotalQuestions() {
        return totalQuestions;
    }

    public void setTotalQuestions(int totalQuestions) {
        this.totalQuestions = totalQuestions;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getResultId() {
        return resultId;
    }

    public void setResultId(String resultId) {
        this.resultId = resultId;
    }
}

