package org.telegram.messenger.camera;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Matrix;
import android.graphics.SurfaceTexture;
import android.graphics.drawable.BitmapDrawable;
import android.hardware.Camera;
import android.hardware.Camera.CameraInfo;
import android.hardware.Camera.Parameters;
import android.hardware.Camera.PictureCallback;
import android.hardware.Camera.Size;
import android.media.MediaRecorder;
import android.media.MediaRecorder.OnInfoListener;
import android.os.Build;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.Bitmaps;
import org.telegram.messenger.BuildVars;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.ImageLoader;
import org.telegram.messenger.NotificationCenter;
import org.telegram.messenger.Utilities;

public class CameraController implements OnInfoListener {
    private static final int CORE_POOL_SIZE = 1;
    private static volatile CameraController Instance = null;
    private static final int KEEP_ALIVE_SECONDS = 60;
    private static final int MAX_POOL_SIZE = 1;
    protected ArrayList<String> availableFlashModes = new ArrayList();
    protected ArrayList<CameraInfo> cameraInfos = null;
    private boolean cameraInitied;
    private boolean loadingCameras;
    private VideoTakeCallback onVideoTakeCallback;
    private String recordedFile;
    private MediaRecorder recorder;
    private ThreadPoolExecutor threadPool = new ThreadPoolExecutor(1, 1, 60, TimeUnit.SECONDS, new LinkedBlockingQueue());

    class AnonymousClass10 implements Runnable {
        final /* synthetic */ Bitmap val$bitmap;
        final /* synthetic */ File val$cacheFile;
        final /* synthetic */ long val$durationFinal;

        AnonymousClass10(File file, Bitmap bitmap, long j) {
            this.val$cacheFile = file;
            this.val$bitmap = bitmap;
            this.val$durationFinal = j;
        }

        public void run() {
            if (CameraController.this.onVideoTakeCallback != null) {
                String path = this.val$cacheFile.getAbsolutePath();
                if (this.val$bitmap != null) {
                    ImageLoader.getInstance().putImageToCache(new BitmapDrawable(this.val$bitmap), Utilities.MD5(path));
                }
                CameraController.this.onVideoTakeCallback.onFinishVideoRecording(path, this.val$durationFinal);
                CameraController.this.onVideoTakeCallback = null;
            }
        }
    }

    static class CompareSizesByArea implements Comparator<Size> {
        CompareSizesByArea() {
        }

        public int compare(Size lhs, Size rhs) {
            return Long.signum((((long) lhs.getWidth()) * ((long) lhs.getHeight())) - (((long) rhs.getWidth()) * ((long) rhs.getHeight())));
        }
    }

    public interface VideoTakeCallback {
        void onFinishVideoRecording(String str, long j);
    }

    private void finishRecordingVideo() {
        /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.DecodeException: Load method exception in method: org.telegram.messenger.camera.CameraController.finishRecordingVideo():void
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
        r0 = 0;
        r1 = 0;
        r3 = new android.media.MediaMetadataRetriever;	 Catch:{ Exception -> 0x002f }
        r3.<init>();	 Catch:{ Exception -> 0x002f }
        r0 = r3;	 Catch:{ Exception -> 0x002f }
        r3 = r13.recordedFile;	 Catch:{ Exception -> 0x002f }
        r0.setDataSource(r3);	 Catch:{ Exception -> 0x002f }
        r3 = 9;	 Catch:{ Exception -> 0x002f }
        r3 = r0.extractMetadata(r3);	 Catch:{ Exception -> 0x002f }
        if (r3 == 0) goto L_0x0025;	 Catch:{ Exception -> 0x002f }
    L_0x0016:
        r4 = java.lang.Long.parseLong(r3);	 Catch:{ Exception -> 0x002f }
        r4 = (float) r4;	 Catch:{ Exception -> 0x002f }
        r5 = 1148846080; // 0x447a0000 float:1000.0 double:5.676053805E-315;	 Catch:{ Exception -> 0x002f }
        r4 = r4 / r5;	 Catch:{ Exception -> 0x002f }
        r4 = (double) r4;	 Catch:{ Exception -> 0x002f }
        r4 = java.lang.Math.ceil(r4);	 Catch:{ Exception -> 0x002f }
        r4 = (int) r4;
        r1 = (long) r4;
    L_0x0025:
        if (r0 == 0) goto L_0x003e;
    L_0x0027:
        r0.release();	 Catch:{ Exception -> 0x002b }
        goto L_0x003e;
    L_0x002b:
        r3 = move-exception;
        goto L_0x003a;
    L_0x002d:
        r3 = move-exception;
        goto L_0x008e;
    L_0x002f:
        r3 = move-exception;
        org.telegram.messenger.FileLog.e(r3);	 Catch:{ all -> 0x002d }
        if (r0 == 0) goto L_0x003e;
        r0.release();	 Catch:{ Exception -> 0x0039 }
        goto L_0x003e;
    L_0x0039:
        r3 = move-exception;
        org.telegram.messenger.FileLog.e(r3);
        goto L_0x003f;
        r7 = r1;
        r1 = r13.recordedFile;
        r2 = 1;
        r9 = android.media.ThumbnailUtils.createVideoThumbnail(r1, r2);
        r1 = new java.lang.StringBuilder;
        r1.<init>();
        r2 = "-2147483648_";
        r1.append(r2);
        r2 = org.telegram.messenger.SharedConfig.getLastLocalId();
        r1.append(r2);
        r2 = ".jpg";
        r1.append(r2);
        r10 = r1.toString();
        r1 = new java.io.File;
        r2 = 4;
        r2 = org.telegram.messenger.FileLoader.getDirectory(r2);
        r1.<init>(r2, r10);
        r11 = r1;
        r1 = new java.io.FileOutputStream;	 Catch:{ Throwable -> 0x0079 }
        r1.<init>(r11);	 Catch:{ Throwable -> 0x0079 }
        r2 = android.graphics.Bitmap.CompressFormat.JPEG;	 Catch:{ Throwable -> 0x0079 }
        r3 = 55;	 Catch:{ Throwable -> 0x0079 }
        r9.compress(r2, r3, r1);	 Catch:{ Throwable -> 0x0079 }
        goto L_0x007d;
    L_0x0079:
        r1 = move-exception;
        org.telegram.messenger.FileLog.e(r1);
        org.telegram.messenger.SharedConfig.saveConfig();
        r5 = r7;
        r12 = new org.telegram.messenger.camera.CameraController$10;
        r1 = r12;
        r2 = r13;
        r3 = r11;
        r4 = r9;
        r1.<init>(r3, r4, r5);
        org.telegram.messenger.AndroidUtilities.runOnUIThread(r12);
        return;
        if (r0 == 0) goto L_0x009a;
        r0.release();	 Catch:{ Exception -> 0x0095 }
        goto L_0x009a;
    L_0x0095:
        r4 = move-exception;
        org.telegram.messenger.FileLog.e(r4);
        throw r3;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.telegram.messenger.camera.CameraController.finishRecordingVideo():void");
    }

