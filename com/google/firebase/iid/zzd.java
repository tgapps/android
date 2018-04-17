package com.google.firebase.iid;

import android.content.BroadcastReceiver.PendingResult;
import android.content.Intent;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

final class zzd {
    final Intent intent;
    private final PendingResult zzbpu;
    private boolean zzbpv = false;
    private final ScheduledFuture<?> zzbpw;

    zzd(Intent intent, PendingResult pendingResult, ScheduledExecutorService scheduledExecutorService) {
        this.intent = intent;
        this.zzbpu = pendingResult;
        this.zzbpw = scheduledExecutorService.schedule(new zze(this, intent), 9500, TimeUnit.MILLISECONDS);
    }

    final synchronized void finish() {
        if (!this.zzbpv) {
            this.zzbpu.finish();
            this.zzbpw.cancel(false);
            this.zzbpv = true;
        }
    }
}
