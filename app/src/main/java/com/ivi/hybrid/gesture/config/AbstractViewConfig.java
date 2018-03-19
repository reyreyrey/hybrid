package com.ivi.hybrid.gesture.config;

import android.support.v4.app.DialogFragment;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

/**
 * author: Rea.X
 * date: 2018/1/29.
 */

public interface AbstractViewConfig {

    View providerView(GestureDetailViewType type);

    boolean isShowToolbar(GestureViewType type);

    void initToolbar(GestureViewType type, Toolbar toolbar, TextView tvBack, TextView tvTitle, View statusBarView);

    boolean allowDismiss(GestureViewType type);

    void onGestureFinished(DialogFragment dialogFragment, GestureViewType type, boolean isRight, int remainTryCount);

    void onGestureWrongMaxCount(DialogFragment dialogFragment, GestureViewType type);
}
