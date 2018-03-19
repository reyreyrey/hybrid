/*
 * Copyright (C) 2017 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.ivi.hybrid.ui.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

public abstract class FragmentPagerAdapter2 extends FragmentPagerAdapter {
    /**
     * current fragment position
     */
    protected int currentPosition = -1;

    /**
     * current fragment
     */
    protected Fragment currentFragment;

    public FragmentPagerAdapter2(FragmentManager fm) {
        super(fm);
    }

    @Override
    public void setPrimaryItem(ViewGroup container, int position, Object object) {
        if (object instanceof Fragment) {
            this.currentFragment = (Fragment) object;
        }
        super.setPrimaryItem(container, position, object);
        this.currentPosition = position;
    }

    /**
     * get current fragment position
     * @return
     */
    public int getCurrentPosition() {
        return currentPosition;
    }

    /**
     * get current fragment
     * @return
     */
    public Fragment getCurrentFragment() {
        return currentFragment;
    }
}
