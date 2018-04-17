package com.google.android.gms.wallet;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.ReflectedParcelable;
import com.google.android.gms.common.internal.safeparcel.AbstractSafeParcelable;
import com.google.android.gms.common.internal.safeparcel.SafeParcelWriter;
import com.google.android.gms.identity.intents.model.UserAddress;

public final class FullWallet extends AbstractSafeParcelable implements ReflectedParcelable {
    public static final Creator<FullWallet> CREATOR = new zzk();
    private String zzaw;
    private String zzax;
    private ProxyCard zzay;
    private String zzaz;
    private zza zzba;
    private zza zzbb;
    private String[] zzbc;
    private UserAddress zzbd;
    private UserAddress zzbe;
    private InstrumentInfo[] zzbf;
    private PaymentMethodToken zzbg;

    private FullWallet() {
    }

    FullWallet(String str, String str2, ProxyCard proxyCard, String str3, zza com_google_android_gms_wallet_zza, zza com_google_android_gms_wallet_zza2, String[] strArr, UserAddress userAddress, UserAddress userAddress2, InstrumentInfo[] instrumentInfoArr, PaymentMethodToken paymentMethodToken) {
        this.zzaw = str;
        this.zzax = str2;
        this.zzay = proxyCard;
        this.zzaz = str3;
        this.zzba = com_google_android_gms_wallet_zza;
        this.zzbb = com_google_android_gms_wallet_zza2;
        this.zzbc = strArr;
        this.zzbd = userAddress;
        this.zzbe = userAddress2;
        this.zzbf = instrumentInfoArr;
        this.zzbg = paymentMethodToken;
    }

    public final String getGoogleTransactionId() {
        return this.zzaw;
    }

    public final String[] getPaymentDescriptions() {
        return this.zzbc;
    }

    public final PaymentMethodToken getPaymentMethodToken() {
        return this.zzbg;
    }

    public final void writeToParcel(Parcel parcel, int i) {
        int beginObjectHeader = SafeParcelWriter.beginObjectHeader(parcel);
        SafeParcelWriter.writeString(parcel, 2, this.zzaw, false);
        SafeParcelWriter.writeString(parcel, 3, this.zzax, false);
        SafeParcelWriter.writeParcelable(parcel, 4, this.zzay, i, false);
        SafeParcelWriter.writeString(parcel, 5, this.zzaz, false);
        SafeParcelWriter.writeParcelable(parcel, 6, this.zzba, i, false);
        SafeParcelWriter.writeParcelable(parcel, 7, this.zzbb, i, false);
        SafeParcelWriter.writeStringArray(parcel, 8, this.zzbc, false);
        SafeParcelWriter.writeParcelable(parcel, 9, this.zzbd, i, false);
        SafeParcelWriter.writeParcelable(parcel, 10, this.zzbe, i, false);
        SafeParcelWriter.writeTypedArray(parcel, 11, this.zzbf, i, false);
        SafeParcelWriter.writeParcelable(parcel, 12, this.zzbg, i, false);
        SafeParcelWriter.finishObjectHeader(parcel, beginObjectHeader);
    }
}
