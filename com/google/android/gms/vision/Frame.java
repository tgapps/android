package com.google.android.gms.vision;

import android.graphics.Bitmap;
import android.graphics.Color;
import java.nio.ByteBuffer;

public class Frame {
    private Bitmap mBitmap;
    private Metadata zzam;
    private ByteBuffer zzan;

    public static class Builder {
        private Frame zzao = new Frame();

        public Frame build() {
            if (this.zzao.zzan != null || this.zzao.mBitmap != null) {
                return this.zzao;
            }
            throw new IllegalStateException("Missing image data.  Call either setBitmap or setImageData to specify the image");
        }

        public Builder setBitmap(Bitmap bitmap) {
            int width = bitmap.getWidth();
            int height = bitmap.getHeight();
            this.zzao.mBitmap = bitmap;
            Metadata metadata = this.zzao.getMetadata();
            metadata.zzap = width;
            metadata.zzaq = height;
            return this;
        }

        public Builder setRotation(int i) {
            this.zzao.getMetadata().zzg = i;
            return this;
        }
    }

    public static class Metadata {
        private int format = -1;
        private int mId;
        private int zzap;
        private int zzaq;
        private long zzar;
        private int zzg;

        public int getHeight() {
            return this.zzaq;
        }

        public int getId() {
            return this.mId;
        }

        public int getRotation() {
            return this.zzg;
        }

        public long getTimestampMillis() {
            return this.zzar;
        }

        public int getWidth() {
            return this.zzap;
        }
    }

    private Frame() {
        this.zzam = new Metadata();
        this.zzan = null;
        this.mBitmap = null;
    }

    public ByteBuffer getGrayscaleImageData() {
        int i = 0;
        if (this.mBitmap == null) {
            return this.zzan;
        }
        int width = this.mBitmap.getWidth();
        int height = this.mBitmap.getHeight();
        int[] iArr = new int[(width * height)];
        this.mBitmap.getPixels(iArr, 0, width, 0, 0, width, height);
        byte[] bArr = new byte[(width * height)];
        while (i < iArr.length) {
            bArr[i] = (byte) ((int) (((((float) Color.red(iArr[i])) * 0.299f) + (((float) Color.green(iArr[i])) * 0.587f)) + (((float) Color.blue(iArr[i])) * 0.114f)));
            i++;
        }
        return ByteBuffer.wrap(bArr);
    }

    public Metadata getMetadata() {
        return this.zzam;
    }
}
