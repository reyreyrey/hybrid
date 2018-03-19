package com.ivi.hybrid.ui.progressDialog;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.os.Build;
import android.support.annotation.RequiresApi;

/**
 * <p>Created by Rea.X on 2017/2/6.</p>
 * <p>显示ProgressDialog的工具类</p>
 */

public class ProgressDialogUtils {

    private static ProgressDialog progressDialog;

    private static void initProgressDialog(Context context) {
        if (progressDialog == null) {
            progressDialog = new ApiProgressDialog(context);
            progressDialog.setCanceledOnTouchOutside(false);
        }
    }


    public static void showProgress(Context context) {
        if (context instanceof Activity) {
            Activity activity = (Activity) context;
            if (activity.isFinishing()) return;
        } else {
            throw new RuntimeException("context must be instanceof Activity");
        }
        if (progressDialog == null)
            initProgressDialog(context);
        if (progressDialog != null && !progressDialog.isShowing()) {
            try {
                progressDialog.show();
            } catch (Exception e) {
            }
        }
    }

    public static void dismissProgress() {
        try {
            if ((progressDialog != null) && progressDialog.isShowing()) {
                Context context = ((ContextWrapper) progressDialog.getContext()).getBaseContext();
                if (context instanceof Activity) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                        if (!((Activity) context).isFinishing() && !((Activity) context).isDestroyed())
                            progressDialog.dismiss();
                    }else{
                        progressDialog.dismiss();
                    }
                } else
                    progressDialog.dismiss();
            }
        } catch (IllegalArgumentException e) {
            // Handle or log or ignore
        } catch (Throwable e) {
            // Handle or log or ignore
        } finally {
            progressDialog = null;
        }
    }
}
