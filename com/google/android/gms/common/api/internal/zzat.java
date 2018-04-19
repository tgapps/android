package com.google.android.gms.common.api.internal;

abstract class zzat implements Runnable {
    private final /* synthetic */ zzaj zzhv;

    private zzat(zzaj com_google_android_gms_common_api_internal_zzaj) {
        this.zzhv = com_google_android_gms_common_api_internal_zzaj;
    }

    public void run() {
        this.zzhv.zzga.lock();
        try {
            if (!Thread.interrupted()) {
                zzaq();
                this.zzhv.zzga.unlock();
            }
        } catch (RuntimeException e) {
            this.zzhv.zzhf.zzb(e);
        } finally {
            this.zzhv.zzga.unlock();
        }
    }

    protected abstract void zzaq();
}
