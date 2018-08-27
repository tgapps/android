package com.google.android.gms.measurement.internal;

import android.os.Binder;
import android.text.TextUtils;
import com.google.android.gms.common.GooglePlayServicesUtilLight;
import com.google.android.gms.common.GoogleSignatureVerifier;
import com.google.android.gms.common.internal.Preconditions;
import com.google.android.gms.common.util.UidVerifier;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;

public final class zzbv extends zzah {
    private final zzfa zzamz;
    private Boolean zzaql;
    private String zzaqm;

    public zzbv(zzfa com_google_android_gms_measurement_internal_zzfa) {
        this(com_google_android_gms_measurement_internal_zzfa, null);
    }

    private zzbv(zzfa com_google_android_gms_measurement_internal_zzfa, String str) {
        Preconditions.checkNotNull(com_google_android_gms_measurement_internal_zzfa);
        this.zzamz = com_google_android_gms_measurement_internal_zzfa;
        this.zzaqm = null;
    }

    public final void zzb(zzh com_google_android_gms_measurement_internal_zzh) {
        zzb(com_google_android_gms_measurement_internal_zzh, false);
        zze(new zzbw(this, com_google_android_gms_measurement_internal_zzh));
    }

    public final void zza(zzad com_google_android_gms_measurement_internal_zzad, zzh com_google_android_gms_measurement_internal_zzh) {
        Preconditions.checkNotNull(com_google_android_gms_measurement_internal_zzad);
        zzb(com_google_android_gms_measurement_internal_zzh, false);
        zze(new zzcg(this, com_google_android_gms_measurement_internal_zzad, com_google_android_gms_measurement_internal_zzh));
    }

    final zzad zzb(zzad com_google_android_gms_measurement_internal_zzad, zzh com_google_android_gms_measurement_internal_zzh) {
        Object obj = null;
        if (!(!"_cmp".equals(com_google_android_gms_measurement_internal_zzad.name) || com_google_android_gms_measurement_internal_zzad.zzaid == null || com_google_android_gms_measurement_internal_zzad.zzaid.size() == 0)) {
            CharSequence string = com_google_android_gms_measurement_internal_zzad.zzaid.getString("_cis");
            if (!TextUtils.isEmpty(string) && (("referrer broadcast".equals(string) || "referrer API".equals(string)) && this.zzamz.zzgq().zzbg(com_google_android_gms_measurement_internal_zzh.packageName))) {
                obj = 1;
            }
        }
        if (obj == null) {
            return com_google_android_gms_measurement_internal_zzad;
        }
        this.zzamz.zzgo().zzjj().zzg("Event has been filtered ", com_google_android_gms_measurement_internal_zzad.toString());
        return new zzad("_cmpx", com_google_android_gms_measurement_internal_zzad.zzaid, com_google_android_gms_measurement_internal_zzad.origin, com_google_android_gms_measurement_internal_zzad.zzaip);
    }

    public final void zza(zzad com_google_android_gms_measurement_internal_zzad, String str, String str2) {
        Preconditions.checkNotNull(com_google_android_gms_measurement_internal_zzad);
        Preconditions.checkNotEmpty(str);
        zzc(str, true);
        zze(new zzch(this, com_google_android_gms_measurement_internal_zzad, str));
    }

    public final byte[] zza(zzad com_google_android_gms_measurement_internal_zzad, String str) {
        Object e;
        Preconditions.checkNotEmpty(str);
        Preconditions.checkNotNull(com_google_android_gms_measurement_internal_zzad);
        zzc(str, true);
        this.zzamz.zzgo().zzjk().zzg("Log and bundle. event", this.zzamz.zzgl().zzbs(com_google_android_gms_measurement_internal_zzad.name));
        long nanoTime = this.zzamz.zzbx().nanoTime() / 1000000;
        try {
            byte[] bArr = (byte[]) this.zzamz.zzgn().zzc(new zzci(this, com_google_android_gms_measurement_internal_zzad, str)).get();
            if (bArr == null) {
                this.zzamz.zzgo().zzjd().zzg("Log and bundle returned null. appId", zzap.zzbv(str));
                bArr = new byte[0];
            }
            this.zzamz.zzgo().zzjk().zzd("Log and bundle processed. event, size, time_ms", this.zzamz.zzgl().zzbs(com_google_android_gms_measurement_internal_zzad.name), Integer.valueOf(bArr.length), Long.valueOf((this.zzamz.zzbx().nanoTime() / 1000000) - nanoTime));
            return bArr;
        } catch (InterruptedException e2) {
            e = e2;
            this.zzamz.zzgo().zzjd().zzd("Failed to log and bundle. appId, event, error", zzap.zzbv(str), this.zzamz.zzgl().zzbs(com_google_android_gms_measurement_internal_zzad.name), e);
            return null;
        } catch (ExecutionException e3) {
            e = e3;
            this.zzamz.zzgo().zzjd().zzd("Failed to log and bundle. appId, event, error", zzap.zzbv(str), this.zzamz.zzgl().zzbs(com_google_android_gms_measurement_internal_zzad.name), e);
            return null;
        }
    }

