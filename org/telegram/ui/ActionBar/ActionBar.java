package org.telegram.ui.ActionBar;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint.FontMetricsInt;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffColorFilter;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build.VERSION;
import android.text.TextPaint;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import java.util.ArrayList;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.BuildVars;
import org.telegram.messenger.LocaleController;
import org.telegram.ui.Components.FireworksEffect;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.ui.Components.SnowflakesEffect;

public class ActionBar extends FrameLayout {
    public ActionBarMenuOnItemClick actionBarMenuOnItemClick;
    private ActionBarMenu actionMode;
    private AnimatorSet actionModeAnimation;
    private View actionModeTop;
    private boolean actionModeVisible;
    private boolean addToContainer;
    private boolean allowOverlayTitle;
    private ImageView backButtonImageView;
    private boolean castShadows;
    private int extraHeight;
    private FireworksEffect fireworksEffect;
    private FontMetricsInt fontMetricsInt;
    private boolean interceptTouches;
    private boolean isBackOverlayVisible;
    protected boolean isSearchFieldVisible;
    protected int itemsActionModeBackgroundColor;
    protected int itemsActionModeColor;
    protected int itemsBackgroundColor;
    protected int itemsColor;
    private CharSequence lastSubtitle;
    private CharSequence lastTitle;
    private boolean manualStart;
    private ActionBarMenu menu;
    private boolean occupyStatusBar;
    protected BaseFragment parentFragment;
    private Rect rect;
    private SnowflakesEffect snowflakesEffect;
    private SimpleTextView subtitleTextView;
    private boolean supportsHolidayImage;
    private Runnable titleActionRunnable;
    private boolean titleOverlayShown;
    private int titleRightMargin;
    private SimpleTextView titleTextView;

    public static class ActionBarMenuOnItemClick {
        public void onItemClick(int id) {
        }

        public boolean canOpenMenu() {
            return true;
        }
    }

