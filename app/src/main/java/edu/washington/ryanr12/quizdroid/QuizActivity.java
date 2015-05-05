package edu.washington.ryanr12.quizdroid;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;


public class QuizActivity extends ActionBarActivity {

    private String category;
    private int questionNumber;
    private int numQuestions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);
        questionNumber = 1;

        Intent launchingIntent = getIntent();
        if(launchingIntent != null) {
            category = launchingIntent.getStringExtra("categoryName");
            FragmentManager fragManager = getFragmentManager();
            FragmentTransaction fragTransaction = fragManager.beginTransaction();

            Bundle categoryBundle = new Bundle();
            categoryBundle.putString("categoryName", category);

            DescriptionFragment descriptionFragment = new DescriptionFragment();
            descriptionFragment.setArguments(categoryBundle);
            fragTransaction.add(R.id.fragmentContainer, descriptionFragment);
            fragTransaction.commit();
        }
    }


    public void viewQuestion(String[] questionInfo, int currentQuestion) {
        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();

        Bundle questionInfoBundle = new Bundle();
        questionInfoBundle.putStringArray("questionInfo", questionInfo);
        questionInfoBundle.putInt("currentQuestion", currentQuestion);
        QuestionFragment questionFragment = new QuestionFragment();
        questionFragment.setArguments(questionInfoBundle);
        ft.add(R.id.fragmentContainer, questionFragment);
        ft.commit();
    }

    public void incrementQuestionNumber() {
        questionNumber++;
    }

    public void setNumQuestions(int value) {
        this.numQuestions = value;
    }

    public int getNumQuestions() {
        return numQuestions;
    }

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
