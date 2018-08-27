package com.google.android.exoplayer2.upstream;

import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ParserException;
import com.google.android.exoplayer2.source.chunk.ChunkedTrackBlacklistUtil;
import com.google.android.exoplayer2.upstream.HttpDataSource.InvalidResponseCodeException;
import com.google.android.exoplayer2.upstream.Loader.Loadable;
import java.io.IOException;

public interface LoadErrorHandlingPolicy<T extends Loadable> {
    public static final LoadErrorHandlingPolicy<Loadable> DEFAULT = new LoadErrorHandlingPolicy<Loadable>() {
        public long getBlacklistDurationMsFor(Loadable loadable, long loadDurationMs, IOException exception, int errorCount) {
            if (!(exception instanceof InvalidResponseCodeException)) {
                return C.TIME_UNSET;
            }
            int responseCode = ((InvalidResponseCodeException) exception).responseCode;
            if (responseCode == 404 || responseCode == 410) {
                return ChunkedTrackBlacklistUtil.DEFAULT_TRACK_BLACKLIST_MS;
            }
            return C.TIME_UNSET;
        }

        public long getRetryDelayMsFor(Loadable loadable, long loadDurationMs, IOException exception, int errorCount) {
            if (exception instanceof ParserException) {
                return C.TIME_UNSET;
            }
            return (long) Math.min((errorCount - 1) * 1000, DefaultLoadControl.DEFAULT_BUFFER_FOR_PLAYBACK_AFTER_REBUFFER_MS);
        }

        public int getMinimumLoadableRetryCount(Loadable loadable) {
            return 3;
        }
    };
    public static final int DEFAULT_MIN_LOADABLE_RETRY_COUNT = 3;

    long getBlacklistDurationMsFor(T t, long j, IOException iOException, int i);

    int getMinimumLoadableRetryCount(T t);

    long getRetryDelayMsFor(T t, long j, IOException iOException, int i);
}
