package com.google.android.gms.wearable.internal;

import com.google.android.gms.common.internal.Preconditions;
import javax.annotation.Nullable;
import javax.annotation.concurrent.GuardedBy;

public final class zzbr extends zzej {
    private final Object lock = new Object();
    @GuardedBy("lock")
    @Nullable
    private zzav zzcw;
    @GuardedBy("lock")
    @Nullable
    private zzbs zzda;

    public final void zza(int i, int i2) {
        synchronized (this.lock) {
            zzbs com_google_android_gms_wearable_internal_zzbs = this.zzda;
            zzav com_google_android_gms_wearable_internal_zzav = new zzav(i, i2);
            this.zzcw = com_google_android_gms_wearable_internal_zzav;
        }
        if (com_google_android_gms_wearable_internal_zzbs != null) {
            com_google_android_gms_wearable_internal_zzbs.zzb(com_google_android_gms_wearable_internal_zzav);
        }
    }

    public final void zza(zzbs com_google_android_gms_wearable_internal_zzbs) {
        synchronized (this.lock) {
            this.zzda = (zzbs) Preconditions.checkNotNull(com_google_android_gms_wearable_internal_zzbs);
            zzav com_google_android_gms_wearable_internal_zzav = this.zzcw;
        }
        if (com_google_android_gms_wearable_internal_zzav != null) {
            com_google_android_gms_wearable_internal_zzbs.zzb(com_google_android_gms_wearable_internal_zzav);
        }
    }
}
