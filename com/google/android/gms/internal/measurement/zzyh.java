package com.google.android.gms.internal.measurement;

import java.lang.reflect.Field;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.security.AccessController;
import java.util.logging.Level;
import java.util.logging.Logger;
import libcore.io.Memory;
import sun.misc.Unsafe;

final class zzyh {
    private static final Logger logger = Logger.getLogger(zzyh.class.getName());
    private static final Class<?> zzbtv = zzua.zztz();
    private static final boolean zzbuv = zzyl();
    private static final Unsafe zzcay = zzyk();
    private static final boolean zzccv = zzm(Long.TYPE);
    private static final boolean zzccw = zzm(Integer.TYPE);
    private static final zzd zzccx;
    private static final boolean zzccy = zzym();
    private static final long zzccz = ((long) zzk(byte[].class));
    private static final long zzcda = ((long) zzk(boolean[].class));
    private static final long zzcdb = ((long) zzl(boolean[].class));
    private static final long zzcdc = ((long) zzk(int[].class));
    private static final long zzcdd = ((long) zzl(int[].class));
    private static final long zzcde = ((long) zzk(long[].class));
    private static final long zzcdf = ((long) zzl(long[].class));
    private static final long zzcdg = ((long) zzk(float[].class));
    private static final long zzcdh = ((long) zzl(float[].class));
    private static final long zzcdi = ((long) zzk(double[].class));
    private static final long zzcdj = ((long) zzl(double[].class));
    private static final long zzcdk = ((long) zzk(Object[].class));
    private static final long zzcdl = ((long) zzl(Object[].class));
    private static final long zzcdm;
    private static final boolean zzcdn;

    static abstract class zzd {
        Unsafe zzcdo;

        zzd(Unsafe unsafe) {
            this.zzcdo = unsafe;
        }

        public abstract void zza(long j, byte b);

        public abstract void zza(Object obj, long j, double d);

        public abstract void zza(Object obj, long j, float f);

        public abstract void zza(Object obj, long j, boolean z);

        public abstract void zza(byte[] bArr, long j, long j2, long j3);

        public abstract void zze(Object obj, long j, byte b);

        public abstract boolean zzm(Object obj, long j);

        public abstract float zzn(Object obj, long j);

        public abstract double zzo(Object obj, long j);

        public abstract byte zzy(Object obj, long j);

        public final int zzk(Object obj, long j) {
            return this.zzcdo.getInt(obj, j);
        }

        public final void zzb(Object obj, long j, int i) {
            this.zzcdo.putInt(obj, j, i);
        }

        public final long zzl(Object obj, long j) {
            return this.zzcdo.getLong(obj, j);
        }

        public final void zza(Object obj, long j, long j2) {
            this.zzcdo.putLong(obj, j, j2);
        }
    }

    static final class zza extends zzd {
        zza(Unsafe unsafe) {
            super(unsafe);
        }

        public final void zza(long j, byte b) {
            Memory.pokeByte((int) (-1 & j), b);
        }

        public final byte zzy(Object obj, long j) {
            if (zzyh.zzcdn) {
                return zzyh.zzq(obj, j);
            }
            return zzyh.zzr(obj, j);
        }

        public final void zze(Object obj, long j, byte b) {
            if (zzyh.zzcdn) {
                zzyh.zza(obj, j, b);
            } else {
                zzyh.zzb(obj, j, b);
            }
        }

        public final boolean zzm(Object obj, long j) {
            if (zzyh.zzcdn) {
                return zzyh.zzs(obj, j);
            }
            return zzyh.zzt(obj, j);
        }

        public final void zza(Object obj, long j, boolean z) {
            if (zzyh.zzcdn) {
                zzyh.zzb(obj, j, z);
            } else {
                zzyh.zzc(obj, j, z);
            }
        }

        public final float zzn(Object obj, long j) {
            return Float.intBitsToFloat(zzk(obj, j));
        }

        public final void zza(Object obj, long j, float f) {
            zzb(obj, j, Float.floatToIntBits(f));
        }

        public final double zzo(Object obj, long j) {
            return Double.longBitsToDouble(zzl(obj, j));
        }

        public final void zza(Object obj, long j, double d) {
            zza(obj, j, Double.doubleToLongBits(d));
        }

        public final void zza(byte[] bArr, long j, long j2, long j3) {
            Memory.pokeByteArray((int) (-1 & j2), bArr, (int) j, (int) j3);
        }
    }

