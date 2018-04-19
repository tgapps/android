package com.google.android.gms.internal.config;

import java.io.IOException;

public final class zzba extends IOException {
    zzba(int i, int i2) {
        super("CodedOutputStream was writing to a flat byte array and ran out of space (pos " + i + " limit " + i2 + ").");
    }
}
