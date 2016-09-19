package kidslauncher.alex.com.kidslauncher.loaders;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;

import java.util.ArrayList;
import java.util.List;

import kidslauncher.alex.com.kidslauncher.ui.models.AppItemModel;

public class AppInfoLoader extends AsyncTaskLoader<List<AppItemModel>> {

    private Context mContext;

    public AppInfoLoader(Context context) {
        super(context);
        mContext = context;
    }

    @Override
    public List<AppItemModel> loadInBackground() {
        PackageManager manager = mContext.getPackageManager();
        Intent i = new Intent(Intent.ACTION_MAIN, null);
        i.addCategory(Intent.CATEGORY_LAUNCHER);
        List<ResolveInfo> availableActivities = manager.queryIntentActivities(i, 0);
        List<AppItemModel> appItemModels = new ArrayList<>(availableActivities.size());
        for (ResolveInfo ri : availableActivities) {
            appItemModels.add(new AppItemModel(ri.activityInfo.packageName, ri.loadLabel(manager).toString()));
        }
        return appItemModels;
    }

}
