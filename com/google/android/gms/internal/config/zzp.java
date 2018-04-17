package com.google.android.gms.internal.config;

import android.content.Context;
import android.os.RemoteException;
import android.util.Log;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Result;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.common.data.DataBufferSafeParcelable;
import com.google.android.gms.common.data.DataHolder;
import com.google.android.gms.common.data.DataHolder.Builder;
import com.google.firebase.iid.FirebaseInstanceId;
import java.util.HashMap;
import java.util.Map.Entry;

final class zzp extends zzs {
    private final /* synthetic */ zzi zzn;

    zzp(zzo com_google_android_gms_internal_config_zzo, GoogleApiClient googleApiClient, zzi com_google_android_gms_internal_config_zzi) {
        this.zzn = com_google_android_gms_internal_config_zzi;
        super(googleApiClient);
    }

    protected final /* synthetic */ Result createFailedResult(Status status) {
        return new zzu(status, new HashMap());
    }

    protected final void zza(Context context, zzah com_google_android_gms_internal_config_zzah) throws RemoteException {
        String str;
        String token;
        String str2;
        Throwable th;
        DataHolder dataHolder;
        Builder buildDataHolder = DataBufferSafeParcelable.buildDataHolder();
        for (Entry entry : this.zzn.zzb().entrySet()) {
            DataBufferSafeParcelable.addValue(buildDataHolder, new zzz((String) entry.getKey(), (String) entry.getValue()));
        }
        DataHolder build = buildDataHolder.build(0);
        try {
            String id = FirebaseInstanceId.getInstance().getId();
            try {
                str = id;
                token = FirebaseInstanceId.getInstance().getToken();
            } catch (Throwable e) {
                str2 = id;
                th = e;
                if (Log.isLoggable("ConfigApiImpl", 3)) {
                    Log.d("ConfigApiImpl", "Cannot retrieve instanceId or instanceIdToken.", th);
                }
                token = null;
                str = str2;
                dataHolder = build;
                com_google_android_gms_internal_config_zzah.zza(r1.zzo, new zzab(context.getPackageName(), r1.zzn.zza(), dataHolder, r1.zzn.getGmpAppId(), str, token, null, r1.zzn.zzc(), zzn.zzb(context), r1.zzn.zzd(), r1.zzn.zze()));
                build.close();
            }
        } catch (Throwable e2) {
            th = e2;
            str2 = null;
            if (Log.isLoggable("ConfigApiImpl", 3)) {
                Log.d("ConfigApiImpl", "Cannot retrieve instanceId or instanceIdToken.", th);
            }
            token = null;
            str = str2;
            dataHolder = build;
            com_google_android_gms_internal_config_zzah.zza(r1.zzo, new zzab(context.getPackageName(), r1.zzn.zza(), dataHolder, r1.zzn.getGmpAppId(), str, token, null, r1.zzn.zzc(), zzn.zzb(context), r1.zzn.zzd(), r1.zzn.zze()));
            build.close();
        }
        dataHolder = build;
        try {
            com_google_android_gms_internal_config_zzah.zza(r1.zzo, new zzab(context.getPackageName(), r1.zzn.zza(), dataHolder, r1.zzn.getGmpAppId(), str, token, null, r1.zzn.zzc(), zzn.zzb(context), r1.zzn.zzd(), r1.zzn.zze()));
            build.close();
        } catch (Throwable e22) {
            Throwable th2 = e22;
            build.close();
        }
    }
}
