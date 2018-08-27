package com.google.android.exoplayer2.extractor.mp3;

import com.google.android.exoplayer2.extractor.Extractor;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;

final /* synthetic */ class Mp3Extractor$$Lambda$0 implements ExtractorsFactory {
    static final ExtractorsFactory $instance = new Mp3Extractor$$Lambda$0();

    private Mp3Extractor$$Lambda$0() {
    }

    public Extractor[] createExtractors() {
        return new Extractor[]{new Mp3Extractor()};
    }
}
