package com.google.android.gms.measurement.internal;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.DeadObjectException;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Looper;
import android.os.RemoteException;
import com.google.android.exoplayer2.extractor.ts.TsExtractor;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.internal.BaseGmsClient.BaseConnectionCallbacks;
import com.google.android.gms.common.internal.BaseGmsClient.BaseOnConnectionFailedListener;
import com.google.android.gms.common.internal.Preconditions;
import com.google.android.gms.common.stats.ConnectionTracker;

public final class zzef implements ServiceConnection, BaseConnectionCallbacks, BaseOnConnectionFailedListener {
    final /* synthetic */ zzdr zzasg;
    private volatile boolean zzasm;
    private volatile zzao zzasn;

    protected zzef(zzdr com_google_android_gms_measurement_internal_zzdr) {
        this.zzasg = com_google_android_gms_measurement_internal_zzdr;
    }

    public final void zzc(Intent intent) {
        this.zzasg.zzaf();
        Context context = this.zzasg.getContext();
        ConnectionTracker instance = ConnectionTracker.getInstance();
        synchronized (this) {
            if (this.zzasm) {
                this.zzasg.zzgo().zzjl().zzbx("Connection attempt already in progress");
                return;
            }
            this.zzasg.zzgo().zzjl().zzbx("Using local app measurement service");
            this.zzasm = true;
            instance.bindService(context, intent, this.zzasg.zzarz, TsExtractor.TS_STREAM_TYPE_AC3);
        }
    }

    public final void zzlg() {
        if (this.zzasn != null && (this.zzasn.isConnected() || this.zzasn.isConnecting())) {
            this.zzasn.disconnect();
        }
        this.zzasn = null;
    }

    public final void onServiceConnected(ComponentName componentName, IBinder iBinder) {
        zzag com_google_android_gms_measurement_internal_zzag;
        Preconditions.checkMainThread("MeasurementServiceConnection.onServiceConnected");
        synchronized (this) {
            if (iBinder == null) {
                this.zzasm = false;
                this.zzasg.zzgo().zzjd().zzbx("Service connected with null binder");
                return;
            }
            try {
                String interfaceDescriptor = iBinder.getInterfaceDescriptor();
                if ("com.google.android.gms.measurement.internal.IMeasurementService".equals(interfaceDescriptor)) {
                    if (iBinder == null) {
                        com_google_android_gms_measurement_internal_zzag = null;
                    } else {
                        IInterface queryLocalInterface = iBinder.queryLocalInterface("com.google.android.gms.measurement.internal.IMeasurementService");
                        if (queryLocalInterface instanceof zzag) {
                            com_google_android_gms_measurement_internal_zzag = (zzag) queryLocalInterface;
                        } else {
                            com_google_android_gms_measurement_internal_zzag = new zzai(iBinder);
                        }
                    }
                    try {
                        this.zzasg.zzgo().zzjl().zzbx("Bound to IMeasurementService interface");
                    } catch (RemoteException e) {
                        this.zzasg.zzgo().zzjd().zzbx("Service connect failed to get IMeasurementService");
                        if (com_google_android_gms_measurement_internal_zzag != null) {
                            this.zzasg.zzgn().zzc(new zzeg(this, com_google_android_gms_measurement_internal_zzag));
                        } else {
                            this.zzasm = false;
                            try {
                                ConnectionTracker.getInstance().unbindService(this.zzasg.getContext(), this.zzasg.zzarz);
                            } catch (IllegalArgumentException e2) {
                            }
                        }
                    }
                    if (com_google_android_gms_measurement_internal_zzag != null) {
                        this.zzasm = false;
                        ConnectionTracker.getInstance().unbindService(this.zzasg.getContext(), this.zzasg.zzarz);
                    } else {
                        this.zzasg.zzgn().zzc(new zzeg(this, com_google_android_gms_measurement_internal_zzag));
                    }
                }
                this.zzasg.zzgo().zzjd().zzg("Got binder with a wrong descriptor", interfaceDescriptor);
                com_google_android_gms_measurement_internal_zzag = null;
                if (com_google_android_gms_measurement_internal_zzag != null) {
                    this.zzasg.zzgn().zzc(new zzeg(this, com_google_android_gms_measurement_internal_zzag));
                } else {
                    this.zzasm = false;
                    ConnectionTracker.getInstance().unbindService(this.zzasg.getContext(), this.zzasg.zzarz);
                }
            } catch (RemoteException e3) {
                com_google_android_gms_measurement_internal_zzag = null;
                this.zzasg.zzgo().zzjd().zzbx("Service connect failed to get IMeasurementService");
                if (com_google_android_gms_measurement_internal_zzag != null) {
                    this.zzasm = false;
                    ConnectionTracker.getInstance().unbindService(this.zzasg.getContext(), this.zzasg.zzarz);
                } else {
                    this.zzasg.zzgn().zzc(new zzeg(this, com_google_android_gms_measurement_internal_zzag));
                }
            }
        }
    }

    public final void onServiceDisconnected(ComponentName componentName) {
        Preconditions.checkMainThread("MeasurementServiceConnection.onServiceDisconnected");
        this.zzasg.zzgo().zzjk().zzbx("Service disconnected");
        this.zzasg.zzgn().zzc(new zzeh(this, componentName));
    }

    public final void zzlh() {
        this.zzasg.zzaf();
        Context context = this.zzasg.getContext();
        synchronized (this) {
            if (this.zzasm) {
                this.zzasg.zzgo().zzjl().zzbx("Connection attempt already in progress");
            } else if (this.zzasn == null || !(!zzn.zzia() || this.zzasn.isConnecting() || this.zzasn.isConnected())) {
                this.zzasn = new zzao(context, Looper.getMainLooper(), this, this);
                this.zzasg.zzgo().zzjl().zzbx("Connecting to remote service");
                this.zzasm = true;
                this.zzasn.checkAvailabilityAndConnect();
            } else {
                this.zzasg.zzgo().zzjl().zzbx("Already awaiting connection attempt");
            }
        }
    }

    public final void onConnected(Bundle bundle) {
        Preconditions.checkMainThread("MeasurementServiceConnection.onConnected");
        synchronized (this) {
            try {
                zzag com_google_android_gms_measurement_internal_zzag = (zzag) this.zzasn.getService();
                if (!zzn.zzia()) {
                    this.zzasn = null;
                }
                this.zzasg.zzgn().zzc(new zzei(this, com_google_android_gms_measurement_internal_zzag));
            } catch (DeadObjectException e) {
                this.zzasn = null;
                this.zzasm = false;
            } catch (IllegalStateException e2) {
                this.zzasn = null;
                this.zzasm = false;
            }
        }
    }

    public final void onConnectionSuspended(int i) {
        Preconditions.checkMainThread("MeasurementServiceConnection.onConnectionSuspended");
        this.zzasg.zzgo().zzjk().zzbx("Service connection suspended");
        this.zzasg.zzgn().zzc(new zzej(this));
    }

    public final void onConnectionFailed(ConnectionResult connectionResult) {
        Preconditions.checkMainThread("MeasurementServiceConnection.onConnectionFailed");
        zzap zzkf = this.zzasg.zzadj.zzkf();
        if (zzkf != null) {
            zzkf.zzjg().zzg("Service connection failed", connectionResult);
        }
        synchronized (this) {
            this.zzasm = false;
            this.zzasn = null;
        }
        this.zzasg.zzgn().zzc(new zzek(this));
    }
}
