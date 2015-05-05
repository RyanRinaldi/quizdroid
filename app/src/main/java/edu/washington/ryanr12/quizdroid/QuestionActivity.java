package edu.washington.ryanr12.quizdroid;

import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


public class QuestionActivity extends ActionBarActivity {

    private int questionNumber;
    private String category;
    private int correctSoFar;
    private String guessed;
    private String correct;
    private String[] questionInfo;
    private int numQuestions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);

        Intent launchingIntent = getIntent();
        numQuestions = launchingIntent.getIntExtra("numQuestions", 0);
        questionNumber = launchingIntent.getIntExtra("questionNumber", 0);
        category = launchingIntent.getStringExtra("category");
        correctSoFar = launchingIntent.getIntExtra("correctSoFar", 0);

        populateQuestionFields(category, questionNumber - 1);


        RadioGroup group = (RadioGroup) findViewById(R.id.answers);
        group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup rg, int selectedId) {
                int guessedIndex = 0;
                Button btnSubmit = (Button) findViewById(R.id.btnSubmit);
                btnSubmit.setVisibility(View.VISIBLE);

                switch (selectedId) {
                    case R.id.answer1:
                        guessedIndex = 1;
                        break;
                    case R.id.answer2:
                        guessedIndex = 2;
                        break;
                    case R.id.answer3:
                        guessedIndex = 3;
                        break;
                    default:
                        guessedIndex = 4;
                        break;
                }

                guessed = questionInfo[guessedIndex];

                //int correctIndex = Integer.parseInt(questionInfo[5]);

            }
        });


        Button btnSubmit = (Button) findViewById(R.id.btnSubmit);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent next = new Intent(QuestionActivity.this, AnswerActivity.class);
                next.putExtra("correctSoFar", correctSoFar);
                next.putExtra("guess", guessed);
                next.putExtra("correct", correct);
                next.putExtra("questionNumber", questionNumber);
                next.putExtra("category", category);
                next.putExtra("numQuestions", numQuestions);
                //Log.i("QuestionActivity", "Guessed: " + guessed + ", Correct: " + correct);
                startActivity(next);

            }
        });

    }
    protected void populateQuestionFields(String category, int question) {
        TextView description = (TextView) findViewById(R.id.questionText);
        RadioButton answer1 = (RadioButton) findViewById(R.id.answer1);
        RadioButton answer2 = (RadioButton) findViewById(R.id.answer2);
        RadioButton answer3 = (RadioButton) findViewById(R.id.answer3);
        RadioButton answer4 = (RadioButton) findViewById(R.id.answer4);


        // int identifier = getStringIdentifier(this, category.toLowerCase() + questionNumber);
        // int identifier = getStringIdentifier(QuestionActivity.this, "physics1");
        int identifier = this.getResources().getIdentifier(category.toLowerCase() +
                questionNumber, "array", this.getPackageName());
        String[] questionInfoArray =  getResources().getStringArray(identifier);

        /*
        TypedArray ta;
        switch (category.toLowerCase()) {
            case "physics" :
                ta = getResources().obtainTypedArray(R.array.physicsQuestions);
                break;
            case "math" :
                ta = getResources().obtainTypedArray(R.array.mathQuestions);
                break;
            default:
                ta = getResources().obtainTypedArray(R.array.marvelQuestions);

        }
        String[][] questions = new String[ta.length()][];
        for(int i = 0; i < ta.length(); i++) {
            int id = ta.getResourceId(i, 0);
            questions[i] = getResources().getStringArray(id);
        }


        description.setText(questions[question][0]);
        answer1.setText(questions[question][1]);
        answer2.setText(questions[question][2]);
        answer3.setText(questions[question][3]);
        answer4.setText(questions[question][4]);

        this.correct = questions[question][5];
        */

        description.setText(questionInfoArray[0]);
        answer1.setText(questionInfoArray[1]);
        answer2.setText(questionInfoArray[2]);
        answer3.setText(questionInfoArray[3]);
        answer4.setText(questionInfoArray[4]);
        // this.correct = questionInfoArray[5];
        int answerNumber = Integer.parseInt(questionInfoArray[5]);
        this.correct = questionInfoArray[answerNumber];


        this.questionInfo = questionInfoArray;




    }

    public static int getStringIdentifier(Context context, String name) {
        return context.getResources().getIdentifier(name, "string", context.getPackageName());
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_question, menu);
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
