package edu.washington.ryanr12.quizdroid;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class AnswerActivity extends ActionBarActivity {

    private int questionNumber;
    private String category;
    private int correctSoFar;
    private int numQuestions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_answer);

        Intent launchingIntent = getIntent();
        questionNumber = launchingIntent.getIntExtra("questionNumber", 0);
        category = launchingIntent.getStringExtra("category");
        correctSoFar = launchingIntent.getIntExtra("correctSoFar", 0);
        String guess = launchingIntent.getStringExtra("guess");
        String answer = launchingIntent.getStringExtra("correct");
        numQuestions = launchingIntent.getIntExtra("numQuestions", 0);


        TextView congratsText = (TextView) findViewById(R.id.congratsText);
        TextView guessText = (TextView) findViewById(R.id.guessText);
        TextView answerText = (TextView) findViewById(R.id.answerText);
        TextView numCorrectText = (TextView) findViewById(R.id.numCorrectText);

        if(guess.toLowerCase().equals(answer.toLowerCase())) {
            congratsText.setText("Correct!");
            correctSoFar++;
        } else {
            congratsText.setText("Wrong!");
        }

        guessText.setText("You guessed: " + guess);
        answerText.setText("Correct answer: " + answer);
        numCorrectText.setText("You have " + correctSoFar + " out of " + questionNumber +
                " correct");

        Button btnNext = (Button) findViewById(R.id.btnNext);
        if(questionNumber >= numQuestions) {
            btnNext.setText("Finish");
            btnNext.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    Intent next = new Intent(AnswerActivity.this, MainActivity.class);
                    startActivity(next);
                }
            });
        } else {
            btnNext.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    questionNumber++;
                    Intent next = new Intent(AnswerActivity.this, QuestionActivity.class);
                    next.putExtra("correctSoFar", correctSoFar);
                    next.putExtra("questionNumber", questionNumber);
                    next.putExtra("category", category);
                    next.putExtra("numQuestions", numQuestions);
                    startActivity(next);
                }
            });
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_answer, menu);
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
