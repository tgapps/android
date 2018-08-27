package com.google.android.gms.internal.measurement;

final class zzxc {
    private static final zzxa zzcbq = zzxm();
    private static final zzxa zzcbr = new zzxb();

    static zzxa zzxk() {
        return zzcbq;
    }

    static zzxa zzxl() {
        return zzcbr;
    }

    private static zzxa zzxm() {
        try {
            return (zzxa) Class.forName("com.google.protobuf.NewInstanceSchemaFull").getDeclaredConstructor(new Class[0]).newInstance(new Object[0]);
        } catch (Exception e) {
            return null;
        }
    }
}
