package com.ivi.hybrid.core.config;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorInt;
import android.support.annotation.LayoutRes;
import android.text.TextUtils;
import android.util.Log;

import com.hybrid.R;
import com.ivi.hybrid.core.Hybrid;
import com.ivi.hybrid.core.config.forward.ForwardMapModel;
import com.ivi.hybrid.core.config.menu.MenuModel;
import com.ivi.hybrid.core.config.theme.Themes;
import com.ivi.hybrid.core.exception.BridgeException;
import com.ivi.hybrid.core.net.models.UserModel;
import com.ivi.hybrid.core.update.UpdateTextModel;
import com.ivi.hybrid.ui.activitys.HybridWebViewActivity;
import com.ivi.hybrid.utils.ResourceUtil;

import org.w3c.dom.Text;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import static com.ivi.hybrid.core.Hybrid.LOCAL_MODEL;
import static com.ivi.hybrid.core.Hybrid.RUNTIME_MODEL;
import static com.ivi.hybrid.core.Hybrid.RUNTIME_TEST_MODEL;

/**
 * author: Rea.X
 * date: 2017/11/1.
 */

public class Config {
    private static Properties properties;
    private static final String CONFIG_FILE_NAME = "config.properties";

    static {
        readProperties();
    }

    private static boolean isTrue(String value) {
        return !TextUtils.isEmpty(value) && (value.equalsIgnoreCase("y") || value.equalsIgnoreCase("yes") || value.equalsIgnoreCase("true"));
    }

    private static void readProperties() {
        try {
            properties = new Properties();
            InputStream inputStream = Hybrid.getContext().getAssets().open(CONFIG_FILE_NAME);
            properties.load(inputStream);
        } catch (IOException e) {
            throw new BridgeException(Hybrid.getContext().getString(R.string.hybrid_no_config_file));
        }
    }

    private static String read(String key) {
        if (properties == null) readProperties();
        return properties.getProperty(key);
    }

    private static String getBaseurlByModel() {
        switch (Hybrid.getRunModel()) {
            case LOCAL_MODEL:
                return read(LOCAL_GATEWAY_API_URL);
            case RUNTIME_TEST_MODEL:
                return read(RUNTIME_TEST_GATEWAY_API_URL);
            case RUNTIME_MODEL:
                return read(RUNTIME_GATEWAY_API_URL);
        }
        throw new BridgeException(Hybrid.getContext().getString(R.string.hybrid_no_gateeay_api_url));
    }

    public static String[] getBaseurl() {
        String url = getBaseurlByModel();
        if (url.contains(";")) {
            return url.split(";");
        }
        return new String[]{url};
    }

    public static int getAppIcon() {
        return ResourceUtil.getMipmapId(Hybrid.get(), read(APP_ICON));
    }

    public static String getAppSign() {
        return read(APP_SIGN);
    }

    public static String getPackageName() {
        return read(PACKAGE_NAME);
    }

    public static String getPaltform() {
        return read(PALTFORM);
    }

    public static boolean isShowLineLoading() {
        return isTrue(read(SHOW_LINE_LOADING));
    }

    public static boolean isShowCircleLoading() {
        return isTrue(read(SHOW_CIRCLE_LOADING));
    }

    public static boolean isFroceOpenNewview() {
        return isTrue(read(FROCE_OPEN_NEWVIEW));
    }

    public static boolean isShowPushNotify() {
        return isTrue(read(IS_SHOW_NOTIFY));
    }

    public static
    @ColorInt
    int getColorPrimary() {
        String color = read(COLOR_PRIMARY);
        return Color.parseColor(color);
    }

    public static
    @LayoutRes
    int getSplashLayout() {
        String layout = read(SPLASH_LAYOUT);
        return ResourceUtil.getLayoutId(Hybrid.get(), layout);
    }

    public static String getProjectId() {
        return read(PROJECT_ID);
    }

    public static boolean isOpenPush() {
        return isTrue(read(IS_OPEN_PUSH));
    }

    public static String getPushIp() {
        switch (Hybrid.getRunModel()) {
            case LOCAL_MODEL:
                return read(PUSH_LOCAL_IP);
            case RUNTIME_TEST_MODEL:
                return read(PUSH_RUNTIME_TEST_IP);
            case RUNTIME_MODEL:
                return read(PUSH_RUNTIME_IP);
        }
        throw new BridgeException(Hybrid.getContext().getString(R.string.hybrid_no_gateeay_api_url));
    }

    public static int getPushPort() {
        switch (Hybrid.getRunModel()) {
            case LOCAL_MODEL:
                return Integer.parseInt(read(PUSH_LOCAL_PORT));
            case RUNTIME_TEST_MODEL:
                return Integer.parseInt(read(PUSH_RUNTIME_TEST_PORT));
            case RUNTIME_MODEL:
                return Integer.parseInt(read(PUSH_RUNTIME_PORT));
        }
        throw new BridgeException(Hybrid.getContext().getString(R.string.hybrid_no_gateeay_api_url));
    }


