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

public final class zziw implements ServiceConnection, BaseConnectionCallbacks, BaseOnConnectionFailedListener {
    final /* synthetic */ zzii zzape;
    private volatile boolean zzapk;
    private volatile zzff zzapl;

    protected zziw(zzii com_google_android_gms_internal_measurement_zzii) {
        this.zzape = com_google_android_gms_internal_measurement_zzii;
    }

    public final void onConnected(Bundle bundle) {
        Preconditions.checkMainThread("MeasurementServiceConnection.onConnected");
        synchronized (this) {
            try {
                zzey com_google_android_gms_internal_measurement_zzey = (zzey) this.zzapl.getService();
                this.zzapl = null;
                this.zzape.zzgd().zzc(new zziz(this, com_google_android_gms_internal_measurement_zzey));
            } catch (DeadObjectException e) {
                this.zzapl = null;
                this.zzapk = false;
            } catch (IllegalStateException e2) {
                this.zzapl = null;
                this.zzapk = false;
            }
        }
    }

    public final void onConnectionFailed(ConnectionResult connectionResult) {
        Preconditions.checkMainThread("MeasurementServiceConnection.onConnectionFailed");
        zzfg zzjo = this.zzape.zzacw.zzjo();
        if (zzjo != null) {
            zzjo.zzip().zzg("Service connection failed", connectionResult);
        }
        synchronized (this) {
            this.zzapk = false;
            this.zzapl = null;
        }
        this.zzape.zzgd().zzc(new zzjb(this));
    }

    public final void onConnectionSuspended(int i) {
        Preconditions.checkMainThread("MeasurementServiceConnection.onConnectionSuspended");
        this.zzape.zzge().zzis().log("Service connection suspended");
        this.zzape.zzgd().zzc(new zzja(this));
    }

    public final void onServiceConnected(ComponentName componentName, IBinder iBinder) {
        Preconditions.checkMainThread("MeasurementServiceConnection.onServiceConnected");
        synchronized (this) {
            if (iBinder == null) {
                this.zzapk = false;
                this.zzape.zzge().zzim().log("Service connected with null binder");
                return;
            }
            zzey com_google_android_gms_internal_measurement_zzey;
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
                        this.zzape.zzge().zzit().log("Bound to IMeasurementService interface");
                    } catch (RemoteException e) {
                        this.zzape.zzge().zzim().log("Service connect failed to get IMeasurementService");
                        if (com_google_android_gms_internal_measurement_zzey != null) {
                            this.zzape.zzgd().zzc(new zzix(this, com_google_android_gms_internal_measurement_zzey));
                        } else {
                            this.zzapk = false;
                            try {
                                ConnectionTracker.getInstance().unbindService(this.zzape.getContext(), this.zzape.zzaox);
                            } catch (IllegalArgumentException e2) {
                            }
                        }
                    }
                    if (com_google_android_gms_internal_measurement_zzey != null) {
                        this.zzapk = false;
                        ConnectionTracker.getInstance().unbindService(this.zzape.getContext(), this.zzape.zzaox);
                    } else {
                        this.zzape.zzgd().zzc(new zzix(this, com_google_android_gms_internal_measurement_zzey));
                    }
                }
                this.zzape.zzge().zzim().zzg("Got binder with a wrong descriptor", interfaceDescriptor);
                com_google_android_gms_internal_measurement_zzey = null;
                if (com_google_android_gms_internal_measurement_zzey != null) {
                    this.zzape.zzgd().zzc(new zzix(this, com_google_android_gms_internal_measurement_zzey));
                } else {
                    this.zzapk = false;
                    ConnectionTracker.getInstance().unbindService(this.zzape.getContext(), this.zzape.zzaox);
                }
            } catch (RemoteException e3) {
                com_google_android_gms_internal_measurement_zzey = null;
                this.zzape.zzge().zzim().log("Service connect failed to get IMeasurementService");
                if (com_google_android_gms_internal_measurement_zzey != null) {
                    this.zzapk = false;
                    ConnectionTracker.getInstance().unbindService(this.zzape.getContext(), this.zzape.zzaox);
                } else {
                    this.zzape.zzgd().zzc(new zzix(this, com_google_android_gms_internal_measurement_zzey));
                }
            }
        }
    }

    public final void onServiceDisconnected(ComponentName componentName) {
        Preconditions.checkMainThread("MeasurementServiceConnection.onServiceDisconnected");
        this.zzape.zzge().zzis().log("Service disconnected");
        this.zzape.zzgd().zzc(new zziy(this, componentName));
    }

    public final void zzc(Intent intent) {
        this.zzape.zzab();
        Context context = this.zzape.getContext();
        ConnectionTracker instance = ConnectionTracker.getInstance();
        synchronized (this) {
            if (this.zzapk) {
                this.zzape.zzge().zzit().log("Connection attempt already in progress");
                return;
            }
            this.zzape.zzge().zzit().log("Using local app measurement service");
            this.zzapk = true;
            instance.bindService(context, intent, this.zzape.zzaox, TsExtractor.TS_STREAM_TYPE_AC3);
        }
    }

    public final void zzkh() {
        this.zzape.zzab();
        Context context = this.zzape.getContext();
        synchronized (this) {
            if (this.zzapk) {
                this.zzape.zzge().zzit().log("Connection attempt already in progress");
            } else if (this.zzapl != null) {
                this.zzape.zzge().zzit().log("Already awaiting connection attempt");
            } else {
                this.zzapl = new zzff(context, Looper.getMainLooper(), this, this);
                this.zzape.zzge().zzit().log("Connecting to remote service");
                this.zzapk = true;
                this.zzapl.checkAvailabilityAndConnect();
            }
        }
    }
}
