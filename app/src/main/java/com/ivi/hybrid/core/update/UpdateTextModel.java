package com.ivi.hybrid.core.update;

import java.io.Serializable;

/**
 * author: Rea.X
 * date: 2018/1/5.
 */

public class UpdateTextModel implements Serializable {

    private String normalUpdateText;
    private String ForceUpdateText;

    public UpdateTextModel(String normalUpdateText, String forceUpdateText) {
        this.normalUpdateText = normalUpdateText;
        ForceUpdateText = forceUpdateText;
    }

    public String getNormalUpdateText() {
        return normalUpdateText;
    }

    public void setNormalUpdateText(String normalUpdateText) {
        this.normalUpdateText = normalUpdateText;
    }

    public String getForceUpdateText() {
        return ForceUpdateText;
    }

    public void setForceUpdateText(String forceUpdateText) {
        ForceUpdateText = forceUpdateText;
    }
}
