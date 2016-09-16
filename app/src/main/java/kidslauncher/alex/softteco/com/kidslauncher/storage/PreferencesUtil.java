package kidslauncher.alex.softteco.com.kidslauncher.storage;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import kidslauncher.alex.softteco.com.kidslauncher.KidsLauncherApp;
import kidslauncher.alex.softteco.com.kidslauncher.ui.models.AppItemModel;

import static android.content.Context.MODE_PRIVATE;

public class PreferencesUtil {

    public static final String PREFS_NAME = "kidslauncher_prefs";
    public static final String KEY_SELECTED_APPS = "KEY_SELECTED_APPS";

    private static volatile PreferencesUtil instance;

    private final SharedPreferences settings;
    private final Gson gson;

    private PreferencesUtil(final String prefName, final Context context) {
        settings = context.getSharedPreferences(prefName, MODE_PRIVATE);
        gson = new Gson();
    }

    public static PreferencesUtil getInstance() {
        return PreferencesUtil.getInstance(KidsLauncherApp.getContext());
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

    public void setString(String key, String value) {
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public String getString(String key, String defaultValue) {
        return settings.getString(key, defaultValue);
    }

    public void setSelectedApps(List<AppItemModel> selectedApps) {
        setObject(KEY_SELECTED_APPS, selectedApps);
    }

    public List<AppItemModel> getSelectedApps() {
        return (ArrayList<AppItemModel>) getObject(KEY_SELECTED_APPS, ArrayList.class);
    }

    private void setObject(String key, Object obj) {
        setString(key, gson.toJson(obj));
    }

    private <T> T getObject(String key, Class<T> classOfT) {
        return gson.fromJson(getString(key, null), classOfT);
    }

    public void drop(String key) {
        settings.edit().remove(key).commit();
    }

    public void dropAll() {
        SharedPreferences.Editor editor = settings.edit();
        editor.clear();
        editor.commit();
    }

}