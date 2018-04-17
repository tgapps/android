package com.google.android.gms.wearable.internal;

import android.content.Context;
import com.google.android.gms.common.api.GoogleApi.Settings;
import com.google.android.gms.common.internal.Asserts;
import com.google.android.gms.common.internal.PendingResultUtil;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.wearable.CapabilityApi;
import com.google.android.gms.wearable.CapabilityClient;
import com.google.android.gms.wearable.CapabilityInfo;

public final class zzaa extends CapabilityClient {
    private final CapabilityApi zzbw = new zzo();

    public zzaa(Context context, Settings settings) {
        super(context, settings);
    }

    public final Task<CapabilityInfo> getCapability(String str, int i) {
        Asserts.checkNotNull(str, "capability must not be null");
        return PendingResultUtil.toTask(this.zzbw.getCapability(asGoogleApiClient(), str, i), zzab.zzbx);
    }
}
