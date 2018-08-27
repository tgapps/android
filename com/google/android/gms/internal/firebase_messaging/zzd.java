package com.google.android.gms.internal.firebase_messaging;

import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;

final class zzd extends WeakReference<Throwable> {
    private final int zzf;

    public zzd(Throwable th, ReferenceQueue<Throwable> referenceQueue) {
        super(th, referenceQueue);
        if (th == null) {
            throw new NullPointerException("The referent cannot be null");
        }
        this.zzf = System.identityHashCode(th);
    }

    public final int hashCode() {
        return this.zzf;
    }

    public final boolean equals(Object obj) {
        if (obj == null || obj.getClass() != getClass()) {
            return false;
        }
        if (this == obj) {
            return true;
        }
        zzd com_google_android_gms_internal_firebase_messaging_zzd = (zzd) obj;
        if (this.zzf == com_google_android_gms_internal_firebase_messaging_zzd.zzf && get() == com_google_android_gms_internal_firebase_messaging_zzd.get()) {
            return true;
        }
        return false;
    }
}
