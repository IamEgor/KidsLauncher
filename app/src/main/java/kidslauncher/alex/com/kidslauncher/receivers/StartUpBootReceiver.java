package kidslauncher.alex.com.kidslauncher.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import kidslauncher.alex.com.kidslauncher.ui.activities.LoginActivity;
import kidslauncher.alex.com.kidslauncher.utils.PreferencesUtil;

public class StartUpBootReceiver extends BroadcastReceiver {

    public static final String TAG = StartUpBootReceiver.class.getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {
        if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {
            Log.d(TAG, "StartUpBootReceiver BOOT_COMPLETED");
            if (PreferencesUtil.getInstance().isBootOnStart()) {
                final Intent startActivity = new Intent(context, LoginActivity.class);
                startActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(startActivity);
            }
        }
    }
}