package kidslauncher.alex.com.kidslauncher.ui.activities;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import java.util.HashMap;

import kidslauncher.alex.com.kidslauncher.R;
import kidslauncher.alex.com.kidslauncher.utils.PreferencesUtil;

public class SettingsActivity extends PreferenceActivity {

    public static final String KEY_PREFERENCES = "KEY_PREFERENCES";

    private ListPreference mListPreference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PreferenceManager manager = getPreferenceManager();
        manager.setSharedPreferencesName(PreferencesUtil.PREFS_NAME);
        addPreferencesFromResource(R.xml.preference_activity);
        Preference myPref = findPreference(getString(R.string.reset_selected_apps));
        mListPreference = (ListPreference) findPreference(getString(R.string.timer_interval_pref));
        myPref.setOnPreferenceClickListener(preference -> {
            AlertDialog.Builder alert = new AlertDialog.Builder(SettingsActivity.this);
            alert.setTitle(getString(R.string.clear_selected_apps_title));
            alert.setMessage(getString(R.string.clear_selected_apps_message));
            alert.setNegativeButton("Cancel", (dialog, whichButton) -> {
            });
            alert.setPositiveButton("OK", (dialogInterface, i) -> {
                PreferencesUtil.getInstance().dropSelectedAppsList();
                PreferencesUtil.getInstance().setBlockIncoming(false);
                PreferencesUtil.getInstance().setBlockWifi(false);
                finish();
            });
            alert.show();
            return true;
        });
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        LinearLayout root = (LinearLayout) findViewById(android.R.id.list).getParent().getParent().getParent();
        Toolbar bar = (Toolbar) LayoutInflater.from(this).inflate(R.layout.item_simple_toolbar, root, false);
        root.addView(bar, 0); // insert at top
        bar.setNavigationOnClickListener(v -> onFinish());
    }

    private void onFinish() {
        Intent intent = new Intent();
        intent.putExtra(KEY_PREFERENCES, new HashMap<>(PreferencesUtil.getInstance().getPreferences().getAll()));
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        onFinish();
    }
}