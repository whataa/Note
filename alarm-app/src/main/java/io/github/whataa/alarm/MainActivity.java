package io.github.whataa.alarm;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import io.github.whataa.alarm.entity.Schedule;
import io.github.whataa.alarm.entity.Task;


public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private List<Schedule> schedules = new ArrayList<>();

    private RecyclerView.Adapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
                holder.tvDesc.setText(schedules.get(position).description);
                holder.tvTime.setText("" + schedules.get(position).hintTime);
            }

            @Override
            public int getItemCount() {
                return schedules.size();
            }
        });
    }

    // xml click
    public void onAddTaskClick(View view) {
        startActivityForResult(new Intent(this, TaskModifyActivity.class), 3);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.w(TAG, "onActivityResult: " + data);
        Task task = (Task) data.getSerializableExtra("DATA");
        schedules.add(task.schedule);
        adapter.notifyDataSetChanged();
        startService(data.setClass(this, TaskService.class));
    }

    class VHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvDesc, tvTime;

        public VHolder(View itemView) {
            super(itemView);
            tvName = (TextView) itemView.findViewById(R.id.alarm_name);
            tvDesc = (TextView) itemView.findViewById(R.id.alarm_desc);
            tvTime = (TextView) itemView.findViewById(R.id.alarm_time);
        }
    }
}

