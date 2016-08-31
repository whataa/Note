package whataa.github.com.matrixer;

import android.util.Log;

/**
 * Created by Administrator on 2016/8/31.
 */
public class Utils {
    public static final boolean DEBUG = true;
    public static void L(String tag, String msg) {
        if (DEBUG) {
            Log.e(tag,msg);
        }
    }
}
