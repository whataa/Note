package io.github.whataa.alarm.db;

import io.github.whataa.alarm.entity.Action;
import io.github.whataa.alarm.entity.RepeatMode;
import io.github.whataa.alarm.entity.Schedule;
import io.realm.DynamicRealm;
import io.realm.Realm;
import io.realm.RealmMigration;
import io.realm.annotations.RealmModule;

/**
 * 代表一个数据库配置<br>
 *     使用@RealmModule来指定该数据库拥有的表
 */

@RealmModule(classes = {
        Action.class, RepeatMode.class, Schedule.class
})
public class DefaultSchema extends RealmHelper.Schemas
        <DefaultSchema.InitTransaction, DefaultSchema.UpdateMigration> {
    @Override
    public String name() {
        return "defaults.realm";
    }

    @Override
    public int version() {
        return 0;
    }

    @Override
    public InitTransaction initialData() {
        return new InitTransaction();
    }

    @Override
    public UpdateMigration migration() {
        return new UpdateMigration();
    }


    /**
     * 仅在数据库文件 第一次被创建 或者 因升级需要清空并重新创建时 有效
     */
    class InitTransaction implements Realm.Transaction {

        @Override
        public void execute(Realm realm) {
        }
    }

    /**
     * 每次数据库版本VERSION升级时执行
     */
    class UpdateMigration implements RealmMigration {

        @Override
        public void migrate(DynamicRealm realm, long oldVersion, long newVersion) {

        }
    }
}
