package com.google.android.gms.internal.measurement;

import android.database.ContentObserver;
import android.os.Handler;

final class zzsh extends ContentObserver {
    zzsh(Handler handler) {
        super(null);
    }

    public final void onChange(boolean z) {
        zzsg.zzbqg.set(true);
    }
}
