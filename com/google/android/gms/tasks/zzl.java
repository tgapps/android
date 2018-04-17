package com.google.android.gms.tasks;

final class zzl implements Runnable {
    private final /* synthetic */ Task zzafn;
    private final /* synthetic */ zzk zzafv;

    zzl(zzk com_google_android_gms_tasks_zzk, Task task) {
        this.zzafv = com_google_android_gms_tasks_zzk;
        this.zzafn = task;
    }

    public final void run() {
        synchronized (this.zzafv.mLock) {
            if (this.zzafv.zzafu != null) {
                this.zzafv.zzafu.onFailure(this.zzafn.getException());
            }
        }
    }
}
