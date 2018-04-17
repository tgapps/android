package com.google.android.gms.internal.measurement;

import android.os.RemoteException;
import android.text.TextUtils;
import java.util.Collections;
import java.util.concurrent.atomic.AtomicReference;

final class zziw implements Runnable {
    private final /* synthetic */ zzec zzanq;
    private final /* synthetic */ String zzaoa;
    private final /* synthetic */ String zzaob;
    private final /* synthetic */ String zzaoc;
    private final /* synthetic */ boolean zzaos;
    private final /* synthetic */ zzil zzapy;
    private final /* synthetic */ AtomicReference zzapz;

    zziw(zzil com_google_android_gms_internal_measurement_zzil, AtomicReference atomicReference, String str, String str2, String str3, boolean z, zzec com_google_android_gms_internal_measurement_zzec) {
        this.zzapy = com_google_android_gms_internal_measurement_zzil;
        this.zzapz = atomicReference;
        this.zzaoc = str;
        this.zzaoa = str2;
        this.zzaob = str3;
        this.zzaos = z;
        this.zzanq = com_google_android_gms_internal_measurement_zzec;
    }

    public final void run() {
        synchronized (this.zzapz) {
            Object zza;
            try {
                zzey zzd = this.zzapy.zzaps;
                if (zzd == null) {
                    this.zzapy.zzgg().zzil().zzd("Failed to get user properties", zzfg.zzbh(this.zzaoc), this.zzaoa, this.zzaob);
                    this.zzapz.set(Collections.emptyList());
                    this.zzapz.notify();
                    return;
                }
                AtomicReference atomicReference;
                if (TextUtils.isEmpty(this.zzaoc)) {
                    atomicReference = this.zzapz;
                    zza = zzd.zza(this.zzaoa, this.zzaob, this.zzaos, this.zzanq);
                } else {
                    atomicReference = this.zzapz;
                    zza = zzd.zza(this.zzaoc, this.zzaoa, this.zzaob, this.zzaos);
                }
                atomicReference.set(zza);
                this.zzapy.zzcu();
                zza = this.zzapz;
                zza.notify();
            } catch (RemoteException e) {
                try {
                    this.zzapy.zzgg().zzil().zzd("Failed to get user properties", zzfg.zzbh(this.zzaoc), this.zzaoa, e);
                    this.zzapz.set(Collections.emptyList());
                    zza = this.zzapz;
                } catch (Throwable th) {
                    this.zzapz.notify();
                }
            }
        }
    }
}
