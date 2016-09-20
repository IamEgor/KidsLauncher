package kidslauncher.alex.com.kidslauncher.ui.activities;

import android.app.AlertDialog;
import android.app.FragmentManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.Locale;
import java.util.TimerTask;

import kidslauncher.alex.com.kidslauncher.R;
import kidslauncher.alex.com.kidslauncher.ui.fragments.ExitDialog;
import kidslauncher.alex.com.kidslauncher.utils.LockTimer;
import kidslauncher.alex.com.kidslauncher.utils.PreferencesUtil;

public abstract class AbstractActivity extends AppCompatActivity implements
        ExitDialog.ExitDialogListener,
        LockTimer.TimerCallbacks {
//        SharedPreferences.OnSharedPreferenceChangeListener {

    public static final String ALARM_ACTION = "kidslauncher.alex.com.kidslauncher.ui.activities.AbstractActivity";

    protected ImageView mLeftButton;
    protected ImageView mRightButton;
    protected ImageView mRightButtonAdditional;
    protected ImageView mExitButton;

    private View mTimeIsOverView;
    private AppCompatButton mContinue;
    private AppCompatButton mEnd;
    private FrameLayout contentContainer;
    private View activityContent;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_abstract);

        mLeftButton = (ImageView) findViewById(R.id.left_toolbar_btn);
        mRightButton = (ImageView) findViewById(R.id.right_toolbar_btn);
        mRightButtonAdditional = (ImageView) findViewById(R.id.right_toolbar_btn_additional);
        mExitButton = (ImageView) findViewById(R.id.exit_toolbar_btn);

        mTimeIsOverView = findViewById(R.id.time_is_over_layout);
        mContinue = (AppCompatButton) findViewById(R.id.continue_btn);
        mEnd = (AppCompatButton) findViewById(R.id.exit_btn);
        contentContainer = (FrameLayout) findViewById(R.id.activity_content_container);

        activityContent = getLayoutInflater().inflate(setContentViewResource(), contentContainer, false);
        contentContainer.addView(activityContent);

        mExitButton.setOnClickListener(view -> actAfterPasswordAccepted(this::finish));
    }

    @Override
    protected void onResume() {
        super.onResume();
//        PreferencesUtil.getInstance().getPreferences().registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
//        PreferencesUtil.getInstance().getPreferences().unregisterOnSharedPreferenceChangeListener(this);
    }

    protected void setTimer(long delay) {
        Log.w("LockTimer","setTimer");
        new LockTimer(delay, 1000, this).start();
    }

    protected void actAfterPasswordAccepted(@NonNull PositiveAction action) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Enter password");
        final EditText input = new EditText(this);
        final TextInputLayout inputLayout = new TextInputLayout(this);
        inputLayout.addView(input);
        input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        builder.setView(inputLayout);
        builder.setPositiveButton("OK", (dialog, which) -> {
        });
        builder.setNegativeButton("Cancel", (dialog, which) -> {
            dialog.cancel();
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(v -> {
            if (PreferencesUtil.getInstance().isMatchingPassword(input.getText().toString())) {
                alertDialog.dismiss();
                action.act();
            } else {
                inputLayout.setError("Wrong password");
            }
        });
    }

    protected void showExitDialog() {
        FragmentManager fm = getFragmentManager();
        ExitDialog exitDialog = ExitDialog.newInstance("Some Title");
        exitDialog.show(fm, "fragment_edit_name");
    }

    @Override
    public void onContinue() {
        Toast.makeText(AbstractActivity.this, "onContinue", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onExit() {
        Toast.makeText(AbstractActivity.this, "onExit", Toast.LENGTH_SHORT).show();

    }

    @LayoutRes
    abstract int setContentViewResource();

    @Override
    public void onTimerTick(long millisUntilFinished) {

    }

    @Override
    public void onTimerFinish() {
        Log.w("LockTimer","onTimerFinish");
        activityContent.setVisibility(View.GONE);
        mTimeIsOverView.setVisibility(View.VISIBLE);
    }

    public interface PositiveAction {
        void act();
    }

}
