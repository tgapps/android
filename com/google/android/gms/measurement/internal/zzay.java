package com.google.android.gms.measurement.internal;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import com.google.android.gms.common.internal.Preconditions;

class zzay extends BroadcastReceiver {
    private static final String zzabi = zzay.class.getName();
    private boolean zzabj;
    private boolean zzabk;
    private final zzfa zzamz;

    zzay(zzfa com_google_android_gms_measurement_internal_zzfa) {
        Preconditions.checkNotNull(com_google_android_gms_measurement_internal_zzfa);
        this.zzamz = com_google_android_gms_measurement_internal_zzfa;
    }

    public void onReceive(Context context, Intent intent) {
        this.zzamz.zzlr();
        String action = intent.getAction();
        this.zzamz.zzgo().zzjl().zzg("NetworkBroadcastReceiver received action", action);
        if ("android.net.conn.CONNECTIVITY_CHANGE".equals(action)) {
            boolean zzfb = this.zzamz.zzlo().zzfb();
            if (this.zzabk != zzfb) {
                this.zzabk = zzfb;
                this.zzamz.zzgn().zzc(new zzaz(this, zzfb));
                return;
            }
            return;
        }
        this.zzamz.zzgo().zzjg().zzg("NetworkBroadcastReceiver received unknown action", action);
    }

    public final void zzey() {
        this.zzamz.zzlr();
        this.zzamz.zzgn().zzaf();
        if (!this.zzabj) {
            this.zzamz.getContext().registerReceiver(this, new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE"));
            this.zzabk = this.zzamz.zzlo().zzfb();
            this.zzamz.zzgo().zzjl().zzg("Registering connectivity change receiver. Network connected", Boolean.valueOf(this.zzabk));
            this.zzabj = true;
        }
    }

    public final void unregister() {
        this.zzamz.zzlr();
        this.zzamz.zzgn().zzaf();
        this.zzamz.zzgn().zzaf();
        if (this.zzabj) {
            this.zzamz.zzgo().zzjl().zzbx("Unregistering connectivity change receiver");
            this.zzabj = false;
            this.zzabk = false;
            try {
                this.zzamz.getContext().unregisterReceiver(this);
            } catch (IllegalArgumentException e) {
                this.zzamz.zzgo().zzjd().zzg("Failed to unregister the network broadcast receiver", e);
            }
        }
    }
}
