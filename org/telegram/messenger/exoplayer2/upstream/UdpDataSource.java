package org.telegram.messenger.exoplayer2.upstream;

import android.net.Uri;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.MulticastSocket;

public final class UdpDataSource implements DataSource {
    public static final int DEAFULT_SOCKET_TIMEOUT_MILLIS = 8000;
    public static final int DEFAULT_MAX_PACKET_SIZE = 2000;
    private InetAddress address;
    private final TransferListener<? super UdpDataSource> listener;
    private MulticastSocket multicastSocket;
    private boolean opened;
    private final DatagramPacket packet;
    private final byte[] packetBuffer;
    private int packetRemaining;
    private DatagramSocket socket;
    private InetSocketAddress socketAddress;
    private final int socketTimeoutMillis;
    private Uri uri;

    public static final class UdpDataSourceException extends IOException {
        public UdpDataSourceException(IOException cause) {
            super(cause);
        }
    }

    public long open(org.telegram.messenger.exoplayer2.upstream.DataSpec r1) throws org.telegram.messenger.exoplayer2.upstream.UdpDataSource.UdpDataSourceException {
        /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.DecodeException: Load method exception in method: org.telegram.messenger.exoplayer2.upstream.UdpDataSource.open(org.telegram.messenger.exoplayer2.upstream.DataSpec):long
	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:116)
	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:249)
	at jadx.core.ProcessClass.process(ProcessClass.java:34)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:306)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler$1.run(JadxDecompiler.java:199)
Caused by: java.lang.NullPointerException
*/
        /*
        r0 = this;
        r0 = r5.uri;
        r4.uri = r0;
        r0 = r4.uri;
        r0 = r0.getHost();
        r1 = r4.uri;
        r1 = r1.getPort();
        r2 = java.net.InetAddress.getByName(r0);	 Catch:{ IOException -> 0x0064 }
        r4.address = r2;	 Catch:{ IOException -> 0x0064 }
        r2 = new java.net.InetSocketAddress;	 Catch:{ IOException -> 0x0064 }
        r3 = r4.address;	 Catch:{ IOException -> 0x0064 }
        r2.<init>(r3, r1);	 Catch:{ IOException -> 0x0064 }
        r4.socketAddress = r2;	 Catch:{ IOException -> 0x0064 }
        r2 = r4.address;	 Catch:{ IOException -> 0x0064 }
        r2 = r2.isMulticastAddress();	 Catch:{ IOException -> 0x0064 }
        if (r2 == 0) goto L_0x003c;	 Catch:{ IOException -> 0x0064 }
    L_0x0027:
        r2 = new java.net.MulticastSocket;	 Catch:{ IOException -> 0x0064 }
        r3 = r4.socketAddress;	 Catch:{ IOException -> 0x0064 }
        r2.<init>(r3);	 Catch:{ IOException -> 0x0064 }
        r4.multicastSocket = r2;	 Catch:{ IOException -> 0x0064 }
        r2 = r4.multicastSocket;	 Catch:{ IOException -> 0x0064 }
        r3 = r4.address;	 Catch:{ IOException -> 0x0064 }
        r2.joinGroup(r3);	 Catch:{ IOException -> 0x0064 }
        r2 = r4.multicastSocket;	 Catch:{ IOException -> 0x0064 }
        r4.socket = r2;	 Catch:{ IOException -> 0x0064 }
        goto L_0x0045;	 Catch:{ IOException -> 0x0064 }
    L_0x003c:
        r2 = new java.net.DatagramSocket;	 Catch:{ IOException -> 0x0064 }
        r3 = r4.socketAddress;	 Catch:{ IOException -> 0x0064 }
        r2.<init>(r3);	 Catch:{ IOException -> 0x0064 }
        r4.socket = r2;	 Catch:{ IOException -> 0x0064 }
        r2 = r4.socket;	 Catch:{ SocketException -> 0x005d }
        r3 = r4.socketTimeoutMillis;	 Catch:{ SocketException -> 0x005d }
        r2.setSoTimeout(r3);	 Catch:{ SocketException -> 0x005d }
        r2 = 1;
        r4.opened = r2;
        r2 = r4.listener;
        if (r2 == 0) goto L_0x005a;
        r2 = r4.listener;
        r2.onTransferStart(r4, r5);
        r2 = -1;
        return r2;
    L_0x005d:
        r2 = move-exception;
        r3 = new org.telegram.messenger.exoplayer2.upstream.UdpDataSource$UdpDataSourceException;
        r3.<init>(r2);
        throw r3;
    L_0x0064:
        r2 = move-exception;
        r3 = new org.telegram.messenger.exoplayer2.upstream.UdpDataSource$UdpDataSourceException;
        r3.<init>(r2);
        throw r3;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.telegram.messenger.exoplayer2.upstream.UdpDataSource.open(org.telegram.messenger.exoplayer2.upstream.DataSpec):long");
    }

    public UdpDataSource(TransferListener<? super UdpDataSource> listener) {
        this(listener, 2000);
    }

    public UdpDataSource(TransferListener<? super UdpDataSource> listener, int maxPacketSize) {
        this(listener, maxPacketSize, 8000);
    }

    public UdpDataSource(TransferListener<? super UdpDataSource> listener, int maxPacketSize, int socketTimeoutMillis) {
        this.listener = listener;
        this.socketTimeoutMillis = socketTimeoutMillis;
        this.packetBuffer = new byte[maxPacketSize];
        this.packet = new DatagramPacket(this.packetBuffer, 0, maxPacketSize);
    }

    public int read(byte[] buffer, int offset, int readLength) throws UdpDataSourceException {
        if (readLength == 0) {
            return 0;
        }
        if (this.packetRemaining == 0) {
            try {
                this.socket.receive(this.packet);
                this.packetRemaining = this.packet.getLength();
                if (this.listener != null) {
                    this.listener.onBytesTransferred(this, this.packetRemaining);
                }
            } catch (IOException e) {
                throw new UdpDataSourceException(e);
            }
        }
        int packetOffset = this.packet.getLength() - this.packetRemaining;
        int bytesToRead = Math.min(this.packetRemaining, readLength);
        System.arraycopy(this.packetBuffer, packetOffset, buffer, offset, bytesToRead);
        this.packetRemaining -= bytesToRead;
        return bytesToRead;
    }

    public Uri getUri() {
        return this.uri;
    }

    public void close() {
        this.uri = null;
        if (this.multicastSocket != null) {
            try {
                this.multicastSocket.leaveGroup(this.address);
            } catch (IOException e) {
            }
            this.multicastSocket = null;
        }
        if (this.socket != null) {
            this.socket.close();
            this.socket = null;
        }
        this.address = null;
        this.socketAddress = null;
        this.packetRemaining = 0;
        if (this.opened) {
            this.opened = false;
            if (this.listener != null) {
                this.listener.onTransferEnd(this);
            }
        }
    }
}
