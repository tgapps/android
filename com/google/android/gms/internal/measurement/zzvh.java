package com.google.android.gms.internal.measurement;

final /* synthetic */ class zzvh {
    static final /* synthetic */ int[] zzbya = new int[zzvi.values().length];
    static final /* synthetic */ int[] zzbyb = new int[zzvv.values().length];

    static {
        try {
            zzbyb[zzvv.BYTE_STRING.ordinal()] = 1;
        } catch (NoSuchFieldError e) {
        }
        try {
            zzbyb[zzvv.MESSAGE.ordinal()] = 2;
        } catch (NoSuchFieldError e2) {
        }
        try {
            zzbyb[zzvv.STRING.ordinal()] = 3;
        } catch (NoSuchFieldError e3) {
        }
        try {
            zzbya[zzvi.MAP.ordinal()] = 1;
        } catch (NoSuchFieldError e4) {
        }
        try {
            zzbya[zzvi.VECTOR.ordinal()] = 2;
        } catch (NoSuchFieldError e5) {
        }
        try {
            zzbya[zzvi.SCALAR.ordinal()] = 3;
        } catch (NoSuchFieldError e6) {
        }
    }
}
