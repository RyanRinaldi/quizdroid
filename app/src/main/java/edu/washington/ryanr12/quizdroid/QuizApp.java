package edu.washington.ryanr12.quizdroid;

import android.app.Application;
import android.util.Log;

import java.util.HashMap;
import java.util.Map;

public class QuizApp extends Application implements TopicRepository {

    private static QuizApp instance;
    private String currentTopic;
    private Map<String, Topic> topics;

    public QuizApp() {
        if(instance == null) {
            instance = this;
        }
    }

    public void setTopic(String value) { this.currentTopic = value; }

    public Topic getTopic() {
        return topics.get(currentTopic);
    }

    public void onCreate() {
        super.onCreate();
        Log.i("QuizApp", "CREATED");

        topics = new HashMap<>();


        // add hard coded question info
        Topic physicsTopic = new Topic("Physics");
        Topic mathTopic = new Topic("Math");
        Topic marvelTopic = new Topic("Marvel Super Heroes");
        addHardcodedTopicQuestions(physicsTopic);
        addHardcodedTopicQuestions(mathTopic);
        addHardcodedTopicQuestions(marvelTopic);

        topics.put(physicsTopic.getTitle(), physicsTopic);
        topics.put(mathTopic.getTitle(), mathTopic);
        topics.put(marvelTopic.getTitle(), marvelTopic);
    }

    // Helper method for hardcoded question info
    private void addHardcodedTopicQuestions(Topic topic) {

        String category = topic.getSimpleName();
        // Get topic information
        int descriptionIndex = getResources().getIdentifier(category + "Description", "string",
                this.getPackageName());
        String description = getResources().getString(descriptionIndex);
        topic.setDescription(description);

        int quizQuestionArray = getResources().getIdentifier(category + "Questions", "array",
                this.getPackageName());
        int numQuestions = getResources().getStringArray(quizQuestionArray).length;

        // For each question: get question info, create a question object, add it to topic
        for(int i = 0; i < numQuestions; i++) {
            int quizInfoIdentifier = getResources().getIdentifier(category + (i + 1),
                    "array", this.getPackageName());
            String[] q = getResources().getStringArray(quizInfoIdentifier);
            topic.addQuestion(new Quiz(q[0], q[1], q[2], q[3], q[4], Integer.parseInt(q[5])));
        }
    }
}
