package org.telegram.ui.ActionBar;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Outline;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build.VERSION;
import android.support.annotation.Keep;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewOutlineProvider;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.LinearLayout;
import java.util.ArrayList;
import java.util.Iterator;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.beta.R;
import org.telegram.ui.ActionBar.Theme.ThemeInfo;
import org.telegram.ui.ActionBar.ThemeDescription.ThemeDescriptionDelegate;
import org.telegram.ui.Components.CubicBezierInterpolator;
import org.telegram.ui.Components.LayoutHelper;

public class ActionBarLayout extends FrameLayout {
    private static Drawable headerShadowDrawable;
    private static Drawable layerShadowDrawable;
    private static Paint scrimPaint;
    private AccelerateDecelerateInterpolator accelerateDecelerateInterpolator = new AccelerateDecelerateInterpolator();
    private int[][] animateEndColors = new int[2][];
    private ThemeInfo animateSetThemeAfterAnimation;
    private int[][] animateStartColors = new int[2][];
    private boolean animateThemeAfterAnimation;
    protected boolean animationInProgress;
    private float animationProgress;
    private Runnable animationRunnable;
    private View backgroundView;
    private boolean beginTrackingSent;
    private LinearLayoutContainer containerView;
    private LinearLayoutContainer containerViewBack;
    private ActionBar currentActionBar;
    private AnimatorSet currentAnimation;
    private DecelerateInterpolator decelerateInterpolator = new DecelerateInterpolator(1.5f);
    private Runnable delayedOpenAnimationRunnable;
    private ActionBarLayoutDelegate delegate;
    private DrawerLayoutContainer drawerLayoutContainer;
    public ArrayList<BaseFragment> fragmentsStack;
    private boolean inActionMode;
    private boolean inPreviewMode;
    public float innerTranslationX;
    private long lastFrameTime;
    private boolean maybeStartTracking;
    private Runnable onCloseAnimationEndRunnable;
    private Runnable onOpenAnimationEndRunnable;
    private Runnable overlayAction;
    protected Activity parentActivity;
    private ColorDrawable previewBackgroundDrawable;
    private boolean rebuildAfterAnimation;
    private boolean rebuildLastAfterAnimation;
    private boolean removeActionBarExtraHeight;
    private boolean showLastAfterAnimation;
    protected boolean startedTracking;
    private int startedTrackingPointerId;
    private int startedTrackingX;
    private int startedTrackingY;
    private String subtitleOverlayText;
    private float themeAnimationValue;
    private ThemeDescriptionDelegate[] themeAnimatorDelegate = new ThemeDescriptionDelegate[2];
    private ThemeDescription[][] themeAnimatorDescriptions = new ThemeDescription[2][];
    private AnimatorSet themeAnimatorSet;
    private String titleOverlayText;
    private boolean transitionAnimationInProgress;
    private boolean transitionAnimationPreviewMode;
    private long transitionAnimationStartTime;
    private boolean useAlphaAnimations;
    private VelocityTracker velocityTracker;
    private Runnable waitingForKeyboardCloseRunnable;

    public interface ActionBarLayoutDelegate {
        boolean needAddFragmentToStack(BaseFragment baseFragment, ActionBarLayout actionBarLayout);

        boolean needCloseLastFragment(ActionBarLayout actionBarLayout);

        boolean needPresentFragment(BaseFragment baseFragment, boolean z, boolean z2, ActionBarLayout actionBarLayout);

        boolean onPreIme();

        void onRebuildAllFragments(ActionBarLayout actionBarLayout, boolean z);
    }

    public class LinearLayoutContainer extends LinearLayout {
        private boolean isKeyboardVisible;
        private Rect rect = new Rect();

        public LinearLayoutContainer(Context context) {
            super(context);
            setOrientation(1);
        }

        protected boolean drawChild(Canvas canvas, View child, long drawingTime) {
            if (child instanceof ActionBar) {
                return super.drawChild(canvas, child, drawingTime);
            }
            boolean result;
            int actionBarHeight = 0;
            int childCount = getChildCount();
            for (int a = 0; a < childCount; a++) {
                View view = getChildAt(a);
                if (view != child && (view instanceof ActionBar) && view.getVisibility() == 0) {
                    if (((ActionBar) view).getCastShadows()) {
                        actionBarHeight = view.getMeasuredHeight();
                    }
                    result = super.drawChild(canvas, child, drawingTime);
                    if (actionBarHeight == 0 && ActionBarLayout.headerShadowDrawable != null) {
                        ActionBarLayout.headerShadowDrawable.setBounds(0, actionBarHeight, getMeasuredWidth(), ActionBarLayout.headerShadowDrawable.getIntrinsicHeight() + actionBarHeight);
                        ActionBarLayout.headerShadowDrawable.draw(canvas);
                        return result;
                    }
                }
            }
            result = super.drawChild(canvas, child, drawingTime);
            return actionBarHeight == 0 ? result : result;
        }

        public boolean hasOverlappingRendering() {
            if (VERSION.SDK_INT >= 28) {
                return true;
            }
            return false;
        }

        protected void onLayout(boolean changed, int l, int t, int r, int b) {
            int i;
            boolean z = false;
            super.onLayout(changed, l, t, r, b);
            View rootView = getRootView();
            getWindowVisibleDisplayFrame(this.rect);
            int height = rootView.getHeight();
            if (this.rect.top != 0) {
                i = AndroidUtilities.statusBarHeight;
            } else {
                i = 0;
            }
            if (((height - i) - AndroidUtilities.getViewInset(rootView)) - (this.rect.bottom - this.rect.top) > 0) {
                z = true;
            }
            this.isKeyboardVisible = z;
            if (ActionBarLayout.this.waitingForKeyboardCloseRunnable != null && !ActionBarLayout.this.containerView.isKeyboardVisible && !ActionBarLayout.this.containerViewBack.isKeyboardVisible) {
                AndroidUtilities.cancelRunOnUIThread(ActionBarLayout.this.waitingForKeyboardCloseRunnable);
                ActionBarLayout.this.waitingForKeyboardCloseRunnable.run();
                ActionBarLayout.this.waitingForKeyboardCloseRunnable = null;
            }
        }

        public boolean dispatchTouchEvent(MotionEvent ev) {
            if ((ActionBarLayout.this.inPreviewMode || ActionBarLayout.this.transitionAnimationPreviewMode) && (ev.getActionMasked() == 0 || ev.getActionMasked() == 5)) {
                return false;
            }
            try {
                if ((!ActionBarLayout.this.inPreviewMode || this != ActionBarLayout.this.containerView) && super.dispatchTouchEvent(ev)) {
                    return true;
                }
                return false;
            } catch (Throwable th) {
                return false;
            }
        }
    }

    public ActionBarLayout(Context context) {
        super(context);
        this.parentActivity = (Activity) context;
        if (layerShadowDrawable == null) {
            layerShadowDrawable = getResources().getDrawable(R.drawable.layer_shadow);
            headerShadowDrawable = getResources().getDrawable(R.drawable.header_shadow).mutate();
            scrimPaint = new Paint();
        }
    }

