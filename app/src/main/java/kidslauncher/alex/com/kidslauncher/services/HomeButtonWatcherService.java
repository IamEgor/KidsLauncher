package kidslauncher.alex.com.kidslauncher.services;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.Timer;
import java.util.TimerTask;

import kidslauncher.alex.com.kidslauncher.KidsLauncherApp;
import kidslauncher.alex.com.kidslauncher.ui.activities.HomeActivity;
import kidslauncher.alex.com.kidslauncher.utils.HomeButtonWatcher;
import kidslauncher.alex.com.kidslauncher.utils.OnHomePressedListener;
import kidslauncher.alex.com.kidslauncher.utils.PreferencesUtil;

public class HomeButtonWatcherService extends Service {

    private static final int AFTER_LONG_PRESS_ON_HOME_BUTTON = 500;
    private static final int DEFAULT_START_ACTIVITY_DELAY = 5001;
    private static final int CHECKER_DELAY = 10 * 1000;
    private static final String LOG_TAG = HomeButtonWatcherService.class.getName();

    private MyBinder binder = new MyBinder();
    private HomeButtonWatcher mHomeButtonWatcher;

    public void onCreate() {
        super.onCreate();
        Log.w(LOG_TAG, LOG_TAG + " onCreate");
        if (!PreferencesUtil.getInstance().isCloseAllowed()) {
            mHomeButtonWatcher = new HomeButtonWatcher(this);
            mHomeButtonWatcher.setOnHomePressedListener(new OnHomePressedListener() {

                @Override
                public void onHomePressed() {
                    Log.w(LOG_TAG, "HomeButtonWatcher.onHomePressed() \n" + "needToRestartActivity = " + needToRestartActivity());
                    restartActivityIfNeeded(DEFAULT_START_ACTIVITY_DELAY);
                }

                @Override
                public void onHomeLongPressed() {
                    Log.w(LOG_TAG, "HomeButtonWatcher.onHomeLongPressed() \n" + "needToRestartActivity = " + needToRestartActivity());
                    restartActivityIfNeeded(AFTER_LONG_PRESS_ON_HOME_BUTTON);
                }

                @Override
                public void onGoogleHelperCall() {
                    Log.w(LOG_TAG, "HomeButtonWatcher.onGoogleHelperCall() \n" + "needToRestartActivity = " + needToRestartActivity());
                    restartActivityIfNeeded(AFTER_LONG_PRESS_ON_HOME_BUTTON);
                }

                private void restartActivityIfNeeded(int delay) {
                    if (needToRestartActivity()) {
                        restartActivity(delay);
                    }
                }

                private boolean needToRestartActivity() {
                    return !PreferencesUtil.getInstance().isCloseAllowed();
                }
            });
            mHomeButtonWatcher.startWatch();
            restartActivity();
            Timer timer = new Timer();
            timer.schedule(new ForegroundChecker(), CHECKER_DELAY, CHECKER_DELAY);
        } else {
            PreferencesUtil.getInstance().setCloseAllowed(false);
            stopSelf();
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent arg0) {
        Log.w(LOG_TAG, LOG_TAG + " onBind");
        return binder;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.w(LOG_TAG, LOG_TAG + " onDestroy");
        if (mHomeButtonWatcher != null) {
            mHomeButtonWatcher.stopWatch();
        }
    }

    public void restartActivity(int delay) {
        new Handler().postDelayed(HomeButtonWatcherService::restartActivity, delay);
    }

    public static void restartActivity() {
        Intent i = new Intent(KidsLauncherApp.getInstance(), HomeActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        KidsLauncherApp.getInstance().startActivity(i);
        Log.d(LOG_TAG, LOG_TAG + " restartActivity");
    }

    public class MyBinder extends Binder {
        public HomeButtonWatcherService getService() {
            return HomeButtonWatcherService.this;
        }
    }

    static class ForegroundChecker extends TimerTask {

        private static final String TAG = ForegroundChecker.class.getSimpleName();

        @Override
        public void run() {
            if (needToRestartActivity()) {
                restartActivity();
            }
        }

        private boolean needToRestartActivity() {
            return !PreferencesUtil.getInstance().isAppInForeground() && !PreferencesUtil.getInstance().isCloseAllowed();
        }
    }

}
