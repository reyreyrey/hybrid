package com.ivi.hybrid.core.update;

import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;

import com.ivi.hybrid.core.call.CallModule;

import static com.ivi.hybrid.core.cons.Cons.PrefrencesKeys.IGNORE;
import static com.ivi.hybrid.core.cons.Cons.PrefrencesKeys.IGNORE_VERSION;


/**
 * Created by Rea.X on 2017/2/23.
 */

class AutoUpdateUtil {

    static void saveIgnore(boolean ignore) {
        CallModule.invokeCacheModuleSave(IGNORE, String.valueOf(ignore));
    }

    static boolean getIgnore() {
        String s = CallModule.invokeCacheModuleGet(IGNORE);
        if (TextUtils.isEmpty(s)) return false;
        return Boolean.parseBoolean(s);
    }

    static void saveIgnoreVersion(String ignoreVersion) {
        CallModule.invokeCacheModuleSave(IGNORE_VERSION, ignoreVersion);
    }

    static String getIgnoreVersion() {
        return CallModule.invokeCacheModuleGet(IGNORE_VERSION);
    }


}
