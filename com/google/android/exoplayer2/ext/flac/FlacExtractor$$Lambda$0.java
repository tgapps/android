package com.google.android.exoplayer2.ext.flac;

import com.google.android.exoplayer2.extractor.Extractor;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;

final /* synthetic */ class FlacExtractor$$Lambda$0 implements ExtractorsFactory {
    static final ExtractorsFactory $instance = new FlacExtractor$$Lambda$0();

    private FlacExtractor$$Lambda$0() {
    }

    public Extractor[] createExtractors() {
        return new Extractor[]{new FlacExtractor()};
    }
}
