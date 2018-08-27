package com.google.android.gms.internal.firebase_messaging;

import com.google.devtools.build.android.desugar.runtime.ThrowableExtension;

final class zzf extends zzb {
    zzf() {
    }

    public final void zza(Throwable th, Throwable th2) {
        ThrowableExtension.addSuppressed(th, th2);
    }
}
