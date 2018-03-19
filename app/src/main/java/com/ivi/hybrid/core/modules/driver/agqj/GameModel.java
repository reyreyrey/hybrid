package com.ivi.hybrid.core.modules.driver.agqj;

import com.google.gson.annotations.SerializedName;
import com.google.gson.internal.LinkedTreeMap;

import java.io.Serializable;

/**
 * author: Rea.X
 * date: 2017/7/12.
 */

public class GameModel implements Serializable {
    private static final long serialVersionUID = -3384269487391636855L;
    @SerializedName("gameCode")
    private String gameCode;
    @SerializedName("isTry")
    private boolean isTry;
    @SerializedName("params")
    private LinkedTreeMap<String, String> params;

    public LinkedTreeMap<String, String> getParams() {
        return params;
    }

    public void setParams(LinkedTreeMap<String, String> params) {
        this.params = params;
    }

    public String getGameCode() {
        return gameCode;
    }

    public void setGameCode(String gameCode) {
        this.gameCode = gameCode;
    }

    public boolean isTry() {
        return isTry;
    }

    public void setTry(boolean aTry) {
        isTry = aTry;
    }

    @Override
    public String toString() {
        return "GameModel{" +
                "gameCode='" + gameCode + '\'' +
                ", isTry=" + isTry +
                '}';
    }
}
