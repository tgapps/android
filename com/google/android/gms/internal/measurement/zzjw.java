package com.google.android.gms.internal.measurement;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;

final class zzjw extends SSLSocketFactory {
    private final SSLSocketFactory zzard;

    zzjw() {
        this(HttpsURLConnection.getDefaultSSLSocketFactory());
    }

    private zzjw(SSLSocketFactory sSLSocketFactory) {
        this.zzard = sSLSocketFactory;
    }

    private final SSLSocket zza(SSLSocket sSLSocket) {
        return new zzjx(this, sSLSocket);
    }

    public final Socket createSocket() throws IOException {
        return zza((SSLSocket) this.zzard.createSocket());
    }

    public final Socket createSocket(String str, int i) throws IOException {
        return zza((SSLSocket) this.zzard.createSocket(str, i));
    }

    public final Socket createSocket(String str, int i, InetAddress inetAddress, int i2) throws IOException {
        return zza((SSLSocket) this.zzard.createSocket(str, i, inetAddress, i2));
    }

    public final Socket createSocket(InetAddress inetAddress, int i) throws IOException {
        return zza((SSLSocket) this.zzard.createSocket(inetAddress, i));
    }

    public final Socket createSocket(InetAddress inetAddress, int i, InetAddress inetAddress2, int i2) throws IOException {
        return zza((SSLSocket) this.zzard.createSocket(inetAddress, i, inetAddress2, i2));
    }

    public final Socket createSocket(Socket socket, String str, int i, boolean z) throws IOException {
        return zza((SSLSocket) this.zzard.createSocket(socket, str, i, z));
    }

    public final String[] getDefaultCipherSuites() {
        return this.zzard.getDefaultCipherSuites();
    }

    public final String[] getSupportedCipherSuites() {
        return this.zzard.getSupportedCipherSuites();
    }
}
