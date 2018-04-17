package com.google.android.gms.internal.measurement;

import com.google.android.gms.common.internal.Preconditions;
import com.google.android.gms.common.util.Clock;

final class zzjp {
    private long startTime;
    private final Clock zzrj;

    public zzjp(Clock clock) {
        Preconditions.checkNotNull(clock);
        this.zzrj = clock;
    }

    public final void clear() {
        this.startTime = 0;
    }

    public final void start() {
        this.startTime = this.zzrj.elapsedRealtime();
    }

    public final boolean zzj(long j) {
        return this.startTime == 0 || this.zzrj.elapsedRealtime() - this.startTime >= 3600000;
    }
}
