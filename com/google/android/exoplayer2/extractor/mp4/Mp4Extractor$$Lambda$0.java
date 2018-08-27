package com.google.android.exoplayer2.extractor.mp4;

import com.google.android.exoplayer2.extractor.Extractor;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;

final /* synthetic */ class Mp4Extractor$$Lambda$0 implements ExtractorsFactory {
    static final ExtractorsFactory $instance = new Mp4Extractor$$Lambda$0();

    private Mp4Extractor$$Lambda$0() {
    }

    public Extractor[] createExtractors() {
        return new Extractor[]{new Mp4Extractor()};
    }
}
