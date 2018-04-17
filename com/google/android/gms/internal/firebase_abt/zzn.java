package com.google.android.gms.internal.firebase_abt;

import java.io.IOException;

public final class zzn extends zzd<zzn> {
    private static volatile zzn[] zzap;
    public String zzaq;

    public zzn() {
        this.zzaq = TtmlNode.ANONYMOUS_REGION_ID;
        this.zzs = null;
        this.zzab = -1;
    }

    public static zzn[] zzo() {
        if (zzap == null) {
            synchronized (zzh.zzaa) {
                if (zzap == null) {
                    zzap = new zzn[0];
                }
            }
        }
        return zzap;
    }

    public final /* synthetic */ zzj zza(zza com_google_android_gms_internal_firebase_abt_zza) throws IOException {
        while (true) {
            int zzd = com_google_android_gms_internal_firebase_abt_zza.zzd();
            if (zzd == 0) {
                return this;
            }
            if (zzd == 10) {
                this.zzaq = com_google_android_gms_internal_firebase_abt_zza.readString();
            } else if (!super.zza(com_google_android_gms_internal_firebase_abt_zza, zzd)) {
                return this;
            }
        }
    }
}