    public final void zza(zzfh com_google_android_gms_measurement_internal_zzfh, zzh com_google_android_gms_measurement_internal_zzh) {
        Preconditions.checkNotNull(com_google_android_gms_measurement_internal_zzfh);
        zzb(com_google_android_gms_measurement_internal_zzh, false);
        if (com_google_android_gms_measurement_internal_zzfh.getValue() == null) {
            zze(new zzcj(this, com_google_android_gms_measurement_internal_zzfh, com_google_android_gms_measurement_internal_zzh));
        } else {
            zze(new zzck(this, com_google_android_gms_measurement_internal_zzfh, com_google_android_gms_measurement_internal_zzh));
        }
    }

    public final List<zzfh> zza(zzh com_google_android_gms_measurement_internal_zzh, boolean z) {
        Object e;
        zzb(com_google_android_gms_measurement_internal_zzh, false);
        try {
            List<zzfj> list = (List) this.zzamz.zzgn().zzb(new zzcl(this, com_google_android_gms_measurement_internal_zzh)).get();
            List<zzfh> arrayList = new ArrayList(list.size());
            for (zzfj com_google_android_gms_measurement_internal_zzfj : list) {
                if (z || !zzfk.zzcv(com_google_android_gms_measurement_internal_zzfj.name)) {
                    arrayList.add(new zzfh(com_google_android_gms_measurement_internal_zzfj));
                }
            }
            return arrayList;
        } catch (InterruptedException e2) {
            e = e2;
            this.zzamz.zzgo().zzjd().zze("Failed to get user attributes. appId", zzap.zzbv(com_google_android_gms_measurement_internal_zzh.packageName), e);
            return null;
        } catch (ExecutionException e3) {
            e = e3;
            this.zzamz.zzgo().zzjd().zze("Failed to get user attributes. appId", zzap.zzbv(com_google_android_gms_measurement_internal_zzh.packageName), e);
            return null;
        }
    }

    public final void zza(zzh com_google_android_gms_measurement_internal_zzh) {
        zzb(com_google_android_gms_measurement_internal_zzh, false);
        zze(new zzcm(this, com_google_android_gms_measurement_internal_zzh));
    }

    private final void zzb(zzh com_google_android_gms_measurement_internal_zzh, boolean z) {
        Preconditions.checkNotNull(com_google_android_gms_measurement_internal_zzh);
        zzc(com_google_android_gms_measurement_internal_zzh.packageName, false);
        this.zzamz.zzgm().zzt(com_google_android_gms_measurement_internal_zzh.zzafx, com_google_android_gms_measurement_internal_zzh.zzagk);
    }

    private final void zzc(String str, boolean z) {
        boolean z2 = false;
        if (TextUtils.isEmpty(str)) {
            this.zzamz.zzgo().zzjd().zzbx("Measurement Service called without app package");
            throw new SecurityException("Measurement Service called without app package");
        }
        if (z) {
            try {
                if (this.zzaql == null) {
                    if ("com.google.android.gms".equals(this.zzaqm) || UidVerifier.isGooglePlayServicesUid(this.zzamz.getContext(), Binder.getCallingUid()) || GoogleSignatureVerifier.getInstance(this.zzamz.getContext()).isUidGoogleSigned(Binder.getCallingUid())) {
                        z2 = true;
                    }
                    this.zzaql = Boolean.valueOf(z2);
                }
                if (this.zzaql.booleanValue()) {
                    return;
                }
            } catch (SecurityException e) {
                this.zzamz.zzgo().zzjd().zzg("Measurement Service called with invalid calling package. appId", zzap.zzbv(str));
                throw e;
            }
        }
        if (this.zzaqm == null && GooglePlayServicesUtilLight.uidHasPackageName(this.zzamz.getContext(), Binder.getCallingUid(), str)) {
            this.zzaqm = str;
        }
        if (!str.equals(this.zzaqm)) {
            throw new SecurityException(String.format("Unknown calling package name '%s'.", new Object[]{str}));
        }
    }

    public final void zza(long j, String str, String str2, String str3) {
        zze(new zzcn(this, str2, str3, str, j));
    }

    public final String zzc(zzh com_google_android_gms_measurement_internal_zzh) {
        zzb(com_google_android_gms_measurement_internal_zzh, false);
        return this.zzamz.zzh(com_google_android_gms_measurement_internal_zzh);
    }

    public final void zza(zzl com_google_android_gms_measurement_internal_zzl, zzh com_google_android_gms_measurement_internal_zzh) {
        Preconditions.checkNotNull(com_google_android_gms_measurement_internal_zzl);
        Preconditions.checkNotNull(com_google_android_gms_measurement_internal_zzl.zzahb);
        zzb(com_google_android_gms_measurement_internal_zzh, false);
        zzl com_google_android_gms_measurement_internal_zzl2 = new zzl(com_google_android_gms_measurement_internal_zzl);
        com_google_android_gms_measurement_internal_zzl2.packageName = com_google_android_gms_measurement_internal_zzh.packageName;
        if (com_google_android_gms_measurement_internal_zzl.zzahb.getValue() == null) {
            zze(new zzbx(this, com_google_android_gms_measurement_internal_zzl2, com_google_android_gms_measurement_internal_zzh));
        } else {
            zze(new zzby(this, com_google_android_gms_measurement_internal_zzl2, com_google_android_gms_measurement_internal_zzh));
        }
    }

