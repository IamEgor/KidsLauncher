package kidslauncher.alex.com.kidslauncher.utils;

import java.util.ArrayList;
import java.util.List;

public class Constants {

    public static final List<String> PERMISSIONS = new ArrayList<String>() {{
        add("android.permission.CAMERA");
        add("android.permission.READ_CONTACTS");
        add("android.permission.WRITE_CONTACTS");
        add("android.permission.GET_ACCOUNTS");
        add("android.permission.READ_EXTERNAL_STORAGE");
        add("android.permission.WRITE_EXTERNAL_STORAGE");
    }};

}
