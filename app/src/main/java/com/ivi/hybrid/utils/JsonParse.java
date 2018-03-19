package com.ivi.hybrid.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.ivi.hybrid.core.net.models.ResultModel;
import com.ivi.hybrid.core.update.VersionModel;

import java.lang.reflect.Type;

/**
 * Created by Rea.X on 2017/2/1.
 */

public class JsonParse {
    private static final Gson parser = new GsonBuilder().serializeNulls().create();


    public static <T> T fromJson(String json, Class<T> cls) {
        try {
            return parser.fromJson(json, cls);
        } catch (Exception e) {

        }
        return null;
    }

    public static <T> ResultModel<T> fromJson(String json, TypeToken<ResultModel<T>> token) {
        try {
            return parser.fromJson(json, token.getType());
        } catch (Exception e) {

        }
        return null;
    }

    public static <T> T fromJson(String json, Type type) {
        try {
            return parser.fromJson(json, type);
        } catch (Exception e) {

        }
        return null;
    }

    public static String toJson(Object obj){
        try{
            return parser.toJson(obj);
        }catch (Exception e){}
        return null;
    }
}
