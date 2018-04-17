package com.google.android.gms.wearable;

import android.content.Context;
import android.os.Looper;
import com.google.android.gms.common.api.Api;
import com.google.android.gms.common.api.Api.AbstractClientBuilder;
import com.google.android.gms.common.api.Api.ApiOptions.Optional;
import com.google.android.gms.common.api.Api.ClientKey;
import com.google.android.gms.common.api.GoogleApi.Settings;
import com.google.android.gms.wearable.internal.zzaa;
import com.google.android.gms.wearable.internal.zzaj;
import com.google.android.gms.wearable.internal.zzbv;
import com.google.android.gms.wearable.internal.zzbw;
import com.google.android.gms.wearable.internal.zzeu;
import com.google.android.gms.wearable.internal.zzez;
import com.google.android.gms.wearable.internal.zzfg;
import com.google.android.gms.wearable.internal.zzgi;
import com.google.android.gms.wearable.internal.zzh;
import com.google.android.gms.wearable.internal.zzhg;
import com.google.android.gms.wearable.internal.zzhq;
import com.google.android.gms.wearable.internal.zzk;
import com.google.android.gms.wearable.internal.zzo;

public class Wearable {
    @Deprecated
    public static final Api<WearableOptions> API = new Api("Wearable.API", CLIENT_BUILDER, CLIENT_KEY);
    private static final AbstractClientBuilder<zzhg, WearableOptions> CLIENT_BUILDER = new zzj();
    private static final ClientKey<zzhg> CLIENT_KEY = new ClientKey();
    @Deprecated
    public static final CapabilityApi CapabilityApi = new zzo();
    @Deprecated
    public static final ChannelApi ChannelApi = new zzaj();
    @Deprecated
    public static final DataApi DataApi = new zzbw();
    @Deprecated
    public static final MessageApi MessageApi = new zzeu();
    @Deprecated
    public static final NodeApi NodeApi = new zzfg();
    @Deprecated
    private static final zzi zzaa = new zzgi();
    @Deprecated
    private static final zzu zzab = new zzhq();
    @Deprecated
    private static final zzc zzx = new zzk();
    @Deprecated
    private static final zza zzy = new zzh();
    @Deprecated
    private static final zzf zzz = new zzbv();

    public static final class WearableOptions implements Optional {
        private final Looper zzac;

        public static class Builder {
            private Looper zzac;
        }

        private WearableOptions(Builder builder) {
            this.zzac = builder.zzac;
        }
    }

    public static CapabilityClient getCapabilityClient(Context context) {
        return new zzaa(context, Settings.DEFAULT_SETTINGS);
    }

    public static MessageClient getMessageClient(Context context) {
        return new zzez(context, Settings.DEFAULT_SETTINGS);
    }
}
