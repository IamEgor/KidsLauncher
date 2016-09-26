package kidslauncher.alex.com.kidslauncher;

import android.app.Application;

import kidslauncher.alex.com.kidslauncher.utils.DefaultActivityLifecycleCallbacks;
import kidslauncher.alex.com.kidslauncher.utils.PreferencesUtil;

public class KidsLauncherApp extends Application {

    private static KidsLauncherApp instance;

    private DefaultActivityLifecycleCallbacks callback;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        callback = new DefaultActivityLifecycleCallbacks();
        registerActivityLifecycleCallbacks(callback);
    }

    public int getActiveActivitiesCount(){
        return callback.getActivitiesInForegroundCount();
    }

    public static KidsLauncherApp getInstance() {
        return instance;
    }

}