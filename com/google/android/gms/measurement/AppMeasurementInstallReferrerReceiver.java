package com.google.android.gms.measurement;

import android.content.BroadcastReceiver;
import android.content.BroadcastReceiver.PendingResult;
import android.content.Context;
import android.content.Intent;
import com.google.android.gms.measurement.internal.zzbj;
import com.google.android.gms.measurement.internal.zzbm;

public final class AppMeasurementInstallReferrerReceiver extends BroadcastReceiver implements zzbm {
    private zzbj zzadq;

    public final void onReceive(Context context, Intent intent) {
        if (this.zzadq == null) {
            this.zzadq = new zzbj(this);
        }
        this.zzadq.onReceive(context, intent);
    }

    public final void doStartService(Context context, Intent intent) {
    }

    public final PendingResult doGoAsync() {
        return goAsync();
    }
}
