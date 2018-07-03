package com.google.android.gms.internal.measurement;

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

public final class zzgn extends zzez {
    private final zzjr zzajp;
    private Boolean zzanc;
    private String zzand;

    public zzgn(zzjr com_google_android_gms_internal_measurement_zzjr) {
        this(com_google_android_gms_internal_measurement_zzjr, null);
    }

    private zzgn(zzjr com_google_android_gms_internal_measurement_zzjr, String str) {
        Preconditions.checkNotNull(com_google_android_gms_internal_measurement_zzjr);
        this.zzajp = com_google_android_gms_internal_measurement_zzjr;
        this.zzand = null;
    }

    private final void zzb(zzdz com_google_android_gms_internal_measurement_zzdz, boolean z) {
        Preconditions.checkNotNull(com_google_android_gms_internal_measurement_zzdz);
        zzc(com_google_android_gms_internal_measurement_zzdz.packageName, false);
        this.zzajp.zzgb().zzcg(com_google_android_gms_internal_measurement_zzdz.zzadm);
    }

    private final void zzc(String str, boolean z) {
        boolean z2 = false;
        if (TextUtils.isEmpty(str)) {
            this.zzajp.zzge().zzim().log("Measurement Service called without app package");
            throw new SecurityException("Measurement Service called without app package");
        }
        if (z) {
            try {
                if (this.zzanc == null) {
                    if ("com.google.android.gms".equals(this.zzand) || UidVerifier.isGooglePlayServicesUid(this.zzajp.getContext(), Binder.getCallingUid()) || GoogleSignatureVerifier.getInstance(this.zzajp.getContext()).isUidGoogleSigned(Binder.getCallingUid())) {
                        z2 = true;
                    }
                    this.zzanc = Boolean.valueOf(z2);
                }
                if (this.zzanc.booleanValue()) {
                    return;
                }
            } catch (SecurityException e) {
                this.zzajp.zzge().zzim().zzg("Measurement Service called with invalid calling package. appId", zzfg.zzbm(str));
                throw e;
            }
        }
        if (this.zzand == null && GooglePlayServicesUtilLight.uidHasPackageName(this.zzajp.getContext(), Binder.getCallingUid(), str)) {
            this.zzand = str;
        }
        if (!str.equals(this.zzand)) {
            throw new SecurityException(String.format("Unknown calling package name '%s'.", new Object[]{str}));
        }
    }

    private final void zze(Runnable runnable) {
        Preconditions.checkNotNull(runnable);
        if (((Boolean) zzew.zzaia.get()).booleanValue() && this.zzajp.zzgd().zzjk()) {
            runnable.run();
        } else {
            this.zzajp.zzgd().zzc(runnable);
        }
    }

    public final List<zzjx> zza(zzdz com_google_android_gms_internal_measurement_zzdz, boolean z) {
        Object e;
        zzb(com_google_android_gms_internal_measurement_zzdz, false);
        try {
            List<zzjz> list = (List) this.zzajp.zzgd().zzb(new zzhd(this, com_google_android_gms_internal_measurement_zzdz)).get();
            List<zzjx> arrayList = new ArrayList(list.size());
            for (zzjz com_google_android_gms_internal_measurement_zzjz : list) {
                if (z || !zzka.zzci(com_google_android_gms_internal_measurement_zzjz.name)) {
                    arrayList.add(new zzjx(com_google_android_gms_internal_measurement_zzjz));
                }
            }
            return arrayList;
        } catch (InterruptedException e2) {
            e = e2;
            this.zzajp.zzge().zzim().zze("Failed to get user attributes. appId", zzfg.zzbm(com_google_android_gms_internal_measurement_zzdz.packageName), e);
            return null;
        } catch (ExecutionException e3) {
            e = e3;
            this.zzajp.zzge().zzim().zze("Failed to get user attributes. appId", zzfg.zzbm(com_google_android_gms_internal_measurement_zzdz.packageName), e);
            return null;
        }
    }

    public final List<zzed> zza(String str, String str2, zzdz com_google_android_gms_internal_measurement_zzdz) {
        Object e;
        zzb(com_google_android_gms_internal_measurement_zzdz, false);
        try {
            return (List) this.zzajp.zzgd().zzb(new zzgv(this, com_google_android_gms_internal_measurement_zzdz, str, str2)).get();
        } catch (InterruptedException e2) {
            e = e2;
        } catch (ExecutionException e3) {
            e = e3;
        }
        this.zzajp.zzge().zzim().zzg("Failed to get conditional user properties", e);
        return Collections.emptyList();
    }

