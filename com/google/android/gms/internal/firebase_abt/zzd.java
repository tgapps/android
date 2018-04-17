package com.google.android.gms.internal.firebase_abt;

import java.io.IOException;

public abstract class zzd<M extends zzd<M>> extends zzj {
    protected zzf zzs;

    public /* synthetic */ Object clone() throws CloneNotSupportedException {
        zzd com_google_android_gms_internal_firebase_abt_zzd = (zzd) super.zzj();
        zzh.zza(this, com_google_android_gms_internal_firebase_abt_zzd);
        return com_google_android_gms_internal_firebase_abt_zzd;
    }

    protected final boolean zza(zza com_google_android_gms_internal_firebase_abt_zza, int i) throws IOException {
        int position = com_google_android_gms_internal_firebase_abt_zza.getPosition();
        if (!com_google_android_gms_internal_firebase_abt_zza.zzb(i)) {
            return false;
        }
        int i2 = i >>> 3;
        zzl com_google_android_gms_internal_firebase_abt_zzl = new zzl(i, com_google_android_gms_internal_firebase_abt_zza.zza(position, com_google_android_gms_internal_firebase_abt_zza.getPosition() - position));
        zzg com_google_android_gms_internal_firebase_abt_zzg = null;
        if (this.zzs == null) {
            this.zzs = new zzf();
        } else {
            com_google_android_gms_internal_firebase_abt_zzg = this.zzs.zzg(i2);
        }
        if (com_google_android_gms_internal_firebase_abt_zzg == null) {
            com_google_android_gms_internal_firebase_abt_zzg = new zzg();
            this.zzs.zza(i2, com_google_android_gms_internal_firebase_abt_zzg);
        }
        com_google_android_gms_internal_firebase_abt_zzg.zza(com_google_android_gms_internal_firebase_abt_zzl);
        return true;
    }

    public final /* synthetic */ zzj zzj() throws CloneNotSupportedException {
        return (zzd) clone();
    }
}
