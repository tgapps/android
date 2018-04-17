package com.google.android.gms.maps.model;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.Objects;
import com.google.android.gms.common.internal.Preconditions;
import com.google.android.gms.common.internal.safeparcel.AbstractSafeParcelable;
import com.google.android.gms.common.internal.safeparcel.SafeParcelWriter;

public class PatternItem extends AbstractSafeParcelable {
    public static final Creator<PatternItem> CREATOR = new zzi();
    private static final String TAG = PatternItem.class.getSimpleName();
    private final int type;
    private final Float zzdu;

    public PatternItem(int i, Float f) {
        boolean z = true;
        if (i != 1) {
            if (f == null || f.floatValue() < 0.0f) {
                z = false;
            }
        }
        String valueOf = String.valueOf(f);
        StringBuilder stringBuilder = new StringBuilder(45 + String.valueOf(valueOf).length());
        stringBuilder.append("Invalid PatternItem: type=");
        stringBuilder.append(i);
        stringBuilder.append(" length=");
        stringBuilder.append(valueOf);
        Preconditions.checkArgument(z, stringBuilder.toString());
        this.type = i;
        this.zzdu = f;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof PatternItem)) {
            return false;
        }
        PatternItem patternItem = (PatternItem) obj;
        return this.type == patternItem.type && Objects.equal(this.zzdu, patternItem.zzdu);
    }

    public int hashCode() {
        return Objects.hashCode(Integer.valueOf(this.type), this.zzdu);
    }

    public String toString() {
        int i = this.type;
        String valueOf = String.valueOf(this.zzdu);
        StringBuilder stringBuilder = new StringBuilder(39 + String.valueOf(valueOf).length());
        stringBuilder.append("[PatternItem: type=");
        stringBuilder.append(i);
        stringBuilder.append(" length=");
        stringBuilder.append(valueOf);
        stringBuilder.append("]");
        return stringBuilder.toString();
    }

    public void writeToParcel(Parcel parcel, int i) {
        i = SafeParcelWriter.beginObjectHeader(parcel);
        SafeParcelWriter.writeInt(parcel, 2, this.type);
        SafeParcelWriter.writeFloatObject(parcel, 3, this.zzdu, false);
        SafeParcelWriter.finishObjectHeader(parcel, i);
    }
}
