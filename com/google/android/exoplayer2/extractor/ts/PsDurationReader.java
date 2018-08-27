package com.google.android.exoplayer2.extractor.ts;

import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.extractor.ExtractorInput;
import com.google.android.exoplayer2.extractor.PositionHolder;
import com.google.android.exoplayer2.util.ParsableByteArray;
import com.google.android.exoplayer2.util.TimestampAdjuster;
import java.io.IOException;

final class PsDurationReader {
    private static final int DURATION_READ_BYTES = 20000;
    private long durationUs = C.TIME_UNSET;
    private long firstScrValue = C.TIME_UNSET;
    private boolean isDurationRead;
    private boolean isFirstScrValueRead;
    private boolean isLastScrValueRead;
    private long lastScrValue = C.TIME_UNSET;
    private final ParsableByteArray packetBuffer = new ParsableByteArray((int) DURATION_READ_BYTES);
    private final TimestampAdjuster scrTimestampAdjuster = new TimestampAdjuster(0);

    PsDurationReader() {
    }

    public boolean isDurationReadFinished() {
        return this.isDurationRead;
    }

    public int readDuration(ExtractorInput input, PositionHolder seekPositionHolder) throws IOException, InterruptedException {
        if (!this.isLastScrValueRead) {
            return readLastScrValue(input, seekPositionHolder);
        }
        if (this.lastScrValue == C.TIME_UNSET) {
            return finishReadDuration(input);
        }
        if (!this.isFirstScrValueRead) {
            return readFirstScrValue(input, seekPositionHolder);
        }
        if (this.firstScrValue == C.TIME_UNSET) {
            return finishReadDuration(input);
        }
        this.durationUs = this.scrTimestampAdjuster.adjustTsTimestamp(this.lastScrValue) - this.scrTimestampAdjuster.adjustTsTimestamp(this.firstScrValue);
        return finishReadDuration(input);
    }

    public long getDurationUs() {
        return this.durationUs;
    }

    private int finishReadDuration(ExtractorInput input) {
        this.isDurationRead = true;
        input.resetPeekPosition();
        return 0;
    }

    private int readFirstScrValue(ExtractorInput input, PositionHolder seekPositionHolder) throws IOException, InterruptedException {
        if (input.getPosition() != 0) {
            seekPositionHolder.position = 0;
            return 1;
        }
        int bytesToRead = (int) Math.min(20000, input.getLength());
        input.resetPeekPosition();
        input.peekFully(this.packetBuffer.data, 0, bytesToRead);
        this.packetBuffer.setPosition(0);
        this.packetBuffer.setLimit(bytesToRead);
        this.firstScrValue = readFirstScrValueFromBuffer(this.packetBuffer);
        this.isFirstScrValueRead = true;
        return 0;
    }

    private long readFirstScrValueFromBuffer(ParsableByteArray packetBuffer) {
        int searchStartPosition = packetBuffer.getPosition();
        int searchEndPosition = packetBuffer.limit();
        for (int searchPosition = searchStartPosition; searchPosition < searchEndPosition - 3; searchPosition++) {
            if (peakIntAtPosition(packetBuffer.data, searchPosition) == 442) {
                long scrValue = readScrValueFromPack(packetBuffer, searchPosition + 4);
                if (scrValue != C.TIME_UNSET) {
                    return scrValue;
                }
            }
        }
        return C.TIME_UNSET;
    }

    private int readLastScrValue(ExtractorInput input, PositionHolder seekPositionHolder) throws IOException, InterruptedException {
        int bytesToRead = (int) Math.min(20000, input.getLength());
        long bufferStartStreamPosition = input.getLength() - ((long) bytesToRead);
        if (input.getPosition() != bufferStartStreamPosition) {
            seekPositionHolder.position = bufferStartStreamPosition;
            return 1;
        }
        input.resetPeekPosition();
        input.peekFully(this.packetBuffer.data, 0, bytesToRead);
        this.packetBuffer.setPosition(0);
        this.packetBuffer.setLimit(bytesToRead);
        this.lastScrValue = readLastScrValueFromBuffer(this.packetBuffer);
        this.isLastScrValueRead = true;
        return 0;
    }

    private long readLastScrValueFromBuffer(ParsableByteArray packetBuffer) {
        int searchStartPosition = packetBuffer.getPosition();
        for (int searchPosition = packetBuffer.limit() - 4; searchPosition >= searchStartPosition; searchPosition--) {
            if (peakIntAtPosition(packetBuffer.data, searchPosition) == 442) {
                long scrValue = readScrValueFromPack(packetBuffer, searchPosition + 4);
                if (scrValue != C.TIME_UNSET) {
                    return scrValue;
                }
            }
        }
        return C.TIME_UNSET;
    }

    private int peakIntAtPosition(byte[] data, int position) {
        return ((((data[position] & 255) << 24) | ((data[position + 1] & 255) << 16)) | ((data[position + 2] & 255) << 8)) | (data[position + 3] & 255);
    }

    private long readScrValueFromPack(ParsableByteArray packetBuffer, int packHeaderStartPosition) {
        packetBuffer.setPosition(packHeaderStartPosition);
        if (packetBuffer.bytesLeft() < 9) {
            return C.TIME_UNSET;
        }
        byte[] scrBytes = new byte[9];
        packetBuffer.readBytes(scrBytes, 0, scrBytes.length);
        if (checkMarkerBits(scrBytes)) {
            return readScrValueFromPackHeader(scrBytes);
        }
        return C.TIME_UNSET;
    }

    private boolean checkMarkerBits(byte[] scrBytes) {
        boolean z = true;
        if ((scrBytes[0] & 196) != 68 || (scrBytes[2] & 4) != 4 || (scrBytes[4] & 4) != 4 || (scrBytes[5] & 1) != 1) {
            return false;
        }
        if ((scrBytes[8] & 3) != 3) {
            z = false;
        }
        return z;
    }

    private static long readScrValueFromPackHeader(byte[] scrBytes) {
        return ((((((((((long) scrBytes[0]) & 56) >> 3) << 30) | ((((long) scrBytes[0]) & 3) << 28)) | ((((long) scrBytes[1]) & 255) << 20)) | (((((long) scrBytes[2]) & 248) >> 3) << 15)) | ((((long) scrBytes[2]) & 3) << 13)) | ((((long) scrBytes[3]) & 255) << 5)) | ((((long) scrBytes[4]) & 248) >> 3);
    }
}
