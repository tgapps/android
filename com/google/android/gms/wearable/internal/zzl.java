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
        return this.id != com_google_android_gms_wearable_internal_zzl.id ? false : this.zzbl != com_google_android_gms_wearable_internal_zzl.zzbl ? false : this.zzbm != com_google_android_gms_wearable_internal_zzl.zzbm ? false : this.zzbn != com_google_android_gms_wearable_internal_zzl.zzbn ? false : this.zzbo != com_google_android_gms_wearable_internal_zzl.zzbo ? false : !this.zzbf.equals(com_google_android_gms_wearable_internal_zzl.zzbf) ? false : (this.zzbg == null ? com_google_android_gms_wearable_internal_zzl.zzbg != null : !this.zzbg.equals(com_google_android_gms_wearable_internal_zzl.zzbg)) ? false : !this.zzbh.equals(com_google_android_gms_wearable_internal_zzl.zzbh) ? false : !this.zzbi.equals(com_google_android_gms_wearable_internal_zzl.zzbi) ? false : !this.zzbj.equals(com_google_android_gms_wearable_internal_zzl.zzbj) ? false : (this.zzbk == null ? com_google_android_gms_wearable_internal_zzl.zzbk != null : !this.zzbk.equals(com_google_android_gms_wearable_internal_zzl.zzbk)) ? false : this.packageName != null ? this.packageName.equals(com_google_android_gms_wearable_internal_zzl.packageName) : com_google_android_gms_wearable_internal_zzl.packageName == null;
    }

    public final int hashCode() {
        int i = 0;
        int hashCode = ((((((((((this.zzbk != null ? this.zzbk.hashCode() : 0) + (((((((((this.zzbg != null ? this.zzbg.hashCode() : 0) + ((((this.id + 31) * 31) + this.zzbf.hashCode()) * 31)) * 31) + this.zzbh.hashCode()) * 31) + this.zzbi.hashCode()) * 31) + this.zzbj.hashCode()) * 31)) * 31) + this.zzbl) * 31) + this.zzbm) * 31) + this.zzbn) * 31) + this.zzbo) * 31;
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
        return new StringBuilder(((((((String.valueOf(str).length() + 211) + String.valueOf(str2).length()) + String.valueOf(str3).length()) + String.valueOf(str4).length()) + String.valueOf(str5).length()) + String.valueOf(str6).length()) + String.valueOf(str7).length()).append("AncsNotificationParcelable{, id=").append(i).append(", appId='").append(str).append('\'').append(", dateTime='").append(str2).append('\'').append(", notificationText='").append(str3).append('\'').append(", title='").append(str4).append('\'').append(", subtitle='").append(str5).append('\'').append(", displayName='").append(str6).append('\'').append(", eventId=").append(b).append(", eventFlags=").append(b2).append(", categoryId=").append(b3).append(", categoryCount=").append(b4).append(", packageName='").append(str7).append('\'').append('}').toString();
    }

    public final void writeToParcel(Parcel parcel, int i) {
        int beginObjectHeader = SafeParcelWriter.beginObjectHeader(parcel);
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
        SafeParcelWriter.finishObjectHeader(parcel, beginObjectHeader);
    }
}
