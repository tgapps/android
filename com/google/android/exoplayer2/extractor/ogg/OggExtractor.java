package com.google.android.exoplayer2.extractor.ogg;

import com.google.android.exoplayer2.ParserException;
import com.google.android.exoplayer2.extractor.Extractor;
import com.google.android.exoplayer2.extractor.ExtractorInput;
import com.google.android.exoplayer2.extractor.ExtractorOutput;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.extractor.PositionHolder;
import com.google.android.exoplayer2.extractor.TrackOutput;
import com.google.android.exoplayer2.util.ParsableByteArray;
import java.io.IOException;

public class OggExtractor implements Extractor {
    public static final ExtractorsFactory FACTORY = OggExtractor$$Lambda$0.$instance;
    private static final int MAX_VERIFICATION_BYTES = 8;
    private ExtractorOutput output;
    private StreamReader streamReader;
    private boolean streamReaderInitialized;

    public boolean sniff(ExtractorInput input) throws IOException, InterruptedException {
        try {
            return sniffInternal(input);
        } catch (ParserException e) {
            return false;
        }
    }

    public void init(ExtractorOutput output) {
        this.output = output;
    }

    public void seek(long position, long timeUs) {
        if (this.streamReader != null) {
            this.streamReader.seek(position, timeUs);
        }
    }

    public void release() {
    }

    public int read(ExtractorInput input, PositionHolder seekPosition) throws IOException, InterruptedException {
        if (this.streamReader == null) {
            if (sniffInternal(input)) {
                input.resetPeekPosition();
            } else {
                throw new ParserException("Failed to determine bitstream type");
            }
        }
        if (!this.streamReaderInitialized) {
            TrackOutput trackOutput = this.output.track(0, 1);
            this.output.endTracks();
            this.streamReader.init(this.output, trackOutput);
            this.streamReaderInitialized = true;
        }
        return this.streamReader.read(input, seekPosition);
    }

    private boolean sniffInternal(ExtractorInput input) throws IOException, InterruptedException {
        OggPageHeader header = new OggPageHeader();
        if (!header.populate(input, true) || (header.type & 2) != 2) {
            return false;
        }
        int length = Math.min(header.bodySize, 8);
        ParsableByteArray scratch = new ParsableByteArray(length);
        input.peekFully(scratch.data, 0, length);
        if (FlacReader.verifyBitstreamType(resetPosition(scratch))) {
            this.streamReader = new FlacReader();
        } else if (VorbisReader.verifyBitstreamType(resetPosition(scratch))) {
            this.streamReader = new VorbisReader();
        } else if (!OpusReader.verifyBitstreamType(resetPosition(scratch))) {
            return false;
        } else {
            this.streamReader = new OpusReader();
        }
        return true;
    }

    private static ParsableByteArray resetPosition(ParsableByteArray scratch) {
        scratch.setPosition(0);
        return scratch;
    }
}
