package com.google.android.exoplayer2.extractor.flv;

import com.google.android.exoplayer2.extractor.Extractor;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;

final /* synthetic */ class FlvExtractor$$Lambda$0 implements ExtractorsFactory {
    static final ExtractorsFactory $instance = new FlvExtractor$$Lambda$0();

    private FlvExtractor$$Lambda$0() {
    }

    public Extractor[] createExtractors() {
        return new Extractor[]{new FlvExtractor()};
    }
}
