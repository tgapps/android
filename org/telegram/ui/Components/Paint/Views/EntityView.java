package org.telegram.ui.Components.Paint.Views;

import android.content.Context;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import java.util.UUID;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.ui.Components.Point;
import org.telegram.ui.Components.Rect;

public class EntityView extends FrameLayout {
    private boolean announcedSelection = false;
    private EntityViewDelegate delegate;
    private GestureDetector gestureDetector;
    private boolean hasPanned = false;
    private boolean hasReleased = false;
    private boolean hasTransformed = false;
    private int offsetX;
    private int offsetY;
    protected Point position = new Point();
    private float previousLocationX;
    private float previousLocationY;
    private boolean recognizedLongPress = false;
    protected SelectionView selectionView;
    private UUID uuid = UUID.randomUUID();

    public interface EntityViewDelegate {
        boolean allowInteraction(EntityView entityView);

        boolean onEntityLongClicked(EntityView entityView);

        boolean onEntitySelected(EntityView entityView);
    }

    public class SelectionView extends FrameLayout {
        public static final int SELECTION_LEFT_HANDLE = 1;
        public static final int SELECTION_RIGHT_HANDLE = 2;
        public static final int SELECTION_WHOLE_HANDLE = 3;
        private int currentHandle;
        protected Paint dotPaint = new Paint(1);
        protected Paint dotStrokePaint = new Paint(1);
        protected Paint paint = new Paint(1);

        public SelectionView(Context context) {
            super(context);
            setWillNotDraw(false);
            this.paint.setColor(-1);
            this.dotPaint.setColor(-12793105);
            this.dotStrokePaint.setColor(-1);
            this.dotStrokePaint.setStyle(Style.STROKE);
            this.dotStrokePaint.setStrokeWidth((float) AndroidUtilities.dp(1.0f));
        }

        protected void updatePosition() {
            Rect bounds = EntityView.this.getSelectionBounds();
            LayoutParams layoutParams = (LayoutParams) getLayoutParams();
            layoutParams.leftMargin = ((int) bounds.x) + EntityView.this.offsetX;
            layoutParams.topMargin = ((int) bounds.y) + EntityView.this.offsetY;
            layoutParams.width = (int) bounds.width;
            layoutParams.height = (int) bounds.height;
            setLayoutParams(layoutParams);
            setRotation(EntityView.this.getRotation());
        }

        protected int pointInsideHandle(float x, float y) {
            return 0;
        }

