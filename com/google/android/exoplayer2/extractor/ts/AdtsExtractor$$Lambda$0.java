package com.google.android.exoplayer2.extractor.ts;

import com.google.android.exoplayer2.extractor.Extractor;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;

final /* synthetic */ class AdtsExtractor$$Lambda$0 implements ExtractorsFactory {
    static final ExtractorsFactory $instance = new AdtsExtractor$$Lambda$0();

    private AdtsExtractor$$Lambda$0() {
    }

    public Extractor[] createExtractors() {
        return new Extractor[]{new AdtsExtractor()};
    }
}
