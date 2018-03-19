# Hybrid框架

移动端Android APP Hybrid开发框架

## hotfix模块

简单封装[腾讯Tinker热修复框架](https://github.com/Tencent/tinker).

使用步骤：

1. 集成扩展com.ivi.hybrid.core.hotfix.TinkerAppLike
```java
//官方推荐使用注解的方式来生成Application子类
//@DefaultLifeCycle
@SuppressWarnings("unused")
@DefaultLifeCycle(application = "module.com.test.App", flags = ShareConstants.TINKER_ENABLE_ALL)
public class LikeApp extends TinkerAppLike {
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
    public void onCreate() {
        super.onCreate();
        Hybrid.init(APP, Hybrid.RUNTIME_MODEL, BuildConfig.DEBUG);
    }
	
	//TinkerAppLike顾名思义指类似Application.它代理了Application生命周期
	//所以，按照原本Application的思维，直接重写TinkerAppLike类方法即可
}
```

2. 参照并修改config/tinker_fix.gradle，主要修改Tinker_Id，修改backApk路径

3. 修改代码后，运行tinkerPatchRelease task生成补丁patch_signed_7zip.apk

4. 手动复制patch_signed_7zip.apk补丁到sdcard下，加载补丁
```java
    public static void applyPatch() {
        String patch = Environment.getExternalStorageDirectory().getAbsolutePath()
                + "/patch_signed_7zip.apk";
        File file = new File(patch);
        if (file.exists()) {
            TinkerInstaller.onReceiveUpgradePatch(LikeApp.APP, patch);
            Toast.makeText(LikeApp.APP, "Patch Success", Toast.LENGTH_LONG).show();
        }
    }
```

5. 重启APP，补丁生效。如果应用补丁失败，不会影响应用程序的正常使用，Tinker会自动清理现场环境