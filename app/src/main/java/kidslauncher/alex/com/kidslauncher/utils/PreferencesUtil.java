package kidslauncher.alex.com.kidslauncher.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import kidslauncher.alex.com.kidslauncher.KidsLauncherApp;
import kidslauncher.alex.com.kidslauncher.R;
import kidslauncher.alex.com.kidslauncher.ui.models.AppItemModel;

import static android.content.Context.MODE_PRIVATE;

public class PreferencesUtil {

    public static final String PREFS_NAME = "kidslauncher_prefs";
    ;
    private static final String KEY_SELECTED_APPS = "KEY_SELECTED_APPS";
    private static final String KEY_APP_PASSWORD = "KEY_APP_PASSWORD";

    private static final String KEY_BLOCK_INCOMING = KidsLauncherApp.getInstance().getString(R.string.block_incoming_calls_pref);
    private static final String KEY_DISABLE_WIFI = KidsLauncherApp.getInstance().getString(R.string.disable_wifi_pref);
    private static final String KEY_BOOT_ON_START = KidsLauncherApp.getInstance().getString(R.string.start_on_boot_pref);
    ;

    private static volatile PreferencesUtil instance;

    private final SharedPreferences settings;
    private final Gson gson;
    private Object timerInterval;

    private PreferencesUtil(final String prefName, final Context context) {
        settings = context.getSharedPreferences(prefName, MODE_PRIVATE);//
        gson = new Gson();
    }

    public static PreferencesUtil getInstance() {
        return PreferencesUtil.getInstance(KidsLauncherApp.getInstance());
    }

    public static PreferencesUtil getInstance(Context context) {
        PreferencesUtil localInstance = instance;
        if (localInstance == null) {
            synchronized (PreferencesUtil.class) {
                localInstance = instance;
                if (localInstance == null) {
                    instance = localInstance = new PreferencesUtil(PREFS_NAME, context);
                }
            }
        }
        return localInstance;
    }

    public SharedPreferences getPreferences() {
        return settings;
    }

    public boolean isPasswordExist() {
        return !TextUtils.isEmpty(getString(KEY_APP_PASSWORD, null));
    }

    public void setPassword(String password) {
        try {
            setString(KEY_APP_PASSWORD, CommonUtils.SHA1(password));
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    public boolean isMatchingPassword(String passwordToCheck) {
        try {
            return TextUtils.equals(getString(KEY_APP_PASSWORD, ""), CommonUtils.SHA1(passwordToCheck));
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void setSelectedApps(List<AppItemModel> selectedApps) {
        Type listOfTestObject = new TypeToken<ArrayList<AppItemModel>>() {
        }.getType();
        setObject(KEY_SELECTED_APPS, selectedApps, listOfTestObject);
    }

    public List<AppItemModel> getSelectedApps() {
        Type listOfTestObject = new TypeToken<ArrayList<AppItemModel>>() {
        }.getType();
        return getObject(KEY_SELECTED_APPS, listOfTestObject);
    }

    public int getTimerInterval() {
        return settings.getInt(KidsLauncherApp.getInstance().getString(R.string.timer_interval_time),
                Integer.parseInt(KidsLauncherApp.getInstance().getResources().getString(R.string.default_timer_value)));
    }

    public void dropSelectedAppsList() {
        drop(KEY_SELECTED_APPS);
    }

    private void setObject(String key, Object obj, Type type) {
        setString(key, gson.toJson(obj, type));
    }

    private <T> T getObject(String key, Type listOfTestObject) {
        return gson.fromJson(getString(key, null), listOfTestObject);
    }

    public void setString(String key, String value) {
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public String getString(String key, String defaultValue) {
        return settings.getString(key, defaultValue);
    }

    public void drop(String key) {
        settings.edit().remove(key).commit();
    }

    public void dropAll() {
        SharedPreferences.Editor editor = settings.edit();
        editor.clear();
        editor.commit();
    }

    public boolean isBlockIncoming() {
        return getBoolean(KEY_BLOCK_INCOMING);
    }

    public void setBlockIncoming(boolean block) {
        setBoolean(KEY_BLOCK_INCOMING, block);
    }

    public boolean isBlockWifi() {
        return getBoolean(KEY_DISABLE_WIFI);
    }

    public void setBlockWifi(boolean block) {
        setBoolean(KEY_DISABLE_WIFI, block);
    }

    public boolean isBootOnStart() {
        return getBoolean(KEY_BOOT_ON_START);
    }

    public void setBootOnStart(boolean block) {
        setBoolean(KEY_BOOT_ON_START, block);
    }

    public boolean getBoolean(String key) {
        return settings.getBoolean(key, false);
    }

    public void setBoolean(String key, boolean block) {
        SharedPreferences.Editor editor = settings.edit();
        editor.putBoolean(key, block);
        editor.apply();
    }

}