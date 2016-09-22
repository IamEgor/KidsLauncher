package kidslauncher.alex.com.kidslauncher;

import android.app.Application;
import android.os.IBinder;

import kidslauncher.alex.com.kidslauncher.utils.PreferencesUtil;

public class KidsLauncherApp extends Application {

    private static KidsLauncherApp instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }

    @Override
    public void onTerminate() {
        PreferencesUtil.getInstance().setBlockIncoming(false);
        PreferencesUtil.getInstance().setBlockWifi(false);
        super.onTerminate();
    }

    public static KidsLauncherApp getInstance() {
        return instance;
    }

}