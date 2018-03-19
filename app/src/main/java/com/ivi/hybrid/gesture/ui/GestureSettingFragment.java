package com.ivi.hybrid.gesture.ui;

import android.content.DialogInterface;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;

import com.ivi.hybrid.gesture.config.AbstractViewConfig;
import com.ivi.hybrid.gesture.config.GestureDetailViewType;
import com.ivi.hybrid.gesture.config.GestureViewType;
import com.ivi.hybrid.gesture.utils.GestureToast;
import com.ivi.hybrid.gesture.utils.cache.GestureCache;
import com.ivi.hybrid.utils.MD5;

import static com.ivi.hybrid.gesture.config.GestureViewType.GESTURE_LOCK;
import static com.ivi.hybrid.gesture.config.GestureViewType.GESTURE_SETTING;

/**
 * author: Rea.X
 * date: 2018/1/31.
 */

public class GestureSettingFragment extends GestureBaseFragment {
    private String firstPassword;
    private View firstHeadView, firstBottomView, secondHeadView, secondBottomView;
    private static GestureSettingFragment gestureSettingFragment;

    public static GestureSettingFragment getInstant() {
        if (gestureSettingFragment != null) return null;
        gestureSettingFragment = new GestureSettingFragment();
        return gestureSettingFragment;
    }
    @Override
    protected void init(View view) {
        super.init(view);
        viewConfig.initToolbar(GESTURE_SETTING, databinding.toolbar.toolbar, databinding.toolbar.tvBack, databinding.toolbar.tvTitle, databinding.topView);
        firstHeadView = viewConfig.providerView(GestureDetailViewType.GESTURE_SETTING_FIRST_HEADER);
        firstBottomView = viewConfig.providerView(GestureDetailViewType.GESTURE_SETTING_FIRST_BOTTOM);
        secondHeadView = viewConfig.providerView(GestureDetailViewType.GESTURE_SETTING_SECOND_HEADER);
        secondBottomView = viewConfig.providerView(GestureDetailViewType.GESTURE_SETTING_SECOND_BOTTOM);
        if (firstHeadView != null){
            ViewGroup group = (ViewGroup) firstHeadView.getParent();
            if(group != null)
                group.removeView(firstHeadView);
            databinding.viewHeader.removeAllViews();
            databinding.viewHeader.addView(firstHeadView);
        }
        if (firstBottomView != null){
            ViewGroup group = (ViewGroup) firstBottomView.getParent();
            if(group != null)
                group.removeView(firstBottomView);
            databinding.viewBottom.removeAllViews();
            databinding.viewBottom.addView(firstBottomView);
        }

    }
    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        gestureSettingFragment = null;
    }

    @Override
    protected boolean showToolbar() {
        return viewConfig.isShowToolbar(GestureViewType.GESTURE_SETTING);
    }

    @Override
    protected boolean allowDismiss() {
        return viewConfig.allowDismiss(GestureViewType.GESTURE_SETTING);
    }

    @Override
    protected void onGestureCallBack(String pwd, boolean isRight) {
        super.onGestureCallBack(pwd, isRight);
        if (TextUtils.isEmpty(firstPassword)) {
            firstPassword = pwd;
            if (secondHeadView != null) {
                ViewGroup group = (ViewGroup) secondHeadView.getParent();
                if(group != null)
                    group.removeView(secondHeadView);
                databinding.viewHeader.removeAllViews();
                databinding.viewHeader.addView(secondHeadView);
            }
            if (secondBottomView != null) {
                ViewGroup group = (ViewGroup) secondBottomView.getParent();
                if(group != null)
                    group.removeView(secondBottomView);
                databinding.viewBottom.removeAllViews();
                databinding.viewBottom.addView(secondBottomView);
            }
            return;
        }
        if(pwd.equalsIgnoreCase(firstPassword)){
            GestureCache.getInstant().saveGesturePwd(pwd);
            viewConfig.onGestureFinished(this, GestureViewType.GESTURE_SETTING, isRight, GestureCache.getInstant().getRemainCount());
            return;
        }
        GestureToast.toast("两次绘制手势不一致，请重新绘制");
    }
}
