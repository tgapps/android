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

final class zzac implements ServiceConnection {
    @GuardedBy("this")
    int state;
    final Messenger zzbu;
    zzah zzbv;
    @GuardedBy("this")
    final Queue<zzaj<?>> zzbw;
    @GuardedBy("this")
    final SparseArray<zzaj<?>> zzbx;
    final /* synthetic */ zzaa zzby;

    private zzac(zzaa com_google_firebase_iid_zzaa) {
        this.zzby = com_google_firebase_iid_zzaa;
        this.state = 0;
        this.zzbu = new Messenger(new Handler(Looper.getMainLooper(), new zzad(this)));
        this.zzbw = new ArrayDeque();
        this.zzbx = new SparseArray();
    }

    final synchronized boolean zzb(zzaj com_google_firebase_iid_zzaj) {
        boolean z = false;
        boolean z2 = true;
        synchronized (this) {
            switch (this.state) {
                case 0:
                    this.zzbw.add(com_google_firebase_iid_zzaj);
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
                    if (!ConnectionTracker.getInstance().bindService(this.zzby.zzv, intent, this, 1)) {
                        zza(0, "Unable to bind to service");
                        break;
                    }
                    this.zzby.zzbr.schedule(new zzae(this), 30, TimeUnit.SECONDS);
                    break;
                case 1:
                    this.zzbw.add(com_google_firebase_iid_zzaj);
                    break;
                case 2:
                    this.zzbw.add(com_google_firebase_iid_zzaj);
                    zzx();
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

    final boolean zza(Message message) {
        int i = message.arg1;
        if (Log.isLoggable("MessengerIpcClient", 3)) {
            Log.d("MessengerIpcClient", "Received response to request: " + i);
        }
        synchronized (this) {
            zzaj com_google_firebase_iid_zzaj = (zzaj) this.zzbx.get(i);
            if (com_google_firebase_iid_zzaj == null) {
                Log.w("MessengerIpcClient", "Received response for unknown request: " + i);
            } else {
                this.zzbx.remove(i);
                zzy();
                Bundle data = message.getData();
                if (data.getBoolean("unsupported", false)) {
                    com_google_firebase_iid_zzaj.zza(new zzak(4, "Not supported by GmsCore"));
                } else {
                    com_google_firebase_iid_zzaj.zzb(data);
                }
            }
        }
        return true;
    }

    public final synchronized void onServiceConnected(ComponentName componentName, IBinder iBinder) {
        if (Log.isLoggable("MessengerIpcClient", 2)) {
            Log.v("MessengerIpcClient", "Service connected");
        }
        if (iBinder == null) {
            zza(0, "Null service connection");
        } else {
            try {
                this.zzbv = new zzah(iBinder);
                this.state = 2;
                zzx();
            } catch (RemoteException e) {
                zza(0, e.getMessage());
            }
        }
    }

    private final void zzx() {
        this.zzby.zzbr.execute(new zzaf(this));
    }

    public final synchronized void onServiceDisconnected(ComponentName componentName) {
        if (Log.isLoggable("MessengerIpcClient", 2)) {
            Log.v("MessengerIpcClient", "Service disconnected");
        }
        zza(2, "Service disconnected");
    }

    final synchronized void zza(int i, String str) {
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
                ConnectionTracker.getInstance().unbindService(this.zzby.zzv, this);
                zzak com_google_firebase_iid_zzak = new zzak(i, str);
                for (zzaj zza : this.zzbw) {
                    zza.zza(com_google_firebase_iid_zzak);
                }
                this.zzbw.clear();
                for (int i2 = 0; i2 < this.zzbx.size(); i2++) {
                    ((zzaj) this.zzbx.valueAt(i2)).zza(com_google_firebase_iid_zzak);
                }
                this.zzbx.clear();
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

    final synchronized void zzy() {
        if (this.state == 2 && this.zzbw.isEmpty() && this.zzbx.size() == 0) {
            if (Log.isLoggable("MessengerIpcClient", 2)) {
                Log.v("MessengerIpcClient", "Finished handling requests, unbinding");
            }
            this.state = 3;
            ConnectionTracker.getInstance().unbindService(this.zzby.zzv, this);
        }
    }

    final synchronized void zzz() {
        if (this.state == 1) {
            zza(1, "Timed out while binding");
        }
    }

    final synchronized void zza(int i) {
        zzaj com_google_firebase_iid_zzaj = (zzaj) this.zzbx.get(i);
        if (com_google_firebase_iid_zzaj != null) {
            Log.w("MessengerIpcClient", "Timing out request: " + i);
            this.zzbx.remove(i);
            com_google_firebase_iid_zzaj.zza(new zzak(3, "Timed out waiting for response"));
            zzy();
        }
    }
}
