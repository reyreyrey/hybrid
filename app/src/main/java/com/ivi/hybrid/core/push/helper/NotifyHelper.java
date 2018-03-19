package com.ivi.hybrid.core.push.helper;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.hybrid.R;
import com.ivi.hybrid.core.config.Config;
import com.ivi.hybrid.core.push.model.ShowMessage;
import com.ivi.hybrid.utils.CommonUtils;
import com.ivi.hybrid.utils.LogUtil;
import com.ivi.hybrid.utils.notify.NotifyUtil;

import static com.ivi.hybrid.core.push.utils.Cons.ACTION_CLICK;
import static com.ivi.hybrid.core.push.utils.Cons.ACTION_MESSAGE;
import static com.ivi.hybrid.core.push.utils.Cons.MESSAGE_DIALOG;

/**
 * author: Rea.X
 * date: 2017/3/11.
 */

public class NotifyHelper {

    private static int messageid;

    public static int showNotify(Context context, ShowMessage showMessage) {
        String message = showMessage.getMessage();
        LogUtil.d("推送消息-_" + message);
        if (TextUtils.isEmpty(message)) return -1;
        String id = "";
        String title = "    ";
        String content = "";
        String link = "";
        if (message.contains("|")) {
            String[] strs = message.split("\\|");
            if (strs.length < 3) return -1;
            id = strs[0];
            title = strs[1];
            content = strs[2];
            if (strs.length >= 4) {
                link = strs[3];
            }
        } else {
            content = message;
        }
        distri(context, id, title, content, link);
        QueueHelper.clearMessage(showMessage.getMessageid());
        return messageid;
    }

    private static void distri(Context context, String id, String title, String content, String link) {
        sendMessage(context, id, title, content, link);
        if (Config.isShowPushNotify()) {
            showNotify(context, id, title, content, link);
        }
    }

    /**
     * 发送透传消息
     *
     * @param context
     * @param id
     * @param title
     * @param content
     */
    private static void sendMessage(Context context, String id, String title, String content, String link) {
        Intent intent = new Intent();
        intent.setAction(ACTION_MESSAGE);
        intent.setPackage(context.getPackageName());
        intent.putExtra("id", id);
        intent.putExtra("title", title);
        intent.putExtra("content", content);
        intent.putExtra("link", link);
        context.sendBroadcast(intent);
    }


    private static void showNotify(Context context, String id, String title, String content, String link) {
        if (Config.isAcceptDialog() && CommonUtils.isAppInTheForeground(context)) {
            Intent intent1 = new Intent();
            intent1.setAction(MESSAGE_DIALOG);
            intent1.setPackage(context.getPackageName());
            intent1.putExtra("id", id);
            intent1.putExtra("title", title);
            intent1.putExtra("content", content);
            intent1.putExtra("link", link);
            context.sendBroadcast(intent1);
            return;
        }
        //设置想要展示的数据内容
        Intent intent = new Intent(ACTION_CLICK);
        intent.setPackage(context.getPackageName());
        intent.putExtra("id", id);
        intent.putExtra("title", title);
        intent.putExtra("content", content);
        intent.putExtra("link", link);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        int icon = Config.getAppIcon();

        //实例化工具类，并且调用接口
        NotifyUtil notify1 = new NotifyUtil(context, (int) (Math.random() * 10000));
        notify1.notify_normal_singline(pendingIntent, icon, context.getString(R.string.hybrid_push_newmessage), title, content, true, true, true);
    }
}
