package com.google.firebase.iid;

import android.support.v4.util.ArrayMap;
import android.util.Log;
import android.util.Pair;
import com.google.android.gms.tasks.Task;
import java.util.Map;
import javax.annotation.concurrent.GuardedBy;

final class zzao {
    @GuardedBy("this")
    private final Map<Pair<String, String>, Task<String>> zzcf = new ArrayMap();

    zzao() {
    }

    final /* synthetic */ Task zza(Pair pair, Task task) throws Exception {
        synchronized (this) {
            this.zzcf.remove(pair);
        }
        return task;
    }

    final synchronized Task<String> zza(String str, String str2, zzaq com_google_firebase_iid_zzaq) {
        Task<String> task;
        Pair pair = new Pair(str, str2);
        task = (Task) this.zzcf.get(pair);
        if (task == null) {
            if (Log.isLoggable("FirebaseInstanceId", 3)) {
                String valueOf = String.valueOf(pair);
                Log.d("FirebaseInstanceId", new StringBuilder(String.valueOf(valueOf).length() + 24).append("Making new request for: ").append(valueOf).toString());
            }
            task = com_google_firebase_iid_zzaq.zzt().continueWithTask(zzi.zzd(), new zzap(this, pair));
            this.zzcf.put(pair, task);
        } else if (Log.isLoggable("FirebaseInstanceId", 3)) {
            String valueOf2 = String.valueOf(pair);
            Log.d("FirebaseInstanceId", new StringBuilder(String.valueOf(valueOf2).length() + 29).append("Joining ongoing request for: ").append(valueOf2).toString());
        }
        return task;
    }
}
