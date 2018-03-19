package com.ivi.hybrid.core.config.menu;

import android.support.annotation.MenuRes;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * author: Rea.X
 * date: 2017/8/11.
 */

public class MenuModel implements Serializable {
    private static final long serialVersionUID = 2482613612817460025L;

    @SerializedName("models")
    private List<MenuItemModel> models;

    public List<MenuItemModel> getModels() {
        return models;
    }

    public void setModels(List<MenuItemModel> models) {
        this.models = models;
    }

    public static class MenuItemModel implements Serializable {
        private static final long serialVersionUID = -3083053939648959169L;
        @SerializedName("menuId")
        private
        @MenuRes
        int menuId;

        public
        @MenuRes
        int getMenuId() {
            return menuId;
        }

        public void setMenuId(@MenuRes int menuId) {
            this.menuId = menuId;
        }
    }

}
