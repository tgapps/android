package com.google.android.gms.internal.measurement;

import android.content.Context;
import android.os.Build.VERSION;
import com.google.android.gms.common.internal.Preconditions;

public final class zzjf<T extends Context> {
    public static boolean zza(Context context, boolean z) {
        Preconditions.checkNotNull(context);
        return zzjv.zzc(context, VERSION.SDK_INT >= 24 ? "com.google.android.gms.measurement.AppMeasurementJobService" : "com.google.android.gms.measurement.AppMeasurementService");
    }
}
