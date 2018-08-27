package com.google.android.gms.internal.measurement;

import com.google.android.gms.internal.measurement.zzvm.zze;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

final class zzuv implements zzyw {
    private final zzut zzbuf;

    public static zzuv zza(zzut com_google_android_gms_internal_measurement_zzut) {
        if (com_google_android_gms_internal_measurement_zzut.zzbuw != null) {
            return com_google_android_gms_internal_measurement_zzut.zzbuw;
        }
        return new zzuv(com_google_android_gms_internal_measurement_zzut);
    }

    private zzuv(zzut com_google_android_gms_internal_measurement_zzut) {
        this.zzbuf = (zzut) zzvo.zza(com_google_android_gms_internal_measurement_zzut, "output");
    }

    public final int zzvj() {
        return zze.zzbze;
    }

    public final void zzn(int i, int i2) throws IOException {
        this.zzbuf.zzg(i, i2);
    }

    public final void zzi(int i, long j) throws IOException {
        this.zzbuf.zza(i, j);
    }

    public final void zzj(int i, long j) throws IOException {
        this.zzbuf.zzc(i, j);
    }

    public final void zza(int i, float f) throws IOException {
        this.zzbuf.zza(i, f);
    }

    public final void zza(int i, double d) throws IOException {
        this.zzbuf.zza(i, d);
    }

    public final void zzo(int i, int i2) throws IOException {
        this.zzbuf.zzd(i, i2);
    }

    public final void zza(int i, long j) throws IOException {
        this.zzbuf.zza(i, j);
    }

    public final void zzd(int i, int i2) throws IOException {
        this.zzbuf.zzd(i, i2);
    }

    public final void zzc(int i, long j) throws IOException {
        this.zzbuf.zzc(i, j);
    }

    public final void zzg(int i, int i2) throws IOException {
        this.zzbuf.zzg(i, i2);
    }

    public final void zzb(int i, boolean z) throws IOException {
        this.zzbuf.zzb(i, z);
    }

    public final void zzb(int i, String str) throws IOException {
        this.zzbuf.zzb(i, str);
    }

    public final void zza(int i, zzud com_google_android_gms_internal_measurement_zzud) throws IOException {
        this.zzbuf.zza(i, com_google_android_gms_internal_measurement_zzud);
    }

    public final void zze(int i, int i2) throws IOException {
        this.zzbuf.zze(i, i2);
    }

    public final void zzf(int i, int i2) throws IOException {
        this.zzbuf.zzf(i, i2);
    }

    public final void zzb(int i, long j) throws IOException {
        this.zzbuf.zzb(i, j);
    }

    public final void zza(int i, Object obj, zzxj com_google_android_gms_internal_measurement_zzxj) throws IOException {
        this.zzbuf.zza(i, (zzwt) obj, com_google_android_gms_internal_measurement_zzxj);
    }

    public final void zzb(int i, Object obj, zzxj com_google_android_gms_internal_measurement_zzxj) throws IOException {
        zzut com_google_android_gms_internal_measurement_zzut = this.zzbuf;
        zzwt com_google_android_gms_internal_measurement_zzwt = (zzwt) obj;
        com_google_android_gms_internal_measurement_zzut.zzc(i, 3);
        com_google_android_gms_internal_measurement_zzxj.zza(com_google_android_gms_internal_measurement_zzwt, com_google_android_gms_internal_measurement_zzut.zzbuw);
        com_google_android_gms_internal_measurement_zzut.zzc(i, 4);
    }

    public final void zzbk(int i) throws IOException {
        this.zzbuf.zzc(i, 3);
    }

    public final void zzbl(int i) throws IOException {
        this.zzbuf.zzc(i, 4);
    }

    public final void zza(int i, Object obj) throws IOException {
        if (obj instanceof zzud) {
            this.zzbuf.zzb(i, (zzud) obj);
        } else {
            this.zzbuf.zzb(i, (zzwt) obj);
        }
    }

