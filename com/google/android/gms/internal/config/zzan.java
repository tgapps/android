package com.google.android.gms.internal.config;

import android.content.Context;
import android.util.Log;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public final class zzan implements Runnable {
    private final Context mContext;
    private final zzar zzaj;
    private final zzao zzat;
    private final zzao zzau;
    private final zzao zzav;

    public zzan(Context context, zzao com_google_android_gms_internal_config_zzao, zzao com_google_android_gms_internal_config_zzao2, zzao com_google_android_gms_internal_config_zzao3, zzar com_google_android_gms_internal_config_zzar) {
        this.mContext = context;
        this.zzat = com_google_android_gms_internal_config_zzao;
        this.zzau = com_google_android_gms_internal_config_zzao2;
        this.zzav = com_google_android_gms_internal_config_zzao3;
        this.zzaj = com_google_android_gms_internal_config_zzar;
    }

    private static zzas zza(zzao com_google_android_gms_internal_config_zzao) {
        zzas com_google_android_gms_internal_config_zzas = new zzas();
        if (com_google_android_gms_internal_config_zzao.zzp() != null) {
            Map zzp = com_google_android_gms_internal_config_zzao.zzp();
            List arrayList = new ArrayList();
            if (zzp != null) {
                for (String str : zzp.keySet()) {
                    List arrayList2 = new ArrayList();
                    Map map = (Map) zzp.get(str);
                    if (map != null) {
                        for (String str2 : map.keySet()) {
                            zzat com_google_android_gms_internal_config_zzat = new zzat();
                            com_google_android_gms_internal_config_zzat.zzbi = str2;
                            com_google_android_gms_internal_config_zzat.zzbj = (byte[]) map.get(str2);
                            arrayList2.add(com_google_android_gms_internal_config_zzat);
                        }
                    }
                    zzav com_google_android_gms_internal_config_zzav = new zzav();
                    com_google_android_gms_internal_config_zzav.namespace = str;
                    com_google_android_gms_internal_config_zzav.zzbo = (zzat[]) arrayList2.toArray(new zzat[arrayList2.size()]);
                    arrayList.add(com_google_android_gms_internal_config_zzav);
                }
            }
            com_google_android_gms_internal_config_zzas.zzbf = (zzav[]) arrayList.toArray(new zzav[arrayList.size()]);
        }
        if (com_google_android_gms_internal_config_zzao.zzg() != null) {
            List zzg = com_google_android_gms_internal_config_zzao.zzg();
            com_google_android_gms_internal_config_zzas.zzbg = (byte[][]) zzg.toArray(new byte[zzg.size()][]);
        }
        com_google_android_gms_internal_config_zzas.timestamp = com_google_android_gms_internal_config_zzao.getTimestamp();
        return com_google_android_gms_internal_config_zzas;
    }

    public final void run() {
        zzbh com_google_android_gms_internal_config_zzaw = new zzaw();
        if (this.zzat != null) {
            com_google_android_gms_internal_config_zzaw.zzbp = zza(this.zzat);
        }
        if (this.zzau != null) {
            com_google_android_gms_internal_config_zzaw.zzbq = zza(this.zzau);
        }
        if (this.zzav != null) {
            com_google_android_gms_internal_config_zzaw.zzbr = zza(this.zzav);
        }
        if (this.zzaj != null) {
            zzau com_google_android_gms_internal_config_zzau = new zzau();
            com_google_android_gms_internal_config_zzau.zzbk = this.zzaj.getLastFetchStatus();
            com_google_android_gms_internal_config_zzau.zzbl = this.zzaj.isDeveloperModeEnabled();
            com_google_android_gms_internal_config_zzaw.zzbs = com_google_android_gms_internal_config_zzau;
        }
        if (!(this.zzaj == null || this.zzaj.zzr() == null)) {
            List arrayList = new ArrayList();
            Map zzr = this.zzaj.zzr();
            for (String str : zzr.keySet()) {
                if (zzr.get(str) != null) {
                    zzax com_google_android_gms_internal_config_zzax = new zzax();
                    com_google_android_gms_internal_config_zzax.namespace = str;
                    com_google_android_gms_internal_config_zzax.zzbv = ((zzal) zzr.get(str)).zzo();
                    com_google_android_gms_internal_config_zzax.resourceId = ((zzal) zzr.get(str)).getResourceId();
                    arrayList.add(com_google_android_gms_internal_config_zzax);
                }
            }
            com_google_android_gms_internal_config_zzaw.zzbt = (zzax[]) arrayList.toArray(new zzax[arrayList.size()]);
        }
        byte[] bArr = new byte[com_google_android_gms_internal_config_zzaw.zzah()];
        try {
            zzaz zzb = zzaz.zzb(bArr, 0, bArr.length);
            com_google_android_gms_internal_config_zzaw.zza(zzb);
            zzb.zzac();
            try {
                FileOutputStream openFileOutput = this.mContext.openFileOutput("persisted_config", 0);
                openFileOutput.write(bArr);
                openFileOutput.close();
            } catch (Throwable e) {
                Log.e("AsyncPersisterTask", "Could not persist config.", e);
            }
        } catch (Throwable e2) {
            throw new RuntimeException("Serializing to a byte array threw an IOException (should never happen).", e2);
        }
    }
}
