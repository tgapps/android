package org.telegram.messenger.exoplayer2.upstream;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.SystemClock;
import android.util.Log;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import org.telegram.messenger.exoplayer2.DefaultLoadControl;
import org.telegram.messenger.exoplayer2.util.Assertions;
import org.telegram.messenger.exoplayer2.util.Util;

public final class Loader implements LoaderErrorThrower {
    public static final int DONT_RETRY = 2;
    public static final int DONT_RETRY_FATAL = 3;
    public static final int RETRY = 0;
    public static final int RETRY_RESET_ERROR_COUNT = 1;
    private LoadTask<? extends Loadable> currentTask;
    private final ExecutorService downloadExecutorService;
    private IOException fatalError;

    public interface Callback<T extends Loadable> {
        void onLoadCanceled(T t, long j, long j2, boolean z);

        void onLoadCompleted(T t, long j, long j2);

        int onLoadError(T t, long j, long j2, IOException iOException);
    }

    @SuppressLint({"HandlerLeak"})
    private final class LoadTask<T extends Loadable> extends Handler implements Runnable {
        private static final int MSG_CANCEL = 1;
        private static final int MSG_END_OF_SOURCE = 2;
        private static final int MSG_FATAL_ERROR = 4;
        private static final int MSG_IO_EXCEPTION = 3;
        private static final int MSG_START = 0;
        private static final String TAG = "LoadTask";
        private final Callback<T> callback;
        private IOException currentError;
        public final int defaultMinRetryCount;
        private int errorCount;
        private volatile Thread executorThread;
        private final T loadable;
        private volatile boolean released;
        private final long startTimeMs;

        public void run() {
            /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.DecodeException: Load method exception in method: org.telegram.messenger.exoplayer2.upstream.Loader.LoadTask.run():void
	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:116)
	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:249)
	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:256)
	at jadx.core.ProcessClass.process(ProcessClass.java:34)
	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:59)
	at jadx.core.ProcessClass.process(ProcessClass.java:42)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:306)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler$1.run(JadxDecompiler.java:199)
