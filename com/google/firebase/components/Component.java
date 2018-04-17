package com.google.firebase.components;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public final class Component<T> {
    private final Set<Class<? super T>> zzab;
    private final Set<Dependency> zzac;
    private final int zzad;
    private final ComponentFactory<T> zzae;

    public static class Builder<T> {
        private final Set<Class<? super T>> zzab;
        private final Set<Dependency> zzac;
        private int zzad;
        private ComponentFactory<T> zzae;

        private Builder(Class<T> cls, Class<? super T>... clsArr) {
            this.zzab = new HashSet();
            this.zzac = new HashSet();
            int i = 0;
            this.zzad = 0;
            zzk.zza(cls, "Null interface");
            this.zzab.add(cls);
            int length = clsArr.length;
            while (i < length) {
                zzk.zza(clsArr[i], "Null interface");
                i++;
            }
            Collections.addAll(this.zzab, clsArr);
        }

        public Component<T> build() {
            zzk.checkState(this.zzae != null, "Missing required property: factory.");
            return new Component(new HashSet(this.zzab), new HashSet(this.zzac), this.zzad, this.zzae);
        }

        public Builder<T> factory(ComponentFactory<T> componentFactory) {
            this.zzae = (ComponentFactory) zzk.zza(componentFactory, "Null factory");
            return this;
        }
    }

    private Component(Set<Class<? super T>> set, Set<Dependency> set2, int i, ComponentFactory<T> componentFactory) {
        this.zzab = Collections.unmodifiableSet(set);
        this.zzac = Collections.unmodifiableSet(set2);
        this.zzad = i;
        this.zzae = componentFactory;
    }

    public static <T> Builder<T> builder(Class<T> cls) {
        return new Builder(cls, new Class[0]);
    }

    public static <T> Component<T> of(Class<T> cls, T t) {
        return builder(cls).factory(new zza(t)).build();
    }

    public final String toString() {
        StringBuilder stringBuilder = new StringBuilder("Component<");
        stringBuilder.append(Arrays.toString(this.zzab.toArray()));
        stringBuilder.append(">{");
        stringBuilder.append(this.zzad);
        stringBuilder.append(", deps=");
        stringBuilder.append(Arrays.toString(this.zzac.toArray()));
        stringBuilder.append("}");
        return stringBuilder.toString();
    }

    public final Set<Class<? super T>> zze() {
        return this.zzab;
    }

    public final Set<Dependency> zzf() {
        return this.zzac;
    }

    public final ComponentFactory<T> zzg() {
        return this.zzae;
    }

    public final boolean zzh() {
        return this.zzad == 1;
    }

    public final boolean zzi() {
        return this.zzad == 2;
    }
}
