package org.telegram.ui.Components;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences.Editor;
import android.database.DataSetObserver;
import android.graphics.Canvas;
import android.graphics.Outline;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffColorFilter;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.os.Build.VERSION;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.LongSparseArray;
import android.util.SparseArray;
import android.util.SparseIntArray;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.View.OnLongClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.ViewOutlineProvider;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnScrollChangedListener;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.ChatObject;
import org.telegram.messenger.DataQuery;
import org.telegram.messenger.Emoji;
import org.telegram.messenger.EmojiData;
import org.telegram.messenger.EmojiSuggestion;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.NotificationCenter;
import org.telegram.messenger.NotificationCenter.NotificationCenterDelegate;
import org.telegram.messenger.UserConfig;
import org.telegram.messenger.beta.R;
import org.telegram.messenger.exoplayer2.DefaultRenderersFactory;
import org.telegram.messenger.support.widget.GridLayoutManager;
import org.telegram.messenger.support.widget.GridLayoutManager.SpanSizeLookup;
import org.telegram.messenger.support.widget.RecyclerView;
import org.telegram.messenger.support.widget.RecyclerView.Adapter;
import org.telegram.messenger.support.widget.RecyclerView.ItemDecoration;
import org.telegram.messenger.support.widget.RecyclerView.LayoutManager;
import org.telegram.messenger.support.widget.RecyclerView.LayoutParams;
import org.telegram.messenger.support.widget.RecyclerView.OnScrollListener;
import org.telegram.messenger.support.widget.RecyclerView.State;
import org.telegram.messenger.support.widget.RecyclerView.ViewHolder;
import org.telegram.tgnet.ConnectionsManager;
import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC.Chat;
import org.telegram.tgnet.TLRPC.ChatFull;
import org.telegram.tgnet.TLRPC.Document;
import org.telegram.tgnet.TLRPC.DocumentAttribute;
import org.telegram.tgnet.TLRPC.InputStickerSet;
import org.telegram.tgnet.TLRPC.StickerSet;
import org.telegram.tgnet.TLRPC.StickerSetCovered;
import org.telegram.tgnet.TLRPC.TL_documentAttributeImageSize;
import org.telegram.tgnet.TLRPC.TL_documentAttributeVideo;
import org.telegram.tgnet.TLRPC.TL_error;
import org.telegram.tgnet.TLRPC.TL_messages_foundStickerSets;
import org.telegram.tgnet.TLRPC.TL_messages_getStickers;
import org.telegram.tgnet.TLRPC.TL_messages_searchStickerSets;
import org.telegram.tgnet.TLRPC.TL_messages_stickerSet;
import org.telegram.tgnet.TLRPC.TL_messages_stickers;
import org.telegram.ui.ActionBar.AlertDialog.Builder;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Cells.ContextLinkCell;
import org.telegram.ui.Cells.EmptyCell;
import org.telegram.ui.Cells.FeaturedStickerSetInfoCell;
import org.telegram.ui.Cells.StickerEmojiCell;
import org.telegram.ui.Cells.StickerSetGroupInfoCell;
import org.telegram.ui.Cells.StickerSetNameCell;
import org.telegram.ui.Components.PagerSlidingTabStrip.IconTabProvider;
import org.telegram.ui.Components.RecyclerListView.Holder;
import org.telegram.ui.Components.RecyclerListView.OnItemClickListener;
import org.telegram.ui.Components.RecyclerListView.OnItemLongClickListener;
import org.telegram.ui.Components.RecyclerListView.SelectionAdapter;
import org.telegram.ui.Components.ScrollSlidingTabStrip.ScrollSlidingTabStripDelegate;
import org.telegram.ui.StickerPreviewViewer;
import org.telegram.ui.StickerPreviewViewer.StickerPreviewViewerDelegate;

public class EmojiView extends FrameLayout implements NotificationCenterDelegate {
    private static final OnScrollChangedListener NOP = new OnScrollChangedListener() {
        public void onScrollChanged() {
        }
    };
    private static final Field superListenerField;
    private ArrayList<EmojiGridAdapter> adapters = new ArrayList();
    private ImageView backspaceButton;
    private boolean backspaceOnce;
    private boolean backspacePressed;
    private ImageView clearSearchImageView;
    private int currentAccount = UserConfig.selectedAccount;
    private int currentBackgroundType = -1;
    private int currentChatId;
    private int currentPage;
    private Paint dotPaint;
    private DragListener dragListener;
    private ArrayList<GridView> emojiGrids = new ArrayList();
    private int emojiSize;
    private LinearLayout emojiTab;
    private int favTabBum = -2;
    private ArrayList<Document> favouriteStickers = new ArrayList();
    private int featuredStickersHash;
    private boolean firstAttach = true;
    private ExtendedGridLayoutManager flowLayoutManager;
    private int gifTabNum = -2;
    private GifsAdapter gifsAdapter;
    private RecyclerListView gifsGridView;
    private int groupStickerPackNum;
    private int groupStickerPackPosition;
    private TL_messages_stickerSet groupStickerSet;
    private boolean groupStickersHidden;
    private Drawable[] icons;
    private ChatFull info;
    private LongSparseArray<StickerSetCovered> installingStickerSets = new LongSparseArray();
    private boolean isLayout;
    private int lastNotifyWidth;
    private Listener listener;
    private int[] location = new int[2];
    private TextView mediaBanTooltip;
    private int minusDy;
    private TextView noRecentTextView;
    private int oldWidth;
    private Object outlineProvider;
    private ViewPager pager;
    private PagerSlidingTabStrip pagerSlidingTabStrip;
    private EmojiColorPickerView pickerView;
    private EmojiPopupWindow pickerViewPopup;
    private int popupHeight;
    private int popupWidth;
    private CloseProgressDrawable2 progressDrawable;
    private ArrayList<Document> recentGifs = new ArrayList();
    private ArrayList<Document> recentStickers = new ArrayList();
    private int recentTabBum = -2;
    private LongSparseArray<StickerSetCovered> removingStickerSets = new LongSparseArray();
    private AnimatorSet searchAnimation;
    private View searchBackground;
    private EditTextBoldCursor searchEditText;
    private FrameLayout searchEditTextContainer;
    private int searchFieldHeight = AndroidUtilities.dp(64.0f);
    private ImageView searchIconImageView;
    private View shadowLine;
    private boolean showGifs;
    private StickerPreviewViewerDelegate stickerPreviewViewerDelegate = new StickerPreviewViewerDelegate() {
        public void sendSticker(Document sticker) {
            EmojiView.this.listener.onStickerSelected(sticker);
        }

        public boolean needSend() {
            return true;
        }

        public void openSet(InputStickerSet set) {
            if (set != null) {
                EmojiView.this.listener.onShowStickerSet(null, set);
            }
        }
    };
    private ArrayList<TL_messages_stickerSet> stickerSets = new ArrayList();
    private Drawable stickersDrawable;
    private TextView stickersEmptyView;
    private StickersGridAdapter stickersGridAdapter;
    private RecyclerListView stickersGridView;
    private GridLayoutManager stickersLayoutManager;
    private OnItemClickListener stickersOnItemClickListener;
    private StickersSearchGridAdapter stickersSearchGridAdapter;
    private ScrollSlidingTabStrip stickersTab;
    private int stickersTabOffset;
    private FrameLayout stickersWrap;
    private boolean switchToGifTab;
    private TrendingGridAdapter trendingGridAdapter;
    private RecyclerListView trendingGridView;
    private GridLayoutManager trendingLayoutManager;
    private boolean trendingLoaded;
    private int trendingTabNum = -2;
    private ArrayList<View> views = new ArrayList();

    public interface DragListener {
        void onDrag(int i);

        void onDragCancel();

        void onDragEnd(float f);

        void onDragStart();
    }

    private class EmojiColorPickerView extends View {
        private Drawable arrowDrawable = getResources().getDrawable(R.drawable.stickers_back_arrow);
        private int arrowX;
        private Drawable backgroundDrawable = getResources().getDrawable(R.drawable.stickers_back_all);
        private String currentEmoji;
        private RectF rect = new RectF();
        private Paint rectPaint = new Paint(1);
        private int selection;

        protected void onDraw(android.graphics.Canvas r1) {
            /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.DecodeException: Load method exception in method: org.telegram.ui.Components.EmojiView.EmojiColorPickerView.onDraw(android.graphics.Canvas):void
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
            r0 = r10.backgroundDrawable;
            r1 = r10.getMeasuredWidth();
            r2 = org.telegram.messenger.AndroidUtilities.isTablet();
            if (r2 == 0) goto L_0x000f;
        L_0x000c:
            r2 = 1114636288; // 0x42700000 float:60.0 double:5.507034975E-315;
            goto L_0x0011;
        L_0x000f:
            r2 = 1112539136; // 0x42500000 float:52.0 double:5.496673668E-315;
        L_0x0011:
            r2 = org.telegram.messenger.AndroidUtilities.dp(r2);
            r3 = 0;
            r0.setBounds(r3, r3, r1, r2);
            r0 = r10.backgroundDrawable;
            r0.draw(r11);
            r0 = r10.arrowDrawable;
            r1 = r10.arrowX;
            r2 = 1091567616; // 0x41100000 float:9.0 double:5.39306059E-315;
            r4 = org.telegram.messenger.AndroidUtilities.dp(r2);
            r1 = r1 - r4;
            r4 = org.telegram.messenger.AndroidUtilities.isTablet();
            r5 = 1111359488; // 0x423e0000 float:47.5 double:5.49084543E-315;
            r6 = 1113456640; // 0x425e0000 float:55.5 double:5.50120674E-315;
            if (r4 == 0) goto L_0x0035;
        L_0x0033:
            r4 = r6;
            goto L_0x0036;
        L_0x0035:
            r4 = r5;
        L_0x0036:
            r4 = org.telegram.messenger.AndroidUtilities.dp(r4);
            r7 = r10.arrowX;
            r8 = org.telegram.messenger.AndroidUtilities.dp(r2);
            r7 = r7 + r8;
            r8 = org.telegram.messenger.AndroidUtilities.isTablet();
            if (r8 == 0) goto L_0x0049;
        L_0x0047:
            r5 = r6;
        L_0x0049:
            r6 = 1090519040; // 0x41000000 float:8.0 double:5.38787994E-315;
            r5 = r5 + r6;
            r5 = org.telegram.messenger.AndroidUtilities.dp(r5);
            r0.setBounds(r1, r4, r7, r5);
            r0 = r10.arrowDrawable;
            r0.draw(r11);
            r0 = r10.currentEmoji;
            if (r0 == 0) goto L_0x00f0;
        L_0x005d:
            r0 = r3;
            r1 = 6;
            if (r0 >= r1) goto L_0x00f0;
        L_0x0061:
            r1 = org.telegram.ui.Components.EmojiView.this;
            r1 = r1.emojiSize;
            r1 = r1 * r0;
            r3 = 5;
            r4 = 4;
            r4 = r4 * r0;
            r3 = r3 + r4;
            r3 = (float) r3;
            r3 = org.telegram.messenger.AndroidUtilities.dp(r3);
            r1 = r1 + r3;
            r3 = org.telegram.messenger.AndroidUtilities.dp(r2);
            r4 = r10.selection;
            if (r4 != r0) goto L_0x00b4;
        L_0x007a:
            r4 = r10.rect;
            r5 = (float) r1;
            r6 = 1080033280; // 0x40600000 float:3.5 double:5.3360734E-315;
            r6 = org.telegram.messenger.AndroidUtilities.dpf2(r6);
            r6 = (int) r6;
            r6 = r3 - r6;
            r6 = (float) r6;
            r7 = org.telegram.ui.Components.EmojiView.this;
            r7 = r7.emojiSize;
            r7 = r7 + r1;
            r7 = (float) r7;
            r8 = org.telegram.ui.Components.EmojiView.this;
            r8 = r8.emojiSize;
            r8 = r8 + r3;
            r9 = 1077936128; // 0x40400000 float:3.0 double:5.325712093E-315;
            r9 = org.telegram.messenger.AndroidUtilities.dp(r9);
            r8 = r8 + r9;
            r8 = (float) r8;
            r4.set(r5, r6, r7, r8);
            r4 = r10.rect;
            r5 = 1082130432; // 0x40800000 float:4.0 double:5.34643471E-315;
            r6 = org.telegram.messenger.AndroidUtilities.dp(r5);
            r6 = (float) r6;
            r5 = org.telegram.messenger.AndroidUtilities.dp(r5);
            r5 = (float) r5;
            r7 = r10.rectPaint;
            r11.drawRoundRect(r4, r6, r5, r7);
        L_0x00b4:
            r4 = r10.currentEmoji;
            if (r0 == 0) goto L_0x00d2;
        L_0x00b8:
            switch(r0) {
                case 1: goto L_0x00ca;
                case 2: goto L_0x00c7;
                case 3: goto L_0x00c4;
                case 4: goto L_0x00c1;
                case 5: goto L_0x00be;
                default: goto L_0x00bb;
            };
        L_0x00bb:
            r5 = "";
            goto L_0x00cd;
        L_0x00be:
            r5 = "🏿";
            goto L_0x00cd;
        L_0x00c1:
            r5 = "🏾";
            goto L_0x00cd;
        L_0x00c4:
            r5 = "🏽";
            goto L_0x00cd;
        L_0x00c7:
            r5 = "🏼";
            goto L_0x00cd;
        L_0x00ca:
            r5 = "🏻";
            r4 = org.telegram.ui.Components.EmojiView.addColorToCode(r4, r5);
        L_0x00d2:
            r5 = org.telegram.messenger.Emoji.getEmojiBigDrawable(r4);
            if (r5 == 0) goto L_0x00ec;
            r6 = org.telegram.ui.Components.EmojiView.this;
            r6 = r6.emojiSize;
            r6 = r6 + r1;
            r7 = org.telegram.ui.Components.EmojiView.this;
            r7 = r7.emojiSize;
            r7 = r7 + r3;
            r5.setBounds(r1, r3, r6, r7);
            r5.draw(r11);
            r3 = r0 + 1;
            goto L_0x005d;
        L_0x00f0:
            return;
            */
            throw new UnsupportedOperationException("Method not decompiled: org.telegram.ui.Components.EmojiView.EmojiColorPickerView.onDraw(android.graphics.Canvas):void");
        }

        public void setEmoji(String emoji, int arrowPosition) {
            this.currentEmoji = emoji;
            this.arrowX = arrowPosition;
            this.rectPaint.setColor(Theme.ACTION_BAR_AUDIO_SELECTOR_COLOR);
            invalidate();
        }

        public String getEmoji() {
            return this.currentEmoji;
        }

        public void setSelection(int position) {
            if (this.selection != position) {
                this.selection = position;
                invalidate();
            }
        }

        public int getSelection() {
            return this.selection;
        }

        public EmojiColorPickerView(Context context) {
            super(context);
        }
    }

    private class EmojiGridAdapter extends BaseAdapter {
        private int emojiPage;

        public EmojiGridAdapter(int page) {
            this.emojiPage = page;
        }

        public Object getItem(int position) {
            return null;
        }

        public int getCount() {
            if (this.emojiPage == -1) {
                return Emoji.recentEmoji.size();
            }
            return EmojiData.dataColored[this.emojiPage].length;
        }

        public long getItemId(int position) {
            return (long) position;
        }

        public View getView(int position, View view, ViewGroup paramViewGroup) {
            String coloredCode;
            String code;
            ImageViewEmoji imageView = (ImageViewEmoji) view;
            if (imageView == null) {
                imageView = new ImageViewEmoji(EmojiView.this.getContext());
            }
            if (this.emojiPage == -1) {
                coloredCode = (String) Emoji.recentEmoji.get(position);
                code = coloredCode;
            } else {
                coloredCode = EmojiData.dataColored[this.emojiPage][position];
                code = coloredCode;
                String color = (String) Emoji.emojiColor.get(code);
                if (color != null) {
                    coloredCode = EmojiView.addColorToCode(coloredCode, color);
                }
            }
            imageView.setImageDrawable(Emoji.getEmojiBigDrawable(coloredCode));
            imageView.setTag(code);
            return imageView;
        }

        public void unregisterDataSetObserver(DataSetObserver observer) {
            if (observer != null) {
                super.unregisterDataSetObserver(observer);
            }
        }
    }

    private class EmojiPopupWindow extends PopupWindow {
        private OnScrollChangedListener mSuperScrollListener;
        private ViewTreeObserver mViewTreeObserver;

        public EmojiPopupWindow() {
            init();
        }

        public EmojiPopupWindow(Context context) {
            super(context);
            init();
        }

        public EmojiPopupWindow(int width, int height) {
            super(width, height);
            init();
        }

        public EmojiPopupWindow(View contentView) {
            super(contentView);
            init();
        }

        public EmojiPopupWindow(View contentView, int width, int height, boolean focusable) {
            super(contentView, width, height, focusable);
            init();
        }

        public EmojiPopupWindow(View contentView, int width, int height) {
            super(contentView, width, height);
            init();
        }

        private void init() {
            if (EmojiView.superListenerField != null) {
                try {
                    this.mSuperScrollListener = (OnScrollChangedListener) EmojiView.superListenerField.get(this);
                    EmojiView.superListenerField.set(this, EmojiView.NOP);
                } catch (Exception e) {
                    this.mSuperScrollListener = null;
                }
            }
        }

        private void unregisterListener() {
            if (this.mSuperScrollListener != null && this.mViewTreeObserver != null) {
                if (this.mViewTreeObserver.isAlive()) {
                    this.mViewTreeObserver.removeOnScrollChangedListener(this.mSuperScrollListener);
                }
                this.mViewTreeObserver = null;
            }
        }

        private void registerListener(View anchor) {
            if (this.mSuperScrollListener != null) {
                ViewTreeObserver vto = anchor.getWindowToken() != null ? anchor.getViewTreeObserver() : null;
                if (vto != this.mViewTreeObserver) {
                    if (this.mViewTreeObserver != null && this.mViewTreeObserver.isAlive()) {
                        this.mViewTreeObserver.removeOnScrollChangedListener(this.mSuperScrollListener);
                    }
                    this.mViewTreeObserver = vto;
                    if (vto != null) {
                        vto.addOnScrollChangedListener(this.mSuperScrollListener);
                    }
                }
            }
        }

        public void showAsDropDown(View anchor, int xoff, int yoff) {
            try {
                super.showAsDropDown(anchor, xoff, yoff);
                registerListener(anchor);
            } catch (Throwable e) {
                FileLog.e(e);
            }
        }

        public void update(View anchor, int xoff, int yoff, int width, int height) {
            super.update(anchor, xoff, yoff, width, height);
            registerListener(anchor);
        }

        public void update(View anchor, int width, int height) {
            super.update(anchor, width, height);
            registerListener(anchor);
        }

        public void showAtLocation(View parent, int gravity, int x, int y) {
            super.showAtLocation(parent, gravity, x, y);
            unregisterListener();
        }

        public void dismiss() {
            setFocusable(false);
            try {
                super.dismiss();
            } catch (Exception e) {
            }
            unregisterListener();
        }
    }

    private class ImageViewEmoji extends ImageView {
        private float lastX;
        private float lastY;
        private boolean touched;
        private float touchedX;
        private float touchedY;

