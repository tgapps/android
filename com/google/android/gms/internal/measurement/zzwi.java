package com.google.android.gms.internal.measurement;

import com.google.android.gms.internal.measurement.zzvm.zze;

final class zzwi implements zzxk {
    private static final zzws zzcap = new zzwj();
    private final zzws zzcao;

    public zzwi() {
        this(new zzwk(zzvl.zzwb(), zzwz()));
    }

    private zzwi(zzws com_google_android_gms_internal_measurement_zzws) {
        this.zzcao = (zzws) zzvo.zza(com_google_android_gms_internal_measurement_zzws, "messageInfoFactory");
    }

    public final <T> zzxj<T> zzh(Class<T> cls) {
        zzxl.zzj(cls);
        zzwr zzf = this.zzcao.zzf(cls);
        if (zzf.zzxh()) {
            if (zzvm.class.isAssignableFrom(cls)) {
                return zzwy.zza(zzxl.zzxt(), zzvc.zzvr(), zzf.zzxi());
            }
            return zzwy.zza(zzxl.zzxr(), zzvc.zzvs(), zzf.zzxi());
        } else if (zzvm.class.isAssignableFrom(cls)) {
            if (zza(zzf)) {
                return zzwx.zza(cls, zzf, zzxc.zzxl(), zzwd.zzwy(), zzxl.zzxt(), zzvc.zzvr(), zzwq.zzxe());
            }
            return zzwx.zza(cls, zzf, zzxc.zzxl(), zzwd.zzwy(), zzxl.zzxt(), null, zzwq.zzxe());
        } else if (zza(zzf)) {
            return zzwx.zza(cls, zzf, zzxc.zzxk(), zzwd.zzwx(), zzxl.zzxr(), zzvc.zzvs(), zzwq.zzxd());
        } else {
            return zzwx.zza(cls, zzf, zzxc.zzxk(), zzwd.zzwx(), zzxl.zzxs(), null, zzwq.zzxd());
        }
    }

    private static boolean zza(zzwr com_google_android_gms_internal_measurement_zzwr) {
        return com_google_android_gms_internal_measurement_zzwr.zzxg() == zze.zzbzb;
    }

    private static zzws zzwz() {
        try {
            return (zzws) Class.forName("com.google.protobuf.DescriptorMessageInfoFactory").getDeclaredMethod("getInstance", new Class[0]).invoke(null, new Object[0]);
        } catch (Exception e) {
            return zzcap;
        }
    }
}