    public static CameraController getInstance() {
        CameraController localInstance = Instance;
        if (localInstance == null) {
            synchronized (CameraController.class) {
                localInstance = Instance;
                if (localInstance == null) {
                    CameraController cameraController = new CameraController();
                    localInstance = cameraController;
                    Instance = cameraController;
                }
            }
        }
        return localInstance;
    }

    public void initCamera() {
        if (!this.loadingCameras) {
            if (!this.cameraInitied) {
                this.loadingCameras = true;
                this.threadPool.execute(new Runnable() {
                    public void run() {
                        try {
                            if (CameraController.this.cameraInfos == null) {
                                int count = Camera.getNumberOfCameras();
                                ArrayList<CameraInfo> result = new ArrayList();
                                CameraInfo info = new CameraInfo();
                                for (int cameraId = 0; cameraId < count; cameraId++) {
                                    int i;
                                    int i2;
                                    Camera.getCameraInfo(cameraId, info);
                                    CameraInfo cameraInfo = new CameraInfo(cameraId, info);
                                    Camera camera = Camera.open(cameraInfo.getCameraId());
                                    Parameters params = camera.getParameters();
                                    List<Size> list = params.getSupportedPreviewSizes();
                                    int a = 0;
                                    while (true) {
                                        i = 720;
                                        i2 = 1280;
                                        if (a >= list.size()) {
                                            break;
                                        }
                                        Size size = (Size) list.get(a);
                                        if (size.width != 1280 || size.height == 720) {
                                            if (size.height < 2160 && size.width < 2160) {
                                                cameraInfo.previewSizes.add(new Size(size.width, size.height));
                                                if (BuildVars.LOGS_ENABLED) {
                                                    StringBuilder stringBuilder = new StringBuilder();
                                                    stringBuilder.append("preview size = ");
                                                    stringBuilder.append(size.width);
                                                    stringBuilder.append(" ");
                                                    stringBuilder.append(size.height);
                                                    FileLog.d(stringBuilder.toString());
                                                }
                                            }
                                        }
                                        a++;
                                    }
                                    List<Size> list2 = params.getSupportedPictureSizes();
                                    int a2 = 0;
                                    while (a2 < list2.size()) {
                                        Size size2 = (Size) list2.get(a2);
                                        if (size2.width != i2 || size2.height == r13) {
                                            if (!"samsung".equals(Build.MANUFACTURER) || !"jflteuc".equals(Build.PRODUCT) || size2.width < 2048) {
                                                cameraInfo.pictureSizes.add(new Size(size2.width, size2.height));
                                                if (BuildVars.LOGS_ENABLED) {
                                                    StringBuilder stringBuilder2 = new StringBuilder();
                                                    stringBuilder2.append("picture size = ");
                                                    stringBuilder2.append(size2.width);
                                                    stringBuilder2.append(" ");
                                                    stringBuilder2.append(size2.height);
                                                    FileLog.d(stringBuilder2.toString());
                                                }
                                            }
                                        }
                                        a2++;
                                        i = 720;
                                        i2 = 1280;
                                    }
                                    camera.release();
                                    result.add(cameraInfo);
                                    Comparator<Size> comparator = new Comparator<Size>() {
                                        public int compare(Size o1, Size o2) {
                                            if (o1.mWidth < o2.mWidth) {
                                                return 1;
                                            }
                                            if (o1.mWidth > o2.mWidth) {
                                                return -1;
                                            }
                                            if (o1.mHeight < o2.mHeight) {
                                                return 1;
                                            }
                                            if (o1.mHeight > o2.mHeight) {
                                                return -1;
                                            }
                                            return 0;
                                        }
                                    };
                                    Collections.sort(cameraInfo.previewSizes, comparator);
                                    Collections.sort(cameraInfo.pictureSizes, comparator);
                                }
                                CameraController.this.cameraInfos = result;
                            }
                            AndroidUtilities.runOnUIThread(new Runnable() {
                                public void run() {
                                    CameraController.this.loadingCameras = false;
                                    CameraController.this.cameraInitied = true;
                                    NotificationCenter.getGlobalInstance().postNotificationName(NotificationCenter.cameraInitied, new Object[0]);
                                }
                            });
                        } catch (Throwable e) {
                            Throwable e2 = e;
                            AndroidUtilities.runOnUIThread(new Runnable() {
                                public void run() {
                                    CameraController.this.loadingCameras = false;
                                    CameraController.this.cameraInitied = false;
                                }
                            });
                            FileLog.e(e2);
                        }
                    }
                });
            }
        }
    }

