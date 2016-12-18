package io.github.whataa.alarm;

import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import io.github.whataa.alarm.common.BaseRealmActivity;
import io.github.whataa.alarm.entity.Action;
import io.github.whataa.alarm.entity.Schedule;
import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;


public class MainActivity extends BaseRealmActivity implements RealmChangeListener<RealmResults<Schedule>> {

    private static final String TAG = MainActivity.class.getSimpleName();
    private RealmResults<Schedule> schedules;

    private RecyclerView.Adapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        schedules = getDefaultRealm().where(Schedule.class).findAll();
        schedules.addChangeListener(this);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter = new RecyclerView.Adapter<VHolder>() {

            @Override
            public VHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                return new VHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recycler_main, parent, false));
            }

            @Override
            public void onBindViewHolder(VHolder holder, int position) {
                holder.tvName.setText(schedules.get(position).name);
                holder.tvDesc.setText("desc: " + schedules.get(position).description);
                holder.tvTime.setText("create time: " + schedules.get(position).hintTime);
                StringBuffer sb = new StringBuffer("actions:").append("\n");
                if (schedules.get(position).actions != null) {
                    for (Action action : schedules.get(position).actions) {
                        sb.append("\t").append(action.duration).append("\n");
                    }
                    holder.tvActions.setText(sb.toString());
                }
            }

            @Override
            public int getItemCount() {
                return schedules.size();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        schedules.removeChangeListeners();
    }

    // xml click
    public void onAddTaskClick(View view) {
        startActivityForResult(new Intent(this, TaskModifyActivity.class), 3);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.w(TAG, "onActivityResult: " + data);
        if (resultCode == RESULT_OK) {
            startService(data.setClass(this, TaskService.class));
        }
    }

    @Override
    public void onChange(RealmResults<Schedule> element) {
        Log.w(TAG, "onChange: " + element
                + "\n" + schedules.size()
                + "\n" + (Thread.currentThread() == Looper.getMainLooper().getThread()));
        adapter.notifyDataSetChanged();
    }

    public void onDelTaskClick(View view) {
        getDefaultRealm().executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.deleteAll();
            }
        });
    }

    class VHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvDesc, tvTime, tvActions;

        VHolder(View itemView) {
            super(itemView);
            tvName = (TextView) itemView.findViewById(R.id.alarm_name);
            tvDesc = (TextView) itemView.findViewById(R.id.alarm_desc);
            tvTime = (TextView) itemView.findViewById(R.id.alarm_time);
            tvActions = (TextView) itemView.findViewById(R.id.alarm_actions);
        }
    }
}

