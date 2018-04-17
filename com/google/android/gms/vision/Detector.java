package com.google.android.gms.vision;

import javax.annotation.concurrent.GuardedBy;

public abstract class Detector<T> {
    private final Object zzac = new Object();
    @GuardedBy("mProcessorLock")
    private Processor<T> zzad;

    public interface Processor<T> {
        void release();
    }

    public boolean isOperational() {
        return true;
    }

    public void release() {
        synchronized (this.zzac) {
            if (this.zzad != null) {
                this.zzad.release();
                this.zzad = null;
            }
        }
    }
}
