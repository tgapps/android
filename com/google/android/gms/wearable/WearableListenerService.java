package com.google.android.gms.wearable;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Binder;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import com.google.android.gms.common.data.DataHolder;
import com.google.android.gms.common.util.UidVerifier;
import com.google.android.gms.wearable.CapabilityApi.CapabilityListener;
import com.google.android.gms.wearable.ChannelApi.ChannelListener;
import com.google.android.gms.wearable.ChannelClient.Channel;
import com.google.android.gms.wearable.ChannelClient.ChannelCallback;
import com.google.android.gms.wearable.DataApi.DataListener;
import com.google.android.gms.wearable.MessageApi.MessageListener;
import com.google.android.gms.wearable.internal.zzah;
import com.google.android.gms.wearable.internal.zzas;
import com.google.android.gms.wearable.internal.zzaw;
import com.google.android.gms.wearable.internal.zzen;
import com.google.android.gms.wearable.internal.zzfe;
import com.google.android.gms.wearable.internal.zzfo;
import com.google.android.gms.wearable.internal.zzhp;
import com.google.android.gms.wearable.internal.zzi;
import com.google.android.gms.wearable.internal.zzl;
import java.util.List;

public class WearableListenerService extends Service implements CapabilityListener, ChannelListener, DataListener, MessageListener {
    public static final String BIND_LISTENER_INTENT_ACTION = "com.google.android.gms.wearable.BIND_LISTENER";
    private ComponentName service;
    private zzc zzad;
    private IBinder zzae;
    private Intent zzaf;
    private Looper zzag;
    private final Object zzah = new Object();
    private boolean zzai;
    private zzas zzaj = new zzas(new zza());

    class zzb implements ServiceConnection {
        private zzb(WearableListenerService wearableListenerService) {
        }

        public final void onServiceConnected(ComponentName componentName, IBinder iBinder) {
        }

        public final void onServiceDisconnected(ComponentName componentName) {
        }
    }

    final class zzc extends Handler {
        private boolean started;
        private final /* synthetic */ WearableListenerService zzak;
        private final zzb zzal = new zzb();

        zzc(WearableListenerService wearableListenerService, Looper looper) {
            this.zzak = wearableListenerService;
            super(looper);
        }

        @SuppressLint({"UntrackedBindService"})
        private final synchronized void zzb() {
            if (!this.started) {
                if (Log.isLoggable("WearableLS", 2)) {
                    String valueOf = String.valueOf(this.zzak.service);
                    StringBuilder stringBuilder = new StringBuilder(13 + String.valueOf(valueOf).length());
                    stringBuilder.append("bindService: ");
                    stringBuilder.append(valueOf);
                    Log.v("WearableLS", stringBuilder.toString());
                }
                this.zzak.bindService(this.zzak.zzaf, this.zzal, 1);
                this.started = true;
            }
        }

        @SuppressLint({"UntrackedBindService"})
        private final synchronized void zzb(String str) {
            if (this.started) {
                if (Log.isLoggable("WearableLS", 2)) {
                    String valueOf = String.valueOf(this.zzak.service);
                    StringBuilder stringBuilder = new StringBuilder((17 + String.valueOf(str).length()) + String.valueOf(valueOf).length());
                    stringBuilder.append("unbindService: ");
                    stringBuilder.append(str);
                    stringBuilder.append(", ");
                    stringBuilder.append(valueOf);
                    Log.v("WearableLS", stringBuilder.toString());
                }
                try {
                    this.zzak.unbindService(this.zzal);
                } catch (Throwable e) {
                    Log.e("WearableLS", "Exception when unbinding from local service", e);
                }
                this.started = false;
            }
        }

        public final void dispatchMessage(Message message) {
            zzb();
            try {
                super.dispatchMessage(message);
            } finally {
                if (!hasMessages(0)) {
                    zzb("dispatch");
                }
            }
        }

        final void quit() {
            getLooper().quit();
            zzb("quit");
        }
    }

    class zza extends ChannelCallback {
        private final /* synthetic */ WearableListenerService zzak;

