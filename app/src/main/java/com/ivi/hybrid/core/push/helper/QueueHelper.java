package com.ivi.hybrid.core.push.helper;


import com.ivi.hybrid.core.push.model.PushMessage;
import com.ivi.hybrid.core.push.model.ShowMessage;
import com.ivi.hybrid.utils.LogUtil;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

/**
 * author: Rea.X
 * date: 2017/3/11.
 */

public class QueueHelper {
    private static Map<String, ArrayList<PushMessage>> queues = new HashMap<>();

    /**
     * 放入一个消息
     *
     * @param message
     */
    public static ShowMessage poll(PushMessage message) {
        if (message.getIsOnlgMessage() == 1) {
            ShowMessage showMessage = new ShowMessage();
            showMessage.setMessageid(message.getMessageId());
            showMessage.setMessage(getString(message.getMessageBytes()));
            return showMessage;
        }
        String messageid = message.getMessageId();
        ArrayList<PushMessage> arrayList;
        if (queues.containsKey(messageid)) {
            arrayList = queues.get(messageid);
        } else {
            arrayList = new ArrayList<>();
        }
        arrayList.add(message);
        queues.put(messageid, arrayList);
        if (check(message, arrayList)) {
            return getShowMessage(arrayList);
        }
        return null;
    }

    private static String getString(byte[] bs) {
        try {
            String s = new String(bs, "UTF-8");
            LogUtil.d("--------->s----"+s);
            return s;
        } catch (Exception e) {
        }
        return null;
    }

    /**
     * 消息显示之后要删除掉队列中的这条记录
     *
     * @param messageid
     */
    public static void clearMessage(String messageid) {
        if (queues.containsKey(messageid)) {
            queues.remove(messageid);
        }
    }

    /**
     * 判定包是否接收完了
     *
     * @param message
     * @param arrayList
     * @return
     */
    private static boolean check(PushMessage message, ArrayList<PushMessage> arrayList) {
        if (message.getFinish() == 1) {
            int packageNumber = message.getMessageOrder();//获取一共有多少个包
            if (arrayList.size() == packageNumber) {
                return true;
            }
        }
        return false;
    }

    /**
     * 获取一条显示的信息
     *
     * @param arrayList
     * @return
     */
    private static ShowMessage getShowMessage(ArrayList<PushMessage> arrayList) {
        Collections.sort(arrayList, new Comparator<PushMessage>() {
            @Override
            public int compare(PushMessage pushMessage, PushMessage t1) {
                Integer integer = Integer.valueOf(t1.getMessageOrder());
                return Integer.valueOf(pushMessage.getMessageOrder()).compareTo(integer);
            }
        });
        ShowMessage showMessage = new ShowMessage();
        String messageid = null;
        int length = 0;
        for (PushMessage m : arrayList) {
            if (m.getMessageBytes() != null)
                length += m.getMessageBytes().length;
        }
        ByteBuffer byteBuffer = ByteBuffer.allocate(length);
        for (PushMessage m : arrayList) {
            messageid = m.getMessageId();
            if (m.getMessageBytes() != null)
                byteBuffer.put(m.getMessageBytes());
        }
        byteBuffer.flip();
        showMessage.setMessage(getString(byteBuffer.array()));
        showMessage.setMessageid(messageid);
        return showMessage;
    }
}
