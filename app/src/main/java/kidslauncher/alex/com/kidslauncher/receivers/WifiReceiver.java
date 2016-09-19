package kidslauncher.alex.com.kidslauncher.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import kidslauncher.alex.com.kidslauncher.utils.PreferencesUtil;
import kidslauncher.alex.com.kidslauncher.utils.CommonUtils;

public class WifiReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        ConnectivityManager conMan = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = conMan.getActiveNetworkInfo();
        if (netInfo != null && netInfo.getType() == ConnectivityManager.TYPE_WIFI) {
            if (PreferencesUtil.getInstance().isBlockWifi()) {
                CommonUtils.disableWifi();
            }
        }
    }
};