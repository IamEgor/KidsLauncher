package kidslauncher.alex.com.kidslauncher.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import kidslauncher.alex.com.kidslauncher.R;

public class FullscreenSplashActivity extends AppCompatActivity {

    private static final int SPLASH_SCREEN_DELAY = 2500;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fullscreen);
        new Handler().postDelayed(() -> startActivity(new Intent(this, HomeActivity.class)), SPLASH_SCREEN_DELAY);
    }

}