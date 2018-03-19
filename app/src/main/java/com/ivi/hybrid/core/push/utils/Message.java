package com.ivi.hybrid.core.push.utils;

import android.content.Context;
import android.support.annotation.Nullable;

import com.ivi.hybrid.core.push.helper.NotifyHelper;
import com.ivi.hybrid.core.push.helper.QueueHelper;
import com.ivi.hybrid.core.push.model.PushMessage;
import com.ivi.hybrid.core.push.model.ShowMessage;
import com.ivi.hybrid.utils.LogUtil;

import java.nio.ByteBuffer;

/**
 * author: Rea.X
 * date: 2017/3/10.
 */

public class Message {

    public static void parse(Context context, byte[] bs) {
        byte flag = bs[0];
        int length = bs[1] & 0xff;
        byte order = bs[2];
        byte[] flags = Utils.byte2byteArray(flag);
        boolean isFinsh = flags[0] == 1;

        PushMessage pushMessage = new PushMessage();
        pushMessage.setMessageOrder(order);

        byte[] nsgidByte = new byte[]{bs[3], bs[4], bs[5], bs[6]};
        ByteBuffer byteBuffer = ByteBuffer.wrap(nsgidByte);
        int messageID = byteBuffer.getInt();
        LogUtil.d("->1=" + messageID);
        messageID = Integer.reverseBytes(messageID);
        LogUtil.d("->2=" + messageID);

        byte[] messageByte;
        if (isFinsh) {
            //只有一条消息
            pushMessage.setMessageId(String.valueOf(messageID));
            pushMessage.setIsOnlgMessage(1);
            messageByte = getMessageBytes(bs, length, true);

        } else {
            pushMessage.setMessageId(String.valueOf(messageID));
            pushMessage.setIsOnlgMessage(0);
            messageByte = getMessageBytes(bs, length, false);
        }
        pushMessage.setMessageBytes(messageByte);
        pushMessage.setIsShowed(0);
        ShowMessage showMessage = QueueHelper.poll(pushMessage);
        if (showMessage != null) {
            NotifyHelper.showNotify(context, showMessage);
        }
    }

    @Nullable
    private static byte[] getMessageBytes(byte[] bs, int length, boolean isFinish) {
        if (length <= 0) return null;
        byte[] strsByte = null;
        if (isFinish) {
            //从第三位取
            LogUtil.d("从第三位去消息体-----------------------");
            strsByte = splitBytes(bs, 3, length);
        } else {
            //从第七位取
            LogUtil.d("从第七位去消息体-----------------------");
            strsByte = splitBytes(bs, 7, length - 4);
        }
        return strsByte;
    }

    private static byte[] splitBytes(byte[] bs, int offset, int length) {
        if (bs.length < (offset + length)) {
            throw new RuntimeException("数组长度太短");
        }
        if (length <= 0) return new byte[0];
        byte[] bytes = new byte[length];
        for (int i = offset; i < offset + length; i++) {
            bytes[i - offset] = bs[i];
        }
        return bytes;
    }

}
