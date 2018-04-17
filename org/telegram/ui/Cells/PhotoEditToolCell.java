package org.telegram.ui.Cells;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.text.TextUtils.TruncateAt;
import android.view.View.MeasureSpec;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.TextView;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.ui.Components.PhotoEditorSeekBar;
import org.telegram.ui.Components.PhotoEditorSeekBar.PhotoEditorSeekBarDelegate;

public class PhotoEditToolCell extends FrameLayout {
    private Runnable hideValueRunnable = new Runnable() {
        public void run() {
            PhotoEditToolCell.this.valueTextView.setTag(null);
            PhotoEditToolCell.this.valueAnimation = new AnimatorSet();
            AnimatorSet access$100 = PhotoEditToolCell.this.valueAnimation;
            r1 = new Animator[2];
            r1[0] = ObjectAnimator.ofFloat(PhotoEditToolCell.this.valueTextView, "alpha", new float[]{0.0f});
            r1[1] = ObjectAnimator.ofFloat(PhotoEditToolCell.this.nameTextView, "alpha", new float[]{1.0f});
            access$100.playTogether(r1);
            PhotoEditToolCell.this.valueAnimation.setDuration(180);
            PhotoEditToolCell.this.valueAnimation.setInterpolator(new DecelerateInterpolator());
            PhotoEditToolCell.this.valueAnimation.addListener(new AnimatorListenerAdapter() {
                public void onAnimationEnd(Animator animation) {
                    if (animation.equals(PhotoEditToolCell.this.valueAnimation)) {
                        PhotoEditToolCell.this.valueAnimation = null;
                    }
                }
            });
            PhotoEditToolCell.this.valueAnimation.start();
        }
    };
    private TextView nameTextView;
    private PhotoEditorSeekBar seekBar;
    private AnimatorSet valueAnimation;
    private TextView valueTextView;

    public PhotoEditToolCell(Context context) {
        super(context);
        this.nameTextView = new TextView(context);
        this.nameTextView.setGravity(5);
        this.nameTextView.setTextColor(-1);
        this.nameTextView.setTextSize(1, 12.0f);
        this.nameTextView.setMaxLines(1);
        this.nameTextView.setSingleLine(true);
        this.nameTextView.setEllipsize(TruncateAt.END);
        addView(this.nameTextView, LayoutHelper.createFrame(80, -2.0f, 19, 0.0f, 0.0f, 0.0f, 0.0f));
        this.valueTextView = new TextView(context);
        this.valueTextView.setTextColor(-9649153);
        this.valueTextView.setTextSize(1, 12.0f);
        this.valueTextView.setGravity(5);
        this.valueTextView.setSingleLine(true);
        addView(this.valueTextView, LayoutHelper.createFrame(80, -2.0f, 19, 0.0f, 0.0f, 0.0f, 0.0f));
        this.seekBar = new PhotoEditorSeekBar(context);
        addView(this.seekBar, LayoutHelper.createFrame(-1, 40.0f, 19, 96.0f, 0.0f, 24.0f, 0.0f));
    }

    public void setSeekBarDelegate(final PhotoEditorSeekBarDelegate photoEditorSeekBarDelegate) {
        this.seekBar.setDelegate(new PhotoEditorSeekBarDelegate() {
            public void onProgressChanged(int i, int progress) {
                photoEditorSeekBarDelegate.onProgressChanged(i, progress);
                TextView access$000;
                StringBuilder stringBuilder;
                if (progress > 0) {
                    access$000 = PhotoEditToolCell.this.valueTextView;
                    stringBuilder = new StringBuilder();
                    stringBuilder.append("+");
                    stringBuilder.append(progress);
                    access$000.setText(stringBuilder.toString());
                } else {
                    access$000 = PhotoEditToolCell.this.valueTextView;
                    stringBuilder = new StringBuilder();
                    stringBuilder.append(TtmlNode.ANONYMOUS_REGION_ID);
                    stringBuilder.append(progress);
                    access$000.setText(stringBuilder.toString());
                }
                if (PhotoEditToolCell.this.valueTextView.getTag() == null) {
                    if (PhotoEditToolCell.this.valueAnimation != null) {
                        PhotoEditToolCell.this.valueAnimation.cancel();
                    }
                    PhotoEditToolCell.this.valueTextView.setTag(Integer.valueOf(1));
                    PhotoEditToolCell.this.valueAnimation = new AnimatorSet();
                    AnimatorSet access$100 = PhotoEditToolCell.this.valueAnimation;
                    r2 = new Animator[2];
                    r2[0] = ObjectAnimator.ofFloat(PhotoEditToolCell.this.valueTextView, "alpha", new float[]{1.0f});
                    r2[1] = ObjectAnimator.ofFloat(PhotoEditToolCell.this.nameTextView, "alpha", new float[]{0.0f});
                    access$100.playTogether(r2);
                    PhotoEditToolCell.this.valueAnimation.setDuration(180);
                    PhotoEditToolCell.this.valueAnimation.setInterpolator(new DecelerateInterpolator());
                    PhotoEditToolCell.this.valueAnimation.addListener(new AnimatorListenerAdapter() {
                        public void onAnimationEnd(Animator animation) {
                            AndroidUtilities.runOnUIThread(PhotoEditToolCell.this.hideValueRunnable, 1000);
                        }
                    });
                    PhotoEditToolCell.this.valueAnimation.start();
                    return;
                }
                AndroidUtilities.cancelRunOnUIThread(PhotoEditToolCell.this.hideValueRunnable);
                AndroidUtilities.runOnUIThread(PhotoEditToolCell.this.hideValueRunnable, 1000);
            }
        });
    }

    public void setTag(Object tag) {
        super.setTag(tag);
        this.seekBar.setTag(tag);
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(MeasureSpec.makeMeasureSpec(MeasureSpec.getSize(widthMeasureSpec), 1073741824), MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(40.0f), 1073741824));
    }

    public void setIconAndTextAndValue(String text, float value, int min, int max) {
        if (this.valueAnimation != null) {
            this.valueAnimation.cancel();
            this.valueAnimation = null;
        }
        AndroidUtilities.cancelRunOnUIThread(this.hideValueRunnable);
        this.valueTextView.setTag(null);
        TextView textView = this.nameTextView;
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(text.substring(0, 1).toUpperCase());
        stringBuilder.append(text.substring(1).toLowerCase());
        textView.setText(stringBuilder.toString());
        TextView textView2;
        StringBuilder stringBuilder2;
        if (value > 0.0f) {
            textView2 = this.valueTextView;
            stringBuilder2 = new StringBuilder();
            stringBuilder2.append("+");
            stringBuilder2.append((int) value);
            textView2.setText(stringBuilder2.toString());
        } else {
            textView2 = this.valueTextView;
            stringBuilder2 = new StringBuilder();
            stringBuilder2.append(TtmlNode.ANONYMOUS_REGION_ID);
            stringBuilder2.append((int) value);
            textView2.setText(stringBuilder2.toString());
        }
        this.valueTextView.setAlpha(0.0f);
        this.nameTextView.setAlpha(1.0f);
        this.seekBar.setMinMax(min, max);
        this.seekBar.setProgress((int) value, false);
    }
}
