package com.google.android.gms.wearable;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.Objects;
import com.google.android.gms.common.internal.ReflectedParcelable;
import com.google.android.gms.common.internal.safeparcel.AbstractSafeParcelable;
import com.google.android.gms.common.internal.safeparcel.SafeParcelWriter;

public class ConnectionConfiguration extends AbstractSafeParcelable implements ReflectedParcelable {
    public static final Creator<ConnectionConfiguration> CREATOR = new zzg();
    private final String name;
    private final int type;
    private final String zzi;
    private final int zzj;
    private final boolean zzk;
    private volatile boolean zzl;
    private volatile String zzm;
    private boolean zzn;
    private String zzo;

    ConnectionConfiguration(String str, String str2, int i, int i2, boolean z, boolean z2, String str3, boolean z3, String str4) {
        this.name = str;
        this.zzi = str2;
        this.type = i;
        this.zzj = i2;
        this.zzk = z;
        this.zzl = z2;
        this.zzm = str3;
        this.zzn = z3;
        this.zzo = str4;
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof ConnectionConfiguration)) {
            return false;
        }
        ConnectionConfiguration connectionConfiguration = (ConnectionConfiguration) obj;
        return Objects.equal(this.name, connectionConfiguration.name) && Objects.equal(this.zzi, connectionConfiguration.zzi) && Objects.equal(Integer.valueOf(this.type), Integer.valueOf(connectionConfiguration.type)) && Objects.equal(Integer.valueOf(this.zzj), Integer.valueOf(connectionConfiguration.zzj)) && Objects.equal(Boolean.valueOf(this.zzk), Boolean.valueOf(connectionConfiguration.zzk)) && Objects.equal(Boolean.valueOf(this.zzn), Boolean.valueOf(connectionConfiguration.zzn));
    }

    public int hashCode() {
        return Objects.hashCode(this.name, this.zzi, Integer.valueOf(this.type), Integer.valueOf(this.zzj), Boolean.valueOf(this.zzk), Boolean.valueOf(this.zzn));
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder("ConnectionConfiguration[ ");
        String str = "mName=";
        String valueOf = String.valueOf(this.name);
        stringBuilder.append(valueOf.length() != 0 ? str.concat(valueOf) : new String(str));
        str = ", mAddress=";
        valueOf = String.valueOf(this.zzi);
        stringBuilder.append(valueOf.length() != 0 ? str.concat(valueOf) : new String(str));
        int i = this.type;
        StringBuilder stringBuilder2 = new StringBuilder(19);
        stringBuilder2.append(", mType=");
        stringBuilder2.append(i);
        stringBuilder.append(stringBuilder2.toString());
        i = this.zzj;
        stringBuilder2 = new StringBuilder(19);
        stringBuilder2.append(", mRole=");
        stringBuilder2.append(i);
        stringBuilder.append(stringBuilder2.toString());
        boolean z = this.zzk;
        StringBuilder stringBuilder3 = new StringBuilder(16);
        stringBuilder3.append(", mEnabled=");
        stringBuilder3.append(z);
        stringBuilder.append(stringBuilder3.toString());
        z = this.zzl;
        stringBuilder3 = new StringBuilder(20);
        stringBuilder3.append(", mIsConnected=");
        stringBuilder3.append(z);
        stringBuilder.append(stringBuilder3.toString());
        str = ", mPeerNodeId=";
        valueOf = String.valueOf(this.zzm);
        stringBuilder.append(valueOf.length() != 0 ? str.concat(valueOf) : new String(str));
        z = this.zzn;
        stringBuilder3 = new StringBuilder(21);
        stringBuilder3.append(", mBtlePriority=");
        stringBuilder3.append(z);
        stringBuilder.append(stringBuilder3.toString());
        str = ", mNodeId=";
        valueOf = String.valueOf(this.zzo);
        stringBuilder.append(valueOf.length() != 0 ? str.concat(valueOf) : new String(str));
        stringBuilder.append("]");
        return stringBuilder.toString();
    }

    public void writeToParcel(Parcel parcel, int i) {
        i = SafeParcelWriter.beginObjectHeader(parcel);
        SafeParcelWriter.writeString(parcel, 2, this.name, false);
        SafeParcelWriter.writeString(parcel, 3, this.zzi, false);
        SafeParcelWriter.writeInt(parcel, 4, this.type);
        SafeParcelWriter.writeInt(parcel, 5, this.zzj);
        SafeParcelWriter.writeBoolean(parcel, 6, this.zzk);
        SafeParcelWriter.writeBoolean(parcel, 7, this.zzl);
        SafeParcelWriter.writeString(parcel, 8, this.zzm, false);
        SafeParcelWriter.writeBoolean(parcel, 9, this.zzn);
        SafeParcelWriter.writeString(parcel, 10, this.zzo, false);
        SafeParcelWriter.finishObjectHeader(parcel, i);
    }
}
