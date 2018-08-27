package com.google.android.exoplayer2.audio;

public class AudioDecoderException extends Exception {
    public AudioDecoderException(String message) {
        super(message);
    }

    public AudioDecoderException(String message, Throwable cause) {
        super(message, cause);
    }
}
