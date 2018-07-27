package com.google.firebase.iid;

import java.util.concurrent.Executor;

final /* synthetic */ class zzj implements Executor {
    static final Executor zzac = new zzj();

    private zzj() {
    }

    public final void execute(Runnable runnable) {
        runnable.run();
    }
}
