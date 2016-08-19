package io.github.whataa.picer.executor;

import android.content.Context;
import android.net.Uri;
import android.widget.ImageView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

public class PicLoader {
    private static PicLoader loader;

    private PicLoader(Context context) {
        picasso = new Picasso.Builder(context).executor(new LowExecutors()).build();
    }

    private Picasso picasso;

    public static PicLoader instance(Context context) {
        if (loader == null) {
            synchronized (PicLoader.class) {
                if (loader == null) {
                    new PicLoader(context.getApplicationContext());
                }
            }
        }
        return loader;
    }

    public void load(String filePath, ImageView target) {
        load(filePath, target, null);
    }

    public void load(String filePath, ImageView target, Callback callback) {
        picasso.load(Uri.parse(filePath)).into(target, callback);
    }


    public static boolean isInitial() {
        return loader != null;
    }
    public void shutDown() {
        if (picasso != null)
            picasso.shutdown();
        loader = null;
    }
}
