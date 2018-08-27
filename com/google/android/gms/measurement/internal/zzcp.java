package com.google.android.gms.measurement.internal;

abstract class zzcp extends zzco {
    private boolean zzvz;

    zzcp(zzbt com_google_android_gms_measurement_internal_zzbt) {
        super(com_google_android_gms_measurement_internal_zzbt);
        this.zzadj.zzb(this);
    }

    protected abstract boolean zzgt();

    final boolean isInitialized() {
        return this.zzvz;
    }

    protected final void zzcl() {
        if (!isInitialized()) {
            throw new IllegalStateException("Not initialized");
        }
    }

    public final void zzq() {
        if (this.zzvz) {
            throw new IllegalStateException("Can't initialize twice");
        } else if (!zzgt()) {
            this.zzadj.zzkq();
            this.zzvz = true;
        }
    }

    public final void zzgs() {
        if (this.zzvz) {
            throw new IllegalStateException("Can't initialize twice");
        }
        zzgu();
        this.zzadj.zzkq();
        this.zzvz = true;
    }

    protected void zzgu() {
    }
}
