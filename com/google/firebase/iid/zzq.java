package com.google.firebase.iid;

import android.util.Base64;
import com.google.android.gms.common.internal.Objects;
import java.security.KeyPair;

final class zzq {
    private final KeyPair zzbd;
    private final long zzbe;

    zzq(KeyPair keyPair, long j) {
        this.zzbd = keyPair;
        this.zzbe = j;
    }

    private final String zzq() {
        return Base64.encodeToString(this.zzbd.getPublic().getEncoded(), 11);
    }

    private final String zzr() {
        return Base64.encodeToString(this.zzbd.getPrivate().getEncoded(), 11);
    }

    public final boolean equals(Object obj) {
        if (!(obj instanceof zzq)) {
            return false;
        }
        zzq com_google_firebase_iid_zzq = (zzq) obj;
        return this.zzbe == com_google_firebase_iid_zzq.zzbe && this.zzbd.getPublic().equals(com_google_firebase_iid_zzq.zzbd.getPublic()) && this.zzbd.getPrivate().equals(com_google_firebase_iid_zzq.zzbd.getPrivate());
    }

    final KeyPair getKeyPair() {
        return this.zzbd;
    }

    public final int hashCode() {
        return Objects.hashCode(this.zzbd.getPublic(), this.zzbd.getPrivate(), Long.valueOf(this.zzbe));
    }
}
