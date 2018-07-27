package com.google.firebase.iid;

import android.os.Bundle;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.Task;
import java.io.IOException;

final class zzs implements Continuation<Bundle, String> {
    private final /* synthetic */ zzp zzbe;

    zzs(zzp com_google_firebase_iid_zzp) {
        this.zzbe = com_google_firebase_iid_zzp;
    }

    public final /* synthetic */ Object then(Task task) throws Exception {
        return zzp.zza((Bundle) task.getResult(IOException.class));
    }
}