    public static Themes getTheme(int index) {
        List<Themes> themes = Hybrid.getConfig().configThemes();
        if (themes == null || themes.size() == 0)
            throw new BridgeException(Hybrid.get().getString(R.string.hybrid_error_need_config_theme));
        if (index >= 0 && index < themes.size()) {
            return themes.get(index);
        } else {
            for (Themes t : themes) {
                if (t.isDefault()) return t;
            }
        }
        return null;
    }

    public static MenuModel getMenu(int index) {
        List<MenuModel> menuModels = Hybrid.getConfig().configMenus();
        if (menuModels == null || menuModels.size() == 0) return null;
        if (index >= 0 && index < menuModels.size()) {
            return menuModels.get(index);
        }
        return null;
    }

    public static ForwardMapModel isNativalPage(String url) {
        Map<String, ForwardMapModel> map = Hybrid.getConfig().configForwardMap();
        if (map == null || map.size() == 0) return null;
        if (map.containsKey(url))
            return map.get(url);
        return null;
    }

    public static UpdateTextModel getTextModel(String currentVersionName, String onlineVersionName, boolean isForce) {
        UpdateTextModel updateTextModel = Hybrid.getConfig().providerUpdateText(currentVersionName, onlineVersionName, isForce);
        return updateTextModel;
    }

    public static Class<? extends HybridWebViewActivity> getCustomerWebViewActivity(){
        return Hybrid.getConfig().customerWebViewActivity();
    }

    public static void live800(UserModel model) {
        Hybrid.getConfig().onOpenVoiceCustomer(model);
    }

    public static boolean isOpenAreaLimit() {
        return isTrue(read(AREA_LIMIT));
    }

    public static boolean isOpenGesture() {
        return isTrue(read(OPEN_GESTURE));
    }

    public static String getLetterpageDetailAddress(String id) {
        return Hybrid.getConfig().getLetterpageDetailAddress(id);
    }

    public static String getFlurryKey() {
        return read(FLURRY_KEY);
    }

    public static boolean isOpenFlurry() {
        return isTrue(read(IS_OPEN_FLURRY));
    }

    public static boolean isOpenDNS() {
        return isTrue(read(OPEN_DNS));
    }

    public static boolean isAcceptDialog() {
        return isTrue(read(ACCEPT_DIALOG));
    }

    /////////////////////////////////////////////常量KEY
    //本地接口地址
    private static final String LOCAL_GATEWAY_API_URL = "local_gateway_api_url";
    //运测接口地址
    private static final String RUNTIME_TEST_GATEWAY_API_URL = "runtime_test_gateway_api_url";
    //运营接口地址
    private static final String RUNTIME_GATEWAY_API_URL = "runtime_gateway_api_url";
    //网络请求参数
    private static final String APP_SIGN = "app_sign";
    private static final String PACKAGE_NAME = "package_name";
    private static final String PALTFORM = "platform";
    //webview相关
    private static final String SHOW_LINE_LOADING = "showLineLoading";
    private static final String SHOW_CIRCLE_LOADING = "showCircleLoading";
    //是否支持newView=true是开启新的Activity
    private static final String FROCE_OPEN_NEWVIEW = "froceOpenNewView";
    //主题色
    private static final String COLOR_PRIMARY = "colorPrimary";
    //启动页布局
    private static final String SPLASH_LAYOUT = "splash_layout";
    //推送配置
    private static final String PUSH_LOCAL_IP = "push_local_ip";
    private static final String PUSH_LOCAL_PORT = "push_local_port";
    private static final String PUSH_RUNTIME_TEST_IP = "push_runtime_test_ip";
    private static final String PUSH_RUNTIME_TEST_PORT = "push_runtime_test_port";
    private static final String PUSH_RUNTIME_IP = "push_runtime_ip";
    private static final String PUSH_RUNTIME_PORT = "push_runtime_port";
    //项目id
    private static final String PROJECT_ID = "project_id";
    //是否开启推送
    private static final String IS_OPEN_PUSH = "is_open_push";
    //推送是否显示在通知栏
    private static final String IS_SHOW_NOTIFY = "is_show_push_notify";
    //app图标
    private static final String APP_ICON = "app_icon";
    //flurry统计
    private static final String FLURRY_KEY = "flurry_key";
    private static final String IS_OPEN_FLURRY = "is_open_flurry";
    //地域限制
    private static final String AREA_LIMIT = "is_open_areaLimit";
    //是否开启DNS
    private static final String OPEN_DNS = "is_open_dns";
    //收到推送弹出dialog
    private static final String ACCEPT_DIALOG = "is_accept_dialog";
    //是否开启手势
    private static final String OPEN_GESTURE = "open_gesture";
}
