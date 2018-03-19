package com.ivi.hybrid.ui.activitys;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.gyf.barlibrary.BarHide;
import com.gyf.barlibrary.ImmersionBar;
import com.hybrid.R;
import com.ivi.hybrid.core.config.Config;
import com.ivi.hybrid.gesture.utils.cache.GestureCache;
import com.ivi.hybrid.utils.LogUtil;

/**
 * author: Rea.X
 * date: 2017/7/18.
 */

public abstract class UIActivity extends FlurryActivity {
    protected AppCompatActivity context;
    protected ImmersionBar immersionBar;
    protected View statusbarView;

    @Override
    protected final void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        setContentView(getLayoutId());
        statusbarView = findViewById(R.id.top_view);
        init();
    }

    protected final void initScreen() {
        immersionBar = ImmersionBar
                .with(this)
                .statusBarView(statusbarView)
                .keyboardEnable(true)
                .navigationBarColorInt(Config.getColorPrimary())
        ;
        immersionBar.init();
    }

    protected final void changeGameScreen() {
            immersionBar.hideBar(BarHide.FLAG_HIDE_BAR).init();
    }

    protected final void changeNormalScreen() {
        immersionBar.hideBar(BarHide.FLAG_SHOW_BAR).init();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (immersionBar != null)
            immersionBar.destroy();
    }


    protected abstract
    @LayoutRes
    int getLayoutId();

    protected abstract void init();

    public void onLoginStatusChanged(boolean isLogin){}

}
