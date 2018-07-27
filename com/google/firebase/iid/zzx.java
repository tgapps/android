package com.google.firebase.iid;

import android.util.Base64;
import com.google.android.gms.common.internal.Objects;
import java.security.KeyPair;

final class zzx {
    private final KeyPair zzbi;
    private final long zzbj;

    zzx(KeyPair keyPair, long j) {
        this.zzbi = keyPair;
        this.zzbj = j;
    }

    private final String zzu() {
        return Base64.encodeToString(this.zzbi.getPublic().getEncoded(), 11);
    }

    private final String zzv() {
        return Base64.encodeToString(this.zzbi.getPrivate().getEncoded(), 11);
    }

    public final boolean equals(Object obj) {
        if (!(obj instanceof zzx)) {
            return false;
        }
        zzx com_google_firebase_iid_zzx = (zzx) obj;
        return this.zzbj == com_google_firebase_iid_zzx.zzbj && this.zzbi.getPublic().equals(com_google_firebase_iid_zzx.zzbi.getPublic()) && this.zzbi.getPrivate().equals(com_google_firebase_iid_zzx.zzbi.getPrivate());
    }

    final KeyPair getKeyPair() {
        return this.zzbi;
    }

    public final int hashCode() {
        return Objects.hashCode(this.zzbi.getPublic(), this.zzbi.getPrivate(), Long.valueOf(this.zzbj));
    }
}
