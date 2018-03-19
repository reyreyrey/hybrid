package com.ivi.hybrid.core.push.callback;


public abstract class Callback {

    public abstract void onSuccess(byte[] t);

    public abstract void onFailure(Error error);

    public abstract void onSend(byte[] t);

    public abstract void onBeforeSend(byte[] t);


    public abstract void sendMessage(int what, Object object);
}
