package com.example.alarm_app;

public class Task {

    public static final int NO_ID = -1;

    // 所属alarm组
    public int groupId;
    public int id;
    public int preId = NO_ID;
    public int nextId = NO_ID;
    // 任务持续时间
    public long duration;
    // 任务描述
    public String description;
    // 任务关闭状态
    public boolean isClosed;
}
