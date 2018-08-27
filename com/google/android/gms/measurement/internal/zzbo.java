package com.google.android.gms.measurement.internal;

import android.content.Context;
import com.google.android.gms.common.internal.Preconditions;
import com.google.android.gms.common.util.Clock;
import java.lang.Thread.UncaughtExceptionHandler;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicLong;

public final class zzbo extends zzcp {
    private static final AtomicLong zzape = new AtomicLong(Long.MIN_VALUE);
    private zzbs zzaov;
    private zzbs zzaow;
    private final PriorityBlockingQueue<zzbr<?>> zzaox = new PriorityBlockingQueue();
    private final BlockingQueue<zzbr<?>> zzaoy = new LinkedBlockingQueue();
    private final UncaughtExceptionHandler zzaoz = new zzbq(this, "Thread death: Uncaught exception on worker thread");
    private final UncaughtExceptionHandler zzapa = new zzbq(this, "Thread death: Uncaught exception on network thread");
    private final Object zzapb = new Object();
    private final Semaphore zzapc = new Semaphore(2);
    private volatile boolean zzapd;

    zzbo(zzbt com_google_android_gms_measurement_internal_zzbt) {
        super(com_google_android_gms_measurement_internal_zzbt);
    }

    protected final boolean zzgt() {
        return false;
    }

    public final void zzaf() {
        if (Thread.currentThread() != this.zzaov) {
            throw new IllegalStateException("Call expected from worker thread");
        }
    }

    public final void zzgc() {
        if (Thread.currentThread() != this.zzaow) {
            throw new IllegalStateException("Call expected from network thread");
        }
    }

    public final boolean zzkb() {
        return Thread.currentThread() == this.zzaov;
    }

    public final <V> Future<V> zzb(Callable<V> callable) throws IllegalStateException {
        zzcl();
        Preconditions.checkNotNull(callable);
        zzbr com_google_android_gms_measurement_internal_zzbr = new zzbr(this, (Callable) callable, false, "Task exception on worker thread");
        if (Thread.currentThread() == this.zzaov) {
            if (!this.zzaox.isEmpty()) {
                zzgo().zzjg().zzbx("Callable skipped the worker queue.");
            }
            com_google_android_gms_measurement_internal_zzbr.run();
        } else {
            zza(com_google_android_gms_measurement_internal_zzbr);
        }
        return com_google_android_gms_measurement_internal_zzbr;
    }

    public final <V> Future<V> zzc(Callable<V> callable) throws IllegalStateException {
        zzcl();
        Preconditions.checkNotNull(callable);
        zzbr com_google_android_gms_measurement_internal_zzbr = new zzbr(this, (Callable) callable, true, "Task exception on worker thread");
        if (Thread.currentThread() == this.zzaov) {
            com_google_android_gms_measurement_internal_zzbr.run();
        } else {
            zza(com_google_android_gms_measurement_internal_zzbr);
        }
        return com_google_android_gms_measurement_internal_zzbr;
    }

    public final void zzc(Runnable runnable) throws IllegalStateException {
        zzcl();
        Preconditions.checkNotNull(runnable);
        zza(new zzbr(this, runnable, false, "Task exception on worker thread"));
    }

    private final void zza(zzbr<?> com_google_android_gms_measurement_internal_zzbr_) {
        synchronized (this.zzapb) {
            this.zzaox.add(com_google_android_gms_measurement_internal_zzbr_);
            if (this.zzaov == null) {
                this.zzaov = new zzbs(this, "Measurement Worker", this.zzaox);
                this.zzaov.setUncaughtExceptionHandler(this.zzaoz);
                this.zzaov.start();
            } else {
                this.zzaov.zzke();
            }
        }
    }

    public final void zzd(Runnable runnable) throws IllegalStateException {
        zzcl();
        Preconditions.checkNotNull(runnable);
        zzbr com_google_android_gms_measurement_internal_zzbr = new zzbr(this, runnable, false, "Task exception on network thread");
        synchronized (this.zzapb) {
            this.zzaoy.add(com_google_android_gms_measurement_internal_zzbr);
            if (this.zzaow == null) {
                this.zzaow = new zzbs(this, "Measurement Network", this.zzaoy);
                this.zzaow.setUncaughtExceptionHandler(this.zzapa);
                this.zzaow.start();
            } else {
                this.zzaow.zzke();
            }
        }
    }

    public final /* bridge */ /* synthetic */ void zzga() {
        super.zzga();
    }

    public final /* bridge */ /* synthetic */ void zzgb() {
        super.zzgb();
    }

    public final /* bridge */ /* synthetic */ zzx zzgk() {
        return super.zzgk();
    }

    public final /* bridge */ /* synthetic */ Clock zzbx() {
        return super.zzbx();
    }

    public final /* bridge */ /* synthetic */ Context getContext() {
        return super.getContext();
    }

    public final /* bridge */ /* synthetic */ zzan zzgl() {
        return super.zzgl();
    }

    public final /* bridge */ /* synthetic */ zzfk zzgm() {
        return super.zzgm();
    }

    public final /* bridge */ /* synthetic */ zzbo zzgn() {
        return super.zzgn();
    }

    public final /* bridge */ /* synthetic */ zzap zzgo() {
        return super.zzgo();
    }

    public final /* bridge */ /* synthetic */ zzba zzgp() {
        return super.zzgp();
    }

    public final /* bridge */ /* synthetic */ zzn zzgq() {
        return super.zzgq();
    }

    public final /* bridge */ /* synthetic */ zzk zzgr() {
        return super.zzgr();
    }
}
