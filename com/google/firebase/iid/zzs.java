package com.google.firebase.iid;

import android.os.Bundle;

final class zzs extends zzt<Void> {
    zzs(int i, int i2, Bundle bundle) {
        super(i, 2, bundle);
    }

    final void zzh(Bundle bundle) {
        if (bundle.getBoolean("ack", false)) {
            finish(null);
        } else {
            zza(new zzu(4, "Invalid response to one way request"));
        }
    }

    final boolean zzst() {
        return true;
    }
}
