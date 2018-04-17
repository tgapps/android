package com.google.android.gms.wearable.internal;

import com.google.android.gms.common.internal.Preconditions;
import com.google.android.gms.wearable.ChannelIOException;
import java.io.IOException;
import java.io.InputStream;
import javax.annotation.Nullable;

public final class zzbj extends InputStream {
    private final InputStream zzcv;
    @Nullable
    private volatile zzav zzcw;

    public zzbj(InputStream inputStream) {
        this.zzcv = (InputStream) Preconditions.checkNotNull(inputStream);
    }

    private final int zza(int i) throws ChannelIOException {
        if (i == -1) {
            zzav com_google_android_gms_wearable_internal_zzav = this.zzcw;
            if (com_google_android_gms_wearable_internal_zzav != null) {
                throw new ChannelIOException("Channel closed unexpectedly before stream was finished", com_google_android_gms_wearable_internal_zzav.zzg, com_google_android_gms_wearable_internal_zzav.zzcj);
            }
        }
        return i;
    }

    public final int available() throws IOException {
        return this.zzcv.available();
    }

    public final void close() throws IOException {
        this.zzcv.close();
    }

    public final void mark(int i) {
        this.zzcv.mark(i);
    }

    public final boolean markSupported() {
        return this.zzcv.markSupported();
    }

    public final int read() throws IOException {
        return zza(this.zzcv.read());
    }

    public final int read(byte[] bArr) throws IOException {
        return zza(this.zzcv.read(bArr));
    }

    public final int read(byte[] bArr, int i, int i2) throws IOException {
        return zza(this.zzcv.read(bArr, i, i2));
    }

    public final void reset() throws IOException {
        this.zzcv.reset();
    }

    public final long skip(long j) throws IOException {
        return this.zzcv.skip(j);
    }

    final void zza(zzav com_google_android_gms_wearable_internal_zzav) {
        this.zzcw = (zzav) Preconditions.checkNotNull(com_google_android_gms_wearable_internal_zzav);
    }
}
