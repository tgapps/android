package com.google.firebase.iid;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;
import android.util.SparseArray;
import com.google.android.gms.common.internal.Preconditions;
import com.google.android.gms.common.stats.ConnectionTracker;
import java.util.ArrayDeque;
import java.util.Queue;
import java.util.concurrent.TimeUnit;
import javax.annotation.concurrent.GuardedBy;

final class zzm implements ServiceConnection {
    @GuardedBy("this")
    int state;
    final Messenger zzbqz;
    zzr zzbra;
    @GuardedBy("this")
    final Queue<zzt<?>> zzbrb;
    @GuardedBy("this")
    final SparseArray<zzt<?>> zzbrc;
    final /* synthetic */ zzk zzbrd;

    private zzm(zzk com_google_firebase_iid_zzk) {
        this.zzbrd = com_google_firebase_iid_zzk;
        this.state = 0;
        this.zzbqz = new Messenger(new Handler(Looper.getMainLooper(), new zzn(this)));
        this.zzbrb = new ArrayDeque();
        this.zzbrc = new SparseArray();
    }

    private final void zzsq() {
        this.zzbrd.zzbqw.execute(new zzp(this));
    }

    public final synchronized void onServiceConnected(ComponentName componentName, IBinder iBinder) {
        if (Log.isLoggable("MessengerIpcClient", 2)) {
            Log.v("MessengerIpcClient", "Service connected");
        }
        if (iBinder == null) {
            zzb(0, "Null service connection");
        } else {
            try {
                this.zzbra = new zzr(iBinder);
                this.state = 2;
                zzsq();
            } catch (RemoteException e) {
                zzb(0, e.getMessage());
            }
        }
    }

    public final synchronized void onServiceDisconnected(ComponentName componentName) {
        if (Log.isLoggable("MessengerIpcClient", 2)) {
            Log.v("MessengerIpcClient", "Service disconnected");
        }
        zzb(2, "Service disconnected");
    }

    final boolean zza(Message message) {
        int i = message.arg1;
        if (Log.isLoggable("MessengerIpcClient", 3)) {
            Log.d("MessengerIpcClient", "Received response to request: " + i);
        }
        synchronized (this) {
            zzt com_google_firebase_iid_zzt = (zzt) this.zzbrc.get(i);
            if (com_google_firebase_iid_zzt == null) {
                Log.w("MessengerIpcClient", "Received response for unknown request: " + i);
            } else {
                this.zzbrc.remove(i);
                zzsr();
                Bundle data = message.getData();
                if (data.getBoolean("unsupported", false)) {
                    com_google_firebase_iid_zzt.zza(new zzu(4, "Not supported by GmsCore"));
                } else {
                    com_google_firebase_iid_zzt.zzh(data);
                }
            }
        }
        return true;
    }

    final synchronized void zzae(int i) {
        zzt com_google_firebase_iid_zzt = (zzt) this.zzbrc.get(i);
        if (com_google_firebase_iid_zzt != null) {
            Log.w("MessengerIpcClient", "Timing out request: " + i);
            this.zzbrc.remove(i);
            com_google_firebase_iid_zzt.zza(new zzu(3, "Timed out waiting for response"));
            zzsr();
        }
    }

    final synchronized void zzb(int i, String str) {
        if (Log.isLoggable("MessengerIpcClient", 3)) {
            String str2 = "MessengerIpcClient";
            String str3 = "Disconnected: ";
            String valueOf = String.valueOf(str);
            Log.d(str2, valueOf.length() != 0 ? str3.concat(valueOf) : new String(str3));
        }
        switch (this.state) {
            case 0:
                throw new IllegalStateException();
            case 1:
            case 2:
                if (Log.isLoggable("MessengerIpcClient", 2)) {
                    Log.v("MessengerIpcClient", "Unbinding service");
                }
                this.state = 4;
                ConnectionTracker.getInstance().unbindService(this.zzbrd.zzqs, this);
                zzu com_google_firebase_iid_zzu = new zzu(i, str);
                for (zzt zza : this.zzbrb) {
                    zza.zza(com_google_firebase_iid_zzu);
                }
                this.zzbrb.clear();
                for (int i2 = 0; i2 < this.zzbrc.size(); i2++) {
                    ((zzt) this.zzbrc.valueAt(i2)).zza(com_google_firebase_iid_zzu);
                }
                this.zzbrc.clear();
                break;
            case 3:
                this.state = 4;
                break;
            case 4:
                break;
            default:
                throw new IllegalStateException("Unknown state: " + this.state);
        }
    }

    final synchronized boolean zzb(zzt com_google_firebase_iid_zzt) {
        boolean z = false;
        boolean z2 = true;
        synchronized (this) {
            switch (this.state) {
                case 0:
                    this.zzbrb.add(com_google_firebase_iid_zzt);
                    if (this.state == 0) {
                        z = true;
                    }
                    Preconditions.checkState(z);
                    if (Log.isLoggable("MessengerIpcClient", 2)) {
                        Log.v("MessengerIpcClient", "Starting bind to GmsCore");
                    }
                    this.state = 1;
                    Intent intent = new Intent("com.google.android.c2dm.intent.REGISTER");
                    intent.setPackage("com.google.android.gms");
                    if (!ConnectionTracker.getInstance().bindService(this.zzbrd.zzqs, intent, this, 1)) {
                        zzb(0, "Unable to bind to service");
                        break;
                    }
                    this.zzbrd.zzbqw.schedule(new zzo(this), 30, TimeUnit.SECONDS);
                    break;
                case 1:
                    this.zzbrb.add(com_google_firebase_iid_zzt);
                    break;
                case 2:
                    this.zzbrb.add(com_google_firebase_iid_zzt);
                    zzsq();
                    break;
                case 3:
                case 4:
                    z2 = false;
                    break;
                default:
                    throw new IllegalStateException("Unknown state: " + this.state);
            }
        }
        return z2;
    }

    final synchronized void zzsr() {
        if (this.state == 2 && this.zzbrb.isEmpty() && this.zzbrc.size() == 0) {
            if (Log.isLoggable("MessengerIpcClient", 2)) {
                Log.v("MessengerIpcClient", "Finished handling requests, unbinding");
            }
            this.state = 3;
            ConnectionTracker.getInstance().unbindService(this.zzbrd.zzqs, this);
        }
    }

    final synchronized void zzss() {
        if (this.state == 1) {
            zzb(1, "Timed out while binding");
        }
    }
}
