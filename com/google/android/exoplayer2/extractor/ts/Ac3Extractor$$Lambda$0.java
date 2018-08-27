package com.google.android.exoplayer2.extractor.ts;

import com.google.android.exoplayer2.extractor.Extractor;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;

final /* synthetic */ class Ac3Extractor$$Lambda$0 implements ExtractorsFactory {
    static final ExtractorsFactory $instance = new Ac3Extractor$$Lambda$0();

    private Ac3Extractor$$Lambda$0() {
    }

    public Extractor[] createExtractors() {
        return new Extractor[]{new Ac3Extractor()};
    }
}
