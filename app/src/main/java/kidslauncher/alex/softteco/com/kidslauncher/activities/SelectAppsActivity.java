package kidslauncher.alex.softteco.com.kidslauncher.activities;

import android.app.LoaderManager;
import android.content.Intent;
import android.content.Loader;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.annimon.stream.Collectors;
import com.annimon.stream.Stream;

import java.util.ArrayList;
import java.util.List;

import kidslauncher.alex.softteco.com.kidslauncher.R;
import kidslauncher.alex.softteco.com.kidslauncher.loaders.AppInfoLoader;
import kidslauncher.alex.softteco.com.kidslauncher.ui.adapters.AppsAdapter;
import kidslauncher.alex.softteco.com.kidslauncher.ui.models.AppItemModel;

public class SelectAppsActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<List<AppItemModel>> {

    public static final String KEY_APPS = "KEY_APPS";

    private AppsAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_apps);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.rv);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        adapter = new AppsAdapter(new ArrayList<>(0), this);
        recyclerView.setAdapter(adapter);
        getLoaderManager().initLoader(0, null, this).forceLoad();
    }

    public void saveSelected(View view) {
        Intent intent = new Intent();
        final String packageName = getPackageName();
        ArrayList<AppItemModel> collect = Stream.of(adapter.getModels())
                .filter(value -> (value.isSelected() || packageName.equals(value.getPackageName())))
                .sorted((appItemModel, t1) -> appItemModel.getPackageName().equals(packageName) ? -1 : 0)
                .collect(Collectors.toCollection(ArrayList::new));
        intent.putParcelableArrayListExtra(KEY_APPS, collect);
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public Loader<List<AppItemModel>> onCreateLoader(int i, Bundle bundle) {
        return new AppInfoLoader(this);
    }

    @Override
    public void onLoadFinished(Loader<List<AppItemModel>> loader, List<AppItemModel> appItemModel) {
        adapter.setModels(appItemModel);
    }

    @Override
    public void onLoaderReset(Loader loader) {

    }

}
