package com.google.android.gms.internal.measurement;

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
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.internal.BaseGmsClient.BaseConnectionCallbacks;
import com.google.android.gms.common.internal.BaseGmsClient.BaseOnConnectionFailedListener;
import com.google.android.gms.common.internal.Preconditions;
import com.google.android.gms.common.stats.ConnectionTracker;
import org.telegram.messenger.exoplayer2.extractor.ts.TsExtractor;

public final class zziz implements ServiceConnection, BaseConnectionCallbacks, BaseOnConnectionFailedListener {
    final /* synthetic */ zzil zzapy;
    private volatile boolean zzaqf;
    private volatile zzff zzaqg;

    protected zziz(zzil com_google_android_gms_internal_measurement_zzil) {
        this.zzapy = com_google_android_gms_internal_measurement_zzil;
    }

    public final void onConnected(Bundle bundle) {
        Preconditions.checkMainThread("MeasurementServiceConnection.onConnected");
        synchronized (this) {
            try {
                zzey com_google_android_gms_internal_measurement_zzey = (zzey) this.zzaqg.getService();
                this.zzaqg = null;
                this.zzapy.zzgf().zzc(new zzjc(this, com_google_android_gms_internal_measurement_zzey));
            } catch (DeadObjectException e) {
                this.zzaqg = null;
                this.zzaqf = false;
            } catch (IllegalStateException e2) {
                this.zzaqg = null;
                this.zzaqf = false;
            }
        }
    }

    public final void onConnectionFailed(ConnectionResult connectionResult) {
        Preconditions.checkMainThread("MeasurementServiceConnection.onConnectionFailed");
        zzfg zzjl = this.zzapy.zzacr.zzjl();
        if (zzjl != null) {
            zzjl.zzin().zzg("Service connection failed", connectionResult);
        }
        synchronized (this) {
            this.zzaqf = false;
            this.zzaqg = null;
        }
        this.zzapy.zzgf().zzc(new zzje(this));
    }

    public final void onConnectionSuspended(int i) {
        Preconditions.checkMainThread("MeasurementServiceConnection.onConnectionSuspended");
        this.zzapy.zzgg().zziq().log("Service connection suspended");
        this.zzapy.zzgf().zzc(new zzjd(this));
    }

    public final void onServiceConnected(ComponentName componentName, IBinder iBinder) {
        zzey com_google_android_gms_internal_measurement_zzey;
        Preconditions.checkMainThread("MeasurementServiceConnection.onServiceConnected");
        synchronized (this) {
            if (iBinder == null) {
                this.zzaqf = false;
                this.zzapy.zzgg().zzil().log("Service connected with null binder");
                return;
            }
            try {
                String interfaceDescriptor = iBinder.getInterfaceDescriptor();
                if ("com.google.android.gms.measurement.internal.IMeasurementService".equals(interfaceDescriptor)) {
                    if (iBinder == null) {
                        com_google_android_gms_internal_measurement_zzey = null;
                    } else {
                        IInterface queryLocalInterface = iBinder.queryLocalInterface("com.google.android.gms.measurement.internal.IMeasurementService");
                        com_google_android_gms_internal_measurement_zzey = queryLocalInterface instanceof zzey ? (zzey) queryLocalInterface : new zzfa(iBinder);
                    }
                    try {
                        this.zzapy.zzgg().zzir().log("Bound to IMeasurementService interface");
                    } catch (RemoteException e) {
                        this.zzapy.zzgg().zzil().log("Service connect failed to get IMeasurementService");
                        if (com_google_android_gms_internal_measurement_zzey != null) {
                            this.zzapy.zzgf().zzc(new zzja(this, com_google_android_gms_internal_measurement_zzey));
                        } else {
                            this.zzaqf = false;
                            try {
                                ConnectionTracker.getInstance().unbindService(this.zzapy.getContext(), this.zzapy.zzapr);
                            } catch (IllegalArgumentException e2) {
                            }
                        }
                    }
                    if (com_google_android_gms_internal_measurement_zzey != null) {
                        this.zzaqf = false;
                        ConnectionTracker.getInstance().unbindService(this.zzapy.getContext(), this.zzapy.zzapr);
                    } else {
                        this.zzapy.zzgf().zzc(new zzja(this, com_google_android_gms_internal_measurement_zzey));
                    }
                }
                this.zzapy.zzgg().zzil().zzg("Got binder with a wrong descriptor", interfaceDescriptor);
                com_google_android_gms_internal_measurement_zzey = null;
                if (com_google_android_gms_internal_measurement_zzey != null) {
                    this.zzapy.zzgf().zzc(new zzja(this, com_google_android_gms_internal_measurement_zzey));
                } else {
                    this.zzaqf = false;
                    ConnectionTracker.getInstance().unbindService(this.zzapy.getContext(), this.zzapy.zzapr);
                }
            } catch (RemoteException e3) {
                com_google_android_gms_internal_measurement_zzey = null;
                this.zzapy.zzgg().zzil().log("Service connect failed to get IMeasurementService");
                if (com_google_android_gms_internal_measurement_zzey != null) {
                    this.zzaqf = false;
                    ConnectionTracker.getInstance().unbindService(this.zzapy.getContext(), this.zzapy.zzapr);
                } else {
                    this.zzapy.zzgf().zzc(new zzja(this, com_google_android_gms_internal_measurement_zzey));
                }
            }
        }
    }

    public final void onServiceDisconnected(ComponentName componentName) {
        Preconditions.checkMainThread("MeasurementServiceConnection.onServiceDisconnected");
        this.zzapy.zzgg().zziq().log("Service disconnected");
        this.zzapy.zzgf().zzc(new zzjb(this, componentName));
    }

    public final void zzc(Intent intent) {
        this.zzapy.zzab();
        Context context = this.zzapy.getContext();
        ConnectionTracker instance = ConnectionTracker.getInstance();
        synchronized (this) {
            if (this.zzaqf) {
                this.zzapy.zzgg().zzir().log("Connection attempt already in progress");
                return;
            }
            this.zzapy.zzgg().zzir().log("Using local app measurement service");
            this.zzaqf = true;
            instance.bindService(context, intent, this.zzapy.zzapr, TsExtractor.TS_STREAM_TYPE_AC3);
        }
    }

    public final void zzkp() {
        this.zzapy.zzab();
        Context context = this.zzapy.getContext();
        synchronized (this) {
            if (this.zzaqf) {
                this.zzapy.zzgg().zzir().log("Connection attempt already in progress");
            } else if (this.zzaqg != null) {
                this.zzapy.zzgg().zzir().log("Already awaiting connection attempt");
            } else {
                this.zzaqg = new zzff(context, Looper.getMainLooper(), this, this);
                this.zzapy.zzgg().zzir().log("Connecting to remote service");
                this.zzaqf = true;
                this.zzaqg.checkAvailabilityAndConnect();
            }
        }
    }
}
