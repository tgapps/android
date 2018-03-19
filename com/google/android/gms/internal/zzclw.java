package com.google.android.gms.internal;

import java.io.IOException;

public final class zzclw extends zzfjm<zzclw> {
    public Integer zzjko;
    public String zzjkp;
    public Boolean zzjkq;
    public String[] zzjkr;

    public zzclw() {
        this.zzjko = null;
        this.zzjkp = null;
        this.zzjkq = null;
        this.zzjkr = zzfjv.EMPTY_STRING_ARRAY;
        this.zzpnc = null;
        this.zzpfd = -1;
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private final com.google.android.gms.internal.zzclw zzi(com.google.android.gms.internal.zzfjj r8) throws java.io.IOException {
        /*
        r7 = this;
        r1 = 0;
    L_0x0001:
        r0 = r8.zzcvt();
        switch(r0) {
            case 0: goto L_0x000e;
            case 8: goto L_0x000f;
            case 18: goto L_0x0045;
            case 24: goto L_0x004c;
            case 34: goto L_0x0057;
            default: goto L_0x0008;
        };
    L_0x0008:
        r0 = super.zza(r8, r0);
        if (r0 != 0) goto L_0x0001;
    L_0x000e:
        return r7;
    L_0x000f:
        r2 = r8.getPosition();
        r3 = r8.zzcwi();	 Catch:{ IllegalArgumentException -> 0x0036 }
        switch(r3) {
            case 0: goto L_0x003e;
            case 1: goto L_0x003e;
            case 2: goto L_0x003e;
            case 3: goto L_0x003e;
            case 4: goto L_0x003e;
            case 5: goto L_0x003e;
            case 6: goto L_0x003e;
            default: goto L_0x001a;
        };	 Catch:{ IllegalArgumentException -> 0x0036 }
    L_0x001a:
        r4 = new java.lang.IllegalArgumentException;	 Catch:{ IllegalArgumentException -> 0x0036 }
        r5 = 41;
        r6 = new java.lang.StringBuilder;	 Catch:{ IllegalArgumentException -> 0x0036 }
        r6.<init>(r5);	 Catch:{ IllegalArgumentException -> 0x0036 }
        r3 = r6.append(r3);	 Catch:{ IllegalArgumentException -> 0x0036 }
        r5 = " is not a valid enum MatchType";
        r3 = r3.append(r5);	 Catch:{ IllegalArgumentException -> 0x0036 }
        r3 = r3.toString();	 Catch:{ IllegalArgumentException -> 0x0036 }
        r4.<init>(r3);	 Catch:{ IllegalArgumentException -> 0x0036 }
        throw r4;	 Catch:{ IllegalArgumentException -> 0x0036 }
    L_0x0036:
        r3 = move-exception;
        r8.zzmg(r2);
        r7.zza(r8, r0);
        goto L_0x0001;
    L_0x003e:
        r3 = java.lang.Integer.valueOf(r3);	 Catch:{ IllegalArgumentException -> 0x0036 }
        r7.zzjko = r3;	 Catch:{ IllegalArgumentException -> 0x0036 }
        goto L_0x0001;
    L_0x0045:
        r0 = r8.readString();
        r7.zzjkp = r0;
        goto L_0x0001;
    L_0x004c:
        r0 = r8.zzcvz();
        r0 = java.lang.Boolean.valueOf(r0);
        r7.zzjkq = r0;
        goto L_0x0001;
    L_0x0057:
        r0 = 34;
        r2 = com.google.android.gms.internal.zzfjv.zzb(r8, r0);
        r0 = r7.zzjkr;
        if (r0 != 0) goto L_0x007d;
    L_0x0061:
        r0 = r1;
    L_0x0062:
        r2 = r2 + r0;
        r2 = new java.lang.String[r2];
        if (r0 == 0) goto L_0x006c;
    L_0x0067:
        r3 = r7.zzjkr;
        java.lang.System.arraycopy(r3, r1, r2, r1, r0);
    L_0x006c:
        r3 = r2.length;
        r3 = r3 + -1;
        if (r0 >= r3) goto L_0x0081;
    L_0x0071:
        r3 = r8.readString();
        r2[r0] = r3;
        r8.zzcvt();
        r0 = r0 + 1;
        goto L_0x006c;
    L_0x007d:
        r0 = r7.zzjkr;
        r0 = r0.length;
        goto L_0x0062;
    L_0x0081:
        r3 = r8.readString();
        r2[r0] = r3;
        r7.zzjkr = r2;
        goto L_0x0001;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.internal.zzclw.zzi(com.google.android.gms.internal.zzfjj):com.google.android.gms.internal.zzclw");
    }

    public final boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof zzclw)) {
            return false;
        }
        zzclw com_google_android_gms_internal_zzclw = (zzclw) obj;
        if (this.zzjko == null) {
            if (com_google_android_gms_internal_zzclw.zzjko != null) {
                return false;
            }
        } else if (!this.zzjko.equals(com_google_android_gms_internal_zzclw.zzjko)) {
            return false;
        }
        if (this.zzjkp == null) {
            if (com_google_android_gms_internal_zzclw.zzjkp != null) {
                return false;
            }
        } else if (!this.zzjkp.equals(com_google_android_gms_internal_zzclw.zzjkp)) {
            return false;
        }
        if (this.zzjkq == null) {
            if (com_google_android_gms_internal_zzclw.zzjkq != null) {
                return false;
            }
        } else if (!this.zzjkq.equals(com_google_android_gms_internal_zzclw.zzjkq)) {
            return false;
        }
        return !zzfjq.equals(this.zzjkr, com_google_android_gms_internal_zzclw.zzjkr) ? false : (this.zzpnc == null || this.zzpnc.isEmpty()) ? com_google_android_gms_internal_zzclw.zzpnc == null || com_google_android_gms_internal_zzclw.zzpnc.isEmpty() : this.zzpnc.equals(com_google_android_gms_internal_zzclw.zzpnc);
    }

    public final int hashCode() {
        int i = 0;
        int hashCode = ((((this.zzjkq == null ? 0 : this.zzjkq.hashCode()) + (((this.zzjkp == null ? 0 : this.zzjkp.hashCode()) + (((this.zzjko == null ? 0 : this.zzjko.intValue()) + ((getClass().getName().hashCode() + 527) * 31)) * 31)) * 31)) * 31) + zzfjq.hashCode(this.zzjkr)) * 31;
        if (!(this.zzpnc == null || this.zzpnc.isEmpty())) {
            i = this.zzpnc.hashCode();
        }
        return hashCode + i;
    }

    public final /* synthetic */ zzfjs zza(zzfjj com_google_android_gms_internal_zzfjj) throws IOException {
        return zzi(com_google_android_gms_internal_zzfjj);
    }

    public final void zza(zzfjk com_google_android_gms_internal_zzfjk) throws IOException {
        if (this.zzjko != null) {
            com_google_android_gms_internal_zzfjk.zzaa(1, this.zzjko.intValue());
        }
        if (this.zzjkp != null) {
            com_google_android_gms_internal_zzfjk.zzn(2, this.zzjkp);
        }
        if (this.zzjkq != null) {
            com_google_android_gms_internal_zzfjk.zzl(3, this.zzjkq.booleanValue());
        }
        if (this.zzjkr != null && this.zzjkr.length > 0) {
            for (String str : this.zzjkr) {
                if (str != null) {
                    com_google_android_gms_internal_zzfjk.zzn(4, str);
                }
            }
        }
        super.zza(com_google_android_gms_internal_zzfjk);
    }

    protected final int zzq() {
        int i = 0;
        int zzq = super.zzq();
        if (this.zzjko != null) {
            zzq += zzfjk.zzad(1, this.zzjko.intValue());
        }
        if (this.zzjkp != null) {
            zzq += zzfjk.zzo(2, this.zzjkp);
        }
        if (this.zzjkq != null) {
            this.zzjkq.booleanValue();
            zzq += zzfjk.zzlg(3) + 1;
        }
        if (this.zzjkr == null || this.zzjkr.length <= 0) {
            return zzq;
        }
        int i2 = 0;
        int i3 = 0;
        while (i < this.zzjkr.length) {
            String str = this.zzjkr[i];
            if (str != null) {
                i3++;
                i2 += zzfjk.zztt(str);
            }
            i++;
        }
        return (zzq + i2) + (i3 * 1);
    }
}
