package com.ivi.hybrid.gesture.utils.cache;

/**
 * author: Rea.X
 * date: 2018/1/30.
 */

interface IGestureCache {
    void saveString(String key, String value);

    String getString(String key);

    void saveInteger(String key, Integer value);

    Integer getInteger(String key);

    Integer getInteger(String key, Integer defaultValue);
}
