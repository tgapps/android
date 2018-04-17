package com.google.android.gms.common.api.internal;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

final class zzbf extends Handler {
    private final /* synthetic */ zzbd zzjh;

    zzbf(zzbd com_google_android_gms_common_api_internal_zzbd, Looper looper) {
        this.zzjh = com_google_android_gms_common_api_internal_zzbd;
        super(looper);
    }

    public final void handleMessage(Message message) {
        switch (message.what) {
            case 1:
                ((zzbe) message.obj).zzc(this.zzjh);
                return;
            case 2:
                throw ((RuntimeException) message.obj);
            default:
                int i = message.what;
                StringBuilder stringBuilder = new StringBuilder(31);
                stringBuilder.append("Unknown message id: ");
                stringBuilder.append(i);
                Log.w("GACStateManager", stringBuilder.toString());
                return;
        }
    }
}
