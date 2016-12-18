package io.github.whataa.alarm;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutCompat;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import java.util.Random;

import io.github.whataa.alarm.common.BaseRealmActivity;
import io.github.whataa.alarm.entity.Action;
import io.github.whataa.alarm.entity.Schedule;
import io.realm.Realm;
import io.realm.RealmList;


/**
 * Created by Administrator on 2016/12/14.
 */

public class TaskModifyActivity extends BaseRealmActivity {
    private static final String TAG = TaskModifyActivity.class.getSimpleName();

    LinearLayout actionGroup;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_modify);
        actionGroup = (LinearLayout) findViewById(R.id.action_group);
    }

    public void onSubTaskClick(View view) {
        Log.w(TAG, "onSubTaskClick: ");
        int scheduleId = new Random().nextInt();
        String title = ((EditText)findViewById(R.id.edit_theme)).getText().toString();
        String desc = ((EditText)findViewById(R.id.edit_desc)).getText().toString();
        final Schedule schedule = new Schedule(scheduleId, System.currentTimeMillis(),
                !"".equals(title)?title : "计划"+scheduleId, !"".equals(desc)?desc : "a great schedule.");
        schedule.actions = new RealmList<>();
        for (int i = 0; i < actionGroup.getChildCount(); i++) {
            EditText editText = (EditText) actionGroup.getChildAt(i);
            String time = editText.getText().toString();
            if (time.equals("")) continue;
            schedule.actions.add(new Action(new Random().nextInt(), schedule.id, i, Long.parseLong(time), "action comes"+i));
        }
        getDefaultRealm().executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.copyToRealm(schedule);
            }
        });

        setResult(RESULT_OK, new Intent().putExtra("DATA", schedule.id));
        finish();
    }

    public void onAddTaskClick(View view) {
        EditText editText = new EditText(this);
        editText.setInputType(InputType.TYPE_CLASS_NUMBER);
        editText.setHint("输入持续时间，单位ms");
        editText.setLines(1);
        editText.setMaxEms(8);
        LinearLayoutCompat.LayoutParams params = new LinearLayoutCompat.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        actionGroup.addView(editText, params);
    }
}
