package edu.washington.ryanr12.quizdroid;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.DownloadManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import android.preference.PreferenceManager;
import android.provider.Settings;
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
    private static final String DOWNLOAD_ID = "QuizDroidDownload";
    public String[] categories;
    QuizApp quizApp;
    private ListView categoryList;
    private SharedPreferences preferences;
    private DownloadManager dm;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        quizApp = (QuizApp) getApplication();

        draw();
        checkForUpdates();

        // Register DownloadManager receiver
        IntentFilter intentFilter = new IntentFilter("edu.washington.ryanr12.quizdroid");
        intentFilter.addAction(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
        registerReceiver(receiver, intentFilter);

        categoryList.setOnItemClickListener(new ListView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent next = new Intent(MainActivity.this, QuizActivity.class);
                quizApp.setTopic(categories[position]);
                startActivity(next);
            }
        });

    }

    // Draws the Main Activity list of topics
    private void draw() {
        categories = quizApp.topicNames();
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        dm = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);

        categoryList = (ListView) findViewById(R.id.lstCategories);

        ArrayAdapter<String> items = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,
                categories);
        categoryList.setAdapter(items);
    }

    public void onPause() {
        super.onPause();
        unregisterReceiver(receiver);
    }

    private void downloadStatus() {
        DownloadManager.Query query = new DownloadManager.Query();
        query.setFilterById(preferences.getLong(DOWNLOAD_ID, 0));
        Cursor c = dm.query(query);
        if(c.moveToFirst()) {
            int status = c.getInt(c.getColumnIndex(DownloadManager.COLUMN_STATUS));
            Log.i("DownloadStatus", "Status: " + status);
            switch(status) {
                case DownloadManager.STATUS_PAUSED:
                case DownloadManager.STATUS_PENDING:
                case DownloadManager.STATUS_RUNNING:
                    break;
                case DownloadManager.STATUS_SUCCESSFUL:
                    try {
                        ParcelFileDescriptor file = dm.openDownloadedFile(preferences.getLong(DOWNLOAD_ID, 0));
                        FileInputStream fis = new ParcelFileDescriptor.AutoCloseInputStream(file);

                        // Convert file to string
                        // BufferedReader reader = new BufferedReader(new InputStreamReader(fis));
                        StringBuffer fileContent = new StringBuffer("");

                        int ch;
                        while( (ch = fis.read()) != -1) {
                            fileContent.append((char)ch);
                        }

                        // Write string to data.json (write method in QuizApp)
                        quizApp.writeJSONFile(fileContent.toString());

                        // Draws the main menu. Updates category list.
                        draw();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case DownloadManager.STATUS_FAILED:
                    dm.remove(preferences.getLong(DOWNLOAD_ID, 0));
                    preferences.edit().clear().commit();
                    fireFailedAlert();
                    break;
            }
        }
    }

    public class DownloadFailedDialogFragment extends DialogFragment {
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage(R.string.download_failed_dialog)
                    // Button: Retry Download
                    .setPositiveButton(R.string.download_failed_retry, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // Retry the download
                            checkForUpdates();
                        }
                    })
                    // Button: Quit QuizDroid
                    .setNegativeButton(R.string.download_failed_quit, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // quit app, try again
                            finish();
                        }
                    });
            return builder.create();
        }
    }


    public class AirplaneModeDialogFragment extends DialogFragment {
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage(R.string.airplaneMode_dialog)
                    // Button: Go to settings
                    .setPositiveButton(R.string.airplaneMode_Okay, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // Take user to settings
                            startActivity(new Intent(Settings.ACTION_AIRPLANE_MODE_SETTINGS));
                        }
                    })
                            // Button: Cancel
                    .setNegativeButton(R.string.airplaneMode_cancel, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // Close dialog
                            dialog.dismiss();
                        }
                    });
            return builder.create();
        }
    }


    private void fireFailedAlert() {
        DialogFragment downloadFailedDialog = new DownloadFailedDialogFragment();
        downloadFailedDialog.show(getFragmentManager(), "download_failed");
    }

    private void checkForUpdates() {
        boolean airplaneMode = Settings.System.getInt(this.getContentResolver(),
                Settings.System.AIRPLANE_MODE_ON, 0) != 0;
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();
        boolean internetAccess = (ni != null && ni.isConnected());
        if(airplaneMode){
            DialogFragment airplaneModeDialog = new AirplaneModeDialogFragment();
            airplaneModeDialog.show(getFragmentManager(), "airplaneMode");
        } if(!internetAccess) {
            fireFailedAlert();
        }

        String url = preferences.getString("questionDataURL", "http://tednewardsandbox.site44.com/questions.json");
        if(!preferences.contains(DOWNLOAD_ID)) {
            Uri resource = Uri.parse(url);
            DownloadManager.Request request = new DownloadManager.Request(resource);
            request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE | DownloadManager.Request.NETWORK_WIFI);
            request.setAllowedOverRoaming(false);
            request.setTitle("Download Questions");
            long id = dm.enqueue(request);
            preferences.edit().putLong(DOWNLOAD_ID, id).commit();
        } else {
            downloadStatus();
        }
    }



    // Receiver; is called every x minutes, where x is specified by user in settings
    private final BroadcastReceiver receiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            Log.i("MainActivity BR", "BroadcastReceiver onReceive");

            checkForUpdates();

            String url = preferences.getString("questionDataURL", "http://tednewardsandbox.site44.com/questions.json");
            Toast.makeText(MainActivity.this, "URL: " + url, Toast.LENGTH_SHORT).show();
            downloadStatus();




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

/*
    public void showToast() {
        Log.i("MainActivity", "Toast should show");
        SharedPreferences p = PreferenceManager.getDefaultSharedPreferences(this);
        String url = p.getString("questionDataURL", "default");
        Toast.makeText(MainActivity.this, "URL: " + url, Toast.LENGTH_SHORT).show();
    }
*/

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
