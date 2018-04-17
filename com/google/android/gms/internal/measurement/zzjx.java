package com.google.android.gms.internal.measurement;

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

final class zzjx extends SSLSocket {
    private final SSLSocket zzare;

    zzjx(zzjw com_google_android_gms_internal_measurement_zzjw, SSLSocket sSLSocket) {
        this.zzare = sSLSocket;
    }

    public final void addHandshakeCompletedListener(HandshakeCompletedListener handshakeCompletedListener) {
        this.zzare.addHandshakeCompletedListener(handshakeCompletedListener);
    }

    public final void bind(SocketAddress socketAddress) throws IOException {
        this.zzare.bind(socketAddress);
    }

    public final synchronized void close() throws IOException {
        this.zzare.close();
    }

    public final void connect(SocketAddress socketAddress) throws IOException {
        this.zzare.connect(socketAddress);
    }

    public final void connect(SocketAddress socketAddress, int i) throws IOException {
        this.zzare.connect(socketAddress, i);
    }

    public final boolean equals(Object obj) {
        return this.zzare.equals(obj);
    }

    public final SocketChannel getChannel() {
        return this.zzare.getChannel();
    }

    public final boolean getEnableSessionCreation() {
        return this.zzare.getEnableSessionCreation();
    }

    public final String[] getEnabledCipherSuites() {
        return this.zzare.getEnabledCipherSuites();
    }

    public final String[] getEnabledProtocols() {
        return this.zzare.getEnabledProtocols();
    }

    public final InetAddress getInetAddress() {
        return this.zzare.getInetAddress();
    }

    public final InputStream getInputStream() throws IOException {
        return this.zzare.getInputStream();
    }

    public final boolean getKeepAlive() throws SocketException {
        return this.zzare.getKeepAlive();
    }

    public final InetAddress getLocalAddress() {
        return this.zzare.getLocalAddress();
    }

    public final int getLocalPort() {
        return this.zzare.getLocalPort();
    }

    public final SocketAddress getLocalSocketAddress() {
        return this.zzare.getLocalSocketAddress();
    }

    public final boolean getNeedClientAuth() {
        return this.zzare.getNeedClientAuth();
    }

    public final boolean getOOBInline() throws SocketException {
        return this.zzare.getOOBInline();
    }

    public final OutputStream getOutputStream() throws IOException {
        return this.zzare.getOutputStream();
    }

    public final int getPort() {
        return this.zzare.getPort();
    }

    public final synchronized int getReceiveBufferSize() throws SocketException {
        return this.zzare.getReceiveBufferSize();
    }

    public final SocketAddress getRemoteSocketAddress() {
        return this.zzare.getRemoteSocketAddress();
    }

    public final boolean getReuseAddress() throws SocketException {
        return this.zzare.getReuseAddress();
    }

    public final synchronized int getSendBufferSize() throws SocketException {
        return this.zzare.getSendBufferSize();
    }

    public final SSLSession getSession() {
        return this.zzare.getSession();
    }

    public final int getSoLinger() throws SocketException {
        return this.zzare.getSoLinger();
    }

    public final synchronized int getSoTimeout() throws SocketException {
        return this.zzare.getSoTimeout();
    }

    public final String[] getSupportedCipherSuites() {
        return this.zzare.getSupportedCipherSuites();
    }

    public final String[] getSupportedProtocols() {
        return this.zzare.getSupportedProtocols();
    }

    public final boolean getTcpNoDelay() throws SocketException {
        return this.zzare.getTcpNoDelay();
    }

    public final int getTrafficClass() throws SocketException {
        return this.zzare.getTrafficClass();
    }

    public final boolean getUseClientMode() {
        return this.zzare.getUseClientMode();
    }

    public final boolean getWantClientAuth() {
        return this.zzare.getWantClientAuth();
    }

    public final boolean isBound() {
        return this.zzare.isBound();
    }

    public final boolean isClosed() {
        return this.zzare.isClosed();
    }

    public final boolean isConnected() {
        return this.zzare.isConnected();
    }

    public final boolean isInputShutdown() {
        return this.zzare.isInputShutdown();
    }

    public final boolean isOutputShutdown() {
        return this.zzare.isOutputShutdown();
    }

    public final void removeHandshakeCompletedListener(HandshakeCompletedListener handshakeCompletedListener) {
        this.zzare.removeHandshakeCompletedListener(handshakeCompletedListener);
    }

    public final void sendUrgentData(int i) throws IOException {
        this.zzare.sendUrgentData(i);
    }

    public final void setEnableSessionCreation(boolean z) {
        this.zzare.setEnableSessionCreation(z);
    }

    public final void setEnabledCipherSuites(String[] strArr) {
        this.zzare.setEnabledCipherSuites(strArr);
    }

    public final void setEnabledProtocols(String[] strArr) {
        if (strArr != null && Arrays.asList(strArr).contains("SSLv3")) {
            List arrayList = new ArrayList(Arrays.asList(this.zzare.getEnabledProtocols()));
            if (arrayList.size() > 1) {
                arrayList.remove("SSLv3");
            }
            strArr = (String[]) arrayList.toArray(new String[arrayList.size()]);
        }
        this.zzare.setEnabledProtocols(strArr);
    }

    public final void setKeepAlive(boolean z) throws SocketException {
        this.zzare.setKeepAlive(z);
    }

    public final void setNeedClientAuth(boolean z) {
        this.zzare.setNeedClientAuth(z);
    }

    public final void setOOBInline(boolean z) throws SocketException {
        this.zzare.setOOBInline(z);
    }

    public final void setPerformancePreferences(int i, int i2, int i3) {
        this.zzare.setPerformancePreferences(i, i2, i3);
    }

    public final synchronized void setReceiveBufferSize(int i) throws SocketException {
        this.zzare.setReceiveBufferSize(i);
    }

    public final void setReuseAddress(boolean z) throws SocketException {
        this.zzare.setReuseAddress(z);
    }

    public final synchronized void setSendBufferSize(int i) throws SocketException {
        this.zzare.setSendBufferSize(i);
    }

    public final void setSoLinger(boolean z, int i) throws SocketException {
        this.zzare.setSoLinger(z, i);
    }

    public final synchronized void setSoTimeout(int i) throws SocketException {
        this.zzare.setSoTimeout(i);
    }

    public final void setTcpNoDelay(boolean z) throws SocketException {
        this.zzare.setTcpNoDelay(z);
    }

    public final void setTrafficClass(int i) throws SocketException {
        this.zzare.setTrafficClass(i);
    }

    public final void setUseClientMode(boolean z) {
        this.zzare.setUseClientMode(z);
    }

    public final void setWantClientAuth(boolean z) {
        this.zzare.setWantClientAuth(z);
    }

    public final void shutdownInput() throws IOException {
        this.zzare.shutdownInput();
    }

    public final void shutdownOutput() throws IOException {
        this.zzare.shutdownOutput();
    }

    public final void startHandshake() throws IOException {
        this.zzare.startHandshake();
    }

    public final String toString() {
        return this.zzare.toString();
    }
}
