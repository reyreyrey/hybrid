
package com.ivi.hybrid.core.net.tools;


import com.ivi.hybrid.core.call.CallModule;
import com.ivi.hybrid.core.cons.Cons;
import com.ivi.hybrid.core.net.models.TokenModel;

import static com.ivi.hybrid.core.cons.Cons.PrefrencesKeys.APP_TOKEN;

/**
 * author: Rea.X
 * date: 2017/4/5.
 */

public enum AppTokenHelper {
    INSTANT;


    /**
     * 保存Token数据
     *
     * @param model
     */
    public synchronized void saveAppToken(TokenModel model) {
        CallModule.invokeCacheModuleSave(APP_TOKEN, model.getAppToken());
    }

    /**
     * 获取Token信息
     *
     * @return
     */
    public synchronized String getAppToken() {
        return CallModule.invokeCacheModuleGet(APP_TOKEN);
    }
}
