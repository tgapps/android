package com.google.android.gms.vision;

import android.graphics.Bitmap;
import android.graphics.Color;
import java.nio.ByteBuffer;

public class Frame {
    private Metadata zzap;
    private ByteBuffer zzaq;
    private Bitmap zzar;

    public static class Builder {
        private Frame zzas = new Frame();

        public Frame build() {
            if (this.zzas.zzaq != null || this.zzas.zzar != null) {
                return this.zzas;
            }
            throw new IllegalStateException("Missing image data.  Call either setBitmap or setImageData to specify the image");
        }

        public Builder setBitmap(Bitmap bitmap) {
            int width = bitmap.getWidth();
            int height = bitmap.getHeight();
            this.zzas.zzar = bitmap;
            Metadata metadata = this.zzas.getMetadata();
            metadata.width = width;
            metadata.height = height;
            return this;
        }

        public Builder setRotation(int i) {
            this.zzas.getMetadata().rotation = i;
            return this;
        }
    }

    public static class Metadata {
        private int format = -1;
        private int height;
        private int id;
        private int rotation;
        private int width;
        private long zzat;

        public int getHeight() {
            return this.height;
        }

        public int getId() {
            return this.id;
        }

        public int getRotation() {
            return this.rotation;
        }

        public long getTimestampMillis() {
            return this.zzat;
        }

        public int getWidth() {
            return this.width;
        }
    }

    private Frame() {
        this.zzap = new Metadata();
        this.zzaq = null;
        this.zzar = null;
    }

    public Bitmap getBitmap() {
        return this.zzar;
    }

    public ByteBuffer getGrayscaleImageData() {
        int i = 0;
        if (this.zzar == null) {
            return this.zzaq;
        }
        int width = this.zzar.getWidth();
        int height = this.zzar.getHeight();
        int[] iArr = new int[(width * height)];
        this.zzar.getPixels(iArr, 0, width, 0, 0, width, height);
        byte[] bArr = new byte[(width * height)];
        while (i < iArr.length) {
            bArr[i] = (byte) ((int) (((((float) Color.red(iArr[i])) * 0.299f) + (((float) Color.green(iArr[i])) * 0.587f)) + (((float) Color.blue(iArr[i])) * 0.114f)));
            i++;
        }
        return ByteBuffer.wrap(bArr);
    }

    public Metadata getMetadata() {
        return this.zzap;
    }
}
