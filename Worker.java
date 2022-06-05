package com.ravi.multiworker;

import java.util.concurrent.CompletableFuture;

public class Worker{
    Work work;
    ICallback callback;
    Worker(Work work) {
        this.work = work;
    }

    public void start(ICallback callback) {
        this.callback = callback;
        CompletableFuture<Boolean> future = new CompletableFuture<>();
        new Thread(new WorkerThread(work, future)).start();
        future.whenComplete( (avoid, e) -> {
            if (e == null) {
                callback.onSuccess(new Result());
            } else {
                callback.onFailure(e);
            }
        });
    }


}

class WorkerThread implements Runnable {
    Work work;
    CompletableFuture<Boolean> future;
    WorkerThread(Work work, CompletableFuture<Boolean> future) {
        this.work = work;
        this.future = future;
    }

    @Override
    public void run() {
        try {
            boolean success = work.execute();
            future.complete(success);
        } catch (Throwable t) {
            future.completeExceptionally(t);
        }
    }
}
