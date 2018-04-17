package com.google.android.gms.common.api.internal;

import java.util.Collections;
import java.util.Set;
import java.util.WeakHashMap;

public class ListenerHolders {
    private final Set<ListenerHolder<?>> zzlm = Collections.newSetFromMap(new WeakHashMap());

    public final void release() {
        for (ListenerHolder clear : this.zzlm) {
            clear.clear();
        }
        this.zzlm.clear();
    }
}
