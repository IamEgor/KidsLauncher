package kidslauncher.alex.com.kidslauncher.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatEditText;
import android.text.TextUtils;

import kidslauncher.alex.com.kidslauncher.R;
import kidslauncher.alex.com.kidslauncher.utils.PreferencesUtil;

public class LoginActivity extends AppCompatActivity {

    private AppCompatEditText mPassword;
    private AppCompatEditText mConfirmPassword;
    private TextInputLayout mPasswordInputLayout;
    private TextInputLayout mConfirmPasswordInputLayout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PreferencesUtil.getInstance().setBlockIncoming(false);
        PreferencesUtil.getInstance().setBlockWifi(false);
        if (PreferencesUtil.getInstance().isPasswordExist()) {
            Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
            startActivity(intent);
            finish();
            return;
        }
        setContentView(R.layout.login_activity);

        mPassword = (AppCompatEditText) findViewById(R.id.password);
        mConfirmPassword = (AppCompatEditText) findViewById(R.id.confirm_password);
        mPasswordInputLayout = (TextInputLayout) findViewById(R.id.password_input_layout);
        mConfirmPasswordInputLayout = (TextInputLayout) findViewById(R.id.confirm_password_input_layout);

        findViewById(R.id.confirm_password_btn).setOnClickListener(view -> {
            mPasswordInputLayout.setErrorEnabled(false);
            mConfirmPasswordInputLayout.setErrorEnabled(false);
            final String passwordText = mPassword.getText().toString();
            final String confirmPasswordText = mConfirmPassword.getText().toString();
            if (TextUtils.isEmpty(passwordText)) {
                mPasswordInputLayout.setError("Should not be empty");
            } else if (TextUtils.isEmpty(confirmPasswordText)) {
                mConfirmPasswordInputLayout.setError("Should not be empty");
            } else if (!passwordText.equals(confirmPasswordText)) {
                mConfirmPasswordInputLayout.setError("Passwords should match");
            } else {
                PreferencesUtil.getInstance().setPassword(passwordText);
                startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                finish();
            }
        });
    }

}
