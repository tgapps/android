package org.telegram.ui.Components;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.SurfaceTexture;
import android.media.AudioManager;
import android.media.AudioManager.OnAudioFocusChangeListener;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build.VERSION;
import android.text.Layout.Alignment;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.Base64;
import android.view.MotionEvent;
import android.view.TextureView;
import android.view.TextureView.SurfaceTextureListener;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewTreeObserver.OnPreDrawListener;
import android.webkit.JavascriptInterface;
import android.webkit.ValueCallback;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import net.hockeyapp.android.UpdateFragment;
import org.json.JSONArray;
import org.json.JSONObject;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.ApplicationLoader;
import org.telegram.messenger.Bitmaps;
import org.telegram.messenger.BuildVars;
import org.telegram.messenger.FileLoader;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.ImageReceiver;
import org.telegram.messenger.Utilities;
import org.telegram.messenger.beta.R;
import org.telegram.messenger.exoplayer2.C;
import org.telegram.messenger.exoplayer2.ui.AspectRatioFrameLayout;
import org.telegram.messenger.exoplayer2.util.MimeTypes;
import org.telegram.tgnet.TLRPC.Photo;
import org.telegram.tgnet.TLRPC.PhotoSize;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Components.VideoPlayer.VideoPlayerDelegate;

public class WebPlayerView extends ViewGroup implements OnAudioFocusChangeListener, VideoPlayerDelegate {
    private static final int AUDIO_FOCUSED = 2;
    private static final int AUDIO_NO_FOCUS_CAN_DUCK = 1;
    private static final int AUDIO_NO_FOCUS_NO_DUCK = 0;
    private static final Pattern aparatFileListPattern = Pattern.compile("fileList\\s*=\\s*JSON\\.parse\\('([^']+)'\\)");
    private static final Pattern aparatIdRegex = Pattern.compile("^https?://(?:www\\.)?aparat\\.com/(?:v/|video/video/embed/videohash/)([a-zA-Z0-9]+)");
    private static final Pattern coubIdRegex = Pattern.compile("(?:coub:|https?://(?:coub\\.com/(?:view|embed|coubs)/|c-cdn\\.coub\\.com/fb-player\\.swf\\?.*\\bcoub(?:ID|id)=))([\\da-z]+)");
    private static final String exprName = "[a-zA-Z_$][a-zA-Z_$0-9]*";
    private static final Pattern exprParensPattern = Pattern.compile("[()]");
    private static final Pattern jsPattern = Pattern.compile("\"assets\":.+?\"js\":\\s*(\"[^\"]+\")");
    private static int lastContainerId = 4001;
    private static final Pattern playerIdPattern = Pattern.compile(".*?-([a-zA-Z0-9_-]+)(?:/watch_as3|/html5player(?:-new)?|(?:/[a-z]{2}_[A-Z]{2})?/base)?\\.([a-z]+)$");
    private static final Pattern sigPattern = Pattern.compile("\\.sig\\|\\|([a-zA-Z0-9$]+)\\(");
    private static final Pattern sigPattern2 = Pattern.compile("[\"']signature[\"']\\s*,\\s*([a-zA-Z0-9$]+)\\(");
    private static final Pattern stmtReturnPattern = Pattern.compile("return(?:\\s+|$)");
    private static final Pattern stmtVarPattern = Pattern.compile("var\\s");
    private static final Pattern stsPattern = Pattern.compile("\"sts\"\\s*:\\s*(\\d+)");
    private static final Pattern twitchClipFilePattern = Pattern.compile("clipInfo\\s*=\\s*(\\{[^']+\\});");
    private static final Pattern twitchClipIdRegex = Pattern.compile("https?://clips\\.twitch\\.tv/(?:[^/]+/)*([^/?#&]+)");
    private static final Pattern twitchStreamIdRegex = Pattern.compile("https?://(?:(?:www\\.)?twitch\\.tv/|player\\.twitch\\.tv/\\?.*?\\bchannel=)([^/#?]+)");
    private static final Pattern vimeoIdRegex = Pattern.compile("https?://(?:(?:www|(player))\\.)?vimeo(pro)?\\.com/(?!(?:channels|album)/[^/?#]+/?(?:$|[?#])|[^/]+/review/|ondemand/)(?:.*?/)?(?:(?:play_redirect_hls|moogaloop\\.swf)\\?clip_id=)?(?:videos?/)?([0-9]+)(?:/[\\da-f]+)?/?(?:[?&].*)?(?:[#].*)?$");
    private static final Pattern youtubeIdRegex = Pattern.compile("(?:youtube(?:-nocookie)?\\.com/(?:[^/\\n\\s]+/\\S+/|(?:v|e(?:mbed)?)/|\\S*?[?&]v=)|youtu\\.be/)([a-zA-Z0-9_-]{11})");
    private boolean allowInlineAnimation;
    private AspectRatioFrameLayout aspectRatioFrameLayout;
    private int audioFocus;
    private Paint backgroundPaint;
    private TextureView changedTextureView;
    private boolean changingTextureView;
    private ControlsView controlsView;
    private float currentAlpha;
    private Bitmap currentBitmap;
    private AsyncTask currentTask;
    private String currentYoutubeId;
    private WebPlayerViewDelegate delegate;
    private boolean drawImage;
    private boolean firstFrameRendered;
    private int fragment_container_id;
    private ImageView fullscreenButton;
    private boolean hasAudioFocus;
    private boolean inFullscreen;
    private boolean initFailed;
    private boolean initied;
    private ImageView inlineButton;
    private String interfaceName;
    private boolean isAutoplay;
    private boolean isCompleted;
    private boolean isInline;
    private boolean isLoading;
    private boolean isStream;
    private long lastUpdateTime;
    private String playAudioType;
    private String playAudioUrl;
    private ImageView playButton;
    private String playVideoType;
    private String playVideoUrl;
    private AnimatorSet progressAnimation;
    private Runnable progressRunnable;
    private RadialProgressView progressView;
    private boolean resumeAudioOnFocusGain;
    private int seekToTime;
    private ImageView shareButton;
    private SurfaceTextureListener surfaceTextureListener;
    private Runnable switchToInlineRunnable;
    private boolean switchingInlineMode;
    private ImageView textureImageView;
    private TextureView textureView;
    private ViewGroup textureViewContainer;
    private VideoPlayer videoPlayer;
    private int waitingForFirstTextureUpload;
    private WebView webView;

    private class AparatVideoTask extends AsyncTask<Void, Void, String> {
        private boolean canRetry = true;
        private String[] results = new String[2];
        private String videoId;

        public AparatVideoTask(String vid) {
            this.videoId = vid;
        }

        protected String doInBackground(Void... voids) {
            String playerCode = WebPlayerView.this.downloadUrlContent(this, String.format(Locale.US, "http://www.aparat.com/video/video/embed/vt/frame/showvideo/yes/videohash/%s", new Object[]{this.videoId}));
            String str = null;
            if (isCancelled()) {
                return null;
            }
            try {
                Matcher filelist = WebPlayerView.aparatFileListPattern.matcher(playerCode);
                if (filelist.find()) {
                    JSONArray json = new JSONArray(filelist.group(1));
                    for (int a = 0; a < json.length(); a++) {
                        JSONArray array = json.getJSONArray(a);
                        if (array.length() != 0) {
                            JSONObject object = array.getJSONObject(0);
                            if (object.has("file")) {
                                this.results[0] = object.getString("file");
                                this.results[1] = "other";
                            }
                        }
                    }
                }
            } catch (Throwable e) {
                FileLog.e(e);
            }
            if (!isCancelled()) {
                str = this.results[0];
            }
            return str;
        }

        protected void onPostExecute(String result) {
            if (result != null) {
                WebPlayerView.this.initied = true;
                WebPlayerView.this.playVideoUrl = result;
                WebPlayerView.this.playVideoType = this.results[1];
                if (WebPlayerView.this.isAutoplay) {
                    WebPlayerView.this.preparePlayer();
                }
                WebPlayerView.this.showProgress(false, true);
                WebPlayerView.this.controlsView.show(true, true);
            } else if (!isCancelled()) {
                WebPlayerView.this.onInitFailed();
            }
        }
    }

    public interface CallJavaResultInterface {
        void jsCallFinished(String str);
    }

    private class ControlsView extends FrameLayout {
        private int bufferedPosition;
        private AnimatorSet currentAnimation;
        private int currentProgressX;
        private int duration;
        private StaticLayout durationLayout;
        private int durationWidth;
        private Runnable hideRunnable = new Runnable() {
            public void run() {
                ControlsView.this.show(false, true);
            }
        };
        private ImageReceiver imageReceiver;
        private boolean isVisible = true;
        private int lastProgressX;
        private int progress;
        private Paint progressBufferedPaint;
        private Paint progressInnerPaint;
        private StaticLayout progressLayout;
        private Paint progressPaint;
        private boolean progressPressed;
        private TextPaint textPaint;

        public ControlsView(Context context) {
            super(context);
            setWillNotDraw(false);
            this.textPaint = new TextPaint(1);
            this.textPaint.setColor(-1);
            this.textPaint.setTextSize((float) AndroidUtilities.dp(12.0f));
            this.progressPaint = new Paint(1);
            this.progressPaint.setColor(-15095832);
            this.progressInnerPaint = new Paint();
            this.progressInnerPaint.setColor(-6975081);
            this.progressBufferedPaint = new Paint(1);
            this.progressBufferedPaint.setColor(-1);
            this.imageReceiver = new ImageReceiver(this);
        }

        public void setDuration(int value) {
            if (this.duration != value && value >= 0) {
                if (!WebPlayerView.this.isStream) {
                    this.duration = value;
                    this.durationLayout = new StaticLayout(String.format(Locale.US, "%d:%02d", new Object[]{Integer.valueOf(this.duration / 60), Integer.valueOf(this.duration % 60)}), this.textPaint, AndroidUtilities.dp(1000.0f), Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);
                    if (this.durationLayout.getLineCount() > 0) {
                        this.durationWidth = (int) Math.ceil((double) this.durationLayout.getLineWidth(0));
                    }
                    invalidate();
                }
            }
        }

        public void setBufferedProgress(int position) {
            this.bufferedPosition = position;
            invalidate();
        }

        public void setProgress(int value) {
            if (!this.progressPressed && value >= 0) {
                if (!WebPlayerView.this.isStream) {
                    this.progress = value;
                    this.progressLayout = new StaticLayout(String.format(Locale.US, "%d:%02d", new Object[]{Integer.valueOf(this.progress / 60), Integer.valueOf(this.progress % 60)}), this.textPaint, AndroidUtilities.dp(1000.0f), Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);
                    invalidate();
                }
            }
        }

        public void show(boolean value, boolean animated) {
            if (this.isVisible != value) {
                this.isVisible = value;
                if (this.currentAnimation != null) {
                    this.currentAnimation.cancel();
                }
                AnimatorSet animatorSet;
                Animator[] animatorArr;
                if (this.isVisible) {
                    if (animated) {
                        this.currentAnimation = new AnimatorSet();
                        animatorSet = this.currentAnimation;
                        animatorArr = new Animator[1];
                        animatorArr[0] = ObjectAnimator.ofFloat(this, "alpha", new float[]{1.0f});
                        animatorSet.playTogether(animatorArr);
                        this.currentAnimation.setDuration(150);
                        this.currentAnimation.addListener(new AnimatorListenerAdapter() {
                            public void onAnimationEnd(Animator animator) {
                                ControlsView.this.currentAnimation = null;
                            }
                        });
                        this.currentAnimation.start();
                    } else {
                        setAlpha(1.0f);
                    }
                } else if (animated) {
                    this.currentAnimation = new AnimatorSet();
                    animatorSet = this.currentAnimation;
                    animatorArr = new Animator[1];
                    animatorArr[0] = ObjectAnimator.ofFloat(this, "alpha", new float[]{0.0f});
                    animatorSet.playTogether(animatorArr);
                    this.currentAnimation.setDuration(150);
                    this.currentAnimation.addListener(new AnimatorListenerAdapter() {
                        public void onAnimationEnd(Animator animator) {
                            ControlsView.this.currentAnimation = null;
                        }
                    });
                    this.currentAnimation.start();
                } else {
                    setAlpha(0.0f);
                }
                checkNeedHide();
            }
        }

        private void checkNeedHide() {
            AndroidUtilities.cancelRunOnUIThread(this.hideRunnable);
            if (this.isVisible && WebPlayerView.this.videoPlayer.isPlaying()) {
                AndroidUtilities.runOnUIThread(this.hideRunnable, 3000);
            }
        }

        public boolean onInterceptTouchEvent(MotionEvent ev) {
            if (ev.getAction() != 0) {
                return super.onInterceptTouchEvent(ev);
            }
            if (this.isVisible) {
                onTouchEvent(ev);
                return this.progressPressed;
            }
            show(true, true);
            return true;
        }

        public void requestDisallowInterceptTouchEvent(boolean disallowIntercept) {
            super.requestDisallowInterceptTouchEvent(disallowIntercept);
            checkNeedHide();
        }

        public boolean onTouchEvent(MotionEvent event) {
            int progressLineX;
            int progressLineEndX;
            int progressY;
            if (WebPlayerView.this.inFullscreen) {
                progressLineX = AndroidUtilities.dp(36.0f) + this.durationWidth;
                progressLineEndX = (getMeasuredWidth() - AndroidUtilities.dp(76.0f)) - this.durationWidth;
                progressY = getMeasuredHeight() - AndroidUtilities.dp(28.0f);
            } else {
                progressLineX = 0;
                progressLineEndX = getMeasuredWidth();
                progressY = getMeasuredHeight() - AndroidUtilities.dp(12.0f);
            }
            int progressX = (this.duration != 0 ? (int) (((float) (progressLineEndX - progressLineX)) * (((float) this.progress) / ((float) this.duration))) : 0) + progressLineX;
            int x;
            if (event.getAction() == 0) {
                if (!this.isVisible || WebPlayerView.this.isInline || WebPlayerView.this.isStream) {
                    show(true, true);
                } else if (this.duration != 0) {
                    x = (int) event.getX();
                    int y = (int) event.getY();
                    if (x >= progressX - AndroidUtilities.dp(10.0f) && x <= AndroidUtilities.dp(10.0f) + progressX && y >= progressY - AndroidUtilities.dp(10.0f) && y <= AndroidUtilities.dp(10.0f) + progressY) {
                        this.progressPressed = true;
                        this.lastProgressX = x;
                        this.currentProgressX = progressX;
                        getParent().requestDisallowInterceptTouchEvent(true);
                        invalidate();
                    }
                }
                AndroidUtilities.cancelRunOnUIThread(this.hideRunnable);
            } else {
                if (event.getAction() != 1) {
                    if (event.getAction() != 3) {
                        if (event.getAction() == 2 && this.progressPressed) {
                            x = (int) event.getX();
                            this.currentProgressX -= this.lastProgressX - x;
                            this.lastProgressX = x;
                            if (this.currentProgressX < progressLineX) {
                                this.currentProgressX = progressLineX;
                            } else if (this.currentProgressX > progressLineEndX) {
                                this.currentProgressX = progressLineEndX;
                            }
                            setProgress((int) (((float) (this.duration * 1000)) * (((float) (this.currentProgressX - progressLineX)) / ((float) (progressLineEndX - progressLineX)))));
                            invalidate();
                        }
                    }
                }
                if (WebPlayerView.this.initied && WebPlayerView.this.videoPlayer.isPlaying()) {
                    AndroidUtilities.runOnUIThread(this.hideRunnable, 3000);
                }
                if (this.progressPressed) {
                    this.progressPressed = false;
                    if (WebPlayerView.this.initied) {
                        this.progress = (int) (((float) this.duration) * (((float) (this.currentProgressX - progressLineX)) / ((float) (progressLineEndX - progressLineX))));
                        WebPlayerView.this.videoPlayer.seekTo(((long) this.progress) * 1000);
                    }
                }
            }
            super.onTouchEvent(event);
            return true;
        }

