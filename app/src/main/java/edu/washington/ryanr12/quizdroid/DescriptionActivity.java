package edu.washington.ryanr12.quizdroid;

import android.content.Intent;
import android.content.res.TypedArray;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


public class DescriptionActivity extends ActionBarActivity {

    private String category;
    private int numQuestions;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_description);

        Intent launchingIntent = getIntent();
        //int categoryNumber = launchingIntent.getIntExtra("categoryNumber", 0);
        //String[] categories = launchingIntent.getStringArrayExtra("categoryArray");


        category = launchingIntent.getStringExtra("categoryName");

        TextView categoryTitle = (TextView) findViewById(R.id.categoryTitle);
        categoryTitle.setText(category);
        String description = "";
        String questionCount = "";
        String[][] questions;
        //TypedArray ta;

        //questions = new ArrayList<Question>();
        numQuestions = 0;
        //String[] currentAnswers;
        //Question current;
        switch (category.toLowerCase()) {
            case "physics" :
                description = getResources().getString(R.string.physicsDescription);
                numQuestions = 3;
                questionCount = numQuestions + " questions";
                break;
            case "math" :
                description = getResources().getString(R.string.mathDescription);
                numQuestions = 3;
                questionCount = numQuestions + " questions";
                break;
            case "marvel super heroes" :
                description = getResources().getString(R.string.marvelDescription);
                numQuestions = 3;
                questionCount = numQuestions + " questions";
                break;
            default :
                description = "unrecognized category";
                numQuestions = 3;
                questionCount = numQuestions + " questions";
                break;
        }

        TextView categoryDescription = (TextView) findViewById(R.id.categoryDescription);
        TextView questionCountView = (TextView) findViewById(R.id.questionCount);
        categoryDescription.setText(description);
        questionCountView.setText(questionCount);

        Button btnNext = (Button) findViewById(R.id.btnBegin);
        btnNext.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent next = new Intent(DescriptionActivity.this, QuestionActivity.class);
                next.putExtra("category", category);
                next.putExtra("questionNumber", 1);
                next.putExtra("numCorrect", 0);
                next.putExtra("numQuestions", numQuestions);
                startActivity(next);
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_description, menu);
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
