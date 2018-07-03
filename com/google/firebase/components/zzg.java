package com.google.firebase.components;

import com.google.firebase.inject.Provider;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public final class zzg implements ComponentContainer {
    private final List<Component<?>> zzah;
    private final Map<Class<?>, zzi<?>> zzai = new HashMap();

    public zzg(Iterable<ComponentRegistrar> iterable, Component<?>... componentArr) {
        zzh com_google_firebase_components_zzh;
        List<Component> arrayList = new ArrayList();
        for (ComponentRegistrar components : iterable) {
            arrayList.addAll(components.getComponents());
        }
        Collections.addAll(arrayList, componentArr);
        Map hashMap = new HashMap(arrayList.size());
        for (Component component : arrayList) {
            zzh com_google_firebase_components_zzh2 = new zzh(component);
            for (Class put : component.zze()) {
                if (hashMap.put(put, com_google_firebase_components_zzh2) != null) {
                    throw new IllegalArgumentException(String.format("Multiple components provide %s.", new Object[]{(Class) r5.next()}));
                }
            }
        }
        for (zzh com_google_firebase_components_zzh3 : hashMap.values()) {
            for (Dependency dependency : com_google_firebase_components_zzh3.zzk().zzf()) {
                zzh com_google_firebase_components_zzh4;
                if (dependency.zzp()) {
                    com_google_firebase_components_zzh4 = (zzh) hashMap.get(dependency.zzn());
                    if (com_google_firebase_components_zzh4 != null) {
                        com_google_firebase_components_zzh3.zza(com_google_firebase_components_zzh4);
                        com_google_firebase_components_zzh4.zzb(com_google_firebase_components_zzh3);
                    }
                }
            }
        }
        Set<zzh> hashSet = new HashSet(hashMap.values());
        Set hashSet2 = new HashSet();
        for (zzh com_google_firebase_components_zzh32 : hashSet) {
            if (com_google_firebase_components_zzh32.zzl()) {
                hashSet2.add(com_google_firebase_components_zzh32);
            }
        }
        List arrayList2 = new ArrayList();
        while (!hashSet2.isEmpty()) {
            com_google_firebase_components_zzh32 = (zzh) hashSet2.iterator().next();
            hashSet2.remove(com_google_firebase_components_zzh32);
            arrayList2.add(com_google_firebase_components_zzh32.zzk());
            for (zzh com_google_firebase_components_zzh42 : com_google_firebase_components_zzh32.zzf()) {
                com_google_firebase_components_zzh42.zzc(com_google_firebase_components_zzh32);
                if (com_google_firebase_components_zzh42.zzl()) {
                    hashSet2.add(com_google_firebase_components_zzh42);
                }
            }
        }
        if (arrayList2.size() == arrayList.size()) {
            Collections.reverse(arrayList2);
            this.zzah = Collections.unmodifiableList(arrayList2);
            for (Component component2 : this.zzah) {
                zzi com_google_firebase_components_zzi = new zzi(component2.zzg(), new zzl(component2.zzf(), this));
                for (Class put2 : component2.zze()) {
                    this.zzai.put(put2, com_google_firebase_components_zzi);
                }
            }
            for (Component component22 : this.zzah) {
                for (Dependency dependency2 : component22.zzf()) {
                    if (dependency2.zzo() && !this.zzai.containsKey(dependency2.zzn())) {
                        throw new MissingDependencyException(String.format("Unsatisfied dependency for component %s: %s", new Object[]{component22, dependency2.zzn()}));
                    }
                }
            }
            return;
        }
        List arrayList3 = new ArrayList();
        for (zzh com_google_firebase_components_zzh322 : hashSet) {
            if (!(com_google_firebase_components_zzh322.zzl() || com_google_firebase_components_zzh322.zzm())) {
                arrayList3.add(com_google_firebase_components_zzh322.zzk());
            }
        }
        throw new DependencyCycleException(arrayList3);
    }

    public final Object get(Class cls) {
        return ComponentContainer$$CC.get(this, cls);
    }

    public final <T> Provider<T> getProvider(Class<T> cls) {
        zzk.zza(cls, "Null interface requested.");
        return (Provider) this.zzai.get(cls);
    }

    public final void zzb(boolean z) {
        for (Component component : this.zzah) {
            if (component.zzh() || (component.zzi() && z)) {
                get((Class) component.zze().iterator().next());
            }
        }
    }
}
