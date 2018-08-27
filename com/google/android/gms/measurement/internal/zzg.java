package com.google.android.gms.measurement.internal;

import android.text.TextUtils;
import com.google.android.gms.common.internal.Preconditions;

final class zzg {
    private final zzbt zzadj;
    private long zzadt;
    private String zzafw;
    private String zzafx;
    private String zzafy;
    private String zzafz;
    private long zzaga;
    private long zzagb;
    private long zzagc;
    private long zzagd;
    private String zzage;
    private long zzagf;
    private boolean zzagg;
    private long zzagh;
    private boolean zzagi;
    private boolean zzagj;
    private String zzagk;
    private long zzagl;
    private long zzagm;
    private long zzagn;
    private long zzago;
    private long zzagp;
    private long zzagq;
    private String zzagr;
    private boolean zzags;
    private long zzagt;
    private long zzagu;
    private String zzts;
    private final String zztt;

    zzg(zzbt com_google_android_gms_measurement_internal_zzbt, String str) {
        Preconditions.checkNotNull(com_google_android_gms_measurement_internal_zzbt);
        Preconditions.checkNotEmpty(str);
        this.zzadj = com_google_android_gms_measurement_internal_zzbt;
        this.zztt = str;
        this.zzadj.zzgn().zzaf();
    }

    public final void zzgv() {
        this.zzadj.zzgn().zzaf();
        this.zzags = false;
    }

    public final String zzal() {
        this.zzadj.zzgn().zzaf();
        return this.zztt;
    }

    public final String getAppInstanceId() {
        this.zzadj.zzgn().zzaf();
        return this.zzafw;
    }

    public final void zzam(String str) {
        this.zzadj.zzgn().zzaf();
        this.zzags = (!zzfk.zzu(this.zzafw, str) ? 1 : 0) | this.zzags;
        this.zzafw = str;
    }

    public final String getGmpAppId() {
        this.zzadj.zzgn().zzaf();
        return this.zzafx;
    }

    public final void zzan(String str) {
        this.zzadj.zzgn().zzaf();
        if (TextUtils.isEmpty(str)) {
            str = null;
        }
        this.zzags = (!zzfk.zzu(this.zzafx, str) ? 1 : 0) | this.zzags;
        this.zzafx = str;
    }

    public final String zzgw() {
        this.zzadj.zzgn().zzaf();
        return this.zzagk;
    }

    public final void zzao(String str) {
        this.zzadj.zzgn().zzaf();
        if (TextUtils.isEmpty(str)) {
            str = null;
        }
        this.zzags = (!zzfk.zzu(this.zzagk, str) ? 1 : 0) | this.zzags;
        this.zzagk = str;
    }

    public final String zzgx() {
        this.zzadj.zzgn().zzaf();
        return this.zzafy;
    }

    public final void zzap(String str) {
        this.zzadj.zzgn().zzaf();
        this.zzags = (!zzfk.zzu(this.zzafy, str) ? 1 : 0) | this.zzags;
        this.zzafy = str;
    }

    public final String getFirebaseInstanceId() {
        this.zzadj.zzgn().zzaf();
        return this.zzafz;
    }

    public final void zzaq(String str) {
        this.zzadj.zzgn().zzaf();
        this.zzags = (!zzfk.zzu(this.zzafz, str) ? 1 : 0) | this.zzags;
        this.zzafz = str;
    }

    public final long zzgy() {
        this.zzadj.zzgn().zzaf();
        return this.zzagb;
    }

    public final void zzs(long j) {
        this.zzadj.zzgn().zzaf();
        this.zzags = (this.zzagb != j ? 1 : 0) | this.zzags;
        this.zzagb = j;
    }

    public final long zzgz() {
        this.zzadj.zzgn().zzaf();
        return this.zzagc;
    }

    public final void zzt(long j) {
        this.zzadj.zzgn().zzaf();
        this.zzags = (this.zzagc != j ? 1 : 0) | this.zzags;
        this.zzagc = j;
    }

    public final String zzak() {
        this.zzadj.zzgn().zzaf();
        return this.zzts;
    }

    public final void setAppVersion(String str) {
        this.zzadj.zzgn().zzaf();
        this.zzags = (!zzfk.zzu(this.zzts, str) ? 1 : 0) | this.zzags;
        this.zzts = str;
    }

    public final long zzha() {
        this.zzadj.zzgn().zzaf();
        return this.zzagd;
    }

    public final void zzu(long j) {
        this.zzadj.zzgn().zzaf();
        this.zzags = (this.zzagd != j ? 1 : 0) | this.zzags;
        this.zzagd = j;
    }

    public final String zzhb() {
        this.zzadj.zzgn().zzaf();
        return this.zzage;
    }

    public final void zzar(String str) {
        this.zzadj.zzgn().zzaf();
        this.zzags = (!zzfk.zzu(this.zzage, str) ? 1 : 0) | this.zzags;
        this.zzage = str;
    }

    public final long zzhc() {
        this.zzadj.zzgn().zzaf();
        return this.zzadt;
    }

    public final void zzv(long j) {
        this.zzadj.zzgn().zzaf();
        this.zzags = (this.zzadt != j ? 1 : 0) | this.zzags;
        this.zzadt = j;
    }

