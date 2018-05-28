package com.google.android.gms.vision.face;

import android.content.Context;
import android.util.Log;
import android.util.SparseArray;
import com.google.android.gms.internal.vision.zzm;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.face.internal.client.zza;
import com.google.android.gms.vision.zzc;
import java.nio.ByteBuffer;
import java.util.HashSet;
import java.util.Set;
import javax.annotation.concurrent.GuardedBy;

public final class FaceDetector extends Detector<Face> {
    private final Object lock;
    private final zzc zzbv;
    @GuardedBy("lock")
    private final zza zzbw;
    @GuardedBy("lock")
    private boolean zzbx;

    public static class Builder {
        private int mode = 0;
        private int zzby = 0;
        private boolean zzbz = false;
        private int zzca = 0;
        private boolean zzcb = true;
        private float zzcc = -1.0f;
        private final Context zze;

        public Builder(Context context) {
            this.zze = context;
        }

        public FaceDetector build() {
            com.google.android.gms.vision.face.internal.client.zzc com_google_android_gms_vision_face_internal_client_zzc = new com.google.android.gms.vision.face.internal.client.zzc();
            com_google_android_gms_vision_face_internal_client_zzc.mode = this.mode;
            com_google_android_gms_vision_face_internal_client_zzc.zzby = this.zzby;
            com_google_android_gms_vision_face_internal_client_zzc.zzca = this.zzca;
            com_google_android_gms_vision_face_internal_client_zzc.zzbz = this.zzbz;
            com_google_android_gms_vision_face_internal_client_zzc.zzcb = this.zzcb;
            com_google_android_gms_vision_face_internal_client_zzc.zzcc = this.zzcc;
            return new FaceDetector(new zza(this.zze, com_google_android_gms_vision_face_internal_client_zzc));
        }

        public Builder setLandmarkType(int i) {
            if (i == 0 || i == 1) {
                this.zzby = i;
                return this;
            }
            throw new IllegalArgumentException("Invalid landmark type: " + i);
        }

        public Builder setMode(int i) {
            switch (i) {
                case 0:
                case 1:
                    this.mode = i;
                    return this;
                default:
                    throw new IllegalArgumentException("Invalid mode: " + i);
            }
        }

        public Builder setTrackingEnabled(boolean z) {
            this.zzcb = z;
            return this;
        }
    }

    private FaceDetector() {
        this.zzbv = new zzc();
        this.lock = new Object();
        this.zzbx = true;
        throw new IllegalStateException("Default constructor called");
    }

    private FaceDetector(zza com_google_android_gms_vision_face_internal_client_zza) {
        this.zzbv = new zzc();
        this.lock = new Object();
        this.zzbx = true;
        this.zzbw = com_google_android_gms_vision_face_internal_client_zza;
    }

    public final SparseArray<Face> detect(Frame frame) {
        if (frame == null) {
            throw new IllegalArgumentException("No frame supplied.");
        }
        Face[] zzb;
        ByteBuffer grayscaleImageData = frame.getGrayscaleImageData();
        synchronized (this.lock) {
            if (this.zzbx) {
                zzb = this.zzbw.zzb(grayscaleImageData, zzm.zzc(frame));
            } else {
                throw new RuntimeException("Cannot use detector after release()");
            }
        }
        Set hashSet = new HashSet();
        SparseArray<Face> sparseArray = new SparseArray(zzb.length);
        int i = 0;
        for (Face face : zzb) {
            int id = face.getId();
            int max = Math.max(i, id);
            if (hashSet.contains(Integer.valueOf(id))) {
                max++;
                id = max;
                i = max;
            } else {
                i = max;
            }
            hashSet.add(Integer.valueOf(id));
            sparseArray.append(this.zzbv.zzb(id), face);
        }
        return sparseArray;
    }

    protected final void finalize() throws Throwable {
        try {
            synchronized (this.lock) {
                if (this.zzbx) {
                    Log.w("FaceDetector", "FaceDetector was not released with FaceDetector.release()");
                    release();
                }
            }
        } finally {
            super.finalize();
        }
    }

    public final boolean isOperational() {
        return this.zzbw.isOperational();
    }

    public final void release() {
        super.release();
        synchronized (this.lock) {
            if (this.zzbx) {
                this.zzbw.zzo();
                this.zzbx = false;
                return;
            }
        }
    }
}
