package com.google.android.gms.measurement.internal;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.SocketAddress;
import java.net.SocketException;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.net.ssl.HandshakeCompletedListener;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocket;

final class zzfm extends SSLSocket {
    private final SSLSocket zzaun;

    zzfm(zzfl com_google_android_gms_measurement_internal_zzfl, SSLSocket sSLSocket) {
        this.zzaun = sSLSocket;
    }

    public final void setEnabledProtocols(String[] strArr) {
        if (strArr != null && Arrays.asList(strArr).contains("SSLv3")) {
            List arrayList = new ArrayList(Arrays.asList(this.zzaun.getEnabledProtocols()));
            if (arrayList.size() > 1) {
                arrayList.remove("SSLv3");
            }
            strArr = (String[]) arrayList.toArray(new String[arrayList.size()]);
        }
        this.zzaun.setEnabledProtocols(strArr);
    }

    public final String[] getSupportedCipherSuites() {
        return this.zzaun.getSupportedCipherSuites();
    }

    public final String[] getEnabledCipherSuites() {
        return this.zzaun.getEnabledCipherSuites();
    }

    public final void setEnabledCipherSuites(String[] strArr) {
        this.zzaun.setEnabledCipherSuites(strArr);
    }

    public final String[] getSupportedProtocols() {
        return this.zzaun.getSupportedProtocols();
    }

    public final String[] getEnabledProtocols() {
        return this.zzaun.getEnabledProtocols();
    }

    public final SSLSession getSession() {
        return this.zzaun.getSession();
    }

    public final void addHandshakeCompletedListener(HandshakeCompletedListener handshakeCompletedListener) {
        this.zzaun.addHandshakeCompletedListener(handshakeCompletedListener);
    }

    public final void removeHandshakeCompletedListener(HandshakeCompletedListener handshakeCompletedListener) {
        this.zzaun.removeHandshakeCompletedListener(handshakeCompletedListener);
    }

    public final void startHandshake() throws IOException {
        this.zzaun.startHandshake();
    }

    public final void setUseClientMode(boolean z) {
        this.zzaun.setUseClientMode(z);
    }

    public final boolean getUseClientMode() {
        return this.zzaun.getUseClientMode();
    }

    public final void setNeedClientAuth(boolean z) {
        this.zzaun.setNeedClientAuth(z);
    }

    public final void setWantClientAuth(boolean z) {
        this.zzaun.setWantClientAuth(z);
    }

    public final boolean getNeedClientAuth() {
        return this.zzaun.getNeedClientAuth();
    }

    public final boolean getWantClientAuth() {
        return this.zzaun.getWantClientAuth();
    }

    public final void setEnableSessionCreation(boolean z) {
        this.zzaun.setEnableSessionCreation(z);
    }

    public final boolean getEnableSessionCreation() {
        return this.zzaun.getEnableSessionCreation();
    }

    public final void bind(SocketAddress socketAddress) throws IOException {
        this.zzaun.bind(socketAddress);
    }

    public final synchronized void close() throws IOException {
        this.zzaun.close();
    }

    public final void connect(SocketAddress socketAddress) throws IOException {
        this.zzaun.connect(socketAddress);
    }

    public final void connect(SocketAddress socketAddress, int i) throws IOException {
        this.zzaun.connect(socketAddress, i);
    }

    public final SocketChannel getChannel() {
        return this.zzaun.getChannel();
    }

    public final InetAddress getInetAddress() {
        return this.zzaun.getInetAddress();
    }

    public final InputStream getInputStream() throws IOException {
        return this.zzaun.getInputStream();
    }

    public final boolean getKeepAlive() throws SocketException {
        return this.zzaun.getKeepAlive();
    }

    public final InetAddress getLocalAddress() {
        return this.zzaun.getLocalAddress();
    }

    public final int getLocalPort() {
        return this.zzaun.getLocalPort();
    }

    public final SocketAddress getLocalSocketAddress() {
        return this.zzaun.getLocalSocketAddress();
    }

    public final boolean getOOBInline() throws SocketException {
        return this.zzaun.getOOBInline();
    }

    public final OutputStream getOutputStream() throws IOException {
        return this.zzaun.getOutputStream();
    }

    public final int getPort() {
        return this.zzaun.getPort();
    }

    public final synchronized int getReceiveBufferSize() throws SocketException {
        return this.zzaun.getReceiveBufferSize();
    }

    public final SocketAddress getRemoteSocketAddress() {
        return this.zzaun.getRemoteSocketAddress();
    }

    public final boolean getReuseAddress() throws SocketException {
        return this.zzaun.getReuseAddress();
    }

    public final synchronized int getSendBufferSize() throws SocketException {
        return this.zzaun.getSendBufferSize();
    }

    public final int getSoLinger() throws SocketException {
        return this.zzaun.getSoLinger();
    }

    public final synchronized int getSoTimeout() throws SocketException {
        return this.zzaun.getSoTimeout();
    }

    public final boolean getTcpNoDelay() throws SocketException {
        return this.zzaun.getTcpNoDelay();
    }

    public final int getTrafficClass() throws SocketException {
        return this.zzaun.getTrafficClass();
    }

    public final boolean isBound() {
        return this.zzaun.isBound();
    }

    public final boolean isClosed() {
        return this.zzaun.isClosed();
    }

    public final boolean isConnected() {
        return this.zzaun.isConnected();
    }

    public final boolean isInputShutdown() {
        return this.zzaun.isInputShutdown();
    }

    public final boolean isOutputShutdown() {
        return this.zzaun.isOutputShutdown();
    }

    public final void sendUrgentData(int i) throws IOException {
        this.zzaun.sendUrgentData(i);
    }

    public final void setKeepAlive(boolean z) throws SocketException {
        this.zzaun.setKeepAlive(z);
    }

    public final void setOOBInline(boolean z) throws SocketException {
        this.zzaun.setOOBInline(z);
    }

    public final void setPerformancePreferences(int i, int i2, int i3) {
        this.zzaun.setPerformancePreferences(i, i2, i3);
    }

    public final synchronized void setReceiveBufferSize(int i) throws SocketException {
        this.zzaun.setReceiveBufferSize(i);
    }

    public final void setReuseAddress(boolean z) throws SocketException {
        this.zzaun.setReuseAddress(z);
    }

    public final synchronized void setSendBufferSize(int i) throws SocketException {
        this.zzaun.setSendBufferSize(i);
    }

    public final void setSoLinger(boolean z, int i) throws SocketException {
        this.zzaun.setSoLinger(z, i);
    }

    public final synchronized void setSoTimeout(int i) throws SocketException {
        this.zzaun.setSoTimeout(i);
    }

    public final void setTcpNoDelay(boolean z) throws SocketException {
        this.zzaun.setTcpNoDelay(z);
    }

    public final void setTrafficClass(int i) throws SocketException {
        this.zzaun.setTrafficClass(i);
    }

    public final void shutdownInput() throws IOException {
        this.zzaun.shutdownInput();
    }

    public final void shutdownOutput() throws IOException {
        this.zzaun.shutdownOutput();
    }

    public final String toString() {
        return this.zzaun.toString();
    }

    public final boolean equals(Object obj) {
        return this.zzaun.equals(obj);
    }
}
