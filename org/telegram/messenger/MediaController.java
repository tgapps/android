package org.telegram.messenger;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.database.ContentObserver;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.SurfaceTexture;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.AudioManager;
import android.media.AudioManager.OnAudioFocusChangeListener;
import android.media.AudioRecord;
import android.media.AudioTrack;
import android.media.MediaCodec.BufferInfo;
import android.media.MediaCodecInfo;
import android.media.MediaCodecInfo.CodecCapabilities;
import android.media.MediaCodecList;
import android.media.MediaExtractor;
import android.media.MediaFormat;
import android.net.Uri;
import android.os.Build.VERSION;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.provider.MediaStore.Images.Media;
import android.provider.MediaStore.Video;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.SparseArray;
import android.view.TextureView;
import android.view.View;
import android.widget.FrameLayout;
import java.io.File;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.CountDownLatch;
import org.telegram.messenger.NotificationCenter.NotificationCenterDelegate;
import org.telegram.messenger.audioinfo.AudioInfo;
import org.telegram.messenger.beta.R;
import org.telegram.messenger.exoplayer2.C;
import org.telegram.messenger.exoplayer2.DefaultRenderersFactory;
import org.telegram.messenger.exoplayer2.trackselection.AdaptiveTrackSelection;
import org.telegram.messenger.exoplayer2.ui.AspectRatioFrameLayout;
import org.telegram.messenger.video.MP4Builder;
import org.telegram.messenger.voip.VoIPService;
import org.telegram.tgnet.ConnectionsManager;
import org.telegram.tgnet.TLRPC.Document;
import org.telegram.tgnet.TLRPC.DocumentAttribute;
import org.telegram.tgnet.TLRPC.EncryptedChat;
import org.telegram.tgnet.TLRPC.InputDocument;
import org.telegram.tgnet.TLRPC.MessageEntity;
import org.telegram.tgnet.TLRPC.TL_document;
import org.telegram.tgnet.TLRPC.TL_documentAttributeAnimated;
import org.telegram.tgnet.TLRPC.TL_documentAttributeAudio;
import org.telegram.tgnet.TLRPC.TL_encryptedChat;
import org.telegram.tgnet.TLRPC.TL_messages_messages;
import org.telegram.tgnet.TLRPC.TL_photoSizeEmpty;
import org.telegram.tgnet.TLRPC.User;
import org.telegram.tgnet.TLRPC.messages_Messages;
import org.telegram.ui.ActionBar.AlertDialog;
import org.telegram.ui.ActionBar.BaseFragment;
import org.telegram.ui.ChatActivity;
import org.telegram.ui.Components.EmbedBottomSheet;
import org.telegram.ui.Components.PhotoFilterView.CurvesToolValue;
import org.telegram.ui.Components.PipRoundVideoView;
import org.telegram.ui.Components.Point;
import org.telegram.ui.Components.VideoPlayer;
import org.telegram.ui.Components.VideoPlayer.VideoPlayerDelegate;
import org.telegram.ui.PhotoViewer;

public class MediaController implements SensorEventListener, OnAudioFocusChangeListener, NotificationCenterDelegate {
    private static final int AUDIO_FOCUSED = 2;
    private static final int AUDIO_NO_FOCUS_CAN_DUCK = 1;
    private static final int AUDIO_NO_FOCUS_NO_DUCK = 0;
    private static volatile MediaController Instance = null;
    public static final String MIME_TYPE = "video/avc";
    private static final int PROCESSOR_TYPE_INTEL = 2;
    private static final int PROCESSOR_TYPE_MTK = 3;
    private static final int PROCESSOR_TYPE_OTHER = 0;
    private static final int PROCESSOR_TYPE_QCOM = 1;
    private static final int PROCESSOR_TYPE_SEC = 4;
    private static final int PROCESSOR_TYPE_TI = 5;
    private static final float VOLUME_DUCK = 0.2f;
    private static final float VOLUME_NORMAL = 1.0f;
    public static AlbumEntry allMediaAlbumEntry;
    public static AlbumEntry allPhotosAlbumEntry;
    private static Runnable broadcastPhotosRunnable;
    private static final String[] projectionPhotos = new String[]{"_id", "bucket_id", "bucket_display_name", "_data", "datetaken", "orientation"};
    private static final String[] projectionVideo = new String[]{"_id", "bucket_id", "bucket_display_name", "_data", "datetaken", "duration"};
    public static int[] readArgs = new int[3];
    private static Runnable refreshGalleryRunnable;
    private Sensor accelerometerSensor;
    private boolean accelerometerVertical;
    private boolean allowStartRecord;
    private int audioFocus = 0;
    private AudioInfo audioInfo;
    private VideoPlayer audioPlayer = null;
    private AudioRecord audioRecorder;
    private AudioTrack audioTrackPlayer = null;
    private Activity baseActivity;
    private int buffersWrited;
    private boolean callInProgress;
    private boolean cancelCurrentVideoConversion = false;
    private int countLess;
    private AspectRatioFrameLayout currentAspectRatioFrameLayout;
    private float currentAspectRatioFrameLayoutRatio;
    private boolean currentAspectRatioFrameLayoutReady;
    private int currentAspectRatioFrameLayoutRotation;
    private int currentPlaylistNum;
    private TextureView currentTextureView;
    private FrameLayout currentTextureViewContainer;
    private long currentTotalPcmDuration;
    private boolean decodingFinished = false;
    private boolean downloadingCurrentMessage;
    private ExternalObserver externalObserver;
    private View feedbackView;
    private ByteBuffer fileBuffer;
    private DispatchQueue fileDecodingQueue;
    private DispatchQueue fileEncodingQueue;
    private BaseFragment flagSecureFragment;
    private boolean forceLoopCurrentPlaylist;
    private ArrayList<AudioBuffer> freePlayerBuffers = new ArrayList();
    private HashMap<String, MessageObject> generatingWaveform = new HashMap();
    private float[] gravity = new float[3];
    private float[] gravityFast = new float[3];
    private Sensor gravitySensor;
    private int hasAudioFocus;
    private int ignoreFirstProgress = 0;
    private boolean ignoreOnPause;
    private boolean ignoreProximity;
    private boolean inputFieldHasText;
    private InternalObserver internalObserver;
    private boolean isDrawingWasReady;
    private boolean isPaused = false;
    private int lastChatAccount;
    private long lastChatEnterTime;
    private long lastChatLeaveTime;
    private ArrayList<Long> lastChatVisibleMessages;
    private long lastMediaCheckTime;
    private int lastMessageId;
    private long lastPlayPcm;
    private long lastProgress = 0;
    private float lastProximityValue = -100.0f;
    private EncryptedChat lastSecretChat;
    private long lastTimestamp = 0;
    private User lastUser;
    private float[] linearAcceleration = new float[3];
    private Sensor linearSensor;
    private String[] mediaProjections = null;
    private PipRoundVideoView pipRoundVideoView;
    private int pipSwitchingState;
    private boolean playMusicAgain;
    private int playerBufferSize = 3840;
    private final Object playerObjectSync = new Object();
    private DispatchQueue playerQueue;
    private final Object playerSync = new Object();
    private MessageObject playingMessageObject;
    private ArrayList<MessageObject> playlist = new ArrayList();
    private float previousAccValue;
    private Timer progressTimer = null;
    private final Object progressTimerSync = new Object();
    private boolean proximityHasDifferentValues;
    private Sensor proximitySensor;
    private boolean proximityTouched;
    private WakeLock proximityWakeLock;
    private ChatActivity raiseChat;
    private boolean raiseToEarRecord;
    private int raisedToBack;
    private int raisedToTop;
    private int raisedToTopSign;
    private int recordBufferSize = 1280;
    private ArrayList<ByteBuffer> recordBuffers = new ArrayList();
    private long recordDialogId;
    private DispatchQueue recordQueue = new DispatchQueue("recordQueue");
    private MessageObject recordReplyingMessageObject;
    private Runnable recordRunnable = new Runnable() {
        public void run() {
            Throwable e;
            boolean flush;
            final ByteBuffer finalBuffer;
            if (MediaController.this.audioRecorder != null) {
                ByteBuffer buffer;
                if (MediaController.this.recordBuffers.isEmpty()) {
                    buffer = ByteBuffer.allocateDirect(MediaController.this.recordBufferSize);
                    buffer.order(ByteOrder.nativeOrder());
                } else {
                    buffer = (ByteBuffer) MediaController.this.recordBuffers.get(0);
                    MediaController.this.recordBuffers.remove(0);
                }
                buffer.rewind();
                int len = MediaController.this.audioRecorder.read(buffer, buffer.capacity());
                if (len > 0) {
                    double sum;
                    double sum2;
                    buffer.limit(len);
                    try {
                        float sampleStep;
                        int newPart;
                        long newSamplesCount = MediaController.this.samplesCount + ((long) (len / 2));
                        int currentPart = (int) ((((double) MediaController.this.samplesCount) / ((double) newSamplesCount)) * ((double) MediaController.this.recordSamples.length));
                        int newPart2 = MediaController.this.recordSamples.length - currentPart;
                        if (currentPart != 0) {
                            sampleStep = ((float) MediaController.this.recordSamples.length) / ((float) currentPart);
                            float currentNum = 0.0f;
                            for (int a = 0; a < currentPart; a++) {
                                MediaController.this.recordSamples[a] = MediaController.this.recordSamples[(int) currentNum];
                                currentNum += sampleStep;
                            }
                        }
                        sampleStep = 0.0f;
                        float sampleStep2 = (((float) len) / 2.0f) / ((float) newPart2);
                        sum = 0.0d;
                        sum2 = currentPart;
                        int i = 0;
                        while (i < len / 2) {
                            try {
                                int currentPart2;
                                short peak = buffer.getShort();
                                if (peak > (short) 2500) {
                                    currentPart2 = currentPart;
                                    newPart = newPart2;
                                    sum += (double) (peak * peak);
                                } else {
                                    currentPart2 = currentPart;
                                    newPart = newPart2;
                                }
                                if (i == ((int) sampleStep) && sum2 < MediaController.this.recordSamples.length) {
                                    MediaController.this.recordSamples[sum2] = peak;
                                    sampleStep += sampleStep2;
                                    sum2++;
                                }
                                i++;
                                currentPart = currentPart2;
                                newPart2 = newPart;
                            } catch (Throwable e2) {
                                e = e2;
                            }
                        }
                        newPart = newPart2;
                        MediaController.this.samplesCount = newSamplesCount;
                    } catch (Throwable e22) {
                        e = e22;
                        sum = 0.0d;
                        FileLog.e(e);
                        flush = false;
                        buffer.position(0);
                        sum2 = Math.sqrt((sum / ((double) len)) / 2.0d);
                        finalBuffer = buffer;
                        if (len != buffer.capacity()) {
                            flush = true;
                        }
                        if (len != 0) {
                            MediaController.this.fileEncodingQueue.postRunnable(new Runnable() {
                                public void run() {
                                    while (finalBuffer.hasRemaining()) {
                                        int oldLimit = -1;
                                        if (finalBuffer.remaining() > MediaController.this.fileBuffer.remaining()) {
                                            oldLimit = finalBuffer.limit();
                                            finalBuffer.limit(MediaController.this.fileBuffer.remaining() + finalBuffer.position());
                                        }
                                        MediaController.this.fileBuffer.put(finalBuffer);
                                        if (MediaController.this.fileBuffer.position() == MediaController.this.fileBuffer.limit() || flush) {
                                            if (MediaController.this.writeFrame(MediaController.this.fileBuffer, !flush ? MediaController.this.fileBuffer.limit() : finalBuffer.position()) != 0) {
                                                MediaController.this.fileBuffer.rewind();
                                                MediaController.this.recordTimeCount = MediaController.this.recordTimeCount + ((long) ((MediaController.this.fileBuffer.limit() / 2) / 16));
                                            }
                                        }
                                        if (oldLimit != -1) {
                                            finalBuffer.limit(oldLimit);
                                        }
                                    }
                                    MediaController.this.recordQueue.postRunnable(new Runnable() {
                                        public void run() {
                                            MediaController.this.recordBuffers.add(finalBuffer);
                                        }
                                    });
                                }
                            });
                        }
                        MediaController.this.recordQueue.postRunnable(MediaController.this.recordRunnable);
                        AndroidUtilities.runOnUIThread(new Runnable() {
                            public void run() {
                                NotificationCenter.getInstance(MediaController.this.recordingCurrentAccount).postNotificationName(NotificationCenter.recordProgressChanged, Long.valueOf(System.currentTimeMillis() - MediaController.this.recordStartTime), Double.valueOf(sum2));
                            }
                        });
                        return;
                    }
                    flush = false;
                    buffer.position(0);
                    sum2 = Math.sqrt((sum / ((double) len)) / 2.0d);
                    finalBuffer = buffer;
                    if (len != buffer.capacity()) {
                        flush = true;
                    }
                    if (len != 0) {
                        MediaController.this.fileEncodingQueue.postRunnable(/* anonymous class already generated */);
                    }
                    MediaController.this.recordQueue.postRunnable(MediaController.this.recordRunnable);
                    AndroidUtilities.runOnUIThread(/* anonymous class already generated */);
                    return;
                }
                MediaController.this.recordBuffers.add(buffer);
                MediaController.this.stopRecordingInternal(MediaController.this.sendAfterDone);
            }
        }
    };
    private short[] recordSamples = new short[1024];
    private Runnable recordStartRunnable;
    private long recordStartTime;
    private long recordTimeCount;
    private TL_document recordingAudio;
    private File recordingAudioFile;
    private int recordingCurrentAccount;
    private boolean resumeAudioOnFocusGain;
    private long samplesCount;
    private float seekToProgressPending;
    private int sendAfterDone;
    private SensorManager sensorManager;
    private boolean sensorsStarted;
    private ArrayList<MessageObject> shuffledPlaylist = new ArrayList();
    private SmsObserver smsObserver;
    private int startObserverToken;
    private StopMediaObserverRunnable stopMediaObserverRunnable;
    private final Object sync = new Object();
    private long timeSinceRaise;
    private boolean useFrontSpeaker;
    private ArrayList<AudioBuffer> usedPlayerBuffers = new ArrayList();
    private boolean videoConvertFirstWrite = true;
    private ArrayList<MessageObject> videoConvertQueue = new ArrayList();
    private final Object videoConvertSync = new Object();
    private VideoPlayer videoPlayer;
    private final Object videoQueueSync = new Object();
    private ArrayList<MessageObject> voiceMessagesPlaylist;
    private SparseArray<MessageObject> voiceMessagesPlaylistMap;
    private boolean voiceMessagesPlaylistUnread;

    class AnonymousClass18 implements Runnable {
        final /* synthetic */ File val$cacheFile;
        final /* synthetic */ CountDownLatch val$countDownLatch;
        final /* synthetic */ Boolean[] val$result;

        AnonymousClass18(Boolean[] boolArr, File file, CountDownLatch countDownLatch) {
            this.val$result = boolArr;
            this.val$cacheFile = file;
            this.val$countDownLatch = countDownLatch;
        }

        public void run() {
            this.val$result[0] = Boolean.valueOf(MediaController.this.openOpusFile(this.val$cacheFile.getAbsolutePath()) != 0);
            this.val$countDownLatch.countDown();
        }
    }

    class AnonymousClass21 implements Runnable {
        final /* synthetic */ MessageObject val$messageObject;

        AnonymousClass21(MessageObject messageObject) {
            this.val$messageObject = messageObject;
        }

        public void run() {
            NotificationCenter.getInstance(this.val$messageObject.currentAccount).postNotificationName(NotificationCenter.FileDidLoaded, FileLoader.getAttachFileName(this.val$messageObject.getDocument()));
        }
    }

    public static class AlbumEntry {
        public int bucketId;
        public String bucketName;
        public PhotoEntry coverPhoto;
        public ArrayList<PhotoEntry> photos = new ArrayList();
        public SparseArray<PhotoEntry> photosByIds = new SparseArray();

        public AlbumEntry(int bucketId, String bucketName, PhotoEntry coverPhoto) {
            this.bucketId = bucketId;
            this.bucketName = bucketName;
            this.coverPhoto = coverPhoto;
        }

        public void addPhoto(PhotoEntry photoEntry) {
            this.photos.add(photoEntry);
            this.photosByIds.put(photoEntry.imageId, photoEntry);
        }
    }

    private class AudioBuffer {
        ByteBuffer buffer;
        byte[] bufferBytes;
        int finished;
        long pcmOffset;
        int size;

        public AudioBuffer(int capacity) {
            this.buffer = ByteBuffer.allocateDirect(capacity);
            this.bufferBytes = new byte[capacity];
        }
    }

    public static class AudioEntry {
        public String author;
        public int duration;
        public String genre;
        public long id;
        public MessageObject messageObject;
        public String path;
        public String title;
    }

    private class ExternalObserver extends ContentObserver {
        public ExternalObserver() {
            super(null);
        }

        public void onChange(boolean selfChange) {
            super.onChange(selfChange);
            MediaController.this.processMediaObserver(Media.EXTERNAL_CONTENT_URI);
        }
    }

    private class GalleryObserverExternal extends ContentObserver {
        public GalleryObserverExternal() {
            super(null);
        }

        public void onChange(boolean selfChange) {
            super.onChange(selfChange);
            if (MediaController.refreshGalleryRunnable != null) {
                AndroidUtilities.cancelRunOnUIThread(MediaController.refreshGalleryRunnable);
            }
            AndroidUtilities.runOnUIThread(MediaController.refreshGalleryRunnable = new Runnable() {
                public void run() {
                    MediaController.refreshGalleryRunnable = null;
                    MediaController.loadGalleryPhotosAlbums(0);
                }
            }, AdaptiveTrackSelection.DEFAULT_MIN_TIME_BETWEEN_BUFFER_REEVALUTATION_MS);
        }
    }

    private class GalleryObserverInternal extends ContentObserver {
        public GalleryObserverInternal() {
            super(null);
        }

        private void scheduleReloadRunnable() {
            AndroidUtilities.runOnUIThread(MediaController.refreshGalleryRunnable = new Runnable() {
                public void run() {
                    if (PhotoViewer.getInstance().isVisible()) {
                        GalleryObserverInternal.this.scheduleReloadRunnable();
                        return;
                    }
                    MediaController.refreshGalleryRunnable = null;
                    MediaController.loadGalleryPhotosAlbums(0);
                }
            }, AdaptiveTrackSelection.DEFAULT_MIN_TIME_BETWEEN_BUFFER_REEVALUTATION_MS);
        }

        public void onChange(boolean selfChange) {
            super.onChange(selfChange);
            if (MediaController.refreshGalleryRunnable != null) {
                AndroidUtilities.cancelRunOnUIThread(MediaController.refreshGalleryRunnable);
            }
            scheduleReloadRunnable();
        }
    }

    private class InternalObserver extends ContentObserver {
        public InternalObserver() {
            super(null);
        }

        public void onChange(boolean selfChange) {
            super.onChange(selfChange);
            MediaController.this.processMediaObserver(Media.INTERNAL_CONTENT_URI);
        }
    }

    public static class PhotoEntry {
        public int bucketId;
        public CharSequence caption;
        public long dateTaken;
        public int duration;
        public VideoEditedInfo editedInfo;
        public ArrayList<MessageEntity> entities;
        public int imageId;
        public String imagePath;
        public boolean isCropped;
        public boolean isFiltered;
        public boolean isMuted;
        public boolean isPainted;
        public boolean isVideo;
        public int orientation;
        public String path;
        public SavedFilterState savedFilterState;
        public ArrayList<InputDocument> stickers = new ArrayList();
        public String thumbPath;
        public int ttl;

        public PhotoEntry(int bucketId, int imageId, long dateTaken, String path, int orientation, boolean isVideo) {
            this.bucketId = bucketId;
            this.imageId = imageId;
            this.dateTaken = dateTaken;
            this.path = path;
            if (isVideo) {
                this.duration = orientation;
            } else {
                this.orientation = orientation;
            }
            this.isVideo = isVideo;
        }

        public void reset() {
            this.isFiltered = false;
            this.isPainted = false;
            this.isCropped = false;
            this.ttl = 0;
            this.imagePath = null;
            if (!this.isVideo) {
                this.thumbPath = null;
            }
            this.editedInfo = null;
            this.caption = null;
            this.entities = null;
            this.savedFilterState = null;
            this.stickers.clear();
        }
    }

    public static class SavedFilterState {
        public float blurAngle;
        public float blurExcludeBlurSize;
        public Point blurExcludePoint;
        public float blurExcludeSize;
        public int blurType;
        public float contrastValue;
        public CurvesToolValue curvesToolValue = new CurvesToolValue();
        public float enhanceValue;
        public float exposureValue;
        public float fadeValue;
        public float grainValue;
        public float highlightsValue;
        public float saturationValue;
        public float shadowsValue;
        public float sharpenValue;
        public int tintHighlightsColor;
        public int tintShadowsColor;
        public float vignetteValue;
        public float warmthValue;
    }

    public static class SearchImage {
        public CharSequence caption;
        public int date;
        public Document document;
        public ArrayList<MessageEntity> entities;
        public int height;
        public String id;
        public String imagePath;
        public String imageUrl;
        public boolean isCropped;
        public boolean isFiltered;
        public boolean isPainted;
        public String localUrl;
        public SavedFilterState savedFilterState;
        public int size;
        public ArrayList<InputDocument> stickers = new ArrayList();
        public String thumbPath;
        public String thumbUrl;
        public int ttl;
        public int type;
        public int width;

        public void reset() {
            this.isFiltered = false;
            this.isPainted = false;
            this.isCropped = false;
            this.ttl = 0;
            this.imagePath = null;
            this.thumbPath = null;
            this.caption = null;
            this.entities = null;
            this.savedFilterState = null;
            this.stickers.clear();
        }
    }

    private class SmsObserver extends ContentObserver {
        public SmsObserver() {
            super(null);
        }

        public void onChange(boolean selfChange) {
            MediaController.this.readSms();
        }
    }

    private final class StopMediaObserverRunnable implements Runnable {
        public int currentObserverToken;

        private StopMediaObserverRunnable() {
            this.currentObserverToken = null;
        }

        public void run() {
            if (this.currentObserverToken == MediaController.this.startObserverToken) {
                try {
                    if (MediaController.this.internalObserver != null) {
                        ApplicationLoader.applicationContext.getContentResolver().unregisterContentObserver(MediaController.this.internalObserver);
                        MediaController.this.internalObserver = null;
                    }
                } catch (Throwable e) {
                    FileLog.e(e);
                }
                try {
                    if (MediaController.this.externalObserver != null) {
                        ApplicationLoader.applicationContext.getContentResolver().unregisterContentObserver(MediaController.this.externalObserver);
                        MediaController.this.externalObserver = null;
                    }
                } catch (Throwable e2) {
                    FileLog.e(e2);
                }
            }
        }
    }

    private static class VideoConvertRunnable implements Runnable {
        private MessageObject messageObject;

        private VideoConvertRunnable(MessageObject message) {
            this.messageObject = message;
        }

        public void run() {
            MediaController.getInstance().convertVideo(this.messageObject);
        }

        public static void runConversion(final MessageObject obj) {
            new Thread(new Runnable() {
                public void run() {
                    try {
                        Thread th = new Thread(new VideoConvertRunnable(obj), "VideoConvertRunnable");
                        th.start();
                        th.join();
                    } catch (Throwable e) {
                        FileLog.e(e);
                    }
                }
            }).start();
        }
    }

    class AnonymousClass20 implements VideoPlayerDelegate {
        final /* synthetic */ MessageObject val$messageObject;

        AnonymousClass20(MessageObject messageObject) {
            this.val$messageObject = messageObject;
        }

        public void onStateChanged(boolean playWhenReady, int playbackState) {
            if (playbackState == 4) {
                if (MediaController.this.playlist.isEmpty() || MediaController.this.playlist.size() <= 1) {
                    MediaController mediaController = MediaController.this;
                    boolean z = this.val$messageObject != null && this.val$messageObject.isVoice();
                    mediaController.cleanupPlayer(true, true, z);
                    return;
                }
                MediaController.this.playNextMessageWithoutOrder(true);
            } else if (MediaController.this.seekToProgressPending == 0.0f) {
            } else {
                if (playbackState == 3 || playbackState == 1) {
                    int seekTo = (int) (((float) MediaController.this.audioPlayer.getDuration()) * MediaController.this.seekToProgressPending);
                    MediaController.this.audioPlayer.seekTo((long) seekTo);
                    MediaController.this.lastProgress = (long) seekTo;
                    MediaController.this.seekToProgressPending = 0.0f;
                }
            }
        }

        public void onError(Exception e) {
        }

        public void onVideoSizeChanged(int width, int height, int unappliedRotationDegrees, float pixelWidthHeightRatio) {
        }

        public void onRenderedFirstFrame() {
        }

        public void onSurfaceTextureUpdated(SurfaceTexture surfaceTexture) {
        }

        public boolean onSurfaceDestroyed(SurfaceTexture surfaceTexture) {
            return false;
        }
    }

    private void checkAudioFocus(org.telegram.messenger.MessageObject r1) {
        /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.DecodeException: Load method exception in method: org.telegram.messenger.MediaController.checkAudioFocus(org.telegram.messenger.MessageObject):void
	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:116)
	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:249)
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
        r0 = r7.isVoice();
        r1 = 1;
        if (r0 != 0) goto L_0x0010;
    L_0x0007:
        r0 = r7.isRoundVideo();
        if (r0 == 0) goto L_0x000e;
    L_0x000d:
        goto L_0x0010;
    L_0x000e:
        r0 = r1;
        goto L_0x0018;
    L_0x0010:
        r0 = r6.useFrontSpeaker;
        if (r0 == 0) goto L_0x0016;
        r0 = 3;
        goto L_0x0018;
        r0 = 2;
        goto L_0x0015;
        r2 = r6.hasAudioFocus;
        if (r2 == r0) goto L_0x003a;
        r6.hasAudioFocus = r0;
        r2 = 2;
        r3 = 3;
        if (r0 != r3) goto L_0x002b;
        r3 = org.telegram.messenger.NotificationsController.audioManager;
        r4 = 0;
        r3 = r3.requestAudioFocus(r6, r4, r1);
        goto L_0x0036;
        r4 = org.telegram.messenger.NotificationsController.audioManager;
        if (r0 != r2) goto L_0x0031;
        r5 = r3;
        goto L_0x0032;
        r5 = r1;
        r3 = r4.requestAudioFocus(r6, r3, r5);
        if (r3 != r1) goto L_0x003a;
        r6.audioFocus = r2;
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.telegram.messenger.MediaController.checkAudioFocus(org.telegram.messenger.MessageObject):void");
    }

    private native void closeOpusFile();

    private boolean convertVideo(org.telegram.messenger.MessageObject r1) {
        /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.DecodeException: Load method exception in method: org.telegram.messenger.MediaController.convertVideo(org.telegram.messenger.MessageObject):boolean
	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:116)
	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:249)
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
        r12 = r115;
        r13 = r116;
        r1 = r13.videoEditedInfo;
        r14 = r1.originalPath;
        r1 = r13.videoEditedInfo;
        r10 = r1.startTime;
        r1 = r13.videoEditedInfo;
        r8 = r1.endTime;
        r1 = r13.videoEditedInfo;
        r1 = r1.resultWidth;
        r2 = r13.videoEditedInfo;
        r2 = r2.resultHeight;
        r3 = r13.videoEditedInfo;
        r3 = r3.rotationValue;
        r4 = r13.videoEditedInfo;
        r15 = r4.originalWidth;
        r4 = r13.videoEditedInfo;
        r6 = r4.originalHeight;
        r4 = r13.videoEditedInfo;
        r7 = r4.bitrate;
        r4 = 0;
        r16 = r4;
        r4 = r116.getDialogId();
        r4 = (int) r4;
        if (r4 != 0) goto L_0x0034;
    L_0x0032:
        r4 = 1;
        goto L_0x0035;
    L_0x0034:
        r4 = 0;
    L_0x0035:
        r5 = new java.io.File;
        r19 = r8;
        r8 = r13.messageOwner;
        r8 = r8.attachPath;
        r5.<init>(r8);
        r8 = r5;
        r5 = android.os.Build.VERSION.SDK_INT;
        r9 = 18;
        if (r5 >= r9) goto L_0x0055;
    L_0x0047:
        if (r2 <= r1) goto L_0x0055;
    L_0x0049:
        if (r1 == r15) goto L_0x0055;
    L_0x004b:
        if (r2 == r6) goto L_0x0055;
    L_0x004d:
        r5 = r2;
        r2 = r1;
        r1 = r5;
        r3 = 90;
        r5 = 270; // 0x10e float:3.78E-43 double:1.334E-321;
        goto L_0x0066;
    L_0x0055:
        r5 = android.os.Build.VERSION.SDK_INT;
        r9 = 20;
        if (r5 <= r9) goto L_0x0080;
    L_0x005b:
        r5 = 90;
        if (r3 != r5) goto L_0x006d;
    L_0x005f:
        r5 = r2;
        r2 = r1;
        r1 = r5;
        r3 = 0;
        r5 = 270; // 0x10e float:3.78E-43 double:1.334E-321;
    L_0x0066:
        r9 = r1;
        r114 = r5;
        r5 = r2;
        r2 = r114;
        goto L_0x0084;
    L_0x006d:
        r5 = 180; // 0xb4 float:2.52E-43 double:8.9E-322;
        if (r3 != r5) goto L_0x0075;
    L_0x0071:
        r5 = 180; // 0xb4 float:2.52E-43 double:8.9E-322;
        r3 = 0;
        goto L_0x0066;
    L_0x0075:
        r5 = 270; // 0x10e float:3.78E-43 double:1.334E-321;
        if (r3 != r5) goto L_0x0080;
    L_0x0079:
        r5 = r2;
        r2 = r1;
        r1 = r5;
        r3 = 0;
        r5 = 90;
        goto L_0x0066;
    L_0x0080:
        r9 = r1;
        r5 = r2;
        r2 = r16;
    L_0x0084:
        r1 = org.telegram.messenger.ApplicationLoader.applicationContext;
        r29 = r10;
        r10 = "videoconvert";
        r11 = 0;
        r10 = r1.getSharedPreferences(r10, r11);
        r1 = new java.io.File;
        r1.<init>(r14);
        r11 = r1;
        r1 = r116.getId();
        if (r1 == 0) goto L_0x00d4;
    L_0x009b:
        r1 = "isPreviousOk";
        r31 = r7;
        r7 = 1;
        r1 = r10.getBoolean(r1, r7);
        r7 = r10.edit();
        r32 = r2;
        r2 = "isPreviousOk";
        r33 = r6;
        r6 = 0;
        r2 = r7.putBoolean(r2, r6);
        r2.commit();
        r2 = r11.canRead();
        if (r2 == 0) goto L_0x00c1;
    L_0x00bc:
        if (r1 != 0) goto L_0x00bf;
    L_0x00be:
        goto L_0x00c1;
    L_0x00bf:
        r6 = 1;
        goto L_0x00db;
    L_0x00c1:
        r6 = 1;
        r12.didWriteData(r13, r8, r6, r6);
        r2 = r10.edit();
        r7 = "isPreviousOk";
        r2 = r2.putBoolean(r7, r6);
        r2.commit();
        r2 = 0;
        return r2;
    L_0x00d4:
        r32 = r2;
        r33 = r6;
        r31 = r7;
        r6 = 1;
    L_0x00db:
        r12.videoConvertFirstWrite = r6;
        r16 = 0;
        r34 = java.lang.System.currentTimeMillis();
        if (r9 == 0) goto L_0x12c9;
    L_0x00e5:
        if (r5 == 0) goto L_0x12c9;
    L_0x00e7:
        r1 = 0;
        r2 = 0;
        r7 = r2;
        r6 = new android.media.MediaCodec$BufferInfo;	 Catch:{ Exception -> 0x121f, all -> 0x11f7 }
        r6.<init>();	 Catch:{ Exception -> 0x121f, all -> 0x11f7 }
        r2 = new org.telegram.messenger.video.Mp4Movie;	 Catch:{ Exception -> 0x121f, all -> 0x11f7 }
        r2.<init>();	 Catch:{ Exception -> 0x121f, all -> 0x11f7 }
        r2.setCacheFile(r8);	 Catch:{ Exception -> 0x121f, all -> 0x11f7 }
        r2.setRotation(r3);	 Catch:{ Exception -> 0x121f, all -> 0x11f7 }
        r2.setSize(r9, r5);	 Catch:{ Exception -> 0x121f, all -> 0x11f7 }
        r36 = r1;
        r1 = new org.telegram.messenger.video.MP4Builder;	 Catch:{ Exception -> 0x11d4, all -> 0x11ae }
        r1.<init>();	 Catch:{ Exception -> 0x11d4, all -> 0x11ae }
        r1 = r1.createMovie(r2, r4);	 Catch:{ Exception -> 0x11d4, all -> 0x11ae }
        r37 = r1;
        r1 = new android.media.MediaExtractor;	 Catch:{ Exception -> 0x118b, all -> 0x1164 }
        r1.<init>();	 Catch:{ Exception -> 0x118b, all -> 0x1164 }
        r7 = r1;
        r7.setDataSource(r14);	 Catch:{ Exception -> 0x1142, all -> 0x111d }
        r115.checkConversionCanceled();	 Catch:{ Exception -> 0x1142, all -> 0x111d }
        if (r9 != r15) goto L_0x0211;
    L_0x0118:
        r1 = r33;
        if (r5 != r1) goto L_0x01f0;
    L_0x011c:
        if (r32 != 0) goto L_0x01f0;
    L_0x011e:
        r38 = r1;
        r1 = r13.videoEditedInfo;	 Catch:{ Exception -> 0x01c7, all -> 0x01a0 }
        r1 = r1.roundVideo;	 Catch:{ Exception -> 0x01c7, all -> 0x01a0 }
        if (r1 == 0) goto L_0x0148;
    L_0x0126:
        r33 = r4;
        r42 = r5;
        r44 = r6;
        r13 = r7;
        r45 = r8;
        r49 = r10;
        r40 = r14;
        r43 = r15;
        r47 = r29;
        r15 = r31;
        r46 = r32;
        r41 = r37;
        r17 = r38;
        r31 = r2;
        r32 = r3;
        r14 = r9;
        r29 = r11;
        goto L_0x0231;
    L_0x0148:
        r39 = r2;
        r1 = r31;
        r2 = -1;
        if (r1 == r2) goto L_0x0152;
    L_0x014f:
        r18 = 1;
        goto L_0x0154;
    L_0x0152:
        r18 = 0;
    L_0x0154:
        r21 = r1;
        r2 = r37;
        r17 = r38;
        r1 = r12;
        r41 = r2;
        r40 = r14;
        r14 = r32;
        r31 = r39;
        r2 = r13;
        r32 = r3;
        r3 = r7;
        r33 = r4;
        r4 = r41;
        r42 = r5;
        r43 = r15;
        r15 = 1;
        r5 = r6;
        r44 = r6;
        r13 = r7;
        r15 = r21;
        r6 = r29;
        r45 = r8;
        r46 = r14;
        r14 = r9;
        r8 = r19;
        r49 = r10;
        r47 = r29;
        r10 = r45;
        r29 = r11;
        r11 = r18;
        r1.readAndWriteTracks(r2, r3, r4, r5, r6, r8, r10, r11);	 Catch:{ Exception -> 0x0257, all -> 0x0240 }
        r5 = r12;
        r1 = r13;
        r110 = r14;
        r82 = r15;
        r107 = r41;
        r14 = r42;
        r104 = r45;
        r81 = r46;
        r9 = r47;
        r15 = r116;
        goto L_0x10b8;
    L_0x01a0:
        r0 = move-exception;
        r33 = r4;
        r13 = r7;
        r40 = r14;
        r43 = r15;
        r47 = r29;
        r46 = r32;
        r41 = r37;
        r17 = r38;
        r32 = r3;
        r29 = r11;
        r1 = r0;
        r14 = r5;
        r4 = r8;
        r110 = r9;
        r3 = r10;
        r5 = r12;
        r82 = r31;
        r11 = r41;
        r81 = r46;
        r9 = r47;
        r15 = r116;
        goto L_0x1295;
    L_0x01c7:
        r0 = move-exception;
        r33 = r4;
        r13 = r7;
        r49 = r10;
        r40 = r14;
        r43 = r15;
        r47 = r29;
        r46 = r32;
        r41 = r37;
        r17 = r38;
        r32 = r3;
        r29 = r11;
        r1 = r0;
        r14 = r5;
        r104 = r8;
        r110 = r9;
        r5 = r12;
        r82 = r31;
        r2 = r41;
        r81 = r46;
        r9 = r47;
        r15 = r116;
        goto L_0x1242;
    L_0x01f0:
        r17 = r1;
        r33 = r4;
        r42 = r5;
        r44 = r6;
        r13 = r7;
        r45 = r8;
        r49 = r10;
        r40 = r14;
        r43 = r15;
        r47 = r29;
        r15 = r31;
        r46 = r32;
        r41 = r37;
        r31 = r2;
        r32 = r3;
        r14 = r9;
        r29 = r11;
        goto L_0x0231;
    L_0x0211:
        r42 = r5;
        r44 = r6;
        r13 = r7;
        r45 = r8;
        r49 = r10;
        r40 = r14;
        r43 = r15;
        r47 = r29;
        r15 = r31;
        r46 = r32;
        r17 = r33;
        r41 = r37;
        r31 = r2;
        r32 = r3;
        r33 = r4;
        r14 = r9;
        r29 = r11;
    L_0x0231:
        r1 = 0;
        r2 = r12.findTrack(r13, r1);	 Catch:{ Exception -> 0x1106, all -> 0x10ed }
        r1 = r2;
        r2 = -1;
        if (r15 == r2) goto L_0x026d;
    L_0x023a:
        r2 = 1;
        r3 = r12.findTrack(r13, r2);	 Catch:{ Exception -> 0x0257, all -> 0x0240 }
        goto L_0x026e;
    L_0x0240:
        r0 = move-exception;
        r1 = r0;
        r5 = r12;
        r110 = r14;
        r82 = r15;
        r11 = r41;
        r14 = r42;
        r4 = r45;
        r81 = r46;
        r9 = r47;
        r3 = r49;
        r15 = r116;
        goto L_0x1295;
    L_0x0257:
        r0 = move-exception;
        r1 = r0;
        r5 = r12;
        r7 = r13;
        r110 = r14;
        r82 = r15;
        r2 = r41;
        r14 = r42;
        r104 = r45;
        r81 = r46;
        r9 = r47;
        r15 = r116;
        goto L_0x1242;
    L_0x026d:
        r3 = -1;
    L_0x026e:
        r2 = r3;
        if (r1 < 0) goto L_0x10a4;
    L_0x0271:
        r3 = 0;
        r4 = 0;
        r5 = 0;
        r6 = 0;
        r7 = r6;
        r8 = -1;
        r6 = 0;
        r10 = 0;
        r11 = 0;
        r22 = 0;
        r23 = -5;
        r24 = -5;
        r25 = 0;
        r50 = r3;
        r3 = android.os.Build.MANUFACTURER;	 Catch:{ Exception -> 0x0fed, all -> 0x0fd2 }
        r3 = r3.toLowerCase();	 Catch:{ Exception -> 0x0fed, all -> 0x0fd2 }
        r51 = r4;
        r4 = android.os.Build.VERSION.SDK_INT;	 Catch:{ Exception -> 0x0fab, all -> 0x0fd2 }
        r52 = r5;
        r5 = 18;
        if (r4 >= r5) goto L_0x0389;
    L_0x0295:
        r4 = "video/avc";	 Catch:{ Exception -> 0x0363, all -> 0x0240 }
        r4 = selectCodec(r4);	 Catch:{ Exception -> 0x0363, all -> 0x0240 }
        r5 = "video/avc";	 Catch:{ Exception -> 0x0363, all -> 0x0240 }
        r5 = selectColorFormat(r4, r5);	 Catch:{ Exception -> 0x0363, all -> 0x0240 }
        if (r5 != 0) goto L_0x02cf;
    L_0x02a3:
        r53 = r5;
        r5 = new java.lang.RuntimeException;	 Catch:{ Exception -> 0x02af, all -> 0x0240 }
        r54 = r6;	 Catch:{ Exception -> 0x02af, all -> 0x0240 }
        r6 = "no supported color format";	 Catch:{ Exception -> 0x02af, all -> 0x0240 }
        r5.<init>(r6);	 Catch:{ Exception -> 0x02af, all -> 0x0240 }
        throw r5;	 Catch:{ Exception -> 0x02af, all -> 0x0240 }
    L_0x02af:
        r0 = move-exception;
        r97 = r1;
        r91 = r2;
        r3 = r7;
        r98 = r13;
        r110 = r14;
        r82 = r15;
        r107 = r41;
        r14 = r42;
        r6 = r44;
        r104 = r45;
        r81 = r46;
        r108 = r47;
        r8 = r50;
        r4 = r51;
        r7 = r52;
        goto L_0x03ce;
    L_0x02cf:
        r53 = r5;
        r54 = r6;
        r5 = r4.getName();	 Catch:{ Exception -> 0x0363, all -> 0x0240 }
        r6 = "OMX.qcom.";	 Catch:{ Exception -> 0x0363, all -> 0x0240 }
        r6 = r5.contains(r6);	 Catch:{ Exception -> 0x0363, all -> 0x0240 }
        if (r6 == 0) goto L_0x02ff;	 Catch:{ Exception -> 0x0363, all -> 0x0240 }
    L_0x02df:
        r6 = 1;	 Catch:{ Exception -> 0x0363, all -> 0x0240 }
        r55 = r6;	 Catch:{ Exception -> 0x0363, all -> 0x0240 }
        r6 = android.os.Build.VERSION.SDK_INT;	 Catch:{ Exception -> 0x0363, all -> 0x0240 }
        r56 = r7;
        r7 = 16;
        if (r6 != r7) goto L_0x02fc;
    L_0x02ea:
        r6 = "lge";	 Catch:{ Exception -> 0x03af, all -> 0x0240 }
        r6 = r3.equals(r6);	 Catch:{ Exception -> 0x03af, all -> 0x0240 }
        if (r6 != 0) goto L_0x02fa;	 Catch:{ Exception -> 0x03af, all -> 0x0240 }
    L_0x02f2:
        r6 = "nokia";	 Catch:{ Exception -> 0x03af, all -> 0x0240 }
        r6 = r3.equals(r6);	 Catch:{ Exception -> 0x03af, all -> 0x0240 }
        if (r6 == 0) goto L_0x02fc;	 Catch:{ Exception -> 0x03af, all -> 0x0240 }
    L_0x02fa:
        r22 = 1;	 Catch:{ Exception -> 0x03af, all -> 0x0240 }
    L_0x02fc:
        r25 = r55;	 Catch:{ Exception -> 0x03af, all -> 0x0240 }
        goto L_0x032d;	 Catch:{ Exception -> 0x03af, all -> 0x0240 }
    L_0x02ff:
        r56 = r7;	 Catch:{ Exception -> 0x03af, all -> 0x0240 }
        r6 = "OMX.Intel.";	 Catch:{ Exception -> 0x03af, all -> 0x0240 }
        r6 = r5.contains(r6);	 Catch:{ Exception -> 0x03af, all -> 0x0240 }
        if (r6 == 0) goto L_0x030d;	 Catch:{ Exception -> 0x03af, all -> 0x0240 }
    L_0x0309:
        r6 = 2;	 Catch:{ Exception -> 0x03af, all -> 0x0240 }
    L_0x030a:
        r25 = r6;	 Catch:{ Exception -> 0x03af, all -> 0x0240 }
        goto L_0x032d;	 Catch:{ Exception -> 0x03af, all -> 0x0240 }
    L_0x030d:
        r6 = "OMX.MTK.VIDEO.ENCODER.AVC";	 Catch:{ Exception -> 0x03af, all -> 0x0240 }
        r6 = r5.equals(r6);	 Catch:{ Exception -> 0x03af, all -> 0x0240 }
        if (r6 == 0) goto L_0x0317;	 Catch:{ Exception -> 0x03af, all -> 0x0240 }
    L_0x0315:
        r6 = 3;	 Catch:{ Exception -> 0x03af, all -> 0x0240 }
        goto L_0x030a;	 Catch:{ Exception -> 0x03af, all -> 0x0240 }
    L_0x0317:
        r6 = "OMX.SEC.AVC.Encoder";	 Catch:{ Exception -> 0x03af, all -> 0x0240 }
        r6 = r5.equals(r6);	 Catch:{ Exception -> 0x03af, all -> 0x0240 }
        if (r6 == 0) goto L_0x0323;	 Catch:{ Exception -> 0x03af, all -> 0x0240 }
    L_0x031f:
        r6 = 4;	 Catch:{ Exception -> 0x03af, all -> 0x0240 }
        r22 = 1;	 Catch:{ Exception -> 0x03af, all -> 0x0240 }
        goto L_0x030a;	 Catch:{ Exception -> 0x03af, all -> 0x0240 }
    L_0x0323:
        r6 = "OMX.TI.DUCATI1.VIDEO.H264E";	 Catch:{ Exception -> 0x03af, all -> 0x0240 }
        r6 = r5.equals(r6);	 Catch:{ Exception -> 0x03af, all -> 0x0240 }
        if (r6 == 0) goto L_0x032d;	 Catch:{ Exception -> 0x03af, all -> 0x0240 }
    L_0x032b:
        r6 = 5;	 Catch:{ Exception -> 0x03af, all -> 0x0240 }
        goto L_0x030a;	 Catch:{ Exception -> 0x03af, all -> 0x0240 }
    L_0x032d:
        r6 = org.telegram.messenger.BuildVars.LOGS_ENABLED;	 Catch:{ Exception -> 0x03af, all -> 0x0240 }
        if (r6 == 0) goto L_0x035b;	 Catch:{ Exception -> 0x03af, all -> 0x0240 }
    L_0x0331:
        r6 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x03af, all -> 0x0240 }
        r6.<init>();	 Catch:{ Exception -> 0x03af, all -> 0x0240 }
        r7 = "codec = ";	 Catch:{ Exception -> 0x03af, all -> 0x0240 }
        r6.append(r7);	 Catch:{ Exception -> 0x03af, all -> 0x0240 }
        r7 = r4.getName();	 Catch:{ Exception -> 0x03af, all -> 0x0240 }
        r6.append(r7);	 Catch:{ Exception -> 0x03af, all -> 0x0240 }
        r7 = " manufacturer = ";	 Catch:{ Exception -> 0x03af, all -> 0x0240 }
        r6.append(r7);	 Catch:{ Exception -> 0x03af, all -> 0x0240 }
        r6.append(r3);	 Catch:{ Exception -> 0x03af, all -> 0x0240 }
        r7 = "device = ";	 Catch:{ Exception -> 0x03af, all -> 0x0240 }
        r6.append(r7);	 Catch:{ Exception -> 0x03af, all -> 0x0240 }
        r7 = android.os.Build.MODEL;	 Catch:{ Exception -> 0x03af, all -> 0x0240 }
        r6.append(r7);	 Catch:{ Exception -> 0x03af, all -> 0x0240 }
        r6 = r6.toString();	 Catch:{ Exception -> 0x03af, all -> 0x0240 }
        org.telegram.messenger.FileLog.d(r6);	 Catch:{ Exception -> 0x03af, all -> 0x0240 }
        r4 = r22;
        r6 = r25;
        r5 = r53;
        goto L_0x0394;
    L_0x0363:
        r0 = move-exception;
        r56 = r7;
        r97 = r1;
        r91 = r2;
        r98 = r13;
        r110 = r14;
        r82 = r15;
        r107 = r41;
        r14 = r42;
        r6 = r44;
        r104 = r45;
        r81 = r46;
        r108 = r47;
        r8 = r50;
        r4 = r51;
        r7 = r52;
        r3 = r56;
        r15 = r116;
        r1 = r0;
        goto L_0x1013;
    L_0x0389:
        r54 = r6;
        r56 = r7;
        r5 = 2130708361; // 0x7f000789 float:1.701803E38 double:1.0527098025E-314;
        r4 = r22;
        r6 = r25;
        r7 = org.telegram.messenger.BuildVars.LOGS_ENABLED;	 Catch:{ Exception -> 0x0f87, all -> 0x0fd2 }
        if (r7 == 0) goto L_0x03d3;
        r7 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x03af, all -> 0x0240 }
        r7.<init>();	 Catch:{ Exception -> 0x03af, all -> 0x0240 }
        r57 = r8;	 Catch:{ Exception -> 0x03af, all -> 0x0240 }
        r8 = "colorFormat = ";	 Catch:{ Exception -> 0x03af, all -> 0x0240 }
        r7.append(r8);	 Catch:{ Exception -> 0x03af, all -> 0x0240 }
        r7.append(r5);	 Catch:{ Exception -> 0x03af, all -> 0x0240 }
        r7 = r7.toString();	 Catch:{ Exception -> 0x03af, all -> 0x0240 }
        org.telegram.messenger.FileLog.d(r7);	 Catch:{ Exception -> 0x03af, all -> 0x0240 }
        goto L_0x03d5;
    L_0x03af:
        r0 = move-exception;
        r97 = r1;
        r91 = r2;
        r98 = r13;
        r110 = r14;
        r82 = r15;
        r107 = r41;
        r14 = r42;
        r6 = r44;
        r104 = r45;
        r81 = r46;
        r108 = r47;
        r8 = r50;
        r4 = r51;
        r7 = r52;
        r3 = r56;
    L_0x03ce:
        r15 = r116;
        r1 = r0;
        goto L_0x1013;
        r57 = r8;
        r7 = r42;
        r8 = 0;
        r9 = r42;
        r22 = r14 * r9;
        r59 = r8;
        r8 = 3;
        r22 = r22 * 3;
        r22 = r22 / 2;	 Catch:{ Exception -> 0x0f64, all -> 0x0f4a }
        if (r6 != 0) goto L_0x0440;
        r25 = r9 % 16;	 Catch:{ Exception -> 0x041d, all -> 0x0407 }
        if (r25 == 0) goto L_0x0403;	 Catch:{ Exception -> 0x041d, all -> 0x0407 }
        r25 = r9 % 16;	 Catch:{ Exception -> 0x041d, all -> 0x0407 }
        r26 = 16;	 Catch:{ Exception -> 0x041d, all -> 0x0407 }
        r25 = 16 - r25;	 Catch:{ Exception -> 0x041d, all -> 0x0407 }
        r7 = r7 + r25;	 Catch:{ Exception -> 0x041d, all -> 0x0407 }
        r25 = r7 - r9;	 Catch:{ Exception -> 0x041d, all -> 0x0407 }
        r25 = r25 * r14;	 Catch:{ Exception -> 0x041d, all -> 0x0407 }
        r26 = r25 * 5;	 Catch:{ Exception -> 0x041d, all -> 0x0407 }
        r26 = r26 / 4;	 Catch:{ Exception -> 0x041d, all -> 0x0407 }
        r22 = r22 + r26;	 Catch:{ Exception -> 0x041d, all -> 0x0407 }
        r60 = r10;	 Catch:{ Exception -> 0x041d, all -> 0x0407 }
        r8 = r22;	 Catch:{ Exception -> 0x041d, all -> 0x0407 }
        r59 = r25;	 Catch:{ Exception -> 0x041d, all -> 0x0407 }
        goto L_0x0489;	 Catch:{ Exception -> 0x041d, all -> 0x0407 }
        r60 = r10;	 Catch:{ Exception -> 0x041d, all -> 0x0407 }
        goto L_0x0487;	 Catch:{ Exception -> 0x041d, all -> 0x0407 }
    L_0x0407:
        r0 = move-exception;	 Catch:{ Exception -> 0x041d, all -> 0x0407 }
        r1 = r0;	 Catch:{ Exception -> 0x041d, all -> 0x0407 }
        r5 = r12;	 Catch:{ Exception -> 0x041d, all -> 0x0407 }
        r110 = r14;	 Catch:{ Exception -> 0x041d, all -> 0x0407 }
        r82 = r15;	 Catch:{ Exception -> 0x041d, all -> 0x0407 }
        r11 = r41;	 Catch:{ Exception -> 0x041d, all -> 0x0407 }
        r4 = r45;	 Catch:{ Exception -> 0x041d, all -> 0x0407 }
        r81 = r46;	 Catch:{ Exception -> 0x041d, all -> 0x0407 }
        r3 = r49;	 Catch:{ Exception -> 0x041d, all -> 0x0407 }
        r15 = r116;	 Catch:{ Exception -> 0x041d, all -> 0x0407 }
        r14 = r9;	 Catch:{ Exception -> 0x041d, all -> 0x0407 }
        r9 = r47;	 Catch:{ Exception -> 0x041d, all -> 0x0407 }
        goto L_0x1295;	 Catch:{ Exception -> 0x041d, all -> 0x0407 }
    L_0x041d:
        r0 = move-exception;	 Catch:{ Exception -> 0x041d, all -> 0x0407 }
        r97 = r1;	 Catch:{ Exception -> 0x041d, all -> 0x0407 }
        r91 = r2;	 Catch:{ Exception -> 0x041d, all -> 0x0407 }
        r98 = r13;	 Catch:{ Exception -> 0x041d, all -> 0x0407 }
        r110 = r14;	 Catch:{ Exception -> 0x041d, all -> 0x0407 }
        r82 = r15;	 Catch:{ Exception -> 0x041d, all -> 0x0407 }
        r107 = r41;	 Catch:{ Exception -> 0x041d, all -> 0x0407 }
        r6 = r44;	 Catch:{ Exception -> 0x041d, all -> 0x0407 }
        r104 = r45;	 Catch:{ Exception -> 0x041d, all -> 0x0407 }
        r81 = r46;	 Catch:{ Exception -> 0x041d, all -> 0x0407 }
        r108 = r47;	 Catch:{ Exception -> 0x041d, all -> 0x0407 }
        r8 = r50;	 Catch:{ Exception -> 0x041d, all -> 0x0407 }
        r4 = r51;	 Catch:{ Exception -> 0x041d, all -> 0x0407 }
        r7 = r52;	 Catch:{ Exception -> 0x041d, all -> 0x0407 }
        r3 = r56;	 Catch:{ Exception -> 0x041d, all -> 0x0407 }
        r15 = r116;	 Catch:{ Exception -> 0x041d, all -> 0x0407 }
        r1 = r0;	 Catch:{ Exception -> 0x041d, all -> 0x0407 }
        r14 = r9;	 Catch:{ Exception -> 0x041d, all -> 0x0407 }
        goto L_0x1013;	 Catch:{ Exception -> 0x041d, all -> 0x0407 }
        r8 = 1;	 Catch:{ Exception -> 0x041d, all -> 0x0407 }
        if (r6 != r8) goto L_0x0463;	 Catch:{ Exception -> 0x041d, all -> 0x0407 }
        r8 = r3.toLowerCase();	 Catch:{ Exception -> 0x041d, all -> 0x0407 }
        r60 = r10;	 Catch:{ Exception -> 0x041d, all -> 0x0407 }
        r10 = "lge";	 Catch:{ Exception -> 0x041d, all -> 0x0407 }
        r8 = r8.equals(r10);	 Catch:{ Exception -> 0x041d, all -> 0x0407 }
        if (r8 != 0) goto L_0x0487;	 Catch:{ Exception -> 0x041d, all -> 0x0407 }
        r8 = r14 * r9;	 Catch:{ Exception -> 0x041d, all -> 0x0407 }
        r8 = r8 + 2047;	 Catch:{ Exception -> 0x041d, all -> 0x0407 }
        r8 = r8 & -2048;	 Catch:{ Exception -> 0x041d, all -> 0x0407 }
        r10 = r14 * r9;	 Catch:{ Exception -> 0x041d, all -> 0x0407 }
        r10 = r8 - r10;	 Catch:{ Exception -> 0x041d, all -> 0x0407 }
        r22 = r22 + r10;	 Catch:{ Exception -> 0x041d, all -> 0x0407 }
        r59 = r10;	 Catch:{ Exception -> 0x041d, all -> 0x0407 }
        r8 = r22;	 Catch:{ Exception -> 0x041d, all -> 0x0407 }
        goto L_0x0489;	 Catch:{ Exception -> 0x041d, all -> 0x0407 }
        r60 = r10;	 Catch:{ Exception -> 0x041d, all -> 0x0407 }
        r8 = 5;	 Catch:{ Exception -> 0x041d, all -> 0x0407 }
        if (r6 != r8) goto L_0x0469;	 Catch:{ Exception -> 0x041d, all -> 0x0407 }
        goto L_0x0487;	 Catch:{ Exception -> 0x041d, all -> 0x0407 }
        r8 = 3;	 Catch:{ Exception -> 0x041d, all -> 0x0407 }
        if (r6 != r8) goto L_0x0487;	 Catch:{ Exception -> 0x041d, all -> 0x0407 }
        r8 = "baidu";	 Catch:{ Exception -> 0x041d, all -> 0x0407 }
        r8 = r3.equals(r8);	 Catch:{ Exception -> 0x041d, all -> 0x0407 }
        if (r8 == 0) goto L_0x0487;	 Catch:{ Exception -> 0x041d, all -> 0x0407 }
        r8 = r9 % 16;	 Catch:{ Exception -> 0x041d, all -> 0x0407 }
        r10 = 16;	 Catch:{ Exception -> 0x041d, all -> 0x0407 }
        r8 = 16 - r8;	 Catch:{ Exception -> 0x041d, all -> 0x0407 }
        r7 = r7 + r8;	 Catch:{ Exception -> 0x041d, all -> 0x0407 }
        r8 = r7 - r9;	 Catch:{ Exception -> 0x041d, all -> 0x0407 }
        r8 = r8 * r14;	 Catch:{ Exception -> 0x041d, all -> 0x0407 }
        r10 = r8 * 5;	 Catch:{ Exception -> 0x041d, all -> 0x0407 }
        r10 = r10 / 4;	 Catch:{ Exception -> 0x041d, all -> 0x0407 }
        r22 = r22 + r10;
        r59 = r8;
        goto L_0x0460;
        r8 = r22;
        r13.selectTrack(r1);	 Catch:{ Exception -> 0x0f64, all -> 0x0f4a }
        r10 = r13.getTrackFormat(r1);	 Catch:{ Exception -> 0x0f64, all -> 0x0f4a }
        r22 = 0;
        if (r2 < 0) goto L_0x0515;
        r13.selectTrack(r2);	 Catch:{ Exception -> 0x04f2, all -> 0x04db }
        r25 = r13.getTrackFormat(r2);	 Catch:{ Exception -> 0x04f2, all -> 0x04db }
        r68 = r25;	 Catch:{ Exception -> 0x04f2, all -> 0x04db }
        r69 = r3;	 Catch:{ Exception -> 0x04f2, all -> 0x04db }
        r3 = "max-input-size";	 Catch:{ Exception -> 0x04f2, all -> 0x04db }
        r70 = r6;	 Catch:{ Exception -> 0x04f2, all -> 0x04db }
        r6 = r68;	 Catch:{ Exception -> 0x04f2, all -> 0x04db }
        r3 = r6.getInteger(r3);	 Catch:{ Exception -> 0x04f2, all -> 0x04db }
        r25 = java.nio.ByteBuffer.allocateDirect(r3);	 Catch:{ Exception -> 0x04f2, all -> 0x04db }
        r22 = r25;
        r71 = r3;
        r72 = r7;
        r3 = r41;
        r7 = 1;
        r25 = r3.addTrack(r6, r7);	 Catch:{ Exception -> 0x04cc, all -> 0x04c2 }
        r24 = r25;
        r6 = r22;
        r7 = r24;
        goto L_0x0521;
    L_0x04c2:
        r0 = move-exception;
        r1 = r0;
        r11 = r3;
        r5 = r12;
        r110 = r14;
        r82 = r15;
        goto L_0x0410;
    L_0x04cc:
        r0 = move-exception;
        r97 = r1;
        r91 = r2;
        r107 = r3;
        r98 = r13;
        r110 = r14;
        r82 = r15;
        goto L_0x042a;
    L_0x04db:
        r0 = move-exception;
        r3 = r41;
        r1 = r0;
        r11 = r3;
        r5 = r12;
        r110 = r14;
        r82 = r15;
        r4 = r45;
        r81 = r46;
        r3 = r49;
        r15 = r116;
        r14 = r9;
        r9 = r47;
        goto L_0x1295;
    L_0x04f2:
        r0 = move-exception;
        r97 = r1;
        r91 = r2;
        r98 = r13;
        r110 = r14;
        r82 = r15;
        r107 = r41;
        r6 = r44;
        r104 = r45;
        r81 = r46;
        r108 = r47;
        r8 = r50;
        r4 = r51;
        r7 = r52;
        r3 = r56;
        r15 = r116;
        r1 = r0;
        r14 = r9;
        goto L_0x1013;
        r69 = r3;
        r70 = r6;
        r72 = r7;
        r3 = r41;
        r6 = r22;
        r7 = r24;
        r74 = r3;
        r73 = r4;
        r3 = 0;
        r76 = r7;
        r75 = r8;
        r7 = r47;
        r22 = (r7 > r3 ? 1 : (r7 == r3 ? 0 : -1));
        if (r22 <= 0) goto L_0x056c;
        r3 = 0;
        r13.seekTo(r7, r3);	 Catch:{ Exception -> 0x054d, all -> 0x0538 }
        r77 = r7;
        goto L_0x0574;
    L_0x0538:
        r0 = move-exception;
        r1 = r0;
        r5 = r12;
        r110 = r14;
        r82 = r15;
        r4 = r45;
        r81 = r46;
        r3 = r49;
        r11 = r74;
        r15 = r116;
        r14 = r9;
        r9 = r7;
        goto L_0x1295;
    L_0x054d:
        r0 = move-exception;
        r97 = r1;
        r91 = r2;
        r108 = r7;
        r98 = r13;
        r110 = r14;
        r82 = r15;
        r6 = r44;
        r104 = r45;
        r81 = r46;
        r8 = r50;
        r4 = r51;
        r7 = r52;
        r3 = r56;
        r107 = r74;
        goto L_0x043a;
        r3 = 0;
        r77 = r7;
        r7 = 0;
        r13.seekTo(r7, r3);	 Catch:{ Exception -> 0x0f27, all -> 0x0f0d }
        r3 = "video/avc";	 Catch:{ Exception -> 0x0f27, all -> 0x0f0d }
        r3 = android.media.MediaFormat.createVideoFormat(r3, r14, r9);	 Catch:{ Exception -> 0x0f27, all -> 0x0f0d }
        r4 = "color-format";	 Catch:{ Exception -> 0x0f27, all -> 0x0f0d }
        r3.setInteger(r4, r5);	 Catch:{ Exception -> 0x0f27, all -> 0x0f0d }
        r4 = "bitrate";	 Catch:{ Exception -> 0x0f27, all -> 0x0f0d }
        if (r15 <= 0) goto L_0x0585;	 Catch:{ Exception -> 0x0f27, all -> 0x0f0d }
        r7 = r15;	 Catch:{ Exception -> 0x0f27, all -> 0x0f0d }
        goto L_0x0588;	 Catch:{ Exception -> 0x0f27, all -> 0x0f0d }
        r7 = 921600; // 0xe1000 float:1.291437E-39 double:4.55331E-318;	 Catch:{ Exception -> 0x0f27, all -> 0x0f0d }
        r3.setInteger(r4, r7);	 Catch:{ Exception -> 0x0f27, all -> 0x0f0d }
        r4 = "frame-rate";	 Catch:{ Exception -> 0x0f27, all -> 0x0f0d }
        r7 = 25;	 Catch:{ Exception -> 0x0f27, all -> 0x0f0d }
        r3.setInteger(r4, r7);	 Catch:{ Exception -> 0x0f27, all -> 0x0f0d }
        r4 = "i-frame-interval";	 Catch:{ Exception -> 0x0f27, all -> 0x0f0d }
        r7 = 10;	 Catch:{ Exception -> 0x0f27, all -> 0x0f0d }
        r3.setInteger(r4, r7);	 Catch:{ Exception -> 0x0f27, all -> 0x0f0d }
        r4 = android.os.Build.VERSION.SDK_INT;	 Catch:{ Exception -> 0x0f27, all -> 0x0f0d }
        r7 = 18;
        if (r4 >= r7) goto L_0x05e1;
        r4 = "stride";	 Catch:{ Exception -> 0x05c2, all -> 0x05ac }
        r7 = r14 + 32;	 Catch:{ Exception -> 0x05c2, all -> 0x05ac }
        r3.setInteger(r4, r7);	 Catch:{ Exception -> 0x05c2, all -> 0x05ac }
        r4 = "slice-height";	 Catch:{ Exception -> 0x05c2, all -> 0x05ac }
        r3.setInteger(r4, r9);	 Catch:{ Exception -> 0x05c2, all -> 0x05ac }
        goto L_0x05e1;
    L_0x05ac:
        r0 = move-exception;
        r1 = r0;
        r5 = r12;
        r110 = r14;
        r82 = r15;
        r4 = r45;
        r81 = r46;
        r3 = r49;
        r11 = r74;
        r15 = r116;
        r14 = r9;
        r9 = r77;
        goto L_0x1295;
    L_0x05c2:
        r0 = move-exception;
        r97 = r1;
        r91 = r2;
        r98 = r13;
        r110 = r14;
        r82 = r15;
        r6 = r44;
        r104 = r45;
        r81 = r46;
        r8 = r50;
        r4 = r51;
        r7 = r52;
        r3 = r56;
        r107 = r74;
        r108 = r77;
        goto L_0x043a;
        r4 = "video/avc";	 Catch:{ Exception -> 0x0f27, all -> 0x0f0d }
        r4 = android.media.MediaCodec.createEncoderByType(r4);	 Catch:{ Exception -> 0x0f27, all -> 0x0f0d }
        r7 = 1;
        r8 = 0;
        r4.configure(r3, r8, r8, r7);	 Catch:{ Exception -> 0x0ef1, all -> 0x0f0d }
        r7 = android.os.Build.VERSION.SDK_INT;	 Catch:{ Exception -> 0x0ef1, all -> 0x0f0d }
        r8 = 18;
        if (r7 < r8) goto L_0x0627;
        r7 = new org.telegram.messenger.video.InputSurface;	 Catch:{ Exception -> 0x0613, all -> 0x05ac }
        r8 = r4.createInputSurface();	 Catch:{ Exception -> 0x0613, all -> 0x05ac }
        r7.<init>(r8);	 Catch:{ Exception -> 0x0613, all -> 0x05ac }
        r7.makeCurrent();	 Catch:{ Exception -> 0x05ff, all -> 0x05ac }
        goto L_0x0629;
    L_0x05ff:
        r0 = move-exception;
        r97 = r1;
        r91 = r2;
        r98 = r13;
        r110 = r14;
        r82 = r15;
        r6 = r44;
        r104 = r45;
        r81 = r46;
        r8 = r50;
        goto L_0x05d9;
    L_0x0613:
        r0 = move-exception;
        r97 = r1;
        r91 = r2;
        r98 = r13;
        r110 = r14;
        r82 = r15;
        r6 = r44;
        r104 = r45;
        r81 = r46;
        r8 = r50;
        goto L_0x05d7;
        r7 = r52;
        r4.start();	 Catch:{ Exception -> 0x0ed4, all -> 0x0f0d }
        r8 = "mime";	 Catch:{ Exception -> 0x0ed4, all -> 0x0f0d }
        r8 = r10.getString(r8);	 Catch:{ Exception -> 0x0ed4, all -> 0x0f0d }
        r8 = android.media.MediaCodec.createDecoderByType(r8);	 Catch:{ Exception -> 0x0ed4, all -> 0x0f0d }
        r79 = r3;
        r3 = android.os.Build.VERSION.SDK_INT;	 Catch:{ Exception -> 0x0eb9, all -> 0x0f0d }
        r80 = r11;
        r11 = 18;
        if (r3 < r11) goto L_0x065b;
        r3 = new org.telegram.messenger.video.OutputSurface;	 Catch:{ Exception -> 0x0648, all -> 0x05ac }
        r3.<init>();	 Catch:{ Exception -> 0x0648, all -> 0x05ac }
        r11 = r46;
        goto L_0x0662;
    L_0x0648:
        r0 = move-exception;
        r97 = r1;
        r91 = r2;
        r98 = r13;
        r110 = r14;
        r82 = r15;
        r6 = r44;
        r104 = r45;
        r81 = r46;
        goto L_0x05d9;
        r3 = new org.telegram.messenger.video.OutputSurface;	 Catch:{ Exception -> 0x0eb9, all -> 0x0f0d }
        r11 = r46;
        r3.<init>(r14, r9, r11);	 Catch:{ Exception -> 0x0e9c, all -> 0x0e82 }
        r81 = r11;
        r11 = r3.getSurface();	 Catch:{ Exception -> 0x0e69, all -> 0x0e51 }
        r83 = r5;
        r82 = r15;
        r5 = 0;
        r15 = 0;
        r8.configure(r10, r11, r5, r15);	 Catch:{ Exception -> 0x0e3a, all -> 0x0e24 }
        r8.start();	 Catch:{ Exception -> 0x0e3a, all -> 0x0e24 }
        r5 = 2500; // 0x9c4 float:3.503E-42 double:1.235E-320;	 Catch:{ Exception -> 0x0e3a, all -> 0x0e24 }
        r11 = 0;	 Catch:{ Exception -> 0x0e3a, all -> 0x0e24 }
        r15 = 0;	 Catch:{ Exception -> 0x0e3a, all -> 0x0e24 }
        r22 = 0;	 Catch:{ Exception -> 0x0e3a, all -> 0x0e24 }
        r84 = r5;	 Catch:{ Exception -> 0x0e3a, all -> 0x0e24 }
        r5 = android.os.Build.VERSION.SDK_INT;	 Catch:{ Exception -> 0x0e3a, all -> 0x0e24 }
        r85 = r10;
        r10 = 21;
        if (r5 >= r10) goto L_0x06b3;
        r5 = r8.getInputBuffers();	 Catch:{ Exception -> 0x06a4, all -> 0x069b }
        r11 = r5;	 Catch:{ Exception -> 0x06a4, all -> 0x069b }
        r5 = r4.getOutputBuffers();	 Catch:{ Exception -> 0x06a4, all -> 0x069b }
        r15 = r5;	 Catch:{ Exception -> 0x06a4, all -> 0x069b }
        r5 = android.os.Build.VERSION.SDK_INT;	 Catch:{ Exception -> 0x06a4, all -> 0x069b }
        r10 = 18;	 Catch:{ Exception -> 0x06a4, all -> 0x069b }
        if (r5 >= r10) goto L_0x06b3;	 Catch:{ Exception -> 0x06a4, all -> 0x069b }
        r5 = r4.getInputBuffers();	 Catch:{ Exception -> 0x06a4, all -> 0x069b }
        r22 = r5;
        goto L_0x06b5;
    L_0x069b:
        r0 = move-exception;
        r1 = r0;
        r5 = r12;
        r110 = r14;
        r4 = r45;
        goto L_0x05b7;
    L_0x06a4:
        r0 = move-exception;
        r97 = r1;
        r91 = r2;
        r98 = r13;
        r110 = r14;
        r6 = r44;
        r104 = r45;
        goto L_0x05db;
        r5 = r22;
        r115.checkConversionCanceled();	 Catch:{ Exception -> 0x0e3a, all -> 0x0e24 }
        if (r54 != 0) goto L_0x0e0f;	 Catch:{ Exception -> 0x0e3a, all -> 0x0e24 }
        r115.checkConversionCanceled();	 Catch:{ Exception -> 0x0e3a, all -> 0x0e24 }
        r86 = r9;
        if (r60 != 0) goto L_0x08cd;
        r22 = 0;
        r24 = r13.getSampleTrackIndex();	 Catch:{ Exception -> 0x08b5, all -> 0x08a2 }
        r87 = r24;
        r9 = r87;
        if (r9 != r1) goto L_0x074e;
        r88 = r14;
        r89 = r15;
        r14 = 2500; // 0x9c4 float:3.503E-42 double:1.235E-320;
        r10 = r8.dequeueInputBuffer(r14);	 Catch:{ Exception -> 0x0739, all -> 0x0728 }
        if (r10 < 0) goto L_0x0715;	 Catch:{ Exception -> 0x0739, all -> 0x0728 }
        r14 = android.os.Build.VERSION.SDK_INT;	 Catch:{ Exception -> 0x0739, all -> 0x0728 }
        r15 = 21;	 Catch:{ Exception -> 0x0739, all -> 0x0728 }
        if (r14 >= r15) goto L_0x06e2;	 Catch:{ Exception -> 0x0739, all -> 0x0728 }
        r14 = r11[r10];	 Catch:{ Exception -> 0x0739, all -> 0x0728 }
        goto L_0x06e6;	 Catch:{ Exception -> 0x0739, all -> 0x0728 }
        r14 = r8.getInputBuffer(r10);	 Catch:{ Exception -> 0x0739, all -> 0x0728 }
        r15 = 0;	 Catch:{ Exception -> 0x0739, all -> 0x0728 }
        r24 = r13.readSampleData(r14, r15);	 Catch:{ Exception -> 0x0739, all -> 0x0728 }
        r15 = r24;	 Catch:{ Exception -> 0x0739, all -> 0x0728 }
        if (r15 >= 0) goto L_0x0701;	 Catch:{ Exception -> 0x0739, all -> 0x0728 }
        r63 = 0;	 Catch:{ Exception -> 0x0739, all -> 0x0728 }
        r64 = 0;	 Catch:{ Exception -> 0x0739, all -> 0x0728 }
        r65 = 0;	 Catch:{ Exception -> 0x0739, all -> 0x0728 }
        r67 = 4;	 Catch:{ Exception -> 0x0739, all -> 0x0728 }
        r61 = r8;	 Catch:{ Exception -> 0x0739, all -> 0x0728 }
        r62 = r10;	 Catch:{ Exception -> 0x0739, all -> 0x0728 }
        r61.queueInputBuffer(r62, r63, r64, r65, r67);	 Catch:{ Exception -> 0x0739, all -> 0x0728 }
        r60 = 1;	 Catch:{ Exception -> 0x0739, all -> 0x0728 }
        goto L_0x0715;	 Catch:{ Exception -> 0x0739, all -> 0x0728 }
        r63 = 0;	 Catch:{ Exception -> 0x0739, all -> 0x0728 }
        r65 = r13.getSampleTime();	 Catch:{ Exception -> 0x0739, all -> 0x0728 }
        r67 = 0;	 Catch:{ Exception -> 0x0739, all -> 0x0728 }
        r61 = r8;	 Catch:{ Exception -> 0x0739, all -> 0x0728 }
        r62 = r10;	 Catch:{ Exception -> 0x0739, all -> 0x0728 }
        r64 = r15;	 Catch:{ Exception -> 0x0739, all -> 0x0728 }
        r61.queueInputBuffer(r62, r63, r64, r65, r67);	 Catch:{ Exception -> 0x0739, all -> 0x0728 }
        r13.advance();	 Catch:{ Exception -> 0x0739, all -> 0x0728 }
        r91 = r2;
        r92 = r6;
        r90 = r11;
        r6 = r44;
        r2 = r45;
        r11 = r74;
        r14 = r76;
        r15 = r116;
        goto L_0x0862;
    L_0x0728:
        r0 = move-exception;
        r1 = r0;
        r5 = r12;
        r4 = r45;
        r3 = r49;
        r11 = r74;
        r9 = r77;
        r14 = r86;
        r110 = r88;
        goto L_0x0253;
    L_0x0739:
        r0 = move-exception;
        r97 = r1;
        r91 = r2;
        r98 = r13;
        r6 = r44;
        r104 = r45;
        r107 = r74;
        r108 = r77;
        r14 = r86;
        r110 = r88;
        goto L_0x03ce;
        r88 = r14;
        r89 = r15;
        r10 = -1;
        if (r2 == r10) goto L_0x084d;
        if (r9 != r2) goto L_0x084d;
        r10 = 0;
        r14 = r13.readSampleData(r6, r10);	 Catch:{ Exception -> 0x0835, all -> 0x0822 }
        r15 = r44;
        r15.size = r14;	 Catch:{ Exception -> 0x080b, all -> 0x0822 }
        r14 = android.os.Build.VERSION.SDK_INT;	 Catch:{ Exception -> 0x080b, all -> 0x0822 }
        r10 = 21;
        if (r14 >= r10) goto L_0x0779;
        r10 = 0;
        r6.position(r10);	 Catch:{ Exception -> 0x0770, all -> 0x0728 }
        r10 = r15.size;	 Catch:{ Exception -> 0x0770, all -> 0x0728 }
        r6.limit(r10);	 Catch:{ Exception -> 0x0770, all -> 0x0728 }
        goto L_0x0779;
    L_0x0770:
        r0 = move-exception;
        r97 = r1;
        r91 = r2;
        r98 = r13;
        r6 = r15;
        goto L_0x0742;
        r10 = r15.size;	 Catch:{ Exception -> 0x080b, all -> 0x0822 }
        if (r10 < 0) goto L_0x0789;
        r90 = r11;
        r10 = r13.getSampleTime();	 Catch:{ Exception -> 0x0770, all -> 0x0728 }
        r15.presentationTimeUs = r10;	 Catch:{ Exception -> 0x0770, all -> 0x0728 }
        r13.advance();	 Catch:{ Exception -> 0x0770, all -> 0x0728 }
        goto L_0x0790;
        r90 = r11;
        r10 = 0;
        r15.size = r10;	 Catch:{ Exception -> 0x080b, all -> 0x0822 }
        r60 = 1;	 Catch:{ Exception -> 0x080b, all -> 0x0822 }
        r10 = r15.size;	 Catch:{ Exception -> 0x080b, all -> 0x0822 }
        if (r10 <= 0) goto L_0x07fc;
        r10 = 0;
        r14 = (r19 > r10 ? 1 : (r19 == r10 ? 0 : -1));
        if (r14 < 0) goto L_0x07a8;
        r10 = r15.presentationTimeUs;	 Catch:{ Exception -> 0x0770, all -> 0x0728 }
        r14 = (r10 > r19 ? 1 : (r10 == r19 ? 0 : -1));
        if (r14 >= 0) goto L_0x07a1;
        goto L_0x07a8;
        r91 = r2;
        r92 = r6;
        r6 = r15;
        goto L_0x071e;
        r10 = 0;
        r15.offset = r10;	 Catch:{ Exception -> 0x080b, all -> 0x0822 }
        r11 = r13.getSampleFlags();	 Catch:{ Exception -> 0x080b, all -> 0x0822 }
        r15.flags = r11;	 Catch:{ Exception -> 0x080b, all -> 0x0822 }
        r11 = r74;
        r14 = r76;
        r18 = r11.writeSampleData(r14, r6, r15, r10);	 Catch:{ Exception -> 0x07e5, all -> 0x07d4 }
        if (r18 == 0) goto L_0x07c9;
        r91 = r2;
        r92 = r6;
        r6 = r15;
        r2 = r45;
        r15 = r116;
        r12.didWriteData(r15, r2, r10, r10);	 Catch:{ Exception -> 0x0891, all -> 0x0883 }
        goto L_0x0862;	 Catch:{ Exception -> 0x0891, all -> 0x0883 }
        r91 = r2;	 Catch:{ Exception -> 0x0891, all -> 0x0883 }
        r92 = r6;	 Catch:{ Exception -> 0x0891, all -> 0x0883 }
        r6 = r15;	 Catch:{ Exception -> 0x0891, all -> 0x0883 }
        r2 = r45;	 Catch:{ Exception -> 0x0891, all -> 0x0883 }
        r15 = r116;	 Catch:{ Exception -> 0x0891, all -> 0x0883 }
        goto L_0x0862;	 Catch:{ Exception -> 0x0891, all -> 0x0883 }
    L_0x07d4:
        r0 = move-exception;	 Catch:{ Exception -> 0x0891, all -> 0x0883 }
        r15 = r116;	 Catch:{ Exception -> 0x0891, all -> 0x0883 }
        r1 = r0;	 Catch:{ Exception -> 0x0891, all -> 0x0883 }
        r5 = r12;	 Catch:{ Exception -> 0x0891, all -> 0x0883 }
        r4 = r45;	 Catch:{ Exception -> 0x0891, all -> 0x0883 }
        r3 = r49;	 Catch:{ Exception -> 0x0891, all -> 0x0883 }
        r9 = r77;	 Catch:{ Exception -> 0x0891, all -> 0x0883 }
        r14 = r86;	 Catch:{ Exception -> 0x0891, all -> 0x0883 }
        r110 = r88;	 Catch:{ Exception -> 0x0891, all -> 0x0883 }
        goto L_0x1295;	 Catch:{ Exception -> 0x0891, all -> 0x0883 }
    L_0x07e5:
        r0 = move-exception;	 Catch:{ Exception -> 0x0891, all -> 0x0883 }
        r91 = r2;	 Catch:{ Exception -> 0x0891, all -> 0x0883 }
        r6 = r15;	 Catch:{ Exception -> 0x0891, all -> 0x0883 }
        r15 = r116;	 Catch:{ Exception -> 0x0891, all -> 0x0883 }
        r97 = r1;	 Catch:{ Exception -> 0x0891, all -> 0x0883 }
        r107 = r11;	 Catch:{ Exception -> 0x0891, all -> 0x0883 }
        r98 = r13;	 Catch:{ Exception -> 0x0891, all -> 0x0883 }
        r104 = r45;	 Catch:{ Exception -> 0x0891, all -> 0x0883 }
        r108 = r77;	 Catch:{ Exception -> 0x0891, all -> 0x0883 }
        r14 = r86;	 Catch:{ Exception -> 0x0891, all -> 0x0883 }
        r110 = r88;	 Catch:{ Exception -> 0x0891, all -> 0x0883 }
        r1 = r0;	 Catch:{ Exception -> 0x0891, all -> 0x0883 }
        goto L_0x1013;	 Catch:{ Exception -> 0x0891, all -> 0x0883 }
        r91 = r2;	 Catch:{ Exception -> 0x0891, all -> 0x0883 }
        r92 = r6;	 Catch:{ Exception -> 0x0891, all -> 0x0883 }
        r6 = r15;	 Catch:{ Exception -> 0x0891, all -> 0x0883 }
        r2 = r45;	 Catch:{ Exception -> 0x0891, all -> 0x0883 }
        r11 = r74;	 Catch:{ Exception -> 0x0891, all -> 0x0883 }
        r14 = r76;	 Catch:{ Exception -> 0x0891, all -> 0x0883 }
        r15 = r116;	 Catch:{ Exception -> 0x0891, all -> 0x0883 }
        goto L_0x0862;	 Catch:{ Exception -> 0x0891, all -> 0x0883 }
    L_0x080b:
        r0 = move-exception;	 Catch:{ Exception -> 0x0891, all -> 0x0883 }
        r91 = r2;	 Catch:{ Exception -> 0x0891, all -> 0x0883 }
        r6 = r15;	 Catch:{ Exception -> 0x0891, all -> 0x0883 }
        r15 = r116;	 Catch:{ Exception -> 0x0891, all -> 0x0883 }
        r97 = r1;	 Catch:{ Exception -> 0x0891, all -> 0x0883 }
        r98 = r13;	 Catch:{ Exception -> 0x0891, all -> 0x0883 }
        r104 = r45;	 Catch:{ Exception -> 0x0891, all -> 0x0883 }
        r107 = r74;	 Catch:{ Exception -> 0x0891, all -> 0x0883 }
        r108 = r77;	 Catch:{ Exception -> 0x0891, all -> 0x0883 }
        r14 = r86;	 Catch:{ Exception -> 0x0891, all -> 0x0883 }
        r110 = r88;	 Catch:{ Exception -> 0x0891, all -> 0x0883 }
        r1 = r0;	 Catch:{ Exception -> 0x0891, all -> 0x0883 }
        goto L_0x1013;	 Catch:{ Exception -> 0x0891, all -> 0x0883 }
    L_0x0822:
        r0 = move-exception;	 Catch:{ Exception -> 0x0891, all -> 0x0883 }
        r11 = r74;	 Catch:{ Exception -> 0x0891, all -> 0x0883 }
        r15 = r116;	 Catch:{ Exception -> 0x0891, all -> 0x0883 }
        r1 = r0;	 Catch:{ Exception -> 0x0891, all -> 0x0883 }
        r5 = r12;	 Catch:{ Exception -> 0x0891, all -> 0x0883 }
        r4 = r45;	 Catch:{ Exception -> 0x0891, all -> 0x0883 }
        r3 = r49;	 Catch:{ Exception -> 0x0891, all -> 0x0883 }
        r9 = r77;	 Catch:{ Exception -> 0x0891, all -> 0x0883 }
        r14 = r86;	 Catch:{ Exception -> 0x0891, all -> 0x0883 }
        r110 = r88;	 Catch:{ Exception -> 0x0891, all -> 0x0883 }
        goto L_0x1295;	 Catch:{ Exception -> 0x0891, all -> 0x0883 }
    L_0x0835:
        r0 = move-exception;	 Catch:{ Exception -> 0x0891, all -> 0x0883 }
        r91 = r2;	 Catch:{ Exception -> 0x0891, all -> 0x0883 }
        r6 = r44;	 Catch:{ Exception -> 0x0891, all -> 0x0883 }
        r15 = r116;	 Catch:{ Exception -> 0x0891, all -> 0x0883 }
        r97 = r1;	 Catch:{ Exception -> 0x0891, all -> 0x0883 }
        r98 = r13;	 Catch:{ Exception -> 0x0891, all -> 0x0883 }
        r104 = r45;	 Catch:{ Exception -> 0x0891, all -> 0x0883 }
        r107 = r74;	 Catch:{ Exception -> 0x0891, all -> 0x0883 }
        r108 = r77;	 Catch:{ Exception -> 0x0891, all -> 0x0883 }
        r14 = r86;	 Catch:{ Exception -> 0x0891, all -> 0x0883 }
        r110 = r88;	 Catch:{ Exception -> 0x0891, all -> 0x0883 }
        r1 = r0;	 Catch:{ Exception -> 0x0891, all -> 0x0883 }
        goto L_0x1013;	 Catch:{ Exception -> 0x0891, all -> 0x0883 }
        r91 = r2;	 Catch:{ Exception -> 0x0891, all -> 0x0883 }
        r92 = r6;	 Catch:{ Exception -> 0x0891, all -> 0x0883 }
        r90 = r11;	 Catch:{ Exception -> 0x0891, all -> 0x0883 }
        r6 = r44;	 Catch:{ Exception -> 0x0891, all -> 0x0883 }
        r2 = r45;	 Catch:{ Exception -> 0x0891, all -> 0x0883 }
        r11 = r74;	 Catch:{ Exception -> 0x0891, all -> 0x0883 }
        r14 = r76;	 Catch:{ Exception -> 0x0891, all -> 0x0883 }
        r15 = r116;	 Catch:{ Exception -> 0x0891, all -> 0x0883 }
        r10 = -1;	 Catch:{ Exception -> 0x0891, all -> 0x0883 }
        if (r9 != r10) goto L_0x0862;	 Catch:{ Exception -> 0x0891, all -> 0x0883 }
        r22 = 1;	 Catch:{ Exception -> 0x0891, all -> 0x0883 }
        if (r22 == 0) goto L_0x08e1;	 Catch:{ Exception -> 0x0891, all -> 0x0883 }
        r93 = r9;	 Catch:{ Exception -> 0x0891, all -> 0x0883 }
        r9 = 2500; // 0x9c4 float:3.503E-42 double:1.235E-320;	 Catch:{ Exception -> 0x0891, all -> 0x0883 }
        r24 = r8.dequeueInputBuffer(r9);	 Catch:{ Exception -> 0x0891, all -> 0x0883 }
        r9 = r24;	 Catch:{ Exception -> 0x0891, all -> 0x0883 }
        if (r9 < 0) goto L_0x08e1;	 Catch:{ Exception -> 0x0891, all -> 0x0883 }
        r63 = 0;	 Catch:{ Exception -> 0x0891, all -> 0x0883 }
        r64 = 0;	 Catch:{ Exception -> 0x0891, all -> 0x0883 }
        r65 = 0;	 Catch:{ Exception -> 0x0891, all -> 0x0883 }
        r67 = 4;	 Catch:{ Exception -> 0x0891, all -> 0x0883 }
        r61 = r8;	 Catch:{ Exception -> 0x0891, all -> 0x0883 }
        r62 = r9;	 Catch:{ Exception -> 0x0891, all -> 0x0883 }
        r61.queueInputBuffer(r62, r63, r64, r65, r67);	 Catch:{ Exception -> 0x0891, all -> 0x0883 }
        r60 = 1;
        goto L_0x08e1;
    L_0x0883:
        r0 = move-exception;
        r1 = r0;
        r4 = r2;
        r5 = r12;
        r3 = r49;
        r9 = r77;
        r14 = r86;
        r110 = r88;
        goto L_0x1295;
    L_0x0891:
        r0 = move-exception;
        r97 = r1;
        r104 = r2;
        r107 = r11;
        r98 = r13;
        r108 = r77;
        r14 = r86;
        r110 = r88;
        goto L_0x03d0;
    L_0x08a2:
        r0 = move-exception;
        r11 = r74;
        r15 = r116;
        r1 = r0;
        r5 = r12;
        r110 = r14;
        r4 = r45;
        r3 = r49;
        r9 = r77;
        r14 = r86;
        goto L_0x1295;
    L_0x08b5:
        r0 = move-exception;
        r91 = r2;
        r6 = r44;
        r15 = r116;
        r97 = r1;
        r98 = r13;
        r110 = r14;
        r104 = r45;
        r107 = r74;
        r108 = r77;
        r14 = r86;
        r1 = r0;
        goto L_0x1013;
        r91 = r2;
        r92 = r6;
        r90 = r11;
        r88 = r14;
        r89 = r15;
        r6 = r44;
        r2 = r45;
        r11 = r74;
        r14 = r76;
        r15 = r116;
        if (r80 != 0) goto L_0x08e5;
        r9 = 1;
        goto L_0x08e6;
        r9 = 0;
        r10 = r9;
        r94 = r14;
        r14 = r23;
        r9 = 1;
        if (r10 != 0) goto L_0x090a;
        if (r9 == 0) goto L_0x08f1;
        goto L_0x090a;
        r45 = r2;
        r44 = r6;
        r74 = r11;
        r23 = r14;
        r9 = r86;
        r14 = r88;
        r15 = r89;
        r11 = r90;
        r2 = r91;
        r6 = r92;
        r76 = r94;
        goto L_0x06b8;
        r115.checkConversionCanceled();	 Catch:{ Exception -> 0x0dfd, all -> 0x0deb }
        r96 = r9;	 Catch:{ Exception -> 0x0dfd, all -> 0x0deb }
        r95 = r10;	 Catch:{ Exception -> 0x0dfd, all -> 0x0deb }
        r9 = 2500; // 0x9c4 float:3.503E-42 double:1.235E-320;	 Catch:{ Exception -> 0x0dfd, all -> 0x0deb }
        r22 = r4.dequeueOutputBuffer(r6, r9);	 Catch:{ Exception -> 0x0dfd, all -> 0x0deb }
        r9 = r22;
        r10 = -1;
        if (r9 != r10) goto L_0x092e;
        r10 = 0;
        r97 = r1;
        r104 = r2;
        r99 = r5;
        r96 = r10;
        r98 = r13;
        r1 = r14;
        r14 = r86;
        r2 = r88;
        goto L_0x0b52;
        r10 = -3;
        if (r9 != r10) goto L_0x0956;
        r10 = android.os.Build.VERSION.SDK_INT;	 Catch:{ Exception -> 0x0944, all -> 0x0883 }
        r97 = r1;
        r1 = 21;
        if (r10 >= r1) goto L_0x093f;
        r1 = r4.getOutputBuffers();	 Catch:{ Exception -> 0x096b, all -> 0x0883 }
        r89 = r1;	 Catch:{ Exception -> 0x096b, all -> 0x0883 }
        r104 = r2;	 Catch:{ Exception -> 0x096b, all -> 0x0883 }
        r99 = r5;	 Catch:{ Exception -> 0x096b, all -> 0x0883 }
        goto L_0x0925;	 Catch:{ Exception -> 0x096b, all -> 0x0883 }
    L_0x0944:
        r0 = move-exception;	 Catch:{ Exception -> 0x096b, all -> 0x0883 }
        r97 = r1;	 Catch:{ Exception -> 0x096b, all -> 0x0883 }
        r1 = r0;	 Catch:{ Exception -> 0x096b, all -> 0x0883 }
        r104 = r2;	 Catch:{ Exception -> 0x096b, all -> 0x0883 }
        r107 = r11;	 Catch:{ Exception -> 0x096b, all -> 0x0883 }
        r98 = r13;	 Catch:{ Exception -> 0x096b, all -> 0x0883 }
        r108 = r77;	 Catch:{ Exception -> 0x096b, all -> 0x0883 }
        r14 = r86;	 Catch:{ Exception -> 0x096b, all -> 0x0883 }
        r110 = r88;	 Catch:{ Exception -> 0x096b, all -> 0x0883 }
        goto L_0x1013;	 Catch:{ Exception -> 0x096b, all -> 0x0883 }
        r97 = r1;	 Catch:{ Exception -> 0x096b, all -> 0x0883 }
        r1 = -2;	 Catch:{ Exception -> 0x096b, all -> 0x0883 }
        if (r9 != r1) goto L_0x097b;	 Catch:{ Exception -> 0x096b, all -> 0x0883 }
        r1 = r4.getOutputFormat();	 Catch:{ Exception -> 0x096b, all -> 0x0883 }
        r10 = -5;	 Catch:{ Exception -> 0x096b, all -> 0x0883 }
        if (r14 != r10) goto L_0x096a;	 Catch:{ Exception -> 0x096b, all -> 0x0883 }
        r10 = 0;	 Catch:{ Exception -> 0x096b, all -> 0x0883 }
        r22 = r11.addTrack(r1, r10);	 Catch:{ Exception -> 0x096b, all -> 0x0883 }
        r1 = r22;
        r14 = r1;
        goto L_0x093f;
    L_0x096b:
        r0 = move-exception;
        r1 = r0;
        r104 = r2;
        r107 = r11;
        r98 = r13;
        r108 = r77;
        r14 = r86;
        r110 = r88;
        goto L_0x1013;
        if (r9 >= 0) goto L_0x09b6;
        r1 = new java.lang.RuntimeException;	 Catch:{ Exception -> 0x09a6, all -> 0x0996 }
        r10 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x09a6, all -> 0x0996 }
        r10.<init>();	 Catch:{ Exception -> 0x09a6, all -> 0x0996 }
        r98 = r13;
        r13 = "unexpected result from encoder.dequeueOutputBuffer: ";	 Catch:{ Exception -> 0x09d1, all -> 0x09c1 }
        r10.append(r13);	 Catch:{ Exception -> 0x09d1, all -> 0x09c1 }
        r10.append(r9);	 Catch:{ Exception -> 0x09d1, all -> 0x09c1 }
        r10 = r10.toString();	 Catch:{ Exception -> 0x09d1, all -> 0x09c1 }
        r1.<init>(r10);	 Catch:{ Exception -> 0x09d1, all -> 0x09c1 }
        throw r1;	 Catch:{ Exception -> 0x09d1, all -> 0x09c1 }
    L_0x0996:
        r0 = move-exception;
        r98 = r13;
        r1 = r0;
        r4 = r2;
        r5 = r12;
        r3 = r49;
        r9 = r77;
        r14 = r86;
        r110 = r88;
        goto L_0x1295;
    L_0x09a6:
        r0 = move-exception;
        r98 = r13;
        r1 = r0;
        r104 = r2;
        r107 = r11;
        r108 = r77;
        r14 = r86;
        r110 = r88;
        goto L_0x1013;
        r98 = r13;
        r1 = android.os.Build.VERSION.SDK_INT;	 Catch:{ Exception -> 0x0ddd, all -> 0x0dcb }
        r10 = 21;
        if (r1 >= r10) goto L_0x09d8;
        r1 = r89[r9];	 Catch:{ Exception -> 0x09d1, all -> 0x09c1 }
        goto L_0x09dc;
    L_0x09c1:
        r0 = move-exception;
        r1 = r0;
        r4 = r2;
        r5 = r12;
        r3 = r49;
        r9 = r77;
        r14 = r86;
        r110 = r88;
        r13 = r98;
        goto L_0x1295;
    L_0x09d1:
        r0 = move-exception;
        r1 = r0;
        r104 = r2;
        r107 = r11;
        goto L_0x0973;
        r1 = r4.getOutputBuffer(r9);	 Catch:{ Exception -> 0x0ddd, all -> 0x0dcb }
        if (r1 != 0) goto L_0x09fc;
        r10 = new java.lang.RuntimeException;	 Catch:{ Exception -> 0x09d1, all -> 0x09c1 }
        r13 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x09d1, all -> 0x09c1 }
        r13.<init>();	 Catch:{ Exception -> 0x09d1, all -> 0x09c1 }
        r99 = r5;	 Catch:{ Exception -> 0x09d1, all -> 0x09c1 }
        r5 = "encoderOutputBuffer ";	 Catch:{ Exception -> 0x09d1, all -> 0x09c1 }
        r13.append(r5);	 Catch:{ Exception -> 0x09d1, all -> 0x09c1 }
        r13.append(r9);	 Catch:{ Exception -> 0x09d1, all -> 0x09c1 }
        r5 = " was null";	 Catch:{ Exception -> 0x09d1, all -> 0x09c1 }
        r13.append(r5);	 Catch:{ Exception -> 0x09d1, all -> 0x09c1 }
        r5 = r13.toString();	 Catch:{ Exception -> 0x09d1, all -> 0x09c1 }
        r10.<init>(r5);	 Catch:{ Exception -> 0x09d1, all -> 0x09c1 }
        throw r10;	 Catch:{ Exception -> 0x09d1, all -> 0x09c1 }
        r99 = r5;
        r5 = r6.size;	 Catch:{ Exception -> 0x0ddd, all -> 0x0dcb }
        r13 = 1;
        if (r5 <= r13) goto L_0x0b37;
        r5 = r6.flags;	 Catch:{ Exception -> 0x0b29, all -> 0x0b16 }
        r5 = r5 & 2;
        if (r5 != 0) goto L_0x0a1f;
        r5 = r11.writeSampleData(r14, r1, r6, r13);	 Catch:{ Exception -> 0x09d1, all -> 0x09c1 }
        if (r5 == 0) goto L_0x0a13;	 Catch:{ Exception -> 0x09d1, all -> 0x09c1 }
        r5 = 0;	 Catch:{ Exception -> 0x09d1, all -> 0x09c1 }
        r12.didWriteData(r15, r2, r5, r5);	 Catch:{ Exception -> 0x09d1, all -> 0x09c1 }
        r100 = r1;
        r104 = r2;
        r103 = r14;
        r14 = r86;
        r2 = r88;
        goto L_0x0b41;
        r5 = -5;
        if (r14 != r5) goto L_0x0b37;
        r5 = r6.size;	 Catch:{ Exception -> 0x0b29, all -> 0x0b16 }
        r5 = new byte[r5];	 Catch:{ Exception -> 0x0b29, all -> 0x0b16 }
        r13 = r6.offset;	 Catch:{ Exception -> 0x0b29, all -> 0x0b16 }
        r10 = r6.size;	 Catch:{ Exception -> 0x0b29, all -> 0x0b16 }
        r13 = r13 + r10;	 Catch:{ Exception -> 0x0b29, all -> 0x0b16 }
        r1.limit(r13);	 Catch:{ Exception -> 0x0b29, all -> 0x0b16 }
        r10 = r6.offset;	 Catch:{ Exception -> 0x0b29, all -> 0x0b16 }
        r1.position(r10);	 Catch:{ Exception -> 0x0b29, all -> 0x0b16 }
        r1.get(r5);	 Catch:{ Exception -> 0x0b29, all -> 0x0b16 }
        r10 = 0;	 Catch:{ Exception -> 0x0b29, all -> 0x0b16 }
        r13 = 0;	 Catch:{ Exception -> 0x0b29, all -> 0x0b16 }
        r100 = r1;	 Catch:{ Exception -> 0x0b29, all -> 0x0b16 }
        r1 = r6.size;	 Catch:{ Exception -> 0x0b29, all -> 0x0b16 }
        r101 = r10;
        r10 = 1;
        r1 = r1 - r10;
        if (r1 < 0) goto L_0x0aca;
        r10 = 3;
        if (r1 <= r10) goto L_0x0aca;
        r10 = r5[r1];	 Catch:{ Exception -> 0x0abc, all -> 0x0aac }
        r102 = r13;	 Catch:{ Exception -> 0x0abc, all -> 0x0aac }
        r13 = 1;	 Catch:{ Exception -> 0x0abc, all -> 0x0aac }
        if (r10 != r13) goto L_0x0a9e;	 Catch:{ Exception -> 0x0abc, all -> 0x0aac }
        r10 = r1 + -1;	 Catch:{ Exception -> 0x0abc, all -> 0x0aac }
        r10 = r5[r10];	 Catch:{ Exception -> 0x0abc, all -> 0x0aac }
        if (r10 != 0) goto L_0x0a9e;	 Catch:{ Exception -> 0x0abc, all -> 0x0aac }
        r10 = r1 + -2;	 Catch:{ Exception -> 0x0abc, all -> 0x0aac }
        r10 = r5[r10];	 Catch:{ Exception -> 0x0abc, all -> 0x0aac }
        if (r10 != 0) goto L_0x0a9e;	 Catch:{ Exception -> 0x0abc, all -> 0x0aac }
        r10 = r1 + -3;	 Catch:{ Exception -> 0x0abc, all -> 0x0aac }
        r10 = r5[r10];	 Catch:{ Exception -> 0x0abc, all -> 0x0aac }
        if (r10 != 0) goto L_0x0a9e;	 Catch:{ Exception -> 0x0abc, all -> 0x0aac }
        r10 = r1 + -3;	 Catch:{ Exception -> 0x0abc, all -> 0x0aac }
        r10 = java.nio.ByteBuffer.allocate(r10);	 Catch:{ Exception -> 0x0abc, all -> 0x0aac }
        r13 = r6.size;	 Catch:{ Exception -> 0x0abc, all -> 0x0aac }
        r22 = r1 + -3;	 Catch:{ Exception -> 0x0abc, all -> 0x0aac }
        r13 = r13 - r22;	 Catch:{ Exception -> 0x0abc, all -> 0x0aac }
        r13 = java.nio.ByteBuffer.allocate(r13);	 Catch:{ Exception -> 0x0abc, all -> 0x0aac }
        r103 = r14;
        r14 = r1 + -3;
        r104 = r2;
        r2 = 0;
        r14 = r10.put(r5, r2, r14);	 Catch:{ Exception -> 0x0a9a, all -> 0x0a8d }
        r14.position(r2);	 Catch:{ Exception -> 0x0a9a, all -> 0x0a8d }
        r2 = r1 + -3;	 Catch:{ Exception -> 0x0a9a, all -> 0x0a8d }
        r14 = r6.size;	 Catch:{ Exception -> 0x0a9a, all -> 0x0a8d }
        r22 = r1 + -3;	 Catch:{ Exception -> 0x0a9a, all -> 0x0a8d }
        r14 = r14 - r22;	 Catch:{ Exception -> 0x0a9a, all -> 0x0a8d }
        r2 = r13.put(r5, r2, r14);	 Catch:{ Exception -> 0x0a9a, all -> 0x0a8d }
        r14 = 0;	 Catch:{ Exception -> 0x0a9a, all -> 0x0a8d }
        r2.position(r14);	 Catch:{ Exception -> 0x0a9a, all -> 0x0a8d }
        goto L_0x0ad4;
    L_0x0a8d:
        r0 = move-exception;
        r1 = r0;
        r5 = r12;
        r3 = r49;
        r9 = r77;
        r14 = r86;
        r110 = r88;
        goto L_0x0bb7;
    L_0x0a9a:
        r0 = move-exception;
        r1 = r0;
        goto L_0x09d5;
        r104 = r2;
        r103 = r14;
        r1 = r1 + -1;
        r13 = r102;
        r14 = r103;
        r2 = r104;
        r10 = 1;
        goto L_0x0a40;
    L_0x0aac:
        r0 = move-exception;
        r1 = r0;
        r4 = r2;
        r5 = r12;
        r3 = r49;
        r9 = r77;
        r14 = r86;
        r110 = r88;
        r13 = r98;
        goto L_0x1295;
    L_0x0abc:
        r0 = move-exception;
        r104 = r2;
        r1 = r0;
        r107 = r11;
        r108 = r77;
        r14 = r86;
        r110 = r88;
        goto L_0x1013;
        r104 = r2;
        r102 = r13;
        r103 = r14;
        r10 = r101;
        r13 = r102;
        r1 = "video/avc";	 Catch:{ Exception -> 0x0b0a, all -> 0x0af9 }
        r14 = r86;
        r2 = r88;
        r1 = android.media.MediaFormat.createVideoFormat(r1, r2, r14);	 Catch:{ Exception -> 0x0bbd, all -> 0x0bae }
        if (r10 == 0) goto L_0x0aef;	 Catch:{ Exception -> 0x0bbd, all -> 0x0bae }
        if (r13 == 0) goto L_0x0aef;	 Catch:{ Exception -> 0x0bbd, all -> 0x0bae }
        r105 = r5;	 Catch:{ Exception -> 0x0bbd, all -> 0x0bae }
        r5 = "csd-0";	 Catch:{ Exception -> 0x0bbd, all -> 0x0bae }
        r1.setByteBuffer(r5, r10);	 Catch:{ Exception -> 0x0bbd, all -> 0x0bae }
        r5 = "csd-1";	 Catch:{ Exception -> 0x0bbd, all -> 0x0bae }
        r1.setByteBuffer(r5, r13);	 Catch:{ Exception -> 0x0bbd, all -> 0x0bae }
        goto L_0x0af1;	 Catch:{ Exception -> 0x0bbd, all -> 0x0bae }
        r105 = r5;	 Catch:{ Exception -> 0x0bbd, all -> 0x0bae }
        r5 = 0;	 Catch:{ Exception -> 0x0bbd, all -> 0x0bae }
        r22 = r11.addTrack(r1, r5);	 Catch:{ Exception -> 0x0bbd, all -> 0x0bae }
        r1 = r22;
        goto L_0x0b43;
    L_0x0af9:
        r0 = move-exception;
        r14 = r86;
        r1 = r0;
        r5 = r12;
        r3 = r49;
        r9 = r77;
        r110 = r88;
        r13 = r98;
        r4 = r104;
        goto L_0x1295;
    L_0x0b0a:
        r0 = move-exception;
        r14 = r86;
        r1 = r0;
        r107 = r11;
        r108 = r77;
        r110 = r88;
        goto L_0x1013;
    L_0x0b16:
        r0 = move-exception;
        r104 = r2;
        r14 = r86;
        r1 = r0;
        r5 = r12;
        r3 = r49;
        r9 = r77;
        r110 = r88;
        r13 = r98;
        r4 = r104;
        goto L_0x1295;
    L_0x0b29:
        r0 = move-exception;
        r104 = r2;
        r14 = r86;
        r1 = r0;
        r107 = r11;
        r108 = r77;
        r110 = r88;
        goto L_0x1013;
        r100 = r1;
        r104 = r2;
        r103 = r14;
        r14 = r86;
        r2 = r88;
        r1 = r103;
        r5 = r6.flags;	 Catch:{ Exception -> 0x0d9f, all -> 0x0d8e }
        r5 = r5 & 4;	 Catch:{ Exception -> 0x0d9f, all -> 0x0d8e }
        if (r5 == 0) goto L_0x0b4b;	 Catch:{ Exception -> 0x0d9f, all -> 0x0d8e }
        r5 = 1;	 Catch:{ Exception -> 0x0d9f, all -> 0x0d8e }
        goto L_0x0b4c;	 Catch:{ Exception -> 0x0d9f, all -> 0x0d8e }
        r5 = 0;	 Catch:{ Exception -> 0x0d9f, all -> 0x0d8e }
        r54 = r5;	 Catch:{ Exception -> 0x0d9f, all -> 0x0d8e }
        r5 = 0;	 Catch:{ Exception -> 0x0d9f, all -> 0x0d8e }
        r4.releaseOutputBuffer(r9, r5);	 Catch:{ Exception -> 0x0d9f, all -> 0x0d8e }
        r5 = -1;	 Catch:{ Exception -> 0x0d9f, all -> 0x0d8e }
        if (r9 == r5) goto L_0x0b69;	 Catch:{ Exception -> 0x0d9f, all -> 0x0d8e }
        r88 = r2;	 Catch:{ Exception -> 0x0d9f, all -> 0x0d8e }
        r86 = r14;	 Catch:{ Exception -> 0x0d9f, all -> 0x0d8e }
        r10 = r95;	 Catch:{ Exception -> 0x0d9f, all -> 0x0d8e }
        r9 = r96;	 Catch:{ Exception -> 0x0d9f, all -> 0x0d8e }
        r13 = r98;	 Catch:{ Exception -> 0x0d9f, all -> 0x0d8e }
        r5 = r99;	 Catch:{ Exception -> 0x0d9f, all -> 0x0d8e }
        r2 = r104;	 Catch:{ Exception -> 0x0d9f, all -> 0x0d8e }
        r14 = r1;	 Catch:{ Exception -> 0x0d9f, all -> 0x0d8e }
        r1 = r97;	 Catch:{ Exception -> 0x0d9f, all -> 0x0d8e }
        goto L_0x08ec;	 Catch:{ Exception -> 0x0d9f, all -> 0x0d8e }
        if (r80 != 0) goto L_0x0da9;	 Catch:{ Exception -> 0x0d9f, all -> 0x0d8e }
        r106 = r9;	 Catch:{ Exception -> 0x0d9f, all -> 0x0d8e }
        r9 = 2500; // 0x9c4 float:3.503E-42 double:1.235E-320;	 Catch:{ Exception -> 0x0d9f, all -> 0x0d8e }
        r13 = r8.dequeueOutputBuffer(r6, r9);	 Catch:{ Exception -> 0x0d9f, all -> 0x0d8e }
        r9 = r13;
        if (r9 != r5) goto L_0x0b81;
        r10 = 0;
        r111 = r1;
        r110 = r2;
        r107 = r11;
        r108 = r77;
        goto L_0x0db3;
        r10 = -3;
        if (r9 != r10) goto L_0x0b8e;
        r111 = r1;
        r110 = r2;
        r107 = r11;
        r108 = r77;
        goto L_0x0db1;
        r10 = -2;
        if (r9 != r10) goto L_0x0bc7;
        r10 = r8.getOutputFormat();	 Catch:{ Exception -> 0x0bbd, all -> 0x0bae }
        r13 = org.telegram.messenger.BuildVars.LOGS_ENABLED;	 Catch:{ Exception -> 0x0bbd, all -> 0x0bae }
        if (r13 == 0) goto L_0x0bad;	 Catch:{ Exception -> 0x0bbd, all -> 0x0bae }
        r13 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x0bbd, all -> 0x0bae }
        r13.<init>();	 Catch:{ Exception -> 0x0bbd, all -> 0x0bae }
        r5 = "newFormat = ";	 Catch:{ Exception -> 0x0bbd, all -> 0x0bae }
        r13.append(r5);	 Catch:{ Exception -> 0x0bbd, all -> 0x0bae }
        r13.append(r10);	 Catch:{ Exception -> 0x0bbd, all -> 0x0bae }
        r5 = r13.toString();	 Catch:{ Exception -> 0x0bbd, all -> 0x0bae }
        org.telegram.messenger.FileLog.d(r5);	 Catch:{ Exception -> 0x0bbd, all -> 0x0bae }
        goto L_0x0b84;	 Catch:{ Exception -> 0x0bbd, all -> 0x0bae }
    L_0x0bae:
        r0 = move-exception;	 Catch:{ Exception -> 0x0bbd, all -> 0x0bae }
        r1 = r0;	 Catch:{ Exception -> 0x0bbd, all -> 0x0bae }
        r110 = r2;	 Catch:{ Exception -> 0x0bbd, all -> 0x0bae }
        r5 = r12;	 Catch:{ Exception -> 0x0bbd, all -> 0x0bae }
        r3 = r49;	 Catch:{ Exception -> 0x0bbd, all -> 0x0bae }
        r9 = r77;	 Catch:{ Exception -> 0x0bbd, all -> 0x0bae }
        r13 = r98;	 Catch:{ Exception -> 0x0bbd, all -> 0x0bae }
        r4 = r104;	 Catch:{ Exception -> 0x0bbd, all -> 0x0bae }
        goto L_0x1295;	 Catch:{ Exception -> 0x0bbd, all -> 0x0bae }
    L_0x0bbd:
        r0 = move-exception;	 Catch:{ Exception -> 0x0bbd, all -> 0x0bae }
        r1 = r0;	 Catch:{ Exception -> 0x0bbd, all -> 0x0bae }
        r110 = r2;	 Catch:{ Exception -> 0x0bbd, all -> 0x0bae }
        r107 = r11;	 Catch:{ Exception -> 0x0bbd, all -> 0x0bae }
        r108 = r77;	 Catch:{ Exception -> 0x0bbd, all -> 0x0bae }
        goto L_0x1013;	 Catch:{ Exception -> 0x0bbd, all -> 0x0bae }
        if (r9 >= 0) goto L_0x0be0;	 Catch:{ Exception -> 0x0bbd, all -> 0x0bae }
        r5 = new java.lang.RuntimeException;	 Catch:{ Exception -> 0x0bbd, all -> 0x0bae }
        r10 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x0bbd, all -> 0x0bae }
        r10.<init>();	 Catch:{ Exception -> 0x0bbd, all -> 0x0bae }
        r13 = "unexpected result from decoder.dequeueOutputBuffer: ";	 Catch:{ Exception -> 0x0bbd, all -> 0x0bae }
        r10.append(r13);	 Catch:{ Exception -> 0x0bbd, all -> 0x0bae }
        r10.append(r9);	 Catch:{ Exception -> 0x0bbd, all -> 0x0bae }
        r10 = r10.toString();	 Catch:{ Exception -> 0x0bbd, all -> 0x0bae }
        r5.<init>(r10);	 Catch:{ Exception -> 0x0bbd, all -> 0x0bae }
        throw r5;	 Catch:{ Exception -> 0x0bbd, all -> 0x0bae }
        r5 = android.os.Build.VERSION.SDK_INT;	 Catch:{ Exception -> 0x0d9f, all -> 0x0d8e }
        r10 = 18;
        if (r5 < r10) goto L_0x0bf0;
        r5 = r6.size;	 Catch:{ Exception -> 0x0bbd, all -> 0x0bae }
        if (r5 == 0) goto L_0x0bec;
        r5 = 1;
        goto L_0x0bed;
        r5 = 0;
        r107 = r11;
        goto L_0x0c1a;
        r5 = r6.size;	 Catch:{ Exception -> 0x0d9f, all -> 0x0d8e }
        if (r5 != 0) goto L_0x0c17;
        r107 = r11;
        r10 = r6.presentationTimeUs;	 Catch:{ Exception -> 0x0c12, all -> 0x0c01 }
        r22 = 0;	 Catch:{ Exception -> 0x0c12, all -> 0x0c01 }
        r5 = (r10 > r22 ? 1 : (r10 == r22 ? 0 : -1));	 Catch:{ Exception -> 0x0c12, all -> 0x0c01 }
        if (r5 == 0) goto L_0x0bff;	 Catch:{ Exception -> 0x0c12, all -> 0x0c01 }
        goto L_0x0c19;	 Catch:{ Exception -> 0x0c12, all -> 0x0c01 }
        r5 = 0;	 Catch:{ Exception -> 0x0c12, all -> 0x0c01 }
        goto L_0x0c1a;	 Catch:{ Exception -> 0x0c12, all -> 0x0c01 }
    L_0x0c01:
        r0 = move-exception;	 Catch:{ Exception -> 0x0c12, all -> 0x0c01 }
        r1 = r0;	 Catch:{ Exception -> 0x0c12, all -> 0x0c01 }
        r110 = r2;	 Catch:{ Exception -> 0x0c12, all -> 0x0c01 }
        r5 = r12;	 Catch:{ Exception -> 0x0c12, all -> 0x0c01 }
        r3 = r49;	 Catch:{ Exception -> 0x0c12, all -> 0x0c01 }
        r9 = r77;	 Catch:{ Exception -> 0x0c12, all -> 0x0c01 }
        r13 = r98;	 Catch:{ Exception -> 0x0c12, all -> 0x0c01 }
        r4 = r104;	 Catch:{ Exception -> 0x0c12, all -> 0x0c01 }
        r11 = r107;	 Catch:{ Exception -> 0x0c12, all -> 0x0c01 }
        goto L_0x1295;	 Catch:{ Exception -> 0x0c12, all -> 0x0c01 }
    L_0x0c12:
        r0 = move-exception;	 Catch:{ Exception -> 0x0c12, all -> 0x0c01 }
        r1 = r0;	 Catch:{ Exception -> 0x0c12, all -> 0x0c01 }
        r110 = r2;	 Catch:{ Exception -> 0x0c12, all -> 0x0c01 }
        goto L_0x0bc3;	 Catch:{ Exception -> 0x0c12, all -> 0x0c01 }
        r107 = r11;	 Catch:{ Exception -> 0x0c12, all -> 0x0c01 }
        r5 = 1;	 Catch:{ Exception -> 0x0c12, all -> 0x0c01 }
        r10 = 0;	 Catch:{ Exception -> 0x0c12, all -> 0x0c01 }
        r13 = (r19 > r10 ? 1 : (r19 == r10 ? 0 : -1));	 Catch:{ Exception -> 0x0c12, all -> 0x0c01 }
        if (r13 <= 0) goto L_0x0c31;	 Catch:{ Exception -> 0x0c12, all -> 0x0c01 }
        r10 = r6.presentationTimeUs;	 Catch:{ Exception -> 0x0c12, all -> 0x0c01 }
        r13 = (r10 > r19 ? 1 : (r10 == r19 ? 0 : -1));	 Catch:{ Exception -> 0x0c12, all -> 0x0c01 }
        if (r13 < 0) goto L_0x0c31;	 Catch:{ Exception -> 0x0c12, all -> 0x0c01 }
        r60 = 1;	 Catch:{ Exception -> 0x0c12, all -> 0x0c01 }
        r80 = 1;	 Catch:{ Exception -> 0x0c12, all -> 0x0c01 }
        r5 = 0;	 Catch:{ Exception -> 0x0c12, all -> 0x0c01 }
        r10 = r6.flags;	 Catch:{ Exception -> 0x0c12, all -> 0x0c01 }
        r10 = r10 | 4;	 Catch:{ Exception -> 0x0c12, all -> 0x0c01 }
        r6.flags = r10;	 Catch:{ Exception -> 0x0c12, all -> 0x0c01 }
        r10 = 0;
        r13 = (r77 > r10 ? 1 : (r77 == r10 ? 0 : -1));
        if (r13 <= 0) goto L_0x0c84;
        r22 = -1;
        r13 = (r57 > r22 ? 1 : (r57 == r22 ? 0 : -1));
        if (r13 != 0) goto L_0x0c84;
        r10 = r6.presentationTimeUs;	 Catch:{ Exception -> 0x0cab, all -> 0x0c9a }
        r13 = (r10 > r77 ? 1 : (r10 == r77 ? 0 : -1));	 Catch:{ Exception -> 0x0cab, all -> 0x0c9a }
        if (r13 >= 0) goto L_0x0c87;	 Catch:{ Exception -> 0x0cab, all -> 0x0c9a }
        r5 = 0;	 Catch:{ Exception -> 0x0cab, all -> 0x0c9a }
        r10 = org.telegram.messenger.BuildVars.LOGS_ENABLED;	 Catch:{ Exception -> 0x0cab, all -> 0x0c9a }
        if (r10 == 0) goto L_0x0c84;	 Catch:{ Exception -> 0x0cab, all -> 0x0c9a }
        r10 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x0cab, all -> 0x0c9a }
        r10.<init>();	 Catch:{ Exception -> 0x0cab, all -> 0x0c9a }
        r11 = "drop frame startTime = ";	 Catch:{ Exception -> 0x0cab, all -> 0x0c9a }
        r10.append(r11);	 Catch:{ Exception -> 0x0cab, all -> 0x0c9a }
        r11 = r77;
        r10.append(r11);	 Catch:{ Exception -> 0x0c7c, all -> 0x0c6b }
        r13 = " present time = ";	 Catch:{ Exception -> 0x0c7c, all -> 0x0c6b }
        r10.append(r13);	 Catch:{ Exception -> 0x0c7c, all -> 0x0c6b }
        r108 = r11;
        r11 = r6.presentationTimeUs;	 Catch:{ Exception -> 0x0c94, all -> 0x0c8e }
        r10.append(r11);	 Catch:{ Exception -> 0x0c94, all -> 0x0c8e }
        r10 = r10.toString();	 Catch:{ Exception -> 0x0c94, all -> 0x0c8e }
        org.telegram.messenger.FileLog.d(r10);	 Catch:{ Exception -> 0x0c94, all -> 0x0c8e }
        goto L_0x0cb3;	 Catch:{ Exception -> 0x0c94, all -> 0x0c8e }
    L_0x0c6b:
        r0 = move-exception;	 Catch:{ Exception -> 0x0c94, all -> 0x0c8e }
        r1 = r0;	 Catch:{ Exception -> 0x0c94, all -> 0x0c8e }
        r110 = r2;	 Catch:{ Exception -> 0x0c94, all -> 0x0c8e }
        r9 = r11;	 Catch:{ Exception -> 0x0c94, all -> 0x0c8e }
        r3 = r49;	 Catch:{ Exception -> 0x0c94, all -> 0x0c8e }
        r13 = r98;	 Catch:{ Exception -> 0x0c94, all -> 0x0c8e }
        r4 = r104;	 Catch:{ Exception -> 0x0c94, all -> 0x0c8e }
        r11 = r107;	 Catch:{ Exception -> 0x0c94, all -> 0x0c8e }
        r5 = r115;	 Catch:{ Exception -> 0x0c94, all -> 0x0c8e }
        goto L_0x1295;	 Catch:{ Exception -> 0x0c94, all -> 0x0c8e }
    L_0x0c7c:
        r0 = move-exception;	 Catch:{ Exception -> 0x0c94, all -> 0x0c8e }
        r108 = r11;	 Catch:{ Exception -> 0x0c94, all -> 0x0c8e }
        r1 = r0;	 Catch:{ Exception -> 0x0c94, all -> 0x0c8e }
        r110 = r2;	 Catch:{ Exception -> 0x0c94, all -> 0x0c8e }
        goto L_0x1013;	 Catch:{ Exception -> 0x0c94, all -> 0x0c8e }
        r108 = r77;	 Catch:{ Exception -> 0x0c94, all -> 0x0c8e }
        goto L_0x0cb3;	 Catch:{ Exception -> 0x0c94, all -> 0x0c8e }
        r108 = r77;	 Catch:{ Exception -> 0x0c94, all -> 0x0c8e }
        r10 = r6.presentationTimeUs;	 Catch:{ Exception -> 0x0c94, all -> 0x0c8e }
        r57 = r10;
        goto L_0x0cb3;
    L_0x0c8e:
        r0 = move-exception;
        r1 = r0;
        r110 = r2;
        goto L_0x0d65;
    L_0x0c94:
        r0 = move-exception;
        r1 = r0;
        r110 = r2;
        goto L_0x1013;
    L_0x0c9a:
        r0 = move-exception;
        r1 = r0;
        r110 = r2;
        r5 = r12;
        r3 = r49;
        r9 = r77;
        r13 = r98;
        r4 = r104;
        r11 = r107;
        goto L_0x1295;
    L_0x0cab:
        r0 = move-exception;
        r108 = r77;
        r1 = r0;
        r110 = r2;
        goto L_0x1013;
        r8.releaseOutputBuffer(r9, r5);	 Catch:{ Exception -> 0x0d88, all -> 0x0d76 }
        if (r5 == 0) goto L_0x0d29;
        r10 = 0;
        r11 = r10;
        r3.awaitNewImage();	 Catch:{ Exception -> 0x0cbe, all -> 0x0c8e }
        goto L_0x0cc4;
    L_0x0cbe:
        r0 = move-exception;
        r10 = r0;
        r11 = 1;
        org.telegram.messenger.FileLog.e(r10);	 Catch:{ Exception -> 0x0d88, all -> 0x0d76 }
        if (r11 != 0) goto L_0x0d29;	 Catch:{ Exception -> 0x0d88, all -> 0x0d76 }
        r10 = android.os.Build.VERSION.SDK_INT;	 Catch:{ Exception -> 0x0d88, all -> 0x0d76 }
        r12 = 18;
        if (r10 < r12) goto L_0x0ce1;
        r10 = 0;
        r3.drawImage(r10);	 Catch:{ Exception -> 0x0c94, all -> 0x0c8e }
        r12 = r6.presentationTimeUs;	 Catch:{ Exception -> 0x0c94, all -> 0x0c8e }
        r22 = 1000; // 0x3e8 float:1.401E-42 double:4.94E-321;	 Catch:{ Exception -> 0x0c94, all -> 0x0c8e }
        r12 = r12 * r22;	 Catch:{ Exception -> 0x0c94, all -> 0x0c8e }
        r7.setPresentationTime(r12);	 Catch:{ Exception -> 0x0c94, all -> 0x0c8e }
        r7.swapBuffers();	 Catch:{ Exception -> 0x0c94, all -> 0x0c8e }
        r111 = r1;
        r110 = r2;
        goto L_0x0d2d;
        r12 = 2500; // 0x9c4 float:3.503E-42 double:1.235E-320;
        r10 = r4.dequeueInputBuffer(r12);	 Catch:{ Exception -> 0x0d88, all -> 0x0d76 }
        if (r10 < 0) goto L_0x0d1b;	 Catch:{ Exception -> 0x0d88, all -> 0x0d76 }
        r12 = 1;	 Catch:{ Exception -> 0x0d88, all -> 0x0d76 }
        r3.drawImage(r12);	 Catch:{ Exception -> 0x0d88, all -> 0x0d76 }
        r22 = r3.getFrame();	 Catch:{ Exception -> 0x0d88, all -> 0x0d76 }
        r12 = r99[r10];	 Catch:{ Exception -> 0x0d88, all -> 0x0d76 }
        r12.clear();	 Catch:{ Exception -> 0x0d88, all -> 0x0d76 }
        r23 = r12;	 Catch:{ Exception -> 0x0d88, all -> 0x0d76 }
        r24 = r83;	 Catch:{ Exception -> 0x0d88, all -> 0x0d76 }
        r25 = r2;	 Catch:{ Exception -> 0x0d88, all -> 0x0d76 }
        r26 = r14;	 Catch:{ Exception -> 0x0d88, all -> 0x0d76 }
        r27 = r59;	 Catch:{ Exception -> 0x0d88, all -> 0x0d76 }
        r28 = r73;	 Catch:{ Exception -> 0x0d88, all -> 0x0d76 }
        org.telegram.messenger.Utilities.convertVideoFrame(r22, r23, r24, r25, r26, r27, r28);	 Catch:{ Exception -> 0x0d88, all -> 0x0d76 }
        r63 = 0;
        r111 = r1;
        r110 = r2;
        r1 = r6.presentationTimeUs;	 Catch:{ Exception -> 0x0d73, all -> 0x0d63 }
        r67 = 0;	 Catch:{ Exception -> 0x0d73, all -> 0x0d63 }
        r61 = r4;	 Catch:{ Exception -> 0x0d73, all -> 0x0d63 }
        r62 = r10;	 Catch:{ Exception -> 0x0d73, all -> 0x0d63 }
        r64 = r75;	 Catch:{ Exception -> 0x0d73, all -> 0x0d63 }
        r65 = r1;	 Catch:{ Exception -> 0x0d73, all -> 0x0d63 }
        r61.queueInputBuffer(r62, r63, r64, r65, r67);	 Catch:{ Exception -> 0x0d73, all -> 0x0d63 }
        goto L_0x0d2d;	 Catch:{ Exception -> 0x0d73, all -> 0x0d63 }
        r111 = r1;	 Catch:{ Exception -> 0x0d73, all -> 0x0d63 }
        r110 = r2;	 Catch:{ Exception -> 0x0d73, all -> 0x0d63 }
        r1 = org.telegram.messenger.BuildVars.LOGS_ENABLED;	 Catch:{ Exception -> 0x0d73, all -> 0x0d63 }
        if (r1 == 0) goto L_0x0d2d;	 Catch:{ Exception -> 0x0d73, all -> 0x0d63 }
        r1 = "input buffer not available";	 Catch:{ Exception -> 0x0d73, all -> 0x0d63 }
        org.telegram.messenger.FileLog.d(r1);	 Catch:{ Exception -> 0x0d73, all -> 0x0d63 }
        goto L_0x0d2d;	 Catch:{ Exception -> 0x0d73, all -> 0x0d63 }
        r111 = r1;	 Catch:{ Exception -> 0x0d73, all -> 0x0d63 }
        r110 = r2;	 Catch:{ Exception -> 0x0d73, all -> 0x0d63 }
        r1 = r6.flags;	 Catch:{ Exception -> 0x0d73, all -> 0x0d63 }
        r1 = r1 & 4;	 Catch:{ Exception -> 0x0d73, all -> 0x0d63 }
        if (r1 == 0) goto L_0x0db1;	 Catch:{ Exception -> 0x0d73, all -> 0x0d63 }
        r1 = 0;	 Catch:{ Exception -> 0x0d73, all -> 0x0d63 }
        r2 = org.telegram.messenger.BuildVars.LOGS_ENABLED;	 Catch:{ Exception -> 0x0d73, all -> 0x0d63 }
        if (r2 == 0) goto L_0x0d3d;	 Catch:{ Exception -> 0x0d73, all -> 0x0d63 }
        r2 = "decoder stream end";	 Catch:{ Exception -> 0x0d73, all -> 0x0d63 }
        org.telegram.messenger.FileLog.d(r2);	 Catch:{ Exception -> 0x0d73, all -> 0x0d63 }
        r2 = android.os.Build.VERSION.SDK_INT;	 Catch:{ Exception -> 0x0d73, all -> 0x0d63 }
        r10 = 18;	 Catch:{ Exception -> 0x0d73, all -> 0x0d63 }
        if (r2 < r10) goto L_0x0d47;	 Catch:{ Exception -> 0x0d73, all -> 0x0d63 }
        r4.signalEndOfInputStream();	 Catch:{ Exception -> 0x0d73, all -> 0x0d63 }
        goto L_0x0d60;	 Catch:{ Exception -> 0x0d73, all -> 0x0d63 }
        r11 = 2500; // 0x9c4 float:3.503E-42 double:1.235E-320;	 Catch:{ Exception -> 0x0d73, all -> 0x0d63 }
        r2 = r4.dequeueInputBuffer(r11);	 Catch:{ Exception -> 0x0d73, all -> 0x0d63 }
        if (r2 < 0) goto L_0x0d60;	 Catch:{ Exception -> 0x0d73, all -> 0x0d63 }
        r63 = 0;	 Catch:{ Exception -> 0x0d73, all -> 0x0d63 }
        r64 = 1;	 Catch:{ Exception -> 0x0d73, all -> 0x0d63 }
        r10 = r6.presentationTimeUs;	 Catch:{ Exception -> 0x0d73, all -> 0x0d63 }
        r67 = 4;	 Catch:{ Exception -> 0x0d73, all -> 0x0d63 }
        r61 = r4;	 Catch:{ Exception -> 0x0d73, all -> 0x0d63 }
        r62 = r2;	 Catch:{ Exception -> 0x0d73, all -> 0x0d63 }
        r65 = r10;	 Catch:{ Exception -> 0x0d73, all -> 0x0d63 }
        r61.queueInputBuffer(r62, r63, r64, r65, r67);	 Catch:{ Exception -> 0x0d73, all -> 0x0d63 }
        r10 = r1;
        goto L_0x0db3;
    L_0x0d63:
        r0 = move-exception;
        r1 = r0;
        r3 = r49;
        r13 = r98;
        r4 = r104;
        r11 = r107;
        r9 = r108;
        r5 = r115;
        goto L_0x1295;
    L_0x0d73:
        r0 = move-exception;
        goto L_0x03d0;
    L_0x0d76:
        r0 = move-exception;
        r110 = r2;
        r1 = r0;
        r3 = r49;
        r13 = r98;
        r4 = r104;
        r11 = r107;
        r9 = r108;
        r5 = r115;
        goto L_0x1295;
    L_0x0d88:
        r0 = move-exception;
        r110 = r2;
        r1 = r0;
        goto L_0x1013;
    L_0x0d8e:
        r0 = move-exception;
        r110 = r2;
        r107 = r11;
        r1 = r0;
        r5 = r12;
        r3 = r49;
        r9 = r77;
        r13 = r98;
        r4 = r104;
        goto L_0x1295;
    L_0x0d9f:
        r0 = move-exception;
        r110 = r2;
        r107 = r11;
        r108 = r77;
        r1 = r0;
        goto L_0x1013;
        r111 = r1;
        r110 = r2;
        r107 = r11;
        r108 = r77;
        r10 = r95;
        r86 = r14;
        r9 = r96;
        r1 = r97;
        r13 = r98;
        r5 = r99;
        r2 = r104;
        r11 = r107;
        r77 = r108;
        r88 = r110;
        r14 = r111;
        r12 = r115;
        goto L_0x08ec;
    L_0x0dcb:
        r0 = move-exception;
        r107 = r11;
        r14 = r86;
        r110 = r88;
        r1 = r0;
        r4 = r2;
        r5 = r12;
        r3 = r49;
        r9 = r77;
        r13 = r98;
        goto L_0x1295;
    L_0x0ddd:
        r0 = move-exception;
        r104 = r2;
        r107 = r11;
        r108 = r77;
        r14 = r86;
        r110 = r88;
        r1 = r0;
        goto L_0x1013;
    L_0x0deb:
        r0 = move-exception;
        r107 = r11;
        r98 = r13;
        r14 = r86;
        r110 = r88;
        r1 = r0;
        r4 = r2;
        r5 = r12;
        r3 = r49;
        r9 = r77;
        goto L_0x1295;
    L_0x0dfd:
        r0 = move-exception;
        r97 = r1;
        r104 = r2;
        r107 = r11;
        r98 = r13;
        r108 = r77;
        r14 = r86;
        r110 = r88;
        r1 = r0;
        goto L_0x1013;
        r97 = r1;
        r91 = r2;
        r98 = r13;
        r110 = r14;
        r6 = r44;
        r104 = r45;
        r107 = r74;
        r108 = r77;
        r15 = r116;
        r14 = r9;
        goto L_0x1019;
    L_0x0e24:
        r0 = move-exception;
        r98 = r13;
        r110 = r14;
        r107 = r74;
        r15 = r116;
        r14 = r9;
        r1 = r0;
        r5 = r12;
        r4 = r45;
        r3 = r49;
        r9 = r77;
        r11 = r107;
        goto L_0x1295;
    L_0x0e3a:
        r0 = move-exception;
        r97 = r1;
        r91 = r2;
        r98 = r13;
        r110 = r14;
        r6 = r44;
        r104 = r45;
        r107 = r74;
        r108 = r77;
        r15 = r116;
        r14 = r9;
        r1 = r0;
        goto L_0x1013;
    L_0x0e51:
        r0 = move-exception;
        r98 = r13;
        r110 = r14;
        r82 = r15;
        r107 = r74;
        r15 = r116;
        r14 = r9;
        r1 = r0;
        r5 = r12;
        r4 = r45;
        r3 = r49;
        r9 = r77;
        r11 = r107;
        goto L_0x1295;
    L_0x0e69:
        r0 = move-exception;
        r97 = r1;
        r91 = r2;
        r98 = r13;
        r110 = r14;
        r82 = r15;
        r6 = r44;
        r104 = r45;
        r107 = r74;
        r108 = r77;
        r15 = r116;
        r14 = r9;
        r1 = r0;
        goto L_0x1013;
    L_0x0e82:
        r0 = move-exception;
        r81 = r11;
        r98 = r13;
        r110 = r14;
        r82 = r15;
        r107 = r74;
        r15 = r116;
        r14 = r9;
        r1 = r0;
        r5 = r12;
        r4 = r45;
        r3 = r49;
        r9 = r77;
        r11 = r107;
        goto L_0x1295;
    L_0x0e9c:
        r0 = move-exception;
        r97 = r1;
        r91 = r2;
        r81 = r11;
        r98 = r13;
        r110 = r14;
        r82 = r15;
        r6 = r44;
        r104 = r45;
        r107 = r74;
        r108 = r77;
        r15 = r116;
        r14 = r9;
        r1 = r0;
        r3 = r56;
        goto L_0x1013;
    L_0x0eb9:
        r0 = move-exception;
        r97 = r1;
        r91 = r2;
        r98 = r13;
        r110 = r14;
        r82 = r15;
        r6 = r44;
        r104 = r45;
        r81 = r46;
        r107 = r74;
        r108 = r77;
        r15 = r116;
        r14 = r9;
        r1 = r0;
        goto L_0x0f46;
    L_0x0ed4:
        r0 = move-exception;
        r97 = r1;
        r91 = r2;
        r98 = r13;
        r110 = r14;
        r82 = r15;
        r6 = r44;
        r104 = r45;
        r81 = r46;
        r107 = r74;
        r108 = r77;
        r15 = r116;
        r14 = r9;
        r1 = r0;
        r8 = r50;
        goto L_0x0f46;
    L_0x0ef1:
        r0 = move-exception;
        r97 = r1;
        r91 = r2;
        r98 = r13;
        r110 = r14;
        r82 = r15;
        r6 = r44;
        r104 = r45;
        r81 = r46;
        r107 = r74;
        r108 = r77;
        r15 = r116;
        r14 = r9;
        r1 = r0;
        r8 = r50;
        goto L_0x0f44;
    L_0x0f0d:
        r0 = move-exception;
        r98 = r13;
        r110 = r14;
        r82 = r15;
        r81 = r46;
        r107 = r74;
        r15 = r116;
        r14 = r9;
        r1 = r0;
        r5 = r12;
        r4 = r45;
        r3 = r49;
        r9 = r77;
        r11 = r107;
        goto L_0x1295;
    L_0x0f27:
        r0 = move-exception;
        r97 = r1;
        r91 = r2;
        r98 = r13;
        r110 = r14;
        r82 = r15;
        r6 = r44;
        r104 = r45;
        r81 = r46;
        r107 = r74;
        r108 = r77;
        r15 = r116;
        r14 = r9;
        r1 = r0;
        r8 = r50;
        r4 = r51;
        r7 = r52;
        r3 = r56;
        goto L_0x1013;
    L_0x0f4a:
        r0 = move-exception;
        r98 = r13;
        r110 = r14;
        r82 = r15;
        r107 = r41;
        r81 = r46;
        r15 = r116;
        r14 = r9;
        r1 = r0;
        r5 = r12;
        r4 = r45;
        r9 = r47;
        r3 = r49;
        r11 = r107;
        goto L_0x1295;
    L_0x0f64:
        r0 = move-exception;
        r97 = r1;
        r91 = r2;
        r98 = r13;
        r110 = r14;
        r82 = r15;
        r107 = r41;
        r6 = r44;
        r104 = r45;
        r81 = r46;
        r108 = r47;
        r15 = r116;
        r14 = r9;
        r1 = r0;
        r8 = r50;
        r4 = r51;
        r7 = r52;
        r3 = r56;
        goto L_0x1013;
    L_0x0f87:
        r0 = move-exception;
        r97 = r1;
        r91 = r2;
        r98 = r13;
        r110 = r14;
        r82 = r15;
        r107 = r41;
        r14 = r42;
        r6 = r44;
        r104 = r45;
        r81 = r46;
        r108 = r47;
        r15 = r116;
        r1 = r0;
        r8 = r50;
        r4 = r51;
        r7 = r52;
        r3 = r56;
        goto L_0x1013;
    L_0x0fab:
        r0 = move-exception;
        r97 = r1;
        r91 = r2;
        r52 = r5;
        r56 = r7;
        r98 = r13;
        r110 = r14;
        r82 = r15;
        r107 = r41;
        r14 = r42;
        r6 = r44;
        r104 = r45;
        r81 = r46;
        r108 = r47;
        r15 = r116;
        r1 = r0;
        r8 = r50;
        r4 = r51;
        r7 = r52;
        r3 = r56;
        goto L_0x1013;
    L_0x0fd2:
        r0 = move-exception;
        r98 = r13;
        r110 = r14;
        r82 = r15;
        r107 = r41;
        r14 = r42;
        r81 = r46;
        r15 = r116;
        r1 = r0;
        r5 = r12;
        r4 = r45;
        r9 = r47;
        r3 = r49;
        r11 = r107;
        goto L_0x1295;
    L_0x0fed:
        r0 = move-exception;
        r97 = r1;
        r91 = r2;
        r51 = r4;
        r52 = r5;
        r56 = r7;
        r98 = r13;
        r110 = r14;
        r82 = r15;
        r107 = r41;
        r14 = r42;
        r6 = r44;
        r104 = r45;
        r81 = r46;
        r108 = r47;
        r15 = r116;
        r1 = r0;
        r8 = r50;
        r7 = r52;
        r3 = r56;
        org.telegram.messenger.FileLog.e(r1);	 Catch:{ Exception -> 0x1097, all -> 0x1086 }
        r1 = 1;
        r16 = r1;
        r2 = r97;
        r1 = r98;
        r1.unselectTrack(r2);	 Catch:{ Exception -> 0x107b, all -> 0x106c }
        if (r3 == 0) goto L_0x1040;
        r3.release();	 Catch:{ Exception -> 0x1035, all -> 0x1026 }
        goto L_0x1040;	 Catch:{ Exception -> 0x1035, all -> 0x1026 }
    L_0x1026:
        r0 = move-exception;	 Catch:{ Exception -> 0x1035, all -> 0x1026 }
        r13 = r1;	 Catch:{ Exception -> 0x1035, all -> 0x1026 }
        r3 = r49;	 Catch:{ Exception -> 0x1035, all -> 0x1026 }
        r4 = r104;	 Catch:{ Exception -> 0x1035, all -> 0x1026 }
        r11 = r107;	 Catch:{ Exception -> 0x1035, all -> 0x1026 }
        r9 = r108;	 Catch:{ Exception -> 0x1035, all -> 0x1026 }
        r5 = r115;	 Catch:{ Exception -> 0x1035, all -> 0x1026 }
        r1 = r0;	 Catch:{ Exception -> 0x1035, all -> 0x1026 }
        goto L_0x1295;	 Catch:{ Exception -> 0x1035, all -> 0x1026 }
    L_0x1035:
        r0 = move-exception;	 Catch:{ Exception -> 0x1035, all -> 0x1026 }
        r7 = r1;	 Catch:{ Exception -> 0x1035, all -> 0x1026 }
        r2 = r107;	 Catch:{ Exception -> 0x1035, all -> 0x1026 }
        r9 = r108;	 Catch:{ Exception -> 0x1035, all -> 0x1026 }
        r5 = r115;	 Catch:{ Exception -> 0x1035, all -> 0x1026 }
        r1 = r0;	 Catch:{ Exception -> 0x1035, all -> 0x1026 }
        goto L_0x1242;	 Catch:{ Exception -> 0x1035, all -> 0x1026 }
        if (r7 == 0) goto L_0x1045;	 Catch:{ Exception -> 0x1035, all -> 0x1026 }
        r7.release();	 Catch:{ Exception -> 0x1035, all -> 0x1026 }
        if (r8 == 0) goto L_0x104d;	 Catch:{ Exception -> 0x1035, all -> 0x1026 }
        r8.stop();	 Catch:{ Exception -> 0x1035, all -> 0x1026 }
        r8.release();	 Catch:{ Exception -> 0x1035, all -> 0x1026 }
        if (r4 == 0) goto L_0x1055;	 Catch:{ Exception -> 0x1035, all -> 0x1026 }
        r4.stop();	 Catch:{ Exception -> 0x1035, all -> 0x1026 }
        r4.release();	 Catch:{ Exception -> 0x1035, all -> 0x1026 }
        r9 = r108;
        r5 = r115;
        r115.checkConversionCanceled();	 Catch:{ Exception -> 0x1067, all -> 0x105e }
        goto L_0x10b8;
    L_0x105e:
        r0 = move-exception;
        r13 = r1;
        r3 = r49;
        r4 = r104;
        r11 = r107;
        goto L_0x1032;
    L_0x1067:
        r0 = move-exception;
        r7 = r1;
        r2 = r107;
        goto L_0x103d;
    L_0x106c:
        r0 = move-exception;
        r9 = r108;
        r5 = r115;
        r13 = r1;
        r3 = r49;
        r4 = r104;
        r11 = r107;
        r1 = r0;
        goto L_0x1295;
    L_0x107b:
        r0 = move-exception;
        r9 = r108;
        r5 = r115;
        r7 = r1;
        r2 = r107;
        r1 = r0;
        goto L_0x1242;
    L_0x1086:
        r0 = move-exception;
        r1 = r98;
        r9 = r108;
        r5 = r115;
        r13 = r1;
        r3 = r49;
        r4 = r104;
        r11 = r107;
        r1 = r0;
        goto L_0x1295;
    L_0x1097:
        r0 = move-exception;
        r1 = r98;
        r9 = r108;
        r5 = r115;
        r7 = r1;
        r2 = r107;
        r1 = r0;
        goto L_0x1242;
    L_0x10a4:
        r5 = r12;
        r1 = r13;
        r110 = r14;
        r82 = r15;
        r107 = r41;
        r14 = r42;
        r6 = r44;
        r104 = r45;
        r81 = r46;
        r9 = r47;
        r15 = r116;
    L_0x10b8:
        if (r1 == 0) goto L_0x10bd;
        r1.release();
        if (r107 == 0) goto L_0x10cb;
        r2 = r107;
        r2.finishMovie();	 Catch:{ Exception -> 0x10c5 }
        goto L_0x10cd;
    L_0x10c5:
        r0 = move-exception;
        r3 = r0;
        org.telegram.messenger.FileLog.e(r3);
        goto L_0x10cd;
        r2 = r107;
        r3 = org.telegram.messenger.BuildVars.LOGS_ENABLED;
        if (r3 == 0) goto L_0x1275;
        r3 = new java.lang.StringBuilder;
        r3.<init>();
        r4 = "time = ";
        r3.append(r4);
        r6 = java.lang.System.currentTimeMillis();
        r11 = r6 - r34;
        r3.append(r11);
        r3 = r3.toString();
        org.telegram.messenger.FileLog.d(r3);
        goto L_0x1275;
    L_0x10ed:
        r0 = move-exception;
        r5 = r12;
        r1 = r13;
        r110 = r14;
        r82 = r15;
        r2 = r41;
        r14 = r42;
        r81 = r46;
        r9 = r47;
        r15 = r116;
        r11 = r2;
        r4 = r45;
        r3 = r49;
        r1 = r0;
        goto L_0x1295;
    L_0x1106:
        r0 = move-exception;
        r5 = r12;
        r1 = r13;
        r110 = r14;
        r82 = r15;
        r2 = r41;
        r14 = r42;
        r104 = r45;
        r81 = r46;
        r9 = r47;
        r15 = r116;
        r7 = r1;
        r1 = r0;
        goto L_0x1242;
    L_0x111d:
        r0 = move-exception;
        r1 = r7;
        r110 = r9;
        r49 = r10;
        r40 = r14;
        r43 = r15;
        r9 = r29;
        r82 = r31;
        r81 = r32;
        r17 = r33;
        r2 = r37;
        r32 = r3;
        r33 = r4;
        r14 = r5;
        r29 = r11;
        r5 = r12;
        r15 = r13;
        r13 = r1;
        r11 = r2;
        r4 = r8;
        r3 = r49;
        r1 = r0;
        goto L_0x1295;
    L_0x1142:
        r0 = move-exception;
        r1 = r7;
        r104 = r8;
        r110 = r9;
        r49 = r10;
        r40 = r14;
        r43 = r15;
        r9 = r29;
        r82 = r31;
        r81 = r32;
        r17 = r33;
        r2 = r37;
        r32 = r3;
        r33 = r4;
        r14 = r5;
        r29 = r11;
        r5 = r12;
        r15 = r13;
        r1 = r0;
        goto L_0x1242;
    L_0x1164:
        r0 = move-exception;
        r98 = r7;
        r110 = r9;
        r49 = r10;
        r40 = r14;
        r43 = r15;
        r9 = r29;
        r82 = r31;
        r81 = r32;
        r17 = r33;
        r2 = r37;
        r32 = r3;
        r33 = r4;
        r14 = r5;
        r29 = r11;
        r5 = r12;
        r15 = r13;
        r1 = r0;
        r11 = r2;
        r4 = r8;
        r3 = r49;
        r13 = r98;
        goto L_0x1295;
    L_0x118b:
        r0 = move-exception;
        r98 = r7;
        r104 = r8;
        r110 = r9;
        r49 = r10;
        r40 = r14;
        r43 = r15;
        r9 = r29;
        r82 = r31;
        r81 = r32;
        r17 = r33;
        r2 = r37;
        r32 = r3;
        r33 = r4;
        r14 = r5;
        r29 = r11;
        r5 = r12;
        r15 = r13;
        r1 = r0;
        goto L_0x1242;
    L_0x11ae:
        r0 = move-exception;
        r98 = r7;
        r110 = r9;
        r49 = r10;
        r40 = r14;
        r43 = r15;
        r9 = r29;
        r82 = r31;
        r81 = r32;
        r17 = r33;
        r32 = r3;
        r33 = r4;
        r14 = r5;
        r29 = r11;
        r5 = r12;
        r15 = r13;
        r1 = r0;
        r4 = r8;
        r11 = r36;
        r3 = r49;
        r13 = r98;
        goto L_0x1295;
    L_0x11d4:
        r0 = move-exception;
        r98 = r7;
        r104 = r8;
        r110 = r9;
        r49 = r10;
        r40 = r14;
        r43 = r15;
        r9 = r29;
        r82 = r31;
        r81 = r32;
        r17 = r33;
        r32 = r3;
        r33 = r4;
        r14 = r5;
        r29 = r11;
        r5 = r12;
        r15 = r13;
        r1 = r0;
        r2 = r36;
        goto L_0x1242;
    L_0x11f7:
        r0 = move-exception;
        r36 = r1;
        r98 = r7;
        r110 = r9;
        r49 = r10;
        r40 = r14;
        r43 = r15;
        r9 = r29;
        r82 = r31;
        r81 = r32;
        r17 = r33;
        r32 = r3;
        r33 = r4;
        r14 = r5;
        r29 = r11;
        r5 = r12;
        r15 = r13;
        r1 = r0;
        r4 = r8;
        r11 = r36;
        r3 = r49;
        r13 = r98;
        goto L_0x1295;
    L_0x121f:
        r0 = move-exception;
        r36 = r1;
        r98 = r7;
        r104 = r8;
        r110 = r9;
        r49 = r10;
        r40 = r14;
        r43 = r15;
        r9 = r29;
        r82 = r31;
        r81 = r32;
        r17 = r33;
        r32 = r3;
        r33 = r4;
        r14 = r5;
        r29 = r11;
        r5 = r12;
        r15 = r13;
        r1 = r0;
        r2 = r36;
    L_0x1242:
        r16 = 1;
        org.telegram.messenger.FileLog.e(r1);	 Catch:{ all -> 0x128d }
        if (r7 == 0) goto L_0x124c;
        r7.release();
        if (r2 == 0) goto L_0x1257;
        r2.finishMovie();	 Catch:{ Exception -> 0x1252 }
        goto L_0x1257;
    L_0x1252:
        r0 = move-exception;
        r1 = r0;
        org.telegram.messenger.FileLog.e(r1);
        r1 = org.telegram.messenger.BuildVars.LOGS_ENABLED;
        if (r1 == 0) goto L_0x1275;
        r1 = new java.lang.StringBuilder;
        r1.<init>();
        r3 = "time = ";
        r1.append(r3);
        r3 = java.lang.System.currentTimeMillis();
        r11 = r3 - r34;
        r1.append(r11);
        r1 = r1.toString();
        org.telegram.messenger.FileLog.d(r1);
        r1 = r16;
        r3 = r49;
        r2 = r3.edit();
        r4 = "isPreviousOk";
        r6 = 1;
        r2 = r2.putBoolean(r4, r6);
        r2.commit();
        r4 = r104;
        r5.didWriteData(r15, r4, r6, r1);
        return r6;
    L_0x128d:
        r0 = move-exception;
        r3 = r49;
        r4 = r104;
        r1 = r0;
        r11 = r2;
        r13 = r7;
    L_0x1295:
        if (r13 == 0) goto L_0x129a;
        r13.release();
        if (r11 == 0) goto L_0x12a5;
        r11.finishMovie();	 Catch:{ Exception -> 0x12a0 }
        goto L_0x12a5;
    L_0x12a0:
        r0 = move-exception;
        r2 = r0;
        org.telegram.messenger.FileLog.e(r2);
        r2 = org.telegram.messenger.BuildVars.LOGS_ENABLED;
        if (r2 == 0) goto L_0x12c6;
        r2 = new java.lang.StringBuilder;
        r2.<init>();
        r6 = "time = ";
        r2.append(r6);
        r6 = java.lang.System.currentTimeMillis();
        r112 = r9;
        r8 = r6 - r34;
        r2.append(r8);
        r2 = r2.toString();
        org.telegram.messenger.FileLog.d(r2);
        goto L_0x12c8;
        r112 = r9;
        throw r1;
    L_0x12c9:
        r110 = r9;
        r40 = r14;
        r43 = r15;
        r112 = r29;
        r82 = r31;
        r81 = r32;
        r17 = r33;
        r32 = r3;
        r33 = r4;
        r14 = r5;
        r4 = r8;
        r3 = r10;
        r29 = r11;
        r5 = r12;
        r15 = r13;
        r1 = r3.edit();
        r2 = "isPreviousOk";
        r6 = 1;
        r1 = r1.putBoolean(r2, r6);
        r1.commit();
        r5.didWriteData(r15, r4, r6, r6);
        r1 = 0;
        return r1;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.telegram.messenger.MediaController.convertVideo(org.telegram.messenger.MessageObject):boolean");
    }

    public static java.lang.String copyFileToCache(android.net.Uri r1, java.lang.String r2) {
        /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.DecodeException: Load method exception in method: org.telegram.messenger.MediaController.copyFileToCache(android.net.Uri, java.lang.String):java.lang.String
	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:116)
	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:249)
	at jadx.core.ProcessClass.process(ProcessClass.java:34)
	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:59)
	at jadx.core.ProcessClass.process(ProcessClass.java:42)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:306)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler$1.run(JadxDecompiler.java:199)
Caused by: java.lang.NullPointerException
*/
        /*
        r0 = 0;
        r1 = 0;
        r2 = r1;
        r3 = getFileName(r10);	 Catch:{ Exception -> 0x00a3 }
        r3 = org.telegram.messenger.FileLoader.fixFileName(r3);	 Catch:{ Exception -> 0x00a3 }
        r4 = 0;	 Catch:{ Exception -> 0x00a3 }
        if (r3 != 0) goto L_0x002a;	 Catch:{ Exception -> 0x00a3 }
    L_0x000e:
        r5 = org.telegram.messenger.SharedConfig.getLastLocalId();	 Catch:{ Exception -> 0x00a3 }
        org.telegram.messenger.SharedConfig.saveConfig();	 Catch:{ Exception -> 0x00a3 }
        r6 = java.util.Locale.US;	 Catch:{ Exception -> 0x00a3 }
        r7 = "%d.%s";	 Catch:{ Exception -> 0x00a3 }
        r8 = 2;	 Catch:{ Exception -> 0x00a3 }
        r8 = new java.lang.Object[r8];	 Catch:{ Exception -> 0x00a3 }
        r9 = java.lang.Integer.valueOf(r5);	 Catch:{ Exception -> 0x00a3 }
        r8[r4] = r9;	 Catch:{ Exception -> 0x00a3 }
        r9 = 1;	 Catch:{ Exception -> 0x00a3 }
        r8[r9] = r11;	 Catch:{ Exception -> 0x00a3 }
        r6 = java.lang.String.format(r6, r7, r8);	 Catch:{ Exception -> 0x00a3 }
        r3 = r6;	 Catch:{ Exception -> 0x00a3 }
    L_0x002a:
        r5 = new java.io.File;	 Catch:{ Exception -> 0x00a3 }
        r6 = 4;	 Catch:{ Exception -> 0x00a3 }
        r6 = org.telegram.messenger.FileLoader.getDirectory(r6);	 Catch:{ Exception -> 0x00a3 }
        r7 = "sharing/";	 Catch:{ Exception -> 0x00a3 }
        r5.<init>(r6, r7);	 Catch:{ Exception -> 0x00a3 }
        r5.mkdirs();	 Catch:{ Exception -> 0x00a3 }
        r6 = new java.io.File;	 Catch:{ Exception -> 0x00a3 }
        r6.<init>(r5, r3);	 Catch:{ Exception -> 0x00a3 }
        r5 = r6;	 Catch:{ Exception -> 0x00a3 }
        r6 = android.net.Uri.fromFile(r5);	 Catch:{ Exception -> 0x00a3 }
        r6 = org.telegram.messenger.AndroidUtilities.isInternalUri(r6);	 Catch:{ Exception -> 0x00a3 }
        if (r6 == 0) goto L_0x0063;
    L_0x004a:
        if (r0 == 0) goto L_0x0055;
    L_0x004c:
        r0.close();	 Catch:{ Exception -> 0x0050 }
        goto L_0x0055;
    L_0x0050:
        r4 = move-exception;
        org.telegram.messenger.FileLog.e(r4);
        goto L_0x0056;
        if (r2 == 0) goto L_0x0061;
        r2.close();	 Catch:{ Exception -> 0x005c }
        goto L_0x0061;
    L_0x005c:
        r4 = move-exception;
        org.telegram.messenger.FileLog.e(r4);
        goto L_0x0062;
        return r1;
    L_0x0063:
        r6 = org.telegram.messenger.ApplicationLoader.applicationContext;	 Catch:{ Exception -> 0x00a3 }
        r6 = r6.getContentResolver();	 Catch:{ Exception -> 0x00a3 }
        r6 = r6.openInputStream(r10);	 Catch:{ Exception -> 0x00a3 }
        r0 = r6;	 Catch:{ Exception -> 0x00a3 }
        r6 = new java.io.FileOutputStream;	 Catch:{ Exception -> 0x00a3 }
        r6.<init>(r5);	 Catch:{ Exception -> 0x00a3 }
        r2 = r6;	 Catch:{ Exception -> 0x00a3 }
        r6 = 20480; // 0x5000 float:2.8699E-41 double:1.01185E-319;	 Catch:{ Exception -> 0x00a3 }
        r6 = new byte[r6];	 Catch:{ Exception -> 0x00a3 }
        r7 = r0.read(r6);	 Catch:{ Exception -> 0x00a3 }
        r8 = r7;	 Catch:{ Exception -> 0x00a3 }
        r9 = -1;	 Catch:{ Exception -> 0x00a3 }
        if (r7 == r9) goto L_0x0084;	 Catch:{ Exception -> 0x00a3 }
        r2.write(r6, r4, r8);	 Catch:{ Exception -> 0x00a3 }
        goto L_0x0078;	 Catch:{ Exception -> 0x00a3 }
        r4 = r5.getAbsolutePath();	 Catch:{ Exception -> 0x00a3 }
        if (r0 == 0) goto L_0x0093;
        r0.close();	 Catch:{ Exception -> 0x008e }
        goto L_0x0093;
    L_0x008e:
        r1 = move-exception;
        org.telegram.messenger.FileLog.e(r1);
        goto L_0x0094;
        if (r2 == 0) goto L_0x009f;
        r2.close();	 Catch:{ Exception -> 0x009a }
        goto L_0x009f;
    L_0x009a:
        r1 = move-exception;
        org.telegram.messenger.FileLog.e(r1);
        goto L_0x00a0;
        return r4;
    L_0x00a1:
        r1 = move-exception;
        goto L_0x00c0;
    L_0x00a3:
        r3 = move-exception;
        org.telegram.messenger.FileLog.e(r3);	 Catch:{ all -> 0x00a1 }
        if (r0 == 0) goto L_0x00b2;
        r0.close();	 Catch:{ Exception -> 0x00ad }
        goto L_0x00b2;
    L_0x00ad:
        r3 = move-exception;
        org.telegram.messenger.FileLog.e(r3);
        goto L_0x00b3;
        if (r2 == 0) goto L_0x00be;
        r2.close();	 Catch:{ Exception -> 0x00b9 }
        goto L_0x00be;
    L_0x00b9:
        r3 = move-exception;
        org.telegram.messenger.FileLog.e(r3);
        goto L_0x00bf;
        return r1;
        if (r0 == 0) goto L_0x00cc;
        r0.close();	 Catch:{ Exception -> 0x00c7 }
        goto L_0x00cc;
    L_0x00c7:
        r3 = move-exception;
        org.telegram.messenger.FileLog.e(r3);
        goto L_0x00cd;
        if (r2 == 0) goto L_0x00d8;
        r2.close();	 Catch:{ Exception -> 0x00d3 }
        goto L_0x00d8;
    L_0x00d3:
        r3 = move-exception;
        org.telegram.messenger.FileLog.e(r3);
        throw r1;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.telegram.messenger.MediaController.copyFileToCache(android.net.Uri, java.lang.String):java.lang.String");
    }

    private native long getTotalPcmDuration();

    public static boolean isGif(android.net.Uri r1) {
        /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.DecodeException: Load method exception in method: org.telegram.messenger.MediaController.isGif(android.net.Uri):boolean
	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:116)
	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:249)
	at jadx.core.ProcessClass.process(ProcessClass.java:34)
	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:59)
	at jadx.core.ProcessClass.process(ProcessClass.java:42)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:306)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler$1.run(JadxDecompiler.java:199)
Caused by: java.lang.NullPointerException
*/
        /*
        r0 = 0;
        r1 = 0;
        r2 = org.telegram.messenger.ApplicationLoader.applicationContext;	 Catch:{ Exception -> 0x0041 }
        r2 = r2.getContentResolver();	 Catch:{ Exception -> 0x0041 }
        r2 = r2.openInputStream(r5);	 Catch:{ Exception -> 0x0041 }
        r0 = r2;	 Catch:{ Exception -> 0x0041 }
        r2 = 3;	 Catch:{ Exception -> 0x0041 }
        r3 = new byte[r2];	 Catch:{ Exception -> 0x0041 }
        r4 = r0.read(r3, r1, r2);	 Catch:{ Exception -> 0x0041 }
        if (r4 != r2) goto L_0x0033;	 Catch:{ Exception -> 0x0041 }
    L_0x0016:
        r2 = new java.lang.String;	 Catch:{ Exception -> 0x0041 }
        r2.<init>(r3);	 Catch:{ Exception -> 0x0041 }
        if (r2 == 0) goto L_0x0033;	 Catch:{ Exception -> 0x0041 }
    L_0x001d:
        r4 = "gif";	 Catch:{ Exception -> 0x0041 }
        r4 = r2.equalsIgnoreCase(r4);	 Catch:{ Exception -> 0x0041 }
        if (r4 == 0) goto L_0x0033;
    L_0x0025:
        r1 = 1;
        if (r0 == 0) goto L_0x0031;
    L_0x0028:
        r0.close();	 Catch:{ Exception -> 0x002c }
        goto L_0x0031;
    L_0x002c:
        r4 = move-exception;
        org.telegram.messenger.FileLog.e(r4);
        goto L_0x0032;
        return r1;
    L_0x0033:
        if (r0 == 0) goto L_0x003e;
        r0.close();	 Catch:{ Exception -> 0x0039 }
        goto L_0x003e;
    L_0x0039:
        r2 = move-exception;
        org.telegram.messenger.FileLog.e(r2);
        goto L_0x004b;
        goto L_0x004b;
    L_0x003f:
        r1 = move-exception;
        goto L_0x004c;
    L_0x0041:
        r2 = move-exception;
        org.telegram.messenger.FileLog.e(r2);	 Catch:{ all -> 0x003f }
        if (r0 == 0) goto L_0x003e;
        r0.close();	 Catch:{ Exception -> 0x0039 }
        goto L_0x003e;
        return r1;
        if (r0 == 0) goto L_0x0058;
        r0.close();	 Catch:{ Exception -> 0x0053 }
        goto L_0x0058;
    L_0x0053:
        r2 = move-exception;
        org.telegram.messenger.FileLog.e(r2);
        throw r1;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.telegram.messenger.MediaController.isGif(android.net.Uri):boolean");
    }

    public static native int isOpusFile(String str);

    public static boolean isWebp(android.net.Uri r1) {
        /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.DecodeException: Load method exception in method: org.telegram.messenger.MediaController.isWebp(android.net.Uri):boolean
	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:116)
	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:249)
	at jadx.core.ProcessClass.process(ProcessClass.java:34)
	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:59)
	at jadx.core.ProcessClass.process(ProcessClass.java:42)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:306)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler$1.run(JadxDecompiler.java:199)
Caused by: java.lang.NullPointerException
*/
        /*
        r0 = 0;
        r1 = 0;
        r2 = org.telegram.messenger.ApplicationLoader.applicationContext;	 Catch:{ Exception -> 0x004f }
        r2 = r2.getContentResolver();	 Catch:{ Exception -> 0x004f }
        r2 = r2.openInputStream(r5);	 Catch:{ Exception -> 0x004f }
        r0 = r2;	 Catch:{ Exception -> 0x004f }
        r2 = 12;	 Catch:{ Exception -> 0x004f }
        r3 = new byte[r2];	 Catch:{ Exception -> 0x004f }
        r4 = r0.read(r3, r1, r2);	 Catch:{ Exception -> 0x004f }
        if (r4 != r2) goto L_0x0041;	 Catch:{ Exception -> 0x004f }
    L_0x0017:
        r2 = new java.lang.String;	 Catch:{ Exception -> 0x004f }
        r2.<init>(r3);	 Catch:{ Exception -> 0x004f }
        if (r2 == 0) goto L_0x0041;	 Catch:{ Exception -> 0x004f }
    L_0x001e:
        r4 = r2.toLowerCase();	 Catch:{ Exception -> 0x004f }
        r2 = r4;	 Catch:{ Exception -> 0x004f }
        r4 = "riff";	 Catch:{ Exception -> 0x004f }
        r4 = r2.startsWith(r4);	 Catch:{ Exception -> 0x004f }
        if (r4 == 0) goto L_0x0041;	 Catch:{ Exception -> 0x004f }
    L_0x002b:
        r4 = "webp";	 Catch:{ Exception -> 0x004f }
        r4 = r2.endsWith(r4);	 Catch:{ Exception -> 0x004f }
        if (r4 == 0) goto L_0x0041;
    L_0x0033:
        r1 = 1;
        if (r0 == 0) goto L_0x003f;
    L_0x0036:
        r0.close();	 Catch:{ Exception -> 0x003a }
        goto L_0x003f;
    L_0x003a:
        r4 = move-exception;
        org.telegram.messenger.FileLog.e(r4);
        goto L_0x0040;
        return r1;
    L_0x0041:
        if (r0 == 0) goto L_0x004c;
        r0.close();	 Catch:{ Exception -> 0x0047 }
        goto L_0x004c;
    L_0x0047:
        r2 = move-exception;
        org.telegram.messenger.FileLog.e(r2);
        goto L_0x0059;
        goto L_0x0059;
    L_0x004d:
        r1 = move-exception;
        goto L_0x005a;
    L_0x004f:
        r2 = move-exception;
        org.telegram.messenger.FileLog.e(r2);	 Catch:{ all -> 0x004d }
        if (r0 == 0) goto L_0x004c;
        r0.close();	 Catch:{ Exception -> 0x0047 }
        goto L_0x004c;
        return r1;
        if (r0 == 0) goto L_0x0066;
        r0.close();	 Catch:{ Exception -> 0x0061 }
        goto L_0x0066;
    L_0x0061:
        r2 = move-exception;
        org.telegram.messenger.FileLog.e(r2);
        throw r1;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.telegram.messenger.MediaController.isWebp(android.net.Uri):boolean");
    }

    private native int openOpusFile(String str);

    private native void readOpusFile(ByteBuffer byteBuffer, int i, int[] iArr);

    private native int seekOpusFile(float f);

    private native int startRecord(String str);

    private native void stopRecord();

    private native int writeFrame(ByteBuffer byteBuffer, int i);

    public void didReceivedNotification(int r1, int r2, java.lang.Object... r3) {
        /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.DecodeException: Load method exception in method: org.telegram.messenger.MediaController.didReceivedNotification(int, int, java.lang.Object[]):void
	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:116)
	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:249)
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
        r0 = org.telegram.messenger.NotificationCenter.FileDidLoaded;
        r1 = 1;
        r2 = 0;
        if (r9 == r0) goto L_0x0171;
    L_0x0006:
        r0 = org.telegram.messenger.NotificationCenter.httpFileDidLoaded;
        if (r9 != r0) goto L_0x000c;
    L_0x000a:
        goto L_0x0171;
    L_0x000c:
        r0 = org.telegram.messenger.NotificationCenter.messagesDeleted;
        if (r9 != r0) goto L_0x0088;
    L_0x0010:
        r0 = r11[r1];
        r0 = (java.lang.Integer) r0;
        r0 = r0.intValue();
        r3 = r11[r2];
        r3 = (java.util.ArrayList) r3;
        r4 = r8.playingMessageObject;
        if (r4 == 0) goto L_0x003d;
    L_0x0020:
        r4 = r8.playingMessageObject;
        r4 = r4.messageOwner;
        r4 = r4.to_id;
        r4 = r4.channel_id;
        if (r0 != r4) goto L_0x003d;
    L_0x002a:
        r4 = r8.playingMessageObject;
        r4 = r4.getId();
        r4 = java.lang.Integer.valueOf(r4);
        r4 = r3.contains(r4);
        if (r4 == 0) goto L_0x003d;
    L_0x003a:
        r8.cleanupPlayer(r1, r1);
    L_0x003d:
        r1 = r8.voiceMessagesPlaylist;
        if (r1 == 0) goto L_0x0086;
    L_0x0041:
        r1 = r8.voiceMessagesPlaylist;
        r1 = r1.isEmpty();
        if (r1 != 0) goto L_0x0086;
    L_0x0049:
        r1 = r8.voiceMessagesPlaylist;
        r1 = r1.get(r2);
        r1 = (org.telegram.messenger.MessageObject) r1;
        r4 = r1.messageOwner;
        r4 = r4.to_id;
        r4 = r4.channel_id;
        if (r0 != r4) goto L_0x0086;
    L_0x005a:
        r4 = r3.size();
        if (r2 >= r4) goto L_0x0086;
    L_0x0060:
        r4 = r3.get(r2);
        r4 = (java.lang.Integer) r4;
        r5 = r8.voiceMessagesPlaylistMap;
        r6 = r4.intValue();
        r5 = r5.get(r6);
        r1 = r5;
        r1 = (org.telegram.messenger.MessageObject) r1;
        r5 = r8.voiceMessagesPlaylistMap;
        r6 = r4.intValue();
        r5.remove(r6);
        if (r1 == 0) goto L_0x0083;
    L_0x007e:
        r5 = r8.voiceMessagesPlaylist;
        r5.remove(r1);
    L_0x0083:
        r2 = r2 + 1;
        goto L_0x005a;
    L_0x0086:
        goto L_0x019b;
    L_0x0088:
        r0 = org.telegram.messenger.NotificationCenter.removeAllMessagesFromDialog;
        if (r9 != r0) goto L_0x00a7;
    L_0x008c:
        r0 = r11[r2];
        r0 = (java.lang.Long) r0;
        r3 = r0.longValue();
        r0 = r8.playingMessageObject;
        if (r0 == 0) goto L_0x00a5;
    L_0x0098:
        r0 = r8.playingMessageObject;
        r5 = r0.getDialogId();
        r0 = (r5 > r3 ? 1 : (r5 == r3 ? 0 : -1));
        if (r0 != 0) goto L_0x00a5;
    L_0x00a2:
        r8.cleanupPlayer(r2, r1);
    L_0x00a5:
        goto L_0x019b;
    L_0x00a7:
        r0 = org.telegram.messenger.NotificationCenter.musicDidLoaded;
        if (r9 != r0) goto L_0x00e7;
    L_0x00ab:
        r0 = r11[r2];
        r0 = (java.lang.Long) r0;
        r3 = r0.longValue();
        r0 = r8.playingMessageObject;
        if (r0 == 0) goto L_0x00e5;
    L_0x00b7:
        r0 = r8.playingMessageObject;
        r0 = r0.isMusic();
        if (r0 == 0) goto L_0x00e5;
    L_0x00bf:
        r0 = r8.playingMessageObject;
        r5 = r0.getDialogId();
        r0 = (r5 > r3 ? 1 : (r5 == r3 ? 0 : -1));
        if (r0 != 0) goto L_0x00e5;
    L_0x00c9:
        r0 = r11[r1];
        r0 = (java.util.ArrayList) r0;
        r1 = r8.playlist;
        r1.addAll(r2, r0);
        r1 = org.telegram.messenger.SharedConfig.shuffleMusic;
        if (r1 == 0) goto L_0x00dc;
    L_0x00d6:
        r8.buildShuffledPlayList();
        r8.currentPlaylistNum = r2;
        goto L_0x00e5;
    L_0x00dc:
        r1 = r8.currentPlaylistNum;
        r2 = r0.size();
        r1 = r1 + r2;
        r8.currentPlaylistNum = r1;
    L_0x00e5:
        goto L_0x019b;
    L_0x00e7:
        r0 = org.telegram.messenger.NotificationCenter.didReceivedNewMessages;
        if (r9 != r0) goto L_0x014f;
    L_0x00eb:
        r0 = r8.voiceMessagesPlaylist;
        if (r0 == 0) goto L_0x019b;
    L_0x00ef:
        r0 = r8.voiceMessagesPlaylist;
        r0 = r0.isEmpty();
        if (r0 != 0) goto L_0x019b;
    L_0x00f7:
        r0 = r8.voiceMessagesPlaylist;
        r0 = r0.get(r2);
        r0 = (org.telegram.messenger.MessageObject) r0;
        r3 = r11[r2];
        r3 = (java.lang.Long) r3;
        r3 = r3.longValue();
        r5 = r0.getDialogId();
        r7 = (r3 > r5 ? 1 : (r3 == r5 ? 0 : -1));
        if (r7 != 0) goto L_0x014e;
    L_0x010f:
        r1 = r11[r1];
        r1 = (java.util.ArrayList) r1;
    L_0x0114:
        r5 = r1.size();
        if (r2 >= r5) goto L_0x014e;
    L_0x011a:
        r5 = r1.get(r2);
        r0 = r5;
        r0 = (org.telegram.messenger.MessageObject) r0;
        r5 = r0.isVoice();
        if (r5 != 0) goto L_0x012d;
    L_0x0127:
        r5 = r0.isRoundVideo();
        if (r5 == 0) goto L_0x014b;
    L_0x012d:
        r5 = r8.voiceMessagesPlaylistUnread;
        if (r5 == 0) goto L_0x013d;
    L_0x0131:
        r5 = r0.isContentUnread();
        if (r5 == 0) goto L_0x014b;
    L_0x0137:
        r5 = r0.isOut();
        if (r5 != 0) goto L_0x014b;
    L_0x013d:
        r5 = r8.voiceMessagesPlaylist;
        r5.add(r0);
        r5 = r8.voiceMessagesPlaylistMap;
        r6 = r0.getId();
        r5.put(r6, r0);
    L_0x014b:
        r2 = r2 + 1;
        goto L_0x0114;
    L_0x014e:
        goto L_0x019b;
    L_0x014f:
        r0 = org.telegram.messenger.NotificationCenter.playerDidStartPlaying;
        if (r9 != r0) goto L_0x019b;
    L_0x0153:
        r0 = r11[r2];
        r0 = (org.telegram.ui.Components.VideoPlayer) r0;
        r1 = getInstance();
        r1 = r1.isCurrentPlayer(r0);
        if (r1 != 0) goto L_0x019b;
    L_0x0161:
        r1 = getInstance();
        r2 = getInstance();
        r2 = r2.getPlayingMessageObject();
        r1.pauseMessage(r2);
        goto L_0x019b;
    L_0x0171:
        r0 = r11[r2];
        r0 = (java.lang.String) r0;
        r2 = r8.downloadingCurrentMessage;
        if (r2 == 0) goto L_0x019a;
    L_0x0179:
        r2 = r8.playingMessageObject;
        if (r2 == 0) goto L_0x019a;
        r2 = r8.playingMessageObject;
        r2 = r2.currentAccount;
        if (r2 != r10) goto L_0x019a;
        r2 = r8.playingMessageObject;
        r2 = r2.getDocument();
        r2 = org.telegram.messenger.FileLoader.getAttachFileName(r2);
        r3 = r2.equals(r0);
        if (r3 == 0) goto L_0x019a;
        r8.playMusicAgain = r1;
        r1 = r8.playingMessageObject;
        r8.playMessage(r1);
    L_0x019b:
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.telegram.messenger.MediaController.didReceivedNotification(int, int, java.lang.Object[]):void");
    }

    public native byte[] getWaveform(String str);

    public native byte[] getWaveform2(short[] sArr, int i);

    public boolean playMessage(org.telegram.messenger.MessageObject r1) {
        /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.DecodeException: Load method exception in method: org.telegram.messenger.MediaController.playMessage(org.telegram.messenger.MessageObject):boolean
	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:116)
	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:249)
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
        r1 = r25;
        r2 = r26;
        r3 = 0;
        if (r2 != 0) goto L_0x0008;
    L_0x0007:
        return r3;
    L_0x0008:
        r4 = r1.audioTrackPlayer;
        r5 = 1;
        if (r4 != 0) goto L_0x0015;
    L_0x000d:
        r4 = r1.audioPlayer;
        if (r4 != 0) goto L_0x0015;
    L_0x0011:
        r4 = r1.videoPlayer;
        if (r4 == 0) goto L_0x002c;
    L_0x0015:
        r4 = r25.isSamePlayingMessage(r26);
        if (r4 == 0) goto L_0x002c;
    L_0x001b:
        r3 = r1.isPaused;
        if (r3 == 0) goto L_0x0022;
    L_0x001f:
        r25.resumeAudio(r26);
    L_0x0022:
        r3 = org.telegram.messenger.SharedConfig.raiseToSpeak;
        if (r3 != 0) goto L_0x002b;
    L_0x0026:
        r3 = r1.raiseChat;
        r1.startRaiseToEarSensors(r3);
    L_0x002b:
        return r5;
    L_0x002c:
        r4 = r26.isOut();
        if (r4 != 0) goto L_0x0041;
    L_0x0032:
        r4 = r26.isContentUnread();
        if (r4 == 0) goto L_0x0041;
    L_0x0038:
        r4 = r2.currentAccount;
        r4 = org.telegram.messenger.MessagesController.getInstance(r4);
        r4.markMessageContentAsRead(r2);
    L_0x0041:
        r4 = r1.playMusicAgain;
        r4 = r4 ^ r5;
        r6 = r1.playingMessageObject;
        if (r6 == 0) goto L_0x0052;
    L_0x0048:
        r4 = 0;
        r6 = r1.playMusicAgain;
        if (r6 != 0) goto L_0x0052;
    L_0x004d:
        r6 = r1.playingMessageObject;
        r6.resetPlayingProgress();
    L_0x0052:
        r1.cleanupPlayer(r4, r3);
        r1.playMusicAgain = r3;
        r6 = 0;
        r1.seekToProgressPending = r6;
        r7 = 0;
        r8 = 0;
        r9 = r2.messageOwner;
        r9 = r9.attachPath;
        if (r9 == 0) goto L_0x007d;
    L_0x0062:
        r9 = r2.messageOwner;
        r9 = r9.attachPath;
        r9 = r9.length();
        if (r9 <= 0) goto L_0x007d;
    L_0x006c:
        r9 = new java.io.File;
        r10 = r2.messageOwner;
        r10 = r10.attachPath;
        r9.<init>(r10);
        r7 = r9;
        r8 = r7.exists();
        if (r8 != 0) goto L_0x007d;
    L_0x007c:
        r7 = 0;
    L_0x007d:
        if (r7 == 0) goto L_0x0081;
    L_0x007f:
        r9 = r7;
        goto L_0x0087;
    L_0x0081:
        r9 = r2.messageOwner;
        r9 = org.telegram.messenger.FileLoader.getPathToMessage(r9);
    L_0x0087:
        r10 = org.telegram.messenger.SharedConfig.streamMedia;
        if (r10 == 0) goto L_0x009a;
    L_0x008b:
        r10 = r26.isMusic();
        if (r10 == 0) goto L_0x009a;
    L_0x0091:
        r10 = r26.getDialogId();
        r10 = (int) r10;
        if (r10 == 0) goto L_0x009a;
    L_0x0098:
        r10 = r5;
        goto L_0x009b;
    L_0x009a:
        r10 = r3;
    L_0x009b:
        r11 = 0;
        r13 = 0;
        if (r9 == 0) goto L_0x010b;
    L_0x00a0:
        if (r9 == r7) goto L_0x010b;
    L_0x00a2:
        r14 = r9.exists();
        r8 = r14;
        if (r14 != 0) goto L_0x010b;
    L_0x00a9:
        if (r10 != 0) goto L_0x010b;
    L_0x00ab:
        r6 = r2.currentAccount;
        r6 = org.telegram.messenger.FileLoader.getInstance(r6);
        r14 = r26.getDocument();
        r6.loadFile(r14, r3, r3);
        r1.downloadingCurrentMessage = r5;
        r1.isPaused = r3;
        r1.lastProgress = r11;
        r1.lastPlayPcm = r11;
        r1.audioInfo = r13;
        r1.playingMessageObject = r2;
        r6 = r1.playingMessageObject;
        r6 = r6.isMusic();
        if (r6 == 0) goto L_0x00e1;
    L_0x00cc:
        r6 = new android.content.Intent;
        r11 = org.telegram.messenger.ApplicationLoader.applicationContext;
        r12 = org.telegram.messenger.MusicPlayerService.class;
        r6.<init>(r11, r12);
        r11 = org.telegram.messenger.ApplicationLoader.applicationContext;	 Catch:{ Throwable -> 0x00db }
        r11.startService(r6);	 Catch:{ Throwable -> 0x00db }
        goto L_0x00e0;
    L_0x00db:
        r0 = move-exception;
        r11 = r0;
        org.telegram.messenger.FileLog.e(r11);
    L_0x00e0:
        goto L_0x00ef;
    L_0x00e1:
        r6 = new android.content.Intent;
        r11 = org.telegram.messenger.ApplicationLoader.applicationContext;
        r12 = org.telegram.messenger.MusicPlayerService.class;
        r6.<init>(r11, r12);
        r11 = org.telegram.messenger.ApplicationLoader.applicationContext;
        r11.stopService(r6);
    L_0x00ef:
        r6 = r1.playingMessageObject;
        r6 = r6.currentAccount;
        r6 = org.telegram.messenger.NotificationCenter.getInstance(r6);
        r11 = org.telegram.messenger.NotificationCenter.messagePlayingPlayStateChanged;
        r12 = new java.lang.Object[r5];
        r13 = r1.playingMessageObject;
        r13 = r13.getId();
        r13 = java.lang.Integer.valueOf(r13);
        r12[r3] = r13;
        r6.postNotificationName(r11, r12);
        return r5;
    L_0x010b:
        r1.downloadingCurrentMessage = r3;
        r14 = r26.isMusic();
        if (r14 == 0) goto L_0x0119;
    L_0x0113:
        r14 = r2.currentAccount;
        r1.checkIsNextMusicFileDownloaded(r14);
        goto L_0x011e;
    L_0x0119:
        r14 = r2.currentAccount;
        r1.checkIsNextVoiceFileDownloaded(r14);
    L_0x011e:
        r14 = r1.currentAspectRatioFrameLayout;
        if (r14 == 0) goto L_0x0129;
    L_0x0122:
        r1.isDrawingWasReady = r3;
        r14 = r1.currentAspectRatioFrameLayout;
        r14.setDrawingReady(r3);
    L_0x0129:
        r14 = r26.isRoundVideo();
        r15 = 1065353216; // 0x3f800000 float:1.0 double:5.263544247E-315;
        r6 = 3;
        if (r14 == 0) goto L_0x01bc;
    L_0x0132:
        r14 = r1.playlist;
        r14.clear();
        r14 = r1.shuffledPlaylist;
        r14.clear();
        r14 = new org.telegram.ui.Components.VideoPlayer;
        r14.<init>();
        r1.videoPlayer = r14;
        r14 = r1.videoPlayer;
        r11 = new org.telegram.messenger.MediaController$16;
        r11.<init>();
        r14.setDelegate(r11);
        r1.currentAspectRatioFrameLayoutReady = r3;
        r11 = r1.pipRoundVideoView;
        if (r11 != 0) goto L_0x0172;
    L_0x0153:
        r11 = r2.currentAccount;
        r11 = org.telegram.messenger.MessagesController.getInstance(r11);
        r16 = r4;
        r3 = r26.getDialogId();
        r3 = r11.isDialogCreated(r3);
        if (r3 != 0) goto L_0x0166;
    L_0x0165:
        goto L_0x0174;
    L_0x0166:
        r3 = r1.currentTextureView;
        if (r3 == 0) goto L_0x019f;
    L_0x016a:
        r3 = r1.videoPlayer;
        r4 = r1.currentTextureView;
        r3.setTextureView(r4);
        goto L_0x019f;
    L_0x0172:
        r16 = r4;
    L_0x0174:
        r3 = r1.pipRoundVideoView;
        if (r3 != 0) goto L_0x0190;
    L_0x0178:
        r3 = new org.telegram.ui.Components.PipRoundVideoView;	 Catch:{ Exception -> 0x018c }
        r3.<init>();	 Catch:{ Exception -> 0x018c }
        r1.pipRoundVideoView = r3;	 Catch:{ Exception -> 0x018c }
        r3 = r1.pipRoundVideoView;	 Catch:{ Exception -> 0x018c }
        r4 = r1.baseActivity;	 Catch:{ Exception -> 0x018c }
        r11 = new org.telegram.messenger.MediaController$17;	 Catch:{ Exception -> 0x018c }
        r11.<init>();	 Catch:{ Exception -> 0x018c }
        r3.show(r4, r11);	 Catch:{ Exception -> 0x018c }
        goto L_0x0190;
    L_0x018c:
        r0 = move-exception;
        r3 = r0;
        r1.pipRoundVideoView = r13;
    L_0x0190:
        r3 = r1.pipRoundVideoView;
        if (r3 == 0) goto L_0x019f;
    L_0x0194:
        r3 = r1.videoPlayer;
        r4 = r1.pipRoundVideoView;
        r4 = r4.getTextureView();
        r3.setTextureView(r4);
    L_0x019f:
        r3 = r1.videoPlayer;
        r4 = android.net.Uri.fromFile(r9);
        r11 = "other";
        r3.preparePlayer(r4, r11);
        r3 = r1.videoPlayer;
        r4 = r1.useFrontSpeaker;
        if (r4 == 0) goto L_0x01b2;
    L_0x01b0:
        r6 = 0;
    L_0x01b2:
        r3.setStreamType(r6);
        r3 = r1.videoPlayer;
        r3.play();
        goto L_0x036d;
    L_0x01bc:
        r16 = r4;
        r3 = r26.isMusic();
        if (r3 != 0) goto L_0x0276;
    L_0x01c4:
        r3 = r9.getAbsolutePath();
        r3 = isOpusFile(r3);
        if (r3 != r5) goto L_0x0276;
    L_0x01ce:
        r3 = r1.pipRoundVideoView;
        if (r3 == 0) goto L_0x01d9;
    L_0x01d2:
        r3 = r1.pipRoundVideoView;
        r3.close(r5);
        r1.pipRoundVideoView = r13;
    L_0x01d9:
        r3 = r1.playlist;
        r3.clear();
        r3 = r1.shuffledPlaylist;
        r3.clear();
        r3 = r1.playerObjectSync;
        monitor-enter(r3);
        r1.ignoreFirstProgress = r6;	 Catch:{ Exception -> 0x0256, all -> 0x0251 }
        r4 = new java.util.concurrent.CountDownLatch;	 Catch:{ Exception -> 0x0256, all -> 0x0251 }
        r4.<init>(r5);	 Catch:{ Exception -> 0x0256, all -> 0x0251 }
        r11 = new java.lang.Boolean[r5];	 Catch:{ Exception -> 0x0256, all -> 0x0251 }
        r12 = r1.fileDecodingQueue;	 Catch:{ Exception -> 0x0256, all -> 0x0251 }
        r14 = new org.telegram.messenger.MediaController$18;	 Catch:{ Exception -> 0x0256, all -> 0x0251 }
        r14.<init>(r11, r9, r4);	 Catch:{ Exception -> 0x0256, all -> 0x0251 }
        r12.postRunnable(r14);	 Catch:{ Exception -> 0x0256, all -> 0x0251 }
        r4.await();	 Catch:{ Exception -> 0x0256, all -> 0x0251 }
        r12 = 0;	 Catch:{ Exception -> 0x0256, all -> 0x0251 }
        r14 = r11[r12];	 Catch:{ Exception -> 0x0256, all -> 0x0251 }
        r14 = r14.booleanValue();	 Catch:{ Exception -> 0x0256, all -> 0x0251 }
        if (r14 != 0) goto L_0x020d;
    L_0x0205:
        monitor-exit(r3);	 Catch:{ all -> 0x0207 }
        return r12;
    L_0x0207:
        r0 = move-exception;
        r4 = r0;
        r17 = r7;
        goto L_0x0274;
    L_0x020d:
        r17 = r7;
        r6 = r25.getTotalPcmDuration();	 Catch:{ Exception -> 0x024e }
        r1.currentTotalPcmDuration = r6;	 Catch:{ Exception -> 0x024e }
        r6 = new android.media.AudioTrack;	 Catch:{ Exception -> 0x024e }
        r7 = r1.useFrontSpeaker;	 Catch:{ Exception -> 0x024e }
        if (r7 == 0) goto L_0x021e;	 Catch:{ Exception -> 0x024e }
    L_0x021b:
        r19 = 0;	 Catch:{ Exception -> 0x024e }
        goto L_0x0220;	 Catch:{ Exception -> 0x024e }
    L_0x021e:
        r19 = 3;	 Catch:{ Exception -> 0x024e }
    L_0x0220:
        r20 = 48000; // 0xbb80 float:6.7262E-41 double:2.3715E-319;	 Catch:{ Exception -> 0x024e }
        r21 = 4;	 Catch:{ Exception -> 0x024e }
        r22 = 2;	 Catch:{ Exception -> 0x024e }
        r7 = r1.playerBufferSize;	 Catch:{ Exception -> 0x024e }
        r24 = 1;	 Catch:{ Exception -> 0x024e }
        r18 = r6;	 Catch:{ Exception -> 0x024e }
        r23 = r7;	 Catch:{ Exception -> 0x024e }
        r18.<init>(r19, r20, r21, r22, r23, r24);	 Catch:{ Exception -> 0x024e }
        r1.audioTrackPlayer = r6;	 Catch:{ Exception -> 0x024e }
        r6 = r1.audioTrackPlayer;	 Catch:{ Exception -> 0x024e }
        r6.setStereoVolume(r15, r15);	 Catch:{ Exception -> 0x024e }
        r6 = r1.audioTrackPlayer;	 Catch:{ Exception -> 0x024e }
        r7 = new org.telegram.messenger.MediaController$19;	 Catch:{ Exception -> 0x024e }
        r7.<init>();	 Catch:{ Exception -> 0x024e }
        r6.setPlaybackPositionUpdateListener(r7);	 Catch:{ Exception -> 0x024e }
        r6 = r1.audioTrackPlayer;	 Catch:{ Exception -> 0x024e }
        r6.play();	 Catch:{ Exception -> 0x024e }
        monitor-exit(r3);	 Catch:{ all -> 0x0272 }
        r7 = r17;	 Catch:{ all -> 0x0272 }
        goto L_0x036d;	 Catch:{ all -> 0x0272 }
    L_0x024e:
        r0 = move-exception;	 Catch:{ all -> 0x0272 }
        r4 = r0;	 Catch:{ all -> 0x0272 }
        goto L_0x025a;	 Catch:{ all -> 0x0272 }
    L_0x0251:
        r0 = move-exception;	 Catch:{ all -> 0x0272 }
        r17 = r7;	 Catch:{ all -> 0x0272 }
        r4 = r0;	 Catch:{ all -> 0x0272 }
        goto L_0x0274;	 Catch:{ all -> 0x0272 }
    L_0x0256:
        r0 = move-exception;	 Catch:{ all -> 0x0272 }
        r17 = r7;	 Catch:{ all -> 0x0272 }
        r4 = r0;	 Catch:{ all -> 0x0272 }
    L_0x025a:
        org.telegram.messenger.FileLog.e(r4);	 Catch:{ all -> 0x0272 }
        r5 = r1.audioTrackPlayer;	 Catch:{ all -> 0x0272 }
        if (r5 == 0) goto L_0x026f;	 Catch:{ all -> 0x0272 }
    L_0x0261:
        r5 = r1.audioTrackPlayer;	 Catch:{ all -> 0x0272 }
        r5.release();	 Catch:{ all -> 0x0272 }
        r1.audioTrackPlayer = r13;	 Catch:{ all -> 0x0272 }
        r5 = 0;	 Catch:{ all -> 0x0272 }
        r1.isPaused = r5;	 Catch:{ all -> 0x0272 }
        r1.playingMessageObject = r13;	 Catch:{ all -> 0x0272 }
        r1.downloadingCurrentMessage = r5;	 Catch:{ all -> 0x0272 }
    L_0x026f:
        monitor-exit(r3);	 Catch:{ all -> 0x0272 }
        r3 = 0;	 Catch:{ all -> 0x0272 }
        return r3;	 Catch:{ all -> 0x0272 }
    L_0x0272:
        r0 = move-exception;	 Catch:{ all -> 0x0272 }
        r4 = r0;	 Catch:{ all -> 0x0272 }
    L_0x0274:
        monitor-exit(r3);	 Catch:{ all -> 0x0272 }
        throw r4;
    L_0x0276:
        r17 = r7;
        r3 = r1.pipRoundVideoView;
        if (r3 == 0) goto L_0x0283;
    L_0x027c:
        r3 = r1.pipRoundVideoView;
        r3.close(r5);
        r1.pipRoundVideoView = r13;
    L_0x0283:
        r3 = new org.telegram.ui.Components.VideoPlayer;	 Catch:{ Exception -> 0x049f }
        r3.<init>();	 Catch:{ Exception -> 0x049f }
        r1.audioPlayer = r3;	 Catch:{ Exception -> 0x049f }
        r3 = r1.audioPlayer;	 Catch:{ Exception -> 0x049f }
        r4 = r1.useFrontSpeaker;	 Catch:{ Exception -> 0x049f }
        if (r4 == 0) goto L_0x0292;	 Catch:{ Exception -> 0x049f }
    L_0x0290:
        r4 = 0;	 Catch:{ Exception -> 0x049f }
        goto L_0x0293;	 Catch:{ Exception -> 0x049f }
    L_0x0292:
        r4 = 3;	 Catch:{ Exception -> 0x049f }
    L_0x0293:
        r3.setStreamType(r4);	 Catch:{ Exception -> 0x049f }
        r3 = r1.audioPlayer;	 Catch:{ Exception -> 0x049f }
        r4 = new org.telegram.messenger.MediaController$20;	 Catch:{ Exception -> 0x049f }
        r4.<init>(r2);	 Catch:{ Exception -> 0x049f }
        r3.setDelegate(r4);	 Catch:{ Exception -> 0x049f }
        if (r8 == 0) goto L_0x02c2;	 Catch:{ Exception -> 0x049f }
    L_0x02a2:
        r3 = r2.mediaExists;	 Catch:{ Exception -> 0x049f }
        if (r3 != 0) goto L_0x02b3;
    L_0x02a6:
        r7 = r17;
        if (r9 == r7) goto L_0x02b5;
    L_0x02aa:
        r3 = new org.telegram.messenger.MediaController$21;	 Catch:{ Exception -> 0x049c }
        r3.<init>(r2);	 Catch:{ Exception -> 0x049c }
        org.telegram.messenger.AndroidUtilities.runOnUIThread(r3);	 Catch:{ Exception -> 0x049c }
        goto L_0x02b5;	 Catch:{ Exception -> 0x049c }
    L_0x02b3:
        r7 = r17;	 Catch:{ Exception -> 0x049c }
    L_0x02b5:
        r3 = r1.audioPlayer;	 Catch:{ Exception -> 0x049c }
        r4 = android.net.Uri.fromFile(r9);	 Catch:{ Exception -> 0x049c }
        r6 = "other";	 Catch:{ Exception -> 0x049c }
        r3.preparePlayer(r4, r6);	 Catch:{ Exception -> 0x049c }
        goto L_0x0348;	 Catch:{ Exception -> 0x049c }
    L_0x02c2:
        r7 = r17;	 Catch:{ Exception -> 0x049c }
        r3 = r26.getDocument();	 Catch:{ Exception -> 0x049c }
        r4 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x049c }
        r4.<init>();	 Catch:{ Exception -> 0x049c }
        r6 = "?account=";	 Catch:{ Exception -> 0x049c }
        r4.append(r6);	 Catch:{ Exception -> 0x049c }
        r6 = r2.currentAccount;	 Catch:{ Exception -> 0x049c }
        r4.append(r6);	 Catch:{ Exception -> 0x049c }
        r6 = "&id=";	 Catch:{ Exception -> 0x049c }
        r4.append(r6);	 Catch:{ Exception -> 0x049c }
        r11 = r3.id;	 Catch:{ Exception -> 0x049c }
        r4.append(r11);	 Catch:{ Exception -> 0x049c }
        r6 = "&hash=";	 Catch:{ Exception -> 0x049c }
        r4.append(r6);	 Catch:{ Exception -> 0x049c }
        r11 = r3.access_hash;	 Catch:{ Exception -> 0x049c }
        r4.append(r11);	 Catch:{ Exception -> 0x049c }
        r6 = "&dc=";	 Catch:{ Exception -> 0x049c }
        r4.append(r6);	 Catch:{ Exception -> 0x049c }
        r6 = r3.dc_id;	 Catch:{ Exception -> 0x049c }
        r4.append(r6);	 Catch:{ Exception -> 0x049c }
        r6 = "&size=";	 Catch:{ Exception -> 0x049c }
        r4.append(r6);	 Catch:{ Exception -> 0x049c }
        r6 = r3.size;	 Catch:{ Exception -> 0x049c }
        r4.append(r6);	 Catch:{ Exception -> 0x049c }
        r6 = "&mime=";	 Catch:{ Exception -> 0x049c }
        r4.append(r6);	 Catch:{ Exception -> 0x049c }
        r6 = r3.mime_type;	 Catch:{ Exception -> 0x049c }
        r11 = "UTF-8";	 Catch:{ Exception -> 0x049c }
        r6 = java.net.URLEncoder.encode(r6, r11);	 Catch:{ Exception -> 0x049c }
        r4.append(r6);	 Catch:{ Exception -> 0x049c }
        r6 = "&name=";	 Catch:{ Exception -> 0x049c }
        r4.append(r6);	 Catch:{ Exception -> 0x049c }
        r6 = org.telegram.messenger.FileLoader.getDocumentFileName(r3);	 Catch:{ Exception -> 0x049c }
        r11 = "UTF-8";	 Catch:{ Exception -> 0x049c }
        r6 = java.net.URLEncoder.encode(r6, r11);	 Catch:{ Exception -> 0x049c }
        r4.append(r6);	 Catch:{ Exception -> 0x049c }
        r4 = r4.toString();	 Catch:{ Exception -> 0x049c }
        r6 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x049c }
        r6.<init>();	 Catch:{ Exception -> 0x049c }
        r11 = "tg://";	 Catch:{ Exception -> 0x049c }
        r6.append(r11);	 Catch:{ Exception -> 0x049c }
        r11 = r26.getFileName();	 Catch:{ Exception -> 0x049c }
        r6.append(r11);	 Catch:{ Exception -> 0x049c }
        r6.append(r4);	 Catch:{ Exception -> 0x049c }
        r6 = r6.toString();	 Catch:{ Exception -> 0x049c }
        r6 = android.net.Uri.parse(r6);	 Catch:{ Exception -> 0x049c }
        r11 = r1.audioPlayer;	 Catch:{ Exception -> 0x049c }
        r12 = "other";	 Catch:{ Exception -> 0x049c }
        r11.preparePlayer(r6, r12);	 Catch:{ Exception -> 0x049c }
    L_0x0348:
        r3 = r1.audioPlayer;	 Catch:{ Exception -> 0x049c }
        r3.play();	 Catch:{ Exception -> 0x049c }
        r3 = r26.isVoice();	 Catch:{ Exception -> 0x049c }
        if (r3 == 0) goto L_0x0360;	 Catch:{ Exception -> 0x049c }
    L_0x0353:
        r1.audioInfo = r13;	 Catch:{ Exception -> 0x049c }
        r3 = r1.playlist;	 Catch:{ Exception -> 0x049c }
        r3.clear();	 Catch:{ Exception -> 0x049c }
        r3 = r1.shuffledPlaylist;	 Catch:{ Exception -> 0x049c }
        r3.clear();	 Catch:{ Exception -> 0x049c }
        goto L_0x036c;
    L_0x0360:
        r3 = org.telegram.messenger.audioinfo.AudioInfo.getAudioInfo(r9);	 Catch:{ Exception -> 0x0367 }
        r1.audioInfo = r3;	 Catch:{ Exception -> 0x0367 }
        goto L_0x036c;
    L_0x0367:
        r0 = move-exception;
        r3 = r0;
        org.telegram.messenger.FileLog.e(r3);	 Catch:{ Exception -> 0x049c }
    L_0x036d:
        r25.checkAudioFocus(r26);
        r25.setPlayerVolume();
        r3 = 0;
        r1.isPaused = r3;
        r3 = 0;
        r1.lastProgress = r3;
        r1.lastPlayPcm = r3;
        r1.playingMessageObject = r2;
        r3 = org.telegram.messenger.SharedConfig.raiseToSpeak;
        if (r3 != 0) goto L_0x0387;
        r3 = r1.raiseChat;
        r1.startRaiseToEarSensors(r3);
        r3 = r1.playingMessageObject;
        r1.startProgressTimer(r3);
        r3 = r2.currentAccount;
        r3 = org.telegram.messenger.NotificationCenter.getInstance(r3);
        r4 = org.telegram.messenger.NotificationCenter.messagePlayingDidStarted;
        r6 = new java.lang.Object[r5];
        r11 = 0;
        r6[r11] = r2;
        r3.postNotificationName(r4, r6);
        r3 = r1.videoPlayer;
        r4 = 2;
        r11 = -9223372036854775807; // 0x8000000000000001 float:1.4E-45 double:-4.9E-324;
        if (r3 == 0) goto L_0x03fe;
        r3 = r1.playingMessageObject;	 Catch:{ Exception -> 0x03ce }
        r3 = r3.audioProgress;	 Catch:{ Exception -> 0x03ce }
        r6 = 0;	 Catch:{ Exception -> 0x03ce }
        r3 = (r3 > r6 ? 1 : (r3 == r6 ? 0 : -1));	 Catch:{ Exception -> 0x03ce }
        if (r3 == 0) goto L_0x03fc;	 Catch:{ Exception -> 0x03ce }
        r3 = r1.audioPlayer;	 Catch:{ Exception -> 0x03ce }
        r13 = r3.getDuration();	 Catch:{ Exception -> 0x03ce }
        r3 = (r13 > r11 ? 1 : (r13 == r11 ? 0 : -1));	 Catch:{ Exception -> 0x03ce }
        if (r3 != 0) goto L_0x03c0;	 Catch:{ Exception -> 0x03ce }
        r3 = r1.playingMessageObject;	 Catch:{ Exception -> 0x03ce }
        r3 = r3.getDuration();	 Catch:{ Exception -> 0x03ce }
        r13 = (long) r3;	 Catch:{ Exception -> 0x03ce }
        r3 = (float) r13;	 Catch:{ Exception -> 0x03ce }
        r6 = r1.playingMessageObject;	 Catch:{ Exception -> 0x03ce }
        r6 = r6.audioProgress;	 Catch:{ Exception -> 0x03ce }
        r3 = r3 * r6;	 Catch:{ Exception -> 0x03ce }
        r3 = (int) r3;	 Catch:{ Exception -> 0x03ce }
        r6 = r1.videoPlayer;	 Catch:{ Exception -> 0x03ce }
        r11 = (long) r3;	 Catch:{ Exception -> 0x03ce }
        r6.seekTo(r11);	 Catch:{ Exception -> 0x03ce }
        goto L_0x03fc;
    L_0x03ce:
        r0 = move-exception;
        r3 = r0;
        r6 = r1.playingMessageObject;
        r11 = 0;
        r6.audioProgress = r11;
        r6 = r1.playingMessageObject;
        r11 = 0;
        r6.audioProgressSec = r11;
        r6 = r2.currentAccount;
        r6 = org.telegram.messenger.NotificationCenter.getInstance(r6);
        r12 = org.telegram.messenger.NotificationCenter.messagePlayingProgressDidChanged;
        r4 = new java.lang.Object[r4];
        r13 = r1.playingMessageObject;
        r13 = r13.getId();
        r13 = java.lang.Integer.valueOf(r13);
        r4[r11] = r13;
        r11 = java.lang.Integer.valueOf(r11);
        r4[r5] = r11;
        r6.postNotificationName(r12, r4);
        org.telegram.messenger.FileLog.e(r3);
        goto L_0x0470;
        r3 = r1.audioPlayer;
        if (r3 == 0) goto L_0x0455;
        r3 = r1.playingMessageObject;	 Catch:{ Exception -> 0x042a }
        r3 = r3.audioProgress;	 Catch:{ Exception -> 0x042a }
        r6 = 0;	 Catch:{ Exception -> 0x042a }
        r3 = (r3 > r6 ? 1 : (r3 == r6 ? 0 : -1));	 Catch:{ Exception -> 0x042a }
        if (r3 == 0) goto L_0x0454;	 Catch:{ Exception -> 0x042a }
        r3 = r1.audioPlayer;	 Catch:{ Exception -> 0x042a }
        r13 = r3.getDuration();	 Catch:{ Exception -> 0x042a }
        r3 = (r13 > r11 ? 1 : (r13 == r11 ? 0 : -1));	 Catch:{ Exception -> 0x042a }
        if (r3 != 0) goto L_0x041c;	 Catch:{ Exception -> 0x042a }
        r3 = r1.playingMessageObject;	 Catch:{ Exception -> 0x042a }
        r3 = r3.getDuration();	 Catch:{ Exception -> 0x042a }
        r13 = (long) r3;	 Catch:{ Exception -> 0x042a }
        r3 = (float) r13;	 Catch:{ Exception -> 0x042a }
        r6 = r1.playingMessageObject;	 Catch:{ Exception -> 0x042a }
        r6 = r6.audioProgress;	 Catch:{ Exception -> 0x042a }
        r3 = r3 * r6;	 Catch:{ Exception -> 0x042a }
        r3 = (int) r3;	 Catch:{ Exception -> 0x042a }
        r6 = r1.audioPlayer;	 Catch:{ Exception -> 0x042a }
        r11 = (long) r3;	 Catch:{ Exception -> 0x042a }
        r6.seekTo(r11);	 Catch:{ Exception -> 0x042a }
        goto L_0x0454;
    L_0x042a:
        r0 = move-exception;
        r3 = r0;
        r6 = r1.playingMessageObject;
        r6.resetPlayingProgress();
        r6 = r2.currentAccount;
        r6 = org.telegram.messenger.NotificationCenter.getInstance(r6);
        r11 = org.telegram.messenger.NotificationCenter.messagePlayingProgressDidChanged;
        r4 = new java.lang.Object[r4];
        r12 = r1.playingMessageObject;
        r12 = r12.getId();
        r12 = java.lang.Integer.valueOf(r12);
        r13 = 0;
        r4[r13] = r12;
        r12 = java.lang.Integer.valueOf(r13);
        r4[r5] = r12;
        r6.postNotificationName(r11, r4);
        org.telegram.messenger.FileLog.e(r3);
        goto L_0x0470;
        r3 = r1.audioTrackPlayer;
        if (r3 == 0) goto L_0x0470;
        r3 = r1.playingMessageObject;
        r3 = r3.audioProgress;
        r3 = (r3 > r15 ? 1 : (r3 == r15 ? 0 : -1));
        if (r3 != 0) goto L_0x0466;
        r3 = r1.playingMessageObject;
        r4 = 0;
        r3.audioProgress = r4;
        r3 = r1.fileDecodingQueue;
        r4 = new org.telegram.messenger.MediaController$22;
        r4.<init>();
        r3.postRunnable(r4);
        r3 = r1.playingMessageObject;
        r3 = r3.isMusic();
        if (r3 == 0) goto L_0x048d;
        r3 = new android.content.Intent;
        r4 = org.telegram.messenger.ApplicationLoader.applicationContext;
        r6 = org.telegram.messenger.MusicPlayerService.class;
        r3.<init>(r4, r6);
        r4 = org.telegram.messenger.ApplicationLoader.applicationContext;	 Catch:{ Throwable -> 0x0487 }
        r4.startService(r3);	 Catch:{ Throwable -> 0x0487 }
        goto L_0x048c;
    L_0x0487:
        r0 = move-exception;
        r4 = r0;
        org.telegram.messenger.FileLog.e(r4);
        goto L_0x049b;
        r3 = new android.content.Intent;
        r4 = org.telegram.messenger.ApplicationLoader.applicationContext;
        r6 = org.telegram.messenger.MusicPlayerService.class;
        r3.<init>(r4, r6);
        r4 = org.telegram.messenger.ApplicationLoader.applicationContext;
        r4.stopService(r3);
        return r5;
    L_0x049c:
        r0 = move-exception;
        r3 = r0;
        goto L_0x04a3;
    L_0x049f:
        r0 = move-exception;
        r7 = r17;
        r3 = r0;
        org.telegram.messenger.FileLog.e(r3);
        r4 = r2.currentAccount;
        r4 = org.telegram.messenger.NotificationCenter.getInstance(r4);
        r6 = org.telegram.messenger.NotificationCenter.messagePlayingPlayStateChanged;
        r5 = new java.lang.Object[r5];
        r11 = r1.playingMessageObject;
        if (r11 == 0) goto L_0x04bb;
        r11 = r1.playingMessageObject;
        r11 = r11.getId();
        goto L_0x04bc;
        r11 = 0;
        r11 = java.lang.Integer.valueOf(r11);
        r12 = 0;
        r5[r12] = r11;
        r4.postNotificationName(r6, r5);
        r4 = r1.audioPlayer;
        if (r4 == 0) goto L_0x04d7;
        r4 = r1.audioPlayer;
        r4.releasePlayer();
        r1.audioPlayer = r13;
        r1.isPaused = r12;
        r1.playingMessageObject = r13;
        r1.downloadingCurrentMessage = r12;
        return r12;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.telegram.messenger.MediaController.playMessage(org.telegram.messenger.MessageObject):boolean");
    }

    public boolean seekToProgress(org.telegram.messenger.MessageObject r1, float r2) {
        /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.DecodeException: Load method exception in method: org.telegram.messenger.MediaController.seekToProgress(org.telegram.messenger.MessageObject, float):boolean
	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:116)
	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:249)
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
        r0 = r7.audioTrackPlayer;
        r1 = 0;
        if (r0 != 0) goto L_0x000d;
    L_0x0005:
        r0 = r7.audioPlayer;
        if (r0 != 0) goto L_0x000d;
    L_0x0009:
        r0 = r7.videoPlayer;
        if (r0 == 0) goto L_0x005f;
    L_0x000d:
        if (r8 == 0) goto L_0x005f;
    L_0x000f:
        r0 = r7.playingMessageObject;
        if (r0 == 0) goto L_0x005f;
    L_0x0013:
        r0 = r7.isSamePlayingMessage(r8);
        if (r0 != 0) goto L_0x001a;
    L_0x0019:
        goto L_0x005f;
    L_0x001a:
        r0 = r7.audioPlayer;	 Catch:{ Exception -> 0x005a }
        if (r0 == 0) goto L_0x003d;	 Catch:{ Exception -> 0x005a }
    L_0x001e:
        r0 = r7.audioPlayer;	 Catch:{ Exception -> 0x005a }
        r2 = r0.getDuration();	 Catch:{ Exception -> 0x005a }
        r4 = -9223372036854775807; // 0x8000000000000001 float:1.4E-45 double:-4.9E-324;	 Catch:{ Exception -> 0x005a }
        r0 = (r2 > r4 ? 1 : (r2 == r4 ? 0 : -1));	 Catch:{ Exception -> 0x005a }
        if (r0 != 0) goto L_0x0030;	 Catch:{ Exception -> 0x005a }
    L_0x002d:
        r7.seekToProgressPending = r9;	 Catch:{ Exception -> 0x005a }
        goto L_0x003c;	 Catch:{ Exception -> 0x005a }
    L_0x0030:
        r0 = (float) r2;	 Catch:{ Exception -> 0x005a }
        r0 = r0 * r9;	 Catch:{ Exception -> 0x005a }
        r0 = (int) r0;	 Catch:{ Exception -> 0x005a }
        r4 = r7.audioPlayer;	 Catch:{ Exception -> 0x005a }
        r5 = (long) r0;	 Catch:{ Exception -> 0x005a }
        r4.seekTo(r5);	 Catch:{ Exception -> 0x005a }
        r4 = (long) r0;	 Catch:{ Exception -> 0x005a }
        r7.lastProgress = r4;	 Catch:{ Exception -> 0x005a }
    L_0x003c:
        goto L_0x0057;	 Catch:{ Exception -> 0x005a }
    L_0x003d:
        r0 = r7.audioTrackPlayer;	 Catch:{ Exception -> 0x005a }
        if (r0 == 0) goto L_0x0045;	 Catch:{ Exception -> 0x005a }
        r7.seekOpusPlayer(r9);	 Catch:{ Exception -> 0x005a }
        goto L_0x0057;	 Catch:{ Exception -> 0x005a }
        r0 = r7.videoPlayer;	 Catch:{ Exception -> 0x005a }
        if (r0 == 0) goto L_0x0057;	 Catch:{ Exception -> 0x005a }
        r0 = r7.videoPlayer;	 Catch:{ Exception -> 0x005a }
        r2 = r7.videoPlayer;	 Catch:{ Exception -> 0x005a }
        r2 = r2.getDuration();	 Catch:{ Exception -> 0x005a }
        r2 = (float) r2;	 Catch:{ Exception -> 0x005a }
        r2 = r2 * r9;	 Catch:{ Exception -> 0x005a }
        r2 = (long) r2;	 Catch:{ Exception -> 0x005a }
        r0.seekTo(r2);	 Catch:{ Exception -> 0x005a }
        r0 = 1;
        return r0;
    L_0x005a:
        r0 = move-exception;
        org.telegram.messenger.FileLog.e(r0);
        return r1;
    L_0x005f:
        return r1;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.telegram.messenger.MediaController.seekToProgress(org.telegram.messenger.MessageObject, float):boolean");
    }

    private void readSms() {
    }

    public static void checkGallery() {
        if (VERSION.SDK_INT >= 24) {
            if (allPhotosAlbumEntry != null) {
                final int prevSize = allPhotosAlbumEntry.photos.size();
                Utilities.globalQueue.postRunnable(new Runnable() {
                    /* JADX WARNING: inconsistent code. */
                    /* Code decompiled incorrectly, please refer to instructions dump. */
                    @android.annotation.SuppressLint({"NewApi"})
                    public void run() {
                        /*
                        r12 = this;
                        r0 = 0;
                        r1 = 0;
                        r2 = r1;
                        r3 = 1;
                        r4 = 0;
                        r5 = org.telegram.messenger.ApplicationLoader.applicationContext;	 Catch:{ Throwable -> 0x003a }
                        r6 = "android.permission.READ_EXTERNAL_STORAGE";
                        r5 = r5.checkSelfPermission(r6);	 Catch:{ Throwable -> 0x003a }
                        if (r5 != 0) goto L_0x0032;
                    L_0x000f:
                        r5 = org.telegram.messenger.ApplicationLoader.applicationContext;	 Catch:{ Throwable -> 0x003a }
                        r6 = r5.getContentResolver();	 Catch:{ Throwable -> 0x003a }
                        r7 = android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI;	 Catch:{ Throwable -> 0x003a }
                        r8 = new java.lang.String[r3];	 Catch:{ Throwable -> 0x003a }
                        r5 = "COUNT(_id)";
                        r8[r4] = r5;	 Catch:{ Throwable -> 0x003a }
                        r9 = 0;
                        r10 = 0;
                        r11 = 0;
                        r5 = android.provider.MediaStore.Images.Media.query(r6, r7, r8, r9, r10, r11);	 Catch:{ Throwable -> 0x003a }
                        r2 = r5;
                        if (r2 == 0) goto L_0x0032;
                    L_0x0027:
                        r5 = r2.moveToNext();	 Catch:{ Throwable -> 0x003a }
                        if (r5 == 0) goto L_0x0032;
                    L_0x002d:
                        r5 = r2.getInt(r4);	 Catch:{ Throwable -> 0x003a }
                        r0 = r0 + r5;
                    L_0x0032:
                        if (r2 == 0) goto L_0x0041;
                    L_0x0034:
                        r2.close();
                        goto L_0x0041;
                    L_0x0038:
                        r1 = move-exception;
                        goto L_0x009b;
                    L_0x003a:
                        r5 = move-exception;
                        org.telegram.messenger.FileLog.e(r5);	 Catch:{ all -> 0x0038 }
                        if (r2 == 0) goto L_0x0041;
                    L_0x0040:
                        goto L_0x0034;
                    L_0x0041:
                        r5 = org.telegram.messenger.ApplicationLoader.applicationContext;	 Catch:{ Throwable -> 0x0076 }
                        r6 = "android.permission.READ_EXTERNAL_STORAGE";
                        r5 = r5.checkSelfPermission(r6);	 Catch:{ Throwable -> 0x0076 }
                        if (r5 != 0) goto L_0x006e;
                    L_0x004b:
                        r5 = org.telegram.messenger.ApplicationLoader.applicationContext;	 Catch:{ Throwable -> 0x0076 }
                        r6 = r5.getContentResolver();	 Catch:{ Throwable -> 0x0076 }
                        r7 = android.provider.MediaStore.Video.Media.EXTERNAL_CONTENT_URI;	 Catch:{ Throwable -> 0x0076 }
                        r8 = new java.lang.String[r3];	 Catch:{ Throwable -> 0x0076 }
                        r3 = "COUNT(_id)";
                        r8[r4] = r3;	 Catch:{ Throwable -> 0x0076 }
                        r9 = 0;
                        r10 = 0;
                        r11 = 0;
                        r3 = android.provider.MediaStore.Images.Media.query(r6, r7, r8, r9, r10, r11);	 Catch:{ Throwable -> 0x0076 }
                        r2 = r3;
                        if (r2 == 0) goto L_0x006e;
                    L_0x0063:
                        r3 = r2.moveToNext();	 Catch:{ Throwable -> 0x0076 }
                        if (r3 == 0) goto L_0x006e;
                    L_0x0069:
                        r3 = r2.getInt(r4);	 Catch:{ Throwable -> 0x0076 }
                        r0 = r0 + r3;
                    L_0x006e:
                        if (r2 == 0) goto L_0x007d;
                    L_0x0070:
                        r2.close();
                        goto L_0x007d;
                    L_0x0074:
                        r1 = move-exception;
                        goto L_0x0095;
                    L_0x0076:
                        r3 = move-exception;
                        org.telegram.messenger.FileLog.e(r3);	 Catch:{ all -> 0x0074 }
                        if (r2 == 0) goto L_0x007d;
                    L_0x007c:
                        goto L_0x0070;
                    L_0x007d:
                        r3 = r0;
                        if (r3 == r0) goto L_0x0094;
                    L_0x0081:
                        r3 = org.telegram.messenger.MediaController.refreshGalleryRunnable;
                        if (r3 == 0) goto L_0x0091;
                    L_0x0087:
                        r3 = org.telegram.messenger.MediaController.refreshGalleryRunnable;
                        org.telegram.messenger.AndroidUtilities.cancelRunOnUIThread(r3);
                        org.telegram.messenger.MediaController.refreshGalleryRunnable = r1;
                    L_0x0091:
                        org.telegram.messenger.MediaController.loadGalleryPhotosAlbums(r4);
                    L_0x0094:
                        return;
                    L_0x0095:
                        if (r2 == 0) goto L_0x009a;
                    L_0x0097:
                        r2.close();
                    L_0x009a:
                        throw r1;
                    L_0x009b:
                        if (r2 == 0) goto L_0x00a0;
                    L_0x009d:
                        r2.close();
                    L_0x00a0:
                        throw r1;
                        */
                        throw new UnsupportedOperationException("Method not decompiled: org.telegram.messenger.MediaController.2.run():void");
                    }
                }, AdaptiveTrackSelection.DEFAULT_MIN_TIME_BETWEEN_BUFFER_REEVALUTATION_MS);
            }
        }
    }

    public static MediaController getInstance() {
        MediaController localInstance = Instance;
        if (localInstance == null) {
            synchronized (MediaController.class) {
                localInstance = Instance;
                if (localInstance == null) {
                    MediaController mediaController = new MediaController();
                    localInstance = mediaController;
                    Instance = mediaController;
                }
            }
        }
        return localInstance;
    }

    public MediaController() {
        this.recordQueue.setPriority(10);
        this.fileEncodingQueue = new DispatchQueue("fileEncodingQueue");
        this.fileEncodingQueue.setPriority(10);
        this.playerQueue = new DispatchQueue("playerQueue");
        this.fileDecodingQueue = new DispatchQueue("fileDecodingQueue");
        this.recordQueue.postRunnable(new Runnable() {
            public void run() {
                /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.DecodeException: Load method exception in method: org.telegram.messenger.MediaController.3.run():void
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
                r0 = org.telegram.messenger.MediaController.this;	 Catch:{ Exception -> 0x0075 }
                r1 = 16000; // 0x3e80 float:2.2421E-41 double:7.905E-320;	 Catch:{ Exception -> 0x0075 }
                r2 = 16;	 Catch:{ Exception -> 0x0075 }
                r3 = 2;	 Catch:{ Exception -> 0x0075 }
                r1 = android.media.AudioRecord.getMinBufferSize(r1, r2, r3);	 Catch:{ Exception -> 0x0075 }
                r0.recordBufferSize = r1;	 Catch:{ Exception -> 0x0075 }
                r0 = org.telegram.messenger.MediaController.this;	 Catch:{ Exception -> 0x0075 }
                r0 = r0.recordBufferSize;	 Catch:{ Exception -> 0x0075 }
                if (r0 > 0) goto L_0x001d;	 Catch:{ Exception -> 0x0075 }
            L_0x0016:
                r0 = org.telegram.messenger.MediaController.this;	 Catch:{ Exception -> 0x0075 }
                r1 = 1280; // 0x500 float:1.794E-42 double:6.324E-321;	 Catch:{ Exception -> 0x0075 }
                r0.recordBufferSize = r1;	 Catch:{ Exception -> 0x0075 }
            L_0x001d:
                r0 = org.telegram.messenger.MediaController.this;	 Catch:{ Exception -> 0x0075 }
                r1 = 48000; // 0xbb80 float:6.7262E-41 double:2.3715E-319;	 Catch:{ Exception -> 0x0075 }
                r2 = 4;	 Catch:{ Exception -> 0x0075 }
                r1 = android.media.AudioTrack.getMinBufferSize(r1, r2, r3);	 Catch:{ Exception -> 0x0075 }
                r0.playerBufferSize = r1;	 Catch:{ Exception -> 0x0075 }
                r0 = org.telegram.messenger.MediaController.this;	 Catch:{ Exception -> 0x0075 }
                r0 = r0.playerBufferSize;	 Catch:{ Exception -> 0x0075 }
                if (r0 > 0) goto L_0x0039;	 Catch:{ Exception -> 0x0075 }
            L_0x0032:
                r0 = org.telegram.messenger.MediaController.this;	 Catch:{ Exception -> 0x0075 }
                r1 = 3840; // 0xf00 float:5.381E-42 double:1.897E-320;	 Catch:{ Exception -> 0x0075 }
                r0.playerBufferSize = r1;	 Catch:{ Exception -> 0x0075 }
            L_0x0039:
                r0 = 0;	 Catch:{ Exception -> 0x0075 }
                r1 = r0;	 Catch:{ Exception -> 0x0075 }
                r2 = 5;	 Catch:{ Exception -> 0x0075 }
                if (r1 >= r2) goto L_0x0057;	 Catch:{ Exception -> 0x0075 }
            L_0x003e:
                r2 = 4096; // 0x1000 float:5.74E-42 double:2.0237E-320;	 Catch:{ Exception -> 0x0075 }
                r2 = java.nio.ByteBuffer.allocateDirect(r2);	 Catch:{ Exception -> 0x0075 }
                r3 = java.nio.ByteOrder.nativeOrder();	 Catch:{ Exception -> 0x0075 }
                r2.order(r3);	 Catch:{ Exception -> 0x0075 }
                r3 = org.telegram.messenger.MediaController.this;	 Catch:{ Exception -> 0x0075 }
                r3 = r3.recordBuffers;	 Catch:{ Exception -> 0x0075 }
                r3.add(r2);	 Catch:{ Exception -> 0x0075 }
                r1 = r1 + 1;	 Catch:{ Exception -> 0x0075 }
                goto L_0x003b;	 Catch:{ Exception -> 0x0075 }
                r1 = 3;	 Catch:{ Exception -> 0x0075 }
                if (r0 >= r1) goto L_0x0074;	 Catch:{ Exception -> 0x0075 }
                r1 = org.telegram.messenger.MediaController.this;	 Catch:{ Exception -> 0x0075 }
                r1 = r1.freePlayerBuffers;	 Catch:{ Exception -> 0x0075 }
                r2 = new org.telegram.messenger.MediaController$AudioBuffer;	 Catch:{ Exception -> 0x0075 }
                r3 = org.telegram.messenger.MediaController.this;	 Catch:{ Exception -> 0x0075 }
                r4 = org.telegram.messenger.MediaController.this;	 Catch:{ Exception -> 0x0075 }
                r4 = r4.playerBufferSize;	 Catch:{ Exception -> 0x0075 }
                r2.<init>(r4);	 Catch:{ Exception -> 0x0075 }
                r1.add(r2);	 Catch:{ Exception -> 0x0075 }
                r0 = r0 + 1;
                goto L_0x0058;
                goto L_0x0079;
            L_0x0075:
                r0 = move-exception;
                org.telegram.messenger.FileLog.e(r0);
                return;
                */
                throw new UnsupportedOperationException("Method not decompiled: org.telegram.messenger.MediaController.3.run():void");
            }
        });
        Utilities.globalQueue.postRunnable(new Runnable() {
            public void run() {
                try {
                    MediaController.this.sensorManager = (SensorManager) ApplicationLoader.applicationContext.getSystemService("sensor");
                    MediaController.this.linearSensor = MediaController.this.sensorManager.getDefaultSensor(10);
                    MediaController.this.gravitySensor = MediaController.this.sensorManager.getDefaultSensor(9);
                    if (MediaController.this.linearSensor == null || MediaController.this.gravitySensor == null) {
                        if (BuildVars.LOGS_ENABLED) {
                            FileLog.d("gravity or linear sensor not found");
                        }
                        MediaController.this.accelerometerSensor = MediaController.this.sensorManager.getDefaultSensor(1);
                        MediaController.this.linearSensor = null;
                        MediaController.this.gravitySensor = null;
                    }
                    MediaController.this.proximitySensor = MediaController.this.sensorManager.getDefaultSensor(8);
                    MediaController.this.proximityWakeLock = ((PowerManager) ApplicationLoader.applicationContext.getSystemService("power")).newWakeLock(32, "proximity");
                } catch (Throwable e) {
                    FileLog.e(e);
                }
                try {
                    PhoneStateListener phoneStateListener = new PhoneStateListener() {
                        public void onCallStateChanged(final int state, String incomingNumber) {
                            AndroidUtilities.runOnUIThread(new Runnable() {
                                public void run() {
                                    EmbedBottomSheet embedBottomSheet;
                                    if (state == 1) {
                                        if (MediaController.this.isPlayingMessage(MediaController.this.playingMessageObject) && !MediaController.this.isMessagePaused()) {
                                            MediaController.this.pauseMessage(MediaController.this.playingMessageObject);
                                        } else if (!(MediaController.this.recordStartRunnable == null && MediaController.this.recordingAudio == null)) {
                                            MediaController.this.stopRecording(2);
                                        }
                                        embedBottomSheet = EmbedBottomSheet.getInstance();
                                        if (embedBottomSheet != null) {
                                            embedBottomSheet.pause();
                                        }
                                        MediaController.this.callInProgress = true;
                                    } else if (state == 0) {
                                        MediaController.this.callInProgress = false;
                                    } else if (state == 2) {
                                        embedBottomSheet = EmbedBottomSheet.getInstance();
                                        if (embedBottomSheet != null) {
                                            embedBottomSheet.pause();
                                        }
                                        MediaController.this.callInProgress = true;
                                    }
                                }
                            });
                        }
                    };
                    TelephonyManager mgr = (TelephonyManager) ApplicationLoader.applicationContext.getSystemService("phone");
                    if (mgr != null) {
                        mgr.listen(phoneStateListener, 32);
                    }
                } catch (Throwable e2) {
                    FileLog.e(e2);
                }
            }
        });
        this.fileBuffer = ByteBuffer.allocateDirect(1920);
        AndroidUtilities.runOnUIThread(new Runnable() {
            public void run() {
                for (int a = 0; a < 3; a++) {
                    NotificationCenter.getInstance(a).addObserver(MediaController.this, NotificationCenter.FileDidLoaded);
                    NotificationCenter.getInstance(a).addObserver(MediaController.this, NotificationCenter.httpFileDidLoaded);
                    NotificationCenter.getInstance(a).addObserver(MediaController.this, NotificationCenter.didReceivedNewMessages);
                    NotificationCenter.getInstance(a).addObserver(MediaController.this, NotificationCenter.messagesDeleted);
                    NotificationCenter.getInstance(a).addObserver(MediaController.this, NotificationCenter.removeAllMessagesFromDialog);
                    NotificationCenter.getInstance(a).addObserver(MediaController.this, NotificationCenter.musicDidLoaded);
                    NotificationCenter.getGlobalInstance().addObserver(MediaController.this, NotificationCenter.playerDidStartPlaying);
                }
            }
        });
        this.mediaProjections = new String[]{"_data", "_display_name", "bucket_display_name", "datetaken", "title", "width", "height"};
        ContentResolver contentResolver = ApplicationLoader.applicationContext.getContentResolver();
        try {
            contentResolver.registerContentObserver(Media.EXTERNAL_CONTENT_URI, true, new GalleryObserverExternal());
        } catch (Throwable e) {
            FileLog.e(e);
        }
        try {
            contentResolver.registerContentObserver(Media.INTERNAL_CONTENT_URI, true, new GalleryObserverInternal());
        } catch (Throwable e2) {
            FileLog.e(e2);
        }
        try {
            contentResolver.registerContentObserver(Video.Media.EXTERNAL_CONTENT_URI, true, new GalleryObserverExternal());
        } catch (Throwable e22) {
            FileLog.e(e22);
        }
        try {
            contentResolver.registerContentObserver(Video.Media.INTERNAL_CONTENT_URI, true, new GalleryObserverInternal());
        } catch (Throwable e222) {
            FileLog.e(e222);
        }
    }

    public void onAudioFocusChange(int focusChange) {
        if (focusChange == -1) {
            if (isPlayingMessage(getPlayingMessageObject()) && !isMessagePaused()) {
                pauseMessage(this.playingMessageObject);
            }
            this.hasAudioFocus = 0;
            this.audioFocus = 0;
        } else if (focusChange == 1) {
            this.audioFocus = 2;
            if (this.resumeAudioOnFocusGain) {
                this.resumeAudioOnFocusGain = false;
                if (isPlayingMessage(getPlayingMessageObject()) && isMessagePaused()) {
                    playMessage(getPlayingMessageObject());
                }
            }
        } else if (focusChange == -3) {
            this.audioFocus = 1;
        } else if (focusChange == -2) {
            this.audioFocus = 0;
            if (isPlayingMessage(getPlayingMessageObject()) && !isMessagePaused()) {
                pauseMessage(this.playingMessageObject);
                this.resumeAudioOnFocusGain = true;
            }
        }
        setPlayerVolume();
    }

    private void setPlayerVolume() {
        try {
            float volume;
            if (this.audioFocus != 1) {
                volume = VOLUME_NORMAL;
            } else {
                volume = VOLUME_DUCK;
            }
            if (this.audioPlayer != null) {
                this.audioPlayer.setVolume(volume);
            } else if (this.audioTrackPlayer != null) {
                this.audioTrackPlayer.setStereoVolume(volume, volume);
            } else if (this.videoPlayer != null) {
                this.videoPlayer.setVolume(volume);
            }
        } catch (Throwable e) {
            FileLog.e(e);
        }
    }

    private void startProgressTimer(final MessageObject currentPlayingMessageObject) {
        synchronized (this.progressTimerSync) {
            if (this.progressTimer != null) {
                try {
                    this.progressTimer.cancel();
                    this.progressTimer = null;
                } catch (Throwable e) {
                    FileLog.e(e);
                }
            }
            String fileName = currentPlayingMessageObject.getFileName();
            this.progressTimer = new Timer();
            this.progressTimer.schedule(new TimerTask() {
                public void run() {
                    synchronized (MediaController.this.sync) {
                        AndroidUtilities.runOnUIThread(new Runnable() {
                            public void run() {
                                if (!(currentPlayingMessageObject == null || ((MediaController.this.audioPlayer == null && MediaController.this.audioTrackPlayer == null && MediaController.this.videoPlayer == null) || MediaController.this.isPaused))) {
                                    try {
                                        if (MediaController.this.ignoreFirstProgress != 0) {
                                            MediaController.this.ignoreFirstProgress = MediaController.this.ignoreFirstProgress - 1;
                                            return;
                                        }
                                        long duration;
                                        long progress;
                                        float bufferedValue;
                                        float f = 0.0f;
                                        if (MediaController.this.videoPlayer != null) {
                                            duration = MediaController.this.videoPlayer.getDuration();
                                            progress = MediaController.this.videoPlayer.getCurrentPosition();
                                            bufferedValue = ((float) MediaController.this.videoPlayer.getBufferedPosition()) / ((float) duration);
                                            if (duration >= 0) {
                                                f = ((float) progress) / ((float) duration);
                                            }
                                            if (progress < 0 || value >= MediaController.VOLUME_NORMAL) {
                                                return;
                                            }
                                        } else if (MediaController.this.audioPlayer != null) {
                                            duration = MediaController.this.audioPlayer.getDuration();
                                            progress = MediaController.this.audioPlayer.getCurrentPosition();
                                            bufferedValue = (duration == C.TIME_UNSET || duration < 0) ? 0.0f : ((float) progress) / ((float) duration);
                                            float bufferedValue2 = ((float) MediaController.this.audioPlayer.getBufferedPosition()) / ((float) duration);
                                            if (duration != C.TIME_UNSET && progress >= 0) {
                                                if (MediaController.this.seekToProgressPending == 0.0f) {
                                                    f = bufferedValue;
                                                    bufferedValue = bufferedValue2;
                                                }
                                            }
                                            return;
                                        } else {
                                            duration = 0;
                                            progress = (long) ((int) (((float) MediaController.this.lastPlayPcm) / 48.0f));
                                            f = ((float) MediaController.this.lastPlayPcm) / ((float) MediaController.this.currentTotalPcmDuration);
                                            bufferedValue = 0.0f;
                                            if (progress == MediaController.this.lastProgress) {
                                                return;
                                            }
                                        }
                                        MediaController.this.lastProgress = progress;
                                        currentPlayingMessageObject.audioPlayerDuration = (int) (duration / 1000);
                                        currentPlayingMessageObject.audioProgress = f;
                                        currentPlayingMessageObject.audioProgressSec = (int) (MediaController.this.lastProgress / 1000);
                                        currentPlayingMessageObject.bufferedProgress = bufferedValue;
                                        NotificationCenter.getInstance(currentPlayingMessageObject.currentAccount).postNotificationName(NotificationCenter.messagePlayingProgressDidChanged, Integer.valueOf(currentPlayingMessageObject.getId()), Float.valueOf(f));
                                    } catch (Throwable e) {
                                        FileLog.e(e);
                                    }
                                }
                            }
                        });
                    }
                }
            }, 0, 17);
        }
    }

    private void stopProgressTimer() {
        synchronized (this.progressTimerSync) {
            if (this.progressTimer != null) {
                try {
                    this.progressTimer.cancel();
                    this.progressTimer = null;
                } catch (Throwable e) {
                    FileLog.e(e);
                }
            }
        }
    }

    public void cleanup() {
        int a = 0;
        cleanupPlayer(false, true);
        this.audioInfo = null;
        this.playMusicAgain = false;
        while (a < 3) {
            DownloadController.getInstance(a).cleanup();
            a++;
        }
        this.videoConvertQueue.clear();
        this.playlist.clear();
        this.shuffledPlaylist.clear();
        this.generatingWaveform.clear();
        this.voiceMessagesPlaylist = null;
        this.voiceMessagesPlaylistMap = null;
        cancelVideoConvert(null);
    }

    public void startMediaObserver() {
        ApplicationLoader.applicationHandler.removeCallbacks(this.stopMediaObserverRunnable);
        this.startObserverToken++;
        try {
            if (this.internalObserver == null) {
                ContentResolver contentResolver = ApplicationLoader.applicationContext.getContentResolver();
                Uri uri = Media.EXTERNAL_CONTENT_URI;
                ContentObserver externalObserver = new ExternalObserver();
                this.externalObserver = externalObserver;
                contentResolver.registerContentObserver(uri, false, externalObserver);
            }
        } catch (Throwable e) {
            FileLog.e(e);
        }
        try {
            if (this.externalObserver == null) {
                contentResolver = ApplicationLoader.applicationContext.getContentResolver();
                uri = Media.INTERNAL_CONTENT_URI;
                externalObserver = new InternalObserver();
                this.internalObserver = externalObserver;
                contentResolver.registerContentObserver(uri, false, externalObserver);
            }
        } catch (Throwable e2) {
            FileLog.e(e2);
        }
    }

    public void startSmsObserver() {
        try {
            if (this.smsObserver == null) {
                ContentResolver contentResolver = ApplicationLoader.applicationContext.getContentResolver();
                Uri parse = Uri.parse("content://sms");
                ContentObserver smsObserver = new SmsObserver();
                this.smsObserver = smsObserver;
                contentResolver.registerContentObserver(parse, false, smsObserver);
            }
            AndroidUtilities.runOnUIThread(new Runnable() {
                public void run() {
                    try {
                        if (MediaController.this.smsObserver != null) {
                            ApplicationLoader.applicationContext.getContentResolver().unregisterContentObserver(MediaController.this.smsObserver);
                            MediaController.this.smsObserver = null;
                        }
                    } catch (Throwable e) {
                        FileLog.e(e);
                    }
                }
            }, 300000);
        } catch (Throwable e) {
            FileLog.e(e);
        }
    }

    public void stopMediaObserver() {
        if (this.stopMediaObserverRunnable == null) {
            this.stopMediaObserverRunnable = new StopMediaObserverRunnable();
        }
        this.stopMediaObserverRunnable.currentObserverToken = this.startObserverToken;
        ApplicationLoader.applicationHandler.postDelayed(this.stopMediaObserverRunnable, DefaultRenderersFactory.DEFAULT_ALLOWED_VIDEO_JOINING_TIME_MS);
    }

    private void processMediaObserver(Uri uri) {
        MediaController mediaController = this;
        try {
            android.graphics.Point size = AndroidUtilities.getRealScreenSize();
            Cursor cursor = ApplicationLoader.applicationContext.getContentResolver().query(uri, mediaController.mediaProjections, null, null, "date_added DESC LIMIT 1");
            final ArrayList<Long> screenshotDates = new ArrayList();
            if (cursor != null) {
                while (cursor.moveToNext()) {
                    String val = TtmlNode.ANONYMOUS_REGION_ID;
                    String data = cursor.getString(null);
                    String display_name = cursor.getString(1);
                    String album_name = cursor.getString(2);
                    long date = cursor.getLong(3);
                    String title = cursor.getString(4);
                    int photoW = cursor.getInt(5);
                    int photoH = cursor.getInt(6);
                    if ((data != null && data.toLowerCase().contains("screenshot")) || ((display_name != null && display_name.toLowerCase().contains("screenshot")) || ((album_name != null && album_name.toLowerCase().contains("screenshot")) || (title != null && title.toLowerCase().contains("screenshot"))))) {
                        if (photoW == 0 || photoH == 0) {
                            try {
                                Options bmOptions = new Options();
                                bmOptions.inJustDecodeBounds = true;
                                BitmapFactory.decodeFile(data, bmOptions);
                                photoW = bmOptions.outWidth;
                                photoH = bmOptions.outHeight;
                            } catch (Exception e) {
                                screenshotDates.add(Long.valueOf(date));
                            }
                        }
                        if (photoW <= 0 || photoH <= 0 || ((photoW == size.x && photoH == size.y) || (photoH == size.x && photoW == size.y))) {
                            screenshotDates.add(Long.valueOf(date));
                        }
                    }
                }
                cursor.close();
            }
            if (!screenshotDates.isEmpty()) {
                AndroidUtilities.runOnUIThread(new Runnable() {
                    public void run() {
                        NotificationCenter.getInstance(MediaController.this.lastChatAccount).postNotificationName(NotificationCenter.screenshotTook, new Object[0]);
                        MediaController.this.checkScreenshots(screenshotDates);
                    }
                });
            }
        } catch (Throwable e2) {
            FileLog.e(e2);
        }
    }

    private void checkScreenshots(ArrayList<Long> dates) {
        if (!(dates == null || dates.isEmpty() || this.lastChatEnterTime == 0)) {
            if (this.lastUser != null || (this.lastSecretChat instanceof TL_encryptedChat)) {
                boolean send = false;
                for (int a = 0; a < dates.size(); a++) {
                    Long date = (Long) dates.get(a);
                    if (this.lastMediaCheckTime == 0 || date.longValue() > this.lastMediaCheckTime) {
                        if (date.longValue() >= this.lastChatEnterTime && (this.lastChatLeaveTime == 0 || date.longValue() <= this.lastChatLeaveTime + AdaptiveTrackSelection.DEFAULT_MIN_TIME_BETWEEN_BUFFER_REEVALUTATION_MS)) {
                            this.lastMediaCheckTime = Math.max(this.lastMediaCheckTime, date.longValue());
                            send = true;
                        }
                    }
                }
                if (send) {
                    if (this.lastSecretChat != null) {
                        SecretChatHelper.getInstance(this.lastChatAccount).sendScreenshotMessage(this.lastSecretChat, this.lastChatVisibleMessages, null);
                    } else {
                        SendMessagesHelper.getInstance(this.lastChatAccount).sendScreenshotMessage(this.lastUser, this.lastMessageId, null);
                    }
                }
            }
        }
    }

    public void setLastVisibleMessageIds(int account, long enterTime, long leaveTime, User user, EncryptedChat encryptedChat, ArrayList<Long> visibleMessages, int visibleMessage) {
        this.lastChatEnterTime = enterTime;
        this.lastChatLeaveTime = leaveTime;
        this.lastChatAccount = account;
        this.lastSecretChat = encryptedChat;
        this.lastUser = user;
        this.lastMessageId = visibleMessage;
        this.lastChatVisibleMessages = visibleMessages;
    }

    private void checkDecoderQueue() {
        this.fileDecodingQueue.postRunnable(new Runnable() {
            public void run() {
                if (MediaController.this.decodingFinished) {
                    MediaController.this.checkPlayerQueue();
                    return;
                }
                boolean was = false;
                while (true) {
                    AudioBuffer buffer = null;
                    synchronized (MediaController.this.playerSync) {
                        if (!MediaController.this.freePlayerBuffers.isEmpty()) {
                            buffer = (AudioBuffer) MediaController.this.freePlayerBuffers.get(0);
                            MediaController.this.freePlayerBuffers.remove(0);
                        }
                        if (!MediaController.this.usedPlayerBuffers.isEmpty()) {
                            was = true;
                        }
                    }
                    if (buffer == null) {
                        break;
                    }
                    MediaController.this.readOpusFile(buffer.buffer, MediaController.this.playerBufferSize, MediaController.readArgs);
                    buffer.size = MediaController.readArgs[0];
                    buffer.pcmOffset = (long) MediaController.readArgs[1];
                    buffer.finished = MediaController.readArgs[2];
                    if (buffer.finished == 1) {
                        MediaController.this.decodingFinished = true;
                    }
                    if (buffer.size == 0) {
                        break;
                    }
                    buffer.buffer.rewind();
                    buffer.buffer.get(buffer.bufferBytes);
                    synchronized (MediaController.this.playerSync) {
                        MediaController.this.usedPlayerBuffers.add(buffer);
                    }
                    was = true;
                }
                synchronized (MediaController.this.playerSync) {
                    MediaController.this.freePlayerBuffers.add(buffer);
                }
                if (was) {
                    MediaController.this.checkPlayerQueue();
                }
            }
        });
    }

    private void checkPlayerQueue() {
        this.playerQueue.postRunnable(new Runnable() {
            /* JADX WARNING: inconsistent code. */
            /* Code decompiled incorrectly, please refer to instructions dump. */
            public void run() {
                /*
                r13 = this;
                r0 = org.telegram.messenger.MediaController.this;
                r0 = r0.playerObjectSync;
                monitor-enter(r0);
                r1 = org.telegram.messenger.MediaController.this;	 Catch:{ all -> 0x00b9 }
                r1 = r1.audioTrackPlayer;	 Catch:{ all -> 0x00b9 }
                if (r1 == 0) goto L_0x00b7;
            L_0x000f:
                r1 = org.telegram.messenger.MediaController.this;	 Catch:{ all -> 0x00b9 }
                r1 = r1.audioTrackPlayer;	 Catch:{ all -> 0x00b9 }
                r1 = r1.getPlayState();	 Catch:{ all -> 0x00b9 }
                r2 = 3;
                if (r1 == r2) goto L_0x001e;
            L_0x001c:
                goto L_0x00b7;
            L_0x001e:
                monitor-exit(r0);	 Catch:{ all -> 0x00b9 }
                r0 = 0;
                r1 = org.telegram.messenger.MediaController.this;
                r1 = r1.playerSync;
                monitor-enter(r1);
                r2 = org.telegram.messenger.MediaController.this;	 Catch:{ all -> 0x00b4 }
                r2 = r2.usedPlayerBuffers;	 Catch:{ all -> 0x00b4 }
                r2 = r2.isEmpty();	 Catch:{ all -> 0x00b4 }
                r3 = 0;
                if (r2 != 0) goto L_0x004a;
            L_0x0034:
                r2 = org.telegram.messenger.MediaController.this;	 Catch:{ all -> 0x00b4 }
                r2 = r2.usedPlayerBuffers;	 Catch:{ all -> 0x00b4 }
                r2 = r2.get(r3);	 Catch:{ all -> 0x00b4 }
                r2 = (org.telegram.messenger.MediaController.AudioBuffer) r2;	 Catch:{ all -> 0x00b4 }
                r0 = r2;
                r2 = org.telegram.messenger.MediaController.this;	 Catch:{ all -> 0x00b4 }
                r2 = r2.usedPlayerBuffers;	 Catch:{ all -> 0x00b4 }
                r2.remove(r3);	 Catch:{ all -> 0x00b4 }
            L_0x004a:
                monitor-exit(r1);	 Catch:{ all -> 0x00b4 }
                r1 = 1;
                if (r0 == 0) goto L_0x008f;
            L_0x004e:
                r2 = r3;
                r4 = org.telegram.messenger.MediaController.this;	 Catch:{ Exception -> 0x005f }
                r4 = r4.audioTrackPlayer;	 Catch:{ Exception -> 0x005f }
                r5 = r0.bufferBytes;	 Catch:{ Exception -> 0x005f }
                r6 = r0.size;	 Catch:{ Exception -> 0x005f }
                r3 = r4.write(r5, r3, r6);	 Catch:{ Exception -> 0x005f }
                r2 = r3;
                goto L_0x0063;
            L_0x005f:
                r3 = move-exception;
                org.telegram.messenger.FileLog.e(r3);
            L_0x0063:
                r3 = org.telegram.messenger.MediaController.this;
                r3.buffersWrited = r3.buffersWrited + 1;
                if (r2 <= 0) goto L_0x0086;
            L_0x006a:
                r10 = r0.pcmOffset;
                r3 = r0.finished;
                if (r3 != r1) goto L_0x0072;
            L_0x0070:
                r8 = r2;
                goto L_0x0074;
            L_0x0072:
                r3 = -1;
                r8 = r3;
            L_0x0074:
                r3 = org.telegram.messenger.MediaController.this;
                r3 = r3.buffersWrited;
                r12 = new org.telegram.messenger.MediaController$10$1;
                r4 = r12;
                r5 = r13;
                r6 = r10;
                r9 = r3;
                r4.<init>(r6, r8, r9);
                org.telegram.messenger.AndroidUtilities.runOnUIThread(r12);
            L_0x0086:
                r3 = r0.finished;
                if (r3 == r1) goto L_0x008f;
            L_0x008a:
                r3 = org.telegram.messenger.MediaController.this;
                r3.checkPlayerQueue();
            L_0x008f:
                if (r0 == 0) goto L_0x0097;
            L_0x0091:
                if (r0 == 0) goto L_0x009c;
            L_0x0093:
                r2 = r0.finished;
                if (r2 == r1) goto L_0x009c;
            L_0x0097:
                r1 = org.telegram.messenger.MediaController.this;
                r1.checkDecoderQueue();
            L_0x009c:
                if (r0 == 0) goto L_0x00b3;
            L_0x009e:
                r1 = org.telegram.messenger.MediaController.this;
                r1 = r1.playerSync;
                monitor-enter(r1);
                r2 = org.telegram.messenger.MediaController.this;	 Catch:{ all -> 0x00b0 }
                r2 = r2.freePlayerBuffers;	 Catch:{ all -> 0x00b0 }
                r2.add(r0);	 Catch:{ all -> 0x00b0 }
                monitor-exit(r1);	 Catch:{ all -> 0x00b0 }
                goto L_0x00b3;
            L_0x00b0:
                r2 = move-exception;
                monitor-exit(r1);	 Catch:{ all -> 0x00b0 }
                throw r2;
            L_0x00b3:
                return;
            L_0x00b4:
                r2 = move-exception;
                monitor-exit(r1);	 Catch:{ all -> 0x00b4 }
                throw r2;
            L_0x00b7:
                monitor-exit(r0);	 Catch:{ all -> 0x00b9 }
                return;
            L_0x00b9:
                r1 = move-exception;
                monitor-exit(r0);	 Catch:{ all -> 0x00b9 }
                throw r1;
                */
                throw new UnsupportedOperationException("Method not decompiled: org.telegram.messenger.MediaController.10.run():void");
            }
        });
    }

    protected boolean isRecordingAudio() {
        if (this.recordStartRunnable == null) {
            if (this.recordingAudio == null) {
                return false;
            }
        }
        return true;
    }

    private boolean isNearToSensor(float value) {
        return value < 5.0f && value != this.proximitySensor.getMaximumRange();
    }

    public boolean isRecordingOrListeningByProximity() {
        return this.proximityTouched && (isRecordingAudio() || (this.playingMessageObject != null && (this.playingMessageObject.isVoice() || this.playingMessageObject.isRoundVideo())));
    }

    public void onSensorChanged(SensorEvent event) {
        SensorEvent sensorEvent = event;
        if (this.sensorsStarted) {
            if (VoIPService.getSharedInstance() == null) {
                if (sensorEvent.sensor == r0.proximitySensor) {
                    if (BuildVars.LOGS_ENABLED) {
                        StringBuilder stringBuilder = new StringBuilder();
                        stringBuilder.append("proximity changed to ");
                        stringBuilder.append(sensorEvent.values[0]);
                        FileLog.d(stringBuilder.toString());
                    }
                    if (r0.lastProximityValue == -100.0f) {
                        r0.lastProximityValue = sensorEvent.values[0];
                    } else if (r0.lastProximityValue != sensorEvent.values[0]) {
                        r0.proximityHasDifferentValues = true;
                    }
                    if (r0.proximityHasDifferentValues) {
                        r0.proximityTouched = isNearToSensor(sensorEvent.values[0]);
                    }
                } else if (sensorEvent.sensor == r0.accelerometerSensor) {
                    double alpha = r0.lastTimestamp == 0 ? 0.9800000190734863d : 1.0d / ((((double) (sensorEvent.timestamp - r0.lastTimestamp)) / 1.0E9d) + 1.0d);
                    r0.lastTimestamp = sensorEvent.timestamp;
                    r0.gravity[0] = (float) ((((double) r0.gravity[0]) * alpha) + ((1.0d - alpha) * ((double) sensorEvent.values[0])));
                    r0.gravity[1] = (float) ((((double) r0.gravity[1]) * alpha) + ((1.0d - alpha) * ((double) sensorEvent.values[1])));
                    r0.gravity[2] = (float) ((((double) r0.gravity[2]) * alpha) + ((1.0d - alpha) * ((double) sensorEvent.values[2])));
                    r0.gravityFast[0] = (r0.gravity[0] * 0.8f) + (sensorEvent.values[0] * 0.19999999f);
                    r0.gravityFast[1] = (r0.gravity[1] * 0.8f) + (sensorEvent.values[1] * 0.19999999f);
                    r0.gravityFast[2] = (0.8f * r0.gravity[2]) + (0.19999999f * sensorEvent.values[2]);
                    r0.linearAcceleration[0] = sensorEvent.values[0] - r0.gravity[0];
                    r0.linearAcceleration[1] = sensorEvent.values[1] - r0.gravity[1];
                    r0.linearAcceleration[2] = sensorEvent.values[2] - r0.gravity[2];
                } else if (sensorEvent.sensor == r0.linearSensor) {
                    r0.linearAcceleration[0] = sensorEvent.values[0];
                    r0.linearAcceleration[1] = sensorEvent.values[1];
                    r0.linearAcceleration[2] = sensorEvent.values[2];
                } else if (sensorEvent.sensor == r0.gravitySensor) {
                    float[] fArr = r0.gravityFast;
                    float[] fArr2 = r0.gravity;
                    float f = sensorEvent.values[0];
                    fArr2[0] = f;
                    fArr[0] = f;
                    fArr = r0.gravityFast;
                    fArr2 = r0.gravity;
                    f = sensorEvent.values[1];
                    fArr2[1] = f;
                    fArr[1] = f;
                    fArr = r0.gravityFast;
                    fArr2 = r0.gravity;
                    f = sensorEvent.values[2];
                    fArr2[2] = f;
                    fArr[2] = f;
                }
                if (sensorEvent.sensor == r0.linearSensor || sensorEvent.sensor == r0.gravitySensor || sensorEvent.sensor == r0.accelerometerSensor) {
                    boolean goodValue;
                    float val = ((r0.gravity[0] * r0.linearAcceleration[0]) + (r0.gravity[1] * r0.linearAcceleration[1])) + (r0.gravity[2] * r0.linearAcceleration[2]);
                    if (r0.raisedToBack != 6 && ((val > 0.0f && r0.previousAccValue > 0.0f) || (val < 0.0f && r0.previousAccValue < 0.0f))) {
                        int sign;
                        if (val > 0.0f) {
                            goodValue = val > 15.0f;
                            sign = 1;
                        } else {
                            goodValue = val < -15.0f;
                            sign = 2;
                        }
                        if (r0.raisedToTopSign == 0 || r0.raisedToTopSign == sign) {
                            if (!goodValue || r0.raisedToBack != 0 || (r0.raisedToTopSign != 0 && r0.raisedToTopSign != sign)) {
                                if (!goodValue) {
                                    r0.countLess++;
                                }
                                if (!(r0.raisedToTopSign == sign && r0.countLess != 10 && r0.raisedToTop == 6 && r0.raisedToBack == 0)) {
                                    r0.raisedToBack = 0;
                                    r0.raisedToTop = 0;
                                    r0.raisedToTopSign = 0;
                                    r0.countLess = 0;
                                }
                            } else if (r0.raisedToTop < 6 && !r0.proximityTouched) {
                                r0.raisedToTopSign = sign;
                                r0.raisedToTop++;
                                if (r0.raisedToTop == 6) {
                                    r0.countLess = 0;
                                }
                            }
                        } else if (r0.raisedToTop != 6 || !goodValue) {
                            if (!goodValue) {
                                r0.countLess++;
                            }
                            if (!(r0.countLess != 10 && r0.raisedToTop == 6 && r0.raisedToBack == 0)) {
                                r0.raisedToTop = 0;
                                r0.raisedToTopSign = 0;
                                r0.raisedToBack = 0;
                                r0.countLess = 0;
                            }
                        } else if (r0.raisedToBack < 6) {
                            r0.raisedToBack++;
                            if (r0.raisedToBack == 6) {
                                r0.raisedToTop = 0;
                                r0.raisedToTopSign = 0;
                                r0.countLess = 0;
                                r0.timeSinceRaise = System.currentTimeMillis();
                                if (BuildVars.LOGS_ENABLED && BuildVars.DEBUG_PRIVATE_VERSION) {
                                    FileLog.d("motion detected");
                                }
                            }
                        }
                    }
                    r0.previousAccValue = val;
                    goodValue = r0.gravityFast[1] > 2.5f && Math.abs(r0.gravityFast[2]) < 4.0f && Math.abs(r0.gravityFast[0]) > 1.5f;
                    r0.accelerometerVertical = goodValue;
                }
                if (r0.raisedToBack == 6 && r0.accelerometerVertical && r0.proximityTouched && !NotificationsController.audioManager.isWiredHeadsetOn()) {
                    if (BuildVars.LOGS_ENABLED) {
                        FileLog.d("sensor values reached");
                    }
                    if (r0.playingMessageObject == null && r0.recordStartRunnable == null && r0.recordingAudio == null && !PhotoViewer.getInstance().isVisible() && ApplicationLoader.isScreenOn && !r0.inputFieldHasText && r0.allowStartRecord && r0.raiseChat != null && !r0.callInProgress) {
                        if (!r0.raiseToEarRecord) {
                            if (BuildVars.LOGS_ENABLED) {
                                FileLog.d("start record");
                            }
                            r0.useFrontSpeaker = true;
                            if (!r0.raiseChat.playFirstUnreadVoiceMessage()) {
                                r0.raiseToEarRecord = true;
                                r0.useFrontSpeaker = false;
                                startRecording(r0.raiseChat.getCurrentAccount(), r0.raiseChat.getDialogId(), null);
                            }
                            if (r0.useFrontSpeaker) {
                                setUseFrontSpeaker(true);
                            }
                            r0.ignoreOnPause = true;
                            if (!(!r0.proximityHasDifferentValues || r0.proximityWakeLock == null || r0.proximityWakeLock.isHeld())) {
                                r0.proximityWakeLock.acquire();
                            }
                        }
                    } else if (r0.playingMessageObject != null && ((r0.playingMessageObject.isVoice() || r0.playingMessageObject.isRoundVideo()) && !r0.useFrontSpeaker)) {
                        if (BuildVars.LOGS_ENABLED) {
                            FileLog.d("start listen");
                        }
                        if (!(!r0.proximityHasDifferentValues || r0.proximityWakeLock == null || r0.proximityWakeLock.isHeld())) {
                            r0.proximityWakeLock.acquire();
                        }
                        setUseFrontSpeaker(true);
                        startAudioAgain(false);
                        r0.ignoreOnPause = true;
                    }
                    r0.raisedToBack = 0;
                    r0.raisedToTop = 0;
                    r0.raisedToTopSign = 0;
                    r0.countLess = 0;
                } else if (r0.proximityTouched) {
                    if (r0.playingMessageObject != null && ((r0.playingMessageObject.isVoice() || r0.playingMessageObject.isRoundVideo()) && !r0.useFrontSpeaker)) {
                        if (BuildVars.LOGS_ENABLED) {
                            FileLog.d("start listen by proximity only");
                        }
                        if (!(!r0.proximityHasDifferentValues || r0.proximityWakeLock == null || r0.proximityWakeLock.isHeld())) {
                            r0.proximityWakeLock.acquire();
                        }
                        setUseFrontSpeaker(true);
                        startAudioAgain(false);
                        r0.ignoreOnPause = true;
                    }
                } else if (!r0.proximityTouched) {
                    if (r0.raiseToEarRecord) {
                        if (BuildVars.LOGS_ENABLED) {
                            FileLog.d("stop record");
                        }
                        stopRecording(2);
                        r0.raiseToEarRecord = false;
                        r0.ignoreOnPause = false;
                        if (r0.proximityHasDifferentValues && r0.proximityWakeLock != null && r0.proximityWakeLock.isHeld()) {
                            r0.proximityWakeLock.release();
                        }
                    } else if (r0.useFrontSpeaker) {
                        if (BuildVars.LOGS_ENABLED) {
                            FileLog.d("stop listen");
                        }
                        r0.useFrontSpeaker = false;
                        startAudioAgain(true);
                        r0.ignoreOnPause = false;
                        if (r0.proximityHasDifferentValues && r0.proximityWakeLock != null && r0.proximityWakeLock.isHeld()) {
                            r0.proximityWakeLock.release();
                        }
                    }
                }
                if (r0.timeSinceRaise != 0 && r0.raisedToBack == 6 && Math.abs(System.currentTimeMillis() - r0.timeSinceRaise) > 1000) {
                    r0.raisedToBack = 0;
                    r0.raisedToTop = 0;
                    r0.raisedToTopSign = 0;
                    r0.countLess = 0;
                    r0.timeSinceRaise = 0;
                }
            }
        }
    }

    private void setUseFrontSpeaker(boolean value) {
        this.useFrontSpeaker = value;
        AudioManager audioManager = NotificationsController.audioManager;
        if (this.useFrontSpeaker) {
            audioManager.setBluetoothScoOn(false);
            audioManager.setSpeakerphoneOn(false);
            return;
        }
        audioManager.setSpeakerphoneOn(true);
    }

    public void startRecordingIfFromSpeaker() {
        if (this.useFrontSpeaker && this.raiseChat != null) {
            if (this.allowStartRecord) {
                this.raiseToEarRecord = true;
                startRecording(this.raiseChat.getCurrentAccount(), this.raiseChat.getDialogId(), null);
                this.ignoreOnPause = true;
            }
        }
    }

    private void startAudioAgain(boolean paused) {
        if (this.playingMessageObject != null) {
            NotificationCenter instance = NotificationCenter.getInstance(this.playingMessageObject.currentAccount);
            int i = NotificationCenter.audioRouteChanged;
            Object[] objArr = new Object[1];
            int i2 = 0;
            objArr[0] = Boolean.valueOf(this.useFrontSpeaker);
            instance.postNotificationName(i, objArr);
            if (this.videoPlayer != null) {
                VideoPlayer videoPlayer = this.videoPlayer;
                if (!this.useFrontSpeaker) {
                    i2 = 3;
                }
                videoPlayer.setStreamType(i2);
                if (paused) {
                    this.videoPlayer.pause();
                } else {
                    this.videoPlayer.play();
                }
            } else {
                boolean post = this.audioPlayer != null;
                final MessageObject currentMessageObject = this.playingMessageObject;
                float progress = this.playingMessageObject.audioProgress;
                cleanupPlayer(false, true);
                currentMessageObject.audioProgress = progress;
                playMessage(currentMessageObject);
                if (paused) {
                    if (post) {
                        AndroidUtilities.runOnUIThread(new Runnable() {
                            public void run() {
                                MediaController.this.pauseMessage(currentMessageObject);
                            }
                        }, 100);
                    } else {
                        pauseMessage(currentMessageObject);
                    }
                }
            }
        }
    }

    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    public void setInputFieldHasText(boolean value) {
        this.inputFieldHasText = value;
    }

    public void setAllowStartRecord(boolean value) {
        this.allowStartRecord = value;
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void startRaiseToEarSensors(org.telegram.ui.ChatActivity r8) {
        /*
        r7 = this;
        if (r8 == 0) goto L_0x0075;
    L_0x0002:
        r0 = r7.accelerometerSensor;
        if (r0 != 0) goto L_0x000e;
    L_0x0006:
        r0 = r7.gravitySensor;
        if (r0 == 0) goto L_0x0075;
    L_0x000a:
        r0 = r7.linearAcceleration;
        if (r0 == 0) goto L_0x0075;
    L_0x000e:
        r0 = r7.proximitySensor;
        if (r0 != 0) goto L_0x0013;
    L_0x0012:
        goto L_0x0075;
    L_0x0013:
        r7.raiseChat = r8;
        r0 = org.telegram.messenger.SharedConfig.raiseToSpeak;
        if (r0 != 0) goto L_0x002e;
    L_0x0019:
        r0 = r7.playingMessageObject;
        if (r0 == 0) goto L_0x002d;
    L_0x001d:
        r0 = r7.playingMessageObject;
        r0 = r0.isVoice();
        if (r0 != 0) goto L_0x002e;
    L_0x0025:
        r0 = r7.playingMessageObject;
        r0 = r0.isRoundVideo();
        if (r0 != 0) goto L_0x002e;
    L_0x002d:
        return;
    L_0x002e:
        r0 = r7.sensorsStarted;
        if (r0 != 0) goto L_0x0074;
    L_0x0032:
        r0 = r7.gravity;
        r1 = r7.gravity;
        r2 = r7.gravity;
        r3 = 2;
        r4 = 0;
        r2[r3] = r4;
        r2 = 1;
        r1[r2] = r4;
        r1 = 0;
        r0[r1] = r4;
        r0 = r7.linearAcceleration;
        r5 = r7.linearAcceleration;
        r6 = r7.linearAcceleration;
        r6[r3] = r4;
        r5[r2] = r4;
        r0[r1] = r4;
        r0 = r7.gravityFast;
        r5 = r7.gravityFast;
        r6 = r7.gravityFast;
        r6[r3] = r4;
        r5[r2] = r4;
        r0[r1] = r4;
        r5 = 0;
        r7.lastTimestamp = r5;
        r7.previousAccValue = r4;
        r7.raisedToTop = r1;
        r7.raisedToTopSign = r1;
        r7.countLess = r1;
        r7.raisedToBack = r1;
        r0 = org.telegram.messenger.Utilities.globalQueue;
        r1 = new org.telegram.messenger.MediaController$12;
        r1.<init>();
        r0.postRunnable(r1);
        r7.sensorsStarted = r2;
    L_0x0074:
        return;
    L_0x0075:
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.telegram.messenger.MediaController.startRaiseToEarSensors(org.telegram.ui.ChatActivity):void");
    }

    public void stopRaiseToEarSensors(ChatActivity chatActivity) {
        if (this.ignoreOnPause) {
            this.ignoreOnPause = false;
            return;
        }
        stopRecording(0);
        if (!(!this.sensorsStarted || this.ignoreOnPause || ((this.accelerometerSensor == null && (this.gravitySensor == null || this.linearAcceleration == null)) || this.proximitySensor == null))) {
            if (this.raiseChat == chatActivity) {
                this.raiseChat = null;
                this.sensorsStarted = false;
                this.accelerometerVertical = false;
                this.proximityTouched = false;
                this.raiseToEarRecord = false;
                this.useFrontSpeaker = false;
                Utilities.globalQueue.postRunnable(new Runnable() {
                    public void run() {
                        if (MediaController.this.linearSensor != null) {
                            MediaController.this.sensorManager.unregisterListener(MediaController.this, MediaController.this.linearSensor);
                        }
                        if (MediaController.this.gravitySensor != null) {
                            MediaController.this.sensorManager.unregisterListener(MediaController.this, MediaController.this.gravitySensor);
                        }
                        if (MediaController.this.accelerometerSensor != null) {
                            MediaController.this.sensorManager.unregisterListener(MediaController.this, MediaController.this.accelerometerSensor);
                        }
                        MediaController.this.sensorManager.unregisterListener(MediaController.this, MediaController.this.proximitySensor);
                    }
                });
                if (this.proximityHasDifferentValues && this.proximityWakeLock != null && this.proximityWakeLock.isHeld()) {
                    this.proximityWakeLock.release();
                }
            }
        }
    }

    public void cleanupPlayer(boolean notify, boolean stopService) {
        cleanupPlayer(notify, stopService, false);
    }

    public void cleanupPlayer(boolean notify, boolean stopService, boolean byVoiceEnd) {
        if (this.audioPlayer != null) {
            try {
                this.audioPlayer.releasePlayer();
            } catch (Throwable e) {
                FileLog.e(e);
            }
            this.audioPlayer = null;
        } else if (this.audioTrackPlayer != null) {
            synchronized (this.playerObjectSync) {
                try {
                    this.audioTrackPlayer.pause();
                    this.audioTrackPlayer.flush();
                } catch (Throwable e2) {
                    FileLog.e(e2);
                }
                try {
                    this.audioTrackPlayer.release();
                } catch (Throwable e22) {
                    FileLog.e(e22);
                }
                this.audioTrackPlayer = null;
            }
        } else if (this.videoPlayer != null) {
            this.currentAspectRatioFrameLayout = null;
            this.currentTextureViewContainer = null;
            this.currentAspectRatioFrameLayoutReady = false;
            this.currentTextureView = null;
            this.videoPlayer.releasePlayer();
            this.videoPlayer = null;
            try {
                this.baseActivity.getWindow().clearFlags(128);
            } catch (Throwable e3) {
                FileLog.e(e3);
            }
        }
        stopProgressTimer();
        this.lastProgress = 0;
        this.buffersWrited = 0;
        this.isPaused = false;
        if (!(this.useFrontSpeaker || SharedConfig.raiseToSpeak)) {
            ChatActivity chat = this.raiseChat;
            stopRaiseToEarSensors(this.raiseChat);
            this.raiseChat = chat;
        }
        if (this.playingMessageObject != null) {
            if (this.downloadingCurrentMessage) {
                FileLoader.getInstance(this.playingMessageObject.currentAccount).cancelLoadFile(this.playingMessageObject.getDocument());
            }
            MessageObject lastFile = this.playingMessageObject;
            if (notify) {
                this.playingMessageObject.resetPlayingProgress();
                NotificationCenter.getInstance(lastFile.currentAccount).postNotificationName(NotificationCenter.messagePlayingProgressDidChanged, Integer.valueOf(this.playingMessageObject.getId()), Integer.valueOf(0));
            }
            this.playingMessageObject = null;
            this.downloadingCurrentMessage = false;
            if (notify) {
                NotificationsController.audioManager.abandonAudioFocus(this);
                this.hasAudioFocus = 0;
                if (this.voiceMessagesPlaylist != null) {
                    if (byVoiceEnd && this.voiceMessagesPlaylist.get(0) == lastFile) {
                        this.voiceMessagesPlaylist.remove(0);
                        this.voiceMessagesPlaylistMap.remove(lastFile.getId());
                        if (this.voiceMessagesPlaylist.isEmpty()) {
                            this.voiceMessagesPlaylist = null;
                            this.voiceMessagesPlaylistMap = null;
                        }
                    } else {
                        this.voiceMessagesPlaylist = null;
                        this.voiceMessagesPlaylistMap = null;
                    }
                }
                if (this.voiceMessagesPlaylist != null) {
                    MessageObject nextVoiceMessage = (MessageObject) this.voiceMessagesPlaylist.get(0);
                    playMessage(nextVoiceMessage);
                    if (!(nextVoiceMessage.isRoundVideo() || this.pipRoundVideoView == null)) {
                        this.pipRoundVideoView.close(true);
                        this.pipRoundVideoView = null;
                    }
                } else {
                    if ((lastFile.isVoice() || lastFile.isRoundVideo()) && lastFile.getId() != 0) {
                        startRecordingIfFromSpeaker();
                    }
                    NotificationCenter.getInstance(lastFile.currentAccount).postNotificationName(NotificationCenter.messagePlayingDidReset, Integer.valueOf(lastFile.getId()), Boolean.valueOf(stopService));
                    this.pipSwitchingState = 0;
                    if (this.pipRoundVideoView != null) {
                        this.pipRoundVideoView.close(true);
                        this.pipRoundVideoView = null;
                    }
                }
            }
            if (stopService) {
                ApplicationLoader.applicationContext.stopService(new Intent(ApplicationLoader.applicationContext, MusicPlayerService.class));
                return;
            }
            return;
        }
        return;
    }

    private void seekOpusPlayer(final float progress) {
        if (progress != VOLUME_NORMAL) {
            if (!this.isPaused) {
                this.audioTrackPlayer.pause();
            }
            this.audioTrackPlayer.flush();
            this.fileDecodingQueue.postRunnable(new Runnable() {
                public void run() {
                    MediaController.this.seekOpusFile(progress);
                    synchronized (MediaController.this.playerSync) {
                        MediaController.this.freePlayerBuffers.addAll(MediaController.this.usedPlayerBuffers);
                        MediaController.this.usedPlayerBuffers.clear();
                    }
                    AndroidUtilities.runOnUIThread(new Runnable() {
                        public void run() {
                            if (!MediaController.this.isPaused) {
                                MediaController.this.ignoreFirstProgress = 3;
                                MediaController.this.lastPlayPcm = (long) (((float) MediaController.this.currentTotalPcmDuration) * progress);
                                if (MediaController.this.audioTrackPlayer != null) {
                                    MediaController.this.audioTrackPlayer.play();
                                }
                                MediaController.this.lastProgress = (long) ((int) ((((float) MediaController.this.currentTotalPcmDuration) / 48.0f) * progress));
                                MediaController.this.checkPlayerQueue();
                            }
                        }
                    });
                }
            });
        }
    }

    private boolean isSamePlayingMessage(MessageObject messageObject) {
        if (this.playingMessageObject == null || this.playingMessageObject.getDialogId() != messageObject.getDialogId() || this.playingMessageObject.getId() != messageObject.getId()) {
            return false;
        }
        return ((this.playingMessageObject.eventId > 0 ? 1 : (this.playingMessageObject.eventId == 0 ? 0 : -1)) == 0) == ((messageObject.eventId > 0 ? 1 : (messageObject.eventId == 0 ? 0 : -1)) == 0);
    }

    public MessageObject getPlayingMessageObject() {
        return this.playingMessageObject;
    }

    public int getPlayingMessageObjectNum() {
        return this.currentPlaylistNum;
    }

    private void buildShuffledPlayList() {
        if (!this.playlist.isEmpty()) {
            ArrayList<MessageObject> all = new ArrayList(this.playlist);
            this.shuffledPlaylist.clear();
            MessageObject messageObject = (MessageObject) this.playlist.get(this.currentPlaylistNum);
            all.remove(this.currentPlaylistNum);
            this.shuffledPlaylist.add(messageObject);
            int count = all.size();
            for (int a = 0; a < count; a++) {
                int index = Utilities.random.nextInt(all.size());
                this.shuffledPlaylist.add(all.get(index));
                all.remove(index);
            }
        }
    }

    public boolean setPlaylist(ArrayList<MessageObject> messageObjects, MessageObject current) {
        return setPlaylist(messageObjects, current, true);
    }

    public boolean setPlaylist(ArrayList<MessageObject> messageObjects, MessageObject current, boolean loadMusic) {
        if (this.playingMessageObject == current) {
            return playMessage(current);
        }
        this.forceLoopCurrentPlaylist = loadMusic ^ 1;
        this.playMusicAgain = this.playlist.isEmpty() ^ 1;
        this.playlist.clear();
        for (int a = messageObjects.size() - 1; a >= 0; a--) {
            MessageObject messageObject = (MessageObject) messageObjects.get(a);
            if (messageObject.isMusic()) {
                this.playlist.add(messageObject);
            }
        }
        this.currentPlaylistNum = this.playlist.indexOf(current);
        if (this.currentPlaylistNum == -1) {
            this.playlist.clear();
            this.shuffledPlaylist.clear();
            this.currentPlaylistNum = this.playlist.size();
            this.playlist.add(current);
        }
        if (current.isMusic()) {
            if (SharedConfig.shuffleMusic) {
                buildShuffledPlayList();
                this.currentPlaylistNum = 0;
            }
            if (loadMusic) {
                DataQuery.getInstance(current.currentAccount).loadMusic(current.getDialogId(), ((MessageObject) this.playlist.get(0)).getIdWithChannel());
            }
        }
        return playMessage(current);
    }

    public void playNextMessage() {
        playNextMessageWithoutOrder(false);
    }

    public boolean findMessageInPlaylistAndPlay(MessageObject messageObject) {
        int index = this.playlist.indexOf(messageObject);
        if (index == -1) {
            return playMessage(messageObject);
        }
        playMessageAtIndex(index);
        return true;
    }

    public void playMessageAtIndex(int index) {
        if (this.currentPlaylistNum >= 0) {
            if (this.currentPlaylistNum < this.playlist.size()) {
                this.currentPlaylistNum = index;
                this.playMusicAgain = true;
                if (this.playingMessageObject != null) {
                    this.playingMessageObject.resetPlayingProgress();
                }
                playMessage((MessageObject) this.playlist.get(this.currentPlaylistNum));
            }
        }
    }

    private void playNextMessageWithoutOrder(boolean byStop) {
        ArrayList<MessageObject> currentPlayList = SharedConfig.shuffleMusic ? this.shuffledPlaylist : this.playlist;
        if (byStop && SharedConfig.repeatMode == 2 && !this.forceLoopCurrentPlaylist) {
            cleanupPlayer(false, false);
            playMessage((MessageObject) currentPlayList.get(this.currentPlaylistNum));
            return;
        }
        boolean last = false;
        if (SharedConfig.playOrderReversed) {
            this.currentPlaylistNum++;
            if (this.currentPlaylistNum >= currentPlayList.size()) {
                this.currentPlaylistNum = 0;
                last = true;
            }
        } else {
            this.currentPlaylistNum--;
            if (this.currentPlaylistNum < 0) {
                this.currentPlaylistNum = currentPlayList.size() - 1;
                last = true;
            }
        }
        if (last && byStop && SharedConfig.repeatMode == 0 && !this.forceLoopCurrentPlaylist) {
            if (!(this.audioPlayer == null && this.audioTrackPlayer == null && this.videoPlayer == null)) {
                if (this.audioPlayer != null) {
                    try {
                        this.audioPlayer.releasePlayer();
                    } catch (Throwable e) {
                        FileLog.e(e);
                    }
                    this.audioPlayer = null;
                } else if (this.audioTrackPlayer != null) {
                    synchronized (this.playerObjectSync) {
                        try {
                            this.audioTrackPlayer.pause();
                            this.audioTrackPlayer.flush();
                        } catch (Throwable e2) {
                            FileLog.e(e2);
                        }
                        try {
                            this.audioTrackPlayer.release();
                        } catch (Throwable e22) {
                            FileLog.e(e22);
                        }
                        this.audioTrackPlayer = null;
                    }
                } else if (this.videoPlayer != null) {
                    this.currentAspectRatioFrameLayout = null;
                    this.currentTextureViewContainer = null;
                    this.currentAspectRatioFrameLayoutReady = false;
                    this.currentTextureView = null;
                    this.videoPlayer.releasePlayer();
                    this.videoPlayer = null;
                    try {
                        this.baseActivity.getWindow().clearFlags(128);
                    } catch (Throwable e3) {
                        FileLog.e(e3);
                    }
                }
                stopProgressTimer();
                this.lastProgress = 0;
                this.buffersWrited = 0;
                this.isPaused = true;
                this.playingMessageObject.audioProgress = 0.0f;
                this.playingMessageObject.audioProgressSec = 0;
                NotificationCenter.getInstance(this.playingMessageObject.currentAccount).postNotificationName(NotificationCenter.messagePlayingProgressDidChanged, Integer.valueOf(this.playingMessageObject.getId()), Integer.valueOf(0));
                NotificationCenter.getInstance(this.playingMessageObject.currentAccount).postNotificationName(NotificationCenter.messagePlayingPlayStateChanged, Integer.valueOf(this.playingMessageObject.getId()));
            }
            return;
        }
        if (this.currentPlaylistNum >= 0) {
            if (this.currentPlaylistNum < currentPlayList.size()) {
                if (this.playingMessageObject != null) {
                    this.playingMessageObject.resetPlayingProgress();
                }
                this.playMusicAgain = true;
                playMessage((MessageObject) currentPlayList.get(this.currentPlaylistNum));
                return;
            }
        }
        return;
    }

    public void playPreviousMessage() {
        ArrayList<MessageObject> currentPlayList = SharedConfig.shuffleMusic ? this.shuffledPlaylist : this.playlist;
        if (!currentPlayList.isEmpty() && this.currentPlaylistNum >= 0) {
            if (this.currentPlaylistNum < currentPlayList.size()) {
                MessageObject currentSong = (MessageObject) currentPlayList.get(this.currentPlaylistNum);
                if (currentSong.audioProgressSec > 10) {
                    seekToProgress(currentSong, 0.0f);
                    return;
                }
                if (SharedConfig.playOrderReversed) {
                    this.currentPlaylistNum--;
                    if (this.currentPlaylistNum < 0) {
                        this.currentPlaylistNum = currentPlayList.size() - 1;
                    }
                } else {
                    this.currentPlaylistNum++;
                    if (this.currentPlaylistNum >= currentPlayList.size()) {
                        this.currentPlaylistNum = 0;
                    }
                }
                if (this.currentPlaylistNum >= 0) {
                    if (this.currentPlaylistNum < currentPlayList.size()) {
                        this.playMusicAgain = true;
                        playMessage((MessageObject) currentPlayList.get(this.currentPlaylistNum));
                    }
                }
            }
        }
    }

    protected void checkIsNextMediaFileDownloaded() {
        if (this.playingMessageObject != null) {
            if (this.playingMessageObject.isMusic()) {
                checkIsNextMusicFileDownloaded(this.playingMessageObject.currentAccount);
            }
        }
    }

    private void checkIsNextVoiceFileDownloaded(int currentAccount) {
        if (this.voiceMessagesPlaylist != null) {
            if (this.voiceMessagesPlaylist.size() >= 2) {
                MessageObject nextAudio = (MessageObject) this.voiceMessagesPlaylist.get(1);
                File file = null;
                if (nextAudio.messageOwner.attachPath != null && nextAudio.messageOwner.attachPath.length() > 0) {
                    file = new File(nextAudio.messageOwner.attachPath);
                    if (!file.exists()) {
                        file = null;
                    }
                }
                File cacheFile = file != null ? file : FileLoader.getPathToMessage(nextAudio.messageOwner);
                if (cacheFile == null || !cacheFile.exists()) {
                    boolean exist = false;
                }
                if (!(cacheFile == null || cacheFile == file || cacheFile.exists())) {
                    FileLoader.getInstance(currentAccount).loadFile(nextAudio.getDocument(), false, 0);
                }
            }
        }
    }

    private void checkIsNextMusicFileDownloaded(int currentAccount) {
        if ((DownloadController.getInstance(currentAccount).getCurrentDownloadMask() & 16) != 0) {
            ArrayList<MessageObject> currentPlayList = SharedConfig.shuffleMusic ? this.shuffledPlaylist : this.playlist;
            if (currentPlayList != null) {
                if (currentPlayList.size() >= 2) {
                    int nextIndex;
                    if (SharedConfig.playOrderReversed) {
                        nextIndex = this.currentPlaylistNum + 1;
                        if (nextIndex >= currentPlayList.size()) {
                            nextIndex = 0;
                        }
                    } else {
                        nextIndex = this.currentPlaylistNum - 1;
                        if (nextIndex < 0) {
                            nextIndex = currentPlayList.size() - 1;
                        }
                    }
                    MessageObject nextAudio = (MessageObject) currentPlayList.get(nextIndex);
                    if (DownloadController.getInstance(currentAccount).canDownloadMedia(nextAudio)) {
                        File file = null;
                        if (!TextUtils.isEmpty(nextAudio.messageOwner.attachPath)) {
                            file = new File(nextAudio.messageOwner.attachPath);
                            if (!file.exists()) {
                                file = null;
                            }
                        }
                        File cacheFile = file != null ? file : FileLoader.getPathToMessage(nextAudio.messageOwner);
                        if (cacheFile == null || !cacheFile.exists()) {
                            boolean exist = false;
                        }
                        if (!(cacheFile == null || cacheFile == file || cacheFile.exists() || !nextAudio.isMusic())) {
                            FileLoader.getInstance(currentAccount).loadFile(nextAudio.getDocument(), false, 0);
                        }
                    }
                }
            }
        }
    }

    public void setVoiceMessagesPlaylist(ArrayList<MessageObject> playlist, boolean unread) {
        this.voiceMessagesPlaylist = playlist;
        if (this.voiceMessagesPlaylist != null) {
            this.voiceMessagesPlaylistUnread = unread;
            this.voiceMessagesPlaylistMap = new SparseArray();
            for (int a = 0; a < this.voiceMessagesPlaylist.size(); a++) {
                MessageObject messageObject = (MessageObject) this.voiceMessagesPlaylist.get(a);
                this.voiceMessagesPlaylistMap.put(messageObject.getId(), messageObject);
            }
        }
    }

    public void setCurrentRoundVisible(boolean visible) {
        if (this.currentAspectRatioFrameLayout != null) {
            if (visible) {
                if (this.pipRoundVideoView != null) {
                    this.pipSwitchingState = 2;
                    this.pipRoundVideoView.close(true);
                    this.pipRoundVideoView = null;
                } else if (this.currentAspectRatioFrameLayout != null) {
                    if (this.currentAspectRatioFrameLayout.getParent() == null) {
                        this.currentTextureViewContainer.addView(this.currentAspectRatioFrameLayout);
                    }
                    this.videoPlayer.setTextureView(this.currentTextureView);
                }
            } else if (this.currentAspectRatioFrameLayout.getParent() != null) {
                this.pipSwitchingState = 1;
                this.currentTextureViewContainer.removeView(this.currentAspectRatioFrameLayout);
            } else {
                if (this.pipRoundVideoView == null) {
                    try {
                        this.pipRoundVideoView = new PipRoundVideoView();
                        this.pipRoundVideoView.show(this.baseActivity, new Runnable() {
                            public void run() {
                                MediaController.this.cleanupPlayer(true, true);
                            }
                        });
                    } catch (Exception e) {
                        this.pipRoundVideoView = null;
                    }
                }
                if (this.pipRoundVideoView != null) {
                    this.videoPlayer.setTextureView(this.pipRoundVideoView.getTextureView());
                }
            }
        }
    }

    public void setTextureView(TextureView textureView, AspectRatioFrameLayout aspectRatioFrameLayout, FrameLayout container, boolean set) {
        if (textureView != null) {
            boolean z = true;
            if (set || this.currentTextureView != textureView) {
                if (this.videoPlayer != null) {
                    if (textureView != this.currentTextureView) {
                        if (aspectRatioFrameLayout == null || !aspectRatioFrameLayout.isDrawingReady()) {
                            z = false;
                        }
                        this.isDrawingWasReady = z;
                        this.currentTextureView = textureView;
                        if (this.pipRoundVideoView != null) {
                            this.videoPlayer.setTextureView(this.pipRoundVideoView.getTextureView());
                        } else {
                            this.videoPlayer.setTextureView(this.currentTextureView);
                        }
                        this.currentAspectRatioFrameLayout = aspectRatioFrameLayout;
                        this.currentTextureViewContainer = container;
                        if (this.currentAspectRatioFrameLayoutReady && this.currentAspectRatioFrameLayout != null) {
                            if (this.currentAspectRatioFrameLayout != null) {
                                this.currentAspectRatioFrameLayout.setAspectRatio(this.currentAspectRatioFrameLayoutRatio, this.currentAspectRatioFrameLayoutRotation);
                            }
                            if (this.currentTextureViewContainer.getVisibility() != 0) {
                                this.currentTextureViewContainer.setVisibility(0);
                            }
                        }
                        return;
                    }
                }
                return;
            }
            this.pipSwitchingState = 1;
            this.currentTextureView = null;
            this.currentAspectRatioFrameLayout = null;
            this.currentTextureViewContainer = null;
        }
    }

    public void setFlagSecure(BaseFragment parentFragment, boolean set) {
        if (set) {
            try {
                parentFragment.getParentActivity().getWindow().setFlags(MessagesController.UPDATE_MASK_CHANNEL, MessagesController.UPDATE_MASK_CHANNEL);
            } catch (Exception e) {
            }
            this.flagSecureFragment = parentFragment;
        } else if (this.flagSecureFragment == parentFragment) {
            try {
                parentFragment.getParentActivity().getWindow().clearFlags(MessagesController.UPDATE_MASK_CHANNEL);
            } catch (Exception e2) {
            }
            this.flagSecureFragment = null;
        }
    }

    public void setBaseActivity(Activity activity, boolean set) {
        if (set) {
            this.baseActivity = activity;
        } else if (this.baseActivity == activity) {
            this.baseActivity = null;
        }
    }

    public void setFeedbackView(View view, boolean set) {
        if (set) {
            this.feedbackView = view;
        } else if (this.feedbackView == view) {
            this.feedbackView = null;
        }
    }

    public void stopAudio() {
        if ((this.audioTrackPlayer != null || this.audioPlayer != null || this.videoPlayer != null) && this.playingMessageObject != null) {
            try {
                if (this.audioPlayer != null) {
                    this.audioPlayer.pause();
                } else if (this.audioTrackPlayer != null) {
                    this.audioTrackPlayer.pause();
                    this.audioTrackPlayer.flush();
                } else if (this.videoPlayer != null) {
                    this.videoPlayer.pause();
                }
            } catch (Throwable e) {
                FileLog.e(e);
            }
            try {
                if (this.audioPlayer != null) {
                    this.audioPlayer.releasePlayer();
                    this.audioPlayer = null;
                } else if (this.audioTrackPlayer != null) {
                    synchronized (this.playerObjectSync) {
                        this.audioTrackPlayer.release();
                        this.audioTrackPlayer = null;
                    }
                } else if (this.videoPlayer != null) {
                    this.currentAspectRatioFrameLayout = null;
                    this.currentTextureViewContainer = null;
                    this.currentAspectRatioFrameLayoutReady = false;
                    this.currentTextureView = null;
                    this.videoPlayer.releasePlayer();
                    this.videoPlayer = null;
                    try {
                        this.baseActivity.getWindow().clearFlags(128);
                    } catch (Throwable e2) {
                        FileLog.e(e2);
                    }
                }
            } catch (Throwable e22) {
                FileLog.e(e22);
            }
            stopProgressTimer();
            this.playingMessageObject = null;
            this.downloadingCurrentMessage = false;
            this.isPaused = false;
            ApplicationLoader.applicationContext.stopService(new Intent(ApplicationLoader.applicationContext, MusicPlayerService.class));
        }
    }

    public AudioInfo getAudioInfo() {
        return this.audioInfo;
    }

    public void toggleShuffleMusic(int type) {
        boolean oldShuffle = SharedConfig.shuffleMusic;
        SharedConfig.toggleShuffleMusic(type);
        if (oldShuffle == SharedConfig.shuffleMusic) {
            return;
        }
        if (SharedConfig.shuffleMusic) {
            buildShuffledPlayList();
            this.currentPlaylistNum = 0;
        } else if (this.playingMessageObject != null) {
            this.currentPlaylistNum = this.playlist.indexOf(this.playingMessageObject);
            if (this.currentPlaylistNum == -1) {
                this.playlist.clear();
                this.shuffledPlaylist.clear();
                cleanupPlayer(true, true);
            }
        }
    }

    public boolean isCurrentPlayer(VideoPlayer player) {
        if (this.videoPlayer != player) {
            if (this.audioPlayer != player) {
                return false;
            }
        }
        return true;
    }

    public boolean pauseMessage(MessageObject messageObject) {
        if (!((this.audioTrackPlayer == null && this.audioPlayer == null && this.videoPlayer == null) || messageObject == null || this.playingMessageObject == null)) {
            if (isSamePlayingMessage(messageObject)) {
                stopProgressTimer();
                try {
                    if (this.audioPlayer != null) {
                        this.audioPlayer.pause();
                    } else if (this.audioTrackPlayer != null) {
                        this.audioTrackPlayer.pause();
                    } else if (this.videoPlayer != null) {
                        this.videoPlayer.pause();
                    }
                    this.isPaused = true;
                    NotificationCenter.getInstance(this.playingMessageObject.currentAccount).postNotificationName(NotificationCenter.messagePlayingPlayStateChanged, Integer.valueOf(this.playingMessageObject.getId()));
                    return true;
                } catch (Throwable e) {
                    FileLog.e(e);
                    this.isPaused = false;
                    return false;
                }
            }
        }
        return false;
    }

    public boolean resumeAudio(MessageObject messageObject) {
        if (!((this.audioTrackPlayer == null && this.audioPlayer == null && this.videoPlayer == null) || messageObject == null || this.playingMessageObject == null)) {
            if (isSamePlayingMessage(messageObject)) {
                try {
                    startProgressTimer(this.playingMessageObject);
                    if (this.audioPlayer != null) {
                        this.audioPlayer.play();
                    } else if (this.audioTrackPlayer != null) {
                        this.audioTrackPlayer.play();
                        checkPlayerQueue();
                    } else if (this.videoPlayer != null) {
                        this.videoPlayer.play();
                    }
                    checkAudioFocus(messageObject);
                    this.isPaused = false;
                    NotificationCenter.getInstance(this.playingMessageObject.currentAccount).postNotificationName(NotificationCenter.messagePlayingPlayStateChanged, Integer.valueOf(this.playingMessageObject.getId()));
                    return true;
                } catch (Throwable e) {
                    FileLog.e(e);
                    return false;
                }
            }
        }
        return false;
    }

    public boolean isRoundVideoDrawingReady() {
        return this.currentAspectRatioFrameLayout != null && this.currentAspectRatioFrameLayout.isDrawingReady();
    }

    public ArrayList<MessageObject> getPlaylist() {
        return this.playlist;
    }

    public boolean isPlayingMessage(MessageObject messageObject) {
        if (!((this.audioTrackPlayer == null && this.audioPlayer == null && this.videoPlayer == null) || messageObject == null)) {
            if (this.playingMessageObject != null) {
                if (this.playingMessageObject.eventId != 0 && this.playingMessageObject.eventId == messageObject.eventId) {
                    return this.downloadingCurrentMessage ^ 1;
                }
                if (isSamePlayingMessage(messageObject)) {
                    return this.downloadingCurrentMessage ^ 1;
                }
                return false;
            }
        }
        return false;
    }

    public boolean isMessagePaused() {
        if (!this.isPaused) {
            if (!this.downloadingCurrentMessage) {
                return false;
            }
        }
        return true;
    }

    public boolean isDownloadingCurrentMessage() {
        return this.downloadingCurrentMessage;
    }

    public void setReplyingMessage(MessageObject reply_to_msg) {
        this.recordReplyingMessageObject = reply_to_msg;
    }

    public void startRecording(int currentAccount, long dialog_id, MessageObject reply_to_msg) {
        boolean paused = false;
        if (!(this.playingMessageObject == null || !isPlayingMessage(this.playingMessageObject) || isMessagePaused())) {
            paused = true;
            pauseMessage(this.playingMessageObject);
        }
        try {
            this.feedbackView.performHapticFeedback(3, 2);
        } catch (Exception e) {
        }
        DispatchQueue dispatchQueue = this.recordQueue;
        final int i = currentAccount;
        final long j = dialog_id;
        final MessageObject messageObject = reply_to_msg;
        Runnable anonymousClass23 = new Runnable() {
            public void run() {
                if (MediaController.this.audioRecorder != null) {
                    AndroidUtilities.runOnUIThread(new Runnable() {
                        public void run() {
                            MediaController.this.recordStartRunnable = null;
                            NotificationCenter.getInstance(i).postNotificationName(NotificationCenter.recordStartError, new Object[0]);
                        }
                    });
                    return;
                }
                MediaController.this.recordingAudio = new TL_document();
                MediaController.this.recordingAudio.dc_id = Integer.MIN_VALUE;
                MediaController.this.recordingAudio.id = (long) SharedConfig.getLastLocalId();
                MediaController.this.recordingAudio.user_id = UserConfig.getInstance(i).getClientUserId();
                MediaController.this.recordingAudio.mime_type = "audio/ogg";
                MediaController.this.recordingAudio.thumb = new TL_photoSizeEmpty();
                MediaController.this.recordingAudio.thumb.type = "s";
                SharedConfig.saveConfig();
                MediaController.this.recordingAudioFile = new File(FileLoader.getDirectory(4), FileLoader.getAttachFileName(MediaController.this.recordingAudio));
                try {
                    if (MediaController.this.startRecord(MediaController.this.recordingAudioFile.getAbsolutePath()) == 0) {
                        AndroidUtilities.runOnUIThread(new Runnable() {
                            public void run() {
                                MediaController.this.recordStartRunnable = null;
                                NotificationCenter.getInstance(i).postNotificationName(NotificationCenter.recordStartError, new Object[0]);
                            }
                        });
                        return;
                    }
                    MediaController.this.audioRecorder = new AudioRecord(1, 16000, 16, 2, MediaController.this.recordBufferSize * 10);
                    MediaController.this.recordStartTime = System.currentTimeMillis();
                    MediaController.this.recordTimeCount = 0;
                    MediaController.this.samplesCount = 0;
                    MediaController.this.recordDialogId = j;
                    MediaController.this.recordingCurrentAccount = i;
                    MediaController.this.recordReplyingMessageObject = messageObject;
                    MediaController.this.fileBuffer.rewind();
                    MediaController.this.audioRecorder.startRecording();
                    MediaController.this.recordQueue.postRunnable(MediaController.this.recordRunnable);
                    AndroidUtilities.runOnUIThread(new Runnable() {
                        public void run() {
                            MediaController.this.recordStartRunnable = null;
                            NotificationCenter.getInstance(i).postNotificationName(NotificationCenter.recordStarted, new Object[0]);
                        }
                    });
                } catch (Throwable e) {
                    FileLog.e(e);
                    MediaController.this.recordingAudio = null;
                    MediaController.this.stopRecord();
                    MediaController.this.recordingAudioFile.delete();
                    MediaController.this.recordingAudioFile = null;
                    try {
                        MediaController.this.audioRecorder.release();
                        MediaController.this.audioRecorder = null;
                    } catch (Throwable e2) {
                        FileLog.e(e2);
                    }
                    AndroidUtilities.runOnUIThread(new Runnable() {
                        public void run() {
                            MediaController.this.recordStartRunnable = null;
                            NotificationCenter.getInstance(i).postNotificationName(NotificationCenter.recordStartError, new Object[0]);
                        }
                    });
                }
            }
        };
        this.recordStartRunnable = anonymousClass23;
        dispatchQueue.postRunnable(anonymousClass23, paused ? 500 : 50);
    }

    public void generateWaveform(MessageObject messageObject) {
        String id = new StringBuilder();
        id.append(messageObject.getId());
        id.append("_");
        id.append(messageObject.getDialogId());
        id = id.toString();
        final String path = FileLoader.getPathToMessage(messageObject.messageOwner).getAbsolutePath();
        if (!this.generatingWaveform.containsKey(id)) {
            this.generatingWaveform.put(id, messageObject);
            Utilities.globalQueue.postRunnable(new Runnable() {
                public void run() {
                    final byte[] waveform = MediaController.this.getWaveform(path);
                    AndroidUtilities.runOnUIThread(new Runnable() {
                        public void run() {
                            MessageObject messageObject = (MessageObject) MediaController.this.generatingWaveform.remove(id);
                            if (!(messageObject == null || waveform == null)) {
                                for (int a = 0; a < messageObject.getDocument().attributes.size(); a++) {
                                    DocumentAttribute attribute = (DocumentAttribute) messageObject.getDocument().attributes.get(a);
                                    if (attribute instanceof TL_documentAttributeAudio) {
                                        attribute.waveform = waveform;
                                        attribute.flags |= 4;
                                        break;
                                    }
                                }
                                messages_Messages messagesRes = new TL_messages_messages();
                                messagesRes.messages.add(messageObject.messageOwner);
                                MessagesStorage.getInstance(messageObject.currentAccount).putMessages(messagesRes, messageObject.getDialogId(), -1, 0, false);
                                new ArrayList().add(messageObject);
                                NotificationCenter.getInstance(messageObject.currentAccount).postNotificationName(NotificationCenter.replaceMessagesObjects, Long.valueOf(messageObject.getDialogId()), arrayList);
                            }
                        }
                    });
                }
            });
        }
    }

    private void stopRecordingInternal(final int send) {
        if (send != 0) {
            final TL_document audioToSend = this.recordingAudio;
            final File recordingAudioFileToSend = this.recordingAudioFile;
            this.fileEncodingQueue.postRunnable(new Runnable() {
                public void run() {
                    MediaController.this.stopRecord();
                    AndroidUtilities.runOnUIThread(new Runnable() {
                        public void run() {
                            audioToSend.date = ConnectionsManager.getInstance(MediaController.this.recordingCurrentAccount).getCurrentTime();
                            audioToSend.size = (int) recordingAudioFileToSend.length();
                            TL_documentAttributeAudio attributeAudio = new TL_documentAttributeAudio();
                            attributeAudio.voice = true;
                            attributeAudio.waveform = MediaController.this.getWaveform2(MediaController.this.recordSamples, MediaController.this.recordSamples.length);
                            if (attributeAudio.waveform != null) {
                                attributeAudio.flags |= 4;
                            }
                            long duration = MediaController.this.recordTimeCount;
                            attributeAudio.duration = (int) (MediaController.this.recordTimeCount / 1000);
                            audioToSend.attributes.add(attributeAudio);
                            if (duration > 700) {
                                if (send == 1) {
                                    SendMessagesHelper.getInstance(MediaController.this.recordingCurrentAccount).sendMessage(audioToSend, null, recordingAudioFileToSend.getAbsolutePath(), MediaController.this.recordDialogId, MediaController.this.recordReplyingMessageObject, null, null, null, null, 0);
                                }
                                NotificationCenter instance = NotificationCenter.getInstance(MediaController.this.recordingCurrentAccount);
                                int i = NotificationCenter.audioDidSent;
                                Object[] objArr = new Object[2];
                                String str = null;
                                objArr[0] = send == 2 ? audioToSend : null;
                                if (send == 2) {
                                    str = recordingAudioFileToSend.getAbsolutePath();
                                }
                                objArr[1] = str;
                                instance.postNotificationName(i, objArr);
                                return;
                            }
                            recordingAudioFileToSend.delete();
                        }
                    });
                }
            });
        }
        try {
            if (this.audioRecorder != null) {
                this.audioRecorder.release();
                this.audioRecorder = null;
            }
        } catch (Throwable e) {
            FileLog.e(e);
        }
        this.recordingAudio = null;
        this.recordingAudioFile = null;
    }

    public void stopRecording(final int send) {
        if (this.recordStartRunnable != null) {
            this.recordQueue.cancelRunnable(this.recordStartRunnable);
            this.recordStartRunnable = null;
        }
        this.recordQueue.postRunnable(new Runnable() {
            public void run() {
                if (MediaController.this.audioRecorder != null) {
                    try {
                        MediaController.this.sendAfterDone = send;
                        MediaController.this.audioRecorder.stop();
                    } catch (Throwable e) {
                        FileLog.e(e);
                        if (MediaController.this.recordingAudioFile != null) {
                            MediaController.this.recordingAudioFile.delete();
                        }
                    }
                    if (send == 0) {
                        MediaController.this.stopRecordingInternal(0);
                    }
                    try {
                        MediaController.this.feedbackView.performHapticFeedback(3, 2);
                    } catch (Exception e2) {
                    }
                    AndroidUtilities.runOnUIThread(new Runnable() {
                        public void run() {
                            NotificationCenter instance = NotificationCenter.getInstance(MediaController.this.recordingCurrentAccount);
                            int i = NotificationCenter.recordStopped;
                            int i2 = 1;
                            Object[] objArr = new Object[1];
                            if (send != 2) {
                                i2 = 0;
                            }
                            objArr[0] = Integer.valueOf(i2);
                            instance.postNotificationName(i, objArr);
                        }
                    });
                }
            }
        });
    }

    public static void saveFile(String fullPath, Context context, int type, String name, String mime) {
        String str = fullPath;
        Context context2 = context;
        if (str != null) {
            File file = null;
            if (!(str == null || fullPath.length() == 0)) {
                file = new File(str);
                if (!file.exists()) {
                    file = null;
                }
            }
            if (file != null) {
                File sourceFile = file;
                final boolean[] cancelled = new boolean[]{false};
                if (sourceFile.exists()) {
                    AlertDialog progressDialog = null;
                    if (!(context2 == null || type == 0)) {
                        try {
                            progressDialog = new AlertDialog(context2, 2);
                            progressDialog.setMessage(LocaleController.getString("Loading", R.string.Loading));
                            progressDialog.setCanceledOnTouchOutside(false);
                            progressDialog.setCancelable(true);
                            progressDialog.setOnCancelListener(new OnCancelListener() {
                                public void onCancel(DialogInterface dialog) {
                                    cancelled[0] = true;
                                }
                            });
                            progressDialog.show();
                        } catch (Throwable e) {
                            FileLog.e(e);
                        }
                    }
                    final AlertDialog finalProgress = progressDialog;
                    final int i = type;
                    final String str2 = name;
                    final File file2 = sourceFile;
                    final boolean[] zArr = cancelled;
                    AnonymousClass28 anonymousClass28 = r4;
                    final String str3 = mime;
                    AnonymousClass28 anonymousClass282 = new Runnable() {

                        class AnonymousClass1 implements Runnable {
                            final /* synthetic */ int val$progress;

                            AnonymousClass1(int i) {
                                this.val$progress = i;
                            }

                            public void run() {
                                try {
                                    finalProgress.setProgress(this.val$progress);
                                } catch (Throwable e) {
                                    FileLog.e(e);
                                }
                            }
                        }

                        public void run() {
                            /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.DecodeException: Load method exception in method: org.telegram.messenger.MediaController.28.run():void
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
                            r1 = r21;
                            r2 = r5;	 Catch:{ Exception -> 0x01d0 }
                            r3 = 2;	 Catch:{ Exception -> 0x01d0 }
                            r4 = 0;	 Catch:{ Exception -> 0x01d0 }
                            if (r2 != 0) goto L_0x000e;	 Catch:{ Exception -> 0x01d0 }
                        L_0x0008:
                            r2 = org.telegram.messenger.AndroidUtilities.generatePicturePath();	 Catch:{ Exception -> 0x01d0 }
                        L_0x000c:
                            goto L_0x00a5;	 Catch:{ Exception -> 0x01d0 }
                        L_0x000e:
                            r2 = r5;	 Catch:{ Exception -> 0x01d0 }
                            r5 = 1;	 Catch:{ Exception -> 0x01d0 }
                            if (r2 != r5) goto L_0x0018;	 Catch:{ Exception -> 0x01d0 }
                        L_0x0013:
                            r2 = org.telegram.messenger.AndroidUtilities.generateVideoPath();	 Catch:{ Exception -> 0x01d0 }
                            goto L_0x000c;	 Catch:{ Exception -> 0x01d0 }
                        L_0x0018:
                            r2 = r5;	 Catch:{ Exception -> 0x01d0 }
                            if (r2 != r3) goto L_0x0023;	 Catch:{ Exception -> 0x01d0 }
                        L_0x001c:
                            r2 = android.os.Environment.DIRECTORY_DOWNLOADS;	 Catch:{ Exception -> 0x01d0 }
                            r2 = android.os.Environment.getExternalStoragePublicDirectory(r2);	 Catch:{ Exception -> 0x01d0 }
                            goto L_0x0029;	 Catch:{ Exception -> 0x01d0 }
                        L_0x0023:
                            r2 = android.os.Environment.DIRECTORY_MUSIC;	 Catch:{ Exception -> 0x01d0 }
                            r2 = android.os.Environment.getExternalStoragePublicDirectory(r2);	 Catch:{ Exception -> 0x01d0 }
                        L_0x0029:
                            r2.mkdir();	 Catch:{ Exception -> 0x01d0 }
                            r5 = new java.io.File;	 Catch:{ Exception -> 0x01d0 }
                            r6 = r6;	 Catch:{ Exception -> 0x01d0 }
                            r5.<init>(r2, r6);	 Catch:{ Exception -> 0x01d0 }
                            r6 = r5.exists();	 Catch:{ Exception -> 0x01d0 }
                            if (r6 == 0) goto L_0x00a4;	 Catch:{ Exception -> 0x01d0 }
                        L_0x0039:
                            r6 = r6;	 Catch:{ Exception -> 0x01d0 }
                            r7 = 46;	 Catch:{ Exception -> 0x01d0 }
                            r6 = r6.lastIndexOf(r7);	 Catch:{ Exception -> 0x01d0 }
                            r7 = r5;	 Catch:{ Exception -> 0x01d0 }
                            r5 = r4;	 Catch:{ Exception -> 0x01d0 }
                        L_0x0043:
                            r8 = 10;	 Catch:{ Exception -> 0x01d0 }
                            if (r5 >= r8) goto L_0x00a2;	 Catch:{ Exception -> 0x01d0 }
                        L_0x0047:
                            r8 = -1;	 Catch:{ Exception -> 0x01d0 }
                            if (r6 == r8) goto L_0x0075;	 Catch:{ Exception -> 0x01d0 }
                        L_0x004a:
                            r8 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x01d0 }
                            r8.<init>();	 Catch:{ Exception -> 0x01d0 }
                            r9 = r6;	 Catch:{ Exception -> 0x01d0 }
                            r9 = r9.substring(r4, r6);	 Catch:{ Exception -> 0x01d0 }
                            r8.append(r9);	 Catch:{ Exception -> 0x01d0 }
                            r9 = "(";	 Catch:{ Exception -> 0x01d0 }
                            r8.append(r9);	 Catch:{ Exception -> 0x01d0 }
                            r9 = r5 + 1;	 Catch:{ Exception -> 0x01d0 }
                            r8.append(r9);	 Catch:{ Exception -> 0x01d0 }
                            r9 = ")";	 Catch:{ Exception -> 0x01d0 }
                            r8.append(r9);	 Catch:{ Exception -> 0x01d0 }
                            r9 = r6;	 Catch:{ Exception -> 0x01d0 }
                            r9 = r9.substring(r6);	 Catch:{ Exception -> 0x01d0 }
                            r8.append(r9);	 Catch:{ Exception -> 0x01d0 }
                            r8 = r8.toString();	 Catch:{ Exception -> 0x01d0 }
                            goto L_0x0092;	 Catch:{ Exception -> 0x01d0 }
                        L_0x0075:
                            r8 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x01d0 }
                            r8.<init>();	 Catch:{ Exception -> 0x01d0 }
                            r9 = r6;	 Catch:{ Exception -> 0x01d0 }
                            r8.append(r9);	 Catch:{ Exception -> 0x01d0 }
                            r9 = "(";	 Catch:{ Exception -> 0x01d0 }
                            r8.append(r9);	 Catch:{ Exception -> 0x01d0 }
                            r9 = r5 + 1;	 Catch:{ Exception -> 0x01d0 }
                            r8.append(r9);	 Catch:{ Exception -> 0x01d0 }
                            r9 = ")";	 Catch:{ Exception -> 0x01d0 }
                            r8.append(r9);	 Catch:{ Exception -> 0x01d0 }
                            r8 = r8.toString();	 Catch:{ Exception -> 0x01d0 }
                        L_0x0092:
                            r9 = new java.io.File;	 Catch:{ Exception -> 0x01d0 }
                            r9.<init>(r2, r8);	 Catch:{ Exception -> 0x01d0 }
                            r7 = r9;	 Catch:{ Exception -> 0x01d0 }
                            r9 = r7.exists();	 Catch:{ Exception -> 0x01d0 }
                            if (r9 != 0) goto L_0x009f;	 Catch:{ Exception -> 0x01d0 }
                        L_0x009e:
                            goto L_0x00a2;	 Catch:{ Exception -> 0x01d0 }
                        L_0x009f:
                            r5 = r5 + 1;	 Catch:{ Exception -> 0x01d0 }
                            goto L_0x0043;	 Catch:{ Exception -> 0x01d0 }
                        L_0x00a2:
                            r2 = r7;	 Catch:{ Exception -> 0x01d0 }
                            goto L_0x00a5;	 Catch:{ Exception -> 0x01d0 }
                        L_0x00a4:
                            r2 = r5;	 Catch:{ Exception -> 0x01d0 }
                        L_0x00a5:
                            r5 = r2.exists();	 Catch:{ Exception -> 0x01d0 }
                            if (r5 != 0) goto L_0x00ae;	 Catch:{ Exception -> 0x01d0 }
                        L_0x00ab:
                            r2.createNewFile();	 Catch:{ Exception -> 0x01d0 }
                        L_0x00ae:
                            r5 = 0;	 Catch:{ Exception -> 0x01d0 }
                            r6 = 0;	 Catch:{ Exception -> 0x01d0 }
                            r7 = 1;	 Catch:{ Exception -> 0x01d0 }
                            r8 = java.lang.System.currentTimeMillis();	 Catch:{ Exception -> 0x01d0 }
                            r10 = 500; // 0x1f4 float:7.0E-43 double:2.47E-321;
                            r12 = r8 - r10;
                            r8 = r12;
                            r12 = new java.io.FileInputStream;	 Catch:{ Exception -> 0x0166 }
                            r13 = r7;	 Catch:{ Exception -> 0x0166 }
                            r12.<init>(r13);	 Catch:{ Exception -> 0x0166 }
                            r12 = r12.getChannel();	 Catch:{ Exception -> 0x0166 }
                            r5 = r12;
                            r12 = new java.io.FileOutputStream;	 Catch:{ Exception -> 0x015f, all -> 0x015a }
                            r12.<init>(r2);	 Catch:{ Exception -> 0x015f, all -> 0x015a }
                            r12 = r12.getChannel();	 Catch:{ Exception -> 0x015f, all -> 0x015a }
                            r6 = r12;	 Catch:{ Exception -> 0x015f, all -> 0x015a }
                            r12 = r5.size();	 Catch:{ Exception -> 0x015f, all -> 0x015a }
                            r14 = r12;
                            r12 = 0;
                        L_0x00d7:
                            r16 = (r12 > r14 ? 1 : (r12 == r14 ? 0 : -1));
                            if (r16 >= 0) goto L_0x0143;
                        L_0x00db:
                            r3 = r8;	 Catch:{ Exception -> 0x013e, all -> 0x0138 }
                            r3 = r3[r4];	 Catch:{ Exception -> 0x013e, all -> 0x0138 }
                            if (r3 == 0) goto L_0x00e6;
                        L_0x00e2:
                            r19 = r5;
                            goto L_0x0145;
                        L_0x00e6:
                            r10 = r14 - r12;
                            r19 = r5;
                            r4 = 4096; // 0x1000 float:5.74E-42 double:2.0237E-320;
                            r17 = java.lang.Math.min(r4, r10);	 Catch:{ Exception -> 0x0133, all -> 0x012d }
                            r10 = r12;	 Catch:{ Exception -> 0x0133, all -> 0x012d }
                            r13 = r6;	 Catch:{ Exception -> 0x0133, all -> 0x012d }
                            r4 = r14;	 Catch:{ Exception -> 0x0133, all -> 0x012d }
                            r14 = r19;	 Catch:{ Exception -> 0x0133, all -> 0x012d }
                            r15 = r10;	 Catch:{ Exception -> 0x0133, all -> 0x012d }
                            r13.transferFrom(r14, r15, r17);	 Catch:{ Exception -> 0x0133, all -> 0x012d }
                            r3 = r9;	 Catch:{ Exception -> 0x0133, all -> 0x012d }
                            if (r3 == 0) goto L_0x011e;	 Catch:{ Exception -> 0x0133, all -> 0x012d }
                        L_0x00fd:
                            r12 = java.lang.System.currentTimeMillis();	 Catch:{ Exception -> 0x0133, all -> 0x012d }
                            r14 = 500; // 0x1f4 float:7.0E-43 double:2.47E-321;	 Catch:{ Exception -> 0x0133, all -> 0x012d }
                            r16 = r12 - r14;	 Catch:{ Exception -> 0x0133, all -> 0x012d }
                            r3 = (r8 > r16 ? 1 : (r8 == r16 ? 0 : -1));	 Catch:{ Exception -> 0x0133, all -> 0x012d }
                            if (r3 > 0) goto L_0x0120;	 Catch:{ Exception -> 0x0133, all -> 0x012d }
                        L_0x0109:
                            r12 = java.lang.System.currentTimeMillis();	 Catch:{ Exception -> 0x0133, all -> 0x012d }
                            r8 = r12;	 Catch:{ Exception -> 0x0133, all -> 0x012d }
                            r3 = (float) r10;	 Catch:{ Exception -> 0x0133, all -> 0x012d }
                            r12 = (float) r4;	 Catch:{ Exception -> 0x0133, all -> 0x012d }
                            r3 = r3 / r12;	 Catch:{ Exception -> 0x0133, all -> 0x012d }
                            r12 = 1120403456; // 0x42c80000 float:100.0 double:5.53552857E-315;	 Catch:{ Exception -> 0x0133, all -> 0x012d }
                            r3 = r3 * r12;	 Catch:{ Exception -> 0x0133, all -> 0x012d }
                            r3 = (int) r3;	 Catch:{ Exception -> 0x0133, all -> 0x012d }
                            r12 = new org.telegram.messenger.MediaController$28$1;	 Catch:{ Exception -> 0x0133, all -> 0x012d }
                            r12.<init>(r3);	 Catch:{ Exception -> 0x0133, all -> 0x012d }
                            org.telegram.messenger.AndroidUtilities.runOnUIThread(r12);	 Catch:{ Exception -> 0x0133, all -> 0x012d }
                            goto L_0x0120;
                        L_0x011e:
                            r14 = 500; // 0x1f4 float:7.0E-43 double:2.47E-321;
                        L_0x0120:
                            r12 = 4096; // 0x1000 float:5.74E-42 double:2.0237E-320;
                            r16 = r10 + r12;
                            r10 = r14;
                            r12 = r16;
                            r3 = 2;
                            r14 = r4;
                            r5 = r19;
                            r4 = 0;
                            goto L_0x00d7;
                        L_0x012d:
                            r0 = move-exception;
                            r3 = r0;
                            r5 = r19;
                            goto L_0x01bd;
                        L_0x0133:
                            r0 = move-exception;
                            r3 = r0;
                            r5 = r19;
                            goto L_0x0168;
                        L_0x0138:
                            r0 = move-exception;
                            r19 = r5;
                            r3 = r0;
                            goto L_0x01bd;
                        L_0x013e:
                            r0 = move-exception;
                            r19 = r5;
                            r3 = r0;
                            goto L_0x0168;
                        L_0x0143:
                            r19 = r5;
                        L_0x0145:
                            if (r19 == 0) goto L_0x014f;
                            r3 = r19;
                            r3.close();	 Catch:{ Exception -> 0x014d }
                            goto L_0x0151;
                        L_0x014d:
                            r0 = move-exception;
                            goto L_0x0151;
                            r3 = r19;
                            if (r6 == 0) goto L_0x0159;
                            r6.close();	 Catch:{ Exception -> 0x0157 }
                            goto L_0x0159;
                        L_0x0157:
                            r0 = move-exception;
                            goto L_0x017f;
                            goto L_0x017f;
                        L_0x015a:
                            r0 = move-exception;
                            r3 = r5;
                            r3 = r0;
                            goto L_0x01bd;
                        L_0x015f:
                            r0 = move-exception;
                            r3 = r5;
                            r3 = r0;
                            goto L_0x0168;
                        L_0x0163:
                            r0 = move-exception;
                            r3 = r0;
                            goto L_0x01bd;
                        L_0x0166:
                            r0 = move-exception;
                            r3 = r0;
                            org.telegram.messenger.FileLog.e(r3);	 Catch:{ all -> 0x0163 }
                            r7 = 0;
                            if (r5 == 0) goto L_0x0174;
                            r5.close();	 Catch:{ Exception -> 0x0172 }
                            goto L_0x0174;
                        L_0x0172:
                            r0 = move-exception;
                            goto L_0x0175;
                            if (r6 == 0) goto L_0x017d;
                            r6.close();	 Catch:{ Exception -> 0x017b }
                            goto L_0x017d;
                        L_0x017b:
                            r0 = move-exception;
                            goto L_0x017e;
                            r3 = r5;
                            r4 = r8;	 Catch:{ Exception -> 0x01d0 }
                            r5 = 0;	 Catch:{ Exception -> 0x01d0 }
                            r4 = r4[r5];	 Catch:{ Exception -> 0x01d0 }
                            if (r4 == 0) goto L_0x018a;	 Catch:{ Exception -> 0x01d0 }
                            r2.delete();	 Catch:{ Exception -> 0x01d0 }
                            r7 = 0;	 Catch:{ Exception -> 0x01d0 }
                            if (r7 == 0) goto L_0x01bc;	 Catch:{ Exception -> 0x01d0 }
                            r4 = r5;	 Catch:{ Exception -> 0x01d0 }
                            r5 = 2;	 Catch:{ Exception -> 0x01d0 }
                            if (r4 != r5) goto L_0x01b5;	 Catch:{ Exception -> 0x01d0 }
                            r4 = org.telegram.messenger.ApplicationLoader.applicationContext;	 Catch:{ Exception -> 0x01d0 }
                            r5 = "download";	 Catch:{ Exception -> 0x01d0 }
                            r4 = r4.getSystemService(r5);	 Catch:{ Exception -> 0x01d0 }
                            r10 = r4;	 Catch:{ Exception -> 0x01d0 }
                            r10 = (android.app.DownloadManager) r10;	 Catch:{ Exception -> 0x01d0 }
                            r11 = r2.getName();	 Catch:{ Exception -> 0x01d0 }
                            r12 = r2.getName();	 Catch:{ Exception -> 0x01d0 }
                            r13 = 0;	 Catch:{ Exception -> 0x01d0 }
                            r14 = r10;	 Catch:{ Exception -> 0x01d0 }
                            r15 = r2.getAbsolutePath();	 Catch:{ Exception -> 0x01d0 }
                            r16 = r2.length();	 Catch:{ Exception -> 0x01d0 }
                            r18 = 1;	 Catch:{ Exception -> 0x01d0 }
                            r10.addCompletedDownload(r11, r12, r13, r14, r15, r16, r18);	 Catch:{ Exception -> 0x01d0 }
                            goto L_0x01bc;	 Catch:{ Exception -> 0x01d0 }
                            r4 = android.net.Uri.fromFile(r2);	 Catch:{ Exception -> 0x01d0 }
                            org.telegram.messenger.AndroidUtilities.addMediaToGallery(r4);	 Catch:{ Exception -> 0x01d0 }
                            goto L_0x01d5;
                            if (r5 == 0) goto L_0x01c6;
                            r5.close();	 Catch:{ Exception -> 0x01c4 }
                            goto L_0x01c6;
                        L_0x01c4:
                            r0 = move-exception;
                            goto L_0x01c7;
                            if (r6 == 0) goto L_0x01cf;
                            r6.close();	 Catch:{ Exception -> 0x01cd }
                            goto L_0x01cf;
                        L_0x01cd:
                            r0 = move-exception;
                            throw r3;	 Catch:{ Exception -> 0x01d0 }
                        L_0x01d0:
                            r0 = move-exception;
                            r2 = r0;
                            org.telegram.messenger.FileLog.e(r2);
                            r2 = r9;
                            if (r2 == 0) goto L_0x01e1;
                            r2 = new org.telegram.messenger.MediaController$28$2;
                            r2.<init>();
                            org.telegram.messenger.AndroidUtilities.runOnUIThread(r2);
                            return;
                            */
                            throw new UnsupportedOperationException("Method not decompiled: org.telegram.messenger.MediaController.28.run():void");
                        }
                    };
                    new Thread(anonymousClass28).start();
                }
            }
        }
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static java.lang.String getFileName(android.net.Uri r9) {
        /*
        r0 = 0;
        r1 = r9.getScheme();
        r2 = "content";
        r1 = r1.equals(r2);
        if (r1 == 0) goto L_0x004b;
    L_0x000d:
        r1 = 0;
        r2 = org.telegram.messenger.ApplicationLoader.applicationContext;	 Catch:{ Exception -> 0x003e }
        r3 = r2.getContentResolver();	 Catch:{ Exception -> 0x003e }
        r2 = 1;
        r5 = new java.lang.String[r2];	 Catch:{ Exception -> 0x003e }
        r2 = 0;
        r4 = "_display_name";
        r5[r2] = r4;	 Catch:{ Exception -> 0x003e }
        r6 = 0;
        r7 = 0;
        r8 = 0;
        r4 = r9;
        r2 = r3.query(r4, r5, r6, r7, r8);	 Catch:{ Exception -> 0x003e }
        r1 = r2;
        r2 = r1.moveToFirst();	 Catch:{ Exception -> 0x003e }
        if (r2 == 0) goto L_0x0036;
    L_0x002b:
        r2 = "_display_name";
        r2 = r1.getColumnIndex(r2);	 Catch:{ Exception -> 0x003e }
        r2 = r1.getString(r2);	 Catch:{ Exception -> 0x003e }
        r0 = r2;
    L_0x0036:
        if (r1 == 0) goto L_0x004b;
    L_0x0038:
        r1.close();
        goto L_0x004b;
    L_0x003c:
        r2 = move-exception;
        goto L_0x0045;
    L_0x003e:
        r2 = move-exception;
        org.telegram.messenger.FileLog.e(r2);	 Catch:{ all -> 0x003c }
        if (r1 == 0) goto L_0x004b;
    L_0x0044:
        goto L_0x0038;
    L_0x0045:
        if (r1 == 0) goto L_0x004a;
    L_0x0047:
        r1.close();
    L_0x004a:
        throw r2;
    L_0x004b:
        if (r0 != 0) goto L_0x0060;
    L_0x004d:
        r0 = r9.getPath();
        r1 = 47;
        r1 = r0.lastIndexOf(r1);
        r2 = -1;
        if (r1 == r2) goto L_0x0060;
    L_0x005a:
        r2 = r1 + 1;
        r0 = r0.substring(r2);
    L_0x0060:
        return r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.telegram.messenger.MediaController.getFileName(android.net.Uri):java.lang.String");
    }

    public static void loadGalleryPhotosAlbums(final int guid) {
        Thread thread = new Thread(new Runnable() {
            /* JADX WARNING: inconsistent code. */
            /* Code decompiled incorrectly, please refer to instructions dump. */
            public void run() {
                /*
                r57 = this;
                r1 = r57;
                r2 = new java.util.ArrayList;
                r2.<init>();
                r3 = new java.util.ArrayList;
                r3.<init>();
                r10 = r3;
                r3 = new android.util.SparseArray;
                r3.<init>();
                r11 = r3;
                r3 = new android.util.SparseArray;
                r3.<init>();
                r12 = r3;
                r3 = 0;
                r4 = 0;
                r5 = 0;
                r6 = r5;
                r7 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x003a }
                r7.<init>();	 Catch:{ Exception -> 0x003a }
                r8 = android.os.Environment.DIRECTORY_DCIM;	 Catch:{ Exception -> 0x003a }
                r8 = android.os.Environment.getExternalStoragePublicDirectory(r8);	 Catch:{ Exception -> 0x003a }
                r8 = r8.getAbsolutePath();	 Catch:{ Exception -> 0x003a }
                r7.append(r8);	 Catch:{ Exception -> 0x003a }
                r8 = "/Camera/";
                r7.append(r8);	 Catch:{ Exception -> 0x003a }
                r7 = r7.toString();	 Catch:{ Exception -> 0x003a }
                r6 = r7;
                goto L_0x003f;
            L_0x003a:
                r0 = move-exception;
                r7 = r0;
                org.telegram.messenger.FileLog.e(r7);
            L_0x003f:
                r13 = r6;
                r6 = 0;
                r7 = 0;
                r9 = 23;
                r15 = android.os.Build.VERSION.SDK_INT;	 Catch:{ Throwable -> 0x0296, all -> 0x028e }
                if (r15 < r9) goto L_0x006c;
            L_0x0049:
                r15 = android.os.Build.VERSION.SDK_INT;	 Catch:{ Throwable -> 0x0066, all -> 0x005e }
                if (r15 < r9) goto L_0x0058;
            L_0x004d:
                r15 = org.telegram.messenger.ApplicationLoader.applicationContext;	 Catch:{ Throwable -> 0x0066, all -> 0x005e }
                r9 = "android.permission.READ_EXTERNAL_STORAGE";
                r9 = r15.checkSelfPermission(r9);	 Catch:{ Throwable -> 0x0066, all -> 0x005e }
                if (r9 != 0) goto L_0x0058;
            L_0x0057:
                goto L_0x006c;
            L_0x0058:
                r23 = r3;
                r24 = r4;
                goto L_0x027e;
            L_0x005e:
                r0 = move-exception;
                r23 = r3;
                r24 = r4;
                r3 = r0;
                goto L_0x04c6;
            L_0x0066:
                r0 = move-exception;
                r23 = r3;
                r3 = r0;
                goto L_0x029c;
            L_0x006c:
                r9 = org.telegram.messenger.ApplicationLoader.applicationContext;	 Catch:{ Throwable -> 0x0296, all -> 0x028e }
                r17 = r9.getContentResolver();	 Catch:{ Throwable -> 0x0296, all -> 0x028e }
                r18 = android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI;	 Catch:{ Throwable -> 0x0296, all -> 0x028e }
                r19 = org.telegram.messenger.MediaController.projectionPhotos;	 Catch:{ Throwable -> 0x0296, all -> 0x028e }
                r20 = 0;
                r21 = 0;
                r22 = "datetaken DESC";
                r9 = android.provider.MediaStore.Images.Media.query(r17, r18, r19, r20, r21, r22);	 Catch:{ Throwable -> 0x0296, all -> 0x028e }
                r5 = r9;
                if (r5 == 0) goto L_0x0278;
            L_0x0085:
                r9 = "_id";
                r9 = r5.getColumnIndex(r9);	 Catch:{ Throwable -> 0x026f, all -> 0x0265 }
                r15 = "bucket_id";
                r15 = r5.getColumnIndex(r15);	 Catch:{ Throwable -> 0x026f, all -> 0x0265 }
                r8 = "bucket_display_name";
                r8 = r5.getColumnIndex(r8);	 Catch:{ Throwable -> 0x026f, all -> 0x0265 }
                r14 = "_data";
                r14 = r5.getColumnIndex(r14);	 Catch:{ Throwable -> 0x026f, all -> 0x0265 }
                r23 = r3;
                r3 = "datetaken";
                r3 = r5.getColumnIndex(r3);	 Catch:{ Throwable -> 0x025e, all -> 0x0256 }
                r24 = r4;
                r4 = "orientation";
                r4 = r5.getColumnIndex(r4);	 Catch:{ Throwable -> 0x024f, all -> 0x0249 }
            L_0x00ad:
                r17 = r5.moveToNext();	 Catch:{ Throwable -> 0x024f, all -> 0x0249 }
                if (r17 == 0) goto L_0x0246;
            L_0x00b3:
                r27 = r5.getInt(r9);	 Catch:{ Throwable -> 0x024f, all -> 0x0249 }
                r17 = r5.getInt(r15);	 Catch:{ Throwable -> 0x024f, all -> 0x0249 }
                r33 = r17;
                r17 = r5.getString(r8);	 Catch:{ Throwable -> 0x024f, all -> 0x0249 }
                r34 = r17;
                r17 = r5.getString(r14);	 Catch:{ Throwable -> 0x024f, all -> 0x0249 }
                r35 = r17;
                r28 = r5.getLong(r3);	 Catch:{ Throwable -> 0x024f, all -> 0x0249 }
                r31 = r5.getInt(r4);	 Catch:{ Throwable -> 0x024f, all -> 0x0249 }
                r36 = r3;
                r3 = r35;
                if (r3 == 0) goto L_0x022e;
            L_0x00d7:
                r17 = r3.length();	 Catch:{ Throwable -> 0x024f, all -> 0x0249 }
                if (r17 != 0) goto L_0x00ea;
            L_0x00de:
                r38 = r4;
                r39 = r5;
                r40 = r8;
                r41 = r9;
                r42 = r14;
                goto L_0x0238;
            L_0x00ea:
                r17 = new org.telegram.messenger.MediaController$PhotoEntry;	 Catch:{ Throwable -> 0x024f, all -> 0x0249 }
                r32 = 0;
                r25 = r17;
                r26 = r33;
                r30 = r3;
                r25.<init>(r26, r27, r28, r30, r31, r32);	 Catch:{ Throwable -> 0x024f, all -> 0x0249 }
                r37 = r17;
                if (r23 != 0) goto L_0x0126;
            L_0x00fb:
                r38 = r4;
                r4 = new org.telegram.messenger.MediaController$AlbumEntry;	 Catch:{ Throwable -> 0x024f, all -> 0x0249 }
                r39 = r5;
                r5 = "AllPhotos";
                r40 = r8;
                r8 = 2131492950; // 0x7f0c0056 float:1.8609366E38 double:1.053097441E-314;
                r5 = org.telegram.messenger.LocaleController.getString(r5, r8);	 Catch:{ Throwable -> 0x011e, all -> 0x0118 }
                r41 = r9;
                r8 = r37;
                r9 = 0;
                r4.<init>(r9, r5, r8);	 Catch:{ Throwable -> 0x011e, all -> 0x0118 }
                r10.add(r9, r4);	 Catch:{ Throwable -> 0x015b, all -> 0x0156 }
                goto L_0x0132;
            L_0x0118:
                r0 = move-exception;
                r3 = r0;
            L_0x011a:
                r5 = r39;
                goto L_0x04c6;
            L_0x011e:
                r0 = move-exception;
                r3 = r0;
            L_0x0120:
                r4 = r24;
            L_0x0122:
                r5 = r39;
                goto L_0x029c;
            L_0x0126:
                r38 = r4;
                r39 = r5;
                r40 = r8;
                r41 = r9;
                r8 = r37;
                r4 = r23;
            L_0x0132:
                if (r24 != 0) goto L_0x0160;
            L_0x0134:
                r5 = new org.telegram.messenger.MediaController$AlbumEntry;	 Catch:{ Throwable -> 0x015b, all -> 0x0156 }
                r9 = "AllMedia";
                r42 = r14;
                r14 = 2131492949; // 0x7f0c0055 float:1.8609364E38 double:1.0530974405E-314;
                r9 = org.telegram.messenger.LocaleController.getString(r9, r14);	 Catch:{ Throwable -> 0x015b, all -> 0x0156 }
                r14 = 0;
                r5.<init>(r14, r9, r8);	 Catch:{ Throwable -> 0x015b, all -> 0x0156 }
                r2.add(r14, r5);	 Catch:{ Throwable -> 0x0150, all -> 0x0149 }
                goto L_0x0164;
            L_0x0149:
                r0 = move-exception;
                r3 = r0;
                r23 = r4;
                r24 = r5;
                goto L_0x011a;
            L_0x0150:
                r0 = move-exception;
                r3 = r0;
                r23 = r4;
                r4 = r5;
                goto L_0x0122;
            L_0x0156:
                r0 = move-exception;
                r3 = r0;
                r23 = r4;
                goto L_0x011a;
            L_0x015b:
                r0 = move-exception;
                r3 = r0;
                r23 = r4;
                goto L_0x0120;
            L_0x0160:
                r42 = r14;
                r5 = r24;
            L_0x0164:
                r4.addPhoto(r8);	 Catch:{ Throwable -> 0x0220, all -> 0x0212 }
                r5.addPhoto(r8);	 Catch:{ Throwable -> 0x0220, all -> 0x0212 }
                r9 = r33;
                r14 = r11.get(r9);	 Catch:{ Throwable -> 0x0220, all -> 0x0212 }
                r14 = (org.telegram.messenger.MediaController.AlbumEntry) r14;	 Catch:{ Throwable -> 0x0220, all -> 0x0212 }
                if (r14 != 0) goto L_0x01b5;
            L_0x0174:
                r43 = r4;
                r4 = new org.telegram.messenger.MediaController$AlbumEntry;	 Catch:{ Throwable -> 0x01a9, all -> 0x019d }
                r44 = r5;
                r5 = r34;
                r4.<init>(r9, r5, r8);	 Catch:{ Throwable -> 0x0208, all -> 0x01fe }
                r14 = r4;
                r11.put(r9, r14);	 Catch:{ Throwable -> 0x0208, all -> 0x01fe }
                if (r6 != 0) goto L_0x0199;
            L_0x0185:
                if (r13 == 0) goto L_0x0199;
            L_0x0187:
                if (r3 == 0) goto L_0x0199;
            L_0x0189:
                r4 = r3.startsWith(r13);	 Catch:{ Throwable -> 0x0208, all -> 0x01fe }
                if (r4 == 0) goto L_0x0199;
            L_0x018f:
                r4 = 0;
                r2.add(r4, r14);	 Catch:{ Throwable -> 0x0208, all -> 0x01fe }
                r4 = java.lang.Integer.valueOf(r9);	 Catch:{ Throwable -> 0x0208, all -> 0x01fe }
                r6 = r4;
                goto L_0x01bb;
            L_0x0199:
                r2.add(r14);	 Catch:{ Throwable -> 0x0208, all -> 0x01fe }
                goto L_0x01bb;
            L_0x019d:
                r0 = move-exception;
                r44 = r5;
                r3 = r0;
                r5 = r39;
                r23 = r43;
                r24 = r44;
                goto L_0x04c6;
            L_0x01a9:
                r0 = move-exception;
                r44 = r5;
                r3 = r0;
                r5 = r39;
                r23 = r43;
                r4 = r44;
                goto L_0x029c;
            L_0x01b5:
                r43 = r4;
                r44 = r5;
                r5 = r34;
            L_0x01bb:
                r14.addPhoto(r8);	 Catch:{ Throwable -> 0x0208, all -> 0x01fe }
                r4 = r12.get(r9);	 Catch:{ Throwable -> 0x0208, all -> 0x01fe }
                r4 = (org.telegram.messenger.MediaController.AlbumEntry) r4;	 Catch:{ Throwable -> 0x0208, all -> 0x01fe }
                if (r4 != 0) goto L_0x01e8;
            L_0x01c6:
                r14 = new org.telegram.messenger.MediaController$AlbumEntry;	 Catch:{ Throwable -> 0x0208, all -> 0x01fe }
                r14.<init>(r9, r5, r8);	 Catch:{ Throwable -> 0x0208, all -> 0x01fe }
                r4 = r14;
                r12.put(r9, r4);	 Catch:{ Throwable -> 0x0208, all -> 0x01fe }
                if (r7 != 0) goto L_0x01e5;
            L_0x01d1:
                if (r13 == 0) goto L_0x01e5;
            L_0x01d3:
                if (r3 == 0) goto L_0x01e5;
            L_0x01d5:
                r14 = r3.startsWith(r13);	 Catch:{ Throwable -> 0x0208, all -> 0x01fe }
                if (r14 == 0) goto L_0x01e5;
            L_0x01db:
                r14 = 0;
                r10.add(r14, r4);	 Catch:{ Throwable -> 0x0208, all -> 0x01fe }
                r14 = java.lang.Integer.valueOf(r9);	 Catch:{ Throwable -> 0x0208, all -> 0x01fe }
                r7 = r14;
                goto L_0x01e8;
            L_0x01e5:
                r10.add(r4);	 Catch:{ Throwable -> 0x0208, all -> 0x01fe }
            L_0x01e8:
                r4.addPhoto(r8);	 Catch:{ Throwable -> 0x0208, all -> 0x01fe }
                r3 = r36;
                r4 = r38;
                r5 = r39;
                r8 = r40;
                r9 = r41;
                r14 = r42;
                r23 = r43;
                r24 = r44;
                goto L_0x00ad;
            L_0x01fe:
                r0 = move-exception;
                r3 = r0;
                r5 = r39;
                r23 = r43;
                r24 = r44;
                goto L_0x04c6;
            L_0x0208:
                r0 = move-exception;
                r3 = r0;
                r5 = r39;
                r23 = r43;
                r4 = r44;
                goto L_0x029c;
            L_0x0212:
                r0 = move-exception;
                r43 = r4;
                r44 = r5;
                r3 = r0;
                r5 = r39;
                r23 = r43;
                r24 = r44;
                goto L_0x04c6;
            L_0x0220:
                r0 = move-exception;
                r43 = r4;
                r44 = r5;
                r3 = r0;
                r5 = r39;
                r23 = r43;
                r4 = r44;
                goto L_0x029c;
            L_0x022e:
                r38 = r4;
                r39 = r5;
                r40 = r8;
                r41 = r9;
                r42 = r14;
            L_0x0238:
                r3 = r36;
                r4 = r38;
                r5 = r39;
                r8 = r40;
                r9 = r41;
                r14 = r42;
                goto L_0x00ad;
            L_0x0246:
                r39 = r5;
                goto L_0x027e;
            L_0x0249:
                r0 = move-exception;
                r39 = r5;
                r3 = r0;
                goto L_0x04c6;
            L_0x024f:
                r0 = move-exception;
                r39 = r5;
                r3 = r0;
                r4 = r24;
                goto L_0x029c;
            L_0x0256:
                r0 = move-exception;
                r24 = r4;
                r39 = r5;
                r3 = r0;
                goto L_0x04c6;
            L_0x025e:
                r0 = move-exception;
                r24 = r4;
                r39 = r5;
                r3 = r0;
                goto L_0x029c;
            L_0x0265:
                r0 = move-exception;
                r23 = r3;
                r24 = r4;
                r39 = r5;
                r3 = r0;
                goto L_0x04c6;
            L_0x026f:
                r0 = move-exception;
                r23 = r3;
                r24 = r4;
                r39 = r5;
                r3 = r0;
                goto L_0x029c;
            L_0x0278:
                r23 = r3;
                r24 = r4;
                r39 = r5;
            L_0x027e:
                if (r5 == 0) goto L_0x028a;
            L_0x0280:
                r5.close();	 Catch:{ Exception -> 0x0284 }
            L_0x0283:
                goto L_0x028a;
            L_0x0284:
                r0 = move-exception;
                r3 = r0;
                org.telegram.messenger.FileLog.e(r3);
                goto L_0x0283;
            L_0x028a:
                r14 = r7;
                r4 = r24;
                goto L_0x02ac;
            L_0x028e:
                r0 = move-exception;
                r23 = r3;
                r24 = r4;
                r3 = r0;
                goto L_0x04c6;
            L_0x0296:
                r0 = move-exception;
                r23 = r3;
                r24 = r4;
                r3 = r0;
            L_0x029c:
                org.telegram.messenger.FileLog.e(r3);	 Catch:{ all -> 0x04c2 }
                if (r5 == 0) goto L_0x02ab;
            L_0x02a1:
                r5.close();	 Catch:{ Exception -> 0x02a5 }
            L_0x02a4:
                goto L_0x02ab;
            L_0x02a5:
                r0 = move-exception;
                r3 = r0;
                org.telegram.messenger.FileLog.e(r3);
                goto L_0x02a4;
            L_0x02ab:
                r14 = r7;
            L_0x02ac:
                r3 = android.os.Build.VERSION.SDK_INT;	 Catch:{ Throwable -> 0x0476, all -> 0x0471 }
                r7 = 23;
                if (r3 < r7) goto L_0x02d3;
            L_0x02b2:
                r3 = android.os.Build.VERSION.SDK_INT;	 Catch:{ Throwable -> 0x02cc, all -> 0x02c6 }
                if (r3 < r7) goto L_0x02c1;
            L_0x02b6:
                r3 = org.telegram.messenger.ApplicationLoader.applicationContext;	 Catch:{ Throwable -> 0x02cc, all -> 0x02c6 }
                r7 = "android.permission.READ_EXTERNAL_STORAGE";
                r3 = r3.checkSelfPermission(r7);	 Catch:{ Throwable -> 0x02cc, all -> 0x02c6 }
                if (r3 != 0) goto L_0x02c1;
            L_0x02c0:
                goto L_0x02d3;
            L_0x02c1:
                r45 = r4;
                r3 = 0;
                goto L_0x0465;
            L_0x02c6:
                r0 = move-exception;
                r3 = r0;
                r45 = r4;
                goto L_0x04b6;
            L_0x02cc:
                r0 = move-exception;
                r45 = r4;
                r3 = 0;
                r4 = r0;
                goto L_0x047b;
            L_0x02d3:
                r3 = org.telegram.messenger.ApplicationLoader.applicationContext;	 Catch:{ Throwable -> 0x0476, all -> 0x0471 }
                r15 = r3.getContentResolver();	 Catch:{ Throwable -> 0x0476, all -> 0x0471 }
                r16 = android.provider.MediaStore.Video.Media.EXTERNAL_CONTENT_URI;	 Catch:{ Throwable -> 0x0476, all -> 0x0471 }
                r17 = org.telegram.messenger.MediaController.projectionVideo;	 Catch:{ Throwable -> 0x0476, all -> 0x0471 }
                r18 = 0;
                r19 = 0;
                r20 = "datetaken DESC";
                r3 = android.provider.MediaStore.Images.Media.query(r15, r16, r17, r18, r19, r20);	 Catch:{ Throwable -> 0x0476, all -> 0x0471 }
                r5 = r3;
                if (r5 == 0) goto L_0x0460;
            L_0x02ec:
                r3 = "_id";
                r3 = r5.getColumnIndex(r3);	 Catch:{ Throwable -> 0x0458, all -> 0x0450 }
                r7 = "bucket_id";
                r7 = r5.getColumnIndex(r7);	 Catch:{ Throwable -> 0x0458, all -> 0x0450 }
                r8 = "bucket_display_name";
                r8 = r5.getColumnIndex(r8);	 Catch:{ Throwable -> 0x0458, all -> 0x0450 }
                r9 = "_data";
                r9 = r5.getColumnIndex(r9);	 Catch:{ Throwable -> 0x0458, all -> 0x0450 }
                r15 = "datetaken";
                r15 = r5.getColumnIndex(r15);	 Catch:{ Throwable -> 0x0458, all -> 0x0450 }
                r45 = r4;
                r4 = "duration";
                r4 = r5.getColumnIndex(r4);	 Catch:{ Throwable -> 0x044a, all -> 0x0444 }
            L_0x0312:
                r16 = r5.moveToNext();	 Catch:{ Throwable -> 0x044a, all -> 0x0444 }
                if (r16 == 0) goto L_0x0440;
            L_0x0318:
                r26 = r5.getInt(r3);	 Catch:{ Throwable -> 0x044a, all -> 0x0444 }
                r16 = r5.getInt(r7);	 Catch:{ Throwable -> 0x044a, all -> 0x0444 }
                r46 = r16;
                r16 = r5.getString(r8);	 Catch:{ Throwable -> 0x044a, all -> 0x0444 }
                r47 = r16;
                r16 = r5.getString(r9);	 Catch:{ Throwable -> 0x044a, all -> 0x0444 }
                r48 = r16;
                r27 = r5.getLong(r15);	 Catch:{ Throwable -> 0x044a, all -> 0x0444 }
                r16 = r5.getLong(r4);	 Catch:{ Throwable -> 0x044a, all -> 0x0444 }
                r49 = r3;
                r3 = r48;
                if (r3 == 0) goto L_0x042b;
            L_0x033c:
                r18 = r3.length();	 Catch:{ Throwable -> 0x044a, all -> 0x0444 }
                if (r18 != 0) goto L_0x034e;
            L_0x0343:
                r51 = r4;
                r50 = r5;
                r52 = r7;
                r53 = r8;
                r3 = 0;
                goto L_0x0434;
            L_0x034e:
                r18 = new org.telegram.messenger.MediaController$PhotoEntry;	 Catch:{ Throwable -> 0x044a, all -> 0x0444 }
                r19 = 1000; // 0x3e8 float:1.401E-42 double:4.94E-321;
                r51 = r4;
                r50 = r5;
                r4 = r16 / r19;
                r4 = (int) r4;	 Catch:{ Throwable -> 0x0424, all -> 0x041e }
                r31 = 1;
                r24 = r18;
                r25 = r46;
                r29 = r3;
                r30 = r4;
                r24.<init>(r25, r26, r27, r29, r30, r31);	 Catch:{ Throwable -> 0x0424, all -> 0x041e }
                r4 = r18;
                if (r45 != 0) goto L_0x0398;
            L_0x036a:
                r5 = new org.telegram.messenger.MediaController$AlbumEntry;	 Catch:{ Throwable -> 0x0391, all -> 0x041e }
                r52 = r7;
                r7 = "AllMedia";
                r53 = r8;
                r8 = 2131492949; // 0x7f0c0055 float:1.8609364E38 double:1.0530974405E-314;
                r7 = org.telegram.messenger.LocaleController.getString(r7, r8);	 Catch:{ Throwable -> 0x0391, all -> 0x041e }
                r8 = 0;
                r5.<init>(r8, r7, r4);	 Catch:{ Throwable -> 0x038c, all -> 0x041e }
                r2.add(r8, r5);	 Catch:{ Throwable -> 0x0387, all -> 0x0381 }
                goto L_0x039e;
            L_0x0381:
                r0 = move-exception;
                r3 = r0;
                r45 = r5;
                goto L_0x0420;
            L_0x0387:
                r0 = move-exception;
                r4 = r0;
                r45 = r5;
                goto L_0x038e;
            L_0x038c:
                r0 = move-exception;
                r4 = r0;
            L_0x038e:
                r3 = r8;
                goto L_0x0427;
            L_0x0391:
                r0 = move-exception;
                r4 = r0;
                r5 = r50;
                r3 = 0;
                goto L_0x047b;
            L_0x0398:
                r52 = r7;
                r53 = r8;
                r5 = r45;
            L_0x039e:
                r5.addPhoto(r4);	 Catch:{ Throwable -> 0x0413, all -> 0x0409 }
                r7 = r46;
                r8 = r11.get(r7);	 Catch:{ Throwable -> 0x0413, all -> 0x0409 }
                r8 = (org.telegram.messenger.MediaController.AlbumEntry) r8;	 Catch:{ Throwable -> 0x0413, all -> 0x0409 }
                if (r8 != 0) goto L_0x03ea;
            L_0x03ab:
                r54 = r5;
                r5 = new org.telegram.messenger.MediaController$AlbumEntry;	 Catch:{ Throwable -> 0x03e1, all -> 0x03d9 }
                r55 = r8;
                r8 = r47;
                r5.<init>(r7, r8, r4);	 Catch:{ Throwable -> 0x03e1, all -> 0x03d9 }
                r11.put(r7, r5);	 Catch:{ Throwable -> 0x03e1, all -> 0x03d9 }
                if (r6 != 0) goto L_0x03d2;
            L_0x03bb:
                if (r13 == 0) goto L_0x03d2;
            L_0x03bd:
                if (r3 == 0) goto L_0x03d2;
            L_0x03bf:
                r18 = r3.startsWith(r13);	 Catch:{ Throwable -> 0x03e1, all -> 0x03d9 }
                if (r18 == 0) goto L_0x03d2;
            L_0x03c5:
                r56 = r3;
                r3 = 0;
                r2.add(r3, r5);	 Catch:{ Throwable -> 0x0407, all -> 0x03d9 }
                r18 = java.lang.Integer.valueOf(r7);	 Catch:{ Throwable -> 0x0407, all -> 0x03d9 }
                r6 = r18;
                goto L_0x03f5;
            L_0x03d2:
                r56 = r3;
                r3 = 0;
                r2.add(r5);	 Catch:{ Throwable -> 0x0407, all -> 0x03d9 }
                goto L_0x03f5;
            L_0x03d9:
                r0 = move-exception;
                r3 = r0;
                r5 = r50;
                r45 = r54;
                goto L_0x04b6;
            L_0x03e1:
                r0 = move-exception;
                r3 = 0;
            L_0x03e3:
                r4 = r0;
                r5 = r50;
                r45 = r54;
                goto L_0x047b;
            L_0x03ea:
                r56 = r3;
                r54 = r5;
                r55 = r8;
                r8 = r47;
                r3 = 0;
                r5 = r55;
            L_0x03f5:
                r5.addPhoto(r4);	 Catch:{ Throwable -> 0x0407, all -> 0x03d9 }
                r3 = r49;
                r5 = r50;
                r4 = r51;
                r7 = r52;
                r8 = r53;
                r45 = r54;
                goto L_0x0312;
            L_0x0407:
                r0 = move-exception;
                goto L_0x03e3;
            L_0x0409:
                r0 = move-exception;
                r54 = r5;
                r3 = r0;
                r5 = r50;
                r45 = r54;
                goto L_0x04b6;
            L_0x0413:
                r0 = move-exception;
                r54 = r5;
                r3 = 0;
                r4 = r0;
                r5 = r50;
                r45 = r54;
                goto L_0x047b;
            L_0x041e:
                r0 = move-exception;
                r3 = r0;
            L_0x0420:
                r5 = r50;
                goto L_0x04b6;
            L_0x0424:
                r0 = move-exception;
                r3 = 0;
                r4 = r0;
            L_0x0427:
                r5 = r50;
                goto L_0x047b;
            L_0x042b:
                r51 = r4;
                r50 = r5;
                r52 = r7;
                r53 = r8;
                r3 = 0;
            L_0x0434:
                r3 = r49;
                r5 = r50;
                r4 = r51;
                r7 = r52;
                r8 = r53;
                goto L_0x0312;
            L_0x0440:
                r50 = r5;
                r3 = 0;
                goto L_0x0465;
            L_0x0444:
                r0 = move-exception;
                r50 = r5;
                r3 = r0;
                goto L_0x04b6;
            L_0x044a:
                r0 = move-exception;
                r50 = r5;
                r3 = 0;
                r4 = r0;
                goto L_0x047b;
            L_0x0450:
                r0 = move-exception;
                r45 = r4;
                r50 = r5;
                r3 = r0;
                goto L_0x04b6;
            L_0x0458:
                r0 = move-exception;
                r45 = r4;
                r50 = r5;
                r3 = 0;
                r4 = r0;
                goto L_0x047b;
            L_0x0460:
                r45 = r4;
                r50 = r5;
                r3 = 0;
            L_0x0465:
                if (r5 == 0) goto L_0x048a;
            L_0x0467:
                r5.close();	 Catch:{ Exception -> 0x046b }
                goto L_0x0483;
            L_0x046b:
                r0 = move-exception;
                r4 = r0;
                org.telegram.messenger.FileLog.e(r4);
                goto L_0x0483;
            L_0x0471:
                r0 = move-exception;
                r45 = r4;
                r3 = r0;
                goto L_0x04b6;
            L_0x0476:
                r0 = move-exception;
                r45 = r4;
                r3 = 0;
                r4 = r0;
            L_0x047b:
                org.telegram.messenger.FileLog.e(r4);	 Catch:{ all -> 0x04b4 }
                if (r5 == 0) goto L_0x048a;
            L_0x0480:
                r5.close();	 Catch:{ Exception -> 0x0484 }
            L_0x0483:
                goto L_0x048a;
            L_0x0484:
                r0 = move-exception;
                r4 = r0;
                org.telegram.messenger.FileLog.e(r4);
                goto L_0x0483;
            L_0x048a:
                r16 = r5;
                r15 = r6;
            L_0x048d:
                r4 = r2.size();
                if (r3 >= r4) goto L_0x04a6;
            L_0x0493:
                r4 = r2.get(r3);
                r4 = (org.telegram.messenger.MediaController.AlbumEntry) r4;
                r4 = r4.photos;
                r5 = new org.telegram.messenger.MediaController$29$1;
                r5.<init>();
                java.util.Collections.sort(r4, r5);
                r3 = r3 + 1;
                goto L_0x048d;
            L_0x04a6:
                r3 = r2;
                r9 = 0;
                r4 = r2;
                r5 = r10;
                r6 = r15;
                r7 = r45;
                r8 = r23;
                org.telegram.messenger.MediaController.broadcastNewPhotos(r3, r4, r5, r6, r7, r8, r9);
                return;
            L_0x04b4:
                r0 = move-exception;
                r3 = r0;
            L_0x04b6:
                if (r5 == 0) goto L_0x04c1;
            L_0x04b8:
                r5.close();	 Catch:{ Exception -> 0x04bc }
                goto L_0x04c1;
            L_0x04bc:
                r0 = move-exception;
                r4 = r0;
                org.telegram.messenger.FileLog.e(r4);
            L_0x04c1:
                throw r3;
            L_0x04c2:
                r0 = move-exception;
                r3 = r0;
                r24 = r4;
            L_0x04c6:
                if (r5 == 0) goto L_0x04d1;
            L_0x04c8:
                r5.close();	 Catch:{ Exception -> 0x04cc }
                goto L_0x04d1;
            L_0x04cc:
                r0 = move-exception;
                r4 = r0;
                org.telegram.messenger.FileLog.e(r4);
            L_0x04d1:
                throw r3;
                */
                throw new UnsupportedOperationException("Method not decompiled: org.telegram.messenger.MediaController.29.run():void");
            }
        });
        thread.setPriority(1);
        thread.start();
    }

    private static void broadcastNewPhotos(int guid, ArrayList<AlbumEntry> mediaAlbumsSorted, ArrayList<AlbumEntry> photoAlbumsSorted, Integer cameraAlbumIdFinal, AlbumEntry allMediaAlbumFinal, AlbumEntry allPhotosAlbumFinal, int delay) {
        if (broadcastPhotosRunnable != null) {
            AndroidUtilities.cancelRunOnUIThread(broadcastPhotosRunnable);
        }
        final int i = guid;
        final ArrayList<AlbumEntry> arrayList = mediaAlbumsSorted;
        final ArrayList<AlbumEntry> arrayList2 = photoAlbumsSorted;
        final Integer num = cameraAlbumIdFinal;
        final AlbumEntry albumEntry = allMediaAlbumFinal;
        final AlbumEntry albumEntry2 = allPhotosAlbumFinal;
        Runnable anonymousClass30 = new Runnable() {
            public void run() {
                if (PhotoViewer.getInstance().isVisible()) {
                    MediaController.broadcastNewPhotos(i, arrayList, arrayList2, num, albumEntry, albumEntry2, 1000);
                    return;
                }
                MediaController.broadcastPhotosRunnable = null;
                MediaController.allPhotosAlbumEntry = albumEntry2;
                MediaController.allMediaAlbumEntry = albumEntry;
                for (int a = 0; a < 3; a++) {
                    NotificationCenter.getInstance(a).postNotificationName(NotificationCenter.albumsDidLoaded, Integer.valueOf(i), arrayList, arrayList2, num);
                }
            }
        };
        broadcastPhotosRunnable = anonymousClass30;
        AndroidUtilities.runOnUIThread(anonymousClass30, (long) delay);
    }

    public void scheduleVideoConvert(MessageObject messageObject) {
        scheduleVideoConvert(messageObject, false);
    }

    public boolean scheduleVideoConvert(MessageObject messageObject, boolean isEmpty) {
        if (messageObject != null) {
            if (messageObject.videoEditedInfo != null) {
                if (isEmpty && !this.videoConvertQueue.isEmpty()) {
                    return false;
                }
                if (isEmpty) {
                    new File(messageObject.messageOwner.attachPath).delete();
                }
                this.videoConvertQueue.add(messageObject);
                if (this.videoConvertQueue.size() == 1) {
                    startVideoConvertFromQueue();
                }
                return true;
            }
        }
        return false;
    }

    public void cancelVideoConvert(MessageObject messageObject) {
        if (messageObject == null) {
            synchronized (this.videoConvertSync) {
                this.cancelCurrentVideoConversion = true;
            }
        } else if (!this.videoConvertQueue.isEmpty()) {
            int a = 0;
            while (a < this.videoConvertQueue.size()) {
                MessageObject object = (MessageObject) this.videoConvertQueue.get(a);
                if (object.getId() != messageObject.getId() || object.currentAccount != messageObject.currentAccount) {
                    a++;
                } else if (a == 0) {
                    synchronized (this.videoConvertSync) {
                        this.cancelCurrentVideoConversion = true;
                    }
                    return;
                } else {
                    this.videoConvertQueue.remove(a);
                    return;
                }
            }
        }
    }

    private boolean startVideoConvertFromQueue() {
        int a = 0;
        if (this.videoConvertQueue.isEmpty()) {
            return false;
        }
        synchronized (this.videoConvertSync) {
            this.cancelCurrentVideoConversion = false;
        }
        MessageObject messageObject = (MessageObject) this.videoConvertQueue.get(0);
        Intent intent = new Intent(ApplicationLoader.applicationContext, VideoEncodingService.class);
        intent.putExtra("path", messageObject.messageOwner.attachPath);
        intent.putExtra("currentAccount", messageObject.currentAccount);
        if (messageObject.messageOwner.media.document != null) {
            while (a < messageObject.messageOwner.media.document.attributes.size()) {
                if (((DocumentAttribute) messageObject.messageOwner.media.document.attributes.get(a)) instanceof TL_documentAttributeAnimated) {
                    intent.putExtra("gif", true);
                    break;
                }
                a++;
            }
        }
        if (messageObject.getId() != 0) {
            try {
                ApplicationLoader.applicationContext.startService(intent);
            } catch (Throwable e) {
                FileLog.e(e);
            }
        }
        VideoConvertRunnable.runConversion(messageObject);
        return true;
    }

    @SuppressLint({"NewApi"})
    public static MediaCodecInfo selectCodec(String mimeType) {
        int numCodecs = MediaCodecList.getCodecCount();
        MediaCodecInfo lastCodecInfo = null;
        for (int i = 0; i < numCodecs; i++) {
            MediaCodecInfo codecInfo = MediaCodecList.getCodecInfoAt(i);
            if (codecInfo.isEncoder()) {
                MediaCodecInfo lastCodecInfo2 = lastCodecInfo;
                for (String type : codecInfo.getSupportedTypes()) {
                    if (type.equalsIgnoreCase(mimeType)) {
                        lastCodecInfo2 = codecInfo;
                        String name = lastCodecInfo2.getName();
                        if (name != null && (!name.equals("OMX.SEC.avc.enc") || name.equals("OMX.SEC.AVC.Encoder"))) {
                            return lastCodecInfo2;
                        }
                    }
                }
                lastCodecInfo = lastCodecInfo2;
            }
        }
        return lastCodecInfo;
    }

    private static boolean isRecognizedFormat(int colorFormat) {
        if (!(colorFormat == 39 || colorFormat == 2130706688)) {
            switch (colorFormat) {
                case 19:
                case 20:
                case 21:
                    break;
                default:
                    return false;
            }
        }
        return true;
    }

    @SuppressLint({"NewApi"})
    public static int selectColorFormat(MediaCodecInfo codecInfo, String mimeType) {
        CodecCapabilities capabilities = codecInfo.getCapabilitiesForType(mimeType);
        int lastColorFormat = 0;
        for (int colorFormat : capabilities.colorFormats) {
            if (isRecognizedFormat(colorFormat)) {
                lastColorFormat = colorFormat;
                if (!codecInfo.getName().equals("OMX.SEC.AVC.Encoder") || colorFormat != 19) {
                    return colorFormat;
                }
            }
        }
        return lastColorFormat;
    }

    private int findTrack(MediaExtractor extractor, boolean audio) {
        int numTracks = extractor.getTrackCount();
        for (int i = 0; i < numTracks; i++) {
            String mime = extractor.getTrackFormat(i).getString("mime");
            if (audio) {
                if (mime.startsWith("audio/")) {
                    return i;
                }
            } else if (mime.startsWith("video/")) {
                return i;
            }
        }
        return -5;
    }

    private void didWriteData(MessageObject messageObject, File file, boolean last, boolean error) {
        boolean firstWrite = this.videoConvertFirstWrite;
        if (firstWrite) {
            this.videoConvertFirstWrite = false;
        }
        final boolean z = error;
        final boolean z2 = last;
        final MessageObject messageObject2 = messageObject;
        final File file2 = file;
        final boolean z3 = firstWrite;
        AndroidUtilities.runOnUIThread(new Runnable() {
            public void run() {
                if (z || z2) {
                    synchronized (MediaController.this.videoConvertSync) {
                        MediaController.this.cancelCurrentVideoConversion = false;
                    }
                    MediaController.this.videoConvertQueue.remove(messageObject2);
                    MediaController.this.startVideoConvertFromQueue();
                }
                if (z) {
                    NotificationCenter.getInstance(messageObject2.currentAccount).postNotificationName(NotificationCenter.FilePreparingFailed, messageObject2, file2.toString());
                    return;
                }
                if (z3) {
                    NotificationCenter.getInstance(messageObject2.currentAccount).postNotificationName(NotificationCenter.FilePreparingStarted, messageObject2, file2.toString());
                }
                NotificationCenter instance = NotificationCenter.getInstance(messageObject2.currentAccount);
                int i = NotificationCenter.FileNewChunkAvailable;
                Object[] objArr = new Object[4];
                objArr[0] = messageObject2;
                objArr[1] = file2.toString();
                objArr[2] = Long.valueOf(file2.length());
                objArr[3] = Long.valueOf(z2 ? file2.length() : 0);
                instance.postNotificationName(i, objArr);
            }
        });
    }

    private long readAndWriteTracks(MessageObject messageObject, MediaExtractor extractor, MP4Builder mediaMuxer, BufferInfo info, long start, long end, File file, boolean needAudio) throws Exception {
        MediaController mediaController;
        int videoTrackIndex;
        int maxBufferSize;
        int videoTrackIndex2;
        int maxBufferSize2;
        int muxerVideoTrackIndex;
        int i;
        int i2;
        MediaController mediaController2;
        MediaExtractor mediaExtractor = extractor;
        MP4Builder mP4Builder = mediaMuxer;
        BufferInfo bufferInfo = info;
        long j = start;
        int videoTrackIndex3 = findTrack(mediaExtractor, false);
        int audioTrackIndex = needAudio ? findTrack(mediaExtractor, true) : -1;
        int muxerVideoTrackIndex2 = -1;
        int muxerAudioTrackIndex = -1;
        boolean inputDone = false;
        if (videoTrackIndex3 >= 0) {
            mediaExtractor.selectTrack(videoTrackIndex3);
            MediaFormat trackFormat = mediaExtractor.getTrackFormat(videoTrackIndex3);
            muxerVideoTrackIndex2 = mP4Builder.addTrack(trackFormat, false);
            int maxBufferSize3 = trackFormat.getInteger("max-input-size");
            videoTrackIndex = videoTrackIndex3;
            if (j > 0) {
                mediaExtractor.seekTo(j, 0);
            } else {
                mediaExtractor.seekTo(0, 0);
            }
            maxBufferSize = maxBufferSize3;
        } else {
            videoTrackIndex = videoTrackIndex3;
            maxBufferSize = 0;
        }
        if (audioTrackIndex >= 0) {
            int i3;
            mediaExtractor.selectTrack(audioTrackIndex);
            MediaFormat trackFormat2 = mediaExtractor.getTrackFormat(audioTrackIndex);
            muxerAudioTrackIndex = mP4Builder.addTrack(trackFormat2, true);
            maxBufferSize = Math.max(trackFormat2.getInteger("max-input-size"), maxBufferSize);
            if (j > 0) {
                mediaExtractor.seekTo(j, 0);
                i3 = maxBufferSize;
            } else {
                i3 = maxBufferSize;
                MediaFormat mediaFormat = trackFormat2;
                mediaExtractor.seekTo(0, 0);
            }
            maxBufferSize = i3;
        }
        ByteBuffer buffer = ByteBuffer.allocateDirect(maxBufferSize);
        if (audioTrackIndex < 0) {
            if (videoTrackIndex < 0) {
                return -1;
            }
        }
        long startTime = -1;
        checkConversionCanceled();
        while (!inputDone) {
            byte[] array;
            int a;
            int i4;
            MessageObject messageObject2;
            checkConversionCanceled();
            boolean eof = false;
            bufferInfo.size = mediaExtractor.readSampleData(buffer, 0);
            int index = extractor.getSampleTrackIndex();
            videoTrackIndex2 = videoTrackIndex;
            if (index == videoTrackIndex2) {
                videoTrackIndex = muxerVideoTrackIndex2;
            } else if (index == audioTrackIndex) {
                videoTrackIndex = muxerAudioTrackIndex;
            } else {
                videoTrackIndex = -1;
                maxBufferSize2 = maxBufferSize;
                maxBufferSize = videoTrackIndex;
                if (maxBufferSize == -1) {
                    muxerVideoTrackIndex = muxerVideoTrackIndex2;
                    if (VERSION.SDK_INT < 21) {
                        buffer.position(0);
                        buffer.limit(bufferInfo.size);
                    }
                    if (index != audioTrackIndex) {
                        array = buffer.array();
                        if (array != null) {
                            muxerVideoTrackIndex2 = buffer.arrayOffset();
                            videoTrackIndex = muxerVideoTrackIndex2 + buffer.limit();
                            int offset = muxerVideoTrackIndex2;
                            muxerVideoTrackIndex2 = -1;
                            a = offset;
                            while (true) {
                                i = muxerAudioTrackIndex;
                                i2 = audioTrackIndex;
                                audioTrackIndex = a;
                                if (audioTrackIndex <= videoTrackIndex - 4) {
                                    break;
                                }
                                if (array[audioTrackIndex] != (byte) 0 && array[audioTrackIndex + 1] == (byte) 0 && array[audioTrackIndex + 2] == (byte) 0) {
                                    if (array[audioTrackIndex + 3] != (byte) 1) {
                                    }
                                    if (muxerVideoTrackIndex2 != -1) {
                                        muxerAudioTrackIndex = (audioTrackIndex - muxerVideoTrackIndex2) - (audioTrackIndex != videoTrackIndex + -4 ? 4 : 0);
                                        array[muxerVideoTrackIndex2] = (byte) (muxerAudioTrackIndex >> 24);
                                        array[muxerVideoTrackIndex2 + 1] = (byte) (muxerAudioTrackIndex >> 16);
                                        array[muxerVideoTrackIndex2 + 2] = (byte) (muxerAudioTrackIndex >> 8);
                                        array[muxerVideoTrackIndex2 + 3] = (byte) muxerAudioTrackIndex;
                                        i4 = audioTrackIndex;
                                    } else {
                                        i4 = audioTrackIndex;
                                    }
                                    muxerVideoTrackIndex2 = i4;
                                    a = audioTrackIndex + 1;
                                    muxerAudioTrackIndex = i;
                                    audioTrackIndex = i2;
                                    mediaController = this;
                                    mP4Builder = mediaMuxer;
                                }
                                if (audioTrackIndex != videoTrackIndex - 4) {
                                    a = audioTrackIndex + 1;
                                    muxerAudioTrackIndex = i;
                                    audioTrackIndex = i2;
                                    mediaController = this;
                                    mP4Builder = mediaMuxer;
                                }
                                if (muxerVideoTrackIndex2 != -1) {
                                    i4 = audioTrackIndex;
                                } else {
                                    if (audioTrackIndex != videoTrackIndex + -4) {
                                    }
                                    muxerAudioTrackIndex = (audioTrackIndex - muxerVideoTrackIndex2) - (audioTrackIndex != videoTrackIndex + -4 ? 4 : 0);
                                    array[muxerVideoTrackIndex2] = (byte) (muxerAudioTrackIndex >> 24);
                                    array[muxerVideoTrackIndex2 + 1] = (byte) (muxerAudioTrackIndex >> 16);
                                    array[muxerVideoTrackIndex2 + 2] = (byte) (muxerAudioTrackIndex >> 8);
                                    array[muxerVideoTrackIndex2 + 3] = (byte) muxerAudioTrackIndex;
                                    i4 = audioTrackIndex;
                                }
                                muxerVideoTrackIndex2 = i4;
                                a = audioTrackIndex + 1;
                                muxerAudioTrackIndex = i;
                                audioTrackIndex = i2;
                                mediaController = this;
                                mP4Builder = mediaMuxer;
                            }
                            if (bufferInfo.size < 0) {
                                bufferInfo.presentationTimeUs = extractor.getSampleTime();
                            } else {
                                bufferInfo.size = 0;
                                eof = true;
                            }
                            if (bufferInfo.size > 0 || eof) {
                                messageObject2 = messageObject;
                                muxerVideoTrackIndex2 = file;
                            } else {
                                File file2;
                                if (index == videoTrackIndex2 && j > 0) {
                                    if (startTime == -1) {
                                        startTime = bufferInfo.presentationTimeUs;
                                    }
                                }
                                if (end >= 0) {
                                    if (bufferInfo.presentationTimeUs >= end) {
                                        messageObject2 = messageObject;
                                        file2 = file;
                                        eof = true;
                                    }
                                }
                                bufferInfo.offset = 0;
                                bufferInfo.flags = extractor.getSampleFlags();
                                if (mediaMuxer.writeSampleData(maxBufferSize, buffer, bufferInfo, false)) {
                                    didWriteData(messageObject, file, false, false);
                                    if (!eof) {
                                        extractor.advance();
                                    }
                                } else {
                                    messageObject2 = messageObject;
                                    file2 = file;
                                    mediaController2 = this;
                                    if (eof) {
                                        extractor.advance();
                                    }
                                }
                            }
                            mP4Builder = mediaMuxer;
                            mediaController2 = this;
                            if (eof) {
                                extractor.advance();
                            }
                        }
                    }
                    i2 = audioTrackIndex;
                    i = muxerAudioTrackIndex;
                    if (bufferInfo.size < 0) {
                        bufferInfo.size = 0;
                        eof = true;
                    } else {
                        bufferInfo.presentationTimeUs = extractor.getSampleTime();
                    }
                    if (bufferInfo.size > 0) {
                    }
                    messageObject2 = messageObject;
                    muxerVideoTrackIndex2 = file;
                    mP4Builder = mediaMuxer;
                    mediaController2 = this;
                    if (eof) {
                        extractor.advance();
                    }
                } else {
                    mediaController2 = mediaController;
                    i2 = audioTrackIndex;
                    muxerVideoTrackIndex = muxerVideoTrackIndex2;
                    i = muxerAudioTrackIndex;
                    audioTrackIndex = messageObject;
                    muxerVideoTrackIndex2 = file;
                    if (index != -1) {
                        eof = true;
                    } else {
                        extractor.advance();
                    }
                }
                if (eof) {
                    inputDone = true;
                }
                videoTrackIndex = videoTrackIndex2;
                mediaController = mediaController2;
                maxBufferSize = maxBufferSize2;
                muxerVideoTrackIndex2 = muxerVideoTrackIndex;
                muxerAudioTrackIndex = i;
                audioTrackIndex = i2;
            }
            maxBufferSize2 = maxBufferSize;
            maxBufferSize = videoTrackIndex;
            if (maxBufferSize == -1) {
                mediaController2 = mediaController;
                i2 = audioTrackIndex;
                muxerVideoTrackIndex = muxerVideoTrackIndex2;
                i = muxerAudioTrackIndex;
                audioTrackIndex = messageObject;
                muxerVideoTrackIndex2 = file;
                if (index != -1) {
                    extractor.advance();
                } else {
                    eof = true;
                }
            } else {
                muxerVideoTrackIndex = muxerVideoTrackIndex2;
                if (VERSION.SDK_INT < 21) {
                    buffer.position(0);
                    buffer.limit(bufferInfo.size);
                }
                if (index != audioTrackIndex) {
                    array = buffer.array();
                    if (array != null) {
                        muxerVideoTrackIndex2 = buffer.arrayOffset();
                        videoTrackIndex = muxerVideoTrackIndex2 + buffer.limit();
                        int offset2 = muxerVideoTrackIndex2;
                        muxerVideoTrackIndex2 = -1;
                        a = offset2;
                        while (true) {
                            i = muxerAudioTrackIndex;
                            i2 = audioTrackIndex;
                            audioTrackIndex = a;
                            if (audioTrackIndex <= videoTrackIndex - 4) {
                                break;
                            }
                            if (array[audioTrackIndex] != (byte) 0) {
                            }
                            if (audioTrackIndex != videoTrackIndex - 4) {
                                a = audioTrackIndex + 1;
                                muxerAudioTrackIndex = i;
                                audioTrackIndex = i2;
                                mediaController = this;
                                mP4Builder = mediaMuxer;
                            }
                            if (muxerVideoTrackIndex2 != -1) {
                                if (audioTrackIndex != videoTrackIndex + -4) {
                                }
                                muxerAudioTrackIndex = (audioTrackIndex - muxerVideoTrackIndex2) - (audioTrackIndex != videoTrackIndex + -4 ? 4 : 0);
                                array[muxerVideoTrackIndex2] = (byte) (muxerAudioTrackIndex >> 24);
                                array[muxerVideoTrackIndex2 + 1] = (byte) (muxerAudioTrackIndex >> 16);
                                array[muxerVideoTrackIndex2 + 2] = (byte) (muxerAudioTrackIndex >> 8);
                                array[muxerVideoTrackIndex2 + 3] = (byte) muxerAudioTrackIndex;
                                i4 = audioTrackIndex;
                            } else {
                                i4 = audioTrackIndex;
                            }
                            muxerVideoTrackIndex2 = i4;
                            a = audioTrackIndex + 1;
                            muxerAudioTrackIndex = i;
                            audioTrackIndex = i2;
                            mediaController = this;
                            mP4Builder = mediaMuxer;
                        }
                        if (bufferInfo.size < 0) {
                            bufferInfo.presentationTimeUs = extractor.getSampleTime();
                        } else {
                            bufferInfo.size = 0;
                            eof = true;
                        }
                        if (bufferInfo.size > 0) {
                        }
                        messageObject2 = messageObject;
                        muxerVideoTrackIndex2 = file;
                        mP4Builder = mediaMuxer;
                        mediaController2 = this;
                        if (eof) {
                            extractor.advance();
                        }
                    }
                }
                i2 = audioTrackIndex;
                i = muxerAudioTrackIndex;
                if (bufferInfo.size < 0) {
                    bufferInfo.size = 0;
                    eof = true;
                } else {
                    bufferInfo.presentationTimeUs = extractor.getSampleTime();
                }
                if (bufferInfo.size > 0) {
                }
                messageObject2 = messageObject;
                muxerVideoTrackIndex2 = file;
                mP4Builder = mediaMuxer;
                mediaController2 = this;
                if (eof) {
                    extractor.advance();
                }
            }
            if (eof) {
                inputDone = true;
            }
            videoTrackIndex = videoTrackIndex2;
            mediaController = mediaController2;
            maxBufferSize = maxBufferSize2;
            muxerVideoTrackIndex2 = muxerVideoTrackIndex;
            muxerAudioTrackIndex = i;
            audioTrackIndex = i2;
        }
        mediaController2 = mediaController;
        maxBufferSize2 = maxBufferSize;
        i2 = audioTrackIndex;
        muxerVideoTrackIndex = muxerVideoTrackIndex2;
        i = muxerAudioTrackIndex;
        videoTrackIndex2 = videoTrackIndex;
        audioTrackIndex = messageObject;
        muxerVideoTrackIndex2 = file;
        if (videoTrackIndex2 >= 0) {
            mediaExtractor.unselectTrack(videoTrackIndex2);
        }
        if (i2 >= 0) {
            mediaExtractor.unselectTrack(i2);
        }
        return startTime;
    }

    private void checkConversionCanceled() throws Exception {
        synchronized (this.videoConvertSync) {
            boolean cancelConversion = this.cancelCurrentVideoConversion;
        }
        if (cancelConversion) {
            throw new RuntimeException("canceled conversion");
        }
    }
}
