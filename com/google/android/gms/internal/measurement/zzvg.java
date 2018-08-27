package com.google.android.gms.internal.measurement;

import java.lang.reflect.Type;

public enum zzvg {
    DOUBLE(0, zzvi.SCALAR, zzvv.DOUBLE),
    FLOAT(1, zzvi.SCALAR, zzvv.FLOAT),
    INT64(2, zzvi.SCALAR, zzvv.LONG),
    UINT64(3, zzvi.SCALAR, zzvv.LONG),
    INT32(4, zzvi.SCALAR, zzvv.INT),
    FIXED64(5, zzvi.SCALAR, zzvv.LONG),
    FIXED32(6, zzvi.SCALAR, zzvv.INT),
    BOOL(7, zzvi.SCALAR, zzvv.BOOLEAN),
    STRING(8, zzvi.SCALAR, zzvv.STRING),
    MESSAGE(9, zzvi.SCALAR, zzvv.MESSAGE),
    BYTES(10, zzvi.SCALAR, zzvv.BYTE_STRING),
    UINT32(11, zzvi.SCALAR, zzvv.INT),
    ENUM(12, zzvi.SCALAR, zzvv.ENUM),
    SFIXED32(13, zzvi.SCALAR, zzvv.INT),
    SFIXED64(14, zzvi.SCALAR, zzvv.LONG),
    SINT32(15, zzvi.SCALAR, zzvv.INT),
    SINT64(16, zzvi.SCALAR, zzvv.LONG),
    GROUP(17, zzvi.SCALAR, zzvv.MESSAGE),
    DOUBLE_LIST(18, zzvi.VECTOR, zzvv.DOUBLE),
    FLOAT_LIST(19, zzvi.VECTOR, zzvv.FLOAT),
    INT64_LIST(20, zzvi.VECTOR, zzvv.LONG),
    UINT64_LIST(21, zzvi.VECTOR, zzvv.LONG),
    INT32_LIST(22, zzvi.VECTOR, zzvv.INT),
    FIXED64_LIST(23, zzvi.VECTOR, zzvv.LONG),
    FIXED32_LIST(24, zzvi.VECTOR, zzvv.INT),
    BOOL_LIST(25, zzvi.VECTOR, zzvv.BOOLEAN),
    STRING_LIST(26, zzvi.VECTOR, zzvv.STRING),
    MESSAGE_LIST(27, zzvi.VECTOR, zzvv.MESSAGE),
    BYTES_LIST(28, zzvi.VECTOR, zzvv.BYTE_STRING),
    UINT32_LIST(29, zzvi.VECTOR, zzvv.INT),
    ENUM_LIST(30, zzvi.VECTOR, zzvv.ENUM),
    SFIXED32_LIST(31, zzvi.VECTOR, zzvv.INT),
    SFIXED64_LIST(32, zzvi.VECTOR, zzvv.LONG),
    SINT32_LIST(33, zzvi.VECTOR, zzvv.INT),
    SINT64_LIST(34, zzvi.VECTOR, zzvv.LONG),
    DOUBLE_LIST_PACKED(35, zzvi.PACKED_VECTOR, zzvv.DOUBLE),
    FLOAT_LIST_PACKED(36, zzvi.PACKED_VECTOR, zzvv.FLOAT),
    INT64_LIST_PACKED(37, zzvi.PACKED_VECTOR, zzvv.LONG),
    UINT64_LIST_PACKED(38, zzvi.PACKED_VECTOR, zzvv.LONG),
    INT32_LIST_PACKED(39, zzvi.PACKED_VECTOR, zzvv.INT),
    FIXED64_LIST_PACKED(40, zzvi.PACKED_VECTOR, zzvv.LONG),
    FIXED32_LIST_PACKED(41, zzvi.PACKED_VECTOR, zzvv.INT),
    BOOL_LIST_PACKED(42, zzvi.PACKED_VECTOR, zzvv.BOOLEAN),
    UINT32_LIST_PACKED(43, zzvi.PACKED_VECTOR, zzvv.INT),
    ENUM_LIST_PACKED(44, zzvi.PACKED_VECTOR, zzvv.ENUM),
    SFIXED32_LIST_PACKED(45, zzvi.PACKED_VECTOR, zzvv.INT),
    SFIXED64_LIST_PACKED(46, zzvi.PACKED_VECTOR, zzvv.LONG),
    SINT32_LIST_PACKED(47, zzvi.PACKED_VECTOR, zzvv.INT),
    SINT64_LIST_PACKED(48, zzvi.PACKED_VECTOR, zzvv.LONG),
    GROUP_LIST(49, zzvi.VECTOR, zzvv.MESSAGE),
    MAP(50, zzvi.MAP, zzvv.VOID);
    
    private static final zzvg[] zzbxx = null;
    private static final Type[] zzbxy = null;
    private final int id;
    private final zzvv zzbxt;
    private final zzvi zzbxu;
    private final Class<?> zzbxv;
    private final boolean zzbxw;

    private zzvg(int i, zzvi com_google_android_gms_internal_measurement_zzvi, zzvv com_google_android_gms_internal_measurement_zzvv) {
        this.id = i;
        this.zzbxu = com_google_android_gms_internal_measurement_zzvi;
        this.zzbxt = com_google_android_gms_internal_measurement_zzvv;
        switch (zzvh.zzbya[com_google_android_gms_internal_measurement_zzvi.ordinal()]) {
            case 1:
                this.zzbxv = com_google_android_gms_internal_measurement_zzvv.zzws();
                break;
            case 2:
                this.zzbxv = com_google_android_gms_internal_measurement_zzvv.zzws();
                break;
            default:
                this.zzbxv = null;
                break;
        }
        boolean z = false;
        if (com_google_android_gms_internal_measurement_zzvi == zzvi.SCALAR) {
            switch (zzvh.zzbyb[com_google_android_gms_internal_measurement_zzvv.ordinal()]) {
                case 1:
                case 2:
                case 3:
                    break;
                default:
                    z = true;
                    break;
            }
        }
        this.zzbxw = z;
    }

    public final int id() {
        return this.id;
    }

    static {
        zzbxy = new Type[0];
        zzvg[] values = values();
        zzbxx = new zzvg[values.length];
        int length = values.length;
        int i;
        while (i < length) {
            zzvg com_google_android_gms_internal_measurement_zzvg = values[i];
            zzbxx[com_google_android_gms_internal_measurement_zzvg.id] = com_google_android_gms_internal_measurement_zzvg;
            i++;
        }
    }
}
