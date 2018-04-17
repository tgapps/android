package com.google.android.gms.wearable.internal;

import android.os.ParcelFileDescriptor.AutoCloseOutputStream;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.common.api.internal.BaseImplementation.ResultHolder;
import com.google.android.gms.common.internal.Preconditions;
import com.google.android.gms.wearable.Channel.GetOutputStreamResult;
import java.io.OutputStream;

final class zzgt extends zzgm<GetOutputStreamResult> {
    private final zzbr zzeu;

    public zzgt(ResultHolder<GetOutputStreamResult> resultHolder, zzbr com_google_android_gms_wearable_internal_zzbr) {
        super(resultHolder);
        this.zzeu = (zzbr) Preconditions.checkNotNull(com_google_android_gms_wearable_internal_zzbr);
    }

    public final void zza(zzdo com_google_android_gms_wearable_internal_zzdo) {
        OutputStream com_google_android_gms_wearable_internal_zzbl;
        if (com_google_android_gms_wearable_internal_zzdo.zzdr != null) {
            com_google_android_gms_wearable_internal_zzbl = new zzbl(new AutoCloseOutputStream(com_google_android_gms_wearable_internal_zzdo.zzdr));
            this.zzeu.zza(new zzbm(com_google_android_gms_wearable_internal_zzbl));
        } else {
            com_google_android_gms_wearable_internal_zzbl = null;
        }
        zza(new zzbh(new Status(com_google_android_gms_wearable_internal_zzdo.statusCode), com_google_android_gms_wearable_internal_zzbl));
    }
}
