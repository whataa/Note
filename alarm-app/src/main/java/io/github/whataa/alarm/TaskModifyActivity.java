package io.github.whataa.alarm;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import io.github.whataa.alarm.entity.Action;
import io.github.whataa.alarm.entity.Schedule;
import io.github.whataa.alarm.entity.Task;


/**
 * Created by Administrator on 2016/12/14.
 */

public class TaskModifyActivity extends AppCompatActivity {
    private static final String TAG = TaskModifyActivity.class.getSimpleName();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_modify);
    }

    public void onSubTaskClick(View view) {
        Log.w(TAG, "onSubTaskClick: ");
        Schedule schedule = new Schedule(1, System.currentTimeMillis() + 3000, "计划1", "这是描述内容");
        Action action0 = new Action(schedule.id, 0, 3000, "action1 comes");
        Action action1 = new Action(schedule.id, 1, 4000, "action2 comes");
        Action action2 = new Action(schedule.id, 2, 5000, "action3 comes");
        List<Action> actions = new ArrayList<>();
        actions.add(action0);
        actions.add(action1);
        actions.add(action2);
        Task task = new Task(schedule, actions);
        setResult(RESULT_OK, new Intent().putExtra("DATA", task));
        finish();
    }
}
