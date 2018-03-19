package com.ivi.hybrid.ui.fragments;

/**
 * author: Rea.X
 * date: 2017/4/7.
 */

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.ivi.hybrid.utils.SoftKeyboardUtils;

public abstract class BaseFragment<T extends ViewDataBinding> extends Fragment {
    protected boolean isVisible;
    protected boolean isPrepared;
    protected boolean isLoaded;
    protected View contentView;
    protected T binding;

    public BaseFragment() {
    }

    @Nullable
    public final View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, getContentView(), container, false);
        this.contentView = binding.getRoot();
        this.isPrepared = true;
        this.init();
        this.onVisible();
        return this.contentView;
    }

    protected void showKeyboard(final EditText editText) {
        (new Handler()).postDelayed(new Runnable() {
            public void run() {
                SoftKeyboardUtils.showSoftKeyboard(BaseFragment.this.getContext(), editText);
            }
        }, 100L);
    }

    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(this.getUserVisibleHint()) {
            this.isVisible = true;
            this.onVisible();
        } else {
            this.isVisible = false;
            this.onInvisible();
        }

    }

    private void onVisible() {
        if(this.isPrepared && this.isVisible) {
            this.lzayLoadEveryVisible();
            if(!this.isLoaded) {
                this.isLoaded = true;
                this.lazyLoad();
            }
        }
    }

    protected abstract void lazyLoad();

    protected void lzayLoadEveryVisible() {
    }

    protected abstract int getContentView();

    protected void onInvisible() {
        try {
            SoftKeyboardUtils.hideSoftKeyboard(this.getContext());
        } catch (Exception var2) {
            ;
        }

    }

    protected abstract void init();

}
