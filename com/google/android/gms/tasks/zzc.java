package com.google.android.gms.tasks;

import java.util.concurrent.Executor;

final class zzc<TResult, TContinuationResult> implements zzq<TResult> {
    private final Executor zzafk;
    private final Continuation<TResult, TContinuationResult> zzafl;
    private final zzu<TContinuationResult> zzafm;

    public zzc(Executor executor, Continuation<TResult, TContinuationResult> continuation, zzu<TContinuationResult> com_google_android_gms_tasks_zzu_TContinuationResult) {
        this.zzafk = executor;
        this.zzafl = continuation;
        this.zzafm = com_google_android_gms_tasks_zzu_TContinuationResult;
    }

    public final void onComplete(Task<TResult> task) {
        this.zzafk.execute(new zzd(this, task));
    }
}
