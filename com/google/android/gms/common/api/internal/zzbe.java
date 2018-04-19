package com.google.android.gms.common.api.internal;

abstract class zzbe {
    private final zzbc zzjg;

    protected zzbe(zzbc com_google_android_gms_common_api_internal_zzbc) {
        this.zzjg = com_google_android_gms_common_api_internal_zzbc;
    }

    protected abstract void zzaq();

    public final void zzc(zzbd com_google_android_gms_common_api_internal_zzbd) {
        com_google_android_gms_common_api_internal_zzbd.zzga.lock();
        try {
            if (com_google_android_gms_common_api_internal_zzbd.zzjc == this.zzjg) {
                zzaq();
                com_google_android_gms_common_api_internal_zzbd.zzga.unlock();
            }
        } finally {
            com_google_android_gms_common_api_internal_zzbd.zzga.unlock();
        }
    }
}
