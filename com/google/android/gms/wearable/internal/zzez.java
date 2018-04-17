package com.google.android.gms.wearable.internal;

import android.content.Context;
import com.google.android.gms.common.api.GoogleApi.Settings;
import com.google.android.gms.common.internal.PendingResultUtil;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.wearable.MessageApi;
import com.google.android.gms.wearable.MessageClient;

public final class zzez extends MessageClient {
    private final MessageApi zzei = new zzeu();

    public zzez(Context context, Settings settings) {
        super(context, settings);
    }

    public final Task<Integer> sendMessage(String str, String str2, byte[] bArr) {
        return PendingResultUtil.toTask(this.zzei.sendMessage(asGoogleApiClient(), str, str2, bArr), zzfa.zzbx);
    }
}
