package org.telegram.ui.Components;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapShader;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader.TileMode;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import java.io.File;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor.DiscardPolicy;
import java.util.concurrent.TimeUnit;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.FileLog;

public class AnimatedFileDrawable extends BitmapDrawable implements Animatable {
    private static ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(2, new DiscardPolicy());
    private static final Handler uiHandler = new Handler(Looper.getMainLooper());
    private RectF actualDrawRect = new RectF();
    private boolean applyTransformation;
    private Bitmap backgroundBitmap;
    private BitmapShader backgroundShader;
    private RectF bitmapRect = new RectF();
    private boolean decodeSingleFrame;
    private boolean decoderCreated;
    private boolean destroyWhenDone;
    private final Rect dstRect = new Rect();
    private int invalidateAfter = 50;
    private volatile boolean isRecycled;
    private volatile boolean isRunning;
    private long lastFrameDecodeTime;
    private long lastFrameTime;
    private int lastTimeStamp;
    private Runnable loadFrameRunnable = new Runnable() {
        public void run() {
            if (!AnimatedFileDrawable.this.isRecycled) {
                if (!AnimatedFileDrawable.this.decoderCreated && AnimatedFileDrawable.this.nativePtr == 0) {
                    AnimatedFileDrawable.this.nativePtr = AnimatedFileDrawable.createDecoder(AnimatedFileDrawable.this.path.getAbsolutePath(), AnimatedFileDrawable.this.metaData);
                    AnimatedFileDrawable.this.decoderCreated = true;
                }
                try {
                    if (AnimatedFileDrawable.this.backgroundBitmap == null) {
                        AnimatedFileDrawable.this.backgroundBitmap = Bitmap.createBitmap(AnimatedFileDrawable.this.metaData[0], AnimatedFileDrawable.this.metaData[1], Config.ARGB_8888);
                        if (!(AnimatedFileDrawable.this.backgroundShader != null || AnimatedFileDrawable.this.backgroundBitmap == null || AnimatedFileDrawable.this.roundRadius == 0)) {
                            AnimatedFileDrawable.this.backgroundShader = new BitmapShader(AnimatedFileDrawable.this.backgroundBitmap, TileMode.CLAMP, TileMode.CLAMP);
                        }
                    }
                } catch (Throwable e) {
                    FileLog.e(e);
                }
                if (AnimatedFileDrawable.this.backgroundBitmap != null) {
                    AnimatedFileDrawable.this.lastFrameDecodeTime = System.currentTimeMillis();
                    AnimatedFileDrawable.getVideoFrame(AnimatedFileDrawable.this.nativePtr, AnimatedFileDrawable.this.backgroundBitmap, AnimatedFileDrawable.this.metaData);
                }
            }
            AndroidUtilities.runOnUIThread(AnimatedFileDrawable.this.uiRunnable);
        }
    };
    private Runnable loadFrameTask;
    protected final Runnable mInvalidateTask = new Runnable() {
        public void run() {
            if (AnimatedFileDrawable.this.secondParentView != null) {
                AnimatedFileDrawable.this.secondParentView.invalidate();
            } else if (AnimatedFileDrawable.this.parentView != null) {
                AnimatedFileDrawable.this.parentView.invalidate();
            }
        }
    };
    private final Runnable mStartTask = new Runnable() {
        public void run() {
            if (AnimatedFileDrawable.this.secondParentView != null) {
                AnimatedFileDrawable.this.secondParentView.invalidate();
            } else if (AnimatedFileDrawable.this.parentView != null) {
                AnimatedFileDrawable.this.parentView.invalidate();
            }
        }
    };
    private final int[] metaData = new int[4];
    private volatile long nativePtr;
    private Bitmap nextRenderingBitmap;
    private BitmapShader nextRenderingShader;
    private View parentView = null;
    private File path;
    private boolean recycleWithSecond;
    private Bitmap renderingBitmap;
    private BitmapShader renderingShader;
    private int roundRadius;
    private RectF roundRect = new RectF();
    private float scaleX = 1.0f;
    private float scaleY = 1.0f;
    private View secondParentView = null;
    private Matrix shaderMatrix = new Matrix();
    private boolean singleFrameDecoded;
    private Runnable uiRunnable = new Runnable() {
        public void run() {
            if (AnimatedFileDrawable.this.destroyWhenDone && AnimatedFileDrawable.this.nativePtr != 0) {
                AnimatedFileDrawable.destroyDecoder(AnimatedFileDrawable.this.nativePtr);
                AnimatedFileDrawable.this.nativePtr = 0;
            }
            if (AnimatedFileDrawable.this.nativePtr == 0) {
                if (AnimatedFileDrawable.this.renderingBitmap != null) {
                    AnimatedFileDrawable.this.renderingBitmap.recycle();
                    AnimatedFileDrawable.this.renderingBitmap = null;
                }
                if (AnimatedFileDrawable.this.backgroundBitmap != null) {
                    AnimatedFileDrawable.this.backgroundBitmap.recycle();
                    AnimatedFileDrawable.this.backgroundBitmap = null;
                }
                return;
            }
            AnimatedFileDrawable.this.singleFrameDecoded = true;
            AnimatedFileDrawable.this.loadFrameTask = null;
            AnimatedFileDrawable.this.nextRenderingBitmap = AnimatedFileDrawable.this.backgroundBitmap;
            AnimatedFileDrawable.this.nextRenderingShader = AnimatedFileDrawable.this.backgroundShader;
            if (AnimatedFileDrawable.this.metaData[3] < AnimatedFileDrawable.this.lastTimeStamp) {
                AnimatedFileDrawable.this.lastTimeStamp = 0;
            }
            if (AnimatedFileDrawable.this.metaData[3] - AnimatedFileDrawable.this.lastTimeStamp != 0) {
                AnimatedFileDrawable.this.invalidateAfter = AnimatedFileDrawable.this.metaData[3] - AnimatedFileDrawable.this.lastTimeStamp;
            }
            AnimatedFileDrawable.this.lastTimeStamp = AnimatedFileDrawable.this.metaData[3];
            if (AnimatedFileDrawable.this.secondParentView != null) {
                AnimatedFileDrawable.this.secondParentView.invalidate();
            } else if (AnimatedFileDrawable.this.parentView != null) {
                AnimatedFileDrawable.this.parentView.invalidate();
            }
            AnimatedFileDrawable.this.scheduleNextGetFrame();
        }
    };

