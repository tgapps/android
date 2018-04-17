package com.google.android.gms.common.internal;

import android.accounts.Account;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.DeadObjectException;
import android.os.Handler;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Looper;
import android.os.Message;
import android.os.RemoteException;
import android.text.TextUtils;
import android.util.Log;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.Feature;
import com.google.android.gms.common.GoogleApiAvailabilityLight;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.common.internal.IGmsServiceBroker.Stub;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Locale;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import javax.annotation.concurrent.GuardedBy;
import org.telegram.messenger.exoplayer2.extractor.ts.TsExtractor;

public abstract class BaseGmsClient<T extends IInterface> {
    public static final String[] GOOGLE_PLUS_REQUIRED_FEATURES = new String[]{"service_esmobile", "service_googleme"};
    private static final Feature[] zzqz = new Feature[0];
    protected ConnectionProgressReportCallbacks mConnectionProgressReportCallbacks;
    private final Context mContext;
    protected AtomicInteger mDisconnectCount;
    final Handler mHandler;
    private final Object mLock;
    private final Looper zzcn;
    private final GoogleApiAvailabilityLight zzgk;
    private int zzra;
    private long zzrb;
    private long zzrc;
    private int zzrd;
    private long zzre;
    private GmsServiceEndpoint zzrf;
    private final GmsClientSupervisor zzrg;
    private final Object zzrh;
    @GuardedBy("mServiceBrokerLock")
    private IGmsServiceBroker zzri;
    @GuardedBy("mLock")
    private T zzrj;
    private final ArrayList<CallbackProxy<?>> zzrk;
    @GuardedBy("mLock")
    private GmsServiceConnection zzrl;
    @GuardedBy("mLock")
    private int zzrm;
    private final BaseConnectionCallbacks zzrn;
    private final BaseOnConnectionFailedListener zzro;
    private final int zzrp;
    private final String zzrq;
    private ConnectionResult zzrr;
    private boolean zzrs;
    private volatile ConnectionInfo zzrt;

    public interface BaseConnectionCallbacks {
        void onConnected(Bundle bundle);

        void onConnectionSuspended(int i);
    }

    public interface BaseOnConnectionFailedListener {
        void onConnectionFailed(ConnectionResult connectionResult);
    }

    protected abstract class CallbackProxy<TListener> {
        private TListener zzli;
        private final /* synthetic */ BaseGmsClient zzru;
        private boolean zzrv = false;

        public CallbackProxy(BaseGmsClient baseGmsClient, TListener tListener) {
            this.zzru = baseGmsClient;
            this.zzli = tListener;
        }

        public void deliverCallback() {
            synchronized (this) {
                Object obj = this.zzli;
                if (this.zzrv) {
                    String valueOf = String.valueOf(this);
                    StringBuilder stringBuilder = new StringBuilder(47 + String.valueOf(valueOf).length());
                    stringBuilder.append("Callback proxy ");
                    stringBuilder.append(valueOf);
                    stringBuilder.append(" being reused. This is not safe.");
                    Log.w("GmsClient", stringBuilder.toString());
                }
            }
            if (obj != null) {
                try {
                    deliverCallback(obj);
                } catch (RuntimeException e) {
                    onDeliverCallbackFailed();
                    throw e;
                }
            }
            onDeliverCallbackFailed();
            synchronized (this) {
                this.zzrv = true;
            }
            unregister();
        }

        protected abstract void deliverCallback(TListener tListener);

        protected abstract void onDeliverCallbackFailed();

        public void removeListener() {
            synchronized (this) {
                this.zzli = null;
            }
        }

        public void unregister() {
            removeListener();
            synchronized (this.zzru.zzrk) {
                this.zzru.zzrk.remove(this);
            }
        }
    }

    public interface ConnectionProgressReportCallbacks {
        void onReportServiceBinding(ConnectionResult connectionResult);
    }

