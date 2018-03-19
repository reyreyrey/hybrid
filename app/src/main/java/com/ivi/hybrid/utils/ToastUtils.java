package com.ivi.hybrid.utils;

import android.content.Context;
import android.graphics.Color;
import android.widget.Toast;

/**
 * Created by Rea.X on 2017/2/2.
 * <p>Toast工具类</p>
 */

public class ToastUtils {

    private static final int COLOR = Color.parseColor("#F9F4E5");


    public static void toastError(Context context, String msg) {
        showNativaToast(context, msg);

    }

    public static void toastSuccess(Context context, String msg) {
        showNativaToast(context, msg);
    }

    public static void toastWarn(Context context, String msg) {
        showNativaToast(context, msg);

    }


    private static void showNativaToast(Context context, String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
    }
}