    private static native long createDecoder(String str, int[] iArr);

    private static native void destroyDecoder(long j);

    private static native int getVideoFrame(long j, Bitmap bitmap, int[] iArr);

    public void draw(android.graphics.Canvas r1) {
        /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.DecodeException: Load method exception in method: org.telegram.ui.Components.AnimatedFileDrawable.draw(android.graphics.Canvas):void
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
        r0 = r11.nativePtr;
        r2 = 0;
        r4 = (r0 > r2 ? 1 : (r0 == r2 ? 0 : -1));
        if (r4 != 0) goto L_0x000c;
    L_0x0008:
        r0 = r11.decoderCreated;
        if (r0 != 0) goto L_0x0010;
    L_0x000c:
        r0 = r11.destroyWhenDone;
        if (r0 == 0) goto L_0x0011;
    L_0x0010:
        return;
    L_0x0011:
        r0 = java.lang.System.currentTimeMillis();
        r2 = r11.isRunning;
        r3 = 0;
        if (r2 == 0) goto L_0x0048;
    L_0x001a:
        r2 = r11.renderingBitmap;
        if (r2 != 0) goto L_0x0026;
    L_0x001e:
        r2 = r11.nextRenderingBitmap;
        if (r2 != 0) goto L_0x0026;
    L_0x0022:
        r11.scheduleNextGetFrame();
        goto L_0x0071;
    L_0x0026:
        r4 = r11.lastFrameTime;
        r6 = r0 - r4;
        r4 = java.lang.Math.abs(r6);
        r2 = r11.invalidateAfter;
        r6 = (long) r2;
        r2 = (r4 > r6 ? 1 : (r4 == r6 ? 0 : -1));
        if (r2 < 0) goto L_0x0071;
    L_0x0035:
        r2 = r11.nextRenderingBitmap;
        if (r2 == 0) goto L_0x0071;
    L_0x0039:
        r2 = r11.nextRenderingBitmap;
        r11.renderingBitmap = r2;
        r2 = r11.nextRenderingShader;
        r11.renderingShader = r2;
        r11.nextRenderingBitmap = r3;
        r11.nextRenderingShader = r3;
        r11.lastFrameTime = r0;
        goto L_0x0071;
    L_0x0048:
        r2 = r11.isRunning;
        if (r2 != 0) goto L_0x0071;
    L_0x004c:
        r2 = r11.decodeSingleFrame;
        if (r2 == 0) goto L_0x0071;
    L_0x0050:
        r4 = r11.lastFrameTime;
        r6 = r0 - r4;
        r4 = java.lang.Math.abs(r6);
        r2 = r11.invalidateAfter;
        r6 = (long) r2;
        r2 = (r4 > r6 ? 1 : (r4 == r6 ? 0 : -1));
        if (r2 < 0) goto L_0x0071;
    L_0x005f:
        r2 = r11.nextRenderingBitmap;
        if (r2 == 0) goto L_0x0071;
    L_0x0063:
        r2 = r11.nextRenderingBitmap;
        r11.renderingBitmap = r2;
        r2 = r11.nextRenderingShader;
        r11.renderingShader = r2;
        r11.nextRenderingBitmap = r3;
        r11.nextRenderingShader = r3;
        r11.lastFrameTime = r0;
    L_0x0071:
        r2 = r11.renderingBitmap;
        if (r2 == 0) goto L_0x023e;
    L_0x0075:
        r2 = r11.applyTransformation;
        r3 = 270; // 0x10e float:3.78E-43 double:1.334E-321;
        r4 = 90;
        r5 = 2;
        if (r2 == 0) goto L_0x00bb;
    L_0x007e:
        r2 = r11.renderingBitmap;
        r2 = r2.getWidth();
        r6 = r11.renderingBitmap;
        r6 = r6.getHeight();
        r7 = r11.metaData;
        r7 = r7[r5];
        if (r7 == r4) goto L_0x0096;
    L_0x0090:
        r7 = r11.metaData;
        r7 = r7[r5];
        if (r7 != r3) goto L_0x0099;
    L_0x0096:
        r7 = r2;
        r2 = r6;
        r6 = r7;
    L_0x0099:
        r7 = r11.dstRect;
        r8 = r11.getBounds();
        r7.set(r8);
        r7 = r11.dstRect;
        r7 = r7.width();
        r7 = (float) r7;
        r8 = (float) r2;
        r7 = r7 / r8;
        r11.scaleX = r7;
        r7 = r11.dstRect;
        r7 = r7.height();
        r7 = (float) r7;
        r8 = (float) r6;
        r7 = r7 / r8;
        r11.scaleY = r7;
        r7 = 0;
        r11.applyTransformation = r7;
    L_0x00bb:
        r2 = r11.roundRadius;
        r6 = 0;
        if (r2 == 0) goto L_0x01a7;
    L_0x00c0:
        r2 = r11.scaleX;
        r7 = r11.scaleY;
        r2 = java.lang.Math.max(r2, r7);
        r7 = r11.renderingShader;
        if (r7 != 0) goto L_0x00d9;
    L_0x00cc:
        r7 = new android.graphics.BitmapShader;
        r8 = r11.backgroundBitmap;
        r9 = android.graphics.Shader.TileMode.CLAMP;
        r10 = android.graphics.Shader.TileMode.CLAMP;
        r7.<init>(r8, r9, r10);
        r11.renderingShader = r7;
    L_0x00d9:
        r7 = r11.getPaint();
        r8 = r11.renderingShader;
        r7.setShader(r8);
        r7 = r11.roundRect;
        r8 = r11.dstRect;
        r7.set(r8);
        r7 = r11.shaderMatrix;
        r7.reset();
        r7 = r11.scaleX;
        r8 = r11.scaleY;
        r7 = r7 - r8;
        r7 = java.lang.Math.abs(r7);
        r8 = 925353388; // 0x3727c5ac float:1.0E-5 double:4.571853193E-315;
        r7 = (r7 > r8 ? 1 : (r7 == r8 ? 0 : -1));
        if (r7 <= 0) goto L_0x016e;
    L_0x00fe:
        r6 = r11.metaData;
        r6 = r6[r5];
        if (r6 == r4) goto L_0x0128;
    L_0x0104:
        r4 = r11.metaData;
        r4 = r4[r5];
        if (r4 != r3) goto L_0x010b;
    L_0x010a:
        goto L_0x0128;
    L_0x010b:
        r3 = r11.dstRect;
        r3 = r3.width();
        r3 = (float) r3;
        r3 = r3 / r2;
        r3 = (double) r3;
        r3 = java.lang.Math.floor(r3);
        r3 = (int) r3;
        r4 = r11.dstRect;
        r4 = r4.height();
        r4 = (float) r4;
        r4 = r4 / r2;
        r6 = (double) r4;
        r6 = java.lang.Math.floor(r6);
        r4 = (int) r6;
        goto L_0x0144;
    L_0x0128:
        r3 = r11.dstRect;
        r3 = r3.height();
        r3 = (float) r3;
        r3 = r3 / r2;
        r3 = (double) r3;
        r3 = java.lang.Math.floor(r3);
        r3 = (int) r3;
        r4 = r11.dstRect;
        r4 = r4.width();
        r4 = (float) r4;
        r4 = r4 / r2;
        r6 = (double) r4;
        r6 = java.lang.Math.floor(r6);
        r4 = (int) r6;
        r6 = r11.bitmapRect;
        r7 = r11.renderingBitmap;
        r7 = r7.getWidth();
        r7 = r7 - r3;
        r7 = r7 / r5;
        r7 = (float) r7;
        r8 = r11.renderingBitmap;
        r8 = r8.getHeight();
        r8 = r8 - r4;
        r8 = r8 / r5;
        r8 = (float) r8;
        r9 = (float) r3;
        r10 = (float) r4;
        r6.set(r7, r8, r9, r10);
        r6 = r11.shaderMatrix;
        r7 = r11.bitmapRect;
        r8 = r11.roundRect;
        r9 = r11.metaData;
        r5 = r9[r5];
        r9 = android.graphics.Matrix.ScaleToFit.START;
        org.telegram.messenger.AndroidUtilities.setRectToRect(r6, r7, r8, r5, r9);
        goto L_0x0190;
    L_0x016e:
        r3 = r11.bitmapRect;
        r4 = r11.renderingBitmap;
        r4 = r4.getWidth();
        r4 = (float) r4;
        r7 = r11.renderingBitmap;
        r7 = r7.getHeight();
        r7 = (float) r7;
        r3.set(r6, r6, r4, r7);
        r3 = r11.shaderMatrix;
        r4 = r11.bitmapRect;
        r6 = r11.roundRect;
        r7 = r11.metaData;
        r5 = r7[r5];
        r7 = android.graphics.Matrix.ScaleToFit.FILL;
        org.telegram.messenger.AndroidUtilities.setRectToRect(r3, r4, r6, r5, r7);
        r3 = r11.renderingShader;
        r4 = r11.shaderMatrix;
        r3.setLocalMatrix(r4);
        r3 = r11.actualDrawRect;
        r4 = r11.roundRadius;
        r4 = (float) r4;
        r5 = r11.roundRadius;
        r5 = (float) r5;
        r6 = r11.getPaint();
        r12.drawRoundRect(r3, r4, r5, r6);
        goto L_0x0212;
    L_0x01a7:
        r2 = r11.dstRect;
        r2 = r2.left;
        r2 = (float) r2;
        r7 = r11.dstRect;
        r7 = r7.top;
        r7 = (float) r7;
        r12.translate(r2, r7);
        r2 = r11.metaData;
        r2 = r2[r5];
        if (r2 != r4) goto L_0x01cb;
        r2 = 1119092736; // 0x42b40000 float:90.0 double:5.529052754E-315;
        r12.rotate(r2);
        r2 = r11.dstRect;
        r2 = r2.width();
        r2 = -r2;
        r2 = (float) r2;
        r12.translate(r6, r2);
        goto L_0x0202;
        r2 = r11.metaData;
        r2 = r2[r5];
        r4 = 180; // 0xb4 float:2.52E-43 double:8.9E-322;
        if (r2 != r4) goto L_0x01ec;
        r2 = 1127481344; // 0x43340000 float:180.0 double:5.570497984E-315;
        r12.rotate(r2);
        r2 = r11.dstRect;
        r2 = r2.width();
        r2 = -r2;
        r2 = (float) r2;
        r3 = r11.dstRect;
        r3 = r3.height();
        r3 = -r3;
        r3 = (float) r3;
        r12.translate(r2, r3);
        goto L_0x0202;
        r2 = r11.metaData;
        r2 = r2[r5];
        if (r2 != r3) goto L_0x0202;
        r2 = 1132920832; // 0x43870000 float:270.0 double:5.597372625E-315;
        r12.rotate(r2);
        r2 = r11.dstRect;
        r2 = r2.height();
        r2 = -r2;
        r2 = (float) r2;
        r12.translate(r2, r6);
        r2 = r11.scaleX;
        r3 = r11.scaleY;
        r12.scale(r2, r3);
        r2 = r11.renderingBitmap;
        r3 = r11.getPaint();
        r12.drawBitmap(r2, r6, r6, r3);
        r2 = r11.isRunning;
        if (r2 == 0) goto L_0x023e;
        r2 = 1;
        r4 = r11.invalidateAfter;
        r4 = (long) r4;
        r6 = r11.lastFrameTime;
        r8 = r0 - r6;
        r6 = r4 - r8;
        r4 = 17;
        r8 = r6 - r4;
        r2 = java.lang.Math.max(r2, r8);
        r4 = uiHandler;
        r5 = r11.mInvalidateTask;
        r4.removeCallbacks(r5);
        r4 = uiHandler;
        r5 = r11.mInvalidateTask;
        r6 = r11.invalidateAfter;
        r6 = (long) r6;
        r6 = java.lang.Math.min(r2, r6);
        r4.postDelayed(r5, r6);
    L_0x023e:
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.telegram.ui.Components.AnimatedFileDrawable.draw(android.graphics.Canvas):void");
    }

