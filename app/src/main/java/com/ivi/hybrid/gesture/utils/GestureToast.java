package com.ivi.hybrid.gesture.utils;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.widget.Toast;

import com.hybrid.R;
import com.hybrid.databinding.GestureSucesstoastBinding;
import com.ivi.hybrid.core.Hybrid;

/**
 * author: Rea.X
 * date: 2018/1/31.
 */

public class GestureToast extends Toast {
    private Context context;
    private boolean isSuccess;
    private String title, content;
    private GestureSucesstoastBinding binding;

    private GestureToast(Context context, String title, String content) {
        this(context, title, content, false);
    }

    private GestureToast(Context context, String title, String content, boolean isSuccess) {
        super(context);
        this.title = title;
        this.content = content;
        this.isSuccess = isSuccess;
        this.context = context;
        init();
    }

    private void init() {
        binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.gesture_sucesstoast, null, false);
        setView(binding.getRoot());
        binding.title.setText(title);
        binding.content.setText(content);
        binding.title.setCompoundDrawablesWithIntrinsicBounds(0, isSuccess ? R.mipmap.gesture_image_successful : 0, 0, 0);
        setGravity(Gravity.CENTER, 0, 0);
    }

    public static void toast(String content) {
        GestureToast toast = new GestureToast(Hybrid.get(), "温馨提示", content, false);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.show();
    }

    public static void toastSuccess(String content) {
        GestureToast toast = new GestureToast(Hybrid.get(), "", content, true);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.show();
    }
}
