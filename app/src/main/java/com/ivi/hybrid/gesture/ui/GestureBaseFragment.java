package com.ivi.hybrid.gesture.ui;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.gyf.barlibrary.BarHide;
import com.gyf.barlibrary.ImmersionBar;
import com.hybrid.R;
import com.hybrid.databinding.GestureLockBinding;
import com.ivi.hybrid.core.Hybrid;
import com.ivi.hybrid.core.config.Config;
import com.ivi.hybrid.gesture.GestureManager;
import com.ivi.hybrid.gesture.config.AbstractViewConfig;
import com.ivi.hybrid.gesture.config.GestureConfig;
import com.ivi.hybrid.gesture.listeners.GestureListener;
import com.ivi.hybrid.gesture.utils.GestureToast;
import com.ivi.hybrid.gesture.utils.cache.GestureCache;
import com.ivi.hybrid.gesture.views.Lock9View;
import com.ivi.hybrid.ui.fragments.BaseDialogFragment;
import com.ivi.hybrid.utils.CommonUtils;
import com.ivi.hybrid.utils.LogUtil;

/**
 * author: Rea.X
 * date: 2018/1/29.
 */

public class GestureBaseFragment extends BaseDialogFragment<GestureLockBinding> implements Lock9View.CallBack {
    protected GestureConfig gestureConfig;
    protected ImmersionBar immersionBar;
    protected AbstractViewConfig viewConfig;
    protected GestureListener gestureListener;

    @Override
    protected void lazyLoad() {

    }

    @Override
    protected int getContentView() {
        return R.layout.gesture_lock;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        gestureConfig = Hybrid.getGestureConfig();
        viewConfig = gestureConfig.getViewConfig();
        create();
        setCancelable(allowDismiss());
        setStyle(DialogFragment.STYLE_NORMAL, android.R.style.Theme_NoTitleBar);
    }

    @Override
    protected void init(View view) {
        databinding.toolbar.getRoot().setVisibility(showToolbar() ? View.VISIBLE : View.GONE);
        initScreen();
        databinding.toolbar.toolbar.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        databinding.viewGesture.lock9View.setNodeSrc(gestureConfig.getNodeSrc());
        databinding.viewGesture.lock9View.setNodeOnSrc(gestureConfig.getNodeOnSrc());
        databinding.viewGesture.lock9View.setLineColor(gestureConfig.getLineColor());
        databinding.viewGesture.lock9View.setLineWidth(gestureConfig.getLineWidth());
        databinding.viewGesture.lock9View.setEnableVibrate(gestureConfig.isEnableVibrate());
        databinding.viewGesture.lock9View.setNodeOnAnim(gestureConfig.getNodeAnimation());
        databinding.viewGesture.lock9View.setCallBack(this);
        databinding.getRoot().setBackground(gestureConfig.getBackground());
    }

    protected final void initScreen() {
        immersionBar = ImmersionBar
                .with(this, getDialog())
                .statusBarView(databinding.topView)
        ;
        immersionBar.init();
    }


    protected boolean showToolbar() {
        return true;
    }

    protected boolean allowDismiss() {
        return true;
    }

    protected void onGestureCallBack(String password, boolean isRight) {

    }

    public void show(FragmentActivity appCompatActivity, GestureListener gestureListener) {
        show(appCompatActivity.getSupportFragmentManager(), "gesture");
        this.gestureListener = gestureListener;
    }

    @Override
    public void dismiss() {
        dismissAllowingStateLoss();
    }

    protected void create() {
    }


    @Override
    public void onFinish(String password) {
        if (password.length() < gestureConfig.getPwdMinLength()) {
            GestureToast.toast(String.format(getString(R.string.ges_min_line), gestureConfig.getPwdMinLength()));
            return;
        }
        boolean isRight = GestureCache.getInstant().checkGesturePwd(password);
        onGestureCallBack(password, isRight);
        if (gestureListener != null) {
            gestureListener.onGestureInputFinish(this, password, isRight);
        }
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        GestureCache.getInstant().gestureDismiss();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (immersionBar != null)
            immersionBar.destroy();
    }
}
