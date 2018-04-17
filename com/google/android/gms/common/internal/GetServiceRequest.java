package com.google.android.gms.common.internal;

import android.accounts.Account;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.Feature;
import com.google.android.gms.common.GoogleApiAvailabilityLight;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.common.internal.IAccountAccessor.Stub;
import com.google.android.gms.common.internal.safeparcel.AbstractSafeParcelable;
import com.google.android.gms.common.internal.safeparcel.SafeParcelWriter;
import java.util.Collection;

public class GetServiceRequest extends AbstractSafeParcelable {
    public static final Creator<GetServiceRequest> CREATOR = new GetServiceRequestCreator();
    private final int version;
    private final int zzst;
    private int zzsu;
    private String zzsv;
    private IBinder zzsw;
    private Scope[] zzsx;
    private Bundle zzsy;
    private Account zzsz;
    private Feature[] zzta;
    private Feature[] zztb;
    private boolean zztc;

    public GetServiceRequest(int i) {
        this.version = 4;
        this.zzsu = GoogleApiAvailabilityLight.GOOGLE_PLAY_SERVICES_VERSION_CODE;
        this.zzst = i;
        this.zztc = true;
    }

    GetServiceRequest(int i, int i2, int i3, String str, IBinder iBinder, Scope[] scopeArr, Bundle bundle, Account account, Feature[] featureArr, Feature[] featureArr2, boolean z) {
        this.version = i;
        this.zzst = i2;
        this.zzsu = i3;
        if ("com.google.android.gms".equals(str)) {
            this.zzsv = "com.google.android.gms";
        } else {
            this.zzsv = str;
        }
        if (i < 2) {
            this.zzsz = zzb(iBinder);
        } else {
            this.zzsw = iBinder;
            this.zzsz = account;
        }
        this.zzsx = scopeArr;
        this.zzsy = bundle;
        this.zzta = featureArr;
        this.zztb = featureArr2;
        this.zztc = z;
    }

    private static Account zzb(IBinder iBinder) {
        return iBinder != null ? AccountAccessor.getAccountBinderSafe(Stub.asInterface(iBinder)) : null;
    }

    public GetServiceRequest setAuthenticatedAccount(IAccountAccessor iAccountAccessor) {
        if (iAccountAccessor != null) {
            this.zzsw = iAccountAccessor.asBinder();
        }
        return this;
    }

    public GetServiceRequest setCallingPackage(String str) {
        this.zzsv = str;
        return this;
    }

    public GetServiceRequest setClientApiFeatures(Feature[] featureArr) {
        this.zztb = featureArr;
        return this;
    }

    public GetServiceRequest setClientRequestedAccount(Account account) {
        this.zzsz = account;
        return this;
    }

    public GetServiceRequest setClientRequiredFeatures(Feature[] featureArr) {
        this.zzta = featureArr;
        return this;
    }

    public GetServiceRequest setExtraArgs(Bundle bundle) {
        this.zzsy = bundle;
        return this;
    }

    public GetServiceRequest setScopes(Collection<Scope> collection) {
        this.zzsx = (Scope[]) collection.toArray(new Scope[collection.size()]);
        return this;
    }

    public void writeToParcel(Parcel parcel, int i) {
        int beginObjectHeader = SafeParcelWriter.beginObjectHeader(parcel);
        SafeParcelWriter.writeInt(parcel, 1, this.version);
        SafeParcelWriter.writeInt(parcel, 2, this.zzst);
        SafeParcelWriter.writeInt(parcel, 3, this.zzsu);
        SafeParcelWriter.writeString(parcel, 4, this.zzsv, false);
        SafeParcelWriter.writeIBinder(parcel, 5, this.zzsw, false);
        SafeParcelWriter.writeTypedArray(parcel, 6, this.zzsx, i, false);
        SafeParcelWriter.writeBundle(parcel, 7, this.zzsy, false);
        SafeParcelWriter.writeParcelable(parcel, 8, this.zzsz, i, false);
        SafeParcelWriter.writeTypedArray(parcel, 10, this.zzta, i, false);
        SafeParcelWriter.writeTypedArray(parcel, 11, this.zztb, i, false);
        SafeParcelWriter.writeBoolean(parcel, 12, this.zztc);
        SafeParcelWriter.finishObjectHeader(parcel, beginObjectHeader);
    }
}
