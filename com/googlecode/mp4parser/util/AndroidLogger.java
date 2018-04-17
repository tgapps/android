package com.googlecode.mp4parser.util;

import android.util.Log;

public class AndroidLogger extends Logger {
    String name;

    public AndroidLogger(String name) {
        this.name = name;
    }

    public void logDebug(String message) {
        StringBuilder stringBuilder = new StringBuilder(String.valueOf(this.name));
        stringBuilder.append(":");
        stringBuilder.append(message);
        Log.d("isoparser", stringBuilder.toString());
    }

    public void logError(String message) {
        StringBuilder stringBuilder = new StringBuilder(String.valueOf(this.name));
        stringBuilder.append(":");
        stringBuilder.append(message);
        Log.e("isoparser", stringBuilder.toString());
    }
}
