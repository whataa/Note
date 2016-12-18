package io.github.whataa.alarm.entity;

import java.io.Serializable;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * 动作
 */
public class Action extends RealmObject implements Serializable, Comparable<Action> {

    @PrimaryKey
    public int id;
    // 所属alarm组
    public int groupId;
    // 取值范围限定在同一个组中；同时代表着所在组中的执行顺序
    public int order;
    // 任务持续时间
    public long duration;
    // 任务描述
    public String description;
    // 任务关闭状态
    public boolean isClosed;

    public Action() {

    }

    public Action(int id, int groupId, int order, long duration, String description) {
        this.id = id;
        this.groupId = groupId;
        this.order = order;
        this.duration = duration;
        this.description = description;
    }

    public void switchExecOrder(Action action) {
        if (action == null || order == action.order) return;
        int tmpId = action.order;
        action.order = this.order;
        this.order = tmpId;
    }

    @Override
    public int compareTo(Action o) {
        return this.order - o.order;
    }
}
