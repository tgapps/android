package android.support.v4.os;

import android.os.Build.VERSION;

public final class CancellationSignal {
    private boolean mCancelInProgress;
    private Object mCancellationSignalObj;
    private boolean mIsCanceled;
    private OnCancelListener mOnCancelListener;

    public interface OnCancelListener {
        void onCancel();
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void cancel() {
        /*
        r4 = this;
        monitor-enter(r4);
        r2 = r4.mIsCanceled;	 Catch:{ all -> 0x0030 }
        if (r2 == 0) goto L_0x0007;
    L_0x0005:
        monitor-exit(r4);	 Catch:{ all -> 0x0030 }
    L_0x0006:
        return;
    L_0x0007:
        r2 = 1;
        r4.mIsCanceled = r2;	 Catch:{ all -> 0x0030 }
        r2 = 1;
        r4.mCancelInProgress = r2;	 Catch:{ all -> 0x0030 }
        r0 = r4.mOnCancelListener;	 Catch:{ all -> 0x0030 }
        r1 = r4.mCancellationSignalObj;	 Catch:{ all -> 0x0030 }
        monitor-exit(r4);	 Catch:{ all -> 0x0030 }
        if (r0 == 0) goto L_0x0017;
    L_0x0014:
        r0.onCancel();	 Catch:{ all -> 0x0033 }
    L_0x0017:
        if (r1 == 0) goto L_0x0024;
    L_0x0019:
        r2 = android.os.Build.VERSION.SDK_INT;	 Catch:{ all -> 0x0033 }
        r3 = 16;
        if (r2 < r3) goto L_0x0024;
    L_0x001f:
        r1 = (android.os.CancellationSignal) r1;	 Catch:{ all -> 0x0033 }
        r1.cancel();	 Catch:{ all -> 0x0033 }
    L_0x0024:
        monitor-enter(r4);
        r2 = 0;
        r4.mCancelInProgress = r2;	 Catch:{ all -> 0x002d }
        r4.notifyAll();	 Catch:{ all -> 0x002d }
        monitor-exit(r4);	 Catch:{ all -> 0x002d }
        goto L_0x0006;
    L_0x002d:
        r2 = move-exception;
        monitor-exit(r4);	 Catch:{ all -> 0x002d }
        throw r2;
    L_0x0030:
        r2 = move-exception;
        monitor-exit(r4);	 Catch:{ all -> 0x0030 }
        throw r2;
    L_0x0033:
        r2 = move-exception;
        monitor-enter(r4);
        r3 = 0;
        r4.mCancelInProgress = r3;	 Catch:{ all -> 0x003d }
        r4.notifyAll();	 Catch:{ all -> 0x003d }
        monitor-exit(r4);	 Catch:{ all -> 0x003d }
        throw r2;
    L_0x003d:
        r2 = move-exception;
        monitor-exit(r4);	 Catch:{ all -> 0x003d }
        throw r2;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.support.v4.os.CancellationSignal.cancel():void");
    }

    public Object getCancellationSignalObject() {
        if (VERSION.SDK_INT < 16) {
            return null;
        }
        Object obj;
        synchronized (this) {
            if (this.mCancellationSignalObj == null) {
                this.mCancellationSignalObj = new android.os.CancellationSignal();
                if (this.mIsCanceled) {
                    ((android.os.CancellationSignal) this.mCancellationSignalObj).cancel();
                }
            }
            obj = this.mCancellationSignalObj;
        }
        return obj;
    }
}
