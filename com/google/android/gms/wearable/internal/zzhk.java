package com.google.android.gms.wearable.internal;

import android.content.IntentFilter;
import com.google.android.gms.common.api.internal.ListenerHolder;
import com.google.android.gms.common.data.DataHolder;
import com.google.android.gms.wearable.CapabilityApi.CapabilityListener;
import com.google.android.gms.wearable.ChannelApi.ChannelListener;
import com.google.android.gms.wearable.DataApi.DataListener;
import com.google.android.gms.wearable.MessageApi.MessageListener;
import java.util.List;
import javax.annotation.Nullable;

public final class zzhk<T> extends zzen {
    private final IntentFilter[] zzba;
    @Nullable
    private final String zzbb;
    private ListenerHolder<DataListener> zzfl;
    private ListenerHolder<MessageListener> zzfm;
    private ListenerHolder<ChannelListener> zzfp;
    private ListenerHolder<CapabilityListener> zzfq;

    public final void onConnectedNodes(List<zzfo> list) {
    }

    public final void zza(DataHolder dataHolder) {
        if (this.zzfl != null) {
            this.zzfl.notifyListener(new zzhl(dataHolder));
        } else {
            dataHolder.close();
        }
    }

    public final void zza(zzah com_google_android_gms_wearable_internal_zzah) {
        if (this.zzfq != null) {
            this.zzfq.notifyListener(new zzho(com_google_android_gms_wearable_internal_zzah));
        }
    }

    public final void zza(zzaw com_google_android_gms_wearable_internal_zzaw) {
        if (this.zzfp != null) {
            this.zzfp.notifyListener(new zzhn(com_google_android_gms_wearable_internal_zzaw));
        }
    }

    public final void zza(zzfe com_google_android_gms_wearable_internal_zzfe) {
        if (this.zzfm != null) {
            this.zzfm.notifyListener(new zzhm(com_google_android_gms_wearable_internal_zzfe));
        }
    }

    public final void zza(zzfo com_google_android_gms_wearable_internal_zzfo) {
    }

    public final void zza(zzi com_google_android_gms_wearable_internal_zzi) {
    }

    public final void zza(zzl com_google_android_gms_wearable_internal_zzl) {
    }

    public final void zzb(zzfo com_google_android_gms_wearable_internal_zzfo) {
    }

    public final IntentFilter[] zze() {
        return this.zzba;
    }

    @Nullable
    public final String zzf() {
        return this.zzbb;
    }
}
