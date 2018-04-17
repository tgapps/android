package org.telegram.ui;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Cap;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.SurfaceTexture;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build.VERSION;
import android.support.annotation.Keep;
import android.util.SparseArray;
import android.view.GestureDetector;
import android.view.GestureDetector.OnDoubleTapListener;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;
import android.view.TextureView;
import android.view.VelocityTracker;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.View.OnApplyWindowInsetsListener;
import android.view.WindowInsets;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;
import java.io.File;
import java.util.ArrayList;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.FileLoader;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.ImageReceiver;
import org.telegram.messenger.ImageReceiver.BitmapHolder;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MessageObject;
import org.telegram.messenger.NotificationCenter;
import org.telegram.messenger.NotificationCenter.NotificationCenterDelegate;
import org.telegram.messenger.UserConfig;
import org.telegram.messenger.beta.R;
import org.telegram.messenger.exoplayer2.ui.AspectRatioFrameLayout;
import org.telegram.messenger.support.widget.helper.ItemTouchHelper.Callback;
import org.telegram.tgnet.ConnectionsManager;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC.Document;
import org.telegram.ui.ActionBar.ActionBar;
import org.telegram.ui.ActionBar.ActionBar.ActionBarMenuOnItemClick;
import org.telegram.ui.ActionBar.DrawerLayoutContainer;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.ui.Components.Scroller;
import org.telegram.ui.Components.VideoPlayer;
import org.telegram.ui.Components.VideoPlayer.VideoPlayerDelegate;
import org.telegram.ui.PhotoViewer.PhotoViewerProvider;
import org.telegram.ui.PhotoViewer.PlaceProviderObject;

public class SecretMediaViewer implements OnDoubleTapListener, OnGestureListener, NotificationCenterDelegate {
    @SuppressLint({"StaticFieldLeak"})
    private static volatile SecretMediaViewer Instance = null;
    private ActionBar actionBar;
    private float animateToClipBottom;
    private float animateToClipHorizontal;
    private float animateToClipTop;
    private float animateToScale;
    private float animateToX;
    private float animateToY;
    private long animationStartTime;
    private float animationValue;
    private AspectRatioFrameLayout aspectRatioFrameLayout;
    private Paint blackPaint = new Paint();
    private boolean canDragDown = true;
    private ImageReceiver centerImage = new ImageReceiver();
    private float clipBottom;
    private float clipHorizontal;
    private float clipTop;
    private long closeTime;
    private boolean closeVideoAfterWatch;
    private FrameLayoutDrawer containerView;
    private int[] coords = new int[2];
    private int currentAccount;
    private AnimatorSet currentActionBarAnimation;
    private int currentChannelId;
    private MessageObject currentMessageObject;
    private PhotoViewerProvider currentProvider;
    private int currentRotation;
    private BitmapHolder currentThumb;
    private boolean disableShowCheck;
    private boolean discardTap;
    private boolean doubleTap;
    private float dragY;
    private boolean draggingDown;
    private GestureDetector gestureDetector;
    private AnimatorSet imageMoveAnimation;
    private DecelerateInterpolator interpolator = new DecelerateInterpolator(1.5f);
    private boolean invalidCoords;
    private boolean isActionBarVisible = true;
    private boolean isPhotoVisible;
    private boolean isPlaying;
    private boolean isVideo;
    private boolean isVisible;
    private Object lastInsets;
    private float maxX;
    private float maxY;
    private float minX;
    private float minY;
    private float moveStartX;
    private float moveStartY;
    private boolean moving;
    private long openTime;
    private Activity parentActivity;
    private Runnable photoAnimationEndRunnable;
    private int photoAnimationInProgress;
    private PhotoBackgroundDrawable photoBackgroundDrawable = new PhotoBackgroundDrawable(Theme.ACTION_BAR_VIDEO_EDIT_COLOR);
    private long photoTransitionAnimationStartTime;
    private float pinchCenterX;
    private float pinchCenterY;
    private float pinchStartDistance;
    private float pinchStartScale = 1.0f;
    private float pinchStartX;
    private float pinchStartY;
    private float scale = 1.0f;
    private Scroller scroller;
    private SecretDeleteTimer secretDeleteTimer;
    private boolean textureUploaded;
    private float translationX;
    private float translationY;
    private boolean useOvershootForScale;
    private VelocityTracker velocityTracker;
    private float videoCrossfadeAlpha;
    private long videoCrossfadeAlphaLastTime;
    private boolean videoCrossfadeStarted;
    private VideoPlayer videoPlayer;
    private TextureView videoTextureView;
    private boolean videoWatchedOneTime;
    private LayoutParams windowLayoutParams;
    private FrameLayout windowView;
    private boolean zoomAnimation;
    private boolean zooming;

    class AnonymousClass10 implements Runnable {
        final /* synthetic */ PlaceProviderObject val$object;

        AnonymousClass10(PlaceProviderObject placeProviderObject) {
            this.val$object = placeProviderObject;
        }

        public void run() {
            SecretMediaViewer.this.imageMoveAnimation = null;
            SecretMediaViewer.this.photoAnimationInProgress = 0;
            if (VERSION.SDK_INT >= 18) {
                SecretMediaViewer.this.containerView.setLayerType(0, null);
            }
            SecretMediaViewer.this.containerView.setVisibility(4);
            SecretMediaViewer.this.onPhotoClosed(this.val$object);
        }
    }

    class AnonymousClass11 extends AnimatorListenerAdapter {
        final /* synthetic */ PlaceProviderObject val$object;

        AnonymousClass11(PlaceProviderObject placeProviderObject) {
            this.val$object = placeProviderObject;
        }

        public void onAnimationEnd(Animator animation) {
            if (this.val$object != null) {
                this.val$object.imageReceiver.setVisible(true, true);
            }
            SecretMediaViewer.this.isVisible = false;
            AndroidUtilities.runOnUIThread(new Runnable() {
                public void run() {
                    if (SecretMediaViewer.this.photoAnimationEndRunnable != null) {
                        SecretMediaViewer.this.photoAnimationEndRunnable.run();
                        SecretMediaViewer.this.photoAnimationEndRunnable = null;
                    }
                }
            });
        }
    }

    class AnonymousClass12 implements Runnable {
        final /* synthetic */ PlaceProviderObject val$object;

        AnonymousClass12(PlaceProviderObject placeProviderObject) {
            this.val$object = placeProviderObject;
        }

        public void run() {
            if (SecretMediaViewer.this.containerView != null) {
                if (VERSION.SDK_INT >= 18) {
                    SecretMediaViewer.this.containerView.setLayerType(0, null);
                }
                SecretMediaViewer.this.containerView.setVisibility(4);
                SecretMediaViewer.this.photoAnimationInProgress = 0;
                SecretMediaViewer.this.onPhotoClosed(this.val$object);
                SecretMediaViewer.this.containerView.setScaleX(1.0f);
                SecretMediaViewer.this.containerView.setScaleY(1.0f);
            }
        }
    }

    private class FrameLayoutDrawer extends FrameLayout {
        public FrameLayoutDrawer(Context context) {
            super(context);
            setWillNotDraw(null);
        }

        public boolean onTouchEvent(MotionEvent event) {
            SecretMediaViewer.this.processTouchEvent(event);
            return true;
        }

        protected void onDraw(Canvas canvas) {
            SecretMediaViewer.this.onDraw(canvas);
        }

        protected boolean drawChild(Canvas canvas, View child, long drawingTime) {
            return child != SecretMediaViewer.this.aspectRatioFrameLayout && super.drawChild(canvas, child, drawingTime);
        }
    }

    private class PhotoBackgroundDrawable extends ColorDrawable {
        private Runnable drawRunnable;
        private int frame;

        public PhotoBackgroundDrawable(int color) {
            super(color);
        }

        @Keep
        public void setAlpha(int alpha) {
            if (SecretMediaViewer.this.parentActivity instanceof LaunchActivity) {
                boolean z;
                DrawerLayoutContainer drawerLayoutContainer = ((LaunchActivity) SecretMediaViewer.this.parentActivity).drawerLayoutContainer;
                if (SecretMediaViewer.this.isPhotoVisible) {
                    if (alpha == 255) {
                        z = false;
                        drawerLayoutContainer.setAllowDrawContent(z);
                    }
                }
                z = true;
                drawerLayoutContainer.setAllowDrawContent(z);
            }
            super.setAlpha(alpha);
        }

        public void draw(Canvas canvas) {
            super.draw(canvas);
            if (getAlpha() != 0) {
                if (this.frame != 2 || this.drawRunnable == null) {
                    invalidateSelf();
                } else {
                    this.drawRunnable.run();
                    this.drawRunnable = null;
                }
                this.frame++;
            }
        }
    }

    private class SecretDeleteTimer extends FrameLayout {
        private Paint afterDeleteProgressPaint;
        private Paint circlePaint;
        private Paint deleteProgressPaint;
        private RectF deleteProgressRect = new RectF();
        private long destroyTime;
        private long destroyTtl;
        private Drawable drawable;
        private ArrayList<Particle> freeParticles = new ArrayList();
        private long lastAnimationTime;
        private Paint particlePaint;
        private ArrayList<Particle> particles = new ArrayList();
        private boolean useVideoProgress;

        private class Particle {
            float alpha;
            float currentTime;
            float lifeTime;
            float velocity;
            float vx;
            float vy;
            float x;
            float y;

            private Particle() {
            }
        }

        @android.annotation.SuppressLint({"DrawAllocation"})
        protected void onDraw(android.graphics.Canvas r1) {
            /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.DecodeException: Load method exception in method: org.telegram.ui.SecretMediaViewer.SecretDeleteTimer.onDraw(android.graphics.Canvas):void
	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:116)
	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:249)
	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:256)
	at jadx.core.ProcessClass.process(ProcessClass.java:34)
	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:59)
	at jadx.core.ProcessClass.process(ProcessClass.java:42)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:306)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler$1.run(JadxDecompiler.java:199)
