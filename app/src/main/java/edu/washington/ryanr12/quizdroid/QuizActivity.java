package edu.washington.ryanr12.quizdroid;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;


public class QuizActivity extends ActionBarActivity {

    //private String category;
    //private String simpleCategoryName;
    //private int questionNumber;
    //private int numQuestions;
    //private int correctSoFar;
    private QuizApp quizApp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        quizApp = (QuizApp) getApplication();

        Intent launchingIntent = getIntent();
        if(launchingIntent != null) {
            FragmentManager fragManager = getFragmentManager();
            FragmentTransaction fragTransaction = fragManager.beginTransaction();

            Bundle categoryBundle = new Bundle();

            DescriptionFragment descriptionFragment = new DescriptionFragment();
            descriptionFragment.setArguments(categoryBundle);
            fragTransaction.add(R.id.fragmentContainer, descriptionFragment);
            fragTransaction.commit();
        }
    }



    public void viewQuestion() {
        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();

        Bundle questionInfoBundle = new Bundle();
        // questionInfoBundle.putStringArray("questionInfo", questionInfo);
        // questionInfoBundle.putInt("currentQuestion", currentQuestion);
        // questionInfoBundle.putString("simpleCategoryName", simpleCategoryName);
        // questionInfoBundle.putInt("questionNumber", questionNumber);
        QuestionFragment questionFragment = new QuestionFragment();
        questionFragment.setArguments(questionInfoBundle);
        ft.replace(R.id.fragmentContainer, questionFragment);
        ft.commit();
    }

    public void viewAnswer(String guessedAnswer, boolean correct) {

        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        Bundle answerInfoBundle = new Bundle();
        answerInfoBundle.putString("guessedAnswer", guessedAnswer);
        // answerInfoBundle.putString("correctAnswer", correctAnswer);
        answerInfoBundle.putBoolean("correct", correct);
        // answerInfoBundle.putInt("questionNumber", questionNumber);
        // answerInfoBundle.putInt("numQuestions", numQuestions);
        // answerInfoBundle.putInt("correctSoFar", correctSoFar);
        AnswerFragment answerFragment = new AnswerFragment();
        answerFragment.setArguments(answerInfoBundle);
        ft.replace(R.id.fragmentContainer, answerFragment);
        ft.commit();
    }

    /*
    public void setSimpleCategoryName(String value) {
        this.simpleCategoryName = value;
    }



    public void incrementCorrectSoFar() { correctSoFar++; }

    public void setNumQuestions(int value) {
        this.numQuestions = value;
    }

    public int getNumQuestions() {
        return numQuestions;
    }

    */

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_quiz, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
