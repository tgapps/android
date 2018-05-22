package com.google.android.gms.internal.measurement;

abstract class zzjq extends zzjp {
    private boolean zzvo;

    zzjq(zzjr com_google_android_gms_internal_measurement_zzjr) {
        super(com_google_android_gms_internal_measurement_zzjr);
        this.zzajp.zzb(this);
    }

    final boolean isInitialized() {
        return this.zzvo;
    }

    protected final void zzch() {
        if (!isInitialized()) {
            throw new IllegalStateException("Not initialized");
        }
    }

    protected abstract boolean zzhf();

    public final void zzm() {
        if (this.zzvo) {
            throw new IllegalStateException("Can't initialize twice");
        }
        zzhf();
        this.zzajp.zzkz();
        this.zzvo = true;
    }
}
