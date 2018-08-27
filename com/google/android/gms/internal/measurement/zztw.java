package com.google.android.gms.internal.measurement;

public abstract class zztw<MessageType extends zztw<MessageType, BuilderType>, BuilderType extends zztx<MessageType, BuilderType>> implements zzwt {
    private static boolean zzbts = false;
    protected int zzbtr = 0;

    public final zzud zztt() {
        try {
            zzuk zzam = zzud.zzam(zzvu());
            zzb(zzam.zzuf());
            return zzam.zzue();
        } catch (Throwable e) {
            String str = "ByteString";
            String name = getClass().getName();
            throw new RuntimeException(new StringBuilder((String.valueOf(name).length() + 62) + String.valueOf(str).length()).append("Serializing ").append(name).append(" to a ").append(str).append(" threw an IOException (should never happen).").toString(), e);
        }
    }

    int zztu() {
        throw new UnsupportedOperationException();
    }

    void zzah(int i) {
        throw new UnsupportedOperationException();
    }
}
