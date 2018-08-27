package com.google.android.exoplayer2.extractor.mkv;

import com.google.android.exoplayer2.extractor.Extractor;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;

final /* synthetic */ class MatroskaExtractor$$Lambda$0 implements ExtractorsFactory {
    static final ExtractorsFactory $instance = new MatroskaExtractor$$Lambda$0();

    private MatroskaExtractor$$Lambda$0() {
    }

    public Extractor[] createExtractors() {
        return new Extractor[]{new MatroskaExtractor()};
    }
}
