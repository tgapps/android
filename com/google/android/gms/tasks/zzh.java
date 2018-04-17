package com.google.android.gms.tasks;

final class zzh implements Runnable {
    private final /* synthetic */ zzg zzafr;

    zzh(zzg com_google_android_gms_tasks_zzg) {
        this.zzafr = com_google_android_gms_tasks_zzg;
    }

    public final void run() {
        synchronized (this.zzafr.mLock) {
            if (this.zzafr.zzafq != null) {
                this.zzafr.zzafq.onCanceled();
            }
        }
    }
}
