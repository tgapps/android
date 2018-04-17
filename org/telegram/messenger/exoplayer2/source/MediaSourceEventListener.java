package org.telegram.messenger.exoplayer2.source;

import android.os.Handler;
import java.io.IOException;
import org.telegram.messenger.exoplayer2.C;
import org.telegram.messenger.exoplayer2.Format;
import org.telegram.messenger.exoplayer2.upstream.DataSpec;
import org.telegram.messenger.exoplayer2.util.Assertions;

public interface MediaSourceEventListener {

    public static final class EventDispatcher {
        private final Handler handler;
        private final MediaSourceEventListener listener;
        private final long mediaTimeOffsetMs;

        public EventDispatcher(Handler handler, MediaSourceEventListener listener) {
            this(handler, listener, 0);
        }

        public EventDispatcher(Handler handler, MediaSourceEventListener listener, long mediaTimeOffsetMs) {
            this.handler = listener != null ? (Handler) Assertions.checkNotNull(handler) : null;
            this.listener = listener;
            this.mediaTimeOffsetMs = mediaTimeOffsetMs;
        }

        public EventDispatcher copyWithMediaTimeOffsetMs(long mediaTimeOffsetMs) {
            return new EventDispatcher(this.handler, this.listener, mediaTimeOffsetMs);
        }

        public void loadStarted(DataSpec dataSpec, int dataType, long elapsedRealtimeMs) {
            loadStarted(dataSpec, dataType, -1, null, 0, null, C.TIME_UNSET, C.TIME_UNSET, elapsedRealtimeMs);
        }

        public void loadStarted(DataSpec dataSpec, int dataType, int trackType, Format trackFormat, int trackSelectionReason, Object trackSelectionData, long mediaStartTimeUs, long mediaEndTimeUs, long elapsedRealtimeMs) {
            if (this.listener != null && r14.handler != null) {
                Handler handler = r14.handler;
                final DataSpec dataSpec2 = dataSpec;
                final int i = dataType;
                final int i2 = trackType;
                final Format format = trackFormat;
                final int i3 = trackSelectionReason;
                final Object obj = trackSelectionData;
                final long j = mediaStartTimeUs;
                final long j2 = mediaEndTimeUs;
                AnonymousClass1 anonymousClass1 = r0;
                final long j3 = elapsedRealtimeMs;
                AnonymousClass1 anonymousClass12 = new Runnable() {
                    public void run() {
                        EventDispatcher.this.listener.onLoadStarted(dataSpec2, i, i2, format, i3, obj, EventDispatcher.this.adjustMediaTime(j), EventDispatcher.this.adjustMediaTime(j2), j3);
                    }
                };
                handler.post(anonymousClass1);
            }
        }

        public void loadCompleted(DataSpec dataSpec, int dataType, long elapsedRealtimeMs, long loadDurationMs, long bytesLoaded) {
            loadCompleted(dataSpec, dataType, -1, null, 0, null, C.TIME_UNSET, C.TIME_UNSET, elapsedRealtimeMs, loadDurationMs, bytesLoaded);
        }

        public void loadCompleted(DataSpec dataSpec, int dataType, int trackType, Format trackFormat, int trackSelectionReason, Object trackSelectionData, long mediaStartTimeUs, long mediaEndTimeUs, long elapsedRealtimeMs, long loadDurationMs, long bytesLoaded) {
            if (this.listener != null && r14.handler != null) {
                final DataSpec dataSpec2 = dataSpec;
                final int i = dataType;
                final int i2 = trackType;
                final Format format = trackFormat;
                final int i3 = trackSelectionReason;
                final Object obj = trackSelectionData;
                final long j = mediaStartTimeUs;
                final long j2 = mediaEndTimeUs;
                AnonymousClass2 anonymousClass2 = r0;
                final long j3 = elapsedRealtimeMs;
                Handler handler = r14.handler;
                final long j4 = loadDurationMs;
                final long j5 = bytesLoaded;
                AnonymousClass2 anonymousClass22 = new Runnable() {
                    public void run() {
                        MediaSourceEventListener access$100 = EventDispatcher.this.listener;
                        DataSpec dataSpec = dataSpec2;
                        int i = i;
                        int i2 = i2;
                        Format format = format;
                        int i3 = i3;
                        Object obj = obj;
                        long access$000 = EventDispatcher.this.adjustMediaTime(j);
                        long access$0002 = EventDispatcher.this.adjustMediaTime(j2);
                        long j = j3;
                        long j2 = j;
                        access$100.onLoadCompleted(dataSpec, i, i2, format, i3, obj, access$000, access$0002, j2, j4, j5);
                    }
                };
                handler.post(anonymousClass2);
            }
        }

        public void loadCanceled(DataSpec dataSpec, int dataType, long elapsedRealtimeMs, long loadDurationMs, long bytesLoaded) {
            loadCanceled(dataSpec, dataType, -1, null, 0, null, C.TIME_UNSET, C.TIME_UNSET, elapsedRealtimeMs, loadDurationMs, bytesLoaded);
        }

