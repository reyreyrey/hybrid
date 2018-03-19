package com.ivi.hybrid.core.bridge;

/**
 * author: Rea.X
 * date: 2017/11/2.
 */

public interface OnBridgeInvokedListener {
    void onBeforeInvoked(AppBridgeModel model);

    void onAfterInvoked(AppBridgeModel model, String result);
}
