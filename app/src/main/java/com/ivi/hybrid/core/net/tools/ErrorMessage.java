package com.ivi.hybrid.core.net.tools;

import com.hybrid.R;
import com.ivi.hybrid.core.Hybrid;
import com.ivi.hybrid.core.net.models.ResultModel;

import java.io.IOException;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

/**
 * author: Rea.X
 * date: 2017/11/3.
 */

public class ErrorMessage {

    /**
     * 通过异常获取错误信息
     *
     * @param e
     * @return
     */
    public static String createErrorMessage(Throwable e) {
        if (e instanceof UnknownHostException || e instanceof ConnectException) {
            //网络连接失败
            return Hybrid.get().getString(R.string.hybrid_connect_error);
        }
        if (e instanceof SocketTimeoutException) {
            //超时
            return Hybrid.get().getString(R.string.hybrid_connect_timeout);
        }
        return Hybrid.get().getString(R.string.hybrid_connect_timeout);
    }

    public static ResultModel getErrorModel(Throwable e) {
        ResultModel model = new ResultModel();
        String message = createErrorMessage(e);
        model.status = false;
        model.message = message;
        return model;
    }
}
