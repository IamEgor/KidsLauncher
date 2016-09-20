package kidslauncher.alex.com.kidslauncher.utils;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import kidslauncher.alex.com.kidslauncher.KidsLauncherApp;
import kidslauncher.alex.com.kidslauncher.receivers.AlarmReceiver;
import kidslauncher.alex.com.kidslauncher.ui.activities.AbstractActivity;
import kidslauncher.alex.com.kidslauncher.ui.activities.HomeActivity;

public class AlarmUtils {

    public static final int REQUEST_CODE = 1;

    public static void scheduleRepeatingAlarm(long timerTime) {
        final KidsLauncherApp context = KidsLauncherApp.getInstance();
        final AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        final Intent intent = new Intent(AbstractActivity.ALARM_ACTION);
        final PendingIntent pendingIntent = PendingIntent.getBroadcast(context, REQUEST_CODE, intent, 0);

        alarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                timerTime,
                timerTime, pendingIntent);
    }

    public static void scheduleAlarm(long delay) {
        final KidsLauncherApp context = KidsLauncherApp.getInstance();
        final AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        final Intent intent = new Intent(AbstractActivity.ALARM_ACTION);
        final PendingIntent pendingIntent = PendingIntent.getBroadcast(context, REQUEST_CODE, intent, 0);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + delay, pendingIntent);
        } else {
            alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + delay, pendingIntent);
        }
    }

    public static void cancelAlarm() {
        final KidsLauncherApp context = KidsLauncherApp.getInstance();
        final AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        final Intent intent = new Intent(AbstractActivity.ALARM_ACTION);
        final PendingIntent sender = PendingIntent.getBroadcast(context, REQUEST_CODE, intent, 0);

        alarmManager.cancel(sender);
    }


}
