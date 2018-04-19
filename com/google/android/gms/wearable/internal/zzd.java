package com.google.android.gms.wearable.internal;

import android.content.IntentFilter;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.AbstractSafeParcelable;
import com.google.android.gms.common.internal.safeparcel.SafeParcelWriter;
import javax.annotation.Nullable;

public final class zzd extends AbstractSafeParcelable {
    public static final Creator<zzd> CREATOR = new zze();
    private final zzem zzaz;
    private final IntentFilter[] zzba;
    @Nullable
    private final String zzbb;
    @Nullable
    private final String zzbc;

    zzd(IBinder iBinder, IntentFilter[] intentFilterArr, @Nullable String str, @Nullable String str2) {
        zzem com_google_android_gms_wearable_internal_zzem = null;
        if (iBinder != null) {
            if (iBinder != null) {
                IInterface queryLocalInterface = iBinder.queryLocalInterface("com.google.android.gms.wearable.internal.IWearableListener");
                com_google_android_gms_wearable_internal_zzem = queryLocalInterface instanceof zzem ? (zzem) queryLocalInterface : new zzeo(iBinder);
            }
            this.zzaz = com_google_android_gms_wearable_internal_zzem;
        } else {
            this.zzaz = null;
        }
        this.zzba = intentFilterArr;
        this.zzbb = str;
        this.zzbc = str2;
    }

    public zzd(zzhk com_google_android_gms_wearable_internal_zzhk) {
        this.zzaz = com_google_android_gms_wearable_internal_zzhk;
        this.zzba = com_google_android_gms_wearable_internal_zzhk.zze();
        this.zzbb = com_google_android_gms_wearable_internal_zzhk.zzf();
        this.zzbc = null;
    }

    public final void writeToParcel(Parcel parcel, int i) {
        int beginObjectHeader = SafeParcelWriter.beginObjectHeader(parcel);
        SafeParcelWriter.writeIBinder(parcel, 2, this.zzaz == null ? null : this.zzaz.asBinder(), false);
        SafeParcelWriter.writeTypedArray(parcel, 3, this.zzba, i, false);
        SafeParcelWriter.writeString(parcel, 4, this.zzbb, false);
        SafeParcelWriter.writeString(parcel, 5, this.zzbc, false);
        SafeParcelWriter.finishObjectHeader(parcel, beginObjectHeader);
    }
}
