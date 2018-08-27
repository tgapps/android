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
    String zzcf;
    String zzch;
    String zzck;
    String zzcl;
    String zzcm;
    @Deprecated
    String zzcn;
    String zzco;
    ArrayList<WalletObjectMessage> zzcp;
    TimeInterval zzcq;
    ArrayList<LatLng> zzcr;
    @Deprecated
    String zzcs;
    @Deprecated
    String zzct;
    ArrayList<LabelValueRow> zzcu;
    boolean zzcv;
    ArrayList<UriData> zzcw;
    ArrayList<TextModuleData> zzcx;
    ArrayList<UriData> zzcy;

    public final class zza {
        private final /* synthetic */ CommonWalletObject zzgo;

        private zza(CommonWalletObject commonWalletObject) {
            this.zzgo = commonWalletObject;
        }

        public final zza zza(String str) {
            this.zzgo.zzcf = str;
            return this;
        }

        public final CommonWalletObject zzf() {
            return this.zzgo;
        }
    }

    public static zza zze() {
        return new zza();
    }

    public void writeToParcel(Parcel parcel, int i) {
        int beginObjectHeader = SafeParcelWriter.beginObjectHeader(parcel);
        SafeParcelWriter.writeString(parcel, 2, this.zzcf, false);
        SafeParcelWriter.writeString(parcel, 3, this.zzco, false);
        SafeParcelWriter.writeString(parcel, 4, this.name, false);
        SafeParcelWriter.writeString(parcel, 5, this.zzch, false);
        SafeParcelWriter.writeString(parcel, 6, this.zzck, false);
        SafeParcelWriter.writeString(parcel, 7, this.zzcl, false);
        SafeParcelWriter.writeString(parcel, 8, this.zzcm, false);
        SafeParcelWriter.writeString(parcel, 9, this.zzcn, false);
        SafeParcelWriter.writeInt(parcel, 10, this.state);
        SafeParcelWriter.writeTypedList(parcel, 11, this.zzcp, false);
        SafeParcelWriter.writeParcelable(parcel, 12, this.zzcq, i, false);
        SafeParcelWriter.writeTypedList(parcel, 13, this.zzcr, false);
        SafeParcelWriter.writeString(parcel, 14, this.zzcs, false);
        SafeParcelWriter.writeString(parcel, 15, this.zzct, false);
        SafeParcelWriter.writeTypedList(parcel, 16, this.zzcu, false);
        SafeParcelWriter.writeBoolean(parcel, 17, this.zzcv);
        SafeParcelWriter.writeTypedList(parcel, 18, this.zzcw, false);
        SafeParcelWriter.writeTypedList(parcel, 19, this.zzcx, false);
        SafeParcelWriter.writeTypedList(parcel, 20, this.zzcy, false);
        SafeParcelWriter.finishObjectHeader(parcel, beginObjectHeader);
    }

    CommonWalletObject(String str, String str2, String str3, String str4, String str5, String str6, String str7, String str8, int i, ArrayList<WalletObjectMessage> arrayList, TimeInterval timeInterval, ArrayList<LatLng> arrayList2, String str9, String str10, ArrayList<LabelValueRow> arrayList3, boolean z, ArrayList<UriData> arrayList4, ArrayList<TextModuleData> arrayList5, ArrayList<UriData> arrayList6) {
        this.zzcf = str;
        this.zzco = str2;
        this.name = str3;
        this.zzch = str4;
        this.zzck = str5;
        this.zzcl = str6;
        this.zzcm = str7;
        this.zzcn = str8;
        this.state = i;
        this.zzcp = arrayList;
        this.zzcq = timeInterval;
        this.zzcr = arrayList2;
        this.zzcs = str9;
        this.zzct = str10;
        this.zzcu = arrayList3;
        this.zzcv = z;
        this.zzcw = arrayList4;
        this.zzcx = arrayList5;
        this.zzcy = arrayList6;
    }

    CommonWalletObject() {
        this.zzcp = ArrayUtils.newArrayList();
        this.zzcr = ArrayUtils.newArrayList();
        this.zzcu = ArrayUtils.newArrayList();
        this.zzcw = ArrayUtils.newArrayList();
        this.zzcx = ArrayUtils.newArrayList();
        this.zzcy = ArrayUtils.newArrayList();
    }
}