    static final class zzb extends zzd {
        zzb(Unsafe unsafe) {
            super(unsafe);
        }

        public final void zza(long j, byte b) {
            Memory.pokeByte(j, b);
        }

        public final byte zzy(Object obj, long j) {
            if (zzyh.zzcdn) {
                return zzyh.zzq(obj, j);
            }
            return zzyh.zzr(obj, j);
        }

        public final void zze(Object obj, long j, byte b) {
            if (zzyh.zzcdn) {
                zzyh.zza(obj, j, b);
            } else {
                zzyh.zzb(obj, j, b);
            }
        }

        public final boolean zzm(Object obj, long j) {
            if (zzyh.zzcdn) {
                return zzyh.zzs(obj, j);
            }
            return zzyh.zzt(obj, j);
        }

        public final void zza(Object obj, long j, boolean z) {
            if (zzyh.zzcdn) {
                zzyh.zzb(obj, j, z);
            } else {
                zzyh.zzc(obj, j, z);
            }
        }

        public final float zzn(Object obj, long j) {
            return Float.intBitsToFloat(zzk(obj, j));
        }

        public final void zza(Object obj, long j, float f) {
            zzb(obj, j, Float.floatToIntBits(f));
        }

        public final double zzo(Object obj, long j) {
            return Double.longBitsToDouble(zzl(obj, j));
        }

        public final void zza(Object obj, long j, double d) {
            zza(obj, j, Double.doubleToLongBits(d));
        }

        public final void zza(byte[] bArr, long j, long j2, long j3) {
            Memory.pokeByteArray(j2, bArr, (int) j, (int) j3);
        }
    }

    static final class zzc extends zzd {
        zzc(Unsafe unsafe) {
            super(unsafe);
        }

        public final void zza(long j, byte b) {
            this.zzcdo.putByte(j, b);
        }

        public final byte zzy(Object obj, long j) {
            return this.zzcdo.getByte(obj, j);
        }

        public final void zze(Object obj, long j, byte b) {
            this.zzcdo.putByte(obj, j, b);
        }

        public final boolean zzm(Object obj, long j) {
            return this.zzcdo.getBoolean(obj, j);
        }

        public final void zza(Object obj, long j, boolean z) {
            this.zzcdo.putBoolean(obj, j, z);
        }

        public final float zzn(Object obj, long j) {
            return this.zzcdo.getFloat(obj, j);
        }

        public final void zza(Object obj, long j, float f) {
            this.zzcdo.putFloat(obj, j, f);
        }

        public final double zzo(Object obj, long j) {
            return this.zzcdo.getDouble(obj, j);
        }

        public final void zza(Object obj, long j, double d) {
            this.zzcdo.putDouble(obj, j, d);
        }

        public final void zza(byte[] bArr, long j, long j2, long j3) {
            this.zzcdo.copyMemory(bArr, zzyh.zzccz + j, null, j2, j3);
        }
    }

    private zzyh() {
    }

    static boolean zzyi() {
        return zzbuv;
    }

    static boolean zzyj() {
        return zzccy;
    }

    private static int zzk(Class<?> cls) {
        if (zzbuv) {
            return zzccx.zzcdo.arrayBaseOffset(cls);
        }
        return -1;
    }

    private static int zzl(Class<?> cls) {
        if (zzbuv) {
            return zzccx.zzcdo.arrayIndexScale(cls);
        }
        return -1;
    }

    static int zzk(Object obj, long j) {
        return zzccx.zzk(obj, j);
    }

    static void zzb(Object obj, long j, int i) {
        zzccx.zzb(obj, j, i);
    }

    static long zzl(Object obj, long j) {
        return zzccx.zzl(obj, j);
    }

    static void zza(Object obj, long j, long j2) {
        zzccx.zza(obj, j, j2);
    }

    static boolean zzm(Object obj, long j) {
        return zzccx.zzm(obj, j);
    }

    static void zza(Object obj, long j, boolean z) {
        zzccx.zza(obj, j, z);
    }

    static float zzn(Object obj, long j) {
        return zzccx.zzn(obj, j);
    }

    static void zza(Object obj, long j, float f) {
        zzccx.zza(obj, j, f);
    }

    static double zzo(Object obj, long j) {
        return zzccx.zzo(obj, j);
    }