    public boolean isCameraInitied() {
        return (!this.cameraInitied || this.cameraInfos == null || this.cameraInfos.isEmpty()) ? false : true;
    }

    public void cleanup() {
        this.threadPool.execute(new Runnable() {
            public void run() {
                if (CameraController.this.cameraInfos != null) {
                    if (!CameraController.this.cameraInfos.isEmpty()) {
                        for (int a = 0; a < CameraController.this.cameraInfos.size(); a++) {
                            CameraInfo info = (CameraInfo) CameraController.this.cameraInfos.get(a);
                            if (info.camera != null) {
                                info.camera.stopPreview();
                                info.camera.setPreviewCallbackWithBuffer(null);
                                info.camera.release();
                                info.camera = null;
                            }
                        }
                        CameraController.this.cameraInfos = null;
                    }
                }
            }
        });
    }

    public void close(final CameraSession session, final CountDownLatch countDownLatch, final Runnable beforeDestroyRunnable) {
        session.destroy();
        this.threadPool.execute(new Runnable() {
            public void run() {
                if (beforeDestroyRunnable != null) {
                    beforeDestroyRunnable.run();
                }
                if (session.cameraInfo.camera != null) {
                    try {
                        session.cameraInfo.camera.stopPreview();
                        session.cameraInfo.camera.setPreviewCallbackWithBuffer(null);
                    } catch (Throwable e) {
                        FileLog.e(e);
                    }
                    try {
                        session.cameraInfo.camera.release();
                    } catch (Throwable e2) {
                        FileLog.e(e2);
                    }
                    session.cameraInfo.camera = null;
                    if (countDownLatch != null) {
                        countDownLatch.countDown();
                    }
                }
            }
        });
        if (countDownLatch != null) {
            try {
                countDownLatch.await();
            } catch (Throwable e) {
                FileLog.e(e);
            }
        }
    }

