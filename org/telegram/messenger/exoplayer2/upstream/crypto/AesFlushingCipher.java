package org.telegram.messenger.exoplayer2.upstream.crypto;

import java.nio.ByteBuffer;
import javax.crypto.Cipher;
import javax.crypto.ShortBufferException;
import org.telegram.messenger.exoplayer2.util.Assertions;

public final class AesFlushingCipher {
    private final int blockSize;
    private final Cipher cipher;
    private final byte[] flushedBlock;
    private int pendingXorBytes;
    private final byte[] zerosBlock;

    public AesFlushingCipher(int r1, byte[] r2, long r3, long r5) {
        /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.DecodeException: Load method exception in method: org.telegram.messenger.exoplayer2.upstream.crypto.AesFlushingCipher.<init>(int, byte[], long, long):void
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
        r8.<init>();
        r0 = "AES/CTR/NoPadding";	 Catch:{ NoSuchAlgorithmException -> 0x0055, NoSuchAlgorithmException -> 0x0055, NoSuchAlgorithmException -> 0x0055, NoSuchAlgorithmException -> 0x0055 }
        r0 = javax.crypto.Cipher.getInstance(r0);	 Catch:{ NoSuchAlgorithmException -> 0x0055, NoSuchAlgorithmException -> 0x0055, NoSuchAlgorithmException -> 0x0055, NoSuchAlgorithmException -> 0x0055 }
        r8.cipher = r0;	 Catch:{ NoSuchAlgorithmException -> 0x0055, NoSuchAlgorithmException -> 0x0055, NoSuchAlgorithmException -> 0x0055, NoSuchAlgorithmException -> 0x0055 }
        r0 = r8.cipher;	 Catch:{ NoSuchAlgorithmException -> 0x0055, NoSuchAlgorithmException -> 0x0055, NoSuchAlgorithmException -> 0x0055, NoSuchAlgorithmException -> 0x0055 }
        r0 = r0.getBlockSize();	 Catch:{ NoSuchAlgorithmException -> 0x0055, NoSuchAlgorithmException -> 0x0055, NoSuchAlgorithmException -> 0x0055, NoSuchAlgorithmException -> 0x0055 }
        r8.blockSize = r0;	 Catch:{ NoSuchAlgorithmException -> 0x0055, NoSuchAlgorithmException -> 0x0055, NoSuchAlgorithmException -> 0x0055, NoSuchAlgorithmException -> 0x0055 }
        r0 = r8.blockSize;	 Catch:{ NoSuchAlgorithmException -> 0x0055, NoSuchAlgorithmException -> 0x0055, NoSuchAlgorithmException -> 0x0055, NoSuchAlgorithmException -> 0x0055 }
        r0 = new byte[r0];	 Catch:{ NoSuchAlgorithmException -> 0x0055, NoSuchAlgorithmException -> 0x0055, NoSuchAlgorithmException -> 0x0055, NoSuchAlgorithmException -> 0x0055 }
        r8.zerosBlock = r0;	 Catch:{ NoSuchAlgorithmException -> 0x0055, NoSuchAlgorithmException -> 0x0055, NoSuchAlgorithmException -> 0x0055, NoSuchAlgorithmException -> 0x0055 }
        r0 = r8.blockSize;	 Catch:{ NoSuchAlgorithmException -> 0x0055, NoSuchAlgorithmException -> 0x0055, NoSuchAlgorithmException -> 0x0055, NoSuchAlgorithmException -> 0x0055 }
        r0 = new byte[r0];	 Catch:{ NoSuchAlgorithmException -> 0x0055, NoSuchAlgorithmException -> 0x0055, NoSuchAlgorithmException -> 0x0055, NoSuchAlgorithmException -> 0x0055 }
        r8.flushedBlock = r0;	 Catch:{ NoSuchAlgorithmException -> 0x0055, NoSuchAlgorithmException -> 0x0055, NoSuchAlgorithmException -> 0x0055, NoSuchAlgorithmException -> 0x0055 }
        r0 = r8.blockSize;	 Catch:{ NoSuchAlgorithmException -> 0x0055, NoSuchAlgorithmException -> 0x0055, NoSuchAlgorithmException -> 0x0055, NoSuchAlgorithmException -> 0x0055 }
        r0 = (long) r0;	 Catch:{ NoSuchAlgorithmException -> 0x0055, NoSuchAlgorithmException -> 0x0055, NoSuchAlgorithmException -> 0x0055, NoSuchAlgorithmException -> 0x0055 }
        r0 = r13 / r0;	 Catch:{ NoSuchAlgorithmException -> 0x0055, NoSuchAlgorithmException -> 0x0055, NoSuchAlgorithmException -> 0x0055, NoSuchAlgorithmException -> 0x0055 }
        r2 = r8.blockSize;	 Catch:{ NoSuchAlgorithmException -> 0x0055, NoSuchAlgorithmException -> 0x0055, NoSuchAlgorithmException -> 0x0055, NoSuchAlgorithmException -> 0x0055 }
        r2 = (long) r2;	 Catch:{ NoSuchAlgorithmException -> 0x0055, NoSuchAlgorithmException -> 0x0055, NoSuchAlgorithmException -> 0x0055, NoSuchAlgorithmException -> 0x0055 }
        r2 = r13 % r2;	 Catch:{ NoSuchAlgorithmException -> 0x0055, NoSuchAlgorithmException -> 0x0055, NoSuchAlgorithmException -> 0x0055, NoSuchAlgorithmException -> 0x0055 }
        r2 = (int) r2;	 Catch:{ NoSuchAlgorithmException -> 0x0055, NoSuchAlgorithmException -> 0x0055, NoSuchAlgorithmException -> 0x0055, NoSuchAlgorithmException -> 0x0055 }
        r3 = r8.cipher;	 Catch:{ NoSuchAlgorithmException -> 0x0055, NoSuchAlgorithmException -> 0x0055, NoSuchAlgorithmException -> 0x0055, NoSuchAlgorithmException -> 0x0055 }
        r4 = new javax.crypto.spec.SecretKeySpec;	 Catch:{ NoSuchAlgorithmException -> 0x0055, NoSuchAlgorithmException -> 0x0055, NoSuchAlgorithmException -> 0x0055, NoSuchAlgorithmException -> 0x0055 }
        r5 = r8.cipher;	 Catch:{ NoSuchAlgorithmException -> 0x0055, NoSuchAlgorithmException -> 0x0055, NoSuchAlgorithmException -> 0x0055, NoSuchAlgorithmException -> 0x0055 }
        r5 = r5.getAlgorithm();	 Catch:{ NoSuchAlgorithmException -> 0x0055, NoSuchAlgorithmException -> 0x0055, NoSuchAlgorithmException -> 0x0055, NoSuchAlgorithmException -> 0x0055 }
        r6 = "/";	 Catch:{ NoSuchAlgorithmException -> 0x0055, NoSuchAlgorithmException -> 0x0055, NoSuchAlgorithmException -> 0x0055, NoSuchAlgorithmException -> 0x0055 }
        r5 = r5.split(r6);	 Catch:{ NoSuchAlgorithmException -> 0x0055, NoSuchAlgorithmException -> 0x0055, NoSuchAlgorithmException -> 0x0055, NoSuchAlgorithmException -> 0x0055 }
        r6 = 0;	 Catch:{ NoSuchAlgorithmException -> 0x0055, NoSuchAlgorithmException -> 0x0055, NoSuchAlgorithmException -> 0x0055, NoSuchAlgorithmException -> 0x0055 }
        r5 = r5[r6];	 Catch:{ NoSuchAlgorithmException -> 0x0055, NoSuchAlgorithmException -> 0x0055, NoSuchAlgorithmException -> 0x0055, NoSuchAlgorithmException -> 0x0055 }
        r4.<init>(r10, r5);	 Catch:{ NoSuchAlgorithmException -> 0x0055, NoSuchAlgorithmException -> 0x0055, NoSuchAlgorithmException -> 0x0055, NoSuchAlgorithmException -> 0x0055 }
        r5 = new javax.crypto.spec.IvParameterSpec;	 Catch:{ NoSuchAlgorithmException -> 0x0055, NoSuchAlgorithmException -> 0x0055, NoSuchAlgorithmException -> 0x0055, NoSuchAlgorithmException -> 0x0055 }
        r7 = r8.getInitializationVector(r11, r0);	 Catch:{ NoSuchAlgorithmException -> 0x0055, NoSuchAlgorithmException -> 0x0055, NoSuchAlgorithmException -> 0x0055, NoSuchAlgorithmException -> 0x0055 }
        r5.<init>(r7);	 Catch:{ NoSuchAlgorithmException -> 0x0055, NoSuchAlgorithmException -> 0x0055, NoSuchAlgorithmException -> 0x0055, NoSuchAlgorithmException -> 0x0055 }
        r3.init(r9, r4, r5);	 Catch:{ NoSuchAlgorithmException -> 0x0055, NoSuchAlgorithmException -> 0x0055, NoSuchAlgorithmException -> 0x0055, NoSuchAlgorithmException -> 0x0055 }
        if (r2 == 0) goto L_0x0053;	 Catch:{ NoSuchAlgorithmException -> 0x0055, NoSuchAlgorithmException -> 0x0055, NoSuchAlgorithmException -> 0x0055, NoSuchAlgorithmException -> 0x0055 }
    L_0x004e:
        r3 = new byte[r2];	 Catch:{ NoSuchAlgorithmException -> 0x0055, NoSuchAlgorithmException -> 0x0055, NoSuchAlgorithmException -> 0x0055, NoSuchAlgorithmException -> 0x0055 }
        r8.updateInPlace(r3, r6, r2);	 Catch:{ NoSuchAlgorithmException -> 0x0055, NoSuchAlgorithmException -> 0x0055, NoSuchAlgorithmException -> 0x0055, NoSuchAlgorithmException -> 0x0055 }
        return;
    L_0x0055:
        r0 = move-exception;
        r1 = new java.lang.RuntimeException;
        r1.<init>(r0);
        throw r1;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.telegram.messenger.exoplayer2.upstream.crypto.AesFlushingCipher.<init>(int, byte[], long, long):void");
    }

