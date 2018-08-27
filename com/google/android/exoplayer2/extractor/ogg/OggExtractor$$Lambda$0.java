package com.google.android.exoplayer2.extractor.ogg;

import com.google.android.exoplayer2.extractor.Extractor;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;

final /* synthetic */ class OggExtractor$$Lambda$0 implements ExtractorsFactory {
    static final ExtractorsFactory $instance = new OggExtractor$$Lambda$0();

    private OggExtractor$$Lambda$0() {
    }

    public Extractor[] createExtractors() {
        return new Extractor[]{new OggExtractor()};
    }
}
