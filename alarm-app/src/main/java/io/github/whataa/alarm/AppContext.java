package io.github.whataa.alarm;

import android.app.Application;

import io.github.whataa.alarm.utils.RealmHelper;
import io.realm.Realm;

/**
 * Created by Administrator on 2016/12/14.
 */

public class AppContext extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Realm.init(this);
        Realm.setDefaultConfiguration(RealmHelper.getConfig());
    }
}
