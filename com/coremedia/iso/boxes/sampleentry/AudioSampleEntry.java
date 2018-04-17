package com.coremedia.iso.boxes.sampleentry;

import com.coremedia.iso.BoxParser;
import com.coremedia.iso.boxes.Box;
import com.coremedia.iso.boxes.Container;
import com.googlecode.mp4parser.DataSource;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.WritableByteChannel;

public final class AudioSampleEntry extends AbstractSampleEntry {
    static final /* synthetic */ boolean $assertionsDisabled = false;
    public static final String TYPE1 = "samr";
    public static final String TYPE10 = "mlpa";
    public static final String TYPE11 = "dtsl";
    public static final String TYPE12 = "dtsh";
    public static final String TYPE13 = "dtse";
    public static final String TYPE2 = "sawb";
    public static final String TYPE3 = "mp4a";
    public static final String TYPE4 = "drms";
    public static final String TYPE5 = "alac";
    public static final String TYPE7 = "owma";
    public static final String TYPE8 = "ac-3";
    public static final String TYPE9 = "ec-3";
    public static final String TYPE_ENCRYPTED = "enca";
    private long bytesPerFrame;
    private long bytesPerPacket;
    private long bytesPerSample;
    private int channelCount;
    private int compressionId;
    private int packetSize;
    private int reserved1;
    private long reserved2;
    private long sampleRate;
    private int sampleSize;
    private long samplesPerPacket;
    private int soundVersion;
    private byte[] soundVersion2Data;

    class AnonymousClass1 implements Box {
        private final /* synthetic */ ByteBuffer val$owmaSpecifics;
        private final /* synthetic */ long val$remaining;

        AnonymousClass1(long j, ByteBuffer byteBuffer) {
            this.val$remaining = j;
            this.val$owmaSpecifics = byteBuffer;
        }

        public Container getParent() {
            return AudioSampleEntry.this;
        }

        public void setParent(Container parent) {
        }

        public long getSize() {
            return this.val$remaining;
        }

        public long getOffset() {
            return 0;
        }

        public String getType() {
            return "----";
        }

        public void getBox(WritableByteChannel writableByteChannel) throws IOException {
            this.val$owmaSpecifics.rewind();
            writableByteChannel.write(this.val$owmaSpecifics);
        }

        public void parse(DataSource dataSource, ByteBuffer header, long contentSize, BoxParser boxParser) throws IOException {
            throw new RuntimeException("NotImplemented");
        }
    }

    public void getBox(java.nio.channels.WritableByteChannel r1) throws java.io.IOException {
        /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.DecodeException: Load method exception in method: com.coremedia.iso.boxes.sampleentry.AudioSampleEntry.getBox(java.nio.channels.WritableByteChannel):void
	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:116)
	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:249)
	at jadx.core.ProcessClass.process(ProcessClass.java:34)
	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:59)
	at jadx.core.ProcessClass.process(ProcessClass.java:42)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:306)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler$1.run(JadxDecompiler.java:199)
