package com.google.android.gms.common.util;

public interface Clock {
    long currentTimeMillis();

    long elapsedRealtime();
}