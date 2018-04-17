package android.support.v4.view;

import android.content.Context;
import android.content.res.Resources.NotFoundException;
import android.content.res.TypedArray;
import android.database.DataSetObserver;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.ClassLoaderCreator;
import android.os.Parcelable.Creator;
import android.os.SystemClock;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.accessibility.AccessibilityNodeInfoCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.FocusFinder;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SoundEffectConstants;
import android.view.VelocityTracker;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.accessibility.AccessibilityEvent;
import android.view.animation.Interpolator;
import android.widget.EdgeEffect;
import android.widget.Scroller;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import org.telegram.messenger.MessagesController;

public class ViewPager extends ViewGroup {
    private static final int CLOSE_ENOUGH = 2;
    private static final Comparator<ItemInfo> COMPARATOR = new Comparator<ItemInfo>() {
        public int compare(ItemInfo lhs, ItemInfo rhs) {
            return lhs.position - rhs.position;
        }
    };
    private static final boolean DEBUG = false;
    private static final int DEFAULT_GUTTER_SIZE = 16;
    private static final int DEFAULT_OFFSCREEN_PAGES = 1;
    private static final int DRAW_ORDER_DEFAULT = 0;
    private static final int DRAW_ORDER_FORWARD = 1;
    private static final int DRAW_ORDER_REVERSE = 2;
    private static final int INVALID_POINTER = -1;
    static final int[] LAYOUT_ATTRS = new int[]{16842931};
    private static final int MAX_SETTLE_DURATION = 600;
    private static final int MIN_DISTANCE_FOR_FLING = 25;
    private static final int MIN_FLING_VELOCITY = 400;
    public static final int SCROLL_STATE_DRAGGING = 1;
    public static final int SCROLL_STATE_IDLE = 0;
    public static final int SCROLL_STATE_SETTLING = 2;
    private static final String TAG = "ViewPager";
    private static final boolean USE_CACHE = false;
    private static final Interpolator sInterpolator = new Interpolator() {
        public float getInterpolation(float t) {
            t -= 1.0f;
            return ((((t * t) * t) * t) * t) + 1.0f;
        }
    };
    private static final ViewPositionComparator sPositionComparator = new ViewPositionComparator();
    private int mActivePointerId = -1;
    PagerAdapter mAdapter;
    private List<OnAdapterChangeListener> mAdapterChangeListeners;
    private int mBottomPageBounds;
    private boolean mCalledSuper;
    private int mChildHeightMeasureSpec;
    private int mChildWidthMeasureSpec;
    private int mCloseEnough;
    int mCurItem;
    private int mDecorChildCount;
    private int mDefaultGutterSize;
    private int mDrawingOrder;
    private ArrayList<View> mDrawingOrderedChildren;
    private final Runnable mEndScrollRunnable = new Runnable() {
        public void run() {
            ViewPager.this.setScrollState(0);
            ViewPager.this.populate();
        }
    };
    private int mExpectedAdapterCount;
    private long mFakeDragBeginTime;
    private boolean mFakeDragging;
    private boolean mFirstLayout = true;
    private float mFirstOffset = -3.4028235E38f;
    private int mFlingDistance;
    private int mGutterSize;
    private boolean mInLayout;
    private float mInitialMotionX;
    private float mInitialMotionY;
    private OnPageChangeListener mInternalPageChangeListener;
    private boolean mIsBeingDragged;
    private boolean mIsScrollStarted;
    private boolean mIsUnableToDrag;
    private final ArrayList<ItemInfo> mItems = new ArrayList();
    private float mLastMotionX;
    private float mLastMotionY;
    private float mLastOffset = Float.MAX_VALUE;
    private EdgeEffect mLeftEdge;
    private Drawable mMarginDrawable;
    private int mMaximumVelocity;
    private int mMinimumVelocity;
    private boolean mNeedCalculatePageOffsets = false;
    private PagerObserver mObserver;
    private int mOffscreenPageLimit = 1;
    private OnPageChangeListener mOnPageChangeListener;
    private List<OnPageChangeListener> mOnPageChangeListeners;
    private int mPageMargin;
    private PageTransformer mPageTransformer;
    private int mPageTransformerLayerType;
    private boolean mPopulatePending;
    private Parcelable mRestoredAdapterState = null;
    private ClassLoader mRestoredClassLoader = null;
    private int mRestoredCurItem = -1;
    private EdgeEffect mRightEdge;
    private int mScrollState = 0;
    private Scroller mScroller;
    private boolean mScrollingCacheEnabled;
    private final ItemInfo mTempItem = new ItemInfo();
    private final Rect mTempRect = new Rect();
    private int mTopPageBounds;
    private int mTouchSlop;
    private VelocityTracker mVelocityTracker;

    @Target({ElementType.TYPE})
    @Retention(RetentionPolicy.RUNTIME)
    @Inherited
    public @interface DecorView {
    }

    static class ItemInfo {
        Object object;
        float offset;
        int position;
        boolean scrolling;
        float widthFactor;

        ItemInfo() {
        }
    }

    public static class LayoutParams extends android.view.ViewGroup.LayoutParams {
        int childIndex;
        public int gravity;
        public boolean isDecor;
        boolean needsMeasure;
        int position;
        float widthFactor = 0.0f;

        public LayoutParams() {
            super(-1, -1);
        }

        public LayoutParams(Context context, AttributeSet attrs) {
            super(context, attrs);
            TypedArray a = context.obtainStyledAttributes(attrs, ViewPager.LAYOUT_ATTRS);
            this.gravity = a.getInteger(0, 48);
            a.recycle();
        }
    }

    public interface OnAdapterChangeListener {
        void onAdapterChanged(ViewPager viewPager, PagerAdapter pagerAdapter, PagerAdapter pagerAdapter2);
    }

    public interface OnPageChangeListener {
        void onPageScrollStateChanged(int i);

        void onPageScrolled(int i, float f, int i2);

        void onPageSelected(int i);
    }

    public interface PageTransformer {
        void transformPage(View view, float f);
    }

    private class PagerObserver extends DataSetObserver {
        PagerObserver() {
        }

        public void onChanged() {
            ViewPager.this.dataSetChanged();
        }

        public void onInvalidated() {
            ViewPager.this.dataSetChanged();
        }
    }

    static class ViewPositionComparator implements Comparator<View> {
        ViewPositionComparator() {
        }

        public int compare(View lhs, View rhs) {
            LayoutParams llp = (LayoutParams) lhs.getLayoutParams();
            LayoutParams rlp = (LayoutParams) rhs.getLayoutParams();
            if (llp.isDecor == rlp.isDecor) {
                return llp.position - rlp.position;
            }
            return llp.isDecor ? 1 : -1;
        }
    }

    class MyAccessibilityDelegate extends AccessibilityDelegateCompat {
        MyAccessibilityDelegate() {
        }

        public void onInitializeAccessibilityEvent(View host, AccessibilityEvent event) {
            super.onInitializeAccessibilityEvent(host, event);
            event.setClassName(ViewPager.class.getName());
            event.setScrollable(canScroll());
            if (event.getEventType() == 4096 && ViewPager.this.mAdapter != null) {
                event.setItemCount(ViewPager.this.mAdapter.getCount());
                event.setFromIndex(ViewPager.this.mCurItem);
                event.setToIndex(ViewPager.this.mCurItem);
            }
        }

        public void onInitializeAccessibilityNodeInfo(View host, AccessibilityNodeInfoCompat info) {
            super.onInitializeAccessibilityNodeInfo(host, info);
            info.setClassName(ViewPager.class.getName());
            info.setScrollable(canScroll());
            if (ViewPager.this.canScrollHorizontally(1)) {
                info.addAction(4096);
            }
            if (ViewPager.this.canScrollHorizontally(-1)) {
                info.addAction(MessagesController.UPDATE_MASK_CHANNEL);
            }
        }

        public boolean performAccessibilityAction(View host, int action, Bundle args) {
            if (super.performAccessibilityAction(host, action, args)) {
                return true;
            }
            if (action != 4096) {
                if (action != MessagesController.UPDATE_MASK_CHANNEL || !ViewPager.this.canScrollHorizontally(-1)) {
                    return false;
                }
                ViewPager.this.setCurrentItem(ViewPager.this.mCurItem - 1);
                return true;
            } else if (!ViewPager.this.canScrollHorizontally(1)) {
                return false;
            } else {
                ViewPager.this.setCurrentItem(ViewPager.this.mCurItem + 1);
                return true;
            }
        }

        private boolean canScroll() {
            return ViewPager.this.mAdapter != null && ViewPager.this.mAdapter.getCount() > 1;
        }
    }

    public static class SavedState extends AbsSavedState {
        public static final Creator<SavedState> CREATOR = new ClassLoaderCreator<SavedState>() {
            public SavedState createFromParcel(Parcel in, ClassLoader loader) {
                return new SavedState(in, loader);
            }

            public SavedState createFromParcel(Parcel in) {
                return new SavedState(in, null);
            }

            public SavedState[] newArray(int size) {
                return new SavedState[size];
            }
        };
        Parcelable adapterState;
        ClassLoader loader;
        int position;

        public SavedState(Parcelable superState) {
            super(superState);
        }

        public void writeToParcel(Parcel out, int flags) {
            super.writeToParcel(out, flags);
            out.writeInt(this.position);
            out.writeParcelable(this.adapterState, flags);
        }

        public String toString() {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("FragmentPager.SavedState{");
            stringBuilder.append(Integer.toHexString(System.identityHashCode(this)));
            stringBuilder.append(" position=");
            stringBuilder.append(this.position);
            stringBuilder.append("}");
            return stringBuilder.toString();
        }

        SavedState(Parcel in, ClassLoader loader) {
            super(in, loader);
            if (loader == null) {
                loader = getClass().getClassLoader();
            }
            this.position = in.readInt();
            this.adapterState = in.readParcelable(loader);
            this.loader = loader;
        }
    }

    private void recomputeScrollPosition(int r1, int r2, int r3, int r4) {
        /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.DecodeException: Load method exception in method: android.support.v4.view.ViewPager.recomputeScrollPosition(int, int, int, int):void
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
        if (r8 <= 0) goto L_0x004b;
    L_0x0002:
        r0 = r6.mItems;
        r0 = r0.isEmpty();
        if (r0 != 0) goto L_0x004b;
    L_0x000a:
        r0 = r6.mScroller;
        r0 = r0.isFinished();
        if (r0 != 0) goto L_0x0021;
    L_0x0012:
        r0 = r6.mScroller;
        r1 = r6.getCurrentItem();
        r2 = r6.getClientWidth();
        r1 = r1 * r2;
        r0.setFinalX(r1);
        goto L_0x007d;
    L_0x0021:
        r0 = r6.getPaddingLeft();
        r0 = r7 - r0;
        r1 = r6.getPaddingRight();
        r0 = r0 - r1;
        r0 = r0 + r9;
        r1 = r6.getPaddingLeft();
        r1 = r8 - r1;
        r2 = r6.getPaddingRight();
        r1 = r1 - r2;
        r1 = r1 + r10;
        r2 = r6.getScrollX();
        r3 = (float) r2;
        r4 = (float) r1;
        r3 = r3 / r4;
        r4 = (float) r0;
        r4 = r4 * r3;
        r4 = (int) r4;
        r5 = r6.getScrollY();
        r6.scrollTo(r4, r5);
        goto L_0x007d;
    L_0x004b:
        r0 = r6.mCurItem;
        r0 = r6.infoForPosition(r0);
        if (r0 == 0) goto L_0x005c;
    L_0x0053:
        r1 = r0.offset;
        r2 = r6.mLastOffset;
        r1 = java.lang.Math.min(r1, r2);
        goto L_0x005d;
    L_0x005c:
        r1 = 0;
        r2 = r6.getPaddingLeft();
        r2 = r7 - r2;
        r3 = r6.getPaddingRight();
        r2 = r2 - r3;
        r2 = (float) r2;
        r2 = r2 * r1;
        r2 = (int) r2;
        r3 = r6.getScrollX();
        if (r2 == r3) goto L_0x007d;
        r3 = 0;
        r6.completeScroll(r3);
        r3 = r6.getScrollY();
        r6.scrollTo(r2, r3);
    L_0x007d:
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.support.v4.view.ViewPager.recomputeScrollPosition(int, int, int, int):void");
    }

    public ViewPager(Context context) {
        super(context);
        initViewPager();
    }

