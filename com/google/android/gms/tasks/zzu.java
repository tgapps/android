package com.google.android.gms.tasks;

import com.google.android.gms.common.internal.Preconditions;
import java.util.concurrent.CancellationException;
import java.util.concurrent.Executor;
import javax.annotation.concurrent.GuardedBy;

final class zzu<TResult> extends Task<TResult> {
    private final Object mLock = new Object();
    private final zzr<TResult> zzage = new zzr();
    @GuardedBy("mLock")
    private boolean zzagf;
    @GuardedBy("mLock")
    private TResult zzagg;
    @GuardedBy("mLock")
    private Exception zzagh;
    private volatile boolean zzfi;

    zzu() {
    }

    @GuardedBy("mLock")
    private final void zzdq() {
        Preconditions.checkState(this.zzagf, "Task is not yet complete");
    }

    @GuardedBy("mLock")
    private final void zzdr() {
        Preconditions.checkState(!this.zzagf, "Task is already complete");
    }

    @GuardedBy("mLock")
    private final void zzds() {
        if (this.zzfi) {
            throw new CancellationException("Task is already canceled.");
        }
    }

    private final void zzdt() {
        synchronized (this.mLock) {
            if (this.zzagf) {
                this.zzage.zza((Task) this);
                return;
            }
        }
    }

    public final Task<TResult> addOnCanceledListener(Executor executor, OnCanceledListener onCanceledListener) {
        this.zzage.zza(new zzg(executor, onCanceledListener));
        zzdt();
        return this;
    }

    public final Task<TResult> addOnCompleteListener(OnCompleteListener<TResult> onCompleteListener) {
        return addOnCompleteListener(TaskExecutors.MAIN_THREAD, onCompleteListener);
    }

    public final Task<TResult> addOnCompleteListener(Executor executor, OnCompleteListener<TResult> onCompleteListener) {
        this.zzage.zza(new zzi(executor, onCompleteListener));
        zzdt();
        return this;
    }

    public final Task<TResult> addOnFailureListener(Executor executor, OnFailureListener onFailureListener) {
        this.zzage.zza(new zzk(executor, onFailureListener));
        zzdt();
        return this;
    }

    public final Task<TResult> addOnSuccessListener(Executor executor, OnSuccessListener<? super TResult> onSuccessListener) {
        this.zzage.zza(new zzm(executor, onSuccessListener));
        zzdt();
        return this;
    }

    public final <TContinuationResult> Task<TContinuationResult> continueWith(Executor executor, Continuation<TResult, TContinuationResult> continuation) {
        Task com_google_android_gms_tasks_zzu = new zzu();
        this.zzage.zza(new zzc(executor, continuation, com_google_android_gms_tasks_zzu));
        zzdt();
        return com_google_android_gms_tasks_zzu;
    }

    public final Exception getException() {
        Exception exception;
        synchronized (this.mLock) {
            exception = this.zzagh;
        }
        return exception;
    }

    public final TResult getResult() {
        TResult tResult;
        synchronized (this.mLock) {
            zzdq();
            zzds();
            if (this.zzagh != null) {
                throw new RuntimeExecutionException(this.zzagh);
            }
            tResult = this.zzagg;
        }
        return tResult;
    }

    public final <X extends Throwable> TResult getResult(Class<X> cls) throws Throwable {
        TResult tResult;
        synchronized (this.mLock) {
            zzdq();
            zzds();
            if (cls.isInstance(this.zzagh)) {
                throw ((Throwable) cls.cast(this.zzagh));
            } else if (this.zzagh != null) {
                throw new RuntimeExecutionException(this.zzagh);
            } else {
                tResult = this.zzagg;
            }
        }
        return tResult;
    }

    public final boolean isCanceled() {
        return this.zzfi;
    }

    public final boolean isComplete() {
        boolean z;
        synchronized (this.mLock) {
            z = this.zzagf;
        }
        return z;
    }

    public final boolean isSuccessful() {
        boolean z;
        synchronized (this.mLock) {
            z = this.zzagf && !this.zzfi && this.zzagh == null;
        }
        return z;
    }

    public final void setException(Exception exception) {
        Preconditions.checkNotNull(exception, "Exception must not be null");
        synchronized (this.mLock) {
            zzdr();
            this.zzagf = true;
            this.zzagh = exception;
        }
        this.zzage.zza((Task) this);
    }

    public final void setResult(TResult tResult) {
        synchronized (this.mLock) {
            zzdr();
            this.zzagf = true;
            this.zzagg = tResult;
        }
        this.zzage.zza((Task) this);
    }

    public final boolean trySetException(Exception exception) {
        boolean z = true;
        Preconditions.checkNotNull(exception, "Exception must not be null");
        synchronized (this.mLock) {
            if (this.zzagf) {
                z = false;
            } else {
                this.zzagf = true;
                this.zzagh = exception;
                this.zzage.zza((Task) this);
            }
        }
        return z;
    }

    public final boolean trySetResult(TResult tResult) {
        boolean z = true;
        synchronized (this.mLock) {
            if (this.zzagf) {
                z = false;
            } else {
                this.zzagf = true;
                this.zzagg = tResult;
                this.zzage.zza((Task) this);
            }
        }
        return z;
    }

    public final boolean zzdp() {
        boolean z = true;
        synchronized (this.mLock) {
            if (this.zzagf) {
                z = false;
            } else {
                this.zzagf = true;
                this.zzfi = true;
                this.zzage.zza((Task) this);
            }
        }
        return z;
    }
}
