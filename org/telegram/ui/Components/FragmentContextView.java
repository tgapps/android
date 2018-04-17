package org.telegram.ui.Components;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffColorFilter;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Keep;
import android.text.SpannableStringBuilder;
import android.text.TextUtils.TruncateAt;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.TextView;
import java.util.ArrayList;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.LocationController;
import org.telegram.messenger.LocationController.SharingLocationInfo;
import org.telegram.messenger.MediaController;
import org.telegram.messenger.MessageObject;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.NotificationCenter;
import org.telegram.messenger.NotificationCenter.NotificationCenterDelegate;
import org.telegram.messenger.SendMessagesHelper;
import org.telegram.messenger.UserConfig;
import org.telegram.messenger.UserObject;
import org.telegram.messenger.beta.R;
import org.telegram.messenger.voip.VoIPService;
import org.telegram.tgnet.ConnectionsManager;
import org.telegram.tgnet.TLRPC.Chat;
import org.telegram.tgnet.TLRPC.Message;
import org.telegram.tgnet.TLRPC.MessageMedia;
import org.telegram.tgnet.TLRPC.User;
import org.telegram.ui.ActionBar.AlertDialog.Builder;
import org.telegram.ui.ActionBar.BaseFragment;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.ChatActivity;
import org.telegram.ui.Components.SharingLocationsAlert.SharingLocationsAlertDelegate;
import org.telegram.ui.DialogsActivity;
import org.telegram.ui.LaunchActivity;
import org.telegram.ui.LocationActivity;
import org.telegram.ui.LocationActivity.LocationActivityDelegate;
import org.telegram.ui.VoIPActivity;

public class FragmentContextView extends FrameLayout implements NotificationCenterDelegate {
    private FragmentContextView additionalContextView;
    private AnimatorSet animatorSet;
    private Runnable checkLocationRunnable = new Runnable() {
        public void run() {
            FragmentContextView.this.checkLocationString();
            AndroidUtilities.runOnUIThread(FragmentContextView.this.checkLocationRunnable, 1000);
        }
    };
    private ImageView closeButton;
    private int currentStyle = -1;
    private boolean firstLocationsLoaded;
    private BaseFragment fragment;
    private FrameLayout frameLayout;
    private boolean isLocation;
    private int lastLocationSharingCount = -1;
    private MessageObject lastMessageObject;
    private String lastString;
    private boolean loadingSharingCount;
    private ImageView playButton;
    private TextView titleTextView;
    private float topPadding;
    private boolean visible;
    private float yPosition;

