package com.google.android.gms.wearable.internal;

import android.util.Log;
import com.google.android.gms.common.internal.Preconditions;
import com.google.android.gms.wearable.ChannelIOException;
import java.io.IOException;
import java.io.OutputStream;
import javax.annotation.Nullable;

public final class zzbl extends OutputStream {
    @Nullable
    private volatile zzav zzcw;
    private final OutputStream zzcy;

    public zzbl(OutputStream outputStream) {
        this.zzcy = (OutputStream) Preconditions.checkNotNull(outputStream);
    }

    private final IOException zza(IOException iOException) {
        zzav com_google_android_gms_wearable_internal_zzav = this.zzcw;
        if (com_google_android_gms_wearable_internal_zzav == null) {
            return iOException;
        }
        if (Log.isLoggable("ChannelOutputStream", 2)) {
            Log.v("ChannelOutputStream", "Caught IOException, but channel has been closed. Translating to ChannelIOException.", iOException);
        }
        return new ChannelIOException("Channel closed unexpectedly before stream was finished", com_google_android_gms_wearable_internal_zzav.zzg, com_google_android_gms_wearable_internal_zzav.zzcj);
    }

    public final void close() throws IOException {
        try {
            this.zzcy.close();
        } catch (IOException e) {
            throw zza(e);
        }
    }

    public final void flush() throws IOException {
        try {
            this.zzcy.flush();
        } catch (IOException e) {
            throw zza(e);
        }
    }

    public final void write(int i) throws IOException {
        try {
            this.zzcy.write(i);
        } catch (IOException e) {
            throw zza(e);
        }
    }

    public final void write(byte[] bArr) throws IOException {
        try {
            this.zzcy.write(bArr);
        } catch (IOException e) {
            throw zza(e);
        }
    }

    public final void write(byte[] bArr, int i, int i2) throws IOException {
        try {
            this.zzcy.write(bArr, i, i2);
        } catch (IOException e) {
            throw zza(e);
        }
    }

    final void zzc(zzav com_google_android_gms_wearable_internal_zzav) {
        this.zzcw = com_google_android_gms_wearable_internal_zzav;
    }
}
