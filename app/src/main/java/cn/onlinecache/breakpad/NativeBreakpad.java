package cn.onlinecache.breakpad;

import android.text.TextUtils;
import android.util.Log;

/**
 * 抓取jni层崩溃信息，写入到Android/data/packagename/file/下
 */
public class NativeBreakpad {
    private static String TAG = "NativeBreakpad";

    static boolean loadBreakpadSuccess = false;

    static {
        try {
            System.loadLibrary("breakpad");
            loadBreakpadSuccess = true;
        } catch (Exception e) {
            loadBreakpadSuccess = false;
            Log.e(TAG, "fail to load breakpad");
        }
    }

    /**
     * init breakpad
     * @param dumpFileDir the directory of dump file
     * @return true: init success  false: init fail
     */
    public static boolean init(String dumpFileDir){
        if (TextUtils.isEmpty(dumpFileDir)) {
            Log.e(TAG, "dumpFileDir can not be empty");
            return false;
        }
        if (loadBreakpadSuccess) {
            return nativeInit(dumpFileDir) > 0 ;
        }
        return false;
    }

    private static native int nativeInit(String dumpFileDir);


    /**
	 * don't use this method in your production app!!
	 */
	public static int testNativeCrash(){
		if (loadBreakpadSuccess){
            Log.d(TAG, "test native crash .......................");
			return nativeTestCrash();
		}
		return -1;
	}

  private static native int nativeTestCrash();


}