    public final long zzhd() {
        this.zzadj.zzgn().zzaf();
        return this.zzagf;
    }

    public final void zzw(long j) {
        this.zzadj.zzgn().zzaf();
        this.zzags = (this.zzagf != j ? 1 : 0) | this.zzags;
        this.zzagf = j;
    }

    public final boolean isMeasurementEnabled() {
        this.zzadj.zzgn().zzaf();
        return this.zzagg;
    }

    public final void setMeasurementEnabled(boolean z) {
        this.zzadj.zzgn().zzaf();
        this.zzags = (this.zzagg != z ? 1 : 0) | this.zzags;
        this.zzagg = z;
    }

    public final void zzx(long j) {
        int i = 1;
        Preconditions.checkArgument(j >= 0);
        this.zzadj.zzgn().zzaf();
        boolean z = this.zzags;
        if (this.zzaga == j) {
            i = 0;
        }
        this.zzags = z | i;
        this.zzaga = j;
    }

    public final long zzhe() {
        this.zzadj.zzgn().zzaf();
        return this.zzaga;
    }

    public final long zzhf() {
        this.zzadj.zzgn().zzaf();
        return this.zzagt;
    }

    public final void zzy(long j) {
        this.zzadj.zzgn().zzaf();
        this.zzags = (this.zzagt != j ? 1 : 0) | this.zzags;
        this.zzagt = j;
    }

    public final long zzhg() {
        this.zzadj.zzgn().zzaf();
        return this.zzagu;
    }

    public final void zzz(long j) {
        this.zzadj.zzgn().zzaf();
        this.zzags = (this.zzagu != j ? 1 : 0) | this.zzags;
        this.zzagu = j;
    }

    public final void zzhh() {
        this.zzadj.zzgn().zzaf();
        long j = this.zzaga + 1;
        if (j > 2147483647L) {
            this.zzadj.zzgo().zzjg().zzg("Bundle index overflow. appId", zzap.zzbv(this.zztt));
            j = 0;
        }
        this.zzags = true;
        this.zzaga = j;
    }

    public final long zzhi() {
        this.zzadj.zzgn().zzaf();
        return this.zzagl;
    }

    public final void zzaa(long j) {
        this.zzadj.zzgn().zzaf();
        this.zzags = (this.zzagl != j ? 1 : 0) | this.zzags;
        this.zzagl = j;
    }

    public final long zzhj() {
        this.zzadj.zzgn().zzaf();
        return this.zzagm;
    }

    public final void zzab(long j) {
        this.zzadj.zzgn().zzaf();
        this.zzags = (this.zzagm != j ? 1 : 0) | this.zzags;
        this.zzagm = j;
    }

    public final long zzhk() {
        this.zzadj.zzgn().zzaf();
        return this.zzagn;
    }

    public final void zzac(long j) {
        this.zzadj.zzgn().zzaf();
        this.zzags = (this.zzagn != j ? 1 : 0) | this.zzags;
        this.zzagn = j;
    }

    public final long zzhl() {
        this.zzadj.zzgn().zzaf();
        return this.zzago;
    }

    public final void zzad(long j) {
        this.zzadj.zzgn().zzaf();
        this.zzags = (this.zzago != j ? 1 : 0) | this.zzags;
        this.zzago = j;
    }

    public final long zzhm() {
        this.zzadj.zzgn().zzaf();
        return this.zzagq;
    }

    public final void zzae(long j) {
        this.zzadj.zzgn().zzaf();
        this.zzags = (this.zzagq != j ? 1 : 0) | this.zzags;
        this.zzagq = j;
    }

    public final long zzhn() {
        this.zzadj.zzgn().zzaf();
        return this.zzagp;
    }

    public final void zzaf(long j) {
        this.zzadj.zzgn().zzaf();
        this.zzags = (this.zzagp != j ? 1 : 0) | this.zzags;
        this.zzagp = j;
    }

    public final String zzho() {
        this.zzadj.zzgn().zzaf();
        return this.zzagr;
    }

    public final String zzhp() {
        this.zzadj.zzgn().zzaf();
        String str = this.zzagr;
        zzas(null);
        return str;
    }

    public final void zzas(String str) {
        this.zzadj.zzgn().zzaf();
        this.zzags = (!zzfk.zzu(this.zzagr, str) ? 1 : 0) | this.zzags;
        this.zzagr = str;
    }

    public final long zzhq() {
        this.zzadj.zzgn().zzaf();
        return this.zzagh;
    }

    public final void zzag(long j) {
        this.zzadj.zzgn().zzaf();
        this.zzags = (this.zzagh != j ? 1 : 0) | this.zzags;
        this.zzagh = j;
    }

    public final boolean zzhr() {
        this.zzadj.zzgn().zzaf();
        return this.zzagi;
    }

    public final void zze(boolean z) {
        this.zzadj.zzgn().zzaf();
        this.zzags = this.zzagi != z;
        this.zzagi = z;
    }

    public final boolean zzhs() {
        this.zzadj.zzgn().zzaf();
        return this.zzagj;
    }

    public final void zzf(boolean z) {
        this.zzadj.zzgn().zzaf();
        this.zzags = this.zzagj != z;
        this.zzagj = z;
    }
}
