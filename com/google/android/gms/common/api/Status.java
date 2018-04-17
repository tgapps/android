package com.google.android.gms.common.api;

import android.app.PendingIntent;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.Objects;
import com.google.android.gms.common.internal.ReflectedParcelable;
import com.google.android.gms.common.internal.safeparcel.AbstractSafeParcelable;
import com.google.android.gms.common.internal.safeparcel.SafeParcelWriter;

public final class Status extends AbstractSafeParcelable implements Result, ReflectedParcelable {
    public static final Creator<Status> CREATOR = new zze();
    public static final Status RESULT_CANCELED = new Status(16);
    public static final Status RESULT_DEAD_CLIENT = new Status(18);
    public static final Status RESULT_INTERNAL_ERROR = new Status(8);
    public static final Status RESULT_INTERRUPTED = new Status(14);
    public static final Status RESULT_SUCCESS = new Status(0);
    public static final Status RESULT_TIMEOUT = new Status(15);
    private static final Status zzdq = new Status(17);
    private final int zzal;
    private final int zzam;
    private final PendingIntent zzan;
    private final String zzao;

    public Status(int i) {
        this(i, null);
    }

    Status(int i, int i2, String str, PendingIntent pendingIntent) {
        this.zzal = i;
        this.zzam = i2;
        this.zzao = str;
        this.zzan = pendingIntent;
    }

    public Status(int i, String str) {
        this(1, i, str, null);
    }

    public Status(int i, String str, PendingIntent pendingIntent) {
        this(1, i, str, pendingIntent);
    }

    public final boolean equals(Object obj) {
        if (!(obj instanceof Status)) {
            return false;
        }
        Status status = (Status) obj;
        return this.zzal == status.zzal && this.zzam == status.zzam && Objects.equal(this.zzao, status.zzao) && Objects.equal(this.zzan, status.zzan);
    }

    public final Status getStatus() {
        return this;
    }

    public final int getStatusCode() {
        return this.zzam;
    }

    public final String getStatusMessage() {
        return this.zzao;
    }

    public final boolean hasResolution() {
        return this.zzan != null;
    }

    public final int hashCode() {
        return Objects.hashCode(Integer.valueOf(this.zzal), Integer.valueOf(this.zzam), this.zzao, this.zzan);
    }

    public final boolean isSuccess() {
        return this.zzam <= 0;
    }

    public final String toString() {
        return Objects.toStringHelper(this).add("statusCode", zzp()).add("resolution", this.zzan).toString();
    }

    public final void writeToParcel(Parcel parcel, int i) {
        int beginObjectHeader = SafeParcelWriter.beginObjectHeader(parcel);
        SafeParcelWriter.writeInt(parcel, 1, getStatusCode());
        SafeParcelWriter.writeString(parcel, 2, getStatusMessage(), false);
        SafeParcelWriter.writeParcelable(parcel, 3, this.zzan, i, false);
        SafeParcelWriter.writeInt(parcel, 1000, this.zzal);
        SafeParcelWriter.finishObjectHeader(parcel, beginObjectHeader);
    }

    public final String zzp() {
        return this.zzao != null ? this.zzao : CommonStatusCodes.getStatusCodeString(this.zzam);
    }
}
