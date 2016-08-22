package io.github.whataa.picer;


import android.content.Context;
import android.text.TextUtils;
import android.view.MotionEvent;

import java.io.File;

public class Utils {
    public static boolean fileExist(String path) {
        return !TextUtils.isEmpty(path) && new File(path).exists();
    }

    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    public static String getTouchAction(int actionId) {
        String actionName = null;
        switch (actionId) {
            case MotionEvent.ACTION_DOWN:
                actionName = "ACTION_DOWN";
                break;
            case MotionEvent.ACTION_MOVE:
                actionName = "ACTION_MOVE";
                break;
            case MotionEvent.ACTION_UP:
                actionName = "ACTION_UP";
                break;
            case MotionEvent.ACTION_CANCEL:
                actionName = "ACTION_CANCEL";
                break;
            case MotionEvent.ACTION_OUTSIDE:
                actionName = "ACTION_OUTSIDE";
                break;
            default:
                actionName = "onKnow action";
        }
        return actionName;
    }
}
