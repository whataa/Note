package io.github.whataa.alarm.common;

import android.app.Application;

import io.github.whataa.alarm.db.RealmHelper;

/**
 * Created by Administrator on 2016/12/14.
 */

public class AppContext extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        RealmHelper.init(this);
    }
}
