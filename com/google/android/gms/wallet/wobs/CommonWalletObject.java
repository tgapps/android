package com.google.android.gms.wallet.wobs;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.annotation.KeepName;
import com.google.android.gms.common.internal.safeparcel.AbstractSafeParcelable;
import com.google.android.gms.common.internal.safeparcel.SafeParcelWriter;
import com.google.android.gms.common.util.ArrayUtils;
import com.google.android.gms.maps.model.LatLng;
import java.util.ArrayList;

@KeepName
public class CommonWalletObject extends AbstractSafeParcelable {
    public static final Creator<CommonWalletObject> CREATOR = new zzb();
    String name;
    int state;
    String zzce;
    String zzcg;
    String zzcj;
    String zzck;
    String zzcl;
    String zzcm;
    String zzcn;
    ArrayList<WalletObjectMessage> zzco;
    TimeInterval zzcp;
    ArrayList<LatLng> zzcq;
    String zzcr;
    String zzcs;
    ArrayList<LabelValueRow> zzct;
    boolean zzcu;
    ArrayList<UriData> zzcv;
    ArrayList<TextModuleData> zzcw;
    ArrayList<UriData> zzcx;

    public final class zza {
        private final /* synthetic */ CommonWalletObject zzgm;

        private zza(CommonWalletObject commonWalletObject) {
            this.zzgm = commonWalletObject;
        }

        public final zza zza(String str) {
            this.zzgm.zzce = str;
            return this;
        }

        public final CommonWalletObject zzf() {
            return this.zzgm;
        }
    }

    CommonWalletObject() {
        this.zzco = ArrayUtils.newArrayList();
        this.zzcq = ArrayUtils.newArrayList();
        this.zzct = ArrayUtils.newArrayList();
        this.zzcv = ArrayUtils.newArrayList();
        this.zzcw = ArrayUtils.newArrayList();
        this.zzcx = ArrayUtils.newArrayList();
    }

    CommonWalletObject(String str, String str2, String str3, String str4, String str5, String str6, String str7, String str8, int i, ArrayList<WalletObjectMessage> arrayList, TimeInterval timeInterval, ArrayList<LatLng> arrayList2, String str9, String str10, ArrayList<LabelValueRow> arrayList3, boolean z, ArrayList<UriData> arrayList4, ArrayList<TextModuleData> arrayList5, ArrayList<UriData> arrayList6) {
        this.zzce = str;
        this.zzcn = str2;
        this.name = str3;
        this.zzcg = str4;
        this.zzcj = str5;
        this.zzck = str6;
        this.zzcl = str7;
        this.zzcm = str8;
        this.state = i;
        this.zzco = arrayList;
        this.zzcp = timeInterval;
        this.zzcq = arrayList2;
        this.zzcr = str9;
        this.zzcs = str10;
        this.zzct = arrayList3;
        this.zzcu = z;
        this.zzcv = arrayList4;
        this.zzcw = arrayList5;
        this.zzcx = arrayList6;
    }

    public static zza zze() {
        return new zza();
    }

    public void writeToParcel(Parcel parcel, int i) {
        int beginObjectHeader = SafeParcelWriter.beginObjectHeader(parcel);
        SafeParcelWriter.writeString(parcel, 2, this.zzce, false);
        SafeParcelWriter.writeString(parcel, 3, this.zzcn, false);
        SafeParcelWriter.writeString(parcel, 4, this.name, false);
        SafeParcelWriter.writeString(parcel, 5, this.zzcg, false);
        SafeParcelWriter.writeString(parcel, 6, this.zzcj, false);
        SafeParcelWriter.writeString(parcel, 7, this.zzck, false);
        SafeParcelWriter.writeString(parcel, 8, this.zzcl, false);
        SafeParcelWriter.writeString(parcel, 9, this.zzcm, false);
        SafeParcelWriter.writeInt(parcel, 10, this.state);
        SafeParcelWriter.writeTypedList(parcel, 11, this.zzco, false);
        SafeParcelWriter.writeParcelable(parcel, 12, this.zzcp, i, false);
        SafeParcelWriter.writeTypedList(parcel, 13, this.zzcq, false);
        SafeParcelWriter.writeString(parcel, 14, this.zzcr, false);
        SafeParcelWriter.writeString(parcel, 15, this.zzcs, false);
        SafeParcelWriter.writeTypedList(parcel, 16, this.zzct, false);
        SafeParcelWriter.writeBoolean(parcel, 17, this.zzcu);
        SafeParcelWriter.writeTypedList(parcel, 18, this.zzcv, false);
        SafeParcelWriter.writeTypedList(parcel, 19, this.zzcw, false);
        SafeParcelWriter.writeTypedList(parcel, 20, this.zzcx, false);
        SafeParcelWriter.finishObjectHeader(parcel, beginObjectHeader);
    }
}
