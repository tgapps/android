package com.google.firebase.remoteconfig;

import com.google.android.gms.common.api.Result;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.internal.config.zzk;
import com.google.android.gms.tasks.TaskCompletionSource;

final class zza implements ResultCallback<zzk> {
    private final /* synthetic */ TaskCompletionSource zzan;
    private final /* synthetic */ FirebaseRemoteConfig zzao;

    zza(FirebaseRemoteConfig firebaseRemoteConfig, TaskCompletionSource taskCompletionSource) {
        this.zzao = firebaseRemoteConfig;
        this.zzan = taskCompletionSource;
    }

    public final /* synthetic */ void onResult(Result result) {
        this.zzao.zza(this.zzan, (zzk) result);
    }
}
