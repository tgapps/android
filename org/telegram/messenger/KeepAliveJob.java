package org.telegram.messenger;

import android.content.Intent;
import java.util.concurrent.CountDownLatch;
import org.telegram.messenger.support.JobIntentService;

public class KeepAliveJob extends JobIntentService {
    private static volatile CountDownLatch countDownLatch;
    private static volatile boolean startingJob;
    private static final Object sync = new Object();

    public static void startJob() {
        Utilities.globalQueue.postRunnable(new Runnable() {
            public void run() {
                if (!KeepAliveJob.startingJob && KeepAliveJob.countDownLatch == null) {
                    try {
                        if (BuildVars.LOGS_ENABLED) {
                            FileLog.d("starting keep-alive job");
                        }
                        synchronized (KeepAliveJob.sync) {
                            KeepAliveJob.startingJob = true;
                        }
                        JobIntentService.enqueueWork(ApplicationLoader.applicationContext, KeepAliveJob.class, 1000, new Intent());
                    } catch (Exception e) {
                    }
                }
            }
        });
    }

    public static void finishJob() {
        Utilities.globalQueue.postRunnable(new Runnable() {
            public void run() {
                synchronized (KeepAliveJob.sync) {
                    if (KeepAliveJob.countDownLatch != null) {
                        if (BuildVars.LOGS_ENABLED) {
                            FileLog.d("finish keep-alive job");
                        }
                        KeepAliveJob.countDownLatch.countDown();
                    }
                    if (KeepAliveJob.startingJob) {
                        if (BuildVars.LOGS_ENABLED) {
                            FileLog.d("finish queued keep-alive job");
                        }
                        KeepAliveJob.startingJob = false;
                    }
                }
            }
        });
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    protected void onHandleWork(android.content.Intent r4) {
        /*
        r3 = this;
        r1 = sync;
        monitor-enter(r1);
        r0 = startingJob;	 Catch:{ all -> 0x0033 }
        if (r0 != 0) goto L_0x0009;
    L_0x0007:
        monitor-exit(r1);	 Catch:{ all -> 0x0033 }
    L_0x0008:
        return;
    L_0x0009:
        r0 = new java.util.concurrent.CountDownLatch;	 Catch:{ all -> 0x0033 }
        r2 = 1;
        r0.<init>(r2);	 Catch:{ all -> 0x0033 }
        countDownLatch = r0;	 Catch:{ all -> 0x0033 }
        monitor-exit(r1);	 Catch:{ all -> 0x0033 }
        r0 = org.telegram.messenger.BuildVars.LOGS_ENABLED;
        if (r0 == 0) goto L_0x001c;
    L_0x0016:
        r0 = "started keep-alive job";
        org.telegram.messenger.FileLog.d(r0);
    L_0x001c:
        r0 = countDownLatch;	 Catch:{ Throwable -> 0x0039 }
        r0.await();	 Catch:{ Throwable -> 0x0039 }
    L_0x0021:
        r1 = sync;
        monitor-enter(r1);
        r0 = 0;
        countDownLatch = r0;	 Catch:{ all -> 0x0036 }
        monitor-exit(r1);	 Catch:{ all -> 0x0036 }
        r0 = org.telegram.messenger.BuildVars.LOGS_ENABLED;
        if (r0 == 0) goto L_0x0008;
    L_0x002c:
        r0 = "ended keep-alive job";
        org.telegram.messenger.FileLog.d(r0);
        goto L_0x0008;
    L_0x0033:
        r0 = move-exception;
        monitor-exit(r1);	 Catch:{ all -> 0x0033 }
        throw r0;
    L_0x0036:
        r0 = move-exception;
        monitor-exit(r1);	 Catch:{ all -> 0x0036 }
        throw r0;
    L_0x0039:
        r0 = move-exception;
        goto L_0x0021;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.telegram.messenger.KeepAliveJob.onHandleWork(android.content.Intent):void");
    }
}
