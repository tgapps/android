package com.google.android.exoplayer2.ext.opus;

import com.google.android.exoplayer2.audio.AudioDecoderException;

public final class OpusDecoderException extends AudioDecoderException {
    OpusDecoderException(String message) {
        super(message);
    }

    OpusDecoderException(String message, Throwable cause) {
        super(message, cause);
    }
}
