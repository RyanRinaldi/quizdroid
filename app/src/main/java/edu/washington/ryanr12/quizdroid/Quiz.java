package edu.washington.ryanr12.quizdroid;

/**
 * Created by Ryan on 5/11/2015.
 */
public class Quiz {
    private String questionText;
    private String[] answers;
    private int answer;


    public Quiz(String questionText, String answer1, String answer2, String answer3,
                String answer4, int answer) {
        this.questionText = questionText;
        answers = new String[5];
        // skipped 0 index for readability
        answers[1] = answer1;
        answers[2] = answer2;
        answers[3] = answer3;
        answers[4] = answer4;
        this.answer = answer;
    }

    public Quiz() {
        this(null, null, null, null, null, 0);
    }

    public String getQuestionText() {
        return questionText;
    }

    public String getGuessText(int chosen) {
        switch (chosen) {
            case 1:
                return answers[1];
            case 2:
                return answers[2];
            case 3:
                return answers[3];
            default:
                return answers[4];
        }
    }

    public String getAnswer1() {
        return answers[1];
    }

    public String getAnswer2() {
        return answers[2];
    }

    public String getAnswer3() {
        return answers[3];
    }

    public String getAnswer4() {
        return answers[4];
    }

    public int getAnswer() {
        return answer;
    }

    public String getAnswerText() {
        return answers[answer];
    }



}
