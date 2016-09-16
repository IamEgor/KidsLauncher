package kidslauncher.alex.softteco.com.kidslauncher.activities;

import android.app.LoaderManager;
import android.content.Loader;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.google.gson.Gson;

import java.util.List;

import kidslauncher.alex.softteco.com.kidslauncher.R;
import kidslauncher.alex.softteco.com.kidslauncher.loaders.AppInfoLoader;
import kidslauncher.alex.softteco.com.kidslauncher.storage.PreferencesUtil;
import kidslauncher.alex.softteco.com.kidslauncher.ui.adapters.SectionsPagerAdapter;
import kidslauncher.alex.softteco.com.kidslauncher.ui.models.AppItemModel;

public class PagerActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<List<AppItemModel>> {

    private SectionsPagerAdapter mSectionsPagerAdapter;

    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main22);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mViewPager = (ViewPager) findViewById(R.id.container);

        Log.w("onCreate", new Gson().toJson(PreferencesUtil.getInstance().getSelectedApps()));

        getLoaderManager().initLoader(0, null, this).forceLoad();
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main22, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
