package kidslauncher.alex.com.kidslauncher.ui.activities;

import android.app.LoaderManager;
import android.content.Intent;
import android.content.Loader;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.annimon.stream.Collectors;
import com.annimon.stream.Stream;

import java.util.ArrayList;
import java.util.List;

import kidslauncher.alex.com.kidslauncher.R;
import kidslauncher.alex.com.kidslauncher.loaders.AppInfoLoader;
import kidslauncher.alex.com.kidslauncher.ui.adapters.AppsAdapter;
import kidslauncher.alex.com.kidslauncher.ui.models.AppItemModel;

public class SelectAppsActivity extends AppCompatActivity implements
        AppsAdapter.OnClickListener,
        LoaderManager.LoaderCallbacks<List<AppItemModel>> {

    public static final String KEY_APPS = "KEY_APPS";

    private ImageView mLeftButton;
    private ImageView mRightButton;
    private ImageView mRightButtonAdditional;
    private RecyclerView mRecyclerView;

    private AppsAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_apps);

        initViews();

        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        adapter = new AppsAdapter(new ArrayList<>(0), this, this);
        mRecyclerView.setAdapter(adapter);

        getLoaderManager().initLoader(0, null, this).forceLoad();
    }

    @Override
    public void onAppItemClick(int selectedCount) {
        setToolbarButtons(selectedCount != 0);
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

    private void initViews() {
        mLeftButton = (ImageView) findViewById(R.id.left_toolbar_btn);
        mRightButton = (ImageView) findViewById(R.id.right_toolbar_btn);
        mRightButtonAdditional = (ImageView) findViewById(R.id.right_toolbar_btn_additional);
        mRecyclerView = (RecyclerView) findViewById(R.id.rv);

        mLeftButton.setVisibility(View.VISIBLE);
        mLeftButton.setImageResource(R.drawable.ic_arrow_back_black_24dp);
        mRightButton.setVisibility(View.INVISIBLE);
        mRightButton.setImageResource(R.drawable.ic_close_black_24dp);

        mLeftButton.setOnClickListener(view -> finish());
        mRightButton.setOnClickListener(this::saveSelected);
    }

    public void saveSelected(View view) {
        Intent intent = new Intent();
        final String packageName = getPackageName();
        ArrayList<AppItemModel> collect = Stream.of(adapter.getModels())
                .filter(value -> (value.isSelected()))//|| packageName.equals(value.getPackageName())))
//                .sorted((appItemModel, t1) -> appItemModel.getPackageName().equals(packageName) ? -1 : 0)
                .collect(Collectors.toCollection(ArrayList::new));
        intent.putParcelableArrayListExtra(KEY_APPS, collect);
        setResult(RESULT_OK, intent);
        finish();
    }

    private void setToolbarButtons(boolean showConfirmButton) {
        if (showConfirmButton) {
            mLeftButton.setImageResource(R.drawable.ic_close_black_24dp);
            mRightButton.setVisibility(View.VISIBLE);
            mRightButton.setImageResource(R.drawable.ic_check_black_24dp);
        } else {
            mLeftButton.setImageResource(R.drawable.ic_arrow_back_black_24dp);
            mRightButton.setVisibility(View.INVISIBLE);
        }
    }

}