    static void zza(Object obj, long j, double d) {
        zzccx.zza(obj, j, d);
    }

    static Object zzp(Object obj, long j) {
        return zzccx.zzcdo.getObject(obj, j);
    }

    static void zza(Object obj, long j, Object obj2) {
        zzccx.zzcdo.putObject(obj, j, obj2);
    }

    static byte zza(byte[] bArr, long j) {
        return zzccx.zzy(bArr, zzccz + j);
    }

    static void zza(byte[] bArr, long j, byte b) {
        zzccx.zze(bArr, zzccz + j, b);
    }

    static void zza(byte[] bArr, long j, long j2, long j3) {
        zzccx.zza(bArr, j, j2, j3);
    }

    static void zza(long j, byte b) {
        zzccx.zza(j, b);
    }

    static long zzb(ByteBuffer byteBuffer) {
        return zzccx.zzl(byteBuffer, zzcdm);
    }

    static Unsafe zzyk() {
        try {
            return (Unsafe) AccessController.doPrivileged(new zzyi());
        } catch (Throwable th) {
            return null;
        }
    }

    private static boolean zzyl() {
        if (zzcay == null) {
            return false;
        }
        try {
            Class cls = zzcay.getClass();
            cls.getMethod("objectFieldOffset", new Class[]{Field.class});
            cls.getMethod("arrayBaseOffset", new Class[]{Class.class});
            cls.getMethod("arrayIndexScale", new Class[]{Class.class});
            cls.getMethod("getInt", new Class[]{Object.class, Long.TYPE});
            cls.getMethod("putInt", new Class[]{Object.class, Long.TYPE, Integer.TYPE});
            cls.getMethod("getLong", new Class[]{Object.class, Long.TYPE});
            cls.getMethod("putLong", new Class[]{Object.class, Long.TYPE, Long.TYPE});
            cls.getMethod("getObject", new Class[]{Object.class, Long.TYPE});
            cls.getMethod("putObject", new Class[]{Object.class, Long.TYPE, Object.class});
            if (zzua.zzty()) {
                return true;
            }
            cls.getMethod("getByte", new Class[]{Object.class, Long.TYPE});
            cls.getMethod("putByte", new Class[]{Object.class, Long.TYPE, Byte.TYPE});
            cls.getMethod("getBoolean", new Class[]{Object.class, Long.TYPE});
            cls.getMethod("putBoolean", new Class[]{Object.class, Long.TYPE, Boolean.TYPE});
            cls.getMethod("getFloat", new Class[]{Object.class, Long.TYPE});
            cls.getMethod("putFloat", new Class[]{Object.class, Long.TYPE, Float.TYPE});
            cls.getMethod("getDouble", new Class[]{Object.class, Long.TYPE});
            cls.getMethod("putDouble", new Class[]{Object.class, Long.TYPE, Double.TYPE});
            return true;
        } catch (Throwable th) {
            String valueOf = String.valueOf(th);
            logger.logp(Level.WARNING, "com.google.protobuf.UnsafeUtil", "supportsUnsafeArrayOperations", new StringBuilder(String.valueOf(valueOf).length() + 71).append("platform method missing - proto runtime falling back to safer methods: ").append(valueOf).toString());
            return false;
        }
    }

    private static boolean zzym() {
        if (zzcay == null) {
            return false;
        }
        try {
            Class cls = zzcay.getClass();
            cls.getMethod("objectFieldOffset", new Class[]{Field.class});
            cls.getMethod("getLong", new Class[]{Object.class, Long.TYPE});
            if (zzyn() == null) {
                return false;
            }
            if (zzua.zzty()) {
                return true;
            }
            cls.getMethod("getByte", new Class[]{Long.TYPE});
            cls.getMethod("putByte", new Class[]{Long.TYPE, Byte.TYPE});
            cls.getMethod("getInt", new Class[]{Long.TYPE});
            cls.getMethod("putInt", new Class[]{Long.TYPE, Integer.TYPE});
            cls.getMethod("getLong", new Class[]{Long.TYPE});
            cls.getMethod("putLong", new Class[]{Long.TYPE, Long.TYPE});
            cls.getMethod("copyMemory", new Class[]{Long.TYPE, Long.TYPE, Long.TYPE});
            cls.getMethod("copyMemory", new Class[]{Object.class, Long.TYPE, Object.class, Long.TYPE, Long.TYPE});
            return true;
        } catch (Throwable th) {
            String valueOf = String.valueOf(th);
            logger.logp(Level.WARNING, "com.google.protobuf.UnsafeUtil", "supportsUnsafeByteBufferOperations", new StringBuilder(String.valueOf(valueOf).length() + 71).append("platform method missing - proto runtime falling back to safer methods: ").append(valueOf).toString());
            return false;
        }
    }

