package com.ivi.hybrid.utils;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

/**
 * author: Rea.X
 * date: 2017/3/15.
 * <p>键盘相关工具类</p>
 */

public class SoftKeyboardUtils {
    public static void hideSoftKeyboard(Context context){
        View focus_view = ((Activity)context).getCurrentFocus();
        if(focus_view != null){
            InputMethodManager inputManager = (InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(focus_view.getWindowToken(), 0);
        }
    }
    public static void hideSoftKeyboard(Context context, View view){
        InputMethodManager inputManager = (InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public static void showSoftKeyboard(Context context, View v){
        v.requestFocus();
        ((InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE)).showSoftInput(v, InputMethodManager.RESULT_SHOWN);
    }
    public static void togSoftkeybord(Context ctx, View view, boolean isHide) {
        InputMethodManager imm = (InputMethodManager) ctx.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (isHide)
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0); // 强制隐藏键盘
        else
            imm.showSoftInput(view, InputMethodManager.SHOW_FORCED);
    }

}
