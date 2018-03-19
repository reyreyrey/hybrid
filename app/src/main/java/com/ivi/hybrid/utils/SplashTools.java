package com.ivi.hybrid.utils;

import android.content.res.AssetManager;

import com.ivi.hybrid.core.Hybrid;
import com.ivi.hybrid.core.config.Config;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import static com.ivi.hybrid.core.cons.Cons.COPY_FILE_PATH;
import static com.ivi.hybrid.core.cons.Cons.HTML_FILE_PATH;
import static com.ivi.hybrid.core.cons.Cons.INDEX_FILE_PATH;
import static com.ivi.hybrid.core.cons.Cons.ZIP_NAME;

/**
 * author: Rea.X
 * date: 2017/11/3.
 */

public class SplashTools {

    public static File copy() throws IOException {
        AssetManager assetManager = Hybrid.get().getAssets();
        InputStream stream = assetManager.open(ZIP_NAME);
        File file = new File(HTML_FILE_PATH);
        if (!file.exists()) file.mkdirs();
        else {
            file = new File(INDEX_FILE_PATH);
            if (Hybrid.isDebug()) {
                deleteFolder(HTML_FILE_PATH);
                new File(HTML_FILE_PATH).mkdirs();
            }
            if (!checkAppVersion()) {
                //先删除
                deleteFolder(HTML_FILE_PATH);
                new File(HTML_FILE_PATH).mkdirs();
            }
            if (file.exists()) {
                //index文件存在就不需要再复制了
                BackConfigHelper.initConfig();
                return null;
            }
        }
        file = new File(COPY_FILE_PATH, ZIP_NAME);
        FileOutputStream outputStream = new FileOutputStream(file);
        byte[] buffer = new byte[1024];
        int len;
        while ((len = stream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, len);
        }
        stream.close();
        outputStream.flush();
        outputStream.close();
        return file;
    }


    /**
     * 删除文件夹
     *
     * @param path
     */
    private static void deleteFolder(String path) {
        File f = new File(path);
        if (f.isDirectory()) {//如果是目录，先递归删除
            String[] list = f.list();
            if (list != null && list.length != 0) {
                for (int i = 0; i < list.length; i++) {
                    deleteFolder(path + "//" + list[i]);//先删除目录下的文件
                }
            }
        }
        try {
            f.delete();
        } catch (RuntimeException e) {
            System.out.println("RuntimeException");
        }
    }


    private static boolean checkAppVersion() {
        String locolVersion = VersionTools.getAppVersion();
        return locolVersion.equals(CommonUtils.getVersionName(Hybrid.get()));
    }
}
