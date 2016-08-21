package io.github.whataa.picer.executor;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.widget.ImageView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.io.File;

public class PicLoader {
    private static PicLoader loader;

    private PicLoader(Context context) {
        picasso = new Picasso.Builder(context).executor(new LowExecutors()).build();
    }

    private Picasso picasso;

    public static void initial(Context context) {
        if (loader == null) {
            synchronized (PicLoader.class) {
                if (loader == null) {
                    loader = new PicLoader(context.getApplicationContext());
                }
            }
        }
    }
    public static PicLoader instance() {
        check();
        return loader;
    }

    public void load(String filePath, ImageView target) {
        load(filePath, target, null);
    }

    public void load(String filePath, ImageView target, Callback callback) {
        Log.d(PicLoader.class.getSimpleName(), filePath);
        picasso.load(Uri.fromFile(new File(filePath))).centerCrop().resize(300,300).into(target, callback);
    }

    public static void check() {
        if (!isInitial()) throw new IllegalStateException("should call PicLoader#initial first");
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
