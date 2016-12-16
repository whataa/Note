package io.github.whataa.alarm.utils;

import io.realm.DynamicRealm;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmMigration;
import io.realm.annotations.RealmModule;

import static io.github.whataa.alarm.utils.RealmHelper.SchemasConfig.SC1;

public class RealmHelper {

    /**
     * 数据库
     */
    public interface SchemasConfig {
        String SC1 = "";
    }

    public static final String NAME = "demoforall.realm";
    public static final int VERSION = 0;

    public static RealmConfiguration getConfig() {
        return new RealmConfiguration.Builder()
                .name(NAME)
                .schemaVersion(VERSION)
                .modules(new AppModule())
                .initialData(new InitTransaction())
//                .deleteRealmIfMigrationNeeded() // 忽略旧文件以重新生成，仅当测试的时候开启
                .migration(new UpdateMigration())
        .build();
    }

    @RealmModule(classes = {})
    static class AppModule {}

    /**
     * 仅在数据库文件 第一次被创建 或者 因升级需要清空并重新创建时 有效
     */
    static class InitTransaction implements Realm.Transaction {

        @Override
        public void execute(Realm realm) {
            switch (realm.getConfiguration().getRealmFileName()) {
                case SC1:
                    break;
            }
        }
    }

    /**
     * 每次数据库版本VERSION升级时执行
     */
    static class UpdateMigration implements RealmMigration {

        @Override
        public void migrate(DynamicRealm realm, long oldVersion, long newVersion) {

        }
    }
}