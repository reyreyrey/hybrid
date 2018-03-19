package com.ivi.hybrid.gesture.ui;

import android.content.DialogInterface;
import android.view.View;
import android.view.ViewGroup;

import com.ivi.hybrid.gesture.config.AbstractViewConfig;
import com.ivi.hybrid.gesture.config.GestureDetailViewType;
import com.ivi.hybrid.gesture.config.GestureViewType;
import com.ivi.hybrid.gesture.utils.cache.GestureCache;
import com.ivi.hybrid.utils.LogUtil;

import static com.ivi.hybrid.gesture.config.GestureViewType.GESTURE_LOCK;

/**
 * author: Rea.X
 * date: 2018/1/30.
 */

public class GestureLockFragment extends GestureBaseFragment {

    private static GestureLockFragment gestureLockFragment;

    public static GestureLockFragment getInstant() {
        if (gestureLockFragment != null) return null;
        gestureLockFragment = new GestureLockFragment();
        return gestureLockFragment;
    }

    @Override
    protected void init(View view) {
        super.init(view);
        viewConfig.initToolbar(GESTURE_LOCK, databinding.toolbar.toolbar, databinding.toolbar.tvBack, databinding.toolbar.tvTitle, databinding.topView);
        View headView = viewConfig.providerView(GestureDetailViewType.GESTURE_LOCK_HEADER);
        View bottomView = viewConfig.providerView(GestureDetailViewType.GESTURE_LOCK_BOTTOM);
        if (headView != null) {
            ViewGroup group = (ViewGroup) headView.getParent();
            if (group != null)
                group.removeView(headView);
            databinding.viewHeader.removeAllViews();
            databinding.viewHeader.addView(headView);
        }

        if (bottomView != null) {
            ViewGroup group = (ViewGroup) bottomView.getParent();
            if (group != null)
                group.removeView(bottomView);
            databinding.viewBottom.removeAllViews();
            databinding.viewBottom.addView(bottomView);
        }
    }


    @Override
    protected boolean showToolbar() {
        return viewConfig.isShowToolbar(GESTURE_LOCK);
    }

    @Override
    protected boolean allowDismiss() {
        return viewConfig.allowDismiss(GESTURE_LOCK);
    }

    @Override
    protected void onGestureCallBack(String pwd, boolean isRight) {
        super.onGestureCallBack(pwd, isRight);
        if (!isRight) {
            GestureCache.getInstant().gestureInputWrong();
            if (GestureCache.getInstant().getRemainCount() <= 0) {
                viewConfig.onGestureWrongMaxCount(this, GESTURE_LOCK);
            }
        } else {
            GestureCache.getInstant().reset();
        }
        viewConfig.onGestureFinished(this, GESTURE_LOCK, isRight, GestureCache.getInstant().getRemainCount());
    }


    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        gestureLockFragment = null;
    }
}
