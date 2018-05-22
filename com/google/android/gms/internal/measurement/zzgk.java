package com.google.android.gms.internal.measurement;

import android.os.Process;
import com.google.android.gms.common.internal.Preconditions;
import java.util.concurrent.BlockingQueue;

final class zzgk extends Thread {
    private final /* synthetic */ zzgg zzalz;
    private final Object zzamc = new Object();
    private final BlockingQueue<zzgj<?>> zzamd;

    public zzgk(zzgg com_google_android_gms_internal_measurement_zzgg, String str, BlockingQueue<zzgj<?>> blockingQueue) {
        this.zzalz = com_google_android_gms_internal_measurement_zzgg;
        Preconditions.checkNotNull(str);
        Preconditions.checkNotNull(blockingQueue);
        this.zzamd = blockingQueue;
        setName(str);
    }

    private final void zza(InterruptedException interruptedException) {
        this.zzalz.zzge().zzip().zzg(String.valueOf(getName()).concat(" was interrupted"), interruptedException);
    }

    public final void run() {
        Object obj = null;
        while (obj == null) {
            try {
                this.zzalz.zzalv.acquire();
                obj = 1;
            } catch (InterruptedException e) {
                zza(e);
            }
        }
        int threadPriority = Process.getThreadPriority(Process.myTid());
        while (true) {
            zzgj com_google_android_gms_internal_measurement_zzgj = (zzgj) this.zzamd.poll();
            if (com_google_android_gms_internal_measurement_zzgj != null) {
                Process.setThreadPriority(com_google_android_gms_internal_measurement_zzgj.zzamb ? threadPriority : 10);
                com_google_android_gms_internal_measurement_zzgj.run();
            } else {
                try {
                    synchronized (this.zzamc) {
                        if (this.zzamd.peek() == null && !this.zzalz.zzalw) {
                            try {
                                this.zzamc.wait(30000);
                            } catch (InterruptedException e2) {
                                zza(e2);
                            }
                        }
                    }
                    synchronized (this.zzalz.zzalu) {
                        if (this.zzamd.peek() == null) {
                            break;
                        }
                    }
                } catch (Throwable th) {
                    synchronized (this.zzalz.zzalu) {
                        this.zzalz.zzalv.release();
                        this.zzalz.zzalu.notifyAll();
                        if (this == this.zzalz.zzalo) {
                            this.zzalz.zzalo = null;
                        } else if (this == this.zzalz.zzalp) {
                            this.zzalz.zzalp = null;
                        } else {
                            this.zzalz.zzge().zzim().log("Current scheduler thread is neither worker nor network");
                        }
                    }
                }
            }
        }
        synchronized (this.zzalz.zzalu) {
            this.zzalz.zzalv.release();
            this.zzalz.zzalu.notifyAll();
            if (this == this.zzalz.zzalo) {
                this.zzalz.zzalo = null;
            } else if (this == this.zzalz.zzalp) {
                this.zzalz.zzalp = null;
            } else {
                this.zzalz.zzge().zzim().log("Current scheduler thread is neither worker nor network");
            }
        }
    }

    public final void zzjn() {
        synchronized (this.zzamc) {
            this.zzamc.notifyAll();
        }
    }
}