    public void updateInPlace(byte[] data, int offset, int length) {
        update(data, offset, length, data, offset);
    }

    public void update(byte[] in, int inOffset, int length, byte[] out, int outOffset) {
        AesFlushingCipher aesFlushingCipher = this;
        int inOffset2 = inOffset;
        int length2 = length;
        int outOffset2 = outOffset;
        do {
            boolean z = true;
            if (aesFlushingCipher.pendingXorBytes > 0) {
                out[outOffset2] = (byte) (in[inOffset2] ^ aesFlushingCipher.flushedBlock[aesFlushingCipher.blockSize - aesFlushingCipher.pendingXorBytes]);
                outOffset2++;
                inOffset2++;
                aesFlushingCipher.pendingXorBytes--;
                length2--;
            } else {
                int written = nonFlushingUpdate(in, inOffset2, length2, out, outOffset2);
                if (length2 != written) {
                    int bytesToFlush = length2 - written;
                    int i = 0;
                    Assertions.checkState(bytesToFlush < aesFlushingCipher.blockSize);
                    outOffset2 += written;
                    aesFlushingCipher.pendingXorBytes = aesFlushingCipher.blockSize - bytesToFlush;
                    if (nonFlushingUpdate(aesFlushingCipher.zerosBlock, 0, aesFlushingCipher.pendingXorBytes, aesFlushingCipher.flushedBlock, 0) != aesFlushingCipher.blockSize) {
                        z = false;
                    }
                    Assertions.checkState(z);
                    while (true) {
                        int i2 = i;
                        if (i2 < bytesToFlush) {
                            int outOffset3 = outOffset2 + 1;
                            out[outOffset2] = aesFlushingCipher.flushedBlock[i2];
                            i = i2 + 1;
                            outOffset2 = outOffset3;
                        } else {
                            return;
                        }
                    }
                }
                return;
            }
        } while (length2 != 0);
    }

    private int nonFlushingUpdate(byte[] in, int inOffset, int length, byte[] out, int outOffset) {
        try {
            return this.cipher.update(in, inOffset, length, out, outOffset);
        } catch (ShortBufferException e) {
            throw new RuntimeException(e);
        }
    }

    private byte[] getInitializationVector(long nonce, long counter) {
        return ByteBuffer.allocate(16).putLong(nonce).putLong(counter).array();
    }
}
