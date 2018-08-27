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
    String zzcf;
    String zzcg;
    String zzch;
    String zzci;
    String zzcj;
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
    LoyaltyPoints zzcz;

    public final void writeToParcel(Parcel parcel, int i) {
        int beginObjectHeader = SafeParcelWriter.beginObjectHeader(parcel);
        SafeParcelWriter.writeString(parcel, 2, this.zzcf, false);
        SafeParcelWriter.writeString(parcel, 3, this.zzcg, false);
        SafeParcelWriter.writeString(parcel, 4, this.zzch, false);
        SafeParcelWriter.writeString(parcel, 5, this.zzci, false);
        SafeParcelWriter.writeString(parcel, 6, this.zzcj, false);
        SafeParcelWriter.writeString(parcel, 7, this.zzck, false);
        SafeParcelWriter.writeString(parcel, 8, this.zzcl, false);
        SafeParcelWriter.writeString(parcel, 9, this.zzcm, false);
        SafeParcelWriter.writeString(parcel, 10, this.zzcn, false);
        SafeParcelWriter.writeString(parcel, 11, this.zzco, false);
        SafeParcelWriter.writeInt(parcel, 12, this.state);
        SafeParcelWriter.writeTypedList(parcel, 13, this.zzcp, false);
        SafeParcelWriter.writeParcelable(parcel, 14, this.zzcq, i, false);
        SafeParcelWriter.writeTypedList(parcel, 15, this.zzcr, false);
        SafeParcelWriter.writeString(parcel, 16, this.zzcs, false);
        SafeParcelWriter.writeString(parcel, 17, this.zzct, false);
        SafeParcelWriter.writeTypedList(parcel, 18, this.zzcu, false);
        SafeParcelWriter.writeBoolean(parcel, 19, this.zzcv);
        SafeParcelWriter.writeTypedList(parcel, 20, this.zzcw, false);
        SafeParcelWriter.writeTypedList(parcel, 21, this.zzcx, false);
        SafeParcelWriter.writeTypedList(parcel, 22, this.zzcy, false);
        SafeParcelWriter.writeParcelable(parcel, 23, this.zzcz, i, false);
        SafeParcelWriter.finishObjectHeader(parcel, beginObjectHeader);
    }

    LoyaltyWalletObject(String str, String str2, String str3, String str4, String str5, String str6, String str7, String str8, String str9, String str10, int i, ArrayList<WalletObjectMessage> arrayList, TimeInterval timeInterval, ArrayList<LatLng> arrayList2, String str11, String str12, ArrayList<LabelValueRow> arrayList3, boolean z, ArrayList<UriData> arrayList4, ArrayList<TextModuleData> arrayList5, ArrayList<UriData> arrayList6, LoyaltyPoints loyaltyPoints) {
        this.zzcf = str;
        this.zzcg = str2;
        this.zzch = str3;
        this.zzci = str4;
        this.zzcj = str5;
        this.zzck = str6;
        this.zzcl = str7;
        this.zzcm = str8;
        this.zzcn = str9;
        this.zzco = str10;
        this.state = i;
        this.zzcp = arrayList;
        this.zzcq = timeInterval;
        this.zzcr = arrayList2;
        this.zzcs = str11;
        this.zzct = str12;
        this.zzcu = arrayList3;
        this.zzcv = z;
        this.zzcw = arrayList4;
        this.zzcx = arrayList5;
        this.zzcy = arrayList6;
        this.zzcz = loyaltyPoints;
    }

    LoyaltyWalletObject() {
        this.zzcp = ArrayUtils.newArrayList();
        this.zzcr = ArrayUtils.newArrayList();
        this.zzcu = ArrayUtils.newArrayList();
        this.zzcw = ArrayUtils.newArrayList();
        this.zzcx = ArrayUtils.newArrayList();
        this.zzcy = ArrayUtils.newArrayList();
    }
}
