package com.google.android.gms.internal.measurement;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import com.google.android.gms.common.internal.Preconditions;

class zzfp extends BroadcastReceiver {
    private static final String zzaar = zzfp.class.getName();
    private boolean zzaas;
    private boolean zzaat;
    private final zzgl zzacr;

    zzfp(zzgl com_google_android_gms_internal_measurement_zzgl) {
        Preconditions.checkNotNull(com_google_android_gms_internal_measurement_zzgl);
        this.zzacr = com_google_android_gms_internal_measurement_zzgl;
    }

    public void onReceive(Context context, Intent intent) {
        this.zzacr.zzch();
        String action = intent.getAction();
        this.zzacr.zzgg().zzir().zzg("NetworkBroadcastReceiver received action", action);
        if ("android.net.conn.CONNECTIVITY_CHANGE".equals(action)) {
            boolean zzew = this.zzacr.zzjq().zzew();
            if (this.zzaat != zzew) {
                this.zzaat = zzew;
                this.zzacr.zzgf().zzc(new zzfq(this, zzew));
            }
            return;
        }
        this.zzacr.zzgg().zzin().zzg("NetworkBroadcastReceiver received unknown action", action);
    }

    public final void unregister() {
        this.zzacr.zzch();
        this.zzacr.zzgf().zzab();
        this.zzacr.zzgf().zzab();
        if (this.zzaas) {
            this.zzacr.zzgg().zzir().log("Unregistering connectivity change receiver");
            this.zzaas = false;
            this.zzaat = false;
            try {
                this.zzacr.getContext().unregisterReceiver(this);
            } catch (IllegalArgumentException e) {
                this.zzacr.zzgg().zzil().zzg("Failed to unregister the network broadcast receiver", e);
            }
        }
    }

    public final void zzet() {
        this.zzacr.zzch();
        this.zzacr.zzgf().zzab();
        if (!this.zzaas) {
            this.zzacr.getContext().registerReceiver(this, new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE"));
            this.zzaat = this.zzacr.zzjq().zzew();
            this.zzacr.zzgg().zzir().zzg("Registering connectivity change receiver. Network connected", Boolean.valueOf(this.zzaat));
            this.zzaas = true;
        }
    }
}
