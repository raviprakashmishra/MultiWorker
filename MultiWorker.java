package com.ravi.multiworker;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.LinkedBlockingQueue;

public class MultiWorker {
    BlockingQueue<Worker> workQueue ;
    List<CompletableFuture> list;

    MultiWorker(BlockingQueue<Worker> workQueue) {
        this.workQueue = workQueue;
        this.list = new ArrayList<>();
    }

    public void start(ICallback callback) {
        while (true) {
            if (workQueue.isEmpty()) {
                // all work done
                callback.onSuccess(new Result());
                break;
            }
        }

    }

}

class MultiWorkerThread implements Runnable {
    BlockingQueue<Worker> workQueue;
    MultiWorkerThread (BlockingQueue<Worker> workQueue) {
        this.workQueue = workQueue;
    }

    @Override
    public void run() {
        while (!workQueue.isEmpty()) {
            Worker worker = workQueue.poll();
            worker.start(worker.callback);
        }
    }
}