    public final List<zzjx> zza(String str, String str2, String str3, boolean z) {
        Object e;
        zzc(str, true);
        try {
            List<zzjz> list = (List) this.zzajp.zzgd().zzb(new zzgu(this, str, str2, str3)).get();
            List<zzjx> arrayList = new ArrayList(list.size());
            for (zzjz com_google_android_gms_internal_measurement_zzjz : list) {
                if (z || !zzka.zzci(com_google_android_gms_internal_measurement_zzjz.name)) {
                    arrayList.add(new zzjx(com_google_android_gms_internal_measurement_zzjz));
                }
            }
            return arrayList;
        } catch (InterruptedException e2) {
            e = e2;
            this.zzajp.zzge().zzim().zze("Failed to get user attributes. appId", zzfg.zzbm(str), e);
            return Collections.emptyList();
        } catch (ExecutionException e3) {
            e = e3;
            this.zzajp.zzge().zzim().zze("Failed to get user attributes. appId", zzfg.zzbm(str), e);
            return Collections.emptyList();
        }
    }

    public final List<zzjx> zza(String str, String str2, boolean z, zzdz com_google_android_gms_internal_measurement_zzdz) {
        Object e;
        zzb(com_google_android_gms_internal_measurement_zzdz, false);
        try {
            List<zzjz> list = (List) this.zzajp.zzgd().zzb(new zzgt(this, com_google_android_gms_internal_measurement_zzdz, str, str2)).get();
            List<zzjx> arrayList = new ArrayList(list.size());
            for (zzjz com_google_android_gms_internal_measurement_zzjz : list) {
                if (z || !zzka.zzci(com_google_android_gms_internal_measurement_zzjz.name)) {
                    arrayList.add(new zzjx(com_google_android_gms_internal_measurement_zzjz));
                }
            }
            return arrayList;
        } catch (InterruptedException e2) {
            e = e2;
            this.zzajp.zzge().zzim().zze("Failed to get user attributes. appId", zzfg.zzbm(com_google_android_gms_internal_measurement_zzdz.packageName), e);
            return Collections.emptyList();
        } catch (ExecutionException e3) {
            e = e3;
            this.zzajp.zzge().zzim().zze("Failed to get user attributes. appId", zzfg.zzbm(com_google_android_gms_internal_measurement_zzdz.packageName), e);
            return Collections.emptyList();
        }
    }

    public final void zza(long j, String str, String str2, String str3) {
        zze(new zzhf(this, str2, str3, str, j));
    }

    public final void zza(zzdz com_google_android_gms_internal_measurement_zzdz) {
        zzb(com_google_android_gms_internal_measurement_zzdz, false);
        zze(new zzhe(this, com_google_android_gms_internal_measurement_zzdz));
    }

    public final void zza(zzed com_google_android_gms_internal_measurement_zzed, zzdz com_google_android_gms_internal_measurement_zzdz) {
        Preconditions.checkNotNull(com_google_android_gms_internal_measurement_zzed);
        Preconditions.checkNotNull(com_google_android_gms_internal_measurement_zzed.zzaep);
        zzb(com_google_android_gms_internal_measurement_zzdz, false);
        zzed com_google_android_gms_internal_measurement_zzed2 = new zzed(com_google_android_gms_internal_measurement_zzed);
        com_google_android_gms_internal_measurement_zzed2.packageName = com_google_android_gms_internal_measurement_zzdz.packageName;
        if (com_google_android_gms_internal_measurement_zzed.zzaep.getValue() == null) {
            zze(new zzgp(this, com_google_android_gms_internal_measurement_zzed2, com_google_android_gms_internal_measurement_zzdz));
        } else {
            zze(new zzgq(this, com_google_android_gms_internal_measurement_zzed2, com_google_android_gms_internal_measurement_zzdz));
        }
    }

    public final void zza(zzeu com_google_android_gms_internal_measurement_zzeu, zzdz com_google_android_gms_internal_measurement_zzdz) {
        Preconditions.checkNotNull(com_google_android_gms_internal_measurement_zzeu);
        zzb(com_google_android_gms_internal_measurement_zzdz, false);
        zze(new zzgy(this, com_google_android_gms_internal_measurement_zzeu, com_google_android_gms_internal_measurement_zzdz));
    }

    public final void zza(zzeu com_google_android_gms_internal_measurement_zzeu, String str, String str2) {
        Preconditions.checkNotNull(com_google_android_gms_internal_measurement_zzeu);
        Preconditions.checkNotEmpty(str);
        zzc(str, true);
        zze(new zzgz(this, com_google_android_gms_internal_measurement_zzeu, str));
    }

