package com.google.android.gms.internal.measurement;

import android.os.IInterface;
import android.os.Message;
import android.os.RemoteException;

public interface zzdu extends IInterface {
    void send(Message message) throws RemoteException;
}
