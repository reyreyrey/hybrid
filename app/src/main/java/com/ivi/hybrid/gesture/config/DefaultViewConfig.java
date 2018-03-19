package com.ivi.hybrid.gesture.config;

import android.app.Activity;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.os.Handler;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.hybrid.R;
import com.hybrid.databinding.GestureLockBottomBinding;
import com.hybrid.databinding.GestureLockHeaderBinding;
import com.hybrid.databinding.GestureSetHeaderBinding;
import com.hybrid.databinding.GestureValidationHeaderBinding;
import com.ivi.hybrid.core.Hybrid;
import com.ivi.hybrid.core.call.CallJS;
import com.ivi.hybrid.core.call.CallModule;
import com.ivi.hybrid.core.net.tools.UserHelper;
import com.ivi.hybrid.core.webview.x5.PostWebView;
import com.ivi.hybrid.gesture.GestureManager;
import com.ivi.hybrid.gesture.utils.GestureToast;
import com.ivi.hybrid.ui.activitys.HybridMainActivity;
import com.ivi.hybrid.ui.activitys.HybridWebViewActivity;
import com.ivi.hybrid.utils.activity_manager.ActivityManager;

/**
 * author: Rea.X
 * date: 2018/1/29.
 */

public class DefaultViewConfig implements AbstractViewConfig {

    private Animation sharkAnim;
    private GestureLockHeaderBinding lockHeaderBinding;
    private GestureLockBottomBinding lockBottomBinding;
    private GestureSetHeaderBinding setHeaderBindingFirst, setHeaderBindingSecond;
    private GestureValidationHeaderBinding validationHeaderBinding;

    public DefaultViewConfig() {
        sharkAnim = AnimationUtils.loadAnimation(Hybrid.get(), R.anim.gesture_anim_shark);
        LayoutInflater layoutInflater = LayoutInflater.from(Hybrid.get());
        lockHeaderBinding = DataBindingUtil.inflate(layoutInflater, R.layout.gesture_lock_header, null, false);
        lockBottomBinding = DataBindingUtil.inflate(layoutInflater, R.layout.gesture_lock_bottom, null, false);
        setHeaderBindingFirst = DataBindingUtil.inflate(layoutInflater, R.layout.gesture_set_header, null, false);
        setHeaderBindingSecond = DataBindingUtil.inflate(layoutInflater, R.layout.gesture_set_header, null, false);
        validationHeaderBinding = DataBindingUtil.inflate(layoutInflater, R.layout.gesture_validation_header, null, false);
    }

    @Override
    public View providerView(GestureDetailViewType type) {
        switch (type) {
            case GESTURE_LOCK_HEADER:
                return lockHeaderBinding.getRoot();
            case GESTURE_LOCK_BOTTOM:
                return lockBottomBinding.getRoot();
            case GESTURE_SETTING_FIRST_BOTTOM:
                return null;
            case GESTURE_SETTING_FIRST_HEADER:
                setHeaderBindingFirst.tvTitle.setText("绘制第一遍");
                return setHeaderBindingFirst.getRoot();
            case GESTURE_SETTING_SECOND_BOTTOM:
                return null;
            case GESTURE_SETTING_SECOND_HEADER:
                setHeaderBindingSecond.tvTitle.setText("请再次绘制手势密码");
                return setHeaderBindingSecond.getRoot();
            case GESTURE_VALIDATION_HEADER:
                return validationHeaderBinding.getRoot();
        }
        return null;
    }

    @Override
    public boolean isShowToolbar(GestureViewType type) {
        switch (type) {
            case GESTURE_LOCK:
                return false;
            case GESTURE_SETTING:
                return false;
            case GESTURE_VALIDATION:
                return true;
        }
        return false;
    }

    @Override
    public void initToolbar(GestureViewType type, Toolbar toolbar, TextView tvBack, TextView tvTitle, View statusBarView) {
        switch (type) {
            case GESTURE_LOCK:
                break;
            case GESTURE_SETTING:
                break;
            case GESTURE_VALIDATION:
                statusBarView.setBackgroundColor(Color.parseColor("#0C0C2D"));
                toolbar.setBackgroundColor(Color.parseColor("#0C0C2D"));
                break;
        }
    }

    @Override
    public boolean allowDismiss(GestureViewType type) {
        switch (type) {
            case GESTURE_LOCK:
                return false;
            case GESTURE_SETTING:
                return false;
            case GESTURE_VALIDATION:
                return true;
        }
        return false;
    }

    @Override
    public void onGestureFinished(final DialogFragment dialogFragment, GestureViewType type, boolean isRight, int remainTryCount) {
        switch (type) {
            case GESTURE_LOCK:
                if (isRight) {
                    dialogFragment.dismissAllowingStateLoss();
                } else {
                    lockHeaderBinding.tvMsg.setTextColor(Color.RED);
                    lockHeaderBinding.tvMsg.setText("手势输入错误，请重试，剩余" + remainTryCount + "次");
                    lockHeaderBinding.tvMsg.startAnimation(sharkAnim);
                }
                break;
            case GESTURE_SETTING:
                GestureToast.toastSuccess("设置成功");
                dialogFragment.dismissAllowingStateLoss();
                break;
            case GESTURE_VALIDATION:
                if (isRight) {
                    GestureManager.showGestureSetting(dialogFragment.getActivity());
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            dialogFragment.dismissAllowingStateLoss();
                        }
                    }, 200);
                } else {
                    validationHeaderBinding.tvMsg.setTextColor(Color.RED);
                    validationHeaderBinding.tvMsg.setText("手势输入错误，请重试，剩余" + remainTryCount + "次");
                    validationHeaderBinding.tvMsg.startAnimation(sharkAnim);
                }
                break;
        }
    }

    @Override
    public void onGestureWrongMaxCount(DialogFragment dialogFragment, GestureViewType type) {
        UserHelper.INSTANT.loginOut();
        dialogFragment.dismissAllowingStateLoss();
        Activity activity = ActivityManager.getTagActivity();
        if (activity != null) {
            if (activity instanceof HybridMainActivity) {
                HybridMainActivity hybridMainActivity = (HybridMainActivity) activity;
                PostWebView postWebView = hybridMainActivity.getCurrentWebView();
                if (postWebView != null) {
                    CallJS.loadFinish(postWebView);
                }
            }
            if (activity instanceof HybridWebViewActivity) {
                HybridWebViewActivity hybridWebViewActivity = (HybridWebViewActivity) activity;
                PostWebView postWebView = hybridWebViewActivity.getWebView();
                if (postWebView != null) {
                    CallJS.loadFinish(postWebView);
                }
            }
        }
    }
}
