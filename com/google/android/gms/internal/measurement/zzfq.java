package com.google.android.gms.internal.measurement;

import com.google.android.gms.internal.measurement.zzvm.zze;

public final class zzfq {

    public static final class zza extends zzvm<zza, zza> implements zzwv {
        private static final zza zzauq = new zza();
        private static volatile zzxd<zza> zznw;
        private String zzauo = TtmlNode.ANONYMOUS_REGION_ID;
        private long zzaup;
        private int zznr;

        public static final class zza extends com.google.android.gms.internal.measurement.zzvm.zza<zza, zza> implements zzwv {
            private zza() {
                super(zza.zzauq);
            }
        }

        private zza() {
        }

        protected final Object zza(int i, Object obj, Object obj2) {
            switch (zzfr.zznq[i - 1]) {
                case 1:
                    return new zza();
                case 2:
                    return new zza();
                case 3:
                    Object[] objArr = new Object[]{"zznr", "zzauo", "zzaup"};
                    return zzvm.zza(zzauq, "\u0001\u0002\u0000\u0001\u0001\u0002\u0002\u0000\u0000\u0000\u0001\b\u0000\u0002\u0002\u0001", objArr);
                case 4:
                    return zzauq;
                case 5:
                    Object obj3 = zznw;
                    if (obj3 != null) {
                        return obj3;
                    }
                    synchronized (zza.class) {
                        obj3 = zznw;
                        if (obj3 == null) {
                            obj3 = new com.google.android.gms.internal.measurement.zzvm.zzb(zzauq);
                            zznw = obj3;
                        }
                    }
                    return obj3;
                case 6:
                    return Byte.valueOf((byte) 1);
                case 7:
                    return null;
                default:
                    throw new UnsupportedOperationException();
            }
        }

        static {
            zzvm.zza(zza.class, zzauq);
        }
    }

    public static final class zzb extends zzvm<zzb, zza> implements zzwv {
        private static final zzb zzaut = new zzb();
        private static volatile zzxd<zzb> zznw;
        private int zzaur = 1;
        private zzvs<zza> zzaus = zzvm.zzwc();
        private int zznr;

        public enum zzb implements zzvp {
            RADS(1),
            PROVISIONING(2);
            
            private static final zzvq<zzb> zzoa = null;
            private final int value;

            public final int zzc() {
                return this.value;
            }

            public static zzb zzs(int i) {
                switch (i) {
                    case 1:
                        return RADS;
                    case 2:
                        return PROVISIONING;
                    default:
                        return null;
                }
            }

            public static zzvr zzd() {
                return zzft.zzoc;
            }

            private zzb(int i) {
                this.value = i;
            }

            static {
                zzoa = new zzfs();
            }
        }

        public static final class zza extends com.google.android.gms.internal.measurement.zzvm.zza<zzb, zza> implements zzwv {
            private zza() {
                super(zzb.zzaut);
            }
        }

        private zzb() {
        }

        protected final Object zza(int i, Object obj, Object obj2) {
            switch (zzfr.zznq[i - 1]) {
                case 1:
                    return new zzb();
                case 2:
                    return new zza();
                case 3:
                    Object[] objArr = new Object[]{"zznr", "zzaur", zzb.zzd(), "zzaus", zza.class};
                    return zzvm.zza(zzaut, "\u0001\u0002\u0000\u0001\u0001\u0002\u0002\u0000\u0001\u0000\u0001\f\u0000\u0002\u001b", objArr);
                case 4:
                    return zzaut;
                case 5:
                    Object obj3 = zznw;
                    if (obj3 != null) {
                        return obj3;
                    }
                    synchronized (zzb.class) {
                        obj3 = zznw;
                        if (obj3 == null) {
                            obj3 = new com.google.android.gms.internal.measurement.zzvm.zzb(zzaut);
                            zznw = obj3;
                        }
                    }
                    return obj3;
                case 6:
                    return Byte.valueOf((byte) 1);
                case 7:
                    return null;
                default:
                    throw new UnsupportedOperationException();
            }
        }

        public static zzxd<zzb> zza() {
            return (zzxd) zzaut.zza(zze.zzbyz, null, null);
        }

        static {
            zzvm.zza(zzb.class, zzaut);
        }
    }
}
