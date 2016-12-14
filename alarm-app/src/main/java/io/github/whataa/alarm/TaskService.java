package io.github.whataa.alarm;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;

import io.github.whataa.alarm.entity.Task;

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
        handler = new Handler(dispatcherThread.getLooper());
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.w(TAG, "onStartCommand: " + intent);
        Task task = (Task) intent.getSerializableExtra("DATA");
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

    class TaskHandler extends Handler {

        public TaskHandler(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(Message msg) {
            Log.w(TAG, "handleMessage: " + msg);
            switch (msg.what) {
                case 1:
                    Task task = (Task) msg.obj;
                    if (task.nextActionId < task.actions.size()) {
                        long delayed = task.actions.get(task.nextActionId++).duration;
                        sendMessageDelayed(msg, delayed);
                    }
                    break;
            }
        }


    }

    static class DispatcherThread extends HandlerThread {

        public DispatcherThread() {
            super(DispatcherThread.class.getName());
        }
    }
}
