package com.google.android.gms.internal.measurement;

import java.io.IOException;

public class zzvt extends IOException {
    private zzwt zzbzm = null;

    public zzvt(String str) {
        super(str);
    }

    public final zzvt zzg(zzwt com_google_android_gms_internal_measurement_zzwt) {
        this.zzbzm = com_google_android_gms_internal_measurement_zzwt;
        return this;
    }

    static zzvt zzwk() {
        return new zzvt("While parsing a protocol message, the input ended unexpectedly in the middle of a field.  This could mean either that the input has been truncated or that an embedded message misreported its own length.");
    }

    static zzvt zzwl() {
        return new zzvt("CodedInputStream encountered an embedded string or message which claimed to have negative size.");
    }

    static zzvt zzwm() {
        return new zzvt("CodedInputStream encountered a malformed varint.");
    }

    static zzvt zzwn() {
        return new zzvt("Protocol message end-group tag did not match expected tag.");
    }

    static zzvu zzwo() {
        return new zzvu("Protocol message tag had invalid wire type.");
    }

    static zzvt zzwp() {
        return new zzvt("Protocol message had too many levels of nesting.  May be malicious.  Use CodedInputStream.setRecursionLimit() to increase the depth limit.");
    }

    static zzvt zzwq() {
        return new zzvt("Failed to parse the message.");
    }

    static zzvt zzwr() {
        return new zzvt("Protocol message had invalid UTF-8.");
    }
}
