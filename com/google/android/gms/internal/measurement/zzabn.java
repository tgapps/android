package com.google.android.gms.internal.measurement;

import java.io.IOException;

public final class zzabn extends zzabd<zzabn> {
    private static volatile zzabn[] zzcaf;
    public String zzcag;

    public zzabn() {
        this.zzcag = TtmlNode.ANONYMOUS_REGION_ID;
        this.zzbzh = null;
        this.zzbzs = -1;
    }

    public static zzabn[] zzwh() {
        if (zzcaf == null) {
            synchronized (zzabh.zzbzr) {
                if (zzcaf == null) {
                    zzcaf = new zzabn[0];
                }
            }
        }
        return zzcaf;
    }

    protected final int zza() {
        int zza = super.zza();
        return (this.zzcag == null || this.zzcag.equals(TtmlNode.ANONYMOUS_REGION_ID)) ? zza : zza + zzabb.zzd(1, this.zzcag);
    }

    public final void zza(zzabb com_google_android_gms_internal_measurement_zzabb) throws IOException {
        if (!(this.zzcag == null || this.zzcag.equals(TtmlNode.ANONYMOUS_REGION_ID))) {
            com_google_android_gms_internal_measurement_zzabb.zzc(1, this.zzcag);
        }
        super.zza(com_google_android_gms_internal_measurement_zzabb);
    }

    public final /* synthetic */ zzabj zzb(zzaba com_google_android_gms_internal_measurement_zzaba) throws IOException {
        while (true) {
            int zzvo = com_google_android_gms_internal_measurement_zzaba.zzvo();
            switch (zzvo) {
                case 0:
                    break;
                case 10:
                    this.zzcag = com_google_android_gms_internal_measurement_zzaba.readString();
                    continue;
                default:
                    if (!super.zza(com_google_android_gms_internal_measurement_zzaba, zzvo)) {
                        break;
                    }
                    continue;
            }
            return this;
        }
    }
}
