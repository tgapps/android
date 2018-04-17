package com.google.android.gms.wearable.internal;

import android.os.IBinder;
import android.os.IInterface;
import android.os.RemoteException;
import android.util.Log;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

final class zzer<T> {
    private final Map<T, zzhk<T>> zzeb = new HashMap();

    zzer() {
    }

    public final void zza(IBinder iBinder) {
        synchronized (this.zzeb) {
            zzep com_google_android_gms_wearable_internal_zzep;
            if (iBinder == null) {
                com_google_android_gms_wearable_internal_zzep = null;
            } else {
                IInterface queryLocalInterface = iBinder.queryLocalInterface("com.google.android.gms.wearable.internal.IWearableService");
                com_google_android_gms_wearable_internal_zzep = queryLocalInterface instanceof zzep ? (zzep) queryLocalInterface : new zzeq(iBinder);
            }
            zzek com_google_android_gms_wearable_internal_zzgz = new zzgz();
            for (Entry entry : this.zzeb.entrySet()) {
                zzhk com_google_android_gms_wearable_internal_zzhk = (zzhk) entry.getValue();
                try {
                    com_google_android_gms_wearable_internal_zzep.zza(com_google_android_gms_wearable_internal_zzgz, new zzd(com_google_android_gms_wearable_internal_zzhk));
                    if (Log.isLoggable("WearableClient", 3)) {
                        String valueOf = String.valueOf(entry.getKey());
                        String valueOf2 = String.valueOf(com_google_android_gms_wearable_internal_zzhk);
                        StringBuilder stringBuilder = new StringBuilder((27 + String.valueOf(valueOf).length()) + String.valueOf(valueOf2).length());
                        stringBuilder.append("onPostInitHandler: added: ");
                        stringBuilder.append(valueOf);
                        stringBuilder.append("/");
                        stringBuilder.append(valueOf2);
                        Log.d("WearableClient", stringBuilder.toString());
                    }
                } catch (RemoteException e) {
                    String valueOf3 = String.valueOf(entry.getKey());
                    String valueOf4 = String.valueOf(com_google_android_gms_wearable_internal_zzhk);
                    StringBuilder stringBuilder2 = new StringBuilder((32 + String.valueOf(valueOf3).length()) + String.valueOf(valueOf4).length());
                    stringBuilder2.append("onPostInitHandler: Didn't add: ");
                    stringBuilder2.append(valueOf3);
                    stringBuilder2.append("/");
                    stringBuilder2.append(valueOf4);
                    Log.w("WearableClient", stringBuilder2.toString());
                }
            }
        }
    }
}
