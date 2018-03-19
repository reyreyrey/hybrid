package com.a01.protal;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.ivi.hybrid.ui.adapter.FragmentPagerAdapter2;
import com.ivi.hybrid.ui.fragments.HybridWebViewFragment;

import static com.a01.protal.MainActivity.INDEX_POSITION;
import static com.a01.protal.MainActivity.MEMBER_POSITION;
import static com.a01.protal.MainActivity.NONE_POSITION;
import static com.a01.protal.MainActivity.PROMOTION_POSITION;

/**
 * author: Rea.X
 * date: 2017/11/6.
 */

public class MainPagerAdapter extends FragmentPagerAdapter2 {
    public MainPagerAdapter(FragmentManager fm) {
        super(fm);
    }
    private static final int COUNT = 3;
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return HybridWebViewFragment.get("common/index.htm", true);
            case 1:
                return HybridWebViewFragment.get("promotions/promotion_list.htm", false);
            case 2:
                return HybridWebViewFragment.get("customer/member_center.htm", false);
                default:
                    return null;
        }
    }

    @Override
    public int getCount() {
        return COUNT;
    }

    int getIndexByUrl(String url) {
        if (url.equalsIgnoreCase("common/index.htm")) {
            return INDEX_POSITION;
        }
        if (url.equalsIgnoreCase("promotions/promotion_list.htm")) {
            return PROMOTION_POSITION;
        }
        if (url.equalsIgnoreCase("customer/member_center.htm")) {
            return MEMBER_POSITION;
        }
        return NONE_POSITION;
    }
}
