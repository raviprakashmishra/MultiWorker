package com.ravi.multiworker;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class MultiWorker {
    BlockingQueue<Worker> workQueue ;
    List<CompletableFuture> list;

    MultiWorker(BlockingQueue<Worker> workQueue) {
        this.workQueue = workQueue;
        this.list = new ArrayList<>();
    }

    public void start(ICallback callback) throws ExecutionException, InterruptedException {
        Future<List<CompletableFuture<Boolean>>> future = Executors.newSingleThreadExecutor().submit(new MultiWorkerThread(workQueue));
        List<CompletableFuture<Boolean>> list = future.get();

        CompletableFuture<Void> allFuture = CompletableFuture.allOf(list.toArray(new CompletableFuture[0]));
        
        allFuture.whenComplete((a, e) -> {
            if (e == null) {
                callback.onFailure(e);
            } else {
                callback.onSuccess(new Result());
            }
        }) ;


    }

}

class MultiWorkerThread implements Callable {
    BlockingQueue<Worker> workQueue;
    MultiWorkerThread (BlockingQueue<Worker> workQueue) {
        this.workQueue = workQueue;
    }

    @Override
    public List<CompletableFuture<Boolean>> call() {
        List<CompletableFuture<Boolean>> list = new ArrayList<>();
        while (!workQueue.isEmpty()) {
            Worker worker = workQueue.poll();
            list.add(worker.start(worker.callback));
        }

        return list;
    }
}
