package edu.washington.ryanr12.quizdroid;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;


public class AnswerFragment extends Fragment {

    private String guessedAnswer;
    private String correctAnswer;
    private boolean correct;
    private int questionNumber;
    private int numQuestions;
    private int correctSoFar;
    private Activity hostActivity;

    public AnswerFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            guessedAnswer = getArguments().getString("guessedAnswer");
            correctAnswer = getArguments().getString("correctAnswer");
            correct = getArguments().getBoolean("correct");
            questionNumber = getArguments().getInt("questionNumber");
            numQuestions = getArguments().getInt("numQuestions");
            correctSoFar = getArguments().getInt("correctSoFar");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_answer, container, false);
        TextView congratsText = (TextView) v.findViewById(R.id.congratsText);
        TextView guessText = (TextView) v.findViewById(R.id.guessText);
        TextView answerText = (TextView) v.findViewById(R.id.answerText);
        TextView numCorrectText = (TextView) v.findViewById(R.id.numCorrectText);

        if(correct) {
            congratsText.setText("Correct!");
            correctSoFar++;
            if(hostActivity instanceof QuizActivity)
                ((QuizActivity) hostActivity).incrementCorrectSoFar();
        } else {
            congratsText.setText("Wrong!");
        }
        guessText.setText("You guessed: " + guessedAnswer);
        answerText.setText("Correct answer: " + correctAnswer);
        numCorrectText.setText("You have " + correctSoFar + " out of " + questionNumber +
                " correct");

        Button btnNext = (Button) v.findViewById(R.id.btnNext);
        if(questionNumber >= numQuestions) {
            btnNext.setText("Finish");
            btnNext.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    Intent next = new Intent(hostActivity, MainActivity.class);
                    startActivity(next);
                }
            });
        } else {
            btnNext.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    if(hostActivity instanceof QuizActivity)
                        ((QuizActivity) hostActivity).viewQuestion(questionNumber + 1);
                }
            });
        }

        return v;
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        hostActivity = activity;
    }

    @Override
    public void onDetach() {
        super.onDetach();

    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

}
