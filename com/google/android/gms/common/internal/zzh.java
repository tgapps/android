package com.google.android.gms.common.internal;

import android.content.ComponentName;
import android.content.Context;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.util.Log;
import com.google.android.gms.common.stats.ConnectionTracker;
import java.util.HashMap;
import javax.annotation.concurrent.GuardedBy;
import org.telegram.messenger.exoplayer2.DefaultRenderersFactory;

final class zzh extends GmsClientSupervisor implements Callback {
    private final Handler mHandler;
    private final Context zzau;
    @GuardedBy("mConnectionStatus")
    private final HashMap<ConnectionStatusConfig, zzi> zztr = new HashMap();
    private final ConnectionTracker zzts;
    private final long zztt;
    private final long zztu;

    zzh(Context context) {
        this.zzau = context.getApplicationContext();
        this.mHandler = new Handler(context.getMainLooper(), this);
        this.zzts = ConnectionTracker.getInstance();
        this.zztt = DefaultRenderersFactory.DEFAULT_ALLOWED_VIDEO_JOINING_TIME_MS;
        this.zztu = 300000;
    }

    protected final boolean bindService(ConnectionStatusConfig connectionStatusConfig, ServiceConnection serviceConnection, String str) {
        boolean isBound;
        Preconditions.checkNotNull(serviceConnection, "ServiceConnection must not be null");
        synchronized (this.zztr) {
            zzi com_google_android_gms_common_internal_zzi = (zzi) this.zztr.get(connectionStatusConfig);
            if (com_google_android_gms_common_internal_zzi != null) {
                this.mHandler.removeMessages(0, connectionStatusConfig);
                if (!com_google_android_gms_common_internal_zzi.zza(serviceConnection)) {
                    com_google_android_gms_common_internal_zzi.zza(serviceConnection, str);
                    switch (com_google_android_gms_common_internal_zzi.getState()) {
                        case 1:
                            serviceConnection.onServiceConnected(com_google_android_gms_common_internal_zzi.getComponentName(), com_google_android_gms_common_internal_zzi.getBinder());
                            break;
                        case 2:
                            com_google_android_gms_common_internal_zzi.zzj(str);
                            break;
                        default:
                            break;
                    }
                }
                String valueOf = String.valueOf(connectionStatusConfig);
                throw new IllegalStateException(new StringBuilder(String.valueOf(valueOf).length() + 81).append("Trying to bind a GmsServiceConnection that was already connected before.  config=").append(valueOf).toString());
            }
            com_google_android_gms_common_internal_zzi = new zzi(this, connectionStatusConfig);
            com_google_android_gms_common_internal_zzi.zza(serviceConnection, str);
            com_google_android_gms_common_internal_zzi.zzj(str);
            this.zztr.put(connectionStatusConfig, com_google_android_gms_common_internal_zzi);
            isBound = com_google_android_gms_common_internal_zzi.isBound();
        }
        return isBound;
    }

    public final boolean handleMessage(Message message) {
        ConnectionStatusConfig connectionStatusConfig;
        zzi com_google_android_gms_common_internal_zzi;
        switch (message.what) {
            case 0:
                synchronized (this.zztr) {
                    connectionStatusConfig = (ConnectionStatusConfig) message.obj;
                    com_google_android_gms_common_internal_zzi = (zzi) this.zztr.get(connectionStatusConfig);
                    if (com_google_android_gms_common_internal_zzi != null && com_google_android_gms_common_internal_zzi.zzcv()) {
                        if (com_google_android_gms_common_internal_zzi.isBound()) {
                            com_google_android_gms_common_internal_zzi.zzk("GmsClientSupervisor");
                        }
                        this.zztr.remove(connectionStatusConfig);
                    }
                }
                return true;
            case 1:
                synchronized (this.zztr) {
                    connectionStatusConfig = (ConnectionStatusConfig) message.obj;
                    com_google_android_gms_common_internal_zzi = (zzi) this.zztr.get(connectionStatusConfig);
                    if (com_google_android_gms_common_internal_zzi != null && com_google_android_gms_common_internal_zzi.getState() == 3) {
                        String valueOf = String.valueOf(connectionStatusConfig);
                        Log.wtf("GmsClientSupervisor", new StringBuilder(String.valueOf(valueOf).length() + 47).append("Timeout waiting for ServiceConnection callback ").append(valueOf).toString(), new Exception());
                        ComponentName componentName = com_google_android_gms_common_internal_zzi.getComponentName();
                        if (componentName == null) {
                            componentName = connectionStatusConfig.getComponentName();
                        }
                        com_google_android_gms_common_internal_zzi.onServiceDisconnected(componentName == null ? new ComponentName(connectionStatusConfig.getPackage(), "unknown") : componentName);
                    }
                }
                return true;
            default:
                return false;
        }
    }

    protected final void unbindService(ConnectionStatusConfig connectionStatusConfig, ServiceConnection serviceConnection, String str) {
        Preconditions.checkNotNull(serviceConnection, "ServiceConnection must not be null");
        synchronized (this.zztr) {
            zzi com_google_android_gms_common_internal_zzi = (zzi) this.zztr.get(connectionStatusConfig);
            String valueOf;
            if (com_google_android_gms_common_internal_zzi == null) {
                valueOf = String.valueOf(connectionStatusConfig);
                throw new IllegalStateException(new StringBuilder(String.valueOf(valueOf).length() + 50).append("Nonexistent connection status for service config: ").append(valueOf).toString());
            } else if (com_google_android_gms_common_internal_zzi.zza(serviceConnection)) {
                com_google_android_gms_common_internal_zzi.zzb(serviceConnection, str);
                if (com_google_android_gms_common_internal_zzi.zzcv()) {
                    this.mHandler.sendMessageDelayed(this.mHandler.obtainMessage(0, connectionStatusConfig), this.zztt);
                }
            } else {
                valueOf = String.valueOf(connectionStatusConfig);
                throw new IllegalStateException(new StringBuilder(String.valueOf(valueOf).length() + 76).append("Trying to unbind a GmsServiceConnection  that was not bound before.  config=").append(valueOf).toString());
            }
        }
    }
}
