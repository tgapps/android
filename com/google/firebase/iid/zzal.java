package com.google.firebase.iid;

import android.os.Bundle;
import com.google.android.exoplayer2.upstream.DataSchemeDataSource;

final class zzal extends zzaj<Bundle> {
    zzal(int i, int i2, Bundle bundle) {
        super(i, 1, bundle);
    }

    final boolean zzaa() {
        return false;
    }

    final void zzb(Bundle bundle) {
        Object bundle2 = bundle.getBundle(DataSchemeDataSource.SCHEME_DATA);
        if (bundle2 == null) {
            bundle2 = Bundle.EMPTY;
        }
        finish(bundle2);
    }
}