Caused by: java.lang.NullPointerException
*/
            /*
            r0 = this;
            r0 = 2;
            r1 = 3;
            r2 = java.lang.Thread.currentThread();	 Catch:{ IOException -> 0x009d, InterruptedException -> 0x008b, Exception -> 0x0072, OutOfMemoryError -> 0x0059, Error -> 0x0044 }
            r4.executorThread = r2;	 Catch:{ IOException -> 0x009d, InterruptedException -> 0x008b, Exception -> 0x0072, OutOfMemoryError -> 0x0059, Error -> 0x0044 }
            r2 = r4.loadable;	 Catch:{ IOException -> 0x009d, InterruptedException -> 0x008b, Exception -> 0x0072, OutOfMemoryError -> 0x0059, Error -> 0x0044 }
            r2 = r2.isLoadCanceled();	 Catch:{ IOException -> 0x009d, InterruptedException -> 0x008b, Exception -> 0x0072, OutOfMemoryError -> 0x0059, Error -> 0x0044 }
            if (r2 != 0) goto L_0x003c;	 Catch:{ IOException -> 0x009d, InterruptedException -> 0x008b, Exception -> 0x0072, OutOfMemoryError -> 0x0059, Error -> 0x0044 }
        L_0x0010:
            r2 = new java.lang.StringBuilder;	 Catch:{ IOException -> 0x009d, InterruptedException -> 0x008b, Exception -> 0x0072, OutOfMemoryError -> 0x0059, Error -> 0x0044 }
            r2.<init>();	 Catch:{ IOException -> 0x009d, InterruptedException -> 0x008b, Exception -> 0x0072, OutOfMemoryError -> 0x0059, Error -> 0x0044 }
            r3 = "load:";	 Catch:{ IOException -> 0x009d, InterruptedException -> 0x008b, Exception -> 0x0072, OutOfMemoryError -> 0x0059, Error -> 0x0044 }
            r2.append(r3);	 Catch:{ IOException -> 0x009d, InterruptedException -> 0x008b, Exception -> 0x0072, OutOfMemoryError -> 0x0059, Error -> 0x0044 }
            r3 = r4.loadable;	 Catch:{ IOException -> 0x009d, InterruptedException -> 0x008b, Exception -> 0x0072, OutOfMemoryError -> 0x0059, Error -> 0x0044 }
            r3 = r3.getClass();	 Catch:{ IOException -> 0x009d, InterruptedException -> 0x008b, Exception -> 0x0072, OutOfMemoryError -> 0x0059, Error -> 0x0044 }
            r3 = r3.getSimpleName();	 Catch:{ IOException -> 0x009d, InterruptedException -> 0x008b, Exception -> 0x0072, OutOfMemoryError -> 0x0059, Error -> 0x0044 }
            r2.append(r3);	 Catch:{ IOException -> 0x009d, InterruptedException -> 0x008b, Exception -> 0x0072, OutOfMemoryError -> 0x0059, Error -> 0x0044 }
            r2 = r2.toString();	 Catch:{ IOException -> 0x009d, InterruptedException -> 0x008b, Exception -> 0x0072, OutOfMemoryError -> 0x0059, Error -> 0x0044 }
            org.telegram.messenger.exoplayer2.util.TraceUtil.beginSection(r2);	 Catch:{ IOException -> 0x009d, InterruptedException -> 0x008b, Exception -> 0x0072, OutOfMemoryError -> 0x0059, Error -> 0x0044 }
            r2 = r4.loadable;	 Catch:{ all -> 0x0037 }
            r2.load();	 Catch:{ all -> 0x0037 }
            org.telegram.messenger.exoplayer2.util.TraceUtil.endSection();	 Catch:{ IOException -> 0x009d, InterruptedException -> 0x008b, Exception -> 0x0072, OutOfMemoryError -> 0x0059, Error -> 0x0044 }
            goto L_0x003c;	 Catch:{ IOException -> 0x009d, InterruptedException -> 0x008b, Exception -> 0x0072, OutOfMemoryError -> 0x0059, Error -> 0x0044 }
        L_0x0037:
            r2 = move-exception;	 Catch:{ IOException -> 0x009d, InterruptedException -> 0x008b, Exception -> 0x0072, OutOfMemoryError -> 0x0059, Error -> 0x0044 }
            org.telegram.messenger.exoplayer2.util.TraceUtil.endSection();	 Catch:{ IOException -> 0x009d, InterruptedException -> 0x008b, Exception -> 0x0072, OutOfMemoryError -> 0x0059, Error -> 0x0044 }
            throw r2;	 Catch:{ IOException -> 0x009d, InterruptedException -> 0x008b, Exception -> 0x0072, OutOfMemoryError -> 0x0059, Error -> 0x0044 }
        L_0x003c:
            r2 = r4.released;	 Catch:{ IOException -> 0x009d, InterruptedException -> 0x008b, Exception -> 0x0072, OutOfMemoryError -> 0x0059, Error -> 0x0044 }
            if (r2 != 0) goto L_0x00a9;	 Catch:{ IOException -> 0x009d, InterruptedException -> 0x008b, Exception -> 0x0072, OutOfMemoryError -> 0x0059, Error -> 0x0044 }
        L_0x0040:
            r4.sendEmptyMessage(r0);	 Catch:{ IOException -> 0x009d, InterruptedException -> 0x008b, Exception -> 0x0072, OutOfMemoryError -> 0x0059, Error -> 0x0044 }
            goto L_0x00a9;
        L_0x0044:
            r0 = move-exception;
            r1 = "LoadTask";
            r2 = "Unexpected error loading stream";
            android.util.Log.e(r1, r2, r0);
            r1 = r4.released;
            if (r1 != 0) goto L_0x0058;
            r1 = 4;
            r1 = r4.obtainMessage(r1, r0);
            r1.sendToTarget();
            throw r0;
        L_0x0059:
            r0 = move-exception;
            r2 = "LoadTask";
            r3 = "OutOfMemory error loading stream";
            android.util.Log.e(r2, r3, r0);
            r2 = r4.released;
            if (r2 != 0) goto L_0x00a9;
            r2 = new org.telegram.messenger.exoplayer2.upstream.Loader$UnexpectedLoaderException;
            r2.<init>(r0);
            r1 = r4.obtainMessage(r1, r2);
            r1.sendToTarget();
            goto L_0x00a9;
        L_0x0072:
            r0 = move-exception;
            r2 = "LoadTask";
            r3 = "Unexpected exception loading stream";
            android.util.Log.e(r2, r3, r0);
            r2 = r4.released;
            if (r2 != 0) goto L_0x00a9;
            r2 = new org.telegram.messenger.exoplayer2.upstream.Loader$UnexpectedLoaderException;
            r2.<init>(r0);
            r1 = r4.obtainMessage(r1, r2);
            r1.sendToTarget();
            goto L_0x00a9;
        L_0x008b:
            r1 = move-exception;
            r2 = r4.loadable;
            r2 = r2.isLoadCanceled();
            org.telegram.messenger.exoplayer2.util.Assertions.checkState(r2);
            r2 = r4.released;
            if (r2 != 0) goto L_0x00a9;
            r4.sendEmptyMessage(r0);
            goto L_0x00a9;
        L_0x009d:
            r0 = move-exception;
            r2 = r4.released;
            if (r2 != 0) goto L_0x00a9;
            r1 = r4.obtainMessage(r1, r0);
            r1.sendToTarget();
            return;
            */
            throw new UnsupportedOperationException("Method not decompiled: org.telegram.messenger.exoplayer2.upstream.Loader.LoadTask.run():void");
        }

        public LoadTask(Looper looper, T loadable, Callback<T> callback, int defaultMinRetryCount, long startTimeMs) {
            super(looper);
            this.loadable = loadable;
            this.callback = callback;
            this.defaultMinRetryCount = defaultMinRetryCount;
            this.startTimeMs = startTimeMs;
        }

        public void maybeThrowError(int minRetryCount) throws IOException {
            if (this.currentError != null && this.errorCount > minRetryCount) {
                throw this.currentError;
            }
        }

        public void start(long delayMillis) {
            Assertions.checkState(Loader.this.currentTask == null);
            Loader.this.currentTask = this;
            if (delayMillis > 0) {
                sendEmptyMessageDelayed(0, delayMillis);
            } else {
                execute();
            }
        }

        public void cancel(boolean released) {
            this.released = released;
            this.currentError = null;
            if (hasMessages(0)) {
                removeMessages(0);
                if (!released) {
                    sendEmptyMessage(1);
                }
            } else {
                this.loadable.cancelLoad();
                if (this.executorThread != null) {
                    this.executorThread.interrupt();
                }
            }
            if (released) {
                finish();
                long nowMs = SystemClock.elapsedRealtime();
                this.callback.onLoadCanceled(this.loadable, nowMs, nowMs - this.startTimeMs, true);
            }
        }

        public void handleMessage(Message msg) {
            if (!this.released) {
                if (msg.what == 0) {
                    execute();
                } else if (msg.what == 4) {
                    throw ((Error) msg.obj);
                } else {
                    finish();
                    long nowMs = SystemClock.elapsedRealtime();
                    long durationMs = nowMs - this.startTimeMs;
                    if (this.loadable.isLoadCanceled()) {
                        this.callback.onLoadCanceled(this.loadable, nowMs, durationMs, false);
                        return;
                    }
                    switch (msg.what) {
                        case 1:
                            this.callback.onLoadCanceled(this.loadable, nowMs, durationMs, false);
                            break;
                        case 2:
                            try {
                                this.callback.onLoadCompleted(this.loadable, nowMs, durationMs);
                                break;
                            } catch (RuntimeException e) {
                                Log.e(TAG, "Unexpected exception handling load completed", e);
                                Loader.this.fatalError = new UnexpectedLoaderException(e);
                                break;
                            }
                        case 3:
                            this.currentError = (IOException) msg.obj;
                            int retryAction = this.callback.onLoadError(this.loadable, nowMs, durationMs, this.currentError);
                            if (retryAction != 3) {
                                if (retryAction != 2) {
                                    int i = 1;
                                    if (retryAction != 1) {
                                        i = 1 + this.errorCount;
                                    }
                                    this.errorCount = i;
                                    start(getRetryDelayMillis());
                                    break;
                                }
                            }
                            Loader.this.fatalError = this.currentError;
                            break;
                            break;
                        default:
                            break;
                    }
                }
            }
        }

        private void execute() {
            this.currentError = null;
            Loader.this.downloadExecutorService.execute(Loader.this.currentTask);
        }

        private void finish() {
            Loader.this.currentTask = null;
        }

        private long getRetryDelayMillis() {
            return (long) Math.min((this.errorCount - 1) * 1000, DefaultLoadControl.DEFAULT_BUFFER_FOR_PLAYBACK_AFTER_REBUFFER_MS);
        }
    }

    public interface Loadable {
        void cancelLoad();

        boolean isLoadCanceled();

        void load() throws IOException, InterruptedException;
    }

    public interface ReleaseCallback {
        void onLoaderReleased();
    }

    private static final class ReleaseTask extends Handler implements Runnable {
        private final ReleaseCallback callback;

        public ReleaseTask(ReleaseCallback callback) {
            this.callback = callback;
        }

        public void run() {
            if (getLooper().getThread().isAlive()) {
                sendEmptyMessage(0);
            }
        }

        public void handleMessage(Message msg) {
            this.callback.onLoaderReleased();
        }
    }

    public static final class UnexpectedLoaderException extends IOException {
        public UnexpectedLoaderException(Throwable cause) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Unexpected ");
            stringBuilder.append(cause.getClass().getSimpleName());
            stringBuilder.append(": ");
            stringBuilder.append(cause.getMessage());
            super(stringBuilder.toString(), cause);
        }
    }

    public Loader(String threadName) {
        this.downloadExecutorService = Util.newSingleThreadExecutor(threadName);
    }

    public <T extends Loadable> long startLoading(T loadable, Callback<T> callback, int defaultMinRetryCount) {
        Looper looper = Looper.myLooper();
        Assertions.checkState(looper != null);
        long startTimeMs = SystemClock.elapsedRealtime();
        new LoadTask(looper, loadable, callback, defaultMinRetryCount, startTimeMs).start(0);
        return startTimeMs;
    }

    public boolean isLoading() {
        return this.currentTask != null;
    }

    public void cancelLoading() {
        this.currentTask.cancel(false);
    }

    public void release() {
        release(null);
    }

    public boolean release(ReleaseCallback callback) {
        boolean callbackInvoked = false;
        if (this.currentTask != null) {
            this.currentTask.cancel(true);
            if (callback != null) {
                this.downloadExecutorService.execute(new ReleaseTask(callback));
            }
        } else if (callback != null) {
            callback.onLoaderReleased();
            callbackInvoked = true;
        }
        this.downloadExecutorService.shutdown();
        return callbackInvoked;
    }

    public void maybeThrowError() throws IOException {
        maybeThrowError(Integer.MIN_VALUE);
    }

    public void maybeThrowError(int minRetryCount) throws IOException {
        if (this.fatalError != null) {
            throw this.fatalError;
        } else if (this.currentTask != null) {
            this.currentTask.maybeThrowError(minRetryCount == Integer.MIN_VALUE ? this.currentTask.defaultMinRetryCount : minRetryCount);
        }
    }
}
