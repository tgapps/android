package com.google.android.gms.internal.config;

import com.google.firebase.abt.FirebaseABTesting;
import java.util.List;

public final class zzam implements Runnable {
    private final FirebaseABTesting zzam;
    private final List<byte[]> zzas;

    public zzam(FirebaseABTesting firebaseABTesting, List<byte[]> list) {
        this.zzam = firebaseABTesting;
        this.zzas = list;
    }

    public final void run() {
        if (this.zzam != null) {
            this.zzam.replaceAllExperiments(this.zzas);
        }
    }
}
