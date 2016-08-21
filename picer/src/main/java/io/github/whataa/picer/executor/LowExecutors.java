package io.github.whataa.picer.executor;

import android.os.Process;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import static android.os.Process.THREAD_PRIORITY_BACKGROUND;

/**
 * Cancel the dynamic judgment of the network and the default thread's count is set as 4.<br/>
 * this cause Picasso's Priority comes invalid.
 */
public class LowExecutors extends ThreadPoolExecutor {
    private static final int DEFAULT_THREAD_COUNT = 4;

    public LowExecutors() {
        super(DEFAULT_THREAD_COUNT, DEFAULT_THREAD_COUNT, 0, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<Runnable>(), new LowThreadFactory());
    }

    static class LowThreadFactory implements ThreadFactory {
        public Thread newThread(Runnable r) {
            return new LowPriorityThread(r);
        }
    }

    private static class LowPriorityThread extends Thread {
        public LowPriorityThread(Runnable r) {
            super(r);
        }

        @Override
        public void run() {
            Process.setThreadPriority(THREAD_PRIORITY_BACKGROUND);
            super.run();
        }
    }
}
