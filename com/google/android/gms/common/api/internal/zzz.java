package com.google.android.gms.common.api.internal;

import android.support.v4.util.ArrayMap;
import android.util.Log;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.AvailabilityException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import java.util.Collections;
import java.util.Map;

final class zzz implements OnCompleteListener<Map<zzh<?>, String>> {
    private final /* synthetic */ zzw zzgu;
    private SignInConnectionListener zzgv;

    final void cancel() {
        this.zzgv.onComplete();
    }

    public final void onComplete(Task<Map<zzh<?>, String>> task) {
        this.zzgu.zzga.lock();
        try {
            SignInConnectionListener signInConnectionListener;
            if (this.zzgu.zzgp) {
                if (task.isSuccessful()) {
                    this.zzgu.zzgr = new ArrayMap(this.zzgu.zzgh.size());
                    for (zzv zzm : this.zzgu.zzgh.values()) {
                        this.zzgu.zzgr.put(zzm.zzm(), ConnectionResult.RESULT_SUCCESS);
                    }
                } else if (task.getException() instanceof AvailabilityException) {
                    AvailabilityException availabilityException = (AvailabilityException) task.getException();
                    if (this.zzgu.zzgn) {
                        this.zzgu.zzgr = new ArrayMap(this.zzgu.zzgh.size());
                        for (zzv com_google_android_gms_common_api_internal_zzv : this.zzgu.zzgh.values()) {
                            Map zzg;
                            zzh zzm2 = com_google_android_gms_common_api_internal_zzv.zzm();
                            Object connectionResult = availabilityException.getConnectionResult(com_google_android_gms_common_api_internal_zzv);
                            if (this.zzgu.zza(com_google_android_gms_common_api_internal_zzv, (ConnectionResult) connectionResult)) {
                                zzg = this.zzgu.zzgr;
                                connectionResult = new ConnectionResult(16);
                            } else {
                                zzg = this.zzgu.zzgr;
                            }
                            zzg.put(zzm2, connectionResult);
                        }
                    } else {
                        this.zzgu.zzgr = availabilityException.zzl();
                    }
                } else {
                    Log.e("ConnectionlessGAC", "Unexpected availability exception", task.getException());
                    this.zzgu.zzgr = Collections.emptyMap();
                }
                if (this.zzgu.isConnected()) {
                    this.zzgu.zzgq.putAll(this.zzgu.zzgr);
                    if (this.zzgu.zzai() == null) {
                        this.zzgu.zzag();
                        this.zzgu.zzah();
                        this.zzgu.zzgl.signalAll();
                    }
                }
                signInConnectionListener = this.zzgv;
            } else {
                signInConnectionListener = this.zzgv;
            }
            signInConnectionListener.onComplete();
        } finally {
            this.zzgu.zzga.unlock();
        }
    }
}