    public AnimatedFileDrawable(File file, boolean createDecoder) {
        this.path = file;
        if (createDecoder) {
            this.nativePtr = createDecoder(file.getAbsolutePath(), this.metaData);
            this.decoderCreated = true;
        }
    }

    public void setParentView(View view) {
        this.parentView = view;
    }

    public void setSecondParentView(View view) {
        this.secondParentView = view;
        if (view == null && this.recycleWithSecond) {
            recycle();
        }
    }

    public void setAllowDecodeSingleFrame(boolean value) {
        this.decodeSingleFrame = value;
        if (this.decodeSingleFrame) {
            scheduleNextGetFrame();
        }
    }

    public void recycle() {
        if (this.secondParentView != null) {
            this.recycleWithSecond = true;
            return;
        }
        this.isRunning = false;
        this.isRecycled = true;
        if (this.loadFrameTask == null) {
            if (this.nativePtr != 0) {
                destroyDecoder(this.nativePtr);
                this.nativePtr = 0;
            }
            if (this.renderingBitmap != null) {
                this.renderingBitmap.recycle();
                this.renderingBitmap = null;
            }
            if (this.nextRenderingBitmap != null) {
                this.nextRenderingBitmap.recycle();
                this.nextRenderingBitmap = null;
            }
        } else {
            this.destroyWhenDone = true;
        }
    }

