package org.telegram.ui.Components;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build.VERSION;
import android.os.Vibrator;
import android.support.v4.os.CancellationSignal;
import android.text.InputFilter;
import android.text.method.PasswordTransformationMethod;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.View.OnTouchListener;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import java.util.ArrayList;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.ApplicationLoader;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.NotificationCenter;
import org.telegram.messenger.SharedConfig;
import org.telegram.messenger.beta.R;
import org.telegram.messenger.exoplayer2.extractor.ts.TsExtractor;
import org.telegram.messenger.support.fingerprint.FingerprintManagerCompat;
import org.telegram.messenger.support.fingerprint.FingerprintManagerCompat.AuthenticationCallback;
import org.telegram.messenger.support.fingerprint.FingerprintManagerCompat.AuthenticationResult;
import org.telegram.ui.ActionBar.AlertDialog;
import org.telegram.ui.ActionBar.AlertDialog.Builder;
import org.telegram.ui.ActionBar.Theme;

public class PasscodeView extends FrameLayout {
    private static final int id_fingerprint_imageview = 1001;
    private static final int id_fingerprint_textview = 1000;
    private Drawable backgroundDrawable;
    private FrameLayout backgroundFrameLayout;
    private CancellationSignal cancellationSignal;
    private ImageView checkImage;
    private PasscodeViewDelegate delegate;
    private ImageView eraseView;
    private AlertDialog fingerprintDialog;
    private ImageView fingerprintImageView;
    private TextView fingerprintStatusTextView;
    private int keyboardHeight;
    private ArrayList<TextView> lettersTextViews;
    private ArrayList<FrameLayout> numberFrameLayouts;
    private ArrayList<TextView> numberTextViews;
    private FrameLayout numbersFrameLayout;
    private TextView passcodeTextView;
    private EditTextBoldCursor passwordEditText;
    private AnimatingTextView passwordEditText2;
    private FrameLayout passwordFrameLayout;
    private Rect rect;
    private boolean selfCancelled;

    private class AnimatingTextView extends FrameLayout {
        private String DOT = "•";
        private ArrayList<TextView> characterTextViews = new ArrayList(4);
        private AnimatorSet currentAnimation;
        private Runnable dotRunnable;
        private ArrayList<TextView> dotTextViews = new ArrayList(4);
        private StringBuilder stringBuilder = new StringBuilder(4);

        private void eraseAllCharacters(boolean r1) {
            /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.DecodeException: Load method exception in method: org.telegram.ui.Components.PasscodeView.AnimatingTextView.eraseAllCharacters(boolean):void
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
            r0 = r9.stringBuilder;
            r0 = r0.length();
            if (r0 != 0) goto L_0x0009;
        L_0x0008:
            return;
        L_0x0009:
            r0 = r9.dotRunnable;
            r1 = 0;
            if (r0 == 0) goto L_0x0015;
        L_0x000e:
            r0 = r9.dotRunnable;
            org.telegram.messenger.AndroidUtilities.cancelRunOnUIThread(r0);
            r9.dotRunnable = r1;
        L_0x0015:
            r0 = r9.currentAnimation;
            if (r0 == 0) goto L_0x0020;
        L_0x0019:
            r0 = r9.currentAnimation;
            r0.cancel();
            r9.currentAnimation = r1;
        L_0x0020:
            r0 = r9.stringBuilder;
            r1 = r9.stringBuilder;
            r1 = r1.length();
            r2 = 0;
            r0.delete(r2, r1);
            r0 = 4;
            r1 = 0;
            if (r10 == 0) goto L_0x00ce;
        L_0x0030:
            r3 = new java.util.ArrayList;
            r3.<init>();
            r4 = r2;
            if (r4 >= r0) goto L_0x00ab;
            r5 = r9.characterTextViews;
            r5 = r5.get(r4);
            r5 = (android.widget.TextView) r5;
            r6 = r5.getAlpha();
            r6 = (r6 > r1 ? 1 : (r6 == r1 ? 0 : -1));
            r7 = 1;
            if (r6 == 0) goto L_0x0070;
            r6 = "scaleX";
            r8 = new float[r7];
            r8[r2] = r1;
            r6 = android.animation.ObjectAnimator.ofFloat(r5, r6, r8);
            r3.add(r6);
            r6 = "scaleY";
            r8 = new float[r7];
            r8[r2] = r1;
            r6 = android.animation.ObjectAnimator.ofFloat(r5, r6, r8);
            r3.add(r6);
            r6 = "alpha";
            r8 = new float[r7];
            r8[r2] = r1;
            r6 = android.animation.ObjectAnimator.ofFloat(r5, r6, r8);
            r3.add(r6);
            r6 = r9.dotTextViews;
            r6 = r6.get(r4);
            r5 = r6;
            r5 = (android.widget.TextView) r5;
            r6 = r5.getAlpha();
            r6 = (r6 > r1 ? 1 : (r6 == r1 ? 0 : -1));
            if (r6 == 0) goto L_0x00a8;
            r6 = "scaleX";
            r8 = new float[r7];
            r8[r2] = r1;
            r6 = android.animation.ObjectAnimator.ofFloat(r5, r6, r8);
            r3.add(r6);
            r6 = "scaleY";
            r8 = new float[r7];
            r8[r2] = r1;
            r6 = android.animation.ObjectAnimator.ofFloat(r5, r6, r8);
            r3.add(r6);
            r6 = "alpha";
            r7 = new float[r7];
            r7[r2] = r1;
            r6 = android.animation.ObjectAnimator.ofFloat(r5, r6, r7);
            r3.add(r6);
            r4 = r4 + 1;
            goto L_0x0036;
            r0 = new android.animation.AnimatorSet;
            r0.<init>();
            r9.currentAnimation = r0;
            r0 = r9.currentAnimation;
            r1 = 150; // 0x96 float:2.1E-43 double:7.4E-322;
            r0.setDuration(r1);
            r0 = r9.currentAnimation;
            r0.playTogether(r3);
            r0 = r9.currentAnimation;
            r1 = new org.telegram.ui.Components.PasscodeView$AnimatingTextView$4;
            r1.<init>();
            r0.addListener(r1);
            r0 = r9.currentAnimation;
            r0.start();
            goto L_0x00ea;
            if (r2 >= r0) goto L_0x00ea;
            r3 = r9.characterTextViews;
            r3 = r3.get(r2);
            r3 = (android.widget.TextView) r3;
            r3.setAlpha(r1);
            r3 = r9.dotTextViews;
            r3 = r3.get(r2);
            r3 = (android.widget.TextView) r3;
            r3.setAlpha(r1);
            r2 = r2 + 1;
            goto L_0x00cf;
            return;
            */
            throw new UnsupportedOperationException("Method not decompiled: org.telegram.ui.Components.PasscodeView.AnimatingTextView.eraseAllCharacters(boolean):void");
        }

