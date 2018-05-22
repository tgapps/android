package com.google.firebase.iid;

import android.os.Bundle;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.Task;
import java.io.IOException;

final class zzn implements Continuation<Bundle, String> {
    private final /* synthetic */ zzl zzbc;

    zzn(zzl com_google_firebase_iid_zzl) {
        this.zzbc = com_google_firebase_iid_zzl;
    }

    public final /* synthetic */ Object then(Task task) throws Exception {
        return this.zzbc.zza((Bundle) task.getResult(IOException.class));
    }
}
