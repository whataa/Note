package io.github.whataa.picer;


import android.content.Context;
import android.text.TextUtils;

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
}
