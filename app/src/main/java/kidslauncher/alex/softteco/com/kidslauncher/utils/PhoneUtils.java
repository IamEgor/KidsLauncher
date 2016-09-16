package kidslauncher.alex.softteco.com.kidslauncher.utils;

import android.telephony.TelephonyManager;

import com.android.internal.telephony.ITelephony;

import java.lang.reflect.Method;

public class PhoneUtils {

    public static ITelephony getITelephony(TelephonyManager telephony) throws Exception {
        Method getITelephonyMethod = telephony.getClass().getDeclaredMethod("getITelephony");
        getITelephonyMethod.setAccessible(true);//Privatization functions can also be used
        return (ITelephony) getITelephonyMethod.invoke(telephony);
    }

}
