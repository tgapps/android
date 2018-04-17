package com.google.android.gms.wearable;

import android.content.Context;
import com.google.android.gms.common.api.GoogleApi;
import com.google.android.gms.common.api.GoogleApi.Settings;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.wearable.Wearable.WearableOptions;

public abstract class MessageClient extends GoogleApi<WearableOptions> {
    public MessageClient(Context context, Settings settings) {
        super(context, Wearable.API, null, settings);
    }

    public abstract Task<Integer> sendMessage(String str, String str2, byte[] bArr);
}
