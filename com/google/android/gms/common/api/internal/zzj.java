package com.google.android.gms.common.api.internal;

import android.support.v4.util.ArrayMap;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.AvailabilityException;
import com.google.android.gms.common.api.GoogleApi;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskCompletionSource;
import java.util.Map;
import java.util.Set;

public final class zzj {
    private final ArrayMap<zzh<?>, ConnectionResult> zzcc = new ArrayMap();
    private final ArrayMap<zzh<?>, String> zzei = new ArrayMap();
    private final TaskCompletionSource<Map<zzh<?>, String>> zzej = new TaskCompletionSource();
    private int zzek;
    private boolean zzel = false;

    public zzj(Iterable<? extends GoogleApi<?>> iterable) {
        for (GoogleApi zzm : iterable) {
            this.zzcc.put(zzm.zzm(), null);
        }
        this.zzek = this.zzcc.keySet().size();
    }

    public final Task<Map<zzh<?>, String>> getTask() {
        return this.zzej.getTask();
    }

    public final void zza(zzh<?> com_google_android_gms_common_api_internal_zzh_, ConnectionResult connectionResult, String str) {
        this.zzcc.put(com_google_android_gms_common_api_internal_zzh_, connectionResult);
        this.zzei.put(com_google_android_gms_common_api_internal_zzh_, str);
        this.zzek--;
        if (!connectionResult.isSuccess()) {
            this.zzel = true;
        }
        if (this.zzek == 0) {
            if (this.zzel) {
                this.zzej.setException(new AvailabilityException(this.zzcc));
                return;
            }
            this.zzej.setResult(this.zzei);
        }
    }

    public final Set<zzh<?>> zzs() {
        return this.zzcc.keySet();
    }
}
