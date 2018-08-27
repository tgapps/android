package com.google.android.gms.wallet;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.AbstractSafeParcelable;
import com.google.android.gms.common.internal.safeparcel.SafeParcelWriter;

@Deprecated
public final class zza extends AbstractSafeParcelable {
    public static final Creator<zza> CREATOR = new zzb();
    private String name;
    private String zzf;
    private String zzg;
    private String zzh;
    private String zzi;
    private String zzj;
    private String zzk;
    private String zzl;
    private String zzm;
    private boolean zzn;
    private String zzo;

    public final void writeToParcel(Parcel parcel, int i) {
        int beginObjectHeader = SafeParcelWriter.beginObjectHeader(parcel);
        SafeParcelWriter.writeString(parcel, 2, this.name, false);
        SafeParcelWriter.writeString(parcel, 3, this.zzf, false);
        SafeParcelWriter.writeString(parcel, 4, this.zzg, false);
        SafeParcelWriter.writeString(parcel, 5, this.zzh, false);
        SafeParcelWriter.writeString(parcel, 6, this.zzi, false);
        SafeParcelWriter.writeString(parcel, 7, this.zzj, false);
        SafeParcelWriter.writeString(parcel, 8, this.zzk, false);
        SafeParcelWriter.writeString(parcel, 9, this.zzl, false);
        SafeParcelWriter.writeString(parcel, 10, this.zzm, false);
        SafeParcelWriter.writeBoolean(parcel, 11, this.zzn);
        SafeParcelWriter.writeString(parcel, 12, this.zzo, false);
        SafeParcelWriter.finishObjectHeader(parcel, beginObjectHeader);
    }

    zza() {
    }

    zza(String str, String str2, String str3, String str4, String str5, String str6, String str7, String str8, String str9, boolean z, String str10) {
        this.name = str;
        this.zzf = str2;
        this.zzg = str3;
        this.zzh = str4;
        this.zzi = str5;
        this.zzj = str6;
        this.zzk = str7;
        this.zzl = str8;
        this.zzm = str9;
        this.zzn = z;
        this.zzo = str10;
    }
}
