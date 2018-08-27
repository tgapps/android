package com.google.firebase.iid;

import android.os.Bundle;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.Task;
import java.io.IOException;

final class zzt implements Continuation<Bundle, String> {
    private final /* synthetic */ zzq zzbk;

    zzt(zzq com_google_firebase_iid_zzq) {
        this.zzbk = com_google_firebase_iid_zzq;
    }

    public final /* synthetic */ Object then(Task task) throws Exception {
        return zzq.zza((Bundle) task.getResult(IOException.class));
    }
}
