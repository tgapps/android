package com.google.android.gms.common.api.internal;

import android.os.IBinder;
import android.os.IBinder.DeathRecipient;
import com.google.android.gms.common.api.zzc;
import java.lang.ref.WeakReference;
import java.util.NoSuchElementException;

final class zzcm implements DeathRecipient, zzcn {
    private final WeakReference<BasePendingResult<?>> zzmr;
    private final WeakReference<zzc> zzms;
    private final WeakReference<IBinder> zzmt;

    private zzcm(BasePendingResult<?> basePendingResult, zzc com_google_android_gms_common_api_zzc, IBinder iBinder) {
        this.zzms = new WeakReference(com_google_android_gms_common_api_zzc);
        this.zzmr = new WeakReference(basePendingResult);
        this.zzmt = new WeakReference(iBinder);
    }

    private final void zzcf() {
        BasePendingResult basePendingResult = (BasePendingResult) this.zzmr.get();
        zzc com_google_android_gms_common_api_zzc = (zzc) this.zzms.get();
        if (!(com_google_android_gms_common_api_zzc == null || basePendingResult == null)) {
            com_google_android_gms_common_api_zzc.remove(basePendingResult.zzo().intValue());
        }
        IBinder iBinder = (IBinder) this.zzmt.get();
        if (iBinder != null) {
            try {
                iBinder.unlinkToDeath(this, 0);
            } catch (NoSuchElementException e) {
            }
        }
    }

    public final void binderDied() {
        zzcf();
    }

    public final void zzc(BasePendingResult<?> basePendingResult) {
        zzcf();
    }
}