    public final class GmsServiceConnection implements ServiceConnection {
        private final /* synthetic */ BaseGmsClient zzru;
        private final int zzrx;

        public GmsServiceConnection(BaseGmsClient baseGmsClient, int i) {
            this.zzru = baseGmsClient;
            this.zzrx = i;
        }

        public final void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            if (iBinder == null) {
                this.zzru.zzj(16);
                return;
            }
            synchronized (this.zzru.zzrh) {
                this.zzru.zzri = Stub.asInterface(iBinder);
            }
            this.zzru.onPostServiceBindingHandler(0, null, this.zzrx);
        }

        public final void onServiceDisconnected(ComponentName componentName) {
            synchronized (this.zzru.zzrh) {
                this.zzru.zzri = null;
            }
            this.zzru.mHandler.sendMessage(this.zzru.mHandler.obtainMessage(6, this.zzrx, 1));
        }
    }

    public interface SignOutCallbacks {
        void onSignOutComplete();
    }

    final class zzb extends Handler {
        private final /* synthetic */ BaseGmsClient zzru;

        public zzb(BaseGmsClient baseGmsClient, Looper looper) {
            this.zzru = baseGmsClient;
            super(looper);
        }

        private static void zza(Message message) {
            CallbackProxy callbackProxy = (CallbackProxy) message.obj;
            callbackProxy.onDeliverCallbackFailed();
            callbackProxy.unregister();
        }

        /* JADX WARNING: inconsistent code. */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        private static boolean zzb(android.os.Message r3) {
            /*
            r0 = r3.what;
            r1 = 1;
            r2 = 2;
            if (r0 == r2) goto L_0x0012;
        L_0x0006:
            r0 = r3.what;
            if (r0 == r1) goto L_0x0012;
        L_0x000a:
            r3 = r3.what;
            r0 = 7;
            if (r3 != r0) goto L_0x0010;
        L_0x000f:
            return r1;
        L_0x0010:
            r3 = 0;
            return r3;
        L_0x0012:
            return r1;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.common.internal.BaseGmsClient.zzb.zzb(android.os.Message):boolean");
        }

        public final void handleMessage(Message message) {
            if (this.zzru.mDisconnectCount.get() != message.arg1) {
                if (zzb(message)) {
                    zza(message);
                }
            } else if ((message.what == 1 || message.what == 7 || message.what == 4 || message.what == 5) && !this.zzru.isConnecting()) {
                zza(message);
            } else {
                PendingIntent pendingIntent = null;
                ConnectionResult zzd;
                if (message.what == 4) {
                    this.zzru.zzrr = new ConnectionResult(message.arg2);
                    if (!this.zzru.zzcr() || this.zzru.zzrs) {
                        zzd = this.zzru.zzrr != null ? this.zzru.zzrr : new ConnectionResult(8);
                        this.zzru.mConnectionProgressReportCallbacks.onReportServiceBinding(zzd);
                        this.zzru.onConnectionFailed(zzd);
                        return;
                    }
                    this.zzru.zza(3, null);
                } else if (message.what == 5) {
                    zzd = this.zzru.zzrr != null ? this.zzru.zzrr : new ConnectionResult(8);
                    this.zzru.mConnectionProgressReportCallbacks.onReportServiceBinding(zzd);
                    this.zzru.onConnectionFailed(zzd);
                } else if (message.what == 3) {
                    if (message.obj instanceof PendingIntent) {
                        pendingIntent = (PendingIntent) message.obj;
                    }
                    ConnectionResult connectionResult = new ConnectionResult(message.arg2, pendingIntent);
                    this.zzru.mConnectionProgressReportCallbacks.onReportServiceBinding(connectionResult);
                    this.zzru.onConnectionFailed(connectionResult);
                } else if (message.what == 6) {
                    this.zzru.zza(5, null);
                    if (this.zzru.zzrn != null) {
                        this.zzru.zzrn.onConnectionSuspended(message.arg2);
                    }
                    this.zzru.onConnectionSuspended(message.arg2);
                    this.zzru.zza(5, 1, null);
                } else if (message.what == 2 && !this.zzru.isConnected()) {
                    zza(message);
                } else if (zzb(message)) {
                    ((CallbackProxy) message.obj).deliverCallback();
                } else {
                    int i = message.what;
                    StringBuilder stringBuilder = new StringBuilder(45);
                    stringBuilder.append("Don't know how to handle message: ");
                    stringBuilder.append(i);
                    Log.wtf("GmsClient", stringBuilder.toString(), new Exception());
                }
            }
        }
    }

    protected class LegacyClientCallbackAdapter implements ConnectionProgressReportCallbacks {
        private final /* synthetic */ BaseGmsClient zzru;

        public LegacyClientCallbackAdapter(BaseGmsClient baseGmsClient) {
            this.zzru = baseGmsClient;
        }

        public void onReportServiceBinding(ConnectionResult connectionResult) {
            if (connectionResult.isSuccess()) {
                this.zzru.getRemoteService(null, this.zzru.getScopes());
                return;
            }
            if (this.zzru.zzro != null) {
                this.zzru.zzro.onConnectionFailed(connectionResult);
            }
        }
    }

    private abstract class zza extends CallbackProxy<Boolean> {
        public final Bundle resolution;
        public final int statusCode;
        private final /* synthetic */ BaseGmsClient zzru;

        protected zza(BaseGmsClient baseGmsClient, int i, Bundle bundle) {
            this.zzru = baseGmsClient;
            super(baseGmsClient, Boolean.valueOf(true));
            this.statusCode = i;
            this.resolution = bundle;
        }

        protected void deliverCallback(Boolean bool) {
            PendingIntent pendingIntent = null;
            if (bool == null) {
                this.zzru.zza(1, null);
                return;
            }
            int i = this.statusCode;
            if (i == 0) {
                if (!handleServiceSuccess()) {
                    this.zzru.zza(1, null);
                    handleServiceFailure(new ConnectionResult(8, null));
                }
            } else if (i != 10) {
                this.zzru.zza(1, null);
                if (this.resolution != null) {
                    pendingIntent = (PendingIntent) this.resolution.getParcelable("pendingIntent");
                }
                handleServiceFailure(new ConnectionResult(this.statusCode, pendingIntent));
            } else {
                this.zzru.zza(1, null);
                throw new IllegalStateException("A fatal developer error has occurred. Check the logs for further information.");
            }
        }

        protected abstract void handleServiceFailure(ConnectionResult connectionResult);

        protected abstract boolean handleServiceSuccess();

        protected void onDeliverCallbackFailed() {
        }
    }

    public static final class GmsCallbacks extends IGmsCallbacks.Stub {
        private BaseGmsClient zzrw;
        private final int zzrx;

        public GmsCallbacks(BaseGmsClient baseGmsClient, int i) {
            this.zzrw = baseGmsClient;
            this.zzrx = i;
        }

        public final void onAccountValidationComplete(int i, Bundle bundle) {
            Log.wtf("GmsClient", "received deprecated onAccountValidationComplete callback, ignoring", new Exception());
        }

        public final void onPostInitComplete(int i, IBinder iBinder, Bundle bundle) {
            Preconditions.checkNotNull(this.zzrw, "onPostInitComplete can be called only once per call to getRemoteService");
            this.zzrw.onPostInitHandler(i, iBinder, bundle, this.zzrx);
            this.zzrw = null;
        }

        public final void onPostInitCompleteWithConnectionInfo(int i, IBinder iBinder, ConnectionInfo connectionInfo) {
            Preconditions.checkNotNull(this.zzrw, "onPostInitCompleteWithConnectionInfo can be called only once per call togetRemoteService");
            Preconditions.checkNotNull(connectionInfo);
            this.zzrw.zza(connectionInfo);
            onPostInitComplete(i, iBinder, connectionInfo.getResolutionBundle());
        }
    }

    protected final class PostInitCallback extends zza {
        public final IBinder service;
        private final /* synthetic */ BaseGmsClient zzru;

        public PostInitCallback(BaseGmsClient baseGmsClient, int i, IBinder iBinder, Bundle bundle) {
            this.zzru = baseGmsClient;
            super(baseGmsClient, i, bundle);
            this.service = iBinder;
        }

        protected final void handleServiceFailure(ConnectionResult connectionResult) {
            if (this.zzru.zzro != null) {
                this.zzru.zzro.onConnectionFailed(connectionResult);
            }
            this.zzru.onConnectionFailed(connectionResult);
        }

        protected final boolean handleServiceSuccess() {
            boolean z = false;
            try {
                String interfaceDescriptor = this.service.getInterfaceDescriptor();
                if (this.zzru.getServiceDescriptor().equals(interfaceDescriptor)) {
                    IInterface createServiceInterface = this.zzru.createServiceInterface(this.service);
                    if (createServiceInterface != null && (this.zzru.zza(2, 4, createServiceInterface) || this.zzru.zza(3, 4, createServiceInterface))) {
                        this.zzru.zzrr = null;
                        Bundle connectionHint = this.zzru.getConnectionHint();
                        if (this.zzru.zzrn != null) {
                            this.zzru.zzrn.onConnected(connectionHint);
                        }
                        z = true;
                    }
                    return z;
                }
                String serviceDescriptor = this.zzru.getServiceDescriptor();
                StringBuilder stringBuilder = new StringBuilder((34 + String.valueOf(serviceDescriptor).length()) + String.valueOf(interfaceDescriptor).length());
                stringBuilder.append("service descriptor mismatch: ");
                stringBuilder.append(serviceDescriptor);
                stringBuilder.append(" vs. ");
                stringBuilder.append(interfaceDescriptor);
                Log.e("GmsClient", stringBuilder.toString());
                return false;
            } catch (RemoteException e) {
                Log.w("GmsClient", "service probably died");
                return false;
            }
        }
    }

    protected final class PostServiceBindingCallback extends zza {
        private final /* synthetic */ BaseGmsClient zzru;

        public PostServiceBindingCallback(BaseGmsClient baseGmsClient, int i, Bundle bundle) {
            this.zzru = baseGmsClient;
            super(baseGmsClient, i, bundle);
        }

        protected final void handleServiceFailure(ConnectionResult connectionResult) {
            this.zzru.mConnectionProgressReportCallbacks.onReportServiceBinding(connectionResult);
            this.zzru.onConnectionFailed(connectionResult);
        }

        protected final boolean handleServiceSuccess() {
            this.zzru.mConnectionProgressReportCallbacks.onReportServiceBinding(ConnectionResult.RESULT_SUCCESS);
            return true;
        }
    }

    protected BaseGmsClient(Context context, Looper looper, int i, BaseConnectionCallbacks baseConnectionCallbacks, BaseOnConnectionFailedListener baseOnConnectionFailedListener, String str) {
        this(context, looper, GmsClientSupervisor.getInstance(context), GoogleApiAvailabilityLight.getInstance(), i, (BaseConnectionCallbacks) Preconditions.checkNotNull(baseConnectionCallbacks), (BaseOnConnectionFailedListener) Preconditions.checkNotNull(baseOnConnectionFailedListener), str);
    }

    protected BaseGmsClient(Context context, Looper looper, GmsClientSupervisor gmsClientSupervisor, GoogleApiAvailabilityLight googleApiAvailabilityLight, int i, BaseConnectionCallbacks baseConnectionCallbacks, BaseOnConnectionFailedListener baseOnConnectionFailedListener, String str) {
        this.mLock = new Object();
        this.zzrh = new Object();
        this.zzrk = new ArrayList();
        this.zzrm = 1;
        this.zzrr = null;
        this.zzrs = false;
        this.zzrt = null;
        this.mDisconnectCount = new AtomicInteger(0);
        this.mContext = (Context) Preconditions.checkNotNull(context, "Context must not be null");
        this.zzcn = (Looper) Preconditions.checkNotNull(looper, "Looper must not be null");
        this.zzrg = (GmsClientSupervisor) Preconditions.checkNotNull(gmsClientSupervisor, "Supervisor must not be null");
        this.zzgk = (GoogleApiAvailabilityLight) Preconditions.checkNotNull(googleApiAvailabilityLight, "API availability must not be null");
        this.mHandler = new zzb(this, looper);
        this.zzrp = i;
        this.zzrn = baseConnectionCallbacks;
        this.zzro = baseOnConnectionFailedListener;
        this.zzrq = str;
    }

    private final void zza(int i, T t) {
        Preconditions.checkArgument((i == 4) == (t != null));
        synchronized (this.mLock) {
            this.zzrm = i;
            this.zzrj = t;
            onSetConnectState(i, t);
            switch (i) {
                case 1:
                    if (this.zzrl != null) {
                        this.zzrg.unbindService(getStartServiceAction(), getStartServicePackage(), getServiceBindFlags(), this.zzrl, getRealClientName());
                        this.zzrl = null;
                        break;
                    }
                    break;
                case 2:
                case 3:
                    String zzcw;
                    if (!(this.zzrl == null || this.zzrf == null)) {
                        zzcw = this.zzrf.zzcw();
                        String packageName = this.zzrf.getPackageName();
                        StringBuilder stringBuilder = new StringBuilder((70 + String.valueOf(zzcw).length()) + String.valueOf(packageName).length());
                        stringBuilder.append("Calling connect() while still connected, missing disconnect() for ");
                        stringBuilder.append(zzcw);
                        stringBuilder.append(" on ");
                        stringBuilder.append(packageName);
                        Log.e("GmsClient", stringBuilder.toString());
                        this.zzrg.unbindService(this.zzrf.zzcw(), this.zzrf.getPackageName(), this.zzrf.getBindFlags(), this.zzrl, getRealClientName());
                        this.mDisconnectCount.incrementAndGet();
                    }
                    this.zzrl = new GmsServiceConnection(this, this.mDisconnectCount.get());
                    GmsServiceEndpoint gmsServiceEndpoint = (this.zzrm != 3 || getLocalStartServiceAction() == null) ? new GmsServiceEndpoint(getStartServicePackage(), getStartServiceAction(), false, getServiceBindFlags()) : new GmsServiceEndpoint(getContext().getPackageName(), getLocalStartServiceAction(), true, getServiceBindFlags());
                    this.zzrf = gmsServiceEndpoint;
                    if (!this.zzrg.bindService(this.zzrf.zzcw(), this.zzrf.getPackageName(), this.zzrf.getBindFlags(), this.zzrl, getRealClientName())) {
                        zzcw = this.zzrf.zzcw();
                        String packageName2 = this.zzrf.getPackageName();
                        StringBuilder stringBuilder2 = new StringBuilder((34 + String.valueOf(zzcw).length()) + String.valueOf(packageName2).length());
                        stringBuilder2.append("unable to connect to service: ");
                        stringBuilder2.append(zzcw);
                        stringBuilder2.append(" on ");
                        stringBuilder2.append(packageName2);
                        Log.e("GmsClient", stringBuilder2.toString());
                        onPostServiceBindingHandler(16, null, this.mDisconnectCount.get());
                        break;
                    }
                    break;
                case 4:
                    onConnectedLocked(t);
                    break;
                default:
                    break;
            }
        }
    }

    private final void zza(ConnectionInfo connectionInfo) {
        this.zzrt = connectionInfo;
    }

    private final boolean zza(int i, int i2, T t) {
        synchronized (this.mLock) {
            if (this.zzrm != i) {
                return false;
            }
            zza(i2, (IInterface) t);
            return true;
        }
    }

    private final boolean zzcq() {
        boolean z;
        synchronized (this.mLock) {
            z = this.zzrm == 3;
        }
        return z;
    }

    private final boolean zzcr() {
        if (this.zzrs || TextUtils.isEmpty(getServiceDescriptor()) || TextUtils.isEmpty(getLocalStartServiceAction())) {
            return false;
        }
        try {
            Class.forName(getServiceDescriptor());
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }

    private final void zzj(int i) {
        if (zzcq()) {
            i = 5;
            this.zzrs = true;
        } else {
            i = 4;
        }
        this.mHandler.sendMessage(this.mHandler.obtainMessage(i, this.mDisconnectCount.get(), 16));
    }

    public void checkAvailabilityAndConnect() {
        int isGooglePlayServicesAvailable = this.zzgk.isGooglePlayServicesAvailable(this.mContext, getMinApkVersion());
        if (isGooglePlayServicesAvailable != 0) {
            zza(1, null);
            triggerNotAvailable(new LegacyClientCallbackAdapter(this), isGooglePlayServicesAvailable, null);
            return;
        }
        connect(new LegacyClientCallbackAdapter(this));
    }

    protected final void checkConnected() {
        if (!isConnected()) {
            throw new IllegalStateException("Not connected. Call connect() and wait for onConnected() to be called.");
        }
    }

    public void connect(ConnectionProgressReportCallbacks connectionProgressReportCallbacks) {
        this.mConnectionProgressReportCallbacks = (ConnectionProgressReportCallbacks) Preconditions.checkNotNull(connectionProgressReportCallbacks, "Connection progress callbacks cannot be null.");
        zza(2, null);
    }

    protected abstract T createServiceInterface(IBinder iBinder);

    public void disconnect() {
        this.mDisconnectCount.incrementAndGet();
        synchronized (this.zzrk) {
            int size = this.zzrk.size();
            for (int i = 0; i < size; i++) {
                ((CallbackProxy) this.zzrk.get(i)).removeListener();
            }
            this.zzrk.clear();
        }
        synchronized (this.zzrh) {
            this.zzri = null;
        }
        zza(1, null);
    }

    public void dump(String str, FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        String str2;
        synchronized (this.mLock) {
            int i = this.zzrm;
            IInterface iInterface = this.zzrj;
        }
        synchronized (this.zzrh) {
            IGmsServiceBroker iGmsServiceBroker = this.zzri;
        }
        printWriter.append(str).append("mConnectState=");
        switch (i) {
            case 1:
                str2 = "DISCONNECTED";
                break;
            case 2:
                str2 = "REMOTE_CONNECTING";
                break;
            case 3:
                str2 = "LOCAL_CONNECTING";
                break;
            case 4:
                str2 = "CONNECTED";
                break;
            case 5:
                str2 = "DISCONNECTING";
                break;
            default:
                str2 = "UNKNOWN";
                break;
        }
        printWriter.print(str2);
        printWriter.append(" mService=");
        if (iInterface == null) {
            printWriter.append("null");
        } else {
            printWriter.append(getServiceDescriptor()).append("@").append(Integer.toHexString(System.identityHashCode(iInterface.asBinder())));
        }
        printWriter.append(" mServiceBroker=");
        if (iGmsServiceBroker == null) {
            printWriter.println("null");
        } else {
            printWriter.append("IGmsServiceBroker@").println(Integer.toHexString(System.identityHashCode(iGmsServiceBroker.asBinder())));
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", Locale.US);
        if (this.zzrc > 0) {
            PrintWriter append = printWriter.append(str).append("lastConnectedTime=");
            long j = this.zzrc;
            String format = simpleDateFormat.format(new Date(this.zzrc));
            StringBuilder stringBuilder = new StringBuilder(String.valueOf(format).length() + 21);
            stringBuilder.append(j);
            stringBuilder.append(" ");
            stringBuilder.append(format);
            append.println(stringBuilder.toString());
        }
        if (this.zzrb > 0) {
            CharSequence charSequence;
            printWriter.append(str).append("lastSuspendedCause=");
            switch (this.zzra) {
                case 1:
                    charSequence = "CAUSE_SERVICE_DISCONNECTED";
                    break;
                case 2:
                    charSequence = "CAUSE_NETWORK_LOST";
                    break;
                default:
                    charSequence = String.valueOf(this.zzra);
                    break;
            }
            printWriter.append(charSequence);
            append = printWriter.append(" lastSuspendedTime=");
            j = this.zzrb;
            format = simpleDateFormat.format(new Date(this.zzrb));
            stringBuilder = new StringBuilder(String.valueOf(format).length() + 21);
            stringBuilder.append(j);
            stringBuilder.append(" ");
            stringBuilder.append(format);
            append.println(stringBuilder.toString());
        }
        if (this.zzre > 0) {
            printWriter.append(str).append("lastFailedStatus=").append(CommonStatusCodes.getStatusCodeString(this.zzrd));
            PrintWriter append2 = printWriter.append(" lastFailedTime=");
            long j2 = this.zzre;
            String format2 = simpleDateFormat.format(new Date(this.zzre));
            StringBuilder stringBuilder2 = new StringBuilder(21 + String.valueOf(format2).length());
            stringBuilder2.append(j2);
            stringBuilder2.append(" ");
            stringBuilder2.append(format2);
            append2.println(stringBuilder2.toString());
        }
    }

    public Account getAccount() {
        return null;
    }

    public final Account getAccountOrDefault() {
        return getAccount() != null ? getAccount() : new Account("<<default account>>", "com.google");
    }

    public Feature[] getApiFeatures() {
        return zzqz;
    }

    public final Feature[] getAvailableFeatures() {
        ConnectionInfo connectionInfo = this.zzrt;
        return connectionInfo == null ? null : connectionInfo.getAvailableFeatures();
    }

    public Bundle getConnectionHint() {
        return null;
    }

    public final Context getContext() {
        return this.mContext;
    }

    public String getEndpointPackageName() {
        if (isConnected() && this.zzrf != null) {
            return this.zzrf.getPackageName();
        }
        throw new RuntimeException("Failed to connect when checking package");
    }

    protected Bundle getGetServiceRequestExtraArgs() {
        return new Bundle();
    }

    protected String getLocalStartServiceAction() {
        return null;
    }

    public int getMinApkVersion() {
        return GoogleApiAvailabilityLight.GOOGLE_PLAY_SERVICES_VERSION_CODE;
    }

    protected final String getRealClientName() {
        return this.zzrq == null ? this.mContext.getClass().getName() : this.zzrq;
    }

    public void getRemoteService(IAccountAccessor iAccountAccessor, Set<Scope> set) {
        GetServiceRequest extraArgs = new GetServiceRequest(this.zzrp).setCallingPackage(this.mContext.getPackageName()).setExtraArgs(getGetServiceRequestExtraArgs());
        if (set != null) {
            extraArgs.setScopes(set);
        }
        if (requiresSignIn()) {
            extraArgs.setClientRequestedAccount(getAccountOrDefault()).setAuthenticatedAccount(iAccountAccessor);
        } else if (requiresAccount()) {
            extraArgs.setClientRequestedAccount(getAccount());
        }
        extraArgs.setClientRequiredFeatures(getRequiredFeatures());
        extraArgs.setClientApiFeatures(getApiFeatures());
        try {
            synchronized (this.zzrh) {
                if (this.zzri != null) {
                    this.zzri.getService(new GmsCallbacks(this, this.mDisconnectCount.get()), extraArgs);
                } else {
                    Log.w("GmsClient", "mServiceBroker is null, client disconnected");
                }
            }
        } catch (Throwable e) {
            Log.w("GmsClient", "IGmsServiceBroker.getService failed", e);
            triggerConnectionSuspended(1);
        } catch (SecurityException e2) {
            throw e2;
        } catch (Throwable e3) {
            Log.w("GmsClient", "IGmsServiceBroker.getService failed", e3);
            onPostInitHandler(8, null, null, this.mDisconnectCount.get());
        }
    }

    public Feature[] getRequiredFeatures() {
        return zzqz;
    }

    protected Set<Scope> getScopes() {
        return Collections.EMPTY_SET;
    }

    public final T getService() throws DeadObjectException {
        T t;
        synchronized (this.mLock) {
            if (this.zzrm == 5) {
                throw new DeadObjectException();
            }
            checkConnected();
            Preconditions.checkState(this.zzrj != null, "Client is connected but service is null");
            t = this.zzrj;
        }
        return t;
    }

    protected int getServiceBindFlags() {
        return TsExtractor.TS_STREAM_TYPE_AC3;
    }

    public IBinder getServiceBrokerBinder() {
        synchronized (this.zzrh) {
            if (this.zzri == null) {
                return null;
            }
            IBinder asBinder = this.zzri.asBinder();
            return asBinder;
        }
    }

    protected abstract String getServiceDescriptor();

    public Intent getSignInIntent() {
        throw new UnsupportedOperationException("Not a sign in API");
    }

    protected abstract String getStartServiceAction();

    protected String getStartServicePackage() {
        return "com.google.android.gms";
    }

    public boolean isConnected() {
        boolean z;
        synchronized (this.mLock) {
            z = this.zzrm == 4;
        }
        return z;
    }

    public boolean isConnecting() {
        boolean z;
        synchronized (this.mLock) {
            if (this.zzrm != 2) {
                if (this.zzrm != 3) {
                    z = false;
                }
            }
            z = true;
        }
        return z;
    }

    protected void onConnectedLocked(T t) {
        this.zzrc = System.currentTimeMillis();
    }

    protected void onConnectionFailed(ConnectionResult connectionResult) {
        this.zzrd = connectionResult.getErrorCode();
        this.zzre = System.currentTimeMillis();
    }

    protected void onConnectionSuspended(int i) {
        this.zzra = i;
        this.zzrb = System.currentTimeMillis();
    }

    protected void onPostInitHandler(int i, IBinder iBinder, Bundle bundle, int i2) {
        this.mHandler.sendMessage(this.mHandler.obtainMessage(1, i2, -1, new PostInitCallback(this, i, iBinder, bundle)));
    }

    protected void onPostServiceBindingHandler(int i, Bundle bundle, int i2) {
        this.mHandler.sendMessage(this.mHandler.obtainMessage(7, i2, -1, new PostServiceBindingCallback(this, i, bundle)));
    }

    void onSetConnectState(int i, T t) {
    }

    public void onUserSignOut(SignOutCallbacks signOutCallbacks) {
        signOutCallbacks.onSignOutComplete();
    }

    public boolean providesSignIn() {
        return false;
    }

    public boolean requiresAccount() {
        return false;
    }

    public boolean requiresGooglePlayServices() {
        return true;
    }

    public boolean requiresSignIn() {
        return false;
    }

    public void triggerConnectionSuspended(int i) {
        this.mHandler.sendMessage(this.mHandler.obtainMessage(6, this.mDisconnectCount.get(), i));
    }

    protected void triggerNotAvailable(ConnectionProgressReportCallbacks connectionProgressReportCallbacks, int i, PendingIntent pendingIntent) {
        this.mConnectionProgressReportCallbacks = (ConnectionProgressReportCallbacks) Preconditions.checkNotNull(connectionProgressReportCallbacks, "Connection progress callbacks cannot be null.");
        this.mHandler.sendMessage(this.mHandler.obtainMessage(3, this.mDisconnectCount.get(), i, pendingIntent));
    }
}
