package com.google.firebase.iid;

import android.os.Bundle;

final class zzaa extends zzab<Void> {
    zzaa(int i, int i2, Bundle bundle) {
        super(i, 2, bundle);
    }

    final void zzb(Bundle bundle) {
        if (bundle.getBoolean("ack", false)) {
            finish(null);
        } else {
            zza(new zzac(4, "Invalid response to one way request"));
        }
    }

    final boolean zzw() {
        return true;
    }
}
