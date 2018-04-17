package org.telegram.messenger.exoplayer2.source.hls;

import android.net.Uri;
import java.io.IOException;
import java.security.GeneralSecurityException;
import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import org.telegram.messenger.exoplayer2.upstream.DataSource;
import org.telegram.messenger.exoplayer2.upstream.DataSourceInputStream;
import org.telegram.messenger.exoplayer2.upstream.DataSpec;
import org.telegram.messenger.exoplayer2.util.Assertions;

final class Aes128DataSource implements DataSource {
    private CipherInputStream cipherInputStream;
    private final byte[] encryptionIv;
    private final byte[] encryptionKey;
    private final DataSource upstream;

    public Aes128DataSource(DataSource upstream, byte[] encryptionKey, byte[] encryptionIv) {
        this.upstream = upstream;
        this.encryptionKey = encryptionKey;
        this.encryptionIv = encryptionIv;
    }

    public long open(DataSpec dataSpec) throws IOException {
        try {
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding");
            try {
                cipher.init(2, new SecretKeySpec(this.encryptionKey, "AES"), new IvParameterSpec(this.encryptionIv));
                this.cipherInputStream = new CipherInputStream(new DataSourceInputStream(this.upstream, dataSpec), cipher);
                return -1;
            } catch (GeneralSecurityException e) {
                throw new RuntimeException(e);
            }
        } catch (GeneralSecurityException e2) {
            throw new RuntimeException(e2);
        }
    }

    public void close() throws IOException {
        this.cipherInputStream = null;
        this.upstream.close();
    }

    public int read(byte[] buffer, int offset, int readLength) throws IOException {
        Assertions.checkState(this.cipherInputStream != null);
        int bytesRead = this.cipherInputStream.read(buffer, offset, readLength);
        if (bytesRead < 0) {
            return -1;
        }
        return bytesRead;
    }

    public Uri getUri() {
        return this.upstream.getUri();
    }
}
