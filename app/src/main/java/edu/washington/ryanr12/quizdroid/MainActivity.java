package edu.washington.ryanr12.quizdroid;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.ParcelFileDescriptor;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.content.Intent;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;


public class MainActivity extends ActionBarActivity {

    private static final int SETTINGS_RESULT = 1;

    public String[] categories;
    QuizApp quizApp;
    private ListView categoryList;
    private Button preferenceBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        quizApp = (QuizApp) getApplication();
        categories = quizApp.topicNames();

        categoryList = (ListView) findViewById(R.id.lstCategories);

        ArrayAdapter<String> items = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,
                categories);
        categoryList.setAdapter(items);

        // Register DownloadManager receiver
        IntentFilter intentFilter = new IntentFilter("edu.washington.ryanr12.quizdroid");
        intentFilter.addAction(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
        registerReceiver(receiver, intentFilter);



/*
        // Used for testing
        SharedPreferences p = PreferenceManager.getDefaultSharedPreferences(this);
        String url = p.getString("questionDataURL", "default");
        Log.i("MainActivity", "The URL Setting is: " + url);
        Log.i("MainActivity", "The time Setting is: " + Integer.parseInt(p.getString("updateTime", "1440")));
*/

        // get repository from quizApp

        categoryList.setOnItemClickListener(new ListView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent next = new Intent(MainActivity.this, QuizActivity.class);
                // next.putExtra("categoryName", categories[position]);
                quizApp.setTopic(categories[position]);
                startActivity(next);
            }
        });

/*        preferenceBtn = (Button) findViewById(R.id.preferencesBtn);
        preferenceBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent next = new Intent(MainActivity.this, UserSettingActivity.class);
                startActivity(next);
            }
        });*/
    }



    private final BroadcastReceiver receiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            Log.i("MainActivity BR", "BroadcastReceiver onRecieve");
            String action = intent.getAction();
            DownloadManager downloadManager = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
            if(DownloadManager.ACTION_DOWNLOAD_COMPLETE.equals(action)) {
                Log.i("MainActivity BR", "Download complete");
                long downloadID = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, 0);
                if(downloadID != 0) {
                    DownloadManager.Query query = new DownloadManager.Query();
                    query.setFilterById(downloadID);
                    Cursor c = downloadManager.query(query);
                    if(c.moveToFirst()) {
                        int status = c.getInt(c.getColumnIndex(DownloadManager.COLUMN_STATUS));
                        Log.i("DownloadManager Sample", "Status: " + status);
                        switch(status) {
                            case DownloadManager.STATUS_SUCCESSFUL:
                                ParcelFileDescriptor file;
                                StringBuilder content = new StringBuilder("");

                                try {
                                    file = downloadManager.openDownloadedFile(downloadID);
                                    FileInputStream fis = new FileInputStream(file.getFileDescriptor());

                                    // Convert file to string
                                    BufferedReader reader = new BufferedReader(new InputStreamReader(fis));

                                    // Write string to data.json (write method in QuizApp)
                                    quizApp.writeJSONFile(reader.toString());
                                } catch (FileNotFoundException e) {
                                    e.printStackTrace();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                break;
                            case DownloadManager.STATUS_FAILED:
                                // Present warning and exit
                                break;
                        }
                    }
                }
            }


        }
    };

    private void displayUserSettings() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String settings = "";
        settings = settings + "SourceURL: " + sharedPreferences.getString("sourceURL", "NOURL");
        settings = settings + "\nTime between update checks: " + sharedPreferences.getInt(
                "updateFrequency", 1440);

    }

    // TODO: Remove this
/*
    protected void onDestroy() {
        super.onDestroy();
        Log.i("MainActivity", "Turning off alarm and unregistering Receiver");
        DownloadService.manageAlarm(this, false);
        unregisterReceiver(receiver);
    }
*/

    /*
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == SETTINGS_RESULT) {
            displayUserSettings();
        }
    }
    */


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    public void showToast() {
        Log.i("MainActivity", "Toast should show");
        SharedPreferences p = PreferenceManager.getDefaultSharedPreferences(this);
        String url = p.getString("questionDataURL", "default");
        Toast.makeText(MainActivity.this, "URL: " + url, Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent next = new Intent(MainActivity.this, UserSettingActivity.class);
            startActivity(next);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
