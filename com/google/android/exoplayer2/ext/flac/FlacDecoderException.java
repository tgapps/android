package com.google.android.exoplayer2.ext.flac;

import com.google.android.exoplayer2.audio.AudioDecoderException;

public final class FlacDecoderException extends AudioDecoderException {
    FlacDecoderException(String message) {
        super(message);
    }

    FlacDecoderException(String message, Throwable cause) {
        super(message, cause);
    }
}
