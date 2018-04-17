package com.google.android.gms.wearable.internal;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.AbstractSafeParcelable;
import com.google.android.gms.common.internal.safeparcel.SafeParcelWriter;

public final class zzi extends AbstractSafeParcelable {
    public static final Creator<zzi> CREATOR = new zzj();
    private final String value;
    private byte zzbd;
    private final byte zzbe;

    public zzi(byte b, byte b2, String str) {
        this.zzbd = b;
        this.zzbe = b2;
        this.value = str;
    }

    public final boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        zzi com_google_android_gms_wearable_internal_zzi = (zzi) obj;
        return this.zzbd == com_google_android_gms_wearable_internal_zzi.zzbd && this.zzbe == com_google_android_gms_wearable_internal_zzi.zzbe && this.value.equals(com_google_android_gms_wearable_internal_zzi.value);
    }

    public final int hashCode() {
        return ((((this.zzbd + 31) * 31) + this.zzbe) * 31) + this.value.hashCode();
    }

    public final String toString() {
        byte b = this.zzbd;
        byte b2 = this.zzbe;
        String str = this.value;
        StringBuilder stringBuilder = new StringBuilder(73 + String.valueOf(str).length());
        stringBuilder.append("AmsEntityUpdateParcelable{, mEntityId=");
        stringBuilder.append(b);
        stringBuilder.append(", mAttributeId=");
        stringBuilder.append(b2);
        stringBuilder.append(", mValue='");
        stringBuilder.append(str);
        stringBuilder.append('\'');
        stringBuilder.append('}');
        return stringBuilder.toString();
    }

    public final void writeToParcel(Parcel parcel, int i) {
        i = SafeParcelWriter.beginObjectHeader(parcel);
        SafeParcelWriter.writeByte(parcel, 2, this.zzbd);
        SafeParcelWriter.writeByte(parcel, 3, this.zzbe);
        SafeParcelWriter.writeString(parcel, 4, this.value, false);
        SafeParcelWriter.finishObjectHeader(parcel, i);
    }
}
