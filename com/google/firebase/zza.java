package com.google.firebase;

import com.google.android.gms.common.api.internal.BackgroundDetector.BackgroundStateChangeListener;

final class zza implements BackgroundStateChangeListener {
    zza() {
    }

    public final void onBackgroundStateChanged(boolean z) {
        FirebaseApp.onBackgroundStateChanged(z);
    }
}
