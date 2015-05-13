package edu.washington.ryanr12.quizdroid;

import android.app.Application;
import android.util.JsonReader;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class QuizApp extends Application implements TopicRepository {

    // Set this to the .json file that holds question information
    private static final String FILENAME = "questions.json";
    private static QuizApp instance;
    private String currentTopic;
    private Map<String, Topic> topics;

    public QuizApp() {
        if(instance == null) {
            instance = this;
        }
    }

    public String[] topicNames() {
        ArrayList<String> result  = new ArrayList<>();
        for(Topic topic : topics.values()) {
            result.add(topic.getTitle());
        }
        String[] endResult = new String[result.size()];
        return result.toArray(endResult);
    }

    public void setTopic(String value) { this.currentTopic = value; }

    public Topic getTopic() {
        return topics.get(currentTopic);
    }

    public void onCreate() {
        super.onCreate();
        Log.i("QuizApp", "CREATED");

        topics = new HashMap<>();




        try {
            InputStream input = getAssets().open(FILENAME);
            fetchJSONQuestionData(input);
        } catch (IOException e) {
            e.printStackTrace();
        }
        /*
        // fetch json question data
        InputStream input = null;
        try {
            input = getAssets().open("questions.json");
            String fileContents = readFile(input);
            JSONObject json = new JSONObject(fileContents);





        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        } finally {
            if(input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    Log.e("QuizApp", "Failed to close JSON InputStream");
                }
            }
        }

        */
/*      Uncomment this to support question information from strings.xml
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
        */
    }

    // gets question data from JSON file
    private void fetchJSONQuestionData(InputStream input) throws IOException {
        JsonReader reader = new JsonReader(new InputStreamReader(input, "UTF-8"));
        try {
            readMessagesArray(reader);
        } finally {
            reader.close();
        }
    }

    private void readMessagesArray(JsonReader reader) throws IOException {
        List messages = new ArrayList();
        reader.beginArray();

        while(reader.hasNext()) {

            reader.beginObject();
            Topic currentTopic = new Topic();
            while (reader.hasNext()) {
                String name = reader.nextName();
                switch (name) {
                    case "title":
                        currentTopic.setTitle(reader.nextString());
                        break;
                    case "desc":
                        currentTopic.setDescription(reader.nextString());
                        break;
                    case "questions":
                        parseQuestions(reader, currentTopic);


                        break;
                    default:
                        reader.skipValue();
                        break;
                }
            }
            topics.put(currentTopic.getTitle(), currentTopic);
            reader.endObject();
        }
        reader.endArray();
    }

    private void parseQuestions(JsonReader reader, Topic topic) throws IOException {
        reader.beginArray();
        Quiz current;
        while (reader.hasNext()) {

            // current = new Quiz();
            String text = "";
            String[] answers = new String[4];
            int answer = 0;

            reader.beginObject();
            while (reader.hasNext()) {
                String name = reader.nextName();
                switch (name) {
                    case "text":
                        text = reader.nextString();
                        break;
                    case "answer":
                        answer = Integer.parseInt(reader.nextString());
                        break;
                    case "answers":
                        reader.beginArray();
                        int i = 0;
                        while (reader.hasNext() && i < 4) {
                            answers[i] = reader.nextString();
                            i++;
                        }
                        reader.endArray();
                        break;
                    default:
                        reader.skipValue();
                        break;
                }
            }
            reader.endObject();
            current = new Quiz(text, answers[0], answers[1], answers[2], answers[3], answer);
            topic.addQuestion(current);
        }
        reader.endArray();
    }

    private String readFile(InputStream input) throws IOException {
        int size = input.available();
        byte[] buffer = new byte[size];
        input.read(buffer);
        input.close();
        return new String(buffer, "UTF-8");
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
