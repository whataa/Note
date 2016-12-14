package io.github.whataa.alarm.entity;

import java.io.Serializable;

/**
 * 动作
 */
public class Action implements Serializable, Comparable<Action> {

    // 所属alarm组
    public int groupId;
    // 取值范围限定在同一个组中；同时代表着所在组中的执行顺序
    public int id;
    // 任务持续时间
    public long duration;
    // 任务描述
    public String description;
    // 任务关闭状态
    public boolean isClosed;

    public Action(int groupId, int id, long duration, String description) {
        this.groupId = groupId;
        this.id = id;
        this.duration = duration;
        this.description = description;
    }

    public void switchExecOrder(Action action) {
        if (action == null || id == action.id) return;
        int tmpId = action.id;
        action.id = this.id;
        this.id = tmpId;
    }

    @Override
    public int compareTo(Action o) {
        return this.id - o.id;
    }
}
