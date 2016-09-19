package kidslauncher.alex.com.kidslauncher.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.List;

import kidslauncher.alex.com.kidslauncher.R;
import kidslauncher.alex.com.kidslauncher.ui.adapters.SectionsPagerAdapter;
import kidslauncher.alex.com.kidslauncher.ui.models.AppItemModel;
import kidslauncher.alex.com.kidslauncher.utils.CommonUtils;
import kidslauncher.alex.com.kidslauncher.utils.PreferencesUtil;

public class HomeActivity extends AbstractActivity {

    public static final String TAG = HomeActivity.class.getName();
    public static final int REQUEST_CODE = 1;

    private ImageView mLeftButton;
    private ImageView mRightButton;
    private ImageView mRightButtonAdditional;
    private View mNoAppsLayout;
    private ViewPager mViewPager;

    private boolean childModeEnabled;
    private SectionsPagerAdapter mSectionsPagerAdapter;

    private PositiveAction turnOn = () -> {
        childModeEnabled = !childModeEnabled;
        mRightButtonAdditional.setColorFilter(getResources().getColor(childModeEnabled ? R.color.colorAccent : R.color.white));
        CommonUtils.disableWifi();
        PreferencesUtil.getInstance().setBlockIncoming(childModeEnabled);
        PreferencesUtil.getInstance().setBlockWifi(childModeEnabled);
    };
    private PositiveAction settingsAction = () ->
            startActivity(new Intent(HomeActivity.this, SettingsActivity.class));

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        initViews();
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
    }

    private void initViews() {
        mLeftButton = (ImageView) findViewById(R.id.left_toolbar_btn);
        mRightButton = (ImageView) findViewById(R.id.right_toolbar_btn);
        mRightButtonAdditional = (ImageView) findViewById(R.id.right_toolbar_btn_additional);
        mNoAppsLayout = findViewById(R.id.item_no_selected);
        mViewPager = (ViewPager) findViewById(R.id.content);

        mLeftButton.setVisibility(View.INVISIBLE);
        mRightButton.setVisibility(View.VISIBLE);
        mRightButton.setImageResource(R.drawable.ic_settings_black_24dp);
        mRightButtonAdditional.setVisibility(View.VISIBLE);
        mRightButtonAdditional.setImageResource(R.drawable.ic_power_settings_new_black_24dp);

        mRightButtonAdditional.setOnClickListener(view -> {
            final List<AppItemModel> selectedApps = PreferencesUtil.getInstance().getSelectedApps();
            if (selectedApps == null || selectedApps.size() == 0) {
                Toast.makeText(HomeActivity.this, "No apps selected", Toast.LENGTH_SHORT).show();
            } else {
                actAfterPasswordAccepted(turnOn);
            }
        });
        mRightButton.setOnClickListener(view -> actAfterPasswordAccepted(settingsAction));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
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

    private void showContentLayout(boolean show) {
        mViewPager.setVisibility(show ? View.VISIBLE : View.GONE);
        mNoAppsLayout.setVisibility(show ? View.GONE : View.VISIBLE);
    }

    public void selectApps(View view) {
        Intent i = new Intent(this, SelectAppsActivity.class);
        startActivityForResult(i, REQUEST_CODE);
    }

}