    protected static void runOnUiThread(Runnable task) {
        if (Looper.myLooper() == uiHandler.getLooper()) {
            task.run();
        } else {
            uiHandler.post(task);
        }
    }

    protected void finalize() throws Throwable {
        try {
            recycle();
        } finally {
            super.finalize();
        }
    }

    public int getOpacity() {
        return -2;
    }

    public void start() {
        if (!this.isRunning) {
            this.isRunning = true;
            scheduleNextGetFrame();
            runOnUiThread(this.mStartTask);
        }
    }

    private void scheduleNextGetFrame() {
        if (this.loadFrameTask == null && !((this.nativePtr == 0 && this.decoderCreated) || this.destroyWhenDone)) {
            if (!this.isRunning) {
                if (this.decodeSingleFrame) {
                    if (this.decodeSingleFrame && this.singleFrameDecoded) {
                    }
                }
            }
            long ms = 0;
            if (this.lastFrameDecodeTime != 0) {
                ms = Math.min((long) this.invalidateAfter, Math.max(0, ((long) this.invalidateAfter) - (System.currentTimeMillis() - this.lastFrameDecodeTime)));
            }
            ScheduledThreadPoolExecutor scheduledThreadPoolExecutor = executor;
            Runnable runnable = this.loadFrameRunnable;
            this.loadFrameTask = runnable;
            scheduledThreadPoolExecutor.schedule(runnable, ms, TimeUnit.MILLISECONDS);
        }
    }

