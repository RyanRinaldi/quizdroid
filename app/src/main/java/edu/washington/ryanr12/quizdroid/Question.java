package edu.washington.ryanr12.quizdroid;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by Ryan on 4/28/2015.
 */
public class Question implements Parcelable {
    private String questionSentence;
    private String[] answers;
    private int correctAnswer;

    public Question (String questionSentence, String[] answers, int correctAnswer) {
        this.questionSentence = questionSentence;
        this.answers = answers;
        this.correctAnswer = correctAnswer;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(questionSentence);
        dest.writeArray(answers);
        dest.writeInt(correctAnswer);
    }

    public String getQuestionSentence() {
        return questionSentence;
    }

    public void setQuestionSentence(String value) {
        questionSentence = value;
    }

    public String[] getAnswers() {
        return answers;
    }

    public void setAnswers(String[] values) {
        answers = values;
    }

    public boolean correctAnswer (int guess) {
        return guess == correctAnswer;
    }
}