        private zza(WearableListenerService wearableListenerService) {
            this.zzak = wearableListenerService;
        }

        public final void onChannelClosed(Channel channel, int i, int i2) {
            this.zzak.onChannelClosed(channel, i, i2);
        }

        public final void onChannelOpened(Channel channel) {
            this.zzak.onChannelOpened(channel);
        }

        public final void onInputClosed(Channel channel, int i, int i2) {
            this.zzak.onInputClosed(channel, i, i2);
        }

        public final void onOutputClosed(Channel channel, int i, int i2) {
            this.zzak.onOutputClosed(channel, i, i2);
        }
    }

    final class zzd extends zzen {
        final /* synthetic */ WearableListenerService zzak;
        private volatile int zzam;

        private zzd(WearableListenerService wearableListenerService) {
            this.zzak = wearableListenerService;
            this.zzam = -1;
        }

        private final boolean zza(Runnable runnable, String str, Object obj) {
            boolean z;
            if (Log.isLoggable("WearableLS", 3)) {
                Log.d("WearableLS", String.format("%s: %s %s", new Object[]{str, this.zzak.service.toString(), obj}));
            }
            int callingUid = Binder.getCallingUid();
            if (callingUid != this.zzam) {
                if (!(zzhp.zza(this.zzak).zze("com.google.android.wearable.app.cn") && UidVerifier.uidHasPackageName(this.zzak, callingUid, "com.google.android.wearable.app.cn"))) {
                    if (!UidVerifier.isGooglePlayServicesUid(this.zzak, callingUid)) {
                        StringBuilder stringBuilder = new StringBuilder(57);
                        stringBuilder.append("Caller is not GooglePlayServices; caller UID: ");
                        stringBuilder.append(callingUid);
                        Log.e("WearableLS", stringBuilder.toString());
                        z = false;
                        if (!z) {
                            return false;
                        }
                        synchronized (this.zzak.zzah) {
                            if (this.zzak.zzai) {
                                this.zzak.zzad.post(runnable);
                                return true;
                            }
                            return false;
                        }
                    }
                }
                this.zzam = callingUid;
            }
            z = true;
            if (!z) {
                return false;
            }
            synchronized (this.zzak.zzah) {
                if (this.zzak.zzai) {
                    this.zzak.zzad.post(runnable);
                    return true;
                }
                return false;
            }
        }

        public final void onConnectedNodes(List<zzfo> list) {
            zza(new zzp(this, list), "onConnectedNodes", list);
        }

        public final void zza(DataHolder dataHolder) {
            Runnable com_google_android_gms_wearable_zzl = new zzl(this, dataHolder);
            try {
                String valueOf = String.valueOf(dataHolder);
                int count = dataHolder.getCount();
                StringBuilder stringBuilder = new StringBuilder(18 + String.valueOf(valueOf).length());
                stringBuilder.append(valueOf);
                stringBuilder.append(", rows=");
                stringBuilder.append(count);
                if (!zza(com_google_android_gms_wearable_zzl, "onDataItemChanged", stringBuilder.toString())) {
                }
            } finally {
                dataHolder.close();
            }
        }

        public final void zza(zzah com_google_android_gms_wearable_internal_zzah) {
            zza(new zzq(this, com_google_android_gms_wearable_internal_zzah), "onConnectedCapabilityChanged", com_google_android_gms_wearable_internal_zzah);
        }

        public final void zza(zzaw com_google_android_gms_wearable_internal_zzaw) {
            zza(new zzt(this, com_google_android_gms_wearable_internal_zzaw), "onChannelEvent", com_google_android_gms_wearable_internal_zzaw);
        }

        public final void zza(zzfe com_google_android_gms_wearable_internal_zzfe) {
            zza(new zzm(this, com_google_android_gms_wearable_internal_zzfe), "onMessageReceived", com_google_android_gms_wearable_internal_zzfe);
        }

        public final void zza(zzfo com_google_android_gms_wearable_internal_zzfo) {
            zza(new zzn(this, com_google_android_gms_wearable_internal_zzfo), "onPeerConnected", com_google_android_gms_wearable_internal_zzfo);
        }

