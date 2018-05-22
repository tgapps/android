package com.google.android.gms.internal.measurement;

import android.content.Context;
import android.os.Looper;
import com.google.android.gms.common.internal.Preconditions;
import com.google.android.gms.common.util.Clock;
import java.lang.Thread.UncaughtExceptionHandler;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.Semaphore;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;

public final class zzgg extends zzhh {
    private static final AtomicLong zzalx = new AtomicLong(Long.MIN_VALUE);
    private ExecutorService zzaln;
    private zzgk zzalo;
    private zzgk zzalp;
    private final PriorityBlockingQueue<zzgj<?>> zzalq = new PriorityBlockingQueue();
    private final BlockingQueue<zzgj<?>> zzalr = new LinkedBlockingQueue();
    private final UncaughtExceptionHandler zzals = new zzgi(this, "Thread death: Uncaught exception on worker thread");
    private final UncaughtExceptionHandler zzalt = new zzgi(this, "Thread death: Uncaught exception on network thread");
    private final Object zzalu = new Object();
    private final Semaphore zzalv = new Semaphore(2);
    private volatile boolean zzalw;

    zzgg(zzgl com_google_android_gms_internal_measurement_zzgl) {
        super(com_google_android_gms_internal_measurement_zzgl);
    }

    public static boolean isMainThread() {
        return Looper.myLooper() == Looper.getMainLooper();
    }

    private final void zza(zzgj<?> com_google_android_gms_internal_measurement_zzgj_) {
        synchronized (this.zzalu) {
            this.zzalq.add(com_google_android_gms_internal_measurement_zzgj_);
            if (this.zzalo == null) {
                this.zzalo = new zzgk(this, "Measurement Worker", this.zzalq);
                this.zzalo.setUncaughtExceptionHandler(this.zzals);
                this.zzalo.start();
            } else {
                this.zzalo.zzjn();
            }
        }
    }

    public final /* bridge */ /* synthetic */ Context getContext() {
        return super.getContext();
    }

    final <T> T zza(AtomicReference<T> atomicReference, long j, String str, Runnable runnable) {
        synchronized (atomicReference) {
            zzgd().zzc(runnable);
            try {
                atomicReference.wait(15000);
            } catch (InterruptedException e) {
                zzfi zzip = zzge().zzip();
                String str2 = "Interrupted waiting for ";
                String valueOf = String.valueOf(str);
                zzip.log(valueOf.length() != 0 ? str2.concat(valueOf) : new String(str2));
                return null;
            }
        }
        T t = atomicReference.get();
        if (t == null) {
            zzfi zzip2 = zzge().zzip();
            String str3 = "Timed out waiting for ";
            valueOf = String.valueOf(str);
            zzip2.log(valueOf.length() != 0 ? str3.concat(valueOf) : new String(str3));
        }
        return t;
    }

    public final void zzab() {
        if (Thread.currentThread() != this.zzalo) {
            throw new IllegalStateException("Call expected from worker thread");
        }
    }

    public final <V> Future<V> zzb(Callable<V> callable) throws IllegalStateException {
        zzch();
        Preconditions.checkNotNull(callable);
        zzgj com_google_android_gms_internal_measurement_zzgj = new zzgj(this, (Callable) callable, false, "Task exception on worker thread");
        if (Thread.currentThread() == this.zzalo) {
            if (!this.zzalq.isEmpty()) {
                zzge().zzip().log("Callable skipped the worker queue.");
            }
            com_google_android_gms_internal_measurement_zzgj.run();
        } else {
            zza(com_google_android_gms_internal_measurement_zzgj);
        }
        return com_google_android_gms_internal_measurement_zzgj;
    }

    public final /* bridge */ /* synthetic */ Clock zzbt() {
        return super.zzbt();
    }

    public final <V> Future<V> zzc(Callable<V> callable) throws IllegalStateException {
        zzch();
        Preconditions.checkNotNull(callable);
        zzgj com_google_android_gms_internal_measurement_zzgj = new zzgj(this, (Callable) callable, true, "Task exception on worker thread");
        if (Thread.currentThread() == this.zzalo) {
            com_google_android_gms_internal_measurement_zzgj.run();
        } else {
            zza(com_google_android_gms_internal_measurement_zzgj);
        }
        return com_google_android_gms_internal_measurement_zzgj;
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
        synchronized (this.zzalu) {
            this.zzalr.add(com_google_android_gms_internal_measurement_zzgj);
            if (this.zzalp == null) {
                this.zzalp = new zzgk(this, "Measurement Network", this.zzalr);
                this.zzalp.setUncaughtExceptionHandler(this.zzalt);
                this.zzalp.start();
            } else {
                this.zzalp.zzjn();
            }
        }
    }

    public final /* bridge */ /* synthetic */ void zzfr() {
        super.zzfr();
    }

    public final void zzfs() {
        if (Thread.currentThread() != this.zzalp) {
            throw new IllegalStateException("Call expected from network thread");
        }
    }

    public final /* bridge */ /* synthetic */ zzdu zzft() {
        return super.zzft();
    }

    public final /* bridge */ /* synthetic */ zzhk zzfu() {
        return super.zzfu();
    }

    public final /* bridge */ /* synthetic */ zzfb zzfv() {
        return super.zzfv();
    }

    public final /* bridge */ /* synthetic */ zzeo zzfw() {
        return super.zzfw();
    }

    public final /* bridge */ /* synthetic */ zzii zzfx() {
        return super.zzfx();
    }

    public final /* bridge */ /* synthetic */ zzif zzfy() {
        return super.zzfy();
    }

    public final /* bridge */ /* synthetic */ zzfc zzfz() {
        return super.zzfz();
    }

    public final /* bridge */ /* synthetic */ zzfe zzga() {
        return super.zzga();
    }

    public final /* bridge */ /* synthetic */ zzka zzgb() {
        return super.zzgb();
    }

    public final /* bridge */ /* synthetic */ zzjh zzgc() {
        return super.zzgc();
    }

    public final /* bridge */ /* synthetic */ zzgg zzgd() {
        return super.zzgd();
    }

    public final /* bridge */ /* synthetic */ zzfg zzge() {
        return super.zzge();
    }

    public final /* bridge */ /* synthetic */ zzfr zzgf() {
        return super.zzgf();
    }

    public final /* bridge */ /* synthetic */ zzef zzgg() {
        return super.zzgg();
    }

    protected final boolean zzhf() {
        return false;
    }

    public final boolean zzjk() {
        return Thread.currentThread() == this.zzalo;
    }

    final ExecutorService zzjl() {
        ExecutorService executorService;
        synchronized (this.zzalu) {
            if (this.zzaln == null) {
                this.zzaln = new ThreadPoolExecutor(0, 1, 30, TimeUnit.SECONDS, new ArrayBlockingQueue(100));
            }
            executorService = this.zzaln;
        }
        return executorService;
    }
}
