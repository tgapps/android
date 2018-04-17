package com.google.android.gms.vision.face;

import android.content.Context;
import android.util.Log;
import android.util.SparseArray;
import com.google.android.gms.internal.vision.zzk;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.face.internal.client.zza;
import com.google.android.gms.vision.zzc;
import java.nio.ByteBuffer;
import java.util.HashSet;
import java.util.Set;
import javax.annotation.concurrent.GuardedBy;

public final class FaceDetector extends Detector<Face> {
    @GuardedBy("mLock")
    private boolean mIsActive;
    private final Object mLock;
    private final zzc zzbm;
    @GuardedBy("mLock")
    private final zza zzbn;

    public static class Builder {
        private final Context mContext;
        private int zzbo = 0;
        private boolean zzbp = false;
        private int zzbq = 0;
        private boolean zzbr = true;
        private int zzbs = 0;
        private float zzbt = -1.0f;

        public Builder(Context context) {
            this.mContext = context;
        }

        public FaceDetector build() {
            com.google.android.gms.vision.face.internal.client.zzc com_google_android_gms_vision_face_internal_client_zzc = new com.google.android.gms.vision.face.internal.client.zzc();
            com_google_android_gms_vision_face_internal_client_zzc.mode = this.zzbs;
            com_google_android_gms_vision_face_internal_client_zzc.zzcd = this.zzbo;
            com_google_android_gms_vision_face_internal_client_zzc.zzce = this.zzbq;
            com_google_android_gms_vision_face_internal_client_zzc.zzcf = this.zzbp;
            com_google_android_gms_vision_face_internal_client_zzc.zzcg = this.zzbr;
            com_google_android_gms_vision_face_internal_client_zzc.zzch = this.zzbt;
            return new FaceDetector(new zza(this.mContext, com_google_android_gms_vision_face_internal_client_zzc));
        }

        public Builder setLandmarkType(int i) {
            if (i == 0 || i == 1) {
                this.zzbo = i;
                return this;
            }
            StringBuilder stringBuilder = new StringBuilder(34);
            stringBuilder.append("Invalid landmark type: ");
            stringBuilder.append(i);
            throw new IllegalArgumentException(stringBuilder.toString());
        }

        public Builder setMode(int i) {
            switch (i) {
                case 0:
                case 1:
                    this.zzbs = i;
                    return this;
                default:
                    StringBuilder stringBuilder = new StringBuilder(25);
                    stringBuilder.append("Invalid mode: ");
                    stringBuilder.append(i);
                    throw new IllegalArgumentException(stringBuilder.toString());
            }
        }

        public Builder setTrackingEnabled(boolean z) {
            this.zzbr = z;
            return this;
        }
    }

    private FaceDetector() {
        this.zzbm = new zzc();
        this.mLock = new Object();
        this.mIsActive = true;
        throw new IllegalStateException("Default constructor called");
    }

    private FaceDetector(zza com_google_android_gms_vision_face_internal_client_zza) {
        this.zzbm = new zzc();
        this.mLock = new Object();
        this.mIsActive = true;
        this.zzbn = com_google_android_gms_vision_face_internal_client_zza;
    }

    public final SparseArray<Face> detect(Frame frame) {
        if (frame == null) {
            throw new IllegalArgumentException("No frame supplied.");
        }
        Face[] zzb;
        ByteBuffer grayscaleImageData = frame.getGrayscaleImageData();
        synchronized (this.mLock) {
            if (this.mIsActive) {
                zzb = this.zzbn.zzb(grayscaleImageData, zzk.zzc(frame));
            } else {
                throw new RuntimeException("Cannot use detector after release()");
            }
        }
        Set hashSet = new HashSet();
        int i = 0;
        SparseArray<Face> sparseArray = new SparseArray(zzb.length);
        int length = zzb.length;
        int i2 = 0;
        while (i < length) {
            Face face = zzb[i];
            int id = face.getId();
            i2 = Math.max(i2, id);
            if (hashSet.contains(Integer.valueOf(id))) {
                id = i2 + 1;
                i2 = id;
            }
            hashSet.add(Integer.valueOf(id));
            sparseArray.append(this.zzbm.zzb(id), face);
            i++;
        }
        return sparseArray;
    }

    protected final void finalize() throws Throwable {
        try {
            synchronized (this.mLock) {
                if (this.mIsActive) {
                    Log.w("FaceDetector", "FaceDetector was not released with FaceDetector.release()");
                    release();
                }
            }
            super.finalize();
        } catch (Throwable th) {
            super.finalize();
        }
    }

    public final boolean isOperational() {
        return this.zzbn.isOperational();
    }

    public final void release() {
        super.release();
        synchronized (this.mLock) {
            if (this.mIsActive) {
                this.zzbn.zzg();
                this.mIsActive = false;
                return;
            }
        }
    }
}
