package com.google.android.gms.wallet.fragment;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import com.google.android.gms.common.internal.safeparcel.AbstractSafeParcelable;
import com.google.android.gms.common.internal.safeparcel.SafeParcelWriter;
import com.google.android.gms.wallet.R;
import org.telegram.messenger.exoplayer2.extractor.ts.TsExtractor;

public final class WalletFragmentStyle extends AbstractSafeParcelable {
    public static final Creator<WalletFragmentStyle> CREATOR = new zzg();
    private Bundle zzgb;
    private int zzgc;

    public WalletFragmentStyle() {
        this.zzgb = new Bundle();
        this.zzgb.putInt("buyButtonAppearanceDefault", 4);
        this.zzgb.putInt("maskedWalletDetailsLogoImageTypeDefault", 3);
    }

    WalletFragmentStyle(Bundle bundle, int i) {
        this.zzgb = bundle;
        this.zzgc = i;
    }

    private static long zza(int i) {
        if (i >= 0) {
            return zza(0, (float) i);
        }
        if (i == -1 || i == -2) {
            return zzc(TsExtractor.TS_STREAM_TYPE_AC3, i);
        }
        throw new IllegalArgumentException("Unexpected dimension value: " + i);
    }

    private static long zza(int i, float f) {
        switch (i) {
            case 0:
            case 1:
            case 2:
            case 3:
            case 4:
            case 5:
                return zzc(i, Float.floatToIntBits(f));
            default:
                throw new IllegalArgumentException("Unrecognized unit: " + i);
        }
    }

    private final void zza(TypedArray typedArray, int i, String str) {
        if (!this.zzgb.containsKey(str)) {
            TypedValue peekValue = typedArray.peekValue(i);
            if (peekValue != null) {
                long zzc;
                Bundle bundle = this.zzgb;
                switch (peekValue.type) {
                    case 5:
                        zzc = zzc(128, peekValue.data);
                        break;
                    case 16:
                        zzc = zza(peekValue.data);
                        break;
                    default:
                        throw new IllegalArgumentException("Unexpected dimension type: " + peekValue.type);
                }
                bundle.putLong(str, zzc);
            }
        }
    }

    private final void zza(TypedArray typedArray, int i, String str, String str2) {
        if (!this.zzgb.containsKey(str) && !this.zzgb.containsKey(str2)) {
            TypedValue peekValue = typedArray.peekValue(i);
            if (peekValue == null) {
                return;
            }
            if (peekValue.type < 28 || peekValue.type > 31) {
                this.zzgb.putInt(str2, peekValue.resourceId);
            } else {
                this.zzgb.putInt(str, peekValue.data);
            }
        }
    }

    private final void zzb(TypedArray typedArray, int i, String str) {
        if (!this.zzgb.containsKey(str)) {
            TypedValue peekValue = typedArray.peekValue(i);
            if (peekValue != null) {
                this.zzgb.putInt(str, peekValue.data);
            }
        }
    }

    private static long zzc(int i, int i2) {
        return (((long) i) << 32) | (((long) i2) & 4294967295L);
    }

    public final WalletFragmentStyle setBuyButtonAppearance(int i) {
        this.zzgb.putInt("buyButtonAppearance", i);
        return this;
    }

    public final WalletFragmentStyle setBuyButtonText(int i) {
        this.zzgb.putInt("buyButtonText", i);
        return this;
    }

    public final WalletFragmentStyle setBuyButtonWidth(int i) {
        this.zzgb.putLong("buyButtonWidth", zza(i));
        return this;
    }

    public final WalletFragmentStyle setStyleResourceId(int i) {
        this.zzgc = i;
        return this;
    }

    public final void writeToParcel(Parcel parcel, int i) {
        int beginObjectHeader = SafeParcelWriter.beginObjectHeader(parcel);
        SafeParcelWriter.writeBundle(parcel, 2, this.zzgb, false);
        SafeParcelWriter.writeInt(parcel, 3, this.zzgc);
        SafeParcelWriter.finishObjectHeader(parcel, beginObjectHeader);
    }

    public final int zza(String str, DisplayMetrics displayMetrics, int i) {
        if (!this.zzgb.containsKey(str)) {
            return i;
        }
        int i2;
        long j = this.zzgb.getLong(str);
        int i3 = (int) (j >>> 32);
        i = (int) j;
        switch (i3) {
            case 0:
                i2 = 0;
                break;
            case 1:
                i2 = 1;
                break;
            case 2:
                i2 = 2;
                break;
            case 3:
                i2 = 3;
                break;
            case 4:
                i2 = 4;
                break;
            case 5:
                i2 = 5;
                break;
            case 128:
                return TypedValue.complexToDimensionPixelSize(i, displayMetrics);
            case TsExtractor.TS_STREAM_TYPE_AC3 /*129*/:
                return i;
            default:
                throw new IllegalStateException("Unexpected unit or type: " + i3);
        }
        return Math.round(TypedValue.applyDimension(i2, Float.intBitsToFloat(i), displayMetrics));
    }

    public final void zza(Context context) {
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(this.zzgc <= 0 ? R.style.WalletFragmentDefaultStyle : this.zzgc, R.styleable.WalletFragmentStyle);
        zza(obtainStyledAttributes, R.styleable.WalletFragmentStyle_buyButtonWidth, "buyButtonWidth");
        zza(obtainStyledAttributes, R.styleable.WalletFragmentStyle_buyButtonHeight, "buyButtonHeight");
        zzb(obtainStyledAttributes, R.styleable.WalletFragmentStyle_buyButtonText, "buyButtonText");
        zzb(obtainStyledAttributes, R.styleable.WalletFragmentStyle_buyButtonAppearance, "buyButtonAppearance");
        zzb(obtainStyledAttributes, R.styleable.WalletFragmentStyle_maskedWalletDetailsTextAppearance, "maskedWalletDetailsTextAppearance");
        zzb(obtainStyledAttributes, R.styleable.WalletFragmentStyle_maskedWalletDetailsHeaderTextAppearance, "maskedWalletDetailsHeaderTextAppearance");
        zza(obtainStyledAttributes, R.styleable.WalletFragmentStyle_maskedWalletDetailsBackground, "maskedWalletDetailsBackgroundColor", "maskedWalletDetailsBackgroundResource");
        zzb(obtainStyledAttributes, R.styleable.WalletFragmentStyle_maskedWalletDetailsButtonTextAppearance, "maskedWalletDetailsButtonTextAppearance");
        zza(obtainStyledAttributes, R.styleable.WalletFragmentStyle_maskedWalletDetailsButtonBackground, "maskedWalletDetailsButtonBackgroundColor", "maskedWalletDetailsButtonBackgroundResource");
        zzb(obtainStyledAttributes, R.styleable.WalletFragmentStyle_maskedWalletDetailsLogoTextColor, "maskedWalletDetailsLogoTextColor");
        zzb(obtainStyledAttributes, R.styleable.WalletFragmentStyle_maskedWalletDetailsLogoImageType, "maskedWalletDetailsLogoImageType");
        obtainStyledAttributes.recycle();
    }
}
