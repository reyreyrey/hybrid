package com.a01.protal;

import android.support.annotation.IdRes;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.ivi.hybrid.core.net.tools.UserHelper;
import com.ivi.hybrid.core.webview.x5.PostWebView;
import com.ivi.hybrid.ui.activitys.HybridMainActivity;
import com.ivi.hybrid.ui.fragments.HybridWebViewFragment;

import static com.ivi.hybrid.core.call.CallModule.callInside;
import static com.ivi.hybrid.core.call.CallModule.callOutside;

public class MainActivity extends HybridMainActivity {
    private MainPagerAdapter adapter;
    private RadioGroup.LayoutParams params;
    private int lastClickPosition;
    private RadioButton loginButton;
    private static final int NEED_PRE_LOAD_COUNT = 3;

    public static final int INDEX_POSITION = 0;
    public static final int PROMOTION_POSITION = 1;
    public static final int MEMBER_POSITION = 3;
    public static final int NONE_POSITION = -1;

    @Override
    protected void needInit() {
        adapter = new MainPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);
        statusbarView.setBackgroundResource(R.drawable.ic_a01_status_bar_bg);
        params = new RadioGroup.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                1);
        initRadioGroup();
        radioGroup.setBackgroundColor(getResources().getColor(R.color.nativation_bar_color));
    }

    private void initRadioGroup() {
        radioGroup.addView(
                createRadioButton(
                        R.id.id_main,
                        R.drawable.tab_index,
                        getString(R.string.tab1),
                        true),
                params);
        loginButton = createRadioButton(
                R.id.id_login,
                R.drawable.tab_login,
                getString(R.string.tab2_no_login),
                false);
        radioGroup.addView(loginButton, params);
        radioGroup.addView(
                createRadioButton(
                        R.id.id_customer,
                        R.drawable.tab_customer,
                        getString(R.string.tab3),
                        false),
                params);
        radioGroup.addView(
                createRadioButton(
                        R.id.id_mine,
                        R.drawable.tab_user,
                        getString(R.string.tab4),
                        false),
                params);
        refreshLoginButton();
    }

    @Override
    public void onSplashDismiss() {
        super.onSplashDismiss();
        immersionBar.navigationBarColorInt(
                getResources().getColor(R.color.nativation_bar_color))
                .init();
    }

    @Override
    protected int needPreloadCount() {
        return NEED_PRE_LOAD_COUNT;
    }

    @Override
    public PostWebView getCurrentWebView() {
        HybridWebViewFragment fragment = (HybridWebViewFragment) adapter.getCurrentFragment();
        return fragment.getWebView();
    }

    @Override
    public void selectTab(String url) {
        try {
            int index = adapter.getIndexByUrl(url);
            View view = radioGroup.getChildAt(index);
            if (view instanceof RadioButton) {
                RadioButton button = (RadioButton) view;
                button.setChecked(true);
            }
        } catch (Throwable e) {
        }
    }

    @Override
    public void onLoginStatusChanged(boolean isLogin) {
        super.onLoginStatusChanged(isLogin);
        refreshLoginButton();
    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {
        switch (i) {
            case R.id.id_main:
                lastClickPosition = INDEX_POSITION;
                viewPager.setCurrentItem(0);
                break;
            case R.id.id_login:
                callInside("common/login.htm");
                ((RadioButton) radioGroup.getChildAt(lastClickPosition)).setChecked(true);
                break;
            case R.id.id_youhui:
                lastClickPosition = PROMOTION_POSITION;
                viewPager.setCurrentItem(1);
                break;
            case R.id.id_mine:
                lastClickPosition = MEMBER_POSITION;
                viewPager.setCurrentItem(2);
                break;
            case R.id.id_customer:
                callOutside(getString(R.string.customer_url));
                ((RadioButton) radioGroup.getChildAt(lastClickPosition)).setChecked(true);
                break;
            default:
                break;
        }
    }


    private RadioButton createRadioButton(int id, int icon, String text, boolean isChecked) {
        LayoutInflater inflater = LayoutInflater.from(context);
        RadioButton radioButton = (RadioButton) inflater.inflate(
                R.layout.radiobutton,
                null,
                false);
        radioButton.setId(id);
        radioButton.setChecked(isChecked);
        radioButton.setCompoundDrawablesWithIntrinsicBounds(0, icon, 0, 0);
        radioButton.setText(text);
        return radioButton;
    }

    private void refreshLoginButton() {
        if (UserHelper.INSTANT.isLogin()) {
            loginButton.setText(R.string.tab2);
            loginButton.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.tab_youhui, 0, 0);
            loginButton.setId(R.id.id_youhui);
        } else {
            loginButton.setText(R.string.tab2_no_login);
            loginButton.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.tab_login, 0, 0);
            loginButton.setId(R.id.id_login);
        }
    }
}
