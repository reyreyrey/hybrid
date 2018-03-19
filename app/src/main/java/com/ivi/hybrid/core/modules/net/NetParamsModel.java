package com.ivi.hybrid.core.modules.net;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Map;

/**
 * author: Rea.X
 * date: 2017/5/16.
 */

public class NetParamsModel implements Serializable {
    private static final long serialVersionUID = -6982116088196436182L;
    @SerializedName("apiUrl")
    private String apiUrl;
    @SerializedName("params")
    private Map<String, String> params;
    @SerializedName("loading")
    private boolean isShowLoading = true;

    public String getApiUrl() {
        return apiUrl;
    }

    public void setApiUrl(String apiUrl) {
        this.apiUrl = apiUrl;
    }

    public Map<String, String> getParams() {
        return params;
    }

    public void setParams(Map<String, String> params) {
        this.params = params;
    }

    public boolean isShowLoading() {
        return isShowLoading;
    }

    public void setShowLoading(boolean showLoading) {
        isShowLoading = showLoading;
    }

    //    public String getValue(String key){
//        try {
//            JSONObject jsonObject = new JSONObject(params);
//            return jsonObject.getString(key);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        return null;
//    }
//
//    public Map<String,String> getMapParams(){
//        try {
//            JSONObject jsonObject = new JSONObject(params);
//            Iterator<String> iterator = jsonObject.keys();
//            Map<String,String> map = new LinkedTreeMap<>();
//            while(iterator.hasNext()){
//                String key = iterator.next();
//                String value = jsonObject.getString(key);
//                map.put(key, value);
//            }
//            return map;
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        return null;
//    }
    @Override
    public String toString() {
        return "NetParamsModel{" +
                "apiUrl='" + apiUrl + '\'' +
                ", params='" + params + '\'' +
                '}';
    }
}