    private void checkPlayer(boolean r1) {
        /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.DecodeException: Load method exception in method: org.telegram.ui.Components.FragmentContextView.checkPlayer(boolean):void
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
        r0 = r17;
        r1 = org.telegram.messenger.MediaController.getInstance();
        r1 = r1.getPlayingMessageObject();
        r2 = r0.fragment;
        r2 = r2.getFragmentView();
        if (r18 != 0) goto L_0x0028;
    L_0x0012:
        if (r2 == 0) goto L_0x0028;
    L_0x0014:
        r4 = r2.getParent();
        if (r4 == 0) goto L_0x0026;
    L_0x001a:
        r4 = r2.getParent();
        r4 = (android.view.View) r4;
        r4 = r4.getVisibility();
        if (r4 == 0) goto L_0x0028;
    L_0x0026:
        r3 = 1;
        goto L_0x002a;
    L_0x0028:
        r3 = r18;
    L_0x002a:
        r4 = 200; // 0xc8 float:2.8E-43 double:9.9E-322;
        r6 = 0;
        r7 = 2;
        r8 = 1108344832; // 0x42100000 float:36.0 double:5.47595105E-315;
        r9 = 0;
        r10 = 1;
        r11 = 0;
        if (r1 == 0) goto L_0x01a0;
    L_0x0035:
        r12 = r1.getId();
        if (r12 != 0) goto L_0x003d;
    L_0x003b:
        goto L_0x01a0;
    L_0x003d:
        r12 = r0.currentStyle;
        r0.updateStyle(r11);
        r13 = 1116733440; // 0x42900000 float:72.0 double:5.517396283E-315;
        if (r3 == 0) goto L_0x0080;
    L_0x0046:
        r14 = r0.topPadding;
        r14 = (r14 > r9 ? 1 : (r14 == r9 ? 0 : -1));
        if (r14 != 0) goto L_0x0080;
    L_0x004c:
        r14 = org.telegram.messenger.AndroidUtilities.dp2(r8);
        r14 = (float) r14;
        r0.setTopPadding(r14);
        r14 = r0.additionalContextView;
        if (r14 == 0) goto L_0x006e;
    L_0x0058:
        r14 = r0.additionalContextView;
        r14 = r14.getVisibility();
        if (r14 != 0) goto L_0x006e;
    L_0x0060:
        r14 = r17.getLayoutParams();
        r14 = (android.widget.FrameLayout.LayoutParams) r14;
        r15 = org.telegram.messenger.AndroidUtilities.dp(r13);
        r15 = -r15;
        r14.topMargin = r15;
        goto L_0x007b;
    L_0x006e:
        r14 = r17.getLayoutParams();
        r14 = (android.widget.FrameLayout.LayoutParams) r14;
        r15 = org.telegram.messenger.AndroidUtilities.dp(r8);
        r15 = -r15;
        r14.topMargin = r15;
    L_0x007b:
        r0.setTranslationY(r9);
        r0.yPosition = r9;
    L_0x0080:
        r14 = r0.visible;
        if (r14 != 0) goto L_0x0104;
    L_0x0084:
        if (r3 != 0) goto L_0x00ff;
    L_0x0086:
        r14 = r0.animatorSet;
        if (r14 == 0) goto L_0x0091;
    L_0x008a:
        r14 = r0.animatorSet;
        r14.cancel();
        r0.animatorSet = r6;
    L_0x0091:
        r6 = new android.animation.AnimatorSet;
        r6.<init>();
        r0.animatorSet = r6;
        r6 = r0.additionalContextView;
        if (r6 == 0) goto L_0x00b2;
    L_0x009c:
        r6 = r0.additionalContextView;
        r6 = r6.getVisibility();
        if (r6 != 0) goto L_0x00b2;
    L_0x00a4:
        r6 = r17.getLayoutParams();
        r6 = (android.widget.FrameLayout.LayoutParams) r6;
        r13 = org.telegram.messenger.AndroidUtilities.dp(r13);
        r13 = -r13;
        r6.topMargin = r13;
        goto L_0x00bf;
    L_0x00b2:
        r6 = r17.getLayoutParams();
        r6 = (android.widget.FrameLayout.LayoutParams) r6;
        r13 = org.telegram.messenger.AndroidUtilities.dp(r8);
        r13 = -r13;
        r6.topMargin = r13;
    L_0x00bf:
        r6 = r0.animatorSet;
        r13 = new android.animation.Animator[r7];
        r14 = "translationY";
        r15 = new float[r7];
        r7 = org.telegram.messenger.AndroidUtilities.dp2(r8);
        r7 = -r7;
        r7 = (float) r7;
        r15[r11] = r7;
        r15[r10] = r9;
        r7 = android.animation.ObjectAnimator.ofFloat(r0, r14, r15);
        r13[r11] = r7;
        r7 = "topPadding";
        r9 = new float[r10];
        r8 = org.telegram.messenger.AndroidUtilities.dp2(r8);
        r8 = (float) r8;
        r9[r11] = r8;
        r7 = android.animation.ObjectAnimator.ofFloat(r0, r7, r9);
        r13[r10] = r7;
        r6.playTogether(r13);
        r6 = r0.animatorSet;
        r6.setDuration(r4);
        r4 = r0.animatorSet;
        r5 = new org.telegram.ui.Components.FragmentContextView$9;
        r5.<init>();
        r4.addListener(r5);
        r4 = r0.animatorSet;
        r4.start();
    L_0x00ff:
        r0.visible = r10;
        r0.setVisibility(r11);
    L_0x0104:
        r4 = org.telegram.messenger.MediaController.getInstance();
        r4 = r4.isMessagePaused();
        if (r4 == 0) goto L_0x0117;
    L_0x010e:
        r4 = r0.playButton;
        r5 = 2131165506; // 0x7f070142 float:1.7945231E38 double:1.052935662E-314;
        r4.setImageResource(r5);
        goto L_0x011f;
    L_0x0117:
        r4 = r0.playButton;
        r5 = 2131165505; // 0x7f070141 float:1.794523E38 double:1.0529356616E-314;
        r4.setImageResource(r5);
    L_0x011f:
        r4 = r0.lastMessageObject;
        if (r4 != r1) goto L_0x0125;
    L_0x0123:
        if (r12 == 0) goto L_0x0221;
    L_0x0125:
        r0.lastMessageObject = r1;
        r4 = r0.lastMessageObject;
        r4 = r4.isVoice();
        if (r4 != 0) goto L_0x015a;
    L_0x012f:
        r4 = r0.lastMessageObject;
        r4 = r4.isRoundVideo();
        if (r4 == 0) goto L_0x0138;
    L_0x0137:
        goto L_0x015a;
    L_0x0138:
        r4 = new android.text.SpannableStringBuilder;
        r5 = "%s - %s";
        r6 = 2;
        r6 = new java.lang.Object[r6];
        r7 = r1.getMusicAuthor();
        r6[r11] = r7;
        r7 = r1.getMusicTitle();
        r6[r10] = r7;
        r5 = java.lang.String.format(r5, r6);
        r4.<init>(r5);
        r5 = r0.titleTextView;
        r6 = android.text.TextUtils.TruncateAt.END;
        r5.setEllipsize(r6);
        goto L_0x017b;
    L_0x015a:
        r4 = new android.text.SpannableStringBuilder;
        r5 = "%s %s";
        r6 = 2;
        r6 = new java.lang.Object[r6];
        r7 = r1.getMusicAuthor();
        r6[r11] = r7;
        r7 = r1.getMusicTitle();
        r6[r10] = r7;
        r5 = java.lang.String.format(r5, r6);
        r4.<init>(r5);
        r5 = r0.titleTextView;
        r6 = android.text.TextUtils.TruncateAt.MIDDLE;
        r5.setEllipsize(r6);
    L_0x017b:
        r5 = new org.telegram.ui.Components.TypefaceSpan;
        r6 = "fonts/rmedium.ttf";
        r6 = org.telegram.messenger.AndroidUtilities.getTypeface(r6);
        r7 = "inappPlayerPerformer";
        r7 = org.telegram.ui.ActionBar.Theme.getColor(r7);
        r5.<init>(r6, r11, r7);
        r6 = r1.getMusicAuthor();
        r6 = r6.length();
        r7 = 18;
        r4.setSpan(r5, r11, r6, r7);
        r6 = r0.titleTextView;
        r6.setText(r4);
        goto L_0x0221;
    L_0x01a0:
        r0.lastMessageObject = r6;
        r7 = org.telegram.messenger.voip.VoIPService.getSharedInstance();
        if (r7 == 0) goto L_0x01b6;
    L_0x01a8:
        r7 = org.telegram.messenger.voip.VoIPService.getSharedInstance();
        r7 = r7.getCallState();
        r12 = 15;
        if (r7 == r12) goto L_0x01b6;
    L_0x01b4:
        r7 = r10;
        goto L_0x01b7;
    L_0x01b6:
        r7 = r11;
    L_0x01b7:
        if (r7 == 0) goto L_0x01bd;
    L_0x01b9:
        r0.checkCall(r11);
        return;
    L_0x01bd:
        r12 = r0.visible;
        if (r12 == 0) goto L_0x0220;
    L_0x01c1:
        r0.visible = r11;
        if (r3 == 0) goto L_0x01d4;
        r4 = r17.getVisibility();
        r5 = 8;
        if (r4 == r5) goto L_0x01d0;
        r0.setVisibility(r5);
        r0.setTopPadding(r9);
        goto L_0x0220;
        r12 = r0.animatorSet;
        if (r12 == 0) goto L_0x01df;
        r12 = r0.animatorSet;
        r12.cancel();
        r0.animatorSet = r6;
        r6 = new android.animation.AnimatorSet;
        r6.<init>();
        r0.animatorSet = r6;
        r6 = r0.animatorSet;
        r12 = 2;
        r12 = new android.animation.Animator[r12];
        r13 = "translationY";
        r14 = new float[r10];
        r8 = org.telegram.messenger.AndroidUtilities.dp2(r8);
        r8 = -r8;
        r8 = (float) r8;
        r14[r11] = r8;
        r8 = android.animation.ObjectAnimator.ofFloat(r0, r13, r14);
        r12[r11] = r8;
        r8 = "topPadding";
        r13 = new float[r10];
        r13[r11] = r9;
        r8 = android.animation.ObjectAnimator.ofFloat(r0, r8, r13);
        r12[r10] = r8;
        r6.playTogether(r12);
        r6 = r0.animatorSet;
        r6.setDuration(r4);
        r4 = r0.animatorSet;
        r5 = new org.telegram.ui.Components.FragmentContextView$8;
        r5.<init>();
        r4.addListener(r5);
        r4 = r0.animatorSet;
        r4.start();
    L_0x0221:
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.telegram.ui.Components.FragmentContextView.checkPlayer(boolean):void");
    }