        protected void onDraw(Canvas canvas) {
            Canvas canvas2 = canvas;
            if (WebPlayerView.this.drawImage) {
                if (WebPlayerView.this.firstFrameRendered && WebPlayerView.this.currentAlpha != 0.0f) {
                    long newTime = System.currentTimeMillis();
                    long dt = newTime - WebPlayerView.this.lastUpdateTime;
                    WebPlayerView.this.lastUpdateTime = newTime;
                    WebPlayerView.this.currentAlpha = WebPlayerView.this.currentAlpha - (((float) dt) / 150.0f);
                    if (WebPlayerView.this.currentAlpha < 0.0f) {
                        WebPlayerView.this.currentAlpha = 0.0f;
                    }
                    invalidate();
                }
                r0.imageReceiver.setAlpha(WebPlayerView.this.currentAlpha);
                r0.imageReceiver.draw(canvas2);
            }
            if (WebPlayerView.this.videoPlayer.isPlayerPrepared() && !WebPlayerView.this.isStream) {
                int i;
                int width = getMeasuredWidth();
                int height = getMeasuredHeight();
                if (!WebPlayerView.this.isInline) {
                    i = 10;
                    if (r0.durationLayout != null) {
                        canvas.save();
                        canvas2.translate((float) ((width - AndroidUtilities.dp(58.0f)) - r0.durationWidth), (float) (height - AndroidUtilities.dp((float) ((WebPlayerView.this.inFullscreen ? 6 : 10) + 29))));
                        r0.durationLayout.draw(canvas2);
                        canvas.restore();
                    }
                    if (r0.progressLayout != null) {
                        canvas.save();
                        float dp = (float) AndroidUtilities.dp(18.0f);
                        if (WebPlayerView.this.inFullscreen) {
                            i = 6;
                        }
                        canvas2.translate(dp, (float) (height - AndroidUtilities.dp((float) (29 + i))));
                        r0.progressLayout.draw(canvas2);
                        canvas.restore();
                    }
                }
                if (r0.duration != 0) {
                    int progressLineY;
                    int progressLineEndX;
                    int cy;
                    int progressX;
                    if (WebPlayerView.this.isInline) {
                        progressLineY = height - AndroidUtilities.dp(3.0f);
                        i = 0;
                        progressLineEndX = width;
                        cy = height - AndroidUtilities.dp(7.0f);
                    } else if (WebPlayerView.this.inFullscreen) {
                        progressLineY = height - AndroidUtilities.dp(29.0f);
                        i = AndroidUtilities.dp(36.0f) + r0.durationWidth;
                        progressLineEndX = (width - AndroidUtilities.dp(76.0f)) - r0.durationWidth;
                        cy = height - AndroidUtilities.dp(28.0f);
                    } else {
                        progressLineY = height - AndroidUtilities.dp(13.0f);
                        i = 0;
                        progressLineEndX = width;
                        cy = height - AndroidUtilities.dp(12.0f);
                    }
                    int progressLineY2 = progressLineY;
                    int progressLineX = i;
                    int progressLineEndX2 = progressLineEndX;
                    int cy2 = cy;
                    if (WebPlayerView.this.inFullscreen) {
                        canvas2.drawRect((float) progressLineX, (float) progressLineY2, (float) progressLineEndX2, (float) (AndroidUtilities.dp(3.0f) + progressLineY2), r0.progressInnerPaint);
                    }
                    if (r0.progressPressed) {
                        progressLineY = r0.currentProgressX;
                    } else {
                        progressLineY = ((int) (((float) (progressLineEndX2 - progressLineX)) * (((float) r0.progress) / ((float) r0.duration)))) + progressLineX;
                    }
                    int progressX2 = progressLineY;
                    if (r0.bufferedPosition == 0 || r0.duration == 0) {
                        progressX = progressX2;
                    } else {
                        progressX = progressX2;
                        canvas2.drawRect((float) progressLineX, (float) progressLineY2, (((float) (progressLineEndX2 - progressLineX)) * (((float) r0.bufferedPosition) / ((float) r0.duration))) + ((float) progressLineX), (float) (AndroidUtilities.dp(3.0f) + progressLineY2), WebPlayerView.this.inFullscreen ? r0.progressBufferedPaint : r0.progressInnerPaint);
                    }
                    canvas2.drawRect((float) progressLineX, (float) progressLineY2, (float) progressX, (float) (AndroidUtilities.dp(3.0f) + progressLineY2), r0.progressPaint);
                    if (!WebPlayerView.this.isInline) {
                        canvas2.drawCircle((float) progressX, (float) cy2, (float) AndroidUtilities.dp(r0.progressPressed ? 7.0f : 5.0f), r0.progressPaint);
                    }
                }
            }
        }
    }

    private class CoubVideoTask extends AsyncTask<Void, Void, String> {
        private boolean canRetry = true;
        private String[] results = new String[4];
        private String videoId;

        public CoubVideoTask(String vid) {
            this.videoId = vid;
        }

        private String decodeUrl(String input) {
            StringBuilder source = new StringBuilder(input);
            for (int a = 0; a < source.length(); a++) {
                char c = source.charAt(a);
                char lower = Character.toLowerCase(c);
                source.setCharAt(a, c == lower ? Character.toUpperCase(c) : lower);
            }
            try {
                return new String(Base64.decode(source.toString(), 0), C.UTF8_NAME);
            } catch (Exception e) {
                return null;
            }
        }

        protected String doInBackground(Void... voids) {
            String playerCode = WebPlayerView.this.downloadUrlContent(this, String.format(Locale.US, "https://coub.com/api/v2/coubs/%s.json", new Object[]{this.videoId}));
            String str = null;
            if (isCancelled()) {
                return null;
            }
            try {
                JSONObject json = new JSONObject(playerCode).getJSONObject("file_versions").getJSONObject("mobile");
                String video = decodeUrl(json.getString("gifv"));
                String audio = json.getJSONArray(MimeTypes.BASE_TYPE_AUDIO).getString(0);
                if (!(video == null || audio == null)) {
                    this.results[0] = video;
                    this.results[1] = "other";
                    this.results[2] = audio;
                    this.results[3] = "other";
                }
            } catch (Throwable e) {
                FileLog.e(e);
            }
            if (!isCancelled()) {
                str = this.results[0];
            }
            return str;
        }

        protected void onPostExecute(String result) {
            if (result != null) {
                WebPlayerView.this.initied = true;
                WebPlayerView.this.playVideoUrl = result;
                WebPlayerView.this.playVideoType = this.results[1];
                WebPlayerView.this.playAudioUrl = this.results[2];
                WebPlayerView.this.playAudioType = this.results[3];
                if (WebPlayerView.this.isAutoplay) {
                    WebPlayerView.this.preparePlayer();
                }
                WebPlayerView.this.showProgress(false, true);
                WebPlayerView.this.controlsView.show(true, true);
            } else if (!isCancelled()) {
                WebPlayerView.this.onInitFailed();
            }
        }
    }

    private class JSExtractor {
        private String[] assign_operators = new String[]{"|=", "^=", "&=", ">>=", "<<=", "-=", "+=", "%=", "/=", "*=", "="};
        ArrayList<String> codeLines = new ArrayList();
        private String jsCode;
        private String[] operators = new String[]{"|", "^", "&", ">>", "<<", "-", "+", "%", "/", "*"};

        public JSExtractor(String js) {
            this.jsCode = js;
        }

        private void interpretExpression(String expr, HashMap<String, String> localVars, int allowRecursion) throws Exception {
            String func;
            expr = expr.trim();
            if (!TextUtils.isEmpty(expr)) {
                int parens_count;
                String remaining_expr;
                String index;
                int a = 0;
                if (expr.charAt(0) == '(') {
                    parens_count = 0;
                    Matcher matcher = WebPlayerView.exprParensPattern.matcher(expr);
                    while (matcher.find()) {
                        if (matcher.group(0).indexOf(48) == 40) {
                            parens_count++;
                        } else {
                            parens_count--;
                            if (parens_count == 0) {
                                interpretExpression(expr.substring(1, matcher.start()), localVars, allowRecursion);
                                remaining_expr = expr.substring(matcher.end()).trim();
                                if (!TextUtils.isEmpty(remaining_expr)) {
                                    expr = remaining_expr;
                                    if (parens_count != 0) {
                                        throw new Exception(String.format("Premature end of parens in %s", new Object[]{expr}));
                                    }
                                }
                                return;
                            }
                        }
                    }
                    if (parens_count != 0) {
                        throw new Exception(String.format("Premature end of parens in %s", new Object[]{expr}));
                    }
                }
                for (String func2 : this.assign_operators) {
                    Matcher matcher2 = Pattern.compile(String.format(Locale.US, "(?x)(%s)(?:\\[([^\\]]+?)\\])?\\s*%s(.*)$", new Object[]{WebPlayerView.exprName, Pattern.quote(func2)})).matcher(expr);
                    if (matcher2.find()) {
                        interpretExpression(matcher2.group(3), localVars, allowRecursion - 1);
                        index = matcher2.group(2);
                        if (TextUtils.isEmpty(index)) {
                            localVars.put(matcher2.group(1), TtmlNode.ANONYMOUS_REGION_ID);
                        } else {
                            interpretExpression(index, localVars, allowRecursion);
                        }
                        return;
                    }
                }
                try {
                    Integer.parseInt(expr);
                } catch (Exception e) {
                    if (!Pattern.compile(String.format(Locale.US, "(?!if|return|true|false)(%s)$", new Object[]{WebPlayerView.exprName})).matcher(expr).find()) {
                        if (expr.charAt(0) != '\"' || expr.charAt(expr.length() - 1) != '\"') {
                            try {
                                new JSONObject(expr).toString();
                            } catch (Exception e2) {
                                Matcher matcher3 = Pattern.compile(String.format(Locale.US, "(%s)\\[(.+)\\]$", new Object[]{WebPlayerView.exprName})).matcher(expr);
                                if (matcher3.find()) {
                                    index = matcher3.group(1);
                                    interpretExpression(matcher3.group(2), localVars, allowRecursion - 1);
                                    return;
                                }
                                matcher3 = Pattern.compile(String.format(Locale.US, "(%s)(?:\\.([^(]+)|\\[([^]]+)\\])\\s*(?:\\(+([^()]*)\\))?$", new Object[]{WebPlayerView.exprName})).matcher(expr);
                                if (matcher3.find()) {
                                    func2 = matcher3.group(1);
                                    String m1 = matcher3.group(2);
                                    remaining_expr = (TextUtils.isEmpty(m1) ? matcher3.group(3) : m1).replace("\"", TtmlNode.ANONYMOUS_REGION_ID);
                                    String arg_str = matcher3.group(4);
                                    if (localVars.get(func2) == null) {
                                        extractObject(func2);
                                    }
                                    if (arg_str != null) {
                                        if (expr.charAt(expr.length() - 1) != ')') {
                                            throw new Exception("last char not ')'");
                                        }
                                        if (arg_str.length() != 0) {
                                            String[] args = arg_str.split(",");
                                            while (a < args.length) {
                                                interpretExpression(args[a], localVars, allowRecursion);
                                                a++;
                                            }
                                        }
                                        return;
                                    }
                                    return;
                                }
                                matcher3 = Pattern.compile(String.format(Locale.US, "(%s)\\[(.+)\\]$", new Object[]{WebPlayerView.exprName})).matcher(expr);
                                if (matcher3.find()) {
                                    Object val = localVars.get(matcher3.group(1));
                                    interpretExpression(matcher3.group(2), localVars, allowRecursion - 1);
                                    return;
                                }
                                for (String func3 : this.operators) {
                                    Matcher matcher4 = Pattern.compile(String.format(Locale.US, "(.+?)%s(.+)", new Object[]{Pattern.quote(func3)})).matcher(expr);
                                    if (matcher4.find()) {
                                        boolean[] abort = new boolean[1];
                                        interpretStatement(matcher4.group(1), localVars, abort, allowRecursion - 1);
                                        if (abort[0]) {
                                            throw new Exception(String.format("Premature left-side return of %s in %s", new Object[]{func3, expr}));
                                        }
                                        interpretStatement(matcher4.group(2), localVars, abort, allowRecursion - 1);
                                        if (abort[0]) {
                                            throw new Exception(String.format("Premature right-side return of %s in %s", new Object[]{func3, expr}));
                                        }
                                    }
                                }
                                matcher3 = Pattern.compile(String.format(Locale.US, "^(%s)\\(([a-zA-Z0-9_$,]*)\\)$", new Object[]{WebPlayerView.exprName})).matcher(expr);
                                if (matcher3.find()) {
                                    extractFunction(matcher3.group(1));
                                }
                                throw new Exception(String.format("Unsupported JS expression %s", new Object[]{expr}));
                            }
                        }
                    }
                }
            }
        }

        private void interpretStatement(String stmt, HashMap<String, String> localVars, boolean[] abort, int allowRecursion) throws Exception {
            if (allowRecursion < 0) {
                throw new Exception("recursion limit reached");
            }
            String expr;
            abort[0] = false;
            stmt = stmt.trim();
            Matcher matcher = WebPlayerView.stmtVarPattern.matcher(stmt);
            if (matcher.find()) {
                expr = stmt.substring(matcher.group(0).length());
            } else {
                matcher = WebPlayerView.stmtReturnPattern.matcher(stmt);
                if (matcher.find()) {
                    String expr2 = stmt.substring(matcher.group(0).length());
                    abort[0] = true;
                    expr = expr2;
                } else {
                    expr = stmt;
                }
            }
            interpretExpression(expr, localVars, allowRecursion);
        }

        private HashMap<String, Object> extractObject(String objname) throws Exception {
            String funcName = "(?:[a-zA-Z$0-9]+|\"[a-zA-Z$0-9]+\"|'[a-zA-Z$0-9]+')";
            HashMap<String, Object> obj = new HashMap();
            Matcher matcher = Pattern.compile(String.format(Locale.US, "(?:var\\s+)?%s\\s*=\\s*\\{\\s*((%s\\s*:\\s*function\\(.*?\\)\\s*\\{.*?\\}(?:,\\s*)?)*)\\}\\s*;", new Object[]{Pattern.quote(objname), funcName})).matcher(this.jsCode);
            String fields = null;
            while (matcher.find()) {
                String code = matcher.group();
                fields = matcher.group(2);
                if (!TextUtils.isEmpty(fields)) {
                    if (!this.codeLines.contains(code)) {
                        this.codeLines.add(matcher.group());
                    }
                    matcher = Pattern.compile(String.format("(%s)\\s*:\\s*function\\(([a-z,]+)\\)\\{([^}]+)\\}", new Object[]{funcName})).matcher(fields);
                    while (matcher.find()) {
                        buildFunction(matcher.group(2).split(","), matcher.group(3));
                    }
                    return obj;
                }
            }
            matcher = Pattern.compile(String.format("(%s)\\s*:\\s*function\\(([a-z,]+)\\)\\{([^}]+)\\}", new Object[]{funcName})).matcher(fields);
            while (matcher.find()) {
                buildFunction(matcher.group(2).split(","), matcher.group(3));
            }
            return obj;
        }

        private void buildFunction(String[] argNames, String funcCode) throws Exception {
            HashMap<String, String> localVars = new HashMap();
            for (Object put : argNames) {
                localVars.put(put, TtmlNode.ANONYMOUS_REGION_ID);
            }
            String[] stmts = funcCode.split(";");
            boolean[] abort = new boolean[1];
            int a = 0;
            while (a < stmts.length) {
                interpretStatement(stmts[a], localVars, abort, 100);
                if (!abort[0]) {
                    a++;
                } else {
                    return;
                }
            }
        }

        private String extractFunction(String funcName) throws Exception {
            try {
                String quote = Pattern.quote(funcName);
                Matcher matcher = Pattern.compile(String.format(Locale.US, "(?x)(?:function\\s+%s|[{;,]\\s*%s\\s*=\\s*function|var\\s+%s\\s*=\\s*function)\\s*\\(([^)]*)\\)\\s*\\{([^}]+)\\}", new Object[]{quote, quote, quote})).matcher(this.jsCode);
                if (matcher.find()) {
                    String group = matcher.group();
                    if (!this.codeLines.contains(group)) {
                        ArrayList arrayList = this.codeLines;
                        StringBuilder stringBuilder = new StringBuilder();
                        stringBuilder.append(group);
                        stringBuilder.append(";");
                        arrayList.add(stringBuilder.toString());
                    }
                    buildFunction(matcher.group(1).split(","), matcher.group(2));
                }
            } catch (Throwable e) {
                this.codeLines.clear();
                FileLog.e(e);
            }
            return TextUtils.join(TtmlNode.ANONYMOUS_REGION_ID, this.codeLines);
        }
    }

    public class JavaScriptInterface {
        private final CallJavaResultInterface callJavaResultInterface;

        public JavaScriptInterface(CallJavaResultInterface callJavaResult) {
            this.callJavaResultInterface = callJavaResult;
        }

        @JavascriptInterface
        public void returnResultToJava(String value) {
            this.callJavaResultInterface.jsCallFinished(value);
        }
    }

    private class TwitchClipVideoTask extends AsyncTask<Void, Void, String> {
        private boolean canRetry = true;
        private String currentUrl;
        private String[] results = new String[2];
        private String videoId;

        public TwitchClipVideoTask(String url, String vid) {
            this.videoId = vid;
            this.currentUrl = url;
        }

        protected String doInBackground(Void... voids) {
            String str = null;
            String playerCode = WebPlayerView.this.downloadUrlContent(this, this.currentUrl, null, false);
            if (isCancelled()) {
                return null;
            }
            try {
                Matcher filelist = WebPlayerView.twitchClipFilePattern.matcher(playerCode);
                if (filelist.find()) {
                    this.results[0] = new JSONObject(filelist.group(1)).getJSONArray("quality_options").getJSONObject(0).getString("source");
                    this.results[1] = "other";
                }
            } catch (Throwable e) {
                FileLog.e(e);
            }
            if (!isCancelled()) {
                str = this.results[0];
            }
            return str;
        }

