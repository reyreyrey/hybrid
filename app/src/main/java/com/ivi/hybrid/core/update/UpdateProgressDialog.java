package com.ivi.hybrid.core.update;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.DialogFragment2;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.hybrid.R;
import com.ivi.hybrid.core.Hybrid;
import com.ivi.hybrid.core.config.Config;
import com.ivi.hybrid.core.config.HybridConfig;
import com.ivi.hybrid.utils.CommonUtils;
import com.ivi.hybrid.utils.activity_manager.ActivityManager;

import static com.ivi.hybrid.utils.CommonUtils.fromatSize;


/**
 * Created by Rea.X on 2017/2/15.
 */

public class UpdateProgressDialog extends DialogFragment2 implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    public static final String FORCE_UPDATE = "isForceUpdate";
    public static final String NO_FORCE_UPDATE = "noForceUpdate";

    //下载文件大小及总大小
    private TextView tv_size, tv_content, tv_progress;
    private CheckBox checkbox;
    private ProgressBar progressbar;
    private ImageView img_close;
    private Button btn_cancel, btn_update;
    private ProgressReceiver progressReceiver;
    private boolean isForceUpdate = false;
    private VersionModel model;

    private View viewNotifyUpdate, viewUpdateing;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_progressbar, null);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        initView(view);
        return view;
    }

    private void initView(View view) {
        viewNotifyUpdate = view.findViewById(R.id.viewNotifyUpdate);
        viewUpdateing = view.findViewById(R.id.viewUpdateing);
        tv_size = (TextView) view.findViewById(R.id.tv_size);
        tv_content = (TextView) view.findViewById(R.id.tv_content);
        tv_progress = (TextView) view.findViewById(R.id.tv_progress);
        checkbox = (CheckBox) view.findViewById(R.id.checkbox);
        checkbox.setOnCheckedChangeListener(this);
        progressbar = (ProgressBar) view.findViewById(R.id.progressbar);
        img_close = (ImageView) view.findViewById(R.id.img_close);
        btn_cancel = (Button) view.findViewById(R.id.btn_cancel);
        btn_update = (Button) view.findViewById(R.id.btn_update);
        btn_cancel.setOnClickListener(this);
        btn_update.setOnClickListener(this);
        img_close.setOnClickListener(this);
        init();
    }

    private void init() {
        viewNotifyUpdate.setVisibility(View.VISIBLE);
        viewUpdateing.setVisibility(View.GONE);
        UpdateTextModel updateTextModel;
        if (isForceUpdate) {
            if (model != null) {
                updateTextModel = Config.getTextModel(CommonUtils.getVersionName(getContext()), model.getVersion_num(), isForceUpdate);
                if (updateTextModel != null && !TextUtils.isEmpty(updateTextModel.getForceUpdateText())) {
                    tv_content.setText(updateTextModel.getForceUpdateText());
                } else {
                    tv_content.setText(String.format(getContext().getString(R.string.update_force_text), model.getVersion_num()));
                }
            }

            btn_cancel.setVisibility(View.GONE);
            checkbox.setVisibility(View.GONE);
        } else {
            if (model != null) {
                updateTextModel = Config.getTextModel(CommonUtils.getVersionName(getContext()), model.getVersion_num(), isForceUpdate);
                if (updateTextModel != null && !TextUtils.isEmpty(updateTextModel.getNormalUpdateText())) {
                    tv_content.setText(updateTextModel.getNormalUpdateText());
                } else {
                    tv_content.setText(String.format(getContext().getString(R.string.update_nomal_text), model.getVersion_num(), CommonUtils.getVersionName(getContext())));
                }
            }
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, android.R.style.Theme_Black_NoTitleBar);
        progressReceiver = new ProgressReceiver();
        IntentFilter intentFilter = new IntentFilter("downloadProgress");
        intentFilter.addAction("downloadError");
        intentFilter.addAction("dismiss");
        getContext().registerReceiver(progressReceiver, intentFilter);
    }

    @Override
    public void show(FragmentManager manager, String tag) {
        super.show(manager, tag);
        if (tag.equals(FORCE_UPDATE)) {
            isForceUpdate = true;
        } else if (tag.equals(NO_FORCE_UPDATE)) {
            isForceUpdate = false;
        }
        setCancelable(!isForceUpdate);
    }

    public void setData(VersionModel model) {
        this.model = model;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getContext().unregisterReceiver(progressReceiver);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btn_cancel) {
            dismissAllowingStateLoss();
        } else if (view.getId() == R.id.btn_update) {
            try {
                if (model != null) {
                    DownloadAppService.downloadAppAndInstall(getContext(), model.getSrc(), false);
                }
            } catch (Throwable e) {
                if (model != null && model.isIs_update()) {
                    //如果强制升级，点击取消下载安装的话，就直接退出app
                    ActivityManager.exitApp();
                }
            }

            viewUpdateing.setVisibility(View.VISIBLE);
            viewNotifyUpdate.setVisibility(View.GONE);
        } else if (view.getId() == R.id.img_close) {
            dismissAllowingStateLoss();
            if (model != null && model.isIs_update()) {
                //如果强制升级，点击取消下载安装的话，就直接退出app
                ActivityManager.exitApp();
            }
        }
    }

    private void refresh(long currentSize, long totalSize, int progress) {
        String current = fromatSize(currentSize);
        String totle = fromatSize(totalSize);
        tv_size.setText(String.format(getContext().getString(R.string.update_size), current, totle));
        progressbar.setMax(100);
        progressbar.setProgress(progress);
        tv_progress.setText(progress + "%");

    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        AutoUpdateUtil.saveIgnore(b);
        if (model != null) {
            AutoUpdateUtil.saveIgnoreVersion(model.getVersion_num());
        }
    }


    private class ProgressReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            try {
                String action = intent.getAction();
                if (action.equals("downloadError")) {
                    dismissAllowingStateLoss();
                    if (model != null && model.isIs_update()) {
                        ActivityManager.exitApp();
                    }
                    return;
                }
                if (action.equals("dismiss")) {
                    dismissAllowingStateLoss();
                    if (model != null && model.isIs_update()) {
                        ActivityManager.exitApp();
                    }
                    return;
                }
                long currentSize = intent.getLongExtra("currentSize", 0);
                long totalSize = intent.getLongExtra("totalSize", 0);
                int progress = intent.getIntExtra("progress", 0);
                if (progress != 100)
                    refresh(currentSize, totalSize, progress);
                else {
                    dismissAllowingStateLoss();
                }
            } catch (Throwable e) {
            }
        }
    }
}
