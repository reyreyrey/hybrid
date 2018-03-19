package com.ivi.hybrid.ui.activitys;

import android.support.v7.app.AppCompatActivity;

import com.flurry.android.FlurryAgent;
import com.ivi.hybrid.core.Hybrid;
import com.ivi.hybrid.core.config.Config;
import com.ivi.hybrid.utils.FlurryHelper;
import com.ivi.hybrid.utils.LogUtil;

/**
 * author: Rea.X
 * date: 2017/11/9.
 */

public class FlurryActivity extends AppCompatActivity {

    @Override
    protected void onStop() {
        super.onStop();
        FlurryHelper.INSTANT.onStop(this);
    }


    @Override
    protected void onStart() {
        super.onStart();
        FlurryHelper.INSTANT.onStart(this);
    }

}
