package com.google.android.gms.common.api.internal;

import android.os.RemoteException;
import com.google.android.gms.common.api.internal.GoogleApiManager.zza;
import com.google.android.gms.common.api.internal.ListenerHolder.ListenerKey;
import com.google.android.gms.tasks.TaskCompletionSource;

public final class zzg extends zzc<Boolean> {
    private final ListenerKey<?> zzea;

    public zzg(ListenerKey<?> listenerKey, TaskCompletionSource<Boolean> taskCompletionSource) {
        super(4, taskCompletionSource);
        this.zzea = listenerKey;
    }

    public final /* bridge */ /* synthetic */ void zza(zzaa com_google_android_gms_common_api_internal_zzaa, boolean z) {
    }

    public final void zzb(zza<?> com_google_android_gms_common_api_internal_GoogleApiManager_zza_) throws RemoteException {
        zzbv com_google_android_gms_common_api_internal_zzbv = (zzbv) com_google_android_gms_common_api_internal_GoogleApiManager_zza_.zzbn().remove(this.zzea);
        if (com_google_android_gms_common_api_internal_zzbv != null) {
            com_google_android_gms_common_api_internal_zzbv.zzlu.unregisterListener(com_google_android_gms_common_api_internal_GoogleApiManager_zza_.zzae(), this.zzdu);
            com_google_android_gms_common_api_internal_zzbv.zzlt.clearListener();
            return;
        }
        this.zzdu.trySetResult(Boolean.valueOf(false));
    }
}
