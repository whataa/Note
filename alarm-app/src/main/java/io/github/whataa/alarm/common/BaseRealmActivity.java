package io.github.whataa.alarm.common;

import android.os.Bundle;
import android.support.annotation.Nullable;

import io.realm.Realm;

/**
 * Created by Summer on 2016/12/18.
 */

public class BaseRealmActivity extends BaseActivity {

    public final Realm getDefaultRealm() {
        return defaultRealm;
    }

    private Realm defaultRealm;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        defaultRealm = Realm.getDefaultInstance();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        defaultRealm.close();
    }
}
