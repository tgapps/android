package com.google.android.gms.internal.measurement;

import android.text.TextUtils;
import com.google.android.gms.common.internal.Preconditions;

final class zzeb {
    private final zzgl zzacr;
    private String zzadg;
    private String zzadh;
    private String zzadi;
    private String zzadj;
    private long zzadk;
    private long zzadl;
    private long zzadm;
    private long zzadn;
    private String zzado;
    private long zzadp;
    private long zzadq;
    private boolean zzadr;
    private long zzads;
    private boolean zzadt;
    private boolean zzadu;
    private long zzadv;
    private long zzadw;
    private long zzadx;
    private long zzady;
    private long zzadz;
    private long zzaea;
    private String zzaeb;
    private boolean zzaec;
    private long zzaed;
    private long zzaee;
    private String zztc;
    private final String zztd;

    zzeb(zzgl com_google_android_gms_internal_measurement_zzgl, String str) {
        Preconditions.checkNotNull(com_google_android_gms_internal_measurement_zzgl);
        Preconditions.checkNotEmpty(str);
        this.zzacr = com_google_android_gms_internal_measurement_zzgl;
        this.zztd = str;
        this.zzacr.zzgf().zzab();
    }

    public final String getAppInstanceId() {
        this.zzacr.zzgf().zzab();
        return this.zzadg;
    }

    public final String getGmpAppId() {
        this.zzacr.zzgf().zzab();
        return this.zzadh;
    }

    public final boolean isMeasurementEnabled() {
        this.zzacr.zzgf().zzab();
        return this.zzadr;
    }

    public final void setAppVersion(String str) {
        this.zzacr.zzgf().zzab();
        this.zzaec = (!zzjv.zzs(this.zztc, str) ? 1 : 0) | this.zzaec;
        this.zztc = str;
    }

    public final void setMeasurementEnabled(boolean z) {
        this.zzacr.zzgf().zzab();
        this.zzaec = (this.zzadr != z ? 1 : 0) | this.zzaec;
        this.zzadr = z;
    }

    public final void zzaa(long j) {
        this.zzacr.zzgf().zzab();
        this.zzaec = (this.zzads != j ? 1 : 0) | this.zzaec;
        this.zzads = j;
    }

    public final String zzag() {
        this.zzacr.zzgf().zzab();
        return this.zztc;
    }

    public final String zzah() {
        this.zzacr.zzgf().zzab();
        return this.zztd;
    }

    public final void zzal(String str) {
        this.zzacr.zzgf().zzab();
        this.zzaec = (!zzjv.zzs(this.zzadg, str) ? 1 : 0) | this.zzaec;
        this.zzadg = str;
    }

    public final void zzam(String str) {
        this.zzacr.zzgf().zzab();
        if (TextUtils.isEmpty(str)) {
            str = null;
        }
        this.zzaec = (!zzjv.zzs(this.zzadh, str) ? 1 : 0) | this.zzaec;
        this.zzadh = str;
    }

    public final void zzan(String str) {
        this.zzacr.zzgf().zzab();
        this.zzaec = (!zzjv.zzs(this.zzadi, str) ? 1 : 0) | this.zzaec;
        this.zzadi = str;
    }

    public final void zzao(String str) {
        this.zzacr.zzgf().zzab();
        this.zzaec = (!zzjv.zzs(this.zzadj, str) ? 1 : 0) | this.zzaec;
        this.zzadj = str;
    }

    public final void zzap(String str) {
        this.zzacr.zzgf().zzab();
        this.zzaec = (!zzjv.zzs(this.zzado, str) ? 1 : 0) | this.zzaec;
        this.zzado = str;
    }

    public final void zzaq(String str) {
        this.zzacr.zzgf().zzab();
        this.zzaec = (!zzjv.zzs(this.zzaeb, str) ? 1 : 0) | this.zzaec;
        this.zzaeb = str;
    }

    public final void zzd(boolean z) {
        this.zzacr.zzgf().zzab();
        this.zzaec = this.zzadt != z;
        this.zzadt = z;
    }

    public final void zze(boolean z) {
        this.zzacr.zzgf().zzab();
        this.zzaec = this.zzadu != z;
        this.zzadu = z;
    }

    public final void zzgj() {
        this.zzacr.zzgf().zzab();
        this.zzaec = false;
    }

    public final String zzgk() {
        this.zzacr.zzgf().zzab();
        return this.zzadi;
    }

    public final String zzgl() {
        this.zzacr.zzgf().zzab();
        return this.zzadj;
    }

    public final long zzgm() {
        this.zzacr.zzgf().zzab();
        return this.zzadl;
    }

    public final long zzgn() {
        this.zzacr.zzgf().zzab();
        return this.zzadm;
    }

    public final long zzgo() {
        this.zzacr.zzgf().zzab();
        return this.zzadn;
    }

    public final String zzgp() {
        this.zzacr.zzgf().zzab();
        return this.zzado;
    }

