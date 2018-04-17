package android.support.v4.view;

import android.content.Context;
import android.os.Build.VERSION;
import android.os.Handler;
import android.os.Message;
import android.view.GestureDetector;
import android.view.GestureDetector.OnDoubleTapListener;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.ViewConfiguration;

public final class GestureDetectorCompat {
    private final GestureDetectorCompatImpl mImpl;

    interface GestureDetectorCompatImpl {
        boolean onTouchEvent(MotionEvent motionEvent);
    }

    static class GestureDetectorCompatImplBase implements GestureDetectorCompatImpl {
        private static final int DOUBLE_TAP_TIMEOUT = ViewConfiguration.getDoubleTapTimeout();
        private static final int LONGPRESS_TIMEOUT = ViewConfiguration.getLongPressTimeout();
        private static final int TAP_TIMEOUT = ViewConfiguration.getTapTimeout();
        private boolean mAlwaysInBiggerTapRegion;
        private boolean mAlwaysInTapRegion;
        MotionEvent mCurrentDownEvent;
        boolean mDeferConfirmSingleTap;
        OnDoubleTapListener mDoubleTapListener;
        private int mDoubleTapSlopSquare;
        private float mDownFocusX;
        private float mDownFocusY;
        private final Handler mHandler;
        private boolean mInLongPress;
        private boolean mIsDoubleTapping;
        private boolean mIsLongpressEnabled;
        private float mLastFocusX;
        private float mLastFocusY;
        final OnGestureListener mListener;
        private int mMaximumFlingVelocity;
        private int mMinimumFlingVelocity;
        private MotionEvent mPreviousUpEvent;
        boolean mStillDown;
        private int mTouchSlopSquare;
        private VelocityTracker mVelocityTracker;

        private class GestureHandler extends Handler {
            GestureHandler() {
            }

