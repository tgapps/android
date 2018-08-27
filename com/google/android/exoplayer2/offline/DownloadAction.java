package com.google.android.exoplayer2.offline;

import android.net.Uri;
import com.google.android.exoplayer2.util.Assertions;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public abstract class DownloadAction {
    private static Deserializer[] defaultDeserializers;
    public final byte[] data;
    public final boolean isRemoveAction;
    public final String type;
    public final Uri uri;
    public final int version;

    public static abstract class Deserializer {
        public final String type;
        public final int version;

        public abstract DownloadAction readFromStream(int i, DataInputStream dataInputStream) throws IOException;

        public Deserializer(String type, int version) {
            this.type = type;
            this.version = version;
        }
    }

    public abstract Downloader createDownloader(DownloaderConstructorHelper downloaderConstructorHelper);

    protected abstract void writeToStream(DataOutputStream dataOutputStream) throws IOException;

    public static synchronized Deserializer[] getDefaultDeserializers() {
        Deserializer[] deserializerArr;
        synchronized (DownloadAction.class) {
            if (defaultDeserializers != null) {
                deserializerArr = defaultDeserializers;
            } else {
                int count;
                Deserializer[] deserializers = new Deserializer[4];
                int count2 = 0 + 1;
                deserializers[0] = ProgressiveDownloadAction.DESERIALIZER;
                try {
                    count = count2 + 1;
                    try {
                        deserializers[count2] = getDeserializer(Class.forName("com.google.android.exoplayer2.source.dash.offline.DashDownloadAction"));
                        count2 = count;
                    } catch (Exception e) {
                        count2 = count;
                        count = count2 + 1;
                        deserializers[count2] = getDeserializer(Class.forName("com.google.android.exoplayer2.source.hls.offline.HlsDownloadAction"));
                        count2 = count;
                        count = count2 + 1;
                        deserializers[count2] = getDeserializer(Class.forName("com.google.android.exoplayer2.source.smoothstreaming.offline.SsDownloadAction"));
                        defaultDeserializers = (Deserializer[]) Arrays.copyOf((Object[]) Assertions.checkNotNull(deserializers), count);
                        deserializerArr = defaultDeserializers;
                        return deserializerArr;
                    }
                } catch (Exception e2) {
                    count = count2;
                    count2 = count;
                    count = count2 + 1;
                    deserializers[count2] = getDeserializer(Class.forName("com.google.android.exoplayer2.source.hls.offline.HlsDownloadAction"));
                    count2 = count;
                    count = count2 + 1;
                    deserializers[count2] = getDeserializer(Class.forName("com.google.android.exoplayer2.source.smoothstreaming.offline.SsDownloadAction"));
                    defaultDeserializers = (Deserializer[]) Arrays.copyOf((Object[]) Assertions.checkNotNull(deserializers), count);
                    deserializerArr = defaultDeserializers;
                    return deserializerArr;
                }
                try {
                    count = count2 + 1;
                    try {
                        deserializers[count2] = getDeserializer(Class.forName("com.google.android.exoplayer2.source.hls.offline.HlsDownloadAction"));
                        count2 = count;
                    } catch (Exception e3) {
                        count2 = count;
                        count = count2 + 1;
                        deserializers[count2] = getDeserializer(Class.forName("com.google.android.exoplayer2.source.smoothstreaming.offline.SsDownloadAction"));
                        defaultDeserializers = (Deserializer[]) Arrays.copyOf((Object[]) Assertions.checkNotNull(deserializers), count);
                        deserializerArr = defaultDeserializers;
                        return deserializerArr;
                    }
                } catch (Exception e4) {
                    count = count2;
                    count2 = count;
                    count = count2 + 1;
                    deserializers[count2] = getDeserializer(Class.forName("com.google.android.exoplayer2.source.smoothstreaming.offline.SsDownloadAction"));
                    defaultDeserializers = (Deserializer[]) Arrays.copyOf((Object[]) Assertions.checkNotNull(deserializers), count);
                    deserializerArr = defaultDeserializers;
                    return deserializerArr;
                }
                try {
                    count = count2 + 1;
                    try {
                        deserializers[count2] = getDeserializer(Class.forName("com.google.android.exoplayer2.source.smoothstreaming.offline.SsDownloadAction"));
                    } catch (Exception e5) {
                    }
                } catch (Exception e6) {
                    count = count2;
                }
                defaultDeserializers = (Deserializer[]) Arrays.copyOf((Object[]) Assertions.checkNotNull(deserializers), count);
                deserializerArr = defaultDeserializers;
            }
        }
        return deserializerArr;
    }

    public static DownloadAction deserializeFromStream(Deserializer[] deserializers, InputStream input) throws IOException {
        DataInputStream dataInputStream = new DataInputStream(input);
        String type = dataInputStream.readUTF();
        int version = dataInputStream.readInt();
        for (Deserializer deserializer : deserializers) {
            if (type.equals(deserializer.type) && deserializer.version >= version) {
                return deserializer.readFromStream(version, dataInputStream);
            }
        }
        throw new DownloadException("No deserializer found for:" + type + ", " + version);
    }

    public static void serializeToStream(DownloadAction action, OutputStream output) throws IOException {
        DataOutputStream dataOutputStream = new DataOutputStream(output);
        dataOutputStream.writeUTF(action.type);
        dataOutputStream.writeInt(action.version);
        action.writeToStream(dataOutputStream);
        dataOutputStream.flush();
    }

    protected DownloadAction(String type, int version, Uri uri, boolean isRemoveAction, byte[] data) {
        this.type = type;
        this.version = version;
        this.uri = uri;
        this.isRemoveAction = isRemoveAction;
        if (data == null) {
            data = new byte[0];
        }
        this.data = data;
    }

    public final byte[] toByteArray() {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        try {
            serializeToStream(this, output);
            return output.toByteArray();
        } catch (IOException e) {
            throw new IllegalStateException();
        }
    }

    public boolean isSameMedia(DownloadAction other) {
        return this.uri.equals(other.uri);
    }

    public List<StreamKey> getKeys() {
        return Collections.emptyList();
    }

    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        DownloadAction that = (DownloadAction) o;
        if (this.type.equals(that.type) && this.version == that.version && this.uri.equals(that.uri) && this.isRemoveAction == that.isRemoveAction && Arrays.equals(this.data, that.data)) {
            return true;
        }
        return false;
    }

    public int hashCode() {
        return (((this.uri.hashCode() * 31) + (this.isRemoveAction ? 1 : 0)) * 31) + Arrays.hashCode(this.data);
    }

    private static Deserializer getDeserializer(Class<?> clazz) throws NoSuchFieldException, IllegalAccessException {
        return (Deserializer) Assertions.checkNotNull(clazz.getDeclaredField("DESERIALIZER").get(null));
    }
}
