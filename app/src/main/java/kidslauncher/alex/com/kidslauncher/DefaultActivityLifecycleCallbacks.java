package kidslauncher.alex.com.kidslauncher;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.util.Log;

public class DefaultActivityLifecycleCallbacks implements Application.ActivityLifecycleCallbacks {

    public static final String TAG = DefaultActivityLifecycleCallbacks.class.getSimpleName();

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        Log.w(TAG, "onActivityCreated : " + activity.getClass().getSimpleName());
    }

    @Override
    public void onActivityStarted(Activity activity) {
        Log.w(TAG, "onActivityStarted : " + activity.getClass().getSimpleName());
    }

    @Override
    public void onActivityResumed(Activity activity) {
        Log.w(TAG, "onActivityResumed : " + activity.getClass().getSimpleName());
    }

    @Override
    public void onActivityPaused(Activity activity) {
        Log.w(TAG, "onActivityPaused : " + activity.getClass().getSimpleName());
    }

    @Override
    public void onActivityStopped(Activity activity) {
        Log.w(TAG, "onActivityStopped : " + activity.getClass().getSimpleName());
    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
        Log.w(TAG, "onActivitySaveInstanceState : " + activity.getClass().getSimpleName());
    }

    @Override
    public void onActivityDestroyed(Activity activity) {
        Log.w(TAG, "onActivityDestroyed : " + activity.getClass().getSimpleName());
    }
}
