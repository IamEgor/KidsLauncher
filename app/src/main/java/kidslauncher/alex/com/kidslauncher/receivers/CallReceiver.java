package kidslauncher.alex.com.kidslauncher.receivers;

import android.content.Context;
import android.util.Log;

import java.util.Date;

import kidslauncher.alex.com.kidslauncher.utils.PreferencesUtil;
import kidslauncher.alex.com.kidslauncher.utils.CommonUtils;

public class CallReceiver extends AbstractPhoneCallReceiver {

    private static final String TAG = "[" + CallReceiver.class.getSimpleName() + "]";

    @Override
    protected void onIncomingCallReceived(Context ctx, String number, Date start) {
        Log.w(TAG, "onIncomingCallReceived");
        if (PreferencesUtil.getInstance().isBlockIncoming()) {
            CommonUtils.blockIncoming();
        }
    }

    @Override
    protected void onIncomingCallAnswered(Context ctx, String number, Date start) {
        Log.w(TAG, "onIncomingCallAnswered");
    }

    @Override
    protected void onIncomingCallEnded(Context ctx, String number, Date start, Date end) {
        Log.w(TAG, "onIncomingCallEnded");
    }

    @Override
    protected void onOutgoingCallStarted(Context ctx, String number, Date start) {
        Log.w(TAG, "onOutgoingCallStarted");
    }

    @Override
    protected void onOutgoingCallEnded(Context ctx, String number, Date start, Date end) {
        Log.w(TAG, "onOutgoingCallEnded");
    }

    @Override
    protected void onMissedCall(Context ctx, String number, Date start) {
        Log.w(TAG, "onMissedCall");
    }

}