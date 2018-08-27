package com.google.android.gms.internal.measurement;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class zzut extends zzuc {
    private static final Logger logger = Logger.getLogger(zzut.class.getName());
    private static final boolean zzbuv = zzyh.zzyi();
    zzuv zzbuw = this;

    public static class zzc extends IOException {
        zzc() {
            super("CodedOutputStream was writing to a flat byte array and ran out of space.");
        }

        zzc(String str) {
            String valueOf = String.valueOf("CodedOutputStream was writing to a flat byte array and ran out of space.: ");
            String valueOf2 = String.valueOf(str);
            super(valueOf2.length() != 0 ? valueOf.concat(valueOf2) : new String(valueOf));
        }

        zzc(Throwable th) {
            super("CodedOutputStream was writing to a flat byte array and ran out of space.", th);
        }

        zzc(String str, Throwable th) {
            String valueOf = String.valueOf("CodedOutputStream was writing to a flat byte array and ran out of space.: ");
            String valueOf2 = String.valueOf(str);
            super(valueOf2.length() != 0 ? valueOf.concat(valueOf2) : new String(valueOf), th);
        }
    }

    static class zza extends zzut {
        private final byte[] buffer;
        private final int limit;
        private final int offset;
        private int position;

        zza(byte[] bArr, int i, int i2) {
            super();
            if (bArr == null) {
                throw new NullPointerException("buffer");
            } else if (((i | i2) | (bArr.length - (i + i2))) < 0) {
                throw new IllegalArgumentException(String.format("Array range is invalid. Buffer.length=%d, offset=%d, length=%d", new Object[]{Integer.valueOf(bArr.length), Integer.valueOf(i), Integer.valueOf(i2)}));
            } else {
                this.buffer = bArr;
                this.offset = i;
                this.position = i;
                this.limit = i + i2;
            }
        }

        public final void zzc(int i, int i2) throws IOException {
            zzay((i << 3) | i2);
        }

        public final void zzd(int i, int i2) throws IOException {
            zzc(i, 0);
            zzax(i2);
        }

        public final void zze(int i, int i2) throws IOException {
            zzc(i, 0);
            zzay(i2);
        }

        public final void zzg(int i, int i2) throws IOException {
            zzc(i, 5);
            zzba(i2);
        }

        public final void zza(int i, long j) throws IOException {
            zzc(i, 0);
            zzav(j);
        }

        public final void zzc(int i, long j) throws IOException {
            zzc(i, 1);
            zzax(j);
        }

        public final void zzb(int i, boolean z) throws IOException {
            int i2 = 0;
            zzc(i, 0);
            if (z) {
                i2 = 1;
            }
            zzc((byte) i2);
        }

        public final void zzb(int i, String str) throws IOException {
            zzc(i, 2);
            zzfw(str);
        }

        public final void zza(int i, zzud com_google_android_gms_internal_measurement_zzud) throws IOException {
            zzc(i, 2);
            zza(com_google_android_gms_internal_measurement_zzud);
        }

        public final void zza(zzud com_google_android_gms_internal_measurement_zzud) throws IOException {
            zzay(com_google_android_gms_internal_measurement_zzud.size());
            com_google_android_gms_internal_measurement_zzud.zza((zzuc) this);
        }

        public final void zze(byte[] bArr, int i, int i2) throws IOException {
            zzay(i2);
            write(bArr, 0, i2);
        }

        public final void zza(int i, zzwt com_google_android_gms_internal_measurement_zzwt) throws IOException {
            zzc(i, 2);
            zzb(com_google_android_gms_internal_measurement_zzwt);
        }

        final void zza(int i, zzwt com_google_android_gms_internal_measurement_zzwt, zzxj com_google_android_gms_internal_measurement_zzxj) throws IOException {
            zzc(i, 2);
            zztw com_google_android_gms_internal_measurement_zztw = (zztw) com_google_android_gms_internal_measurement_zzwt;
            int zztu = com_google_android_gms_internal_measurement_zztw.zztu();
            if (zztu == -1) {
                zztu = com_google_android_gms_internal_measurement_zzxj.zzae(com_google_android_gms_internal_measurement_zztw);
                com_google_android_gms_internal_measurement_zztw.zzah(zztu);
            }
            zzay(zztu);
            com_google_android_gms_internal_measurement_zzxj.zza(com_google_android_gms_internal_measurement_zzwt, this.zzbuw);
        }

        public final void zzb(int i, zzwt com_google_android_gms_internal_measurement_zzwt) throws IOException {
            zzc(1, 3);
            zze(2, i);
            zza(3, com_google_android_gms_internal_measurement_zzwt);
            zzc(1, 4);
        }

        public final void zzb(int i, zzud com_google_android_gms_internal_measurement_zzud) throws IOException {
            zzc(1, 3);
            zze(2, i);
            zza(3, com_google_android_gms_internal_measurement_zzud);
            zzc(1, 4);
        }

        public final void zzb(zzwt com_google_android_gms_internal_measurement_zzwt) throws IOException {
            zzay(com_google_android_gms_internal_measurement_zzwt.zzvu());
            com_google_android_gms_internal_measurement_zzwt.zzb(this);
        }

        final void zza(zzwt com_google_android_gms_internal_measurement_zzwt, zzxj com_google_android_gms_internal_measurement_zzxj) throws IOException {
            zztw com_google_android_gms_internal_measurement_zztw = (zztw) com_google_android_gms_internal_measurement_zzwt;
            int zztu = com_google_android_gms_internal_measurement_zztw.zztu();
            if (zztu == -1) {
                zztu = com_google_android_gms_internal_measurement_zzxj.zzae(com_google_android_gms_internal_measurement_zztw);
                com_google_android_gms_internal_measurement_zztw.zzah(zztu);
            }
            zzay(zztu);
            com_google_android_gms_internal_measurement_zzxj.zza(com_google_android_gms_internal_measurement_zzwt, this.zzbuw);
        }

        public final void zzc(byte b) throws IOException {
            try {
                byte[] bArr = this.buffer;
                int i = this.position;
                this.position = i + 1;
                bArr[i] = b;
            } catch (Throwable e) {
                throw new zzc(String.format("Pos: %d, limit: %d, len: %d", new Object[]{Integer.valueOf(this.position), Integer.valueOf(this.limit), Integer.valueOf(1)}), e);
            }
        }

        public final void zzax(int i) throws IOException {
            if (i >= 0) {
                zzay(i);
            } else {
                zzav((long) i);
            }
        }

        public final void zzay(int i) throws IOException {
            byte[] bArr;
            int i2;
            if (!zzut.zzbuv || zzvg() < 10) {
                while ((i & -128) != 0) {
                    try {
                        bArr = this.buffer;
                        i2 = this.position;
                        this.position = i2 + 1;
                        bArr[i2] = (byte) ((i & 127) | 128);
                        i >>>= 7;
                    } catch (Throwable e) {
                        throw new zzc(String.format("Pos: %d, limit: %d, len: %d", new Object[]{Integer.valueOf(this.position), Integer.valueOf(this.limit), Integer.valueOf(1)}), e);
                    }
                }
                bArr = this.buffer;
                i2 = this.position;
                this.position = i2 + 1;
                bArr[i2] = (byte) i;
                return;
            }
            while ((i & -128) != 0) {
                bArr = this.buffer;
                i2 = this.position;
                this.position = i2 + 1;
                zzyh.zza(bArr, (long) i2, (byte) ((i & 127) | 128));
                i >>>= 7;
            }
            bArr = this.buffer;
            i2 = this.position;
            this.position = i2 + 1;
            zzyh.zza(bArr, (long) i2, (byte) i);
        }

        public final void zzba(int i) throws IOException {
            try {
                byte[] bArr = this.buffer;
                int i2 = this.position;
                this.position = i2 + 1;
                bArr[i2] = (byte) i;
                bArr = this.buffer;
                i2 = this.position;
                this.position = i2 + 1;
                bArr[i2] = (byte) (i >> 8);
                bArr = this.buffer;
                i2 = this.position;
                this.position = i2 + 1;
                bArr[i2] = (byte) (i >> 16);
                bArr = this.buffer;
                i2 = this.position;
                this.position = i2 + 1;
                bArr[i2] = i >> 24;
            } catch (Throwable e) {
                throw new zzc(String.format("Pos: %d, limit: %d, len: %d", new Object[]{Integer.valueOf(this.position), Integer.valueOf(this.limit), Integer.valueOf(1)}), e);
            }
        }

        public final void zzav(long j) throws IOException {
            byte[] bArr;
            int i;
            if (!zzut.zzbuv || zzvg() < 10) {
                while ((j & -128) != 0) {
                    try {
                        bArr = this.buffer;
                        i = this.position;
                        this.position = i + 1;
                        bArr[i] = (byte) ((((int) j) & 127) | 128);
                        j >>>= 7;
                    } catch (Throwable e) {
                        throw new zzc(String.format("Pos: %d, limit: %d, len: %d", new Object[]{Integer.valueOf(this.position), Integer.valueOf(this.limit), Integer.valueOf(1)}), e);
                    }
                }
                bArr = this.buffer;
                i = this.position;
                this.position = i + 1;
                bArr[i] = (byte) ((int) j);
                return;
            }
            while ((j & -128) != 0) {
                bArr = this.buffer;
                i = this.position;
                this.position = i + 1;
                zzyh.zza(bArr, (long) i, (byte) ((((int) j) & 127) | 128));
                j >>>= 7;
            }
            bArr = this.buffer;
            i = this.position;
            this.position = i + 1;
            zzyh.zza(bArr, (long) i, (byte) ((int) j));
        }

        public final void zzax(long j) throws IOException {
            try {
                byte[] bArr = this.buffer;
                int i = this.position;
                this.position = i + 1;
                bArr[i] = (byte) ((int) j);
                bArr = this.buffer;
                i = this.position;
                this.position = i + 1;
                bArr[i] = (byte) ((int) (j >> 8));
                bArr = this.buffer;
                i = this.position;
                this.position = i + 1;
                bArr[i] = (byte) ((int) (j >> 16));
                bArr = this.buffer;
                i = this.position;
                this.position = i + 1;
                bArr[i] = (byte) ((int) (j >> 24));
                bArr = this.buffer;
                i = this.position;
                this.position = i + 1;
                bArr[i] = (byte) ((int) (j >> 32));
                bArr = this.buffer;
                i = this.position;
                this.position = i + 1;
                bArr[i] = (byte) ((int) (j >> 40));
                bArr = this.buffer;
                i = this.position;
                this.position = i + 1;
                bArr[i] = (byte) ((int) (j >> 48));
                bArr = this.buffer;
                i = this.position;
                this.position = i + 1;
                bArr[i] = (byte) ((int) (j >> 56));
            } catch (Throwable e) {
                throw new zzc(String.format("Pos: %d, limit: %d, len: %d", new Object[]{Integer.valueOf(this.position), Integer.valueOf(this.limit), Integer.valueOf(1)}), e);
            }
        }

        public final void write(byte[] bArr, int i, int i2) throws IOException {
            try {
                System.arraycopy(bArr, i, this.buffer, this.position, i2);
                this.position += i2;
            } catch (Throwable e) {
                throw new zzc(String.format("Pos: %d, limit: %d, len: %d", new Object[]{Integer.valueOf(this.position), Integer.valueOf(this.limit), Integer.valueOf(i2)}), e);
            }
        }

        public final void zza(byte[] bArr, int i, int i2) throws IOException {
            write(bArr, i, i2);
        }

        public final void zzfw(String str) throws IOException {
            int i = this.position;
            try {
                int zzbd = zzut.zzbd(str.length() * 3);
                int zzbd2 = zzut.zzbd(str.length());
                if (zzbd2 == zzbd) {
                    this.position = i + zzbd2;
                    zzbd = zzyj.zza(str, this.buffer, this.position, zzvg());
                    this.position = i;
                    zzay((zzbd - i) - zzbd2);
                    this.position = zzbd;
                    return;
                }
                zzay(zzyj.zza(str));
                this.position = zzyj.zza(str, this.buffer, this.position, zzvg());
            } catch (zzyn e) {
                this.position = i;
                zza(str, e);
            } catch (Throwable e2) {
                throw new zzc(e2);
            }
        }

        public void flush() {
        }

        public final int zzvg() {
            return this.limit - this.position;
        }

        public final int zzvi() {
            return this.position - this.offset;
        }
    }

    static final class zzd extends zzut {
        private final int zzbuy;
        private final ByteBuffer zzbuz;
        private final ByteBuffer zzbva;

        zzd(ByteBuffer byteBuffer) {
            super();
            this.zzbuz = byteBuffer;
            this.zzbva = byteBuffer.duplicate().order(ByteOrder.LITTLE_ENDIAN);
            this.zzbuy = byteBuffer.position();
        }

        public final void zzc(int i, int i2) throws IOException {
            zzay((i << 3) | i2);
        }

        public final void zzd(int i, int i2) throws IOException {
            zzc(i, 0);
            zzax(i2);
        }

        public final void zze(int i, int i2) throws IOException {
            zzc(i, 0);
            zzay(i2);
        }

        public final void zzg(int i, int i2) throws IOException {
            zzc(i, 5);
            zzba(i2);
        }

        public final void zza(int i, long j) throws IOException {
            zzc(i, 0);
            zzav(j);
        }

        public final void zzc(int i, long j) throws IOException {
            zzc(i, 1);
            zzax(j);
        }

        public final void zzb(int i, boolean z) throws IOException {
            int i2 = 0;
            zzc(i, 0);
            if (z) {
                i2 = 1;
            }
            zzc((byte) i2);
        }

        public final void zzb(int i, String str) throws IOException {
            zzc(i, 2);
            zzfw(str);
        }

        public final void zza(int i, zzud com_google_android_gms_internal_measurement_zzud) throws IOException {
            zzc(i, 2);
            zza(com_google_android_gms_internal_measurement_zzud);
        }

        public final void zza(int i, zzwt com_google_android_gms_internal_measurement_zzwt) throws IOException {
            zzc(i, 2);
            zzb(com_google_android_gms_internal_measurement_zzwt);
        }

        final void zza(int i, zzwt com_google_android_gms_internal_measurement_zzwt, zzxj com_google_android_gms_internal_measurement_zzxj) throws IOException {
            zzc(i, 2);
            zza(com_google_android_gms_internal_measurement_zzwt, com_google_android_gms_internal_measurement_zzxj);
        }

        public final void zzb(int i, zzwt com_google_android_gms_internal_measurement_zzwt) throws IOException {
            zzc(1, 3);
            zze(2, i);
            zza(3, com_google_android_gms_internal_measurement_zzwt);
            zzc(1, 4);
        }

        public final void zzb(int i, zzud com_google_android_gms_internal_measurement_zzud) throws IOException {
            zzc(1, 3);
            zze(2, i);
            zza(3, com_google_android_gms_internal_measurement_zzud);
            zzc(1, 4);
        }

        public final void zzb(zzwt com_google_android_gms_internal_measurement_zzwt) throws IOException {
            zzay(com_google_android_gms_internal_measurement_zzwt.zzvu());
            com_google_android_gms_internal_measurement_zzwt.zzb(this);
        }

        final void zza(zzwt com_google_android_gms_internal_measurement_zzwt, zzxj com_google_android_gms_internal_measurement_zzxj) throws IOException {
            zztw com_google_android_gms_internal_measurement_zztw = (zztw) com_google_android_gms_internal_measurement_zzwt;
            int zztu = com_google_android_gms_internal_measurement_zztw.zztu();
            if (zztu == -1) {
                zztu = com_google_android_gms_internal_measurement_zzxj.zzae(com_google_android_gms_internal_measurement_zztw);
                com_google_android_gms_internal_measurement_zztw.zzah(zztu);
            }
            zzay(zztu);
            com_google_android_gms_internal_measurement_zzxj.zza(com_google_android_gms_internal_measurement_zzwt, this.zzbuw);
        }

        public final void zzc(byte b) throws IOException {
            try {
                this.zzbva.put(b);
            } catch (Throwable e) {
                throw new zzc(e);
            }
        }

        public final void zza(zzud com_google_android_gms_internal_measurement_zzud) throws IOException {
            zzay(com_google_android_gms_internal_measurement_zzud.size());
            com_google_android_gms_internal_measurement_zzud.zza((zzuc) this);
        }

        public final void zze(byte[] bArr, int i, int i2) throws IOException {
            zzay(i2);
            write(bArr, 0, i2);
        }

        public final void zzax(int i) throws IOException {
            if (i >= 0) {
                zzay(i);
            } else {
                zzav((long) i);
            }
        }

        public final void zzay(int i) throws IOException {
            while ((i & -128) != 0) {
                this.zzbva.put((byte) ((i & 127) | 128));
                i >>>= 7;
            }
            try {
                this.zzbva.put((byte) i);
            } catch (Throwable e) {
                throw new zzc(e);
            }
        }

        public final void zzba(int i) throws IOException {
            try {
                this.zzbva.putInt(i);
            } catch (Throwable e) {
                throw new zzc(e);
            }
        }

        public final void zzav(long j) throws IOException {
            while ((-128 & j) != 0) {
                this.zzbva.put((byte) ((((int) j) & 127) | 128));
                j >>>= 7;
            }
            try {
                this.zzbva.put((byte) ((int) j));
            } catch (Throwable e) {
                throw new zzc(e);
            }
        }

        public final void zzax(long j) throws IOException {
            try {
                this.zzbva.putLong(j);
            } catch (Throwable e) {
                throw new zzc(e);
            }
        }

        public final void write(byte[] bArr, int i, int i2) throws IOException {
            try {
                this.zzbva.put(bArr, i, i2);
            } catch (Throwable e) {
                throw new zzc(e);
            } catch (Throwable e2) {
                throw new zzc(e2);
            }
        }

        public final void zza(byte[] bArr, int i, int i2) throws IOException {
            write(bArr, i, i2);
        }

        public final void zzfw(String str) throws IOException {
            int position = this.zzbva.position();
            try {
                int zzbd = zzut.zzbd(str.length() * 3);
                int zzbd2 = zzut.zzbd(str.length());
                if (zzbd2 == zzbd) {
                    zzbd = this.zzbva.position() + zzbd2;
                    this.zzbva.position(zzbd);
                    zzfy(str);
                    zzbd2 = this.zzbva.position();
                    this.zzbva.position(position);
                    zzay(zzbd2 - zzbd);
                    this.zzbva.position(zzbd2);
                    return;
                }
                zzay(zzyj.zza(str));
                zzfy(str);
            } catch (zzyn e) {
                this.zzbva.position(position);
                zza(str, e);
            } catch (Throwable e2) {
                throw new zzc(e2);
            }
        }

        public final void flush() {
            this.zzbuz.position(this.zzbva.position());
        }

        public final int zzvg() {
            return this.zzbva.remaining();
        }

        private final void zzfy(String str) throws IOException {
            try {
                zzyj.zza(str, this.zzbva);
            } catch (Throwable e) {
                throw new zzc(e);
            }
        }
    }

    static final class zze extends zzut {
        private final ByteBuffer zzbuz;
        private final ByteBuffer zzbva;
        private final long zzbvb;
        private final long zzbvc;
        private final long zzbvd;
        private final long zzbve = (this.zzbvd - 10);
        private long zzbvf = this.zzbvc;

        zze(ByteBuffer byteBuffer) {
            super();
            this.zzbuz = byteBuffer;
            this.zzbva = byteBuffer.duplicate().order(ByteOrder.LITTLE_ENDIAN);
            this.zzbvb = zzyh.zzb(byteBuffer);
            this.zzbvc = this.zzbvb + ((long) byteBuffer.position());
            this.zzbvd = this.zzbvb + ((long) byteBuffer.limit());
        }

        public final void zzc(int i, int i2) throws IOException {
            zzay((i << 3) | i2);
        }

        public final void zzd(int i, int i2) throws IOException {
            zzc(i, 0);
            zzax(i2);
        }

        public final void zze(int i, int i2) throws IOException {
            zzc(i, 0);
            zzay(i2);
        }

        public final void zzg(int i, int i2) throws IOException {
            zzc(i, 5);
            zzba(i2);
        }

        public final void zza(int i, long j) throws IOException {
            zzc(i, 0);
            zzav(j);
        }

        public final void zzc(int i, long j) throws IOException {
            zzc(i, 1);
            zzax(j);
        }

        public final void zzb(int i, boolean z) throws IOException {
            int i2 = 0;
            zzc(i, 0);
            if (z) {
                i2 = 1;
            }
            zzc((byte) i2);
        }

        public final void zzb(int i, String str) throws IOException {
            zzc(i, 2);
            zzfw(str);
        }

        public final void zza(int i, zzud com_google_android_gms_internal_measurement_zzud) throws IOException {
            zzc(i, 2);
            zza(com_google_android_gms_internal_measurement_zzud);
        }

        public final void zza(int i, zzwt com_google_android_gms_internal_measurement_zzwt) throws IOException {
            zzc(i, 2);
            zzb(com_google_android_gms_internal_measurement_zzwt);
        }

        final void zza(int i, zzwt com_google_android_gms_internal_measurement_zzwt, zzxj com_google_android_gms_internal_measurement_zzxj) throws IOException {
            zzc(i, 2);
            zza(com_google_android_gms_internal_measurement_zzwt, com_google_android_gms_internal_measurement_zzxj);
        }

        public final void zzb(int i, zzwt com_google_android_gms_internal_measurement_zzwt) throws IOException {
            zzc(1, 3);
            zze(2, i);
            zza(3, com_google_android_gms_internal_measurement_zzwt);
            zzc(1, 4);
        }

        public final void zzb(int i, zzud com_google_android_gms_internal_measurement_zzud) throws IOException {
            zzc(1, 3);
            zze(2, i);
            zza(3, com_google_android_gms_internal_measurement_zzud);
            zzc(1, 4);
        }

        public final void zzb(zzwt com_google_android_gms_internal_measurement_zzwt) throws IOException {
            zzay(com_google_android_gms_internal_measurement_zzwt.zzvu());
            com_google_android_gms_internal_measurement_zzwt.zzb(this);
        }

        final void zza(zzwt com_google_android_gms_internal_measurement_zzwt, zzxj com_google_android_gms_internal_measurement_zzxj) throws IOException {
            zztw com_google_android_gms_internal_measurement_zztw = (zztw) com_google_android_gms_internal_measurement_zzwt;
            int zztu = com_google_android_gms_internal_measurement_zztw.zztu();
            if (zztu == -1) {
                zztu = com_google_android_gms_internal_measurement_zzxj.zzae(com_google_android_gms_internal_measurement_zztw);
                com_google_android_gms_internal_measurement_zztw.zzah(zztu);
            }
            zzay(zztu);
            com_google_android_gms_internal_measurement_zzxj.zza(com_google_android_gms_internal_measurement_zzwt, this.zzbuw);
        }

        public final void zzc(byte b) throws IOException {
            if (this.zzbvf >= this.zzbvd) {
                throw new zzc(String.format("Pos: %d, limit: %d, len: %d", new Object[]{Long.valueOf(this.zzbvf), Long.valueOf(this.zzbvd), Integer.valueOf(1)}));
            }
            long j = this.zzbvf;
            this.zzbvf = 1 + j;
            zzyh.zza(j, b);
        }

        public final void zza(zzud com_google_android_gms_internal_measurement_zzud) throws IOException {
            zzay(com_google_android_gms_internal_measurement_zzud.size());
            com_google_android_gms_internal_measurement_zzud.zza((zzuc) this);
        }

        public final void zze(byte[] bArr, int i, int i2) throws IOException {
            zzay(i2);
            write(bArr, 0, i2);
        }

        public final void zzax(int i) throws IOException {
            if (i >= 0) {
                zzay(i);
            } else {
                zzav((long) i);
            }
        }

        public final void zzay(int i) throws IOException {
            long j;
            if (this.zzbvf <= this.zzbve) {
                while ((i & -128) != 0) {
                    j = this.zzbvf;
                    this.zzbvf = j + 1;
                    zzyh.zza(j, (byte) ((i & 127) | 128));
                    i >>>= 7;
                }
                j = this.zzbvf;
                this.zzbvf = j + 1;
                zzyh.zza(j, (byte) i);
                return;
            }
            while (this.zzbvf < this.zzbvd) {
                if ((i & -128) == 0) {
                    j = this.zzbvf;
                    this.zzbvf = j + 1;
                    zzyh.zza(j, (byte) i);
                    return;
                }
                j = this.zzbvf;
                this.zzbvf = j + 1;
                zzyh.zza(j, (byte) ((i & 127) | 128));
                i >>>= 7;
            }
            throw new zzc(String.format("Pos: %d, limit: %d, len: %d", new Object[]{Long.valueOf(this.zzbvf), Long.valueOf(this.zzbvd), Integer.valueOf(1)}));
        }

        public final void zzba(int i) throws IOException {
            this.zzbva.putInt((int) (this.zzbvf - this.zzbvb), i);
            this.zzbvf += 4;
        }

        public final void zzav(long j) throws IOException {
            long j2;
            if (this.zzbvf <= this.zzbve) {
                while ((j & -128) != 0) {
                    j2 = this.zzbvf;
                    this.zzbvf = j2 + 1;
                    zzyh.zza(j2, (byte) ((((int) j) & 127) | 128));
                    j >>>= 7;
                }
                j2 = this.zzbvf;
                this.zzbvf = j2 + 1;
                zzyh.zza(j2, (byte) ((int) j));
                return;
            }
            while (this.zzbvf < this.zzbvd) {
                if ((j & -128) == 0) {
                    j2 = this.zzbvf;
                    this.zzbvf = j2 + 1;
                    zzyh.zza(j2, (byte) ((int) j));
                    return;
                }
                j2 = this.zzbvf;
                this.zzbvf = j2 + 1;
                zzyh.zza(j2, (byte) ((((int) j) & 127) | 128));
                j >>>= 7;
            }
            throw new zzc(String.format("Pos: %d, limit: %d, len: %d", new Object[]{Long.valueOf(this.zzbvf), Long.valueOf(this.zzbvd), Integer.valueOf(1)}));
        }

        public final void zzax(long j) throws IOException {
            this.zzbva.putLong((int) (this.zzbvf - this.zzbvb), j);
            this.zzbvf += 8;
        }

        public final void write(byte[] bArr, int i, int i2) throws IOException {
            if (bArr != null && i >= 0 && i2 >= 0 && bArr.length - i2 >= i && this.zzbvd - ((long) i2) >= this.zzbvf) {
                zzyh.zza(bArr, (long) i, this.zzbvf, (long) i2);
                this.zzbvf += (long) i2;
            } else if (bArr == null) {
                throw new NullPointerException("value");
            } else {
                throw new zzc(String.format("Pos: %d, limit: %d, len: %d", new Object[]{Long.valueOf(this.zzbvf), Long.valueOf(this.zzbvd), Integer.valueOf(i2)}));
            }
        }

        public final void zza(byte[] bArr, int i, int i2) throws IOException {
            write(bArr, i, i2);
        }

        public final void zzfw(String str) throws IOException {
            long j = this.zzbvf;
            try {
                int zzbd = zzut.zzbd(str.length() * 3);
                int zzbd2 = zzut.zzbd(str.length());
                if (zzbd2 == zzbd) {
                    zzbd = ((int) (this.zzbvf - this.zzbvb)) + zzbd2;
                    this.zzbva.position(zzbd);
                    zzyj.zza(str, this.zzbva);
                    zzbd = this.zzbva.position() - zzbd;
                    zzay(zzbd);
                    this.zzbvf = ((long) zzbd) + this.zzbvf;
                    return;
                }
                zzbd = zzyj.zza(str);
                zzay(zzbd);
                zzbe(this.zzbvf);
                zzyj.zza(str, this.zzbva);
                this.zzbvf = ((long) zzbd) + this.zzbvf;
            } catch (zzyn e) {
                this.zzbvf = j;
                zzbe(this.zzbvf);
                zza(str, e);
            } catch (Throwable e2) {
                throw new zzc(e2);
            } catch (Throwable e22) {
                throw new zzc(e22);
            }
        }

        public final void flush() {
            this.zzbuz.position((int) (this.zzbvf - this.zzbvb));
        }

        public final int zzvg() {
            return (int) (this.zzbvd - this.zzbvf);
        }

        private final void zzbe(long j) {
            this.zzbva.position((int) (j - this.zzbvb));
        }
    }

    static final class zzb extends zza {
        private final ByteBuffer zzbux;
        private int zzbuy;

        zzb(ByteBuffer byteBuffer) {
            super(byteBuffer.array(), byteBuffer.arrayOffset() + byteBuffer.position(), byteBuffer.remaining());
            this.zzbux = byteBuffer;
            this.zzbuy = byteBuffer.position();
        }

        public final void flush() {
            this.zzbux.position(this.zzbuy + zzvi());
        }
    }

    public static zzut zzj(byte[] bArr) {
        return new zza(bArr, 0, bArr.length);
    }

    public abstract void flush() throws IOException;

    public abstract void write(byte[] bArr, int i, int i2) throws IOException;

    public abstract void zza(int i, long j) throws IOException;

    public abstract void zza(int i, zzud com_google_android_gms_internal_measurement_zzud) throws IOException;

    public abstract void zza(int i, zzwt com_google_android_gms_internal_measurement_zzwt) throws IOException;

    abstract void zza(int i, zzwt com_google_android_gms_internal_measurement_zzwt, zzxj com_google_android_gms_internal_measurement_zzxj) throws IOException;

    public abstract void zza(zzud com_google_android_gms_internal_measurement_zzud) throws IOException;

    abstract void zza(zzwt com_google_android_gms_internal_measurement_zzwt, zzxj com_google_android_gms_internal_measurement_zzxj) throws IOException;

    public abstract void zzav(long j) throws IOException;

    public abstract void zzax(int i) throws IOException;

    public abstract void zzax(long j) throws IOException;

    public abstract void zzay(int i) throws IOException;

    public abstract void zzb(int i, zzud com_google_android_gms_internal_measurement_zzud) throws IOException;

    public abstract void zzb(int i, zzwt com_google_android_gms_internal_measurement_zzwt) throws IOException;

    public abstract void zzb(int i, String str) throws IOException;

    public abstract void zzb(int i, boolean z) throws IOException;

    public abstract void zzb(zzwt com_google_android_gms_internal_measurement_zzwt) throws IOException;

    public abstract void zzba(int i) throws IOException;

    public abstract void zzc(byte b) throws IOException;

    public abstract void zzc(int i, int i2) throws IOException;

    public abstract void zzc(int i, long j) throws IOException;

    public abstract void zzd(int i, int i2) throws IOException;

    public abstract void zze(int i, int i2) throws IOException;

    abstract void zze(byte[] bArr, int i, int i2) throws IOException;

    public abstract void zzfw(String str) throws IOException;

    public abstract void zzg(int i, int i2) throws IOException;

    public abstract int zzvg();

    public static zzut zza(ByteBuffer byteBuffer) {
        if (byteBuffer.hasArray()) {
            return new zzb(byteBuffer);
        }
        if (!byteBuffer.isDirect() || byteBuffer.isReadOnly()) {
            throw new IllegalArgumentException("ByteBuffer is read-only");
        } else if (zzyh.zzyj()) {
            return new zze(byteBuffer);
        } else {
            return new zzd(byteBuffer);
        }
    }

    private zzut() {
    }

    public final void zzf(int i, int i2) throws IOException {
        zze(i, zzbi(i2));
    }

    public final void zzb(int i, long j) throws IOException {
        zza(i, zzbd(j));
    }

    public final void zza(int i, float f) throws IOException {
        zzg(i, Float.floatToRawIntBits(f));
    }

    public final void zza(int i, double d) throws IOException {
        zzc(i, Double.doubleToRawLongBits(d));
    }

    public final void zzaz(int i) throws IOException {
        zzay(zzbi(i));
    }

    public final void zzaw(long j) throws IOException {
        zzav(zzbd(j));
    }

    public final void zza(float f) throws IOException {
        zzba(Float.floatToRawIntBits(f));
    }

    public final void zzb(double d) throws IOException {
        zzax(Double.doubleToRawLongBits(d));
    }

    public final void zzu(boolean z) throws IOException {
        zzc((byte) (z ? 1 : 0));
    }

    public static int zzh(int i, int i2) {
        return zzbb(i) + zzbc(i2);
    }

    public static int zzi(int i, int i2) {
        return zzbb(i) + zzbd(i2);
    }

    public static int zzj(int i, int i2) {
        return zzbb(i) + zzbd(zzbi(i2));
    }

    public static int zzk(int i, int i2) {
        return zzbb(i) + 4;
    }

    public static int zzl(int i, int i2) {
        return zzbb(i) + 4;
    }

    public static int zzd(int i, long j) {
        return zzbb(i) + zzaz(j);
    }

    public static int zze(int i, long j) {
        return zzbb(i) + zzaz(j);
    }

    public static int zzf(int i, long j) {
        return zzbb(i) + zzaz(zzbd(j));
    }

    public static int zzg(int i, long j) {
        return zzbb(i) + 8;
    }

    public static int zzh(int i, long j) {
        return zzbb(i) + 8;
    }

    public static int zzb(int i, float f) {
        return zzbb(i) + 4;
    }

    public static int zzb(int i, double d) {
        return zzbb(i) + 8;
    }

    public static int zzc(int i, boolean z) {
        return zzbb(i) + 1;
    }

    public static int zzm(int i, int i2) {
        return zzbb(i) + zzbc(i2);
    }

    public static int zzc(int i, String str) {
        return zzbb(i) + zzfx(str);
    }

    public static int zzc(int i, zzud com_google_android_gms_internal_measurement_zzud) {
        int zzbb = zzbb(i);
        int size = com_google_android_gms_internal_measurement_zzud.size();
        return zzbb + (size + zzbd(size));
    }

    public static int zza(int i, zzwa com_google_android_gms_internal_measurement_zzwa) {
        int zzbb = zzbb(i);
        int zzvu = com_google_android_gms_internal_measurement_zzwa.zzvu();
        return zzbb + (zzvu + zzbd(zzvu));
    }

    public static int zzc(int i, zzwt com_google_android_gms_internal_measurement_zzwt) {
        return zzbb(i) + zzc(com_google_android_gms_internal_measurement_zzwt);
    }

    static int zzb(int i, zzwt com_google_android_gms_internal_measurement_zzwt, zzxj com_google_android_gms_internal_measurement_zzxj) {
        return zzbb(i) + zzb(com_google_android_gms_internal_measurement_zzwt, com_google_android_gms_internal_measurement_zzxj);
    }

    public static int zzd(int i, zzwt com_google_android_gms_internal_measurement_zzwt) {
        return ((zzbb(1) << 1) + zzi(2, i)) + zzc(3, com_google_android_gms_internal_measurement_zzwt);
    }

    public static int zzd(int i, zzud com_google_android_gms_internal_measurement_zzud) {
        return ((zzbb(1) << 1) + zzi(2, i)) + zzc(3, com_google_android_gms_internal_measurement_zzud);
    }

    public static int zzb(int i, zzwa com_google_android_gms_internal_measurement_zzwa) {
        return ((zzbb(1) << 1) + zzi(2, i)) + zza(3, com_google_android_gms_internal_measurement_zzwa);
    }

    public static int zzbb(int i) {
        return zzbd(i << 3);
    }

    public static int zzbc(int i) {
        if (i >= 0) {
            return zzbd(i);
        }
        return 10;
    }

    public static int zzbd(int i) {
        if ((i & -128) == 0) {
            return 1;
        }
        if ((i & -16384) == 0) {
            return 2;
        }
        if ((-2097152 & i) == 0) {
            return 3;
        }
        if ((-268435456 & i) == 0) {
            return 4;
        }
        return 5;
    }

    public static int zzbe(int i) {
        return zzbd(zzbi(i));
    }

    public static int zzbf(int i) {
        return 4;
    }

    public static int zzbg(int i) {
        return 4;
    }

    public static int zzay(long j) {
        return zzaz(j);
    }

    public static int zzaz(long j) {
        if ((-128 & j) == 0) {
            return 1;
        }
        if (j < 0) {
            return 10;
        }
        long j2;
        int i = 2;
        if ((-34359738368L & j) != 0) {
            i = 6;
            j2 = j >>> 28;
        } else {
            j2 = j;
        }
        if ((-2097152 & j2) != 0) {
            i += 2;
            j2 >>>= 14;
        }
        if ((j2 & -16384) != 0) {
            return i + 1;
        }
        return i;
    }

    public static int zzba(long j) {
        return zzaz(zzbd(j));
    }

    public static int zzbb(long j) {
        return 8;
    }

    public static int zzbc(long j) {
        return 8;
    }

    public static int zzb(float f) {
        return 4;
    }

    public static int zzc(double d) {
        return 8;
    }

    public static int zzv(boolean z) {
        return 1;
    }

    public static int zzbh(int i) {
        return zzbc(i);
    }

    public static int zzfx(String str) {
        int zza;
        try {
            zza = zzyj.zza(str);
        } catch (zzyn e) {
            zza = str.getBytes(zzvo.UTF_8).length;
        }
        return zza + zzbd(zza);
    }

    public static int zza(zzwa com_google_android_gms_internal_measurement_zzwa) {
        int zzvu = com_google_android_gms_internal_measurement_zzwa.zzvu();
        return zzvu + zzbd(zzvu);
    }

    public static int zzb(zzud com_google_android_gms_internal_measurement_zzud) {
        int size = com_google_android_gms_internal_measurement_zzud.size();
        return size + zzbd(size);
    }

    public static int zzk(byte[] bArr) {
        int length = bArr.length;
        return length + zzbd(length);
    }

    public static int zzc(zzwt com_google_android_gms_internal_measurement_zzwt) {
        int zzvu = com_google_android_gms_internal_measurement_zzwt.zzvu();
        return zzvu + zzbd(zzvu);
    }

    static int zzb(zzwt com_google_android_gms_internal_measurement_zzwt, zzxj com_google_android_gms_internal_measurement_zzxj) {
        zztw com_google_android_gms_internal_measurement_zztw = (zztw) com_google_android_gms_internal_measurement_zzwt;
        int zztu = com_google_android_gms_internal_measurement_zztw.zztu();
        if (zztu == -1) {
            zztu = com_google_android_gms_internal_measurement_zzxj.zzae(com_google_android_gms_internal_measurement_zztw);
            com_google_android_gms_internal_measurement_zztw.zzah(zztu);
        }
        return zztu + zzbd(zztu);
    }

    private static int zzbi(int i) {
        return (i << 1) ^ (i >> 31);
    }

    private static long zzbd(long j) {
        return (j << 1) ^ (j >> 63);
    }

    final void zza(String str, zzyn com_google_android_gms_internal_measurement_zzyn) throws IOException {
        logger.logp(Level.WARNING, "com.google.protobuf.CodedOutputStream", "inefficientWriteStringNoTag", "Converting ill-formed UTF-16. Your Protocol Buffer will not round trip correctly!", com_google_android_gms_internal_measurement_zzyn);
        byte[] bytes = str.getBytes(zzvo.UTF_8);
        try {
            zzay(bytes.length);
            zza(bytes, 0, bytes.length);
        } catch (Throwable e) {
            throw new zzc(e);
        } catch (zzc e2) {
            throw e2;
        }
    }

    @Deprecated
    static int zzc(int i, zzwt com_google_android_gms_internal_measurement_zzwt, zzxj com_google_android_gms_internal_measurement_zzxj) {
        int zzbb = zzbb(i) << 1;
        zztw com_google_android_gms_internal_measurement_zztw = (zztw) com_google_android_gms_internal_measurement_zzwt;
        int zztu = com_google_android_gms_internal_measurement_zztw.zztu();
        if (zztu == -1) {
            zztu = com_google_android_gms_internal_measurement_zzxj.zzae(com_google_android_gms_internal_measurement_zztw);
            com_google_android_gms_internal_measurement_zztw.zzah(zztu);
        }
        return zztu + zzbb;
    }

    @Deprecated
    public static int zzd(zzwt com_google_android_gms_internal_measurement_zzwt) {
        return com_google_android_gms_internal_measurement_zzwt.zzvu();
    }

    @Deprecated
    public static int zzbj(int i) {
        return zzbd(i);
    }
}
