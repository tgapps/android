package com.google.android.gms.measurement.internal;

import android.os.Process;
import com.google.android.gms.common.internal.Preconditions;
import java.util.concurrent.BlockingQueue;

final class zzbs extends Thread {
    private final /* synthetic */ zzbo zzapg;
    private final Object zzapj = new Object();
    private final BlockingQueue<zzbr<?>> zzapk;

    public zzbs(zzbo com_google_android_gms_measurement_internal_zzbo, String str, BlockingQueue<zzbr<?>> blockingQueue) {
        this.zzapg = com_google_android_gms_measurement_internal_zzbo;
        Preconditions.checkNotNull(str);
        Preconditions.checkNotNull(blockingQueue);
        this.zzapk = blockingQueue;
        setName(str);
    }

    public final void run() {
        Object obj = null;
        while (obj == null) {
            try {
                this.zzapg.zzapc.acquire();
                obj = 1;
            } catch (InterruptedException e) {
                zza(e);
            }
        }
        int threadPriority = Process.getThreadPriority(Process.myTid());
        while (true) {
            zzbr com_google_android_gms_measurement_internal_zzbr = (zzbr) this.zzapk.poll();
            if (com_google_android_gms_measurement_internal_zzbr != null) {
                Process.setThreadPriority(com_google_android_gms_measurement_internal_zzbr.zzapi ? threadPriority : 10);
                com_google_android_gms_measurement_internal_zzbr.run();
            } else {
                try {
                    synchronized (this.zzapj) {
                        if (this.zzapk.peek() == null && !this.zzapg.zzapd) {
                            try {
                                this.zzapj.wait(30000);
                            } catch (InterruptedException e2) {
                                zza(e2);
                            }
                        }
                    }
                    synchronized (this.zzapg.zzapb) {
                        if (this.zzapk.peek() == null) {
                            break;
                        }
                    }
                } catch (Throwable th) {
                    synchronized (this.zzapg.zzapb) {
                        this.zzapg.zzapc.release();
                        this.zzapg.zzapb.notifyAll();
                        if (this == this.zzapg.zzaov) {
                            this.zzapg.zzaov = null;
                        } else if (this == this.zzapg.zzaow) {
                            this.zzapg.zzaow = null;
                        } else {
                            this.zzapg.zzgo().zzjd().zzbx("Current scheduler thread is neither worker nor network");
                        }
                    }
                }
            }
        }
        synchronized (this.zzapg.zzapb) {
            this.zzapg.zzapc.release();
            this.zzapg.zzapb.notifyAll();
            if (this == this.zzapg.zzaov) {
                this.zzapg.zzaov = null;
            } else if (this == this.zzapg.zzaow) {
                this.zzapg.zzaow = null;
            } else {
                this.zzapg.zzgo().zzjd().zzbx("Current scheduler thread is neither worker nor network");
            }
        }
    }

    public final void zzke() {
        synchronized (this.zzapj) {
            this.zzapj.notifyAll();
        }
    }

    private final void zza(InterruptedException interruptedException) {
        this.zzapg.zzgo().zzjg().zzg(String.valueOf(getName()).concat(" was interrupted"), interruptedException);
    }
}
