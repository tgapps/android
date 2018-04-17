package org.telegram.messenger.camera;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.view.TextureView;
import android.view.TextureView.SurfaceTextureListener;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;
import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.tgnet.ConnectionsManager;

@SuppressLint({"NewApi"})
public class CameraView extends FrameLayout implements SurfaceTextureListener {
    private CameraSession cameraSession;
    private boolean circleShape = false;
    private int clipLeft;
    private int clipTop;
    private int cx;
    private int cy;
    private CameraViewDelegate delegate;
    private int focusAreaSize;
    private float focusProgress = 1.0f;
    private boolean initialFrontface;
    private boolean initied;
    private float innerAlpha;
    private Paint innerPaint = new Paint(1);
    private DecelerateInterpolator interpolator = new DecelerateInterpolator();
    private boolean isFrontface;
    private long lastDrawTime;
    private Matrix matrix = new Matrix();
    private boolean mirror;
    private float outerAlpha;
    private Paint outerPaint = new Paint(1);
    private Size previewSize;
    private TextureView textureView;
    private Matrix txform = new Matrix();

    public interface CameraViewDelegate {
        void onCameraCreated(Camera camera);

        void onCameraInit();
    }

    private void adjustAspectRatio(int r1, int r2, int r3) {
        /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.DecodeException: Load method exception in method: org.telegram.messenger.camera.CameraView.adjustAspectRatio(int, int, int):void
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
        r0 = r18;
        r1 = r19;
        r2 = r20;
        r3 = r21;
        r4 = r0.txform;
        r4.reset();
        r4 = r18.getWidth();
        r5 = r18.getHeight();
        r6 = r4 / 2;
        r6 = (float) r6;
        r7 = r5 / 2;
        r7 = (float) r7;
        r8 = 2;
        if (r3 == 0) goto L_0x0032;
    L_0x001e:
        if (r3 != r8) goto L_0x0021;
    L_0x0020:
        goto L_0x0032;
    L_0x0021:
        r9 = r0.clipTop;
        r9 = r9 + r5;
        r9 = (float) r9;
        r10 = (float) r2;
        r9 = r9 / r10;
        r10 = r0.clipLeft;
        r10 = r10 + r4;
        r10 = (float) r10;
        r11 = (float) r1;
        r10 = r10 / r11;
        r9 = java.lang.Math.max(r9, r10);
        goto L_0x0042;
    L_0x0032:
        r9 = r0.clipTop;
        r9 = r9 + r5;
        r9 = (float) r9;
        r10 = (float) r1;
        r9 = r9 / r10;
        r10 = r0.clipLeft;
        r10 = r10 + r4;
        r10 = (float) r10;
        r11 = (float) r2;
        r10 = r10 / r11;
        r9 = java.lang.Math.max(r9, r10);
        r10 = (float) r1;
        r10 = r10 * r9;
        r11 = (float) r2;
        r11 = r11 * r9;
        r12 = (float) r4;
        r12 = r11 / r12;
        r13 = (float) r5;
        r13 = r10 / r13;
        r14 = r0.txform;
        r14.postScale(r12, r13, r6, r7);
        r14 = 1;
        if (r14 == r3) goto L_0x0063;
        r14 = 3;
        if (r14 != r3) goto L_0x0059;
        goto L_0x0063;
        if (r8 != r3) goto L_0x006f;
        r14 = r0.txform;
        r15 = 1127481344; // 0x43340000 float:180.0 double:5.570497984E-315;
        r14.postRotate(r15, r6, r7);
        goto L_0x006f;
        r14 = r0.txform;
        r15 = 90;
        r16 = r3 + -2;
        r15 = r15 * r16;
        r15 = (float) r15;
        r14.postRotate(r15, r6, r7);
        r14 = r0.mirror;
        if (r14 == 0) goto L_0x007c;
        r14 = r0.txform;
        r15 = -1082130432; // 0xffffffffbf800000 float:-1.0 double:NaN;
        r8 = 1065353216; // 0x3f800000 float:1.0 double:5.263544247E-315;
        r14.postScale(r15, r8, r6, r7);
        r8 = r0.clipTop;
        if (r8 != 0) goto L_0x0084;
        r8 = r0.clipLeft;
        if (r8 == 0) goto L_0x0094;
        r8 = r0.txform;
        r14 = r0.clipLeft;
        r14 = -r14;
        r15 = 2;
        r14 = r14 / r15;
        r14 = (float) r14;
        r1 = r0.clipTop;
        r1 = -r1;
        r1 = r1 / r15;
        r1 = (float) r1;
        r8.postTranslate(r14, r1);
        r1 = r0.textureView;
        r8 = r0.txform;
        r1.setTransform(r8);
        r1 = new android.graphics.Matrix;
        r1.<init>();
        r8 = r0.cameraSession;
        r8 = r8.getDisplayOrientation();
        r8 = (float) r8;
        r1.postRotate(r8);
        r8 = (float) r4;
        r14 = 1157234688; // 0x44fa0000 float:2000.0 double:5.717499035E-315;
        r8 = r8 / r14;
        r15 = (float) r5;
        r15 = r15 / r14;
        r1.postScale(r8, r15);
        r8 = (float) r4;
        r14 = 1073741824; // 0x40000000 float:2.0 double:5.304989477E-315;
        r8 = r8 / r14;
        r15 = (float) r5;
        r15 = r15 / r14;
        r1.postTranslate(r8, r15);
        r8 = r0.matrix;
        r1.invert(r8);
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.telegram.messenger.camera.CameraView.adjustAspectRatio(int, int, int):void");
    }

