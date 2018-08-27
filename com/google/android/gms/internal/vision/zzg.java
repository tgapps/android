package com.google.android.gms.internal.vision;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.IBinder;
import android.os.IInterface;
import android.os.RemoteException;
import android.util.Log;
import com.google.android.gms.dynamic.ObjectWrapper;
import com.google.android.gms.dynamite.DynamiteModule;
import com.google.android.gms.dynamite.DynamiteModule.LoadingException;
import com.google.android.gms.vision.barcode.Barcode;
import java.nio.ByteBuffer;

public final class zzg extends zzl<zzh> {
    private final zze zzbm;

    public zzg(Context context, zze com_google_android_gms_internal_vision_zze) {
        super(context, "BarcodeNativeHandle", "barcode");
        this.zzbm = com_google_android_gms_internal_vision_zze;
        zzp();
    }

    protected final /* synthetic */ Object zza(DynamiteModule dynamiteModule, Context context) throws RemoteException, LoadingException {
        zzj com_google_android_gms_internal_vision_zzj;
        IBinder instantiate = dynamiteModule.instantiate("com.google.android.gms.vision.barcode.ChimeraNativeBarcodeDetectorCreator");
        if (instantiate == null) {
            com_google_android_gms_internal_vision_zzj = null;
        } else {
            IInterface queryLocalInterface = instantiate.queryLocalInterface("com.google.android.gms.vision.barcode.internal.client.INativeBarcodeDetectorCreator");
            com_google_android_gms_internal_vision_zzj = queryLocalInterface instanceof zzj ? (zzj) queryLocalInterface : new zzk(instantiate);
        }
        return com_google_android_gms_internal_vision_zzj == null ? null : com_google_android_gms_internal_vision_zzj.zza(ObjectWrapper.wrap(context), this.zzbm);
    }

    public final Barcode[] zza(Bitmap bitmap, zzm com_google_android_gms_internal_vision_zzm) {
        if (!isOperational()) {
            return new Barcode[0];
        }
        try {
            return ((zzh) zzp()).zzb(ObjectWrapper.wrap(bitmap), com_google_android_gms_internal_vision_zzm);
        } catch (Throwable e) {
            Log.e("BarcodeNativeHandle", "Error calling native barcode detector", e);
            return new Barcode[0];
        }
    }

    public final Barcode[] zza(ByteBuffer byteBuffer, zzm com_google_android_gms_internal_vision_zzm) {
        if (!isOperational()) {
            return new Barcode[0];
        }
        try {
            return ((zzh) zzp()).zza(ObjectWrapper.wrap(byteBuffer), com_google_android_gms_internal_vision_zzm);
        } catch (Throwable e) {
            Log.e("BarcodeNativeHandle", "Error calling native barcode detector", e);
            return new Barcode[0];
        }
    }

    protected final void zzm() throws RemoteException {
        if (isOperational()) {
            ((zzh) zzp()).zzn();
        }
    }
}
