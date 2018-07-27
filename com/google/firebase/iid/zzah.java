package com.google.firebase.iid;

import android.os.Bundle;

final class zzah extends zzai<Void> {
    zzah(int i, int i2, Bundle bundle) {
        super(i, 2, bundle);
    }

    final boolean zzaa() {
        return true;
    }

    final void zzb(Bundle bundle) {
        if (bundle.getBoolean("ack", false)) {
            finish(null);
        } else {
            zza(new zzaj(4, "Invalid response to one way request"));
        }
    }
}
