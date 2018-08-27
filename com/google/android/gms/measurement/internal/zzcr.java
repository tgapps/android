package com.google.android.gms.measurement.internal;

import android.content.Context;
import com.google.android.gms.common.internal.Preconditions;

public final class zzcr {
    boolean zzadv = true;
    String zzadx;
    String zzapm;
    String zzapn;
    Boolean zzaqg;
    final Context zzri;

    public zzcr(Context context, zzak com_google_android_gms_measurement_internal_zzak) {
        Preconditions.checkNotNull(context);
        Context applicationContext = context.getApplicationContext();
        Preconditions.checkNotNull(applicationContext);
        this.zzri = applicationContext;
        if (com_google_android_gms_measurement_internal_zzak != null) {
            this.zzadx = com_google_android_gms_measurement_internal_zzak.zzadx;
            this.zzapm = com_google_android_gms_measurement_internal_zzak.origin;
            this.zzapn = com_google_android_gms_measurement_internal_zzak.zzadw;
            this.zzadv = com_google_android_gms_measurement_internal_zzak.zzadv;
            if (com_google_android_gms_measurement_internal_zzak.zzady != null) {
                this.zzaqg = Boolean.valueOf(com_google_android_gms_measurement_internal_zzak.zzady.getBoolean("dataCollectionDefaultEnabled", true));
            }
        }
    }
}
