package com.google.android.gms.internal;

import java.io.IOException;

public final class zzclu extends zzfjm<zzclu> {
    public Integer zzjkg;
    public Boolean zzjkh;
    public String zzjki;
    public String zzjkj;
    public String zzjkk;

    public zzclu() {
        this.zzjkg = null;
        this.zzjkh = null;
        this.zzjki = null;
        this.zzjkj = null;
        this.zzjkk = null;
        this.zzpnc = null;
        this.zzpfd = -1;
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private final com.google.android.gms.internal.zzclu zzh(com.google.android.gms.internal.zzfjj r7) throws java.io.IOException {
        /*
        r6 = this;
    L_0x0000:
        r0 = r7.zzcvt();
        switch(r0) {
            case 0: goto L_0x000d;
            case 8: goto L_0x000e;
            case 16: goto L_0x0044;
            case 26: goto L_0x004f;
            case 34: goto L_0x0056;
            case 42: goto L_0x005d;
            default: goto L_0x0007;
        };
    L_0x0007:
        r0 = super.zza(r7, r0);
        if (r0 != 0) goto L_0x0000;
    L_0x000d:
        return r6;
    L_0x000e:
        r1 = r7.getPosition();
        r2 = r7.zzcwi();	 Catch:{ IllegalArgumentException -> 0x0035 }
        switch(r2) {
            case 0: goto L_0x003d;
            case 1: goto L_0x003d;
            case 2: goto L_0x003d;
            case 3: goto L_0x003d;
            case 4: goto L_0x003d;
            default: goto L_0x0019;
        };	 Catch:{ IllegalArgumentException -> 0x0035 }
    L_0x0019:
        r3 = new java.lang.IllegalArgumentException;	 Catch:{ IllegalArgumentException -> 0x0035 }
        r4 = 46;
        r5 = new java.lang.StringBuilder;	 Catch:{ IllegalArgumentException -> 0x0035 }
        r5.<init>(r4);	 Catch:{ IllegalArgumentException -> 0x0035 }
        r2 = r5.append(r2);	 Catch:{ IllegalArgumentException -> 0x0035 }
        r4 = " is not a valid enum ComparisonType";
        r2 = r2.append(r4);	 Catch:{ IllegalArgumentException -> 0x0035 }
        r2 = r2.toString();	 Catch:{ IllegalArgumentException -> 0x0035 }
        r3.<init>(r2);	 Catch:{ IllegalArgumentException -> 0x0035 }
        throw r3;	 Catch:{ IllegalArgumentException -> 0x0035 }
    L_0x0035:
        r2 = move-exception;
        r7.zzmg(r1);
        r6.zza(r7, r0);
        goto L_0x0000;
    L_0x003d:
        r2 = java.lang.Integer.valueOf(r2);	 Catch:{ IllegalArgumentException -> 0x0035 }
        r6.zzjkg = r2;	 Catch:{ IllegalArgumentException -> 0x0035 }
        goto L_0x0000;
    L_0x0044:
        r0 = r7.zzcvz();
        r0 = java.lang.Boolean.valueOf(r0);
        r6.zzjkh = r0;
        goto L_0x0000;
    L_0x004f:
        r0 = r7.readString();
        r6.zzjki = r0;
        goto L_0x0000;
    L_0x0056:
        r0 = r7.readString();
        r6.zzjkj = r0;
        goto L_0x0000;
    L_0x005d:
        r0 = r7.readString();
        r6.zzjkk = r0;
        goto L_0x0000;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.internal.zzclu.zzh(com.google.android.gms.internal.zzfjj):com.google.android.gms.internal.zzclu");
    }

    public final boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof zzclu)) {
            return false;
        }
        zzclu com_google_android_gms_internal_zzclu = (zzclu) obj;
        if (this.zzjkg == null) {
            if (com_google_android_gms_internal_zzclu.zzjkg != null) {
                return false;
            }
        } else if (!this.zzjkg.equals(com_google_android_gms_internal_zzclu.zzjkg)) {
            return false;
        }
        if (this.zzjkh == null) {
            if (com_google_android_gms_internal_zzclu.zzjkh != null) {
                return false;
            }
        } else if (!this.zzjkh.equals(com_google_android_gms_internal_zzclu.zzjkh)) {
            return false;
        }
        if (this.zzjki == null) {
            if (com_google_android_gms_internal_zzclu.zzjki != null) {
                return false;
            }
        } else if (!this.zzjki.equals(com_google_android_gms_internal_zzclu.zzjki)) {
            return false;
        }
        if (this.zzjkj == null) {
            if (com_google_android_gms_internal_zzclu.zzjkj != null) {
                return false;
            }
        } else if (!this.zzjkj.equals(com_google_android_gms_internal_zzclu.zzjkj)) {
            return false;
        }
        if (this.zzjkk == null) {
            if (com_google_android_gms_internal_zzclu.zzjkk != null) {
                return false;
            }
        } else if (!this.zzjkk.equals(com_google_android_gms_internal_zzclu.zzjkk)) {
            return false;
        }
        return (this.zzpnc == null || this.zzpnc.isEmpty()) ? com_google_android_gms_internal_zzclu.zzpnc == null || com_google_android_gms_internal_zzclu.zzpnc.isEmpty() : this.zzpnc.equals(com_google_android_gms_internal_zzclu.zzpnc);
    }

    public final int hashCode() {
        int i = 0;
        int hashCode = ((this.zzjkk == null ? 0 : this.zzjkk.hashCode()) + (((this.zzjkj == null ? 0 : this.zzjkj.hashCode()) + (((this.zzjki == null ? 0 : this.zzjki.hashCode()) + (((this.zzjkh == null ? 0 : this.zzjkh.hashCode()) + (((this.zzjkg == null ? 0 : this.zzjkg.intValue()) + ((getClass().getName().hashCode() + 527) * 31)) * 31)) * 31)) * 31)) * 31)) * 31;
        if (!(this.zzpnc == null || this.zzpnc.isEmpty())) {
            i = this.zzpnc.hashCode();
        }
        return hashCode + i;
    }

    public final /* synthetic */ zzfjs zza(zzfjj com_google_android_gms_internal_zzfjj) throws IOException {
        return zzh(com_google_android_gms_internal_zzfjj);
    }

    public final void zza(zzfjk com_google_android_gms_internal_zzfjk) throws IOException {
        if (this.zzjkg != null) {
            com_google_android_gms_internal_zzfjk.zzaa(1, this.zzjkg.intValue());
        }
        if (this.zzjkh != null) {
            com_google_android_gms_internal_zzfjk.zzl(2, this.zzjkh.booleanValue());
        }
        if (this.zzjki != null) {
            com_google_android_gms_internal_zzfjk.zzn(3, this.zzjki);
        }
        if (this.zzjkj != null) {
            com_google_android_gms_internal_zzfjk.zzn(4, this.zzjkj);
        }
        if (this.zzjkk != null) {
            com_google_android_gms_internal_zzfjk.zzn(5, this.zzjkk);
        }
        super.zza(com_google_android_gms_internal_zzfjk);
    }

    protected final int zzq() {
        int zzq = super.zzq();
        if (this.zzjkg != null) {
            zzq += zzfjk.zzad(1, this.zzjkg.intValue());
        }
        if (this.zzjkh != null) {
            this.zzjkh.booleanValue();
            zzq += zzfjk.zzlg(2) + 1;
        }
        if (this.zzjki != null) {
            zzq += zzfjk.zzo(3, this.zzjki);
        }
        if (this.zzjkj != null) {
            zzq += zzfjk.zzo(4, this.zzjkj);
        }
        return this.zzjkk != null ? zzq + zzfjk.zzo(5, this.zzjkk) : zzq;
    }
}
