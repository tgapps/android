package com.google.android.exoplayer2.extractor.mp4;

import com.google.android.exoplayer2.extractor.Extractor;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;

final /* synthetic */ class FragmentedMp4Extractor$$Lambda$0 implements ExtractorsFactory {
    static final ExtractorsFactory $instance = new FragmentedMp4Extractor$$Lambda$0();

    private FragmentedMp4Extractor$$Lambda$0() {
    }

    public Extractor[] createExtractors() {
        return new Extractor[]{new FragmentedMp4Extractor()};
    }
}
