package com.google.android.gms.wallet;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.AbstractSafeParcelable;
import com.google.android.gms.common.internal.safeparcel.SafeParcelWriter;

@Deprecated
public final class zza extends AbstractSafeParcelable {
    public static final Creator<zza> CREATOR = new zzb();
    private String name;
    private String zze;
    private String zzf;
    private String zzg;
    private String zzh;
    private String zzi;
    private String zzj;
    private String zzk;
    private String zzl;
    private boolean zzm;
    private String zzn;

    zza() {
    }

    zza(String str, String str2, String str3, String str4, String str5, String str6, String str7, String str8, String str9, boolean z, String str10) {
        this.name = str;
        this.zze = str2;
        this.zzf = str3;
        this.zzg = str4;
        this.zzh = str5;
        this.zzi = str6;
        this.zzj = str7;
        this.zzk = str8;
        this.zzl = str9;
        this.zzm = z;
        this.zzn = str10;
    }

    public final void writeToParcel(Parcel parcel, int i) {
        int beginObjectHeader = SafeParcelWriter.beginObjectHeader(parcel);
        SafeParcelWriter.writeString(parcel, 2, this.name, false);
        SafeParcelWriter.writeString(parcel, 3, this.zze, false);
        SafeParcelWriter.writeString(parcel, 4, this.zzf, false);
        SafeParcelWriter.writeString(parcel, 5, this.zzg, false);
        SafeParcelWriter.writeString(parcel, 6, this.zzh, false);
        SafeParcelWriter.writeString(parcel, 7, this.zzi, false);
        SafeParcelWriter.writeString(parcel, 8, this.zzj, false);
        SafeParcelWriter.writeString(parcel, 9, this.zzk, false);
        SafeParcelWriter.writeString(parcel, 10, this.zzl, false);
        SafeParcelWriter.writeBoolean(parcel, 11, this.zzm);
        SafeParcelWriter.writeString(parcel, 12, this.zzn, false);
        SafeParcelWriter.finishObjectHeader(parcel, beginObjectHeader);
    }
}