    public final void zza(int i, List<Integer> list, boolean z) throws IOException {
        int i2 = 0;
        if (z) {
            this.zzbuf.zzc(i, 2);
            int i3 = 0;
            for (int i4 = 0; i4 < list.size(); i4++) {
                i3 += zzut.zzbc(((Integer) list.get(i4)).intValue());
            }
            this.zzbuf.zzay(i3);
            while (i2 < list.size()) {
                this.zzbuf.zzax(((Integer) list.get(i2)).intValue());
                i2++;
            }
            return;
        }
        while (i2 < list.size()) {
            this.zzbuf.zzd(i, ((Integer) list.get(i2)).intValue());
            i2++;
        }
    }

    public final void zzb(int i, List<Integer> list, boolean z) throws IOException {
        int i2 = 0;
        if (z) {
            this.zzbuf.zzc(i, 2);
            int i3 = 0;
            for (int i4 = 0; i4 < list.size(); i4++) {
                i3 += zzut.zzbf(((Integer) list.get(i4)).intValue());
            }
            this.zzbuf.zzay(i3);
            while (i2 < list.size()) {
                this.zzbuf.zzba(((Integer) list.get(i2)).intValue());
                i2++;
            }
            return;
        }
        while (i2 < list.size()) {
            this.zzbuf.zzg(i, ((Integer) list.get(i2)).intValue());
            i2++;
        }
    }

    public final void zzc(int i, List<Long> list, boolean z) throws IOException {
        int i2 = 0;
        if (z) {
            this.zzbuf.zzc(i, 2);
            int i3 = 0;
            for (int i4 = 0; i4 < list.size(); i4++) {
                i3 += zzut.zzay(((Long) list.get(i4)).longValue());
            }
            this.zzbuf.zzay(i3);
            while (i2 < list.size()) {
                this.zzbuf.zzav(((Long) list.get(i2)).longValue());
                i2++;
            }
            return;
        }
        while (i2 < list.size()) {
            this.zzbuf.zza(i, ((Long) list.get(i2)).longValue());
            i2++;
        }
    }

    public final void zzd(int i, List<Long> list, boolean z) throws IOException {
        int i2 = 0;
        if (z) {
            this.zzbuf.zzc(i, 2);
            int i3 = 0;
            for (int i4 = 0; i4 < list.size(); i4++) {
                i3 += zzut.zzaz(((Long) list.get(i4)).longValue());
            }
            this.zzbuf.zzay(i3);
            while (i2 < list.size()) {
                this.zzbuf.zzav(((Long) list.get(i2)).longValue());
                i2++;
            }
            return;
        }
        while (i2 < list.size()) {
            this.zzbuf.zza(i, ((Long) list.get(i2)).longValue());
            i2++;
        }
    }

    public final void zze(int i, List<Long> list, boolean z) throws IOException {
        int i2 = 0;
        if (z) {
            this.zzbuf.zzc(i, 2);
            int i3 = 0;
            for (int i4 = 0; i4 < list.size(); i4++) {
                i3 += zzut.zzbb(((Long) list.get(i4)).longValue());
            }
            this.zzbuf.zzay(i3);
            while (i2 < list.size()) {
                this.zzbuf.zzax(((Long) list.get(i2)).longValue());
                i2++;
            }
            return;
        }
        while (i2 < list.size()) {
            this.zzbuf.zzc(i, ((Long) list.get(i2)).longValue());
            i2++;
        }
    }

    public final void zzf(int i, List<Float> list, boolean z) throws IOException {
        int i2 = 0;
        if (z) {
            this.zzbuf.zzc(i, 2);
            int i3 = 0;
            for (int i4 = 0; i4 < list.size(); i4++) {
                i3 += zzut.zzb(((Float) list.get(i4)).floatValue());
            }
            this.zzbuf.zzay(i3);
            while (i2 < list.size()) {
                this.zzbuf.zza(((Float) list.get(i2)).floatValue());
                i2++;
            }
            return;
        }
        while (i2 < list.size()) {
            this.zzbuf.zza(i, ((Float) list.get(i2)).floatValue());
            i2++;
        }
    }

