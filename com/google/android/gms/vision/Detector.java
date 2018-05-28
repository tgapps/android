package com.google.android.gms.vision;

import javax.annotation.concurrent.GuardedBy;

public abstract class Detector<T> {
    private final Object zzad = new Object();
    @GuardedBy("processorLock")
    private Processor<T> zzae;

    public interface Processor<T> {
        void release();
    }

    public boolean isOperational() {
        return true;
    }

    public void release() {
        synchronized (this.zzad) {
            if (this.zzae != null) {
                this.zzae.release();
                this.zzae = null;
            }
        }
    }
}
