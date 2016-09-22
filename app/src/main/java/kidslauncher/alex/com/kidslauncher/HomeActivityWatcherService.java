package kidslauncher.alex.com.kidslauncher;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import kidslauncher.alex.com.kidslauncher.ui.activities.HomeActivity;


public class HomeActivityWatcherService extends Service {

    private static final String LOG_TAG = HomeActivityWatcherService.class.getName();

    private MyBinder binder = new MyBinder();

    public void onCreate() {
        super.onCreate();
        Log.d(LOG_TAG, LOG_TAG + " onCreate");
    }

    @Nullable
    @Override
    public IBinder onBind(Intent arg0) {
        Log.d(LOG_TAG, LOG_TAG + " onBind");
        return binder;
    }

    public void restartActivity(int delay) {
        new Handler().postDelayed(this::restartActivity, delay);
    }

    public void restartActivity() {
        Intent i = new Intent(this, HomeActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
    }

    public class MyBinder extends Binder {
        public HomeActivityWatcherService getService() {
            return HomeActivityWatcherService.this;
        }
    }
}
