package org.telegram.ui;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Paint.Cap;
import android.graphics.Paint.Join;
import android.graphics.Paint.Style;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffColorFilter;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.SurfaceTexture;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaCodecInfo;
import android.net.Uri;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.SystemClock;
import android.provider.Settings;
import android.support.annotation.Keep;
import android.support.v4.content.FileProvider;
import android.text.Layout.Alignment;
import android.text.Selection;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.TextUtils.TruncateAt;
import android.text.method.LinkMovementMethod;
import android.util.SparseArray;
import android.view.ActionMode;
import android.view.ActionMode.Callback;
import android.view.ContextThemeWrapper;
import android.view.GestureDetector;
import android.view.GestureDetector.OnDoubleTapListener;
import android.view.GestureDetector.OnGestureListener;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.TextureView;
import android.view.TextureView.SurfaceTextureListener;
import android.view.VelocityTracker;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.View.OnApplyWindowInsetsListener;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewTreeObserver.OnPreDrawListener;
import android.view.WindowInsets;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.Scroller;
import android.widget.TextView;
import android.widget.Toast;
import com.coremedia.iso.IsoFile;
import com.coremedia.iso.boxes.Box;
import com.coremedia.iso.boxes.Container;
import com.coremedia.iso.boxes.MediaBox;
import com.coremedia.iso.boxes.MediaHeaderBox;
import com.coremedia.iso.boxes.TimeToSampleBox;
import com.coremedia.iso.boxes.TimeToSampleBox.Entry;
import com.coremedia.iso.boxes.TrackBox;
import com.coremedia.iso.boxes.TrackHeaderBox;
import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout;
import com.google.android.exoplayer2.util.MimeTypes;
import com.googlecode.mp4parser.util.Matrix;
import com.googlecode.mp4parser.util.Path;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.lang.reflect.Array;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.ApplicationLoader;
import org.telegram.messenger.Bitmaps;
import org.telegram.messenger.BringAppForegroundService;
import org.telegram.messenger.BuildVars;
import org.telegram.messenger.ChatObject;
import org.telegram.messenger.ContactsController;
import org.telegram.messenger.DataQuery;
import org.telegram.messenger.DispatchQueue;
import org.telegram.messenger.Emoji;
import org.telegram.messenger.EmojiSuggestion;
import org.telegram.messenger.FileLoader;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.ImageLoader;
import org.telegram.messenger.ImageReceiver;
import org.telegram.messenger.ImageReceiver.BitmapHolder;
import org.telegram.messenger.ImageReceiver.ImageReceiverDelegate;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MediaController;
import org.telegram.messenger.MediaController.PhotoEntry;
import org.telegram.messenger.MediaController.SavedFilterState;
import org.telegram.messenger.MediaController.SearchImage;
import org.telegram.messenger.MessageObject;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.MessagesStorage;
import org.telegram.messenger.NotificationCenter;
import org.telegram.messenger.NotificationCenter.NotificationCenterDelegate;
import org.telegram.messenger.SecureDocument;
import org.telegram.messenger.SendMessagesHelper;
import org.telegram.messenger.SharedConfig;
import org.telegram.messenger.UserConfig;
import org.telegram.messenger.UserObject;
import org.telegram.messenger.Utilities;
import org.telegram.messenger.VideoEditedInfo;
import org.telegram.messenger.WebFile;
import org.telegram.messenger.beta.R;
import org.telegram.messenger.support.widget.DefaultItemAnimator;
import org.telegram.messenger.support.widget.LinearLayoutManager;
import org.telegram.messenger.support.widget.LinearSmoothScrollerEnd;
import org.telegram.messenger.support.widget.RecyclerView;
import org.telegram.messenger.support.widget.RecyclerView.Adapter;
import org.telegram.messenger.support.widget.RecyclerView.ItemDecoration;
import org.telegram.messenger.support.widget.RecyclerView.State;
import org.telegram.messenger.support.widget.RecyclerView.ViewHolder;
import org.telegram.messenger.support.widget.helper.ItemTouchHelper;
import org.telegram.tgnet.ConnectionsManager;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC.BotInlineResult;
import org.telegram.tgnet.TLRPC.Chat;
import org.telegram.tgnet.TLRPC.Document;
import org.telegram.tgnet.TLRPC.EncryptedChat;
import org.telegram.tgnet.TLRPC.FileLocation;
import org.telegram.tgnet.TLRPC.InputDocument;
import org.telegram.tgnet.TLRPC.MessageEntity;
import org.telegram.tgnet.TLRPC.Photo;
import org.telegram.tgnet.TLRPC.PhotoSize;
import org.telegram.tgnet.TLRPC.TL_inputPhoto;
import org.telegram.tgnet.TLRPC.TL_message;
import org.telegram.tgnet.TLRPC.TL_messageActionEmpty;
import org.telegram.tgnet.TLRPC.TL_messageActionUserUpdatedPhoto;
import org.telegram.tgnet.TLRPC.TL_messageMediaEmpty;
import org.telegram.tgnet.TLRPC.TL_messageMediaInvoice;
import org.telegram.tgnet.TLRPC.TL_messageMediaPhoto;
import org.telegram.tgnet.TLRPC.TL_messageMediaWebPage;
import org.telegram.tgnet.TLRPC.TL_messageService;
import org.telegram.tgnet.TLRPC.TL_photoEmpty;
import org.telegram.tgnet.TLRPC.TL_webDocument;
import org.telegram.tgnet.TLRPC.User;
import org.telegram.ui.ActionBar.ActionBar;
import org.telegram.ui.ActionBar.ActionBar.ActionBarMenuOnItemClick;
import org.telegram.ui.ActionBar.ActionBarMenu;
import org.telegram.ui.ActionBar.ActionBarMenuItem;
import org.telegram.ui.ActionBar.AlertDialog;
import org.telegram.ui.ActionBar.AlertDialog.Builder;
import org.telegram.ui.ActionBar.BottomSheet;
import org.telegram.ui.ActionBar.SimpleTextView;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Adapters.MentionsAdapter;
import org.telegram.ui.Adapters.MentionsAdapter.MentionsAdapterDelegate;
import org.telegram.ui.Cells.CheckBoxCell;
import org.telegram.ui.Cells.PhotoPickerPhotoCell;
import org.telegram.ui.Components.AnimatedFileDrawable;
import org.telegram.ui.Components.BackupImageView;
import org.telegram.ui.Components.ChatAttachAlert;
import org.telegram.ui.Components.CheckBox;
import org.telegram.ui.Components.ClippingImageView;
import org.telegram.ui.Components.CubicBezierInterpolator;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.ui.Components.NumberPicker;
import org.telegram.ui.Components.NumberPicker.Formatter;
import org.telegram.ui.Components.PhotoCropView;
import org.telegram.ui.Components.PhotoCropView.PhotoCropViewDelegate;
import org.telegram.ui.Components.PhotoFilterView;
import org.telegram.ui.Components.PhotoPaintView;
import org.telegram.ui.Components.PhotoViewerCaptionEnterView;
import org.telegram.ui.Components.PhotoViewerCaptionEnterView.PhotoViewerCaptionEnterViewDelegate;
import org.telegram.ui.Components.PickerBottomLayoutViewer;
import org.telegram.ui.Components.PipVideoView;
import org.telegram.ui.Components.RadialProgressView;
import org.telegram.ui.Components.RecyclerListView;
import org.telegram.ui.Components.RecyclerListView.Holder;
import org.telegram.ui.Components.RecyclerListView.OnItemClickListener;
import org.telegram.ui.Components.RecyclerListView.OnItemLongClickListener;
import org.telegram.ui.Components.RecyclerListView.SelectionAdapter;
import org.telegram.ui.Components.SeekBar;
import org.telegram.ui.Components.SeekBar.SeekBarDelegate;
import org.telegram.ui.Components.SizeNotifierFrameLayoutPhoto;
import org.telegram.ui.Components.StickersAlert;
import org.telegram.ui.Components.URLSpanUserMentionPhotoViewer;
import org.telegram.ui.Components.VideoPlayer;
import org.telegram.ui.Components.VideoPlayer.VideoPlayerDelegate;
import org.telegram.ui.Components.VideoTimelinePlayView;
import org.telegram.ui.Components.VideoTimelinePlayView.VideoTimelineViewDelegate;
import org.telegram.ui.DialogsActivity.DialogsActivityDelegate;

public class PhotoViewer implements OnDoubleTapListener, OnGestureListener, NotificationCenterDelegate {
    @SuppressLint({"StaticFieldLeak"})
    private static volatile PhotoViewer Instance = null;
    private static volatile PhotoViewer PipInstance = null;
    private static DecelerateInterpolator decelerateInterpolator = null;
    private static final int gallery_menu_cancel_loading = 7;
    private static final int gallery_menu_delete = 6;
    private static final int gallery_menu_masks = 13;
    private static final int gallery_menu_openin = 11;
    private static final int gallery_menu_pip = 5;
    private static final int gallery_menu_save = 1;
    private static final int gallery_menu_send = 3;
    private static final int gallery_menu_share = 10;
    private static final int gallery_menu_showall = 2;
    private static final int gallery_menu_showinchat = 4;
    private static Drawable[] progressDrawables;
    private static Paint progressPaint;
    private ActionBar actionBar;
    private AnimatorSet actionBarAnimator;
    private Context actvityContext;
    private boolean allowMentions;
    private boolean allowShare;
    private float animateToScale;
    private float animateToX;
    private float animateToY;
    private ClippingImageView animatingImageView;
    private Runnable animationEndRunnable;
    private int animationInProgress;
    private long animationStartTime;
    private float animationValue;
    private float[][] animationValues = ((float[][]) Array.newInstance(Float.TYPE, new int[]{2, 8}));
    private boolean applying;
    private AspectRatioFrameLayout aspectRatioFrameLayout;
    private boolean attachedToWindow;
    private long audioFramesSize;
    private ArrayList<Photo> avatarsArr = new ArrayList();
    private int avatarsDialogId;
    private BackgroundDrawable backgroundDrawable = new BackgroundDrawable(Theme.ACTION_BAR_VIDEO_EDIT_COLOR);
    private int bitrate;
    private Paint blackPaint = new Paint();
    private FrameLayout bottomLayout;
    private boolean bottomTouchEnabled = true;
    private ImageView cameraItem;
    private boolean canDragDown = true;
    private boolean canZoom = true;
    private PhotoViewerCaptionEnterView captionEditText;
    private TextView captionTextView;
    private ImageReceiver centerImage = new ImageReceiver();
    private AnimatorSet changeModeAnimation;
    private TextureView changedTextureView;
    private boolean changingPage;
    private boolean changingTextureView;
    private CheckBox checkImageView;
    private int classGuid;
    private ImageView compressItem;
    private AnimatorSet compressItemAnimation;
    private int compressionsCount = -1;
    private FrameLayoutDrawer containerView;
    private ImageView cropItem;
    private int currentAccount;
    private AnimatedFileDrawable currentAnimation;
    private Bitmap currentBitmap;
    private BotInlineResult currentBotInlineResult;
    private AnimatorSet currentCaptionAnimation;
    private long currentDialogId;
    private int currentEditMode;
    private FileLocation currentFileLocation;
    private String[] currentFileNames = new String[3];
    private int currentIndex;
    private AnimatorSet currentListViewAnimation;
    private Runnable currentLoadingVideoRunnable;
    private MessageObject currentMessageObject;
    private String currentPathObject;
    private PlaceProviderObject currentPlaceObject;
    private Uri currentPlayingVideoFile;
    private SecureDocument currentSecureDocument;
    private String currentSubtitle;
    private BitmapHolder currentThumb;
    private FileLocation currentUserAvatarLocation = null;
    private boolean currentVideoFinishedLoading;
    private int dateOverride;
    private TextView dateTextView;
    private boolean disableShowCheck;
    private boolean discardTap;
    private boolean doneButtonPressed;
    private boolean dontResetZoomOnFirstLayout;
    private boolean doubleTap;
    private float dragY;
    private boolean draggingDown;
    private PickerBottomLayoutViewer editorDoneLayout;
    private boolean[] endReached = new boolean[]{false, true};
    private long endTime;
    private long estimatedDuration;
    private int estimatedSize;
    private boolean firstAnimationDelay;
    boolean fromCamera;
    private GestureDetector gestureDetector;
    private GroupedPhotosListView groupedPhotosListView;
    private PlaceProviderObject hideAfterAnimation;
    private AnimatorSet hintAnimation;
    private Runnable hintHideRunnable;
    private TextView hintTextView;
    private boolean ignoreDidSetImage;
    private AnimatorSet imageMoveAnimation;
    private ArrayList<MessageObject> imagesArr = new ArrayList();
    private ArrayList<Object> imagesArrLocals = new ArrayList();
    private ArrayList<FileLocation> imagesArrLocations = new ArrayList();
    private ArrayList<Integer> imagesArrLocationsSizes = new ArrayList();
    private ArrayList<MessageObject> imagesArrTemp = new ArrayList();
    private SparseArray<MessageObject>[] imagesByIds = new SparseArray[]{new SparseArray(), new SparseArray()};
    private SparseArray<MessageObject>[] imagesByIdsTemp = new SparseArray[]{new SparseArray(), new SparseArray()};
    private boolean inPreview;
    private DecelerateInterpolator interpolator = new DecelerateInterpolator(1.5f);
    private boolean invalidCoords;
    private boolean isActionBarVisible = true;
    private boolean isCurrentVideo;
    private boolean isEvent;
    private boolean isFirstLoading;
    private boolean isInline;
    private boolean isPhotosListViewVisible;
    private boolean isPlaying;
    private boolean isStreaming;
    private boolean isVisible;
    private boolean keepScreenOnFlagSet;
    private long lastBufferedPositionCheck;
    private Object lastInsets;
    private String lastTitle;
    private ImageReceiver leftImage = new ImageReceiver();
    private boolean loadInitialVideo;
    private boolean loadingMoreImages;
    private ActionBarMenuItem masksItem;
    private int maxSelectedPhotos = -1;
    private float maxX;
    private float maxY;
    private LinearLayoutManager mentionLayoutManager;
    private AnimatorSet mentionListAnimation;
    private RecyclerListView mentionListView;
    private MentionsAdapter mentionsAdapter;
    private ActionBarMenuItem menuItem;
    private long mergeDialogId;
    private float minX;
    private float minY;
    private AnimatorSet miniProgressAnimator;
    private Runnable miniProgressShowRunnable = new Runnable() {
        public void run() {
            PhotoViewer.this.toggleMiniProgressInternal(true);
        }
    };
    private RadialProgressView miniProgressView;
    private float moveStartX;
    private float moveStartY;
    private boolean moving;
    private ImageView muteItem;
    private boolean muteVideo;
    private String nameOverride;
    private TextView nameTextView;
    private boolean needCaptionLayout;
    private boolean needSearchImageInArr;
    private boolean opennedFromMedia;
    private int originalBitrate;
    private int originalHeight;
    private long originalSize;
    private int originalWidth;
    private ImageView paintItem;
    private Activity parentActivity;
    private ChatAttachAlert parentAlert;
    private ChatActivity parentChatActivity;
    private PhotoCropView photoCropView;
    private PhotoFilterView photoFilterView;
    private PhotoPaintView photoPaintView;
    private PhotoProgressView[] photoProgressViews = new PhotoProgressView[3];
    private CounterView photosCounterView;
    private FrameLayout pickerView;
    private ImageView pickerViewSendButton;
    private float pinchCenterX;
    private float pinchCenterY;
    private float pinchStartDistance;
    private float pinchStartScale = 1.0f;
    private float pinchStartX;
    private float pinchStartY;
    private boolean pipAnimationInProgress;
    private boolean pipAvailable;
    private ActionBarMenuItem pipItem;
    private int[] pipPosition = new int[2];
    private PipVideoView pipVideoView;
    private PhotoViewerProvider placeProvider;
    private int previewViewEnd;
    private int previousCompression;
    private RadialProgressView progressView;
    private QualityChooseView qualityChooseView;
    private AnimatorSet qualityChooseViewAnimation;
    private PickerBottomLayoutViewer qualityPicker;
    private boolean requestingPreview;
    private TextView resetButton;
    private int resultHeight;
    private int resultWidth;
    private ImageReceiver rightImage = new ImageReceiver();
    private int rotationValue;
    private float scale = 1.0f;
    private Scroller scroller;
    private ArrayList<SecureDocument> secureDocuments = new ArrayList();
    private float seekToProgressPending;
    private int selectedCompression;
    private ListAdapter selectedPhotosAdapter;
    private RecyclerListView selectedPhotosListView;
    private ActionBarMenuItem sendItem;
    private int sendPhotoType;
    private ImageView shareButton;
    private PlaceProviderObject showAfterAnimation;
    private int slideshowMessageId;
    private long startTime;
    private long startedPlayTime;
    private boolean streamingAlertShown;
    private SurfaceTextureListener surfaceTextureListener = new SurfaceTextureListener() {
        public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
        }

        public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {
        }

        public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
            if (PhotoViewer.this.videoTextureView == null || !PhotoViewer.this.changingTextureView) {
                return true;
            }
            if (PhotoViewer.this.switchingInlineMode) {
                PhotoViewer.this.waitingForFirstTextureUpload = 2;
            }
            PhotoViewer.this.videoTextureView.setSurfaceTexture(surface);
            PhotoViewer.this.videoTextureView.setVisibility(0);
            PhotoViewer.this.changingTextureView = false;
            PhotoViewer.this.containerView.invalidate();
            return false;
        }

        public void onSurfaceTextureUpdated(SurfaceTexture surface) {
            if (PhotoViewer.this.waitingForFirstTextureUpload == 1) {
                PhotoViewer.this.changedTextureView.getViewTreeObserver().addOnPreDrawListener(new OnPreDrawListener() {
                    public boolean onPreDraw() {
                        PhotoViewer.this.changedTextureView.getViewTreeObserver().removeOnPreDrawListener(this);
                        if (PhotoViewer.this.textureImageView != null) {
                            PhotoViewer.this.textureImageView.setVisibility(4);
                            PhotoViewer.this.textureImageView.setImageDrawable(null);
                            if (PhotoViewer.this.currentBitmap != null) {
                                PhotoViewer.this.currentBitmap.recycle();
                                PhotoViewer.this.currentBitmap = null;
                            }
                        }
                        AndroidUtilities.runOnUIThread(new Runnable() {
                            public void run() {
                                if (PhotoViewer.this.isInline) {
                                    PhotoViewer.this.dismissInternal();
                                }
                            }
                        });
                        PhotoViewer.this.waitingForFirstTextureUpload = 0;
                        return true;
                    }
                });
                PhotoViewer.this.changedTextureView.invalidate();
            }
        }
    };
    private TextView switchCaptionTextView;
    private int switchImageAfterAnimation;
    private Runnable switchToInlineRunnable = new Runnable() {
        public void run() {
            PhotoViewer.this.switchingInlineMode = false;
            if (PhotoViewer.this.currentBitmap != null) {
                PhotoViewer.this.currentBitmap.recycle();
                PhotoViewer.this.currentBitmap = null;
            }
            PhotoViewer.this.changingTextureView = true;
            if (PhotoViewer.this.textureImageView != null) {
                try {
                    PhotoViewer.this.currentBitmap = Bitmaps.createBitmap(PhotoViewer.this.videoTextureView.getWidth(), PhotoViewer.this.videoTextureView.getHeight(), Config.ARGB_8888);
                    PhotoViewer.this.videoTextureView.getBitmap(PhotoViewer.this.currentBitmap);
                } catch (Throwable e) {
                    if (PhotoViewer.this.currentBitmap != null) {
                        PhotoViewer.this.currentBitmap.recycle();
                        PhotoViewer.this.currentBitmap = null;
                    }
                    FileLog.e(e);
                }
                if (PhotoViewer.this.currentBitmap != null) {
                    PhotoViewer.this.textureImageView.setVisibility(0);
                    PhotoViewer.this.textureImageView.setImageBitmap(PhotoViewer.this.currentBitmap);
                } else {
                    PhotoViewer.this.textureImageView.setImageDrawable(null);
                }
            }
            PhotoViewer.this.isInline = true;
            PhotoViewer.this.pipVideoView = new PipVideoView();
            PhotoViewer.this.changedTextureView = PhotoViewer.this.pipVideoView.show(PhotoViewer.this.parentActivity, PhotoViewer.this, PhotoViewer.this.aspectRatioFrameLayout.getAspectRatio(), PhotoViewer.this.aspectRatioFrameLayout.getVideoRotation());
            PhotoViewer.this.changedTextureView.setVisibility(4);
            PhotoViewer.this.aspectRatioFrameLayout.removeView(PhotoViewer.this.videoTextureView);
        }
    };
    private boolean switchingInlineMode;
    private int switchingToIndex;
    private ImageView textureImageView;
    private boolean textureUploaded;
    private ImageView timeItem;
    private int totalImagesCount;
    private int totalImagesCountMerge;
    private long transitionAnimationStartTime;
    private float translationX;
    private float translationY;
    private boolean tryStartRequestPreviewOnFinish;
    private ImageView tuneItem;
    private Runnable updateProgressRunnable = new Runnable() {
        public void run() {
            if (PhotoViewer.this.videoPlayer != null) {
                float progress;
                if (PhotoViewer.this.isCurrentVideo) {
                    if (!PhotoViewer.this.videoTimelineView.isDragging()) {
                        progress = ((float) PhotoViewer.this.videoPlayer.getCurrentPosition()) / ((float) PhotoViewer.this.videoPlayer.getDuration());
                        if (PhotoViewer.this.inPreview || PhotoViewer.this.videoTimelineView.getVisibility() != 0) {
                            PhotoViewer.this.videoTimelineView.setProgress(progress);
                        } else if (progress >= PhotoViewer.this.videoTimelineView.getRightProgress()) {
                            PhotoViewer.this.videoPlayer.pause();
                            PhotoViewer.this.videoTimelineView.setProgress(0.0f);
                            PhotoViewer.this.videoPlayer.seekTo((long) ((int) (PhotoViewer.this.videoTimelineView.getLeftProgress() * ((float) PhotoViewer.this.videoPlayer.getDuration()))));
                            PhotoViewer.this.containerView.invalidate();
                        } else {
                            progress -= PhotoViewer.this.videoTimelineView.getLeftProgress();
                            if (progress < 0.0f) {
                                progress = 0.0f;
                            }
                            progress /= PhotoViewer.this.videoTimelineView.getRightProgress() - PhotoViewer.this.videoTimelineView.getLeftProgress();
                            if (progress > 1.0f) {
                                progress = 1.0f;
                            }
                            PhotoViewer.this.videoTimelineView.setProgress(progress);
                        }
                        PhotoViewer.this.updateVideoPlayerTime();
                    }
                } else if (!PhotoViewer.this.videoPlayerSeekbar.isDragging()) {
                    float bufferedProgress;
                    progress = ((float) PhotoViewer.this.videoPlayer.getCurrentPosition()) / ((float) PhotoViewer.this.videoPlayer.getDuration());
                    if (PhotoViewer.this.currentVideoFinishedLoading) {
                        bufferedProgress = 1.0f;
                    } else {
                        long newTime = SystemClock.elapsedRealtime();
                        if (Math.abs(newTime - PhotoViewer.this.lastBufferedPositionCheck) >= 500) {
                            if (PhotoViewer.this.isStreaming) {
                                float access$1100;
                                FileLoader instance = FileLoader.getInstance(PhotoViewer.this.currentAccount);
                                if (PhotoViewer.this.seekToProgressPending != 0.0f) {
                                    access$1100 = PhotoViewer.this.seekToProgressPending;
                                } else {
                                    access$1100 = progress;
                                }
                                bufferedProgress = instance.getBufferedProgressFromPosition(access$1100, PhotoViewer.this.currentFileNames[0]);
                            } else {
                                bufferedProgress = 1.0f;
                            }
                            PhotoViewer.this.lastBufferedPositionCheck = newTime;
                        } else {
                            bufferedProgress = -1.0f;
                        }
                    }
                    if (PhotoViewer.this.inPreview || PhotoViewer.this.videoTimelineView.getVisibility() != 0) {
                        if (PhotoViewer.this.seekToProgressPending == 0.0f) {
                            PhotoViewer.this.videoPlayerSeekbar.setProgress(progress);
                        }
                        if (bufferedProgress != -1.0f) {
                            PhotoViewer.this.videoPlayerSeekbar.setBufferedProgress(bufferedProgress);
                            if (PhotoViewer.this.pipVideoView != null) {
                                PhotoViewer.this.pipVideoView.setBufferedProgress(bufferedProgress);
                            }
                        }
                    } else if (progress >= PhotoViewer.this.videoTimelineView.getRightProgress()) {
                        PhotoViewer.this.videoPlayer.pause();
                        PhotoViewer.this.videoPlayerSeekbar.setProgress(0.0f);
                        PhotoViewer.this.videoPlayer.seekTo((long) ((int) (PhotoViewer.this.videoTimelineView.getLeftProgress() * ((float) PhotoViewer.this.videoPlayer.getDuration()))));
                        PhotoViewer.this.containerView.invalidate();
                    } else {
                        progress -= PhotoViewer.this.videoTimelineView.getLeftProgress();
                        if (progress < 0.0f) {
                            progress = 0.0f;
                        }
                        progress /= PhotoViewer.this.videoTimelineView.getRightProgress() - PhotoViewer.this.videoTimelineView.getLeftProgress();
                        if (progress > 1.0f) {
                            progress = 1.0f;
                        }
                        PhotoViewer.this.videoPlayerSeekbar.setProgress(progress);
                    }
                    PhotoViewer.this.videoPlayerControlFrameLayout.invalidate();
                    PhotoViewer.this.updateVideoPlayerTime();
                }
            }
            if (PhotoViewer.this.isPlaying) {
                AndroidUtilities.runOnUIThread(PhotoViewer.this.updateProgressRunnable, 17);
            }
        }
    };
    private VelocityTracker velocityTracker;
    private ImageView videoBackwardButton;
    private float videoCrossfadeAlpha;
    private long videoCrossfadeAlphaLastTime;
    private boolean videoCrossfadeStarted;
    private float videoDuration;
    private ImageView videoForwardButton;
    private int videoFramerate;
    private long videoFramesSize;
    private boolean videoHasAudio;
    private ImageView videoPlayButton;
    private VideoPlayer videoPlayer;
    private FrameLayout videoPlayerControlFrameLayout;
    private SeekBar videoPlayerSeekbar;
    private SimpleTextView videoPlayerTime;
    private MessageObject videoPreviewMessageObject;
    private TextureView videoTextureView;
    private VideoTimelinePlayView videoTimelineView;
    private AlertDialog visibleDialog;
    private int waitingForDraw;
    private int waitingForFirstTextureUpload;
    private boolean wasLayout;
    private LayoutParams windowLayoutParams;
    private FrameLayout windowView;
    private boolean zoomAnimation;
    private boolean zooming;

    private class BackgroundDrawable extends ColorDrawable {
        private boolean allowDrawContent;
        private Runnable drawRunnable;

        public BackgroundDrawable(int color) {
            super(color);
        }

        @Keep
        public void setAlpha(int alpha) {
            if (PhotoViewer.this.parentActivity instanceof LaunchActivity) {
                boolean z = (PhotoViewer.this.isVisible && alpha == 255) ? false : true;
                this.allowDrawContent = z;
                ((LaunchActivity) PhotoViewer.this.parentActivity).drawerLayoutContainer.setAllowDrawContent(this.allowDrawContent);
                if (PhotoViewer.this.parentAlert != null) {
                    if (!this.allowDrawContent) {
                        AndroidUtilities.runOnUIThread(new Runnable() {
                            public void run() {
                                if (PhotoViewer.this.parentAlert != null) {
                                    PhotoViewer.this.parentAlert.setAllowDrawContent(BackgroundDrawable.this.allowDrawContent);
                                }
                            }
                        }, 50);
                    } else if (PhotoViewer.this.parentAlert != null) {
                        PhotoViewer.this.parentAlert.setAllowDrawContent(this.allowDrawContent);
                    }
                }
            }
            super.setAlpha(alpha);
        }

        public void draw(Canvas canvas) {
            super.draw(canvas);
            if (getAlpha() != 0 && this.drawRunnable != null) {
                AndroidUtilities.runOnUIThread(this.drawRunnable);
                this.drawRunnable = null;
            }
        }
    }

    private class CounterView extends View {
        private int currentCount = 0;
        private int height;
        private Paint paint;
        private RectF rect;
        private float rotation;
        private StaticLayout staticLayout;
        private TextPaint textPaint = new TextPaint(1);
        private int width;

        public CounterView(Context context) {
            super(context);
            this.textPaint.setTextSize((float) AndroidUtilities.dp(18.0f));
            this.textPaint.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
            this.textPaint.setColor(-1);
            this.paint = new Paint(1);
            this.paint.setColor(-1);
            this.paint.setStrokeWidth((float) AndroidUtilities.dp(2.0f));
            this.paint.setStyle(Style.STROKE);
            this.paint.setStrokeJoin(Join.ROUND);
            this.rect = new RectF();
            setCount(0);
        }

        @Keep
        public void setScaleX(float scaleX) {
            super.setScaleX(scaleX);
            invalidate();
        }

        @Keep
        public void setRotationX(float rotationX) {
            this.rotation = rotationX;
            invalidate();
        }

        public float getRotationX() {
            return this.rotation;
        }

        public void setCount(int value) {
            this.staticLayout = new StaticLayout(TtmlNode.ANONYMOUS_REGION_ID + Math.max(1, value), this.textPaint, AndroidUtilities.dp(100.0f), Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);
            this.width = (int) Math.ceil((double) this.staticLayout.getLineWidth(0));
            this.height = this.staticLayout.getLineBottom(0);
            AnimatorSet animatorSet = new AnimatorSet();
            if (value == 0) {
                Animator[] animatorArr = new Animator[4];
                animatorArr[0] = ObjectAnimator.ofFloat(this, "scaleX", new float[]{0.0f});
                animatorArr[1] = ObjectAnimator.ofFloat(this, "scaleY", new float[]{0.0f});
                animatorArr[2] = ObjectAnimator.ofInt(this.paint, "alpha", new int[]{0});
                animatorArr[3] = ObjectAnimator.ofInt(this.textPaint, "alpha", new int[]{0});
                animatorSet.playTogether(animatorArr);
                animatorSet.setInterpolator(new DecelerateInterpolator());
            } else if (this.currentCount == 0) {
                animatorSet.playTogether(new Animator[]{ObjectAnimator.ofFloat(this, "scaleX", new float[]{0.0f, 1.0f}), ObjectAnimator.ofFloat(this, "scaleY", new float[]{0.0f, 1.0f}), ObjectAnimator.ofInt(this.paint, "alpha", new int[]{0, 255}), ObjectAnimator.ofInt(this.textPaint, "alpha", new int[]{0, 255})});
                animatorSet.setInterpolator(new DecelerateInterpolator());
            } else if (value < this.currentCount) {
                animatorSet.playTogether(new Animator[]{ObjectAnimator.ofFloat(this, "scaleX", new float[]{1.1f, 1.0f}), ObjectAnimator.ofFloat(this, "scaleY", new float[]{1.1f, 1.0f})});
                animatorSet.setInterpolator(new OvershootInterpolator());
            } else {
                animatorSet.playTogether(new Animator[]{ObjectAnimator.ofFloat(this, "scaleX", new float[]{0.9f, 1.0f}), ObjectAnimator.ofFloat(this, "scaleY", new float[]{0.9f, 1.0f})});
                animatorSet.setInterpolator(new OvershootInterpolator());
            }
            animatorSet.setDuration(180);
            animatorSet.start();
            requestLayout();
            this.currentCount = value;
        }

        protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
            super.onMeasure(MeasureSpec.makeMeasureSpec(Math.max(this.width + AndroidUtilities.dp(20.0f), AndroidUtilities.dp(30.0f)), 1073741824), MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(40.0f), 1073741824));
        }

        protected void onDraw(Canvas canvas) {
            int cy = getMeasuredHeight() / 2;
            this.paint.setAlpha(255);
            this.rect.set((float) AndroidUtilities.dp(1.0f), (float) (cy - AndroidUtilities.dp(14.0f)), (float) (getMeasuredWidth() - AndroidUtilities.dp(1.0f)), (float) (AndroidUtilities.dp(14.0f) + cy));
            canvas.drawRoundRect(this.rect, (float) AndroidUtilities.dp(15.0f), (float) AndroidUtilities.dp(15.0f), this.paint);
            if (this.staticLayout != null) {
                this.textPaint.setAlpha((int) ((1.0f - this.rotation) * 255.0f));
                canvas.save();
                canvas.translate((float) ((getMeasuredWidth() - this.width) / 2), (((float) ((getMeasuredHeight() - this.height) / 2)) + AndroidUtilities.dpf2(0.2f)) + (this.rotation * ((float) AndroidUtilities.dp(5.0f))));
                this.staticLayout.draw(canvas);
                canvas.restore();
                this.paint.setAlpha((int) (this.rotation * 255.0f));
                int cx = (int) this.rect.centerX();
                cy = (int) (((float) ((int) this.rect.centerY())) - ((((float) AndroidUtilities.dp(5.0f)) * (1.0f - this.rotation)) + ((float) AndroidUtilities.dp(3.0f))));
                canvas.drawLine((float) (AndroidUtilities.dp(0.5f) + cx), (float) (cy - AndroidUtilities.dp(0.5f)), (float) (cx - AndroidUtilities.dp(6.0f)), (float) (AndroidUtilities.dp(6.0f) + cy), this.paint);
                canvas.drawLine((float) (cx - AndroidUtilities.dp(0.5f)), (float) (cy - AndroidUtilities.dp(0.5f)), (float) (AndroidUtilities.dp(6.0f) + cx), (float) (AndroidUtilities.dp(6.0f) + cy), this.paint);
            }
        }
    }

    private class GroupedPhotosListView extends View implements OnGestureListener {
        private boolean animateAllLine;
        private int animateToDX;
        private int animateToDXStart;
        private int animateToItem = -1;
        private Paint backgroundPaint = new Paint();
        private long currentGroupId;
        private int currentImage;
        private float currentItemProgress = 1.0f;
        private ArrayList<Object> currentObjects = new ArrayList();
        private ArrayList<TLObject> currentPhotos = new ArrayList();
        private int drawDx;
        private GestureDetector gestureDetector;
        private boolean ignoreChanges;
        private ArrayList<ImageReceiver> imagesToDraw = new ArrayList();
        private int itemHeight;
        private int itemSpacing;
        private int itemWidth;
        private int itemY;
        private long lastUpdateTime;
        private float moveLineProgress;
        private boolean moving;
        private int nextImage;
        private float nextItemProgress = 0.0f;
        private int nextPhotoScrolling = -1;
        private Scroller scroll;
        private boolean scrolling;
        private boolean stopedScrolling;
        private ArrayList<ImageReceiver> unusedReceivers = new ArrayList();

        public GroupedPhotosListView(Context context) {
            super(context);
            this.gestureDetector = new GestureDetector(context, this);
            this.scroll = new Scroller(context);
            this.itemWidth = AndroidUtilities.dp(42.0f);
            this.itemHeight = AndroidUtilities.dp(56.0f);
            this.itemSpacing = AndroidUtilities.dp(1.0f);
            this.itemY = AndroidUtilities.dp(3.0f);
            this.backgroundPaint.setColor(Theme.ACTION_BAR_PHOTO_VIEWER_COLOR);
        }

        public void clear() {
            this.currentPhotos.clear();
            this.currentObjects.clear();
            this.imagesToDraw.clear();
        }

        public void fillList() {
            if (this.ignoreChanges) {
                this.ignoreChanges = false;
                return;
            }
            int max;
            int a;
            MessageObject object;
            int min;
            boolean changed = false;
            int newCount = 0;
            Object currentObject = null;
            if (!PhotoViewer.this.imagesArrLocations.isEmpty()) {
                FileLocation location = (FileLocation) PhotoViewer.this.imagesArrLocations.get(PhotoViewer.this.currentIndex);
                newCount = PhotoViewer.this.imagesArrLocations.size();
                currentObject = location;
            } else if (!PhotoViewer.this.imagesArr.isEmpty()) {
                MessageObject messageObject = (MessageObject) PhotoViewer.this.imagesArr.get(PhotoViewer.this.currentIndex);
                MessageObject currentObject2 = messageObject;
                if (messageObject.getGroupIdForUse() == this.currentGroupId) {
                    max = Math.min(PhotoViewer.this.currentIndex + 10, PhotoViewer.this.imagesArr.size());
                    for (a = PhotoViewer.this.currentIndex; a < max; a++) {
                        object = (MessageObject) PhotoViewer.this.imagesArr.get(a);
                        if (PhotoViewer.this.slideshowMessageId == 0 && object.getGroupIdForUse() != this.currentGroupId) {
                            break;
                        }
                        newCount++;
                    }
                    min = Math.max(PhotoViewer.this.currentIndex - 10, 0);
                    for (a = PhotoViewer.this.currentIndex - 1; a >= min; a--) {
                        object = (MessageObject) PhotoViewer.this.imagesArr.get(a);
                        if (PhotoViewer.this.slideshowMessageId == 0 && object.getGroupIdForUse() != this.currentGroupId) {
                            break;
                        }
                        newCount++;
                    }
                } else {
                    changed = true;
                    this.currentGroupId = messageObject.getGroupIdForUse();
                }
            }
            if (currentObject != null) {
                if (!changed) {
                    if (newCount != this.currentPhotos.size() || this.currentObjects.indexOf(currentObject) == -1) {
                        changed = true;
                    } else {
                        int newImageIndex = this.currentObjects.indexOf(currentObject);
                        if (!(this.currentImage == newImageIndex || newImageIndex == -1)) {
                            if (this.animateAllLine) {
                                this.animateToItem = newImageIndex;
                                this.nextImage = newImageIndex;
                                this.animateToDX = (this.currentImage - newImageIndex) * (this.itemWidth + this.itemSpacing);
                                this.moving = true;
                                this.animateAllLine = false;
                                this.lastUpdateTime = System.currentTimeMillis();
                                invalidate();
                            } else {
                                fillImages(true, (this.currentImage - newImageIndex) * (this.itemWidth + this.itemSpacing));
                                this.currentImage = newImageIndex;
                                this.moving = false;
                            }
                            this.drawDx = 0;
                        }
                    }
                }
                if (changed) {
                    this.animateAllLine = false;
                    this.currentPhotos.clear();
                    this.currentObjects.clear();
                    if (!PhotoViewer.this.imagesArrLocations.isEmpty()) {
                        this.currentObjects.addAll(PhotoViewer.this.imagesArrLocations);
                        this.currentPhotos.addAll(PhotoViewer.this.imagesArrLocations);
                        this.currentImage = PhotoViewer.this.currentIndex;
                        this.animateToItem = -1;
                    } else if (!PhotoViewer.this.imagesArr.isEmpty() && (this.currentGroupId != 0 || PhotoViewer.this.slideshowMessageId != 0)) {
                        max = Math.min(PhotoViewer.this.currentIndex + 10, PhotoViewer.this.imagesArr.size());
                        for (a = PhotoViewer.this.currentIndex; a < max; a++) {
                            object = (MessageObject) PhotoViewer.this.imagesArr.get(a);
                            if (PhotoViewer.this.slideshowMessageId == 0 && object.getGroupIdForUse() != this.currentGroupId) {
                                break;
                            }
                            this.currentObjects.add(object);
                            this.currentPhotos.add(FileLoader.getClosestPhotoSizeWithSize(object.photoThumbs, 56, true));
                        }
                        this.currentImage = 0;
                        this.animateToItem = -1;
                        min = Math.max(PhotoViewer.this.currentIndex - 10, 0);
                        for (a = PhotoViewer.this.currentIndex - 1; a >= min; a--) {
                            object = (MessageObject) PhotoViewer.this.imagesArr.get(a);
                            if (PhotoViewer.this.slideshowMessageId == 0 && object.getGroupIdForUse() != this.currentGroupId) {
                                break;
                            }
                            this.currentObjects.add(0, object);
                            this.currentPhotos.add(0, FileLoader.getClosestPhotoSizeWithSize(object.photoThumbs, 56, true));
                            this.currentImage++;
                        }
                    }
                    if (this.currentPhotos.size() == 1) {
                        this.currentPhotos.clear();
                        this.currentObjects.clear();
                    }
                    fillImages(false, 0);
                }
            }
        }

        public void setMoveProgress(float progress) {
            if (!this.scrolling && this.animateToItem < 0) {
                if (progress > 0.0f) {
                    this.nextImage = this.currentImage - 1;
                } else {
                    this.nextImage = this.currentImage + 1;
                }
                if (this.nextImage < 0 || this.nextImage >= this.currentPhotos.size()) {
                    this.currentItemProgress = 1.0f;
                } else {
                    this.currentItemProgress = 1.0f - Math.abs(progress);
                }
                this.nextItemProgress = 1.0f - this.currentItemProgress;
                this.moving = progress != 0.0f;
                invalidate();
                if (!this.currentPhotos.isEmpty()) {
                    if (progress < 0.0f && this.currentImage == this.currentPhotos.size() - 1) {
                        return;
                    }
                    if (progress <= 0.0f || this.currentImage != 0) {
                        this.drawDx = (int) (((float) (this.itemWidth + this.itemSpacing)) * progress);
                        fillImages(true, this.drawDx);
                    }
                }
            }
        }

        private ImageReceiver getFreeReceiver() {
            ImageReceiver receiver;
            if (this.unusedReceivers.isEmpty()) {
                receiver = new ImageReceiver(this);
            } else {
                receiver = (ImageReceiver) this.unusedReceivers.get(0);
                this.unusedReceivers.remove(0);
            }
            this.imagesToDraw.add(receiver);
            receiver.setCurrentAccount(PhotoViewer.this.currentAccount);
            return receiver;
        }

        private void fillImages(boolean move, int dx) {
            if (!(move || this.imagesToDraw.isEmpty())) {
                this.unusedReceivers.addAll(this.imagesToDraw);
                this.imagesToDraw.clear();
                this.moving = false;
                this.moveLineProgress = 1.0f;
                this.currentItemProgress = 1.0f;
                this.nextItemProgress = 0.0f;
            }
            invalidate();
            if (getMeasuredWidth() != 0 && !this.currentPhotos.isEmpty()) {
                int addRightIndex;
                int addLeftIndex;
                int count;
                int a;
                ImageReceiver receiver;
                int x;
                TLObject location;
                int width = getMeasuredWidth();
                int startX = (getMeasuredWidth() / 2) - (this.itemWidth / 2);
                if (move) {
                    addRightIndex = Integer.MIN_VALUE;
                    addLeftIndex = ConnectionsManager.DEFAULT_DATACENTER_ID;
                    count = this.imagesToDraw.size();
                    a = 0;
                    while (a < count) {
                        receiver = (ImageReceiver) this.imagesToDraw.get(a);
                        int num = receiver.getParam();
                        x = (((num - this.currentImage) * (this.itemWidth + this.itemSpacing)) + startX) + dx;
                        if (x > width || this.itemWidth + x < 0) {
                            this.unusedReceivers.add(receiver);
                            this.imagesToDraw.remove(a);
                            count--;
                            a--;
                        }
                        addLeftIndex = Math.min(addLeftIndex, num - 1);
                        addRightIndex = Math.max(addRightIndex, num + 1);
                        a++;
                    }
                } else {
                    addRightIndex = this.currentImage;
                    addLeftIndex = this.currentImage - 1;
                }
                if (addRightIndex != Integer.MIN_VALUE) {
                    count = this.currentPhotos.size();
                    for (a = addRightIndex; a < count; a++) {
                        x = (((a - this.currentImage) * (this.itemWidth + this.itemSpacing)) + startX) + dx;
                        if (x >= width) {
                            break;
                        }
                        location = (TLObject) this.currentPhotos.get(a);
                        if (location instanceof PhotoSize) {
                            location = ((PhotoSize) location).location;
                        }
                        receiver = getFreeReceiver();
                        receiver.setImageCoords(x, this.itemY, this.itemWidth, this.itemHeight);
                        receiver.setImage(null, null, null, null, (FileLocation) location, "80_80", 0, null, 1);
                        receiver.setParam(a);
                    }
                }
                if (addLeftIndex != ConnectionsManager.DEFAULT_DATACENTER_ID) {
                    a = addLeftIndex;
                    while (a >= 0) {
                        x = ((((a - this.currentImage) * (this.itemWidth + this.itemSpacing)) + startX) + dx) + this.itemWidth;
                        if (x > 0) {
                            location = (TLObject) this.currentPhotos.get(a);
                            if (location instanceof PhotoSize) {
                                location = ((PhotoSize) location).location;
                            }
                            receiver = getFreeReceiver();
                            receiver.setImageCoords(x, this.itemY, this.itemWidth, this.itemHeight);
                            receiver.setImage(null, null, null, null, (FileLocation) location, "80_80", 0, null, 1);
                            receiver.setParam(a);
                            a--;
                        } else {
                            return;
                        }
                    }
                }
            }
        }

        public boolean onDown(MotionEvent e) {
            if (!this.scroll.isFinished()) {
                this.scroll.abortAnimation();
            }
            this.animateToItem = -1;
            return true;
        }

        public void onShowPress(MotionEvent e) {
        }

        public boolean onSingleTapUp(MotionEvent e) {
            stopScrolling();
            int count = this.imagesToDraw.size();
            for (int a = 0; a < count; a++) {
                ImageReceiver receiver = (ImageReceiver) this.imagesToDraw.get(a);
                if (receiver.isInsideImage(e.getX(), e.getY())) {
                    int num = receiver.getParam();
                    if (num < 0 || num >= this.currentObjects.size()) {
                        return true;
                    }
                    int idx;
                    if (!PhotoViewer.this.imagesArr.isEmpty()) {
                        idx = PhotoViewer.this.imagesArr.indexOf((MessageObject) this.currentObjects.get(num));
                        if (PhotoViewer.this.currentIndex == idx) {
                            return true;
                        }
                        this.moveLineProgress = 1.0f;
                        this.animateAllLine = true;
                        PhotoViewer.this.currentIndex = -1;
                        if (PhotoViewer.this.currentThumb != null) {
                            PhotoViewer.this.currentThumb.release();
                            PhotoViewer.this.currentThumb = null;
                        }
                        PhotoViewer.this.setImageIndex(idx, true);
                    } else if (!PhotoViewer.this.imagesArrLocations.isEmpty()) {
                        idx = PhotoViewer.this.imagesArrLocations.indexOf((FileLocation) this.currentObjects.get(num));
                        if (PhotoViewer.this.currentIndex == idx) {
                            return true;
                        }
                        this.moveLineProgress = 1.0f;
                        this.animateAllLine = true;
                        PhotoViewer.this.currentIndex = -1;
                        if (PhotoViewer.this.currentThumb != null) {
                            PhotoViewer.this.currentThumb.release();
                            PhotoViewer.this.currentThumb = null;
                        }
                        PhotoViewer.this.setImageIndex(idx, true);
                    }
                    return false;
                }
            }
            return false;
        }

        private void updateAfterScroll() {
            int indexChange = 0;
            int dx = this.drawDx;
            if (Math.abs(dx) > (this.itemWidth / 2) + this.itemSpacing) {
                if (dx > 0) {
                    dx -= (this.itemWidth / 2) + this.itemSpacing;
                    indexChange = 0 + 1;
                } else {
                    dx += (this.itemWidth / 2) + this.itemSpacing;
                    indexChange = 0 - 1;
                }
                indexChange += dx / (this.itemWidth + (this.itemSpacing * 2));
            }
            this.nextPhotoScrolling = this.currentImage - indexChange;
            if (PhotoViewer.this.currentIndex != this.nextPhotoScrolling && this.nextPhotoScrolling >= 0 && this.nextPhotoScrolling < this.currentPhotos.size()) {
                MessageObject photo = this.currentObjects.get(this.nextPhotoScrolling);
                int nextPhoto = -1;
                if (!PhotoViewer.this.imagesArr.isEmpty()) {
                    nextPhoto = PhotoViewer.this.imagesArr.indexOf(photo);
                } else if (!PhotoViewer.this.imagesArrLocations.isEmpty()) {
                    nextPhoto = PhotoViewer.this.imagesArrLocations.indexOf((FileLocation) photo);
                }
                if (nextPhoto >= 0) {
                    this.ignoreChanges = true;
                    PhotoViewer.this.currentIndex = -1;
                    if (PhotoViewer.this.currentThumb != null) {
                        PhotoViewer.this.currentThumb.release();
                        PhotoViewer.this.currentThumb = null;
                    }
                    PhotoViewer.this.setImageIndex(nextPhoto, true);
                }
            }
            if (!this.scrolling) {
                this.scrolling = true;
                this.stopedScrolling = false;
            }
            fillImages(true, this.drawDx);
        }

        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            this.drawDx = (int) (((float) this.drawDx) - distanceX);
            int min = getMinScrollX();
            int max = getMaxScrollX();
            if (this.drawDx < min) {
                this.drawDx = min;
            } else if (this.drawDx > max) {
                this.drawDx = max;
            }
            updateAfterScroll();
            return false;
        }

        public void onLongPress(MotionEvent e) {
        }

        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            this.scroll.abortAnimation();
            if (this.currentPhotos.size() >= 10) {
                this.scroll.fling(this.drawDx, 0, Math.round(velocityX), 0, getMinScrollX(), getMaxScrollX(), 0, 0);
            }
            return false;
        }

        private void stopScrolling() {
            this.scrolling = false;
            if (!this.scroll.isFinished()) {
                this.scroll.abortAnimation();
            }
            if (this.nextPhotoScrolling >= 0 && this.nextPhotoScrolling < this.currentObjects.size()) {
                this.stopedScrolling = true;
                int i = this.nextPhotoScrolling;
                this.animateToItem = i;
                this.nextImage = i;
                this.animateToDX = (this.currentImage - this.nextPhotoScrolling) * (this.itemWidth + this.itemSpacing);
                this.animateToDXStart = this.drawDx;
                this.moveLineProgress = 1.0f;
                this.nextPhotoScrolling = -1;
            }
            invalidate();
        }

        public boolean onTouchEvent(MotionEvent event) {
            boolean z = false;
            if (!this.currentPhotos.isEmpty() && getAlpha() == 1.0f) {
                if (this.gestureDetector.onTouchEvent(event) || super.onTouchEvent(event)) {
                    z = true;
                }
                if (this.scrolling && event.getAction() == 1 && this.scroll.isFinished()) {
                    stopScrolling();
                }
            }
            return z;
        }

        private int getMinScrollX() {
            return (-((this.currentPhotos.size() - this.currentImage) - 1)) * (this.itemWidth + (this.itemSpacing * 2));
        }

        private int getMaxScrollX() {
            return this.currentImage * (this.itemWidth + (this.itemSpacing * 2));
        }

        protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
            super.onLayout(changed, left, top, right, bottom);
            fillImages(false, 0);
        }

        protected void onDraw(Canvas canvas) {
            if (!this.imagesToDraw.isEmpty()) {
                PhotoSize photoSize;
                int trueWidth;
                int nextTrueWidth;
                canvas.drawRect(0.0f, 0.0f, (float) getMeasuredWidth(), (float) getMeasuredHeight(), this.backgroundPaint);
                int count = this.imagesToDraw.size();
                int moveX = this.drawDx;
                int maxItemWidth = (int) (((float) this.itemWidth) * 2.0f);
                int padding = AndroidUtilities.dp(8.0f);
                TLObject object = (TLObject) this.currentPhotos.get(this.currentImage);
                if (object instanceof PhotoSize) {
                    photoSize = (PhotoSize) object;
                    trueWidth = Math.max(this.itemWidth, (int) (((float) photoSize.w) * (((float) this.itemHeight) / ((float) photoSize.h))));
                } else {
                    trueWidth = this.itemHeight;
                }
                int currentPaddings = (int) (((float) (padding * 2)) * this.currentItemProgress);
                trueWidth = (this.itemWidth + ((int) (((float) (Math.min(maxItemWidth, trueWidth) - this.itemWidth)) * this.currentItemProgress))) + currentPaddings;
                if (this.nextImage < 0 || this.nextImage >= this.currentPhotos.size()) {
                    nextTrueWidth = this.itemWidth;
                } else {
                    object = (TLObject) this.currentPhotos.get(this.nextImage);
                    if (object instanceof PhotoSize) {
                        photoSize = (PhotoSize) object;
                        nextTrueWidth = Math.max(this.itemWidth, (int) (((float) photoSize.w) * (((float) this.itemHeight) / ((float) photoSize.h))));
                    } else {
                        nextTrueWidth = this.itemHeight;
                    }
                }
                nextTrueWidth = Math.min(maxItemWidth, nextTrueWidth);
                int nextPaddings = (int) (((float) (padding * 2)) * this.nextItemProgress);
                moveX = (int) ((((float) (this.nextImage > this.currentImage ? -1 : 1)) * (this.nextItemProgress * ((float) (((nextTrueWidth + nextPaddings) - this.itemWidth) / 2)))) + ((float) moveX));
                nextTrueWidth = (this.itemWidth + ((int) (((float) (nextTrueWidth - this.itemWidth)) * this.nextItemProgress))) + nextPaddings;
                int startX = (getMeasuredWidth() - trueWidth) / 2;
                for (int a = 0; a < count; a++) {
                    ImageReceiver receiver = (ImageReceiver) this.imagesToDraw.get(a);
                    int num = receiver.getParam();
                    if (num == this.currentImage) {
                        receiver.setImageX((startX + moveX) + (currentPaddings / 2));
                        receiver.setImageWidth(trueWidth - currentPaddings);
                    } else {
                        if (this.nextImage < this.currentImage) {
                            if (num >= this.currentImage) {
                                receiver.setImageX((((startX + trueWidth) + this.itemSpacing) + (((receiver.getParam() - this.currentImage) - 1) * (this.itemWidth + this.itemSpacing))) + moveX);
                            } else if (num <= this.nextImage) {
                                receiver.setImageX((((((receiver.getParam() - this.currentImage) + 1) * (this.itemWidth + this.itemSpacing)) + startX) - (this.itemSpacing + nextTrueWidth)) + moveX);
                            } else {
                                receiver.setImageX((((receiver.getParam() - this.currentImage) * (this.itemWidth + this.itemSpacing)) + startX) + moveX);
                            }
                        } else if (num < this.currentImage) {
                            receiver.setImageX((((receiver.getParam() - this.currentImage) * (this.itemWidth + this.itemSpacing)) + startX) + moveX);
                        } else if (num <= this.nextImage) {
                            receiver.setImageX((((startX + trueWidth) + this.itemSpacing) + (((receiver.getParam() - this.currentImage) - 1) * (this.itemWidth + this.itemSpacing))) + moveX);
                        } else {
                            receiver.setImageX(((((startX + trueWidth) + this.itemSpacing) + (((receiver.getParam() - this.currentImage) - 2) * (this.itemWidth + this.itemSpacing))) + (this.itemSpacing + nextTrueWidth)) + moveX);
                        }
                        if (num == this.nextImage) {
                            receiver.setImageWidth(nextTrueWidth - nextPaddings);
                            receiver.setImageX(receiver.getImageX() + (nextPaddings / 2));
                        } else {
                            receiver.setImageWidth(this.itemWidth);
                        }
                    }
                    receiver.draw(canvas);
                }
                long newTime = System.currentTimeMillis();
                long dt = newTime - this.lastUpdateTime;
                if (dt > 17) {
                    dt = 17;
                }
                this.lastUpdateTime = newTime;
                if (this.animateToItem >= 0) {
                    if (this.moveLineProgress > 0.0f) {
                        this.moveLineProgress -= ((float) dt) / 200.0f;
                        if (this.animateToItem == this.currentImage) {
                            if (this.currentItemProgress < 1.0f) {
                                this.currentItemProgress += ((float) dt) / 200.0f;
                                if (this.currentItemProgress > 1.0f) {
                                    this.currentItemProgress = 1.0f;
                                }
                            }
                            this.drawDx = this.animateToDXStart + ((int) Math.ceil((double) (this.currentItemProgress * ((float) (this.animateToDX - this.animateToDXStart)))));
                        } else {
                            this.nextItemProgress = CubicBezierInterpolator.EASE_OUT.getInterpolation(1.0f - this.moveLineProgress);
                            if (this.stopedScrolling) {
                                if (this.currentItemProgress > 0.0f) {
                                    this.currentItemProgress -= ((float) dt) / 200.0f;
                                    if (this.currentItemProgress < 0.0f) {
                                        this.currentItemProgress = 0.0f;
                                    }
                                }
                                this.drawDx = this.animateToDXStart + ((int) Math.ceil((double) (this.nextItemProgress * ((float) (this.animateToDX - this.animateToDXStart)))));
                            } else {
                                this.currentItemProgress = CubicBezierInterpolator.EASE_OUT.getInterpolation(this.moveLineProgress);
                                this.drawDx = (int) Math.ceil((double) (this.nextItemProgress * ((float) this.animateToDX)));
                            }
                        }
                        if (this.moveLineProgress <= 0.0f) {
                            this.currentImage = this.animateToItem;
                            this.moveLineProgress = 1.0f;
                            this.currentItemProgress = 1.0f;
                            this.nextItemProgress = 0.0f;
                            this.moving = false;
                            this.stopedScrolling = false;
                            this.drawDx = 0;
                            this.animateToItem = -1;
                        }
                    }
                    fillImages(true, this.drawDx);
                    invalidate();
                }
                if (this.scrolling && this.currentItemProgress > 0.0f) {
                    this.currentItemProgress -= ((float) dt) / 200.0f;
                    if (this.currentItemProgress < 0.0f) {
                        this.currentItemProgress = 0.0f;
                    }
                    invalidate();
                }
                if (!this.scroll.isFinished()) {
                    if (this.scroll.computeScrollOffset()) {
                        this.drawDx = this.scroll.getCurrX();
                        updateAfterScroll();
                        invalidate();
                    }
                    if (this.scroll.isFinished()) {
                        stopScrolling();
                    }
                }
            }
        }
    }

    private static class LinkMovementMethodMy extends LinkMovementMethod {
        private LinkMovementMethodMy() {
        }

        public boolean onTouchEvent(TextView widget, Spannable buffer, MotionEvent event) {
            try {
                boolean result = super.onTouchEvent(widget, buffer, event);
                if (event.getAction() != 1 && event.getAction() != 3) {
                    return result;
                }
                Selection.removeSelection(buffer);
                return result;
            } catch (Throwable e) {
                FileLog.e(e);
                return false;
            }
        }
    }

    private class PhotoProgressView {
        private float alpha = 1.0f;
        private float animatedAlphaValue = 1.0f;
        private float animatedProgressValue = 0.0f;
        private float animationProgressStart = 0.0f;
        private int backgroundState = -1;
        private float currentProgress = 0.0f;
        private long currentProgressTime = 0;
        private long lastUpdateTime = 0;
        private View parent;
        private int previousBackgroundState = -2;
        private RectF progressRect = new RectF();
        private float radOffset = 0.0f;
        private float scale = 1.0f;
        private int size = AndroidUtilities.dp(64.0f);

        public PhotoProgressView(Context context, View parentView) {
            if (PhotoViewer.decelerateInterpolator == null) {
                PhotoViewer.decelerateInterpolator = new DecelerateInterpolator(1.5f);
                PhotoViewer.progressPaint = new Paint(1);
                PhotoViewer.progressPaint.setStyle(Style.STROKE);
                PhotoViewer.progressPaint.setStrokeCap(Cap.ROUND);
                PhotoViewer.progressPaint.setStrokeWidth((float) AndroidUtilities.dp(3.0f));
                PhotoViewer.progressPaint.setColor(-1);
            }
            this.parent = parentView;
        }

        private void updateAnimation() {
            long newTime = System.currentTimeMillis();
            long dt = newTime - this.lastUpdateTime;
            if (dt > 18) {
                dt = 18;
            }
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
                        this.animatedProgressValue = this.animationProgressStart + (PhotoViewer.decelerateInterpolator.getInterpolation(((float) this.currentProgressTime) / 300.0f) * progressDiff);
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
            if (this.backgroundState != state || !animated) {
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
            int x = (PhotoViewer.this.getContainerViewWidth() - sizeScaled) / 2;
            int y = (PhotoViewer.this.getContainerViewHeight() - sizeScaled) / 2;
            if (this.previousBackgroundState >= 0 && this.previousBackgroundState < 4) {
                drawable = PhotoViewer.progressDrawables[this.previousBackgroundState];
                if (drawable != null) {
                    drawable.setAlpha((int) ((this.animatedAlphaValue * 255.0f) * this.alpha));
                    drawable.setBounds(x, y, x + sizeScaled, y + sizeScaled);
                    drawable.draw(canvas);
                }
            }
            if (this.backgroundState >= 0 && this.backgroundState < 4) {
                drawable = PhotoViewer.progressDrawables[this.backgroundState];
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
                    PhotoViewer.progressPaint.setAlpha((int) ((this.animatedAlphaValue * 255.0f) * this.alpha));
                } else {
                    PhotoViewer.progressPaint.setAlpha((int) (this.alpha * 255.0f));
                }
                this.progressRect.set((float) (x + diff), (float) (y + diff), (float) ((x + sizeScaled) - diff), (float) ((y + sizeScaled) - diff));
                canvas.drawArc(this.progressRect, this.radOffset - 0.049804688f, Math.max(4.0f, 360.0f * this.animatedProgressValue), false, PhotoViewer.progressPaint);
                updateAnimation();
            }
        }
    }

    public interface PhotoViewerProvider {
        boolean allowCaption();

        boolean allowGroupPhotos();

        boolean canCaptureMorePhotos();

        boolean canScrollAway();

        boolean cancelButtonPressed();

        void deleteImageAtIndex(int i);

        String getDeleteMessageString();

        int getPhotoIndex(int i);

        PlaceProviderObject getPlaceForPhoto(MessageObject messageObject, FileLocation fileLocation, int i);

        int getSelectedCount();

        HashMap<Object, Object> getSelectedPhotos();

        ArrayList<Object> getSelectedPhotosOrder();

        BitmapHolder getThumbForPhoto(MessageObject messageObject, FileLocation fileLocation, int i);

        boolean isPhotoChecked(int i);

        void needAddMorePhotos();

        boolean scaleToFill();

        void sendButtonPressed(int i, VideoEditedInfo videoEditedInfo);

        int setPhotoChecked(int i, VideoEditedInfo videoEditedInfo);

        int setPhotoUnchecked(Object obj);

        void toggleGroupPhotosEnabled();

        void updatePhotoAtIndex(int i);

        void willHidePhotoViewer();

        void willSwitchFromPhoto(MessageObject messageObject, FileLocation fileLocation, int i);
    }

    public static class PlaceProviderObject {
        public int clipBottomAddition;
        public int clipTopAddition;
        public int dialogId;
        public ImageReceiver imageReceiver;
        public int index;
        public boolean isEvent;
        public View parentView;
        public int radius;
        public float scale = 1.0f;
        public int size;
        public BitmapHolder thumb;
        public int viewX;
        public int viewY;
    }

    private class QualityChooseView extends View {
        private int circleSize;
        private int gapSize;
        private int lineSize;
        private boolean moving;
        private Paint paint = new Paint(1);
        private int sideSide;
        private boolean startMoving;
        private int startMovingQuality;
        private float startX;
        private TextPaint textPaint = new TextPaint(1);

        public QualityChooseView(Context context) {
            super(context);
            this.textPaint.setTextSize((float) AndroidUtilities.dp(12.0f));
            this.textPaint.setColor(-3289651);
        }

        public boolean onTouchEvent(MotionEvent event) {
            boolean z = false;
            float x = event.getX();
            int a;
            int cx;
            if (event.getAction() == 0) {
                getParent().requestDisallowInterceptTouchEvent(true);
                a = 0;
                while (a < PhotoViewer.this.compressionsCount) {
                    cx = (this.sideSide + (((this.lineSize + (this.gapSize * 2)) + this.circleSize) * a)) + (this.circleSize / 2);
                    if (x <= ((float) (cx - AndroidUtilities.dp(15.0f))) || x >= ((float) (AndroidUtilities.dp(15.0f) + cx))) {
                        a++;
                    } else {
                        if (a == PhotoViewer.this.selectedCompression) {
                            z = true;
                        }
                        this.startMoving = z;
                        this.startX = x;
                        this.startMovingQuality = PhotoViewer.this.selectedCompression;
                    }
                }
            } else if (event.getAction() == 2) {
                if (this.startMoving) {
                    if (Math.abs(this.startX - x) >= AndroidUtilities.getPixelsInCM(0.5f, true)) {
                        this.moving = true;
                        this.startMoving = false;
                    }
                } else if (this.moving) {
                    a = 0;
                    while (a < PhotoViewer.this.compressionsCount) {
                        cx = (this.sideSide + (((this.lineSize + (this.gapSize * 2)) + this.circleSize) * a)) + (this.circleSize / 2);
                        int diff = ((this.lineSize / 2) + (this.circleSize / 2)) + this.gapSize;
                        if (x <= ((float) (cx - diff)) || x >= ((float) (cx + diff))) {
                            a++;
                        } else if (PhotoViewer.this.selectedCompression != a) {
                            PhotoViewer.this.selectedCompression = a;
                            PhotoViewer.this.didChangedCompressionLevel(false);
                            invalidate();
                        }
                    }
                }
            } else if (event.getAction() == 1 || event.getAction() == 3) {
                if (!this.moving) {
                    a = 0;
                    while (a < PhotoViewer.this.compressionsCount) {
                        cx = (this.sideSide + (((this.lineSize + (this.gapSize * 2)) + this.circleSize) * a)) + (this.circleSize / 2);
                        if (x <= ((float) (cx - AndroidUtilities.dp(15.0f))) || x >= ((float) (AndroidUtilities.dp(15.0f) + cx))) {
                            a++;
                        } else if (PhotoViewer.this.selectedCompression != a) {
                            PhotoViewer.this.selectedCompression = a;
                            PhotoViewer.this.didChangedCompressionLevel(true);
                            invalidate();
                        }
                    }
                } else if (PhotoViewer.this.selectedCompression != this.startMovingQuality) {
                    PhotoViewer.this.requestVideoPreview(1);
                }
                this.startMoving = false;
                this.moving = false;
            }
            return true;
        }

        protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
            this.circleSize = AndroidUtilities.dp(12.0f);
            this.gapSize = AndroidUtilities.dp(2.0f);
            this.sideSide = AndroidUtilities.dp(18.0f);
        }

        protected void onDraw(Canvas canvas) {
            if (PhotoViewer.this.compressionsCount != 1) {
                this.lineSize = (((getMeasuredWidth() - (this.circleSize * PhotoViewer.this.compressionsCount)) - (this.gapSize * 8)) - (this.sideSide * 2)) / (PhotoViewer.this.compressionsCount - 1);
            } else {
                this.lineSize = ((getMeasuredWidth() - (this.circleSize * PhotoViewer.this.compressionsCount)) - (this.gapSize * 8)) - (this.sideSide * 2);
            }
            int cy = (getMeasuredHeight() / 2) + AndroidUtilities.dp(6.0f);
            int a = 0;
            while (a < PhotoViewer.this.compressionsCount) {
                String text;
                int cx = (this.sideSide + (((this.lineSize + (this.gapSize * 2)) + this.circleSize) * a)) + (this.circleSize / 2);
                if (a <= PhotoViewer.this.selectedCompression) {
                    this.paint.setColor(-11292945);
                } else {
                    this.paint.setColor(1728053247);
                }
                if (a == PhotoViewer.this.compressionsCount - 1) {
                    text = Math.min(PhotoViewer.this.originalWidth, PhotoViewer.this.originalHeight) + TtmlNode.TAG_P;
                } else if (a == 0) {
                    text = "240p";
                } else if (a == 1) {
                    text = "360p";
                } else if (a == 2) {
                    text = "480p";
                } else {
                    text = "720p";
                }
                float width = this.textPaint.measureText(text);
                canvas.drawCircle((float) cx, (float) cy, a == PhotoViewer.this.selectedCompression ? (float) AndroidUtilities.dp(8.0f) : (float) (this.circleSize / 2), this.paint);
                canvas.drawText(text, ((float) cx) - (width / 2.0f), (float) (cy - AndroidUtilities.dp(16.0f)), this.textPaint);
                if (a != 0) {
                    int x = ((cx - (this.circleSize / 2)) - this.gapSize) - this.lineSize;
                    canvas.drawRect((float) x, (float) (cy - AndroidUtilities.dp(1.0f)), (float) (this.lineSize + x), (float) (AndroidUtilities.dp(2.0f) + cy), this.paint);
                }
                a++;
            }
        }
    }

    public static class EmptyPhotoViewerProvider implements PhotoViewerProvider {
        public PlaceProviderObject getPlaceForPhoto(MessageObject messageObject, FileLocation fileLocation, int index) {
            return null;
        }

        public BitmapHolder getThumbForPhoto(MessageObject messageObject, FileLocation fileLocation, int index) {
            return null;
        }

        public void willSwitchFromPhoto(MessageObject messageObject, FileLocation fileLocation, int index) {
        }

        public void willHidePhotoViewer() {
        }

        public int setPhotoUnchecked(Object photoEntry) {
            return -1;
        }

        public boolean isPhotoChecked(int index) {
            return false;
        }

        public int setPhotoChecked(int index, VideoEditedInfo videoEditedInfo) {
            return -1;
        }

        public boolean cancelButtonPressed() {
            return true;
        }

        public void sendButtonPressed(int index, VideoEditedInfo videoEditedInfo) {
        }

        public int getSelectedCount() {
            return 0;
        }

        public void updatePhotoAtIndex(int index) {
        }

        public boolean allowCaption() {
            return true;
        }

        public boolean scaleToFill() {
            return false;
        }

        public void toggleGroupPhotosEnabled() {
        }

        public ArrayList<Object> getSelectedPhotosOrder() {
            return null;
        }

        public HashMap<Object, Object> getSelectedPhotos() {
            return null;
        }

        public boolean canScrollAway() {
            return true;
        }

        public boolean allowGroupPhotos() {
            return true;
        }

        public void needAddMorePhotos() {
        }

        public int getPhotoIndex(int index) {
            return -1;
        }

        public void deleteImageAtIndex(int index) {
        }

        public String getDeleteMessageString() {
            return null;
        }

        public boolean canCaptureMorePhotos() {
            return true;
        }
    }

    private class FrameLayoutDrawer extends SizeNotifierFrameLayoutPhoto {
        private Paint paint = new Paint();

        public FrameLayoutDrawer(Context context) {
            super(context);
            setWillNotDraw(false);
            this.paint.setColor(855638016);
        }

        protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
            int widthSize = MeasureSpec.getSize(widthMeasureSpec);
            int heightSize = MeasureSpec.getSize(heightMeasureSpec);
            setMeasuredDimension(widthSize, heightSize);
            measureChildWithMargins(PhotoViewer.this.captionEditText, widthMeasureSpec, 0, heightMeasureSpec, 0);
            int inputFieldHeight = PhotoViewer.this.captionEditText.getMeasuredHeight();
            int childCount = getChildCount();
            for (int i = 0; i < childCount; i++) {
                View child = getChildAt(i);
                if (!(child.getVisibility() == 8 || child == PhotoViewer.this.captionEditText)) {
                    if (child == PhotoViewer.this.aspectRatioFrameLayout) {
                        measureChildWithMargins(child, widthMeasureSpec, 0, MeasureSpec.makeMeasureSpec((VERSION.SDK_INT >= 21 ? AndroidUtilities.statusBarHeight : 0) + AndroidUtilities.displaySize.y, 1073741824), 0);
                    } else if (!PhotoViewer.this.captionEditText.isPopupView(child)) {
                        measureChildWithMargins(child, widthMeasureSpec, 0, heightMeasureSpec, 0);
                    } else if (!AndroidUtilities.isInMultiwindow) {
                        child.measure(MeasureSpec.makeMeasureSpec(widthSize, 1073741824), MeasureSpec.makeMeasureSpec(child.getLayoutParams().height, 1073741824));
                    } else if (AndroidUtilities.isTablet()) {
                        child.measure(MeasureSpec.makeMeasureSpec(widthSize, 1073741824), MeasureSpec.makeMeasureSpec(Math.min(AndroidUtilities.dp(320.0f), (heightSize - inputFieldHeight) - AndroidUtilities.statusBarHeight), 1073741824));
                    } else {
                        child.measure(MeasureSpec.makeMeasureSpec(widthSize, 1073741824), MeasureSpec.makeMeasureSpec((heightSize - inputFieldHeight) - AndroidUtilities.statusBarHeight, 1073741824));
                    }
                }
            }
        }

        protected void onLayout(boolean changed, int l, int t, int r, int b) {
            int count = getChildCount();
            int paddingBottom = (getKeyboardHeight() > AndroidUtilities.dp(20.0f) || AndroidUtilities.isInMultiwindow) ? 0 : PhotoViewer.this.captionEditText.getEmojiPadding();
            for (int i = 0; i < count; i++) {
                View child = getChildAt(i);
                if (child.getVisibility() != 8) {
                    int childLeft;
                    int childTop;
                    FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) child.getLayoutParams();
                    int width = child.getMeasuredWidth();
                    int height = child.getMeasuredHeight();
                    int gravity = lp.gravity;
                    if (gravity == -1) {
                        gravity = 51;
                    }
                    int verticalGravity = gravity & 112;
                    switch ((gravity & 7) & 7) {
                        case 1:
                            childLeft = ((((r - l) - width) / 2) + lp.leftMargin) - lp.rightMargin;
                            break;
                        case 5:
                            childLeft = ((r - l) - width) - lp.rightMargin;
                            break;
                        default:
                            childLeft = lp.leftMargin;
                            break;
                    }
                    switch (verticalGravity) {
                        case 16:
                            childTop = (((((b - paddingBottom) - t) - height) / 2) + lp.topMargin) - lp.bottomMargin;
                            break;
                        case 48:
                            childTop = lp.topMargin;
                            break;
                        case 80:
                            childTop = (((b - paddingBottom) - t) - height) - lp.bottomMargin;
                            break;
                        default:
                            childTop = lp.topMargin;
                            break;
                    }
                    if (child == PhotoViewer.this.mentionListView) {
                        childTop -= PhotoViewer.this.captionEditText.getMeasuredHeight();
                    } else if (PhotoViewer.this.captionEditText.isPopupView(child)) {
                        if (AndroidUtilities.isInMultiwindow) {
                            childTop = (PhotoViewer.this.captionEditText.getTop() - child.getMeasuredHeight()) + AndroidUtilities.dp(1.0f);
                        } else {
                            childTop = PhotoViewer.this.captionEditText.getBottom();
                        }
                    } else if (child == PhotoViewer.this.selectedPhotosListView) {
                        childTop = PhotoViewer.this.actionBar.getMeasuredHeight();
                    } else if (child == PhotoViewer.this.captionTextView || child == PhotoViewer.this.switchCaptionTextView) {
                        int offset = 0;
                        if (!PhotoViewer.this.groupedPhotosListView.currentPhotos.isEmpty()) {
                            offset = 0 + PhotoViewer.this.groupedPhotosListView.getMeasuredHeight();
                        }
                        childTop -= offset;
                    } else if (PhotoViewer.this.hintTextView != null && child == PhotoViewer.this.hintTextView) {
                        childTop = PhotoViewer.this.selectedPhotosListView.getBottom() + AndroidUtilities.dp(3.0f);
                    } else if (child == PhotoViewer.this.cameraItem) {
                        int top = PhotoViewer.this.pickerView.getTop();
                        float f = (PhotoViewer.this.sendPhotoType == 4 || PhotoViewer.this.sendPhotoType == 5) ? 40.0f : 15.0f;
                        childTop = (top - AndroidUtilities.dp(f)) - PhotoViewer.this.cameraItem.getMeasuredHeight();
                    }
                    child.layout(childLeft, childTop, childLeft + width, childTop + height);
                }
            }
            notifyHeightChanged();
        }

        protected void onDraw(Canvas canvas) {
            PhotoViewer.this.onDraw(canvas);
            if (VERSION.SDK_INT >= 21 && AndroidUtilities.statusBarHeight != 0 && PhotoViewer.this.actionBar != null) {
                this.paint.setAlpha((int) ((255.0f * PhotoViewer.this.actionBar.getAlpha()) * 0.2f));
                canvas.drawRect(0.0f, 0.0f, (float) getMeasuredWidth(), (float) AndroidUtilities.statusBarHeight, this.paint);
            }
        }

        protected boolean drawChild(Canvas canvas, View child, long drawingTime) {
            if (child == PhotoViewer.this.mentionListView || child == PhotoViewer.this.captionEditText) {
                if (!PhotoViewer.this.captionEditText.isPopupShowing() && PhotoViewer.this.captionEditText.getEmojiPadding() == 0 && ((AndroidUtilities.usingHardwareInput && PhotoViewer.this.captionEditText.getTag() == null) || getKeyboardHeight() == 0)) {
                    return false;
                }
            } else if (child == PhotoViewer.this.cameraItem || child == PhotoViewer.this.pickerView || child == PhotoViewer.this.pickerViewSendButton || child == PhotoViewer.this.captionTextView || (PhotoViewer.this.muteItem.getVisibility() == 0 && child == PhotoViewer.this.bottomLayout)) {
                int paddingBottom;
                if (getKeyboardHeight() > AndroidUtilities.dp(20.0f) || AndroidUtilities.isInMultiwindow) {
                    paddingBottom = 0;
                } else {
                    paddingBottom = PhotoViewer.this.captionEditText.getEmojiPadding();
                }
                if (PhotoViewer.this.captionEditText.isPopupShowing() || ((AndroidUtilities.usingHardwareInput && PhotoViewer.this.captionEditText.getTag() != null) || getKeyboardHeight() > 0 || paddingBottom != 0)) {
                    PhotoViewer.this.bottomTouchEnabled = false;
                    return false;
                }
                PhotoViewer.this.bottomTouchEnabled = true;
            } else if (child == PhotoViewer.this.checkImageView || child == PhotoViewer.this.photosCounterView) {
                if (PhotoViewer.this.captionEditText.getTag() != null) {
                    PhotoViewer.this.bottomTouchEnabled = false;
                    return false;
                }
                PhotoViewer.this.bottomTouchEnabled = true;
            } else if (child == PhotoViewer.this.miniProgressView) {
                return false;
            }
            try {
                if (child == PhotoViewer.this.aspectRatioFrameLayout || !super.drawChild(canvas, child, drawingTime)) {
                    return false;
                }
                return true;
            } catch (Throwable th) {
                return true;
            }
        }
    }

    private class ListAdapter extends SelectionAdapter {
        private Context mContext;

        public ListAdapter(Context context) {
            this.mContext = context;
        }

        public boolean isEnabled(ViewHolder holder) {
            return true;
        }

        public int getItemCount() {
            if (PhotoViewer.this.placeProvider == null || PhotoViewer.this.placeProvider.getSelectedPhotosOrder() == null) {
                return 0;
            }
            if (PhotoViewer.this.placeProvider.allowGroupPhotos()) {
                return PhotoViewer.this.placeProvider.getSelectedPhotosOrder().size() + 1;
            }
            return PhotoViewer.this.placeProvider.getSelectedPhotosOrder().size();
        }

        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view;
            switch (viewType) {
                case 0:
                    View cell = new PhotoPickerPhotoCell(this.mContext, false);
                    cell.checkFrame.setOnClickListener(new OnClickListener() {
                        public void onClick(View v) {
                            Object photoEntry = ((View) v.getParent()).getTag();
                            int idx = PhotoViewer.this.imagesArrLocals.indexOf(photoEntry);
                            int num;
                            if (idx >= 0) {
                                num = PhotoViewer.this.placeProvider.setPhotoChecked(idx, PhotoViewer.this.getCurrentVideoEditedInfo());
                                boolean checked = PhotoViewer.this.placeProvider.isPhotoChecked(idx);
                                if (idx == PhotoViewer.this.currentIndex) {
                                    PhotoViewer.this.checkImageView.setChecked(-1, false, true);
                                }
                                if (num >= 0) {
                                    if (PhotoViewer.this.placeProvider.allowGroupPhotos()) {
                                        num++;
                                    }
                                    PhotoViewer.this.selectedPhotosAdapter.notifyItemRemoved(num);
                                }
                                PhotoViewer.this.updateSelectedCount();
                                return;
                            }
                            num = PhotoViewer.this.placeProvider.setPhotoUnchecked(photoEntry);
                            if (num >= 0) {
                                if (PhotoViewer.this.placeProvider.allowGroupPhotos()) {
                                    num++;
                                }
                                PhotoViewer.this.selectedPhotosAdapter.notifyItemRemoved(num);
                                PhotoViewer.this.updateSelectedCount();
                            }
                        }
                    });
                    view = cell;
                    break;
                default:
                    View imageView = new ImageView(this.mContext) {
                        protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
                            super.onMeasure(MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(66.0f), 1073741824), MeasureSpec.makeMeasureSpec(MeasureSpec.getSize(heightMeasureSpec), 1073741824));
                        }
                    };
                    imageView.setScaleType(ScaleType.CENTER);
                    imageView.setImageResource(R.drawable.photos_group);
                    view = imageView;
                    break;
            }
            return new Holder(view);
        }

        public void onBindViewHolder(ViewHolder holder, int position) {
            switch (holder.getItemViewType()) {
                case 0:
                    PhotoPickerPhotoCell cell = holder.itemView;
                    cell.itemWidth = AndroidUtilities.dp(82.0f);
                    BackupImageView imageView = cell.photoImage;
                    imageView.setOrientation(0, true);
                    ArrayList<Object> order = PhotoViewer.this.placeProvider.getSelectedPhotosOrder();
                    if (PhotoViewer.this.placeProvider.allowGroupPhotos()) {
                        position--;
                    }
                    PhotoEntry object = PhotoViewer.this.placeProvider.getSelectedPhotos().get(order.get(position));
                    if (object instanceof PhotoEntry) {
                        PhotoEntry photoEntry = object;
                        cell.setTag(photoEntry);
                        cell.videoInfoContainer.setVisibility(4);
                        if (photoEntry.thumbPath != null) {
                            imageView.setImage(photoEntry.thumbPath, null, this.mContext.getResources().getDrawable(R.drawable.nophotos));
                        } else if (photoEntry.path != null) {
                            imageView.setOrientation(photoEntry.orientation, true);
                            if (photoEntry.isVideo) {
                                cell.videoInfoContainer.setVisibility(0);
                                int seconds = photoEntry.duration - ((photoEntry.duration / 60) * 60);
                                cell.videoTextView.setText(String.format("%d:%02d", new Object[]{Integer.valueOf(minutes), Integer.valueOf(seconds)}));
                                imageView.setImage("vthumb://" + photoEntry.imageId + ":" + photoEntry.path, null, this.mContext.getResources().getDrawable(R.drawable.nophotos));
                            } else {
                                imageView.setImage("thumb://" + photoEntry.imageId + ":" + photoEntry.path, null, this.mContext.getResources().getDrawable(R.drawable.nophotos));
                            }
                        } else {
                            imageView.setImageResource(R.drawable.nophotos);
                        }
                        cell.setChecked(-1, true, false);
                        cell.checkBox.setVisibility(0);
                        return;
                    } else if (object instanceof SearchImage) {
                        SearchImage photoEntry2 = (SearchImage) object;
                        cell.setTag(photoEntry2);
                        cell.setImage(photoEntry2);
                        cell.videoInfoContainer.setVisibility(4);
                        cell.setChecked(-1, true, false);
                        cell.checkBox.setVisibility(0);
                        return;
                    } else {
                        return;
                    }
                case 1:
                    holder.itemView.setColorFilter(SharedConfig.groupPhotosEnabled ? new PorterDuffColorFilter(-10043398, Mode.MULTIPLY) : null);
                    return;
                default:
                    return;
            }
        }

        public int getItemViewType(int i) {
            if (i == 0 && PhotoViewer.this.placeProvider.allowGroupPhotos()) {
                return 1;
            }
            return 0;
        }
    }

    public static PhotoViewer getPipInstance() {
        return PipInstance;
    }

    public static PhotoViewer getInstance() {
        PhotoViewer localInstance = Instance;
        if (localInstance == null) {
            synchronized (PhotoViewer.class) {
                try {
                    localInstance = Instance;
                    if (localInstance == null) {
                        PhotoViewer localInstance2 = new PhotoViewer();
                        try {
                            Instance = localInstance2;
                            localInstance = localInstance2;
                        } catch (Throwable th) {
                            Throwable th2 = th;
                            localInstance = localInstance2;
                            throw th2;
                        }
                    }
                } catch (Throwable th3) {
                    th2 = th3;
                    throw th2;
                }
            }
        }
        return localInstance;
    }

    public static boolean hasInstance() {
        return Instance != null;
    }

    public PhotoViewer() {
        this.blackPaint.setColor(Theme.ACTION_BAR_VIDEO_EDIT_COLOR);
    }

    public void didReceivedNotification(int id, int account, Object... args) {
        String location;
        int a;
        if (id == NotificationCenter.FileDidFailedLoad) {
            location = args[0];
            a = 0;
            while (a < 3) {
                if (this.currentFileNames[a] == null || !this.currentFileNames[a].equals(location)) {
                    a++;
                } else {
                    this.photoProgressViews[a].setProgress(1.0f, true);
                    checkProgress(a, true);
                    return;
                }
            }
        } else if (id == NotificationCenter.FileDidLoaded) {
            location = (String) args[0];
            a = 0;
            while (a < 3) {
                if (this.currentFileNames[a] == null || !this.currentFileNames[a].equals(location)) {
                    a++;
                } else {
                    this.photoProgressViews[a].setProgress(1.0f, true);
                    checkProgress(a, true);
                    if (this.videoPlayer == null && a == 0 && ((this.currentMessageObject != null && this.currentMessageObject.isVideo()) || (this.currentBotInlineResult != null && (this.currentBotInlineResult.type.equals(MimeTypes.BASE_TYPE_VIDEO) || MessageObject.isVideoDocument(this.currentBotInlineResult.document))))) {
                        onActionClick(false);
                    }
                    if (a == 0 && this.videoPlayer != null) {
                        this.currentVideoFinishedLoading = true;
                        return;
                    }
                    return;
                }
            }
        } else if (id == NotificationCenter.FileLoadProgressChanged) {
            location = (String) args[0];
            a = 0;
            while (a < 3) {
                if (this.currentFileNames[a] != null && this.currentFileNames[a].equals(location)) {
                    Float loadProgress = args[1];
                    this.photoProgressViews[a].setProgress(loadProgress.floatValue(), true);
                    if (!(a != 0 || this.videoPlayer == null || this.videoPlayerSeekbar == null)) {
                        float bufferedProgress;
                        if (this.currentVideoFinishedLoading) {
                            bufferedProgress = 1.0f;
                        } else {
                            long newTime = SystemClock.elapsedRealtime();
                            if (Math.abs(newTime - this.lastBufferedPositionCheck) >= 500) {
                                float progress;
                                if (this.seekToProgressPending == 0.0f) {
                                    long duration = this.videoPlayer.getDuration();
                                    long position = this.videoPlayer.getCurrentPosition();
                                    if (duration < 0 || duration == C.TIME_UNSET || position < 0) {
                                        progress = 0.0f;
                                    } else {
                                        progress = ((float) position) / ((float) duration);
                                    }
                                } else {
                                    progress = this.seekToProgressPending;
                                }
                                bufferedProgress = this.isStreaming ? FileLoader.getInstance(this.currentAccount).getBufferedProgressFromPosition(progress, this.currentFileNames[0]) : 1.0f;
                                this.lastBufferedPositionCheck = newTime;
                            } else {
                                bufferedProgress = -1.0f;
                            }
                        }
                        if (bufferedProgress != -1.0f) {
                            this.videoPlayerSeekbar.setBufferedProgress(bufferedProgress);
                            if (this.pipVideoView != null) {
                                this.pipVideoView.setBufferedProgress(bufferedProgress);
                            }
                            this.videoPlayerControlFrameLayout.invalidate();
                        }
                        checkBufferedProgress(loadProgress.floatValue());
                    }
                }
                a++;
            }
        } else if (id == NotificationCenter.dialogPhotosLoaded) {
            guid = ((Integer) args[3]).intValue();
            if (this.avatarsDialogId == ((Integer) args[0]).intValue() && this.classGuid == guid) {
                boolean fromCache = ((Boolean) args[2]).booleanValue();
                int setToImage = -1;
                ArrayList<Photo> photos = args[4];
                if (!photos.isEmpty()) {
                    this.imagesArrLocations.clear();
                    this.imagesArrLocationsSizes.clear();
                    this.avatarsArr.clear();
                    for (a = 0; a < photos.size(); a++) {
                        Photo photo = (Photo) photos.get(a);
                        if (!(photo == null || (photo instanceof TL_photoEmpty) || photo.sizes == null)) {
                            PhotoSize sizeFull = FileLoader.getClosestPhotoSizeWithSize(photo.sizes, 640);
                            if (sizeFull != null) {
                                if (setToImage == -1 && this.currentFileLocation != null) {
                                    for (int b = 0; b < photo.sizes.size(); b++) {
                                        PhotoSize size = (PhotoSize) photo.sizes.get(b);
                                        if (size.location.local_id == this.currentFileLocation.local_id && size.location.volume_id == this.currentFileLocation.volume_id) {
                                            setToImage = this.imagesArrLocations.size();
                                            break;
                                        }
                                    }
                                }
                                this.imagesArrLocations.add(sizeFull.location);
                                this.imagesArrLocationsSizes.add(Integer.valueOf(sizeFull.size));
                                this.avatarsArr.add(photo);
                            }
                        }
                    }
                    if (this.avatarsArr.isEmpty()) {
                        this.menuItem.hideSubItem(6);
                    } else {
                        this.menuItem.showSubItem(6);
                    }
                    this.needSearchImageInArr = false;
                    this.currentIndex = -1;
                    if (setToImage != -1) {
                        setImageIndex(setToImage, true);
                    } else {
                        this.avatarsArr.add(0, new TL_photoEmpty());
                        this.imagesArrLocations.add(0, this.currentFileLocation);
                        this.imagesArrLocationsSizes.add(0, Integer.valueOf(0));
                        setImageIndex(0, true);
                    }
                    if (fromCache) {
                        MessagesController.getInstance(this.currentAccount).loadDialogPhotos(this.avatarsDialogId, 80, 0, false, this.classGuid);
                    }
                }
            }
        } else if (id == NotificationCenter.mediaCountDidLoaded) {
            uid = ((Long) args[0]).longValue();
            if (uid == this.currentDialogId || uid == this.mergeDialogId) {
                if (uid == this.currentDialogId) {
                    this.totalImagesCount = ((Integer) args[1]).intValue();
                } else if (uid == this.mergeDialogId) {
                    this.totalImagesCountMerge = ((Integer) args[1]).intValue();
                }
                if (this.needSearchImageInArr && this.isFirstLoading) {
                    this.isFirstLoading = false;
                    this.loadingMoreImages = true;
                    DataQuery.getInstance(this.currentAccount).loadMedia(this.currentDialogId, 80, 0, 0, true, this.classGuid);
                } else if (!this.imagesArr.isEmpty()) {
                    if (this.opennedFromMedia) {
                        this.actionBar.setTitle(LocaleController.formatString("Of", R.string.Of, Integer.valueOf(this.currentIndex + 1), Integer.valueOf(this.totalImagesCount + this.totalImagesCountMerge)));
                    } else {
                        this.actionBar.setTitle(LocaleController.formatString("Of", R.string.Of, Integer.valueOf((((this.totalImagesCount + this.totalImagesCountMerge) - this.imagesArr.size()) + this.currentIndex) + 1), Integer.valueOf(this.totalImagesCount + this.totalImagesCountMerge)));
                    }
                }
            }
        } else if (id == NotificationCenter.mediaDidLoaded) {
            uid = ((Long) args[0]).longValue();
            guid = ((Integer) args[3]).intValue();
            if ((uid == this.currentDialogId || uid == this.mergeDialogId) && guid == this.classGuid) {
                this.loadingMoreImages = false;
                int loadIndex = uid == this.currentDialogId ? 0 : 1;
                ArrayList<MessageObject> arr = args[2];
                this.endReached[loadIndex] = ((Boolean) args[5]).booleanValue();
                int added;
                MessageObject message;
                if (!this.needSearchImageInArr) {
                    added = 0;
                    Iterator it = arr.iterator();
                    while (it.hasNext()) {
                        message = (MessageObject) it.next();
                        if (this.imagesByIds[loadIndex].indexOfKey(message.getId()) < 0) {
                            added++;
                            if (this.opennedFromMedia) {
                                this.imagesArr.add(message);
                            } else {
                                this.imagesArr.add(0, message);
                            }
                            this.imagesByIds[loadIndex].put(message.getId(), message);
                        }
                    }
                    if (this.opennedFromMedia) {
                        if (added == 0) {
                            this.totalImagesCount = this.imagesArr.size();
                            this.totalImagesCountMerge = 0;
                        }
                    } else if (added != 0) {
                        int index = this.currentIndex;
                        this.currentIndex = -1;
                        setImageIndex(index + added, true);
                    } else {
                        this.totalImagesCount = this.imagesArr.size();
                        this.totalImagesCountMerge = 0;
                    }
                } else if (!arr.isEmpty() || (loadIndex == 0 && this.mergeDialogId != 0)) {
                    int foundIndex = -1;
                    MessageObject currentMessage = (MessageObject) this.imagesArr.get(this.currentIndex);
                    added = 0;
                    for (a = 0; a < arr.size(); a++) {
                        message = (MessageObject) arr.get(a);
                        if (this.imagesByIdsTemp[loadIndex].indexOfKey(message.getId()) < 0) {
                            this.imagesByIdsTemp[loadIndex].put(message.getId(), message);
                            if (this.opennedFromMedia) {
                                this.imagesArrTemp.add(message);
                                if (message.getId() == currentMessage.getId()) {
                                    foundIndex = added;
                                }
                                added++;
                            } else {
                                added++;
                                this.imagesArrTemp.add(0, message);
                                if (message.getId() == currentMessage.getId()) {
                                    foundIndex = arr.size() - added;
                                }
                            }
                        }
                    }
                    if (added == 0 && (loadIndex != 0 || this.mergeDialogId == 0)) {
                        this.totalImagesCount = this.imagesArr.size();
                        this.totalImagesCountMerge = 0;
                    }
                    if (foundIndex != -1) {
                        this.imagesArr.clear();
                        this.imagesArr.addAll(this.imagesArrTemp);
                        for (a = 0; a < 2; a++) {
                            this.imagesByIds[a] = this.imagesByIdsTemp[a].clone();
                            this.imagesByIdsTemp[a].clear();
                        }
                        this.imagesArrTemp.clear();
                        this.needSearchImageInArr = false;
                        this.currentIndex = -1;
                        if (foundIndex >= this.imagesArr.size()) {
                            foundIndex = this.imagesArr.size() - 1;
                        }
                        setImageIndex(foundIndex, true);
                        return;
                    }
                    int loadFromMaxId;
                    if (this.opennedFromMedia) {
                        loadFromMaxId = this.imagesArrTemp.isEmpty() ? 0 : ((MessageObject) this.imagesArrTemp.get(this.imagesArrTemp.size() - 1)).getId();
                        if (loadIndex == 0 && this.endReached[loadIndex] && this.mergeDialogId != 0) {
                            loadIndex = 1;
                            if (!(this.imagesArrTemp.isEmpty() || ((MessageObject) this.imagesArrTemp.get(this.imagesArrTemp.size() - 1)).getDialogId() == this.mergeDialogId)) {
                                loadFromMaxId = 0;
                            }
                        }
                    } else {
                        if (this.imagesArrTemp.isEmpty()) {
                            loadFromMaxId = 0;
                        } else {
                            loadFromMaxId = ((MessageObject) this.imagesArrTemp.get(0)).getId();
                        }
                        if (loadIndex == 0 && this.endReached[loadIndex] && this.mergeDialogId != 0) {
                            loadIndex = 1;
                            if (!(this.imagesArrTemp.isEmpty() || ((MessageObject) this.imagesArrTemp.get(0)).getDialogId() == this.mergeDialogId)) {
                                loadFromMaxId = 0;
                            }
                        }
                    }
                    if (!this.endReached[loadIndex]) {
                        this.loadingMoreImages = true;
                        if (this.opennedFromMedia) {
                            long j;
                            DataQuery instance = DataQuery.getInstance(this.currentAccount);
                            if (loadIndex == 0) {
                                j = this.currentDialogId;
                            } else {
                                j = this.mergeDialogId;
                            }
                            instance.loadMedia(j, 80, loadFromMaxId, 0, true, this.classGuid);
                            return;
                        }
                        DataQuery.getInstance(this.currentAccount).loadMedia(loadIndex == 0 ? this.currentDialogId : this.mergeDialogId, 80, loadFromMaxId, 0, true, this.classGuid);
                    }
                } else {
                    this.needSearchImageInArr = false;
                }
            }
        } else if (id == NotificationCenter.emojiDidLoaded) {
            if (this.captionTextView != null) {
                this.captionTextView.invalidate();
            }
        } else if (id == NotificationCenter.FilePreparingFailed) {
            MessageObject messageObject = args[0];
            if (this.loadInitialVideo) {
                this.loadInitialVideo = false;
                this.progressView.setVisibility(4);
                preparePlayer(this.currentPlayingVideoFile, false, false);
            } else if (this.tryStartRequestPreviewOnFinish) {
                releasePlayer();
                this.tryStartRequestPreviewOnFinish = !MediaController.getInstance().scheduleVideoConvert(this.videoPreviewMessageObject, true);
            } else if (messageObject == this.videoPreviewMessageObject) {
                this.requestingPreview = false;
                this.progressView.setVisibility(4);
            }
        } else if (id == NotificationCenter.FileNewChunkAvailable && ((MessageObject) args[0]) == this.videoPreviewMessageObject) {
            String finalPath = args[1];
            if (((Long) args[3]).longValue() != 0) {
                this.requestingPreview = false;
                this.progressView.setVisibility(4);
                preparePlayer(Uri.fromFile(new File(finalPath)), false, true);
            }
        }
    }

    private void showDownloadAlert() {
        boolean alreadyDownloading = false;
        Builder builder = new Builder(this.parentActivity);
        builder.setTitle(LocaleController.getString("AppName", R.string.AppName));
        builder.setPositiveButton(LocaleController.getString("OK", R.string.OK), null);
        if (this.currentMessageObject != null && this.currentMessageObject.isVideo() && FileLoader.getInstance(this.currentMessageObject.currentAccount).isLoadingFile(this.currentFileNames[0])) {
            alreadyDownloading = true;
        }
        if (alreadyDownloading) {
            builder.setMessage(LocaleController.getString("PleaseStreamDownload", R.string.PleaseStreamDownload));
        } else {
            builder.setMessage(LocaleController.getString("PleaseDownload", R.string.PleaseDownload));
        }
        showAlertDialog(builder);
    }

    private void onSharePressed() {
        Throwable e;
        boolean z = true;
        if (this.parentActivity != null && this.allowShare) {
            File f = null;
            boolean isVideo = false;
            try {
                if (this.currentMessageObject != null) {
                    isVideo = this.currentMessageObject.isVideo();
                    if (!TextUtils.isEmpty(this.currentMessageObject.messageOwner.attachPath)) {
                        File f2 = new File(this.currentMessageObject.messageOwner.attachPath);
                        try {
                            if (f2.exists()) {
                                f = f2;
                            } else {
                                f = null;
                            }
                        } catch (Exception e2) {
                            e = e2;
                            f = f2;
                            FileLog.e(e);
                        }
                    }
                    if (f == null) {
                        f = FileLoader.getPathToMessage(this.currentMessageObject.messageOwner);
                    }
                } else if (this.currentFileLocation != null) {
                    TLObject tLObject = this.currentFileLocation;
                    if (this.avatarsDialogId == 0 && !this.isEvent) {
                        z = false;
                    }
                    f = FileLoader.getPathToAttach(tLObject, z);
                }
                if (f.exists()) {
                    Intent intent = new Intent("android.intent.action.SEND");
                    if (isVideo) {
                        intent.setType(MimeTypes.VIDEO_MP4);
                    } else if (this.currentMessageObject != null) {
                        intent.setType(this.currentMessageObject.getMimeType());
                    } else {
                        intent.setType("image/jpeg");
                    }
                    if (VERSION.SDK_INT >= 24) {
                        try {
                            intent.putExtra("android.intent.extra.STREAM", FileProvider.getUriForFile(this.parentActivity, "org.telegram.messenger.beta.provider", f));
                            intent.setFlags(1);
                        } catch (Exception e3) {
                            intent.putExtra("android.intent.extra.STREAM", Uri.fromFile(f));
                        }
                    } else {
                        intent.putExtra("android.intent.extra.STREAM", Uri.fromFile(f));
                    }
                    this.parentActivity.startActivityForResult(Intent.createChooser(intent, LocaleController.getString("ShareFile", R.string.ShareFile)), 500);
                    return;
                }
                showDownloadAlert();
            } catch (Exception e4) {
                e = e4;
                FileLog.e(e);
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

    public void setParentAlert(ChatAttachAlert alert) {
        this.parentAlert = alert;
    }

    public void setParentActivity(Activity activity) {
        this.currentAccount = UserConfig.selectedAccount;
        this.centerImage.setCurrentAccount(this.currentAccount);
        this.leftImage.setCurrentAccount(this.currentAccount);
        this.rightImage.setCurrentAccount(this.currentAccount);
        if (this.parentActivity != activity) {
            FrameLayout.LayoutParams layoutParams;
            this.parentActivity = activity;
            this.actvityContext = new ContextThemeWrapper(this.parentActivity, R.style.Theme.TMessages);
            if (progressDrawables == null) {
                progressDrawables = new Drawable[4];
                progressDrawables[0] = this.parentActivity.getResources().getDrawable(R.drawable.circle_big);
                progressDrawables[1] = this.parentActivity.getResources().getDrawable(R.drawable.cancel_big);
                progressDrawables[2] = this.parentActivity.getResources().getDrawable(R.drawable.load_big);
                progressDrawables[3] = this.parentActivity.getResources().getDrawable(R.drawable.play_big);
            }
            this.scroller = new Scroller(activity);
            this.windowView = new FrameLayout(activity) {
                private Runnable attachRunnable;

                public boolean onInterceptTouchEvent(MotionEvent ev) {
                    return PhotoViewer.this.isVisible && super.onInterceptTouchEvent(ev);
                }

                public boolean onTouchEvent(MotionEvent event) {
                    return PhotoViewer.this.isVisible && PhotoViewer.this.onTouchEvent(event);
                }

                protected boolean drawChild(Canvas canvas, View child, long drawingTime) {
                    boolean result = super.drawChild(canvas, child, drawingTime);
                    if (VERSION.SDK_INT >= 21 && child == PhotoViewer.this.animatingImageView && PhotoViewer.this.lastInsets != null) {
                        canvas.drawRect(0.0f, (float) getMeasuredHeight(), (float) getMeasuredWidth(), (float) (getMeasuredHeight() + ((WindowInsets) PhotoViewer.this.lastInsets).getSystemWindowInsetBottom()), PhotoViewer.this.blackPaint);
                    }
                    return result;
                }

                protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
                    int widthSize = MeasureSpec.getSize(widthMeasureSpec);
                    int heightSize = MeasureSpec.getSize(heightMeasureSpec);
                    if (VERSION.SDK_INT >= 21 && PhotoViewer.this.lastInsets != null) {
                        WindowInsets insets = (WindowInsets) PhotoViewer.this.lastInsets;
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
                    if (VERSION.SDK_INT >= 21 && PhotoViewer.this.lastInsets != null) {
                        widthSize -= ((WindowInsets) PhotoViewer.this.lastInsets).getSystemWindowInsetLeft();
                    }
                    ViewGroup.LayoutParams layoutParams = PhotoViewer.this.animatingImageView.getLayoutParams();
                    PhotoViewer.this.animatingImageView.measure(MeasureSpec.makeMeasureSpec(layoutParams.width, Integer.MIN_VALUE), MeasureSpec.makeMeasureSpec(layoutParams.height, Integer.MIN_VALUE));
                    PhotoViewer.this.containerView.measure(MeasureSpec.makeMeasureSpec(widthSize, 1073741824), MeasureSpec.makeMeasureSpec(heightSize, 1073741824));
                }

                protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
                    int x = 0;
                    if (VERSION.SDK_INT >= 21 && PhotoViewer.this.lastInsets != null) {
                        x = 0 + ((WindowInsets) PhotoViewer.this.lastInsets).getSystemWindowInsetLeft();
                    }
                    PhotoViewer.this.animatingImageView.layout(x, 0, PhotoViewer.this.animatingImageView.getMeasuredWidth() + x, PhotoViewer.this.animatingImageView.getMeasuredHeight());
                    PhotoViewer.this.containerView.layout(x, 0, PhotoViewer.this.containerView.getMeasuredWidth() + x, PhotoViewer.this.containerView.getMeasuredHeight());
                    PhotoViewer.this.wasLayout = true;
                    if (changed) {
                        if (!PhotoViewer.this.dontResetZoomOnFirstLayout) {
                            PhotoViewer.this.scale = 1.0f;
                            PhotoViewer.this.translationX = 0.0f;
                            PhotoViewer.this.translationY = 0.0f;
                            PhotoViewer.this.updateMinMax(PhotoViewer.this.scale);
                        }
                        if (PhotoViewer.this.checkImageView != null) {
                            PhotoViewer.this.checkImageView.post(new Runnable() {
                                public void run() {
                                    int i = 0;
                                    FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) PhotoViewer.this.checkImageView.getLayoutParams();
                                    int rotation = ((WindowManager) ApplicationLoader.applicationContext.getSystemService("window")).getDefaultDisplay().getRotation();
                                    layoutParams.topMargin = (VERSION.SDK_INT >= 21 ? AndroidUtilities.statusBarHeight : 0) + ((ActionBar.getCurrentActionBarHeight() - AndroidUtilities.dp(40.0f)) / 2);
                                    PhotoViewer.this.checkImageView.setLayoutParams(layoutParams);
                                    layoutParams = (FrameLayout.LayoutParams) PhotoViewer.this.photosCounterView.getLayoutParams();
                                    int currentActionBarHeight = (ActionBar.getCurrentActionBarHeight() - AndroidUtilities.dp(40.0f)) / 2;
                                    if (VERSION.SDK_INT >= 21) {
                                        i = AndroidUtilities.statusBarHeight;
                                    }
                                    layoutParams.topMargin = currentActionBarHeight + i;
                                    PhotoViewer.this.photosCounterView.setLayoutParams(layoutParams);
                                }
                            });
                        }
                    }
                    if (PhotoViewer.this.dontResetZoomOnFirstLayout) {
                        PhotoViewer.this.setScaleToFill();
                        PhotoViewer.this.dontResetZoomOnFirstLayout = false;
                    }
                }

                protected void onAttachedToWindow() {
                    super.onAttachedToWindow();
                    PhotoViewer.this.attachedToWindow = true;
                }

                protected void onDetachedFromWindow() {
                    super.onDetachedFromWindow();
                    PhotoViewer.this.attachedToWindow = false;
                    PhotoViewer.this.wasLayout = false;
                }

                public boolean dispatchKeyEventPreIme(KeyEvent event) {
                    if (event == null || event.getKeyCode() != 4 || event.getAction() != 1) {
                        return super.dispatchKeyEventPreIme(event);
                    }
                    if (PhotoViewer.this.captionEditText.isPopupShowing() || PhotoViewer.this.captionEditText.isKeyboardVisible()) {
                        PhotoViewer.this.closeCaptionEnter(false);
                        return false;
                    }
                    PhotoViewer.getInstance().closePhoto(true, false);
                    return true;
                }

                public ActionMode startActionModeForChild(View originalView, Callback callback, int type) {
                    if (VERSION.SDK_INT >= 23) {
                        View view = PhotoViewer.this.parentActivity.findViewById(16908290);
                        if (view instanceof ViewGroup) {
                            try {
                                return ((ViewGroup) view).startActionModeForChild(originalView, callback, type);
                            } catch (Throwable e) {
                                FileLog.e(e);
                            }
                        }
                    }
                    return super.startActionModeForChild(originalView, callback, type);
                }
            };
            this.windowView.setBackgroundDrawable(this.backgroundDrawable);
            this.windowView.setClipChildren(true);
            this.windowView.setFocusable(false);
            this.animatingImageView = new ClippingImageView(activity);
            this.animatingImageView.setAnimationValues(this.animationValues);
            this.windowView.addView(this.animatingImageView, LayoutHelper.createFrame(40, 40.0f));
            this.containerView = new FrameLayoutDrawer(activity);
            this.containerView.setFocusable(false);
            this.windowView.addView(this.containerView, LayoutHelper.createFrame(-1, -1, 51));
            if (VERSION.SDK_INT >= 21) {
                this.containerView.setFitsSystemWindows(true);
                this.containerView.setOnApplyWindowInsetsListener(new OnApplyWindowInsetsListener() {
                    @SuppressLint({"NewApi"})
                    public WindowInsets onApplyWindowInsets(View v, WindowInsets insets) {
                        WindowInsets oldInsets = (WindowInsets) PhotoViewer.this.lastInsets;
                        PhotoViewer.this.lastInsets = insets;
                        if (oldInsets == null || !oldInsets.toString().equals(insets.toString())) {
                            if (PhotoViewer.this.animationInProgress == 1) {
                                PhotoViewer.this.animatingImageView.setTranslationX(PhotoViewer.this.animatingImageView.getTranslationX() - ((float) PhotoViewer.this.getLeftInset()));
                                PhotoViewer.this.animationValues[0][2] = PhotoViewer.this.animatingImageView.getTranslationX();
                            }
                            PhotoViewer.this.windowView.requestLayout();
                        }
                        return insets.consumeSystemWindowInsets();
                    }
                });
                this.containerView.setSystemUiVisibility(1280);
            }
            this.windowLayoutParams = new LayoutParams();
            this.windowLayoutParams.height = -1;
            this.windowLayoutParams.format = -3;
            this.windowLayoutParams.width = -1;
            this.windowLayoutParams.gravity = 51;
            this.windowLayoutParams.type = 99;
            if (VERSION.SDK_INT >= 21) {
                this.windowLayoutParams.flags = -2147417848;
            } else {
                this.windowLayoutParams.flags = 8;
            }
            this.actionBar = new ActionBar(activity) {
                public void setAlpha(float alpha) {
                    super.setAlpha(alpha);
                    PhotoViewer.this.containerView.invalidate();
                }
            };
            this.actionBar.setTitleColor(-1);
            this.actionBar.setSubtitleColor(-1);
            this.actionBar.setBackgroundColor(Theme.ACTION_BAR_PHOTO_VIEWER_COLOR);
            this.actionBar.setOccupyStatusBar(VERSION.SDK_INT >= 21);
            this.actionBar.setItemsBackgroundColor(Theme.ACTION_BAR_WHITE_SELECTOR_COLOR, false);
            this.actionBar.setBackButtonImage(R.drawable.ic_ab_back);
            this.actionBar.setTitle(LocaleController.formatString("Of", R.string.Of, Integer.valueOf(1), Integer.valueOf(1)));
            this.containerView.addView(this.actionBar, LayoutHelper.createFrame(-1, -2.0f));
            this.actionBar.setActionBarMenuOnItemClick(new ActionBarMenuOnItemClick() {
                public void onItemClick(int id) {
                    if (id == -1) {
                        if (PhotoViewer.this.needCaptionLayout && (PhotoViewer.this.captionEditText.isPopupShowing() || PhotoViewer.this.captionEditText.isKeyboardVisible())) {
                            PhotoViewer.this.closeCaptionEnter(false);
                        } else {
                            PhotoViewer.this.closePhoto(true, false);
                        }
                    } else if (id == 1) {
                        if (VERSION.SDK_INT < 23 || PhotoViewer.this.parentActivity.checkSelfPermission("android.permission.WRITE_EXTERNAL_STORAGE") == 0) {
                            File f = null;
                            if (PhotoViewer.this.currentMessageObject != null) {
                                if ((PhotoViewer.this.currentMessageObject.messageOwner.media instanceof TL_messageMediaWebPage) && PhotoViewer.this.currentMessageObject.messageOwner.media.webpage != null && PhotoViewer.this.currentMessageObject.messageOwner.media.webpage.document == null) {
                                    f = FileLoader.getPathToAttach(PhotoViewer.this.getFileLocation(PhotoViewer.this.currentIndex, null), true);
                                } else {
                                    f = FileLoader.getPathToMessage(PhotoViewer.this.currentMessageObject.messageOwner);
                                }
                            } else if (PhotoViewer.this.currentFileLocation != null) {
                                TLObject access$8300 = PhotoViewer.this.currentFileLocation;
                                boolean z = PhotoViewer.this.avatarsDialogId != 0 || PhotoViewer.this.isEvent;
                                f = FileLoader.getPathToAttach(access$8300, z);
                            }
                            if (f == null || !f.exists()) {
                                PhotoViewer.this.showDownloadAlert();
                                return;
                            }
                            String file = f.toString();
                            Context access$2500 = PhotoViewer.this.parentActivity;
                            if (PhotoViewer.this.currentMessageObject == null || !PhotoViewer.this.currentMessageObject.isVideo()) {
                                r2 = 0;
                            } else {
                                r2 = 1;
                            }
                            MediaController.saveFile(file, access$2500, r2, null, null);
                            return;
                        }
                        PhotoViewer.this.parentActivity.requestPermissions(new String[]{"android.permission.WRITE_EXTERNAL_STORAGE"}, 4);
                    } else if (id == 2) {
                        if (PhotoViewer.this.currentDialogId != 0) {
                            PhotoViewer.this.disableShowCheck = true;
                            Bundle args2 = new Bundle();
                            args2.putLong("dialog_id", PhotoViewer.this.currentDialogId);
                            r0 = new MediaActivity(args2, new int[]{-1, -1, -1, -1, -1});
                            if (PhotoViewer.this.parentChatActivity != null) {
                                r0.setChatInfo(PhotoViewer.this.parentChatActivity.getCurrentChatInfo());
                            }
                            PhotoViewer.this.closePhoto(false, false);
                            ((LaunchActivity) PhotoViewer.this.parentActivity).presentFragment(r0, false, true);
                        }
                    } else if (id == 4) {
                        if (PhotoViewer.this.currentMessageObject != null) {
                            args = new Bundle();
                            int lower_part = (int) PhotoViewer.this.currentDialogId;
                            int high_id = (int) (PhotoViewer.this.currentDialogId >> 32);
                            if (lower_part == 0) {
                                args.putInt("enc_id", high_id);
                            } else if (high_id == 1) {
                                args.putInt("chat_id", lower_part);
                            } else if (lower_part > 0) {
                                args.putInt("user_id", lower_part);
                            } else if (lower_part < 0) {
                                Chat chat = MessagesController.getInstance(PhotoViewer.this.currentAccount).getChat(Integer.valueOf(-lower_part));
                                if (!(chat == null || chat.migrated_to == null)) {
                                    args.putInt("migrated_to", lower_part);
                                    lower_part = -chat.migrated_to.channel_id;
                                }
                                args.putInt("chat_id", -lower_part);
                            }
                            args.putInt("message_id", PhotoViewer.this.currentMessageObject.getId());
                            NotificationCenter.getInstance(PhotoViewer.this.currentAccount).postNotificationName(NotificationCenter.closeChats, new Object[0]);
                            LaunchActivity launchActivity = (LaunchActivity) PhotoViewer.this.parentActivity;
                            boolean remove = launchActivity.getMainFragmentsCount() > 1 || AndroidUtilities.isTablet();
                            launchActivity.presentFragment(new ChatActivity(args), remove, true);
                            PhotoViewer.this.currentMessageObject = null;
                            PhotoViewer.this.closePhoto(false, false);
                        }
                    } else if (id == 3) {
                        if (PhotoViewer.this.currentMessageObject != null && PhotoViewer.this.parentActivity != null) {
                            ((LaunchActivity) PhotoViewer.this.parentActivity).switchToAccount(PhotoViewer.this.currentMessageObject.currentAccount, true);
                            args = new Bundle();
                            args.putBoolean("onlySelect", true);
                            args.putInt("dialogsType", 3);
                            r0 = new DialogsActivity(args);
                            ArrayList<MessageObject> fmessages = new ArrayList();
                            fmessages.add(PhotoViewer.this.currentMessageObject);
                            final ArrayList<MessageObject> arrayList = fmessages;
                            r0.setDelegate(new DialogsActivityDelegate() {
                                public void didSelectDialogs(DialogsActivity fragment, ArrayList<Long> dids, CharSequence message, boolean param) {
                                    long did;
                                    if (dids.size() > 1 || ((Long) dids.get(0)).longValue() == ((long) UserConfig.getInstance(PhotoViewer.this.currentAccount).getClientUserId()) || message != null) {
                                        for (int a = 0; a < dids.size(); a++) {
                                            did = ((Long) dids.get(a)).longValue();
                                            if (message != null) {
                                                SendMessagesHelper.getInstance(PhotoViewer.this.currentAccount).sendMessage(message.toString(), did, null, null, true, null, null, null);
                                            }
                                            SendMessagesHelper.getInstance(PhotoViewer.this.currentAccount).sendMessage(arrayList, did);
                                        }
                                        fragment.finishFragment();
                                        return;
                                    }
                                    did = ((Long) dids.get(0)).longValue();
                                    int lower_part = (int) did;
                                    int high_part = (int) (did >> 32);
                                    Bundle args = new Bundle();
                                    args.putBoolean("scrollToTopOnResume", true);
                                    if (lower_part == 0) {
                                        args.putInt("enc_id", high_part);
                                    } else if (lower_part > 0) {
                                        args.putInt("user_id", lower_part);
                                    } else if (lower_part < 0) {
                                        args.putInt("chat_id", -lower_part);
                                    }
                                    NotificationCenter.getInstance(PhotoViewer.this.currentAccount).postNotificationName(NotificationCenter.closeChats, new Object[0]);
                                    ChatActivity chatActivity = new ChatActivity(args);
                                    if (((LaunchActivity) PhotoViewer.this.parentActivity).presentFragment(chatActivity, true, false)) {
                                        chatActivity.showFieldPanelForForward(true, arrayList);
                                    } else {
                                        fragment.finishFragment();
                                    }
                                }
                            });
                            ((LaunchActivity) PhotoViewer.this.parentActivity).presentFragment(r0, false, true);
                            PhotoViewer.this.closePhoto(false, false);
                        }
                    } else if (id == 6) {
                        if (PhotoViewer.this.parentActivity != null && PhotoViewer.this.placeProvider != null) {
                            final boolean[] zArr;
                            Builder builder = new Builder(PhotoViewer.this.parentActivity);
                            String text = PhotoViewer.this.placeProvider.getDeleteMessageString();
                            if (text != null) {
                                builder.setMessage(text);
                            } else if (PhotoViewer.this.currentMessageObject != null && PhotoViewer.this.currentMessageObject.isVideo()) {
                                builder.setMessage(LocaleController.formatString("AreYouSureDeleteVideo", R.string.AreYouSureDeleteVideo, new Object[0]));
                            } else if (PhotoViewer.this.currentMessageObject == null || !PhotoViewer.this.currentMessageObject.isGif()) {
                                builder.setMessage(LocaleController.formatString("AreYouSureDeletePhoto", R.string.AreYouSureDeletePhoto, new Object[0]));
                            } else {
                                builder.setMessage(LocaleController.formatString("AreYouSure", R.string.AreYouSure, new Object[0]));
                            }
                            builder.setTitle(LocaleController.getString("AppName", R.string.AppName));
                            boolean[] deleteForAll = new boolean[1];
                            if (PhotoViewer.this.currentMessageObject != null) {
                                int lower_id = (int) PhotoViewer.this.currentMessageObject.getDialogId();
                                if (lower_id != 0) {
                                    User currentUser;
                                    Chat currentChat;
                                    if (lower_id > 0) {
                                        currentUser = MessagesController.getInstance(PhotoViewer.this.currentAccount).getUser(Integer.valueOf(lower_id));
                                        currentChat = null;
                                    } else {
                                        currentUser = null;
                                        currentChat = MessagesController.getInstance(PhotoViewer.this.currentAccount).getChat(Integer.valueOf(-lower_id));
                                    }
                                    if (!(currentUser == null && ChatObject.isChannel(currentChat))) {
                                        int currentDate = ConnectionsManager.getInstance(PhotoViewer.this.currentAccount).getCurrentTime();
                                        if (!((currentUser == null || currentUser.id == UserConfig.getInstance(PhotoViewer.this.currentAccount).getClientUserId()) && currentChat == null) && ((PhotoViewer.this.currentMessageObject.messageOwner.action == null || (PhotoViewer.this.currentMessageObject.messageOwner.action instanceof TL_messageActionEmpty)) && PhotoViewer.this.currentMessageObject.isOut() && currentDate - PhotoViewer.this.currentMessageObject.messageOwner.date <= 172800)) {
                                            int dp;
                                            View frameLayout = new FrameLayout(PhotoViewer.this.parentActivity);
                                            CheckBoxCell cell = new CheckBoxCell(PhotoViewer.this.parentActivity, 1);
                                            cell.setBackgroundDrawable(Theme.getSelectorDrawable(false));
                                            if (currentChat != null) {
                                                cell.setText(LocaleController.getString("DeleteForAll", R.string.DeleteForAll), TtmlNode.ANONYMOUS_REGION_ID, false, false);
                                            } else {
                                                cell.setText(LocaleController.formatString("DeleteForUser", R.string.DeleteForUser, UserObject.getFirstName(currentUser)), TtmlNode.ANONYMOUS_REGION_ID, false, false);
                                            }
                                            if (LocaleController.isRTL) {
                                                r2 = AndroidUtilities.dp(16.0f);
                                            } else {
                                                r2 = AndroidUtilities.dp(8.0f);
                                            }
                                            if (LocaleController.isRTL) {
                                                dp = AndroidUtilities.dp(8.0f);
                                            } else {
                                                dp = AndroidUtilities.dp(16.0f);
                                            }
                                            cell.setPadding(r2, 0, dp, 0);
                                            frameLayout.addView(cell, LayoutHelper.createFrame(-1, 48.0f, 51, 0.0f, 0.0f, 0.0f, 0.0f));
                                            zArr = deleteForAll;
                                            cell.setOnClickListener(new OnClickListener() {
                                                public void onClick(View v) {
                                                    boolean z;
                                                    CheckBoxCell cell = (CheckBoxCell) v;
                                                    boolean[] zArr = zArr;
                                                    if (zArr[0]) {
                                                        z = false;
                                                    } else {
                                                        z = true;
                                                    }
                                                    zArr[0] = z;
                                                    cell.setChecked(zArr[0], true);
                                                }
                                            });
                                            builder.setView(frameLayout);
                                        }
                                    }
                                }
                            }
                            zArr = deleteForAll;
                            builder.setPositiveButton(LocaleController.getString("OK", R.string.OK), new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    if (PhotoViewer.this.imagesArr.isEmpty()) {
                                        int index;
                                        if (PhotoViewer.this.avatarsArr.isEmpty()) {
                                            if (!PhotoViewer.this.secureDocuments.isEmpty() && PhotoViewer.this.placeProvider != null) {
                                                PhotoViewer.this.secureDocuments.remove(PhotoViewer.this.currentIndex);
                                                PhotoViewer.this.placeProvider.deleteImageAtIndex(PhotoViewer.this.currentIndex);
                                                if (PhotoViewer.this.secureDocuments.isEmpty()) {
                                                    PhotoViewer.this.closePhoto(false, false);
                                                    return;
                                                }
                                                index = PhotoViewer.this.currentIndex;
                                                if (index >= PhotoViewer.this.secureDocuments.size()) {
                                                    index = PhotoViewer.this.secureDocuments.size() - 1;
                                                }
                                                PhotoViewer.this.currentIndex = -1;
                                                PhotoViewer.this.setImageIndex(index, true);
                                            }
                                        } else if (PhotoViewer.this.currentIndex >= 0 && PhotoViewer.this.currentIndex < PhotoViewer.this.avatarsArr.size()) {
                                            Photo photo = (Photo) PhotoViewer.this.avatarsArr.get(PhotoViewer.this.currentIndex);
                                            FileLocation currentLocation = (FileLocation) PhotoViewer.this.imagesArrLocations.get(PhotoViewer.this.currentIndex);
                                            if (photo instanceof TL_photoEmpty) {
                                                photo = null;
                                            }
                                            boolean current = false;
                                            if (PhotoViewer.this.currentUserAvatarLocation != null) {
                                                if (photo != null) {
                                                    Iterator it = photo.sizes.iterator();
                                                    while (it.hasNext()) {
                                                        PhotoSize size = (PhotoSize) it.next();
                                                        if (size.location.local_id == PhotoViewer.this.currentUserAvatarLocation.local_id && size.location.volume_id == PhotoViewer.this.currentUserAvatarLocation.volume_id) {
                                                            current = true;
                                                            break;
                                                        }
                                                    }
                                                } else if (currentLocation.local_id == PhotoViewer.this.currentUserAvatarLocation.local_id && currentLocation.volume_id == PhotoViewer.this.currentUserAvatarLocation.volume_id) {
                                                    current = true;
                                                }
                                            }
                                            if (current) {
                                                MessagesController.getInstance(PhotoViewer.this.currentAccount).deleteUserPhoto(null);
                                                PhotoViewer.this.closePhoto(false, false);
                                            } else if (photo != null) {
                                                TL_inputPhoto inputPhoto = new TL_inputPhoto();
                                                inputPhoto.id = photo.id;
                                                inputPhoto.access_hash = photo.access_hash;
                                                MessagesController.getInstance(PhotoViewer.this.currentAccount).deleteUserPhoto(inputPhoto);
                                                MessagesStorage.getInstance(PhotoViewer.this.currentAccount).clearUserPhoto(PhotoViewer.this.avatarsDialogId, photo.id);
                                                PhotoViewer.this.imagesArrLocations.remove(PhotoViewer.this.currentIndex);
                                                PhotoViewer.this.imagesArrLocationsSizes.remove(PhotoViewer.this.currentIndex);
                                                PhotoViewer.this.avatarsArr.remove(PhotoViewer.this.currentIndex);
                                                if (PhotoViewer.this.imagesArrLocations.isEmpty()) {
                                                    PhotoViewer.this.closePhoto(false, false);
                                                    return;
                                                }
                                                index = PhotoViewer.this.currentIndex;
                                                if (index >= PhotoViewer.this.avatarsArr.size()) {
                                                    index = PhotoViewer.this.avatarsArr.size() - 1;
                                                }
                                                PhotoViewer.this.currentIndex = -1;
                                                PhotoViewer.this.setImageIndex(index, true);
                                            }
                                        }
                                    } else if (PhotoViewer.this.currentIndex >= 0 && PhotoViewer.this.currentIndex < PhotoViewer.this.imagesArr.size()) {
                                        MessageObject obj = (MessageObject) PhotoViewer.this.imagesArr.get(PhotoViewer.this.currentIndex);
                                        if (obj.isSent()) {
                                            PhotoViewer.this.closePhoto(false, false);
                                            ArrayList<Integer> arr = new ArrayList();
                                            if (PhotoViewer.this.slideshowMessageId != 0) {
                                                arr.add(Integer.valueOf(PhotoViewer.this.slideshowMessageId));
                                            } else {
                                                arr.add(Integer.valueOf(obj.getId()));
                                            }
                                            ArrayList<Long> random_ids = null;
                                            EncryptedChat encryptedChat = null;
                                            if (((int) obj.getDialogId()) == 0 && obj.messageOwner.random_id != 0) {
                                                random_ids = new ArrayList();
                                                random_ids.add(Long.valueOf(obj.messageOwner.random_id));
                                                encryptedChat = MessagesController.getInstance(PhotoViewer.this.currentAccount).getEncryptedChat(Integer.valueOf((int) (obj.getDialogId() >> 32)));
                                            }
                                            MessagesController.getInstance(PhotoViewer.this.currentAccount).deleteMessages(arr, random_ids, encryptedChat, obj.messageOwner.to_id.channel_id, zArr[0]);
                                        }
                                    }
                                }
                            });
                            builder.setNegativeButton(LocaleController.getString("Cancel", R.string.Cancel), null);
                            PhotoViewer.this.showAlertDialog(builder);
                        }
                    } else if (id == 10) {
                        PhotoViewer.this.onSharePressed();
                    } else if (id == 11) {
                        try {
                            AndroidUtilities.openForView(PhotoViewer.this.currentMessageObject, PhotoViewer.this.parentActivity);
                            PhotoViewer.this.closePhoto(false, false);
                        } catch (Throwable e) {
                            FileLog.e(e);
                        }
                    } else if (id == 13) {
                        if (PhotoViewer.this.parentActivity != null && PhotoViewer.this.currentMessageObject != null && PhotoViewer.this.currentMessageObject.messageOwner.media != null && PhotoViewer.this.currentMessageObject.messageOwner.media.photo != null) {
                            new StickersAlert(PhotoViewer.this.parentActivity, PhotoViewer.this.currentMessageObject.messageOwner.media.photo).show();
                        }
                    } else if (id == 5) {
                        if (PhotoViewer.this.pipItem.getAlpha() == 1.0f) {
                            PhotoViewer.this.switchToPip();
                        }
                    } else if (id == 7 && PhotoViewer.this.currentMessageObject != null) {
                        FileLoader.getInstance(PhotoViewer.this.currentAccount).cancelLoadFile(PhotoViewer.this.currentMessageObject.getDocument());
                        PhotoViewer.this.releasePlayer();
                        PhotoViewer.this.bottomLayout.setTag(Integer.valueOf(1));
                        PhotoViewer.this.bottomLayout.setVisibility(0);
                    }
                }

                public boolean canOpenMenu() {
                    boolean z = false;
                    if (PhotoViewer.this.currentMessageObject != null) {
                        return FileLoader.getPathToMessage(PhotoViewer.this.currentMessageObject.messageOwner).exists();
                    }
                    if (PhotoViewer.this.currentFileLocation == null) {
                        return false;
                    }
                    TLObject access$8300 = PhotoViewer.this.currentFileLocation;
                    if (PhotoViewer.this.avatarsDialogId != 0 || PhotoViewer.this.isEvent) {
                        z = true;
                    }
                    return FileLoader.getPathToAttach(access$8300, z).exists();
                }
            });
            ActionBarMenu menu = this.actionBar.createMenu();
            this.masksItem = menu.addItem(13, (int) R.drawable.ic_masks_msk1);
            this.pipItem = menu.addItem(5, (int) R.drawable.ic_goinline);
            this.sendItem = menu.addItem(3, (int) R.drawable.msg_panel_reply);
            this.menuItem = menu.addItem(0, (int) R.drawable.ic_ab_other);
            this.menuItem.addSubItem(11, LocaleController.getString("OpenInExternalApp", R.string.OpenInExternalApp)).setTextColor(-328966);
            this.menuItem.addSubItem(2, LocaleController.getString("ShowAllMedia", R.string.ShowAllMedia)).setTextColor(-328966);
            this.menuItem.addSubItem(4, LocaleController.getString("ShowInChat", R.string.ShowInChat)).setTextColor(-328966);
            this.menuItem.addSubItem(10, LocaleController.getString("ShareFile", R.string.ShareFile)).setTextColor(-328966);
            this.menuItem.addSubItem(1, LocaleController.getString("SaveToGallery", R.string.SaveToGallery)).setTextColor(-328966);
            this.menuItem.addSubItem(6, LocaleController.getString("Delete", R.string.Delete)).setTextColor(-328966);
            this.menuItem.addSubItem(7, LocaleController.getString("StopDownload", R.string.StopDownload)).setTextColor(-328966);
            this.menuItem.redrawPopup(-115203550);
            this.bottomLayout = new FrameLayout(this.actvityContext);
            this.bottomLayout.setBackgroundColor(Theme.ACTION_BAR_PHOTO_VIEWER_COLOR);
            this.containerView.addView(this.bottomLayout, LayoutHelper.createFrame(-1, 48, 83));
            this.groupedPhotosListView = new GroupedPhotosListView(this.actvityContext);
            this.containerView.addView(this.groupedPhotosListView, LayoutHelper.createFrame(-1, 62.0f, 83, 0.0f, 0.0f, 0.0f, 48.0f));
            this.captionTextView = createCaptionTextView();
            this.switchCaptionTextView = createCaptionTextView();
            for (int a = 0; a < 3; a++) {
                this.photoProgressViews[a] = new PhotoProgressView(this.containerView.getContext(), this.containerView);
                this.photoProgressViews[a].setBackgroundState(0, false);
            }
            this.miniProgressView = new RadialProgressView(this.actvityContext) {
                public void setAlpha(float alpha) {
                    super.setAlpha(alpha);
                    if (PhotoViewer.this.containerView != null) {
                        PhotoViewer.this.containerView.invalidate();
                    }
                }

                public void invalidate() {
                    super.invalidate();
                    if (PhotoViewer.this.containerView != null) {
                        PhotoViewer.this.containerView.invalidate();
                    }
                }
            };
            this.miniProgressView.setUseSelfAlpha(true);
            this.miniProgressView.setProgressColor(-1);
            this.miniProgressView.setSize(AndroidUtilities.dp(54.0f));
            this.miniProgressView.setBackgroundResource(R.drawable.circle_big);
            this.miniProgressView.setVisibility(4);
            this.miniProgressView.setAlpha(0.0f);
            this.containerView.addView(this.miniProgressView, LayoutHelper.createFrame(64, 64, 17));
            this.shareButton = new ImageView(this.containerView.getContext());
            this.shareButton.setImageResource(R.drawable.share);
            this.shareButton.setScaleType(ScaleType.CENTER);
            this.shareButton.setBackgroundDrawable(Theme.createSelectorDrawable(Theme.ACTION_BAR_WHITE_SELECTOR_COLOR));
            this.bottomLayout.addView(this.shareButton, LayoutHelper.createFrame(50, -1, 53));
            this.shareButton.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    PhotoViewer.this.onSharePressed();
                }
            });
            this.nameTextView = new TextView(this.containerView.getContext());
            this.nameTextView.setTextSize(1, 14.0f);
            this.nameTextView.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
            this.nameTextView.setSingleLine(true);
            this.nameTextView.setMaxLines(1);
            this.nameTextView.setEllipsize(TruncateAt.END);
            this.nameTextView.setTextColor(-1);
            this.nameTextView.setGravity(3);
            this.bottomLayout.addView(this.nameTextView, LayoutHelper.createFrame(-1, -2.0f, 51, 16.0f, 5.0f, 60.0f, 0.0f));
            this.dateTextView = new TextView(this.containerView.getContext());
            this.dateTextView.setTextSize(1, 13.0f);
            this.dateTextView.setSingleLine(true);
            this.dateTextView.setMaxLines(1);
            this.dateTextView.setEllipsize(TruncateAt.END);
            this.dateTextView.setTextColor(-1);
            this.dateTextView.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
            this.dateTextView.setGravity(3);
            this.bottomLayout.addView(this.dateTextView, LayoutHelper.createFrame(-1, -2.0f, 51, 16.0f, 25.0f, 50.0f, 0.0f));
            createVideoControlsInterface();
            this.progressView = new RadialProgressView(this.parentActivity);
            this.progressView.setProgressColor(-1);
            this.progressView.setBackgroundResource(R.drawable.circle_big);
            this.progressView.setVisibility(4);
            this.containerView.addView(this.progressView, LayoutHelper.createFrame(54, 54, 17));
            this.qualityPicker = new PickerBottomLayoutViewer(this.parentActivity);
            this.qualityPicker.setBackgroundColor(Theme.ACTION_BAR_PHOTO_VIEWER_COLOR);
            this.qualityPicker.updateSelectedCount(0, false);
            this.qualityPicker.setTranslationY((float) AndroidUtilities.dp(120.0f));
            this.qualityPicker.doneButton.setText(LocaleController.getString("Done", R.string.Done).toUpperCase());
            this.containerView.addView(this.qualityPicker, LayoutHelper.createFrame(-1, 48, 83));
            this.qualityPicker.cancelButton.setOnClickListener(new OnClickListener() {
                public void onClick(View view) {
                    PhotoViewer.this.selectedCompression = PhotoViewer.this.previousCompression;
                    PhotoViewer.this.didChangedCompressionLevel(false);
                    PhotoViewer.this.showQualityView(false);
                    PhotoViewer.this.requestVideoPreview(2);
                }
            });
            this.qualityPicker.doneButton.setOnClickListener(new OnClickListener() {
                public void onClick(View view) {
                    PhotoViewer.this.showQualityView(false);
                    PhotoViewer.this.requestVideoPreview(2);
                }
            });
            this.qualityChooseView = new QualityChooseView(this.parentActivity);
            this.qualityChooseView.setTranslationY((float) AndroidUtilities.dp(120.0f));
            this.qualityChooseView.setVisibility(4);
            this.qualityChooseView.setBackgroundColor(Theme.ACTION_BAR_PHOTO_VIEWER_COLOR);
            this.containerView.addView(this.qualityChooseView, LayoutHelper.createFrame(-1, 70.0f, 83, 0.0f, 0.0f, 0.0f, 48.0f));
            this.pickerView = new FrameLayout(this.actvityContext) {
                public boolean dispatchTouchEvent(MotionEvent ev) {
                    return PhotoViewer.this.bottomTouchEnabled && super.dispatchTouchEvent(ev);
                }

                public boolean onInterceptTouchEvent(MotionEvent ev) {
                    return PhotoViewer.this.bottomTouchEnabled && super.onInterceptTouchEvent(ev);
                }

                public boolean onTouchEvent(MotionEvent event) {
                    return PhotoViewer.this.bottomTouchEnabled && super.onTouchEvent(event);
                }
            };
            this.pickerView.setBackgroundColor(Theme.ACTION_BAR_PHOTO_VIEWER_COLOR);
            this.containerView.addView(this.pickerView, LayoutHelper.createFrame(-1, -2, 83));
            this.videoTimelineView = new VideoTimelinePlayView(this.parentActivity);
            this.videoTimelineView.setDelegate(new VideoTimelineViewDelegate() {
                public void onLeftProgressChanged(float progress) {
                    if (PhotoViewer.this.videoPlayer != null) {
                        if (PhotoViewer.this.videoPlayer.isPlaying()) {
                            PhotoViewer.this.videoPlayer.pause();
                            PhotoViewer.this.containerView.invalidate();
                        }
                        PhotoViewer.this.videoPlayer.seekTo((long) ((int) (PhotoViewer.this.videoDuration * progress)));
                        PhotoViewer.this.videoPlayerSeekbar.setProgress(0.0f);
                        PhotoViewer.this.videoTimelineView.setProgress(0.0f);
                        PhotoViewer.this.updateVideoInfo();
                    }
                }

                public void onRightProgressChanged(float progress) {
                    if (PhotoViewer.this.videoPlayer != null) {
                        if (PhotoViewer.this.videoPlayer.isPlaying()) {
                            PhotoViewer.this.videoPlayer.pause();
                            PhotoViewer.this.containerView.invalidate();
                        }
                        PhotoViewer.this.videoPlayer.seekTo((long) ((int) (PhotoViewer.this.videoDuration * progress)));
                        PhotoViewer.this.videoPlayerSeekbar.setProgress(0.0f);
                        PhotoViewer.this.videoTimelineView.setProgress(0.0f);
                        PhotoViewer.this.updateVideoInfo();
                    }
                }

                public void onPlayProgressChanged(float progress) {
                    if (PhotoViewer.this.videoPlayer != null) {
                        PhotoViewer.this.videoPlayer.seekTo((long) ((int) (PhotoViewer.this.videoDuration * progress)));
                    }
                }

                public void didStartDragging() {
                }

                public void didStopDragging() {
                }
            });
            this.pickerView.addView(this.videoTimelineView, LayoutHelper.createFrame(-1, 58.0f, 51, 0.0f, 8.0f, 0.0f, 88.0f));
            this.pickerViewSendButton = new ImageView(this.parentActivity);
            this.pickerViewSendButton.setScaleType(ScaleType.CENTER);
            this.pickerViewSendButton.setBackgroundDrawable(Theme.createSimpleSelectorCircleDrawable(AndroidUtilities.dp(56.0f), -10043398, -10043398));
            this.pickerViewSendButton.setColorFilter(new PorterDuffColorFilter(-1, Mode.MULTIPLY));
            this.pickerViewSendButton.setPadding(AndroidUtilities.dp(4.0f), 0, 0, 0);
            this.pickerViewSendButton.setImageResource(R.drawable.ic_send);
            this.containerView.addView(this.pickerViewSendButton, LayoutHelper.createFrame(56, 56.0f, 85, 0.0f, 0.0f, 14.0f, 14.0f));
            this.pickerViewSendButton.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    if (PhotoViewer.this.captionEditText.getTag() == null && PhotoViewer.this.placeProvider != null && !PhotoViewer.this.doneButtonPressed) {
                        PhotoViewer.this.placeProvider.sendButtonPressed(PhotoViewer.this.currentIndex, PhotoViewer.this.getCurrentVideoEditedInfo());
                        PhotoViewer.this.doneButtonPressed = true;
                        PhotoViewer.this.closePhoto(false, false);
                    }
                }
            });
            LinearLayout itemsLayout = new LinearLayout(this.parentActivity);
            itemsLayout.setOrientation(0);
            this.pickerView.addView(itemsLayout, LayoutHelper.createFrame(-2, 48.0f, 81, 0.0f, 0.0f, 34.0f, 0.0f));
            this.cropItem = new ImageView(this.parentActivity);
            this.cropItem.setScaleType(ScaleType.CENTER);
            this.cropItem.setImageResource(R.drawable.photo_crop);
            this.cropItem.setBackgroundDrawable(Theme.createSelectorDrawable(Theme.ACTION_BAR_WHITE_SELECTOR_COLOR));
            itemsLayout.addView(this.cropItem, LayoutHelper.createLinear(70, 48));
            this.cropItem.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    if (PhotoViewer.this.captionEditText.getTag() == null) {
                        PhotoViewer.this.switchToEditMode(1);
                    }
                }
            });
            this.paintItem = new ImageView(this.parentActivity);
            this.paintItem.setScaleType(ScaleType.CENTER);
            this.paintItem.setImageResource(R.drawable.photo_paint);
            this.paintItem.setBackgroundDrawable(Theme.createSelectorDrawable(Theme.ACTION_BAR_WHITE_SELECTOR_COLOR));
            itemsLayout.addView(this.paintItem, LayoutHelper.createLinear(70, 48));
            this.paintItem.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    if (PhotoViewer.this.captionEditText.getTag() == null) {
                        PhotoViewer.this.switchToEditMode(3);
                    }
                }
            });
            this.compressItem = new ImageView(this.parentActivity);
            this.compressItem.setTag(Integer.valueOf(1));
            this.compressItem.setScaleType(ScaleType.CENTER);
            this.compressItem.setBackgroundDrawable(Theme.createSelectorDrawable(Theme.ACTION_BAR_WHITE_SELECTOR_COLOR));
            this.selectedCompression = MessagesController.getGlobalMainSettings().getInt("compress_video2", 1);
            if (this.selectedCompression <= 0) {
                this.compressItem.setImageResource(R.drawable.video_240);
            } else if (this.selectedCompression == 1) {
                this.compressItem.setImageResource(R.drawable.video_360);
            } else if (this.selectedCompression == 2) {
                this.compressItem.setImageResource(R.drawable.video_480);
            } else if (this.selectedCompression == 3) {
                this.compressItem.setImageResource(R.drawable.video_720);
            } else if (this.selectedCompression == 4) {
                this.compressItem.setImageResource(R.drawable.video_1080);
            }
            itemsLayout.addView(this.compressItem, LayoutHelper.createLinear(70, 48));
            this.compressItem.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    if (PhotoViewer.this.captionEditText.getTag() == null) {
                        PhotoViewer.this.showQualityView(true);
                        PhotoViewer.this.requestVideoPreview(1);
                    }
                }
            });
            this.muteItem = new ImageView(this.parentActivity);
            this.muteItem.setScaleType(ScaleType.CENTER);
            this.muteItem.setBackgroundDrawable(Theme.createSelectorDrawable(Theme.ACTION_BAR_WHITE_SELECTOR_COLOR));
            itemsLayout.addView(this.muteItem, LayoutHelper.createLinear(70, 48));
            this.muteItem.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    if (PhotoViewer.this.captionEditText.getTag() == null) {
                        PhotoViewer.this.muteVideo = !PhotoViewer.this.muteVideo;
                        if (!PhotoViewer.this.muteVideo || PhotoViewer.this.checkImageView.isChecked()) {
                            Object object = PhotoViewer.this.imagesArrLocals.get(PhotoViewer.this.currentIndex);
                            if (object instanceof PhotoEntry) {
                                ((PhotoEntry) object).editedInfo = PhotoViewer.this.getCurrentVideoEditedInfo();
                            }
                        } else {
                            PhotoViewer.this.checkImageView.callOnClick();
                        }
                        PhotoViewer.this.updateMuteButton();
                    }
                }
            });
            this.cameraItem = new ImageView(this.parentActivity);
            this.cameraItem.setScaleType(ScaleType.CENTER);
            this.cameraItem.setImageResource(R.drawable.photo_add);
            this.cameraItem.setBackgroundDrawable(Theme.createSelectorDrawable(Theme.ACTION_BAR_WHITE_SELECTOR_COLOR));
            this.containerView.addView(this.cameraItem, LayoutHelper.createFrame(48, 48.0f, 85, 0.0f, 0.0f, 16.0f, 0.0f));
            this.cameraItem.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    if (PhotoViewer.this.placeProvider != null && PhotoViewer.this.captionEditText.getTag() == null) {
                        PhotoViewer.this.placeProvider.needAddMorePhotos();
                        PhotoViewer.this.closePhoto(true, false);
                    }
                }
            });
            this.tuneItem = new ImageView(this.parentActivity);
            this.tuneItem.setScaleType(ScaleType.CENTER);
            this.tuneItem.setImageResource(R.drawable.photo_tools);
            this.tuneItem.setBackgroundDrawable(Theme.createSelectorDrawable(Theme.ACTION_BAR_WHITE_SELECTOR_COLOR));
            itemsLayout.addView(this.tuneItem, LayoutHelper.createLinear(70, 48));
            this.tuneItem.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    if (PhotoViewer.this.captionEditText.getTag() == null) {
                        PhotoViewer.this.switchToEditMode(2);
                    }
                }
            });
            this.timeItem = new ImageView(this.parentActivity);
            this.timeItem.setScaleType(ScaleType.CENTER);
            this.timeItem.setImageResource(R.drawable.photo_timer);
            this.timeItem.setBackgroundDrawable(Theme.createSelectorDrawable(Theme.ACTION_BAR_WHITE_SELECTOR_COLOR));
            itemsLayout.addView(this.timeItem, LayoutHelper.createLinear(70, 48));
            this.timeItem.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    if (PhotoViewer.this.parentActivity != null && PhotoViewer.this.captionEditText.getTag() == null) {
                        CharSequence string;
                        int currentTTL;
                        BottomSheet.Builder builder = new BottomSheet.Builder(PhotoViewer.this.parentActivity);
                        builder.setUseHardwareLayer(false);
                        LinearLayout linearLayout = new LinearLayout(PhotoViewer.this.parentActivity);
                        linearLayout.setOrientation(1);
                        builder.setCustomView(linearLayout);
                        TextView titleView = new TextView(PhotoViewer.this.parentActivity);
                        titleView.setLines(1);
                        titleView.setSingleLine(true);
                        titleView.setText(LocaleController.getString("MessageLifetime", R.string.MessageLifetime));
                        titleView.setTextColor(-1);
                        titleView.setTextSize(1, 16.0f);
                        titleView.setEllipsize(TruncateAt.MIDDLE);
                        titleView.setPadding(AndroidUtilities.dp(21.0f), AndroidUtilities.dp(8.0f), AndroidUtilities.dp(21.0f), AndroidUtilities.dp(4.0f));
                        titleView.setGravity(16);
                        linearLayout.addView(titleView, LayoutHelper.createFrame(-1, -2.0f));
                        titleView.setOnTouchListener(new OnTouchListener() {
                            public boolean onTouch(View v, MotionEvent event) {
                                return true;
                            }
                        });
                        titleView = new TextView(PhotoViewer.this.parentActivity);
                        if (PhotoViewer.this.isCurrentVideo) {
                            string = LocaleController.getString("MessageLifetimeVideo", R.string.MessageLifetimeVideo);
                        } else {
                            string = LocaleController.getString("MessageLifetimePhoto", R.string.MessageLifetimePhoto);
                        }
                        titleView.setText(string);
                        titleView.setTextColor(-8355712);
                        titleView.setTextSize(1, 14.0f);
                        titleView.setEllipsize(TruncateAt.MIDDLE);
                        titleView.setPadding(AndroidUtilities.dp(21.0f), 0, AndroidUtilities.dp(21.0f), AndroidUtilities.dp(8.0f));
                        titleView.setGravity(16);
                        linearLayout.addView(titleView, LayoutHelper.createFrame(-1, -2.0f));
                        titleView.setOnTouchListener(new OnTouchListener() {
                            public boolean onTouch(View v, MotionEvent event) {
                                return true;
                            }
                        });
                        final BottomSheet bottomSheet = builder.create();
                        final NumberPicker numberPicker = new NumberPicker(PhotoViewer.this.parentActivity);
                        numberPicker.setMinValue(0);
                        numberPicker.setMaxValue(28);
                        Object object = PhotoViewer.this.imagesArrLocals.get(PhotoViewer.this.currentIndex);
                        if (object instanceof PhotoEntry) {
                            currentTTL = ((PhotoEntry) object).ttl;
                        } else if (object instanceof SearchImage) {
                            currentTTL = ((SearchImage) object).ttl;
                        } else {
                            currentTTL = 0;
                        }
                        if (currentTTL == 0) {
                            numberPicker.setValue(MessagesController.getGlobalMainSettings().getInt("self_destruct", 7));
                        } else if (currentTTL < 0 || currentTTL >= 21) {
                            numberPicker.setValue(((currentTTL / 5) + 21) - 5);
                        } else {
                            numberPicker.setValue(currentTTL);
                        }
                        numberPicker.setTextColor(-1);
                        numberPicker.setSelectorColor(-11711155);
                        numberPicker.setFormatter(new Formatter() {
                            public String format(int value) {
                                if (value == 0) {
                                    return LocaleController.getString("ShortMessageLifetimeForever", R.string.ShortMessageLifetimeForever);
                                }
                                if (value < 1 || value >= 21) {
                                    return LocaleController.formatTTLString((value - 16) * 5);
                                }
                                return LocaleController.formatTTLString(value);
                            }
                        });
                        linearLayout.addView(numberPicker, LayoutHelper.createLinear(-1, -2));
                        FrameLayout buttonsLayout = new FrameLayout(PhotoViewer.this.parentActivity) {
                            protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
                                int count = getChildCount();
                                View positiveButton = null;
                                int width = right - left;
                                for (int a = 0; a < count; a++) {
                                    View child = getChildAt(a);
                                    if (((Integer) child.getTag()).intValue() == -1) {
                                        positiveButton = child;
                                        child.layout((width - getPaddingRight()) - child.getMeasuredWidth(), getPaddingTop(), (width - getPaddingRight()) + child.getMeasuredWidth(), getPaddingTop() + child.getMeasuredHeight());
                                    } else if (((Integer) child.getTag()).intValue() == -2) {
                                        int x = (width - getPaddingRight()) - child.getMeasuredWidth();
                                        if (positiveButton != null) {
                                            x -= positiveButton.getMeasuredWidth() + AndroidUtilities.dp(8.0f);
                                        }
                                        child.layout(x, getPaddingTop(), child.getMeasuredWidth() + x, getPaddingTop() + child.getMeasuredHeight());
                                    } else {
                                        child.layout(getPaddingLeft(), getPaddingTop(), getPaddingLeft() + child.getMeasuredWidth(), getPaddingTop() + child.getMeasuredHeight());
                                    }
                                }
                            }
                        };
                        buttonsLayout.setPadding(AndroidUtilities.dp(8.0f), AndroidUtilities.dp(8.0f), AndroidUtilities.dp(8.0f), AndroidUtilities.dp(8.0f));
                        linearLayout.addView(buttonsLayout, LayoutHelper.createLinear(-1, 52));
                        TextView textView = new TextView(PhotoViewer.this.parentActivity);
                        textView.setMinWidth(AndroidUtilities.dp(64.0f));
                        textView.setTag(Integer.valueOf(-1));
                        textView.setTextSize(1, 14.0f);
                        textView.setTextColor(-11944718);
                        textView.setGravity(17);
                        textView.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
                        textView.setText(LocaleController.getString("Done", R.string.Done).toUpperCase());
                        textView.setBackgroundDrawable(Theme.getRoundRectSelectorDrawable());
                        textView.setPadding(AndroidUtilities.dp(10.0f), 0, AndroidUtilities.dp(10.0f), 0);
                        buttonsLayout.addView(textView, LayoutHelper.createFrame(-2, 36, 53));
                        textView.setOnClickListener(new OnClickListener() {
                            public void onClick(View v) {
                                int seconds;
                                int value = numberPicker.getValue();
                                Editor editor = MessagesController.getGlobalMainSettings().edit();
                                editor.putInt("self_destruct", value);
                                editor.commit();
                                bottomSheet.dismiss();
                                if (value < 0 || value >= 21) {
                                    seconds = (value - 16) * 5;
                                } else {
                                    seconds = value;
                                }
                                Object object = PhotoViewer.this.imagesArrLocals.get(PhotoViewer.this.currentIndex);
                                if (object instanceof PhotoEntry) {
                                    ((PhotoEntry) object).ttl = seconds;
                                } else if (object instanceof SearchImage) {
                                    ((SearchImage) object).ttl = seconds;
                                }
                                PhotoViewer.this.timeItem.setColorFilter(seconds != 0 ? new PorterDuffColorFilter(-12734994, Mode.MULTIPLY) : null);
                                if (!PhotoViewer.this.checkImageView.isChecked()) {
                                    PhotoViewer.this.checkImageView.callOnClick();
                                }
                            }
                        });
                        textView = new TextView(PhotoViewer.this.parentActivity);
                        textView.setMinWidth(AndroidUtilities.dp(64.0f));
                        textView.setTag(Integer.valueOf(-2));
                        textView.setTextSize(1, 14.0f);
                        textView.setTextColor(-11944718);
                        textView.setGravity(17);
                        textView.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
                        textView.setText(LocaleController.getString("Cancel", R.string.Cancel).toUpperCase());
                        textView.setBackgroundDrawable(Theme.getRoundRectSelectorDrawable());
                        textView.setPadding(AndroidUtilities.dp(10.0f), 0, AndroidUtilities.dp(10.0f), 0);
                        buttonsLayout.addView(textView, LayoutHelper.createFrame(-2, 36, 53));
                        textView.setOnClickListener(new OnClickListener() {
                            public void onClick(View v) {
                                bottomSheet.dismiss();
                            }
                        });
                        bottomSheet.show();
                        bottomSheet.setBackgroundColor(Theme.ACTION_BAR_VIDEO_EDIT_COLOR);
                    }
                }
            });
            this.editorDoneLayout = new PickerBottomLayoutViewer(this.actvityContext);
            this.editorDoneLayout.setBackgroundColor(Theme.ACTION_BAR_PHOTO_VIEWER_COLOR);
            this.editorDoneLayout.updateSelectedCount(0, false);
            this.editorDoneLayout.setVisibility(8);
            this.containerView.addView(this.editorDoneLayout, LayoutHelper.createFrame(-1, 48, 83));
            this.editorDoneLayout.cancelButton.setOnClickListener(new OnClickListener() {
                public void onClick(View view) {
                    if (PhotoViewer.this.currentEditMode == 1) {
                        PhotoViewer.this.photoCropView.cancelAnimationRunnable();
                    }
                    PhotoViewer.this.switchToEditMode(0);
                }
            });
            this.editorDoneLayout.doneButton.setOnClickListener(new OnClickListener() {
                public void onClick(View view) {
                    if (PhotoViewer.this.currentEditMode != 1 || PhotoViewer.this.photoCropView.isReady()) {
                        PhotoViewer.this.applyCurrentEditMode();
                        PhotoViewer.this.switchToEditMode(0);
                    }
                }
            });
            this.resetButton = new TextView(this.actvityContext);
            this.resetButton.setVisibility(8);
            this.resetButton.setTextSize(1, 14.0f);
            this.resetButton.setTextColor(-1);
            this.resetButton.setGravity(17);
            this.resetButton.setBackgroundDrawable(Theme.createSelectorDrawable(Theme.ACTION_BAR_PICKER_SELECTOR_COLOR, 0));
            this.resetButton.setPadding(AndroidUtilities.dp(20.0f), 0, AndroidUtilities.dp(20.0f), 0);
            this.resetButton.setText(LocaleController.getString("Reset", R.string.CropReset).toUpperCase());
            this.resetButton.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
            this.editorDoneLayout.addView(this.resetButton, LayoutHelper.createFrame(-2, -1, 49));
            this.resetButton.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    PhotoViewer.this.photoCropView.reset();
                }
            });
            this.gestureDetector = new GestureDetector(this.containerView.getContext(), this);
            this.gestureDetector.setOnDoubleTapListener(this);
            ImageReceiverDelegate imageReceiverDelegate = new ImageReceiverDelegate() {
                public void didSetImage(ImageReceiver imageReceiver, boolean set, boolean thumb) {
                    if (imageReceiver == PhotoViewer.this.centerImage && set && !thumb && PhotoViewer.this.currentEditMode == 1 && PhotoViewer.this.photoCropView != null) {
                        Bitmap bitmap = imageReceiver.getBitmap();
                        if (bitmap != null) {
                            PhotoViewer.this.photoCropView.setBitmap(bitmap, imageReceiver.getOrientation(), PhotoViewer.this.sendPhotoType != 1);
                        }
                    }
                    if (imageReceiver != PhotoViewer.this.centerImage || !set || PhotoViewer.this.placeProvider == null || !PhotoViewer.this.placeProvider.scaleToFill() || PhotoViewer.this.ignoreDidSetImage) {
                        return;
                    }
                    if (PhotoViewer.this.wasLayout) {
                        PhotoViewer.this.setScaleToFill();
                    } else {
                        PhotoViewer.this.dontResetZoomOnFirstLayout = true;
                    }
                }
            };
            this.centerImage.setParentView(this.containerView);
            this.centerImage.setCrossfadeAlpha((byte) 2);
            this.centerImage.setInvalidateAll(true);
            this.centerImage.setDelegate(imageReceiverDelegate);
            this.leftImage.setParentView(this.containerView);
            this.leftImage.setCrossfadeAlpha((byte) 2);
            this.leftImage.setInvalidateAll(true);
            this.leftImage.setDelegate(imageReceiverDelegate);
            this.rightImage.setParentView(this.containerView);
            this.rightImage.setCrossfadeAlpha((byte) 2);
            this.rightImage.setInvalidateAll(true);
            this.rightImage.setDelegate(imageReceiverDelegate);
            int rotation = ((WindowManager) ApplicationLoader.applicationContext.getSystemService("window")).getDefaultDisplay().getRotation();
            this.checkImageView = new CheckBox(this.containerView.getContext(), R.drawable.selectphoto_large) {
                public boolean onTouchEvent(MotionEvent event) {
                    return PhotoViewer.this.bottomTouchEnabled && super.onTouchEvent(event);
                }
            };
            this.checkImageView.setDrawBackground(true);
            this.checkImageView.setHasBorder(true);
            this.checkImageView.setSize(40);
            this.checkImageView.setCheckOffset(AndroidUtilities.dp(1.0f));
            this.checkImageView.setColor(-10043398, -1);
            this.checkImageView.setVisibility(8);
            FrameLayoutDrawer frameLayoutDrawer = this.containerView;
            View view = this.checkImageView;
            float f = (rotation == 3 || rotation == 1) ? 58.0f : 68.0f;
            frameLayoutDrawer.addView(view, LayoutHelper.createFrame(40, 40.0f, 53, 0.0f, f, 10.0f, 0.0f));
            if (VERSION.SDK_INT >= 21) {
                layoutParams = (FrameLayout.LayoutParams) this.checkImageView.getLayoutParams();
                layoutParams.topMargin += AndroidUtilities.statusBarHeight;
            }
            this.checkImageView.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    if (PhotoViewer.this.captionEditText.getTag() == null) {
                        PhotoViewer.this.setPhotoChecked();
                    }
                }
            });
            this.photosCounterView = new CounterView(this.parentActivity);
            frameLayoutDrawer = this.containerView;
            view = this.photosCounterView;
            f = (rotation == 3 || rotation == 1) ? 58.0f : 68.0f;
            frameLayoutDrawer.addView(view, LayoutHelper.createFrame(40, 40.0f, 53, 0.0f, f, 66.0f, 0.0f));
            if (VERSION.SDK_INT >= 21) {
                layoutParams = (FrameLayout.LayoutParams) this.photosCounterView.getLayoutParams();
                layoutParams.topMargin += AndroidUtilities.statusBarHeight;
            }
            this.photosCounterView.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    if (PhotoViewer.this.captionEditText.getTag() == null && PhotoViewer.this.placeProvider != null && PhotoViewer.this.placeProvider.getSelectedPhotosOrder() != null && !PhotoViewer.this.placeProvider.getSelectedPhotosOrder().isEmpty()) {
                        PhotoViewer.this.togglePhotosListView(!PhotoViewer.this.isPhotosListViewVisible, true);
                    }
                }
            });
            this.selectedPhotosListView = new RecyclerListView(this.parentActivity);
            this.selectedPhotosListView.setVisibility(8);
            this.selectedPhotosListView.setAlpha(0.0f);
            this.selectedPhotosListView.setTranslationY((float) (-AndroidUtilities.dp(10.0f)));
            this.selectedPhotosListView.addItemDecoration(new ItemDecoration() {
                public void getItemOffsets(Rect outRect, View view, RecyclerView parent, State state) {
                    int position = parent.getChildAdapterPosition(view);
                    if ((view instanceof PhotoPickerPhotoCell) && position == 0) {
                        outRect.left = AndroidUtilities.dp(3.0f);
                    } else {
                        outRect.left = 0;
                    }
                    outRect.right = AndroidUtilities.dp(3.0f);
                }
            });
            ((DefaultItemAnimator) this.selectedPhotosListView.getItemAnimator()).setDelayAnimations(false);
            this.selectedPhotosListView.setBackgroundColor(Theme.ACTION_BAR_PHOTO_VIEWER_COLOR);
            this.selectedPhotosListView.setPadding(0, AndroidUtilities.dp(3.0f), 0, AndroidUtilities.dp(3.0f));
            this.selectedPhotosListView.setLayoutManager(new LinearLayoutManager(this.parentActivity, 0, false) {
                public void smoothScrollToPosition(RecyclerView recyclerView, State state, int position) {
                    LinearSmoothScrollerEnd linearSmoothScroller = new LinearSmoothScrollerEnd(recyclerView.getContext());
                    linearSmoothScroller.setTargetPosition(position);
                    startSmoothScroll(linearSmoothScroller);
                }
            });
            RecyclerListView recyclerListView = this.selectedPhotosListView;
            Adapter listAdapter = new ListAdapter(this.parentActivity);
            this.selectedPhotosAdapter = listAdapter;
            recyclerListView.setAdapter(listAdapter);
            this.containerView.addView(this.selectedPhotosListView, LayoutHelper.createFrame(-1, 88, 51));
            this.selectedPhotosListView.setOnItemClickListener(new OnItemClickListener() {
                public void onItemClick(View view, int position) {
                    if (position == 0 && PhotoViewer.this.placeProvider.allowGroupPhotos()) {
                        boolean z;
                        boolean enabled = SharedConfig.groupPhotosEnabled;
                        SharedConfig.toggleGroupPhotosEnabled();
                        PhotoViewer.this.placeProvider.toggleGroupPhotosEnabled();
                        ((ImageView) view).setColorFilter(!enabled ? new PorterDuffColorFilter(-10043398, Mode.MULTIPLY) : null);
                        PhotoViewer photoViewer = PhotoViewer.this;
                        if (enabled) {
                            z = false;
                        } else {
                            z = true;
                        }
                        photoViewer.showHint(false, z);
                        return;
                    }
                    PhotoViewer.this.ignoreDidSetImage = true;
                    int idx = PhotoViewer.this.imagesArrLocals.indexOf(view.getTag());
                    if (idx >= 0) {
                        PhotoViewer.this.currentIndex = -1;
                        PhotoViewer.this.setImageIndex(idx, true);
                    }
                    PhotoViewer.this.ignoreDidSetImage = false;
                }
            });
            this.captionEditText = new PhotoViewerCaptionEnterView(this.actvityContext, this.containerView, this.windowView) {
                public boolean dispatchTouchEvent(MotionEvent ev) {
                    try {
                        return !PhotoViewer.this.bottomTouchEnabled && super.dispatchTouchEvent(ev);
                    } catch (Throwable e) {
                        FileLog.e(e);
                        return false;
                    }
                }

                public boolean onInterceptTouchEvent(MotionEvent ev) {
                    try {
                        return !PhotoViewer.this.bottomTouchEnabled && super.onInterceptTouchEvent(ev);
                    } catch (Throwable e) {
                        FileLog.e(e);
                        return false;
                    }
                }

                public boolean onTouchEvent(MotionEvent event) {
                    return !PhotoViewer.this.bottomTouchEnabled && super.onTouchEvent(event);
                }
            };
            this.captionEditText.setDelegate(new PhotoViewerCaptionEnterViewDelegate() {
                public void onCaptionEnter() {
                    PhotoViewer.this.closeCaptionEnter(true);
                }

                public void onTextChanged(CharSequence text) {
                    if (PhotoViewer.this.mentionsAdapter != null && PhotoViewer.this.captionEditText != null && PhotoViewer.this.parentChatActivity != null && text != null) {
                        PhotoViewer.this.mentionsAdapter.searchUsernameOrHashtag(text.toString(), PhotoViewer.this.captionEditText.getCursorPosition(), PhotoViewer.this.parentChatActivity.messages, false);
                    }
                }

                public void onWindowSizeChanged(int size) {
                    int i;
                    int min = Math.min(3, PhotoViewer.this.mentionsAdapter.getItemCount()) * 36;
                    if (PhotoViewer.this.mentionsAdapter.getItemCount() > 3) {
                        i = 18;
                    } else {
                        i = 0;
                    }
                    if (size - (ActionBar.getCurrentActionBarHeight() * 2) < AndroidUtilities.dp((float) (i + min))) {
                        PhotoViewer.this.allowMentions = false;
                        if (PhotoViewer.this.mentionListView != null && PhotoViewer.this.mentionListView.getVisibility() == 0) {
                            PhotoViewer.this.mentionListView.setVisibility(4);
                            return;
                        }
                        return;
                    }
                    PhotoViewer.this.allowMentions = true;
                    if (PhotoViewer.this.mentionListView != null && PhotoViewer.this.mentionListView.getVisibility() == 4) {
                        PhotoViewer.this.mentionListView.setVisibility(0);
                    }
                }
            });
            this.containerView.addView(this.captionEditText, LayoutHelper.createFrame(-1, -2, 83));
            this.mentionListView = new RecyclerListView(this.actvityContext) {
                public boolean dispatchTouchEvent(MotionEvent ev) {
                    return !PhotoViewer.this.bottomTouchEnabled && super.dispatchTouchEvent(ev);
                }

                public boolean onInterceptTouchEvent(MotionEvent ev) {
                    return !PhotoViewer.this.bottomTouchEnabled && super.onInterceptTouchEvent(ev);
                }

                public boolean onTouchEvent(MotionEvent event) {
                    return !PhotoViewer.this.bottomTouchEnabled && super.onTouchEvent(event);
                }
            };
            this.mentionListView.setTag(Integer.valueOf(5));
            this.mentionLayoutManager = new LinearLayoutManager(this.actvityContext) {
                public boolean supportsPredictiveItemAnimations() {
                    return false;
                }
            };
            this.mentionLayoutManager.setOrientation(1);
            this.mentionListView.setLayoutManager(this.mentionLayoutManager);
            this.mentionListView.setBackgroundColor(Theme.ACTION_BAR_PHOTO_VIEWER_COLOR);
            this.mentionListView.setVisibility(8);
            this.mentionListView.setClipToPadding(true);
            this.mentionListView.setOverScrollMode(2);
            this.containerView.addView(this.mentionListView, LayoutHelper.createFrame(-1, 110, 83));
            recyclerListView = this.mentionListView;
            listAdapter = new MentionsAdapter(this.actvityContext, true, 0, new MentionsAdapterDelegate() {
                public void needChangePanelVisibility(boolean show) {
                    if (show) {
                        int i;
                        FrameLayout.LayoutParams layoutParams3 = (FrameLayout.LayoutParams) PhotoViewer.this.mentionListView.getLayoutParams();
                        int min = Math.min(3, PhotoViewer.this.mentionsAdapter.getItemCount()) * 36;
                        if (PhotoViewer.this.mentionsAdapter.getItemCount() > 3) {
                            i = 18;
                        } else {
                            i = 0;
                        }
                        int height = min + i;
                        layoutParams3.height = AndroidUtilities.dp((float) height);
                        layoutParams3.topMargin = -AndroidUtilities.dp((float) height);
                        PhotoViewer.this.mentionListView.setLayoutParams(layoutParams3);
                        if (PhotoViewer.this.mentionListAnimation != null) {
                            PhotoViewer.this.mentionListAnimation.cancel();
                            PhotoViewer.this.mentionListAnimation = null;
                        }
                        if (PhotoViewer.this.mentionListView.getVisibility() == 0) {
                            PhotoViewer.this.mentionListView.setAlpha(1.0f);
                            return;
                        }
                        PhotoViewer.this.mentionLayoutManager.scrollToPositionWithOffset(0, 10000);
                        if (PhotoViewer.this.allowMentions) {
                            PhotoViewer.this.mentionListView.setVisibility(0);
                            PhotoViewer.this.mentionListAnimation = new AnimatorSet();
                            PhotoViewer.this.mentionListAnimation.playTogether(new Animator[]{ObjectAnimator.ofFloat(PhotoViewer.this.mentionListView, "alpha", new float[]{0.0f, 1.0f})});
                            PhotoViewer.this.mentionListAnimation.addListener(new AnimatorListenerAdapter() {
                                public void onAnimationEnd(Animator animation) {
                                    if (PhotoViewer.this.mentionListAnimation != null && PhotoViewer.this.mentionListAnimation.equals(animation)) {
                                        PhotoViewer.this.mentionListAnimation = null;
                                    }
                                }
                            });
                            PhotoViewer.this.mentionListAnimation.setDuration(200);
                            PhotoViewer.this.mentionListAnimation.start();
                            return;
                        }
                        PhotoViewer.this.mentionListView.setAlpha(1.0f);
                        PhotoViewer.this.mentionListView.setVisibility(4);
                        return;
                    }
                    if (PhotoViewer.this.mentionListAnimation != null) {
                        PhotoViewer.this.mentionListAnimation.cancel();
                        PhotoViewer.this.mentionListAnimation = null;
                    }
                    if (PhotoViewer.this.mentionListView.getVisibility() == 8) {
                        return;
                    }
                    if (PhotoViewer.this.allowMentions) {
                        PhotoViewer.this.mentionListAnimation = new AnimatorSet();
                        AnimatorSet access$12300 = PhotoViewer.this.mentionListAnimation;
                        Animator[] animatorArr = new Animator[1];
                        animatorArr[0] = ObjectAnimator.ofFloat(PhotoViewer.this.mentionListView, "alpha", new float[]{0.0f});
                        access$12300.playTogether(animatorArr);
                        PhotoViewer.this.mentionListAnimation.addListener(new AnimatorListenerAdapter() {
                            public void onAnimationEnd(Animator animation) {
                                if (PhotoViewer.this.mentionListAnimation != null && PhotoViewer.this.mentionListAnimation.equals(animation)) {
                                    PhotoViewer.this.mentionListView.setVisibility(8);
                                    PhotoViewer.this.mentionListAnimation = null;
                                }
                            }
                        });
                        PhotoViewer.this.mentionListAnimation.setDuration(200);
                        PhotoViewer.this.mentionListAnimation.start();
                        return;
                    }
                    PhotoViewer.this.mentionListView.setVisibility(8);
                }

                public void onContextSearch(boolean searching) {
                }

                public void onContextClick(BotInlineResult result) {
                }
            });
            this.mentionsAdapter = listAdapter;
            recyclerListView.setAdapter(listAdapter);
            this.mentionListView.setOnItemClickListener(new OnItemClickListener() {
                public void onItemClick(View view, int position) {
                    User object = PhotoViewer.this.mentionsAdapter.getItem(position);
                    int start = PhotoViewer.this.mentionsAdapter.getResultStartPosition();
                    int len = PhotoViewer.this.mentionsAdapter.getResultLength();
                    if (object instanceof User) {
                        User user = object;
                        if (user.username != null) {
                            PhotoViewer.this.captionEditText.replaceWithText(start, len, "@" + user.username + " ", false);
                            return;
                        }
                        Spannable spannable = new SpannableString(UserObject.getFirstName(user) + " ");
                        spannable.setSpan(new URLSpanUserMentionPhotoViewer(TtmlNode.ANONYMOUS_REGION_ID + user.id, true), 0, spannable.length(), 33);
                        PhotoViewer.this.captionEditText.replaceWithText(start, len, spannable, false);
                    } else if (object instanceof String) {
                        PhotoViewer.this.captionEditText.replaceWithText(start, len, object + " ", false);
                    } else if (object instanceof EmojiSuggestion) {
                        String code = ((EmojiSuggestion) object).emoji;
                        PhotoViewer.this.captionEditText.addEmojiToRecent(code);
                        PhotoViewer.this.captionEditText.replaceWithText(start, len, code, true);
                    }
                }
            });
            this.mentionListView.setOnItemLongClickListener(new OnItemLongClickListener() {
                public boolean onItemClick(View view, int position) {
                    if (!(PhotoViewer.this.mentionsAdapter.getItem(position) instanceof String)) {
                        return false;
                    }
                    Builder builder = new Builder(PhotoViewer.this.parentActivity);
                    builder.setTitle(LocaleController.getString("AppName", R.string.AppName));
                    builder.setMessage(LocaleController.getString("ClearSearch", R.string.ClearSearch));
                    builder.setPositiveButton(LocaleController.getString("ClearButton", R.string.ClearButton).toUpperCase(), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialogInterface, int i) {
                            PhotoViewer.this.mentionsAdapter.clearRecentHashtags();
                        }
                    });
                    builder.setNegativeButton(LocaleController.getString("Cancel", R.string.Cancel), null);
                    PhotoViewer.this.showAlertDialog(builder);
                    return true;
                }
            });
        }
    }

    private boolean checkInlinePermissions() {
        if (this.parentActivity == null) {
            return false;
        }
        if (VERSION.SDK_INT < 23 || Settings.canDrawOverlays(this.parentActivity)) {
            return true;
        }
        new Builder(this.parentActivity).setTitle(LocaleController.getString("AppName", R.string.AppName)).setMessage(LocaleController.getString("PermissionDrawAboveOtherApps", R.string.PermissionDrawAboveOtherApps)).setPositiveButton(LocaleController.getString("PermissionOpenSettings", R.string.PermissionOpenSettings), new DialogInterface.OnClickListener() {
            @TargetApi(23)
            public void onClick(DialogInterface dialog, int which) {
                if (PhotoViewer.this.parentActivity != null) {
                    try {
                        PhotoViewer.this.parentActivity.startActivity(new Intent("android.settings.action.MANAGE_OVERLAY_PERMISSION", Uri.parse("package:" + PhotoViewer.this.parentActivity.getPackageName())));
                    } catch (Throwable e) {
                        FileLog.e(e);
                    }
                }
            }
        }).show();
        return false;
    }

    private TextView createCaptionTextView() {
        TextView textView = new TextView(this.actvityContext) {
            public boolean onTouchEvent(MotionEvent event) {
                return PhotoViewer.this.bottomTouchEnabled && super.onTouchEvent(event);
            }
        };
        textView.setMovementMethod(new LinkMovementMethodMy());
        textView.setPadding(AndroidUtilities.dp(20.0f), AndroidUtilities.dp(8.0f), AndroidUtilities.dp(20.0f), AndroidUtilities.dp(8.0f));
        textView.setLinkTextColor(-1);
        textView.setTextColor(-1);
        textView.setHighlightColor(872415231);
        textView.setEllipsize(TruncateAt.END);
        textView.setGravity((LocaleController.isRTL ? 5 : 3) | 16);
        textView.setTextSize(1, 16.0f);
        textView.setVisibility(4);
        textView.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                if (PhotoViewer.this.needCaptionLayout) {
                    PhotoViewer.this.openCaptionEnter();
                }
            }
        });
        return textView;
    }

    private int getLeftInset() {
        if (this.lastInsets == null || VERSION.SDK_INT < 21) {
            return 0;
        }
        return ((WindowInsets) this.lastInsets).getSystemWindowInsetLeft();
    }

    private void dismissInternal() {
        try {
            if (this.windowView.getParent() != null) {
                ((LaunchActivity) this.parentActivity).drawerLayoutContainer.setAllowDrawContent(true);
                ((WindowManager) this.parentActivity.getSystemService("window")).removeView(this.windowView);
            }
        } catch (Throwable e) {
            FileLog.e(e);
        }
    }

    private void switchToPip() {
        if (this.videoPlayer != null && this.textureUploaded && checkInlinePermissions() && !this.changingTextureView && !this.switchingInlineMode && !this.isInline) {
            if (PipInstance != null) {
                PipInstance.destroyPhotoViewer();
            }
            PipInstance = Instance;
            Instance = null;
            this.switchingInlineMode = true;
            this.isVisible = false;
            if (this.currentPlaceObject != null) {
                this.currentPlaceObject.imageReceiver.setVisible(true, true);
            }
            if (VERSION.SDK_INT >= 21) {
                this.pipAnimationInProgress = true;
                org.telegram.ui.Components.Rect rect = PipVideoView.getPipRect(this.aspectRatioFrameLayout.getAspectRatio());
                float scale = rect.width / ((float) this.videoTextureView.getWidth());
                rect.y += (float) AndroidUtilities.statusBarHeight;
                AnimatorSet animatorSet = new AnimatorSet();
                Animator[] animatorArr = new Animator[13];
                animatorArr[0] = ObjectAnimator.ofFloat(this.textureImageView, "scaleX", new float[]{scale});
                animatorArr[1] = ObjectAnimator.ofFloat(this.textureImageView, "scaleY", new float[]{scale});
                animatorArr[2] = ObjectAnimator.ofFloat(this.textureImageView, "translationX", new float[]{rect.x});
                animatorArr[3] = ObjectAnimator.ofFloat(this.textureImageView, "translationY", new float[]{rect.y});
                animatorArr[4] = ObjectAnimator.ofFloat(this.videoTextureView, "scaleX", new float[]{scale});
                animatorArr[5] = ObjectAnimator.ofFloat(this.videoTextureView, "scaleY", new float[]{scale});
                animatorArr[6] = ObjectAnimator.ofFloat(this.videoTextureView, "translationX", new float[]{rect.x - this.aspectRatioFrameLayout.getX()});
                animatorArr[7] = ObjectAnimator.ofFloat(this.videoTextureView, "translationY", new float[]{rect.y - this.aspectRatioFrameLayout.getY()});
                animatorArr[8] = ObjectAnimator.ofInt(this.backgroundDrawable, "alpha", new int[]{0});
                animatorArr[9] = ObjectAnimator.ofFloat(this.actionBar, "alpha", new float[]{0.0f});
                animatorArr[10] = ObjectAnimator.ofFloat(this.bottomLayout, "alpha", new float[]{0.0f});
                animatorArr[11] = ObjectAnimator.ofFloat(this.captionTextView, "alpha", new float[]{0.0f});
                animatorArr[12] = ObjectAnimator.ofFloat(this.groupedPhotosListView, "alpha", new float[]{0.0f});
                animatorSet.playTogether(animatorArr);
                animatorSet.setInterpolator(new DecelerateInterpolator());
                animatorSet.setDuration(250);
                animatorSet.addListener(new AnimatorListenerAdapter() {
                    public void onAnimationEnd(Animator animation) {
                        PhotoViewer.this.pipAnimationInProgress = false;
                        PhotoViewer.this.switchToInlineRunnable.run();
                    }
                });
                animatorSet.start();
                return;
            }
            this.switchToInlineRunnable.run();
            dismissInternal();
        }
    }

    public VideoPlayer getVideoPlayer() {
        return this.videoPlayer;
    }

    public void exitFromPip() {
        if (this.isInline) {
            if (Instance != null) {
                Instance.closePhoto(false, true);
            }
            Instance = PipInstance;
            PipInstance = null;
            this.switchingInlineMode = true;
            if (this.currentBitmap != null) {
                this.currentBitmap.recycle();
                this.currentBitmap = null;
            }
            this.changingTextureView = true;
            this.isInline = false;
            this.videoTextureView.setVisibility(4);
            this.aspectRatioFrameLayout.addView(this.videoTextureView);
            if (ApplicationLoader.mainInterfacePaused) {
                try {
                    this.parentActivity.startService(new Intent(ApplicationLoader.applicationContext, BringAppForegroundService.class));
                } catch (Throwable e) {
                    FileLog.e(e);
                }
            }
            if (VERSION.SDK_INT >= 21) {
                this.pipAnimationInProgress = true;
                org.telegram.ui.Components.Rect rect = PipVideoView.getPipRect(this.aspectRatioFrameLayout.getAspectRatio());
                float scale = rect.width / ((float) this.textureImageView.getLayoutParams().width);
                rect.y += (float) AndroidUtilities.statusBarHeight;
                this.textureImageView.setScaleX(scale);
                this.textureImageView.setScaleY(scale);
                this.textureImageView.setTranslationX(rect.x);
                this.textureImageView.setTranslationY(rect.y);
                this.videoTextureView.setScaleX(scale);
                this.videoTextureView.setScaleY(scale);
                this.videoTextureView.setTranslationX(rect.x - this.aspectRatioFrameLayout.getX());
                this.videoTextureView.setTranslationY(rect.y - this.aspectRatioFrameLayout.getY());
            } else {
                this.pipVideoView.close();
                this.pipVideoView = null;
            }
            try {
                this.isVisible = true;
                ((WindowManager) this.parentActivity.getSystemService("window")).addView(this.windowView, this.windowLayoutParams);
                if (this.currentPlaceObject != null) {
                    this.currentPlaceObject.imageReceiver.setVisible(false, false);
                }
            } catch (Throwable e2) {
                FileLog.e(e2);
            }
            if (VERSION.SDK_INT >= 21) {
                this.waitingForDraw = 4;
            }
        }
    }

    private void createVideoControlsInterface() {
        this.videoPlayerSeekbar = new SeekBar(this.containerView.getContext());
        this.videoPlayerSeekbar.setLineHeight(AndroidUtilities.dp(4.0f));
        this.videoPlayerSeekbar.setColors(1728053247, 1728053247, -2764585, -1, -1);
        this.videoPlayerSeekbar.setDelegate(new SeekBarDelegate() {
            public void onSeekBarDrag(float progress) {
                if (PhotoViewer.this.videoPlayer != null) {
                    if (!PhotoViewer.this.inPreview && PhotoViewer.this.videoTimelineView.getVisibility() == 0) {
                        progress = PhotoViewer.this.videoTimelineView.getLeftProgress() + ((PhotoViewer.this.videoTimelineView.getRightProgress() - PhotoViewer.this.videoTimelineView.getLeftProgress()) * progress);
                    }
                    long duration = PhotoViewer.this.videoPlayer.getDuration();
                    if (duration == C.TIME_UNSET) {
                        PhotoViewer.this.seekToProgressPending = progress;
                    } else {
                        PhotoViewer.this.videoPlayer.seekTo((long) ((int) (((float) duration) * progress)));
                    }
                }
            }
        });
        this.videoPlayerControlFrameLayout = new FrameLayout(this.containerView.getContext()) {
            public boolean onTouchEvent(MotionEvent event) {
                int x = (int) event.getX();
                int y = (int) event.getY();
                if (PhotoViewer.this.videoPlayerSeekbar.onTouch(event.getAction(), event.getX() - ((float) AndroidUtilities.dp(48.0f)), event.getY())) {
                    getParent().requestDisallowInterceptTouchEvent(true);
                    invalidate();
                }
                return true;
            }

            protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
                long duration;
                super.onMeasure(widthMeasureSpec, heightMeasureSpec);
                if (PhotoViewer.this.videoPlayer != null) {
                    duration = PhotoViewer.this.videoPlayer.getDuration();
                    if (duration == C.TIME_UNSET) {
                        duration = 0;
                    }
                } else {
                    duration = 0;
                }
                duration /= 1000;
                PhotoViewer.this.videoPlayerSeekbar.setSize((getMeasuredWidth() - AndroidUtilities.dp(64.0f)) - ((int) Math.ceil((double) PhotoViewer.this.videoPlayerTime.getPaint().measureText(String.format("%02d:%02d / %02d:%02d", new Object[]{Long.valueOf(duration / 60), Long.valueOf(duration % 60), Long.valueOf(duration / 60), Long.valueOf(duration % 60)})))), getMeasuredHeight());
            }

            protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
                super.onLayout(changed, left, top, right, bottom);
                float progress = 0.0f;
                if (PhotoViewer.this.videoPlayer != null) {
                    progress = ((float) PhotoViewer.this.videoPlayer.getCurrentPosition()) / ((float) PhotoViewer.this.videoPlayer.getDuration());
                    if (!PhotoViewer.this.inPreview && PhotoViewer.this.videoTimelineView.getVisibility() == 0) {
                        progress -= PhotoViewer.this.videoTimelineView.getLeftProgress();
                        if (progress < 0.0f) {
                            progress = 0.0f;
                        }
                        progress /= PhotoViewer.this.videoTimelineView.getRightProgress() - PhotoViewer.this.videoTimelineView.getLeftProgress();
                        if (progress > 1.0f) {
                            progress = 1.0f;
                        }
                    }
                }
                PhotoViewer.this.videoPlayerSeekbar.setProgress(progress);
                PhotoViewer.this.videoTimelineView.setProgress(progress);
            }

            protected void onDraw(Canvas canvas) {
                canvas.save();
                canvas.translate((float) AndroidUtilities.dp(48.0f), 0.0f);
                PhotoViewer.this.videoPlayerSeekbar.draw(canvas);
                canvas.restore();
            }
        };
        this.videoPlayerControlFrameLayout.setWillNotDraw(false);
        this.bottomLayout.addView(this.videoPlayerControlFrameLayout, LayoutHelper.createFrame(-1, -1, 51));
        this.videoPlayButton = new ImageView(this.containerView.getContext());
        this.videoPlayButton.setScaleType(ScaleType.CENTER);
        this.videoPlayerControlFrameLayout.addView(this.videoPlayButton, LayoutHelper.createFrame(48, 48.0f, 51, 4.0f, 0.0f, 0.0f, 0.0f));
        this.videoPlayButton.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                if (PhotoViewer.this.videoPlayer != null) {
                    if (PhotoViewer.this.isPlaying) {
                        PhotoViewer.this.videoPlayer.pause();
                    } else {
                        if (PhotoViewer.this.isCurrentVideo) {
                            if (Math.abs(PhotoViewer.this.videoTimelineView.getProgress() - 1.0f) < 0.01f || PhotoViewer.this.videoPlayer.getCurrentPosition() == PhotoViewer.this.videoPlayer.getDuration()) {
                                PhotoViewer.this.videoPlayer.seekTo(0);
                            }
                        } else if (Math.abs(PhotoViewer.this.videoPlayerSeekbar.getProgress() - 1.0f) < 0.01f || PhotoViewer.this.videoPlayer.getCurrentPosition() == PhotoViewer.this.videoPlayer.getDuration()) {
                            PhotoViewer.this.videoPlayer.seekTo(0);
                        }
                        PhotoViewer.this.videoPlayer.play();
                    }
                    PhotoViewer.this.containerView.invalidate();
                }
            }
        });
        this.videoPlayerTime = new SimpleTextView(this.containerView.getContext());
        this.videoPlayerTime.setTextColor(-1);
        this.videoPlayerTime.setGravity(53);
        this.videoPlayerTime.setTextSize(13);
        this.videoPlayerControlFrameLayout.addView(this.videoPlayerTime, LayoutHelper.createFrame(-2, -1.0f, 53, 0.0f, 17.0f, 7.0f, 0.0f));
    }

    private void openCaptionEnter() {
        if (this.imageMoveAnimation == null && this.changeModeAnimation == null && this.currentEditMode == 0) {
            this.selectedPhotosListView.setVisibility(8);
            this.selectedPhotosListView.setEnabled(false);
            this.selectedPhotosListView.setAlpha(0.0f);
            this.selectedPhotosListView.setTranslationY((float) (-AndroidUtilities.dp(10.0f)));
            this.photosCounterView.setRotationX(0.0f);
            this.isPhotosListViewVisible = false;
            this.captionEditText.setTag(Integer.valueOf(1));
            this.captionEditText.openKeyboard();
            this.lastTitle = this.actionBar.getTitle();
            if (this.isCurrentVideo) {
                this.actionBar.setTitle(this.muteVideo ? LocaleController.getString("GifCaption", R.string.GifCaption) : LocaleController.getString("VideoCaption", R.string.VideoCaption));
                this.actionBar.setSubtitle(null);
                return;
            }
            this.actionBar.setTitle(LocaleController.getString("PhotoCaption", R.string.PhotoCaption));
        }
    }

    private VideoEditedInfo getCurrentVideoEditedInfo() {
        int i = -1;
        if (!this.isCurrentVideo || this.currentPlayingVideoFile == null || this.compressionsCount == 0) {
            return null;
        }
        VideoEditedInfo videoEditedInfo = new VideoEditedInfo();
        videoEditedInfo.startTime = this.startTime;
        videoEditedInfo.endTime = this.endTime;
        videoEditedInfo.rotationValue = this.rotationValue;
        videoEditedInfo.originalWidth = this.originalWidth;
        videoEditedInfo.originalHeight = this.originalHeight;
        videoEditedInfo.bitrate = this.bitrate;
        videoEditedInfo.originalPath = this.currentPlayingVideoFile.getPath();
        videoEditedInfo.estimatedSize = (long) this.estimatedSize;
        videoEditedInfo.estimatedDuration = this.estimatedDuration;
        videoEditedInfo.framerate = this.videoFramerate;
        if (this.muteVideo || !(this.compressItem.getTag() == null || this.selectedCompression == this.compressionsCount - 1)) {
            if (this.muteVideo) {
                this.selectedCompression = 1;
                updateWidthHeightBitrateForCompression();
            }
            videoEditedInfo.resultWidth = this.resultWidth;
            videoEditedInfo.resultHeight = this.resultHeight;
            if (!this.muteVideo) {
                i = this.bitrate;
            }
            videoEditedInfo.bitrate = i;
            videoEditedInfo.muted = this.muteVideo;
            return videoEditedInfo;
        }
        videoEditedInfo.resultWidth = this.originalWidth;
        videoEditedInfo.resultHeight = this.originalHeight;
        if (!this.muteVideo) {
            i = this.originalBitrate;
        }
        videoEditedInfo.bitrate = i;
        videoEditedInfo.muted = this.muteVideo;
        return videoEditedInfo;
    }

    private void closeCaptionEnter(boolean apply) {
        CharSequence charSequence = null;
        if (this.currentIndex >= 0 && this.currentIndex < this.imagesArrLocals.size()) {
            PhotoEntry object = this.imagesArrLocals.get(this.currentIndex);
            if (apply) {
                CharSequence[] result = new CharSequence[]{this.captionEditText.getFieldCharSequence()};
                ArrayList<MessageEntity> entities = DataQuery.getInstance(this.currentAccount).getEntities(result);
                if (object instanceof PhotoEntry) {
                    PhotoEntry photoEntry = object;
                    photoEntry.caption = result[0];
                    photoEntry.entities = entities;
                } else if (object instanceof SearchImage) {
                    SearchImage photoEntry2 = (SearchImage) object;
                    photoEntry2.caption = result[0];
                    photoEntry2.entities = entities;
                }
                if (!(this.captionEditText.getFieldCharSequence().length() == 0 || this.placeProvider.isPhotoChecked(this.currentIndex))) {
                    setPhotoChecked();
                }
                setCurrentCaption(null, result[0], false);
            }
            this.captionEditText.setTag(null);
            if (this.lastTitle != null) {
                this.actionBar.setTitle(this.lastTitle);
                this.lastTitle = null;
            }
            if (this.isCurrentVideo) {
                ActionBar actionBar = this.actionBar;
                if (!this.muteVideo) {
                    charSequence = this.currentSubtitle;
                }
                actionBar.setSubtitle(charSequence);
            }
            updateCaptionTextForCurrentPhoto(object);
            if (this.captionEditText.isPopupShowing()) {
                this.captionEditText.hidePopup();
            }
            this.captionEditText.closeKeyboard();
        }
    }

    private void updateVideoPlayerTime() {
        String newText;
        if (this.videoPlayer == null) {
            newText = String.format("%02d:%02d / %02d:%02d", new Object[]{Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(0)});
        } else {
            long current = this.videoPlayer.getCurrentPosition();
            if (current < 0) {
                current = 0;
            }
            long total = this.videoPlayer.getDuration();
            if (total < 0) {
                total = 0;
            }
            if (total == C.TIME_UNSET || current == C.TIME_UNSET) {
                newText = String.format("%02d:%02d / %02d:%02d", new Object[]{Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(0)});
            } else {
                if (!this.inPreview && this.videoTimelineView.getVisibility() == 0) {
                    total = (long) (((float) total) * (this.videoTimelineView.getRightProgress() - this.videoTimelineView.getLeftProgress()));
                    current = (long) (((float) current) - (this.videoTimelineView.getLeftProgress() * ((float) total)));
                    if (current > total) {
                        current = total;
                    }
                }
                current /= 1000;
                total /= 1000;
                newText = String.format("%02d:%02d / %02d:%02d", new Object[]{Long.valueOf(current / 60), Long.valueOf(current % 60), Long.valueOf(total / 60), Long.valueOf(total % 60)});
            }
        }
        this.videoPlayerTime.setText(newText);
    }

    private void checkBufferedProgress(float progress) {
        if (this.isStreaming && this.parentActivity != null && !this.streamingAlertShown && this.videoPlayer != null && this.currentMessageObject != null) {
            Document document = this.currentMessageObject.getDocument();
            if (document != null && progress < 0.9f) {
                if ((((float) document.size) * progress >= 5242880.0f || (progress >= 0.5f && document.size >= 2097152)) && Math.abs(SystemClock.elapsedRealtime() - this.startedPlayTime) >= AdaptiveTrackSelection.DEFAULT_MIN_TIME_BETWEEN_BUFFER_REEVALUTATION_MS) {
                    if (this.videoPlayer.getDuration() == C.TIME_UNSET) {
                        Toast.makeText(this.parentActivity, LocaleController.getString("VideoDoesNotSupportStreaming", R.string.VideoDoesNotSupportStreaming), 1).show();
                    }
                    this.streamingAlertShown = true;
                }
            }
        }
    }

    private void preparePlayer(Uri uri, boolean playWhenReady, boolean preview) {
        int i = 0;
        if (!preview) {
            this.currentPlayingVideoFile = uri;
        }
        if (this.parentActivity != null) {
            this.streamingAlertShown = false;
            this.startedPlayTime = SystemClock.elapsedRealtime();
            this.currentVideoFinishedLoading = false;
            this.lastBufferedPositionCheck = 0;
            this.seekToProgressPending = 0.0f;
            this.firstAnimationDelay = true;
            this.inPreview = preview;
            releasePlayer();
            if (this.videoTextureView == null) {
                this.aspectRatioFrameLayout = new AspectRatioFrameLayout(this.parentActivity) {
                    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
                        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
                        if (PhotoViewer.this.textureImageView != null) {
                            ViewGroup.LayoutParams layoutParams = PhotoViewer.this.textureImageView.getLayoutParams();
                            layoutParams.width = getMeasuredWidth();
                            layoutParams.height = getMeasuredHeight();
                        }
                    }
                };
                this.aspectRatioFrameLayout.setVisibility(4);
                this.containerView.addView(this.aspectRatioFrameLayout, 0, LayoutHelper.createFrame(-1, -1, 17));
                this.videoTextureView = new TextureView(this.parentActivity);
                this.videoTextureView.setPivotX(0.0f);
                this.videoTextureView.setPivotY(0.0f);
                this.videoTextureView.setOpaque(false);
                this.aspectRatioFrameLayout.addView(this.videoTextureView, LayoutHelper.createFrame(-1, -1, 17));
            }
            if (VERSION.SDK_INT >= 21 && this.textureImageView == null) {
                this.textureImageView = new ImageView(this.parentActivity);
                this.textureImageView.setBackgroundColor(-65536);
                this.textureImageView.setPivotX(0.0f);
                this.textureImageView.setPivotY(0.0f);
                this.textureImageView.setVisibility(4);
                this.containerView.addView(this.textureImageView);
            }
            this.textureUploaded = false;
            this.videoCrossfadeStarted = false;
            TextureView textureView = this.videoTextureView;
            this.videoCrossfadeAlpha = 0.0f;
            textureView.setAlpha(0.0f);
            this.videoPlayButton.setImageResource(R.drawable.inline_video_play);
            if (this.videoPlayer == null) {
                this.videoPlayer = new VideoPlayer();
                this.videoPlayer.setTextureView(this.videoTextureView);
                this.videoPlayer.setDelegate(new VideoPlayerDelegate() {
                    public void onStateChanged(boolean playWhenReady, int playbackState) {
                        if (PhotoViewer.this.videoPlayer != null) {
                            if (PhotoViewer.this.isStreaming) {
                                PhotoViewer.this.toggleMiniProgress(playbackState == 2, true);
                            }
                            if (!playWhenReady || playbackState == 4 || playbackState == 1) {
                                try {
                                    PhotoViewer.this.parentActivity.getWindow().clearFlags(128);
                                    PhotoViewer.this.keepScreenOnFlagSet = false;
                                } catch (Throwable e) {
                                    FileLog.e(e);
                                }
                            } else {
                                try {
                                    PhotoViewer.this.parentActivity.getWindow().addFlags(128);
                                    PhotoViewer.this.keepScreenOnFlagSet = true;
                                } catch (Throwable e2) {
                                    FileLog.e(e2);
                                }
                            }
                            if (PhotoViewer.this.seekToProgressPending != 0.0f && (playbackState == 3 || playbackState == 1)) {
                                PhotoViewer.this.videoPlayer.seekTo((long) ((int) (((float) PhotoViewer.this.videoPlayer.getDuration()) * PhotoViewer.this.seekToProgressPending)));
                                PhotoViewer.this.seekToProgressPending = 0.0f;
                            }
                            if (playbackState == 3) {
                                if (PhotoViewer.this.aspectRatioFrameLayout.getVisibility() != 0) {
                                    PhotoViewer.this.aspectRatioFrameLayout.setVisibility(0);
                                }
                                if (!PhotoViewer.this.pipItem.isEnabled()) {
                                    PhotoViewer.this.pipAvailable = true;
                                    PhotoViewer.this.pipItem.setEnabled(true);
                                    PhotoViewer.this.pipItem.setAlpha(1.0f);
                                }
                            }
                            if (!PhotoViewer.this.videoPlayer.isPlaying() || playbackState == 4) {
                                if (PhotoViewer.this.isPlaying) {
                                    PhotoViewer.this.isPlaying = false;
                                    PhotoViewer.this.videoPlayButton.setImageResource(R.drawable.inline_video_play);
                                    AndroidUtilities.cancelRunOnUIThread(PhotoViewer.this.updateProgressRunnable);
                                    if (playbackState == 4) {
                                        if (!PhotoViewer.this.isCurrentVideo) {
                                            if (!PhotoViewer.this.isActionBarVisible) {
                                                PhotoViewer.this.toggleActionBar(true, true);
                                            }
                                            if (!PhotoViewer.this.videoPlayerSeekbar.isDragging()) {
                                                PhotoViewer.this.videoPlayerSeekbar.setProgress(0.0f);
                                                PhotoViewer.this.videoPlayerControlFrameLayout.invalidate();
                                                if (PhotoViewer.this.inPreview || PhotoViewer.this.videoTimelineView.getVisibility() != 0) {
                                                    PhotoViewer.this.videoPlayer.seekTo(0);
                                                } else {
                                                    PhotoViewer.this.videoPlayer.seekTo((long) ((int) (PhotoViewer.this.videoTimelineView.getLeftProgress() * ((float) PhotoViewer.this.videoPlayer.getDuration()))));
                                                }
                                                PhotoViewer.this.videoPlayer.pause();
                                            }
                                        } else if (!PhotoViewer.this.videoTimelineView.isDragging()) {
                                            PhotoViewer.this.videoTimelineView.setProgress(0.0f);
                                            if (PhotoViewer.this.inPreview || PhotoViewer.this.videoTimelineView.getVisibility() != 0) {
                                                PhotoViewer.this.videoPlayer.seekTo(0);
                                            } else {
                                                PhotoViewer.this.videoPlayer.seekTo((long) ((int) (PhotoViewer.this.videoTimelineView.getLeftProgress() * ((float) PhotoViewer.this.videoPlayer.getDuration()))));
                                            }
                                            PhotoViewer.this.videoPlayer.pause();
                                            PhotoViewer.this.containerView.invalidate();
                                        }
                                        if (PhotoViewer.this.pipVideoView != null) {
                                            PhotoViewer.this.pipVideoView.onVideoCompleted();
                                        }
                                    }
                                }
                            } else if (!PhotoViewer.this.isPlaying) {
                                PhotoViewer.this.isPlaying = true;
                                PhotoViewer.this.videoPlayButton.setImageResource(R.drawable.inline_video_pause);
                                AndroidUtilities.runOnUIThread(PhotoViewer.this.updateProgressRunnable);
                            }
                            if (PhotoViewer.this.pipVideoView != null) {
                                PhotoViewer.this.pipVideoView.updatePlayButton();
                            }
                            PhotoViewer.this.updateVideoPlayerTime();
                        }
                    }

                    public void onError(Exception e) {
                        FileLog.e((Throwable) e);
                        if (PhotoViewer.this.menuItem.isSubItemVisible(11)) {
                            Builder builder = new Builder(PhotoViewer.this.parentActivity);
                            builder.setTitle(LocaleController.getString("AppName", R.string.AppName));
                            builder.setMessage(LocaleController.getString("CantPlayVideo", R.string.CantPlayVideo));
                            builder.setPositiveButton(LocaleController.getString("Open", R.string.Open), new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    try {
                                        AndroidUtilities.openForView(PhotoViewer.this.currentMessageObject, PhotoViewer.this.parentActivity);
                                        PhotoViewer.this.closePhoto(false, false);
                                    } catch (Throwable e) {
                                        FileLog.e(e);
                                    }
                                }
                            });
                            builder.setNegativeButton(LocaleController.getString("Cancel", R.string.Cancel), null);
                            PhotoViewer.this.showAlertDialog(builder);
                        }
                    }

                    public void onVideoSizeChanged(int width, int height, int unappliedRotationDegrees, float pixelWidthHeightRatio) {
                        if (PhotoViewer.this.aspectRatioFrameLayout != null) {
                            if (unappliedRotationDegrees == 90 || unappliedRotationDegrees == 270) {
                                int temp = width;
                                width = height;
                                height = temp;
                            }
                            PhotoViewer.this.aspectRatioFrameLayout.setAspectRatio(height == 0 ? 1.0f : (((float) width) * pixelWidthHeightRatio) / ((float) height), unappliedRotationDegrees);
                        }
                    }

                    public void onRenderedFirstFrame() {
                        if (!PhotoViewer.this.textureUploaded) {
                            PhotoViewer.this.textureUploaded = true;
                            PhotoViewer.this.containerView.invalidate();
                        }
                    }

                    public boolean onSurfaceDestroyed(SurfaceTexture surfaceTexture) {
                        if (PhotoViewer.this.changingTextureView) {
                            PhotoViewer.this.changingTextureView = false;
                            if (PhotoViewer.this.isInline) {
                                if (PhotoViewer.this.isInline) {
                                    PhotoViewer.this.waitingForFirstTextureUpload = 1;
                                }
                                PhotoViewer.this.changedTextureView.setSurfaceTexture(surfaceTexture);
                                PhotoViewer.this.changedTextureView.setSurfaceTextureListener(PhotoViewer.this.surfaceTextureListener);
                                PhotoViewer.this.changedTextureView.setVisibility(0);
                                return true;
                            }
                        }
                        return false;
                    }

                    public void onSurfaceTextureUpdated(SurfaceTexture surfaceTexture) {
                        if (PhotoViewer.this.waitingForFirstTextureUpload == 2) {
                            if (PhotoViewer.this.textureImageView != null) {
                                PhotoViewer.this.textureImageView.setVisibility(4);
                                PhotoViewer.this.textureImageView.setImageDrawable(null);
                                if (PhotoViewer.this.currentBitmap != null) {
                                    PhotoViewer.this.currentBitmap.recycle();
                                    PhotoViewer.this.currentBitmap = null;
                                }
                            }
                            PhotoViewer.this.switchingInlineMode = false;
                            if (VERSION.SDK_INT >= 21) {
                                PhotoViewer.this.aspectRatioFrameLayout.getLocationInWindow(PhotoViewer.this.pipPosition);
                                int[] access$13900 = PhotoViewer.this.pipPosition;
                                access$13900[0] = access$13900[0] - PhotoViewer.this.getLeftInset();
                                access$13900 = PhotoViewer.this.pipPosition;
                                access$13900[1] = (int) (((float) access$13900[1]) - PhotoViewer.this.containerView.getTranslationY());
                                AnimatorSet animatorSet = new AnimatorSet();
                                Animator[] animatorArr = new Animator[13];
                                animatorArr[0] = ObjectAnimator.ofFloat(PhotoViewer.this.textureImageView, "scaleX", new float[]{1.0f});
                                animatorArr[1] = ObjectAnimator.ofFloat(PhotoViewer.this.textureImageView, "scaleY", new float[]{1.0f});
                                animatorArr[2] = ObjectAnimator.ofFloat(PhotoViewer.this.textureImageView, "translationX", new float[]{(float) PhotoViewer.this.pipPosition[0]});
                                animatorArr[3] = ObjectAnimator.ofFloat(PhotoViewer.this.textureImageView, "translationY", new float[]{(float) PhotoViewer.this.pipPosition[1]});
                                animatorArr[4] = ObjectAnimator.ofFloat(PhotoViewer.this.videoTextureView, "scaleX", new float[]{1.0f});
                                animatorArr[5] = ObjectAnimator.ofFloat(PhotoViewer.this.videoTextureView, "scaleY", new float[]{1.0f});
                                animatorArr[6] = ObjectAnimator.ofFloat(PhotoViewer.this.videoTextureView, "translationX", new float[]{((float) PhotoViewer.this.pipPosition[0]) - PhotoViewer.this.aspectRatioFrameLayout.getX()});
                                animatorArr[7] = ObjectAnimator.ofFloat(PhotoViewer.this.videoTextureView, "translationY", new float[]{((float) PhotoViewer.this.pipPosition[1]) - PhotoViewer.this.aspectRatioFrameLayout.getY()});
                                animatorArr[8] = ObjectAnimator.ofInt(PhotoViewer.this.backgroundDrawable, "alpha", new int[]{255});
                                animatorArr[9] = ObjectAnimator.ofFloat(PhotoViewer.this.actionBar, "alpha", new float[]{1.0f});
                                animatorArr[10] = ObjectAnimator.ofFloat(PhotoViewer.this.bottomLayout, "alpha", new float[]{1.0f});
                                animatorArr[11] = ObjectAnimator.ofFloat(PhotoViewer.this.captionTextView, "alpha", new float[]{1.0f});
                                animatorArr[12] = ObjectAnimator.ofFloat(PhotoViewer.this.groupedPhotosListView, "alpha", new float[]{1.0f});
                                animatorSet.playTogether(animatorArr);
                                animatorSet.setInterpolator(new DecelerateInterpolator());
                                animatorSet.setDuration(250);
                                animatorSet.addListener(new AnimatorListenerAdapter() {
                                    public void onAnimationEnd(Animator animation) {
                                        PhotoViewer.this.pipAnimationInProgress = false;
                                    }
                                });
                                animatorSet.start();
                            }
                            PhotoViewer.this.waitingForFirstTextureUpload = 0;
                        }
                    }
                });
            }
            this.videoPlayer.preparePlayer(uri, "other");
            this.videoPlayerSeekbar.setProgress(0.0f);
            this.videoTimelineView.setProgress(0.0f);
            this.videoPlayerSeekbar.setBufferedProgress(0.0f);
            if (this.currentBotInlineResult != null && (this.currentBotInlineResult.type.equals(MimeTypes.BASE_TYPE_VIDEO) || MessageObject.isVideoDocument(this.currentBotInlineResult.document))) {
                this.bottomLayout.setVisibility(0);
                this.bottomLayout.setTranslationY((float) (-AndroidUtilities.dp(48.0f)));
            }
            FrameLayout frameLayout = this.videoPlayerControlFrameLayout;
            if (this.isCurrentVideo) {
                i = 8;
            }
            frameLayout.setVisibility(i);
            this.dateTextView.setVisibility(8);
            this.nameTextView.setVisibility(8);
            if (this.allowShare) {
                this.shareButton.setVisibility(8);
                this.menuItem.showSubItem(10);
            }
            this.videoPlayer.setPlayWhenReady(playWhenReady);
            this.inPreview = preview;
        }
    }

    private void releasePlayer() {
        if (this.videoPlayer != null) {
            this.videoPlayer.releasePlayer();
            this.videoPlayer = null;
        }
        toggleMiniProgress(false, false);
        this.pipAvailable = false;
        if (this.pipItem.isEnabled()) {
            this.pipItem.setEnabled(false);
            this.pipItem.setAlpha(0.5f);
        }
        if (this.keepScreenOnFlagSet) {
            try {
                this.parentActivity.getWindow().clearFlags(128);
                this.keepScreenOnFlagSet = false;
            } catch (Throwable e) {
                FileLog.e(e);
            }
        }
        if (this.aspectRatioFrameLayout != null) {
            this.containerView.removeView(this.aspectRatioFrameLayout);
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
        if (!this.inPreview && !this.requestingPreview) {
            this.videoPlayerControlFrameLayout.setVisibility(8);
            this.dateTextView.setVisibility(0);
            this.nameTextView.setVisibility(0);
            if (this.allowShare) {
                this.shareButton.setVisibility(0);
                this.menuItem.hideSubItem(10);
            }
        }
    }

    private void updateCaptionTextForCurrentPhoto(Object object) {
        CharSequence caption = null;
        if (object instanceof PhotoEntry) {
            caption = ((PhotoEntry) object).caption;
        } else if (!(object instanceof BotInlineResult) && (object instanceof SearchImage)) {
            caption = ((SearchImage) object).caption;
        }
        if (TextUtils.isEmpty(caption)) {
            this.captionEditText.setFieldText(TtmlNode.ANONYMOUS_REGION_ID);
        } else {
            this.captionEditText.setFieldText(caption);
        }
    }

    public void showAlertDialog(Builder builder) {
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
                this.visibleDialog = builder.show();
                this.visibleDialog.setCanceledOnTouchOutside(true);
                this.visibleDialog.setOnDismissListener(new OnDismissListener() {
                    public void onDismiss(DialogInterface dialog) {
                        PhotoViewer.this.visibleDialog = null;
                    }
                });
            } catch (Throwable e2) {
                FileLog.e(e2);
            }
        }
    }

    private void applyCurrentEditMode() {
        Bitmap bitmap = null;
        ArrayList<InputDocument> stickers = null;
        SavedFilterState savedFilterState = null;
        boolean removeSavedState = false;
        if (this.currentEditMode == 1) {
            bitmap = this.photoCropView.getBitmap();
            removeSavedState = true;
        } else if (this.currentEditMode == 2) {
            bitmap = this.photoFilterView.getBitmap();
            savedFilterState = this.photoFilterView.getSavedFilterState();
        } else if (this.currentEditMode == 3) {
            bitmap = this.photoPaintView.getBitmap();
            stickers = this.photoPaintView.getMasks();
            removeSavedState = true;
        }
        if (bitmap != null) {
            PhotoSize size = ImageLoader.scaleAndSaveImage(bitmap, (float) AndroidUtilities.getPhotoSize(), (float) AndroidUtilities.getPhotoSize(), 80, false, 101, 101);
            if (size != null) {
                PhotoEntry object = this.imagesArrLocals.get(this.currentIndex);
                if (object instanceof PhotoEntry) {
                    PhotoEntry entry = object;
                    entry.imagePath = FileLoader.getPathToAttach(size, true).toString();
                    size = ImageLoader.scaleAndSaveImage(bitmap, (float) AndroidUtilities.dp(120.0f), (float) AndroidUtilities.dp(120.0f), 70, false, 101, 101);
                    if (size != null) {
                        entry.thumbPath = FileLoader.getPathToAttach(size, true).toString();
                    }
                    if (stickers != null) {
                        entry.stickers.addAll(stickers);
                    }
                    if (this.currentEditMode == 1) {
                        this.cropItem.setColorFilter(new PorterDuffColorFilter(-12734994, Mode.MULTIPLY));
                        entry.isCropped = true;
                    } else if (this.currentEditMode == 2) {
                        this.tuneItem.setColorFilter(new PorterDuffColorFilter(-12734994, Mode.MULTIPLY));
                        entry.isFiltered = true;
                    } else if (this.currentEditMode == 3) {
                        this.paintItem.setColorFilter(new PorterDuffColorFilter(-12734994, Mode.MULTIPLY));
                        entry.isPainted = true;
                    }
                    if (savedFilterState != null) {
                        entry.savedFilterState = savedFilterState;
                    } else if (removeSavedState) {
                        entry.savedFilterState = null;
                    }
                } else if (object instanceof SearchImage) {
                    SearchImage entry2 = (SearchImage) object;
                    entry2.imagePath = FileLoader.getPathToAttach(size, true).toString();
                    size = ImageLoader.scaleAndSaveImage(bitmap, (float) AndroidUtilities.dp(120.0f), (float) AndroidUtilities.dp(120.0f), 70, false, 101, 101);
                    if (size != null) {
                        entry2.thumbPath = FileLoader.getPathToAttach(size, true).toString();
                    }
                    if (stickers != null) {
                        entry2.stickers.addAll(stickers);
                    }
                    if (this.currentEditMode == 1) {
                        this.cropItem.setColorFilter(new PorterDuffColorFilter(-12734994, Mode.MULTIPLY));
                        entry2.isCropped = true;
                    } else if (this.currentEditMode == 2) {
                        this.tuneItem.setColorFilter(new PorterDuffColorFilter(-12734994, Mode.MULTIPLY));
                        entry2.isFiltered = true;
                    } else if (this.currentEditMode == 3) {
                        this.paintItem.setColorFilter(new PorterDuffColorFilter(-12734994, Mode.MULTIPLY));
                        entry2.isPainted = true;
                    }
                    if (savedFilterState != null) {
                        entry2.savedFilterState = savedFilterState;
                    } else if (removeSavedState) {
                        entry2.savedFilterState = null;
                    }
                }
                if ((this.sendPhotoType == 0 || this.sendPhotoType == 4) && this.placeProvider != null) {
                    this.placeProvider.updatePhotoAtIndex(this.currentIndex);
                    if (!this.placeProvider.isPhotoChecked(this.currentIndex)) {
                        setPhotoChecked();
                    }
                }
                if (this.currentEditMode == 1) {
                    float scaleX = this.photoCropView.getRectSizeX() / ((float) getContainerViewWidth());
                    float scaleY = this.photoCropView.getRectSizeY() / ((float) getContainerViewHeight());
                    if (scaleX <= scaleY) {
                        scaleX = scaleY;
                    }
                    this.scale = scaleX;
                    this.translationX = (this.photoCropView.getRectX() + (this.photoCropView.getRectSizeX() / 2.0f)) - ((float) (getContainerViewWidth() / 2));
                    this.translationY = (this.photoCropView.getRectY() + (this.photoCropView.getRectSizeY() / 2.0f)) - ((float) (getContainerViewHeight() / 2));
                    this.zoomAnimation = true;
                    this.applying = true;
                    this.photoCropView.onDisappear();
                }
                this.centerImage.setParentView(null);
                this.centerImage.setOrientation(0, true);
                this.ignoreDidSetImage = true;
                this.centerImage.setImageBitmap(bitmap);
                this.ignoreDidSetImage = false;
                this.centerImage.setParentView(this.containerView);
            }
        }
    }

    private void setPhotoChecked() {
        if (this.placeProvider == null) {
            return;
        }
        if (this.placeProvider.getSelectedPhotos() == null || this.maxSelectedPhotos < 0 || this.placeProvider.getSelectedPhotos().size() < this.maxSelectedPhotos || this.placeProvider.isPhotoChecked(this.currentIndex)) {
            int num = this.placeProvider.setPhotoChecked(this.currentIndex, getCurrentVideoEditedInfo());
            boolean checked = this.placeProvider.isPhotoChecked(this.currentIndex);
            this.checkImageView.setChecked(checked, true);
            if (num >= 0) {
                if (this.placeProvider.allowGroupPhotos()) {
                    num++;
                }
                if (checked) {
                    this.selectedPhotosAdapter.notifyItemInserted(num);
                    this.selectedPhotosListView.smoothScrollToPosition(num);
                } else {
                    this.selectedPhotosAdapter.notifyItemRemoved(num);
                }
            }
            updateSelectedCount();
        }
    }

    private void switchToEditMode(int mode) {
        if (this.currentEditMode == mode || this.centerImage.getBitmap() == null || this.changeModeAnimation != null || this.imageMoveAnimation != null || this.photoProgressViews[0].backgroundState != -1 || this.captionEditText.getTag() != null) {
            return;
        }
        final int i;
        if (mode == 0) {
            if (this.centerImage.getBitmap() != null) {
                float scale;
                float newScale;
                int bitmapWidth = this.centerImage.getBitmapWidth();
                int bitmapHeight = this.centerImage.getBitmapHeight();
                float scaleX = ((float) getContainerViewWidth()) / ((float) bitmapWidth);
                float scaleY = ((float) getContainerViewHeight()) / ((float) bitmapHeight);
                float newScaleX = ((float) getContainerViewWidth(0)) / ((float) bitmapWidth);
                float newScaleY = ((float) getContainerViewHeight(0)) / ((float) bitmapHeight);
                if (scaleX > scaleY) {
                    scale = scaleY;
                } else {
                    scale = scaleX;
                }
                if (newScaleX > newScaleY) {
                    newScale = newScaleY;
                } else {
                    newScale = newScaleX;
                }
                if (this.sendPhotoType != 1 || this.applying) {
                    this.animateToScale = newScale / scale;
                } else {
                    float fillScale;
                    float minSide = (float) Math.min(getContainerViewWidth(), getContainerViewHeight());
                    scaleX = minSide / ((float) bitmapWidth);
                    scaleY = minSide / ((float) bitmapHeight);
                    if (scaleX > scaleY) {
                        fillScale = scaleX;
                    } else {
                        fillScale = scaleY;
                    }
                    this.scale = fillScale / scale;
                    this.animateToScale = (this.scale * newScale) / fillScale;
                }
                this.animateToX = 0.0f;
                if (this.currentEditMode == 1) {
                    this.animateToY = (float) AndroidUtilities.dp(58.0f);
                } else if (this.currentEditMode == 2) {
                    this.animateToY = (float) AndroidUtilities.dp(92.0f);
                } else if (this.currentEditMode == 3) {
                    this.animateToY = (float) AndroidUtilities.dp(44.0f);
                }
                if (VERSION.SDK_INT >= 21) {
                    this.animateToY -= (float) (AndroidUtilities.statusBarHeight / 2);
                }
                this.animationStartTime = System.currentTimeMillis();
                this.zoomAnimation = true;
            }
            this.imageMoveAnimation = new AnimatorSet();
            AnimatorSet animatorSet;
            Animator[] animatorArr;
            if (this.currentEditMode == 1) {
                animatorSet = this.imageMoveAnimation;
                animatorArr = new Animator[3];
                animatorArr[0] = ObjectAnimator.ofFloat(this.editorDoneLayout, "translationY", new float[]{(float) AndroidUtilities.dp(48.0f)});
                animatorArr[1] = ObjectAnimator.ofFloat(this, "animationValue", new float[]{0.0f, 1.0f});
                animatorArr[2] = ObjectAnimator.ofFloat(this.photoCropView, "alpha", new float[]{0.0f});
                animatorSet.playTogether(animatorArr);
            } else if (this.currentEditMode == 2) {
                this.photoFilterView.shutdown();
                animatorSet = this.imageMoveAnimation;
                animatorArr = new Animator[2];
                animatorArr[0] = ObjectAnimator.ofFloat(this.photoFilterView.getToolsView(), "translationY", new float[]{(float) AndroidUtilities.dp(186.0f)});
                animatorArr[1] = ObjectAnimator.ofFloat(this, "animationValue", new float[]{0.0f, 1.0f});
                animatorSet.playTogether(animatorArr);
            } else if (this.currentEditMode == 3) {
                this.photoPaintView.shutdown();
                animatorSet = this.imageMoveAnimation;
                animatorArr = new Animator[3];
                animatorArr[0] = ObjectAnimator.ofFloat(this.photoPaintView.getToolsView(), "translationY", new float[]{(float) AndroidUtilities.dp(126.0f)});
                animatorArr[1] = ObjectAnimator.ofFloat(this.photoPaintView.getColorPicker(), "translationY", new float[]{(float) AndroidUtilities.dp(126.0f)});
                animatorArr[2] = ObjectAnimator.ofFloat(this, "animationValue", new float[]{0.0f, 1.0f});
                animatorSet.playTogether(animatorArr);
            }
            this.imageMoveAnimation.setDuration(200);
            i = mode;
            this.imageMoveAnimation.addListener(new AnimatorListenerAdapter() {
                public void onAnimationEnd(Animator animation) {
                    if (PhotoViewer.this.currentEditMode == 1) {
                        PhotoViewer.this.editorDoneLayout.setVisibility(8);
                        PhotoViewer.this.photoCropView.setVisibility(8);
                    } else if (PhotoViewer.this.currentEditMode == 2) {
                        PhotoViewer.this.containerView.removeView(PhotoViewer.this.photoFilterView);
                        PhotoViewer.this.photoFilterView = null;
                    } else if (PhotoViewer.this.currentEditMode == 3) {
                        PhotoViewer.this.containerView.removeView(PhotoViewer.this.photoPaintView);
                        PhotoViewer.this.photoPaintView = null;
                    }
                    PhotoViewer.this.imageMoveAnimation = null;
                    PhotoViewer.this.currentEditMode = i;
                    PhotoViewer.this.applying = false;
                    PhotoViewer.this.animateToScale = 1.0f;
                    PhotoViewer.this.animateToX = 0.0f;
                    PhotoViewer.this.animateToY = 0.0f;
                    PhotoViewer.this.scale = 1.0f;
                    PhotoViewer.this.updateMinMax(PhotoViewer.this.scale);
                    PhotoViewer.this.containerView.invalidate();
                    AnimatorSet animatorSet = new AnimatorSet();
                    ArrayList<Animator> arrayList = new ArrayList();
                    arrayList.add(ObjectAnimator.ofFloat(PhotoViewer.this.pickerView, "translationY", new float[]{0.0f}));
                    arrayList.add(ObjectAnimator.ofFloat(PhotoViewer.this.pickerViewSendButton, "translationY", new float[]{0.0f}));
                    arrayList.add(ObjectAnimator.ofFloat(PhotoViewer.this.actionBar, "translationY", new float[]{0.0f}));
                    if (PhotoViewer.this.needCaptionLayout) {
                        arrayList.add(ObjectAnimator.ofFloat(PhotoViewer.this.captionTextView, "translationY", new float[]{0.0f}));
                    }
                    if (PhotoViewer.this.sendPhotoType == 0 || PhotoViewer.this.sendPhotoType == 4) {
                        arrayList.add(ObjectAnimator.ofFloat(PhotoViewer.this.checkImageView, "alpha", new float[]{1.0f}));
                        arrayList.add(ObjectAnimator.ofFloat(PhotoViewer.this.photosCounterView, "alpha", new float[]{1.0f}));
                    }
                    if (PhotoViewer.this.cameraItem.getTag() != null) {
                        PhotoViewer.this.cameraItem.setVisibility(0);
                        arrayList.add(ObjectAnimator.ofFloat(PhotoViewer.this.cameraItem, "alpha", new float[]{1.0f}));
                    }
                    animatorSet.playTogether(arrayList);
                    animatorSet.setDuration(200);
                    animatorSet.addListener(new AnimatorListenerAdapter() {
                        public void onAnimationStart(Animator animation) {
                            PhotoViewer.this.pickerView.setVisibility(0);
                            PhotoViewer.this.pickerViewSendButton.setVisibility(0);
                            PhotoViewer.this.actionBar.setVisibility(0);
                            if (PhotoViewer.this.needCaptionLayout) {
                                PhotoViewer.this.captionTextView.setVisibility(PhotoViewer.this.captionTextView.getTag() != null ? 0 : 4);
                            }
                            if (PhotoViewer.this.sendPhotoType == 0 || PhotoViewer.this.sendPhotoType == 4 || ((PhotoViewer.this.sendPhotoType == 2 || PhotoViewer.this.sendPhotoType == 5) && PhotoViewer.this.imagesArrLocals.size() > 1)) {
                                PhotoViewer.this.checkImageView.setVisibility(0);
                                PhotoViewer.this.photosCounterView.setVisibility(0);
                            }
                        }
                    });
                    animatorSet.start();
                }
            });
            this.imageMoveAnimation.start();
        } else if (mode == 1) {
            if (this.photoCropView == null) {
                this.photoCropView = new PhotoCropView(this.actvityContext);
                this.photoCropView.setVisibility(8);
                FrameLayoutDrawer frameLayoutDrawer = this.containerView;
                frameLayoutDrawer.addView(this.photoCropView, LayoutHelper.createFrame(-1, -1.0f, 51, 0.0f, 0.0f, 0.0f, 48.0f));
                this.photoCropView.setDelegate(new PhotoCropViewDelegate() {
                    public void needMoveImageTo(float x, float y, float s, boolean animated) {
                        if (animated) {
                            PhotoViewer.this.animateTo(s, x, y, true);
                            return;
                        }
                        PhotoViewer.this.translationX = x;
                        PhotoViewer.this.translationY = y;
                        PhotoViewer.this.scale = s;
                        PhotoViewer.this.containerView.invalidate();
                    }

                    public Bitmap getBitmap() {
                        return PhotoViewer.this.centerImage.getBitmap();
                    }

                    public void onChange(boolean reset) {
                        PhotoViewer.this.resetButton.setVisibility(reset ? 8 : 0);
                    }
                });
            }
            this.photoCropView.onAppear();
            this.editorDoneLayout.doneButton.setText(LocaleController.getString("Crop", R.string.Crop));
            this.editorDoneLayout.doneButton.setTextColor(-11420173);
            this.changeModeAnimation = new AnimatorSet();
            arrayList = new ArrayList();
            arrayList.add(ObjectAnimator.ofFloat(this.pickerView, "translationY", new float[]{0.0f, (float) AndroidUtilities.dp(96.0f)}));
            arrayList.add(ObjectAnimator.ofFloat(this.pickerViewSendButton, "translationY", new float[]{0.0f, (float) AndroidUtilities.dp(96.0f)}));
            arrayList.add(ObjectAnimator.ofFloat(this.actionBar, "translationY", new float[]{0.0f, (float) (-this.actionBar.getHeight())}));
            if (this.needCaptionLayout) {
                arrayList.add(ObjectAnimator.ofFloat(this.captionTextView, "translationY", new float[]{0.0f, (float) AndroidUtilities.dp(96.0f)}));
            }
            if (this.sendPhotoType == 0 || this.sendPhotoType == 4) {
                arrayList.add(ObjectAnimator.ofFloat(this.checkImageView, "alpha", new float[]{1.0f, 0.0f}));
                arrayList.add(ObjectAnimator.ofFloat(this.photosCounterView, "alpha", new float[]{1.0f, 0.0f}));
            }
            if (this.selectedPhotosListView.getVisibility() == 0) {
                arrayList.add(ObjectAnimator.ofFloat(this.selectedPhotosListView, "alpha", new float[]{1.0f, 0.0f}));
            }
            if (this.cameraItem.getTag() != null) {
                arrayList.add(ObjectAnimator.ofFloat(this.cameraItem, "alpha", new float[]{1.0f, 0.0f}));
            }
            this.changeModeAnimation.playTogether(arrayList);
            this.changeModeAnimation.setDuration(200);
            i = mode;
            this.changeModeAnimation.addListener(new AnimatorListenerAdapter() {
                public void onAnimationEnd(Animator animation) {
                    PhotoViewer.this.changeModeAnimation = null;
                    PhotoViewer.this.pickerView.setVisibility(8);
                    PhotoViewer.this.pickerViewSendButton.setVisibility(8);
                    PhotoViewer.this.cameraItem.setVisibility(8);
                    PhotoViewer.this.selectedPhotosListView.setVisibility(8);
                    PhotoViewer.this.selectedPhotosListView.setAlpha(0.0f);
                    PhotoViewer.this.selectedPhotosListView.setTranslationY((float) (-AndroidUtilities.dp(10.0f)));
                    PhotoViewer.this.photosCounterView.setRotationX(0.0f);
                    PhotoViewer.this.selectedPhotosListView.setEnabled(false);
                    PhotoViewer.this.isPhotosListViewVisible = false;
                    if (PhotoViewer.this.needCaptionLayout) {
                        PhotoViewer.this.captionTextView.setVisibility(4);
                    }
                    if (PhotoViewer.this.sendPhotoType == 0 || PhotoViewer.this.sendPhotoType == 4 || ((PhotoViewer.this.sendPhotoType == 2 || PhotoViewer.this.sendPhotoType == 5) && PhotoViewer.this.imagesArrLocals.size() > 1)) {
                        PhotoViewer.this.checkImageView.setVisibility(8);
                        PhotoViewer.this.photosCounterView.setVisibility(8);
                    }
                    Bitmap bitmap = PhotoViewer.this.centerImage.getBitmap();
                    if (bitmap != null) {
                        float scale;
                        float newScale;
                        PhotoViewer.this.photoCropView.setBitmap(bitmap, PhotoViewer.this.centerImage.getOrientation(), PhotoViewer.this.sendPhotoType != 1);
                        int bitmapWidth = PhotoViewer.this.centerImage.getBitmapWidth();
                        int bitmapHeight = PhotoViewer.this.centerImage.getBitmapHeight();
                        float scaleX = ((float) PhotoViewer.this.getContainerViewWidth()) / ((float) bitmapWidth);
                        float scaleY = ((float) PhotoViewer.this.getContainerViewHeight()) / ((float) bitmapHeight);
                        float newScaleX = ((float) PhotoViewer.this.getContainerViewWidth(1)) / ((float) bitmapWidth);
                        float newScaleY = ((float) PhotoViewer.this.getContainerViewHeight(1)) / ((float) bitmapHeight);
                        if (scaleX > scaleY) {
                            scale = scaleY;
                        } else {
                            scale = scaleX;
                        }
                        if (newScaleX > newScaleY) {
                            newScale = newScaleY;
                        } else {
                            newScale = newScaleX;
                        }
                        if (PhotoViewer.this.sendPhotoType == 1) {
                            float minSide = (float) Math.min(PhotoViewer.this.getContainerViewWidth(1), PhotoViewer.this.getContainerViewHeight(1));
                            newScaleX = minSide / ((float) bitmapWidth);
                            newScaleY = minSide / ((float) bitmapHeight);
                            if (newScaleX > newScaleY) {
                                newScale = newScaleX;
                            } else {
                                newScale = newScaleY;
                            }
                        }
                        PhotoViewer.this.animateToScale = newScale / scale;
                        PhotoViewer.this.animateToX = 0.0f;
                        PhotoViewer.this.animateToY = (float) ((VERSION.SDK_INT >= 21 ? AndroidUtilities.statusBarHeight / 2 : 0) + (-AndroidUtilities.dp(56.0f)));
                        PhotoViewer.this.animationStartTime = System.currentTimeMillis();
                        PhotoViewer.this.zoomAnimation = true;
                    }
                    PhotoViewer.this.imageMoveAnimation = new AnimatorSet();
                    AnimatorSet access$14600 = PhotoViewer.this.imageMoveAnimation;
                    r13 = new Animator[3];
                    r13[0] = ObjectAnimator.ofFloat(PhotoViewer.this.editorDoneLayout, "translationY", new float[]{(float) AndroidUtilities.dp(48.0f), 0.0f});
                    float[] fArr = new float[2];
                    r13[1] = ObjectAnimator.ofFloat(PhotoViewer.this, "animationValue", new float[]{0.0f, 1.0f});
                    fArr = new float[2];
                    r13[2] = ObjectAnimator.ofFloat(PhotoViewer.this.photoCropView, "alpha", new float[]{0.0f, 1.0f});
                    access$14600.playTogether(r13);
                    PhotoViewer.this.imageMoveAnimation.setDuration(200);
                    PhotoViewer.this.imageMoveAnimation.addListener(new AnimatorListenerAdapter() {
                        public void onAnimationStart(Animator animation) {
                            PhotoViewer.this.editorDoneLayout.setVisibility(0);
                            PhotoViewer.this.photoCropView.setVisibility(0);
                        }

                        public void onAnimationEnd(Animator animation) {
                            PhotoViewer.this.photoCropView.onAppeared();
                            PhotoViewer.this.imageMoveAnimation = null;
                            PhotoViewer.this.currentEditMode = i;
                            PhotoViewer.this.animateToScale = 1.0f;
                            PhotoViewer.this.animateToX = 0.0f;
                            PhotoViewer.this.animateToY = 0.0f;
                            PhotoViewer.this.scale = 1.0f;
                            PhotoViewer.this.updateMinMax(PhotoViewer.this.scale);
                            PhotoViewer.this.containerView.invalidate();
                        }
                    });
                    PhotoViewer.this.imageMoveAnimation.start();
                }
            });
            this.changeModeAnimation.start();
        } else if (mode == 2) {
            if (this.photoFilterView == null) {
                Bitmap bitmap;
                SavedFilterState state = null;
                String originalPath = null;
                int orientation = 0;
                if (!this.imagesArrLocals.isEmpty()) {
                    PhotoEntry object = this.imagesArrLocals.get(this.currentIndex);
                    if (object instanceof PhotoEntry) {
                        PhotoEntry entry = object;
                        if (entry.imagePath == null) {
                            originalPath = entry.path;
                            state = entry.savedFilterState;
                        }
                        orientation = entry.orientation;
                    } else if (object instanceof SearchImage) {
                        SearchImage entry2 = (SearchImage) object;
                        state = entry2.savedFilterState;
                        originalPath = entry2.imageUrl;
                    }
                }
                if (state == null) {
                    bitmap = this.centerImage.getBitmap();
                    orientation = this.centerImage.getOrientation();
                } else {
                    bitmap = BitmapFactory.decodeFile(originalPath);
                }
                this.photoFilterView = new PhotoFilterView(this.parentActivity, bitmap, orientation, state);
                this.containerView.addView(this.photoFilterView, LayoutHelper.createFrame(-1, -1.0f));
                this.photoFilterView.getDoneTextView().setOnClickListener(new OnClickListener() {
                    public void onClick(View v) {
                        PhotoViewer.this.applyCurrentEditMode();
                        PhotoViewer.this.switchToEditMode(0);
                    }
                });
                this.photoFilterView.getCancelTextView().setOnClickListener(new OnClickListener() {
                    public void onClick(View v) {
                        if (!PhotoViewer.this.photoFilterView.hasChanges()) {
                            PhotoViewer.this.switchToEditMode(0);
                        } else if (PhotoViewer.this.parentActivity != null) {
                            Builder builder = new Builder(PhotoViewer.this.parentActivity);
                            builder.setMessage(LocaleController.getString("DiscardChanges", R.string.DiscardChanges));
                            builder.setTitle(LocaleController.getString("AppName", R.string.AppName));
                            builder.setPositiveButton(LocaleController.getString("OK", R.string.OK), new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    PhotoViewer.this.switchToEditMode(0);
                                }
                            });
                            builder.setNegativeButton(LocaleController.getString("Cancel", R.string.Cancel), null);
                            PhotoViewer.this.showAlertDialog(builder);
                        }
                    }
                });
                this.photoFilterView.getToolsView().setTranslationY((float) AndroidUtilities.dp(186.0f));
            }
            this.changeModeAnimation = new AnimatorSet();
            arrayList = new ArrayList();
            arrayList.add(ObjectAnimator.ofFloat(this.pickerView, "translationY", new float[]{0.0f, (float) AndroidUtilities.dp(96.0f)}));
            arrayList.add(ObjectAnimator.ofFloat(this.pickerViewSendButton, "translationY", new float[]{0.0f, (float) AndroidUtilities.dp(96.0f)}));
            arrayList.add(ObjectAnimator.ofFloat(this.actionBar, "translationY", new float[]{0.0f, (float) (-this.actionBar.getHeight())}));
            if (this.sendPhotoType == 0 || this.sendPhotoType == 4) {
                arrayList.add(ObjectAnimator.ofFloat(this.checkImageView, "alpha", new float[]{1.0f, 0.0f}));
                arrayList.add(ObjectAnimator.ofFloat(this.photosCounterView, "alpha", new float[]{1.0f, 0.0f}));
            }
            if (this.selectedPhotosListView.getVisibility() == 0) {
                arrayList.add(ObjectAnimator.ofFloat(this.selectedPhotosListView, "alpha", new float[]{1.0f, 0.0f}));
            }
            if (this.cameraItem.getTag() != null) {
                arrayList.add(ObjectAnimator.ofFloat(this.cameraItem, "alpha", new float[]{1.0f, 0.0f}));
            }
            this.changeModeAnimation.playTogether(arrayList);
            this.changeModeAnimation.setDuration(200);
            i = mode;
            this.changeModeAnimation.addListener(new AnimatorListenerAdapter() {
                public void onAnimationEnd(Animator animation) {
                    PhotoViewer.this.changeModeAnimation = null;
                    PhotoViewer.this.pickerView.setVisibility(8);
                    PhotoViewer.this.pickerViewSendButton.setVisibility(8);
                    PhotoViewer.this.actionBar.setVisibility(8);
                    PhotoViewer.this.cameraItem.setVisibility(8);
                    PhotoViewer.this.selectedPhotosListView.setVisibility(8);
                    PhotoViewer.this.selectedPhotosListView.setAlpha(0.0f);
                    PhotoViewer.this.selectedPhotosListView.setTranslationY((float) (-AndroidUtilities.dp(10.0f)));
                    PhotoViewer.this.photosCounterView.setRotationX(0.0f);
                    PhotoViewer.this.selectedPhotosListView.setEnabled(false);
                    PhotoViewer.this.isPhotosListViewVisible = false;
                    if (PhotoViewer.this.needCaptionLayout) {
                        PhotoViewer.this.captionTextView.setVisibility(4);
                    }
                    if (PhotoViewer.this.sendPhotoType == 0 || PhotoViewer.this.sendPhotoType == 4 || ((PhotoViewer.this.sendPhotoType == 2 || PhotoViewer.this.sendPhotoType == 5) && PhotoViewer.this.imagesArrLocals.size() > 1)) {
                        PhotoViewer.this.checkImageView.setVisibility(8);
                        PhotoViewer.this.photosCounterView.setVisibility(8);
                    }
                    if (PhotoViewer.this.centerImage.getBitmap() != null) {
                        float scale;
                        float newScale;
                        int bitmapWidth = PhotoViewer.this.centerImage.getBitmapWidth();
                        int bitmapHeight = PhotoViewer.this.centerImage.getBitmapHeight();
                        float scaleX = ((float) PhotoViewer.this.getContainerViewWidth()) / ((float) bitmapWidth);
                        float scaleY = ((float) PhotoViewer.this.getContainerViewHeight()) / ((float) bitmapHeight);
                        float newScaleX = ((float) PhotoViewer.this.getContainerViewWidth(2)) / ((float) bitmapWidth);
                        float newScaleY = ((float) PhotoViewer.this.getContainerViewHeight(2)) / ((float) bitmapHeight);
                        if (scaleX > scaleY) {
                            scale = scaleY;
                        } else {
                            scale = scaleX;
                        }
                        if (newScaleX > newScaleY) {
                            newScale = newScaleY;
                        } else {
                            newScale = newScaleX;
                        }
                        PhotoViewer.this.animateToScale = newScale / scale;
                        PhotoViewer.this.animateToX = 0.0f;
                        PhotoViewer.this.animateToY = (float) ((VERSION.SDK_INT >= 21 ? AndroidUtilities.statusBarHeight / 2 : 0) + (-AndroidUtilities.dp(92.0f)));
                        PhotoViewer.this.animationStartTime = System.currentTimeMillis();
                        PhotoViewer.this.zoomAnimation = true;
                    }
                    PhotoViewer.this.imageMoveAnimation = new AnimatorSet();
                    AnimatorSet access$14600 = PhotoViewer.this.imageMoveAnimation;
                    r12 = new Animator[2];
                    float[] fArr = new float[2];
                    r12[0] = ObjectAnimator.ofFloat(PhotoViewer.this, "animationValue", new float[]{0.0f, 1.0f});
                    r12[1] = ObjectAnimator.ofFloat(PhotoViewer.this.photoFilterView.getToolsView(), "translationY", new float[]{(float) AndroidUtilities.dp(186.0f), 0.0f});
                    access$14600.playTogether(r12);
                    PhotoViewer.this.imageMoveAnimation.setDuration(200);
                    PhotoViewer.this.imageMoveAnimation.addListener(new AnimatorListenerAdapter() {
                        public void onAnimationStart(Animator animation) {
                        }

                        public void onAnimationEnd(Animator animation) {
                            PhotoViewer.this.photoFilterView.init();
                            PhotoViewer.this.imageMoveAnimation = null;
                            PhotoViewer.this.currentEditMode = i;
                            PhotoViewer.this.animateToScale = 1.0f;
                            PhotoViewer.this.animateToX = 0.0f;
                            PhotoViewer.this.animateToY = 0.0f;
                            PhotoViewer.this.scale = 1.0f;
                            PhotoViewer.this.updateMinMax(PhotoViewer.this.scale);
                            PhotoViewer.this.containerView.invalidate();
                        }
                    });
                    PhotoViewer.this.imageMoveAnimation.start();
                }
            });
            this.changeModeAnimation.start();
        } else if (mode == 3) {
            if (this.photoPaintView == null) {
                this.photoPaintView = new PhotoPaintView(this.parentActivity, this.centerImage.getBitmap(), this.centerImage.getOrientation());
                this.containerView.addView(this.photoPaintView, LayoutHelper.createFrame(-1, -1.0f));
                this.photoPaintView.getDoneTextView().setOnClickListener(new OnClickListener() {
                    public void onClick(View v) {
                        PhotoViewer.this.applyCurrentEditMode();
                        PhotoViewer.this.switchToEditMode(0);
                    }
                });
                this.photoPaintView.getCancelTextView().setOnClickListener(new OnClickListener() {
                    public void onClick(View v) {
                        PhotoViewer.this.photoPaintView.maybeShowDismissalAlert(PhotoViewer.this, PhotoViewer.this.parentActivity, new Runnable() {
                            public void run() {
                                PhotoViewer.this.switchToEditMode(0);
                            }
                        });
                    }
                });
                this.photoPaintView.getColorPicker().setTranslationY((float) AndroidUtilities.dp(126.0f));
                this.photoPaintView.getToolsView().setTranslationY((float) AndroidUtilities.dp(126.0f));
            }
            this.changeModeAnimation = new AnimatorSet();
            arrayList = new ArrayList();
            arrayList.add(ObjectAnimator.ofFloat(this.pickerView, "translationY", new float[]{0.0f, (float) AndroidUtilities.dp(96.0f)}));
            arrayList.add(ObjectAnimator.ofFloat(this.pickerViewSendButton, "translationY", new float[]{0.0f, (float) AndroidUtilities.dp(96.0f)}));
            arrayList.add(ObjectAnimator.ofFloat(this.actionBar, "translationY", new float[]{0.0f, (float) (-this.actionBar.getHeight())}));
            if (this.needCaptionLayout) {
                arrayList.add(ObjectAnimator.ofFloat(this.captionTextView, "translationY", new float[]{0.0f, (float) AndroidUtilities.dp(96.0f)}));
            }
            if (this.sendPhotoType == 0 || this.sendPhotoType == 4) {
                arrayList.add(ObjectAnimator.ofFloat(this.checkImageView, "alpha", new float[]{1.0f, 0.0f}));
                arrayList.add(ObjectAnimator.ofFloat(this.photosCounterView, "alpha", new float[]{1.0f, 0.0f}));
            }
            if (this.selectedPhotosListView.getVisibility() == 0) {
                arrayList.add(ObjectAnimator.ofFloat(this.selectedPhotosListView, "alpha", new float[]{1.0f, 0.0f}));
            }
            if (this.cameraItem.getTag() != null) {
                arrayList.add(ObjectAnimator.ofFloat(this.cameraItem, "alpha", new float[]{1.0f, 0.0f}));
            }
            this.changeModeAnimation.playTogether(arrayList);
            this.changeModeAnimation.setDuration(200);
            i = mode;
            this.changeModeAnimation.addListener(new AnimatorListenerAdapter() {
                public void onAnimationEnd(Animator animation) {
                    PhotoViewer.this.changeModeAnimation = null;
                    PhotoViewer.this.pickerView.setVisibility(8);
                    PhotoViewer.this.pickerViewSendButton.setVisibility(8);
                    PhotoViewer.this.cameraItem.setVisibility(8);
                    PhotoViewer.this.selectedPhotosListView.setVisibility(8);
                    PhotoViewer.this.selectedPhotosListView.setAlpha(0.0f);
                    PhotoViewer.this.selectedPhotosListView.setTranslationY((float) (-AndroidUtilities.dp(10.0f)));
                    PhotoViewer.this.photosCounterView.setRotationX(0.0f);
                    PhotoViewer.this.selectedPhotosListView.setEnabled(false);
                    PhotoViewer.this.isPhotosListViewVisible = false;
                    if (PhotoViewer.this.needCaptionLayout) {
                        PhotoViewer.this.captionTextView.setVisibility(4);
                    }
                    if (PhotoViewer.this.sendPhotoType == 0 || PhotoViewer.this.sendPhotoType == 4 || ((PhotoViewer.this.sendPhotoType == 2 || PhotoViewer.this.sendPhotoType == 5) && PhotoViewer.this.imagesArrLocals.size() > 1)) {
                        PhotoViewer.this.checkImageView.setVisibility(8);
                        PhotoViewer.this.photosCounterView.setVisibility(8);
                    }
                    if (PhotoViewer.this.centerImage.getBitmap() != null) {
                        float scale;
                        float newScale;
                        int bitmapWidth = PhotoViewer.this.centerImage.getBitmapWidth();
                        int bitmapHeight = PhotoViewer.this.centerImage.getBitmapHeight();
                        float scaleX = ((float) PhotoViewer.this.getContainerViewWidth()) / ((float) bitmapWidth);
                        float scaleY = ((float) PhotoViewer.this.getContainerViewHeight()) / ((float) bitmapHeight);
                        float newScaleX = ((float) PhotoViewer.this.getContainerViewWidth(3)) / ((float) bitmapWidth);
                        float newScaleY = ((float) PhotoViewer.this.getContainerViewHeight(3)) / ((float) bitmapHeight);
                        if (scaleX > scaleY) {
                            scale = scaleY;
                        } else {
                            scale = scaleX;
                        }
                        if (newScaleX > newScaleY) {
                            newScale = newScaleY;
                        } else {
                            newScale = newScaleX;
                        }
                        PhotoViewer.this.animateToScale = newScale / scale;
                        PhotoViewer.this.animateToX = 0.0f;
                        PhotoViewer.this.animateToY = (float) ((VERSION.SDK_INT >= 21 ? AndroidUtilities.statusBarHeight / 2 : 0) + (-AndroidUtilities.dp(44.0f)));
                        PhotoViewer.this.animationStartTime = System.currentTimeMillis();
                        PhotoViewer.this.zoomAnimation = true;
                    }
                    PhotoViewer.this.imageMoveAnimation = new AnimatorSet();
                    AnimatorSet access$14600 = PhotoViewer.this.imageMoveAnimation;
                    Animator[] animatorArr = new Animator[3];
                    float[] fArr = new float[2];
                    animatorArr[0] = ObjectAnimator.ofFloat(PhotoViewer.this, "animationValue", new float[]{0.0f, 1.0f});
                    animatorArr[1] = ObjectAnimator.ofFloat(PhotoViewer.this.photoPaintView.getColorPicker(), "translationY", new float[]{(float) AndroidUtilities.dp(126.0f), 0.0f});
                    animatorArr[2] = ObjectAnimator.ofFloat(PhotoViewer.this.photoPaintView.getToolsView(), "translationY", new float[]{(float) AndroidUtilities.dp(126.0f), 0.0f});
                    access$14600.playTogether(animatorArr);
                    PhotoViewer.this.imageMoveAnimation.setDuration(200);
                    PhotoViewer.this.imageMoveAnimation.addListener(new AnimatorListenerAdapter() {
                        public void onAnimationStart(Animator animation) {
                        }

                        public void onAnimationEnd(Animator animation) {
                            PhotoViewer.this.photoPaintView.init();
                            PhotoViewer.this.imageMoveAnimation = null;
                            PhotoViewer.this.currentEditMode = i;
                            PhotoViewer.this.animateToScale = 1.0f;
                            PhotoViewer.this.animateToX = 0.0f;
                            PhotoViewer.this.animateToY = 0.0f;
                            PhotoViewer.this.scale = 1.0f;
                            PhotoViewer.this.updateMinMax(PhotoViewer.this.scale);
                            PhotoViewer.this.containerView.invalidate();
                        }
                    });
                    PhotoViewer.this.imageMoveAnimation.start();
                }
            });
            this.changeModeAnimation.start();
        }
    }

    private void toggleCheckImageView(boolean show) {
        float f;
        float f2 = 1.0f;
        AnimatorSet animatorSet = new AnimatorSet();
        ArrayList<Animator> arrayList = new ArrayList();
        FrameLayout frameLayout = this.pickerView;
        String str = "alpha";
        float[] fArr = new float[1];
        fArr[0] = show ? 1.0f : 0.0f;
        arrayList.add(ObjectAnimator.ofFloat(frameLayout, str, fArr));
        ImageView imageView = this.pickerViewSendButton;
        str = "alpha";
        fArr = new float[1];
        if (show) {
            f = 1.0f;
        } else {
            f = 0.0f;
        }
        fArr[0] = f;
        arrayList.add(ObjectAnimator.ofFloat(imageView, str, fArr));
        if (this.needCaptionLayout) {
            TextView textView = this.captionTextView;
            str = "alpha";
            fArr = new float[1];
            if (show) {
                f = 1.0f;
            } else {
                f = 0.0f;
            }
            fArr[0] = f;
            arrayList.add(ObjectAnimator.ofFloat(textView, str, fArr));
        }
        if (this.sendPhotoType == 0 || this.sendPhotoType == 4) {
            CheckBox checkBox = this.checkImageView;
            str = "alpha";
            fArr = new float[1];
            if (show) {
                f = 1.0f;
            } else {
                f = 0.0f;
            }
            fArr[0] = f;
            arrayList.add(ObjectAnimator.ofFloat(checkBox, str, fArr));
            CounterView counterView = this.photosCounterView;
            String str2 = "alpha";
            float[] fArr2 = new float[1];
            if (!show) {
                f2 = 0.0f;
            }
            fArr2[0] = f2;
            arrayList.add(ObjectAnimator.ofFloat(counterView, str2, fArr2));
        }
        animatorSet.playTogether(arrayList);
        animatorSet.setDuration(200);
        animatorSet.start();
    }

    private void toggleMiniProgressInternal(final boolean show) {
        if (show) {
            this.miniProgressView.setVisibility(0);
        }
        this.miniProgressAnimator = new AnimatorSet();
        AnimatorSet animatorSet = this.miniProgressAnimator;
        Animator[] animatorArr = new Animator[1];
        RadialProgressView radialProgressView = this.miniProgressView;
        String str = "alpha";
        float[] fArr = new float[1];
        fArr[0] = show ? 1.0f : 0.0f;
        animatorArr[0] = ObjectAnimator.ofFloat(radialProgressView, str, fArr);
        animatorSet.playTogether(animatorArr);
        this.miniProgressAnimator.setDuration(200);
        this.miniProgressAnimator.addListener(new AnimatorListenerAdapter() {
            public void onAnimationEnd(Animator animation) {
                if (animation.equals(PhotoViewer.this.miniProgressAnimator)) {
                    if (!show) {
                        PhotoViewer.this.miniProgressView.setVisibility(4);
                    }
                    PhotoViewer.this.miniProgressAnimator = null;
                }
            }

            public void onAnimationCancel(Animator animation) {
                if (animation.equals(PhotoViewer.this.miniProgressAnimator)) {
                    PhotoViewer.this.miniProgressAnimator = null;
                }
            }
        });
        this.miniProgressAnimator.start();
    }

    private void toggleMiniProgress(boolean show, boolean animated) {
        if (animated) {
            toggleMiniProgressInternal(show);
            if (show) {
                if (this.miniProgressAnimator != null) {
                    this.miniProgressAnimator.cancel();
                    this.miniProgressAnimator = null;
                }
                AndroidUtilities.cancelRunOnUIThread(this.miniProgressShowRunnable);
                if (this.firstAnimationDelay) {
                    this.firstAnimationDelay = false;
                    toggleMiniProgressInternal(true);
                    return;
                }
                AndroidUtilities.runOnUIThread(this.miniProgressShowRunnable, 500);
                return;
            }
            AndroidUtilities.cancelRunOnUIThread(this.miniProgressShowRunnable);
            if (this.miniProgressAnimator != null) {
                this.miniProgressAnimator.cancel();
                toggleMiniProgressInternal(false);
                return;
            }
            return;
        }
        if (this.miniProgressAnimator != null) {
            this.miniProgressAnimator.cancel();
            this.miniProgressAnimator = null;
        }
        this.miniProgressView.setAlpha(show ? 1.0f : 0.0f);
        this.miniProgressView.setVisibility(show ? 0 : 4);
    }

    private void toggleActionBar(final boolean show, boolean animated) {
        float f = 1.0f;
        if (this.actionBarAnimator != null) {
            this.actionBarAnimator.cancel();
        }
        if (show) {
            this.actionBar.setVisibility(0);
            if (this.bottomLayout.getTag() != null) {
                this.bottomLayout.setVisibility(0);
            }
            if (this.captionTextView.getTag() != null) {
                this.captionTextView.setVisibility(0);
            }
        }
        this.isActionBarVisible = show;
        if (VERSION.SDK_INT >= 21) {
            LayoutParams layoutParams;
            if (show) {
                if ((this.windowLayoutParams.flags & 1024) != 0) {
                    layoutParams = this.windowLayoutParams;
                    layoutParams.flags &= -1025;
                    if (this.windowView != null) {
                        try {
                            ((WindowManager) this.parentActivity.getSystemService("window")).updateViewLayout(this.windowView, this.windowLayoutParams);
                        } catch (Exception e) {
                        }
                    }
                }
            } else if ((this.windowLayoutParams.flags & 1024) == 0) {
                layoutParams = this.windowLayoutParams;
                layoutParams.flags |= 1024;
                if (this.windowView != null) {
                    try {
                        ((WindowManager) this.parentActivity.getSystemService("window")).updateViewLayout(this.windowView, this.windowLayoutParams);
                    } catch (Exception e2) {
                    }
                }
            }
        }
        float f2;
        if (animated) {
            ArrayList<Animator> arrayList = new ArrayList();
            ActionBar actionBar = this.actionBar;
            String str = "alpha";
            float[] fArr = new float[1];
            if (show) {
                f2 = 1.0f;
            } else {
                f2 = 0.0f;
            }
            fArr[0] = f2;
            arrayList.add(ObjectAnimator.ofFloat(actionBar, str, fArr));
            if (this.bottomLayout != null) {
                FrameLayout frameLayout = this.bottomLayout;
                str = "alpha";
                fArr = new float[1];
                if (show) {
                    f2 = 1.0f;
                } else {
                    f2 = 0.0f;
                }
                fArr[0] = f2;
                arrayList.add(ObjectAnimator.ofFloat(frameLayout, str, fArr));
            }
            GroupedPhotosListView groupedPhotosListView = this.groupedPhotosListView;
            str = "alpha";
            fArr = new float[1];
            if (show) {
                f2 = 1.0f;
            } else {
                f2 = 0.0f;
            }
            fArr[0] = f2;
            arrayList.add(ObjectAnimator.ofFloat(groupedPhotosListView, str, fArr));
            if (this.captionTextView.getTag() != null) {
                TextView textView = this.captionTextView;
                String str2 = "alpha";
                float[] fArr2 = new float[1];
                if (!show) {
                    f = 0.0f;
                }
                fArr2[0] = f;
                arrayList.add(ObjectAnimator.ofFloat(textView, str2, fArr2));
            }
            this.actionBarAnimator = new AnimatorSet();
            this.actionBarAnimator.playTogether(arrayList);
            this.actionBarAnimator.setDuration(200);
            this.actionBarAnimator.addListener(new AnimatorListenerAdapter() {
                public void onAnimationEnd(Animator animation) {
                    if (animation.equals(PhotoViewer.this.actionBarAnimator)) {
                        if (!show) {
                            PhotoViewer.this.actionBar.setVisibility(4);
                            if (PhotoViewer.this.bottomLayout.getTag() != null) {
                                PhotoViewer.this.bottomLayout.setVisibility(4);
                            }
                            if (PhotoViewer.this.captionTextView.getTag() != null) {
                                PhotoViewer.this.captionTextView.setVisibility(4);
                            }
                        }
                        PhotoViewer.this.actionBarAnimator = null;
                    }
                }

                public void onAnimationCancel(Animator animation) {
                    if (animation.equals(PhotoViewer.this.actionBarAnimator)) {
                        PhotoViewer.this.actionBarAnimator = null;
                    }
                }
            });
            this.actionBarAnimator.start();
            return;
        }
        actionBar = this.actionBar;
        if (show) {
            f2 = 1.0f;
        } else {
            f2 = 0.0f;
        }
        actionBar.setAlpha(f2);
        frameLayout = this.bottomLayout;
        if (show) {
            f2 = 1.0f;
        } else {
            f2 = 0.0f;
        }
        frameLayout.setAlpha(f2);
        groupedPhotosListView = this.groupedPhotosListView;
        if (show) {
            f2 = 1.0f;
        } else {
            f2 = 0.0f;
        }
        groupedPhotosListView.setAlpha(f2);
        textView = this.captionTextView;
        if (!show) {
            f = 0.0f;
        }
        textView.setAlpha(f);
    }

    private void togglePhotosListView(boolean show, boolean animated) {
        float f = 1.0f;
        if (show != this.isPhotosListViewVisible) {
            if (show) {
                this.selectedPhotosListView.setVisibility(0);
            }
            this.isPhotosListViewVisible = show;
            this.selectedPhotosListView.setEnabled(show);
            RecyclerListView recyclerListView;
            CounterView counterView;
            if (animated) {
                ArrayList<Animator> arrayList = new ArrayList();
                recyclerListView = this.selectedPhotosListView;
                String str = "alpha";
                float[] fArr = new float[1];
                fArr[0] = show ? 1.0f : 0.0f;
                arrayList.add(ObjectAnimator.ofFloat(recyclerListView, str, fArr));
                recyclerListView = this.selectedPhotosListView;
                str = "translationY";
                fArr = new float[1];
                fArr[0] = show ? 0.0f : (float) (-AndroidUtilities.dp(10.0f));
                arrayList.add(ObjectAnimator.ofFloat(recyclerListView, str, fArr));
                counterView = this.photosCounterView;
                String str2 = "rotationX";
                float[] fArr2 = new float[1];
                if (!show) {
                    f = 0.0f;
                }
                fArr2[0] = f;
                arrayList.add(ObjectAnimator.ofFloat(counterView, str2, fArr2));
                this.currentListViewAnimation = new AnimatorSet();
                this.currentListViewAnimation.playTogether(arrayList);
                if (!show) {
                    this.currentListViewAnimation.addListener(new AnimatorListenerAdapter() {
                        public void onAnimationEnd(Animator animation) {
                            if (PhotoViewer.this.currentListViewAnimation != null && PhotoViewer.this.currentListViewAnimation.equals(animation)) {
                                PhotoViewer.this.selectedPhotosListView.setVisibility(8);
                                PhotoViewer.this.currentListViewAnimation = null;
                            }
                        }
                    });
                }
                this.currentListViewAnimation.setDuration(200);
                this.currentListViewAnimation.start();
                return;
            }
            float f2;
            recyclerListView = this.selectedPhotosListView;
            if (show) {
                f2 = 1.0f;
            } else {
                f2 = 0.0f;
            }
            recyclerListView.setAlpha(f2);
            this.selectedPhotosListView.setTranslationY(show ? 0.0f : (float) (-AndroidUtilities.dp(10.0f)));
            counterView = this.photosCounterView;
            if (!show) {
                f = 0.0f;
            }
            counterView.setRotationX(f);
            if (!show) {
                this.selectedPhotosListView.setVisibility(8);
            }
        }
    }

    private String getFileName(int index) {
        if (index < 0) {
            return null;
        }
        if (this.secureDocuments.isEmpty()) {
            if (this.imagesArrLocations.isEmpty() && this.imagesArr.isEmpty()) {
                if (this.imagesArrLocals.isEmpty() || index >= this.imagesArrLocals.size()) {
                    return null;
                }
                SearchImage object = this.imagesArrLocals.get(index);
                if (object instanceof SearchImage) {
                    return object.getAttachName();
                }
                if (!(object instanceof BotInlineResult)) {
                    return null;
                }
                BotInlineResult botInlineResult = (BotInlineResult) object;
                if (botInlineResult.document != null) {
                    return FileLoader.getAttachFileName(botInlineResult.document);
                }
                if (botInlineResult.photo != null) {
                    return FileLoader.getAttachFileName(FileLoader.getClosestPhotoSizeWithSize(botInlineResult.photo.sizes, AndroidUtilities.getPhotoSize()));
                }
                if (botInlineResult.content instanceof TL_webDocument) {
                    return Utilities.MD5(botInlineResult.content.url) + "." + ImageLoader.getHttpUrlExtension(botInlineResult.content.url, FileLoader.getExtensionByMime(botInlineResult.content.mime_type));
                }
                return null;
            } else if (this.imagesArrLocations.isEmpty()) {
                if (this.imagesArr.isEmpty() || index >= this.imagesArr.size()) {
                    return null;
                }
                return FileLoader.getMessageFileName(((MessageObject) this.imagesArr.get(index)).messageOwner);
            } else if (index >= this.imagesArrLocations.size()) {
                return null;
            } else {
                FileLocation location = (FileLocation) this.imagesArrLocations.get(index);
                return location.volume_id + "_" + location.local_id + ".jpg";
            }
        } else if (index >= this.secureDocuments.size()) {
            return null;
        } else {
            SecureDocument location2 = (SecureDocument) this.secureDocuments.get(index);
            return location2.secureFile.dc_id + "_" + location2.secureFile.id + ".jpg";
        }
    }

    private TLObject getFileLocation(int index, int[] size) {
        if (index < 0) {
            return null;
        }
        if (this.secureDocuments.isEmpty()) {
            if (this.imagesArrLocations.isEmpty()) {
                if (this.imagesArr.isEmpty() || index >= this.imagesArr.size()) {
                    return null;
                }
                MessageObject message = (MessageObject) this.imagesArr.get(index);
                PhotoSize sizeFull;
                if (message.messageOwner instanceof TL_messageService) {
                    if (message.messageOwner.action instanceof TL_messageActionUserUpdatedPhoto) {
                        return message.messageOwner.action.newUserPhoto.photo_big;
                    }
                    sizeFull = FileLoader.getClosestPhotoSizeWithSize(message.photoThumbs, AndroidUtilities.getPhotoSize());
                    if (sizeFull != null) {
                        if (size != null) {
                            size[0] = sizeFull.size;
                            if (size[0] == 0) {
                                size[0] = -1;
                            }
                        }
                        return sizeFull.location;
                    } else if (size == null) {
                        return null;
                    } else {
                        size[0] = -1;
                        return null;
                    }
                } else if (((message.messageOwner.media instanceof TL_messageMediaPhoto) && message.messageOwner.media.photo != null) || ((message.messageOwner.media instanceof TL_messageMediaWebPage) && message.messageOwner.media.webpage != null)) {
                    sizeFull = FileLoader.getClosestPhotoSizeWithSize(message.photoThumbs, AndroidUtilities.getPhotoSize());
                    if (sizeFull != null) {
                        if (size != null) {
                            size[0] = sizeFull.size;
                            if (size[0] == 0) {
                                size[0] = -1;
                            }
                        }
                        return sizeFull.location;
                    } else if (size == null) {
                        return null;
                    } else {
                        size[0] = -1;
                        return null;
                    }
                } else if (message.messageOwner.media instanceof TL_messageMediaInvoice) {
                    return ((TL_messageMediaInvoice) message.messageOwner.media).photo;
                } else {
                    if (message.getDocument() == null || message.getDocument().thumb == null) {
                        return null;
                    }
                    if (size != null) {
                        size[0] = message.getDocument().thumb.size;
                        if (size[0] == 0) {
                            size[0] = -1;
                        }
                    }
                    return message.getDocument().thumb.location;
                }
            } else if (index >= this.imagesArrLocations.size()) {
                return null;
            } else {
                if (size != null) {
                    size[0] = ((Integer) this.imagesArrLocationsSizes.get(index)).intValue();
                }
                return (TLObject) this.imagesArrLocations.get(index);
            }
        } else if (index >= this.secureDocuments.size()) {
            return null;
        } else {
            if (size != null) {
                size[0] = ((SecureDocument) this.secureDocuments.get(index)).secureFile.size;
            }
            return (TLObject) this.secureDocuments.get(index);
        }
    }

    private void updateSelectedCount() {
        if (this.placeProvider != null) {
            int count = this.placeProvider.getSelectedCount();
            this.photosCounterView.setCount(count);
            if (count == 0) {
                togglePhotosListView(false, true);
            }
        }
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void onPhotoShow(org.telegram.messenger.MessageObject r24, org.telegram.tgnet.TLRPC.FileLocation r25, java.util.ArrayList<org.telegram.messenger.MessageObject> r26, java.util.ArrayList<org.telegram.messenger.SecureDocument> r27, java.util.ArrayList<java.lang.Object> r28, int r29, org.telegram.ui.PhotoViewer.PlaceProviderObject r30) {
        /*
        r23 = this;
        r3 = org.telegram.tgnet.ConnectionsManager.generateClassGuid();
        r0 = r23;
        r0.classGuid = r3;
        r3 = 0;
        r0 = r23;
        r0.currentMessageObject = r3;
        r3 = 0;
        r0 = r23;
        r0.currentFileLocation = r3;
        r3 = 0;
        r0 = r23;
        r0.currentSecureDocument = r3;
        r3 = 0;
        r0 = r23;
        r0.currentPathObject = r3;
        r3 = 0;
        r0 = r23;
        r0.fromCamera = r3;
        r3 = 0;
        r0 = r23;
        r0.currentBotInlineResult = r3;
        r3 = -1;
        r0 = r23;
        r0.currentIndex = r3;
        r0 = r23;
        r3 = r0.currentFileNames;
        r4 = 0;
        r5 = 0;
        r3[r4] = r5;
        r0 = r23;
        r3 = r0.currentFileNames;
        r4 = 1;
        r5 = 0;
        r3[r4] = r5;
        r0 = r23;
        r3 = r0.currentFileNames;
        r4 = 2;
        r5 = 0;
        r3[r4] = r5;
        r3 = 0;
        r0 = r23;
        r0.avatarsDialogId = r3;
        r3 = 0;
        r0 = r23;
        r0.totalImagesCount = r3;
        r3 = 0;
        r0 = r23;
        r0.totalImagesCountMerge = r3;
        r3 = 0;
        r0 = r23;
        r0.currentEditMode = r3;
        r3 = 1;
        r0 = r23;
        r0.isFirstLoading = r3;
        r3 = 0;
        r0 = r23;
        r0.needSearchImageInArr = r3;
        r3 = 0;
        r0 = r23;
        r0.loadingMoreImages = r3;
        r0 = r23;
        r3 = r0.endReached;
        r4 = 0;
        r5 = 0;
        r3[r4] = r5;
        r0 = r23;
        r4 = r0.endReached;
        r5 = 1;
        r0 = r23;
        r6 = r0.mergeDialogId;
        r8 = 0;
        r3 = (r6 > r8 ? 1 : (r6 == r8 ? 0 : -1));
        if (r3 != 0) goto L_0x00de;
    L_0x007d:
        r3 = 1;
    L_0x007e:
        r4[r5] = r3;
        r3 = 0;
        r0 = r23;
        r0.opennedFromMedia = r3;
        r3 = 0;
        r0 = r23;
        r0.needCaptionLayout = r3;
        r0 = r23;
        r3 = r0.containerView;
        r4 = 1;
        r4 = java.lang.Integer.valueOf(r4);
        r3.setTag(r4);
        r3 = 0;
        r0 = r23;
        r0.isCurrentVideo = r3;
        r0 = r23;
        r3 = r0.imagesArr;
        r3.clear();
        r0 = r23;
        r3 = r0.imagesArrLocations;
        r3.clear();
        r0 = r23;
        r3 = r0.imagesArrLocationsSizes;
        r3.clear();
        r0 = r23;
        r3 = r0.avatarsArr;
        r3.clear();
        r0 = r23;
        r3 = r0.secureDocuments;
        r3.clear();
        r0 = r23;
        r3 = r0.imagesArrLocals;
        r3.clear();
        r2 = 0;
    L_0x00c6:
        r3 = 2;
        if (r2 >= r3) goto L_0x00e0;
    L_0x00c9:
        r0 = r23;
        r3 = r0.imagesByIds;
        r3 = r3[r2];
        r3.clear();
        r0 = r23;
        r3 = r0.imagesByIdsTemp;
        r3 = r3[r2];
        r3.clear();
        r2 = r2 + 1;
        goto L_0x00c6;
    L_0x00de:
        r3 = 0;
        goto L_0x007e;
    L_0x00e0:
        r0 = r23;
        r3 = r0.imagesArrTemp;
        r3.clear();
        r3 = 0;
        r0 = r23;
        r0.currentUserAvatarLocation = r3;
        r0 = r23;
        r3 = r0.containerView;
        r4 = 0;
        r5 = 0;
        r6 = 0;
        r7 = 0;
        r3.setPadding(r4, r5, r6, r7);
        r0 = r23;
        r3 = r0.currentThumb;
        if (r3 == 0) goto L_0x0104;
    L_0x00fd:
        r0 = r23;
        r3 = r0.currentThumb;
        r3.release();
    L_0x0104:
        if (r30 == 0) goto L_0x0303;
    L_0x0106:
        r0 = r30;
        r3 = r0.thumb;
    L_0x010a:
        r0 = r23;
        r0.currentThumb = r3;
        if (r30 == 0) goto L_0x0306;
    L_0x0110:
        r0 = r30;
        r3 = r0.isEvent;
        if (r3 == 0) goto L_0x0306;
    L_0x0116:
        r3 = 1;
    L_0x0117:
        r0 = r23;
        r0.isEvent = r3;
        r0 = r23;
        r3 = r0.menuItem;
        r4 = 0;
        r3.setVisibility(r4);
        r0 = r23;
        r3 = r0.sendItem;
        r4 = 8;
        r3.setVisibility(r4);
        r0 = r23;
        r3 = r0.pipItem;
        r4 = 8;
        r3.setVisibility(r4);
        r0 = r23;
        r3 = r0.cameraItem;
        r4 = 8;
        r3.setVisibility(r4);
        r0 = r23;
        r3 = r0.cameraItem;
        r4 = 0;
        r3.setTag(r4);
        r0 = r23;
        r3 = r0.bottomLayout;
        r4 = 0;
        r3.setVisibility(r4);
        r0 = r23;
        r3 = r0.bottomLayout;
        r4 = 1;
        r4 = java.lang.Integer.valueOf(r4);
        r3.setTag(r4);
        r0 = r23;
        r3 = r0.bottomLayout;
        r4 = 0;
        r3.setTranslationY(r4);
        r0 = r23;
        r3 = r0.captionTextView;
        r4 = 0;
        r3.setTranslationY(r4);
        r0 = r23;
        r3 = r0.shareButton;
        r4 = 8;
        r3.setVisibility(r4);
        r0 = r23;
        r3 = r0.qualityChooseView;
        if (r3 == 0) goto L_0x0191;
    L_0x0179:
        r0 = r23;
        r3 = r0.qualityChooseView;
        r4 = 4;
        r3.setVisibility(r4);
        r0 = r23;
        r3 = r0.qualityPicker;
        r4 = 4;
        r3.setVisibility(r4);
        r0 = r23;
        r3 = r0.qualityChooseView;
        r4 = 0;
        r3.setTag(r4);
    L_0x0191:
        r0 = r23;
        r3 = r0.qualityChooseViewAnimation;
        if (r3 == 0) goto L_0x01a3;
    L_0x0197:
        r0 = r23;
        r3 = r0.qualityChooseViewAnimation;
        r3.cancel();
        r3 = 0;
        r0 = r23;
        r0.qualityChooseViewAnimation = r3;
    L_0x01a3:
        r3 = 0;
        r0 = r23;
        r0.allowShare = r3;
        r3 = 0;
        r0 = r23;
        r0.slideshowMessageId = r3;
        r3 = 0;
        r0 = r23;
        r0.nameOverride = r3;
        r3 = 0;
        r0 = r23;
        r0.dateOverride = r3;
        r0 = r23;
        r3 = r0.menuItem;
        r4 = 2;
        r3.hideSubItem(r4);
        r0 = r23;
        r3 = r0.menuItem;
        r4 = 4;
        r3.hideSubItem(r4);
        r0 = r23;
        r3 = r0.menuItem;
        r4 = 10;
        r3.hideSubItem(r4);
        r0 = r23;
        r3 = r0.menuItem;
        r4 = 11;
        r3.hideSubItem(r4);
        r0 = r23;
        r3 = r0.actionBar;
        r4 = 0;
        r3.setTranslationY(r4);
        r0 = r23;
        r3 = r0.checkImageView;
        r4 = 1065353216; // 0x3f800000 float:1.0 double:5.263544247E-315;
        r3.setAlpha(r4);
        r0 = r23;
        r3 = r0.checkImageView;
        r4 = 8;
        r3.setVisibility(r4);
        r0 = r23;
        r3 = r0.actionBar;
        r4 = 0;
        r3.setTitleRightMargin(r4);
        r0 = r23;
        r3 = r0.photosCounterView;
        r4 = 1065353216; // 0x3f800000 float:1.0 double:5.263544247E-315;
        r3.setAlpha(r4);
        r0 = r23;
        r3 = r0.photosCounterView;
        r4 = 8;
        r3.setVisibility(r4);
        r0 = r23;
        r3 = r0.pickerView;
        r4 = 8;
        r3.setVisibility(r4);
        r0 = r23;
        r3 = r0.pickerViewSendButton;
        r4 = 8;
        r3.setVisibility(r4);
        r0 = r23;
        r3 = r0.pickerViewSendButton;
        r4 = 0;
        r3.setTranslationY(r4);
        r0 = r23;
        r3 = r0.pickerView;
        r4 = 1065353216; // 0x3f800000 float:1.0 double:5.263544247E-315;
        r3.setAlpha(r4);
        r0 = r23;
        r3 = r0.pickerViewSendButton;
        r4 = 1065353216; // 0x3f800000 float:1.0 double:5.263544247E-315;
        r3.setAlpha(r4);
        r0 = r23;
        r3 = r0.pickerView;
        r4 = 0;
        r3.setTranslationY(r4);
        r0 = r23;
        r3 = r0.paintItem;
        r4 = 8;
        r3.setVisibility(r4);
        r0 = r23;
        r3 = r0.cropItem;
        r4 = 8;
        r3.setVisibility(r4);
        r0 = r23;
        r3 = r0.tuneItem;
        r4 = 8;
        r3.setVisibility(r4);
        r0 = r23;
        r3 = r0.timeItem;
        r4 = 8;
        r3.setVisibility(r4);
        r0 = r23;
        r3 = r0.videoTimelineView;
        r4 = 8;
        r3.setVisibility(r4);
        r0 = r23;
        r3 = r0.compressItem;
        r4 = 8;
        r3.setVisibility(r4);
        r0 = r23;
        r3 = r0.captionEditText;
        r4 = 8;
        r3.setVisibility(r4);
        r0 = r23;
        r3 = r0.mentionListView;
        r4 = 8;
        r3.setVisibility(r4);
        r0 = r23;
        r3 = r0.muteItem;
        r4 = 8;
        r3.setVisibility(r4);
        r0 = r23;
        r3 = r0.actionBar;
        r4 = 0;
        r3.setSubtitle(r4);
        r0 = r23;
        r3 = r0.masksItem;
        r4 = 8;
        r3.setVisibility(r4);
        r3 = 0;
        r0 = r23;
        r0.muteVideo = r3;
        r0 = r23;
        r3 = r0.muteItem;
        r4 = 2131165690; // 0x7f0701fa float:1.7945604E38 double:1.052935753E-314;
        r3.setImageResource(r4);
        r0 = r23;
        r3 = r0.editorDoneLayout;
        r4 = 8;
        r3.setVisibility(r4);
        r0 = r23;
        r3 = r0.captionTextView;
        r4 = 0;
        r3.setTag(r4);
        r0 = r23;
        r3 = r0.captionTextView;
        r4 = 4;
        r3.setVisibility(r4);
        r0 = r23;
        r3 = r0.photoCropView;
        if (r3 == 0) goto L_0x02da;
    L_0x02d1:
        r0 = r23;
        r3 = r0.photoCropView;
        r4 = 8;
        r3.setVisibility(r4);
    L_0x02da:
        r0 = r23;
        r3 = r0.photoFilterView;
        if (r3 == 0) goto L_0x02e9;
    L_0x02e0:
        r0 = r23;
        r3 = r0.photoFilterView;
        r4 = 8;
        r3.setVisibility(r4);
    L_0x02e9:
        r2 = 0;
    L_0x02ea:
        r3 = 3;
        if (r2 >= r3) goto L_0x0309;
    L_0x02ed:
        r0 = r23;
        r3 = r0.photoProgressViews;
        r3 = r3[r2];
        if (r3 == 0) goto L_0x0300;
    L_0x02f5:
        r0 = r23;
        r3 = r0.photoProgressViews;
        r3 = r3[r2];
        r4 = -1;
        r5 = 0;
        r3.setBackgroundState(r4, r5);
    L_0x0300:
        r2 = r2 + 1;
        goto L_0x02ea;
    L_0x0303:
        r3 = 0;
        goto L_0x010a;
    L_0x0306:
        r3 = 0;
        goto L_0x0117;
    L_0x0309:
        if (r24 == 0) goto L_0x04e1;
    L_0x030b:
        if (r26 != 0) goto L_0x04e1;
    L_0x030d:
        r0 = r24;
        r3 = r0.messageOwner;
        r3 = r3.media;
        r3 = r3 instanceof org.telegram.tgnet.TLRPC.TL_messageMediaWebPage;
        if (r3 == 0) goto L_0x03dd;
    L_0x0317:
        r0 = r24;
        r3 = r0.messageOwner;
        r3 = r3.media;
        r3 = r3.webpage;
        if (r3 == 0) goto L_0x03dd;
    L_0x0321:
        r0 = r24;
        r3 = r0.messageOwner;
        r3 = r3.media;
        r0 = r3.webpage;
        r22 = r0;
        r0 = r22;
        r0 = r0.site_name;
        r20 = r0;
        if (r20 == 0) goto L_0x03dd;
    L_0x0333:
        r20 = r20.toLowerCase();
        r3 = "instagram";
        r0 = r20;
        r3 = r0.equals(r3);
        if (r3 != 0) goto L_0x035a;
    L_0x0342:
        r3 = "twitter";
        r0 = r20;
        r3 = r0.equals(r3);
        if (r3 != 0) goto L_0x035a;
    L_0x034d:
        r3 = "telegram_album";
        r0 = r22;
        r4 = r0.type;
        r3 = r3.equals(r4);
        if (r3 == 0) goto L_0x03dd;
    L_0x035a:
        r0 = r22;
        r3 = r0.author;
        r3 = android.text.TextUtils.isEmpty(r3);
        if (r3 != 0) goto L_0x036c;
    L_0x0364:
        r0 = r22;
        r3 = r0.author;
        r0 = r23;
        r0.nameOverride = r3;
    L_0x036c:
        r0 = r22;
        r3 = r0.cached_page;
        r3 = r3 instanceof org.telegram.tgnet.TLRPC.TL_pageFull;
        if (r3 == 0) goto L_0x0399;
    L_0x0374:
        r2 = 0;
    L_0x0375:
        r0 = r22;
        r3 = r0.cached_page;
        r3 = r3.blocks;
        r3 = r3.size();
        if (r2 >= r3) goto L_0x0399;
    L_0x0381:
        r0 = r22;
        r3 = r0.cached_page;
        r3 = r3.blocks;
        r13 = r3.get(r2);
        r13 = (org.telegram.tgnet.TLRPC.PageBlock) r13;
        r3 = r13 instanceof org.telegram.tgnet.TLRPC.TL_pageBlockAuthorDate;
        if (r3 == 0) goto L_0x0490;
    L_0x0391:
        r13 = (org.telegram.tgnet.TLRPC.TL_pageBlockAuthorDate) r13;
        r3 = r13.published_date;
        r0 = r23;
        r0.dateOverride = r3;
    L_0x0399:
        r3 = 0;
        r4 = 0;
        r0 = r24;
        r12 = r0.getWebPagePhotos(r3, r4);
        r3 = r12.isEmpty();
        if (r3 != 0) goto L_0x03dd;
    L_0x03a7:
        r3 = r24.getId();
        r0 = r23;
        r0.slideshowMessageId = r3;
        r3 = 0;
        r0 = r23;
        r0.needSearchImageInArr = r3;
        r0 = r23;
        r3 = r0.imagesArr;
        r3.addAll(r12);
        r0 = r23;
        r3 = r0.imagesArr;
        r3 = r3.size();
        r0 = r23;
        r0.totalImagesCount = r3;
        r0 = r23;
        r3 = r0.imagesArr;
        r0 = r24;
        r16 = r3.indexOf(r0);
        if (r16 >= 0) goto L_0x03d5;
    L_0x03d3:
        r16 = 0;
    L_0x03d5:
        r3 = 1;
        r0 = r23;
        r1 = r16;
        r0.setImageIndex(r1, r3);
    L_0x03dd:
        r0 = r23;
        r3 = r0.slideshowMessageId;
        if (r3 != 0) goto L_0x0408;
    L_0x03e3:
        r0 = r23;
        r3 = r0.imagesArr;
        r0 = r24;
        r3.add(r0);
        r0 = r23;
        r3 = r0.currentAnimation;
        if (r3 != 0) goto L_0x03fc;
    L_0x03f2:
        r0 = r24;
        r4 = r0.eventId;
        r6 = 0;
        r3 = (r4 > r6 ? 1 : (r4 == r6 ? 0 : -1));
        if (r3 == 0) goto L_0x0494;
    L_0x03fc:
        r3 = 0;
        r0 = r23;
        r0.needSearchImageInArr = r3;
    L_0x0401:
        r3 = 0;
        r4 = 1;
        r0 = r23;
        r0.setImageIndex(r3, r4);
    L_0x0408:
        r0 = r23;
        r3 = r0.currentAnimation;
        if (r3 != 0) goto L_0x0458;
    L_0x040e:
        r0 = r23;
        r3 = r0.isEvent;
        if (r3 != 0) goto L_0x0458;
    L_0x0414:
        r0 = r23;
        r4 = r0.currentDialogId;
        r6 = 0;
        r3 = (r4 > r6 ? 1 : (r4 == r6 ? 0 : -1));
        if (r3 == 0) goto L_0x07e5;
    L_0x041e:
        r0 = r23;
        r3 = r0.totalImagesCount;
        if (r3 != 0) goto L_0x07e5;
    L_0x0424:
        r0 = r23;
        r3 = r0.currentAccount;
        r3 = org.telegram.messenger.DataQuery.getInstance(r3);
        r0 = r23;
        r4 = r0.currentDialogId;
        r6 = 0;
        r0 = r23;
        r7 = r0.classGuid;
        r8 = 1;
        r3.getMediaCount(r4, r6, r7, r8);
        r0 = r23;
        r4 = r0.mergeDialogId;
        r6 = 0;
        r3 = (r4 > r6 ? 1 : (r4 == r6 ? 0 : -1));
        if (r3 == 0) goto L_0x0458;
    L_0x0443:
        r0 = r23;
        r3 = r0.currentAccount;
        r3 = org.telegram.messenger.DataQuery.getInstance(r3);
        r0 = r23;
        r4 = r0.mergeDialogId;
        r6 = 0;
        r0 = r23;
        r7 = r0.classGuid;
        r8 = 1;
        r3.getMediaCount(r4, r6, r7, r8);
    L_0x0458:
        r0 = r23;
        r3 = r0.currentMessageObject;
        if (r3 == 0) goto L_0x0468;
    L_0x045e:
        r0 = r23;
        r3 = r0.currentMessageObject;
        r3 = r3.isVideo();
        if (r3 != 0) goto L_0x0489;
    L_0x0468:
        r0 = r23;
        r3 = r0.currentBotInlineResult;
        if (r3 == 0) goto L_0x0805;
    L_0x046e:
        r0 = r23;
        r3 = r0.currentBotInlineResult;
        r3 = r3.type;
        r4 = "video";
        r3 = r3.equals(r4);
        if (r3 != 0) goto L_0x0489;
    L_0x047d:
        r0 = r23;
        r3 = r0.currentBotInlineResult;
        r3 = r3.document;
        r3 = org.telegram.messenger.MessageObject.isVideoDocument(r3);
        if (r3 == 0) goto L_0x0805;
    L_0x0489:
        r3 = 0;
        r0 = r23;
        r0.onActionClick(r3);
    L_0x048f:
        return;
    L_0x0490:
        r2 = r2 + 1;
        goto L_0x0375;
    L_0x0494:
        r0 = r24;
        r3 = r0.messageOwner;
        r3 = r3.media;
        r3 = r3 instanceof org.telegram.tgnet.TLRPC.TL_messageMediaInvoice;
        if (r3 != 0) goto L_0x0401;
    L_0x049e:
        r0 = r24;
        r3 = r0.messageOwner;
        r3 = r3.media;
        r3 = r3 instanceof org.telegram.tgnet.TLRPC.TL_messageMediaWebPage;
        if (r3 != 0) goto L_0x0401;
    L_0x04a8:
        r0 = r24;
        r3 = r0.messageOwner;
        r3 = r3.action;
        if (r3 == 0) goto L_0x04ba;
    L_0x04b0:
        r0 = r24;
        r3 = r0.messageOwner;
        r3 = r3.action;
        r3 = r3 instanceof org.telegram.tgnet.TLRPC.TL_messageActionEmpty;
        if (r3 == 0) goto L_0x0401;
    L_0x04ba:
        r3 = 1;
        r0 = r23;
        r0.needSearchImageInArr = r3;
        r0 = r23;
        r3 = r0.imagesByIds;
        r4 = 0;
        r3 = r3[r4];
        r4 = r24.getId();
        r0 = r24;
        r3.put(r4, r0);
        r0 = r23;
        r3 = r0.menuItem;
        r4 = 2;
        r3.showSubItem(r4);
        r0 = r23;
        r3 = r0.sendItem;
        r4 = 0;
        r3.setVisibility(r4);
        goto L_0x0401;
    L_0x04e1:
        if (r27 == 0) goto L_0x04f6;
    L_0x04e3:
        r0 = r23;
        r3 = r0.secureDocuments;
        r0 = r27;
        r3.addAll(r0);
        r3 = 1;
        r0 = r23;
        r1 = r29;
        r0.setImageIndex(r1, r3);
        goto L_0x0408;
    L_0x04f6:
        if (r25 == 0) goto L_0x0572;
    L_0x04f8:
        r0 = r30;
        r3 = r0.dialogId;
        r0 = r23;
        r0.avatarsDialogId = r3;
        r0 = r23;
        r3 = r0.imagesArrLocations;
        r0 = r25;
        r3.add(r0);
        r0 = r23;
        r3 = r0.imagesArrLocationsSizes;
        r0 = r30;
        r4 = r0.size;
        r4 = java.lang.Integer.valueOf(r4);
        r3.add(r4);
        r0 = r23;
        r3 = r0.avatarsArr;
        r4 = new org.telegram.tgnet.TLRPC$TL_photoEmpty;
        r4.<init>();
        r3.add(r4);
        r0 = r23;
        r4 = r0.shareButton;
        r0 = r23;
        r3 = r0.videoPlayerControlFrameLayout;
        r3 = r3.getVisibility();
        if (r3 == 0) goto L_0x0565;
    L_0x0532:
        r3 = 0;
    L_0x0533:
        r4.setVisibility(r3);
        r3 = 1;
        r0 = r23;
        r0.allowShare = r3;
        r0 = r23;
        r3 = r0.menuItem;
        r4 = 2;
        r3.hideSubItem(r4);
        r0 = r23;
        r3 = r0.shareButton;
        r3 = r3.getVisibility();
        if (r3 != 0) goto L_0x0568;
    L_0x054d:
        r0 = r23;
        r3 = r0.menuItem;
        r4 = 10;
        r3.hideSubItem(r4);
    L_0x0556:
        r3 = 0;
        r4 = 1;
        r0 = r23;
        r0.setImageIndex(r3, r4);
        r0 = r25;
        r1 = r23;
        r1.currentUserAvatarLocation = r0;
        goto L_0x0408;
    L_0x0565:
        r3 = 8;
        goto L_0x0533;
    L_0x0568:
        r0 = r23;
        r3 = r0.menuItem;
        r4 = 10;
        r3.showSubItem(r4);
        goto L_0x0556;
    L_0x0572:
        if (r26 == 0) goto L_0x05d2;
    L_0x0574:
        r3 = 1;
        r0 = r23;
        r0.opennedFromMedia = r3;
        r0 = r23;
        r3 = r0.menuItem;
        r4 = 4;
        r3.showSubItem(r4);
        r0 = r23;
        r3 = r0.sendItem;
        r4 = 0;
        r3.setVisibility(r4);
        r0 = r23;
        r3 = r0.imagesArr;
        r0 = r26;
        r3.addAll(r0);
        r2 = 0;
    L_0x0593:
        r0 = r23;
        r3 = r0.imagesArr;
        r3 = r3.size();
        if (r2 >= r3) goto L_0x05c8;
    L_0x059d:
        r0 = r23;
        r3 = r0.imagesArr;
        r17 = r3.get(r2);
        r17 = (org.telegram.messenger.MessageObject) r17;
        r0 = r23;
        r4 = r0.imagesByIds;
        r6 = r17.getDialogId();
        r0 = r23;
        r8 = r0.currentDialogId;
        r3 = (r6 > r8 ? 1 : (r6 == r8 ? 0 : -1));
        if (r3 != 0) goto L_0x05c6;
    L_0x05b7:
        r3 = 0;
    L_0x05b8:
        r3 = r4[r3];
        r4 = r17.getId();
        r0 = r17;
        r3.put(r4, r0);
        r2 = r2 + 1;
        goto L_0x0593;
    L_0x05c6:
        r3 = 1;
        goto L_0x05b8;
    L_0x05c8:
        r3 = 1;
        r0 = r23;
        r1 = r29;
        r0.setImageIndex(r1, r3);
        goto L_0x0408;
    L_0x05d2:
        if (r28 == 0) goto L_0x0408;
    L_0x05d4:
        r0 = r23;
        r3 = r0.sendPhotoType;
        if (r3 == 0) goto L_0x05f6;
    L_0x05da:
        r0 = r23;
        r3 = r0.sendPhotoType;
        r4 = 4;
        if (r3 == r4) goto L_0x05f6;
    L_0x05e1:
        r0 = r23;
        r3 = r0.sendPhotoType;
        r4 = 2;
        if (r3 == r4) goto L_0x05ef;
    L_0x05e8:
        r0 = r23;
        r3 = r0.sendPhotoType;
        r4 = 5;
        if (r3 != r4) goto L_0x0613;
    L_0x05ef:
        r3 = r28.size();
        r4 = 1;
        if (r3 <= r4) goto L_0x0613;
    L_0x05f6:
        r0 = r23;
        r3 = r0.checkImageView;
        r4 = 0;
        r3.setVisibility(r4);
        r0 = r23;
        r3 = r0.photosCounterView;
        r4 = 0;
        r3.setVisibility(r4);
        r0 = r23;
        r3 = r0.actionBar;
        r4 = 1120403456; // 0x42c80000 float:100.0 double:5.53552857E-315;
        r4 = org.telegram.messenger.AndroidUtilities.dp(r4);
        r3.setTitleRightMargin(r4);
    L_0x0613:
        r0 = r23;
        r3 = r0.sendPhotoType;
        r4 = 2;
        if (r3 == r4) goto L_0x0621;
    L_0x061a:
        r0 = r23;
        r3 = r0.sendPhotoType;
        r4 = 5;
        if (r3 != r4) goto L_0x063f;
    L_0x0621:
        r0 = r23;
        r3 = r0.placeProvider;
        r3 = r3.canCaptureMorePhotos();
        if (r3 == 0) goto L_0x063f;
    L_0x062b:
        r0 = r23;
        r3 = r0.cameraItem;
        r4 = 0;
        r3.setVisibility(r4);
        r0 = r23;
        r3 = r0.cameraItem;
        r4 = 1;
        r4 = java.lang.Integer.valueOf(r4);
        r3.setTag(r4);
    L_0x063f:
        r0 = r23;
        r3 = r0.menuItem;
        r4 = 8;
        r3.setVisibility(r4);
        r0 = r23;
        r3 = r0.imagesArrLocals;
        r0 = r28;
        r3.addAll(r0);
        r0 = r23;
        r3 = r0.imagesArrLocals;
        r0 = r29;
        r18 = r3.get(r0);
        r0 = r18;
        r3 = r0 instanceof org.telegram.messenger.MediaController.PhotoEntry;
        if (r3 == 0) goto L_0x078e;
    L_0x0661:
        r18 = (org.telegram.messenger.MediaController.PhotoEntry) r18;
        r0 = r18;
        r3 = r0.isVideo;
        if (r3 == 0) goto L_0x0784;
    L_0x0669:
        r0 = r23;
        r3 = r0.cropItem;
        r4 = 8;
        r3.setVisibility(r4);
        r0 = r23;
        r3 = r0.bottomLayout;
        r4 = 0;
        r3.setVisibility(r4);
        r0 = r23;
        r3 = r0.bottomLayout;
        r4 = 1;
        r4 = java.lang.Integer.valueOf(r4);
        r3.setTag(r4);
        r0 = r23;
        r3 = r0.bottomLayout;
        r4 = 1111490560; // 0x42400000 float:48.0 double:5.491493014E-315;
        r4 = org.telegram.messenger.AndroidUtilities.dp(r4);
        r4 = -r4;
        r4 = (float) r4;
        r3.setTranslationY(r4);
    L_0x0695:
        r10 = 1;
    L_0x0696:
        r0 = r23;
        r3 = r0.parentChatActivity;
        if (r3 == 0) goto L_0x0711;
    L_0x069c:
        r0 = r23;
        r3 = r0.parentChatActivity;
        r3 = r3.currentEncryptedChat;
        if (r3 == 0) goto L_0x06b4;
    L_0x06a4:
        r0 = r23;
        r3 = r0.parentChatActivity;
        r3 = r3.currentEncryptedChat;
        r3 = r3.layer;
        r3 = org.telegram.messenger.AndroidUtilities.getPeerLayerVersion(r3);
        r4 = 46;
        if (r3 < r4) goto L_0x0711;
    L_0x06b4:
        r0 = r23;
        r3 = r0.mentionsAdapter;
        r0 = r23;
        r4 = r0.parentChatActivity;
        r4 = r4.info;
        r3.setChatInfo(r4);
        r0 = r23;
        r4 = r0.mentionsAdapter;
        r0 = r23;
        r3 = r0.parentChatActivity;
        r3 = r3.currentChat;
        if (r3 == 0) goto L_0x07c8;
    L_0x06cd:
        r3 = 1;
    L_0x06ce:
        r4.setNeedUsernames(r3);
        r0 = r23;
        r3 = r0.mentionsAdapter;
        r4 = 0;
        r3.setNeedBotContext(r4);
        if (r10 == 0) goto L_0x07cb;
    L_0x06db:
        r0 = r23;
        r3 = r0.placeProvider;
        if (r3 == 0) goto L_0x06f1;
    L_0x06e1:
        r0 = r23;
        r3 = r0.placeProvider;
        if (r3 == 0) goto L_0x07cb;
    L_0x06e7:
        r0 = r23;
        r3 = r0.placeProvider;
        r3 = r3.allowCaption();
        if (r3 == 0) goto L_0x07cb;
    L_0x06f1:
        r3 = 1;
    L_0x06f2:
        r0 = r23;
        r0.needCaptionLayout = r3;
        r0 = r23;
        r4 = r0.captionEditText;
        r0 = r23;
        r3 = r0.needCaptionLayout;
        if (r3 == 0) goto L_0x07ce;
    L_0x0700:
        r3 = 0;
    L_0x0701:
        r4.setVisibility(r3);
        r0 = r23;
        r3 = r0.needCaptionLayout;
        if (r3 == 0) goto L_0x0711;
    L_0x070a:
        r0 = r23;
        r3 = r0.captionEditText;
        r3.onCreate();
    L_0x0711:
        r0 = r23;
        r3 = r0.pickerView;
        r4 = 0;
        r3.setVisibility(r4);
        r0 = r23;
        r3 = r0.pickerViewSendButton;
        r4 = 0;
        r3.setVisibility(r4);
        r0 = r23;
        r3 = r0.pickerViewSendButton;
        r4 = 0;
        r3.setTranslationY(r4);
        r0 = r23;
        r3 = r0.pickerViewSendButton;
        r4 = 1065353216; // 0x3f800000 float:1.0 double:5.263544247E-315;
        r3.setAlpha(r4);
        r0 = r23;
        r3 = r0.bottomLayout;
        r4 = 8;
        r3.setVisibility(r4);
        r0 = r23;
        r3 = r0.bottomLayout;
        r4 = 0;
        r3.setTag(r4);
        r0 = r23;
        r3 = r0.containerView;
        r4 = 0;
        r3.setTag(r4);
        r3 = 1;
        r0 = r23;
        r1 = r29;
        r0.setImageIndex(r1, r3);
        r0 = r23;
        r3 = r0.sendPhotoType;
        r4 = 4;
        if (r3 == r4) goto L_0x07d2;
    L_0x075a:
        r0 = r23;
        r3 = r0.sendPhotoType;
        r4 = 5;
        if (r3 == r4) goto L_0x07d2;
    L_0x0761:
        r0 = r23;
        r3 = r0.paintItem;
        r0 = r23;
        r4 = r0.cropItem;
        r4 = r4.getVisibility();
        r3.setVisibility(r4);
        r0 = r23;
        r3 = r0.tuneItem;
        r0 = r23;
        r4 = r0.cropItem;
        r4 = r4.getVisibility();
        r3.setVisibility(r4);
    L_0x077f:
        r23.updateSelectedCount();
        goto L_0x0408;
    L_0x0784:
        r0 = r23;
        r3 = r0.cropItem;
        r4 = 0;
        r3.setVisibility(r4);
        goto L_0x0695;
    L_0x078e:
        r0 = r18;
        r3 = r0 instanceof org.telegram.tgnet.TLRPC.BotInlineResult;
        if (r3 == 0) goto L_0x07a0;
    L_0x0794:
        r0 = r23;
        r3 = r0.cropItem;
        r4 = 8;
        r3.setVisibility(r4);
        r10 = 0;
        goto L_0x0696;
    L_0x07a0:
        r0 = r23;
        r4 = r0.cropItem;
        r0 = r18;
        r3 = r0 instanceof org.telegram.messenger.MediaController.SearchImage;
        if (r3 == 0) goto L_0x07c3;
    L_0x07aa:
        r18 = (org.telegram.messenger.MediaController.SearchImage) r18;
        r0 = r18;
        r3 = r0.type;
        if (r3 != 0) goto L_0x07c3;
    L_0x07b2:
        r3 = 0;
    L_0x07b3:
        r4.setVisibility(r3);
        r0 = r23;
        r3 = r0.cropItem;
        r3 = r3.getVisibility();
        if (r3 != 0) goto L_0x07c6;
    L_0x07c0:
        r10 = 1;
    L_0x07c1:
        goto L_0x0696;
    L_0x07c3:
        r3 = 8;
        goto L_0x07b3;
    L_0x07c6:
        r10 = 0;
        goto L_0x07c1;
    L_0x07c8:
        r3 = 0;
        goto L_0x06ce;
    L_0x07cb:
        r3 = 0;
        goto L_0x06f2;
    L_0x07ce:
        r3 = 8;
        goto L_0x0701;
    L_0x07d2:
        r0 = r23;
        r3 = r0.paintItem;
        r4 = 8;
        r3.setVisibility(r4);
        r0 = r23;
        r3 = r0.tuneItem;
        r4 = 8;
        r3.setVisibility(r4);
        goto L_0x077f;
    L_0x07e5:
        r0 = r23;
        r3 = r0.avatarsDialogId;
        if (r3 == 0) goto L_0x0458;
    L_0x07eb:
        r0 = r23;
        r3 = r0.currentAccount;
        r3 = org.telegram.messenger.MessagesController.getInstance(r3);
        r0 = r23;
        r4 = r0.avatarsDialogId;
        r5 = 80;
        r6 = 0;
        r8 = 1;
        r0 = r23;
        r9 = r0.classGuid;
        r3.loadDialogPhotos(r4, r5, r6, r8, r9);
        goto L_0x0458;
    L_0x0805:
        r0 = r23;
        r3 = r0.imagesArrLocals;
        r3 = r3.isEmpty();
        if (r3 != 0) goto L_0x048f;
    L_0x080f:
        r0 = r23;
        r3 = r0.imagesArrLocals;
        r0 = r29;
        r15 = r3.get(r0);
        r14 = 0;
        r0 = r23;
        r3 = r0.parentChatActivity;
        if (r3 == 0) goto L_0x0879;
    L_0x0820:
        r0 = r23;
        r3 = r0.parentChatActivity;
        r21 = r3.getCurrentUser();
    L_0x0828:
        r0 = r23;
        r3 = r0.parentChatActivity;
        if (r3 == 0) goto L_0x087c;
    L_0x082e:
        r0 = r23;
        r3 = r0.parentChatActivity;
        r3 = r3.isSecretChat();
        if (r3 != 0) goto L_0x087c;
    L_0x0838:
        if (r21 == 0) goto L_0x087c;
    L_0x083a:
        r0 = r21;
        r3 = r0.bot;
        if (r3 != 0) goto L_0x087c;
    L_0x0840:
        r0 = r23;
        r3 = r0.parentChatActivity;
        r3 = r3.isEditingMessageMedia();
        if (r3 != 0) goto L_0x087c;
    L_0x084a:
        r11 = 1;
    L_0x084b:
        r3 = r15 instanceof org.telegram.messenger.MediaController.PhotoEntry;
        if (r3 == 0) goto L_0x087e;
    L_0x084f:
        r19 = r15;
        r19 = (org.telegram.messenger.MediaController.PhotoEntry) r19;
        r0 = r19;
        r3 = r0.isVideo;
        if (r3 == 0) goto L_0x086d;
    L_0x0859:
        r3 = new java.io.File;
        r0 = r19;
        r4 = r0.path;
        r3.<init>(r4);
        r3 = android.net.Uri.fromFile(r3);
        r4 = 0;
        r5 = 0;
        r0 = r23;
        r0.preparePlayer(r3, r4, r5);
    L_0x086d:
        if (r11 == 0) goto L_0x048f;
    L_0x086f:
        r0 = r23;
        r3 = r0.timeItem;
        r4 = 0;
        r3.setVisibility(r4);
        goto L_0x048f;
    L_0x0879:
        r21 = 0;
        goto L_0x0828;
    L_0x087c:
        r11 = 0;
        goto L_0x084b;
    L_0x087e:
        if (r11 == 0) goto L_0x086d;
    L_0x0880:
        r3 = r15 instanceof org.telegram.messenger.MediaController.SearchImage;
        if (r3 == 0) goto L_0x086d;
    L_0x0884:
        r15 = (org.telegram.messenger.MediaController.SearchImage) r15;
        r3 = r15.type;
        if (r3 != 0) goto L_0x088c;
    L_0x088a:
        r11 = 1;
    L_0x088b:
        goto L_0x086d;
    L_0x088c:
        r11 = 0;
        goto L_0x088b;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.telegram.ui.PhotoViewer.onPhotoShow(org.telegram.messenger.MessageObject, org.telegram.tgnet.TLRPC$FileLocation, java.util.ArrayList, java.util.ArrayList, java.util.ArrayList, int, org.telegram.ui.PhotoViewer$PlaceProviderObject):void");
    }

    public boolean isMuteVideo() {
        return this.muteVideo;
    }

    private void setImages() {
        if (this.animationInProgress == 0) {
            setIndexToImage(this.centerImage, this.currentIndex);
            setIndexToImage(this.rightImage, this.currentIndex + 1);
            setIndexToImage(this.leftImage, this.currentIndex - 1);
        }
    }

    private void setIsAboutToSwitchToIndex(int index, boolean init) {
        if (init || this.switchingToIndex != index) {
            boolean z;
            this.switchingToIndex = index;
            boolean isVideo = false;
            CharSequence caption = null;
            String newFileName = getFileName(index);
            MessageObject newMessageObject = null;
            Chat chat;
            User user;
            if (this.imagesArr.isEmpty()) {
                if (!this.secureDocuments.isEmpty()) {
                    this.allowShare = false;
                    this.menuItem.hideSubItem(1);
                    this.nameTextView.setText(TtmlNode.ANONYMOUS_REGION_ID);
                    this.dateTextView.setText(TtmlNode.ANONYMOUS_REGION_ID);
                    this.actionBar.setTitle(LocaleController.formatString("Of", R.string.Of, Integer.valueOf(this.switchingToIndex + 1), Integer.valueOf(this.secureDocuments.size())));
                } else if (this.imagesArrLocations.isEmpty()) {
                    if (!this.imagesArrLocals.isEmpty()) {
                        if (index >= 0 && index < this.imagesArrLocals.size()) {
                            ColorFilter filter;
                            BotInlineResult object = this.imagesArrLocals.get(index);
                            int ttl = 0;
                            boolean isFiltered = false;
                            boolean isPainted = false;
                            boolean isCropped = false;
                            if (object instanceof BotInlineResult) {
                                BotInlineResult botInlineResult = object;
                                this.currentBotInlineResult = botInlineResult;
                                if (botInlineResult.document != null) {
                                    isVideo = MessageObject.isVideoDocument(botInlineResult.document);
                                } else if (botInlineResult.content instanceof TL_webDocument) {
                                    isVideo = botInlineResult.type.equals(MimeTypes.BASE_TYPE_VIDEO);
                                }
                            } else {
                                PhotoEntry photoEntry;
                                SearchImage searchImage;
                                String pathObject = null;
                                boolean isAnimation = false;
                                if (object instanceof PhotoEntry) {
                                    photoEntry = (PhotoEntry) object;
                                    pathObject = photoEntry.path;
                                    isVideo = photoEntry.isVideo;
                                } else if (object instanceof SearchImage) {
                                    searchImage = (SearchImage) object;
                                    pathObject = searchImage.getPathToAttach();
                                    if (searchImage.type == 1) {
                                        isAnimation = true;
                                    }
                                }
                                if (isVideo) {
                                    this.muteItem.setVisibility(0);
                                    this.compressItem.setVisibility(0);
                                    this.isCurrentVideo = true;
                                    boolean isMuted = false;
                                    if (object instanceof PhotoEntry) {
                                        photoEntry = (PhotoEntry) object;
                                        isMuted = photoEntry.editedInfo != null && photoEntry.editedInfo.muted;
                                    }
                                    processOpenVideo(pathObject, isMuted);
                                    this.videoTimelineView.setVisibility(0);
                                    this.paintItem.setVisibility(8);
                                    this.cropItem.setVisibility(8);
                                    this.tuneItem.setVisibility(8);
                                } else {
                                    this.videoTimelineView.setVisibility(8);
                                    this.muteItem.setVisibility(8);
                                    this.isCurrentVideo = false;
                                    this.compressItem.setVisibility(8);
                                    if (isAnimation) {
                                        this.paintItem.setVisibility(8);
                                        this.cropItem.setVisibility(8);
                                        this.tuneItem.setVisibility(8);
                                    } else {
                                        if (this.sendPhotoType == 4 || this.sendPhotoType == 5) {
                                            this.paintItem.setVisibility(8);
                                            this.tuneItem.setVisibility(8);
                                        } else {
                                            this.paintItem.setVisibility(0);
                                            this.tuneItem.setVisibility(0);
                                        }
                                        this.cropItem.setVisibility(0);
                                    }
                                    this.actionBar.setSubtitle(null);
                                }
                                if (object instanceof PhotoEntry) {
                                    photoEntry = (PhotoEntry) object;
                                    z = photoEntry.bucketId == 0 && photoEntry.dateTaken == 0 && this.imagesArrLocals.size() == 1;
                                    this.fromCamera = z;
                                    caption = photoEntry.caption;
                                    ttl = photoEntry.ttl;
                                    isFiltered = photoEntry.isFiltered;
                                    isPainted = photoEntry.isPainted;
                                    isCropped = photoEntry.isCropped;
                                } else if (object instanceof SearchImage) {
                                    searchImage = (SearchImage) object;
                                    caption = searchImage.caption;
                                    ttl = searchImage.ttl;
                                    isFiltered = searchImage.isFiltered;
                                    isPainted = searchImage.isPainted;
                                    isCropped = searchImage.isCropped;
                                }
                            }
                            if (this.bottomLayout.getVisibility() != 8) {
                                this.bottomLayout.setVisibility(8);
                            }
                            this.bottomLayout.setTag(null);
                            if (!this.fromCamera) {
                                this.actionBar.setTitle(LocaleController.formatString("Of", R.string.Of, Integer.valueOf(this.switchingToIndex + 1), Integer.valueOf(this.imagesArrLocals.size())));
                            } else if (isVideo) {
                                this.actionBar.setTitle(LocaleController.getString("AttachVideo", R.string.AttachVideo));
                            } else {
                                this.actionBar.setTitle(LocaleController.getString("AttachPhoto", R.string.AttachPhoto));
                            }
                            if (this.parentChatActivity != null) {
                                chat = this.parentChatActivity.getCurrentChat();
                                if (chat != null) {
                                    this.actionBar.setTitle(chat.title);
                                } else {
                                    user = this.parentChatActivity.getCurrentUser();
                                    if (user != null) {
                                        this.actionBar.setTitle(ContactsController.formatName(user.first_name, user.last_name));
                                    }
                                }
                            }
                            if (this.sendPhotoType == 0 || this.sendPhotoType == 4 || ((this.sendPhotoType == 2 || this.sendPhotoType == 5) && this.imagesArrLocals.size() > 1)) {
                                this.checkImageView.setChecked(this.placeProvider.isPhotoChecked(this.switchingToIndex), false);
                            }
                            updateCaptionTextForCurrentPhoto(object);
                            ColorFilter porterDuffColorFilter = new PorterDuffColorFilter(-12734994, Mode.MULTIPLY);
                            this.timeItem.setColorFilter(ttl != 0 ? porterDuffColorFilter : null);
                            this.paintItem.setColorFilter(isPainted ? porterDuffColorFilter : null);
                            this.cropItem.setColorFilter(isCropped ? porterDuffColorFilter : null);
                            ImageView imageView = this.tuneItem;
                            if (!isFiltered) {
                                filter = null;
                            }
                            imageView.setColorFilter(filter);
                        } else {
                            return;
                        }
                    }
                } else if (index >= 0 && index < this.imagesArrLocations.size()) {
                    this.nameTextView.setText(TtmlNode.ANONYMOUS_REGION_ID);
                    this.dateTextView.setText(TtmlNode.ANONYMOUS_REGION_ID);
                    if (this.avatarsDialogId != UserConfig.getInstance(this.currentAccount).getClientUserId() || this.avatarsArr.isEmpty()) {
                        this.menuItem.hideSubItem(6);
                    } else {
                        this.menuItem.showSubItem(6);
                    }
                    if (this.isEvent) {
                        this.actionBar.setTitle(LocaleController.getString("AttachPhoto", R.string.AttachPhoto));
                    } else {
                        this.actionBar.setTitle(LocaleController.formatString("Of", R.string.Of, Integer.valueOf(this.switchingToIndex + 1), Integer.valueOf(this.imagesArrLocations.size())));
                    }
                    this.menuItem.showSubItem(1);
                    this.allowShare = true;
                    this.shareButton.setVisibility(this.videoPlayerControlFrameLayout.getVisibility() != 0 ? 0 : 8);
                    if (this.shareButton.getVisibility() == 0) {
                        this.menuItem.hideSubItem(10);
                    } else {
                        this.menuItem.showSubItem(10);
                    }
                    this.groupedPhotosListView.fillList();
                } else {
                    return;
                }
            } else if (this.switchingToIndex >= 0 && this.switchingToIndex < this.imagesArr.size()) {
                newMessageObject = (MessageObject) this.imagesArr.get(this.switchingToIndex);
                isVideo = newMessageObject.isVideo();
                boolean isInvoice = newMessageObject.isInvoice();
                if (isInvoice) {
                    this.masksItem.setVisibility(8);
                    this.menuItem.hideSubItem(6);
                    this.menuItem.hideSubItem(11);
                    caption = newMessageObject.messageOwner.media.description;
                    this.allowShare = false;
                    this.bottomLayout.setTranslationY((float) AndroidUtilities.dp(48.0f));
                    this.captionTextView.setTranslationY((float) AndroidUtilities.dp(48.0f));
                } else {
                    long date;
                    ActionBarMenuItem actionBarMenuItem = this.masksItem;
                    int i = (!newMessageObject.hasPhotoStickers() || ((int) newMessageObject.getDialogId()) == 0) ? 8 : 0;
                    actionBarMenuItem.setVisibility(i);
                    if (newMessageObject.canDeleteMessage(null) && this.slideshowMessageId == 0) {
                        this.menuItem.showSubItem(6);
                    } else {
                        this.menuItem.hideSubItem(6);
                    }
                    if (isVideo) {
                        this.menuItem.showSubItem(11);
                        if (this.pipItem.getVisibility() != 0) {
                            this.pipItem.setVisibility(0);
                        }
                        if (!this.pipAvailable) {
                            this.pipItem.setEnabled(false);
                            this.pipItem.setAlpha(0.5f);
                        }
                    } else {
                        this.menuItem.hideSubItem(11);
                        if (this.pipItem.getVisibility() != 8) {
                            this.pipItem.setVisibility(8);
                        }
                    }
                    if (this.nameOverride != null) {
                        this.nameTextView.setText(this.nameOverride);
                    } else if (newMessageObject.isFromUser()) {
                        user = MessagesController.getInstance(this.currentAccount).getUser(Integer.valueOf(newMessageObject.messageOwner.from_id));
                        if (user != null) {
                            this.nameTextView.setText(UserObject.getUserName(user));
                        } else {
                            this.nameTextView.setText(TtmlNode.ANONYMOUS_REGION_ID);
                        }
                    } else {
                        chat = MessagesController.getInstance(this.currentAccount).getChat(Integer.valueOf(newMessageObject.messageOwner.to_id.channel_id));
                        if (chat != null) {
                            this.nameTextView.setText(chat.title);
                        } else {
                            this.nameTextView.setText(TtmlNode.ANONYMOUS_REGION_ID);
                        }
                    }
                    if (this.dateOverride != 0) {
                        date = ((long) this.dateOverride) * 1000;
                    } else {
                        date = ((long) newMessageObject.messageOwner.date) * 1000;
                    }
                    String dateString = LocaleController.formatString("formatDateAtTime", R.string.formatDateAtTime, LocaleController.getInstance().formatterYear.format(new Date(date)), LocaleController.getInstance().formatterDay.format(new Date(date)));
                    if (newFileName == null || !isVideo) {
                        this.dateTextView.setText(dateString);
                    } else {
                        this.dateTextView.setText(String.format("%s (%s)", new Object[]{dateString, AndroidUtilities.formatFileSize((long) newMessageObject.getDocument().size)}));
                    }
                    caption = newMessageObject.caption;
                }
                if (this.currentAnimation != null) {
                    this.menuItem.hideSubItem(1);
                    this.menuItem.hideSubItem(10);
                    if (!newMessageObject.canDeleteMessage(null)) {
                        this.menuItem.setVisibility(8);
                    }
                    this.allowShare = true;
                    this.shareButton.setVisibility(0);
                    this.actionBar.setTitle(LocaleController.getString("AttachGif", R.string.AttachGif));
                } else {
                    if (this.totalImagesCount + this.totalImagesCountMerge == 0 || this.needSearchImageInArr) {
                        if (this.slideshowMessageId == 0 && (newMessageObject.messageOwner.media instanceof TL_messageMediaWebPage)) {
                            if (newMessageObject.isVideo()) {
                                this.actionBar.setTitle(LocaleController.getString("AttachVideo", R.string.AttachVideo));
                            } else {
                                this.actionBar.setTitle(LocaleController.getString("AttachPhoto", R.string.AttachPhoto));
                            }
                        } else if (isInvoice) {
                            this.actionBar.setTitle(newMessageObject.messageOwner.media.title);
                        }
                    } else if (this.opennedFromMedia) {
                        if (this.imagesArr.size() < this.totalImagesCount + this.totalImagesCountMerge && !this.loadingMoreImages && this.switchingToIndex > this.imagesArr.size() - 5) {
                            loadFromMaxId = this.imagesArr.isEmpty() ? 0 : ((MessageObject) this.imagesArr.get(this.imagesArr.size() - 1)).getId();
                            loadIndex = 0;
                            if (this.endReached[0] && this.mergeDialogId != 0) {
                                loadIndex = 1;
                                if (!(this.imagesArr.isEmpty() || ((MessageObject) this.imagesArr.get(this.imagesArr.size() - 1)).getDialogId() == this.mergeDialogId)) {
                                    loadFromMaxId = 0;
                                }
                            }
                            DataQuery.getInstance(this.currentAccount).loadMedia(loadIndex == 0 ? this.currentDialogId : this.mergeDialogId, 80, loadFromMaxId, 0, true, this.classGuid);
                            this.loadingMoreImages = true;
                        }
                        this.actionBar.setTitle(LocaleController.formatString("Of", R.string.Of, Integer.valueOf(this.switchingToIndex + 1), Integer.valueOf(this.totalImagesCount + this.totalImagesCountMerge)));
                    } else {
                        if (this.imagesArr.size() < this.totalImagesCount + this.totalImagesCountMerge && !this.loadingMoreImages && this.switchingToIndex < 5) {
                            loadFromMaxId = this.imagesArr.isEmpty() ? 0 : ((MessageObject) this.imagesArr.get(0)).getId();
                            loadIndex = 0;
                            if (this.endReached[0] && this.mergeDialogId != 0) {
                                loadIndex = 1;
                                if (!(this.imagesArr.isEmpty() || ((MessageObject) this.imagesArr.get(0)).getDialogId() == this.mergeDialogId)) {
                                    loadFromMaxId = 0;
                                }
                            }
                            DataQuery.getInstance(this.currentAccount).loadMedia(loadIndex == 0 ? this.currentDialogId : this.mergeDialogId, 80, loadFromMaxId, 0, true, this.classGuid);
                            this.loadingMoreImages = true;
                        }
                        this.actionBar.setTitle(LocaleController.formatString("Of", R.string.Of, Integer.valueOf((((this.totalImagesCount + this.totalImagesCountMerge) - this.imagesArr.size()) + this.switchingToIndex) + 1), Integer.valueOf(this.totalImagesCount + this.totalImagesCountMerge)));
                    }
                    if (((int) this.currentDialogId) == 0) {
                        this.sendItem.setVisibility(8);
                    }
                    if (newMessageObject.messageOwner.ttl == 0 || newMessageObject.messageOwner.ttl >= 3600) {
                        this.allowShare = true;
                        this.menuItem.showSubItem(1);
                        this.shareButton.setVisibility(this.videoPlayerControlFrameLayout.getVisibility() != 0 ? 0 : 8);
                        if (this.shareButton.getVisibility() == 0) {
                            this.menuItem.hideSubItem(10);
                        } else {
                            this.menuItem.showSubItem(10);
                        }
                    } else {
                        this.allowShare = false;
                        this.menuItem.hideSubItem(1);
                        this.shareButton.setVisibility(8);
                        this.menuItem.hideSubItem(10);
                    }
                }
                this.groupedPhotosListView.fillList();
            } else {
                return;
            }
            if (init) {
                z = false;
            } else {
                z = true;
            }
            setCurrentCaption(newMessageObject, caption, z);
        }
    }

    private void setImageIndex(int index, boolean init) {
        if (this.currentIndex != index && this.placeProvider != null) {
            if (!(init || this.currentThumb == null)) {
                this.currentThumb.release();
                this.currentThumb = null;
            }
            this.currentFileNames[0] = getFileName(index);
            this.currentFileNames[1] = getFileName(index + 1);
            this.currentFileNames[2] = getFileName(index - 1);
            this.placeProvider.willSwitchFromPhoto(this.currentMessageObject, this.currentFileLocation, this.currentIndex);
            int prevIndex = this.currentIndex;
            this.currentIndex = index;
            setIsAboutToSwitchToIndex(this.currentIndex, init);
            boolean isVideo = false;
            boolean sameImage = false;
            Uri videoPath = null;
            if (this.imagesArr.isEmpty()) {
                if (this.secureDocuments.isEmpty()) {
                    if (this.imagesArrLocations.isEmpty()) {
                        if (!this.imagesArrLocals.isEmpty()) {
                            if (index < 0 || index >= this.imagesArrLocals.size()) {
                                closePhoto(false, false);
                                return;
                            }
                            BotInlineResult object = this.imagesArrLocals.get(index);
                            if (object instanceof BotInlineResult) {
                                BotInlineResult botInlineResult = object;
                                this.currentBotInlineResult = botInlineResult;
                                if (botInlineResult.document != null) {
                                    this.currentPathObject = FileLoader.getPathToAttach(botInlineResult.document).getAbsolutePath();
                                    isVideo = MessageObject.isVideoDocument(botInlineResult.document);
                                } else if (botInlineResult.photo != null) {
                                    this.currentPathObject = FileLoader.getPathToAttach(FileLoader.getClosestPhotoSizeWithSize(botInlineResult.photo.sizes, AndroidUtilities.getPhotoSize())).getAbsolutePath();
                                } else if (botInlineResult.content instanceof TL_webDocument) {
                                    this.currentPathObject = botInlineResult.content.url;
                                    isVideo = botInlineResult.type.equals(MimeTypes.BASE_TYPE_VIDEO);
                                }
                            } else if (object instanceof PhotoEntry) {
                                PhotoEntry photoEntry = (PhotoEntry) object;
                                this.currentPathObject = photoEntry.path;
                                isVideo = photoEntry.isVideo;
                                videoPath = Uri.fromFile(new File(photoEntry.path));
                            } else if (object instanceof SearchImage) {
                                this.currentPathObject = ((SearchImage) object).getPathToAttach();
                            }
                        }
                    } else if (index < 0 || index >= this.imagesArrLocations.size()) {
                        closePhoto(false, false);
                        return;
                    } else {
                        FileLocation old = this.currentFileLocation;
                        FileLocation newLocation = (FileLocation) this.imagesArrLocations.get(index);
                        if (init && old != null && newLocation != null && old.local_id == newLocation.local_id && old.volume_id == newLocation.volume_id) {
                            sameImage = true;
                        }
                        this.currentFileLocation = (FileLocation) this.imagesArrLocations.get(index);
                    }
                } else if (index < 0 || index >= this.secureDocuments.size()) {
                    closePhoto(false, false);
                    return;
                } else {
                    this.currentSecureDocument = (SecureDocument) this.secureDocuments.get(index);
                }
            } else if (this.currentIndex < 0 || this.currentIndex >= this.imagesArr.size()) {
                closePhoto(false, false);
                return;
            } else {
                MessageObject newMessageObject = (MessageObject) this.imagesArr.get(this.currentIndex);
                sameImage = init && this.currentMessageObject != null && this.currentMessageObject.getId() == newMessageObject.getId();
                this.currentMessageObject = newMessageObject;
                isVideo = newMessageObject.isVideo();
            }
            if (this.currentPlaceObject != null) {
                if (this.animationInProgress == 0) {
                    this.currentPlaceObject.imageReceiver.setVisible(true, true);
                } else {
                    this.showAfterAnimation = this.currentPlaceObject;
                }
            }
            this.currentPlaceObject = this.placeProvider.getPlaceForPhoto(this.currentMessageObject, this.currentFileLocation, this.currentIndex);
            if (this.currentPlaceObject != null) {
                if (this.animationInProgress == 0) {
                    this.currentPlaceObject.imageReceiver.setVisible(false, true);
                } else {
                    this.hideAfterAnimation = this.currentPlaceObject;
                }
            }
            if (!sameImage) {
                this.draggingDown = false;
                this.translationX = 0.0f;
                this.translationY = 0.0f;
                this.scale = 1.0f;
                this.animateToX = 0.0f;
                this.animateToY = 0.0f;
                this.animateToScale = 1.0f;
                this.animationStartTime = 0;
                this.imageMoveAnimation = null;
                this.changeModeAnimation = null;
                if (this.aspectRatioFrameLayout != null) {
                    this.aspectRatioFrameLayout.setVisibility(4);
                }
                this.pinchStartDistance = 0.0f;
                this.pinchStartScale = 1.0f;
                this.pinchCenterX = 0.0f;
                this.pinchCenterY = 0.0f;
                this.pinchStartX = 0.0f;
                this.pinchStartY = 0.0f;
                this.moveStartX = 0.0f;
                this.moveStartY = 0.0f;
                this.zooming = false;
                this.moving = false;
                this.doubleTap = false;
                this.invalidCoords = false;
                this.canDragDown = true;
                this.changingPage = false;
                this.switchImageAfterAnimation = 0;
                boolean z = (this.imagesArrLocals.isEmpty() && (this.currentFileNames[0] == null || isVideo || this.photoProgressViews[0].backgroundState == 0)) ? false : true;
                this.canZoom = z;
                updateMinMax(this.scale);
                releasePlayer();
            }
            if (isVideo && videoPath != null) {
                this.isStreaming = false;
                preparePlayer(videoPath, false, false);
            }
            if (prevIndex == -1) {
                setImages();
                for (int a = 0; a < 3; a++) {
                    checkProgress(a, false);
                }
                return;
            }
            checkProgress(0, false);
            ImageReceiver temp;
            PhotoProgressView tempProgress;
            if (prevIndex > this.currentIndex) {
                temp = this.rightImage;
                this.rightImage = this.centerImage;
                this.centerImage = this.leftImage;
                this.leftImage = temp;
                tempProgress = this.photoProgressViews[0];
                this.photoProgressViews[0] = this.photoProgressViews[2];
                this.photoProgressViews[2] = tempProgress;
                setIndexToImage(this.leftImage, this.currentIndex - 1);
                checkProgress(1, false);
                checkProgress(2, false);
            } else if (prevIndex < this.currentIndex) {
                temp = this.leftImage;
                this.leftImage = this.centerImage;
                this.centerImage = this.rightImage;
                this.rightImage = temp;
                tempProgress = this.photoProgressViews[0];
                this.photoProgressViews[0] = this.photoProgressViews[1];
                this.photoProgressViews[1] = tempProgress;
                setIndexToImage(this.rightImage, this.currentIndex + 1);
                checkProgress(1, false);
                checkProgress(2, false);
            }
        }
    }

    private void setCurrentCaption(MessageObject messageObject, CharSequence caption, boolean animated) {
        if (this.needCaptionLayout) {
            if (this.captionTextView.getParent() != this.pickerView) {
                this.captionTextView.setBackgroundDrawable(null);
                this.containerView.removeView(this.captionTextView);
                this.pickerView.addView(this.captionTextView, LayoutHelper.createFrame(-1, -2.0f, 83, 0.0f, 0.0f, 76.0f, 48.0f));
            }
        } else if (this.captionTextView.getParent() != this.containerView) {
            this.captionTextView.setBackgroundColor(Theme.ACTION_BAR_PHOTO_VIEWER_COLOR);
            this.pickerView.removeView(this.captionTextView);
            this.containerView.addView(this.captionTextView, LayoutHelper.createFrame(-1, -2.0f, 83, 0.0f, 0.0f, 0.0f, 48.0f));
        }
        if (this.isCurrentVideo) {
            this.captionTextView.setMaxLines(1);
            this.captionTextView.setSingleLine(true);
        } else {
            this.captionTextView.setSingleLine(false);
            this.captionTextView.setMaxLines(10);
        }
        boolean wasVisisble = this.captionTextView.getTag() != null;
        AnimatorSet animatorSet;
        Animator[] animatorArr;
        if (!TextUtils.isEmpty(caption)) {
            CharSequence str;
            Theme.createChatResources(null, true);
            if (messageObject == null || messageObject.messageOwner.entities.isEmpty()) {
                str = Emoji.replaceEmoji(new SpannableStringBuilder(caption), this.captionTextView.getPaint().getFontMetricsInt(), AndroidUtilities.dp(20.0f), false);
            } else {
                Spannable spannableString = SpannableString.valueOf(caption.toString());
                messageObject.addEntitiesToText(spannableString, true, false);
                str = Emoji.replaceEmoji(spannableString, this.captionTextView.getPaint().getFontMetricsInt(), AndroidUtilities.dp(20.0f), false);
            }
            this.captionTextView.setTag(str);
            if (this.currentCaptionAnimation != null) {
                this.currentCaptionAnimation.cancel();
                this.currentCaptionAnimation = null;
            }
            try {
                this.captionTextView.setText(str);
            } catch (Throwable e) {
                FileLog.e(e);
            }
            this.captionTextView.setTextColor(-1);
            boolean visible = this.isActionBarVisible && (this.bottomLayout.getVisibility() == 0 || this.pickerView.getVisibility() == 0);
            if (visible) {
                this.captionTextView.setVisibility(0);
                if (!animated || wasVisisble) {
                    this.captionTextView.setAlpha(1.0f);
                    return;
                }
                this.currentCaptionAnimation = new AnimatorSet();
                this.currentCaptionAnimation.setDuration(200);
                this.currentCaptionAnimation.setInterpolator(decelerateInterpolator);
                this.currentCaptionAnimation.addListener(new AnimatorListenerAdapter() {
                    public void onAnimationEnd(Animator animation) {
                        if (animation.equals(PhotoViewer.this.currentCaptionAnimation)) {
                            PhotoViewer.this.currentCaptionAnimation = null;
                        }
                    }
                });
                animatorSet = this.currentCaptionAnimation;
                animatorArr = new Animator[2];
                animatorArr[0] = ObjectAnimator.ofFloat(this.captionTextView, "alpha", new float[]{0.0f, 1.0f});
                animatorArr[1] = ObjectAnimator.ofFloat(this.captionTextView, "translationY", new float[]{(float) AndroidUtilities.dp(5.0f), 0.0f});
                animatorSet.playTogether(animatorArr);
                this.currentCaptionAnimation.start();
            } else if (this.captionTextView.getVisibility() == 0) {
                this.captionTextView.setVisibility(4);
                this.captionTextView.setAlpha(0.0f);
            }
        } else if (this.needCaptionLayout) {
            this.captionTextView.setText(LocaleController.getString("AddCaption", R.string.AddCaption));
            this.captionTextView.setTag("empty");
            this.captionTextView.setVisibility(0);
            this.captionTextView.setTextColor(-1291845633);
        } else {
            this.captionTextView.setTextColor(-1);
            this.captionTextView.setTag(null);
            if (animated && wasVisisble) {
                this.currentCaptionAnimation = new AnimatorSet();
                this.currentCaptionAnimation.setDuration(200);
                this.currentCaptionAnimation.setInterpolator(decelerateInterpolator);
                this.currentCaptionAnimation.addListener(new AnimatorListenerAdapter() {
                    public void onAnimationEnd(Animator animation) {
                        if (animation.equals(PhotoViewer.this.currentCaptionAnimation)) {
                            PhotoViewer.this.captionTextView.setVisibility(4);
                            PhotoViewer.this.currentCaptionAnimation = null;
                        }
                    }

                    public void onAnimationCancel(Animator animation) {
                        if (animation.equals(PhotoViewer.this.currentCaptionAnimation)) {
                            PhotoViewer.this.currentCaptionAnimation = null;
                        }
                    }
                });
                animatorSet = this.currentCaptionAnimation;
                animatorArr = new Animator[2];
                animatorArr[0] = ObjectAnimator.ofFloat(this.captionTextView, "alpha", new float[]{0.0f});
                animatorArr[1] = ObjectAnimator.ofFloat(this.captionTextView, "translationY", new float[]{(float) AndroidUtilities.dp(5.0f)});
                animatorSet.playTogether(animatorArr);
                this.currentCaptionAnimation.start();
                return;
            }
            this.captionTextView.setVisibility(4);
        }
    }

    private void checkProgress(int a, boolean animated) {
        int index = this.currentIndex;
        if (a == 1) {
            index++;
        } else if (a == 2) {
            index--;
        }
        if (this.currentFileNames[a] != null) {
            boolean z;
            File f = null;
            boolean isVideo = false;
            boolean canStream = false;
            if (this.currentMessageObject != null) {
                if (index < 0 || index >= this.imagesArr.size()) {
                    this.photoProgressViews[a].setBackgroundState(-1, animated);
                    return;
                }
                MessageObject messageObject = (MessageObject) this.imagesArr.get(index);
                if (!TextUtils.isEmpty(messageObject.messageOwner.attachPath)) {
                    f = new File(messageObject.messageOwner.attachPath);
                    if (!f.exists()) {
                        f = null;
                    }
                }
                if (f == null) {
                    if ((messageObject.messageOwner.media instanceof TL_messageMediaWebPage) && messageObject.messageOwner.media.webpage != null && messageObject.messageOwner.media.webpage.document == null) {
                        f = FileLoader.getPathToAttach(getFileLocation(index, null), true);
                    } else {
                        f = FileLoader.getPathToMessage(messageObject.messageOwner);
                    }
                }
                canStream = SharedConfig.streamMedia && messageObject.isVideo() && ((int) messageObject.getDialogId()) != 0;
                isVideo = messageObject.isVideo();
            } else if (this.currentBotInlineResult != null) {
                if (index < 0 || index >= this.imagesArrLocals.size()) {
                    this.photoProgressViews[a].setBackgroundState(-1, animated);
                    return;
                }
                BotInlineResult botInlineResult = (BotInlineResult) this.imagesArrLocals.get(index);
                if (botInlineResult.type.equals(MimeTypes.BASE_TYPE_VIDEO) || MessageObject.isVideoDocument(botInlineResult.document)) {
                    if (botInlineResult.document != null) {
                        f = FileLoader.getPathToAttach(botInlineResult.document);
                    } else if (botInlineResult.content instanceof TL_webDocument) {
                        f = new File(FileLoader.getDirectory(4), Utilities.MD5(botInlineResult.content.url) + "." + ImageLoader.getHttpUrlExtension(botInlineResult.content.url, "mp4"));
                    }
                    isVideo = true;
                } else if (botInlineResult.document != null) {
                    f = new File(FileLoader.getDirectory(3), this.currentFileNames[a]);
                } else if (botInlineResult.photo != null) {
                    f = new File(FileLoader.getDirectory(0), this.currentFileNames[a]);
                }
                if (f == null || !f.exists()) {
                    f = new File(FileLoader.getDirectory(4), this.currentFileNames[a]);
                }
            } else if (this.currentFileLocation != null) {
                if (index < 0 || index >= this.imagesArrLocations.size()) {
                    this.photoProgressViews[a].setBackgroundState(-1, animated);
                    return;
                }
                FileLocation location = (FileLocation) this.imagesArrLocations.get(index);
                z = this.avatarsDialogId != 0 || this.isEvent;
                f = FileLoader.getPathToAttach(location, z);
            } else if (this.currentSecureDocument != null) {
                if (index < 0 || index >= this.secureDocuments.size()) {
                    this.photoProgressViews[a].setBackgroundState(-1, animated);
                    return;
                }
                f = FileLoader.getPathToAttach((SecureDocument) this.secureDocuments.get(index), true);
            } else if (this.currentPathObject != null) {
                f = new File(FileLoader.getDirectory(3), this.currentFileNames[a]);
                if (!f.exists()) {
                    f = new File(FileLoader.getDirectory(4), this.currentFileNames[a]);
                }
            }
            boolean exists = f.exists();
            if (f == null || !(exists || canStream)) {
                if (!isVideo) {
                    this.photoProgressViews[a].setBackgroundState(0, animated);
                } else if (FileLoader.getInstance(this.currentAccount).isLoadingFile(this.currentFileNames[a])) {
                    this.photoProgressViews[a].setBackgroundState(1, false);
                } else {
                    this.photoProgressViews[a].setBackgroundState(2, false);
                }
                Float progress = ImageLoader.getInstance().getFileProgress(this.currentFileNames[a]);
                if (progress == null) {
                    progress = Float.valueOf(0.0f);
                }
                this.photoProgressViews[a].setProgress(progress.floatValue(), false);
            } else {
                if (isVideo) {
                    this.photoProgressViews[a].setBackgroundState(3, animated);
                } else {
                    this.photoProgressViews[a].setBackgroundState(-1, animated);
                }
                if (a == 0) {
                    if (exists) {
                        this.menuItem.hideSubItem(7);
                    } else if (FileLoader.getInstance(this.currentAccount).isLoadingFile(this.currentFileNames[a])) {
                        this.menuItem.showSubItem(7);
                    } else {
                        this.menuItem.hideSubItem(7);
                    }
                }
            }
            if (a == 0) {
                if (this.imagesArrLocals.isEmpty() && (this.currentFileNames[0] == null || isVideo || this.photoProgressViews[0].backgroundState == 0)) {
                    z = false;
                } else {
                    z = true;
                }
                this.canZoom = z;
                return;
            }
            return;
        }
        boolean isLocalVideo = false;
        if (!this.imagesArrLocals.isEmpty() && index >= 0 && index < this.imagesArrLocals.size()) {
            PhotoEntry object = this.imagesArrLocals.get(index);
            if (object instanceof PhotoEntry) {
                isLocalVideo = object.isVideo;
            }
        }
        if (isLocalVideo) {
            this.photoProgressViews[a].setBackgroundState(3, animated);
        } else {
            this.photoProgressViews[a].setBackgroundState(-1, animated);
        }
    }

    public int getSelectiongLength() {
        return this.captionEditText != null ? this.captionEditText.getSelectionLength() : 0;
    }

    private void setIndexToImage(ImageReceiver imageReceiver, int index) {
        imageReceiver.setOrientation(0, false);
        int size;
        BitmapHolder placeHolder;
        int imageSize;
        String str;
        Drawable bitmapDrawable;
        if (!this.secureDocuments.isEmpty()) {
            imageReceiver.setParentMessageObject(null);
            if (index >= 0 && index < this.secureDocuments.size()) {
                Object object = this.secureDocuments.get(index);
                size = (int) (((float) AndroidUtilities.getPhotoSize()) / AndroidUtilities.density);
                placeHolder = null;
                if (this.currentThumb != null && imageReceiver == this.centerImage) {
                    placeHolder = this.currentThumb;
                }
                if (placeHolder == null) {
                    placeHolder = this.placeProvider.getThumbForPhoto(null, null, index);
                }
                SecureDocument document = (SecureDocument) this.secureDocuments.get(index);
                imageSize = document.secureFile.size;
                str = "d";
                if (placeHolder != null) {
                    bitmapDrawable = new BitmapDrawable(placeHolder.bitmap);
                } else {
                    bitmapDrawable = null;
                }
                imageReceiver.setImage(document, null, str, bitmapDrawable, null, null, imageSize, null, 0);
            }
        } else if (this.imagesArrLocals.isEmpty()) {
            int[] size2 = new int[1];
            TLObject fileLocation = getFileLocation(index, size2);
            if (fileLocation != null) {
                MessageObject messageObject = null;
                if (!this.imagesArr.isEmpty()) {
                    messageObject = (MessageObject) this.imagesArr.get(index);
                }
                imageReceiver.setParentMessageObject(messageObject);
                if (messageObject != null) {
                    imageReceiver.setShouldGenerateQualityThumb(true);
                }
                PhotoSize thumbLocation;
                Drawable drawable;
                if (messageObject != null && messageObject.isVideo()) {
                    imageReceiver.setNeedsQualityThumb(true);
                    if (messageObject.photoThumbs == null || messageObject.photoThumbs.isEmpty()) {
                        imageReceiver.setImageBitmap(this.parentActivity.getResources().getDrawable(R.drawable.photoview_placeholder));
                        return;
                    }
                    placeHolder = null;
                    if (this.currentThumb != null && imageReceiver == this.centerImage) {
                        placeHolder = this.currentThumb;
                    }
                    thumbLocation = FileLoader.getClosestPhotoSizeWithSize(messageObject.photoThumbs, 100);
                    if (placeHolder != null) {
                        r0 = new BitmapDrawable(placeHolder.bitmap);
                    } else {
                        drawable = null;
                    }
                    imageReceiver.setImage(null, null, null, drawable, thumbLocation.location, "b", 0, null, 1);
                    return;
                } else if (messageObject == null || this.currentAnimation == null) {
                    FileLocation fileLocation2;
                    int i;
                    imageReceiver.setNeedsQualityThumb(true);
                    placeHolder = null;
                    if (this.currentThumb != null && imageReceiver == this.centerImage) {
                        placeHolder = this.currentThumb;
                    }
                    if (size2[0] == 0) {
                        size2[0] = -1;
                    }
                    thumbLocation = messageObject != null ? FileLoader.getClosestPhotoSizeWithSize(messageObject.photoThumbs, 100) : null;
                    if (thumbLocation != null && thumbLocation.location == fileLocation) {
                        thumbLocation = null;
                    }
                    boolean cacheOnly = (messageObject != null && messageObject.isWebpage()) || this.avatarsDialogId != 0 || this.isEvent;
                    if (placeHolder != null) {
                        r0 = new BitmapDrawable(placeHolder.bitmap);
                    } else {
                        drawable = null;
                    }
                    if (thumbLocation != null) {
                        fileLocation2 = thumbLocation.location;
                    } else {
                        fileLocation2 = null;
                    }
                    String str2 = "b";
                    int i2 = size2[0];
                    if (cacheOnly) {
                        i = 1;
                    } else {
                        i = 0;
                    }
                    imageReceiver.setImage(fileLocation, null, null, drawable, fileLocation2, str2, i2, null, i);
                    return;
                } else {
                    imageReceiver.setImageBitmap(this.currentAnimation);
                    this.currentAnimation.setSecondParentView(this.containerView);
                    return;
                }
            }
            imageReceiver.setNeedsQualityThumb(true);
            imageReceiver.setParentMessageObject(null);
            if (size2[0] == 0) {
                imageReceiver.setImageBitmap((Bitmap) null);
            } else {
                imageReceiver.setImageBitmap(this.parentActivity.getResources().getDrawable(R.drawable.photoview_placeholder));
            }
        } else {
            imageReceiver.setParentMessageObject(null);
            if (index < 0 || index >= this.imagesArrLocals.size()) {
                imageReceiver.setImageBitmap((Bitmap) null);
                return;
            }
            PhotoEntry object2 = this.imagesArrLocals.get(index);
            size = (int) (((float) AndroidUtilities.getPhotoSize()) / AndroidUtilities.density);
            placeHolder = null;
            if (this.currentThumb != null && imageReceiver == this.centerImage) {
                placeHolder = this.currentThumb;
            }
            if (placeHolder == null) {
                placeHolder = this.placeProvider.getThumbForPhoto(null, null, index);
            }
            str = null;
            Document document2 = null;
            TLObject webDocument = null;
            FileLocation photo = null;
            imageSize = 0;
            String filter = null;
            boolean isVideo = false;
            int cacheType = 0;
            if (object2 instanceof PhotoEntry) {
                PhotoEntry photoEntry = object2;
                isVideo = photoEntry.isVideo;
                if (photoEntry.isVideo) {
                    str = photoEntry.thumbPath != null ? photoEntry.thumbPath : "vthumb://" + photoEntry.imageId + ":" + photoEntry.path;
                } else {
                    if (photoEntry.imagePath != null) {
                        str = photoEntry.imagePath;
                    } else {
                        imageReceiver.setOrientation(photoEntry.orientation, false);
                        str = photoEntry.path;
                    }
                    filter = String.format(Locale.US, "%d_%d", new Object[]{Integer.valueOf(size), Integer.valueOf(size)});
                }
            } else if (object2 instanceof BotInlineResult) {
                cacheType = 1;
                BotInlineResult botInlineResult = (BotInlineResult) object2;
                if (botInlineResult.type.equals(MimeTypes.BASE_TYPE_VIDEO) || MessageObject.isVideoDocument(botInlineResult.document)) {
                    if (botInlineResult.document != null) {
                        photo = botInlineResult.document.thumb.location;
                    } else if (botInlineResult.thumb instanceof TL_webDocument) {
                        webDocument = WebFile.createWithWebDocument(botInlineResult.thumb);
                    }
                } else if (botInlineResult.type.equals("gif") && botInlineResult.document != null) {
                    document2 = botInlineResult.document;
                    imageSize = botInlineResult.document.size;
                    filter = "d";
                } else if (botInlineResult.photo != null) {
                    PhotoSize sizeFull = FileLoader.getClosestPhotoSizeWithSize(botInlineResult.photo.sizes, AndroidUtilities.getPhotoSize());
                    photo = sizeFull.location;
                    imageSize = sizeFull.size;
                    filter = String.format(Locale.US, "%d_%d", new Object[]{Integer.valueOf(size), Integer.valueOf(size)});
                } else if (botInlineResult.content instanceof TL_webDocument) {
                    if (botInlineResult.type.equals("gif")) {
                        filter = "d";
                    } else {
                        filter = String.format(Locale.US, "%d_%d", new Object[]{Integer.valueOf(size), Integer.valueOf(size)});
                    }
                    webDocument = WebFile.createWithWebDocument(botInlineResult.content);
                }
            } else if (object2 instanceof SearchImage) {
                cacheType = 1;
                SearchImage photoEntry2 = (SearchImage) object2;
                if (photoEntry2.photoSize != null) {
                    photo = photoEntry2.photoSize.location;
                    imageSize = photoEntry2.photoSize.size;
                } else if (photoEntry2.imagePath != null) {
                    str = photoEntry2.imagePath;
                } else if (photoEntry2.document != null) {
                    document2 = photoEntry2.document;
                    imageSize = photoEntry2.document.size;
                } else {
                    str = photoEntry2.imageUrl;
                    imageSize = photoEntry2.size;
                }
                filter = "d";
            }
            if (document2 != null) {
                FileLocation fileLocation3;
                str = "d";
                if (placeHolder != null) {
                    bitmapDrawable = new BitmapDrawable(placeHolder.bitmap);
                } else {
                    bitmapDrawable = null;
                }
                if (placeHolder == null) {
                    fileLocation3 = document2.thumb.location;
                } else {
                    fileLocation3 = null;
                }
                imageReceiver.setImage(document2, null, str, bitmapDrawable, fileLocation3, String.format(Locale.US, "%d_%d", new Object[]{Integer.valueOf(size), Integer.valueOf(size)}), imageSize, null, cacheType);
            } else if (photo != null) {
                Drawable drawable2;
                if (placeHolder != null) {
                    r0 = new BitmapDrawable(placeHolder.bitmap);
                } else {
                    drawable2 = null;
                }
                imageReceiver.setImage(photo, null, filter, drawable2, null, String.format(Locale.US, "%d_%d", new Object[]{Integer.valueOf(size), Integer.valueOf(size)}), imageSize, null, cacheType);
            } else if (webDocument != null) {
                Drawable drawable3;
                if (placeHolder != null) {
                    r0 = new BitmapDrawable(placeHolder.bitmap);
                } else {
                    drawable3 = (!isVideo || this.parentActivity == null) ? null : this.parentActivity.getResources().getDrawable(R.drawable.nophotos);
                }
                imageReceiver.setImage(webDocument, filter, drawable3, null, cacheType);
            } else {
                Drawable bitmapDrawable2 = placeHolder != null ? new BitmapDrawable(placeHolder.bitmap) : (!isVideo || this.parentActivity == null) ? null : this.parentActivity.getResources().getDrawable(R.drawable.nophotos);
                imageReceiver.setImage(str, filter, bitmapDrawable2, null, imageSize);
            }
        }
    }

    public static boolean isShowingImage(MessageObject object) {
        boolean result = false;
        if (Instance != null) {
            result = (Instance.pipAnimationInProgress || !Instance.isVisible || Instance.disableShowCheck || object == null || Instance.currentMessageObject == null || Instance.currentMessageObject.getId() != object.getId()) ? false : true;
        }
        if (result || PipInstance == null) {
            return result;
        }
        if (!PipInstance.isVisible || PipInstance.disableShowCheck || object == null || PipInstance.currentMessageObject == null || PipInstance.currentMessageObject.getId() != object.getId()) {
            return false;
        }
        return true;
    }

    public static boolean isShowingImage(FileLocation object) {
        if (Instance != null) {
            return Instance.isVisible && !Instance.disableShowCheck && object != null && Instance.currentFileLocation != null && object.local_id == Instance.currentFileLocation.local_id && object.volume_id == Instance.currentFileLocation.volume_id && object.dc_id == Instance.currentFileLocation.dc_id;
        } else {
            return false;
        }
    }

    public static boolean isShowingImage(BotInlineResult object) {
        if (Instance != null) {
            return (!Instance.isVisible || Instance.disableShowCheck || object == null || Instance.currentBotInlineResult == null || object.id != Instance.currentBotInlineResult.id) ? false : true;
        } else {
            return false;
        }
    }

    public static boolean isShowingImage(String object) {
        if (Instance != null) {
            return (!Instance.isVisible || Instance.disableShowCheck || object == null || Instance.currentPathObject == null || !object.equals(Instance.currentPathObject)) ? false : true;
        } else {
            return false;
        }
    }

    public void setParentChatActivity(ChatActivity chatActivity) {
        this.parentChatActivity = chatActivity;
    }

    public void setMaxSelectedPhotos(int value) {
        this.maxSelectedPhotos = value;
    }

    public boolean openPhoto(MessageObject messageObject, long dialogId, long mergeDialogId, PhotoViewerProvider provider) {
        return openPhoto(messageObject, null, null, null, null, 0, provider, null, dialogId, mergeDialogId);
    }

    public boolean openPhoto(FileLocation fileLocation, PhotoViewerProvider provider) {
        return openPhoto(null, fileLocation, null, null, null, 0, provider, null, 0, 0);
    }

    public boolean openPhoto(ArrayList<MessageObject> messages, int index, long dialogId, long mergeDialogId, PhotoViewerProvider provider) {
        return openPhoto((MessageObject) messages.get(index), null, messages, null, null, index, provider, null, dialogId, mergeDialogId);
    }

    public boolean openPhoto(ArrayList<SecureDocument> documents, int index, PhotoViewerProvider provider) {
        return openPhoto(null, null, null, documents, null, index, provider, null, 0, 0);
    }

    public boolean openPhotoForSelect(ArrayList<Object> photos, int index, int type, PhotoViewerProvider provider, ChatActivity chatActivity) {
        this.sendPhotoType = type;
        if (this.pickerViewSendButton != null) {
            FrameLayout.LayoutParams layoutParams2 = (FrameLayout.LayoutParams) this.pickerViewSendButton.getLayoutParams();
            if (this.sendPhotoType == 4 || this.sendPhotoType == 5) {
                this.pickerViewSendButton.setImageResource(R.drawable.ic_send);
                this.pickerViewSendButton.setPadding(AndroidUtilities.dp(4.0f), 0, 0, 0);
                layoutParams2.bottomMargin = AndroidUtilities.dp(19.0f);
            } else if (this.sendPhotoType == 1) {
                this.pickerViewSendButton.setImageResource(R.drawable.bigcheck);
                this.pickerViewSendButton.setPadding(0, AndroidUtilities.dp(1.0f), 0, 0);
                layoutParams2.bottomMargin = AndroidUtilities.dp(19.0f);
            } else {
                this.pickerViewSendButton.setImageResource(R.drawable.ic_send);
                this.pickerViewSendButton.setPadding(AndroidUtilities.dp(4.0f), 0, 0, 0);
                layoutParams2.bottomMargin = AndroidUtilities.dp(14.0f);
            }
            this.pickerViewSendButton.setLayoutParams(layoutParams2);
        }
        return openPhoto(null, null, null, null, photos, index, provider, chatActivity, 0, 0);
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

    public boolean openPhoto(MessageObject messageObject, FileLocation fileLocation, ArrayList<MessageObject> messages, ArrayList<SecureDocument> documents, ArrayList<Object> photos, int index, PhotoViewerProvider provider, ChatActivity chatActivity, long dialogId, long mDialogId) {
        if (this.parentActivity == null || this.isVisible || ((provider == null && checkAnimation()) || (messageObject == null && fileLocation == null && messages == null && photos == null && documents == null))) {
            return false;
        }
        final PlaceProviderObject object = provider.getPlaceForPhoto(messageObject, fileLocation, index);
        if (object == null && photos == null) {
            return false;
        }
        this.lastInsets = null;
        WindowManager wm = (WindowManager) this.parentActivity.getSystemService("window");
        if (this.attachedToWindow) {
            try {
                wm.removeView(this.windowView);
            } catch (Exception e) {
            }
        }
        try {
            this.windowLayoutParams.type = 99;
            if (VERSION.SDK_INT >= 21) {
                this.windowLayoutParams.flags = -2147417848;
            } else {
                this.windowLayoutParams.flags = 8;
            }
            this.windowLayoutParams.softInputMode = 272;
            this.windowView.setFocusable(false);
            this.containerView.setFocusable(false);
            wm.addView(this.windowView, this.windowLayoutParams);
            this.doneButtonPressed = false;
            this.parentChatActivity = chatActivity;
            this.actionBar.setTitle(LocaleController.formatString("Of", R.string.Of, Integer.valueOf(1), Integer.valueOf(1)));
            NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.FileDidFailedLoad);
            NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.FileDidLoaded);
            NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.FileLoadProgressChanged);
            NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.mediaCountDidLoaded);
            NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.mediaDidLoaded);
            NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.dialogPhotosLoaded);
            NotificationCenter.getGlobalInstance().addObserver(this, NotificationCenter.emojiDidLoaded);
            NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.FilePreparingFailed);
            NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.FileNewChunkAvailable);
            this.placeProvider = provider;
            this.mergeDialogId = mDialogId;
            this.currentDialogId = dialogId;
            this.selectedPhotosAdapter.notifyDataSetChanged();
            if (this.velocityTracker == null) {
                this.velocityTracker = VelocityTracker.obtain();
            }
            this.isVisible = true;
            toggleActionBar(true, false);
            togglePhotosListView(false, false);
            if (object != null) {
                float scale;
                this.disableShowCheck = true;
                this.animationInProgress = 1;
                if (messageObject != null) {
                    this.currentAnimation = object.imageReceiver.getAnimation();
                }
                onPhotoShow(messageObject, fileLocation, messages, documents, photos, index, object);
                Rect drawRegion = object.imageReceiver.getDrawRegion();
                int orientation = object.imageReceiver.getOrientation();
                int animatedOrientation = object.imageReceiver.getAnimatedOrientation();
                if (animatedOrientation != 0) {
                    orientation = animatedOrientation;
                }
                this.animatingImageView.setVisibility(0);
                this.animatingImageView.setRadius(object.radius);
                this.animatingImageView.setOrientation(orientation);
                this.animatingImageView.setNeedRadius(object.radius != 0);
                this.animatingImageView.setImageBitmap(object.thumb);
                this.animatingImageView.setAlpha(1.0f);
                this.animatingImageView.setPivotX(0.0f);
                this.animatingImageView.setPivotY(0.0f);
                this.animatingImageView.setScaleX(object.scale);
                this.animatingImageView.setScaleY(object.scale);
                this.animatingImageView.setTranslationX(((float) object.viewX) + (((float) drawRegion.left) * object.scale));
                this.animatingImageView.setTranslationY(((float) object.viewY) + (((float) drawRegion.top) * object.scale));
                ViewGroup.LayoutParams layoutParams = this.animatingImageView.getLayoutParams();
                layoutParams.width = drawRegion.right - drawRegion.left;
                layoutParams.height = drawRegion.bottom - drawRegion.top;
                this.animatingImageView.setLayoutParams(layoutParams);
                float scaleX = ((float) AndroidUtilities.displaySize.x) / ((float) layoutParams.width);
                float scaleY = ((float) ((VERSION.SDK_INT >= 21 ? AndroidUtilities.statusBarHeight : 0) + AndroidUtilities.displaySize.y)) / ((float) layoutParams.height);
                if (scaleX > scaleY) {
                    scale = scaleY;
                } else {
                    scale = scaleX;
                }
                float xPos = (((float) AndroidUtilities.displaySize.x) - (((float) layoutParams.width) * scale)) / 2.0f;
                float yPos = (((float) ((VERSION.SDK_INT >= 21 ? AndroidUtilities.statusBarHeight : 0) + AndroidUtilities.displaySize.y)) - (((float) layoutParams.height) * scale)) / 2.0f;
                int clipHorizontal = Math.abs(drawRegion.left - object.imageReceiver.getImageX());
                int clipVertical = Math.abs(drawRegion.top - object.imageReceiver.getImageY());
                int[] coords2 = new int[2];
                object.parentView.getLocationInWindow(coords2);
                int clipTop = ((coords2[1] - (VERSION.SDK_INT >= 21 ? 0 : AndroidUtilities.statusBarHeight)) - (object.viewY + drawRegion.top)) + object.clipTopAddition;
                if (clipTop < 0) {
                    clipTop = 0;
                }
                int clipBottom = ((layoutParams.height + (object.viewY + drawRegion.top)) - ((object.parentView.getHeight() + coords2[1]) - (VERSION.SDK_INT >= 21 ? 0 : AndroidUtilities.statusBarHeight))) + object.clipBottomAddition;
                if (clipBottom < 0) {
                    clipBottom = 0;
                }
                clipTop = Math.max(clipTop, clipVertical);
                clipBottom = Math.max(clipBottom, clipVertical);
                this.animationValues[0][0] = this.animatingImageView.getScaleX();
                this.animationValues[0][1] = this.animatingImageView.getScaleY();
                this.animationValues[0][2] = this.animatingImageView.getTranslationX();
                this.animationValues[0][3] = this.animatingImageView.getTranslationY();
                this.animationValues[0][4] = ((float) clipHorizontal) * object.scale;
                this.animationValues[0][5] = ((float) clipTop) * object.scale;
                this.animationValues[0][6] = ((float) clipBottom) * object.scale;
                this.animationValues[0][7] = (float) this.animatingImageView.getRadius();
                this.animationValues[1][0] = scale;
                this.animationValues[1][1] = scale;
                this.animationValues[1][2] = xPos;
                this.animationValues[1][3] = yPos;
                this.animationValues[1][4] = 0.0f;
                this.animationValues[1][5] = 0.0f;
                this.animationValues[1][6] = 0.0f;
                this.animationValues[1][7] = 0.0f;
                this.animatingImageView.setAnimationProgress(0.0f);
                this.backgroundDrawable.setAlpha(0);
                this.containerView.setAlpha(0.0f);
                final AnimatorSet animatorSet = new AnimatorSet();
                animatorSet.playTogether(new Animator[]{ObjectAnimator.ofFloat(this.animatingImageView, "animationProgress", new float[]{0.0f, 1.0f}), ObjectAnimator.ofInt(this.backgroundDrawable, "alpha", new int[]{0, 255}), ObjectAnimator.ofFloat(this.containerView, "alpha", new float[]{0.0f, 1.0f})});
                final ArrayList<Object> arrayList = photos;
                this.animationEndRunnable = new Runnable() {
                    public void run() {
                        if (PhotoViewer.this.containerView != null && PhotoViewer.this.windowView != null) {
                            if (VERSION.SDK_INT >= 18) {
                                PhotoViewer.this.containerView.setLayerType(0, null);
                            }
                            PhotoViewer.this.animationInProgress = 0;
                            PhotoViewer.this.transitionAnimationStartTime = 0;
                            PhotoViewer.this.setImages();
                            PhotoViewer.this.containerView.invalidate();
                            PhotoViewer.this.animatingImageView.setVisibility(8);
                            if (PhotoViewer.this.showAfterAnimation != null) {
                                PhotoViewer.this.showAfterAnimation.imageReceiver.setVisible(true, true);
                            }
                            if (PhotoViewer.this.hideAfterAnimation != null) {
                                PhotoViewer.this.hideAfterAnimation.imageReceiver.setVisible(false, true);
                            }
                            if (arrayList != null && PhotoViewer.this.sendPhotoType != 3) {
                                if (VERSION.SDK_INT >= 21) {
                                    PhotoViewer.this.windowLayoutParams.flags = -2147417856;
                                } else {
                                    PhotoViewer.this.windowLayoutParams.flags = 0;
                                }
                                PhotoViewer.this.windowLayoutParams.softInputMode = 272;
                                ((WindowManager) PhotoViewer.this.parentActivity.getSystemService("window")).updateViewLayout(PhotoViewer.this.windowView, PhotoViewer.this.windowLayoutParams);
                                PhotoViewer.this.windowView.setFocusable(true);
                                PhotoViewer.this.containerView.setFocusable(true);
                            }
                        }
                    }
                };
                animatorSet.setDuration(200);
                animatorSet.addListener(new AnimatorListenerAdapter() {
                    public void onAnimationEnd(Animator animation) {
                        AndroidUtilities.runOnUIThread(new Runnable() {
                            public void run() {
                                NotificationCenter.getInstance(PhotoViewer.this.currentAccount).setAnimationInProgress(false);
                                if (PhotoViewer.this.animationEndRunnable != null) {
                                    PhotoViewer.this.animationEndRunnable.run();
                                    PhotoViewer.this.animationEndRunnable = null;
                                }
                            }
                        });
                    }
                });
                this.transitionAnimationStartTime = System.currentTimeMillis();
                AndroidUtilities.runOnUIThread(new Runnable() {
                    public void run() {
                        NotificationCenter.getInstance(PhotoViewer.this.currentAccount).setAllowedNotificationsDutingAnimation(new int[]{NotificationCenter.dialogsNeedReload, NotificationCenter.closeChats, NotificationCenter.mediaCountDidLoaded, NotificationCenter.mediaDidLoaded, NotificationCenter.dialogPhotosLoaded});
                        NotificationCenter.getInstance(PhotoViewer.this.currentAccount).setAnimationInProgress(true);
                        animatorSet.start();
                    }
                });
                if (VERSION.SDK_INT >= 18) {
                    this.containerView.setLayerType(2, null);
                }
                this.backgroundDrawable.drawRunnable = new Runnable() {
                    public void run() {
                        PhotoViewer.this.disableShowCheck = false;
                        object.imageReceiver.setVisible(false, true);
                    }
                };
            } else {
                if (!(photos == null || this.sendPhotoType == 3)) {
                    if (VERSION.SDK_INT >= 21) {
                        this.windowLayoutParams.flags = -2147417856;
                    } else {
                        this.windowLayoutParams.flags = 0;
                    }
                    this.windowLayoutParams.softInputMode = 272;
                    wm.updateViewLayout(this.windowView, this.windowLayoutParams);
                    this.windowView.setFocusable(true);
                    this.containerView.setFocusable(true);
                }
                this.backgroundDrawable.setAlpha(255);
                this.containerView.setAlpha(1.0f);
                onPhotoShow(messageObject, fileLocation, messages, documents, photos, index, object);
            }
            return true;
        } catch (Throwable e2) {
            FileLog.e(e2);
            return false;
        }
    }

    public void closePhoto(boolean animated, boolean fromEditMode) {
        if (fromEditMode || this.currentEditMode == 0) {
            if (this.qualityChooseView == null || this.qualityChooseView.getTag() == null) {
                try {
                    if (this.visibleDialog != null) {
                        this.visibleDialog.dismiss();
                        this.visibleDialog = null;
                    }
                } catch (Throwable e) {
                    FileLog.e(e);
                }
                if (!(VERSION.SDK_INT < 21 || this.actionBar == null || (this.windowLayoutParams.flags & 1024) == 0)) {
                    LayoutParams layoutParams = this.windowLayoutParams;
                    layoutParams.flags &= -1025;
                    ((WindowManager) this.parentActivity.getSystemService("window")).updateViewLayout(this.windowView, this.windowLayoutParams);
                }
                if (this.currentEditMode != 0) {
                    if (this.currentEditMode == 2) {
                        this.photoFilterView.shutdown();
                        this.containerView.removeView(this.photoFilterView);
                        this.photoFilterView = null;
                    } else if (this.currentEditMode == 1) {
                        this.editorDoneLayout.setVisibility(8);
                        this.photoCropView.setVisibility(8);
                    } else if (this.currentEditMode == 3) {
                        this.photoPaintView.shutdown();
                        this.containerView.removeView(this.photoPaintView);
                        this.photoPaintView = null;
                    }
                    this.currentEditMode = 0;
                }
                if (this.parentActivity == null) {
                    return;
                }
                if ((this.isInline || this.isVisible) && !checkAnimation() && this.placeProvider != null) {
                    if (!this.captionEditText.hideActionMode() || fromEditMode) {
                        releasePlayer();
                        this.captionEditText.onDestroy();
                        this.parentChatActivity = null;
                        removeObservers();
                        this.isActionBarVisible = false;
                        if (this.velocityTracker != null) {
                            this.velocityTracker.recycle();
                            this.velocityTracker = null;
                        }
                        PlaceProviderObject object = this.placeProvider.getPlaceForPhoto(this.currentMessageObject, this.currentFileLocation, this.currentIndex);
                        if (this.isInline) {
                            this.isInline = false;
                            this.animationInProgress = 0;
                            onPhotoClosed(object);
                            this.containerView.setScaleX(1.0f);
                            this.containerView.setScaleY(1.0f);
                            return;
                        }
                        AnimatorSet animatorSet;
                        Animator[] animatorArr;
                        final PlaceProviderObject placeProviderObject;
                        if (animated) {
                            float scale2;
                            this.animationInProgress = 1;
                            this.animatingImageView.setVisibility(0);
                            this.containerView.invalidate();
                            animatorSet = new AnimatorSet();
                            ViewGroup.LayoutParams layoutParams2 = this.animatingImageView.getLayoutParams();
                            Rect drawRegion = null;
                            int orientation = this.centerImage.getOrientation();
                            int animatedOrientation = 0;
                            if (!(object == null || object.imageReceiver == null)) {
                                animatedOrientation = object.imageReceiver.getAnimatedOrientation();
                            }
                            if (animatedOrientation != 0) {
                                orientation = animatedOrientation;
                            }
                            this.animatingImageView.setOrientation(orientation);
                            if (object != null) {
                                this.animatingImageView.setNeedRadius(object.radius != 0);
                                drawRegion = object.imageReceiver.getDrawRegion();
                                layoutParams2.width = drawRegion.right - drawRegion.left;
                                layoutParams2.height = drawRegion.bottom - drawRegion.top;
                                this.animatingImageView.setImageBitmap(object.thumb);
                            } else {
                                this.animatingImageView.setNeedRadius(false);
                                layoutParams2.width = this.centerImage.getImageWidth();
                                layoutParams2.height = this.centerImage.getImageHeight();
                                this.animatingImageView.setImageBitmap(this.centerImage.getBitmapSafe());
                            }
                            this.animatingImageView.setLayoutParams(layoutParams2);
                            float scaleX = ((float) AndroidUtilities.displaySize.x) / ((float) layoutParams2.width);
                            float scaleY = ((float) ((VERSION.SDK_INT >= 21 ? AndroidUtilities.statusBarHeight : 0) + AndroidUtilities.displaySize.y)) / ((float) layoutParams2.height);
                            if (scaleX > scaleY) {
                                scale2 = scaleY;
                            } else {
                                scale2 = scaleX;
                            }
                            float yPos = (((float) ((VERSION.SDK_INT >= 21 ? AndroidUtilities.statusBarHeight : 0) + AndroidUtilities.displaySize.y)) - ((((float) layoutParams2.height) * this.scale) * scale2)) / 2.0f;
                            this.animatingImageView.setTranslationX(this.translationX + ((((float) AndroidUtilities.displaySize.x) - ((((float) layoutParams2.width) * this.scale) * scale2)) / 2.0f));
                            this.animatingImageView.setTranslationY(this.translationY + yPos);
                            this.animatingImageView.setScaleX(this.scale * scale2);
                            this.animatingImageView.setScaleY(this.scale * scale2);
                            if (object != null) {
                                object.imageReceiver.setVisible(false, true);
                                int clipHorizontal = Math.abs(drawRegion.left - object.imageReceiver.getImageX());
                                int clipVertical = Math.abs(drawRegion.top - object.imageReceiver.getImageY());
                                int[] coords2 = new int[2];
                                object.parentView.getLocationInWindow(coords2);
                                int clipTop = ((coords2[1] - (VERSION.SDK_INT >= 21 ? 0 : AndroidUtilities.statusBarHeight)) - (object.viewY + drawRegion.top)) + object.clipTopAddition;
                                if (clipTop < 0) {
                                    clipTop = 0;
                                }
                                int clipBottom = (((drawRegion.bottom - drawRegion.top) + (object.viewY + drawRegion.top)) - ((object.parentView.getHeight() + coords2[1]) - (VERSION.SDK_INT >= 21 ? 0 : AndroidUtilities.statusBarHeight))) + object.clipBottomAddition;
                                if (clipBottom < 0) {
                                    clipBottom = 0;
                                }
                                clipTop = Math.max(clipTop, clipVertical);
                                clipBottom = Math.max(clipBottom, clipVertical);
                                this.animationValues[0][0] = this.animatingImageView.getScaleX();
                                this.animationValues[0][1] = this.animatingImageView.getScaleY();
                                this.animationValues[0][2] = this.animatingImageView.getTranslationX();
                                this.animationValues[0][3] = this.animatingImageView.getTranslationY();
                                this.animationValues[0][4] = 0.0f;
                                this.animationValues[0][5] = 0.0f;
                                this.animationValues[0][6] = 0.0f;
                                this.animationValues[0][7] = 0.0f;
                                this.animationValues[1][0] = object.scale;
                                this.animationValues[1][1] = object.scale;
                                this.animationValues[1][2] = (((float) object.viewX) + (((float) drawRegion.left) * object.scale)) - ((float) getLeftInset());
                                this.animationValues[1][3] = ((float) object.viewY) + (((float) drawRegion.top) * object.scale);
                                this.animationValues[1][4] = ((float) clipHorizontal) * object.scale;
                                this.animationValues[1][5] = ((float) clipTop) * object.scale;
                                this.animationValues[1][6] = ((float) clipBottom) * object.scale;
                                this.animationValues[1][7] = (float) object.radius;
                                animatorArr = new Animator[3];
                                float[] fArr = new float[2];
                                animatorArr[0] = ObjectAnimator.ofFloat(this.animatingImageView, "animationProgress", new float[]{0.0f, 1.0f});
                                animatorArr[1] = ObjectAnimator.ofInt(this.backgroundDrawable, "alpha", new int[]{0});
                                animatorArr[2] = ObjectAnimator.ofFloat(this.containerView, "alpha", new float[]{0.0f});
                                animatorSet.playTogether(animatorArr);
                            } else {
                                float f;
                                int h = AndroidUtilities.displaySize.y + (VERSION.SDK_INT >= 21 ? AndroidUtilities.statusBarHeight : 0);
                                Animator[] animatorArr2 = new Animator[4];
                                animatorArr2[0] = ObjectAnimator.ofInt(this.backgroundDrawable, "alpha", new int[]{0});
                                animatorArr2[1] = ObjectAnimator.ofFloat(this.animatingImageView, "alpha", new float[]{0.0f});
                                ClippingImageView clippingImageView = this.animatingImageView;
                                String str = "translationY";
                                float[] fArr2 = new float[1];
                                if (this.translationY >= 0.0f) {
                                    f = (float) h;
                                } else {
                                    f = (float) (-h);
                                }
                                fArr2[0] = f;
                                animatorArr2[2] = ObjectAnimator.ofFloat(clippingImageView, str, fArr2);
                                animatorArr2[3] = ObjectAnimator.ofFloat(this.containerView, "alpha", new float[]{0.0f});
                                animatorSet.playTogether(animatorArr2);
                            }
                            placeProviderObject = object;
                            this.animationEndRunnable = new Runnable() {
                                public void run() {
                                    if (VERSION.SDK_INT >= 18) {
                                        PhotoViewer.this.containerView.setLayerType(0, null);
                                    }
                                    PhotoViewer.this.animationInProgress = 0;
                                    PhotoViewer.this.onPhotoClosed(placeProviderObject);
                                }
                            };
                            animatorSet.setDuration(200);
                            animatorSet.addListener(new AnimatorListenerAdapter() {
                                public void onAnimationEnd(Animator animation) {
                                    AndroidUtilities.runOnUIThread(new Runnable() {
                                        public void run() {
                                            if (PhotoViewer.this.animationEndRunnable != null) {
                                                PhotoViewer.this.animationEndRunnable.run();
                                                PhotoViewer.this.animationEndRunnable = null;
                                            }
                                        }
                                    });
                                }
                            });
                            this.transitionAnimationStartTime = System.currentTimeMillis();
                            if (VERSION.SDK_INT >= 18) {
                                this.containerView.setLayerType(2, null);
                            }
                            animatorSet.start();
                        } else {
                            animatorSet = new AnimatorSet();
                            animatorArr = new Animator[4];
                            animatorArr[0] = ObjectAnimator.ofFloat(this.containerView, "scaleX", new float[]{0.9f});
                            animatorArr[1] = ObjectAnimator.ofFloat(this.containerView, "scaleY", new float[]{0.9f});
                            animatorArr[2] = ObjectAnimator.ofInt(this.backgroundDrawable, "alpha", new int[]{0});
                            animatorArr[3] = ObjectAnimator.ofFloat(this.containerView, "alpha", new float[]{0.0f});
                            animatorSet.playTogether(animatorArr);
                            this.animationInProgress = 2;
                            placeProviderObject = object;
                            this.animationEndRunnable = new Runnable() {
                                public void run() {
                                    if (PhotoViewer.this.containerView != null) {
                                        if (VERSION.SDK_INT >= 18) {
                                            PhotoViewer.this.containerView.setLayerType(0, null);
                                        }
                                        PhotoViewer.this.animationInProgress = 0;
                                        PhotoViewer.this.onPhotoClosed(placeProviderObject);
                                        PhotoViewer.this.containerView.setScaleX(1.0f);
                                        PhotoViewer.this.containerView.setScaleY(1.0f);
                                    }
                                }
                            };
                            animatorSet.setDuration(200);
                            animatorSet.addListener(new AnimatorListenerAdapter() {
                                public void onAnimationEnd(Animator animation) {
                                    if (PhotoViewer.this.animationEndRunnable != null) {
                                        PhotoViewer.this.animationEndRunnable.run();
                                        PhotoViewer.this.animationEndRunnable = null;
                                    }
                                }
                            });
                            this.transitionAnimationStartTime = System.currentTimeMillis();
                            if (VERSION.SDK_INT >= 18) {
                                this.containerView.setLayerType(2, null);
                            }
                            animatorSet.start();
                        }
                        if (this.currentAnimation != null) {
                            this.currentAnimation.setSecondParentView(null);
                            this.currentAnimation = null;
                            this.centerImage.setImageBitmap((Drawable) null);
                        }
                        if (this.placeProvider != null && !this.placeProvider.canScrollAway()) {
                            this.placeProvider.cancelButtonPressed();
                            return;
                        }
                        return;
                    }
                    return;
                }
                return;
            }
            this.qualityPicker.cancelButton.callOnClick();
        } else if (this.currentEditMode != 3 || this.photoPaintView == null) {
            if (this.currentEditMode == 1) {
                this.photoCropView.cancelAnimationRunnable();
            }
            switchToEditMode(0);
        } else {
            this.photoPaintView.maybeShowDismissalAlert(this, this.parentActivity, new PhotoViewer$$Lambda$0(this));
        }
    }

    final /* synthetic */ void lambda$closePhoto$0$PhotoViewer() {
        switchToEditMode(0);
    }

    private void removeObservers() {
        NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.FileDidFailedLoad);
        NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.FileDidLoaded);
        NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.FileLoadProgressChanged);
        NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.mediaCountDidLoaded);
        NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.mediaDidLoaded);
        NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.dialogPhotosLoaded);
        NotificationCenter.getGlobalInstance().removeObserver(this, NotificationCenter.emojiDidLoaded);
        NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.FilePreparingFailed);
        NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.FileNewChunkAvailable);
        ConnectionsManager.getInstance(this.currentAccount).cancelRequestsForGuid(this.classGuid);
    }

    public void destroyPhotoViewer() {
        if (this.parentActivity != null && this.windowView != null) {
            if (this.pipVideoView != null) {
                this.pipVideoView.close();
                this.pipVideoView = null;
            }
            removeObservers();
            releasePlayer();
            try {
                if (this.windowView.getParent() != null) {
                    ((WindowManager) this.parentActivity.getSystemService("window")).removeViewImmediate(this.windowView);
                }
                this.windowView = null;
            } catch (Throwable e) {
                FileLog.e(e);
            }
            if (this.currentThumb != null) {
                this.currentThumb.release();
                this.currentThumb = null;
            }
            this.animatingImageView.setImageBitmap(null);
            if (this.captionEditText != null) {
                this.captionEditText.onDestroy();
            }
            if (this == PipInstance) {
                PipInstance = null;
            } else {
                Instance = null;
            }
        }
    }

    private void onPhotoClosed(PlaceProviderObject object) {
        this.isVisible = false;
        this.disableShowCheck = true;
        this.currentMessageObject = null;
        this.currentBotInlineResult = null;
        this.currentFileLocation = null;
        this.currentSecureDocument = null;
        this.currentPathObject = null;
        if (this.currentThumb != null) {
            this.currentThumb.release();
            this.currentThumb = null;
        }
        this.parentAlert = null;
        if (this.currentAnimation != null) {
            this.currentAnimation.setSecondParentView(null);
            this.currentAnimation = null;
        }
        for (int a = 0; a < 3; a++) {
            if (this.photoProgressViews[a] != null) {
                this.photoProgressViews[a].setBackgroundState(-1, false);
            }
        }
        requestVideoPreview(0);
        if (this.videoTimelineView != null) {
            this.videoTimelineView.destroy();
        }
        this.centerImage.setImageBitmap((Bitmap) null);
        this.leftImage.setImageBitmap((Bitmap) null);
        this.rightImage.setImageBitmap((Bitmap) null);
        this.containerView.post(new Runnable() {
            public void run() {
                PhotoViewer.this.animatingImageView.setImageBitmap(null);
                try {
                    if (PhotoViewer.this.windowView.getParent() != null) {
                        ((WindowManager) PhotoViewer.this.parentActivity.getSystemService("window")).removeView(PhotoViewer.this.windowView);
                    }
                } catch (Throwable e) {
                    FileLog.e(e);
                }
            }
        });
        if (this.placeProvider != null) {
            this.placeProvider.willHidePhotoViewer();
        }
        this.groupedPhotosListView.clear();
        this.placeProvider = null;
        this.selectedPhotosAdapter.notifyDataSetChanged();
        this.disableShowCheck = false;
        if (object != null) {
            object.imageReceiver.setVisible(true, true);
        }
    }

    private void redraw(final int count) {
        if (count < 6 && this.containerView != null) {
            this.containerView.invalidate();
            AndroidUtilities.runOnUIThread(new Runnable() {
                public void run() {
                    PhotoViewer.this.redraw(count + 1);
                }
            }, 100);
        }
    }

    public void onResume() {
        redraw(0);
        if (this.videoPlayer != null) {
            this.videoPlayer.seekTo(this.videoPlayer.getCurrentPosition() + 1);
        }
    }

    public void onConfigurationChanged(Configuration newConfig) {
        if (this.pipVideoView != null) {
            this.pipVideoView.onConfigurationChanged();
        }
    }

    public void onPause() {
        if (this.currentAnimation != null) {
            closePhoto(false, false);
        } else if (this.lastTitle != null) {
            closeCaptionEnter(true);
        }
    }

    public boolean isVisible() {
        return this.isVisible && this.placeProvider != null;
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
        } else {
            this.maxY = 0.0f;
            this.minY = 0.0f;
        }
        if (this.currentEditMode == 1) {
            this.maxX += this.photoCropView.getLimitX();
            this.maxY += this.photoCropView.getLimitY();
            this.minX -= this.photoCropView.getLimitWidth();
            this.minY -= this.photoCropView.getLimitHeight();
        }
    }

    private int getAdditionX() {
        if (this.currentEditMode == 0 || this.currentEditMode == 3) {
            return 0;
        }
        return AndroidUtilities.dp(14.0f);
    }

    private int getAdditionY() {
        int i = 0;
        int dp;
        if (this.currentEditMode == 3) {
            dp = AndroidUtilities.dp(8.0f);
            if (VERSION.SDK_INT >= 21) {
                i = AndroidUtilities.statusBarHeight;
            }
            return i + dp;
        } else if (this.currentEditMode == 0) {
            return 0;
        } else {
            dp = AndroidUtilities.dp(14.0f);
            if (VERSION.SDK_INT >= 21) {
                i = AndroidUtilities.statusBarHeight;
            }
            return i + dp;
        }
    }

    private int getContainerViewWidth() {
        return getContainerViewWidth(this.currentEditMode);
    }

    private int getContainerViewWidth(int mode) {
        int width = this.containerView.getWidth();
        if (mode == 0 || mode == 3) {
            return width;
        }
        return width - AndroidUtilities.dp(28.0f);
    }

    private int getContainerViewHeight() {
        return getContainerViewHeight(this.currentEditMode);
    }

    private int getContainerViewHeight(int mode) {
        int height = AndroidUtilities.displaySize.y;
        if (mode == 0 && VERSION.SDK_INT >= 21) {
            height += AndroidUtilities.statusBarHeight;
        }
        if (mode == 1) {
            return height - AndroidUtilities.dp(144.0f);
        }
        if (mode == 2) {
            return height - AndroidUtilities.dp(214.0f);
        }
        if (mode == 3) {
            return height - (AndroidUtilities.dp(48.0f) + ActionBar.getCurrentActionBarHeight());
        }
        return height;
    }

    private boolean onTouchEvent(MotionEvent ev) {
        if (this.animationInProgress != 0 || this.animationStartTime != 0) {
            return false;
        }
        if (this.currentEditMode == 2) {
            this.photoFilterView.onTouch(ev);
            return true;
        } else if (this.currentEditMode == 1) {
            return true;
        } else {
            if (this.captionEditText.isPopupShowing() || this.captionEditText.isKeyboardVisible()) {
                if (ev.getAction() == 1) {
                    closeCaptionEnter(true);
                }
                return true;
            } else if (this.currentEditMode == 0 && ev.getPointerCount() == 1 && this.gestureDetector.onTouchEvent(ev) && this.doubleTap) {
                this.doubleTap = false;
                this.moving = false;
                this.zooming = false;
                checkMinMax(false);
                return true;
            } else {
                if (ev.getActionMasked() == 0 || ev.getActionMasked() == 5) {
                    if (this.currentEditMode == 1) {
                        this.photoCropView.cancelAnimationRunnable();
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
                            float y = ev.getY();
                            this.moveStartY = y;
                            this.dragY = y;
                            this.draggingDown = false;
                            this.canDragDown = true;
                            if (this.velocityTracker != null) {
                                this.velocityTracker.clear();
                            }
                        }
                    }
                } else if (ev.getActionMasked() == 2) {
                    if (this.currentEditMode == 1) {
                        this.photoCropView.cancelAnimationRunnable();
                    }
                    if (this.canZoom && ev.getPointerCount() == 2 && !this.draggingDown && this.zooming && !this.changingPage) {
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
                        float dx = Math.abs(ev.getX() - this.moveStartX);
                        float dy = Math.abs(ev.getY() - this.dragY);
                        if (dx > ((float) AndroidUtilities.dp(3.0f)) || dy > ((float) AndroidUtilities.dp(3.0f))) {
                            this.discardTap = true;
                            if (this.qualityChooseView != null && this.qualityChooseView.getVisibility() == 0) {
                                return true;
                            }
                        }
                        if (this.placeProvider.canScrollAway() && this.currentEditMode == 0 && this.canDragDown && !this.draggingDown && this.scale == 1.0f && dy >= ((float) AndroidUtilities.dp(30.0f)) && dy / 2.0f > dx) {
                            this.draggingDown = true;
                            this.moving = false;
                            this.dragY = ev.getY();
                            if (this.isActionBarVisible && this.containerView.getTag() != null) {
                                toggleActionBar(false, true);
                            } else if (this.pickerView.getVisibility() == 0) {
                                toggleActionBar(false, true);
                                togglePhotosListView(false, true);
                                toggleCheckImageView(false);
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
                            float moveDx = this.moveStartX - ev.getX();
                            float moveDy = this.moveStartY - ev.getY();
                            if (this.moving || this.currentEditMode != 0 || ((this.scale == 1.0f && Math.abs(moveDy) + ((float) AndroidUtilities.dp(12.0f)) < Math.abs(moveDx)) || this.scale != 1.0f)) {
                                if (!this.moving) {
                                    moveDx = 0.0f;
                                    moveDy = 0.0f;
                                    this.moving = true;
                                    this.canDragDown = false;
                                }
                                this.moveStartX = ev.getX();
                                this.moveStartY = ev.getY();
                                updateMinMax(this.scale);
                                if ((this.translationX < this.minX && !(this.currentEditMode == 0 && this.rightImage.hasImage())) || (this.translationX > this.maxX && !(this.currentEditMode == 0 && this.leftImage.hasImage()))) {
                                    moveDx /= 3.0f;
                                }
                                if (this.maxY == 0.0f && this.minY == 0.0f && this.currentEditMode == 0) {
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
                                if (!(this.scale == 1.0f && this.currentEditMode == 0)) {
                                    this.translationY -= moveDy;
                                }
                                this.containerView.invalidate();
                            }
                        }
                    }
                } else if (ev.getActionMasked() == 3 || ev.getActionMasked() == 1 || ev.getActionMasked() == 6) {
                    if (this.currentEditMode == 1) {
                        this.photoCropView.startAnimationRunnable();
                    }
                    if (this.zooming) {
                        this.invalidCoords = true;
                        if (this.scale < 1.0f) {
                            updateMinMax(1.0f);
                            animateTo(1.0f, 0.0f, 0.0f, true);
                        } else if (this.scale > 3.0f) {
                            float atx = (this.pinchCenterX - ((float) (getContainerViewWidth() / 2))) - (((this.pinchCenterX - ((float) (getContainerViewWidth() / 2))) - this.pinchStartX) * (3.0f / this.pinchStartScale));
                            float aty = (this.pinchCenterY - ((float) (getContainerViewHeight() / 2))) - (((this.pinchCenterY - ((float) (getContainerViewHeight() / 2))) - this.pinchStartY) * (3.0f / this.pinchStartScale));
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
                            checkMinMax(true);
                        }
                        this.zooming = false;
                    } else if (this.draggingDown) {
                        if (Math.abs(this.dragY - ev.getY()) > ((float) getContainerViewHeight()) / 6.0f) {
                            closePhoto(true, false);
                        } else {
                            if (this.pickerView.getVisibility() == 0) {
                                toggleActionBar(true, true);
                                toggleCheckImageView(true);
                            }
                            animateTo(1.0f, 0.0f, 0.0f, false);
                        }
                        this.draggingDown = false;
                    } else if (this.moving) {
                        float moveToX = this.translationX;
                        float moveToY = this.translationY;
                        updateMinMax(this.scale);
                        this.moving = false;
                        this.canDragDown = true;
                        float velocity = 0.0f;
                        if (this.velocityTracker != null && this.scale == 1.0f) {
                            this.velocityTracker.computeCurrentVelocity(1000);
                            velocity = this.velocityTracker.getXVelocity();
                        }
                        if (this.currentEditMode == 0) {
                            if ((this.translationX < this.minX - ((float) (getContainerViewWidth() / 3)) || velocity < ((float) (-AndroidUtilities.dp(650.0f)))) && this.rightImage.hasImage()) {
                                goToNext();
                                return true;
                            } else if ((this.translationX > this.maxX + ((float) (getContainerViewWidth() / 3)) || velocity > ((float) AndroidUtilities.dp(650.0f))) && this.leftImage.hasImage()) {
                                goToPrev();
                                return true;
                            }
                        }
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
                        animateTo(this.scale, moveToX, moveToY, false);
                    }
                }
                return false;
            }
        }
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
        animateTo(newScale, newTx, newTy, isZoom, ItemTouchHelper.Callback.DEFAULT_SWIPE_ANIMATION_DURATION);
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
                    PhotoViewer.this.imageMoveAnimation = null;
                    PhotoViewer.this.containerView.invalidate();
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

    private void hideHint() {
        this.hintAnimation = new AnimatorSet();
        AnimatorSet animatorSet = this.hintAnimation;
        Animator[] animatorArr = new Animator[1];
        animatorArr[0] = ObjectAnimator.ofFloat(this.hintTextView, "alpha", new float[]{0.0f});
        animatorSet.playTogether(animatorArr);
        this.hintAnimation.addListener(new AnimatorListenerAdapter() {
            public void onAnimationEnd(Animator animation) {
                if (animation.equals(PhotoViewer.this.hintAnimation)) {
                    PhotoViewer.this.hintAnimation = null;
                    PhotoViewer.this.hintHideRunnable = null;
                    if (PhotoViewer.this.hintTextView != null) {
                        PhotoViewer.this.hintTextView.setVisibility(8);
                    }
                }
            }

            public void onAnimationCancel(Animator animation) {
                if (animation.equals(PhotoViewer.this.hintAnimation)) {
                    PhotoViewer.this.hintHideRunnable = null;
                    PhotoViewer.this.hintHideRunnable = null;
                }
            }
        });
        this.hintAnimation.setDuration(300);
        this.hintAnimation.start();
    }

    private void showHint(boolean hide, boolean enabled) {
        if (this.containerView == null) {
            return;
        }
        if (!hide || this.hintTextView != null) {
            if (this.hintTextView == null) {
                this.hintTextView = new TextView(this.containerView.getContext());
                this.hintTextView.setBackgroundDrawable(Theme.createRoundRectDrawable(AndroidUtilities.dp(3.0f), -871296751));
                this.hintTextView.setTextColor(-1);
                this.hintTextView.setTextSize(1, 14.0f);
                this.hintTextView.setPadding(AndroidUtilities.dp(8.0f), AndroidUtilities.dp(7.0f), AndroidUtilities.dp(8.0f), AndroidUtilities.dp(7.0f));
                this.hintTextView.setGravity(16);
                this.hintTextView.setAlpha(0.0f);
                this.containerView.addView(this.hintTextView, LayoutHelper.createFrame(-2, -2.0f, 51, 5.0f, 0.0f, 5.0f, 3.0f));
            }
            if (hide) {
                if (this.hintAnimation != null) {
                    this.hintAnimation.cancel();
                    this.hintAnimation = null;
                }
                AndroidUtilities.cancelRunOnUIThread(this.hintHideRunnable);
                this.hintHideRunnable = null;
                hideHint();
                return;
            }
            this.hintTextView.setText(enabled ? LocaleController.getString("GroupPhotosHelp", R.string.GroupPhotosHelp) : LocaleController.getString("SinglePhotosHelp", R.string.SinglePhotosHelp));
            if (this.hintHideRunnable != null) {
                if (this.hintAnimation != null) {
                    this.hintAnimation.cancel();
                    this.hintAnimation = null;
                } else {
                    AndroidUtilities.cancelRunOnUIThread(this.hintHideRunnable);
                    Runnable anonymousClass76 = new Runnable() {
                        public void run() {
                            PhotoViewer.this.hideHint();
                        }
                    };
                    this.hintHideRunnable = anonymousClass76;
                    AndroidUtilities.runOnUIThread(anonymousClass76, AdaptiveTrackSelection.DEFAULT_MIN_TIME_BETWEEN_BUFFER_REEVALUTATION_MS);
                    return;
                }
            } else if (this.hintAnimation != null) {
                return;
            }
            this.hintTextView.setVisibility(0);
            this.hintAnimation = new AnimatorSet();
            AnimatorSet animatorSet = this.hintAnimation;
            Animator[] animatorArr = new Animator[1];
            animatorArr[0] = ObjectAnimator.ofFloat(this.hintTextView, "alpha", new float[]{1.0f});
            animatorSet.playTogether(animatorArr);
            this.hintAnimation.addListener(new AnimatorListenerAdapter() {
                public void onAnimationEnd(Animator animation) {
                    if (animation.equals(PhotoViewer.this.hintAnimation)) {
                        PhotoViewer.this.hintAnimation = null;
                        AndroidUtilities.runOnUIThread(PhotoViewer.this.hintHideRunnable = new Runnable() {
                            public void run() {
                                PhotoViewer.this.hideHint();
                            }
                        }, AdaptiveTrackSelection.DEFAULT_MIN_TIME_BETWEEN_BUFFER_REEVALUTATION_MS);
                    }
                }

                public void onAnimationCancel(Animator animation) {
                    if (animation.equals(PhotoViewer.this.hintAnimation)) {
                        PhotoViewer.this.hintAnimation = null;
                    }
                }
            });
            this.hintAnimation.setDuration(300);
            this.hintAnimation.start();
        }
    }

    @SuppressLint({"NewApi", "DrawAllocation"})
    private void onDraw(Canvas canvas) {
        if (this.animationInProgress == 1) {
            return;
        }
        if (this.isVisible || this.animationInProgress == 2 || this.pipAnimationInProgress) {
            float currentScale;
            float currentTranslationY;
            float currentTranslationX;
            float translateX;
            float scaleDiff;
            float alpha;
            int bitmapWidth;
            int bitmapHeight;
            float scaleX;
            float scaleY;
            float scale;
            int width;
            int height;
            float aty = -1.0f;
            if (this.imageMoveAnimation != null) {
                if (!this.scroller.isFinished()) {
                    this.scroller.abortAnimation();
                }
                float ts = this.scale + ((this.animateToScale - this.scale) * this.animationValue);
                float tx = this.translationX + ((this.animateToX - this.translationX) * this.animationValue);
                float ty = this.translationY + ((this.animateToY - this.translationY) * this.animationValue);
                if (this.currentEditMode == 1) {
                    this.photoCropView.setAnimationProgress(this.animationValue);
                }
                if (this.animateToScale == 1.0f && this.scale == 1.0f && this.translationX == 0.0f) {
                    aty = ty;
                }
                currentScale = ts;
                currentTranslationY = ty;
                currentTranslationX = tx;
                this.containerView.invalidate();
            } else {
                if (this.animationStartTime != 0) {
                    this.translationX = this.animateToX;
                    this.translationY = this.animateToY;
                    this.scale = this.animateToScale;
                    this.animationStartTime = 0;
                    if (this.currentEditMode == 1) {
                        this.photoCropView.setAnimationProgress(1.0f);
                    }
                    updateMinMax(this.scale);
                    this.zoomAnimation = false;
                }
                if (!this.scroller.isFinished() && this.scroller.computeScrollOffset()) {
                    if (((float) this.scroller.getStartX()) < this.maxX && ((float) this.scroller.getStartX()) > this.minX) {
                        this.translationX = (float) this.scroller.getCurrX();
                    }
                    if (((float) this.scroller.getStartY()) < this.maxY && ((float) this.scroller.getStartY()) > this.minY) {
                        this.translationY = (float) this.scroller.getCurrY();
                    }
                    this.containerView.invalidate();
                }
                if (this.switchImageAfterAnimation != 0) {
                    if (this.switchImageAfterAnimation == 1) {
                        AndroidUtilities.runOnUIThread(new Runnable() {
                            public void run() {
                                PhotoViewer.this.setImageIndex(PhotoViewer.this.currentIndex + 1, false);
                            }
                        });
                    } else if (this.switchImageAfterAnimation == 2) {
                        AndroidUtilities.runOnUIThread(new Runnable() {
                            public void run() {
                                PhotoViewer.this.setImageIndex(PhotoViewer.this.currentIndex - 1, false);
                            }
                        });
                    }
                    this.switchImageAfterAnimation = 0;
                }
                currentScale = this.scale;
                currentTranslationY = this.translationY;
                currentTranslationX = this.translationX;
                if (!this.moving) {
                    aty = this.translationY;
                }
            }
            if (!(this.animationInProgress == 2 || this.pipAnimationInProgress || this.isInline)) {
                if (this.currentEditMode != 0 || this.scale != 1.0f || aty == -1.0f || this.zoomAnimation) {
                    this.backgroundDrawable.setAlpha(255);
                } else {
                    float maxValue = ((float) getContainerViewHeight()) / 4.0f;
                    this.backgroundDrawable.setAlpha((int) Math.max(127.0f, 255.0f * (1.0f - (Math.min(Math.abs(aty), maxValue) / maxValue))));
                }
            }
            ImageReceiver sideImage = null;
            if (this.currentEditMode == 0) {
                if (!(this.scale < 1.0f || this.zoomAnimation || this.zooming)) {
                    if (currentTranslationX > this.maxX + ((float) AndroidUtilities.dp(5.0f))) {
                        sideImage = this.leftImage;
                    } else if (currentTranslationX < this.minX - ((float) AndroidUtilities.dp(5.0f))) {
                        sideImage = this.rightImage;
                    } else {
                        this.groupedPhotosListView.setMoveProgress(0.0f);
                    }
                }
                this.changingPage = sideImage != null;
            }
            if (sideImage == this.rightImage) {
                translateX = currentTranslationX;
                scaleDiff = 0.0f;
                alpha = 1.0f;
                if (!this.zoomAnimation && translateX < this.minX) {
                    alpha = Math.min(1.0f, (this.minX - translateX) / ((float) canvas.getWidth()));
                    scaleDiff = (1.0f - alpha) * 0.3f;
                    translateX = (float) ((-canvas.getWidth()) - (AndroidUtilities.dp(30.0f) / 2));
                }
                if (sideImage.hasBitmapImage()) {
                    canvas.save();
                    canvas.translate((float) (getContainerViewWidth() / 2), (float) (getContainerViewHeight() / 2));
                    canvas.translate(((float) (canvas.getWidth() + (AndroidUtilities.dp(30.0f) / 2))) + translateX, 0.0f);
                    canvas.scale(1.0f - scaleDiff, 1.0f - scaleDiff);
                    bitmapWidth = sideImage.getBitmapWidth();
                    bitmapHeight = sideImage.getBitmapHeight();
                    scaleX = ((float) getContainerViewWidth()) / ((float) bitmapWidth);
                    scaleY = ((float) getContainerViewHeight()) / ((float) bitmapHeight);
                    if (scaleX > scaleY) {
                        scale = scaleY;
                    } else {
                        scale = scaleX;
                    }
                    width = (int) (((float) bitmapWidth) * scale);
                    height = (int) (((float) bitmapHeight) * scale);
                    sideImage.setAlpha(alpha);
                    sideImage.setImageCoords((-width) / 2, (-height) / 2, width, height);
                    sideImage.draw(canvas);
                    canvas.restore();
                }
                this.groupedPhotosListView.setMoveProgress(-alpha);
                canvas.save();
                canvas.translate(translateX, currentTranslationY / currentScale);
                canvas.translate(((((float) canvas.getWidth()) * (this.scale + 1.0f)) + ((float) AndroidUtilities.dp(30.0f))) / 2.0f, (-currentTranslationY) / currentScale);
                this.photoProgressViews[1].setScale(1.0f - scaleDiff);
                this.photoProgressViews[1].setAlpha(alpha);
                this.photoProgressViews[1].onDraw(canvas);
                canvas.restore();
            }
            translateX = currentTranslationX;
            scaleDiff = 0.0f;
            alpha = 1.0f;
            if (!this.zoomAnimation && translateX > this.maxX && this.currentEditMode == 0) {
                alpha = Math.min(1.0f, (translateX - this.maxX) / ((float) canvas.getWidth()));
                scaleDiff = alpha * 0.3f;
                alpha = 1.0f - alpha;
                translateX = this.maxX;
            }
            boolean drawTextureView = this.aspectRatioFrameLayout != null && this.aspectRatioFrameLayout.getVisibility() == 0;
            if (this.centerImage.hasBitmapImage()) {
                canvas.save();
                canvas.translate((float) ((getContainerViewWidth() / 2) + getAdditionX()), (float) ((getContainerViewHeight() / 2) + getAdditionY()));
                canvas.translate(translateX, currentTranslationY);
                canvas.scale(currentScale - scaleDiff, currentScale - scaleDiff);
                if (this.currentEditMode == 1) {
                    this.photoCropView.setBitmapParams(currentScale, translateX, currentTranslationY);
                }
                bitmapWidth = this.centerImage.getBitmapWidth();
                bitmapHeight = this.centerImage.getBitmapHeight();
                if (drawTextureView && this.textureUploaded && Math.abs((((float) bitmapWidth) / ((float) bitmapHeight)) - (((float) this.videoTextureView.getMeasuredWidth()) / ((float) this.videoTextureView.getMeasuredHeight()))) > 0.01f) {
                    bitmapWidth = this.videoTextureView.getMeasuredWidth();
                    bitmapHeight = this.videoTextureView.getMeasuredHeight();
                }
                scaleX = ((float) getContainerViewWidth()) / ((float) bitmapWidth);
                scaleY = ((float) getContainerViewHeight()) / ((float) bitmapHeight);
                if (scaleX > scaleY) {
                    scale = scaleY;
                } else {
                    scale = scaleX;
                }
                width = (int) (((float) bitmapWidth) * scale);
                height = (int) (((float) bitmapHeight) * scale);
                if (!(drawTextureView && this.textureUploaded && this.videoCrossfadeStarted && this.videoCrossfadeAlpha == 1.0f)) {
                    this.centerImage.setAlpha(alpha);
                    this.centerImage.setImageCoords((-width) / 2, (-height) / 2, width, height);
                    this.centerImage.draw(canvas);
                }
                if (drawTextureView) {
                    if (!this.videoCrossfadeStarted && this.textureUploaded) {
                        this.videoCrossfadeStarted = true;
                        this.videoCrossfadeAlpha = 0.0f;
                        this.videoCrossfadeAlphaLastTime = System.currentTimeMillis();
                    }
                    canvas.translate((float) ((-width) / 2), (float) ((-height) / 2));
                    this.videoTextureView.setAlpha(this.videoCrossfadeAlpha * alpha);
                    this.aspectRatioFrameLayout.draw(canvas);
                    if (this.videoCrossfadeStarted && this.videoCrossfadeAlpha < 1.0f) {
                        long newUpdateTime = System.currentTimeMillis();
                        long dt = newUpdateTime - this.videoCrossfadeAlphaLastTime;
                        this.videoCrossfadeAlphaLastTime = newUpdateTime;
                        this.videoCrossfadeAlpha += ((float) dt) / 200.0f;
                        this.containerView.invalidate();
                        if (this.videoCrossfadeAlpha > 1.0f) {
                            this.videoCrossfadeAlpha = 1.0f;
                        }
                    }
                }
                canvas.restore();
            }
            boolean drawProgress = this.isCurrentVideo ? this.progressView.getVisibility() != 0 && (this.videoPlayer == null || !this.videoPlayer.isPlaying()) : (drawTextureView || this.videoPlayerControlFrameLayout.getVisibility() == 0) ? false : true;
            if (drawProgress) {
                canvas.save();
                canvas.translate(translateX, currentTranslationY / currentScale);
                this.photoProgressViews[0].setScale(1.0f - scaleDiff);
                this.photoProgressViews[0].setAlpha(alpha);
                this.photoProgressViews[0].onDraw(canvas);
                canvas.restore();
            }
            if (!this.pipAnimationInProgress && (this.miniProgressView.getVisibility() == 0 || this.miniProgressAnimator != null)) {
                canvas.save();
                canvas.translate(((float) this.miniProgressView.getLeft()) + translateX, ((float) this.miniProgressView.getTop()) + (currentTranslationY / currentScale));
                this.miniProgressView.draw(canvas);
                canvas.restore();
            }
            if (sideImage == this.leftImage) {
                if (sideImage.hasBitmapImage()) {
                    canvas.save();
                    canvas.translate((float) (getContainerViewWidth() / 2), (float) (getContainerViewHeight() / 2));
                    canvas.translate(((-((((float) canvas.getWidth()) * (this.scale + 1.0f)) + ((float) AndroidUtilities.dp(30.0f)))) / 2.0f) + currentTranslationX, 0.0f);
                    bitmapWidth = sideImage.getBitmapWidth();
                    bitmapHeight = sideImage.getBitmapHeight();
                    scaleX = ((float) getContainerViewWidth()) / ((float) bitmapWidth);
                    scaleY = ((float) getContainerViewHeight()) / ((float) bitmapHeight);
                    if (scaleX > scaleY) {
                        scale = scaleY;
                    } else {
                        scale = scaleX;
                    }
                    width = (int) (((float) bitmapWidth) * scale);
                    height = (int) (((float) bitmapHeight) * scale);
                    sideImage.setAlpha(1.0f);
                    sideImage.setImageCoords((-width) / 2, (-height) / 2, width, height);
                    sideImage.draw(canvas);
                    canvas.restore();
                }
                this.groupedPhotosListView.setMoveProgress(1.0f - alpha);
                canvas.save();
                canvas.translate(currentTranslationX, currentTranslationY / currentScale);
                canvas.translate((-((((float) canvas.getWidth()) * (this.scale + 1.0f)) + ((float) AndroidUtilities.dp(30.0f)))) / 2.0f, (-currentTranslationY) / currentScale);
                this.photoProgressViews[2].setScale(1.0f);
                this.photoProgressViews[2].setAlpha(1.0f);
                this.photoProgressViews[2].onDraw(canvas);
                canvas.restore();
            }
            if (this.waitingForDraw != 0) {
                this.waitingForDraw--;
                if (this.waitingForDraw == 0) {
                    if (this.textureImageView != null) {
                        try {
                            this.currentBitmap = Bitmaps.createBitmap(this.videoTextureView.getWidth(), this.videoTextureView.getHeight(), Config.ARGB_8888);
                            this.changedTextureView.getBitmap(this.currentBitmap);
                        } catch (Throwable e) {
                            if (this.currentBitmap != null) {
                                this.currentBitmap.recycle();
                                this.currentBitmap = null;
                            }
                            FileLog.e(e);
                        }
                        if (this.currentBitmap != null) {
                            this.textureImageView.setVisibility(0);
                            this.textureImageView.setImageBitmap(this.currentBitmap);
                        } else {
                            this.textureImageView.setImageDrawable(null);
                        }
                    }
                    this.pipVideoView.close();
                    this.pipVideoView = null;
                    return;
                }
                this.containerView.invalidate();
            }
        }
    }

    private void onActionClick(boolean download) {
        if ((this.currentMessageObject != null || this.currentBotInlineResult != null) && this.currentFileNames[0] != null) {
            Uri uri = null;
            File file = null;
            this.isStreaming = false;
            if (this.currentMessageObject != null) {
                if (!(this.currentMessageObject.messageOwner.attachPath == null || this.currentMessageObject.messageOwner.attachPath.length() == 0)) {
                    file = new File(this.currentMessageObject.messageOwner.attachPath);
                    if (!file.exists()) {
                        file = null;
                    }
                }
                if (file == null) {
                    file = FileLoader.getPathToMessage(this.currentMessageObject.messageOwner);
                    if (!file.exists()) {
                        file = null;
                        if (SharedConfig.streamMedia && ((int) this.currentMessageObject.getDialogId()) != 0 && this.currentMessageObject.isVideo() && this.currentMessageObject.canStreamVideo()) {
                            try {
                                FileLoader.getInstance(this.currentAccount).loadFile(this.currentMessageObject.getDocument(), true, 0);
                                Document document = this.currentMessageObject.getDocument();
                                uri = Uri.parse("tg://" + this.currentMessageObject.getFileName() + ("?account=" + this.currentMessageObject.currentAccount + "&id=" + document.id + "&hash=" + document.access_hash + "&dc=" + document.dc_id + "&size=" + document.size + "&mime=" + URLEncoder.encode(document.mime_type, C.UTF8_NAME) + "&name=" + URLEncoder.encode(FileLoader.getDocumentFileName(document), C.UTF8_NAME)));
                                this.isStreaming = true;
                                checkProgress(0, false);
                            } catch (Exception e) {
                            }
                        }
                    }
                }
            } else if (this.currentBotInlineResult != null) {
                if (this.currentBotInlineResult.document != null) {
                    file = FileLoader.getPathToAttach(this.currentBotInlineResult.document);
                    if (!file.exists()) {
                        file = null;
                    }
                } else if (this.currentBotInlineResult.content instanceof TL_webDocument) {
                    file = new File(FileLoader.getDirectory(4), Utilities.MD5(this.currentBotInlineResult.content.url) + "." + ImageLoader.getHttpUrlExtension(this.currentBotInlineResult.content.url, "mp4"));
                    if (!file.exists()) {
                        file = null;
                    }
                }
            }
            if (file != null && uri == null) {
                uri = Uri.fromFile(file);
            }
            if (uri != null) {
                preparePlayer(uri, true, false);
            } else if (!download) {
            } else {
                if (this.currentMessageObject != null) {
                    if (FileLoader.getInstance(this.currentAccount).isLoadingFile(this.currentFileNames[0])) {
                        FileLoader.getInstance(this.currentAccount).cancelLoadFile(this.currentMessageObject.getDocument());
                    } else {
                        FileLoader.getInstance(this.currentAccount).loadFile(this.currentMessageObject.getDocument(), true, 0);
                    }
                } else if (this.currentBotInlineResult == null) {
                } else {
                    if (this.currentBotInlineResult.document != null) {
                        if (FileLoader.getInstance(this.currentAccount).isLoadingFile(this.currentFileNames[0])) {
                            FileLoader.getInstance(this.currentAccount).cancelLoadFile(this.currentBotInlineResult.document);
                        } else {
                            FileLoader.getInstance(this.currentAccount).loadFile(this.currentBotInlineResult.document, true, 0);
                        }
                    } else if (!(this.currentBotInlineResult.content instanceof TL_webDocument)) {
                    } else {
                        if (ImageLoader.getInstance().isLoadingHttpFile(this.currentBotInlineResult.content.url)) {
                            ImageLoader.getInstance().cancelLoadHttpFile(this.currentBotInlineResult.content.url);
                        } else {
                            ImageLoader.getInstance().loadHttpFile(this.currentBotInlineResult.content.url, "mp4", this.currentAccount);
                        }
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
            this.containerView.postInvalidate();
        }
        return false;
    }

    public boolean onSingleTapConfirmed(MotionEvent e) {
        boolean z = false;
        if (this.discardTap) {
            return false;
        }
        float x;
        float y;
        int state;
        if (this.containerView.getTag() != null) {
            boolean drawTextureView;
            if (this.aspectRatioFrameLayout == null || this.aspectRatioFrameLayout.getVisibility() != 0) {
                drawTextureView = false;
            } else {
                drawTextureView = true;
            }
            x = e.getX();
            y = e.getY();
            if (!(this.photoProgressViews[0] == null || this.containerView == null || drawTextureView)) {
                state = this.photoProgressViews[0].backgroundState;
                if (state > 0 && state <= 3 && x >= ((float) (getContainerViewWidth() - AndroidUtilities.dp(100.0f))) / 2.0f && x <= ((float) (getContainerViewWidth() + AndroidUtilities.dp(100.0f))) / 2.0f && y >= ((float) (getContainerViewHeight() - AndroidUtilities.dp(100.0f))) / 2.0f && y <= ((float) (getContainerViewHeight() + AndroidUtilities.dp(100.0f))) / 2.0f) {
                    onActionClick(true);
                    checkProgress(0, true);
                    return true;
                }
            }
            if (!this.isActionBarVisible) {
                z = true;
            }
            toggleActionBar(z, true);
            return true;
        } else if (this.sendPhotoType == 0 || this.sendPhotoType == 4) {
            if (this.isCurrentVideo) {
                this.videoPlayButton.callOnClick();
                return true;
            }
            this.checkImageView.performClick();
            return true;
        } else if (this.currentBotInlineResult != null && (this.currentBotInlineResult.type.equals(MimeTypes.BASE_TYPE_VIDEO) || MessageObject.isVideoDocument(this.currentBotInlineResult.document))) {
            state = this.photoProgressViews[0].backgroundState;
            if (state <= 0 || state > 3) {
                return true;
            }
            x = e.getX();
            y = e.getY();
            if (x < ((float) (getContainerViewWidth() - AndroidUtilities.dp(100.0f))) / 2.0f || x > ((float) (getContainerViewWidth() + AndroidUtilities.dp(100.0f))) / 2.0f || y < ((float) (getContainerViewHeight() - AndroidUtilities.dp(100.0f))) / 2.0f || y > ((float) (getContainerViewHeight() + AndroidUtilities.dp(100.0f))) / 2.0f) {
                return true;
            }
            onActionClick(true);
            checkProgress(0, true);
            return true;
        } else if (this.sendPhotoType != 2 || !this.isCurrentVideo) {
            return true;
        } else {
            this.videoPlayButton.callOnClick();
            return true;
        }
    }

    public boolean onDoubleTap(MotionEvent e) {
        if (this.videoPlayer != null && this.videoPlayerControlFrameLayout.getVisibility() == 0) {
            long current = this.videoPlayer.getCurrentPosition();
            long total = this.videoPlayer.getDuration();
            if (total >= 0 && current >= 0 && total != C.TIME_UNSET && current != C.TIME_UNSET) {
                int width = getContainerViewWidth();
                float x = e.getX();
                long old = current;
                if (x >= ((float) ((width / 3) * 2))) {
                    current += 10000;
                } else if (x < ((float) (width / 3))) {
                    current -= 10000;
                }
                if (old != current) {
                    if (current > total) {
                        current = total;
                    } else if (current < 0) {
                        current = 0;
                    }
                    this.videoPlayer.seekTo(current);
                    this.containerView.invalidate();
                    this.videoPlayerSeekbar.setProgress(((float) current) / ((float) total));
                    this.videoPlayerControlFrameLayout.invalidate();
                    return true;
                }
            }
        }
        if (!this.canZoom || (this.scale == 1.0f && (this.translationY != 0.0f || this.translationX != 0.0f))) {
            return false;
        }
        if (this.animationStartTime != 0 || this.animationInProgress != 0) {
            return false;
        }
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

    public boolean onDoubleTapEvent(MotionEvent e) {
        return false;
    }

    public void updateMuteButton() {
        if (this.videoPlayer != null) {
            this.videoPlayer.setMute(this.muteVideo);
        }
        if (this.videoHasAudio) {
            this.muteItem.setEnabled(true);
            this.muteItem.setClickable(true);
            this.muteItem.setAlpha(1.0f);
            if (this.muteVideo) {
                this.actionBar.setSubtitle(null);
                this.muteItem.setImageResource(R.drawable.volume_off);
                this.muteItem.setColorFilter(new PorterDuffColorFilter(-12734994, Mode.MULTIPLY));
                if (this.compressItem.getTag() != null) {
                    this.compressItem.setClickable(false);
                    this.compressItem.setAlpha(0.5f);
                    this.compressItem.setEnabled(false);
                }
                this.videoTimelineView.setMaxProgressDiff(30000.0f / this.videoDuration);
                return;
            }
            this.muteItem.setColorFilter(null);
            this.actionBar.setSubtitle(this.currentSubtitle);
            this.muteItem.setImageResource(R.drawable.volume_on);
            if (this.compressItem.getTag() != null) {
                this.compressItem.setClickable(true);
                this.compressItem.setAlpha(1.0f);
                this.compressItem.setEnabled(true);
            }
            this.videoTimelineView.setMaxProgressDiff(1.0f);
            return;
        }
        this.muteItem.setEnabled(false);
        this.muteItem.setClickable(false);
        this.muteItem.setAlpha(0.5f);
    }

    private void didChangedCompressionLevel(boolean request) {
        Editor editor = MessagesController.getGlobalMainSettings().edit();
        editor.putInt("compress_video2", this.selectedCompression);
        editor.commit();
        updateWidthHeightBitrateForCompression();
        updateVideoInfo();
        if (request) {
            requestVideoPreview(1);
        }
    }

    private void updateVideoInfo() {
        if (this.actionBar != null) {
            if (this.compressionsCount == 0) {
                this.actionBar.setSubtitle(null);
                return;
            }
            int width;
            int height;
            CharSequence charSequence;
            if (this.selectedCompression == 0) {
                this.compressItem.setImageResource(R.drawable.video_240);
            } else if (this.selectedCompression == 1) {
                this.compressItem.setImageResource(R.drawable.video_360);
            } else if (this.selectedCompression == 2) {
                this.compressItem.setImageResource(R.drawable.video_480);
            } else if (this.selectedCompression == 3) {
                this.compressItem.setImageResource(R.drawable.video_720);
            } else if (this.selectedCompression == 4) {
                this.compressItem.setImageResource(R.drawable.video_1080);
            }
            this.estimatedDuration = (long) Math.ceil((double) ((this.videoTimelineView.getRightProgress() - this.videoTimelineView.getLeftProgress()) * this.videoDuration));
            if (this.compressItem.getTag() == null || this.selectedCompression == this.compressionsCount - 1) {
                width = (this.rotationValue == 90 || this.rotationValue == 270) ? this.originalHeight : this.originalWidth;
                height = (this.rotationValue == 90 || this.rotationValue == 270) ? this.originalWidth : this.originalHeight;
                this.estimatedSize = (int) (((float) this.originalSize) * (((float) this.estimatedDuration) / this.videoDuration));
            } else {
                width = (this.rotationValue == 90 || this.rotationValue == 270) ? this.resultHeight : this.resultWidth;
                height = (this.rotationValue == 90 || this.rotationValue == 270) ? this.resultWidth : this.resultHeight;
                this.estimatedSize = (int) (((float) (this.audioFramesSize + this.videoFramesSize)) * (((float) this.estimatedDuration) / this.videoDuration));
                this.estimatedSize += (this.estimatedSize / 32768) * 16;
            }
            if (this.videoTimelineView.getLeftProgress() == 0.0f) {
                this.startTime = -1;
            } else {
                this.startTime = ((long) (this.videoTimelineView.getLeftProgress() * this.videoDuration)) * 1000;
            }
            if (this.videoTimelineView.getRightProgress() == 1.0f) {
                this.endTime = -1;
            } else {
                this.endTime = ((long) (this.videoTimelineView.getRightProgress() * this.videoDuration)) * 1000;
            }
            String videoDimension = String.format("%dx%d", new Object[]{Integer.valueOf(width), Integer.valueOf(height)});
            int seconds = ((int) Math.ceil((double) (this.estimatedDuration / 1000))) - (((int) ((this.estimatedDuration / 1000) / 60)) * 60);
            String videoTimeSize = String.format("%d:%02d, ~%s", new Object[]{Integer.valueOf((int) ((this.estimatedDuration / 1000) / 60)), Integer.valueOf(seconds), AndroidUtilities.formatFileSize((long) this.estimatedSize)});
            this.currentSubtitle = String.format("%s, %s", new Object[]{videoDimension, videoTimeSize});
            ActionBar actionBar = this.actionBar;
            if (this.muteVideo) {
                charSequence = null;
            } else {
                charSequence = this.currentSubtitle;
            }
            actionBar.setSubtitle(charSequence);
        }
    }

    private void requestVideoPreview(int request) {
        if (this.videoPreviewMessageObject != null) {
            MediaController.getInstance().cancelVideoConvert(this.videoPreviewMessageObject);
        }
        boolean wasRequestingPreview = this.requestingPreview && !this.tryStartRequestPreviewOnFinish;
        this.requestingPreview = false;
        this.loadInitialVideo = false;
        this.progressView.setVisibility(4);
        if (request != 1) {
            this.tryStartRequestPreviewOnFinish = false;
            if (request == 2) {
                preparePlayer(this.currentPlayingVideoFile, false, false);
            }
        } else if (this.selectedCompression == this.compressionsCount - 1) {
            this.tryStartRequestPreviewOnFinish = false;
            if (wasRequestingPreview) {
                this.progressView.setVisibility(0);
                this.loadInitialVideo = true;
            } else {
                preparePlayer(this.currentPlayingVideoFile, false, false);
            }
        } else {
            this.requestingPreview = true;
            releasePlayer();
            if (this.videoPreviewMessageObject == null) {
                TL_message message = new TL_message();
                message.id = 0;
                message.message = TtmlNode.ANONYMOUS_REGION_ID;
                message.media = new TL_messageMediaEmpty();
                message.action = new TL_messageActionEmpty();
                this.videoPreviewMessageObject = new MessageObject(UserConfig.selectedAccount, message, false);
                this.videoPreviewMessageObject.messageOwner.attachPath = new File(FileLoader.getDirectory(4), "video_preview.mp4").getAbsolutePath();
                this.videoPreviewMessageObject.videoEditedInfo = new VideoEditedInfo();
                this.videoPreviewMessageObject.videoEditedInfo.rotationValue = this.rotationValue;
                this.videoPreviewMessageObject.videoEditedInfo.originalWidth = this.originalWidth;
                this.videoPreviewMessageObject.videoEditedInfo.originalHeight = this.originalHeight;
                this.videoPreviewMessageObject.videoEditedInfo.framerate = this.videoFramerate;
                this.videoPreviewMessageObject.videoEditedInfo.originalPath = this.currentPlayingVideoFile.getPath();
            }
            VideoEditedInfo videoEditedInfo = this.videoPreviewMessageObject.videoEditedInfo;
            long start = this.startTime;
            videoEditedInfo.startTime = start;
            videoEditedInfo = this.videoPreviewMessageObject.videoEditedInfo;
            long end = this.endTime;
            videoEditedInfo.endTime = end;
            if (start == -1) {
                start = 0;
            }
            if (end == -1) {
                end = (long) (this.videoDuration * 1000.0f);
            }
            if (end - start > 5000000) {
                this.videoPreviewMessageObject.videoEditedInfo.endTime = 5000000 + start;
            }
            this.videoPreviewMessageObject.videoEditedInfo.bitrate = this.bitrate;
            this.videoPreviewMessageObject.videoEditedInfo.resultWidth = this.resultWidth;
            this.videoPreviewMessageObject.videoEditedInfo.resultHeight = this.resultHeight;
            if (!MediaController.getInstance().scheduleVideoConvert(this.videoPreviewMessageObject, true)) {
                this.tryStartRequestPreviewOnFinish = true;
            }
            this.requestingPreview = true;
            this.progressView.setVisibility(0);
        }
        this.containerView.invalidate();
    }

    private void updateWidthHeightBitrateForCompression() {
        if (this.compressionsCount > 0) {
            if (this.selectedCompression >= this.compressionsCount) {
                this.selectedCompression = this.compressionsCount - 1;
            }
            if (this.selectedCompression != this.compressionsCount - 1) {
                float maxSize;
                int targetBitrate;
                float scale;
                switch (this.selectedCompression) {
                    case 0:
                        maxSize = 426.0f;
                        targetBitrate = 400000;
                        break;
                    case 1:
                        maxSize = 640.0f;
                        targetBitrate = 900000;
                        break;
                    case 2:
                        maxSize = 854.0f;
                        targetBitrate = 1100000;
                        break;
                    default:
                        targetBitrate = 2500000;
                        maxSize = 1280.0f;
                        break;
                }
                if (this.originalWidth > this.originalHeight) {
                    scale = maxSize / ((float) this.originalWidth);
                } else {
                    scale = maxSize / ((float) this.originalHeight);
                }
                this.resultWidth = Math.round((((float) this.originalWidth) * scale) / 2.0f) * 2;
                this.resultHeight = Math.round((((float) this.originalHeight) * scale) / 2.0f) * 2;
                if (this.bitrate != 0) {
                    this.bitrate = Math.min(targetBitrate, (int) (((float) this.originalBitrate) / scale));
                    this.videoFramesSize = (long) ((((float) (this.bitrate / 8)) * this.videoDuration) / 1000.0f);
                }
            }
        }
    }

    private void showQualityView(final boolean show) {
        if (show) {
            this.previousCompression = this.selectedCompression;
        }
        if (this.qualityChooseViewAnimation != null) {
            this.qualityChooseViewAnimation.cancel();
        }
        this.qualityChooseViewAnimation = new AnimatorSet();
        AnimatorSet animatorSet;
        Animator[] animatorArr;
        if (show) {
            this.qualityChooseView.setTag(Integer.valueOf(1));
            animatorSet = this.qualityChooseViewAnimation;
            animatorArr = new Animator[3];
            animatorArr[0] = ObjectAnimator.ofFloat(this.pickerView, "translationY", new float[]{0.0f, (float) AndroidUtilities.dp(152.0f)});
            animatorArr[1] = ObjectAnimator.ofFloat(this.pickerViewSendButton, "translationY", new float[]{0.0f, (float) AndroidUtilities.dp(152.0f)});
            animatorArr[2] = ObjectAnimator.ofFloat(this.bottomLayout, "translationY", new float[]{(float) (-AndroidUtilities.dp(48.0f)), (float) AndroidUtilities.dp(104.0f)});
            animatorSet.playTogether(animatorArr);
        } else {
            this.qualityChooseView.setTag(null);
            animatorSet = this.qualityChooseViewAnimation;
            animatorArr = new Animator[3];
            animatorArr[0] = ObjectAnimator.ofFloat(this.qualityChooseView, "translationY", new float[]{0.0f, (float) AndroidUtilities.dp(166.0f)});
            animatorArr[1] = ObjectAnimator.ofFloat(this.qualityPicker, "translationY", new float[]{0.0f, (float) AndroidUtilities.dp(166.0f)});
            animatorArr[2] = ObjectAnimator.ofFloat(this.bottomLayout, "translationY", new float[]{(float) (-AndroidUtilities.dp(48.0f)), (float) AndroidUtilities.dp(118.0f)});
            animatorSet.playTogether(animatorArr);
        }
        this.qualityChooseViewAnimation.addListener(new AnimatorListenerAdapter() {
            public void onAnimationEnd(Animator animation) {
                if (animation.equals(PhotoViewer.this.qualityChooseViewAnimation)) {
                    PhotoViewer.this.qualityChooseViewAnimation = new AnimatorSet();
                    AnimatorSet access$17700;
                    Animator[] animatorArr;
                    if (show) {
                        PhotoViewer.this.qualityChooseView.setVisibility(0);
                        PhotoViewer.this.qualityPicker.setVisibility(0);
                        access$17700 = PhotoViewer.this.qualityChooseViewAnimation;
                        animatorArr = new Animator[3];
                        animatorArr[0] = ObjectAnimator.ofFloat(PhotoViewer.this.qualityChooseView, "translationY", new float[]{0.0f});
                        animatorArr[1] = ObjectAnimator.ofFloat(PhotoViewer.this.qualityPicker, "translationY", new float[]{0.0f});
                        animatorArr[2] = ObjectAnimator.ofFloat(PhotoViewer.this.bottomLayout, "translationY", new float[]{(float) (-AndroidUtilities.dp(48.0f))});
                        access$17700.playTogether(animatorArr);
                    } else {
                        PhotoViewer.this.qualityChooseView.setVisibility(4);
                        PhotoViewer.this.qualityPicker.setVisibility(4);
                        access$17700 = PhotoViewer.this.qualityChooseViewAnimation;
                        animatorArr = new Animator[3];
                        animatorArr[0] = ObjectAnimator.ofFloat(PhotoViewer.this.pickerView, "translationY", new float[]{0.0f});
                        animatorArr[1] = ObjectAnimator.ofFloat(PhotoViewer.this.pickerViewSendButton, "translationY", new float[]{0.0f});
                        animatorArr[2] = ObjectAnimator.ofFloat(PhotoViewer.this.bottomLayout, "translationY", new float[]{(float) (-AndroidUtilities.dp(48.0f))});
                        access$17700.playTogether(animatorArr);
                    }
                    PhotoViewer.this.qualityChooseViewAnimation.addListener(new AnimatorListenerAdapter() {
                        public void onAnimationEnd(Animator animation) {
                            if (animation.equals(PhotoViewer.this.qualityChooseViewAnimation)) {
                                PhotoViewer.this.qualityChooseViewAnimation = null;
                            }
                        }
                    });
                    PhotoViewer.this.qualityChooseViewAnimation.setDuration(200);
                    PhotoViewer.this.qualityChooseViewAnimation.setInterpolator(new AccelerateInterpolator());
                    PhotoViewer.this.qualityChooseViewAnimation.start();
                }
            }

            public void onAnimationCancel(Animator animation) {
                PhotoViewer.this.qualityChooseViewAnimation = null;
            }
        });
        this.qualityChooseViewAnimation.setDuration(200);
        this.qualityChooseViewAnimation.setInterpolator(new DecelerateInterpolator());
        this.qualityChooseViewAnimation.start();
    }

    private ByteArrayInputStream cleanBuffer(byte[] data) {
        byte[] output = new byte[data.length];
        int inPos = 0;
        int outPos = 0;
        while (inPos < data.length) {
            if (data[inPos] == (byte) 0 && data[inPos + 1] == (byte) 0 && data[inPos + 2] == (byte) 3) {
                output[outPos] = (byte) 0;
                output[outPos + 1] = (byte) 0;
                inPos += 3;
                outPos += 2;
            } else {
                output[outPos] = data[inPos];
                inPos++;
                outPos++;
            }
        }
        return new ByteArrayInputStream(output, 0, outPos);
    }

    private void processOpenVideo(final String videoPath, boolean muted) {
        if (this.currentLoadingVideoRunnable != null) {
            Utilities.globalQueue.cancelRunnable(this.currentLoadingVideoRunnable);
            this.currentLoadingVideoRunnable = null;
        }
        this.videoPreviewMessageObject = null;
        setCompressItemEnabled(false, true);
        this.muteVideo = muted;
        this.videoTimelineView.setVideoPath(videoPath);
        this.compressionsCount = -1;
        this.rotationValue = 0;
        this.videoFramerate = 25;
        this.originalSize = new File(videoPath).length();
        DispatchQueue dispatchQueue = Utilities.globalQueue;
        Runnable anonymousClass81 = new Runnable() {
            public void run() {
                if (PhotoViewer.this.currentLoadingVideoRunnable == this) {
                    TrackHeaderBox trackHeaderBox = null;
                    boolean isAvc = true;
                    IsoFile isoFile = new IsoFile(videoPath);
                    List<Box> boxes = Path.getPaths((Container) isoFile, "/moov/trak/");
                    if (Path.getPath((Container) isoFile, "/moov/trak/mdia/minf/stbl/stsd/mp4a/") == null && BuildVars.LOGS_ENABLED) {
                        FileLog.d("video hasn't mp4a atom");
                    }
                    if (Path.getPath((Container) isoFile, "/moov/trak/mdia/minf/stbl/stsd/avc1/") == null) {
                        if (BuildVars.LOGS_ENABLED) {
                            FileLog.d("video hasn't avc1 atom");
                        }
                        isAvc = false;
                    }
                    PhotoViewer.this.audioFramesSize = 0;
                    PhotoViewer.this.videoFramesSize = 0;
                    int b = 0;
                    while (b < boxes.size()) {
                        if (PhotoViewer.this.currentLoadingVideoRunnable == this) {
                            int a;
                            TrackBox trackBox = (TrackBox) ((Box) boxes.get(b));
                            long sampleSizes = 0;
                            long trackBitrate = 0;
                            MediaBox mediaBox = null;
                            MediaHeaderBox mediaHeaderBox = null;
                            try {
                                mediaBox = trackBox.getMediaBox();
                                mediaHeaderBox = mediaBox.getMediaHeaderBox();
                                long[] sizes = mediaBox.getMediaInformationBox().getSampleTableBox().getSampleSizeBox().getSampleSizes();
                                a = 0;
                                while (a < sizes.length) {
                                    if (PhotoViewer.this.currentLoadingVideoRunnable == this) {
                                        sampleSizes += sizes[a];
                                        a++;
                                    } else {
                                        return;
                                    }
                                }
                                PhotoViewer.this.videoDuration = ((float) mediaHeaderBox.getDuration()) / ((float) mediaHeaderBox.getTimescale());
                                trackBitrate = (long) ((int) (((float) (8 * sampleSizes)) / PhotoViewer.this.videoDuration));
                            } catch (Throwable e) {
                                FileLog.e(e);
                            }
                            if (PhotoViewer.this.currentLoadingVideoRunnable == this) {
                                TrackHeaderBox headerBox = trackBox.getTrackHeaderBox();
                                if (headerBox.getWidth() == 0.0d || headerBox.getHeight() == 0.0d) {
                                    PhotoViewer.this.audioFramesSize = PhotoViewer.this.audioFramesSize + sampleSizes;
                                } else if (trackHeaderBox == null || trackHeaderBox.getWidth() < headerBox.getWidth() || trackHeaderBox.getHeight() < headerBox.getHeight()) {
                                    trackHeaderBox = headerBox;
                                    PhotoViewer.this.originalBitrate = PhotoViewer.this.bitrate = (int) ((trackBitrate / 100000) * 100000);
                                    if (PhotoViewer.this.bitrate > 900000) {
                                        PhotoViewer.this.bitrate = 900000;
                                    }
                                    PhotoViewer.this.videoFramesSize = PhotoViewer.this.videoFramesSize + sampleSizes;
                                    if (!(mediaBox == null || mediaHeaderBox == null)) {
                                        TimeToSampleBox timeToSampleBox = mediaBox.getMediaInformationBox().getSampleTableBox().getTimeToSampleBox();
                                        if (timeToSampleBox != null) {
                                            List<Entry> entries = timeToSampleBox.getEntries();
                                            long delta = 0;
                                            int size = Math.min(entries.size(), 11);
                                            for (a = 1; a < size; a++) {
                                                delta += ((Entry) entries.get(a)).getDelta();
                                            }
                                            if (delta != 0) {
                                                try {
                                                    PhotoViewer.this.videoFramerate = (int) (((double) mediaHeaderBox.getTimescale()) / ((double) (delta / ((long) (size - 1)))));
                                                } catch (Throwable e2) {
                                                    FileLog.e(e2);
                                                    isAvc = false;
                                                }
                                            } else {
                                                continue;
                                            }
                                        } else {
                                            continue;
                                        }
                                    }
                                }
                                b++;
                            } else {
                                return;
                            }
                        }
                        return;
                    }
                    if (trackHeaderBox == null) {
                        if (BuildVars.LOGS_ENABLED) {
                            FileLog.d("video hasn't trackHeaderBox atom");
                        }
                        isAvc = false;
                    }
                    final boolean isAvcFinal = isAvc;
                    TrackHeaderBox trackHeaderBoxFinal = trackHeaderBox;
                    if (PhotoViewer.this.currentLoadingVideoRunnable == this) {
                        PhotoViewer.this.currentLoadingVideoRunnable = null;
                        final TrackHeaderBox trackHeaderBox2 = trackHeaderBoxFinal;
                        AndroidUtilities.runOnUIThread(new Runnable() {
                            public void run() {
                                if (PhotoViewer.this.parentActivity != null) {
                                    PhotoViewer.this.videoHasAudio = isAvcFinal;
                                    if (isAvcFinal) {
                                        boolean z;
                                        Matrix matrix = trackHeaderBox2.getMatrix();
                                        if (matrix.equals(Matrix.ROTATE_90)) {
                                            PhotoViewer.this.rotationValue = 90;
                                        } else if (matrix.equals(Matrix.ROTATE_180)) {
                                            PhotoViewer.this.rotationValue = 180;
                                        } else if (matrix.equals(Matrix.ROTATE_270)) {
                                            PhotoViewer.this.rotationValue = 270;
                                        } else {
                                            PhotoViewer.this.rotationValue = 0;
                                        }
                                        PhotoViewer.this.resultWidth = PhotoViewer.this.originalWidth = (int) trackHeaderBox2.getWidth();
                                        PhotoViewer.this.resultHeight = PhotoViewer.this.originalHeight = (int) trackHeaderBox2.getHeight();
                                        PhotoViewer.this.videoDuration = PhotoViewer.this.videoDuration * 1000.0f;
                                        PhotoViewer.this.selectedCompression = MessagesController.getGlobalMainSettings().getInt("compress_video2", 1);
                                        if (PhotoViewer.this.originalWidth > 1280 || PhotoViewer.this.originalHeight > 1280) {
                                            PhotoViewer.this.compressionsCount = 5;
                                        } else if (PhotoViewer.this.originalWidth > 854 || PhotoViewer.this.originalHeight > 854) {
                                            PhotoViewer.this.compressionsCount = 4;
                                        } else if (PhotoViewer.this.originalWidth > 640 || PhotoViewer.this.originalHeight > 640) {
                                            PhotoViewer.this.compressionsCount = 3;
                                        } else if (PhotoViewer.this.originalWidth > 480 || PhotoViewer.this.originalHeight > 480) {
                                            PhotoViewer.this.compressionsCount = 2;
                                        } else {
                                            PhotoViewer.this.compressionsCount = 1;
                                        }
                                        PhotoViewer.this.updateWidthHeightBitrateForCompression();
                                        PhotoViewer photoViewer = PhotoViewer.this;
                                        if (PhotoViewer.this.compressionsCount > 1) {
                                            z = true;
                                        } else {
                                            z = false;
                                        }
                                        photoViewer.setCompressItemEnabled(z, true);
                                        if (BuildVars.LOGS_ENABLED) {
                                            FileLog.d("compressionsCount = " + PhotoViewer.this.compressionsCount + " w = " + PhotoViewer.this.originalWidth + " h = " + PhotoViewer.this.originalHeight);
                                        }
                                        if (VERSION.SDK_INT < 18 && PhotoViewer.this.compressItem.getTag() != null) {
                                            try {
                                                MediaCodecInfo codecInfo = MediaController.selectCodec("video/avc");
                                                if (codecInfo == null) {
                                                    if (BuildVars.LOGS_ENABLED) {
                                                        FileLog.d("no codec info for video/avc");
                                                    }
                                                    PhotoViewer.this.setCompressItemEnabled(false, true);
                                                } else {
                                                    String name = codecInfo.getName();
                                                    if (name.equals("OMX.google.h264.encoder") || name.equals("OMX.ST.VFM.H264Enc") || name.equals("OMX.Exynos.avc.enc") || name.equals("OMX.MARVELL.VIDEO.HW.CODA7542ENCODER") || name.equals("OMX.MARVELL.VIDEO.H264ENCODER") || name.equals("OMX.k3.video.encoder.avc") || name.equals("OMX.TI.DUCATI1.VIDEO.H264E")) {
                                                        if (BuildVars.LOGS_ENABLED) {
                                                            FileLog.d("unsupported encoder = " + name);
                                                        }
                                                        PhotoViewer.this.setCompressItemEnabled(false, true);
                                                    } else if (MediaController.selectColorFormat(codecInfo, "video/avc") == 0) {
                                                        if (BuildVars.LOGS_ENABLED) {
                                                            FileLog.d("no color format for video/avc");
                                                        }
                                                        PhotoViewer.this.setCompressItemEnabled(false, true);
                                                    }
                                                }
                                            } catch (Throwable e) {
                                                PhotoViewer.this.setCompressItemEnabled(false, true);
                                                FileLog.e(e);
                                            }
                                        }
                                        PhotoViewer.this.qualityChooseView.invalidate();
                                    } else {
                                        PhotoViewer.this.compressionsCount = 0;
                                    }
                                    PhotoViewer.this.updateVideoInfo();
                                    PhotoViewer.this.updateMuteButton();
                                }
                            }
                        });
                    }
                }
            }
        };
        this.currentLoadingVideoRunnable = anonymousClass81;
        dispatchQueue.postRunnable(anonymousClass81);
    }

    private void setCompressItemEnabled(boolean enabled, boolean animated) {
        float f = 1.0f;
        if (this.compressItem != null) {
            if (enabled && this.compressItem.getTag() != null) {
                return;
            }
            if (enabled || this.compressItem.getTag() != null) {
                this.compressItem.setTag(enabled ? Integer.valueOf(1) : null);
                this.compressItem.setEnabled(enabled);
                this.compressItem.setClickable(enabled);
                if (this.compressItemAnimation != null) {
                    this.compressItemAnimation.cancel();
                    this.compressItemAnimation = null;
                }
                if (animated) {
                    float f2;
                    this.compressItemAnimation = new AnimatorSet();
                    AnimatorSet animatorSet = this.compressItemAnimation;
                    Animator[] animatorArr = new Animator[1];
                    ImageView imageView = this.compressItem;
                    String str = "alpha";
                    float[] fArr = new float[1];
                    if (enabled) {
                        f2 = 1.0f;
                    } else {
                        f2 = 0.5f;
                    }
                    fArr[0] = f2;
                    animatorArr[0] = ObjectAnimator.ofFloat(imageView, str, fArr);
                    animatorSet.playTogether(animatorArr);
                    this.compressItemAnimation.setDuration(180);
                    this.compressItemAnimation.setInterpolator(decelerateInterpolator);
                    this.compressItemAnimation.start();
                    return;
                }
                ImageView imageView2 = this.compressItem;
                if (!enabled) {
                    f = 0.5f;
                }
                imageView2.setAlpha(f);
            }
        }
    }
}
