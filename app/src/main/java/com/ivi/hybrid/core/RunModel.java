package com.ivi.hybrid.core;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import static com.ivi.hybrid.core.Hybrid.LOCAL_MODEL;
import static com.ivi.hybrid.core.Hybrid.RUNTIME_MODEL;
import static com.ivi.hybrid.core.Hybrid.RUNTIME_TEST_MODEL;

/**
 * author: Rea.X
 * date: 2017/11/1.
 */
@Retention(RetentionPolicy.SOURCE)
@IntDef({LOCAL_MODEL, RUNTIME_TEST_MODEL, RUNTIME_MODEL})
public @interface RunModel {
}
