package com.google.android.exoplayer2.extractor.ts;

import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.extractor.ExtractorInput;
import com.google.android.exoplayer2.extractor.PositionHolder;
import com.google.android.exoplayer2.util.ParsableByteArray;
import com.google.android.exoplayer2.util.TimestampAdjuster;
import java.io.IOException;

final class TsDurationReader {
    private static final int DURATION_READ_BYTES = 37600;
    private static final int DURATION_READ_PACKETS = 200;
    private long durationUs = C.TIME_UNSET;
    private long firstPcrValue = C.TIME_UNSET;
    private boolean isDurationRead;
    private boolean isFirstPcrValueRead;
    private boolean isLastPcrValueRead;
    private long lastPcrValue = C.TIME_UNSET;
    private final ParsableByteArray packetBuffer = new ParsableByteArray((int) DURATION_READ_BYTES);
    private final TimestampAdjuster pcrTimestampAdjuster = new TimestampAdjuster(0);

    TsDurationReader() {
    }

    public boolean isDurationReadFinished() {
        return this.isDurationRead;
    }

    public int readDuration(ExtractorInput input, PositionHolder seekPositionHolder, int pcrPid) throws IOException, InterruptedException {
        if (pcrPid <= 0) {
            return finishReadDuration(input);
        }
        if (!this.isLastPcrValueRead) {
            return readLastPcrValue(input, seekPositionHolder, pcrPid);
        }
        if (this.lastPcrValue == C.TIME_UNSET) {
            return finishReadDuration(input);
        }
        if (!this.isFirstPcrValueRead) {
            return readFirstPcrValue(input, seekPositionHolder, pcrPid);
        }
        if (this.firstPcrValue == C.TIME_UNSET) {
            return finishReadDuration(input);
        }
        this.durationUs = this.pcrTimestampAdjuster.adjustTsTimestamp(this.lastPcrValue) - this.pcrTimestampAdjuster.adjustTsTimestamp(this.firstPcrValue);
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

    private int readFirstPcrValue(ExtractorInput input, PositionHolder seekPositionHolder, int pcrPid) throws IOException, InterruptedException {
        if (input.getPosition() != 0) {
            seekPositionHolder.position = 0;
            return 1;
        }
        int bytesToRead = (int) Math.min(37600, input.getLength());
        input.resetPeekPosition();
        input.peekFully(this.packetBuffer.data, 0, bytesToRead);
        this.packetBuffer.setPosition(0);
        this.packetBuffer.setLimit(bytesToRead);
        this.firstPcrValue = readFirstPcrValueFromBuffer(this.packetBuffer, pcrPid);
        this.isFirstPcrValueRead = true;
        return 0;
    }

    private long readFirstPcrValueFromBuffer(ParsableByteArray packetBuffer, int pcrPid) {
        int searchStartPosition = packetBuffer.getPosition();
        int searchEndPosition = packetBuffer.limit();
        for (int searchPosition = searchStartPosition; searchPosition < searchEndPosition; searchPosition++) {
            if (packetBuffer.data[searchPosition] == (byte) 71) {
                long pcrValue = readPcrFromPacket(packetBuffer, searchPosition, pcrPid);
                if (pcrValue != C.TIME_UNSET) {
                    return pcrValue;
                }
            }
        }
        return C.TIME_UNSET;
    }

    private int readLastPcrValue(ExtractorInput input, PositionHolder seekPositionHolder, int pcrPid) throws IOException, InterruptedException {
        int bytesToRead = (int) Math.min(37600, input.getLength());
        long bufferStartStreamPosition = input.getLength() - ((long) bytesToRead);
        if (input.getPosition() != bufferStartStreamPosition) {
            seekPositionHolder.position = bufferStartStreamPosition;
            return 1;
        }
        input.resetPeekPosition();
        input.peekFully(this.packetBuffer.data, 0, bytesToRead);
        this.packetBuffer.setPosition(0);
        this.packetBuffer.setLimit(bytesToRead);
        this.lastPcrValue = readLastPcrValueFromBuffer(this.packetBuffer, pcrPid);
        this.isLastPcrValueRead = true;
        return 0;
    }

    private long readLastPcrValueFromBuffer(ParsableByteArray packetBuffer, int pcrPid) {
        int searchStartPosition = packetBuffer.getPosition();
        for (int searchPosition = packetBuffer.limit() - 1; searchPosition >= searchStartPosition; searchPosition--) {
            if (packetBuffer.data[searchPosition] == (byte) 71) {
                long pcrValue = readPcrFromPacket(packetBuffer, searchPosition, pcrPid);
                if (pcrValue != C.TIME_UNSET) {
                    return pcrValue;
                }
            }
        }
        return C.TIME_UNSET;
    }

    private static long readPcrFromPacket(ParsableByteArray packetBuffer, int startOfPacket, int pcrPid) {
        boolean pcrFlagSet = true;
        packetBuffer.setPosition(startOfPacket);
        if (packetBuffer.bytesLeft() < 5) {
            return C.TIME_UNSET;
        }
        int tsPacketHeader = packetBuffer.readInt();
        if ((8388608 & tsPacketHeader) != 0 || ((2096896 & tsPacketHeader) >> 8) != pcrPid) {
            return C.TIME_UNSET;
        }
        boolean adaptationFieldExists;
        if ((tsPacketHeader & 32) != 0) {
            adaptationFieldExists = true;
        } else {
            adaptationFieldExists = false;
        }
        if (!adaptationFieldExists || packetBuffer.readUnsignedByte() < 7 || packetBuffer.bytesLeft() < 7) {
            return C.TIME_UNSET;
        }
        if ((packetBuffer.readUnsignedByte() & 16) != 16) {
            pcrFlagSet = false;
        }
        if (!pcrFlagSet) {
            return C.TIME_UNSET;
        }
        byte[] pcrBytes = new byte[6];
        packetBuffer.readBytes(pcrBytes, 0, pcrBytes.length);
        return readPcrValueFromPcrBytes(pcrBytes);
    }

    private static long readPcrValueFromPcrBytes(byte[] pcrBytes) {
        return (((((((long) pcrBytes[0]) & 255) << 25) | ((((long) pcrBytes[1]) & 255) << 17)) | ((((long) pcrBytes[2]) & 255) << 9)) | ((((long) pcrBytes[3]) & 255) << 1)) | ((((long) pcrBytes[4]) & 255) >> 7);
    }
}
