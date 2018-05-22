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

final class zzu implements ServiceConnection {
    @GuardedBy("this")
    int state;
    final Messenger zzbj;
    zzz zzbk;
    @GuardedBy("this")
    final Queue<zzab<?>> zzbl;
    @GuardedBy("this")
    final SparseArray<zzab<?>> zzbm;
    final /* synthetic */ zzs zzbn;

    private zzu(zzs com_google_firebase_iid_zzs) {
        this.zzbn = com_google_firebase_iid_zzs;
        this.state = 0;
        this.zzbj = new Messenger(new Handler(Looper.getMainLooper(), new zzv(this)));
        this.zzbl = new ArrayDeque();
        this.zzbm = new SparseArray();
    }

    private final void zzt() {
        this.zzbn.zzbg.execute(new zzx(this));
    }

    public final synchronized void onServiceConnected(ComponentName componentName, IBinder iBinder) {
        if (Log.isLoggable("MessengerIpcClient", 2)) {
            Log.v("MessengerIpcClient", "Service connected");
        }
        if (iBinder == null) {
            zza(0, "Null service connection");
        } else {
            try {
                this.zzbk = new zzz(iBinder);
                this.state = 2;
                zzt();
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
        zzab com_google_firebase_iid_zzab = (zzab) this.zzbm.get(i);
        if (com_google_firebase_iid_zzab != null) {
            Log.w("MessengerIpcClient", "Timing out request: " + i);
            this.zzbm.remove(i);
            com_google_firebase_iid_zzab.zza(new zzac(3, "Timed out waiting for response"));
            zzu();
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
                ConnectionTracker.getInstance().unbindService(this.zzbn.zzz, this);
                zzac com_google_firebase_iid_zzac = new zzac(i, str);
                for (zzab zza : this.zzbl) {
                    zza.zza(com_google_firebase_iid_zzac);
                }
                this.zzbl.clear();
                for (int i2 = 0; i2 < this.zzbm.size(); i2++) {
                    ((zzab) this.zzbm.valueAt(i2)).zza(com_google_firebase_iid_zzac);
                }
                this.zzbm.clear();
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
            zzab com_google_firebase_iid_zzab = (zzab) this.zzbm.get(i);
            if (com_google_firebase_iid_zzab == null) {
                Log.w("MessengerIpcClient", "Received response for unknown request: " + i);
            } else {
                this.zzbm.remove(i);
                zzu();
                Bundle data = message.getData();
                if (data.getBoolean("unsupported", false)) {
                    com_google_firebase_iid_zzab.zza(new zzac(4, "Not supported by GmsCore"));
                } else {
                    com_google_firebase_iid_zzab.zzb(data);
                }
            }
        }
        return true;
    }

    final synchronized boolean zzb(zzab com_google_firebase_iid_zzab) {
        boolean z = false;
        boolean z2 = true;
        synchronized (this) {
            switch (this.state) {
                case 0:
                    this.zzbl.add(com_google_firebase_iid_zzab);
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
                    if (!ConnectionTracker.getInstance().bindService(this.zzbn.zzz, intent, this, 1)) {
                        zza(0, "Unable to bind to service");
                        break;
                    }
                    this.zzbn.zzbg.schedule(new zzw(this), 30, TimeUnit.SECONDS);
                    break;
                case 1:
                    this.zzbl.add(com_google_firebase_iid_zzab);
                    break;
                case 2:
                    this.zzbl.add(com_google_firebase_iid_zzab);
                    zzt();
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

    final synchronized void zzu() {
        if (this.state == 2 && this.zzbl.isEmpty() && this.zzbm.size() == 0) {
            if (Log.isLoggable("MessengerIpcClient", 2)) {
                Log.v("MessengerIpcClient", "Finished handling requests, unbinding");
            }
            this.state = 3;
            ConnectionTracker.getInstance().unbindService(this.zzbn.zzz, this);
        }
    }

    final synchronized void zzv() {
        if (this.state == 1) {
            zza(1, "Timed out while binding");
        }
    }
}
