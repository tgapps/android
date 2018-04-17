package org.telegram.ui.Cells;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.view.View.MeasureSpec;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.beta.R;
import org.telegram.ui.Components.BackupImageView;
import org.telegram.ui.Components.CheckBox;
import org.telegram.ui.Components.LayoutHelper;

public class PhotoPickerPhotoCell extends FrameLayout {
    private AnimatorSet animator;
    private AnimatorSet animatorSet;
    public CheckBox checkBox;
    public FrameLayout checkFrame;
    public int itemWidth;
    public BackupImageView photoImage;
    public FrameLayout videoInfoContainer;
    public TextView videoTextView;
    private boolean zoomOnSelect;

    public PhotoPickerPhotoCell(Context context, boolean zoom) {
        super(context);
        this.zoomOnSelect = zoom;
        this.photoImage = new BackupImageView(context);
        addView(this.photoImage, LayoutHelper.createFrame(-1, -1.0f));
        this.checkFrame = new FrameLayout(context);
        addView(this.checkFrame, LayoutHelper.createFrame(42, 42, 53));
        this.videoInfoContainer = new FrameLayout(context);
        this.videoInfoContainer.setBackgroundResource(R.drawable.phototime);
        this.videoInfoContainer.setPadding(AndroidUtilities.dp(3.0f), 0, AndroidUtilities.dp(3.0f), 0);
        addView(this.videoInfoContainer, LayoutHelper.createFrame(-1, 16, 83));
        ImageView imageView1 = new ImageView(context);
        imageView1.setImageResource(R.drawable.ic_video);
        this.videoInfoContainer.addView(imageView1, LayoutHelper.createFrame(-2, -2, 19));
        this.videoTextView = new TextView(context);
        this.videoTextView.setTextColor(-1);
        this.videoTextView.setTextSize(1, 12.0f);
        this.videoInfoContainer.addView(this.videoTextView, LayoutHelper.createFrame(-2, -2.0f, 19, 18.0f, -0.7f, 0.0f, 0.0f));
        this.checkBox = new CheckBox(context, R.drawable.checkbig);
        this.checkBox.setSize(zoom ? 30 : 26);
        this.checkBox.setCheckOffset(AndroidUtilities.dp(1.0f));
        this.checkBox.setDrawBackground(true);
        this.checkBox.setColor(-10043398, -1);
        addView(this.checkBox, LayoutHelper.createFrame(zoom ? 30 : 26, zoom ? 30.0f : 26.0f, 53, 0.0f, 4.0f, 4.0f, 0.0f));
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(MeasureSpec.makeMeasureSpec(this.itemWidth, 1073741824), MeasureSpec.makeMeasureSpec(this.itemWidth, 1073741824));
    }

    public void showCheck(boolean show) {
        if (this.animatorSet != null) {
            this.animatorSet.cancel();
            this.animatorSet = null;
        }
        this.animatorSet = new AnimatorSet();
        this.animatorSet.setInterpolator(new DecelerateInterpolator());
        this.animatorSet.setDuration(180);
        AnimatorSet animatorSet = this.animatorSet;
        Animator[] animatorArr = new Animator[2];
        FrameLayout frameLayout = this.videoInfoContainer;
        String str = "alpha";
        float[] fArr = new float[1];
        float f = 0.0f;
        fArr[0] = show ? 1.0f : 0.0f;
        animatorArr[0] = ObjectAnimator.ofFloat(frameLayout, str, fArr);
        CheckBox checkBox = this.checkBox;
        str = "alpha";
        fArr = new float[1];
        if (show) {
            f = 1.0f;
        }
        fArr[0] = f;
        animatorArr[1] = ObjectAnimator.ofFloat(checkBox, str, fArr);
        animatorSet.playTogether(animatorArr);
        this.animatorSet.addListener(new AnimatorListenerAdapter() {
            public void onAnimationEnd(Animator animation) {
                if (animation.equals(PhotoPickerPhotoCell.this.animatorSet)) {
                    PhotoPickerPhotoCell.this.animatorSet = null;
                }
            }
        });
        this.animatorSet.start();
    }

    public void setNum(int num) {
        this.checkBox.setNum(num);
    }

    public void setChecked(int num, final boolean checked, boolean animated) {
        this.checkBox.setChecked(num, checked, animated);
        if (this.animator != null) {
            this.animator.cancel();
            this.animator = null;
        }
        if (this.zoomOnSelect) {
            int i = -16119286;
            float f = 1.0f;
            if (animated) {
                if (checked) {
                    setBackgroundColor(-16119286);
                }
                this.animator = new AnimatorSet();
                AnimatorSet animatorSet = this.animator;
                Animator[] animatorArr = new Animator[2];
                BackupImageView backupImageView = this.photoImage;
                String str = "scaleX";
                float[] fArr = new float[1];
                fArr[0] = checked ? 0.85f : 1.0f;
                animatorArr[0] = ObjectAnimator.ofFloat(backupImageView, str, fArr);
                backupImageView = this.photoImage;
                str = "scaleY";
                fArr = new float[1];
                if (checked) {
                    f = 0.85f;
                }
                fArr[0] = f;
                animatorArr[1] = ObjectAnimator.ofFloat(backupImageView, str, fArr);
                animatorSet.playTogether(animatorArr);
                this.animator.setDuration(200);
                this.animator.addListener(new AnimatorListenerAdapter() {
                    public void onAnimationEnd(Animator animation) {
                        if (PhotoPickerPhotoCell.this.animator != null && PhotoPickerPhotoCell.this.animator.equals(animation)) {
                            PhotoPickerPhotoCell.this.animator = null;
                            if (!checked) {
                                PhotoPickerPhotoCell.this.setBackgroundColor(0);
                            }
                        }
                    }

                    public void onAnimationCancel(Animator animation) {
                        if (PhotoPickerPhotoCell.this.animator != null && PhotoPickerPhotoCell.this.animator.equals(animation)) {
                            PhotoPickerPhotoCell.this.animator = null;
                        }
                    }
                });
                this.animator.start();
                return;
            }
            if (!checked) {
                i = 0;
            }
            setBackgroundColor(i);
            this.photoImage.setScaleX(checked ? 0.85f : 1.0f);
            BackupImageView backupImageView2 = this.photoImage;
            if (checked) {
                f = 0.85f;
            }
            backupImageView2.setScaleY(f);
        }
    }
}