    protected void onLayout(boolean r1, int r2, int r3, int r4, int r5) {
        /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.DecodeException: Load method exception in method: org.telegram.ui.ActionBar.ActionBar.onLayout(boolean, int, int, int, int):void
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
        r0 = r16;
        r3 = r0.occupyStatusBar;
        r4 = 0;
        if (r3 == 0) goto L_0x000a;
    L_0x0007:
        r3 = org.telegram.messenger.AndroidUtilities.statusBarHeight;
        goto L_0x000b;
    L_0x000a:
        r3 = r4;
    L_0x000b:
        r5 = r0.backButtonImageView;
        r6 = 8;
        if (r5 == 0) goto L_0x003b;
    L_0x0011:
        r5 = r0.backButtonImageView;
        r5 = r5.getVisibility();
        if (r5 == r6) goto L_0x003b;
    L_0x0019:
        r5 = r0.backButtonImageView;
        r7 = r0.backButtonImageView;
        r7 = r7.getMeasuredWidth();
        r8 = r0.backButtonImageView;
        r8 = r8.getMeasuredHeight();
        r8 = r8 + r3;
        r5.layout(r4, r3, r7, r8);
        r5 = org.telegram.messenger.AndroidUtilities.isTablet();
        if (r5 == 0) goto L_0x0034;
    L_0x0031:
        r5 = 1117782016; // 0x42a00000 float:80.0 double:5.522576936E-315;
        goto L_0x0036;
    L_0x0034:
        r5 = 1116733440; // 0x42900000 float:72.0 double:5.517396283E-315;
    L_0x0036:
        r5 = org.telegram.messenger.AndroidUtilities.dp(r5);
        goto L_0x004a;
    L_0x003b:
        r5 = org.telegram.messenger.AndroidUtilities.isTablet();
        if (r5 == 0) goto L_0x0044;
    L_0x0041:
        r5 = 1104150528; // 0x41d00000 float:26.0 double:5.455228437E-315;
        goto L_0x0046;
    L_0x0044:
        r5 = 1099956224; // 0x41900000 float:18.0 double:5.43450582E-315;
    L_0x0046:
        r5 = org.telegram.messenger.AndroidUtilities.dp(r5);
    L_0x004a:
        r7 = r0.menu;
        if (r7 == 0) goto L_0x0086;
    L_0x004e:
        r7 = r0.menu;
        r7 = r7.getVisibility();
        if (r7 == r6) goto L_0x0086;
    L_0x0056:
        r7 = r0.isSearchFieldVisible;
        if (r7 == 0) goto L_0x006a;
    L_0x005a:
        r7 = org.telegram.messenger.AndroidUtilities.isTablet();
        if (r7 == 0) goto L_0x0063;
    L_0x0060:
        r7 = 1116995584; // 0x42940000 float:74.0 double:5.518691446E-315;
        goto L_0x0065;
    L_0x0063:
        r7 = 1115947008; // 0x42840000 float:66.0 double:5.51351079E-315;
    L_0x0065:
        r7 = org.telegram.messenger.AndroidUtilities.dp(r7);
        goto L_0x0073;
    L_0x006a:
        r7 = r20 - r18;
        r8 = r0.menu;
        r8 = r8.getMeasuredWidth();
        r7 = r7 - r8;
    L_0x0073:
        r8 = r0.menu;
        r9 = r0.menu;
        r9 = r9.getMeasuredWidth();
        r9 = r9 + r7;
        r10 = r0.menu;
        r10 = r10.getMeasuredHeight();
        r10 = r10 + r3;
        r8.layout(r7, r3, r9, r10);
    L_0x0086:
        r7 = r0.titleTextView;
        r8 = 2;
        if (r7 == 0) goto L_0x00ec;
    L_0x008b:
        r7 = r0.titleTextView;
        r7 = r7.getVisibility();
        if (r7 == r6) goto L_0x00ec;
    L_0x0093:
        r7 = r0.subtitleTextView;
        if (r7 == 0) goto L_0x00c9;
    L_0x0097:
        r7 = r0.subtitleTextView;
        r7 = r7.getVisibility();
        if (r7 == r6) goto L_0x00c9;
    L_0x009f:
        r7 = getCurrentActionBarHeight();
        r7 = r7 / r8;
        r9 = r0.titleTextView;
        r9 = r9.getTextHeight();
        r7 = r7 - r9;
        r7 = r7 / r8;
        r9 = org.telegram.messenger.AndroidUtilities.isTablet();
        if (r9 != 0) goto L_0x00c1;
    L_0x00b2:
        r9 = r16.getResources();
        r9 = r9.getConfiguration();
        r9 = r9.orientation;
        if (r9 != r8) goto L_0x00c1;
    L_0x00be:
        r9 = 1073741824; // 0x40000000 float:2.0 double:5.304989477E-315;
        goto L_0x00c3;
    L_0x00c1:
        r9 = 1077936128; // 0x40400000 float:3.0 double:5.325712093E-315;
    L_0x00c3:
        r9 = org.telegram.messenger.AndroidUtilities.dp(r9);
        r7 = r7 + r9;
        goto L_0x00d5;
    L_0x00c9:
        r7 = getCurrentActionBarHeight();
        r9 = r0.titleTextView;
        r9 = r9.getTextHeight();
        r7 = r7 - r9;
        r7 = r7 / r8;
    L_0x00d5:
        r9 = r0.titleTextView;
        r10 = r3 + r7;
        r11 = r0.titleTextView;
        r11 = r11.getMeasuredWidth();
        r11 = r11 + r5;
        r12 = r3 + r7;
        r13 = r0.titleTextView;
        r13 = r13.getTextHeight();
        r12 = r12 + r13;
        r9.layout(r5, r10, r11, r12);
    L_0x00ec:
        r7 = r0.subtitleTextView;
        if (r7 == 0) goto L_0x0139;
    L_0x00f0:
        r7 = r0.subtitleTextView;
        r7 = r7.getVisibility();
        if (r7 == r6) goto L_0x0139;
    L_0x00f8:
        r7 = getCurrentActionBarHeight();
        r7 = r7 / r8;
        r9 = getCurrentActionBarHeight();
        r9 = r9 / r8;
        r10 = r0.subtitleTextView;
        r10 = r10.getTextHeight();
        r9 = r9 - r10;
        r9 = r9 / r8;
        r7 = r7 + r9;
        r9 = org.telegram.messenger.AndroidUtilities.isTablet();
        if (r9 != 0) goto L_0x011b;
    L_0x0111:
        r9 = r16.getResources();
        r9 = r9.getConfiguration();
        r9 = r9.orientation;
    L_0x011b:
        r9 = 1065353216; // 0x3f800000 float:1.0 double:5.263544247E-315;
        r9 = org.telegram.messenger.AndroidUtilities.dp(r9);
        r7 = r7 - r9;
        r9 = r0.subtitleTextView;
        r10 = r3 + r7;
        r11 = r0.subtitleTextView;
        r11 = r11.getMeasuredWidth();
        r11 = r11 + r5;
        r12 = r3 + r7;
        r13 = r0.subtitleTextView;
        r13 = r13.getTextHeight();
        r12 = r12 + r13;
        r9.layout(r5, r10, r11, r12);
    L_0x0139:
        r7 = r16.getChildCount();
        if (r4 >= r7) goto L_0x01d0;
    L_0x0140:
        r9 = r0.getChildAt(r4);
        r10 = r9.getVisibility();
        if (r10 == r6) goto L_0x01c5;
    L_0x014a:
        r10 = r0.titleTextView;
        if (r9 == r10) goto L_0x01c5;
    L_0x014e:
        r10 = r0.subtitleTextView;
        if (r9 == r10) goto L_0x01c5;
    L_0x0152:
        r10 = r0.menu;
        if (r9 == r10) goto L_0x01c5;
    L_0x0156:
        r10 = r0.backButtonImageView;
        if (r9 != r10) goto L_0x015e;
    L_0x015b:
        r14 = r3;
        goto L_0x01c6;
    L_0x015e:
        r10 = r9.getLayoutParams();
        r10 = (android.widget.FrameLayout.LayoutParams) r10;
        r11 = r9.getMeasuredWidth();
        r12 = r9.getMeasuredHeight();
        r13 = r10.gravity;
        r6 = -1;
        if (r13 != r6) goto L_0x0173;
    L_0x0171:
        r13 = 51;
    L_0x0173:
        r6 = r13 & 7;
        r8 = r13 & 112;
        r0 = r6 & 7;
        r14 = r3;
        r3 = 1;
        if (r0 == r3) goto L_0x0189;
    L_0x017d:
        r3 = 5;
        if (r0 == r3) goto L_0x0183;
    L_0x0180:
        r0 = r10.leftMargin;
        goto L_0x0195;
    L_0x0183:
        r0 = r20 - r11;
        r3 = r10.rightMargin;
        r0 = r0 - r3;
        goto L_0x0195;
    L_0x0189:
        r0 = r20 - r18;
        r0 = r0 - r11;
        r3 = 2;
        r0 = r0 / r3;
        r3 = r10.leftMargin;
        r0 = r0 + r3;
        r3 = r10.rightMargin;
        r0 = r0 - r3;
        r3 = 16;
        if (r8 == r3) goto L_0x01af;
        r3 = 48;
        if (r8 == r3) goto L_0x01ac;
        r3 = 80;
        if (r8 == r3) goto L_0x01a5;
        r3 = r10.topMargin;
        goto L_0x01bc;
        r3 = r21 - r19;
        r3 = r3 - r12;
        r1 = r10.bottomMargin;
        r3 = r3 - r1;
        goto L_0x01bc;
        r3 = r10.topMargin;
        goto L_0x01bc;
        r1 = r21 - r19;
        r1 = r1 - r12;
        r3 = 2;
        r1 = r1 / r3;
        r3 = r10.topMargin;
        r1 = r1 + r3;
        r3 = r10.bottomMargin;
        r3 = r1 - r3;
        r1 = r3;
        r3 = r0 + r11;
        r2 = r1 + r12;
        r9.layout(r0, r1, r3, r2);
        goto L_0x01c6;
    L_0x01c5:
        r14 = r3;
    L_0x01c6:
        r4 = r4 + 1;
        r3 = r14;
        r0 = r16;
        r6 = 8;
        r8 = 2;
        goto L_0x013e;
    L_0x01d0:
        r14 = r3;
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.telegram.ui.ActionBar.ActionBar.onLayout(boolean, int, int, int, int):void");
    }

