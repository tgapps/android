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

final class zzab implements ServiceConnection {
    @GuardedBy("this")
    int state;
    final Messenger zzbo;
    zzag zzbp;
    @GuardedBy("this")
    final Queue<zzai<?>> zzbq;
    @GuardedBy("this")
    final SparseArray<zzai<?>> zzbr;
    final /* synthetic */ zzz zzbs;

    private zzab(zzz com_google_firebase_iid_zzz) {
        this.zzbs = com_google_firebase_iid_zzz;
        this.state = 0;
        this.zzbo = new Messenger(new Handler(Looper.getMainLooper(), new zzac(this)));
        this.zzbq = new ArrayDeque();
        this.zzbr = new SparseArray();
    }

    private final void zzx() {
        this.zzbs.zzbl.execute(new zzae(this));
    }

    public final synchronized void onServiceConnected(ComponentName componentName, IBinder iBinder) {
        if (Log.isLoggable("MessengerIpcClient", 2)) {
            Log.v("MessengerIpcClient", "Service connected");
        }
        if (iBinder == null) {
            zza(0, "Null service connection");
        } else {
            try {
                this.zzbp = new zzag(iBinder);
                this.state = 2;
                zzx();
            } catch (RemoteException e) {
                zza(0, e.getMessage());
            }
        }
    }

    public final synchronized void onServiceDisconnected(ComponentName componentName) {
        if (Log.isLoggable("MessengerIpcClient", 2)) {
            Log.v("MessengerIpcClient", "Service disconnected");
        }
        zza(2, "Service disconnected");
    }

    final synchronized void zza(int i) {
        zzai com_google_firebase_iid_zzai = (zzai) this.zzbr.get(i);
        if (com_google_firebase_iid_zzai != null) {
            Log.w("MessengerIpcClient", "Timing out request: " + i);
            this.zzbr.remove(i);
            com_google_firebase_iid_zzai.zza(new zzaj(3, "Timed out waiting for response"));
            zzy();
        }
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
                ConnectionTracker.getInstance().unbindService(this.zzbs.zzv, this);
                zzaj com_google_firebase_iid_zzaj = new zzaj(i, str);
                for (zzai zza : this.zzbq) {
                    zza.zza(com_google_firebase_iid_zzaj);
                }
                this.zzbq.clear();
                for (int i2 = 0; i2 < this.zzbr.size(); i2++) {
                    ((zzai) this.zzbr.valueAt(i2)).zza(com_google_firebase_iid_zzaj);
                }
                this.zzbr.clear();
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

    final boolean zza(Message message) {
        int i = message.arg1;
        if (Log.isLoggable("MessengerIpcClient", 3)) {
            Log.d("MessengerIpcClient", "Received response to request: " + i);
        }
        synchronized (this) {
            zzai com_google_firebase_iid_zzai = (zzai) this.zzbr.get(i);
            if (com_google_firebase_iid_zzai == null) {
                Log.w("MessengerIpcClient", "Received response for unknown request: " + i);
            } else {
                this.zzbr.remove(i);
                zzy();
                Bundle data = message.getData();
                if (data.getBoolean("unsupported", false)) {
                    com_google_firebase_iid_zzai.zza(new zzaj(4, "Not supported by GmsCore"));
                } else {
                    com_google_firebase_iid_zzai.zzb(data);
                }
            }
        }
        return true;
    }

    final synchronized boolean zzb(zzai com_google_firebase_iid_zzai) {
        boolean z = false;
        boolean z2 = true;
        synchronized (this) {
            switch (this.state) {
                case 0:
                    this.zzbq.add(com_google_firebase_iid_zzai);
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
                    if (!ConnectionTracker.getInstance().bindService(this.zzbs.zzv, intent, this, 1)) {
                        zza(0, "Unable to bind to service");
                        break;
                    }
                    this.zzbs.zzbl.schedule(new zzad(this), 30, TimeUnit.SECONDS);
                    break;
                case 1:
                    this.zzbq.add(com_google_firebase_iid_zzai);
                    break;
                case 2:
                    this.zzbq.add(com_google_firebase_iid_zzai);
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

    final synchronized void zzy() {
        if (this.state == 2 && this.zzbq.isEmpty() && this.zzbr.size() == 0) {
            if (Log.isLoggable("MessengerIpcClient", 2)) {
                Log.v("MessengerIpcClient", "Finished handling requests, unbinding");
            }
            this.state = 3;
            ConnectionTracker.getInstance().unbindService(this.zzbs.zzv, this);
        }
    }

    final synchronized void zzz() {
        if (this.state == 1) {
            zza(1, "Timed out while binding");
        }
    }
}