    public final void zzg(int i, List<Double> list, boolean z) throws IOException {
        int i2 = 0;
        if (z) {
            this.zzbuf.zzc(i, 2);
            int i3 = 0;
            for (int i4 = 0; i4 < list.size(); i4++) {
                i3 += zzut.zzc(((Double) list.get(i4)).doubleValue());
            }
            this.zzbuf.zzay(i3);
            while (i2 < list.size()) {
                this.zzbuf.zzb(((Double) list.get(i2)).doubleValue());
                i2++;
            }
            return;
        }
        while (i2 < list.size()) {
            this.zzbuf.zza(i, ((Double) list.get(i2)).doubleValue());
            i2++;
        }
    }

    public final void zzh(int i, List<Integer> list, boolean z) throws IOException {
        int i2 = 0;
        if (z) {
            this.zzbuf.zzc(i, 2);
            int i3 = 0;
            for (int i4 = 0; i4 < list.size(); i4++) {
                i3 += zzut.zzbh(((Integer) list.get(i4)).intValue());
            }
            this.zzbuf.zzay(i3);
            while (i2 < list.size()) {
                this.zzbuf.zzax(((Integer) list.get(i2)).intValue());
                i2++;
            }
            return;
        }
        while (i2 < list.size()) {
            this.zzbuf.zzd(i, ((Integer) list.get(i2)).intValue());
            i2++;
        }
    }

    public final void zzi(int i, List<Boolean> list, boolean z) throws IOException {
        int i2 = 0;
        if (z) {
            this.zzbuf.zzc(i, 2);
            int i3 = 0;
            for (int i4 = 0; i4 < list.size(); i4++) {
                i3 += zzut.zzv(((Boolean) list.get(i4)).booleanValue());
            }
            this.zzbuf.zzay(i3);
            while (i2 < list.size()) {
                this.zzbuf.zzu(((Boolean) list.get(i2)).booleanValue());
                i2++;
            }
            return;
        }
        while (i2 < list.size()) {
            this.zzbuf.zzb(i, ((Boolean) list.get(i2)).booleanValue());
            i2++;
        }
    }

    public final void zza(int i, List<String> list) throws IOException {
        int i2 = 0;
        if (list instanceof zzwc) {
            zzwc com_google_android_gms_internal_measurement_zzwc = (zzwc) list;
            for (int i3 = 0; i3 < list.size(); i3++) {
                Object raw = com_google_android_gms_internal_measurement_zzwc.getRaw(i3);
                if (raw instanceof String) {
                    this.zzbuf.zzb(i, (String) raw);
                } else {
                    this.zzbuf.zza(i, (zzud) raw);
                }
            }
            return;
        }
        while (i2 < list.size()) {
            this.zzbuf.zzb(i, (String) list.get(i2));
            i2++;
        }
    }

    public final void zzb(int i, List<zzud> list) throws IOException {
        for (int i2 = 0; i2 < list.size(); i2++) {
            this.zzbuf.zza(i, (zzud) list.get(i2));
        }
    }

    public final void zzj(int i, List<Integer> list, boolean z) throws IOException {
        int i2 = 0;
        if (z) {
            this.zzbuf.zzc(i, 2);
            int i3 = 0;
            for (int i4 = 0; i4 < list.size(); i4++) {
                i3 += zzut.zzbd(((Integer) list.get(i4)).intValue());
            }
            this.zzbuf.zzay(i3);
            while (i2 < list.size()) {
                this.zzbuf.zzay(((Integer) list.get(i2)).intValue());
                i2++;
            }
            return;
        }
        while (i2 < list.size()) {
            this.zzbuf.zze(i, ((Integer) list.get(i2)).intValue());
            i2++;
        }
    }

