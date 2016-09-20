package kidslauncher.alex.com.kidslauncher.ui.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;

import kidslauncher.alex.com.kidslauncher.R;

public class ExitDialog extends DialogFragment {

    private EditText mEditText;

    public ExitDialog() {
        // Empty constructor is required for DialogFragment
        // Make sure not to add arguments to the constructor
        // Use `newInstance` instead as shown below
    }

    public static ExitDialog newInstance(String title) {
        ExitDialog frag = new ExitDialog();
        Bundle args = new Bundle();
        args.putString("title", title);
        frag.setArguments(args);
        return frag;
    }

//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
////        getDialog().setCanceledOnTouchOutside(false);
//        return inflater.inflate(R.layout.dialog_fragment_exit, container);
//    }

//    @Override
//    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
//        super.onViewCreated(view, savedInstanceState);
//        // Get field from view
//        mEditText = (EditText) view.findViewById(R.id.txt_your_name);
//        // Fetch arguments from bundle and set title
//        String title = getArguments().getString("title", "Enter Name");
//        getDialog().setTitle(title);
//        // Show soft keyboard automatically and request focus to field
//        mEditText.requestFocus();
//        getDialog().getWindow().setSoftInputMode(
//                WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
//    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        String title = getArguments().getString("title");

        ExitDialogListener dialogListener = (ExitDialogListener) getActivity();

        return new AlertDialog.Builder(getActivity())
                .setIcon(R.drawable.ic_power_settings_new_black_24dp)
                .setTitle(title)
                .setCancelable(false)
                .setPositiveButton("onContinue", (dialog, whichButton) -> {
                            dialogListener.onContinue();
                        }
                )
                .setNegativeButton("onExit", (dialog, whichButton) -> {
                            dialogListener.onExit();
                        }
                )
                .create();
    }

    public interface ExitDialogListener {
        void onExit();

        void onContinue();
    }

}
