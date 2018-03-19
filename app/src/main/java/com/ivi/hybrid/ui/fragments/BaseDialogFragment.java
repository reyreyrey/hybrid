package com.ivi.hybrid.ui.fragments;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.DialogFragment2;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hybrid.R;
import com.ivi.hybrid.utils.CommonUtils;
import com.ivi.hybrid.utils.SoftKeyboardUtils;

public abstract class BaseDialogFragment<T extends ViewDataBinding> extends DialogFragment2 implements View.OnClickListener{
    protected boolean isVisible, isPrepared, isLoaded;
    protected Context context;
    protected AppCompatActivity activity;
    protected T databinding;
    protected View contentView;

    @Nullable
    @Override
    public final View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        databinding = DataBindingUtil.inflate(inflater, getContentView(), container, false);
        this.contentView = databinding.getRoot();
        isPrepared = true;
        context = getContext();
        activity = (AppCompatActivity) getActivity();
        onVisible();
        return this.contentView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init(view);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (getUserVisibleHint()) {
            isVisible = true;
            onVisible();
        } else {
            isVisible = false;
            onInvisible();
        }
    }

    private void onVisible() {
        if (!isPrepared || !isVisible) {
            return;
        }
        lzayLoadEveryVisible();
        if (isLoaded) return;
        isLoaded = true;
        lazyLoad();
    }

    /**
     * fragment处于可见状态时自动调用该方法，实现懒加载，一般用作网络请求
     */
    protected abstract void lazyLoad();

    /**
     * 每次都調用
     */
    protected void lzayLoadEveryVisible(){

    }

    /**
     * 获取界面主布局
     *
     * @return
     */
    protected abstract int getContentView();

    /**
     * 界面不可见时自动调用
     */
    protected void onInvisible() {
        try {
            SoftKeyboardUtils.hideSoftKeyboard(getContext());
        } catch (Exception e) {
        }
    }


    /**
     * 初始化操作
     */
    protected abstract void init(View view);

    @Override
    public void onClick(View v) {

    }
}
