package io.github.whataa.alarm.db;

import android.content.Context;

import java.io.File;

import io.realm.Realm;
import io.realm.RealmAsyncTask;
import io.realm.RealmConfiguration;
import io.realm.RealmMigration;
import io.realm.RealmObject;
import io.realm.RealmResults;

public class RealmHelper {


    static abstract class Schemas<T extends Realm.Transaction, M extends RealmMigration> {
        public abstract String name();

        public abstract int version();

        public abstract T initialData();

        public abstract M migration();

        protected File directory() {
            return null;
        }

        protected byte[] encryptionKey() {
            return null;
        }

    }


    public static void init(Context context) {
        Realm.init(context);
        Realm.setDefaultConfiguration(RealmHelper.getConfig(new DefaultSchema()));
    }

    public static RealmConfiguration getConfig(Schemas schema) {
        RealmConfiguration.Builder builder = new RealmConfiguration.Builder()
                .name(schema.name())
                .schemaVersion(schema.version())
                .modules(schema)
                .initialData(schema.initialData())
                .deleteRealmIfMigrationNeeded() // 忽略旧文件以重新生成，仅当测试的时候开启
                .migration(schema.migration());
        if (schema.directory() != null) {
            if (!schema.directory().exists()) {
                schema.directory().mkdirs();
            }
            builder.directory(schema.directory());
        }
        if (schema.encryptionKey() != null) {
            builder.encryptionKey(schema.encryptionKey());
        }
        return builder.build();
    }

    /**
     * <p>异步事务调用会返回一个 RealmAsyncTask 对象。
     * 当你退出 Activity 或者 Fragment 时可以使用该对象取消异步事务。
     * 如果你在回调函数中更新 UI，那么忘记取消异步事务可能会造成你的应用崩溃。</p>
     *
     * @param tasks
     */
    public static void releaseTask(RealmAsyncTask... tasks) {
        for (RealmAsyncTask task : tasks) {
            if (task != null && !task.isCancelled()) {
                task.cancel();
            }
        }
    }

    public static void releaseListeners(RealmResults... results) {
        for (RealmResults result : results) {
            result.removeChangeListeners();
        }
    }

    public static void releaseListeners(RealmObject... objects) {
        for (RealmObject object : objects) {
            object.removeChangeListeners();
        }
    }

    /**
     * 不要试图在非Looper线程中调用
     * @param realm
     */
    public static void releaseListeners(Realm realm) {
        if (realm != null) {
            try {
                realm.removeAllChangeListeners();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}