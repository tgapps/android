package com.google.firebase.components;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/* compiled from: com.google.firebase:firebase-common@@16.0.1 */
final class zze {

    /* compiled from: com.google.firebase:firebase-common@@16.0.1 */
    static class zza {
        private final Component<?> zza;
        private final Set<zza> zzb = new HashSet();
        private final Set<zza> zzc = new HashSet();

        zza(Component<?> component) {
            this.zza = component;
        }

        final void zza(zza com_google_firebase_components_zze_zza) {
            this.zzb.add(com_google_firebase_components_zze_zza);
        }

        final void zzb(zza com_google_firebase_components_zze_zza) {
            this.zzc.add(com_google_firebase_components_zze_zza);
        }

        final Set<zza> zza() {
            return this.zzb;
        }

        final void zzc(zza com_google_firebase_components_zze_zza) {
            this.zzc.remove(com_google_firebase_components_zze_zza);
        }

        final Component<?> zzb() {
            return this.zza;
        }

        final boolean zzc() {
            return this.zzc.isEmpty();
        }

        final boolean zzd() {
            return this.zzb.isEmpty();
        }
    }

    private static Set<zza> zza(Set<zza> set) {
        Set<zza> hashSet = new HashSet();
        for (zza com_google_firebase_components_zze_zza : set) {
            if (com_google_firebase_components_zze_zza.zzc()) {
                hashSet.add(com_google_firebase_components_zze_zza);
            }
        }
        return hashSet;
    }

    static List<Component<?>> zza(List<Component<?>> list) {
        zza com_google_firebase_components_zze_zza;
        Map hashMap = new HashMap(list.size());
        for (Component component : list) {
            zza com_google_firebase_components_zze_zza2 = new zza(component);
            for (Class put : component.zza()) {
                if (hashMap.put(put, com_google_firebase_components_zze_zza2) != null) {
                    throw new IllegalArgumentException(String.format("Multiple components provide %s.", new Object[]{(Class) r4.next()}));
                }
            }
        }
        for (zza com_google_firebase_components_zze_zza3 : hashMap.values()) {
            for (Dependency dependency : com_google_firebase_components_zze_zza3.zzb().zzb()) {
                if (dependency.zzc()) {
                    com_google_firebase_components_zze_zza = (zza) hashMap.get(dependency.zza());
                    if (com_google_firebase_components_zze_zza != null) {
                        zza com_google_firebase_components_zze_zza32;
                        com_google_firebase_components_zze_zza32.zza(com_google_firebase_components_zze_zza);
                        com_google_firebase_components_zze_zza.zzb(com_google_firebase_components_zze_zza32);
                    }
                }
            }
        }
        Set<zza> hashSet = new HashSet(hashMap.values());
        Set zza = zza((Set) hashSet);
        List<Component<?>> arrayList = new ArrayList();
        while (!zza.isEmpty()) {
            com_google_firebase_components_zze_zza32 = (zza) zza.iterator().next();
            zza.remove(com_google_firebase_components_zze_zza32);
            arrayList.add(com_google_firebase_components_zze_zza32.zzb());
            for (zza com_google_firebase_components_zze_zza4 : com_google_firebase_components_zze_zza32.zza()) {
                com_google_firebase_components_zze_zza4.zzc(com_google_firebase_components_zze_zza32);
                if (com_google_firebase_components_zze_zza4.zzc()) {
                    zza.add(com_google_firebase_components_zze_zza4);
                }
            }
        }
        if (arrayList.size() == list.size()) {
            Collections.reverse(arrayList);
            return arrayList;
        }
        List arrayList2 = new ArrayList();
        for (zza com_google_firebase_components_zze_zza322 : hashSet) {
            if (!(com_google_firebase_components_zze_zza322.zzc() || com_google_firebase_components_zze_zza322.zzd())) {
                arrayList2.add(com_google_firebase_components_zze_zza322.zzb());
            }
        }
        throw new DependencyCycleException(arrayList2);
    }
}
