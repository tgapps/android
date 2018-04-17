package com.google.firebase.components;

import java.util.Arrays;
import java.util.List;

public class DependencyCycleException extends DependencyException {
    private final List<Component<?>> zzap;

    public DependencyCycleException(List<Component<?>> list) {
        String str = "Dependency cycle detected: ";
        String valueOf = String.valueOf(Arrays.toString(list.toArray()));
        super(valueOf.length() != 0 ? str.concat(valueOf) : new String(str));
        this.zzap = list;
    }
}
