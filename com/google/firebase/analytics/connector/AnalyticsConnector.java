package com.google.firebase.analytics.connector;

import android.os.Bundle;

public interface AnalyticsConnector {
    void logEvent(String str, String str2, Bundle bundle);

    void setUserProperty(String str, String str2, Object obj);
}
