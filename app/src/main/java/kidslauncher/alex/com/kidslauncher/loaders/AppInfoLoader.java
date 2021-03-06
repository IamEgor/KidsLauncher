package kidslauncher.alex.com.kidslauncher.loaders;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import kidslauncher.alex.com.kidslauncher.ui.models.AppItemModel;
import kidslauncher.alex.com.kidslauncher.utils.CommonUtils;

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
        i.addCategory(Intent.CATEGORY_LAUNCHER);//CATEGORY_LAUNCHER
        List<ResolveInfo> availableActivities = manager.queryIntentActivities(i, 0);
        List<AppItemModel> appItemModels = new ArrayList<>(availableActivities.size());
        for (ResolveInfo resolveInfo : availableActivities) {
            try {
                PackageInfo packageInfo = manager.getPackageInfo(resolveInfo.activityInfo.packageName, PackageManager.GET_PERMISSIONS);
                String packageName = packageInfo.packageName;
                String[] requestedPermissions = packageInfo.requestedPermissions;
                List<String> permissions = requestedPermissions == null ? new ArrayList<>(0) : Arrays.asList(requestedPermissions);
                if (CommonUtils.notContainsDangerousPermissions(permissions)){
                    appItemModels.add(new AppItemModel(packageName, resolveInfo.loadLabel(manager).toString(), permissions));
                }
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
        }
        return appItemModels;
    }

}