    public final void zza(zzjx com_google_android_gms_internal_measurement_zzjx, zzdz com_google_android_gms_internal_measurement_zzdz) {
        Preconditions.checkNotNull(com_google_android_gms_internal_measurement_zzjx);
        zzb(com_google_android_gms_internal_measurement_zzdz, false);
        if (com_google_android_gms_internal_measurement_zzjx.getValue() == null) {
            zze(new zzhb(this, com_google_android_gms_internal_measurement_zzjx, com_google_android_gms_internal_measurement_zzdz));
        } else {
            zze(new zzhc(this, com_google_android_gms_internal_measurement_zzjx, com_google_android_gms_internal_measurement_zzdz));
        }
    }

    public final byte[] zza(zzeu com_google_android_gms_internal_measurement_zzeu, String str) {
        Object e;
        Preconditions.checkNotEmpty(str);
        Preconditions.checkNotNull(com_google_android_gms_internal_measurement_zzeu);
        zzc(str, true);
        this.zzajp.zzge().zzis().zzg("Log and bundle. event", this.zzajp.zzga().zzbj(com_google_android_gms_internal_measurement_zzeu.name));
        long nanoTime = this.zzajp.zzbt().nanoTime() / 1000000;
        try {
            byte[] bArr = (byte[]) this.zzajp.zzgd().zzc(new zzha(this, com_google_android_gms_internal_measurement_zzeu, str)).get();
            if (bArr == null) {
                this.zzajp.zzge().zzim().zzg("Log and bundle returned null. appId", zzfg.zzbm(str));
                bArr = new byte[0];
            }
            this.zzajp.zzge().zzis().zzd("Log and bundle processed. event, size, time_ms", this.zzajp.zzga().zzbj(com_google_android_gms_internal_measurement_zzeu.name), Integer.valueOf(bArr.length), Long.valueOf((this.zzajp.zzbt().nanoTime() / 1000000) - nanoTime));
            return bArr;
        } catch (InterruptedException e2) {
            e = e2;
            this.zzajp.zzge().zzim().zzd("Failed to log and bundle. appId, event, error", zzfg.zzbm(str), this.zzajp.zzga().zzbj(com_google_android_gms_internal_measurement_zzeu.name), e);
            return null;
        } catch (ExecutionException e3) {
            e = e3;
            this.zzajp.zzge().zzim().zzd("Failed to log and bundle. appId, event, error", zzfg.zzbm(str), this.zzajp.zzga().zzbj(com_google_android_gms_internal_measurement_zzeu.name), e);
            return null;
        }
    }

    public final void zzb(zzdz com_google_android_gms_internal_measurement_zzdz) {
        zzb(com_google_android_gms_internal_measurement_zzdz, false);
        zze(new zzgo(this, com_google_android_gms_internal_measurement_zzdz));
    }

    public final void zzb(zzed com_google_android_gms_internal_measurement_zzed) {
        Preconditions.checkNotNull(com_google_android_gms_internal_measurement_zzed);
        Preconditions.checkNotNull(com_google_android_gms_internal_measurement_zzed.zzaep);
        zzc(com_google_android_gms_internal_measurement_zzed.packageName, true);
        zzed com_google_android_gms_internal_measurement_zzed2 = new zzed(com_google_android_gms_internal_measurement_zzed);
        if (com_google_android_gms_internal_measurement_zzed.zzaep.getValue() == null) {
            zze(new zzgr(this, com_google_android_gms_internal_measurement_zzed2));
        } else {
            zze(new zzgs(this, com_google_android_gms_internal_measurement_zzed2));
        }
    }

    public final String zzc(zzdz com_google_android_gms_internal_measurement_zzdz) {
        zzb(com_google_android_gms_internal_measurement_zzdz, false);
        return this.zzajp.zzh(com_google_android_gms_internal_measurement_zzdz);
    }

    public final void zzd(zzdz com_google_android_gms_internal_measurement_zzdz) {
        zzc(com_google_android_gms_internal_measurement_zzdz.packageName, false);
        zze(new zzgx(this, com_google_android_gms_internal_measurement_zzdz));
    }

    public final List<zzed> zze(String str, String str2, String str3) {
        Object e;
        zzc(str, true);
        try {
            return (List) this.zzajp.zzgd().zzb(new zzgw(this, str, str2, str3)).get();
        } catch (InterruptedException e2) {
            e = e2;
        } catch (ExecutionException e3) {
            e = e3;
        }
        this.zzajp.zzge().zzim().zzg("Failed to get conditional user properties", e);
        return Collections.emptyList();
    }
}
