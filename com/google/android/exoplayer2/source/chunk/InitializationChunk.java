package com.google.android.exoplayer2.source.chunk;

import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.extractor.DefaultExtractorInput;
import com.google.android.exoplayer2.extractor.Extractor;
import com.google.android.exoplayer2.extractor.ExtractorInput;
import com.google.android.exoplayer2.extractor.PositionHolder;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DataSpec;
import com.google.android.exoplayer2.util.Assertions;
import com.google.android.exoplayer2.util.Util;
import java.io.IOException;

public final class InitializationChunk extends Chunk {
    private static final PositionHolder DUMMY_POSITION_HOLDER = new PositionHolder();
    private final ChunkExtractorWrapper extractorWrapper;
    private volatile boolean loadCanceled;
    private long nextLoadPosition;

    public InitializationChunk(DataSource dataSource, DataSpec dataSpec, Format trackFormat, int trackSelectionReason, Object trackSelectionData, ChunkExtractorWrapper extractorWrapper) {
        super(dataSource, dataSpec, 2, trackFormat, trackSelectionReason, trackSelectionData, C.TIME_UNSET, C.TIME_UNSET);
        this.extractorWrapper = extractorWrapper;
    }

    public void cancelLoad() {
        this.loadCanceled = true;
    }

    public void load() throws IOException, InterruptedException {
        DataSpec loadDataSpec = this.dataSpec.subrange(this.nextLoadPosition);
        ExtractorInput input;
        try {
            input = new DefaultExtractorInput(this.dataSource, loadDataSpec.absoluteStreamPosition, this.dataSource.open(loadDataSpec));
            if (this.nextLoadPosition == 0) {
                this.extractorWrapper.init(null, C.TIME_UNSET);
            }
            Extractor extractor = this.extractorWrapper.extractor;
            int result = 0;
            while (result == 0 && !this.loadCanceled) {
                result = extractor.read(input, DUMMY_POSITION_HOLDER);
            }
            Assertions.checkState(result != 1);
            this.nextLoadPosition = input.getPosition() - this.dataSpec.absoluteStreamPosition;
            Util.closeQuietly(this.dataSource);
        } catch (Throwable th) {
            Util.closeQuietly(this.dataSource);
        }
    }
}