        public ImageViewEmoji(Context context) {
            super(context);
            setOnClickListener(new OnClickListener(EmojiView.this) {
                public void onClick(View v) {
                    ImageViewEmoji.this.sendEmoji(null);
                }
            });
            setOnLongClickListener(new OnLongClickListener(EmojiView.this) {
                public boolean onLongClick(View view) {
                    String code = (String) view.getTag();
                    int yOffset = 0;
                    if (EmojiData.emojiColoredMap.containsKey(code)) {
                        int i;
                        ImageViewEmoji.this.touched = true;
                        ImageViewEmoji.this.touchedX = ImageViewEmoji.this.lastX;
                        ImageViewEmoji.this.touchedY = ImageViewEmoji.this.lastY;
                        String color = (String) Emoji.emojiColor.get(code);
                        int i2 = 5;
                        if (color != null) {
                            i = -1;
                            switch (color.hashCode()) {
                                case 1773375:
                                    if (color.equals("🏻")) {
                                        i = 0;
                                        break;
                                    }
                                    break;
                                case 1773376:
                                    if (color.equals("🏼")) {
                                        i = true;
                                        break;
                                    }
                                    break;
                                case 1773377:
                                    if (color.equals("🏽")) {
                                        i = 2;
                                        break;
                                    }
                                    break;
                                case 1773378:
                                    if (color.equals("🏾")) {
                                        i = 3;
                                        break;
                                    }
                                    break;
                                case 1773379:
                                    if (color.equals("🏿")) {
                                        i = 4;
                                        break;
                                    }
                                    break;
                                default:
                                    break;
                            }
                            switch (i) {
                                case 0:
                                    EmojiView.this.pickerView.setSelection(1);
                                    break;
                                case 1:
                                    EmojiView.this.pickerView.setSelection(2);
                                    break;
                                case 2:
                                    EmojiView.this.pickerView.setSelection(3);
                                    break;
                                case 3:
                                    EmojiView.this.pickerView.setSelection(4);
                                    break;
                                case 4:
                                    EmojiView.this.pickerView.setSelection(5);
                                    break;
                                default:
                                    break;
                            }
                        }
                        EmojiView.this.pickerView.setSelection(0);
                        view.getLocationOnScreen(EmojiView.this.location);
                        i = EmojiView.this.emojiSize * EmojiView.this.pickerView.getSelection();
                        int selection = 4 * EmojiView.this.pickerView.getSelection();
                        if (!AndroidUtilities.isTablet()) {
                            i2 = 1;
                        }
                        i += AndroidUtilities.dp((float) (selection - i2));
                        if (EmojiView.this.location[0] - i < AndroidUtilities.dp(5.0f)) {
                            i += (EmojiView.this.location[0] - i) - AndroidUtilities.dp(5.0f);
                        } else if ((EmojiView.this.location[0] - i) + EmojiView.this.popupWidth > AndroidUtilities.displaySize.x - AndroidUtilities.dp(5.0f)) {
                            i += ((EmojiView.this.location[0] - i) + EmojiView.this.popupWidth) - (AndroidUtilities.displaySize.x - AndroidUtilities.dp(5.0f));
                        }
                        i2 = -i;
                        if (view.getTop() < 0) {
                            yOffset = view.getTop();
                        }
                        EmojiView.this.pickerView.setEmoji(code, (AndroidUtilities.dp(AndroidUtilities.isTablet() ? 30.0f : 22.0f) - i2) + ((int) AndroidUtilities.dpf2(0.5f)));
                        EmojiView.this.pickerViewPopup.setFocusable(true);
                        EmojiView.this.pickerViewPopup.showAsDropDown(view, i2, (((-view.getMeasuredHeight()) - EmojiView.this.popupHeight) + ((view.getMeasuredHeight() - EmojiView.this.emojiSize) / 2)) - yOffset);
                        view.getParent().requestDisallowInterceptTouchEvent(true);
                        return true;
                    }
                    if (EmojiView.this.pager.getCurrentItem() == 0) {
                        EmojiView.this.listener.onClearEmojiRecent();
                    }
                    return false;
                }
            });
            setBackgroundDrawable(Theme.getSelectorDrawable(null));
            setScaleType(ScaleType.CENTER);
        }

        private void sendEmoji(String override) {
            String code = override != null ? override : (String) getTag();
            new SpannableStringBuilder().append(code);
            if (override == null) {
                if (EmojiView.this.pager.getCurrentItem() != 0) {
                    String color = (String) Emoji.emojiColor.get(code);
                    if (color != null) {
                        code = EmojiView.addColorToCode(code, color);
                    }
                }
                EmojiView.this.addEmojiToRecent(code);
                if (EmojiView.this.listener != null) {
                    EmojiView.this.listener.onEmojiSelected(Emoji.fixEmoji(code));
                }
            } else if (EmojiView.this.listener != null) {
                EmojiView.this.listener.onEmojiSelected(Emoji.fixEmoji(override));
            }
        }

        public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
            setMeasuredDimension(MeasureSpec.getSize(widthMeasureSpec), MeasureSpec.getSize(widthMeasureSpec));
        }

