package com.ivi.hybrid.core.log;

import android.support.annotation.IntDef;

import com.ivi.hybrid.core.Hybrid;
import com.ivi.hybrid.core.domain.DomainHelper;
import com.ivi.hybrid.core.hotfix.Patcher;
import com.ivi.hybrid.core.net.models.UserModel;
import com.ivi.hybrid.core.net.tools.UserHelper;
import com.ivi.hybrid.utils.CommonUtils;
import com.ivi.hybrid.utils.VersionTools;

import org.litepal.crud.DataSupport;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.HashMap;
import java.util.Map;

import static com.ivi.hybrid.utils.DeviceUtils.getDeviceInfo;
import static com.ivi.hybrid.utils.MemoryUtils.getMemoryString;
import static com.ivi.hybrid.utils.NetWorkUtils.getAPNTypeString;

/**
 * author: Rea.X
 * date: 2017/11/14.
 */

public class LogMessage extends DataSupport {
    public static final int LOG = 1;
    public static final int JS_ERROR = 2;
    public static final int UN_CAUGHT_EXCEPTION = 3;
    //错误信息
    private String message;
    //设备信息
    private String deviceInfo;
    //level
    private int level;
    //类型 1：log日志   2：jsError   3:unCaughtException
    private int type;
    //用户名（未登录为空）
    private String username;
    //app版本号
    private String version;
    //增量包版本号
    private String deltaVersion;
    //获取的域名
    private String domain;
    //网络环境
    private String netEnvironment;
    //内存使用
    private String storage;

    private String fileAbsolutePath;


    @Retention(RetentionPolicy.SOURCE)
    @IntDef({LOG, JS_ERROR, UN_CAUGHT_EXCEPTION})
    public @interface LogType {
    }

    public LogMessage() {
        this.deviceInfo = getDeviceInfo();
        UserModel model = UserHelper.INSTANT.getUserModel();
        if (model != null) {
            this.username = model.getLogin_name();
        }
        this.version = CommonUtils.getVersionNameOriginal(Hybrid.get());
        this.deltaVersion = "H5:" + VersionTools.getDeltaVersion() + "--hotfix:" + Patcher.getPatcherVersion();
        DomainHelper.DomainIP domainIP = DomainHelper.getCanUsedDomain();
        if (domainIP != null) {
            this.domain = domainIP.domain;
        }
        this.netEnvironment = getAPNTypeString();
        this.storage = getMemoryString();
    }

    public Map<String, String> getParams() {
        Map<String, String> params = new HashMap<>();
        params.put("detail", message.replaceAll("'", "''"));
        params.put("level", String.valueOf(level));
        params.put("deviceInfo", deviceInfo);
        params.put("type", String.valueOf(type));
        params.put("loginName", username);
        params.put("version", version);
        params.put("delta_version", deltaVersion);
        params.put("domain", domain);
        params.put("env", netEnvironment);
        params.put("storage", storage);
        return params;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public @LogType
    int getType() {
        return type;
    }

    public void setType(@LogType int type) {
        this.type = type;
    }

    public void setFileAbsolutePath(String path) {
        this.fileAbsolutePath = path;
    }

    public String getFileAbsolutePath() {
        return fileAbsolutePath;
    }

    @Override
    public String toString() {
        return "LogMessage{" +
                "message='" + message + '\'' +
                ", deviceInfo='" + deviceInfo + '\'' +
                ", level=" + level +
                ", type=" + type +
                ", username='" + username + '\'' +
                ", version='" + version + '\'' +
                ", deltaVersion='" + deltaVersion + '\'' +
                ", domain='" + domain + '\'' +
                ", netEnvironment='" + netEnvironment + '\'' +
                ", storage='" + storage + '\'' +
                '}';
    }
}
