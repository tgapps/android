package org.telegram.ui;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Shader.TileMode;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import java.util.ArrayList;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.ApplicationLoader;
import org.telegram.messenger.NotificationCenter;
import org.telegram.messenger.beta.R;
import org.telegram.ui.ActionBar.ActionBarLayout;
import org.telegram.ui.ActionBar.ActionBarLayout.ActionBarLayoutDelegate;
import org.telegram.ui.ActionBar.BaseFragment;
import org.telegram.ui.ActionBar.DrawerLayoutContainer;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Components.LayoutHelper;

public class ManageSpaceActivity extends Activity implements ActionBarLayoutDelegate {
    private static ArrayList<BaseFragment> layerFragmentsStack = new ArrayList();
    private static ArrayList<BaseFragment> mainFragmentsStack = new ArrayList();
    private ActionBarLayout actionBarLayout;
    protected DrawerLayoutContainer drawerLayoutContainer;
    private boolean finished;
    private ActionBarLayout layersActionBarLayout;

    public void needLayout() {
        /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.DecodeException: Load method exception in method: org.telegram.ui.ManageSpaceActivity.needLayout():void
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
        r0 = org.telegram.messenger.AndroidUtilities.isTablet();
        if (r0 == 0) goto L_0x00ac;
    L_0x0006:
        r0 = r7.layersActionBarLayout;
        r0 = r0.getLayoutParams();
        r0 = (android.widget.RelativeLayout.LayoutParams) r0;
        r1 = org.telegram.messenger.AndroidUtilities.displaySize;
        r1 = r1.x;
        r2 = r0.width;
        r1 = r1 - r2;
        r2 = 2;
        r1 = r1 / r2;
        r0.leftMargin = r1;
        r1 = android.os.Build.VERSION.SDK_INT;
        r3 = 21;
        if (r1 < r3) goto L_0x0022;
    L_0x001f:
        r1 = org.telegram.messenger.AndroidUtilities.statusBarHeight;
        goto L_0x0023;
    L_0x0022:
        r1 = 0;
    L_0x0023:
        r3 = org.telegram.messenger.AndroidUtilities.displaySize;
        r3 = r3.y;
        r4 = r0.height;
        r3 = r3 - r4;
        r3 = r3 - r1;
        r3 = r3 / r2;
        r3 = r3 + r1;
        r0.topMargin = r3;
        r3 = r7.layersActionBarLayout;
        r3.setLayoutParams(r0);
        r3 = org.telegram.messenger.AndroidUtilities.isSmallTablet();
        r4 = -1;
        if (r3 == 0) goto L_0x005b;
    L_0x003b:
        r3 = r7.getResources();
        r3 = r3.getConfiguration();
        r3 = r3.orientation;
        if (r3 != r2) goto L_0x0048;
    L_0x0047:
        goto L_0x005b;
    L_0x0048:
        r2 = r7.actionBarLayout;
        r2 = r2.getLayoutParams();
        r0 = r2;
        r0 = (android.widget.RelativeLayout.LayoutParams) r0;
        r0.width = r4;
        r0.height = r4;
        r2 = r7.actionBarLayout;
        r2.setLayoutParams(r0);
        goto L_0x00ac;
    L_0x005b:
        r3 = org.telegram.messenger.AndroidUtilities.displaySize;
        r3 = r3.x;
        r3 = r3 / 100;
        r3 = r3 * 35;
        r5 = 1134559232; // 0x43a00000 float:320.0 double:5.605467397E-315;
        r6 = org.telegram.messenger.AndroidUtilities.dp(r5);
        if (r3 >= r6) goto L_0x006f;
    L_0x006b:
        r3 = org.telegram.messenger.AndroidUtilities.dp(r5);
    L_0x006f:
        r5 = r7.actionBarLayout;
        r5 = r5.getLayoutParams();
        r0 = r5;
        r0 = (android.widget.RelativeLayout.LayoutParams) r0;
        r0.width = r3;
        r0.height = r4;
        r4 = r7.actionBarLayout;
        r4.setLayoutParams(r0);
        r4 = org.telegram.messenger.AndroidUtilities.isSmallTablet();
        if (r4 == 0) goto L_0x00ab;
    L_0x0087:
        r4 = r7.actionBarLayout;
        r4 = r4.fragmentsStack;
        r4 = r4.size();
        if (r4 != r2) goto L_0x00ab;
        r2 = r7.actionBarLayout;
        r2 = r2.fragmentsStack;
        r4 = 1;
        r2 = r2.get(r4);
        r2 = (org.telegram.ui.ActionBar.BaseFragment) r2;
        r2.onPause();
        r5 = r7.actionBarLayout;
        r5 = r5.fragmentsStack;
        r5.remove(r4);
        r4 = r7.actionBarLayout;
        r4.showLastFragment();
    L_0x00ac:
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.telegram.ui.ManageSpaceActivity.needLayout():void");
    }

