package com.google.firebase.iid;

import android.os.Build.VERSION;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.os.RemoteException;
import android.util.Log;
import com.google.android.gms.internal.firebase_messaging.zze;
import com.google.android.gms.internal.firebase_messaging.zzf;

public class zzi implements Parcelable {
    public static final Creator<zzi> CREATOR = new zzj();
    private Messenger zzaf;
    private zze zzag;

    public static final class zza extends ClassLoader {
        protected final Class<?> loadClass(String str, boolean z) throws ClassNotFoundException {
            if (!"com.google.android.gms.iid.MessengerCompat".equals(str)) {
                return super.loadClass(str, z);
            }
            if (FirebaseInstanceId.zzj()) {
                Log.d("FirebaseInstanceId", "Using renamed FirebaseIidMessengerCompat class");
            }
            return zzi.class;
        }
    }

    public zzi(IBinder iBinder) {
        if (VERSION.SDK_INT >= 21) {
            this.zzaf = new Messenger(iBinder);
        } else {
            this.zzag = zzf.zza(iBinder);
        }
    }

    private final IBinder getBinder() {
        return this.zzaf != null ? this.zzaf.getBinder() : this.zzag.asBinder();
    }

    public int describeContents() {
        return 0;
    }

    public boolean equals(Object obj) {
        boolean z = false;
        if (obj != null) {
            try {
                z = getBinder().equals(((zzi) obj).getBinder());
            } catch (ClassCastException e) {
            }
        }
        return z;
    }

    public int hashCode() {
        return getBinder().hashCode();
    }

    public final void send(Message message) throws RemoteException {
        if (this.zzaf != null) {
            this.zzaf.send(message);
        } else {
            this.zzag.send(message);
        }
    }

    public void writeToParcel(Parcel parcel, int i) {
        if (this.zzaf != null) {
            parcel.writeStrongBinder(this.zzaf.getBinder());
        } else {
            parcel.writeStrongBinder(this.zzag.asBinder());
        }
    }
}
