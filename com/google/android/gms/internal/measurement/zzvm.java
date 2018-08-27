package com.google.android.gms.internal.measurement;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public abstract class zzvm<MessageType extends zzvm<MessageType, BuilderType>, BuilderType extends zza<MessageType, BuilderType>> extends zztw<MessageType, BuilderType> {
    private static Map<Object, zzvm<?, ?>> zzbyo = new ConcurrentHashMap();
    protected zzyc zzbym = zzyc.zzyf();
    private int zzbyn = -1;

    public enum zze {
        public static final int zzbyt = 1;
        public static final int zzbyu = 2;
        public static final int zzbyv = 3;
        public static final int zzbyw = 4;
        public static final int zzbyx = 5;
        public static final int zzbyy = 6;
        public static final int zzbyz = 7;
        private static final /* synthetic */ int[] zzbza = new int[]{zzbyt, zzbyu, zzbyv, zzbyw, zzbyx, zzbyy, zzbyz};
        public static final int zzbzb = 1;
        public static final int zzbzc = 2;
        private static final /* synthetic */ int[] zzbzd = new int[]{zzbzb, zzbzc};
        public static final int zzbze = 1;
        public static final int zzbzf = 2;
        private static final /* synthetic */ int[] zzbzg = new int[]{zzbze, zzbzf};

        public static int[] values$50KLMJ33DTMIUPRFDTJMOP9FE1P6UT3FC9QMCBQ7CLN6ASJ1EHIM8JB5EDPM2PR59HKN8P949LIN8Q3FCHA6UIBEEPNMMP9R0() {
            return (int[]) zzbza.clone();
        }
    }

    public static class zzd<ContainingType extends zzwt, Type> extends zzux<ContainingType, Type> {
    }

    public static class zzb<T extends zzvm<T, ?>> extends zzty<T> {
        private final T zzbyp;

        public zzb(T t) {
            this.zzbyp = t;
        }

        public final /* synthetic */ Object zza(zzuo com_google_android_gms_internal_measurement_zzuo, zzuz com_google_android_gms_internal_measurement_zzuz) throws zzvt {
            return zzvm.zza(this.zzbyp, com_google_android_gms_internal_measurement_zzuo, com_google_android_gms_internal_measurement_zzuz);
        }
    }

    public static abstract class zza<MessageType extends zzvm<MessageType, BuilderType>, BuilderType extends zza<MessageType, BuilderType>> extends zztx<MessageType, BuilderType> {
        private final MessageType zzbyp;
        private MessageType zzbyq;
        private boolean zzbyr = false;

        protected zza(MessageType messageType) {
            this.zzbyp = messageType;
            this.zzbyq = (zzvm) messageType.zza(zze.zzbyw, null, null);
        }

        public final boolean isInitialized() {
            return zzvm.zza(this.zzbyq, false);
        }

        public MessageType zzwg() {
            if (this.zzbyr) {
                return this.zzbyq;
            }
            zzvm com_google_android_gms_internal_measurement_zzvm = this.zzbyq;
            zzxf.zzxn().zzag(com_google_android_gms_internal_measurement_zzvm).zzu(com_google_android_gms_internal_measurement_zzvm);
            this.zzbyr = true;
            return this.zzbyq;
        }

        public final MessageType zzwh() {
            boolean z;
            zzvm com_google_android_gms_internal_measurement_zzvm = (zzvm) zzwi();
            boolean booleanValue = Boolean.TRUE.booleanValue();
            byte byteValue = ((Byte) com_google_android_gms_internal_measurement_zzvm.zza(zze.zzbyt, null, null)).byteValue();
            if (byteValue == (byte) 1) {
                z = true;
            } else if (byteValue == (byte) 0) {
                z = false;
            } else {
                boolean zzaf = zzxf.zzxn().zzag(com_google_android_gms_internal_measurement_zzvm).zzaf(com_google_android_gms_internal_measurement_zzvm);
                if (booleanValue) {
                    Object obj;
                    int i = zze.zzbyu;
                    if (zzaf) {
                        obj = com_google_android_gms_internal_measurement_zzvm;
                    } else {
                        obj = null;
                    }
                    com_google_android_gms_internal_measurement_zzvm.zza(i, obj, null);
                }
                z = zzaf;
            }
            if (z) {
                return com_google_android_gms_internal_measurement_zzvm;
            }
            throw new zzya(com_google_android_gms_internal_measurement_zzvm);
        }

        public final BuilderType zza(MessageType messageType) {
            if (this.zzbyr) {
                zzvm com_google_android_gms_internal_measurement_zzvm = (zzvm) this.zzbyq.zza(zze.zzbyw, null, null);
                zza(com_google_android_gms_internal_measurement_zzvm, this.zzbyq);
                this.zzbyq = com_google_android_gms_internal_measurement_zzvm;
                this.zzbyr = false;
            }
            zza(this.zzbyq, messageType);
            return this;
        }

        private static void zza(MessageType messageType, MessageType messageType2) {
            zzxf.zzxn().zzag(messageType).zzd(messageType, messageType2);
        }

        protected final /* synthetic */ zztx zza(zztw com_google_android_gms_internal_measurement_zztw) {
            return zza((zzvm) com_google_android_gms_internal_measurement_zztw);
        }

        public final /* synthetic */ zztx zztv() {
            return (zza) clone();
        }

        public /* synthetic */ zzwt zzwi() {
            return zzwg();
        }

        public /* synthetic */ zzwt zzwj() {
            return zzwh();
        }

        public final /* synthetic */ zzwt zzwf() {
            return this.zzbyp;
        }

        public /* synthetic */ Object clone() throws CloneNotSupportedException {
            zza com_google_android_gms_internal_measurement_zzvm_zza = (zza) this.zzbyp.zza(zze.zzbyx, null, null);
            com_google_android_gms_internal_measurement_zzvm_zza.zza((zzvm) zzwi());
            return com_google_android_gms_internal_measurement_zzvm_zza;
        }
    }

    public static abstract class zzc<MessageType extends zzc<MessageType, BuilderType>, BuilderType> extends zzvm<MessageType, BuilderType> implements zzwv {
        protected zzvd<Object> zzbys = zzvd.zzvt();
    }

    protected abstract Object zza(int i, Object obj, Object obj2);

    public String toString() {
        return zzww.zza(this, super.toString());
    }

    public int hashCode() {
        if (this.zzbtr != 0) {
            return this.zzbtr;
        }
        this.zzbtr = zzxf.zzxn().zzag(this).hashCode(this);
        return this.zzbtr;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (((zzvm) zza(zze.zzbyy, null, null)).getClass().isInstance(obj)) {
            return zzxf.zzxn().zzag(this).equals(this, (zzvm) obj);
        }
        return false;
    }

    public final boolean isInitialized() {
        boolean booleanValue = Boolean.TRUE.booleanValue();
        byte byteValue = ((Byte) zza(zze.zzbyt, null, null)).byteValue();
        if (byteValue == (byte) 1) {
            return true;
        }
        if (byteValue == (byte) 0) {
            return false;
        }
        boolean zzaf = zzxf.zzxn().zzag(this).zzaf(this);
        if (booleanValue) {
            Object obj;
            int i = zze.zzbyu;
            if (zzaf) {
                obj = this;
            } else {
                obj = null;
            }
            zza(i, obj, null);
        }
        return zzaf;
    }

    final int zztu() {
        return this.zzbyn;
    }

    final void zzah(int i) {
        this.zzbyn = i;
    }

    public final void zzb(zzut com_google_android_gms_internal_measurement_zzut) throws IOException {
        zzxf.zzxn().zzi(getClass()).zza(this, zzuv.zza(com_google_android_gms_internal_measurement_zzut));
    }

    public final int zzvu() {
        if (this.zzbyn == -1) {
            this.zzbyn = zzxf.zzxn().zzag(this).zzae(this);
        }
        return this.zzbyn;
    }

    static <T extends zzvm<?, ?>> T zzg(Class<T> cls) {
        T t = (zzvm) zzbyo.get(cls);
        if (t == null) {
            try {
                Class.forName(cls.getName(), true, cls.getClassLoader());
                t = (zzvm) zzbyo.get(cls);
            } catch (Throwable e) {
                throw new IllegalStateException("Class initialization cannot fail.", e);
            }
        }
        if (t != null) {
            return t;
        }
        String str = "Unable to get default instance for: ";
        String valueOf = String.valueOf(cls.getName());
        throw new IllegalStateException(valueOf.length() != 0 ? str.concat(valueOf) : new String(str));
    }

    protected static <T extends zzvm<?, ?>> void zza(Class<T> cls, T t) {
        zzbyo.put(cls, t);
    }

    protected static Object zza(zzwt com_google_android_gms_internal_measurement_zzwt, String str, Object[] objArr) {
        return new zzxh(com_google_android_gms_internal_measurement_zzwt, str, objArr);
    }

    static Object zza(Method method, Object obj, Object... objArr) {
        Throwable e;
        try {
            return method.invoke(obj, objArr);
        } catch (Throwable e2) {
            throw new RuntimeException("Couldn't use Java reflection to implement protocol message reflection.", e2);
        } catch (InvocationTargetException e3) {
            e2 = e3.getCause();
            if (e2 instanceof RuntimeException) {
                throw ((RuntimeException) e2);
            } else if (e2 instanceof Error) {
                throw ((Error) e2);
            } else {
                throw new RuntimeException("Unexpected exception thrown by generated accessor method.", e2);
            }
        }
    }

    protected static final <T extends zzvm<T, ?>> boolean zza(T t, boolean z) {
        byte byteValue = ((Byte) t.zza(zze.zzbyt, null, null)).byteValue();
        if (byteValue == (byte) 1) {
            return true;
        }
        if (byteValue == (byte) 0) {
            return false;
        }
        return zzxf.zzxn().zzag(t).zzaf(t);
    }

    protected static <E> zzvs<E> zzwc() {
        return zzxg.zzxo();
    }

    static <T extends zzvm<T, ?>> T zza(T t, zzuo com_google_android_gms_internal_measurement_zzuo, zzuz com_google_android_gms_internal_measurement_zzuz) throws zzvt {
        zzvm com_google_android_gms_internal_measurement_zzvm = (zzvm) t.zza(zze.zzbyw, null, null);
        try {
            zzxf.zzxn().zzag(com_google_android_gms_internal_measurement_zzvm).zza(com_google_android_gms_internal_measurement_zzvm, zzur.zza(com_google_android_gms_internal_measurement_zzuo), com_google_android_gms_internal_measurement_zzuz);
            zzxf.zzxn().zzag(com_google_android_gms_internal_measurement_zzvm).zzu(com_google_android_gms_internal_measurement_zzvm);
            return com_google_android_gms_internal_measurement_zzvm;
        } catch (IOException e) {
            if (e.getCause() instanceof zzvt) {
                throw ((zzvt) e.getCause());
            }
            throw new zzvt(e.getMessage()).zzg(com_google_android_gms_internal_measurement_zzvm);
        } catch (RuntimeException e2) {
            if (e2.getCause() instanceof zzvt) {
                throw ((zzvt) e2.getCause());
            }
            throw e2;
        }
    }

    public final /* synthetic */ zzwu zzwd() {
        zza com_google_android_gms_internal_measurement_zzvm_zza = (zza) zza(zze.zzbyx, null, null);
        com_google_android_gms_internal_measurement_zzvm_zza.zza(this);
        return com_google_android_gms_internal_measurement_zzvm_zza;
    }

    public final /* synthetic */ zzwu zzwe() {
        return (zza) zza(zze.zzbyx, null, null);
    }

    public final /* synthetic */ zzwt zzwf() {
        return (zzvm) zza(zze.zzbyy, null, null);
    }
}
