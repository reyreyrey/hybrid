package com.ivi.hybrid.ui.badger;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.ivi.hybrid.core.Hybrid;
import com.ivi.hybrid.core.call.CallModule;
import com.ivi.hybrid.utils.LogUtil;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import me.leolin.shortcutbadger.ShortcutBadger;


/**
 * author: Rea.X
 * date: 2017/5/5.
 */

public class ShortcurBadgerTools {

    private static final String UNREAD_MESSAGE = "unread_message";
    private static Set<String> unreadMessages;

    static {
        unreadMessages = new HashSet<>();
        init();
    }

    private static final String TAG = "ShortcurBadgerTools debug::";

    public static void updateShortcutBadger(Context context) {
        int count = unreadMessages.size();
        ShortcutBadger.applyCount(context, count);
        LogUtil.d(TAG + "updateShortcutBadger count->" + count);
    }

    public static void add(String messageId) {
        unreadMessages.add(messageId);
        updateShortcutBadger(Hybrid.get());
        sync();
    }

    public static void addAll(int num) {
        for (int i = 0; i < num; i++) {
            unreadMessages.add(UUID.randomUUID().toString());
        }
        updateShortcutBadger(Hybrid.get());
        sync();
    }


    public static void delete(String messageId) {
        if (unreadMessages.contains(messageId))
            unreadMessages.remove(messageId);
        updateShortcutBadger(Hybrid.get());
        sync();
    }

    public static int getUnReadMsgCount() {
        return unreadMessages.size();
    }


    public static void clear() {
        unreadMessages.clear();
        updateShortcutBadger(Hybrid.get());
    }

    private static void sync() {
        StringBuffer sb = new StringBuffer();
        for (String messageid : unreadMessages) {
            sb.append(messageid);
            sb.append(";");
        }
        String s = sb.toString().substring(0, sb.length() - 1);
        CallModule.invokeCacheModuleSave(UNREAD_MESSAGE, s);
    }

    private static void init() {
        String s = CallModule.invokeCacheModuleGet(UNREAD_MESSAGE);
        if (!TextUtils.isEmpty(s)) {
            if (s.contains(";")) {
                String[] ids = s.split(";");
                if (ids != null && ids.length > 0) {
                    unreadMessages.addAll(Arrays.asList(ids));
                }
            } else {
                unreadMessages.add(s);
            }
        }
    }
}
