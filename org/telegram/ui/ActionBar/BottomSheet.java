package org.telegram.ui.ActionBar;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface.OnClickListener;
import android.content.res.Configuration;
import android.graphics.Canvas;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffColorFilter;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.support.v4.view.NestedScrollingParent;
import android.support.v4.view.NestedScrollingParentHelper;
import android.text.TextUtils.TruncateAt;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.View.OnApplyWindowInsetsListener;
import android.view.View.OnTouchListener;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowInsets;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.TextView;
import java.util.ArrayList;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.UserConfig;
import org.telegram.messenger.beta.R;
import org.telegram.ui.Components.LayoutHelper;

public class BottomSheet extends Dialog {
    protected static int backgroundPaddingLeft;
    protected static int backgroundPaddingTop;
    private AccelerateInterpolator accelerateInterpolator = new AccelerateInterpolator();
    private boolean allowCustomAnimation = true;
    private boolean allowDrawContent = true;
    private boolean allowNestedScroll = true;
    private boolean applyBottomPadding = true;
    private boolean applyTopPadding = true;
    protected ColorDrawable backDrawable = new ColorDrawable(Theme.ACTION_BAR_VIDEO_EDIT_COLOR);
    protected ContainerView container;
    protected ViewGroup containerView;
    protected int currentAccount = UserConfig.selectedAccount;
    protected AnimatorSet currentSheetAnimation;
    private View customView;
    private DecelerateInterpolator decelerateInterpolator = new DecelerateInterpolator();
    private BottomSheetDelegateInterface delegate;
    private boolean dismissed;
    private boolean focusable;
    protected boolean fullWidth;
    private int[] itemIcons;
    private ArrayList<BottomSheetCell> itemViews = new ArrayList();
    private CharSequence[] items;
    private WindowInsets lastInsets;
    private int layoutCount;
    protected View nestedScrollChild;
    private OnClickListener onClickListener;
    private Drawable shadowDrawable;
    private boolean showWithoutAnimation;
    private Runnable startAnimationRunnable;
    private int tag;
    private CharSequence title;
    private int touchSlop;
    private boolean useFastDismiss;
    private boolean useHardwareLayer = true;

    public static class BottomSheetCell extends FrameLayout {
        private ImageView imageView;
        private TextView textView;

        public BottomSheetCell(Context context, int type) {
            super(context);
            setBackgroundDrawable(Theme.getSelectorDrawable(false));
            setPadding(AndroidUtilities.dp(16.0f), 0, AndroidUtilities.dp(16.0f), 0);
            this.imageView = new ImageView(context);
            this.imageView.setScaleType(ScaleType.CENTER);
            this.imageView.setColorFilter(new PorterDuffColorFilter(Theme.getColor(Theme.key_dialogIcon), Mode.MULTIPLY));
            int i = 3;
            addView(this.imageView, LayoutHelper.createFrame(24, 24, (LocaleController.isRTL ? 5 : 3) | 16));
            this.textView = new TextView(context);
            this.textView.setLines(1);
            this.textView.setSingleLine(true);
            this.textView.setGravity(1);
            this.textView.setEllipsize(TruncateAt.END);
            if (type == 0) {
                this.textView.setTextColor(Theme.getColor(Theme.key_dialogTextBlack));
                this.textView.setTextSize(1, 16.0f);
                View view = this.textView;
                if (LocaleController.isRTL) {
                    i = 5;
                }
                addView(view, LayoutHelper.createFrame(-2, -2, i | 16));
            } else if (type == 1) {
                this.textView.setGravity(17);
                this.textView.setTextColor(Theme.getColor(Theme.key_dialogTextBlack));
                this.textView.setTextSize(1, 14.0f);
                this.textView.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
                addView(this.textView, LayoutHelper.createFrame(-1, -1.0f));
            }
        }

        protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
            super.onMeasure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(48.0f), 1073741824));
        }

        public void setTextColor(int color) {
            this.textView.setTextColor(color);
        }

        public void setGravity(int gravity) {
            this.textView.setGravity(gravity);
        }

        public void setTextAndIcon(CharSequence text, int icon) {
            this.textView.setText(text);
            if (icon != 0) {
                this.imageView.setImageResource(icon);
                this.imageView.setVisibility(0);
                this.textView.setPadding(LocaleController.isRTL ? 0 : AndroidUtilities.dp(56.0f), 0, LocaleController.isRTL ? AndroidUtilities.dp(56.0f) : 0, 0);
                return;
            }
            this.imageView.setVisibility(4);
            this.textView.setPadding(0, 0, 0, 0);
        }
    }

    public interface BottomSheetDelegateInterface {
        boolean canDismiss();

        void onOpenAnimationEnd();

        void onOpenAnimationStart();
    }

    public static class Builder {
        private BottomSheet bottomSheet;

        public Builder(Context context) {
            this.bottomSheet = new BottomSheet(context, false);
        }

        public Builder(Context context, boolean needFocus) {
            this.bottomSheet = new BottomSheet(context, needFocus);
        }

        public Builder setItems(CharSequence[] items, OnClickListener onClickListener) {
            this.bottomSheet.items = items;
            this.bottomSheet.onClickListener = onClickListener;
            return this;
        }

        public Builder setItems(CharSequence[] items, int[] icons, OnClickListener onClickListener) {
            this.bottomSheet.items = items;
            this.bottomSheet.itemIcons = icons;
            this.bottomSheet.onClickListener = onClickListener;
            return this;
        }

        public Builder setCustomView(View view) {
            this.bottomSheet.customView = view;
            return this;
        }

        public Builder setTitle(CharSequence title) {
            this.bottomSheet.title = title;
            return this;
        }

        public BottomSheet create() {
            return this.bottomSheet;
        }

        public BottomSheet show() {
            this.bottomSheet.show();
            return this.bottomSheet;
        }

        public Builder setTag(int tag) {
            this.bottomSheet.tag = tag;
            return this;
        }

        public Builder setUseHardwareLayer(boolean value) {
            this.bottomSheet.useHardwareLayer = value;
            return this;
        }

        public Builder setDelegate(BottomSheetDelegate delegate) {
            this.bottomSheet.setDelegate(delegate);
            return this;
        }

        public Builder setApplyTopPadding(boolean value) {
            this.bottomSheet.applyTopPadding = value;
            return this;
        }

        public Builder setApplyBottomPadding(boolean value) {
            this.bottomSheet.applyBottomPadding = value;
            return this;
        }

        public BottomSheet setUseFullWidth(boolean value) {
            this.bottomSheet.fullWidth = value;
            return this.bottomSheet;
        }
    }

    public static class BottomSheetDelegate implements BottomSheetDelegateInterface {
        public void onOpenAnimationStart() {
        }

        public void onOpenAnimationEnd() {
        }

        public boolean canDismiss() {
            return true;
        }
    }

    protected class ContainerView extends FrameLayout implements NestedScrollingParent {
        private AnimatorSet currentAnimation = null;
        private boolean maybeStartTracking = false;
        private NestedScrollingParentHelper nestedScrollingParentHelper = new NestedScrollingParentHelper(this);
        private boolean startedTracking = false;
        private int startedTrackingPointerId = -1;
        private int startedTrackingX;
        private int startedTrackingY;
        private VelocityTracker velocityTracker = null;

        protected void onLayout(boolean r1, int r2, int r3, int r4, int r5) {
            /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.DecodeException: Load method exception in method: org.telegram.ui.ActionBar.BottomSheet.ContainerView.onLayout(boolean, int, int, int, int):void
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
            r0 = r17;
            r1 = org.telegram.ui.ActionBar.BottomSheet.this;
            r1.layoutCount = r1.layoutCount - 1;
            r1 = org.telegram.ui.ActionBar.BottomSheet.this;
            r1 = r1.containerView;
            r7 = 21;
            if (r1 == 0) goto L_0x0083;
        L_0x000f:
            r1 = org.telegram.ui.ActionBar.BottomSheet.this;
            r1 = r1.lastInsets;
            if (r1 == 0) goto L_0x0034;
        L_0x0017:
            r1 = android.os.Build.VERSION.SDK_INT;
            if (r1 < r7) goto L_0x0034;
        L_0x001b:
            r1 = org.telegram.ui.ActionBar.BottomSheet.this;
            r1 = r1.lastInsets;
            r1 = r1.getSystemWindowInsetLeft();
            r1 = r19 + r1;
            r2 = org.telegram.ui.ActionBar.BottomSheet.this;
            r2 = r2.lastInsets;
            r2 = r2.getSystemWindowInsetRight();
            r2 = r21 - r2;
            goto L_0x0038;
        L_0x0034:
            r1 = r19;
            r2 = r21;
        L_0x0038:
            r3 = r22 - r20;
            r4 = org.telegram.ui.ActionBar.BottomSheet.this;
            r4 = r4.containerView;
            r4 = r4.getMeasuredHeight();
            r3 = r3 - r4;
            r4 = r2 - r1;
            r5 = org.telegram.ui.ActionBar.BottomSheet.this;
            r5 = r5.containerView;
            r5 = r5.getMeasuredWidth();
            r4 = r4 - r5;
            r4 = r4 / 2;
            r5 = org.telegram.ui.ActionBar.BottomSheet.this;
            r5 = r5.lastInsets;
            if (r5 == 0) goto L_0x0067;
        L_0x0058:
            r5 = android.os.Build.VERSION.SDK_INT;
            if (r5 < r7) goto L_0x0067;
        L_0x005c:
            r5 = org.telegram.ui.ActionBar.BottomSheet.this;
            r5 = r5.lastInsets;
            r5 = r5.getSystemWindowInsetLeft();
            r4 = r4 + r5;
        L_0x0067:
            r5 = org.telegram.ui.ActionBar.BottomSheet.this;
            r5 = r5.containerView;
            r6 = org.telegram.ui.ActionBar.BottomSheet.this;
            r6 = r6.containerView;
            r6 = r6.getMeasuredWidth();
            r6 = r6 + r4;
            r8 = org.telegram.ui.ActionBar.BottomSheet.this;
            r8 = r8.containerView;
            r8 = r8.getMeasuredHeight();
            r8 = r8 + r3;
            r5.layout(r4, r3, r6, r8);
            r8 = r1;
            r9 = r2;
            goto L_0x0087;
        L_0x0083:
            r8 = r19;
            r9 = r21;
        L_0x0087:
            r10 = r17.getChildCount();
            r1 = 0;
            r11 = r1;
            if (r11 >= r10) goto L_0x013f;
        L_0x008f:
            r12 = r0.getChildAt(r11);
            r1 = r12.getVisibility();
            r2 = 8;
            if (r1 == r2) goto L_0x0139;
        L_0x009b:
            r1 = org.telegram.ui.ActionBar.BottomSheet.this;
            r1 = r1.containerView;
            if (r12 != r1) goto L_0x00a3;
        L_0x00a1:
            goto L_0x0139;
        L_0x00a3:
            r1 = org.telegram.ui.ActionBar.BottomSheet.this;
            r2 = r12;
            r3 = r8;
            r4 = r20;
            r5 = r9;
            r6 = r22;
            r1 = r1.onCustomLayout(r2, r3, r4, r5, r6);
            if (r1 != 0) goto L_0x0139;
        L_0x00b2:
            r1 = r12.getLayoutParams();
            r1 = (android.widget.FrameLayout.LayoutParams) r1;
            r2 = r12.getMeasuredWidth();
            r3 = r12.getMeasuredHeight();
            r4 = r1.gravity;
            r5 = -1;
            if (r4 != r5) goto L_0x00c7;
        L_0x00c5:
            r4 = 51;
        L_0x00c7:
            r5 = r4 & 7;
            r6 = r4 & 112;
            r13 = r5 & 7;
            r7 = 1;
            if (r13 == r7) goto L_0x00dc;
        L_0x00d0:
            r7 = 5;
            if (r13 == r7) goto L_0x00d6;
        L_0x00d3:
            r7 = r1.leftMargin;
            goto L_0x00e8;
        L_0x00d6:
            r7 = r9 - r2;
            r13 = r1.rightMargin;
            r7 = r7 - r13;
            goto L_0x00e8;
        L_0x00dc:
            r7 = r9 - r8;
            r7 = r7 - r2;
            r7 = r7 / 2;
            r13 = r1.leftMargin;
            r7 = r7 + r13;
            r13 = r1.rightMargin;
            r7 = r7 - r13;
            r13 = 16;
            if (r6 == r13) goto L_0x0105;
            r13 = 48;
            if (r6 == r13) goto L_0x0101;
            r13 = 80;
            if (r6 == r13) goto L_0x00f9;
            r13 = r1.topMargin;
            r14 = r4;
            goto L_0x0113;
            r13 = r22 - r20;
            r13 = r13 - r3;
            r14 = r4;
            r4 = r1.bottomMargin;
            r13 = r13 - r4;
            goto L_0x0113;
            r14 = r4;
            r13 = r1.topMargin;
            goto L_0x0113;
            r14 = r4;
            r4 = r22 - r20;
            r4 = r4 - r3;
            r4 = r4 / 2;
            r13 = r1.topMargin;
            r4 = r4 + r13;
            r13 = r1.bottomMargin;
            r13 = r4 - r13;
            r4 = r13;
            r13 = org.telegram.ui.ActionBar.BottomSheet.this;
            r13 = r13.lastInsets;
            if (r13 == 0) goto L_0x012f;
            r13 = android.os.Build.VERSION.SDK_INT;
            r15 = r1;
            r1 = 21;
            if (r13 < r1) goto L_0x0132;
            r13 = org.telegram.ui.ActionBar.BottomSheet.this;
            r13 = r13.lastInsets;
            r13 = r13.getSystemWindowInsetLeft();
            r7 = r7 + r13;
            goto L_0x0132;
            r15 = r1;
            r1 = 21;
            r13 = r7 + r2;
            r1 = r4 + r3;
            r12.layout(r7, r4, r13, r1);
        L_0x0139:
            r1 = r11 + 1;
            r7 = 21;
            goto L_0x008c;
        L_0x013f:
            r1 = org.telegram.ui.ActionBar.BottomSheet.this;
            r1 = r1.layoutCount;
            if (r1 != 0) goto L_0x0167;
            r1 = org.telegram.ui.ActionBar.BottomSheet.this;
            r1 = r1.startAnimationRunnable;
            if (r1 == 0) goto L_0x0167;
            r1 = org.telegram.ui.ActionBar.BottomSheet.this;
            r1 = r1.startAnimationRunnable;
            org.telegram.messenger.AndroidUtilities.cancelRunOnUIThread(r1);
            r1 = org.telegram.ui.ActionBar.BottomSheet.this;
            r1 = r1.startAnimationRunnable;
            r1.run();
            r1 = org.telegram.ui.ActionBar.BottomSheet.this;
            r2 = 0;
            r1.startAnimationRunnable = r2;
            return;
            */
            throw new UnsupportedOperationException("Method not decompiled: org.telegram.ui.ActionBar.BottomSheet.ContainerView.onLayout(boolean, int, int, int, int):void");
        }

        public ContainerView(Context context) {
            super(context);
        }

        public boolean onStartNestedScroll(View child, View target, int nestedScrollAxes) {
            return (BottomSheet.this.nestedScrollChild == null || child == BottomSheet.this.nestedScrollChild) && !BottomSheet.this.dismissed && BottomSheet.this.allowNestedScroll && nestedScrollAxes == 2 && !BottomSheet.this.canDismissWithSwipe();
        }

        public void onNestedScrollAccepted(View child, View target, int nestedScrollAxes) {
            this.nestedScrollingParentHelper.onNestedScrollAccepted(child, target, nestedScrollAxes);
            if (!BottomSheet.this.dismissed) {
                if (BottomSheet.this.allowNestedScroll) {
                    cancelCurrentAnimation();
                }
            }
        }

        public void onStopNestedScroll(View target) {
            this.nestedScrollingParentHelper.onStopNestedScroll(target);
            if (!BottomSheet.this.dismissed) {
                if (BottomSheet.this.allowNestedScroll) {
                    float currentTranslation = BottomSheet.this.containerView.getTranslationY();
                    checkDismiss(0.0f, 0.0f);
                }
            }
        }

        public void onNestedScroll(View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed) {
            if (!BottomSheet.this.dismissed) {
                if (BottomSheet.this.allowNestedScroll) {
                    cancelCurrentAnimation();
                    if (dyUnconsumed != 0) {
                        float currentTranslation = BottomSheet.this.containerView.getTranslationY() - ((float) dyUnconsumed);
                        if (currentTranslation < 0.0f) {
                            currentTranslation = 0.0f;
                        }
                        BottomSheet.this.containerView.setTranslationY(currentTranslation);
                    }
                }
            }
        }

        public void onNestedPreScroll(View target, int dx, int dy, int[] consumed) {
            if (!BottomSheet.this.dismissed) {
                if (BottomSheet.this.allowNestedScroll) {
                    cancelCurrentAnimation();
                    float currentTranslation = BottomSheet.this.containerView.getTranslationY();
                    if (currentTranslation > 0.0f && dy > 0) {
                        currentTranslation -= (float) dy;
                        consumed[1] = dy;
                        if (currentTranslation < 0.0f) {
                            currentTranslation = 0.0f;
                        }
                        BottomSheet.this.containerView.setTranslationY(currentTranslation);
                    }
                }
            }
        }

        public boolean onNestedFling(View target, float velocityX, float velocityY, boolean consumed) {
            return false;
        }

        public boolean onNestedPreFling(View target, float velocityX, float velocityY) {
            return false;
        }

        public int getNestedScrollAxes() {
            return this.nestedScrollingParentHelper.getNestedScrollAxes();
        }

        private void checkDismiss(float velX, float velY) {
            float translationY = BottomSheet.this.containerView.getTranslationY();
            boolean backAnimation = (translationY < AndroidUtilities.getPixelsInCM(0.8f, false) && (velY < 3500.0f || Math.abs(velY) < Math.abs(velX))) || (velY < 0.0f && Math.abs(velY) >= 3500.0f);
            if (backAnimation) {
                this.currentAnimation = new AnimatorSet();
                AnimatorSet animatorSet = this.currentAnimation;
                Animator[] animatorArr = new Animator[1];
                animatorArr[0] = ObjectAnimator.ofFloat(BottomSheet.this.containerView, "translationY", new float[]{0.0f});
                animatorSet.playTogether(animatorArr);
                this.currentAnimation.setDuration((long) ((int) (150.0f * (translationY / AndroidUtilities.getPixelsInCM(0.8f, false)))));
                this.currentAnimation.setInterpolator(new DecelerateInterpolator());
                this.currentAnimation.addListener(new AnimatorListenerAdapter() {
                    public void onAnimationEnd(Animator animation) {
                        if (ContainerView.this.currentAnimation != null && ContainerView.this.currentAnimation.equals(animation)) {
                            ContainerView.this.currentAnimation = null;
                        }
                    }
                });
                this.currentAnimation.start();
                return;
            }
            boolean allowOld = BottomSheet.this.allowCustomAnimation;
            BottomSheet.this.allowCustomAnimation = false;
            BottomSheet.this.useFastDismiss = true;
            BottomSheet.this.dismiss();
            BottomSheet.this.allowCustomAnimation = allowOld;
        }

        private void cancelCurrentAnimation() {
            if (this.currentAnimation != null) {
                this.currentAnimation.cancel();
                this.currentAnimation = null;
            }
        }

        public boolean onTouchEvent(MotionEvent ev) {
            boolean z = false;
            if (BottomSheet.this.dismissed) {
                return false;
            }
            if (BottomSheet.this.onContainerTouchEvent(ev)) {
                return true;
            }
            if (BottomSheet.this.canDismissWithTouchOutside() && ev != null && ((ev.getAction() == 0 || ev.getAction() == 2) && !this.startedTracking && !this.maybeStartTracking && ev.getPointerCount() == 1)) {
                this.startedTrackingX = (int) ev.getX();
                this.startedTrackingY = (int) ev.getY();
                if (this.startedTrackingY >= BottomSheet.this.containerView.getTop() && this.startedTrackingX >= BottomSheet.this.containerView.getLeft()) {
                    if (this.startedTrackingX <= BottomSheet.this.containerView.getRight()) {
                        this.startedTrackingPointerId = ev.getPointerId(0);
                        this.maybeStartTracking = true;
                        cancelCurrentAnimation();
                        if (this.velocityTracker != null) {
                            this.velocityTracker.clear();
                        }
                    }
                }
                BottomSheet.this.dismiss();
                return true;
            } else if (ev != null && ev.getAction() == 2 && ev.getPointerId(0) == this.startedTrackingPointerId) {
                if (this.velocityTracker == null) {
                    this.velocityTracker = VelocityTracker.obtain();
                }
                dx = (float) Math.abs((int) (ev.getX() - ((float) this.startedTrackingX)));
                float dy = (float) (((int) ev.getY()) - this.startedTrackingY);
                this.velocityTracker.addMovement(ev);
                if (this.maybeStartTracking && !this.startedTracking && dy > 0.0f && dy / 3.0f > Math.abs(dx) && Math.abs(dy) >= ((float) BottomSheet.this.touchSlop)) {
                    this.startedTrackingY = (int) ev.getY();
                    this.maybeStartTracking = false;
                    this.startedTracking = true;
                    requestDisallowInterceptTouchEvent(true);
                } else if (this.startedTracking) {
                    float translationY = BottomSheet.this.containerView.getTranslationY() + dy;
                    if (translationY < 0.0f) {
                        translationY = 0.0f;
                    }
                    BottomSheet.this.containerView.setTranslationY(translationY);
                    this.startedTrackingY = (int) ev.getY();
                }
            } else if (ev == null || (ev != null && ev.getPointerId(0) == this.startedTrackingPointerId && (ev.getAction() == 3 || ev.getAction() == 1 || ev.getAction() == 6))) {
                if (this.velocityTracker == null) {
                    this.velocityTracker = VelocityTracker.obtain();
                }
                this.velocityTracker.computeCurrentVelocity(1000);
                dx = BottomSheet.this.containerView.getTranslationY();
                if (!this.startedTracking) {
                    if (dx == 0.0f) {
                        this.maybeStartTracking = false;
                        this.startedTracking = false;
                        if (this.velocityTracker != null) {
                            this.velocityTracker.recycle();
                            this.velocityTracker = null;
                        }
                        this.startedTrackingPointerId = -1;
                    }
                }
                checkDismiss(this.velocityTracker.getXVelocity(), this.velocityTracker.getYVelocity());
                this.startedTracking = false;
                if (this.velocityTracker != null) {
                    this.velocityTracker.recycle();
                    this.velocityTracker = null;
                }
                this.startedTrackingPointerId = -1;
            }
            if (!this.startedTracking) {
                if (BottomSheet.this.canDismissWithSwipe()) {
                    return z;
                }
            }
            z = true;
            return z;
        }

        protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
            int widthSpec;
            int width = MeasureSpec.getSize(widthMeasureSpec);
            int height = MeasureSpec.getSize(heightMeasureSpec);
            if (BottomSheet.this.lastInsets != null && VERSION.SDK_INT >= 21) {
                height -= BottomSheet.this.lastInsets.getSystemWindowInsetBottom();
            }
            setMeasuredDimension(width, height);
            if (BottomSheet.this.lastInsets != null && VERSION.SDK_INT >= 21) {
                width -= BottomSheet.this.lastInsets.getSystemWindowInsetRight() + BottomSheet.this.lastInsets.getSystemWindowInsetLeft();
            }
            int i = 0;
            boolean isPortrait = width < height;
            if (BottomSheet.this.containerView != null) {
                if (BottomSheet.this.fullWidth) {
                    BottomSheet.this.containerView.measure(MeasureSpec.makeMeasureSpec((BottomSheet.backgroundPaddingLeft * 2) + width, 1073741824), MeasureSpec.makeMeasureSpec(height, Integer.MIN_VALUE));
                } else {
                    if (AndroidUtilities.isTablet()) {
                        widthSpec = MeasureSpec.makeMeasureSpec(((int) (((float) Math.min(AndroidUtilities.displaySize.x, AndroidUtilities.displaySize.y)) * 0.8f)) + (BottomSheet.backgroundPaddingLeft * 2), 1073741824);
                    } else {
                        widthSpec = MeasureSpec.makeMeasureSpec(isPortrait ? (BottomSheet.backgroundPaddingLeft * 2) + width : ((int) Math.max(((float) width) * 0.8f, (float) Math.min(AndroidUtilities.dp(480.0f), width))) + (BottomSheet.backgroundPaddingLeft * 2), 1073741824);
                    }
                    BottomSheet.this.containerView.measure(widthSpec, MeasureSpec.makeMeasureSpec(height, Integer.MIN_VALUE));
                }
            }
            widthSpec = getChildCount();
            while (i < widthSpec) {
                View child = getChildAt(i);
                if (child.getVisibility() != 8) {
                    if (child != BottomSheet.this.containerView) {
                        if (!BottomSheet.this.onCustomMeasure(child, width, height)) {
                            measureChildWithMargins(child, MeasureSpec.makeMeasureSpec(width, 1073741824), 0, MeasureSpec.makeMeasureSpec(height, 1073741824), 0);
                        }
                    }
                }
                i++;
            }
        }

        public boolean onInterceptTouchEvent(MotionEvent event) {
            if (BottomSheet.this.canDismissWithSwipe()) {
                return onTouchEvent(event);
            }
            return super.onInterceptTouchEvent(event);
        }

        public void requestDisallowInterceptTouchEvent(boolean disallowIntercept) {
            if (this.maybeStartTracking && !this.startedTracking) {
                onTouchEvent(null);
            }
            super.requestDisallowInterceptTouchEvent(disallowIntercept);
        }

        public boolean hasOverlappingRendering() {
            return false;
        }

        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            BottomSheet.this.onContainerDraw(canvas);
        }
    }

    public void onAttachedToWindow() {
        super.onAttachedToWindow();
    }

    public void setAllowNestedScroll(boolean value) {
        this.allowNestedScroll = value;
        if (!this.allowNestedScroll) {
            this.containerView.setTranslationY(0.0f);
        }
    }

    public BottomSheet(Context context, boolean needFocus) {
        super(context, R.style.TransparentDialog);
        if (VERSION.SDK_INT >= 21) {
            getWindow().addFlags(-2147417856);
        }
        this.touchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
        Rect padding = new Rect();
        this.shadowDrawable = context.getResources().getDrawable(R.drawable.sheet_shadow).mutate();
        this.shadowDrawable.setColorFilter(new PorterDuffColorFilter(Theme.getColor(Theme.key_dialogBackground), Mode.MULTIPLY));
        this.shadowDrawable.getPadding(padding);
        backgroundPaddingLeft = padding.left;
        backgroundPaddingTop = padding.top;
        this.container = new ContainerView(getContext()) {
            public boolean drawChild(Canvas canvas, View child, long drawingTime) {
                boolean z = true;
                try {
                    if (!BottomSheet.this.allowDrawContent || !super.drawChild(canvas, child, drawingTime)) {
                        z = false;
                    }
                    return z;
                } catch (Throwable e) {
                    FileLog.e(e);
                    return true;
                }
            }
        };
        this.container.setBackgroundDrawable(this.backDrawable);
        this.focusable = needFocus;
        if (VERSION.SDK_INT >= 21) {
            this.container.setFitsSystemWindows(true);
            this.container.setOnApplyWindowInsetsListener(new OnApplyWindowInsetsListener() {
                @SuppressLint({"NewApi"})
                public WindowInsets onApplyWindowInsets(View v, WindowInsets insets) {
                    BottomSheet.this.lastInsets = insets;
                    v.requestLayout();
                    return insets.consumeSystemWindowInsets();
                }
            });
            this.container.setSystemUiVisibility(1280);
        }
        this.backDrawable.setAlpha(0);
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window window = getWindow();
        window.setWindowAnimations(R.style.DialogNoAnimation);
        setContentView(this.container, new LayoutParams(-1, -1));
        if (this.containerView == null) {
            r0.containerView = new FrameLayout(getContext()) {
                public boolean hasOverlappingRendering() {
                    return false;
                }

                public void setTranslationY(float translationY) {
                    super.setTranslationY(translationY);
                    BottomSheet.this.onContainerTranslationYChanged(translationY);
                }
            };
            r0.containerView.setBackgroundDrawable(r0.shadowDrawable);
            r0.containerView.setPadding(backgroundPaddingLeft, ((r0.applyTopPadding ? AndroidUtilities.dp(8.0f) : 0) + backgroundPaddingTop) - 1, backgroundPaddingLeft, r0.applyBottomPadding ? AndroidUtilities.dp(8.0f) : 0);
        }
        if (VERSION.SDK_INT >= 21) {
            r0.containerView.setFitsSystemWindows(true);
        }
        r0.containerView.setVisibility(4);
        r0.container.addView(r0.containerView, 0, LayoutHelper.createFrame(-1, -2, 80));
        int topOffset = 0;
        if (r0.title != null) {
            TextView titleView = new TextView(getContext());
            titleView.setLines(1);
            titleView.setSingleLine(true);
            titleView.setText(r0.title);
            titleView.setTextColor(Theme.getColor(Theme.key_dialogTextGray2));
            titleView.setTextSize(1, 16.0f);
            titleView.setEllipsize(TruncateAt.MIDDLE);
            titleView.setPadding(AndroidUtilities.dp(16.0f), 0, AndroidUtilities.dp(16.0f), AndroidUtilities.dp(8.0f));
            titleView.setGravity(16);
            r0.containerView.addView(titleView, LayoutHelper.createFrame(-1, 48.0f));
            titleView.setOnTouchListener(new OnTouchListener() {
                public boolean onTouch(View v, MotionEvent event) {
                    return true;
                }
            });
            topOffset = 0 + 48;
        }
        if (r0.customView != null) {
            if (r0.customView.getParent() != null) {
                ((ViewGroup) r0.customView.getParent()).removeView(r0.customView);
            }
            r0.containerView.addView(r0.customView, LayoutHelper.createFrame(-1, -2.0f, 51, 0.0f, (float) topOffset, 0.0f, 0.0f));
        } else if (r0.items != null) {
            int topOffset2 = topOffset;
            topOffset = 0;
            while (topOffset < r0.items.length) {
                if (r0.items[topOffset] != null) {
                    BottomSheetCell cell = new BottomSheetCell(getContext(), 0);
                    cell.setTextAndIcon(r0.items[topOffset], r0.itemIcons != null ? r0.itemIcons[topOffset] : 0);
                    r0.containerView.addView(cell, LayoutHelper.createFrame(-1, 48.0f, 51, 0.0f, (float) topOffset2, 0.0f, 0.0f));
                    topOffset2 += 48;
                    cell.setTag(Integer.valueOf(topOffset));
                    cell.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {
                            BottomSheet.this.dismissWithButtonClick(((Integer) v.getTag()).intValue());
                        }
                    });
                    r0.itemViews.add(cell);
                }
                topOffset++;
            }
        }
        WindowManager.LayoutParams params = window.getAttributes();
        params.width = -1;
        params.gravity = 51;
        params.dimAmount = 0.0f;
        params.flags &= -3;
        if (!r0.focusable) {
            params.flags |= 131072;
        }
        params.height = -1;
        window.setAttributes(params);
    }

    public void setShowWithoutAnimation(boolean value) {
        this.showWithoutAnimation = value;
    }

    public void setBackgroundColor(int color) {
        this.shadowDrawable.setColorFilter(color, Mode.MULTIPLY);
    }

    public void show() {
        super.show();
        if (this.focusable) {
            getWindow().setSoftInputMode(16);
        }
        this.dismissed = false;
        cancelSheetAnimation();
        this.containerView.measure(MeasureSpec.makeMeasureSpec(AndroidUtilities.displaySize.x + (backgroundPaddingLeft * 2), Integer.MIN_VALUE), MeasureSpec.makeMeasureSpec(AndroidUtilities.displaySize.y, Integer.MIN_VALUE));
        if (this.showWithoutAnimation) {
            this.backDrawable.setAlpha(51);
            this.containerView.setTranslationY(0.0f);
            return;
        }
        this.backDrawable.setAlpha(0);
        if (VERSION.SDK_INT >= 18) {
            this.layoutCount = 2;
            this.containerView.setTranslationY((float) this.containerView.getMeasuredHeight());
            Runnable anonymousClass6 = new Runnable() {
                public void run() {
                    if (BottomSheet.this.startAnimationRunnable == this) {
                        if (!BottomSheet.this.dismissed) {
                            BottomSheet.this.startAnimationRunnable = null;
                            BottomSheet.this.startOpenAnimation();
                        }
                    }
                }
            };
            this.startAnimationRunnable = anonymousClass6;
            AndroidUtilities.runOnUIThread(anonymousClass6, 150);
        } else {
            startOpenAnimation();
        }
    }

    public void setAllowDrawContent(boolean value) {
        if (this.allowDrawContent != value) {
            this.allowDrawContent = value;
            this.container.setBackgroundDrawable(this.allowDrawContent ? this.backDrawable : null);
            this.container.invalidate();
        }
    }

    protected boolean canDismissWithSwipe() {
        return true;
    }

    protected boolean onContainerTouchEvent(MotionEvent event) {
        return false;
    }

    public void setCustomView(View view) {
        this.customView = view;
    }

    public void setTitle(CharSequence value) {
        this.title = value;
    }

    public void setApplyTopPadding(boolean value) {
        this.applyTopPadding = value;
    }

    public void setApplyBottomPadding(boolean value) {
        this.applyBottomPadding = value;
    }

    protected boolean onCustomMeasure(View view, int width, int height) {
        return false;
    }

    protected boolean onCustomLayout(View view, int left, int top, int right, int bottom) {
        return false;
    }

    protected boolean canDismissWithTouchOutside() {
        return true;
    }

    protected void onContainerTranslationYChanged(float translationY) {
    }

    private void cancelSheetAnimation() {
        if (this.currentSheetAnimation != null) {
            this.currentSheetAnimation.cancel();
            this.currentSheetAnimation = null;
        }
    }

    private void startOpenAnimation() {
        if (!this.dismissed) {
            this.containerView.setVisibility(0);
            if (!onCustomOpenAnimation()) {
                if (VERSION.SDK_INT >= 20 && this.useHardwareLayer) {
                    this.container.setLayerType(2, null);
                }
                this.containerView.setTranslationY((float) this.containerView.getMeasuredHeight());
                AnimatorSet animatorSet = new AnimatorSet();
                r2 = new Animator[2];
                r2[0] = ObjectAnimator.ofFloat(this.containerView, "translationY", new float[]{0.0f});
                r2[1] = ObjectAnimator.ofInt(this.backDrawable, "alpha", new int[]{51});
                animatorSet.playTogether(r2);
                animatorSet.setDuration(200);
                animatorSet.setStartDelay(20);
                animatorSet.setInterpolator(new DecelerateInterpolator());
                animatorSet.addListener(new AnimatorListenerAdapter() {
                    public void onAnimationEnd(Animator animation) {
                        if (BottomSheet.this.currentSheetAnimation != null && BottomSheet.this.currentSheetAnimation.equals(animation)) {
                            BottomSheet.this.currentSheetAnimation = null;
                            if (BottomSheet.this.delegate != null) {
                                BottomSheet.this.delegate.onOpenAnimationEnd();
                            }
                            if (BottomSheet.this.useHardwareLayer) {
                                BottomSheet.this.container.setLayerType(0, null);
                            }
                        }
                    }

                    public void onAnimationCancel(Animator animation) {
                        if (BottomSheet.this.currentSheetAnimation != null && BottomSheet.this.currentSheetAnimation.equals(animation)) {
                            BottomSheet.this.currentSheetAnimation = null;
                        }
                    }
                });
                animatorSet.start();
                this.currentSheetAnimation = animatorSet;
            }
        }
    }

    public void setDelegate(BottomSheetDelegateInterface bottomSheetDelegate) {
        this.delegate = bottomSheetDelegate;
    }

    public FrameLayout getContainer() {
        return this.container;
    }

    public ViewGroup getSheetContainer() {
        return this.containerView;
    }

    public int getTag() {
        return this.tag;
    }

    public void setItemText(int item, CharSequence text) {
        if (item >= 0) {
            if (item < this.itemViews.size()) {
                ((BottomSheetCell) this.itemViews.get(item)).textView.setText(text);
            }
        }
    }

    public boolean isDismissed() {
        return this.dismissed;
    }

    public void dismissWithButtonClick(final int item) {
        if (!this.dismissed) {
            this.dismissed = true;
            cancelSheetAnimation();
            AnimatorSet animatorSet = new AnimatorSet();
            r2 = new Animator[2];
            r2[0] = ObjectAnimator.ofFloat(this.containerView, "translationY", new float[]{(float) (this.containerView.getMeasuredHeight() + AndroidUtilities.dp(10.0f))});
            r2[1] = ObjectAnimator.ofInt(this.backDrawable, "alpha", new int[]{0});
            animatorSet.playTogether(r2);
            animatorSet.setDuration(180);
            animatorSet.setInterpolator(new AccelerateInterpolator());
            animatorSet.addListener(new AnimatorListenerAdapter() {
                public void onAnimationEnd(Animator animation) {
                    if (BottomSheet.this.currentSheetAnimation != null && BottomSheet.this.currentSheetAnimation.equals(animation)) {
                        BottomSheet.this.currentSheetAnimation = null;
                        if (BottomSheet.this.onClickListener != null) {
                            BottomSheet.this.onClickListener.onClick(BottomSheet.this, item);
                        }
                        AndroidUtilities.runOnUIThread(new Runnable() {
                            public void run() {
                                try {
                                    super.dismiss();
                                } catch (Throwable e) {
                                    FileLog.e(e);
                                }
                            }
                        });
                    }
                }

                public void onAnimationCancel(Animator animation) {
                    if (BottomSheet.this.currentSheetAnimation != null && BottomSheet.this.currentSheetAnimation.equals(animation)) {
                        BottomSheet.this.currentSheetAnimation = null;
                    }
                }
            });
            animatorSet.start();
            this.currentSheetAnimation = animatorSet;
        }
    }

    public void dismiss() {
        if ((this.delegate == null || this.delegate.canDismiss()) && !this.dismissed) {
            this.dismissed = true;
            cancelSheetAnimation();
            if (!(this.allowCustomAnimation && onCustomCloseAnimation())) {
                AnimatorSet animatorSet = new AnimatorSet();
                r2 = new Animator[2];
                r2[0] = ObjectAnimator.ofFloat(this.containerView, "translationY", new float[]{(float) (this.containerView.getMeasuredHeight() + AndroidUtilities.dp(10.0f))});
                r2[1] = ObjectAnimator.ofInt(this.backDrawable, "alpha", new int[]{0});
                animatorSet.playTogether(r2);
                if (this.useFastDismiss) {
                    int height = this.containerView.getMeasuredHeight();
                    animatorSet.setDuration((long) Math.max(60, (int) ((180.0f * (((float) height) - this.containerView.getTranslationY())) / ((float) height))));
                    this.useFastDismiss = false;
                } else {
                    animatorSet.setDuration(180);
                }
                animatorSet.setInterpolator(new AccelerateInterpolator());
                animatorSet.addListener(new AnimatorListenerAdapter() {
                    public void onAnimationEnd(Animator animation) {
                        if (BottomSheet.this.currentSheetAnimation != null && BottomSheet.this.currentSheetAnimation.equals(animation)) {
                            BottomSheet.this.currentSheetAnimation = null;
                            AndroidUtilities.runOnUIThread(new Runnable() {
                                public void run() {
                                    try {
                                        BottomSheet.this.dismissInternal();
                                    } catch (Throwable e) {
                                        FileLog.e(e);
                                    }
                                }
                            });
                        }
                    }

                    public void onAnimationCancel(Animator animation) {
                        if (BottomSheet.this.currentSheetAnimation != null && BottomSheet.this.currentSheetAnimation.equals(animation)) {
                            BottomSheet.this.currentSheetAnimation = null;
                        }
                    }
                });
                animatorSet.start();
                this.currentSheetAnimation = animatorSet;
            }
        }
    }

    public void dismissInternal() {
        try {
            super.dismiss();
        } catch (Throwable e) {
            FileLog.e(e);
        }
    }

    protected boolean onCustomCloseAnimation() {
        return false;
    }

    protected boolean onCustomOpenAnimation() {
        return false;
    }

    protected int getLeftInset() {
        if (this.lastInsets == null || VERSION.SDK_INT < 21) {
            return 0;
        }
        return this.lastInsets.getSystemWindowInsetLeft();
    }

    public void onConfigurationChanged(Configuration newConfig) {
    }

    public void onContainerDraw(Canvas canvas) {
    }
}
