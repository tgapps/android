package com.google.firebase.internal;

import android.content.Context;
import com.google.firebase.FirebaseApp;
import java.util.concurrent.atomic.AtomicReference;

public final class zzb {
    private static final AtomicReference<zzb> zzq = new AtomicReference();

    private zzb(Context context) {
    }

    public static void zzb(FirebaseApp firebaseApp) {
    }

    public static zzb zze(Context context) {
        zzq.compareAndSet(null, new zzb(context));
        return (zzb) zzq.get();
    }
}
