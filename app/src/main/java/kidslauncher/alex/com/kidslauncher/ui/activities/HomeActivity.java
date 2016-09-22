package kidslauncher.alex.com.kidslauncher.ui.activities;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.util.HashMap;
import java.util.List;

import kidslauncher.alex.com.kidslauncher.HomeActivityWatcherService;
import kidslauncher.alex.com.kidslauncher.R;
import kidslauncher.alex.com.kidslauncher.ui.adapters.SectionsPagerAdapter;
import kidslauncher.alex.com.kidslauncher.ui.models.AppItemModel;
import kidslauncher.alex.com.kidslauncher.utils.CommonUtils;
import kidslauncher.alex.com.kidslauncher.utils.HomeWatcher;
import kidslauncher.alex.com.kidslauncher.utils.OnHomePressedListener;
import kidslauncher.alex.com.kidslauncher.utils.PreferencesUtil;

public class HomeActivity extends AbstractActivity {

    public static final String TAG = HomeActivity.class.getSimpleName();
    public static final int REQUEST_CODE_SELECT_APPS = 1;
    public static final int REQUEST_CODE_PREFERENCES = 2;

    private View mNoAppsLayout;
    private ViewPager mViewPager;

    private boolean childModeEnabled;
    private SectionsPagerAdapter mSectionsPagerAdapter;
    private boolean isAfterLongPressHomeButton;
    private boolean isAfterPressHomeButton;
    private HomeWatcher mHomeWatcher;

    private HomeActivityWatcherService mWatcherService;
    private boolean mBound = false;

