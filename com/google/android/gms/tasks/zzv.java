package com.google.android.gms.tasks;

import java.util.concurrent.Callable;

final class zzv implements Runnable {
    private final /* synthetic */ Callable val$callable;
    private final /* synthetic */ zzu zzagj;

    zzv(zzu com_google_android_gms_tasks_zzu, Callable callable) {
        this.zzagj = com_google_android_gms_tasks_zzu;
        this.val$callable = callable;
    }

    public final void run() {
        try {
            this.zzagj.setResult(this.val$callable.call());
        } catch (Exception e) {
            this.zzagj.setException(e);
        }
    }
}