        /* JADX WARNING: inconsistent code. */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public boolean onTouchEvent(android.view.MotionEvent r19) {
            /*
            r18 = this;
            r0 = r18;
            r1 = r19.getActionMasked();
            r2 = 0;
            r3 = 0;
            r4 = 3;
            switch(r1) {
                case 0: goto L_0x0113;
                case 1: goto L_0x0106;
                case 2: goto L_0x0012;
                case 3: goto L_0x0106;
                case 4: goto L_0x000c;
                case 5: goto L_0x0113;
                case 6: goto L_0x0106;
                default: goto L_0x000c;
            };
        L_0x000c:
            r16 = r1;
            r17 = r2;
            goto L_0x0143;
        L_0x0012:
            r3 = r0.currentHandle;
            if (r3 != r4) goto L_0x0029;
        L_0x0016:
            r3 = r19.getRawX();
            r5 = r19.getRawY();
            r6 = org.telegram.ui.Components.Paint.Views.EntityView.this;
            r2 = r6.onTouchMove(r3, r5);
            r16 = r1;
            goto L_0x0145;
        L_0x0029:
            r3 = r0.currentHandle;
            if (r3 == 0) goto L_0x000c;
        L_0x002d:
            r3 = org.telegram.ui.Components.Paint.Views.EntityView.this;
            r5 = 1;
            r3.hasTransformed = r5;
            r3 = new org.telegram.ui.Components.Point;
            r6 = r19.getRawX();
            r7 = org.telegram.ui.Components.Paint.Views.EntityView.this;
            r7 = r7.previousLocationX;
            r6 = r6 - r7;
            r7 = r19.getRawY();
            r8 = org.telegram.ui.Components.Paint.Views.EntityView.this;
            r8 = r8.previousLocationY;
            r7 = r7 - r8;
            r3.<init>(r6, r7);
            r6 = r18.getRotation();
            r6 = (double) r6;
            r6 = java.lang.Math.toRadians(r6);
            r6 = (float) r6;
            r7 = r3.x;
            r7 = (double) r7;
            r9 = (double) r6;
            r9 = java.lang.Math.cos(r9);
            r7 = r7 * r9;
            r9 = r3.y;
            r9 = (double) r9;
            r11 = (double) r6;
            r11 = java.lang.Math.sin(r11);
            r9 = r9 * r11;
            r7 = r7 + r9;
            r7 = (float) r7;
            r8 = r0.currentHandle;
            if (r8 != r5) goto L_0x0073;
        L_0x0070:
            r8 = -1082130432; // 0xffffffffbf800000 float:-1.0 double:NaN;
            r7 = r7 * r8;
        L_0x0073:
            r8 = 1065353216; // 0x3f800000 float:1.0 double:5.263544247E-315;
            r9 = 1073741824; // 0x40000000 float:2.0 double:5.304989477E-315;
            r9 = r9 * r7;
            r10 = r18.getWidth();
            r10 = (float) r10;
            r9 = r9 / r10;
            r8 = r8 + r9;
            r9 = org.telegram.ui.Components.Paint.Views.EntityView.this;
            r9.scale(r8);
            r9 = r18.getLeft();
            r10 = r18.getWidth();
            r11 = 2;
            r10 = r10 / r11;
            r9 = r9 + r10;
            r9 = (float) r9;
            r10 = r18.getTop();
            r12 = r18.getHeight();
            r12 = r12 / r11;
            r10 = r10 + r12;
            r10 = (float) r10;
            r12 = r19.getRawX();
            r13 = r18.getParent();
            r13 = (android.view.View) r13;
            r13 = r13.getLeft();
            r13 = (float) r13;
            r12 = r12 - r13;
            r13 = r19.getRawY();
            r14 = r18.getParent();
            r14 = (android.view.View) r14;
            r14 = r14.getTop();
            r14 = (float) r14;
            r13 = r13 - r14;
            r14 = org.telegram.messenger.AndroidUtilities.statusBarHeight;
            r14 = (float) r14;
            r13 = r13 - r14;
            r14 = 0;
            r15 = r0.currentHandle;
            if (r15 != r5) goto L_0x00d4;
        L_0x00c4:
            r5 = r10 - r13;
            r4 = (double) r5;
            r11 = r9 - r12;
            r16 = r1;
            r17 = r2;
            r1 = (double) r11;
            r1 = java.lang.Math.atan2(r4, r1);
            r14 = (float) r1;
            goto L_0x00e7;
        L_0x00d4:
            r16 = r1;
            r17 = r2;
            r1 = r0.currentHandle;
            if (r1 != r11) goto L_0x00e7;
        L_0x00dc:
            r1 = r13 - r10;
            r1 = (double) r1;
            r4 = r12 - r9;
            r4 = (double) r4;
            r1 = java.lang.Math.atan2(r1, r4);
            r14 = (float) r1;
        L_0x00e7:
            r1 = org.telegram.ui.Components.Paint.Views.EntityView.this;
            r4 = (double) r14;
            r4 = java.lang.Math.toDegrees(r4);
            r2 = (float) r4;
            r1.rotate(r2);
            r1 = org.telegram.ui.Components.Paint.Views.EntityView.this;
            r2 = r19.getRawX();
            r1.previousLocationX = r2;
            r1 = org.telegram.ui.Components.Paint.Views.EntityView.this;
            r2 = r19.getRawY();
            r1.previousLocationY = r2;
            r2 = 1;
            goto L_0x0145;
        L_0x0106:
            r16 = r1;
            r17 = r2;
            r1 = org.telegram.ui.Components.Paint.Views.EntityView.this;
            r1.onTouchUp();
            r0.currentHandle = r3;
            r2 = 1;
            goto L_0x0145;
        L_0x0113:
            r16 = r1;
            r17 = r2;
            r1 = r19.getX();
            r2 = r19.getY();
            r1 = r0.pointInsideHandle(r1, r2);
            if (r1 == 0) goto L_0x0140;
        L_0x0125:
            r0.currentHandle = r1;
            r2 = org.telegram.ui.Components.Paint.Views.EntityView.this;
            r4 = r19.getRawX();
            r2.previousLocationX = r4;
            r2 = org.telegram.ui.Components.Paint.Views.EntityView.this;
            r4 = r19.getRawY();
            r2.previousLocationY = r4;
            r2 = org.telegram.ui.Components.Paint.Views.EntityView.this;
            r2.hasReleased = r3;
            r2 = 1;
            goto L_0x0142;
        L_0x0140:
            r2 = r17;
        L_0x0142:
            goto L_0x0145;
        L_0x0143:
            r2 = r17;
        L_0x0145:
            r1 = r0.currentHandle;
            r3 = 3;
            if (r1 != r3) goto L_0x0156;
        L_0x014a:
            r1 = org.telegram.ui.Components.Paint.Views.EntityView.this;
            r1 = r1.gestureDetector;
            r3 = r19;
            r1.onTouchEvent(r3);
            goto L_0x0158;
        L_0x0156:
            r3 = r19;
        L_0x0158:
            return r2;
            */
            throw new UnsupportedOperationException("Method not decompiled: org.telegram.ui.Components.Paint.Views.EntityView.SelectionView.onTouchEvent(android.view.MotionEvent):boolean");
        }
    }