    public FragmentContextView(Context context, BaseFragment parentFragment, boolean location) {
        super(context);
        this.fragment = parentFragment;
        this.visible = true;
        this.isLocation = location;
        ((ViewGroup) this.fragment.getFragmentView()).setClipToPadding(false);
        setTag(Integer.valueOf(1));
        this.frameLayout = new FrameLayout(context);
        this.frameLayout.setWillNotDraw(false);
        addView(this.frameLayout, LayoutHelper.createFrame(-1, 36.0f, 51, 0.0f, 0.0f, 0.0f, 0.0f));
        View shadow = new View(context);
        shadow.setBackgroundResource(R.drawable.header_shadow);
        addView(shadow, LayoutHelper.createFrame(-1, 3.0f, 51, 0.0f, 36.0f, 0.0f, 0.0f));
        this.playButton = new ImageView(context);
        this.playButton.setScaleType(ScaleType.CENTER);
        this.playButton.setColorFilter(new PorterDuffColorFilter(Theme.getColor(Theme.key_inappPlayerPlayPause), Mode.MULTIPLY));
        addView(this.playButton, LayoutHelper.createFrame(36, 36.0f, 51, 0.0f, 0.0f, 0.0f, 0.0f));
        this.playButton.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                if (FragmentContextView.this.currentStyle != 0) {
                    return;
                }
                if (MediaController.getInstance().isMessagePaused()) {
                    MediaController.getInstance().playMessage(MediaController.getInstance().getPlayingMessageObject());
                } else {
                    MediaController.getInstance().pauseMessage(MediaController.getInstance().getPlayingMessageObject());
                }
            }
        });
        this.titleTextView = new TextView(context);
        this.titleTextView.setMaxLines(1);
        this.titleTextView.setLines(1);
        this.titleTextView.setSingleLine(true);
        this.titleTextView.setEllipsize(TruncateAt.END);
        this.titleTextView.setTextSize(1, 15.0f);
        this.titleTextView.setGravity(19);
        addView(this.titleTextView, LayoutHelper.createFrame(-1, 36.0f, 51, 35.0f, 0.0f, 36.0f, 0.0f));
        this.closeButton = new ImageView(context);
        this.closeButton.setImageResource(R.drawable.miniplayer_close);
        this.closeButton.setColorFilter(new PorterDuffColorFilter(Theme.getColor(Theme.key_inappPlayerClose), Mode.MULTIPLY));
        this.closeButton.setScaleType(ScaleType.CENTER);
        addView(this.closeButton, LayoutHelper.createFrame(36, 36, 53));
        this.closeButton.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                if (FragmentContextView.this.currentStyle == 2) {
                    Builder builder = new Builder(FragmentContextView.this.fragment.getParentActivity());
                    builder.setTitle(LocaleController.getString("AppName", R.string.AppName));
                    if (FragmentContextView.this.fragment instanceof DialogsActivity) {
                        builder.setMessage(LocaleController.getString("StopLiveLocationAlertAll", R.string.StopLiveLocationAlertAll));
                    } else {
                        ChatActivity activity = (ChatActivity) FragmentContextView.this.fragment;
                        Chat chat = activity.getCurrentChat();
                        User user = activity.getCurrentUser();
                        if (chat != null) {
                            builder.setMessage(LocaleController.formatString("StopLiveLocationAlertToGroup", R.string.StopLiveLocationAlertToGroup, chat.title));
                        } else if (user != null) {
                            builder.setMessage(LocaleController.formatString("StopLiveLocationAlertToUser", R.string.StopLiveLocationAlertToUser, UserObject.getFirstName(user)));
                        } else {
                            builder.setMessage(LocaleController.getString("AreYouSure", R.string.AreYouSure));
                        }
                    }
                    builder.setPositiveButton(LocaleController.getString("OK", R.string.OK), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialogInterface, int i) {
                            if (FragmentContextView.this.fragment instanceof DialogsActivity) {
                                for (int a = 0; a < 3; a++) {
                                    LocationController.getInstance(a).removeAllLocationSharings();
                                }
                                return;
                            }
                            LocationController.getInstance(FragmentContextView.this.fragment.getCurrentAccount()).removeSharingLocation(((ChatActivity) FragmentContextView.this.fragment).getDialogId());
                        }
                    });
                    builder.setNegativeButton(LocaleController.getString("Cancel", R.string.Cancel), null);
                    builder.show();
                    return;
                }
                MediaController.getInstance().cleanupPlayer(true, true);
            }
        });
        setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                long dialog_id;
                if (FragmentContextView.this.currentStyle == 0) {
                    MessageObject messageObject = MediaController.getInstance().getPlayingMessageObject();
                    if (!(FragmentContextView.this.fragment == null || messageObject == null)) {
                        if (messageObject.isMusic()) {
                            FragmentContextView.this.fragment.showDialog(new AudioPlayerAlert(FragmentContextView.this.getContext()));
                        } else {
                            dialog_id = 0;
                            if (FragmentContextView.this.fragment instanceof ChatActivity) {
                                dialog_id = ((ChatActivity) FragmentContextView.this.fragment).getDialogId();
                            }
                            if (messageObject.getDialogId() == dialog_id) {
                                ((ChatActivity) FragmentContextView.this.fragment).scrollToMessageId(messageObject.getId(), 0, false, 0, true);
                            } else {
                                dialog_id = messageObject.getDialogId();
                                Bundle args = new Bundle();
                                int lower_part = (int) dialog_id;
                                int high_id = (int) (dialog_id >> 32);
                                if (lower_part == 0) {
                                    args.putInt("enc_id", high_id);
                                } else if (high_id == 1) {
                                    args.putInt("chat_id", lower_part);
                                } else if (lower_part > 0) {
                                    args.putInt("user_id", lower_part);
                                } else if (lower_part < 0) {
                                    args.putInt("chat_id", -lower_part);
                                }
                                args.putInt("message_id", messageObject.getId());
                                FragmentContextView.this.fragment.presentFragment(new ChatActivity(args), FragmentContextView.this.fragment instanceof ChatActivity);
                            }
                        }
                    }
                } else if (FragmentContextView.this.currentStyle == 1) {
                    Intent intent = new Intent(FragmentContextView.this.getContext(), VoIPActivity.class);
                    intent.addFlags(805306368);
                    FragmentContextView.this.getContext().startActivity(intent);
                } else if (FragmentContextView.this.currentStyle == 2) {
                    long did;
                    dialog_id = 0;
                    int account = UserConfig.selectedAccount;
                    if (FragmentContextView.this.fragment instanceof ChatActivity) {
                        did = ((ChatActivity) FragmentContextView.this.fragment).getDialogId();
                        account = FragmentContextView.this.fragment.getCurrentAccount();
                    } else if (LocationController.getLocationsCount() == 1) {
                        for (int a = 0; a < 3; a++) {
                            if (!LocationController.getInstance(a).sharingLocationsUI.isEmpty()) {
                                SharingLocationInfo info = (SharingLocationInfo) LocationController.getInstance(a).sharingLocationsUI.get(0);
                                dialog_id = info.did;
                                account = info.messageObject.currentAccount;
                                break;
                            }
                        }
                        did = dialog_id;
                    } else {
                        did = 0;
                    }
                    if (did != 0) {
                        FragmentContextView.this.openSharingLocation(LocationController.getInstance(account).getSharingLocationInfo(did));
                    } else {
                        FragmentContextView.this.fragment.showDialog(new SharingLocationsAlert(FragmentContextView.this.getContext(), new SharingLocationsAlertDelegate() {
                            public void didSelectLocation(SharingLocationInfo info) {
                                FragmentContextView.this.openSharingLocation(info);
                            }
                        }));
                    }
                }
            }
        });
    }

    public void setAdditionalContextView(FragmentContextView contextView) {
        this.additionalContextView = contextView;
    }

    private void openSharingLocation(final SharingLocationInfo info) {
        if (info != null) {
            if (this.fragment.getParentActivity() != null) {
                LaunchActivity launchActivity = (LaunchActivity) this.fragment.getParentActivity();
                launchActivity.switchToAccount(info.messageObject.currentAccount, true);
                LocationActivity locationActivity = new LocationActivity(2);
                locationActivity.setMessageObject(info.messageObject);
                final long dialog_id = info.messageObject.getDialogId();
                locationActivity.setDelegate(new LocationActivityDelegate() {
                    public void didSelectLocation(MessageMedia location, int live) {
                        SendMessagesHelper.getInstance(info.messageObject.currentAccount).sendMessage(location, dialog_id, null, null, null);
                    }
                });
                launchActivity.presentFragment(locationActivity);
            }
        }
    }

    public float getTopPadding() {
        return this.topPadding;
    }

    private void checkVisibility() {
        boolean show = false;
        int i = 0;
        if (this.isLocation) {
            if (this.fragment instanceof DialogsActivity) {
                show = LocationController.getLocationsCount() != 0;
            } else {
                show = LocationController.getInstance(this.fragment.getCurrentAccount()).isSharingLocation(((ChatActivity) this.fragment).getDialogId());
            }
        } else if (VoIPService.getSharedInstance() == null || VoIPService.getSharedInstance().getCallState() == 15) {
            MessageObject messageObject = MediaController.getInstance().getPlayingMessageObject();
            if (!(messageObject == null || messageObject.getId() == 0)) {
                show = true;
            }
        } else {
            show = true;
        }
        if (!show) {
            i = 8;
        }
        setVisibility(i);
    }

    @Keep
    public void setTopPadding(float value) {
        this.topPadding = value;
        if (this.fragment != null) {
            View view = this.fragment.getFragmentView();
            int additionalPadding = 0;
            if (this.additionalContextView != null && this.additionalContextView.getVisibility() == 0) {
                additionalPadding = AndroidUtilities.dp(36.0f);
            }
            if (view != null) {
                view.setPadding(0, ((int) this.topPadding) + additionalPadding, 0, 0);
            }
            if (this.isLocation && this.additionalContextView != null) {
                ((LayoutParams) this.additionalContextView.getLayoutParams()).topMargin = (-AndroidUtilities.dp(36.0f)) - ((int) this.topPadding);
            }
        }
    }

    private void updateStyle(int style) {
        if (this.currentStyle != style) {
            this.currentStyle = style;
            if (style != 0) {
                if (style != 2) {
                    if (style == 1) {
                        this.titleTextView.setText(LocaleController.getString("ReturnToCall", R.string.ReturnToCall));
                        this.frameLayout.setBackgroundColor(Theme.getColor(Theme.key_returnToCallBackground));
                        this.frameLayout.setTag(Theme.key_returnToCallBackground);
                        this.titleTextView.setTextColor(Theme.getColor(Theme.key_returnToCallText));
                        this.titleTextView.setTag(Theme.key_returnToCallText);
                        this.closeButton.setVisibility(8);
                        this.playButton.setVisibility(8);
                        this.titleTextView.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
                        this.titleTextView.setTextSize(1, 14.0f);
                        this.titleTextView.setLayoutParams(LayoutHelper.createFrame(-2, -2.0f, 17, 0.0f, 0.0f, 0.0f, 2.0f));
                    }
                }
            }
            this.frameLayout.setBackgroundColor(Theme.getColor(Theme.key_inappPlayerBackground));
            this.frameLayout.setTag(Theme.key_inappPlayerBackground);
            this.titleTextView.setTextColor(Theme.getColor(Theme.key_inappPlayerTitle));
            this.titleTextView.setTag(Theme.key_inappPlayerTitle);
            this.closeButton.setVisibility(0);
            this.playButton.setVisibility(0);
            this.titleTextView.setTypeface(Typeface.DEFAULT);
            this.titleTextView.setTextSize(1, 15.0f);
            this.titleTextView.setLayoutParams(LayoutHelper.createFrame(-1, 36.0f, 51, 35.0f, 0.0f, 36.0f, 0.0f));
            if (style == 0) {
                this.playButton.setLayoutParams(LayoutHelper.createFrame(36, 36.0f, 51, 0.0f, 0.0f, 0.0f, 0.0f));
                this.titleTextView.setLayoutParams(LayoutHelper.createFrame(-1, 36.0f, 51, 35.0f, 0.0f, 36.0f, 0.0f));
            } else if (style == 2) {
                this.playButton.setLayoutParams(LayoutHelper.createFrame(36, 36.0f, 51, 8.0f, 0.0f, 0.0f, 0.0f));
                this.titleTextView.setLayoutParams(LayoutHelper.createFrame(-1, 36.0f, 51, 51.0f, 0.0f, 36.0f, 0.0f));
            }
        }
    }

    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        this.topPadding = 0.0f;
        if (this.isLocation) {
            NotificationCenter.getGlobalInstance().removeObserver(this, NotificationCenter.liveLocationsChanged);
            NotificationCenter.getGlobalInstance().removeObserver(this, NotificationCenter.liveLocationsCacheChanged);
            return;
        }
        for (int a = 0; a < 3; a++) {
            NotificationCenter.getInstance(a).removeObserver(this, NotificationCenter.messagePlayingDidReset);
            NotificationCenter.getInstance(a).removeObserver(this, NotificationCenter.messagePlayingPlayStateChanged);
            NotificationCenter.getInstance(a).removeObserver(this, NotificationCenter.messagePlayingDidStarted);
        }
        NotificationCenter.getGlobalInstance().removeObserver(this, NotificationCenter.didStartedCall);
        NotificationCenter.getGlobalInstance().removeObserver(this, NotificationCenter.didEndedCall);
    }

    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (this.isLocation) {
            NotificationCenter.getGlobalInstance().addObserver(this, NotificationCenter.liveLocationsChanged);
            NotificationCenter.getGlobalInstance().addObserver(this, NotificationCenter.liveLocationsCacheChanged);
            if (this.additionalContextView != null) {
                this.additionalContextView.checkVisibility();
            }
            checkLiveLocation(true);
            return;
        }
        for (int a = 0; a < 3; a++) {
            NotificationCenter.getInstance(a).addObserver(this, NotificationCenter.messagePlayingDidReset);
            NotificationCenter.getInstance(a).addObserver(this, NotificationCenter.messagePlayingPlayStateChanged);
            NotificationCenter.getInstance(a).addObserver(this, NotificationCenter.messagePlayingDidStarted);
        }
        NotificationCenter.getGlobalInstance().addObserver(this, NotificationCenter.didStartedCall);
        NotificationCenter.getGlobalInstance().addObserver(this, NotificationCenter.didEndedCall);
        if (this.additionalContextView != null) {
            this.additionalContextView.checkVisibility();
        }
        if (VoIPService.getSharedInstance() == null || VoIPService.getSharedInstance().getCallState() == 15) {
            checkPlayer(true);
        } else {
            checkCall(true);
        }
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, AndroidUtilities.dp2(39.0f));
    }

    public void didReceivedNotification(int id, int account, Object... args) {
        if (id == NotificationCenter.liveLocationsChanged) {
            checkLiveLocation(false);
        } else if (id != NotificationCenter.liveLocationsCacheChanged) {
            if (!(id == NotificationCenter.messagePlayingDidStarted || id == NotificationCenter.messagePlayingPlayStateChanged || id == NotificationCenter.messagePlayingDidReset)) {
                if (id != NotificationCenter.didEndedCall) {
                    if (id == NotificationCenter.didStartedCall) {
                        checkCall(false);
                        return;
                    } else {
                        checkPlayer(false);
                        return;
                    }
                }
            }
            checkPlayer(false);
        } else if (this.fragment instanceof ChatActivity) {
            if (((ChatActivity) this.fragment).getDialogId() == ((Long) args[0]).longValue()) {
                checkLocationString();
            }
        }
    }

    private void checkLiveLocation(boolean create) {
        boolean show;
        View fragmentView = this.fragment.getFragmentView();
        if (!(create || fragmentView == null || (fragmentView.getParent() != null && ((View) fragmentView.getParent()).getVisibility() == 0))) {
            create = true;
        }
        if (this.fragment instanceof DialogsActivity) {
            show = LocationController.getLocationsCount() != 0;
        } else {
            show = LocationController.getInstance(this.fragment.getCurrentAccount()).isSharingLocation(((ChatActivity) this.fragment).getDialogId());
        }
        if (show) {
            updateStyle(2);
            this.playButton.setImageDrawable(new ShareLocationDrawable(getContext(), true));
            if (create && this.topPadding == 0.0f) {
                setTopPadding((float) AndroidUtilities.dp2(36.0f));
                setTranslationY(0.0f);
                this.yPosition = 0.0f;
            }
            if (!this.visible) {
                if (!create) {
                    if (this.animatorSet != null) {
                        this.animatorSet.cancel();
                        this.animatorSet = null;
                    }
                    this.animatorSet = new AnimatorSet();
                    AnimatorSet animatorSet = this.animatorSet;
                    r10 = new Animator[2];
                    r10[0] = ObjectAnimator.ofFloat(this, "translationY", new float[]{(float) (-AndroidUtilities.dp2(36.0f)), 0.0f});
                    r10[1] = ObjectAnimator.ofFloat(this, "topPadding", new float[]{(float) AndroidUtilities.dp2(36.0f)});
                    animatorSet.playTogether(r10);
                    this.animatorSet.setDuration(200);
                    this.animatorSet.addListener(new AnimatorListenerAdapter() {
                        public void onAnimationEnd(Animator animation) {
                            if (FragmentContextView.this.animatorSet != null && FragmentContextView.this.animatorSet.equals(animation)) {
                                FragmentContextView.this.animatorSet = null;
                            }
                        }
                    });
                    this.animatorSet.start();
                }
                this.visible = true;
                setVisibility(0);
            }
            if (this.fragment instanceof DialogsActivity) {
                int lower_id;
                String param;
                String liveLocation = LocaleController.getString("AttachLiveLocation", R.string.AttachLiveLocation);
                ArrayList<SharingLocationInfo> infos = new ArrayList();
                for (int a = 0; a < 3; a++) {
                    infos.addAll(LocationController.getInstance(a).sharingLocationsUI);
                }
                if (infos.size() == 1) {
                    String param2;
                    SharingLocationInfo info = (SharingLocationInfo) infos.get(0);
                    lower_id = (int) info.messageObject.getDialogId();
                    if (lower_id > 0) {
                        param2 = UserObject.getFirstName(MessagesController.getInstance(info.messageObject.currentAccount).getUser(Integer.valueOf(lower_id)));
                    } else {
                        Chat chat = MessagesController.getInstance(info.messageObject.currentAccount).getChat(Integer.valueOf(-lower_id));
                        if (chat != null) {
                            param2 = chat.title;
                        } else {
                            param2 = TtmlNode.ANONYMOUS_REGION_ID;
                        }
                    }
                    param = param2;
                } else {
                    param = LocaleController.formatPluralString("Chats", infos.size());
                }
                String fullString = String.format(LocaleController.getString("AttachLiveLocationIsSharing", R.string.AttachLiveLocationIsSharing), new Object[]{liveLocation, param});
                lower_id = fullString.indexOf(liveLocation);
                SpannableStringBuilder stringBuilder = new SpannableStringBuilder(fullString);
                this.titleTextView.setEllipsize(TruncateAt.END);
                stringBuilder.setSpan(new TypefaceSpan(AndroidUtilities.getTypeface("fonts/rmedium.ttf"), 0, Theme.getColor(Theme.key_inappPlayerPerformer)), lower_id, liveLocation.length() + lower_id, 18);
                this.titleTextView.setText(stringBuilder);
                return;
            }
            this.checkLocationRunnable.run();
            checkLocationString();
            return;
        }
        this.lastLocationSharingCount = -1;
        AndroidUtilities.cancelRunOnUIThread(this.checkLocationRunnable);
        if (this.visible) {
            this.visible = false;
            if (create) {
                if (getVisibility() != 8) {
                    setVisibility(8);
                }
                setTopPadding(0.0f);
                return;
            }
            if (this.animatorSet != null) {
                this.animatorSet.cancel();
                this.animatorSet = null;
            }
            this.animatorSet = new AnimatorSet();
            animatorSet = this.animatorSet;
            r8 = new Animator[2];
            r8[0] = ObjectAnimator.ofFloat(this, "translationY", new float[]{(float) (-AndroidUtilities.dp2(36.0f))});
            r8[1] = ObjectAnimator.ofFloat(this, "topPadding", new float[]{0.0f});
            animatorSet.playTogether(r8);
            this.animatorSet.setDuration(200);
            this.animatorSet.addListener(new AnimatorListenerAdapter() {
                public void onAnimationEnd(Animator animation) {
                    if (FragmentContextView.this.animatorSet != null && FragmentContextView.this.animatorSet.equals(animation)) {
                        FragmentContextView.this.setVisibility(8);
                        FragmentContextView.this.animatorSet = null;
                    }
                }
            });
            this.animatorSet.start();
        }
    }

    private void checkLocationString() {
        if (this.fragment instanceof ChatActivity) {
            if (r0.titleTextView != null) {
                int date;
                ChatActivity chatActivity = r0.fragment;
                long dialogId = chatActivity.getDialogId();
                int currentAccount = chatActivity.getCurrentAccount();
                ArrayList<Message> messages = (ArrayList) LocationController.getInstance(currentAccount).locationsCache.get(dialogId);
                if (!r0.firstLocationsLoaded) {
                    LocationController.getInstance(currentAccount).loadLiveLocations(dialogId);
                    r0.firstLocationsLoaded = true;
                }
                int locationSharingCount = 0;
                User notYouUser = null;
                if (messages != null) {
                    int currentUserId = UserConfig.getInstance(currentAccount).getClientUserId();
                    date = ConnectionsManager.getInstance(currentAccount).getCurrentTime();
                    User notYouUser2 = null;
                    int locationSharingCount2 = 0;
                    for (locationSharingCount = 0; locationSharingCount < messages.size(); locationSharingCount++) {
                        Message message = (Message) messages.get(locationSharingCount);
                        if (message.media != null) {
                            if (message.date + message.media.period > date) {
                                if (notYouUser2 == null && message.from_id != currentUserId) {
                                    notYouUser2 = MessagesController.getInstance(currentAccount).getUser(Integer.valueOf(message.from_id));
                                }
                                locationSharingCount2++;
                            }
                        }
                    }
                    locationSharingCount = locationSharingCount2;
                    notYouUser = notYouUser2;
                }
                if (r0.lastLocationSharingCount != locationSharingCount) {
                    String fullString;
                    int start;
                    SpannableStringBuilder stringBuilder;
                    r0.lastLocationSharingCount = locationSharingCount;
                    String liveLocation = LocaleController.getString("AttachLiveLocation", R.string.AttachLiveLocation);
                    if (locationSharingCount == 0) {
                        fullString = liveLocation;
                    } else {
                        date = locationSharingCount - 1;
                        if (!LocationController.getInstance(currentAccount).isSharingLocation(dialogId)) {
                            int i = 0;
                            if (date != 0) {
                                fullString = String.format("%1$s - %2$s %3$s", new Object[]{liveLocation, UserObject.getFirstName(notYouUser), LocaleController.formatPluralString("AndOther", date)});
                            } else {
                                fullString = String.format("%1$s - %2$s", new Object[]{liveLocation, UserObject.getFirstName(notYouUser)});
                                if (r0.lastString != null || !fullString.equals(r0.lastString)) {
                                    r0.lastString = fullString;
                                    start = fullString.indexOf(liveLocation);
                                    stringBuilder = new SpannableStringBuilder(fullString);
                                    r0.titleTextView.setEllipsize(TruncateAt.END);
                                    if (start >= 0) {
                                        stringBuilder.setSpan(new TypefaceSpan(AndroidUtilities.getTypeface("fonts/rmedium.ttf"), 0, Theme.getColor(Theme.key_inappPlayerPerformer)), start, liveLocation.length() + start, 18);
                                    }
                                    r0.titleTextView.setText(stringBuilder);
                                }
                                return;
                            }
                        } else if (date == 0) {
                            fullString = String.format("%1$s - %2$s", new Object[]{liveLocation, LocaleController.getString("ChatYourSelfName", R.string.ChatYourSelfName)});
                        } else if (date != 1 || notYouUser == null) {
                            fullString = String.format("%1$s - %2$s %3$s", new Object[]{liveLocation, LocaleController.getString("ChatYourSelfName", R.string.ChatYourSelfName), LocaleController.formatPluralString("AndOther", date)});
                        } else {
                            Object[] objArr = new Object[2];
                            objArr[0] = liveLocation;
                            objArr[1] = LocaleController.formatString("SharingYouAndOtherName", R.string.SharingYouAndOtherName, UserObject.getFirstName(notYouUser));
                            fullString = String.format("%1$s - %2$s", objArr);
                        }
                    }
                    if (r0.lastString != null) {
                    }
                    r0.lastString = fullString;
                    start = fullString.indexOf(liveLocation);
                    stringBuilder = new SpannableStringBuilder(fullString);
                    r0.titleTextView.setEllipsize(TruncateAt.END);
                    if (start >= 0) {
                        stringBuilder.setSpan(new TypefaceSpan(AndroidUtilities.getTypeface("fonts/rmedium.ttf"), 0, Theme.getColor(Theme.key_inappPlayerPerformer)), start, liveLocation.length() + start, 18);
                    }
                    r0.titleTextView.setText(stringBuilder);
                }
            }
        }
    }

    private void checkCall(boolean create) {
        View fragmentView = this.fragment.getFragmentView();
        if (!(create || fragmentView == null || (fragmentView.getParent() != null && ((View) fragmentView.getParent()).getVisibility() == 0))) {
            create = true;
        }
        boolean callAvailable = (VoIPService.getSharedInstance() == null || VoIPService.getSharedInstance().getCallState() == 15) ? false : true;
        AnimatorSet animatorSet;
        if (callAvailable) {
            updateStyle(1);
            if (create && this.topPadding == 0.0f) {
                setTopPadding((float) AndroidUtilities.dp2(36.0f));
                if (this.additionalContextView == null || this.additionalContextView.getVisibility() != 0) {
                    ((LayoutParams) getLayoutParams()).topMargin = -AndroidUtilities.dp(36.0f);
                } else {
                    ((LayoutParams) getLayoutParams()).topMargin = -AndroidUtilities.dp(72.0f);
                }
                setTranslationY(0.0f);
                this.yPosition = 0.0f;
            }
            if (!this.visible) {
                if (!create) {
                    if (this.animatorSet != null) {
                        this.animatorSet.cancel();
                        this.animatorSet = null;
                    }
                    this.animatorSet = new AnimatorSet();
                    if (this.additionalContextView == null || this.additionalContextView.getVisibility() != 0) {
                        ((LayoutParams) getLayoutParams()).topMargin = -AndroidUtilities.dp(36.0f);
                    } else {
                        ((LayoutParams) getLayoutParams()).topMargin = -AndroidUtilities.dp(72.0f);
                    }
                    animatorSet = this.animatorSet;
                    r10 = new Animator[2];
                    r10[0] = ObjectAnimator.ofFloat(this, "translationY", new float[]{(float) (-AndroidUtilities.dp2(36.0f)), 0.0f});
                    r10[1] = ObjectAnimator.ofFloat(this, "topPadding", new float[]{(float) AndroidUtilities.dp2(36.0f)});
                    animatorSet.playTogether(r10);
                    this.animatorSet.setDuration(200);
                    this.animatorSet.addListener(new AnimatorListenerAdapter() {
                        public void onAnimationEnd(Animator animation) {
                            if (FragmentContextView.this.animatorSet != null && FragmentContextView.this.animatorSet.equals(animation)) {
                                FragmentContextView.this.animatorSet = null;
                            }
                        }
                    });
                    this.animatorSet.start();
                }
                this.visible = true;
                setVisibility(0);
            }
        } else if (this.visible) {
            this.visible = false;
            if (create) {
                if (getVisibility() != 8) {
                    setVisibility(8);
                }
                setTopPadding(0.0f);
                return;
            }
            if (this.animatorSet != null) {
                this.animatorSet.cancel();
                this.animatorSet = null;
            }
            this.animatorSet = new AnimatorSet();
            animatorSet = this.animatorSet;
            r7 = new Animator[2];
            r7[0] = ObjectAnimator.ofFloat(this, "translationY", new float[]{(float) (-AndroidUtilities.dp2(36.0f))});
            r7[1] = ObjectAnimator.ofFloat(this, "topPadding", new float[]{0.0f});
            animatorSet.playTogether(r7);
            this.animatorSet.setDuration(200);
            this.animatorSet.addListener(new AnimatorListenerAdapter() {
                public void onAnimationEnd(Animator animation) {
                    if (FragmentContextView.this.animatorSet != null && FragmentContextView.this.animatorSet.equals(animation)) {
                        FragmentContextView.this.setVisibility(8);
                        FragmentContextView.this.animatorSet = null;
                    }
                }
            });
            this.animatorSet.start();
        }
    }

    @Keep
    public void setTranslationY(float translationY) {
        super.setTranslationY(translationY);
        this.yPosition = translationY;
        invalidate();
    }

    protected boolean drawChild(Canvas canvas, View child, long drawingTime) {
        int restoreToCount = canvas.save();
        if (this.yPosition < 0.0f) {
            canvas.clipRect(0, (int) (-this.yPosition), child.getMeasuredWidth(), AndroidUtilities.dp2(39.0f));
        }
        boolean result = super.drawChild(canvas, child, drawingTime);
        canvas.restoreToCount(restoreToCount);
        return result;
    }
}
