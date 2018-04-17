package com.google.android.gms.wallet;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.AbstractSafeParcelable;
import com.google.android.gms.common.internal.safeparcel.SafeParcelWriter;
import com.google.android.gms.common.util.ArrayUtils;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.wallet.wobs.LabelValueRow;
import com.google.android.gms.wallet.wobs.LoyaltyPoints;
import com.google.android.gms.wallet.wobs.TextModuleData;
import com.google.android.gms.wallet.wobs.TimeInterval;
import com.google.android.gms.wallet.wobs.UriData;
import com.google.android.gms.wallet.wobs.WalletObjectMessage;
import java.util.ArrayList;

public final class LoyaltyWalletObject extends AbstractSafeParcelable {
    public static final Creator<LoyaltyWalletObject> CREATOR = new zzv();
    int state;
    String zzce;
    String zzcf;
    String zzcg;
    String zzch;
    String zzci;
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
    LoyaltyPoints zzcy;

    LoyaltyWalletObject() {
        this.zzco = ArrayUtils.newArrayList();
        this.zzcq = ArrayUtils.newArrayList();
        this.zzct = ArrayUtils.newArrayList();
        this.zzcv = ArrayUtils.newArrayList();
        this.zzcw = ArrayUtils.newArrayList();
        this.zzcx = ArrayUtils.newArrayList();
    }

    LoyaltyWalletObject(String str, String str2, String str3, String str4, String str5, String str6, String str7, String str8, String str9, String str10, int i, ArrayList<WalletObjectMessage> arrayList, TimeInterval timeInterval, ArrayList<LatLng> arrayList2, String str11, String str12, ArrayList<LabelValueRow> arrayList3, boolean z, ArrayList<UriData> arrayList4, ArrayList<TextModuleData> arrayList5, ArrayList<UriData> arrayList6, LoyaltyPoints loyaltyPoints) {
        this.zzce = str;
        this.zzcf = str2;
        this.zzcg = str3;
        this.zzch = str4;
        this.zzci = str5;
        this.zzcj = str6;
        this.zzck = str7;
        this.zzcl = str8;
        this.zzcm = str9;
        this.zzcn = str10;
        this.state = i;
        this.zzco = arrayList;
        this.zzcp = timeInterval;
        this.zzcq = arrayList2;
        this.zzcr = str11;
        this.zzcs = str12;
        this.zzct = arrayList3;
        this.zzcu = z;
        this.zzcv = arrayList4;
        this.zzcw = arrayList5;
        this.zzcx = arrayList6;
        this.zzcy = loyaltyPoints;
    }

    public final void writeToParcel(Parcel parcel, int i) {
        int beginObjectHeader = SafeParcelWriter.beginObjectHeader(parcel);
        SafeParcelWriter.writeString(parcel, 2, this.zzce, false);
        SafeParcelWriter.writeString(parcel, 3, this.zzcf, false);
        SafeParcelWriter.writeString(parcel, 4, this.zzcg, false);
        SafeParcelWriter.writeString(parcel, 5, this.zzch, false);
        SafeParcelWriter.writeString(parcel, 6, this.zzci, false);
        SafeParcelWriter.writeString(parcel, 7, this.zzcj, false);
        SafeParcelWriter.writeString(parcel, 8, this.zzck, false);
        SafeParcelWriter.writeString(parcel, 9, this.zzcl, false);
        SafeParcelWriter.writeString(parcel, 10, this.zzcm, false);
        SafeParcelWriter.writeString(parcel, 11, this.zzcn, false);
        SafeParcelWriter.writeInt(parcel, 12, this.state);
        SafeParcelWriter.writeTypedList(parcel, 13, this.zzco, false);
        SafeParcelWriter.writeParcelable(parcel, 14, this.zzcp, i, false);
        SafeParcelWriter.writeTypedList(parcel, 15, this.zzcq, false);
        SafeParcelWriter.writeString(parcel, 16, this.zzcr, false);
        SafeParcelWriter.writeString(parcel, 17, this.zzcs, false);
        SafeParcelWriter.writeTypedList(parcel, 18, this.zzct, false);
        SafeParcelWriter.writeBoolean(parcel, 19, this.zzcu);
        SafeParcelWriter.writeTypedList(parcel, 20, this.zzcv, false);
        SafeParcelWriter.writeTypedList(parcel, 21, this.zzcw, false);
        SafeParcelWriter.writeTypedList(parcel, 22, this.zzcx, false);
        SafeParcelWriter.writeParcelable(parcel, 23, this.zzcy, i, false);
        SafeParcelWriter.finishObjectHeader(parcel, beginObjectHeader);
    }
}
