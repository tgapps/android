package com.google.android.exoplayer2.ext.flac;

import com.google.android.exoplayer2.ExoPlayerLibraryInfo;

public final class FlacLibrary {
    static {
        ExoPlayerLibraryInfo.registerModule("goog.exo.flac");
    }

    private FlacLibrary() {
    }
}