    public final long zzgq() {
        this.zzacr.zzgf().zzab();
        return this.zzadp;
    }

    public final long zzgr() {
        this.zzacr.zzgf().zzab();
        return this.zzadq;
    }

    public final long zzgs() {
        this.zzacr.zzgf().zzab();
        return this.zzadk;
    }

    public final long zzgt() {
        this.zzacr.zzgf().zzab();
        return this.zzaed;
    }

    public final long zzgu() {
        this.zzacr.zzgf().zzab();
        return this.zzaee;
    }

    public final void zzgv() {
        this.zzacr.zzgf().zzab();
        long j = this.zzadk + 1;
        if (j > 2147483647L) {
            this.zzacr.zzgg().zzin().zzg("Bundle index overflow. appId", zzfg.zzbh(this.zztd));
            j = 0;
        }
        this.zzaec = true;
        this.zzadk = j;
    }

    public final long zzgw() {
        this.zzacr.zzgf().zzab();
        return this.zzadv;
    }

    public final long zzgx() {
        this.zzacr.zzgf().zzab();
        return this.zzadw;
    }

    public final long zzgy() {
        this.zzacr.zzgf().zzab();
        return this.zzadx;
    }

    public final long zzgz() {
        this.zzacr.zzgf().zzab();
        return this.zzady;
    }

    public final long zzha() {
        this.zzacr.zzgf().zzab();
        return this.zzaea;
    }

    public final long zzhb() {
        this.zzacr.zzgf().zzab();
        return this.zzadz;
    }

    public final String zzhc() {
        this.zzacr.zzgf().zzab();
        return this.zzaeb;
    }

    public final String zzhd() {
        this.zzacr.zzgf().zzab();
        String str = this.zzaeb;
        zzaq(null);
        return str;
    }

    public final long zzhe() {
        this.zzacr.zzgf().zzab();
        return this.zzads;
    }

    public final boolean zzhf() {
        this.zzacr.zzgf().zzab();
        return this.zzadt;
    }

    public final boolean zzhg() {
        this.zzacr.zzgf().zzab();
        return this.zzadu;
    }

    public final void zzm(long j) {
        this.zzacr.zzgf().zzab();
        this.zzaec = (this.zzadl != j ? 1 : 0) | this.zzaec;
        this.zzadl = j;
    }

    public final void zzn(long j) {
        this.zzacr.zzgf().zzab();
        this.zzaec = (this.zzadm != j ? 1 : 0) | this.zzaec;
        this.zzadm = j;
    }

    public final void zzo(long j) {
        this.zzacr.zzgf().zzab();
        this.zzaec = (this.zzadn != j ? 1 : 0) | this.zzaec;
        this.zzadn = j;
    }

    public final void zzp(long j) {
        this.zzacr.zzgf().zzab();
        this.zzaec = (this.zzadp != j ? 1 : 0) | this.zzaec;
        this.zzadp = j;
    }

    public final void zzq(long j) {
        this.zzacr.zzgf().zzab();
        this.zzaec = (this.zzadq != j ? 1 : 0) | this.zzaec;
        this.zzadq = j;
    }

    public final void zzr(long j) {
        int i = 1;
        Preconditions.checkArgument(j >= 0);
        this.zzacr.zzgf().zzab();
        boolean z = this.zzaec;
        if (this.zzadk == j) {
            i = 0;
        }
        this.zzaec = z | i;
        this.zzadk = j;
    }

    public final void zzs(long j) {
        this.zzacr.zzgf().zzab();
        this.zzaec = (this.zzaed != j ? 1 : 0) | this.zzaec;
        this.zzaed = j;
    }

    public final void zzt(long j) {
        this.zzacr.zzgf().zzab();
        this.zzaec = (this.zzaee != j ? 1 : 0) | this.zzaec;
        this.zzaee = j;
    }

    public final void zzu(long j) {
        this.zzacr.zzgf().zzab();
        this.zzaec = (this.zzadv != j ? 1 : 0) | this.zzaec;
        this.zzadv = j;
    }

    public final void zzv(long j) {
        this.zzacr.zzgf().zzab();
        this.zzaec = (this.zzadw != j ? 1 : 0) | this.zzaec;
        this.zzadw = j;
    }

    public final void zzw(long j) {
        this.zzacr.zzgf().zzab();
        this.zzaec = (this.zzadx != j ? 1 : 0) | this.zzaec;
        this.zzadx = j;
    }

    public final void zzx(long j) {
        this.zzacr.zzgf().zzab();
        this.zzaec = (this.zzady != j ? 1 : 0) | this.zzaec;
        this.zzady = j;
    }

    public final void zzy(long j) {
        this.zzacr.zzgf().zzab();
        this.zzaec = (this.zzaea != j ? 1 : 0) | this.zzaec;
        this.zzaea = j;
    }

    public final void zzz(long j) {
        this.zzacr.zzgf().zzab();
        this.zzaec = (this.zzadz != j ? 1 : 0) | this.zzaec;
        this.zzadz = j;
    }
}
