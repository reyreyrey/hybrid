package com.a01.protal;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.multidex.MultiDex;

import com.ivi.hybrid.core.Hybrid;
import com.ivi.hybrid.core.hotfix.TinkerAppLike;
import com.ivi.hybrid.gesture.config.DefaultViewConfig;
import com.ivi.hybrid.gesture.config.GestureConfig;
import com.ivi.hybrid.utils.CommonUtils;
import com.tencent.tinker.anno.DefaultLifeCycle;
import com.tencent.tinker.loader.shareutil.ShareConstants;

@SuppressWarnings("unused")
@DefaultLifeCycle(application = "com.a01.protal.App", flags = ShareConstants.TINKER_ENABLE_ALL)
public class LikeApp extends TinkerAppLike {

//        private static final int MODEL = BuildConfig.MODEL;
//  private static final int MODEL = Hybrid.LOCAL_MODEL;
//  private static final int MODEL = Hybrid.RUNTIME_MODEL;
    private static final int MODEL = Hybrid.RUNTIME_TEST_MODEL;

    public LikeApp(Application application,
                   int tinkerFlags,
                   boolean tinkerLoadVerifyFlag,
                   long applicationStartElapsedTime,
                   long applicationStartMillisTime,
                   Intent tinkerResultIntent) {
        super(application,
                tinkerFlags,
                tinkerLoadVerifyFlag,
                applicationStartElapsedTime,
                applicationStartMillisTime,
                tinkerResultIntent);
    }

    @Override
    public void onBaseContextAttached(Context base) {
        super.onBaseContextAttached(base);
        MultiDex.install(base);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        if (CommonUtils.isServicePid(getApplication())) {
            return;
        }

        Hybrid.init(getApplication(), MODEL, BuildConfig.DEBUG, new IHybridConfig());
//        if (!LeakCanary.isInAnalyzerProcess(getApplication())) {
//            LeakCanary.install(getApplication());
//            StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
//                    .detectAll()
//                    .penaltyLog()
//                    .penaltyDeath()
//                    .build());
//            StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
//                    .detectAll()
//                    .penaltyLog()
//                    .penaltyDeath()
//                    .build());
//        }
        GestureConfig config = new GestureConfig.Builder()
                .background(getApplication().getResources().getDrawable(com.hybrid.R.drawable.gesture_background))//整个界面背景色
                .enableVibrate(true)//设置是否震动
                .lineColor(Color.parseColor("#5FD624"))//连线的颜色，设置为透明色表示没有连线
                .nodeAnimation(0)//线连接到这个点的时候的动画效果，有预制的动画R.anim.gesture_node_on_1
                .viewConfig(new DefaultViewConfig())//设置顶部和底部的view,参考DefaultViewConfig
                .lineWidth((int) getApplication().getResources().getDimension(com.hybrid.R.dimen.linehight))//连线的宽度
                .maxTryCount(5)//最多错误次数
                .nodeSrcDrawable(getApplication().getResources().getDrawable(com.hybrid.R.mipmap.gesture_bg_choice_join_n))//正常状态下的圆圈图片
                .nodeOnSrcDrawable(getApplication().getResources().getDrawable(com.hybrid.R.mipmap.gesture_bg_choice_join_s))//连接状态下的圆圈图片
                .pwdMinLength(4)//最少连接的点
                .build();
        Hybrid.gesture(config);

    }
}