        public final void zza(zzi com_google_android_gms_wearable_internal_zzi) {
            zza(new zzs(this, com_google_android_gms_wearable_internal_zzi), "onEntityUpdate", com_google_android_gms_wearable_internal_zzi);
        }

        public final void zza(zzl com_google_android_gms_wearable_internal_zzl) {
            zza(new zzr(this, com_google_android_gms_wearable_internal_zzl), "onNotificationReceived", com_google_android_gms_wearable_internal_zzl);
        }

        public final void zzb(zzfo com_google_android_gms_wearable_internal_zzfo) {
            zza(new zzo(this, com_google_android_gms_wearable_internal_zzfo), "onPeerDisconnected", com_google_android_gms_wearable_internal_zzfo);
        }
    }

    public Looper getLooper() {
        if (this.zzag == null) {
            HandlerThread handlerThread = new HandlerThread("WearableListenerService");
            handlerThread.start();
            this.zzag = handlerThread.getLooper();
        }
        return this.zzag;
    }

    public final IBinder onBind(Intent intent) {
        return BIND_LISTENER_INTENT_ACTION.equals(intent.getAction()) ? this.zzae : null;
    }

    public void onCapabilityChanged(CapabilityInfo capabilityInfo) {
    }

    public void onChannelClosed(Channel channel, int i, int i2) {
    }

    public void onChannelClosed(Channel channel, int i, int i2) {
    }

    public void onChannelOpened(Channel channel) {
    }

    public void onChannelOpened(Channel channel) {
    }

    public void onConnectedNodes(List<Node> list) {
    }

    public void onCreate() {
        super.onCreate();
        this.service = new ComponentName(this, getClass().getName());
        if (Log.isLoggable("WearableLS", 3)) {
            String valueOf = String.valueOf(this.service);
            StringBuilder stringBuilder = new StringBuilder(10 + String.valueOf(valueOf).length());
            stringBuilder.append("onCreate: ");
            stringBuilder.append(valueOf);
            Log.d("WearableLS", stringBuilder.toString());
        }
        this.zzad = new zzc(this, getLooper());
        this.zzaf = new Intent(BIND_LISTENER_INTENT_ACTION);
        this.zzaf.setComponent(this.service);
        this.zzae = new zzd();
    }

    public void onDataChanged(DataEventBuffer dataEventBuffer) {
    }

    public void onDestroy() {
        if (Log.isLoggable("WearableLS", 3)) {
            String valueOf = String.valueOf(this.service);
            StringBuilder stringBuilder = new StringBuilder(11 + String.valueOf(valueOf).length());
            stringBuilder.append("onDestroy: ");
            stringBuilder.append(valueOf);
            Log.d("WearableLS", stringBuilder.toString());
        }
        synchronized (this.zzah) {
            this.zzai = true;
            if (this.zzad == null) {
                String valueOf2 = String.valueOf(this.service);
                StringBuilder stringBuilder2 = new StringBuilder(111 + String.valueOf(valueOf2).length());
                stringBuilder2.append("onDestroy: mServiceHandler not set, did you override onCreate() but forget to call super.onCreate()? component=");
                stringBuilder2.append(valueOf2);
                throw new IllegalStateException(stringBuilder2.toString());
            }
            this.zzad.quit();
        }
        super.onDestroy();
    }

    public void onEntityUpdate(zzb com_google_android_gms_wearable_zzb) {
    }

    public void onInputClosed(Channel channel, int i, int i2) {
    }

    public void onInputClosed(Channel channel, int i, int i2) {
    }

    public void onMessageReceived(MessageEvent messageEvent) {
    }

    public void onNotificationReceived(zzd com_google_android_gms_wearable_zzd) {
    }

    public void onOutputClosed(Channel channel, int i, int i2) {
    }

    public void onOutputClosed(Channel channel, int i, int i2) {
    }

    public void onPeerConnected(Node node) {
    }

    public void onPeerDisconnected(Node node) {
    }
}
