package com.ivi.hybrid.core.net.tools;

import android.text.TextUtils;

import com.google.gson.reflect.TypeToken;
import com.ivi.hybrid.core.call.CallModule;
import com.ivi.hybrid.core.net.models.ResultModel;
import com.ivi.hybrid.core.net.models.UserModel;
import com.ivi.hybrid.core.webview.x5.tools.WebTools;
import com.ivi.hybrid.utils.Cookie;
import com.ivi.hybrid.utils.JsonParse;

import static com.ivi.hybrid.core.cons.Cons.CUSTOMER_KEY;


/**
 * author: Rea.X
 * date: 2017/4/5.
 */

public enum UserHelper {
    INSTANT;


    public synchronized UserModel getUserModel() {
        String result = CallModule.invokeCacheModuleGet(CUSTOMER_KEY);
        if (TextUtils.isEmpty(result)) return null;
        UserModel resultModel = JsonParse.fromJson(result, new TypeToken<UserModel>() {
        }.getType());
        if (resultModel == null) return null;
        return resultModel;
    }

    /**
     * 获取Token信息
     *
     * @return
     */
    public synchronized String getUserToken() {
        UserModel model = getUserModel();
        if (model == null) return "";
        try {
            return model.getUser_token();
        } catch (Exception e) {
            return "";
        }
    }

    public synchronized String getLoginName() {
        UserModel model = getUserModel();
        try {
            return model.getLogin_name();
        } catch (Exception e) {
            return "";
        }
    }

    public synchronized String getUserId() {
        UserModel model = getUserModel();
        try {
            return model.getCustomer_id();
        } catch (Exception e) {
            return "";
        }
    }

    public synchronized String getPwd() {
        UserModel model = getUserModel();
        try {
            return model.getPwd();
        } catch (Exception e) {
            return "";
        }
    }

    public synchronized boolean isLogin() {
        UserModel model = getUserModel();
        try {
            return model != null && !TextUtils.isEmpty(model.getUser_token());
        } catch (Exception e) {
        }
        return false;
    }

    public synchronized void loginOut(){
        CallModule.invokeCacheModuleSave(CUSTOMER_KEY, "");
        Cookie.clearCookie();
    }
}
