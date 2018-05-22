package com.google.android.gms.tasks;

final class zzd implements Runnable {
    private final /* synthetic */ Task zzafn;
    private final /* synthetic */ zzc zzafo;

    zzd(zzc com_google_android_gms_tasks_zzc, Task task) {
        this.zzafo = com_google_android_gms_tasks_zzc;
        this.zzafn = task;
    }

    public final void run() {
        if (this.zzafn.isCanceled()) {
            this.zzafo.zzafm.zzdp();
            return;
        }
        try {
            this.zzafo.zzafm.setResult(this.zzafo.zzafl.then(this.zzafn));
        } catch (Exception e) {
            if (e.getCause() instanceof Exception) {
                this.zzafo.zzafm.setException((Exception) e.getCause());
            } else {
                this.zzafo.zzafm.setException(e);
            }
        } catch (Exception e2) {
            this.zzafo.zzafm.setException(e2);
        }
    }
}