    public EntityView(Context context, Point pos) {
        super(context);
        this.position = pos;
        this.gestureDetector = new GestureDetector(context, new SimpleOnGestureListener() {
            public void onLongPress(MotionEvent e) {
                if (!(EntityView.this.hasPanned || EntityView.this.hasTransformed)) {
                    if (!EntityView.this.hasReleased) {
                        EntityView.this.recognizedLongPress = true;
                        if (EntityView.this.delegate != null) {
                            EntityView.this.performHapticFeedback(0);
                            EntityView.this.delegate.onEntityLongClicked(EntityView.this);
                        }
                    }
                }
            }
        });
    }

    public UUID getUUID() {
        return this.uuid;
    }

    public Point getPosition() {
        return this.position;
    }

    public void setPosition(Point value) {
        this.position = value;
        updatePosition();
    }

    public float getScale() {
        return getScaleX();
    }

    public void setScale(float scale) {
        setScaleX(scale);
        setScaleY(scale);
    }

    public void setDelegate(EntityViewDelegate entityViewDelegate) {
        this.delegate = entityViewDelegate;
    }

    public void setOffset(int x, int y) {
        this.offsetX = x;
        this.offsetY = y;
    }

    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return this.delegate.allowInteraction(this);
    }

    private boolean onTouchMove(float x, float y) {
        float scale = ((View) getParent()).getScaleX();
        Point translation = new Point((x - this.previousLocationX) / scale, (y - this.previousLocationY) / scale);
        if (((float) Math.hypot((double) translation.x, (double) translation.y)) <= (this.hasPanned ? 6.0f : 16.0f)) {
            return false;
        }
        pan(translation);
        this.previousLocationX = x;
        this.previousLocationY = y;
        this.hasPanned = true;
        return true;
    }

    private void onTouchUp() {
        if (!(this.recognizedLongPress || this.hasPanned || this.hasTransformed || this.announcedSelection || this.delegate == null)) {
            this.delegate.onEntitySelected(this);
        }
        this.recognizedLongPress = false;
        this.hasPanned = false;
        this.hasTransformed = false;
        this.hasReleased = true;
        this.announcedSelection = false;
    }

    public boolean onTouchEvent(MotionEvent event) {
        if (event.getPointerCount() <= 1) {
            if (this.delegate.allowInteraction(this)) {
                float x = event.getRawX();
                float y = event.getRawY();
                boolean handled = false;
                switch (event.getActionMasked()) {
                    case 0:
                    case 5:
                        if (!(isSelected() || this.delegate == null)) {
                            this.delegate.onEntitySelected(this);
                            this.announcedSelection = true;
                        }
                        this.previousLocationX = x;
                        this.previousLocationY = y;
                        handled = true;
                        this.hasReleased = false;
                        break;
                    case 1:
                    case 3:
                    case 6:
                        onTouchUp();
                        handled = true;
                        break;
                    case 2:
                        handled = onTouchMove(x, y);
                        break;
                    default:
                        break;
                }
                this.gestureDetector.onTouchEvent(event);
                return handled;
            }
        }
        return false;
    }

    public void pan(Point translation) {
        Point point = this.position;
        point.x += translation.x;
        point = this.position;
        point.y += translation.y;
        updatePosition();
    }

    protected void updatePosition() {
        float halfHeight = ((float) getHeight()) / 2.0f;
        setX(this.position.x - (((float) getWidth()) / 2.0f));
        setY(this.position.y - halfHeight);
        updateSelectionView();
    }

    public void scale(float scale) {
        setScale(Math.max(getScale() * scale, 0.1f));
        updateSelectionView();
    }

    public void rotate(float angle) {
        setRotation(angle);
        updateSelectionView();
    }

    protected Rect getSelectionBounds() {
        return new Rect(0.0f, 0.0f, 0.0f, 0.0f);
    }

    public boolean isSelected() {
        return this.selectionView != null;
    }

    protected SelectionView createSelectionView() {
        return null;
    }

    public void updateSelectionView() {
        if (this.selectionView != null) {
            this.selectionView.updatePosition();
        }
    }

    public void select(ViewGroup selectionContainer) {
        SelectionView selectionView = createSelectionView();
        this.selectionView = selectionView;
        selectionContainer.addView(selectionView);
        selectionView.updatePosition();
    }

    public void deselect() {
        if (this.selectionView != null) {
            if (this.selectionView.getParent() != null) {
                ((ViewGroup) this.selectionView.getParent()).removeView(this.selectionView);
            }
            this.selectionView = null;
        }
    }

    public void setSelectionVisibility(boolean visible) {
        if (this.selectionView != null) {
            this.selectionView.setVisibility(visible ? 0 : 8);
        }
    }
}