    private static boolean zzm(Class<?> cls) {
        if (!zzua.zzty()) {
            return false;
        }
        try {
            Class cls2 = zzbtv;
            cls2.getMethod("peekLong", new Class[]{cls, Boolean.TYPE});
            cls2.getMethod("pokeLong", new Class[]{cls, Long.TYPE, Boolean.TYPE});
            cls2.getMethod("pokeInt", new Class[]{cls, Integer.TYPE, Boolean.TYPE});
            cls2.getMethod("peekInt", new Class[]{cls, Boolean.TYPE});
            cls2.getMethod("pokeByte", new Class[]{cls, Byte.TYPE});
            cls2.getMethod("peekByte", new Class[]{cls});
            cls2.getMethod("pokeByteArray", new Class[]{cls, byte[].class, Integer.TYPE, Integer.TYPE});
            cls2.getMethod("peekByteArray", new Class[]{cls, byte[].class, Integer.TYPE, Integer.TYPE});
            return true;
        } catch (Throwable th) {
            return false;
        }
    }

    private static Field zzyn() {
        Field zzb;
        if (zzua.zzty()) {
            zzb = zzb(Buffer.class, "effectiveDirectAddress");
            if (zzb != null) {
                return zzb;
            }
        }
        zzb = zzb(Buffer.class, "address");
        return (zzb == null || zzb.getType() != Long.TYPE) ? null : zzb;
    }

    private static Field zzb(Class<?> cls, String str) {
        try {
            Field declaredField = cls.getDeclaredField(str);
            declaredField.setAccessible(true);
            return declaredField;
        } catch (Throwable th) {
            return null;
        }
    }

    private static byte zzq(Object obj, long j) {
        return (byte) (zzk(obj, -4 & j) >>> ((int) (((-1 ^ j) & 3) << 3)));
    }

    private static byte zzr(Object obj, long j) {
        return (byte) (zzk(obj, -4 & j) >>> ((int) ((3 & j) << 3)));
    }

    private static void zza(Object obj, long j, byte b) {
        int i = ((((int) j) ^ -1) & 3) << 3;
        zzb(obj, j & -4, (zzk(obj, j & -4) & ((255 << i) ^ -1)) | ((b & 255) << i));
    }

    private static void zzb(Object obj, long j, byte b) {
        int i = (((int) j) & 3) << 3;
        zzb(obj, j & -4, (zzk(obj, j & -4) & ((255 << i) ^ -1)) | ((b & 255) << i));
    }

    private static boolean zzs(Object obj, long j) {
        return zzq(obj, j) != (byte) 0;
    }

    private static boolean zzt(Object obj, long j) {
        return zzr(obj, j) != (byte) 0;
    }

    private static void zzb(Object obj, long j, boolean z) {
        zza(obj, j, (byte) (z ? 1 : 0));
    }

    private static void zzc(Object obj, long j, boolean z) {
        zzb(obj, j, (byte) (z ? 1 : 0));
    }

    static {
        long j;
        boolean z;
        zzd com_google_android_gms_internal_measurement_zzyh_zzd = null;
        if (zzcay != null) {
            if (!zzua.zzty()) {
                com_google_android_gms_internal_measurement_zzyh_zzd = new zzc(zzcay);
            } else if (zzccv) {
                com_google_android_gms_internal_measurement_zzyh_zzd = new zzb(zzcay);
            } else if (zzccw) {
                com_google_android_gms_internal_measurement_zzyh_zzd = new zza(zzcay);
            }
        }
        zzccx = com_google_android_gms_internal_measurement_zzyh_zzd;
        Field zzyn = zzyn();
        if (zzyn == null || zzccx == null) {
            j = -1;
        } else {
            j = zzccx.zzcdo.objectFieldOffset(zzyn);
        }
        zzcdm = j;
        if (ByteOrder.nativeOrder() == ByteOrder.BIG_ENDIAN) {
            z = true;
        } else {
            z = false;
        }
        zzcdn = z;
    }
}
