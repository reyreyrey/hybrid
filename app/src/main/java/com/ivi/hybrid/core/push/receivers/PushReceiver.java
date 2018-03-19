package com.ivi.hybrid.core.push.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.View;

import com.ivi.hybrid.core.call.CallModule;
import com.ivi.hybrid.core.config.Config;
import com.ivi.hybrid.core.dialog.AcceptMsgDialog;
import com.ivi.hybrid.core.modules.forward.WebViewBundleModel;
import com.ivi.hybrid.core.push.utils.Cons;
import com.ivi.hybrid.core.push.utils.Utils;
import com.ivi.hybrid.ui.activitys.HybridWebViewActivity;
import com.ivi.hybrid.ui.badger.ShortcurBadgerTools;
import com.ivi.hybrid.utils.CommonUtils;

import static com.ivi.hybrid.core.cons.Cons.PrefrencesKeys.DOMAIN;
import static com.ivi.hybrid.utils.activity_manager.ActivityManager.getTagActivity;


/**
 * author: Rea.X
 * date: 2017/3/11.
 */

public class PushReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (action.equals(Intent.ACTION_BOOT_COMPLETED) || action.equals(Cons.ACTION_ALAMER)) {
            Utils.startService(context);
            return;
        }

        if (intent.getAction().equals(Cons.ACTION_CLICK)) {
            String id = intent.getStringExtra("id");
            String link = intent.getStringExtra("link");
            link = checkUrl(link);
            if (CommonUtils.isAppInTheForeground(context)) {
                //App在前台就直接跳转到消息详情界面
                if (id.equalsIgnoreCase("-1") && TextUtils.isEmpty(link)) return;
                if (!TextUtils.isEmpty(link))
                    HybridWebViewActivity.openWebView(new WebViewBundleModel(link, false, -1, false, -1), true);
                else
                    CallModule.callInside(Config.getLetterpageDetailAddress(id));
            } else {
                //App在后台，就先跳转到MainActivity,并将id传过去，MainActivity再判断id，并跳转到消息详情界面
                Intent in = new Intent(context, CommonUtils.getLaunchActivity());
                in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                in.putExtra("id", id);
                in.putExtra("link", link);
                context.startActivity(in);
            }
        } else if (intent.getAction().equals(Cons.ACTION_MESSAGE)) {
            //接收到消息
            String id = intent.getStringExtra("id");
            ShortcurBadgerTools.add(id);
        } else if (intent.getAction().equals(Cons.MESSAGE_DIALOG)) {
                FragmentActivity activity = (FragmentActivity) getTagActivity();
                String id = intent.getStringExtra("id");
                String link = intent.getStringExtra("link");
                String title = intent.getStringExtra("title");
                String content = intent.getStringExtra("content");
                getDialog(activity, link, id, title, content).show();
        }
    }

    private String checkUrl(String url) {
        if (TextUtils.isEmpty(url)) return "";
        if (!url.startsWith("http://") && !url.startsWith("https://")) {
            String domain = CallModule.invokeCacheModuleGet(DOMAIN);
            if (!TextUtils.isEmpty(domain) && domain.endsWith("/")) {
                domain = domain.substring(0, domain.length() - 1);
            }
            if (url.startsWith("/")) {
                url = url.substring(1);
            }
            url = domain + "/" + url;
        }
        return url;
    }

    private static AcceptMsgDialog getDialog(final Context context, final String link, final String id, String title, String content)  {
        final AcceptMsgDialog acceptMsgDialog = new AcceptMsgDialog(context);
        acceptMsgDialog.setTitle(title);
        acceptMsgDialog.setMsg(content);
        if (TextUtils.isEmpty(link) && id.equalsIgnoreCase("-1")) {
            //这个时候，只显示忽略按钮
            return acceptMsgDialog;
        }
        if (!TextUtils.isEmpty(link)) {
            acceptMsgDialog.setDetailsListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    HybridWebViewActivity.openWebView(new WebViewBundleModel(link, false, -1, false, -1), false);
                    acceptMsgDialog.dismiss();
                }
            });
            return acceptMsgDialog;
        }
        if (!id.equalsIgnoreCase("-1")) {
            acceptMsgDialog.setDetailsListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CallModule.callInside(Config.getLetterpageDetailAddress(id));
                    acceptMsgDialog.dismiss();
                }
            });
            return acceptMsgDialog;
        }
        return null;
    }
}