    private PositiveAction turnOn = () -> {
        childModeEnabled = !childModeEnabled;
        mRightButtonAdditional.setColorFilter(getResources().getColor(childModeEnabled ? R.color.colorAccent : R.color.white));
        CommonUtils.setWifiEnabled(false);
        PreferencesUtil.getInstance().setBlockIncoming(childModeEnabled);
        PreferencesUtil.getInstance().setBlockWifi(childModeEnabled);
    };
    private PositiveAction settingsAction = () ->
            startActivityForResult(new Intent(HomeActivity.this, SettingsActivity.class), REQUEST_CODE_PREFERENCES);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initViews();
        initHomeButtonActors();
    }

    protected void initHomeButtonActors() {
        mHomeWatcher = new HomeWatcher(this);
        mHomeWatcher.setOnHomePressedListener(new OnHomePressedListener() {
            @Override
            public void onHomePressed() {
                Log.w(TAG, "HomeWatcher.onHomePressed()");
                isAfterPressHomeButton = true;
            }

            @Override
            public void onHomeLongPressed() {
                Log.w(TAG, "HomeWatcher.onHomeLongPressed()");
                isAfterLongPressHomeButton = true;
            }
        });
        mHomeWatcher.startWatch();
        // Bind to LocalService
        Intent intent = new Intent(this, HomeActivityWatcherService.class);
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data == null) {
            return;
        }
        if (requestCode == REQUEST_CODE_SELECT_APPS) {
            List<AppItemModel> apps = data.getParcelableArrayListExtra(SelectAppsActivity.KEY_APPS);
            if (apps != null) {
                PreferencesUtil.getInstance().setSelectedApps(apps);
                Log.w(TAG, "onActivityResult:  " + apps);
                mSectionsPagerAdapter = new SectionsPagerAdapter(getFragmentManager(), apps);
                mViewPager.setAdapter(mSectionsPagerAdapter);
                mSectionsPagerAdapter.notifyDataSetChanged();
                showContentLayout(true);
            }
        } else if (requestCode == REQUEST_CODE_PREFERENCES) {
            HashMap<String, ?> map = (HashMap<String, ?>) data.getSerializableExtra(SettingsActivity.KEY_PREFERENCES);
            Boolean isHomeActivity = (Boolean) map.get(getString(R.string.is_home_activity_pref));
            if (isHomeActivity != null) {
                switchActivities(isHomeActivity);
            }
        }
    }

    private void switchActivities(Boolean isHomeActivity) {
        Log.w("switchActivities", "this instanceof HomeLauncherActivity " + (this instanceof HomeLauncherActivity));
        if (!(this instanceof HomeLauncherActivity) && isHomeActivity) {
            resetPreferredLauncherAndOpenChooser();
        } else if (this instanceof HomeLauncherActivity && !isHomeActivity) {
            Intent startMain = new Intent(this, HomeActivity.class);
            startActivity(startMain);
            finish();
            if(isMyLauncherDefault()){
                resetPreferredLauncherAndOpenChooser();
            }
        }
    }

    public void resetPreferredLauncherAndOpenChooser() {
        PackageManager packageManager = getPackageManager();
        ComponentName componentName = new ComponentName(this, HomeLauncherActivity.class);
        packageManager.setComponentEnabledSetting(componentName, PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP);

        Intent selector = new Intent(Intent.ACTION_MAIN);
        selector.addCategory(Intent.CATEGORY_HOME);
        selector.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(selector);

        packageManager.setComponentEnabledSetting(componentName, PackageManager.COMPONENT_ENABLED_STATE_DEFAULT, PackageManager.DONT_KILL_APP);
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.w(TAG, "onStop(); isAfterLongPressHomeButton == " + isAfterLongPressHomeButton);
        if (mBound && (isAfterLongPressHomeButton || isAfterPressHomeButton)) {
            mWatcherService.restartActivity();
            isAfterPressHomeButton = false;
            isAfterLongPressHomeButton = false;
        }
        // Unbind from the service
        if (mBound) {
            unbindService(mConnection);
            mBound = false;
        }
    }

    @Override
    protected void onDestroy() {
        if (mHomeWatcher != null) {
            mHomeWatcher.stopWatch();
        }
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        Toast.makeText(HomeActivity.this, R.string.on_back_pressed_message, Toast.LENGTH_SHORT).show();
    }

    @Override
    int setContentViewResource() {
        return R.layout.activity_home;
    }

    private void initViews() {
        mNoAppsLayout = findViewById(R.id.item_no_selected);
        mViewPager = (ViewPager) findViewById(R.id.selected_apps_viewpager);

        mLeftButton.setVisibility(View.INVISIBLE);
        mRightButton.setVisibility(View.VISIBLE);
        mRightButton.setImageResource(R.drawable.ic_settings_black_24dp);
        mRightButtonAdditional.setVisibility(View.VISIBLE);
        mRightButtonAdditional.setImageResource(R.drawable.ic_power_settings_new_black_24dp);
        mExitButton.setVisibility(View.VISIBLE);

        mRightButtonAdditional.setOnClickListener(view -> actAfterPasswordAccepted(turnOn));
        mRightButton.setOnClickListener(view -> actAfterPasswordAccepted(settingsAction));

        final List<AppItemModel> selectedApps = PreferencesUtil.getInstance().getSelectedApps();
        if (selectedApps != null) {
            mSectionsPagerAdapter = new SectionsPagerAdapter(getFragmentManager(), selectedApps);
            mViewPager.setAdapter(mSectionsPagerAdapter);
            showContentLayout(true);
        } else {
            showContentLayout(false);
        }
    }

    public void selectApps(View view) {
        Intent i = new Intent(this, SelectAppsActivity.class);
        startActivityForResult(i, REQUEST_CODE_SELECT_APPS);
    }

    private void showContentLayout(boolean show) {
        mViewPager.setVisibility(show ? View.VISIBLE : View.GONE);
        mNoAppsLayout.setVisibility(show ? View.GONE : View.VISIBLE);
    }

    /**
     * Defines callbacks for service binding, passed to bindService()
     */
    private ServiceConnection mConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className,
                                       IBinder service) {
            // We've bound to LocalService, cast the IBinder and get LocalService instance
            HomeActivityWatcherService.MyBinder binder = (HomeActivityWatcherService.MyBinder) service;
            mWatcherService = binder.getService();
            mBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            mBound = false;
        }
    };

}