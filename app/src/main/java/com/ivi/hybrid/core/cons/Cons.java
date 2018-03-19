package com.ivi.hybrid.core.cons;


import com.ivi.hybrid.core.Hybrid;
import com.ivi.hybrid.utils.CommonUtils;

import java.io.File;

/**
 * author: Rea.X
 * date: 2017/5/16.
 */

public class Cons {
    //网络超时时间
    public static final long TIMEOUT = 30 * 1000;
    //加载本地文件的判断依据
    public static final String LOCA_HEML_HEAD = "file://";
    public static final String FILE_URL_BEFORE = LOCA_HEML_HEAD + "/";
    //assets中压缩包的名字
    public static final String ZIP_NAME = "app.zip";
    //增量包下载到本地的名字
    public static final String UPDATE_ZIP_NAME = "update.zip";
    //给JS返回结果时需要执行的js代码


    public static final String ACTION_LOG_SERVICE = "com.rea.log.service";

    //保存用户信息的key
    public static final String CUSTOMER_KEY = "customer";


    public static final String CONFIG_PATH ;
    //html文件所在的文件夹
    public static final String HTML_FILE_PATH ;
    //复制到文件夹
    public static final String COPY_FILE_PATH ;
    //index.html文件所在
    public static final String INDEX_FILE_PATH ;

    public static final int TO_NEW_WEB = 1111;

    //c++层报错时，错误日志写入到这个文件夹中
    public static final String NATIVAL_CARSH_DIR ;

    public static final String CHECK_GAME_FLAG = "needCheckType";

    static {
        NATIVAL_CARSH_DIR = CommonUtils.getCacheDir(Hybrid.get()) + File.separator + "nativalCrash";
        CONFIG_PATH = CommonUtils.getCacheDir(Hybrid.get()) + File.separator + "app/__config/config.json";
        HTML_FILE_PATH = CommonUtils.getCacheDir(Hybrid.get()) + File.separator + "app" + File.separator + "__html" + File.separator;
        COPY_FILE_PATH = CommonUtils.getCacheDir(Hybrid.get()) + File.separator + "app" + File.separator;
        INDEX_FILE_PATH = HTML_FILE_PATH + "common/index.htm";
    }

    /**
     * Prefrences
     */
    public static class PrefrencesKeys {
        public static final String APP_TOKEN = "app_token";
        public static final String CDN = "cdnurl";
        public static final String DOMAIN = "domain";
        public static final String VERSION = "del_version";
        public static final String IGNORE = "ignore";
        public static final String IGNORE_VERSION = "ignore_version";
        public static final String LOCO_APP_VERSION = "loco_app_version";

        public static final String APP_ID = "app_id";

        public static final String CHANNEL_ID_JSON = "key_channel_id_json";
    }


    public static class NetMessage {
        public static final int MESSAGE_END = 1;
    }


    public static class Loading {
        public static final String SHOW = "show";
        public static final String HIDE = "hide";
    }

}
