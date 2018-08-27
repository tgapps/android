package com.google.android.gms.internal.measurement;

public enum zzvv {
    VOID(Void.class, Void.class, null),
    INT(Integer.TYPE, Integer.class, Integer.valueOf(0)),
    LONG(Long.TYPE, Long.class, Long.valueOf(0)),
    FLOAT(Float.TYPE, Float.class, Float.valueOf(0.0f)),
    DOUBLE(Double.TYPE, Double.class, Double.valueOf(0.0d)),
    BOOLEAN(Boolean.TYPE, Boolean.class, Boolean.valueOf(false)),
    STRING(String.class, String.class, TtmlNode.ANONYMOUS_REGION_ID),
    BYTE_STRING(zzud.class, zzud.class, zzud.zzbtz),
    ENUM(Integer.TYPE, Integer.class, null),
    MESSAGE(Object.class, Object.class, null);
    
    private final Class<?> zzbzx;
    private final Class<?> zzbzy;
    private final Object zzbzz;

    private zzvv(Class<?> cls, Class<?> cls2, Object obj) {
        this.zzbzx = cls;
        this.zzbzy = cls2;
        this.zzbzz = obj;
    }

    public final Class<?> zzws() {
        return this.zzbzy;
    }
}
