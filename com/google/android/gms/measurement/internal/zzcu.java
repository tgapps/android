package com.google.android.gms.measurement.internal;

import android.os.Bundle;

final class zzcu implements Runnable {
    private final /* synthetic */ String val$name;
    private final /* synthetic */ String zzaeh;
    private final /* synthetic */ boolean zzafg;
    private final /* synthetic */ String zzaqq;
    private final /* synthetic */ zzcs zzarc;
    private final /* synthetic */ long zzard;
    private final /* synthetic */ Bundle zzare;
    private final /* synthetic */ boolean zzarf;
    private final /* synthetic */ boolean zzarg;

    zzcu(zzcs com_google_android_gms_measurement_internal_zzcs, String str, String str2, long j, Bundle bundle, boolean z, boolean z2, boolean z3, String str3) {
        this.zzarc = com_google_android_gms_measurement_internal_zzcs;
        this.zzaeh = str;
        this.val$name = str2;
        this.zzard = j;
        this.zzare = bundle;
        this.zzafg = z;
        this.zzarf = z2;
        this.zzarg = z3;
        this.zzaqq = str3;
    }

    public final void run() {
        this.zzarc.zza(this.zzaeh, this.val$name, this.zzard, this.zzare, this.zzafg, this.zzarf, this.zzarg, this.zzaqq);
    }
}
