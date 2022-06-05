package com.ravi.multiworker;

public class Work {

    public boolean execute() throws Throwable{
        // do some work here
        synchronized (this) {
            if (true) {
                return true;
            } else {
                return false;
            }
        }

    }
}
