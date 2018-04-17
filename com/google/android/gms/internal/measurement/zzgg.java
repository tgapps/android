package com.google.android.gms.internal.measurement;

import android.content.Context;
import android.os.Looper;
import com.google.android.gms.common.internal.Preconditions;
import com.google.android.gms.common.util.Clock;
import java.lang.Thread.UncaughtExceptionHandler;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.Semaphore;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

public final class zzgg extends zzhk {
    private static final AtomicLong zzalv = new AtomicLong(Long.MIN_VALUE);
    private ExecutorService zzall;
    private zzgk zzalm;
    private zzgk zzaln;
    private final PriorityBlockingQueue<zzgj<?>> zzalo = new PriorityBlockingQueue();
    private final BlockingQueue<zzgj<?>> zzalp = new LinkedBlockingQueue();
    private final UncaughtExceptionHandler zzalq = new zzgi(this, "Thread death: Uncaught exception on worker thread");
    private final UncaughtExceptionHandler zzalr = new zzgi(this, "Thread death: Uncaught exception on network thread");
    private final Object zzals = new Object();
    private final Semaphore zzalt = new Semaphore(2);
    private volatile boolean zzalu;

    zzgg(zzgl com_google_android_gms_internal_measurement_zzgl) {
        super(com_google_android_gms_internal_measurement_zzgl);
    }

    public static boolean isMainThread() {
        return Looper.myLooper() == Looper.getMainLooper();
    }

    private final void zza(zzgj<?> com_google_android_gms_internal_measurement_zzgj_) {
        synchronized (this.zzals) {
            this.zzalo.add(com_google_android_gms_internal_measurement_zzgj_);
            if (this.zzalm == null) {
                this.zzalm = new zzgk(this, "Measurement Worker", this.zzalo);
                this.zzalm.setUncaughtExceptionHandler(this.zzalq);
                this.zzalm.start();
            } else {
                this.zzalm.zzjj();
            }
        }
    }

    public final /* bridge */ /* synthetic */ Context getContext() {
        return super.getContext();
    }

    public final void zzab() {
        if (Thread.currentThread() != this.zzalm) {
            throw new IllegalStateException("Call expected from worker thread");
        }
    }

    public final /* bridge */ /* synthetic */ Clock zzbt() {
        return super.zzbt();
    }

    public final void zzc(Runnable runnable) throws IllegalStateException {
        zzch();
        Preconditions.checkNotNull(runnable);
        zza(new zzgj(this, runnable, false, "Task exception on worker thread"));
    }

    public final void zzd(Runnable runnable) throws IllegalStateException {
        zzch();
        Preconditions.checkNotNull(runnable);
        zzgj com_google_android_gms_internal_measurement_zzgj = new zzgj(this, runnable, false, "Task exception on network thread");
        synchronized (this.zzals) {
            this.zzalp.add(com_google_android_gms_internal_measurement_zzgj);
            if (this.zzaln == null) {
                this.zzaln = new zzgk(this, "Measurement Network", this.zzalp);
                this.zzaln.setUncaughtExceptionHandler(this.zzalr);
                this.zzaln.start();
            } else {
                this.zzaln.zzjj();
            }
        }
    }

    public final /* bridge */ /* synthetic */ void zzfq() {
        super.zzfq();
    }

    public final void zzfr() {
        if (Thread.currentThread() != this.zzaln) {
            throw new IllegalStateException("Call expected from network thread");
        }
    }

    public final /* bridge */ /* synthetic */ zzdx zzfs() {
        return super.zzfs();
    }

    public final /* bridge */ /* synthetic */ zzee zzft() {
        return super.zzft();
    }

    public final /* bridge */ /* synthetic */ zzhm zzfu() {
        return super.zzfu();
    }

    public final /* bridge */ /* synthetic */ zzfb zzfv() {
        return super.zzfv();
    }

    public final /* bridge */ /* synthetic */ zzeo zzfw() {
        return super.zzfw();
    }

    public final /* bridge */ /* synthetic */ zzil zzfx() {
        return super.zzfx();
    }

    public final /* bridge */ /* synthetic */ zzih zzfy() {
        return super.zzfy();
    }

    public final /* bridge */ /* synthetic */ zzfc zzfz() {
        return super.zzfz();
    }

    public final /* bridge */ /* synthetic */ zzei zzga() {
        return super.zzga();
    }

    public final /* bridge */ /* synthetic */ zzfe zzgb() {
        return super.zzgb();
    }

    public final /* bridge */ /* synthetic */ zzjv zzgc() {
        return super.zzgc();
    }

    public final /* bridge */ /* synthetic */ zzgf zzgd() {
        return super.zzgd();
    }

    public final /* bridge */ /* synthetic */ zzjk zzge() {
        return super.zzge();
    }

    public final /* bridge */ /* synthetic */ zzgg zzgf() {
        return super.zzgf();
    }

    public final /* bridge */ /* synthetic */ zzfg zzgg() {
        return super.zzgg();
    }

    public final /* bridge */ /* synthetic */ zzfr zzgh() {
        return super.zzgh();
    }

    public final /* bridge */ /* synthetic */ zzeh zzgi() {
        return super.zzgi();
    }

    protected final boolean zzhh() {
        return false;
    }

    public final boolean zzjg() {
        return Thread.currentThread() == this.zzalm;
    }

    final ExecutorService zzjh() {
        ExecutorService executorService;
        synchronized (this.zzals) {
            if (this.zzall == null) {
                this.zzall = new ThreadPoolExecutor(0, 1, 30, TimeUnit.SECONDS, new ArrayBlockingQueue(100));
            }
            executorService = this.zzall;
        }
        return executorService;
    }
}
