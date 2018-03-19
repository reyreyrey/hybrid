package com.a01.protal;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.text.TextUtils;

import com.goldarmor.live800lib.live800sdk.manager.LIVManager;
import com.goldarmor.live800lib.live800sdk.request.LIVUserInfo;
import com.ivi.hybrid.core.Hybrid;
import com.ivi.hybrid.core.config.HybridConfig;
import com.ivi.hybrid.core.config.forward.ForwardMapModel;
import com.ivi.hybrid.core.config.menu.MenuModel;
import com.ivi.hybrid.core.config.theme.Themes;
import com.ivi.hybrid.core.net.models.UserModel;
import com.ivi.hybrid.core.update.UpdateTextModel;
import com.ivi.hybrid.ui.activitys.HybridWebViewActivity;
import com.ivi.hybrid.utils.activity_manager.ActivityManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * author: Rea.X
 * date: 2017/11/8.
 */

public class IHybridConfig extends HybridConfig {


    @Override
    public Map<String, ForwardMapModel> configForwardMap() {
        return null;
    }

    @Override
    public List<Themes> configThemes() {
        List<Themes> list = new ArrayList<>();
        Themes themes = new Themes();
        themes.setToolbarBackground(
                new ColorDrawable(Hybrid.get().getResources().getColor(R.color.status_bar_color)));
        themes.setTitleTextColor(Color.WHITE);
        themes.setStatusBarbackground(
                new ColorDrawable(Hybrid.get().getResources().getColor(R.color.colorPrimary)));
        themes.setNavigationBarColor(
                Hybrid.get().getResources().getColor(R.color.nativation_bar_color));
        themes.setDefault(true);
        themes.setBackIcon(R.drawable.ic_back);
        list.add(themes);
        return list;
    }

    @Override
    public List<MenuModel> configMenus() {
        return null;
    }

    @Override
    protected void onOpenVoiceCustomer(UserModel model) {
        LIVUserInfo userInfo = new LIVUserInfo();
        if (model != null && !TextUtils.isEmpty(model.getCustomer_id())) {
            String customerId = model.getCustomer_id();
            userInfo.setUserId(customerId);
        }
        LIVManager.getInstance().init(Hybrid.get());
        LIVManager.getInstance().setAuthorities(Hybrid.get().getResources().getString(R.string.AutoUpdateprovide));
        LIVManager.getInstance().startService(ActivityManager.getTagActivity(), userInfo, "", "10");
    }

    @Override
    protected String getLetterpageDetailAddress(String id) {
        return "customer/letter_detail.htm?id=" + id;
    }

    @Override
    protected Class<? extends HybridWebViewActivity> customerWebViewActivity() {
        return WebViewActivity.class;
    }

    @Override
    protected UpdateTextModel providerUpdateText(String s, String s1, boolean b) {
        return null;
    }
}
