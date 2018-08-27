package com.google.android.gms.internal.measurement;

public enum zzyq {
    DOUBLE(zzyv.DOUBLE, 1),
    FLOAT(zzyv.FLOAT, 5),
    INT64(zzyv.LONG, 0),
    UINT64(zzyv.LONG, 0),
    INT32(zzyv.INT, 0),
    FIXED64(zzyv.LONG, 1),
    FIXED32(zzyv.INT, 5),
    BOOL(zzyv.BOOLEAN, 0),
    STRING(zzyv.STRING, 2),
    GROUP(zzyv.MESSAGE, 3),
    MESSAGE(zzyv.MESSAGE, 2),
    BYTES(zzyv.BYTE_STRING, 2),
    UINT32(zzyv.INT, 0),
    ENUM(zzyv.ENUM, 0),
    SFIXED32(zzyv.INT, 5),
    SFIXED64(zzyv.LONG, 1),
    SINT32(zzyv.INT, 0),
    SINT64(zzyv.LONG, 0);
    
    private final zzyv zzcei;
    private final int zzcej;

    private zzyq(zzyv com_google_android_gms_internal_measurement_zzyv, int i) {
        this.zzcei = com_google_android_gms_internal_measurement_zzyv;
        this.zzcej = i;
    }

    public final zzyv zzyp() {
        return this.zzcei;
    }

    public final int zzyq() {
        return this.zzcej;
    }
}
