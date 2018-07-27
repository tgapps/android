package com.google.firebase.iid;

import android.support.annotation.Keep;
import com.google.firebase.FirebaseApp;
import com.google.firebase.components.Component;
import com.google.firebase.components.ComponentRegistrar;
import com.google.firebase.components.Dependency;
import com.google.firebase.iid.internal.FirebaseInstanceIdInternal;
import java.util.Arrays;
import java.util.List;

@Keep
public final class Registrar implements ComponentRegistrar {

    private static class zza implements FirebaseInstanceIdInternal {
        private final FirebaseInstanceId zzce;

        public zza(FirebaseInstanceId firebaseInstanceId) {
            this.zzce = firebaseInstanceId;
        }
    }

    @Keep
    public final List<Component<?>> getComponents() {
        Component build = Component.builder(FirebaseInstanceId.class).add(Dependency.required(FirebaseApp.class)).factory(zzam.zzcd).alwaysEager().build();
        Component build2 = Component.builder(FirebaseInstanceIdInternal.class).add(Dependency.required(FirebaseInstanceId.class)).factory(zzan.zzcd).build();
        return Arrays.asList(new Component[]{build, build2});
    }
}
