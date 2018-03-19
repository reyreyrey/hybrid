package com.ivi.hybrid.core.log;

import android.content.Context;

import com.google.gson.annotations.SerializedName;
import com.ivi.hybrid.core.Hybrid;
import com.ivi.hybrid.core.domain.DomainHelper;
import com.ivi.hybrid.core.hotfix.Patcher;
import com.ivi.hybrid.core.net.models.UserModel;
import com.ivi.hybrid.core.net.tools.UserHelper;
import com.ivi.hybrid.utils.CommonUtils;
import com.ivi.hybrid.utils.DeviceUtils;
import com.ivi.hybrid.utils.VersionTools;
import com.ivi.hybrid.utils.activity_manager.ActivityManager;
import com.ivi.hybrid.utils.collectDevice.DeviceInfo;

import org.litepal.crud.DataSupport;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.ivi.hybrid.utils.MemoryUtils.getMemoryString;
import static com.ivi.hybrid.utils.NetWorkUtils.getAPNTypeString;
import static com.ivi.hybrid.utils.collectDevice.CollectDeviceInfoTools.getProvidersName;
import static com.ivi.hybrid.utils.collectDevice.DeviceInfo.getFirmwareVersion;
import static com.ivi.hybrid.utils.collectDevice.DeviceInfo.getPhoneModel;

public class StartupMessage extends DataSupport implements Serializable {
    private static final long serialVersionUID = 3074359059208898184L;

    @SerializedName("imei")
    private String imei;
    @SerializedName("deviceModels")
    private String deviceModels;
    @SerializedName("deviceOsVersion")
    private String deviceOsVersion;
    @SerializedName("appVersion")
    private String appVersion;
    @SerializedName("netEnv")
    private String netEnv;
    @SerializedName("loginName")
    private String loginName;
    @SerializedName("h5DeltaVersion")
    private String h5DeltaVersion;
    @SerializedName("hotfixVersion")
    private String hotfixVersion;
    @SerializedName("storage")
    private String storage;
    @SerializedName("macAddress")
    private String macAddress;
    @SerializedName("operators")
    private String operators;
    @SerializedName("channelId")
    private String channelId;
    @SerializedName("domain")
    private String domain;
    @SerializedName("startupTime")
    private Date startupTime;

    public Map<String, String> getParams() {
        Map<String, String> params = new HashMap<>();
        //imei
        params.put("device_imei", imei);
        //设备类型
        params.put("device_models", deviceModels);
        //设备版本
        params.put("device_os_version", deviceOsVersion);
        //app版本
        params.put("app_version", appVersion);
        //网络类型 3G 4G wifi
        params.put("net_env", netEnv);
        //登录名
        params.put("login_name", loginName);
        //H5增量包版本号
        params.put("h5_delta_version", h5DeltaVersion);
        //原生热修复版本号
        params.put("hotfix_version", hotfixVersion);
        //内存   手机总可用内存：2.73GB,app使用内存：73.67MB,剩余可用内存：556.50MB
        params.put("storage", storage);
        //mac地址
        params.put("mac_address", macAddress);
        //运营商  中国移动
        params.put("operators", operators);
        //渠道号
        params.put("channel_id", channelId);
        //domain
        params.put("domain", domain);
        //启动时间
        params.put("time", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(startupTime));
        return params;
    }

    public StartupMessage() {
        UserModel model = UserHelper.INSTANT.getUserModel();
        if (model != null) {
            this.loginName = model.getLogin_name();
        }
        this.appVersion = CommonUtils.getVersionNameOriginal(Hybrid.get());
        this.h5DeltaVersion = VersionTools.getDeltaVersion();
        this.hotfixVersion = Patcher.getPatcherVersion();
        DomainHelper.DomainIP domainIP = DomainHelper.getCanUsedDomain();
        if (domainIP != null) {
            this.domain = domainIP.domain;
        }
        this.netEnv = getAPNTypeString();
        this.storage = getMemoryString();
        this.imei = DeviceUtils.getDeviceId();
        this.deviceModels = getPhoneModel();
        this.deviceOsVersion = getFirmwareVersion();
        this.channelId = Hybrid.getChannelID();
        this.operators = getProvidersName();
        this.macAddress = DeviceInfo.getMac();
        this.startupTime = new Date();
    }
}