        public void loadCanceled(DataSpec dataSpec, int dataType, int trackType, Format trackFormat, int trackSelectionReason, Object trackSelectionData, long mediaStartTimeUs, long mediaEndTimeUs, long elapsedRealtimeMs, long loadDurationMs, long bytesLoaded) {
            if (this.listener != null && r14.handler != null) {
                final DataSpec dataSpec2 = dataSpec;
                final int i = dataType;
                final int i2 = trackType;
                final Format format = trackFormat;
                final int i3 = trackSelectionReason;
                final Object obj = trackSelectionData;
                final long j = mediaStartTimeUs;
                final long j2 = mediaEndTimeUs;
                AnonymousClass3 anonymousClass3 = r0;
                final long j3 = elapsedRealtimeMs;
                Handler handler = r14.handler;
                final long j4 = loadDurationMs;
                final long j5 = bytesLoaded;
                AnonymousClass3 anonymousClass32 = new Runnable() {
                    public void run() {
                        MediaSourceEventListener access$100 = EventDispatcher.this.listener;
                        DataSpec dataSpec = dataSpec2;
                        int i = i;
                        int i2 = i2;
                        Format format = format;
                        int i3 = i3;
                        Object obj = obj;
                        long access$000 = EventDispatcher.this.adjustMediaTime(j);
                        long access$0002 = EventDispatcher.this.adjustMediaTime(j2);
                        long j = j3;
                        long j2 = j;
                        access$100.onLoadCanceled(dataSpec, i, i2, format, i3, obj, access$000, access$0002, j2, j4, j5);
                    }
                };
                handler.post(anonymousClass3);
            }
        }

        public void loadError(DataSpec dataSpec, int dataType, long elapsedRealtimeMs, long loadDurationMs, long bytesLoaded, IOException error, boolean wasCanceled) {
            loadError(dataSpec, dataType, -1, null, 0, null, C.TIME_UNSET, C.TIME_UNSET, elapsedRealtimeMs, loadDurationMs, bytesLoaded, error, wasCanceled);
        }

        public void loadError(DataSpec dataSpec, int dataType, int trackType, Format trackFormat, int trackSelectionReason, Object trackSelectionData, long mediaStartTimeUs, long mediaEndTimeUs, long elapsedRealtimeMs, long loadDurationMs, long bytesLoaded, IOException error, boolean wasCanceled) {
            if (this.listener != null && r14.handler != null) {
                final DataSpec dataSpec2 = dataSpec;
                final int i = dataType;
                final int i2 = trackType;
                final Format format = trackFormat;
                final int i3 = trackSelectionReason;
                final Object obj = trackSelectionData;
                final long j = mediaStartTimeUs;
                final long j2 = mediaEndTimeUs;
                AnonymousClass4 anonymousClass4 = r0;
                final long j3 = elapsedRealtimeMs;
                Handler handler = r14.handler;
                final long j4 = loadDurationMs;
                final long j5 = bytesLoaded;
                final IOException iOException = error;
                final boolean z = wasCanceled;
                AnonymousClass4 anonymousClass42 = new Runnable() {
                    public void run() {
                        MediaSourceEventListener access$100 = EventDispatcher.this.listener;
                        DataSpec dataSpec = dataSpec2;
                        int i = i;
                        int i2 = i2;
                        Format format = format;
                        int i3 = i3;
                        Object obj = obj;
                        long access$000 = EventDispatcher.this.adjustMediaTime(j);
                        long access$0002 = EventDispatcher.this.adjustMediaTime(j2);
                        long j = j3;
                        long j2 = j4;
                        long j3 = j5;
                        long j4 = j3;
                        access$100.onLoadError(dataSpec, i, i2, format, i3, obj, access$000, access$0002, j, j2, j4, iOException, z);
                    }
                };
                handler.post(anonymousClass4);
            }
        }

        public void upstreamDiscarded(int trackType, long mediaStartTimeUs, long mediaEndTimeUs) {
            if (this.listener != null && this.handler != null) {
                final int i = trackType;
                final long j = mediaStartTimeUs;
                final long j2 = mediaEndTimeUs;
                this.handler.post(new Runnable() {
                    public void run() {
                        EventDispatcher.this.listener.onUpstreamDiscarded(i, EventDispatcher.this.adjustMediaTime(j), EventDispatcher.this.adjustMediaTime(j2));
                    }
                });
            }
        }

        public void downstreamFormatChanged(int trackType, Format trackFormat, int trackSelectionReason, Object trackSelectionData, long mediaTimeUs) {
            if (this.listener != null && r8.handler != null) {
                final int i = trackType;
                final Format format = trackFormat;
                final int i2 = trackSelectionReason;
                final Object obj = trackSelectionData;
                final long j = mediaTimeUs;
                r8.handler.post(new Runnable() {
                    public void run() {
                        EventDispatcher.this.listener.onDownstreamFormatChanged(i, format, i2, obj, EventDispatcher.this.adjustMediaTime(j));
                    }
                });
            }
        }

        private long adjustMediaTime(long mediaTimeUs) {
            long mediaTimeMs = C.usToMs(mediaTimeUs);
            return mediaTimeMs == C.TIME_UNSET ? C.TIME_UNSET : this.mediaTimeOffsetMs + mediaTimeMs;
        }
    }

    void onDownstreamFormatChanged(int i, Format format, int i2, Object obj, long j);

    void onLoadCanceled(DataSpec dataSpec, int i, int i2, Format format, int i3, Object obj, long j, long j2, long j3, long j4, long j5);

    void onLoadCompleted(DataSpec dataSpec, int i, int i2, Format format, int i3, Object obj, long j, long j2, long j3, long j4, long j5);

    void onLoadError(DataSpec dataSpec, int i, int i2, Format format, int i3, Object obj, long j, long j2, long j3, long j4, long j5, IOException iOException, boolean z);

    void onLoadStarted(DataSpec dataSpec, int i, int i2, Format format, int i3, Object obj, long j, long j2, long j3);

    void onUpstreamDiscarded(int i, long j, long j2);
}
