package com.google.android.gms.internal.measurement;

public abstract class zztx<MessageType extends zztw<MessageType, BuilderType>, BuilderType extends zztx<MessageType, BuilderType>> implements zzwu {
    protected abstract BuilderType zza(MessageType messageType);

    public abstract BuilderType zztv();

    public /* synthetic */ Object clone() throws CloneNotSupportedException {
        return zztv();
    }

    public final /* synthetic */ zzwu zza(zzwt com_google_android_gms_internal_measurement_zzwt) {
        if (zzwf().getClass().isInstance(com_google_android_gms_internal_measurement_zzwt)) {
            return zza((zztw) com_google_android_gms_internal_measurement_zzwt);
        }
        throw new IllegalArgumentException("mergeFrom(MessageLite) can only merge messages of the same type.");
    }
}