    public void init(ArrayList<BaseFragment> stack) {
        this.fragmentsStack = stack;
        this.containerViewBack = new LinearLayoutContainer(this.parentActivity);
        addView(this.containerViewBack);
        LayoutParams layoutParams = (LayoutParams) this.containerViewBack.getLayoutParams();
        layoutParams.width = -1;
        layoutParams.height = -1;
        layoutParams.gravity = 51;
        this.containerViewBack.setLayoutParams(layoutParams);
        this.containerView = new LinearLayoutContainer(this.parentActivity);
        addView(this.containerView);
        layoutParams = (LayoutParams) this.containerView.getLayoutParams();
        layoutParams.width = -1;
        layoutParams.height = -1;
        layoutParams.gravity = 51;
        this.containerView.setLayoutParams(layoutParams);
        Iterator it = this.fragmentsStack.iterator();
        while (it.hasNext()) {
            ((BaseFragment) it.next()).setParentLayout(this);
        }
    }

    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (!this.fragmentsStack.isEmpty()) {
            BaseFragment lastFragment = (BaseFragment) this.fragmentsStack.get(this.fragmentsStack.size() - 1);
            lastFragment.onConfigurationChanged(newConfig);
            if (lastFragment.visibleDialog instanceof BottomSheet) {
                ((BottomSheet) lastFragment.visibleDialog).onConfigurationChanged(newConfig);
            }
        }
    }

    public void drawHeaderShadow(Canvas canvas, int y) {
        if (headerShadowDrawable != null) {
            headerShadowDrawable.setBounds(0, y, getMeasuredWidth(), headerShadowDrawable.getIntrinsicHeight() + y);
            headerShadowDrawable.draw(canvas);
        }
    }

    @Keep
    public void setInnerTranslationX(float value) {
        this.innerTranslationX = value;
        invalidate();
    }

    @Keep
    public float getInnerTranslationX() {
        return this.innerTranslationX;
    }

    public void dismissDialogs() {
        if (!this.fragmentsStack.isEmpty()) {
            ((BaseFragment) this.fragmentsStack.get(this.fragmentsStack.size() - 1)).dismissCurrentDialig();
        }
    }

    public void onResume() {
        if (this.transitionAnimationInProgress) {
            if (this.currentAnimation != null) {
                this.currentAnimation.cancel();
                this.currentAnimation = null;
            }
            if (this.onCloseAnimationEndRunnable != null) {
                onCloseAnimationEnd();
            } else if (this.onOpenAnimationEndRunnable != null) {
                onOpenAnimationEnd();
            }
        }
        if (!this.fragmentsStack.isEmpty()) {
            ((BaseFragment) this.fragmentsStack.get(this.fragmentsStack.size() - 1)).onResume();
        }
    }

    public void onPause() {
        if (!this.fragmentsStack.isEmpty()) {
            ((BaseFragment) this.fragmentsStack.get(this.fragmentsStack.size() - 1)).onPause();
        }
    }

    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return this.animationInProgress || checkTransitionAnimation() || onTouchEvent(ev);
    }

    public void requestDisallowInterceptTouchEvent(boolean disallowIntercept) {
        onTouchEvent(null);
        super.requestDisallowInterceptTouchEvent(disallowIntercept);
    }

    public boolean dispatchKeyEventPreIme(KeyEvent event) {
        if (event == null || event.getKeyCode() != 4 || event.getAction() != 1) {
            return super.dispatchKeyEventPreIme(event);
        }
        if ((this.delegate == null || !this.delegate.onPreIme()) && !super.dispatchKeyEventPreIme(event)) {
            return false;
        }
        return true;
    }

    protected boolean drawChild(Canvas canvas, View child, long drawingTime) {
        int width = (getWidth() - getPaddingLeft()) - getPaddingRight();
        int translationX = ((int) this.innerTranslationX) + getPaddingRight();
        int clipLeft = getPaddingLeft();
        int clipRight = width + getPaddingLeft();
        if (child == this.containerViewBack) {
            clipRight = translationX;
        } else if (child == this.containerView) {
            clipLeft = translationX;
        }
        int restoreCount = canvas.save();
        if (!(this.transitionAnimationInProgress || this.inPreviewMode)) {
            canvas.clipRect(clipLeft, 0, clipRight, getHeight());
        }
        if ((this.inPreviewMode || this.transitionAnimationPreviewMode) && child == this.containerView) {
            View view = this.containerView.getChildAt(0);
            if (view != null) {
                this.previewBackgroundDrawable.setBounds(0, 0, getMeasuredWidth(), getMeasuredHeight());
                this.previewBackgroundDrawable.draw(canvas);
                int x = (getMeasuredWidth() - AndroidUtilities.dp(24.0f)) / 2;
                int y = (int) ((this.containerView.getTranslationY() + ((float) view.getTop())) - ((float) AndroidUtilities.dp((float) ((VERSION.SDK_INT < 21 ? 20 : 0) + 12))));
                Theme.moveUpDrawable.setBounds(x, y, AndroidUtilities.dp(24.0f) + x, AndroidUtilities.dp(24.0f) + y);
                Theme.moveUpDrawable.draw(canvas);
            }
        }
        boolean result = super.drawChild(canvas, child, drawingTime);
        canvas.restoreToCount(restoreCount);
        if (translationX != 0) {
            if (child == this.containerView) {
                float alpha = Math.max(0.0f, Math.min(((float) (width - translationX)) / ((float) AndroidUtilities.dp(20.0f)), 1.0f));
                layerShadowDrawable.setBounds(translationX - layerShadowDrawable.getIntrinsicWidth(), child.getTop(), translationX, child.getBottom());
                layerShadowDrawable.setAlpha((int) (255.0f * alpha));
                layerShadowDrawable.draw(canvas);
            } else if (child == this.containerViewBack) {
                float opacity = Math.min(0.8f, ((float) (width - translationX)) / ((float) width));
                if (opacity < 0.0f) {
                    opacity = 0.0f;
                }
                scrimPaint.setColor(((int) (153.0f * opacity)) << 24);
                canvas.drawRect((float) clipLeft, 0.0f, (float) clipRight, (float) getHeight(), scrimPaint);
            }
        }
        return result;
    }

    public void setDelegate(ActionBarLayoutDelegate delegate) {
        this.delegate = delegate;
    }

    private void onSlideAnimationEnd(boolean backAnimation) {
        BaseFragment lastFragment;
        if (!backAnimation) {
            lastFragment = (BaseFragment) this.fragmentsStack.get(this.fragmentsStack.size() - 1);
            lastFragment.onPause();
            lastFragment.onFragmentDestroy();
            lastFragment.setParentLayout(null);
            this.fragmentsStack.remove(this.fragmentsStack.size() - 1);
            LinearLayoutContainer temp = this.containerView;
            this.containerView = this.containerViewBack;
            this.containerViewBack = temp;
            bringChildToFront(this.containerView);
            lastFragment = (BaseFragment) this.fragmentsStack.get(this.fragmentsStack.size() - 1);
            this.currentActionBar = lastFragment.actionBar;
            lastFragment.onResume();
            lastFragment.onBecomeFullyVisible();
        } else if (this.fragmentsStack.size() >= 2) {
            ViewGroup parent;
            lastFragment = (BaseFragment) this.fragmentsStack.get(this.fragmentsStack.size() - 2);
            lastFragment.onPause();
            if (lastFragment.fragmentView != null) {
                parent = (ViewGroup) lastFragment.fragmentView.getParent();
                if (parent != null) {
                    lastFragment.onRemoveFromParent();
                    parent.removeView(lastFragment.fragmentView);
                }
            }
            if (lastFragment.actionBar != null && lastFragment.actionBar.getAddToContainer()) {
                parent = (ViewGroup) lastFragment.actionBar.getParent();
                if (parent != null) {
                    parent.removeView(lastFragment.actionBar);
                }
            }
        }
        this.containerViewBack.setVisibility(8);
        this.startedTracking = false;
        this.animationInProgress = false;
        this.containerView.setTranslationX(0.0f);
        this.containerViewBack.setTranslationX(0.0f);
        setInnerTranslationX(0.0f);
    }

    private void prepareForMoving(MotionEvent ev) {
        this.maybeStartTracking = false;
        this.startedTracking = true;
        this.startedTrackingX = (int) ev.getX();
        this.containerViewBack.setVisibility(0);
        this.beginTrackingSent = false;
        BaseFragment lastFragment = (BaseFragment) this.fragmentsStack.get(this.fragmentsStack.size() - 2);
        View fragmentView = lastFragment.fragmentView;
        if (fragmentView == null) {
            fragmentView = lastFragment.createView(this.parentActivity);
        }
        ViewGroup parent = (ViewGroup) fragmentView.getParent();
        if (parent != null) {
            lastFragment.onRemoveFromParent();
            parent.removeView(fragmentView);
        }
        if (lastFragment.actionBar != null && lastFragment.actionBar.getAddToContainer()) {
            parent = (ViewGroup) lastFragment.actionBar.getParent();
            if (parent != null) {
                parent.removeView(lastFragment.actionBar);
            }
            if (this.removeActionBarExtraHeight) {
                lastFragment.actionBar.setOccupyStatusBar(false);
            }
            this.containerViewBack.addView(lastFragment.actionBar);
            lastFragment.actionBar.setTitleOverlayText(this.titleOverlayText, this.subtitleOverlayText, this.overlayAction);
        }
        this.containerViewBack.addView(fragmentView);
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) fragmentView.getLayoutParams();
        layoutParams.width = -1;
        layoutParams.height = -1;
        layoutParams.leftMargin = 0;
        layoutParams.rightMargin = 0;
        layoutParams.bottomMargin = 0;
        layoutParams.topMargin = 0;
        fragmentView.setLayoutParams(layoutParams);
        if (!lastFragment.hasOwnBackground && fragmentView.getBackground() == null) {
            fragmentView.setBackgroundColor(Theme.getColor(Theme.key_windowBackgroundWhite));
        }
        lastFragment.onResume();
    }

    public boolean onTouchEvent(MotionEvent ev) {
        if (checkTransitionAnimation() || this.inActionMode || this.animationInProgress) {
            return false;
        }
        if (this.fragmentsStack.size() > 1) {
            if (ev == null || ev.getAction() != 0 || this.startedTracking || this.maybeStartTracking) {
                if (ev != null && ev.getAction() == 2 && ev.getPointerId(0) == this.startedTrackingPointerId) {
                    if (this.velocityTracker == null) {
                        this.velocityTracker = VelocityTracker.obtain();
                    }
                    int dx = Math.max(0, (int) (ev.getX() - ((float) this.startedTrackingX)));
                    int dy = Math.abs(((int) ev.getY()) - this.startedTrackingY);
                    this.velocityTracker.addMovement(ev);
                    if (this.maybeStartTracking && !this.startedTracking && ((float) dx) >= AndroidUtilities.getPixelsInCM(0.4f, true) && Math.abs(dx) / 3 > dy) {
                        prepareForMoving(ev);
                    } else if (this.startedTracking) {
                        if (!this.beginTrackingSent) {
                            if (this.parentActivity.getCurrentFocus() != null) {
                                AndroidUtilities.hideKeyboard(this.parentActivity.getCurrentFocus());
                            }
                            ((BaseFragment) this.fragmentsStack.get(this.fragmentsStack.size() - 1)).onBeginSlide();
                            this.beginTrackingSent = true;
                        }
                        this.containerView.setTranslationX((float) dx);
                        setInnerTranslationX((float) dx);
                    }
                } else if (ev != null && ev.getPointerId(0) == this.startedTrackingPointerId && (ev.getAction() == 3 || ev.getAction() == 1 || ev.getAction() == 6)) {
                    float velX;
                    if (this.velocityTracker == null) {
                        this.velocityTracker = VelocityTracker.obtain();
                    }
                    this.velocityTracker.computeCurrentVelocity(1000);
                    if (!(this.inPreviewMode || this.transitionAnimationPreviewMode || this.startedTracking || !((BaseFragment) this.fragmentsStack.get(this.fragmentsStack.size() - 1)).swipeBackEnabled)) {
                        velX = this.velocityTracker.getXVelocity();
                        float velY = this.velocityTracker.getYVelocity();
                        if (velX >= 3500.0f && velX > Math.abs(velY)) {
                            prepareForMoving(ev);
                            if (!this.beginTrackingSent) {
                                if (((Activity) getContext()).getCurrentFocus() != null) {
                                    AndroidUtilities.hideKeyboard(((Activity) getContext()).getCurrentFocus());
                                }
                                this.beginTrackingSent = true;
                            }
                        }
                    }
                    if (this.startedTracking) {
                        float distToMove;
                        float x = this.containerView.getX();
                        AnimatorSet animatorSet = new AnimatorSet();
                        velX = this.velocityTracker.getXVelocity();
                        final boolean backAnimation = x < ((float) this.containerView.getMeasuredWidth()) / 3.0f && (velX < 3500.0f || velX < this.velocityTracker.getYVelocity());
                        Animator[] animatorArr;
                        if (backAnimation) {
                            distToMove = x;
                            animatorArr = new Animator[2];
                            animatorArr[0] = ObjectAnimator.ofFloat(this.containerView, "translationX", new float[]{0.0f});
                            animatorArr[1] = ObjectAnimator.ofFloat(this, "innerTranslationX", new float[]{0.0f});
                            animatorSet.playTogether(animatorArr);
                        } else {
                            distToMove = ((float) this.containerView.getMeasuredWidth()) - x;
                            animatorArr = new Animator[2];
                            animatorArr[0] = ObjectAnimator.ofFloat(this.containerView, "translationX", new float[]{(float) this.containerView.getMeasuredWidth()});
                            animatorArr[1] = ObjectAnimator.ofFloat(this, "innerTranslationX", new float[]{(float) this.containerView.getMeasuredWidth()});
                            animatorSet.playTogether(animatorArr);
                        }
                        animatorSet.setDuration((long) Math.max((int) ((200.0f / ((float) this.containerView.getMeasuredWidth())) * distToMove), 50));
                        animatorSet.addListener(new AnimatorListenerAdapter() {
                            public void onAnimationEnd(Animator animator) {
                                ActionBarLayout.this.onSlideAnimationEnd(backAnimation);
                            }
                        });
                        animatorSet.start();
                        this.animationInProgress = true;
                    } else {
                        this.maybeStartTracking = false;
                        this.startedTracking = false;
                    }
                    if (this.velocityTracker != null) {
                        this.velocityTracker.recycle();
                        this.velocityTracker = null;
                    }
                } else if (ev == null) {
                    this.maybeStartTracking = false;
                    this.startedTracking = false;
                    if (this.velocityTracker != null) {
                        this.velocityTracker.recycle();
                        this.velocityTracker = null;
                    }
                }
            } else if (!((BaseFragment) this.fragmentsStack.get(this.fragmentsStack.size() - 1)).swipeBackEnabled) {
                return false;
            } else {
                this.startedTrackingPointerId = ev.getPointerId(0);
                this.maybeStartTracking = true;
                this.startedTrackingX = (int) ev.getX();
                this.startedTrackingY = (int) ev.getY();
                if (this.velocityTracker != null) {
                    this.velocityTracker.clear();
                }
            }
        }
        return this.startedTracking;
    }

    public void onBackPressed() {
        if (!this.transitionAnimationPreviewMode && !this.startedTracking && !checkTransitionAnimation() && !this.fragmentsStack.isEmpty()) {
            if (this.currentActionBar != null && this.currentActionBar.isSearchFieldVisible) {
                this.currentActionBar.closeSearchField();
            } else if (((BaseFragment) this.fragmentsStack.get(this.fragmentsStack.size() - 1)).onBackPressed() && !this.fragmentsStack.isEmpty()) {
                closeLastFragment(true);
            }
        }
    }

    public void onLowMemory() {
        Iterator it = this.fragmentsStack.iterator();
        while (it.hasNext()) {
            ((BaseFragment) it.next()).onLowMemory();
        }
    }

    private void onAnimationEndCheck(boolean byCheck) {
        onCloseAnimationEnd();
        onOpenAnimationEnd();
        if (this.waitingForKeyboardCloseRunnable != null) {
            AndroidUtilities.cancelRunOnUIThread(this.waitingForKeyboardCloseRunnable);
            this.waitingForKeyboardCloseRunnable = null;
        }
        if (this.currentAnimation != null) {
            if (byCheck) {
                this.currentAnimation.cancel();
            }
            this.currentAnimation = null;
        }
        if (this.animationRunnable != null) {
            AndroidUtilities.cancelRunOnUIThread(this.animationRunnable);
            this.animationRunnable = null;
        }
        setAlpha(1.0f);
        this.containerView.setAlpha(1.0f);
        this.containerView.setScaleX(1.0f);
        this.containerView.setScaleY(1.0f);
        this.containerViewBack.setAlpha(1.0f);
        this.containerViewBack.setScaleX(1.0f);
        this.containerViewBack.setScaleY(1.0f);
    }

    public BaseFragment getLastFragment() {
        if (this.fragmentsStack.isEmpty()) {
            return null;
        }
        return (BaseFragment) this.fragmentsStack.get(this.fragmentsStack.size() - 1);
    }

    public boolean checkTransitionAnimation() {
        if (this.transitionAnimationPreviewMode) {
            return false;
        }
        if (this.transitionAnimationInProgress && this.transitionAnimationStartTime < System.currentTimeMillis() - 1500) {
            onAnimationEndCheck(true);
        }
        return this.transitionAnimationInProgress;
    }

    private void presentFragmentInternalRemoveOld(boolean removeLast, BaseFragment fragment) {
        if (fragment != null) {
            fragment.onPause();
            if (removeLast) {
                fragment.onFragmentDestroy();
                fragment.setParentLayout(null);
                this.fragmentsStack.remove(fragment);
            } else {
                ViewGroup parent;
                if (fragment.fragmentView != null) {
                    parent = (ViewGroup) fragment.fragmentView.getParent();
                    if (parent != null) {
                        fragment.onRemoveFromParent();
                        parent.removeView(fragment.fragmentView);
                    }
                }
                if (fragment.actionBar != null && fragment.actionBar.getAddToContainer()) {
                    parent = (ViewGroup) fragment.actionBar.getParent();
                    if (parent != null) {
                        parent.removeView(fragment.actionBar);
                    }
                }
            }
            this.containerViewBack.setVisibility(8);
        }
    }

    public boolean presentFragmentAsPreview(BaseFragment fragment) {
        return presentFragment(fragment, false, false, true, true);
    }

    public boolean presentFragment(BaseFragment fragment) {
        return presentFragment(fragment, false, false, true, false);
    }

    public boolean presentFragment(BaseFragment fragment, boolean removeLast) {
        return presentFragment(fragment, removeLast, false, true, false);
    }

    private void startLayoutAnimation(final boolean open, final boolean first, final boolean preview) {
        if (first) {
            this.animationProgress = 0.0f;
            this.lastFrameTime = System.nanoTime() / 1000000;
        }
        Runnable anonymousClass2 = new Runnable() {
            public void run() {
                if (ActionBarLayout.this.animationRunnable == this) {
                    ActionBarLayout.this.animationRunnable = null;
                    if (first) {
                        ActionBarLayout.this.transitionAnimationStartTime = System.currentTimeMillis();
                    }
                    long newTime = System.nanoTime() / 1000000;
                    long dt = newTime - ActionBarLayout.this.lastFrameTime;
                    if (dt > 18) {
                        dt = 18;
                    }
                    ActionBarLayout.this.lastFrameTime = newTime;
                    ActionBarLayout.this.animationProgress = ActionBarLayout.this.animationProgress + (((float) dt) / 150.0f);
                    if (ActionBarLayout.this.animationProgress > 1.0f) {
                        ActionBarLayout.this.animationProgress = 1.0f;
                    }
                    float interpolated = ActionBarLayout.this.decelerateInterpolator.getInterpolation(ActionBarLayout.this.animationProgress);
                    if (open) {
                        ActionBarLayout.this.containerView.setAlpha(interpolated);
                        if (preview) {
                            ActionBarLayout.this.containerView.setScaleX((0.1f * interpolated) + 0.9f);
                            ActionBarLayout.this.containerView.setScaleY((0.1f * interpolated) + 0.9f);
                            ActionBarLayout.this.previewBackgroundDrawable.setAlpha((int) (128.0f * interpolated));
                            Theme.moveUpDrawable.setAlpha((int) (255.0f * interpolated));
                            ActionBarLayout.this.containerView.invalidate();
                            ActionBarLayout.this.invalidate();
                        } else {
                            ActionBarLayout.this.containerView.setTranslationX(((float) AndroidUtilities.dp(48.0f)) * (1.0f - interpolated));
                        }
                    } else {
                        ActionBarLayout.this.containerViewBack.setAlpha(1.0f - interpolated);
                        if (preview) {
                            ActionBarLayout.this.containerViewBack.setScaleX(((1.0f - interpolated) * 0.1f) + 0.9f);
                            ActionBarLayout.this.containerViewBack.setScaleY(((1.0f - interpolated) * 0.1f) + 0.9f);
                            ActionBarLayout.this.previewBackgroundDrawable.setAlpha((int) ((1.0f - interpolated) * 128.0f));
                            Theme.moveUpDrawable.setAlpha((int) (255.0f * (1.0f - interpolated)));
                            ActionBarLayout.this.containerView.invalidate();
                            ActionBarLayout.this.invalidate();
                        } else {
                            ActionBarLayout.this.containerViewBack.setTranslationX(((float) AndroidUtilities.dp(48.0f)) * interpolated);
                        }
                    }
                    if (ActionBarLayout.this.animationProgress < 1.0f) {
                        ActionBarLayout.this.startLayoutAnimation(open, false, preview);
                    } else {
                        ActionBarLayout.this.onAnimationEndCheck(false);
                    }
                }
            }
        };
        this.animationRunnable = anonymousClass2;
        AndroidUtilities.runOnUIThread(anonymousClass2);
    }

    public void resumeDelayedFragmentAnimation() {
        if (this.delayedOpenAnimationRunnable != null) {
            AndroidUtilities.cancelRunOnUIThread(this.delayedOpenAnimationRunnable);
            this.delayedOpenAnimationRunnable.run();
            this.delayedOpenAnimationRunnable = null;
        }
    }

    public boolean presentFragment(BaseFragment fragment, boolean removeLast, boolean forceWithoutAnimation, boolean check, boolean preview) {
        if (checkTransitionAnimation() || ((this.delegate != null && check && !this.delegate.needPresentFragment(fragment, removeLast, forceWithoutAnimation, this)) || !fragment.onFragmentCreate())) {
            return false;
        }
        ViewGroup parent;
        fragment.setInPreviewMode(preview);
        if (this.parentActivity.getCurrentFocus() != null) {
            AndroidUtilities.hideKeyboard(this.parentActivity.getCurrentFocus());
        }
        boolean needAnimation = preview || (!forceWithoutAnimation && MessagesController.getGlobalMainSettings().getBoolean("view_animations", true));
        BaseFragment currentFragment = !this.fragmentsStack.isEmpty() ? (BaseFragment) this.fragmentsStack.get(this.fragmentsStack.size() - 1) : null;
        fragment.setParentLayout(this);
        View fragmentView = fragment.fragmentView;
        if (fragmentView == null) {
            fragmentView = fragment.createView(this.parentActivity);
        } else {
            parent = (ViewGroup) fragmentView.getParent();
            if (parent != null) {
                fragment.onRemoveFromParent();
                parent.removeView(fragmentView);
            }
        }
        if (fragment.actionBar != null && fragment.actionBar.getAddToContainer()) {
            if (this.removeActionBarExtraHeight) {
                fragment.actionBar.setOccupyStatusBar(false);
            }
            parent = (ViewGroup) fragment.actionBar.getParent();
            if (parent != null) {
                parent.removeView(fragment.actionBar);
            }
            this.containerViewBack.addView(fragment.actionBar);
            fragment.actionBar.setTitleOverlayText(this.titleOverlayText, this.subtitleOverlayText, this.overlayAction);
        }
        this.containerViewBack.addView(fragmentView);
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) fragmentView.getLayoutParams();
        layoutParams.width = -1;
        layoutParams.height = -1;
        if (preview) {
            int dp = AndroidUtilities.dp(8.0f);
            layoutParams.leftMargin = dp;
            layoutParams.rightMargin = dp;
            dp = AndroidUtilities.dp(46.0f);
            layoutParams.bottomMargin = dp;
            layoutParams.topMargin = dp;
            layoutParams.topMargin += AndroidUtilities.statusBarHeight;
        } else {
            layoutParams.leftMargin = 0;
            layoutParams.rightMargin = 0;
            layoutParams.bottomMargin = 0;
            layoutParams.topMargin = 0;
        }
        fragmentView.setLayoutParams(layoutParams);
        this.fragmentsStack.add(fragment);
        fragment.onResume();
        this.currentActionBar = fragment.actionBar;
        if (!fragment.hasOwnBackground && fragmentView.getBackground() == null) {
            fragmentView.setBackgroundColor(Theme.getColor(Theme.key_windowBackgroundWhite));
        }
        LinearLayoutContainer temp = this.containerView;
        this.containerView = this.containerViewBack;
        this.containerViewBack = temp;
        this.containerView.setVisibility(0);
        setInnerTranslationX(0.0f);
        this.containerView.setTranslationY(0.0f);
        if (preview) {
            if (VERSION.SDK_INT >= 21) {
                fragmentView.setOutlineProvider(new ViewOutlineProvider() {
                    @TargetApi(21)
                    public void getOutline(View view, Outline outline) {
                        outline.setRoundRect(0, AndroidUtilities.statusBarHeight, view.getMeasuredWidth(), view.getMeasuredHeight(), (float) AndroidUtilities.dp(6.0f));
                    }
                });
                fragmentView.setClipToOutline(true);
                fragmentView.setElevation((float) AndroidUtilities.dp(4.0f));
            }
            if (this.previewBackgroundDrawable == null) {
                this.previewBackgroundDrawable = new ColorDrawable(Integer.MIN_VALUE);
            }
            this.previewBackgroundDrawable.setAlpha(0);
            Theme.moveUpDrawable.setAlpha(0);
        }
        bringChildToFront(this.containerView);
        if (!needAnimation) {
            presentFragmentInternalRemoveOld(removeLast, currentFragment);
            if (this.backgroundView != null) {
                this.backgroundView.setVisibility(0);
            }
        }
        if (!needAnimation && !preview) {
            if (this.backgroundView != null) {
                this.backgroundView.setAlpha(1.0f);
                this.backgroundView.setVisibility(0);
            }
            fragment.onTransitionAnimationStart(true, false);
            fragment.onTransitionAnimationEnd(true, false);
            fragment.onBecomeFullyVisible();
        } else if (this.useAlphaAnimations && this.fragmentsStack.size() == 1) {
            presentFragmentInternalRemoveOld(removeLast, currentFragment);
            this.transitionAnimationStartTime = System.currentTimeMillis();
            this.transitionAnimationInProgress = true;
            this.onOpenAnimationEndRunnable = new ActionBarLayout$$Lambda$0(fragment);
            ArrayList<Animator> animators = new ArrayList();
            animators.add(ObjectAnimator.ofFloat(this, "alpha", new float[]{0.0f, 1.0f}));
            if (this.backgroundView != null) {
                this.backgroundView.setVisibility(0);
                animators.add(ObjectAnimator.ofFloat(this.backgroundView, "alpha", new float[]{0.0f, 1.0f}));
            }
            fragment.onTransitionAnimationStart(true, false);
            this.currentAnimation = new AnimatorSet();
            this.currentAnimation.playTogether(animators);
            this.currentAnimation.setInterpolator(this.accelerateDecelerateInterpolator);
            this.currentAnimation.setDuration(200);
            this.currentAnimation.addListener(new AnimatorListenerAdapter() {
                public void onAnimationEnd(Animator animation) {
                    ActionBarLayout.this.onAnimationEndCheck(false);
                }
            });
            this.currentAnimation.start();
        } else {
            this.transitionAnimationPreviewMode = preview;
            this.transitionAnimationStartTime = System.currentTimeMillis();
            this.transitionAnimationInProgress = true;
            this.onOpenAnimationEndRunnable = new ActionBarLayout$$Lambda$1(this, preview, removeLast, currentFragment, fragment);
            fragment.onTransitionAnimationStart(true, false);
            AnimatorSet animation = null;
            if (!preview) {
                animation = fragment.onCustomTransitionAnimation(true, new ActionBarLayout$$Lambda$2(this));
            }
            if (animation == null) {
                this.containerView.setAlpha(0.0f);
                if (preview) {
                    this.containerView.setTranslationX(0.0f);
                    this.containerView.setScaleX(0.9f);
                    this.containerView.setScaleY(0.9f);
                } else {
                    this.containerView.setTranslationX(48.0f);
                    this.containerView.setScaleX(1.0f);
                    this.containerView.setScaleY(1.0f);
                }
                final boolean z;
                if (this.containerView.isKeyboardVisible || this.containerViewBack.isKeyboardVisible) {
                    z = preview;
                    this.waitingForKeyboardCloseRunnable = new Runnable() {
                        public void run() {
                            if (ActionBarLayout.this.waitingForKeyboardCloseRunnable == this) {
                                ActionBarLayout.this.waitingForKeyboardCloseRunnable = null;
                                ActionBarLayout.this.startLayoutAnimation(true, true, z);
                            }
                        }
                    };
                    AndroidUtilities.runOnUIThread(this.waitingForKeyboardCloseRunnable, 200);
                } else if (fragment.needDelayOpenAnimation()) {
                    z = preview;
                    this.delayedOpenAnimationRunnable = new Runnable() {
                        public void run() {
                            if (ActionBarLayout.this.delayedOpenAnimationRunnable == this) {
                                ActionBarLayout.this.delayedOpenAnimationRunnable = null;
                                ActionBarLayout.this.startLayoutAnimation(true, true, z);
                            }
                        }
                    };
                    AndroidUtilities.runOnUIThread(this.delayedOpenAnimationRunnable, 200);
                } else {
                    startLayoutAnimation(true, true, preview);
                }
            } else {
                this.containerView.setAlpha(1.0f);
                this.containerView.setTranslationX(0.0f);
                this.currentAnimation = animation;
            }
        }
        return true;
    }

    static final /* synthetic */ void lambda$presentFragment$0$ActionBarLayout(BaseFragment fragment) {
        fragment.onTransitionAnimationEnd(true, false);
        fragment.onBecomeFullyVisible();
    }

    final /* synthetic */ void lambda$presentFragment$1$ActionBarLayout(boolean preview, boolean removeLast, BaseFragment currentFragment, BaseFragment fragment) {
        if (preview) {
            this.inPreviewMode = true;
            this.transitionAnimationPreviewMode = false;
            this.containerView.setScaleX(1.0f);
            this.containerView.setScaleY(1.0f);
        } else {
            presentFragmentInternalRemoveOld(removeLast, currentFragment);
            this.containerView.setTranslationX(0.0f);
        }
        fragment.onTransitionAnimationEnd(true, false);
        fragment.onBecomeFullyVisible();
    }

    final /* synthetic */ void lambda$presentFragment$2$ActionBarLayout() {
        onAnimationEndCheck(false);
    }

    public boolean addFragmentToStack(BaseFragment fragment) {
        return addFragmentToStack(fragment, -1);
    }

    public boolean addFragmentToStack(BaseFragment fragment, int position) {
        if ((this.delegate != null && !this.delegate.needAddFragmentToStack(fragment, this)) || !fragment.onFragmentCreate()) {
            return false;
        }
        fragment.setParentLayout(this);
        if (position == -1) {
            if (!this.fragmentsStack.isEmpty()) {
                ViewGroup parent;
                BaseFragment previousFragment = (BaseFragment) this.fragmentsStack.get(this.fragmentsStack.size() - 1);
                previousFragment.onPause();
                if (previousFragment.actionBar != null && previousFragment.actionBar.getAddToContainer()) {
                    parent = (ViewGroup) previousFragment.actionBar.getParent();
                    if (parent != null) {
                        parent.removeView(previousFragment.actionBar);
                    }
                }
                if (previousFragment.fragmentView != null) {
                    parent = (ViewGroup) previousFragment.fragmentView.getParent();
                    if (parent != null) {
                        previousFragment.onRemoveFromParent();
                        parent.removeView(previousFragment.fragmentView);
                    }
                }
            }
            this.fragmentsStack.add(fragment);
        } else {
            this.fragmentsStack.add(position, fragment);
        }
        return true;
    }

    private void closeLastFragmentInternalRemoveOld(BaseFragment fragment) {
        fragment.onPause();
        fragment.onFragmentDestroy();
        fragment.setParentLayout(null);
        this.fragmentsStack.remove(fragment);
        this.containerViewBack.setVisibility(8);
        bringChildToFront(this.containerView);
    }

    public void movePreviewFragment(float dy) {
        if (this.inPreviewMode && !this.transitionAnimationPreviewMode) {
            float currentTranslation = this.containerView.getTranslationY();
            float nextTranslation = -dy;
            if (nextTranslation > 0.0f) {
                nextTranslation = 0.0f;
            } else if (nextTranslation < ((float) (-AndroidUtilities.dp(60.0f)))) {
                this.inPreviewMode = false;
                nextTranslation = 0.0f;
                BaseFragment prevFragment = (BaseFragment) this.fragmentsStack.get(this.fragmentsStack.size() - 2);
                BaseFragment fragment = (BaseFragment) this.fragmentsStack.get(this.fragmentsStack.size() - 1);
                if (VERSION.SDK_INT >= 21) {
                    fragment.fragmentView.setOutlineProvider(null);
                    fragment.fragmentView.setClipToOutline(false);
                }
                LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) fragment.fragmentView.getLayoutParams();
                layoutParams.leftMargin = 0;
                layoutParams.rightMargin = 0;
                layoutParams.bottomMargin = 0;
                layoutParams.topMargin = 0;
                fragment.fragmentView.setLayoutParams(layoutParams);
                presentFragmentInternalRemoveOld(false, prevFragment);
                AnimatorSet animatorSet = new AnimatorSet();
                animatorSet.playTogether(new Animator[]{ObjectAnimator.ofFloat(fragment.fragmentView, "scaleX", new float[]{1.0f, 1.05f, 1.0f}), ObjectAnimator.ofFloat(fragment.fragmentView, "scaleY", new float[]{1.0f, 1.05f, 1.0f})});
                animatorSet.setDuration(200);
                animatorSet.setInterpolator(new CubicBezierInterpolator(0.42d, 0.0d, 0.58d, 1.0d));
                animatorSet.start();
                performHapticFeedback(3);
                fragment.setInPreviewMode(false);
            }
            if (currentTranslation != nextTranslation) {
                this.containerView.setTranslationY(nextTranslation);
                invalidate();
            }
        }
    }

    public void finishPreviewFragment() {
        if (this.inPreviewMode || this.transitionAnimationPreviewMode) {
            closeLastFragment(true);
        }
    }

    public void closeLastFragment(boolean animated) {
        if ((this.delegate == null || this.delegate.needCloseLastFragment(this)) && !checkTransitionAnimation() && !this.fragmentsStack.isEmpty()) {
            if (this.parentActivity.getCurrentFocus() != null) {
                AndroidUtilities.hideKeyboard(this.parentActivity.getCurrentFocus());
            }
            setInnerTranslationX(0.0f);
            boolean needAnimation = this.inPreviewMode || this.transitionAnimationPreviewMode || (animated && MessagesController.getGlobalMainSettings().getBoolean("view_animations", true));
            BaseFragment currentFragment = (BaseFragment) this.fragmentsStack.get(this.fragmentsStack.size() - 1);
            BaseFragment previousFragment = null;
            if (this.fragmentsStack.size() > 1) {
                previousFragment = (BaseFragment) this.fragmentsStack.get(this.fragmentsStack.size() - 2);
            }
            if (previousFragment != null) {
                ViewGroup parent;
                LinearLayoutContainer temp = this.containerView;
                this.containerView = this.containerViewBack;
                this.containerViewBack = temp;
                this.containerView.setVisibility(0);
                previousFragment.setParentLayout(this);
                View fragmentView = previousFragment.fragmentView;
                if (fragmentView == null) {
                    fragmentView = previousFragment.createView(this.parentActivity);
                } else {
                    parent = (ViewGroup) fragmentView.getParent();
                    if (parent != null) {
                        previousFragment.onRemoveFromParent();
                        parent.removeView(fragmentView);
                    }
                }
                if (previousFragment.actionBar != null && previousFragment.actionBar.getAddToContainer()) {
                    if (this.removeActionBarExtraHeight) {
                        previousFragment.actionBar.setOccupyStatusBar(false);
                    }
                    parent = (ViewGroup) previousFragment.actionBar.getParent();
                    if (parent != null) {
                        parent.removeView(previousFragment.actionBar);
                    }
                    this.containerView.addView(previousFragment.actionBar);
                    previousFragment.actionBar.setTitleOverlayText(this.titleOverlayText, this.subtitleOverlayText, this.overlayAction);
                }
                this.containerView.addView(fragmentView);
                LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) fragmentView.getLayoutParams();
                layoutParams.width = -1;
                layoutParams.height = -1;
                layoutParams.leftMargin = 0;
                layoutParams.rightMargin = 0;
                layoutParams.bottomMargin = 0;
                layoutParams.topMargin = 0;
                fragmentView.setLayoutParams(layoutParams);
                previousFragment.onTransitionAnimationStart(true, true);
                currentFragment.onTransitionAnimationStart(false, false);
                previousFragment.onResume();
                this.currentActionBar = previousFragment.actionBar;
                if (!previousFragment.hasOwnBackground && fragmentView.getBackground() == null) {
                    fragmentView.setBackgroundColor(Theme.getColor(Theme.key_windowBackgroundWhite));
                }
                if (!needAnimation) {
                    closeLastFragmentInternalRemoveOld(currentFragment);
                }
                if (needAnimation) {
                    this.transitionAnimationStartTime = System.currentTimeMillis();
                    this.transitionAnimationInProgress = true;
                    this.onCloseAnimationEndRunnable = new ActionBarLayout$$Lambda$3(this, currentFragment, previousFragment);
                    AnimatorSet animation = null;
                    if (!(this.inPreviewMode || this.transitionAnimationPreviewMode)) {
                        animation = currentFragment.onCustomTransitionAnimation(false, new ActionBarLayout$$Lambda$4(this));
                    }
                    if (animation != null) {
                        this.currentAnimation = animation;
                        return;
                    } else if (this.containerView.isKeyboardVisible || this.containerViewBack.isKeyboardVisible) {
                        this.waitingForKeyboardCloseRunnable = new Runnable() {
                            public void run() {
                                if (ActionBarLayout.this.waitingForKeyboardCloseRunnable == this) {
                                    ActionBarLayout.this.waitingForKeyboardCloseRunnable = null;
                                    ActionBarLayout.this.startLayoutAnimation(false, true, false);
                                }
                            }
                        };
                        AndroidUtilities.runOnUIThread(this.waitingForKeyboardCloseRunnable, 200);
                        return;
                    } else {
                        boolean z = this.inPreviewMode || this.transitionAnimationPreviewMode;
                        startLayoutAnimation(false, true, z);
                        return;
                    }
                }
                currentFragment.onTransitionAnimationEnd(false, false);
                previousFragment.onTransitionAnimationEnd(true, true);
                previousFragment.onBecomeFullyVisible();
            } else if (this.useAlphaAnimations) {
                this.transitionAnimationStartTime = System.currentTimeMillis();
                this.transitionAnimationInProgress = true;
                this.onCloseAnimationEndRunnable = new ActionBarLayout$$Lambda$5(this, currentFragment);
                ArrayList<Animator> animators = new ArrayList();
                animators.add(ObjectAnimator.ofFloat(this, "alpha", new float[]{1.0f, 0.0f}));
                if (this.backgroundView != null) {
                    animators.add(ObjectAnimator.ofFloat(this.backgroundView, "alpha", new float[]{1.0f, 0.0f}));
                }
                this.currentAnimation = new AnimatorSet();
                this.currentAnimation.playTogether(animators);
                this.currentAnimation.setInterpolator(this.accelerateDecelerateInterpolator);
                this.currentAnimation.setDuration(200);
                this.currentAnimation.addListener(new AnimatorListenerAdapter() {
                    public void onAnimationStart(Animator animation) {
                        ActionBarLayout.this.transitionAnimationStartTime = System.currentTimeMillis();
                    }

                    public void onAnimationEnd(Animator animation) {
                        ActionBarLayout.this.onAnimationEndCheck(false);
                    }
                });
                this.currentAnimation.start();
            } else {
                removeFragmentFromStackInternal(currentFragment);
                setVisibility(8);
                if (this.backgroundView != null) {
                    this.backgroundView.setVisibility(8);
                }
            }
        }
    }

    final /* synthetic */ void lambda$closeLastFragment$3$ActionBarLayout(BaseFragment currentFragment, BaseFragment previousFragmentFinal) {
        if (this.inPreviewMode || this.transitionAnimationPreviewMode) {
            this.containerViewBack.setScaleX(1.0f);
            this.containerViewBack.setScaleY(1.0f);
            this.inPreviewMode = false;
            this.transitionAnimationPreviewMode = false;
        } else {
            this.containerViewBack.setTranslationX(0.0f);
        }
        closeLastFragmentInternalRemoveOld(currentFragment);
        currentFragment.onTransitionAnimationEnd(false, false);
        previousFragmentFinal.onTransitionAnimationEnd(true, true);
        previousFragmentFinal.onBecomeFullyVisible();
    }

    final /* synthetic */ void lambda$closeLastFragment$4$ActionBarLayout() {
        onAnimationEndCheck(false);
    }

    final /* synthetic */ void lambda$closeLastFragment$5$ActionBarLayout(BaseFragment currentFragment) {
        removeFragmentFromStackInternal(currentFragment);
        setVisibility(8);
        if (this.backgroundView != null) {
            this.backgroundView.setVisibility(8);
        }
        if (this.drawerLayoutContainer != null) {
            this.drawerLayoutContainer.setAllowOpenDrawer(true, false);
        }
    }

    public void showLastFragment() {
        if (!this.fragmentsStack.isEmpty()) {
            BaseFragment previousFragment;
            ViewGroup parent;
            for (int a = 0; a < this.fragmentsStack.size() - 1; a++) {
                previousFragment = (BaseFragment) this.fragmentsStack.get(a);
                if (previousFragment.actionBar != null && previousFragment.actionBar.getAddToContainer()) {
                    parent = (ViewGroup) previousFragment.actionBar.getParent();
                    if (parent != null) {
                        parent.removeView(previousFragment.actionBar);
                    }
                }
                if (previousFragment.fragmentView != null) {
                    parent = (ViewGroup) previousFragment.fragmentView.getParent();
                    if (parent != null) {
                        previousFragment.onPause();
                        previousFragment.onRemoveFromParent();
                        parent.removeView(previousFragment.fragmentView);
                    }
                }
            }
            previousFragment = (BaseFragment) this.fragmentsStack.get(this.fragmentsStack.size() - 1);
            previousFragment.setParentLayout(this);
            View fragmentView = previousFragment.fragmentView;
            if (fragmentView == null) {
                fragmentView = previousFragment.createView(this.parentActivity);
            } else {
                parent = (ViewGroup) fragmentView.getParent();
                if (parent != null) {
                    previousFragment.onRemoveFromParent();
                    parent.removeView(fragmentView);
                }
            }
            if (previousFragment.actionBar != null && previousFragment.actionBar.getAddToContainer()) {
                if (this.removeActionBarExtraHeight) {
                    previousFragment.actionBar.setOccupyStatusBar(false);
                }
                parent = (ViewGroup) previousFragment.actionBar.getParent();
                if (parent != null) {
                    parent.removeView(previousFragment.actionBar);
                }
                this.containerView.addView(previousFragment.actionBar);
                previousFragment.actionBar.setTitleOverlayText(this.titleOverlayText, this.subtitleOverlayText, this.overlayAction);
            }
            this.containerView.addView(fragmentView, LayoutHelper.createLinear(-1, -1));
            previousFragment.onResume();
            this.currentActionBar = previousFragment.actionBar;
            if (!previousFragment.hasOwnBackground && fragmentView.getBackground() == null) {
                fragmentView.setBackgroundColor(Theme.getColor(Theme.key_windowBackgroundWhite));
            }
        }
    }

    private void removeFragmentFromStackInternal(BaseFragment fragment) {
        fragment.onPause();
        fragment.onFragmentDestroy();
        fragment.setParentLayout(null);
        this.fragmentsStack.remove(fragment);
    }

    public void removeFragmentFromStack(int num) {
        if (num < this.fragmentsStack.size()) {
            removeFragmentFromStackInternal((BaseFragment) this.fragmentsStack.get(num));
        }
    }

    public void removeFragmentFromStack(BaseFragment fragment) {
        if (this.useAlphaAnimations && this.fragmentsStack.size() == 1 && AndroidUtilities.isTablet()) {
            closeLastFragment(true);
        } else {
            removeFragmentFromStackInternal(fragment);
        }
    }

    public void removeAllFragments() {
        int a = 0;
        while (this.fragmentsStack.size() > 0) {
            removeFragmentFromStackInternal((BaseFragment) this.fragmentsStack.get(a));
            a = (a - 1) + 1;
        }
    }

    @Keep
    public void setThemeAnimationValue(float value) {
        this.themeAnimationValue = value;
        for (int j = 0; j < 2; j++) {
            if (this.themeAnimatorDescriptions[j] != null) {
                for (int i = 0; i < this.themeAnimatorDescriptions[j].length; i++) {
                    int rE = Color.red(this.animateEndColors[j][i]);
                    int gE = Color.green(this.animateEndColors[j][i]);
                    int bE = Color.blue(this.animateEndColors[j][i]);
                    int aE = Color.alpha(this.animateEndColors[j][i]);
                    int rS = Color.red(this.animateStartColors[j][i]);
                    int gS = Color.green(this.animateStartColors[j][i]);
                    int bS = Color.blue(this.animateStartColors[j][i]);
                    int aS = Color.alpha(this.animateStartColors[j][i]);
                    this.themeAnimatorDescriptions[j][i].setColor(Color.argb(Math.min(255, (int) (((float) aS) + (((float) (aE - aS)) * value))), Math.min(255, (int) (((float) rS) + (((float) (rE - rS)) * value))), Math.min(255, (int) (((float) gS) + (((float) (gE - gS)) * value))), Math.min(255, (int) (((float) bS) + (((float) (bE - bS)) * value)))), false, false);
                }
                if (this.themeAnimatorDelegate[j] != null) {
                    this.themeAnimatorDelegate[j].didSetColor();
                }
            }
        }
    }

    @Keep
    public float getThemeAnimationValue() {
        return this.themeAnimationValue;
    }

    public void animateThemedValues(ThemeInfo theme) {
        int i = 1;
        if (this.transitionAnimationInProgress || this.startedTracking) {
            this.animateThemeAfterAnimation = true;
            this.animateSetThemeAfterAnimation = theme;
            return;
        }
        BaseFragment fragment;
        if (this.themeAnimatorSet != null) {
            this.themeAnimatorSet.cancel();
            this.themeAnimatorSet = null;
        }
        boolean startAnimation = false;
        for (int i2 = 0; i2 < 2; i2++) {
            int a;
            if (i2 == 0) {
                fragment = getLastFragment();
            } else if (this.inPreviewMode || this.transitionAnimationPreviewMode || this.fragmentsStack.size() > 1) {
                fragment = (BaseFragment) this.fragmentsStack.get(this.fragmentsStack.size() - 2);
            } else {
                this.themeAnimatorDescriptions[i2] = null;
                this.animateStartColors[i2] = null;
                this.animateEndColors[i2] = null;
                this.themeAnimatorDelegate[i2] = null;
            }
            if (fragment != null) {
                startAnimation = true;
                this.themeAnimatorDescriptions[i2] = fragment.getThemeDescriptions();
                this.animateStartColors[i2] = new int[this.themeAnimatorDescriptions[i2].length];
                for (a = 0; a < this.themeAnimatorDescriptions[i2].length; a++) {
                    this.animateStartColors[i2][a] = this.themeAnimatorDescriptions[i2][a].getSetColor();
                    ThemeDescriptionDelegate delegate = this.themeAnimatorDescriptions[i2][a].setDelegateDisabled();
                    if (this.themeAnimatorDelegate[i2] == null && delegate != null) {
                        this.themeAnimatorDelegate[i2] = delegate;
                    }
                }
                if (i2 == 0) {
                    Theme.applyTheme(theme, true);
                }
                this.animateEndColors[i2] = new int[this.themeAnimatorDescriptions[i2].length];
                for (a = 0; a < this.themeAnimatorDescriptions[i2].length; a++) {
                    this.animateEndColors[i2][a] = this.themeAnimatorDescriptions[i2][a].getSetColor();
                }
            }
        }
        if (startAnimation) {
            this.themeAnimatorSet = new AnimatorSet();
            this.themeAnimatorSet.addListener(new AnimatorListenerAdapter() {
                public void onAnimationEnd(Animator animation) {
                    if (animation.equals(ActionBarLayout.this.themeAnimatorSet)) {
                        for (int a = 0; a < 2; a++) {
                            ActionBarLayout.this.themeAnimatorDescriptions[a] = null;
                            ActionBarLayout.this.animateStartColors[a] = null;
                            ActionBarLayout.this.animateEndColors[a] = null;
                            ActionBarLayout.this.themeAnimatorDelegate[a] = null;
                        }
                        ActionBarLayout.this.themeAnimatorSet = null;
                    }
                }

                public void onAnimationCancel(Animator animation) {
                    if (animation.equals(ActionBarLayout.this.themeAnimatorSet)) {
                        for (int a = 0; a < 2; a++) {
                            ActionBarLayout.this.themeAnimatorDescriptions[a] = null;
                            ActionBarLayout.this.animateStartColors[a] = null;
                            ActionBarLayout.this.animateEndColors[a] = null;
                            ActionBarLayout.this.themeAnimatorDelegate[a] = null;
                        }
                        ActionBarLayout.this.themeAnimatorSet = null;
                    }
                }
            });
            this.themeAnimatorSet.playTogether(new Animator[]{ObjectAnimator.ofFloat(this, "themeAnimationValue", new float[]{0.0f, 1.0f})});
            this.themeAnimatorSet.setDuration(200);
            this.themeAnimatorSet.start();
            int size = this.fragmentsStack.size();
            if (this.inPreviewMode || this.transitionAnimationPreviewMode) {
                i = 2;
            }
            int count = size - i;
            for (a = 0; a < count; a++) {
                fragment = (BaseFragment) this.fragmentsStack.get(a);
                fragment.clearViews();
                fragment.setParentLayout(this);
            }
        }
    }

    public void rebuildAllFragmentViews(boolean last, boolean showLastAfter) {
        if (this.transitionAnimationInProgress || this.startedTracking) {
            this.rebuildAfterAnimation = true;
            this.rebuildLastAfterAnimation = last;
            this.showLastAfterAnimation = showLastAfter;
            return;
        }
        int size = this.fragmentsStack.size();
        if (!last) {
            size--;
        }
        if (this.inPreviewMode) {
            size--;
        }
        for (int a = 0; a < size; a++) {
            ((BaseFragment) this.fragmentsStack.get(a)).clearViews();
            ((BaseFragment) this.fragmentsStack.get(a)).setParentLayout(this);
        }
        if (this.delegate != null) {
            this.delegate.onRebuildAllFragments(this, last);
        }
        if (showLastAfter) {
            showLastFragment();
        }
    }

    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (!(keyCode != 82 || checkTransitionAnimation() || this.startedTracking || this.currentActionBar == null)) {
            this.currentActionBar.onMenuButtonPressed();
        }
        return super.onKeyUp(keyCode, event);
    }

    public void onActionModeStarted(Object mode) {
        if (this.currentActionBar != null) {
            this.currentActionBar.setVisibility(8);
        }
        this.inActionMode = true;
    }

    public void onActionModeFinished(Object mode) {
        if (this.currentActionBar != null) {
            this.currentActionBar.setVisibility(0);
        }
        this.inActionMode = false;
    }

    private void onCloseAnimationEnd() {
        if (this.transitionAnimationInProgress && this.onCloseAnimationEndRunnable != null) {
            this.transitionAnimationInProgress = false;
            this.transitionAnimationPreviewMode = false;
            this.transitionAnimationStartTime = 0;
            this.onCloseAnimationEndRunnable.run();
            this.onCloseAnimationEndRunnable = null;
            checkNeedRebuild();
        }
    }

    private void checkNeedRebuild() {
        if (this.rebuildAfterAnimation) {
            rebuildAllFragmentViews(this.rebuildLastAfterAnimation, this.showLastAfterAnimation);
            this.rebuildAfterAnimation = false;
        } else if (this.animateThemeAfterAnimation) {
            animateThemedValues(this.animateSetThemeAfterAnimation);
            this.animateSetThemeAfterAnimation = null;
            this.animateThemeAfterAnimation = false;
        }
    }

    private void onOpenAnimationEnd() {
        if (this.transitionAnimationInProgress && this.onOpenAnimationEndRunnable != null) {
            this.transitionAnimationInProgress = false;
            this.transitionAnimationPreviewMode = false;
            this.transitionAnimationStartTime = 0;
            this.onOpenAnimationEndRunnable.run();
            this.onOpenAnimationEndRunnable = null;
            checkNeedRebuild();
        }
    }

    public void startActivityForResult(Intent intent, int requestCode) {
        if (this.parentActivity != null) {
            if (this.transitionAnimationInProgress) {
                if (this.currentAnimation != null) {
                    this.currentAnimation.cancel();
                    this.currentAnimation = null;
                }
                if (this.onCloseAnimationEndRunnable != null) {
                    onCloseAnimationEnd();
                } else if (this.onOpenAnimationEndRunnable != null) {
                    onOpenAnimationEnd();
                }
                this.containerView.invalidate();
                if (intent != null) {
                    this.parentActivity.startActivityForResult(intent, requestCode);
                }
            } else if (intent != null) {
                this.parentActivity.startActivityForResult(intent, requestCode);
            }
        }
    }

    public void setUseAlphaAnimations(boolean value) {
        this.useAlphaAnimations = value;
    }

    public void setBackgroundView(View view) {
        this.backgroundView = view;
    }

    public void setDrawerLayoutContainer(DrawerLayoutContainer layout) {
        this.drawerLayoutContainer = layout;
    }

    public DrawerLayoutContainer getDrawerLayoutContainer() {
        return this.drawerLayoutContainer;
    }

    public void setRemoveActionBarExtraHeight(boolean value) {
        this.removeActionBarExtraHeight = value;
    }

    public void setTitleOverlayText(String title, String subtitle, Runnable action) {
        this.titleOverlayText = title;
        this.subtitleOverlayText = subtitle;
        this.overlayAction = action;
        for (int a = 0; a < this.fragmentsStack.size(); a++) {
            BaseFragment fragment = (BaseFragment) this.fragmentsStack.get(a);
            if (fragment.actionBar != null) {
                fragment.actionBar.setTitleOverlayText(this.titleOverlayText, this.subtitleOverlayText, action);
            }
        }
    }

    public boolean extendActionMode(Menu menu) {
        return !this.fragmentsStack.isEmpty() && ((BaseFragment) this.fragmentsStack.get(this.fragmentsStack.size() - 1)).extendActionMode(menu);
    }

    public boolean hasOverlappingRendering() {
        return false;
    }
}