    public final void zzk(int i, List<Integer> list, boolean z) throws IOException {
        int i2 = 0;
        if (z) {
            this.zzbuf.zzc(i, 2);
            int i3 = 0;
            for (int i4 = 0; i4 < list.size(); i4++) {
                i3 += zzut.zzbg(((Integer) list.get(i4)).intValue());
            }
            this.zzbuf.zzay(i3);
            while (i2 < list.size()) {
                this.zzbuf.zzba(((Integer) list.get(i2)).intValue());
                i2++;
            }
            return;
        }
        while (i2 < list.size()) {
            this.zzbuf.zzg(i, ((Integer) list.get(i2)).intValue());
            i2++;
        }
    }

    public final void zzl(int i, List<Long> list, boolean z) throws IOException {
        int i2 = 0;
        if (z) {
            this.zzbuf.zzc(i, 2);
            int i3 = 0;
            for (int i4 = 0; i4 < list.size(); i4++) {
                i3 += zzut.zzbc(((Long) list.get(i4)).longValue());
            }
            this.zzbuf.zzay(i3);
            while (i2 < list.size()) {
                this.zzbuf.zzax(((Long) list.get(i2)).longValue());
                i2++;
            }
            return;
        }
        while (i2 < list.size()) {
            this.zzbuf.zzc(i, ((Long) list.get(i2)).longValue());
            i2++;
        }
    }

    public final void zzm(int i, List<Integer> list, boolean z) throws IOException {
        int i2 = 0;
        if (z) {
            this.zzbuf.zzc(i, 2);
            int i3 = 0;
            for (int i4 = 0; i4 < list.size(); i4++) {
                i3 += zzut.zzbe(((Integer) list.get(i4)).intValue());
            }
            this.zzbuf.zzay(i3);
            while (i2 < list.size()) {
                this.zzbuf.zzaz(((Integer) list.get(i2)).intValue());
                i2++;
            }
            return;
        }
        while (i2 < list.size()) {
            this.zzbuf.zzf(i, ((Integer) list.get(i2)).intValue());
            i2++;
        }
    }

    public final void zzn(int i, List<Long> list, boolean z) throws IOException {
        int i2 = 0;
        if (z) {
            this.zzbuf.zzc(i, 2);
            int i3 = 0;
            for (int i4 = 0; i4 < list.size(); i4++) {
                i3 += zzut.zzba(((Long) list.get(i4)).longValue());
            }
            this.zzbuf.zzay(i3);
            while (i2 < list.size()) {
                this.zzbuf.zzaw(((Long) list.get(i2)).longValue());
                i2++;
            }
            return;
        }
        while (i2 < list.size()) {
            this.zzbuf.zzb(i, ((Long) list.get(i2)).longValue());
            i2++;
        }
    }

    public final void zza(int i, List<?> list, zzxj com_google_android_gms_internal_measurement_zzxj) throws IOException {
        for (int i2 = 0; i2 < list.size(); i2++) {
            zza(i, list.get(i2), com_google_android_gms_internal_measurement_zzxj);
        }
    }

    public final void zzb(int i, List<?> list, zzxj com_google_android_gms_internal_measurement_zzxj) throws IOException {
        for (int i2 = 0; i2 < list.size(); i2++) {
            zzb(i, list.get(i2), com_google_android_gms_internal_measurement_zzxj);
        }
    }

    public final <K, V> void zza(int i, zzwm<K, V> com_google_android_gms_internal_measurement_zzwm_K__V, Map<K, V> map) throws IOException {
        for (Entry entry : map.entrySet()) {
            this.zzbuf.zzc(i, 2);
            this.zzbuf.zzay(zzwl.zza(com_google_android_gms_internal_measurement_zzwm_K__V, entry.getKey(), entry.getValue()));
            zzwl.zza(this.zzbuf, com_google_android_gms_internal_measurement_zzwm_K__V, entry.getKey(), entry.getValue());
        }
    }
}
