package kidslauncher.alex.softteco.com.kidslauncher.utils;

import android.content.Context;
import android.net.wifi.WifiManager;
import android.os.RemoteException;
import android.telephony.TelephonyManager;

import com.android.internal.telephony.ITelephony;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import kidslauncher.alex.softteco.com.kidslauncher.KidsLauncherApp;

/**
 * Created by yegor on 16.09.16.
 */
public class Utils {


    public void disableWifi() {
        WifiManager wifiManager = (WifiManager) KidsLauncherApp.getContext().getSystemService(Context.WIFI_SERVICE);
        wifiManager.setWifiEnabled(!wifiManager.isWifiEnabled());
//        Toast.makeText(HomeActivity.this, "diableWifi", Toast.LENGTH_SHORT).show();
    }

    public void blockIncoming() {
        try {
            TelephonyManager telephonyManager = (TelephonyManager) KidsLauncherApp.getContext().getSystemService(Context.TELEPHONY_SERVICE);
            Class clazz = Class.forName(telephonyManager.getClass().getName());
            Method method = clazz.getDeclaredMethod("getITelephony");
            method.setAccessible(true);
            ITelephony telephonyService = (ITelephony) method.invoke(telephonyManager);
            telephonyService.endCall();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
//        Toast.makeText(HomeActivity.this, "blockIncoming", Toast.LENGTH_SHORT).show();
    }

}