        public AnimatingTextView(Context context) {
            super(context);
            for (int a = 0; a < 4; a++) {
                TextView textView = new TextView(context);
                textView.setTextColor(-1);
                textView.setTextSize(1, 36.0f);
                textView.setGravity(17);
                textView.setAlpha(0.0f);
                textView.setPivotX((float) AndroidUtilities.dp(25.0f));
                textView.setPivotY((float) AndroidUtilities.dp(25.0f));
                addView(textView);
                LayoutParams layoutParams = (LayoutParams) textView.getLayoutParams();
                layoutParams.width = AndroidUtilities.dp(50.0f);
                layoutParams.height = AndroidUtilities.dp(50.0f);
                layoutParams.gravity = 51;
                textView.setLayoutParams(layoutParams);
                this.characterTextViews.add(textView);
                textView = new TextView(context);
                textView.setTextColor(-1);
                textView.setTextSize(1, 36.0f);
                textView.setGravity(17);
                textView.setAlpha(0.0f);
                textView.setText(this.DOT);
                textView.setPivotX((float) AndroidUtilities.dp(25.0f));
                textView.setPivotY((float) AndroidUtilities.dp(25.0f));
                addView(textView);
                LayoutParams layoutParams2 = (LayoutParams) textView.getLayoutParams();
                layoutParams2.width = AndroidUtilities.dp(50.0f);
                layoutParams2.height = AndroidUtilities.dp(50.0f);
                layoutParams2.gravity = 51;
                textView.setLayoutParams(layoutParams2);
                this.dotTextViews.add(textView);
            }
        }

        private int getXForTextView(int pos) {
            return (((getMeasuredWidth() - (this.stringBuilder.length() * AndroidUtilities.dp(30.0f))) / 2) + (AndroidUtilities.dp(30.0f) * pos)) - AndroidUtilities.dp(10.0f);
        }

        public void appendCharacter(String c) {
            if (this.stringBuilder.length() != 4) {
                try {
                    performHapticFeedback(3);
                } catch (Throwable e) {
                    FileLog.e(e);
                }
                ArrayList<Animator> animators = new ArrayList();
                final int newPos = this.stringBuilder.length();
                this.stringBuilder.append(c);
                TextView textView = (TextView) this.characterTextViews.get(newPos);
                textView.setText(c);
                textView.setTranslationX((float) getXForTextView(newPos));
                animators.add(ObjectAnimator.ofFloat(textView, "scaleX", new float[]{0.0f, 1.0f}));
                animators.add(ObjectAnimator.ofFloat(textView, "scaleY", new float[]{0.0f, 1.0f}));
                animators.add(ObjectAnimator.ofFloat(textView, "alpha", new float[]{0.0f, 1.0f}));
                animators.add(ObjectAnimator.ofFloat(textView, "translationY", new float[]{(float) AndroidUtilities.dp(20.0f), 0.0f}));
                textView = (TextView) this.dotTextViews.get(newPos);
                textView.setTranslationX((float) getXForTextView(newPos));
                textView.setAlpha(0.0f);
                animators.add(ObjectAnimator.ofFloat(textView, "scaleX", new float[]{0.0f, 1.0f}));
                animators.add(ObjectAnimator.ofFloat(textView, "scaleY", new float[]{0.0f, 1.0f}));
                animators.add(ObjectAnimator.ofFloat(textView, "translationY", new float[]{(float) AndroidUtilities.dp(20.0f), 0.0f}));
                for (int a = newPos + 1; a < 4; a++) {
                    textView = (TextView) this.characterTextViews.get(a);
                    if (textView.getAlpha() != 0.0f) {
                        animators.add(ObjectAnimator.ofFloat(textView, "scaleX", new float[]{0.0f}));
                        animators.add(ObjectAnimator.ofFloat(textView, "scaleY", new float[]{0.0f}));
                        animators.add(ObjectAnimator.ofFloat(textView, "alpha", new float[]{0.0f}));
                    }
                    textView = (TextView) this.dotTextViews.get(a);
                    if (textView.getAlpha() != 0.0f) {
                        animators.add(ObjectAnimator.ofFloat(textView, "scaleX", new float[]{0.0f}));
                        animators.add(ObjectAnimator.ofFloat(textView, "scaleY", new float[]{0.0f}));
                        animators.add(ObjectAnimator.ofFloat(textView, "alpha", new float[]{0.0f}));
                    }
                }
                if (this.dotRunnable != null) {
                    AndroidUtilities.cancelRunOnUIThread(this.dotRunnable);
                }
                this.dotRunnable = new Runnable() {
                    public void run() {
                        if (AnimatingTextView.this.dotRunnable == this) {
                            ArrayList<Animator> animators = new ArrayList();
                            TextView textView = (TextView) AnimatingTextView.this.characterTextViews.get(newPos);
                            animators.add(ObjectAnimator.ofFloat(textView, "scaleX", new float[]{0.0f}));
                            animators.add(ObjectAnimator.ofFloat(textView, "scaleY", new float[]{0.0f}));
                            animators.add(ObjectAnimator.ofFloat(textView, "alpha", new float[]{0.0f}));
                            textView = (TextView) AnimatingTextView.this.dotTextViews.get(newPos);
                            animators.add(ObjectAnimator.ofFloat(textView, "scaleX", new float[]{1.0f}));
                            animators.add(ObjectAnimator.ofFloat(textView, "scaleY", new float[]{1.0f}));
                            animators.add(ObjectAnimator.ofFloat(textView, "alpha", new float[]{1.0f}));
                            AnimatingTextView.this.currentAnimation = new AnimatorSet();
                            AnimatingTextView.this.currentAnimation.setDuration(150);
                            AnimatingTextView.this.currentAnimation.playTogether(animators);
                            AnimatingTextView.this.currentAnimation.addListener(new AnimatorListenerAdapter() {
                                public void onAnimationEnd(Animator animation) {
                                    if (AnimatingTextView.this.currentAnimation != null && AnimatingTextView.this.currentAnimation.equals(animation)) {
                                        AnimatingTextView.this.currentAnimation = null;
                                    }
                                }
                            });
                            AnimatingTextView.this.currentAnimation.start();
                        }
                    }
                };
                AndroidUtilities.runOnUIThread(this.dotRunnable, 1500);
                for (int a2 = 0; a2 < newPos; a2++) {
                    textView = (TextView) this.characterTextViews.get(a2);
                    animators.add(ObjectAnimator.ofFloat(textView, "translationX", new float[]{(float) getXForTextView(a2)}));
                    animators.add(ObjectAnimator.ofFloat(textView, "scaleX", new float[]{0.0f}));
                    animators.add(ObjectAnimator.ofFloat(textView, "scaleY", new float[]{0.0f}));
                    animators.add(ObjectAnimator.ofFloat(textView, "alpha", new float[]{0.0f}));
                    animators.add(ObjectAnimator.ofFloat(textView, "translationY", new float[]{0.0f}));
                    textView = (TextView) this.dotTextViews.get(a2);
                    animators.add(ObjectAnimator.ofFloat(textView, "translationX", new float[]{(float) getXForTextView(a2)}));
                    animators.add(ObjectAnimator.ofFloat(textView, "scaleX", new float[]{1.0f}));
                    animators.add(ObjectAnimator.ofFloat(textView, "scaleY", new float[]{1.0f}));
                    animators.add(ObjectAnimator.ofFloat(textView, "alpha", new float[]{1.0f}));
                    animators.add(ObjectAnimator.ofFloat(textView, "translationY", new float[]{0.0f}));
                }
                if (this.currentAnimation != null) {
                    this.currentAnimation.cancel();
                }
                this.currentAnimation = new AnimatorSet();
                this.currentAnimation.setDuration(150);
                this.currentAnimation.playTogether(animators);
                this.currentAnimation.addListener(new AnimatorListenerAdapter() {
                    public void onAnimationEnd(Animator animation) {
                        if (AnimatingTextView.this.currentAnimation != null && AnimatingTextView.this.currentAnimation.equals(animation)) {
                            AnimatingTextView.this.currentAnimation = null;
                        }
                    }
                });
                this.currentAnimation.start();
            }
        }

        public String getString() {
            return this.stringBuilder.toString();
        }

        public int lenght() {
            return this.stringBuilder.length();
        }

