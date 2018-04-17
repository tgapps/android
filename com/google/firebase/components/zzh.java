package com.google.firebase.components;

import java.util.HashSet;
import java.util.Set;

final class zzh {
    private final Component<?> zzaj;
    private final Set<zzh> zzak = new HashSet();
    private final Set<zzh> zzal = new HashSet();

    zzh(Component<?> component) {
        this.zzaj = component;
    }

    final void zza(zzh com_google_firebase_components_zzh) {
        this.zzak.add(com_google_firebase_components_zzh);
    }

    final void zzb(zzh com_google_firebase_components_zzh) {
        this.zzal.add(com_google_firebase_components_zzh);
    }

    final void zzc(zzh com_google_firebase_components_zzh) {
        this.zzal.remove(com_google_firebase_components_zzh);
    }

    final Set<zzh> zzf() {
        return this.zzak;
    }

    final Component<?> zzk() {
        return this.zzaj;
    }

    final boolean zzl() {
        return this.zzal.isEmpty();
    }

    final boolean zzm() {
        return this.zzak.isEmpty();
    }
}
