package com.google.android.gms.common.api;

import com.google.android.gms.common.internal.Preconditions;

public class BooleanResult implements Result {
    private final Status mStatus;
    private final boolean zzck;

    public BooleanResult(Status status, boolean z) {
        this.mStatus = (Status) Preconditions.checkNotNull(status, "Status must not be null");
        this.zzck = z;
    }

    public final boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof BooleanResult)) {
            return false;
        }
        BooleanResult booleanResult = (BooleanResult) obj;
        return this.mStatus.equals(booleanResult.mStatus) && this.zzck == booleanResult.zzck;
    }

    public Status getStatus() {
        return this.mStatus;
    }

    public boolean getValue() {
        return this.zzck;
    }

    public final int hashCode() {
        return (this.zzck ? 1 : 0) + ((this.mStatus.hashCode() + 527) * 31);
    }
}
