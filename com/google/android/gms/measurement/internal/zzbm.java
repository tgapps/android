package com.google.android.gms.measurement.internal;

import android.content.BroadcastReceiver.PendingResult;
import android.content.Context;
import android.content.Intent;

public interface zzbm {
    PendingResult doGoAsync();

    void doStartService(Context context, Intent intent);
}