    public final void zzb(zzl com_google_android_gms_measurement_internal_zzl) {
        Preconditions.checkNotNull(com_google_android_gms_measurement_internal_zzl);
        Preconditions.checkNotNull(com_google_android_gms_measurement_internal_zzl.zzahb);
        zzc(com_google_android_gms_measurement_internal_zzl.packageName, true);
        zzl com_google_android_gms_measurement_internal_zzl2 = new zzl(com_google_android_gms_measurement_internal_zzl);
        if (com_google_android_gms_measurement_internal_zzl.zzahb.getValue() == null) {
            zze(new zzbz(this, com_google_android_gms_measurement_internal_zzl2));
        } else {
            zze(new zzca(this, com_google_android_gms_measurement_internal_zzl2));
        }
    }

    public final List<zzfh> zza(String str, String str2, boolean z, zzh com_google_android_gms_measurement_internal_zzh) {
        Object e;
        zzb(com_google_android_gms_measurement_internal_zzh, false);
        try {
            List<zzfj> list = (List) this.zzamz.zzgn().zzb(new zzcb(this, com_google_android_gms_measurement_internal_zzh, str, str2)).get();
            List<zzfh> arrayList = new ArrayList(list.size());
            for (zzfj com_google_android_gms_measurement_internal_zzfj : list) {
                if (z || !zzfk.zzcv(com_google_android_gms_measurement_internal_zzfj.name)) {
                    arrayList.add(new zzfh(com_google_android_gms_measurement_internal_zzfj));
                }
            }
            return arrayList;
        } catch (InterruptedException e2) {
            e = e2;
            this.zzamz.zzgo().zzjd().zze("Failed to get user attributes. appId", zzap.zzbv(com_google_android_gms_measurement_internal_zzh.packageName), e);
            return Collections.emptyList();
        } catch (ExecutionException e3) {
            e = e3;
            this.zzamz.zzgo().zzjd().zze("Failed to get user attributes. appId", zzap.zzbv(com_google_android_gms_measurement_internal_zzh.packageName), e);
            return Collections.emptyList();
        }
    }

    public final List<zzfh> zza(String str, String str2, String str3, boolean z) {
        Object e;
        zzc(str, true);
        try {
            List<zzfj> list = (List) this.zzamz.zzgn().zzb(new zzcc(this, str, str2, str3)).get();
            List<zzfh> arrayList = new ArrayList(list.size());
            for (zzfj com_google_android_gms_measurement_internal_zzfj : list) {
                if (z || !zzfk.zzcv(com_google_android_gms_measurement_internal_zzfj.name)) {
                    arrayList.add(new zzfh(com_google_android_gms_measurement_internal_zzfj));
                }
            }
            return arrayList;
        } catch (InterruptedException e2) {
            e = e2;
            this.zzamz.zzgo().zzjd().zze("Failed to get user attributes. appId", zzap.zzbv(str), e);
            return Collections.emptyList();
        } catch (ExecutionException e3) {
            e = e3;
            this.zzamz.zzgo().zzjd().zze("Failed to get user attributes. appId", zzap.zzbv(str), e);
            return Collections.emptyList();
        }
    }

    public final List<zzl> zza(String str, String str2, zzh com_google_android_gms_measurement_internal_zzh) {
        Object e;
        zzb(com_google_android_gms_measurement_internal_zzh, false);
        try {
            return (List) this.zzamz.zzgn().zzb(new zzcd(this, com_google_android_gms_measurement_internal_zzh, str, str2)).get();
        } catch (InterruptedException e2) {
            e = e2;
        } catch (ExecutionException e3) {
            e = e3;
        }
        this.zzamz.zzgo().zzjd().zzg("Failed to get conditional user properties", e);
        return Collections.emptyList();
    }

    public final List<zzl> zze(String str, String str2, String str3) {
        Object e;
        zzc(str, true);
        try {
            return (List) this.zzamz.zzgn().zzb(new zzce(this, str, str2, str3)).get();
        } catch (InterruptedException e2) {
            e = e2;
        } catch (ExecutionException e3) {
            e = e3;
        }
        this.zzamz.zzgo().zzjd().zzg("Failed to get conditional user properties", e);
        return Collections.emptyList();
    }

    public final void zzd(zzh com_google_android_gms_measurement_internal_zzh) {
        zzc(com_google_android_gms_measurement_internal_zzh.packageName, false);
        zze(new zzcf(this, com_google_android_gms_measurement_internal_zzh));
    }

    private final void zze(Runnable runnable) {
        Preconditions.checkNotNull(runnable);
        if (((Boolean) zzaf.zzakv.get()).booleanValue() && this.zzamz.zzgn().zzkb()) {
            runnable.run();
        } else {
            this.zzamz.zzgn().zzc(runnable);
        }
    }
}
