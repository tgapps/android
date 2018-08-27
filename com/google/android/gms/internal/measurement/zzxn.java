package com.google.android.gms.internal.measurement;

import java.util.Collections;
import java.util.List;
import java.util.Map.Entry;

final class zzxn extends zzxm<FieldDescriptorType, Object> {
    zzxn(int i) {
        super(i);
    }

    public final void zzsm() {
        if (!isImmutable()) {
            for (int i = 0; i < zzxw(); i++) {
                Entry zzbu = zzbu(i);
                if (((zzvf) zzbu.getKey()).zzvy()) {
                    zzbu.setValue(Collections.unmodifiableList((List) zzbu.getValue()));
                }
            }
            for (Entry entry : zzxx()) {
                if (((zzvf) entry.getKey()).zzvy()) {
                    entry.setValue(Collections.unmodifiableList((List) entry.getValue()));
                }
            }
        }
        super.zzsm();
    }
}