        public boolean onTouchEvent(MotionEvent event) {
            if (this.touched) {
                if (event.getAction() != 1) {
                    if (event.getAction() != 3) {
                        if (event.getAction() == 2) {
                            boolean ignore = false;
                            if (this.touchedX != -10000.0f) {
                                if (Math.abs(this.touchedX - event.getX()) <= AndroidUtilities.getPixelsInCM(0.2f, true)) {
                                    if (Math.abs(this.touchedY - event.getY()) <= AndroidUtilities.getPixelsInCM(0.2f, false)) {
                                        ignore = true;
                                    }
                                }
                                this.touchedX = -10000.0f;
                                this.touchedY = -10000.0f;
                            }
                            if (!ignore) {
                                getLocationOnScreen(EmojiView.this.location);
                                float x = ((float) EmojiView.this.location[0]) + event.getX();
                                EmojiView.this.pickerView.getLocationOnScreen(EmojiView.this.location);
                                int position = (int) ((x - ((float) (EmojiView.this.location[0] + AndroidUtilities.dp(3.0f)))) / ((float) (EmojiView.this.emojiSize + AndroidUtilities.dp(4.0f))));
                                if (position < 0) {
                                    position = 0;
                                } else if (position > 5) {
                                    position = 5;
                                }
                                EmojiView.this.pickerView.setSelection(position);
                            }
                        }
                    }
                }
                if (EmojiView.this.pickerViewPopup != null && EmojiView.this.pickerViewPopup.isShowing()) {
                    EmojiView.this.pickerViewPopup.dismiss();
                    String color = null;
                    switch (EmojiView.this.pickerView.getSelection()) {
                        case 1:
                            color = "🏻";
                            break;
                        case 2:
                            color = "🏼";
                            break;
                        case 3:
                            color = "🏽";
                            break;
                        case 4:
                            color = "🏾";
                            break;
                        case 5:
                            color = "🏿";
                            break;
                        default:
                            break;
                    }
                    String code = (String) getTag();
                    if (EmojiView.this.pager.getCurrentItem() != 0) {
                        if (color != null) {
                            Emoji.emojiColor.put(code, color);
                            code = EmojiView.addColorToCode(code, color);
                        } else {
                            Emoji.emojiColor.remove(code);
                        }
                        setImageDrawable(Emoji.getEmojiBigDrawable(code));
                        sendEmoji(null);
                        Emoji.saveEmojiColors();
                    } else if (color != null) {
                        sendEmoji(EmojiView.addColorToCode(code, color));
                    } else {
                        sendEmoji(code);
                    }
                }
                this.touched = false;
                this.touchedX = -10000.0f;
                this.touchedY = -10000.0f;
            }
            this.lastX = event.getX();
            this.lastY = event.getY();
            return super.onTouchEvent(event);
        }
    }

    public interface Listener {
        boolean isExpanded();

        boolean isSearchOpened();

        boolean onBackspace();

        void onClearEmojiRecent();

        void onEmojiSelected(String str);

        void onGifSelected(Document document);

        void onGifTab(boolean z);

        void onSearchOpenClose(boolean z);

        void onShowStickerSet(StickerSet stickerSet, InputStickerSet inputStickerSet);

        void onStickerSelected(Document document);

        void onStickerSetAdd(StickerSetCovered stickerSetCovered);

        void onStickerSetRemove(StickerSetCovered stickerSetCovered);

        void onStickersGroupClick(int i);

        void onStickersSettingsClick();

        void onStickersTab(boolean z);
    }

    private class EmojiPagesAdapter extends PagerAdapter implements IconTabProvider {
        private EmojiPagesAdapter() {
        }

        public void destroyItem(ViewGroup viewGroup, int position, Object object) {
            View view;
            if (position == 6) {
                view = EmojiView.this.stickersWrap;
            } else {
                view = (View) EmojiView.this.views.get(position);
            }
            viewGroup.removeView(view);
        }

        public boolean canScrollToTab(int position) {
            if (position != 6 || EmojiView.this.currentChatId == 0) {
                return true;
            }
            EmojiView.this.showStickerBanHint();
            return false;
        }

        public int getCount() {
            return EmojiView.this.views.size();
        }

        public Drawable getPageIconDrawable(int position) {
            return EmojiView.this.icons[position];
        }

        public void customOnDraw(Canvas canvas, int position) {
            if (position == 6 && !DataQuery.getInstance(EmojiView.this.currentAccount).getUnreadStickerSets().isEmpty() && EmojiView.this.dotPaint != null) {
                canvas.drawCircle((float) ((canvas.getWidth() / 2) + AndroidUtilities.dp(9.0f)), (float) ((canvas.getHeight() / 2) - AndroidUtilities.dp(8.0f)), (float) AndroidUtilities.dp(5.0f), EmojiView.this.dotPaint);
            }
        }

        public Object instantiateItem(ViewGroup viewGroup, int position) {
            View view;
            if (position == 6) {
                view = EmojiView.this.stickersWrap;
            } else {
                view = (View) EmojiView.this.views.get(position);
            }
            viewGroup.addView(view);
            return view;
        }

        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        public void unregisterDataSetObserver(DataSetObserver observer) {
            if (observer != null) {
                super.unregisterDataSetObserver(observer);
            }
        }
    }

    private class GifsAdapter extends SelectionAdapter {
        private Context mContext;

        public GifsAdapter(Context context) {
            this.mContext = context;
        }

        public boolean isEnabled(ViewHolder holder) {
            return false;
        }

        public int getItemCount() {
            return EmojiView.this.recentGifs.size();
        }

        public long getItemId(int i) {
            return (long) i;
        }

        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            return new Holder(new ContextLinkCell(this.mContext));
        }

        public void onBindViewHolder(ViewHolder viewHolder, int i) {
            Document document = (Document) EmojiView.this.recentGifs.get(i);
            if (document != null) {
                ((ContextLinkCell) viewHolder.itemView).setGif(document, false);
            }
        }
    }

    private class StickersGridAdapter extends SelectionAdapter {
        private SparseArray<Object> cache = new SparseArray();
        private Context context;
        private HashMap<Object, Integer> packStartPosition = new HashMap();
        private SparseIntArray positionToRow = new SparseIntArray();
        private SparseArray<Object> rowStartPack = new SparseArray();
        private int stickersPerRow;
        private int totalItems;

        public void onBindViewHolder(org.telegram.messenger.support.widget.RecyclerView.ViewHolder r1, int r2) {
            /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.DecodeException: Load method exception in method: org.telegram.ui.Components.EmojiView.StickersGridAdapter.onBindViewHolder(org.telegram.messenger.support.widget.RecyclerView$ViewHolder, int):void
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
            r0 = r10.getItemViewType();
            r1 = 0;
            r2 = 0;
            r3 = 1;
            switch(r0) {
                case 0: goto L_0x0154;
                case 1: goto L_0x00c9;
                case 2: goto L_0x001c;
                case 3: goto L_0x000c;
                default: goto L_0x000a;
            };
        L_0x000a:
            goto L_0x0182;
        L_0x000c:
            r0 = r10.itemView;
            r0 = (org.telegram.ui.Cells.StickerSetGroupInfoCell) r0;
            r1 = r9.totalItems;
            r1 = r1 - r3;
            if (r11 != r1) goto L_0x0017;
        L_0x0015:
            r2 = r3;
        L_0x0017:
            r0.setIsLast(r2);
            goto L_0x0182;
        L_0x001c:
            r0 = r10.itemView;
            r0 = (org.telegram.ui.Cells.StickerSetNameCell) r0;
            r4 = org.telegram.ui.Components.EmojiView.this;
            r4 = r4.groupStickerPackPosition;
            if (r11 != r4) goto L_0x0085;
        L_0x0028:
            r4 = org.telegram.ui.Components.EmojiView.this;
            r4 = r4.groupStickersHidden;
            if (r4 == 0) goto L_0x003a;
        L_0x0030:
            r4 = org.telegram.ui.Components.EmojiView.this;
            r4 = r4.groupStickerSet;
            if (r4 != 0) goto L_0x003a;
        L_0x0038:
            r4 = 0;
            goto L_0x0049;
        L_0x003a:
            r4 = org.telegram.ui.Components.EmojiView.this;
            r4 = r4.groupStickerSet;
            if (r4 == 0) goto L_0x0046;
        L_0x0042:
            r4 = 2131165662; // 0x7f0701de float:1.7945547E38 double:1.052935739E-314;
            goto L_0x0049;
        L_0x0046:
            r4 = 2131165663; // 0x7f0701df float:1.794555E38 double:1.0529357397E-314;
        L_0x0049:
            r5 = org.telegram.ui.Components.EmojiView.this;
            r5 = r5.info;
            if (r5 == 0) goto L_0x006c;
        L_0x0051:
            r1 = org.telegram.ui.Components.EmojiView.this;
            r1 = r1.currentAccount;
            r1 = org.telegram.messenger.MessagesController.getInstance(r1);
            r5 = org.telegram.ui.Components.EmojiView.this;
            r5 = r5.info;
            r5 = r5.id;
            r5 = java.lang.Integer.valueOf(r5);
            r1 = r1.getChat(r5);
        L_0x006c:
            r5 = "CurrentGroupStickers";
            r6 = 2131493317; // 0x7f0c01c5 float:1.861011E38 double:1.0530976223E-314;
            r3 = new java.lang.Object[r3];
            if (r1 == 0) goto L_0x0078;
        L_0x0075:
            r7 = r1.title;
            goto L_0x007a;
        L_0x0078:
            r7 = "Group Stickers";
        L_0x007a:
            r3[r2] = r7;
            r2 = org.telegram.messenger.LocaleController.formatString(r5, r6, r3);
            r0.setText(r2, r4);
            goto L_0x0182;
        L_0x0085:
            r1 = r9.cache;
            r1 = r1.get(r11);
            r3 = r1 instanceof org.telegram.tgnet.TLRPC.TL_messages_stickerSet;
            if (r3 == 0) goto L_0x009e;
        L_0x008f:
            r3 = r1;
            r3 = (org.telegram.tgnet.TLRPC.TL_messages_stickerSet) r3;
            r4 = r3.set;
            if (r4 == 0) goto L_0x009d;
        L_0x0096:
            r4 = r3.set;
            r4 = r4.title;
            r0.setText(r4, r2);
        L_0x009d:
            goto L_0x00c7;
        L_0x009e:
            r3 = org.telegram.ui.Components.EmojiView.this;
            r3 = r3.recentStickers;
            if (r1 != r3) goto L_0x00b3;
        L_0x00a6:
            r3 = "RecentStickers";
            r4 = 2131494216; // 0x7f0c0548 float:1.8611934E38 double:1.0530980664E-314;
            r3 = org.telegram.messenger.LocaleController.getString(r3, r4);
            r0.setText(r3, r2);
            goto L_0x00c7;
        L_0x00b3:
            r3 = org.telegram.ui.Components.EmojiView.this;
            r3 = r3.favouriteStickers;
            if (r1 != r3) goto L_0x00c7;
        L_0x00bb:
            r3 = "FavoriteStickers";
            r4 = 2131493534; // 0x7f0c029e float:1.861055E38 double:1.0530977295E-314;
            r3 = org.telegram.messenger.LocaleController.getString(r3, r4);
            r0.setText(r3, r2);
        L_0x00c7:
            goto L_0x0182;
        L_0x00c9:
            r0 = r10.itemView;
            r0 = (org.telegram.ui.Cells.EmptyCell) r0;
            r2 = r9.totalItems;
            r4 = 1118044160; // 0x42a40000 float:82.0 double:5.5238721E-315;
            if (r11 != r2) goto L_0x014c;
        L_0x00d3:
            r2 = r9.positionToRow;
            r5 = r11 + -1;
            r6 = -2147483648; // 0xffffffff80000000 float:-0.0 double:NaN;
            r2 = r2.get(r5, r6);
            if (r2 != r6) goto L_0x00e3;
        L_0x00df:
            r0.setHeight(r3);
            goto L_0x014b;
        L_0x00e3:
            r5 = r9.rowStartPack;
            r5 = r5.get(r2);
            r6 = r5 instanceof org.telegram.tgnet.TLRPC.TL_messages_stickerSet;
            if (r6 == 0) goto L_0x00f3;
        L_0x00ed:
            r1 = r5;
            r1 = (org.telegram.tgnet.TLRPC.TL_messages_stickerSet) r1;
            r1 = r1.documents;
            goto L_0x010e;
        L_0x00f3:
            r6 = r5 instanceof java.lang.String;
            if (r6 == 0) goto L_0x010d;
        L_0x00f7:
            r1 = "recent";
            r1 = r1.equals(r5);
            if (r1 == 0) goto L_0x0106;
            r1 = org.telegram.ui.Components.EmojiView.this;
            r1 = r1.recentStickers;
            goto L_0x00f2;
            r1 = org.telegram.ui.Components.EmojiView.this;
            r1 = r1.favouriteStickers;
            goto L_0x00f2;
        L_0x010e:
            if (r1 != 0) goto L_0x0114;
            r0.setHeight(r3);
            goto L_0x014b;
            r6 = r1.isEmpty();
            if (r6 == 0) goto L_0x0124;
            r3 = 1090519040; // 0x41000000 float:8.0 double:5.38787994E-315;
            r3 = org.telegram.messenger.AndroidUtilities.dp(r3);
            r0.setHeight(r3);
            goto L_0x014b;
            r6 = org.telegram.ui.Components.EmojiView.this;
            r6 = r6.pager;
            r6 = r6.getHeight();
            r7 = r1.size();
            r7 = (float) r7;
            r8 = r9.stickersPerRow;
            r8 = (float) r8;
            r7 = r7 / r8;
            r7 = (double) r7;
            r7 = java.lang.Math.ceil(r7);
            r7 = (int) r7;
            r4 = org.telegram.messenger.AndroidUtilities.dp(r4);
            r7 = r7 * r4;
            r4 = r6 - r7;
            if (r4 <= 0) goto L_0x0148;
            r3 = r4;
            r0.setHeight(r3);
        L_0x014b:
            goto L_0x0182;
        L_0x014c:
            r1 = org.telegram.messenger.AndroidUtilities.dp(r4);
            r0.setHeight(r1);
            goto L_0x0182;
        L_0x0154:
            r0 = r9.cache;
            r0 = r0.get(r11);
            r0 = (org.telegram.tgnet.TLRPC.Document) r0;
            r1 = r10.itemView;
            r1 = (org.telegram.ui.Cells.StickerEmojiCell) r1;
            r1.setSticker(r0, r2);
            r4 = org.telegram.ui.Components.EmojiView.this;
            r4 = r4.recentStickers;
            r4 = r4.contains(r0);
            if (r4 != 0) goto L_0x017d;
            r4 = org.telegram.ui.Components.EmojiView.this;
            r4 = r4.favouriteStickers;
            r4 = r4.contains(r0);
            if (r4 == 0) goto L_0x017c;
            goto L_0x017d;
            goto L_0x017e;
            r2 = r3;
            r1.setRecent(r2);
        L_0x0182:
            return;
            */
            throw new UnsupportedOperationException("Method not decompiled: org.telegram.ui.Components.EmojiView.StickersGridAdapter.onBindViewHolder(org.telegram.messenger.support.widget.RecyclerView$ViewHolder, int):void");
        }

        public StickersGridAdapter(Context context) {
            this.context = context;
        }

        public boolean isEnabled(ViewHolder holder) {
            return false;
        }

        public int getItemCount() {
            return this.totalItems != 0 ? this.totalItems + 1 : 0;
        }

        public Object getItem(int i) {
            return this.cache.get(i);
        }

        public int getPositionForPack(Object pack) {
            Integer pos = (Integer) this.packStartPosition.get(pack);
            if (pos == null) {
                return -1;
            }
            return pos.intValue();
        }

        public int getItemViewType(int position) {
            if (position == 0) {
                return 4;
            }
            Object object = this.cache.get(position);
            if (object == null) {
                return 1;
            }
            if (object instanceof Document) {
                return 0;
            }
            if (object instanceof String) {
                return 3;
            }
            return 2;
        }

        public int getTabForPosition(int position) {
            int width;
            if (position == 0) {
                position = 1;
            }
            if (this.stickersPerRow == 0) {
                width = EmojiView.this.getMeasuredWidth();
                if (width == 0) {
                    width = AndroidUtilities.displaySize.x;
                }
                this.stickersPerRow = width / AndroidUtilities.dp(72.0f);
            }
            width = this.positionToRow.get(position, Integer.MIN_VALUE);
            if (width == Integer.MIN_VALUE) {
                return (EmojiView.this.stickerSets.size() - 1) + EmojiView.this.stickersTabOffset;
            }
            TL_messages_stickerSet pack = this.rowStartPack.get(width);
            if (!(pack instanceof String)) {
                return EmojiView.this.stickersTabOffset + EmojiView.this.stickerSets.indexOf(pack);
            } else if ("recent".equals(pack)) {
                return EmojiView.this.recentTabBum;
            } else {
                return EmojiView.this.favTabBum;
            }
        }

        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = null;
            switch (viewType) {
                case 0:
                    view = new StickerEmojiCell(this.context) {
                        public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
                            super.onMeasure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(82.0f), 1073741824));
                        }
                    };
                    break;
                case 1:
                    view = new EmptyCell(this.context);
                    break;
                case 2:
                    view = new StickerSetNameCell(this.context);
                    ((StickerSetNameCell) view).setOnIconClickListener(new OnClickListener() {
                        public void onClick(View v) {
                            if (EmojiView.this.groupStickerSet == null) {
                                Editor edit = MessagesController.getEmojiSettings(EmojiView.this.currentAccount).edit();
                                StringBuilder stringBuilder = new StringBuilder();
                                stringBuilder.append("group_hide_stickers_");
                                stringBuilder.append(EmojiView.this.info.id);
                                edit.putLong(stringBuilder.toString(), EmojiView.this.info.stickerset != null ? EmojiView.this.info.stickerset.id : 0).commit();
                                EmojiView.this.updateStickerTabs();
                                if (EmojiView.this.stickersGridAdapter != null) {
                                    EmojiView.this.stickersGridAdapter.notifyDataSetChanged();
                                }
                            } else if (EmojiView.this.listener != null) {
                                EmojiView.this.listener.onStickersGroupClick(EmojiView.this.info.id);
                            }
                        }
                    });
                    break;
                case 3:
                    view = new StickerSetGroupInfoCell(this.context);
                    ((StickerSetGroupInfoCell) view).setAddOnClickListener(new OnClickListener() {
                        public void onClick(View v) {
                            if (EmojiView.this.listener != null) {
                                EmojiView.this.listener.onStickersGroupClick(EmojiView.this.info.id);
                            }
                        }
                    });
                    view.setLayoutParams(new LayoutParams(-1, -2));
                    break;
                case 4:
                    view = new View(this.context);
                    view.setLayoutParams(new LayoutParams(-1, EmojiView.this.searchFieldHeight));
                    break;
                default:
                    break;
            }
            return new Holder(view);
        }

        public void notifyDataSetChanged() {
            int width = EmojiView.this.getMeasuredWidth();
            if (width == 0) {
                width = AndroidUtilities.displaySize.x;
            }
            r0.stickersPerRow = width / AndroidUtilities.dp(72.0f);
            EmojiView.this.stickersLayoutManager.setSpanCount(r0.stickersPerRow);
            r0.rowStartPack.clear();
            r0.packStartPosition.clear();
            r0.positionToRow.clear();
            r0.cache.clear();
            int i = 0;
            r0.totalItems = 0;
            ArrayList<TL_messages_stickerSet> packs = EmojiView.this.stickerSets;
            int startRow = 0;
            int a = -3;
            while (a < packs.size()) {
                TL_messages_stickerSet pack = null;
                int i2;
                if (a == -3) {
                    SparseArray sparseArray = r0.cache;
                    i2 = r0.totalItems;
                    r0.totalItems = i2 + 1;
                    sparseArray.put(i2, "search");
                    startRow++;
                } else {
                    ArrayList<Document> documents;
                    int startRow2;
                    int startRow3;
                    if (a == -2) {
                        documents = EmojiView.this.favouriteStickers;
                        r0.packStartPosition.put("fav", Integer.valueOf(r0.totalItems));
                    } else if (a == -1) {
                        documents = EmojiView.this.recentStickers;
                        r0.packStartPosition.put("recent", Integer.valueOf(r0.totalItems));
                    } else {
                        pack = (TL_messages_stickerSet) packs.get(a);
                        documents = pack.documents;
                        r0.packStartPosition.put(pack, Integer.valueOf(r0.totalItems));
                    }
                    if (a == EmojiView.this.groupStickerPackNum) {
                        EmojiView.this.groupStickerPackPosition = r0.totalItems;
                        if (documents.isEmpty()) {
                            r0.rowStartPack.put(startRow, pack);
                            startRow2 = startRow + 1;
                            r0.positionToRow.put(r0.totalItems, startRow);
                            r0.rowStartPack.put(startRow2, pack);
                            startRow3 = startRow2 + 1;
                            r0.positionToRow.put(r0.totalItems + 1, startRow2);
                            SparseArray sparseArray2 = r0.cache;
                            i2 = r0.totalItems;
                            r0.totalItems = i2 + 1;
                            sparseArray2.put(i2, pack);
                            sparseArray2 = r0.cache;
                            i2 = r0.totalItems;
                            r0.totalItems = i2 + 1;
                            sparseArray2.put(i2, "group");
                            startRow = startRow3;
                        }
                    }
                    if (!documents.isEmpty()) {
                        startRow3 = (int) Math.ceil((double) (((float) documents.size()) / ((float) r0.stickersPerRow)));
                        if (pack != null) {
                            r0.cache.put(r0.totalItems, pack);
                        } else {
                            r0.cache.put(r0.totalItems, documents);
                        }
                        r0.positionToRow.put(r0.totalItems, startRow);
                        for (startRow2 = i; startRow2 < documents.size(); startRow2++) {
                            r0.cache.put((1 + startRow2) + r0.totalItems, documents.get(startRow2));
                            r0.positionToRow.put((1 + startRow2) + r0.totalItems, (startRow + 1) + (startRow2 / r0.stickersPerRow));
                        }
                        for (i = 0; i < startRow3 + 1; i++) {
                            if (pack != null) {
                                r0.rowStartPack.put(startRow + i, pack);
                            } else {
                                r0.rowStartPack.put(startRow + i, a == -1 ? "recent" : "fav");
                            }
                        }
                        r0.totalItems += (r0.stickersPerRow * startRow3) + 1;
                        startRow += startRow3 + 1;
                    }
                }
                a++;
                i = 0;
            }
            super.notifyDataSetChanged();
        }
    }

    private class StickersSearchGridAdapter extends SelectionAdapter {
        private SparseArray<Object> cache = new SparseArray();
        boolean cleared;
        private Context context;
        private ArrayList<ArrayList<Document>> emojiArrays = new ArrayList();
        private HashMap<ArrayList<Document>, String> emojiStickers = new HashMap();
        private ArrayList<TL_messages_stickerSet> localPacks = new ArrayList();
        private HashMap<TL_messages_stickerSet, Integer> localPacksByName = new HashMap();
        private HashMap<TL_messages_stickerSet, Boolean> localPacksByShortName = new HashMap();
        private SparseIntArray positionToRow = new SparseIntArray();
        private SparseArray<StickerSetCovered> positionsToSets = new SparseArray();
        private int reqId;
        private int reqId2;
        private SparseArray<Object> rowStartPack = new SparseArray();
        private String searchQuery;
        private Runnable searchRunnable = new Runnable() {
            private void clear() {
                if (!StickersSearchGridAdapter.this.cleared) {
                    StickersSearchGridAdapter.this.cleared = true;
                    StickersSearchGridAdapter.this.emojiStickers.clear();
                    StickersSearchGridAdapter.this.emojiArrays.clear();
                    StickersSearchGridAdapter.this.localPacks.clear();
                    StickersSearchGridAdapter.this.serverPacks.clear();
                    StickersSearchGridAdapter.this.localPacksByShortName.clear();
                    StickersSearchGridAdapter.this.localPacksByName.clear();
                }
            }

            public void run() {
                if (!TextUtils.isEmpty(StickersSearchGridAdapter.this.searchQuery)) {
                    int length;
                    int a;
                    int size;
                    int a2;
                    TL_messages_stickerSet set;
                    EmojiView.this.progressDrawable.startAnimation();
                    StickersSearchGridAdapter.this.cleared = false;
                    final ArrayList<Document> emojiStickersArray = new ArrayList(0);
                    final LongSparseArray<Document> emojiStickersMap = new LongSparseArray(0);
                    HashMap<String, ArrayList<Document>> allStickers = DataQuery.getInstance(EmojiView.this.currentAccount).getAllStickers();
                    if (StickersSearchGridAdapter.this.searchQuery.length() <= 14) {
                        CharSequence emoji = StickersSearchGridAdapter.this.searchQuery;
                        length = emoji.length();
                        CharSequence emoji2 = emoji;
                        a = 0;
                        while (a < length) {
                            if (a < length - 1 && ((emoji2.charAt(a) == '?' && emoji2.charAt(a + 1) >= '?' && emoji2.charAt(a + 1) <= '?') || (emoji2.charAt(a) == '‍' && (emoji2.charAt(a + 1) == '♀' || emoji2.charAt(a + 1) == '♂')))) {
                                emoji2 = TextUtils.concat(new CharSequence[]{emoji2.subSequence(0, a), emoji2.subSequence(a + 2, emoji2.length())});
                                length -= 2;
                                a--;
                            } else if (emoji2.charAt(a) == '️') {
                                emoji2 = TextUtils.concat(new CharSequence[]{emoji2.subSequence(0, a), emoji2.subSequence(a + 1, emoji2.length())});
                                length--;
                                a--;
                            }
                            a++;
                        }
                        ArrayList<Document> newStickers = allStickers != null ? (ArrayList) allStickers.get(emoji2.toString()) : null;
                        if (!(newStickers == null || newStickers.isEmpty())) {
                            clear();
                            emojiStickersArray.addAll(newStickers);
                            size = newStickers.size();
                            for (a2 = 0; a2 < size; a2++) {
                                Document document = (Document) newStickers.get(a2);
                                emojiStickersMap.put(document.id, document);
                            }
                            StickersSearchGridAdapter.this.emojiStickers.put(emojiStickersArray, StickersSearchGridAdapter.this.searchQuery);
                            StickersSearchGridAdapter.this.emojiArrays.add(emojiStickersArray);
                        }
                    }
                    if (!(allStickers == null || allStickers.isEmpty() || StickersSearchGridAdapter.this.searchQuery.length() <= 1)) {
                        Object[] suggestions;
                        if (StickersSearchGridAdapter.this.searchQuery.startsWith(":")) {
                            suggestions = StickersSearchGridAdapter.this.searchQuery;
                        } else {
                            StringBuilder stringBuilder = new StringBuilder();
                            stringBuilder.append(":");
                            stringBuilder.append(StickersSearchGridAdapter.this.searchQuery);
                            suggestions = stringBuilder.toString();
                        }
                        suggestions = Emoji.getSuggestion(suggestions);
                        if (suggestions != null) {
                            int size2 = Math.min(10, suggestions.length);
                            for (length = 0; length < size2; length++) {
                                EmojiSuggestion suggestion = suggestions[length];
                                suggestion.emoji = suggestion.emoji.replace("️", TtmlNode.ANONYMOUS_REGION_ID);
                                ArrayList<Document> newStickers2 = allStickers != null ? (ArrayList) allStickers.get(suggestion.emoji) : null;
                                if (!(newStickers2 == null || newStickers2.isEmpty())) {
                                    clear();
                                    if (!StickersSearchGridAdapter.this.emojiStickers.containsKey(newStickers2)) {
                                        StickersSearchGridAdapter.this.emojiStickers.put(newStickers2, suggestion.emoji);
                                        StickersSearchGridAdapter.this.emojiArrays.add(newStickers2);
                                    }
                                }
                            }
                        }
                    }
                    ArrayList<TL_messages_stickerSet> local = DataQuery.getInstance(EmojiView.this.currentAccount).getStickerSets(0);
                    length = local.size();
                    for (a = 0; a < length; a++) {
                        set = (TL_messages_stickerSet) local.get(a);
                        a2 = set.set.title.toLowerCase().indexOf(StickersSearchGridAdapter.this.searchQuery);
                        size = a2;
                        if (a2 >= 0) {
                            if (size == 0 || set.set.title.charAt(size - 1) == ' ') {
                                clear();
                                StickersSearchGridAdapter.this.localPacks.add(set);
                                StickersSearchGridAdapter.this.localPacksByName.put(set, Integer.valueOf(size));
                            }
                        } else if (set.set.short_name != null) {
                            a2 = set.set.short_name.toLowerCase().indexOf(StickersSearchGridAdapter.this.searchQuery);
                            size = a2;
                            if (a2 >= 0 && (size == 0 || set.set.short_name.charAt(size - 1) == ' ')) {
                                clear();
                                StickersSearchGridAdapter.this.localPacks.add(set);
                                StickersSearchGridAdapter.this.localPacksByShortName.put(set, Boolean.valueOf(true));
                            }
                        }
                    }
                    local = DataQuery.getInstance(EmojiView.this.currentAccount).getStickerSets(3);
                    length = local.size();
                    for (a = 0; a < length; a++) {
                        set = (TL_messages_stickerSet) local.get(a);
                        a2 = set.set.title.toLowerCase().indexOf(StickersSearchGridAdapter.this.searchQuery);
                        size = a2;
                        if (a2 >= 0) {
                            if (size == 0 || set.set.title.charAt(size - 1) == ' ') {
                                clear();
                                StickersSearchGridAdapter.this.localPacks.add(set);
                                StickersSearchGridAdapter.this.localPacksByName.put(set, Integer.valueOf(size));
                            }
                        } else if (set.set.short_name != null) {
                            a2 = set.set.short_name.toLowerCase().indexOf(StickersSearchGridAdapter.this.searchQuery);
                            size = a2;
                            if (a2 >= 0 && (size == 0 || set.set.short_name.charAt(size - 1) == ' ')) {
                                clear();
                                StickersSearchGridAdapter.this.localPacks.add(set);
                                StickersSearchGridAdapter.this.localPacksByShortName.put(set, Boolean.valueOf(true));
                            }
                        }
                    }
                    if (!((StickersSearchGridAdapter.this.localPacks.isEmpty() && StickersSearchGridAdapter.this.emojiStickers.isEmpty()) || EmojiView.this.stickersGridView.getAdapter() == EmojiView.this.stickersSearchGridAdapter)) {
                        EmojiView.this.stickersGridView.setAdapter(EmojiView.this.stickersSearchGridAdapter);
                    }
                    final TL_messages_searchStickerSets req = new TL_messages_searchStickerSets();
                    req.q = StickersSearchGridAdapter.this.searchQuery;
                    StickersSearchGridAdapter.this.reqId = ConnectionsManager.getInstance(EmojiView.this.currentAccount).sendRequest(req, new RequestDelegate() {
                        public void run(final TLObject response, TL_error error) {
                            if (response instanceof TL_messages_foundStickerSets) {
                                AndroidUtilities.runOnUIThread(new Runnable() {
                                    public void run() {
                                        if (req.q.equals(StickersSearchGridAdapter.this.searchQuery)) {
                                            AnonymousClass1.this.clear();
                                            EmojiView.this.progressDrawable.stopAnimation();
                                            StickersSearchGridAdapter.this.reqId = 0;
                                            if (EmojiView.this.stickersGridView.getAdapter() != EmojiView.this.stickersSearchGridAdapter) {
                                                EmojiView.this.stickersGridView.setAdapter(EmojiView.this.stickersSearchGridAdapter);
                                            }
                                            StickersSearchGridAdapter.this.serverPacks.addAll(response.sets);
                                            StickersSearchGridAdapter.this.notifyDataSetChanged();
                                        }
                                    }
                                });
                            }
                        }
                    });
                    if (Emoji.isValidEmoji(StickersSearchGridAdapter.this.searchQuery)) {
                        final TL_messages_getStickers req2 = new TL_messages_getStickers();
                        req2.emoticon = StickersSearchGridAdapter.this.searchQuery;
                        req2.hash = TtmlNode.ANONYMOUS_REGION_ID;
                        req2.exclude_featured = true;
                        StickersSearchGridAdapter.this.reqId2 = ConnectionsManager.getInstance(EmojiView.this.currentAccount).sendRequest(req2, new RequestDelegate() {
                            public void run(final TLObject response, TL_error error) {
                                AndroidUtilities.runOnUIThread(new Runnable() {
                                    public void run() {
                                        if (req2.emoticon.equals(StickersSearchGridAdapter.this.searchQuery)) {
                                            StickersSearchGridAdapter.this.reqId2 = 0;
                                            if (response instanceof TL_messages_stickers) {
                                                TL_messages_stickers res = response;
                                                int oldCount = emojiStickersArray.size();
                                                int size = res.stickers.size();
                                                for (int a = 0; a < size; a++) {
                                                    Document document = (Document) res.stickers.get(a);
                                                    if (emojiStickersMap.indexOfKey(document.id) < 0) {
                                                        emojiStickersArray.add(document);
                                                    }
                                                }
                                                if (oldCount != emojiStickersArray.size()) {
                                                    StickersSearchGridAdapter.this.emojiStickers.put(emojiStickersArray, StickersSearchGridAdapter.this.searchQuery);
                                                    if (oldCount == 0) {
                                                        StickersSearchGridAdapter.this.emojiArrays.add(emojiStickersArray);
                                                    }
                                                    StickersSearchGridAdapter.this.notifyDataSetChanged();
                                                }
                                            }
                                        }
                                    }
                                });
                            }
                        });
                    }
                    StickersSearchGridAdapter.this.notifyDataSetChanged();
                }
            }
        };
        private ArrayList<StickerSetCovered> serverPacks = new ArrayList();
        private int totalItems;

        public void onBindViewHolder(org.telegram.messenger.support.widget.RecyclerView.ViewHolder r1, int r2) {
            /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.DecodeException: Load method exception in method: org.telegram.ui.Components.EmojiView.StickersSearchGridAdapter.onBindViewHolder(org.telegram.messenger.support.widget.RecyclerView$ViewHolder, int):void
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
            r0 = r10.getItemViewType();
            r1 = 0;
            r2 = 1;
            r3 = 0;
            switch(r0) {
                case 0: goto L_0x01c5;
                case 1: goto L_0x0144;
                case 2: goto L_0x00c8;
                case 3: goto L_0x000c;
                default: goto L_0x000a;
            };
        L_0x000a:
            goto L_0x01f3;
        L_0x000c:
            r0 = r9.cache;
            r0 = r0.get(r11);
            r0 = (org.telegram.tgnet.TLRPC.StickerSetCovered) r0;
            r1 = r10.itemView;
            r1 = (org.telegram.ui.Cells.FeaturedStickerSetInfoCell) r1;
            r4 = org.telegram.ui.Components.EmojiView.this;
            r4 = r4.installingStickerSets;
            r5 = r0.set;
            r5 = r5.id;
            r4 = r4.indexOfKey(r5);
            if (r4 < 0) goto L_0x002a;
        L_0x0028:
            r4 = r2;
            goto L_0x002b;
        L_0x002a:
            r4 = r3;
        L_0x002b:
            r5 = org.telegram.ui.Components.EmojiView.this;
            r5 = r5.removingStickerSets;
            r6 = r0.set;
            r6 = r6.id;
            r5 = r5.indexOfKey(r6);
            if (r5 < 0) goto L_0x003d;
        L_0x003b:
            r5 = r2;
            goto L_0x003e;
        L_0x003d:
            r5 = r3;
        L_0x003e:
            if (r4 != 0) goto L_0x0042;
        L_0x0040:
            if (r5 == 0) goto L_0x006f;
        L_0x0042:
            if (r4 == 0) goto L_0x0059;
        L_0x0044:
            r6 = r1.isInstalled();
            if (r6 == 0) goto L_0x0059;
        L_0x004a:
            r6 = org.telegram.ui.Components.EmojiView.this;
            r6 = r6.installingStickerSets;
            r7 = r0.set;
            r7 = r7.id;
            r6.remove(r7);
            r4 = 0;
            goto L_0x006f;
        L_0x0059:
            if (r5 == 0) goto L_0x006f;
        L_0x005b:
            r6 = r1.isInstalled();
            if (r6 != 0) goto L_0x006f;
        L_0x0061:
            r6 = org.telegram.ui.Components.EmojiView.this;
            r6 = r6.removingStickerSets;
            r7 = r0.set;
            r7 = r7.id;
            r6.remove(r7);
            r5 = 0;
        L_0x006f:
            if (r4 != 0) goto L_0x0076;
        L_0x0071:
            if (r5 == 0) goto L_0x0074;
        L_0x0073:
            goto L_0x0076;
        L_0x0074:
            r2 = r3;
        L_0x0076:
            r1.setDrawProgress(r2);
            r2 = r9.searchQuery;
            r2 = android.text.TextUtils.isEmpty(r2);
            if (r2 == 0) goto L_0x0083;
        L_0x0081:
            r2 = -1;
            goto L_0x0091;
        L_0x0083:
            r2 = r0.set;
            r2 = r2.title;
            r2 = r2.toLowerCase();
            r6 = r9.searchQuery;
            r2 = r2.indexOf(r6);
        L_0x0091:
            if (r2 < 0) goto L_0x009e;
        L_0x0093:
            r6 = r9.searchQuery;
            r6 = r6.length();
            r1.setStickerSet(r0, r3, r2, r6);
            goto L_0x01f3;
        L_0x009e:
            r1.setStickerSet(r0, r3);
            r3 = r9.searchQuery;
            r3 = android.text.TextUtils.isEmpty(r3);
            if (r3 != 0) goto L_0x01f3;
        L_0x00a9:
            r3 = r0.set;
            r3 = r3.short_name;
            r3 = r3.toLowerCase();
            r6 = r9.searchQuery;
            r3 = r3.startsWith(r6);
            if (r3 == 0) goto L_0x01f3;
        L_0x00b9:
            r3 = r0.set;
            r3 = r3.short_name;
            r6 = r9.searchQuery;
            r6 = r6.length();
            r1.setUrl(r3, r6);
            goto L_0x01f3;
        L_0x00c8:
            r0 = r10.itemView;
            r0 = (org.telegram.ui.Cells.StickerSetNameCell) r0;
            r2 = r9.cache;
            r2 = r2.get(r11);
            r4 = r2 instanceof org.telegram.tgnet.TLRPC.TL_messages_stickerSet;
            if (r4 == 0) goto L_0x0130;
        L_0x00d6:
            r4 = r2;
            r4 = (org.telegram.tgnet.TLRPC.TL_messages_stickerSet) r4;
            r5 = r9.searchQuery;
            r5 = android.text.TextUtils.isEmpty(r5);
            if (r5 != 0) goto L_0x0102;
        L_0x00e1:
            r5 = r9.localPacksByShortName;
            r5 = r5.containsKey(r4);
            if (r5 == 0) goto L_0x0102;
        L_0x00e9:
            r1 = r4.set;
            if (r1 == 0) goto L_0x00f4;
        L_0x00ed:
            r1 = r4.set;
            r1 = r1.title;
            r0.setText(r1, r3);
        L_0x00f4:
            r1 = r4.set;
            r1 = r1.short_name;
            r3 = r9.searchQuery;
            r3 = r3.length();
            r0.setUrl(r1, r3);
            goto L_0x012e;
        L_0x0102:
            r5 = r9.localPacksByName;
            r5 = r5.get(r4);
            r5 = (java.lang.Integer) r5;
            r6 = r4.set;
            if (r6 == 0) goto L_0x012b;
        L_0x010e:
            if (r5 == 0) goto L_0x012b;
        L_0x0110:
            r6 = r4.set;
            r6 = r6.title;
            r7 = r5.intValue();
            r8 = r9.searchQuery;
            r8 = android.text.TextUtils.isEmpty(r8);
            if (r8 != 0) goto L_0x0127;
        L_0x0120:
            r8 = r9.searchQuery;
            r8 = r8.length();
            goto L_0x0128;
        L_0x0127:
            r8 = r3;
        L_0x0128:
            r0.setText(r6, r3, r7, r8);
        L_0x012b:
            r0.setUrl(r1, r3);
        L_0x012e:
            goto L_0x01f3;
        L_0x0130:
            r4 = r2 instanceof java.util.ArrayList;
            if (r4 == 0) goto L_0x01f3;
        L_0x0134:
            r4 = r9.emojiStickers;
            r4 = r4.get(r2);
            r4 = (java.lang.CharSequence) r4;
            r0.setText(r4, r3);
            r0.setUrl(r1, r3);
            goto L_0x01f3;
        L_0x0144:
            r0 = r10.itemView;
            r0 = (org.telegram.ui.Cells.EmptyCell) r0;
            r3 = r9.totalItems;
            r4 = 1118044160; // 0x42a40000 float:82.0 double:5.5238721E-315;
            if (r11 != r3) goto L_0x01bd;
        L_0x014e:
            r3 = r9.positionToRow;
            r5 = r11 + -1;
            r6 = -2147483648; // 0xffffffff80000000 float:-0.0 double:NaN;
            r3 = r3.get(r5, r6);
            if (r3 != r6) goto L_0x015e;
        L_0x015a:
            r0.setHeight(r2);
            goto L_0x01bc;
        L_0x015e:
            r5 = r9.rowStartPack;
            r5 = r5.get(r3);
            r6 = r5 instanceof org.telegram.tgnet.TLRPC.TL_messages_stickerSet;
            if (r6 == 0) goto L_0x016e;
        L_0x0168:
            r1 = r5;
            r1 = (org.telegram.tgnet.TLRPC.TL_messages_stickerSet) r1;
            r1 = r1.documents;
            goto L_0x0177;
        L_0x016e:
            r6 = r5 instanceof java.util.ArrayList;
            if (r6 == 0) goto L_0x0176;
        L_0x0172:
            r1 = r5;
            r1 = (java.util.ArrayList) r1;
            goto L_0x016d;
        L_0x0177:
            if (r1 != 0) goto L_0x017d;
            r0.setHeight(r2);
            goto L_0x01bc;
            r6 = r1.isEmpty();
            if (r6 == 0) goto L_0x018d;
            r2 = 1090519040; // 0x41000000 float:8.0 double:5.38787994E-315;
            r2 = org.telegram.messenger.AndroidUtilities.dp(r2);
            r0.setHeight(r2);
            goto L_0x01bc;
            r6 = org.telegram.ui.Components.EmojiView.this;
            r6 = r6.pager;
            r6 = r6.getHeight();
            r7 = r1.size();
            r7 = (float) r7;
            r8 = org.telegram.ui.Components.EmojiView.this;
            r8 = r8.stickersGridAdapter;
            r8 = r8.stickersPerRow;
            r8 = (float) r8;
            r7 = r7 / r8;
            r7 = (double) r7;
            r7 = java.lang.Math.ceil(r7);
            r7 = (int) r7;
            r4 = org.telegram.messenger.AndroidUtilities.dp(r4);
            r7 = r7 * r4;
            r4 = r6 - r7;
            if (r4 <= 0) goto L_0x01b9;
            r2 = r4;
            r0.setHeight(r2);
        L_0x01bc:
            goto L_0x01f3;
        L_0x01bd:
            r1 = org.telegram.messenger.AndroidUtilities.dp(r4);
            r0.setHeight(r1);
            goto L_0x01f3;
        L_0x01c5:
            r0 = r9.cache;
            r0 = r0.get(r11);
            r0 = (org.telegram.tgnet.TLRPC.Document) r0;
            r1 = r10.itemView;
            r1 = (org.telegram.ui.Cells.StickerEmojiCell) r1;
            r1.setSticker(r0, r3);
            r4 = org.telegram.ui.Components.EmojiView.this;
            r4 = r4.recentStickers;
            r4 = r4.contains(r0);
            if (r4 != 0) goto L_0x01ef;
            r4 = org.telegram.ui.Components.EmojiView.this;
            r4 = r4.favouriteStickers;
            r4 = r4.contains(r0);
            if (r4 == 0) goto L_0x01ed;
            goto L_0x01ef;
            r2 = r3;
            r1.setRecent(r2);
        L_0x01f3:
            return;
            */
            throw new UnsupportedOperationException("Method not decompiled: org.telegram.ui.Components.EmojiView.StickersSearchGridAdapter.onBindViewHolder(org.telegram.messenger.support.widget.RecyclerView$ViewHolder, int):void");
        }

        public StickersSearchGridAdapter(Context context) {
            this.context = context;
        }

        public boolean isEnabled(ViewHolder holder) {
            return false;
        }

        public int getItemCount() {
            if (this.totalItems != 1) {
                return this.totalItems + 1;
            }
            return 2;
        }

        public Object getItem(int i) {
            return this.cache.get(i);
        }

        public void search(String text) {
            if (this.reqId != 0) {
                ConnectionsManager.getInstance(EmojiView.this.currentAccount).cancelRequest(this.reqId, true);
                this.reqId = 0;
            }
            if (this.reqId2 != 0) {
                ConnectionsManager.getInstance(EmojiView.this.currentAccount).cancelRequest(this.reqId2, true);
                this.reqId2 = 0;
            }
            if (TextUtils.isEmpty(text)) {
                this.searchQuery = null;
                this.localPacks.clear();
                this.emojiStickers.clear();
                this.serverPacks.clear();
                if (EmojiView.this.stickersGridView.getAdapter() != EmojiView.this.stickersGridAdapter) {
                    EmojiView.this.stickersGridView.setAdapter(EmojiView.this.stickersGridAdapter);
                }
                notifyDataSetChanged();
            } else {
                this.searchQuery = text.toLowerCase();
            }
            AndroidUtilities.cancelRunOnUIThread(this.searchRunnable);
            AndroidUtilities.runOnUIThread(this.searchRunnable, 300);
        }

        public int getItemViewType(int position) {
            if (position == 0) {
                return 4;
            }
            if (position == 1 && this.totalItems == 1) {
                return 5;
            }
            Object object = this.cache.get(position);
            if (object == null) {
                return 1;
            }
            if (object instanceof Document) {
                return 0;
            }
            if (object instanceof StickerSetCovered) {
                return 3;
            }
            return 2;
        }

        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            StickersSearchGridAdapter stickersSearchGridAdapter = this;
            View view = null;
            switch (viewType) {
                case 0:
                    view = new StickerEmojiCell(stickersSearchGridAdapter.context) {
                        public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
                            super.onMeasure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(82.0f), 1073741824));
                        }
                    };
                    break;
                case 1:
                    view = new EmptyCell(stickersSearchGridAdapter.context);
                    break;
                case 2:
                    view = new StickerSetNameCell(stickersSearchGridAdapter.context);
                    break;
                case 3:
                    view = new FeaturedStickerSetInfoCell(stickersSearchGridAdapter.context, 17);
                    ((FeaturedStickerSetInfoCell) view).setAddOnClickListener(new OnClickListener() {
                        public void onClick(View v) {
                            FeaturedStickerSetInfoCell parent = (FeaturedStickerSetInfoCell) v.getParent();
                            StickerSetCovered pack = parent.getStickerSet();
                            if (EmojiView.this.installingStickerSets.indexOfKey(pack.set.id) < 0) {
                                if (EmojiView.this.removingStickerSets.indexOfKey(pack.set.id) < 0) {
                                    if (parent.isInstalled()) {
                                        EmojiView.this.removingStickerSets.put(pack.set.id, pack);
                                        EmojiView.this.listener.onStickerSetRemove(parent.getStickerSet());
                                    } else {
                                        EmojiView.this.installingStickerSets.put(pack.set.id, pack);
                                        EmojiView.this.listener.onStickerSetAdd(parent.getStickerSet());
                                    }
                                    parent.setDrawProgress(true);
                                }
                            }
                        }
                    });
                    break;
                case 4:
                    view = new View(stickersSearchGridAdapter.context);
                    view.setLayoutParams(new LayoutParams(-1, EmojiView.this.searchFieldHeight));
                    break;
                case 5:
                    View frameLayout = new FrameLayout(stickersSearchGridAdapter.context) {
                        protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
                            super.onMeasure(widthMeasureSpec, MeasureSpec.makeMeasureSpec((int) (((float) (((EmojiView.this.stickersGridView.getMeasuredHeight() - EmojiView.this.searchFieldHeight) - AndroidUtilities.dp(8.0f)) / 3)) * 1.7f), 1073741824));
                        }
                    };
                    ImageView imageView = new ImageView(stickersSearchGridAdapter.context);
                    imageView.setScaleType(ScaleType.CENTER);
                    imageView.setImageResource(R.drawable.stickers_none);
                    imageView.setColorFilter(new PorterDuffColorFilter(Theme.getColor(Theme.key_chat_emojiPanelEmptyText), Mode.MULTIPLY));
                    frameLayout.addView(imageView, LayoutHelper.createFrame(-2, -2.0f, 17, 0.0f, 0.0f, 0.0f, 48.0f));
                    TextView textView = new TextView(stickersSearchGridAdapter.context);
                    textView.setText(LocaleController.getString("NoStickersFound", R.string.NoStickersFound));
                    textView.setTextSize(1, 18.0f);
                    textView.setTextColor(Theme.getColor(Theme.key_chat_emojiPanelEmptyText));
                    textView.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
                    frameLayout.addView(textView, LayoutHelper.createFrame(-2, -2.0f, 17, 0.0f, 30.0f, 0.0f, 0.0f));
                    view = frameLayout;
                    view.setLayoutParams(new LayoutParams(-1, -2));
                    break;
                default:
                    break;
            }
            return new Holder(view);
        }

        public void notifyDataSetChanged() {
            this.rowStartPack.clear();
            this.positionToRow.clear();
            this.cache.clear();
            this.positionsToSets.clear();
            this.totalItems = 0;
            int startRow = 0;
            int serverSize = this.serverPacks.size();
            int localSize = this.localPacks.size();
            int emojiSize = this.emojiArrays.size();
            for (int a = -1; a < (serverSize + localSize) + emojiSize; a++) {
                Object pack = null;
                int i;
                if (a == -1) {
                    SparseArray sparseArray = r0.cache;
                    i = r0.totalItems;
                    r0.totalItems = i + 1;
                    sparseArray.put(i, "search");
                    startRow++;
                } else {
                    ArrayList<Document> documents;
                    int idx = a;
                    if (idx < localSize) {
                        TL_messages_stickerSet set = (TL_messages_stickerSet) r0.localPacks.get(idx);
                        documents = set.documents;
                        pack = set;
                    } else {
                        idx -= localSize;
                        if (idx < emojiSize) {
                            documents = (ArrayList) r0.emojiArrays.get(idx);
                        } else {
                            StickerSetCovered set2 = (StickerSetCovered) r0.serverPacks.get(idx - emojiSize);
                            documents = set2.covers;
                            pack = set2;
                        }
                    }
                    if (!documents.isEmpty()) {
                        idx = (int) Math.ceil((double) (((float) documents.size()) / ((float) EmojiView.this.stickersGridAdapter.stickersPerRow)));
                        if (pack != null) {
                            r0.cache.put(r0.totalItems, pack);
                        } else {
                            r0.cache.put(r0.totalItems, documents);
                        }
                        if (a >= localSize && (pack instanceof StickerSetCovered)) {
                            r0.positionsToSets.put(r0.totalItems, (StickerSetCovered) pack);
                        }
                        r0.positionToRow.put(r0.totalItems, startRow);
                        int size = documents.size();
                        for (i = 0; i < size; i++) {
                            r0.cache.put((1 + i) + r0.totalItems, documents.get(i));
                            r0.positionToRow.put((1 + i) + r0.totalItems, (startRow + 1) + (i / EmojiView.this.stickersGridAdapter.stickersPerRow));
                            if (a >= localSize && (pack instanceof StickerSetCovered)) {
                                r0.positionsToSets.put((1 + i) + r0.totalItems, (StickerSetCovered) pack);
                            }
                        }
                        for (int b = 0; b < idx + 1; b++) {
                            if (pack != null) {
                                r0.rowStartPack.put(startRow + b, pack);
                            } else {
                                r0.rowStartPack.put(startRow + b, documents);
                            }
                        }
                        r0.totalItems += (EmojiView.this.stickersGridAdapter.stickersPerRow * idx) + 1;
                        startRow += idx + 1;
                    }
                }
            }
            super.notifyDataSetChanged();
        }
    }

    private class TrendingGridAdapter extends SelectionAdapter {
        private SparseArray<Object> cache = new SparseArray();
        private Context context;
        private SparseArray<StickerSetCovered> positionsToSets = new SparseArray();
        private ArrayList<StickerSetCovered> sets = new ArrayList();
        private int stickersPerRow;
        private int totalItems;

        public TrendingGridAdapter(Context context) {
            this.context = context;
        }

        public int getItemCount() {
            return this.totalItems;
        }

        public Object getItem(int i) {
            return this.cache.get(i);
        }

        public boolean isEnabled(ViewHolder holder) {
            return false;
        }

        public int getItemViewType(int position) {
            Object object = this.cache.get(position);
            if (object == null) {
                return 1;
            }
            if (object instanceof Document) {
                return 0;
            }
            return 2;
        }

        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = null;
            switch (viewType) {
                case 0:
                    view = new StickerEmojiCell(this.context) {
                        public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
                            super.onMeasure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(82.0f), 1073741824));
                        }
                    };
                    break;
                case 1:
                    view = new EmptyCell(this.context);
                    break;
                case 2:
                    view = new FeaturedStickerSetInfoCell(this.context, 17);
                    ((FeaturedStickerSetInfoCell) view).setAddOnClickListener(new OnClickListener() {
                        public void onClick(View v) {
                            FeaturedStickerSetInfoCell parent = (FeaturedStickerSetInfoCell) v.getParent();
                            StickerSetCovered pack = parent.getStickerSet();
                            if (EmojiView.this.installingStickerSets.indexOfKey(pack.set.id) < 0) {
                                if (EmojiView.this.removingStickerSets.indexOfKey(pack.set.id) < 0) {
                                    if (parent.isInstalled()) {
                                        EmojiView.this.removingStickerSets.put(pack.set.id, pack);
                                        EmojiView.this.listener.onStickerSetRemove(parent.getStickerSet());
                                    } else {
                                        EmojiView.this.installingStickerSets.put(pack.set.id, pack);
                                        EmojiView.this.listener.onStickerSetAdd(parent.getStickerSet());
                                    }
                                    parent.setDrawProgress(true);
                                }
                            }
                        }
                    });
                    break;
                default:
                    break;
            }
            return new Holder(view);
        }

        public void onBindViewHolder(ViewHolder holder, int position) {
            boolean z = false;
            switch (holder.getItemViewType()) {
                case 0:
                    ((StickerEmojiCell) holder.itemView).setSticker((Document) this.cache.get(position), false);
                    return;
                case 1:
                    ((EmptyCell) holder.itemView).setHeight(AndroidUtilities.dp(82.0f));
                    return;
                case 2:
                    ArrayList<Long> unreadStickers = DataQuery.getInstance(EmojiView.this.currentAccount).getUnreadStickerSets();
                    StickerSetCovered stickerSetCovered = (StickerSetCovered) this.sets.get(((Integer) this.cache.get(position)).intValue());
                    boolean unread = unreadStickers != null && unreadStickers.contains(Long.valueOf(stickerSetCovered.set.id));
                    FeaturedStickerSetInfoCell cell = holder.itemView;
                    cell.setStickerSet(stickerSetCovered, unread);
                    if (unread) {
                        DataQuery.getInstance(EmojiView.this.currentAccount).markFaturedStickersByIdAsRead(stickerSetCovered.set.id);
                    }
                    boolean installing = EmojiView.this.installingStickerSets.indexOfKey(stickerSetCovered.set.id) >= 0;
                    boolean removing = EmojiView.this.removingStickerSets.indexOfKey(stickerSetCovered.set.id) >= 0;
                    if (installing || removing) {
                        if (installing && cell.isInstalled()) {
                            EmojiView.this.installingStickerSets.remove(stickerSetCovered.set.id);
                            installing = false;
                        } else if (removing && !cell.isInstalled()) {
                            EmojiView.this.removingStickerSets.remove(stickerSetCovered.set.id);
                            removing = false;
                        }
                    }
                    if (!installing) {
                        if (!removing) {
                            cell.setDrawProgress(z);
                            return;
                        }
                    }
                    z = true;
                    cell.setDrawProgress(z);
                    return;
                default:
                    return;
            }
        }

        public void notifyDataSetChanged() {
            int width = EmojiView.this.getMeasuredWidth();
            if (width == 0) {
                if (AndroidUtilities.isTablet()) {
                    int width2 = AndroidUtilities.displaySize.x;
                    int leftSide = (width2 * 35) / 100;
                    if (leftSide < AndroidUtilities.dp(320.0f)) {
                        leftSide = AndroidUtilities.dp(320.0f);
                    }
                    width = width2 - leftSide;
                } else {
                    width = AndroidUtilities.displaySize.x;
                }
                if (width == 0) {
                    width = 1080;
                }
            }
            this.stickersPerRow = Math.max(1, width / AndroidUtilities.dp(72.0f));
            EmojiView.this.trendingLayoutManager.setSpanCount(this.stickersPerRow);
            if (!EmojiView.this.trendingLoaded) {
                this.cache.clear();
                this.positionsToSets.clear();
                this.sets.clear();
                this.totalItems = 0;
                ArrayList<StickerSetCovered> packs = DataQuery.getInstance(EmojiView.this.currentAccount).getFeaturedStickerSets();
                int num = 0;
                for (int a = 0; a < packs.size(); a++) {
                    StickerSetCovered pack = (StickerSetCovered) packs.get(a);
                    if (!DataQuery.getInstance(EmojiView.this.currentAccount).isStickerPackInstalled(pack.set.id)) {
                        if (!pack.covers.isEmpty() || pack.cover != null) {
                            int count;
                            this.sets.add(pack);
                            this.positionsToSets.put(this.totalItems, pack);
                            SparseArray sparseArray = this.cache;
                            int i = this.totalItems;
                            this.totalItems = i + 1;
                            int num2 = num + 1;
                            sparseArray.put(i, Integer.valueOf(num));
                            num = this.totalItems / this.stickersPerRow;
                            if (pack.covers.isEmpty()) {
                                count = 1;
                                this.cache.put(this.totalItems, pack.cover);
                            } else {
                                count = (int) Math.ceil((double) (((float) pack.covers.size()) / ((float) this.stickersPerRow)));
                                for (i = 0; i < pack.covers.size(); i++) {
                                    this.cache.put(this.totalItems + i, pack.covers.get(i));
                                }
                            }
                            for (i = 0; i < this.stickersPerRow * count; i++) {
                                this.positionsToSets.put(this.totalItems + i, pack);
                            }
                            this.totalItems += this.stickersPerRow * count;
                            num = num2;
                        }
                    }
                }
                if (this.totalItems != 0) {
                    EmojiView.this.trendingLoaded = true;
                    EmojiView.this.featuredStickersHash = DataQuery.getInstance(EmojiView.this.currentAccount).getFeaturesStickersHashWithoutUnread();
                }
                super.notifyDataSetChanged();
            }
        }
    }

    static {
        Field f = null;
        try {
            f = PopupWindow.class.getDeclaredField("mOnScrollChangedListener");
            f.setAccessible(true);
        } catch (NoSuchFieldException e) {
        }
        superListenerField = f;
    }

    private static String addColorToCode(String code, String color) {
        String end = null;
        int lenght = code.length();
        if (lenght > 2 && code.charAt(code.length() - 2) == '‍') {
            end = code.substring(code.length() - 2);
            code = code.substring(0, code.length() - 2);
        } else if (lenght > 3 && code.charAt(code.length() - 3) == '‍') {
            end = code.substring(code.length() - 3);
            code = code.substring(0, code.length() - 3);
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(code);
        stringBuilder.append(color);
        code = stringBuilder.toString();
        if (end == null) {
            return code;
        }
        stringBuilder = new StringBuilder();
        stringBuilder.append(code);
        stringBuilder.append(end);
        return stringBuilder.toString();
    }

    public void addEmojiToRecent(String code) {
        if (Emoji.isValidEmoji(code)) {
            Emoji.addRecentEmoji(code);
            if (!(getVisibility() == 0 && this.pager.getCurrentItem() == 0)) {
                Emoji.sortEmoji();
            }
            Emoji.saveRecentEmoji();
            ((EmojiGridAdapter) this.adapters.get(0)).notifyDataSetChanged();
        }
    }

    public EmojiView(boolean needStickers, boolean needGif, Context context, ChatFull chatFull) {
        boolean z = needGif;
        Context context2 = context;
        super(context2);
        this.stickersDrawable = context.getResources().getDrawable(R.drawable.ic_smiles2_stickers);
        Theme.setDrawableColorByKey(this.stickersDrawable, Theme.key_chat_emojiPanelIcon);
        this.icons = new Drawable[]{Theme.createEmojiIconSelectorDrawable(context2, R.drawable.ic_smiles2_recent, Theme.getColor(Theme.key_chat_emojiPanelIcon), Theme.getColor(Theme.key_chat_emojiPanelIconSelected)), Theme.createEmojiIconSelectorDrawable(context2, R.drawable.ic_smiles2_smile, Theme.getColor(Theme.key_chat_emojiPanelIcon), Theme.getColor(Theme.key_chat_emojiPanelIconSelected)), Theme.createEmojiIconSelectorDrawable(context2, R.drawable.ic_smiles2_nature, Theme.getColor(Theme.key_chat_emojiPanelIcon), Theme.getColor(Theme.key_chat_emojiPanelIconSelected)), Theme.createEmojiIconSelectorDrawable(context2, R.drawable.ic_smiles2_food, Theme.getColor(Theme.key_chat_emojiPanelIcon), Theme.getColor(Theme.key_chat_emojiPanelIconSelected)), Theme.createEmojiIconSelectorDrawable(context2, R.drawable.ic_smiles2_car, Theme.getColor(Theme.key_chat_emojiPanelIcon), Theme.getColor(Theme.key_chat_emojiPanelIconSelected)), Theme.createEmojiIconSelectorDrawable(context2, R.drawable.ic_smiles2_objects, Theme.getColor(Theme.key_chat_emojiPanelIcon), Theme.getColor(Theme.key_chat_emojiPanelIconSelected)), this.stickersDrawable};
        this.showGifs = z;
        this.info = chatFull;
        this.dotPaint = new Paint(1);
        this.dotPaint.setColor(Theme.getColor(Theme.key_chat_emojiPanelNewTrending));
        if (VERSION.SDK_INT >= 21) {
            r0.outlineProvider = new ViewOutlineProvider() {
                @TargetApi(21)
                public void getOutline(View view, Outline outline) {
                    outline.setRoundRect(view.getPaddingLeft(), view.getPaddingTop(), view.getMeasuredWidth() - view.getPaddingRight(), view.getMeasuredHeight() - view.getPaddingBottom(), (float) AndroidUtilities.dp(6.0f));
                }
            };
        }
        for (int i = 0; i < EmojiData.dataColored.length + 1; i++) {
            GridView gridView = new GridView(context2);
            if (AndroidUtilities.isTablet()) {
                gridView.setColumnWidth(AndroidUtilities.dp(60.0f));
            } else {
                gridView.setColumnWidth(AndroidUtilities.dp(45.0f));
            }
            gridView.setNumColumns(-1);
            EmojiGridAdapter emojiGridAdapter = new EmojiGridAdapter(i - 1);
            gridView.setAdapter(emojiGridAdapter);
            r0.adapters.add(emojiGridAdapter);
            r0.emojiGrids.add(gridView);
            FrameLayout frameLayout = new FrameLayout(context2);
            frameLayout.addView(gridView, LayoutHelper.createFrame(-1, -1.0f, 51, 0.0f, 48.0f, 0.0f, 0.0f));
            r0.views.add(frameLayout);
        }
        if (needStickers) {
            r0.stickersWrap = new FrameLayout(context2);
            DataQuery.getInstance(r0.currentAccount).checkStickers(0);
            DataQuery.getInstance(r0.currentAccount).checkFeaturedStickers();
            r0.stickersGridView = new RecyclerListView(context2) {
                boolean ignoreLayout;

                public boolean onInterceptTouchEvent(MotionEvent event) {
                    boolean result = StickerPreviewViewer.getInstance().onInterceptTouchEvent(event, EmojiView.this.stickersGridView, EmojiView.this.getMeasuredHeight(), EmojiView.this.stickerPreviewViewerDelegate);
                    if (!super.onInterceptTouchEvent(event)) {
                        if (!result) {
                            return false;
                        }
                    }
                    return true;
                }

                public void setVisibility(int visibility) {
                    if ((EmojiView.this.gifsGridView == null || EmojiView.this.gifsGridView.getVisibility() != 0) && (EmojiView.this.trendingGridView == null || EmojiView.this.trendingGridView.getVisibility() != 0)) {
                        super.setVisibility(visibility);
                    } else {
                        super.setVisibility(8);
                    }
                }

                protected void onLayout(boolean changed, int l, int t, int r, int b) {
                    if (EmojiView.this.firstAttach && EmojiView.this.stickersGridAdapter.getItemCount() > 0) {
                        this.ignoreLayout = true;
                        EmojiView.this.stickersLayoutManager.scrollToPositionWithOffset(1, 0);
                        EmojiView.this.firstAttach = false;
                        this.ignoreLayout = false;
                    }
                    super.onLayout(changed, l, t, r, b);
                    EmojiView.this.checkSearchFieldScroll();
                }

                public void requestLayout() {
                    if (!this.ignoreLayout) {
                        super.requestLayout();
                    }
                }
            };
            RecyclerListView recyclerListView = r0.stickersGridView;
            LayoutManager gridLayoutManager = new GridLayoutManager(context2, 5);
            r0.stickersLayoutManager = gridLayoutManager;
            recyclerListView.setLayoutManager(gridLayoutManager);
            r0.stickersLayoutManager.setSpanSizeLookup(new SpanSizeLookup() {
                public int getSpanSize(int position) {
                    if (EmojiView.this.stickersGridView.getAdapter() == EmojiView.this.stickersGridAdapter) {
                        if (position == 0) {
                            return EmojiView.this.stickersGridAdapter.stickersPerRow;
                        }
                        if (position == EmojiView.this.stickersGridAdapter.totalItems || (EmojiView.this.stickersGridAdapter.cache.get(position) != null && !(EmojiView.this.stickersGridAdapter.cache.get(position) instanceof Document))) {
                            return EmojiView.this.stickersGridAdapter.stickersPerRow;
                        }
                        return 1;
                    } else if (position == EmojiView.this.stickersSearchGridAdapter.totalItems || (EmojiView.this.stickersSearchGridAdapter.cache.get(position) != null && !(EmojiView.this.stickersSearchGridAdapter.cache.get(position) instanceof Document))) {
                        return EmojiView.this.stickersGridAdapter.stickersPerRow;
                    } else {
                        return 1;
                    }
                }
            });
            r0.stickersGridView.setPadding(0, AndroidUtilities.dp(52.0f), 0, 0);
            r0.stickersGridView.setClipToPadding(false);
            r0.views.add(r0.stickersWrap);
            r0.stickersSearchGridAdapter = new StickersSearchGridAdapter(context2);
            RecyclerListView recyclerListView2 = r0.stickersGridView;
            Adapter stickersGridAdapter = new StickersGridAdapter(context2);
            r0.stickersGridAdapter = stickersGridAdapter;
            recyclerListView2.setAdapter(stickersGridAdapter);
            r0.stickersGridView.setOnTouchListener(new OnTouchListener() {
                public boolean onTouch(View v, MotionEvent event) {
                    return StickerPreviewViewer.getInstance().onTouch(event, EmojiView.this.stickersGridView, EmojiView.this.getMeasuredHeight(), EmojiView.this.stickersOnItemClickListener, EmojiView.this.stickerPreviewViewerDelegate);
                }
            });
            r0.stickersOnItemClickListener = new OnItemClickListener() {
                public void onItemClick(View view, int position) {
                    if (EmojiView.this.stickersGridView.getAdapter() == EmojiView.this.stickersSearchGridAdapter) {
                        StickerSetCovered pack = (StickerSetCovered) EmojiView.this.stickersSearchGridAdapter.positionsToSets.get(position);
                        if (pack != null) {
                            EmojiView.this.listener.onShowStickerSet(pack.set, null);
                            return;
                        }
                    }
                    if (view instanceof StickerEmojiCell) {
                        StickerPreviewViewer.getInstance().reset();
                        StickerEmojiCell cell = (StickerEmojiCell) view;
                        if (!cell.isDisabled()) {
                            cell.disable();
                            EmojiView.this.listener.onStickerSelected(cell.getSticker());
                        }
                    }
                }
            };
            r0.stickersGridView.setOnItemClickListener(r0.stickersOnItemClickListener);
            r0.stickersGridView.setGlowColor(Theme.getColor(Theme.key_chat_emojiPanelBackground));
            r0.stickersWrap.addView(r0.stickersGridView);
            r0.searchEditTextContainer = new FrameLayout(context2);
            r0.searchEditTextContainer.setBackgroundColor(Theme.getColor(Theme.key_chat_emojiPanelBackground));
            r0.stickersWrap.addView(r0.searchEditTextContainer, new LayoutParams(-1, r0.searchFieldHeight));
            r0.searchBackground = new View(context2);
            r0.searchBackground.setBackgroundDrawable(Theme.createRoundRectDrawable(AndroidUtilities.dp(3.0f), Theme.getColor(Theme.key_chat_emojiSearchBackground)));
            r0.searchEditTextContainer.addView(r0.searchBackground, LayoutHelper.createFrame(-1, 36.0f, 51, 14.0f, 14.0f, 14.0f, 0.0f));
            r0.searchIconImageView = new ImageView(context2);
            r0.searchIconImageView.setScaleType(ScaleType.CENTER);
            r0.searchIconImageView.setImageResource(R.drawable.sticker_search);
            r0.searchIconImageView.setColorFilter(new PorterDuffColorFilter(Theme.getColor(Theme.key_chat_emojiPanelIcon), Mode.MULTIPLY));
            r0.searchEditTextContainer.addView(r0.searchIconImageView, LayoutHelper.createFrame(36, 36.0f, 51, 14.0f, 14.0f, 0.0f, 0.0f));
            r0.clearSearchImageView = new ImageView(context2);
            r0.clearSearchImageView.setScaleType(ScaleType.CENTER);
            ImageView imageView = r0.clearSearchImageView;
            Drawable closeProgressDrawable2 = new CloseProgressDrawable2();
            r0.progressDrawable = closeProgressDrawable2;
            imageView.setImageDrawable(closeProgressDrawable2);
            r0.clearSearchImageView.setScaleX(0.1f);
            r0.clearSearchImageView.setScaleY(0.1f);
            r0.clearSearchImageView.setAlpha(0.0f);
            r0.clearSearchImageView.setColorFilter(new PorterDuffColorFilter(Theme.getColor(Theme.key_chat_emojiPanelIcon), Mode.MULTIPLY));
            r0.searchEditTextContainer.addView(r0.clearSearchImageView, LayoutHelper.createFrame(36, 36.0f, 53, 14.0f, 14.0f, 14.0f, 0.0f));
            r0.clearSearchImageView.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    EmojiView.this.searchEditText.setText(TtmlNode.ANONYMOUS_REGION_ID);
                    AndroidUtilities.showKeyboard(EmojiView.this.searchEditText);
                }
            });
            r0.searchEditText = new EditTextBoldCursor(context2) {
                public boolean onTouchEvent(MotionEvent event) {
                    if (event.getAction() == 0) {
                        if (!EmojiView.this.listener.isSearchOpened()) {
                            if (EmojiView.this.searchAnimation != null) {
                                EmojiView.this.searchAnimation.cancel();
                                EmojiView.this.searchAnimation = null;
                            }
                            if (EmojiView.this.listener == null || !EmojiView.this.listener.isExpanded()) {
                                EmojiView.this.searchEditTextContainer.setTranslationY((float) AndroidUtilities.dp(0.0f));
                                EmojiView.this.stickersTab.setTranslationY((float) (-AndroidUtilities.dp(47.0f)));
                                EmojiView.this.stickersGridView.setPadding(0, AndroidUtilities.dp(4.0f), 0, 0);
                            } else {
                                EmojiView.this.searchAnimation = new AnimatorSet();
                                AnimatorSet access$3400 = EmojiView.this.searchAnimation;
                                r5 = new Animator[3];
                                r5[0] = ObjectAnimator.ofFloat(EmojiView.this.stickersTab, "translationY", new float[]{(float) (-AndroidUtilities.dp(47.0f))});
                                r5[1] = ObjectAnimator.ofFloat(EmojiView.this.stickersGridView, "translationY", new float[]{(float) (-AndroidUtilities.dp(48.0f))});
                                r5[2] = ObjectAnimator.ofFloat(EmojiView.this.searchEditTextContainer, "translationY", new float[]{(float) AndroidUtilities.dp(0.0f)});
                                access$3400.playTogether(r5);
                                EmojiView.this.searchAnimation.setDuration(200);
                                EmojiView.this.searchAnimation.setInterpolator(CubicBezierInterpolator.EASE_OUT_QUINT);
                                EmojiView.this.searchAnimation.addListener(new AnimatorListenerAdapter() {
                                    public void onAnimationEnd(Animator animation) {
                                        if (animation.equals(EmojiView.this.searchAnimation)) {
                                            EmojiView.this.stickersGridView.setTranslationY(0.0f);
                                            EmojiView.this.stickersGridView.setPadding(0, AndroidUtilities.dp(4.0f), 0, 0);
                                            EmojiView.this.searchAnimation = null;
                                        }
                                    }

                                    public void onAnimationCancel(Animator animation) {
                                        if (animation.equals(EmojiView.this.searchAnimation)) {
                                            EmojiView.this.searchAnimation = null;
                                        }
                                    }
                                });
                                EmojiView.this.searchAnimation.start();
                            }
                        }
                        EmojiView.this.listener.onSearchOpenClose(true);
                        EmojiView.this.searchEditText.requestFocus();
                        AndroidUtilities.showKeyboard(EmojiView.this.searchEditText);
                    }
                    return super.onTouchEvent(event);
                }
            };
            r0.searchEditText.setTextSize(1, 16.0f);
            r0.searchEditText.setHintTextColor(Theme.getColor(Theme.key_windowBackgroundWhiteHintText));
            r0.searchEditText.setTextColor(Theme.getColor(Theme.key_windowBackgroundWhiteBlackText));
            r0.searchEditText.setBackgroundDrawable(null);
            r0.searchEditText.setPadding(0, 0, 0, 0);
            r0.searchEditText.setMaxLines(1);
            r0.searchEditText.setLines(1);
            r0.searchEditText.setSingleLine(true);
            r0.searchEditText.setImeOptions(268435459);
            r0.searchEditText.setHint(LocaleController.getString("SearchStickersHint", R.string.SearchStickersHint));
            r0.searchEditText.setCursorColor(Theme.getColor(Theme.key_featuredStickers_addedIcon));
            r0.searchEditText.setCursorSize(AndroidUtilities.dp(20.0f));
            r0.searchEditText.setCursorWidth(1.5f);
            r0.searchEditTextContainer.addView(r0.searchEditText, LayoutHelper.createFrame(-1, 40.0f, 51, 46.0f, 12.0f, 46.0f, 0.0f));
            r0.searchEditText.addTextChangedListener(new TextWatcher() {
                public void afterTextChanged(android.text.Editable r1) {
                    /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.DecodeException: Load method exception in method: org.telegram.ui.Components.EmojiView.10.afterTextChanged(android.text.Editable):void
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
                    r0 = org.telegram.ui.Components.EmojiView.this;
                    r0 = r0.searchEditText;
                    r0 = r0.length();
                    r1 = 0;
                    r2 = 1;
                    if (r0 <= 0) goto L_0x0010;
                L_0x000e:
                    r0 = r2;
                    goto L_0x0011;
                L_0x0010:
                    r0 = r1;
                L_0x0011:
                    r3 = org.telegram.ui.Components.EmojiView.this;
                    r3 = r3.clearSearchImageView;
                    r3 = r3.getAlpha();
                    r4 = 0;
                    r3 = (r3 > r4 ? 1 : (r3 == r4 ? 0 : -1));
                    if (r3 == 0) goto L_0x0022;
                L_0x0020:
                    r1 = r2;
                L_0x0022:
                    if (r0 == r1) goto L_0x0057;
                L_0x0024:
                    r2 = org.telegram.ui.Components.EmojiView.this;
                    r2 = r2.clearSearchImageView;
                    r2 = r2.animate();
                    r3 = 1065353216; // 0x3f800000 float:1.0 double:5.263544247E-315;
                    if (r0 == 0) goto L_0x0034;
                L_0x0032:
                    r4 = r3;
                    goto L_0x0035;
                    r2 = r2.alpha(r4);
                    r4 = 150; // 0x96 float:2.1E-43 double:7.4E-322;
                    r2 = r2.setDuration(r4);
                    r4 = 1036831949; // 0x3dcccccd float:0.1 double:5.122630465E-315;
                    if (r0 == 0) goto L_0x0046;
                    r5 = r3;
                    goto L_0x0048;
                    r5 = r4;
                    r2 = r2.scaleX(r5);
                    if (r0 == 0) goto L_0x004f;
                    goto L_0x0050;
                    r3 = r4;
                    r2 = r2.scaleY(r3);
                    r2.start();
                L_0x0057:
                    r2 = org.telegram.ui.Components.EmojiView.this;
                    r2 = r2.stickersSearchGridAdapter;
                    r3 = org.telegram.ui.Components.EmojiView.this;
                    r3 = r3.searchEditText;
                    r3 = r3.getText();
                    r3 = r3.toString();
                    r2.search(r3);
                    return;
                    */
                    throw new UnsupportedOperationException("Method not decompiled: org.telegram.ui.Components.EmojiView.10.afterTextChanged(android.text.Editable):void");
                }

                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                public void onTextChanged(CharSequence s, int start, int before, int count) {
                }
            });
            r0.trendingGridView = new RecyclerListView(context2);
            r0.trendingGridView.setItemAnimator(null);
            r0.trendingGridView.setLayoutAnimation(null);
            recyclerListView2 = r0.trendingGridView;
            LayoutManager anonymousClass11 = new GridLayoutManager(context2, 5) {
                public boolean supportsPredictiveItemAnimations() {
                    return false;
                }
            };
            r0.trendingLayoutManager = anonymousClass11;
            recyclerListView2.setLayoutManager(anonymousClass11);
            r0.trendingLayoutManager.setSpanSizeLookup(new SpanSizeLookup() {
                public int getSpanSize(int position) {
                    if (!(EmojiView.this.trendingGridAdapter.cache.get(position) instanceof Integer)) {
                        if (position != EmojiView.this.trendingGridAdapter.totalItems) {
                            return 1;
                        }
                    }
                    return EmojiView.this.trendingGridAdapter.stickersPerRow;
                }
            });
            r0.trendingGridView.setOnScrollListener(new OnScrollListener() {
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    EmojiView.this.checkStickersTabY(recyclerView, dy);
                }
            });
            r0.trendingGridView.setClipToPadding(false);
            r0.trendingGridView.setPadding(0, AndroidUtilities.dp(48.0f), 0, 0);
            recyclerListView2 = r0.trendingGridView;
            Adapter trendingGridAdapter = new TrendingGridAdapter(context2);
            r0.trendingGridAdapter = trendingGridAdapter;
            recyclerListView2.setAdapter(trendingGridAdapter);
            r0.trendingGridView.setOnItemClickListener(new OnItemClickListener() {
                public void onItemClick(View view, int position) {
                    StickerSetCovered pack = (StickerSetCovered) EmojiView.this.trendingGridAdapter.positionsToSets.get(position);
                    if (pack != null) {
                        EmojiView.this.listener.onShowStickerSet(pack.set, null);
                    }
                }
            });
            r0.trendingGridAdapter.notifyDataSetChanged();
            r0.trendingGridView.setGlowColor(Theme.getColor(Theme.key_chat_emojiPanelBackground));
            r0.trendingGridView.setVisibility(8);
            r0.stickersWrap.addView(r0.trendingGridView);
            if (z) {
                r0.gifsGridView = new RecyclerListView(context2);
                r0.gifsGridView.setClipToPadding(false);
                r0.gifsGridView.setPadding(0, AndroidUtilities.dp(48.0f), 0, 0);
                recyclerListView2 = r0.gifsGridView;
                anonymousClass11 = new ExtendedGridLayoutManager(context2, 100) {
                    private Size size = new Size();

                    protected Size getSizeForItem(int i) {
                        Document document = (Document) EmojiView.this.recentGifs.get(i);
                        Size size = this.size;
                        float f = 100.0f;
                        float f2 = (document.thumb == null || document.thumb.w == 0) ? 100.0f : (float) document.thumb.w;
                        size.width = f2;
                        size = this.size;
                        if (!(document.thumb == null || document.thumb.h == 0)) {
                            f = (float) document.thumb.h;
                        }
                        size.height = f;
                        int b = 0;
                        while (b < document.attributes.size()) {
                            DocumentAttribute attribute = (DocumentAttribute) document.attributes.get(b);
                            if (!(attribute instanceof TL_documentAttributeImageSize)) {
                                if (!(attribute instanceof TL_documentAttributeVideo)) {
                                    b++;
                                }
                            }
                            this.size.width = (float) attribute.w;
                            this.size.height = (float) attribute.h;
                        }
                        return this.size;
                    }
                };
                r0.flowLayoutManager = anonymousClass11;
                recyclerListView2.setLayoutManager(anonymousClass11);
                r0.flowLayoutManager.setSpanSizeLookup(new SpanSizeLookup() {
                    public int getSpanSize(int position) {
                        return EmojiView.this.flowLayoutManager.getSpanSizeForItem(position);
                    }
                });
                r0.gifsGridView.addItemDecoration(new ItemDecoration() {
                    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, State state) {
                        int i = 0;
                        outRect.left = 0;
                        outRect.top = 0;
                        outRect.bottom = 0;
                        int position = parent.getChildAdapterPosition(view);
                        if (!EmojiView.this.flowLayoutManager.isFirstRow(position)) {
                            outRect.top = AndroidUtilities.dp(2.0f);
                        }
                        if (!EmojiView.this.flowLayoutManager.isLastInRow(position)) {
                            i = AndroidUtilities.dp(2.0f);
                        }
                        outRect.right = i;
                    }
                });
                r0.gifsGridView.setOverScrollMode(2);
                recyclerListView2 = r0.gifsGridView;
                trendingGridAdapter = new GifsAdapter(context2);
                r0.gifsAdapter = trendingGridAdapter;
                recyclerListView2.setAdapter(trendingGridAdapter);
                r0.gifsGridView.setOnScrollListener(new OnScrollListener() {
                    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                        EmojiView.this.checkStickersTabY(recyclerView, dy);
                    }
                });
                r0.gifsGridView.setOnItemClickListener(new OnItemClickListener() {
                    public void onItemClick(View view, int position) {
                        if (position >= 0 && position < EmojiView.this.recentGifs.size()) {
                            if (EmojiView.this.listener != null) {
                                EmojiView.this.listener.onGifSelected((Document) EmojiView.this.recentGifs.get(position));
                            }
                        }
                    }
                });
                r0.gifsGridView.setOnItemLongClickListener(new OnItemLongClickListener() {
                    public boolean onItemClick(View view, int position) {
                        if (position >= 0) {
                            if (position < EmojiView.this.recentGifs.size()) {
                                final Document searchImage = (Document) EmojiView.this.recentGifs.get(position);
                                Builder builder = new Builder(view.getContext());
                                builder.setTitle(LocaleController.getString("AppName", R.string.AppName));
                                builder.setMessage(LocaleController.getString("DeleteGif", R.string.DeleteGif));
                                builder.setPositiveButton(LocaleController.getString("OK", R.string.OK).toUpperCase(), new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        DataQuery.getInstance(EmojiView.this.currentAccount).removeRecentGif(searchImage);
                                        EmojiView.this.recentGifs = DataQuery.getInstance(EmojiView.this.currentAccount).getRecentGifs();
                                        if (EmojiView.this.gifsAdapter != null) {
                                            EmojiView.this.gifsAdapter.notifyDataSetChanged();
                                        }
                                        if (EmojiView.this.recentGifs.isEmpty()) {
                                            EmojiView.this.updateStickerTabs();
                                            if (EmojiView.this.stickersGridAdapter != null) {
                                                EmojiView.this.stickersGridAdapter.notifyDataSetChanged();
                                            }
                                        }
                                    }
                                });
                                builder.setNegativeButton(LocaleController.getString("Cancel", R.string.Cancel), null);
                                builder.show().setCanceledOnTouchOutside(true);
                                return true;
                            }
                        }
                        return false;
                    }
                });
                r0.gifsGridView.setVisibility(8);
                r0.stickersWrap.addView(r0.gifsGridView);
            }
            r0.stickersEmptyView = new TextView(context2);
            r0.stickersEmptyView.setText(LocaleController.getString("NoStickers", R.string.NoStickers));
            r0.stickersEmptyView.setTextSize(1, 18.0f);
            r0.stickersEmptyView.setTextColor(Theme.getColor(Theme.key_chat_emojiPanelEmptyText));
            r0.stickersEmptyView.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
            r0.stickersWrap.addView(r0.stickersEmptyView, LayoutHelper.createFrame(-2, -2.0f, 17, 0.0f, 48.0f, 0.0f, 0.0f));
            r0.stickersGridView.setEmptyView(r0.stickersEmptyView);
            r0.stickersTab = new ScrollSlidingTabStrip(context2) {
                float downX;
                float downY;
                boolean draggingHorizontally;
                boolean draggingVertically;
                boolean first = true;
                float lastTranslateX;
                float lastX;
                boolean startedScroll;
                final int touchslop = ViewConfiguration.get(getContext()).getScaledTouchSlop();
                VelocityTracker vTracker;

                public boolean onInterceptTouchEvent(MotionEvent ev) {
                    if (getParent() != null) {
                        getParent().requestDisallowInterceptTouchEvent(true);
                    }
                    if (ev.getAction() == 0) {
                        this.draggingHorizontally = false;
                        this.draggingVertically = false;
                        this.downX = ev.getRawX();
                        this.downY = ev.getRawY();
                    } else if (!(this.draggingVertically || this.draggingHorizontally || EmojiView.this.dragListener == null || Math.abs(ev.getRawY() - this.downY) < ((float) this.touchslop))) {
                        this.draggingVertically = true;
                        this.downY = ev.getRawY();
                        EmojiView.this.dragListener.onDragStart();
                        if (this.startedScroll) {
                            EmojiView.this.pager.endFakeDrag();
                            this.startedScroll = false;
                        }
                        return true;
                    }
                    return super.onInterceptTouchEvent(ev);
                }

                public boolean onTouchEvent(MotionEvent ev) {
                    boolean z = false;
                    if (this.first) {
                        this.first = false;
                        this.lastX = ev.getX();
                    }
                    if (ev.getAction() == 0) {
                        this.draggingHorizontally = false;
                        this.draggingVertically = false;
                        this.downX = ev.getRawX();
                        this.downY = ev.getRawY();
                    } else if (!(this.draggingVertically || this.draggingHorizontally || EmojiView.this.dragListener == null)) {
                        if (Math.abs(ev.getRawX() - this.downX) >= ((float) this.touchslop)) {
                            this.draggingHorizontally = true;
                        } else if (Math.abs(ev.getRawY() - this.downY) >= ((float) this.touchslop)) {
                            this.draggingVertically = true;
                            this.downY = ev.getRawY();
                            EmojiView.this.dragListener.onDragStart();
                            if (this.startedScroll) {
                                EmojiView.this.pager.endFakeDrag();
                                this.startedScroll = false;
                            }
                        }
                    }
                    float velocity;
                    if (this.draggingVertically) {
                        if (this.vTracker == null) {
                            this.vTracker = VelocityTracker.obtain();
                        }
                        this.vTracker.addMovement(ev);
                        if (ev.getAction() != 1) {
                            if (ev.getAction() != 3) {
                                EmojiView.this.dragListener.onDrag(Math.round(ev.getRawY() - this.downY));
                                return true;
                            }
                        }
                        this.vTracker.computeCurrentVelocity(1000);
                        velocity = this.vTracker.getYVelocity();
                        this.vTracker.recycle();
                        this.vTracker = null;
                        if (ev.getAction() == 1) {
                            EmojiView.this.dragListener.onDragEnd(velocity);
                        } else {
                            EmojiView.this.dragListener.onDragCancel();
                        }
                        this.first = true;
                        this.draggingHorizontally = false;
                        this.draggingVertically = false;
                        return true;
                    }
                    velocity = EmojiView.this.stickersTab.getTranslationX();
                    if (EmojiView.this.stickersTab.getScrollX() == 0 && velocity == 0.0f) {
                        if (this.startedScroll || this.lastX - ev.getX() >= 0.0f) {
                            if (this.startedScroll && this.lastX - ev.getX() > 0.0f && EmojiView.this.pager.isFakeDragging()) {
                                EmojiView.this.pager.endFakeDrag();
                                this.startedScroll = false;
                            }
                        } else if (EmojiView.this.pager.beginFakeDrag()) {
                            this.startedScroll = true;
                            this.lastTranslateX = EmojiView.this.stickersTab.getTranslationX();
                        }
                    }
                    if (this.startedScroll) {
                        try {
                            EmojiView.this.pager.fakeDragBy((float) ((int) (((ev.getX() - this.lastX) + velocity) - this.lastTranslateX)));
                            this.lastTranslateX = velocity;
                        } catch (Throwable e) {
                            try {
                                EmojiView.this.pager.endFakeDrag();
                            } catch (Exception e2) {
                            }
                            this.startedScroll = false;
                            FileLog.e(e);
                        }
                    }
                    this.lastX = ev.getX();
                    if (ev.getAction() == 3 || ev.getAction() == 1) {
                        this.first = true;
                        this.draggingHorizontally = false;
                        this.draggingVertically = false;
                        if (this.startedScroll) {
                            EmojiView.this.pager.endFakeDrag();
                            this.startedScroll = false;
                        }
                    }
                    if (!this.startedScroll) {
                        if (!super.onTouchEvent(ev)) {
                            return z;
                        }
                    }
                    z = true;
                    return z;
                }
            };
            r0.stickersTab.setUnderlineHeight(AndroidUtilities.dp(1.0f));
            r0.stickersTab.setIndicatorColor(Theme.getColor(Theme.key_chat_emojiPanelStickerPackSelector));
            r0.stickersTab.setUnderlineColor(Theme.getColor(Theme.key_chat_emojiPanelStickerPackSelector));
            r0.stickersTab.setBackgroundColor(Theme.getColor(Theme.key_chat_emojiPanelBackground));
            r0.stickersTab.setVisibility(4);
            addView(r0.stickersTab, LayoutHelper.createFrame(-1, 48, 51));
            r0.stickersTab.setTranslationX((float) AndroidUtilities.displaySize.x);
            updateStickerTabs();
            r0.stickersTab.setDelegate(new ScrollSlidingTabStripDelegate() {
                public void onPageSelected(int page) {
                    if (EmojiView.this.gifsGridView != null) {
                        if (page == EmojiView.this.gifTabNum + 1) {
                            if (EmojiView.this.gifsGridView.getVisibility() != 0) {
                                EmojiView.this.listener.onGifTab(true);
                                EmojiView.this.showGifTab();
                            }
                        } else if (page != EmojiView.this.trendingTabNum + 1) {
                            int i = 8;
                            if (EmojiView.this.gifsGridView.getVisibility() == 0) {
                                EmojiView.this.listener.onGifTab(false);
                                EmojiView.this.gifsGridView.setVisibility(8);
                                EmojiView.this.stickersGridView.setVisibility(0);
                                EmojiView.this.searchEditTextContainer.setVisibility(0);
                                int vis = EmojiView.this.stickersGridView.getVisibility();
                                TextView access$5400 = EmojiView.this.stickersEmptyView;
                                if (EmojiView.this.stickersGridAdapter.getItemCount() == 0) {
                                    i = 0;
                                }
                                access$5400.setVisibility(i);
                                EmojiView.this.checkScroll();
                                EmojiView.this.saveNewPage();
                            } else if (EmojiView.this.trendingGridView.getVisibility() == 0) {
                                EmojiView.this.trendingGridView.setVisibility(8);
                                EmojiView.this.stickersGridView.setVisibility(0);
                                EmojiView.this.searchEditTextContainer.setVisibility(0);
                                TextView access$54002 = EmojiView.this.stickersEmptyView;
                                if (EmojiView.this.stickersGridAdapter.getItemCount() == 0) {
                                    i = 0;
                                }
                                access$54002.setVisibility(i);
                                EmojiView.this.saveNewPage();
                            }
                        } else if (EmojiView.this.trendingGridView.getVisibility() != 0) {
                            EmojiView.this.showTrendingTab();
                        }
                    }
                    if (page == 0) {
                        EmojiView.this.pager.setCurrentItem(0);
                        return;
                    }
                    if (page != EmojiView.this.gifTabNum + 1) {
                        if (page != EmojiView.this.trendingTabNum + 1) {
                            if (page == EmojiView.this.recentTabBum + 1) {
                                EmojiView.this.stickersLayoutManager.scrollToPositionWithOffset(EmojiView.this.stickersGridAdapter.getPositionForPack("recent"), 0);
                                EmojiView.this.checkStickersTabY(null, 0);
                                EmojiView.this.stickersTab.onPageScrolled(EmojiView.this.recentTabBum + 1, (EmojiView.this.recentTabBum > 0 ? EmojiView.this.recentTabBum : EmojiView.this.stickersTabOffset) + 1);
                            } else if (page == EmojiView.this.favTabBum + 1) {
                                EmojiView.this.stickersLayoutManager.scrollToPositionWithOffset(EmojiView.this.stickersGridAdapter.getPositionForPack("fav"), 0);
                                EmojiView.this.checkStickersTabY(null, 0);
                                EmojiView.this.stickersTab.onPageScrolled(EmojiView.this.favTabBum + 1, (EmojiView.this.favTabBum > 0 ? EmojiView.this.favTabBum : EmojiView.this.stickersTabOffset) + 1);
                            } else {
                                vis = (page - 1) - EmojiView.this.stickersTabOffset;
                                if (vis >= EmojiView.this.stickerSets.size()) {
                                    if (EmojiView.this.listener != null) {
                                        EmojiView.this.listener.onStickersSettingsClick();
                                    }
                                    return;
                                }
                                if (vis >= EmojiView.this.stickerSets.size()) {
                                    vis = EmojiView.this.stickerSets.size() - 1;
                                }
                                EmojiView.this.stickersLayoutManager.scrollToPositionWithOffset(EmojiView.this.stickersGridAdapter.getPositionForPack(EmojiView.this.stickerSets.get(vis)), 0);
                                EmojiView.this.checkStickersTabY(null, 0);
                                EmojiView.this.checkScroll();
                            }
                        }
                    }
                }
            });
            r0.stickersGridView.setOnScrollListener(new OnScrollListener() {
                public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                    if (newState == 1) {
                        AndroidUtilities.hideKeyboard(EmojiView.this.searchEditText);
                    }
                }

                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    EmojiView.this.checkScroll();
                    EmojiView.this.checkStickersTabY(recyclerView, dy);
                    EmojiView.this.checkSearchFieldScroll();
                }
            });
        }
        r0.pager = new ViewPager(context2) {
            public boolean onInterceptTouchEvent(MotionEvent ev) {
                if (getParent() != null) {
                    getParent().requestDisallowInterceptTouchEvent(true);
                }
                return super.onInterceptTouchEvent(ev);
            }
        };
        r0.pager.setAdapter(new EmojiPagesAdapter());
        r0.emojiTab = new LinearLayout(context2) {
            public boolean onInterceptTouchEvent(MotionEvent ev) {
                if (getParent() != null) {
                    getParent().requestDisallowInterceptTouchEvent(true);
                }
                return super.onInterceptTouchEvent(ev);
            }
        };
        r0.emojiTab.setOrientation(0);
        addView(r0.emojiTab, LayoutHelper.createFrame(-1, 48.0f));
        r0.pagerSlidingTabStrip = new PagerSlidingTabStrip(context2);
        r0.pagerSlidingTabStrip.setViewPager(r0.pager);
        r0.pagerSlidingTabStrip.setShouldExpand(true);
        r0.pagerSlidingTabStrip.setIndicatorHeight(AndroidUtilities.dp(2.0f));
        r0.pagerSlidingTabStrip.setUnderlineHeight(AndroidUtilities.dp(1.0f));
        r0.pagerSlidingTabStrip.setIndicatorColor(Theme.getColor(Theme.key_chat_emojiPanelIconSelector));
        r0.pagerSlidingTabStrip.setUnderlineColor(Theme.getColor(Theme.key_chat_emojiPanelShadowLine));
        r0.emojiTab.addView(r0.pagerSlidingTabStrip, LayoutHelper.createLinear(0, 48, 1.0f));
        r0.pagerSlidingTabStrip.setOnPageChangeListener(new OnPageChangeListener() {
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                EmojiView.this.onPageScrolled(position, (EmojiView.this.getMeasuredWidth() - EmojiView.this.getPaddingLeft()) - EmojiView.this.getPaddingRight(), positionOffsetPixels);
            }

            public void onPageSelected(int position) {
                EmojiView.this.saveNewPage();
            }

            public void onPageScrollStateChanged(int state) {
            }
        });
        FrameLayout frameLayout2 = new FrameLayout(context2);
        r0.emojiTab.addView(frameLayout2, LayoutHelper.createLinear(52, 48));
        r0.backspaceButton = new ImageView(context2) {
            public boolean onTouchEvent(MotionEvent event) {
                if (event.getAction() == 0) {
                    EmojiView.this.backspacePressed = true;
                    EmojiView.this.backspaceOnce = false;
                    EmojiView.this.postBackspaceRunnable(350);
                } else if (event.getAction() == 3 || event.getAction() == 1) {
                    EmojiView.this.backspacePressed = false;
                    if (!(EmojiView.this.backspaceOnce || EmojiView.this.listener == null || !EmojiView.this.listener.onBackspace())) {
                        EmojiView.this.backspaceButton.performHapticFeedback(3);
                    }
                }
                super.onTouchEvent(event);
                return true;
            }
        };
        r0.backspaceButton.setImageResource(R.drawable.ic_smiles_backspace);
        r0.backspaceButton.setColorFilter(new PorterDuffColorFilter(Theme.getColor(Theme.key_chat_emojiPanelBackspace), Mode.MULTIPLY));
        r0.backspaceButton.setScaleType(ScaleType.CENTER);
        frameLayout2.addView(r0.backspaceButton, LayoutHelper.createFrame(52, 48.0f));
        r0.shadowLine = new View(context2);
        r0.shadowLine.setBackgroundColor(Theme.getColor(Theme.key_chat_emojiPanelShadowLine));
        frameLayout2.addView(r0.shadowLine, LayoutHelper.createFrame(52, 1, 83));
        r0.noRecentTextView = new TextView(context2);
        r0.noRecentTextView.setText(LocaleController.getString("NoRecent", R.string.NoRecent));
        r0.noRecentTextView.setTextSize(1, 18.0f);
        r0.noRecentTextView.setTextColor(Theme.getColor(Theme.key_chat_emojiPanelEmptyText));
        r0.noRecentTextView.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
        r0.noRecentTextView.setGravity(17);
        r0.noRecentTextView.setClickable(false);
        r0.noRecentTextView.setFocusable(false);
        ((FrameLayout) r0.views.get(0)).addView(r0.noRecentTextView, LayoutHelper.createFrame(-2, -2.0f, 17, 0.0f, 48.0f, 0.0f, 0.0f));
        ((GridView) r0.emojiGrids.get(0)).setEmptyView(r0.noRecentTextView);
        addView(r0.pager, 0, LayoutHelper.createFrame(-1, -1, 51));
        r0.mediaBanTooltip = new CorrectlyMeasuringTextView(context2);
        r0.mediaBanTooltip.setBackgroundDrawable(Theme.createRoundRectDrawable(AndroidUtilities.dp(3.0f), Theme.getColor(Theme.key_chat_gifSaveHintBackground)));
        r0.mediaBanTooltip.setTextColor(Theme.getColor(Theme.key_chat_gifSaveHintText));
        r0.mediaBanTooltip.setPadding(AndroidUtilities.dp(8.0f), AndroidUtilities.dp(7.0f), AndroidUtilities.dp(8.0f), AndroidUtilities.dp(7.0f));
        r0.mediaBanTooltip.setGravity(16);
        r0.mediaBanTooltip.setTextSize(1, 14.0f);
        r0.mediaBanTooltip.setVisibility(4);
        addView(r0.mediaBanTooltip, LayoutHelper.createFrame(-2, -2.0f, 53, 30.0f, 53.0f, 5.0f, 0.0f));
        r0.emojiSize = AndroidUtilities.dp(AndroidUtilities.isTablet() ? 40.0f : 32.0f);
        r0.pickerView = new EmojiColorPickerView(context2);
        View view = r0.pickerView;
        int dp = AndroidUtilities.dp((float) ((((AndroidUtilities.isTablet() ? 40 : 32) * 6) + 10) + 20));
        r0.popupWidth = dp;
        int dp2 = AndroidUtilities.dp(AndroidUtilities.isTablet() ? 64.0f : 56.0f);
        r0.popupHeight = dp2;
        r0.pickerViewPopup = new EmojiPopupWindow(view, dp, dp2);
        r0.pickerViewPopup.setOutsideTouchable(true);
        r0.pickerViewPopup.setClippingEnabled(true);
        r0.pickerViewPopup.setInputMethodMode(2);
        r0.pickerViewPopup.setSoftInputMode(0);
        r0.pickerViewPopup.getContentView().setFocusableInTouchMode(true);
        r0.pickerViewPopup.getContentView().setOnKeyListener(new OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode != 82 || event.getRepeatCount() != 0 || event.getAction() != 1 || EmojiView.this.pickerViewPopup == null || !EmojiView.this.pickerViewPopup.isShowing()) {
                    return false;
                }
                EmojiView.this.pickerViewPopup.dismiss();
                return true;
            }
        });
        r0.currentPage = MessagesController.getGlobalEmojiSettings().getInt("selected_page", 0);
        Emoji.loadRecentEmoji();
        ((EmojiGridAdapter) r0.adapters.get(0)).notifyDataSetChanged();
    }

    public void showSearchField(boolean show) {
        int position = this.stickersLayoutManager.findFirstVisibleItemPosition();
        if (show) {
            if (position == 1 || position == 2) {
                this.stickersLayoutManager.scrollToPosition(0);
                this.stickersTab.setTranslationY(0.0f);
            }
        } else if (position == 0) {
            this.stickersLayoutManager.scrollToPositionWithOffset(1, 0);
        }
    }

    public void hideSearchKeyboard() {
        AndroidUtilities.hideKeyboard(this.searchEditText);
    }

    public void closeSearch(boolean animated) {
        closeSearch(animated, -1);
    }

    public void closeSearch(boolean animated, long scrollToSet) {
        if (this.searchAnimation != null) {
            this.searchAnimation.cancel();
            this.searchAnimation = null;
        }
        this.searchEditText.setText(TtmlNode.ANONYMOUS_REGION_ID);
        if (scrollToSet != -1) {
            TL_messages_stickerSet set = DataQuery.getInstance(this.currentAccount).getStickerSetById(scrollToSet);
            if (set != null) {
                int pos = this.stickersGridAdapter.getPositionForPack(set);
                if (pos >= 0) {
                    this.stickersLayoutManager.scrollToPositionWithOffset(pos, AndroidUtilities.dp(60.0f));
                }
            }
        }
        if (animated) {
            this.searchAnimation = new AnimatorSet();
            AnimatorSet animatorSet = this.searchAnimation;
            r5 = new Animator[3];
            r5[0] = ObjectAnimator.ofFloat(this.stickersTab, "translationY", new float[]{0.0f});
            r5[1] = ObjectAnimator.ofFloat(this.stickersGridView, "translationY", new float[]{(float) (AndroidUtilities.dp(48.0f) - this.searchFieldHeight)});
            r5[2] = ObjectAnimator.ofFloat(this.searchEditTextContainer, "translationY", new float[]{(float) (AndroidUtilities.dp(48.0f) - this.searchFieldHeight)});
            animatorSet.playTogether(r5);
            this.searchAnimation.setDuration(200);
            this.searchAnimation.setInterpolator(CubicBezierInterpolator.EASE_OUT_QUINT);
            this.searchAnimation.addListener(new AnimatorListenerAdapter() {
                public void onAnimationEnd(Animator animation) {
                    if (animation.equals(EmojiView.this.searchAnimation)) {
                        int pos = EmojiView.this.stickersLayoutManager.findFirstVisibleItemPosition();
                        int firstVisPos = EmojiView.this.stickersLayoutManager.findFirstVisibleItemPosition();
                        int top = 0;
                        if (firstVisPos != -1) {
                            top = (int) (((float) EmojiView.this.stickersLayoutManager.findViewByPosition(firstVisPos).getTop()) + EmojiView.this.stickersGridView.getTranslationY());
                        }
                        EmojiView.this.stickersGridView.setTranslationY(0.0f);
                        EmojiView.this.stickersGridView.setPadding(0, AndroidUtilities.dp(52.0f), 0, 0);
                        if (firstVisPos != -1) {
                            EmojiView.this.stickersLayoutManager.scrollToPositionWithOffset(firstVisPos, top - EmojiView.this.stickersGridView.getPaddingTop());
                        }
                        EmojiView.this.searchAnimation = null;
                    }
                }

                public void onAnimationCancel(Animator animation) {
                    if (animation.equals(EmojiView.this.searchAnimation)) {
                        EmojiView.this.searchAnimation = null;
                    }
                }
            });
            this.searchAnimation.start();
            return;
        }
        this.stickersLayoutManager.scrollToPositionWithOffset(1, 0);
        this.searchEditTextContainer.setTranslationY((float) (AndroidUtilities.dp(48.0f) - this.searchFieldHeight));
        this.stickersTab.setTranslationY(0.0f);
        this.stickersGridView.setPadding(0, AndroidUtilities.dp(52.0f), 0, 0);
        this.listener.onSearchOpenClose(false);
    }

    private void checkStickersTabY(View list, int dy) {
        if (list == null) {
            ScrollSlidingTabStrip scrollSlidingTabStrip = this.stickersTab;
            this.minusDy = 0;
            scrollSlidingTabStrip.setTranslationY((float) null);
        } else if (list.getVisibility() == 0) {
            if (this.listener == null || !this.listener.isSearchOpened()) {
                if (dy > 0 && this.stickersGridView != null && this.stickersGridView.getVisibility() == 0) {
                    ViewHolder holder = this.stickersGridView.findViewHolderForAdapterPosition(0);
                    if (holder != null && holder.itemView.getTop() + this.searchFieldHeight >= this.stickersGridView.getPaddingTop()) {
                        return;
                    }
                }
                this.minusDy -= dy;
                if (this.minusDy > 0) {
                    this.minusDy = 0;
                } else if (this.minusDy < (-AndroidUtilities.dp(288.0f))) {
                    this.minusDy = -AndroidUtilities.dp(288.0f);
                }
                this.stickersTab.setTranslationY((float) Math.max(-AndroidUtilities.dp(47.0f), this.minusDy));
            }
        }
    }

    private void checkSearchFieldScroll() {
        if (this.stickersGridView != null) {
            if (this.listener == null || !this.listener.isSearchOpened()) {
                ViewHolder holder = this.stickersGridView.findViewHolderForAdapterPosition(0);
                if (holder != null) {
                    this.searchEditTextContainer.setTranslationY((float) holder.itemView.getTop());
                } else {
                    this.searchEditTextContainer.setTranslationY((float) (-this.searchFieldHeight));
                }
            }
        }
    }

    private void checkScroll() {
        int firstVisibleItem = this.stickersLayoutManager.findFirstVisibleItemPosition();
        if (firstVisibleItem != -1 && this.stickersGridView != null) {
            int firstTab;
            if (this.favTabBum > 0) {
                firstTab = this.favTabBum;
            } else if (this.recentTabBum > 0) {
                firstTab = this.recentTabBum;
            } else {
                firstTab = this.stickersTabOffset;
                if (this.stickersGridView.getVisibility() == 0) {
                    if (!(this.gifsGridView == null || this.gifsGridView.getVisibility() == 0)) {
                        this.gifsGridView.setVisibility(0);
                    }
                    if (this.stickersEmptyView != null && this.stickersEmptyView.getVisibility() == 0) {
                        this.stickersEmptyView.setVisibility(8);
                    }
                    this.stickersTab.onPageScrolled(this.gifTabNum + 1, firstTab + 1);
                }
                this.stickersTab.onPageScrolled(this.stickersGridAdapter.getTabForPosition(firstVisibleItem) + 1, firstTab + 1);
                return;
            }
            if (this.stickersGridView.getVisibility() == 0) {
                this.stickersTab.onPageScrolled(this.stickersGridAdapter.getTabForPosition(firstVisibleItem) + 1, firstTab + 1);
                return;
            }
            this.gifsGridView.setVisibility(0);
            this.stickersEmptyView.setVisibility(8);
            this.stickersTab.onPageScrolled(this.gifTabNum + 1, firstTab + 1);
        }
    }

    private void saveNewPage() {
        int newPage;
        if (this.pager.getCurrentItem() != 6) {
            newPage = 0;
        } else if (this.gifsGridView == null || this.gifsGridView.getVisibility() != 0) {
            newPage = 1;
        } else {
            newPage = 2;
        }
        if (this.currentPage != newPage) {
            this.currentPage = newPage;
            MessagesController.getGlobalEmojiSettings().edit().putInt("selected_page", newPage).commit();
        }
    }

    public void clearRecentEmoji() {
        Emoji.clearRecentEmoji();
        ((EmojiGridAdapter) this.adapters.get(0)).notifyDataSetChanged();
    }

    private void showTrendingTab() {
        this.trendingGridView.setVisibility(0);
        this.stickersGridView.setVisibility(8);
        this.firstAttach = true;
        this.searchEditTextContainer.setVisibility(8);
        this.stickersEmptyView.setVisibility(8);
        this.gifsGridView.setVisibility(8);
        this.stickersTab.onPageScrolled(this.trendingTabNum + 1, (this.recentTabBum > 0 ? this.recentTabBum : this.stickersTabOffset) + 1);
        saveNewPage();
    }

    private void showGifTab() {
        this.gifsGridView.setVisibility(0);
        this.stickersGridView.setVisibility(8);
        this.firstAttach = true;
        this.searchEditTextContainer.setVisibility(8);
        this.stickersEmptyView.setVisibility(8);
        this.trendingGridView.setVisibility(8);
        this.stickersTab.onPageScrolled(this.gifTabNum + 1, (this.recentTabBum > 0 ? this.recentTabBum : this.stickersTabOffset) + 1);
        saveNewPage();
    }

    private void onPageScrolled(int position, int width, int positionOffsetPixels) {
        if (this.stickersTab != null) {
            if (width == 0) {
                width = AndroidUtilities.displaySize.x;
            }
            int margin = 0;
            boolean z = true;
            int i = 0;
            if (position == 5) {
                margin = -positionOffsetPixels;
                if (this.listener != null) {
                    Listener listener = this.listener;
                    if (positionOffsetPixels == 0) {
                        z = false;
                    }
                    listener.onStickersTab(z);
                }
            } else if (position == 6) {
                margin = -width;
                if (this.listener != null) {
                    this.listener.onStickersTab(true);
                }
            } else if (this.listener != null) {
                this.listener.onStickersTab(false);
            }
            if (this.emojiTab.getTranslationX() != ((float) margin)) {
                this.emojiTab.setTranslationX((float) margin);
                this.stickersTab.setTranslationX((float) (width + margin));
                ScrollSlidingTabStrip scrollSlidingTabStrip = this.stickersTab;
                if (margin >= 0) {
                    i = 4;
                }
                scrollSlidingTabStrip.setVisibility(i);
            }
        }
    }

    private void postBackspaceRunnable(final int time) {
        AndroidUtilities.runOnUIThread(new Runnable() {
            public void run() {
                if (EmojiView.this.backspacePressed) {
                    if (EmojiView.this.listener != null && EmojiView.this.listener.onBackspace()) {
                        EmojiView.this.backspaceButton.performHapticFeedback(3);
                    }
                    EmojiView.this.backspaceOnce = true;
                    EmojiView.this.postBackspaceRunnable(Math.max(50, time - 100));
                }
            }
        }, (long) time);
    }

    public void switchToGifRecent() {
        if (this.gifTabNum < 0 || this.recentGifs.isEmpty()) {
            this.switchToGifTab = true;
        } else {
            this.stickersTab.selectTab(this.gifTabNum + 1);
        }
        this.pager.setCurrentItem(6);
    }

    private void updateStickerTabs() {
        if (this.stickersTab != null) {
            this.recentTabBum = -2;
            this.favTabBum = -2;
            this.gifTabNum = -2;
            this.trendingTabNum = -2;
            this.stickersTabOffset = 0;
            int lastPosition = this.stickersTab.getCurrentPosition();
            this.stickersTab.removeTabs();
            Drawable drawable = getContext().getResources().getDrawable(R.drawable.ic_smiles2_smile);
            Theme.setDrawableColorByKey(drawable, Theme.key_chat_emojiPanelIcon);
            this.stickersTab.addIconTab(drawable);
            if (this.showGifs && !this.recentGifs.isEmpty()) {
                drawable = getContext().getResources().getDrawable(R.drawable.ic_smiles_gif);
                Theme.setDrawableColorByKey(drawable, Theme.key_chat_emojiPanelIcon);
                this.stickersTab.addIconTab(drawable);
                this.gifTabNum = this.stickersTabOffset;
                this.stickersTabOffset++;
            }
            ArrayList<Long> unread = DataQuery.getInstance(this.currentAccount).getUnreadStickerSets();
            if (!(this.trendingGridAdapter == null || this.trendingGridAdapter.getItemCount() == 0 || unread.isEmpty())) {
                drawable = getContext().getResources().getDrawable(R.drawable.ic_smiles_trend);
                Theme.setDrawableColorByKey(drawable, Theme.key_chat_emojiPanelIcon);
                TextView stickersCounter = this.stickersTab.addIconTabWithCounter(drawable);
                this.trendingTabNum = this.stickersTabOffset;
                this.stickersTabOffset++;
                stickersCounter.setText(String.format("%d", new Object[]{Integer.valueOf(unread.size())}));
            }
            if (!this.favouriteStickers.isEmpty()) {
                this.favTabBum = this.stickersTabOffset;
                this.stickersTabOffset++;
                drawable = getContext().getResources().getDrawable(R.drawable.staredstickerstab);
                Theme.setDrawableColorByKey(drawable, Theme.key_chat_emojiPanelIcon);
                this.stickersTab.addIconTab(drawable);
            }
            if (!this.recentStickers.isEmpty()) {
                this.recentTabBum = this.stickersTabOffset;
                this.stickersTabOffset++;
                drawable = getContext().getResources().getDrawable(R.drawable.ic_smiles2_recent);
                Theme.setDrawableColorByKey(drawable, Theme.key_chat_emojiPanelIcon);
                this.stickersTab.addIconTab(drawable);
            }
            this.stickerSets.clear();
            TL_messages_stickerSet tL_messages_stickerSet = null;
            this.groupStickerSet = null;
            this.groupStickerPackPosition = -1;
            this.groupStickerPackNum = -10;
            ArrayList<TL_messages_stickerSet> packs = DataQuery.getInstance(this.currentAccount).getStickerSets(0);
            for (int a = 0; a < packs.size(); a++) {
                TL_messages_stickerSet pack = (TL_messages_stickerSet) packs.get(a);
                if (!(pack.set.archived || pack.documents == null)) {
                    if (!pack.documents.isEmpty()) {
                        this.stickerSets.add(pack);
                    }
                }
            }
            if (this.info != null) {
                TL_messages_stickerSet pack2;
                TL_messages_stickerSet set;
                long hiddenStickerSetId = MessagesController.getEmojiSettings(this.currentAccount);
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("group_hide_stickers_");
                stringBuilder.append(this.info.id);
                hiddenStickerSetId = hiddenStickerSetId.getLong(stringBuilder.toString(), -1);
                Chat chat = MessagesController.getInstance(this.currentAccount).getChat(Integer.valueOf(this.info.id));
                if (!(chat == null || this.info.stickerset == null)) {
                    if (ChatObject.hasAdminRights(chat)) {
                        if (this.info.stickerset != null) {
                            this.groupStickersHidden = hiddenStickerSetId == this.info.stickerset.id;
                        }
                        if (this.info.stickerset != null) {
                            pack2 = DataQuery.getInstance(this.currentAccount).getGroupStickerSetById(this.info.stickerset);
                            if (!(pack2 == null || pack2.documents == null || pack2.documents.isEmpty() || pack2.set == null)) {
                                set = new TL_messages_stickerSet();
                                set.documents = pack2.documents;
                                set.packs = pack2.packs;
                                set.set = pack2.set;
                                if (this.groupStickersHidden) {
                                    this.groupStickerPackNum = 0;
                                    this.stickerSets.add(0, set);
                                } else {
                                    this.groupStickerPackNum = this.stickerSets.size();
                                    this.stickerSets.add(set);
                                }
                                if (this.info.can_set_stickers) {
                                    tL_messages_stickerSet = set;
                                }
                                this.groupStickerSet = tL_messages_stickerSet;
                            }
                        } else if (this.info.can_set_stickers) {
                            tL_messages_stickerSet = new TL_messages_stickerSet();
                            if (this.groupStickersHidden) {
                                this.groupStickerPackNum = 0;
                                this.stickerSets.add(0, tL_messages_stickerSet);
                            } else {
                                this.groupStickerPackNum = this.stickerSets.size();
                                this.stickerSets.add(tL_messages_stickerSet);
                            }
                        }
                    }
                }
                this.groupStickersHidden = hiddenStickerSetId != -1;
                if (this.info.stickerset != null) {
                    pack2 = DataQuery.getInstance(this.currentAccount).getGroupStickerSetById(this.info.stickerset);
                    set = new TL_messages_stickerSet();
                    set.documents = pack2.documents;
                    set.packs = pack2.packs;
                    set.set = pack2.set;
                    if (this.groupStickersHidden) {
                        this.groupStickerPackNum = 0;
                        this.stickerSets.add(0, set);
                    } else {
                        this.groupStickerPackNum = this.stickerSets.size();
                        this.stickerSets.add(set);
                    }
                    if (this.info.can_set_stickers) {
                        tL_messages_stickerSet = set;
                    }
                    this.groupStickerSet = tL_messages_stickerSet;
                } else if (this.info.can_set_stickers) {
                    tL_messages_stickerSet = new TL_messages_stickerSet();
                    if (this.groupStickersHidden) {
                        this.groupStickerPackNum = 0;
                        this.stickerSets.add(0, tL_messages_stickerSet);
                    } else {
                        this.groupStickerPackNum = this.stickerSets.size();
                        this.stickerSets.add(tL_messages_stickerSet);
                    }
                }
            }
            int a2 = 0;
            while (a2 < this.stickerSets.size()) {
                if (a2 == this.groupStickerPackNum) {
                    Chat chat2 = MessagesController.getInstance(this.currentAccount).getChat(Integer.valueOf(this.info.id));
                    if (chat2 == null) {
                        this.stickerSets.remove(0);
                        a2--;
                    } else {
                        this.stickersTab.addStickerTab(chat2);
                    }
                } else {
                    this.stickersTab.addStickerTab((Document) ((TL_messages_stickerSet) this.stickerSets.get(a2)).documents.get(0));
                }
                a2++;
            }
            if (!(this.trendingGridAdapter == null || this.trendingGridAdapter.getItemCount() == 0 || !unread.isEmpty())) {
                drawable = getContext().getResources().getDrawable(R.drawable.ic_smiles_trend);
                Theme.setDrawableColorByKey(drawable, Theme.key_chat_emojiPanelIcon);
                this.trendingTabNum = this.stickersTabOffset + this.stickerSets.size();
                this.stickersTab.addIconTab(drawable);
            }
            drawable = getContext().getResources().getDrawable(R.drawable.ic_smiles_settings);
            Theme.setDrawableColorByKey(drawable, Theme.key_chat_emojiPanelIcon);
            this.stickersTab.addIconTab(drawable);
            this.stickersTab.updateTabStyles();
            if (lastPosition != 0) {
                this.stickersTab.onPageScrolled(lastPosition, lastPosition);
            }
            if (this.switchToGifTab && this.gifTabNum >= 0 && this.gifsGridView.getVisibility() != 0) {
                showGifTab();
                this.switchToGifTab = false;
            }
            checkPanels();
        }
    }

    private void checkPanels() {
        if (this.stickersTab != null) {
            int i = 8;
            if (this.trendingTabNum == -2 && this.trendingGridView != null && this.trendingGridView.getVisibility() == 0) {
                this.gifsGridView.setVisibility(8);
                this.trendingGridView.setVisibility(8);
                this.stickersGridView.setVisibility(0);
                this.searchEditTextContainer.setVisibility(0);
                this.stickersEmptyView.setVisibility(this.stickersGridAdapter.getItemCount() != 0 ? 8 : 0);
            }
            if (this.gifTabNum == -2 && this.gifsGridView != null && this.gifsGridView.getVisibility() == 0) {
                this.listener.onGifTab(false);
                this.gifsGridView.setVisibility(8);
                this.trendingGridView.setVisibility(8);
                this.stickersGridView.setVisibility(0);
                this.searchEditTextContainer.setVisibility(0);
                TextView textView = this.stickersEmptyView;
                if (this.stickersGridAdapter.getItemCount() == 0) {
                    i = 0;
                }
                textView.setVisibility(i);
            } else if (this.gifTabNum != -2) {
                if (this.gifsGridView != null && this.gifsGridView.getVisibility() == 0) {
                    this.stickersTab.onPageScrolled(this.gifTabNum + 1, (this.recentTabBum > 0 ? this.recentTabBum : this.stickersTabOffset) + 1);
                } else if (this.trendingGridView == null || this.trendingGridView.getVisibility() != 0) {
                    int position = this.stickersLayoutManager.findFirstVisibleItemPosition();
                    if (position != -1) {
                        int firstTab;
                        if (this.favTabBum > 0) {
                            firstTab = this.favTabBum;
                        } else if (this.recentTabBum > 0) {
                            firstTab = this.recentTabBum;
                        } else {
                            firstTab = this.stickersTabOffset;
                            this.stickersTab.onPageScrolled(this.stickersGridAdapter.getTabForPosition(position) + 1, firstTab + 1);
                        }
                        this.stickersTab.onPageScrolled(this.stickersGridAdapter.getTabForPosition(position) + 1, firstTab + 1);
                    }
                } else {
                    this.stickersTab.onPageScrolled(this.trendingTabNum + 1, (this.recentTabBum > 0 ? this.recentTabBum : this.stickersTabOffset) + 1);
                }
            }
        }
    }

    public void addRecentSticker(Document document) {
        if (document != null) {
            DataQuery.getInstance(this.currentAccount).addRecentSticker(0, document, (int) (System.currentTimeMillis() / 1000), false);
            boolean wasEmpty = this.recentStickers.isEmpty();
            this.recentStickers = DataQuery.getInstance(this.currentAccount).getRecentStickers(0);
            if (this.stickersGridAdapter != null) {
                this.stickersGridAdapter.notifyDataSetChanged();
            }
            if (wasEmpty) {
                updateStickerTabs();
            }
        }
    }

    public void addRecentGif(Document document) {
        if (document != null) {
            boolean wasEmpty = this.recentGifs.isEmpty();
            this.recentGifs = DataQuery.getInstance(this.currentAccount).getRecentGifs();
            if (this.gifsAdapter != null) {
                this.gifsAdapter.notifyDataSetChanged();
            }
            if (wasEmpty) {
                updateStickerTabs();
            }
        }
    }

    public void requestLayout() {
        if (!this.isLayout) {
            super.requestLayout();
        }
    }

    public void updateUIColors() {
        if (AndroidUtilities.isInMultiwindow) {
            getBackground().setColorFilter(new PorterDuffColorFilter(Theme.getColor(Theme.key_chat_emojiPanelBackground), Mode.MULTIPLY));
        } else {
            setBackgroundColor(Theme.getColor(Theme.key_chat_emojiPanelBackground));
            this.emojiTab.setBackgroundColor(Theme.getColor(Theme.key_chat_emojiPanelBackground));
        }
        if (this.searchEditTextContainer != null) {
            this.searchEditTextContainer.setBackgroundColor(Theme.getColor(Theme.key_chat_emojiPanelBackground));
        }
        if (this.dotPaint != null) {
            this.dotPaint.setColor(Theme.getColor(Theme.key_chat_emojiPanelNewTrending));
        }
        if (this.stickersGridView != null) {
            this.stickersGridView.setGlowColor(Theme.getColor(Theme.key_chat_emojiPanelBackground));
        }
        if (this.trendingGridView != null) {
            this.trendingGridView.setGlowColor(Theme.getColor(Theme.key_chat_emojiPanelBackground));
        }
        if (this.stickersEmptyView != null) {
            this.stickersEmptyView.setTextColor(Theme.getColor(Theme.key_chat_emojiPanelEmptyText));
        }
        if (this.stickersTab != null) {
            this.stickersTab.setIndicatorColor(Theme.getColor(Theme.key_chat_emojiPanelStickerPackSelector));
            this.stickersTab.setUnderlineColor(Theme.getColor(Theme.key_chat_emojiPanelStickerPackSelector));
            this.stickersTab.setBackgroundColor(Theme.getColor(Theme.key_chat_emojiPanelBackground));
        }
        if (this.pagerSlidingTabStrip != null) {
            this.pagerSlidingTabStrip.setIndicatorColor(Theme.getColor(Theme.key_chat_emojiPanelIconSelector));
            this.pagerSlidingTabStrip.setUnderlineColor(Theme.getColor(Theme.key_chat_emojiPanelShadowLine));
        }
        if (this.backspaceButton != null) {
            this.backspaceButton.setColorFilter(new PorterDuffColorFilter(Theme.getColor(Theme.key_chat_emojiPanelBackspace), Mode.MULTIPLY));
        }
        if (this.searchIconImageView != null) {
            this.clearSearchImageView.setColorFilter(new PorterDuffColorFilter(Theme.getColor(Theme.key_chat_emojiPanelIcon), Mode.MULTIPLY));
        }
        if (this.shadowLine != null) {
            this.shadowLine.setBackgroundColor(Theme.getColor(Theme.key_chat_emojiPanelShadowLine));
        }
        if (this.noRecentTextView != null) {
            this.noRecentTextView.setTextColor(Theme.getColor(Theme.key_chat_emojiPanelEmptyText));
        }
        if (this.mediaBanTooltip != null) {
            ((ShapeDrawable) this.mediaBanTooltip.getBackground()).getPaint().setColor(Theme.getColor(Theme.key_chat_gifSaveHintBackground));
            this.mediaBanTooltip.setTextColor(Theme.getColor(Theme.key_chat_gifSaveHintText));
        }
        Theme.setDrawableColorByKey(this.stickersDrawable, Theme.key_chat_emojiPanelIcon);
        for (int a = 0; a < this.icons.length - 1; a++) {
            Theme.setEmojiDrawableColor(this.icons[a], Theme.getColor(Theme.key_chat_emojiPanelIcon), false);
            Theme.setEmojiDrawableColor(this.icons[a], Theme.getColor(Theme.key_chat_emojiPanelIconSelected), true);
        }
        if (this.searchBackground != null) {
            Theme.setDrawableColorByKey(this.searchBackground.getBackground(), Theme.key_chat_emojiSearchBackground);
            this.searchBackground.invalidate();
        }
    }

    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        this.isLayout = true;
        if (AndroidUtilities.isInMultiwindow) {
            if (this.currentBackgroundType != 1) {
                if (VERSION.SDK_INT >= 21) {
                    setOutlineProvider((ViewOutlineProvider) this.outlineProvider);
                    setClipToOutline(true);
                    setElevation((float) AndroidUtilities.dp(2.0f));
                }
                setBackgroundResource(R.drawable.smiles_popup);
                getBackground().setColorFilter(new PorterDuffColorFilter(Theme.getColor(Theme.key_chat_emojiPanelBackground), Mode.MULTIPLY));
                this.emojiTab.setBackgroundDrawable(null);
                this.currentBackgroundType = 1;
            }
        } else if (this.currentBackgroundType != 0) {
            if (VERSION.SDK_INT >= 21) {
                setOutlineProvider(null);
                setClipToOutline(false);
                setElevation(0.0f);
            }
            setBackgroundColor(Theme.getColor(Theme.key_chat_emojiPanelBackground));
            this.emojiTab.setBackgroundColor(Theme.getColor(Theme.key_chat_emojiPanelBackground));
            this.currentBackgroundType = 0;
        }
        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) this.emojiTab.getLayoutParams();
        FrameLayout.LayoutParams layoutParams1 = null;
        layoutParams.width = MeasureSpec.getSize(widthMeasureSpec);
        if (this.stickersTab != null) {
            layoutParams1 = (FrameLayout.LayoutParams) this.stickersTab.getLayoutParams();
            if (layoutParams1 != null) {
                layoutParams1.width = layoutParams.width;
            }
        }
        if (layoutParams.width != this.oldWidth) {
            if (!(this.stickersTab == null || layoutParams1 == null)) {
                onPageScrolled(this.pager.getCurrentItem(), (layoutParams.width - getPaddingLeft()) - getPaddingRight(), 0);
                this.stickersTab.setLayoutParams(layoutParams1);
            }
            this.emojiTab.setLayoutParams(layoutParams);
            this.oldWidth = layoutParams.width;
        }
        super.onMeasure(MeasureSpec.makeMeasureSpec(layoutParams.width, 1073741824), MeasureSpec.makeMeasureSpec(MeasureSpec.getSize(heightMeasureSpec), 1073741824));
        this.isLayout = false;
    }

    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        if (this.lastNotifyWidth != right - left) {
            this.lastNotifyWidth = right - left;
            reloadStickersAdapter();
        }
        super.onLayout(changed, left, top, right, bottom);
    }

    private void reloadStickersAdapter() {
        if (this.stickersGridAdapter != null) {
            this.stickersGridAdapter.notifyDataSetChanged();
        }
        if (this.trendingGridAdapter != null) {
            this.trendingGridAdapter.notifyDataSetChanged();
        }
        if (this.stickersSearchGridAdapter != null) {
            this.stickersSearchGridAdapter.notifyDataSetChanged();
        }
        if (StickerPreviewViewer.getInstance().isVisible()) {
            StickerPreviewViewer.getInstance().close();
        }
        StickerPreviewViewer.getInstance().reset();
    }

    public void setListener(Listener value) {
        this.listener = value;
    }

    public void setDragListener(DragListener dragListener) {
        this.dragListener = dragListener;
    }

    public void setChatInfo(ChatFull chatInfo) {
        this.info = chatInfo;
        updateStickerTabs();
    }

    public void invalidateViews() {
        for (int a = 0; a < this.emojiGrids.size(); a++) {
            ((GridView) this.emojiGrids.get(a)).invalidateViews();
        }
    }

    public void onOpen(boolean forceEmoji) {
        if (this.stickersTab != null) {
            if (!(this.currentPage == 0 || this.currentChatId == 0)) {
                this.currentPage = 0;
            }
            if (this.currentPage != 0) {
                if (!forceEmoji) {
                    if (this.currentPage == 1) {
                        if (this.pager.getCurrentItem() != 6) {
                            this.pager.setCurrentItem(6);
                        }
                        if (this.stickersTab.getCurrentPosition() != this.gifTabNum + 1) {
                            return;
                        }
                        if (this.recentTabBum >= 0) {
                            this.stickersTab.selectTab(this.recentTabBum + 1);
                            return;
                        } else if (this.favTabBum >= 0) {
                            this.stickersTab.selectTab(this.favTabBum + 1);
                            return;
                        } else if (this.gifTabNum >= 0) {
                            this.stickersTab.selectTab(this.gifTabNum + 2);
                            return;
                        } else {
                            this.stickersTab.selectTab(1);
                            return;
                        }
                    } else if (this.currentPage == 2) {
                        if (this.pager.getCurrentItem() != 6) {
                            this.pager.setCurrentItem(6);
                        }
                        if (this.stickersTab.getCurrentPosition() == this.gifTabNum + 1) {
                            return;
                        }
                        if (this.gifTabNum < 0 || this.recentGifs.isEmpty()) {
                            this.switchToGifTab = true;
                            return;
                        } else {
                            this.stickersTab.selectTab(this.gifTabNum + 1);
                            return;
                        }
                    } else {
                        return;
                    }
                }
            }
            if (this.pager.getCurrentItem() == 6) {
                this.pager.setCurrentItem(0, forceEmoji ^ 1);
            }
        }
    }

    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (this.stickersGridAdapter != null) {
            NotificationCenter.getGlobalInstance().addObserver(this, NotificationCenter.emojiDidLoaded);
            NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.stickersDidLoaded);
            NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.recentImagesDidLoaded);
            NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.featuredStickersDidLoaded);
            NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.groupStickersDidLoaded);
            AndroidUtilities.runOnUIThread(new Runnable() {
                public void run() {
                    EmojiView.this.updateStickerTabs();
                    EmojiView.this.reloadStickersAdapter();
                }
            });
        }
    }

    public void setVisibility(int visibility) {
        super.setVisibility(visibility);
        if (visibility != 8) {
            Emoji.sortEmoji();
            ((EmojiGridAdapter) this.adapters.get(0)).notifyDataSetChanged();
            if (this.stickersGridAdapter != null) {
                NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.stickersDidLoaded);
                NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.recentDocumentsDidLoaded);
                updateStickerTabs();
                reloadStickersAdapter();
                if (!(this.gifsGridView == null || this.gifsGridView.getVisibility() != 0 || this.listener == null)) {
                    Listener listener = this.listener;
                    boolean z = this.pager != null && this.pager.getCurrentItem() >= 6;
                    listener.onGifTab(z);
                }
            }
            if (this.trendingGridAdapter != null) {
                this.trendingLoaded = false;
                this.trendingGridAdapter.notifyDataSetChanged();
            }
            checkDocuments(true);
            checkDocuments(false);
            DataQuery.getInstance(this.currentAccount).loadRecents(0, true, true, false);
            DataQuery.getInstance(this.currentAccount).loadRecents(0, false, true, false);
            DataQuery.getInstance(this.currentAccount).loadRecents(2, false, true, false);
        }
    }

    public int getCurrentPage() {
        return this.currentPage;
    }

    public void onDestroy() {
        if (this.stickersGridAdapter != null) {
            NotificationCenter.getGlobalInstance().removeObserver(this, NotificationCenter.emojiDidLoaded);
            NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.stickersDidLoaded);
            NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.recentDocumentsDidLoaded);
            NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.featuredStickersDidLoaded);
            NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.groupStickersDidLoaded);
        }
    }

    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (this.pickerViewPopup != null && this.pickerViewPopup.isShowing()) {
            this.pickerViewPopup.dismiss();
        }
    }

    private void checkDocuments(boolean isGif) {
        int previousCount;
        if (isGif) {
            previousCount = this.recentGifs.size();
            this.recentGifs = DataQuery.getInstance(this.currentAccount).getRecentGifs();
            if (this.gifsAdapter != null) {
                this.gifsAdapter.notifyDataSetChanged();
            }
            if (previousCount != this.recentGifs.size()) {
                updateStickerTabs();
            }
            if (this.stickersGridAdapter != null) {
                this.stickersGridAdapter.notifyDataSetChanged();
            }
            return;
        }
        previousCount = this.recentStickers.size();
        int previousCount2 = this.favouriteStickers.size();
        this.recentStickers = DataQuery.getInstance(this.currentAccount).getRecentStickers(0);
        this.favouriteStickers = DataQuery.getInstance(this.currentAccount).getRecentStickers(2);
        for (int a = 0; a < this.favouriteStickers.size(); a++) {
            Document favSticker = (Document) this.favouriteStickers.get(a);
            for (int b = 0; b < this.recentStickers.size(); b++) {
                Document recSticker = (Document) this.recentStickers.get(b);
                if (recSticker.dc_id == favSticker.dc_id && recSticker.id == favSticker.id) {
                    this.recentStickers.remove(b);
                    break;
                }
            }
        }
        if (!(previousCount == this.recentStickers.size() && previousCount2 == this.favouriteStickers.size())) {
            updateStickerTabs();
        }
        if (this.stickersGridAdapter != null) {
            this.stickersGridAdapter.notifyDataSetChanged();
        }
        checkPanels();
    }

    public void setStickersBanned(boolean value, int chatId) {
        if (value) {
            this.currentChatId = chatId;
        } else {
            this.currentChatId = 0;
        }
        View view = this.pagerSlidingTabStrip.getTab(6);
        if (view != null) {
            view.setAlpha(this.currentChatId != 0 ? 0.5f : 1.0f);
            if (this.currentChatId != 0 && this.pager.getCurrentItem() == 6) {
                this.pager.setCurrentItem(0);
            }
        }
    }

    public void showStickerBanHint() {
        if (this.mediaBanTooltip.getVisibility() != 0) {
            Chat chat = MessagesController.getInstance(this.currentAccount).getChat(Integer.valueOf(this.currentChatId));
            if (chat != null) {
                if (chat.banned_rights != null) {
                    if (AndroidUtilities.isBannedForever(chat.banned_rights.until_date)) {
                        this.mediaBanTooltip.setText(LocaleController.getString("AttachStickersRestrictedForever", R.string.AttachStickersRestrictedForever));
                    } else {
                        this.mediaBanTooltip.setText(LocaleController.formatString("AttachStickersRestricted", R.string.AttachStickersRestricted, LocaleController.formatDateForBan((long) chat.banned_rights.until_date)));
                    }
                    this.mediaBanTooltip.setVisibility(0);
                    AnimatorSet AnimatorSet = new AnimatorSet();
                    AnimatorSet.playTogether(new Animator[]{ObjectAnimator.ofFloat(this.mediaBanTooltip, "alpha", new float[]{0.0f, 1.0f})});
                    AnimatorSet.addListener(new AnimatorListenerAdapter() {
                        public void onAnimationEnd(Animator animation) {
                            AndroidUtilities.runOnUIThread(new Runnable() {
                                public void run() {
                                    if (EmojiView.this.mediaBanTooltip != null) {
                                        AnimatorSet AnimatorSet = new AnimatorSet();
                                        Animator[] animatorArr = new Animator[1];
                                        animatorArr[0] = ObjectAnimator.ofFloat(EmojiView.this.mediaBanTooltip, "alpha", new float[]{0.0f});
                                        AnimatorSet.playTogether(animatorArr);
                                        AnimatorSet.addListener(new AnimatorListenerAdapter() {
                                            public void onAnimationEnd(Animator animation) {
                                                if (EmojiView.this.mediaBanTooltip != null) {
                                                    EmojiView.this.mediaBanTooltip.setVisibility(4);
                                                }
                                            }
                                        });
                                        AnimatorSet.setDuration(300);
                                        AnimatorSet.start();
                                    }
                                }
                            }, DefaultRenderersFactory.DEFAULT_ALLOWED_VIDEO_JOINING_TIME_MS);
                        }
                    });
                    AnimatorSet.setDuration(300);
                    AnimatorSet.start();
                }
            }
        }
    }

    private void updateVisibleTrendingSets() {
        if (this.trendingGridAdapter != null) {
            if (this.trendingGridAdapter != null) {
                try {
                    int count = this.trendingGridView.getChildCount();
                    for (int a = 0; a < count; a++) {
                        View child = this.trendingGridView.getChildAt(a);
                        if (child instanceof FeaturedStickerSetInfoCell) {
                            if (((Holder) this.trendingGridView.getChildViewHolder(child)) != null) {
                                FeaturedStickerSetInfoCell cell = (FeaturedStickerSetInfoCell) child;
                                ArrayList<Long> unreadStickers = DataQuery.getInstance(this.currentAccount).getUnreadStickerSets();
                                StickerSetCovered stickerSetCovered = cell.getStickerSet();
                                boolean z = true;
                                boolean unread = unreadStickers != null && unreadStickers.contains(Long.valueOf(stickerSetCovered.set.id));
                                cell.setStickerSet(stickerSetCovered, unread);
                                if (unread) {
                                    DataQuery.getInstance(this.currentAccount).markFaturedStickersByIdAsRead(stickerSetCovered.set.id);
                                }
                                boolean installing = this.installingStickerSets.indexOfKey(stickerSetCovered.set.id) >= 0;
                                boolean removing = this.removingStickerSets.indexOfKey(stickerSetCovered.set.id) >= 0;
                                if (installing || removing) {
                                    if (installing && cell.isInstalled()) {
                                        this.installingStickerSets.remove(stickerSetCovered.set.id);
                                        installing = false;
                                    } else if (removing && !cell.isInstalled()) {
                                        this.removingStickerSets.remove(stickerSetCovered.set.id);
                                        removing = false;
                                    }
                                }
                                if (!installing) {
                                    if (!removing) {
                                        z = false;
                                    }
                                }
                                cell.setDrawProgress(z);
                            }
                        }
                    }
                } catch (Throwable e) {
                    FileLog.e(e);
                }
            }
        }
    }

    public boolean areThereAnyStickers() {
        return this.stickersGridAdapter != null && this.stickersGridAdapter.getItemCount() > 0;
    }

    public void didReceivedNotification(int id, int account, Object... args) {
        int a = 0;
        if (id == NotificationCenter.stickersDidLoaded) {
            if (((Integer) args[0]).intValue() == 0) {
                if (this.trendingGridAdapter != null) {
                    if (this.trendingLoaded) {
                        updateVisibleTrendingSets();
                    } else {
                        this.trendingGridAdapter.notifyDataSetChanged();
                    }
                }
                updateStickerTabs();
                reloadStickersAdapter();
                checkPanels();
            }
        } else if (id == NotificationCenter.recentDocumentsDidLoaded) {
            boolean isGif = ((Boolean) args[0]).booleanValue();
            a = ((Integer) args[1]).intValue();
            if (isGif || a == 0 || a == 2) {
                checkDocuments(isGif);
            }
        } else if (id == NotificationCenter.featuredStickersDidLoaded) {
            if (this.trendingGridAdapter != null) {
                if (this.featuredStickersHash != DataQuery.getInstance(this.currentAccount).getFeaturesStickersHashWithoutUnread()) {
                    this.trendingLoaded = false;
                }
                if (this.trendingLoaded) {
                    updateVisibleTrendingSets();
                } else {
                    this.trendingGridAdapter.notifyDataSetChanged();
                }
            }
            if (this.pagerSlidingTabStrip != null) {
                count = this.pagerSlidingTabStrip.getChildCount();
                while (a < count) {
                    this.pagerSlidingTabStrip.getChildAt(a).invalidate();
                    a++;
                }
            }
            updateStickerTabs();
        } else if (id == NotificationCenter.groupStickersDidLoaded) {
            if (this.info != null && this.info.stickerset != null && this.info.stickerset.id == ((Long) args[0]).longValue()) {
                updateStickerTabs();
            }
        } else if (id == NotificationCenter.emojiDidLoaded && this.stickersGridView != null) {
            count = this.stickersGridView.getChildCount();
            while (a < count) {
                View child = this.stickersGridView.getChildAt(a);
                if (child instanceof StickerSetNameCell) {
                    child.invalidate();
                }
                a++;
            }
        }
    }
}
