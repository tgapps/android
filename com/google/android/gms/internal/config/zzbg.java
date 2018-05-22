package com.google.android.gms.internal.config;

import java.io.IOException;

public final class zzbg extends IOException {
    public zzbg(String str) {
        super(str);
    }

    static zzbg zzaf() {
        return new zzbg("While parsing a protocol message, the input ended unexpectedly in the middle of a field.  This could mean either than the input has been truncated or that an embedded message misreported its own length.");
    }

    static zzbg zzag() {
        return new zzbg("CodedInputStream encountered an embedded string or message which claimed to have negative size.");
    }
}
