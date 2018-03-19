package com.ivi.hybrid.core.config.forward;

import android.app.Activity;
import android.os.Bundle;

import java.io.Serializable;

/**
 * author: Rea.X
 * date: 2017/7/4.
 */

public class ForwardMapModel implements Serializable {
    private static final long serialVersionUID = 5311610035596640826L;

    /**
     * 需要跳转的activity
     */
    private Class<? extends Activity> activity;
    /**
     * requestCode
     */
    private int requestCode = -1;
    /**
     * 需要传递的参数
     */
    private Bundle bundle;
    /**
     * NewTask
     */
    private int flag = -1;


    public Class<? extends Activity> getActivity() {
        return activity;
    }

    public void setActivity(Class<? extends Activity> activity) {
        this.activity = activity;
    }

    public int getRequestCode() {
        return requestCode;
    }

    public void setRequestCode(int requestCode) {
        this.requestCode = requestCode;
    }

    public Bundle getBundle() {
        return bundle;
    }

    public void setBundle(Bundle bundle) {
        this.bundle = bundle;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }
}