        protected void onPostExecute(String result) {
            if (result != null) {
                WebPlayerView.this.initied = true;
                WebPlayerView.this.playVideoUrl = result;
                WebPlayerView.this.playVideoType = this.results[1];
                if (WebPlayerView.this.isAutoplay) {
                    WebPlayerView.this.preparePlayer();
                }
                WebPlayerView.this.showProgress(false, true);
                WebPlayerView.this.controlsView.show(true, true);
            } else if (!isCancelled()) {
                WebPlayerView.this.onInitFailed();
            }
        }
    }

    private class TwitchStreamVideoTask extends AsyncTask<Void, Void, String> {
        private boolean canRetry = true;
        private String currentUrl;
        private String[] results = new String[2];
        private String videoId;

        public TwitchStreamVideoTask(String url, String vid) {
            this.videoId = vid;
            this.currentUrl = url;
        }

        protected String doInBackground(Void... voids) {
            HashMap<String, String> headers = new HashMap();
            headers.put("Client-ID", "jzkbprff40iqj646a697cyrvl0zt2m6");
            int indexOf = this.videoId.indexOf(38);
            int idx = indexOf;
            if (indexOf > 0) {
                r1.videoId = r1.videoId.substring(0, idx);
            }
            String streamCode = WebPlayerView.this.downloadUrlContent(r1, String.format(Locale.US, "https://api.twitch.tv/kraken/streams/%s?stream_type=all", new Object[]{r1.videoId}), headers, false);
            if (isCancelled()) {
                return null;
            }
            try {
                JSONObject stream = new JSONObject(streamCode).getJSONObject("stream");
                JSONObject accessToken = new JSONObject(WebPlayerView.this.downloadUrlContent(r1, String.format(Locale.US, "https://api.twitch.tv/api/channels/%s/access_token", new Object[]{r1.videoId}), headers, false));
                String sig = URLEncoder.encode(accessToken.getString("sig"), C.UTF8_NAME);
                String token = URLEncoder.encode(accessToken.getString("token"), C.UTF8_NAME);
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("https://youtube.googleapis.com/v/");
                stringBuilder.append(r1.videoId);
                URLEncoder.encode(stringBuilder.toString(), C.UTF8_NAME);
                stringBuilder = new StringBuilder();
                stringBuilder.append("allow_source=true&allow_audio_only=true&allow_spectre=true&player=twitchweb&segment_preference=4&p=");
                stringBuilder.append((int) (Math.random() * 1.0E7d));
                stringBuilder.append("&sig=");
                stringBuilder.append(sig);
                stringBuilder.append("&token=");
                stringBuilder.append(token);
                String params = stringBuilder.toString();
                r1.results[0] = String.format(Locale.US, "https://usher.ttvnw.net/api/channel/hls/%s.m3u8?%s", new Object[]{r1.videoId, params});
                r1.results[1] = "hls";
            } catch (Throwable e) {
                FileLog.e(e);
            }
            return isCancelled() ? null : r1.results[0];
        }

        protected void onPostExecute(String result) {
            if (result != null) {
                WebPlayerView.this.initied = true;
                WebPlayerView.this.playVideoUrl = result;
                WebPlayerView.this.playVideoType = this.results[1];
                if (WebPlayerView.this.isAutoplay) {
                    WebPlayerView.this.preparePlayer();
                }
                WebPlayerView.this.showProgress(false, true);
                WebPlayerView.this.controlsView.show(true, true);
            } else if (!isCancelled()) {
                WebPlayerView.this.onInitFailed();
            }
        }
    }

    private class VimeoVideoTask extends AsyncTask<Void, Void, String> {
        private boolean canRetry = true;
        private String[] results = new String[2];
        private String videoId;

        public VimeoVideoTask(String vid) {
            this.videoId = vid;
        }

        protected String doInBackground(Void... voids) {
            String playerCode = WebPlayerView.this.downloadUrlContent(this, String.format(Locale.US, "https://player.vimeo.com/video/%s/config", new Object[]{this.videoId}));
            String str = null;
            if (isCancelled()) {
                return null;
            }
            try {
                JSONObject files = new JSONObject(playerCode).getJSONObject("request").getJSONObject("files");
                if (files.has("hls")) {
                    JSONObject hls = files.getJSONObject("hls");
                    try {
                        this.results[0] = hls.getString(UpdateFragment.FRAGMENT_URL);
                    } catch (Exception e) {
                        this.results[0] = hls.getJSONObject("cdns").getJSONObject(hls.getString("default_cdn")).getString(UpdateFragment.FRAGMENT_URL);
                    }
                    this.results[1] = "hls";
                } else if (files.has("progressive")) {
                    this.results[1] = "other";
                    this.results[0] = files.getJSONArray("progressive").getJSONObject(0).getString(UpdateFragment.FRAGMENT_URL);
                }
            } catch (Throwable e2) {
                FileLog.e(e2);
            }
            if (!isCancelled()) {
                str = this.results[0];
            }
            return str;
        }

        protected void onPostExecute(String result) {
            if (result != null) {
                WebPlayerView.this.initied = true;
                WebPlayerView.this.playVideoUrl = result;
                WebPlayerView.this.playVideoType = this.results[1];
                if (WebPlayerView.this.isAutoplay) {
                    WebPlayerView.this.preparePlayer();
                }
                WebPlayerView.this.showProgress(false, true);
                WebPlayerView.this.controlsView.show(true, true);
            } else if (!isCancelled()) {
                WebPlayerView.this.onInitFailed();
            }
        }
    }

    public interface WebPlayerViewDelegate {
        boolean checkInlinePermissions();

        ViewGroup getTextureViewContainer();

        void onInitFailed();

        void onInlineSurfaceTextureReady();

        void onPlayStateChanged(WebPlayerView webPlayerView, boolean z);

        void onSharePressed();

        TextureView onSwitchInlineMode(View view, boolean z, float f, int i, boolean z2);

        TextureView onSwitchToFullscreen(View view, boolean z, float f, int i, boolean z2);

        void onVideoSizeChanged(float f, int i);

        void prepareToSwitchInlineMode(boolean z, Runnable runnable, float f, boolean z2);
    }

    private class YoutubeVideoTask extends AsyncTask<Void, Void, String[]> {
        private boolean canRetry = true;
        private CountDownLatch countDownLatch = new CountDownLatch(1);
        private String[] result = new String[2];
        private String sig;
        private String videoId;

        class AnonymousClass1 implements Runnable {
            final /* synthetic */ String val$functionCodeFinal;

            AnonymousClass1(String str) {
                this.val$functionCodeFinal = str;
            }

            public void run() {
                if (VERSION.SDK_INT >= 21) {
                    WebPlayerView.this.webView.evaluateJavascript(this.val$functionCodeFinal, new ValueCallback<String>() {
                        public void onReceiveValue(String value) {
                            String[] access$1300 = YoutubeVideoTask.this.result;
                            String str = YoutubeVideoTask.this.result[0];
                            CharSequence access$1400 = YoutubeVideoTask.this.sig;
                            StringBuilder stringBuilder = new StringBuilder();
                            stringBuilder.append("/signature/");
                            stringBuilder.append(value.substring(1, value.length() - 1));
                            access$1300[0] = str.replace(access$1400, stringBuilder.toString());
                            YoutubeVideoTask.this.countDownLatch.countDown();
                        }
                    });
                    return;
                }
                try {
                    String javascript = new StringBuilder();
                    javascript.append("<script>");
                    javascript.append(this.val$functionCodeFinal);
                    javascript.append("</script>");
                    String base64 = Base64.encodeToString(javascript.toString().getBytes(C.UTF8_NAME), null);
                    WebView access$1600 = WebPlayerView.this.webView;
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("data:text/html;charset=utf-8;base64,");
                    stringBuilder.append(base64);
                    access$1600.loadUrl(stringBuilder.toString());
                } catch (Throwable e) {
                    FileLog.e(e);
                }
            }
        }

