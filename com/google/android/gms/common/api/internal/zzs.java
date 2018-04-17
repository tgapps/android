package com.google.android.gms.common.api.internal;

final class zzs implements Runnable {
    private final /* synthetic */ zzr zzgc;

    public final void run() {
        this.zzgc.zzga.lock();
        try {
            this.zzgc.zzaa();
        } finally {
            this.zzgc.zzga.unlock();
        }
    }
}
