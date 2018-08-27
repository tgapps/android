package com.google.firebase.components;

import com.google.android.gms.common.internal.Preconditions;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/* compiled from: com.google.firebase:firebase-common@@16.0.1 */
public final class Component<T> {
    private final Set<Class<? super T>> zza;
    private final Set<Dependency> zzb;
    private final int zzc;
    private final ComponentFactory<T> zzd;
    private final Set<Class<?>> zze;

    /* compiled from: com.google.firebase:firebase-common@@16.0.1 */
    public static class Builder<T> {
        private final Set<Class<? super T>> zza;
        private final Set<Dependency> zzb;
        private int zzc;
        private ComponentFactory<T> zzd;
        private Set<Class<?>> zze;

        private Builder(Class<T> anInterface, Class<? super T>... additionalInterfaces) {
            int i = 0;
            this.zza = new HashSet();
            this.zzb = new HashSet();
            this.zzc = 0;
            this.zze = new HashSet();
            Preconditions.checkNotNull(anInterface, "Null interface");
            this.zza.add(anInterface);
            int length = additionalInterfaces.length;
            while (i < length) {
                Preconditions.checkNotNull(additionalInterfaces[i], "Null interface");
                i++;
            }
            Collections.addAll(this.zza, additionalInterfaces);
        }

        public Builder<T> add(Dependency dependency) {
            Preconditions.checkNotNull(dependency, "Null dependency");
            Preconditions.checkArgument(!this.zza.contains(dependency.zza()), "Components are not allowed to depend on interfaces they themselves provide.");
            this.zzb.add(dependency);
            return this;
        }

        public Builder<T> alwaysEager() {
            return zza(1);
        }

        public Builder<T> eagerInDefaultApp() {
            return zza(2);
        }

        private Builder<T> zza(int i) {
            Preconditions.checkState(this.zzc == 0, "Instantiation type has already been set.");
            this.zzc = i;
            return this;
        }

        public Builder<T> factory(ComponentFactory<T> value) {
            this.zzd = (ComponentFactory) Preconditions.checkNotNull(value, "Null factory");
            return this;
        }

        public Component<T> build() {
            boolean z;
            if (this.zzd != null) {
                z = true;
            } else {
                z = false;
            }
            Preconditions.checkState(z, "Missing required property: factory.");
            return new Component(new HashSet(this.zza), new HashSet(this.zzb), this.zzc, this.zzd, this.zze);
        }
    }

    private Component(Set<Class<? super T>> providedInterfaces, Set<Dependency> dependencies, int instantiation, ComponentFactory<T> factory, Set<Class<?>> publishedEvents) {
        this.zza = Collections.unmodifiableSet(providedInterfaces);
        this.zzb = Collections.unmodifiableSet(dependencies);
        this.zzc = instantiation;
        this.zzd = factory;
        this.zze = Collections.unmodifiableSet(publishedEvents);
    }

    public final Set<Class<? super T>> zza() {
        return this.zza;
    }

    public final Set<Dependency> zzb() {
        return this.zzb;
    }

    public final ComponentFactory<T> zzc() {
        return this.zzd;
    }

    public final Set<Class<?>> zzd() {
        return this.zze;
    }

    public final boolean zze() {
        return this.zzc == 1;
    }

    public final boolean zzf() {
        return this.zzc == 2;
    }

    public final String toString() {
        return "Component<" + Arrays.toString(this.zza.toArray()) + ">{" + this.zzc + ", deps=" + Arrays.toString(this.zzb.toArray()) + "}";
    }

    public static <T> Builder<T> builder(Class<T> anInterface) {
        return new Builder(anInterface, new Class[0]);
    }

    public static <T> Builder<T> builder(Class<T> anInterface, Class<? super T>... additionalInterfaces) {
        return new Builder(anInterface, additionalInterfaces);
    }

    @SafeVarargs
    public static <T> Component<T> of(T value, Class<T> anInterface, Class<? super T>... additionalInterfaces) {
        return builder(anInterface, additionalInterfaces).factory(new zzb(value)).build();
    }

    static final /* synthetic */ Object zza(Object obj) {
        return obj;
    }
}
