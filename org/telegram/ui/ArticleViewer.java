package org.telegram.ui;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.DataSetObserver;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Cap;
import android.graphics.Paint.Style;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffColorFilter;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.SurfaceTexture;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.support.annotation.Keep;
import android.support.v4.content.FileProvider;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.Layout.Alignment;
import android.text.Spannable;
import android.text.Spannable.Factory;
import android.text.SpannableStringBuilder;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.TextUtils.TruncateAt;
import android.text.style.MetricAffectingSpan;
import android.util.LongSparseArray;
import android.util.SparseArray;
import android.view.GestureDetector;
import android.view.GestureDetector.OnDoubleTapListener;
import android.view.GestureDetector.OnGestureListener;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.TextureView;
import android.view.VelocityTracker;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.View.OnApplyWindowInsetsListener;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.WindowInsets;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.view.animation.DecelerateInterpolator;
import android.webkit.CookieManager;
import android.webkit.WebChromeClient;
import android.webkit.WebChromeClient.CustomViewCallback;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.TextView;
import android.widget.Toast;
import java.io.File;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.ApplicationLoader;
import org.telegram.messenger.DownloadController;
import org.telegram.messenger.DownloadController.FileDownloadProgressListener;
import org.telegram.messenger.Emoji;
import org.telegram.messenger.FileLoader;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.ImageLoader;
import org.telegram.messenger.ImageReceiver;
import org.telegram.messenger.ImageReceiver.BitmapHolder;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MediaController;
import org.telegram.messenger.MessageObject;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.MessagesStorage;
import org.telegram.messenger.NotificationCenter;
import org.telegram.messenger.NotificationCenter.NotificationCenterDelegate;
import org.telegram.messenger.UserConfig;
import org.telegram.messenger.beta.R;
import org.telegram.messenger.browser.Browser;
import org.telegram.messenger.exoplayer2.C;
import org.telegram.messenger.exoplayer2.ui.AspectRatioFrameLayout;
import org.telegram.messenger.support.widget.GridLayoutManager;
import org.telegram.messenger.support.widget.LinearLayoutManager;
import org.telegram.messenger.support.widget.RecyclerView;
import org.telegram.messenger.support.widget.RecyclerView.Adapter;
import org.telegram.messenger.support.widget.RecyclerView.ItemDecoration;
import org.telegram.messenger.support.widget.RecyclerView.LayoutManager;
import org.telegram.messenger.support.widget.RecyclerView.OnScrollListener;
import org.telegram.messenger.support.widget.RecyclerView.State;
import org.telegram.messenger.support.widget.RecyclerView.ViewHolder;
import org.telegram.messenger.support.widget.helper.ItemTouchHelper.Callback;
import org.telegram.tgnet.ConnectionsManager;
import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC.Chat;
import org.telegram.tgnet.TLRPC.Document;
import org.telegram.tgnet.TLRPC.DocumentAttribute;
import org.telegram.tgnet.TLRPC.FileLocation;
import org.telegram.tgnet.TLRPC.PageBlock;
import org.telegram.tgnet.TLRPC.Photo;
import org.telegram.tgnet.TLRPC.PhotoSize;
import org.telegram.tgnet.TLRPC.RichText;
import org.telegram.tgnet.TLRPC.TL_channels_joinChannel;
import org.telegram.tgnet.TLRPC.TL_contacts_resolveUsername;
import org.telegram.tgnet.TLRPC.TL_contacts_resolvedPeer;
import org.telegram.tgnet.TLRPC.TL_documentAttributeAudio;
import org.telegram.tgnet.TLRPC.TL_error;
import org.telegram.tgnet.TLRPC.TL_fileLocationUnavailable;
import org.telegram.tgnet.TLRPC.TL_messageActionChatAddUser;
import org.telegram.tgnet.TLRPC.TL_messages_getWebPage;
import org.telegram.tgnet.TLRPC.TL_pageBlockAudio;
import org.telegram.tgnet.TLRPC.TL_pageBlockAuthorDate;
import org.telegram.tgnet.TLRPC.TL_pageBlockBlockquote;
import org.telegram.tgnet.TLRPC.TL_pageBlockChannel;
import org.telegram.tgnet.TLRPC.TL_pageBlockCollage;
import org.telegram.tgnet.TLRPC.TL_pageBlockCover;
import org.telegram.tgnet.TLRPC.TL_pageBlockDivider;
import org.telegram.tgnet.TLRPC.TL_pageBlockEmbed;
import org.telegram.tgnet.TLRPC.TL_pageBlockEmbedPost;
import org.telegram.tgnet.TLRPC.TL_pageBlockFooter;
import org.telegram.tgnet.TLRPC.TL_pageBlockHeader;
import org.telegram.tgnet.TLRPC.TL_pageBlockList;
import org.telegram.tgnet.TLRPC.TL_pageBlockParagraph;
import org.telegram.tgnet.TLRPC.TL_pageBlockPhoto;
import org.telegram.tgnet.TLRPC.TL_pageBlockPreformatted;
import org.telegram.tgnet.TLRPC.TL_pageBlockPullquote;
import org.telegram.tgnet.TLRPC.TL_pageBlockSlideshow;
import org.telegram.tgnet.TLRPC.TL_pageBlockSubheader;
import org.telegram.tgnet.TLRPC.TL_pageBlockSubtitle;
import org.telegram.tgnet.TLRPC.TL_pageBlockTitle;
import org.telegram.tgnet.TLRPC.TL_pageBlockVideo;
import org.telegram.tgnet.TLRPC.TL_pageFull;
import org.telegram.tgnet.TLRPC.TL_photo;
import org.telegram.tgnet.TLRPC.TL_textBold;
import org.telegram.tgnet.TLRPC.TL_textConcat;
import org.telegram.tgnet.TLRPC.TL_textEmail;
import org.telegram.tgnet.TLRPC.TL_textEmpty;
import org.telegram.tgnet.TLRPC.TL_textFixed;
import org.telegram.tgnet.TLRPC.TL_textItalic;
import org.telegram.tgnet.TLRPC.TL_textPlain;
import org.telegram.tgnet.TLRPC.TL_textStrike;
import org.telegram.tgnet.TLRPC.TL_textUnderline;
import org.telegram.tgnet.TLRPC.TL_textUrl;
import org.telegram.tgnet.TLRPC.TL_updateNewChannelMessage;
import org.telegram.tgnet.TLRPC.TL_user;
import org.telegram.tgnet.TLRPC.TL_webPage;
import org.telegram.tgnet.TLRPC.Update;
import org.telegram.tgnet.TLRPC.Updates;
import org.telegram.tgnet.TLRPC.User;
import org.telegram.tgnet.TLRPC.WebPage;
import org.telegram.ui.ActionBar.ActionBar;
import org.telegram.ui.ActionBar.ActionBar.ActionBarMenuOnItemClick;
import org.telegram.ui.ActionBar.ActionBarMenu;
import org.telegram.ui.ActionBar.ActionBarMenuItem;
import org.telegram.ui.ActionBar.ActionBarPopupWindow;
import org.telegram.ui.ActionBar.ActionBarPopupWindow.ActionBarPopupWindowLayout;
import org.telegram.ui.ActionBar.ActionBarPopupWindow.OnDispatchKeyEventListener;
import org.telegram.ui.ActionBar.AlertDialog;
import org.telegram.ui.ActionBar.BackDrawable;
import org.telegram.ui.ActionBar.BaseFragment;
import org.telegram.ui.ActionBar.BottomSheet.Builder;
import org.telegram.ui.ActionBar.DrawerLayoutContainer;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Components.AlertsCreator;
import org.telegram.ui.Components.AnimatedFileDrawable;
import org.telegram.ui.Components.AvatarDrawable;
import org.telegram.ui.Components.ClippingImageView;
import org.telegram.ui.Components.ContextProgressView;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.ui.Components.LinkPath;
import org.telegram.ui.Components.RadialProgress;
import org.telegram.ui.Components.RecyclerListView;
import org.telegram.ui.Components.RecyclerListView.Holder;
import org.telegram.ui.Components.RecyclerListView.OnItemClickListener;
import org.telegram.ui.Components.RecyclerListView.OnItemLongClickListener;
import org.telegram.ui.Components.RecyclerListView.SelectionAdapter;
import org.telegram.ui.Components.Scroller;
import org.telegram.ui.Components.SeekBar;
import org.telegram.ui.Components.SeekBar.SeekBarDelegate;
import org.telegram.ui.Components.ShareAlert;
import org.telegram.ui.Components.TextPaintSpan;
import org.telegram.ui.Components.TextPaintUrlSpan;
import org.telegram.ui.Components.TypefaceSpan;
import org.telegram.ui.Components.VideoPlayer;
import org.telegram.ui.Components.VideoPlayer.VideoPlayerDelegate;
import org.telegram.ui.Components.WebPlayerView;
import org.telegram.ui.Components.WebPlayerView.WebPlayerViewDelegate;

public class ArticleViewer implements OnDoubleTapListener, OnGestureListener, NotificationCenterDelegate {
    @SuppressLint({"StaticFieldLeak"})
    private static volatile ArticleViewer Instance = null;
    private static final int TEXT_FLAG_ITALIC = 2;
    private static final int TEXT_FLAG_MEDIUM = 1;
    private static final int TEXT_FLAG_MONO = 4;
    private static final int TEXT_FLAG_REGULAR = 0;
    private static final int TEXT_FLAG_STRIKE = 32;
    private static final int TEXT_FLAG_UNDERLINE = 16;
    private static final int TEXT_FLAG_URL = 8;
    private static TextPaint audioTimePaint = new TextPaint(1);
    private static SparseArray<TextPaint> authorTextPaints = new SparseArray();
    private static SparseArray<TextPaint> captionTextPaints = new SparseArray();
    private static TextPaint channelNamePaint = null;
    private static Paint colorPaint = null;
    private static DecelerateInterpolator decelerateInterpolator = null;
    private static Paint dividerPaint = null;
    private static Paint dotsPaint = null;
    private static TextPaint embedPostAuthorPaint = null;
    private static SparseArray<TextPaint> embedPostCaptionTextPaints = new SparseArray();
    private static TextPaint embedPostDatePaint = null;
    private static SparseArray<TextPaint> embedPostTextPaints = new SparseArray();
    private static SparseArray<TextPaint> embedTextPaints = new SparseArray();
    private static TextPaint errorTextPaint = null;
    private static SparseArray<TextPaint> footerTextPaints = new SparseArray();
    private static final int gallery_menu_openin = 3;
    private static final int gallery_menu_save = 1;
    private static final int gallery_menu_share = 2;
    private static SparseArray<TextPaint> headerTextPaints = new SparseArray();
    private static SparseArray<TextPaint> listTextPaints = new SparseArray();
    private static TextPaint listTextPointerPaint;
    private static SparseArray<TextPaint> paragraphTextPaints = new SparseArray();
    private static Paint preformattedBackgroundPaint;
    private static SparseArray<TextPaint> preformattedTextPaints = new SparseArray();
    private static Drawable[] progressDrawables;
    private static Paint progressPaint;
    private static Paint quoteLinePaint;
    private static SparseArray<TextPaint> quoteTextPaints = new SparseArray();
    private static Paint selectorPaint;
    private static SparseArray<TextPaint> slideshowTextPaints = new SparseArray();
    private static SparseArray<TextPaint> subheaderTextPaints = new SparseArray();
    private static SparseArray<TextPaint> subquoteTextPaints = new SparseArray();
    private static SparseArray<TextPaint> subtitleTextPaints = new SparseArray();
    private static SparseArray<TextPaint> titleTextPaints = new SparseArray();
    private static Paint urlPaint;
    private static SparseArray<TextPaint> videoTextPaints = new SparseArray();
    private ActionBar actionBar;
    private WebpageAdapter adapter;
    private HashMap<String, Integer> anchors = new HashMap();
    private float animateToScale;
    private float animateToX;
    private float animateToY;
    private ClippingImageView animatingImageView;
    private Runnable animationEndRunnable;
    private int animationInProgress;
    private long animationStartTime;
    private float animationValue;
    private float[][] animationValues = ((float[][]) Array.newInstance(float.class, new int[]{2, 8}));
    private AspectRatioFrameLayout aspectRatioFrameLayout;
    private boolean attachedToWindow;
    private HashMap<TL_pageBlockAudio, MessageObject> audioBlocks = new HashMap();
    private ArrayList<MessageObject> audioMessages = new ArrayList();
    private ImageView backButton;
    private BackDrawable backDrawable;
    private Paint backgroundPaint;
    private View barBackground;
    private Paint blackPaint = new Paint();
    private ArrayList<PageBlock> blocks = new ArrayList();
    private FrameLayout bottomLayout;
    private boolean canDragDown = true;
    private boolean canZoom = true;
    private TextView captionTextView;
    private TextView captionTextViewNew;
    private TextView captionTextViewOld;
    private ImageReceiver centerImage = new ImageReceiver();
    private boolean changingPage;
    private TL_pageBlockChannel channelBlock;
    private boolean checkingForLongPress = false;
    private boolean collapsed;
    private ColorCell[] colorCells = new ColorCell[3];
    private FrameLayout containerView;
    private int[] coords = new int[2];
    private ArrayList<BlockEmbedCell> createdWebViews = new ArrayList();
    private int currentAccount;
    private AnimatorSet currentActionBarAnimation;
    private AnimatedFileDrawable currentAnimation;
    private String[] currentFileNames = new String[3];
    private int currentHeaderHeight;
    private int currentIndex;
    private PageBlock currentMedia;
    private WebPage currentPage;
    private PlaceProviderObject currentPlaceObject;
    private WebPlayerView currentPlayingVideo;
    private int currentRotation;
    private BitmapHolder currentThumb;
    private View customView;
    private CustomViewCallback customViewCallback;
    private boolean disableShowCheck;
    private boolean discardTap;
    private boolean dontResetZoomOnFirstLayout;
    private boolean doubleTap;
    private float dragY;
    private boolean draggingDown;
    private boolean drawBlockSelection;
    private FontCell[] fontCells = new FontCell[2];
    private final int fontSizeCount = 5;
    private AspectRatioFrameLayout fullscreenAspectRatioView;
    private TextureView fullscreenTextureView;
    private FrameLayout fullscreenVideoContainer;
    private WebPlayerView fullscreenedVideo;
    private GestureDetector gestureDetector;
    private Paint headerPaint = new Paint();
    private Paint headerProgressPaint = new Paint();
    private FrameLayout headerView;
    private PlaceProviderObject hideAfterAnimation;
    private AnimatorSet imageMoveAnimation;
    private ArrayList<PageBlock> imagesArr = new ArrayList();
    private DecelerateInterpolator interpolator = new DecelerateInterpolator(1.5f);
    private boolean invalidCoords;
    private boolean isActionBarVisible = true;
    private boolean isPhotoVisible;
    private boolean isPlaying;
    private int isRtl = -1;
    private boolean isVisible;
    private Object lastInsets;
    private Drawable layerShadowDrawable;
    private LinearLayoutManager layoutManager;
    private ImageReceiver leftImage = new ImageReceiver();
    private RecyclerListView listView;
    private Chat loadedChannel;
    private boolean loadingChannel;
    private float maxX;
    private float maxY;
    private ActionBarMenuItem menuItem;
    private float minX;
    private float minY;
    private float moveStartX;
    private float moveStartY;
    private boolean moving;
    private boolean nightModeEnabled;
    private FrameLayout nightModeHintView;
    private ImageView nightModeImageView;
    private int openUrlReqId;
    private ArrayList<WebPage> pagesStack = new ArrayList();
    private Activity parentActivity;
    private BaseFragment parentFragment;
    private CheckForLongPress pendingCheckForLongPress = null;
    private CheckForTap pendingCheckForTap = null;
    private Runnable photoAnimationEndRunnable;
    private int photoAnimationInProgress;
    private PhotoBackgroundDrawable photoBackgroundDrawable = new PhotoBackgroundDrawable(Theme.ACTION_BAR_VIDEO_EDIT_COLOR);
    private ArrayList<PageBlock> photoBlocks = new ArrayList();
    private View photoContainerBackground;
    private FrameLayoutDrawer photoContainerView;
    private long photoTransitionAnimationStartTime;
    private float pinchCenterX;
    private float pinchCenterY;
    private float pinchStartDistance;
    private float pinchStartScale = 1.0f;
    private float pinchStartX;
    private float pinchStartY;
    private ActionBarPopupWindowLayout popupLayout;
    private Rect popupRect;
    private ActionBarPopupWindow popupWindow;
    private int pressCount = 0;
    private int pressedLayoutY;
    private TextPaintUrlSpan pressedLink;
    private StaticLayout pressedLinkOwnerLayout;
    private View pressedLinkOwnerView;
    private int previewsReqId;
    private ContextProgressView progressView;
    private AnimatorSet progressViewAnimation;
    private RadialProgressView[] radialProgressViews = new RadialProgressView[3];
    private ImageReceiver rightImage = new ImageReceiver();
    private float scale = 1.0f;
    private Paint scrimPaint;
    private Scroller scroller;
    private int selectedColor = 0;
    private int selectedFont = 0;
    private int selectedFontSize = 2;
    private ActionBarMenuItem settingsButton;
    private ImageView shareButton;
    private FrameLayout shareContainer;
    private PlaceProviderObject showAfterAnimation;
    private Drawable slideDotBigDrawable;
    private Drawable slideDotDrawable;
    private int switchImageAfterAnimation;
    private boolean textureUploaded;
    private long transitionAnimationStartTime;
    private float translationX;
    private float translationY;
    private Runnable updateProgressRunnable = new Runnable() {
        public void run() {
            if (!(ArticleViewer.this.videoPlayer == null || ArticleViewer.this.videoPlayerSeekbar == null || ArticleViewer.this.videoPlayerSeekbar.isDragging())) {
                ArticleViewer.this.videoPlayerSeekbar.setProgress(((float) ArticleViewer.this.videoPlayer.getCurrentPosition()) / ((float) ArticleViewer.this.videoPlayer.getDuration()));
                ArticleViewer.this.videoPlayerControlFrameLayout.invalidate();
                ArticleViewer.this.updateVideoPlayerTime();
            }
            if (ArticleViewer.this.isPlaying) {
                AndroidUtilities.runOnUIThread(ArticleViewer.this.updateProgressRunnable, 100);
            }
        }
    };
    private LinkPath urlPath = new LinkPath();
    private VelocityTracker velocityTracker;
    private float videoCrossfadeAlpha;
    private long videoCrossfadeAlphaLastTime;
    private boolean videoCrossfadeStarted;
    private ImageView videoPlayButton;
    private VideoPlayer videoPlayer;
    private FrameLayout videoPlayerControlFrameLayout;
    private SeekBar videoPlayerSeekbar;
    private TextView videoPlayerTime;
    private TextureView videoTextureView;
    private Dialog visibleDialog;
    private boolean wasLayout;
    private LayoutParams windowLayoutParams;
    private WindowView windowView;
    private boolean zoomAnimation;
    private boolean zooming;

    class AnonymousClass27 implements Runnable {
        final /* synthetic */ AnimatorSet val$animatorSet;

        AnonymousClass27(AnimatorSet animatorSet) {
            this.val$animatorSet = animatorSet;
        }

        public void run() {
            NotificationCenter.getInstance(ArticleViewer.this.currentAccount).setAllowedNotificationsDutingAnimation(new int[]{NotificationCenter.dialogsNeedReload, NotificationCenter.closeChats});
            NotificationCenter.getInstance(ArticleViewer.this.currentAccount).setAnimationInProgress(true);
            this.val$animatorSet.start();
        }
    }

    private class BlockAuthorDateCell extends View {
        private TL_pageBlockAuthorDate currentBlock;
        private int lastCreatedWidth;
        private StaticLayout textLayout;
        private int textX;
        private int textY = AndroidUtilities.dp(8.0f);

        public BlockAuthorDateCell(Context context) {
            super(context);
        }

        public void setBlock(TL_pageBlockAuthorDate block) {
            this.currentBlock = block;
            this.lastCreatedWidth = 0;
            requestLayout();
        }

        public boolean onTouchEvent(MotionEvent event) {
            if (!ArticleViewer.this.checkLayoutForLinks(event, this, this.textLayout, this.textX, this.textY)) {
                if (!super.onTouchEvent(event)) {
                    return false;
                }
            }
            return true;
        }

        protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
            CharSequence text;
            int width = MeasureSpec.getSize(widthMeasureSpec);
            int height = 0;
            if (this.currentBlock == null) {
                height = 1;
            } else if (r1.lastCreatedWidth != width) {
                Spannable spannableAuthor;
                MetricAffectingSpan[] spans;
                CharSequence text2;
                int idx;
                Spannable spannable;
                int a;
                CharSequence author = ArticleViewer.this.getText(r1.currentBlock.author, r1.currentBlock.author, r1.currentBlock);
                if (author instanceof Spannable) {
                    spannableAuthor = (Spannable) author;
                    spans = (MetricAffectingSpan[]) spannableAuthor.getSpans(0, author.length(), MetricAffectingSpan.class);
                } else {
                    spannableAuthor = null;
                    spans = null;
                }
                if (r1.currentBlock.published_date != 0 && !TextUtils.isEmpty(author)) {
                    text2 = LocaleController.formatString("ArticleDateByAuthor", R.string.ArticleDateByAuthor, LocaleController.getInstance().chatFullDate.format(((long) r1.currentBlock.published_date) * 1000), author);
                } else if (TextUtils.isEmpty(author)) {
                    text2 = LocaleController.getInstance().chatFullDate.format(((long) r1.currentBlock.published_date) * 1000);
                    text = text2;
                    if (spans != null) {
                        try {
                            if (spans.length > 0) {
                                idx = TextUtils.indexOf(text, author);
                                if (idx != -1) {
                                    spannable = Factory.getInstance().newSpannable(text);
                                    text = spannable;
                                    for (a = 0; a < spans.length; a++) {
                                        spannable.setSpan(spans[a], spannableAuthor.getSpanStart(spans[a]) + idx, spannableAuthor.getSpanEnd(spans[a]) + idx, 33);
                                    }
                                }
                            }
                        } catch (Throwable e) {
                            text2 = text;
                            FileLog.e(e);
                        }
                    }
                    text2 = text;
                    r1.textLayout = ArticleViewer.this.createLayoutForText(text2, null, width - AndroidUtilities.dp(36.0f), r1.currentBlock);
                    if (r1.textLayout != null) {
                        height = 0 + (AndroidUtilities.dp(16.0f) + r1.textLayout.getHeight());
                        if (ArticleViewer.this.isRtl != 1) {
                            r1.textX = (int) Math.floor((double) ((((float) width) - r1.textLayout.getLineWidth(0)) - ((float) AndroidUtilities.dp(16.0f))));
                        } else {
                            r1.textX = AndroidUtilities.dp(18.0f);
                        }
                    }
                } else {
                    text2 = LocaleController.formatString("ArticleByAuthor", R.string.ArticleByAuthor, author);
                }
                text = text2;
                if (spans != null) {
                    if (spans.length > 0) {
                        idx = TextUtils.indexOf(text, author);
                        if (idx != -1) {
                            spannable = Factory.getInstance().newSpannable(text);
                            text = spannable;
                            for (a = 0; a < spans.length; a++) {
                                spannable.setSpan(spans[a], spannableAuthor.getSpanStart(spans[a]) + idx, spannableAuthor.getSpanEnd(spans[a]) + idx, 33);
                            }
                        }
                    }
                }
                text2 = text;
                r1.textLayout = ArticleViewer.this.createLayoutForText(text2, null, width - AndroidUtilities.dp(36.0f), r1.currentBlock);
                if (r1.textLayout != null) {
                    height = 0 + (AndroidUtilities.dp(16.0f) + r1.textLayout.getHeight());
                    if (ArticleViewer.this.isRtl != 1) {
                        r1.textX = AndroidUtilities.dp(18.0f);
                    } else {
                        r1.textX = (int) Math.floor((double) ((((float) width) - r1.textLayout.getLineWidth(0)) - ((float) AndroidUtilities.dp(16.0f))));
                    }
                }
            }
            setMeasuredDimension(width, height);
        }

        protected void onDraw(Canvas canvas) {
            if (!(this.currentBlock == null || this.textLayout == null)) {
                canvas.save();
                canvas.translate((float) this.textX, (float) this.textY);
                ArticleViewer.this.drawLayoutLink(canvas, this.textLayout);
                this.textLayout.draw(canvas);
                canvas.restore();
            }
        }
    }

    private class BlockBlockquoteCell extends View {
        private TL_pageBlockBlockquote currentBlock;
        private boolean hasRtl;
        private int lastCreatedWidth;
        private StaticLayout textLayout;
        private StaticLayout textLayout2;
        private int textX;
        private int textY = AndroidUtilities.dp(8.0f);
        private int textY2;

        public BlockBlockquoteCell(Context context) {
            super(context);
        }

        public void setBlock(TL_pageBlockBlockquote block) {
            this.currentBlock = block;
            this.lastCreatedWidth = 0;
            requestLayout();
        }

        public boolean onTouchEvent(MotionEvent event) {
            if (!(ArticleViewer.this.checkLayoutForLinks(event, this, this.textLayout, this.textX, this.textY) || ArticleViewer.this.checkLayoutForLinks(event, this, this.textLayout2, this.textX, this.textY2))) {
                if (!super.onTouchEvent(event)) {
                    return false;
                }
            }
            return true;
        }

        protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
            int width = MeasureSpec.getSize(widthMeasureSpec);
            int height = 0;
            if (this.currentBlock == null) {
                height = 1;
            } else if (this.lastCreatedWidth != width) {
                int textWidth = width - AndroidUtilities.dp(50.0f);
                if (this.currentBlock.level > 0) {
                    textWidth -= AndroidUtilities.dp((float) (this.currentBlock.level * 14));
                }
                this.textLayout = ArticleViewer.this.createLayoutForText(null, this.currentBlock.text, textWidth, this.currentBlock);
                int a = 0;
                this.hasRtl = false;
                if (this.textLayout != null) {
                    height = 0 + (AndroidUtilities.dp(8.0f) + this.textLayout.getHeight());
                    int count = this.textLayout.getLineCount();
                    while (a < count) {
                        if (this.textLayout.getLineLeft(a) > 0.0f) {
                            ArticleViewer.this.isRtl = 1;
                            this.hasRtl = true;
                            break;
                        }
                        a++;
                    }
                }
                if (this.currentBlock.level > 0) {
                    if (this.hasRtl) {
                        this.textX = AndroidUtilities.dp((float) (14 + (this.currentBlock.level * 14)));
                    } else {
                        this.textX = AndroidUtilities.dp((float) (14 * this.currentBlock.level)) + AndroidUtilities.dp(32.0f);
                    }
                } else if (this.hasRtl) {
                    this.textX = AndroidUtilities.dp(14.0f);
                } else {
                    this.textX = AndroidUtilities.dp(32.0f);
                }
                this.textLayout2 = ArticleViewer.this.createLayoutForText(null, this.currentBlock.caption, textWidth, this.currentBlock);
                if (this.textLayout2 != null) {
                    this.textY2 = AndroidUtilities.dp(8.0f) + height;
                    height += AndroidUtilities.dp(8.0f) + this.textLayout2.getHeight();
                }
                if (height != 0) {
                    height += AndroidUtilities.dp(8.0f);
                }
            }
            setMeasuredDimension(width, height);
        }

        protected void onDraw(Canvas canvas) {
            if (this.currentBlock != null) {
                if (this.textLayout != null) {
                    canvas.save();
                    canvas.translate((float) this.textX, (float) this.textY);
                    ArticleViewer.this.drawLayoutLink(canvas, this.textLayout);
                    this.textLayout.draw(canvas);
                    canvas.restore();
                }
                if (this.textLayout2 != null) {
                    canvas.save();
                    canvas.translate((float) this.textX, (float) this.textY2);
                    ArticleViewer.this.drawLayoutLink(canvas, this.textLayout2);
                    this.textLayout2.draw(canvas);
                    canvas.restore();
                }
                if (this.hasRtl) {
                    int x = getMeasuredWidth() - AndroidUtilities.dp(20.0f);
                    canvas.drawRect((float) x, (float) AndroidUtilities.dp(6.0f), (float) (AndroidUtilities.dp(2.0f) + x), (float) (getMeasuredHeight() - AndroidUtilities.dp(6.0f)), ArticleViewer.quoteLinePaint);
                } else {
                    canvas.drawRect((float) AndroidUtilities.dp((float) (18 + (this.currentBlock.level * 14))), (float) AndroidUtilities.dp(6.0f), (float) AndroidUtilities.dp((float) (20 + (this.currentBlock.level * 14))), (float) (getMeasuredHeight() - AndroidUtilities.dp(6.0f)), ArticleViewer.quoteLinePaint);
                }
                if (this.currentBlock.level > 0) {
                    canvas.drawRect((float) AndroidUtilities.dp(18.0f), 0.0f, (float) AndroidUtilities.dp(20.0f), (float) (getMeasuredHeight() - (this.currentBlock.bottom ? AndroidUtilities.dp(6.0f) : 0)), ArticleViewer.quoteLinePaint);
                }
            }
        }
    }

    private class BlockChannelCell extends FrameLayout {
        private Paint backgroundPaint;
        private int buttonWidth;
        private AnimatorSet currentAnimation;
        private TL_pageBlockChannel currentBlock;
        private int currentState;
        private int currentType;
        private ImageView imageView;
        private int lastCreatedWidth;
        private ContextProgressView progressView;
        private StaticLayout textLayout;
        private TextView textView;
        private int textX = AndroidUtilities.dp(18.0f);
        private int textX2;
        private int textY = AndroidUtilities.dp(11.0f);
        private int textY2 = AndroidUtilities.dp(11.5f);

        public BlockChannelCell(Context context, int type) {
            super(context);
            setWillNotDraw(false);
            this.backgroundPaint = new Paint();
            this.currentType = type;
            this.textView = new TextView(context);
            this.textView.setTextSize(1, 14.0f);
            this.textView.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
            this.textView.setText(LocaleController.getString("ChannelJoin", R.string.ChannelJoin));
            this.textView.setGravity(19);
            addView(this.textView, LayoutHelper.createFrame(-2, 39, 53));
            this.textView.setOnClickListener(new OnClickListener(ArticleViewer.this) {
                public void onClick(View v) {
                    if (BlockChannelCell.this.currentState == 0) {
                        BlockChannelCell.this.setState(1, true);
                        ArticleViewer.this.joinChannel(BlockChannelCell.this, ArticleViewer.this.loadedChannel);
                    }
                }
            });
            this.imageView = new ImageView(context);
            this.imageView.setImageResource(R.drawable.list_check);
            this.imageView.setScaleType(ScaleType.CENTER);
            addView(this.imageView, LayoutHelper.createFrame(39, 39, 53));
            this.progressView = new ContextProgressView(context, 0);
            addView(this.progressView, LayoutHelper.createFrame(39, 39, 53));
        }

        public void setBlock(TL_pageBlockChannel block) {
            this.currentBlock = block;
            int color = ArticleViewer.this.getSelectedColor();
            if (this.currentType == 0) {
                this.textView.setTextColor(-14840360);
                if (color == 0) {
                    this.backgroundPaint.setColor(-526345);
                } else if (color == 1) {
                    this.backgroundPaint.setColor(-1712440);
                } else if (color == 2) {
                    this.backgroundPaint.setColor(-14277082);
                }
                this.imageView.setColorFilter(new PorterDuffColorFilter(-6710887, Mode.MULTIPLY));
            } else {
                this.textView.setTextColor(-1);
                this.backgroundPaint.setColor(Theme.ACTION_BAR_PHOTO_VIEWER_COLOR);
                this.imageView.setColorFilter(new PorterDuffColorFilter(-1, Mode.MULTIPLY));
            }
            this.lastCreatedWidth = 0;
            Chat channel = MessagesController.getInstance(ArticleViewer.this.currentAccount).getChat(Integer.valueOf(block.channel.id));
            if (channel != null) {
                if (!channel.min) {
                    ArticleViewer.this.loadedChannel = channel;
                    if (!channel.left || channel.kicked) {
                        setState(4, false);
                    } else {
                        setState(0, false);
                    }
                    requestLayout();
                }
            }
            ArticleViewer.this.loadChannel(this, block.channel);
            setState(1, false);
            requestLayout();
        }

        public void setState(int state, boolean animated) {
            if (this.currentAnimation != null) {
                this.currentAnimation.cancel();
            }
            this.currentState = state;
            float f = 0.0f;
            float f2 = 0.1f;
            if (animated) {
                this.currentAnimation = new AnimatorSet();
                AnimatorSet animatorSet = this.currentAnimation;
                Animator[] animatorArr = new Animator[9];
                TextView textView = this.textView;
                String str = "alpha";
                float[] fArr = new float[1];
                fArr[0] = state == 0 ? 1.0f : 0.0f;
                animatorArr[0] = ObjectAnimator.ofFloat(textView, str, fArr);
                textView = this.textView;
                str = "scaleX";
                fArr = new float[1];
                fArr[0] = state == 0 ? 1.0f : 0.1f;
                animatorArr[1] = ObjectAnimator.ofFloat(textView, str, fArr);
                textView = this.textView;
                str = "scaleY";
                fArr = new float[1];
                fArr[0] = state == 0 ? 1.0f : 0.1f;
                animatorArr[2] = ObjectAnimator.ofFloat(textView, str, fArr);
                ContextProgressView contextProgressView = this.progressView;
                String str2 = "alpha";
                float[] fArr2 = new float[1];
                fArr2[0] = state == 1 ? 1.0f : 0.0f;
                animatorArr[3] = ObjectAnimator.ofFloat(contextProgressView, str2, fArr2);
                contextProgressView = this.progressView;
                str2 = "scaleX";
                fArr2 = new float[1];
                fArr2[0] = state == 1 ? 1.0f : 0.1f;
                animatorArr[4] = ObjectAnimator.ofFloat(contextProgressView, str2, fArr2);
                contextProgressView = this.progressView;
                str2 = "scaleY";
                fArr2 = new float[1];
                fArr2[0] = state == 1 ? 1.0f : 0.1f;
                animatorArr[5] = ObjectAnimator.ofFloat(contextProgressView, str2, fArr2);
                ImageView imageView = this.imageView;
                str2 = "alpha";
                fArr2 = new float[1];
                if (state == 2) {
                    f = 1.0f;
                }
                fArr2[0] = f;
                animatorArr[6] = ObjectAnimator.ofFloat(imageView, str2, fArr2);
                ImageView imageView2 = this.imageView;
                str = "scaleX";
                fArr = new float[1];
                fArr[0] = state == 2 ? 1.0f : 0.1f;
                animatorArr[7] = ObjectAnimator.ofFloat(imageView2, str, fArr);
                imageView2 = this.imageView;
                str = "scaleY";
                float[] fArr3 = new float[1];
                if (state == 2) {
                    f2 = 1.0f;
                }
                fArr3[0] = f2;
                animatorArr[8] = ObjectAnimator.ofFloat(imageView2, str, fArr3);
                animatorSet.playTogether(animatorArr);
                this.currentAnimation.setDuration(150);
                this.currentAnimation.start();
                return;
            }
            this.textView.setAlpha(state == 0 ? 1.0f : 0.0f);
            this.textView.setScaleX(state == 0 ? 1.0f : 0.1f);
            this.textView.setScaleY(state == 0 ? 1.0f : 0.1f);
            this.progressView.setAlpha(state == 1 ? 1.0f : 0.0f);
            this.progressView.setScaleX(state == 1 ? 1.0f : 0.1f);
            this.progressView.setScaleY(state == 1 ? 1.0f : 0.1f);
            ImageView imageView3 = this.imageView;
            if (state == 2) {
                f = 1.0f;
            }
            imageView3.setAlpha(f);
            this.imageView.setScaleX(state == 2 ? 1.0f : 0.1f);
            ImageView imageView4 = this.imageView;
            if (state == 2) {
                f2 = 1.0f;
            }
            imageView4.setScaleY(f2);
        }

        public boolean onTouchEvent(MotionEvent event) {
            if (this.currentType != 0) {
                return super.onTouchEvent(event);
            }
            boolean z;
            if (!ArticleViewer.this.checkLayoutForLinks(event, this, this.textLayout, this.textX, this.textY)) {
                if (!super.onTouchEvent(event)) {
                    z = false;
                    return z;
                }
            }
            z = true;
            return z;
        }

        protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
            int width = MeasureSpec.getSize(widthMeasureSpec);
            setMeasuredDimension(width, AndroidUtilities.dp(48.0f));
            this.textView.measure(MeasureSpec.makeMeasureSpec(MeasureSpec.getSize(widthMeasureSpec), Integer.MIN_VALUE), MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(39.0f), 1073741824));
            this.buttonWidth = this.textView.getMeasuredWidth();
            this.progressView.measure(MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(39.0f), 1073741824), MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(39.0f), 1073741824));
            this.imageView.measure(MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(39.0f), 1073741824), MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(39.0f), 1073741824));
            if (this.currentBlock != null && this.lastCreatedWidth != width) {
                this.textLayout = ArticleViewer.this.createLayoutForText(this.currentBlock.channel.title, null, (width - AndroidUtilities.dp(52.0f)) - this.buttonWidth, this.currentBlock);
                this.textX2 = (getMeasuredWidth() - this.textX) - this.buttonWidth;
            }
        }

        protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
            this.imageView.layout((this.textX2 + (this.buttonWidth / 2)) - AndroidUtilities.dp(19.0f), 0, (this.textX2 + (this.buttonWidth / 2)) + AndroidUtilities.dp(20.0f), AndroidUtilities.dp(39.0f));
            this.progressView.layout((this.textX2 + (this.buttonWidth / 2)) - AndroidUtilities.dp(19.0f), 0, (this.textX2 + (this.buttonWidth / 2)) + AndroidUtilities.dp(20.0f), AndroidUtilities.dp(39.0f));
            this.textView.layout(this.textX2, 0, this.textX2 + this.textView.getMeasuredWidth(), this.textView.getMeasuredHeight());
        }

        protected void onDraw(Canvas canvas) {
            if (this.currentBlock != null) {
                canvas.drawRect(0.0f, 0.0f, (float) getMeasuredWidth(), (float) AndroidUtilities.dp(39.0f), this.backgroundPaint);
                if (this.textLayout != null) {
                    canvas.save();
                    canvas.translate((float) this.textX, (float) this.textY);
                    ArticleViewer.this.drawLayoutLink(canvas, this.textLayout);
                    this.textLayout.draw(canvas);
                    canvas.restore();
                }
            }
        }
    }

    private class BlockCollageCell extends FrameLayout {
        private Adapter adapter;
        private TL_pageBlockCollage currentBlock;
        private GridLayoutManager gridLayoutManager;
        private boolean inLayout;
        private RecyclerListView innerListView;
        private int lastCreatedWidth;
        private int listX;
        private StaticLayout textLayout;
        private int textX;
        private int textY;

        protected void onMeasure(int r1, int r2) {
            /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.DecodeException: Load method exception in method: org.telegram.ui.ArticleViewer.BlockCollageCell.onMeasure(int, int):void
	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:116)
	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:249)
	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:256)
	at jadx.core.ProcessClass.process(ProcessClass.java:34)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:306)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler$1.run(JadxDecompiler.java:199)
Caused by: java.lang.NullPointerException
*/
            /*
            r0 = this;
            r0 = 1;
            r13.inLayout = r0;
            r1 = android.view.View.MeasureSpec.getSize(r14);
            r2 = r13.currentBlock;
            r3 = 0;
            if (r2 == 0) goto L_0x00c4;
        L_0x000c:
            r0 = r1;
            r2 = r13.currentBlock;
            r2 = r2.level;
            r4 = 1099956224; // 0x41900000 float:18.0 double:5.43450582E-315;
            if (r2 <= 0) goto L_0x0034;
            r2 = 14;
            r5 = r13.currentBlock;
            r5 = r5.level;
            r2 = r2 * r5;
            r2 = (float) r2;
            r2 = org.telegram.messenger.AndroidUtilities.dp(r2);
            r5 = org.telegram.messenger.AndroidUtilities.dp(r4);
            r2 = r2 + r5;
            r13.listX = r2;
            r13.textX = r2;
            r2 = r13.listX;
            r4 = org.telegram.messenger.AndroidUtilities.dp(r4);
            r2 = r2 + r4;
            r0 = r0 - r2;
            r2 = r0;
            goto L_0x0044;
            r13.listX = r3;
            r2 = org.telegram.messenger.AndroidUtilities.dp(r4);
            r13.textX = r2;
            r2 = 1108344832; // 0x42100000 float:36.0 double:5.47595105E-315;
            r2 = org.telegram.messenger.AndroidUtilities.dp(r2);
            r2 = r1 - r2;
            r4 = 1120403456; // 0x42c80000 float:100.0 double:5.53552857E-315;
            r4 = org.telegram.messenger.AndroidUtilities.dp(r4);
            r4 = r0 / r4;
            r5 = r13.currentBlock;
            r5 = r5.items;
            r5 = r5.size();
            r5 = (float) r5;
            r6 = (float) r4;
            r5 = r5 / r6;
            r5 = (double) r5;
            r5 = java.lang.Math.ceil(r5);
            r5 = (int) r5;
            r6 = r0 / r4;
            r7 = r13.gridLayoutManager;
            r7.setSpanCount(r4);
            r7 = r13.innerListView;
            r8 = r6 * r4;
            r9 = 1073741824; // 0x40000000 float:2.0 double:5.304989477E-315;
            r10 = org.telegram.messenger.AndroidUtilities.dp(r9);
            r8 = r8 + r10;
            r10 = 1073741824; // 0x40000000 float:2.0 double:5.304989477E-315;
            r8 = android.view.View.MeasureSpec.makeMeasureSpec(r8, r10);
            r11 = r6 * r5;
            r10 = android.view.View.MeasureSpec.makeMeasureSpec(r11, r10);
            r7.measure(r8, r10);
            r7 = r5 * r6;
            r8 = org.telegram.messenger.AndroidUtilities.dp(r9);
            r7 = r7 - r8;
            r8 = r13.lastCreatedWidth;
            r9 = 1090519040; // 0x41000000 float:8.0 double:5.38787994E-315;
            if (r8 == r1) goto L_0x00b1;
            r8 = org.telegram.ui.ArticleViewer.this;
            r10 = 0;
            r11 = r13.currentBlock;
            r11 = r11.caption;
            r12 = r13.currentBlock;
            r8 = r8.createLayoutForText(r10, r11, r2, r12);
            r13.textLayout = r8;
            r8 = r13.textLayout;
            if (r8 == 0) goto L_0x00b1;
            r8 = org.telegram.messenger.AndroidUtilities.dp(r9);
            r8 = r8 + r7;
            r13.textY = r8;
            r8 = org.telegram.messenger.AndroidUtilities.dp(r9);
            r10 = r13.textLayout;
            r10 = r10.getHeight();
            r8 = r8 + r10;
            r7 = r7 + r8;
            r8 = r13.currentBlock;
            r8 = r8.level;
            if (r8 <= 0) goto L_0x00c2;
            r8 = r13.currentBlock;
            r8 = r8.bottom;
            if (r8 != 0) goto L_0x00c2;
            r8 = org.telegram.messenger.AndroidUtilities.dp(r9);
            r7 = r7 + r8;
            r0 = r7;
            goto L_0x00c5;
            r13.setMeasuredDimension(r1, r0);
            r13.inLayout = r3;
            return;
            */
            throw new UnsupportedOperationException("Method not decompiled: org.telegram.ui.ArticleViewer.BlockCollageCell.onMeasure(int, int):void");
        }

        public BlockCollageCell(Context context) {
            super(context);
            this.innerListView = new RecyclerListView(context, ArticleViewer.this) {
                public void requestLayout() {
                    if (!BlockCollageCell.this.inLayout) {
                        super.requestLayout();
                    }
                }
            };
            this.innerListView.addItemDecoration(new ItemDecoration(ArticleViewer.this) {
                public void getItemOffsets(Rect outRect, View view, RecyclerView parent, State state) {
                    outRect.left = 0;
                    outRect.top = 0;
                    int dp = AndroidUtilities.dp(2.0f);
                    outRect.right = dp;
                    outRect.bottom = dp;
                }
            });
            RecyclerListView recyclerListView = this.innerListView;
            LayoutManager gridLayoutManager = new GridLayoutManager(context, 3);
            this.gridLayoutManager = gridLayoutManager;
            recyclerListView.setLayoutManager(gridLayoutManager);
            recyclerListView = this.innerListView;
            Adapter anonymousClass3 = new Adapter(ArticleViewer.this) {
                public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                    return new Holder(viewType != 0 ? new BlockVideoCell(BlockCollageCell.this.getContext(), 2) : new BlockPhotoCell(BlockCollageCell.this.getContext(), 2));
                }

                public void onBindViewHolder(ViewHolder holder, int position) {
                    if (holder.getItemViewType() != 0) {
                        holder.itemView.setBlock((TL_pageBlockVideo) BlockCollageCell.this.currentBlock.items.get(position), true, true);
                    } else {
                        holder.itemView.setBlock((TL_pageBlockPhoto) BlockCollageCell.this.currentBlock.items.get(position), true, true);
                    }
                }

                public int getItemCount() {
                    if (BlockCollageCell.this.currentBlock == null) {
                        return 0;
                    }
                    return BlockCollageCell.this.currentBlock.items.size();
                }

                public int getItemViewType(int position) {
                    if (((PageBlock) BlockCollageCell.this.currentBlock.items.get(position)) instanceof TL_pageBlockPhoto) {
                        return 0;
                    }
                    return 1;
                }
            };
            this.adapter = anonymousClass3;
            recyclerListView.setAdapter(anonymousClass3);
            addView(this.innerListView, LayoutHelper.createFrame(-1, -2.0f));
            setWillNotDraw(null);
        }

        public void setBlock(TL_pageBlockCollage block) {
            this.currentBlock = block;
            this.lastCreatedWidth = 0;
            this.adapter.notifyDataSetChanged();
            int color = ArticleViewer.this.getSelectedColor();
            if (color == 0) {
                this.innerListView.setGlowColor(-657673);
            } else if (color == 1) {
                this.innerListView.setGlowColor(-659492);
            } else if (color == 2) {
                this.innerListView.setGlowColor(-15461356);
            }
            requestLayout();
        }

        public boolean onTouchEvent(MotionEvent event) {
            if (!ArticleViewer.this.checkLayoutForLinks(event, this, this.textLayout, this.textX, this.textY)) {
                if (!super.onTouchEvent(event)) {
                    return false;
                }
            }
            return true;
        }

        protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
            this.innerListView.layout(this.listX, 0, this.listX + this.innerListView.getMeasuredWidth(), this.innerListView.getMeasuredHeight());
        }

        protected void onDraw(Canvas canvas) {
            if (this.currentBlock != null) {
                if (this.textLayout != null) {
                    canvas.save();
                    canvas.translate((float) this.textX, (float) this.textY);
                    ArticleViewer.this.drawLayoutLink(canvas, this.textLayout);
                    this.textLayout.draw(canvas);
                    canvas.restore();
                }
                if (this.currentBlock.level > 0) {
                    canvas.drawRect((float) AndroidUtilities.dp(18.0f), 0.0f, (float) AndroidUtilities.dp(20.0f), (float) (getMeasuredHeight() - (this.currentBlock.bottom ? AndroidUtilities.dp(6.0f) : 0)), ArticleViewer.quoteLinePaint);
                }
            }
        }
    }

    private class BlockDividerCell extends View {
        private RectF rect = new RectF();

        public BlockDividerCell(Context context) {
            super(context);
            if (ArticleViewer.dividerPaint == null) {
                ArticleViewer.dividerPaint = new Paint();
                int color = ArticleViewer.this.getSelectedColor();
                if (color == 0) {
                    ArticleViewer.dividerPaint.setColor(-3288619);
                } else if (color == 1) {
                    ArticleViewer.dividerPaint.setColor(-4080987);
                } else if (color == 2) {
                    ArticleViewer.dividerPaint.setColor(-12303292);
                }
            }
        }

        protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
            setMeasuredDimension(MeasureSpec.getSize(widthMeasureSpec), AndroidUtilities.dp(18.0f));
        }

        protected void onDraw(Canvas canvas) {
            int width = getMeasuredWidth() / 3;
            this.rect.set((float) width, (float) AndroidUtilities.dp(8.0f), (float) (width * 2), (float) AndroidUtilities.dp(10.0f));
            canvas.drawRoundRect(this.rect, (float) AndroidUtilities.dp(1.0f), (float) AndroidUtilities.dp(1.0f), ArticleViewer.dividerPaint);
        }
    }

    private class BlockEmbedCell extends FrameLayout {
        private TL_pageBlockEmbed currentBlock;
        private int lastCreatedWidth;
        private int listX;
        private StaticLayout textLayout;
        private int textX;
        private int textY;
        private WebPlayerView videoView;
        private TouchyWebView webView;

        public class TouchyWebView extends WebView {
            public TouchyWebView(Context context) {
                super(context);
                setFocusable(false);
            }

            public boolean onTouchEvent(MotionEvent event) {
                if (BlockEmbedCell.this.currentBlock != null) {
                    if (BlockEmbedCell.this.currentBlock.allow_scrolling) {
                        requestDisallowInterceptTouchEvent(true);
                    } else {
                        ArticleViewer.this.windowView.requestDisallowInterceptTouchEvent(true);
                    }
                }
                return super.onTouchEvent(event);
            }
        }

        @SuppressLint({"SetJavaScriptEnabled"})
        public BlockEmbedCell(Context context) {
            super(context);
            setWillNotDraw(false);
            this.videoView = new WebPlayerView(context, false, false, new WebPlayerViewDelegate(ArticleViewer.this) {
                public void onInitFailed() {
                    BlockEmbedCell.this.webView.setVisibility(0);
                    BlockEmbedCell.this.videoView.setVisibility(4);
                    BlockEmbedCell.this.videoView.loadVideo(null, null, null, false);
                    HashMap<String, String> args = new HashMap();
                    args.put("Referer", "http://youtube.com");
                    BlockEmbedCell.this.webView.loadUrl(BlockEmbedCell.this.currentBlock.url, args);
                }

                public void onVideoSizeChanged(float aspectRatio, int rotation) {
                    ArticleViewer.this.fullscreenAspectRatioView.setAspectRatio(aspectRatio, rotation);
                }

                public void onInlineSurfaceTextureReady() {
                }

                public TextureView onSwitchToFullscreen(View controlsView, boolean fullscreen, float aspectRatio, int rotation, boolean byButton) {
                    if (fullscreen) {
                        ArticleViewer.this.fullscreenAspectRatioView.addView(ArticleViewer.this.fullscreenTextureView, LayoutHelper.createFrame(-1, -1.0f));
                        ArticleViewer.this.fullscreenAspectRatioView.setVisibility(0);
                        ArticleViewer.this.fullscreenAspectRatioView.setAspectRatio(aspectRatio, rotation);
                        ArticleViewer.this.fullscreenedVideo = BlockEmbedCell.this.videoView;
                        ArticleViewer.this.fullscreenVideoContainer.addView(controlsView, LayoutHelper.createFrame(-1, -1.0f));
                        ArticleViewer.this.fullscreenVideoContainer.setVisibility(0);
                    } else {
                        ArticleViewer.this.fullscreenAspectRatioView.removeView(ArticleViewer.this.fullscreenTextureView);
                        ArticleViewer.this.fullscreenedVideo = null;
                        ArticleViewer.this.fullscreenAspectRatioView.setVisibility(8);
                        ArticleViewer.this.fullscreenVideoContainer.setVisibility(4);
                    }
                    return ArticleViewer.this.fullscreenTextureView;
                }

                public void prepareToSwitchInlineMode(boolean inline, Runnable switchInlineModeRunnable, float aspectRatio, boolean animated) {
                }

                public TextureView onSwitchInlineMode(View controlsView, boolean inline, float aspectRatio, int rotation, boolean animated) {
                    return null;
                }

                public void onSharePressed() {
                    if (ArticleViewer.this.parentActivity != null) {
                        ArticleViewer.this.showDialog(new ShareAlert(ArticleViewer.this.parentActivity, null, BlockEmbedCell.this.currentBlock.url, false, BlockEmbedCell.this.currentBlock.url, true));
                    }
                }

                public void onPlayStateChanged(WebPlayerView playerView, boolean playing) {
                    if (playing) {
                        if (!(ArticleViewer.this.currentPlayingVideo == null || ArticleViewer.this.currentPlayingVideo == playerView)) {
                            ArticleViewer.this.currentPlayingVideo.pause();
                        }
                        ArticleViewer.this.currentPlayingVideo = playerView;
                        try {
                            ArticleViewer.this.parentActivity.getWindow().addFlags(128);
                        } catch (Throwable e) {
                            FileLog.e(e);
                        }
                        return;
                    }
                    if (ArticleViewer.this.currentPlayingVideo == playerView) {
                        ArticleViewer.this.currentPlayingVideo = null;
                    }
                    try {
                        ArticleViewer.this.parentActivity.getWindow().clearFlags(128);
                    } catch (Throwable e2) {
                        FileLog.e(e2);
                    }
                }

                public boolean checkInlinePermissions() {
                    return false;
                }

                public ViewGroup getTextureViewContainer() {
                    return null;
                }
            });
            addView(this.videoView);
            ArticleViewer.this.createdWebViews.add(this);
            this.webView = new TouchyWebView(context);
            this.webView.getSettings().setJavaScriptEnabled(true);
            this.webView.getSettings().setDomStorageEnabled(true);
            this.webView.getSettings().setAllowContentAccess(true);
            if (VERSION.SDK_INT >= 17) {
                this.webView.getSettings().setMediaPlaybackRequiresUserGesture(false);
            }
            if (VERSION.SDK_INT >= 21) {
                this.webView.getSettings().setMixedContentMode(0);
                CookieManager.getInstance().setAcceptThirdPartyCookies(this.webView, true);
            }
            this.webView.setWebChromeClient(new WebChromeClient(ArticleViewer.this) {
                public void onShowCustomView(View view, int requestedOrientation, CustomViewCallback callback) {
                    onShowCustomView(view, callback);
                }

                public void onShowCustomView(View view, CustomViewCallback callback) {
                    if (ArticleViewer.this.customView != null) {
                        callback.onCustomViewHidden();
                        return;
                    }
                    ArticleViewer.this.customView = view;
                    ArticleViewer.this.customViewCallback = callback;
                    AndroidUtilities.runOnUIThread(new Runnable() {
                        public void run() {
                            if (ArticleViewer.this.customView != null) {
                                ArticleViewer.this.fullscreenVideoContainer.addView(ArticleViewer.this.customView, LayoutHelper.createFrame(-1, -1.0f));
                                ArticleViewer.this.fullscreenVideoContainer.setVisibility(0);
                            }
                        }
                    }, 100);
                }

                public void onHideCustomView() {
                    super.onHideCustomView();
                    if (ArticleViewer.this.customView != null) {
                        ArticleViewer.this.fullscreenVideoContainer.setVisibility(4);
                        ArticleViewer.this.fullscreenVideoContainer.removeView(ArticleViewer.this.customView);
                        if (!(ArticleViewer.this.customViewCallback == null || ArticleViewer.this.customViewCallback.getClass().getName().contains(".chromium."))) {
                            ArticleViewer.this.customViewCallback.onCustomViewHidden();
                        }
                        ArticleViewer.this.customView = null;
                    }
                }
            });
            this.webView.setWebViewClient(new WebViewClient(ArticleViewer.this) {
                public void onLoadResource(WebView view, String url) {
                    super.onLoadResource(view, url);
                }

                public void onPageFinished(WebView view, String url) {
                    super.onPageFinished(view, url);
                }
            });
            addView(this.webView);
        }

        public void destroyWebView(boolean completely) {
            try {
                this.webView.stopLoading();
                this.webView.loadUrl("about:blank");
                if (completely) {
                    this.webView.destroy();
                }
                this.currentBlock = null;
            } catch (Throwable e) {
                FileLog.e(e);
            }
            this.videoView.destroy();
        }

        public void setBlock(TL_pageBlockEmbed block) {
            TL_pageBlockEmbed previousBlock = this.currentBlock;
            this.currentBlock = block;
            this.lastCreatedWidth = 0;
            if (previousBlock != this.currentBlock) {
                try {
                    this.webView.loadUrl("about:blank");
                } catch (Throwable e) {
                    FileLog.e(e);
                }
                try {
                    if (this.currentBlock.html != null) {
                        this.webView.loadDataWithBaseURL("https://telegram.org/embed", this.currentBlock.html, "text/html", C.UTF8_NAME, null);
                        this.videoView.setVisibility(4);
                        this.videoView.loadVideo(null, null, null, false);
                        this.webView.setVisibility(0);
                    } else {
                        if (this.videoView.loadVideo(block.url, this.currentBlock.poster_photo_id != 0 ? ArticleViewer.this.getPhotoWithId(this.currentBlock.poster_photo_id) : null, null, this.currentBlock.autoplay)) {
                            this.webView.setVisibility(4);
                            this.videoView.setVisibility(0);
                            this.webView.stopLoading();
                            this.webView.loadUrl("about:blank");
                        } else {
                            this.webView.setVisibility(0);
                            this.videoView.setVisibility(4);
                            this.videoView.loadVideo(null, null, null, false);
                            HashMap<String, String> args = new HashMap();
                            args.put("Referer", "http://youtube.com");
                            this.webView.loadUrl(this.currentBlock.url, args);
                        }
                    }
                } catch (Throwable e2) {
                    FileLog.e(e2);
                }
            }
            requestLayout();
        }

        protected void onDetachedFromWindow() {
            super.onDetachedFromWindow();
            if (!ArticleViewer.this.isVisible) {
                this.currentBlock = null;
            }
        }

        protected void onAttachedToWindow() {
            super.onAttachedToWindow();
        }

        public boolean onTouchEvent(MotionEvent event) {
            if (!ArticleViewer.this.checkLayoutForLinks(event, this, this.textLayout, this.textX, this.textY)) {
                if (!super.onTouchEvent(event)) {
                    return false;
                }
            }
            return true;
        }

        protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
            int height;
            int width = MeasureSpec.getSize(widthMeasureSpec);
            if (this.currentBlock != null) {
                int dp;
                float scale;
                int listWidth = width;
                if (this.currentBlock.level > 0) {
                    dp = AndroidUtilities.dp((float) (14 * this.currentBlock.level)) + AndroidUtilities.dp(18.0f);
                    this.listX = dp;
                    this.textX = dp;
                    listWidth -= this.listX + AndroidUtilities.dp(18.0f);
                    dp = listWidth;
                } else {
                    this.listX = 0;
                    this.textX = AndroidUtilities.dp(18.0f);
                    dp = width - AndroidUtilities.dp(36.0f);
                }
                if (this.currentBlock.w == 0) {
                    scale = 1.0f;
                } else {
                    scale = ((float) width) / ((float) this.currentBlock.w);
                }
                height = (int) (((float) (this.currentBlock.w == 0 ? AndroidUtilities.dp((float) this.currentBlock.h) : this.currentBlock.h)) * scale);
                this.webView.measure(MeasureSpec.makeMeasureSpec(listWidth, 1073741824), MeasureSpec.makeMeasureSpec(height, 1073741824));
                if (this.videoView.getParent() == this) {
                    this.videoView.measure(MeasureSpec.makeMeasureSpec(listWidth, 1073741824), MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(10.0f) + height, 1073741824));
                }
                if (this.lastCreatedWidth != width) {
                    this.textLayout = ArticleViewer.this.createLayoutForText(null, this.currentBlock.caption, dp, this.currentBlock);
                    if (this.textLayout != null) {
                        this.textY = AndroidUtilities.dp(8.0f) + height;
                        height += AndroidUtilities.dp(8.0f) + this.textLayout.getHeight();
                    }
                }
                height += AndroidUtilities.dp(5.0f);
                if (this.currentBlock.level > 0 && !this.currentBlock.bottom) {
                    height += AndroidUtilities.dp(8.0f);
                } else if (this.currentBlock.level == 0 && this.textLayout != null) {
                    height += AndroidUtilities.dp(8.0f);
                }
            } else {
                height = 1;
            }
            setMeasuredDimension(width, height);
        }

        protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
            this.webView.layout(this.listX, 0, this.listX + this.webView.getMeasuredWidth(), this.webView.getMeasuredHeight());
            if (this.videoView.getParent() == this) {
                this.videoView.layout(this.listX, 0, this.listX + this.videoView.getMeasuredWidth(), this.videoView.getMeasuredHeight());
            }
        }

        protected void onDraw(Canvas canvas) {
            if (this.currentBlock != null) {
                if (this.textLayout != null) {
                    canvas.save();
                    canvas.translate((float) this.textX, (float) this.textY);
                    ArticleViewer.this.drawLayoutLink(canvas, this.textLayout);
                    this.textLayout.draw(canvas);
                    canvas.restore();
                }
                if (this.currentBlock.level > 0) {
                    canvas.drawRect((float) AndroidUtilities.dp(18.0f), 0.0f, (float) AndroidUtilities.dp(20.0f), (float) (getMeasuredHeight() - (this.currentBlock.bottom ? AndroidUtilities.dp(6.0f) : 0)), ArticleViewer.quoteLinePaint);
                }
            }
        }
    }

    private class BlockEmbedPostCell extends View {
        private AvatarDrawable avatarDrawable;
        private ImageReceiver avatarImageView = new ImageReceiver(this);
        private boolean avatarVisible;
        private int captionX = AndroidUtilities.dp(18.0f);
        private int captionY;
        private TL_pageBlockEmbedPost currentBlock;
        private StaticLayout dateLayout;
        private int dateX;
        private int lastCreatedWidth;
        private int lineHeight;
        private StaticLayout nameLayout;
        private int nameX;
        private StaticLayout textLayout;
        private int textX = AndroidUtilities.dp(32.0f);
        private int textY = AndroidUtilities.dp(56.0f);

        public BlockEmbedPostCell(Context context) {
            super(context);
            this.avatarImageView.setRoundRadius(AndroidUtilities.dp(20.0f));
            this.avatarImageView.setImageCoords(AndroidUtilities.dp(32.0f), AndroidUtilities.dp(8.0f), AndroidUtilities.dp(40.0f), AndroidUtilities.dp(40.0f));
            this.avatarDrawable = new AvatarDrawable();
        }

        public void setBlock(TL_pageBlockEmbedPost block) {
            this.currentBlock = block;
            this.lastCreatedWidth = 0;
            requestLayout();
        }

        public boolean onTouchEvent(MotionEvent event) {
            if (!ArticleViewer.this.checkLayoutForLinks(event, this, this.textLayout, this.textX, this.textY)) {
                if (!super.onTouchEvent(event)) {
                    return false;
                }
            }
            return true;
        }

        protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
            int width = MeasureSpec.getSize(widthMeasureSpec);
            int height = 0;
            if (this.currentBlock == null) {
                height = 1;
            } else if (r0.lastCreatedWidth != width) {
                int i = 0;
                boolean z = r0.currentBlock.author_photo_id != 0;
                r0.avatarVisible = z;
                if (z) {
                    Photo photo = ArticleViewer.this.getPhotoWithId(r0.currentBlock.author_photo_id);
                    boolean z2 = photo instanceof TL_photo;
                    r0.avatarVisible = z2;
                    if (z2) {
                        r0.avatarDrawable.setInfo(0, r0.currentBlock.author, null, false);
                        PhotoSize image = FileLoader.getClosestPhotoSizeWithSize(photo.sizes, AndroidUtilities.dp(40.0f), true);
                        r0.avatarImageView.setImage(image != null ? image.location : null, String.format(Locale.US, "%d_%d", new Object[]{Integer.valueOf(40), Integer.valueOf(40)}), r0.avatarDrawable, 0, null, 1);
                    }
                }
                r0.nameLayout = ArticleViewer.this.createLayoutForText(r0.currentBlock.author, null, width - AndroidUtilities.dp((float) ((r0.avatarVisible ? 54 : 0) + 50)), r0.currentBlock);
                if (r0.currentBlock.date != 0) {
                    ArticleViewer articleViewer = ArticleViewer.this;
                    CharSequence format = LocaleController.getInstance().chatFullDate.format(((long) r0.currentBlock.date) * 1000);
                    if (r0.avatarVisible) {
                        i = 54;
                    }
                    r0.dateLayout = articleViewer.createLayoutForText(format, null, width - AndroidUtilities.dp((float) (50 + i)), r0.currentBlock);
                } else {
                    r0.dateLayout = null;
                }
                height = AndroidUtilities.dp(56.0f);
                if (r0.currentBlock.text != null) {
                    r0.textLayout = ArticleViewer.this.createLayoutForText(null, r0.currentBlock.text, width - AndroidUtilities.dp(50.0f), r0.currentBlock);
                    if (r0.textLayout != null) {
                        height += AndroidUtilities.dp(8.0f) + r0.textLayout.getHeight();
                    }
                }
                r0.lineHeight = height;
            }
            setMeasuredDimension(width, height);
        }

        protected void onDraw(Canvas canvas) {
            if (this.currentBlock != null) {
                if (this.avatarVisible) {
                    this.avatarImageView.draw(canvas);
                }
                int i = 54;
                int i2 = 0;
                if (this.nameLayout != null) {
                    canvas.save();
                    canvas.translate((float) AndroidUtilities.dp((float) ((this.avatarVisible ? 54 : 0) + 32)), (float) AndroidUtilities.dp(this.dateLayout != null ? 10.0f : 19.0f));
                    this.nameLayout.draw(canvas);
                    canvas.restore();
                }
                if (this.dateLayout != null) {
                    canvas.save();
                    if (!this.avatarVisible) {
                        i = 0;
                    }
                    canvas.translate((float) AndroidUtilities.dp((float) (32 + i)), (float) AndroidUtilities.dp(29.0f));
                    this.dateLayout.draw(canvas);
                    canvas.restore();
                }
                if (this.textLayout != null) {
                    canvas.save();
                    canvas.translate((float) this.textX, (float) this.textY);
                    ArticleViewer.this.drawLayoutLink(canvas, this.textLayout);
                    this.textLayout.draw(canvas);
                    canvas.restore();
                }
                float dp = (float) AndroidUtilities.dp(18.0f);
                float dp2 = (float) AndroidUtilities.dp(6.0f);
                float dp3 = (float) AndroidUtilities.dp(20.0f);
                i = this.lineHeight;
                if (this.currentBlock.level == 0) {
                    i2 = AndroidUtilities.dp(6.0f);
                }
                canvas.drawRect(dp, dp2, dp3, (float) (i - i2), ArticleViewer.quoteLinePaint);
            }
        }
    }

    private class BlockFooterCell extends View {
        private TL_pageBlockFooter currentBlock;
        private int lastCreatedWidth;
        private StaticLayout textLayout;
        private int textX = AndroidUtilities.dp(18.0f);
        private int textY = AndroidUtilities.dp(8.0f);

        public BlockFooterCell(Context context) {
            super(context);
        }

        public void setBlock(TL_pageBlockFooter block) {
            this.currentBlock = block;
            this.lastCreatedWidth = 0;
            requestLayout();
        }

        public boolean onTouchEvent(MotionEvent event) {
            if (!ArticleViewer.this.checkLayoutForLinks(event, this, this.textLayout, this.textX, this.textY)) {
                if (!super.onTouchEvent(event)) {
                    return false;
                }
            }
            return true;
        }

        protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
            int width = MeasureSpec.getSize(widthMeasureSpec);
            int height = 0;
            if (this.currentBlock != null) {
                if (this.currentBlock.level == 0) {
                    this.textY = AndroidUtilities.dp(8.0f);
                    this.textX = AndroidUtilities.dp(18.0f);
                } else {
                    this.textY = 0;
                    this.textX = AndroidUtilities.dp((float) (18 + (14 * this.currentBlock.level)));
                }
                if (this.lastCreatedWidth != width) {
                    this.textLayout = ArticleViewer.this.createLayoutForText(null, this.currentBlock.text, (width - AndroidUtilities.dp(18.0f)) - this.textX, this.currentBlock);
                    if (this.textLayout != null) {
                        height = this.textLayout.getHeight();
                        height = this.currentBlock.level > 0 ? height + AndroidUtilities.dp(8.0f) : height + AndroidUtilities.dp(16.0f);
                    }
                }
            } else {
                height = 1;
            }
            setMeasuredDimension(width, height);
        }

        protected void onDraw(Canvas canvas) {
            if (this.currentBlock != null) {
                if (this.textLayout != null) {
                    canvas.save();
                    canvas.translate((float) this.textX, (float) this.textY);
                    ArticleViewer.this.drawLayoutLink(canvas, this.textLayout);
                    this.textLayout.draw(canvas);
                    canvas.restore();
                }
                if (this.currentBlock.level > 0) {
                    canvas.drawRect((float) AndroidUtilities.dp(18.0f), 0.0f, (float) AndroidUtilities.dp(20.0f), (float) (getMeasuredHeight() - (this.currentBlock.bottom ? AndroidUtilities.dp(6.0f) : 0)), ArticleViewer.quoteLinePaint);
                }
            }
        }
    }

    private class BlockHeaderCell extends View {
        private TL_pageBlockHeader currentBlock;
        private int lastCreatedWidth;
        private StaticLayout textLayout;
        private int textX = AndroidUtilities.dp(18.0f);
        private int textY = AndroidUtilities.dp(8.0f);

        public BlockHeaderCell(Context context) {
            super(context);
        }

        public void setBlock(TL_pageBlockHeader block) {
            this.currentBlock = block;
            this.lastCreatedWidth = 0;
            requestLayout();
        }

        public boolean onTouchEvent(MotionEvent event) {
            if (!ArticleViewer.this.checkLayoutForLinks(event, this, this.textLayout, this.textX, this.textY)) {
                if (!super.onTouchEvent(event)) {
                    return false;
                }
            }
            return true;
        }

        protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
            int width = MeasureSpec.getSize(widthMeasureSpec);
            int height = 0;
            if (this.currentBlock == null) {
                height = 1;
            } else if (this.lastCreatedWidth != width) {
                this.textLayout = ArticleViewer.this.createLayoutForText(null, this.currentBlock.text, width - AndroidUtilities.dp(36.0f), this.currentBlock);
                if (this.textLayout != null) {
                    height = 0 + (AndroidUtilities.dp(16.0f) + this.textLayout.getHeight());
                }
            }
            setMeasuredDimension(width, height);
        }

        protected void onDraw(Canvas canvas) {
            if (!(this.currentBlock == null || this.textLayout == null)) {
                canvas.save();
                canvas.translate((float) this.textX, (float) this.textY);
                ArticleViewer.this.drawLayoutLink(canvas, this.textLayout);
                this.textLayout.draw(canvas);
                canvas.restore();
            }
        }
    }

    private class BlockListCell extends View {
        private TL_pageBlockList currentBlock;
        private boolean hasRtl;
        private int lastCreatedWidth;
        private int maxLetterWidth;
        private ArrayList<StaticLayout> textLayouts = new ArrayList();
        private ArrayList<StaticLayout> textNumLayouts = new ArrayList();
        private int textX;
        private ArrayList<Integer> textYLayouts = new ArrayList();

        protected void onMeasure(int r1, int r2) {
            /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.DecodeException: Load method exception in method: org.telegram.ui.ArticleViewer.BlockListCell.onMeasure(int, int):void
	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:116)
	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:249)
	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:256)
	at jadx.core.ProcessClass.process(ProcessClass.java:34)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:306)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler$1.run(JadxDecompiler.java:199)
Caused by: java.lang.NullPointerException
*/
            /*
            r0 = this;
            r0 = android.view.View.MeasureSpec.getSize(r13);
            r1 = 0;
            r2 = 0;
            r12.hasRtl = r2;
            r12.maxLetterWidth = r2;
            r3 = r12.currentBlock;
            if (r3 == 0) goto L_0x0131;
        L_0x000e:
            r3 = r12.lastCreatedWidth;
            if (r3 == r0) goto L_0x0132;
        L_0x0012:
            r3 = r12.textLayouts;
            r3.clear();
            r3 = r12.textYLayouts;
            r3.clear();
            r3 = r12.textNumLayouts;
            r3.clear();
            r3 = r12.currentBlock;
            r3 = r3.items;
            r3 = r3.size();
            r4 = r2;
            r5 = 1103101952; // 0x41c00000 float:24.0 double:5.450047783E-315;
            r6 = 0;
            if (r4 >= r3) goto L_0x00d6;
        L_0x002f:
            r7 = r12.currentBlock;
            r7 = r7.items;
            r7 = r7.get(r4);
            r7 = (org.telegram.tgnet.TLRPC.RichText) r7;
            r8 = 1;
            if (r4 != 0) goto L_0x006a;
            r9 = org.telegram.ui.ArticleViewer.this;
            r5 = org.telegram.messenger.AndroidUtilities.dp(r5);
            r5 = r0 - r5;
            r10 = r12.maxLetterWidth;
            r5 = r5 - r10;
            r10 = r12.currentBlock;
            r5 = r9.createLayoutForText(r6, r7, r5, r10);
            if (r5 == 0) goto L_0x006a;
            r6 = r5.getLineCount();
            r9 = r2;
            if (r9 >= r6) goto L_0x006a;
            r10 = r5.getLineLeft(r9);
            r11 = 0;
            r10 = (r10 > r11 ? 1 : (r10 == r11 ? 0 : -1));
            if (r10 <= 0) goto L_0x0067;
            r12.hasRtl = r8;
            r10 = org.telegram.ui.ArticleViewer.this;
            r10.isRtl = r8;
            goto L_0x006a;
            r9 = r9 + 1;
            goto L_0x0054;
            r5 = r12.currentBlock;
            r5 = r5.ordered;
            if (r5 == 0) goto L_0x0096;
            r5 = r12.hasRtl;
            if (r5 == 0) goto L_0x0085;
            r5 = ".%d";
            r6 = new java.lang.Object[r8];
            r8 = r4 + 1;
            r8 = java.lang.Integer.valueOf(r8);
            r6[r2] = r8;
            r5 = java.lang.String.format(r5, r6);
            goto L_0x0098;
            r5 = "%d.";
            r6 = new java.lang.Object[r8];
            r8 = r4 + 1;
            r8 = java.lang.Integer.valueOf(r8);
            r6[r2] = r8;
            r5 = java.lang.String.format(r5, r6);
            goto L_0x0084;
            r5 = "•";
            r6 = org.telegram.ui.ArticleViewer.this;
            r8 = 1113063424; // 0x42580000 float:54.0 double:5.499263994E-315;
            r8 = org.telegram.messenger.AndroidUtilities.dp(r8);
            r8 = r0 - r8;
            r9 = r12.currentBlock;
            r6 = r6.createLayoutForText(r5, r7, r8, r9);
            r8 = r12.textNumLayouts;
            r8.add(r6);
            r8 = r12.currentBlock;
            r8 = r8.ordered;
            if (r8 == 0) goto L_0x00c8;
            if (r6 == 0) goto L_0x00d2;
            r8 = r12.maxLetterWidth;
            r9 = r6.getLineWidth(r2);
            r9 = (double) r9;
            r9 = java.lang.Math.ceil(r9);
            r9 = (int) r9;
            r8 = java.lang.Math.max(r8, r9);
            r12.maxLetterWidth = r8;
            goto L_0x00d2;
            if (r4 != 0) goto L_0x00d2;
            r8 = 1094713344; // 0x41400000 float:12.0 double:5.408602553E-315;
            r8 = org.telegram.messenger.AndroidUtilities.dp(r8);
            r12.maxLetterWidth = r8;
            r4 = r4 + 1;
            goto L_0x002a;
            r4 = 1090519040; // 0x41000000 float:8.0 double:5.38787994E-315;
            if (r2 >= r3) goto L_0x0115;
            r7 = r12.currentBlock;
            r7 = r7.items;
            r7 = r7.get(r2);
            r7 = (org.telegram.tgnet.TLRPC.RichText) r7;
            r4 = org.telegram.messenger.AndroidUtilities.dp(r4);
            r1 = r1 + r4;
            r4 = org.telegram.ui.ArticleViewer.this;
            r8 = 1109917696; // 0x42280000 float:42.0 double:5.483722033E-315;
            r8 = org.telegram.messenger.AndroidUtilities.dp(r8);
            r8 = r0 - r8;
            r9 = r12.maxLetterWidth;
            r8 = r8 - r9;
            r9 = r12.currentBlock;
            r4 = r4.createLayoutForText(r6, r7, r8, r9);
            r8 = r12.textYLayouts;
            r9 = java.lang.Integer.valueOf(r1);
            r8.add(r9);
            r8 = r12.textLayouts;
            r8.add(r4);
            if (r4 == 0) goto L_0x0112;
            r8 = r4.getHeight();
            r1 = r1 + r8;
            r2 = r2 + 1;
            goto L_0x00d7;
            r2 = r12.hasRtl;
            if (r2 == 0) goto L_0x0122;
            r2 = 1099956224; // 0x41900000 float:18.0 double:5.43450582E-315;
            r2 = org.telegram.messenger.AndroidUtilities.dp(r2);
            r12.textX = r2;
            goto L_0x012b;
            r2 = org.telegram.messenger.AndroidUtilities.dp(r5);
            r5 = r12.maxLetterWidth;
            r2 = r2 + r5;
            r12.textX = r2;
            r2 = org.telegram.messenger.AndroidUtilities.dp(r4);
            r1 = r1 + r2;
            goto L_0x0132;
        L_0x0131:
            r1 = 1;
        L_0x0132:
            r12.setMeasuredDimension(r0, r1);
            return;
            */
            throw new UnsupportedOperationException("Method not decompiled: org.telegram.ui.ArticleViewer.BlockListCell.onMeasure(int, int):void");
        }

        public BlockListCell(Context context) {
            super(context);
        }

        public void setBlock(TL_pageBlockList block) {
            this.currentBlock = block;
            this.lastCreatedWidth = 0;
            requestLayout();
        }

        public boolean onTouchEvent(MotionEvent event) {
            int count = this.textLayouts.size();
            int textX = AndroidUtilities.dp(1108344832);
            int a = 0;
            while (true) {
                int a2 = a;
                if (a2 >= count) {
                    return super.onTouchEvent(event);
                }
                StaticLayout textLayout = (StaticLayout) this.textLayouts.get(a2);
                if (ArticleViewer.this.checkLayoutForLinks(event, this, textLayout, textX, ((Integer) this.textYLayouts.get(a2)).intValue())) {
                    return true;
                }
                a = a2 + 1;
            }
        }

        protected void onDraw(Canvas canvas) {
            if (this.currentBlock != null) {
                int count = this.textLayouts.size();
                int width = getMeasuredWidth();
                for (int a = 0; a < count; a++) {
                    StaticLayout textLayout = (StaticLayout) this.textLayouts.get(a);
                    StaticLayout textLayout2 = (StaticLayout) this.textNumLayouts.get(a);
                    canvas.save();
                    if (this.hasRtl) {
                        canvas.translate((float) ((width - AndroidUtilities.dp(18.0f)) - ((int) Math.ceil((double) textLayout2.getLineWidth(0)))), (float) ((Integer) this.textYLayouts.get(a)).intValue());
                    } else {
                        canvas.translate((float) AndroidUtilities.dp(18.0f), (float) ((Integer) this.textYLayouts.get(a)).intValue());
                    }
                    if (textLayout2 != null) {
                        textLayout2.draw(canvas);
                    }
                    canvas.restore();
                    canvas.save();
                    canvas.translate((float) this.textX, (float) ((Integer) this.textYLayouts.get(a)).intValue());
                    ArticleViewer.this.drawLayoutLink(canvas, textLayout);
                    if (textLayout != null) {
                        textLayout.draw(canvas);
                    }
                    canvas.restore();
                }
            }
        }
    }

    private class BlockParagraphCell extends View {
        private TL_pageBlockParagraph currentBlock;
        private int lastCreatedWidth;
        private StaticLayout textLayout;
        private int textX;
        private int textY;

        public BlockParagraphCell(Context context) {
            super(context);
        }

        public void setBlock(TL_pageBlockParagraph block) {
            this.currentBlock = block;
            this.lastCreatedWidth = 0;
            requestLayout();
        }

        public boolean onTouchEvent(MotionEvent event) {
            if (!ArticleViewer.this.checkLayoutForLinks(event, this, this.textLayout, this.textX, this.textY)) {
                if (!super.onTouchEvent(event)) {
                    return false;
                }
            }
            return true;
        }

        protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
            int width = MeasureSpec.getSize(widthMeasureSpec);
            int height = 0;
            if (this.currentBlock != null) {
                if (this.currentBlock.level == 0) {
                    if (this.currentBlock.caption != null) {
                        this.textY = AndroidUtilities.dp(4.0f);
                    } else {
                        this.textY = AndroidUtilities.dp(8.0f);
                    }
                    this.textX = AndroidUtilities.dp(18.0f);
                } else {
                    this.textY = 0;
                    this.textX = AndroidUtilities.dp((float) (18 + (14 * this.currentBlock.level)));
                }
                if (this.lastCreatedWidth != width) {
                    if (this.currentBlock.text != null) {
                        this.textLayout = ArticleViewer.this.createLayoutForText(null, this.currentBlock.text, (width - AndroidUtilities.dp(18.0f)) - this.textX, this.currentBlock);
                    } else if (this.currentBlock.caption != null) {
                        this.textLayout = ArticleViewer.this.createLayoutForText(null, this.currentBlock.caption, (width - AndroidUtilities.dp(18.0f)) - this.textX, this.currentBlock);
                    }
                    if (this.textLayout != null) {
                        height = this.textLayout.getHeight();
                        height = this.currentBlock.level > 0 ? height + AndroidUtilities.dp(8.0f) : height + AndroidUtilities.dp(16.0f);
                    }
                }
            } else {
                height = 1;
            }
            setMeasuredDimension(width, height);
        }

        protected void onDraw(Canvas canvas) {
            if (this.currentBlock != null) {
                if (this.textLayout != null) {
                    canvas.save();
                    canvas.translate((float) this.textX, (float) this.textY);
                    ArticleViewer.this.drawLayoutLink(canvas, this.textLayout);
                    this.textLayout.draw(canvas);
                    canvas.restore();
                }
                if (this.currentBlock.level > 0) {
                    canvas.drawRect((float) AndroidUtilities.dp(18.0f), 0.0f, (float) AndroidUtilities.dp(20.0f), (float) (getMeasuredHeight() - (this.currentBlock.bottom ? AndroidUtilities.dp(6.0f) : 0)), ArticleViewer.quoteLinePaint);
                }
            }
        }
    }

    private class BlockPhotoCell extends FrameLayout {
        private BlockChannelCell channelCell;
        private TL_pageBlockPhoto currentBlock;
        private int currentType;
        private ImageReceiver imageView = new ImageReceiver(this);
        private boolean isFirst;
        private boolean isLast;
        private int lastCreatedWidth;
        private PageBlock parentBlock;
        private boolean photoPressed;
        private StaticLayout textLayout;
        private int textX;
        private int textY;

        public BlockPhotoCell(Context context, int type) {
            super(context);
            setWillNotDraw(false);
            this.channelCell = new BlockChannelCell(context, 1);
            addView(this.channelCell, LayoutHelper.createFrame(-1, -2.0f));
            this.currentType = type;
        }

        public void setBlock(TL_pageBlockPhoto block, boolean first, boolean last) {
            this.parentBlock = null;
            this.currentBlock = block;
            this.lastCreatedWidth = 0;
            this.isFirst = first;
            this.isLast = last;
            this.channelCell.setVisibility(4);
            requestLayout();
        }

        public void setParentBlock(PageBlock block) {
            this.parentBlock = block;
            if (ArticleViewer.this.channelBlock != null && (this.parentBlock instanceof TL_pageBlockCover)) {
                this.channelCell.setBlock(ArticleViewer.this.channelBlock);
                this.channelCell.setVisibility(0);
            }
        }

        public View getChannelCell() {
            return this.channelCell;
        }

        public boolean onTouchEvent(MotionEvent event) {
            float x = event.getX();
            float y = event.getY();
            boolean z = false;
            if (this.channelCell.getVisibility() != 0 || y <= this.channelCell.getTranslationY() || y >= this.channelCell.getTranslationY() + ((float) AndroidUtilities.dp(39.0f))) {
                if (event.getAction() == 0 && this.imageView.isInsideImage(x, y)) {
                    this.photoPressed = true;
                } else if (event.getAction() == 1 && this.photoPressed) {
                    this.photoPressed = false;
                    ArticleViewer.this.openPhoto(this.currentBlock);
                } else if (event.getAction() == 3) {
                    this.photoPressed = false;
                }
                if (!(this.photoPressed || ArticleViewer.this.checkLayoutForLinks(event, this, this.textLayout, this.textX, this.textY))) {
                    if (!super.onTouchEvent(event)) {
                        return z;
                    }
                }
                z = true;
                return z;
            }
            if (ArticleViewer.this.channelBlock != null && event.getAction() == 1) {
                MessagesController.getInstance(ArticleViewer.this.currentAccount).openByUserName(ArticleViewer.this.channelBlock.channel.username, ArticleViewer.this.parentFragment, 2);
                ArticleViewer.this.close(false, true);
            }
            return true;
        }

        protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
            int width = MeasureSpec.getSize(widthMeasureSpec);
            int height = 0;
            if (this.currentType == 1) {
                width = ArticleViewer.this.listView.getWidth();
                height = ((View) getParent()).getMeasuredHeight();
            } else if (r0.currentType == 2) {
                height = width;
            }
            if (r0.currentBlock != null) {
                int photoX;
                int textWidth;
                boolean z;
                Photo photo = ArticleViewer.this.getPhotoWithId(r0.currentBlock.photo_id);
                int photoWidth = width;
                if (r0.currentType != 0 || r0.currentBlock.level <= 0) {
                    photoX = 0;
                    r0.textX = AndroidUtilities.dp(18.0f);
                    textWidth = width - AndroidUtilities.dp(36.0f);
                } else {
                    textWidth = AndroidUtilities.dp((float) (14 * r0.currentBlock.level)) + AndroidUtilities.dp(18.0f);
                    photoX = textWidth;
                    r0.textX = textWidth;
                    photoWidth -= AndroidUtilities.dp(18.0f) + photoX;
                    textWidth = photoWidth;
                }
                if (photo != null) {
                    int dp;
                    String filter;
                    Object[] objArr;
                    PhotoSize image = FileLoader.getClosestPhotoSizeWithSize(photo.sizes, AndroidUtilities.getPhotoSize());
                    PhotoSize thumb = FileLoader.getClosestPhotoSizeWithSize(photo.sizes, 80, true);
                    if (image == thumb) {
                        thumb = null;
                    }
                    if (r0.currentType == 0) {
                        height = (int) (((float) image.h) * (((float) photoWidth) / ((float) image.w)));
                        if (r0.parentBlock instanceof TL_pageBlockCover) {
                            height = Math.min(height, photoWidth);
                        } else {
                            int maxHeight = (int) (((float) (Math.max(ArticleViewer.this.listView.getMeasuredWidth(), ArticleViewer.this.listView.getMeasuredHeight()) - AndroidUtilities.dp(56.0f))) * 1063675494);
                            if (height > maxHeight) {
                                height = maxHeight;
                                photoWidth = (int) (((float) image.w) * (((float) height) / ((float) image.h)));
                                photoX += ((width - photoX) - photoWidth) / 2;
                            }
                        }
                    }
                    ImageReceiver imageReceiver = r0.imageView;
                    if (!(r0.isFirst || r0.currentType == 1 || r0.currentType == 2)) {
                        if (r0.currentBlock.level <= 0) {
                            dp = AndroidUtilities.dp(8.0f);
                            imageReceiver.setImageCoords(photoX, dp, photoWidth, height);
                            if (r0.currentType != 0) {
                                filter = null;
                                z = false;
                            } else {
                                objArr = new Object[2];
                                z = false;
                                objArr[0] = Integer.valueOf(photoWidth);
                                objArr[1] = Integer.valueOf(height);
                                filter = String.format(Locale.US, "%d_%d", objArr);
                            }
                            r0.imageView.setImage(image.location, filter, thumb == null ? thumb.location : null, thumb == null ? "80_80_b" : null, image.size, null, 1);
                        }
                    }
                    dp = 0;
                    imageReceiver.setImageCoords(photoX, dp, photoWidth, height);
                    if (r0.currentType != 0) {
                        objArr = new Object[2];
                        z = false;
                        objArr[0] = Integer.valueOf(photoWidth);
                        objArr[1] = Integer.valueOf(height);
                        filter = String.format(Locale.US, "%d_%d", objArr);
                    } else {
                        filter = null;
                        z = false;
                    }
                    if (thumb == null) {
                    }
                    if (thumb == null) {
                    }
                    r0.imageView.setImage(image.location, filter, thumb == null ? thumb.location : null, thumb == null ? "80_80_b" : null, image.size, null, 1);
                } else {
                    z = false;
                }
                if (r0.currentType == 0 && r0.lastCreatedWidth != width) {
                    r0.textLayout = ArticleViewer.this.createLayoutForText(null, r0.currentBlock.caption, textWidth, r0.currentBlock);
                    if (r0.textLayout != null) {
                        height += AndroidUtilities.dp(8.0f) + r0.textLayout.getHeight();
                    }
                }
                if (!r0.isFirst && r0.currentType == 0 && r0.currentBlock.level <= 0) {
                    height += AndroidUtilities.dp(8.0f);
                }
                if ((r0.parentBlock instanceof TL_pageBlockCover) && ArticleViewer.this.blocks != null && ArticleViewer.this.blocks.size() > 1 && (ArticleViewer.this.blocks.get(1) instanceof TL_pageBlockChannel)) {
                    z = true;
                }
                boolean nextIsChannel = z;
                if (!(r0.currentType == 2 || nextIsChannel)) {
                    height += AndroidUtilities.dp(8.0f);
                }
            } else {
                height = 1;
            }
            r0.channelCell.measure(widthMeasureSpec, heightMeasureSpec);
            r0.channelCell.setTranslationY((float) (r0.imageView.getImageHeight() - AndroidUtilities.dp(39.0f)));
            setMeasuredDimension(width, height);
        }

        protected void onDraw(Canvas canvas) {
            if (this.currentBlock != null) {
                this.imageView.draw(canvas);
                if (this.textLayout != null) {
                    canvas.save();
                    float f = (float) this.textX;
                    int imageY = (this.imageView.getImageY() + this.imageView.getImageHeight()) + AndroidUtilities.dp(8.0f);
                    this.textY = imageY;
                    canvas.translate(f, (float) imageY);
                    ArticleViewer.this.drawLayoutLink(canvas, this.textLayout);
                    this.textLayout.draw(canvas);
                    canvas.restore();
                }
                if (this.currentBlock.level > 0) {
                    canvas.drawRect((float) AndroidUtilities.dp(18.0f), 0.0f, (float) AndroidUtilities.dp(20.0f), (float) (getMeasuredHeight() - (this.currentBlock.bottom ? AndroidUtilities.dp(6.0f) : 0)), ArticleViewer.quoteLinePaint);
                }
            }
        }
    }

    private class BlockPreformattedCell extends View {
        private TL_pageBlockPreformatted currentBlock;
        private int lastCreatedWidth;
        private StaticLayout textLayout;

        public BlockPreformattedCell(Context context) {
            super(context);
        }

        public void setBlock(TL_pageBlockPreformatted block) {
            this.currentBlock = block;
            this.lastCreatedWidth = 0;
            requestLayout();
        }

        protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
            int width = MeasureSpec.getSize(widthMeasureSpec);
            int height = 0;
            if (this.currentBlock == null) {
                height = 1;
            } else if (this.lastCreatedWidth != width) {
                this.textLayout = ArticleViewer.this.createLayoutForText(null, this.currentBlock.text, width - AndroidUtilities.dp(24.0f), this.currentBlock);
                if (this.textLayout != null) {
                    height = 0 + (AndroidUtilities.dp(32.0f) + this.textLayout.getHeight());
                }
            }
            setMeasuredDimension(width, height);
        }

        protected void onDraw(Canvas canvas) {
            if (this.currentBlock != null) {
                canvas.drawRect(0.0f, (float) AndroidUtilities.dp(8.0f), (float) getMeasuredWidth(), (float) (getMeasuredHeight() - AndroidUtilities.dp(8.0f)), ArticleViewer.preformattedBackgroundPaint);
                if (this.textLayout != null) {
                    canvas.save();
                    canvas.translate((float) AndroidUtilities.dp(12.0f), (float) AndroidUtilities.dp(16.0f));
                    this.textLayout.draw(canvas);
                    canvas.restore();
                }
            }
        }
    }

    private class BlockPullquoteCell extends View {
        private TL_pageBlockPullquote currentBlock;
        private int lastCreatedWidth;
        private StaticLayout textLayout;
        private StaticLayout textLayout2;
        private int textX = AndroidUtilities.dp(18.0f);
        private int textY = AndroidUtilities.dp(8.0f);
        private int textY2;

        public BlockPullquoteCell(Context context) {
            super(context);
        }

        public void setBlock(TL_pageBlockPullquote block) {
            this.currentBlock = block;
            this.lastCreatedWidth = 0;
            requestLayout();
        }

        public boolean onTouchEvent(MotionEvent event) {
            if (!(ArticleViewer.this.checkLayoutForLinks(event, this, this.textLayout, this.textX, this.textY) || ArticleViewer.this.checkLayoutForLinks(event, this, this.textLayout2, this.textX, this.textY2))) {
                if (!super.onTouchEvent(event)) {
                    return false;
                }
            }
            return true;
        }

        protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
            int width = MeasureSpec.getSize(widthMeasureSpec);
            int height = 0;
            if (this.currentBlock == null) {
                height = 1;
            } else if (this.lastCreatedWidth != width) {
                this.textLayout = ArticleViewer.this.createLayoutForText(null, this.currentBlock.text, width - AndroidUtilities.dp(36.0f), this.currentBlock);
                if (this.textLayout != null) {
                    height = 0 + (AndroidUtilities.dp(8.0f) + this.textLayout.getHeight());
                }
                this.textLayout2 = ArticleViewer.this.createLayoutForText(null, this.currentBlock.caption, width - AndroidUtilities.dp(36.0f), this.currentBlock);
                if (this.textLayout2 != null) {
                    this.textY2 = AndroidUtilities.dp(2.0f) + height;
                    height += AndroidUtilities.dp(8.0f) + this.textLayout2.getHeight();
                }
                if (height != 0) {
                    height += AndroidUtilities.dp(8.0f);
                }
            }
            setMeasuredDimension(width, height);
        }

        protected void onDraw(Canvas canvas) {
            if (this.currentBlock != null) {
                if (this.textLayout != null) {
                    canvas.save();
                    canvas.translate((float) this.textX, (float) this.textY);
                    ArticleViewer.this.drawLayoutLink(canvas, this.textLayout);
                    this.textLayout.draw(canvas);
                    canvas.restore();
                }
                if (this.textLayout2 != null) {
                    canvas.save();
                    canvas.translate((float) this.textX, (float) this.textY2);
                    ArticleViewer.this.drawLayoutLink(canvas, this.textLayout2);
                    this.textLayout2.draw(canvas);
                    canvas.restore();
                }
            }
        }
    }

    private class BlockSlideshowCell extends FrameLayout {
        private PagerAdapter adapter;
        private TL_pageBlockSlideshow currentBlock;
        private View dotsContainer;
        private ViewPager innerListView;
        private int lastCreatedWidth;
        private StaticLayout textLayout;
        private int textX = AndroidUtilities.dp(18.0f);
        private int textY;

        public BlockSlideshowCell(Context context) {
            super(context);
            if (ArticleViewer.dotsPaint == null) {
                ArticleViewer.dotsPaint = new Paint(1);
                ArticleViewer.dotsPaint.setColor(-1);
            }
            this.innerListView = new ViewPager(context, ArticleViewer.this) {
                public boolean onTouchEvent(MotionEvent ev) {
                    return super.onTouchEvent(ev);
                }

                public boolean onInterceptTouchEvent(MotionEvent ev) {
                    ArticleViewer.this.windowView.requestDisallowInterceptTouchEvent(true);
                    return super.onInterceptTouchEvent(ev);
                }
            };
            this.innerListView.addOnPageChangeListener(new OnPageChangeListener(ArticleViewer.this) {
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                }

                public void onPageSelected(int position) {
                    BlockSlideshowCell.this.dotsContainer.invalidate();
                }

                public void onPageScrollStateChanged(int state) {
                }
            });
            ViewPager viewPager = this.innerListView;
            PagerAdapter anonymousClass3 = new PagerAdapter(ArticleViewer.this) {

                class ObjectContainer {
                    private PageBlock block;
                    private View view;

                    ObjectContainer() {
                    }
                }

                public int getCount() {
                    if (BlockSlideshowCell.this.currentBlock == null) {
                        return 0;
                    }
                    return BlockSlideshowCell.this.currentBlock.items.size();
                }

                public boolean isViewFromObject(View view, Object object) {
                    return ((ObjectContainer) object).view == view;
                }

                public int getItemPosition(Object object) {
                    if (BlockSlideshowCell.this.currentBlock.items.contains(((ObjectContainer) object).block)) {
                        return -1;
                    }
                    return -2;
                }

                public Object instantiateItem(ViewGroup container, int position) {
                    View view;
                    PageBlock block = (PageBlock) BlockSlideshowCell.this.currentBlock.items.get(position);
                    if (block instanceof TL_pageBlockPhoto) {
                        view = new BlockPhotoCell(BlockSlideshowCell.this.getContext(), 1);
                        ((BlockPhotoCell) view).setBlock((TL_pageBlockPhoto) block, true, true);
                    } else {
                        view = new BlockVideoCell(BlockSlideshowCell.this.getContext(), 1);
                        ((BlockVideoCell) view).setBlock((TL_pageBlockVideo) block, true, true);
                    }
                    container.addView(view);
                    ObjectContainer objectContainer = new ObjectContainer();
                    objectContainer.view = view;
                    objectContainer.block = block;
                    return objectContainer;
                }

                public void destroyItem(ViewGroup container, int position, Object object) {
                    container.removeView(((ObjectContainer) object).view);
                }

                public void unregisterDataSetObserver(DataSetObserver observer) {
                    if (observer != null) {
                        super.unregisterDataSetObserver(observer);
                    }
                }
            };
            this.adapter = anonymousClass3;
            viewPager.setAdapter(anonymousClass3);
            int color = ArticleViewer.this.getSelectedColor();
            if (color == 0) {
                AndroidUtilities.setViewPagerEdgeEffectColor(this.innerListView, -657673);
            } else if (color == 1) {
                AndroidUtilities.setViewPagerEdgeEffectColor(this.innerListView, -659492);
            } else if (color == 2) {
                AndroidUtilities.setViewPagerEdgeEffectColor(this.innerListView, -15461356);
            }
            addView(this.innerListView);
            this.dotsContainer = new View(context, ArticleViewer.this) {
                protected void onDraw(Canvas canvas) {
                    if (BlockSlideshowCell.this.currentBlock != null) {
                        int selected = BlockSlideshowCell.this.innerListView.getCurrentItem();
                        int a = 0;
                        while (a < BlockSlideshowCell.this.currentBlock.items.size()) {
                            int cx = AndroidUtilities.dp(4.0f) + (AndroidUtilities.dp(13.0f) * a);
                            Drawable drawable = selected == a ? ArticleViewer.this.slideDotBigDrawable : ArticleViewer.this.slideDotDrawable;
                            drawable.setBounds(cx - AndroidUtilities.dp(5.0f), 0, AndroidUtilities.dp(5.0f) + cx, AndroidUtilities.dp(10.0f));
                            drawable.draw(canvas);
                            a++;
                        }
                    }
                }
            };
            addView(this.dotsContainer);
            setWillNotDraw(null);
        }

        public void setBlock(TL_pageBlockSlideshow block) {
            this.currentBlock = block;
            this.lastCreatedWidth = 0;
            this.innerListView.setCurrentItem(0, false);
            this.adapter.notifyDataSetChanged();
            requestLayout();
        }

        public boolean onTouchEvent(MotionEvent event) {
            if (!ArticleViewer.this.checkLayoutForLinks(event, this, this.textLayout, this.textX, this.textY)) {
                if (!super.onTouchEvent(event)) {
                    return false;
                }
            }
            return true;
        }

        protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
            int height;
            int width = MeasureSpec.getSize(widthMeasureSpec);
            if (this.currentBlock != null) {
                height = AndroidUtilities.dp(1134231552);
                this.innerListView.measure(MeasureSpec.makeMeasureSpec(width, 1073741824), MeasureSpec.makeMeasureSpec(height, 1073741824));
                int count = this.currentBlock.items.size();
                this.dotsContainer.measure(MeasureSpec.makeMeasureSpec(((AndroidUtilities.dp(7.0f) * count) + ((count - 1) * AndroidUtilities.dp(6.0f))) + AndroidUtilities.dp(4.0f), 1073741824), MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(10.0f), 1073741824));
                if (this.lastCreatedWidth != width) {
                    this.textY = AndroidUtilities.dp(16.0f) + height;
                    this.textLayout = ArticleViewer.this.createLayoutForText(null, this.currentBlock.caption, width - AndroidUtilities.dp(36.0f), this.currentBlock);
                    if (this.textLayout != null) {
                        height += AndroidUtilities.dp(8.0f) + this.textLayout.getHeight();
                    }
                }
                height += AndroidUtilities.dp(16.0f);
            } else {
                height = 1;
            }
            setMeasuredDimension(width, height);
        }

        protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
            this.innerListView.layout(0, AndroidUtilities.dp(8.0f), this.innerListView.getMeasuredWidth(), AndroidUtilities.dp(8.0f) + this.innerListView.getMeasuredHeight());
            int y = this.innerListView.getBottom() - AndroidUtilities.dp(23.0f);
            int x = ((right - left) - this.dotsContainer.getMeasuredWidth()) / 2;
            this.dotsContainer.layout(x, y, this.dotsContainer.getMeasuredWidth() + x, this.dotsContainer.getMeasuredHeight() + y);
        }

        protected void onDraw(Canvas canvas) {
            if (!(this.currentBlock == null || this.textLayout == null)) {
                canvas.save();
                canvas.translate((float) this.textX, (float) this.textY);
                ArticleViewer.this.drawLayoutLink(canvas, this.textLayout);
                this.textLayout.draw(canvas);
                canvas.restore();
            }
        }
    }

    private class BlockSubheaderCell extends View {
        private TL_pageBlockSubheader currentBlock;
        private int lastCreatedWidth;
        private StaticLayout textLayout;
        private int textX = AndroidUtilities.dp(18.0f);
        private int textY = AndroidUtilities.dp(8.0f);

        public BlockSubheaderCell(Context context) {
            super(context);
        }

        public void setBlock(TL_pageBlockSubheader block) {
            this.currentBlock = block;
            this.lastCreatedWidth = 0;
            requestLayout();
        }

        public boolean onTouchEvent(MotionEvent event) {
            if (!ArticleViewer.this.checkLayoutForLinks(event, this, this.textLayout, this.textX, this.textY)) {
                if (!super.onTouchEvent(event)) {
                    return false;
                }
            }
            return true;
        }

        protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
            int width = MeasureSpec.getSize(widthMeasureSpec);
            int height = 0;
            if (this.currentBlock == null) {
                height = 1;
            } else if (this.lastCreatedWidth != width) {
                this.textLayout = ArticleViewer.this.createLayoutForText(null, this.currentBlock.text, width - AndroidUtilities.dp(36.0f), this.currentBlock);
                if (this.textLayout != null) {
                    height = 0 + (AndroidUtilities.dp(16.0f) + this.textLayout.getHeight());
                }
            }
            setMeasuredDimension(width, height);
        }

        protected void onDraw(Canvas canvas) {
            if (!(this.currentBlock == null || this.textLayout == null)) {
                canvas.save();
                canvas.translate((float) this.textX, (float) this.textY);
                ArticleViewer.this.drawLayoutLink(canvas, this.textLayout);
                this.textLayout.draw(canvas);
                canvas.restore();
            }
        }
    }

    private class BlockSubtitleCell extends View {
        private TL_pageBlockSubtitle currentBlock;
        private int lastCreatedWidth;
        private StaticLayout textLayout;
        private int textX = AndroidUtilities.dp(18.0f);
        private int textY = AndroidUtilities.dp(8.0f);

        public BlockSubtitleCell(Context context) {
            super(context);
        }

        public void setBlock(TL_pageBlockSubtitle block) {
            this.currentBlock = block;
            this.lastCreatedWidth = 0;
            requestLayout();
        }

        public boolean onTouchEvent(MotionEvent event) {
            if (!ArticleViewer.this.checkLayoutForLinks(event, this, this.textLayout, this.textX, this.textY)) {
                if (!super.onTouchEvent(event)) {
                    return false;
                }
            }
            return true;
        }

        protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
            int width = MeasureSpec.getSize(widthMeasureSpec);
            int height = 0;
            if (this.currentBlock == null) {
                height = 1;
            } else if (this.lastCreatedWidth != width) {
                this.textLayout = ArticleViewer.this.createLayoutForText(null, this.currentBlock.text, width - AndroidUtilities.dp(36.0f), this.currentBlock);
                if (this.textLayout != null) {
                    height = 0 + (AndroidUtilities.dp(16.0f) + this.textLayout.getHeight());
                }
            }
            setMeasuredDimension(width, height);
        }

        protected void onDraw(Canvas canvas) {
            if (!(this.currentBlock == null || this.textLayout == null)) {
                canvas.save();
                canvas.translate((float) this.textX, (float) this.textY);
                ArticleViewer.this.drawLayoutLink(canvas, this.textLayout);
                this.textLayout.draw(canvas);
                canvas.restore();
            }
        }
    }

    private class BlockTitleCell extends View {
        private TL_pageBlockTitle currentBlock;
        private int lastCreatedWidth;
        private StaticLayout textLayout;
        private int textX = AndroidUtilities.dp(18.0f);
        private int textY;

        public BlockTitleCell(Context context) {
            super(context);
        }

        public void setBlock(TL_pageBlockTitle block) {
            this.currentBlock = block;
            this.lastCreatedWidth = 0;
            requestLayout();
        }

        public boolean onTouchEvent(MotionEvent event) {
            if (!ArticleViewer.this.checkLayoutForLinks(event, this, this.textLayout, this.textX, this.textY)) {
                if (!super.onTouchEvent(event)) {
                    return false;
                }
            }
            return true;
        }

        protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
            int width = MeasureSpec.getSize(widthMeasureSpec);
            int height = 0;
            if (this.currentBlock == null) {
                height = 1;
            } else if (this.lastCreatedWidth != width) {
                this.textLayout = ArticleViewer.this.createLayoutForText(null, this.currentBlock.text, width - AndroidUtilities.dp(36.0f), this.currentBlock);
                if (this.textLayout != null) {
                    height = 0 + (AndroidUtilities.dp(16.0f) + this.textLayout.getHeight());
                    if (ArticleViewer.this.isRtl == -1) {
                        int count = this.textLayout.getLineCount();
                        for (int a = 0; a < count; a++) {
                            if (this.textLayout.getLineLeft(a) > 0.0f) {
                                ArticleViewer.this.isRtl = 1;
                                break;
                            }
                        }
                        if (ArticleViewer.this.isRtl == -1) {
                            ArticleViewer.this.isRtl = 0;
                        }
                    }
                }
                if (this.currentBlock.first) {
                    height += AndroidUtilities.dp(8.0f);
                    this.textY = AndroidUtilities.dp(16.0f);
                } else {
                    this.textY = AndroidUtilities.dp(8.0f);
                }
            }
            setMeasuredDimension(width, height);
        }

        protected void onDraw(Canvas canvas) {
            if (!(this.currentBlock == null || this.textLayout == null)) {
                canvas.save();
                canvas.translate((float) this.textX, (float) this.textY);
                ArticleViewer.this.drawLayoutLink(canvas, this.textLayout);
                this.textLayout.draw(canvas);
                canvas.restore();
            }
        }
    }

    class CheckForLongPress implements Runnable {
        public int currentPressCount;

        CheckForLongPress() {
        }

        public void run() {
            if (ArticleViewer.this.checkingForLongPress && ArticleViewer.this.windowView != null) {
                ArticleViewer.this.checkingForLongPress = false;
                ArticleViewer.this.windowView.performHapticFeedback(0);
                if (ArticleViewer.this.pressedLink != null) {
                    final String urlFinal = ArticleViewer.this.pressedLink.getUrl();
                    Builder builder = new Builder(ArticleViewer.this.parentActivity);
                    builder.setTitle(urlFinal);
                    builder.setItems(new CharSequence[]{LocaleController.getString("Open", R.string.Open), LocaleController.getString("Copy", R.string.Copy)}, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            if (ArticleViewer.this.parentActivity != null) {
                                if (which == 0) {
                                    Browser.openUrl(ArticleViewer.this.parentActivity, urlFinal);
                                } else if (which == 1) {
                                    String url = urlFinal;
                                    if (url.startsWith("mailto:")) {
                                        url = url.substring(7);
                                    } else if (url.startsWith("tel:")) {
                                        url = url.substring(4);
                                    }
                                    AndroidUtilities.addToClipboard(url);
                                }
                            }
                        }
                    });
                    ArticleViewer.this.showDialog(builder.create());
                    ArticleViewer.this.hideActionBar();
                    ArticleViewer.this.pressedLink = null;
                    ArticleViewer.this.pressedLinkOwnerLayout = null;
                    ArticleViewer.this.pressedLinkOwnerView.invalidate();
                } else if (ArticleViewer.this.pressedLinkOwnerLayout != null && ArticleViewer.this.pressedLinkOwnerView != null) {
                    int y = (ArticleViewer.this.pressedLinkOwnerView.getTop() - AndroidUtilities.dp(54.0f)) + ArticleViewer.this.pressedLayoutY;
                    if (y < 0) {
                        y *= -1;
                    }
                    ArticleViewer.this.pressedLinkOwnerView.invalidate();
                    ArticleViewer.this.drawBlockSelection = true;
                    ArticleViewer.this.showPopup(ArticleViewer.this.pressedLinkOwnerView, 48, 0, y);
                    ArticleViewer.this.listView.setLayoutFrozen(true);
                    ArticleViewer.this.listView.setLayoutFrozen(false);
                }
            }
        }
    }

    private final class CheckForTap implements Runnable {
        private CheckForTap() {
        }

        public void run() {
            if (ArticleViewer.this.pendingCheckForLongPress == null) {
                ArticleViewer.this.pendingCheckForLongPress = new CheckForLongPress();
            }
            ArticleViewer.this.pendingCheckForLongPress.currentPressCount = ArticleViewer.access$804(ArticleViewer.this);
            if (ArticleViewer.this.windowView != null) {
                ArticleViewer.this.windowView.postDelayed(ArticleViewer.this.pendingCheckForLongPress, (long) (ViewConfiguration.getLongPressTimeout() - ViewConfiguration.getTapTimeout()));
            }
        }
    }

    public class ColorCell extends FrameLayout {
        private int currentColor;
        private boolean selected;
        private TextView textView;

        public ColorCell(Context context) {
            super(context);
            if (ArticleViewer.colorPaint == null) {
                ArticleViewer.colorPaint = new Paint(1);
                ArticleViewer.selectorPaint = new Paint(1);
                ArticleViewer.selectorPaint.setColor(-15428119);
                ArticleViewer.selectorPaint.setStyle(Style.STROKE);
                ArticleViewer.selectorPaint.setStrokeWidth((float) AndroidUtilities.dp(2.0f));
            }
            setBackgroundDrawable(Theme.createSelectorDrawable(251658240, 2));
            setWillNotDraw(false);
            this.textView = new TextView(context);
            this.textView.setTextColor(-14606047);
            this.textView.setTextSize(1, 16.0f);
            this.textView.setLines(1);
            this.textView.setMaxLines(1);
            this.textView.setSingleLine(true);
            int i = 3;
            this.textView.setGravity((LocaleController.isRTL ? 5 : 3) | 16);
            this.textView.setPadding(0, 0, 0, AndroidUtilities.dp(1.0f));
            View view = this.textView;
            if (LocaleController.isRTL) {
                i = 5;
            }
            int i2 = i | 48;
            int i3 = 53;
            float f = (float) (LocaleController.isRTL ? 17 : 53);
            if (!LocaleController.isRTL) {
                i3 = 17;
            }
            addView(view, LayoutHelper.createFrame(-1, -1.0f, i2, f, 0.0f, (float) i3, 0.0f));
        }

        protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
            super.onMeasure(MeasureSpec.makeMeasureSpec(MeasureSpec.getSize(widthMeasureSpec), 1073741824), MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(48.0f), 1073741824));
        }

        public void setTextAndColor(String text, int color) {
            this.textView.setText(text);
            this.currentColor = color;
            invalidate();
        }

        public void select(boolean value) {
            if (this.selected != value) {
                this.selected = value;
                invalidate();
            }
        }

        protected void onDraw(Canvas canvas) {
            ArticleViewer.colorPaint.setColor(this.currentColor);
            canvas.drawCircle((float) (!LocaleController.isRTL ? AndroidUtilities.dp(28.0f) : getMeasuredWidth() - AndroidUtilities.dp(28.0f)), (float) (getMeasuredHeight() / 2), (float) AndroidUtilities.dp(10.0f), ArticleViewer.colorPaint);
            if (this.selected) {
                ArticleViewer.selectorPaint.setStrokeWidth((float) AndroidUtilities.dp(2.0f));
                ArticleViewer.selectorPaint.setColor(-15428119);
                canvas.drawCircle((float) (!LocaleController.isRTL ? AndroidUtilities.dp(28.0f) : getMeasuredWidth() - AndroidUtilities.dp(28.0f)), (float) (getMeasuredHeight() / 2), (float) AndroidUtilities.dp(10.0f), ArticleViewer.selectorPaint);
            } else if (this.currentColor == -1) {
                ArticleViewer.selectorPaint.setStrokeWidth((float) AndroidUtilities.dp(1.0f));
                ArticleViewer.selectorPaint.setColor(-4539718);
                canvas.drawCircle((float) (!LocaleController.isRTL ? AndroidUtilities.dp(28.0f) : getMeasuredWidth() - AndroidUtilities.dp(28.0f)), (float) (getMeasuredHeight() / 2), (float) AndroidUtilities.dp(9.0f), ArticleViewer.selectorPaint);
            }
        }
    }

    public class FontCell extends FrameLayout {
        private TextView textView;
        private TextView textView2;
        final /* synthetic */ ArticleViewer this$0;

        public FontCell(ArticleViewer this$0, Context context) {
            Context context2 = context;
            this.this$0 = this$0;
            super(context2);
            setBackgroundDrawable(Theme.createSelectorDrawable(251658240, 2));
            this.textView = new TextView(context2);
            this.textView.setTextColor(-14606047);
            this.textView.setTextSize(1, 16.0f);
            this.textView.setLines(1);
            this.textView.setMaxLines(1);
            this.textView.setSingleLine(true);
            int i = 3;
            this.textView.setGravity((LocaleController.isRTL ? 5 : 3) | 16);
            View view = r0.textView;
            int i2 = (LocaleController.isRTL ? 5 : 3) | 48;
            int i3 = 53;
            float f = (float) (LocaleController.isRTL ? 17 : 53);
            if (!LocaleController.isRTL) {
                i3 = 17;
            }
            addView(view, LayoutHelper.createFrame(-1, -1.0f, i2, f, 0.0f, (float) i3, 0.0f));
            r0.textView2 = new TextView(context2);
            r0.textView2.setTextColor(-14606047);
            r0.textView2.setTextSize(1, 16.0f);
            r0.textView2.setLines(1);
            r0.textView2.setMaxLines(1);
            r0.textView2.setSingleLine(true);
            r0.textView2.setText("Aa");
            r0.textView2.setGravity((LocaleController.isRTL ? 5 : 3) | 16);
            view = r0.textView2;
            if (LocaleController.isRTL) {
                i = 5;
            }
            addView(view, LayoutHelper.createFrame(-1, -1.0f, i | 48, 17.0f, 0.0f, 17.0f, 0.0f));
        }

        protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
            super.onMeasure(MeasureSpec.makeMeasureSpec(MeasureSpec.getSize(widthMeasureSpec), 1073741824), MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(48.0f), 1073741824));
        }

        public void select(boolean value) {
            this.textView2.setTextColor(value ? -15428119 : -14606047);
        }

        public void setTextAndTypeface(String text, Typeface typeface) {
            this.textView.setText(text);
            this.textView.setTypeface(typeface);
            this.textView2.setTypeface(typeface);
            invalidate();
        }
    }

    private class FrameLayoutDrawer extends FrameLayout {
        public FrameLayoutDrawer(Context context) {
            super(context);
        }

        public boolean onTouchEvent(MotionEvent event) {
            ArticleViewer.this.processTouchEvent(event);
            return true;
        }

        protected void onDraw(Canvas canvas) {
            ArticleViewer.this.drawContent(canvas);
        }

        protected boolean drawChild(Canvas canvas, View child, long drawingTime) {
            return child != ArticleViewer.this.aspectRatioFrameLayout && super.drawChild(canvas, child, drawingTime);
        }
    }

    private class PhotoBackgroundDrawable extends ColorDrawable {
        private Runnable drawRunnable;

        public PhotoBackgroundDrawable(int color) {
            super(color);
        }

        @Keep
        public void setAlpha(int alpha) {
            if (ArticleViewer.this.parentActivity instanceof LaunchActivity) {
                boolean z;
                DrawerLayoutContainer drawerLayoutContainer = ((LaunchActivity) ArticleViewer.this.parentActivity).drawerLayoutContainer;
                if (ArticleViewer.this.isPhotoVisible) {
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
            if (getAlpha() != 0 && this.drawRunnable != null) {
                this.drawRunnable.run();
                this.drawRunnable = null;
            }
        }
    }

    public static class PlaceProviderObject {
        public int clipBottomAddition;
        public int clipTopAddition;
        public ImageReceiver imageReceiver;
        public int index;
        public View parentView;
        public int radius;
        public float scale = 1.0f;
        public int size;
        public BitmapHolder thumb;
        public int viewX;
        public int viewY;
    }

    private class RadialProgressView {
        private float alpha = 1.0f;
        private float animatedAlphaValue = 1.0f;
        private float animatedProgressValue = 0.0f;
        private float animationProgressStart = 0.0f;
        private int backgroundState = -1;
        private float currentProgress = 0.0f;
        private long currentProgressTime = 0;
        private long lastUpdateTime = 0;
        private View parent = null;
        private int previousBackgroundState = -2;
        private RectF progressRect = new RectF();
        private float radOffset = 0.0f;
        private float scale = 1.0f;
        private int size = AndroidUtilities.dp(64.0f);

        public RadialProgressView(Context context, View parentView) {
            if (ArticleViewer.decelerateInterpolator == null) {
                ArticleViewer.decelerateInterpolator = new DecelerateInterpolator(1.5f);
                ArticleViewer.progressPaint = new Paint(1);
                ArticleViewer.progressPaint.setStyle(Style.STROKE);
                ArticleViewer.progressPaint.setStrokeCap(Cap.ROUND);
                ArticleViewer.progressPaint.setStrokeWidth((float) AndroidUtilities.dp(3.0f));
                ArticleViewer.progressPaint.setColor(-1);
            }
            this.parent = parentView;
        }

        private void updateAnimation() {
            long newTime = System.currentTimeMillis();
            long dt = newTime - this.lastUpdateTime;
            this.lastUpdateTime = newTime;
            if (this.animatedProgressValue != 1.0f) {
                this.radOffset += ((float) (360 * dt)) / 3000.0f;
                float progressDiff = this.currentProgress - this.animationProgressStart;
                if (progressDiff > 0.0f) {
                    this.currentProgressTime += dt;
                    if (this.currentProgressTime >= 300) {
                        this.animatedProgressValue = this.currentProgress;
                        this.animationProgressStart = this.currentProgress;
                        this.currentProgressTime = 0;
                    } else {
                        this.animatedProgressValue = this.animationProgressStart + (ArticleViewer.decelerateInterpolator.getInterpolation(((float) this.currentProgressTime) / 300.0f) * progressDiff);
                    }
                }
                this.parent.invalidate();
            }
            if (this.animatedProgressValue >= 1.0f && this.previousBackgroundState != -2) {
                this.animatedAlphaValue -= ((float) dt) / 200.0f;
                if (this.animatedAlphaValue <= 0.0f) {
                    this.animatedAlphaValue = 0.0f;
                    this.previousBackgroundState = -2;
                }
                this.parent.invalidate();
            }
        }

        public void setProgress(float value, boolean animated) {
            if (animated) {
                this.animationProgressStart = this.animatedProgressValue;
            } else {
                this.animatedProgressValue = value;
                this.animationProgressStart = value;
            }
            this.currentProgress = value;
            this.currentProgressTime = 0;
        }

        public void setBackgroundState(int state, boolean animated) {
            this.lastUpdateTime = System.currentTimeMillis();
            if (!animated || this.backgroundState == state) {
                this.previousBackgroundState = -2;
            } else {
                this.previousBackgroundState = this.backgroundState;
                this.animatedAlphaValue = 1.0f;
            }
            this.backgroundState = state;
            this.parent.invalidate();
        }

        public void setAlpha(float value) {
            this.alpha = value;
        }

        public void setScale(float value) {
            this.scale = value;
        }

        public void onDraw(Canvas canvas) {
            Drawable drawable;
            int sizeScaled = (int) (((float) this.size) * this.scale);
            int x = (ArticleViewer.this.getContainerViewWidth() - sizeScaled) / 2;
            int y = (ArticleViewer.this.getContainerViewHeight() - sizeScaled) / 2;
            if (this.previousBackgroundState >= 0 && this.previousBackgroundState < 4) {
                drawable = ArticleViewer.progressDrawables[this.previousBackgroundState];
                if (drawable != null) {
                    drawable.setAlpha((int) ((this.animatedAlphaValue * 255.0f) * this.alpha));
                    drawable.setBounds(x, y, x + sizeScaled, y + sizeScaled);
                    drawable.draw(canvas);
                }
            }
            if (this.backgroundState >= 0 && this.backgroundState < 4) {
                drawable = ArticleViewer.progressDrawables[this.backgroundState];
                if (drawable != null) {
                    if (this.previousBackgroundState != -2) {
                        drawable.setAlpha((int) (((1.0f - this.animatedAlphaValue) * 255.0f) * this.alpha));
                    } else {
                        drawable.setAlpha((int) (this.alpha * 255.0f));
                    }
                    drawable.setBounds(x, y, x + sizeScaled, y + sizeScaled);
                    drawable.draw(canvas);
                }
            }
            if (this.backgroundState == 0 || this.backgroundState == 1 || this.previousBackgroundState == 0 || this.previousBackgroundState == 1) {
                int diff = AndroidUtilities.dp(4.0f);
                if (this.previousBackgroundState != -2) {
                    ArticleViewer.progressPaint.setAlpha((int) ((255.0f * this.animatedAlphaValue) * this.alpha));
                } else {
                    ArticleViewer.progressPaint.setAlpha((int) (255.0f * this.alpha));
                }
                this.progressRect.set((float) (x + diff), (float) (y + diff), (float) ((x + sizeScaled) - diff), (float) ((y + sizeScaled) - diff));
                canvas.drawArc(this.progressRect, -90.0f + this.radOffset, Math.max(4.0f, 360.0f * this.animatedProgressValue), false, ArticleViewer.progressPaint);
                updateAnimation();
            }
        }
    }

    private class SizeChooseView extends View {
        private int circleSize;
        private int gapSize;
        private int lineSize;
        private boolean moving;
        private Paint paint = new Paint(1);
        private int sideSide;
        private boolean startMoving;
        private int startMovingQuality;
        private float startX;

        public SizeChooseView(Context context) {
            super(context);
        }

        /* JADX WARNING: inconsistent code. */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public boolean onTouchEvent(android.view.MotionEvent r11) {
            /*
            r10 = this;
            r0 = r11.getX();
            r1 = r11.getAction();
            r2 = 5;
            r3 = 1097859072; // 0x41700000 float:15.0 double:5.424144515E-315;
            r4 = 0;
            r5 = 2;
            r6 = 1;
            if (r1 != 0) goto L_0x005b;
        L_0x0010:
            r1 = r10.getParent();
            r1.requestDisallowInterceptTouchEvent(r6);
            r1 = r4;
        L_0x0018:
            if (r1 >= r2) goto L_0x012c;
        L_0x001a:
            r7 = r10.sideSide;
            r8 = r10.lineSize;
            r9 = r10.gapSize;
            r9 = r9 * r5;
            r8 = r8 + r9;
            r9 = r10.circleSize;
            r8 = r8 + r9;
            r8 = r8 * r1;
            r7 = r7 + r8;
            r8 = r10.circleSize;
            r8 = r8 / r5;
            r7 = r7 + r8;
            r8 = org.telegram.messenger.AndroidUtilities.dp(r3);
            r8 = r7 - r8;
            r8 = (float) r8;
            r8 = (r0 > r8 ? 1 : (r0 == r8 ? 0 : -1));
            if (r8 <= 0) goto L_0x0058;
        L_0x0036:
            r8 = org.telegram.messenger.AndroidUtilities.dp(r3);
            r8 = r8 + r7;
            r8 = (float) r8;
            r8 = (r0 > r8 ? 1 : (r0 == r8 ? 0 : -1));
            if (r8 >= 0) goto L_0x0058;
        L_0x0040:
            r2 = org.telegram.ui.ArticleViewer.this;
            r2 = r2.selectedFontSize;
            if (r1 != r2) goto L_0x004a;
        L_0x0048:
            r4 = r6;
        L_0x004a:
            r10.startMoving = r4;
            r10.startX = r0;
            r2 = org.telegram.ui.ArticleViewer.this;
            r2 = r2.selectedFontSize;
            r10.startMovingQuality = r2;
            goto L_0x012c;
        L_0x0058:
            r1 = r1 + 1;
            goto L_0x0018;
        L_0x005b:
            r1 = r11.getAction();
            if (r1 != r5) goto L_0x00c6;
        L_0x0061:
            r1 = r10.startMoving;
            if (r1 == 0) goto L_0x007c;
        L_0x0065:
            r1 = r10.startX;
            r1 = r1 - r0;
            r1 = java.lang.Math.abs(r1);
            r2 = 1056964608; // 0x3f000000 float:0.5 double:5.222099017E-315;
            r2 = org.telegram.messenger.AndroidUtilities.getPixelsInCM(r2, r6);
            r1 = (r1 > r2 ? 1 : (r1 == r2 ? 0 : -1));
            if (r1 < 0) goto L_0x012c;
        L_0x0076:
            r10.moving = r6;
            r10.startMoving = r4;
            goto L_0x012c;
        L_0x007c:
            r1 = r10.moving;
            if (r1 == 0) goto L_0x012c;
        L_0x0081:
            r1 = r4;
            if (r1 >= r2) goto L_0x012c;
        L_0x0084:
            r3 = r10.sideSide;
            r4 = r10.lineSize;
            r7 = r10.gapSize;
            r7 = r7 * r5;
            r4 = r4 + r7;
            r7 = r10.circleSize;
            r4 = r4 + r7;
            r4 = r4 * r1;
            r3 = r3 + r4;
            r4 = r10.circleSize;
            r4 = r4 / r5;
            r3 = r3 + r4;
            r4 = r10.lineSize;
            r4 = r4 / r5;
            r7 = r10.circleSize;
            r7 = r7 / r5;
            r4 = r4 + r7;
            r7 = r10.gapSize;
            r4 = r4 + r7;
            r7 = r3 - r4;
            r7 = (float) r7;
            r7 = (r0 > r7 ? 1 : (r0 == r7 ? 0 : -1));
            if (r7 <= 0) goto L_0x00c3;
        L_0x00a6:
            r7 = r3 + r4;
            r7 = (float) r7;
            r7 = (r0 > r7 ? 1 : (r0 == r7 ? 0 : -1));
            if (r7 >= 0) goto L_0x00c3;
        L_0x00ad:
            r2 = org.telegram.ui.ArticleViewer.this;
            r2 = r2.selectedFontSize;
            if (r2 == r1) goto L_0x012c;
        L_0x00b5:
            r2 = org.telegram.ui.ArticleViewer.this;
            r2.selectedFontSize = r1;
            r2 = org.telegram.ui.ArticleViewer.this;
            r2.updatePaintSize();
            r10.invalidate();
            goto L_0x012c;
        L_0x00c3:
            r4 = r1 + 1;
            goto L_0x0081;
        L_0x00c6:
            r1 = r11.getAction();
            if (r1 == r6) goto L_0x00d3;
        L_0x00cc:
            r1 = r11.getAction();
            r7 = 3;
            if (r1 != r7) goto L_0x012c;
        L_0x00d3:
            r1 = r10.moving;
            if (r1 != 0) goto L_0x0119;
        L_0x00d7:
            r1 = r4;
        L_0x00d8:
            if (r1 >= r2) goto L_0x0128;
        L_0x00da:
            r7 = r10.sideSide;
            r8 = r10.lineSize;
            r9 = r10.gapSize;
            r9 = r9 * r5;
            r8 = r8 + r9;
            r9 = r10.circleSize;
            r8 = r8 + r9;
            r8 = r8 * r1;
            r7 = r7 + r8;
            r8 = r10.circleSize;
            r8 = r8 / r5;
            r7 = r7 + r8;
            r8 = org.telegram.messenger.AndroidUtilities.dp(r3);
            r8 = r7 - r8;
            r8 = (float) r8;
            r8 = (r0 > r8 ? 1 : (r0 == r8 ? 0 : -1));
            if (r8 <= 0) goto L_0x0116;
        L_0x00f6:
            r8 = org.telegram.messenger.AndroidUtilities.dp(r3);
            r8 = r8 + r7;
            r8 = (float) r8;
            r8 = (r0 > r8 ? 1 : (r0 == r8 ? 0 : -1));
            if (r8 >= 0) goto L_0x0116;
        L_0x0100:
            r2 = org.telegram.ui.ArticleViewer.this;
            r2 = r2.selectedFontSize;
            if (r2 == r1) goto L_0x0128;
        L_0x0108:
            r2 = org.telegram.ui.ArticleViewer.this;
            r2.selectedFontSize = r1;
            r2 = org.telegram.ui.ArticleViewer.this;
            r2.updatePaintSize();
            r10.invalidate();
            goto L_0x0128;
        L_0x0116:
            r1 = r1 + 1;
            goto L_0x00d8;
        L_0x0119:
            r1 = org.telegram.ui.ArticleViewer.this;
            r1 = r1.selectedFontSize;
            r2 = r10.startMovingQuality;
            if (r1 == r2) goto L_0x0128;
        L_0x0123:
            r1 = org.telegram.ui.ArticleViewer.this;
            r1.updatePaintSize();
        L_0x0128:
            r10.startMoving = r4;
            r10.moving = r4;
        L_0x012c:
            return r6;
            */
            throw new UnsupportedOperationException("Method not decompiled: org.telegram.ui.ArticleViewer.SizeChooseView.onTouchEvent(android.view.MotionEvent):boolean");
        }

        protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
            int width = MeasureSpec.getSize(widthMeasureSpec);
            this.circleSize = AndroidUtilities.dp(5.0f);
            this.gapSize = AndroidUtilities.dp(2.0f);
            this.sideSide = AndroidUtilities.dp(17.0f);
            this.lineSize = (((getMeasuredWidth() - (this.circleSize * 5)) - (this.gapSize * 8)) - (this.sideSide * 2)) / 4;
        }

        protected void onDraw(Canvas canvas) {
            int cy = getMeasuredHeight() / 2;
            int a = 0;
            while (a < 5) {
                int cx = (this.sideSide + (((this.lineSize + (this.gapSize * 2)) + this.circleSize) * a)) + (this.circleSize / 2);
                if (a <= ArticleViewer.this.selectedFontSize) {
                    this.paint.setColor(-15428119);
                } else {
                    this.paint.setColor(-3355444);
                }
                canvas.drawCircle((float) cx, (float) cy, (float) (a == ArticleViewer.this.selectedFontSize ? AndroidUtilities.dp(4.0f) : this.circleSize / 2), this.paint);
                if (a != 0) {
                    int x = ((cx - (this.circleSize / 2)) - this.gapSize) - this.lineSize;
                    canvas.drawRect((float) x, (float) (cy - AndroidUtilities.dp(1.0f)), (float) (this.lineSize + x), (float) (AndroidUtilities.dp(1.0f) + cy), this.paint);
                }
                a++;
            }
        }
    }

    private class WindowView extends FrameLayout {
        private float alpha;
        private Runnable attachRunnable;
        private boolean closeAnimationInProgress;
        private float innerTranslationX;
        private boolean maybeStartTracking;
        private boolean selfLayout;
        private boolean startedTracking;
        private int startedTrackingPointerId;
        private int startedTrackingX;
        private int startedTrackingY;
        private VelocityTracker tracker;

        public WindowView(Context context) {
            super(context);
        }

        protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
            int widthSize = MeasureSpec.getSize(widthMeasureSpec);
            int heightSize = MeasureSpec.getSize(heightMeasureSpec);
            if (VERSION.SDK_INT < 21 || ArticleViewer.this.lastInsets == null) {
                setMeasuredDimension(widthSize, heightSize);
            } else {
                setMeasuredDimension(widthSize, heightSize);
                WindowInsets insets = (WindowInsets) ArticleViewer.this.lastInsets;
                if (AndroidUtilities.incorrectDisplaySizeFix) {
                    if (heightSize > AndroidUtilities.displaySize.y) {
                        heightSize = AndroidUtilities.displaySize.y;
                    }
                    heightSize += AndroidUtilities.statusBarHeight;
                }
                heightSize -= insets.getSystemWindowInsetBottom();
                widthSize -= insets.getSystemWindowInsetRight() + insets.getSystemWindowInsetLeft();
                if (insets.getSystemWindowInsetRight() != 0) {
                    ArticleViewer.this.barBackground.measure(MeasureSpec.makeMeasureSpec(insets.getSystemWindowInsetRight(), 1073741824), MeasureSpec.makeMeasureSpec(heightSize, 1073741824));
                } else if (insets.getSystemWindowInsetLeft() != 0) {
                    ArticleViewer.this.barBackground.measure(MeasureSpec.makeMeasureSpec(insets.getSystemWindowInsetLeft(), 1073741824), MeasureSpec.makeMeasureSpec(heightSize, 1073741824));
                } else {
                    ArticleViewer.this.barBackground.measure(MeasureSpec.makeMeasureSpec(widthSize, 1073741824), MeasureSpec.makeMeasureSpec(insets.getSystemWindowInsetBottom(), 1073741824));
                }
            }
            ArticleViewer.this.containerView.measure(MeasureSpec.makeMeasureSpec(widthSize, 1073741824), MeasureSpec.makeMeasureSpec(heightSize, 1073741824));
            ArticleViewer.this.photoContainerView.measure(MeasureSpec.makeMeasureSpec(widthSize, 1073741824), MeasureSpec.makeMeasureSpec(heightSize, 1073741824));
            ArticleViewer.this.photoContainerBackground.measure(MeasureSpec.makeMeasureSpec(widthSize, 1073741824), MeasureSpec.makeMeasureSpec(heightSize, 1073741824));
            ArticleViewer.this.fullscreenVideoContainer.measure(MeasureSpec.makeMeasureSpec(widthSize, 1073741824), MeasureSpec.makeMeasureSpec(heightSize, 1073741824));
            ViewGroup.LayoutParams layoutParams = ArticleViewer.this.animatingImageView.getLayoutParams();
            ArticleViewer.this.animatingImageView.measure(MeasureSpec.makeMeasureSpec(layoutParams.width, Integer.MIN_VALUE), MeasureSpec.makeMeasureSpec(layoutParams.height, Integer.MIN_VALUE));
        }

        protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
            if (!this.selfLayout) {
                int x;
                if (VERSION.SDK_INT < 21 || ArticleViewer.this.lastInsets == null) {
                    x = 0;
                } else {
                    WindowInsets insets = (WindowInsets) ArticleViewer.this.lastInsets;
                    x = insets.getSystemWindowInsetLeft();
                    if (insets.getSystemWindowInsetRight() != 0) {
                        ArticleViewer.this.barBackground.layout((right - left) - insets.getSystemWindowInsetRight(), 0, right - left, bottom - top);
                    } else if (insets.getSystemWindowInsetLeft() != 0) {
                        ArticleViewer.this.barBackground.layout(0, 0, insets.getSystemWindowInsetLeft(), bottom - top);
                    } else {
                        ArticleViewer.this.barBackground.layout(0, (bottom - top) - insets.getStableInsetBottom(), right - left, bottom - top);
                    }
                }
                int x2 = x;
                ArticleViewer.this.containerView.layout(x2, 0, ArticleViewer.this.containerView.getMeasuredWidth() + x2, ArticleViewer.this.containerView.getMeasuredHeight());
                ArticleViewer.this.photoContainerView.layout(x2, 0, ArticleViewer.this.photoContainerView.getMeasuredWidth() + x2, ArticleViewer.this.photoContainerView.getMeasuredHeight());
                ArticleViewer.this.photoContainerBackground.layout(x2, 0, ArticleViewer.this.photoContainerBackground.getMeasuredWidth() + x2, ArticleViewer.this.photoContainerBackground.getMeasuredHeight());
                ArticleViewer.this.fullscreenVideoContainer.layout(x2, 0, ArticleViewer.this.fullscreenVideoContainer.getMeasuredWidth() + x2, ArticleViewer.this.fullscreenVideoContainer.getMeasuredHeight());
                ArticleViewer.this.animatingImageView.layout(0, 0, ArticleViewer.this.animatingImageView.getMeasuredWidth(), ArticleViewer.this.animatingImageView.getMeasuredHeight());
            }
        }

        protected void onAttachedToWindow() {
            super.onAttachedToWindow();
            ArticleViewer.this.attachedToWindow = true;
        }

        protected void onDetachedFromWindow() {
            super.onDetachedFromWindow();
            ArticleViewer.this.attachedToWindow = false;
        }

        public void requestDisallowInterceptTouchEvent(boolean disallowIntercept) {
            handleTouchEvent(null);
            super.requestDisallowInterceptTouchEvent(disallowIntercept);
        }

        public boolean onInterceptTouchEvent(MotionEvent ev) {
            return !ArticleViewer.this.collapsed && (handleTouchEvent(ev) || super.onInterceptTouchEvent(ev));
        }

        public boolean onTouchEvent(MotionEvent event) {
            return !ArticleViewer.this.collapsed && (handleTouchEvent(event) || super.onTouchEvent(event));
        }

        @Keep
        public void setInnerTranslationX(float value) {
            this.innerTranslationX = value;
            if (ArticleViewer.this.parentActivity instanceof LaunchActivity) {
                boolean z;
                DrawerLayoutContainer drawerLayoutContainer = ((LaunchActivity) ArticleViewer.this.parentActivity).drawerLayoutContainer;
                if (ArticleViewer.this.isVisible && this.alpha == 1.0f) {
                    if (this.innerTranslationX == 0.0f) {
                        z = false;
                        drawerLayoutContainer.setAllowDrawContent(z);
                    }
                }
                z = true;
                drawerLayoutContainer.setAllowDrawContent(z);
            }
            invalidate();
        }

        protected boolean drawChild(Canvas canvas, View child, long drawingTime) {
            Canvas canvas2 = canvas;
            int width = getMeasuredWidth();
            int translationX = (int) this.innerTranslationX;
            int restoreCount = canvas.save();
            canvas2.clipRect(translationX, 0, width, getHeight());
            boolean result = super.drawChild(canvas, child, drawingTime);
            canvas2.restoreToCount(restoreCount);
            if (translationX == 0) {
                View view = child;
            } else if (child == ArticleViewer.this.containerView) {
                float opacity = Math.min(0.8f, ((float) (width - translationX)) / ((float) width));
                if (opacity < 0.0f) {
                    opacity = 0.0f;
                }
                ArticleViewer.this.scrimPaint.setColor(((int) (153.0f * opacity)) << 24);
                canvas2.drawRect(0.0f, 0.0f, (float) translationX, (float) getHeight(), ArticleViewer.this.scrimPaint);
                opacity = Math.max(0.0f, Math.min(((float) (width - translationX)) / ((float) AndroidUtilities.dp(20.0f)), 1.0f));
                ArticleViewer.this.layerShadowDrawable.setBounds(translationX - ArticleViewer.this.layerShadowDrawable.getIntrinsicWidth(), child.getTop(), translationX, child.getBottom());
                ArticleViewer.this.layerShadowDrawable.setAlpha((int) (255.0f * opacity));
                ArticleViewer.this.layerShadowDrawable.draw(canvas2);
            }
            return result;
        }

        @Keep
        public float getInnerTranslationX() {
            return this.innerTranslationX;
        }

        private void prepareForMoving(MotionEvent ev) {
            this.maybeStartTracking = false;
            this.startedTracking = true;
            this.startedTrackingX = (int) ev.getX();
        }

        public boolean handleTouchEvent(MotionEvent event) {
            if (ArticleViewer.this.isPhotoVisible || this.closeAnimationInProgress || ArticleViewer.this.fullscreenVideoContainer.getVisibility() == 0) {
                return false;
            }
            if (event != null && event.getAction() == 0 && !this.startedTracking && !this.maybeStartTracking) {
                this.startedTrackingPointerId = event.getPointerId(0);
                this.maybeStartTracking = true;
                this.startedTrackingX = (int) event.getX();
                this.startedTrackingY = (int) event.getY();
                if (this.tracker != null) {
                    this.tracker.clear();
                }
            } else if (event != null && event.getAction() == 2 && event.getPointerId(0) == this.startedTrackingPointerId) {
                if (this.tracker == null) {
                    this.tracker = VelocityTracker.obtain();
                }
                int dx = Math.max(0, (int) (event.getX() - ((float) this.startedTrackingX)));
                int dy = Math.abs(((int) event.getY()) - this.startedTrackingY);
                this.tracker.addMovement(event);
                if (this.maybeStartTracking && !this.startedTracking && ((float) dx) >= AndroidUtilities.getPixelsInCM(0.4f, true) && Math.abs(dx) / 3 > dy) {
                    prepareForMoving(event);
                } else if (this.startedTracking) {
                    ArticleViewer.this.containerView.setTranslationX((float) dx);
                    setInnerTranslationX((float) dx);
                }
            } else if (event != null && event.getPointerId(0) == this.startedTrackingPointerId && (event.getAction() == 3 || event.getAction() == 1 || event.getAction() == 6)) {
                float velX;
                if (this.tracker == null) {
                    this.tracker = VelocityTracker.obtain();
                }
                this.tracker.computeCurrentVelocity(1000);
                if (!this.startedTracking) {
                    velX = this.tracker.getXVelocity();
                    float velY = this.tracker.getYVelocity();
                    if (velX >= 3500.0f && velX > Math.abs(velY)) {
                        prepareForMoving(event);
                    }
                }
                if (this.startedTracking) {
                    float distToMove;
                    velX = ArticleViewer.this.containerView.getX();
                    AnimatorSet animatorSet = new AnimatorSet();
                    float velX2 = this.tracker.getXVelocity();
                    final boolean backAnimation = velX < ((float) ArticleViewer.this.containerView.getMeasuredWidth()) / 3.0f && (velX2 < 3500.0f || velX2 < this.tracker.getYVelocity());
                    Animator[] animatorArr;
                    if (backAnimation) {
                        distToMove = velX;
                        animatorArr = new Animator[2];
                        animatorArr[0] = ObjectAnimator.ofFloat(ArticleViewer.this.containerView, "translationX", new float[]{0.0f});
                        animatorArr[1] = ObjectAnimator.ofFloat(this, "innerTranslationX", new float[]{0.0f});
                        animatorSet.playTogether(animatorArr);
                    } else {
                        distToMove = ((float) ArticleViewer.this.containerView.getMeasuredWidth()) - velX;
                        animatorArr = new Animator[2];
                        animatorArr[0] = ObjectAnimator.ofFloat(ArticleViewer.this.containerView, "translationX", new float[]{(float) ArticleViewer.this.containerView.getMeasuredWidth()});
                        animatorArr[1] = ObjectAnimator.ofFloat(this, "innerTranslationX", new float[]{(float) ArticleViewer.this.containerView.getMeasuredWidth()});
                        animatorSet.playTogether(animatorArr);
                    }
                    animatorSet.setDuration((long) Math.max((int) ((200.0f / ((float) ArticleViewer.this.containerView.getMeasuredWidth())) * distToMove), 50));
                    animatorSet.addListener(new AnimatorListenerAdapter() {
                        public void onAnimationEnd(Animator animator) {
                            if (!backAnimation) {
                                ArticleViewer.this.saveCurrentPagePosition();
                                ArticleViewer.this.onClosed();
                            }
                            WindowView.this.startedTracking = false;
                            WindowView.this.closeAnimationInProgress = false;
                        }
                    });
                    animatorSet.start();
                    this.closeAnimationInProgress = true;
                } else {
                    this.maybeStartTracking = false;
                    this.startedTracking = false;
                }
                if (this.tracker != null) {
                    this.tracker.recycle();
                    this.tracker = null;
                }
            } else if (event == null) {
                this.maybeStartTracking = false;
                this.startedTracking = false;
                if (this.tracker != null) {
                    this.tracker.recycle();
                    this.tracker = null;
                }
            }
            return this.startedTracking;
        }

        protected void onDraw(Canvas canvas) {
            canvas.drawRect(this.innerTranslationX, 0.0f, (float) getMeasuredWidth(), (float) getMeasuredHeight(), ArticleViewer.this.backgroundPaint);
        }

        @Keep
        public void setAlpha(float value) {
            ArticleViewer.this.backgroundPaint.setAlpha((int) (255.0f * value));
            this.alpha = value;
            if (ArticleViewer.this.parentActivity instanceof LaunchActivity) {
                boolean z;
                DrawerLayoutContainer drawerLayoutContainer = ((LaunchActivity) ArticleViewer.this.parentActivity).drawerLayoutContainer;
                if (ArticleViewer.this.isVisible && this.alpha == 1.0f) {
                    if (this.innerTranslationX == 0.0f) {
                        z = false;
                        drawerLayoutContainer.setAllowDrawContent(z);
                    }
                }
                z = true;
                drawerLayoutContainer.setAllowDrawContent(z);
            }
            invalidate();
        }

        @Keep
        public float getAlpha() {
            return this.alpha;
        }
    }

    class AnonymousClass24 implements RequestDelegate {
        final /* synthetic */ int val$currentAccount;
        final /* synthetic */ MessageObject val$messageObject;
        final /* synthetic */ WebPage val$webPageFinal;

        AnonymousClass24(WebPage webPage, MessageObject messageObject, int i) {
            this.val$webPageFinal = webPage;
            this.val$messageObject = messageObject;
            this.val$currentAccount = i;
        }

        public void run(TLObject response, TL_error error) {
            if (response instanceof TL_webPage) {
                final TL_webPage webPage = (TL_webPage) response;
                if (webPage.cached_page != null) {
                    AndroidUtilities.runOnUIThread(new Runnable() {
                        public void run() {
                            if (!ArticleViewer.this.pagesStack.isEmpty() && ArticleViewer.this.pagesStack.get(0) == AnonymousClass24.this.val$webPageFinal && webPage.cached_page != null) {
                                if (AnonymousClass24.this.val$messageObject != null) {
                                    AnonymousClass24.this.val$messageObject.messageOwner.media.webpage = webPage;
                                }
                                ArticleViewer.this.pagesStack.set(0, webPage);
                                if (ArticleViewer.this.pagesStack.size() == 1) {
                                    ArticleViewer.this.currentPage = webPage;
                                    Editor edit = ApplicationLoader.applicationContext.getSharedPreferences("articles", 0).edit();
                                    StringBuilder stringBuilder = new StringBuilder();
                                    stringBuilder.append("article");
                                    stringBuilder.append(ArticleViewer.this.currentPage.id);
                                    edit.remove(stringBuilder.toString()).commit();
                                    ArticleViewer.this.updateInterfaceForCurrentPage(false);
                                }
                            }
                        }
                    });
                    LongSparseArray<WebPage> webpages = new LongSparseArray(1);
                    webpages.put(webPage.id, webPage);
                    MessagesStorage.getInstance(this.val$currentAccount).putWebPages(webpages);
                }
            }
        }
    }

    private class BlockAudioCell extends View implements FileDownloadProgressListener {
        private int TAG;
        private int buttonPressed;
        private int buttonState;
        private int buttonX;
        private int buttonY;
        private TL_pageBlockAudio currentBlock;
        private Document currentDocument;
        private MessageObject currentMessageObject;
        private StaticLayout durationLayout;
        private boolean isFirst;
        private boolean isLast;
        private int lastCreatedWidth;
        private String lastTimeString;
        private RadialProgress radialProgress = new RadialProgress(this);
        private SeekBar seekBar;
        private int seekBarX;
        private int seekBarY;
        private StaticLayout textLayout;
        private int textX;
        private int textY = AndroidUtilities.dp(54.0f);
        private StaticLayout titleLayout;

        public BlockAudioCell(Context context) {
            super(context);
            this.radialProgress.setAlphaForPrevious(true);
            this.radialProgress.setDiff(AndroidUtilities.dp(0.0f));
            this.radialProgress.setStrikeWidth(AndroidUtilities.dp(2.0f));
            this.TAG = DownloadController.getInstance(ArticleViewer.this.currentAccount).generateObserverTag();
            this.seekBar = new SeekBar(context);
            this.seekBar.setDelegate(new SeekBarDelegate(ArticleViewer.this) {
                public void onSeekBarDrag(float progress) {
                    if (BlockAudioCell.this.currentMessageObject != null) {
                        BlockAudioCell.this.currentMessageObject.audioProgress = progress;
                        MediaController.getInstance().seekToProgress(BlockAudioCell.this.currentMessageObject, progress);
                    }
                }
            });
        }

        public void setBlock(TL_pageBlockAudio block, boolean first, boolean last) {
            this.currentBlock = block;
            this.currentMessageObject = (MessageObject) ArticleViewer.this.audioBlocks.get(this.currentBlock);
            this.currentDocument = this.currentMessageObject.getDocument();
            this.lastCreatedWidth = 0;
            this.isFirst = first;
            this.isLast = last;
            this.radialProgress.setProgressColor(ArticleViewer.this.getTextColor());
            this.seekBar.setColors(ArticleViewer.this.getTextColor() & 1073741823, ArticleViewer.this.getTextColor() & 1073741823, ArticleViewer.this.getTextColor(), ArticleViewer.this.getTextColor(), ArticleViewer.this.getTextColor());
            updateButtonState(false);
            requestLayout();
        }

        public MessageObject getMessageObject() {
            return this.currentMessageObject;
        }

        public boolean onTouchEvent(MotionEvent event) {
            float x = event.getX();
            float y = event.getY();
            boolean z = true;
            if (this.seekBar.onTouch(event.getAction(), event.getX() - ((float) this.seekBarX), event.getY() - ((float) this.seekBarY))) {
                if (event.getAction() == 0) {
                    getParent().requestDisallowInterceptTouchEvent(true);
                }
                invalidate();
                return true;
            }
            if (event.getAction() == 0) {
                if ((this.buttonState != -1 && x >= ((float) this.buttonX) && x <= ((float) (this.buttonX + AndroidUtilities.dp(48.0f))) && y >= ((float) this.buttonY) && y <= ((float) (this.buttonY + AndroidUtilities.dp(48.0f)))) || this.buttonState == 0) {
                    this.buttonPressed = 1;
                    invalidate();
                }
            } else if (event.getAction() == 1) {
                if (this.buttonPressed == 1) {
                    this.buttonPressed = 0;
                    playSoundEffect(0);
                    didPressedButton(false);
                    this.radialProgress.swapBackground(getDrawableForCurrentState());
                    invalidate();
                }
            } else if (event.getAction() == 3) {
                this.buttonPressed = 0;
            }
            if (this.buttonPressed == 0 && !ArticleViewer.this.checkLayoutForLinks(event, this, this.textLayout, this.textX, this.textY)) {
                if (!super.onTouchEvent(event)) {
                    z = false;
                }
            }
            return z;
        }

        @SuppressLint({"DrawAllocation"})
        protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
            int width = MeasureSpec.getSize(widthMeasureSpec);
            int height = AndroidUtilities.dp(1113063424);
            if (this.currentBlock != null) {
                int height2;
                float f;
                SpannableStringBuilder stringBuilder;
                int i;
                if (r0.currentBlock.level > 0) {
                    r0.textX = AndroidUtilities.dp((float) (14 * r0.currentBlock.level)) + AndroidUtilities.dp(18.0f);
                } else {
                    r0.textX = AndroidUtilities.dp(18.0f);
                }
                int textWidth = (width - r0.textX) - AndroidUtilities.dp(18.0f);
                int size = AndroidUtilities.dp(1109393408);
                r0.buttonX = AndroidUtilities.dp(16.0f);
                r0.buttonY = AndroidUtilities.dp(7.0f);
                r0.currentBlock.caption = new TL_textPlain();
                r0.radialProgress.setProgressRect(r0.buttonX, r0.buttonY, r0.buttonX + size, r0.buttonY + size);
                if (r0.lastCreatedWidth != width) {
                    r0.textLayout = ArticleViewer.this.createLayoutForText(null, r0.currentBlock.caption, textWidth, r0.currentBlock);
                    if (r0.textLayout != null) {
                        height += AndroidUtilities.dp(8.0f) + r0.textLayout.getHeight();
                    }
                }
                if (!r0.isFirst && r0.currentBlock.level <= 0) {
                    height += AndroidUtilities.dp(8.0f);
                }
                String author = r0.currentMessageObject.getMusicAuthor(false);
                String title = r0.currentMessageObject.getMusicTitle(false);
                r0.seekBarX = (r0.buttonX + AndroidUtilities.dp(50.0f)) + size;
                int w = (width - r0.seekBarX) - AndroidUtilities.dp(18.0f);
                if (TextUtils.isEmpty(title)) {
                    if (TextUtils.isEmpty(author)) {
                        r0.titleLayout = null;
                        r0.seekBarY = r0.buttonY + ((size - AndroidUtilities.dp(30.0f)) / 2);
                        height2 = height;
                        f = 30.0f;
                        r0.seekBar.setSize(w, AndroidUtilities.dp(f));
                        height = height2;
                    }
                }
                if (!TextUtils.isEmpty(title) && !TextUtils.isEmpty(author)) {
                    stringBuilder = new SpannableStringBuilder(String.format("%s - %s", new Object[]{author, title}));
                } else if (TextUtils.isEmpty(title)) {
                    stringBuilder = new SpannableStringBuilder(author);
                    if (!TextUtils.isEmpty(author)) {
                        stringBuilder.setSpan(new TypefaceSpan(AndroidUtilities.getTypeface("fonts/rmedium.ttf")), 0, author.length(), 18);
                    }
                    height2 = height;
                    f = 30.0f;
                    i = 2;
                    r0.titleLayout = new StaticLayout(TextUtils.ellipsize(stringBuilder, Theme.chat_audioTitlePaint, (float) w, TruncateAt.END), ArticleViewer.audioTimePaint, w, Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);
                    r0.seekBarY = (r0.buttonY + ((size - AndroidUtilities.dp(f)) / 2)) + AndroidUtilities.dp(11.0f);
                    r0.seekBar.setSize(w, AndroidUtilities.dp(f));
                    height = height2;
                } else {
                    stringBuilder = new SpannableStringBuilder(title);
                }
                if (TextUtils.isEmpty(author)) {
                    stringBuilder.setSpan(new TypefaceSpan(AndroidUtilities.getTypeface("fonts/rmedium.ttf")), 0, author.length(), 18);
                }
                height2 = height;
                f = 30.0f;
                i = 2;
                r0.titleLayout = new StaticLayout(TextUtils.ellipsize(stringBuilder, Theme.chat_audioTitlePaint, (float) w, TruncateAt.END), ArticleViewer.audioTimePaint, w, Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);
                r0.seekBarY = (r0.buttonY + ((size - AndroidUtilities.dp(f)) / 2)) + AndroidUtilities.dp(11.0f);
                r0.seekBar.setSize(w, AndroidUtilities.dp(f));
                height = height2;
            } else {
                height = 1;
            }
            setMeasuredDimension(width, height);
            updatePlayingMessageProgress();
        }

        protected void onDraw(Canvas canvas) {
            if (this.currentBlock != null) {
                this.radialProgress.draw(canvas);
                canvas.save();
                canvas.translate((float) this.seekBarX, (float) this.seekBarY);
                this.seekBar.draw(canvas);
                canvas.restore();
                if (this.durationLayout != null) {
                    canvas.save();
                    canvas.translate((float) (this.buttonX + AndroidUtilities.dp(54.0f)), (float) (this.seekBarY + AndroidUtilities.dp(6.0f)));
                    this.durationLayout.draw(canvas);
                    canvas.restore();
                }
                if (this.titleLayout != null) {
                    canvas.save();
                    canvas.translate((float) (this.buttonX + AndroidUtilities.dp(54.0f)), (float) (this.seekBarY - AndroidUtilities.dp(16.0f)));
                    this.titleLayout.draw(canvas);
                    canvas.restore();
                }
                if (this.textLayout != null) {
                    canvas.save();
                    canvas.translate((float) this.textX, (float) this.textY);
                    ArticleViewer.this.drawLayoutLink(canvas, this.textLayout);
                    this.textLayout.draw(canvas);
                    canvas.restore();
                }
                if (this.currentBlock.level > 0) {
                    canvas.drawRect((float) AndroidUtilities.dp(18.0f), 0.0f, (float) AndroidUtilities.dp(20.0f), (float) (getMeasuredHeight() - (this.currentBlock.bottom ? AndroidUtilities.dp(6.0f) : 0)), ArticleViewer.quoteLinePaint);
                }
            }
        }

        private Drawable getDrawableForCurrentState() {
            return Theme.chat_ivStatesDrawable[this.buttonState][this.buttonPressed != 0 ? 1 : 0];
        }

        public void updatePlayingMessageProgress() {
            if (this.currentDocument != null) {
                if (this.currentMessageObject != null) {
                    if (!this.seekBar.isDragging()) {
                        this.seekBar.setProgress(this.currentMessageObject.audioProgress);
                    }
                    int duration = 0;
                    if (MediaController.getInstance().isPlayingMessage(this.currentMessageObject)) {
                        duration = this.currentMessageObject.audioProgressSec;
                    } else {
                        for (int a = 0; a < this.currentDocument.attributes.size(); a++) {
                            DocumentAttribute attribute = (DocumentAttribute) this.currentDocument.attributes.get(a);
                            if (attribute instanceof TL_documentAttributeAudio) {
                                duration = attribute.duration;
                                break;
                            }
                        }
                    }
                    String timeString = String.format("%d:%02d", new Object[]{Integer.valueOf(duration / 60), Integer.valueOf(duration % 60)});
                    if (this.lastTimeString == null || !(this.lastTimeString == null || this.lastTimeString.equals(timeString))) {
                        this.lastTimeString = timeString;
                        ArticleViewer.audioTimePaint.setTextSize((float) AndroidUtilities.dp(16.0f));
                        this.durationLayout = new StaticLayout(timeString, ArticleViewer.audioTimePaint, (int) Math.ceil((double) ArticleViewer.audioTimePaint.measureText(timeString)), Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);
                    }
                    ArticleViewer.audioTimePaint.setColor(ArticleViewer.this.getTextColor());
                    invalidate();
                }
            }
        }

        public void updateButtonState(boolean animated) {
            String fileName = FileLoader.getAttachFileName(this.currentDocument);
            boolean fileExists = FileLoader.getPathToAttach(this.currentDocument, true).exists();
            if (TextUtils.isEmpty(fileName)) {
                this.radialProgress.setBackground(null, false, false);
                return;
            }
            if (fileExists) {
                DownloadController.getInstance(ArticleViewer.this.currentAccount).removeLoadingFileObserver(this);
                boolean playing = MediaController.getInstance().isPlayingMessage(this.currentMessageObject);
                if (playing) {
                    if (!playing || !MediaController.getInstance().isMessagePaused()) {
                        this.buttonState = 1;
                        this.radialProgress.setBackground(getDrawableForCurrentState(), false, animated);
                    }
                }
                this.buttonState = 0;
                this.radialProgress.setBackground(getDrawableForCurrentState(), false, animated);
            } else {
                DownloadController.getInstance(ArticleViewer.this.currentAccount).addLoadingFileObserver(fileName, null, this);
                if (FileLoader.getInstance(ArticleViewer.this.currentAccount).isLoadingFile(fileName)) {
                    this.buttonState = 3;
                    Float progress = ImageLoader.getInstance().getFileProgress(fileName);
                    if (progress != null) {
                        this.radialProgress.setProgress(progress.floatValue(), animated);
                    } else {
                        this.radialProgress.setProgress(0.0f, animated);
                    }
                    this.radialProgress.setBackground(getDrawableForCurrentState(), true, animated);
                } else {
                    this.buttonState = 2;
                    this.radialProgress.setProgress(0.0f, animated);
                    this.radialProgress.setBackground(getDrawableForCurrentState(), false, animated);
                }
            }
            updatePlayingMessageProgress();
        }

        private void didPressedButton(boolean animated) {
            if (this.buttonState == 0) {
                if (MediaController.getInstance().setPlaylist(ArticleViewer.this.audioMessages, this.currentMessageObject, false)) {
                    this.buttonState = 1;
                    this.radialProgress.setBackground(getDrawableForCurrentState(), false, false);
                    invalidate();
                }
            } else if (this.buttonState == 1) {
                if (MediaController.getInstance().pauseMessage(this.currentMessageObject)) {
                    this.buttonState = 0;
                    this.radialProgress.setBackground(getDrawableForCurrentState(), false, false);
                    invalidate();
                }
            } else if (this.buttonState == 2) {
                this.radialProgress.setProgress(0.0f, false);
                FileLoader.getInstance(ArticleViewer.this.currentAccount).loadFile(this.currentDocument, true, 1);
                this.buttonState = 3;
                this.radialProgress.setBackground(getDrawableForCurrentState(), true, false);
                invalidate();
            } else if (this.buttonState == 3) {
                FileLoader.getInstance(ArticleViewer.this.currentAccount).cancelLoadFile(this.currentDocument);
                this.buttonState = 2;
                this.radialProgress.setBackground(getDrawableForCurrentState(), false, false);
                invalidate();
            }
        }

        public void onFailedDownload(String fileName) {
            updateButtonState(true);
        }

        public void onSuccessDownload(String fileName) {
            this.radialProgress.setProgress(1.0f, true);
            updateButtonState(true);
        }

        public void onProgressUpload(String fileName, float progress, boolean isEncrypted) {
        }

        public void onProgressDownload(String fileName, float progress) {
            this.radialProgress.setProgress(progress, true);
            if (this.buttonState != 3) {
                updateButtonState(false);
            }
        }

        public int getObserverTag() {
            return this.TAG;
        }
    }

    private class BlockVideoCell extends FrameLayout implements FileDownloadProgressListener {
        private int TAG;
        private int buttonPressed;
        private int buttonState;
        private int buttonX;
        private int buttonY;
        private boolean cancelLoading;
        private BlockChannelCell channelCell;
        private TL_pageBlockVideo currentBlock;
        private Document currentDocument;
        private int currentType;
        private ImageReceiver imageView = new ImageReceiver(this);
        private boolean isFirst;
        private boolean isGif;
        private boolean isLast;
        private int lastCreatedWidth;
        private PageBlock parentBlock;
        private boolean photoPressed;
        private RadialProgress radialProgress;
        private StaticLayout textLayout;
        private int textX;
        private int textY;

        public BlockVideoCell(Context context, int type) {
            super(context);
            setWillNotDraw(false);
            this.currentType = type;
            this.radialProgress = new RadialProgress(this);
            this.radialProgress.setAlphaForPrevious(true);
            this.radialProgress.setProgressColor(-1);
            this.TAG = DownloadController.getInstance(ArticleViewer.this.currentAccount).generateObserverTag();
            this.channelCell = new BlockChannelCell(context, 1);
            addView(this.channelCell, LayoutHelper.createFrame(-1, -2.0f));
        }

        public void setBlock(TL_pageBlockVideo block, boolean first, boolean last) {
            this.currentBlock = block;
            this.parentBlock = null;
            this.cancelLoading = false;
            this.currentDocument = ArticleViewer.this.getDocumentWithId(this.currentBlock.video_id);
            this.isGif = MessageObject.isGifDocument(this.currentDocument);
            this.lastCreatedWidth = 0;
            this.isFirst = first;
            this.isLast = last;
            this.channelCell.setVisibility(4);
            updateButtonState(false);
            requestLayout();
        }

        public void setParentBlock(PageBlock block) {
            this.parentBlock = block;
            if (ArticleViewer.this.channelBlock != null && (this.parentBlock instanceof TL_pageBlockCover)) {
                this.channelCell.setBlock(ArticleViewer.this.channelBlock);
                this.channelCell.setVisibility(0);
            }
        }

        public View getChannelCell() {
            return this.channelCell;
        }

        public boolean onTouchEvent(MotionEvent event) {
            float x = event.getX();
            float y = event.getY();
            boolean z = false;
            if (this.channelCell.getVisibility() != 0 || y <= this.channelCell.getTranslationY() || y >= this.channelCell.getTranslationY() + ((float) AndroidUtilities.dp(39.0f))) {
                if (event.getAction() == 0 && this.imageView.isInsideImage(x, y)) {
                    if ((this.buttonState == -1 || x < ((float) this.buttonX) || x > ((float) (this.buttonX + AndroidUtilities.dp(48.0f))) || y < ((float) this.buttonY) || y > ((float) (this.buttonY + AndroidUtilities.dp(48.0f)))) && this.buttonState != 0) {
                        this.photoPressed = true;
                    } else {
                        this.buttonPressed = 1;
                        invalidate();
                    }
                } else if (event.getAction() == 1) {
                    if (this.photoPressed) {
                        this.photoPressed = false;
                        ArticleViewer.this.openPhoto(this.currentBlock);
                    } else if (this.buttonPressed == 1) {
                        this.buttonPressed = 0;
                        playSoundEffect(0);
                        didPressedButton(false);
                        this.radialProgress.swapBackground(getDrawableForCurrentState());
                        invalidate();
                    }
                } else if (event.getAction() == 3) {
                    this.photoPressed = false;
                }
                if (!(this.photoPressed || this.buttonPressed != 0 || ArticleViewer.this.checkLayoutForLinks(event, this, this.textLayout, this.textX, this.textY))) {
                    if (!super.onTouchEvent(event)) {
                        return z;
                    }
                }
                z = true;
                return z;
            }
            if (ArticleViewer.this.channelBlock != null && event.getAction() == 1) {
                MessagesController.getInstance(ArticleViewer.this.currentAccount).openByUserName(ArticleViewer.this.channelBlock.channel.username, ArticleViewer.this.parentFragment, 2);
                ArticleViewer.this.close(false, true);
            }
            return true;
        }

        protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
            int width = MeasureSpec.getSize(widthMeasureSpec);
            int height = 0;
            if (this.currentType == 1) {
                width = ArticleViewer.this.listView.getWidth();
                height = ((View) getParent()).getMeasuredHeight();
            } else if (r0.currentType == 2) {
                height = width;
            }
            if (r0.currentBlock != null) {
                int photoX;
                int textWidth;
                int photoWidth = width;
                if (r0.currentType != 0 || r0.currentBlock.level <= 0) {
                    photoX = 0;
                    r0.textX = AndroidUtilities.dp(18.0f);
                    textWidth = width - AndroidUtilities.dp(36.0f);
                } else {
                    textWidth = AndroidUtilities.dp((float) (14 * r0.currentBlock.level)) + AndroidUtilities.dp(18.0f);
                    photoX = textWidth;
                    r0.textX = textWidth;
                    photoWidth -= AndroidUtilities.dp(18.0f) + photoX;
                    textWidth = photoWidth;
                }
                if (r0.currentDocument != null) {
                    int maxHeight;
                    int size;
                    PhotoSize thumb = r0.currentDocument.thumb;
                    if (r0.currentType == 0) {
                        height = (int) (((float) thumb.h) * (((float) photoWidth) / ((float) thumb.w)));
                        if (r0.parentBlock instanceof TL_pageBlockCover) {
                            height = Math.min(height, photoWidth);
                        } else {
                            maxHeight = (int) (((float) (Math.max(ArticleViewer.this.listView.getMeasuredWidth(), ArticleViewer.this.listView.getMeasuredHeight()) - AndroidUtilities.dp(56.0f))) * 1063675494);
                            if (height > maxHeight) {
                                height = maxHeight;
                                photoWidth = (int) (((float) thumb.w) * (((float) height) / ((float) thumb.h)));
                                photoX += ((width - photoX) - photoWidth) / 2;
                            }
                        }
                    }
                    ImageReceiver imageReceiver = r0.imageView;
                    if (!(r0.isFirst || r0.currentType == 1 || r0.currentType == 2)) {
                        if (r0.currentBlock.level <= 0) {
                            maxHeight = AndroidUtilities.dp(8.0f);
                            imageReceiver.setImageCoords(photoX, maxHeight, photoWidth, height);
                            if (r0.isGif) {
                                r0.imageView.setImage(null, null, thumb == null ? thumb.location : null, thumb == null ? "80_80_b" : null, 0, null, 1);
                            } else {
                                r0.imageView.setImage(r0.currentDocument, String.format(Locale.US, "%d_%d", new Object[]{Integer.valueOf(photoWidth), Integer.valueOf(height)}), thumb == null ? thumb.location : null, thumb == null ? "80_80_b" : null, r0.currentDocument.size, null, 1);
                            }
                            size = AndroidUtilities.dp(1111490560);
                            r0.buttonX = (int) (((float) r0.imageView.getImageX()) + (((float) (r0.imageView.getImageWidth() - size)) / 2.0f));
                            r0.buttonY = (int) (((float) r0.imageView.getImageY()) + (((float) (r0.imageView.getImageHeight() - size)) / 2.0f));
                            r0.radialProgress.setProgressRect(r0.buttonX, r0.buttonY, r0.buttonX + size, r0.buttonY + size);
                        }
                    }
                    maxHeight = 0;
                    imageReceiver.setImageCoords(photoX, maxHeight, photoWidth, height);
                    if (r0.isGif) {
                        if (thumb == null) {
                        }
                        if (thumb == null) {
                        }
                        r0.imageView.setImage(null, null, thumb == null ? thumb.location : null, thumb == null ? "80_80_b" : null, 0, null, 1);
                    } else {
                        if (thumb == null) {
                        }
                        if (thumb == null) {
                        }
                        r0.imageView.setImage(r0.currentDocument, String.format(Locale.US, "%d_%d", new Object[]{Integer.valueOf(photoWidth), Integer.valueOf(height)}), thumb == null ? thumb.location : null, thumb == null ? "80_80_b" : null, r0.currentDocument.size, null, 1);
                    }
                    size = AndroidUtilities.dp(1111490560);
                    r0.buttonX = (int) (((float) r0.imageView.getImageX()) + (((float) (r0.imageView.getImageWidth() - size)) / 2.0f));
                    r0.buttonY = (int) (((float) r0.imageView.getImageY()) + (((float) (r0.imageView.getImageHeight() - size)) / 2.0f));
                    r0.radialProgress.setProgressRect(r0.buttonX, r0.buttonY, r0.buttonX + size, r0.buttonY + size);
                }
                if (r0.currentType == 0 && r0.lastCreatedWidth != width) {
                    r0.textLayout = ArticleViewer.this.createLayoutForText(null, r0.currentBlock.caption, textWidth, r0.currentBlock);
                    if (r0.textLayout != null) {
                        height += AndroidUtilities.dp(8.0f) + r0.textLayout.getHeight();
                    }
                }
                if (!r0.isFirst && r0.currentType == 0 && r0.currentBlock.level <= 0) {
                    height += AndroidUtilities.dp(8.0f);
                }
                boolean z = (r0.parentBlock instanceof TL_pageBlockCover) && ArticleViewer.this.blocks != null && ArticleViewer.this.blocks.size() > 1 && (ArticleViewer.this.blocks.get(1) instanceof TL_pageBlockChannel);
                boolean nextIsChannel = z;
                if (!(r0.currentType == 2 || nextIsChannel)) {
                    height += AndroidUtilities.dp(8.0f);
                }
            } else {
                height = 1;
            }
            r0.channelCell.measure(widthMeasureSpec, heightMeasureSpec);
            r0.channelCell.setTranslationY((float) (r0.imageView.getImageHeight() - AndroidUtilities.dp(39.0f)));
            setMeasuredDimension(width, height);
        }

        protected void onDraw(Canvas canvas) {
            if (this.currentBlock != null) {
                this.imageView.draw(canvas);
                if (this.imageView.getVisible()) {
                    this.radialProgress.draw(canvas);
                }
                if (this.textLayout != null) {
                    canvas.save();
                    float f = (float) this.textX;
                    int imageY = (this.imageView.getImageY() + this.imageView.getImageHeight()) + AndroidUtilities.dp(8.0f);
                    this.textY = imageY;
                    canvas.translate(f, (float) imageY);
                    ArticleViewer.this.drawLayoutLink(canvas, this.textLayout);
                    this.textLayout.draw(canvas);
                    canvas.restore();
                }
                if (this.currentBlock.level > 0) {
                    canvas.drawRect((float) AndroidUtilities.dp(18.0f), 0.0f, (float) AndroidUtilities.dp(20.0f), (float) (getMeasuredHeight() - (this.currentBlock.bottom ? AndroidUtilities.dp(6.0f) : 0)), ArticleViewer.quoteLinePaint);
                }
            }
        }

        private Drawable getDrawableForCurrentState() {
            if (this.buttonState < 0 || this.buttonState >= 4) {
                return null;
            }
            return Theme.chat_photoStatesDrawables[this.buttonState][this.buttonPressed];
        }

        public void updateButtonState(boolean animated) {
            String fileName = FileLoader.getAttachFileName(this.currentDocument);
            boolean fileExists = FileLoader.getPathToAttach(this.currentDocument, true).exists();
            if (TextUtils.isEmpty(fileName)) {
                this.radialProgress.setBackground(null, false, false);
                return;
            }
            if (fileExists) {
                DownloadController.getInstance(ArticleViewer.this.currentAccount).removeLoadingFileObserver(this);
                if (this.isGif) {
                    this.buttonState = -1;
                } else {
                    this.buttonState = 3;
                }
                this.radialProgress.setBackground(getDrawableForCurrentState(), false, animated);
                invalidate();
            } else {
                DownloadController.getInstance(ArticleViewer.this.currentAccount).addLoadingFileObserver(fileName, null, this);
                float setProgress = 0.0f;
                boolean progressVisible = false;
                if (FileLoader.getInstance(ArticleViewer.this.currentAccount).isLoadingFile(fileName)) {
                    progressVisible = true;
                    this.buttonState = 1;
                    Float progress = ImageLoader.getInstance().getFileProgress(fileName);
                    setProgress = progress != null ? progress.floatValue() : 0.0f;
                } else if (this.cancelLoading || !this.isGif) {
                    this.buttonState = 0;
                } else {
                    progressVisible = true;
                    this.buttonState = 1;
                }
                this.radialProgress.setBackground(getDrawableForCurrentState(), progressVisible, animated);
                this.radialProgress.setProgress(setProgress, false);
                invalidate();
            }
        }

        private void didPressedButton(boolean animated) {
            if (this.buttonState == 0) {
                this.cancelLoading = false;
                this.radialProgress.setProgress(0.0f, false);
                if (this.isGif) {
                    this.imageView.setImage(this.currentDocument, null, this.currentDocument.thumb != null ? this.currentDocument.thumb.location : null, "80_80_b", this.currentDocument.size, null, 1);
                } else {
                    FileLoader.getInstance(ArticleViewer.this.currentAccount).loadFile(this.currentDocument, true, 1);
                }
                this.buttonState = 1;
                this.radialProgress.setBackground(getDrawableForCurrentState(), true, animated);
                invalidate();
            } else if (this.buttonState == 1) {
                this.cancelLoading = true;
                if (this.isGif) {
                    this.imageView.cancelLoadImage();
                } else {
                    FileLoader.getInstance(ArticleViewer.this.currentAccount).cancelLoadFile(this.currentDocument);
                }
                this.buttonState = 0;
                this.radialProgress.setBackground(getDrawableForCurrentState(), false, animated);
                invalidate();
            } else if (this.buttonState == 2) {
                this.imageView.setAllowStartAnimation(true);
                this.imageView.startAnimation();
                this.buttonState = -1;
                this.radialProgress.setBackground(getDrawableForCurrentState(), false, animated);
            } else if (this.buttonState == 3) {
                ArticleViewer.this.openPhoto(this.currentBlock);
            }
        }

        public void onFailedDownload(String fileName) {
            updateButtonState(false);
        }

        public void onSuccessDownload(String fileName) {
            this.radialProgress.setProgress(1.0f, true);
            if (this.isGif) {
                this.buttonState = 2;
                didPressedButton(true);
                return;
            }
            updateButtonState(true);
        }

        public void onProgressUpload(String fileName, float progress, boolean isEncrypted) {
        }

        public void onProgressDownload(String fileName, float progress) {
            this.radialProgress.setProgress(progress, true);
            if (this.buttonState != 1) {
                updateButtonState(false);
            }
        }

        public int getObserverTag() {
            return this.TAG;
        }
    }

    private class WebpageAdapter extends SelectionAdapter {
        private Context context;

        public WebpageAdapter(Context ctx) {
            this.context = ctx;
        }

        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view;
            switch (viewType) {
                case 0:
                    view = new BlockParagraphCell(this.context);
                    break;
                case 1:
                    view = new BlockHeaderCell(this.context);
                    break;
                case 2:
                    view = new BlockDividerCell(this.context);
                    break;
                case 3:
                    view = new BlockEmbedCell(this.context);
                    break;
                case 4:
                    view = new BlockSubtitleCell(this.context);
                    break;
                case 5:
                    view = new BlockVideoCell(this.context, 0);
                    break;
                case 6:
                    view = new BlockPullquoteCell(this.context);
                    break;
                case 7:
                    view = new BlockBlockquoteCell(this.context);
                    break;
                case 8:
                    view = new BlockSlideshowCell(this.context);
                    break;
                case 9:
                    view = new BlockPhotoCell(this.context, 0);
                    break;
                case 10:
                    view = new BlockAuthorDateCell(this.context);
                    break;
                case 11:
                    view = new BlockTitleCell(this.context);
                    break;
                case 12:
                    view = new BlockListCell(this.context);
                    break;
                case 13:
                    view = new BlockFooterCell(this.context);
                    break;
                case 14:
                    view = new BlockPreformattedCell(this.context);
                    break;
                case 15:
                    view = new BlockSubheaderCell(this.context);
                    break;
                case 16:
                    view = new BlockEmbedPostCell(this.context);
                    break;
                case 17:
                    view = new BlockCollageCell(this.context);
                    break;
                case 18:
                    view = new BlockChannelCell(this.context, 0);
                    break;
                case 19:
                    view = new BlockAudioCell(this.context);
                    break;
                default:
                    view = new FrameLayout(this.context) {
                        protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
                            super.onMeasure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(44.0f), 1073741824));
                        }
                    };
                    view.setTag(Integer.valueOf(90));
                    TextView textView = new TextView(this.context);
                    view.addView(textView, LayoutHelper.createFrame(-1, 34.0f, 51, 0.0f, 10.0f, 0.0f, 0.0f));
                    textView.setText(LocaleController.getString("PreviewFeedback", R.string.PreviewFeedback));
                    textView.setTextSize(1, 12.0f);
                    textView.setGravity(17);
                    break;
            }
            view.setLayoutParams(new RecyclerView.LayoutParams(-1, -2));
            return new Holder(view);
        }

        public boolean isEnabled(ViewHolder holder) {
            return false;
        }

        public void onBindViewHolder(ViewHolder holder, int position) {
            boolean z = false;
            if (position < ArticleViewer.this.blocks.size()) {
                PageBlock block = (PageBlock) ArticleViewer.this.blocks.get(position);
                PageBlock originalBlock = block;
                if (block instanceof TL_pageBlockCover) {
                    block = block.cover;
                }
                boolean z2;
                switch (holder.getItemViewType()) {
                    case 0:
                        holder.itemView.setBlock((TL_pageBlockParagraph) block);
                        break;
                    case 1:
                        holder.itemView.setBlock((TL_pageBlockHeader) block);
                        break;
                    case 2:
                        BlockDividerCell cell = holder.itemView;
                        break;
                    case 3:
                        holder.itemView.setBlock((TL_pageBlockEmbed) block);
                        break;
                    case 4:
                        holder.itemView.setBlock((TL_pageBlockSubtitle) block);
                        break;
                    case 5:
                        BlockVideoCell cell2 = holder.itemView;
                        TL_pageBlockVideo tL_pageBlockVideo = (TL_pageBlockVideo) block;
                        z2 = position == 0;
                        if (position == ArticleViewer.this.blocks.size() - 1) {
                            z = true;
                        }
                        cell2.setBlock(tL_pageBlockVideo, z2, z);
                        cell2.setParentBlock(originalBlock);
                        break;
                    case 6:
                        holder.itemView.setBlock((TL_pageBlockPullquote) block);
                        break;
                    case 7:
                        holder.itemView.setBlock((TL_pageBlockBlockquote) block);
                        break;
                    case 8:
                        holder.itemView.setBlock((TL_pageBlockSlideshow) block);
                        break;
                    case 9:
                        BlockPhotoCell cell3 = holder.itemView;
                        TL_pageBlockPhoto tL_pageBlockPhoto = (TL_pageBlockPhoto) block;
                        z2 = position == 0;
                        if (position == ArticleViewer.this.blocks.size() - 1) {
                            z = true;
                        }
                        cell3.setBlock(tL_pageBlockPhoto, z2, z);
                        cell3.setParentBlock(originalBlock);
                        break;
                    case 10:
                        holder.itemView.setBlock((TL_pageBlockAuthorDate) block);
                        break;
                    case 11:
                        holder.itemView.setBlock((TL_pageBlockTitle) block);
                        break;
                    case 12:
                        holder.itemView.setBlock((TL_pageBlockList) block);
                        break;
                    case 13:
                        holder.itemView.setBlock((TL_pageBlockFooter) block);
                        break;
                    case 14:
                        holder.itemView.setBlock((TL_pageBlockPreformatted) block);
                        break;
                    case 15:
                        holder.itemView.setBlock((TL_pageBlockSubheader) block);
                        break;
                    case 16:
                        holder.itemView.setBlock((TL_pageBlockEmbedPost) block);
                        break;
                    case 17:
                        holder.itemView.setBlock((TL_pageBlockCollage) block);
                        break;
                    case 18:
                        holder.itemView.setBlock((TL_pageBlockChannel) block);
                        break;
                    case 19:
                        BlockAudioCell cell4 = holder.itemView;
                        TL_pageBlockAudio tL_pageBlockAudio = (TL_pageBlockAudio) block;
                        z2 = position == 0;
                        if (position == ArticleViewer.this.blocks.size() - 1) {
                            z = true;
                        }
                        cell4.setBlock(tL_pageBlockAudio, z2, z);
                        break;
                    default:
                        break;
                }
            } else if (holder.getItemViewType() == 90) {
                TextView textView = (TextView) ((ViewGroup) holder.itemView).getChildAt(0);
                int color = ArticleViewer.this.getSelectedColor();
                if (color == 0) {
                    textView.setTextColor(-8879475);
                    textView.setBackgroundColor(-1183760);
                } else if (color == 1) {
                    textView.setTextColor(ArticleViewer.this.getGrayTextColor());
                    textView.setBackgroundColor(-1712440);
                } else if (color == 2) {
                    textView.setTextColor(ArticleViewer.this.getGrayTextColor());
                    textView.setBackgroundColor(-14277082);
                }
            }
        }

        private int getTypeForBlock(PageBlock block) {
            if (block instanceof TL_pageBlockParagraph) {
                return 0;
            }
            if (block instanceof TL_pageBlockHeader) {
                return 1;
            }
            if (block instanceof TL_pageBlockDivider) {
                return 2;
            }
            if (block instanceof TL_pageBlockEmbed) {
                return 3;
            }
            if (block instanceof TL_pageBlockSubtitle) {
                return 4;
            }
            if (block instanceof TL_pageBlockVideo) {
                return 5;
            }
            if (block instanceof TL_pageBlockPullquote) {
                return 6;
            }
            if (block instanceof TL_pageBlockBlockquote) {
                return 7;
            }
            if (block instanceof TL_pageBlockSlideshow) {
                return 8;
            }
            if (block instanceof TL_pageBlockPhoto) {
                return 9;
            }
            if (block instanceof TL_pageBlockAuthorDate) {
                return 10;
            }
            if (block instanceof TL_pageBlockTitle) {
                return 11;
            }
            if (block instanceof TL_pageBlockList) {
                return 12;
            }
            if (block instanceof TL_pageBlockFooter) {
                return 13;
            }
            if (block instanceof TL_pageBlockPreformatted) {
                return 14;
            }
            if (block instanceof TL_pageBlockSubheader) {
                return 15;
            }
            if (block instanceof TL_pageBlockEmbedPost) {
                return 16;
            }
            if (block instanceof TL_pageBlockCollage) {
                return 17;
            }
            if (block instanceof TL_pageBlockChannel) {
                return 18;
            }
            if (block instanceof TL_pageBlockAudio) {
                return 19;
            }
            if (block instanceof TL_pageBlockCover) {
                return getTypeForBlock(block.cover);
            }
            return 0;
        }

        public int getItemViewType(int position) {
            if (position == ArticleViewer.this.blocks.size()) {
                return 90;
            }
            return getTypeForBlock((PageBlock) ArticleViewer.this.blocks.get(position));
        }

        public int getItemCount() {
            return (ArticleViewer.this.currentPage == null || ArticleViewer.this.currentPage.cached_page == null) ? 0 : ArticleViewer.this.blocks.size() + 1;
        }
    }

    private boolean open(org.telegram.messenger.MessageObject r1, org.telegram.tgnet.TLRPC.WebPage r2, java.lang.String r3, boolean r4) {
        /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.DecodeException: Load method exception in method: org.telegram.ui.ArticleViewer.open(org.telegram.messenger.MessageObject, org.telegram.tgnet.TLRPC$WebPage, java.lang.String, boolean):boolean
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
        r1 = r18;
        r2 = r19;
        r3 = r21;
        r4 = r1.parentActivity;
        r5 = 0;
        if (r4 == 0) goto L_0x0217;
    L_0x000b:
        r4 = r1.isVisible;
        if (r4 == 0) goto L_0x0013;
    L_0x000f:
        r4 = r1.collapsed;
        if (r4 == 0) goto L_0x0217;
    L_0x0013:
        if (r2 != 0) goto L_0x0019;
    L_0x0015:
        if (r20 != 0) goto L_0x0019;
    L_0x0017:
        goto L_0x0217;
    L_0x0019:
        if (r2 == 0) goto L_0x0022;
    L_0x001b:
        r6 = r2.messageOwner;
        r6 = r6.media;
        r4 = r6.webpage;
        goto L_0x0024;
    L_0x0022:
        r4 = r20;
    L_0x0024:
        if (r22 == 0) goto L_0x004b;
    L_0x0026:
        r7 = new org.telegram.tgnet.TLRPC$TL_messages_getWebPage;
        r7.<init>();
        r8 = r4.url;
        r7.url = r8;
        r8 = r4.cached_page;
        r8 = r8 instanceof org.telegram.tgnet.TLRPC.TL_pagePart;
        if (r8 == 0) goto L_0x0038;
    L_0x0035:
        r7.hash = r5;
        goto L_0x003c;
    L_0x0038:
        r8 = r4.hash;
        r7.hash = r8;
    L_0x003c:
        r8 = r4;
        r9 = org.telegram.messenger.UserConfig.selectedAccount;
        r10 = org.telegram.tgnet.ConnectionsManager.getInstance(r9);
        r11 = new org.telegram.ui.ArticleViewer$24;
        r11.<init>(r8, r2, r9);
        r10.sendRequest(r7, r11);
    L_0x004b:
        r7 = r1.pagesStack;
        r7.clear();
        r1.collapsed = r5;
        r7 = r1.backDrawable;
        r8 = 0;
        r7.setRotation(r8, r5);
        r7 = r1.containerView;
        r7.setTranslationX(r8);
        r7 = r1.containerView;
        r7.setTranslationY(r8);
        r7 = r1.listView;
        r7.setTranslationY(r8);
        r7 = r1.listView;
        r9 = 1065353216; // 0x3f800000 float:1.0 double:5.263544247E-315;
        r7.setAlpha(r9);
        r7 = r1.windowView;
        r7.setInnerTranslationX(r8);
        r7 = r1.actionBar;
        r9 = 8;
        r7.setVisibility(r9);
        r7 = r1.bottomLayout;
        r7.setVisibility(r9);
        r7 = r1.captionTextViewNew;
        r7.setVisibility(r9);
        r7 = r1.captionTextViewOld;
        r7.setVisibility(r9);
        r7 = r1.shareContainer;
        r7.setAlpha(r8);
        r7 = r1.backButton;
        r7.setAlpha(r8);
        r7 = r1.settingsButton;
        r7.setAlpha(r8);
        r7 = r1.layoutManager;
        r7.scrollToPositionWithOffset(r5, r5);
        r7 = 1113587712; // 0x42600000 float:56.0 double:5.50185432E-315;
        r9 = org.telegram.messenger.AndroidUtilities.dp(r7);
        r9 = -r9;
        r1.checkScroll(r9);
        r9 = 0;
        r10 = -1;
        r11 = 35;
        if (r2 == 0) goto L_0x0113;
    L_0x00ad:
        r12 = r2.messageOwner;
        r12 = r12.media;
        r4 = r12.webpage;
        r12 = r4.url;
        r12 = r12.toLowerCase();
        r13 = r3;
        r3 = r5;
        r14 = r2.messageOwner;
        r14 = r14.entities;
        r14 = r14.size();
        if (r3 >= r14) goto L_0x0110;
    L_0x00c5:
        r14 = r2.messageOwner;
        r14 = r14.entities;
        r14 = r14.get(r3);
        r14 = (org.telegram.tgnet.TLRPC.MessageEntity) r14;
        r15 = r14 instanceof org.telegram.tgnet.TLRPC.TL_messageEntityUrl;
        if (r15 == 0) goto L_0x0109;
        r15 = r2.messageOwner;	 Catch:{ Exception -> 0x0104 }
        r15 = r15.message;	 Catch:{ Exception -> 0x0104 }
        r7 = r14.offset;	 Catch:{ Exception -> 0x0104 }
        r8 = r14.offset;	 Catch:{ Exception -> 0x0104 }
        r5 = r14.length;	 Catch:{ Exception -> 0x0104 }
        r8 = r8 + r5;	 Catch:{ Exception -> 0x0104 }
        r5 = r15.substring(r7, r8);	 Catch:{ Exception -> 0x0104 }
        r5 = r5.toLowerCase();	 Catch:{ Exception -> 0x0104 }
        r13 = r5;	 Catch:{ Exception -> 0x0104 }
        r5 = r13.contains(r12);	 Catch:{ Exception -> 0x0104 }
        if (r5 != 0) goto L_0x00f5;	 Catch:{ Exception -> 0x0104 }
        r5 = r12.contains(r13);	 Catch:{ Exception -> 0x0104 }
        if (r5 == 0) goto L_0x00f4;	 Catch:{ Exception -> 0x0104 }
        goto L_0x00f5;	 Catch:{ Exception -> 0x0104 }
        goto L_0x0109;	 Catch:{ Exception -> 0x0104 }
        r5 = r13.lastIndexOf(r11);	 Catch:{ Exception -> 0x0104 }
        r7 = r5;	 Catch:{ Exception -> 0x0104 }
        if (r5 == r10) goto L_0x0103;	 Catch:{ Exception -> 0x0104 }
        r5 = r7 + 1;	 Catch:{ Exception -> 0x0104 }
        r5 = r13.substring(r5);	 Catch:{ Exception -> 0x0104 }
        r9 = r5;
        goto L_0x0110;
    L_0x0104:
        r0 = move-exception;
        r5 = r0;
        org.telegram.messenger.FileLog.e(r5);
        r3 = r3 + 1;
        r5 = 0;
        r7 = 1113587712; // 0x42600000 float:56.0 double:5.50185432E-315;
        r8 = 0;
        goto L_0x00bb;
        r3 = r13;
        goto L_0x0122;
    L_0x0113:
        if (r3 == 0) goto L_0x0122;
        r5 = r3.lastIndexOf(r11);
        r7 = r5;
        if (r5 == r10) goto L_0x0122;
        r5 = r7 + 1;
        r9 = r3.substring(r5);
        r1.addPageToStack(r4, r9);
        r5 = 0;
        r1.lastInsets = r5;
        r7 = r1.isVisible;
        if (r7 != 0) goto L_0x0171;
        r7 = r1.parentActivity;
        r8 = "window";
        r7 = r7.getSystemService(r8);
        r7 = (android.view.WindowManager) r7;
        r8 = r1.attachedToWindow;
        if (r8 == 0) goto L_0x0141;
        r8 = r1.windowView;	 Catch:{ Exception -> 0x0140 }
        r7.removeView(r8);	 Catch:{ Exception -> 0x0140 }
        goto L_0x0141;
    L_0x0140:
        r0 = move-exception;
        r8 = android.os.Build.VERSION.SDK_INT;	 Catch:{ Exception -> 0x016a }
        r10 = 21;	 Catch:{ Exception -> 0x016a }
        if (r8 < r10) goto L_0x014e;	 Catch:{ Exception -> 0x016a }
        r8 = r1.windowLayoutParams;	 Catch:{ Exception -> 0x016a }
        r10 = -2147417856; // 0xffffffff80010100 float:-9.2194E-41 double:NaN;	 Catch:{ Exception -> 0x016a }
        r8.flags = r10;	 Catch:{ Exception -> 0x016a }
        r8 = r1.windowLayoutParams;	 Catch:{ Exception -> 0x016a }
        r10 = r8.flags;	 Catch:{ Exception -> 0x016a }
        r10 = r10 | 1032;	 Catch:{ Exception -> 0x016a }
        r8.flags = r10;	 Catch:{ Exception -> 0x016a }
        r8 = r1.windowView;	 Catch:{ Exception -> 0x016a }
        r10 = 0;	 Catch:{ Exception -> 0x016a }
        r8.setFocusable(r10);	 Catch:{ Exception -> 0x016a }
        r8 = r1.containerView;	 Catch:{ Exception -> 0x016a }
        r8.setFocusable(r10);	 Catch:{ Exception -> 0x016a }
        r8 = r1.windowView;	 Catch:{ Exception -> 0x016a }
        r10 = r1.windowLayoutParams;	 Catch:{ Exception -> 0x016a }
        r7.addView(r8, r10);	 Catch:{ Exception -> 0x016a }
        goto L_0x018a;
    L_0x016a:
        r0 = move-exception;
        r5 = r0;
        org.telegram.messenger.FileLog.e(r5);
        r8 = 0;
        return r8;
        r7 = r1.windowLayoutParams;
        r8 = r7.flags;
        r8 = r8 & -17;
        r7.flags = r8;
        r7 = r1.parentActivity;
        r8 = "window";
        r7 = r7.getSystemService(r8);
        r7 = (android.view.WindowManager) r7;
        r8 = r1.windowView;
        r10 = r1.windowLayoutParams;
        r7.updateViewLayout(r8, r10);
        r7 = 1;
        r1.isVisible = r7;
        r1.animationInProgress = r7;
        r8 = r1.windowView;
        r10 = 0;
        r8.setAlpha(r10);
        r8 = r1.containerView;
        r8.setAlpha(r10);
        r8 = new android.animation.AnimatorSet;
        r8.<init>();
        r10 = 3;
        r10 = new android.animation.Animator[r10];
        r11 = r1.windowView;
        r12 = "alpha";
        r13 = 2;
        r14 = new float[r13];
        r14 = {0, 1065353216};
        r11 = android.animation.ObjectAnimator.ofFloat(r11, r12, r14);
        r12 = 0;
        r10[r12] = r11;
        r11 = r1.containerView;
        r12 = "alpha";
        r14 = new float[r13];
        r14 = {0, 1065353216};
        r11 = android.animation.ObjectAnimator.ofFloat(r11, r12, r14);
        r10[r7] = r11;
        r11 = r1.windowView;
        r12 = "translationX";
        r14 = new float[r13];
        r15 = 1113587712; // 0x42600000 float:56.0 double:5.50185432E-315;
        r15 = org.telegram.messenger.AndroidUtilities.dp(r15);
        r15 = (float) r15;
        r16 = 0;
        r14[r16] = r15;
        r15 = 0;
        r14[r7] = r15;
        r11 = android.animation.ObjectAnimator.ofFloat(r11, r12, r14);
        r10[r13] = r11;
        r8.playTogether(r10);
        r10 = new org.telegram.ui.ArticleViewer$25;
        r10.<init>();
        r1.animationEndRunnable = r10;
        r10 = 150; // 0x96 float:2.1E-43 double:7.4E-322;
        r8.setDuration(r10);
        r10 = r1.interpolator;
        r8.setInterpolator(r10);
        r10 = new org.telegram.ui.ArticleViewer$26;
        r10.<init>();
        r8.addListener(r10);
        r10 = java.lang.System.currentTimeMillis();
        r1.transitionAnimationStartTime = r10;
        r10 = new org.telegram.ui.ArticleViewer$27;
        r10.<init>(r8);
        org.telegram.messenger.AndroidUtilities.runOnUIThread(r10);
        r10 = android.os.Build.VERSION.SDK_INT;
        r11 = 18;
        if (r10 < r11) goto L_0x0211;
        r10 = r1.containerView;
        r10.setLayerType(r13, r5);
        r5 = 200; // 0xc8 float:2.8E-43 double:9.9E-322;
        r1.showActionBar(r5);
        return r7;
    L_0x0217:
        r5 = 0;
        return r5;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.telegram.ui.ArticleViewer.open(org.telegram.messenger.MessageObject, org.telegram.tgnet.TLRPC$WebPage, java.lang.String, boolean):boolean");
    }

    private void updateInterfaceForCurrentPage(boolean r1) {
        /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.DecodeException: Load method exception in method: org.telegram.ui.ArticleViewer.updateInterfaceForCurrentPage(boolean):void
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
        r0 = r13.currentPage;
        if (r0 == 0) goto L_0x0294;
    L_0x0004:
        r0 = r13.currentPage;
        r0 = r0.cached_page;
        if (r0 != 0) goto L_0x000c;
    L_0x000a:
        goto L_0x0294;
    L_0x000c:
        r0 = -1;
        r13.isRtl = r0;
        r1 = 0;
        r13.channelBlock = r1;
        r2 = r13.blocks;
        r2.clear();
        r2 = r13.photoBlocks;
        r2.clear();
        r2 = r13.audioBlocks;
        r2.clear();
        r2 = r13.audioMessages;
        r2.clear();
        r2 = 0;
        r3 = r13.currentPage;
        r3 = r3.cached_page;
        r3 = r3.blocks;
        r3 = r3.size();
        r4 = 0;
        r5 = r4;
    L_0x0033:
        r6 = 1;
        if (r5 >= r3) goto L_0x020e;
    L_0x0036:
        r7 = r13.currentPage;
        r7 = r7.cached_page;
        r7 = r7.blocks;
        r7 = r7.get(r5);
        r7 = (org.telegram.tgnet.TLRPC.PageBlock) r7;
        r8 = r7 instanceof org.telegram.tgnet.TLRPC.TL_pageBlockUnsupported;
        if (r8 == 0) goto L_0x0048;
    L_0x0046:
        goto L_0x020a;
    L_0x0048:
        r8 = r7 instanceof org.telegram.tgnet.TLRPC.TL_pageBlockAnchor;
        if (r8 == 0) goto L_0x0063;
    L_0x004c:
        r6 = r13.anchors;
        r8 = r7.name;
        r8 = r8.toLowerCase();
        r9 = r13.blocks;
        r9 = r9.size();
        r9 = java.lang.Integer.valueOf(r9);
        r6.put(r8, r9);
        goto L_0x020a;
    L_0x0063:
        r8 = r7 instanceof org.telegram.tgnet.TLRPC.TL_pageBlockAudio;
        if (r8 == 0) goto L_0x00d0;
    L_0x0067:
        r8 = new org.telegram.tgnet.TLRPC$TL_message;
        r8.<init>();
        r8.out = r6;
        r9 = r13.currentPage;
        r9 = r9.id;
        r9 = (int) r9;
        r9 = r9 + r5;
        r7.mid = r9;
        r8.id = r9;
        r9 = new org.telegram.tgnet.TLRPC$TL_peerUser;
        r9.<init>();
        r8.to_id = r9;
        r9 = r8.to_id;
        r10 = r13.currentAccount;
        r10 = org.telegram.messenger.UserConfig.getInstance(r10);
        r10 = r10.getClientUserId();
        r8.from_id = r10;
        r9.user_id = r10;
        r9 = java.lang.System.currentTimeMillis();
        r11 = 1000; // 0x3e8 float:1.401E-42 double:4.94E-321;
        r9 = r9 / r11;
        r9 = (int) r9;
        r8.date = r9;
        r9 = "";
        r8.message = r9;
        r9 = new org.telegram.tgnet.TLRPC$TL_messageMediaDocument;
        r9.<init>();
        r8.media = r9;
        r9 = r8.media;
        r10 = r9.flags;
        r10 = r10 | 3;
        r9.flags = r10;
        r9 = r8.media;
        r10 = r7.audio_id;
        r10 = r13.getDocumentWithId(r10);
        r9.document = r10;
        r9 = r8.flags;
        r9 = r9 | 768;
        r8.flags = r9;
        r9 = new org.telegram.messenger.MessageObject;
        r10 = org.telegram.messenger.UserConfig.selectedAccount;
        r9.<init>(r10, r8, r4);
        r10 = r13.audioMessages;
        r10.add(r9);
        r10 = r13.audioBlocks;
        r11 = r7;
        r11 = (org.telegram.tgnet.TLRPC.TL_pageBlockAudio) r11;
        r10.put(r11, r9);
    L_0x00d0:
        r8 = r7.text;
        r13.setRichTextParents(r1, r8);
        r8 = r7.caption;
        r13.setRichTextParents(r1, r8);
        r8 = r7 instanceof org.telegram.tgnet.TLRPC.TL_pageBlockAuthorDate;
        if (r8 == 0) goto L_0x00e8;
    L_0x00de:
        r8 = r7;
        r8 = (org.telegram.tgnet.TLRPC.TL_pageBlockAuthorDate) r8;
        r8 = r8.author;
        r13.setRichTextParents(r1, r8);
        goto L_0x0162;
    L_0x00e8:
        r8 = r7 instanceof org.telegram.tgnet.TLRPC.TL_pageBlockCollage;
        if (r8 == 0) goto L_0x0116;
    L_0x00ec:
        r8 = r7;
        r8 = (org.telegram.tgnet.TLRPC.TL_pageBlockCollage) r8;
        r9 = r4;
    L_0x00f0:
        r10 = r8.items;
        r10 = r10.size();
        if (r9 >= r10) goto L_0x0115;
    L_0x00f8:
        r10 = r8.items;
        r10 = r10.get(r9);
        r10 = (org.telegram.tgnet.TLRPC.PageBlock) r10;
        r10 = r10.text;
        r13.setRichTextParents(r1, r10);
        r10 = r8.items;
        r10 = r10.get(r9);
        r10 = (org.telegram.tgnet.TLRPC.PageBlock) r10;
        r10 = r10.caption;
        r13.setRichTextParents(r1, r10);
        r9 = r9 + 1;
        goto L_0x00f0;
    L_0x0115:
        goto L_0x0162;
    L_0x0116:
        r8 = r7 instanceof org.telegram.tgnet.TLRPC.TL_pageBlockList;
        if (r8 == 0) goto L_0x0135;
    L_0x011a:
        r8 = r7;
        r8 = (org.telegram.tgnet.TLRPC.TL_pageBlockList) r8;
        r9 = r4;
    L_0x011e:
        r10 = r8.items;
        r10 = r10.size();
        if (r9 >= r10) goto L_0x0134;
    L_0x0126:
        r10 = r8.items;
        r10 = r10.get(r9);
        r10 = (org.telegram.tgnet.TLRPC.RichText) r10;
        r13.setRichTextParents(r1, r10);
        r9 = r9 + 1;
        goto L_0x011e;
    L_0x0134:
        goto L_0x0162;
    L_0x0135:
        r8 = r7 instanceof org.telegram.tgnet.TLRPC.TL_pageBlockSlideshow;
        if (r8 == 0) goto L_0x0162;
    L_0x0139:
        r8 = r7;
        r8 = (org.telegram.tgnet.TLRPC.TL_pageBlockSlideshow) r8;
        r9 = r4;
    L_0x013d:
        r10 = r8.items;
        r10 = r10.size();
        if (r9 >= r10) goto L_0x0162;
    L_0x0145:
        r10 = r8.items;
        r10 = r10.get(r9);
        r10 = (org.telegram.tgnet.TLRPC.PageBlock) r10;
        r10 = r10.text;
        r13.setRichTextParents(r1, r10);
        r10 = r8.items;
        r10 = r10.get(r9);
        r10 = (org.telegram.tgnet.TLRPC.PageBlock) r10;
        r10 = r10.caption;
        r13.setRichTextParents(r1, r10);
        r9 = r9 + 1;
        goto L_0x013d;
    L_0x0162:
        if (r5 != 0) goto L_0x0190;
    L_0x0164:
        r7.first = r6;
        r8 = r7 instanceof org.telegram.tgnet.TLRPC.TL_pageBlockCover;
        if (r8 == 0) goto L_0x0198;
    L_0x016a:
        r8 = r7.cover;
        r8 = r8.caption;
        if (r8 == 0) goto L_0x0198;
    L_0x0170:
        r8 = r7.cover;
        r8 = r8.caption;
        r8 = r8 instanceof org.telegram.tgnet.TLRPC.TL_textEmpty;
        if (r8 != 0) goto L_0x0198;
    L_0x0178:
        if (r3 <= r6) goto L_0x0198;
    L_0x017a:
        r8 = r13.currentPage;
        r8 = r8.cached_page;
        r8 = r8.blocks;
        r8 = r8.get(r6);
        r8 = (org.telegram.tgnet.TLRPC.PageBlock) r8;
        r9 = r8 instanceof org.telegram.tgnet.TLRPC.TL_pageBlockChannel;
        if (r9 == 0) goto L_0x018f;
    L_0x018a:
        r9 = r8;
        r9 = (org.telegram.tgnet.TLRPC.TL_pageBlockChannel) r9;
        r13.channelBlock = r9;
    L_0x018f:
        goto L_0x0198;
    L_0x0190:
        if (r5 != r6) goto L_0x0198;
    L_0x0192:
        r8 = r13.channelBlock;
        if (r8 == 0) goto L_0x0198;
    L_0x0196:
        goto L_0x020a;
    L_0x0198:
        r13.addAllMediaFromBlock(r7);
        r8 = r13.blocks;
        r8.add(r7);
        r8 = r7 instanceof org.telegram.tgnet.TLRPC.TL_pageBlockEmbedPost;
        if (r8 == 0) goto L_0x020a;
    L_0x01a4:
        r8 = r7.blocks;
        r8 = r8.isEmpty();
        if (r8 != 0) goto L_0x01f6;
    L_0x01ac:
        r7.level = r0;
        r8 = r4;
    L_0x01af:
        r9 = r7.blocks;
        r9 = r9.size();
        if (r8 >= r9) goto L_0x01f6;
    L_0x01b7:
        r9 = r7.blocks;
        r9 = r9.get(r8);
        r9 = (org.telegram.tgnet.TLRPC.PageBlock) r9;
        r10 = r9 instanceof org.telegram.tgnet.TLRPC.TL_pageBlockUnsupported;
        if (r10 == 0) goto L_0x01c4;
    L_0x01c3:
        goto L_0x01f3;
    L_0x01c4:
        r10 = r9 instanceof org.telegram.tgnet.TLRPC.TL_pageBlockAnchor;
        if (r10 == 0) goto L_0x01de;
    L_0x01c8:
        r10 = r13.anchors;
        r11 = r9.name;
        r11 = r11.toLowerCase();
        r12 = r13.blocks;
        r12 = r12.size();
        r12 = java.lang.Integer.valueOf(r12);
        r10.put(r11, r12);
        goto L_0x01f3;
    L_0x01de:
        r9.level = r6;
        r10 = r7.blocks;
        r10 = r10.size();
        r10 = r10 - r6;
        if (r8 != r10) goto L_0x01eb;
    L_0x01e9:
        r9.bottom = r6;
    L_0x01eb:
        r10 = r13.blocks;
        r10.add(r9);
        r13.addAllMediaFromBlock(r9);
    L_0x01f3:
        r8 = r8 + 1;
        goto L_0x01af;
    L_0x01f6:
        r6 = r7.caption;
        r6 = r6 instanceof org.telegram.tgnet.TLRPC.TL_textEmpty;
        if (r6 != 0) goto L_0x020a;
    L_0x01fc:
        r6 = new org.telegram.tgnet.TLRPC$TL_pageBlockParagraph;
        r6.<init>();
        r8 = r7.caption;
        r6.caption = r8;
        r8 = r13.blocks;
        r8.add(r6);
    L_0x020a:
        r5 = r5 + 1;
        goto L_0x0033;
    L_0x020e:
        r1 = r13.adapter;
        r1.notifyDataSetChanged();
        r1 = r13.pagesStack;
        r1 = r1.size();
        if (r1 == r6) goto L_0x0224;
    L_0x021b:
        if (r14 == 0) goto L_0x021e;
    L_0x021d:
        goto L_0x0224;
    L_0x021e:
        r0 = r13.layoutManager;
        r0.scrollToPositionWithOffset(r4, r4);
        goto L_0x0293;
    L_0x0224:
        r1 = org.telegram.messenger.ApplicationLoader.applicationContext;
        r5 = "articles";
        r1 = r1.getSharedPreferences(r5, r4);
        r5 = new java.lang.StringBuilder;
        r5.<init>();
        r7 = "article";
        r5.append(r7);
        r7 = r13.currentPage;
        r7 = r7.id;
        r5.append(r7);
        r5 = r5.toString();
        r7 = r1.getInt(r5, r0);
        r8 = new java.lang.StringBuilder;
        r8.<init>();
        r8.append(r5);
        r9 = "r";
        r8.append(r9);
        r8 = r8.toString();
        r8 = r1.getBoolean(r8, r6);
        r9 = org.telegram.messenger.AndroidUtilities.displaySize;
        r9 = r9.x;
        r10 = org.telegram.messenger.AndroidUtilities.displaySize;
        r10 = r10.y;
        if (r9 <= r10) goto L_0x0265;
    L_0x0264:
        goto L_0x0266;
    L_0x0265:
        r6 = r4;
    L_0x0266:
        if (r8 != r6) goto L_0x0285;
    L_0x0268:
        r6 = new java.lang.StringBuilder;
        r6.<init>();
        r6.append(r5);
        r8 = "o";
        r6.append(r8);
        r6 = r6.toString();
        r4 = r1.getInt(r6, r4);
        r6 = r13.listView;
        r6 = r6.getPaddingTop();
        r4 = r4 - r6;
        goto L_0x028b;
    L_0x0285:
        r4 = 1092616192; // 0x41200000 float:10.0 double:5.398241246E-315;
        r4 = org.telegram.messenger.AndroidUtilities.dp(r4);
    L_0x028b:
        if (r7 == r0) goto L_0x0292;
    L_0x028d:
        r0 = r13.layoutManager;
        r0.scrollToPositionWithOffset(r7, r4);
    L_0x0293:
        return;
    L_0x0294:
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.telegram.ui.ArticleViewer.updateInterfaceForCurrentPage(boolean):void");
    }

    private void updatePaintColors() {
        /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.DecodeException: Load method exception in method: org.telegram.ui.ArticleViewer.updatePaintColors():void
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
        r0 = org.telegram.messenger.ApplicationLoader.applicationContext;
        r1 = "articles";
        r2 = 0;
        r0 = r0.getSharedPreferences(r1, r2);
        r0 = r0.edit();
        r1 = "font_color";
        r3 = r9.selectedColor;
        r0 = r0.putInt(r1, r3);
        r0.commit();
        r0 = r9.getSelectedColor();
        r1 = -1;
        r3 = 2;
        r4 = 1;
        if (r0 != 0) goto L_0x002f;
    L_0x0021:
        r5 = r9.backgroundPaint;
        r5.setColor(r1);
        r5 = r9.listView;
        r6 = -657673; // 0xfffffffffff5f6f7 float:NaN double:NaN;
        r5.setGlowColor(r6);
        goto L_0x004e;
    L_0x002f:
        if (r0 != r4) goto L_0x003f;
    L_0x0031:
        r5 = r9.backgroundPaint;
        r6 = -659492; // 0xfffffffffff5efdc float:NaN double:NaN;
        r5.setColor(r6);
        r5 = r9.listView;
        r5.setGlowColor(r6);
        goto L_0x004e;
    L_0x003f:
        if (r0 != r3) goto L_0x004e;
    L_0x0041:
        r5 = r9.backgroundPaint;
        r6 = -15461356; // 0xffffffffff141414 float:-1.9683E38 double:NaN;
        r5.setColor(r6);
        r5 = r9.listView;
        r5.setGlowColor(r6);
    L_0x004e:
        r5 = r2;
    L_0x004f:
        r6 = org.telegram.ui.ActionBar.Theme.chat_ivStatesDrawable;
        r6 = r6.length;
        if (r5 >= r6) goto L_0x008b;
    L_0x0054:
        r6 = org.telegram.ui.ActionBar.Theme.chat_ivStatesDrawable;
        r6 = r6[r5];
        r6 = r6[r2];
        r7 = r9.getTextColor();
        org.telegram.ui.ActionBar.Theme.setCombinedDrawableColor(r6, r7, r2);
        r6 = org.telegram.ui.ActionBar.Theme.chat_ivStatesDrawable;
        r6 = r6[r5];
        r6 = r6[r2];
        r7 = r9.getTextColor();
        org.telegram.ui.ActionBar.Theme.setCombinedDrawableColor(r6, r7, r4);
        r6 = org.telegram.ui.ActionBar.Theme.chat_ivStatesDrawable;
        r6 = r6[r5];
        r6 = r6[r4];
        r7 = r9.getTextColor();
        org.telegram.ui.ActionBar.Theme.setCombinedDrawableColor(r6, r7, r2);
        r6 = org.telegram.ui.ActionBar.Theme.chat_ivStatesDrawable;
        r6 = r6[r5];
        r6 = r6[r4];
        r7 = r9.getTextColor();
        org.telegram.ui.ActionBar.Theme.setCombinedDrawableColor(r6, r7, r4);
        r5 = r5 + 1;
        goto L_0x004f;
    L_0x008b:
        r5 = quoteLinePaint;
        if (r5 == 0) goto L_0x0098;
    L_0x008f:
        r5 = quoteLinePaint;
        r6 = r9.getTextColor();
        r5.setColor(r6);
    L_0x0098:
        r5 = listTextPointerPaint;
        if (r5 == 0) goto L_0x00a5;
    L_0x009c:
        r5 = listTextPointerPaint;
        r6 = r9.getTextColor();
        r5.setColor(r6);
    L_0x00a5:
        r5 = preformattedBackgroundPaint;
        r6 = -14277082; // 0xffffffffff262626 float:-2.2084993E38 double:NaN;
        r7 = -1712440; // 0xffffffffffe5dec8 float:NaN double:NaN;
        if (r5 == 0) goto L_0x00c9;
    L_0x00af:
        if (r0 != 0) goto L_0x00ba;
    L_0x00b1:
        r5 = preformattedBackgroundPaint;
        r8 = -657156; // 0xfffffffffff5f8fc float:NaN double:NaN;
        r5.setColor(r8);
        goto L_0x00c9;
    L_0x00ba:
        if (r0 != r4) goto L_0x00c2;
    L_0x00bc:
        r5 = preformattedBackgroundPaint;
        r5.setColor(r7);
        goto L_0x00c9;
    L_0x00c2:
        if (r0 != r3) goto L_0x00c9;
    L_0x00c4:
        r5 = preformattedBackgroundPaint;
        r5.setColor(r6);
    L_0x00c9:
        r5 = urlPaint;
        if (r5 == 0) goto L_0x00e7;
    L_0x00cd:
        if (r0 != 0) goto L_0x00d8;
    L_0x00cf:
        r5 = urlPaint;
        r6 = -1315861; // 0xffffffffffebebeb float:NaN double:NaN;
        r5.setColor(r6);
        goto L_0x00e7;
    L_0x00d8:
        if (r0 != r4) goto L_0x00e0;
    L_0x00da:
        r5 = urlPaint;
        r5.setColor(r7);
        goto L_0x00e7;
    L_0x00e0:
        if (r0 != r3) goto L_0x00e7;
    L_0x00e2:
        r5 = urlPaint;
        r5.setColor(r6);
    L_0x00e7:
        r5 = embedPostAuthorPaint;
        if (r5 == 0) goto L_0x00f4;
    L_0x00eb:
        r5 = embedPostAuthorPaint;
        r6 = r9.getTextColor();
        r5.setColor(r6);
    L_0x00f4:
        r5 = channelNamePaint;
        if (r5 == 0) goto L_0x010b;
    L_0x00f8:
        r5 = r9.channelBlock;
        if (r5 != 0) goto L_0x0106;
    L_0x00fc:
        r1 = channelNamePaint;
        r5 = r9.getTextColor();
        r1.setColor(r5);
        goto L_0x010b;
    L_0x0106:
        r5 = channelNamePaint;
        r5.setColor(r1);
    L_0x010b:
        r1 = embedPostDatePaint;
        if (r1 == 0) goto L_0x012f;
    L_0x010f:
        if (r0 != 0) goto L_0x011a;
    L_0x0111:
        r1 = embedPostDatePaint;
        r5 = -7366752; // 0xffffffffff8f97a0 float:NaN double:NaN;
        r1.setColor(r5);
        goto L_0x012f;
    L_0x011a:
        if (r0 != r4) goto L_0x0125;
    L_0x011c:
        r1 = embedPostDatePaint;
        r5 = -11711675; // 0xffffffffff4d4b45 float:-2.7288256E38 double:NaN;
        r1.setColor(r5);
        goto L_0x012f;
    L_0x0125:
        if (r0 != r3) goto L_0x012f;
    L_0x0127:
        r1 = embedPostDatePaint;
        r5 = -10066330; // 0xffffffffff666666 float:-3.0625412E38 double:NaN;
        r1.setColor(r5);
    L_0x012f:
        r1 = dividerPaint;
        if (r1 == 0) goto L_0x0153;
    L_0x0133:
        if (r0 != 0) goto L_0x013e;
    L_0x0135:
        r1 = dividerPaint;
        r3 = -3288619; // 0xffffffffffcdd1d5 float:NaN double:NaN;
        r1.setColor(r3);
        goto L_0x0153;
    L_0x013e:
        if (r0 != r4) goto L_0x0149;
    L_0x0140:
        r1 = dividerPaint;
        r3 = -4080987; // 0xffffffffffc1baa5 float:NaN double:NaN;
        r1.setColor(r3);
        goto L_0x0153;
    L_0x0149:
        if (r0 != r3) goto L_0x0153;
    L_0x014b:
        r1 = dividerPaint;
        r3 = -12303292; // 0xffffffffff444444 float:-2.6088314E38 double:NaN;
        r1.setColor(r3);
    L_0x0153:
        r1 = r2;
    L_0x0154:
        r3 = titleTextPaints;
        r3 = r3.size();
        if (r1 >= r3) goto L_0x016e;
    L_0x015c:
        r3 = titleTextPaints;
        r3 = r3.valueAt(r1);
        r3 = (android.text.TextPaint) r3;
        r4 = r9.getTextColor();
        r3.setColor(r4);
        r1 = r1 + 1;
        goto L_0x0154;
    L_0x016e:
        r1 = r2;
    L_0x016f:
        r3 = subtitleTextPaints;
        r3 = r3.size();
        if (r1 >= r3) goto L_0x0189;
    L_0x0177:
        r3 = subtitleTextPaints;
        r3 = r3.valueAt(r1);
        r3 = (android.text.TextPaint) r3;
        r4 = r9.getTextColor();
        r3.setColor(r4);
        r1 = r1 + 1;
        goto L_0x016f;
    L_0x0189:
        r1 = r2;
    L_0x018a:
        r3 = headerTextPaints;
        r3 = r3.size();
        if (r1 >= r3) goto L_0x01a4;
    L_0x0192:
        r3 = headerTextPaints;
        r3 = r3.valueAt(r1);
        r3 = (android.text.TextPaint) r3;
        r4 = r9.getTextColor();
        r3.setColor(r4);
        r1 = r1 + 1;
        goto L_0x018a;
    L_0x01a4:
        r1 = r2;
    L_0x01a5:
        r3 = subheaderTextPaints;
        r3 = r3.size();
        if (r1 >= r3) goto L_0x01bf;
    L_0x01ad:
        r3 = subheaderTextPaints;
        r3 = r3.valueAt(r1);
        r3 = (android.text.TextPaint) r3;
        r4 = r9.getTextColor();
        r3.setColor(r4);
        r1 = r1 + 1;
        goto L_0x01a5;
    L_0x01bf:
        r1 = r2;
    L_0x01c0:
        r3 = quoteTextPaints;
        r3 = r3.size();
        if (r1 >= r3) goto L_0x01da;
    L_0x01c8:
        r3 = quoteTextPaints;
        r3 = r3.valueAt(r1);
        r3 = (android.text.TextPaint) r3;
        r4 = r9.getTextColor();
        r3.setColor(r4);
        r1 = r1 + 1;
        goto L_0x01c0;
    L_0x01da:
        r1 = r2;
    L_0x01db:
        r3 = preformattedTextPaints;
        r3 = r3.size();
        if (r1 >= r3) goto L_0x01f5;
    L_0x01e3:
        r3 = preformattedTextPaints;
        r3 = r3.valueAt(r1);
        r3 = (android.text.TextPaint) r3;
        r4 = r9.getTextColor();
        r3.setColor(r4);
        r1 = r1 + 1;
        goto L_0x01db;
    L_0x01f5:
        r1 = r2;
    L_0x01f6:
        r3 = paragraphTextPaints;
        r3 = r3.size();
        if (r1 >= r3) goto L_0x0210;
    L_0x01fe:
        r3 = paragraphTextPaints;
        r3 = r3.valueAt(r1);
        r3 = (android.text.TextPaint) r3;
        r4 = r9.getTextColor();
        r3.setColor(r4);
        r1 = r1 + 1;
        goto L_0x01f6;
    L_0x0210:
        r1 = r2;
    L_0x0211:
        r3 = listTextPaints;
        r3 = r3.size();
        if (r1 >= r3) goto L_0x022b;
    L_0x0219:
        r3 = listTextPaints;
        r3 = r3.valueAt(r1);
        r3 = (android.text.TextPaint) r3;
        r4 = r9.getTextColor();
        r3.setColor(r4);
        r1 = r1 + 1;
        goto L_0x0211;
    L_0x022b:
        r1 = r2;
    L_0x022c:
        r3 = embedPostTextPaints;
        r3 = r3.size();
        if (r1 >= r3) goto L_0x0246;
    L_0x0234:
        r3 = embedPostTextPaints;
        r3 = r3.valueAt(r1);
        r3 = (android.text.TextPaint) r3;
        r4 = r9.getTextColor();
        r3.setColor(r4);
        r1 = r1 + 1;
        goto L_0x022c;
    L_0x0246:
        r1 = r2;
    L_0x0247:
        r3 = videoTextPaints;
        r3 = r3.size();
        if (r1 >= r3) goto L_0x0261;
    L_0x024f:
        r3 = videoTextPaints;
        r3 = r3.valueAt(r1);
        r3 = (android.text.TextPaint) r3;
        r4 = r9.getTextColor();
        r3.setColor(r4);
        r1 = r1 + 1;
        goto L_0x0247;
    L_0x0261:
        r1 = r2;
    L_0x0262:
        r3 = captionTextPaints;
        r3 = r3.size();
        if (r1 >= r3) goto L_0x027c;
    L_0x026a:
        r3 = captionTextPaints;
        r3 = r3.valueAt(r1);
        r3 = (android.text.TextPaint) r3;
        r4 = r9.getTextColor();
        r3.setColor(r4);
        r1 = r1 + 1;
        goto L_0x0262;
    L_0x027c:
        r1 = r2;
    L_0x027d:
        r3 = authorTextPaints;
        r3 = r3.size();
        if (r1 >= r3) goto L_0x0297;
    L_0x0285:
        r3 = authorTextPaints;
        r3 = r3.valueAt(r1);
        r3 = (android.text.TextPaint) r3;
        r4 = r9.getTextColor();
        r3.setColor(r4);
        r1 = r1 + 1;
        goto L_0x027d;
    L_0x0297:
        r1 = r2;
    L_0x0298:
        r3 = footerTextPaints;
        r3 = r3.size();
        if (r1 >= r3) goto L_0x02b2;
    L_0x02a0:
        r3 = footerTextPaints;
        r3 = r3.valueAt(r1);
        r3 = (android.text.TextPaint) r3;
        r4 = r9.getTextColor();
        r3.setColor(r4);
        r1 = r1 + 1;
        goto L_0x0298;
    L_0x02b2:
        r1 = r2;
    L_0x02b3:
        r3 = subquoteTextPaints;
        r3 = r3.size();
        if (r1 >= r3) goto L_0x02cd;
    L_0x02bb:
        r3 = subquoteTextPaints;
        r3 = r3.valueAt(r1);
        r3 = (android.text.TextPaint) r3;
        r4 = r9.getTextColor();
        r3.setColor(r4);
        r1 = r1 + 1;
        goto L_0x02b3;
    L_0x02cd:
        r1 = r2;
    L_0x02ce:
        r3 = embedPostCaptionTextPaints;
        r3 = r3.size();
        if (r1 >= r3) goto L_0x02e8;
    L_0x02d6:
        r3 = embedPostCaptionTextPaints;
        r3 = r3.valueAt(r1);
        r3 = (android.text.TextPaint) r3;
        r4 = r9.getTextColor();
        r3.setColor(r4);
        r1 = r1 + 1;
        goto L_0x02ce;
    L_0x02e8:
        r1 = r2;
        r3 = embedTextPaints;
        r3 = r3.size();
        if (r1 >= r3) goto L_0x0303;
    L_0x02f1:
        r3 = embedTextPaints;
        r3 = r3.valueAt(r1);
        r3 = (android.text.TextPaint) r3;
        r4 = r9.getTextColor();
        r3.setColor(r4);
        r1 = r1 + 1;
        goto L_0x02e9;
        r1 = r2;
        r2 = slideshowTextPaints;
        r2 = r2.size();
        if (r1 >= r2) goto L_0x031f;
        r2 = slideshowTextPaints;
        r2 = r2.valueAt(r1);
        r2 = (android.text.TextPaint) r2;
        r3 = r9.getTextColor();
        r2.setColor(r3);
        r2 = r1 + 1;
        goto L_0x0304;
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.telegram.ui.ArticleViewer.updatePaintColors():void");
    }

    private void updatePaintFonts() {
        /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.DecodeException: Load method exception in method: org.telegram.ui.ArticleViewer.updatePaintFonts():void
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
        r14 = r16;
        r0 = org.telegram.messenger.ApplicationLoader.applicationContext;
        r1 = "articles";
        r15 = 0;
        r0 = r0.getSharedPreferences(r1, r15);
        r0 = r0.edit();
        r1 = "font_type";
        r2 = r14.selectedFont;
        r0 = r0.putInt(r1, r2);
        r0.commit();
        r0 = r14.selectedFont;
        if (r0 != 0) goto L_0x0022;
    L_0x001e:
        r0 = android.graphics.Typeface.DEFAULT;
    L_0x0020:
        r3 = r0;
        goto L_0x0025;
    L_0x0022:
        r0 = android.graphics.Typeface.SERIF;
        goto L_0x0020;
    L_0x0025:
        r0 = r14.selectedFont;
        if (r0 != 0) goto L_0x0031;
    L_0x0029:
        r0 = "fonts/ritalic.ttf";
        r0 = org.telegram.messenger.AndroidUtilities.getTypeface(r0);
    L_0x002f:
        r6 = r0;
        goto L_0x0039;
    L_0x0031:
        r0 = "serif";
        r1 = 2;
        r0 = android.graphics.Typeface.create(r0, r1);
        goto L_0x002f;
    L_0x0039:
        r0 = r14.selectedFont;
        if (r0 != 0) goto L_0x0045;
    L_0x003d:
        r0 = "fonts/rmedium.ttf";
        r0 = org.telegram.messenger.AndroidUtilities.getTypeface(r0);
    L_0x0043:
        r5 = r0;
        goto L_0x004d;
    L_0x0045:
        r0 = "serif";
        r1 = 1;
        r0 = android.graphics.Typeface.create(r0, r1);
        goto L_0x0043;
    L_0x004d:
        r0 = r14.selectedFont;
        if (r0 != 0) goto L_0x0059;
    L_0x0051:
        r0 = "fonts/rmediumitalic.ttf";
        r0 = org.telegram.messenger.AndroidUtilities.getTypeface(r0);
    L_0x0057:
        r4 = r0;
        goto L_0x0061;
    L_0x0059:
        r0 = "serif";
        r1 = 3;
        r0 = android.graphics.Typeface.create(r0, r1);
        goto L_0x0057;
    L_0x0061:
        r0 = r15;
    L_0x0062:
        r7 = r0;
        r0 = quoteTextPaints;
        r0 = r0.size();
        if (r7 >= r0) goto L_0x0081;
    L_0x006b:
        r0 = quoteTextPaints;
        r1 = r0.keyAt(r7);
        r0 = quoteTextPaints;
        r0 = r0.valueAt(r7);
        r2 = r0;
        r2 = (android.text.TextPaint) r2;
        r0 = r14;
        r0.updateFontEntry(r1, r2, r3, r4, r5, r6);
        r0 = r7 + 1;
        goto L_0x0062;
    L_0x0081:
        r0 = r15;
    L_0x0082:
        r1 = preformattedTextPaints;
        r1 = r1.size();
        if (r0 >= r1) goto L_0x00a4;
    L_0x008a:
        r1 = preformattedTextPaints;
        r8 = r1.keyAt(r0);
        r1 = preformattedTextPaints;
        r1 = r1.valueAt(r0);
        r9 = r1;
        r9 = (android.text.TextPaint) r9;
        r7 = r14;
        r10 = r3;
        r11 = r4;
        r12 = r5;
        r13 = r6;
        r7.updateFontEntry(r8, r9, r10, r11, r12, r13);
        r0 = r0 + 1;
        goto L_0x0082;
    L_0x00a4:
        r0 = r15;
    L_0x00a5:
        r1 = paragraphTextPaints;
        r1 = r1.size();
        if (r0 >= r1) goto L_0x00c7;
    L_0x00ad:
        r1 = paragraphTextPaints;
        r8 = r1.keyAt(r0);
        r1 = paragraphTextPaints;
        r1 = r1.valueAt(r0);
        r9 = r1;
        r9 = (android.text.TextPaint) r9;
        r7 = r14;
        r10 = r3;
        r11 = r4;
        r12 = r5;
        r13 = r6;
        r7.updateFontEntry(r8, r9, r10, r11, r12, r13);
        r0 = r0 + 1;
        goto L_0x00a5;
    L_0x00c7:
        r0 = r15;
    L_0x00c8:
        r1 = listTextPaints;
        r1 = r1.size();
        if (r0 >= r1) goto L_0x00ea;
    L_0x00d0:
        r1 = listTextPaints;
        r8 = r1.keyAt(r0);
        r1 = listTextPaints;
        r1 = r1.valueAt(r0);
        r9 = r1;
        r9 = (android.text.TextPaint) r9;
        r7 = r14;
        r10 = r3;
        r11 = r4;
        r12 = r5;
        r13 = r6;
        r7.updateFontEntry(r8, r9, r10, r11, r12, r13);
        r0 = r0 + 1;
        goto L_0x00c8;
    L_0x00ea:
        r0 = r15;
    L_0x00eb:
        r1 = embedPostTextPaints;
        r1 = r1.size();
        if (r0 >= r1) goto L_0x010d;
    L_0x00f3:
        r1 = embedPostTextPaints;
        r8 = r1.keyAt(r0);
        r1 = embedPostTextPaints;
        r1 = r1.valueAt(r0);
        r9 = r1;
        r9 = (android.text.TextPaint) r9;
        r7 = r14;
        r10 = r3;
        r11 = r4;
        r12 = r5;
        r13 = r6;
        r7.updateFontEntry(r8, r9, r10, r11, r12, r13);
        r0 = r0 + 1;
        goto L_0x00eb;
    L_0x010d:
        r0 = r15;
    L_0x010e:
        r1 = videoTextPaints;
        r1 = r1.size();
        if (r0 >= r1) goto L_0x0130;
    L_0x0116:
        r1 = videoTextPaints;
        r8 = r1.keyAt(r0);
        r1 = videoTextPaints;
        r1 = r1.valueAt(r0);
        r9 = r1;
        r9 = (android.text.TextPaint) r9;
        r7 = r14;
        r10 = r3;
        r11 = r4;
        r12 = r5;
        r13 = r6;
        r7.updateFontEntry(r8, r9, r10, r11, r12, r13);
        r0 = r0 + 1;
        goto L_0x010e;
    L_0x0130:
        r0 = r15;
    L_0x0131:
        r1 = captionTextPaints;
        r1 = r1.size();
        if (r0 >= r1) goto L_0x0153;
    L_0x0139:
        r1 = captionTextPaints;
        r8 = r1.keyAt(r0);
        r1 = captionTextPaints;
        r1 = r1.valueAt(r0);
        r9 = r1;
        r9 = (android.text.TextPaint) r9;
        r7 = r14;
        r10 = r3;
        r11 = r4;
        r12 = r5;
        r13 = r6;
        r7.updateFontEntry(r8, r9, r10, r11, r12, r13);
        r0 = r0 + 1;
        goto L_0x0131;
    L_0x0153:
        r0 = r15;
    L_0x0154:
        r1 = authorTextPaints;
        r1 = r1.size();
        if (r0 >= r1) goto L_0x0176;
    L_0x015c:
        r1 = authorTextPaints;
        r8 = r1.keyAt(r0);
        r1 = authorTextPaints;
        r1 = r1.valueAt(r0);
        r9 = r1;
        r9 = (android.text.TextPaint) r9;
        r7 = r14;
        r10 = r3;
        r11 = r4;
        r12 = r5;
        r13 = r6;
        r7.updateFontEntry(r8, r9, r10, r11, r12, r13);
        r0 = r0 + 1;
        goto L_0x0154;
    L_0x0176:
        r0 = r15;
    L_0x0177:
        r1 = footerTextPaints;
        r1 = r1.size();
        if (r0 >= r1) goto L_0x0199;
    L_0x017f:
        r1 = footerTextPaints;
        r8 = r1.keyAt(r0);
        r1 = footerTextPaints;
        r1 = r1.valueAt(r0);
        r9 = r1;
        r9 = (android.text.TextPaint) r9;
        r7 = r14;
        r10 = r3;
        r11 = r4;
        r12 = r5;
        r13 = r6;
        r7.updateFontEntry(r8, r9, r10, r11, r12, r13);
        r0 = r0 + 1;
        goto L_0x0177;
    L_0x0199:
        r0 = r15;
    L_0x019a:
        r1 = subquoteTextPaints;
        r1 = r1.size();
        if (r0 >= r1) goto L_0x01bc;
    L_0x01a2:
        r1 = subquoteTextPaints;
        r8 = r1.keyAt(r0);
        r1 = subquoteTextPaints;
        r1 = r1.valueAt(r0);
        r9 = r1;
        r9 = (android.text.TextPaint) r9;
        r7 = r14;
        r10 = r3;
        r11 = r4;
        r12 = r5;
        r13 = r6;
        r7.updateFontEntry(r8, r9, r10, r11, r12, r13);
        r0 = r0 + 1;
        goto L_0x019a;
    L_0x01bc:
        r0 = r15;
    L_0x01bd:
        r1 = embedPostCaptionTextPaints;
        r1 = r1.size();
        if (r0 >= r1) goto L_0x01df;
    L_0x01c5:
        r1 = embedPostCaptionTextPaints;
        r8 = r1.keyAt(r0);
        r1 = embedPostCaptionTextPaints;
        r1 = r1.valueAt(r0);
        r9 = r1;
        r9 = (android.text.TextPaint) r9;
        r7 = r14;
        r10 = r3;
        r11 = r4;
        r12 = r5;
        r13 = r6;
        r7.updateFontEntry(r8, r9, r10, r11, r12, r13);
        r0 = r0 + 1;
        goto L_0x01bd;
    L_0x01df:
        r0 = r15;
        r1 = embedTextPaints;
        r1 = r1.size();
        if (r0 >= r1) goto L_0x0202;
    L_0x01e8:
        r1 = embedTextPaints;
        r8 = r1.keyAt(r0);
        r1 = embedTextPaints;
        r1 = r1.valueAt(r0);
        r9 = r1;
        r9 = (android.text.TextPaint) r9;
        r7 = r14;
        r10 = r3;
        r11 = r4;
        r12 = r5;
        r13 = r6;
        r7.updateFontEntry(r8, r9, r10, r11, r12, r13);
        r0 = r0 + 1;
        goto L_0x01e0;
        r0 = r15;
        r1 = slideshowTextPaints;
        r1 = r1.size();
        if (r0 >= r1) goto L_0x0226;
        r1 = slideshowTextPaints;
        r8 = r1.keyAt(r0);
        r1 = slideshowTextPaints;
        r1 = r1.valueAt(r0);
        r9 = r1;
        r9 = (android.text.TextPaint) r9;
        r7 = r14;
        r10 = r3;
        r11 = r4;
        r12 = r5;
        r13 = r6;
        r7.updateFontEntry(r8, r9, r10, r11, r12, r13);
        r15 = r0 + 1;
        goto L_0x0203;
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.telegram.ui.ArticleViewer.updatePaintFonts():void");
    }

    public void didReceivedNotification(int r1, int r2, java.lang.Object... r3) {
        /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.DecodeException: Load method exception in method: org.telegram.ui.ArticleViewer.didReceivedNotification(int, int, java.lang.Object[]):void
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
        r0 = org.telegram.messenger.NotificationCenter.FileDidFailedLoad;
        r1 = 1065353216; // 0x3f800000 float:1.0 double:5.263544247E-315;
        r2 = 3;
        r3 = 1;
        r4 = 0;
        if (r9 != r0) goto L_0x0030;
    L_0x0009:
        r0 = r11[r4];
        r0 = (java.lang.String) r0;
    L_0x000e:
        if (r4 >= r2) goto L_0x002e;
    L_0x0010:
        r5 = r8.currentFileNames;
        r5 = r5[r4];
        if (r5 == 0) goto L_0x002b;
    L_0x0016:
        r5 = r8.currentFileNames;
        r5 = r5[r4];
        r5 = r5.equals(r0);
        if (r5 == 0) goto L_0x002b;
    L_0x0020:
        r2 = r8.radialProgressViews;
        r2 = r2[r4];
        r2.setProgress(r1, r3);
        r8.checkProgress(r4, r3);
        goto L_0x002e;
    L_0x002b:
        r4 = r4 + 1;
        goto L_0x000e;
    L_0x002e:
        goto L_0x016c;
    L_0x0030:
        r0 = org.telegram.messenger.NotificationCenter.FileDidLoaded;
        if (r9 != r0) goto L_0x0068;
    L_0x0034:
        r0 = r11[r4];
        r0 = (java.lang.String) r0;
        r5 = r4;
    L_0x0039:
        if (r5 >= r2) goto L_0x0066;
    L_0x003b:
        r6 = r8.currentFileNames;
        r6 = r6[r5];
        if (r6 == 0) goto L_0x0063;
    L_0x0041:
        r6 = r8.currentFileNames;
        r6 = r6[r5];
        r6 = r6.equals(r0);
        if (r6 == 0) goto L_0x0063;
    L_0x004b:
        r2 = r8.radialProgressViews;
        r2 = r2[r5];
        r2.setProgress(r1, r3);
        r8.checkProgress(r5, r3);
        if (r5 != 0) goto L_0x0066;
    L_0x0057:
        r1 = r8.currentIndex;
        r1 = r8.isMediaVideo(r1);
        if (r1 == 0) goto L_0x0066;
    L_0x005f:
        r8.onActionClick(r4);
        goto L_0x0066;
    L_0x0063:
        r5 = r5 + 1;
        goto L_0x0039;
    L_0x0066:
        goto L_0x016c;
    L_0x0068:
        r0 = org.telegram.messenger.NotificationCenter.FileLoadProgressChanged;
        if (r9 != r0) goto L_0x0098;
    L_0x006c:
        r0 = r11[r4];
        r0 = (java.lang.String) r0;
    L_0x0071:
        r1 = r4;
        if (r1 >= r2) goto L_0x0096;
    L_0x0074:
        r4 = r8.currentFileNames;
        r4 = r4[r1];
        if (r4 == 0) goto L_0x0093;
    L_0x007a:
        r4 = r8.currentFileNames;
        r4 = r4[r1];
        r4 = r4.equals(r0);
        if (r4 == 0) goto L_0x0093;
    L_0x0084:
        r4 = r11[r3];
        r4 = (java.lang.Float) r4;
        r5 = r8.radialProgressViews;
        r5 = r5[r1];
        r6 = r4.floatValue();
        r5.setProgress(r6, r3);
    L_0x0093:
        r4 = r1 + 1;
        goto L_0x0071;
    L_0x0096:
        goto L_0x016c;
    L_0x0098:
        r0 = org.telegram.messenger.NotificationCenter.emojiDidLoaded;
        if (r9 != r0) goto L_0x00a7;
    L_0x009c:
        r0 = r8.captionTextView;
        if (r0 == 0) goto L_0x016c;
    L_0x00a0:
        r0 = r8.captionTextView;
        r0.invalidate();
        goto L_0x016c;
    L_0x00a7:
        r0 = org.telegram.messenger.NotificationCenter.needSetDayNightTheme;
        if (r9 != r0) goto L_0x00c2;
    L_0x00ab:
        r0 = r8.nightModeEnabled;
        if (r0 == 0) goto L_0x016c;
    L_0x00af:
        r0 = r8.selectedColor;
        r1 = 2;
        if (r0 == r1) goto L_0x016c;
    L_0x00b4:
        r0 = r8.adapter;
        if (r0 == 0) goto L_0x016c;
    L_0x00b8:
        r8.updatePaintColors();
        r0 = r8.adapter;
        r0.notifyDataSetChanged();
        goto L_0x016c;
    L_0x00c2:
        r0 = org.telegram.messenger.NotificationCenter.messagePlayingDidStarted;
        if (r9 != r0) goto L_0x00ec;
    L_0x00c6:
        r0 = r11[r4];
        r0 = (org.telegram.messenger.MessageObject) r0;
        r1 = r8.listView;
        if (r1 == 0) goto L_0x00ea;
    L_0x00ce:
        r1 = r8.listView;
        r1 = r1.getChildCount();
        r2 = r4;
    L_0x00d5:
        if (r2 >= r1) goto L_0x00ea;
    L_0x00d7:
        r3 = r8.listView;
        r3 = r3.getChildAt(r2);
        r5 = r3 instanceof org.telegram.ui.ArticleViewer.BlockAudioCell;
        if (r5 == 0) goto L_0x00e7;
    L_0x00e1:
        r5 = r3;
        r5 = (org.telegram.ui.ArticleViewer.BlockAudioCell) r5;
        r5.updateButtonState(r4);
    L_0x00e7:
        r2 = r2 + 1;
        goto L_0x00d5;
    L_0x00ea:
        goto L_0x016c;
    L_0x00ec:
        r0 = org.telegram.messenger.NotificationCenter.messagePlayingDidReset;
        if (r9 == r0) goto L_0x0145;
    L_0x00f0:
        r0 = org.telegram.messenger.NotificationCenter.messagePlayingPlayStateChanged;
        if (r9 != r0) goto L_0x00f5;
    L_0x00f4:
        goto L_0x0145;
    L_0x00f5:
        r0 = org.telegram.messenger.NotificationCenter.messagePlayingProgressDidChanged;
        if (r9 != r0) goto L_0x016c;
    L_0x00f9:
        r0 = r11[r4];
        r0 = (java.lang.Integer) r0;
        r1 = r8.listView;
        if (r1 == 0) goto L_0x016c;
    L_0x0101:
        r1 = r8.listView;
        r1 = r1.getChildCount();
    L_0x0108:
        r2 = r4;
        if (r2 >= r1) goto L_0x016c;
    L_0x010b:
        r3 = r8.listView;
        r3 = r3.getChildAt(r2);
        r4 = r3 instanceof org.telegram.ui.ArticleViewer.BlockAudioCell;
        if (r4 == 0) goto L_0x0142;
    L_0x0115:
        r4 = r3;
        r4 = (org.telegram.ui.ArticleViewer.BlockAudioCell) r4;
        r5 = r4.getMessageObject();
        if (r5 == 0) goto L_0x0142;
    L_0x011e:
        r6 = r5.getId();
        r7 = r0.intValue();
        if (r6 != r7) goto L_0x0142;
    L_0x0128:
        r6 = org.telegram.messenger.MediaController.getInstance();
        r6 = r6.getPlayingMessageObject();
        if (r6 == 0) goto L_0x016c;
    L_0x0132:
        r7 = r6.audioProgress;
        r5.audioProgress = r7;
        r7 = r6.audioProgressSec;
        r5.audioProgressSec = r7;
        r7 = r6.audioPlayerDuration;
        r5.audioPlayerDuration = r7;
        r4.updatePlayingMessageProgress();
        goto L_0x016c;
    L_0x0142:
        r4 = r2 + 1;
        goto L_0x0108;
    L_0x0145:
        r0 = r8.listView;
        if (r0 == 0) goto L_0x016c;
    L_0x0149:
        r0 = r8.listView;
        r0 = r0.getChildCount();
        r1 = r4;
        if (r1 >= r0) goto L_0x016b;
    L_0x0152:
        r2 = r8.listView;
        r2 = r2.getChildAt(r1);
        r3 = r2 instanceof org.telegram.ui.ArticleViewer.BlockAudioCell;
        if (r3 == 0) goto L_0x0168;
        r3 = r2;
        r3 = (org.telegram.ui.ArticleViewer.BlockAudioCell) r3;
        r5 = r3.getMessageObject();
        if (r5 == 0) goto L_0x0168;
        r3.updateButtonState(r4);
        r1 = r1 + 1;
        goto L_0x0150;
    L_0x016c:
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.telegram.ui.ArticleViewer.didReceivedNotification(int, int, java.lang.Object[]):void");
    }

    static /* synthetic */ int access$804(ArticleViewer x0) {
        int i = x0.pressCount + 1;
        x0.pressCount = i;
        return i;
    }

    public static ArticleViewer getInstance() {
        ArticleViewer localInstance = Instance;
        if (localInstance == null) {
            synchronized (ArticleViewer.class) {
                localInstance = Instance;
                if (localInstance == null) {
                    ArticleViewer articleViewer = new ArticleViewer();
                    localInstance = articleViewer;
                    Instance = articleViewer;
                }
            }
        }
        return localInstance;
    }

    public static boolean hasInstance() {
        return Instance != null;
    }

    private void showPopup(View parent, int gravity, int x, int y) {
        if (this.popupWindow == null || !this.popupWindow.isShowing()) {
            if (this.popupLayout == null) {
                this.popupRect = new Rect();
                this.popupLayout = new ActionBarPopupWindowLayout(this.parentActivity);
                this.popupLayout.setBackgroundDrawable(this.parentActivity.getResources().getDrawable(R.drawable.menu_copy));
                this.popupLayout.setAnimationEnabled(false);
                this.popupLayout.setOnTouchListener(new OnTouchListener() {
                    public boolean onTouch(View v, MotionEvent event) {
                        if (event.getActionMasked() == 0 && ArticleViewer.this.popupWindow != null && ArticleViewer.this.popupWindow.isShowing()) {
                            v.getHitRect(ArticleViewer.this.popupRect);
                            if (!ArticleViewer.this.popupRect.contains((int) event.getX(), (int) event.getY())) {
                                ArticleViewer.this.popupWindow.dismiss();
                            }
                        }
                        return false;
                    }
                });
                this.popupLayout.setDispatchKeyEventListener(new OnDispatchKeyEventListener() {
                    public void onDispatchKeyEvent(KeyEvent keyEvent) {
                        if (keyEvent.getKeyCode() == 4 && keyEvent.getRepeatCount() == 0 && ArticleViewer.this.popupWindow != null && ArticleViewer.this.popupWindow.isShowing()) {
                            ArticleViewer.this.popupWindow.dismiss();
                        }
                    }
                });
                this.popupLayout.setShowedFromBotton(false);
                TextView deleteView = new TextView(this.parentActivity);
                deleteView.setTextColor(Theme.ACTION_BAR_VIDEO_EDIT_COLOR);
                deleteView.setBackgroundDrawable(Theme.getSelectorDrawable(false));
                deleteView.setGravity(16);
                deleteView.setPadding(AndroidUtilities.dp(14.0f), 0, AndroidUtilities.dp(14.0f), 0);
                deleteView.setTextSize(1, 15.0f);
                deleteView.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
                deleteView.setText(LocaleController.getString("Copy", R.string.Copy).toUpperCase());
                deleteView.setOnClickListener(new OnClickListener() {
                    public void onClick(View v) {
                        if (ArticleViewer.this.pressedLinkOwnerLayout != null) {
                            AndroidUtilities.addToClipboard(ArticleViewer.this.pressedLinkOwnerLayout.getText());
                            Toast.makeText(ArticleViewer.this.parentActivity, LocaleController.getString("TextCopied", R.string.TextCopied), 0).show();
                            if (ArticleViewer.this.popupWindow != null && ArticleViewer.this.popupWindow.isShowing()) {
                                ArticleViewer.this.popupWindow.dismiss(true);
                            }
                        }
                    }
                });
                this.popupLayout.addView(deleteView, LayoutHelper.createFrame(-2, 38.0f));
                this.popupWindow = new ActionBarPopupWindow(this.popupLayout, -2, -2);
                this.popupWindow.setAnimationEnabled(false);
                this.popupWindow.setAnimationStyle(R.style.PopupAnimation);
                this.popupWindow.setOutsideTouchable(true);
                this.popupWindow.setClippingEnabled(true);
                this.popupWindow.setInputMethodMode(2);
                this.popupWindow.setSoftInputMode(0);
                this.popupWindow.getContentView().setFocusableInTouchMode(true);
                this.popupWindow.setOnDismissListener(new OnDismissListener() {
                    public void onDismiss() {
                        if (ArticleViewer.this.pressedLinkOwnerView != null) {
                            ArticleViewer.this.pressedLinkOwnerLayout = null;
                            ArticleViewer.this.pressedLinkOwnerView.invalidate();
                            ArticleViewer.this.pressedLinkOwnerView = null;
                        }
                    }
                });
            }
            this.popupLayout.measure(MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(1000.0f), Integer.MIN_VALUE), MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(1000.0f), Integer.MIN_VALUE));
            this.popupWindow.setFocusable(true);
            this.popupWindow.showAtLocation(parent, gravity, x, y);
            this.popupWindow.startAnimation();
            return;
        }
        this.popupWindow.dismiss();
    }

    private void setRichTextParents(RichText parentRichText, RichText richText) {
        if (richText != null) {
            richText.parentRichText = parentRichText;
            if (richText instanceof TL_textFixed) {
                setRichTextParents(richText, ((TL_textFixed) richText).text);
            } else if (richText instanceof TL_textItalic) {
                setRichTextParents(richText, ((TL_textItalic) richText).text);
            } else if (richText instanceof TL_textBold) {
                setRichTextParents(richText, ((TL_textBold) richText).text);
            } else if (richText instanceof TL_textUnderline) {
                setRichTextParents(richText, ((TL_textUnderline) richText).text);
            } else if (richText instanceof TL_textStrike) {
                setRichTextParents(parentRichText, ((TL_textStrike) richText).text);
            } else if (richText instanceof TL_textEmail) {
                setRichTextParents(richText, ((TL_textEmail) richText).text);
            } else if (richText instanceof TL_textUrl) {
                setRichTextParents(richText, ((TL_textUrl) richText).text);
            } else if (richText instanceof TL_textConcat) {
                int count = richText.texts.size();
                for (int a = 0; a < count; a++) {
                    setRichTextParents(richText, (RichText) richText.texts.get(a));
                }
            }
        }
    }

    private void addPageToStack(WebPage webPage, String anchor) {
        saveCurrentPagePosition();
        this.currentPage = webPage;
        this.pagesStack.add(webPage);
        updateInterfaceForCurrentPage(false);
        if (anchor != null) {
            Integer row = (Integer) this.anchors.get(anchor.toLowerCase());
            if (row != null) {
                this.layoutManager.scrollToPositionWithOffset(row.intValue(), 0);
            }
        }
    }

    private boolean removeLastPageFromStack() {
        if (this.pagesStack.size() < 2) {
            return false;
        }
        this.pagesStack.remove(this.pagesStack.size() - 1);
        this.currentPage = (WebPage) this.pagesStack.get(this.pagesStack.size() - 1);
        updateInterfaceForCurrentPage(true);
        return true;
    }

    protected void startCheckLongPress() {
        if (!this.checkingForLongPress) {
            this.checkingForLongPress = true;
            if (this.pendingCheckForTap == null) {
                this.pendingCheckForTap = new CheckForTap();
            }
            this.windowView.postDelayed(this.pendingCheckForTap, (long) ViewConfiguration.getTapTimeout());
        }
    }

    protected void cancelCheckLongPress() {
        this.checkingForLongPress = false;
        if (this.pendingCheckForLongPress != null) {
            this.windowView.removeCallbacks(this.pendingCheckForLongPress);
        }
        if (this.pendingCheckForTap != null) {
            this.windowView.removeCallbacks(this.pendingCheckForTap);
        }
    }

    private int getTextFlags(RichText richText) {
        if (richText instanceof TL_textFixed) {
            return getTextFlags(richText.parentRichText) | 4;
        }
        if (richText instanceof TL_textItalic) {
            return getTextFlags(richText.parentRichText) | 2;
        }
        if (richText instanceof TL_textBold) {
            return getTextFlags(richText.parentRichText) | 1;
        }
        if (richText instanceof TL_textUnderline) {
            return getTextFlags(richText.parentRichText) | 16;
        }
        if (richText instanceof TL_textStrike) {
            return getTextFlags(richText.parentRichText) | 32;
        }
        if (richText instanceof TL_textEmail) {
            return getTextFlags(richText.parentRichText) | 8;
        }
        if (richText instanceof TL_textUrl) {
            return getTextFlags(richText.parentRichText) | 8;
        }
        if (richText != null) {
            return getTextFlags(richText.parentRichText);
        }
        return 0;
    }

    private CharSequence getText(RichText parentRichText, RichText richText, PageBlock parentBlock) {
        if (richText instanceof TL_textFixed) {
            return getText(parentRichText, ((TL_textFixed) richText).text, parentBlock);
        }
        if (richText instanceof TL_textItalic) {
            return getText(parentRichText, ((TL_textItalic) richText).text, parentBlock);
        }
        if (richText instanceof TL_textBold) {
            return getText(parentRichText, ((TL_textBold) richText).text, parentBlock);
        }
        if (richText instanceof TL_textUnderline) {
            return getText(parentRichText, ((TL_textUnderline) richText).text, parentBlock);
        }
        if (richText instanceof TL_textStrike) {
            return getText(parentRichText, ((TL_textStrike) richText).text, parentBlock);
        }
        TextPaint textPaint = null;
        int a = 0;
        SpannableStringBuilder spannableStringBuilder;
        MetricAffectingSpan[] innerSpans;
        if (richText instanceof TL_textEmail) {
            spannableStringBuilder = new SpannableStringBuilder(getText(parentRichText, ((TL_textEmail) richText).text, parentBlock));
            innerSpans = (MetricAffectingSpan[]) spannableStringBuilder.getSpans(0, spannableStringBuilder.length(), MetricAffectingSpan.class);
            if (innerSpans != null) {
                if (innerSpans.length != 0) {
                    spannableStringBuilder.setSpan(new TextPaintUrlSpan(textPaint, getUrl(richText)), 0, spannableStringBuilder.length(), 33);
                    return spannableStringBuilder;
                }
            }
            textPaint = getTextPaint(parentRichText, richText, parentBlock);
            spannableStringBuilder.setSpan(new TextPaintUrlSpan(textPaint, getUrl(richText)), 0, spannableStringBuilder.length(), 33);
            return spannableStringBuilder;
        } else if (richText instanceof TL_textUrl) {
            spannableStringBuilder = new SpannableStringBuilder(getText(parentRichText, ((TL_textUrl) richText).text, parentBlock));
            innerSpans = (MetricAffectingSpan[]) spannableStringBuilder.getSpans(0, spannableStringBuilder.length(), MetricAffectingSpan.class);
            if (innerSpans != null) {
                if (innerSpans.length != 0) {
                    spannableStringBuilder.setSpan(new TextPaintUrlSpan(textPaint, getUrl(richText)), 0, spannableStringBuilder.length(), 33);
                    return spannableStringBuilder;
                }
            }
            textPaint = getTextPaint(parentRichText, richText, parentBlock);
            spannableStringBuilder.setSpan(new TextPaintUrlSpan(textPaint, getUrl(richText)), 0, spannableStringBuilder.length(), 33);
            return spannableStringBuilder;
        } else if (richText instanceof TL_textPlain) {
            return ((TL_textPlain) richText).text;
        } else {
            if (richText instanceof TL_textEmpty) {
                return TtmlNode.ANONYMOUS_REGION_ID;
            }
            if (richText instanceof TL_textConcat) {
                spannableStringBuilder = new SpannableStringBuilder();
                int count = richText.texts.size();
                while (a < count) {
                    RichText innerRichText = (RichText) richText.texts.get(a);
                    CharSequence innerText = getText(parentRichText, innerRichText, parentBlock);
                    int flags = getTextFlags(innerRichText);
                    int startLength = spannableStringBuilder.length();
                    spannableStringBuilder.append(innerText);
                    if (!(flags == 0 || (innerText instanceof SpannableStringBuilder))) {
                        if ((flags & 8) != 0) {
                            String url = getUrl(innerRichText);
                            if (url == null) {
                                url = getUrl(parentRichText);
                            }
                            spannableStringBuilder.setSpan(new TextPaintUrlSpan(getTextPaint(parentRichText, innerRichText, parentBlock), url), startLength, spannableStringBuilder.length(), 33);
                        } else {
                            spannableStringBuilder.setSpan(new TextPaintSpan(getTextPaint(parentRichText, innerRichText, parentBlock)), startLength, spannableStringBuilder.length(), 33);
                        }
                    }
                    a++;
                }
                return spannableStringBuilder;
            }
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("not supported ");
            stringBuilder.append(richText);
            return stringBuilder.toString();
        }
    }

    private String getUrl(RichText richText) {
        if (richText instanceof TL_textFixed) {
            return getUrl(((TL_textFixed) richText).text);
        }
        if (richText instanceof TL_textItalic) {
            return getUrl(((TL_textItalic) richText).text);
        }
        if (richText instanceof TL_textBold) {
            return getUrl(((TL_textBold) richText).text);
        }
        if (richText instanceof TL_textUnderline) {
            return getUrl(((TL_textUnderline) richText).text);
        }
        if (richText instanceof TL_textStrike) {
            return getUrl(((TL_textStrike) richText).text);
        }
        if (richText instanceof TL_textEmail) {
            return ((TL_textEmail) richText).email;
        }
        if (richText instanceof TL_textUrl) {
            return ((TL_textUrl) richText).url;
        }
        return null;
    }

    private int getTextColor() {
        switch (getSelectedColor()) {
            case 0:
            case 1:
                return -14606047;
            default:
                return -6710887;
        }
    }

    private int getGrayTextColor() {
        switch (getSelectedColor()) {
            case 0:
                return -8156010;
            case 1:
                return -11711675;
            default:
                return -10066330;
        }
    }

    private TextPaint getTextPaint(RichText parentRichText, RichText richText, PageBlock parentBlock) {
        int additionalSize;
        TextPaint paint;
        int flags = getTextFlags(richText);
        SparseArray<TextPaint> currentMap = null;
        int textSize = AndroidUtilities.dp(14.0f);
        int textColor = -65536;
        if (this.selectedFontSize == 0) {
            additionalSize = -AndroidUtilities.dp(4.0f);
        } else if (this.selectedFontSize == 1) {
            additionalSize = -AndroidUtilities.dp(2.0f);
        } else if (this.selectedFontSize == 3) {
            additionalSize = AndroidUtilities.dp(2.0f);
        } else if (this.selectedFontSize == 4) {
            additionalSize = AndroidUtilities.dp(4.0f);
        } else {
            additionalSize = 0;
            if (parentBlock instanceof TL_pageBlockPhoto) {
                currentMap = captionTextPaints;
                textSize = AndroidUtilities.dp(14.0f);
                textColor = getGrayTextColor();
            } else if (parentBlock instanceof TL_pageBlockTitle) {
                currentMap = titleTextPaints;
                textSize = AndroidUtilities.dp(24.0f);
                textColor = getTextColor();
            } else if (parentBlock instanceof TL_pageBlockAuthorDate) {
                currentMap = authorTextPaints;
                textSize = AndroidUtilities.dp(14.0f);
                textColor = getGrayTextColor();
            } else if (parentBlock instanceof TL_pageBlockFooter) {
                currentMap = footerTextPaints;
                textSize = AndroidUtilities.dp(14.0f);
                textColor = getGrayTextColor();
            } else if (parentBlock instanceof TL_pageBlockSubtitle) {
                currentMap = subtitleTextPaints;
                textSize = AndroidUtilities.dp(21.0f);
                textColor = getTextColor();
            } else if (parentBlock instanceof TL_pageBlockHeader) {
                currentMap = headerTextPaints;
                textSize = AndroidUtilities.dp(21.0f);
                textColor = getTextColor();
            } else if (parentBlock instanceof TL_pageBlockSubheader) {
                if (!(parentBlock instanceof TL_pageBlockBlockquote)) {
                    if (parentBlock instanceof TL_pageBlockPullquote) {
                        if (parentBlock instanceof TL_pageBlockPreformatted) {
                            currentMap = preformattedTextPaints;
                            textSize = AndroidUtilities.dp(14.0f);
                            textColor = getTextColor();
                        } else if (parentBlock instanceof TL_pageBlockParagraph) {
                            if (parentBlock instanceof TL_pageBlockList) {
                                currentMap = listTextPaints;
                                textSize = AndroidUtilities.dp(15.0f);
                                textColor = getTextColor();
                            } else if (parentBlock instanceof TL_pageBlockEmbed) {
                                currentMap = embedTextPaints;
                                textSize = AndroidUtilities.dp(14.0f);
                                textColor = getGrayTextColor();
                            } else if (parentBlock instanceof TL_pageBlockSlideshow) {
                                currentMap = slideshowTextPaints;
                                textSize = AndroidUtilities.dp(14.0f);
                                textColor = getGrayTextColor();
                            } else if (parentBlock instanceof TL_pageBlockEmbedPost) {
                                if ((parentBlock instanceof TL_pageBlockVideo) || (parentBlock instanceof TL_pageBlockAudio)) {
                                    currentMap = videoTextPaints;
                                    textSize = AndroidUtilities.dp(14.0f);
                                    textColor = getTextColor();
                                }
                            } else if (richText != null) {
                                currentMap = embedPostTextPaints;
                                textSize = AndroidUtilities.dp(14.0f);
                                textColor = getTextColor();
                            }
                        } else if (parentBlock.caption != parentRichText) {
                            currentMap = embedPostCaptionTextPaints;
                            textSize = AndroidUtilities.dp(14.0f);
                            textColor = getGrayTextColor();
                        } else {
                            currentMap = paragraphTextPaints;
                            textSize = AndroidUtilities.dp(16.0f);
                            textColor = getTextColor();
                        }
                    }
                }
                if (parentBlock.text == parentRichText) {
                    currentMap = quoteTextPaints;
                    textSize = AndroidUtilities.dp(15.0f);
                    textColor = getTextColor();
                } else if (parentBlock.caption == parentRichText) {
                    currentMap = subquoteTextPaints;
                    textSize = AndroidUtilities.dp(14.0f);
                    textColor = getGrayTextColor();
                }
            } else {
                currentMap = subheaderTextPaints;
                textSize = AndroidUtilities.dp(18.0f);
                textColor = getTextColor();
            }
            if (currentMap != null) {
                if (errorTextPaint == null) {
                    errorTextPaint = new TextPaint(1);
                    errorTextPaint.setColor(-65536);
                }
                errorTextPaint.setTextSize((float) AndroidUtilities.dp(14.0f));
                return errorTextPaint;
            }
            paint = (TextPaint) currentMap.get(flags);
            if (paint == null) {
                paint = new TextPaint(1);
                if ((flags & 4) != 0) {
                    paint.setTypeface(AndroidUtilities.getTypeface("fonts/rmono.ttf"));
                } else {
                    if (!(this.selectedFont == 1 || (parentBlock instanceof TL_pageBlockTitle) || (parentBlock instanceof TL_pageBlockHeader) || (parentBlock instanceof TL_pageBlockSubtitle))) {
                        if (parentBlock instanceof TL_pageBlockSubheader) {
                            if ((flags & 1) == 0 && (flags & 2) != 0) {
                                paint.setTypeface(AndroidUtilities.getTypeface("fonts/rmediumitalic.ttf"));
                            } else if ((flags & 1) != 0) {
                                paint.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
                            } else if ((flags & 2) != 0) {
                                paint.setTypeface(AndroidUtilities.getTypeface("fonts/ritalic.ttf"));
                            }
                        }
                    }
                    if ((flags & 1) == 0 && (flags & 2) != 0) {
                        paint.setTypeface(Typeface.create(C.SERIF_NAME, 3));
                    } else if ((flags & 1) != 0) {
                        paint.setTypeface(Typeface.create(C.SERIF_NAME, 1));
                    } else if ((flags & 2) == 0) {
                        paint.setTypeface(Typeface.create(C.SERIF_NAME, 2));
                    } else {
                        paint.setTypeface(Typeface.create(C.SERIF_NAME, 0));
                    }
                }
                if ((flags & 32) != 0) {
                    paint.setFlags(paint.getFlags() | 16);
                }
                if ((flags & 16) != 0) {
                    paint.setFlags(paint.getFlags() | 8);
                }
                if ((flags & 8) != 0) {
                    paint.setFlags(paint.getFlags() | 8);
                    textColor = getTextColor();
                }
                paint.setColor(textColor);
                currentMap.put(flags, paint);
            }
            paint.setTextSize((float) (textSize + additionalSize));
            return paint;
        }
        if (parentBlock instanceof TL_pageBlockPhoto) {
            currentMap = captionTextPaints;
            textSize = AndroidUtilities.dp(14.0f);
            textColor = getGrayTextColor();
        } else if (parentBlock instanceof TL_pageBlockTitle) {
            currentMap = titleTextPaints;
            textSize = AndroidUtilities.dp(24.0f);
            textColor = getTextColor();
        } else if (parentBlock instanceof TL_pageBlockAuthorDate) {
            currentMap = authorTextPaints;
            textSize = AndroidUtilities.dp(14.0f);
            textColor = getGrayTextColor();
        } else if (parentBlock instanceof TL_pageBlockFooter) {
            currentMap = footerTextPaints;
            textSize = AndroidUtilities.dp(14.0f);
            textColor = getGrayTextColor();
        } else if (parentBlock instanceof TL_pageBlockSubtitle) {
            currentMap = subtitleTextPaints;
            textSize = AndroidUtilities.dp(21.0f);
            textColor = getTextColor();
        } else if (parentBlock instanceof TL_pageBlockHeader) {
            currentMap = headerTextPaints;
            textSize = AndroidUtilities.dp(21.0f);
            textColor = getTextColor();
        } else if (parentBlock instanceof TL_pageBlockSubheader) {
            if (parentBlock instanceof TL_pageBlockBlockquote) {
                if (parentBlock instanceof TL_pageBlockPullquote) {
                    if (parentBlock instanceof TL_pageBlockPreformatted) {
                        currentMap = preformattedTextPaints;
                        textSize = AndroidUtilities.dp(14.0f);
                        textColor = getTextColor();
                    } else if (parentBlock instanceof TL_pageBlockParagraph) {
                        if (parentBlock instanceof TL_pageBlockList) {
                            currentMap = listTextPaints;
                            textSize = AndroidUtilities.dp(15.0f);
                            textColor = getTextColor();
                        } else if (parentBlock instanceof TL_pageBlockEmbed) {
                            currentMap = embedTextPaints;
                            textSize = AndroidUtilities.dp(14.0f);
                            textColor = getGrayTextColor();
                        } else if (parentBlock instanceof TL_pageBlockSlideshow) {
                            currentMap = slideshowTextPaints;
                            textSize = AndroidUtilities.dp(14.0f);
                            textColor = getGrayTextColor();
                        } else if (parentBlock instanceof TL_pageBlockEmbedPost) {
                            currentMap = videoTextPaints;
                            textSize = AndroidUtilities.dp(14.0f);
                            textColor = getTextColor();
                        } else if (richText != null) {
                            currentMap = embedPostTextPaints;
                            textSize = AndroidUtilities.dp(14.0f);
                            textColor = getTextColor();
                        }
                    } else if (parentBlock.caption != parentRichText) {
                        currentMap = paragraphTextPaints;
                        textSize = AndroidUtilities.dp(16.0f);
                        textColor = getTextColor();
                    } else {
                        currentMap = embedPostCaptionTextPaints;
                        textSize = AndroidUtilities.dp(14.0f);
                        textColor = getGrayTextColor();
                    }
                }
            }
            if (parentBlock.text == parentRichText) {
                currentMap = quoteTextPaints;
                textSize = AndroidUtilities.dp(15.0f);
                textColor = getTextColor();
            } else if (parentBlock.caption == parentRichText) {
                currentMap = subquoteTextPaints;
                textSize = AndroidUtilities.dp(14.0f);
                textColor = getGrayTextColor();
            }
        } else {
            currentMap = subheaderTextPaints;
            textSize = AndroidUtilities.dp(18.0f);
            textColor = getTextColor();
        }
        if (currentMap != null) {
            paint = (TextPaint) currentMap.get(flags);
            if (paint == null) {
                paint = new TextPaint(1);
                if ((flags & 4) != 0) {
                    paint.setTypeface(AndroidUtilities.getTypeface("fonts/rmono.ttf"));
                } else if (parentBlock instanceof TL_pageBlockSubheader) {
                    if ((flags & 1) == 0) {
                    }
                    if ((flags & 1) != 0) {
                        paint.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
                    } else if ((flags & 2) != 0) {
                        paint.setTypeface(AndroidUtilities.getTypeface("fonts/ritalic.ttf"));
                    }
                } else {
                    if ((flags & 1) == 0) {
                    }
                    if ((flags & 1) != 0) {
                        paint.setTypeface(Typeface.create(C.SERIF_NAME, 1));
                    } else if ((flags & 2) == 0) {
                        paint.setTypeface(Typeface.create(C.SERIF_NAME, 0));
                    } else {
                        paint.setTypeface(Typeface.create(C.SERIF_NAME, 2));
                    }
                }
                if ((flags & 32) != 0) {
                    paint.setFlags(paint.getFlags() | 16);
                }
                if ((flags & 16) != 0) {
                    paint.setFlags(paint.getFlags() | 8);
                }
                if ((flags & 8) != 0) {
                    paint.setFlags(paint.getFlags() | 8);
                    textColor = getTextColor();
                }
                paint.setColor(textColor);
                currentMap.put(flags, paint);
            }
            paint.setTextSize((float) (textSize + additionalSize));
            return paint;
        }
        if (errorTextPaint == null) {
            errorTextPaint = new TextPaint(1);
            errorTextPaint.setColor(-65536);
        }
        errorTextPaint.setTextSize((float) AndroidUtilities.dp(14.0f));
        return errorTextPaint;
    }

    private StaticLayout createLayoutForText(CharSequence plainText, RichText richText, int width, PageBlock parentBlock) {
        ArticleViewer articleViewer = this;
        Object obj = plainText;
        RichText richText2 = richText;
        PageBlock pageBlock = parentBlock;
        if (obj == null && (richText2 == null || (richText2 instanceof TL_textEmpty))) {
            return null;
        }
        CharSequence text;
        int color = getSelectedColor();
        if (quoteLinePaint == null) {
            quoteLinePaint = new Paint();
            quoteLinePaint.setColor(getTextColor());
            preformattedBackgroundPaint = new Paint();
            if (color == 0) {
                preformattedBackgroundPaint.setColor(-657156);
            } else if (color == 1) {
                preformattedBackgroundPaint.setColor(-1712440);
            } else if (color == 2) {
                preformattedBackgroundPaint.setColor(-14277082);
            }
            urlPaint = new Paint();
            if (color == 0) {
                urlPaint.setColor(-1315861);
            } else if (color == 1) {
                urlPaint.setColor(-1712440);
            } else if (color == 2) {
                urlPaint.setColor(-14277082);
            }
        }
        if (obj != null) {
            text = obj;
        } else {
            text = getText(richText2, richText2, pageBlock);
        }
        if (TextUtils.isEmpty(text)) {
            return null;
        }
        TextPaint paint;
        TextPaint paint2;
        if ((pageBlock instanceof TL_pageBlockEmbedPost) && richText2 == null) {
            if (pageBlock.author == obj) {
                if (embedPostAuthorPaint == null) {
                    embedPostAuthorPaint = new TextPaint(1);
                    embedPostAuthorPaint.setColor(getTextColor());
                }
                embedPostAuthorPaint.setTextSize((float) AndroidUtilities.dp(15.0f));
                paint = embedPostAuthorPaint;
            } else {
                if (embedPostDatePaint == null) {
                    embedPostDatePaint = new TextPaint(1);
                    if (color == 0) {
                        embedPostDatePaint.setColor(-7366752);
                    } else if (color == 1) {
                        embedPostDatePaint.setColor(-11711675);
                    } else if (color == 2) {
                        embedPostDatePaint.setColor(-10066330);
                    }
                }
                embedPostDatePaint.setTextSize((float) AndroidUtilities.dp(14.0f));
                paint = embedPostDatePaint;
            }
        } else if (pageBlock instanceof TL_pageBlockChannel) {
            if (channelNamePaint == null) {
                channelNamePaint = new TextPaint(1);
                channelNamePaint.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
            }
            if (articleViewer.channelBlock == null) {
                channelNamePaint.setColor(getTextColor());
            } else {
                channelNamePaint.setColor(-1);
            }
            channelNamePaint.setTextSize((float) AndroidUtilities.dp(15.0f));
            paint = channelNamePaint;
        } else if (!(pageBlock instanceof TL_pageBlockList) || obj == null) {
            paint = getTextPaint(richText2, richText2, pageBlock);
            paint2 = paint;
            if (!(pageBlock instanceof TL_pageBlockPullquote)) {
                if (richText2 != null || pageBlock == null || (pageBlock instanceof TL_pageBlockBlockquote) || richText2 != pageBlock.caption) {
                    return new StaticLayout(text, paint2, width, Alignment.ALIGN_NORMAL, 1.0f, (float) AndroidUtilities.dp(4.0f), false);
                }
            }
            return new StaticLayout(text, paint2, width, Alignment.ALIGN_CENTER, 1.0f, 0.0f, false);
        } else {
            int additionalSize;
            if (listTextPointerPaint == null) {
                listTextPointerPaint = new TextPaint(1);
                listTextPointerPaint.setColor(getTextColor());
            }
            if (articleViewer.selectedFontSize == 0) {
                additionalSize = -AndroidUtilities.dp(4.0f);
            } else if (articleViewer.selectedFontSize == 1) {
                additionalSize = -AndroidUtilities.dp(2.0f);
            } else if (articleViewer.selectedFontSize == 3) {
                additionalSize = AndroidUtilities.dp(2.0f);
            } else if (articleViewer.selectedFontSize == 4) {
                additionalSize = AndroidUtilities.dp(4.0f);
            } else {
                additionalSize = 0;
                listTextPointerPaint.setTextSize((float) (AndroidUtilities.dp(15.0f) + additionalSize));
                paint = listTextPointerPaint;
                paint2 = paint;
                if (pageBlock instanceof TL_pageBlockPullquote) {
                    if (richText2 != null) {
                    }
                    return new StaticLayout(text, paint2, width, Alignment.ALIGN_NORMAL, 1.0f, (float) AndroidUtilities.dp(4.0f), false);
                }
                return new StaticLayout(text, paint2, width, Alignment.ALIGN_CENTER, 1.0f, 0.0f, false);
            }
            listTextPointerPaint.setTextSize((float) (AndroidUtilities.dp(15.0f) + additionalSize));
            paint = listTextPointerPaint;
            paint2 = paint;
            if (pageBlock instanceof TL_pageBlockPullquote) {
                if (richText2 != null) {
                }
                return new StaticLayout(text, paint2, width, Alignment.ALIGN_NORMAL, 1.0f, (float) AndroidUtilities.dp(4.0f), false);
            }
            return new StaticLayout(text, paint2, width, Alignment.ALIGN_CENTER, 1.0f, 0.0f, false);
        }
        paint2 = paint;
        if (pageBlock instanceof TL_pageBlockPullquote) {
            if (richText2 != null) {
            }
            return new StaticLayout(text, paint2, width, Alignment.ALIGN_NORMAL, 1.0f, (float) AndroidUtilities.dp(4.0f), false);
        }
        return new StaticLayout(text, paint2, width, Alignment.ALIGN_CENTER, 1.0f, 0.0f, false);
    }

    private void drawLayoutLink(Canvas canvas, StaticLayout layout) {
        if (canvas != null) {
            if (this.pressedLinkOwnerLayout == layout) {
                if (this.pressedLink != null) {
                    canvas.drawPath(this.urlPath, urlPaint);
                } else if (this.drawBlockSelection && layout != null) {
                    float width;
                    float x;
                    if (layout.getLineCount() == 1) {
                        width = layout.getLineWidth(0);
                        x = layout.getLineLeft(0);
                    } else {
                        width = (float) layout.getWidth();
                        x = 0.0f;
                    }
                    canvas.drawRect(((float) (-AndroidUtilities.dp(2.0f))) + x, 0.0f, (x + width) + ((float) AndroidUtilities.dp(2.0f)), (float) layout.getHeight(), urlPaint);
                }
            }
        }
    }

    private boolean checkLayoutForLinks(MotionEvent event, View parentView, StaticLayout layout, int layoutX, int layoutY) {
        Throwable e;
        ArticleViewer articleViewer = this;
        View view = parentView;
        StaticLayout staticLayout = layout;
        int i = layoutX;
        int i2 = layoutY;
        if (view != null) {
            if (staticLayout != null) {
                boolean removeLink;
                boolean z;
                int x = (int) event.getX();
                int y = (int) event.getY();
                boolean removeLink2 = false;
                int i3;
                if (event.getAction() != 0) {
                    i3 = y;
                    removeLink = false;
                    if (event.getAction() != 1) {
                        z = true;
                        if (event.getAction() == 3) {
                            removeLink2 = true;
                        }
                        removeLink2 = removeLink;
                    } else if (articleViewer.pressedLink != null) {
                        removeLink2 = true;
                        String url = articleViewer.pressedLink.getUrl();
                        if (url != null) {
                            String anchor;
                            boolean isAnchor = false;
                            int lastIndexOf = url.lastIndexOf(35);
                            x = lastIndexOf;
                            if (lastIndexOf != -1) {
                                anchor = url.substring(x + 1);
                                if (url.toLowerCase().contains(articleViewer.currentPage.url.toLowerCase())) {
                                    Integer row = (Integer) articleViewer.anchors.get(anchor);
                                    if (row != null) {
                                        articleViewer.layoutManager.scrollToPositionWithOffset(row.intValue(), 0);
                                        isAnchor = true;
                                    }
                                }
                            } else {
                                anchor = null;
                            }
                            final String anchor2 = anchor;
                            if (!isAnchor && articleViewer.openUrlReqId == 0) {
                                z = true;
                                showProgressView(true);
                                final TL_messages_getWebPage req = new TL_messages_getWebPage();
                                req.url = articleViewer.pressedLink.getUrl();
                                req.hash = 0;
                                articleViewer.openUrlReqId = ConnectionsManager.getInstance(articleViewer.currentAccount).sendRequest(req, new RequestDelegate() {
                                    public void run(final TLObject response, TL_error error) {
                                        AndroidUtilities.runOnUIThread(new Runnable() {
                                            public void run() {
                                                if (ArticleViewer.this.openUrlReqId != 0) {
                                                    ArticleViewer.this.openUrlReqId = 0;
                                                    ArticleViewer.this.showProgressView(false);
                                                    if (ArticleViewer.this.isVisible) {
                                                        if ((response instanceof TL_webPage) && (((TL_webPage) response).cached_page instanceof TL_pageFull)) {
                                                            ArticleViewer.this.addPageToStack((TL_webPage) response, anchor2);
                                                        } else {
                                                            Browser.openUrl(ArticleViewer.this.parentActivity, req.url);
                                                        }
                                                    }
                                                }
                                            }
                                        });
                                    }
                                });
                            }
                        }
                        z = true;
                    }
                    if (removeLink2) {
                        articleViewer.pressedLink = null;
                        articleViewer.pressedLinkOwnerLayout = null;
                        articleViewer.pressedLinkOwnerView = null;
                        parentView.invalidate();
                    }
                    if (event.getAction() == 0) {
                        startCheckLongPress();
                    }
                    cancelCheckLongPress();
                    if (articleViewer.pressedLinkOwnerLayout == null) {
                        z = false;
                    }
                    return z;
                } else if (x < i || x > layout.getWidth() + i || y < i2 || y > layout.getHeight() + i2) {
                    i3 = y;
                    removeLink = false;
                    z = true;
                    removeLink2 = removeLink;
                    if (removeLink2) {
                        articleViewer.pressedLink = null;
                        articleViewer.pressedLinkOwnerLayout = null;
                        articleViewer.pressedLinkOwnerView = null;
                        parentView.invalidate();
                    }
                    if (event.getAction() == 0) {
                        startCheckLongPress();
                    }
                    if (!(event.getAction() == 0 || event.getAction() == 2)) {
                        cancelCheckLongPress();
                    }
                    if (articleViewer.pressedLinkOwnerLayout == null) {
                        z = false;
                    }
                    return z;
                } else {
                    articleViewer.pressedLinkOwnerLayout = staticLayout;
                    articleViewer.pressedLinkOwnerView = view;
                    articleViewer.pressedLayoutY = i2;
                    if (layout.getText() instanceof Spannable) {
                        int checkX = x - i;
                        try {
                            int line = staticLayout.getLineForVertical(y - i2);
                            int off = staticLayout.getOffsetForHorizontal(line, (float) checkX);
                            float left = staticLayout.getLineLeft(line);
                            if (left <= ((float) checkX) && left + staticLayout.getLineWidth(line) >= ((float) checkX)) {
                                Spannable buffer = (Spannable) layout.getText();
                                TextPaintUrlSpan[] link = (TextPaintUrlSpan[]) buffer.getSpans(off, off, TextPaintUrlSpan.class);
                                if (link != null && link.length > 0) {
                                    articleViewer.pressedLink = link[0];
                                    x = buffer.getSpanEnd(articleViewer.pressedLink);
                                    i2 = buffer.getSpanStart(articleViewer.pressedLink);
                                    i = 1;
                                    while (true) {
                                        i3 = y;
                                        try {
                                            if (i >= link.length) {
                                                break;
                                            }
                                            int end;
                                            TextPaintUrlSpan span = link[i];
                                            int end2 = buffer.getSpanEnd(span);
                                            Spannable buffer2 = buffer;
                                            buffer = buffer.getSpanStart(span);
                                            if (i2 <= buffer) {
                                                removeLink = removeLink2;
                                                end = end2;
                                                if (end <= x) {
                                                    continue;
                                                    i++;
                                                    y = i3;
                                                    buffer = buffer2;
                                                    removeLink2 = removeLink;
                                                }
                                            } else {
                                                removeLink = removeLink2;
                                                end = end2;
                                            }
                                            try {
                                                articleViewer.pressedLink = span;
                                                i2 = buffer;
                                                x = end;
                                                i++;
                                                y = i3;
                                                buffer = buffer2;
                                                removeLink2 = removeLink;
                                            } catch (Throwable e2) {
                                                e = e2;
                                            }
                                        } catch (Throwable e22) {
                                            removeLink = removeLink2;
                                            e = e22;
                                        }
                                    }
                                    removeLink = removeLink2;
                                    try {
                                        articleViewer.urlPath.setCurrentLayout(staticLayout, i2, 0.0f);
                                        staticLayout.getSelectionPath(i2, x, articleViewer.urlPath);
                                        parentView.invalidate();
                                    } catch (Throwable e222) {
                                        FileLog.e(e222);
                                    }
                                }
                            }
                            i3 = y;
                            removeLink = false;
                        } catch (Throwable e2222) {
                            int i4 = x;
                            i3 = y;
                            removeLink = false;
                            e = e2222;
                            FileLog.e(e);
                            z = true;
                            removeLink2 = removeLink;
                            if (removeLink2) {
                                articleViewer.pressedLink = null;
                                articleViewer.pressedLinkOwnerLayout = null;
                                articleViewer.pressedLinkOwnerView = null;
                                parentView.invalidate();
                            }
                            if (event.getAction() == 0) {
                                startCheckLongPress();
                            }
                            cancelCheckLongPress();
                            if (articleViewer.pressedLinkOwnerLayout == null) {
                                z = false;
                            }
                            return z;
                        }
                    }
                    i3 = y;
                    removeLink = false;
                }
                z = true;
                removeLink2 = removeLink;
                if (removeLink2) {
                    articleViewer.pressedLink = null;
                    articleViewer.pressedLinkOwnerLayout = null;
                    articleViewer.pressedLinkOwnerView = null;
                    parentView.invalidate();
                }
                if (event.getAction() == 0) {
                    startCheckLongPress();
                }
                cancelCheckLongPress();
                if (articleViewer.pressedLinkOwnerLayout == null) {
                    z = false;
                }
                return z;
            }
        }
        return false;
    }

    private Photo getPhotoWithId(long id) {
        if (this.currentPage != null) {
            if (this.currentPage.cached_page != null) {
                if (this.currentPage.photo != null && this.currentPage.photo.id == id) {
                    return this.currentPage.photo;
                }
                for (int a = 0; a < this.currentPage.cached_page.photos.size(); a++) {
                    Photo photo = (Photo) this.currentPage.cached_page.photos.get(a);
                    if (photo.id == id) {
                        return photo;
                    }
                }
                return null;
            }
        }
        return null;
    }

    private Document getDocumentWithId(long id) {
        if (this.currentPage != null) {
            if (this.currentPage.cached_page != null) {
                if (this.currentPage.document != null && this.currentPage.document.id == id) {
                    return this.currentPage.document;
                }
                for (int a = 0; a < this.currentPage.cached_page.documents.size(); a++) {
                    Document document = (Document) this.currentPage.cached_page.documents.get(a);
                    if (document.id == id) {
                        return document;
                    }
                }
                return null;
            }
        }
        return null;
    }

    private void updatePaintSize() {
        ApplicationLoader.applicationContext.getSharedPreferences("articles", 0).edit().putInt("font_size", this.selectedFontSize).commit();
        this.adapter.notifyDataSetChanged();
    }

    private void updateFontEntry(int flags, TextPaint paint, Typeface typefaceNormal, Typeface typefaceBoldItalic, Typeface typefaceBold, Typeface typefaceItalic) {
        if ((flags & 1) != 0 && (flags & 2) != 0) {
            paint.setTypeface(typefaceBoldItalic);
        } else if ((flags & 1) != 0) {
            paint.setTypeface(typefaceBold);
        } else if ((flags & 2) != 0) {
            paint.setTypeface(typefaceItalic);
        } else {
            paint.setTypeface(typefaceNormal);
        }
    }

    private int getSelectedColor() {
        int currentColor = this.selectedColor;
        if (!this.nightModeEnabled || currentColor == 2) {
            return currentColor;
        }
        if (Theme.selectedAutoNightType == 0) {
            int hour = Calendar.getInstance().get(11);
            if ((hour < 22 || hour > 24) && (hour < 0 || hour > 6)) {
                return currentColor;
            }
            return 2;
        } else if (Theme.isCurrentThemeNight()) {
            return 2;
        } else {
            return currentColor;
        }
    }

    public void setParentActivity(Activity activity, BaseFragment fragment) {
        Context context = activity;
        this.parentFragment = fragment;
        this.currentAccount = UserConfig.selectedAccount;
        this.leftImage.setCurrentAccount(this.currentAccount);
        this.rightImage.setCurrentAccount(this.currentAccount);
        this.centerImage.setCurrentAccount(this.currentAccount);
        NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.messagePlayingProgressDidChanged);
        NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.messagePlayingDidReset);
        NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.messagePlayingPlayStateChanged);
        NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.messagePlayingDidStarted);
        NotificationCenter.getGlobalInstance().addObserver(this, NotificationCenter.needSetDayNightTheme);
        if (this.parentActivity == context) {
            updatePaintColors();
            return;
        }
        r0.parentActivity = context;
        SharedPreferences sharedPreferences = ApplicationLoader.applicationContext.getSharedPreferences("articles", 0);
        r0.selectedFontSize = sharedPreferences.getInt("font_size", 2);
        r0.selectedFont = sharedPreferences.getInt("font_type", 0);
        r0.selectedColor = sharedPreferences.getInt("font_color", 0);
        r0.nightModeEnabled = sharedPreferences.getBoolean("nightModeEnabled", false);
        r0.backgroundPaint = new Paint();
        r0.layerShadowDrawable = activity.getResources().getDrawable(R.drawable.layer_shadow);
        r0.slideDotDrawable = activity.getResources().getDrawable(R.drawable.slide_dot_small);
        r0.slideDotBigDrawable = activity.getResources().getDrawable(R.drawable.slide_dot_big);
        r0.scrimPaint = new Paint();
        r0.windowView = new WindowView(context);
        r0.windowView.setWillNotDraw(false);
        r0.windowView.setClipChildren(true);
        r0.windowView.setFocusable(false);
        r0.containerView = new FrameLayout(context);
        r0.windowView.addView(r0.containerView, LayoutHelper.createFrame(-1, -1, 51));
        r0.containerView.setFitsSystemWindows(true);
        if (VERSION.SDK_INT >= 21) {
            r0.containerView.setOnApplyWindowInsetsListener(new OnApplyWindowInsetsListener() {
                @SuppressLint({"NewApi"})
                public WindowInsets onApplyWindowInsets(View v, WindowInsets insets) {
                    WindowInsets oldInsets = (WindowInsets) ArticleViewer.this.lastInsets;
                    ArticleViewer.this.lastInsets = insets;
                    if (oldInsets == null || !oldInsets.toString().equals(insets.toString())) {
                        ArticleViewer.this.windowView.requestLayout();
                    }
                    return insets.consumeSystemWindowInsets();
                }
            });
        }
        r0.containerView.setSystemUiVisibility(1028);
        r0.photoContainerBackground = new View(context);
        r0.photoContainerBackground.setVisibility(4);
        r0.photoContainerBackground.setBackgroundDrawable(r0.photoBackgroundDrawable);
        r0.windowView.addView(r0.photoContainerBackground, LayoutHelper.createFrame(-1, -1, 51));
        r0.animatingImageView = new ClippingImageView(context);
        r0.animatingImageView.setAnimationValues(r0.animationValues);
        r0.animatingImageView.setVisibility(8);
        r0.windowView.addView(r0.animatingImageView, LayoutHelper.createFrame(40, 40.0f));
        r0.photoContainerView = new FrameLayoutDrawer(context) {
            protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
                super.onLayout(changed, left, top, right, bottom);
                int y = (bottom - top) - ArticleViewer.this.captionTextView.getMeasuredHeight();
                if (ArticleViewer.this.bottomLayout.getVisibility() == 0) {
                    y -= ArticleViewer.this.bottomLayout.getMeasuredHeight();
                }
                ArticleViewer.this.captionTextView.layout(0, y, ArticleViewer.this.captionTextView.getMeasuredWidth(), ArticleViewer.this.captionTextView.getMeasuredHeight() + y);
            }
        };
        r0.photoContainerView.setVisibility(4);
        r0.photoContainerView.setWillNotDraw(false);
        r0.windowView.addView(r0.photoContainerView, LayoutHelper.createFrame(-1, -1, 51));
        r0.fullscreenVideoContainer = new FrameLayout(context);
        r0.fullscreenVideoContainer.setBackgroundColor(Theme.ACTION_BAR_VIDEO_EDIT_COLOR);
        r0.fullscreenVideoContainer.setVisibility(4);
        r0.windowView.addView(r0.fullscreenVideoContainer, LayoutHelper.createFrame(-1, -1.0f));
        r0.fullscreenAspectRatioView = new AspectRatioFrameLayout(context);
        r0.fullscreenAspectRatioView.setVisibility(8);
        r0.fullscreenVideoContainer.addView(r0.fullscreenAspectRatioView, LayoutHelper.createFrame(-1, -1, 17));
        r0.fullscreenTextureView = new TextureView(context);
        if (VERSION.SDK_INT >= 21) {
            r0.barBackground = new View(context);
            r0.barBackground.setBackgroundColor(Theme.ACTION_BAR_VIDEO_EDIT_COLOR);
            r0.windowView.addView(r0.barBackground);
        }
        r0.listView = new RecyclerListView(context) {
            protected void onLayout(boolean changed, int l, int t, int r, int b) {
                super.onLayout(changed, l, t, r, b);
                int count = getChildCount();
                for (int a = 0; a < count; a++) {
                    View child = getChildAt(a);
                    if ((child.getTag() instanceof Integer) && ((Integer) child.getTag()).intValue() == 90 && child.getBottom() < getMeasuredHeight()) {
                        int height = getMeasuredHeight();
                        child.layout(0, height - child.getMeasuredHeight(), child.getMeasuredWidth(), height);
                        return;
                    }
                }
            }
        };
        RecyclerListView recyclerListView = r0.listView;
        LayoutManager linearLayoutManager = new LinearLayoutManager(r0.parentActivity, 1, false);
        r0.layoutManager = linearLayoutManager;
        recyclerListView.setLayoutManager(linearLayoutManager);
        recyclerListView = r0.listView;
        Adapter webpageAdapter = new WebpageAdapter(r0.parentActivity);
        r0.adapter = webpageAdapter;
        recyclerListView.setAdapter(webpageAdapter);
        r0.listView.setClipToPadding(false);
        r0.listView.setPadding(0, AndroidUtilities.dp(56.0f), 0, 0);
        r0.listView.setTopGlowOffset(AndroidUtilities.dp(56.0f));
        r0.containerView.addView(r0.listView, LayoutHelper.createFrame(-1, -1.0f));
        r0.listView.setOnItemLongClickListener(new OnItemLongClickListener() {
            public boolean onItemClick(View view, int position) {
                return false;
            }
        });
        r0.listView.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(View view, int position) {
                if (position != ArticleViewer.this.blocks.size() || ArticleViewer.this.currentPage == null) {
                    if (position >= 0 && position < ArticleViewer.this.blocks.size()) {
                        PageBlock pageBlock = (PageBlock) ArticleViewer.this.blocks.get(position);
                        if (pageBlock instanceof TL_pageBlockChannel) {
                            MessagesController.getInstance(ArticleViewer.this.currentAccount).openByUserName(pageBlock.channel.username, ArticleViewer.this.parentFragment, 2);
                            ArticleViewer.this.close(false, true);
                        }
                    }
                } else if (ArticleViewer.this.previewsReqId == 0) {
                    TLObject object = MessagesController.getInstance(ArticleViewer.this.currentAccount).getUserOrChat("previews");
                    if (object instanceof TL_user) {
                        ArticleViewer.this.openPreviewsChat((User) object, ArticleViewer.this.currentPage.id);
                    } else {
                        final int currentAccount = UserConfig.selectedAccount;
                        final long pageId = ArticleViewer.this.currentPage.id;
                        ArticleViewer.this.showProgressView(true);
                        TL_contacts_resolveUsername req = new TL_contacts_resolveUsername();
                        req.username = "previews";
                        ArticleViewer.this.previewsReqId = ConnectionsManager.getInstance(currentAccount).sendRequest(req, new RequestDelegate() {
                            public void run(final TLObject response, TL_error error) {
                                AndroidUtilities.runOnUIThread(new Runnable() {
                                    public void run() {
                                        if (ArticleViewer.this.previewsReqId != 0) {
                                            ArticleViewer.this.previewsReqId = 0;
                                            ArticleViewer.this.showProgressView(false);
                                            if (response != null) {
                                                TL_contacts_resolvedPeer res = response;
                                                MessagesController.getInstance(currentAccount).putUsers(res.users, false);
                                                MessagesStorage.getInstance(currentAccount).putUsersAndChats(res.users, res.chats, false, true);
                                                if (!res.users.isEmpty()) {
                                                    ArticleViewer.this.openPreviewsChat((User) res.users.get(0), pageId);
                                                }
                                            }
                                        }
                                    }
                                });
                            }
                        });
                    }
                }
            }
        });
        r0.listView.setOnScrollListener(new OnScrollListener() {
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (ArticleViewer.this.listView.getChildCount() != 0) {
                    ArticleViewer.this.headerView.invalidate();
                    ArticleViewer.this.checkScroll(dy);
                }
            }
        });
        r0.headerPaint.setColor(Theme.ACTION_BAR_VIDEO_EDIT_COLOR);
        r0.headerProgressPaint.setColor(-14408666);
        r0.headerView = new FrameLayout(context) {
            protected void onDraw(Canvas canvas) {
                int width = getMeasuredWidth();
                int height = getMeasuredHeight();
                canvas.drawRect(0.0f, 0.0f, (float) width, (float) height, ArticleViewer.this.headerPaint);
                if (ArticleViewer.this.layoutManager != null) {
                    View view;
                    int first = ArticleViewer.this.layoutManager.findFirstVisibleItemPosition();
                    int last = ArticleViewer.this.layoutManager.findLastVisibleItemPosition();
                    int count = ArticleViewer.this.layoutManager.getItemCount();
                    if (last >= count - 2) {
                        view = ArticleViewer.this.layoutManager.findViewByPosition(count - 2);
                    } else {
                        view = ArticleViewer.this.layoutManager.findViewByPosition(first);
                    }
                    if (view != null) {
                        float viewProgress;
                        float itemProgress = ((float) width) / ((float) (count - 1));
                        int childCount = ArticleViewer.this.layoutManager.getChildCount();
                        float viewHeight = (float) view.getMeasuredHeight();
                        if (last >= count - 2) {
                            viewProgress = ((((float) ((count - 2) - first)) * itemProgress) * ((float) (ArticleViewer.this.listView.getMeasuredHeight() - view.getTop()))) / viewHeight;
                        } else {
                            viewProgress = (1.0f - ((((float) Math.min(0, view.getTop() - ArticleViewer.this.listView.getPaddingTop())) + viewHeight) / viewHeight)) * itemProgress;
                        }
                        float f = (float) height;
                        float f2 = f;
                        canvas.drawRect(0.0f, 0.0f, (((float) first) * itemProgress) + viewProgress, f2, ArticleViewer.this.headerProgressPaint);
                    }
                }
            }
        };
        r0.headerView.setOnTouchListener(new OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });
        r0.headerView.setWillNotDraw(false);
        r0.containerView.addView(r0.headerView, LayoutHelper.createFrame(-1, 56.0f));
        r0.backButton = new ImageView(context);
        r0.backButton.setScaleType(ScaleType.CENTER);
        r0.backDrawable = new BackDrawable(false);
        r0.backDrawable.setAnimationTime(200.0f);
        r0.backDrawable.setColor(-5000269);
        r0.backDrawable.setRotated(false);
        r0.backButton.setImageDrawable(r0.backDrawable);
        r0.backButton.setBackgroundDrawable(Theme.createSelectorDrawable(Theme.ACTION_BAR_WHITE_SELECTOR_COLOR));
        r0.headerView.addView(r0.backButton, LayoutHelper.createFrame(54, 56.0f));
        r0.backButton.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                ArticleViewer.this.close(true, true);
            }
        });
        LinearLayout settingsContainer = new LinearLayout(r0.parentActivity);
        settingsContainer.setPadding(0, AndroidUtilities.dp(4.0f), 0, AndroidUtilities.dp(4.0f));
        settingsContainer.setOrientation(1);
        int a = 0;
        while (a < 3) {
            r0.colorCells[a] = new ColorCell(r0.parentActivity);
            switch (a) {
                case 0:
                    r0.nightModeImageView = new ImageView(r0.parentActivity);
                    r0.nightModeImageView.setScaleType(ScaleType.CENTER);
                    r0.nightModeImageView.setImageResource(R.drawable.moon);
                    ImageView imageView = r0.nightModeImageView;
                    int i = (!r0.nightModeEnabled || r0.selectedColor == 2) ? -3355444 : -15428119;
                    imageView.setColorFilter(new PorterDuffColorFilter(i, Mode.MULTIPLY));
                    r0.nightModeImageView.setBackgroundDrawable(Theme.createSelectorDrawable(251658240));
                    r0.colorCells[a].addView(r0.nightModeImageView, LayoutHelper.createFrame(48, 48, 48 | (LocaleController.isRTL ? 3 : 5)));
                    r0.nightModeImageView.setOnClickListener(new OnClickListener() {
                        public void onClick(View v) {
                            ArticleViewer.this.nightModeEnabled = ArticleViewer.this.nightModeEnabled ^ 1;
                            ApplicationLoader.applicationContext.getSharedPreferences("articles", 0).edit().putBoolean("nightModeEnabled", ArticleViewer.this.nightModeEnabled).commit();
                            ArticleViewer.this.updateNightModeButton();
                            ArticleViewer.this.updatePaintColors();
                            ArticleViewer.this.adapter.notifyDataSetChanged();
                            if (ArticleViewer.this.nightModeEnabled) {
                                ArticleViewer.this.showNightModeHint();
                            }
                        }
                    });
                    r0.colorCells[a].setTextAndColor(LocaleController.getString("ColorWhite", R.string.ColorWhite), -1);
                    break;
                case 1:
                    r0.colorCells[a].setTextAndColor(LocaleController.getString("ColorSepia", R.string.ColorSepia), -1382967);
                    break;
                case 2:
                    r0.colorCells[a].setTextAndColor(LocaleController.getString("ColorDark", R.string.ColorDark), -14474461);
                    break;
                default:
                    break;
            }
            r0.colorCells[a].select(a == r0.selectedColor);
            r0.colorCells[a].setTag(Integer.valueOf(a));
            r0.colorCells[a].setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    int num = ((Integer) v.getTag()).intValue();
                    ArticleViewer.this.selectedColor = num;
                    int a = 0;
                    while (a < 3) {
                        ArticleViewer.this.colorCells[a].select(a == num);
                        a++;
                    }
                    ArticleViewer.this.updateNightModeButton();
                    ArticleViewer.this.updatePaintColors();
                    ArticleViewer.this.adapter.notifyDataSetChanged();
                }
            });
            settingsContainer.addView(r0.colorCells[a], LayoutHelper.createLinear(-1, 48));
            a++;
        }
        updateNightModeButton();
        View divider = new View(r0.parentActivity);
        divider.setBackgroundColor(-2039584);
        settingsContainer.addView(divider, LayoutHelper.createLinear(-1, 1, 15.0f, 4.0f, 15.0f, 4.0f));
        divider.getLayoutParams().height = 1;
        int a2 = 0;
        while (a2 < 2) {
            r0.fontCells[a2] = new FontCell(r0, r0.parentActivity);
            switch (a2) {
                case 0:
                    r0.fontCells[a2].setTextAndTypeface("Roboto", Typeface.DEFAULT);
                    break;
                case 1:
                    r0.fontCells[a2].setTextAndTypeface("Serif", Typeface.SERIF);
                    break;
                default:
                    break;
            }
            r0.fontCells[a2].select(a2 == r0.selectedFont);
            r0.fontCells[a2].setTag(Integer.valueOf(a2));
            r0.fontCells[a2].setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    int num = ((Integer) v.getTag()).intValue();
                    ArticleViewer.this.selectedFont = num;
                    int a = 0;
                    while (a < 2) {
                        ArticleViewer.this.fontCells[a].select(a == num);
                        a++;
                    }
                    ArticleViewer.this.updatePaintFonts();
                    ArticleViewer.this.adapter.notifyDataSetChanged();
                }
            });
            settingsContainer.addView(r0.fontCells[a2], LayoutHelper.createLinear(-1, 48));
            a2++;
        }
        divider = new View(r0.parentActivity);
        divider.setBackgroundColor(-2039584);
        settingsContainer.addView(divider, LayoutHelper.createLinear(-1, 1, 15.0f, 4.0f, 15.0f, 4.0f));
        divider.getLayoutParams().height = 1;
        TextView textView = new TextView(r0.parentActivity);
        textView.setTextColor(-14606047);
        textView.setTextSize(1, 16.0f);
        textView.setLines(1);
        textView.setMaxLines(1);
        textView.setSingleLine(true);
        textView.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
        textView.setGravity((LocaleController.isRTL ? 5 : 3) | 48);
        textView.setText(LocaleController.getString("FontSize", R.string.FontSize));
        settingsContainer.addView(textView, LayoutHelper.createLinear(-2, -2, (LocaleController.isRTL ? 5 : 3) | 48, 17, 12, 17, 0));
        settingsContainer.addView(new SizeChooseView(r0.parentActivity), LayoutHelper.createLinear(-1, 38, 0.0f, 0.0f, 0.0f, 1.0f));
        r0.settingsButton = new ActionBarMenuItem(r0.parentActivity, null, Theme.ACTION_BAR_WHITE_SELECTOR_COLOR, -1);
        r0.settingsButton.setPopupAnimationEnabled(false);
        r0.settingsButton.setLayoutInScreen(true);
        TextView textView2 = new TextView(r0.parentActivity);
        textView2.setTextSize(1, 18.0f);
        textView2.setText("Aa");
        textView2.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
        textView2.setTextColor(-5000269);
        textView2.setGravity(17);
        r0.settingsButton.addView(textView2, LayoutHelper.createFrame(-1, -1.0f));
        r0.settingsButton.addSubItem(settingsContainer, AndroidUtilities.dp(220.0f), -2);
        r0.settingsButton.redrawPopup(-1);
        r0.headerView.addView(r0.settingsButton, LayoutHelper.createFrame(48, 56.0f, 53, 0.0f, 0.0f, 56.0f, 0.0f));
        r0.shareContainer = new FrameLayout(context);
        r0.headerView.addView(r0.shareContainer, LayoutHelper.createFrame(48, 56, 53));
        r0.shareContainer.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                if (ArticleViewer.this.currentPage != null) {
                    if (ArticleViewer.this.parentActivity != null) {
                        ArticleViewer.this.showDialog(new ShareAlert(ArticleViewer.this.parentActivity, null, ArticleViewer.this.currentPage.url, false, ArticleViewer.this.currentPage.url, true));
                        ArticleViewer.this.hideActionBar();
                    }
                }
            }
        });
        r0.shareButton = new ImageView(context);
        r0.shareButton.setScaleType(ScaleType.CENTER);
        r0.shareButton.setImageResource(R.drawable.ic_share_article);
        r0.shareButton.setBackgroundDrawable(Theme.createSelectorDrawable(Theme.ACTION_BAR_WHITE_SELECTOR_COLOR));
        r0.shareContainer.addView(r0.shareButton, LayoutHelper.createFrame(48, 56.0f));
        r0.progressView = new ContextProgressView(context, 2);
        r0.progressView.setVisibility(8);
        r0.shareContainer.addView(r0.progressView, LayoutHelper.createFrame(48, 56.0f));
        r0.windowLayoutParams = new LayoutParams();
        r0.windowLayoutParams.height = -1;
        r0.windowLayoutParams.format = -3;
        r0.windowLayoutParams.width = -1;
        r0.windowLayoutParams.gravity = 51;
        r0.windowLayoutParams.type = 99;
        if (VERSION.SDK_INT >= 21) {
            r0.windowLayoutParams.flags = -2147417848;
        } else {
            r0.windowLayoutParams.flags = 8;
        }
        if (progressDrawables == null) {
            progressDrawables = new Drawable[4];
            progressDrawables[0] = r0.parentActivity.getResources().getDrawable(R.drawable.circle_big);
            progressDrawables[1] = r0.parentActivity.getResources().getDrawable(R.drawable.cancel_big);
            progressDrawables[2] = r0.parentActivity.getResources().getDrawable(R.drawable.load_big);
            progressDrawables[3] = r0.parentActivity.getResources().getDrawable(R.drawable.play_big);
        }
        r0.scroller = new Scroller(context);
        r0.blackPaint.setColor(Theme.ACTION_BAR_VIDEO_EDIT_COLOR);
        r0.actionBar = new ActionBar(context);
        r0.actionBar.setBackgroundColor(Theme.ACTION_BAR_PHOTO_VIEWER_COLOR);
        r0.actionBar.setOccupyStatusBar(false);
        r0.actionBar.setTitleColor(-1);
        r0.actionBar.setItemsBackgroundColor(Theme.ACTION_BAR_WHITE_SELECTOR_COLOR, false);
        r0.actionBar.setBackButtonImage(R.drawable.ic_ab_back);
        r0.actionBar.setTitle(LocaleController.formatString("Of", R.string.Of, Integer.valueOf(1), Integer.valueOf(1)));
        r0.photoContainerView.addView(r0.actionBar, LayoutHelper.createFrame(-1, -2.0f));
        r0.actionBar.setActionBarMenuOnItemClick(new ActionBarMenuOnItemClick() {
            public void onItemClick(int id) {
                if (id == -1) {
                    ArticleViewer.this.closePhoto(true);
                } else if (id == 1) {
                    if (VERSION.SDK_INT < 23 || ArticleViewer.this.parentActivity.checkSelfPermission("android.permission.WRITE_EXTERNAL_STORAGE") == 0) {
                        File f = ArticleViewer.this.getMediaFile(ArticleViewer.this.currentIndex);
                        if (f == null || !f.exists()) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(ArticleViewer.this.parentActivity);
                            builder.setTitle(LocaleController.getString("AppName", R.string.AppName));
                            builder.setPositiveButton(LocaleController.getString("OK", R.string.OK), null);
                            builder.setMessage(LocaleController.getString("PleaseDownload", R.string.PleaseDownload));
                            ArticleViewer.this.showDialog(builder.create());
                        } else {
                            MediaController.saveFile(f.toString(), ArticleViewer.this.parentActivity, ArticleViewer.this.isMediaVideo(ArticleViewer.this.currentIndex), null, null);
                        }
                    } else {
                        ArticleViewer.this.parentActivity.requestPermissions(new String[]{"android.permission.WRITE_EXTERNAL_STORAGE"}, 4);
                    }
                } else if (id == 2) {
                    ArticleViewer.this.onSharePressed();
                } else if (id == 3) {
                    try {
                        AndroidUtilities.openForView(ArticleViewer.this.getMedia(ArticleViewer.this.currentIndex), ArticleViewer.this.parentActivity);
                        ArticleViewer.this.closePhoto(false);
                    } catch (Throwable e) {
                        FileLog.e(e);
                    }
                }
            }

            public boolean canOpenMenu() {
                File f = ArticleViewer.this.getMediaFile(ArticleViewer.this.currentIndex);
                return f != null && f.exists();
            }
        });
        ActionBarMenu menu = r0.actionBar.createMenu();
        menu.addItem(2, (int) R.drawable.share);
        r0.menuItem = menu.addItem(0, (int) R.drawable.ic_ab_other);
        r0.menuItem.setLayoutInScreen(true);
        r0.menuItem.addSubItem(3, LocaleController.getString("OpenInExternalApp", R.string.OpenInExternalApp));
        r0.menuItem.addSubItem(1, LocaleController.getString("SaveToGallery", R.string.SaveToGallery));
        r0.bottomLayout = new FrameLayout(r0.parentActivity);
        r0.bottomLayout.setBackgroundColor(Theme.ACTION_BAR_PHOTO_VIEWER_COLOR);
        r0.photoContainerView.addView(r0.bottomLayout, LayoutHelper.createFrame(-1, 48, 83));
        r0.captionTextViewOld = new TextView(context);
        r0.captionTextViewOld.setMaxLines(10);
        r0.captionTextViewOld.setBackgroundColor(Theme.ACTION_BAR_PHOTO_VIEWER_COLOR);
        r0.captionTextViewOld.setPadding(AndroidUtilities.dp(20.0f), AndroidUtilities.dp(8.0f), AndroidUtilities.dp(20.0f), AndroidUtilities.dp(8.0f));
        r0.captionTextViewOld.setLinkTextColor(-1);
        r0.captionTextViewOld.setTextColor(-1);
        r0.captionTextViewOld.setGravity(19);
        r0.captionTextViewOld.setTextSize(1, 16.0f);
        r0.captionTextViewOld.setVisibility(4);
        r0.photoContainerView.addView(r0.captionTextViewOld, LayoutHelper.createFrame(-1, -2, 83));
        TextView textView3 = new TextView(context);
        r0.captionTextViewNew = textView3;
        r0.captionTextView = textView3;
        r0.captionTextViewNew.setMaxLines(10);
        r0.captionTextViewNew.setBackgroundColor(Theme.ACTION_BAR_PHOTO_VIEWER_COLOR);
        r0.captionTextViewNew.setPadding(AndroidUtilities.dp(20.0f), AndroidUtilities.dp(8.0f), AndroidUtilities.dp(20.0f), AndroidUtilities.dp(8.0f));
        r0.captionTextViewNew.setLinkTextColor(-1);
        r0.captionTextViewNew.setTextColor(-1);
        r0.captionTextViewNew.setGravity(19);
        r0.captionTextViewNew.setTextSize(1, 16.0f);
        r0.captionTextViewNew.setVisibility(4);
        r0.photoContainerView.addView(r0.captionTextViewNew, LayoutHelper.createFrame(-1, -2, 83));
        r0.radialProgressViews[0] = new RadialProgressView(context, r0.photoContainerView);
        r0.radialProgressViews[0].setBackgroundState(0, false);
        r0.radialProgressViews[1] = new RadialProgressView(context, r0.photoContainerView);
        r0.radialProgressViews[1].setBackgroundState(0, false);
        r0.radialProgressViews[2] = new RadialProgressView(context, r0.photoContainerView);
        r0.radialProgressViews[2].setBackgroundState(0, false);
        r0.videoPlayerSeekbar = new SeekBar(context);
        r0.videoPlayerSeekbar.setColors(1728053247, 1728053247, -2764585, -1, -1);
        r0.videoPlayerSeekbar.setDelegate(new SeekBarDelegate() {
            public void onSeekBarDrag(float progress) {
                if (ArticleViewer.this.videoPlayer != null) {
                    ArticleViewer.this.videoPlayer.seekTo((long) ((int) (((float) ArticleViewer.this.videoPlayer.getDuration()) * progress)));
                }
            }
        });
        r0.videoPlayerControlFrameLayout = new FrameLayout(context) {
            public boolean onTouchEvent(MotionEvent event) {
                int x = (int) event.getX();
                int y = (int) event.getY();
                if (!ArticleViewer.this.videoPlayerSeekbar.onTouch(event.getAction(), event.getX() - ((float) AndroidUtilities.dp(48.0f)), event.getY())) {
                    return super.onTouchEvent(event);
                }
                getParent().requestDisallowInterceptTouchEvent(true);
                invalidate();
                return true;
            }

            protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
                long duration;
                super.onMeasure(widthMeasureSpec, heightMeasureSpec);
                if (ArticleViewer.this.videoPlayer != null) {
                    duration = ArticleViewer.this.videoPlayer.getDuration();
                    if (duration == C.TIME_UNSET) {
                        duration = 0;
                    }
                } else {
                    duration = 0;
                }
                duration /= 1000;
                ArticleViewer.this.videoPlayerSeekbar.setSize((getMeasuredWidth() - AndroidUtilities.dp(64.0f)) - ((int) Math.ceil((double) ArticleViewer.this.videoPlayerTime.getPaint().measureText(String.format("%02d:%02d / %02d:%02d", new Object[]{Long.valueOf(duration / 60), Long.valueOf(duration % 60), Long.valueOf(duration / 60), Long.valueOf(duration % 60)})))), getMeasuredHeight());
            }

            protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
                super.onLayout(changed, left, top, right, bottom);
                float progress = 0.0f;
                if (ArticleViewer.this.videoPlayer != null) {
                    progress = ((float) ArticleViewer.this.videoPlayer.getCurrentPosition()) / ((float) ArticleViewer.this.videoPlayer.getDuration());
                }
                ArticleViewer.this.videoPlayerSeekbar.setProgress(progress);
            }

            protected void onDraw(Canvas canvas) {
                canvas.save();
                canvas.translate((float) AndroidUtilities.dp(48.0f), 0.0f);
                ArticleViewer.this.videoPlayerSeekbar.draw(canvas);
                canvas.restore();
            }
        };
        r0.videoPlayerControlFrameLayout.setWillNotDraw(false);
        r0.bottomLayout.addView(r0.videoPlayerControlFrameLayout, LayoutHelper.createFrame(-1, -1, 51));
        r0.videoPlayButton = new ImageView(context);
        r0.videoPlayButton.setScaleType(ScaleType.CENTER);
        r0.videoPlayerControlFrameLayout.addView(r0.videoPlayButton, LayoutHelper.createFrame(48, 48, 51));
        r0.videoPlayButton.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                if (ArticleViewer.this.videoPlayer == null) {
                    return;
                }
                if (ArticleViewer.this.isPlaying) {
                    ArticleViewer.this.videoPlayer.pause();
                } else {
                    ArticleViewer.this.videoPlayer.play();
                }
            }
        });
        r0.videoPlayerTime = new TextView(context);
        r0.videoPlayerTime.setTextColor(-1);
        r0.videoPlayerTime.setGravity(16);
        r0.videoPlayerTime.setTextSize(1, 13.0f);
        r0.videoPlayerControlFrameLayout.addView(r0.videoPlayerTime, LayoutHelper.createFrame(-2, -1.0f, 53, 0.0f, 0.0f, 8.0f, 0.0f));
        r0.gestureDetector = new GestureDetector(context, r0);
        r0.gestureDetector.setOnDoubleTapListener(r0);
        r0.centerImage.setParentView(r0.photoContainerView);
        r0.centerImage.setCrossfadeAlpha((byte) 2);
        r0.centerImage.setInvalidateAll(true);
        r0.leftImage.setParentView(r0.photoContainerView);
        r0.leftImage.setCrossfadeAlpha((byte) 2);
        r0.leftImage.setInvalidateAll(true);
        r0.rightImage.setParentView(r0.photoContainerView);
        r0.rightImage.setCrossfadeAlpha((byte) 2);
        r0.rightImage.setInvalidateAll(true);
        updatePaintColors();
    }

    private void showNightModeHint() {
        if (this.parentActivity != null && this.nightModeHintView == null) {
            if (this.nightModeEnabled) {
                this.nightModeHintView = new FrameLayout(this.parentActivity);
                this.nightModeHintView.setBackgroundColor(Theme.ACTION_BAR_MEDIA_PICKER_COLOR);
                this.containerView.addView(this.nightModeHintView, LayoutHelper.createFrame(-1, -2, 83));
                ImageView nightModeImageView = new ImageView(this.parentActivity);
                nightModeImageView.setScaleType(ScaleType.CENTER);
                nightModeImageView.setImageResource(R.drawable.moon);
                int i = 3;
                this.nightModeHintView.addView(nightModeImageView, LayoutHelper.createFrame(56, 56, (LocaleController.isRTL ? 5 : 3) | 16));
                TextView textView = new TextView(this.parentActivity);
                textView.setText(LocaleController.getString("InstantViewNightMode", R.string.InstantViewNightMode));
                textView.setTextColor(-1);
                textView.setTextSize(1, 15.0f);
                FrameLayout frameLayout = this.nightModeHintView;
                if (LocaleController.isRTL) {
                    i = 5;
                }
                int i2 = i | 48;
                int i3 = 10;
                float f = (float) (LocaleController.isRTL ? 10 : 56);
                if (LocaleController.isRTL) {
                    i3 = 56;
                }
                frameLayout.addView(textView, LayoutHelper.createFrame(-1, -1.0f, i2, f, 11.0f, (float) i3, 12.0f));
                AnimatorSet animatorSet = new AnimatorSet();
                Animator[] animatorArr = new Animator[1];
                animatorArr[0] = ObjectAnimator.ofFloat(this.nightModeHintView, "translationY", new float[]{(float) AndroidUtilities.dp(100.0f), 0.0f});
                animatorSet.playTogether(animatorArr);
                animatorSet.setInterpolator(new DecelerateInterpolator(1.5f));
                animatorSet.addListener(new AnimatorListenerAdapter() {
                    public void onAnimationEnd(Animator animation) {
                        AndroidUtilities.runOnUIThread(new Runnable() {
                            public void run() {
                                AnimatorSet animatorSet = new AnimatorSet();
                                Animator[] animatorArr = new Animator[1];
                                animatorArr[0] = ObjectAnimator.ofFloat(ArticleViewer.this.nightModeHintView, "translationY", new float[]{(float) AndroidUtilities.dp(100.0f)});
                                animatorSet.playTogether(animatorArr);
                                animatorSet.setInterpolator(new DecelerateInterpolator(1.5f));
                                animatorSet.setDuration(250);
                                animatorSet.start();
                            }
                        }, 3000);
                    }
                });
                animatorSet.setDuration(250);
                animatorSet.start();
            }
        }
    }

    private void updateNightModeButton() {
        this.nightModeImageView.setEnabled(this.selectedColor != 2);
        this.nightModeImageView.setAlpha(this.selectedColor == 2 ? 0.5f : 1.0f);
        ImageView imageView = this.nightModeImageView;
        int i = (!this.nightModeEnabled || this.selectedColor == 2) ? -3355444 : -15428119;
        imageView.setColorFilter(new PorterDuffColorFilter(i, Mode.MULTIPLY));
    }

    private void checkScroll(int dy) {
        int maxHeight = AndroidUtilities.dp(1113587712);
        int minHeight = Math.max(AndroidUtilities.statusBarHeight, AndroidUtilities.dp(24.0f));
        float heightDiff = (float) (maxHeight - minHeight);
        int newHeight = this.currentHeaderHeight - dy;
        if (newHeight < minHeight) {
            newHeight = minHeight;
        } else if (newHeight > maxHeight) {
            newHeight = maxHeight;
        }
        this.currentHeaderHeight = newHeight;
        float scale = 0.8f + ((((float) (this.currentHeaderHeight - minHeight)) / heightDiff) * 0.2f);
        int scaledHeight = (int) (((float) maxHeight) * scale);
        this.backButton.setScaleX(scale);
        this.backButton.setScaleY(scale);
        this.backButton.setTranslationY((float) ((maxHeight - this.currentHeaderHeight) / 2));
        this.shareContainer.setScaleX(scale);
        this.shareContainer.setScaleY(scale);
        this.settingsButton.setScaleX(scale);
        this.settingsButton.setScaleY(scale);
        this.shareContainer.setTranslationY((float) ((maxHeight - this.currentHeaderHeight) / 2));
        this.settingsButton.setTranslationY((float) ((maxHeight - this.currentHeaderHeight) / 2));
        this.headerView.setTranslationY((float) (this.currentHeaderHeight - maxHeight));
        this.listView.setTopGlowOffset(this.currentHeaderHeight);
    }

    private void openPreviewsChat(User user, long wid) {
        if (user != null) {
            if (this.parentActivity != null) {
                Bundle args = new Bundle();
                args.putInt("user_id", user.id);
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("webpage");
                stringBuilder.append(wid);
                args.putString("botUser", stringBuilder.toString());
                ((LaunchActivity) this.parentActivity).presentFragment(new ChatActivity(args), false, true);
                close(false, true);
            }
        }
    }

    private void addAllMediaFromBlock(PageBlock block) {
        if (!(block instanceof TL_pageBlockPhoto)) {
            if (!(block instanceof TL_pageBlockVideo) || !isVideoBlock(block)) {
                int a = 0;
                int count;
                PageBlock innerBlock;
                if (block instanceof TL_pageBlockSlideshow) {
                    TL_pageBlockSlideshow slideshow = (TL_pageBlockSlideshow) block;
                    count = slideshow.items.size();
                    while (a < count) {
                        innerBlock = (PageBlock) slideshow.items.get(a);
                        if ((innerBlock instanceof TL_pageBlockPhoto) || ((innerBlock instanceof TL_pageBlockVideo) && isVideoBlock(innerBlock))) {
                            this.photoBlocks.add(innerBlock);
                        }
                        a++;
                    }
                    return;
                } else if (block instanceof TL_pageBlockCollage) {
                    TL_pageBlockCollage collage = (TL_pageBlockCollage) block;
                    count = collage.items.size();
                    while (a < count) {
                        innerBlock = (PageBlock) collage.items.get(a);
                        if ((innerBlock instanceof TL_pageBlockPhoto) || ((innerBlock instanceof TL_pageBlockVideo) && isVideoBlock(innerBlock))) {
                            this.photoBlocks.add(innerBlock);
                        }
                        a++;
                    }
                    return;
                } else if (!(block instanceof TL_pageBlockCover)) {
                    return;
                } else {
                    if ((block.cover instanceof TL_pageBlockPhoto) || ((block.cover instanceof TL_pageBlockVideo) && isVideoBlock(block.cover))) {
                        this.photoBlocks.add(block.cover);
                        return;
                    }
                    return;
                }
            }
        }
        this.photoBlocks.add(block);
    }

    public boolean open(MessageObject messageObject) {
        return open(messageObject, null, null, true);
    }

    public boolean open(TL_webPage webpage, String url) {
        return open(null, webpage, url, true);
    }

    private void hideActionBar() {
        AnimatorSet animatorSet = new AnimatorSet();
        r1 = new Animator[3];
        r1[0] = ObjectAnimator.ofFloat(this.backButton, "alpha", new float[]{0.0f});
        r1[1] = ObjectAnimator.ofFloat(this.shareContainer, "alpha", new float[]{0.0f});
        r1[2] = ObjectAnimator.ofFloat(this.settingsButton, "alpha", new float[]{0.0f});
        animatorSet.playTogether(r1);
        animatorSet.setDuration(250);
        animatorSet.setInterpolator(new DecelerateInterpolator());
        animatorSet.start();
    }

    private void showActionBar(int delay) {
        AnimatorSet animatorSet = new AnimatorSet();
        r1 = new Animator[3];
        r1[0] = ObjectAnimator.ofFloat(this.backButton, "alpha", new float[]{1.0f});
        r1[1] = ObjectAnimator.ofFloat(this.shareContainer, "alpha", new float[]{1.0f});
        r1[2] = ObjectAnimator.ofFloat(this.settingsButton, "alpha", new float[]{1.0f});
        animatorSet.playTogether(r1);
        animatorSet.setDuration(150);
        animatorSet.setStartDelay((long) delay);
        animatorSet.start();
    }

    private void showProgressView(final boolean show) {
        if (this.progressViewAnimation != null) {
            this.progressViewAnimation.cancel();
        }
        this.progressViewAnimation = new AnimatorSet();
        AnimatorSet animatorSet;
        Animator[] animatorArr;
        if (show) {
            this.progressView.setVisibility(0);
            this.shareContainer.setEnabled(false);
            animatorSet = this.progressViewAnimation;
            animatorArr = new Animator[6];
            animatorArr[0] = ObjectAnimator.ofFloat(this.shareButton, "scaleX", new float[]{0.1f});
            animatorArr[1] = ObjectAnimator.ofFloat(this.shareButton, "scaleY", new float[]{0.1f});
            animatorArr[2] = ObjectAnimator.ofFloat(this.shareButton, "alpha", new float[]{0.0f});
            animatorArr[3] = ObjectAnimator.ofFloat(this.progressView, "scaleX", new float[]{1.0f});
            animatorArr[4] = ObjectAnimator.ofFloat(this.progressView, "scaleY", new float[]{1.0f});
            animatorArr[5] = ObjectAnimator.ofFloat(this.progressView, "alpha", new float[]{1.0f});
            animatorSet.playTogether(animatorArr);
        } else {
            this.shareButton.setVisibility(0);
            this.shareContainer.setEnabled(true);
            animatorSet = this.progressViewAnimation;
            animatorArr = new Animator[6];
            animatorArr[0] = ObjectAnimator.ofFloat(this.progressView, "scaleX", new float[]{0.1f});
            animatorArr[1] = ObjectAnimator.ofFloat(this.progressView, "scaleY", new float[]{0.1f});
            animatorArr[2] = ObjectAnimator.ofFloat(this.progressView, "alpha", new float[]{0.0f});
            animatorArr[3] = ObjectAnimator.ofFloat(this.shareButton, "scaleX", new float[]{1.0f});
            animatorArr[4] = ObjectAnimator.ofFloat(this.shareButton, "scaleY", new float[]{1.0f});
            animatorArr[5] = ObjectAnimator.ofFloat(this.shareButton, "alpha", new float[]{1.0f});
            animatorSet.playTogether(animatorArr);
        }
        this.progressViewAnimation.addListener(new AnimatorListenerAdapter() {
            public void onAnimationEnd(Animator animation) {
                if (ArticleViewer.this.progressViewAnimation != null && ArticleViewer.this.progressViewAnimation.equals(animation)) {
                    if (show) {
                        ArticleViewer.this.shareButton.setVisibility(4);
                    } else {
                        ArticleViewer.this.progressView.setVisibility(4);
                    }
                }
            }

            public void onAnimationCancel(Animator animation) {
                if (ArticleViewer.this.progressViewAnimation != null && ArticleViewer.this.progressViewAnimation.equals(animation)) {
                    ArticleViewer.this.progressViewAnimation = null;
                }
            }
        });
        this.progressViewAnimation.setDuration(150);
        this.progressViewAnimation.start();
    }

    public void collapse() {
        if (this.parentActivity != null && this.isVisible) {
            if (!checkAnimation()) {
                if (this.fullscreenVideoContainer.getVisibility() == 0) {
                    if (this.customView != null) {
                        this.fullscreenVideoContainer.setVisibility(4);
                        this.customViewCallback.onCustomViewHidden();
                        this.fullscreenVideoContainer.removeView(this.customView);
                        this.customView = null;
                    } else if (this.fullscreenedVideo != null) {
                        this.fullscreenedVideo.exitFullscreen();
                    }
                }
                if (this.isPhotoVisible) {
                    closePhoto(false);
                }
                try {
                    if (this.visibleDialog != null) {
                        this.visibleDialog.dismiss();
                        this.visibleDialog = null;
                    }
                } catch (Throwable e) {
                    FileLog.e(e);
                }
                AnimatorSet animatorSet = new AnimatorSet();
                Animator[] animatorArr = new Animator[12];
                animatorArr[0] = ObjectAnimator.ofFloat(this.containerView, "translationX", new float[]{(float) (this.containerView.getMeasuredWidth() - AndroidUtilities.dp(56.0f))});
                FrameLayout frameLayout = this.containerView;
                String str = "translationY";
                float[] fArr = new float[1];
                fArr[0] = (float) (ActionBar.getCurrentActionBarHeight() + (VERSION.SDK_INT >= 21 ? AndroidUtilities.statusBarHeight : 0));
                animatorArr[1] = ObjectAnimator.ofFloat(frameLayout, str, fArr);
                animatorArr[2] = ObjectAnimator.ofFloat(this.windowView, "alpha", new float[]{0.0f});
                animatorArr[3] = ObjectAnimator.ofFloat(this.listView, "alpha", new float[]{0.0f});
                animatorArr[4] = ObjectAnimator.ofFloat(this.listView, "translationY", new float[]{(float) (-AndroidUtilities.dp(56.0f))});
                animatorArr[5] = ObjectAnimator.ofFloat(this.headerView, "translationY", new float[]{0.0f});
                animatorArr[6] = ObjectAnimator.ofFloat(this.backButton, "scaleX", new float[]{1.0f});
                animatorArr[7] = ObjectAnimator.ofFloat(this.backButton, "scaleY", new float[]{1.0f});
                animatorArr[8] = ObjectAnimator.ofFloat(this.backButton, "translationY", new float[]{0.0f});
                animatorArr[9] = ObjectAnimator.ofFloat(this.shareContainer, "scaleX", new float[]{1.0f});
                animatorArr[10] = ObjectAnimator.ofFloat(this.shareContainer, "translationY", new float[]{0.0f});
                animatorArr[11] = ObjectAnimator.ofFloat(this.shareContainer, "scaleY", new float[]{1.0f});
                animatorSet.playTogether(animatorArr);
                this.collapsed = true;
                this.animationInProgress = 2;
                this.animationEndRunnable = new Runnable() {
                    public void run() {
                        if (ArticleViewer.this.containerView != null) {
                            if (VERSION.SDK_INT >= 18) {
                                ArticleViewer.this.containerView.setLayerType(0, null);
                            }
                            ArticleViewer.this.animationInProgress = 0;
                            ((WindowManager) ArticleViewer.this.parentActivity.getSystemService("window")).updateViewLayout(ArticleViewer.this.windowView, ArticleViewer.this.windowLayoutParams);
                        }
                    }
                };
                animatorSet.setInterpolator(new DecelerateInterpolator());
                animatorSet.setDuration(250);
                animatorSet.addListener(new AnimatorListenerAdapter() {
                    public void onAnimationEnd(Animator animation) {
                        if (ArticleViewer.this.animationEndRunnable != null) {
                            ArticleViewer.this.animationEndRunnable.run();
                            ArticleViewer.this.animationEndRunnable = null;
                        }
                    }
                });
                this.transitionAnimationStartTime = System.currentTimeMillis();
                if (VERSION.SDK_INT >= 18) {
                    this.containerView.setLayerType(2, null);
                }
                this.backDrawable.setRotation(1.0f, true);
                animatorSet.start();
            }
        }
    }

    public void uncollapse() {
        if (this.parentActivity != null && this.isVisible) {
            if (!checkAnimation()) {
                AnimatorSet animatorSet = new AnimatorSet();
                r1 = new Animator[12];
                r1[0] = ObjectAnimator.ofFloat(this.containerView, "translationX", new float[]{0.0f});
                r1[1] = ObjectAnimator.ofFloat(this.containerView, "translationY", new float[]{0.0f});
                r1[2] = ObjectAnimator.ofFloat(this.windowView, "alpha", new float[]{1.0f});
                r1[3] = ObjectAnimator.ofFloat(this.listView, "alpha", new float[]{1.0f});
                r1[4] = ObjectAnimator.ofFloat(this.listView, "translationY", new float[]{0.0f});
                r1[5] = ObjectAnimator.ofFloat(this.headerView, "translationY", new float[]{0.0f});
                r1[6] = ObjectAnimator.ofFloat(this.backButton, "scaleX", new float[]{1.0f});
                r1[7] = ObjectAnimator.ofFloat(this.backButton, "scaleY", new float[]{1.0f});
                r1[8] = ObjectAnimator.ofFloat(this.backButton, "translationY", new float[]{0.0f});
                r1[9] = ObjectAnimator.ofFloat(this.shareContainer, "scaleX", new float[]{1.0f});
                r1[10] = ObjectAnimator.ofFloat(this.shareContainer, "translationY", new float[]{0.0f});
                r1[11] = ObjectAnimator.ofFloat(this.shareContainer, "scaleY", new float[]{1.0f});
                animatorSet.playTogether(r1);
                this.collapsed = false;
                this.animationInProgress = 2;
                this.animationEndRunnable = new Runnable() {
                    public void run() {
                        if (ArticleViewer.this.containerView != null) {
                            if (VERSION.SDK_INT >= 18) {
                                ArticleViewer.this.containerView.setLayerType(0, null);
                            }
                            ArticleViewer.this.animationInProgress = 0;
                        }
                    }
                };
                animatorSet.setDuration(250);
                animatorSet.setInterpolator(new DecelerateInterpolator());
                animatorSet.addListener(new AnimatorListenerAdapter() {
                    public void onAnimationEnd(Animator animation) {
                        if (ArticleViewer.this.animationEndRunnable != null) {
                            ArticleViewer.this.animationEndRunnable.run();
                            ArticleViewer.this.animationEndRunnable = null;
                        }
                    }
                });
                this.transitionAnimationStartTime = System.currentTimeMillis();
                if (VERSION.SDK_INT >= 18) {
                    this.containerView.setLayerType(2, null);
                }
                this.backDrawable.setRotation(0.0f, true);
                animatorSet.start();
            }
        }
    }

    private void saveCurrentPagePosition() {
        if (this.currentPage != null) {
            int position = this.layoutManager.findFirstVisibleItemPosition();
            if (position != -1) {
                int offset;
                View view = this.layoutManager.findViewByPosition(position);
                boolean z = false;
                if (view != null) {
                    offset = view.getTop();
                } else {
                    offset = 0;
                }
                Editor editor = ApplicationLoader.applicationContext.getSharedPreferences("articles", 0).edit();
                String key = new StringBuilder();
                key.append("article");
                key.append(this.currentPage.id);
                key = key.toString();
                Editor putInt = editor.putInt(key, position);
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append(key);
                stringBuilder.append("o");
                putInt = putInt.putInt(stringBuilder.toString(), offset);
                stringBuilder = new StringBuilder();
                stringBuilder.append(key);
                stringBuilder.append("r");
                String stringBuilder2 = stringBuilder.toString();
                if (AndroidUtilities.displaySize.x > AndroidUtilities.displaySize.y) {
                    z = true;
                }
                putInt.putBoolean(stringBuilder2, z).commit();
            }
        }
    }

    public void close(boolean byBackPress, boolean force) {
        if (this.parentActivity != null && this.isVisible) {
            if (!checkAnimation()) {
                NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.messagePlayingProgressDidChanged);
                NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.messagePlayingDidReset);
                NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.messagePlayingPlayStateChanged);
                NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.messagePlayingDidStarted);
                NotificationCenter.getGlobalInstance().removeObserver(this, NotificationCenter.needSetDayNightTheme);
                if (this.fullscreenVideoContainer.getVisibility() == 0) {
                    if (this.customView != null) {
                        this.fullscreenVideoContainer.setVisibility(4);
                        this.customViewCallback.onCustomViewHidden();
                        this.fullscreenVideoContainer.removeView(this.customView);
                        this.customView = null;
                    } else if (this.fullscreenedVideo != null) {
                        this.fullscreenedVideo.exitFullscreen();
                    }
                    if (!force) {
                        return;
                    }
                }
                if (this.isPhotoVisible) {
                    closePhoto(force ^ 1);
                    if (!force) {
                        return;
                    }
                }
                if (this.openUrlReqId != 0) {
                    ConnectionsManager.getInstance(this.currentAccount).cancelRequest(this.openUrlReqId, true);
                    this.openUrlReqId = 0;
                    showProgressView(false);
                }
                if (this.previewsReqId != 0) {
                    ConnectionsManager.getInstance(this.currentAccount).cancelRequest(this.previewsReqId, true);
                    this.previewsReqId = 0;
                    showProgressView(false);
                }
                saveCurrentPagePosition();
                if (!byBackPress || force || !removeLastPageFromStack()) {
                    this.parentFragment = null;
                    try {
                        if (this.visibleDialog != null) {
                            this.visibleDialog.dismiss();
                            this.visibleDialog = null;
                        }
                    } catch (Throwable e) {
                        FileLog.e(e);
                    }
                    AnimatorSet animatorSet = new AnimatorSet();
                    r4 = new Animator[3];
                    r4[0] = ObjectAnimator.ofFloat(this.windowView, "alpha", new float[]{0.0f});
                    r4[1] = ObjectAnimator.ofFloat(this.containerView, "alpha", new float[]{0.0f});
                    r4[2] = ObjectAnimator.ofFloat(this.windowView, "translationX", new float[]{0.0f, (float) AndroidUtilities.dp(56.0f)});
                    animatorSet.playTogether(r4);
                    this.animationInProgress = 2;
                    this.animationEndRunnable = new Runnable() {
                        public void run() {
                            if (ArticleViewer.this.containerView != null) {
                                if (VERSION.SDK_INT >= 18) {
                                    ArticleViewer.this.containerView.setLayerType(0, null);
                                }
                                ArticleViewer.this.animationInProgress = 0;
                                ArticleViewer.this.onClosed();
                            }
                        }
                    };
                    animatorSet.setDuration(150);
                    animatorSet.setInterpolator(this.interpolator);
                    animatorSet.addListener(new AnimatorListenerAdapter() {
                        public void onAnimationEnd(Animator animation) {
                            if (ArticleViewer.this.animationEndRunnable != null) {
                                ArticleViewer.this.animationEndRunnable.run();
                                ArticleViewer.this.animationEndRunnable = null;
                            }
                        }
                    });
                    this.transitionAnimationStartTime = System.currentTimeMillis();
                    if (VERSION.SDK_INT >= 18) {
                        this.containerView.setLayerType(2, null);
                    }
                    animatorSet.start();
                }
            }
        }
    }

    private void onClosed() {
        this.isVisible = false;
        this.currentPage = null;
        this.blocks.clear();
        this.photoBlocks.clear();
        this.adapter.notifyDataSetChanged();
        try {
            this.parentActivity.getWindow().clearFlags(128);
        } catch (Throwable e) {
            FileLog.e(e);
        }
        for (int a = 0; a < this.createdWebViews.size(); a++) {
            ((BlockEmbedCell) this.createdWebViews.get(a)).destroyWebView(false);
        }
        this.containerView.post(new Runnable() {
            public void run() {
                try {
                    if (ArticleViewer.this.windowView.getParent() != null) {
                        ((WindowManager) ArticleViewer.this.parentActivity.getSystemService("window")).removeView(ArticleViewer.this.windowView);
                    }
                } catch (Throwable e) {
                    FileLog.e(e);
                }
            }
        });
    }

    private void loadChannel(final BlockChannelCell cell, Chat channel) {
        if (!this.loadingChannel) {
            if (!TextUtils.isEmpty(channel.username)) {
                this.loadingChannel = true;
                TL_contacts_resolveUsername req = new TL_contacts_resolveUsername();
                req.username = channel.username;
                final int currentAccount = UserConfig.selectedAccount;
                ConnectionsManager.getInstance(currentAccount).sendRequest(req, new RequestDelegate() {
                    public void run(final TLObject response, final TL_error error) {
                        AndroidUtilities.runOnUIThread(new Runnable() {
                            public void run() {
                                ArticleViewer.this.loadingChannel = false;
                                if (!(ArticleViewer.this.parentFragment == null || ArticleViewer.this.blocks == null)) {
                                    if (!ArticleViewer.this.blocks.isEmpty()) {
                                        if (error == null) {
                                            TL_contacts_resolvedPeer res = response;
                                            if (res.chats.isEmpty()) {
                                                cell.setState(4, false);
                                            } else {
                                                MessagesController.getInstance(currentAccount).putUsers(res.users, false);
                                                MessagesController.getInstance(currentAccount).putChats(res.chats, false);
                                                MessagesStorage.getInstance(currentAccount).putUsersAndChats(res.users, res.chats, false, true);
                                                ArticleViewer.this.loadedChannel = (Chat) res.chats.get(0);
                                                if (!ArticleViewer.this.loadedChannel.left || ArticleViewer.this.loadedChannel.kicked) {
                                                    cell.setState(4, false);
                                                } else {
                                                    cell.setState(0, false);
                                                }
                                            }
                                        } else {
                                            cell.setState(4, false);
                                        }
                                    }
                                }
                            }
                        });
                    }
                });
            }
        }
    }

    private void joinChannel(BlockChannelCell cell, Chat channel) {
        TL_channels_joinChannel req = new TL_channels_joinChannel();
        req.channel = MessagesController.getInputChannel(channel);
        int currentAccount = UserConfig.selectedAccount;
        final BlockChannelCell blockChannelCell = cell;
        final int i = currentAccount;
        final TL_channels_joinChannel tL_channels_joinChannel = req;
        final Chat chat = channel;
        ConnectionsManager.getInstance(currentAccount).sendRequest(req, new RequestDelegate() {
            public void run(TLObject response, final TL_error error) {
                if (error != null) {
                    AndroidUtilities.runOnUIThread(new Runnable() {
                        public void run() {
                            blockChannelCell.setState(0, false);
                            AlertsCreator.processError(i, error, ArticleViewer.this.parentFragment, tL_channels_joinChannel, Boolean.valueOf(true));
                        }
                    });
                    return;
                }
                boolean hasJoinMessage = false;
                Updates updates = (Updates) response;
                for (int a = 0; a < updates.updates.size(); a++) {
                    Update update = (Update) updates.updates.get(a);
                    if ((update instanceof TL_updateNewChannelMessage) && (((TL_updateNewChannelMessage) update).message.action instanceof TL_messageActionChatAddUser)) {
                        hasJoinMessage = true;
                        break;
                    }
                }
                MessagesController.getInstance(i).processUpdates(updates, false);
                if (!hasJoinMessage) {
                    MessagesController.getInstance(i).generateJoinMessage(chat.id, true);
                }
                AndroidUtilities.runOnUIThread(new Runnable() {
                    public void run() {
                        blockChannelCell.setState(2, false);
                    }
                });
                AndroidUtilities.runOnUIThread(new Runnable() {
                    public void run() {
                        MessagesController.getInstance(i).loadFullChat(chat.id, 0, true);
                    }
                }, 1000);
                MessagesStorage.getInstance(i).updateDialogsWithDeletedMessages(new ArrayList(), null, true, chat.id);
            }
        });
    }

    private boolean checkAnimation() {
        if (this.animationInProgress != 0 && Math.abs(this.transitionAnimationStartTime - System.currentTimeMillis()) >= 500) {
            if (this.animationEndRunnable != null) {
                this.animationEndRunnable.run();
                this.animationEndRunnable = null;
            }
            this.animationInProgress = 0;
        }
        if (this.animationInProgress != 0) {
            return true;
        }
        return false;
    }

    public void destroyArticleViewer() {
        if (this.parentActivity != null) {
            if (this.windowView != null) {
                releasePlayer();
                try {
                    if (this.windowView.getParent() != null) {
                        ((WindowManager) this.parentActivity.getSystemService("window")).removeViewImmediate(this.windowView);
                    }
                    this.windowView = null;
                } catch (Throwable e) {
                    FileLog.e(e);
                }
                for (int a = 0; a < this.createdWebViews.size(); a++) {
                    ((BlockEmbedCell) this.createdWebViews.get(a)).destroyWebView(true);
                }
                this.createdWebViews.clear();
                try {
                    this.parentActivity.getWindow().clearFlags(128);
                } catch (Throwable e2) {
                    FileLog.e(e2);
                }
                if (this.currentThumb != null) {
                    this.currentThumb.release();
                    this.currentThumb = null;
                }
                this.animatingImageView.setImageBitmap(null);
                this.parentActivity = null;
                this.parentFragment = null;
                Instance = null;
            }
        }
    }

    public boolean isVisible() {
        return this.isVisible;
    }

    public void showDialog(Dialog dialog) {
        if (this.parentActivity != null) {
            try {
                if (this.visibleDialog != null) {
                    this.visibleDialog.dismiss();
                    this.visibleDialog = null;
                }
            } catch (Throwable e) {
                FileLog.e(e);
            }
            try {
                this.visibleDialog = dialog;
                this.visibleDialog.setCanceledOnTouchOutside(true);
                this.visibleDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    public void onDismiss(DialogInterface dialog) {
                        ArticleViewer.this.showActionBar(120);
                        ArticleViewer.this.visibleDialog = null;
                    }
                });
                dialog.show();
            } catch (Throwable e2) {
                FileLog.e(e2);
            }
        }
    }

    private void onSharePressed() {
        if (this.parentActivity != null) {
            if (this.currentMedia != null) {
                try {
                    File f = getMediaFile(this.currentIndex);
                    if (f == null || !f.exists()) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(this.parentActivity);
                        builder.setTitle(LocaleController.getString("AppName", R.string.AppName));
                        builder.setPositiveButton(LocaleController.getString("OK", R.string.OK), null);
                        builder.setMessage(LocaleController.getString("PleaseDownload", R.string.PleaseDownload));
                        showDialog(builder.create());
                    } else {
                        Intent intent = new Intent("android.intent.action.SEND");
                        intent.setType(getMediaMime(this.currentIndex));
                        if (VERSION.SDK_INT >= 24) {
                            try {
                                intent.putExtra("android.intent.extra.STREAM", FileProvider.getUriForFile(this.parentActivity, "org.telegram.messenger.beta.provider", f));
                                intent.setFlags(1);
                            } catch (Exception e) {
                                intent.putExtra("android.intent.extra.STREAM", Uri.fromFile(f));
                            }
                        } else {
                            intent.putExtra("android.intent.extra.STREAM", Uri.fromFile(f));
                        }
                        this.parentActivity.startActivityForResult(Intent.createChooser(intent, LocaleController.getString("ShareFile", R.string.ShareFile)), 500);
                    }
                } catch (Throwable e2) {
                    FileLog.e(e2);
                }
            }
        }
    }

    private void setScaleToFill() {
        float bitmapWidth = (float) this.centerImage.getBitmapWidth();
        float containerWidth = (float) getContainerViewWidth();
        float bitmapHeight = (float) this.centerImage.getBitmapHeight();
        float containerHeight = (float) getContainerViewHeight();
        float scaleFit = Math.min(containerHeight / bitmapHeight, containerWidth / bitmapWidth);
        this.scale = Math.max(containerWidth / ((float) ((int) (bitmapWidth * scaleFit))), containerHeight / ((float) ((int) (bitmapHeight * scaleFit))));
        updateMinMax(this.scale);
    }

    private void updateVideoPlayerTime() {
        String newText;
        if (this.videoPlayer == null) {
            newText = String.format("%02d:%02d / %02d:%02d", new Object[]{Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(0)});
        } else {
            long current = this.videoPlayer.getCurrentPosition() / 1000;
            if (this.videoPlayer.getDuration() / 1000 == C.TIME_UNSET || current == C.TIME_UNSET) {
                newText = String.format("%02d:%02d / %02d:%02d", new Object[]{Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(0)});
                if (!TextUtils.equals(this.videoPlayerTime.getText(), newText)) {
                    this.videoPlayerTime.setText(newText);
                }
            }
            newText = String.format("%02d:%02d / %02d:%02d", new Object[]{Long.valueOf(current / 60), Long.valueOf(current % 60), Long.valueOf(total / 60), Long.valueOf(total % 60)});
        }
        if (!TextUtils.equals(this.videoPlayerTime.getText(), newText)) {
            this.videoPlayerTime.setText(newText);
        }
    }

    @SuppressLint({"NewApi"})
    private void preparePlayer(File file, boolean playWhenReady) {
        if (this.parentActivity != null) {
            releasePlayer();
            if (this.videoTextureView == null) {
                this.aspectRatioFrameLayout = new AspectRatioFrameLayout(this.parentActivity);
                this.aspectRatioFrameLayout.setVisibility(4);
                this.photoContainerView.addView(this.aspectRatioFrameLayout, 0, LayoutHelper.createFrame(-1, -1, 17));
                this.videoTextureView = new TextureView(this.parentActivity);
                this.videoTextureView.setOpaque(false);
                this.aspectRatioFrameLayout.addView(this.videoTextureView, LayoutHelper.createFrame(-1, -1, 17));
            }
            this.textureUploaded = false;
            this.videoCrossfadeStarted = false;
            TextureView textureView = this.videoTextureView;
            this.videoCrossfadeAlpha = 0.0f;
            textureView.setAlpha(0.0f);
            this.videoPlayButton.setImageResource(R.drawable.inline_video_play);
            if (this.videoPlayer == null) {
                long duration;
                this.videoPlayer = new VideoPlayer();
                this.videoPlayer.setTextureView(this.videoTextureView);
                this.videoPlayer.setDelegate(new VideoPlayerDelegate() {
                    public void onStateChanged(boolean playWhenReady, int playbackState) {
                        if (ArticleViewer.this.videoPlayer != null) {
                            if (playbackState == 4 || playbackState == 1) {
                                try {
                                    ArticleViewer.this.parentActivity.getWindow().clearFlags(128);
                                } catch (Throwable e) {
                                    FileLog.e(e);
                                }
                            } else {
                                try {
                                    ArticleViewer.this.parentActivity.getWindow().addFlags(128);
                                } catch (Throwable e2) {
                                    FileLog.e(e2);
                                }
                            }
                            if (playbackState == 3 && ArticleViewer.this.aspectRatioFrameLayout.getVisibility() != 0) {
                                ArticleViewer.this.aspectRatioFrameLayout.setVisibility(0);
                            }
                            if (!ArticleViewer.this.videoPlayer.isPlaying() || playbackState == 4) {
                                if (ArticleViewer.this.isPlaying) {
                                    ArticleViewer.this.isPlaying = false;
                                    ArticleViewer.this.videoPlayButton.setImageResource(R.drawable.inline_video_play);
                                    AndroidUtilities.cancelRunOnUIThread(ArticleViewer.this.updateProgressRunnable);
                                    if (playbackState == 4 && !ArticleViewer.this.videoPlayerSeekbar.isDragging()) {
                                        ArticleViewer.this.videoPlayerSeekbar.setProgress(0.0f);
                                        ArticleViewer.this.videoPlayerControlFrameLayout.invalidate();
                                        ArticleViewer.this.videoPlayer.seekTo(0);
                                        ArticleViewer.this.videoPlayer.pause();
                                    }
                                }
                            } else if (!ArticleViewer.this.isPlaying) {
                                ArticleViewer.this.isPlaying = true;
                                ArticleViewer.this.videoPlayButton.setImageResource(R.drawable.inline_video_pause);
                                AndroidUtilities.runOnUIThread(ArticleViewer.this.updateProgressRunnable);
                            }
                            ArticleViewer.this.updateVideoPlayerTime();
                        }
                    }

                    public void onError(Exception e) {
                        FileLog.e((Throwable) e);
                    }

                    public void onVideoSizeChanged(int width, int height, int unappliedRotationDegrees, float pixelWidthHeightRatio) {
                        if (ArticleViewer.this.aspectRatioFrameLayout != null) {
                            if (unappliedRotationDegrees == 90 || unappliedRotationDegrees == 270) {
                                int temp = width;
                                width = height;
                                height = temp;
                            }
                            ArticleViewer.this.aspectRatioFrameLayout.setAspectRatio(height == 0 ? 1.0f : (((float) width) * pixelWidthHeightRatio) / ((float) height), unappliedRotationDegrees);
                        }
                    }

                    public void onRenderedFirstFrame() {
                        if (!ArticleViewer.this.textureUploaded) {
                            ArticleViewer.this.textureUploaded = true;
                            ArticleViewer.this.containerView.invalidate();
                        }
                    }

                    public boolean onSurfaceDestroyed(SurfaceTexture surfaceTexture) {
                        return false;
                    }

                    public void onSurfaceTextureUpdated(SurfaceTexture surfaceTexture) {
                    }
                });
                if (this.videoPlayer != null) {
                    duration = this.videoPlayer.getDuration();
                    if (duration == C.TIME_UNSET) {
                        duration = 0;
                    }
                } else {
                    duration = 0;
                }
                duration /= 1000;
                Math.ceil((double) this.videoPlayerTime.getPaint().measureText(String.format("%02d:%02d / %02d:%02d", new Object[]{Long.valueOf(duration / 60), Long.valueOf(duration % 60), Long.valueOf(duration / 60), Long.valueOf(duration % 60)})));
            }
            this.videoPlayer.preparePlayer(Uri.fromFile(file), "other");
            this.bottomLayout.setVisibility(0);
            this.videoPlayer.setPlayWhenReady(playWhenReady);
        }
    }

    private void releasePlayer() {
        if (this.videoPlayer != null) {
            this.videoPlayer.releasePlayer();
            this.videoPlayer = null;
        }
        try {
            this.parentActivity.getWindow().clearFlags(128);
        } catch (Throwable e) {
            FileLog.e(e);
        }
        if (this.aspectRatioFrameLayout != null) {
            this.photoContainerView.removeView(this.aspectRatioFrameLayout);
            this.aspectRatioFrameLayout = null;
        }
        if (this.videoTextureView != null) {
            this.videoTextureView = null;
        }
        if (this.isPlaying) {
            this.isPlaying = false;
            this.videoPlayButton.setImageResource(R.drawable.inline_video_play);
            AndroidUtilities.cancelRunOnUIThread(this.updateProgressRunnable);
        }
        this.bottomLayout.setVisibility(8);
    }

    private void toggleActionBar(boolean show, boolean animated) {
        if (show) {
            this.actionBar.setVisibility(0);
            if (this.videoPlayer != null) {
                this.bottomLayout.setVisibility(0);
            }
            if (this.captionTextView.getTag() != null) {
                this.captionTextView.setVisibility(0);
            }
        }
        this.isActionBarVisible = show;
        this.actionBar.setEnabled(show);
        this.bottomLayout.setEnabled(show);
        float f = 0.0f;
        if (animated) {
            ArrayList<Animator> arrayList = new ArrayList();
            ActionBar actionBar = this.actionBar;
            String str = "alpha";
            float[] fArr = new float[1];
            fArr[0] = show ? 1.0f : 0.0f;
            arrayList.add(ObjectAnimator.ofFloat(actionBar, str, fArr));
            FrameLayout frameLayout = this.bottomLayout;
            str = "alpha";
            fArr = new float[1];
            fArr[0] = show ? 1.0f : 0.0f;
            arrayList.add(ObjectAnimator.ofFloat(frameLayout, str, fArr));
            if (this.captionTextView.getTag() != null) {
                TextView textView = this.captionTextView;
                str = "alpha";
                float[] fArr2 = new float[1];
                if (show) {
                    f = 1.0f;
                }
                fArr2[0] = f;
                arrayList.add(ObjectAnimator.ofFloat(textView, str, fArr2));
            }
            this.currentActionBarAnimation = new AnimatorSet();
            this.currentActionBarAnimation.playTogether(arrayList);
            if (!show) {
                this.currentActionBarAnimation.addListener(new AnimatorListenerAdapter() {
                    public void onAnimationEnd(Animator animation) {
                        if (ArticleViewer.this.currentActionBarAnimation != null && ArticleViewer.this.currentActionBarAnimation.equals(animation)) {
                            ArticleViewer.this.actionBar.setVisibility(8);
                            if (ArticleViewer.this.videoPlayer != null) {
                                ArticleViewer.this.bottomLayout.setVisibility(8);
                            }
                            if (ArticleViewer.this.captionTextView.getTag() != null) {
                                ArticleViewer.this.captionTextView.setVisibility(4);
                            }
                            ArticleViewer.this.currentActionBarAnimation = null;
                        }
                    }
                });
            }
            this.currentActionBarAnimation.setDuration(200);
            this.currentActionBarAnimation.start();
            return;
        }
        this.actionBar.setAlpha(show ? 1.0f : 0.0f);
        this.bottomLayout.setAlpha(show ? 1.0f : 0.0f);
        if (this.captionTextView.getTag() != null) {
            TextView textView2 = this.captionTextView;
            if (show) {
                f = 1.0f;
            }
            textView2.setAlpha(f);
        }
        if (!show) {
            this.actionBar.setVisibility(8);
            if (this.videoPlayer != null) {
                this.bottomLayout.setVisibility(8);
            }
            if (this.captionTextView.getTag() != null) {
                this.captionTextView.setVisibility(4);
            }
        }
    }

    private String getFileName(int index) {
        TLObject media = getMedia(index);
        if (media instanceof Photo) {
            media = FileLoader.getClosestPhotoSizeWithSize(((Photo) media).sizes, AndroidUtilities.getPhotoSize());
        }
        return FileLoader.getAttachFileName(media);
    }

    private TLObject getMedia(int index) {
        if (!this.imagesArr.isEmpty() && index < this.imagesArr.size()) {
            if (index >= 0) {
                PageBlock block = (PageBlock) this.imagesArr.get(index);
                if (block.photo_id != 0) {
                    return getPhotoWithId(block.photo_id);
                }
                if (block.video_id != 0) {
                    return getDocumentWithId(block.video_id);
                }
                return null;
            }
        }
        return null;
    }

    private File getMediaFile(int index) {
        if (!this.imagesArr.isEmpty() && index < this.imagesArr.size()) {
            if (index >= 0) {
                PageBlock block = (PageBlock) this.imagesArr.get(index);
                if (block.photo_id != 0) {
                    Photo photo = getPhotoWithId(block.photo_id);
                    if (photo != null) {
                        PhotoSize sizeFull = FileLoader.getClosestPhotoSizeWithSize(photo.sizes, AndroidUtilities.getPhotoSize());
                        if (sizeFull != null) {
                            return FileLoader.getPathToAttach(sizeFull, true);
                        }
                    }
                } else if (block.video_id != 0) {
                    Document document = getDocumentWithId(block.video_id);
                    if (document != null) {
                        return FileLoader.getPathToAttach(document, true);
                    }
                }
                return null;
            }
        }
        return null;
    }

    private boolean isVideoBlock(PageBlock block) {
        if (!(block == null || block.video_id == 0)) {
            Document document = getDocumentWithId(block.video_id);
            if (document != null) {
                return MessageObject.isVideoDocument(document);
            }
        }
        return false;
    }

    private boolean isMediaVideo(int index) {
        return !this.imagesArr.isEmpty() && index < this.imagesArr.size() && index >= 0 && isVideoBlock((PageBlock) this.imagesArr.get(index));
    }

    private String getMediaMime(int index) {
        if (index < this.imagesArr.size()) {
            if (index >= 0) {
                PageBlock block = (PageBlock) this.imagesArr.get(index);
                if (block instanceof TL_pageBlockVideo) {
                    Document document = getDocumentWithId(block.video_id);
                    if (document != null) {
                        return document.mime_type;
                    }
                }
                return "image/jpeg";
            }
        }
        return "image/jpeg";
    }

    private FileLocation getFileLocation(int index, int[] size) {
        if (index >= 0) {
            if (index < this.imagesArr.size()) {
                TLObject media = getMedia(index);
                if (media instanceof Photo) {
                    PhotoSize sizeFull = FileLoader.getClosestPhotoSizeWithSize(((Photo) media).sizes, AndroidUtilities.getPhotoSize());
                    if (sizeFull != null) {
                        size[0] = sizeFull.size;
                        if (size[0] == 0) {
                            size[0] = -1;
                        }
                        return sizeFull.location;
                    }
                    size[0] = -1;
                } else if (media instanceof Document) {
                    Document document = (Document) media;
                    if (document.thumb != null) {
                        size[0] = document.thumb.size;
                        if (size[0] == 0) {
                            size[0] = -1;
                        }
                        return document.thumb.location;
                    }
                }
                return null;
            }
        }
        return null;
    }

    private void onPhotoShow(int index, PlaceProviderObject object) {
        this.currentIndex = -1;
        this.currentFileNames[0] = null;
        this.currentFileNames[1] = null;
        this.currentFileNames[2] = null;
        if (this.currentThumb != null) {
            this.currentThumb.release();
        }
        this.currentThumb = object != null ? object.thumb : null;
        this.menuItem.setVisibility(0);
        this.menuItem.hideSubItem(3);
        this.actionBar.setTranslationY(0.0f);
        this.captionTextView.setTag(null);
        this.captionTextView.setVisibility(4);
        for (int a = 0; a < 3; a++) {
            if (this.radialProgressViews[a] != null) {
                this.radialProgressViews[a].setBackgroundState(-1, false);
            }
        }
        setImageIndex(index, true);
        if (this.currentMedia != null && isMediaVideo(this.currentIndex)) {
            onActionClick(false);
        }
    }

    private void setImages() {
        if (this.photoAnimationInProgress == 0) {
            setIndexToImage(this.centerImage, this.currentIndex);
            setIndexToImage(this.rightImage, this.currentIndex + 1);
            setIndexToImage(this.leftImage, this.currentIndex - 1);
        }
    }

    private void setImageIndex(int index, boolean init) {
        int i = index;
        if (this.currentIndex != i) {
            if (!(init || r0.currentThumb == null)) {
                r0.currentThumb.release();
                r0.currentThumb = null;
            }
            r0.currentFileNames[0] = getFileName(index);
            r0.currentFileNames[1] = getFileName(i + 1);
            r0.currentFileNames[2] = getFileName(i - 1);
            int prevIndex = r0.currentIndex;
            r0.currentIndex = i;
            boolean isVideo = false;
            boolean sameImage = false;
            if (!r0.imagesArr.isEmpty()) {
                if (r0.currentIndex >= 0) {
                    if (r0.currentIndex < r0.imagesArr.size()) {
                        PageBlock newMedia = (PageBlock) r0.imagesArr.get(r0.currentIndex);
                        boolean z = r0.currentMedia != null && r0.currentMedia == newMedia;
                        sameImage = z;
                        r0.currentMedia = newMedia;
                        isVideo = isMediaVideo(r0.currentIndex);
                        if (isVideo) {
                            r0.menuItem.showSubItem(3);
                        }
                        setCurrentCaption(getText(r0.currentMedia.caption, r0.currentMedia.caption, r0.currentMedia));
                        if (r0.currentAnimation != null) {
                            r0.menuItem.setVisibility(8);
                            r0.menuItem.hideSubItem(1);
                            r0.actionBar.setTitle(LocaleController.getString("AttachGif", R.string.AttachGif));
                        } else {
                            r0.menuItem.setVisibility(0);
                            if (r0.imagesArr.size() != 1) {
                                r0.actionBar.setTitle(LocaleController.formatString("Of", R.string.Of, Integer.valueOf(r0.currentIndex + 1), Integer.valueOf(r0.imagesArr.size())));
                            } else if (isVideo) {
                                r0.actionBar.setTitle(LocaleController.getString("AttachVideo", R.string.AttachVideo));
                            } else {
                                r0.actionBar.setTitle(LocaleController.getString("AttachPhoto", R.string.AttachPhoto));
                            }
                            r0.menuItem.showSubItem(1);
                        }
                    }
                }
                closePhoto(false);
                return;
            }
            int count = r0.listView.getChildCount();
            for (int a = 0; a < count; a++) {
                View child = r0.listView.getChildAt(a);
                if (child instanceof BlockSlideshowCell) {
                    BlockSlideshowCell cell = (BlockSlideshowCell) child;
                    int idx = cell.currentBlock.items.indexOf(r0.currentMedia);
                    if (idx != -1) {
                        cell.innerListView.setCurrentItem(idx, false);
                        break;
                    }
                }
            }
            if (r0.currentPlaceObject != null) {
                if (r0.photoAnimationInProgress == 0) {
                    r0.currentPlaceObject.imageReceiver.setVisible(true, true);
                } else {
                    r0.showAfterAnimation = r0.currentPlaceObject;
                }
            }
            r0.currentPlaceObject = getPlaceForPhoto(r0.currentMedia);
            if (r0.currentPlaceObject != null) {
                if (r0.photoAnimationInProgress == 0) {
                    r0.currentPlaceObject.imageReceiver.setVisible(false, true);
                } else {
                    r0.hideAfterAnimation = r0.currentPlaceObject;
                }
            }
            if (!sameImage) {
                r0.draggingDown = false;
                r0.translationX = 0.0f;
                r0.translationY = 0.0f;
                r0.scale = 1.0f;
                r0.animateToX = 0.0f;
                r0.animateToY = 0.0f;
                r0.animateToScale = 1.0f;
                r0.animationStartTime = 0;
                r0.imageMoveAnimation = null;
                if (r0.aspectRatioFrameLayout != null) {
                    r0.aspectRatioFrameLayout.setVisibility(4);
                }
                releasePlayer();
                r0.pinchStartDistance = 0.0f;
                r0.pinchStartScale = 1.0f;
                r0.pinchCenterX = 0.0f;
                r0.pinchCenterY = 0.0f;
                r0.pinchStartX = 0.0f;
                r0.pinchStartY = 0.0f;
                r0.moveStartX = 0.0f;
                r0.moveStartY = 0.0f;
                r0.zooming = false;
                r0.moving = false;
                r0.doubleTap = false;
                r0.invalidCoords = false;
                r0.canDragDown = true;
                r0.changingPage = false;
                r0.switchImageAfterAnimation = 0;
                boolean z2 = (r0.currentFileNames[0] == null || isVideo || r0.radialProgressViews[0].backgroundState == 0) ? false : true;
                r0.canZoom = z2;
                updateMinMax(r0.scale);
            }
            if (prevIndex == -1) {
                setImages();
                for (int a2 = 0; a2 < 3; a2++) {
                    checkProgress(a2, false);
                }
            } else {
                checkProgress(0, false);
                ImageReceiver temp;
                RadialProgressView tempProgress;
                if (prevIndex > r0.currentIndex) {
                    temp = r0.rightImage;
                    r0.rightImage = r0.centerImage;
                    r0.centerImage = r0.leftImage;
                    r0.leftImage = temp;
                    tempProgress = r0.radialProgressViews[0];
                    r0.radialProgressViews[0] = r0.radialProgressViews[2];
                    r0.radialProgressViews[2] = tempProgress;
                    setIndexToImage(r0.leftImage, r0.currentIndex - 1);
                    checkProgress(1, false);
                    checkProgress(2, false);
                } else if (prevIndex < r0.currentIndex) {
                    temp = r0.leftImage;
                    r0.leftImage = r0.centerImage;
                    r0.centerImage = r0.rightImage;
                    r0.rightImage = temp;
                    tempProgress = r0.radialProgressViews[0];
                    r0.radialProgressViews[0] = r0.radialProgressViews[1];
                    r0.radialProgressViews[1] = tempProgress;
                    setIndexToImage(r0.rightImage, r0.currentIndex + 1);
                    checkProgress(1, false);
                    checkProgress(2, false);
                }
            }
        }
    }

    private void setCurrentCaption(CharSequence caption) {
        if (TextUtils.isEmpty(caption)) {
            this.captionTextView.setTextColor(-1);
            this.captionTextView.setTag(null);
            this.captionTextView.setVisibility(4);
            return;
        }
        this.captionTextView = this.captionTextViewOld;
        this.captionTextViewOld = this.captionTextViewNew;
        this.captionTextViewNew = this.captionTextView;
        Theme.createChatResources(null, true);
        CharSequence str = Emoji.replaceEmoji(new SpannableStringBuilder(caption.toString()), this.captionTextView.getPaint().getFontMetricsInt(), AndroidUtilities.dp(20.0f), false);
        this.captionTextView.setTag(str);
        this.captionTextView.setText(str);
        this.captionTextView.setTextColor(-1);
        this.captionTextView.setAlpha(this.actionBar.getVisibility() == 0 ? 1.0f : 0.0f);
        AndroidUtilities.runOnUIThread(new Runnable() {
            public void run() {
                ArticleViewer.this.captionTextViewOld.setTag(null);
                int i = 4;
                ArticleViewer.this.captionTextViewOld.setVisibility(4);
                TextView access$14400 = ArticleViewer.this.captionTextViewNew;
                if (ArticleViewer.this.actionBar.getVisibility() == 0) {
                    i = 0;
                }
                access$14400.setVisibility(i);
            }
        });
    }

    private void checkProgress(int a, boolean animated) {
        if (this.currentFileNames[a] != null) {
            int index = this.currentIndex;
            boolean z = true;
            if (a == 1) {
                index++;
            } else if (a == 2) {
                index--;
            }
            File f = getMediaFile(index);
            boolean isVideo = isMediaVideo(index);
            if (f == null || !f.exists()) {
                if (!isVideo) {
                    this.radialProgressViews[a].setBackgroundState(0, animated);
                } else if (FileLoader.getInstance(this.currentAccount).isLoadingFile(this.currentFileNames[a])) {
                    this.radialProgressViews[a].setBackgroundState(1, false);
                } else {
                    this.radialProgressViews[a].setBackgroundState(2, false);
                }
                Float progress = ImageLoader.getInstance().getFileProgress(this.currentFileNames[a]);
                if (progress == null) {
                    progress = Float.valueOf(0.0f);
                }
                this.radialProgressViews[a].setProgress(progress.floatValue(), false);
            } else if (isVideo) {
                this.radialProgressViews[a].setBackgroundState(3, animated);
            } else {
                this.radialProgressViews[a].setBackgroundState(-1, animated);
            }
            if (a == 0) {
                if (this.currentFileNames[0] == null || isVideo || this.radialProgressViews[0].backgroundState == 0) {
                    z = false;
                }
                this.canZoom = z;
            }
            return;
        }
        this.radialProgressViews[a].setBackgroundState(-1, animated);
    }

    private void setIndexToImage(ImageReceiver imageReceiver, int index) {
        ImageReceiver imageReceiver2 = imageReceiver;
        int i = index;
        imageReceiver2.setOrientation(0, false);
        int[] size = new int[1];
        TLObject fileLocation = getFileLocation(i, size);
        if (fileLocation != null) {
            TLObject media = getMedia(i);
            if (media instanceof Photo) {
                Photo photo = (Photo) media;
                BitmapHolder placeHolder = null;
                if (r0.currentThumb != null && imageReceiver2 == r0.centerImage) {
                    placeHolder = r0.currentThumb;
                }
                BitmapHolder placeHolder2 = placeHolder;
                if (size[0] == 0) {
                    size[0] = -1;
                }
                PhotoSize thumbLocation = FileLoader.getClosestPhotoSizeWithSize(photo.sizes, 80);
                imageReceiver2.setImage(fileLocation, null, null, placeHolder2 != null ? new BitmapDrawable(placeHolder2.bitmap) : null, thumbLocation != null ? thumbLocation.location : null, "b", size[0], null, 1);
            } else if (isMediaVideo(i)) {
                if (fileLocation instanceof TL_fileLocationUnavailable) {
                    imageReceiver2.setImageBitmap(r0.parentActivity.getResources().getDrawable(R.drawable.photoview_placeholder));
                } else {
                    BitmapHolder placeHolder3 = null;
                    if (r0.currentThumb != null && imageReceiver2 == r0.centerImage) {
                        placeHolder3 = r0.currentThumb;
                    }
                    BitmapHolder placeHolder4 = placeHolder3;
                    BitmapHolder bitmapHolder = placeHolder4;
                    imageReceiver2.setImage(null, null, null, placeHolder4 != null ? new BitmapDrawable(placeHolder4.bitmap) : null, fileLocation, "b", 0, null, 1);
                }
            } else if (r0.currentAnimation != null) {
                imageReceiver2.setImageBitmap(r0.currentAnimation);
                r0.currentAnimation.setSecondParentView(r0.photoContainerView);
            }
        } else if (size[0] == 0) {
            imageReceiver2.setImageBitmap((Bitmap) null);
        } else {
            imageReceiver2.setImageBitmap(r0.parentActivity.getResources().getDrawable(R.drawable.photoview_placeholder));
        }
    }

    public boolean isShowingImage(PageBlock object) {
        return this.isPhotoVisible && !this.disableShowCheck && object != null && this.currentMedia == object;
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

    public boolean openPhoto(PageBlock block) {
        PageBlock pageBlock = block;
        if (!(this.parentActivity == null || r0.isPhotoVisible || checkPhotoAnimation())) {
            if (pageBlock != null) {
                final PlaceProviderObject object = getPlaceForPhoto(block);
                if (object == null) {
                    return false;
                }
                Rect drawRegion;
                int orientation;
                int animatedOrientation;
                ViewGroup.LayoutParams layoutParams;
                float scaleX;
                float scaleY;
                float scale;
                float height;
                float xPos;
                float yPos;
                int clipHorizontal;
                int clipVertical;
                int[] coords2;
                int clipTop;
                final AnimatorSet animatorSet;
                Animator[] animatorArr;
                NotificationCenter.getInstance(r0.currentAccount).addObserver(r0, NotificationCenter.FileDidFailedLoad);
                NotificationCenter.getInstance(r0.currentAccount).addObserver(r0, NotificationCenter.FileDidLoaded);
                NotificationCenter.getInstance(r0.currentAccount).addObserver(r0, NotificationCenter.FileLoadProgressChanged);
                NotificationCenter.getGlobalInstance().addObserver(r0, NotificationCenter.emojiDidLoaded);
                if (r0.velocityTracker == null) {
                    r0.velocityTracker = VelocityTracker.obtain();
                }
                r0.isPhotoVisible = true;
                toggleActionBar(true, false);
                r0.actionBar.setAlpha(0.0f);
                r0.bottomLayout.setAlpha(0.0f);
                r0.captionTextView.setAlpha(0.0f);
                r0.photoBackgroundDrawable.setAlpha(0);
                r0.disableShowCheck = true;
                r0.photoAnimationInProgress = 1;
                if (pageBlock != null) {
                    r0.currentAnimation = object.imageReceiver.getAnimation();
                }
                int index = r0.photoBlocks.indexOf(pageBlock);
                r0.imagesArr.clear();
                if (pageBlock instanceof TL_pageBlockVideo) {
                    if (!isVideoBlock(block)) {
                        r0.imagesArr.add(pageBlock);
                        index = 0;
                        onPhotoShow(index, object);
                        drawRegion = object.imageReceiver.getDrawRegion();
                        orientation = object.imageReceiver.getOrientation();
                        animatedOrientation = object.imageReceiver.getAnimatedOrientation();
                        if (animatedOrientation != 0) {
                            orientation = animatedOrientation;
                        }
                        r0.animatingImageView.setVisibility(0);
                        r0.animatingImageView.setRadius(object.radius);
                        r0.animatingImageView.setOrientation(orientation);
                        r0.animatingImageView.setNeedRadius(object.radius == 0);
                        r0.animatingImageView.setImageBitmap(object.thumb);
                        r0.animatingImageView.setAlpha(1.0f);
                        r0.animatingImageView.setPivotX(0.0f);
                        r0.animatingImageView.setPivotY(0.0f);
                        r0.animatingImageView.setScaleX(object.scale);
                        r0.animatingImageView.setScaleY(object.scale);
                        r0.animatingImageView.setTranslationX(((float) object.viewX) + (((float) drawRegion.left) * object.scale));
                        r0.animatingImageView.setTranslationY(((float) object.viewY) + (((float) drawRegion.top) * object.scale));
                        layoutParams = r0.animatingImageView.getLayoutParams();
                        layoutParams.width = drawRegion.right - drawRegion.left;
                        layoutParams.height = drawRegion.bottom - drawRegion.top;
                        r0.animatingImageView.setLayoutParams(layoutParams);
                        scaleX = ((float) AndroidUtilities.displaySize.x) / ((float) layoutParams.width);
                        scaleY = ((float) (AndroidUtilities.displaySize.y + AndroidUtilities.statusBarHeight)) / ((float) layoutParams.height);
                        scale = scaleX <= scaleY ? scaleY : scaleX;
                        height = ((float) layoutParams.height) * scale;
                        xPos = (((float) AndroidUtilities.displaySize.x) - (((float) layoutParams.width) * scale)) / 2.0f;
                        if (VERSION.SDK_INT >= 21 && r0.lastInsets != null) {
                            xPos += (float) ((WindowInsets) r0.lastInsets).getSystemWindowInsetLeft();
                        }
                        yPos = (((float) (AndroidUtilities.displaySize.y + AndroidUtilities.statusBarHeight)) - height) / 2.0f;
                        clipHorizontal = Math.abs(drawRegion.left - object.imageReceiver.getImageX());
                        clipVertical = Math.abs(drawRegion.top - object.imageReceiver.getImageY());
                        coords2 = new int[2];
                        object.parentView.getLocationInWindow(coords2);
                        clipTop = (coords2[1] - (object.viewY + drawRegion.top)) + object.clipTopAddition;
                        if (clipTop < 0) {
                            clipTop = 0;
                        }
                        index = clipTop;
                        animatedOrientation = (((object.viewY + drawRegion.top) + layoutParams.height) - (coords2[1] + object.parentView.getHeight())) + object.clipBottomAddition;
                        if (animatedOrientation < 0) {
                            animatedOrientation = 0;
                        }
                        index = Math.max(index, clipVertical);
                        animatedOrientation = Math.max(animatedOrientation, clipVertical);
                        r0.animationValues[0][0] = r0.animatingImageView.getScaleX();
                        r0.animationValues[0][1] = r0.animatingImageView.getScaleY();
                        r0.animationValues[0][2] = r0.animatingImageView.getTranslationX();
                        r0.animationValues[0][3] = r0.animatingImageView.getTranslationY();
                        r0.animationValues[0][4] = ((float) clipHorizontal) * object.scale;
                        r0.animationValues[0][5] = ((float) index) * object.scale;
                        r0.animationValues[0][6] = ((float) animatedOrientation) * object.scale;
                        r0.animationValues[0][7] = (float) r0.animatingImageView.getRadius();
                        r0.animationValues[1][0] = scale;
                        r0.animationValues[1][1] = scale;
                        r0.animationValues[1][2] = xPos;
                        r0.animationValues[1][3] = yPos;
                        r0.animationValues[1][4] = 0.0f;
                        r0.animationValues[1][5] = 0.0f;
                        r0.animationValues[1][6] = 0.0f;
                        r0.animationValues[1][7] = 0.0f;
                        r0.photoContainerView.setVisibility(0);
                        r0.photoContainerBackground.setVisibility(0);
                        r0.animatingImageView.setAnimationProgress(0.0f);
                        animatorSet = new AnimatorSet();
                        animatorArr = new Animator[5];
                        animatorArr[0] = ObjectAnimator.ofFloat(r0.animatingImageView, "animationProgress", new float[]{0.0f, 1.0f});
                        animatorArr[1] = ObjectAnimator.ofInt(r0.photoBackgroundDrawable, "alpha", new int[]{0, 255});
                        animatorArr[2] = ObjectAnimator.ofFloat(r0.actionBar, "alpha", new float[]{0.0f, 1.0f});
                        animatorArr[3] = ObjectAnimator.ofFloat(r0.bottomLayout, "alpha", new float[]{0.0f, 1.0f});
                        animatorArr[4] = ObjectAnimator.ofFloat(r0.captionTextView, "alpha", new float[]{0.0f, 1.0f});
                        animatorSet.playTogether(animatorArr);
                        r0.photoAnimationEndRunnable = new Runnable() {
                            public void run() {
                                if (ArticleViewer.this.photoContainerView != null) {
                                    if (VERSION.SDK_INT >= 18) {
                                        ArticleViewer.this.photoContainerView.setLayerType(0, null);
                                    }
                                    ArticleViewer.this.photoAnimationInProgress = 0;
                                    ArticleViewer.this.photoTransitionAnimationStartTime = 0;
                                    ArticleViewer.this.setImages();
                                    ArticleViewer.this.photoContainerView.invalidate();
                                    ArticleViewer.this.animatingImageView.setVisibility(8);
                                    if (ArticleViewer.this.showAfterAnimation != null) {
                                        ArticleViewer.this.showAfterAnimation.imageReceiver.setVisible(true, true);
                                    }
                                    if (ArticleViewer.this.hideAfterAnimation != null) {
                                        ArticleViewer.this.hideAfterAnimation.imageReceiver.setVisible(false, true);
                                    }
                                }
                            }
                        };
                        animatorSet.setDuration(200);
                        animatorSet.addListener(new AnimatorListenerAdapter() {
                            public void onAnimationEnd(Animator animation) {
                                AndroidUtilities.runOnUIThread(new Runnable() {
                                    public void run() {
                                        NotificationCenter.getInstance(ArticleViewer.this.currentAccount).setAnimationInProgress(false);
                                        if (ArticleViewer.this.photoAnimationEndRunnable != null) {
                                            ArticleViewer.this.photoAnimationEndRunnable.run();
                                            ArticleViewer.this.photoAnimationEndRunnable = null;
                                        }
                                    }
                                });
                            }
                        });
                        r0.photoTransitionAnimationStartTime = System.currentTimeMillis();
                        AndroidUtilities.runOnUIThread(new Runnable() {
                            public void run() {
                                NotificationCenter.getInstance(ArticleViewer.this.currentAccount).setAllowedNotificationsDutingAnimation(new int[]{NotificationCenter.dialogsNeedReload, NotificationCenter.closeChats});
                                NotificationCenter.getInstance(ArticleViewer.this.currentAccount).setAnimationInProgress(true);
                                animatorSet.start();
                            }
                        });
                        if (VERSION.SDK_INT >= 18) {
                            r0.photoContainerView.setLayerType(2, null);
                        }
                        r0.photoBackgroundDrawable.drawRunnable = new Runnable() {
                            public void run() {
                                ArticleViewer.this.disableShowCheck = false;
                                object.imageReceiver.setVisible(false, true);
                            }
                        };
                        return true;
                    }
                }
                r0.imagesArr.addAll(r0.photoBlocks);
                onPhotoShow(index, object);
                drawRegion = object.imageReceiver.getDrawRegion();
                orientation = object.imageReceiver.getOrientation();
                animatedOrientation = object.imageReceiver.getAnimatedOrientation();
                if (animatedOrientation != 0) {
                    orientation = animatedOrientation;
                }
                r0.animatingImageView.setVisibility(0);
                r0.animatingImageView.setRadius(object.radius);
                r0.animatingImageView.setOrientation(orientation);
                if (object.radius == 0) {
                }
                r0.animatingImageView.setNeedRadius(object.radius == 0);
                r0.animatingImageView.setImageBitmap(object.thumb);
                r0.animatingImageView.setAlpha(1.0f);
                r0.animatingImageView.setPivotX(0.0f);
                r0.animatingImageView.setPivotY(0.0f);
                r0.animatingImageView.setScaleX(object.scale);
                r0.animatingImageView.setScaleY(object.scale);
                r0.animatingImageView.setTranslationX(((float) object.viewX) + (((float) drawRegion.left) * object.scale));
                r0.animatingImageView.setTranslationY(((float) object.viewY) + (((float) drawRegion.top) * object.scale));
                layoutParams = r0.animatingImageView.getLayoutParams();
                layoutParams.width = drawRegion.right - drawRegion.left;
                layoutParams.height = drawRegion.bottom - drawRegion.top;
                r0.animatingImageView.setLayoutParams(layoutParams);
                scaleX = ((float) AndroidUtilities.displaySize.x) / ((float) layoutParams.width);
                scaleY = ((float) (AndroidUtilities.displaySize.y + AndroidUtilities.statusBarHeight)) / ((float) layoutParams.height);
                if (scaleX <= scaleY) {
                }
                height = ((float) layoutParams.height) * scale;
                xPos = (((float) AndroidUtilities.displaySize.x) - (((float) layoutParams.width) * scale)) / 2.0f;
                xPos += (float) ((WindowInsets) r0.lastInsets).getSystemWindowInsetLeft();
                yPos = (((float) (AndroidUtilities.displaySize.y + AndroidUtilities.statusBarHeight)) - height) / 2.0f;
                clipHorizontal = Math.abs(drawRegion.left - object.imageReceiver.getImageX());
                clipVertical = Math.abs(drawRegion.top - object.imageReceiver.getImageY());
                coords2 = new int[2];
                object.parentView.getLocationInWindow(coords2);
                clipTop = (coords2[1] - (object.viewY + drawRegion.top)) + object.clipTopAddition;
                if (clipTop < 0) {
                    clipTop = 0;
                }
                index = clipTop;
                animatedOrientation = (((object.viewY + drawRegion.top) + layoutParams.height) - (coords2[1] + object.parentView.getHeight())) + object.clipBottomAddition;
                if (animatedOrientation < 0) {
                    animatedOrientation = 0;
                }
                index = Math.max(index, clipVertical);
                animatedOrientation = Math.max(animatedOrientation, clipVertical);
                r0.animationValues[0][0] = r0.animatingImageView.getScaleX();
                r0.animationValues[0][1] = r0.animatingImageView.getScaleY();
                r0.animationValues[0][2] = r0.animatingImageView.getTranslationX();
                r0.animationValues[0][3] = r0.animatingImageView.getTranslationY();
                r0.animationValues[0][4] = ((float) clipHorizontal) * object.scale;
                r0.animationValues[0][5] = ((float) index) * object.scale;
                r0.animationValues[0][6] = ((float) animatedOrientation) * object.scale;
                r0.animationValues[0][7] = (float) r0.animatingImageView.getRadius();
                r0.animationValues[1][0] = scale;
                r0.animationValues[1][1] = scale;
                r0.animationValues[1][2] = xPos;
                r0.animationValues[1][3] = yPos;
                r0.animationValues[1][4] = 0.0f;
                r0.animationValues[1][5] = 0.0f;
                r0.animationValues[1][6] = 0.0f;
                r0.animationValues[1][7] = 0.0f;
                r0.photoContainerView.setVisibility(0);
                r0.photoContainerBackground.setVisibility(0);
                r0.animatingImageView.setAnimationProgress(0.0f);
                animatorSet = new AnimatorSet();
                animatorArr = new Animator[5];
                animatorArr[0] = ObjectAnimator.ofFloat(r0.animatingImageView, "animationProgress", new float[]{0.0f, 1.0f});
                animatorArr[1] = ObjectAnimator.ofInt(r0.photoBackgroundDrawable, "alpha", new int[]{0, 255});
                animatorArr[2] = ObjectAnimator.ofFloat(r0.actionBar, "alpha", new float[]{0.0f, 1.0f});
                animatorArr[3] = ObjectAnimator.ofFloat(r0.bottomLayout, "alpha", new float[]{0.0f, 1.0f});
                animatorArr[4] = ObjectAnimator.ofFloat(r0.captionTextView, "alpha", new float[]{0.0f, 1.0f});
                animatorSet.playTogether(animatorArr);
                r0.photoAnimationEndRunnable = /* anonymous class already generated */;
                animatorSet.setDuration(200);
                animatorSet.addListener(/* anonymous class already generated */);
                r0.photoTransitionAnimationStartTime = System.currentTimeMillis();
                AndroidUtilities.runOnUIThread(/* anonymous class already generated */);
                if (VERSION.SDK_INT >= 18) {
                    r0.photoContainerView.setLayerType(2, null);
                }
                r0.photoBackgroundDrawable.drawRunnable = /* anonymous class already generated */;
                return true;
            }
        }
        return false;
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void closePhoto(boolean r27) {
        /*
        r26 = this;
        r0 = r26;
        r1 = r0.parentActivity;
        if (r1 == 0) goto L_0x0429;
    L_0x0006:
        r1 = r0.isPhotoVisible;
        if (r1 == 0) goto L_0x0429;
    L_0x000a:
        r1 = r26.checkPhotoAnimation();
        if (r1 == 0) goto L_0x0012;
    L_0x0010:
        goto L_0x0429;
    L_0x0012:
        r26.releasePlayer();
        r1 = r0.currentAccount;
        r1 = org.telegram.messenger.NotificationCenter.getInstance(r1);
        r2 = org.telegram.messenger.NotificationCenter.FileDidFailedLoad;
        r1.removeObserver(r0, r2);
        r1 = r0.currentAccount;
        r1 = org.telegram.messenger.NotificationCenter.getInstance(r1);
        r2 = org.telegram.messenger.NotificationCenter.FileDidLoaded;
        r1.removeObserver(r0, r2);
        r1 = r0.currentAccount;
        r1 = org.telegram.messenger.NotificationCenter.getInstance(r1);
        r2 = org.telegram.messenger.NotificationCenter.FileLoadProgressChanged;
        r1.removeObserver(r0, r2);
        r1 = org.telegram.messenger.NotificationCenter.getGlobalInstance();
        r2 = org.telegram.messenger.NotificationCenter.needSetDayNightTheme;
        r1.removeObserver(r0, r2);
        r1 = org.telegram.messenger.NotificationCenter.getGlobalInstance();
        r2 = org.telegram.messenger.NotificationCenter.emojiDidLoaded;
        r1.removeObserver(r0, r2);
        r1 = 0;
        r0.isActionBarVisible = r1;
        r2 = r0.velocityTracker;
        r3 = 0;
        if (r2 == 0) goto L_0x0057;
    L_0x0050:
        r2 = r0.velocityTracker;
        r2.recycle();
        r0.velocityTracker = r3;
    L_0x0057:
        r2 = r0.currentMedia;
        r2 = r0.getPlaceForPhoto(r2);
        r13 = 1;
        if (r27 == 0) goto L_0x037d;
    L_0x0060:
        r0.photoAnimationInProgress = r13;
        r14 = r0.animatingImageView;
        r14.setVisibility(r1);
        r14 = r0.photoContainerView;
        r14.invalidate();
        r14 = new android.animation.AnimatorSet;
        r14.<init>();
        r3 = r0.animatingImageView;
        r3 = r3.getLayoutParams();
        r15 = 0;
        r4 = r0.centerImage;
        r4 = r4.getOrientation();
        r16 = 0;
        if (r2 == 0) goto L_0x008c;
    L_0x0082:
        r5 = r2.imageReceiver;
        if (r5 == 0) goto L_0x008c;
    L_0x0086:
        r5 = r2.imageReceiver;
        r16 = r5.getAnimatedOrientation();
    L_0x008c:
        if (r16 == 0) goto L_0x0090;
    L_0x008e:
        r4 = r16;
    L_0x0090:
        r5 = r0.animatingImageView;
        r5.setOrientation(r4);
        if (r2 == 0) goto L_0x00bf;
    L_0x0097:
        r5 = r0.animatingImageView;
        r6 = r2.radius;
        if (r6 == 0) goto L_0x009f;
    L_0x009d:
        r6 = r13;
        goto L_0x00a0;
    L_0x009f:
        r6 = r1;
    L_0x00a0:
        r5.setNeedRadius(r6);
        r5 = r2.imageReceiver;
        r5 = r5.getDrawRegion();
        r6 = r5.right;
        r7 = r5.left;
        r6 = r6 - r7;
        r3.width = r6;
        r6 = r5.bottom;
        r7 = r5.top;
        r6 = r6 - r7;
        r3.height = r6;
        r6 = r0.animatingImageView;
        r7 = r2.thumb;
        r6.setImageBitmap(r7);
        goto L_0x00e0;
    L_0x00bf:
        r5 = r0.animatingImageView;
        r5.setNeedRadius(r1);
        r5 = r0.centerImage;
        r5 = r5.getImageWidth();
        r3.width = r5;
        r5 = r0.centerImage;
        r5 = r5.getImageHeight();
        r3.height = r5;
        r5 = r0.animatingImageView;
        r6 = r0.centerImage;
        r6 = r6.getBitmapSafe();
        r5.setImageBitmap(r6);
        r5 = r15;
    L_0x00e0:
        r6 = r0.animatingImageView;
        r6.setLayoutParams(r3);
        r6 = org.telegram.messenger.AndroidUtilities.displaySize;
        r6 = r6.x;
        r6 = (float) r6;
        r7 = r3.width;
        r7 = (float) r7;
        r6 = r6 / r7;
        r7 = org.telegram.messenger.AndroidUtilities.displaySize;
        r7 = r7.y;
        r15 = org.telegram.messenger.AndroidUtilities.statusBarHeight;
        r7 = r7 + r15;
        r7 = (float) r7;
        r8 = r3.height;
        r8 = (float) r8;
        r7 = r7 / r8;
        r8 = (r6 > r7 ? 1 : (r6 == r7 ? 0 : -1));
        if (r8 <= 0) goto L_0x0100;
    L_0x00fe:
        r8 = r7;
        goto L_0x0101;
    L_0x0100:
        r8 = r6;
    L_0x0101:
        r9 = r3.width;
        r9 = (float) r9;
        r12 = r0.scale;
        r9 = r9 * r12;
        r9 = r9 * r8;
        r12 = r3.height;
        r12 = (float) r12;
        r10 = r0.scale;
        r12 = r12 * r10;
        r12 = r12 * r8;
        r10 = org.telegram.messenger.AndroidUtilities.displaySize;
        r10 = r10.x;
        r10 = (float) r10;
        r10 = r10 - r9;
        r15 = 1073741824; // 0x40000000 float:2.0 double:5.304989477E-315;
        r10 = r10 / r15;
        r11 = android.os.Build.VERSION.SDK_INT;
        r1 = 21;
        if (r11 < r1) goto L_0x012c;
    L_0x011e:
        r1 = r0.lastInsets;
        if (r1 == 0) goto L_0x012c;
    L_0x0122:
        r1 = r0.lastInsets;
        r1 = (android.view.WindowInsets) r1;
        r1 = r1.getSystemWindowInsetLeft();
        r1 = (float) r1;
        r10 = r10 + r1;
    L_0x012c:
        r1 = org.telegram.messenger.AndroidUtilities.displaySize;
        r1 = r1.y;
        r11 = org.telegram.messenger.AndroidUtilities.statusBarHeight;
        r1 = r1 + r11;
        r1 = (float) r1;
        r1 = r1 - r12;
        r1 = r1 / r15;
        r11 = r0.animatingImageView;
        r13 = r0.translationX;
        r13 = r13 + r10;
        r11.setTranslationX(r13);
        r11 = r0.animatingImageView;
        r13 = r0.translationY;
        r13 = r13 + r1;
        r11.setTranslationY(r13);
        r11 = r0.animatingImageView;
        r13 = r0.scale;
        r13 = r13 * r8;
        r11.setScaleX(r13);
        r11 = r0.animatingImageView;
        r13 = r0.scale;
        r13 = r13 * r8;
        r11.setScaleY(r13);
        if (r2 == 0) goto L_0x02ce;
    L_0x0158:
        r11 = r2.imageReceiver;
        r17 = r1;
        r1 = 1;
        r13 = 0;
        r11.setVisible(r13, r1);
        r1 = r5.left;
        r11 = r2.imageReceiver;
        r11 = r11.getImageX();
        r1 = r1 - r11;
        r1 = java.lang.Math.abs(r1);
        r11 = r5.top;
        r13 = r2.imageReceiver;
        r13 = r13.getImageY();
        r11 = r11 - r13;
        r11 = java.lang.Math.abs(r11);
        r18 = r3;
        r13 = 2;
        r3 = new int[r13];
        r13 = r2.parentView;
        r13.getLocationInWindow(r3);
        r13 = 1;
        r15 = r3[r13];
        r13 = r2.viewY;
        r19 = r4;
        r4 = r5.top;
        r13 = r13 + r4;
        r15 = r15 - r13;
        r4 = r2.clipTopAddition;
        r15 = r15 + r4;
        if (r15 >= 0) goto L_0x0196;
    L_0x0195:
        r15 = 0;
    L_0x0196:
        r4 = r15;
        r13 = r2.viewY;
        r20 = r6;
        r6 = r5.top;
        r13 = r13 + r6;
        r6 = r5.bottom;
        r21 = r7;
        r7 = r5.top;
        r6 = r6 - r7;
        r13 = r13 + r6;
        r6 = 1;
        r7 = r3[r6];
        r6 = r2.parentView;
        r6 = r6.getHeight();
        r7 = r7 + r6;
        r13 = r13 - r7;
        r6 = r2.clipBottomAddition;
        r13 = r13 + r6;
        if (r13 >= 0) goto L_0x01b7;
    L_0x01b6:
        r13 = 0;
    L_0x01b7:
        r4 = java.lang.Math.max(r4, r11);
        r6 = java.lang.Math.max(r13, r11);
        r7 = r0.animationValues;
        r13 = 0;
        r7 = r7[r13];
        r22 = r3;
        r3 = r0.animatingImageView;
        r3 = r3.getScaleX();
        r7[r13] = r3;
        r3 = r0.animationValues;
        r3 = r3[r13];
        r7 = r0.animatingImageView;
        r7 = r7.getScaleY();
        r15 = 1;
        r3[r15] = r7;
        r3 = r0.animationValues;
        r3 = r3[r13];
        r7 = r0.animatingImageView;
        r7 = r7.getTranslationX();
        r15 = 2;
        r3[r15] = r7;
        r3 = r0.animationValues;
        r3 = r3[r13];
        r7 = r0.animatingImageView;
        r7 = r7.getTranslationY();
        r15 = 3;
        r3[r15] = r7;
        r3 = r0.animationValues;
        r3 = r3[r13];
        r7 = 0;
        r15 = 4;
        r3[r15] = r7;
        r3 = r0.animationValues;
        r3 = r3[r13];
        r15 = 5;
        r3[r15] = r7;
        r3 = r0.animationValues;
        r3 = r3[r13];
        r15 = 6;
        r3[r15] = r7;
        r3 = r0.animationValues;
        r3 = r3[r13];
        r15 = 7;
        r3[r15] = r7;
        r3 = r0.animationValues;
        r7 = 1;
        r3 = r3[r7];
        r7 = r2.scale;
        r3[r13] = r7;
        r3 = r0.animationValues;
        r7 = 1;
        r3 = r3[r7];
        r13 = r2.scale;
        r3[r7] = r13;
        r3 = r0.animationValues;
        r3 = r3[r7];
        r13 = r2.viewX;
        r13 = (float) r13;
        r7 = r5.left;
        r7 = (float) r7;
        r23 = r8;
        r8 = r2.scale;
        r7 = r7 * r8;
        r13 = r13 + r7;
        r7 = 2;
        r3[r7] = r13;
        r3 = r0.animationValues;
        r7 = 1;
        r3 = r3[r7];
        r8 = r2.viewY;
        r8 = (float) r8;
        r13 = r5.top;
        r13 = (float) r13;
        r7 = r2.scale;
        r13 = r13 * r7;
        r8 = r8 + r13;
        r7 = 3;
        r3[r7] = r8;
        r3 = r0.animationValues;
        r7 = 1;
        r3 = r3[r7];
        r8 = (float) r1;
        r13 = r2.scale;
        r8 = r8 * r13;
        r13 = 4;
        r3[r13] = r8;
        r3 = r0.animationValues;
        r3 = r3[r7];
        r8 = (float) r4;
        r13 = r2.scale;
        r8 = r8 * r13;
        r13 = 5;
        r3[r13] = r8;
        r3 = r0.animationValues;
        r3 = r3[r7];
        r8 = (float) r6;
        r13 = r2.scale;
        r8 = r8 * r13;
        r13 = 6;
        r3[r13] = r8;
        r3 = r0.animationValues;
        r3 = r3[r7];
        r7 = r2.radius;
        r7 = (float) r7;
        r3[r15] = r7;
        r3 = 5;
        r3 = new android.animation.Animator[r3];
        r7 = r0.animatingImageView;
        r8 = "animationProgress";
        r24 = r1;
        r13 = 2;
        r1 = new float[r13];
        r1 = {0, 1065353216};
        r1 = android.animation.ObjectAnimator.ofFloat(r7, r8, r1);
        r7 = 0;
        r3[r7] = r1;
        r1 = r0.photoBackgroundDrawable;
        r8 = "alpha";
        r25 = r4;
        r13 = 1;
        r4 = new int[r13];
        r4[r7] = r7;
        r1 = android.animation.ObjectAnimator.ofInt(r1, r8, r4);
        r3[r13] = r1;
        r1 = r0.actionBar;
        r4 = "alpha";
        r8 = new float[r13];
        r15 = 0;
        r8[r7] = r15;
        r1 = android.animation.ObjectAnimator.ofFloat(r1, r4, r8);
        r4 = 2;
        r3[r4] = r1;
        r1 = r0.bottomLayout;
        r4 = "alpha";
        r8 = new float[r13];
        r8[r7] = r15;
        r1 = android.animation.ObjectAnimator.ofFloat(r1, r4, r8);
        r4 = 3;
        r3[r4] = r1;
        r1 = r0.captionTextView;
        r4 = "alpha";
        r8 = new float[r13];
        r8[r7] = r15;
        r1 = android.animation.ObjectAnimator.ofFloat(r1, r4, r8);
        r4 = 4;
        r3[r4] = r1;
        r14.playTogether(r3);
        goto L_0x034f;
    L_0x02ce:
        r17 = r1;
        r18 = r3;
        r19 = r4;
        r20 = r6;
        r21 = r7;
        r23 = r8;
        r1 = org.telegram.messenger.AndroidUtilities.displaySize;
        r1 = r1.y;
        r3 = org.telegram.messenger.AndroidUtilities.statusBarHeight;
        r1 = r1 + r3;
        r3 = 6;
        r3 = new android.animation.Animator[r3];
        r4 = r0.photoBackgroundDrawable;
        r6 = "alpha";
        r7 = 1;
        r8 = new int[r7];
        r11 = 0;
        r8[r11] = r11;
        r4 = android.animation.ObjectAnimator.ofInt(r4, r6, r8);
        r3[r11] = r4;
        r4 = r0.animatingImageView;
        r6 = "alpha";
        r8 = new float[r7];
        r13 = 0;
        r8[r11] = r13;
        r4 = android.animation.ObjectAnimator.ofFloat(r4, r6, r8);
        r3[r7] = r4;
        r4 = r0.animatingImageView;
        r6 = "translationY";
        r8 = new float[r7];
        r7 = r0.translationY;
        r7 = (r7 > r13 ? 1 : (r7 == r13 ? 0 : -1));
        if (r7 < 0) goto L_0x0311;
    L_0x030f:
        r7 = (float) r1;
        goto L_0x0313;
    L_0x0311:
        r7 = -r1;
        r7 = (float) r7;
    L_0x0313:
        r11 = 0;
        r8[r11] = r7;
        r4 = android.animation.ObjectAnimator.ofFloat(r4, r6, r8);
        r6 = 2;
        r3[r6] = r4;
        r4 = r0.actionBar;
        r6 = "alpha";
        r7 = 1;
        r8 = new float[r7];
        r13 = 0;
        r8[r11] = r13;
        r4 = android.animation.ObjectAnimator.ofFloat(r4, r6, r8);
        r6 = 3;
        r3[r6] = r4;
        r4 = r0.bottomLayout;
        r6 = "alpha";
        r8 = new float[r7];
        r8[r11] = r13;
        r4 = android.animation.ObjectAnimator.ofFloat(r4, r6, r8);
        r6 = 4;
        r3[r6] = r4;
        r4 = r0.captionTextView;
        r6 = "alpha";
        r7 = new float[r7];
        r7[r11] = r13;
        r4 = android.animation.ObjectAnimator.ofFloat(r4, r6, r7);
        r6 = 5;
        r3[r6] = r4;
        r14.playTogether(r3);
    L_0x034f:
        r1 = new org.telegram.ui.ArticleViewer$47;
        r1.<init>(r2);
        r0.photoAnimationEndRunnable = r1;
        r3 = 200; // 0xc8 float:2.8E-43 double:9.9E-322;
        r14.setDuration(r3);
        r1 = new org.telegram.ui.ArticleViewer$48;
        r1.<init>();
        r14.addListener(r1);
        r3 = java.lang.System.currentTimeMillis();
        r0.photoTransitionAnimationStartTime = r3;
        r1 = android.os.Build.VERSION.SDK_INT;
        r3 = 18;
        if (r1 < r3) goto L_0x0376;
    L_0x036f:
        r1 = r0.photoContainerView;
        r3 = 2;
        r4 = 0;
        r1.setLayerType(r3, r4);
    L_0x0376:
        r14.start();
        r5 = 0;
        goto L_0x0415;
    L_0x037d:
        r1 = new android.animation.AnimatorSet;
        r1.<init>();
        r3 = 6;
        r3 = new android.animation.Animator[r3];
        r4 = r0.photoContainerView;
        r5 = "scaleX";
        r6 = 1;
        r7 = new float[r6];
        r8 = 1063675494; // 0x3f666666 float:0.9 double:5.2552552E-315;
        r9 = 0;
        r7[r9] = r8;
        r4 = android.animation.ObjectAnimator.ofFloat(r4, r5, r7);
        r3[r9] = r4;
        r4 = r0.photoContainerView;
        r5 = "scaleY";
        r7 = new float[r6];
        r7[r9] = r8;
        r4 = android.animation.ObjectAnimator.ofFloat(r4, r5, r7);
        r3[r6] = r4;
        r4 = r0.photoBackgroundDrawable;
        r5 = "alpha";
        r7 = new int[r6];
        r7[r9] = r9;
        r4 = android.animation.ObjectAnimator.ofInt(r4, r5, r7);
        r5 = 2;
        r3[r5] = r4;
        r4 = r0.actionBar;
        r5 = "alpha";
        r7 = new float[r6];
        r8 = 0;
        r7[r9] = r8;
        r4 = android.animation.ObjectAnimator.ofFloat(r4, r5, r7);
        r5 = 3;
        r3[r5] = r4;
        r4 = r0.bottomLayout;
        r5 = "alpha";
        r7 = new float[r6];
        r7[r9] = r8;
        r4 = android.animation.ObjectAnimator.ofFloat(r4, r5, r7);
        r5 = 4;
        r3[r5] = r4;
        r4 = r0.captionTextView;
        r5 = "alpha";
        r6 = new float[r6];
        r6[r9] = r8;
        r4 = android.animation.ObjectAnimator.ofFloat(r4, r5, r6);
        r5 = 5;
        r3[r5] = r4;
        r1.playTogether(r3);
        r3 = 2;
        r0.photoAnimationInProgress = r3;
        r3 = new org.telegram.ui.ArticleViewer$49;
        r3.<init>(r2);
        r0.photoAnimationEndRunnable = r3;
        r3 = 200; // 0xc8 float:2.8E-43 double:9.9E-322;
        r1.setDuration(r3);
        r3 = new org.telegram.ui.ArticleViewer$50;
        r3.<init>();
        r1.addListener(r3);
        r3 = java.lang.System.currentTimeMillis();
        r0.photoTransitionAnimationStartTime = r3;
        r3 = android.os.Build.VERSION.SDK_INT;
        r4 = 18;
        if (r3 < r4) goto L_0x0411;
    L_0x0409:
        r3 = r0.photoContainerView;
        r4 = 2;
        r5 = 0;
        r3.setLayerType(r4, r5);
        goto L_0x0412;
    L_0x0411:
        r5 = 0;
    L_0x0412:
        r1.start();
    L_0x0415:
        r1 = r0.currentAnimation;
        if (r1 == 0) goto L_0x0428;
    L_0x0419:
        r1 = r0.currentAnimation;
        r1.setSecondParentView(r5);
        r0.currentAnimation = r5;
        r1 = r0.centerImage;
        r3 = r5;
        r3 = (android.graphics.drawable.Drawable) r3;
        r1.setImageBitmap(r3);
    L_0x0428:
        return;
    L_0x0429:
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.telegram.ui.ArticleViewer.closePhoto(boolean):void");
    }

    private void onPhotoClosed(PlaceProviderObject object) {
        this.isPhotoVisible = false;
        this.disableShowCheck = true;
        this.currentMedia = null;
        if (this.currentThumb != null) {
            this.currentThumb.release();
            this.currentThumb = null;
        }
        if (this.currentAnimation != null) {
            this.currentAnimation.setSecondParentView(null);
            this.currentAnimation = null;
        }
        for (int a = 0; a < 3; a++) {
            if (this.radialProgressViews[a] != null) {
                this.radialProgressViews[a].setBackgroundState(-1, false);
            }
        }
        Bitmap bitmap = (Bitmap) null;
        this.centerImage.setImageBitmap(bitmap);
        this.leftImage.setImageBitmap(bitmap);
        this.rightImage.setImageBitmap(bitmap);
        this.photoContainerView.post(new Runnable() {
            public void run() {
                ArticleViewer.this.animatingImageView.setImageBitmap(null);
            }
        });
        this.disableShowCheck = false;
        if (object != null) {
            object.imageReceiver.setVisible(true, true);
        }
    }

    public void onPause() {
        if (this.currentAnimation != null) {
            closePhoto(false);
        }
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
        return this.photoContainerView.getWidth();
    }

    private int getContainerViewHeight() {
        return this.photoContainerView.getHeight();
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
                        float moveDy;
                        if (ev.getActionMasked() == 2) {
                            if (this.canZoom && ev.getPointerCount() == 2 && !this.draggingDown && this.zooming && !this.changingPage) {
                                this.discardTap = true;
                                this.scale = (((float) Math.hypot((double) (ev.getX(1) - ev.getX(0)), (double) (ev.getY(1) - ev.getY(0)))) / this.pinchStartDistance) * this.pinchStartScale;
                                this.translationX = (this.pinchCenterX - ((float) (getContainerViewWidth() / 2))) - (((this.pinchCenterX - ((float) (getContainerViewWidth() / 2))) - this.pinchStartX) * (this.scale / this.pinchStartScale));
                                this.translationY = (this.pinchCenterY - ((float) (getContainerViewHeight() / 2))) - (((this.pinchCenterY - ((float) (getContainerViewHeight() / 2))) - this.pinchStartY) * (this.scale / this.pinchStartScale));
                                updateMinMax(this.scale);
                                this.photoContainerView.invalidate();
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
                                    this.photoContainerView.invalidate();
                                } else if (this.invalidCoords || this.animationStartTime != 0) {
                                    this.invalidCoords = false;
                                    this.moveStartX = ev.getX();
                                    this.moveStartY = ev.getY();
                                } else {
                                    moveDx = this.moveStartX - ev.getX();
                                    moveDy = this.moveStartY - ev.getY();
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
                                        if ((this.translationX < this.minX && !this.rightImage.hasImage()) || (this.translationX > this.maxX && !this.leftImage.hasImage())) {
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
                                        this.photoContainerView.invalidate();
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
                                    closePhoto(true);
                                } else {
                                    animateTo(1.0f, 0.0f, 0.0f, false);
                                }
                                this.draggingDown = false;
                            } else if (this.moving) {
                                dx = this.translationX;
                                moveDy = this.translationY;
                                updateMinMax(this.scale);
                                this.moving = false;
                                this.canDragDown = true;
                                float velocity = 0.0f;
                                if (this.velocityTracker != null && this.scale == 1.0f) {
                                    this.velocityTracker.computeCurrentVelocity(1000);
                                    velocity = this.velocityTracker.getXVelocity();
                                }
                                if ((this.translationX < this.minX - ((float) (getContainerViewWidth() / 3)) || velocity < ((float) (-AndroidUtilities.dp(650.0f)))) && this.rightImage.hasImage()) {
                                    goToNext();
                                    return true;
                                } else if ((this.translationX > this.maxX + ((float) (getContainerViewWidth() / 3)) || velocity > ((float) AndroidUtilities.dp(650.0f))) && this.leftImage.hasImage()) {
                                    goToPrev();
                                    return true;
                                } else {
                                    if (this.translationX < this.minX) {
                                        dx = this.minX;
                                    } else if (this.translationX > this.maxX) {
                                        dx = this.maxX;
                                    }
                                    if (this.translationY < this.minY) {
                                        moveDy = this.minY;
                                    } else if (this.translationY > this.maxY) {
                                        moveDy = this.maxY;
                                    }
                                    animateTo(this.scale, dx, moveDy, false);
                                }
                            }
                        }
                        return false;
                    }
                }
                this.discardTap = false;
                if (!this.scroller.isFinished()) {
                    this.scroller.abortAnimation();
                }
                if (!(this.draggingDown || this.changingPage)) {
                    if (this.canZoom && ev.getPointerCount() == 2) {
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

    private void goToNext() {
        float extra = 0.0f;
        if (this.scale != 1.0f) {
            extra = ((float) ((getContainerViewWidth() - this.centerImage.getImageWidth()) / 2)) * this.scale;
        }
        this.switchImageAfterAnimation = 1;
        animateTo(this.scale, ((this.minX - ((float) getContainerViewWidth())) - extra) - ((float) (AndroidUtilities.dp(30.0f) / 2)), this.translationY, false);
    }

    private void goToPrev() {
        float extra = 0.0f;
        if (this.scale != 1.0f) {
            extra = ((float) ((getContainerViewWidth() - this.centerImage.getImageWidth()) / 2)) * this.scale;
        }
        this.switchImageAfterAnimation = 2;
        animateTo(this.scale, ((this.maxX + ((float) getContainerViewWidth())) + extra) + ((float) (AndroidUtilities.dp(30.0f) / 2)), this.translationY, false);
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
                    ArticleViewer.this.imageMoveAnimation = null;
                    ArticleViewer.this.photoContainerView.invalidate();
                }
            });
            this.imageMoveAnimation.start();
        }
    }

    @Keep
    public void setAnimationValue(float value) {
        this.animationValue = value;
        this.photoContainerView.invalidate();
    }

    @Keep
    public float getAnimationValue() {
        return this.animationValue;
    }

    private void drawContent(Canvas canvas) {
        Canvas canvas2 = canvas;
        if (this.photoAnimationInProgress != 1) {
            if (r0.isPhotoVisible || r0.photoAnimationInProgress == 2) {
                float ts;
                float tx;
                float ty;
                float currentScale;
                float currentTranslationY;
                float currentTranslationX;
                float alpha;
                int bitmapWidth;
                float scaleY;
                int width;
                int height;
                int width2;
                float f;
                ImageReceiver imageReceiver;
                float aty = -1.0f;
                if (r0.imageMoveAnimation != null) {
                    if (!r0.scroller.isFinished()) {
                        r0.scroller.abortAnimation();
                    }
                    ts = r0.scale + ((r0.animateToScale - r0.scale) * r0.animationValue);
                    tx = r0.translationX + ((r0.animateToX - r0.translationX) * r0.animationValue);
                    ty = r0.translationY + ((r0.animateToY - r0.translationY) * r0.animationValue);
                    if (r0.animateToScale == 1.0f && r0.scale == 1.0f && r0.translationX == 0.0f) {
                        aty = ty;
                    }
                    currentScale = ts;
                    currentTranslationY = ty;
                    currentTranslationX = tx;
                    r0.photoContainerView.invalidate();
                } else {
                    if (r0.animationStartTime != 0) {
                        r0.translationX = r0.animateToX;
                        r0.translationY = r0.animateToY;
                        r0.scale = r0.animateToScale;
                        r0.animationStartTime = 0;
                        updateMinMax(r0.scale);
                        r0.zoomAnimation = false;
                    }
                    if (!r0.scroller.isFinished() && r0.scroller.computeScrollOffset()) {
                        if (((float) r0.scroller.getStartX()) < r0.maxX && ((float) r0.scroller.getStartX()) > r0.minX) {
                            r0.translationX = (float) r0.scroller.getCurrX();
                        }
                        if (((float) r0.scroller.getStartY()) < r0.maxY && ((float) r0.scroller.getStartY()) > r0.minY) {
                            r0.translationY = (float) r0.scroller.getCurrY();
                        }
                        r0.photoContainerView.invalidate();
                    }
                    if (r0.switchImageAfterAnimation != 0) {
                        if (r0.switchImageAfterAnimation == 1) {
                            setImageIndex(r0.currentIndex + 1, false);
                        } else if (r0.switchImageAfterAnimation == 2) {
                            setImageIndex(r0.currentIndex - 1, false);
                        }
                        r0.switchImageAfterAnimation = 0;
                    }
                    currentScale = r0.scale;
                    currentTranslationY = r0.translationY;
                    currentTranslationX = r0.translationX;
                    if (!r0.moving) {
                        aty = r0.translationY;
                    }
                }
                if (r0.scale != 1.0f || aty == -1.0f || r0.zoomAnimation) {
                    r0.photoBackgroundDrawable.setAlpha(255);
                } else {
                    ts = ((float) getContainerViewHeight()) / 4.0f;
                    r0.photoBackgroundDrawable.setAlpha((int) Math.max(127.0f, 255.0f * (1.0f - (Math.min(Math.abs(aty), ts) / ts))));
                }
                ImageReceiver sideImage = null;
                if (!(r0.scale < 1.0f || r0.zoomAnimation || r0.zooming)) {
                    if (currentTranslationX > r0.maxX + ((float) AndroidUtilities.dp(5.0f))) {
                        sideImage = r0.leftImage;
                    } else if (currentTranslationX < r0.minX - ((float) AndroidUtilities.dp(5.0f))) {
                        sideImage = r0.rightImage;
                    }
                }
                r0.changingPage = sideImage != null;
                if (sideImage == r0.rightImage) {
                    tx = currentTranslationX;
                    float scaleDiff = 0.0f;
                    float alpha2 = 1.0f;
                    if (!r0.zoomAnimation && tx < r0.minX) {
                        alpha2 = Math.min(1.0f, (r0.minX - tx) / ((float) canvas.getWidth()));
                        scaleDiff = (1.0f - alpha2) * 0.3f;
                        tx = (float) ((-canvas.getWidth()) - (AndroidUtilities.dp(30.0f) / 2));
                    }
                    alpha = alpha2;
                    if (sideImage.hasBitmapImage()) {
                        canvas.save();
                        canvas2.translate((float) (getContainerViewWidth() / 2), (float) (getContainerViewHeight() / 2));
                        canvas2.translate(((float) (canvas.getWidth() + (AndroidUtilities.dp(30.0f) / 2))) + tx, 0.0f);
                        canvas2.scale(1.0f - scaleDiff, 1.0f - scaleDiff);
                        bitmapWidth = sideImage.getBitmapWidth();
                        int bitmapHeight = sideImage.getBitmapHeight();
                        float scaleX = ((float) getContainerViewWidth()) / ((float) bitmapWidth);
                        scaleY = ((float) getContainerViewHeight()) / ((float) bitmapHeight);
                        float scale = scaleX > scaleY ? scaleY : scaleX;
                        width = (int) (((float) bitmapWidth) * scale);
                        height = (int) (((float) bitmapHeight) * scale);
                        sideImage.setAlpha(alpha);
                        sideImage.setImageCoords((-width) / 2, (-height) / 2, width, height);
                        sideImage.draw(canvas2);
                        canvas.restore();
                    }
                    canvas.save();
                    canvas2.translate(tx, currentTranslationY / currentScale);
                    canvas2.translate(((((float) canvas.getWidth()) * (r0.scale + 1.0f)) + ((float) AndroidUtilities.dp(30.0f))) / 2.0f, (-currentTranslationY) / currentScale);
                    r0.radialProgressViews[1].setScale(1.0f - scaleDiff);
                    r0.radialProgressViews[1].setAlpha(alpha);
                    r0.radialProgressViews[1].onDraw(canvas2);
                    canvas.restore();
                }
                aty = currentTranslationX;
                alpha = 0.0f;
                float alpha3 = 1.0f;
                if (!r0.zoomAnimation && aty > r0.maxX) {
                    alpha3 = Math.min(1.0f, (aty - r0.maxX) / ((float) canvas.getWidth()));
                    alpha = alpha3 * 0.3f;
                    alpha3 = 1.0f - alpha3;
                    aty = r0.maxX;
                }
                boolean drawTextureView = r0.aspectRatioFrameLayout != null && r0.aspectRatioFrameLayout.getVisibility() == 0;
                if (r0.centerImage.hasBitmapImage()) {
                    long newUpdateTime;
                    float f2;
                    int i;
                    canvas.save();
                    canvas2.translate((float) (getContainerViewWidth() / 2), (float) (getContainerViewHeight() / 2));
                    canvas2.translate(aty, currentTranslationY);
                    canvas2.scale(currentScale - alpha, currentScale - alpha);
                    bitmapWidth = r0.centerImage.getBitmapWidth();
                    width = r0.centerImage.getBitmapHeight();
                    if (drawTextureView && r0.textureUploaded && Math.abs((((float) bitmapWidth) / ((float) width)) - (((float) r0.videoTextureView.getMeasuredWidth()) / ((float) r0.videoTextureView.getMeasuredHeight()))) > 0.01f) {
                        bitmapWidth = r0.videoTextureView.getMeasuredWidth();
                        width = r0.videoTextureView.getMeasuredHeight();
                    }
                    tx = ((float) getContainerViewWidth()) / ((float) bitmapWidth);
                    ty = ((float) getContainerViewHeight()) / ((float) width);
                    scaleY = tx > ty ? ty : tx;
                    width2 = (int) (((float) bitmapWidth) * scaleY);
                    bitmapWidth = (int) (((float) width) * scaleY);
                    if (drawTextureView) {
                        if (r0.textureUploaded != 0 && r0.videoCrossfadeStarted) {
                            if (r0.videoCrossfadeAlpha == 1.0f) {
                                float f3 = tx;
                                float f4 = ty;
                                if (drawTextureView) {
                                    if (!r0.videoCrossfadeStarted && r0.textureUploaded) {
                                        r0.videoCrossfadeStarted = true;
                                        r0.videoCrossfadeAlpha = 0.0f;
                                        r0.videoCrossfadeAlphaLastTime = System.currentTimeMillis();
                                    }
                                    canvas2.translate((float) ((-width2) / 2), (float) ((-bitmapWidth) / 2));
                                    r0.videoTextureView.setAlpha(r0.videoCrossfadeAlpha * alpha3);
                                    r0.aspectRatioFrameLayout.draw(canvas2);
                                    if (r0.videoCrossfadeStarted && r0.videoCrossfadeAlpha < 1.0f) {
                                        newUpdateTime = System.currentTimeMillis();
                                        f = alpha3;
                                        imageReceiver = sideImage;
                                        alpha3 = newUpdateTime - r0.videoCrossfadeAlphaLastTime;
                                        r0.videoCrossfadeAlphaLastTime = newUpdateTime;
                                        r0.videoCrossfadeAlpha += ((float) alpha3) / 300.0f;
                                        r0.photoContainerView.invalidate();
                                        if (r0.videoCrossfadeAlpha > 1.0f) {
                                            r0.videoCrossfadeAlpha = 1.0f;
                                        }
                                        canvas.restore();
                                    }
                                }
                                f = alpha3;
                                imageReceiver = sideImage;
                                f2 = scaleY;
                                i = width2;
                                canvas.restore();
                            }
                        }
                    }
                    r0.centerImage.setAlpha(alpha3);
                    r0.centerImage.setImageCoords((-width2) / 2, (-bitmapWidth) / 2, width2, bitmapWidth);
                    r0.centerImage.draw(canvas2);
                    if (drawTextureView) {
                        r0.videoCrossfadeStarted = true;
                        r0.videoCrossfadeAlpha = 0.0f;
                        r0.videoCrossfadeAlphaLastTime = System.currentTimeMillis();
                        canvas2.translate((float) ((-width2) / 2), (float) ((-bitmapWidth) / 2));
                        r0.videoTextureView.setAlpha(r0.videoCrossfadeAlpha * alpha3);
                        r0.aspectRatioFrameLayout.draw(canvas2);
                        newUpdateTime = System.currentTimeMillis();
                        f = alpha3;
                        imageReceiver = sideImage;
                        alpha3 = newUpdateTime - r0.videoCrossfadeAlphaLastTime;
                        r0.videoCrossfadeAlphaLastTime = newUpdateTime;
                        r0.videoCrossfadeAlpha += ((float) alpha3) / 300.0f;
                        r0.photoContainerView.invalidate();
                        if (r0.videoCrossfadeAlpha > 1.0f) {
                            r0.videoCrossfadeAlpha = 1.0f;
                        }
                        canvas.restore();
                    }
                    f = alpha3;
                    imageReceiver = sideImage;
                    f2 = scaleY;
                    i = width2;
                    canvas.restore();
                } else {
                    f = alpha3;
                    imageReceiver = sideImage;
                }
                if (drawTextureView || r0.bottomLayout.getVisibility() == 0) {
                } else {
                    canvas.save();
                    canvas2.translate(aty, currentTranslationY / currentScale);
                    r0.radialProgressViews[0].setScale(1.0f - alpha);
                    r0.radialProgressViews[0].setAlpha(f);
                    r0.radialProgressViews[0].onDraw(canvas2);
                    canvas.restore();
                }
                sideImage = imageReceiver;
                float f5;
                if (sideImage == r0.leftImage) {
                    if (sideImage.hasBitmapImage()) {
                        canvas.save();
                        canvas2.translate((float) (getContainerViewWidth() / 2), (float) (getContainerViewHeight() / 2));
                        canvas2.translate(((-((((float) canvas.getWidth()) * (r0.scale + 1.0f)) + ((float) AndroidUtilities.dp(30.0f)))) / 2.0f) + currentTranslationX, 0.0f);
                        height = sideImage.getBitmapWidth();
                        width = sideImage.getBitmapHeight();
                        tx = ((float) getContainerViewWidth()) / ((float) height);
                        ty = ((float) getContainerViewHeight()) / ((float) width);
                        scaleY = tx > ty ? ty : tx;
                        width2 = (int) (((float) height) * scaleY);
                        aty = (int) (((float) width) * scaleY);
                        sideImage.setAlpha(1.0f);
                        sideImage.setImageCoords((-width2) / 2, (-aty) / 2, width2, aty);
                        sideImage.draw(canvas2);
                        canvas.restore();
                    } else {
                        f5 = alpha;
                    }
                    canvas.save();
                    canvas2.translate(currentTranslationX, currentTranslationY / currentScale);
                    canvas2.translate((-((((float) canvas.getWidth()) * (r0.scale + 1.0f)) + ((float) AndroidUtilities.dp(30.0f)))) / 2.0f, (-currentTranslationY) / currentScale);
                    r0.radialProgressViews[2].setScale(1.0f);
                    r0.radialProgressViews[2].setAlpha(1.0f);
                    r0.radialProgressViews[2].onDraw(canvas2);
                    canvas.restore();
                } else {
                    f5 = alpha;
                }
            }
        }
    }

    private void onActionClick(boolean download) {
        TLObject media = getMedia(this.currentIndex);
        if (media instanceof Document) {
            if (this.currentFileNames[0] != null) {
                Document document = (Document) media;
                File file = null;
                if (this.currentMedia != null) {
                    file = getMediaFile(this.currentIndex);
                    if (!(file == null || file.exists())) {
                        file = null;
                    }
                }
                if (file != null) {
                    preparePlayer(file, true);
                } else if (download) {
                    if (FileLoader.getInstance(this.currentAccount).isLoadingFile(this.currentFileNames[0])) {
                        FileLoader.getInstance(this.currentAccount).cancelLoadFile(document);
                    } else {
                        FileLoader.getInstance(this.currentAccount).loadFile(document, true, 1);
                    }
                }
            }
        }
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
            this.photoContainerView.postInvalidate();
        }
        return false;
    }

    public boolean onSingleTapConfirmed(MotionEvent e) {
        if (this.discardTap) {
            return false;
        }
        boolean drawTextureView = this.aspectRatioFrameLayout != null && this.aspectRatioFrameLayout.getVisibility() == 0;
        if (!(this.radialProgressViews[0] == null || this.photoContainerView == null || drawTextureView)) {
            int state = this.radialProgressViews[0].backgroundState;
            if (state > 0 && state <= 3) {
                float x = e.getX();
                float y = e.getY();
                if (x >= ((float) (getContainerViewWidth() - AndroidUtilities.dp(100.0f))) / 2.0f && x <= ((float) (getContainerViewWidth() + AndroidUtilities.dp(100.0f))) / 2.0f && y >= ((float) (getContainerViewHeight() - AndroidUtilities.dp(100.0f))) / 2.0f && y <= ((float) (getContainerViewHeight() + AndroidUtilities.dp(100.0f))) / 2.0f) {
                    onActionClick(true);
                    checkProgress(0, true);
                    return true;
                }
            }
        }
        toggleActionBar(this.isActionBarVisible ^ true, true);
        return true;
    }

    public boolean onDoubleTap(MotionEvent e) {
        if (this.canZoom) {
            if (this.scale == 1.0f) {
                if (this.translationY == 0.0f) {
                    if (this.translationX != 0.0f) {
                    }
                }
            }
            if (this.animationStartTime == 0) {
                if (this.photoAnimationInProgress == 0) {
                    if (this.scale == 1.0f) {
                        float atx = (e.getX() - ((float) (getContainerViewWidth() / 2))) - (((e.getX() - ((float) (getContainerViewWidth() / 2))) - this.translationX) * (3.0f / this.scale));
                        float aty = (e.getY() - ((float) (getContainerViewHeight() / 2))) - (((e.getY() - ((float) (getContainerViewHeight() / 2))) - this.translationY) * (3.0f / this.scale));
                        updateMinMax(3.0f);
                        if (atx < this.minX) {
                            atx = this.minX;
                        } else if (atx > this.maxX) {
                            atx = this.maxX;
                        }
                        if (aty < this.minY) {
                            aty = this.minY;
                        } else if (aty > this.maxY) {
                            aty = this.maxY;
                        }
                        animateTo(3.0f, atx, aty, true);
                    } else {
                        animateTo(1.0f, 0.0f, 0.0f, true);
                    }
                    this.doubleTap = true;
                    return true;
                }
            }
            return false;
        }
        return false;
    }

    public boolean onDoubleTapEvent(MotionEvent e) {
        return false;
    }

    private ImageReceiver getImageReceiverFromListView(ViewGroup listView, PageBlock pageBlock, int[] coords) {
        int count = listView.getChildCount();
        for (int a = 0; a < count; a++) {
            View view = listView.getChildAt(a);
            if (view instanceof BlockPhotoCell) {
                BlockPhotoCell cell = (BlockPhotoCell) view;
                if (cell.currentBlock == pageBlock) {
                    view.getLocationInWindow(coords);
                    return cell.imageView;
                }
            } else if (view instanceof BlockVideoCell) {
                BlockVideoCell cell2 = (BlockVideoCell) view;
                if (cell2.currentBlock == pageBlock) {
                    view.getLocationInWindow(coords);
                    return cell2.imageView;
                }
            } else if (view instanceof BlockCollageCell) {
                imageReceiver = getImageReceiverFromListView(((BlockCollageCell) view).innerListView, pageBlock, coords);
                if (imageReceiver != null) {
                    return imageReceiver;
                }
            } else if (view instanceof BlockSlideshowCell) {
                imageReceiver = getImageReceiverFromListView(((BlockSlideshowCell) view).innerListView, pageBlock, coords);
                if (imageReceiver != null) {
                    return imageReceiver;
                }
            } else {
                continue;
            }
        }
        return null;
    }

    private PlaceProviderObject getPlaceForPhoto(PageBlock pageBlock) {
        ImageReceiver imageReceiver = getImageReceiverFromListView(this.listView, pageBlock, this.coords);
        if (imageReceiver == null) {
            return null;
        }
        PlaceProviderObject object = new PlaceProviderObject();
        object.viewX = this.coords[0];
        object.viewY = this.coords[1];
        object.parentView = this.listView;
        object.imageReceiver = imageReceiver;
        object.thumb = imageReceiver.getBitmapSafe();
        object.radius = imageReceiver.getRoundRadius();
        object.clipTopAddition = this.currentHeaderHeight;
        return object;
    }
}
