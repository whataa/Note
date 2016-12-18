package io.github.whataa.alarm.entity;

import java.io.Serializable;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * 一个计划
 */
public class Schedule extends RealmObject implements Serializable {
    @PrimaryKey
    public int id;
    // 提示时间
    public long hintTime;
    // 总开关
    public boolean isClosed;
    // 是否重复
    public boolean isRepeat;
    // 重复模式：周1~周日，true表示需要提醒，否则false
    public RepeatMode repeatMode;
    // 提醒主题
    public String name;
    // 提醒描述
    public String description;
    // 是否震动
    public boolean vibrate;
    // 是否有提示音
    public boolean sound;
    // 提示音文件路径，null则使用系统默认；
    public String soundPath;

    public RealmList<Action> actions;

    public Schedule() {

    }

    public Schedule(int id, long hintTime, String name, String description) {
        this.id = id;
        this.hintTime = hintTime;
        this.name = name;
        this.description = description;
    }
}
