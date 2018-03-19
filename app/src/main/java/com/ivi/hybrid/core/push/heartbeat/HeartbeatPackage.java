package com.ivi.hybrid.core.push.heartbeat;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;


import com.ivi.hybrid.core.config.Config;
import com.ivi.hybrid.core.modules.cache.PushPreferenceHelper;
import com.ivi.hybrid.core.net.tools.UserHelper;
import com.ivi.hybrid.core.push.utils.Utils;
import com.ivi.hybrid.utils.CommonUtils;
import com.ivi.hybrid.utils.LogUtil;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;

/**
 * author: Rea.X
 * date: 2017/3/10.
 */

public class HeartbeatPackage {
    //标志位(第一位：包是否结束   第二位：是否是新的请求(0新的)（是否带上uuid）   第三位：是否打开客户端（是否在线）)
    private byte flag;
    //包长
    private byte length;
    //包顺序
    private byte order;
    //android是1
    private byte client = 1;
    //uuid
    private byte[] uuidBytes;
    //用户id
    private byte[] iUserid;

    private Context mcontext;

    private byte[] progectIdByteArray;


    public HeartbeatPackage(Context context) {
        mcontext = context.getApplicationContext();
    }

    private void init() {
        iUserid = getUserId();
        uuidBytes = PushPreferenceHelper.getUUIDBytes();
        flag = getFlag();
        order = (byte) 1;
        progectIdByteArray = getProgectIdByte();
        length = getLength();
        LogUtil.d("TAG iUserid->"+ Utils.logByteArray(iUserid));
        LogUtil.d("TAG uuidBytes->"+Utils.logByteArray(uuidBytes));
        LogUtil.d("TAG flag->"+flag);
        LogUtil.d("TAG order->"+order);
        LogUtil.d("TAG progectIdByteArray->"+Utils.logByteArray(progectIdByteArray));
        LogUtil.d("TAG length->"+length);
    }

    //是否登录
    private byte[] getUserId() {
        String id = UserHelper.INSTANT.getUserId();
        if (TextUtils.isEmpty(id)) return new byte[]{0, 0, 0, 0};
        try {
            int d = Integer.parseInt(id);
            byte[] bs = Utils.intToByteArray2(d);
            return bs;
        } catch (Exception e) {
            return new byte[]{0, 0, 0, 0};
        }
    }

    private byte getFlag() {
        StringBuffer sb = new StringBuffer();
        sb.append("1");//心跳包默认是1
        sb.append(uuidBytes == null ? "0" : "1");
        boolean isAppIntheFont = CommonUtils.isAppInTheForeground(mcontext);
        LogUtil.d("isAppIntheFont->" + isAppIntheFont);
        sb.append(isAppIntheFont ? "1" : "0");
        sb.append("00000");
        return Utils.binaryStr2Byte(sb.toString());
    }

    private byte getLength() {
        byte length = 0;
        if (uuidBytes != null)
            length += uuidBytes.length;
        length += iUserid.length;
        if (uuidBytes == null) {
            length += progectIdByteArray.length;
            length += 1;
        }
        return length;
    }

    private byte[] getProgectIdByte() {
        String progectId = Config.getProjectId();
        try {
            return progectId.getBytes("utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
    }

    private int getPackageLength() {
        return 3 + getLength();
    }

    private byte[] initPackage() {
        ByteBuffer buffer = ByteBuffer.allocate(getPackageLength());
        buffer.put(flag);
        buffer.put(length);
        buffer.put(order);
        if (uuidBytes != null)
            buffer.put(uuidBytes);


        if (uuidBytes == null) {
            buffer.put(client);
        }
        buffer.put(iUserid);
        if (uuidBytes == null) {
            buffer.put(progectIdByteArray);
        }


        buffer.flip();
        return buffer.array();
    }

    long temp;

    public byte[] get() {
        temp = System.currentTimeMillis();
        init();
        byte[] bs = initPackage();
        LogUtil.d("生成心跳包耗时>>>>>>" + (System.currentTimeMillis() - temp) + "ms");
        return bs;
    }

    public boolean isHaveUUID() {
        uuidBytes = PushPreferenceHelper.getUUIDBytes();
        LogUtil.d(">>>>>>>" + uuidBytes);
        return uuidBytes != null;
    }
}
