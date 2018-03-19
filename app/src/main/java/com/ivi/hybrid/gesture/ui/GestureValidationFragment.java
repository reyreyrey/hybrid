package com.ivi.hybrid.gesture.ui;

import android.content.DialogInterface;
import android.view.View;
import android.view.ViewGroup;

import com.ivi.hybrid.gesture.config.GestureDetailViewType;
import com.ivi.hybrid.gesture.utils.cache.GestureCache;

import static com.ivi.hybrid.gesture.config.GestureViewType.GESTURE_LOCK;
import static com.ivi.hybrid.gesture.config.GestureViewType.GESTURE_VALIDATION;

/**
 * author: Rea.X
 * date: 2018/1/31.
 */

public class GestureValidationFragment extends GestureBaseFragment {
    private static GestureValidationFragment gestureValidationFragment;

    public static GestureValidationFragment getInstant() {
        if (gestureValidationFragment != null) return null;
        gestureValidationFragment = new GestureValidationFragment();
        return gestureValidationFragment;
    }

    @Override
    protected void init(View view) {
        super.init(view);
        viewConfig.initToolbar(GESTURE_VALIDATION, databinding.toolbar.toolbar, databinding.toolbar.tvBack, databinding.toolbar.tvTitle, databinding.topView);
        View headView = viewConfig.providerView(GestureDetailViewType.GESTURE_VALIDATION_HEADER);
        if (headView != null) {
            ViewGroup group = (ViewGroup) headView.getParent();
            if (group != null)
                group.removeView(headView);
            databinding.viewHeader.removeAllViews();
            databinding.viewHeader.addView(headView);
        }
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        gestureValidationFragment = null;
    }


    @Override
    protected boolean showToolbar() {
        return viewConfig.isShowToolbar(GESTURE_VALIDATION);
    }

    @Override
    protected boolean allowDismiss() {
        return viewConfig.allowDismiss(GESTURE_VALIDATION);
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
        viewConfig.onGestureFinished(this, GESTURE_VALIDATION, isRight, GestureCache.getInstant().getRemainCount());
    }
}