        protected java.lang.String[] doInBackground(java.lang.Void... r1) {
            /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.DecodeException: Load method exception in method: org.telegram.ui.Components.WebPlayerView.YoutubeVideoTask.doInBackground(java.lang.Void[]):java.lang.String[]
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
            r1 = r26;
            r2 = org.telegram.ui.Components.WebPlayerView.this;
            r3 = new java.lang.StringBuilder;
            r3.<init>();
            r4 = "https://www.youtube.com/embed/";
            r3.append(r4);
            r4 = r1.videoId;
            r3.append(r4);
            r3 = r3.toString();
            r2 = r2.downloadUrlContent(r1, r3);
            r3 = r26.isCancelled();
            r4 = 0;
            if (r3 == 0) goto L_0x0023;
        L_0x0022:
            return r4;
        L_0x0023:
            r3 = new java.lang.StringBuilder;
            r3.<init>();
            r5 = "video_id=";
            r3.append(r5);
            r5 = r1.videoId;
            r3.append(r5);
            r5 = "&ps=default&gl=US&hl=en";
            r3.append(r5);
            r3 = r3.toString();
            r5 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x006a }
            r5.<init>();	 Catch:{ Exception -> 0x006a }
            r5.append(r3);	 Catch:{ Exception -> 0x006a }
            r6 = "&eurl=";	 Catch:{ Exception -> 0x006a }
            r5.append(r6);	 Catch:{ Exception -> 0x006a }
            r6 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x006a }
            r6.<init>();	 Catch:{ Exception -> 0x006a }
            r7 = "https://youtube.googleapis.com/v/";	 Catch:{ Exception -> 0x006a }
            r6.append(r7);	 Catch:{ Exception -> 0x006a }
            r7 = r1.videoId;	 Catch:{ Exception -> 0x006a }
            r6.append(r7);	 Catch:{ Exception -> 0x006a }
            r6 = r6.toString();	 Catch:{ Exception -> 0x006a }
            r7 = "UTF-8";	 Catch:{ Exception -> 0x006a }
            r6 = java.net.URLEncoder.encode(r6, r7);	 Catch:{ Exception -> 0x006a }
            r5.append(r6);	 Catch:{ Exception -> 0x006a }
            r5 = r5.toString();	 Catch:{ Exception -> 0x006a }
            r3 = r5;
            goto L_0x006f;
        L_0x006a:
            r0 = move-exception;
            r5 = r0;
            org.telegram.messenger.FileLog.e(r5);
        L_0x006f:
            if (r2 == 0) goto L_0x00b3;
        L_0x0071:
            r5 = org.telegram.ui.Components.WebPlayerView.stsPattern;
            r5 = r5.matcher(r2);
            r6 = r5.find();
            if (r6 == 0) goto L_0x00a2;
        L_0x007f:
            r6 = new java.lang.StringBuilder;
            r6.<init>();
            r6.append(r3);
            r7 = "&sts=";
            r6.append(r7);
            r7 = r5.start();
            r7 = r7 + 6;
            r8 = r5.end();
            r7 = r2.substring(r7, r8);
            r6.append(r7);
            r3 = r6.toString();
            goto L_0x00b3;
        L_0x00a2:
            r6 = new java.lang.StringBuilder;
            r6.<init>();
            r6.append(r3);
            r7 = "&sts=";
            r6.append(r7);
            r3 = r6.toString();
        L_0x00b3:
            r5 = r1.result;
            r6 = "dash";
            r7 = 1;
            r5[r7] = r6;
            r5 = 0;
            r6 = 0;
            r8 = 5;
            r8 = new java.lang.String[r8];
            r9 = "";
            r10 = 0;
            r8[r10] = r9;
            r9 = "&el=leanback";
            r8[r7] = r9;
            r9 = "&el=embedded";
            r11 = 2;
            r8[r11] = r9;
            r9 = "&el=detailpage";
            r12 = 3;
            r8[r12] = r9;
            r9 = 4;
            r13 = "&el=vevo";
            r8[r9] = r13;
            r9 = r5;
            r5 = r10;
            r13 = r8.length;
            if (r5 >= r13) goto L_0x02a0;
        L_0x00dc:
            r13 = org.telegram.ui.Components.WebPlayerView.this;
            r14 = new java.lang.StringBuilder;
            r14.<init>();
            r15 = "https://www.youtube.com/get_video_info?";
            r14.append(r15);
            r14.append(r3);
            r15 = r8[r5];
            r14.append(r15);
            r14 = r14.toString();
            r13 = r13.downloadUrlContent(r1, r14);
            r14 = r26.isCancelled();
            if (r14 == 0) goto L_0x00ff;
        L_0x00fe:
            return r4;
        L_0x00ff:
            r14 = 0;
            r15 = 0;
            r16 = 0;
            if (r13 == 0) goto L_0x0272;
        L_0x0105:
            r12 = "&";
            r12 = r13.split(r12);
            r17 = r6;
            r6 = r10;
            r4 = r12.length;
            if (r6 >= r4) goto L_0x026d;
        L_0x0111:
            r4 = r12[r6];
            r10 = "dashmpd";
            r4 = r4.startsWith(r10);
            if (r4 == 0) goto L_0x013f;
        L_0x011b:
            r14 = 1;
            r4 = r12[r6];
            r10 = "=";
            r4 = r4.split(r10);
            r10 = r4.length;
            if (r10 != r11) goto L_0x013a;
        L_0x0127:
            r10 = r1.result;	 Catch:{ Exception -> 0x0135 }
            r11 = r4[r7];	 Catch:{ Exception -> 0x0135 }
            r7 = "UTF-8";	 Catch:{ Exception -> 0x0135 }
            r7 = java.net.URLDecoder.decode(r11, r7);	 Catch:{ Exception -> 0x0135 }
            r11 = 0;	 Catch:{ Exception -> 0x0135 }
            r10[r11] = r7;	 Catch:{ Exception -> 0x0135 }
            goto L_0x013a;
        L_0x0135:
            r0 = move-exception;
            r7 = r0;
            org.telegram.messenger.FileLog.e(r7);
            r19 = r3;
            goto L_0x0263;
        L_0x013f:
            r4 = r12[r6];
            r7 = "url_encoded_fmt_stream_map";
            r4 = r4.startsWith(r7);
            if (r4 == 0) goto L_0x01eb;
            r4 = r12[r6];
            r7 = "=";
            r4 = r4.split(r7);
            r7 = r4.length;
            r10 = 2;
            if (r7 != r10) goto L_0x01e7;
            r7 = 1;
            r10 = r4[r7];	 Catch:{ Exception -> 0x01dd }
            r7 = "UTF-8";	 Catch:{ Exception -> 0x01dd }
            r7 = java.net.URLDecoder.decode(r10, r7);	 Catch:{ Exception -> 0x01dd }
            r10 = "[&,]";	 Catch:{ Exception -> 0x01dd }
            r7 = r7.split(r10);	 Catch:{ Exception -> 0x01dd }
            r10 = 0;
            r11 = 0;
            r18 = r11;
            r11 = r10;
            r10 = 0;
            r19 = r3;
            r3 = r7.length;	 Catch:{ Exception -> 0x01d8 }
            if (r10 >= r3) goto L_0x01d5;	 Catch:{ Exception -> 0x01d8 }
            r3 = r7[r10];	 Catch:{ Exception -> 0x01d8 }
            r20 = r4;
            r4 = "=";	 Catch:{ Exception -> 0x01d2 }
            r3 = r3.split(r4);	 Catch:{ Exception -> 0x01d2 }
            r21 = r7;	 Catch:{ Exception -> 0x01d2 }
            r4 = 0;	 Catch:{ Exception -> 0x01d2 }
            r7 = r3[r4];	 Catch:{ Exception -> 0x01d2 }
            r4 = "type";	 Catch:{ Exception -> 0x01d2 }
            r4 = r7.startsWith(r4);	 Catch:{ Exception -> 0x01d2 }
            if (r4 == 0) goto L_0x019a;	 Catch:{ Exception -> 0x01d2 }
            r4 = 1;	 Catch:{ Exception -> 0x01d2 }
            r7 = r3[r4];	 Catch:{ Exception -> 0x01d2 }
            r4 = "UTF-8";	 Catch:{ Exception -> 0x01d2 }
            r4 = java.net.URLDecoder.decode(r7, r4);	 Catch:{ Exception -> 0x01d2 }
            r7 = "video/mp4";	 Catch:{ Exception -> 0x01d2 }
            r7 = r4.contains(r7);	 Catch:{ Exception -> 0x01d2 }
            if (r7 == 0) goto L_0x0199;	 Catch:{ Exception -> 0x01d2 }
            r18 = 1;	 Catch:{ Exception -> 0x01d2 }
            goto L_0x01c0;	 Catch:{ Exception -> 0x01d2 }
            r4 = 0;	 Catch:{ Exception -> 0x01d2 }
            r7 = r3[r4];	 Catch:{ Exception -> 0x01d2 }
            r4 = "url";	 Catch:{ Exception -> 0x01d2 }
            r4 = r7.startsWith(r4);	 Catch:{ Exception -> 0x01d2 }
            if (r4 == 0) goto L_0x01b0;	 Catch:{ Exception -> 0x01d2 }
            r4 = 1;	 Catch:{ Exception -> 0x01d2 }
            r7 = r3[r4];	 Catch:{ Exception -> 0x01d2 }
            r4 = "UTF-8";	 Catch:{ Exception -> 0x01d2 }
            r4 = java.net.URLDecoder.decode(r7, r4);	 Catch:{ Exception -> 0x01d2 }
            r11 = r4;	 Catch:{ Exception -> 0x01d2 }
            goto L_0x01c0;	 Catch:{ Exception -> 0x01d2 }
            r4 = 0;	 Catch:{ Exception -> 0x01d2 }
            r7 = r3[r4];	 Catch:{ Exception -> 0x01d2 }
            r4 = "itag";	 Catch:{ Exception -> 0x01d2 }
            r4 = r7.startsWith(r4);	 Catch:{ Exception -> 0x01d2 }
            if (r4 == 0) goto L_0x01c0;
            r4 = 0;
            r7 = 0;
            r11 = r4;
            r18 = r7;
            if (r18 == 0) goto L_0x01c9;
            if (r11 == 0) goto L_0x01c9;
            r4 = r11;
            r17 = r4;
            goto L_0x01d7;
            r10 = r10 + 1;
            r3 = r19;
            r4 = r20;
            r7 = r21;
            goto L_0x016a;
        L_0x01d2:
            r0 = move-exception;
            r3 = r0;
            goto L_0x01e3;
            r20 = r4;
            goto L_0x01e9;
        L_0x01d8:
            r0 = move-exception;
            r20 = r4;
            r3 = r0;
            goto L_0x01e3;
        L_0x01dd:
            r0 = move-exception;
            r19 = r3;
            r20 = r4;
            r3 = r0;
            org.telegram.messenger.FileLog.e(r3);
            goto L_0x01e9;
            r19 = r3;
            goto L_0x0263;
            r19 = r3;
            r3 = r12[r6];
            r4 = "use_cipher_signature";
            r3 = r3.startsWith(r4);
            if (r3 == 0) goto L_0x0214;
            r3 = r12[r6];
            r4 = "=";
            r3 = r3.split(r4);
            r4 = r3.length;
            r7 = 2;
            if (r4 != r7) goto L_0x0213;
            r4 = 1;
            r7 = r3[r4];
            r4 = r7.toLowerCase();
            r7 = "true";
            r4 = r4.equals(r7);
            if (r4 == 0) goto L_0x0213;
            r9 = 1;
            goto L_0x0263;
            r3 = r12[r6];
            r4 = "hlsvp";
            r3 = r3.startsWith(r4);
            if (r3 == 0) goto L_0x023c;
            r3 = r12[r6];
            r4 = "=";
            r3 = r3.split(r4);
            r4 = r3.length;
            r7 = 2;
            if (r4 != r7) goto L_0x023b;
            r4 = 1;
            r7 = r3[r4];	 Catch:{ Exception -> 0x0236 }
            r4 = "UTF-8";	 Catch:{ Exception -> 0x0236 }
            r4 = java.net.URLDecoder.decode(r7, r4);	 Catch:{ Exception -> 0x0236 }
            r15 = r4;
            goto L_0x023b;
        L_0x0236:
            r0 = move-exception;
            r4 = r0;
            org.telegram.messenger.FileLog.e(r4);
            goto L_0x0263;
            r3 = r12[r6];
            r4 = "livestream";
            r3 = r3.startsWith(r4);
            if (r3 == 0) goto L_0x0263;
            r3 = r12[r6];
            r4 = "=";
            r3 = r3.split(r4);
            r4 = r3.length;
            r7 = 2;
            if (r4 != r7) goto L_0x0263;
            r4 = 1;
            r7 = r3[r4];
            r4 = r7.toLowerCase();
            r7 = "1";
            r4 = r4.equals(r7);
            if (r4 == 0) goto L_0x0263;
            r16 = 1;
            r6 = r6 + 1;
            r3 = r19;
            r4 = 0;
            r7 = 1;
            r10 = 0;
            r11 = 2;
            goto L_0x010e;
        L_0x026d:
            r19 = r3;
            r6 = r17;
            goto L_0x0274;
        L_0x0272:
            r19 = r3;
            if (r16 == 0) goto L_0x0292;
            if (r15 == 0) goto L_0x0290;
            if (r9 != 0) goto L_0x0290;
            r3 = "/s/";
            r3 = r15.contains(r3);
            if (r3 == 0) goto L_0x0283;
            goto L_0x0290;
            r3 = r1.result;
            r4 = 0;
            r3[r4] = r15;
            r3 = r1.result;
            r4 = "hls";
            r7 = 1;
            r3[r7] = r4;
            goto L_0x0292;
            r3 = 0;
            return r3;
            if (r14 == 0) goto L_0x0295;
            goto L_0x02a2;
            r5 = r5 + 1;
            r3 = r19;
            r4 = 0;
            r7 = 1;
            r10 = 0;
            r11 = 2;
            r12 = 3;
            goto L_0x00d9;
        L_0x02a0:
            r19 = r3;
            r3 = r1.result;
            r4 = 0;
            r3 = r3[r4];
            if (r3 != 0) goto L_0x02b6;
            if (r6 == 0) goto L_0x02b6;
            r3 = r1.result;
            r3[r4] = r6;
            r3 = r1.result;
            r5 = "other";
            r7 = 1;
            r3[r7] = r5;
            r3 = r1.result;
            r3 = r3[r4];
            if (r3 == 0) goto L_0x04c3;
            if (r9 != 0) goto L_0x02cf;
            r3 = r1.result;
            r3 = r3[r4];
            r4 = "/s/";
            r3 = r3.contains(r4);
            if (r3 == 0) goto L_0x02cb;
            goto L_0x02cf;
            r23 = r2;
            goto L_0x04c5;
            if (r2 == 0) goto L_0x04c3;
            r9 = 1;
            r3 = r1.result;
            r4 = 0;
            r3 = r3[r4];
            r5 = "/s/";
            r3 = r3.indexOf(r5);
            r5 = r1.result;
            r5 = r5[r4];
            r4 = 47;
            r7 = r3 + 10;
            r4 = r5.indexOf(r4, r7);
            r5 = -1;
            if (r3 == r5) goto L_0x04c3;
            if (r4 != r5) goto L_0x02f8;
            r5 = r1.result;
            r7 = 0;
            r5 = r5[r7];
            r4 = r5.length();
            goto L_0x02f9;
            r7 = 0;
            r5 = r1.result;
            r5 = r5[r7];
            r5 = r5.substring(r3, r4);
            r1.sig = r5;
            r5 = 0;
            r7 = org.telegram.ui.Components.WebPlayerView.jsPattern;
            r7 = r7.matcher(r2);
            r10 = r7.find();
            if (r10 == 0) goto L_0x032e;
            r10 = new org.json.JSONTokener;	 Catch:{ Exception -> 0x0329 }
            r11 = 1;	 Catch:{ Exception -> 0x0329 }
            r12 = r7.group(r11);	 Catch:{ Exception -> 0x0329 }
            r10.<init>(r12);	 Catch:{ Exception -> 0x0329 }
            r11 = r10.nextValue();	 Catch:{ Exception -> 0x0329 }
            r12 = r11 instanceof java.lang.String;	 Catch:{ Exception -> 0x0329 }
            if (r12 == 0) goto L_0x0328;	 Catch:{ Exception -> 0x0329 }
            r12 = r11;	 Catch:{ Exception -> 0x0329 }
            r12 = (java.lang.String) r12;	 Catch:{ Exception -> 0x0329 }
            r5 = r12;
            goto L_0x032e;
        L_0x0329:
            r0 = move-exception;
            r10 = r0;
            org.telegram.messenger.FileLog.e(r10);
            if (r5 == 0) goto L_0x04c3;
            r10 = org.telegram.ui.Components.WebPlayerView.playerIdPattern;
            r7 = r10.matcher(r5);
            r10 = r7.find();
            if (r10 == 0) goto L_0x0358;
            r10 = new java.lang.StringBuilder;
            r10.<init>();
            r11 = 1;
            r12 = r7.group(r11);
            r10.append(r12);
            r11 = 2;
            r11 = r7.group(r11);
            r10.append(r11);
            r10 = r10.toString();
            goto L_0x0359;
            r10 = 0;
            r11 = 0;
            r12 = 0;
            r13 = org.telegram.messenger.ApplicationLoader.applicationContext;
            r14 = "youtubecode";
            r15 = 0;
            r13 = r13.getSharedPreferences(r14, r15);
            if (r10 == 0) goto L_0x0381;
            r14 = 0;
            r11 = r13.getString(r10, r14);
            r15 = new java.lang.StringBuilder;
            r15.<init>();
            r15.append(r10);
            r14 = "n";
            r15.append(r14);
            r14 = r15.toString();
            r15 = 0;
            r12 = r13.getString(r14, r15);
            if (r11 != 0) goto L_0x0442;
            r14 = "//";
            r14 = r5.startsWith(r14);
            if (r14 == 0) goto L_0x039d;
            r14 = new java.lang.StringBuilder;
            r14.<init>();
            r15 = "https:";
            r14.append(r15);
            r14.append(r5);
            r5 = r14.toString();
            goto L_0x03b6;
            r14 = "/";
            r14 = r5.startsWith(r14);
            if (r14 == 0) goto L_0x03b6;
            r14 = new java.lang.StringBuilder;
            r14.<init>();
            r15 = "https://www.youtube.com";
            r14.append(r15);
            r14.append(r5);
            r5 = r14.toString();
            r14 = org.telegram.ui.Components.WebPlayerView.this;
            r14 = r14.downloadUrlContent(r1, r5);
            r15 = r26.isCancelled();
            if (r15 == 0) goto L_0x03c4;
            r15 = 0;
            return r15;
            r15 = 0;
            if (r14 == 0) goto L_0x0442;
            r15 = org.telegram.ui.Components.WebPlayerView.sigPattern;
            r7 = r15.matcher(r14);
            r15 = r7.find();
            if (r15 == 0) goto L_0x03db;
            r15 = 1;
            r12 = r7.group(r15);
            goto L_0x03ef;
            r15 = 1;
            r15 = org.telegram.ui.Components.WebPlayerView.sigPattern2;
            r7 = r15.matcher(r14);
            r15 = r7.find();
            if (r15 == 0) goto L_0x03ef;
            r15 = 1;
            r12 = r7.group(r15);
            if (r12 == 0) goto L_0x0442;
            r15 = new org.telegram.ui.Components.WebPlayerView$JSExtractor;	 Catch:{ Exception -> 0x0438 }
            r23 = r2;
            r2 = org.telegram.ui.Components.WebPlayerView.this;	 Catch:{ Exception -> 0x0433 }
            r15.<init>(r14);	 Catch:{ Exception -> 0x0433 }
            r2 = r15;	 Catch:{ Exception -> 0x0433 }
            r15 = r2.extractFunction(r12);	 Catch:{ Exception -> 0x0433 }
            r11 = r15;	 Catch:{ Exception -> 0x0433 }
            r15 = android.text.TextUtils.isEmpty(r11);	 Catch:{ Exception -> 0x0433 }
            if (r15 != 0) goto L_0x0430;	 Catch:{ Exception -> 0x0433 }
            if (r10 == 0) goto L_0x0430;	 Catch:{ Exception -> 0x0433 }
            r15 = r13.edit();	 Catch:{ Exception -> 0x0433 }
            r15 = r15.putString(r10, r11);	 Catch:{ Exception -> 0x0433 }
            r24 = r2;	 Catch:{ Exception -> 0x0433 }
            r2 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x0433 }
            r2.<init>();	 Catch:{ Exception -> 0x0433 }
            r2.append(r10);	 Catch:{ Exception -> 0x0433 }
            r25 = r3;
            r3 = "n";	 Catch:{ Exception -> 0x042d }
            r2.append(r3);	 Catch:{ Exception -> 0x042d }
            r2 = r2.toString();	 Catch:{ Exception -> 0x042d }
            r2 = r15.putString(r2, r12);	 Catch:{ Exception -> 0x042d }
            r2.commit();	 Catch:{ Exception -> 0x042d }
            goto L_0x0432;
        L_0x042d:
            r0 = move-exception;
            r2 = r0;
            goto L_0x043e;
            r25 = r3;
            goto L_0x0446;
        L_0x0433:
            r0 = move-exception;
            r25 = r3;
            r2 = r0;
            goto L_0x043e;
        L_0x0438:
            r0 = move-exception;
            r23 = r2;
            r25 = r3;
            r2 = r0;
            org.telegram.messenger.FileLog.e(r2);
            goto L_0x0446;
            r23 = r2;
            r25 = r3;
            r2 = android.text.TextUtils.isEmpty(r11);
            if (r2 != 0) goto L_0x04c5;
            r2 = android.os.Build.VERSION.SDK_INT;
            r3 = 21;
            if (r2 < r3) goto L_0x0476;
            r2 = new java.lang.StringBuilder;
            r2.<init>();
            r2.append(r11);
            r2.append(r12);
            r3 = "('";
            r2.append(r3);
            r3 = r1.sig;
            r14 = 3;
            r3 = r3.substring(r14);
            r2.append(r3);
            r3 = "');";
            r2.append(r3);
            r2 = r2.toString();
            goto L_0x04ad;
            r2 = new java.lang.StringBuilder;
            r2.<init>();
            r2.append(r11);
            r3 = "window.";
            r2.append(r3);
            r3 = org.telegram.ui.Components.WebPlayerView.this;
            r3 = r3.interfaceName;
            r2.append(r3);
            r3 = ".returnResultToJava(";
            r2.append(r3);
            r2.append(r12);
            r3 = "('";
            r2.append(r3);
            r3 = r1.sig;
            r14 = 3;
            r3 = r3.substring(r14);
            r2.append(r3);
            r3 = "'));";
            r2.append(r3);
            r2 = r2.toString();
            goto L_0x0475;
            r3 = r2;
            r11 = new org.telegram.ui.Components.WebPlayerView$YoutubeVideoTask$1;	 Catch:{ Exception -> 0x04bd }
            r11.<init>(r3);	 Catch:{ Exception -> 0x04bd }
            org.telegram.messenger.AndroidUtilities.runOnUIThread(r11);	 Catch:{ Exception -> 0x04bd }
            r11 = r1.countDownLatch;	 Catch:{ Exception -> 0x04bd }
            r11.await();	 Catch:{ Exception -> 0x04bd }
            r9 = 0;
            goto L_0x04c5;
        L_0x04bd:
            r0 = move-exception;
            r11 = r0;
            org.telegram.messenger.FileLog.e(r11);
            goto L_0x04c5;
            r23 = r2;
            r2 = r26.isCancelled();
            if (r2 != 0) goto L_0x04d3;
            if (r9 == 0) goto L_0x04ce;
            goto L_0x04d3;
            r4 = r1.result;
            r22 = r4;
            goto L_0x04d5;
            r22 = 0;
            return r22;
            */
            throw new UnsupportedOperationException("Method not decompiled: org.telegram.ui.Components.WebPlayerView.YoutubeVideoTask.doInBackground(java.lang.Void[]):java.lang.String[]");
        }

        public YoutubeVideoTask(String vid) {
            this.videoId = vid;
        }

        private void onInterfaceResult(String value) {
            String[] strArr = this.result;
            String str = this.result[0];
            CharSequence charSequence = this.sig;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("/signature/");
            stringBuilder.append(value);
            strArr[0] = str.replace(charSequence, stringBuilder.toString());
            this.countDownLatch.countDown();
        }

        protected void onPostExecute(String[] result) {
            if (result[0] != null) {
                if (BuildVars.LOGS_ENABLED) {
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("start play youtube video ");
                    stringBuilder.append(result[1]);
                    stringBuilder.append(" ");
                    stringBuilder.append(result[0]);
                    FileLog.d(stringBuilder.toString());
                }
                WebPlayerView.this.initied = true;
                WebPlayerView.this.playVideoUrl = result[0];
                WebPlayerView.this.playVideoType = result[1];
                if (WebPlayerView.this.playVideoType.equals("hls")) {
                    WebPlayerView.this.isStream = true;
                }
                if (WebPlayerView.this.isAutoplay) {
                    WebPlayerView.this.preparePlayer();
                }
                WebPlayerView.this.showProgress(false, true);
                WebPlayerView.this.controlsView.show(true, true);
            } else if (!isCancelled()) {
                WebPlayerView.this.onInitFailed();
            }
        }
    }

    private abstract class function {
        public abstract Object run(Object[] objArr);

        private function() {
        }
    }

    protected java.lang.String downloadUrlContent(android.os.AsyncTask r1, java.lang.String r2, java.util.HashMap<java.lang.String, java.lang.String> r3, boolean r4) {
        /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.DecodeException: Load method exception in method: org.telegram.ui.Components.WebPlayerView.downloadUrlContent(android.os.AsyncTask, java.lang.String, java.util.HashMap, boolean):java.lang.String
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
        r2 = 1;
        r3 = 0;
        r4 = 0;
        r5 = 0;
        r6 = 0;
        r7 = r6;
        r8 = new java.net.URL;	 Catch:{ Throwable -> 0x0133 }
        r9 = r20;
        r8.<init>(r9);	 Catch:{ Throwable -> 0x0131 }
        r10 = r8.openConnection();	 Catch:{ Throwable -> 0x0131 }
        r7 = r10;	 Catch:{ Throwable -> 0x0131 }
        r10 = "User-Agent";	 Catch:{ Throwable -> 0x0131 }
        r11 = "Mozilla/5.0 (X11; Linux x86_64; rv:10.0) Gecko/20150101 Firefox/47.0 (Chrome)";	 Catch:{ Throwable -> 0x0131 }
        r7.addRequestProperty(r10, r11);	 Catch:{ Throwable -> 0x0131 }
        if (r22 == 0) goto L_0x0029;
    L_0x001b:
        r10 = "Accept-Encoding";	 Catch:{ Throwable -> 0x0023 }
        r11 = "gzip, deflate";	 Catch:{ Throwable -> 0x0023 }
        r7.addRequestProperty(r10, r11);	 Catch:{ Throwable -> 0x0023 }
        goto L_0x0029;
    L_0x0023:
        r0 = move-exception;
        r1 = r0;
        r17 = r2;
        goto L_0x0139;
    L_0x0029:
        r10 = "Accept-Language";	 Catch:{ Throwable -> 0x0131 }
        r11 = "en-us,en;q=0.5";	 Catch:{ Throwable -> 0x0131 }
        r7.addRequestProperty(r10, r11);	 Catch:{ Throwable -> 0x0131 }
        r10 = "Accept";	 Catch:{ Throwable -> 0x0131 }
        r11 = "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8";	 Catch:{ Throwable -> 0x0131 }
        r7.addRequestProperty(r10, r11);	 Catch:{ Throwable -> 0x0131 }
        r10 = "Accept-Charset";	 Catch:{ Throwable -> 0x0131 }
        r11 = "ISO-8859-1,utf-8;q=0.7,*;q=0.7";	 Catch:{ Throwable -> 0x0131 }
        r7.addRequestProperty(r10, r11);	 Catch:{ Throwable -> 0x0131 }
        if (r21 == 0) goto L_0x0064;
    L_0x0040:
        r10 = r21.entrySet();	 Catch:{ Throwable -> 0x0023 }
        r10 = r10.iterator();	 Catch:{ Throwable -> 0x0023 }
    L_0x0048:
        r11 = r10.hasNext();	 Catch:{ Throwable -> 0x0023 }
        if (r11 == 0) goto L_0x0064;	 Catch:{ Throwable -> 0x0023 }
    L_0x004e:
        r11 = r10.next();	 Catch:{ Throwable -> 0x0023 }
        r11 = (java.util.Map.Entry) r11;	 Catch:{ Throwable -> 0x0023 }
        r12 = r11.getKey();	 Catch:{ Throwable -> 0x0023 }
        r12 = (java.lang.String) r12;	 Catch:{ Throwable -> 0x0023 }
        r13 = r11.getValue();	 Catch:{ Throwable -> 0x0023 }
        r13 = (java.lang.String) r13;	 Catch:{ Throwable -> 0x0023 }
        r7.addRequestProperty(r12, r13);	 Catch:{ Throwable -> 0x0023 }
        goto L_0x0048;
    L_0x0064:
        r10 = 5000; // 0x1388 float:7.006E-42 double:2.4703E-320;
        r7.setConnectTimeout(r10);	 Catch:{ Throwable -> 0x0131 }
        r7.setReadTimeout(r10);	 Catch:{ Throwable -> 0x0131 }
        r10 = r7 instanceof java.net.HttpURLConnection;	 Catch:{ Throwable -> 0x0131 }
        if (r10 == 0) goto L_0x00fc;	 Catch:{ Throwable -> 0x0131 }
    L_0x0070:
        r10 = r7;	 Catch:{ Throwable -> 0x0131 }
        r10 = (java.net.HttpURLConnection) r10;	 Catch:{ Throwable -> 0x0131 }
        r11 = 1;	 Catch:{ Throwable -> 0x0131 }
        r10.setInstanceFollowRedirects(r11);	 Catch:{ Throwable -> 0x0131 }
        r11 = r10.getResponseCode();	 Catch:{ Throwable -> 0x0131 }
        r12 = 302; // 0x12e float:4.23E-43 double:1.49E-321;	 Catch:{ Throwable -> 0x0131 }
        if (r11 == r12) goto L_0x008c;	 Catch:{ Throwable -> 0x0131 }
    L_0x007f:
        r12 = 301; // 0x12d float:4.22E-43 double:1.487E-321;	 Catch:{ Throwable -> 0x0131 }
        if (r11 == r12) goto L_0x008c;	 Catch:{ Throwable -> 0x0131 }
    L_0x0083:
        r12 = 303; // 0x12f float:4.25E-43 double:1.497E-321;	 Catch:{ Throwable -> 0x0131 }
        if (r11 != r12) goto L_0x0088;	 Catch:{ Throwable -> 0x0131 }
    L_0x0087:
        goto L_0x008c;	 Catch:{ Throwable -> 0x0131 }
    L_0x0088:
        r17 = r2;	 Catch:{ Throwable -> 0x0131 }
        goto L_0x00fe;	 Catch:{ Throwable -> 0x0131 }
    L_0x008c:
        r12 = "Location";	 Catch:{ Throwable -> 0x0131 }
        r12 = r10.getHeaderField(r12);	 Catch:{ Throwable -> 0x0131 }
        r13 = "Set-Cookie";	 Catch:{ Throwable -> 0x0131 }
        r13 = r10.getHeaderField(r13);	 Catch:{ Throwable -> 0x0131 }
        r14 = new java.net.URL;	 Catch:{ Throwable -> 0x0131 }
        r14.<init>(r12);	 Catch:{ Throwable -> 0x0131 }
        r8 = r14;	 Catch:{ Throwable -> 0x0131 }
        r14 = r8.openConnection();	 Catch:{ Throwable -> 0x0131 }
        r7 = r14;	 Catch:{ Throwable -> 0x0131 }
        r14 = "Cookie";	 Catch:{ Throwable -> 0x0131 }
        r7.setRequestProperty(r14, r13);	 Catch:{ Throwable -> 0x0131 }
        r14 = "User-Agent";	 Catch:{ Throwable -> 0x0131 }
        r6 = "Mozilla/5.0 (X11; Linux x86_64; rv:10.0) Gecko/20150101 Firefox/47.0 (Chrome)";	 Catch:{ Throwable -> 0x0131 }
        r7.addRequestProperty(r14, r6);	 Catch:{ Throwable -> 0x0131 }
        if (r22 == 0) goto L_0x00b8;
    L_0x00b1:
        r6 = "Accept-Encoding";	 Catch:{ Throwable -> 0x0023 }
        r14 = "gzip, deflate";	 Catch:{ Throwable -> 0x0023 }
        r7.addRequestProperty(r6, r14);	 Catch:{ Throwable -> 0x0023 }
    L_0x00b8:
        r6 = "Accept-Language";	 Catch:{ Throwable -> 0x0131 }
        r14 = "en-us,en;q=0.5";	 Catch:{ Throwable -> 0x0131 }
        r7.addRequestProperty(r6, r14);	 Catch:{ Throwable -> 0x0131 }
        r6 = "Accept";	 Catch:{ Throwable -> 0x0131 }
        r14 = "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8";	 Catch:{ Throwable -> 0x0131 }
        r7.addRequestProperty(r6, r14);	 Catch:{ Throwable -> 0x0131 }
        r6 = "Accept-Charset";	 Catch:{ Throwable -> 0x0131 }
        r14 = "ISO-8859-1,utf-8;q=0.7,*;q=0.7";	 Catch:{ Throwable -> 0x0131 }
        r7.addRequestProperty(r6, r14);	 Catch:{ Throwable -> 0x0131 }
        if (r21 == 0) goto L_0x00fc;	 Catch:{ Throwable -> 0x0131 }
    L_0x00cf:
        r6 = r21.entrySet();	 Catch:{ Throwable -> 0x0131 }
        r6 = r6.iterator();	 Catch:{ Throwable -> 0x0131 }
    L_0x00d7:
        r14 = r6.hasNext();	 Catch:{ Throwable -> 0x0131 }
        if (r14 == 0) goto L_0x00fc;	 Catch:{ Throwable -> 0x0131 }
    L_0x00dd:
        r14 = r6.next();	 Catch:{ Throwable -> 0x0131 }
        r14 = (java.util.Map.Entry) r14;	 Catch:{ Throwable -> 0x0131 }
        r16 = r14.getKey();	 Catch:{ Throwable -> 0x0131 }
        r1 = r16;	 Catch:{ Throwable -> 0x0131 }
        r1 = (java.lang.String) r1;	 Catch:{ Throwable -> 0x0131 }
        r16 = r14.getValue();	 Catch:{ Throwable -> 0x0131 }
        r17 = r2;
        r2 = r16;	 Catch:{ Throwable -> 0x012e }
        r2 = (java.lang.String) r2;	 Catch:{ Throwable -> 0x012e }
        r7.addRequestProperty(r1, r2);	 Catch:{ Throwable -> 0x012e }
        r2 = r17;	 Catch:{ Throwable -> 0x012e }
        goto L_0x00d7;	 Catch:{ Throwable -> 0x012e }
    L_0x00fc:
        r17 = r2;	 Catch:{ Throwable -> 0x012e }
    L_0x00fe:
        r7.connect();	 Catch:{ Throwable -> 0x012e }
        if (r22 == 0) goto L_0x0126;
    L_0x0103:
        r1 = new java.util.zip.GZIPInputStream;	 Catch:{ Exception -> 0x010d }
        r2 = r7.getInputStream();	 Catch:{ Exception -> 0x010d }
        r1.<init>(r2);	 Catch:{ Exception -> 0x010d }
        goto L_0x012a;
    L_0x010d:
        r0 = move-exception;
        r1 = r0;
        if (r3 == 0) goto L_0x0117;
    L_0x0111:
        r3.close();	 Catch:{ Exception -> 0x0115 }
        goto L_0x0117;
    L_0x0115:
        r0 = move-exception;
        goto L_0x0118;
        r2 = r8.openConnection();	 Catch:{ Throwable -> 0x012e }
        r7 = r2;	 Catch:{ Throwable -> 0x012e }
        r7.connect();	 Catch:{ Throwable -> 0x012e }
        r2 = r7.getInputStream();	 Catch:{ Throwable -> 0x012e }
        r1 = r2;	 Catch:{ Throwable -> 0x012e }
        goto L_0x010c;	 Catch:{ Throwable -> 0x012e }
    L_0x0126:
        r1 = r7.getInputStream();	 Catch:{ Throwable -> 0x012e }
    L_0x012a:
        r3 = r1;
        r2 = r17;
        goto L_0x016e;
    L_0x012e:
        r0 = move-exception;
        r1 = r0;
        goto L_0x0139;
    L_0x0131:
        r0 = move-exception;
        goto L_0x0136;
    L_0x0133:
        r0 = move-exception;
        r9 = r20;
        r17 = r2;
        r1 = r0;
    L_0x0139:
        r2 = r1 instanceof java.net.SocketTimeoutException;
        if (r2 == 0) goto L_0x0145;
        r2 = org.telegram.tgnet.ConnectionsManager.isNetworkOnline();
        if (r2 == 0) goto L_0x0169;
        r2 = 0;
        goto L_0x016b;
        r2 = r1 instanceof java.net.UnknownHostException;
        if (r2 == 0) goto L_0x014b;
        r2 = 0;
        goto L_0x0144;
        r2 = r1 instanceof java.net.SocketException;
        if (r2 == 0) goto L_0x0163;
        r2 = r1.getMessage();
        if (r2 == 0) goto L_0x0169;
        r2 = r1.getMessage();
        r6 = "ECONNRESET";
        r2 = r2.contains(r6);
        if (r2 == 0) goto L_0x0169;
        r2 = 0;
        goto L_0x0144;
        r2 = r1 instanceof java.io.FileNotFoundException;
        if (r2 == 0) goto L_0x0169;
        r2 = 0;
        goto L_0x0144;
        r2 = r17;
        org.telegram.messenger.FileLog.e(r1);
        if (r2 == 0) goto L_0x01da;
        if (r7 == 0) goto L_0x018c;
        r1 = r7 instanceof java.net.HttpURLConnection;	 Catch:{ Exception -> 0x0186 }
        if (r1 == 0) goto L_0x018c;	 Catch:{ Exception -> 0x0186 }
        r1 = r7;	 Catch:{ Exception -> 0x0186 }
        r1 = (java.net.HttpURLConnection) r1;	 Catch:{ Exception -> 0x0186 }
        r1 = r1.getResponseCode();	 Catch:{ Exception -> 0x0186 }
        r6 = 200; // 0xc8 float:2.8E-43 double:9.9E-322;
        if (r1 == r6) goto L_0x018c;
        r6 = 202; // 0xca float:2.83E-43 double:1.0E-321;
        if (r1 == r6) goto L_0x018c;
        goto L_0x018c;
    L_0x0186:
        r0 = move-exception;
        r1 = r0;
        org.telegram.messenger.FileLog.e(r1);
        goto L_0x018d;
        if (r3 == 0) goto L_0x01cd;
        r1 = 32768; // 0x8000 float:4.5918E-41 double:1.61895E-319;
        r1 = new byte[r1];	 Catch:{ Throwable -> 0x01c8 }
        r6 = r19.isCancelled();	 Catch:{ Throwable -> 0x01c8 }
        if (r6 == 0) goto L_0x019b;
        goto L_0x01c3;
        r6 = r3.read(r1);	 Catch:{ Exception -> 0x01bb }
        if (r6 <= 0) goto L_0x01b5;	 Catch:{ Exception -> 0x01bb }
        if (r5 != 0) goto L_0x01a9;	 Catch:{ Exception -> 0x01bb }
        r8 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x01bb }
        r8.<init>();	 Catch:{ Exception -> 0x01bb }
        r5 = r8;	 Catch:{ Exception -> 0x01bb }
        r8 = new java.lang.String;	 Catch:{ Exception -> 0x01bb }
        r10 = 0;	 Catch:{ Exception -> 0x01bb }
        r11 = "UTF-8";	 Catch:{ Exception -> 0x01bb }
        r8.<init>(r1, r10, r6, r11);	 Catch:{ Exception -> 0x01bb }
        r5.append(r8);	 Catch:{ Exception -> 0x01bb }
        goto L_0x0194;
        r8 = -1;
        if (r6 != r8) goto L_0x01ba;
        r4 = 1;
        goto L_0x01c3;
        goto L_0x01c3;
    L_0x01bb:
        r0 = move-exception;
        r6 = r5;
        r5 = r0;
        org.telegram.messenger.FileLog.e(r5);	 Catch:{ Throwable -> 0x01c4 }
        r5 = r6;
        goto L_0x01cd;
    L_0x01c4:
        r0 = move-exception;
        r1 = r0;
        r5 = r6;
        goto L_0x01ca;
    L_0x01c8:
        r0 = move-exception;
        r1 = r0;
        org.telegram.messenger.FileLog.e(r1);
        if (r3 == 0) goto L_0x01d9;
        r3.close();	 Catch:{ Throwable -> 0x01d3 }
        goto L_0x01d9;
    L_0x01d3:
        r0 = move-exception;
        r1 = r0;
        org.telegram.messenger.FileLog.e(r1);
        goto L_0x01da;
        if (r4 == 0) goto L_0x01e2;
        r6 = r5.toString();
        r15 = r6;
        goto L_0x01e3;
        r15 = 0;
        return r15;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.telegram.ui.Components.WebPlayerView.downloadUrlContent(android.os.AsyncTask, java.lang.String, java.util.HashMap, boolean):java.lang.String");
    }

    protected String downloadUrlContent(AsyncTask parentTask, String url) {
        return downloadUrlContent(parentTask, url, null, true);
    }

    @SuppressLint({"SetJavaScriptEnabled", "AddJavascriptInterface"})
    public WebPlayerView(Context context, boolean allowInline, boolean allowShare, WebPlayerViewDelegate webPlayerViewDelegate) {
        Context context2 = context;
        super(context);
        int i = lastContainerId;
        lastContainerId = i + 1;
        this.fragment_container_id = i;
        r0.allowInlineAnimation = VERSION.SDK_INT >= 21;
        r0.backgroundPaint = new Paint();
        r0.progressRunnable = new Runnable() {
            public void run() {
                if (WebPlayerView.this.videoPlayer != null) {
                    if (WebPlayerView.this.videoPlayer.isPlaying()) {
                        WebPlayerView.this.controlsView.setProgress((int) (WebPlayerView.this.videoPlayer.getCurrentPosition() / 1000));
                        WebPlayerView.this.controlsView.setBufferedProgress((int) (WebPlayerView.this.videoPlayer.getBufferedPosition() / 1000));
                        AndroidUtilities.runOnUIThread(WebPlayerView.this.progressRunnable, 1000);
                    }
                }
            }
        };
        r0.surfaceTextureListener = new SurfaceTextureListener() {
            public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
            }

            public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {
            }

            public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
                if (!WebPlayerView.this.changingTextureView) {
                    return true;
                }
                if (WebPlayerView.this.switchingInlineMode) {
                    WebPlayerView.this.waitingForFirstTextureUpload = 2;
                }
                WebPlayerView.this.textureView.setSurfaceTexture(surface);
                WebPlayerView.this.textureView.setVisibility(0);
                WebPlayerView.this.changingTextureView = false;
                return false;
            }

            public void onSurfaceTextureUpdated(SurfaceTexture surface) {
                if (WebPlayerView.this.waitingForFirstTextureUpload == 1) {
                    WebPlayerView.this.changedTextureView.getViewTreeObserver().addOnPreDrawListener(new OnPreDrawListener() {
                        public boolean onPreDraw() {
                            WebPlayerView.this.changedTextureView.getViewTreeObserver().removeOnPreDrawListener(this);
                            if (WebPlayerView.this.textureImageView != null) {
                                WebPlayerView.this.textureImageView.setVisibility(4);
                                WebPlayerView.this.textureImageView.setImageDrawable(null);
                                if (WebPlayerView.this.currentBitmap != null) {
                                    WebPlayerView.this.currentBitmap.recycle();
                                    WebPlayerView.this.currentBitmap = null;
                                }
                            }
                            AndroidUtilities.runOnUIThread(new Runnable() {
                                public void run() {
                                    WebPlayerView.this.delegate.onInlineSurfaceTextureReady();
                                }
                            });
                            WebPlayerView.this.waitingForFirstTextureUpload = 0;
                            return true;
                        }
                    });
                    WebPlayerView.this.changedTextureView.invalidate();
                }
            }
        };
        r0.switchToInlineRunnable = new Runnable() {
            public void run() {
                WebPlayerView.this.switchingInlineMode = false;
                if (WebPlayerView.this.currentBitmap != null) {
                    WebPlayerView.this.currentBitmap.recycle();
                    WebPlayerView.this.currentBitmap = null;
                }
                WebPlayerView.this.changingTextureView = true;
                if (WebPlayerView.this.textureImageView != null) {
                    try {
                        WebPlayerView.this.currentBitmap = Bitmaps.createBitmap(WebPlayerView.this.textureView.getWidth(), WebPlayerView.this.textureView.getHeight(), Config.ARGB_8888);
                        WebPlayerView.this.textureView.getBitmap(WebPlayerView.this.currentBitmap);
                    } catch (Throwable e) {
                        if (WebPlayerView.this.currentBitmap != null) {
                            WebPlayerView.this.currentBitmap.recycle();
                            WebPlayerView.this.currentBitmap = null;
                        }
                        FileLog.e(e);
                    }
                    if (WebPlayerView.this.currentBitmap != null) {
                        WebPlayerView.this.textureImageView.setVisibility(0);
                        WebPlayerView.this.textureImageView.setImageBitmap(WebPlayerView.this.currentBitmap);
                    } else {
                        WebPlayerView.this.textureImageView.setImageDrawable(null);
                    }
                }
                WebPlayerView.this.isInline = true;
                WebPlayerView.this.updatePlayButton();
                WebPlayerView.this.updateShareButton();
                WebPlayerView.this.updateFullscreenButton();
                WebPlayerView.this.updateInlineButton();
                ViewGroup viewGroup = (ViewGroup) WebPlayerView.this.controlsView.getParent();
                if (viewGroup != null) {
                    viewGroup.removeView(WebPlayerView.this.controlsView);
                }
                WebPlayerView.this.changedTextureView = WebPlayerView.this.delegate.onSwitchInlineMode(WebPlayerView.this.controlsView, WebPlayerView.this.isInline, WebPlayerView.this.aspectRatioFrameLayout.getAspectRatio(), WebPlayerView.this.aspectRatioFrameLayout.getVideoRotation(), WebPlayerView.this.allowInlineAnimation);
                WebPlayerView.this.changedTextureView.setVisibility(4);
                ViewGroup parent = (ViewGroup) WebPlayerView.this.textureView.getParent();
                if (parent != null) {
                    parent.removeView(WebPlayerView.this.textureView);
                }
                WebPlayerView.this.controlsView.show(false, false);
            }
        };
        setWillNotDraw(false);
        r0.delegate = webPlayerViewDelegate;
        r0.backgroundPaint.setColor(Theme.ACTION_BAR_VIDEO_EDIT_COLOR);
        r0.aspectRatioFrameLayout = new AspectRatioFrameLayout(context2) {
            protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
                super.onMeasure(widthMeasureSpec, heightMeasureSpec);
                if (WebPlayerView.this.textureViewContainer != null) {
                    LayoutParams layoutParams = WebPlayerView.this.textureView.getLayoutParams();
                    layoutParams.width = getMeasuredWidth();
                    layoutParams.height = getMeasuredHeight();
                    if (WebPlayerView.this.textureImageView != null) {
                        layoutParams = WebPlayerView.this.textureImageView.getLayoutParams();
                        layoutParams.width = getMeasuredWidth();
                        layoutParams.height = getMeasuredHeight();
                    }
                }
            }
        };
        addView(r0.aspectRatioFrameLayout, LayoutHelper.createFrame(-1, -1, 17));
        r0.interfaceName = "JavaScriptInterface";
        r0.webView = new WebView(context2);
        r0.webView.addJavascriptInterface(new JavaScriptInterface(new CallJavaResultInterface() {
            public void jsCallFinished(String value) {
                if (WebPlayerView.this.currentTask != null && !WebPlayerView.this.currentTask.isCancelled() && (WebPlayerView.this.currentTask instanceof YoutubeVideoTask)) {
                    ((YoutubeVideoTask) WebPlayerView.this.currentTask).onInterfaceResult(value);
                }
            }
        }), r0.interfaceName);
        WebSettings webSettings = r0.webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDefaultTextEncodingName("utf-8");
        r0.textureViewContainer = r0.delegate.getTextureViewContainer();
        r0.textureView = new TextureView(context2);
        r0.textureView.setPivotX(0.0f);
        r0.textureView.setPivotY(0.0f);
        if (r0.textureViewContainer != null) {
            r0.textureViewContainer.addView(r0.textureView);
        } else {
            r0.aspectRatioFrameLayout.addView(r0.textureView, LayoutHelper.createFrame(-1, -1, 17));
        }
        if (r0.allowInlineAnimation && r0.textureViewContainer != null) {
            r0.textureImageView = new ImageView(context2);
            r0.textureImageView.setBackgroundColor(-65536);
            r0.textureImageView.setPivotX(0.0f);
            r0.textureImageView.setPivotY(0.0f);
            r0.textureImageView.setVisibility(4);
            r0.textureViewContainer.addView(r0.textureImageView);
        }
        r0.videoPlayer = new VideoPlayer();
        r0.videoPlayer.setDelegate(r0);
        r0.videoPlayer.setTextureView(r0.textureView);
        r0.controlsView = new ControlsView(context2);
        if (r0.textureViewContainer != null) {
            r0.textureViewContainer.addView(r0.controlsView);
        } else {
            addView(r0.controlsView, LayoutHelper.createFrame(-1, -1.0f));
        }
        r0.progressView = new RadialProgressView(context2);
        r0.progressView.setProgressColor(-1);
        addView(r0.progressView, LayoutHelper.createFrame(48, 48, 17));
        r0.fullscreenButton = new ImageView(context2);
        r0.fullscreenButton.setScaleType(ScaleType.CENTER);
        r0.controlsView.addView(r0.fullscreenButton, LayoutHelper.createFrame(56, 56.0f, 85, 0.0f, 0.0f, 0.0f, 5.0f));
        r0.fullscreenButton.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                if (!(!WebPlayerView.this.initied || WebPlayerView.this.changingTextureView || WebPlayerView.this.switchingInlineMode)) {
                    if (WebPlayerView.this.firstFrameRendered) {
                        WebPlayerView.this.inFullscreen = WebPlayerView.this.inFullscreen ^ true;
                        WebPlayerView.this.updateFullscreenState(true);
                    }
                }
            }
        });
        r0.playButton = new ImageView(context2);
        r0.playButton.setScaleType(ScaleType.CENTER);
        r0.controlsView.addView(r0.playButton, LayoutHelper.createFrame(48, 48, 17));
        r0.playButton.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                if (WebPlayerView.this.initied) {
                    if (WebPlayerView.this.playVideoUrl != null) {
                        if (!WebPlayerView.this.videoPlayer.isPlayerPrepared()) {
                            WebPlayerView.this.preparePlayer();
                        }
                        if (WebPlayerView.this.videoPlayer.isPlaying()) {
                            WebPlayerView.this.videoPlayer.pause();
                        } else {
                            WebPlayerView.this.isCompleted = false;
                            WebPlayerView.this.videoPlayer.play();
                        }
                        WebPlayerView.this.updatePlayButton();
                    }
                }
            }
        });
        if (allowInline) {
            r0.inlineButton = new ImageView(context2);
            r0.inlineButton.setScaleType(ScaleType.CENTER);
            r0.controlsView.addView(r0.inlineButton, LayoutHelper.createFrame(56, 48, 53));
            r0.inlineButton.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    if (!(WebPlayerView.this.textureView == null || !WebPlayerView.this.delegate.checkInlinePermissions() || WebPlayerView.this.changingTextureView || WebPlayerView.this.switchingInlineMode)) {
                        if (WebPlayerView.this.firstFrameRendered) {
                            WebPlayerView.this.switchingInlineMode = true;
                            if (WebPlayerView.this.isInline) {
                                ViewGroup parent = (ViewGroup) WebPlayerView.this.aspectRatioFrameLayout.getParent();
                                if (parent != WebPlayerView.this) {
                                    if (parent != null) {
                                        parent.removeView(WebPlayerView.this.aspectRatioFrameLayout);
                                    }
                                    WebPlayerView.this.addView(WebPlayerView.this.aspectRatioFrameLayout, 0, LayoutHelper.createFrame(-1, -1, 17));
                                    WebPlayerView.this.aspectRatioFrameLayout.measure(MeasureSpec.makeMeasureSpec(WebPlayerView.this.getMeasuredWidth(), 1073741824), MeasureSpec.makeMeasureSpec(WebPlayerView.this.getMeasuredHeight() - AndroidUtilities.dp(10.0f), 1073741824));
                                }
                                if (WebPlayerView.this.currentBitmap != null) {
                                    WebPlayerView.this.currentBitmap.recycle();
                                    WebPlayerView.this.currentBitmap = null;
                                }
                                WebPlayerView.this.changingTextureView = true;
                                WebPlayerView.this.isInline = false;
                                WebPlayerView.this.updatePlayButton();
                                WebPlayerView.this.updateShareButton();
                                WebPlayerView.this.updateFullscreenButton();
                                WebPlayerView.this.updateInlineButton();
                                WebPlayerView.this.textureView.setVisibility(4);
                                if (WebPlayerView.this.textureViewContainer != null) {
                                    WebPlayerView.this.textureViewContainer.addView(WebPlayerView.this.textureView);
                                } else {
                                    WebPlayerView.this.aspectRatioFrameLayout.addView(WebPlayerView.this.textureView);
                                }
                                parent = (ViewGroup) WebPlayerView.this.controlsView.getParent();
                                if (parent != WebPlayerView.this) {
                                    if (parent != null) {
                                        parent.removeView(WebPlayerView.this.controlsView);
                                    }
                                    if (WebPlayerView.this.textureViewContainer != null) {
                                        WebPlayerView.this.textureViewContainer.addView(WebPlayerView.this.controlsView);
                                    } else {
                                        WebPlayerView.this.addView(WebPlayerView.this.controlsView, 1);
                                    }
                                }
                                WebPlayerView.this.controlsView.show(false, false);
                                WebPlayerView.this.delegate.prepareToSwitchInlineMode(false, null, WebPlayerView.this.aspectRatioFrameLayout.getAspectRatio(), WebPlayerView.this.allowInlineAnimation);
                            } else {
                                WebPlayerView.this.inFullscreen = false;
                                WebPlayerView.this.delegate.prepareToSwitchInlineMode(true, WebPlayerView.this.switchToInlineRunnable, WebPlayerView.this.aspectRatioFrameLayout.getAspectRatio(), WebPlayerView.this.allowInlineAnimation);
                            }
                        }
                    }
                }
            });
        }
        if (allowShare) {
            r0.shareButton = new ImageView(context2);
            r0.shareButton.setScaleType(ScaleType.CENTER);
            r0.shareButton.setImageResource(R.drawable.ic_share_video);
            r0.controlsView.addView(r0.shareButton, LayoutHelper.createFrame(56, 48, 53));
            r0.shareButton.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    if (WebPlayerView.this.delegate != null) {
                        WebPlayerView.this.delegate.onSharePressed();
                    }
                }
            });
        }
        updatePlayButton();
        updateFullscreenButton();
        updateInlineButton();
        updateShareButton();
    }

    private void onInitFailed() {
        if (this.controlsView.getParent() != this) {
            this.controlsView.setVisibility(8);
        }
        this.delegate.onInitFailed();
    }

    public void updateTextureImageView() {
        if (this.textureImageView != null) {
            try {
                this.currentBitmap = Bitmaps.createBitmap(this.textureView.getWidth(), this.textureView.getHeight(), Config.ARGB_8888);
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
    }

    public String getYoutubeId() {
        return this.currentYoutubeId;
    }

    public void onStateChanged(boolean playWhenReady, int playbackState) {
        if (playbackState != 2) {
            if (this.videoPlayer.getDuration() != C.TIME_UNSET) {
                this.controlsView.setDuration((int) (this.videoPlayer.getDuration() / 1000));
            } else {
                this.controlsView.setDuration(0);
            }
        }
        if (playbackState == 4 || playbackState == 1 || !this.videoPlayer.isPlaying()) {
            this.delegate.onPlayStateChanged(this, false);
        } else {
            this.delegate.onPlayStateChanged(this, true);
        }
        if (this.videoPlayer.isPlaying() && playbackState != 4) {
            updatePlayButton();
        } else if (playbackState == 4) {
            this.isCompleted = true;
            this.videoPlayer.pause();
            this.videoPlayer.seekTo(0);
            updatePlayButton();
            this.controlsView.show(true, true);
        }
    }

    protected void onDraw(Canvas canvas) {
        canvas.drawRect(0.0f, 0.0f, (float) getMeasuredWidth(), (float) (getMeasuredHeight() - AndroidUtilities.dp(10.0f)), this.backgroundPaint);
    }

    public void onError(Exception e) {
        FileLog.e((Throwable) e);
        onInitFailed();
    }

    public void onVideoSizeChanged(int width, int height, int unappliedRotationDegrees, float pixelWidthHeightRatio) {
        if (this.aspectRatioFrameLayout != null) {
            if (unappliedRotationDegrees == 90 || unappliedRotationDegrees == 270) {
                int temp = width;
                width = height;
                height = temp;
            }
            float ratio = height == 0 ? 1.0f : (((float) width) * pixelWidthHeightRatio) / ((float) height);
            this.aspectRatioFrameLayout.setAspectRatio(ratio, unappliedRotationDegrees);
            if (this.inFullscreen) {
                this.delegate.onVideoSizeChanged(ratio, unappliedRotationDegrees);
            }
        }
    }

    public void onRenderedFirstFrame() {
        this.firstFrameRendered = true;
        this.lastUpdateTime = System.currentTimeMillis();
        this.controlsView.invalidate();
    }

    public boolean onSurfaceDestroyed(SurfaceTexture surfaceTexture) {
        if (this.changingTextureView) {
            this.changingTextureView = false;
            if (this.inFullscreen || this.isInline) {
                if (this.isInline) {
                    this.waitingForFirstTextureUpload = 1;
                }
                this.changedTextureView.setSurfaceTexture(surfaceTexture);
                this.changedTextureView.setSurfaceTextureListener(this.surfaceTextureListener);
                this.changedTextureView.setVisibility(0);
                return true;
            }
        }
        return false;
    }

    public void onSurfaceTextureUpdated(SurfaceTexture surfaceTexture) {
        if (this.waitingForFirstTextureUpload == 2) {
            if (this.textureImageView != null) {
                this.textureImageView.setVisibility(4);
                this.textureImageView.setImageDrawable(null);
                if (this.currentBitmap != null) {
                    this.currentBitmap.recycle();
                    this.currentBitmap = null;
                }
            }
            this.switchingInlineMode = false;
            this.delegate.onSwitchInlineMode(this.controlsView, false, this.aspectRatioFrameLayout.getAspectRatio(), this.aspectRatioFrameLayout.getVideoRotation(), this.allowInlineAnimation);
            this.waitingForFirstTextureUpload = 0;
        }
    }

    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int x = ((r - l) - this.aspectRatioFrameLayout.getMeasuredWidth()) / 2;
        int y = (((b - t) - AndroidUtilities.dp(10.0f)) - this.aspectRatioFrameLayout.getMeasuredHeight()) / 2;
        this.aspectRatioFrameLayout.layout(x, y, this.aspectRatioFrameLayout.getMeasuredWidth() + x, this.aspectRatioFrameLayout.getMeasuredHeight() + y);
        if (this.controlsView.getParent() == this) {
            this.controlsView.layout(0, 0, this.controlsView.getMeasuredWidth(), this.controlsView.getMeasuredHeight());
        }
        int x2 = ((r - l) - this.progressView.getMeasuredWidth()) / 2;
        x = ((b - t) - this.progressView.getMeasuredHeight()) / 2;
        this.progressView.layout(x2, x, this.progressView.getMeasuredWidth() + x2, this.progressView.getMeasuredHeight() + x);
        this.controlsView.imageReceiver.setImageCoords(0, 0, getMeasuredWidth(), getMeasuredHeight() - AndroidUtilities.dp(10.0f));
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        this.aspectRatioFrameLayout.measure(MeasureSpec.makeMeasureSpec(width, 1073741824), MeasureSpec.makeMeasureSpec(height - AndroidUtilities.dp(10.0f), 1073741824));
        if (this.controlsView.getParent() == this) {
            this.controlsView.measure(MeasureSpec.makeMeasureSpec(width, 1073741824), MeasureSpec.makeMeasureSpec(height, 1073741824));
        }
        this.progressView.measure(MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(44.0f), 1073741824), MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(44.0f), 1073741824));
        setMeasuredDimension(width, height);
    }

    private void updatePlayButton() {
        this.controlsView.checkNeedHide();
        AndroidUtilities.cancelRunOnUIThread(this.progressRunnable);
        if (this.videoPlayer.isPlaying()) {
            this.playButton.setImageResource(this.isInline ? R.drawable.ic_pauseinline : R.drawable.ic_pause);
            AndroidUtilities.runOnUIThread(this.progressRunnable, 500);
            checkAudioFocus();
        } else if (this.isCompleted) {
            this.playButton.setImageResource(this.isInline ? R.drawable.ic_againinline : R.drawable.ic_again);
        } else {
            this.playButton.setImageResource(this.isInline ? R.drawable.ic_playinline : R.drawable.ic_play);
        }
    }

    private void checkAudioFocus() {
        if (!this.hasAudioFocus) {
            AudioManager audioManager = (AudioManager) ApplicationLoader.applicationContext.getSystemService(MimeTypes.BASE_TYPE_AUDIO);
            this.hasAudioFocus = true;
            if (audioManager.requestAudioFocus(this, 3, 1) == 1) {
                this.audioFocus = 2;
            }
        }
    }

    public void onAudioFocusChange(int focusChange) {
        if (focusChange == -1) {
            if (this.videoPlayer.isPlaying()) {
                this.videoPlayer.pause();
                updatePlayButton();
            }
            this.hasAudioFocus = false;
            this.audioFocus = 0;
        } else if (focusChange == 1) {
            this.audioFocus = 2;
            if (this.resumeAudioOnFocusGain) {
                this.resumeAudioOnFocusGain = false;
                this.videoPlayer.play();
            }
        } else if (focusChange == -3) {
            this.audioFocus = 1;
        } else if (focusChange == -2) {
            this.audioFocus = 0;
            if (this.videoPlayer.isPlaying()) {
                this.resumeAudioOnFocusGain = true;
                this.videoPlayer.pause();
                updatePlayButton();
            }
        }
    }

    private void updateFullscreenButton() {
        if (this.videoPlayer.isPlayerPrepared()) {
            if (!this.isInline) {
                this.fullscreenButton.setVisibility(0);
                if (this.inFullscreen) {
                    this.fullscreenButton.setImageResource(R.drawable.ic_outfullscreen);
                    this.fullscreenButton.setLayoutParams(LayoutHelper.createFrame(56, 56.0f, 85, 0.0f, 0.0f, 0.0f, 1.0f));
                } else {
                    this.fullscreenButton.setImageResource(R.drawable.ic_gofullscreen);
                    this.fullscreenButton.setLayoutParams(LayoutHelper.createFrame(56, 56.0f, 85, 0.0f, 0.0f, 0.0f, 5.0f));
                }
                return;
            }
        }
        this.fullscreenButton.setVisibility(8);
    }

    private void updateShareButton() {
        if (this.shareButton != null) {
            int i;
            ImageView imageView = this.shareButton;
            if (!this.isInline) {
                if (this.videoPlayer.isPlayerPrepared()) {
                    i = 0;
                    imageView.setVisibility(i);
                }
            }
            i = 8;
            imageView.setVisibility(i);
        }
    }

    private View getControlView() {
        return this.controlsView;
    }

    private View getProgressView() {
        return this.progressView;
    }

    private void updateInlineButton() {
        if (this.inlineButton != null) {
            this.inlineButton.setImageResource(this.isInline ? R.drawable.ic_goinline : R.drawable.ic_outinline);
            this.inlineButton.setVisibility(this.videoPlayer.isPlayerPrepared() ? 0 : 8);
            if (this.isInline) {
                this.inlineButton.setLayoutParams(LayoutHelper.createFrame(40, 40, 53));
            } else {
                this.inlineButton.setLayoutParams(LayoutHelper.createFrame(56, 50, 53));
            }
        }
    }

    private void preparePlayer() {
        if (this.playVideoUrl != null) {
            if (this.playVideoUrl == null || this.playAudioUrl == null) {
                this.videoPlayer.preparePlayer(Uri.parse(this.playVideoUrl), this.playVideoType);
            } else {
                this.videoPlayer.preparePlayerLoop(Uri.parse(this.playVideoUrl), this.playVideoType, Uri.parse(this.playAudioUrl), this.playAudioType);
            }
            this.videoPlayer.setPlayWhenReady(this.isAutoplay);
            this.isLoading = false;
            if (this.videoPlayer.getDuration() != C.TIME_UNSET) {
                this.controlsView.setDuration((int) (this.videoPlayer.getDuration() / 1000));
            } else {
                this.controlsView.setDuration(0);
            }
            updateFullscreenButton();
            updateShareButton();
            updateInlineButton();
            this.controlsView.invalidate();
            if (this.seekToTime != -1) {
                this.videoPlayer.seekTo((long) (this.seekToTime * 1000));
            }
        }
    }

    public void pause() {
        this.videoPlayer.pause();
        updatePlayButton();
        this.controlsView.show(true, true);
    }

    private void updateFullscreenState(boolean byButton) {
        if (this.textureView != null) {
            updateFullscreenButton();
            ViewGroup viewGroup;
            if (this.textureViewContainer == null) {
                this.changingTextureView = true;
                if (!this.inFullscreen) {
                    if (this.textureViewContainer != null) {
                        this.textureViewContainer.addView(this.textureView);
                    } else {
                        this.aspectRatioFrameLayout.addView(this.textureView);
                    }
                }
                if (this.inFullscreen) {
                    viewGroup = (ViewGroup) this.controlsView.getParent();
                    if (viewGroup != null) {
                        viewGroup.removeView(this.controlsView);
                    }
                } else {
                    ViewGroup parent = (ViewGroup) this.controlsView.getParent();
                    if (parent != this) {
                        if (parent != null) {
                            parent.removeView(this.controlsView);
                        }
                        if (this.textureViewContainer != null) {
                            this.textureViewContainer.addView(this.controlsView);
                        } else {
                            addView(this.controlsView, 1);
                        }
                    }
                }
                this.changedTextureView = this.delegate.onSwitchToFullscreen(this.controlsView, this.inFullscreen, this.aspectRatioFrameLayout.getAspectRatio(), this.aspectRatioFrameLayout.getVideoRotation(), byButton);
                this.changedTextureView.setVisibility(4);
                if (this.inFullscreen && this.changedTextureView != null) {
                    viewGroup = (ViewGroup) this.textureView.getParent();
                    if (viewGroup != null) {
                        viewGroup.removeView(this.textureView);
                    }
                }
                this.controlsView.checkNeedHide();
            } else {
                if (this.inFullscreen) {
                    viewGroup = (ViewGroup) this.aspectRatioFrameLayout.getParent();
                    if (viewGroup != null) {
                        viewGroup.removeView(this.aspectRatioFrameLayout);
                    }
                } else {
                    viewGroup = (ViewGroup) this.aspectRatioFrameLayout.getParent();
                    if (viewGroup != this) {
                        if (viewGroup != null) {
                            viewGroup.removeView(this.aspectRatioFrameLayout);
                        }
                        addView(this.aspectRatioFrameLayout, 0);
                    }
                }
                this.delegate.onSwitchToFullscreen(this.controlsView, this.inFullscreen, this.aspectRatioFrameLayout.getAspectRatio(), this.aspectRatioFrameLayout.getVideoRotation(), byButton);
            }
        }
    }

    public void exitFullscreen() {
        if (this.inFullscreen) {
            this.inFullscreen = false;
            updateInlineButton();
            updateFullscreenState(false);
        }
    }

    public boolean isInitied() {
        return this.initied;
    }

    public boolean isInline() {
        if (!this.isInline) {
            if (!this.switchingInlineMode) {
                return false;
            }
        }
        return true;
    }

    public void enterFullscreen() {
        if (!this.inFullscreen) {
            this.inFullscreen = true;
            updateInlineButton();
            updateFullscreenState(false);
        }
    }

    public boolean isInFullscreen() {
        return this.inFullscreen;
    }

    public boolean loadVideo(String url, Photo thumb, String originalUrl, boolean autoplay) {
        String str;
        String t;
        Throwable e;
        Throwable youtubeId;
        Matcher matcher;
        PhotoSize photoSize;
        YoutubeVideoTask task;
        VimeoVideoTask task2;
        CoubVideoTask task3;
        AparatVideoTask task4;
        TwitchClipVideoTask task5;
        TwitchStreamVideoTask task6;
        Executor executor;
        Void[] voidArr;
        boolean z;
        String str2 = url;
        Photo photo = thumb;
        String youtubeId2 = null;
        String vimeoId = null;
        String coubId = null;
        String twitchClipId = null;
        String twitchStreamId = null;
        String mp4File = null;
        String aparatId = null;
        this.seekToTime = -1;
        if (str2 == null) {
            str = null;
        } else if (str2.endsWith(".mp4")) {
            mp4File = str2;
        } else {
            Matcher matcher2;
            String id;
            if (originalUrl != null) {
                try {
                    Uri uri = Uri.parse(originalUrl);
                    t = uri.getQueryParameter("t");
                    if (t == null) {
                        try {
                            t = uri.getQueryParameter("time_continue");
                        } catch (Exception e2) {
                            e = e2;
                            str = null;
                            youtubeId = e;
                            try {
                                FileLog.e(youtubeId);
                                matcher2 = youtubeIdRegex.matcher(str2);
                                t = null;
                                if (matcher2.find()) {
                                    t = matcher2.group(1);
                                }
                                if (t != null) {
                                    str = t;
                                }
                            } catch (Throwable e3) {
                                FileLog.e(e3);
                            }
                            youtubeId2 = str;
                            if (youtubeId2 == null) {
                                try {
                                    matcher = vimeoIdRegex.matcher(str2);
                                    id = null;
                                    if (matcher.find()) {
                                        id = matcher.group(3);
                                    }
                                    if (id != null) {
                                        vimeoId = id;
                                    }
                                } catch (Throwable e32) {
                                    FileLog.e(e32);
                                }
                            }
                            if (vimeoId == null) {
                                try {
                                    matcher = aparatIdRegex.matcher(str2);
                                    id = null;
                                    if (matcher.find()) {
                                        id = matcher.group(1);
                                    }
                                    if (id != null) {
                                        aparatId = id;
                                    }
                                } catch (Throwable e322) {
                                    FileLog.e(e322);
                                }
                            }
                            if (aparatId == null) {
                                try {
                                    matcher = twitchClipIdRegex.matcher(str2);
                                    id = null;
                                    if (matcher.find()) {
                                        id = matcher.group(1);
                                    }
                                    if (id != null) {
                                        twitchClipId = id;
                                    }
                                } catch (Throwable e3222) {
                                    FileLog.e(e3222);
                                }
                            }
                            if (twitchClipId == null) {
                                try {
                                    matcher = twitchStreamIdRegex.matcher(str2);
                                    id = null;
                                    if (matcher.find()) {
                                        id = matcher.group(1);
                                    }
                                    if (id != null) {
                                        twitchStreamId = id;
                                    }
                                } catch (Throwable e32222) {
                                    FileLog.e(e32222);
                                }
                            }
                            if (twitchStreamId == null) {
                                try {
                                    matcher = coubIdRegex.matcher(str2);
                                    id = null;
                                    if (matcher.find()) {
                                        id = matcher.group(1);
                                    }
                                    if (id != null) {
                                        coubId = id;
                                    }
                                } catch (Throwable e322222) {
                                    FileLog.e(e322222);
                                }
                            }
                            r1.initied = false;
                            r1.isCompleted = false;
                            r1.isAutoplay = autoplay;
                            r1.playVideoUrl = null;
                            r1.playAudioUrl = null;
                            destroy();
                            r1.firstFrameRendered = false;
                            r1.currentAlpha = 1.0f;
                            if (r1.currentTask != null) {
                                r1.currentTask.cancel(true);
                                r1.currentTask = null;
                            }
                            updateFullscreenButton();
                            updateShareButton();
                            updateInlineButton();
                            updatePlayButton();
                            if (photo == null) {
                                r1.drawImage = false;
                            } else {
                                photoSize = FileLoader.getClosestPhotoSizeWithSize(photo.sizes, 80, true);
                                if (photoSize != null) {
                                    r1.controlsView.imageReceiver.setImage(null, null, photo == null ? null : photoSize.location, photo == null ? null : "80_80_b", 0, null, 1);
                                    r1.drawImage = true;
                                }
                            }
                            if (r1.progressAnimation != null) {
                                r1.progressAnimation.cancel();
                                r1.progressAnimation = null;
                            }
                            r1.isLoading = true;
                            r1.controlsView.setProgress(0);
                            r1.currentYoutubeId = youtubeId2;
                            youtubeId2 = null;
                            if (mp4File == null) {
                                if (youtubeId2 == null) {
                                    task = new YoutubeVideoTask(youtubeId2);
                                    task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new Void[]{null, null, null});
                                    r1.currentTask = task;
                                } else if (vimeoId == null) {
                                    task2 = new VimeoVideoTask(vimeoId);
                                    task2.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new Void[]{null, null, null});
                                    r1.currentTask = task2;
                                } else if (coubId == null) {
                                    task3 = new CoubVideoTask(coubId);
                                    task3.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new Void[]{null, null, null});
                                    r1.currentTask = task3;
                                    r1.isStream = true;
                                } else if (aparatId == null) {
                                    task4 = new AparatVideoTask(aparatId);
                                    task4.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new Void[]{null, null, null});
                                    r1.currentTask = task4;
                                } else if (twitchClipId == null) {
                                    task5 = new TwitchClipVideoTask(str2, twitchClipId);
                                    task5.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new Void[]{null, null, null});
                                    r1.currentTask = task5;
                                } else if (twitchStreamId != null) {
                                    task6 = new TwitchStreamVideoTask(str2, twitchStreamId);
                                    executor = AsyncTask.THREAD_POOL_EXECUTOR;
                                    voidArr = new Void[3];
                                    z = true;
                                    voidArr[1] = null;
                                    voidArr[2] = null;
                                    task6.executeOnExecutor(executor, voidArr);
                                    r1.currentTask = task6;
                                    r1.isStream = true;
                                    r1.controlsView.show(false, false);
                                    showProgress(z, false);
                                }
                                z = true;
                                r1.controlsView.show(false, false);
                                showProgress(z, false);
                            } else {
                                r1.initied = true;
                                r1.playVideoUrl = mp4File;
                                r1.playVideoType = "other";
                                if (r1.isAutoplay) {
                                    preparePlayer();
                                }
                                showProgress(false, false);
                                r1.controlsView.show(true, true);
                            }
                            if (twitchStreamId != null) {
                                r1.controlsView.setVisibility(8);
                                return false;
                            }
                            r1.controlsView.setVisibility(0);
                            return true;
                        }
                    }
                    if (t == null) {
                        str = null;
                    } else if (t.contains("m")) {
                        String[] args = t.split("m");
                        str = null;
                        try {
                            r1.seekToTime = (Utilities.parseInt(args[0]).intValue() * 60) + Utilities.parseInt(args[1]).intValue();
                        } catch (Exception e4) {
                            e322222 = e4;
                            youtubeId = e322222;
                            FileLog.e(youtubeId);
                            matcher2 = youtubeIdRegex.matcher(str2);
                            t = null;
                            if (matcher2.find()) {
                                t = matcher2.group(1);
                            }
                            if (t != null) {
                                str = t;
                            }
                            youtubeId2 = str;
                            if (youtubeId2 == null) {
                                matcher = vimeoIdRegex.matcher(str2);
                                id = null;
                                if (matcher.find()) {
                                    id = matcher.group(3);
                                }
                                if (id != null) {
                                    vimeoId = id;
                                }
                            }
                            if (vimeoId == null) {
                                matcher = aparatIdRegex.matcher(str2);
                                id = null;
                                if (matcher.find()) {
                                    id = matcher.group(1);
                                }
                                if (id != null) {
                                    aparatId = id;
                                }
                            }
                            if (aparatId == null) {
                                matcher = twitchClipIdRegex.matcher(str2);
                                id = null;
                                if (matcher.find()) {
                                    id = matcher.group(1);
                                }
                                if (id != null) {
                                    twitchClipId = id;
                                }
                            }
                            if (twitchClipId == null) {
                                matcher = twitchStreamIdRegex.matcher(str2);
                                id = null;
                                if (matcher.find()) {
                                    id = matcher.group(1);
                                }
                                if (id != null) {
                                    twitchStreamId = id;
                                }
                            }
                            if (twitchStreamId == null) {
                                matcher = coubIdRegex.matcher(str2);
                                id = null;
                                if (matcher.find()) {
                                    id = matcher.group(1);
                                }
                                if (id != null) {
                                    coubId = id;
                                }
                            }
                            r1.initied = false;
                            r1.isCompleted = false;
                            r1.isAutoplay = autoplay;
                            r1.playVideoUrl = null;
                            r1.playAudioUrl = null;
                            destroy();
                            r1.firstFrameRendered = false;
                            r1.currentAlpha = 1.0f;
                            if (r1.currentTask != null) {
                                r1.currentTask.cancel(true);
                                r1.currentTask = null;
                            }
                            updateFullscreenButton();
                            updateShareButton();
                            updateInlineButton();
                            updatePlayButton();
                            if (photo == null) {
                                r1.drawImage = false;
                            } else {
                                photoSize = FileLoader.getClosestPhotoSizeWithSize(photo.sizes, 80, true);
                                if (photoSize != null) {
                                    if (photo == null) {
                                    }
                                    if (photo == null) {
                                    }
                                    r1.controlsView.imageReceiver.setImage(null, null, photo == null ? null : photoSize.location, photo == null ? null : "80_80_b", 0, null, 1);
                                    r1.drawImage = true;
                                }
                            }
                            if (r1.progressAnimation != null) {
                                r1.progressAnimation.cancel();
                                r1.progressAnimation = null;
                            }
                            r1.isLoading = true;
                            r1.controlsView.setProgress(0);
                            r1.currentYoutubeId = youtubeId2;
                            youtubeId2 = null;
                            if (mp4File == null) {
                                if (youtubeId2 == null) {
                                    task = new YoutubeVideoTask(youtubeId2);
                                    task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new Void[]{null, null, null});
                                    r1.currentTask = task;
                                } else if (vimeoId == null) {
                                    task2 = new VimeoVideoTask(vimeoId);
                                    task2.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new Void[]{null, null, null});
                                    r1.currentTask = task2;
                                } else if (coubId == null) {
                                    task3 = new CoubVideoTask(coubId);
                                    task3.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new Void[]{null, null, null});
                                    r1.currentTask = task3;
                                    r1.isStream = true;
                                } else if (aparatId == null) {
                                    task4 = new AparatVideoTask(aparatId);
                                    task4.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new Void[]{null, null, null});
                                    r1.currentTask = task4;
                                } else if (twitchClipId == null) {
                                    task5 = new TwitchClipVideoTask(str2, twitchClipId);
                                    task5.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new Void[]{null, null, null});
                                    r1.currentTask = task5;
                                } else if (twitchStreamId != null) {
                                    task6 = new TwitchStreamVideoTask(str2, twitchStreamId);
                                    executor = AsyncTask.THREAD_POOL_EXECUTOR;
                                    voidArr = new Void[3];
                                    z = true;
                                    voidArr[1] = null;
                                    voidArr[2] = null;
                                    task6.executeOnExecutor(executor, voidArr);
                                    r1.currentTask = task6;
                                    r1.isStream = true;
                                    r1.controlsView.show(false, false);
                                    showProgress(z, false);
                                }
                                z = true;
                                r1.controlsView.show(false, false);
                                showProgress(z, false);
                            } else {
                                r1.initied = true;
                                r1.playVideoUrl = mp4File;
                                r1.playVideoType = "other";
                                if (r1.isAutoplay) {
                                    preparePlayer();
                                }
                                showProgress(false, false);
                                r1.controlsView.show(true, true);
                            }
                            if (twitchStreamId != null) {
                                r1.controlsView.setVisibility(8);
                                return false;
                            }
                            r1.controlsView.setVisibility(0);
                            return true;
                        }
                    } else {
                        str = null;
                        r1.seekToTime = Utilities.parseInt(t).intValue();
                    }
                } catch (Throwable e3222222) {
                    str = null;
                    youtubeId = e3222222;
                    FileLog.e(youtubeId);
                    matcher2 = youtubeIdRegex.matcher(str2);
                    t = null;
                    if (matcher2.find()) {
                        t = matcher2.group(1);
                    }
                    if (t != null) {
                        str = t;
                    }
                    youtubeId2 = str;
                    if (youtubeId2 == null) {
                        matcher = vimeoIdRegex.matcher(str2);
                        id = null;
                        if (matcher.find()) {
                            id = matcher.group(3);
                        }
                        if (id != null) {
                            vimeoId = id;
                        }
                    }
                    if (vimeoId == null) {
                        matcher = aparatIdRegex.matcher(str2);
                        id = null;
                        if (matcher.find()) {
                            id = matcher.group(1);
                        }
                        if (id != null) {
                            aparatId = id;
                        }
                    }
                    if (aparatId == null) {
                        matcher = twitchClipIdRegex.matcher(str2);
                        id = null;
                        if (matcher.find()) {
                            id = matcher.group(1);
                        }
                        if (id != null) {
                            twitchClipId = id;
                        }
                    }
                    if (twitchClipId == null) {
                        matcher = twitchStreamIdRegex.matcher(str2);
                        id = null;
                        if (matcher.find()) {
                            id = matcher.group(1);
                        }
                        if (id != null) {
                            twitchStreamId = id;
                        }
                    }
                    if (twitchStreamId == null) {
                        matcher = coubIdRegex.matcher(str2);
                        id = null;
                        if (matcher.find()) {
                            id = matcher.group(1);
                        }
                        if (id != null) {
                            coubId = id;
                        }
                    }
                    r1.initied = false;
                    r1.isCompleted = false;
                    r1.isAutoplay = autoplay;
                    r1.playVideoUrl = null;
                    r1.playAudioUrl = null;
                    destroy();
                    r1.firstFrameRendered = false;
                    r1.currentAlpha = 1.0f;
                    if (r1.currentTask != null) {
                        r1.currentTask.cancel(true);
                        r1.currentTask = null;
                    }
                    updateFullscreenButton();
                    updateShareButton();
                    updateInlineButton();
                    updatePlayButton();
                    if (photo == null) {
                        photoSize = FileLoader.getClosestPhotoSizeWithSize(photo.sizes, 80, true);
                        if (photoSize != null) {
                            if (photo == null) {
                            }
                            if (photo == null) {
                            }
                            r1.controlsView.imageReceiver.setImage(null, null, photo == null ? null : photoSize.location, photo == null ? null : "80_80_b", 0, null, 1);
                            r1.drawImage = true;
                        }
                    } else {
                        r1.drawImage = false;
                    }
                    if (r1.progressAnimation != null) {
                        r1.progressAnimation.cancel();
                        r1.progressAnimation = null;
                    }
                    r1.isLoading = true;
                    r1.controlsView.setProgress(0);
                    r1.currentYoutubeId = youtubeId2;
                    youtubeId2 = null;
                    if (mp4File == null) {
                        r1.initied = true;
                        r1.playVideoUrl = mp4File;
                        r1.playVideoType = "other";
                        if (r1.isAutoplay) {
                            preparePlayer();
                        }
                        showProgress(false, false);
                        r1.controlsView.show(true, true);
                    } else {
                        if (youtubeId2 == null) {
                            task = new YoutubeVideoTask(youtubeId2);
                            task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new Void[]{null, null, null});
                            r1.currentTask = task;
                        } else if (vimeoId == null) {
                            task2 = new VimeoVideoTask(vimeoId);
                            task2.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new Void[]{null, null, null});
                            r1.currentTask = task2;
                        } else if (coubId == null) {
                            task3 = new CoubVideoTask(coubId);
                            task3.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new Void[]{null, null, null});
                            r1.currentTask = task3;
                            r1.isStream = true;
                        } else if (aparatId == null) {
                            task4 = new AparatVideoTask(aparatId);
                            task4.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new Void[]{null, null, null});
                            r1.currentTask = task4;
                        } else if (twitchClipId == null) {
                            task5 = new TwitchClipVideoTask(str2, twitchClipId);
                            task5.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new Void[]{null, null, null});
                            r1.currentTask = task5;
                        } else if (twitchStreamId != null) {
                            task6 = new TwitchStreamVideoTask(str2, twitchStreamId);
                            executor = AsyncTask.THREAD_POOL_EXECUTOR;
                            voidArr = new Void[3];
                            z = true;
                            voidArr[1] = null;
                            voidArr[2] = null;
                            task6.executeOnExecutor(executor, voidArr);
                            r1.currentTask = task6;
                            r1.isStream = true;
                            r1.controlsView.show(false, false);
                            showProgress(z, false);
                        }
                        z = true;
                        r1.controlsView.show(false, false);
                        showProgress(z, false);
                    }
                    if (twitchStreamId != null) {
                        r1.controlsView.setVisibility(0);
                        return true;
                    }
                    r1.controlsView.setVisibility(8);
                    return false;
                }
            }
            str = null;
            matcher2 = youtubeIdRegex.matcher(str2);
            t = null;
            if (matcher2.find()) {
                t = matcher2.group(1);
            }
            if (t != null) {
                str = t;
            }
            youtubeId2 = str;
            if (youtubeId2 == null) {
                matcher = vimeoIdRegex.matcher(str2);
                id = null;
                if (matcher.find()) {
                    id = matcher.group(3);
                }
                if (id != null) {
                    vimeoId = id;
                }
            }
            if (vimeoId == null) {
                matcher = aparatIdRegex.matcher(str2);
                id = null;
                if (matcher.find()) {
                    id = matcher.group(1);
                }
                if (id != null) {
                    aparatId = id;
                }
            }
            if (aparatId == null) {
                matcher = twitchClipIdRegex.matcher(str2);
                id = null;
                if (matcher.find()) {
                    id = matcher.group(1);
                }
                if (id != null) {
                    twitchClipId = id;
                }
            }
            if (twitchClipId == null) {
                matcher = twitchStreamIdRegex.matcher(str2);
                id = null;
                if (matcher.find()) {
                    id = matcher.group(1);
                }
                if (id != null) {
                    twitchStreamId = id;
                }
            }
            if (twitchStreamId == null) {
                matcher = coubIdRegex.matcher(str2);
                id = null;
                if (matcher.find()) {
                    id = matcher.group(1);
                }
                if (id != null) {
                    coubId = id;
                }
            }
        }
        r1.initied = false;
        r1.isCompleted = false;
        r1.isAutoplay = autoplay;
        r1.playVideoUrl = null;
        r1.playAudioUrl = null;
        destroy();
        r1.firstFrameRendered = false;
        r1.currentAlpha = 1.0f;
        if (r1.currentTask != null) {
            r1.currentTask.cancel(true);
            r1.currentTask = null;
        }
        updateFullscreenButton();
        updateShareButton();
        updateInlineButton();
        updatePlayButton();
        if (photo == null) {
            photoSize = FileLoader.getClosestPhotoSizeWithSize(photo.sizes, 80, true);
            if (photoSize != null) {
                if (photo == null) {
                }
                if (photo == null) {
                }
                r1.controlsView.imageReceiver.setImage(null, null, photo == null ? null : photoSize.location, photo == null ? null : "80_80_b", 0, null, 1);
                r1.drawImage = true;
            }
        } else {
            r1.drawImage = false;
        }
        if (r1.progressAnimation != null) {
            r1.progressAnimation.cancel();
            r1.progressAnimation = null;
        }
        r1.isLoading = true;
        r1.controlsView.setProgress(0);
        if (!(youtubeId2 == null || BuildVars.DEBUG_PRIVATE_VERSION)) {
            r1.currentYoutubeId = youtubeId2;
            youtubeId2 = null;
        }
        if (mp4File == null) {
            r1.initied = true;
            r1.playVideoUrl = mp4File;
            r1.playVideoType = "other";
            if (r1.isAutoplay) {
                preparePlayer();
            }
            showProgress(false, false);
            r1.controlsView.show(true, true);
        } else {
            if (youtubeId2 == null) {
                task = new YoutubeVideoTask(youtubeId2);
                task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new Void[]{null, null, null});
                r1.currentTask = task;
            } else if (vimeoId == null) {
                task2 = new VimeoVideoTask(vimeoId);
                task2.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new Void[]{null, null, null});
                r1.currentTask = task2;
            } else if (coubId == null) {
                task3 = new CoubVideoTask(coubId);
                task3.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new Void[]{null, null, null});
                r1.currentTask = task3;
                r1.isStream = true;
            } else if (aparatId == null) {
                task4 = new AparatVideoTask(aparatId);
                task4.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new Void[]{null, null, null});
                r1.currentTask = task4;
            } else if (twitchClipId == null) {
                task5 = new TwitchClipVideoTask(str2, twitchClipId);
                task5.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new Void[]{null, null, null});
                r1.currentTask = task5;
            } else if (twitchStreamId != null) {
                task6 = new TwitchStreamVideoTask(str2, twitchStreamId);
                executor = AsyncTask.THREAD_POOL_EXECUTOR;
                voidArr = new Void[3];
                z = true;
                voidArr[1] = null;
                voidArr[2] = null;
                task6.executeOnExecutor(executor, voidArr);
                r1.currentTask = task6;
                r1.isStream = true;
                r1.controlsView.show(false, false);
                showProgress(z, false);
            }
            z = true;
            r1.controlsView.show(false, false);
            showProgress(z, false);
        }
        if (youtubeId2 == null && vimeoId == null && coubId == null && aparatId == null && mp4File == null && twitchClipId == null) {
            if (twitchStreamId != null) {
                r1.controlsView.setVisibility(8);
                return false;
            }
        }
        r1.controlsView.setVisibility(0);
        return true;
    }

    public View getAspectRatioView() {
        return this.aspectRatioFrameLayout;
    }

    public TextureView getTextureView() {
        return this.textureView;
    }

    public ImageView getTextureImageView() {
        return this.textureImageView;
    }

    public View getControlsView() {
        return this.controlsView;
    }

    public void destroy() {
        this.videoPlayer.releasePlayer();
        if (this.currentTask != null) {
            this.currentTask.cancel(true);
            this.currentTask = null;
        }
        this.webView.stopLoading();
    }

    private void showProgress(boolean show, boolean animated) {
        float f = 0.0f;
        if (animated) {
            if (this.progressAnimation != null) {
                this.progressAnimation.cancel();
            }
            this.progressAnimation = new AnimatorSet();
            AnimatorSet animatorSet = this.progressAnimation;
            Animator[] animatorArr = new Animator[1];
            RadialProgressView radialProgressView = this.progressView;
            String str = "alpha";
            float[] fArr = new float[1];
            if (show) {
                f = 1.0f;
            }
            fArr[0] = f;
            animatorArr[0] = ObjectAnimator.ofFloat(radialProgressView, str, fArr);
            animatorSet.playTogether(animatorArr);
            this.progressAnimation.setDuration(150);
            this.progressAnimation.addListener(new AnimatorListenerAdapter() {
                public void onAnimationEnd(Animator animator) {
                    WebPlayerView.this.progressAnimation = null;
                }
            });
            this.progressAnimation.start();
            return;
        }
        RadialProgressView radialProgressView2 = this.progressView;
        if (show) {
            f = 1.0f;
        }
        radialProgressView2.setAlpha(f);
    }
}
