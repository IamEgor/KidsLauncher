package kidslauncher.alex.softteco.com.kidslauncher.receivers;

import android.content.Context;
import android.util.Log;

import java.util.Date;

public class CallReceiver extends PhonecallReceiver {

    @Override
    protected void onIncomingCallReceived(Context ctx, String number, Date start) {
        Log.w("CallReceiver", "onIncomingCallReceived");
    }

    @Override
    protected void onIncomingCallAnswered(Context ctx, String number, Date start) {
        Log.w("CallReceiver", "onIncomingCallAnswered");
    }

    @Override
    protected void onIncomingCallEnded(Context ctx, String number, Date start, Date end) {
        Log.w("CallReceiver", "onIncomingCallEnded");
    }

    @Override
    protected void onOutgoingCallStarted(Context ctx, String number, Date start) {
        Log.w("CallReceiver", "onOutgoingCallStarted");
    }

    @Override
    protected void onOutgoingCallEnded(Context ctx, String number, Date start, Date end) {
        Log.w("CallReceiver", "onOutgoingCallEnded");
    }

    @Override
    protected void onMissedCall(Context ctx, String number, Date start) {
        Log.w("CallReceiver", "onMissedCall");
    }

}