    public ActionBar(Context context) {
        super(context);
        this.occupyStatusBar = VERSION.SDK_INT >= 21;
        this.addToContainer = true;
        this.interceptTouches = true;
        this.castShadows = true;
        setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                if (ActionBar.this.titleActionRunnable != null) {
                    ActionBar.this.titleActionRunnable.run();
                }
            }
        });
    }

    private void createBackButtonImage() {
        if (this.backButtonImageView == null) {
            this.backButtonImageView = new ImageView(getContext());
            this.backButtonImageView.setScaleType(ScaleType.CENTER);
            this.backButtonImageView.setBackgroundDrawable(Theme.createSelectorDrawable(this.itemsBackgroundColor));
            if (this.itemsColor != 0) {
                this.backButtonImageView.setColorFilter(new PorterDuffColorFilter(this.itemsColor, Mode.MULTIPLY));
            }
            this.backButtonImageView.setPadding(AndroidUtilities.dp(1.0f), 0, 0, 0);
            addView(this.backButtonImageView, LayoutHelper.createFrame(54, 54, 51));
            this.backButtonImageView.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    if (ActionBar.this.actionModeVisible || !ActionBar.this.isSearchFieldVisible) {
                        if (ActionBar.this.actionBarMenuOnItemClick != null) {
                            ActionBar.this.actionBarMenuOnItemClick.onItemClick(-1);
                        }
                        return;
                    }
                    ActionBar.this.closeSearchField();
                }
            });
        }
    }

    public void setBackButtonDrawable(Drawable drawable) {
        if (this.backButtonImageView == null) {
            createBackButtonImage();
        }
        this.backButtonImageView.setVisibility(drawable == null ? 8 : 0);
        this.backButtonImageView.setImageDrawable(drawable);
        if (drawable instanceof BackDrawable) {
            BackDrawable backDrawable = (BackDrawable) drawable;
            backDrawable.setRotation(isActionModeShowed() ? 1.0f : 0.0f, false);
            backDrawable.setRotatedColor(this.itemsActionModeColor);
            backDrawable.setColor(this.itemsColor);
        }
    }

    public void setSupportsHolidayImage(boolean value) {
        this.supportsHolidayImage = value;
        if (this.supportsHolidayImage) {
            this.fontMetricsInt = new FontMetricsInt();
            this.rect = new Rect();
        }
        invalidate();
    }

    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (this.supportsHolidayImage && !this.titleOverlayShown && !LocaleController.isRTL && ev.getAction() == 0) {
            Drawable drawable = Theme.getCurrentHolidayDrawable();
            if (drawable != null && drawable.getBounds().contains((int) ev.getX(), (int) ev.getY())) {
                this.manualStart = true;
                if (this.snowflakesEffect == null) {
                    this.fireworksEffect = null;
                    this.snowflakesEffect = new SnowflakesEffect();
                    this.titleTextView.invalidate();
                    invalidate();
                } else if (BuildVars.DEBUG_PRIVATE_VERSION) {
                    this.snowflakesEffect = null;
                    this.fireworksEffect = new FireworksEffect();
                    this.titleTextView.invalidate();
                    invalidate();
                }
            }
        }
        return super.onInterceptTouchEvent(ev);
    }

    protected boolean drawChild(Canvas canvas, View child, long drawingTime) {
        boolean result = super.drawChild(canvas, child, drawingTime);
        if (this.supportsHolidayImage && !this.titleOverlayShown && !LocaleController.isRTL && child == this.titleTextView) {
            Drawable drawable = Theme.getCurrentHolidayDrawable();
            if (drawable != null) {
                TextPaint textPaint = this.titleTextView.getTextPaint();
                textPaint.getFontMetricsInt(this.fontMetricsInt);
                textPaint.getTextBounds((String) this.titleTextView.getText(), 0, 1, this.rect);
                int x = (this.titleTextView.getTextStartX() + Theme.getCurrentHolidayDrawableXOffset()) + ((this.rect.width() - (drawable.getIntrinsicWidth() + Theme.getCurrentHolidayDrawableXOffset())) / 2);
                int y = (this.titleTextView.getTextStartY() + Theme.getCurrentHolidayDrawableYOffset()) + ((int) Math.ceil((double) (((float) (this.titleTextView.getTextHeight() - this.rect.height())) / 2.0f)));
                drawable.setBounds(x, y - drawable.getIntrinsicHeight(), drawable.getIntrinsicWidth() + x, y);
                drawable.draw(canvas);
                if (Theme.canStartHolidayAnimation()) {
                    if (this.snowflakesEffect == null) {
                        this.snowflakesEffect = new SnowflakesEffect();
                    }
                } else if (!(this.manualStart || this.snowflakesEffect == null)) {
                    this.snowflakesEffect = null;
                }
                if (this.snowflakesEffect != null) {
                    this.snowflakesEffect.onDraw(this, canvas);
                } else if (this.fireworksEffect != null) {
                    this.fireworksEffect.onDraw(this, canvas);
                }
            }
        }
        return result;
    }

    public void setBackButtonImage(int resource) {
        if (this.backButtonImageView == null) {
            createBackButtonImage();
        }
        this.backButtonImageView.setVisibility(resource == 0 ? 8 : 0);
        this.backButtonImageView.setImageResource(resource);
    }

    private void createSubtitleTextView() {
        if (this.subtitleTextView == null) {
            this.subtitleTextView = new SimpleTextView(getContext());
            this.subtitleTextView.setGravity(3);
            this.subtitleTextView.setVisibility(8);
            this.subtitleTextView.setTextColor(Theme.getColor(Theme.key_actionBarDefaultSubtitle));
            addView(this.subtitleTextView, 0, LayoutHelper.createFrame(-2, -2, 51));
        }
    }

    public void setAddToContainer(boolean value) {
        this.addToContainer = value;
    }

    public boolean getAddToContainer() {
        return this.addToContainer;
    }

    public void setSubtitle(CharSequence value) {
        if (value != null && this.subtitleTextView == null) {
            createSubtitleTextView();
        }
        if (this.subtitleTextView != null) {
            this.lastSubtitle = value;
            SimpleTextView simpleTextView = this.subtitleTextView;
            int i = (TextUtils.isEmpty(value) || this.isSearchFieldVisible) ? 8 : 0;
            simpleTextView.setVisibility(i);
            this.subtitleTextView.setText(value);
        }
    }

    private void createTitleTextView() {
        if (this.titleTextView == null) {
            this.titleTextView = new SimpleTextView(getContext());
            this.titleTextView.setGravity(3);
            this.titleTextView.setTextColor(Theme.getColor(Theme.key_actionBarDefaultTitle));
            this.titleTextView.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
            addView(this.titleTextView, 0, LayoutHelper.createFrame(-2, -2, 51));
        }
    }

    public void setTitleRightMargin(int value) {
        this.titleRightMargin = value;
    }

    public void setTitle(CharSequence value) {
        if (value != null && this.titleTextView == null) {
            createTitleTextView();
        }
        if (this.titleTextView != null) {
            this.lastTitle = value;
            SimpleTextView simpleTextView = this.titleTextView;
            int i = (value == null || this.isSearchFieldVisible) ? 4 : 0;
            simpleTextView.setVisibility(i);
            this.titleTextView.setText(value);
        }
    }

    public void setTitleColor(int color) {
        if (this.titleTextView == null) {
            createTitleTextView();
        }
        this.titleTextView.setTextColor(color);
    }

    public void setSubtitleColor(int color) {
        if (this.subtitleTextView == null) {
            createSubtitleTextView();
        }
        this.subtitleTextView.setTextColor(color);
    }

    public void setPopupItemsColor(int color) {
        if (this.menu != null) {
            this.menu.setPopupItemsColor(color);
        }
    }

    public void setPopupBackgroundColor(int color) {
        if (this.menu != null) {
            this.menu.redrawPopup(color);
        }
    }

    public SimpleTextView getSubtitleTextView() {
        return this.subtitleTextView;
    }

    public SimpleTextView getTitleTextView() {
        return this.titleTextView;
    }

    public String getTitle() {
        if (this.titleTextView == null) {
            return null;
        }
        return this.titleTextView.getText().toString();
    }

    public String getSubtitle() {
        if (this.subtitleTextView == null) {
            return null;
        }
        return this.subtitleTextView.getText().toString();
    }

    public ActionBarMenu createMenu() {
        if (this.menu != null) {
            return this.menu;
        }
        this.menu = new ActionBarMenu(getContext(), this);
        addView(this.menu, 0, LayoutHelper.createFrame(-2, -1, 5));
        return this.menu;
    }

    public void setActionBarMenuOnItemClick(ActionBarMenuOnItemClick listener) {
        this.actionBarMenuOnItemClick = listener;
    }

    public ActionBarMenuOnItemClick getActionBarMenuOnItemClick() {
        return this.actionBarMenuOnItemClick;
    }

    public View getBackButton() {
        return this.backButtonImageView;
    }

    public ActionBarMenu createActionMode() {
        if (this.actionMode != null) {
            return this.actionMode;
        }
        this.actionMode = new ActionBarMenu(getContext(), this);
        this.actionMode.isActionMode = true;
        this.actionMode.setBackgroundColor(Theme.getColor(Theme.key_actionBarActionModeDefault));
        addView(this.actionMode, indexOfChild(this.backButtonImageView));
        this.actionMode.setPadding(0, this.occupyStatusBar ? AndroidUtilities.statusBarHeight : 0, 0, 0);
        LayoutParams layoutParams = (LayoutParams) this.actionMode.getLayoutParams();
        layoutParams.height = -1;
        layoutParams.width = -1;
        layoutParams.gravity = 5;
        this.actionMode.setLayoutParams(layoutParams);
        this.actionMode.setVisibility(4);
        if (this.occupyStatusBar && this.actionModeTop == null) {
            this.actionModeTop = new View(getContext());
            this.actionModeTop.setBackgroundColor(Theme.getColor(Theme.key_actionBarActionModeDefaultTop));
            addView(this.actionModeTop);
            layoutParams = (LayoutParams) this.actionModeTop.getLayoutParams();
            layoutParams.height = AndroidUtilities.statusBarHeight;
            layoutParams.width = -1;
            layoutParams.gravity = 51;
            this.actionModeTop.setLayoutParams(layoutParams);
            this.actionModeTop.setVisibility(4);
        }
        return this.actionMode;
    }

    public void showActionMode() {
        if (this.actionMode != null) {
            if (!this.actionModeVisible) {
                this.actionModeVisible = true;
                ArrayList<Animator> animators = new ArrayList();
                animators.add(ObjectAnimator.ofFloat(this.actionMode, "alpha", new float[]{0.0f, 1.0f}));
                if (this.occupyStatusBar && this.actionModeTop != null) {
                    animators.add(ObjectAnimator.ofFloat(this.actionModeTop, "alpha", new float[]{0.0f, 1.0f}));
                }
                if (this.actionModeAnimation != null) {
                    this.actionModeAnimation.cancel();
                }
                this.actionModeAnimation = new AnimatorSet();
                this.actionModeAnimation.playTogether(animators);
                this.actionModeAnimation.setDuration(200);
                this.actionModeAnimation.addListener(new AnimatorListenerAdapter() {
                    public void onAnimationStart(Animator animation) {
                        ActionBar.this.actionMode.setVisibility(0);
                        if (ActionBar.this.occupyStatusBar && ActionBar.this.actionModeTop != null) {
                            ActionBar.this.actionModeTop.setVisibility(0);
                        }
                    }

                    public void onAnimationEnd(Animator animation) {
                        if (ActionBar.this.actionModeAnimation != null && ActionBar.this.actionModeAnimation.equals(animation)) {
                            ActionBar.this.actionModeAnimation = null;
                            if (ActionBar.this.titleTextView != null) {
                                ActionBar.this.titleTextView.setVisibility(4);
                            }
                            if (!(ActionBar.this.subtitleTextView == null || TextUtils.isEmpty(ActionBar.this.subtitleTextView.getText()))) {
                                ActionBar.this.subtitleTextView.setVisibility(4);
                            }
                            if (ActionBar.this.menu != null) {
                                ActionBar.this.menu.setVisibility(4);
                            }
                        }
                    }

                    public void onAnimationCancel(Animator animation) {
                        if (ActionBar.this.actionModeAnimation != null && ActionBar.this.actionModeAnimation.equals(animation)) {
                            ActionBar.this.actionModeAnimation = null;
                        }
                    }
                });
                this.actionModeAnimation.start();
                if (this.backButtonImageView != null) {
                    Drawable drawable = this.backButtonImageView.getDrawable();
                    if (drawable instanceof BackDrawable) {
                        ((BackDrawable) drawable).setRotation(1.0f, true);
                    }
                    this.backButtonImageView.setBackgroundDrawable(Theme.createSelectorDrawable(this.itemsActionModeBackgroundColor));
                }
            }
        }
    }

    public void hideActionMode() {
        if (this.actionMode != null) {
            if (this.actionModeVisible) {
                this.actionModeVisible = false;
                ArrayList<Animator> animators = new ArrayList();
                animators.add(ObjectAnimator.ofFloat(this.actionMode, "alpha", new float[]{0.0f}));
                if (this.occupyStatusBar && this.actionModeTop != null) {
                    animators.add(ObjectAnimator.ofFloat(this.actionModeTop, "alpha", new float[]{0.0f}));
                }
                if (this.actionModeAnimation != null) {
                    this.actionModeAnimation.cancel();
                }
                this.actionModeAnimation = new AnimatorSet();
                this.actionModeAnimation.playTogether(animators);
                this.actionModeAnimation.setDuration(200);
                this.actionModeAnimation.addListener(new AnimatorListenerAdapter() {
                    public void onAnimationEnd(Animator animation) {
                        if (ActionBar.this.actionModeAnimation != null && ActionBar.this.actionModeAnimation.equals(animation)) {
                            ActionBar.this.actionModeAnimation = null;
                            ActionBar.this.actionMode.setVisibility(4);
                            if (ActionBar.this.occupyStatusBar && ActionBar.this.actionModeTop != null) {
                                ActionBar.this.actionModeTop.setVisibility(4);
                            }
                        }
                    }

                    public void onAnimationCancel(Animator animation) {
                        if (ActionBar.this.actionModeAnimation != null && ActionBar.this.actionModeAnimation.equals(animation)) {
                            ActionBar.this.actionModeAnimation = null;
                        }
                    }
                });
                this.actionModeAnimation.start();
                if (this.titleTextView != null) {
                    this.titleTextView.setVisibility(0);
                }
                if (!(this.subtitleTextView == null || TextUtils.isEmpty(this.subtitleTextView.getText()))) {
                    this.subtitleTextView.setVisibility(0);
                }
                if (this.menu != null) {
                    this.menu.setVisibility(0);
                }
                if (this.backButtonImageView != null) {
                    Drawable drawable = this.backButtonImageView.getDrawable();
                    if (drawable instanceof BackDrawable) {
                        ((BackDrawable) drawable).setRotation(0.0f, true);
                    }
                    this.backButtonImageView.setBackgroundDrawable(Theme.createSelectorDrawable(this.itemsBackgroundColor));
                }
            }
        }
    }

    public void showActionModeTop() {
        if (this.occupyStatusBar && this.actionModeTop == null) {
            this.actionModeTop = new View(getContext());
            this.actionModeTop.setBackgroundColor(Theme.getColor(Theme.key_actionBarActionModeDefaultTop));
            addView(this.actionModeTop);
            LayoutParams layoutParams = (LayoutParams) this.actionModeTop.getLayoutParams();
            layoutParams.height = AndroidUtilities.statusBarHeight;
            layoutParams.width = -1;
            layoutParams.gravity = 51;
            this.actionModeTop.setLayoutParams(layoutParams);
        }
    }

    public void setActionModeTopColor(int color) {
        if (this.actionModeTop != null) {
            this.actionModeTop.setBackgroundColor(color);
        }
    }

    public void setSearchTextColor(int color, boolean placeholder) {
        if (this.menu != null) {
            this.menu.setSearchTextColor(color, placeholder);
        }
    }

    public void setActionModeColor(int color) {
        if (this.actionMode != null) {
            this.actionMode.setBackgroundColor(color);
        }
    }

    public boolean isActionModeShowed() {
        return this.actionMode != null && this.actionModeVisible;
    }

    protected void onSearchFieldVisibilityChanged(boolean visible) {
        this.isSearchFieldVisible = visible;
        int i = 0;
        if (this.titleTextView != null) {
            this.titleTextView.setVisibility(visible ? 4 : 0);
        }
        if (!(this.subtitleTextView == null || TextUtils.isEmpty(this.subtitleTextView.getText()))) {
            SimpleTextView simpleTextView = this.subtitleTextView;
            if (visible) {
                i = 4;
            }
            simpleTextView.setVisibility(i);
        }
        Drawable drawable = this.backButtonImageView.getDrawable();
        if (drawable != null && (drawable instanceof MenuDrawable)) {
            ((MenuDrawable) drawable).setRotation(visible ? 1.0f : 0.0f, true);
        }
    }

    public void setInterceptTouches(boolean value) {
        this.interceptTouches = value;
    }

    public void setExtraHeight(int value) {
        this.extraHeight = value;
    }

    public void closeSearchField() {
        closeSearchField(true);
    }

    public void closeSearchField(boolean closeKeyboard) {
        if (this.isSearchFieldVisible) {
            if (this.menu != null) {
                this.menu.closeSearchField(closeKeyboard);
            }
        }
    }

    public void openSearchField(String text) {
        if (this.menu != null) {
            if (text != null) {
                this.menu.openSearchField(this.isSearchFieldVisible ^ 1, text);
            }
        }
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int textLeft;
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        int actionBarHeight = getCurrentActionBarHeight();
        int actionBarHeightSpec = MeasureSpec.makeMeasureSpec(actionBarHeight, 1073741824);
        int i = 0;
        setMeasuredDimension(width, ((this.occupyStatusBar ? AndroidUtilities.statusBarHeight : 0) + actionBarHeight) + r6.extraHeight);
        if (r6.backButtonImageView == null || r6.backButtonImageView.getVisibility() == 8) {
            textLeft = AndroidUtilities.dp(AndroidUtilities.isTablet() ? 26.0f : 18.0f);
        } else {
            r6.backButtonImageView.measure(MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(54.0f), 1073741824), actionBarHeightSpec);
            textLeft = AndroidUtilities.dp(AndroidUtilities.isTablet() ? 1117782016 : 1116733440);
        }
        int textLeft2 = textLeft;
        if (!(r6.menu == null || r6.menu.getVisibility() == 8)) {
            if (r6.isSearchFieldVisible) {
                textLeft = MeasureSpec.makeMeasureSpec(width - AndroidUtilities.dp(AndroidUtilities.isTablet() ? 74.0f : 66.0f), 1073741824);
            } else {
                textLeft = MeasureSpec.makeMeasureSpec(width, Integer.MIN_VALUE);
            }
            r6.menu.measure(textLeft, actionBarHeightSpec);
        }
        if (!((r6.titleTextView == null || r6.titleTextView.getVisibility() == 8) && (r6.subtitleTextView == null || r6.subtitleTextView.getVisibility() == 8))) {
            SimpleTextView simpleTextView;
            textLeft = (((width - (r6.menu != null ? r6.menu.getMeasuredWidth() : 0)) - AndroidUtilities.dp(16.0f)) - textLeft2) - r6.titleRightMargin;
            if (!(r6.titleTextView == null || r6.titleTextView.getVisibility() == 8)) {
                simpleTextView = r6.titleTextView;
                int i2 = (AndroidUtilities.isTablet() || getResources().getConfiguration().orientation != 2) ? 20 : 18;
                simpleTextView.setTextSize(i2);
                r6.titleTextView.measure(MeasureSpec.makeMeasureSpec(textLeft, Integer.MIN_VALUE), MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(24.0f), Integer.MIN_VALUE));
            }
            if (!(r6.subtitleTextView == null || r6.subtitleTextView.getVisibility() == 8)) {
                simpleTextView = r6.subtitleTextView;
                int i3 = (AndroidUtilities.isTablet() || getResources().getConfiguration().orientation != 2) ? 16 : 14;
                simpleTextView.setTextSize(i3);
                r6.subtitleTextView.measure(MeasureSpec.makeMeasureSpec(textLeft, Integer.MIN_VALUE), MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(20.0f), Integer.MIN_VALUE));
            }
        }
        int childCount = getChildCount();
        while (true) {
            int i4 = i;
            if (i4 < childCount) {
                View child = getChildAt(i4);
                if (!(child.getVisibility() == 8 || child == r6.titleTextView || child == r6.subtitleTextView || child == r6.menu)) {
                    if (child != r6.backButtonImageView) {
                        measureChildWithMargins(child, widthMeasureSpec, 0, MeasureSpec.makeMeasureSpec(getMeasuredHeight(), 1073741824), null);
                    }
                }
                i = i4 + 1;
            } else {
                return;
            }
        }
    }

    public void onMenuButtonPressed() {
        if (this.menu != null) {
            this.menu.onMenuButtonPressed();
        }
    }

    protected void onPause() {
        if (this.menu != null) {
            this.menu.hideAllPopupMenus();
        }
    }

    public void setAllowOverlayTitle(boolean value) {
        this.allowOverlayTitle = value;
    }

    public void setTitleOverlayText(String title, String subtitle, Runnable action) {
        if (this.allowOverlayTitle) {
            if (this.parentFragment.parentLayout != null) {
                SimpleTextView simpleTextView;
                CharSequence textToSet = title != null ? title : this.lastTitle;
                if (textToSet != null && this.titleTextView == null) {
                    createTitleTextView();
                }
                int i = 0;
                if (this.titleTextView != null) {
                    this.titleOverlayShown = title != null;
                    if (this.supportsHolidayImage) {
                        this.titleTextView.invalidate();
                        invalidate();
                    }
                    simpleTextView = this.titleTextView;
                    int i2 = (textToSet == null || this.isSearchFieldVisible) ? 4 : 0;
                    simpleTextView.setVisibility(i2);
                    this.titleTextView.setText(textToSet);
                }
                textToSet = subtitle != null ? subtitle : this.lastSubtitle;
                if (textToSet != null && this.subtitleTextView == null) {
                    createSubtitleTextView();
                }
                if (this.subtitleTextView != null) {
                    simpleTextView = this.subtitleTextView;
                    if (TextUtils.isEmpty(textToSet) || this.isSearchFieldVisible) {
                        i = 8;
                    }
                    simpleTextView.setVisibility(i);
                    this.subtitleTextView.setText(textToSet);
                }
                this.titleActionRunnable = action;
            }
        }
    }

    public boolean isSearchFieldVisible() {
        return this.isSearchFieldVisible;
    }

    public void setOccupyStatusBar(boolean value) {
        this.occupyStatusBar = value;
        if (this.actionMode != null) {
            this.actionMode.setPadding(0, this.occupyStatusBar ? AndroidUtilities.statusBarHeight : 0, 0, 0);
        }
    }

    public boolean getOccupyStatusBar() {
        return this.occupyStatusBar;
    }

    public void setItemsBackgroundColor(int color, boolean isActionMode) {
        if (isActionMode) {
            this.itemsActionModeBackgroundColor = color;
            if (this.actionModeVisible && this.backButtonImageView != null) {
                this.backButtonImageView.setBackgroundDrawable(Theme.createSelectorDrawable(this.itemsActionModeBackgroundColor));
            }
            if (this.actionMode != null) {
                this.actionMode.updateItemsBackgroundColor();
                return;
            }
            return;
        }
        this.itemsBackgroundColor = color;
        if (this.backButtonImageView != null) {
            this.backButtonImageView.setBackgroundDrawable(Theme.createSelectorDrawable(this.itemsBackgroundColor));
        }
        if (this.menu != null) {
            this.menu.updateItemsBackgroundColor();
        }
    }

    public void setItemsColor(int color, boolean isActionMode) {
        Drawable drawable;
        if (isActionMode) {
            this.itemsActionModeColor = color;
            if (this.actionMode != null) {
                this.actionMode.updateItemsColor();
            }
            if (this.backButtonImageView != null) {
                drawable = this.backButtonImageView.getDrawable();
                if (drawable instanceof BackDrawable) {
                    ((BackDrawable) drawable).setRotatedColor(color);
                }
                return;
            }
            return;
        }
        this.itemsColor = color;
        if (!(this.backButtonImageView == null || this.itemsColor == 0)) {
            this.backButtonImageView.setColorFilter(new PorterDuffColorFilter(this.itemsColor, Mode.MULTIPLY));
            drawable = this.backButtonImageView.getDrawable();
            if (drawable instanceof BackDrawable) {
                ((BackDrawable) drawable).setColor(color);
            }
        }
        if (this.menu != null) {
            this.menu.updateItemsColor();
        }
    }

    public void setCastShadows(boolean value) {
        this.castShadows = value;
    }

    public boolean getCastShadows() {
        return this.castShadows;
    }

    public boolean onTouchEvent(MotionEvent event) {
        if (!super.onTouchEvent(event)) {
            if (!this.interceptTouches) {
                return false;
            }
        }
        return true;
    }

    public static int getCurrentActionBarHeight() {
        if (AndroidUtilities.isTablet()) {
            return AndroidUtilities.dp(64.0f);
        }
        if (AndroidUtilities.displaySize.x > AndroidUtilities.displaySize.y) {
            return AndroidUtilities.dp(48.0f);
        }
        return AndroidUtilities.dp(56.0f);
    }

    public boolean hasOverlappingRendering() {
        return false;
    }
}
