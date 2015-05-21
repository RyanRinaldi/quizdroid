package edu.washington.ryanr12.quizdroid;

import android.app.Application;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.util.JsonReader;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
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
    private DownloadManager downloadManager;

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
        // Log.i("QuizApp", "CREATED");

        topics = new HashMap<>();




        try {
            InputStream input = getAssets().open(FILENAME);
            fetchJSONQuestionData(input);
        } catch (IOException e) {
            e.printStackTrace();
        }

/*      Uncomment this to support question information from strings.xml, instead of from JSON
        fetchStringsXmlQuestionData();
            */
        DownloadService.manageAlarm(this, true);
    }


   /* public BroadcastReceiver getReceiver() {
        return this.receiver;
    }*/

    public DownloadManager getDownloadManager() {
        return this.downloadManager;
    }

    public void writeJSONFile(String data) {
        try {
            Log.i("QuizApp", "Writing JSON file");

            File file = new File(getFilesDir().getAbsolutePath(), "questions.json");
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(data.getBytes());
            fos.close();
            InputStream input = new FileInputStream(file);
            fetchJSONQuestionData(input);
        }
        catch (IOException e) {
            Log.e("QuizApp", "IOEXception--File write failed: " + e.toString());
        }
    }




    // add hard coded question info
    private void fetchStringsXmlQuestionData() {
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

    // gets question data from JSON file
    private void fetchJSONQuestionData(InputStream input) throws IOException {
        JsonReader reader = new JsonReader(new InputStreamReader(input, "UTF-8"));
        try {
            readMessagesArray(reader);
        } finally {
            reader.close();
        }
    }

    // reads the JSON file
    private void readMessagesArray(JsonReader reader) throws IOException {
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

    // Parses individual JSON questions, adds them to topic
    private void parseQuestions(JsonReader reader, Topic topic) throws IOException {
        reader.beginArray();
        Quiz current;
        while (reader.hasNext()) {
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


    // Helper method for hardcoded question info. Only used when fetchStringsXmlQuestionInfo is
    // uncommented
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
