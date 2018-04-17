package com.google.android.gms.tasks;

final class zzn implements Runnable {
    private final /* synthetic */ Task zzafn;
    private final /* synthetic */ zzm zzafx;

    zzn(zzm com_google_android_gms_tasks_zzm, Task task) {
        this.zzafx = com_google_android_gms_tasks_zzm;
        this.zzafn = task;
    }

    public final void run() {
        synchronized (this.zzafx.mLock) {
            if (this.zzafx.zzafw != null) {
                this.zzafx.zzafw.onSuccess(this.zzafn.getResult());
            }
        }
    }
}
