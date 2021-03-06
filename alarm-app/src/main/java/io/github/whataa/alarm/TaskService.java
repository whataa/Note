package io.github.whataa.alarm;

import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import java.util.Random;

import io.github.whataa.alarm.entity.Schedule;
import io.github.whataa.alarm.entity.Task;
import io.realm.Realm;
import io.realm.RealmResults;

/**
 * - 第一次启动时，读取数据库，设置闹钟；
 * - 任务Task启动
 * - 任务Task启动成功
 * - 任务Task取消
 * - 任务Task取消成功
 * - 任务Task节点Action执行开始
 * - 任务Task节点Action执行结束
 * - 任务Task完成
 */
public class TaskService extends Service {

    public static final String TAG = TaskService.class.getSimpleName();
    private DispatcherThread dispatcherThread;
    private Handler handler;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.w(TAG, "onCreate: ");
        dispatcherThread = new DispatcherThread();
        dispatcherThread.start();
        handler = new TaskHandler(dispatcherThread.getLooper());
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.w(TAG, "onStartCommand: " + intent);
        Realm realm = Realm.getDefaultInstance();
        RealmResults<Schedule> results = realm.where(Schedule.class).equalTo("id", intent.getIntExtra("DATA", 0)).findAll();
        Schedule schedule = realm.copyFromRealm(results.first());
        Task task = new Task(schedule, schedule.actions);
        dispatchSubmit(task);
        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        Log.w(TAG, "onDestroy: ");
        dispatcherThread.quit();
        super.onDestroy();
    }

    void dispatchSubmit(Task task) {
        handler.sendMessage(handler.obtainMessage(1, task));
    }
    void dispatchCancel() {

    }
    void performSubmit() {

    }
    void performCancel() {

    }

    private void sendNotificationMessage(String text) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_launcher)
                .setTicker("setTicker:"+text)
                .setSubText("setSubText:"+text)
                .setVibrate(new long[]{0,100})
                .setContentInfo("setContentInfo:"+text)
//                .setGroup("GROUP") // 与Android Wear有关
                .setContentTitle("setContentTitle:"+text);
//        if (text == null) {
//            builder.setGroupSummary(true);
//        } else {
//            builder.setSubText("setSubText:"+text).setContentInfo("setContentInfo:"+text);
//        }
        ((NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE)).notify(new Random().nextInt(),builder.build());
    }

    class TaskHandler extends Handler {

        TaskHandler(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(Message msg) {
            Log.w(TAG, "handleMessage: " + msg);

            switch (msg.what) {
                case 1:
                    Task task = (Task) msg.obj;
                    sendNotificationMessage(task.toString());
                    if (task.nextActionOrder < task.actions.size()) {
                        long delayed = task.actions.get(task.nextActionOrder++).duration;
                        sendMessageDelayed(obtainMessage(1, msg.obj), delayed);
                    }
                    break;
            }
        }


    }

    static class DispatcherThread extends HandlerThread {

        public DispatcherThread() {
            super(DispatcherThread.class.getName());
        }

        @Override
        protected void onLooperPrepared() {
            Log.w(TAG, "DispatcherThread: onLooperPrepared");
        }
    }
}
