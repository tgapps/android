package com.google.android.gms.common.internal;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;

public abstract class GmsClientSupervisor {
    private static final Object zztm = new Object();
    private static GmsClientSupervisor zztn;

    protected static final class ConnectionStatusConfig {
        private final ComponentName mComponentName = null;
        private final String zzto;
        private final String zztp;
        private final int zztq;

        public ConnectionStatusConfig(String str, String str2, int i) {
            this.zzto = Preconditions.checkNotEmpty(str);
            this.zztp = Preconditions.checkNotEmpty(str2);
            this.zztq = i;
        }

        public final boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (!(obj instanceof ConnectionStatusConfig)) {
                return false;
            }
            ConnectionStatusConfig connectionStatusConfig = (ConnectionStatusConfig) obj;
            return Objects.equal(this.zzto, connectionStatusConfig.zzto) && Objects.equal(this.zztp, connectionStatusConfig.zztp) && Objects.equal(this.mComponentName, connectionStatusConfig.mComponentName) && this.zztq == connectionStatusConfig.zztq;
        }

        public final int getBindFlags() {
            return this.zztq;
        }

        public final ComponentName getComponentName() {
            return this.mComponentName;
        }

        public final String getPackage() {
            return this.zztp;
        }

        public final Intent getStartServiceIntent(Context context) {
            return this.zzto != null ? new Intent(this.zzto).setPackage(this.zztp) : new Intent().setComponent(this.mComponentName);
        }

        public final int hashCode() {
            return Objects.hashCode(this.zzto, this.zztp, this.mComponentName, Integer.valueOf(this.zztq));
        }

        public final String toString() {
            return this.zzto == null ? this.mComponentName.flattenToString() : this.zzto;
        }
    }

    public static GmsClientSupervisor getInstance(Context context) {
        synchronized (zztm) {
            if (zztn == null) {
                zztn = new zzh(context.getApplicationContext());
            }
        }
        return zztn;
    }

    protected abstract boolean bindService(ConnectionStatusConfig connectionStatusConfig, ServiceConnection serviceConnection, String str);

    public boolean bindService(String str, String str2, int i, ServiceConnection serviceConnection, String str3) {
        return bindService(new ConnectionStatusConfig(str, str2, i), serviceConnection, str3);
    }

    protected abstract void unbindService(ConnectionStatusConfig connectionStatusConfig, ServiceConnection serviceConnection, String str);

    public void unbindService(String str, String str2, int i, ServiceConnection serviceConnection, String str3) {
        unbindService(new ConnectionStatusConfig(str, str2, i), serviceConnection, str3);
    }
}
