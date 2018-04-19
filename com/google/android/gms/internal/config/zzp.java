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
        String id;
        String token;
        Throwable e;
        Builder buildDataHolder = DataBufferSafeParcelable.buildDataHolder();
        for (Entry entry : this.zzn.zzb().entrySet()) {
            DataBufferSafeParcelable.addValue(buildDataHolder, new zzz((String) entry.getKey(), (String) entry.getValue()));
        }
        DataHolder build = buildDataHolder.build(0);
        try {
            id = FirebaseInstanceId.getInstance().getId();
            try {
                token = FirebaseInstanceId.getInstance().getToken();
            } catch (IllegalStateException e2) {
                e = e2;
                if (Log.isLoggable("ConfigApiImpl", 3)) {
                    Log.d("ConfigApiImpl", "Cannot retrieve instanceId or instanceIdToken.", e);
                }
                token = null;
                com_google_android_gms_internal_config_zzah.zza(this.zzo, new zzab(context.getPackageName(), this.zzn.zza(), build, this.zzn.getGmpAppId(), id, token, null, this.zzn.zzc(), zzn.zzb(context), this.zzn.zzd(), this.zzn.zze()));
            }
        } catch (IllegalStateException e3) {
            e = e3;
            id = null;
            if (Log.isLoggable("ConfigApiImpl", 3)) {
                Log.d("ConfigApiImpl", "Cannot retrieve instanceId or instanceIdToken.", e);
            }
            token = null;
            com_google_android_gms_internal_config_zzah.zza(this.zzo, new zzab(context.getPackageName(), this.zzn.zza(), build, this.zzn.getGmpAppId(), id, token, null, this.zzn.zzc(), zzn.zzb(context), this.zzn.zzd(), this.zzn.zze()));
        }
        try {
            com_google_android_gms_internal_config_zzah.zza(this.zzo, new zzab(context.getPackageName(), this.zzn.zza(), build, this.zzn.getGmpAppId(), id, token, null, this.zzn.zzc(), zzn.zzb(context), this.zzn.zzd(), this.zzn.zze()));
        } finally {
            build.close();
        }
    }
}
