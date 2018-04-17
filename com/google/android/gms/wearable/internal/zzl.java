package com.google.android.gms.wearable.internal;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.AbstractSafeParcelable;
import com.google.android.gms.common.internal.safeparcel.SafeParcelWriter;
import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;

@Immutable
public final class zzl extends AbstractSafeParcelable {
    public static final Creator<zzl> CREATOR = new zzm();
    private int id;
    @Nullable
    private final String packageName;
    private final String zzbf;
    @Nullable
    private final String zzbg;
    private final String zzbh;
    private final String zzbi;
    private final String zzbj;
    @Nullable
    private final String zzbk;
    private final byte zzbl;
    private final byte zzbm;
    private final byte zzbn;
    private final byte zzbo;

    public zzl(int i, String str, @Nullable String str2, String str3, String str4, String str5, @Nullable String str6, byte b, byte b2, byte b3, byte b4, @Nullable String str7) {
        this.id = i;
        this.zzbf = str;
        this.zzbg = str2;
        this.zzbh = str3;
        this.zzbi = str4;
        this.zzbj = str5;
        this.zzbk = str6;
        this.zzbl = b;
        this.zzbm = b2;
        this.zzbn = b3;
        this.zzbo = b4;
        this.packageName = str7;
    }

    public final boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        zzl com_google_android_gms_wearable_internal_zzl = (zzl) obj;
        if (this.id != com_google_android_gms_wearable_internal_zzl.id || this.zzbl != com_google_android_gms_wearable_internal_zzl.zzbl || this.zzbm != com_google_android_gms_wearable_internal_zzl.zzbm || this.zzbn != com_google_android_gms_wearable_internal_zzl.zzbn || this.zzbo != com_google_android_gms_wearable_internal_zzl.zzbo || !this.zzbf.equals(com_google_android_gms_wearable_internal_zzl.zzbf)) {
            return false;
        }
        if (this.zzbg != null) {
            if (!this.zzbg.equals(com_google_android_gms_wearable_internal_zzl.zzbg)) {
                return false;
            }
        } else if (com_google_android_gms_wearable_internal_zzl.zzbg != null) {
            return false;
        }
        if (!this.zzbh.equals(com_google_android_gms_wearable_internal_zzl.zzbh) || !this.zzbi.equals(com_google_android_gms_wearable_internal_zzl.zzbi) || !this.zzbj.equals(com_google_android_gms_wearable_internal_zzl.zzbj)) {
            return false;
        }
        if (this.zzbk != null) {
            if (!this.zzbk.equals(com_google_android_gms_wearable_internal_zzl.zzbk)) {
                return false;
            }
        } else if (com_google_android_gms_wearable_internal_zzl.zzbk != null) {
            return false;
        }
        return this.packageName != null ? this.packageName.equals(com_google_android_gms_wearable_internal_zzl.packageName) : com_google_android_gms_wearable_internal_zzl.packageName == null;
    }

    public final int hashCode() {
        int i = 0;
        int hashCode = (((((((((((((((((((((this.id + 31) * 31) + this.zzbf.hashCode()) * 31) + (this.zzbg != null ? this.zzbg.hashCode() : 0)) * 31) + this.zzbh.hashCode()) * 31) + this.zzbi.hashCode()) * 31) + this.zzbj.hashCode()) * 31) + (this.zzbk != null ? this.zzbk.hashCode() : 0)) * 31) + this.zzbl) * 31) + this.zzbm) * 31) + this.zzbn) * 31) + this.zzbo) * 31;
        if (this.packageName != null) {
            i = this.packageName.hashCode();
        }
        return hashCode + i;
    }

    public final String toString() {
        int i = this.id;
        String str = this.zzbf;
        String str2 = this.zzbg;
        String str3 = this.zzbh;
        String str4 = this.zzbi;
        String str5 = this.zzbj;
        String str6 = this.zzbk;
        byte b = this.zzbl;
        byte b2 = this.zzbm;
        byte b3 = this.zzbn;
        byte b4 = this.zzbo;
        String str7 = this.packageName;
        StringBuilder stringBuilder = new StringBuilder(((((((211 + String.valueOf(str).length()) + String.valueOf(str2).length()) + String.valueOf(str3).length()) + String.valueOf(str4).length()) + String.valueOf(str5).length()) + String.valueOf(str6).length()) + String.valueOf(str7).length());
        stringBuilder.append("AncsNotificationParcelable{, id=");
        stringBuilder.append(i);
        stringBuilder.append(", appId='");
        stringBuilder.append(str);
        stringBuilder.append('\'');
        stringBuilder.append(", dateTime='");
        stringBuilder.append(str2);
        stringBuilder.append('\'');
        stringBuilder.append(", notificationText='");
        stringBuilder.append(str3);
        stringBuilder.append('\'');
        stringBuilder.append(", title='");
        stringBuilder.append(str4);
        stringBuilder.append('\'');
        stringBuilder.append(", subtitle='");
        stringBuilder.append(str5);
        stringBuilder.append('\'');
        stringBuilder.append(", displayName='");
        stringBuilder.append(str6);
        stringBuilder.append('\'');
        stringBuilder.append(", eventId=");
        stringBuilder.append(b);
        stringBuilder.append(", eventFlags=");
        stringBuilder.append(b2);
        stringBuilder.append(", categoryId=");
        stringBuilder.append(b3);
        stringBuilder.append(", categoryCount=");
        stringBuilder.append(b4);
        stringBuilder.append(", packageName='");
        stringBuilder.append(str7);
        stringBuilder.append('\'');
        stringBuilder.append('}');
        return stringBuilder.toString();
    }

    public final void writeToParcel(Parcel parcel, int i) {
        i = SafeParcelWriter.beginObjectHeader(parcel);
        SafeParcelWriter.writeInt(parcel, 2, this.id);
        SafeParcelWriter.writeString(parcel, 3, this.zzbf, false);
        SafeParcelWriter.writeString(parcel, 4, this.zzbg, false);
        SafeParcelWriter.writeString(parcel, 5, this.zzbh, false);
        SafeParcelWriter.writeString(parcel, 6, this.zzbi, false);
        SafeParcelWriter.writeString(parcel, 7, this.zzbj, false);
        SafeParcelWriter.writeString(parcel, 8, this.zzbk == null ? this.zzbf : this.zzbk, false);
        SafeParcelWriter.writeByte(parcel, 9, this.zzbl);
        SafeParcelWriter.writeByte(parcel, 10, this.zzbm);
        SafeParcelWriter.writeByte(parcel, 11, this.zzbn);
        SafeParcelWriter.writeByte(parcel, 12, this.zzbo);
        SafeParcelWriter.writeString(parcel, 13, this.packageName, false);
        SafeParcelWriter.finishObjectHeader(parcel, i);
    }
}
