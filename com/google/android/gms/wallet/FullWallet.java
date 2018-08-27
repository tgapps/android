package com.google.android.gms.wallet;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.ReflectedParcelable;
import com.google.android.gms.common.internal.safeparcel.AbstractSafeParcelable;
import com.google.android.gms.common.internal.safeparcel.SafeParcelWriter;
import com.google.android.gms.identity.intents.model.UserAddress;

public final class FullWallet extends AbstractSafeParcelable implements ReflectedParcelable {
    public static final Creator<FullWallet> CREATOR = new zzk();
    private String zzax;
    private String zzay;
    private ProxyCard zzaz;
    private String zzba;
    private zza zzbb;
    private zza zzbc;
    private String[] zzbd;
    private UserAddress zzbe;
    private UserAddress zzbf;
    private InstrumentInfo[] zzbg;
    private PaymentMethodToken zzbh;

    public final void writeToParcel(Parcel parcel, int i) {
        int beginObjectHeader = SafeParcelWriter.beginObjectHeader(parcel);
        SafeParcelWriter.writeString(parcel, 2, this.zzax, false);
        SafeParcelWriter.writeString(parcel, 3, this.zzay, false);
        SafeParcelWriter.writeParcelable(parcel, 4, this.zzaz, i, false);
        SafeParcelWriter.writeString(parcel, 5, this.zzba, false);
        SafeParcelWriter.writeParcelable(parcel, 6, this.zzbb, i, false);
        SafeParcelWriter.writeParcelable(parcel, 7, this.zzbc, i, false);
        SafeParcelWriter.writeStringArray(parcel, 8, this.zzbd, false);
        SafeParcelWriter.writeParcelable(parcel, 9, this.zzbe, i, false);
        SafeParcelWriter.writeParcelable(parcel, 10, this.zzbf, i, false);
        SafeParcelWriter.writeTypedArray(parcel, 11, this.zzbg, i, false);
        SafeParcelWriter.writeParcelable(parcel, 12, this.zzbh, i, false);
        SafeParcelWriter.finishObjectHeader(parcel, beginObjectHeader);
    }

    FullWallet(String str, String str2, ProxyCard proxyCard, String str3, zza com_google_android_gms_wallet_zza, zza com_google_android_gms_wallet_zza2, String[] strArr, UserAddress userAddress, UserAddress userAddress2, InstrumentInfo[] instrumentInfoArr, PaymentMethodToken paymentMethodToken) {
        this.zzax = str;
        this.zzay = str2;
        this.zzaz = proxyCard;
        this.zzba = str3;
        this.zzbb = com_google_android_gms_wallet_zza;
        this.zzbc = com_google_android_gms_wallet_zza2;
        this.zzbd = strArr;
        this.zzbe = userAddress;
        this.zzbf = userAddress2;
        this.zzbg = instrumentInfoArr;
        this.zzbh = paymentMethodToken;
    }

    private FullWallet() {
    }

    public final String getGoogleTransactionId() {
        return this.zzax;
    }

    public final String[] getPaymentDescriptions() {
        return this.zzbd;
    }

    public final PaymentMethodToken getPaymentMethodToken() {
        return this.zzbh;
    }
}
