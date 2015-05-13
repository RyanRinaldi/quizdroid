package edu.washington.ryanr12.quizdroid;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;


public class DescriptionFragment extends Fragment {
    // private String category;
    // private String simpleCategoryName;
    private Activity hostActivity;
    private QuizApp quizApp;

    // TODO: Rename and change types and number of parameters


    public DescriptionFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        String category = "";
        // description = "";
        quizApp = (QuizApp) hostActivity.getApplication();

        super.onCreate(savedInstanceState);
        /*
        if (getArguments() != null) {
            category = getArguments().getString("categoryName");
            quizApp.setTopic(category);
        }
        */

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_description, container, false);

        /*
        switch (category.toLowerCase()) {
            case "physics" :
                simpleCategoryName = "physics";

                // numQuestions = getResources().getStringArray(R.array.physicsQuestions).length;
                break;
            case "math" :
                simpleCategoryName = "math";
                // numQuestions = getResources().getStringArray(R.array.mathQuestions).length;
                break;
            case "marvel super heroes" :
                simpleCategoryName = "marvel";
                // numQuestions = getResources().getStringArray(R.array.marvelQuestions).length;
                break;
            default :
                simpleCategoryName = null;
                break;
        }
        */
        String simpleCategoryName = quizApp.getTopic().getSimpleName();
        String category = quizApp.getTopic().getTitle();
        String description = quizApp.getTopic().getDescription();
        int numQuestions = quizApp.getTopic().getNumQuestions();
        /*
        if(hostActivity instanceof QuizActivity) {
            ((QuizActivity) hostActivity).setSimpleCategoryName(simpleCategoryName);
        }
        */

        if(simpleCategoryName != null) {

            /*
            int descriptionIdentifier = getResources().getIdentifier(simpleCategoryName + "Description",
                    "string", hostActivity.getPackageName());
            String description = getResources().getString(descriptionIdentifier);

            int numQuestionsIdentifier = getResources().getIdentifier(simpleCategoryName + "Questions",
                    "array", hostActivity.getPackageName());
            int numQuestions = getResources().getStringArray(numQuestionsIdentifier).length;
            */

            TextView titleTV = (TextView) v.findViewById(R.id.categoryTitle);
            TextView descriptionTV = (TextView) v.findViewById(R.id.categoryDescription);
            TextView questionCountTV = (TextView) v.findViewById(R.id.questionCount);

            titleTV.setText(category);
            descriptionTV.setText(description);
            questionCountTV.setText(numQuestions + " questions");

            Button btnBegin = (Button) v.findViewById(R.id.btnBegin);


            btnBegin.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {

                    /*
                    int quizInfoIdentifier = getResources().getIdentifier(simpleCategoryName + "1",
                            "array", hostActivity.getPackageName());
                    String[] questionInfo = getResources().getStringArray(quizInfoIdentifier);
                    */

                    if(hostActivity instanceof QuizActivity)
                        ((QuizActivity) hostActivity).viewQuestion();
                }
            });

        }


        //TextView tv = (TextView) v.findViewById(R.id.testTV);
        //tv.setText("Category is: " + category);
        return v;
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        this.hostActivity = activity;
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
