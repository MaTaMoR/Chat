package me.matamor.test.server;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicBoolean;

public class RunnableQueue extends Thread {

    private final Queue<Runnable> queue = new ConcurrentLinkedQueue<>();
    private final AtomicBoolean enabled = new AtomicBoolean(true);

    public void registerQueue(Runnable Runnable) {
        this.queue.add(Runnable);
    }

    public void stopTicking() {
        this.enabled.set(false);
    }

    @Override
    public void run() {
        while (this.enabled.get()) {
            Runnable Runnable = this.queue.poll();

            if (Runnable != null) {
                Runnable.run();
            }
        }
    }
}
