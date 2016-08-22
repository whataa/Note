package io.github.whataa.picer.executor;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.widget.ImageView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.MemoryPolicy;
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

    public void loadPreview(String filePath, ImageView target) {
        picasso.load(Uri.fromFile(new File(filePath)))
                .memoryPolicy(MemoryPolicy.NO_CACHE,MemoryPolicy.NO_STORE)
                .centerCrop()
                .fit()
                .into(target);
    }

    public void load(String filePath, ImageView target) {
        load(filePath, target, null);
    }

    public void load(String filePath, ImageView target, Callback callback) {
        picasso.load(Uri.fromFile(new File(filePath)))
                .resize(200,200)
                .centerCrop()
                .config(Bitmap.Config.RGB_565)
                .into(target, callback);
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