Caused by: java.lang.NullPointerException
*/
            /*
            r0 = this;
            r0 = r28;
            r7 = r29;
            r1 = org.telegram.ui.SecretMediaViewer.this;
            r1 = r1.currentMessageObject;
            if (r1 == 0) goto L_0x01f6;
        L_0x000c:
            r1 = org.telegram.ui.SecretMediaViewer.this;
            r1 = r1.currentMessageObject;
            r1 = r1.messageOwner;
            r1 = r1.destroyTime;
            if (r1 != 0) goto L_0x001a;
        L_0x0018:
            goto L_0x01f6;
        L_0x001a:
            r1 = r28.getMeasuredWidth();
            r2 = 1108082688; // 0x420c0000 float:35.0 double:5.47465589E-315;
            r2 = org.telegram.messenger.AndroidUtilities.dp(r2);
            r1 = r1 - r2;
            r1 = (float) r1;
            r2 = r28.getMeasuredHeight();
            r2 = r2 / 2;
            r2 = (float) r2;
            r3 = 1098907648; // 0x41800000 float:16.0 double:5.42932517E-315;
            r3 = org.telegram.messenger.AndroidUtilities.dp(r3);
            r3 = (float) r3;
            r4 = r0.circlePaint;
            r7.drawCircle(r1, r2, r3, r4);
            r1 = r0.useVideoProgress;
            r8 = 1065353216; // 0x3f800000 float:1.0 double:5.263544247E-315;
            if (r1 == 0) goto L_0x0075;
        L_0x003f:
            r1 = org.telegram.ui.SecretMediaViewer.this;
            r1 = r1.videoPlayer;
            if (r1 == 0) goto L_0x0072;
        L_0x0047:
            r1 = org.telegram.ui.SecretMediaViewer.this;
            r1 = r1.videoPlayer;
            r1 = r1.getDuration();
            r3 = org.telegram.ui.SecretMediaViewer.this;
            r3 = r3.videoPlayer;
            r3 = r3.getCurrentPosition();
            r5 = -9223372036854775807; // 0x8000000000000001 float:1.4E-45 double:-4.9E-324;
            r9 = (r1 > r5 ? 1 : (r1 == r5 ? 0 : -1));
            if (r9 == 0) goto L_0x006e;
        L_0x0064:
            r9 = (r3 > r5 ? 1 : (r3 == r5 ? 0 : -1));
            if (r9 == 0) goto L_0x006e;
            r5 = (float) r3;
            r6 = (float) r1;
            r5 = r5 / r6;
            r5 = r8 - r5;
            goto L_0x0070;
            r5 = r8;
            r1 = r5;
            goto L_0x009e;
        L_0x0072:
            r1 = 1065353216; // 0x3f800000 float:1.0 double:5.263544247E-315;
            goto L_0x009e;
        L_0x0075:
            r1 = java.lang.System.currentTimeMillis();
            r3 = org.telegram.ui.SecretMediaViewer.this;
            r3 = r3.currentAccount;
            r3 = org.telegram.tgnet.ConnectionsManager.getInstance(r3);
            r3 = r3.getTimeDifference();
            r3 = r3 * 1000;
            r3 = (long) r3;
            r5 = r1 + r3;
            r1 = 0;
            r3 = r0.destroyTime;
            r9 = r3 - r5;
            r1 = java.lang.Math.max(r1, r9);
            r1 = (float) r1;
            r2 = r0.destroyTtl;
            r2 = (float) r2;
            r3 = 1148846080; // 0x447a0000 float:1000.0 double:5.676053805E-315;
            r2 = r2 * r3;
            r1 = r1 / r2;
            r9 = r1;
            r1 = r28.getMeasuredWidth();
            r2 = 1109393408; // 0x42200000 float:40.0 double:5.481131706E-315;
            r2 = org.telegram.messenger.AndroidUtilities.dp(r2);
            r10 = r1 - r2;
            r1 = r28.getMeasuredHeight();
            r11 = 1096810496; // 0x41600000 float:14.0 double:5.41896386E-315;
            r2 = org.telegram.messenger.AndroidUtilities.dp(r11);
            r1 = r1 - r2;
            r1 = r1 / 2;
            r2 = 1056964608; // 0x3f000000 float:0.5 double:5.222099017E-315;
            r2 = org.telegram.messenger.AndroidUtilities.dp(r2);
            r12 = r1 - r2;
            r1 = r0.drawable;
            r2 = 1092616192; // 0x41200000 float:10.0 double:5.398241246E-315;
            r2 = org.telegram.messenger.AndroidUtilities.dp(r2);
            r2 = r2 + r10;
            r3 = org.telegram.messenger.AndroidUtilities.dp(r11);
            r3 = r3 + r12;
            r1.setBounds(r10, r12, r2, r3);
            r1 = r0.drawable;
            r1.draw(r7);
            r1 = -1011613696; // 0xffffffffc3b40000 float:-360.0 double:NaN;
            r13 = r1 * r9;
            r2 = r0.deleteProgressRect;
            r3 = -1028390912; // 0xffffffffc2b40000 float:-90.0 double:NaN;
            r5 = 0;
            r6 = r0.afterDeleteProgressPaint;
            r1 = r7;
            r4 = r13;
            r1.drawArc(r2, r3, r4, r5, r6);
            r1 = r0.particles;
            r1 = r1.size();
            r3 = 0;
            if (r3 >= r1) goto L_0x010e;
            r4 = r0.particles;
            r4 = r4.get(r3);
            r4 = (org.telegram.ui.SecretMediaViewer.SecretDeleteTimer.Particle) r4;
            r5 = r0.particlePaint;
            r6 = 1132396544; // 0x437f0000 float:255.0 double:5.5947823E-315;
            r14 = r4.alpha;
            r6 = r6 * r14;
            r6 = (int) r6;
            r5.setAlpha(r6);
            r5 = r4.x;
            r6 = r4.y;
            r14 = r0.particlePaint;
            r7.drawPoint(r5, r6, r14);
            r3 = r3 + 1;
            goto L_0x00ed;
            r3 = 1119092736; // 0x42b40000 float:90.0 double:5.529052754E-315;
            r4 = r13 - r3;
            r4 = (double) r4;
            r14 = 4580687790476533049; // 0x3f91df46a2529d39 float:-2.854354E-18 double:0.017453292519943295;
            r4 = r4 * r14;
            r4 = java.lang.Math.sin(r4);
            r3 = r13 - r3;
            r16 = r9;
            r8 = (double) r3;
            r8 = r8 * r14;
            r8 = java.lang.Math.cos(r8);
            r8 = -r8;
            r3 = org.telegram.messenger.AndroidUtilities.dp(r11);
            r14 = -r8;
            r6 = (double) r3;
            r14 = r14 * r6;
            r6 = r0.deleteProgressRect;
            r6 = r6.centerX();
            r6 = (double) r6;
            r14 = r14 + r6;
            r6 = (float) r14;
            r14 = (double) r3;
            r14 = r14 * r4;
            r7 = r0.deleteProgressRect;
            r7 = r7.centerY();
            r20 = r3;
            r2 = (double) r7;
            r14 = r14 + r2;
            r2 = (float) r14;
            r3 = 0;
            r7 = 1;
            if (r3 >= r7) goto L_0x01df;
            r7 = r0.freeParticles;
            r7 = r7.isEmpty();
            if (r7 != 0) goto L_0x0160;
            r7 = r0.freeParticles;
            r11 = 0;
            r7 = r7.get(r11);
            r7 = (org.telegram.ui.SecretMediaViewer.SecretDeleteTimer.Particle) r7;
            r14 = r0.freeParticles;
            r14.remove(r11);
            goto L_0x0167;
            r11 = 0;
            r7 = new org.telegram.ui.SecretMediaViewer$SecretDeleteTimer$Particle;
            r14 = 0;
            r7.<init>();
            r7.x = r6;
            r7.y = r2;
            r14 = org.telegram.messenger.Utilities.random;
            r15 = 140; // 0x8c float:1.96E-43 double:6.9E-322;
            r14 = r14.nextInt(r15);
            r14 = r14 + -70;
            r14 = (double) r14;
            r17 = 4580687790476533049; // 0x3f91df46a2529d39 float:-2.854354E-18 double:0.017453292519943295;
            r14 = r14 * r17;
            r21 = 0;
            r19 = (r14 > r21 ? 1 : (r14 == r21 ? 0 : -1));
            if (r19 >= 0) goto L_0x018a;
            r21 = 4618760256179416344; // 0x401921fb54442d18 float:3.37028055E12 double:6.283185307179586;
            r14 = r21 + r14;
            r21 = java.lang.Math.cos(r14);
            r21 = r21 * r4;
            r23 = java.lang.Math.sin(r14);
            r23 = r23 * r8;
            r25 = r12;
            r11 = r21 - r23;
            r11 = (float) r11;
            r7.vx = r11;
            r11 = java.lang.Math.sin(r14);
            r11 = r11 * r4;
            r21 = java.lang.Math.cos(r14);
            r21 = r21 * r8;
            r11 = r11 + r21;
            r11 = (float) r11;
            r7.vy = r11;
            r11 = 1065353216; // 0x3f800000 float:1.0 double:5.263544247E-315;
            r7.alpha = r11;
            r12 = 0;
            r7.currentTime = r12;
            r12 = 400; // 0x190 float:5.6E-43 double:1.976E-321;
            r11 = org.telegram.messenger.Utilities.random;
            r26 = r1;
            r1 = 100;
            r1 = r11.nextInt(r1);
            r12 = r12 + r1;
            r1 = (float) r12;
            r7.lifeTime = r1;
            r1 = 1101004800; // 0x41a00000 float:20.0 double:5.439686476E-315;
            r11 = org.telegram.messenger.Utilities.random;
            r11 = r11.nextFloat();
            r12 = 1082130432; // 0x40800000 float:4.0 double:5.34643471E-315;
            r11 = r11 * r12;
            r1 = r1 + r11;
            r7.velocity = r1;
            r1 = r0.particles;
            r1.add(r7);
            r3 = r3 + 1;
            r12 = r25;
            r1 = r26;
            goto L_0x0146;
            r26 = r1;
            r25 = r12;
            r11 = java.lang.System.currentTimeMillis();
            r14 = r0.lastAnimationTime;
            r27 = r2;
            r1 = r11 - r14;
            r0.updateParticles(r1);
            r0.lastAnimationTime = r11;
            r28.invalidate();
            return;
        L_0x01f6:
            return;
            */
            throw new UnsupportedOperationException("Method not decompiled: org.telegram.ui.SecretMediaViewer.SecretDeleteTimer.onDraw(android.graphics.Canvas):void");
        }

        public SecretDeleteTimer(Context context) {
            super(context);
            int a = 0;
            setWillNotDraw(false);
            this.particlePaint = new Paint(1);
            this.particlePaint.setStrokeWidth((float) AndroidUtilities.dp(1.5f));
            this.particlePaint.setColor(-1644826);
            this.particlePaint.setStrokeCap(Cap.ROUND);
            this.particlePaint.setStyle(Style.STROKE);
            this.deleteProgressPaint = new Paint(1);
            this.deleteProgressPaint.setColor(-1644826);
            this.afterDeleteProgressPaint = new Paint(1);
            this.afterDeleteProgressPaint.setStyle(Style.STROKE);
            this.afterDeleteProgressPaint.setStrokeCap(Cap.ROUND);
            this.afterDeleteProgressPaint.setColor(-1644826);
            this.afterDeleteProgressPaint.setStrokeWidth((float) AndroidUtilities.dp(2.0f));
            this.circlePaint = new Paint(1);
            this.circlePaint.setColor(Theme.ACTION_BAR_PHOTO_VIEWER_COLOR);
            this.drawable = context.getResources().getDrawable(R.drawable.flame_small);
            while (a < 40) {
                this.freeParticles.add(new Particle());
                a++;
            }
        }

        private void setDestroyTime(long time, long ttl, boolean videoProgress) {
            this.destroyTime = time;
            this.destroyTtl = ttl;
            this.useVideoProgress = videoProgress;
            this.lastAnimationTime = System.currentTimeMillis();
            invalidate();
        }

        private void updateParticles(long dt) {
            int count = this.particles.size();
            int a = 0;
            while (a < count) {
                Particle particle = (Particle) this.particles.get(a);
                if (particle.currentTime >= particle.lifeTime) {
                    if (this.freeParticles.size() < 40) {
                        this.freeParticles.add(particle);
                    }
                    this.particles.remove(a);
                    a--;
                    count--;
                } else {
                    particle.alpha = 1.0f - AndroidUtilities.decelerateInterpolator.getInterpolation(particle.currentTime / particle.lifeTime);
                    particle.x += ((particle.vx * particle.velocity) * ((float) dt)) / 500.0f;
                    particle.y += ((particle.vy * particle.velocity) * ((float) dt)) / 500.0f;
                    particle.currentTime += (float) dt;
                }
                a++;
            }
        }

        protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
            int y = (getMeasuredHeight() / 2) - (AndroidUtilities.dp(28.0f) / 2);
            this.deleteProgressRect.set((float) (getMeasuredWidth() - AndroidUtilities.dp(49.0f)), (float) y, (float) (getMeasuredWidth() - AndroidUtilities.dp(21.0f)), (float) (AndroidUtilities.dp(28.0f) + y));
        }
    }

    public void closePhoto(boolean r1, boolean r2) {
        /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.DecodeException: Load method exception in method: org.telegram.ui.SecretMediaViewer.closePhoto(boolean, boolean):void
	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:116)
	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:249)
	at jadx.core.ProcessClass.process(ProcessClass.java:34)
	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:59)
	at jadx.core.ProcessClass.process(ProcessClass.java:42)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:306)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler$1.run(JadxDecompiler.java:199)
