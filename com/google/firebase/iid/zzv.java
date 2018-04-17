package com.google.firebase.iid;

import android.os.Bundle;
import org.telegram.messenger.exoplayer2.upstream.DataSchemeDataSource;

final class zzv extends zzt<Bundle> {
    zzv(int i, int i2, Bundle bundle) {
        super(i, 1, bundle);
    }

    final void zzh(Bundle bundle) {
        Object bundle2 = bundle.getBundle(DataSchemeDataSource.SCHEME_DATA);
        if (bundle2 == null) {
            bundle2 = Bundle.EMPTY;
        }
        finish(bundle2);
    }

    final boolean zzst() {
        return false;
    }
}
