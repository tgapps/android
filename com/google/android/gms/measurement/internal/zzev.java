package com.google.android.gms.measurement.internal;

import com.google.android.gms.common.internal.Preconditions;
import com.google.android.gms.common.util.Clock;

final class zzev {
    private long startTime;
    private final Clock zzrz;

    public zzev(Clock clock) {
        Preconditions.checkNotNull(clock);
        this.zzrz = clock;
    }

    public final void start() {
        this.startTime = this.zzrz.elapsedRealtime();
    }

    public final void clear() {
        this.startTime = 0;
    }

    public final boolean zzj(long j) {
        if (this.startTime != 0 && this.zzrz.elapsedRealtime() - this.startTime < 3600000) {
            return false;
        }
        return true;
    }
}