Caused by: java.lang.NullPointerException
*/
        /*
        r0 = this;
        r0 = r17;
        r1 = r0.parentActivity;
        if (r1 == 0) goto L_0x02f4;
    L_0x0006:
        r1 = r0.isPhotoVisible;
        if (r1 == 0) goto L_0x02f4;
    L_0x000a:
        r1 = r17.checkPhotoAnimation();
        if (r1 == 0) goto L_0x0012;
    L_0x0010:
        goto L_0x02f4;
    L_0x0012:
        r1 = r0.currentAccount;
        r1 = org.telegram.messenger.NotificationCenter.getInstance(r1);
        r2 = org.telegram.messenger.NotificationCenter.messagesDeleted;
        r1.removeObserver(r0, r2);
        r1 = r0.currentAccount;
        r1 = org.telegram.messenger.NotificationCenter.getInstance(r1);
        r2 = org.telegram.messenger.NotificationCenter.updateMessageMedia;
        r1.removeObserver(r0, r2);
        r1 = r0.currentAccount;
        r1 = org.telegram.messenger.NotificationCenter.getInstance(r1);
        r2 = org.telegram.messenger.NotificationCenter.didCreatedNewDeleteTask;
        r1.removeObserver(r0, r2);
        r1 = 0;
        r0.isActionBarVisible = r1;
        r2 = r0.velocityTracker;
        r3 = 0;
        if (r2 == 0) goto L_0x0042;
    L_0x003b:
        r2 = r0.velocityTracker;
        r2.recycle();
        r0.velocityTracker = r3;
    L_0x0042:
        r4 = java.lang.System.currentTimeMillis();
        r0.closeTime = r4;
        r2 = r0.currentProvider;
        if (r2 == 0) goto L_0x006e;
    L_0x004c:
        r2 = r0.currentMessageObject;
        r2 = r2.messageOwner;
        r2 = r2.media;
        r2 = r2.photo;
        r2 = r2 instanceof org.telegram.tgnet.TLRPC.TL_photoEmpty;
        if (r2 != 0) goto L_0x006e;
    L_0x0058:
        r2 = r0.currentMessageObject;
        r2 = r2.messageOwner;
        r2 = r2.media;
        r2 = r2.document;
        r2 = r2 instanceof org.telegram.tgnet.TLRPC.TL_documentEmpty;
        if (r2 == 0) goto L_0x0065;
    L_0x0064:
        goto L_0x006e;
    L_0x0065:
        r2 = r0.currentProvider;
        r4 = r0.currentMessageObject;
        r2 = r2.getPlaceForPhoto(r4, r3, r1);
        goto L_0x006f;
    L_0x006e:
        r2 = 0;
        r4 = r0.videoPlayer;
        if (r4 == 0) goto L_0x0079;
        r4 = r0.videoPlayer;
        r4.pause();
        r6 = 3;
        r7 = 2;
        r8 = 0;
        r9 = 1;
        if (r18 == 0) goto L_0x027d;
        r0.photoAnimationInProgress = r6;
        r10 = r0.containerView;
        r10.invalidate();
        r10 = new android.animation.AnimatorSet;
        r10.<init>();
        r0.imageMoveAnimation = r10;
        r10 = 21;
        if (r2 == 0) goto L_0x0178;
        r11 = r2.imageReceiver;
        r11 = r11.getThumbBitmap();
        if (r11 == 0) goto L_0x0178;
        if (r19 != 0) goto L_0x0178;
        r12 = r2.imageReceiver;
        r12.setVisible(r1, r9);
        r12 = r2.imageReceiver;
        r12 = r12.getDrawRegion();
        r13 = r12.right;
        r14 = r12.left;
        r13 = r13 - r14;
        r13 = (float) r13;
        r14 = r12.bottom;
        r3 = r12.top;
        r14 = r14 - r3;
        r3 = (float) r14;
        r14 = org.telegram.messenger.AndroidUtilities.displaySize;
        r14 = r14.x;
        r4 = org.telegram.messenger.AndroidUtilities.displaySize;
        r4 = r4.y;
        r5 = android.os.Build.VERSION.SDK_INT;
        if (r5 < r10) goto L_0x00c1;
        r5 = org.telegram.messenger.AndroidUtilities.statusBarHeight;
        goto L_0x00c2;
        r5 = r1;
        r4 = r4 + r5;
        r5 = (float) r14;
        r5 = r13 / r5;
        r6 = (float) r4;
        r6 = r3 / r6;
        r5 = java.lang.Math.max(r5, r6);
        r0.animateToScale = r5;
        r5 = r2.viewX;
        r6 = r12.left;
        r5 = r5 + r6;
        r5 = (float) r5;
        r6 = 1073741824; // 0x40000000 float:2.0 double:5.304989477E-315;
        r15 = r13 / r6;
        r5 = r5 + r15;
        r1 = r14 / 2;
        r1 = (float) r1;
        r5 = r5 - r1;
        r0.animateToX = r5;
        r1 = r2.viewY;
        r5 = r12.top;
        r1 = r1 + r5;
        r1 = (float) r1;
        r5 = r3 / r6;
        r1 = r1 + r5;
        r5 = r4 / 2;
        r5 = (float) r5;
        r1 = r1 - r5;
        r0.animateToY = r1;
        r1 = r12.left;
        r5 = r2.imageReceiver;
        r5 = r5.getImageX();
        r1 = r1 - r5;
        r1 = java.lang.Math.abs(r1);
        r1 = (float) r1;
        r0.animateToClipHorizontal = r1;
        r1 = r12.top;
        r5 = r2.imageReceiver;
        r5 = r5.getImageY();
        r1 = r1 - r5;
        r1 = java.lang.Math.abs(r1);
        r5 = new int[r7];
        r6 = r2.parentView;
        r6.getLocationInWindow(r5);
        r6 = r5[r9];
        r7 = android.os.Build.VERSION.SDK_INT;
        if (r7 < r10) goto L_0x011b;
        r7 = 0;
        goto L_0x011d;
        r7 = org.telegram.messenger.AndroidUtilities.statusBarHeight;
        r6 = r6 - r7;
        r7 = r2.viewY;
        r10 = r12.top;
        r7 = r7 + r10;
        r6 = r6 - r7;
        r7 = r2.clipTopAddition;
        r6 = r6 + r7;
        r6 = (float) r6;
        r0.animateToClipTop = r6;
        r6 = r0.animateToClipTop;
        r6 = (r6 > r8 ? 1 : (r6 == r8 ? 0 : -1));
        if (r6 >= 0) goto L_0x0132;
        r0.animateToClipTop = r8;
        r6 = r2.viewY;
        r7 = r12.top;
        r6 = r6 + r7;
        r7 = (int) r3;
        r6 = r6 + r7;
        r7 = r5[r9];
        r10 = r2.parentView;
        r10 = r10.getHeight();
        r7 = r7 + r10;
        r10 = android.os.Build.VERSION.SDK_INT;
        r9 = 21;
        if (r10 < r9) goto L_0x014a;
        r9 = 0;
        goto L_0x014c;
        r9 = org.telegram.messenger.AndroidUtilities.statusBarHeight;
        r7 = r7 - r9;
        r6 = r6 - r7;
        r7 = r2.clipBottomAddition;
        r6 = r6 + r7;
        r6 = (float) r6;
        r0.animateToClipBottom = r6;
        r6 = r0.animateToClipBottom;
        r6 = (r6 > r8 ? 1 : (r6 == r8 ? 0 : -1));
        if (r6 >= 0) goto L_0x015c;
        r0.animateToClipBottom = r8;
        r6 = java.lang.System.currentTimeMillis();
        r0.animationStartTime = r6;
        r6 = r0.animateToClipBottom;
        r7 = (float) r1;
        r6 = java.lang.Math.max(r6, r7);
        r0.animateToClipBottom = r6;
        r6 = r0.animateToClipTop;
        r7 = (float) r1;
        r6 = java.lang.Math.max(r6, r7);
        r0.animateToClipTop = r6;
        r6 = 1;
        r0.zoomAnimation = r6;
        goto L_0x0193;
        r1 = org.telegram.messenger.AndroidUtilities.displaySize;
        r1 = r1.y;
        r3 = android.os.Build.VERSION.SDK_INT;
        r4 = 21;
        if (r3 < r4) goto L_0x0185;
        r3 = org.telegram.messenger.AndroidUtilities.statusBarHeight;
        goto L_0x0186;
        r3 = 0;
        r1 = r1 + r3;
        r3 = r0.translationY;
        r3 = (r3 > r8 ? 1 : (r3 == r8 ? 0 : -1));
        if (r3 < 0) goto L_0x018f;
        r3 = (float) r1;
        goto L_0x0191;
        r3 = -r1;
        r3 = (float) r3;
        r0.animateToY = r3;
        r1 = r0.isVideo;
        r3 = 5;
        if (r1 == 0) goto L_0x01ec;
        r1 = 0;
        r0.videoCrossfadeStarted = r1;
        r0.textureUploaded = r1;
        r4 = r0.imageMoveAnimation;
        r3 = new android.animation.Animator[r3];
        r5 = r0.photoBackgroundDrawable;
        r6 = "alpha";
        r7 = 1;
        r9 = new int[r7];
        r9[r1] = r1;
        r5 = android.animation.ObjectAnimator.ofInt(r5, r6, r9);
        r3[r1] = r5;
        r5 = "animationValue";
        r6 = 2;
        r9 = new float[r6];
        r9 = {0, 1065353216};
        r5 = android.animation.ObjectAnimator.ofFloat(r0, r5, r9);
        r3[r7] = r5;
        r5 = r0.actionBar;
        r9 = "alpha";
        r10 = new float[r7];
        r10[r1] = r8;
        r5 = android.animation.ObjectAnimator.ofFloat(r5, r9, r10);
        r3[r6] = r5;
        r5 = r0.secretDeleteTimer;
        r6 = "alpha";
        r9 = new float[r7];
        r9[r1] = r8;
        r5 = android.animation.ObjectAnimator.ofFloat(r5, r6, r9);
        r6 = 3;
        r3[r6] = r5;
        r5 = "videoCrossfadeAlpha";
        r6 = new float[r7];
        r6[r1] = r8;
        r1 = android.animation.ObjectAnimator.ofFloat(r0, r5, r6);
        r5 = 4;
        r3[r5] = r1;
        r4.playTogether(r3);
        goto L_0x0242;
        r7 = 1;
        r1 = r0.centerImage;
        r1.setManualAlphaAnimator(r7);
        r1 = r0.imageMoveAnimation;
        r3 = new android.animation.Animator[r3];
        r4 = r0.photoBackgroundDrawable;
        r5 = "alpha";
        r6 = new int[r7];
        r9 = 0;
        r6[r9] = r9;
        r4 = android.animation.ObjectAnimator.ofInt(r4, r5, r6);
        r3[r9] = r4;
        r4 = "animationValue";
        r5 = 2;
        r6 = new float[r5];
        r6 = {0, 1065353216};
        r4 = android.animation.ObjectAnimator.ofFloat(r0, r4, r6);
        r3[r7] = r4;
        r4 = r0.actionBar;
        r6 = "alpha";
        r10 = new float[r7];
        r10[r9] = r8;
        r4 = android.animation.ObjectAnimator.ofFloat(r4, r6, r10);
        r3[r5] = r4;
        r4 = r0.secretDeleteTimer;
        r5 = "alpha";
        r6 = new float[r7];
        r6[r9] = r8;
        r4 = android.animation.ObjectAnimator.ofFloat(r4, r5, r6);
        r5 = 3;
        r3[r5] = r4;
        r4 = r0.centerImage;
        r5 = "currentAlpha";
        r6 = new float[r7];
        r6[r9] = r8;
        r4 = android.animation.ObjectAnimator.ofFloat(r4, r5, r6);
        r5 = 4;
        r3[r5] = r4;
        r1.playTogether(r3);
        r1 = new org.telegram.ui.SecretMediaViewer$10;
        r1.<init>(r2);
        r0.photoAnimationEndRunnable = r1;
        r1 = r0.imageMoveAnimation;
        r3 = new android.view.animation.DecelerateInterpolator;
        r3.<init>();
        r1.setInterpolator(r3);
        r1 = r0.imageMoveAnimation;
        r3 = 250; // 0xfa float:3.5E-43 double:1.235E-321;
        r1.setDuration(r3);
        r1 = r0.imageMoveAnimation;
        r3 = new org.telegram.ui.SecretMediaViewer$11;
        r3.<init>(r2);
        r1.addListener(r3);
        r3 = java.lang.System.currentTimeMillis();
        r0.photoTransitionAnimationStartTime = r3;
        r1 = android.os.Build.VERSION.SDK_INT;
        r3 = 18;
        if (r1 < r3) goto L_0x0277;
        r1 = r0.containerView;
        r3 = 2;
        r4 = 0;
        r1.setLayerType(r3, r4);
        r1 = r0.imageMoveAnimation;
        r1.start();
        goto L_0x02f3;
        r1 = new android.animation.AnimatorSet;
        r1.<init>();
        r3 = 4;
        r3 = new android.animation.Animator[r3];
        r4 = r0.containerView;
        r5 = "scaleX";
        r6 = 1;
        r7 = new float[r6];
        r9 = 1063675494; // 0x3f666666 float:0.9 double:5.2552552E-315;
        r10 = 0;
        r7[r10] = r9;
        r4 = android.animation.ObjectAnimator.ofFloat(r4, r5, r7);
        r3[r10] = r4;
        r4 = r0.containerView;
        r5 = "scaleY";
        r7 = new float[r6];
        r7[r10] = r9;
        r4 = android.animation.ObjectAnimator.ofFloat(r4, r5, r7);
        r3[r6] = r4;
        r4 = r0.photoBackgroundDrawable;
        r5 = "alpha";
        r7 = new int[r6];
        r7[r10] = r10;
        r4 = android.animation.ObjectAnimator.ofInt(r4, r5, r7);
        r5 = 2;
        r3[r5] = r4;
        r4 = r0.actionBar;
        r7 = "alpha";
        r6 = new float[r6];
        r6[r10] = r8;
        r4 = android.animation.ObjectAnimator.ofFloat(r4, r7, r6);
        r6 = 3;
        r3[r6] = r4;
        r1.playTogether(r3);
        r0.photoAnimationInProgress = r5;
        r3 = new org.telegram.ui.SecretMediaViewer$12;
        r3.<init>(r2);
        r0.photoAnimationEndRunnable = r3;
        r3 = 200; // 0xc8 float:2.8E-43 double:9.9E-322;
        r1.setDuration(r3);
        r3 = new org.telegram.ui.SecretMediaViewer$13;
        r3.<init>();
        r1.addListener(r3);
        r3 = java.lang.System.currentTimeMillis();
        r0.photoTransitionAnimationStartTime = r3;
        r3 = android.os.Build.VERSION.SDK_INT;
        r4 = 18;
        if (r3 < r4) goto L_0x02f0;
        r3 = r0.containerView;
        r4 = 2;
        r5 = 0;
        r3.setLayerType(r4, r5);
        r1.start();
        return;
    L_0x02f4:
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.telegram.ui.SecretMediaViewer.closePhoto(boolean, boolean):void");
    }

    public static SecretMediaViewer getInstance() {
        SecretMediaViewer localInstance = Instance;
        if (localInstance == null) {
            synchronized (PhotoViewer.class) {
                localInstance = Instance;
                if (localInstance == null) {
                    SecretMediaViewer secretMediaViewer = new SecretMediaViewer();
                    localInstance = secretMediaViewer;
                    Instance = secretMediaViewer;
                }
            }
        }
        return localInstance;
    }

    public static boolean hasInstance() {
        return Instance != null;
    }

    public void didReceivedNotification(int id, int account, Object... args) {
        if (id == NotificationCenter.messagesDeleted) {
            if (this.currentMessageObject == null || ((Integer) args[1]).intValue() != 0) {
                return;
            }
            if (args[0].contains(Integer.valueOf(this.currentMessageObject.getId()))) {
                if (!this.isVideo || this.videoWatchedOneTime) {
                    closePhoto(true, true);
                } else {
                    this.closeVideoAfterWatch = true;
                }
            }
        } else if (id == NotificationCenter.didCreatedNewDeleteTask) {
            if (this.currentMessageObject != null) {
                if (this.secretDeleteTimer != null) {
                    SparseArray<ArrayList<Long>> mids = args[0];
                    for (int i = 0; i < mids.size(); i++) {
                        int key = mids.keyAt(i);
                        ArrayList<Long> arr = (ArrayList) mids.get(key);
                        for (int a = 0; a < arr.size(); a++) {
                            long mid = ((Long) arr.get(a)).longValue();
                            if (a == 0) {
                                int channelId = (int) (mid >> 32);
                                if (channelId < 0) {
                                    channelId = 0;
                                }
                                if (channelId != this.currentChannelId) {
                                    return;
                                }
                            }
                            if (((long) this.currentMessageObject.getId()) == mid) {
                                this.currentMessageObject.messageOwner.destroyTime = key;
                                this.secretDeleteTimer.invalidate();
                                return;
                            }
                        }
                    }
                }
            }
        } else if (id == NotificationCenter.updateMessageMedia) {
            if (this.currentMessageObject.getId() == args[0].id) {
                if (!this.isVideo || this.videoWatchedOneTime) {
                    closePhoto(true, true);
                } else {
                    this.closeVideoAfterWatch = true;
                }
            }
        }
    }

    private void preparePlayer(File file) {
        if (this.parentActivity != null) {
            releasePlayer();
            if (this.videoTextureView == null) {
                this.aspectRatioFrameLayout = new AspectRatioFrameLayout(this.parentActivity);
                this.aspectRatioFrameLayout.setVisibility(4);
                this.containerView.addView(this.aspectRatioFrameLayout, 0, LayoutHelper.createFrame(-1, -1, 17));
                this.videoTextureView = new TextureView(this.parentActivity);
                this.videoTextureView.setOpaque(false);
                this.aspectRatioFrameLayout.addView(this.videoTextureView, LayoutHelper.createFrame(-1, -1, 17));
            }
            this.textureUploaded = false;
            this.videoCrossfadeStarted = false;
            TextureView textureView = this.videoTextureView;
            this.videoCrossfadeAlpha = 0.0f;
            textureView.setAlpha(0.0f);
            if (this.videoPlayer == null) {
                this.videoPlayer = new VideoPlayer();
                this.videoPlayer.setTextureView(this.videoTextureView);
                this.videoPlayer.setDelegate(new VideoPlayerDelegate() {
                    public void onStateChanged(boolean playWhenReady, int playbackState) {
                        if (SecretMediaViewer.this.videoPlayer != null) {
                            if (SecretMediaViewer.this.currentMessageObject != null) {
                                if (playbackState == 4 || playbackState == 1) {
                                    try {
                                        SecretMediaViewer.this.parentActivity.getWindow().clearFlags(128);
                                    } catch (Throwable e) {
                                        FileLog.e(e);
                                    }
                                } else {
                                    try {
                                        SecretMediaViewer.this.parentActivity.getWindow().addFlags(128);
                                    } catch (Throwable e2) {
                                        FileLog.e(e2);
                                    }
                                }
                                if (playbackState == 3 && SecretMediaViewer.this.aspectRatioFrameLayout.getVisibility() != 0) {
                                    SecretMediaViewer.this.aspectRatioFrameLayout.setVisibility(0);
                                }
                                if (!SecretMediaViewer.this.videoPlayer.isPlaying() || playbackState == 4) {
                                    if (SecretMediaViewer.this.isPlaying) {
                                        SecretMediaViewer.this.isPlaying = false;
                                        if (playbackState == 4) {
                                            SecretMediaViewer.this.videoWatchedOneTime = true;
                                            if (SecretMediaViewer.this.closeVideoAfterWatch) {
                                                SecretMediaViewer.this.closePhoto(true, true);
                                            } else {
                                                SecretMediaViewer.this.videoPlayer.seekTo(0);
                                                SecretMediaViewer.this.videoPlayer.play();
                                            }
                                        }
                                    }
                                } else if (!SecretMediaViewer.this.isPlaying) {
                                    SecretMediaViewer.this.isPlaying = true;
                                }
                            }
                        }
                    }

                    public void onError(Exception e) {
                        FileLog.e((Throwable) e);
                    }

                    public void onVideoSizeChanged(int width, int height, int unappliedRotationDegrees, float pixelWidthHeightRatio) {
                        if (SecretMediaViewer.this.aspectRatioFrameLayout != null) {
                            if (unappliedRotationDegrees == 90 || unappliedRotationDegrees == 270) {
                                int temp = width;
                                width = height;
                                height = temp;
                            }
                            SecretMediaViewer.this.aspectRatioFrameLayout.setAspectRatio(height == 0 ? 1.0f : (((float) width) * pixelWidthHeightRatio) / ((float) height), unappliedRotationDegrees);
                        }
                    }

                    public void onRenderedFirstFrame() {
                        if (!SecretMediaViewer.this.textureUploaded) {
                            SecretMediaViewer.this.textureUploaded = true;
                            SecretMediaViewer.this.containerView.invalidate();
                        }
                    }

                    public boolean onSurfaceDestroyed(SurfaceTexture surfaceTexture) {
                        return false;
                    }

                    public void onSurfaceTextureUpdated(SurfaceTexture surfaceTexture) {
                    }
                });
            }
            this.videoPlayer.preparePlayer(Uri.fromFile(file), "other");
            this.videoPlayer.setPlayWhenReady(true);
        }
    }

    private void releasePlayer() {
        if (this.videoPlayer != null) {
            this.videoPlayer.releasePlayer();
            this.videoPlayer = null;
        }
        try {
            if (this.parentActivity != null) {
                this.parentActivity.getWindow().clearFlags(128);
            }
        } catch (Throwable e) {
            FileLog.e(e);
        }
        if (this.aspectRatioFrameLayout != null) {
            this.containerView.removeView(this.aspectRatioFrameLayout);
            this.aspectRatioFrameLayout = null;
        }
        if (this.videoTextureView != null) {
            this.videoTextureView = null;
        }
        this.isPlaying = false;
    }

    public void setParentActivity(Activity activity) {
        this.currentAccount = UserConfig.selectedAccount;
        this.centerImage.setCurrentAccount(this.currentAccount);
        if (this.parentActivity != activity) {
            this.parentActivity = activity;
            this.scroller = new Scroller(activity);
            this.windowView = new FrameLayout(activity) {
                protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
                    int widthSize = MeasureSpec.getSize(widthMeasureSpec);
                    int heightSize = MeasureSpec.getSize(heightMeasureSpec);
                    if (VERSION.SDK_INT >= 21 && SecretMediaViewer.this.lastInsets != null) {
                        WindowInsets insets = (WindowInsets) SecretMediaViewer.this.lastInsets;
                        if (AndroidUtilities.incorrectDisplaySizeFix) {
                            if (heightSize > AndroidUtilities.displaySize.y) {
                                heightSize = AndroidUtilities.displaySize.y;
                            }
                            heightSize += AndroidUtilities.statusBarHeight;
                        }
                        heightSize -= insets.getSystemWindowInsetBottom();
                        widthSize -= insets.getSystemWindowInsetRight();
                    } else if (heightSize > AndroidUtilities.displaySize.y) {
                        heightSize = AndroidUtilities.displaySize.y;
                    }
                    setMeasuredDimension(widthSize, heightSize);
                    if (VERSION.SDK_INT >= 21 && SecretMediaViewer.this.lastInsets != null) {
                        widthSize -= ((WindowInsets) SecretMediaViewer.this.lastInsets).getSystemWindowInsetLeft();
                    }
                    SecretMediaViewer.this.containerView.measure(MeasureSpec.makeMeasureSpec(widthSize, 1073741824), MeasureSpec.makeMeasureSpec(heightSize, 1073741824));
                }

                protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
                    int x = 0;
                    if (VERSION.SDK_INT >= 21 && SecretMediaViewer.this.lastInsets != null) {
                        x = 0 + ((WindowInsets) SecretMediaViewer.this.lastInsets).getSystemWindowInsetLeft();
                    }
                    SecretMediaViewer.this.containerView.layout(x, 0, SecretMediaViewer.this.containerView.getMeasuredWidth() + x, SecretMediaViewer.this.containerView.getMeasuredHeight());
                    if (changed) {
                        if (SecretMediaViewer.this.imageMoveAnimation == null) {
                            SecretMediaViewer.this.scale = 1.0f;
                            SecretMediaViewer.this.translationX = 0.0f;
                            SecretMediaViewer.this.translationY = 0.0f;
                        }
                        SecretMediaViewer.this.updateMinMax(SecretMediaViewer.this.scale);
                    }
                }
            };
            this.windowView.setBackgroundDrawable(this.photoBackgroundDrawable);
            this.windowView.setFocusable(true);
            this.windowView.setFocusableInTouchMode(true);
            this.containerView = new FrameLayoutDrawer(activity) {
                protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
                    super.onLayout(changed, left, top, right, bottom);
                    if (SecretMediaViewer.this.secretDeleteTimer != null) {
                        int y = ((ActionBar.getCurrentActionBarHeight() - SecretMediaViewer.this.secretDeleteTimer.getMeasuredHeight()) / 2) + (VERSION.SDK_INT >= 21 ? AndroidUtilities.statusBarHeight : 0);
                        SecretMediaViewer.this.secretDeleteTimer.layout(SecretMediaViewer.this.secretDeleteTimer.getLeft(), y, SecretMediaViewer.this.secretDeleteTimer.getRight(), SecretMediaViewer.this.secretDeleteTimer.getMeasuredHeight() + y);
                    }
                }
            };
            this.containerView.setFocusable(false);
            this.windowView.addView(this.containerView);
            FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) this.containerView.getLayoutParams();
            layoutParams.width = -1;
            layoutParams.height = -1;
            layoutParams.gravity = 51;
            this.containerView.setLayoutParams(layoutParams);
            if (VERSION.SDK_INT >= 21) {
                this.containerView.setFitsSystemWindows(true);
                this.containerView.setOnApplyWindowInsetsListener(new OnApplyWindowInsetsListener() {
                    @SuppressLint({"NewApi"})
                    public WindowInsets onApplyWindowInsets(View v, WindowInsets insets) {
                        WindowInsets oldInsets = (WindowInsets) SecretMediaViewer.this.lastInsets;
                        SecretMediaViewer.this.lastInsets = insets;
                        if (oldInsets == null || !oldInsets.toString().equals(insets.toString())) {
                            SecretMediaViewer.this.windowView.requestLayout();
                        }
                        return insets.consumeSystemWindowInsets();
                    }
                });
                this.containerView.setSystemUiVisibility(1280);
            }
            this.gestureDetector = new GestureDetector(this.containerView.getContext(), this);
            this.gestureDetector.setOnDoubleTapListener(this);
            this.actionBar = new ActionBar(activity);
            this.actionBar.setTitleColor(-1);
            this.actionBar.setSubtitleColor(-1);
            this.actionBar.setBackgroundColor(Theme.ACTION_BAR_PHOTO_VIEWER_COLOR);
            this.actionBar.setOccupyStatusBar(VERSION.SDK_INT >= 21);
            this.actionBar.setItemsBackgroundColor(Theme.ACTION_BAR_WHITE_SELECTOR_COLOR, false);
            this.actionBar.setBackButtonImage(R.drawable.ic_ab_back);
            this.actionBar.setTitleRightMargin(AndroidUtilities.dp(70.0f));
            this.containerView.addView(this.actionBar, LayoutHelper.createFrame(-1, -2.0f));
            this.actionBar.setActionBarMenuOnItemClick(new ActionBarMenuOnItemClick() {
                public void onItemClick(int id) {
                    if (id == -1) {
                        SecretMediaViewer.this.closePhoto(true, false);
                    }
                }
            });
            this.secretDeleteTimer = new SecretDeleteTimer(activity);
            this.containerView.addView(this.secretDeleteTimer, LayoutHelper.createFrame(119, 48.0f, 53, 0.0f, 0.0f, 0.0f, 0.0f));
            this.windowLayoutParams = new LayoutParams();
            this.windowLayoutParams.height = -1;
            this.windowLayoutParams.format = -3;
            this.windowLayoutParams.width = -1;
            this.windowLayoutParams.gravity = 48;
            this.windowLayoutParams.type = 99;
            if (VERSION.SDK_INT >= 21) {
                this.windowLayoutParams.flags = -2147417848;
            } else {
                this.windowLayoutParams.flags = 8;
            }
            this.centerImage.setParentView(this.containerView);
            this.centerImage.setForceCrossfade(true);
        }
    }

    public void openMedia(MessageObject messageObject, PhotoViewerProvider provider) {
        MessageObject messageObject2 = messageObject;
        PhotoViewerProvider photoViewerProvider = provider;
        if (!(this.parentActivity == null || messageObject2 == null || !messageObject.needDrawBluredPreview())) {
            if (photoViewerProvider != null) {
                final PlaceProviderObject object = photoViewerProvider.getPlaceForPhoto(messageObject2, null, 0);
                if (object != null) {
                    r1.currentProvider = photoViewerProvider;
                    r1.openTime = System.currentTimeMillis();
                    r1.closeTime = 0;
                    r1.isActionBarVisible = true;
                    r1.isPhotoVisible = true;
                    r1.draggingDown = false;
                    if (r1.aspectRatioFrameLayout != null) {
                        r1.aspectRatioFrameLayout.setVisibility(4);
                    }
                    releasePlayer();
                    r1.pinchStartDistance = 0.0f;
                    r1.pinchStartScale = 1.0f;
                    r1.pinchCenterX = 0.0f;
                    r1.pinchCenterY = 0.0f;
                    r1.pinchStartX = 0.0f;
                    r1.pinchStartY = 0.0f;
                    r1.moveStartX = 0.0f;
                    r1.moveStartY = 0.0f;
                    r1.zooming = false;
                    r1.moving = false;
                    r1.doubleTap = false;
                    r1.invalidCoords = false;
                    r1.canDragDown = true;
                    updateMinMax(r1.scale);
                    r1.photoBackgroundDrawable.setAlpha(0);
                    r1.containerView.setAlpha(1.0f);
                    r1.containerView.setVisibility(0);
                    r1.secretDeleteTimer.setAlpha(1.0f);
                    r1.isVideo = false;
                    r1.videoWatchedOneTime = false;
                    r1.closeVideoAfterWatch = false;
                    r1.disableShowCheck = true;
                    r1.centerImage.setManualAlphaAnimator(false);
                    Rect drawRegion = object.imageReceiver.getDrawRegion();
                    float width = (float) (drawRegion.right - drawRegion.left);
                    float height = (float) (drawRegion.bottom - drawRegion.top);
                    int viewWidth = AndroidUtilities.displaySize.x;
                    int viewHeight = AndroidUtilities.displaySize.y + (VERSION.SDK_INT >= 21 ? AndroidUtilities.statusBarHeight : 0);
                    r1.scale = Math.max(width / ((float) viewWidth), height / ((float) viewHeight));
                    r1.translationX = (((float) (object.viewX + drawRegion.left)) + (width / 2.0f)) - ((float) (viewWidth / 2));
                    r1.translationY = (((float) (object.viewY + drawRegion.top)) + (height / 2.0f)) - ((float) (viewHeight / 2));
                    r1.clipHorizontal = (float) Math.abs(drawRegion.left - object.imageReceiver.getImageX());
                    int clipVertical = Math.abs(drawRegion.top - object.imageReceiver.getImageY());
                    int[] coords2 = new int[2];
                    object.parentView.getLocationInWindow(coords2);
                    r1.clipTop = (float) (((coords2[1] - (VERSION.SDK_INT >= 21 ? 0 : AndroidUtilities.statusBarHeight)) - (object.viewY + drawRegion.top)) + object.clipTopAddition);
                    if (r1.clipTop < 0.0f) {
                        r1.clipTop = 0.0f;
                    }
                    r1.clipBottom = (float) ((((object.viewY + drawRegion.top) + ((int) height)) - ((coords2[1] + object.parentView.getHeight()) - (VERSION.SDK_INT >= 21 ? 0 : AndroidUtilities.statusBarHeight))) + object.clipBottomAddition);
                    if (r1.clipBottom < 0.0f) {
                        r1.clipBottom = 0.0f;
                    }
                    r1.clipTop = Math.max(r1.clipTop, (float) clipVertical);
                    r1.clipBottom = Math.max(r1.clipBottom, (float) clipVertical);
                    r1.animationStartTime = System.currentTimeMillis();
                    r1.animateToX = 0.0f;
                    r1.animateToY = 0.0f;
                    r1.animateToClipBottom = 0.0f;
                    r1.animateToClipHorizontal = 0.0f;
                    r1.animateToClipTop = 0.0f;
                    r1.animateToScale = 1.0f;
                    r1.zoomAnimation = true;
                    NotificationCenter.getInstance(r1.currentAccount).addObserver(r1, NotificationCenter.messagesDeleted);
                    NotificationCenter.getInstance(r1.currentAccount).addObserver(r1, NotificationCenter.updateMessageMedia);
                    NotificationCenter.getInstance(r1.currentAccount).addObserver(r1, NotificationCenter.didCreatedNewDeleteTask);
                    r1.currentChannelId = messageObject2.messageOwner.to_id != null ? messageObject2.messageOwner.to_id.channel_id : 0;
                    toggleActionBar(true, false);
                    r1.currentMessageObject = messageObject2;
                    TLObject document = messageObject.getDocument();
                    if (r1.currentThumb != null) {
                        r1.currentThumb.release();
                        r1.currentThumb = null;
                    }
                    r1.currentThumb = object.imageReceiver.getThumbBitmapSafe();
                    int i;
                    int[] iArr;
                    Rect rect;
                    if (document == null) {
                        i = clipVertical;
                        iArr = coords2;
                        rect = drawRegion;
                        r1.actionBar.setTitle(LocaleController.getString("DisappearingPhoto", R.string.DisappearingPhoto));
                        r1.centerImage.setImage(FileLoader.getClosestPhotoSizeWithSize(messageObject2.photoThumbs, AndroidUtilities.getPhotoSize()).location, null, r1.currentThumb != null ? new BitmapDrawable(r1.currentThumb.bitmap) : null, -1, null, 2);
                        r1.secretDeleteTimer.setDestroyTime(((long) messageObject2.messageOwner.destroyTime) * 1000, (long) messageObject2.messageOwner.ttl, false);
                    } else if (MessageObject.isGifDocument((Document) document)) {
                        r1.actionBar.setTitle(LocaleController.getString("DisappearingGif", R.string.DisappearingGif));
                        r1.centerImage.setImage(document, null, r1.currentThumb != null ? new BitmapDrawable(r1.currentThumb.bitmap) : null, -1, null, 1);
                        r1.secretDeleteTimer.setDestroyTime(((long) messageObject2.messageOwner.destroyTime) * 1000, (long) messageObject2.messageOwner.ttl, false);
                        r33 = document;
                        i = clipVertical;
                        iArr = coords2;
                        rect = drawRegion;
                    } else {
                        r1.actionBar.setTitle(LocaleController.getString("DisappearingVideo", R.string.DisappearingVideo));
                        File f = new File(messageObject2.messageOwner.attachPath);
                        if (f.exists()) {
                            preparePlayer(f);
                            r33 = document;
                        } else {
                            File file = FileLoader.getPathToMessage(messageObject2.messageOwner);
                            StringBuilder stringBuilder = new StringBuilder();
                            stringBuilder.append(file.getAbsolutePath());
                            stringBuilder.append(".enc");
                            File encryptedFile = new File(stringBuilder.toString());
                            if (encryptedFile.exists()) {
                                file = encryptedFile;
                            }
                            preparePlayer(file);
                        }
                        r1.isVideo = true;
                        r1.centerImage.setImage(null, null, r1.currentThumb != null ? new BitmapDrawable(r1.currentThumb.bitmap) : null, -1, null, 2);
                        long destroyTime = ((long) messageObject2.messageOwner.destroyTime) * 1000;
                        if (((long) (messageObject.getDuration() * 1000)) > destroyTime - (System.currentTimeMillis() + ((long) (ConnectionsManager.getInstance(r1.currentAccount).getTimeDifference() * 1000)))) {
                            r1.secretDeleteTimer.setDestroyTime(-1, -1, true);
                        } else {
                            r1.secretDeleteTimer.setDestroyTime(((long) messageObject2.messageOwner.destroyTime) * 1000, (long) messageObject2.messageOwner.ttl, false);
                        }
                    }
                    try {
                        if (r1.windowView.getParent() != null) {
                            ((WindowManager) r1.parentActivity.getSystemService("window")).removeView(r1.windowView);
                        }
                    } catch (Throwable e) {
                        FileLog.e(e);
                    }
                    ((WindowManager) r1.parentActivity.getSystemService("window")).addView(r1.windowView, r1.windowLayoutParams);
                    r1.secretDeleteTimer.invalidate();
                    r1.isVisible = true;
                    r1.imageMoveAnimation = new AnimatorSet();
                    r1.imageMoveAnimation.playTogether(new Animator[]{ObjectAnimator.ofFloat(r1.actionBar, "alpha", new float[]{0.0f, 1.0f}), ObjectAnimator.ofFloat(r1.secretDeleteTimer, "alpha", new float[]{0.0f, 1.0f}), ObjectAnimator.ofInt(r1.photoBackgroundDrawable, "alpha", new int[]{0, 255}), ObjectAnimator.ofFloat(r1.secretDeleteTimer, "alpha", new float[]{0.0f, 1.0f}), ObjectAnimator.ofFloat(r1, "animationValue", new float[]{0.0f, 1.0f})});
                    r1.photoAnimationInProgress = 3;
                    r1.photoAnimationEndRunnable = new Runnable() {
                        public void run() {
                            SecretMediaViewer.this.photoAnimationInProgress = 0;
                            SecretMediaViewer.this.imageMoveAnimation = null;
                            if (SecretMediaViewer.this.containerView != null) {
                                if (VERSION.SDK_INT >= 18) {
                                    SecretMediaViewer.this.containerView.setLayerType(0, null);
                                }
                                SecretMediaViewer.this.containerView.invalidate();
                            }
                        }
                    };
                    r1.imageMoveAnimation.setDuration(250);
                    r1.imageMoveAnimation.addListener(new AnimatorListenerAdapter() {
                        public void onAnimationEnd(Animator animation) {
                            if (SecretMediaViewer.this.photoAnimationEndRunnable != null) {
                                SecretMediaViewer.this.photoAnimationEndRunnable.run();
                                SecretMediaViewer.this.photoAnimationEndRunnable = null;
                            }
                        }
                    });
                    r1.photoTransitionAnimationStartTime = System.currentTimeMillis();
                    if (VERSION.SDK_INT >= 18) {
                        r1.containerView.setLayerType(2, null);
                    }
                    r1.imageMoveAnimation.setInterpolator(new DecelerateInterpolator());
                    r1.photoBackgroundDrawable.frame = 0;
                    r1.photoBackgroundDrawable.drawRunnable = new Runnable() {
                        public void run() {
                            SecretMediaViewer.this.disableShowCheck = false;
                            object.imageReceiver.setVisible(false, true);
                        }
                    };
                    r1.imageMoveAnimation.start();
                }
            }
        }
    }

    public boolean isShowingImage(MessageObject object) {
        return (!this.isVisible || this.disableShowCheck || object == null || this.currentMessageObject == null || this.currentMessageObject.getId() != object.getId()) ? false : true;
    }

    private void toggleActionBar(boolean show, boolean animated) {
        if (show) {
            this.actionBar.setVisibility(0);
        }
        this.actionBar.setEnabled(show);
        this.isActionBarVisible = show;
        float f = 0.0f;
        if (animated) {
            ArrayList<Animator> arrayList = new ArrayList();
            ActionBar actionBar = this.actionBar;
            String str = "alpha";
            float[] fArr = new float[1];
            if (show) {
                f = 1.0f;
            }
            fArr[0] = f;
            arrayList.add(ObjectAnimator.ofFloat(actionBar, str, fArr));
            this.currentActionBarAnimation = new AnimatorSet();
            this.currentActionBarAnimation.playTogether(arrayList);
            if (!show) {
                this.currentActionBarAnimation.addListener(new AnimatorListenerAdapter() {
                    public void onAnimationEnd(Animator animation) {
                        if (SecretMediaViewer.this.currentActionBarAnimation != null && SecretMediaViewer.this.currentActionBarAnimation.equals(animation)) {
                            SecretMediaViewer.this.actionBar.setVisibility(8);
                            SecretMediaViewer.this.currentActionBarAnimation = null;
                        }
                    }
                });
            }
            this.currentActionBarAnimation.setDuration(200);
            this.currentActionBarAnimation.start();
            return;
        }
        ActionBar actionBar2 = this.actionBar;
        if (show) {
            f = 1.0f;
        }
        actionBar2.setAlpha(f);
        if (!show) {
            this.actionBar.setVisibility(8);
        }
    }

    public boolean isVisible() {
        return this.isVisible;
    }

    public void destroyPhotoViewer() {
        NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.messagesDeleted);
        NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.updateMessageMedia);
        NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.didCreatedNewDeleteTask);
        this.isVisible = false;
        this.currentProvider = null;
        if (this.currentThumb != null) {
            this.currentThumb.release();
            this.currentThumb = null;
        }
        releasePlayer();
        if (!(this.parentActivity == null || this.windowView == null)) {
            try {
                if (this.windowView.getParent() != null) {
                    ((WindowManager) this.parentActivity.getSystemService("window")).removeViewImmediate(this.windowView);
                }
                this.windowView = null;
            } catch (Throwable e) {
                FileLog.e(e);
            }
        }
        Instance = null;
    }

    private void onDraw(Canvas canvas) {
        Canvas canvas2 = canvas;
        if (this.isPhotoVisible) {
            float av;
            float f;
            float f2;
            float f3;
            float f4;
            float f5;
            float currentClipHorizontal;
            float currentClipHorizontal2;
            float currentClipHorizontal3;
            int bitmapWidth;
            int bitmapWidth2;
            int height;
            int i;
            float f6;
            float aty = -1.0f;
            if (r0.imageMoveAnimation != null) {
                if (!r0.scroller.isFinished()) {
                    r0.scroller.abortAnimation();
                }
                if (r0.useOvershootForScale) {
                    if (r0.animationValue < 0.9f) {
                        av = r0.animationValue / 0.9f;
                        f = r0.scale + (((r0.animateToScale * 1.02f) - r0.scale) * av);
                    } else {
                        av = 1.0f;
                        f = r0.animateToScale + ((r0.animateToScale * 0.01999998f) * (1.0f - ((r0.animationValue - 0.9f) / 0.100000024f)));
                    }
                    f2 = r0.translationY + ((r0.animateToY - r0.translationY) * av);
                    f3 = r0.translationX + ((r0.animateToX - r0.translationX) * av);
                    f4 = r0.clipTop + ((r0.animateToClipTop - r0.clipTop) * av);
                    f5 = r0.clipBottom + ((r0.animateToClipBottom - r0.clipBottom) * av);
                    currentClipHorizontal = r0.clipHorizontal + ((r0.animateToClipHorizontal - r0.clipHorizontal) * av);
                } else {
                    f = r0.scale + ((r0.animateToScale - r0.scale) * r0.animationValue);
                    f2 = r0.translationY + ((r0.animateToY - r0.translationY) * r0.animationValue);
                    f3 = r0.translationX + ((r0.animateToX - r0.translationX) * r0.animationValue);
                    f4 = r0.clipTop + ((r0.animateToClipTop - r0.clipTop) * r0.animationValue);
                    f5 = r0.clipBottom + ((r0.animateToClipBottom - r0.clipBottom) * r0.animationValue);
                    currentClipHorizontal = r0.clipHorizontal + ((r0.animateToClipHorizontal - r0.clipHorizontal) * r0.animationValue);
                }
                currentClipHorizontal2 = currentClipHorizontal;
                if (r0.animateToScale == 1.0f && r0.scale == 1.0f && r0.translationX == 0.0f) {
                    aty = f2;
                }
                r0.containerView.invalidate();
                currentClipHorizontal3 = currentClipHorizontal2;
            } else {
                if (r0.animationStartTime != 0) {
                    r0.translationX = r0.animateToX;
                    r0.translationY = r0.animateToY;
                    r0.clipBottom = r0.animateToClipBottom;
                    r0.clipTop = r0.animateToClipTop;
                    r0.clipHorizontal = r0.animateToClipHorizontal;
                    r0.scale = r0.animateToScale;
                    r0.animationStartTime = 0;
                    updateMinMax(r0.scale);
                    r0.zoomAnimation = false;
                    r0.useOvershootForScale = false;
                }
                if (!r0.scroller.isFinished() && r0.scroller.computeScrollOffset()) {
                    if (((float) r0.scroller.getStartX()) < r0.maxX && ((float) r0.scroller.getStartX()) > r0.minX) {
                        r0.translationX = (float) r0.scroller.getCurrX();
                    }
                    if (((float) r0.scroller.getStartY()) < r0.maxY && ((float) r0.scroller.getStartY()) > r0.minY) {
                        r0.translationY = (float) r0.scroller.getCurrY();
                    }
                    r0.containerView.invalidate();
                }
                f = r0.scale;
                f2 = r0.translationY;
                f3 = r0.translationX;
                f4 = r0.clipTop;
                f5 = r0.clipBottom;
                currentClipHorizontal3 = r0.clipHorizontal;
                if (!r0.moving) {
                    aty = r0.translationY;
                }
            }
            float translateX = f3;
            av = 0.0f;
            currentClipHorizontal = 1.0f;
            if (r0.photoAnimationInProgress != 3) {
                if (r0.scale != 1.0f || aty == -1.0f || r0.zoomAnimation) {
                    r0.photoBackgroundDrawable.setAlpha(255);
                } else {
                    currentClipHorizontal2 = ((float) getContainerViewHeight()) / 4.0f;
                    r0.photoBackgroundDrawable.setAlpha((int) Math.max(127.0f, (1.0f - (Math.min(Math.abs(aty), currentClipHorizontal2) / currentClipHorizontal2)) * 255.0f));
                }
                if (!r0.zoomAnimation && translateX > r0.maxX) {
                    currentClipHorizontal2 = Math.min(1.0f, (translateX - r0.maxX) / ((float) canvas.getWidth()));
                    av = currentClipHorizontal2 * 0.3f;
                    currentClipHorizontal = 1.0f - currentClipHorizontal2;
                    translateX = r0.maxX;
                }
            }
            boolean z = r0.aspectRatioFrameLayout != null && r0.aspectRatioFrameLayout.getVisibility() == 0;
            boolean drawTextureView = z;
            canvas.save();
            float sc = f - av;
            canvas2.translate(((float) (getContainerViewWidth() / 2)) + translateX, ((float) (getContainerViewHeight() / 2)) + f2);
            canvas2.scale(sc, sc);
            int bitmapWidth3 = r0.centerImage.getBitmapWidth();
            int bitmapHeight = r0.centerImage.getBitmapHeight();
            float f7;
            if (drawTextureView) {
                if (r0.textureUploaded != null) {
                    bitmapWidth = bitmapWidth3;
                    aty = ((float) bitmapWidth3) / ((float) bitmapHeight);
                    if (Math.abs(aty - (((float) r0.videoTextureView.getMeasuredWidth()) / ((float) r0.videoTextureView.getMeasuredHeight()))) > 0.01f) {
                        bitmapWidth2 = r0.videoTextureView.getMeasuredWidth();
                        float scale1 = aty;
                        bitmapHeight = r0.videoTextureView.getMeasuredHeight();
                        aty = Math.min(((float) getContainerViewHeight()) / ((float) bitmapHeight), ((float) getContainerViewWidth()) / ((float) bitmapWidth2));
                        bitmapWidth3 = (int) (((float) bitmapWidth2) * aty);
                        height = (int) (((float) bitmapHeight) * aty);
                        canvas2.clipRect(((float) ((-bitmapWidth3) / 2)) + (currentClipHorizontal3 / sc), ((float) ((-height) / 2)) + (f4 / sc), ((float) (bitmapWidth3 / 2)) - (currentClipHorizontal3 / sc), ((float) (height / 2)) - (f5 / sc));
                        if (!(drawTextureView && r0.textureUploaded && r0.videoCrossfadeStarted && r0.videoCrossfadeAlpha == 1.0f)) {
                            r0.centerImage.setAlpha(currentClipHorizontal);
                            r0.centerImage.setImageCoords((-bitmapWidth3) / 2, (-height) / 2, bitmapWidth3, height);
                            r0.centerImage.draw(canvas2);
                        }
                        if (drawTextureView) {
                            i = bitmapWidth3;
                            f6 = sc;
                        } else {
                            if (r0.videoCrossfadeStarted && r0.textureUploaded) {
                                r0.videoCrossfadeStarted = true;
                                r0.videoCrossfadeAlpha = 0.0f;
                                r0.videoCrossfadeAlphaLastTime = System.currentTimeMillis();
                            }
                            canvas2.translate((float) ((-bitmapWidth3) / 2), (float) ((-height) / 2));
                            r0.videoTextureView.setAlpha(r0.videoCrossfadeAlpha * currentClipHorizontal);
                            r0.aspectRatioFrameLayout.draw(canvas2);
                            if (r0.videoCrossfadeStarted || r0.videoCrossfadeAlpha >= 1.0f) {
                                f6 = sc;
                            } else {
                                long newUpdateTime = System.currentTimeMillis();
                                sc = newUpdateTime - r0.videoCrossfadeAlphaLastTime;
                                r0.videoCrossfadeAlphaLastTime = newUpdateTime;
                                r0.videoCrossfadeAlpha += ((float) sc) / 200.0f;
                                r0.containerView.invalidate();
                                if (r0.videoCrossfadeAlpha > 1.0f) {
                                    r0.videoCrossfadeAlpha = 1.0f;
                                }
                            }
                        }
                        canvas.restore();
                    }
                }
                bitmapWidth = bitmapWidth3;
                f7 = translateX;
            } else {
                bitmapWidth = bitmapWidth3;
                f7 = translateX;
            }
            bitmapWidth2 = bitmapWidth;
            aty = Math.min(((float) getContainerViewHeight()) / ((float) bitmapHeight), ((float) getContainerViewWidth()) / ((float) bitmapWidth2));
            bitmapWidth3 = (int) (((float) bitmapWidth2) * aty);
            height = (int) (((float) bitmapHeight) * aty);
            canvas2.clipRect(((float) ((-bitmapWidth3) / 2)) + (currentClipHorizontal3 / sc), ((float) ((-height) / 2)) + (f4 / sc), ((float) (bitmapWidth3 / 2)) - (currentClipHorizontal3 / sc), ((float) (height / 2)) - (f5 / sc));
            r0.centerImage.setAlpha(currentClipHorizontal);
            r0.centerImage.setImageCoords((-bitmapWidth3) / 2, (-height) / 2, bitmapWidth3, height);
            r0.centerImage.draw(canvas2);
            if (drawTextureView) {
                i = bitmapWidth3;
                f6 = sc;
            } else {
                if (r0.videoCrossfadeStarted) {
                }
                canvas2.translate((float) ((-bitmapWidth3) / 2), (float) ((-height) / 2));
                r0.videoTextureView.setAlpha(r0.videoCrossfadeAlpha * currentClipHorizontal);
                r0.aspectRatioFrameLayout.draw(canvas2);
                if (r0.videoCrossfadeStarted) {
                }
                f6 = sc;
            }
            canvas.restore();
        }
    }

    @Keep
    public float getVideoCrossfadeAlpha() {
        return this.videoCrossfadeAlpha;
    }

    @Keep
    public void setVideoCrossfadeAlpha(float value) {
        this.videoCrossfadeAlpha = value;
        this.containerView.invalidate();
    }

    private boolean checkPhotoAnimation() {
        if (this.photoAnimationInProgress != 0 && Math.abs(this.photoTransitionAnimationStartTime - System.currentTimeMillis()) >= 500) {
            if (this.photoAnimationEndRunnable != null) {
                this.photoAnimationEndRunnable.run();
                this.photoAnimationEndRunnable = null;
            }
            this.photoAnimationInProgress = 0;
        }
        if (this.photoAnimationInProgress != 0) {
            return true;
        }
        return false;
    }

    public long getOpenTime() {
        return this.openTime;
    }

    public long getCloseTime() {
        return this.closeTime;
    }

    public MessageObject getCurrentMessageObject() {
        return this.currentMessageObject;
    }

    private void onPhotoClosed(PlaceProviderObject object) {
        this.isVisible = false;
        this.currentProvider = null;
        this.disableShowCheck = false;
        releasePlayer();
        ArrayList<File> filesToDelete = new ArrayList();
        AndroidUtilities.runOnUIThread(new Runnable() {
            public void run() {
                if (SecretMediaViewer.this.currentThumb != null) {
                    SecretMediaViewer.this.currentThumb.release();
                    SecretMediaViewer.this.currentThumb = null;
                }
                SecretMediaViewer.this.centerImage.setImageBitmap((Bitmap) null);
                try {
                    if (SecretMediaViewer.this.windowView.getParent() != null) {
                        ((WindowManager) SecretMediaViewer.this.parentActivity.getSystemService("window")).removeView(SecretMediaViewer.this.windowView);
                    }
                } catch (Throwable e) {
                    FileLog.e(e);
                }
                SecretMediaViewer.this.isPhotoVisible = false;
            }
        }, 50);
    }

    private void updateMinMax(float scale) {
        int maxW = ((int) ((((float) this.centerImage.getImageWidth()) * scale) - ((float) getContainerViewWidth()))) / 2;
        int maxH = ((int) ((((float) this.centerImage.getImageHeight()) * scale) - ((float) getContainerViewHeight()))) / 2;
        if (maxW > 0) {
            this.minX = (float) (-maxW);
            this.maxX = (float) maxW;
        } else {
            this.maxX = 0.0f;
            this.minX = 0.0f;
        }
        if (maxH > 0) {
            this.minY = (float) (-maxH);
            this.maxY = (float) maxH;
            return;
        }
        this.maxY = 0.0f;
        this.minY = 0.0f;
    }

    private int getContainerViewWidth() {
        return this.containerView.getWidth();
    }

    private int getContainerViewHeight() {
        return this.containerView.getHeight();
    }

    private boolean processTouchEvent(MotionEvent ev) {
        if (this.photoAnimationInProgress == 0) {
            if (this.animationStartTime == 0) {
                if (ev.getPointerCount() == 1 && this.gestureDetector.onTouchEvent(ev) && this.doubleTap) {
                    this.doubleTap = false;
                    this.moving = false;
                    this.zooming = false;
                    checkMinMax(false);
                    return true;
                }
                float dx;
                if (ev.getActionMasked() != 0) {
                    if (ev.getActionMasked() != 5) {
                        float moveDx;
                        if (ev.getActionMasked() == 2) {
                            if (ev.getPointerCount() == 2 && !this.draggingDown && this.zooming) {
                                this.discardTap = true;
                                this.scale = (((float) Math.hypot((double) (ev.getX(1) - ev.getX(0)), (double) (ev.getY(1) - ev.getY(0)))) / this.pinchStartDistance) * this.pinchStartScale;
                                this.translationX = (this.pinchCenterX - ((float) (getContainerViewWidth() / 2))) - (((this.pinchCenterX - ((float) (getContainerViewWidth() / 2))) - this.pinchStartX) * (this.scale / this.pinchStartScale));
                                this.translationY = (this.pinchCenterY - ((float) (getContainerViewHeight() / 2))) - (((this.pinchCenterY - ((float) (getContainerViewHeight() / 2))) - this.pinchStartY) * (this.scale / this.pinchStartScale));
                                updateMinMax(this.scale);
                                this.containerView.invalidate();
                            } else if (ev.getPointerCount() == 1) {
                                if (this.velocityTracker != null) {
                                    this.velocityTracker.addMovement(ev);
                                }
                                dx = Math.abs(ev.getX() - this.moveStartX);
                                float dy = Math.abs(ev.getY() - this.dragY);
                                if (dx > ((float) AndroidUtilities.dp(3.0f)) || dy > ((float) AndroidUtilities.dp(3.0f))) {
                                    this.discardTap = true;
                                }
                                if (this.canDragDown && !this.draggingDown && this.scale == 1.0f && dy >= ((float) AndroidUtilities.dp(30.0f)) && dy / 2.0f > dx) {
                                    this.draggingDown = true;
                                    this.moving = false;
                                    this.dragY = ev.getY();
                                    if (this.isActionBarVisible) {
                                        toggleActionBar(false, true);
                                    }
                                    return true;
                                } else if (this.draggingDown) {
                                    this.translationY = ev.getY() - this.dragY;
                                    this.containerView.invalidate();
                                } else if (this.invalidCoords || this.animationStartTime != 0) {
                                    this.invalidCoords = false;
                                    this.moveStartX = ev.getX();
                                    this.moveStartY = ev.getY();
                                } else {
                                    moveDx = this.moveStartX - ev.getX();
                                    float moveDy = this.moveStartY - ev.getY();
                                    if (this.moving || ((this.scale == 1.0f && Math.abs(moveDy) + ((float) AndroidUtilities.dp(12.0f)) < Math.abs(moveDx)) || this.scale != 1.0f)) {
                                        if (!this.moving) {
                                            moveDx = 0.0f;
                                            moveDy = 0.0f;
                                            this.moving = true;
                                            this.canDragDown = false;
                                        }
                                        this.moveStartX = ev.getX();
                                        this.moveStartY = ev.getY();
                                        updateMinMax(this.scale);
                                        if (this.translationX < this.minX || this.translationX > this.maxX) {
                                            moveDx /= 3.0f;
                                        }
                                        if (this.maxY == 0.0f && this.minY == 0.0f) {
                                            if (this.translationY - moveDy < this.minY) {
                                                this.translationY = this.minY;
                                                moveDy = 0.0f;
                                            } else if (this.translationY - moveDy > this.maxY) {
                                                this.translationY = this.maxY;
                                                moveDy = 0.0f;
                                            }
                                        } else if (this.translationY < this.minY || this.translationY > this.maxY) {
                                            moveDy /= 3.0f;
                                        }
                                        this.translationX -= moveDx;
                                        if (this.scale != 1.0f) {
                                            this.translationY -= moveDy;
                                        }
                                        this.containerView.invalidate();
                                    }
                                }
                            }
                        } else if (ev.getActionMasked() == 3 || ev.getActionMasked() == 1 || ev.getActionMasked() == 6) {
                            if (this.zooming) {
                                this.invalidCoords = true;
                                if (this.scale < 1.0f) {
                                    updateMinMax(1.0f);
                                    animateTo(1.0f, 0.0f, 0.0f, true);
                                } else if (this.scale > 3.0f) {
                                    dx = (this.pinchCenterX - ((float) (getContainerViewWidth() / 2))) - (((this.pinchCenterX - ((float) (getContainerViewWidth() / 2))) - this.pinchStartX) * (3.0f / this.pinchStartScale));
                                    moveDx = (this.pinchCenterY - ((float) (getContainerViewHeight() / 2))) - (((this.pinchCenterY - ((float) (getContainerViewHeight() / 2))) - this.pinchStartY) * (3.0f / this.pinchStartScale));
                                    updateMinMax(3.0f);
                                    if (dx < this.minX) {
                                        dx = this.minX;
                                    } else if (dx > this.maxX) {
                                        dx = this.maxX;
                                    }
                                    if (moveDx < this.minY) {
                                        moveDx = this.minY;
                                    } else if (moveDx > this.maxY) {
                                        moveDx = this.maxY;
                                    }
                                    animateTo(3.0f, dx, moveDx, true);
                                } else {
                                    checkMinMax(true);
                                }
                                this.zooming = false;
                            } else if (this.draggingDown) {
                                if (Math.abs(this.dragY - ev.getY()) > ((float) getContainerViewHeight()) / 6.0f) {
                                    closePhoto(true, false);
                                } else {
                                    animateTo(1.0f, 0.0f, 0.0f, false);
                                }
                                this.draggingDown = false;
                            } else if (this.moving) {
                                dx = this.translationX;
                                moveDx = this.translationY;
                                updateMinMax(this.scale);
                                this.moving = false;
                                this.canDragDown = true;
                                if (this.velocityTracker != null && this.scale == 1.0f) {
                                    this.velocityTracker.computeCurrentVelocity(1000);
                                }
                                if (this.translationX < this.minX) {
                                    dx = this.minX;
                                } else if (this.translationX > this.maxX) {
                                    dx = this.maxX;
                                }
                                if (this.translationY < this.minY) {
                                    moveDx = this.minY;
                                } else if (this.translationY > this.maxY) {
                                    moveDx = this.maxY;
                                }
                                animateTo(this.scale, dx, moveDx, false);
                            }
                        }
                        return false;
                    }
                }
                this.discardTap = false;
                if (!this.scroller.isFinished()) {
                    this.scroller.abortAnimation();
                }
                if (!this.draggingDown) {
                    if (ev.getPointerCount() == 2) {
                        this.pinchStartDistance = (float) Math.hypot((double) (ev.getX(1) - ev.getX(0)), (double) (ev.getY(1) - ev.getY(0)));
                        this.pinchStartScale = this.scale;
                        this.pinchCenterX = (ev.getX(0) + ev.getX(1)) / 2.0f;
                        this.pinchCenterY = (ev.getY(0) + ev.getY(1)) / 2.0f;
                        this.pinchStartX = this.translationX;
                        this.pinchStartY = this.translationY;
                        this.zooming = true;
                        this.moving = false;
                        if (this.velocityTracker != null) {
                            this.velocityTracker.clear();
                        }
                    } else if (ev.getPointerCount() == 1) {
                        this.moveStartX = ev.getX();
                        dx = ev.getY();
                        this.moveStartY = dx;
                        this.dragY = dx;
                        this.draggingDown = false;
                        this.canDragDown = true;
                        if (this.velocityTracker != null) {
                            this.velocityTracker.clear();
                        }
                    }
                }
                return false;
            }
        }
        return false;
    }

    private void checkMinMax(boolean zoom) {
        float moveToX = this.translationX;
        float moveToY = this.translationY;
        updateMinMax(this.scale);
        if (this.translationX < this.minX) {
            moveToX = this.minX;
        } else if (this.translationX > this.maxX) {
            moveToX = this.maxX;
        }
        if (this.translationY < this.minY) {
            moveToY = this.minY;
        } else if (this.translationY > this.maxY) {
            moveToY = this.maxY;
        }
        animateTo(this.scale, moveToX, moveToY, zoom);
    }

    private void animateTo(float newScale, float newTx, float newTy, boolean isZoom) {
        animateTo(newScale, newTx, newTy, isZoom, Callback.DEFAULT_SWIPE_ANIMATION_DURATION);
    }

    private void animateTo(float newScale, float newTx, float newTy, boolean isZoom, int duration) {
        if (this.scale != newScale || this.translationX != newTx || this.translationY != newTy) {
            this.zoomAnimation = isZoom;
            this.animateToScale = newScale;
            this.animateToX = newTx;
            this.animateToY = newTy;
            this.animationStartTime = System.currentTimeMillis();
            this.imageMoveAnimation = new AnimatorSet();
            this.imageMoveAnimation.playTogether(new Animator[]{ObjectAnimator.ofFloat(this, "animationValue", new float[]{0.0f, 1.0f})});
            this.imageMoveAnimation.setInterpolator(this.interpolator);
            this.imageMoveAnimation.setDuration((long) duration);
            this.imageMoveAnimation.addListener(new AnimatorListenerAdapter() {
                public void onAnimationEnd(Animator animation) {
                    SecretMediaViewer.this.imageMoveAnimation = null;
                    SecretMediaViewer.this.containerView.invalidate();
                }
            });
            this.imageMoveAnimation.start();
        }
    }

    @Keep
    public void setAnimationValue(float value) {
        this.animationValue = value;
        this.containerView.invalidate();
    }

    @Keep
    public float getAnimationValue() {
        return this.animationValue;
    }

    public boolean onDown(MotionEvent e) {
        return false;
    }

    public void onShowPress(MotionEvent e) {
    }

    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return false;
    }

    public void onLongPress(MotionEvent e) {
    }

    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        if (this.scale != 1.0f) {
            this.scroller.abortAnimation();
            this.scroller.fling(Math.round(this.translationX), Math.round(this.translationY), Math.round(velocityX), Math.round(velocityY), (int) this.minX, (int) this.maxX, (int) this.minY, (int) this.maxY);
            this.containerView.postInvalidate();
        }
        return false;
    }

    public boolean onSingleTapConfirmed(MotionEvent e) {
        if (this.discardTap) {
            return false;
        }
        toggleActionBar(this.isActionBarVisible ^ true, true);
        return true;
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean onDoubleTap(android.view.MotionEvent r9) {
        /*
        r8 = this;
        r0 = r8.scale;
        r1 = 1065353216; // 0x3f800000 float:1.0 double:5.263544247E-315;
        r0 = (r0 > r1 ? 1 : (r0 == r1 ? 0 : -1));
        r2 = 0;
        r3 = 0;
        if (r0 != 0) goto L_0x0017;
    L_0x000a:
        r0 = r8.translationY;
        r0 = (r0 > r3 ? 1 : (r0 == r3 ? 0 : -1));
        if (r0 != 0) goto L_0x0016;
    L_0x0010:
        r0 = r8.translationX;
        r0 = (r0 > r3 ? 1 : (r0 == r3 ? 0 : -1));
        if (r0 == 0) goto L_0x0017;
    L_0x0016:
        return r2;
    L_0x0017:
        r4 = r8.animationStartTime;
        r6 = 0;
        r0 = (r4 > r6 ? 1 : (r4 == r6 ? 0 : -1));
        if (r0 != 0) goto L_0x009f;
    L_0x001f:
        r0 = r8.photoAnimationInProgress;
        if (r0 == 0) goto L_0x0025;
    L_0x0023:
        goto L_0x009f;
    L_0x0025:
        r0 = r8.scale;
        r0 = (r0 > r1 ? 1 : (r0 == r1 ? 0 : -1));
        r2 = 1;
        if (r0 != 0) goto L_0x0099;
    L_0x002c:
        r0 = r9.getX();
        r1 = r8.getContainerViewWidth();
        r1 = r1 / 2;
        r1 = (float) r1;
        r0 = r0 - r1;
        r1 = r9.getX();
        r3 = r8.getContainerViewWidth();
        r3 = r3 / 2;
        r3 = (float) r3;
        r1 = r1 - r3;
        r3 = r8.translationX;
        r1 = r1 - r3;
        r3 = r8.scale;
        r4 = 1077936128; // 0x40400000 float:3.0 double:5.325712093E-315;
        r3 = r4 / r3;
        r1 = r1 * r3;
        r0 = r0 - r1;
        r1 = r9.getY();
        r3 = r8.getContainerViewHeight();
        r3 = r3 / 2;
        r3 = (float) r3;
        r1 = r1 - r3;
        r3 = r9.getY();
        r5 = r8.getContainerViewHeight();
        r5 = r5 / 2;
        r5 = (float) r5;
        r3 = r3 - r5;
        r5 = r8.translationY;
        r3 = r3 - r5;
        r5 = r8.scale;
        r5 = r4 / r5;
        r3 = r3 * r5;
        r1 = r1 - r3;
        r8.updateMinMax(r4);
        r3 = r8.minX;
        r3 = (r0 > r3 ? 1 : (r0 == r3 ? 0 : -1));
        if (r3 >= 0) goto L_0x007c;
    L_0x0079:
        r0 = r8.minX;
        goto L_0x0084;
    L_0x007c:
        r3 = r8.maxX;
        r3 = (r0 > r3 ? 1 : (r0 == r3 ? 0 : -1));
        if (r3 <= 0) goto L_0x0084;
    L_0x0082:
        r0 = r8.maxX;
    L_0x0084:
        r3 = r8.minY;
        r3 = (r1 > r3 ? 1 : (r1 == r3 ? 0 : -1));
        if (r3 >= 0) goto L_0x008d;
    L_0x008a:
        r1 = r8.minY;
        goto L_0x0095;
    L_0x008d:
        r3 = r8.maxY;
        r3 = (r1 > r3 ? 1 : (r1 == r3 ? 0 : -1));
        if (r3 <= 0) goto L_0x0095;
    L_0x0093:
        r1 = r8.maxY;
    L_0x0095:
        r8.animateTo(r4, r0, r1, r2);
        goto L_0x009c;
    L_0x0099:
        r8.animateTo(r1, r3, r3, r2);
    L_0x009c:
        r8.doubleTap = r2;
        return r2;
    L_0x009f:
        return r2;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.telegram.ui.SecretMediaViewer.onDoubleTap(android.view.MotionEvent):boolean");
    }

    public boolean onDoubleTapEvent(MotionEvent e) {
        return false;
    }

    private boolean scaleToFill() {
        return false;
    }
}
