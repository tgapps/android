package com.google.android.exoplayer2.text.cea;

import java.util.Collections;
import java.util.List;

public final class Cea708InitializationData {
    public final boolean isWideAspectRatio;

    private Cea708InitializationData(List<byte[]> initializationData) {
        boolean z;
        if (((byte[]) initializationData.get(0))[0] != (byte) 0) {
            z = true;
        } else {
            z = false;
        }
        this.isWideAspectRatio = z;
    }

    public static Cea708InitializationData fromData(List<byte[]> initializationData) {
        return new Cea708InitializationData(initializationData);
    }

    public static List<byte[]> buildData(boolean isWideAspectRatio) {
        int i = 1;
        Object obj = new byte[1];
        if (!isWideAspectRatio) {
            i = 0;
        }
        obj[0] = (byte) i;
        return Collections.singletonList(obj);
    }
}
