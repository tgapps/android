package com.google.android.gms.common.api.internal;

import android.os.Handler;
import android.os.Message;
import com.google.android.gms.common.internal.Preconditions;

public final class ListenerHolder<L> {
    private final zza zzlh;
    private volatile L zzli;

    public static final class ListenerKey<L> {
        private final L zzli;
        private final String zzll;

        public final boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (!(obj instanceof ListenerKey)) {
                return false;
            }
            ListenerKey listenerKey = (ListenerKey) obj;
            return this.zzli == listenerKey.zzli && this.zzll.equals(listenerKey.zzll);
        }

        public final int hashCode() {
            return (System.identityHashCode(this.zzli) * 31) + this.zzll.hashCode();
        }
    }

    public interface Notifier<L> {
        void notifyListener(L l);

        void onNotifyListenerFailed();
    }

    private final class zza extends Handler {
        private final /* synthetic */ ListenerHolder zzlk;

        public final void handleMessage(Message message) {
            boolean z = true;
            if (message.what != 1) {
                z = false;
            }
            Preconditions.checkArgument(z);
            this.zzlk.notifyListenerInternal((Notifier) message.obj);
        }
    }

    public final void clear() {
        this.zzli = null;
    }

    public final void notifyListener(Notifier<? super L> notifier) {
        Preconditions.checkNotNull(notifier, "Notifier must not be null");
        this.zzlh.sendMessage(this.zzlh.obtainMessage(1, notifier));
    }

    final void notifyListenerInternal(Notifier<? super L> notifier) {
        Object obj = this.zzli;
        if (obj == null) {
            notifier.onNotifyListenerFailed();
            return;
        }
        try {
            notifier.notifyListener(obj);
        } catch (RuntimeException e) {
            notifier.onNotifyListenerFailed();
            throw e;
        }
    }
}
