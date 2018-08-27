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

public class zzk implements Parcelable {
    public static final Creator<zzk> CREATOR = new zzl();
    private Messenger zzad;
    private zzu zzae;

    public static final class zza extends ClassLoader {
        protected final Class<?> loadClass(String str, boolean z) throws ClassNotFoundException {
            if (!"com.google.android.gms.iid.MessengerCompat".equals(str)) {
                return super.loadClass(str, z);
            }
            if (FirebaseInstanceId.zzk()) {
                Log.d("FirebaseInstanceId", "Using renamed FirebaseIidMessengerCompat class");
            }
            return zzk.class;
        }
    }

    public zzk(IBinder iBinder) {
        if (VERSION.SDK_INT >= 21) {
            this.zzad = new Messenger(iBinder);
        } else {
            this.zzae = new zzv(iBinder);
        }
    }

    public final void send(Message message) throws RemoteException {
        if (this.zzad != null) {
            this.zzad.send(message);
        } else {
            this.zzae.send(message);
        }
    }

    private final IBinder getBinder() {
        return this.zzad != null ? this.zzad.getBinder() : this.zzae.asBinder();
    }

    public boolean equals(Object obj) {
        boolean z = false;
        if (obj != null) {
            try {
                z = getBinder().equals(((zzk) obj).getBinder());
            } catch (ClassCastException e) {
            }
        }
        return z;
    }

    public int hashCode() {
        return getBinder().hashCode();
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel parcel, int i) {
        if (this.zzad != null) {
            parcel.writeStrongBinder(this.zzad.getBinder());
        } else {
            parcel.writeStrongBinder(this.zzae.asBinder());
        }
    }
}
