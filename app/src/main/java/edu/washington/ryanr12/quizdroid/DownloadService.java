package edu.washington.ryanr12.quizdroid;

import android.app.AlarmManager;
import android.app.DownloadManager;
import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

/**
 * Created by Ryan on 5/19/2015.
 */
public class DownloadService extends IntentService {
    private DownloadManager downloadManager;
    private long enqueue;
    public static final int REQUEST_CODE = 123;

    public DownloadService() {
        super("DownloadService");
    }

    public void onCreate() {
        super.onCreate();
    }

    public void onDestroy() {
        super.onDestroy();
    }

    protected void onHandleIntent(Intent workIntent) {
        Log.i("DownloadService", "handling alarm intent");

        String url = "http://tednewardsandbox.site44.com/questions.json";
        Log.i("DownloadService", "Download Beginning");
        downloadManager = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
        enqueue = downloadManager.enqueue(request);
    }

    public static void manageAlarm(Context context, boolean on) {
        Log.i("DownloadService", "Setting alarm on to: " + on);

/*
        Intent i = new Intent(context, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, i,
                PendingIntent.FLAG_UPDATE_CURRENT);
*/

        Intent i = new Intent();
        i.setAction("edu.washington.ryanr12.quizdroid");
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, i,
                PendingIntent.FLAG_UPDATE_CURRENT);


        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        if(on) {

            int refreshInterval = 5000;
            Log.i("DownloadService", "setting alarm to " + refreshInterval);
            alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(),
                    refreshInterval, pendingIntent);
        } else {
            alarmManager.cancel(pendingIntent);
            pendingIntent.cancel();

            Log.i("DownloadService", "Alarm Cancelled");
        }
    }
}