    public void stop() {
        this.isRunning = false;
    }

    public boolean isRunning() {
        return this.isRunning;
    }

    public int getIntrinsicHeight() {
        if (!this.decoderCreated) {
            return AndroidUtilities.dp(100.0f);
        }
        if (this.metaData[2] != 90) {
            if (this.metaData[2] != 270) {
                return this.metaData[1];
            }
        }
        return this.metaData[0];
    }

    public int getIntrinsicWidth() {
        if (!this.decoderCreated) {
            return AndroidUtilities.dp(100.0f);
        }
        if (this.metaData[2] != 90) {
            if (this.metaData[2] != 270) {
                return this.metaData[0];
            }
        }
        return this.metaData[1];
    }

    protected void onBoundsChange(Rect bounds) {
        super.onBoundsChange(bounds);
        this.applyTransformation = true;
    }

    public int getMinimumHeight() {
        if (!this.decoderCreated) {
            return AndroidUtilities.dp(100.0f);
        }
        if (this.metaData[2] != 90) {
            if (this.metaData[2] != 270) {
                return this.metaData[1];
            }
        }
        return this.metaData[0];
    }

    public int getMinimumWidth() {
        if (!this.decoderCreated) {
            return AndroidUtilities.dp(100.0f);
        }
        if (this.metaData[2] != 90) {
            if (this.metaData[2] != 270) {
                return this.metaData[0];
            }
        }
        return this.metaData[1];
    }

    public Bitmap getAnimatedBitmap() {
        if (this.renderingBitmap != null) {
            return this.renderingBitmap;
        }
        if (this.nextRenderingBitmap != null) {
            return this.nextRenderingBitmap;
        }
        return null;
    }

    public void setActualDrawRect(int x, int y, int width, int height) {
        this.actualDrawRect.set((float) x, (float) y, (float) (x + width), (float) (y + height));
    }

    public void setRoundRadius(int value) {
        this.roundRadius = value;
        getPaint().setFlags(1);
    }

    public boolean hasBitmap() {
        return (this.nativePtr == 0 || (this.renderingBitmap == null && this.nextRenderingBitmap == null)) ? false : true;
    }

    public int getOrientation() {
        return this.metaData[2];
    }

    public AnimatedFileDrawable makeCopy() {
        AnimatedFileDrawable drawable = new AnimatedFileDrawable(this.path, false);
        drawable.metaData[0] = this.metaData[0];
        drawable.metaData[1] = this.metaData[1];
        return drawable;
    }
}
