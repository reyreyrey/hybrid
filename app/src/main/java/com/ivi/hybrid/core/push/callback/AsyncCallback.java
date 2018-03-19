package com.ivi.hybrid.core.push.callback;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import static com.ivi.hybrid.core.push.requests.Request.MESSAGE_FAILURE;
import static com.ivi.hybrid.core.push.requests.Request.MESSAGE_SEND;
import static com.ivi.hybrid.core.push.requests.Request.MESSAGE_SEND_BEFORE;
import static com.ivi.hybrid.core.push.requests.Request.MESSAGE_SUCCESS;


public abstract class AsyncCallback extends Callback {

    private Handler mHandler;

    public AsyncCallback() {
        mHandler = new InternalHandler(this, Looper.myLooper());
    }

    private static class InternalHandler extends Handler {

        private AsyncCallback mCallback;

        public InternalHandler(AsyncCallback callback, Looper looper) {
            super(looper);
            mCallback = callback;
        }

        @Override
        public void handleMessage(Message msg) {
            mCallback.handleMessage(msg);
        }
    }


    private void handleMessage(Message message) {

        switch (message.what) {
            case MESSAGE_SUCCESS:
                onSuccess((byte[]) message.obj);
                break;
            case MESSAGE_FAILURE:
                onFailure(new Error("ERROR"));
                break;
            case MESSAGE_SEND:
                onSend((byte[]) message.obj);
                break;
            case MESSAGE_SEND_BEFORE:
                onBeforeSend((byte[]) message.obj);
                break;
            default:
                break;
        }
    }

    @Override
    public void sendMessage(int what, Object object) {
        mHandler.obtainMessage(what, object).sendToTarget();
    }

}
