package app_utility;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import com.bel.antimatter.R;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import static android.content.ContentValues.TAG;
import static androidx.core.app.NotificationCompat.PRIORITY_MAX;

public class AdminRegisterService extends Service implements OnAsyncInterfaceListener {

    String channelId = "app_utility.OfflineTransferService";
    String channelName = "offline_transfer";

    public static AdminRegisterService refOfService;
    /*startService(new Intent(MyService.ServiceIntent));
    stopService(new Intent((MyService.ServiceIntent));*/

    public static OnAsyncInterfaceListener onAsyncInterfaceListener;
    SharedPreferencesClass sharedPreferencesClass;

    NotificationManager notifyMgr;
    NotificationCompat.Builder nBuilder;
    NotificationCompat.InboxStyle inboxStyle;

    //AsyncInterface asyncInterface;

    Timer timer = new Timer();
    Handler handler = new Handler();
    public String VOLLEY_STATUS = "NOT_RUNNING";

    long startTime = 0;
    long endTime = 0;
    long totalTime = 0;

    BELAsyncTask belAsyncTask;
    DatabaseHandler db;
    ArrayList<DataBaseHelper> alDBTemporaryData;


    public AdminRegisterService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        /*
        this will make sure service will run on background in oreo and above
        service wont run without a notification from oreo version.
        After the system has created the service, the app has five seconds to call the service's startForeground() method
        to show the new service's user-visible notification. If the app does not call startForeground() within the time limit,
        the system stops the service and declares the app to be ANR.
         */
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForeground();
        }

        refOfService = this;
        onAsyncInterfaceListener = this;
        db = new DatabaseHandler(getApplicationContext());
        sharedPreferencesClass = new SharedPreferencesClass(getApplicationContext());
        TimerTask doAsynchronousTask = new TimerTask() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    public void run() {
                        alDBTemporaryData = new ArrayList<>(db.getAllEmployeeData());
                        if (alDBTemporaryData.size() >= 1 && VOLLEY_STATUS.equals("NOT_RUNNING")) {
                            belAsyncTask = new BELAsyncTask(getApplicationContext(), alDBTemporaryData, db);
                            belAsyncTask.execute(String.valueOf(2), sharedPreferencesClass.getUserName());
                            VOLLEY_STATUS = "RUNNING";
                        }
                        /*if (sharedPreferencesClass.getUserOdooID() == StaticReferenceClass.DEFAULT_ODOO_ID) {
                            belAsyncTask = new BELAsyncTask(getApplicationContext());
                            belAsyncTask.execute(String.valueOf(5), sharedPreferencesClass.getUserName());
                        } else {
                            alDBTemporaryData = new ArrayList<>(db.getAllTemporaryData());
                            if (alDBTemporaryData.size() >= 1) {
                                belAsyncTask = new BELAsyncTask(getApplicationContext(), alDBTemporaryData, db);
                                belAsyncTask.execute(String.valueOf(6), String.valueOf(sharedPreferencesClass.getUserOdooID()));
                            }
                        }*/
                        //Toast.makeText(getApplicationContext(), "I am still running", Toast.LENGTH_LONG).show();
                        Log.e("Service status: ", "RUNNING");
                    }
                });
            }
        };
        //Starts after 20 sec and will repeat on every 20 sec of time interval.
        timer.schedule(doAsynchronousTask, 0, 10000);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void startForeground() {
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(getApplicationContext(), createNotificationChannel());
        Notification notification = notificationBuilder.setOngoing(true)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setPriority(PRIORITY_MAX)
                .setCategory(Notification.CATEGORY_SERVICE)
                .build();
        startForeground(101, notification);
    }


    @RequiresApi(Build.VERSION_CODES.O)
    private String createNotificationChannel() {
        NotificationChannel chan = new NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_NONE);
        chan.setLightColor(Color.BLUE);
        chan.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
        notifyMgr = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notifyMgr.createNotificationChannel(chan);
        return channelId;
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e(TAG, "onStartCommand");
        super.onStartCommand(intent, flags, startId);

        return START_STICKY;
    }

    @Override
    public void onTaskRemoved(Intent in) {
        Log.e("Service is killed", "");
        super.onTaskRemoved(in);
        /*if (sharedPreferenceClass.getUserTraceableInfo()) {
            Intent intent = new Intent("app_utility.TrackingService.ServiceStopped");
            sendBroadcast(intent);
        }*/
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //TrackingService.this.stopSelf();
        timer.cancel();
        timer.purge();

        //refOfService.stopForeground(true);
        refOfService.stopSelf();
        /*if (sharedPreferenceClass.getUserTraceableInfo()) {
            Intent intent = new Intent("app_utility.TrackingService.ServiceStopped");
            sendBroadcast(intent);
        }*/

        Log.i(TAG, "Service destroyed ...");
    }

    @Override
    public void onAsyncComplete(String sMSG, int type, String sResult) {
        switch (sMSG) {
            case "ODOO_ID_RETRIEVED":
                if (type != StaticReferenceClass.DEFAULT_ODOO_ID) {
                    sharedPreferencesClass.setUserOdooID(type);
                }
                break;
        }
        VOLLEY_STATUS = "NOT_RUNNING";
    }

}
