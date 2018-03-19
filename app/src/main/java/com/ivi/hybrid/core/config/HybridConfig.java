package com.ivi.hybrid.core.config;

import com.ivi.hybrid.core.config.forward.ForwardMapModel;
import com.ivi.hybrid.core.config.menu.MenuModel;
import com.ivi.hybrid.core.config.theme.Themes;
import com.ivi.hybrid.core.net.models.UserModel;
import com.ivi.hybrid.core.update.UpdateTextModel;
import com.ivi.hybrid.ui.activitys.HybridWebViewActivity;

import java.util.List;
import java.util.Map;

/**
 * author: Rea.X
 * date: 2017/11/8.
 */

public abstract class HybridConfig {

    /**
     * 本地类文件与html url的映射表
     * 当js加载的url与hash表的中key一致时，跳转到key对应的页面
     *
     * @return Map
     */
    protected abstract Map<String, ForwardMapModel> configForwardMap();

    /**
     * 配置预置主题列表，加载新页面时，js会传下标，从这个列表中拿出下标对应的主题作为新页面的主题
     *
     * @return
     */
    protected abstract List<Themes> configThemes();


    /**
     * 配置menu，加载新页面时，js会传下标，从这个列表中拿出下标对应的menu作为新页面的menu
     *
     * @return
     */
    protected abstract List<MenuModel> configMenus();

    /**
     * js调用语音客服时
     *
     * @param model
     */
    protected abstract void onOpenVoiceCustomer(UserModel model);

    /**
     * 返回站内信详情页面的相对地址,例如 common/letterpagedetail.htm?id=1
     *
     * @param id 站内信消息id
     * @return 站内信详情页面的相对地址
     */
    protected abstract String getLetterpageDetailAddress(String id);

    /**
     * 自定义的WebViewActivity
     * @return 自定义的WebViewActivity
     */
    protected abstract Class<? extends HybridWebViewActivity> customerWebViewActivity();

    /**
     * 提供更新文案
     * @param currentVersionName  当前app版本号
     * @param onlineVersionName 线上app版本号
     * @param isForce 是否强制更新
     * @return 更新文案model
     */
    protected abstract UpdateTextModel providerUpdateText(String currentVersionName, String onlineVersionName, boolean isForce);
}
