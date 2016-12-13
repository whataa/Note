package com.example.alarm_app;

public class Alarm {
    public int id;
    // 提示时间
    public long hintTime;
    // 总开关
    public boolean isClosed;
    // 是否重复
    public boolean isRepeat;
    // 重复模式：周1~周日，true表示需要提醒，否则false
    public boolean[] repeatMode = new boolean[7];
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
}