            GestureHandler(Handler handler) {
                super(handler.getLooper());
            }

            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case 1:
                        GestureDetectorCompatImplBase.this.mListener.onShowPress(GestureDetectorCompatImplBase.this.mCurrentDownEvent);
                        return;
                    case 2:
                        GestureDetectorCompatImplBase.this.dispatchLongPress();
                        return;
                    case 3:
                        if (GestureDetectorCompatImplBase.this.mDoubleTapListener == null) {
                            return;
                        }
                        if (GestureDetectorCompatImplBase.this.mStillDown) {
                            GestureDetectorCompatImplBase.this.mDeferConfirmSingleTap = true;
                            return;
                        } else {
                            GestureDetectorCompatImplBase.this.mDoubleTapListener.onSingleTapConfirmed(GestureDetectorCompatImplBase.this.mCurrentDownEvent);
                            return;
                        }
                    default:
                        StringBuilder stringBuilder = new StringBuilder();
                        stringBuilder.append("Unknown message ");
                        stringBuilder.append(msg);
                        throw new RuntimeException(stringBuilder.toString());
                }
            }
        }

        GestureDetectorCompatImplBase(Context context, OnGestureListener listener, Handler handler) {
            if (handler != null) {
                this.mHandler = new GestureHandler(handler);
            } else {
                this.mHandler = new GestureHandler();
            }
            this.mListener = listener;
            if (listener instanceof OnDoubleTapListener) {
                setOnDoubleTapListener((OnDoubleTapListener) listener);
            }
            init(context);
        }

        private void init(Context context) {
            if (context == null) {
                throw new IllegalArgumentException("Context must not be null");
            } else if (this.mListener == null) {
                throw new IllegalArgumentException("OnGestureListener must not be null");
            } else {
                this.mIsLongpressEnabled = true;
                ViewConfiguration configuration = ViewConfiguration.get(context);
                int touchSlop = configuration.getScaledTouchSlop();
                int doubleTapSlop = configuration.getScaledDoubleTapSlop();
                this.mMinimumFlingVelocity = configuration.getScaledMinimumFlingVelocity();
                this.mMaximumFlingVelocity = configuration.getScaledMaximumFlingVelocity();
                this.mTouchSlopSquare = touchSlop * touchSlop;
                this.mDoubleTapSlopSquare = doubleTapSlop * doubleTapSlop;
            }
        }

        public void setOnDoubleTapListener(OnDoubleTapListener onDoubleTapListener) {
            this.mDoubleTapListener = onDoubleTapListener;
        }

        public boolean onTouchEvent(MotionEvent ev) {
            int i;
            MotionEvent motionEvent = ev;
            int action = ev.getAction();
            if (this.mVelocityTracker == null) {
                r0.mVelocityTracker = VelocityTracker.obtain();
            }
            r0.mVelocityTracker.addMovement(motionEvent);
            boolean pointerUp = (action & 255) == 6;
            int skipIndex = pointerUp ? ev.getActionIndex() : -1;
            int count = ev.getPointerCount();
            float sumY = 0.0f;
            float sumX = 0.0f;
            for (i = 0; i < count; i++) {
                if (skipIndex != i) {
                    sumX += motionEvent.getX(i);
                    sumY += motionEvent.getY(i);
                }
            }
            i = pointerUp ? count - 1 : count;
            float focusX = sumX / ((float) i);
            float focusY = sumY / ((float) i);
            boolean handled = false;
            boolean z;
            int i2;
            int pointerId;
            float velocityX;
            int distance;
            switch (action & 255) {
                case 0:
                    z = pointerUp;
                    i2 = skipIndex;
                    if (r0.mDoubleTapListener != null) {
                        boolean hadTapMessage = r0.mHandler.hasMessages(3);
                        if (hadTapMessage) {
                            r0.mHandler.removeMessages(3);
                        }
                        if (r0.mCurrentDownEvent == null || r0.mPreviousUpEvent == null || !hadTapMessage || !isConsideredDoubleTap(r0.mCurrentDownEvent, r0.mPreviousUpEvent, motionEvent)) {
                            r0.mHandler.sendEmptyMessageDelayed(3, (long) DOUBLE_TAP_TIMEOUT);
                        } else {
                            r0.mIsDoubleTapping = true;
                            handled = (r0.mDoubleTapListener.onDoubleTap(r0.mCurrentDownEvent) | false) | r0.mDoubleTapListener.onDoubleTapEvent(motionEvent);
                        }
                    }
                    r0.mLastFocusX = focusX;
                    r0.mDownFocusX = focusX;
                    r0.mLastFocusY = focusY;
                    r0.mDownFocusY = focusY;
                    if (r0.mCurrentDownEvent != null) {
                        r0.mCurrentDownEvent.recycle();
                    }
                    r0.mCurrentDownEvent = MotionEvent.obtain(ev);
                    r0.mAlwaysInTapRegion = true;
                    r0.mAlwaysInBiggerTapRegion = true;
                    r0.mStillDown = true;
                    r0.mInLongPress = false;
                    r0.mDeferConfirmSingleTap = false;
                    if (r0.mIsLongpressEnabled) {
                        r0.mHandler.removeMessages(2);
                        r0.mHandler.sendEmptyMessageAtTime(2, (r0.mCurrentDownEvent.getDownTime() + ((long) TAP_TIMEOUT)) + ((long) LONGPRESS_TIMEOUT));
                    }
                    r0.mHandler.sendEmptyMessageAtTime(1, r0.mCurrentDownEvent.getDownTime() + ((long) TAP_TIMEOUT));
                    return handled | r0.mListener.onDown(motionEvent);
                case 1:
                    z = pointerUp;
                    i2 = skipIndex;
                    r0.mStillDown = false;
                    action = MotionEvent.obtain(ev);
                    if (r0.mIsDoubleTapping) {
                        handled = false | r0.mDoubleTapListener.onDoubleTapEvent(motionEvent);
                    } else if (r0.mInLongPress) {
                        r0.mHandler.removeMessages(3);
                        r0.mInLongPress = false;
                    } else if (r0.mAlwaysInTapRegion) {
                        handled = r0.mListener.onSingleTapUp(motionEvent);
                        if (r0.mDeferConfirmSingleTap && r0.mDoubleTapListener != null) {
                            r0.mDoubleTapListener.onSingleTapConfirmed(motionEvent);
                        }
                    } else {
                        VelocityTracker velocityTracker = r0.mVelocityTracker;
                        pointerId = motionEvent.getPointerId(0);
                        velocityTracker.computeCurrentVelocity(1000, (float) r0.mMaximumFlingVelocity);
                        float velocityY = velocityTracker.getYVelocity(pointerId);
                        velocityX = velocityTracker.getXVelocity(pointerId);
                        if (Math.abs(velocityY) > ((float) r0.mMinimumFlingVelocity) || Math.abs(velocityX) > ((float) r0.mMinimumFlingVelocity)) {
                            handled = r0.mListener.onFling(r0.mCurrentDownEvent, motionEvent, velocityX, velocityY);
                        }
                    }
                    if (r0.mPreviousUpEvent != null) {
                        r0.mPreviousUpEvent.recycle();
                    }
                    r0.mPreviousUpEvent = action;
                    if (r0.mVelocityTracker != null) {
                        r0.mVelocityTracker.recycle();
                        r0.mVelocityTracker = null;
                    }
                    r0.mIsDoubleTapping = false;
                    r0.mDeferConfirmSingleTap = false;
                    r0.mHandler.removeMessages(1);
                    r0.mHandler.removeMessages(2);
                    return handled;
                case 2:
                    z = pointerUp;
                    i2 = skipIndex;
                    if (r0.mInLongPress != 0) {
                        return false;
                    }
                    action = r0.mLastFocusX - focusX;
                    pointerUp = r0.mLastFocusY - focusY;
                    if (r0.mIsDoubleTapping) {
                        return false | r0.mDoubleTapListener.onDoubleTapEvent(motionEvent);
                    }
                    if (r0.mAlwaysInTapRegion) {
                        skipIndex = (int) (focusX - r0.mDownFocusX);
                        int deltaY = (int) (focusY - r0.mDownFocusY);
                        distance = (skipIndex * skipIndex) + (deltaY * deltaY);
                        if (distance > r0.mTouchSlopSquare) {
                            boolean handled2 = r0.mListener.onScroll(r0.mCurrentDownEvent, motionEvent, action, pointerUp);
                            r0.mLastFocusX = focusX;
                            r0.mLastFocusY = focusY;
                            r0.mAlwaysInTapRegion = false;
                            r0.mHandler.removeMessages(3);
                            r0.mHandler.removeMessages(1);
                            r0.mHandler.removeMessages(2);
                            handled = handled2;
                        }
                        if (distance > r0.mTouchSlopSquare) {
                            r0.mAlwaysInBiggerTapRegion = false;
                        }
                        return handled;
                    } else if (Math.abs(action) < 1.0f && Math.abs(pointerUp) < 1.0f) {
                        return false;
                    } else {
                        handled = r0.mListener.onScroll(r0.mCurrentDownEvent, motionEvent, action, pointerUp);
                        r0.mLastFocusX = focusX;
                        r0.mLastFocusY = focusY;
                        return handled;
                    }
                case 3:
                    z = pointerUp;
                    i2 = skipIndex;
                    cancel();
                    return false;
                case 5:
                    z = pointerUp;
                    i2 = skipIndex;
                    r0.mLastFocusX = focusX;
                    r0.mDownFocusX = focusX;
                    r0.mLastFocusY = focusY;
                    r0.mDownFocusY = focusY;
                    cancelTaps();
                    return false;
                case 6:
                    r0.mLastFocusX = focusX;
                    r0.mDownFocusX = focusX;
                    r0.mLastFocusY = focusY;
                    r0.mDownFocusY = focusY;
                    r0.mVelocityTracker.computeCurrentVelocity(1000, (float) r0.mMaximumFlingVelocity);
                    pointerId = ev.getActionIndex();
                    distance = motionEvent.getPointerId(pointerId);
                    float x1 = r0.mVelocityTracker.getXVelocity(distance);
                    velocityX = r0.mVelocityTracker.getYVelocity(distance);
                    int i3 = 0;
                    while (true) {
                        int action2 = action;
                        action = i3;
                        int upIndex;
                        if (action < count) {
                            if (action == pointerId) {
                                z = pointerUp;
                                i2 = skipIndex;
                                upIndex = pointerId;
                            } else {
                                z = pointerUp;
                                pointerUp = motionEvent.getPointerId(action);
                                i2 = skipIndex;
                                upIndex = pointerId;
                                if ((r0.mVelocityTracker.getXVelocity(pointerUp) * x1) + (r0.mVelocityTracker.getYVelocity(pointerUp) * velocityX) < 0.0f) {
                                    boolean id2 = pointerUp;
                                    r0.mVelocityTracker.clear();
                                }
                            }
                            i3 = action + 1;
                            action = action2;
                            pointerUp = z;
                            skipIndex = i2;
                            pointerId = upIndex;
                        } else {
                            i2 = skipIndex;
                            upIndex = pointerId;
                        }
                        return false;
                    }
                default:
                    return false;
            }
        }

        private void cancel() {
            this.mHandler.removeMessages(1);
            this.mHandler.removeMessages(2);
            this.mHandler.removeMessages(3);
            this.mVelocityTracker.recycle();
            this.mVelocityTracker = null;
            this.mIsDoubleTapping = false;
            this.mStillDown = false;
            this.mAlwaysInTapRegion = false;
            this.mAlwaysInBiggerTapRegion = false;
            this.mDeferConfirmSingleTap = false;
            if (this.mInLongPress) {
                this.mInLongPress = false;
            }
        }

        private void cancelTaps() {
            this.mHandler.removeMessages(1);
            this.mHandler.removeMessages(2);
            this.mHandler.removeMessages(3);
            this.mIsDoubleTapping = false;
            this.mAlwaysInTapRegion = false;
            this.mAlwaysInBiggerTapRegion = false;
            this.mDeferConfirmSingleTap = false;
            if (this.mInLongPress) {
                this.mInLongPress = false;
            }
        }

        private boolean isConsideredDoubleTap(MotionEvent firstDown, MotionEvent firstUp, MotionEvent secondDown) {
            boolean z = false;
            if (!this.mAlwaysInBiggerTapRegion || secondDown.getEventTime() - firstUp.getEventTime() > ((long) DOUBLE_TAP_TIMEOUT)) {
                return false;
            }
            int deltaX = ((int) firstDown.getX()) - ((int) secondDown.getX());
            int deltaY = ((int) firstDown.getY()) - ((int) secondDown.getY());
            if ((deltaX * deltaX) + (deltaY * deltaY) < this.mDoubleTapSlopSquare) {
                z = true;
            }
            return z;
        }

        void dispatchLongPress() {
            this.mHandler.removeMessages(3);
            this.mDeferConfirmSingleTap = false;
            this.mInLongPress = true;
            this.mListener.onLongPress(this.mCurrentDownEvent);
        }
    }

    static class GestureDetectorCompatImplJellybeanMr2 implements GestureDetectorCompatImpl {
        private final GestureDetector mDetector;

        GestureDetectorCompatImplJellybeanMr2(Context context, OnGestureListener listener, Handler handler) {
            this.mDetector = new GestureDetector(context, listener, handler);
        }

        public boolean onTouchEvent(MotionEvent ev) {
            return this.mDetector.onTouchEvent(ev);
        }
    }

    public GestureDetectorCompat(Context context, OnGestureListener listener) {
        this(context, listener, null);
    }

    public GestureDetectorCompat(Context context, OnGestureListener listener, Handler handler) {
        if (VERSION.SDK_INT > 17) {
            this.mImpl = new GestureDetectorCompatImplJellybeanMr2(context, listener, handler);
        } else {
            this.mImpl = new GestureDetectorCompatImplBase(context, listener, handler);
        }
    }

    public boolean onTouchEvent(MotionEvent event) {
        return this.mImpl.onTouchEvent(event);
    }
}
