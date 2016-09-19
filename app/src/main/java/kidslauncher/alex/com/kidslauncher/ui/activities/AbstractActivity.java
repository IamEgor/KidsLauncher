package kidslauncher.alex.com.kidslauncher.ui.activities;

import android.app.AlertDialog;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.widget.EditText;

import kidslauncher.alex.com.kidslauncher.utils.PreferencesUtil;

public abstract class AbstractActivity extends AppCompatActivity {

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

    public interface PositiveAction {
        void act();
    }

}
