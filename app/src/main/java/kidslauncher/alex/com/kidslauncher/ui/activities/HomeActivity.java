package kidslauncher.alex.com.kidslauncher.ui.activities;

import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Toast;

import java.util.List;

import kidslauncher.alex.com.kidslauncher.utils.HomeWatcher;
import kidslauncher.alex.com.kidslauncher.utils.OnHomePressedListener;
import kidslauncher.alex.com.kidslauncher.R;
import kidslauncher.alex.com.kidslauncher.receivers.AlarmReceiver;
import kidslauncher.alex.com.kidslauncher.ui.adapters.SectionsPagerAdapter;
import kidslauncher.alex.com.kidslauncher.ui.models.AppItemModel;
import kidslauncher.alex.com.kidslauncher.utils.AlarmUtils;
import kidslauncher.alex.com.kidslauncher.utils.CommonUtils;
import kidslauncher.alex.com.kidslauncher.utils.PreferencesUtil;

public class HomeActivity extends AbstractActivity {

    public static final String TAG = HomeActivity.class.getSimpleName();
    public static final int REQUEST_CODE = 1;

    private View mNoAppsLayout;
    private ViewPager mViewPager;

    private boolean childModeEnabled;
    private SectionsPagerAdapter mSectionsPagerAdapter;
    private BroadcastReceiver mReceiver;
    private boolean isAfterLongPressHomeButton;
    private HomeWatcher mHomeWatcher;

    private PositiveAction turnOn = () -> {
        childModeEnabled = !childModeEnabled;
        mRightButtonAdditional.setColorFilter(getResources().getColor(childModeEnabled ? R.color.colorAccent : R.color.white));
        CommonUtils.setWifiEnabled(false);
        PreferencesUtil.getInstance().setBlockIncoming(childModeEnabled);
        PreferencesUtil.getInstance().setBlockWifi(childModeEnabled);
    };
    private PositiveAction settingsAction = () ->
            startActivity(new Intent(HomeActivity.this, SettingsActivity.class));

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initViews();
        mHomeWatcher = new HomeWatcher(this);
        mHomeWatcher.setOnHomePressedListener(new OnHomePressedListener() {
            @Override
            public void onHomePressed() {
                Log.w(TAG, "HomeWatcher.onHomePressed()");
                Toast.makeText(HomeActivity.this, R.string.on_back_pressed_message, Toast.LENGTH_SHORT).show();
                Intent i = new Intent(HomeActivity.this, HomeActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(i);
            }

            @Override
            public void onHomeLongPressed() {
                Log.w(TAG, "HomeWatcher.onHomeLongPressed()");
                isAfterLongPressHomeButton = true;
            }
        });
        mHomeWatcher.startWatch();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
        if (data == null) {
            return;
        }
        List<AppItemModel> apps = data.getParcelableArrayListExtra(SelectAppsActivity.KEY_APPS);
        if (apps != null) {
            PreferencesUtil.getInstance(this).setSelectedApps(apps);
            Log.w(TAG, "onActivityResult:  " + apps);
            mSectionsPagerAdapter = new SectionsPagerAdapter(getFragmentManager(), apps);
            mViewPager.setAdapter(mSectionsPagerAdapter);
            mSectionsPagerAdapter.notifyDataSetChanged();
            showContentLayout(true);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.w(TAG, "onStop(); isAfterLongPressHomeButton = " + isAfterLongPressHomeButton);
    }

    @Override
    protected void onDestroy() {
        Log.w(TAG, "onDestroy(); isAfterLongPressHomeButton = " + isAfterLongPressHomeButton);
//        if (isAfterLongPressHomeButton) {
//            Log.w(TAG, "trying to restart");
//            Intent i = new Intent(HomeActivity.this, HomeActivity.class);
////            i.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
//            startActivity(i);
//        }
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
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_HOME) {
            Toast.makeText(HomeActivity.this, "onKeyDown KEYCODE_HOME", Toast.LENGTH_SHORT).show();
            return true;
        }
        if (keyCode == KeyEvent.KEYCODE_MOVE_HOME) {
            Toast.makeText(HomeActivity.this, "onKeyDown KEYCODE_MOVE_HOME", Toast.LENGTH_SHORT).show();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onResume() {
        super.onResume();
        final List<AppItemModel> selectedApps = PreferencesUtil.getInstance().getSelectedApps();
        if (selectedApps != null) {
            mSectionsPagerAdapter = new SectionsPagerAdapter(getFragmentManager(), selectedApps);
            mViewPager.setAdapter(mSectionsPagerAdapter);
            showContentLayout(true);
        } else {
            showContentLayout(false);
        }
        setTimer(8 * 1000);
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

        mRightButtonAdditional.setOnClickListener(view -> {
            final List<AppItemModel> selectedApps = PreferencesUtil.getInstance().getSelectedApps();
            if (selectedApps == null || selectedApps.size() == 0) {
                Toast.makeText(HomeActivity.this, R.string.no_apps_selected_message, Toast.LENGTH_SHORT).show();
            } else {
                actAfterPasswordAccepted(turnOn);
            }
        });
        mRightButton.setOnClickListener(view -> actAfterPasswordAccepted(settingsAction));
    }

    private void showContentLayout(boolean show) {
        mViewPager.setVisibility(show ? View.VISIBLE : View.GONE);
        mNoAppsLayout.setVisibility(show ? View.GONE : View.VISIBLE);
    }

    public void selectApps(View view) {
        Intent i = new Intent(this, SelectAppsActivity.class);
        startActivityForResult(i, REQUEST_CODE);
    }

}