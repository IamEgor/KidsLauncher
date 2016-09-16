package kidslauncher.alex.softteco.com.kidslauncher.activities;

import android.app.Activity;
import android.app.LoaderManager;
import android.content.Intent;
import android.content.Loader;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;

import com.google.gson.Gson;

import java.util.List;

import kidslauncher.alex.softteco.com.kidslauncher.R;
import kidslauncher.alex.softteco.com.kidslauncher.loaders.AppInfoLoader;
import kidslauncher.alex.softteco.com.kidslauncher.storage.PreferencesUtil;
import kidslauncher.alex.softteco.com.kidslauncher.ui.adapters.SectionsPagerAdapter;
import kidslauncher.alex.softteco.com.kidslauncher.ui.models.AppItemModel;

public class HomeActivity extends Activity implements
        LoaderManager.LoaderCallbacks<List<AppItemModel>> {

    private static final int REQUEST_CODE = 1;

    private View mNoAppsLayout;
    private ViewPager mViewPager;

    private SectionsPagerAdapter mSectionsPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        mNoAppsLayout = findViewById(R.id.item_no_selected);
        mViewPager = (ViewPager) findViewById(R.id.content);

        showContentLayout(PreferencesUtil.getInstance().getSelectedApps() != null);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data == null) {
            return;
        }
        List<AppItemModel> apps = data.getParcelableArrayListExtra(SelectAppsActivity.KEY_APPS);
        if (apps != null) {
            Log.w("onActivityResult", "apps = " + new Gson().toJson(apps));
            mSectionsPagerAdapter = new SectionsPagerAdapter(getFragmentManager(), apps);
            mViewPager.setAdapter(mSectionsPagerAdapter);
            showContentLayout(true);
        }
    }

    @Override
    public Loader<List<AppItemModel>> onCreateLoader(int i, Bundle bundle) {
        return new AppInfoLoader(this);
    }

    @Override
    public void onLoadFinished(Loader<List<AppItemModel>> loader, List<AppItemModel> appItemModel) {
        mSectionsPagerAdapter = new SectionsPagerAdapter(getFragmentManager(), appItemModel);
        mViewPager.setAdapter(mSectionsPagerAdapter);
    }

    @Override
    public void onLoaderReset(Loader loader) {

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