    public CameraView(Context context, boolean frontface) {
        super(context, null);
        this.isFrontface = frontface;
        this.initialFrontface = frontface;
        this.textureView = new TextureView(context);
        this.textureView.setSurfaceTextureListener(this);
        addView(this.textureView);
        this.focusAreaSize = AndroidUtilities.dp(96.0f);
        this.outerPaint.setColor(-1);
        this.outerPaint.setStyle(Style.STROKE);
        this.outerPaint.setStrokeWidth((float) AndroidUtilities.dp(2.0f));
        this.innerPaint.setColor(ConnectionsManager.DEFAULT_DATACENTER_ID);
    }

    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        checkPreviewMatrix();
    }

    public void setMirror(boolean value) {
        this.mirror = value;
    }

    public boolean isFrontface() {
        return this.isFrontface;
    }

    public boolean hasFrontFaceCamera() {
        ArrayList<CameraInfo> cameraInfos = CameraController.getInstance().getCameras();
        for (int a = 0; a < cameraInfos.size(); a++) {
            if (((CameraInfo) cameraInfos.get(a)).frontCamera != 0) {
                return true;
            }
        }
        return false;
    }

    public void switchCamera() {
        if (this.cameraSession != null) {
            CameraController.getInstance().close(this.cameraSession, null, null);
            this.cameraSession = null;
        }
        this.initied = false;
        this.isFrontface ^= 1;
        initCamera(this.isFrontface);
    }

    private void initCamera(boolean front) {
        CameraView cameraView = this;
        CameraInfo info = null;
        ArrayList<CameraInfo> cameraInfos = CameraController.getInstance().getCameras();
        if (cameraInfos != null) {
            for (int a = 0; a < cameraInfos.size(); a++) {
                CameraInfo cameraInfo = (CameraInfo) cameraInfos.get(a);
                if ((cameraView.isFrontface && cameraInfo.frontCamera != 0) || (!cameraView.isFrontface && cameraInfo.frontCamera == 0)) {
                    info = cameraInfo;
                    break;
                }
            }
            if (info != null) {
                Size aspectRatio;
                int wantedWidth;
                int wantedHeight;
                int width;
                Size pictureSize;
                Size pictureSize2;
                SurfaceTexture surfaceTexture;
                float screenSize = ((float) Math.max(AndroidUtilities.displaySize.x, AndroidUtilities.displaySize.y)) / ((float) Math.min(AndroidUtilities.displaySize.x, AndroidUtilities.displaySize.y));
                if (cameraView.initialFrontface) {
                    aspectRatio = new Size(16, 9);
                    wantedWidth = 480;
                    wantedHeight = 270;
                } else if (Math.abs(screenSize - 1.3333334f) < 0.1f) {
                    aspectRatio = new Size(4, 3);
                    wantedWidth = 1280;
                    wantedHeight = 960;
                } else {
                    aspectRatio = new Size(16, 9);
                    wantedWidth = 1280;
                    wantedHeight = 720;
                    if (cameraView.textureView.getWidth() > 0 && cameraView.textureView.getHeight() > 0) {
                        width = Math.min(AndroidUtilities.displaySize.x, AndroidUtilities.displaySize.y);
                        cameraView.previewSize = CameraController.chooseOptimalSize(info.getPreviewSizes(), width, (aspectRatio.getHeight() * width) / aspectRatio.getWidth(), aspectRatio);
                    }
                    pictureSize = CameraController.chooseOptimalSize(info.getPictureSizes(), wantedWidth, wantedHeight, aspectRatio);
                    if (pictureSize.getWidth() >= 1280 && pictureSize.getHeight() >= 1280) {
                        if (Math.abs(screenSize - 1.3333334f) >= 0.1f) {
                            aspectRatio = new Size(3, 4);
                        } else {
                            aspectRatio = new Size(9, 16);
                        }
                        pictureSize2 = CameraController.chooseOptimalSize(info.getPictureSizes(), wantedHeight, wantedWidth, aspectRatio);
                        if (pictureSize2.getWidth() < 1280 || pictureSize2.getHeight() < 1280) {
                            pictureSize = pictureSize2;
                        }
                    }
                    surfaceTexture = cameraView.textureView.getSurfaceTexture();
                    if (!(cameraView.previewSize == null || surfaceTexture == null)) {
                        surfaceTexture.setDefaultBufferSize(cameraView.previewSize.getWidth(), cameraView.previewSize.getHeight());
                        cameraView.cameraSession = new CameraSession(info, cameraView.previewSize, pictureSize, 256);
                        CameraController.getInstance().open(cameraView.cameraSession, surfaceTexture, new Runnable() {
                            public void run() {
                                if (CameraView.this.cameraSession != null) {
                                    CameraView.this.cameraSession.setInitied();
                                }
                                CameraView.this.checkPreviewMatrix();
                            }
                        }, new Runnable() {
                            public void run() {
                                if (CameraView.this.delegate != null) {
                                    CameraView.this.delegate.onCameraCreated(CameraView.this.cameraSession.cameraInfo.camera);
                                }
                            }
                        });
                    }
                }
                width = Math.min(AndroidUtilities.displaySize.x, AndroidUtilities.displaySize.y);
                cameraView.previewSize = CameraController.chooseOptimalSize(info.getPreviewSizes(), width, (aspectRatio.getHeight() * width) / aspectRatio.getWidth(), aspectRatio);
                pictureSize = CameraController.chooseOptimalSize(info.getPictureSizes(), wantedWidth, wantedHeight, aspectRatio);
                if (Math.abs(screenSize - 1.3333334f) >= 0.1f) {
                    aspectRatio = new Size(9, 16);
                } else {
                    aspectRatio = new Size(3, 4);
                }
                pictureSize2 = CameraController.chooseOptimalSize(info.getPictureSizes(), wantedHeight, wantedWidth, aspectRatio);
                pictureSize = pictureSize2;
                surfaceTexture = cameraView.textureView.getSurfaceTexture();
                surfaceTexture.setDefaultBufferSize(cameraView.previewSize.getWidth(), cameraView.previewSize.getHeight());
                cameraView.cameraSession = new CameraSession(info, cameraView.previewSize, pictureSize, 256);
                CameraController.getInstance().open(cameraView.cameraSession, surfaceTexture, /* anonymous class already generated */, /* anonymous class already generated */);
            }
        }
    }

    public Size getPreviewSize() {
        return this.previewSize;
    }

    public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
        initCamera(this.isFrontface);
    }

    public void onSurfaceTextureSizeChanged(SurfaceTexture surfaceTexture, int width, int height) {
        checkPreviewMatrix();
    }

    public boolean onSurfaceTextureDestroyed(SurfaceTexture surfaceTexture) {
        if (this.cameraSession != null) {
            CameraController.getInstance().close(this.cameraSession, null, null);
        }
        return false;
    }

    public void onSurfaceTextureUpdated(SurfaceTexture surface) {
        if (!this.initied && this.cameraSession != null && this.cameraSession.isInitied()) {
            if (this.delegate != null) {
                this.delegate.onCameraInit();
            }
            this.initied = true;
        }
    }

    public void setClipTop(int value) {
        this.clipTop = value;
    }

    public void setClipLeft(int value) {
        this.clipLeft = value;
    }

    private void checkPreviewMatrix() {
        if (this.previewSize != null) {
            adjustAspectRatio(this.previewSize.getWidth(), this.previewSize.getHeight(), ((Activity) getContext()).getWindowManager().getDefaultDisplay().getRotation());
        }
    }

    private Rect calculateTapArea(float x, float y, float coefficient) {
        int areaSize = Float.valueOf(((float) this.focusAreaSize) * coefficient).intValue();
        int left = clamp(((int) x) - (areaSize / 2), 0, getWidth() - areaSize);
        int top = clamp(((int) y) - (areaSize / 2), 0, getHeight() - areaSize);
        RectF rectF = new RectF((float) left, (float) top, (float) (left + areaSize), (float) (top + areaSize));
        this.matrix.mapRect(rectF);
        return new Rect(Math.round(rectF.left), Math.round(rectF.top), Math.round(rectF.right), Math.round(rectF.bottom));
    }

    private int clamp(int x, int min, int max) {
        if (x > max) {
            return max;
        }
        if (x < min) {
            return min;
        }
        return x;
    }

    public void focusToPoint(int x, int y) {
        Rect focusRect = calculateTapArea((float) x, (float) y, 1.0f);
        Rect meteringRect = calculateTapArea((float) x, (float) y, 1.5f);
        if (this.cameraSession != null) {
            this.cameraSession.focusToRect(focusRect, meteringRect);
        }
        this.focusProgress = 0.0f;
        this.innerAlpha = 1.0f;
        this.outerAlpha = 1.0f;
        this.cx = x;
        this.cy = y;
        this.lastDrawTime = System.currentTimeMillis();
        invalidate();
    }

    public void setDelegate(CameraViewDelegate cameraViewDelegate) {
        this.delegate = cameraViewDelegate;
    }

    public boolean isInitied() {
        return this.initied;
    }

    public CameraSession getCameraSession() {
        return this.cameraSession;
    }

    public void destroy(boolean async, Runnable beforeDestroyRunnable) {
        if (this.cameraSession != null) {
            this.cameraSession.destroy();
            CameraController.getInstance().close(this.cameraSession, !async ? new CountDownLatch(1) : null, beforeDestroyRunnable);
        }
    }

    protected boolean drawChild(Canvas canvas, View child, long drawingTime) {
        Canvas canvas2 = canvas;
        boolean result = super.drawChild(canvas, child, drawingTime);
        if (!(this.focusProgress == 1.0f && r0.innerAlpha == 0.0f && r0.outerAlpha == 0.0f)) {
            int baseRad = AndroidUtilities.dp(1106247680);
            long newTime = System.currentTimeMillis();
            long dt = newTime - r0.lastDrawTime;
            if (dt < 0 || dt > 17) {
                dt = 17;
            }
            r0.lastDrawTime = newTime;
            r0.outerPaint.setAlpha((int) (r0.interpolator.getInterpolation(r0.outerAlpha) * 255.0f));
            r0.innerPaint.setAlpha((int) (r0.interpolator.getInterpolation(r0.innerAlpha) * 127.0f));
            float interpolated = r0.interpolator.getInterpolation(r0.focusProgress);
            canvas2.drawCircle((float) r0.cx, (float) r0.cy, ((float) baseRad) + (((float) baseRad) * (1.0f - interpolated)), r0.outerPaint);
            canvas2.drawCircle((float) r0.cx, (float) r0.cy, ((float) baseRad) * interpolated, r0.innerPaint);
            if (r0.focusProgress < 1.0f) {
                r0.focusProgress += ((float) dt) / 200.0f;
                if (r0.focusProgress > 1.0f) {
                    r0.focusProgress = 1.0f;
                }
                invalidate();
            } else if (r0.innerAlpha != 0.0f) {
                r0.innerAlpha -= ((float) dt) / 150.0f;
                if (r0.innerAlpha < 0.0f) {
                    r0.innerAlpha = 0.0f;
                }
                invalidate();
            } else if (r0.outerAlpha != 0.0f) {
                r0.outerAlpha -= ((float) dt) / 150.0f;
                if (r0.outerAlpha < 0.0f) {
                    r0.outerAlpha = 0.0f;
                }
                invalidate();
            }
        }
        return result;
    }
}
