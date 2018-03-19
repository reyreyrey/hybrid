package com.ivi.hybrid.core.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.hybrid.R;


/**
 * Created by Fish.C on 2017/11/22.
 */

public class AcceptMsgDialog extends Dialog {
    private String title;
    private String message;
    private View.OnClickListener onDetailsListener;

    public AcceptMsgDialog(Context context) {
        this(context, R.style.AcceptDialog);
        //super(context);
    }

    private AcceptMsgDialog(Context context, int theme) {
        super(context, theme);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.accept_dialog);
        init();
    }

    private void init() {
        setCanceledOnTouchOutside(false);
        WindowManager m = getWindow().getWindowManager();
        Display d = m.getDefaultDisplay();
        WindowManager.LayoutParams p = getWindow().getAttributes();
        p.width = d.getWidth() - 100;
        getWindow().setAttributes(p);

        TextView textTitle = (TextView) findViewById(R.id.dialog_text_title);
        if (!TextUtils.isEmpty(title)) {
            textTitle.setVisibility(View.VISIBLE);
            textTitle.setText(title);
        } else {
            textTitle.setVisibility(View.GONE);
        }

        TextView textMsg = (TextView) findViewById(R.id.dialog_message);
        if (!TextUtils.isEmpty(message)) {
            textMsg.setVisibility(View.VISIBLE);
            textMsg.setText(message);
        } else {
            textMsg.setVisibility(View.GONE);
        }


        TextView textDetails = (TextView) findViewById(R.id.dialog_details);
        View line = findViewById(R.id.dialog_line2);
        if (onDetailsListener != null) {
            textDetails.setVisibility(View.VISIBLE);
            line.setVisibility(View.VISIBLE);
            textDetails.setOnClickListener(onDetailsListener);
        } else {
            textDetails.setVisibility(View.GONE);
            line.setVisibility(View.GONE);
            textDetails.setOnClickListener(null);
        }

        TextView cancel = (TextView) findViewById(R.id.dialog_text_cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setMsg(String message) {
        this.message = message;
    }



    /**
     * 详情按钮
     */
    public void setDetailsListener(View.OnClickListener onDetailsListener) {
        this.onDetailsListener = onDetailsListener;
    }
}

