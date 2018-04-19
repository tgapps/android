package com.google.android.gms.dynamic;

import android.content.Intent;
import android.os.Bundle;
import android.os.IInterface;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.RemoteException;
import com.google.android.gms.internal.stable.zzb;
import com.google.android.gms.internal.stable.zzc;
import org.telegram.messenger.exoplayer2.RendererCapabilities;

public interface IFragmentWrapper extends IInterface {

    public static abstract class Stub extends zzb implements IFragmentWrapper {
        public Stub() {
            super("com.google.android.gms.dynamic.IFragmentWrapper");
        }

        protected boolean dispatchTransaction(int i, Parcel parcel, Parcel parcel2, int i2) throws RemoteException {
            IInterface activity;
            int id;
            boolean retainInstance;
            switch (i) {
                case 2:
                    activity = getActivity();
                    parcel2.writeNoException();
                    zzc.zza(parcel2, activity);
                    break;
                case 3:
                    Parcelable arguments = getArguments();
                    parcel2.writeNoException();
                    zzc.zzb(parcel2, arguments);
                    break;
                case 4:
                    id = getId();
                    parcel2.writeNoException();
                    parcel2.writeInt(id);
                    break;
                case 5:
                    activity = getParentFragment();
                    parcel2.writeNoException();
                    zzc.zza(parcel2, activity);
                    break;
                case 6:
                    activity = getResources();
                    parcel2.writeNoException();
                    zzc.zza(parcel2, activity);
                    break;
                case 7:
                    retainInstance = getRetainInstance();
                    parcel2.writeNoException();
                    zzc.zza(parcel2, retainInstance);
                    break;
                case 8:
                    String tag = getTag();
                    parcel2.writeNoException();
                    parcel2.writeString(tag);
                    break;
                case 9:
                    activity = getTargetFragment();
                    parcel2.writeNoException();
                    zzc.zza(parcel2, activity);
                    break;
                case 10:
                    id = getTargetRequestCode();
                    parcel2.writeNoException();
                    parcel2.writeInt(id);
                    break;
                case 11:
                    retainInstance = getUserVisibleHint();
                    parcel2.writeNoException();
                    zzc.zza(parcel2, retainInstance);
                    break;
                case 12:
                    activity = getView();
                    parcel2.writeNoException();
                    zzc.zza(parcel2, activity);
                    break;
                case 13:
                    retainInstance = isAdded();
                    parcel2.writeNoException();
                    zzc.zza(parcel2, retainInstance);
                    break;
                case 14:
                    retainInstance = isDetached();
                    parcel2.writeNoException();
                    zzc.zza(parcel2, retainInstance);
                    break;
                case 15:
                    retainInstance = isHidden();
                    parcel2.writeNoException();
                    zzc.zza(parcel2, retainInstance);
                    break;
                case 16:
                    retainInstance = isInLayout();
                    parcel2.writeNoException();
                    zzc.zza(parcel2, retainInstance);
                    break;
                case 17:
                    retainInstance = isRemoving();
                    parcel2.writeNoException();
                    zzc.zza(parcel2, retainInstance);
                    break;
                case 18:
                    retainInstance = isResumed();
                    parcel2.writeNoException();
                    zzc.zza(parcel2, retainInstance);
                    break;
                case 19:
                    retainInstance = isVisible();
                    parcel2.writeNoException();
                    zzc.zza(parcel2, retainInstance);
                    break;
                case 20:
                    registerForContextMenu(com.google.android.gms.dynamic.IObjectWrapper.Stub.asInterface(parcel.readStrongBinder()));
                    parcel2.writeNoException();
                    break;
                case 21:
                    setHasOptionsMenu(zzc.zza(parcel));
                    parcel2.writeNoException();
                    break;
                case 22:
                    setMenuVisibility(zzc.zza(parcel));
                    parcel2.writeNoException();
                    break;
                case 23:
                    setRetainInstance(zzc.zza(parcel));
                    parcel2.writeNoException();
                    break;
                case RendererCapabilities.ADAPTIVE_SUPPORT_MASK /*24*/:
                    setUserVisibleHint(zzc.zza(parcel));
                    parcel2.writeNoException();
                    break;
                case 25:
                    startActivity((Intent) zzc.zza(parcel, Intent.CREATOR));
                    parcel2.writeNoException();
                    break;
                case 26:
                    startActivityForResult((Intent) zzc.zza(parcel, Intent.CREATOR), parcel.readInt());
                    parcel2.writeNoException();
                    break;
                case 27:
                    unregisterForContextMenu(com.google.android.gms.dynamic.IObjectWrapper.Stub.asInterface(parcel.readStrongBinder()));
                    parcel2.writeNoException();
                    break;
                default:
                    return false;
            }
            return true;
        }
    }

    IObjectWrapper getActivity() throws RemoteException;

    Bundle getArguments() throws RemoteException;

    int getId() throws RemoteException;

    IFragmentWrapper getParentFragment() throws RemoteException;

    IObjectWrapper getResources() throws RemoteException;

    boolean getRetainInstance() throws RemoteException;

    String getTag() throws RemoteException;

    IFragmentWrapper getTargetFragment() throws RemoteException;

    int getTargetRequestCode() throws RemoteException;

    boolean getUserVisibleHint() throws RemoteException;

    IObjectWrapper getView() throws RemoteException;

    boolean isAdded() throws RemoteException;

    boolean isDetached() throws RemoteException;

    boolean isHidden() throws RemoteException;

    boolean isInLayout() throws RemoteException;

    boolean isRemoving() throws RemoteException;

    boolean isResumed() throws RemoteException;

    boolean isVisible() throws RemoteException;

    void registerForContextMenu(IObjectWrapper iObjectWrapper) throws RemoteException;

    void setHasOptionsMenu(boolean z) throws RemoteException;

    void setMenuVisibility(boolean z) throws RemoteException;

    void setRetainInstance(boolean z) throws RemoteException;

    void setUserVisibleHint(boolean z) throws RemoteException;

    void startActivity(Intent intent) throws RemoteException;

    void startActivityForResult(Intent intent, int i) throws RemoteException;

    void unregisterForContextMenu(IObjectWrapper iObjectWrapper) throws RemoteException;
}
