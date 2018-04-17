package com.google.android.gms.wallet.fragment;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.util.AttributeSet;
import com.google.android.gms.common.internal.ReflectedParcelable;
import com.google.android.gms.common.internal.safeparcel.AbstractSafeParcelable;
import com.google.android.gms.common.internal.safeparcel.SafeParcelWriter;
import com.google.android.gms.wallet.R;

public final class WalletFragmentOptions extends AbstractSafeParcelable implements ReflectedParcelable {
    public static final Creator<WalletFragmentOptions> CREATOR = new zzf();
    private int environment;
    private int mode;
    private int theme;
    private WalletFragmentStyle zzfz;

    public final class Builder {
        private final /* synthetic */ WalletFragmentOptions zzga;

        private Builder(WalletFragmentOptions walletFragmentOptions) {
            this.zzga = walletFragmentOptions;
        }

        public final WalletFragmentOptions build() {
            return this.zzga;
        }

        public final Builder setEnvironment(int i) {
            this.zzga.environment = i;
            return this;
        }

        public final Builder setFragmentStyle(WalletFragmentStyle walletFragmentStyle) {
            this.zzga.zzfz = walletFragmentStyle;
            return this;
        }

        public final Builder setMode(int i) {
            this.zzga.mode = i;
            return this;
        }
    }

    private WalletFragmentOptions() {
        this.environment = 3;
        this.zzfz = new WalletFragmentStyle();
    }

    WalletFragmentOptions(int i, int i2, WalletFragmentStyle walletFragmentStyle, int i3) {
        this.environment = i;
        this.theme = i2;
        this.zzfz = walletFragmentStyle;
        this.mode = i3;
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public static WalletFragmentOptions zza(Context context, AttributeSet attributeSet) {
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R.styleable.WalletFragmentOptions);
        int i = obtainStyledAttributes.getInt(R.styleable.WalletFragmentOptions_appTheme, 0);
        int i2 = obtainStyledAttributes.getInt(R.styleable.WalletFragmentOptions_environment, 1);
        int resourceId = obtainStyledAttributes.getResourceId(R.styleable.WalletFragmentOptions_fragmentStyle, 0);
        int i3 = obtainStyledAttributes.getInt(R.styleable.WalletFragmentOptions_fragmentMode, 1);
        obtainStyledAttributes.recycle();
        WalletFragmentOptions walletFragmentOptions = new WalletFragmentOptions();
        walletFragmentOptions.theme = i;
        walletFragmentOptions.environment = i2;
        walletFragmentOptions.zzfz = new WalletFragmentStyle().setStyleResourceId(resourceId);
        walletFragmentOptions.zzfz.zza(context);
        walletFragmentOptions.mode = i3;
        return walletFragmentOptions;
    }

    public final int getEnvironment() {
        return this.environment;
    }

    public final WalletFragmentStyle getFragmentStyle() {
        return this.zzfz;
    }

    public final int getMode() {
        return this.mode;
    }

    public final int getTheme() {
        return this.theme;
    }

    public final void writeToParcel(Parcel parcel, int i) {
        int beginObjectHeader = SafeParcelWriter.beginObjectHeader(parcel);
        SafeParcelWriter.writeInt(parcel, 2, getEnvironment());
        SafeParcelWriter.writeInt(parcel, 3, getTheme());
        SafeParcelWriter.writeParcelable(parcel, 4, getFragmentStyle(), i, false);
        SafeParcelWriter.writeInt(parcel, 5, getMode());
        SafeParcelWriter.finishObjectHeader(parcel, beginObjectHeader);
    }

    public final void zza(Context context) {
        if (this.zzfz != null) {
            this.zzfz.zza(context);
        }
    }
}
