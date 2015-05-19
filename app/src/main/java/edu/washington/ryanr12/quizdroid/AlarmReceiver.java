package edu.washington.ryanr12.quizdroid;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by Ryan on 5/19/2015.
 */
public class AlarmReceiver extends BroadcastReceiver {
    public AlarmReceiver() {

    }

    public void onReceive(Context context, Intent intent) {
        Log.i("AlarmReceiver", "Received alarm broadcast");

        if(context instanceof MainActivity) {
            ((MainActivity)context).showToast();
        }

        // Start download
        Intent downloadServiceIntent = new Intent(context, DownloadService.class);
        context.startService(downloadServiceIntent);
    }
}
