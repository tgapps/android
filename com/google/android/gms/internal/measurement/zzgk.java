package com.google.android.gms.internal.measurement;

import android.os.Process;
import com.google.android.gms.common.internal.Preconditions;
import java.util.concurrent.BlockingQueue;

final class zzgk extends Thread {
    private final /* synthetic */ zzgg zzalx;
    private final Object zzama = new Object();
    private final BlockingQueue<zzgj<?>> zzamb;

    public zzgk(zzgg com_google_android_gms_internal_measurement_zzgg, String str, BlockingQueue<zzgj<?>> blockingQueue) {
        this.zzalx = com_google_android_gms_internal_measurement_zzgg;
        Preconditions.checkNotNull(str);
        Preconditions.checkNotNull(blockingQueue);
        this.zzamb = blockingQueue;
        setName(str);
    }

    private final void zza(InterruptedException interruptedException) {
        this.zzalx.zzgg().zzin().zzg(String.valueOf(getName()).concat(" was interrupted"), interruptedException);
    }

    public final void run() {
        Object obj = null;
        while (obj == null) {
            try {
                this.zzalx.zzalt.acquire();
                obj = 1;
            } catch (InterruptedException e) {
                zza(e);
            }
        }
        int threadPriority = Process.getThreadPriority(Process.myTid());
        while (true) {
            zzgj com_google_android_gms_internal_measurement_zzgj = (zzgj) this.zzamb.poll();
            if (com_google_android_gms_internal_measurement_zzgj != null) {
                Process.setThreadPriority(com_google_android_gms_internal_measurement_zzgj.zzalz ? threadPriority : 10);
                com_google_android_gms_internal_measurement_zzgj.run();
            } else {
                try {
                    synchronized (this.zzama) {
                        if (this.zzamb.peek() == null && !this.zzalx.zzalu) {
                            try {
                                this.zzama.wait(30000);
                            } catch (InterruptedException e2) {
                                zza(e2);
                            }
                        }
                    }
                    synchronized (this.zzalx.zzals) {
                        if (this.zzamb.peek() == null) {
                            break;
                        }
                    }
                } catch (Throwable th) {
                    synchronized (this.zzalx.zzals) {
                        this.zzalx.zzalt.release();
                        this.zzalx.zzals.notifyAll();
                        if (this == this.zzalx.zzalm) {
                            this.zzalx.zzalm = null;
                        } else if (this == this.zzalx.zzaln) {
                            this.zzalx.zzaln = null;
                        } else {
                            this.zzalx.zzgg().zzil().log("Current scheduler thread is neither worker nor network");
                        }
                    }
                }
            }
        }
        synchronized (this.zzalx.zzals) {
            this.zzalx.zzalt.release();
            this.zzalx.zzals.notifyAll();
            if (this == this.zzalx.zzalm) {
                this.zzalx.zzalm = null;
            } else if (this == this.zzalx.zzaln) {
                this.zzalx.zzaln = null;
            } else {
                this.zzalx.zzgg().zzil().log("Current scheduler thread is neither worker nor network");
            }
        }
    }

    public final void zzjj() {
        synchronized (this.zzama) {
            this.zzama.notifyAll();
        }
    }
}