Caused by: java.lang.NullPointerException
*/
        /*
        r0 = this;
        r0 = r8.getHeader();
        r9.write(r0);
        r0 = r8.soundVersion;
        r1 = 0;
        r2 = 16;
        r3 = 1;
        if (r0 != r3) goto L_0x0012;
    L_0x0010:
        r0 = r2;
        goto L_0x0014;
        r0 = r1;
        r4 = 28;
        r4 = r4 + r0;
        r0 = r8.soundVersion;
        r5 = 2;
        if (r0 != r5) goto L_0x001f;
        r1 = 36;
        r4 = r4 + r1;
        r0 = java.nio.ByteBuffer.allocate(r4);
        r1 = 6;
        r0.position(r1);
        r1 = r8.dataReferenceIndex;
        com.coremedia.iso.IsoTypeWriter.writeUInt16(r0, r1);
        r1 = r8.soundVersion;
        com.coremedia.iso.IsoTypeWriter.writeUInt16(r0, r1);
        r1 = r8.reserved1;
        com.coremedia.iso.IsoTypeWriter.writeUInt16(r0, r1);
        r6 = r8.reserved2;
        com.coremedia.iso.IsoTypeWriter.writeUInt32(r0, r6);
        r1 = r8.channelCount;
        com.coremedia.iso.IsoTypeWriter.writeUInt16(r0, r1);
        r1 = r8.sampleSize;
        com.coremedia.iso.IsoTypeWriter.writeUInt16(r0, r1);
        r1 = r8.compressionId;
        com.coremedia.iso.IsoTypeWriter.writeUInt16(r0, r1);
        r1 = r8.packetSize;
        com.coremedia.iso.IsoTypeWriter.writeUInt16(r0, r1);
        r1 = r8.type;
        r4 = "mlpa";
        r1 = r1.equals(r4);
        if (r1 == 0) goto L_0x0062;
        r1 = r8.getSampleRate();
        com.coremedia.iso.IsoTypeWriter.writeUInt32(r0, r1);
        goto L_0x006b;
        r6 = r8.getSampleRate();
        r1 = r6 << r2;
        com.coremedia.iso.IsoTypeWriter.writeUInt32(r0, r1);
        r1 = r8.soundVersion;
        if (r1 != r3) goto L_0x0083;
        r1 = r8.samplesPerPacket;
        com.coremedia.iso.IsoTypeWriter.writeUInt32(r0, r1);
        r1 = r8.bytesPerPacket;
        com.coremedia.iso.IsoTypeWriter.writeUInt32(r0, r1);
        r1 = r8.bytesPerFrame;
        com.coremedia.iso.IsoTypeWriter.writeUInt32(r0, r1);
        r1 = r8.bytesPerSample;
        com.coremedia.iso.IsoTypeWriter.writeUInt32(r0, r1);
        r1 = r8.soundVersion;
        if (r1 != r5) goto L_0x00a0;
        r1 = r8.samplesPerPacket;
        com.coremedia.iso.IsoTypeWriter.writeUInt32(r0, r1);
        r1 = r8.bytesPerPacket;
        com.coremedia.iso.IsoTypeWriter.writeUInt32(r0, r1);
        r1 = r8.bytesPerFrame;
        com.coremedia.iso.IsoTypeWriter.writeUInt32(r0, r1);
        r1 = r8.bytesPerSample;
        com.coremedia.iso.IsoTypeWriter.writeUInt32(r0, r1);
        r1 = r8.soundVersion2Data;
        r0.put(r1);
        r1 = r0.rewind();
        r1 = (java.nio.ByteBuffer) r1;
        r9.write(r1);
        r8.writeContainer(r9);
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.coremedia.iso.boxes.sampleentry.AudioSampleEntry.getBox(java.nio.channels.WritableByteChannel):void");
    }

    public long getSize() {
        /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.DecodeException: Load method exception in method: com.coremedia.iso.boxes.sampleentry.AudioSampleEntry.getSize():long
	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:116)
	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:249)
	at jadx.core.ProcessClass.process(ProcessClass.java:34)
	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:59)
	at jadx.core.ProcessClass.process(ProcessClass.java:42)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:306)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler$1.run(JadxDecompiler.java:199)
Caused by: java.lang.NullPointerException
*/
        /*
        r0 = this;
        r0 = r8.soundVersion;
        r1 = 0;
        r2 = 16;
        r3 = 1;
        if (r0 != r3) goto L_0x000b;
    L_0x0009:
        r0 = r2;
        goto L_0x000d;
        r0 = r1;
        r3 = 28;
        r3 = r3 + r0;
        r0 = r8.soundVersion;
        r4 = 2;
        if (r0 != r4) goto L_0x0018;
        r1 = 36;
        r3 = r3 + r1;
        r0 = (long) r3;
        r3 = r8.getContainerSize();
        r5 = r0 + r3;
        r0 = r8.largeBox;
        if (r0 != 0) goto L_0x0035;
        r0 = 8;
        r3 = r5 + r0;
        r0 = 4294967296; // 0x100000000 float:0.0 double:2.121995791E-314;
        r7 = (r3 > r0 ? 1 : (r3 == r0 ? 0 : -1));
        if (r7 < 0) goto L_0x0032;
        goto L_0x0035;
        r2 = 8;
        r0 = (long) r2;
        r2 = r5 + r0;
        return r2;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.coremedia.iso.boxes.sampleentry.AudioSampleEntry.getSize():long");
    }

    public void parse(com.googlecode.mp4parser.DataSource r1, java.nio.ByteBuffer r2, long r3, com.coremedia.iso.BoxParser r5) throws java.io.IOException {
        /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.DecodeException: Load method exception in method: com.coremedia.iso.boxes.sampleentry.AudioSampleEntry.parse(com.googlecode.mp4parser.DataSource, java.nio.ByteBuffer, long, com.coremedia.iso.BoxParser):void
	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:116)
	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:249)
	at jadx.core.ProcessClass.process(ProcessClass.java:34)
	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:59)
	at jadx.core.ProcessClass.process(ProcessClass.java:42)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:306)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler$1.run(JadxDecompiler.java:199)
Caused by: java.lang.NullPointerException
*/
        /*
        r0 = this;
        r0 = r13;
        r1 = r14;
        r2 = 28;
        r2 = java.nio.ByteBuffer.allocate(r2);
        r1.read(r2);
        r3 = 6;
        r2.position(r3);
        r3 = com.coremedia.iso.IsoTypeReader.readUInt16(r2);
        r0.dataReferenceIndex = r3;
        r3 = com.coremedia.iso.IsoTypeReader.readUInt16(r2);
        r0.soundVersion = r3;
        r3 = com.coremedia.iso.IsoTypeReader.readUInt16(r2);
        r0.reserved1 = r3;
        r3 = com.coremedia.iso.IsoTypeReader.readUInt32(r2);
        r0.reserved2 = r3;
        r3 = com.coremedia.iso.IsoTypeReader.readUInt16(r2);
        r0.channelCount = r3;
        r3 = com.coremedia.iso.IsoTypeReader.readUInt16(r2);
        r0.sampleSize = r3;
        r3 = com.coremedia.iso.IsoTypeReader.readUInt16(r2);
        r0.compressionId = r3;
        r3 = com.coremedia.iso.IsoTypeReader.readUInt16(r2);
        r0.packetSize = r3;
        r3 = com.coremedia.iso.IsoTypeReader.readUInt32(r2);
        r0.sampleRate = r3;
        r3 = r0.type;
        r4 = "mlpa";
        r3 = r3.equals(r4);
        r4 = 16;
        if (r3 != 0) goto L_0x0056;
    L_0x0051:
        r5 = r0.sampleRate;
        r5 = r5 >>> r4;
        r0.sampleRate = r5;
    L_0x0056:
        r3 = r0.soundVersion;
        r5 = 1;
        if (r3 != r5) goto L_0x007d;
    L_0x005b:
        r3 = java.nio.ByteBuffer.allocate(r4);
        r1.read(r3);
        r3.rewind();
        r6 = com.coremedia.iso.IsoTypeReader.readUInt32(r3);
        r0.samplesPerPacket = r6;
        r6 = com.coremedia.iso.IsoTypeReader.readUInt32(r3);
        r0.bytesPerPacket = r6;
        r6 = com.coremedia.iso.IsoTypeReader.readUInt32(r3);
        r0.bytesPerFrame = r6;
        r6 = com.coremedia.iso.IsoTypeReader.readUInt32(r3);
        r0.bytesPerSample = r6;
    L_0x007d:
        r3 = r0.soundVersion;
        r6 = 36;
        r7 = 2;
        if (r3 != r7) goto L_0x00b1;
    L_0x0084:
        r3 = java.nio.ByteBuffer.allocate(r6);
        r1.read(r3);
        r3.rewind();
        r8 = com.coremedia.iso.IsoTypeReader.readUInt32(r3);
        r0.samplesPerPacket = r8;
        r8 = com.coremedia.iso.IsoTypeReader.readUInt32(r3);
        r0.bytesPerPacket = r8;
        r8 = com.coremedia.iso.IsoTypeReader.readUInt32(r3);
        r0.bytesPerFrame = r8;
        r8 = com.coremedia.iso.IsoTypeReader.readUInt32(r3);
        r0.bytesPerSample = r8;
        r8 = 20;
        r8 = new byte[r8];
        r0.soundVersion2Data = r8;
        r8 = r0.soundVersion2Data;
        r3.get(r8);
    L_0x00b1:
        r3 = "owma";
        r8 = r0.type;
        r3 = r3.equals(r8);
        r8 = 28;
        r10 = 0;
        if (r3 == 0) goto L_0x00f0;
    L_0x00be:
        r3 = java.lang.System.err;
        r11 = "owma";
        r3.println(r11);
        r11 = r16 - r8;
        r3 = r0.soundVersion;
        if (r3 != r5) goto L_0x00cc;
        goto L_0x00cd;
        r4 = r10;
        r3 = (long) r4;
        r8 = r11 - r3;
        r3 = r0.soundVersion;
        if (r3 != r7) goto L_0x00d5;
        goto L_0x00d6;
        r6 = r10;
        r3 = (long) r6;
        r5 = r8 - r3;
        r3 = com.googlecode.mp4parser.util.CastUtils.l2i(r5);
        r3 = java.nio.ByteBuffer.allocate(r3);
        r1.read(r3);
        r4 = new com.coremedia.iso.boxes.sampleentry.AudioSampleEntry$1;
        r4.<init>(r5, r3);
        r0.addBox(r4);
        r3 = r18;
        goto L_0x010b;
        r11 = r16 - r8;
        r3 = r0.soundVersion;
        if (r3 != r5) goto L_0x00f8;
        goto L_0x00f9;
        r4 = r10;
        r3 = (long) r4;
        r8 = r11 - r3;
        r3 = r0.soundVersion;
        if (r3 != r7) goto L_0x0101;
        goto L_0x0102;
        r6 = r10;
        r3 = (long) r6;
        r5 = r8 - r3;
        r3 = r18;
        r0.initContainer(r1, r5, r3);
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.coremedia.iso.boxes.sampleentry.AudioSampleEntry.parse(com.googlecode.mp4parser.DataSource, java.nio.ByteBuffer, long, com.coremedia.iso.BoxParser):void");
    }

    public AudioSampleEntry(String type) {
        super(type);
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getChannelCount() {
        return this.channelCount;
    }

    public int getSampleSize() {
        return this.sampleSize;
    }

    public long getSampleRate() {
        return this.sampleRate;
    }

    public int getSoundVersion() {
        return this.soundVersion;
    }

    public int getCompressionId() {
        return this.compressionId;
    }

    public int getPacketSize() {
        return this.packetSize;
    }

    public long getSamplesPerPacket() {
        return this.samplesPerPacket;
    }

    public long getBytesPerPacket() {
        return this.bytesPerPacket;
    }

    public long getBytesPerFrame() {
        return this.bytesPerFrame;
    }

    public long getBytesPerSample() {
        return this.bytesPerSample;
    }

    public byte[] getSoundVersion2Data() {
        return this.soundVersion2Data;
    }

    public int getReserved1() {
        return this.reserved1;
    }

    public long getReserved2() {
        return this.reserved2;
    }

    public void setChannelCount(int channelCount) {
        this.channelCount = channelCount;
    }

    public void setSampleSize(int sampleSize) {
        this.sampleSize = sampleSize;
    }

    public void setSampleRate(long sampleRate) {
        this.sampleRate = sampleRate;
    }

    public void setSoundVersion(int soundVersion) {
        this.soundVersion = soundVersion;
    }

    public void setCompressionId(int compressionId) {
        this.compressionId = compressionId;
    }

    public void setPacketSize(int packetSize) {
        this.packetSize = packetSize;
    }

    public void setSamplesPerPacket(long samplesPerPacket) {
        this.samplesPerPacket = samplesPerPacket;
    }

    public void setBytesPerPacket(long bytesPerPacket) {
        this.bytesPerPacket = bytesPerPacket;
    }

    public void setBytesPerFrame(long bytesPerFrame) {
        this.bytesPerFrame = bytesPerFrame;
    }

    public void setBytesPerSample(long bytesPerSample) {
        this.bytesPerSample = bytesPerSample;
    }

    public void setReserved1(int reserved1) {
        this.reserved1 = reserved1;
    }

    public void setReserved2(long reserved2) {
        this.reserved2 = reserved2;
    }

    public void setSoundVersion2Data(byte[] soundVersion2Data) {
        this.soundVersion2Data = soundVersion2Data;
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder("AudioSampleEntry{bytesPerSample=");
        stringBuilder.append(this.bytesPerSample);
        stringBuilder.append(", bytesPerFrame=");
        stringBuilder.append(this.bytesPerFrame);
        stringBuilder.append(", bytesPerPacket=");
        stringBuilder.append(this.bytesPerPacket);
        stringBuilder.append(", samplesPerPacket=");
        stringBuilder.append(this.samplesPerPacket);
        stringBuilder.append(", packetSize=");
        stringBuilder.append(this.packetSize);
        stringBuilder.append(", compressionId=");
        stringBuilder.append(this.compressionId);
        stringBuilder.append(", soundVersion=");
        stringBuilder.append(this.soundVersion);
        stringBuilder.append(", sampleRate=");
        stringBuilder.append(this.sampleRate);
        stringBuilder.append(", sampleSize=");
        stringBuilder.append(this.sampleSize);
        stringBuilder.append(", channelCount=");
        stringBuilder.append(this.channelCount);
        stringBuilder.append(", boxes=");
        stringBuilder.append(getBoxes());
        stringBuilder.append('}');
        return stringBuilder.toString();
    }
}
