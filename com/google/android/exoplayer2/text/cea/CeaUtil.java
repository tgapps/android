package com.google.android.exoplayer2.text.cea;

import android.util.Log;
import com.google.android.exoplayer2.extractor.TrackOutput;
import com.google.android.exoplayer2.util.ParsableByteArray;
import com.google.android.exoplayer2.util.Util;

public final class CeaUtil {
    private static final int COUNTRY_CODE = 181;
    private static final int PAYLOAD_TYPE_CC = 4;
    private static final int PROVIDER_CODE_ATSC = 49;
    private static final int PROVIDER_CODE_DIRECTV = 47;
    private static final String TAG = "CeaUtil";
    public static final int USER_DATA_IDENTIFIER_GA94 = Util.getIntegerCodeForString("GA94");
    public static final int USER_DATA_TYPE_CODE_MPEG_CC = 3;

    public static void consume(long presentationTimeUs, ParsableByteArray seiBuffer, TrackOutput[] outputs) {
        while (seiBuffer.bytesLeft() > 1) {
            int payloadType = readNon255TerminatedValue(seiBuffer);
            int payloadSize = readNon255TerminatedValue(seiBuffer);
            int nextPayloadPosition = seiBuffer.getPosition() + payloadSize;
            if (payloadSize == -1 || payloadSize > seiBuffer.bytesLeft()) {
                Log.w(TAG, "Skipping remainder of malformed SEI NAL unit.");
                nextPayloadPosition = seiBuffer.limit();
            } else if (payloadType == 4 && payloadSize >= 8) {
                int countryCode = seiBuffer.readUnsignedByte();
                int providerCode = seiBuffer.readUnsignedShort();
                int userIdentifier = 0;
                if (providerCode == 49) {
                    userIdentifier = seiBuffer.readInt();
                }
                int userDataTypeCode = seiBuffer.readUnsignedByte();
                if (providerCode == PROVIDER_CODE_DIRECTV) {
                    seiBuffer.skipBytes(1);
                }
                boolean messageIsSupportedCeaCaption = countryCode == COUNTRY_CODE && ((providerCode == 49 || providerCode == PROVIDER_CODE_DIRECTV) && userDataTypeCode == 3);
                if (providerCode == 49) {
                    messageIsSupportedCeaCaption &= userIdentifier == USER_DATA_IDENTIFIER_GA94 ? 1 : 0;
                }
                if (messageIsSupportedCeaCaption) {
                    consumeCcData(presentationTimeUs, seiBuffer, outputs);
                }
            }
            seiBuffer.setPosition(nextPayloadPosition);
        }
    }

    public static void consumeCcData(long presentationTimeUs, ParsableByteArray ccDataBuffer, TrackOutput[] outputs) {
        int firstByte = ccDataBuffer.readUnsignedByte();
        if ((firstByte & 64) != 0) {
            int ccCount = firstByte & 31;
            ccDataBuffer.skipBytes(1);
            int sampleLength = ccCount * 3;
            int sampleStartPosition = ccDataBuffer.getPosition();
            for (TrackOutput output : outputs) {
                ccDataBuffer.setPosition(sampleStartPosition);
                output.sampleData(ccDataBuffer, sampleLength);
                output.sampleMetadata(presentationTimeUs, 1, sampleLength, 0, null);
            }
        }
    }

    private static int readNon255TerminatedValue(ParsableByteArray buffer) {
        int value = 0;
        while (buffer.bytesLeft() != 0) {
            int b = buffer.readUnsignedByte();
            value += b;
            if (b != 255) {
                int i = value;
                return value;
            }
        }
        return -1;
    }

    private CeaUtil() {
    }
}
