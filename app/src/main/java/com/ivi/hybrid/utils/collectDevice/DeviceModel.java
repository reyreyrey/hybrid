package com.ivi.hybrid.utils.collectDevice;


import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 设备信息类
 */
public class DeviceModel implements Serializable {
    private static final long serialVersionUID = 3420553693849055378L;
    @SerializedName("phoneNumber")
    public String phoneNumber = "";//手机号码
    @SerializedName("board")
    public String board;//手机厂商
    @SerializedName("platform")
    public String platform = "";//手机型号
    @SerializedName("systemNameAndVersion")
    public String systemNameAndVersion = "";//手机操作系统类型及版本
    @SerializedName("macAddress")
    public String macAddress = "";//Mac地址
    @SerializedName("IMEINumber")
    public String IMEINumber = "";//IMEI号码
    @SerializedName("UUIDString")
    public String UUIDString = "";//UUID
    @SerializedName("carrier")
    public String carrier = "";//手机运行商
    @SerializedName("allAppList")
    public List<AppInfo> allAppList = null;//用户安装的所有app
    @SerializedName("appVersion")
    public String appVersion = "";//当前客户端版本
    @SerializedName("appKey")
    public String appKey = "";//当前渠道名称
    @SerializedName("downloadAppList")
    public List<AppInfo> downloadAppList = new ArrayList<AppInfo>();

    public static class AppInfo implements Serializable {
        private static final long serialVersionUID = 7103953958123305770L;
        @SerializedName("appName")
        public String appName; // 应用的名字
        @SerializedName("packageName")
        public String packageName; // 应用的包名
        @SerializedName("versionName")
        public String versionName; // 版本名字
        @SerializedName("versionCode")
        public String versionCode; // 版本号
        @SerializedName("tag")
        public int tag; // tag值

        public String getAppName() {
            return appName;
        }

        public void setAppName(String appName) {
            this.appName = appName;
        }

        public String getPackageName() {
            return packageName;
        }

        public void setPackageName(String packageName) {
            this.packageName = packageName;
        }

        public String getVersionName() {
            return versionName;
        }

        public void setVersionName(String versionName) {
            this.versionName = versionName;
        }

        public String getVersionCode() {
            return versionCode;
        }

        public void setVersionCode(String versionCode) {
            this.versionCode = versionCode;
        }

        public int getTag() {
            return tag;
        }

        public void setTag(int tag) {
            this.tag = tag;
        }
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public String getSystemNameAndVersion() {
        return systemNameAndVersion;
    }

    public void setSystemNameAndVersion(String systemNameAndVersion) {
        this.systemNameAndVersion = systemNameAndVersion;
    }

    public String getMacAddress() {
        return macAddress;
    }

    public void setMacAddress(String macAddress) {
        this.macAddress = macAddress;
    }

    public String getIMEINumber() {
        return IMEINumber;
    }

    public void setIMEINumber(String IMEINumber) {
        this.IMEINumber = IMEINumber;
    }

    public String getUUIDString() {
        return UUIDString;
    }

    public void setUUIDString(String UUIDString) {
        this.UUIDString = UUIDString;
    }

    public String getCarrier() {
        return carrier;
    }

    public void setCarrier(String carrier) {
        this.carrier = carrier;
    }

    public List<AppInfo> getAllAppList() {
        return allAppList;
    }

    public void setAllAppList(List<AppInfo> allAppList) {
        this.allAppList = allAppList;
    }

    public String getAppVersion() {
        return appVersion;
    }

    public void setAppVersion(String appVersion) {
        this.appVersion = appVersion;
    }

    public String getAppKey() {
        return appKey;
    }

    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }

    public List<AppInfo> getDownloadAppList() {
        return downloadAppList;
    }

    public void setDownloadAppList(List<AppInfo> downloadAppList) {
        this.downloadAppList = downloadAppList;
    }
}
