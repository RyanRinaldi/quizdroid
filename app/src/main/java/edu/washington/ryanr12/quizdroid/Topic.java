package edu.washington.ryanr12.quizdroid;

import java.util.ArrayList;
import java.util.List;


public class Topic {
    private String title;
    private String description;
    private List<Quiz> questions;
    private int questionNumber;
    private int correctSoFar;


    public Topic (String title, String description) {
        this(title, description, null);
    }

    public Topic (String title, String description, List<Quiz> questions) {
        this.title = title;
        this.description = description;
        this.questions = questions;
        if (questions == null) {
            this.questions = new ArrayList<>();
        }
        questionNumber = 1;
    }

    public int getCorrectSoFar() { return this.correctSoFar; }

    public void incrementCorrectSoFar() { this.correctSoFar++; }

    public void incrementQuestionNumber() { this.questionNumber++; }

    public void reset() {
        this.correctSoFar = 0;
        this.questionNumber = 1;
    }

    public void setDescription(String value) {
        this.description = value;
    }

    public String getDescription() { return this.description; }

    public int getQuestionNumber() { return this.questionNumber; }

    // public void setQuestionNumber(int value) { this.questionNumber = value; }

    public int getNumQuestions() { return this.questions.size(); }

    public String getTitle() { return this.title; }

    // Simple names used to get information out of strings.xml
    public String getSimpleName() {
        if(title.equals("Marvel Super Heroes")) {
            return "marvel";
        }
        return title.toLowerCase();
    }



    public Quiz getQuestion(int i) {
        return questions.get(i - 1);
    }

    public Topic(String title) {
        this(title, null);
    }

    public void addQuestion(Quiz q) {
        this.questions.add(q);
    }

}