    protected void onCreate(Bundle savedInstanceState) {
        ApplicationLoader.postInitApplication();
        boolean z = true;
        requestWindowFeature(1);
        setTheme(R.style.Theme.TMessages);
        getWindow().setBackgroundDrawableResource(R.drawable.transparent);
        super.onCreate(savedInstanceState);
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            AndroidUtilities.statusBarHeight = getResources().getDimensionPixelSize(resourceId);
        }
        Theme.createDialogsResources(this);
        this.actionBarLayout = new ActionBarLayout(this);
        this.drawerLayoutContainer = new DrawerLayoutContainer(this);
        this.drawerLayoutContainer.setAllowOpenDrawer(false, false);
        setContentView(this.drawerLayoutContainer, new LayoutParams(-1, -1));
        if (AndroidUtilities.isTablet()) {
            getWindow().setSoftInputMode(16);
            RelativeLayout launchLayout = new RelativeLayout(this);
            this.drawerLayoutContainer.addView(launchLayout);
            FrameLayout.LayoutParams layoutParams1 = (FrameLayout.LayoutParams) launchLayout.getLayoutParams();
            layoutParams1.width = -1;
            layoutParams1.height = -1;
            launchLayout.setLayoutParams(layoutParams1);
            View backgroundTablet = new View(this);
            BitmapDrawable drawable = (BitmapDrawable) getResources().getDrawable(R.drawable.catstile);
            drawable.setTileModeXY(TileMode.REPEAT, TileMode.REPEAT);
            backgroundTablet.setBackgroundDrawable(drawable);
            launchLayout.addView(backgroundTablet, LayoutHelper.createRelative(-1, -1));
            launchLayout.addView(this.actionBarLayout, LayoutHelper.createRelative(-1, -1));
            FrameLayout shadowTablet = new FrameLayout(this);
            shadowTablet.setBackgroundColor(Theme.ACTION_BAR_PHOTO_VIEWER_COLOR);
            launchLayout.addView(shadowTablet, LayoutHelper.createRelative(-1, -1));
            shadowTablet.setOnTouchListener(new OnTouchListener() {
                public boolean onTouch(View v, MotionEvent event) {
                    if (ManageSpaceActivity.this.actionBarLayout.fragmentsStack.isEmpty() || event.getAction() != 1) {
                        return false;
                    }
                    float x = event.getX();
                    float y = event.getY();
                    int[] location = new int[2];
                    ManageSpaceActivity.this.layersActionBarLayout.getLocationOnScreen(location);
                    int viewX = location[0];
                    int viewY = location[1];
                    if (!ManageSpaceActivity.this.layersActionBarLayout.checkTransitionAnimation()) {
                        if (x <= ((float) viewX) || x >= ((float) (ManageSpaceActivity.this.layersActionBarLayout.getWidth() + viewX)) || y <= ((float) viewY) || y >= ((float) (ManageSpaceActivity.this.layersActionBarLayout.getHeight() + viewY))) {
                            if (!ManageSpaceActivity.this.layersActionBarLayout.fragmentsStack.isEmpty()) {
                                for (int a = 0; a < ManageSpaceActivity.this.layersActionBarLayout.fragmentsStack.size() - 1; a = (a - 1) + 1) {
                                    ManageSpaceActivity.this.layersActionBarLayout.removeFragmentFromStack((BaseFragment) ManageSpaceActivity.this.layersActionBarLayout.fragmentsStack.get(0));
                                }
                                ManageSpaceActivity.this.layersActionBarLayout.closeLastFragment(true);
                            }
                            return true;
                        }
                    }
                    return false;
                }
            });
            shadowTablet.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                }
            });
            this.layersActionBarLayout = new ActionBarLayout(this);
            this.layersActionBarLayout.setRemoveActionBarExtraHeight(true);
            this.layersActionBarLayout.setBackgroundView(shadowTablet);
            this.layersActionBarLayout.setUseAlphaAnimations(true);
            this.layersActionBarLayout.setBackgroundResource(R.drawable.boxshadow);
            launchLayout.addView(this.layersActionBarLayout, LayoutHelper.createRelative(530, 528));
            this.layersActionBarLayout.init(layerFragmentsStack);
            this.layersActionBarLayout.setDelegate(this);
            this.layersActionBarLayout.setDrawerLayoutContainer(this.drawerLayoutContainer);
        } else {
            this.drawerLayoutContainer.addView(this.actionBarLayout, new LayoutParams(-1, -1));
        }
        this.drawerLayoutContainer.setParentActionBarLayout(this.actionBarLayout);
        this.actionBarLayout.setDrawerLayoutContainer(this.drawerLayoutContainer);
        this.actionBarLayout.init(mainFragmentsStack);
        this.actionBarLayout.setDelegate(this);
        NotificationCenter.getGlobalInstance().postNotificationName(NotificationCenter.closeOtherAppActivities, this);
        Intent intent = getIntent();
        if (savedInstanceState == null) {
            z = false;
        }
        handleIntent(intent, false, z, false);
        needLayout();
    }

    private boolean handleIntent(Intent intent, boolean isNew, boolean restore, boolean fromPassword) {
        if (AndroidUtilities.isTablet()) {
            if (this.layersActionBarLayout.fragmentsStack.isEmpty()) {
                this.layersActionBarLayout.addFragmentToStack(new CacheControlActivity());
            }
        } else if (this.actionBarLayout.fragmentsStack.isEmpty()) {
            this.actionBarLayout.addFragmentToStack(new CacheControlActivity());
        }
        this.actionBarLayout.showLastFragment();
        if (AndroidUtilities.isTablet()) {
            this.layersActionBarLayout.showLastFragment();
        }
        intent.setAction(null);
        return false;
    }

    public boolean onPreIme() {
        return false;
    }

    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        handleIntent(intent, true, false, false);
    }

    private void onFinish() {
        if (!this.finished) {
            this.finished = true;
        }
    }

    public void presentFragment(BaseFragment fragment) {
        this.actionBarLayout.presentFragment(fragment);
    }

    public boolean presentFragment(BaseFragment fragment, boolean removeLast, boolean forceWithoutAnimation) {
        return this.actionBarLayout.presentFragment(fragment, removeLast, forceWithoutAnimation, true);
    }

    public void fixLayout() {
        if (AndroidUtilities.isTablet() && this.actionBarLayout != null) {
            this.actionBarLayout.getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
                public void onGlobalLayout() {
                    ManageSpaceActivity.this.needLayout();
                    if (ManageSpaceActivity.this.actionBarLayout != null) {
                        ManageSpaceActivity.this.actionBarLayout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    }
                }
            });
        }
    }

    protected void onPause() {
        super.onPause();
        this.actionBarLayout.onPause();
        if (AndroidUtilities.isTablet()) {
            this.layersActionBarLayout.onPause();
        }
    }

    protected void onDestroy() {
        super.onDestroy();
        onFinish();
    }

    protected void onResume() {
        super.onResume();
        this.actionBarLayout.onResume();
        if (AndroidUtilities.isTablet()) {
            this.layersActionBarLayout.onResume();
        }
    }

    public void onConfigurationChanged(Configuration newConfig) {
        AndroidUtilities.checkDisplaySize(this, newConfig);
        super.onConfigurationChanged(newConfig);
        fixLayout();
    }

    public void onBackPressed() {
        if (PhotoViewer.getInstance().isVisible()) {
            PhotoViewer.getInstance().closePhoto(true, false);
        } else if (this.drawerLayoutContainer.isDrawerOpened()) {
            this.drawerLayoutContainer.closeDrawer(false);
        } else if (!AndroidUtilities.isTablet()) {
            this.actionBarLayout.onBackPressed();
        } else if (this.layersActionBarLayout.getVisibility() == 0) {
            this.layersActionBarLayout.onBackPressed();
        } else {
            this.actionBarLayout.onBackPressed();
        }
    }

    public void onLowMemory() {
        super.onLowMemory();
        this.actionBarLayout.onLowMemory();
        if (AndroidUtilities.isTablet()) {
            this.layersActionBarLayout.onLowMemory();
        }
    }

    public boolean needPresentFragment(BaseFragment fragment, boolean removeLast, boolean forceWithoutAnimation, ActionBarLayout layout) {
        return true;
    }

    public boolean needAddFragmentToStack(BaseFragment fragment, ActionBarLayout layout) {
        return true;
    }

    public boolean needCloseLastFragment(ActionBarLayout layout) {
        if (AndroidUtilities.isTablet()) {
            if (layout == this.actionBarLayout && layout.fragmentsStack.size() <= 1) {
                onFinish();
                finish();
                return false;
            } else if (layout == this.layersActionBarLayout && this.actionBarLayout.fragmentsStack.isEmpty() && this.layersActionBarLayout.fragmentsStack.size() == 1) {
                onFinish();
                finish();
                return false;
            }
        } else if (layout.fragmentsStack.size() <= 1) {
            onFinish();
            finish();
            return false;
        }
        return true;
    }

    public void onRebuildAllFragments(ActionBarLayout layout, boolean last) {
        if (AndroidUtilities.isTablet() && layout == this.layersActionBarLayout) {
            this.actionBarLayout.rebuildAllFragmentViews(last, last);
        }
    }
}
