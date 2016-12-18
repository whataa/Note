package io.github.whataa.alarm.entity;

import java.io.Serializable;
import java.util.List;

/**
 * 一个任务包括：排期计划 + 多个动作命令
 */
public class Task implements Serializable {

    /**
     * 下一个即将执行的动作，可手动指定
     */
    public int nextActionOrder;
    public Schedule schedule;
    public List<Action> actions;

    public Task(Schedule schedule, List<Action> actions) {
        this.schedule = schedule;
        this.actions = actions;
    }
}
