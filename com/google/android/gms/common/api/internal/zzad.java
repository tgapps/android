package com.google.android.gms.common.api.internal;

import android.support.v4.util.ArraySet;
import com.google.android.gms.common.ConnectionResult;

public class zzad extends zzk {
    private GoogleApiManager zzcq;
    private final ArraySet<zzh<?>> zzhb;

    private final void zzan() {
        if (!this.zzhb.isEmpty()) {
            this.zzcq.zza(this);
        }
    }

    public final void onResume() {
        super.onResume();
        zzan();
    }

    public final void onStart() {
        super.onStart();
        zzan();
    }

    public void onStop() {
        super.onStop();
        this.zzcq.zzb(this);
    }

    protected final void zza(ConnectionResult connectionResult, int i) {
        this.zzcq.zza(connectionResult, i);
    }

    final ArraySet<zzh<?>> zzam() {
        return this.zzhb;
    }

    protected final void zzr() {
        this.zzcq.zzr();
    }
}
