package com.ivi.hybrid.ui.progressDialog;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.widget.ImageView;

import com.hybrid.R;


/**
 * author: Rea.X
 * date: 2017/3/13.
 */

public class ApiProgressDialog extends ProgressDialog {
    private ImageView loading_img;
    private AnimationDrawable animationDrawable;

    public ApiProgressDialog(Context context) {
        super(context, R.style.ProgressDialogStyle);
    }

    public ApiProgressDialog(Context context, int theme) {
        super(context, R.style.ProgressDialogStyle);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setCanceledOnTouchOutside(false);
        setContentView(R.layout.hybrid_dialog_progress);
        loading_img = (ImageView) findViewById(R.id.loading_img);
        loading_img.setBackgroundResource(R.drawable.loading_annmation);
        animationDrawable = (AnimationDrawable) loading_img.getBackground();
    }

    @Override
    public void show() {
        super.show();
        animationDrawable.start();
    }

    @Override
    public void dismiss() {
        super.dismiss();
        animationDrawable.stop();
    }
}
