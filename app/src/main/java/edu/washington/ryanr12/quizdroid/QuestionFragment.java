package edu.washington.ryanr12.quizdroid;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;


public class QuestionFragment extends Fragment {

    private Activity hostActivity;
    private View v;
    private Quiz currentQuestion;
    // private String[] questionInfo;
    // private int questionNumber;
    // private String simpleCategoryName;
    private String guessedAnswer;
    // private String correctAnswer;
    private Button btnSubmit;
    private int guessedIndex;
    // private int answerIndex;
    private QuizApp quizApp;

    public QuestionFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        quizApp = (QuizApp) hostActivity.getApplication();
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_question, container, false);

        // Pulls information on the current question
        int questionNumber = quizApp.getTopic().getQuestionNumber();
        currentQuestion = quizApp.getTopic().getQuestion(questionNumber);

        TextView description = (TextView) v.findViewById(R.id.questionText);
        RadioButton answer1 = (RadioButton) v.findViewById(R.id.answer1);
        RadioButton answer2 = (RadioButton) v.findViewById(R.id.answer2);
        RadioButton answer3 = (RadioButton) v.findViewById(R.id.answer3);
        RadioButton answer4 = (RadioButton) v.findViewById(R.id.answer4);

        description.setText(currentQuestion.getQuestionText());
        answer1.setText(currentQuestion.getAnswer1());
        answer2.setText(currentQuestion.getAnswer2());
        answer3.setText(currentQuestion.getAnswer3());
        answer4.setText(currentQuestion.getAnswer4());
        btnSubmit = (Button) v.findViewById(R.id.btnSubmit);

        RadioGroup group = (RadioGroup) v.findViewById(R.id.answers);
        group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup rg, int selectedId) {
                guessedIndex = 0;
                Button btnSubmit = (Button) v.findViewById(R.id.btnSubmit);
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

                guessedAnswer = currentQuestion.getGuessText(guessedIndex);

            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(hostActivity instanceof QuizActivity)
                    ((QuizActivity) hostActivity).viewAnswer(guessedAnswer,
                            guessedIndex == currentQuestion.getAnswer());
            }
        });


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