    public ViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        initViewPager();
    }

    void initViewPager() {
        setWillNotDraw(false);
        setDescendantFocusability(262144);
        setFocusable(true);
        Context context = getContext();
        this.mScroller = new Scroller(context, sInterpolator);
        ViewConfiguration configuration = ViewConfiguration.get(context);
        float density = context.getResources().getDisplayMetrics().density;
        this.mTouchSlop = configuration.getScaledPagingTouchSlop();
        this.mMinimumVelocity = (int) (400.0f * density);
        this.mMaximumVelocity = configuration.getScaledMaximumFlingVelocity();
        this.mLeftEdge = new EdgeEffect(context);
        this.mRightEdge = new EdgeEffect(context);
        this.mFlingDistance = (int) (25.0f * density);
        this.mCloseEnough = (int) (2.0f * density);
        this.mDefaultGutterSize = (int) (16.0f * density);
        ViewCompat.setAccessibilityDelegate(this, new MyAccessibilityDelegate());
        if (ViewCompat.getImportantForAccessibility(this) == 0) {
            ViewCompat.setImportantForAccessibility(this, 1);
        }
        ViewCompat.setOnApplyWindowInsetsListener(this, new OnApplyWindowInsetsListener() {
            private final Rect mTempRect = new Rect();

            public WindowInsetsCompat onApplyWindowInsets(View v, WindowInsetsCompat originalInsets) {
                WindowInsetsCompat applied = ViewCompat.onApplyWindowInsets(v, originalInsets);
                if (applied.isConsumed()) {
                    return applied;
                }
                Rect res = this.mTempRect;
                res.left = applied.getSystemWindowInsetLeft();
                res.top = applied.getSystemWindowInsetTop();
                res.right = applied.getSystemWindowInsetRight();
                res.bottom = applied.getSystemWindowInsetBottom();
                int count = ViewPager.this.getChildCount();
                for (int i = 0; i < count; i++) {
                    WindowInsetsCompat childInsets = ViewCompat.dispatchApplyWindowInsets(ViewPager.this.getChildAt(i), applied);
                    res.left = Math.min(childInsets.getSystemWindowInsetLeft(), res.left);
                    res.top = Math.min(childInsets.getSystemWindowInsetTop(), res.top);
                    res.right = Math.min(childInsets.getSystemWindowInsetRight(), res.right);
                    res.bottom = Math.min(childInsets.getSystemWindowInsetBottom(), res.bottom);
                }
                return applied.replaceSystemWindowInsets(res.left, res.top, res.right, res.bottom);
            }
        });
    }

    protected void onDetachedFromWindow() {
        removeCallbacks(this.mEndScrollRunnable);
        if (!(this.mScroller == null || this.mScroller.isFinished())) {
            this.mScroller.abortAnimation();
        }
        super.onDetachedFromWindow();
    }

    void setScrollState(int newState) {
        if (this.mScrollState != newState) {
            this.mScrollState = newState;
            if (this.mPageTransformer != null) {
                enableLayers(newState != 0);
            }
            dispatchOnScrollStateChanged(newState);
        }
    }

    public void setAdapter(PagerAdapter adapter) {
        if (this.mAdapter != null) {
            this.mAdapter.setViewPagerObserver(null);
            this.mAdapter.startUpdate((ViewGroup) this);
            for (int i = 0; i < this.mItems.size(); i++) {
                ItemInfo ii = (ItemInfo) this.mItems.get(i);
                this.mAdapter.destroyItem((ViewGroup) this, ii.position, ii.object);
            }
            this.mAdapter.finishUpdate((ViewGroup) this);
            this.mItems.clear();
            removeNonDecorViews();
            this.mCurItem = 0;
            scrollTo(0, 0);
        }
        PagerAdapter oldAdapter = this.mAdapter;
        this.mAdapter = adapter;
        this.mExpectedAdapterCount = 0;
        if (this.mAdapter != null) {
            if (this.mObserver == null) {
                this.mObserver = new PagerObserver();
            }
            this.mAdapter.setViewPagerObserver(this.mObserver);
            this.mPopulatePending = false;
            boolean wasFirstLayout = this.mFirstLayout;
            this.mFirstLayout = true;
            this.mExpectedAdapterCount = this.mAdapter.getCount();
            if (this.mRestoredCurItem >= 0) {
                this.mAdapter.restoreState(this.mRestoredAdapterState, this.mRestoredClassLoader);
                setCurrentItemInternal(this.mRestoredCurItem, false, true);
                this.mRestoredCurItem = -1;
                this.mRestoredAdapterState = null;
                this.mRestoredClassLoader = null;
            } else if (wasFirstLayout) {
                requestLayout();
            } else {
                populate();
            }
        }
        if (this.mAdapterChangeListeners != null && !this.mAdapterChangeListeners.isEmpty()) {
            int count = this.mAdapterChangeListeners.size();
            for (int i2 = 0; i2 < count; i2++) {
                ((OnAdapterChangeListener) this.mAdapterChangeListeners.get(i2)).onAdapterChanged(this, oldAdapter, adapter);
            }
        }
    }

    private void removeNonDecorViews() {
        int i = 0;
        while (i < getChildCount()) {
            if (!((LayoutParams) getChildAt(i).getLayoutParams()).isDecor) {
                removeViewAt(i);
                i--;
            }
            i++;
        }
    }

    public PagerAdapter getAdapter() {
        return this.mAdapter;
    }

    public void addOnAdapterChangeListener(OnAdapterChangeListener listener) {
        if (this.mAdapterChangeListeners == null) {
            this.mAdapterChangeListeners = new ArrayList();
        }
        this.mAdapterChangeListeners.add(listener);
    }

    public void removeOnAdapterChangeListener(OnAdapterChangeListener listener) {
        if (this.mAdapterChangeListeners != null) {
            this.mAdapterChangeListeners.remove(listener);
        }
    }

    private int getClientWidth() {
        return (getMeasuredWidth() - getPaddingLeft()) - getPaddingRight();
    }

    public void setCurrentItem(int item) {
        this.mPopulatePending = false;
        setCurrentItemInternal(item, this.mFirstLayout ^ 1, false);
    }

    public void setCurrentItem(int item, boolean smoothScroll) {
        this.mPopulatePending = false;
        setCurrentItemInternal(item, smoothScroll, false);
    }

    public int getCurrentItem() {
        return this.mCurItem;
    }

    void setCurrentItemInternal(int item, boolean smoothScroll, boolean always) {
        setCurrentItemInternal(item, smoothScroll, always, 0);
    }

    void setCurrentItemInternal(int item, boolean smoothScroll, boolean always, int velocity) {
        if (this.mAdapter != null) {
            if (this.mAdapter.getCount() > 0) {
                if (always || this.mCurItem != item || this.mItems.size() == 0) {
                    boolean dispatchSelected = true;
                    if (item < 0) {
                        item = 0;
                    } else if (item >= this.mAdapter.getCount()) {
                        item = this.mAdapter.getCount() - 1;
                    }
                    int pageLimit = this.mOffscreenPageLimit;
                    if (item > this.mCurItem + pageLimit || item < this.mCurItem - pageLimit) {
                        for (int i = 0; i < this.mItems.size(); i++) {
                            ((ItemInfo) this.mItems.get(i)).scrolling = true;
                        }
                    }
                    if (this.mCurItem == item) {
                        dispatchSelected = false;
                    }
                    if (this.mFirstLayout) {
                        this.mCurItem = item;
                        if (dispatchSelected) {
                            dispatchOnPageSelected(item);
                        }
                        requestLayout();
                    } else {
                        populate(item);
                        scrollToItem(item, smoothScroll, velocity, dispatchSelected);
                    }
                    return;
                }
                setScrollingCacheEnabled(false);
                return;
            }
        }
        setScrollingCacheEnabled(false);
    }

    private void scrollToItem(int item, boolean smoothScroll, int velocity, boolean dispatchSelected) {
        ItemInfo curInfo = infoForPosition(item);
        int destX = 0;
        if (curInfo != null) {
            destX = (int) (((float) getClientWidth()) * Math.max(this.mFirstOffset, Math.min(curInfo.offset, this.mLastOffset)));
        }
        if (smoothScroll) {
            smoothScrollTo(destX, 0, velocity);
            if (dispatchSelected) {
                dispatchOnPageSelected(item);
                return;
            }
            return;
        }
        if (dispatchSelected) {
            dispatchOnPageSelected(item);
        }
        completeScroll(false);
        scrollTo(destX, 0);
        pageScrolled(destX);
    }

    @Deprecated
    public void setOnPageChangeListener(OnPageChangeListener listener) {
        this.mOnPageChangeListener = listener;
    }

    public void addOnPageChangeListener(OnPageChangeListener listener) {
        if (this.mOnPageChangeListeners == null) {
            this.mOnPageChangeListeners = new ArrayList();
        }
        this.mOnPageChangeListeners.add(listener);
    }

    public void removeOnPageChangeListener(OnPageChangeListener listener) {
        if (this.mOnPageChangeListeners != null) {
            this.mOnPageChangeListeners.remove(listener);
        }
    }

    public void clearOnPageChangeListeners() {
        if (this.mOnPageChangeListeners != null) {
            this.mOnPageChangeListeners.clear();
        }
    }

    public void setPageTransformer(boolean reverseDrawingOrder, PageTransformer transformer) {
        setPageTransformer(reverseDrawingOrder, transformer, 2);
    }

    public void setPageTransformer(boolean reverseDrawingOrder, PageTransformer transformer, int pageLayerType) {
        int i = 1;
        boolean hasTransformer = transformer != null;
        boolean needsPopulate = hasTransformer != (this.mPageTransformer != null);
        this.mPageTransformer = transformer;
        setChildrenDrawingOrderEnabled(hasTransformer);
        if (hasTransformer) {
            if (reverseDrawingOrder) {
                i = 2;
            }
            this.mDrawingOrder = i;
            this.mPageTransformerLayerType = pageLayerType;
        } else {
            this.mDrawingOrder = 0;
        }
        if (needsPopulate) {
            populate();
        }
    }

    protected int getChildDrawingOrder(int childCount, int i) {
        return ((LayoutParams) ((View) this.mDrawingOrderedChildren.get(this.mDrawingOrder == 2 ? (childCount - 1) - i : i)).getLayoutParams()).childIndex;
    }

    OnPageChangeListener setInternalPageChangeListener(OnPageChangeListener listener) {
        OnPageChangeListener oldListener = this.mInternalPageChangeListener;
        this.mInternalPageChangeListener = listener;
        return oldListener;
    }

    public int getOffscreenPageLimit() {
        return this.mOffscreenPageLimit;
    }

    public void setOffscreenPageLimit(int limit) {
        if (limit < 1) {
            String str = TAG;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Requested offscreen page limit ");
            stringBuilder.append(limit);
            stringBuilder.append(" too small; defaulting to ");
            stringBuilder.append(1);
            Log.w(str, stringBuilder.toString());
            limit = 1;
        }
        if (limit != this.mOffscreenPageLimit) {
            this.mOffscreenPageLimit = limit;
            populate();
        }
    }

    public void setPageMargin(int marginPixels) {
        int oldMargin = this.mPageMargin;
        this.mPageMargin = marginPixels;
        int width = getWidth();
        recomputeScrollPosition(width, width, marginPixels, oldMargin);
        requestLayout();
    }

    public int getPageMargin() {
        return this.mPageMargin;
    }

    public void setPageMarginDrawable(Drawable d) {
        this.mMarginDrawable = d;
        if (d != null) {
            refreshDrawableState();
        }
        setWillNotDraw(d == null);
        invalidate();
    }

    public void setPageMarginDrawable(int resId) {
        setPageMarginDrawable(ContextCompat.getDrawable(getContext(), resId));
    }

    protected boolean verifyDrawable(Drawable who) {
        if (!super.verifyDrawable(who)) {
            if (who != this.mMarginDrawable) {
                return false;
            }
        }
        return true;
    }

    protected void drawableStateChanged() {
        super.drawableStateChanged();
        Drawable d = this.mMarginDrawable;
        if (d != null && d.isStateful()) {
            d.setState(getDrawableState());
        }
    }

    float distanceInfluenceForSnapDuration(float f) {
        return (float) Math.sin((double) ((f - 0.5f) * 0.47123894f));
    }

    void smoothScrollTo(int x, int y) {
        smoothScrollTo(x, y, 0);
    }

    void smoothScrollTo(int x, int y, int velocity) {
        ViewPager viewPager = this;
        if (getChildCount() == 0) {
            setScrollingCacheEnabled(false);
            return;
        }
        int sx;
        boolean wasScrolling = (viewPager.mScroller == null || viewPager.mScroller.isFinished()) ? false : true;
        if (wasScrolling) {
            sx = viewPager.mIsScrollStarted ? viewPager.mScroller.getCurrX() : viewPager.mScroller.getStartX();
            viewPager.mScroller.abortAnimation();
            setScrollingCacheEnabled(false);
        } else {
            sx = getScrollX();
        }
        int sy = getScrollY();
        int dx = x - sx;
        int dy = y - sy;
        if (dx == 0 && dy == 0) {
            completeScroll(false);
            populate();
            setScrollState(0);
            return;
        }
        int duration;
        setScrollingCacheEnabled(true);
        setScrollState(2);
        int width = getClientWidth();
        int halfWidth = width / 2;
        float distanceRatio = Math.min(1.0f, (((float) Math.abs(dx)) * 1.0f) / ((float) width));
        float distance = ((float) halfWidth) + (((float) halfWidth) * distanceInfluenceForSnapDuration(distanceRatio));
        int velocity2 = Math.abs(velocity);
        if (velocity2 > 0) {
            duration = 4 * Math.round(1000.0f * Math.abs(distance / ((float) velocity2)));
        } else {
            duration = (int) ((1.0f + (((float) Math.abs(dx)) / (((float) viewPager.mPageMargin) + (((float) width) * viewPager.mAdapter.getPageWidth(viewPager.mCurItem))))) * 100.0f);
        }
        int duration2 = Math.min(duration, MAX_SETTLE_DURATION);
        viewPager.mIsScrollStarted = false;
        viewPager.mScroller.startScroll(sx, sy, dx, dy, duration2);
        ViewCompat.postInvalidateOnAnimation(this);
    }

    ItemInfo addNewItem(int position, int index) {
        ItemInfo ii = new ItemInfo();
        ii.position = position;
        ii.object = this.mAdapter.instantiateItem((ViewGroup) this, position);
        ii.widthFactor = this.mAdapter.getPageWidth(position);
        if (index >= 0) {
            if (index < this.mItems.size()) {
                this.mItems.add(index, ii);
                return ii;
            }
        }
        this.mItems.add(ii);
        return ii;
    }

    void dataSetChanged() {
        int adapterCount = this.mAdapter.getCount();
        this.mExpectedAdapterCount = adapterCount;
        boolean needPopulate = this.mItems.size() < (this.mOffscreenPageLimit * 2) + 1 && this.mItems.size() < adapterCount;
        boolean isUpdating = false;
        int newCurrItem = this.mCurItem;
        boolean needPopulate2 = needPopulate;
        int i = 0;
        while (i < this.mItems.size()) {
            ItemInfo ii = (ItemInfo) this.mItems.get(i);
            int newPos = this.mAdapter.getItemPosition(ii.object);
            if (newPos != -1) {
                if (newPos == -2) {
                    this.mItems.remove(i);
                    i--;
                    if (!isUpdating) {
                        this.mAdapter.startUpdate((ViewGroup) this);
                        isUpdating = true;
                    }
                    this.mAdapter.destroyItem((ViewGroup) this, ii.position, ii.object);
                    needPopulate2 = true;
                    if (this.mCurItem == ii.position) {
                        newCurrItem = Math.max(0, Math.min(this.mCurItem, adapterCount - 1));
                        needPopulate2 = true;
                    }
                } else if (ii.position != newPos) {
                    if (ii.position == this.mCurItem) {
                        newCurrItem = newPos;
                    }
                    ii.position = newPos;
                    needPopulate2 = true;
                }
            }
            i++;
        }
        if (isUpdating) {
            this.mAdapter.finishUpdate((ViewGroup) this);
        }
        Collections.sort(this.mItems, COMPARATOR);
        if (needPopulate2) {
            i = getChildCount();
            for (int i2 = 0; i2 < i; i2++) {
                LayoutParams lp = (LayoutParams) getChildAt(i2).getLayoutParams();
                if (!lp.isDecor) {
                    lp.widthFactor = 0.0f;
                }
            }
            setCurrentItemInternal(newCurrItem, false, true);
            requestLayout();
        }
    }

    void populate() {
        populate(this.mCurItem);
    }

    void populate(int newCurrentItem) {
        int i = newCurrentItem;
        ItemInfo oldCurInfo = null;
        if (this.mCurItem != i) {
            oldCurInfo = infoForPosition(r1.mCurItem);
            r1.mCurItem = i;
        }
        if (r1.mAdapter == null) {
            sortChildDrawingOrder();
        } else if (r1.mPopulatePending) {
            sortChildDrawingOrder();
        } else if (getWindowToken() != null) {
            r1.mAdapter.startUpdate(r1);
            int pageLimit = r1.mOffscreenPageLimit;
            int startPos = Math.max(0, r1.mCurItem - pageLimit);
            int N = r1.mAdapter.getCount();
            int endPos = Math.min(N - 1, r1.mCurItem + pageLimit);
            if (N != r1.mExpectedAdapterCount) {
                String resName;
                try {
                    resName = getResources().getResourceName(getId());
                } catch (NotFoundException e) {
                    resName = Integer.toHexString(getId());
                }
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("The application's PagerAdapter changed the adapter's contents without calling PagerAdapter#notifyDataSetChanged! Expected adapter item count: ");
                stringBuilder.append(r1.mExpectedAdapterCount);
                stringBuilder.append(", found: ");
                stringBuilder.append(N);
                stringBuilder.append(" Pager id: ");
                stringBuilder.append(resName);
                stringBuilder.append(" Pager class: ");
                stringBuilder.append(getClass());
                stringBuilder.append(" Problematic adapter: ");
                stringBuilder.append(r1.mAdapter.getClass());
                throw new IllegalStateException(stringBuilder.toString());
            }
            ItemInfo ii;
            float extraWidthLeft;
            int itemIndex;
            ItemInfo ii2;
            int clientWidth;
            float leftWidthNeeded;
            int pos;
            float leftWidthNeeded2;
            ItemInfo itemInfo;
            float rightWidthNeeded;
            int ii3;
            int pageLimit2;
            int startPos2;
            View child;
            LayoutParams lp;
            View currentFocused;
            ItemInfo ii4;
            int i2;
            View child2;
            ItemInfo curItem = null;
            int curIndex = 0;
            while (curIndex < r1.mItems.size()) {
                ii = (ItemInfo) r1.mItems.get(curIndex);
                if (ii.position >= r1.mCurItem) {
                    if (ii.position == r1.mCurItem) {
                        curItem = ii;
                    }
                    if (curItem == null && N > 0) {
                        curItem = addNewItem(r1.mCurItem, curIndex);
                    }
                    if (curItem == null) {
                        extraWidthLeft = 0.0f;
                        itemIndex = curIndex - 1;
                        ii2 = itemIndex < 0 ? (ItemInfo) r1.mItems.get(itemIndex) : null;
                        clientWidth = getClientWidth();
                        leftWidthNeeded = clientWidth > 0 ? 0.0f : (((float) getPaddingLeft()) / ((float) clientWidth)) + (2.0f - curItem.widthFactor);
                        pos = r1.mCurItem - 1;
                        while (pos >= 0) {
                            if (extraWidthLeft >= leftWidthNeeded || pos >= startPos) {
                                leftWidthNeeded2 = leftWidthNeeded;
                                if (ii2 == null && pos == ii2.position) {
                                    extraWidthLeft += ii2.widthFactor;
                                    itemIndex--;
                                    leftWidthNeeded = itemIndex >= 0 ? (ItemInfo) r1.mItems.get(itemIndex) : null;
                                } else {
                                    extraWidthLeft += addNewItem(pos, itemIndex + 1).widthFactor;
                                    curIndex++;
                                    leftWidthNeeded = itemIndex < 0 ? (ItemInfo) r1.mItems.get(itemIndex) : null;
                                    ii2 = leftWidthNeeded;
                                    pos--;
                                    leftWidthNeeded = leftWidthNeeded2;
                                }
                            } else if (ii2 == null) {
                                leftWidthNeeded2 = leftWidthNeeded;
                                break;
                            } else if (pos != ii2.position || ii2.scrolling) {
                                leftWidthNeeded2 = leftWidthNeeded;
                                pos--;
                                leftWidthNeeded = leftWidthNeeded2;
                            } else {
                                r1.mItems.remove(itemIndex);
                                leftWidthNeeded2 = leftWidthNeeded;
                                r1.mAdapter.destroyItem(r1, pos, ii2.object);
                                itemIndex--;
                                curIndex--;
                                leftWidthNeeded = itemIndex >= 0 ? (ItemInfo) r1.mItems.get(itemIndex) : null;
                            }
                            ii2 = leftWidthNeeded;
                            pos--;
                            leftWidthNeeded = leftWidthNeeded2;
                        }
                        leftWidthNeeded = curItem.widthFactor;
                        pos = curIndex + 1;
                        if (leftWidthNeeded >= 2.0f) {
                            itemInfo = pos >= r1.mItems.size() ? (ItemInfo) r1.mItems.get(pos) : null;
                            rightWidthNeeded = clientWidth > 0 ? 0.0f : (((float) getPaddingRight()) / ((float) clientWidth)) + 2.0f;
                            ii3 = r1.mCurItem + 1;
                            while (ii3 < N) {
                                if (leftWidthNeeded >= rightWidthNeeded || ii3 <= endPos) {
                                    pageLimit2 = pageLimit;
                                    startPos2 = startPos;
                                    if (itemInfo == null && ii3 == itemInfo.position) {
                                        leftWidthNeeded += itemInfo.widthFactor;
                                        pos++;
                                        itemInfo = pos < r1.mItems.size() ? (ItemInfo) r1.mItems.get(pos) : 0;
                                    } else {
                                        pageLimit = addNewItem(ii3, pos);
                                        pos++;
                                        leftWidthNeeded += pageLimit.widthFactor;
                                        itemInfo = pos >= r1.mItems.size() ? (ItemInfo) r1.mItems.get(pos) : null;
                                    }
                                } else if (itemInfo == null) {
                                    pageLimit2 = pageLimit;
                                    startPos2 = startPos;
                                    break;
                                } else {
                                    pageLimit2 = pageLimit;
                                    if (ii3 == itemInfo.position && itemInfo.scrolling == 0) {
                                        r1.mItems.remove(pos);
                                        startPos2 = startPos;
                                        r1.mAdapter.destroyItem(r1, ii3, itemInfo.object);
                                        itemInfo = pos < r1.mItems.size() ? (ItemInfo) r1.mItems.get(pos) : 0;
                                    } else {
                                        startPos2 = startPos;
                                    }
                                }
                                ii3++;
                                pageLimit = pageLimit2;
                                startPos = startPos2;
                            }
                            startPos2 = startPos;
                        } else {
                            pageLimit2 = pageLimit;
                            startPos2 = startPos;
                            itemInfo = ii2;
                        }
                        calculatePageOffsets(curItem, curIndex, oldCurInfo);
                        r1.mAdapter.setPrimaryItem(r1, r1.mCurItem, curItem.object);
                    } else {
                        startPos2 = startPos;
                    }
                    r1.mAdapter.finishUpdate(r1);
                    i = getChildCount();
                    for (pageLimit = 0; pageLimit < i; pageLimit++) {
                        child = getChildAt(pageLimit);
                        lp = (LayoutParams) child.getLayoutParams();
                        lp.childIndex = pageLimit;
                        if (!lp.isDecor) {
                            if (lp.widthFactor == 0.0f) {
                                ii = infoForChild(child);
                                if (ii != null) {
                                    lp.widthFactor = ii.widthFactor;
                                    lp.position = ii.position;
                                }
                            }
                        }
                    }
                    sortChildDrawingOrder();
                    if (hasFocus()) {
                        currentFocused = findFocus();
                        ii4 = currentFocused == null ? infoForAnyChild(currentFocused) : null;
                        if (ii4 == null || ii4.position != r1.mCurItem) {
                            i2 = 0;
                            while (true) {
                                clientWidth = i2;
                                if (clientWidth < getChildCount()) {
                                    break;
                                }
                                child2 = getChildAt(clientWidth);
                                ii4 = infoForChild(child2);
                                if (ii4 == null && ii4.position == r1.mCurItem && child2.requestFocus(2)) {
                                    break;
                                }
                                i2 = clientWidth + 1;
                            }
                        }
                    }
                }
                curIndex++;
            }
            curItem = addNewItem(r1.mCurItem, curIndex);
            if (curItem == null) {
                startPos2 = startPos;
            } else {
                extraWidthLeft = 0.0f;
                itemIndex = curIndex - 1;
                if (itemIndex < 0) {
                }
                clientWidth = getClientWidth();
                if (clientWidth > 0) {
                }
                leftWidthNeeded = clientWidth > 0 ? 0.0f : (((float) getPaddingLeft()) / ((float) clientWidth)) + (2.0f - curItem.widthFactor);
                pos = r1.mCurItem - 1;
                while (pos >= 0) {
                    if (extraWidthLeft >= leftWidthNeeded) {
                    }
                    leftWidthNeeded2 = leftWidthNeeded;
                    if (ii2 == null) {
                    }
                    extraWidthLeft += addNewItem(pos, itemIndex + 1).widthFactor;
                    curIndex++;
                    if (itemIndex < 0) {
                    }
                    leftWidthNeeded = itemIndex < 0 ? (ItemInfo) r1.mItems.get(itemIndex) : null;
                    ii2 = leftWidthNeeded;
                    pos--;
                    leftWidthNeeded = leftWidthNeeded2;
                }
                leftWidthNeeded = curItem.widthFactor;
                pos = curIndex + 1;
                if (leftWidthNeeded >= 2.0f) {
                    pageLimit2 = pageLimit;
                    startPos2 = startPos;
                    itemInfo = ii2;
                } else {
                    if (pos >= r1.mItems.size()) {
                    }
                    if (clientWidth > 0) {
                    }
                    ii3 = r1.mCurItem + 1;
                    while (ii3 < N) {
                        if (leftWidthNeeded >= rightWidthNeeded) {
                        }
                        pageLimit2 = pageLimit;
                        startPos2 = startPos;
                        if (itemInfo == null) {
                        }
                        pageLimit = addNewItem(ii3, pos);
                        pos++;
                        leftWidthNeeded += pageLimit.widthFactor;
                        if (pos >= r1.mItems.size()) {
                        }
                        ii3++;
                        pageLimit = pageLimit2;
                        startPos = startPos2;
                    }
                    startPos2 = startPos;
                }
                calculatePageOffsets(curItem, curIndex, oldCurInfo);
                r1.mAdapter.setPrimaryItem(r1, r1.mCurItem, curItem.object);
            }
            r1.mAdapter.finishUpdate(r1);
            i = getChildCount();
            for (pageLimit = 0; pageLimit < i; pageLimit++) {
                child = getChildAt(pageLimit);
                lp = (LayoutParams) child.getLayoutParams();
                lp.childIndex = pageLimit;
                if (!lp.isDecor) {
                    if (lp.widthFactor == 0.0f) {
                        ii = infoForChild(child);
                        if (ii != null) {
                            lp.widthFactor = ii.widthFactor;
                            lp.position = ii.position;
                        }
                    }
                }
            }
            sortChildDrawingOrder();
            if (hasFocus()) {
                currentFocused = findFocus();
                if (currentFocused == null) {
                }
                ii4 = currentFocused == null ? infoForAnyChild(currentFocused) : null;
                i2 = 0;
                while (true) {
                    clientWidth = i2;
                    if (clientWidth < getChildCount()) {
                        break;
                    }
                    child2 = getChildAt(clientWidth);
                    ii4 = infoForChild(child2);
                    if (ii4 == null) {
                    }
                    i2 = clientWidth + 1;
                }
            }
        }
    }

    private void sortChildDrawingOrder() {
        if (this.mDrawingOrder != 0) {
            if (this.mDrawingOrderedChildren == null) {
                this.mDrawingOrderedChildren = new ArrayList();
            } else {
                this.mDrawingOrderedChildren.clear();
            }
            int childCount = getChildCount();
            for (int i = 0; i < childCount; i++) {
                this.mDrawingOrderedChildren.add(getChildAt(i));
            }
            Collections.sort(this.mDrawingOrderedChildren, sPositionComparator);
        }
    }

    private void calculatePageOffsets(ItemInfo curItem, int curIndex, ItemInfo oldCurInfo) {
        int oldCurPosition;
        int itemIndex;
        float offset;
        int N = this.mAdapter.getCount();
        int width = getClientWidth();
        float marginOffset = width > 0 ? ((float) this.mPageMargin) / ((float) width) : 0.0f;
        if (oldCurInfo != null) {
            oldCurPosition = oldCurInfo.position;
            int pos;
            ItemInfo ii;
            if (oldCurPosition < curItem.position) {
                itemIndex = 0;
                offset = (oldCurInfo.offset + oldCurInfo.widthFactor) + marginOffset;
                pos = oldCurPosition + 1;
                while (pos <= curItem.position && itemIndex < this.mItems.size()) {
                    ii = (ItemInfo) this.mItems.get(itemIndex);
                    while (pos > ii.position && itemIndex < this.mItems.size() - 1) {
                        itemIndex++;
                        ii = (ItemInfo) this.mItems.get(itemIndex);
                    }
                    while (pos < ii.position) {
                        offset += this.mAdapter.getPageWidth(pos) + marginOffset;
                        pos++;
                    }
                    ii.offset = offset;
                    offset += ii.widthFactor + marginOffset;
                    pos++;
                }
            } else if (oldCurPosition > curItem.position) {
                itemIndex = this.mItems.size() - 1;
                offset = oldCurInfo.offset;
                pos = oldCurPosition - 1;
                while (pos >= curItem.position && itemIndex >= 0) {
                    ii = (ItemInfo) this.mItems.get(itemIndex);
                    while (pos < ii.position && itemIndex > 0) {
                        itemIndex--;
                        ii = (ItemInfo) this.mItems.get(itemIndex);
                    }
                    while (pos > ii.position) {
                        offset -= this.mAdapter.getPageWidth(pos) + marginOffset;
                        pos--;
                    }
                    offset -= ii.widthFactor + marginOffset;
                    ii.offset = offset;
                    pos--;
                }
            }
        }
        oldCurPosition = this.mItems.size();
        float offset2 = curItem.offset;
        int pos2 = curItem.position - 1;
        this.mFirstOffset = curItem.position == 0 ? curItem.offset : -3.4028235E38f;
        this.mLastOffset = curItem.position == N + -1 ? (curItem.offset + curItem.widthFactor) - 1.0f : Float.MAX_VALUE;
        int i = curIndex - 1;
        while (i >= 0) {
            ItemInfo ii2 = (ItemInfo) this.mItems.get(i);
            while (pos2 > ii2.position) {
                offset2 -= this.mAdapter.getPageWidth(pos2) + marginOffset;
                pos2--;
            }
            offset2 -= ii2.widthFactor + marginOffset;
            ii2.offset = offset2;
            if (ii2.position == 0) {
                this.mFirstOffset = offset2;
            }
            i--;
            pos2--;
        }
        offset = (curItem.offset + curItem.widthFactor) + marginOffset;
        itemIndex = curItem.position + 1;
        pos2 = curIndex + 1;
        while (pos2 < oldCurPosition) {
            ii2 = (ItemInfo) this.mItems.get(pos2);
            while (itemIndex < ii2.position) {
                offset += this.mAdapter.getPageWidth(itemIndex) + marginOffset;
                itemIndex++;
            }
            if (ii2.position == N - 1) {
                this.mLastOffset = (ii2.widthFactor + offset) - 1.0f;
            }
            ii2.offset = offset;
            offset += ii2.widthFactor + marginOffset;
            pos2++;
            itemIndex++;
        }
        this.mNeedCalculatePageOffsets = false;
    }

    public Parcelable onSaveInstanceState() {
        SavedState ss = new SavedState(super.onSaveInstanceState());
        ss.position = this.mCurItem;
        if (this.mAdapter != null) {
            ss.adapterState = this.mAdapter.saveState();
        }
        return ss;
    }

    public void onRestoreInstanceState(Parcelable state) {
        if (state instanceof SavedState) {
            SavedState ss = (SavedState) state;
            super.onRestoreInstanceState(ss.getSuperState());
            if (this.mAdapter != null) {
                this.mAdapter.restoreState(ss.adapterState, ss.loader);
                setCurrentItemInternal(ss.position, false, true);
            } else {
                this.mRestoredCurItem = ss.position;
                this.mRestoredAdapterState = ss.adapterState;
                this.mRestoredClassLoader = ss.loader;
            }
            return;
        }
        super.onRestoreInstanceState(state);
    }

    public void addView(View child, int index, android.view.ViewGroup.LayoutParams params) {
        if (!checkLayoutParams(params)) {
            params = generateLayoutParams(params);
        }
        LayoutParams lp = (LayoutParams) params;
        lp.isDecor |= isDecorView(child);
        if (!this.mInLayout) {
            super.addView(child, index, params);
        } else if (lp == null || !lp.isDecor) {
            lp.needsMeasure = true;
            addViewInLayout(child, index, params);
        } else {
            throw new IllegalStateException("Cannot add pager decor view during layout");
        }
    }

    private static boolean isDecorView(View view) {
        return view.getClass().getAnnotation(DecorView.class) != null;
    }

    public void removeView(View view) {
        if (this.mInLayout) {
            removeViewInLayout(view);
        } else {
            super.removeView(view);
        }
    }

    ItemInfo infoForChild(View child) {
        for (int i = 0; i < this.mItems.size(); i++) {
            ItemInfo ii = (ItemInfo) this.mItems.get(i);
            if (this.mAdapter.isViewFromObject(child, ii.object)) {
                return ii;
            }
        }
        return null;
    }

    ItemInfo infoForAnyChild(View child) {
        while (true) {
            Object parent = child.getParent();
            ViewParent parent2 = parent;
            if (parent == this) {
                return infoForChild(child);
            }
            if (parent2 == null) {
                break;
            } else if (!(parent2 instanceof View)) {
                break;
            } else {
                child = (View) parent2;
            }
        }
        return null;
    }

    ItemInfo infoForPosition(int position) {
        for (int i = 0; i < this.mItems.size(); i++) {
            ItemInfo ii = (ItemInfo) this.mItems.get(i);
            if (ii.position == position) {
                return ii;
            }
        }
        return null;
    }

    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        this.mFirstLayout = true;
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthSize;
        int maxGutterSize;
        setMeasuredDimension(getDefaultSize(0, widthMeasureSpec), getDefaultSize(0, heightMeasureSpec));
        int measuredWidth = getMeasuredWidth();
        int maxGutterSize2 = measuredWidth / 10;
        this.mGutterSize = Math.min(maxGutterSize2, this.mDefaultGutterSize);
        int childWidthSize = (measuredWidth - getPaddingLeft()) - getPaddingRight();
        int childHeightSize = (getMeasuredHeight() - getPaddingTop()) - getPaddingBottom();
        int size = getChildCount();
        int childHeightSize2 = childHeightSize;
        childHeightSize = childWidthSize;
        childWidthSize = 0;
        while (childWidthSize < size) {
            int measuredWidth2;
            int heightMode;
            View child = getChildAt(childWidthSize);
            if (child.getVisibility() != 8) {
                LayoutParams lp = (LayoutParams) child.getLayoutParams();
                if (lp != null && lp.isDecor) {
                    boolean consumeVertical;
                    boolean z;
                    boolean consumeHorizontal;
                    int widthSize2;
                    int heightSize;
                    int hgrav = lp.gravity & 7;
                    int vgrav = lp.gravity & 112;
                    int widthMode = Integer.MIN_VALUE;
                    int heightMode2 = Integer.MIN_VALUE;
                    if (vgrav != 48) {
                        if (vgrav != 80) {
                            consumeVertical = false;
                            if (hgrav != 3) {
                                if (hgrav == 5) {
                                    z = false;
                                    consumeHorizontal = z;
                                    if (!consumeVertical) {
                                        widthMode = 1073741824;
                                    } else if (consumeHorizontal) {
                                        heightMode2 = 1073741824;
                                    }
                                    widthSize2 = childHeightSize;
                                    heightSize = childHeightSize2;
                                    measuredWidth2 = measuredWidth;
                                    if (lp.width != -2) {
                                        widthMode = 1073741824;
                                        if (lp.width != -1) {
                                            widthSize = lp.width;
                                            if (lp.height != -2) {
                                                heightMode2 = 1073741824;
                                                if (lp.height != -1) {
                                                    measuredWidth = lp.height;
                                                    heightMode = 1073741824;
                                                    maxGutterSize = maxGutterSize2;
                                                    child.measure(MeasureSpec.makeMeasureSpec(widthSize, widthMode), MeasureSpec.makeMeasureSpec(measuredWidth, heightMode));
                                                    if (!consumeVertical) {
                                                        childHeightSize2 -= child.getMeasuredHeight();
                                                    } else if (!consumeHorizontal) {
                                                        childHeightSize -= child.getMeasuredWidth();
                                                    }
                                                    childWidthSize++;
                                                    measuredWidth = measuredWidth2;
                                                    maxGutterSize2 = maxGutterSize;
                                                    widthSize = widthMeasureSpec;
                                                    heightMode = heightMeasureSpec;
                                                }
                                            }
                                            heightMode = heightMode2;
                                            measuredWidth = heightSize;
                                            maxGutterSize = maxGutterSize2;
                                            child.measure(MeasureSpec.makeMeasureSpec(widthSize, widthMode), MeasureSpec.makeMeasureSpec(measuredWidth, heightMode));
                                            if (!consumeVertical) {
                                                childHeightSize2 -= child.getMeasuredHeight();
                                            } else if (!consumeHorizontal) {
                                                childHeightSize -= child.getMeasuredWidth();
                                            }
                                            childWidthSize++;
                                            measuredWidth = measuredWidth2;
                                            maxGutterSize2 = maxGutterSize;
                                            widthSize = widthMeasureSpec;
                                            heightMode = heightMeasureSpec;
                                        }
                                    }
                                    widthSize = widthSize2;
                                    if (lp.height != -2) {
                                        heightMode2 = 1073741824;
                                        if (lp.height != -1) {
                                            measuredWidth = lp.height;
                                            heightMode = 1073741824;
                                            maxGutterSize = maxGutterSize2;
                                            child.measure(MeasureSpec.makeMeasureSpec(widthSize, widthMode), MeasureSpec.makeMeasureSpec(measuredWidth, heightMode));
                                            if (!consumeVertical) {
                                                childHeightSize2 -= child.getMeasuredHeight();
                                            } else if (!consumeHorizontal) {
                                                childHeightSize -= child.getMeasuredWidth();
                                            }
                                            childWidthSize++;
                                            measuredWidth = measuredWidth2;
                                            maxGutterSize2 = maxGutterSize;
                                            widthSize = widthMeasureSpec;
                                            heightMode = heightMeasureSpec;
                                        }
                                    }
                                    heightMode = heightMode2;
                                    measuredWidth = heightSize;
                                    maxGutterSize = maxGutterSize2;
                                    child.measure(MeasureSpec.makeMeasureSpec(widthSize, widthMode), MeasureSpec.makeMeasureSpec(measuredWidth, heightMode));
                                    if (!consumeVertical) {
                                        childHeightSize2 -= child.getMeasuredHeight();
                                    } else if (!consumeHorizontal) {
                                        childHeightSize -= child.getMeasuredWidth();
                                    }
                                    childWidthSize++;
                                    measuredWidth = measuredWidth2;
                                    maxGutterSize2 = maxGutterSize;
                                    widthSize = widthMeasureSpec;
                                    heightMode = heightMeasureSpec;
                                }
                            }
                            z = true;
                            consumeHorizontal = z;
                            if (!consumeVertical) {
                                widthMode = 1073741824;
                            } else if (consumeHorizontal) {
                                heightMode2 = 1073741824;
                            }
                            widthSize2 = childHeightSize;
                            heightSize = childHeightSize2;
                            measuredWidth2 = measuredWidth;
                            if (lp.width != -2) {
                                widthMode = 1073741824;
                                if (lp.width != -1) {
                                    widthSize = lp.width;
                                    if (lp.height != -2) {
                                        heightMode2 = 1073741824;
                                        if (lp.height != -1) {
                                            measuredWidth = lp.height;
                                            heightMode = 1073741824;
                                            maxGutterSize = maxGutterSize2;
                                            child.measure(MeasureSpec.makeMeasureSpec(widthSize, widthMode), MeasureSpec.makeMeasureSpec(measuredWidth, heightMode));
                                            if (!consumeVertical) {
                                                childHeightSize2 -= child.getMeasuredHeight();
                                            } else if (!consumeHorizontal) {
                                                childHeightSize -= child.getMeasuredWidth();
                                            }
                                            childWidthSize++;
                                            measuredWidth = measuredWidth2;
                                            maxGutterSize2 = maxGutterSize;
                                            widthSize = widthMeasureSpec;
                                            heightMode = heightMeasureSpec;
                                        }
                                    }
                                    heightMode = heightMode2;
                                    measuredWidth = heightSize;
                                    maxGutterSize = maxGutterSize2;
                                    child.measure(MeasureSpec.makeMeasureSpec(widthSize, widthMode), MeasureSpec.makeMeasureSpec(measuredWidth, heightMode));
                                    if (!consumeVertical) {
                                        childHeightSize2 -= child.getMeasuredHeight();
                                    } else if (!consumeHorizontal) {
                                        childHeightSize -= child.getMeasuredWidth();
                                    }
                                    childWidthSize++;
                                    measuredWidth = measuredWidth2;
                                    maxGutterSize2 = maxGutterSize;
                                    widthSize = widthMeasureSpec;
                                    heightMode = heightMeasureSpec;
                                }
                            }
                            widthSize = widthSize2;
                            if (lp.height != -2) {
                                heightMode2 = 1073741824;
                                if (lp.height != -1) {
                                    measuredWidth = lp.height;
                                    heightMode = 1073741824;
                                    maxGutterSize = maxGutterSize2;
                                    child.measure(MeasureSpec.makeMeasureSpec(widthSize, widthMode), MeasureSpec.makeMeasureSpec(measuredWidth, heightMode));
                                    if (!consumeVertical) {
                                        childHeightSize2 -= child.getMeasuredHeight();
                                    } else if (!consumeHorizontal) {
                                        childHeightSize -= child.getMeasuredWidth();
                                    }
                                    childWidthSize++;
                                    measuredWidth = measuredWidth2;
                                    maxGutterSize2 = maxGutterSize;
                                    widthSize = widthMeasureSpec;
                                    heightMode = heightMeasureSpec;
                                }
                            }
                            heightMode = heightMode2;
                            measuredWidth = heightSize;
                            maxGutterSize = maxGutterSize2;
                            child.measure(MeasureSpec.makeMeasureSpec(widthSize, widthMode), MeasureSpec.makeMeasureSpec(measuredWidth, heightMode));
                            if (!consumeVertical) {
                                childHeightSize2 -= child.getMeasuredHeight();
                            } else if (!consumeHorizontal) {
                                childHeightSize -= child.getMeasuredWidth();
                            }
                            childWidthSize++;
                            measuredWidth = measuredWidth2;
                            maxGutterSize2 = maxGutterSize;
                            widthSize = widthMeasureSpec;
                            heightMode = heightMeasureSpec;
                        }
                    }
                    consumeVertical = true;
                    if (hgrav != 3) {
                        if (hgrav == 5) {
                            z = false;
                            consumeHorizontal = z;
                            if (!consumeVertical) {
                                widthMode = 1073741824;
                            } else if (consumeHorizontal) {
                                heightMode2 = 1073741824;
                            }
                            widthSize2 = childHeightSize;
                            heightSize = childHeightSize2;
                            measuredWidth2 = measuredWidth;
                            if (lp.width != -2) {
                                widthMode = 1073741824;
                                if (lp.width != -1) {
                                    widthSize = lp.width;
                                    if (lp.height != -2) {
                                        heightMode2 = 1073741824;
                                        if (lp.height != -1) {
                                            measuredWidth = lp.height;
                                            heightMode = 1073741824;
                                            maxGutterSize = maxGutterSize2;
                                            child.measure(MeasureSpec.makeMeasureSpec(widthSize, widthMode), MeasureSpec.makeMeasureSpec(measuredWidth, heightMode));
                                            if (!consumeVertical) {
                                                childHeightSize2 -= child.getMeasuredHeight();
                                            } else if (!consumeHorizontal) {
                                                childHeightSize -= child.getMeasuredWidth();
                                            }
                                            childWidthSize++;
                                            measuredWidth = measuredWidth2;
                                            maxGutterSize2 = maxGutterSize;
                                            widthSize = widthMeasureSpec;
                                            heightMode = heightMeasureSpec;
                                        }
                                    }
                                    heightMode = heightMode2;
                                    measuredWidth = heightSize;
                                    maxGutterSize = maxGutterSize2;
                                    child.measure(MeasureSpec.makeMeasureSpec(widthSize, widthMode), MeasureSpec.makeMeasureSpec(measuredWidth, heightMode));
                                    if (!consumeVertical) {
                                        childHeightSize2 -= child.getMeasuredHeight();
                                    } else if (!consumeHorizontal) {
                                        childHeightSize -= child.getMeasuredWidth();
                                    }
                                    childWidthSize++;
                                    measuredWidth = measuredWidth2;
                                    maxGutterSize2 = maxGutterSize;
                                    widthSize = widthMeasureSpec;
                                    heightMode = heightMeasureSpec;
                                }
                            }
                            widthSize = widthSize2;
                            if (lp.height != -2) {
                                heightMode2 = 1073741824;
                                if (lp.height != -1) {
                                    measuredWidth = lp.height;
                                    heightMode = 1073741824;
                                    maxGutterSize = maxGutterSize2;
                                    child.measure(MeasureSpec.makeMeasureSpec(widthSize, widthMode), MeasureSpec.makeMeasureSpec(measuredWidth, heightMode));
                                    if (!consumeVertical) {
                                        childHeightSize2 -= child.getMeasuredHeight();
                                    } else if (!consumeHorizontal) {
                                        childHeightSize -= child.getMeasuredWidth();
                                    }
                                    childWidthSize++;
                                    measuredWidth = measuredWidth2;
                                    maxGutterSize2 = maxGutterSize;
                                    widthSize = widthMeasureSpec;
                                    heightMode = heightMeasureSpec;
                                }
                            }
                            heightMode = heightMode2;
                            measuredWidth = heightSize;
                            maxGutterSize = maxGutterSize2;
                            child.measure(MeasureSpec.makeMeasureSpec(widthSize, widthMode), MeasureSpec.makeMeasureSpec(measuredWidth, heightMode));
                            if (!consumeVertical) {
                                childHeightSize2 -= child.getMeasuredHeight();
                            } else if (!consumeHorizontal) {
                                childHeightSize -= child.getMeasuredWidth();
                            }
                            childWidthSize++;
                            measuredWidth = measuredWidth2;
                            maxGutterSize2 = maxGutterSize;
                            widthSize = widthMeasureSpec;
                            heightMode = heightMeasureSpec;
                        }
                    }
                    z = true;
                    consumeHorizontal = z;
                    if (!consumeVertical) {
                        widthMode = 1073741824;
                    } else if (consumeHorizontal) {
                        heightMode2 = 1073741824;
                    }
                    widthSize2 = childHeightSize;
                    heightSize = childHeightSize2;
                    measuredWidth2 = measuredWidth;
                    if (lp.width != -2) {
                        widthMode = 1073741824;
                        if (lp.width != -1) {
                            widthSize = lp.width;
                            if (lp.height != -2) {
                                heightMode2 = 1073741824;
                                if (lp.height != -1) {
                                    measuredWidth = lp.height;
                                    heightMode = 1073741824;
                                    maxGutterSize = maxGutterSize2;
                                    child.measure(MeasureSpec.makeMeasureSpec(widthSize, widthMode), MeasureSpec.makeMeasureSpec(measuredWidth, heightMode));
                                    if (!consumeVertical) {
                                        childHeightSize2 -= child.getMeasuredHeight();
                                    } else if (!consumeHorizontal) {
                                        childHeightSize -= child.getMeasuredWidth();
                                    }
                                    childWidthSize++;
                                    measuredWidth = measuredWidth2;
                                    maxGutterSize2 = maxGutterSize;
                                    widthSize = widthMeasureSpec;
                                    heightMode = heightMeasureSpec;
                                }
                            }
                            heightMode = heightMode2;
                            measuredWidth = heightSize;
                            maxGutterSize = maxGutterSize2;
                            child.measure(MeasureSpec.makeMeasureSpec(widthSize, widthMode), MeasureSpec.makeMeasureSpec(measuredWidth, heightMode));
                            if (!consumeVertical) {
                                childHeightSize2 -= child.getMeasuredHeight();
                            } else if (!consumeHorizontal) {
                                childHeightSize -= child.getMeasuredWidth();
                            }
                            childWidthSize++;
                            measuredWidth = measuredWidth2;
                            maxGutterSize2 = maxGutterSize;
                            widthSize = widthMeasureSpec;
                            heightMode = heightMeasureSpec;
                        }
                    }
                    widthSize = widthSize2;
                    if (lp.height != -2) {
                        heightMode2 = 1073741824;
                        if (lp.height != -1) {
                            measuredWidth = lp.height;
                            heightMode = 1073741824;
                            maxGutterSize = maxGutterSize2;
                            child.measure(MeasureSpec.makeMeasureSpec(widthSize, widthMode), MeasureSpec.makeMeasureSpec(measuredWidth, heightMode));
                            if (!consumeVertical) {
                                childHeightSize2 -= child.getMeasuredHeight();
                            } else if (!consumeHorizontal) {
                                childHeightSize -= child.getMeasuredWidth();
                            }
                            childWidthSize++;
                            measuredWidth = measuredWidth2;
                            maxGutterSize2 = maxGutterSize;
                            widthSize = widthMeasureSpec;
                            heightMode = heightMeasureSpec;
                        }
                    }
                    heightMode = heightMode2;
                    measuredWidth = heightSize;
                    maxGutterSize = maxGutterSize2;
                    child.measure(MeasureSpec.makeMeasureSpec(widthSize, widthMode), MeasureSpec.makeMeasureSpec(measuredWidth, heightMode));
                    if (!consumeVertical) {
                        childHeightSize2 -= child.getMeasuredHeight();
                    } else if (!consumeHorizontal) {
                        childHeightSize -= child.getMeasuredWidth();
                    }
                    childWidthSize++;
                    measuredWidth = measuredWidth2;
                    maxGutterSize2 = maxGutterSize;
                    widthSize = widthMeasureSpec;
                    heightMode = heightMeasureSpec;
                }
            }
            measuredWidth2 = measuredWidth;
            maxGutterSize = maxGutterSize2;
            childWidthSize++;
            measuredWidth = measuredWidth2;
            maxGutterSize2 = maxGutterSize;
            widthSize = widthMeasureSpec;
            heightMode = heightMeasureSpec;
        }
        maxGutterSize = maxGutterSize2;
        r0.mChildWidthMeasureSpec = MeasureSpec.makeMeasureSpec(childHeightSize, 1073741824);
        r0.mChildHeightMeasureSpec = MeasureSpec.makeMeasureSpec(childHeightSize2, 1073741824);
        r0.mInLayout = true;
        populate();
        widthSize = 0;
        r0.mInLayout = false;
        measuredWidth = getChildCount();
        while (widthSize < measuredWidth) {
            View child2 = getChildAt(widthSize);
            if (child2.getVisibility() != 8) {
                LayoutParams lp2 = (LayoutParams) child2.getLayoutParams();
                if (lp2 == null || !lp2.isDecor) {
                    child2.measure(MeasureSpec.makeMeasureSpec((int) (((float) childHeightSize) * lp2.widthFactor), 1073741824), r0.mChildHeightMeasureSpec);
                }
            }
            widthSize++;
        }
    }

    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (w != oldw) {
            recomputeScrollPosition(w, oldw, this.mPageMargin, this.mPageMargin);
        }
    }

    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int hgrav;
        int childLeft;
        int i;
        int i2;
        boolean z;
        ViewPager viewPager = this;
        int count = getChildCount();
        int width = r - l;
        int height = b - t;
        int paddingLeft = getPaddingLeft();
        int paddingTop = getPaddingTop();
        int paddingRight = getPaddingRight();
        int paddingBottom = getPaddingBottom();
        int scrollX = getScrollX();
        int decorCount = 0;
        int paddingBottom2 = paddingBottom;
        paddingBottom = paddingTop;
        paddingTop = paddingLeft;
        for (paddingLeft = 0; paddingLeft < count; paddingLeft++) {
            View child = getChildAt(paddingLeft);
            if (child.getVisibility() != 8) {
                LayoutParams lp = (LayoutParams) child.getLayoutParams();
                int childLeft2 = 0;
                if (lp.isDecor != 0) {
                    hgrav = lp.gravity & 7;
                    int vgrav = lp.gravity & 112;
                    if (hgrav == 1) {
                        childLeft = Math.max((width - child.getMeasuredWidth()) / 2, paddingTop);
                    } else if (hgrav == 3) {
                        childLeft = paddingTop;
                        paddingTop += child.getMeasuredWidth();
                    } else if (hgrav != 5) {
                        childLeft = paddingTop;
                    } else {
                        childLeft = (width - paddingRight) - child.getMeasuredWidth();
                        paddingRight += child.getMeasuredWidth();
                    }
                    if (vgrav == 16) {
                        hgrav = Math.max((height - child.getMeasuredHeight()) / 2, paddingBottom);
                    } else if (vgrav == 48) {
                        hgrav = paddingBottom;
                        paddingBottom += child.getMeasuredHeight();
                    } else if (vgrav != 80) {
                        hgrav = paddingBottom;
                    } else {
                        hgrav = (height - paddingBottom2) - child.getMeasuredHeight();
                        paddingBottom2 += child.getMeasuredHeight();
                    }
                    childLeft += scrollX;
                    child.layout(childLeft, hgrav, childLeft + child.getMeasuredWidth(), hgrav + child.getMeasuredHeight());
                    decorCount++;
                }
            }
        }
        childLeft = (width - paddingTop) - paddingRight;
        hgrav = 0;
        while (hgrav < count) {
            int count2;
            View child2 = getChildAt(hgrav);
            if (child2.getVisibility() != 8) {
                LayoutParams lp2 = (LayoutParams) child2.getLayoutParams();
                if (!lp2.isDecor) {
                    ItemInfo infoForChild = infoForChild(child2);
                    ItemInfo ii = infoForChild;
                    if (infoForChild != null) {
                        count2 = count;
                        count = (int) (((float) childLeft) * ii.offset);
                        int childLeft3 = paddingTop + count;
                        int childTop = paddingBottom;
                        int loff = count;
                        if (lp2.needsMeasure != 0) {
                            lp2.needsMeasure = false;
                            i = childLeft;
                            i2 = width;
                            child2.measure(MeasureSpec.makeMeasureSpec((int) (((float) childLeft) * lp2.widthFactor), 1073741824), MeasureSpec.makeMeasureSpec((height - paddingBottom) - paddingBottom2, 1073741824));
                        } else {
                            i = childLeft;
                            i2 = width;
                        }
                        width = childTop;
                        child2.layout(childLeft3, width, child2.getMeasuredWidth() + childLeft3, child2.getMeasuredHeight() + width);
                        hgrav++;
                        count = count2;
                        childLeft = i;
                        width = i2;
                    }
                }
            }
            count2 = count;
            i = childLeft;
            i2 = width;
            hgrav++;
            count = count2;
            childLeft = i;
            width = i2;
        }
        i = childLeft;
        i2 = width;
        viewPager.mTopPageBounds = paddingBottom;
        viewPager.mBottomPageBounds = height - paddingBottom2;
        viewPager.mDecorChildCount = decorCount;
        if (viewPager.mFirstLayout) {
            z = false;
            scrollToItem(viewPager.mCurItem, false, 0, false);
        } else {
            z = false;
        }
        viewPager.mFirstLayout = z;
    }

    public void computeScroll() {
        this.mIsScrollStarted = true;
        if (this.mScroller.isFinished() || !this.mScroller.computeScrollOffset()) {
            completeScroll(true);
            return;
        }
        int oldX = getScrollX();
        int oldY = getScrollY();
        int x = this.mScroller.getCurrX();
        int y = this.mScroller.getCurrY();
        if (!(oldX == x && oldY == y)) {
            scrollTo(x, y);
            if (!pageScrolled(x)) {
                this.mScroller.abortAnimation();
                scrollTo(0, y);
            }
        }
        ViewCompat.postInvalidateOnAnimation(this);
    }

    private boolean pageScrolled(int xpos) {
        if (this.mItems.size() != 0) {
            ItemInfo ii = infoForCurrentScrollPosition();
            int width = getClientWidth();
            int widthWithMargin = this.mPageMargin + width;
            float marginOffset = ((float) this.mPageMargin) / ((float) width);
            int currentPage = ii.position;
            float pageOffset = ((((float) xpos) / ((float) width)) - ii.offset) / (ii.widthFactor + marginOffset);
            int offsetPixels = (int) (((float) widthWithMargin) * pageOffset);
            this.mCalledSuper = false;
            onPageScrolled(currentPage, pageOffset, offsetPixels);
            if (this.mCalledSuper) {
                return true;
            }
            throw new IllegalStateException("onPageScrolled did not call superclass implementation");
        } else if (this.mFirstLayout) {
            return false;
        } else {
            this.mCalledSuper = false;
            onPageScrolled(0, 0.0f, 0);
            if (this.mCalledSuper) {
                return false;
            }
            throw new IllegalStateException("onPageScrolled did not call superclass implementation");
        }
    }

    protected void onPageScrolled(int position, float offset, int offsetPixels) {
        int scrollX;
        int paddingLeft;
        int i = 0;
        if (this.mDecorChildCount > 0) {
            scrollX = getScrollX();
            paddingLeft = getPaddingLeft();
            int paddingRight = getPaddingRight();
            int width = getWidth();
            int childCount = getChildCount();
            int paddingRight2 = paddingRight;
            paddingRight = paddingLeft;
            for (paddingLeft = 0; paddingLeft < childCount; paddingLeft++) {
                View child = getChildAt(paddingLeft);
                LayoutParams lp = (LayoutParams) child.getLayoutParams();
                if (lp.isDecor) {
                    int childLeft;
                    int hgrav = lp.gravity & 7;
                    if (hgrav == 1) {
                        childLeft = Math.max((width - child.getMeasuredWidth()) / 2, paddingRight);
                    } else if (hgrav == 3) {
                        childLeft = paddingRight;
                        paddingRight += child.getWidth();
                    } else if (hgrav != 5) {
                        childLeft = paddingRight;
                    } else {
                        childLeft = (width - paddingRight2) - child.getMeasuredWidth();
                        paddingRight2 += child.getMeasuredWidth();
                    }
                    int childOffset = (childLeft + scrollX) - child.getLeft();
                    if (childOffset != 0) {
                        child.offsetLeftAndRight(childOffset);
                    }
                }
            }
        }
        dispatchOnPageScrolled(position, offset, offsetPixels);
        if (r0.mPageTransformer != null) {
            scrollX = getScrollX();
            paddingLeft = getChildCount();
            while (i < paddingLeft) {
                View child2 = getChildAt(i);
                if (!((LayoutParams) child2.getLayoutParams()).isDecor) {
                    r0.mPageTransformer.transformPage(child2, ((float) (child2.getLeft() - scrollX)) / ((float) getClientWidth()));
                }
                i++;
            }
        }
        r0.mCalledSuper = true;
    }

    private void dispatchOnPageScrolled(int position, float offset, int offsetPixels) {
        if (this.mOnPageChangeListener != null) {
            this.mOnPageChangeListener.onPageScrolled(position, offset, offsetPixels);
        }
        if (this.mOnPageChangeListeners != null) {
            int z = this.mOnPageChangeListeners.size();
            for (int i = 0; i < z; i++) {
                OnPageChangeListener listener = (OnPageChangeListener) this.mOnPageChangeListeners.get(i);
                if (listener != null) {
                    listener.onPageScrolled(position, offset, offsetPixels);
                }
            }
        }
        if (this.mInternalPageChangeListener != null) {
            this.mInternalPageChangeListener.onPageScrolled(position, offset, offsetPixels);
        }
    }

    private void dispatchOnPageSelected(int position) {
        if (this.mOnPageChangeListener != null) {
            this.mOnPageChangeListener.onPageSelected(position);
        }
        if (this.mOnPageChangeListeners != null) {
            int z = this.mOnPageChangeListeners.size();
            for (int i = 0; i < z; i++) {
                OnPageChangeListener listener = (OnPageChangeListener) this.mOnPageChangeListeners.get(i);
                if (listener != null) {
                    listener.onPageSelected(position);
                }
            }
        }
        if (this.mInternalPageChangeListener != null) {
            this.mInternalPageChangeListener.onPageSelected(position);
        }
    }

    private void dispatchOnScrollStateChanged(int state) {
        if (this.mOnPageChangeListener != null) {
            this.mOnPageChangeListener.onPageScrollStateChanged(state);
        }
        if (this.mOnPageChangeListeners != null) {
            int z = this.mOnPageChangeListeners.size();
            for (int i = 0; i < z; i++) {
                OnPageChangeListener listener = (OnPageChangeListener) this.mOnPageChangeListeners.get(i);
                if (listener != null) {
                    listener.onPageScrollStateChanged(state);
                }
            }
        }
        if (this.mInternalPageChangeListener != null) {
            this.mInternalPageChangeListener.onPageScrollStateChanged(state);
        }
    }

    private void completeScroll(boolean postEvents) {
        boolean needPopulate = this.mScrollState == 2;
        if (needPopulate) {
            setScrollingCacheEnabled(false);
            if (true ^ this.mScroller.isFinished()) {
                this.mScroller.abortAnimation();
                int oldX = getScrollX();
                int oldY = getScrollY();
                int x = this.mScroller.getCurrX();
                int y = this.mScroller.getCurrY();
                if (!(oldX == x && oldY == y)) {
                    scrollTo(x, y);
                    if (x != oldX) {
                        pageScrolled(x);
                    }
                }
            }
        }
        this.mPopulatePending = false;
        boolean needPopulate2 = needPopulate;
        for (int i = 0; i < this.mItems.size(); i++) {
            ItemInfo ii = (ItemInfo) this.mItems.get(i);
            if (ii.scrolling) {
                needPopulate2 = true;
                ii.scrolling = false;
            }
        }
        if (!needPopulate2) {
            return;
        }
        if (postEvents) {
            ViewCompat.postOnAnimation(this, this.mEndScrollRunnable);
        } else {
            this.mEndScrollRunnable.run();
        }
    }

    private boolean isGutterDrag(float x, float dx) {
        return (x < ((float) this.mGutterSize) && dx > 0.0f) || (x > ((float) (getWidth() - this.mGutterSize)) && dx < 0.0f);
    }

    private void enableLayers(boolean enable) {
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            getChildAt(i).setLayerType(enable ? this.mPageTransformerLayerType : 0, null);
        }
    }

    public boolean onInterceptTouchEvent(MotionEvent ev) {
        View view = this;
        MotionEvent motionEvent = ev;
        int action = ev.getAction() & 255;
        if (action != 3) {
            if (action != 1) {
                if (action != 0) {
                    if (view.mIsBeingDragged) {
                        return true;
                    }
                    if (view.mIsUnableToDrag) {
                        return false;
                    }
                }
                if (action == 0) {
                    float x = ev.getX();
                    view.mInitialMotionX = x;
                    view.mLastMotionX = x;
                    x = ev.getY();
                    view.mInitialMotionY = x;
                    view.mLastMotionY = x;
                    view.mActivePointerId = motionEvent.getPointerId(0);
                    view.mIsUnableToDrag = false;
                    view.mIsScrollStarted = true;
                    view.mScroller.computeScrollOffset();
                    if (view.mScrollState != 2 || Math.abs(view.mScroller.getFinalX() - view.mScroller.getCurrX()) <= view.mCloseEnough) {
                        completeScroll(false);
                        view.mIsBeingDragged = false;
                    } else {
                        view.mScroller.abortAnimation();
                        view.mPopulatePending = false;
                        populate();
                        view.mIsBeingDragged = true;
                        requestParentDisallowInterceptTouchEvent(true);
                        setScrollState(1);
                    }
                } else if (action == 2) {
                    int activePointerId = view.mActivePointerId;
                    if (activePointerId != -1) {
                        float y;
                        int pointerIndex = motionEvent.findPointerIndex(activePointerId);
                        float x2 = motionEvent.getX(pointerIndex);
                        float dx = x2 - view.mLastMotionX;
                        float xDiff = Math.abs(dx);
                        float y2 = motionEvent.getY(pointerIndex);
                        float yDiff = Math.abs(y2 - view.mInitialMotionY);
                        if (dx == 0.0f || isGutterDrag(view.mLastMotionX, dx)) {
                            y = y2;
                        } else {
                            y = y2;
                            if (canScroll(view, false, (int) dx, (int) x2, (int) y2)) {
                                view.mLastMotionX = x2;
                                view.mLastMotionY = y;
                                view.mIsUnableToDrag = true;
                                return false;
                            }
                        }
                        if (xDiff > ((float) view.mTouchSlop) && 0.5f * xDiff > yDiff) {
                            view.mIsBeingDragged = true;
                            requestParentDisallowInterceptTouchEvent(true);
                            setScrollState(1);
                            view.mLastMotionX = dx > 0.0f ? view.mInitialMotionX + ((float) view.mTouchSlop) : view.mInitialMotionX - ((float) view.mTouchSlop);
                            view.mLastMotionY = y;
                            setScrollingCacheEnabled(true);
                        } else if (yDiff > ((float) view.mTouchSlop)) {
                            view.mIsUnableToDrag = true;
                        }
                        if (view.mIsBeingDragged && performDrag(x2)) {
                            ViewCompat.postInvalidateOnAnimation(this);
                        }
                    }
                } else if (action == 6) {
                    onSecondaryPointerUp(ev);
                }
                if (view.mVelocityTracker == null) {
                    view.mVelocityTracker = VelocityTracker.obtain();
                }
                view.mVelocityTracker.addMovement(motionEvent);
                return view.mIsBeingDragged;
            }
        }
        resetTouch();
        return false;
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean onTouchEvent(android.view.MotionEvent r18) {
        /*
        r17 = this;
        r0 = r17;
        r1 = r18;
        r2 = r0.mFakeDragging;
        r3 = 1;
        if (r2 == 0) goto L_0x000a;
    L_0x0009:
        return r3;
    L_0x000a:
        r2 = r18.getAction();
        r4 = 0;
        if (r2 != 0) goto L_0x0018;
    L_0x0011:
        r2 = r18.getEdgeFlags();
        if (r2 == 0) goto L_0x0018;
    L_0x0017:
        return r4;
    L_0x0018:
        r2 = r0.mAdapter;
        if (r2 == 0) goto L_0x0166;
    L_0x001c:
        r2 = r0.mAdapter;
        r2 = r2.getCount();
        if (r2 != 0) goto L_0x0026;
    L_0x0024:
        goto L_0x0166;
    L_0x0026:
        r2 = r0.mVelocityTracker;
        if (r2 != 0) goto L_0x0030;
    L_0x002a:
        r2 = android.view.VelocityTracker.obtain();
        r0.mVelocityTracker = r2;
    L_0x0030:
        r2 = r0.mVelocityTracker;
        r2.addMovement(r1);
        r2 = r18.getAction();
        r5 = 0;
        r6 = r2 & 255;
        switch(r6) {
            case 0: goto L_0x013d;
            case 1: goto L_0x00ec;
            case 2: goto L_0x0075;
            case 3: goto L_0x0064;
            case 4: goto L_0x003f;
            case 5: goto L_0x0053;
            case 6: goto L_0x0043;
            default: goto L_0x003f;
        };
    L_0x003f:
        r16 = r2;
        goto L_0x0160;
    L_0x0043:
        r17.onSecondaryPointerUp(r18);
        r4 = r0.mActivePointerId;
        r4 = r1.findPointerIndex(r4);
        r4 = r1.getX(r4);
        r0.mLastMotionX = r4;
        goto L_0x0071;
    L_0x0053:
        r4 = r18.getActionIndex();
        r6 = r1.getX(r4);
        r0.mLastMotionX = r6;
        r7 = r1.getPointerId(r4);
        r0.mActivePointerId = r7;
        goto L_0x0071;
    L_0x0064:
        r6 = r0.mIsBeingDragged;
        if (r6 == 0) goto L_0x0071;
    L_0x0068:
        r6 = r0.mCurItem;
        r0.scrollToItem(r6, r3, r4, r4);
        r5 = r17.resetTouch();
    L_0x0071:
        r16 = r2;
        goto L_0x0160;
    L_0x0075:
        r4 = r0.mIsBeingDragged;
        if (r4 != 0) goto L_0x00d8;
    L_0x0079:
        r4 = r0.mActivePointerId;
        r4 = r1.findPointerIndex(r4);
        r6 = -1;
        if (r4 != r6) goto L_0x0087;
    L_0x0082:
        r5 = r17.resetTouch();
        goto L_0x0071;
    L_0x0087:
        r6 = r1.getX(r4);
        r7 = r0.mLastMotionX;
        r7 = r6 - r7;
        r7 = java.lang.Math.abs(r7);
        r8 = r1.getY(r4);
        r9 = r0.mLastMotionY;
        r9 = r8 - r9;
        r9 = java.lang.Math.abs(r9);
        r10 = r0.mTouchSlop;
        r10 = (float) r10;
        r10 = (r7 > r10 ? 1 : (r7 == r10 ? 0 : -1));
        if (r10 <= 0) goto L_0x00d8;
    L_0x00a6:
        r10 = (r7 > r9 ? 1 : (r7 == r9 ? 0 : -1));
        if (r10 <= 0) goto L_0x00d8;
    L_0x00aa:
        r0.mIsBeingDragged = r3;
        r0.requestParentDisallowInterceptTouchEvent(r3);
        r10 = r0.mInitialMotionX;
        r10 = r6 - r10;
        r11 = 0;
        r10 = (r10 > r11 ? 1 : (r10 == r11 ? 0 : -1));
        if (r10 <= 0) goto L_0x00bf;
    L_0x00b8:
        r10 = r0.mInitialMotionX;
        r11 = r0.mTouchSlop;
        r11 = (float) r11;
        r10 = r10 + r11;
        goto L_0x00c5;
    L_0x00bf:
        r10 = r0.mInitialMotionX;
        r11 = r0.mTouchSlop;
        r11 = (float) r11;
        r10 = r10 - r11;
    L_0x00c5:
        r0.mLastMotionX = r10;
        r0.mLastMotionY = r8;
        r0.setScrollState(r3);
        r0.setScrollingCacheEnabled(r3);
        r10 = r17.getParent();
        if (r10 == 0) goto L_0x00d8;
    L_0x00d5:
        r10.requestDisallowInterceptTouchEvent(r3);
    L_0x00d8:
        r4 = r0.mIsBeingDragged;
        if (r4 == 0) goto L_0x0071;
    L_0x00dc:
        r4 = r0.mActivePointerId;
        r4 = r1.findPointerIndex(r4);
        r6 = r1.getX(r4);
        r7 = r0.performDrag(r6);
        r5 = r5 | r7;
        goto L_0x0071;
    L_0x00ec:
        r4 = r0.mIsBeingDragged;
        if (r4 == 0) goto L_0x003f;
    L_0x00f0:
        r4 = r0.mVelocityTracker;
        r6 = 1000; // 0x3e8 float:1.401E-42 double:4.94E-321;
        r7 = r0.mMaximumVelocity;
        r7 = (float) r7;
        r4.computeCurrentVelocity(r6, r7);
        r6 = r0.mActivePointerId;
        r6 = r4.getXVelocity(r6);
        r6 = (int) r6;
        r0.mPopulatePending = r3;
        r7 = r17.getClientWidth();
        r8 = r17.getScrollX();
        r9 = r17.infoForCurrentScrollPosition();
        r10 = r0.mPageMargin;
        r10 = (float) r10;
        r11 = (float) r7;
        r10 = r10 / r11;
        r11 = r9.position;
        r12 = (float) r8;
        r13 = (float) r7;
        r12 = r12 / r13;
        r13 = r9.offset;
        r12 = r12 - r13;
        r13 = r9.widthFactor;
        r13 = r13 + r10;
        r12 = r12 / r13;
        r13 = r0.mActivePointerId;
        r13 = r1.findPointerIndex(r13);
        r14 = r1.getX(r13);
        r15 = r0.mInitialMotionX;
        r15 = r14 - r15;
        r15 = (int) r15;
        r16 = r2;
        r2 = r0.determineTargetPage(r11, r12, r6, r15);
        r0.setCurrentItemInternal(r2, r3, r3, r6);
        r5 = r17.resetTouch();
        goto L_0x0160;
    L_0x013d:
        r16 = r2;
        r2 = r0.mScroller;
        r2.abortAnimation();
        r0.mPopulatePending = r4;
        r17.populate();
        r2 = r18.getX();
        r0.mInitialMotionX = r2;
        r0.mLastMotionX = r2;
        r2 = r18.getY();
        r0.mInitialMotionY = r2;
        r0.mLastMotionY = r2;
        r2 = r1.getPointerId(r4);
        r0.mActivePointerId = r2;
    L_0x0160:
        if (r5 == 0) goto L_0x0165;
    L_0x0162:
        android.support.v4.view.ViewCompat.postInvalidateOnAnimation(r17);
    L_0x0165:
        return r3;
    L_0x0166:
        return r4;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.support.v4.view.ViewPager.onTouchEvent(android.view.MotionEvent):boolean");
    }

    private boolean resetTouch() {
        this.mActivePointerId = -1;
        endDrag();
        this.mLeftEdge.onRelease();
        this.mRightEdge.onRelease();
        if (!this.mLeftEdge.isFinished()) {
            if (!this.mRightEdge.isFinished()) {
                return false;
            }
        }
        return true;
    }

    private void requestParentDisallowInterceptTouchEvent(boolean disallowIntercept) {
        ViewParent parent = getParent();
        if (parent != null) {
            parent.requestDisallowInterceptTouchEvent(disallowIntercept);
        }
    }

    private boolean performDrag(float x) {
        float f = x;
        boolean needsInvalidate = false;
        float deltaX = this.mLastMotionX - f;
        this.mLastMotionX = f;
        float scrollX = ((float) getScrollX()) + deltaX;
        int width = getClientWidth();
        float leftBound = ((float) width) * this.mFirstOffset;
        float rightBound = ((float) width) * this.mLastOffset;
        boolean leftAbsolute = true;
        boolean rightAbsolute = true;
        ItemInfo firstItem = (ItemInfo) this.mItems.get(0);
        ItemInfo lastItem = (ItemInfo) this.mItems.get(this.mItems.size() - 1);
        if (firstItem.position != 0) {
            leftAbsolute = false;
            leftBound = firstItem.offset * ((float) width);
        }
        if (lastItem.position != r0.mAdapter.getCount() - 1) {
            rightAbsolute = false;
            rightBound = lastItem.offset * ((float) width);
        }
        if (scrollX < leftBound) {
            if (leftAbsolute) {
                r0.mLeftEdge.onPull(Math.abs(leftBound - scrollX) / ((float) width));
                needsInvalidate = true;
            }
            scrollX = leftBound;
        } else if (scrollX > rightBound) {
            if (rightAbsolute) {
                r0.mRightEdge.onPull(Math.abs(scrollX - rightBound) / ((float) width));
                needsInvalidate = true;
            }
            scrollX = rightBound;
        }
        r0.mLastMotionX += scrollX - ((float) ((int) scrollX));
        scrollTo((int) scrollX, getScrollY());
        pageScrolled((int) scrollX);
        return needsInvalidate;
    }

    private ItemInfo infoForCurrentScrollPosition() {
        int width = getClientWidth();
        float marginOffset = 0.0f;
        float scrollOffset = width > 0 ? ((float) getScrollX()) / ((float) width) : 0.0f;
        if (width > 0) {
            marginOffset = ((float) this.mPageMargin) / ((float) width);
        }
        int lastPos = -1;
        float lastOffset = 0.0f;
        float lastWidth = 0.0f;
        boolean first = true;
        ItemInfo lastItem = null;
        int i = 0;
        while (i < this.mItems.size()) {
            ItemInfo ii = (ItemInfo) this.mItems.get(i);
            if (!(first || ii.position == lastPos + 1)) {
                ii = this.mTempItem;
                ii.offset = (lastOffset + lastWidth) + marginOffset;
                ii.position = lastPos + 1;
                ii.widthFactor = this.mAdapter.getPageWidth(ii.position);
                i--;
            }
            float offset = ii.offset;
            float leftBound = offset;
            float rightBound = (ii.widthFactor + offset) + marginOffset;
            if (!first) {
                if (scrollOffset < leftBound) {
                    return lastItem;
                }
            }
            if (scrollOffset >= rightBound) {
                if (i != this.mItems.size() - 1) {
                    first = false;
                    lastPos = ii.position;
                    lastOffset = offset;
                    lastWidth = ii.widthFactor;
                    lastItem = ii;
                    i++;
                }
            }
            return ii;
        }
        return lastItem;
    }

    private int determineTargetPage(int currentPage, float pageOffset, int velocity, int deltaX) {
        int targetPage;
        if (Math.abs(deltaX) <= this.mFlingDistance || Math.abs(velocity) <= this.mMinimumVelocity) {
            targetPage = currentPage + ((int) (pageOffset + (currentPage >= this.mCurItem ? 0.4f : 0.6f)));
        } else {
            targetPage = velocity > 0 ? currentPage : currentPage + 1;
        }
        if (this.mItems.size() <= 0) {
            return targetPage;
        }
        return Math.max(((ItemInfo) this.mItems.get(0)).position, Math.min(targetPage, ((ItemInfo) this.mItems.get(this.mItems.size() - 1)).position));
    }

    public void draw(Canvas canvas) {
        super.draw(canvas);
        boolean needsInvalidate = false;
        int overScrollMode = getOverScrollMode();
        if (overScrollMode != 0) {
            if (overScrollMode != 1 || this.mAdapter == null || this.mAdapter.getCount() <= 1) {
                this.mLeftEdge.finish();
                this.mRightEdge.finish();
                if (needsInvalidate) {
                    ViewCompat.postInvalidateOnAnimation(this);
                }
            }
        }
        if (!this.mLeftEdge.isFinished()) {
            int restoreCount = canvas.save();
            int height = (getHeight() - getPaddingTop()) - getPaddingBottom();
            int width = getWidth();
            canvas.rotate(270.0f);
            canvas.translate((float) ((-height) + getPaddingTop()), this.mFirstOffset * ((float) width));
            this.mLeftEdge.setSize(height, width);
            needsInvalidate = false | this.mLeftEdge.draw(canvas);
            canvas.restoreToCount(restoreCount);
        }
        if (!this.mRightEdge.isFinished()) {
            restoreCount = canvas.save();
            height = getWidth();
            width = (getHeight() - getPaddingTop()) - getPaddingBottom();
            canvas.rotate(90.0f);
            canvas.translate((float) (-getPaddingTop()), (-(this.mLastOffset + 1.0f)) * ((float) height));
            this.mRightEdge.setSize(width, height);
            needsInvalidate |= this.mRightEdge.draw(canvas);
            canvas.restoreToCount(restoreCount);
        }
        if (needsInvalidate) {
            ViewCompat.postInvalidateOnAnimation(this);
        }
    }

    protected void onDraw(Canvas canvas) {
        Canvas canvas2;
        super.onDraw(canvas);
        if (this.mPageMargin > 0 && r0.mMarginDrawable != null && r0.mItems.size() > 0 && r0.mAdapter != null) {
            int scrollX = getScrollX();
            int width = getWidth();
            float marginOffset = ((float) r0.mPageMargin) / ((float) width);
            ItemInfo ii = (ItemInfo) r0.mItems.get(0);
            float offset = ii.offset;
            int itemCount = r0.mItems.size();
            int firstPos = ii.position;
            int lastPos = ((ItemInfo) r0.mItems.get(itemCount - 1)).position;
            float offset2 = offset;
            int itemIndex = 0;
            int pos = firstPos;
            while (pos < lastPos) {
                float drawAt;
                float marginOffset2;
                while (pos > ii.position && itemIndex < itemCount) {
                    itemIndex++;
                    ii = (ItemInfo) r0.mItems.get(itemIndex);
                }
                if (pos == ii.position) {
                    offset2 = (ii.offset + ii.widthFactor) + marginOffset;
                    drawAt = (ii.offset + ii.widthFactor) * ((float) width);
                } else {
                    float widthFactor = r0.mAdapter.getPageWidth(pos);
                    drawAt = (offset2 + widthFactor) * ((float) width);
                    offset2 += widthFactor + marginOffset;
                }
                if (((float) r0.mPageMargin) + drawAt > ((float) scrollX)) {
                    marginOffset2 = marginOffset;
                    r0.mMarginDrawable.setBounds(Math.round(drawAt), r0.mTopPageBounds, Math.round(((float) r0.mPageMargin) + drawAt), r0.mBottomPageBounds);
                    r0.mMarginDrawable.draw(canvas);
                } else {
                    canvas2 = canvas;
                    marginOffset2 = marginOffset;
                }
                if (drawAt <= ((float) (scrollX + width))) {
                    pos++;
                    marginOffset = marginOffset2;
                } else {
                    return;
                }
            }
        }
        canvas2 = canvas;
    }

    public boolean beginFakeDrag() {
        if (this.mIsBeingDragged) {
            return false;
        }
        this.mFakeDragging = true;
        setScrollState(1);
        this.mLastMotionX = 0.0f;
        this.mInitialMotionX = 0.0f;
        if (this.mVelocityTracker == null) {
            this.mVelocityTracker = VelocityTracker.obtain();
        } else {
            this.mVelocityTracker.clear();
        }
        long time = SystemClock.uptimeMillis();
        MotionEvent ev = MotionEvent.obtain(time, time, 0, 0.0f, 0.0f, 0);
        this.mVelocityTracker.addMovement(ev);
        ev.recycle();
        this.mFakeDragBeginTime = time;
        return true;
    }

    public void endFakeDrag() {
        if (this.mFakeDragging) {
            if (this.mAdapter != null) {
                VelocityTracker velocityTracker = this.mVelocityTracker;
                velocityTracker.computeCurrentVelocity(1000, (float) this.mMaximumVelocity);
                int initialVelocity = (int) velocityTracker.getXVelocity(this.mActivePointerId);
                this.mPopulatePending = true;
                int width = getClientWidth();
                int scrollX = getScrollX();
                ItemInfo ii = infoForCurrentScrollPosition();
                setCurrentItemInternal(determineTargetPage(ii.position, ((((float) scrollX) / ((float) width)) - ii.offset) / ii.widthFactor, initialVelocity, (int) (this.mLastMotionX - this.mInitialMotionX)), true, true, initialVelocity);
            }
            endDrag();
            this.mFakeDragging = false;
            return;
        }
        throw new IllegalStateException("No fake drag in progress. Call beginFakeDrag first.");
    }

    public void fakeDragBy(float xOffset) {
        if (!this.mFakeDragging) {
            throw new IllegalStateException("No fake drag in progress. Call beginFakeDrag first.");
        } else if (r0.mAdapter != null) {
            r0.mLastMotionX += xOffset;
            float scrollX = ((float) getScrollX()) - xOffset;
            int width = getClientWidth();
            float leftBound = ((float) width) * r0.mFirstOffset;
            float rightBound = ((float) width) * r0.mLastOffset;
            ItemInfo firstItem = (ItemInfo) r0.mItems.get(0);
            ItemInfo lastItem = (ItemInfo) r0.mItems.get(r0.mItems.size() - 1);
            if (firstItem.position != 0) {
                leftBound = firstItem.offset * ((float) width);
            }
            if (lastItem.position != r0.mAdapter.getCount() - 1) {
                rightBound = lastItem.offset * ((float) width);
            }
            if (scrollX < leftBound) {
                scrollX = leftBound;
            } else if (scrollX > rightBound) {
                scrollX = rightBound;
            }
            r0.mLastMotionX += scrollX - ((float) ((int) scrollX));
            scrollTo((int) scrollX, getScrollY());
            pageScrolled((int) scrollX);
            MotionEvent ev = MotionEvent.obtain(r0.mFakeDragBeginTime, SystemClock.uptimeMillis(), 2, r0.mLastMotionX, 0.0f, 0);
            r0.mVelocityTracker.addMovement(ev);
            ev.recycle();
        }
    }

    public boolean isFakeDragging() {
        return this.mFakeDragging;
    }

    private void onSecondaryPointerUp(MotionEvent ev) {
        int pointerIndex = ev.getActionIndex();
        if (ev.getPointerId(pointerIndex) == this.mActivePointerId) {
            int newPointerIndex = pointerIndex == 0 ? 1 : 0;
            this.mLastMotionX = ev.getX(newPointerIndex);
            this.mActivePointerId = ev.getPointerId(newPointerIndex);
            if (this.mVelocityTracker != null) {
                this.mVelocityTracker.clear();
            }
        }
    }

    private void endDrag() {
        this.mIsBeingDragged = false;
        this.mIsUnableToDrag = false;
        if (this.mVelocityTracker != null) {
            this.mVelocityTracker.recycle();
            this.mVelocityTracker = null;
        }
    }

    private void setScrollingCacheEnabled(boolean enabled) {
        if (this.mScrollingCacheEnabled != enabled) {
            this.mScrollingCacheEnabled = enabled;
        }
    }

    public boolean canScrollHorizontally(int direction) {
        boolean z = false;
        if (this.mAdapter == null) {
            return false;
        }
        int width = getClientWidth();
        int scrollX = getScrollX();
        if (direction < 0) {
            if (scrollX > ((int) (((float) width) * this.mFirstOffset))) {
                z = true;
            }
            return z;
        } else if (direction <= 0) {
            return false;
        } else {
            if (scrollX < ((int) (((float) width) * this.mLastOffset))) {
                z = true;
            }
            return z;
        }
    }

    protected boolean canScroll(View v, boolean checkV, int dx, int x, int y) {
        View view = v;
        boolean z = true;
        if (view instanceof ViewGroup) {
            ViewGroup group = (ViewGroup) view;
            int scrollX = view.getScrollX();
            int scrollY = view.getScrollY();
            for (int i = group.getChildCount() - 1; i >= 0; i--) {
                View child = group.getChildAt(i);
                if (x + scrollX >= child.getLeft() && x + scrollX < child.getRight() && y + scrollY >= child.getTop() && y + scrollY < child.getBottom()) {
                    if (canScroll(child, true, dx, (x + scrollX) - child.getLeft(), (y + scrollY) - child.getTop())) {
                        return true;
                    }
                }
            }
        }
        if (!checkV) {
            scrollX = dx;
        } else if (view.canScrollHorizontally(-dx)) {
            return z;
        }
        z = false;
        return z;
    }

    public boolean dispatchKeyEvent(KeyEvent event) {
        if (!super.dispatchKeyEvent(event)) {
            if (!executeKeyEvent(event)) {
                return false;
            }
        }
        return true;
    }

    public boolean executeKeyEvent(KeyEvent event) {
        if (event.getAction() != 0) {
            return false;
        }
        int keyCode = event.getKeyCode();
        if (keyCode != 61) {
            switch (keyCode) {
                case 21:
                    if (event.hasModifiers(2)) {
                        return pageLeft();
                    }
                    return arrowScroll(17);
                case 22:
                    if (event.hasModifiers(2)) {
                        return pageRight();
                    }
                    return arrowScroll(66);
                default:
                    return false;
            }
        } else if (event.hasNoModifiers()) {
            return arrowScroll(2);
        } else {
            if (event.hasModifiers(1)) {
                return arrowScroll(1);
            }
            return false;
        }
    }

    public boolean arrowScroll(int direction) {
        boolean isChild;
        View currentFocused = findFocus();
        if (currentFocused == this) {
            currentFocused = null;
        } else if (currentFocused != null) {
            isChild = false;
            for (ViewPager parent = currentFocused.getParent(); parent instanceof ViewGroup; parent = parent.getParent()) {
                if (parent == this) {
                    isChild = true;
                    break;
                }
            }
            if (!isChild) {
                StringBuilder sb = new StringBuilder();
                sb.append(currentFocused.getClass().getSimpleName());
                for (ViewParent parent2 = currentFocused.getParent(); parent2 instanceof ViewGroup; parent2 = parent2.getParent()) {
                    sb.append(" => ");
                    sb.append(parent2.getClass().getSimpleName());
                }
                String str = TAG;
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("arrowScroll tried to find focus based on non-child current focused view ");
                stringBuilder.append(sb.toString());
                Log.e(str, stringBuilder.toString());
                currentFocused = null;
            }
        }
        isChild = false;
        View nextFocused = FocusFinder.getInstance().findNextFocus(this, currentFocused, direction);
        if (nextFocused == null || nextFocused == currentFocused) {
            if (direction != 17) {
                if (direction != 1) {
                    if (direction == 66 || direction == 2) {
                        isChild = pageRight();
                    }
                }
            }
            isChild = pageLeft();
        } else if (direction == 17) {
            nextLeft = getChildRectInPagerCoordinates(this.mTempRect, nextFocused).left;
            currLeft = getChildRectInPagerCoordinates(this.mTempRect, currentFocused).left;
            if (currentFocused == null || nextLeft < currLeft) {
                isChild = nextFocused.requestFocus();
            } else {
                isChild = pageLeft();
            }
        } else if (direction == 66) {
            nextLeft = getChildRectInPagerCoordinates(this.mTempRect, nextFocused).left;
            currLeft = getChildRectInPagerCoordinates(this.mTempRect, currentFocused).left;
            if (currentFocused == null || nextLeft > currLeft) {
                isChild = nextFocused.requestFocus();
            } else {
                isChild = pageRight();
            }
        }
        if (isChild) {
            playSoundEffect(SoundEffectConstants.getContantForFocusDirection(direction));
        }
        return isChild;
    }

    private Rect getChildRectInPagerCoordinates(Rect outRect, View child) {
        if (outRect == null) {
            outRect = new Rect();
        }
        if (child == null) {
            outRect.set(0, 0, 0, 0);
            return outRect;
        }
        outRect.left = child.getLeft();
        outRect.right = child.getRight();
        outRect.top = child.getTop();
        outRect.bottom = child.getBottom();
        ViewGroup parent = child.getParent();
        while ((parent instanceof ViewGroup) && parent != this) {
            ViewGroup group = parent;
            outRect.left += group.getLeft();
            outRect.right += group.getRight();
            outRect.top += group.getTop();
            outRect.bottom += group.getBottom();
            parent = group.getParent();
        }
        return outRect;
    }

    boolean pageLeft() {
        if (this.mCurItem <= 0) {
            return false;
        }
        setCurrentItem(this.mCurItem - 1, true);
        return true;
    }

    boolean pageRight() {
        if (this.mAdapter == null || this.mCurItem >= this.mAdapter.getCount() - 1) {
            return false;
        }
        setCurrentItem(this.mCurItem + 1, true);
        return true;
    }

    public void addFocusables(ArrayList<View> views, int direction, int focusableMode) {
        int focusableCount = views.size();
        int descendantFocusability = getDescendantFocusability();
        if (descendantFocusability != 393216) {
            for (int i = 0; i < getChildCount(); i++) {
                View child = getChildAt(i);
                if (child.getVisibility() == 0) {
                    ItemInfo ii = infoForChild(child);
                    if (ii != null && ii.position == this.mCurItem) {
                        child.addFocusables(views, direction, focusableMode);
                    }
                }
            }
        }
        if ((descendantFocusability == 262144 && focusableCount != views.size()) || !isFocusable()) {
            return;
        }
        if (!(((focusableMode & 1) == 1 && isInTouchMode() && !isFocusableInTouchMode()) || views == null)) {
            views.add(this);
        }
    }

    public void addTouchables(ArrayList<View> views) {
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            if (child.getVisibility() == 0) {
                ItemInfo ii = infoForChild(child);
                if (ii != null && ii.position == this.mCurItem) {
                    child.addTouchables(views);
                }
            }
        }
    }

    protected boolean onRequestFocusInDescendants(int direction, Rect previouslyFocusedRect) {
        int index;
        int increment;
        int end;
        int count = getChildCount();
        if ((direction & 2) != 0) {
            index = 0;
            increment = 1;
            end = count;
        } else {
            index = count - 1;
            increment = -1;
            end = -1;
        }
        for (int i = index; i != end; i += increment) {
            View child = getChildAt(i);
            if (child.getVisibility() == 0) {
                ItemInfo ii = infoForChild(child);
                if (ii != null && ii.position == this.mCurItem && child.requestFocus(direction, previouslyFocusedRect)) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean dispatchPopulateAccessibilityEvent(AccessibilityEvent event) {
        if (event.getEventType() == 4096) {
            return super.dispatchPopulateAccessibilityEvent(event);
        }
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = getChildAt(i);
            if (child.getVisibility() == 0) {
                ItemInfo ii = infoForChild(child);
                if (ii != null && ii.position == this.mCurItem && child.dispatchPopulateAccessibilityEvent(event)) {
                    return true;
                }
            }
        }
        return false;
    }

    protected android.view.ViewGroup.LayoutParams generateDefaultLayoutParams() {
        return new LayoutParams();
    }

    protected android.view.ViewGroup.LayoutParams generateLayoutParams(android.view.ViewGroup.LayoutParams p) {
        return generateDefaultLayoutParams();
    }

    protected boolean checkLayoutParams(android.view.ViewGroup.LayoutParams p) {
        return (p instanceof LayoutParams) && super.checkLayoutParams(p);
    }

    public android.view.ViewGroup.LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new LayoutParams(getContext(), attrs);
    }
}
