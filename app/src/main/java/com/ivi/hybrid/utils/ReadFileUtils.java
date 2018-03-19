package com.ivi.hybrid.utils;

import android.util.Base64;

import com.ivi.hybrid.core.log.LogMessage;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.logging.LogManager;

import static com.ivi.hybrid.core.log.LogMessage.UN_CAUGHT_EXCEPTION;

/**
 * Created by Fish.C on 2017/12/1.
 */

public class ReadFileUtils {
    public static void find(String pathName, List<LogMessage> messages) throws IOException {
        File dirFile = new File(pathName);
        if (!dirFile.exists()) {
            return;
        }
        String[] fileList = dirFile.list();
        for (int i = 0; i < fileList.length; i++) {
            String string = fileList[i];
            File file = new File(dirFile.getPath(), string);
            if (file.isDirectory()) {
                find(file.getCanonicalPath(), messages);
            } else {
                String result = fileChangBetyArry(file.getAbsolutePath());
                if (null != result) {
                    LogMessage m = new LogMessage();
                    m.setType(UN_CAUGHT_EXCEPTION);
                    m.setMessage(result);
                    m.setFileAbsolutePath(file.getAbsolutePath());
                    messages.add(m);
                }
            }
        }
    }

    private static String fileChangBetyArry(String filename) {
        FileInputStream fis = null;
        BufferedInputStream bis = null;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            fis = new FileInputStream(filename);
            bis = new BufferedInputStream(fis);
            int len = -1;
            byte[] buf = new byte[1024];
            while ((len = bis.read(buf)) != -1) {
                baos.write(buf, 0, len);
            }
            byte[] buffer = baos.toByteArray();
            String encode = Base64.encodeToString(buffer, Base64.NO_WRAP);
            return encode;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            close(baos, bis, fis);
        }
        return null;
    }

    private static void close(Closeable... closeable) {
        if (null != closeable) {
            for (Closeable closeable1 : closeable) {
                try {
                    closeable1.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
