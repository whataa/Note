package io.github.whataa.alarm.entity;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Summer on 2016/12/17.
 */

public class RepeatMode extends RealmObject {
    @PrimaryKey
    public int id;
    public int scheduleId;

    public boolean monday;
    public boolean tuesday ;
    public boolean wednesday ;
    public boolean thursday ;
    public boolean friday ;
    public boolean saturday ;
    public boolean sunday;
}