        public void eraseLastCharacter() {
            if (this.stringBuilder.length() != 0) {
                int a;
                try {
                    performHapticFeedback(3);
                } catch (Throwable e) {
                    FileLog.e(e);
                }
                ArrayList<Animator> animators = new ArrayList();
                int deletingPos = this.stringBuilder.length() - 1;
                if (deletingPos != 0) {
                    this.stringBuilder.deleteCharAt(deletingPos);
                }
                for (a = deletingPos; a < 4; a++) {
                    TextView textView = (TextView) this.characterTextViews.get(a);
                    if (textView.getAlpha() != 0.0f) {
                        animators.add(ObjectAnimator.ofFloat(textView, "scaleX", new float[]{0.0f}));
                        animators.add(ObjectAnimator.ofFloat(textView, "scaleY", new float[]{0.0f}));
                        animators.add(ObjectAnimator.ofFloat(textView, "alpha", new float[]{0.0f}));
                        animators.add(ObjectAnimator.ofFloat(textView, "translationY", new float[]{0.0f}));
                        animators.add(ObjectAnimator.ofFloat(textView, "translationX", new float[]{(float) getXForTextView(a)}));
                    }
                    textView = (TextView) this.dotTextViews.get(a);
                    if (textView.getAlpha() != 0.0f) {
                        animators.add(ObjectAnimator.ofFloat(textView, "scaleX", new float[]{0.0f}));
                        animators.add(ObjectAnimator.ofFloat(textView, "scaleY", new float[]{0.0f}));
                        animators.add(ObjectAnimator.ofFloat(textView, "alpha", new float[]{0.0f}));
                        animators.add(ObjectAnimator.ofFloat(textView, "translationY", new float[]{0.0f}));
                        animators.add(ObjectAnimator.ofFloat(textView, "translationX", new float[]{(float) getXForTextView(a)}));
                    }
                }
                if (deletingPos == 0) {
                    this.stringBuilder.deleteCharAt(deletingPos);
                }
                for (a = 0; a < deletingPos; a++) {
                    animators.add(ObjectAnimator.ofFloat((TextView) this.characterTextViews.get(a), "translationX", new float[]{(float) getXForTextView(a)}));
                    animators.add(ObjectAnimator.ofFloat((TextView) this.dotTextViews.get(a), "translationX", new float[]{(float) getXForTextView(a)}));
                }
                if (this.dotRunnable != null) {
                    AndroidUtilities.cancelRunOnUIThread(this.dotRunnable);
                    this.dotRunnable = null;
                }
                if (this.currentAnimation != null) {
                    this.currentAnimation.cancel();
                }
                this.currentAnimation = new AnimatorSet();
                this.currentAnimation.setDuration(150);
                this.currentAnimation.playTogether(animators);
                this.currentAnimation.addListener(new AnimatorListenerAdapter() {
                    public void onAnimationEnd(Animator animation) {
                        if (AnimatingTextView.this.currentAnimation != null && AnimatingTextView.this.currentAnimation.equals(animation)) {
                            AnimatingTextView.this.currentAnimation = null;
                        }
                    }
                });
                this.currentAnimation.start();
            }
        }

        protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
            if (this.dotRunnable != null) {
                AndroidUtilities.cancelRunOnUIThread(this.dotRunnable);
                this.dotRunnable = null;
            }
            if (this.currentAnimation != null) {
                this.currentAnimation.cancel();
                this.currentAnimation = null;
            }
            for (int a = 0; a < 4; a++) {
                if (a < this.stringBuilder.length()) {
                    TextView textView = (TextView) this.characterTextViews.get(a);
                    textView.setAlpha(0.0f);
                    textView.setScaleX(1.0f);
                    textView.setScaleY(1.0f);
                    textView.setTranslationY(0.0f);
                    textView.setTranslationX((float) getXForTextView(a));
                    textView = (TextView) this.dotTextViews.get(a);
                    textView.setAlpha(1.0f);
                    textView.setScaleX(1.0f);
                    textView.setScaleY(1.0f);
                    textView.setTranslationY(0.0f);
                    textView.setTranslationX((float) getXForTextView(a));
                } else {
                    ((TextView) this.characterTextViews.get(a)).setAlpha(0.0f);
                    ((TextView) this.dotTextViews.get(a)).setAlpha(0.0f);
                }
            }
            super.onLayout(changed, left, top, right, bottom);
        }
    }

    public interface PasscodeViewDelegate {
        void didAcceptedPassword();
    }

    public PasscodeView(android.content.Context r1) {
        /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.DecodeException: Load method exception in method: org.telegram.ui.Components.PasscodeView.<init>(android.content.Context):void
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
        r0 = r17;
        r1 = r18;
        r17.<init>(r18);
        r2 = 0;
        r0.keyboardHeight = r2;
        r3 = new android.graphics.Rect;
        r3.<init>();
        r0.rect = r3;
        r0.setWillNotDraw(r2);
        r3 = 8;
        r0.setVisibility(r3);
        r3 = new android.widget.FrameLayout;
        r3.<init>(r1);
        r0.backgroundFrameLayout = r3;
        r3 = r0.backgroundFrameLayout;
        r0.addView(r3);
        r3 = r0.backgroundFrameLayout;
        r3 = r3.getLayoutParams();
        r3 = (android.widget.FrameLayout.LayoutParams) r3;
        r4 = -1;
        r3.width = r4;
        r3.height = r4;
        r5 = r0.backgroundFrameLayout;
        r5.setLayoutParams(r3);
        r5 = new android.widget.FrameLayout;
        r5.<init>(r1);
        r0.passwordFrameLayout = r5;
        r5 = r0.passwordFrameLayout;
        r0.addView(r5);
        r5 = r0.passwordFrameLayout;
        r5 = r5.getLayoutParams();
        r3 = r5;
        r3 = (android.widget.FrameLayout.LayoutParams) r3;
        r3.width = r4;
        r3.height = r4;
        r5 = 51;
        r3.gravity = r5;
        r6 = r0.passwordFrameLayout;
        r6.setLayoutParams(r3);
        r6 = new android.widget.ImageView;
        r6.<init>(r1);
        r7 = android.widget.ImageView.ScaleType.FIT_XY;
        r6.setScaleType(r7);
        r7 = 2131165573; // 0x7f070185 float:1.7945367E38 double:1.052935695E-314;
        r6.setImageResource(r7);
        r7 = r0.passwordFrameLayout;
        r7.addView(r6);
        r7 = r6.getLayoutParams();
        r3 = r7;
        r3 = (android.widget.FrameLayout.LayoutParams) r3;
        r7 = org.telegram.messenger.AndroidUtilities.density;
        r8 = 1065353216; // 0x3f800000 float:1.0 double:5.263544247E-315;
        r7 = (r7 > r8 ? 1 : (r7 == r8 ? 0 : -1));
        if (r7 >= 0) goto L_0x008c;
    L_0x007d:
        r7 = 1106247680; // 0x41f00000 float:30.0 double:5.465589745E-315;
        r9 = org.telegram.messenger.AndroidUtilities.dp(r7);
        r3.width = r9;
        r7 = org.telegram.messenger.AndroidUtilities.dp(r7);
        r3.height = r7;
        goto L_0x009a;
    L_0x008c:
        r7 = 1109393408; // 0x42200000 float:40.0 double:5.481131706E-315;
        r9 = org.telegram.messenger.AndroidUtilities.dp(r7);
        r3.width = r9;
        r7 = org.telegram.messenger.AndroidUtilities.dp(r7);
        r3.height = r7;
    L_0x009a:
        r7 = 81;
        r3.gravity = r7;
        r9 = 1120403456; // 0x42c80000 float:100.0 double:5.53552857E-315;
        r10 = org.telegram.messenger.AndroidUtilities.dp(r9);
        r3.bottomMargin = r10;
        r6.setLayoutParams(r3);
        r10 = new android.widget.TextView;
        r10.<init>(r1);
        r0.passcodeTextView = r10;
        r10 = r0.passcodeTextView;
        r10.setTextColor(r4);
        r10 = r0.passcodeTextView;
        r11 = 1096810496; // 0x41600000 float:14.0 double:5.41896386E-315;
        r12 = 1;
        r10.setTextSize(r12, r11);
        r10 = r0.passcodeTextView;
        r10.setGravity(r12);
        r10 = r0.passwordFrameLayout;
        r11 = r0.passcodeTextView;
        r10.addView(r11);
        r10 = r0.passcodeTextView;
        r10 = r10.getLayoutParams();
        r3 = r10;
        r3 = (android.widget.FrameLayout.LayoutParams) r3;
        r10 = -2;
        r3.width = r10;
        r3.height = r10;
        r11 = 1115160576; // 0x42780000 float:62.0 double:5.5096253E-315;
        r11 = org.telegram.messenger.AndroidUtilities.dp(r11);
        r3.bottomMargin = r11;
        r3.gravity = r7;
        r11 = r0.passcodeTextView;
        r11.setLayoutParams(r3);
        r11 = new org.telegram.ui.Components.PasscodeView$AnimatingTextView;
        r11.<init>(r1);
        r0.passwordEditText2 = r11;
        r11 = r0.passwordFrameLayout;
        r13 = r0.passwordEditText2;
        r11.addView(r13);
        r11 = r0.passwordEditText2;
        r11 = r11.getLayoutParams();
        r3 = r11;
        r3 = (android.widget.FrameLayout.LayoutParams) r3;
        r3.height = r10;
        r3.width = r4;
        r11 = 1116471296; // 0x428c0000 float:70.0 double:5.51610112E-315;
        r13 = org.telegram.messenger.AndroidUtilities.dp(r11);
        r3.leftMargin = r13;
        r13 = org.telegram.messenger.AndroidUtilities.dp(r11);
        r3.rightMargin = r13;
        r13 = 1086324736; // 0x40c00000 float:6.0 double:5.367157323E-315;
        r13 = org.telegram.messenger.AndroidUtilities.dp(r13);
        r3.bottomMargin = r13;
        r3.gravity = r7;
        r13 = r0.passwordEditText2;
        r13.setLayoutParams(r3);
        r13 = new org.telegram.ui.Components.EditTextBoldCursor;
        r13.<init>(r1);
        r0.passwordEditText = r13;
        r13 = r0.passwordEditText;
        r14 = 1108344832; // 0x42100000 float:36.0 double:5.47595105E-315;
        r13.setTextSize(r12, r14);
        r13 = r0.passwordEditText;
        r13.setTextColor(r4);
        r13 = r0.passwordEditText;
        r13.setMaxLines(r12);
        r13 = r0.passwordEditText;
        r13.setLines(r12);
        r13 = r0.passwordEditText;
        r13.setGravity(r12);
        r13 = r0.passwordEditText;
        r13.setSingleLine(r12);
        r13 = r0.passwordEditText;
        r15 = 6;
        r13.setImeOptions(r15);
        r13 = r0.passwordEditText;
        r15 = android.graphics.Typeface.DEFAULT;
        r13.setTypeface(r15);
        r13 = r0.passwordEditText;
        r15 = 0;
        r13.setBackgroundDrawable(r15);
        r13 = r0.passwordEditText;
        r13.setCursorColor(r4);
        r13 = r0.passwordEditText;
        r15 = 1107296256; // 0x42000000 float:32.0 double:5.4707704E-315;
        r15 = org.telegram.messenger.AndroidUtilities.dp(r15);
        r13.setCursorSize(r15);
        r13 = r0.passwordFrameLayout;
        r15 = r0.passwordEditText;
        r13.addView(r15);
        r13 = r0.passwordEditText;
        r13 = r13.getLayoutParams();
        r3 = r13;
        r3 = (android.widget.FrameLayout.LayoutParams) r3;
        r3.height = r10;
        r3.width = r4;
        r10 = org.telegram.messenger.AndroidUtilities.dp(r11);
        r3.leftMargin = r10;
        r10 = org.telegram.messenger.AndroidUtilities.dp(r11);
        r3.rightMargin = r10;
        r3.gravity = r7;
        r7 = r0.passwordEditText;
        r7.setLayoutParams(r3);
        r7 = r0.passwordEditText;
        r10 = new org.telegram.ui.Components.PasscodeView$1;
        r10.<init>();
        r7.setOnEditorActionListener(r10);
        r7 = r0.passwordEditText;
        r10 = new org.telegram.ui.Components.PasscodeView$2;
        r10.<init>();
        r7.addTextChangedListener(r10);
        r7 = r0.passwordEditText;
        r10 = new org.telegram.ui.Components.PasscodeView$3;
        r10.<init>();
        r7.setCustomSelectionActionModeCallback(r10);
        r7 = new android.widget.ImageView;
        r7.<init>(r1);
        r0.checkImage = r7;
        r7 = r0.checkImage;
        r10 = 2131165571; // 0x7f070183 float:1.7945363E38 double:1.052935694E-314;
        r7.setImageResource(r10);
        r7 = r0.checkImage;
        r10 = android.widget.ImageView.ScaleType.CENTER;
        r7.setScaleType(r10);
        r7 = r0.checkImage;
        r10 = 2131165227; // 0x7f07002b float:1.7944665E38 double:1.0529355243E-314;
        r7.setBackgroundResource(r10);
        r7 = r0.passwordFrameLayout;
        r10 = r0.checkImage;
        r7.addView(r10);
        r7 = r0.checkImage;
        r7 = r7.getLayoutParams();
        r3 = r7;
        r3 = (android.widget.FrameLayout.LayoutParams) r3;
        r7 = 1114636288; // 0x42700000 float:60.0 double:5.507034975E-315;
        r7 = org.telegram.messenger.AndroidUtilities.dp(r7);
        r3.width = r7;
        r7 = 1114636288; // 0x42700000 float:60.0 double:5.507034975E-315;
        r7 = org.telegram.messenger.AndroidUtilities.dp(r7);
        r3.height = r7;
        r7 = 1082130432; // 0x40800000 float:4.0 double:5.34643471E-315;
        r7 = org.telegram.messenger.AndroidUtilities.dp(r7);
        r3.bottomMargin = r7;
        r7 = 1092616192; // 0x41200000 float:10.0 double:5.398241246E-315;
        r7 = org.telegram.messenger.AndroidUtilities.dp(r7);
        r3.rightMargin = r7;
        r7 = 85;
        r3.gravity = r7;
        r7 = r0.checkImage;
        r7.setLayoutParams(r3);
        r7 = r0.checkImage;
        r10 = new org.telegram.ui.Components.PasscodeView$4;
        r10.<init>();
        r7.setOnClickListener(r10);
        r7 = new android.widget.FrameLayout;
        r7.<init>(r1);
        r10 = 654311423; // 0x26ffffff float:1.7763567E-15 double:3.23272796E-315;
        r7.setBackgroundColor(r10);
        r10 = r0.passwordFrameLayout;
        r10.addView(r7);
        r10 = r7.getLayoutParams();
        r3 = r10;
        r3 = (android.widget.FrameLayout.LayoutParams) r3;
        r3.width = r4;
        r8 = org.telegram.messenger.AndroidUtilities.dp(r8);
        r3.height = r8;
        r8 = 83;
        r3.gravity = r8;
        r8 = 1101004800; // 0x41a00000 float:20.0 double:5.439686476E-315;
        r10 = org.telegram.messenger.AndroidUtilities.dp(r8);
        r3.leftMargin = r10;
        r10 = org.telegram.messenger.AndroidUtilities.dp(r8);
        r3.rightMargin = r10;
        r7.setLayoutParams(r3);
        r10 = new android.widget.FrameLayout;
        r10.<init>(r1);
        r0.numbersFrameLayout = r10;
        r10 = r0.numbersFrameLayout;
        r0.addView(r10);
        r10 = r0.numbersFrameLayout;
        r10 = r10.getLayoutParams();
        r3 = r10;
        r3 = (android.widget.FrameLayout.LayoutParams) r3;
        r3.width = r4;
        r3.height = r4;
        r3.gravity = r5;
        r10 = r0.numbersFrameLayout;
        r10.setLayoutParams(r3);
        r10 = new java.util.ArrayList;
        r11 = 10;
        r10.<init>(r11);
        r0.lettersTextViews = r10;
        r10 = new java.util.ArrayList;
        r10.<init>(r11);
        r0.numberTextViews = r10;
        r10 = new java.util.ArrayList;
        r10.<init>(r11);
        r0.numberFrameLayouts = r10;
        r10 = r3;
        r3 = r2;
    L_0x027b:
        r13 = 1112014848; // 0x42480000 float:50.0 double:5.49408334E-315;
        if (r3 >= r11) goto L_0x033e;
    L_0x027f:
        r15 = new android.widget.TextView;
        r15.<init>(r1);
        r15.setTextColor(r4);
        r15.setTextSize(r12, r14);
        r4 = 17;
        r15.setGravity(r4);
        r4 = java.util.Locale.US;
        r14 = "%d";
        r9 = new java.lang.Object[r12];
        r16 = java.lang.Integer.valueOf(r3);
        r9[r2] = r16;
        r4 = java.lang.String.format(r4, r14, r9);
        r15.setText(r4);
        r4 = r0.numbersFrameLayout;
        r4.addView(r15);
        r4 = r15.getLayoutParams();
        r4 = (android.widget.FrameLayout.LayoutParams) r4;
        r9 = org.telegram.messenger.AndroidUtilities.dp(r13);
        r4.width = r9;
        r9 = org.telegram.messenger.AndroidUtilities.dp(r13);
        r4.height = r9;
        r4.gravity = r5;
        r15.setLayoutParams(r4);
        r9 = r0.numberTextViews;
        r9.add(r15);
        r9 = new android.widget.TextView;
        r9.<init>(r1);
        r10 = 1094713344; // 0x41400000 float:12.0 double:5.408602553E-315;
        r9.setTextSize(r12, r10);
        r10 = 2147483647; // 0x7fffffff float:NaN double:1.060997895E-314;
        r9.setTextColor(r10);
        r10 = 17;
        r9.setGravity(r10);
        r10 = r0.numbersFrameLayout;
        r10.addView(r9);
        r10 = r9.getLayoutParams();
        r10 = (android.widget.FrameLayout.LayoutParams) r10;
        r4 = org.telegram.messenger.AndroidUtilities.dp(r13);
        r10.width = r4;
        r4 = org.telegram.messenger.AndroidUtilities.dp(r8);
        r10.height = r4;
        r10.gravity = r5;
        r9.setLayoutParams(r10);
        if (r3 == 0) goto L_0x032a;
    L_0x02f6:
        switch(r3) {
            case 2: goto L_0x0324;
            case 3: goto L_0x031e;
            case 4: goto L_0x0318;
            case 5: goto L_0x0312;
            case 6: goto L_0x030c;
            case 7: goto L_0x0306;
            case 8: goto L_0x0300;
            case 9: goto L_0x02fa;
            default: goto L_0x02f9;
        };
    L_0x02f9:
        goto L_0x0330;
    L_0x02fa:
        r4 = "WXYZ";
        r9.setText(r4);
        goto L_0x0330;
    L_0x0300:
        r4 = "TUV";
        r9.setText(r4);
        goto L_0x0330;
    L_0x0306:
        r4 = "PQRS";
        r9.setText(r4);
        goto L_0x0330;
    L_0x030c:
        r4 = "MNO";
        r9.setText(r4);
        goto L_0x0330;
    L_0x0312:
        r4 = "JKL";
        r9.setText(r4);
        goto L_0x0330;
    L_0x0318:
        r4 = "GHI";
        r9.setText(r4);
        goto L_0x0330;
    L_0x031e:
        r4 = "DEF";
        r9.setText(r4);
        goto L_0x0330;
    L_0x0324:
        r4 = "ABC";
        r9.setText(r4);
        goto L_0x0330;
    L_0x032a:
        r4 = "+";
        r9.setText(r4);
    L_0x0330:
        r4 = r0.lettersTextViews;
        r4.add(r9);
        r3 = r3 + 1;
        r4 = -1;
        r9 = 1120403456; // 0x42c80000 float:100.0 double:5.53552857E-315;
        r14 = 1108344832; // 0x42100000 float:36.0 double:5.47595105E-315;
        goto L_0x027b;
    L_0x033e:
        r3 = new android.widget.ImageView;
        r3.<init>(r1);
        r0.eraseView = r3;
        r3 = r0.eraseView;
        r4 = android.widget.ImageView.ScaleType.CENTER;
        r3.setScaleType(r4);
        r3 = r0.eraseView;
        r4 = 2131165572; // 0x7f070184 float:1.7945365E38 double:1.0529356947E-314;
        r3.setImageResource(r4);
        r3 = r0.numbersFrameLayout;
        r4 = r0.eraseView;
        r3.addView(r4);
        r3 = r0.eraseView;
        r3 = r3.getLayoutParams();
        r3 = (android.widget.FrameLayout.LayoutParams) r3;
        r4 = org.telegram.messenger.AndroidUtilities.dp(r13);
        r3.width = r4;
        r4 = org.telegram.messenger.AndroidUtilities.dp(r13);
        r3.height = r4;
        r3.gravity = r5;
        r4 = r0.eraseView;
        r4.setLayoutParams(r3);
        r4 = 11;
        if (r2 >= r4) goto L_0x03a7;
    L_0x037b:
        r4 = new android.widget.FrameLayout;
        r4.<init>(r1);
        r8 = 2131165227; // 0x7f07002b float:1.7944665E38 double:1.0529355243E-314;
        r4.setBackgroundResource(r8);
        r8 = java.lang.Integer.valueOf(r2);
        r4.setTag(r8);
        if (r2 != r11) goto L_0x0397;
        r8 = new org.telegram.ui.Components.PasscodeView$5;
        r8.<init>();
        r4.setOnLongClickListener(r8);
        r8 = new org.telegram.ui.Components.PasscodeView$6;
        r8.<init>();
        r4.setOnClickListener(r8);
        r8 = r0.numberFrameLayouts;
        r8.add(r4);
        r2 = r2 + 1;
        goto L_0x0377;
        r2 = r11;
        if (r2 < 0) goto L_0x03d5;
        r4 = r0.numberFrameLayouts;
        r4 = r4.get(r2);
        r4 = (android.widget.FrameLayout) r4;
        r8 = r0.numbersFrameLayout;
        r8.addView(r4);
        r8 = r4.getLayoutParams();
        r3 = r8;
        r3 = (android.widget.FrameLayout.LayoutParams) r3;
        r8 = 1120403456; // 0x42c80000 float:100.0 double:5.53552857E-315;
        r9 = org.telegram.messenger.AndroidUtilities.dp(r8);
        r3.width = r9;
        r9 = org.telegram.messenger.AndroidUtilities.dp(r8);
        r3.height = r9;
        r3.gravity = r5;
        r4.setLayoutParams(r3);
        r11 = r2 + -1;
        goto L_0x03a8;
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.telegram.ui.Components.PasscodeView.<init>(android.content.Context):void");
    }

    public void setDelegate(PasscodeViewDelegate delegate) {
        this.delegate = delegate;
    }

    private void processDone(boolean fingerprint) {
        if (!fingerprint) {
            String password = TtmlNode.ANONYMOUS_REGION_ID;
            if (SharedConfig.passcodeType == 0) {
                password = this.passwordEditText2.getString();
            } else if (SharedConfig.passcodeType == 1) {
                password = this.passwordEditText.getText().toString();
            }
            if (password.length() == 0) {
                onPasscodeError();
                return;
            } else if (!SharedConfig.checkPasscode(password)) {
                this.passwordEditText.setText(TtmlNode.ANONYMOUS_REGION_ID);
                this.passwordEditText2.eraseAllCharacters(true);
                onPasscodeError();
                return;
            }
        }
        this.passwordEditText.clearFocus();
        AndroidUtilities.hideKeyboard(this.passwordEditText);
        AnimatorSet AnimatorSet = new AnimatorSet();
        AnimatorSet.setDuration(200);
        r2 = new Animator[2];
        r2[0] = ObjectAnimator.ofFloat(this, "translationY", new float[]{(float) AndroidUtilities.dp(20.0f)});
        r2[1] = ObjectAnimator.ofFloat(this, "alpha", new float[]{(float) AndroidUtilities.dp(0.0f)});
        AnimatorSet.playTogether(r2);
        AnimatorSet.addListener(new AnimatorListenerAdapter() {
            public void onAnimationEnd(Animator animation) {
                PasscodeView.this.setVisibility(8);
            }
        });
        AnimatorSet.start();
        SharedConfig.appLocked = false;
        SharedConfig.saveConfig();
        NotificationCenter.getGlobalInstance().postNotificationName(NotificationCenter.didSetPasscode, new Object[0]);
        setOnTouchListener(null);
        if (this.delegate != null) {
            this.delegate.didAcceptedPassword();
        }
    }

    private void shakeTextView(final float x, final int num) {
        if (num != 6) {
            AnimatorSet AnimatorSet = new AnimatorSet();
            Animator[] animatorArr = new Animator[1];
            animatorArr[0] = ObjectAnimator.ofFloat(this.passcodeTextView, "translationX", new float[]{(float) AndroidUtilities.dp(x)});
            AnimatorSet.playTogether(animatorArr);
            AnimatorSet.setDuration(50);
            AnimatorSet.addListener(new AnimatorListenerAdapter() {
                public void onAnimationEnd(Animator animation) {
                    PasscodeView.this.shakeTextView(num == 5 ? 0.0f : -x, num + 1);
                }
            });
            AnimatorSet.start();
        }
    }

    private void onPasscodeError() {
        Vibrator v = (Vibrator) getContext().getSystemService("vibrator");
        if (v != null) {
            v.vibrate(200);
        }
        shakeTextView(2.0f, 0);
    }

    public void onResume() {
        if (SharedConfig.passcodeType == 1) {
            if (this.passwordEditText != null) {
                this.passwordEditText.requestFocus();
                AndroidUtilities.showKeyboard(this.passwordEditText);
            }
            AndroidUtilities.runOnUIThread(new Runnable() {
                public void run() {
                    if (PasscodeView.this.passwordEditText != null) {
                        PasscodeView.this.passwordEditText.requestFocus();
                        AndroidUtilities.showKeyboard(PasscodeView.this.passwordEditText);
                    }
                }
            }, 200);
        }
        checkFingerprint();
    }

    public void onPause() {
        if (this.fingerprintDialog != null) {
            try {
                if (this.fingerprintDialog.isShowing()) {
                    this.fingerprintDialog.dismiss();
                }
                this.fingerprintDialog = null;
            } catch (Throwable e) {
                FileLog.e(e);
            }
        }
        try {
            if (VERSION.SDK_INT >= 23 && this.cancellationSignal != null) {
                this.cancellationSignal.cancel();
                this.cancellationSignal = null;
            }
        } catch (Throwable e2) {
            FileLog.e(e2);
        }
    }

    private void checkFingerprint() {
        PasscodeView passcodeView = this;
        Activity parentActivity = (Activity) getContext();
        if (VERSION.SDK_INT >= 23 && parentActivity != null && SharedConfig.useFingerprint && !ApplicationLoader.mainInterfacePaused) {
            try {
                if (passcodeView.fingerprintDialog == null || !passcodeView.fingerprintDialog.isShowing()) {
                    try {
                        FingerprintManagerCompat fingerprintManager = FingerprintManagerCompat.from(ApplicationLoader.applicationContext);
                        if (fingerprintManager.isHardwareDetected() && fingerprintManager.hasEnrolledFingerprints()) {
                            RelativeLayout relativeLayout = new RelativeLayout(getContext());
                            relativeLayout.setPadding(AndroidUtilities.dp(24.0f), 0, AndroidUtilities.dp(24.0f), 0);
                            TextView fingerprintTextView = new TextView(getContext());
                            fingerprintTextView.setId(id_fingerprint_textview);
                            fingerprintTextView.setTextAppearance(16974344);
                            fingerprintTextView.setTextColor(Theme.getColor(Theme.key_dialogTextBlack));
                            fingerprintTextView.setText(LocaleController.getString("FingerprintInfo", R.string.FingerprintInfo));
                            relativeLayout.addView(fingerprintTextView);
                            RelativeLayout.LayoutParams layoutParams = LayoutHelper.createRelative(-2, -2);
                            layoutParams.addRule(10);
                            layoutParams.addRule(20);
                            fingerprintTextView.setLayoutParams(layoutParams);
                            passcodeView.fingerprintImageView = new ImageView(getContext());
                            passcodeView.fingerprintImageView.setImageResource(R.drawable.ic_fp_40px);
                            passcodeView.fingerprintImageView.setId(id_fingerprint_imageview);
                            relativeLayout.addView(passcodeView.fingerprintImageView, LayoutHelper.createRelative(-2.0f, -2.0f, 0, 20, 0, 0, 20, 3, id_fingerprint_textview));
                            passcodeView.fingerprintStatusTextView = new TextView(getContext());
                            passcodeView.fingerprintStatusTextView.setGravity(16);
                            passcodeView.fingerprintStatusTextView.setText(LocaleController.getString("FingerprintHelp", R.string.FingerprintHelp));
                            passcodeView.fingerprintStatusTextView.setTextAppearance(16974320);
                            passcodeView.fingerprintStatusTextView.setTextColor(Theme.getColor(Theme.key_dialogTextBlack) & 1124073471);
                            relativeLayout.addView(passcodeView.fingerprintStatusTextView);
                            RelativeLayout.LayoutParams layoutParams2 = LayoutHelper.createRelative(-2, -2);
                            layoutParams2.setMarginStart(AndroidUtilities.dp(16.0f));
                            layoutParams2.addRule(8, id_fingerprint_imageview);
                            layoutParams2.addRule(6, id_fingerprint_imageview);
                            layoutParams2.addRule(17, id_fingerprint_imageview);
                            passcodeView.fingerprintStatusTextView.setLayoutParams(layoutParams2);
                            Builder builder = new Builder(getContext());
                            builder.setTitle(LocaleController.getString("AppName", R.string.AppName));
                            builder.setView(relativeLayout);
                            builder.setNegativeButton(LocaleController.getString("Cancel", R.string.Cancel), null);
                            builder.setOnDismissListener(new OnDismissListener() {
                                public void onDismiss(DialogInterface dialog) {
                                    if (PasscodeView.this.cancellationSignal != null) {
                                        PasscodeView.this.selfCancelled = true;
                                        PasscodeView.this.cancellationSignal.cancel();
                                        PasscodeView.this.cancellationSignal = null;
                                    }
                                }
                            });
                            if (passcodeView.fingerprintDialog != null) {
                                if (passcodeView.fingerprintDialog.isShowing()) {
                                    passcodeView.fingerprintDialog.dismiss();
                                }
                            }
                            passcodeView.fingerprintDialog = builder.show();
                            passcodeView.cancellationSignal = new CancellationSignal();
                            passcodeView.selfCancelled = false;
                            fingerprintManager.authenticate(null, 0, passcodeView.cancellationSignal, new AuthenticationCallback() {
                                public void onAuthenticationError(int errMsgId, CharSequence errString) {
                                    if (!PasscodeView.this.selfCancelled) {
                                        PasscodeView.this.showFingerprintError(errString);
                                    }
                                }

                                public void onAuthenticationHelp(int helpMsgId, CharSequence helpString) {
                                    PasscodeView.this.showFingerprintError(helpString);
                                }

                                public void onAuthenticationFailed() {
                                    PasscodeView.this.showFingerprintError(LocaleController.getString("FingerprintNotRecognized", R.string.FingerprintNotRecognized));
                                }

                                public void onAuthenticationSucceeded(AuthenticationResult result) {
                                    try {
                                        if (PasscodeView.this.fingerprintDialog.isShowing()) {
                                            PasscodeView.this.fingerprintDialog.dismiss();
                                        }
                                    } catch (Throwable e) {
                                        FileLog.e(e);
                                    }
                                    PasscodeView.this.fingerprintDialog = null;
                                    PasscodeView.this.processDone(true);
                                }
                            }, null);
                        }
                    } catch (Throwable e) {
                        FileLog.e(e);
                    } catch (Throwable th) {
                    }
                }
            } catch (Throwable e2) {
                FileLog.e(e2);
            }
        }
    }

    public void onShow() {
        Activity parentActivity = (Activity) getContext();
        if (SharedConfig.passcodeType == 1) {
            if (this.passwordEditText != null) {
                this.passwordEditText.requestFocus();
                AndroidUtilities.showKeyboard(this.passwordEditText);
            }
        } else if (parentActivity != null) {
            View currentFocus = parentActivity.getCurrentFocus();
            if (currentFocus != null) {
                currentFocus.clearFocus();
                AndroidUtilities.hideKeyboard(((Activity) getContext()).getCurrentFocus());
            }
        }
        checkFingerprint();
        if (getVisibility() != 0) {
            setAlpha(1.0f);
            setTranslationY(0.0f);
            if (Theme.isCustomTheme()) {
                this.backgroundDrawable = Theme.getCachedWallpaper();
                this.backgroundFrameLayout.setBackgroundColor(-1090519040);
            } else if (MessagesController.getGlobalMainSettings().getInt("selectedBackground", 1000001) == 1000001) {
                this.backgroundFrameLayout.setBackgroundColor(-11436898);
            } else {
                this.backgroundDrawable = Theme.getCachedWallpaper();
                if (this.backgroundDrawable != null) {
                    this.backgroundFrameLayout.setBackgroundColor(-1090519040);
                } else {
                    this.backgroundFrameLayout.setBackgroundColor(-11436898);
                }
            }
            this.passcodeTextView.setText(LocaleController.getString("EnterYourPasscode", R.string.EnterYourPasscode));
            if (SharedConfig.passcodeType == 0) {
                this.numbersFrameLayout.setVisibility(0);
                this.passwordEditText.setVisibility(8);
                this.passwordEditText2.setVisibility(0);
                this.checkImage.setVisibility(8);
            } else if (SharedConfig.passcodeType == 1) {
                this.passwordEditText.setFilters(new InputFilter[0]);
                this.passwordEditText.setInputType(TsExtractor.TS_STREAM_TYPE_AC3);
                this.numbersFrameLayout.setVisibility(8);
                this.passwordEditText.setFocusable(true);
                this.passwordEditText.setFocusableInTouchMode(true);
                this.passwordEditText.setVisibility(0);
                this.passwordEditText2.setVisibility(8);
                this.checkImage.setVisibility(0);
            }
            setVisibility(0);
            this.passwordEditText.setTransformationMethod(PasswordTransformationMethod.getInstance());
            this.passwordEditText.setText(TtmlNode.ANONYMOUS_REGION_ID);
            this.passwordEditText2.eraseAllCharacters(false);
            setOnTouchListener(new OnTouchListener() {
                public boolean onTouch(View v, MotionEvent event) {
                    return true;
                }
            });
        }
    }

    private void showFingerprintError(CharSequence error) {
        this.fingerprintImageView.setImageResource(R.drawable.ic_fingerprint_error);
        this.fingerprintStatusTextView.setText(error);
        this.fingerprintStatusTextView.setTextColor(-765666);
        Vibrator v = (Vibrator) getContext().getSystemService("vibrator");
        if (v != null) {
            v.vibrate(200);
        }
        AndroidUtilities.shakeView(this.fingerprintStatusTextView, 2.0f, 0);
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        LayoutParams layoutParams;
        PasscodeView passcodeView = this;
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int a = 0;
        int height = AndroidUtilities.displaySize.y - (VERSION.SDK_INT >= 21 ? 0 : AndroidUtilities.statusBarHeight);
        if (AndroidUtilities.isTablet() || getContext().getResources().getConfiguration().orientation != 2) {
            int top = 0;
            int left = 0;
            if (AndroidUtilities.isTablet()) {
                if (width > AndroidUtilities.dp(498.0f)) {
                    int left2 = (width - AndroidUtilities.dp(498.0f)) / 2;
                    width = AndroidUtilities.dp(498.0f);
                    left = left2;
                }
                if (height > AndroidUtilities.dp(528.0f)) {
                    top = (height - AndroidUtilities.dp(528.0f)) / 2;
                    height = AndroidUtilities.dp(528.0f);
                }
            }
            LayoutParams layoutParams2 = (LayoutParams) passcodeView.passwordFrameLayout.getLayoutParams();
            layoutParams2.height = height / 3;
            layoutParams2.width = width;
            layoutParams2.topMargin = top;
            layoutParams2.leftMargin = left;
            passcodeView.passwordFrameLayout.setTag(Integer.valueOf(top));
            passcodeView.passwordFrameLayout.setLayoutParams(layoutParams2);
            layoutParams2 = (LayoutParams) passcodeView.numbersFrameLayout.getLayoutParams();
            layoutParams2.height = (height / 3) * 2;
            layoutParams2.leftMargin = left;
            layoutParams2.topMargin = (height - layoutParams2.height) + top;
            layoutParams2.width = width;
            passcodeView.numbersFrameLayout.setLayoutParams(layoutParams2);
            layoutParams = layoutParams2;
        } else {
            layoutParams = (LayoutParams) passcodeView.passwordFrameLayout.getLayoutParams();
            layoutParams.width = SharedConfig.passcodeType == 0 ? width / 2 : width;
            layoutParams.height = AndroidUtilities.dp(140.0f);
            layoutParams.topMargin = (height - AndroidUtilities.dp(140.0f)) / 2;
            passcodeView.passwordFrameLayout.setLayoutParams(layoutParams);
            layoutParams = (LayoutParams) passcodeView.numbersFrameLayout.getLayoutParams();
            layoutParams.height = height;
            layoutParams.leftMargin = width / 2;
            layoutParams.topMargin = height - layoutParams.height;
            layoutParams.width = width / 2;
            passcodeView.numbersFrameLayout.setLayoutParams(layoutParams);
        }
        float f = 50.0f;
        int sizeBetweenNumbersX = (layoutParams.width - (AndroidUtilities.dp(50.0f) * 3)) / 4;
        int sizeBetweenNumbersY = (layoutParams.height - (AndroidUtilities.dp(50.0f) * 4)) / 5;
        while (a < 11) {
            int num;
            int row;
            int col;
            int top2;
            FrameLayout frameLayout;
            LayoutParams layoutParams1;
            if (a == 0) {
                num = 10;
            } else if (a == 10) {
                num = 11;
            } else {
                num = a - 1;
                row = num / 3;
                col = num % 3;
                if (a >= 10) {
                    TextView textView = (TextView) passcodeView.numberTextViews.get(a);
                    TextView textView1 = (TextView) passcodeView.lettersTextViews.get(a);
                    layoutParams = (LayoutParams) textView.getLayoutParams();
                    LayoutParams layoutParams12 = (LayoutParams) textView1.getLayoutParams();
                    top2 = ((AndroidUtilities.dp(f) + sizeBetweenNumbersY) * row) + sizeBetweenNumbersY;
                    layoutParams.topMargin = top2;
                    layoutParams12.topMargin = top2;
                    int dp = ((AndroidUtilities.dp(f) + sizeBetweenNumbersX) * col) + sizeBetweenNumbersX;
                    layoutParams.leftMargin = dp;
                    layoutParams12.leftMargin = dp;
                    layoutParams12.topMargin += AndroidUtilities.dp(40.0f);
                    textView.setLayoutParams(layoutParams);
                    textView1.setLayoutParams(layoutParams12);
                    f = 50.0f;
                } else {
                    layoutParams = (LayoutParams) passcodeView.eraseView.getLayoutParams();
                    f = 50.0f;
                    left2 = (((AndroidUtilities.dp(50.0f) + sizeBetweenNumbersY) * row) + sizeBetweenNumbersY) + AndroidUtilities.dp(8.0f);
                    layoutParams.topMargin = left2;
                    layoutParams.leftMargin = ((AndroidUtilities.dp(50.0f) + sizeBetweenNumbersX) * col) + sizeBetweenNumbersX;
                    top2 = left2 - AndroidUtilities.dp(8.0f);
                    passcodeView.eraseView.setLayoutParams(layoutParams);
                }
                frameLayout = (FrameLayout) passcodeView.numberFrameLayouts.get(a);
                layoutParams1 = (LayoutParams) frameLayout.getLayoutParams();
                layoutParams1.topMargin = top2 - AndroidUtilities.dp(17.0f);
                layoutParams1.leftMargin = layoutParams.leftMargin - AndroidUtilities.dp(25.0f);
                frameLayout.setLayoutParams(layoutParams1);
                a++;
            }
            row = num / 3;
            col = num % 3;
            if (a >= 10) {
                layoutParams = (LayoutParams) passcodeView.eraseView.getLayoutParams();
                f = 50.0f;
                left2 = (((AndroidUtilities.dp(50.0f) + sizeBetweenNumbersY) * row) + sizeBetweenNumbersY) + AndroidUtilities.dp(8.0f);
                layoutParams.topMargin = left2;
                layoutParams.leftMargin = ((AndroidUtilities.dp(50.0f) + sizeBetweenNumbersX) * col) + sizeBetweenNumbersX;
                top2 = left2 - AndroidUtilities.dp(8.0f);
                passcodeView.eraseView.setLayoutParams(layoutParams);
            } else {
                TextView textView2 = (TextView) passcodeView.numberTextViews.get(a);
                TextView textView12 = (TextView) passcodeView.lettersTextViews.get(a);
                layoutParams = (LayoutParams) textView2.getLayoutParams();
                LayoutParams layoutParams122 = (LayoutParams) textView12.getLayoutParams();
                top2 = ((AndroidUtilities.dp(f) + sizeBetweenNumbersY) * row) + sizeBetweenNumbersY;
                layoutParams.topMargin = top2;
                layoutParams122.topMargin = top2;
                int dp2 = ((AndroidUtilities.dp(f) + sizeBetweenNumbersX) * col) + sizeBetweenNumbersX;
                layoutParams.leftMargin = dp2;
                layoutParams122.leftMargin = dp2;
                layoutParams122.topMargin += AndroidUtilities.dp(40.0f);
                textView2.setLayoutParams(layoutParams);
                textView12.setLayoutParams(layoutParams122);
                f = 50.0f;
            }
            frameLayout = (FrameLayout) passcodeView.numberFrameLayouts.get(a);
            layoutParams1 = (LayoutParams) frameLayout.getLayoutParams();
            layoutParams1.topMargin = top2 - AndroidUtilities.dp(17.0f);
            layoutParams1.leftMargin = layoutParams.leftMargin - AndroidUtilities.dp(25.0f);
            frameLayout.setLayoutParams(layoutParams1);
            a++;
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        View rootView = getRootView();
        int usableViewHeight = (rootView.getHeight() - AndroidUtilities.statusBarHeight) - AndroidUtilities.getViewInset(rootView);
        getWindowVisibleDisplayFrame(this.rect);
        this.keyboardHeight = usableViewHeight - (this.rect.bottom - this.rect.top);
        if (SharedConfig.passcodeType == 1 && (AndroidUtilities.isTablet() || getContext().getResources().getConfiguration().orientation != 2)) {
            int t = 0;
            if (this.passwordFrameLayout.getTag() != null) {
                t = ((Integer) this.passwordFrameLayout.getTag()).intValue();
            }
            LayoutParams layoutParams = (LayoutParams) this.passwordFrameLayout.getLayoutParams();
            layoutParams.topMargin = ((layoutParams.height + t) - (this.keyboardHeight / 2)) - (VERSION.SDK_INT >= 21 ? AndroidUtilities.statusBarHeight : 0);
            this.passwordFrameLayout.setLayoutParams(layoutParams);
        }
        super.onLayout(changed, left, top, right, bottom);
    }

    protected void onDraw(Canvas canvas) {
        if (getVisibility() == 0) {
            if (this.backgroundDrawable == null) {
                super.onDraw(canvas);
            } else if (this.backgroundDrawable instanceof ColorDrawable) {
                this.backgroundDrawable.setBounds(0, 0, getMeasuredWidth(), getMeasuredHeight());
                this.backgroundDrawable.draw(canvas);
            } else {
                float scaleX = ((float) getMeasuredWidth()) / ((float) this.backgroundDrawable.getIntrinsicWidth());
                float scaleY = ((float) (getMeasuredHeight() + this.keyboardHeight)) / ((float) this.backgroundDrawable.getIntrinsicHeight());
                float scale = scaleX < scaleY ? scaleY : scaleX;
                int width = (int) Math.ceil((double) (((float) this.backgroundDrawable.getIntrinsicWidth()) * scale));
                int height = (int) Math.ceil((double) (((float) this.backgroundDrawable.getIntrinsicHeight()) * scale));
                int x = (getMeasuredWidth() - width) / 2;
                int y = ((getMeasuredHeight() - height) + this.keyboardHeight) / 2;
                this.backgroundDrawable.setBounds(x, y, x + width, y + height);
                this.backgroundDrawable.draw(canvas);
            }
        }
    }
}
