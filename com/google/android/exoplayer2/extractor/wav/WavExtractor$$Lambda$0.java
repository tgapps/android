package com.google.android.exoplayer2.extractor.wav;

import com.google.android.exoplayer2.extractor.Extractor;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;

final /* synthetic */ class WavExtractor$$Lambda$0 implements ExtractorsFactory {
    static final ExtractorsFactory $instance = new WavExtractor$$Lambda$0();

    private WavExtractor$$Lambda$0() {
    }

    public Extractor[] createExtractors() {
        return new Extractor[]{new WavExtractor()};
    }
}
