package com.google.android.gms.measurement.internal;

import android.os.RemoteException;
import android.text.TextUtils;

final class zzdz implements Runnable {
    private final /* synthetic */ zzh zzaqn;
    private final /* synthetic */ String zzaqq;
    private final /* synthetic */ zzad zzaqr;
    private final /* synthetic */ zzdr zzasg;
    private final /* synthetic */ boolean zzasi;
    private final /* synthetic */ boolean zzasj;

    zzdz(zzdr com_google_android_gms_measurement_internal_zzdr, boolean z, boolean z2, zzad com_google_android_gms_measurement_internal_zzad, zzh com_google_android_gms_measurement_internal_zzh, String str) {
        this.zzasg = com_google_android_gms_measurement_internal_zzdr;
        this.zzasi = z;
        this.zzasj = z2;
        this.zzaqr = com_google_android_gms_measurement_internal_zzad;
        this.zzaqn = com_google_android_gms_measurement_internal_zzh;
        this.zzaqq = str;
    }

    public final void run() {
        zzag zzd = this.zzasg.zzasa;
        if (zzd == null) {
            this.zzasg.zzgo().zzjd().zzbx("Discarding data. Failed to send event to service");
            return;
        }
        if (this.zzasi) {
            this.zzasg.zza(zzd, this.zzasj ? null : this.zzaqr, this.zzaqn);
        } else {
            try {
                if (TextUtils.isEmpty(this.zzaqq)) {
                    zzd.zza(this.zzaqr, this.zzaqn);
                } else {
                    zzd.zza(this.zzaqr, this.zzaqq, this.zzasg.zzgo().zzjn());
                }
            } catch (RemoteException e) {
                this.zzasg.zzgo().zzjd().zzg("Failed to send event to the service", e);
            }
        }
        this.zzasg.zzcy();
    }
}
