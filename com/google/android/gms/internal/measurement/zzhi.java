package com.google.android.gms.internal.measurement;

abstract class zzhi extends zzhh {
    private boolean zzvo;

    zzhi(zzgm com_google_android_gms_internal_measurement_zzgm) {
        super(com_google_android_gms_internal_measurement_zzgm);
        this.zzacw.zzb(this);
    }

    final boolean isInitialized() {
        return this.zzvo;
    }

    protected final void zzch() {
        if (!isInitialized()) {
            throw new IllegalStateException("Not initialized");
        }
    }

    protected abstract boolean zzhh();

    protected void zzin() {
    }

    public final void zzke() {
        if (this.zzvo) {
            throw new IllegalStateException("Can't initialize twice");
        }
        zzin();
        this.zzacw.zzkc();
        this.zzvo = true;
    }

    public final void zzm() {
        if (this.zzvo) {
            throw new IllegalStateException("Can't initialize twice");
        } else if (!zzhh()) {
            this.zzacw.zzkc();
            this.zzvo = true;
        }
    }
}
