package kidslauncher.alex.com.kidslauncher.utils;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import kidslauncher.alex.com.kidslauncher.KidsLauncherApp;
import kidslauncher.alex.com.kidslauncher.receivers.AlarmReceiver;
import kidslauncher.alex.com.kidslauncher.ui.activities.HomeActivity;

public class AlarmUtils {

    public static final int REQUEST_CODE = 1;

    public static void scheduleRepetingAlarm(long timerTime) {
        final KidsLauncherApp context = KidsLauncherApp.getInstance();
        final AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
//        final Intent intent = new Intent(context, AlarmReceiver.class);
        final Intent intent = new Intent(HomeActivity.TAG);
        final PendingIntent pendingIntent = PendingIntent.getBroadcast(context, REQUEST_CODE, intent, 0);

        alarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                timerTime,
                timerTime, pendingIntent);
    }

    public static void cancelAlarm() {
        final KidsLauncherApp context = KidsLauncherApp.getInstance();

        Intent intent = new Intent(context, AlarmReceiver.class);
        intent.setAction(HomeActivity.TAG);
        PendingIntent sender = PendingIntent.getBroadcast(context, REQUEST_CODE, intent, 0);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        alarmManager.cancel(sender);
    }


}
