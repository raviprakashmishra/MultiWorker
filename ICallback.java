package com.ravi.multiworker;

public interface ICallback {

    public void onSuccess(Result result);

    public void onFailure(Throwable t);
}