    public ArrayList<CameraInfo> getCameras() {
        return this.cameraInfos;
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static int getOrientation(byte[] r10) {
        /*
        r0 = 0;
        if (r10 != 0) goto L_0x0004;
    L_0x0003:
        return r0;
    L_0x0004:
        r1 = 0;
        r2 = r1;
        r1 = r0;
    L_0x0007:
        r3 = r2 + 3;
        r4 = 4;
        r5 = 8;
        r6 = 1;
        r7 = 2;
        r8 = r10.length;
        if (r3 >= r8) goto L_0x0062;
    L_0x0011:
        r3 = r2 + 1;
        r2 = r10[r2];
        r8 = 255; // 0xff float:3.57E-43 double:1.26E-321;
        r2 = r2 & r8;
        if (r2 != r8) goto L_0x0063;
    L_0x001a:
        r2 = r10[r3];
        r2 = r2 & r8;
        if (r2 != r8) goto L_0x0022;
    L_0x0020:
        r2 = r3;
        goto L_0x0007;
    L_0x0022:
        r3 = r3 + 1;
        r8 = 216; // 0xd8 float:3.03E-43 double:1.067E-321;
        if (r2 == r8) goto L_0x0020;
    L_0x0028:
        if (r2 != r6) goto L_0x002b;
    L_0x002a:
        goto L_0x0020;
    L_0x002b:
        r8 = 217; // 0xd9 float:3.04E-43 double:1.07E-321;
        if (r2 == r8) goto L_0x0063;
    L_0x002f:
        r8 = 218; // 0xda float:3.05E-43 double:1.077E-321;
        if (r2 != r8) goto L_0x0034;
    L_0x0033:
        goto L_0x0063;
    L_0x0034:
        r1 = pack(r10, r3, r7, r0);
        if (r1 < r7) goto L_0x0061;
    L_0x003a:
        r8 = r3 + r1;
        r9 = r10.length;
        if (r8 <= r9) goto L_0x0040;
    L_0x003f:
        goto L_0x0061;
    L_0x0040:
        r8 = 225; // 0xe1 float:3.15E-43 double:1.11E-321;
        if (r2 != r8) goto L_0x005e;
    L_0x0044:
        if (r1 < r5) goto L_0x005e;
    L_0x0046:
        r8 = r3 + 2;
        r8 = pack(r10, r8, r4, r0);
        r9 = 1165519206; // 0x45786966 float:3974.5874 double:5.758429993E-315;
        if (r8 != r9) goto L_0x005e;
    L_0x0051:
        r8 = r3 + 6;
        r8 = pack(r10, r8, r7, r0);
        if (r8 != 0) goto L_0x005e;
    L_0x0059:
        r3 = r3 + 8;
        r1 = r1 + -8;
        goto L_0x0063;
    L_0x005e:
        r3 = r3 + r1;
        r1 = 0;
        goto L_0x0020;
    L_0x0061:
        return r0;
    L_0x0062:
        r3 = r2;
    L_0x0063:
        if (r1 <= r5) goto L_0x00c1;
    L_0x0065:
        r2 = pack(r10, r3, r4, r0);
        r8 = 1229531648; // 0x49492a00 float:823968.0 double:6.074693478E-315;
        if (r2 == r8) goto L_0x0074;
    L_0x006e:
        r9 = 1296891946; // 0x4d4d002a float:2.14958752E8 double:6.40749757E-315;
        if (r2 == r9) goto L_0x0074;
    L_0x0073:
        return r0;
    L_0x0074:
        if (r2 != r8) goto L_0x0078;
    L_0x0076:
        r8 = r6;
        goto L_0x0079;
    L_0x0078:
        r8 = r0;
    L_0x0079:
        r9 = r3 + 4;
        r4 = pack(r10, r9, r4, r8);
        r4 = r4 + r7;
        r9 = 10;
        if (r4 < r9) goto L_0x00c0;
    L_0x0084:
        if (r4 <= r1) goto L_0x0087;
    L_0x0086:
        goto L_0x00c0;
    L_0x0087:
        r3 = r3 + r4;
        r1 = r1 - r4;
        r9 = r3 + -2;
        r4 = pack(r10, r9, r7, r8);
    L_0x008f:
        r9 = r4 + -1;
        if (r4 <= 0) goto L_0x00c1;
    L_0x0093:
        r4 = 12;
        if (r1 < r4) goto L_0x00c1;
    L_0x0097:
        r2 = pack(r10, r3, r7, r8);
        r4 = 274; // 0x112 float:3.84E-43 double:1.354E-321;
        if (r2 != r4) goto L_0x00ba;
    L_0x009f:
        r4 = r3 + 8;
        r4 = pack(r10, r4, r7, r8);
        if (r4 == r6) goto L_0x00b9;
    L_0x00a7:
        r6 = 3;
        if (r4 == r6) goto L_0x00b6;
    L_0x00aa:
        r6 = 6;
        if (r4 == r6) goto L_0x00b3;
    L_0x00ad:
        if (r4 == r5) goto L_0x00b0;
    L_0x00af:
        return r0;
    L_0x00b0:
        r0 = 270; // 0x10e float:3.78E-43 double:1.334E-321;
        return r0;
    L_0x00b3:
        r0 = 90;
        return r0;
    L_0x00b6:
        r0 = 180; // 0xb4 float:2.52E-43 double:8.9E-322;
        return r0;
    L_0x00b9:
        return r0;
    L_0x00ba:
        r3 = r3 + 12;
        r1 = r1 + -12;
        r4 = r9;
        goto L_0x008f;
    L_0x00c0:
        return r0;
    L_0x00c1:
        return r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.telegram.messenger.camera.CameraController.getOrientation(byte[]):int");
    }

    private static int pack(byte[] bytes, int offset, int length, boolean littleEndian) {
        int step = 1;
        if (littleEndian) {
            offset += length - 1;
            step = -1;
        }
        int value = 0;
        while (true) {
            int length2 = length - 1;
            if (length <= 0) {
                return value;
            }
            value = (value << 8) | (bytes[offset] & 255);
            offset += step;
            length = length2;
        }
    }

    public boolean takePicture(final File path, CameraSession session, final Runnable callback) {
        if (session == null) {
            return false;
        }
        final CameraInfo info = session.cameraInfo;
        try {
            info.camera.takePicture(null, null, new PictureCallback() {
                public void onPictureTaken(byte[] data, Camera camera) {
                    Bitmap bitmap = null;
                    int size = (int) (((float) AndroidUtilities.getPhotoSize()) / AndroidUtilities.density);
                    String key = String.format(Locale.US, "%s@%d_%d", new Object[]{Utilities.MD5(path.getAbsolutePath()), Integer.valueOf(size), Integer.valueOf(size)});
                    try {
                        Options options = new Options();
                        options.inJustDecodeBounds = true;
                        BitmapFactory.decodeByteArray(data, 0, data.length, options);
                        float scaleFactor = Math.max(((float) options.outWidth) / ((float) AndroidUtilities.getPhotoSize()), ((float) options.outHeight) / ((float) AndroidUtilities.getPhotoSize()));
                        if (scaleFactor < 1.0f) {
                            scaleFactor = 1.0f;
                        }
                        options.inJustDecodeBounds = false;
                        options.inSampleSize = (int) scaleFactor;
                        options.inPurgeable = true;
                        bitmap = BitmapFactory.decodeByteArray(data, 0, data.length, options);
                    } catch (Throwable e) {
                        FileLog.e(e);
                    }
                    try {
                        if (info.frontCamera != 0) {
                            try {
                                Matrix matrix = new Matrix();
                                matrix.setRotate((float) CameraController.getOrientation(data));
                                matrix.postScale(-1.0f, 1.0f);
                                Bitmap scaled = Bitmaps.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, false);
                                if (scaled != bitmap) {
                                    bitmap.recycle();
                                }
                                FileOutputStream outputStream = new FileOutputStream(path);
                                scaled.compress(CompressFormat.JPEG, 80, outputStream);
                                outputStream.flush();
                                outputStream.getFD().sync();
                                outputStream.close();
                                if (scaled != null) {
                                    ImageLoader.getInstance().putImageToCache(new BitmapDrawable(scaled), key);
                                }
                                if (callback != null) {
                                    callback.run();
                                }
                                return;
                            } catch (Throwable e2) {
                                FileLog.e(e2);
                            }
                        }
                        FileOutputStream outputStream2 = new FileOutputStream(path);
                        outputStream2.write(data);
                        outputStream2.flush();
                        outputStream2.getFD().sync();
                        outputStream2.close();
                        if (bitmap != null) {
                            ImageLoader.getInstance().putImageToCache(new BitmapDrawable(bitmap), key);
                        }
                    } catch (Throwable e22) {
                        FileLog.e(e22);
                    }
                    if (callback != null) {
                        callback.run();
                    }
                }
            });
            return true;
        } catch (Throwable e) {
            FileLog.e(e);
            return false;
        }
    }

    public void startPreview(final CameraSession session) {
        if (session != null) {
            this.threadPool.execute(new Runnable() {
                @android.annotation.SuppressLint({"NewApi"})
                public void run() {
                    /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.DecodeException: Load method exception in method: org.telegram.messenger.camera.CameraController.5.run():void
	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:116)
	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:249)
	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:256)
	at jadx.core.ProcessClass.process(ProcessClass.java:34)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:306)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler$1.run(JadxDecompiler.java:199)
Caused by: java.lang.NullPointerException
*/
                    /*
                    r0 = this;
                    r0 = r3;
                    r0 = r0.cameraInfo;
                    r0 = r0.camera;
                    if (r0 != 0) goto L_0x001c;
                L_0x0008:
                    r1 = r3;	 Catch:{ Exception -> 0x001a }
                    r1 = r1.cameraInfo;	 Catch:{ Exception -> 0x001a }
                    r2 = r3;	 Catch:{ Exception -> 0x001a }
                    r2 = r2.cameraInfo;	 Catch:{ Exception -> 0x001a }
                    r2 = r2.cameraId;	 Catch:{ Exception -> 0x001a }
                    r2 = android.hardware.Camera.open(r2);	 Catch:{ Exception -> 0x001a }
                    r1.camera = r2;	 Catch:{ Exception -> 0x001a }
                    r0 = r2;	 Catch:{ Exception -> 0x001a }
                    goto L_0x001c;	 Catch:{ Exception -> 0x001a }
                L_0x001a:
                    r1 = move-exception;	 Catch:{ Exception -> 0x001a }
                    goto L_0x0020;	 Catch:{ Exception -> 0x001a }
                L_0x001c:
                    r0.startPreview();	 Catch:{ Exception -> 0x001a }
                    goto L_0x0030;
                    r2 = r3;
                    r2 = r2.cameraInfo;
                    r3 = 0;
                    r2.camera = r3;
                    if (r0 == 0) goto L_0x002d;
                    r0.release();
                    org.telegram.messenger.FileLog.e(r1);
                    return;
                    */
                    throw new UnsupportedOperationException("Method not decompiled: org.telegram.messenger.camera.CameraController.5.run():void");
                }
            });
        }
    }

    public void stopPreview(final CameraSession session) {
        if (session != null) {
            this.threadPool.execute(new Runnable() {
                @android.annotation.SuppressLint({"NewApi"})
                public void run() {
                    /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.DecodeException: Load method exception in method: org.telegram.messenger.camera.CameraController.6.run():void
	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:116)
	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:249)
	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:256)
	at jadx.core.ProcessClass.process(ProcessClass.java:34)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:306)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler$1.run(JadxDecompiler.java:199)
Caused by: java.lang.NullPointerException
*/
                    /*
                    r0 = this;
                    r0 = r3;
                    r0 = r0.cameraInfo;
                    r0 = r0.camera;
                    if (r0 != 0) goto L_0x001c;
                L_0x0008:
                    r1 = r3;	 Catch:{ Exception -> 0x001a }
                    r1 = r1.cameraInfo;	 Catch:{ Exception -> 0x001a }
                    r2 = r3;	 Catch:{ Exception -> 0x001a }
                    r2 = r2.cameraInfo;	 Catch:{ Exception -> 0x001a }
                    r2 = r2.cameraId;	 Catch:{ Exception -> 0x001a }
                    r2 = android.hardware.Camera.open(r2);	 Catch:{ Exception -> 0x001a }
                    r1.camera = r2;	 Catch:{ Exception -> 0x001a }
                    r0 = r2;	 Catch:{ Exception -> 0x001a }
                    goto L_0x001c;	 Catch:{ Exception -> 0x001a }
                L_0x001a:
                    r1 = move-exception;	 Catch:{ Exception -> 0x001a }
                    goto L_0x0020;	 Catch:{ Exception -> 0x001a }
                L_0x001c:
                    r0.stopPreview();	 Catch:{ Exception -> 0x001a }
                    goto L_0x0030;
                    r2 = r3;
                    r2 = r2.cameraInfo;
                    r3 = 0;
                    r2.camera = r3;
                    if (r0 == 0) goto L_0x002d;
                    r0.release();
                    org.telegram.messenger.FileLog.e(r1);
                    return;
                    */
                    throw new UnsupportedOperationException("Method not decompiled: org.telegram.messenger.camera.CameraController.6.run():void");
                }
            });
        }
    }

    public void openRound(CameraSession session, SurfaceTexture texture, Runnable callback, Runnable configureCallback) {
        if (session != null) {
            if (texture != null) {
                final CameraSession cameraSession = session;
                final Runnable runnable = configureCallback;
                final SurfaceTexture surfaceTexture = texture;
                final Runnable runnable2 = callback;
                this.threadPool.execute(new Runnable() {
                    @SuppressLint({"NewApi"})
                    public void run() {
                        Camera camera = cameraSession.cameraInfo.camera;
                        try {
                            if (BuildVars.LOGS_ENABLED) {
                                FileLog.d("start creating round camera session");
                            }
                            if (camera == null) {
                                CameraInfo cameraInfo = cameraSession.cameraInfo;
                                Camera open = Camera.open(cameraSession.cameraInfo.cameraId);
                                cameraInfo.camera = open;
                                camera = open;
                            }
                            Parameters params = camera.getParameters();
                            cameraSession.configureRoundCamera();
                            if (runnable != null) {
                                runnable.run();
                            }
                            camera.setPreviewTexture(surfaceTexture);
                            camera.startPreview();
                            if (runnable2 != null) {
                                AndroidUtilities.runOnUIThread(runnable2);
                            }
                            if (BuildVars.LOGS_ENABLED) {
                                FileLog.d("round camera session created");
                            }
                        } catch (Throwable e) {
                            cameraSession.cameraInfo.camera = null;
                            if (camera != null) {
                                camera.release();
                            }
                            FileLog.e(e);
                        }
                    }
                });
                return;
            }
        }
        if (BuildVars.LOGS_ENABLED) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("failed to open round ");
            stringBuilder.append(session);
            stringBuilder.append(" tex = ");
            stringBuilder.append(texture);
            FileLog.d(stringBuilder.toString());
        }
    }

    public void open(CameraSession session, SurfaceTexture texture, Runnable callback, Runnable prestartCallback) {
        if (session != null) {
            if (texture != null) {
                final CameraSession cameraSession = session;
                final Runnable runnable = prestartCallback;
                final SurfaceTexture surfaceTexture = texture;
                final Runnable runnable2 = callback;
                this.threadPool.execute(new Runnable() {
                    @android.annotation.SuppressLint({"NewApi"})
                    public void run() {
                        /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.DecodeException: Load method exception in method: org.telegram.messenger.camera.CameraController.8.run():void
	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:116)
	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:249)
	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:256)
	at jadx.core.ProcessClass.process(ProcessClass.java:34)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:306)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler$1.run(JadxDecompiler.java:199)
Caused by: java.lang.NullPointerException
*/
                        /*
                        r0 = this;
                        r0 = r3;
                        r0 = r0.cameraInfo;
                        r0 = r0.camera;
                        if (r0 != 0) goto L_0x001d;
                    L_0x0008:
                        r1 = r3;	 Catch:{ Exception -> 0x001a }
                        r1 = r1.cameraInfo;	 Catch:{ Exception -> 0x001a }
                        r2 = r3;	 Catch:{ Exception -> 0x001a }
                        r2 = r2.cameraInfo;	 Catch:{ Exception -> 0x001a }
                        r2 = r2.cameraId;	 Catch:{ Exception -> 0x001a }
                        r2 = android.hardware.Camera.open(r2);	 Catch:{ Exception -> 0x001a }
                        r1.camera = r2;	 Catch:{ Exception -> 0x001a }
                        r0 = r2;	 Catch:{ Exception -> 0x001a }
                        goto L_0x001d;	 Catch:{ Exception -> 0x001a }
                    L_0x001a:
                        r1 = move-exception;	 Catch:{ Exception -> 0x001a }
                        goto L_0x008d;	 Catch:{ Exception -> 0x001a }
                    L_0x001d:
                        r1 = r0.getParameters();	 Catch:{ Exception -> 0x001a }
                        r2 = r1.getSupportedFlashModes();	 Catch:{ Exception -> 0x001a }
                        r3 = org.telegram.messenger.camera.CameraController.this;	 Catch:{ Exception -> 0x001a }
                        r3 = r3.availableFlashModes;	 Catch:{ Exception -> 0x001a }
                        r3.clear();	 Catch:{ Exception -> 0x001a }
                        if (r2 == 0) goto L_0x006d;	 Catch:{ Exception -> 0x001a }
                        r3 = 0;	 Catch:{ Exception -> 0x001a }
                        r4 = r3;	 Catch:{ Exception -> 0x001a }
                        r5 = r2.size();	 Catch:{ Exception -> 0x001a }
                        if (r4 >= r5) goto L_0x005e;	 Catch:{ Exception -> 0x001a }
                        r5 = r2.get(r4);	 Catch:{ Exception -> 0x001a }
                        r5 = (java.lang.String) r5;	 Catch:{ Exception -> 0x001a }
                        r6 = "off";	 Catch:{ Exception -> 0x001a }
                        r6 = r5.equals(r6);	 Catch:{ Exception -> 0x001a }
                        if (r6 != 0) goto L_0x0054;	 Catch:{ Exception -> 0x001a }
                        r6 = "on";	 Catch:{ Exception -> 0x001a }
                        r6 = r5.equals(r6);	 Catch:{ Exception -> 0x001a }
                        if (r6 != 0) goto L_0x0054;	 Catch:{ Exception -> 0x001a }
                        r6 = "auto";	 Catch:{ Exception -> 0x001a }
                        r6 = r5.equals(r6);	 Catch:{ Exception -> 0x001a }
                        if (r6 == 0) goto L_0x005b;	 Catch:{ Exception -> 0x001a }
                        r6 = org.telegram.messenger.camera.CameraController.this;	 Catch:{ Exception -> 0x001a }
                        r6 = r6.availableFlashModes;	 Catch:{ Exception -> 0x001a }
                        r6.add(r5);	 Catch:{ Exception -> 0x001a }
                        r4 = r4 + 1;	 Catch:{ Exception -> 0x001a }
                        goto L_0x0030;	 Catch:{ Exception -> 0x001a }
                        r4 = r3;	 Catch:{ Exception -> 0x001a }
                        r5 = org.telegram.messenger.camera.CameraController.this;	 Catch:{ Exception -> 0x001a }
                        r5 = r5.availableFlashModes;	 Catch:{ Exception -> 0x001a }
                        r3 = r5.get(r3);	 Catch:{ Exception -> 0x001a }
                        r3 = (java.lang.String) r3;	 Catch:{ Exception -> 0x001a }
                        r4.checkFlashMode(r3);	 Catch:{ Exception -> 0x001a }
                        r3 = r4;	 Catch:{ Exception -> 0x001a }
                        if (r3 == 0) goto L_0x0076;	 Catch:{ Exception -> 0x001a }
                        r3 = r4;	 Catch:{ Exception -> 0x001a }
                        r3.run();	 Catch:{ Exception -> 0x001a }
                        r3 = r3;	 Catch:{ Exception -> 0x001a }
                        r3.configurePhotoCamera();	 Catch:{ Exception -> 0x001a }
                        r3 = r5;	 Catch:{ Exception -> 0x001a }
                        r0.setPreviewTexture(r3);	 Catch:{ Exception -> 0x001a }
                        r0.startPreview();	 Catch:{ Exception -> 0x001a }
                        r3 = r6;	 Catch:{ Exception -> 0x001a }
                        if (r3 == 0) goto L_0x008c;	 Catch:{ Exception -> 0x001a }
                        r3 = r6;	 Catch:{ Exception -> 0x001a }
                        org.telegram.messenger.AndroidUtilities.runOnUIThread(r3);	 Catch:{ Exception -> 0x001a }
                        goto L_0x009d;
                        r2 = r3;
                        r2 = r2.cameraInfo;
                        r3 = 0;
                        r2.camera = r3;
                        if (r0 == 0) goto L_0x009a;
                        r0.release();
                        org.telegram.messenger.FileLog.e(r1);
                        return;
                        */
                        throw new UnsupportedOperationException("Method not decompiled: org.telegram.messenger.camera.CameraController.8.run():void");
                    }
                });
            }
        }
    }

    public void recordVideo(CameraSession session, File path, VideoTakeCallback callback, Runnable onVideoStartRecord) {
        CameraSession cameraSession = session;
        if (cameraSession != null) {
            CameraInfo info = cameraSession.cameraInfo;
            final Camera camera = info.camera;
            final CameraSession cameraSession2 = cameraSession;
            final File file = path;
            final CameraInfo cameraInfo = info;
            final VideoTakeCallback videoTakeCallback = callback;
            final Runnable runnable = onVideoStartRecord;
            this.threadPool.execute(new Runnable() {
                public void run() {
                    try {
                        if (camera != null) {
                            try {
                                Parameters params = camera.getParameters();
                                params.setFlashMode(cameraSession2.getCurrentFlashMode().equals("on") ? "torch" : "off");
                                camera.setParameters(params);
                            } catch (Throwable e) {
                                FileLog.e(e);
                            }
                            camera.unlock();
                            try {
                                CameraController.this.recorder = new MediaRecorder();
                                CameraController.this.recorder.setCamera(camera);
                                CameraController.this.recorder.setVideoSource(1);
                                CameraController.this.recorder.setAudioSource(5);
                                cameraSession2.configureRecorder(1, CameraController.this.recorder);
                                CameraController.this.recorder.setOutputFile(file.getAbsolutePath());
                                CameraController.this.recorder.setMaxFileSize(1073741824);
                                CameraController.this.recorder.setVideoFrameRate(30);
                                CameraController.this.recorder.setMaxDuration(0);
                                Size pictureSize = CameraController.chooseOptimalSize(cameraInfo.getPictureSizes(), 720, 480, new Size(16, 9));
                                CameraController.this.recorder.setVideoEncodingBitRate(1800000);
                                CameraController.this.recorder.setVideoSize(pictureSize.getWidth(), pictureSize.getHeight());
                                CameraController.this.recorder.setOnInfoListener(CameraController.this);
                                CameraController.this.recorder.prepare();
                                CameraController.this.recorder.start();
                                CameraController.this.onVideoTakeCallback = videoTakeCallback;
                                CameraController.this.recordedFile = file.getAbsolutePath();
                                if (runnable != null) {
                                    AndroidUtilities.runOnUIThread(runnable);
                                }
                            } catch (Throwable e2) {
                                CameraController.this.recorder.release();
                                CameraController.this.recorder = null;
                                FileLog.e(e2);
                            }
                        }
                    } catch (Throwable e22) {
                        FileLog.e(e22);
                    }
                }
            });
        }
    }

    public void onInfo(MediaRecorder mediaRecorder, int what, int extra) {
        if (what == 800 || what == 801 || what == 1) {
            MediaRecorder tempRecorder = this.recorder;
            this.recorder = null;
            if (tempRecorder != null) {
                tempRecorder.stop();
                tempRecorder.release();
            }
            if (this.onVideoTakeCallback != null) {
                finishRecordingVideo();
            }
        }
    }

    public void stopVideoRecording(final CameraSession session, final boolean abandon) {
        this.threadPool.execute(new Runnable() {
            public void run() {
                try {
                    final Camera camera = session.cameraInfo.camera;
                    if (!(camera == null || CameraController.this.recorder == null)) {
                        MediaRecorder tempRecorder = CameraController.this.recorder;
                        CameraController.this.recorder = null;
                        try {
                            tempRecorder.stop();
                        } catch (Throwable e) {
                            FileLog.e(e);
                        }
                        try {
                            tempRecorder.release();
                        } catch (Throwable e2) {
                            FileLog.e(e2);
                        }
                        try {
                            camera.reconnect();
                            camera.startPreview();
                        } catch (Throwable e22) {
                            FileLog.e(e22);
                        }
                        try {
                            session.stopVideoRecording();
                        } catch (Throwable e222) {
                            FileLog.e(e222);
                        }
                    }
                    try {
                        Parameters params = camera.getParameters();
                        params.setFlashMode("off");
                        camera.setParameters(params);
                    } catch (Throwable e3) {
                        FileLog.e(e3);
                    }
                    CameraController.this.threadPool.execute(new Runnable() {
                        public void run() {
                            try {
                                Parameters params = camera.getParameters();
                                params.setFlashMode(session.getCurrentFlashMode());
                                camera.setParameters(params);
                            } catch (Throwable e) {
                                FileLog.e(e);
                            }
                        }
                    });
                    if (abandon || CameraController.this.onVideoTakeCallback == null) {
                        CameraController.this.onVideoTakeCallback = null;
                    } else {
                        CameraController.this.finishRecordingVideo();
                    }
                } catch (Exception e4) {
                }
            }
        });
    }

    public static Size chooseOptimalSize(List<Size> choices, int width, int height, Size aspectRatio) {
        List<Size> bigEnough = new ArrayList();
        int w = aspectRatio.getWidth();
        int h = aspectRatio.getHeight();
        for (int a = 0; a < choices.size(); a++) {
            Size option = (Size) choices.get(a);
            if (option.getHeight() == (option.getWidth() * h) / w && option.getWidth() >= width && option.getHeight() >= height) {
                bigEnough.add(option);
            }
        }
        if (bigEnough.size() > 0) {
            return (Size) Collections.min(bigEnough, new CompareSizesByArea());
        }
        return (Size) Collections.max(choices, new CompareSizesByArea());
    }
}
