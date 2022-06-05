package com.ravi.listeners;

import java.util.ArrayList;

public class MyEvent {
    MyList<IListener> list;
    boolean isFinished = false;

    MyEvent() {
        list = new MyList<>();
    }

    public void registerCallback(IListener listener) {
        produceEvents(listener);
    }

    public void produceEvents(IListener listener) {
        list.addItem(listener, () -> {
            if (isFinished) {
                eventDone();
            }
        });
    }

    public void startEvent() {
        // logic here

        eventDone();
    }

    public void eventDone() {
        executeCallBacks();
    }

    private void executeCallBacks() {
        synchronized (list) {
            new Thread(() -> {
                for (IListener listener: list) {
                    listener.done();
                    // this produces ConcurrentModificationException
                    // concurrent exception can also be there even if we use fail-safe iterator because while call backs are being
                    // executed at the same time someone may try to add a new listener to the list
                    list.remove(listener);
                }
                if (!isFinished)  {
                    isFinished = true;
                }
            }).start();
        }
    }

}

class MyList<K> extends ArrayList<K> {
    public boolean addItem(K o, ItemAdded itemAdded) {
        boolean added = super.add(o);
        if (added) {
            itemAdded.itemAdded();
        }
        return added;
    }
}

interface ItemAdded {
    public void itemAdded();
}


