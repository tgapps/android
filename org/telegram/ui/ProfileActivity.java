package org.telegram.ui;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.StateListAnimator;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Configuration;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Outline;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.support.annotation.Keep;
import android.text.TextUtils;
import android.util.SparseArray;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewGroup;
import android.view.ViewOutlineProvider;
import android.view.ViewTreeObserver.OnPreDrawListener;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.ApplicationLoader;
import org.telegram.messenger.ChatObject;
import org.telegram.messenger.ContactsController;
import org.telegram.messenger.DataQuery;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MessageObject;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.MessagesStorage;
import org.telegram.messenger.NotificationCenter;
import org.telegram.messenger.NotificationCenter.NotificationCenterDelegate;
import org.telegram.messenger.NotificationsController;
import org.telegram.messenger.SecretChatHelper;
import org.telegram.messenger.SendMessagesHelper;
import org.telegram.messenger.UserConfig;
import org.telegram.messenger.Utilities;
import org.telegram.messenger.beta.R;
import org.telegram.messenger.support.widget.LinearLayoutManager;
import org.telegram.messenger.support.widget.RecyclerView;
import org.telegram.messenger.support.widget.RecyclerView.LayoutParams;
import org.telegram.messenger.support.widget.RecyclerView.OnScrollListener;
import org.telegram.messenger.support.widget.RecyclerView.ViewHolder;
import org.telegram.messenger.support.widget.helper.ItemTouchHelper.Callback;
import org.telegram.tgnet.ConnectionsManager;
import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC.BotInfo;
import org.telegram.tgnet.TLRPC.ChannelParticipant;
import org.telegram.tgnet.TLRPC.Chat;
import org.telegram.tgnet.TLRPC.ChatFull;
import org.telegram.tgnet.TLRPC.ChatParticipant;
import org.telegram.tgnet.TLRPC.EncryptedChat;
import org.telegram.tgnet.TLRPC.FileLocation;
import org.telegram.tgnet.TLRPC.InputFile;
import org.telegram.tgnet.TLRPC.PhotoSize;
import org.telegram.tgnet.TLRPC.TL_channelAdminRights;
import org.telegram.tgnet.TLRPC.TL_channelBannedRights;
import org.telegram.tgnet.TLRPC.TL_channelFull;
import org.telegram.tgnet.TLRPC.TL_channelParticipant;
import org.telegram.tgnet.TLRPC.TL_channelParticipantAdmin;
import org.telegram.tgnet.TLRPC.TL_channelParticipantBanned;
import org.telegram.tgnet.TLRPC.TL_channelParticipantCreator;
import org.telegram.tgnet.TLRPC.TL_channelParticipantsRecent;
import org.telegram.tgnet.TLRPC.TL_channels_channelParticipant;
import org.telegram.tgnet.TLRPC.TL_channels_channelParticipants;
import org.telegram.tgnet.TLRPC.TL_channels_getParticipant;
import org.telegram.tgnet.TLRPC.TL_channels_getParticipants;
import org.telegram.tgnet.TLRPC.TL_chatChannelParticipant;
import org.telegram.tgnet.TLRPC.TL_chatParticipant;
import org.telegram.tgnet.TLRPC.TL_chatParticipants;
import org.telegram.tgnet.TLRPC.TL_chatParticipantsForbidden;
import org.telegram.tgnet.TLRPC.TL_chatPhotoEmpty;
import org.telegram.tgnet.TLRPC.TL_decryptedMessageActionSetMessageTTL;
import org.telegram.tgnet.TLRPC.TL_dialog;
import org.telegram.tgnet.TLRPC.TL_encryptedChat;
import org.telegram.tgnet.TLRPC.TL_error;
import org.telegram.tgnet.TLRPC.TL_messageEncryptedAction;
import org.telegram.tgnet.TLRPC.TL_peerNotifySettings;
import org.telegram.tgnet.TLRPC.TL_userEmpty;
import org.telegram.tgnet.TLRPC.TL_userFull;
import org.telegram.tgnet.TLRPC.User;
import org.telegram.ui.ActionBar.ActionBar;
import org.telegram.ui.ActionBar.ActionBar.ActionBarMenuOnItemClick;
import org.telegram.ui.ActionBar.ActionBarMenu;
import org.telegram.ui.ActionBar.ActionBarMenuItem;
import org.telegram.ui.ActionBar.AlertDialog.Builder;
import org.telegram.ui.ActionBar.BackDrawable;
import org.telegram.ui.ActionBar.BaseFragment;
import org.telegram.ui.ActionBar.SimpleTextView;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.ActionBar.ThemeDescription;
import org.telegram.ui.ActionBar.ThemeDescription.ThemeDescriptionDelegate;
import org.telegram.ui.Cells.AboutLinkCell;
import org.telegram.ui.Cells.AboutLinkCell.AboutLinkCellDelegate;
import org.telegram.ui.Cells.DividerCell;
import org.telegram.ui.Cells.EmptyCell;
import org.telegram.ui.Cells.LoadingCell;
import org.telegram.ui.Cells.ShadowSectionCell;
import org.telegram.ui.Cells.TextCell;
import org.telegram.ui.Cells.TextDetailCell;
import org.telegram.ui.Cells.TextInfoPrivacyCell;
import org.telegram.ui.Cells.UserCell;
import org.telegram.ui.ChannelRightsEditActivity.ChannelRightsEditActivityDelegate;
import org.telegram.ui.Components.AlertsCreator;
import org.telegram.ui.Components.AvatarDrawable;
import org.telegram.ui.Components.AvatarUpdater;
import org.telegram.ui.Components.AvatarUpdater.AvatarUpdaterDelegate;
import org.telegram.ui.Components.BackupImageView;
import org.telegram.ui.Components.CombinedDrawable;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.ui.Components.RecyclerListView;
import org.telegram.ui.Components.RecyclerListView.Holder;
import org.telegram.ui.Components.RecyclerListView.OnItemClickListener;
import org.telegram.ui.Components.RecyclerListView.OnItemLongClickListener;
import org.telegram.ui.Components.RecyclerListView.SelectionAdapter;
import org.telegram.ui.Components.voip.VoIPHelper;
import org.telegram.ui.ContactsActivity.ContactsActivityDelegate;
import org.telegram.ui.DialogsActivity.DialogsActivityDelegate;
import org.telegram.ui.PhotoViewer.EmptyPhotoViewerProvider;
import org.telegram.ui.PhotoViewer.PhotoViewerProvider;
import org.telegram.ui.PhotoViewer.PlaceProviderObject;

public class ProfileActivity extends BaseFragment implements NotificationCenterDelegate, DialogsActivityDelegate {
    private static final int add_contact = 1;
    private static final int add_shortcut = 14;
    private static final int block_contact = 2;
    private static final int call_item = 15;
    private static final int convert_to_supergroup = 13;
    private static final int delete_contact = 5;
    private static final int edit_channel = 12;
    private static final int edit_contact = 4;
    private static final int edit_name = 8;
    private static final int invite_to_group = 9;
    private static final int leave_group = 7;
    private static final int search_members = 16;
    private static final int set_admins = 11;
    private static final int share = 10;
    private static final int share_contact = 3;
    private int addMemberRow;
    private boolean allowProfileAnimation = true;
    private ActionBarMenuItem animatingItem;
    private float animationProgress;
    private AvatarDrawable avatarDrawable;
    private BackupImageView avatarImage;
    private AvatarUpdater avatarUpdater;
    private int banFromGroup;
    private BotInfo botInfo;
    private ActionBarMenuItem callItem;
    private int channelInfoRow;
    private int channelNameRow;
    private int chat_id;
    private int convertHelpRow;
    private int convertRow;
    private boolean creatingChat;
    private ChannelParticipant currentChannelParticipant;
    private Chat currentChat;
    private EncryptedChat currentEncryptedChat;
    private long dialog_id;
    private ActionBarMenuItem editItem;
    private int emptyRow;
    private int emptyRowChat;
    private int emptyRowChat2;
    private int extraHeight;
    private int groupsInCommonRow;
    private ChatFull info;
    private int initialAnimationExtraHeight;
    private boolean isBot;
    private LinearLayoutManager layoutManager;
    private int leaveChannelRow;
    private ListAdapter listAdapter;
    private RecyclerListView listView;
    private int loadMoreMembersRow;
    private boolean loadingUsers;
    private int membersEndRow;
    private int membersRow;
    private int membersSectionRow;
    private long mergeDialogId;
    private SimpleTextView[] nameTextView = new SimpleTextView[2];
    private int onlineCount = -1;
    private SimpleTextView[] onlineTextView = new SimpleTextView[2];
    private boolean openAnimationInProgress;
    private SparseArray<ChatParticipant> participantsMap = new SparseArray();
    private int phoneRow;
    private boolean playProfileAnimation;
    private PhotoViewerProvider provider = new EmptyPhotoViewerProvider() {
        public PlaceProviderObject getPlaceForPhoto(MessageObject messageObject, FileLocation fileLocation, int index) {
            if (fileLocation == null) {
                return null;
            }
            FileLocation photoBig = null;
            if (ProfileActivity.this.user_id != 0) {
                User user = MessagesController.getInstance(ProfileActivity.this.currentAccount).getUser(Integer.valueOf(ProfileActivity.this.user_id));
                if (!(user == null || user.photo == null || user.photo.photo_big == null)) {
                    photoBig = user.photo.photo_big;
                }
            } else if (ProfileActivity.this.chat_id != 0) {
                Chat chat = MessagesController.getInstance(ProfileActivity.this.currentAccount).getChat(Integer.valueOf(ProfileActivity.this.chat_id));
                if (!(chat == null || chat.photo == null || chat.photo.photo_big == null)) {
                    photoBig = chat.photo.photo_big;
                }
            }
            if (photoBig == null || photoBig.local_id != fileLocation.local_id || photoBig.volume_id != fileLocation.volume_id || photoBig.dc_id != fileLocation.dc_id) {
                return null;
            }
            int[] coords = new int[2];
            ProfileActivity.this.avatarImage.getLocationInWindow(coords);
            PlaceProviderObject object = new PlaceProviderObject();
            int i = 0;
            object.viewX = coords[0];
            int i2 = coords[1];
            if (VERSION.SDK_INT < 21) {
                i = AndroidUtilities.statusBarHeight;
            }
            object.viewY = i2 - i;
            object.parentView = ProfileActivity.this.avatarImage;
            object.imageReceiver = ProfileActivity.this.avatarImage.getImageReceiver();
            if (ProfileActivity.this.user_id != 0) {
                object.dialogId = ProfileActivity.this.user_id;
            } else if (ProfileActivity.this.chat_id != 0) {
                object.dialogId = -ProfileActivity.this.chat_id;
            }
            object.thumb = object.imageReceiver.getBitmapSafe();
            object.size = -1;
            object.radius = ProfileActivity.this.avatarImage.getImageReceiver().getRoundRadius();
            object.scale = ProfileActivity.this.avatarImage.getScaleX();
            return object;
        }

        public void willHidePhotoViewer() {
            ProfileActivity.this.avatarImage.getImageReceiver().setVisible(true, true);
        }
    };
    private boolean recreateMenuAfterAnimation;
    private int rowCount = 0;
    private int sectionRow;
    private int selectedUser;
    private int settingsKeyRow;
    private int settingsNotificationsRow;
    private int settingsTimerRow;
    private int sharedMediaRow;
    private ArrayList<Integer> sortedUsers;
    private int startSecretChatRow;
    private TopView topView;
    private int totalMediaCount = -1;
    private int totalMediaCountMerge = -1;
    private boolean userBlocked;
    private int userInfoDetailedRow;
    private int userInfoRow;
    private int userSectionRow;
    private int user_id;
    private int usernameRow;
    private boolean usersEndReached;
    private ImageView writeButton;
    private AnimatorSet writeButtonAnimation;

    private class TopView extends View {
        private int currentColor;
        private Paint paint = new Paint();

        public TopView(Context context) {
            super(context);
        }

        protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
            setMeasuredDimension(MeasureSpec.getSize(widthMeasureSpec), (ActionBar.getCurrentActionBarHeight() + (ProfileActivity.this.actionBar.getOccupyStatusBar() ? AndroidUtilities.statusBarHeight : 0)) + AndroidUtilities.dp(91.0f));
        }

        public void setBackgroundColor(int color) {
            if (color != this.currentColor) {
                this.paint.setColor(color);
                invalidate();
            }
        }

        protected void onDraw(Canvas canvas) {
            int height = getMeasuredHeight() - AndroidUtilities.dp(91.0f);
            canvas.drawRect(0.0f, 0.0f, (float) getMeasuredWidth(), (float) (ProfileActivity.this.extraHeight + height), this.paint);
            if (ProfileActivity.this.parentLayout != null) {
                ProfileActivity.this.parentLayout.drawHeaderShadow(canvas, ProfileActivity.this.extraHeight + height);
            }
        }
    }

    private class ListAdapter extends SelectionAdapter {
        private Context mContext;

        public boolean isEnabled(org.telegram.messenger.support.widget.RecyclerView.ViewHolder r1) {
            /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.DecodeException: Load method exception in method: org.telegram.ui.ProfileActivity.ListAdapter.isEnabled(org.telegram.messenger.support.widget.RecyclerView$ViewHolder):boolean
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
            r0 = r5.getAdapterPosition();
            r1 = org.telegram.ui.ProfileActivity.this;
            r1 = r1.user_id;
            r2 = 1;
            r3 = 0;
            if (r1 == 0) goto L_0x0063;
        L_0x000e:
            r1 = org.telegram.ui.ProfileActivity.this;
            r1 = r1.phoneRow;
            if (r0 == r1) goto L_0x0061;
        L_0x0016:
            r1 = org.telegram.ui.ProfileActivity.this;
            r1 = r1.settingsTimerRow;
            if (r0 == r1) goto L_0x0061;
            r1 = org.telegram.ui.ProfileActivity.this;
            r1 = r1.settingsKeyRow;
            if (r0 == r1) goto L_0x0061;
            r1 = org.telegram.ui.ProfileActivity.this;
            r1 = r1.settingsNotificationsRow;
            if (r0 == r1) goto L_0x0061;
            r1 = org.telegram.ui.ProfileActivity.this;
            r1 = r1.sharedMediaRow;
            if (r0 == r1) goto L_0x0061;
            r1 = org.telegram.ui.ProfileActivity.this;
            r1 = r1.startSecretChatRow;
            if (r0 == r1) goto L_0x0061;
            r1 = org.telegram.ui.ProfileActivity.this;
            r1 = r1.usernameRow;
            if (r0 == r1) goto L_0x0061;
            r1 = org.telegram.ui.ProfileActivity.this;
            r1 = r1.userInfoRow;
            if (r0 == r1) goto L_0x0061;
            r1 = org.telegram.ui.ProfileActivity.this;
            r1 = r1.groupsInCommonRow;
            if (r0 == r1) goto L_0x0061;
            r1 = org.telegram.ui.ProfileActivity.this;
            r1 = r1.userInfoDetailedRow;
            if (r0 != r1) goto L_0x005f;
            goto L_0x0061;
            r2 = r3;
            goto L_0x0062;
            return r2;
        L_0x0063:
            r1 = org.telegram.ui.ProfileActivity.this;
            r1 = r1.chat_id;
            if (r1 == 0) goto L_0x00c0;
            r1 = org.telegram.ui.ProfileActivity.this;
            r1 = r1.convertRow;
            if (r0 == r1) goto L_0x00be;
            r1 = org.telegram.ui.ProfileActivity.this;
            r1 = r1.settingsNotificationsRow;
            if (r0 == r1) goto L_0x00be;
            r1 = org.telegram.ui.ProfileActivity.this;
            r1 = r1.sharedMediaRow;
            if (r0 == r1) goto L_0x00be;
            r1 = org.telegram.ui.ProfileActivity.this;
            r1 = r1.emptyRowChat2;
            if (r0 <= r1) goto L_0x0093;
            r1 = org.telegram.ui.ProfileActivity.this;
            r1 = r1.membersEndRow;
            if (r0 < r1) goto L_0x00be;
            r1 = org.telegram.ui.ProfileActivity.this;
            r1 = r1.addMemberRow;
            if (r0 == r1) goto L_0x00be;
            r1 = org.telegram.ui.ProfileActivity.this;
            r1 = r1.channelNameRow;
            if (r0 == r1) goto L_0x00be;
            r1 = org.telegram.ui.ProfileActivity.this;
            r1 = r1.leaveChannelRow;
            if (r0 == r1) goto L_0x00be;
            r1 = org.telegram.ui.ProfileActivity.this;
            r1 = r1.channelInfoRow;
            if (r0 == r1) goto L_0x00be;
            r1 = org.telegram.ui.ProfileActivity.this;
            r1 = r1.membersRow;
            if (r0 != r1) goto L_0x00bc;
            goto L_0x00be;
            r2 = r3;
            goto L_0x00bf;
            return r2;
            return r3;
            */
            throw new UnsupportedOperationException("Method not decompiled: org.telegram.ui.ProfileActivity.ListAdapter.isEnabled(org.telegram.messenger.support.widget.RecyclerView$ViewHolder):boolean");
        }

        public void onBindViewHolder(org.telegram.messenger.support.widget.RecyclerView.ViewHolder r1, int r2) {
            /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.DecodeException: Load method exception in method: org.telegram.ui.ProfileActivity.ListAdapter.onBindViewHolder(org.telegram.messenger.support.widget.RecyclerView$ViewHolder, int):void
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
            r0 = r18;
            r1 = r19;
            r2 = r20;
            r3 = 1;
            r4 = r19.getItemViewType();
            if (r4 == 0) goto L_0x06d5;
        L_0x000d:
            r5 = 8;
            r6 = 2131165612; // 0x7f0701ac float:1.7945446E38 double:1.0529357145E-314;
            r7 = 0;
            r8 = 1;
            if (r4 == r5) goto L_0x067f;
        L_0x0016:
            r5 = 2;
            r9 = -1;
            r10 = 0;
            switch(r4) {
                case 2: goto L_0x0500;
                case 3: goto L_0x00d5;
                case 4: goto L_0x0020;
                default: goto L_0x001c;
            };
        L_0x001c:
            r16 = r3;
            goto L_0x0703;
        L_0x0020:
            r4 = r1.itemView;
            r4 = (org.telegram.ui.Cells.UserCell) r4;
            r6 = org.telegram.ui.ProfileActivity.this;
            r6 = r6.sortedUsers;
            r6 = r6.isEmpty();
            if (r6 != 0) goto L_0x005a;
        L_0x0030:
            r6 = org.telegram.ui.ProfileActivity.this;
            r6 = r6.info;
            r6 = r6.participants;
            r6 = r6.participants;
            r9 = org.telegram.ui.ProfileActivity.this;
            r9 = r9.sortedUsers;
            r11 = org.telegram.ui.ProfileActivity.this;
            r11 = r11.emptyRowChat2;
            r11 = r2 - r11;
            r11 = r11 - r8;
            r9 = r9.get(r11);
            r9 = (java.lang.Integer) r9;
            r9 = r9.intValue();
            r6 = r6.get(r9);
            r6 = (org.telegram.tgnet.TLRPC.ChatParticipant) r6;
            goto L_0x0073;
        L_0x005a:
            r6 = org.telegram.ui.ProfileActivity.this;
            r6 = r6.info;
            r6 = r6.participants;
            r6 = r6.participants;
            r9 = org.telegram.ui.ProfileActivity.this;
            r9 = r9.emptyRowChat2;
            r9 = r2 - r9;
            r9 = r9 - r8;
            r6 = r6.get(r9);
            r6 = (org.telegram.tgnet.TLRPC.ChatParticipant) r6;
        L_0x0073:
            if (r6 == 0) goto L_0x001c;
        L_0x0075:
            r9 = r6 instanceof org.telegram.tgnet.TLRPC.TL_chatChannelParticipant;
            if (r9 == 0) goto L_0x0092;
        L_0x0079:
            r9 = r6;
            r9 = (org.telegram.tgnet.TLRPC.TL_chatChannelParticipant) r9;
            r9 = r9.channelParticipant;
            r11 = r9 instanceof org.telegram.tgnet.TLRPC.TL_channelParticipantCreator;
            if (r11 == 0) goto L_0x0086;
        L_0x0082:
            r4.setIsAdmin(r8);
            goto L_0x0091;
        L_0x0086:
            r11 = r9 instanceof org.telegram.tgnet.TLRPC.TL_channelParticipantAdmin;
            if (r11 == 0) goto L_0x008e;
        L_0x008a:
            r4.setIsAdmin(r5);
            goto L_0x0091;
        L_0x008e:
            r4.setIsAdmin(r10);
        L_0x0091:
            goto L_0x00af;
        L_0x0092:
            r9 = r6 instanceof org.telegram.tgnet.TLRPC.TL_chatParticipantCreator;
            if (r9 == 0) goto L_0x009a;
        L_0x0096:
            r4.setIsAdmin(r8);
            goto L_0x00af;
        L_0x009a:
            r9 = org.telegram.ui.ProfileActivity.this;
            r9 = r9.currentChat;
            r9 = r9.admins_enabled;
            if (r9 == 0) goto L_0x00ac;
        L_0x00a4:
            r9 = r6 instanceof org.telegram.tgnet.TLRPC.TL_chatParticipantAdmin;
            if (r9 == 0) goto L_0x00ac;
        L_0x00a8:
            r4.setIsAdmin(r5);
            goto L_0x00af;
        L_0x00ac:
            r4.setIsAdmin(r10);
        L_0x00af:
            r5 = org.telegram.ui.ProfileActivity.this;
            r5 = r5.currentAccount;
            r5 = org.telegram.messenger.MessagesController.getInstance(r5);
            r9 = r6.user_id;
            r9 = java.lang.Integer.valueOf(r9);
            r5 = r5.getUser(r9);
            r9 = org.telegram.ui.ProfileActivity.this;
            r9 = r9.emptyRowChat2;
            r9 = r9 + r8;
            if (r2 != r9) goto L_0x00d0;
        L_0x00cc:
            r10 = 2131165497; // 0x7f070139 float:1.7945213E38 double:1.0529356577E-314;
        L_0x00d0:
            r4.setData(r5, r7, r7, r10);
            goto L_0x001c;
        L_0x00d5:
            r4 = r1.itemView;
            r4 = (org.telegram.ui.Cells.TextCell) r4;
            r6 = "windowBackgroundWhiteBlackText";
            r6 = org.telegram.ui.ActionBar.Theme.getColor(r6);
            r4.setTextColor(r6);
            r6 = "windowBackgroundWhiteBlackText";
            r4.setTag(r6);
            r6 = org.telegram.ui.ProfileActivity.this;
            r6 = r6.sharedMediaRow;
            r7 = 2131165613; // 0x7f0701ad float:1.7945448E38 double:1.052935715E-314;
            if (r2 != r6) goto L_0x015f;
        L_0x00f2:
            r5 = org.telegram.ui.ProfileActivity.this;
            r5 = r5.totalMediaCount;
            if (r5 != r9) goto L_0x0104;
        L_0x00fa:
            r5 = "Loading";
            r6 = 2131493762; // 0x7f0c0382 float:1.8611013E38 double:1.053097842E-314;
            r5 = org.telegram.messenger.LocaleController.getString(r5, r6);
            goto L_0x0129;
        L_0x0104:
            r5 = "%d";
            r6 = new java.lang.Object[r8];
            r8 = org.telegram.ui.ProfileActivity.this;
            r8 = r8.totalMediaCount;
            r11 = org.telegram.ui.ProfileActivity.this;
            r11 = r11.totalMediaCountMerge;
            if (r11 == r9) goto L_0x011d;
        L_0x0116:
            r9 = org.telegram.ui.ProfileActivity.this;
            r9 = r9.totalMediaCountMerge;
            goto L_0x011e;
        L_0x011d:
            r9 = r10;
        L_0x011e:
            r8 = r8 + r9;
            r8 = java.lang.Integer.valueOf(r8);
            r6[r10] = r8;
            r5 = java.lang.String.format(r5, r6);
        L_0x0129:
            r6 = org.telegram.ui.ProfileActivity.this;
            r6 = r6.user_id;
            r8 = 2131494395; // 0x7f0c05fb float:1.8612297E38 double:1.053098155E-314;
            if (r6 == 0) goto L_0x0154;
        L_0x0134:
            r6 = org.telegram.ui.ProfileActivity.this;
            r6 = r6.currentAccount;
            r6 = org.telegram.messenger.UserConfig.getInstance(r6);
            r6 = r6.getClientUserId();
            r9 = org.telegram.ui.ProfileActivity.this;
            r9 = r9.user_id;
            if (r6 != r9) goto L_0x0154;
        L_0x014a:
            r6 = "SharedMedia";
            r6 = org.telegram.messenger.LocaleController.getString(r6, r8);
            r4.setTextAndValueAndIcon(r6, r5, r7);
            goto L_0x015d;
        L_0x0154:
            r6 = "SharedMedia";
            r6 = org.telegram.messenger.LocaleController.getString(r6, r8);
            r4.setTextAndValue(r6, r5);
        L_0x015d:
            goto L_0x001c;
        L_0x015f:
            r6 = org.telegram.ui.ProfileActivity.this;
            r6 = r6.groupsInCommonRow;
            if (r2 != r6) goto L_0x019d;
        L_0x0167:
            r5 = org.telegram.ui.ProfileActivity.this;
            r5 = r5.currentAccount;
            r5 = org.telegram.messenger.MessagesController.getInstance(r5);
            r6 = org.telegram.ui.ProfileActivity.this;
            r6 = r6.user_id;
            r5 = r5.getUserFull(r6);
            r6 = "GroupsInCommon";
            r7 = 2131493645; // 0x7f0c030d float:1.8610776E38 double:1.0530977843E-314;
            r6 = org.telegram.messenger.LocaleController.getString(r6, r7);
            r7 = "%d";
            r8 = new java.lang.Object[r8];
            if (r5 == 0) goto L_0x018d;
        L_0x018a:
            r9 = r5.common_chats_count;
            goto L_0x018e;
        L_0x018d:
            r9 = r10;
        L_0x018e:
            r9 = java.lang.Integer.valueOf(r9);
            r8[r10] = r9;
            r7 = java.lang.String.format(r7, r8);
            r4.setTextAndValue(r6, r7);
            goto L_0x001c;
        L_0x019d:
            r6 = org.telegram.ui.ProfileActivity.this;
            r6 = r6.settingsTimerRow;
            r9 = 32;
            if (r2 != r6) goto L_0x01e3;
        L_0x01a7:
            r5 = org.telegram.ui.ProfileActivity.this;
            r5 = r5.currentAccount;
            r5 = org.telegram.messenger.MessagesController.getInstance(r5);
            r6 = org.telegram.ui.ProfileActivity.this;
            r6 = r6.dialog_id;
            r6 = r6 >> r9;
            r6 = (int) r6;
            r6 = java.lang.Integer.valueOf(r6);
            r5 = r5.getEncryptedChat(r6);
            r6 = r5.ttl;
            if (r6 != 0) goto L_0x01cf;
        L_0x01c5:
            r6 = "ShortMessageLifetimeForever";
            r7 = 2131494402; // 0x7f0c0602 float:1.8612311E38 double:1.0530981583E-314;
            r6 = org.telegram.messenger.LocaleController.getString(r6, r7);
            goto L_0x01d5;
        L_0x01cf:
            r6 = r5.ttl;
            r6 = org.telegram.messenger.LocaleController.formatTTLString(r6);
        L_0x01d5:
            r7 = "MessageLifetime";
            r8 = 2131493818; // 0x7f0c03ba float:1.8611127E38 double:1.05309787E-314;
            r7 = org.telegram.messenger.LocaleController.getString(r7, r8);
            r4.setTextAndValue(r7, r6);
            goto L_0x001c;
        L_0x01e3:
            r6 = org.telegram.ui.ProfileActivity.this;
            r6 = r6.settingsNotificationsRow;
            if (r2 != r6) goto L_0x038a;
        L_0x01eb:
            r6 = org.telegram.ui.ProfileActivity.this;
            r6 = r6.currentAccount;
            r6 = org.telegram.messenger.MessagesController.getNotificationsSettings(r6);
            r9 = org.telegram.ui.ProfileActivity.this;
            r11 = r9.dialog_id;
            r13 = 0;
            r9 = (r11 > r13 ? 1 : (r11 == r13 ? 0 : -1));
            if (r9 == 0) goto L_0x0208;
        L_0x0201:
            r9 = org.telegram.ui.ProfileActivity.this;
            r11 = r9.dialog_id;
        L_0x0207:
            goto L_0x0220;
        L_0x0208:
            r9 = org.telegram.ui.ProfileActivity.this;
            r9 = r9.user_id;
            if (r9 == 0) goto L_0x0218;
        L_0x0210:
            r9 = org.telegram.ui.ProfileActivity.this;
            r9 = r9.user_id;
            r11 = (long) r9;
            goto L_0x0207;
        L_0x0218:
            r9 = org.telegram.ui.ProfileActivity.this;
            r9 = r9.chat_id;
            r9 = -r9;
            r11 = (long) r9;
        L_0x0220:
            r9 = new java.lang.StringBuilder;
            r9.<init>();
            r13 = "custom_";
            r9.append(r13);
            r9.append(r11);
            r9 = r9.toString();
            r9 = r6.getBoolean(r9, r10);
            r13 = new java.lang.StringBuilder;
            r13.<init>();
            r14 = "notify2_";
            r13.append(r14);
            r13.append(r11);
            r13 = r13.toString();
            r13 = r6.contains(r13);
            r14 = new java.lang.StringBuilder;
            r14.<init>();
            r15 = "notify2_";
            r14.append(r15);
            r14.append(r11);
            r14 = r14.toString();
            r14 = r6.getInt(r14, r10);
            r15 = new java.lang.StringBuilder;
            r15.<init>();
            r7 = "notifyuntil_";
            r15.append(r7);
            r15.append(r11);
            r7 = r15.toString();
            r7 = r6.getInt(r7, r10);
            r15 = 3;
            r5 = 2131494014; // 0x7f0c047e float:1.8611524E38 double:1.0530979666E-314;
            r10 = 2131494006; // 0x7f0c0476 float:1.8611508E38 double:1.0530979627E-314;
            if (r14 != r15) goto L_0x0319;
        L_0x027d:
            r15 = 2147483647; // 0x7fffffff float:NaN double:1.060997895E-314;
            if (r7 == r15) goto L_0x0319;
        L_0x0282:
            r15 = org.telegram.ui.ProfileActivity.this;
            r15 = r15.currentAccount;
            r15 = org.telegram.tgnet.ConnectionsManager.getInstance(r15);
            r15 = r15.getCurrentTime();
            r7 = r7 - r15;
            if (r7 > 0) goto L_0x02a6;
        L_0x0293:
            if (r9 == 0) goto L_0x029f;
        L_0x0295:
            r5 = "NotificationsCustom";
            r5 = org.telegram.messenger.LocaleController.getString(r5, r10);
        L_0x029b:
            r16 = r3;
            goto L_0x035b;
        L_0x029f:
            r8 = "NotificationsOn";
            r5 = org.telegram.messenger.LocaleController.getString(r8, r5);
            goto L_0x029b;
        L_0x02a6:
            r5 = 3600; // 0xe10 float:5.045E-42 double:1.7786E-320;
            if (r7 >= r5) goto L_0x02c1;
        L_0x02aa:
            r5 = "WillUnmuteIn";
            r8 = new java.lang.Object[r8];
            r15 = "Minutes";
            r10 = r7 / 60;
            r10 = org.telegram.messenger.LocaleController.formatPluralString(r15, r10);
            r15 = 0;
            r8[r15] = r10;
            r10 = 2131494641; // 0x7f0c06f1 float:1.8612796E38 double:1.0530982764E-314;
            r5 = org.telegram.messenger.LocaleController.formatString(r5, r10, r8);
            goto L_0x029b;
        L_0x02c1:
            r5 = 86400; // 0x15180 float:1.21072E-40 double:4.26873E-319;
            r10 = 1114636288; // 0x42700000 float:60.0 double:5.507034975E-315;
            if (r7 >= r5) goto L_0x02e9;
        L_0x02c8:
            r5 = "WillUnmuteIn";
            r8 = new java.lang.Object[r8];
            r15 = "Hours";
            r16 = r3;
            r3 = (float) r7;
            r3 = r3 / r10;
            r3 = r3 / r10;
            r1 = (double) r3;
            r1 = java.lang.Math.ceil(r1);
            r1 = (int) r1;
            r1 = org.telegram.messenger.LocaleController.formatPluralString(r15, r1);
            r2 = 0;
            r8[r2] = r1;
            r1 = 2131494641; // 0x7f0c06f1 float:1.8612796E38 double:1.0530982764E-314;
            r5 = org.telegram.messenger.LocaleController.formatString(r5, r1, r8);
        L_0x02e7:
            goto L_0x035b;
        L_0x02e9:
            r16 = r3;
            r1 = 31536000; // 0x1e13380 float:8.2725845E-38 double:1.5580854E-316;
            if (r7 >= r1) goto L_0x0315;
        L_0x02f0:
            r1 = "WillUnmuteIn";
            r2 = new java.lang.Object[r8];
            r3 = "Days";
            r5 = (float) r7;
            r5 = r5 / r10;
            r5 = r5 / r10;
            r8 = 1103101952; // 0x41c00000 float:24.0 double:5.450047783E-315;
            r5 = r5 / r8;
            r17 = r7;
            r7 = (double) r5;
            r7 = java.lang.Math.ceil(r7);
            r5 = (int) r7;
            r3 = org.telegram.messenger.LocaleController.formatPluralString(r3, r5);
            r5 = 0;
            r2[r5] = r3;
            r3 = 2131494641; // 0x7f0c06f1 float:1.8612796E38 double:1.0530982764E-314;
            r5 = org.telegram.messenger.LocaleController.formatString(r1, r3, r2);
            r7 = r17;
            goto L_0x035b;
        L_0x0315:
            r17 = r7;
            r5 = 0;
            goto L_0x02e7;
        L_0x0319:
            r16 = r3;
            if (r14 != 0) goto L_0x0332;
        L_0x031d:
            if (r13 == 0) goto L_0x0321;
        L_0x031f:
            r1 = 1;
        L_0x0320:
            goto L_0x033c;
        L_0x0321:
            r1 = (int) r11;
            if (r1 >= 0) goto L_0x032b;
        L_0x0324:
            r1 = "EnableGroup";
            r1 = r6.getBoolean(r1, r8);
            goto L_0x0320;
        L_0x032b:
            r1 = "EnableAll";
            r1 = r6.getBoolean(r1, r8);
            goto L_0x0320;
        L_0x0332:
            if (r14 != r8) goto L_0x0336;
        L_0x0334:
            r1 = 1;
            goto L_0x0320;
        L_0x0336:
            r1 = 2;
            if (r14 != r1) goto L_0x033b;
        L_0x0339:
            r1 = 0;
            goto L_0x0320;
        L_0x033b:
            r1 = 0;
        L_0x033c:
            if (r1 == 0) goto L_0x0347;
        L_0x033e:
            if (r9 == 0) goto L_0x0347;
        L_0x0340:
            r2 = "NotificationsCustom";
            r5 = org.telegram.messenger.LocaleController.getString(r2, r10);
            goto L_0x02e7;
        L_0x0347:
            if (r1 == 0) goto L_0x0351;
        L_0x0349:
            r2 = "NotificationsOn";
            r2 = org.telegram.messenger.LocaleController.getString(r2, r5);
        L_0x034f:
            r5 = r2;
            goto L_0x035b;
        L_0x0351:
            r2 = "NotificationsOff";
            r3 = 2131494013; // 0x7f0c047d float:1.8611522E38 double:1.053097966E-314;
            r2 = org.telegram.messenger.LocaleController.getString(r2, r3);
            goto L_0x034f;
        L_0x035b:
            r1 = r5;
            r2 = 2131494004; // 0x7f0c0474 float:1.8611504E38 double:1.0530979617E-314;
            if (r1 == 0) goto L_0x036e;
        L_0x0361:
            r3 = "Notifications";
            r2 = org.telegram.messenger.LocaleController.getString(r3, r2);
            r3 = 2131165613; // 0x7f0701ad float:1.7945448E38 double:1.052935715E-314;
            r4.setTextAndValueAndIcon(r2, r1, r3);
            goto L_0x0383;
        L_0x036e:
            r3 = 2131165613; // 0x7f0701ad float:1.7945448E38 double:1.052935715E-314;
            r5 = "Notifications";
            r2 = org.telegram.messenger.LocaleController.getString(r5, r2);
            r5 = "NotificationsOff";
            r8 = 2131494013; // 0x7f0c047d float:1.8611522E38 double:1.053097966E-314;
            r5 = org.telegram.messenger.LocaleController.getString(r5, r8);
            r4.setTextAndValueAndIcon(r2, r5, r3);
            r1 = r19;
            r2 = r20;
            goto L_0x0703;
        L_0x038a:
            r16 = r3;
            r1 = org.telegram.ui.ProfileActivity.this;
            r1 = r1.startSecretChatRow;
            r2 = r20;
            if (r2 != r1) goto L_0x03b4;
            r1 = "StartEncryptedChat";
            r3 = 2131494421; // 0x7f0c0615 float:1.861235E38 double:1.0530981677E-314;
            r1 = org.telegram.messenger.LocaleController.getString(r1, r3);
            r4.setText(r1);
            r1 = "windowBackgroundWhiteGreenText2";
            r4.setTag(r1);
            r1 = "windowBackgroundWhiteGreenText2";
            r1 = org.telegram.ui.ActionBar.Theme.getColor(r1);
            r4.setTextColor(r1);
            r1 = r19;
            goto L_0x0703;
            r1 = org.telegram.ui.ProfileActivity.this;
            r1 = r1.settingsKeyRow;
            if (r2 != r1) goto L_0x03eb;
            r1 = new org.telegram.ui.Components.IdenticonDrawable;
            r1.<init>();
            r3 = org.telegram.ui.ProfileActivity.this;
            r3 = r3.currentAccount;
            r3 = org.telegram.messenger.MessagesController.getInstance(r3);
            r5 = org.telegram.ui.ProfileActivity.this;
            r5 = r5.dialog_id;
            r5 = r5 >> r9;
            r5 = (int) r5;
            r5 = java.lang.Integer.valueOf(r5);
            r3 = r3.getEncryptedChat(r5);
            r1.setEncryptedChat(r3);
            r5 = "EncryptionKey";
            r6 = 2131493438; // 0x7f0c023e float:1.8610356E38 double:1.053097682E-314;
            r5 = org.telegram.messenger.LocaleController.getString(r5, r6);
            r4.setTextAndValueDrawable(r5, r1);
            goto L_0x03b0;
            r1 = org.telegram.ui.ProfileActivity.this;
            r1 = r1.leaveChannelRow;
            if (r2 != r1) goto L_0x040e;
            r1 = "windowBackgroundWhiteRedText5";
            r4.setTag(r1);
            r1 = "windowBackgroundWhiteRedText5";
            r1 = org.telegram.ui.ActionBar.Theme.getColor(r1);
            r4.setTextColor(r1);
            r1 = "LeaveChannel";
            r3 = 2131493741; // 0x7f0c036d float:1.861097E38 double:1.053097832E-314;
            r1 = org.telegram.messenger.LocaleController.getString(r1, r3);
            r4.setText(r1);
            goto L_0x03b0;
            r1 = org.telegram.ui.ProfileActivity.this;
            r1 = r1.convertRow;
            if (r2 != r1) goto L_0x0432;
            r1 = "UpgradeGroup";
            r3 = 2131494528; // 0x7f0c0680 float:1.8612567E38 double:1.0530982206E-314;
            r1 = org.telegram.messenger.LocaleController.getString(r1, r3);
            r4.setText(r1);
            r1 = "windowBackgroundWhiteGreenText2";
            r4.setTag(r1);
            r1 = "windowBackgroundWhiteGreenText2";
            r1 = org.telegram.ui.ActionBar.Theme.getColor(r1);
            r4.setTextColor(r1);
            goto L_0x03b0;
            r1 = org.telegram.ui.ProfileActivity.this;
            r1 = r1.addMemberRow;
            if (r2 != r1) goto L_0x045e;
            r1 = org.telegram.ui.ProfileActivity.this;
            r1 = r1.chat_id;
            if (r1 <= 0) goto L_0x0450;
            r1 = "AddMember";
            r3 = 2131492932; // 0x7f0c0044 float:1.860933E38 double:1.053097432E-314;
            r1 = org.telegram.messenger.LocaleController.getString(r1, r3);
            r4.setText(r1);
            goto L_0x03b0;
            r1 = "AddRecipient";
            r3 = 2131492934; // 0x7f0c0046 float:1.8609334E38 double:1.053097433E-314;
            r1 = org.telegram.messenger.LocaleController.getString(r1, r3);
            r4.setText(r1);
            goto L_0x03b0;
            r1 = org.telegram.ui.ProfileActivity.this;
            r1 = r1.membersRow;
            if (r2 != r1) goto L_0x03b0;
            r1 = org.telegram.ui.ProfileActivity.this;
            r1 = r1.info;
            if (r1 == 0) goto L_0x04ce;
            r1 = org.telegram.ui.ProfileActivity.this;
            r1 = r1.currentChat;
            r1 = org.telegram.messenger.ChatObject.isChannel(r1);
            if (r1 == 0) goto L_0x04a9;
            r1 = org.telegram.ui.ProfileActivity.this;
            r1 = r1.currentChat;
            r1 = r1.megagroup;
            if (r1 != 0) goto L_0x04a9;
            r1 = "ChannelSubscribers";
            r3 = 2131493210; // 0x7f0c015a float:1.8609894E38 double:1.0530975694E-314;
            r1 = org.telegram.messenger.LocaleController.getString(r1, r3);
            r3 = "%d";
            r5 = new java.lang.Object[r8];
            r6 = org.telegram.ui.ProfileActivity.this;
            r6 = r6.info;
            r6 = r6.participants_count;
            r6 = java.lang.Integer.valueOf(r6);
            r7 = 0;
            r5[r7] = r6;
            r3 = java.lang.String.format(r3, r5);
            r4.setTextAndValue(r1, r3);
            goto L_0x03b0;
            r1 = "ChannelMembers";
            r3 = 2131493177; // 0x7f0c0139 float:1.8609827E38 double:1.053097553E-314;
            r1 = org.telegram.messenger.LocaleController.getString(r1, r3);
            r3 = "%d";
            r5 = new java.lang.Object[r8];
            r6 = org.telegram.ui.ProfileActivity.this;
            r6 = r6.info;
            r6 = r6.participants_count;
            r6 = java.lang.Integer.valueOf(r6);
            r7 = 0;
            r5[r7] = r6;
            r3 = java.lang.String.format(r3, r5);
            r4.setTextAndValue(r1, r3);
            goto L_0x03b0;
            r1 = org.telegram.ui.ProfileActivity.this;
            r1 = r1.currentChat;
            r1 = org.telegram.messenger.ChatObject.isChannel(r1);
            if (r1 == 0) goto L_0x04f2;
            r1 = org.telegram.ui.ProfileActivity.this;
            r1 = r1.currentChat;
            r1 = r1.megagroup;
            if (r1 != 0) goto L_0x04f2;
            r1 = "ChannelSubscribers";
            r3 = 2131493210; // 0x7f0c015a float:1.8609894E38 double:1.0530975694E-314;
            r1 = org.telegram.messenger.LocaleController.getString(r1, r3);
            r4.setText(r1);
            goto L_0x03b0;
            r1 = "ChannelMembers";
            r3 = 2131493177; // 0x7f0c0139 float:1.8609827E38 double:1.053097553E-314;
            r1 = org.telegram.messenger.LocaleController.getString(r1, r3);
            r4.setText(r1);
            goto L_0x03b0;
        L_0x0500:
            r16 = r3;
            r1 = r19;
            r3 = r1.itemView;
            r3 = (org.telegram.ui.Cells.TextDetailCell) r3;
            r4 = 0;
            r3.setMultiline(r4);
            r4 = org.telegram.ui.ProfileActivity.this;
            r4 = r4.phoneRow;
            if (r2 != r4) goto L_0x056f;
            r4 = org.telegram.ui.ProfileActivity.this;
            r4 = r4.currentAccount;
            r4 = org.telegram.messenger.MessagesController.getInstance(r4);
            r5 = org.telegram.ui.ProfileActivity.this;
            r5 = r5.user_id;
            r5 = java.lang.Integer.valueOf(r5);
            r4 = r4.getUser(r5);
            r5 = r4.phone;
            if (r5 == 0) goto L_0x0554;
            r5 = r4.phone;
            r5 = r5.length();
            if (r5 == 0) goto L_0x0554;
            r5 = org.telegram.PhoneFormat.PhoneFormat.getInstance();
            r6 = new java.lang.StringBuilder;
            r6.<init>();
            r7 = "+";
            r6.append(r7);
            r7 = r4.phone;
            r6.append(r7);
            r6 = r6.toString();
            r5 = r5.format(r6);
            goto L_0x055d;
            r5 = "NumberUnknown";
            r6 = 2131494027; // 0x7f0c048b float:1.861155E38 double:1.053097973E-314;
            r5 = org.telegram.messenger.LocaleController.getString(r5, r6);
            r6 = "PhoneMobile";
            r7 = 2131494152; // 0x7f0c0508 float:1.8611804E38 double:1.053098035E-314;
            r6 = org.telegram.messenger.LocaleController.getString(r6, r7);
            r7 = 2131165614; // 0x7f0701ae float:1.794545E38 double:1.0529357155E-314;
            r8 = 0;
            r3.setTextAndValueAndIcon(r5, r6, r7, r8);
            goto L_0x0703;
            r4 = org.telegram.ui.ProfileActivity.this;
            r4 = r4.usernameRow;
            if (r2 != r4) goto L_0x05e4;
            r4 = org.telegram.ui.ProfileActivity.this;
            r4 = r4.currentAccount;
            r4 = org.telegram.messenger.MessagesController.getInstance(r4);
            r5 = org.telegram.ui.ProfileActivity.this;
            r5 = r5.user_id;
            r5 = java.lang.Integer.valueOf(r5);
            r4 = r4.getUser(r5);
            if (r4 == 0) goto L_0x05ad;
            r5 = r4.username;
            r5 = android.text.TextUtils.isEmpty(r5);
            if (r5 != 0) goto L_0x05ad;
            r5 = new java.lang.StringBuilder;
            r5.<init>();
            r7 = "@";
            r5.append(r7);
            r7 = r4.username;
            r5.append(r7);
            r5 = r5.toString();
            goto L_0x05af;
            r5 = "-";
            r7 = org.telegram.ui.ProfileActivity.this;
            r7 = r7.phoneRow;
            if (r7 != r9) goto L_0x05d6;
            r7 = org.telegram.ui.ProfileActivity.this;
            r7 = r7.userInfoRow;
            if (r7 != r9) goto L_0x05d6;
            r7 = org.telegram.ui.ProfileActivity.this;
            r7 = r7.userInfoDetailedRow;
            if (r7 != r9) goto L_0x05d6;
            r7 = "Username";
            r8 = 2131494556; // 0x7f0c069c float:1.8612624E38 double:1.0530982344E-314;
            r7 = org.telegram.messenger.LocaleController.getString(r7, r8);
            r8 = 11;
            r3.setTextAndValueAndIcon(r5, r7, r6, r8);
            goto L_0x05e2;
            r6 = "Username";
            r7 = 2131494556; // 0x7f0c069c float:1.8612624E38 double:1.0530982344E-314;
            r6 = org.telegram.messenger.LocaleController.getString(r6, r7);
            r3.setTextAndValue(r5, r6);
            goto L_0x0703;
            r4 = org.telegram.ui.ProfileActivity.this;
            r4 = r4.channelNameRow;
            if (r2 != r4) goto L_0x064b;
            r4 = org.telegram.ui.ProfileActivity.this;
            r4 = r4.currentChat;
            if (r4 == 0) goto L_0x061c;
            r4 = org.telegram.ui.ProfileActivity.this;
            r4 = r4.currentChat;
            r4 = r4.username;
            r4 = android.text.TextUtils.isEmpty(r4);
            if (r4 != 0) goto L_0x061c;
            r4 = new java.lang.StringBuilder;
            r4.<init>();
            r5 = "@";
            r4.append(r5);
            r5 = org.telegram.ui.ProfileActivity.this;
            r5 = r5.currentChat;
            r5 = r5.username;
            r4.append(r5);
            r4 = r4.toString();
            goto L_0x061e;
            r4 = "-";
            r5 = new java.lang.StringBuilder;
            r5.<init>();
            r6 = org.telegram.ui.ProfileActivity.this;
            r6 = r6.currentAccount;
            r6 = org.telegram.messenger.MessagesController.getInstance(r6);
            r6 = r6.linkPrefix;
            r5.append(r6);
            r6 = "/";
            r5.append(r6);
            r6 = org.telegram.ui.ProfileActivity.this;
            r6 = r6.currentChat;
            r6 = r6.username;
            r5.append(r6);
            r5 = r5.toString();
            r3.setTextAndValue(r4, r5);
            goto L_0x0703;
            r4 = org.telegram.ui.ProfileActivity.this;
            r4 = r4.userInfoDetailedRow;
            if (r2 != r4) goto L_0x0703;
            r4 = org.telegram.ui.ProfileActivity.this;
            r4 = r4.currentAccount;
            r4 = org.telegram.messenger.MessagesController.getInstance(r4);
            r5 = org.telegram.ui.ProfileActivity.this;
            r5 = r5.user_id;
            r4 = r4.getUserFull(r5);
            r3.setMultiline(r8);
            if (r4 == 0) goto L_0x066f;
            r7 = r4.about;
            r5 = "UserBio";
            r8 = 2131494541; // 0x7f0c068d float:1.8612593E38 double:1.053098227E-314;
            r5 = org.telegram.messenger.LocaleController.getString(r5, r8);
            r8 = 11;
            r3.setTextAndValueAndIcon(r7, r5, r6, r8);
            goto L_0x0703;
        L_0x067f:
            r16 = r3;
            r3 = r1.itemView;
            r3 = (org.telegram.ui.Cells.AboutLinkCell) r3;
            r4 = org.telegram.ui.ProfileActivity.this;
            r4 = r4.userInfoRow;
            if (r2 != r4) goto L_0x06b0;
            r4 = org.telegram.ui.ProfileActivity.this;
            r4 = r4.currentAccount;
            r4 = org.telegram.messenger.MessagesController.getInstance(r4);
            r5 = org.telegram.ui.ProfileActivity.this;
            r5 = r5.user_id;
            r4 = r4.getUserFull(r5);
            if (r4 == 0) goto L_0x06a6;
            r7 = r4.about;
            r5 = org.telegram.ui.ProfileActivity.this;
            r5 = r5.isBot;
            r3.setTextAndIcon(r7, r6, r5);
            goto L_0x0703;
            r4 = org.telegram.ui.ProfileActivity.this;
            r4 = r4.channelInfoRow;
            if (r2 != r4) goto L_0x0703;
            r4 = org.telegram.ui.ProfileActivity.this;
            r4 = r4.info;
            r4 = r4.about;
            r5 = "\n\n\n";
            r5 = r4.contains(r5);
            if (r5 == 0) goto L_0x06d1;
            r5 = "\n\n\n";
            r7 = "\n\n";
            r4 = r4.replace(r5, r7);
            goto L_0x06c0;
            r3.setTextAndIcon(r4, r6, r8);
            goto L_0x0703;
        L_0x06d5:
            r16 = r3;
            r3 = org.telegram.ui.ProfileActivity.this;
            r3 = r3.emptyRowChat;
            if (r2 == r3) goto L_0x06f6;
            r3 = org.telegram.ui.ProfileActivity.this;
            r3 = r3.emptyRowChat2;
            if (r2 != r3) goto L_0x06e8;
            goto L_0x06f6;
            r3 = r1.itemView;
            r3 = (org.telegram.ui.Cells.EmptyCell) r3;
            r4 = 1108344832; // 0x42100000 float:36.0 double:5.47595105E-315;
            r4 = org.telegram.messenger.AndroidUtilities.dp(r4);
            r3.setHeight(r4);
            goto L_0x0703;
            r3 = r1.itemView;
            r3 = (org.telegram.ui.Cells.EmptyCell) r3;
            r4 = 1090519040; // 0x41000000 float:8.0 double:5.38787994E-315;
            r4 = org.telegram.messenger.AndroidUtilities.dp(r4);
            r3.setHeight(r4);
        L_0x0703:
            return;
            */
            throw new UnsupportedOperationException("Method not decompiled: org.telegram.ui.ProfileActivity.ListAdapter.onBindViewHolder(org.telegram.messenger.support.widget.RecyclerView$ViewHolder, int):void");
        }

        public ListAdapter(Context context) {
            this.mContext = context;
        }

        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = null;
            switch (viewType) {
                case 0:
                    view = new EmptyCell(this.mContext);
                    break;
                case 1:
                    view = new DividerCell(this.mContext);
                    view.setPadding(AndroidUtilities.dp(72.0f), 0, 0, 0);
                    break;
                case 2:
                    view = new TextDetailCell(this.mContext);
                    break;
                case 3:
                    view = new TextCell(this.mContext);
                    break;
                case 4:
                    view = new UserCell(this.mContext, 61, 0, true);
                    break;
                case 5:
                    view = new ShadowSectionCell(this.mContext);
                    CombinedDrawable combinedDrawable = new CombinedDrawable(new ColorDrawable(Theme.getColor(Theme.key_windowBackgroundGray)), Theme.getThemedDrawable(this.mContext, R.drawable.greydivider, Theme.key_windowBackgroundGrayShadow));
                    combinedDrawable.setFullsize(true);
                    view.setBackgroundDrawable(combinedDrawable);
                    break;
                case 6:
                    view = new TextInfoPrivacyCell(this.mContext);
                    TextInfoPrivacyCell cell = (TextInfoPrivacyCell) view;
                    CombinedDrawable combinedDrawable2 = new CombinedDrawable(new ColorDrawable(Theme.getColor(Theme.key_windowBackgroundGray)), Theme.getThemedDrawable(this.mContext, R.drawable.greydivider, Theme.key_windowBackgroundGrayShadow));
                    combinedDrawable2.setFullsize(true);
                    cell.setBackgroundDrawable(combinedDrawable2);
                    cell.setText(AndroidUtilities.replaceTags(LocaleController.formatString("ConvertGroupInfo", R.string.ConvertGroupInfo, LocaleController.formatPluralString("Members", MessagesController.getInstance(ProfileActivity.this.currentAccount).maxMegagroupCount))));
                    break;
                case 7:
                    view = new LoadingCell(this.mContext);
                    break;
                case 8:
                    view = new AboutLinkCell(this.mContext);
                    ((AboutLinkCell) view).setDelegate(new AboutLinkCellDelegate() {
                        public void didPressUrl(String url) {
                            if (url.startsWith("@")) {
                                MessagesController.getInstance(ProfileActivity.this.currentAccount).openByUserName(url.substring(1), ProfileActivity.this, 0);
                            } else if (url.startsWith("#")) {
                                DialogsActivity fragment = new DialogsActivity(null);
                                fragment.setSearchString(url);
                                ProfileActivity.this.presentFragment(fragment);
                            } else if (url.startsWith("/") && ProfileActivity.this.parentLayout.fragmentsStack.size() > 1) {
                                BaseFragment previousFragment = (BaseFragment) ProfileActivity.this.parentLayout.fragmentsStack.get(ProfileActivity.this.parentLayout.fragmentsStack.size() - 2);
                                if (previousFragment instanceof ChatActivity) {
                                    ProfileActivity.this.finishFragment();
                                    ((ChatActivity) previousFragment).chatActivityEnterView.setCommand(null, url, false, false);
                                }
                            }
                        }
                    });
                    break;
                default:
                    break;
            }
            view.setLayoutParams(new LayoutParams(-1, -2));
            return new Holder(view);
        }

        public int getItemCount() {
            return ProfileActivity.this.rowCount;
        }

        public int getItemViewType(int i) {
            if (!(i == ProfileActivity.this.emptyRow || i == ProfileActivity.this.emptyRowChat)) {
                if (i != ProfileActivity.this.emptyRowChat2) {
                    if (i != ProfileActivity.this.sectionRow) {
                        if (i != ProfileActivity.this.userSectionRow) {
                            if (!(i == ProfileActivity.this.phoneRow || i == ProfileActivity.this.usernameRow || i == ProfileActivity.this.channelNameRow)) {
                                if (i != ProfileActivity.this.userInfoDetailedRow) {
                                    if (!(i == ProfileActivity.this.leaveChannelRow || i == ProfileActivity.this.sharedMediaRow || i == ProfileActivity.this.settingsTimerRow || i == ProfileActivity.this.settingsNotificationsRow || i == ProfileActivity.this.startSecretChatRow || i == ProfileActivity.this.settingsKeyRow || i == ProfileActivity.this.convertRow || i == ProfileActivity.this.addMemberRow || i == ProfileActivity.this.groupsInCommonRow)) {
                                        if (i != ProfileActivity.this.membersRow) {
                                            if (i > ProfileActivity.this.emptyRowChat2 && i < ProfileActivity.this.membersEndRow) {
                                                return 4;
                                            }
                                            if (i == ProfileActivity.this.membersSectionRow) {
                                                return 5;
                                            }
                                            if (i == ProfileActivity.this.convertHelpRow) {
                                                return 6;
                                            }
                                            if (i == ProfileActivity.this.loadMoreMembersRow) {
                                                return 7;
                                            }
                                            if (i != ProfileActivity.this.userInfoRow) {
                                                if (i != ProfileActivity.this.channelInfoRow) {
                                                    return 0;
                                                }
                                            }
                                            return 8;
                                        }
                                    }
                                    return 3;
                                }
                            }
                            return 2;
                        }
                    }
                    return 1;
                }
            }
            return 0;
        }
    }

    private void updateOnlineCount() {
        /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.DecodeException: Load method exception in method: org.telegram.ui.ProfileActivity.updateOnlineCount():void
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
        r0 = 0;
        r6.onlineCount = r0;
        r1 = r6.currentAccount;
        r1 = org.telegram.tgnet.ConnectionsManager.getInstance(r1);
        r1 = r1.getCurrentTime();
        r2 = r6.sortedUsers;
        r2.clear();
        r2 = r6.info;
        r2 = r2 instanceof org.telegram.tgnet.TLRPC.TL_chatFull;
        if (r2 != 0) goto L_0x002c;
    L_0x0018:
        r2 = r6.info;
        r2 = r2 instanceof org.telegram.tgnet.TLRPC.TL_channelFull;
        if (r2 == 0) goto L_0x00ab;
        r2 = r6.info;
        r2 = r2.participants_count;
        r3 = 200; // 0xc8 float:2.8E-43 double:9.9E-322;
        if (r2 > r3) goto L_0x00ab;
        r2 = r6.info;
        r2 = r2.participants;
        if (r2 == 0) goto L_0x00ab;
        r2 = r6.info;
        r2 = r2.participants;
        r2 = r2.participants;
        r2 = r2.size();
        if (r0 >= r2) goto L_0x0089;
        r2 = r6.info;
        r2 = r2.participants;
        r2 = r2.participants;
        r2 = r2.get(r0);
        r2 = (org.telegram.tgnet.TLRPC.ChatParticipant) r2;
        r3 = r6.currentAccount;
        r3 = org.telegram.messenger.MessagesController.getInstance(r3);
        r4 = r2.user_id;
        r4 = java.lang.Integer.valueOf(r4);
        r3 = r3.getUser(r4);
        if (r3 == 0) goto L_0x007d;
        r4 = r3.status;
        if (r4 == 0) goto L_0x007d;
        r4 = r3.status;
        r4 = r4.expires;
        if (r4 > r1) goto L_0x006f;
        r4 = r3.id;
        r5 = r6.currentAccount;
        r5 = org.telegram.messenger.UserConfig.getInstance(r5);
        r5 = r5.getClientUserId();
        if (r4 != r5) goto L_0x007d;
        r4 = r3.status;
        r4 = r4.expires;
        r5 = 10000; // 0x2710 float:1.4013E-41 double:4.9407E-320;
        if (r4 <= r5) goto L_0x007d;
        r4 = r6.onlineCount;
        r4 = r4 + 1;
        r6.onlineCount = r4;
        r4 = r6.sortedUsers;
        r5 = java.lang.Integer.valueOf(r0);
        r4.add(r5);
        r0 = r0 + 1;
        goto L_0x002d;
        r0 = r6.sortedUsers;	 Catch:{ Exception -> 0x0094 }
        r2 = new org.telegram.ui.ProfileActivity$29;	 Catch:{ Exception -> 0x0094 }
        r2.<init>();	 Catch:{ Exception -> 0x0094 }
        java.util.Collections.sort(r0, r2);	 Catch:{ Exception -> 0x0094 }
        goto L_0x0098;
    L_0x0094:
        r0 = move-exception;
        org.telegram.messenger.FileLog.e(r0);
        r0 = r6.listAdapter;
        if (r0 == 0) goto L_0x00ab;
        r0 = r6.listAdapter;
        r2 = r6.emptyRowChat2;
        r2 = r2 + 1;
        r3 = r6.sortedUsers;
        r3 = r3.size();
        r0.notifyItemRangeChanged(r2, r3);
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.telegram.ui.ProfileActivity.updateOnlineCount():void");
    }

    private void updateProfileData() {
        /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.DecodeException: Load method exception in method: org.telegram.ui.ProfileActivity.updateProfileData():void
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
        r0 = r20;
        r1 = r0.avatarImage;
        if (r1 == 0) goto L_0x049a;
    L_0x0006:
        r1 = r0.nameTextView;
        if (r1 != 0) goto L_0x000c;
    L_0x000a:
        goto L_0x049a;
    L_0x000c:
        r1 = r0.currentAccount;
        r1 = org.telegram.tgnet.ConnectionsManager.getInstance(r1);
        r1 = r1.getConnectionState();
        r2 = 2;
        r4 = 1;
        if (r1 != r2) goto L_0x0024;
    L_0x001a:
        r5 = "WaitingForNetwork";
        r6 = 2131494621; // 0x7f0c06dd float:1.8612756E38 double:1.0530982665E-314;
        r5 = org.telegram.messenger.LocaleController.getString(r5, r6);
    L_0x0023:
        goto L_0x004b;
    L_0x0024:
        if (r1 != r4) goto L_0x0030;
    L_0x0026:
        r5 = "Connecting";
        r6 = 2131493282; // 0x7f0c01a2 float:1.861004E38 double:1.053097605E-314;
        r5 = org.telegram.messenger.LocaleController.getString(r5, r6);
        goto L_0x0023;
    L_0x0030:
        r5 = 5;
        if (r1 != r5) goto L_0x003d;
    L_0x0033:
        r5 = "Updating";
        r6 = 2131494527; // 0x7f0c067f float:1.8612565E38 double:1.05309822E-314;
        r5 = org.telegram.messenger.LocaleController.getString(r5, r6);
        goto L_0x0023;
    L_0x003d:
        r5 = 4;
        if (r1 != r5) goto L_0x004a;
    L_0x0040:
        r5 = "ConnectingToProxy";
        r6 = 2131493283; // 0x7f0c01a3 float:1.8610042E38 double:1.0530976055E-314;
        r5 = org.telegram.messenger.LocaleController.getString(r5, r6);
        goto L_0x0023;
    L_0x004a:
        r5 = 0;
    L_0x004b:
        r6 = r0.user_id;
        r7 = 0;
        if (r6 == 0) goto L_0x01f7;
    L_0x0050:
        r6 = r0.currentAccount;
        r6 = org.telegram.messenger.MessagesController.getInstance(r6);
        r8 = r0.user_id;
        r8 = java.lang.Integer.valueOf(r8);
        r6 = r6.getUser(r8);
        r8 = 0;
        r9 = 0;
        r10 = r6.photo;
        if (r10 == 0) goto L_0x006e;
    L_0x0066:
        r10 = r6.photo;
        r8 = r10.photo_small;
        r10 = r6.photo;
        r9 = r10.photo_big;
    L_0x006e:
        r10 = r0.avatarDrawable;
        r10.setInfo(r6);
        r10 = r0.avatarImage;
        r11 = "50_50";
        r12 = r0.avatarDrawable;
        r10.setImage(r8, r11, r12);
        r10 = org.telegram.messenger.UserObject.getUserName(r6);
        r11 = r6.id;
        r12 = r0.currentAccount;
        r12 = org.telegram.messenger.UserConfig.getInstance(r12);
        r12 = r12.getClientUserId();
        if (r11 != r12) goto L_0x00a1;
    L_0x008e:
        r11 = "ChatYourSelf";
        r12 = 2131493233; // 0x7f0c0171 float:1.860994E38 double:1.053097581E-314;
        r11 = org.telegram.messenger.LocaleController.getString(r11, r12);
        r12 = "ChatYourSelfName";
        r13 = 2131493238; // 0x7f0c0176 float:1.860995E38 double:1.053097583E-314;
        r10 = org.telegram.messenger.LocaleController.getString(r12, r13);
        goto L_0x00ce;
    L_0x00a1:
        r11 = r6.id;
        r12 = 333000; // 0x514c8 float:4.66632E-40 double:1.64524E-318;
        if (r11 == r12) goto L_0x00c5;
        r11 = r6.id;
        r12 = 777000; // 0xbdb28 float:1.088809E-39 double:3.83889E-318;
        if (r11 != r12) goto L_0x00b0;
        goto L_0x00c5;
        r11 = r0.isBot;
        if (r11 == 0) goto L_0x00be;
        r11 = "Bot";
        r12 = 2131493086; // 0x7f0c00de float:1.8609642E38 double:1.053097508E-314;
        r11 = org.telegram.messenger.LocaleController.getString(r11, r12);
        goto L_0x00ce;
        r11 = r0.currentAccount;
        r11 = org.telegram.messenger.LocaleController.formatUserStatus(r11, r6);
        goto L_0x00ce;
        r11 = "ServiceNotifications";
        r12 = 2131494365; // 0x7f0c05dd float:1.8612236E38 double:1.05309814E-314;
        r11 = org.telegram.messenger.LocaleController.getString(r11, r12);
        r12 = r7;
        if (r12 >= r2) goto L_0x01e7;
        r13 = r0.nameTextView;
        r13 = r13[r12];
        if (r13 != 0) goto L_0x00da;
        goto L_0x01e2;
        if (r12 != 0) goto L_0x0165;
        r13 = r6.id;
        r14 = r0.currentAccount;
        r14 = org.telegram.messenger.UserConfig.getInstance(r14);
        r14 = r14.getClientUserId();
        if (r13 == r14) goto L_0x0165;
        r13 = r6.id;
        r13 = r13 / 1000;
        r14 = 777; // 0x309 float:1.089E-42 double:3.84E-321;
        if (r13 == r14) goto L_0x0165;
        r13 = r6.id;
        r13 = r13 / 1000;
        r14 = 333; // 0x14d float:4.67E-43 double:1.645E-321;
        if (r13 == r14) goto L_0x0165;
        r13 = r6.phone;
        if (r13 == 0) goto L_0x0165;
        r13 = r6.phone;
        r13 = r13.length();
        if (r13 == 0) goto L_0x0165;
        r13 = r0.currentAccount;
        r13 = org.telegram.messenger.ContactsController.getInstance(r13);
        r13 = r13.contactsDict;
        r14 = r6.id;
        r14 = java.lang.Integer.valueOf(r14);
        r13 = r13.get(r14);
        if (r13 != 0) goto L_0x0165;
        r13 = r0.currentAccount;
        r13 = org.telegram.messenger.ContactsController.getInstance(r13);
        r13 = r13.contactsDict;
        r13 = r13.size();
        if (r13 != 0) goto L_0x0134;
        r13 = r0.currentAccount;
        r13 = org.telegram.messenger.ContactsController.getInstance(r13);
        r13 = r13.isLoadingContacts();
        if (r13 != 0) goto L_0x0165;
        r13 = org.telegram.PhoneFormat.PhoneFormat.getInstance();
        r14 = new java.lang.StringBuilder;
        r14.<init>();
        r15 = "+";
        r14.append(r15);
        r15 = r6.phone;
        r14.append(r15);
        r14 = r14.toString();
        r13 = r13.format(r14);
        r14 = r0.nameTextView;
        r14 = r14[r12];
        r14 = r14.getText();
        r14 = r14.equals(r13);
        if (r14 != 0) goto L_0x0164;
        r14 = r0.nameTextView;
        r14 = r14[r12];
        r14.setText(r13);
        goto L_0x017a;
        r13 = r0.nameTextView;
        r13 = r13[r12];
        r13 = r13.getText();
        r13 = r13.equals(r10);
        if (r13 != 0) goto L_0x017a;
        r13 = r0.nameTextView;
        r13 = r13[r12];
        r13.setText(r10);
        if (r12 != 0) goto L_0x0186;
        if (r5 == 0) goto L_0x0186;
        r13 = r0.onlineTextView;
        r13 = r13[r12];
        r13.setText(r5);
        goto L_0x019b;
        r13 = r0.onlineTextView;
        r13 = r13[r12];
        r13 = r13.getText();
        r13 = r13.equals(r11);
        if (r13 != 0) goto L_0x019b;
        r13 = r0.onlineTextView;
        r13 = r13[r12];
        r13.setText(r11);
        r13 = r0.currentEncryptedChat;
        if (r13 == 0) goto L_0x01a2;
        r13 = org.telegram.ui.ActionBar.Theme.chat_lockIconDrawable;
        goto L_0x01a3;
        r13 = 0;
        r14 = 0;
        if (r12 != 0) goto L_0x01c6;
        r15 = r0.currentAccount;
        r15 = org.telegram.messenger.MessagesController.getInstance(r15);
        r2 = r0.dialog_id;
        r16 = 0;
        r18 = (r2 > r16 ? 1 : (r2 == r16 ? 0 : -1));
        if (r18 == 0) goto L_0x01b7;
        r2 = r0.dialog_id;
        goto L_0x01ba;
        r2 = r0.user_id;
        r2 = (long) r2;
        r2 = r15.isDialogMuted(r2);
        if (r2 == 0) goto L_0x01c3;
        r3 = org.telegram.ui.ActionBar.Theme.chat_muteIconDrawable;
        goto L_0x01c4;
        r3 = 0;
        r14 = r3;
        goto L_0x01d4;
        r2 = r6.verified;
        if (r2 == 0) goto L_0x01d4;
        r2 = new org.telegram.ui.Components.CombinedDrawable;
        r3 = org.telegram.ui.ActionBar.Theme.profile_verifiedDrawable;
        r15 = org.telegram.ui.ActionBar.Theme.profile_verifiedCheckDrawable;
        r2.<init>(r3, r15);
        r14 = r2;
        r2 = r0.nameTextView;
        r2 = r2[r12];
        r2.setLeftDrawable(r13);
        r2 = r0.nameTextView;
        r2 = r2[r12];
        r2.setRightDrawable(r14);
        r12 = r12 + 1;
        r2 = 2;
        goto L_0x00d0;
        r2 = r0.avatarImage;
        r2 = r2.getImageReceiver();
        r3 = org.telegram.ui.PhotoViewer.isShowingImage(r9);
        r3 = r3 ^ r4;
        r2.setVisible(r3, r7);
        goto L_0x0499;
    L_0x01f7:
        r2 = r0.chat_id;
        if (r2 == 0) goto L_0x0499;
        r2 = r0.currentAccount;
        r2 = org.telegram.messenger.MessagesController.getInstance(r2);
        r3 = r0.chat_id;
        r3 = java.lang.Integer.valueOf(r3);
        r2 = r2.getChat(r3);
        if (r2 == 0) goto L_0x0210;
        r0.currentChat = r2;
        goto L_0x0212;
        r2 = r0.currentChat;
        r3 = org.telegram.messenger.ChatObject.isChannel(r2);
        r6 = 200; // 0xc8 float:2.8E-43 double:9.9E-322;
        if (r3 == 0) goto L_0x02fb;
        r3 = r0.info;
        if (r3 == 0) goto L_0x02c5;
        r3 = r0.currentChat;
        r3 = r3.megagroup;
        if (r3 != 0) goto L_0x0238;
        r3 = r0.info;
        r3 = r3.participants_count;
        if (r3 == 0) goto L_0x02c5;
        r3 = r0.currentChat;
        r3 = r3.admin;
        if (r3 != 0) goto L_0x02c5;
        r3 = r0.info;
        r3 = r3.can_view_participants;
        if (r3 == 0) goto L_0x0238;
        goto L_0x02c5;
        r3 = r0.currentChat;
        r3 = r3.megagroup;
        if (r3 == 0) goto L_0x0279;
        r3 = r0.info;
        r3 = r3.participants_count;
        if (r3 > r6) goto L_0x0279;
        r3 = r0.onlineCount;
        if (r3 <= r4) goto L_0x026e;
        r3 = r0.info;
        r3 = r3.participants_count;
        if (r3 == 0) goto L_0x026e;
        r3 = "%s, %s";
        r8 = 2;
        r9 = new java.lang.Object[r8];
        r8 = "Members";
        r10 = r0.info;
        r10 = r10.participants_count;
        r8 = org.telegram.messenger.LocaleController.formatPluralString(r8, r10);
        r9[r7] = r8;
        r8 = "OnlineCount";
        r10 = r0.onlineCount;
        r8 = org.telegram.messenger.LocaleController.formatPluralString(r8, r10);
        r9[r4] = r8;
        r3 = java.lang.String.format(r3, r9);
        goto L_0x02d8;
        r3 = "Members";
        r8 = r0.info;
        r8 = r8.participants_count;
        r3 = org.telegram.messenger.LocaleController.formatPluralString(r3, r8);
        goto L_0x02d8;
        r3 = new int[r4];
        r8 = r0.info;
        r8 = r8.participants_count;
        r8 = org.telegram.messenger.LocaleController.formatShortNumber(r8, r3);
        r9 = r0.currentChat;
        r9 = r9.megagroup;
        if (r9 == 0) goto L_0x02a6;
        r9 = "Members";
        r10 = r3[r7];
        r9 = org.telegram.messenger.LocaleController.formatPluralString(r9, r10);
        r10 = "%d";
        r11 = new java.lang.Object[r4];
        r12 = r3[r7];
        r12 = java.lang.Integer.valueOf(r12);
        r11[r7] = r12;
        r10 = java.lang.String.format(r10, r11);
        r9 = r9.replace(r10, r8);
        goto L_0x02c2;
        r9 = "Subscribers";
        r10 = r3[r7];
        r9 = org.telegram.messenger.LocaleController.formatPluralString(r9, r10);
        r10 = "%d";
        r11 = new java.lang.Object[r4];
        r12 = r3[r7];
        r12 = java.lang.Integer.valueOf(r12);
        r11[r7] = r12;
        r10 = java.lang.String.format(r10, r11);
        r9 = r9.replace(r10, r8);
        r3 = r9;
        goto L_0x0334;
        r3 = r0.currentChat;
        r3 = r3.megagroup;
        if (r3 == 0) goto L_0x02d9;
        r3 = "Loading";
        r8 = 2131493762; // 0x7f0c0382 float:1.8611013E38 double:1.053097842E-314;
        r3 = org.telegram.messenger.LocaleController.getString(r3, r8);
        r3 = r3.toLowerCase();
        goto L_0x0334;
        r3 = r2.flags;
        r3 = r3 & 64;
        if (r3 == 0) goto L_0x02ed;
        r3 = "ChannelPublic";
        r8 = 2131493200; // 0x7f0c0150 float:1.8609873E38 double:1.0530975645E-314;
        r3 = org.telegram.messenger.LocaleController.getString(r3, r8);
        r3 = r3.toLowerCase();
        goto L_0x02d8;
        r3 = "ChannelPrivate";
        r8 = 2131493197; // 0x7f0c014d float:1.8609867E38 double:1.053097563E-314;
        r3 = org.telegram.messenger.LocaleController.getString(r3, r8);
        r3 = r3.toLowerCase();
        goto L_0x02d8;
        r3 = r2.participants_count;
        r8 = r0.info;
        if (r8 == 0) goto L_0x030b;
        r8 = r0.info;
        r8 = r8.participants;
        r8 = r8.participants;
        r3 = r8.size();
        if (r3 == 0) goto L_0x032e;
        r8 = r0.onlineCount;
        if (r8 <= r4) goto L_0x032e;
        r8 = "%s, %s";
        r9 = 2;
        r10 = new java.lang.Object[r9];
        r9 = "Members";
        r9 = org.telegram.messenger.LocaleController.formatPluralString(r9, r3);
        r10[r7] = r9;
        r9 = "OnlineCount";
        r11 = r0.onlineCount;
        r9 = org.telegram.messenger.LocaleController.formatPluralString(r9, r11);
        r10[r4] = r9;
        r8 = java.lang.String.format(r8, r10);
        r3 = r8;
        goto L_0x0334;
        r8 = "Members";
        r3 = org.telegram.messenger.LocaleController.formatPluralString(r8, r3);
        r8 = r7;
        r9 = 2;
        if (r8 >= r9) goto L_0x046f;
        r10 = r0.nameTextView;
        r10 = r10[r8];
        if (r10 != 0) goto L_0x0342;
        r11 = 0;
        goto L_0x0469;
        r10 = r2.title;
        if (r10 == 0) goto L_0x035f;
        r10 = r0.nameTextView;
        r10 = r10[r8];
        r10 = r10.getText();
        r11 = r2.title;
        r10 = r10.equals(r11);
        if (r10 != 0) goto L_0x035f;
        r10 = r0.nameTextView;
        r10 = r10[r8];
        r11 = r2.title;
        r10.setText(r11);
        r10 = r0.nameTextView;
        r10 = r10[r8];
        r11 = 0;
        r10.setLeftDrawable(r11);
        if (r8 == 0) goto L_0x0388;
        r10 = r2.verified;
        if (r10 == 0) goto L_0x037f;
        r10 = r0.nameTextView;
        r10 = r10[r8];
        r11 = new org.telegram.ui.Components.CombinedDrawable;
        r12 = org.telegram.ui.ActionBar.Theme.profile_verifiedDrawable;
        r13 = org.telegram.ui.ActionBar.Theme.profile_verifiedCheckDrawable;
        r11.<init>(r12, r13);
        r10.setRightDrawable(r11);
        r11 = 0;
        goto L_0x03a4;
        r10 = r0.nameTextView;
        r10 = r10[r8];
        r11 = 0;
        r10.setRightDrawable(r11);
        goto L_0x03a4;
        r11 = 0;
        r10 = r0.nameTextView;
        r10 = r10[r8];
        r12 = r0.currentAccount;
        r12 = org.telegram.messenger.MessagesController.getInstance(r12);
        r13 = r0.chat_id;
        r13 = -r13;
        r13 = (long) r13;
        r12 = r12.isDialogMuted(r13);
        if (r12 == 0) goto L_0x03a0;
        r12 = org.telegram.ui.ActionBar.Theme.chat_muteIconDrawable;
        goto L_0x03a1;
        r12 = r11;
        r10.setRightDrawable(r12);
        if (r8 != 0) goto L_0x03b1;
        if (r5 == 0) goto L_0x03b1;
        r10 = r0.onlineTextView;
        r10 = r10[r8];
        r10.setText(r5);
        goto L_0x0469;
        r10 = r0.currentChat;
        r10 = r10.megagroup;
        if (r10 == 0) goto L_0x03dc;
        r10 = r0.info;
        if (r10 == 0) goto L_0x03dc;
        r10 = r0.info;
        r10 = r10.participants_count;
        if (r10 > r6) goto L_0x03dc;
        r10 = r0.onlineCount;
        if (r10 <= 0) goto L_0x03dc;
        r10 = r0.onlineTextView;
        r10 = r10[r8];
        r10 = r10.getText();
        r10 = r10.equals(r3);
        if (r10 != 0) goto L_0x0469;
        r10 = r0.onlineTextView;
        r10 = r10[r8];
        r10.setText(r3);
        goto L_0x0469;
        if (r8 != 0) goto L_0x0454;
        r10 = r0.currentChat;
        r10 = org.telegram.messenger.ChatObject.isChannel(r10);
        if (r10 == 0) goto L_0x0454;
        r10 = r0.info;
        if (r10 == 0) goto L_0x0454;
        r10 = r0.info;
        r10 = r10.participants_count;
        if (r10 == 0) goto L_0x0454;
        r10 = r0.currentChat;
        r10 = r10.megagroup;
        if (r10 != 0) goto L_0x03fc;
        r10 = r0.currentChat;
        r10 = r10.broadcast;
        if (r10 == 0) goto L_0x0454;
        r10 = new int[r4];
        r12 = r0.info;
        r12 = r12.participants_count;
        r12 = org.telegram.messenger.LocaleController.formatShortNumber(r12, r10);
        r13 = r0.currentChat;
        r13 = r13.megagroup;
        if (r13 == 0) goto L_0x0430;
        r13 = r0.onlineTextView;
        r13 = r13[r8];
        r14 = "Members";
        r15 = r10[r7];
        r14 = org.telegram.messenger.LocaleController.formatPluralString(r14, r15);
        r15 = "%d";
        r6 = new java.lang.Object[r4];
        r9 = r10[r7];
        r9 = java.lang.Integer.valueOf(r9);
        r6[r7] = r9;
        r6 = java.lang.String.format(r15, r6);
        r6 = r14.replace(r6, r12);
        r13.setText(r6);
        goto L_0x0453;
        r6 = r0.onlineTextView;
        r6 = r6[r8];
        r9 = "Subscribers";
        r13 = r10[r7];
        r9 = org.telegram.messenger.LocaleController.formatPluralString(r9, r13);
        r13 = "%d";
        r14 = new java.lang.Object[r4];
        r15 = r10[r7];
        r15 = java.lang.Integer.valueOf(r15);
        r14[r7] = r15;
        r13 = java.lang.String.format(r13, r14);
        r9 = r9.replace(r13, r12);
        r6.setText(r9);
        goto L_0x0469;
        r6 = r0.onlineTextView;
        r6 = r6[r8];
        r6 = r6.getText();
        r6 = r6.equals(r3);
        if (r6 != 0) goto L_0x0469;
        r6 = r0.onlineTextView;
        r6 = r6[r8];
        r6.setText(r3);
        r8 = r8 + 1;
        r6 = 200; // 0xc8 float:2.8E-43 double:9.9E-322;
        goto L_0x0335;
        r6 = 0;
        r8 = 0;
        r9 = r2.photo;
        if (r9 == 0) goto L_0x047d;
        r9 = r2.photo;
        r6 = r9.photo_small;
        r9 = r2.photo;
        r8 = r9.photo_big;
        r9 = r0.avatarDrawable;
        r9.setInfo(r2);
        r9 = r0.avatarImage;
        r10 = "50_50";
        r11 = r0.avatarDrawable;
        r9.setImage(r6, r10, r11);
        r9 = r0.avatarImage;
        r9 = r9.getImageReceiver();
        r10 = org.telegram.ui.PhotoViewer.isShowingImage(r8);
        r4 = r4 ^ r10;
        r9.setVisible(r4, r7);
        return;
    L_0x049a:
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.telegram.ui.ProfileActivity.updateProfileData():void");
    }

    public ProfileActivity(Bundle args) {
        super(args);
    }

    public boolean onFragmentCreate() {
        this.user_id = this.arguments.getInt("user_id", 0);
        this.chat_id = this.arguments.getInt("chat_id", 0);
        this.banFromGroup = this.arguments.getInt("ban_chat_id", 0);
        if (this.user_id != 0) {
            this.dialog_id = this.arguments.getLong("dialog_id", 0);
            if (this.dialog_id != 0) {
                this.currentEncryptedChat = MessagesController.getInstance(this.currentAccount).getEncryptedChat(Integer.valueOf((int) (this.dialog_id >> 32)));
            }
            User user = MessagesController.getInstance(this.currentAccount).getUser(Integer.valueOf(this.user_id));
            if (user == null) {
                return false;
            }
            NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.updateInterfaces);
            NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.contactsDidLoaded);
            NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.encryptedChatCreated);
            NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.encryptedChatUpdated);
            NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.blockedUsersDidLoaded);
            NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.botInfoDidLoaded);
            NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.userInfoDidLoaded);
            if (this.currentEncryptedChat != null) {
                NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.didReceivedNewMessages);
            }
            this.userBlocked = MessagesController.getInstance(this.currentAccount).blockedUsers.contains(Integer.valueOf(this.user_id));
            if (user.bot) {
                this.isBot = true;
                DataQuery.getInstance(this.currentAccount).loadBotInfo(user.id, true, this.classGuid);
            }
            MessagesController.getInstance(this.currentAccount).loadFullUser(MessagesController.getInstance(this.currentAccount).getUser(Integer.valueOf(this.user_id)), this.classGuid, true);
            this.participantsMap = null;
        } else if (this.chat_id == 0) {
            return false;
        } else {
            this.currentChat = MessagesController.getInstance(this.currentAccount).getChat(Integer.valueOf(this.chat_id));
            if (this.currentChat == null) {
                final CountDownLatch countDownLatch = new CountDownLatch(1);
                MessagesStorage.getInstance(this.currentAccount).getStorageQueue().postRunnable(new Runnable() {
                    public void run() {
                        ProfileActivity.this.currentChat = MessagesStorage.getInstance(ProfileActivity.this.currentAccount).getChat(ProfileActivity.this.chat_id);
                        countDownLatch.countDown();
                    }
                });
                try {
                    countDownLatch.await();
                } catch (Throwable e) {
                    FileLog.e(e);
                }
                if (this.currentChat == null) {
                    return false;
                }
                MessagesController.getInstance(this.currentAccount).putChat(this.currentChat, true);
            }
            if (this.currentChat.megagroup) {
                getChannelParticipants(true);
            } else {
                this.participantsMap = null;
            }
            NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.chatInfoDidLoaded);
            this.sortedUsers = new ArrayList();
            updateOnlineCount();
            this.avatarUpdater = new AvatarUpdater();
            this.avatarUpdater.delegate = new AvatarUpdaterDelegate() {
                public void didUploadedPhoto(InputFile file, PhotoSize small, PhotoSize big) {
                    if (ProfileActivity.this.chat_id != 0) {
                        MessagesController.getInstance(ProfileActivity.this.currentAccount).changeChatAvatar(ProfileActivity.this.chat_id, file);
                    }
                }
            };
            this.avatarUpdater.parentFragment = this;
            if (ChatObject.isChannel(this.currentChat)) {
                MessagesController.getInstance(this.currentAccount).loadFullChat(this.chat_id, this.classGuid, true);
            }
        }
        if (this.dialog_id != 0) {
            DataQuery.getInstance(this.currentAccount).getMediaCount(this.dialog_id, 0, this.classGuid, true);
        } else if (this.user_id != 0) {
            DataQuery.getInstance(this.currentAccount).getMediaCount((long) this.user_id, 0, this.classGuid, true);
        } else if (this.chat_id > 0) {
            DataQuery.getInstance(this.currentAccount).getMediaCount((long) (-this.chat_id), 0, this.classGuid, true);
            if (this.mergeDialogId != 0) {
                DataQuery.getInstance(this.currentAccount).getMediaCount(this.mergeDialogId, 0, this.classGuid, true);
            }
        }
        NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.mediaCountDidLoaded);
        NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.updateInterfaces);
        NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.closeChats);
        updateRowsIds();
        return true;
    }

    public void onFragmentDestroy() {
        super.onFragmentDestroy();
        NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.mediaCountDidLoaded);
        NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.updateInterfaces);
        NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.closeChats);
        if (this.user_id != 0) {
            NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.contactsDidLoaded);
            NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.encryptedChatCreated);
            NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.encryptedChatUpdated);
            NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.blockedUsersDidLoaded);
            NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.botInfoDidLoaded);
            NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.userInfoDidLoaded);
            MessagesController.getInstance(this.currentAccount).cancelLoadFullUser(this.user_id);
            if (this.currentEncryptedChat != null) {
                NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.didReceivedNewMessages);
            }
        } else if (this.chat_id != 0) {
            NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.chatInfoDidLoaded);
            this.avatarUpdater.clear();
        }
    }

    protected ActionBar createActionBar(Context context) {
        int i;
        boolean z;
        ActionBar actionBar = new ActionBar(context) {
            public boolean onTouchEvent(MotionEvent event) {
                return super.onTouchEvent(event);
            }
        };
        if (this.user_id == 0) {
            if (!ChatObject.isChannel(this.chat_id, this.currentAccount) || this.currentChat.megagroup) {
                i = this.chat_id;
                z = false;
                actionBar.setItemsBackgroundColor(AvatarDrawable.getButtonColorForId(i), false);
                actionBar.setItemsColor(Theme.getColor(Theme.key_actionBarDefaultIcon), false);
                actionBar.setItemsColor(Theme.getColor(Theme.key_actionBarActionModeDefaultIcon), true);
                actionBar.setBackButtonDrawable(new BackDrawable(false));
                actionBar.setCastShadows(false);
                actionBar.setAddToContainer(false);
                if (VERSION.SDK_INT >= 21 && !AndroidUtilities.isTablet()) {
                    z = true;
                }
                actionBar.setOccupyStatusBar(z);
                return actionBar;
            }
        }
        i = 5;
        z = false;
        actionBar.setItemsBackgroundColor(AvatarDrawable.getButtonColorForId(i), false);
        actionBar.setItemsColor(Theme.getColor(Theme.key_actionBarDefaultIcon), false);
        actionBar.setItemsColor(Theme.getColor(Theme.key_actionBarActionModeDefaultIcon), true);
        actionBar.setBackButtonDrawable(new BackDrawable(false));
        actionBar.setCastShadows(false);
        actionBar.setAddToContainer(false);
        z = true;
        actionBar.setOccupyStatusBar(z);
        return actionBar;
    }

    public View createView(Context context) {
        int i;
        TopView topView;
        int i2;
        int a;
        float f;
        SimpleTextView simpleTextView;
        int i3;
        float f2;
        Drawable drawable;
        Drawable shadowDrawable;
        Drawable combinedDrawable;
        boolean isChannel;
        View view;
        int i4;
        StateListAnimator animator;
        Context context2 = context;
        Theme.createProfileResources(context);
        this.hasOwnBackground = true;
        this.extraHeight = AndroidUtilities.dp(88.0f);
        this.actionBar.setActionBarMenuOnItemClick(new ActionBarMenuOnItemClick() {

            class AnonymousClass2 implements OnClickListener {
                final /* synthetic */ User val$user;

                AnonymousClass2(User user) {
                    this.val$user = user;
                }

                public void onClick(DialogInterface dialogInterface, int i) {
                    ArrayList<User> arrayList = new ArrayList();
                    arrayList.add(this.val$user);
                    ContactsController.getInstance(ProfileActivity.this.currentAccount).deleteContact(arrayList);
                }
            }

            class AnonymousClass3 implements DialogsActivityDelegate {
                final /* synthetic */ User val$user;

                AnonymousClass3(User user) {
                    this.val$user = user;
                }

                public void didSelectDialogs(DialogsActivity fragment, ArrayList<Long> dids, CharSequence message, boolean param) {
                    long did = ((Long) dids.get(0)).longValue();
                    Bundle args = new Bundle();
                    args.putBoolean("scrollToTopOnResume", true);
                    args.putInt("chat_id", -((int) did));
                    if (MessagesController.getInstance(ProfileActivity.this.currentAccount).checkCanOpenChat(args, fragment)) {
                        NotificationCenter.getInstance(ProfileActivity.this.currentAccount).removeObserver(ProfileActivity.this, NotificationCenter.closeChats);
                        NotificationCenter.getInstance(ProfileActivity.this.currentAccount).postNotificationName(NotificationCenter.closeChats, new Object[0]);
                        MessagesController.getInstance(ProfileActivity.this.currentAccount).addUserToChat(-((int) did), r0.val$user, null, 0, null, ProfileActivity.this);
                        ProfileActivity.this.presentFragment(new ChatActivity(args), true);
                        ProfileActivity.this.removeSelfFromStack();
                    }
                }
            }

            public void onItemClick(int r1) {
                /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.DecodeException: Load method exception in method: org.telegram.ui.ProfileActivity.5.onItemClick(int):void
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
                r1 = r18;
                r2 = r19;
                r3 = org.telegram.ui.ProfileActivity.this;
                r3 = r3.getParentActivity();
                if (r3 != 0) goto L_0x000d;
            L_0x000c:
                return;
            L_0x000d:
                r3 = -1;
                if (r2 != r3) goto L_0x0017;
            L_0x0010:
                r3 = org.telegram.ui.ProfileActivity.this;
                r3.finishFragment();
                goto L_0x048b;
            L_0x0017:
                r3 = 0;
                r4 = 2131493127; // 0x7f0c0107 float:1.8609725E38 double:1.0530975284E-314;
                r5 = 2131494028; // 0x7f0c048c float:1.8611553E38 double:1.0530979736E-314;
                r6 = 2131492981; // 0x7f0c0075 float:1.860943E38 double:1.0530974563E-314;
                r7 = 2;
                if (r2 != r7) goto L_0x00f1;
            L_0x0024:
                r7 = org.telegram.ui.ProfileActivity.this;
                r7 = r7.currentAccount;
                r7 = org.telegram.messenger.MessagesController.getInstance(r7);
                r8 = org.telegram.ui.ProfileActivity.this;
                r8 = r8.user_id;
                r8 = java.lang.Integer.valueOf(r8);
                r7 = r7.getUser(r8);
                if (r7 != 0) goto L_0x003f;
            L_0x003e:
                return;
            L_0x003f:
                r8 = org.telegram.ui.ProfileActivity.this;
                r8 = r8.isBot;
                if (r8 != 0) goto L_0x009d;
            L_0x0047:
                r8 = new org.telegram.ui.ActionBar.AlertDialog$Builder;
                r9 = org.telegram.ui.ProfileActivity.this;
                r9 = r9.getParentActivity();
                r8.<init>(r9);
                r9 = org.telegram.ui.ProfileActivity.this;
                r9 = r9.userBlocked;
                if (r9 != 0) goto L_0x0067;
            L_0x005a:
                r9 = "AreYouSureBlockContact";
                r10 = 2131492999; // 0x7f0c0087 float:1.8609466E38 double:1.053097465E-314;
                r9 = org.telegram.messenger.LocaleController.getString(r9, r10);
                r8.setMessage(r9);
                goto L_0x0073;
            L_0x0067:
                r9 = "AreYouSureUnblockContact";
                r10 = 2131493016; // 0x7f0c0098 float:1.86095E38 double:1.0530974736E-314;
                r9 = org.telegram.messenger.LocaleController.getString(r9, r10);
                r8.setMessage(r9);
            L_0x0073:
                r9 = "AppName";
                r6 = org.telegram.messenger.LocaleController.getString(r9, r6);
                r8.setTitle(r6);
                r6 = "OK";
                r5 = org.telegram.messenger.LocaleController.getString(r6, r5);
                r6 = new org.telegram.ui.ProfileActivity$5$1;
                r6.<init>();
                r8.setPositiveButton(r5, r6);
                r5 = "Cancel";
                r4 = org.telegram.messenger.LocaleController.getString(r5, r4);
                r8.setNegativeButton(r4, r3);
                r3 = org.telegram.ui.ProfileActivity.this;
                r4 = r8.create();
                r3.showDialog(r4);
                goto L_0x00ef;
            L_0x009d:
                r3 = org.telegram.ui.ProfileActivity.this;
                r3 = r3.userBlocked;
                if (r3 != 0) goto L_0x00b9;
            L_0x00a5:
                r3 = org.telegram.ui.ProfileActivity.this;
                r3 = r3.currentAccount;
                r3 = org.telegram.messenger.MessagesController.getInstance(r3);
                r4 = org.telegram.ui.ProfileActivity.this;
                r4 = r4.user_id;
                r3.blockUser(r4);
                goto L_0x00ef;
            L_0x00b9:
                r3 = org.telegram.ui.ProfileActivity.this;
                r3 = r3.currentAccount;
                r3 = org.telegram.messenger.MessagesController.getInstance(r3);
                r4 = org.telegram.ui.ProfileActivity.this;
                r4 = r4.user_id;
                r3.unblockUser(r4);
                r3 = org.telegram.ui.ProfileActivity.this;
                r3 = r3.currentAccount;
                r8 = org.telegram.messenger.SendMessagesHelper.getInstance(r3);
                r9 = "/start";
                r3 = org.telegram.ui.ProfileActivity.this;
                r3 = r3.user_id;
                r10 = (long) r3;
                r12 = 0;
                r13 = 0;
                r14 = 0;
                r15 = 0;
                r16 = 0;
                r17 = 0;
                r8.sendMessage(r9, r10, r12, r13, r14, r15, r16, r17);
                r3 = org.telegram.ui.ProfileActivity.this;
                r3.finishFragment();
            L_0x00ef:
                goto L_0x048b;
            L_0x00f1:
                r8 = 1;
                if (r2 != r8) goto L_0x0129;
            L_0x00f4:
                r3 = org.telegram.ui.ProfileActivity.this;
                r3 = r3.currentAccount;
                r3 = org.telegram.messenger.MessagesController.getInstance(r3);
                r4 = org.telegram.ui.ProfileActivity.this;
                r4 = r4.user_id;
                r4 = java.lang.Integer.valueOf(r4);
                r3 = r3.getUser(r4);
                r4 = new android.os.Bundle;
                r4.<init>();
                r5 = "user_id";
                r6 = r3.id;
                r4.putInt(r5, r6);
                r5 = "addContact";
                r4.putBoolean(r5, r8);
                r5 = org.telegram.ui.ProfileActivity.this;
                r6 = new org.telegram.ui.ContactAddActivity;
                r6.<init>(r4);
                r5.presentFragment(r6);
                goto L_0x048b;
            L_0x0129:
                r9 = 3;
                if (r2 != r9) goto L_0x0163;
            L_0x012c:
                r3 = new android.os.Bundle;
                r3.<init>();
                r4 = "onlySelect";
                r3.putBoolean(r4, r8);
                r4 = "selectAlertString";
                r5 = "SendContactTo";
                r6 = 2131494335; // 0x7f0c05bf float:1.8612175E38 double:1.053098125E-314;
                r5 = org.telegram.messenger.LocaleController.getString(r5, r6);
                r3.putString(r4, r5);
                r4 = "selectAlertStringGroup";
                r5 = "SendContactToGroup";
                r6 = 2131494336; // 0x7f0c05c0 float:1.8612178E38 double:1.0530981257E-314;
                r5 = org.telegram.messenger.LocaleController.getString(r5, r6);
                r3.putString(r4, r5);
                r4 = new org.telegram.ui.DialogsActivity;
                r4.<init>(r3);
                r5 = org.telegram.ui.ProfileActivity.this;
                r4.setDelegate(r5);
                r5 = org.telegram.ui.ProfileActivity.this;
                r5.presentFragment(r4);
                goto L_0x048b;
            L_0x0163:
                r9 = 4;
                if (r2 != r9) goto L_0x0182;
            L_0x0166:
                r3 = new android.os.Bundle;
                r3.<init>();
                r4 = "user_id";
                r5 = org.telegram.ui.ProfileActivity.this;
                r5 = r5.user_id;
                r3.putInt(r4, r5);
                r4 = org.telegram.ui.ProfileActivity.this;
                r5 = new org.telegram.ui.ContactAddActivity;
                r5.<init>(r3);
                r4.presentFragment(r5);
                goto L_0x048b;
            L_0x0182:
                r9 = 5;
                if (r2 != r9) goto L_0x01eb;
            L_0x0185:
                r7 = org.telegram.ui.ProfileActivity.this;
                r7 = r7.currentAccount;
                r7 = org.telegram.messenger.MessagesController.getInstance(r7);
                r8 = org.telegram.ui.ProfileActivity.this;
                r8 = r8.user_id;
                r8 = java.lang.Integer.valueOf(r8);
                r7 = r7.getUser(r8);
                if (r7 == 0) goto L_0x01ea;
            L_0x019f:
                r8 = org.telegram.ui.ProfileActivity.this;
                r8 = r8.getParentActivity();
                if (r8 != 0) goto L_0x01a8;
            L_0x01a7:
                goto L_0x01ea;
            L_0x01a8:
                r8 = new org.telegram.ui.ActionBar.AlertDialog$Builder;
                r9 = org.telegram.ui.ProfileActivity.this;
                r9 = r9.getParentActivity();
                r8.<init>(r9);
                r9 = "AreYouSureDeleteContact";
                r10 = 2131493004; // 0x7f0c008c float:1.8609476E38 double:1.0530974676E-314;
                r9 = org.telegram.messenger.LocaleController.getString(r9, r10);
                r8.setMessage(r9);
                r9 = "AppName";
                r6 = org.telegram.messenger.LocaleController.getString(r9, r6);
                r8.setTitle(r6);
                r6 = "OK";
                r5 = org.telegram.messenger.LocaleController.getString(r6, r5);
                r6 = new org.telegram.ui.ProfileActivity$5$2;
                r6.<init>(r7);
                r8.setPositiveButton(r5, r6);
                r5 = "Cancel";
                r4 = org.telegram.messenger.LocaleController.getString(r5, r4);
                r8.setNegativeButton(r4, r3);
                r3 = org.telegram.ui.ProfileActivity.this;
                r4 = r8.create();
                r3.showDialog(r4);
                goto L_0x048b;
            L_0x01ea:
                return;
            L_0x01eb:
                r3 = 7;
                if (r2 != r3) goto L_0x01f5;
            L_0x01ee:
                r3 = org.telegram.ui.ProfileActivity.this;
                r3.leaveChatPressed();
                goto L_0x048b;
            L_0x01f5:
                r3 = 8;
                if (r2 != r3) goto L_0x0215;
            L_0x01f9:
                r3 = new android.os.Bundle;
                r3.<init>();
                r4 = "chat_id";
                r5 = org.telegram.ui.ProfileActivity.this;
                r5 = r5.chat_id;
                r3.putInt(r4, r5);
                r4 = org.telegram.ui.ProfileActivity.this;
                r5 = new org.telegram.ui.ChangeChatNameActivity;
                r5.<init>(r3);
                r4.presentFragment(r5);
                goto L_0x048b;
            L_0x0215:
                r3 = 12;
                if (r2 != r3) goto L_0x023e;
            L_0x0219:
                r3 = new android.os.Bundle;
                r3.<init>();
                r4 = "chat_id";
                r5 = org.telegram.ui.ProfileActivity.this;
                r5 = r5.chat_id;
                r3.putInt(r4, r5);
                r4 = new org.telegram.ui.ChannelEditActivity;
                r4.<init>(r3);
                r5 = org.telegram.ui.ProfileActivity.this;
                r5 = r5.info;
                r4.setInfo(r5);
                r5 = org.telegram.ui.ProfileActivity.this;
                r5.presentFragment(r4);
                goto L_0x048b;
            L_0x023e:
                r3 = 9;
                r4 = 0;
                if (r2 != r3) goto L_0x029b;
            L_0x0243:
                r3 = org.telegram.ui.ProfileActivity.this;
                r3 = r3.currentAccount;
                r3 = org.telegram.messenger.MessagesController.getInstance(r3);
                r5 = org.telegram.ui.ProfileActivity.this;
                r5 = r5.user_id;
                r5 = java.lang.Integer.valueOf(r5);
                r3 = r3.getUser(r5);
                if (r3 != 0) goto L_0x025e;
            L_0x025d:
                return;
            L_0x025e:
                r5 = new android.os.Bundle;
                r5.<init>();
                r6 = "onlySelect";
                r5.putBoolean(r6, r8);
                r6 = "dialogsType";
                r5.putInt(r6, r7);
                r6 = "addToGroupAlertString";
                r9 = "AddToTheGroupTitle";
                r10 = 2131492946; // 0x7f0c0052 float:1.8609358E38 double:1.053097439E-314;
                r7 = new java.lang.Object[r7];
                r11 = org.telegram.messenger.UserObject.getUserName(r3);
                r7[r4] = r11;
                r4 = "%1$s";
                r7[r8] = r4;
                r4 = org.telegram.messenger.LocaleController.formatString(r9, r10, r7);
                r5.putString(r6, r4);
                r4 = new org.telegram.ui.DialogsActivity;
                r4.<init>(r5);
                r6 = new org.telegram.ui.ProfileActivity$5$3;
                r6.<init>(r3);
                r4.setDelegate(r6);
                r6 = org.telegram.ui.ProfileActivity.this;
                r6.presentFragment(r4);
                goto L_0x048b;
            L_0x029b:
                r3 = 10;
                if (r2 != r3) goto L_0x0371;
            L_0x029f:
                r3 = org.telegram.ui.ProfileActivity.this;	 Catch:{ Exception -> 0x036a }
                r3 = r3.currentAccount;	 Catch:{ Exception -> 0x036a }
                r3 = org.telegram.messenger.MessagesController.getInstance(r3);	 Catch:{ Exception -> 0x036a }
                r5 = org.telegram.ui.ProfileActivity.this;	 Catch:{ Exception -> 0x036a }
                r5 = r5.user_id;	 Catch:{ Exception -> 0x036a }
                r5 = java.lang.Integer.valueOf(r5);	 Catch:{ Exception -> 0x036a }
                r3 = r3.getUser(r5);	 Catch:{ Exception -> 0x036a }
                if (r3 != 0) goto L_0x02ba;	 Catch:{ Exception -> 0x036a }
            L_0x02b9:
                return;	 Catch:{ Exception -> 0x036a }
            L_0x02ba:
                r5 = new android.content.Intent;	 Catch:{ Exception -> 0x036a }
                r6 = "android.intent.action.SEND";	 Catch:{ Exception -> 0x036a }
                r5.<init>(r6);	 Catch:{ Exception -> 0x036a }
                r6 = "text/plain";	 Catch:{ Exception -> 0x036a }
                r5.setType(r6);	 Catch:{ Exception -> 0x036a }
                r6 = org.telegram.ui.ProfileActivity.this;	 Catch:{ Exception -> 0x036a }
                r6 = r6.currentAccount;	 Catch:{ Exception -> 0x036a }
                r6 = org.telegram.messenger.MessagesController.getInstance(r6);	 Catch:{ Exception -> 0x036a }
                r9 = org.telegram.ui.ProfileActivity.this;	 Catch:{ Exception -> 0x036a }
                r9 = r9.botInfo;	 Catch:{ Exception -> 0x036a }
                r9 = r9.user_id;	 Catch:{ Exception -> 0x036a }
                r6 = r6.getUserFull(r9);	 Catch:{ Exception -> 0x036a }
                r9 = org.telegram.ui.ProfileActivity.this;	 Catch:{ Exception -> 0x036a }
                r9 = r9.botInfo;	 Catch:{ Exception -> 0x036a }
                if (r9 == 0) goto L_0x0324;	 Catch:{ Exception -> 0x036a }
            L_0x02e4:
                if (r6 == 0) goto L_0x0324;	 Catch:{ Exception -> 0x036a }
            L_0x02e6:
                r9 = r6.about;	 Catch:{ Exception -> 0x036a }
                r9 = android.text.TextUtils.isEmpty(r9);	 Catch:{ Exception -> 0x036a }
                if (r9 != 0) goto L_0x0324;	 Catch:{ Exception -> 0x036a }
            L_0x02ee:
                r9 = "android.intent.extra.TEXT";	 Catch:{ Exception -> 0x036a }
                r10 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x036a }
                r10.<init>();	 Catch:{ Exception -> 0x036a }
                r11 = "%s https://";	 Catch:{ Exception -> 0x036a }
                r10.append(r11);	 Catch:{ Exception -> 0x036a }
                r11 = org.telegram.ui.ProfileActivity.this;	 Catch:{ Exception -> 0x036a }
                r11 = r11.currentAccount;	 Catch:{ Exception -> 0x036a }
                r11 = org.telegram.messenger.MessagesController.getInstance(r11);	 Catch:{ Exception -> 0x036a }
                r11 = r11.linkPrefix;	 Catch:{ Exception -> 0x036a }
                r10.append(r11);	 Catch:{ Exception -> 0x036a }
                r11 = "/%s";	 Catch:{ Exception -> 0x036a }
                r10.append(r11);	 Catch:{ Exception -> 0x036a }
                r10 = r10.toString();	 Catch:{ Exception -> 0x036a }
                r7 = new java.lang.Object[r7];	 Catch:{ Exception -> 0x036a }
                r11 = r6.about;	 Catch:{ Exception -> 0x036a }
                r7[r4] = r11;	 Catch:{ Exception -> 0x036a }
                r4 = r3.username;	 Catch:{ Exception -> 0x036a }
                r7[r8] = r4;	 Catch:{ Exception -> 0x036a }
                r4 = java.lang.String.format(r10, r7);	 Catch:{ Exception -> 0x036a }
                r5.putExtra(r9, r4);	 Catch:{ Exception -> 0x036a }
                goto L_0x0355;	 Catch:{ Exception -> 0x036a }
            L_0x0324:
                r7 = "android.intent.extra.TEXT";	 Catch:{ Exception -> 0x036a }
                r9 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x036a }
                r9.<init>();	 Catch:{ Exception -> 0x036a }
                r10 = "https://";	 Catch:{ Exception -> 0x036a }
                r9.append(r10);	 Catch:{ Exception -> 0x036a }
                r10 = org.telegram.ui.ProfileActivity.this;	 Catch:{ Exception -> 0x036a }
                r10 = r10.currentAccount;	 Catch:{ Exception -> 0x036a }
                r10 = org.telegram.messenger.MessagesController.getInstance(r10);	 Catch:{ Exception -> 0x036a }
                r10 = r10.linkPrefix;	 Catch:{ Exception -> 0x036a }
                r9.append(r10);	 Catch:{ Exception -> 0x036a }
                r10 = "/%s";	 Catch:{ Exception -> 0x036a }
                r9.append(r10);	 Catch:{ Exception -> 0x036a }
                r9 = r9.toString();	 Catch:{ Exception -> 0x036a }
                r8 = new java.lang.Object[r8];	 Catch:{ Exception -> 0x036a }
                r10 = r3.username;	 Catch:{ Exception -> 0x036a }
                r8[r4] = r10;	 Catch:{ Exception -> 0x036a }
                r4 = java.lang.String.format(r9, r8);	 Catch:{ Exception -> 0x036a }
                r5.putExtra(r7, r4);	 Catch:{ Exception -> 0x036a }
            L_0x0355:
                r4 = org.telegram.ui.ProfileActivity.this;	 Catch:{ Exception -> 0x036a }
                r7 = "BotShare";	 Catch:{ Exception -> 0x036a }
                r8 = 2131493094; // 0x7f0c00e6 float:1.8609658E38 double:1.053097512E-314;	 Catch:{ Exception -> 0x036a }
                r7 = org.telegram.messenger.LocaleController.getString(r7, r8);	 Catch:{ Exception -> 0x036a }
                r7 = android.content.Intent.createChooser(r5, r7);	 Catch:{ Exception -> 0x036a }
                r8 = 500; // 0x1f4 float:7.0E-43 double:2.47E-321;	 Catch:{ Exception -> 0x036a }
                r4.startActivityForResult(r7, r8);	 Catch:{ Exception -> 0x036a }
                goto L_0x036f;
            L_0x036a:
                r0 = move-exception;
                r3 = r0;
                org.telegram.messenger.FileLog.e(r3);
            L_0x036f:
                goto L_0x048b;
            L_0x0371:
                r3 = 11;
                if (r2 != r3) goto L_0x039a;
            L_0x0375:
                r3 = new android.os.Bundle;
                r3.<init>();
                r4 = "chat_id";
                r5 = org.telegram.ui.ProfileActivity.this;
                r5 = r5.chat_id;
                r3.putInt(r4, r5);
                r4 = new org.telegram.ui.SetAdminsActivity;
                r4.<init>(r3);
                r5 = org.telegram.ui.ProfileActivity.this;
                r5 = r5.info;
                r4.setChatInfo(r5);
                r5 = org.telegram.ui.ProfileActivity.this;
                r5.presentFragment(r4);
                goto L_0x048b;
            L_0x039a:
                r3 = 13;
                if (r2 != r3) goto L_0x03ba;
            L_0x039e:
                r3 = new android.os.Bundle;
                r3.<init>();
                r4 = "chat_id";
                r5 = org.telegram.ui.ProfileActivity.this;
                r5 = r5.chat_id;
                r3.putInt(r4, r5);
                r4 = org.telegram.ui.ProfileActivity.this;
                r5 = new org.telegram.ui.ConvertGroupActivity;
                r5.<init>(r3);
                r4.presentFragment(r5);
                goto L_0x048b;
            L_0x03ba:
                r3 = 14;
                if (r2 != r3) goto L_0x040b;
            L_0x03be:
                r3 = org.telegram.ui.ProfileActivity.this;	 Catch:{ Exception -> 0x0404 }
                r3 = r3.currentEncryptedChat;	 Catch:{ Exception -> 0x0404 }
                if (r3 == 0) goto L_0x03d3;	 Catch:{ Exception -> 0x0404 }
            L_0x03c6:
                r3 = org.telegram.ui.ProfileActivity.this;	 Catch:{ Exception -> 0x0404 }
                r3 = r3.currentEncryptedChat;	 Catch:{ Exception -> 0x0404 }
                r3 = r3.id;	 Catch:{ Exception -> 0x0404 }
                r3 = (long) r3;	 Catch:{ Exception -> 0x0404 }
                r5 = 32;	 Catch:{ Exception -> 0x0404 }
                r3 = r3 << r5;	 Catch:{ Exception -> 0x0404 }
                goto L_0x03f4;	 Catch:{ Exception -> 0x0404 }
            L_0x03d3:
                r3 = org.telegram.ui.ProfileActivity.this;	 Catch:{ Exception -> 0x0404 }
                r3 = r3.user_id;	 Catch:{ Exception -> 0x0404 }
                if (r3 == 0) goto L_0x03e3;	 Catch:{ Exception -> 0x0404 }
                r3 = org.telegram.ui.ProfileActivity.this;	 Catch:{ Exception -> 0x0404 }
                r3 = r3.user_id;	 Catch:{ Exception -> 0x0404 }
                r3 = (long) r3;	 Catch:{ Exception -> 0x0404 }
                goto L_0x03d2;	 Catch:{ Exception -> 0x0404 }
                r3 = org.telegram.ui.ProfileActivity.this;	 Catch:{ Exception -> 0x0404 }
                r3 = r3.chat_id;	 Catch:{ Exception -> 0x0404 }
                if (r3 == 0) goto L_0x0403;	 Catch:{ Exception -> 0x0404 }
                r3 = org.telegram.ui.ProfileActivity.this;	 Catch:{ Exception -> 0x0404 }
                r3 = r3.chat_id;	 Catch:{ Exception -> 0x0404 }
                r3 = -r3;	 Catch:{ Exception -> 0x0404 }
                r3 = (long) r3;	 Catch:{ Exception -> 0x0404 }
                goto L_0x03d2;	 Catch:{ Exception -> 0x0404 }
                r5 = org.telegram.ui.ProfileActivity.this;	 Catch:{ Exception -> 0x0404 }
                r5 = r5.currentAccount;	 Catch:{ Exception -> 0x0404 }
                r5 = org.telegram.messenger.DataQuery.getInstance(r5);	 Catch:{ Exception -> 0x0404 }
                r5.installShortcut(r3);	 Catch:{ Exception -> 0x0404 }
                goto L_0x0409;
                return;
            L_0x0404:
                r0 = move-exception;
                r3 = r0;
                org.telegram.messenger.FileLog.e(r3);
                goto L_0x048b;
            L_0x040b:
                r3 = 15;
                if (r2 != r3) goto L_0x0443;
                r3 = org.telegram.ui.ProfileActivity.this;
                r3 = r3.currentAccount;
                r3 = org.telegram.messenger.MessagesController.getInstance(r3);
                r4 = org.telegram.ui.ProfileActivity.this;
                r4 = r4.user_id;
                r4 = java.lang.Integer.valueOf(r4);
                r3 = r3.getUser(r4);
                if (r3 == 0) goto L_0x0442;
                r4 = org.telegram.ui.ProfileActivity.this;
                r4 = r4.getParentActivity();
                r5 = org.telegram.ui.ProfileActivity.this;
                r5 = r5.currentAccount;
                r5 = org.telegram.messenger.MessagesController.getInstance(r5);
                r6 = r3.id;
                r5 = r5.getUserFull(r6);
                org.telegram.ui.Components.voip.VoIPHelper.startCall(r3, r4, r5);
                goto L_0x048b;
                r3 = 16;
                if (r2 != r3) goto L_0x048b;
                r3 = new android.os.Bundle;
                r3.<init>();
                r4 = "chat_id";
                r5 = org.telegram.ui.ProfileActivity.this;
                r5 = r5.chat_id;
                r3.putInt(r4, r5);
                r4 = org.telegram.ui.ProfileActivity.this;
                r4 = r4.currentChat;
                r4 = org.telegram.messenger.ChatObject.isChannel(r4);
                if (r4 == 0) goto L_0x0478;
                r4 = "type";
                r3.putInt(r4, r7);
                r4 = "open_search";
                r3.putBoolean(r4, r8);
                r4 = org.telegram.ui.ProfileActivity.this;
                r5 = new org.telegram.ui.ChannelUsersActivity;
                r5.<init>(r3);
                r4.presentFragment(r5);
                goto L_0x048b;
                r4 = new org.telegram.ui.ChatUsersActivity;
                r4.<init>(r3);
                r5 = org.telegram.ui.ProfileActivity.this;
                r5 = r5.info;
                r4.setInfo(r5);
                r5 = org.telegram.ui.ProfileActivity.this;
                r5.presentFragment(r4);
            L_0x048b:
                return;
                */
                throw new UnsupportedOperationException("Method not decompiled: org.telegram.ui.ProfileActivity.5.onItemClick(int):void");
            }
        });
        createActionBarMenu();
        this.listAdapter = new ListAdapter(context2);
        this.avatarDrawable = new AvatarDrawable();
        this.avatarDrawable.setProfile(true);
        this.fragmentView = new FrameLayout(context2) {
            public boolean hasOverlappingRendering() {
                return false;
            }

            protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
                super.onLayout(changed, left, top, right, bottom);
                ProfileActivity.this.checkListViewScroll();
            }
        };
        FrameLayout frameLayout = this.fragmentView;
        this.listView = new RecyclerListView(context2) {
            public boolean hasOverlappingRendering() {
                return false;
            }
        };
        this.listView.setTag(Integer.valueOf(6));
        this.listView.setBackgroundColor(Theme.getColor(Theme.key_windowBackgroundWhite));
        this.listView.setVerticalScrollBarEnabled(false);
        this.listView.setItemAnimator(null);
        this.listView.setLayoutAnimation(null);
        this.listView.setClipToPadding(false);
        this.layoutManager = new LinearLayoutManager(context2) {
            public boolean supportsPredictiveItemAnimations() {
                return false;
            }
        };
        this.layoutManager.setOrientation(1);
        this.listView.setLayoutManager(this.layoutManager);
        RecyclerListView recyclerListView = this.listView;
        if (this.user_id == 0) {
            if (!ChatObject.isChannel(r0.chat_id, r0.currentAccount) || r0.currentChat.megagroup) {
                i = r0.chat_id;
                recyclerListView.setGlowColor(AvatarDrawable.getProfileBackColorForId(i));
                frameLayout.addView(r0.listView, LayoutHelper.createFrame(-1, -1, 51));
                r0.listView.setAdapter(r0.listAdapter);
                r0.listView.setOnItemClickListener(new OnItemClickListener() {
                    public void onItemClick(View view, int position) {
                        int i = position;
                        if (ProfileActivity.this.getParentActivity() != null) {
                            Bundle args;
                            if (i == ProfileActivity.this.sharedMediaRow) {
                                args = new Bundle();
                                if (ProfileActivity.this.user_id != 0) {
                                    args.putLong("dialog_id", ProfileActivity.this.dialog_id != 0 ? ProfileActivity.this.dialog_id : (long) ProfileActivity.this.user_id);
                                } else {
                                    args.putLong("dialog_id", (long) (-ProfileActivity.this.chat_id));
                                }
                                MediaActivity fragment = new MediaActivity(args);
                                fragment.setChatInfo(ProfileActivity.this.info);
                                ProfileActivity.this.presentFragment(fragment);
                            } else if (i == ProfileActivity.this.groupsInCommonRow) {
                                ProfileActivity.this.presentFragment(new CommonGroupsActivity(ProfileActivity.this.user_id));
                            } else if (i == ProfileActivity.this.settingsKeyRow) {
                                args = new Bundle();
                                args.putInt("chat_id", (int) (ProfileActivity.this.dialog_id >> 32));
                                ProfileActivity.this.presentFragment(new IdenticonActivity(args));
                            } else if (i == ProfileActivity.this.settingsTimerRow) {
                                ProfileActivity.this.showDialog(AlertsCreator.createTTLAlert(ProfileActivity.this.getParentActivity(), ProfileActivity.this.currentEncryptedChat).create());
                            } else {
                                int i2 = 1;
                                if (i == ProfileActivity.this.settingsNotificationsRow) {
                                    long did;
                                    String[] strArr;
                                    String[] descriptions;
                                    int[] icons;
                                    LinearLayout linearLayout;
                                    int a;
                                    TextView textView;
                                    Drawable drawable;
                                    Builder builder;
                                    if (ProfileActivity.this.dialog_id != 0) {
                                        did = ProfileActivity.this.dialog_id;
                                    } else if (ProfileActivity.this.user_id != 0) {
                                        did = (long) ProfileActivity.this.user_id;
                                    } else {
                                        did = (long) (-ProfileActivity.this.chat_id);
                                        strArr = new String[5];
                                        strArr[0] = LocaleController.getString("NotificationsTurnOn", R.string.NotificationsTurnOn);
                                        strArr[1] = LocaleController.formatString("MuteFor", R.string.MuteFor, LocaleController.formatPluralString("Hours", 1));
                                        strArr[2] = LocaleController.formatString("MuteFor", R.string.MuteFor, LocaleController.formatPluralString("Days", 2));
                                        strArr[3] = LocaleController.getString("NotificationsCustomize", R.string.NotificationsCustomize);
                                        strArr[4] = LocaleController.getString("NotificationsTurnOff", R.string.NotificationsTurnOff);
                                        descriptions = strArr;
                                        icons = new int[]{R.drawable.notifications_s_on, R.drawable.notifications_s_1h, R.drawable.notifications_s_2d, R.drawable.notifications_s_custom, R.drawable.notifications_s_off};
                                        linearLayout = new LinearLayout(ProfileActivity.this.getParentActivity());
                                        linearLayout.setOrientation(1);
                                        a = 0;
                                        while (a < descriptions.length) {
                                            textView = new TextView(ProfileActivity.this.getParentActivity());
                                            textView.setTextColor(Theme.getColor(Theme.key_dialogTextBlack));
                                            textView.setTextSize(i2, 16.0f);
                                            textView.setLines(i2);
                                            textView.setMaxLines(i2);
                                            drawable = ProfileActivity.this.getParentActivity().getResources().getDrawable(icons[a]);
                                            drawable.setColorFilter(new PorterDuffColorFilter(Theme.getColor(Theme.key_dialogIcon), Mode.MULTIPLY));
                                            textView.setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null);
                                            textView.setTag(Integer.valueOf(a));
                                            textView.setBackgroundDrawable(Theme.getSelectorDrawable(false));
                                            textView.setPadding(AndroidUtilities.dp(24.0f), 0, AndroidUtilities.dp(24.0f), 0);
                                            textView.setSingleLine(true);
                                            textView.setGravity(19);
                                            textView.setCompoundDrawablePadding(AndroidUtilities.dp(26.0f));
                                            textView.setText(descriptions[a]);
                                            linearLayout.addView(textView, LayoutHelper.createLinear(-1, 48, 51));
                                            textView.setOnClickListener(new View.OnClickListener() {
                                                public void onClick(View v) {
                                                    int i = ((Integer) v.getTag()).intValue();
                                                    if (i == 0) {
                                                        Editor editor = MessagesController.getNotificationsSettings(ProfileActivity.this.currentAccount).edit();
                                                        StringBuilder stringBuilder = new StringBuilder();
                                                        stringBuilder.append("notify2_");
                                                        stringBuilder.append(did);
                                                        editor.putInt(stringBuilder.toString(), 0);
                                                        MessagesStorage.getInstance(ProfileActivity.this.currentAccount).setDialogFlags(did, 0);
                                                        editor.commit();
                                                        TL_dialog dialog = (TL_dialog) MessagesController.getInstance(ProfileActivity.this.currentAccount).dialogs_dict.get(did);
                                                        if (dialog != null) {
                                                            dialog.notify_settings = new TL_peerNotifySettings();
                                                        }
                                                        NotificationsController.getInstance(ProfileActivity.this.currentAccount).updateServerNotificationsSettings(did);
                                                    } else if (i == 3) {
                                                        Bundle args = new Bundle();
                                                        args.putLong("dialog_id", did);
                                                        ProfileActivity.this.presentFragment(new ProfileNotificationsActivity(args));
                                                    } else {
                                                        long flags;
                                                        int untilTime = ConnectionsManager.getInstance(ProfileActivity.this.currentAccount).getCurrentTime();
                                                        if (i == 1) {
                                                            untilTime += 3600;
                                                        } else if (i == 2) {
                                                            untilTime += 172800;
                                                        } else if (i == 4) {
                                                            untilTime = ConnectionsManager.DEFAULT_DATACENTER_ID;
                                                        }
                                                        Editor editor2 = MessagesController.getNotificationsSettings(ProfileActivity.this.currentAccount).edit();
                                                        StringBuilder stringBuilder2;
                                                        if (i == 4) {
                                                            stringBuilder2 = new StringBuilder();
                                                            stringBuilder2.append("notify2_");
                                                            stringBuilder2.append(did);
                                                            editor2.putInt(stringBuilder2.toString(), 2);
                                                            flags = 1;
                                                        } else {
                                                            StringBuilder stringBuilder3 = new StringBuilder();
                                                            stringBuilder3.append("notify2_");
                                                            stringBuilder3.append(did);
                                                            editor2.putInt(stringBuilder3.toString(), 3);
                                                            stringBuilder2 = new StringBuilder();
                                                            stringBuilder2.append("notifyuntil_");
                                                            stringBuilder2.append(did);
                                                            editor2.putInt(stringBuilder2.toString(), untilTime);
                                                            flags = (((long) untilTime) << 32) | 1;
                                                        }
                                                        NotificationsController.getInstance(ProfileActivity.this.currentAccount).removeNotificationsForDialog(did);
                                                        MessagesStorage.getInstance(ProfileActivity.this.currentAccount).setDialogFlags(did, flags);
                                                        editor2.commit();
                                                        TL_dialog dialog2 = (TL_dialog) MessagesController.getInstance(ProfileActivity.this.currentAccount).dialogs_dict.get(did);
                                                        if (dialog2 != null) {
                                                            dialog2.notify_settings = new TL_peerNotifySettings();
                                                            dialog2.notify_settings.mute_until = untilTime;
                                                        }
                                                        NotificationsController.getInstance(ProfileActivity.this.currentAccount).updateServerNotificationsSettings(did);
                                                    }
                                                    ProfileActivity.this.listAdapter.notifyItemChanged(ProfileActivity.this.settingsNotificationsRow);
                                                    ProfileActivity.this.dismissCurrentDialig();
                                                }
                                            });
                                            a++;
                                            i2 = 1;
                                        }
                                        builder = new Builder(ProfileActivity.this.getParentActivity());
                                        builder.setTitle(LocaleController.getString("Notifications", R.string.Notifications));
                                        builder.setView(linearLayout);
                                        ProfileActivity.this.showDialog(builder.create());
                                    }
                                    strArr = new String[5];
                                    strArr[0] = LocaleController.getString("NotificationsTurnOn", R.string.NotificationsTurnOn);
                                    strArr[1] = LocaleController.formatString("MuteFor", R.string.MuteFor, LocaleController.formatPluralString("Hours", 1));
                                    strArr[2] = LocaleController.formatString("MuteFor", R.string.MuteFor, LocaleController.formatPluralString("Days", 2));
                                    strArr[3] = LocaleController.getString("NotificationsCustomize", R.string.NotificationsCustomize);
                                    strArr[4] = LocaleController.getString("NotificationsTurnOff", R.string.NotificationsTurnOff);
                                    descriptions = strArr;
                                    icons = new int[]{R.drawable.notifications_s_on, R.drawable.notifications_s_1h, R.drawable.notifications_s_2d, R.drawable.notifications_s_custom, R.drawable.notifications_s_off};
                                    linearLayout = new LinearLayout(ProfileActivity.this.getParentActivity());
                                    linearLayout.setOrientation(1);
                                    a = 0;
                                    while (a < descriptions.length) {
                                        textView = new TextView(ProfileActivity.this.getParentActivity());
                                        textView.setTextColor(Theme.getColor(Theme.key_dialogTextBlack));
                                        textView.setTextSize(i2, 16.0f);
                                        textView.setLines(i2);
                                        textView.setMaxLines(i2);
                                        drawable = ProfileActivity.this.getParentActivity().getResources().getDrawable(icons[a]);
                                        drawable.setColorFilter(new PorterDuffColorFilter(Theme.getColor(Theme.key_dialogIcon), Mode.MULTIPLY));
                                        textView.setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null);
                                        textView.setTag(Integer.valueOf(a));
                                        textView.setBackgroundDrawable(Theme.getSelectorDrawable(false));
                                        textView.setPadding(AndroidUtilities.dp(24.0f), 0, AndroidUtilities.dp(24.0f), 0);
                                        textView.setSingleLine(true);
                                        textView.setGravity(19);
                                        textView.setCompoundDrawablePadding(AndroidUtilities.dp(26.0f));
                                        textView.setText(descriptions[a]);
                                        linearLayout.addView(textView, LayoutHelper.createLinear(-1, 48, 51));
                                        textView.setOnClickListener(/* anonymous class already generated */);
                                        a++;
                                        i2 = 1;
                                    }
                                    builder = new Builder(ProfileActivity.this.getParentActivity());
                                    builder.setTitle(LocaleController.getString("Notifications", R.string.Notifications));
                                    builder.setView(linearLayout);
                                    ProfileActivity.this.showDialog(builder.create());
                                } else if (i == ProfileActivity.this.startSecretChatRow) {
                                    builder = new Builder(ProfileActivity.this.getParentActivity());
                                    builder.setMessage(LocaleController.getString("AreYouSureSecretChat", R.string.AreYouSureSecretChat));
                                    builder.setTitle(LocaleController.getString("AppName", R.string.AppName));
                                    builder.setPositiveButton(LocaleController.getString("OK", R.string.OK), new OnClickListener() {
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            ProfileActivity.this.creatingChat = true;
                                            SecretChatHelper.getInstance(ProfileActivity.this.currentAccount).startSecretChat(ProfileActivity.this.getParentActivity(), MessagesController.getInstance(ProfileActivity.this.currentAccount).getUser(Integer.valueOf(ProfileActivity.this.user_id)));
                                        }
                                    });
                                    builder.setNegativeButton(LocaleController.getString("Cancel", R.string.Cancel), null);
                                    ProfileActivity.this.showDialog(builder.create());
                                } else if (i > ProfileActivity.this.emptyRowChat2 && i < ProfileActivity.this.membersEndRow) {
                                    int user_id;
                                    if (ProfileActivity.this.sortedUsers.isEmpty()) {
                                        user_id = ((ChatParticipant) ProfileActivity.this.info.participants.participants.get((i - ProfileActivity.this.emptyRowChat2) - 1)).user_id;
                                    } else {
                                        user_id = ((ChatParticipant) ProfileActivity.this.info.participants.participants.get(((Integer) ProfileActivity.this.sortedUsers.get((i - ProfileActivity.this.emptyRowChat2) - 1)).intValue())).user_id;
                                    }
                                    if (user_id != UserConfig.getInstance(ProfileActivity.this.currentAccount).getClientUserId()) {
                                        Bundle args2 = new Bundle();
                                        args2.putInt("user_id", user_id);
                                        ProfileActivity.this.presentFragment(new ProfileActivity(args2));
                                    }
                                } else if (i == ProfileActivity.this.addMemberRow) {
                                    ProfileActivity.this.openAddMember();
                                } else if (i == ProfileActivity.this.channelNameRow) {
                                    try {
                                        Intent intent = new Intent("android.intent.action.SEND");
                                        intent.setType("text/plain");
                                        StringBuilder stringBuilder;
                                        if (ProfileActivity.this.info.about == null || ProfileActivity.this.info.about.length() <= 0) {
                                            stringBuilder = new StringBuilder();
                                            stringBuilder.append(ProfileActivity.this.currentChat.title);
                                            stringBuilder.append("\nhttps://");
                                            stringBuilder.append(MessagesController.getInstance(ProfileActivity.this.currentAccount).linkPrefix);
                                            stringBuilder.append("/");
                                            stringBuilder.append(ProfileActivity.this.currentChat.username);
                                            intent.putExtra("android.intent.extra.TEXT", stringBuilder.toString());
                                        } else {
                                            stringBuilder = new StringBuilder();
                                            stringBuilder.append(ProfileActivity.this.currentChat.title);
                                            stringBuilder.append("\n");
                                            stringBuilder.append(ProfileActivity.this.info.about);
                                            stringBuilder.append("\nhttps://");
                                            stringBuilder.append(MessagesController.getInstance(ProfileActivity.this.currentAccount).linkPrefix);
                                            stringBuilder.append("/");
                                            stringBuilder.append(ProfileActivity.this.currentChat.username);
                                            intent.putExtra("android.intent.extra.TEXT", stringBuilder.toString());
                                        }
                                        ProfileActivity.this.getParentActivity().startActivityForResult(Intent.createChooser(intent, LocaleController.getString("BotShare", R.string.BotShare)), 500);
                                    } catch (Throwable e) {
                                        FileLog.e(e);
                                    }
                                } else if (i == ProfileActivity.this.leaveChannelRow) {
                                    ProfileActivity.this.leaveChatPressed();
                                } else if (i == ProfileActivity.this.membersRow) {
                                    args = new Bundle();
                                    args.putInt("chat_id", ProfileActivity.this.chat_id);
                                    args.putInt("type", 2);
                                    ProfileActivity.this.presentFragment(new ChannelUsersActivity(args));
                                } else if (i == ProfileActivity.this.convertRow) {
                                    builder = new Builder(ProfileActivity.this.getParentActivity());
                                    builder.setMessage(LocaleController.getString("ConvertGroupAlert", R.string.ConvertGroupAlert));
                                    builder.setTitle(LocaleController.getString("ConvertGroupAlertWarning", R.string.ConvertGroupAlertWarning));
                                    builder.setPositiveButton(LocaleController.getString("OK", R.string.OK), new OnClickListener() {
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            MessagesController.getInstance(ProfileActivity.this.currentAccount).convertToMegaGroup(ProfileActivity.this.getParentActivity(), ProfileActivity.this.chat_id);
                                        }
                                    });
                                    builder.setNegativeButton(LocaleController.getString("Cancel", R.string.Cancel), null);
                                    ProfileActivity.this.showDialog(builder.create());
                                } else {
                                    ProfileActivity.this.processOnClickOrPress(i);
                                }
                            }
                        }
                    }
                });
                r0.listView.setOnItemLongClickListener(new OnItemLongClickListener() {
                    public boolean onItemClick(View view, int position) {
                        int i = position;
                        if (i <= ProfileActivity.this.emptyRowChat2 || i >= ProfileActivity.this.membersEndRow) {
                            return ProfileActivity.this.processOnClickOrPress(i);
                        }
                        if (ProfileActivity.this.getParentActivity() == null) {
                            return false;
                        }
                        ChatParticipant user;
                        ChannelParticipant channelParticipant;
                        boolean allowKick = false;
                        boolean allowSetAdmin = false;
                        boolean canEditAdmin = false;
                        if (ProfileActivity.this.sortedUsers.isEmpty()) {
                            user = (ChatParticipant) ProfileActivity.this.info.participants.participants.get((i - ProfileActivity.this.emptyRowChat2) - 1);
                        } else {
                            user = (ChatParticipant) ProfileActivity.this.info.participants.participants.get(((Integer) ProfileActivity.this.sortedUsers.get((i - ProfileActivity.this.emptyRowChat2) - 1)).intValue());
                        }
                        ProfileActivity.this.selectedUser = user.user_id;
                        if (ChatObject.isChannel(ProfileActivity.this.currentChat)) {
                            channelParticipant = ((TL_chatChannelParticipant) user).channelParticipant;
                            if (user.user_id == UserConfig.getInstance(ProfileActivity.this.currentAccount).getClientUserId()) {
                                return false;
                            }
                            boolean z;
                            User u = MessagesController.getInstance(ProfileActivity.this.currentAccount).getUser(Integer.valueOf(user.user_id));
                            if (!(channelParticipant instanceof TL_channelParticipant)) {
                                if (!(channelParticipant instanceof TL_channelParticipantBanned)) {
                                    z = false;
                                    allowSetAdmin = z;
                                    z = ((channelParticipant instanceof TL_channelParticipantAdmin) && !(channelParticipant instanceof TL_channelParticipantCreator)) || channelParticipant.can_edit;
                                    canEditAdmin = z;
                                }
                            }
                            z = true;
                            allowSetAdmin = z;
                            if (channelParticipant instanceof TL_channelParticipantAdmin) {
                            }
                            canEditAdmin = z;
                        } else {
                            channelParticipant = null;
                            if (user.user_id != UserConfig.getInstance(ProfileActivity.this.currentAccount).getClientUserId()) {
                                if (ProfileActivity.this.currentChat.creator) {
                                    allowKick = true;
                                } else if ((user instanceof TL_chatParticipant) && ((ProfileActivity.this.currentChat.admin && ProfileActivity.this.currentChat.admins_enabled) || user.inviter_id == UserConfig.getInstance(ProfileActivity.this.currentAccount).getClientUserId())) {
                                    allowKick = true;
                                }
                            }
                            if (!allowKick) {
                                return false;
                            }
                        }
                        Builder builder = new Builder(ProfileActivity.this.getParentActivity());
                        ArrayList<String> items = new ArrayList();
                        final ArrayList<Integer> actions = new ArrayList();
                        boolean z2 = ProfileActivity.this.currentChat.megagroup;
                        int i2 = R.string.KickFromGroup;
                        if (z2) {
                            if (allowSetAdmin && ChatObject.canAddAdmins(ProfileActivity.this.currentChat)) {
                                items.add(LocaleController.getString("SetAsAdmin", R.string.SetAsAdmin));
                                actions.add(Integer.valueOf(0));
                            }
                            if (ChatObject.canBlockUsers(ProfileActivity.this.currentChat) && canEditAdmin) {
                                items.add(LocaleController.getString("KickFromSupergroup", R.string.KickFromSupergroup));
                                actions.add(Integer.valueOf(1));
                                items.add(LocaleController.getString("KickFromGroup", R.string.KickFromGroup));
                                actions.add(Integer.valueOf(2));
                            }
                        } else {
                            String str;
                            if (ProfileActivity.this.chat_id > 0) {
                                str = "KickFromGroup";
                            } else {
                                str = "KickFromBroadcast";
                                i2 = R.string.KickFromBroadcast;
                            }
                            items.add(LocaleController.getString(str, i2));
                            actions.add(Integer.valueOf(2));
                        }
                        if (items.isEmpty()) {
                            return false;
                        }
                        builder.setItems((CharSequence[]) items.toArray(new CharSequence[items.size()]), new OnClickListener() {
                            public void onClick(DialogInterface dialogInterface, final int i) {
                                if (((Integer) actions.get(i)).intValue() == 2) {
                                    ProfileActivity.this.kickUser(ProfileActivity.this.selectedUser);
                                    return;
                                }
                                ChannelRightsEditActivity channelRightsEditActivity = new ChannelRightsEditActivity(user.user_id, ProfileActivity.this.chat_id, channelParticipant.admin_rights, channelParticipant.banned_rights, ((Integer) actions.get(i)).intValue(), true);
                                channelRightsEditActivity.setDelegate(new ChannelRightsEditActivityDelegate() {
                                    public void didSetRights(int rights, TL_channelAdminRights rightsAdmin, TL_channelBannedRights rightsBanned) {
                                        if (((Integer) actions.get(i)).intValue() == 0) {
                                            TL_chatChannelParticipant channelParticipant = user;
                                            if (rights == 1) {
                                                channelParticipant.channelParticipant = new TL_channelParticipantAdmin();
                                            } else {
                                                channelParticipant.channelParticipant = new TL_channelParticipant();
                                            }
                                            channelParticipant.channelParticipant.inviter_id = UserConfig.getInstance(ProfileActivity.this.currentAccount).getClientUserId();
                                            channelParticipant.channelParticipant.user_id = user.user_id;
                                            channelParticipant.channelParticipant.date = user.date;
                                            channelParticipant.channelParticipant.banned_rights = rightsBanned;
                                            channelParticipant.channelParticipant.admin_rights = rightsAdmin;
                                        } else if (((Integer) actions.get(i)).intValue() == 1 && rights == 0 && ProfileActivity.this.currentChat.megagroup && ProfileActivity.this.info != null && ProfileActivity.this.info.participants != null) {
                                            int a;
                                            boolean changed = false;
                                            int a2 = 0;
                                            for (int a3 = 0; a3 < ProfileActivity.this.info.participants.participants.size(); a3++) {
                                                if (((TL_chatChannelParticipant) ProfileActivity.this.info.participants.participants.get(a3)).channelParticipant.user_id == user.user_id) {
                                                    if (ProfileActivity.this.info != null) {
                                                        ChatFull access$2400 = ProfileActivity.this.info;
                                                        access$2400.participants_count--;
                                                    }
                                                    ProfileActivity.this.info.participants.participants.remove(a3);
                                                    changed = true;
                                                    if (ProfileActivity.this.info != null && ProfileActivity.this.info.participants != null) {
                                                        while (true) {
                                                            a = a2;
                                                            if (a < ProfileActivity.this.info.participants.participants.size()) {
                                                                break;
                                                            } else if (((ChatParticipant) ProfileActivity.this.info.participants.participants.get(a)).user_id == user.user_id) {
                                                                break;
                                                            } else {
                                                                a2 = a + 1;
                                                            }
                                                        }
                                                        ProfileActivity.this.info.participants.participants.remove(a);
                                                        changed = true;
                                                    }
                                                    if (changed) {
                                                        ProfileActivity.this.updateOnlineCount();
                                                        ProfileActivity.this.updateRowsIds();
                                                        ProfileActivity.this.listAdapter.notifyDataSetChanged();
                                                    }
                                                }
                                            }
                                            while (true) {
                                                a = a2;
                                                if (a < ProfileActivity.this.info.participants.participants.size()) {
                                                    if (((ChatParticipant) ProfileActivity.this.info.participants.participants.get(a)).user_id == user.user_id) {
                                                        break;
                                                    }
                                                    a2 = a + 1;
                                                } else {
                                                    break;
                                                }
                                            }
                                            ProfileActivity.this.info.participants.participants.remove(a);
                                            changed = true;
                                            if (changed) {
                                                ProfileActivity.this.updateOnlineCount();
                                                ProfileActivity.this.updateRowsIds();
                                                ProfileActivity.this.listAdapter.notifyDataSetChanged();
                                            }
                                        }
                                    }
                                });
                                ProfileActivity.this.presentFragment(channelRightsEditActivity);
                            }
                        });
                        ProfileActivity.this.showDialog(builder.create());
                        return true;
                    }
                });
                if (r0.banFromGroup == 0) {
                    if (r0.currentChannelParticipant == null) {
                        TL_channels_getParticipant req = new TL_channels_getParticipant();
                        req.channel = MessagesController.getInstance(r0.currentAccount).getInputChannel(r0.banFromGroup);
                        req.user_id = MessagesController.getInstance(r0.currentAccount).getInputUser(r0.user_id);
                        ConnectionsManager.getInstance(r0.currentAccount).sendRequest(req, new RequestDelegate() {
                            public void run(final TLObject response, TL_error error) {
                                if (response != null) {
                                    AndroidUtilities.runOnUIThread(new Runnable() {
                                        public void run() {
                                            ProfileActivity.this.currentChannelParticipant = ((TL_channels_channelParticipant) response).participant;
                                        }
                                    });
                                }
                            }
                        });
                    }
                    FrameLayout frameLayout1 = new FrameLayout(context2) {
                        protected void onDraw(Canvas canvas) {
                            int bottom = Theme.chat_composeShadowDrawable.getIntrinsicHeight();
                            Theme.chat_composeShadowDrawable.setBounds(0, 0, getMeasuredWidth(), bottom);
                            Theme.chat_composeShadowDrawable.draw(canvas);
                            canvas.drawRect(0.0f, (float) bottom, (float) getMeasuredWidth(), (float) getMeasuredHeight(), Theme.chat_composeBackgroundPaint);
                        }
                    };
                    frameLayout1.setWillNotDraw(false);
                    frameLayout.addView(frameLayout1, LayoutHelper.createFrame(-1, 51, 83));
                    frameLayout1.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {
                            ChannelRightsEditActivity fragment = new ChannelRightsEditActivity(ProfileActivity.this.user_id, ProfileActivity.this.banFromGroup, null, ProfileActivity.this.currentChannelParticipant != null ? ProfileActivity.this.currentChannelParticipant.banned_rights : null, 1, true);
                            fragment.setDelegate(new ChannelRightsEditActivityDelegate() {
                                public void didSetRights(int rights, TL_channelAdminRights rightsAdmin, TL_channelBannedRights rightsBanned) {
                                    ProfileActivity.this.removeSelfFromStack();
                                }
                            });
                            ProfileActivity.this.presentFragment(fragment);
                        }
                    });
                    TextView textView = new TextView(context2);
                    textView.setTextColor(Theme.getColor(Theme.key_windowBackgroundWhiteRedText));
                    textView.setTextSize(1, 15.0f);
                    textView.setGravity(17);
                    textView.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
                    textView.setText(LocaleController.getString("BanFromTheGroup", R.string.BanFromTheGroup));
                    frameLayout1.addView(textView, LayoutHelper.createFrame(-2, -2.0f, 17, 0.0f, 1.0f, 0.0f, 0.0f));
                    r0.listView.setPadding(0, AndroidUtilities.dp(88.0f), 0, AndroidUtilities.dp(48.0f));
                    r0.listView.setBottomGlowOffset(AndroidUtilities.dp(48.0f));
                } else {
                    r0.listView.setPadding(0, AndroidUtilities.dp(88.0f), 0, 0);
                }
                r0.topView = new TopView(context2);
                topView = r0.topView;
                if (r0.user_id == 0) {
                    if (ChatObject.isChannel(r0.chat_id, r0.currentAccount) || r0.currentChat.megagroup) {
                        i2 = r0.chat_id;
                        topView.setBackgroundColor(AvatarDrawable.getProfileBackColorForId(i2));
                        frameLayout.addView(r0.topView);
                        frameLayout.addView(r0.actionBar);
                        r0.avatarImage = new BackupImageView(context2);
                        r0.avatarImage.setRoundRadius(AndroidUtilities.dp(21.0f));
                        r0.avatarImage.setPivotX(0.0f);
                        r0.avatarImage.setPivotY(0.0f);
                        frameLayout.addView(r0.avatarImage, LayoutHelper.createFrame(42, 42.0f, 51, 64.0f, 0.0f, 0.0f, 0.0f));
                        r0.avatarImage.setOnClickListener(new View.OnClickListener() {
                            public void onClick(View v) {
                                if (ProfileActivity.this.user_id != 0) {
                                    User user = MessagesController.getInstance(ProfileActivity.this.currentAccount).getUser(Integer.valueOf(ProfileActivity.this.user_id));
                                    if (!(user.photo == null || user.photo.photo_big == null)) {
                                        PhotoViewer.getInstance().setParentActivity(ProfileActivity.this.getParentActivity());
                                        PhotoViewer.getInstance().openPhoto(user.photo.photo_big, ProfileActivity.this.provider);
                                    }
                                } else if (ProfileActivity.this.chat_id != 0) {
                                    Chat chat = MessagesController.getInstance(ProfileActivity.this.currentAccount).getChat(Integer.valueOf(ProfileActivity.this.chat_id));
                                    if (chat.photo != null && chat.photo.photo_big != null) {
                                        PhotoViewer.getInstance().setParentActivity(ProfileActivity.this.getParentActivity());
                                        PhotoViewer.getInstance().openPhoto(chat.photo.photo_big, ProfileActivity.this.provider);
                                    }
                                }
                            }
                        });
                        a = 0;
                        while (a < 2) {
                            if (r0.playProfileAnimation || a != 0) {
                                r0.nameTextView[a] = new SimpleTextView(context2);
                                if (a != 1) {
                                    r0.nameTextView[a].setTextColor(Theme.getColor(Theme.key_profile_title));
                                } else {
                                    r0.nameTextView[a].setTextColor(Theme.getColor(Theme.key_actionBarDefaultTitle));
                                }
                                r0.nameTextView[a].setTextSize(18);
                                r0.nameTextView[a].setGravity(3);
                                r0.nameTextView[a].setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
                                r0.nameTextView[a].setLeftDrawableTopPadding(-AndroidUtilities.dp(1.3f));
                                r0.nameTextView[a].setPivotX(0.0f);
                                r0.nameTextView[a].setPivotY(0.0f);
                                f = 1.0f;
                                r0.nameTextView[a].setAlpha(a != 0 ? 0.0f : 1.0f);
                                frameLayout.addView(r0.nameTextView[a], LayoutHelper.createFrame(-2, -2.0f, 51, 118.0f, 0.0f, a != 0 ? 48.0f : 0.0f, 0.0f));
                                r0.onlineTextView[a] = new SimpleTextView(context2);
                                simpleTextView = r0.onlineTextView[a];
                                if (r0.user_id == 0) {
                                    if (ChatObject.isChannel(r0.chat_id, r0.currentAccount) || r0.currentChat.megagroup) {
                                        i3 = r0.chat_id;
                                        simpleTextView.setTextColor(AvatarDrawable.getProfileTextColorForId(i3));
                                        r0.onlineTextView[a].setTextSize(14);
                                        r0.onlineTextView[a].setGravity(3);
                                        simpleTextView = r0.onlineTextView[a];
                                        if (a == 0) {
                                            f = 0.0f;
                                        }
                                        simpleTextView.setAlpha(f);
                                        frameLayout.addView(r0.onlineTextView[a], LayoutHelper.createFrame(-2, -2.0f, 51, 118.0f, 0.0f, a != 0 ? 48.0f : 8.0f, 0.0f));
                                    }
                                }
                                i3 = 5;
                                simpleTextView.setTextColor(AvatarDrawable.getProfileTextColorForId(i3));
                                r0.onlineTextView[a].setTextSize(14);
                                r0.onlineTextView[a].setGravity(3);
                                simpleTextView = r0.onlineTextView[a];
                                if (a == 0) {
                                    f = 0.0f;
                                }
                                simpleTextView.setAlpha(f);
                                if (a != 0) {
                                }
                                frameLayout.addView(r0.onlineTextView[a], LayoutHelper.createFrame(-2, -2.0f, 51, 118.0f, 0.0f, a != 0 ? 48.0f : 8.0f, 0.0f));
                            }
                            a++;
                        }
                        if (r0.user_id != 0 || (r0.chat_id >= 0 && (!ChatObject.isLeftFromChat(r0.currentChat) || ChatObject.isChannel(r0.currentChat)))) {
                            r0.writeButton = new ImageView(context2);
                            f2 = 56.0f;
                            drawable = Theme.createSimpleSelectorCircleDrawable(AndroidUtilities.dp(56.0f), Theme.getColor(Theme.key_profile_actionBackground), Theme.getColor(Theme.key_profile_actionPressedBackground));
                            if (VERSION.SDK_INT < 21) {
                                shadowDrawable = context.getResources().getDrawable(R.drawable.floating_shadow_profile).mutate();
                                shadowDrawable.setColorFilter(new PorterDuffColorFilter(Theme.ACTION_BAR_VIDEO_EDIT_COLOR, Mode.MULTIPLY));
                                combinedDrawable = new CombinedDrawable(shadowDrawable, drawable, 0, 0);
                                combinedDrawable.setIconSize(AndroidUtilities.dp(56.0f), AndroidUtilities.dp(56.0f));
                                drawable = combinedDrawable;
                            }
                            r0.writeButton.setBackgroundDrawable(drawable);
                            r0.writeButton.setScaleType(ScaleType.CENTER);
                            r0.writeButton.setColorFilter(new PorterDuffColorFilter(Theme.getColor(Theme.key_profile_actionIcon), Mode.MULTIPLY));
                            if (r0.user_id == 0) {
                                r0.writeButton.setImageResource(R.drawable.floating_message);
                                r0.writeButton.setPadding(0, AndroidUtilities.dp(3.0f), 0, 0);
                            } else if (r0.chat_id != 0) {
                                isChannel = ChatObject.isChannel(r0.currentChat);
                                if ((isChannel || ChatObject.canEditInfo(r0.currentChat)) && (isChannel || r0.currentChat.admin || r0.currentChat.creator || !r0.currentChat.admins_enabled)) {
                                    r0.writeButton.setImageResource(R.drawable.floating_camera);
                                } else {
                                    r0.writeButton.setImageResource(R.drawable.floating_message);
                                    r0.writeButton.setPadding(0, AndroidUtilities.dp(3.0f), 0, 0);
                                }
                            }
                            view = r0.writeButton;
                            i4 = VERSION.SDK_INT < 21 ? 56 : 60;
                            if (VERSION.SDK_INT < 21) {
                                f2 = 60.0f;
                            }
                            frameLayout.addView(view, LayoutHelper.createFrame(i4, f2, 53, 0.0f, 0.0f, 16.0f, 0.0f));
                            if (VERSION.SDK_INT >= 21) {
                                animator = new StateListAnimator();
                                animator.addState(new int[]{16842919}, ObjectAnimator.ofFloat(r0.writeButton, "translationZ", new float[]{(float) AndroidUtilities.dp(2.0f), (float) AndroidUtilities.dp(4.0f)}).setDuration(200));
                                animator.addState(new int[0], ObjectAnimator.ofFloat(r0.writeButton, "translationZ", new float[]{(float) AndroidUtilities.dp(4.0f), (float) AndroidUtilities.dp(2.0f)}).setDuration(200));
                                r0.writeButton.setStateListAnimator(animator);
                                r0.writeButton.setOutlineProvider(new ViewOutlineProvider() {
                                    @SuppressLint({"NewApi"})
                                    public void getOutline(View view, Outline outline) {
                                        outline.setOval(0, 0, AndroidUtilities.dp(56.0f), AndroidUtilities.dp(56.0f));
                                    }
                                });
                            }
                            r0.writeButton.setOnClickListener(new View.OnClickListener() {
                                public void onClick(View v) {
                                    if (ProfileActivity.this.getParentActivity() != null) {
                                        Bundle args;
                                        if (ProfileActivity.this.user_id != 0) {
                                            if (ProfileActivity.this.playProfileAnimation && (ProfileActivity.this.parentLayout.fragmentsStack.get(ProfileActivity.this.parentLayout.fragmentsStack.size() - 2) instanceof ChatActivity)) {
                                                ProfileActivity.this.finishFragment();
                                            } else {
                                                User user = MessagesController.getInstance(ProfileActivity.this.currentAccount).getUser(Integer.valueOf(ProfileActivity.this.user_id));
                                                if (user != null) {
                                                    if (!(user instanceof TL_userEmpty)) {
                                                        args = new Bundle();
                                                        args.putInt("user_id", ProfileActivity.this.user_id);
                                                        if (MessagesController.getInstance(ProfileActivity.this.currentAccount).checkCanOpenChat(args, ProfileActivity.this)) {
                                                            NotificationCenter.getInstance(ProfileActivity.this.currentAccount).removeObserver(ProfileActivity.this, NotificationCenter.closeChats);
                                                            NotificationCenter.getInstance(ProfileActivity.this.currentAccount).postNotificationName(NotificationCenter.closeChats, new Object[0]);
                                                            ProfileActivity.this.presentFragment(new ChatActivity(args), true);
                                                        }
                                                    }
                                                }
                                            }
                                        } else if (ProfileActivity.this.chat_id != 0) {
                                            boolean isChannel = ChatObject.isChannel(ProfileActivity.this.currentChat);
                                            if ((!isChannel || ChatObject.canEditInfo(ProfileActivity.this.currentChat)) && (isChannel || ProfileActivity.this.currentChat.admin || ProfileActivity.this.currentChat.creator || !ProfileActivity.this.currentChat.admins_enabled)) {
                                                CharSequence[] items;
                                                Builder builder = new Builder(ProfileActivity.this.getParentActivity());
                                                Chat chat = MessagesController.getInstance(ProfileActivity.this.currentAccount).getChat(Integer.valueOf(ProfileActivity.this.chat_id));
                                                if (!(chat.photo == null || chat.photo.photo_big == null)) {
                                                    if (!(chat.photo instanceof TL_chatPhotoEmpty)) {
                                                        items = new CharSequence[]{LocaleController.getString("FromCamera", R.string.FromCamera), LocaleController.getString("FromGalley", R.string.FromGalley), LocaleController.getString("DeletePhoto", R.string.DeletePhoto)};
                                                        builder.setItems(items, new OnClickListener() {
                                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                                if (i == 0) {
                                                                    ProfileActivity.this.avatarUpdater.openCamera();
                                                                } else if (i == 1) {
                                                                    ProfileActivity.this.avatarUpdater.openGallery();
                                                                } else if (i == 2) {
                                                                    MessagesController.getInstance(ProfileActivity.this.currentAccount).changeChatAvatar(ProfileActivity.this.chat_id, null);
                                                                }
                                                            }
                                                        });
                                                        ProfileActivity.this.showDialog(builder.create());
                                                    }
                                                }
                                                items = new CharSequence[]{LocaleController.getString("FromCamera", R.string.FromCamera), LocaleController.getString("FromGalley", R.string.FromGalley)};
                                                builder.setItems(items, /* anonymous class already generated */);
                                                ProfileActivity.this.showDialog(builder.create());
                                            } else if (ProfileActivity.this.playProfileAnimation && (ProfileActivity.this.parentLayout.fragmentsStack.get(ProfileActivity.this.parentLayout.fragmentsStack.size() - 2) instanceof ChatActivity)) {
                                                ProfileActivity.this.finishFragment();
                                            } else {
                                                args = new Bundle();
                                                args.putInt("chat_id", ProfileActivity.this.currentChat.id);
                                                if (MessagesController.getInstance(ProfileActivity.this.currentAccount).checkCanOpenChat(args, ProfileActivity.this)) {
                                                    NotificationCenter.getInstance(ProfileActivity.this.currentAccount).removeObserver(ProfileActivity.this, NotificationCenter.closeChats);
                                                    NotificationCenter.getInstance(ProfileActivity.this.currentAccount).postNotificationName(NotificationCenter.closeChats, new Object[0]);
                                                    ProfileActivity.this.presentFragment(new ChatActivity(args), true);
                                                }
                                            }
                                        }
                                    }
                                }
                            });
                        }
                        needLayout();
                        r0.listView.setOnScrollListener(new OnScrollListener() {
                            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                                ProfileActivity.this.checkListViewScroll();
                                if (ProfileActivity.this.participantsMap != null && ProfileActivity.this.loadMoreMembersRow != -1 && ProfileActivity.this.layoutManager.findLastVisibleItemPosition() > ProfileActivity.this.loadMoreMembersRow - 8) {
                                    ProfileActivity.this.getChannelParticipants(false);
                                }
                            }
                        });
                        return r0.fragmentView;
                    }
                }
                i2 = 5;
                topView.setBackgroundColor(AvatarDrawable.getProfileBackColorForId(i2));
                frameLayout.addView(r0.topView);
                frameLayout.addView(r0.actionBar);
                r0.avatarImage = new BackupImageView(context2);
                r0.avatarImage.setRoundRadius(AndroidUtilities.dp(21.0f));
                r0.avatarImage.setPivotX(0.0f);
                r0.avatarImage.setPivotY(0.0f);
                frameLayout.addView(r0.avatarImage, LayoutHelper.createFrame(42, 42.0f, 51, 64.0f, 0.0f, 0.0f, 0.0f));
                r0.avatarImage.setOnClickListener(/* anonymous class already generated */);
                a = 0;
                while (a < 2) {
                    if (!r0.playProfileAnimation) {
                    }
                    r0.nameTextView[a] = new SimpleTextView(context2);
                    if (a != 1) {
                        r0.nameTextView[a].setTextColor(Theme.getColor(Theme.key_actionBarDefaultTitle));
                    } else {
                        r0.nameTextView[a].setTextColor(Theme.getColor(Theme.key_profile_title));
                    }
                    r0.nameTextView[a].setTextSize(18);
                    r0.nameTextView[a].setGravity(3);
                    r0.nameTextView[a].setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
                    r0.nameTextView[a].setLeftDrawableTopPadding(-AndroidUtilities.dp(1.3f));
                    r0.nameTextView[a].setPivotX(0.0f);
                    r0.nameTextView[a].setPivotY(0.0f);
                    f = 1.0f;
                    if (a != 0) {
                    }
                    r0.nameTextView[a].setAlpha(a != 0 ? 0.0f : 1.0f);
                    if (a != 0) {
                    }
                    frameLayout.addView(r0.nameTextView[a], LayoutHelper.createFrame(-2, -2.0f, 51, 118.0f, 0.0f, a != 0 ? 48.0f : 0.0f, 0.0f));
                    r0.onlineTextView[a] = new SimpleTextView(context2);
                    simpleTextView = r0.onlineTextView[a];
                    if (r0.user_id == 0) {
                        if (ChatObject.isChannel(r0.chat_id, r0.currentAccount)) {
                        }
                        i3 = r0.chat_id;
                        simpleTextView.setTextColor(AvatarDrawable.getProfileTextColorForId(i3));
                        r0.onlineTextView[a].setTextSize(14);
                        r0.onlineTextView[a].setGravity(3);
                        simpleTextView = r0.onlineTextView[a];
                        if (a == 0) {
                            f = 0.0f;
                        }
                        simpleTextView.setAlpha(f);
                        if (a != 0) {
                        }
                        frameLayout.addView(r0.onlineTextView[a], LayoutHelper.createFrame(-2, -2.0f, 51, 118.0f, 0.0f, a != 0 ? 48.0f : 8.0f, 0.0f));
                        a++;
                    }
                    i3 = 5;
                    simpleTextView.setTextColor(AvatarDrawable.getProfileTextColorForId(i3));
                    r0.onlineTextView[a].setTextSize(14);
                    r0.onlineTextView[a].setGravity(3);
                    simpleTextView = r0.onlineTextView[a];
                    if (a == 0) {
                        f = 0.0f;
                    }
                    simpleTextView.setAlpha(f);
                    if (a != 0) {
                    }
                    frameLayout.addView(r0.onlineTextView[a], LayoutHelper.createFrame(-2, -2.0f, 51, 118.0f, 0.0f, a != 0 ? 48.0f : 8.0f, 0.0f));
                    a++;
                }
                r0.writeButton = new ImageView(context2);
                f2 = 56.0f;
                drawable = Theme.createSimpleSelectorCircleDrawable(AndroidUtilities.dp(56.0f), Theme.getColor(Theme.key_profile_actionBackground), Theme.getColor(Theme.key_profile_actionPressedBackground));
                if (VERSION.SDK_INT < 21) {
                    shadowDrawable = context.getResources().getDrawable(R.drawable.floating_shadow_profile).mutate();
                    shadowDrawable.setColorFilter(new PorterDuffColorFilter(Theme.ACTION_BAR_VIDEO_EDIT_COLOR, Mode.MULTIPLY));
                    combinedDrawable = new CombinedDrawable(shadowDrawable, drawable, 0, 0);
                    combinedDrawable.setIconSize(AndroidUtilities.dp(56.0f), AndroidUtilities.dp(56.0f));
                    drawable = combinedDrawable;
                }
                r0.writeButton.setBackgroundDrawable(drawable);
                r0.writeButton.setScaleType(ScaleType.CENTER);
                r0.writeButton.setColorFilter(new PorterDuffColorFilter(Theme.getColor(Theme.key_profile_actionIcon), Mode.MULTIPLY));
                if (r0.user_id == 0) {
                    r0.writeButton.setImageResource(R.drawable.floating_message);
                    r0.writeButton.setPadding(0, AndroidUtilities.dp(3.0f), 0, 0);
                } else if (r0.chat_id != 0) {
                    isChannel = ChatObject.isChannel(r0.currentChat);
                    if (isChannel) {
                    }
                    r0.writeButton.setImageResource(R.drawable.floating_camera);
                }
                view = r0.writeButton;
                if (VERSION.SDK_INT < 21) {
                }
                i4 = VERSION.SDK_INT < 21 ? 56 : 60;
                if (VERSION.SDK_INT < 21) {
                    f2 = 60.0f;
                }
                frameLayout.addView(view, LayoutHelper.createFrame(i4, f2, 53, 0.0f, 0.0f, 16.0f, 0.0f));
                if (VERSION.SDK_INT >= 21) {
                    animator = new StateListAnimator();
                    animator.addState(new int[]{16842919}, ObjectAnimator.ofFloat(r0.writeButton, "translationZ", new float[]{(float) AndroidUtilities.dp(2.0f), (float) AndroidUtilities.dp(4.0f)}).setDuration(200));
                    animator.addState(new int[0], ObjectAnimator.ofFloat(r0.writeButton, "translationZ", new float[]{(float) AndroidUtilities.dp(4.0f), (float) AndroidUtilities.dp(2.0f)}).setDuration(200));
                    r0.writeButton.setStateListAnimator(animator);
                    r0.writeButton.setOutlineProvider(/* anonymous class already generated */);
                }
                r0.writeButton.setOnClickListener(/* anonymous class already generated */);
                needLayout();
                r0.listView.setOnScrollListener(/* anonymous class already generated */);
                return r0.fragmentView;
            }
        }
        i = 5;
        recyclerListView.setGlowColor(AvatarDrawable.getProfileBackColorForId(i));
        frameLayout.addView(r0.listView, LayoutHelper.createFrame(-1, -1, 51));
        r0.listView.setAdapter(r0.listAdapter);
        r0.listView.setOnItemClickListener(/* anonymous class already generated */);
        r0.listView.setOnItemLongClickListener(/* anonymous class already generated */);
        if (r0.banFromGroup == 0) {
            r0.listView.setPadding(0, AndroidUtilities.dp(88.0f), 0, 0);
        } else {
            if (r0.currentChannelParticipant == null) {
                TL_channels_getParticipant req2 = new TL_channels_getParticipant();
                req2.channel = MessagesController.getInstance(r0.currentAccount).getInputChannel(r0.banFromGroup);
                req2.user_id = MessagesController.getInstance(r0.currentAccount).getInputUser(r0.user_id);
                ConnectionsManager.getInstance(r0.currentAccount).sendRequest(req2, /* anonymous class already generated */);
            }
            FrameLayout frameLayout12 = /* anonymous class already generated */;
            frameLayout12.setWillNotDraw(false);
            frameLayout.addView(frameLayout12, LayoutHelper.createFrame(-1, 51, 83));
            frameLayout12.setOnClickListener(/* anonymous class already generated */);
            TextView textView2 = new TextView(context2);
            textView2.setTextColor(Theme.getColor(Theme.key_windowBackgroundWhiteRedText));
            textView2.setTextSize(1, 15.0f);
            textView2.setGravity(17);
            textView2.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
            textView2.setText(LocaleController.getString("BanFromTheGroup", R.string.BanFromTheGroup));
            frameLayout12.addView(textView2, LayoutHelper.createFrame(-2, -2.0f, 17, 0.0f, 1.0f, 0.0f, 0.0f));
            r0.listView.setPadding(0, AndroidUtilities.dp(88.0f), 0, AndroidUtilities.dp(48.0f));
            r0.listView.setBottomGlowOffset(AndroidUtilities.dp(48.0f));
        }
        r0.topView = new TopView(context2);
        topView = r0.topView;
        if (r0.user_id == 0) {
            if (ChatObject.isChannel(r0.chat_id, r0.currentAccount)) {
            }
            i2 = r0.chat_id;
            topView.setBackgroundColor(AvatarDrawable.getProfileBackColorForId(i2));
            frameLayout.addView(r0.topView);
            frameLayout.addView(r0.actionBar);
            r0.avatarImage = new BackupImageView(context2);
            r0.avatarImage.setRoundRadius(AndroidUtilities.dp(21.0f));
            r0.avatarImage.setPivotX(0.0f);
            r0.avatarImage.setPivotY(0.0f);
            frameLayout.addView(r0.avatarImage, LayoutHelper.createFrame(42, 42.0f, 51, 64.0f, 0.0f, 0.0f, 0.0f));
            r0.avatarImage.setOnClickListener(/* anonymous class already generated */);
            a = 0;
            while (a < 2) {
                if (r0.playProfileAnimation) {
                }
                r0.nameTextView[a] = new SimpleTextView(context2);
                if (a != 1) {
                    r0.nameTextView[a].setTextColor(Theme.getColor(Theme.key_profile_title));
                } else {
                    r0.nameTextView[a].setTextColor(Theme.getColor(Theme.key_actionBarDefaultTitle));
                }
                r0.nameTextView[a].setTextSize(18);
                r0.nameTextView[a].setGravity(3);
                r0.nameTextView[a].setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
                r0.nameTextView[a].setLeftDrawableTopPadding(-AndroidUtilities.dp(1.3f));
                r0.nameTextView[a].setPivotX(0.0f);
                r0.nameTextView[a].setPivotY(0.0f);
                f = 1.0f;
                if (a != 0) {
                }
                r0.nameTextView[a].setAlpha(a != 0 ? 0.0f : 1.0f);
                if (a != 0) {
                }
                frameLayout.addView(r0.nameTextView[a], LayoutHelper.createFrame(-2, -2.0f, 51, 118.0f, 0.0f, a != 0 ? 48.0f : 0.0f, 0.0f));
                r0.onlineTextView[a] = new SimpleTextView(context2);
                simpleTextView = r0.onlineTextView[a];
                if (r0.user_id == 0) {
                    if (ChatObject.isChannel(r0.chat_id, r0.currentAccount)) {
                    }
                    i3 = r0.chat_id;
                    simpleTextView.setTextColor(AvatarDrawable.getProfileTextColorForId(i3));
                    r0.onlineTextView[a].setTextSize(14);
                    r0.onlineTextView[a].setGravity(3);
                    simpleTextView = r0.onlineTextView[a];
                    if (a == 0) {
                        f = 0.0f;
                    }
                    simpleTextView.setAlpha(f);
                    if (a != 0) {
                    }
                    frameLayout.addView(r0.onlineTextView[a], LayoutHelper.createFrame(-2, -2.0f, 51, 118.0f, 0.0f, a != 0 ? 48.0f : 8.0f, 0.0f));
                    a++;
                }
                i3 = 5;
                simpleTextView.setTextColor(AvatarDrawable.getProfileTextColorForId(i3));
                r0.onlineTextView[a].setTextSize(14);
                r0.onlineTextView[a].setGravity(3);
                simpleTextView = r0.onlineTextView[a];
                if (a == 0) {
                    f = 0.0f;
                }
                simpleTextView.setAlpha(f);
                if (a != 0) {
                }
                frameLayout.addView(r0.onlineTextView[a], LayoutHelper.createFrame(-2, -2.0f, 51, 118.0f, 0.0f, a != 0 ? 48.0f : 8.0f, 0.0f));
                a++;
            }
            r0.writeButton = new ImageView(context2);
            f2 = 56.0f;
            drawable = Theme.createSimpleSelectorCircleDrawable(AndroidUtilities.dp(56.0f), Theme.getColor(Theme.key_profile_actionBackground), Theme.getColor(Theme.key_profile_actionPressedBackground));
            if (VERSION.SDK_INT < 21) {
                shadowDrawable = context.getResources().getDrawable(R.drawable.floating_shadow_profile).mutate();
                shadowDrawable.setColorFilter(new PorterDuffColorFilter(Theme.ACTION_BAR_VIDEO_EDIT_COLOR, Mode.MULTIPLY));
                combinedDrawable = new CombinedDrawable(shadowDrawable, drawable, 0, 0);
                combinedDrawable.setIconSize(AndroidUtilities.dp(56.0f), AndroidUtilities.dp(56.0f));
                drawable = combinedDrawable;
            }
            r0.writeButton.setBackgroundDrawable(drawable);
            r0.writeButton.setScaleType(ScaleType.CENTER);
            r0.writeButton.setColorFilter(new PorterDuffColorFilter(Theme.getColor(Theme.key_profile_actionIcon), Mode.MULTIPLY));
            if (r0.user_id == 0) {
                r0.writeButton.setImageResource(R.drawable.floating_message);
                r0.writeButton.setPadding(0, AndroidUtilities.dp(3.0f), 0, 0);
            } else if (r0.chat_id != 0) {
                isChannel = ChatObject.isChannel(r0.currentChat);
                if (isChannel) {
                }
                r0.writeButton.setImageResource(R.drawable.floating_camera);
            }
            view = r0.writeButton;
            if (VERSION.SDK_INT < 21) {
            }
            i4 = VERSION.SDK_INT < 21 ? 56 : 60;
            if (VERSION.SDK_INT < 21) {
                f2 = 60.0f;
            }
            frameLayout.addView(view, LayoutHelper.createFrame(i4, f2, 53, 0.0f, 0.0f, 16.0f, 0.0f));
            if (VERSION.SDK_INT >= 21) {
                animator = new StateListAnimator();
                animator.addState(new int[]{16842919}, ObjectAnimator.ofFloat(r0.writeButton, "translationZ", new float[]{(float) AndroidUtilities.dp(2.0f), (float) AndroidUtilities.dp(4.0f)}).setDuration(200));
                animator.addState(new int[0], ObjectAnimator.ofFloat(r0.writeButton, "translationZ", new float[]{(float) AndroidUtilities.dp(4.0f), (float) AndroidUtilities.dp(2.0f)}).setDuration(200));
                r0.writeButton.setStateListAnimator(animator);
                r0.writeButton.setOutlineProvider(/* anonymous class already generated */);
            }
            r0.writeButton.setOnClickListener(/* anonymous class already generated */);
            needLayout();
            r0.listView.setOnScrollListener(/* anonymous class already generated */);
            return r0.fragmentView;
        }
        i2 = 5;
        topView.setBackgroundColor(AvatarDrawable.getProfileBackColorForId(i2));
        frameLayout.addView(r0.topView);
        frameLayout.addView(r0.actionBar);
        r0.avatarImage = new BackupImageView(context2);
        r0.avatarImage.setRoundRadius(AndroidUtilities.dp(21.0f));
        r0.avatarImage.setPivotX(0.0f);
        r0.avatarImage.setPivotY(0.0f);
        frameLayout.addView(r0.avatarImage, LayoutHelper.createFrame(42, 42.0f, 51, 64.0f, 0.0f, 0.0f, 0.0f));
        r0.avatarImage.setOnClickListener(/* anonymous class already generated */);
        a = 0;
        while (a < 2) {
            if (r0.playProfileAnimation) {
            }
            r0.nameTextView[a] = new SimpleTextView(context2);
            if (a != 1) {
                r0.nameTextView[a].setTextColor(Theme.getColor(Theme.key_actionBarDefaultTitle));
            } else {
                r0.nameTextView[a].setTextColor(Theme.getColor(Theme.key_profile_title));
            }
            r0.nameTextView[a].setTextSize(18);
            r0.nameTextView[a].setGravity(3);
            r0.nameTextView[a].setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
            r0.nameTextView[a].setLeftDrawableTopPadding(-AndroidUtilities.dp(1.3f));
            r0.nameTextView[a].setPivotX(0.0f);
            r0.nameTextView[a].setPivotY(0.0f);
            f = 1.0f;
            if (a != 0) {
            }
            r0.nameTextView[a].setAlpha(a != 0 ? 0.0f : 1.0f);
            if (a != 0) {
            }
            frameLayout.addView(r0.nameTextView[a], LayoutHelper.createFrame(-2, -2.0f, 51, 118.0f, 0.0f, a != 0 ? 48.0f : 0.0f, 0.0f));
            r0.onlineTextView[a] = new SimpleTextView(context2);
            simpleTextView = r0.onlineTextView[a];
            if (r0.user_id == 0) {
                if (ChatObject.isChannel(r0.chat_id, r0.currentAccount)) {
                }
                i3 = r0.chat_id;
                simpleTextView.setTextColor(AvatarDrawable.getProfileTextColorForId(i3));
                r0.onlineTextView[a].setTextSize(14);
                r0.onlineTextView[a].setGravity(3);
                simpleTextView = r0.onlineTextView[a];
                if (a == 0) {
                    f = 0.0f;
                }
                simpleTextView.setAlpha(f);
                if (a != 0) {
                }
                frameLayout.addView(r0.onlineTextView[a], LayoutHelper.createFrame(-2, -2.0f, 51, 118.0f, 0.0f, a != 0 ? 48.0f : 8.0f, 0.0f));
                a++;
            }
            i3 = 5;
            simpleTextView.setTextColor(AvatarDrawable.getProfileTextColorForId(i3));
            r0.onlineTextView[a].setTextSize(14);
            r0.onlineTextView[a].setGravity(3);
            simpleTextView = r0.onlineTextView[a];
            if (a == 0) {
                f = 0.0f;
            }
            simpleTextView.setAlpha(f);
            if (a != 0) {
            }
            frameLayout.addView(r0.onlineTextView[a], LayoutHelper.createFrame(-2, -2.0f, 51, 118.0f, 0.0f, a != 0 ? 48.0f : 8.0f, 0.0f));
            a++;
        }
        r0.writeButton = new ImageView(context2);
        f2 = 56.0f;
        drawable = Theme.createSimpleSelectorCircleDrawable(AndroidUtilities.dp(56.0f), Theme.getColor(Theme.key_profile_actionBackground), Theme.getColor(Theme.key_profile_actionPressedBackground));
        if (VERSION.SDK_INT < 21) {
            shadowDrawable = context.getResources().getDrawable(R.drawable.floating_shadow_profile).mutate();
            shadowDrawable.setColorFilter(new PorterDuffColorFilter(Theme.ACTION_BAR_VIDEO_EDIT_COLOR, Mode.MULTIPLY));
            combinedDrawable = new CombinedDrawable(shadowDrawable, drawable, 0, 0);
            combinedDrawable.setIconSize(AndroidUtilities.dp(56.0f), AndroidUtilities.dp(56.0f));
            drawable = combinedDrawable;
        }
        r0.writeButton.setBackgroundDrawable(drawable);
        r0.writeButton.setScaleType(ScaleType.CENTER);
        r0.writeButton.setColorFilter(new PorterDuffColorFilter(Theme.getColor(Theme.key_profile_actionIcon), Mode.MULTIPLY));
        if (r0.user_id == 0) {
            r0.writeButton.setImageResource(R.drawable.floating_message);
            r0.writeButton.setPadding(0, AndroidUtilities.dp(3.0f), 0, 0);
        } else if (r0.chat_id != 0) {
            isChannel = ChatObject.isChannel(r0.currentChat);
            if (isChannel) {
            }
            r0.writeButton.setImageResource(R.drawable.floating_camera);
        }
        view = r0.writeButton;
        if (VERSION.SDK_INT < 21) {
        }
        i4 = VERSION.SDK_INT < 21 ? 56 : 60;
        if (VERSION.SDK_INT < 21) {
            f2 = 60.0f;
        }
        frameLayout.addView(view, LayoutHelper.createFrame(i4, f2, 53, 0.0f, 0.0f, 16.0f, 0.0f));
        if (VERSION.SDK_INT >= 21) {
            animator = new StateListAnimator();
            animator.addState(new int[]{16842919}, ObjectAnimator.ofFloat(r0.writeButton, "translationZ", new float[]{(float) AndroidUtilities.dp(2.0f), (float) AndroidUtilities.dp(4.0f)}).setDuration(200));
            animator.addState(new int[0], ObjectAnimator.ofFloat(r0.writeButton, "translationZ", new float[]{(float) AndroidUtilities.dp(4.0f), (float) AndroidUtilities.dp(2.0f)}).setDuration(200));
            r0.writeButton.setStateListAnimator(animator);
            r0.writeButton.setOutlineProvider(/* anonymous class already generated */);
        }
        r0.writeButton.setOnClickListener(/* anonymous class already generated */);
        needLayout();
        r0.listView.setOnScrollListener(/* anonymous class already generated */);
        return r0.fragmentView;
    }

    private boolean processOnClickOrPress(final int position) {
        final User user;
        Builder builder;
        Chat chat;
        if (position != this.usernameRow) {
            if (position != this.channelNameRow) {
                if (position == this.phoneRow) {
                    user = MessagesController.getInstance(this.currentAccount).getUser(Integer.valueOf(this.user_id));
                    if (!(user == null || user.phone == null || user.phone.length() == 0)) {
                        if (getParentActivity() != null) {
                            builder = new Builder(getParentActivity());
                            ArrayList<CharSequence> items = new ArrayList();
                            final ArrayList<Integer> actions = new ArrayList();
                            TL_userFull userFull = MessagesController.getInstance(this.currentAccount).getUserFull(user.id);
                            if (userFull != null && userFull.phone_calls_available) {
                                items.add(LocaleController.getString("CallViaTelegram", R.string.CallViaTelegram));
                                actions.add(Integer.valueOf(2));
                            }
                            items.add(LocaleController.getString("Call", R.string.Call));
                            actions.add(Integer.valueOf(0));
                            items.add(LocaleController.getString("Copy", R.string.Copy));
                            actions.add(Integer.valueOf(1));
                            builder.setItems((CharSequence[]) items.toArray(new CharSequence[items.size()]), new OnClickListener() {
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    i = ((Integer) actions.get(i)).intValue();
                                    StringBuilder stringBuilder;
                                    if (i == 0) {
                                        try {
                                            stringBuilder = new StringBuilder();
                                            stringBuilder.append("tel:+");
                                            stringBuilder.append(user.phone);
                                            Intent intent = new Intent("android.intent.action.DIAL", Uri.parse(stringBuilder.toString()));
                                            intent.addFlags(268435456);
                                            ProfileActivity.this.getParentActivity().startActivityForResult(intent, 500);
                                        } catch (Throwable e) {
                                            FileLog.e(e);
                                        }
                                    } else if (i == 1) {
                                        try {
                                            ClipboardManager clipboard = (ClipboardManager) ApplicationLoader.applicationContext.getSystemService("clipboard");
                                            stringBuilder = new StringBuilder();
                                            stringBuilder.append("+");
                                            stringBuilder.append(user.phone);
                                            clipboard.setPrimaryClip(ClipData.newPlainText("label", stringBuilder.toString()));
                                        } catch (Throwable e2) {
                                            FileLog.e(e2);
                                        }
                                    } else if (i == 2) {
                                        VoIPHelper.startCall(user, ProfileActivity.this.getParentActivity(), MessagesController.getInstance(ProfileActivity.this.currentAccount).getUserFull(user.id));
                                    }
                                }
                            });
                            showDialog(builder.create());
                            return true;
                        }
                    }
                    return false;
                }
                if (!(position == this.channelInfoRow || position == this.userInfoRow)) {
                    if (position != this.userInfoDetailedRow) {
                        return false;
                    }
                }
                Builder builder2 = new Builder(getParentActivity());
                builder2.setItems(new CharSequence[]{LocaleController.getString("Copy", R.string.Copy)}, new OnClickListener() {
                    public void onClick(DialogInterface dialogInterface, int i) {
                        try {
                            String about;
                            if (position == ProfileActivity.this.channelInfoRow) {
                                about = ProfileActivity.this.info.about;
                            } else {
                                TL_userFull userFull = MessagesController.getInstance(ProfileActivity.this.currentAccount).getUserFull(ProfileActivity.this.user_id);
                                about = userFull != null ? userFull.about : null;
                            }
                            if (!TextUtils.isEmpty(about)) {
                                AndroidUtilities.addToClipboard(about);
                            }
                        } catch (Throwable e) {
                            FileLog.e(e);
                        }
                    }
                });
                showDialog(builder2.create());
                return true;
            }
        }
        if (position == this.usernameRow) {
            user = MessagesController.getInstance(this.currentAccount).getUser(Integer.valueOf(this.user_id));
            if (user != null) {
                if (user.username != null) {
                    chat = user.username;
                }
            }
            return false;
        }
        String username = MessagesController.getInstance(this.currentAccount).getChat(Integer.valueOf(this.chat_id));
        if (username != null) {
            if (username.username != null) {
                chat = username.username;
            }
        }
        return false;
        builder = new Builder(getParentActivity());
        builder.setItems(new CharSequence[]{LocaleController.getString("Copy", R.string.Copy)}, new OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                if (i == 0) {
                    try {
                        ClipboardManager clipboard = (ClipboardManager) ApplicationLoader.applicationContext.getSystemService("clipboard");
                        StringBuilder stringBuilder = new StringBuilder();
                        stringBuilder.append("@");
                        stringBuilder.append(chat);
                        clipboard.setPrimaryClip(ClipData.newPlainText("label", stringBuilder.toString()));
                    } catch (Throwable e) {
                        FileLog.e(e);
                    }
                }
            }
        });
        showDialog(builder.create());
        return true;
    }

    private void leaveChatPressed() {
        Builder builder = new Builder(getParentActivity());
        boolean isChannel = ChatObject.isChannel(this.chat_id, this.currentAccount);
        int i = R.string.AreYouSureDeleteAndExit;
        if (!isChannel || this.currentChat.megagroup) {
            builder.setMessage(LocaleController.getString("AreYouSureDeleteAndExit", R.string.AreYouSureDeleteAndExit));
        } else {
            String str;
            if (ChatObject.isChannel(this.chat_id, this.currentAccount)) {
                str = "ChannelLeaveAlert";
                i = R.string.ChannelLeaveAlert;
            } else {
                str = "AreYouSureDeleteAndExit";
            }
            builder.setMessage(LocaleController.getString(str, i));
        }
        builder.setTitle(LocaleController.getString("AppName", R.string.AppName));
        builder.setPositiveButton(LocaleController.getString("OK", R.string.OK), new OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                ProfileActivity.this.kickUser(0);
            }
        });
        builder.setNegativeButton(LocaleController.getString("Cancel", R.string.Cancel), null);
        showDialog(builder.create());
    }

    public void saveSelfArgs(Bundle args) {
        if (this.chat_id != 0 && this.avatarUpdater != null && this.avatarUpdater.currentPicturePath != null) {
            args.putString("path", this.avatarUpdater.currentPicturePath);
        }
    }

    public void restoreSelfArgs(Bundle args) {
        if (this.chat_id != 0) {
            MessagesController.getInstance(this.currentAccount).loadChatInfo(this.chat_id, null, false);
            if (this.avatarUpdater != null) {
                this.avatarUpdater.currentPicturePath = args.getString("path");
            }
        }
    }

    public void onActivityResultFragment(int requestCode, int resultCode, Intent data) {
        if (this.chat_id != 0) {
            this.avatarUpdater.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void getChannelParticipants(boolean reload) {
        if (!(this.loadingUsers || this.participantsMap == null)) {
            if (this.info != null) {
                this.loadingUsers = true;
                int i = 0;
                final int delay = (this.participantsMap.size() == 0 || !reload) ? 0 : 300;
                final TL_channels_getParticipants req = new TL_channels_getParticipants();
                req.channel = MessagesController.getInstance(this.currentAccount).getInputChannel(this.chat_id);
                req.filter = new TL_channelParticipantsRecent();
                if (!reload) {
                    i = this.participantsMap.size();
                }
                req.offset = i;
                req.limit = Callback.DEFAULT_DRAG_ANIMATION_DURATION;
                ConnectionsManager.getInstance(this.currentAccount).bindRequestToGuid(ConnectionsManager.getInstance(this.currentAccount).sendRequest(req, new RequestDelegate() {
                    public void run(final TLObject response, final TL_error error) {
                        AndroidUtilities.runOnUIThread(new Runnable() {
                            public void run() {
                                if (error == null) {
                                    TL_channels_channelParticipants res = response;
                                    MessagesController.getInstance(ProfileActivity.this.currentAccount).putUsers(res.users, false);
                                    if (res.users.size() < Callback.DEFAULT_DRAG_ANIMATION_DURATION) {
                                        ProfileActivity.this.usersEndReached = true;
                                    }
                                    if (req.offset == 0) {
                                        ProfileActivity.this.participantsMap.clear();
                                        ProfileActivity.this.info.participants = new TL_chatParticipants();
                                        MessagesStorage.getInstance(ProfileActivity.this.currentAccount).putUsersAndChats(res.users, null, true, true);
                                        MessagesStorage.getInstance(ProfileActivity.this.currentAccount).updateChannelUsers(ProfileActivity.this.chat_id, res.participants);
                                    }
                                    for (int a = 0; a < res.participants.size(); a++) {
                                        TL_chatChannelParticipant participant = new TL_chatChannelParticipant();
                                        participant.channelParticipant = (ChannelParticipant) res.participants.get(a);
                                        participant.inviter_id = participant.channelParticipant.inviter_id;
                                        participant.user_id = participant.channelParticipant.user_id;
                                        participant.date = participant.channelParticipant.date;
                                        if (ProfileActivity.this.participantsMap.indexOfKey(participant.user_id) < 0) {
                                            ProfileActivity.this.info.participants.participants.add(participant);
                                            ProfileActivity.this.participantsMap.put(participant.user_id, participant);
                                        }
                                    }
                                }
                                ProfileActivity.this.updateOnlineCount();
                                ProfileActivity.this.loadingUsers = false;
                                ProfileActivity.this.updateRowsIds();
                                if (ProfileActivity.this.listAdapter != null) {
                                    ProfileActivity.this.listAdapter.notifyDataSetChanged();
                                }
                            }
                        }, (long) delay);
                    }
                }), this.classGuid);
            }
        }
    }

    private void openAddMember() {
        Bundle args = new Bundle();
        args.putBoolean("onlyUsers", true);
        args.putBoolean("destroyAfterSelect", true);
        args.putBoolean("returnAsResult", true);
        args.putBoolean("needForwardCount", true ^ ChatObject.isChannel(this.currentChat));
        if (this.chat_id > 0) {
            if (ChatObject.canAddViaLink(this.currentChat)) {
                args.putInt("chat_id", this.currentChat.id);
            }
            args.putString("selectAlertString", LocaleController.getString("AddToTheGroup", R.string.AddToTheGroup));
        }
        ContactsActivity fragment = new ContactsActivity(args);
        fragment.setDelegate(new ContactsActivityDelegate() {
            public void didSelectContact(User user, String param, ContactsActivity activity) {
                MessagesController.getInstance(ProfileActivity.this.currentAccount).addUserToChat(ProfileActivity.this.chat_id, user, ProfileActivity.this.info, param != null ? Utilities.parseInt(param).intValue() : 0, null, ProfileActivity.this);
            }
        });
        if (!(this.info == null || this.info.participants == null)) {
            SparseArray<User> users = new SparseArray();
            for (int a = 0; a < this.info.participants.participants.size(); a++) {
                users.put(((ChatParticipant) this.info.participants.participants.get(a)).user_id, null);
            }
            fragment.setIgnoreUsers(users);
        }
        presentFragment(fragment);
    }

    private void checkListViewScroll() {
        if (this.listView.getChildCount() > 0) {
            if (!this.openAnimationInProgress) {
                boolean z = false;
                View child = this.listView.getChildAt(0);
                Holder holder = (Holder) this.listView.findContainingViewHolder(child);
                int top = child.getTop();
                int newOffset = 0;
                if (top >= 0 && holder != null && holder.getAdapterPosition() == 0) {
                    newOffset = top;
                }
                if (this.extraHeight != newOffset) {
                    this.extraHeight = newOffset;
                    this.topView.invalidate();
                    if (this.playProfileAnimation) {
                        if (this.extraHeight != 0) {
                            z = true;
                        }
                        this.allowProfileAnimation = z;
                    }
                    needLayout();
                }
            }
        }
    }

    private void needLayout() {
        int newTop = (this.actionBar.getOccupyStatusBar() ? AndroidUtilities.statusBarHeight : 0) + ActionBar.getCurrentActionBarHeight();
        if (!(this.listView == null || this.openAnimationInProgress)) {
            FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) this.listView.getLayoutParams();
            if (layoutParams.topMargin != newTop) {
                layoutParams.topMargin = newTop;
                this.listView.setLayoutParams(layoutParams);
            }
        }
        if (this.avatarImage != null) {
            float diff = ((float) this.extraHeight) / ((float) AndroidUtilities.dp(88.0f));
            this.listView.setTopGlowOffset(this.extraHeight);
            if (this.writeButton != null) {
                this.writeButton.setTranslationY((float) ((((this.actionBar.getOccupyStatusBar() ? AndroidUtilities.statusBarHeight : 0) + ActionBar.getCurrentActionBarHeight()) + this.extraHeight) - AndroidUtilities.dp(29.5f)));
                if (!this.openAnimationInProgress) {
                    boolean setVisible = diff > 0.2f;
                    if (setVisible != (this.writeButton.getTag() == null)) {
                        AnimatorSet old;
                        if (setVisible) {
                            this.writeButton.setTag(null);
                        } else {
                            this.writeButton.setTag(Integer.valueOf(0));
                        }
                        if (this.writeButtonAnimation != null) {
                            old = this.writeButtonAnimation;
                            this.writeButtonAnimation = null;
                            old.cancel();
                        }
                        this.writeButtonAnimation = new AnimatorSet();
                        Animator[] animatorArr;
                        if (setVisible) {
                            this.writeButtonAnimation.setInterpolator(new DecelerateInterpolator());
                            AnimatorSet animatorSet = this.writeButtonAnimation;
                            animatorArr = new Animator[3];
                            animatorArr[0] = ObjectAnimator.ofFloat(this.writeButton, "scaleX", new float[]{1.0f});
                            animatorArr[1] = ObjectAnimator.ofFloat(this.writeButton, "scaleY", new float[]{1.0f});
                            animatorArr[2] = ObjectAnimator.ofFloat(this.writeButton, "alpha", new float[]{1.0f});
                            animatorSet.playTogether(animatorArr);
                        } else {
                            this.writeButtonAnimation.setInterpolator(new AccelerateInterpolator());
                            old = this.writeButtonAnimation;
                            animatorArr = new Animator[3];
                            animatorArr[0] = ObjectAnimator.ofFloat(this.writeButton, "scaleX", new float[]{0.2f});
                            animatorArr[1] = ObjectAnimator.ofFloat(this.writeButton, "scaleY", new float[]{0.2f});
                            animatorArr[2] = ObjectAnimator.ofFloat(this.writeButton, "alpha", new float[]{0.0f});
                            old.playTogether(animatorArr);
                        }
                        this.writeButtonAnimation.setDuration(150);
                        this.writeButtonAnimation.addListener(new AnimatorListenerAdapter() {
                            public void onAnimationEnd(Animator animation) {
                                if (ProfileActivity.this.writeButtonAnimation != null && ProfileActivity.this.writeButtonAnimation.equals(animation)) {
                                    ProfileActivity.this.writeButtonAnimation = null;
                                }
                            }
                        });
                        this.writeButtonAnimation.start();
                    }
                }
            }
            float avatarY = ((((float) (this.actionBar.getOccupyStatusBar() ? AndroidUtilities.statusBarHeight : 0)) + ((((float) ActionBar.getCurrentActionBarHeight()) / 2.0f) * (1.0f + diff))) - (21.0f * AndroidUtilities.density)) + ((27.0f * AndroidUtilities.density) * diff);
            this.avatarImage.setScaleX(((18.0f * diff) + 42.0f) / 42.0f);
            this.avatarImage.setScaleY(((18.0f * diff) + 42.0f) / 42.0f);
            this.avatarImage.setTranslationX(((float) (-AndroidUtilities.dp(47.0f))) * diff);
            this.avatarImage.setTranslationY((float) Math.ceil((double) avatarY));
            for (int a = 0; a < 2; a++) {
                if (this.nameTextView[a] != null) {
                    this.nameTextView[a].setTranslationX((AndroidUtilities.density * -21.0f) * diff);
                    this.nameTextView[a].setTranslationY((((float) Math.floor((double) avatarY)) + ((float) AndroidUtilities.dp(1.3f))) + (((float) AndroidUtilities.dp(7.0f)) * diff));
                    this.onlineTextView[a].setTranslationX((-21.0f * AndroidUtilities.density) * diff);
                    this.onlineTextView[a].setTranslationY((((float) Math.floor((double) avatarY)) + ((float) AndroidUtilities.dp(24.0f))) + (((float) Math.floor((double) (11.0f * AndroidUtilities.density))) * diff));
                    this.nameTextView[a].setScaleX((0.12f * diff) + 1.0f);
                    this.nameTextView[a].setScaleY((0.12f * diff) + 1.0f);
                    if (a == 1 && !this.openAnimationInProgress) {
                        int width;
                        int i;
                        FrameLayout.LayoutParams layoutParams2;
                        if (AndroidUtilities.isTablet()) {
                            width = AndroidUtilities.dp(1140129792);
                        } else {
                            width = AndroidUtilities.displaySize.x;
                        }
                        if (this.callItem == null) {
                            if (this.editItem == null) {
                                i = 0;
                                width = (int) (((float) (width - AndroidUtilities.dp(126.0f + (((float) (40 + i)) * (1.0f - diff))))) - this.nameTextView[a].getTranslationX());
                                layoutParams2 = (FrameLayout.LayoutParams) this.nameTextView[a].getLayoutParams();
                                if (((float) width) >= (this.nameTextView[a].getPaint().measureText(this.nameTextView[a].getText().toString()) * this.nameTextView[a].getScaleX()) + ((float) this.nameTextView[a].getSideDrawablesSize())) {
                                    layoutParams2.width = (int) Math.ceil((double) (((float) width) / this.nameTextView[a].getScaleX()));
                                } else {
                                    layoutParams2.width = -2;
                                }
                                this.nameTextView[a].setLayoutParams(layoutParams2);
                                layoutParams2 = (FrameLayout.LayoutParams) this.onlineTextView[a].getLayoutParams();
                                layoutParams2.rightMargin = (int) Math.ceil((double) ((this.onlineTextView[a].getTranslationX() + ((float) AndroidUtilities.dp(8.0f))) + (((float) AndroidUtilities.dp(40.0f)) * (1.0f - diff))));
                                this.onlineTextView[a].setLayoutParams(layoutParams2);
                            }
                        }
                        i = 48;
                        width = (int) (((float) (width - AndroidUtilities.dp(126.0f + (((float) (40 + i)) * (1.0f - diff))))) - this.nameTextView[a].getTranslationX());
                        layoutParams2 = (FrameLayout.LayoutParams) this.nameTextView[a].getLayoutParams();
                        if (((float) width) >= (this.nameTextView[a].getPaint().measureText(this.nameTextView[a].getText().toString()) * this.nameTextView[a].getScaleX()) + ((float) this.nameTextView[a].getSideDrawablesSize())) {
                            layoutParams2.width = -2;
                        } else {
                            layoutParams2.width = (int) Math.ceil((double) (((float) width) / this.nameTextView[a].getScaleX()));
                        }
                        this.nameTextView[a].setLayoutParams(layoutParams2);
                        layoutParams2 = (FrameLayout.LayoutParams) this.onlineTextView[a].getLayoutParams();
                        layoutParams2.rightMargin = (int) Math.ceil((double) ((this.onlineTextView[a].getTranslationX() + ((float) AndroidUtilities.dp(8.0f))) + (((float) AndroidUtilities.dp(40.0f)) * (1.0f - diff))));
                        this.onlineTextView[a].setLayoutParams(layoutParams2);
                    }
                }
            }
        }
    }

    private void fixLayout() {
        if (this.fragmentView != null) {
            this.fragmentView.getViewTreeObserver().addOnPreDrawListener(new OnPreDrawListener() {
                public boolean onPreDraw() {
                    if (ProfileActivity.this.fragmentView != null) {
                        ProfileActivity.this.checkListViewScroll();
                        ProfileActivity.this.needLayout();
                        ProfileActivity.this.fragmentView.getViewTreeObserver().removeOnPreDrawListener(this);
                    }
                    return true;
                }
            });
        }
    }

    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        fixLayout();
    }

    public void didReceivedNotification(int id, int account, final Object... args) {
        int a = 0;
        int mask;
        Chat newChat;
        if (id == NotificationCenter.updateInterfaces) {
            mask = ((Integer) args[0]).intValue();
            if (this.user_id != 0) {
                if (!((mask & 2) == 0 && (mask & 1) == 0 && (mask & 4) == 0)) {
                    updateProfileData();
                }
                if (!((mask & 1024) == 0 || this.listView == 0)) {
                    Holder a2 = (Holder) this.listView.findViewHolderForPosition(this.phoneRow);
                    if (a2 != null) {
                        this.listAdapter.onBindViewHolder(a2, this.phoneRow);
                    }
                }
            } else if (this.chat_id != 0) {
                if ((mask & MessagesController.UPDATE_MASK_CHAT_ADMINS) != 0) {
                    newChat = MessagesController.getInstance(this.currentAccount).getChat(Integer.valueOf(this.chat_id));
                    if (newChat != null) {
                        this.currentChat = newChat;
                        createActionBarMenu();
                        updateRowsIds();
                        if (this.listAdapter != null) {
                            this.listAdapter.notifyDataSetChanged();
                        }
                    }
                }
                if (!((mask & MessagesController.UPDATE_MASK_CHANNEL) == 0 && (mask & 8) == 0 && (mask & 16) == 0 && (mask & 32) == 0 && (mask & 4) == 0)) {
                    updateOnlineCount();
                    updateProfileData();
                }
                if ((mask & MessagesController.UPDATE_MASK_CHANNEL) != 0) {
                    updateRowsIds();
                    if (this.listAdapter != null) {
                        this.listAdapter.notifyDataSetChanged();
                    }
                }
                if (!(((mask & 2) == 0 && (mask & 1) == 0 && (mask & 4) == 0) || this.listView == null)) {
                    int count = this.listView.getChildCount();
                    while (a < count) {
                        View child = this.listView.getChildAt(a);
                        if (child instanceof UserCell) {
                            ((UserCell) child).update(mask);
                        }
                        a++;
                    }
                }
            }
        } else if (id == NotificationCenter.contactsDidLoaded) {
            createActionBarMenu();
        } else if (id == NotificationCenter.mediaCountDidLoaded) {
            long uid = ((Long) args[0]).longValue();
            long did = this.dialog_id;
            if (did == 0) {
                if (this.user_id != 0) {
                    did = (long) this.user_id;
                } else if (this.chat_id != 0) {
                    did = (long) (-this.chat_id);
                }
            }
            if (uid == did || uid == this.mergeDialogId) {
                if (uid == did) {
                    this.totalMediaCount = ((Integer) args[1]).intValue();
                } else {
                    this.totalMediaCountMerge = ((Integer) args[1]).intValue();
                }
                if (this.listView != null) {
                    mask = this.listView.getChildCount();
                    while (a < mask) {
                        Holder holder = (Holder) this.listView.getChildViewHolder(this.listView.getChildAt(a));
                        if (holder.getAdapterPosition() == this.sharedMediaRow) {
                            this.listAdapter.onBindViewHolder(holder, this.sharedMediaRow);
                            break;
                        }
                        a++;
                    }
                }
            }
        } else if (id == NotificationCenter.encryptedChatCreated) {
            if (this.creatingChat) {
                AndroidUtilities.runOnUIThread(new Runnable() {
                    public void run() {
                        NotificationCenter.getInstance(ProfileActivity.this.currentAccount).removeObserver(ProfileActivity.this, NotificationCenter.closeChats);
                        NotificationCenter.getInstance(ProfileActivity.this.currentAccount).postNotificationName(NotificationCenter.closeChats, new Object[0]);
                        EncryptedChat encryptedChat = args[0];
                        Bundle args2 = new Bundle();
                        args2.putInt("enc_id", encryptedChat.id);
                        ProfileActivity.this.presentFragment(new ChatActivity(args2), true);
                    }
                });
            }
        } else if (id == NotificationCenter.encryptedChatUpdated) {
            EncryptedChat chat = args[0];
            if (this.currentEncryptedChat != 0 && chat.id == this.currentEncryptedChat.id) {
                this.currentEncryptedChat = chat;
                updateRowsIds();
                if (this.listAdapter != 0) {
                    this.listAdapter.notifyDataSetChanged();
                }
            }
        } else if (id == NotificationCenter.blockedUsersDidLoaded) {
            boolean oldValue = this.userBlocked;
            this.userBlocked = MessagesController.getInstance(this.currentAccount).blockedUsers.contains(Integer.valueOf(this.user_id));
            if (oldValue != this.userBlocked) {
                createActionBarMenu();
            }
        } else if (id == NotificationCenter.chatInfoDidLoaded) {
            ChatFull chatFull = args[0];
            if (chatFull.id == this.chat_id) {
                boolean byChannelUsers = ((Boolean) args[2]).booleanValue();
                if ((this.info instanceof TL_channelFull) && chatFull.participants == null && this.info != null) {
                    chatFull.participants = this.info.participants;
                }
                if (this.info == null && (chatFull instanceof TL_channelFull)) {
                    a = 1;
                }
                this.info = chatFull;
                if (this.mergeDialogId == 0 && this.info.migrated_from_chat_id != 0) {
                    this.mergeDialogId = (long) (-this.info.migrated_from_chat_id);
                    DataQuery.getInstance(this.currentAccount).getMediaCount(this.mergeDialogId, 0, this.classGuid, true);
                }
                fetchUsersFromChannelInfo();
                updateOnlineCount();
                updateRowsIds();
                if (this.listAdapter != null) {
                    this.listAdapter.notifyDataSetChanged();
                }
                newChat = MessagesController.getInstance(this.currentAccount).getChat(Integer.valueOf(this.chat_id));
                if (newChat != null) {
                    this.currentChat = newChat;
                    createActionBarMenu();
                }
                if (this.currentChat.megagroup && !(a == 0 && byChannelUsers)) {
                    getChannelParticipants(true);
                }
            }
        } else if (id == NotificationCenter.closeChats) {
            removeSelfFromStack();
        } else if (id == NotificationCenter.botInfoDidLoaded) {
            BotInfo info = args[0];
            if (info.user_id == this.user_id) {
                this.botInfo = info;
                updateRowsIds();
                if (this.listAdapter != 0) {
                    this.listAdapter.notifyDataSetChanged();
                }
            }
        } else if (id == NotificationCenter.userInfoDidLoaded) {
            if (((Integer) args[0]).intValue() == this.user_id) {
                if (this.openAnimationInProgress == 0 && this.callItem == 0) {
                    createActionBarMenu();
                } else {
                    this.recreateMenuAfterAnimation = true;
                }
                updateRowsIds();
                if (this.listAdapter != 0) {
                    this.listAdapter.notifyDataSetChanged();
                }
            }
        } else if (id == NotificationCenter.didReceivedNewMessages && ((Long) args[0]).longValue() == this.dialog_id) {
            ArrayList<MessageObject> arr = args[1];
            while (a < arr.size()) {
                MessageObject obj = (MessageObject) arr.get(a);
                if (this.currentEncryptedChat != null && obj.messageOwner.action != null && (obj.messageOwner.action instanceof TL_messageEncryptedAction) && (obj.messageOwner.action.encryptedAction instanceof TL_decryptedMessageActionSetMessageTTL)) {
                    TL_decryptedMessageActionSetMessageTTL action = obj.messageOwner.action.encryptedAction;
                    if (this.listAdapter != null) {
                        this.listAdapter.notifyDataSetChanged();
                    }
                }
                a++;
            }
        }
    }

    public void onResume() {
        super.onResume();
        if (this.listAdapter != null) {
            this.listAdapter.notifyDataSetChanged();
        }
        updateProfileData();
        fixLayout();
    }

    public void setPlayProfileAnimation(boolean value) {
        SharedPreferences preferences = MessagesController.getGlobalMainSettings();
        if (!AndroidUtilities.isTablet() && preferences.getBoolean("view_animations", true)) {
            this.playProfileAnimation = value;
        }
    }

    protected void onTransitionAnimationStart(boolean isOpen, boolean backward) {
        if (!backward && this.playProfileAnimation && this.allowProfileAnimation) {
            this.openAnimationInProgress = true;
        }
        NotificationCenter.getInstance(this.currentAccount).setAllowedNotificationsDutingAnimation(new int[]{NotificationCenter.dialogsNeedReload, NotificationCenter.closeChats, NotificationCenter.mediaCountDidLoaded});
        NotificationCenter.getInstance(this.currentAccount).setAnimationInProgress(true);
    }

    protected void onTransitionAnimationEnd(boolean isOpen, boolean backward) {
        if (!backward && this.playProfileAnimation && this.allowProfileAnimation) {
            this.openAnimationInProgress = false;
            if (this.recreateMenuAfterAnimation) {
                createActionBarMenu();
            }
        }
        NotificationCenter.getInstance(this.currentAccount).setAnimationInProgress(false);
    }

    public float getAnimationProgress() {
        return this.animationProgress;
    }

    @Keep
    public void setAnimationProgress(float progress) {
        int color;
        int actionBarColor;
        int r;
        int g;
        int b;
        int i;
        int titleColor;
        int a;
        int rD;
        int gD;
        int bD;
        int aD;
        int i2;
        int i3;
        int i4;
        int color2;
        int actionBarColor2;
        int r2;
        int g2;
        int i5;
        int color3;
        int subtitleColor;
        int r3;
        int g3;
        float f = progress;
        this.animationProgress = f;
        this.listView.setAlpha(f);
        this.listView.setTranslationX(((float) AndroidUtilities.dp(48.0f)) - (((float) AndroidUtilities.dp(48.0f)) * f));
        if (this.user_id == 0) {
            if (!ChatObject.isChannel(r0.chat_id, r0.currentAccount) || r0.currentChat.megagroup) {
                color = r0.chat_id;
                color = AvatarDrawable.getProfileBackColorForId(color);
                actionBarColor = Theme.getColor(Theme.key_actionBarDefault);
                r = Color.red(actionBarColor);
                g = Color.green(actionBarColor);
                b = Color.blue(actionBarColor);
                r0.topView.setBackgroundColor(Color.rgb(r + ((int) (((float) (Color.red(color) - r)) * f)), g + ((int) (((float) (Color.green(color) - g)) * f)), b + ((int) (((float) (Color.blue(color) - b)) * f))));
                if (r0.user_id == 0) {
                    if (ChatObject.isChannel(r0.chat_id, r0.currentAccount) || r0.currentChat.megagroup) {
                        i = r0.chat_id;
                        color = AvatarDrawable.getIconColorForId(i);
                        i = Theme.getColor(Theme.key_actionBarDefaultIcon);
                        r = Color.red(i);
                        g = Color.green(i);
                        b = Color.blue(i);
                        r0.actionBar.setItemsColor(Color.rgb(r + ((int) (((float) (Color.red(color) - r)) * f)), g + ((int) (((float) (Color.green(color) - g)) * f)), b + ((int) (((float) (Color.blue(color) - b)) * f))), false);
                        color = Theme.getColor(Theme.key_profile_title);
                        titleColor = Theme.getColor(Theme.key_actionBarDefaultTitle);
                        r = Color.red(titleColor);
                        g = Color.green(titleColor);
                        b = Color.blue(titleColor);
                        a = Color.alpha(titleColor);
                        rD = (int) (((float) (Color.red(color) - r)) * f);
                        gD = (int) (((float) (Color.green(color) - g)) * f);
                        bD = (int) (((float) (Color.blue(color) - b)) * f);
                        aD = (int) (((float) (Color.alpha(color) - a)) * f);
                        i2 = 0;
                        while (true) {
                            i3 = 2;
                            i4 = i2;
                            if (i4 >= 2) {
                                break;
                            }
                            if (r0.nameTextView[i4] != null) {
                                color2 = color;
                                actionBarColor2 = actionBarColor;
                                r2 = r;
                                g2 = g;
                            } else {
                                color2 = color;
                                actionBarColor2 = actionBarColor;
                                r2 = r;
                                g2 = g;
                                r0.nameTextView[i4].setTextColor(Color.argb(a + aD, r + rD, g + gD, b + bD));
                            }
                            i2 = i4 + 1;
                            color = color2;
                            actionBarColor = actionBarColor2;
                            r = r2;
                            g = g2;
                        }
                        actionBarColor2 = actionBarColor;
                        r2 = r;
                        g2 = g;
                        if (r0.user_id == 0) {
                            if (ChatObject.isChannel(r0.chat_id, r0.currentAccount) || r0.currentChat.megagroup) {
                                color = r0.chat_id;
                                color = AvatarDrawable.getProfileTextColorForId(color);
                                actionBarColor = Theme.getColor(Theme.key_actionBarDefaultSubtitle);
                                r = Color.red(actionBarColor);
                                g = Color.green(actionBarColor);
                                b = Color.blue(actionBarColor);
                                a = Color.alpha(actionBarColor);
                                rD = (int) (((float) (Color.red(color) - r)) * f);
                                gD = (int) (((float) (Color.green(color) - g)) * f);
                                bD = (int) (((float) (Color.blue(color) - b)) * f);
                                i4 = (int) (((float) (Color.alpha(color) - a)) * f);
                                i5 = 0;
                                while (true) {
                                    aD = i5;
                                    if (aD >= i3) {
                                        break;
                                    }
                                    if (r0.onlineTextView[aD] != null) {
                                        color3 = color;
                                        subtitleColor = actionBarColor;
                                        r3 = r;
                                        g3 = g;
                                    } else {
                                        color3 = color;
                                        subtitleColor = actionBarColor;
                                        r3 = r;
                                        g3 = g;
                                        r0.onlineTextView[aD].setTextColor(Color.argb(a + i4, r + rD, g + gD, b + bD));
                                    }
                                    i5 = aD + 1;
                                    color = color3;
                                    actionBarColor = subtitleColor;
                                    r = r3;
                                    g = g3;
                                    i3 = 2;
                                }
                                subtitleColor = actionBarColor;
                                r3 = r;
                                g3 = g;
                                r0.extraHeight = (int) (((float) r0.initialAnimationExtraHeight) * f);
                                color = AvatarDrawable.getProfileColorForId(r0.user_id == 0 ? r0.user_id : r0.chat_id);
                                i3 = AvatarDrawable.getColorForId(r0.user_id == 0 ? r0.user_id : r0.chat_id);
                                if (color != i3) {
                                    r0.avatarDrawable.setColor(Color.rgb(Color.red(i3) + ((int) (((float) (Color.red(color) - Color.red(i3))) * f)), Color.green(i3) + ((int) (((float) (Color.green(color) - Color.green(i3))) * f)), Color.blue(i3) + ((int) (((float) (Color.blue(color) - Color.blue(i3))) * f))));
                                    r0.avatarImage.invalidate();
                                }
                                needLayout();
                            }
                        }
                        color = 5;
                        color = AvatarDrawable.getProfileTextColorForId(color);
                        actionBarColor = Theme.getColor(Theme.key_actionBarDefaultSubtitle);
                        r = Color.red(actionBarColor);
                        g = Color.green(actionBarColor);
                        b = Color.blue(actionBarColor);
                        a = Color.alpha(actionBarColor);
                        rD = (int) (((float) (Color.red(color) - r)) * f);
                        gD = (int) (((float) (Color.green(color) - g)) * f);
                        bD = (int) (((float) (Color.blue(color) - b)) * f);
                        i4 = (int) (((float) (Color.alpha(color) - a)) * f);
                        i5 = 0;
                        while (true) {
                            aD = i5;
                            if (aD >= i3) {
                                break;
                            }
                            if (r0.onlineTextView[aD] != null) {
                                color3 = color;
                                subtitleColor = actionBarColor;
                                r3 = r;
                                g3 = g;
                                r0.onlineTextView[aD].setTextColor(Color.argb(a + i4, r + rD, g + gD, b + bD));
                            } else {
                                color3 = color;
                                subtitleColor = actionBarColor;
                                r3 = r;
                                g3 = g;
                            }
                            i5 = aD + 1;
                            color = color3;
                            actionBarColor = subtitleColor;
                            r = r3;
                            g = g3;
                            i3 = 2;
                        }
                        subtitleColor = actionBarColor;
                        r3 = r;
                        g3 = g;
                        r0.extraHeight = (int) (((float) r0.initialAnimationExtraHeight) * f);
                        if (r0.user_id == 0) {
                        }
                        color = AvatarDrawable.getProfileColorForId(r0.user_id == 0 ? r0.user_id : r0.chat_id);
                        if (r0.user_id == 0) {
                        }
                        i3 = AvatarDrawable.getColorForId(r0.user_id == 0 ? r0.user_id : r0.chat_id);
                        if (color != i3) {
                            r0.avatarDrawable.setColor(Color.rgb(Color.red(i3) + ((int) (((float) (Color.red(color) - Color.red(i3))) * f)), Color.green(i3) + ((int) (((float) (Color.green(color) - Color.green(i3))) * f)), Color.blue(i3) + ((int) (((float) (Color.blue(color) - Color.blue(i3))) * f))));
                            r0.avatarImage.invalidate();
                        }
                        needLayout();
                    }
                }
                i = 5;
                color = AvatarDrawable.getIconColorForId(i);
                i = Theme.getColor(Theme.key_actionBarDefaultIcon);
                r = Color.red(i);
                g = Color.green(i);
                b = Color.blue(i);
                r0.actionBar.setItemsColor(Color.rgb(r + ((int) (((float) (Color.red(color) - r)) * f)), g + ((int) (((float) (Color.green(color) - g)) * f)), b + ((int) (((float) (Color.blue(color) - b)) * f))), false);
                color = Theme.getColor(Theme.key_profile_title);
                titleColor = Theme.getColor(Theme.key_actionBarDefaultTitle);
                r = Color.red(titleColor);
                g = Color.green(titleColor);
                b = Color.blue(titleColor);
                a = Color.alpha(titleColor);
                rD = (int) (((float) (Color.red(color) - r)) * f);
                gD = (int) (((float) (Color.green(color) - g)) * f);
                bD = (int) (((float) (Color.blue(color) - b)) * f);
                aD = (int) (((float) (Color.alpha(color) - a)) * f);
                i2 = 0;
                while (true) {
                    i3 = 2;
                    i4 = i2;
                    if (i4 >= 2) {
                        break;
                    }
                    if (r0.nameTextView[i4] != null) {
                        color2 = color;
                        actionBarColor2 = actionBarColor;
                        r2 = r;
                        g2 = g;
                        r0.nameTextView[i4].setTextColor(Color.argb(a + aD, r + rD, g + gD, b + bD));
                    } else {
                        color2 = color;
                        actionBarColor2 = actionBarColor;
                        r2 = r;
                        g2 = g;
                    }
                    i2 = i4 + 1;
                    color = color2;
                    actionBarColor = actionBarColor2;
                    r = r2;
                    g = g2;
                }
                actionBarColor2 = actionBarColor;
                r2 = r;
                g2 = g;
                if (r0.user_id == 0) {
                    if (ChatObject.isChannel(r0.chat_id, r0.currentAccount)) {
                    }
                    color = r0.chat_id;
                    color = AvatarDrawable.getProfileTextColorForId(color);
                    actionBarColor = Theme.getColor(Theme.key_actionBarDefaultSubtitle);
                    r = Color.red(actionBarColor);
                    g = Color.green(actionBarColor);
                    b = Color.blue(actionBarColor);
                    a = Color.alpha(actionBarColor);
                    rD = (int) (((float) (Color.red(color) - r)) * f);
                    gD = (int) (((float) (Color.green(color) - g)) * f);
                    bD = (int) (((float) (Color.blue(color) - b)) * f);
                    i4 = (int) (((float) (Color.alpha(color) - a)) * f);
                    i5 = 0;
                    while (true) {
                        aD = i5;
                        if (aD >= i3) {
                            break;
                        }
                        if (r0.onlineTextView[aD] != null) {
                            color3 = color;
                            subtitleColor = actionBarColor;
                            r3 = r;
                            g3 = g;
                        } else {
                            color3 = color;
                            subtitleColor = actionBarColor;
                            r3 = r;
                            g3 = g;
                            r0.onlineTextView[aD].setTextColor(Color.argb(a + i4, r + rD, g + gD, b + bD));
                        }
                        i5 = aD + 1;
                        color = color3;
                        actionBarColor = subtitleColor;
                        r = r3;
                        g = g3;
                        i3 = 2;
                    }
                    subtitleColor = actionBarColor;
                    r3 = r;
                    g3 = g;
                    r0.extraHeight = (int) (((float) r0.initialAnimationExtraHeight) * f);
                    if (r0.user_id == 0) {
                    }
                    color = AvatarDrawable.getProfileColorForId(r0.user_id == 0 ? r0.user_id : r0.chat_id);
                    if (r0.user_id == 0) {
                    }
                    i3 = AvatarDrawable.getColorForId(r0.user_id == 0 ? r0.user_id : r0.chat_id);
                    if (color != i3) {
                        r0.avatarDrawable.setColor(Color.rgb(Color.red(i3) + ((int) (((float) (Color.red(color) - Color.red(i3))) * f)), Color.green(i3) + ((int) (((float) (Color.green(color) - Color.green(i3))) * f)), Color.blue(i3) + ((int) (((float) (Color.blue(color) - Color.blue(i3))) * f))));
                        r0.avatarImage.invalidate();
                    }
                    needLayout();
                }
                color = 5;
                color = AvatarDrawable.getProfileTextColorForId(color);
                actionBarColor = Theme.getColor(Theme.key_actionBarDefaultSubtitle);
                r = Color.red(actionBarColor);
                g = Color.green(actionBarColor);
                b = Color.blue(actionBarColor);
                a = Color.alpha(actionBarColor);
                rD = (int) (((float) (Color.red(color) - r)) * f);
                gD = (int) (((float) (Color.green(color) - g)) * f);
                bD = (int) (((float) (Color.blue(color) - b)) * f);
                i4 = (int) (((float) (Color.alpha(color) - a)) * f);
                i5 = 0;
                while (true) {
                    aD = i5;
                    if (aD >= i3) {
                        break;
                    }
                    if (r0.onlineTextView[aD] != null) {
                        color3 = color;
                        subtitleColor = actionBarColor;
                        r3 = r;
                        g3 = g;
                        r0.onlineTextView[aD].setTextColor(Color.argb(a + i4, r + rD, g + gD, b + bD));
                    } else {
                        color3 = color;
                        subtitleColor = actionBarColor;
                        r3 = r;
                        g3 = g;
                    }
                    i5 = aD + 1;
                    color = color3;
                    actionBarColor = subtitleColor;
                    r = r3;
                    g = g3;
                    i3 = 2;
                }
                subtitleColor = actionBarColor;
                r3 = r;
                g3 = g;
                r0.extraHeight = (int) (((float) r0.initialAnimationExtraHeight) * f);
                if (r0.user_id == 0) {
                }
                color = AvatarDrawable.getProfileColorForId(r0.user_id == 0 ? r0.user_id : r0.chat_id);
                if (r0.user_id == 0) {
                }
                i3 = AvatarDrawable.getColorForId(r0.user_id == 0 ? r0.user_id : r0.chat_id);
                if (color != i3) {
                    r0.avatarDrawable.setColor(Color.rgb(Color.red(i3) + ((int) (((float) (Color.red(color) - Color.red(i3))) * f)), Color.green(i3) + ((int) (((float) (Color.green(color) - Color.green(i3))) * f)), Color.blue(i3) + ((int) (((float) (Color.blue(color) - Color.blue(i3))) * f))));
                    r0.avatarImage.invalidate();
                }
                needLayout();
            }
        }
        color = 5;
        color = AvatarDrawable.getProfileBackColorForId(color);
        actionBarColor = Theme.getColor(Theme.key_actionBarDefault);
        r = Color.red(actionBarColor);
        g = Color.green(actionBarColor);
        b = Color.blue(actionBarColor);
        r0.topView.setBackgroundColor(Color.rgb(r + ((int) (((float) (Color.red(color) - r)) * f)), g + ((int) (((float) (Color.green(color) - g)) * f)), b + ((int) (((float) (Color.blue(color) - b)) * f))));
        if (r0.user_id == 0) {
            if (ChatObject.isChannel(r0.chat_id, r0.currentAccount)) {
            }
            i = r0.chat_id;
            color = AvatarDrawable.getIconColorForId(i);
            i = Theme.getColor(Theme.key_actionBarDefaultIcon);
            r = Color.red(i);
            g = Color.green(i);
            b = Color.blue(i);
            r0.actionBar.setItemsColor(Color.rgb(r + ((int) (((float) (Color.red(color) - r)) * f)), g + ((int) (((float) (Color.green(color) - g)) * f)), b + ((int) (((float) (Color.blue(color) - b)) * f))), false);
            color = Theme.getColor(Theme.key_profile_title);
            titleColor = Theme.getColor(Theme.key_actionBarDefaultTitle);
            r = Color.red(titleColor);
            g = Color.green(titleColor);
            b = Color.blue(titleColor);
            a = Color.alpha(titleColor);
            rD = (int) (((float) (Color.red(color) - r)) * f);
            gD = (int) (((float) (Color.green(color) - g)) * f);
            bD = (int) (((float) (Color.blue(color) - b)) * f);
            aD = (int) (((float) (Color.alpha(color) - a)) * f);
            i2 = 0;
            while (true) {
                i3 = 2;
                i4 = i2;
                if (i4 >= 2) {
                    break;
                }
                if (r0.nameTextView[i4] != null) {
                    color2 = color;
                    actionBarColor2 = actionBarColor;
                    r2 = r;
                    g2 = g;
                } else {
                    color2 = color;
                    actionBarColor2 = actionBarColor;
                    r2 = r;
                    g2 = g;
                    r0.nameTextView[i4].setTextColor(Color.argb(a + aD, r + rD, g + gD, b + bD));
                }
                i2 = i4 + 1;
                color = color2;
                actionBarColor = actionBarColor2;
                r = r2;
                g = g2;
            }
            actionBarColor2 = actionBarColor;
            r2 = r;
            g2 = g;
            if (r0.user_id == 0) {
                if (ChatObject.isChannel(r0.chat_id, r0.currentAccount)) {
                }
                color = r0.chat_id;
                color = AvatarDrawable.getProfileTextColorForId(color);
                actionBarColor = Theme.getColor(Theme.key_actionBarDefaultSubtitle);
                r = Color.red(actionBarColor);
                g = Color.green(actionBarColor);
                b = Color.blue(actionBarColor);
                a = Color.alpha(actionBarColor);
                rD = (int) (((float) (Color.red(color) - r)) * f);
                gD = (int) (((float) (Color.green(color) - g)) * f);
                bD = (int) (((float) (Color.blue(color) - b)) * f);
                i4 = (int) (((float) (Color.alpha(color) - a)) * f);
                i5 = 0;
                while (true) {
                    aD = i5;
                    if (aD >= i3) {
                        break;
                    }
                    if (r0.onlineTextView[aD] != null) {
                        color3 = color;
                        subtitleColor = actionBarColor;
                        r3 = r;
                        g3 = g;
                    } else {
                        color3 = color;
                        subtitleColor = actionBarColor;
                        r3 = r;
                        g3 = g;
                        r0.onlineTextView[aD].setTextColor(Color.argb(a + i4, r + rD, g + gD, b + bD));
                    }
                    i5 = aD + 1;
                    color = color3;
                    actionBarColor = subtitleColor;
                    r = r3;
                    g = g3;
                    i3 = 2;
                }
                subtitleColor = actionBarColor;
                r3 = r;
                g3 = g;
                r0.extraHeight = (int) (((float) r0.initialAnimationExtraHeight) * f);
                if (r0.user_id == 0) {
                }
                color = AvatarDrawable.getProfileColorForId(r0.user_id == 0 ? r0.user_id : r0.chat_id);
                if (r0.user_id == 0) {
                }
                i3 = AvatarDrawable.getColorForId(r0.user_id == 0 ? r0.user_id : r0.chat_id);
                if (color != i3) {
                    r0.avatarDrawable.setColor(Color.rgb(Color.red(i3) + ((int) (((float) (Color.red(color) - Color.red(i3))) * f)), Color.green(i3) + ((int) (((float) (Color.green(color) - Color.green(i3))) * f)), Color.blue(i3) + ((int) (((float) (Color.blue(color) - Color.blue(i3))) * f))));
                    r0.avatarImage.invalidate();
                }
                needLayout();
            }
            color = 5;
            color = AvatarDrawable.getProfileTextColorForId(color);
            actionBarColor = Theme.getColor(Theme.key_actionBarDefaultSubtitle);
            r = Color.red(actionBarColor);
            g = Color.green(actionBarColor);
            b = Color.blue(actionBarColor);
            a = Color.alpha(actionBarColor);
            rD = (int) (((float) (Color.red(color) - r)) * f);
            gD = (int) (((float) (Color.green(color) - g)) * f);
            bD = (int) (((float) (Color.blue(color) - b)) * f);
            i4 = (int) (((float) (Color.alpha(color) - a)) * f);
            i5 = 0;
            while (true) {
                aD = i5;
                if (aD >= i3) {
                    break;
                }
                if (r0.onlineTextView[aD] != null) {
                    color3 = color;
                    subtitleColor = actionBarColor;
                    r3 = r;
                    g3 = g;
                    r0.onlineTextView[aD].setTextColor(Color.argb(a + i4, r + rD, g + gD, b + bD));
                } else {
                    color3 = color;
                    subtitleColor = actionBarColor;
                    r3 = r;
                    g3 = g;
                }
                i5 = aD + 1;
                color = color3;
                actionBarColor = subtitleColor;
                r = r3;
                g = g3;
                i3 = 2;
            }
            subtitleColor = actionBarColor;
            r3 = r;
            g3 = g;
            r0.extraHeight = (int) (((float) r0.initialAnimationExtraHeight) * f);
            if (r0.user_id == 0) {
            }
            color = AvatarDrawable.getProfileColorForId(r0.user_id == 0 ? r0.user_id : r0.chat_id);
            if (r0.user_id == 0) {
            }
            i3 = AvatarDrawable.getColorForId(r0.user_id == 0 ? r0.user_id : r0.chat_id);
            if (color != i3) {
                r0.avatarDrawable.setColor(Color.rgb(Color.red(i3) + ((int) (((float) (Color.red(color) - Color.red(i3))) * f)), Color.green(i3) + ((int) (((float) (Color.green(color) - Color.green(i3))) * f)), Color.blue(i3) + ((int) (((float) (Color.blue(color) - Color.blue(i3))) * f))));
                r0.avatarImage.invalidate();
            }
            needLayout();
        }
        i = 5;
        color = AvatarDrawable.getIconColorForId(i);
        i = Theme.getColor(Theme.key_actionBarDefaultIcon);
        r = Color.red(i);
        g = Color.green(i);
        b = Color.blue(i);
        r0.actionBar.setItemsColor(Color.rgb(r + ((int) (((float) (Color.red(color) - r)) * f)), g + ((int) (((float) (Color.green(color) - g)) * f)), b + ((int) (((float) (Color.blue(color) - b)) * f))), false);
        color = Theme.getColor(Theme.key_profile_title);
        titleColor = Theme.getColor(Theme.key_actionBarDefaultTitle);
        r = Color.red(titleColor);
        g = Color.green(titleColor);
        b = Color.blue(titleColor);
        a = Color.alpha(titleColor);
        rD = (int) (((float) (Color.red(color) - r)) * f);
        gD = (int) (((float) (Color.green(color) - g)) * f);
        bD = (int) (((float) (Color.blue(color) - b)) * f);
        aD = (int) (((float) (Color.alpha(color) - a)) * f);
        i2 = 0;
        while (true) {
            i3 = 2;
            i4 = i2;
            if (i4 >= 2) {
                break;
            }
            if (r0.nameTextView[i4] != null) {
                color2 = color;
                actionBarColor2 = actionBarColor;
                r2 = r;
                g2 = g;
                r0.nameTextView[i4].setTextColor(Color.argb(a + aD, r + rD, g + gD, b + bD));
            } else {
                color2 = color;
                actionBarColor2 = actionBarColor;
                r2 = r;
                g2 = g;
            }
            i2 = i4 + 1;
            color = color2;
            actionBarColor = actionBarColor2;
            r = r2;
            g = g2;
        }
        actionBarColor2 = actionBarColor;
        r2 = r;
        g2 = g;
        if (r0.user_id == 0) {
            if (ChatObject.isChannel(r0.chat_id, r0.currentAccount)) {
            }
            color = r0.chat_id;
            color = AvatarDrawable.getProfileTextColorForId(color);
            actionBarColor = Theme.getColor(Theme.key_actionBarDefaultSubtitle);
            r = Color.red(actionBarColor);
            g = Color.green(actionBarColor);
            b = Color.blue(actionBarColor);
            a = Color.alpha(actionBarColor);
            rD = (int) (((float) (Color.red(color) - r)) * f);
            gD = (int) (((float) (Color.green(color) - g)) * f);
            bD = (int) (((float) (Color.blue(color) - b)) * f);
            i4 = (int) (((float) (Color.alpha(color) - a)) * f);
            i5 = 0;
            while (true) {
                aD = i5;
                if (aD >= i3) {
                    break;
                }
                if (r0.onlineTextView[aD] != null) {
                    color3 = color;
                    subtitleColor = actionBarColor;
                    r3 = r;
                    g3 = g;
                } else {
                    color3 = color;
                    subtitleColor = actionBarColor;
                    r3 = r;
                    g3 = g;
                    r0.onlineTextView[aD].setTextColor(Color.argb(a + i4, r + rD, g + gD, b + bD));
                }
                i5 = aD + 1;
                color = color3;
                actionBarColor = subtitleColor;
                r = r3;
                g = g3;
                i3 = 2;
            }
            subtitleColor = actionBarColor;
            r3 = r;
            g3 = g;
            r0.extraHeight = (int) (((float) r0.initialAnimationExtraHeight) * f);
            if (r0.user_id == 0) {
            }
            color = AvatarDrawable.getProfileColorForId(r0.user_id == 0 ? r0.user_id : r0.chat_id);
            if (r0.user_id == 0) {
            }
            i3 = AvatarDrawable.getColorForId(r0.user_id == 0 ? r0.user_id : r0.chat_id);
            if (color != i3) {
                r0.avatarDrawable.setColor(Color.rgb(Color.red(i3) + ((int) (((float) (Color.red(color) - Color.red(i3))) * f)), Color.green(i3) + ((int) (((float) (Color.green(color) - Color.green(i3))) * f)), Color.blue(i3) + ((int) (((float) (Color.blue(color) - Color.blue(i3))) * f))));
                r0.avatarImage.invalidate();
            }
            needLayout();
        }
        color = 5;
        color = AvatarDrawable.getProfileTextColorForId(color);
        actionBarColor = Theme.getColor(Theme.key_actionBarDefaultSubtitle);
        r = Color.red(actionBarColor);
        g = Color.green(actionBarColor);
        b = Color.blue(actionBarColor);
        a = Color.alpha(actionBarColor);
        rD = (int) (((float) (Color.red(color) - r)) * f);
        gD = (int) (((float) (Color.green(color) - g)) * f);
        bD = (int) (((float) (Color.blue(color) - b)) * f);
        i4 = (int) (((float) (Color.alpha(color) - a)) * f);
        i5 = 0;
        while (true) {
            aD = i5;
            if (aD >= i3) {
                break;
            }
            if (r0.onlineTextView[aD] != null) {
                color3 = color;
                subtitleColor = actionBarColor;
                r3 = r;
                g3 = g;
                r0.onlineTextView[aD].setTextColor(Color.argb(a + i4, r + rD, g + gD, b + bD));
            } else {
                color3 = color;
                subtitleColor = actionBarColor;
                r3 = r;
                g3 = g;
            }
            i5 = aD + 1;
            color = color3;
            actionBarColor = subtitleColor;
            r = r3;
            g = g3;
            i3 = 2;
        }
        subtitleColor = actionBarColor;
        r3 = r;
        g3 = g;
        r0.extraHeight = (int) (((float) r0.initialAnimationExtraHeight) * f);
        if (r0.user_id == 0) {
        }
        color = AvatarDrawable.getProfileColorForId(r0.user_id == 0 ? r0.user_id : r0.chat_id);
        if (r0.user_id == 0) {
        }
        i3 = AvatarDrawable.getColorForId(r0.user_id == 0 ? r0.user_id : r0.chat_id);
        if (color != i3) {
            r0.avatarDrawable.setColor(Color.rgb(Color.red(i3) + ((int) (((float) (Color.red(color) - Color.red(i3))) * f)), Color.green(i3) + ((int) (((float) (Color.green(color) - Color.green(i3))) * f)), Color.blue(i3) + ((int) (((float) (Color.blue(color) - Color.blue(i3))) * f))));
            r0.avatarImage.invalidate();
        }
        needLayout();
    }

    protected AnimatorSet onCustomTransitionAnimation(boolean isOpen, Runnable callback) {
        if (this.playProfileAnimation && r0.allowProfileAnimation) {
            final AnimatorSet animatorSet = new AnimatorSet();
            animatorSet.setDuration(180);
            int i = 2;
            r0.listView.setLayerType(2, null);
            ActionBarMenu menu = r0.actionBar.createMenu();
            if (menu.getItem(10) == null && r0.animatingItem == null) {
                r0.animatingItem = menu.addItem(10, (int) R.drawable.ic_ab_other);
            }
            int a;
            if (isOpen) {
                FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) r0.onlineTextView[1].getLayoutParams();
                layoutParams.rightMargin = (int) ((-21.0f * AndroidUtilities.density) + ((float) AndroidUtilities.dp(8.0f)));
                r0.onlineTextView[1].setLayoutParams(layoutParams);
                int width = (int) Math.ceil((double) (((float) (AndroidUtilities.displaySize.x - AndroidUtilities.dp(126.0f))) + (21.0f * AndroidUtilities.density)));
                layoutParams = (FrameLayout.LayoutParams) r0.nameTextView[1].getLayoutParams();
                if (((float) width) < (r0.nameTextView[1].getPaint().measureText(r0.nameTextView[1].getText().toString()) * 1.12f) + ((float) r0.nameTextView[1].getSideDrawablesSize())) {
                    layoutParams.width = (int) Math.ceil((double) (((float) width) / 1.12f));
                } else {
                    layoutParams.width = -2;
                }
                r0.nameTextView[1].setLayoutParams(layoutParams);
                r0.initialAnimationExtraHeight = AndroidUtilities.dp(88.0f);
                r0.fragmentView.setBackgroundColor(0);
                setAnimationProgress(0.0f);
                ArrayList<Animator> animators = new ArrayList();
                animators.add(ObjectAnimator.ofFloat(r0, "animationProgress", new float[]{0.0f, 1.0f}));
                if (r0.writeButton != null) {
                    r0.writeButton.setScaleX(0.2f);
                    r0.writeButton.setScaleY(0.2f);
                    r0.writeButton.setAlpha(0.0f);
                    animators.add(ObjectAnimator.ofFloat(r0.writeButton, "scaleX", new float[]{1.0f}));
                    animators.add(ObjectAnimator.ofFloat(r0.writeButton, "scaleY", new float[]{1.0f}));
                    animators.add(ObjectAnimator.ofFloat(r0.writeButton, "alpha", new float[]{1.0f}));
                }
                a = 0;
                while (a < i) {
                    r0.onlineTextView[a].setAlpha(a == 0 ? 1.0f : 0.0f);
                    r0.nameTextView[a].setAlpha(a == 0 ? 1.0f : 0.0f);
                    Object obj = r0.onlineTextView[a];
                    String str = "alpha";
                    float[] fArr = new float[1];
                    fArr[0] = a == 0 ? 0.0f : 1.0f;
                    animators.add(ObjectAnimator.ofFloat(obj, str, fArr));
                    Object obj2 = r0.nameTextView[a];
                    String str2 = "alpha";
                    float[] fArr2 = new float[1];
                    fArr2[0] = a == 0 ? 0.0f : 1.0f;
                    animators.add(ObjectAnimator.ofFloat(obj2, str2, fArr2));
                    a++;
                    i = 2;
                }
                if (r0.animatingItem != null) {
                    r0.animatingItem.setAlpha(1.0f);
                    animators.add(ObjectAnimator.ofFloat(r0.animatingItem, "alpha", new float[]{0.0f}));
                }
                if (r0.callItem != null) {
                    r0.callItem.setAlpha(0.0f);
                    animators.add(ObjectAnimator.ofFloat(r0.callItem, "alpha", new float[]{1.0f}));
                }
                if (r0.editItem != null) {
                    r0.editItem.setAlpha(0.0f);
                    animators.add(ObjectAnimator.ofFloat(r0.editItem, "alpha", new float[]{1.0f}));
                }
                animatorSet.playTogether(animators);
            } else {
                r0.initialAnimationExtraHeight = r0.extraHeight;
                ArrayList<Animator> animators2 = new ArrayList();
                animators2.add(ObjectAnimator.ofFloat(r0, "animationProgress", new float[]{1.0f, 0.0f}));
                if (r0.writeButton != null) {
                    animators2.add(ObjectAnimator.ofFloat(r0.writeButton, "scaleX", new float[]{0.2f}));
                    animators2.add(ObjectAnimator.ofFloat(r0.writeButton, "scaleY", new float[]{0.2f}));
                    animators2.add(ObjectAnimator.ofFloat(r0.writeButton, "alpha", new float[]{0.0f}));
                }
                a = 0;
                while (a < 2) {
                    Object obj3 = r0.onlineTextView[a];
                    String str3 = "alpha";
                    float[] fArr3 = new float[1];
                    fArr3[0] = a == 0 ? 1.0f : 0.0f;
                    animators2.add(ObjectAnimator.ofFloat(obj3, str3, fArr3));
                    obj3 = r0.nameTextView[a];
                    str3 = "alpha";
                    fArr3 = new float[1];
                    fArr3[0] = a == 0 ? 1.0f : 0.0f;
                    animators2.add(ObjectAnimator.ofFloat(obj3, str3, fArr3));
                    a++;
                }
                if (r0.animatingItem != null) {
                    r0.animatingItem.setAlpha(0.0f);
                    animators2.add(ObjectAnimator.ofFloat(r0.animatingItem, "alpha", new float[]{1.0f}));
                }
                if (r0.callItem != null) {
                    r0.callItem.setAlpha(1.0f);
                    animators2.add(ObjectAnimator.ofFloat(r0.callItem, "alpha", new float[]{0.0f}));
                }
                if (r0.editItem != null) {
                    r0.editItem.setAlpha(1.0f);
                    animators2.add(ObjectAnimator.ofFloat(r0.editItem, "alpha", new float[]{0.0f}));
                }
                animatorSet.playTogether(animators2);
            }
            final Runnable runnable = callback;
            animatorSet.addListener(new AnimatorListenerAdapter() {
                public void onAnimationEnd(Animator animation) {
                    ProfileActivity.this.listView.setLayerType(0, null);
                    if (ProfileActivity.this.animatingItem != null) {
                        ProfileActivity.this.actionBar.createMenu().clearItems();
                        ProfileActivity.this.animatingItem = null;
                    }
                    runnable.run();
                }
            });
            animatorSet.setInterpolator(new DecelerateInterpolator());
            AndroidUtilities.runOnUIThread(new Runnable() {
                public void run() {
                    animatorSet.start();
                }
            }, 50);
            return animatorSet;
        }
        runnable = callback;
        return null;
    }

    public void setChatInfo(ChatFull chatInfo) {
        this.info = chatInfo;
        if (!(this.info == null || this.info.migrated_from_chat_id == 0)) {
            this.mergeDialogId = (long) (-this.info.migrated_from_chat_id);
        }
        fetchUsersFromChannelInfo();
    }

    private void fetchUsersFromChannelInfo() {
        if (this.currentChat != null) {
            if (this.currentChat.megagroup) {
                if ((this.info instanceof TL_channelFull) && this.info.participants != null) {
                    for (int a = 0; a < this.info.participants.participants.size(); a++) {
                        ChatParticipant chatParticipant = (ChatParticipant) this.info.participants.participants.get(a);
                        this.participantsMap.put(chatParticipant.user_id, chatParticipant);
                    }
                }
            }
        }
    }

    private void kickUser(int uid) {
        if (uid != 0) {
            MessagesController.getInstance(this.currentAccount).deleteUserFromChat(this.chat_id, MessagesController.getInstance(this.currentAccount).getUser(Integer.valueOf(uid)), this.info);
            return;
        }
        NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.closeChats);
        if (AndroidUtilities.isTablet()) {
            NotificationCenter.getInstance(this.currentAccount).postNotificationName(NotificationCenter.closeChats, Long.valueOf(-((long) this.chat_id)));
        } else {
            NotificationCenter.getInstance(this.currentAccount).postNotificationName(NotificationCenter.closeChats, new Object[0]);
        }
        MessagesController.getInstance(this.currentAccount).deleteUserFromChat(this.chat_id, MessagesController.getInstance(this.currentAccount).getUser(Integer.valueOf(UserConfig.getInstance(this.currentAccount).getClientUserId())), this.info);
        this.playProfileAnimation = false;
        finishFragment();
    }

    public boolean isChat() {
        return this.chat_id != 0;
    }

    private void updateRowsIds() {
        this.emptyRow = -1;
        this.phoneRow = -1;
        this.userInfoRow = -1;
        this.userInfoDetailedRow = -1;
        this.userSectionRow = -1;
        this.sectionRow = -1;
        this.sharedMediaRow = -1;
        this.settingsNotificationsRow = -1;
        this.usernameRow = -1;
        this.settingsTimerRow = -1;
        this.settingsKeyRow = -1;
        this.startSecretChatRow = -1;
        this.membersEndRow = -1;
        this.emptyRowChat2 = -1;
        this.addMemberRow = -1;
        this.channelInfoRow = -1;
        this.channelNameRow = -1;
        this.convertRow = -1;
        this.convertHelpRow = -1;
        this.emptyRowChat = -1;
        this.membersSectionRow = -1;
        this.membersRow = -1;
        this.leaveChannelRow = -1;
        this.loadMoreMembersRow = -1;
        this.groupsInCommonRow = -1;
        boolean hasUsername = false;
        this.rowCount = 0;
        int i;
        if (this.user_id != 0) {
            int i2;
            User user = MessagesController.getInstance(this.currentAccount).getUser(Integer.valueOf(this.user_id));
            int i3 = this.rowCount;
            this.rowCount = i3 + 1;
            this.emptyRow = i3;
            if (!(this.isBot || TextUtils.isEmpty(user.phone))) {
                i3 = this.rowCount;
                this.rowCount = i3 + 1;
                this.phoneRow = i3;
            }
            TL_userFull userFull = MessagesController.getInstance(this.currentAccount).getUserFull(this.user_id);
            if (!(user == null || TextUtils.isEmpty(user.username))) {
                hasUsername = true;
            }
            if (!(userFull == null || TextUtils.isEmpty(userFull.about))) {
                if (this.phoneRow != -1) {
                    i2 = this.rowCount;
                    this.rowCount = i2 + 1;
                    this.userSectionRow = i2;
                }
                if (!hasUsername) {
                    if (!this.isBot) {
                        i2 = this.rowCount;
                        this.rowCount = i2 + 1;
                        this.userInfoDetailedRow = i2;
                    }
                }
                i2 = this.rowCount;
                this.rowCount = i2 + 1;
                this.userInfoRow = i2;
            }
            if (hasUsername) {
                i2 = this.rowCount;
                this.rowCount = i2 + 1;
                this.usernameRow = i2;
            }
            if (!(this.phoneRow == -1 && this.userInfoRow == -1 && this.userInfoDetailedRow == -1 && this.usernameRow == -1)) {
                i = this.rowCount;
                this.rowCount = i + 1;
                this.sectionRow = i;
            }
            if (this.user_id != UserConfig.getInstance(this.currentAccount).getClientUserId()) {
                i = this.rowCount;
                this.rowCount = i + 1;
                this.settingsNotificationsRow = i;
            }
            i = this.rowCount;
            this.rowCount = i + 1;
            this.sharedMediaRow = i;
            if (this.currentEncryptedChat instanceof TL_encryptedChat) {
                i = this.rowCount;
                this.rowCount = i + 1;
                this.settingsTimerRow = i;
                i = this.rowCount;
                this.rowCount = i + 1;
                this.settingsKeyRow = i;
            }
            if (!(userFull == null || userFull.common_chats_count == 0)) {
                i = this.rowCount;
                this.rowCount = i + 1;
                this.groupsInCommonRow = i;
            }
            if (!(user == null || this.isBot || this.currentEncryptedChat != null || user.id == UserConfig.getInstance(this.currentAccount).getClientUserId())) {
                i = this.rowCount;
                this.rowCount = i + 1;
                this.startSecretChatRow = i;
            }
        } else if (this.chat_id == 0) {
        } else {
            if (this.chat_id > 0) {
                int i4 = this.rowCount;
                this.rowCount = i4 + 1;
                this.emptyRow = i4;
                if (ChatObject.isChannel(this.currentChat) && (!(this.info == null || this.info.about == null || this.info.about.length() <= 0) || (this.currentChat.username != null && this.currentChat.username.length() > 0))) {
                    if (!(this.info == null || this.info.about == null || this.info.about.length() <= 0)) {
                        i4 = this.rowCount;
                        this.rowCount = i4 + 1;
                        this.channelInfoRow = i4;
                    }
                    if (this.currentChat.username != null && this.currentChat.username.length() > 0) {
                        i4 = this.rowCount;
                        this.rowCount = i4 + 1;
                        this.channelNameRow = i4;
                    }
                    i4 = this.rowCount;
                    this.rowCount = i4 + 1;
                    this.sectionRow = i4;
                }
                i4 = this.rowCount;
                this.rowCount = i4 + 1;
                this.settingsNotificationsRow = i4;
                i4 = this.rowCount;
                this.rowCount = i4 + 1;
                this.sharedMediaRow = i4;
                if (ChatObject.isChannel(this.currentChat)) {
                    if (!(this.currentChat.megagroup || this.info == null || (!this.currentChat.creator && !this.info.can_view_participants))) {
                        i = this.rowCount;
                        this.rowCount = i + 1;
                        this.membersRow = i;
                    }
                    if (!(this.currentChat.creator || this.currentChat.left || this.currentChat.kicked || this.currentChat.megagroup)) {
                        i = this.rowCount;
                        this.rowCount = i + 1;
                        this.leaveChannelRow = i;
                    }
                    if (this.currentChat.megagroup && (((this.currentChat.admin_rights != null && this.currentChat.admin_rights.invite_users) || this.currentChat.creator || this.currentChat.democracy) && (this.info == null || this.info.participants_count < MessagesController.getInstance(this.currentAccount).maxMegagroupCount))) {
                        i = this.rowCount;
                        this.rowCount = i + 1;
                        this.addMemberRow = i;
                    }
                    if (this.info != null && this.currentChat.megagroup && this.info.participants != null && !this.info.participants.participants.isEmpty()) {
                        i = this.rowCount;
                        this.rowCount = i + 1;
                        this.emptyRowChat = i;
                        i = this.rowCount;
                        this.rowCount = i + 1;
                        this.membersSectionRow = i;
                        i = this.rowCount;
                        this.rowCount = i + 1;
                        this.emptyRowChat2 = i;
                        this.rowCount += this.info.participants.participants.size();
                        this.membersEndRow = this.rowCount;
                        if (!this.usersEndReached) {
                            i = this.rowCount;
                            this.rowCount = i + 1;
                            this.loadMoreMembersRow = i;
                            return;
                        }
                        return;
                    }
                    return;
                }
                if (this.info != null) {
                    if (!(this.info.participants instanceof TL_chatParticipantsForbidden) && this.info.participants.participants.size() < MessagesController.getInstance(this.currentAccount).maxGroupCount && (this.currentChat.admin || this.currentChat.creator || !this.currentChat.admins_enabled)) {
                        i4 = this.rowCount;
                        this.rowCount = i4 + 1;
                        this.addMemberRow = i4;
                    }
                    if (this.currentChat.creator && this.info.participants.participants.size() >= MessagesController.getInstance(this.currentAccount).minGroupConvertSize) {
                        i4 = this.rowCount;
                        this.rowCount = i4 + 1;
                        this.convertRow = i4;
                    }
                }
                i4 = this.rowCount;
                this.rowCount = i4 + 1;
                this.emptyRowChat = i4;
                if (this.convertRow != -1) {
                    i = this.rowCount;
                    this.rowCount = i + 1;
                    this.convertHelpRow = i;
                } else {
                    i = this.rowCount;
                    this.rowCount = i + 1;
                    this.membersSectionRow = i;
                }
                if (this.info != null && !(this.info.participants instanceof TL_chatParticipantsForbidden)) {
                    i = this.rowCount;
                    this.rowCount = i + 1;
                    this.emptyRowChat2 = i;
                    this.rowCount += this.info.participants.participants.size();
                    this.membersEndRow = this.rowCount;
                }
            } else if (!ChatObject.isChannel(this.currentChat) && this.info != null && !(this.info.participants instanceof TL_chatParticipantsForbidden)) {
                i = this.rowCount;
                this.rowCount = i + 1;
                this.addMemberRow = i;
                i = this.rowCount;
                this.rowCount = i + 1;
                this.emptyRowChat2 = i;
                this.rowCount += this.info.participants.participants.size();
                this.membersEndRow = this.rowCount;
            }
        }
    }

    private void createActionBarMenu() {
        ActionBarMenu menu = this.actionBar.createMenu();
        menu.clearItems();
        this.animatingItem = null;
        ActionBarMenuItem item = null;
        if (this.user_id != 0) {
            if (UserConfig.getInstance(this.currentAccount).getClientUserId() != this.user_id) {
                TL_userFull userFull = MessagesController.getInstance(this.currentAccount).getUserFull(this.user_id);
                if (userFull != null && userFull.phone_calls_available) {
                    this.callItem = menu.addItem(15, (int) R.drawable.ic_call_white_24dp);
                }
                if (ContactsController.getInstance(this.currentAccount).contactsDict.get(Integer.valueOf(this.user_id)) == null) {
                    User user = MessagesController.getInstance(this.currentAccount).getUser(Integer.valueOf(this.user_id));
                    if (user != null) {
                        item = menu.addItem(10, (int) R.drawable.ic_ab_other);
                        if (this.isBot) {
                            if (!user.bot_nochats) {
                                item.addSubItem(9, LocaleController.getString("BotInvite", R.string.BotInvite));
                            }
                            item.addSubItem(10, LocaleController.getString("BotShare", R.string.BotShare));
                        }
                        if (user.phone != null && user.phone.length() != 0) {
                            item.addSubItem(1, LocaleController.getString("AddContact", R.string.AddContact));
                            item.addSubItem(3, LocaleController.getString("ShareContact", R.string.ShareContact));
                            item.addSubItem(2, !this.userBlocked ? LocaleController.getString("BlockContact", R.string.BlockContact) : LocaleController.getString("Unblock", R.string.Unblock));
                        } else if (this.isBot) {
                            String str;
                            int i;
                            if (this.userBlocked) {
                                str = "BotRestart";
                                i = R.string.BotRestart;
                            } else {
                                str = "BotStop";
                                i = R.string.BotStop;
                            }
                            item.addSubItem(2, LocaleController.getString(str, i));
                        } else {
                            item.addSubItem(2, !this.userBlocked ? LocaleController.getString("BlockContact", R.string.BlockContact) : LocaleController.getString("Unblock", R.string.Unblock));
                        }
                    } else {
                        return;
                    }
                }
                item = menu.addItem(10, (int) R.drawable.ic_ab_other);
                item.addSubItem(3, LocaleController.getString("ShareContact", R.string.ShareContact));
                item.addSubItem(2, !this.userBlocked ? LocaleController.getString("BlockContact", R.string.BlockContact) : LocaleController.getString("Unblock", R.string.Unblock));
                item.addSubItem(4, LocaleController.getString("EditContact", R.string.EditContact));
                item.addSubItem(5, LocaleController.getString("DeleteContact", R.string.DeleteContact));
            } else {
                item = menu.addItem(10, (int) R.drawable.ic_ab_other);
                item.addSubItem(3, LocaleController.getString("ShareContact", R.string.ShareContact));
            }
        } else if (this.chat_id != 0) {
            if (this.chat_id > 0) {
                Chat chat = MessagesController.getInstance(this.currentAccount).getChat(Integer.valueOf(this.chat_id));
                if (this.writeButton != null) {
                    boolean isChannel = ChatObject.isChannel(this.currentChat);
                    if ((!isChannel || ChatObject.canChangeChatInfo(this.currentChat)) && (isChannel || this.currentChat.admin || this.currentChat.creator || !this.currentChat.admins_enabled)) {
                        this.writeButton.setImageResource(R.drawable.floating_camera);
                        this.writeButton.setPadding(0, 0, 0, 0);
                    } else {
                        this.writeButton.setImageResource(R.drawable.floating_message);
                        this.writeButton.setPadding(0, AndroidUtilities.dp(3.0f), 0, 0);
                    }
                }
                if (ChatObject.isChannel(chat)) {
                    if (ChatObject.hasAdminRights(chat)) {
                        this.editItem = menu.addItem(12, (int) R.drawable.menu_settings);
                        if (null == null) {
                            item = menu.addItem(10, (int) R.drawable.ic_ab_other);
                        }
                        if (chat.megagroup) {
                            item.addSubItem(12, LocaleController.getString("ManageGroupMenu", R.string.ManageGroupMenu));
                        } else {
                            item.addSubItem(12, LocaleController.getString("ManageChannelMenu", R.string.ManageChannelMenu));
                        }
                    }
                    if (chat.megagroup) {
                        if (item == null) {
                            item = menu.addItem(10, (int) R.drawable.ic_ab_other);
                        }
                        item.addSubItem(16, LocaleController.getString("SearchMembers", R.string.SearchMembers));
                        if (!(chat.creator || chat.left || chat.kicked)) {
                            item.addSubItem(7, LocaleController.getString("LeaveMegaMenu", R.string.LeaveMegaMenu));
                        }
                    }
                } else {
                    if (!chat.admins_enabled || chat.creator || chat.admin) {
                        this.editItem = menu.addItem(8, (int) R.drawable.group_edit_profile);
                    }
                    item = menu.addItem(10, (int) R.drawable.ic_ab_other);
                    if (chat.creator && this.chat_id > 0) {
                        item.addSubItem(11, LocaleController.getString("SetAdmins", R.string.SetAdmins));
                    }
                    if (!chat.admins_enabled || chat.creator || chat.admin) {
                        item.addSubItem(8, LocaleController.getString("ChannelEdit", R.string.ChannelEdit));
                    }
                    item.addSubItem(16, LocaleController.getString("SearchMembers", R.string.SearchMembers));
                    if (chat.creator && (this.info == null || this.info.participants.participants.size() > 0)) {
                        item.addSubItem(13, LocaleController.getString("ConvertGroupMenu", R.string.ConvertGroupMenu));
                    }
                    item.addSubItem(7, LocaleController.getString("DeleteAndExit", R.string.DeleteAndExit));
                }
            } else {
                item = menu.addItem(10, (int) R.drawable.ic_ab_other);
                item.addSubItem(8, LocaleController.getString("EditName", R.string.EditName));
            }
        }
        if (item == null) {
            item = menu.addItem(10, (int) R.drawable.ic_ab_other);
        }
        item.addSubItem(14, LocaleController.getString("AddShortcut", R.string.AddShortcut));
    }

    protected void onDialogDismiss(Dialog dialog) {
        if (this.listView != null) {
            this.listView.invalidateViews();
        }
    }

    public void didSelectDialogs(DialogsActivity fragment, ArrayList<Long> dids, CharSequence message, boolean param) {
        ProfileActivity profileActivity = this;
        long did = ((Long) dids.get(0)).longValue();
        Bundle args = new Bundle();
        args.putBoolean("scrollToTopOnResume", true);
        int lower_part = (int) did;
        if (lower_part == 0) {
            args.putInt("enc_id", (int) (did >> 32));
        } else if (lower_part > 0) {
            args.putInt("user_id", lower_part);
        } else if (lower_part < 0) {
            args.putInt("chat_id", -lower_part);
        }
        if (MessagesController.getInstance(profileActivity.currentAccount).checkCanOpenChat(args, fragment)) {
            NotificationCenter.getInstance(profileActivity.currentAccount).removeObserver(profileActivity, NotificationCenter.closeChats);
            NotificationCenter.getInstance(profileActivity.currentAccount).postNotificationName(NotificationCenter.closeChats, new Object[0]);
            presentFragment(new ChatActivity(args), true);
            removeSelfFromStack();
            SendMessagesHelper.getInstance(profileActivity.currentAccount).sendMessage(MessagesController.getInstance(profileActivity.currentAccount).getUser(Integer.valueOf(profileActivity.user_id)), did, null, null, null);
        }
    }

    public void onRequestPermissionsResultFragment(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == 101) {
            User user = MessagesController.getInstance(this.currentAccount).getUser(Integer.valueOf(this.user_id));
            if (user != null) {
                if (grantResults.length <= 0 || grantResults[0] != 0) {
                    VoIPHelper.permissionDenied(getParentActivity(), null);
                } else {
                    VoIPHelper.startCall(user, getParentActivity(), MessagesController.getInstance(this.currentAccount).getUserFull(user.id));
                }
            }
        }
    }

    public ThemeDescription[] getThemeDescriptions() {
        ThemeDescriptionDelegate сellDelegate = new ThemeDescriptionDelegate() {
            public void didSetColor() {
                if (ProfileActivity.this.listView != null) {
                    int count = ProfileActivity.this.listView.getChildCount();
                    for (int a = 0; a < count; a++) {
                        View child = ProfileActivity.this.listView.getChildAt(a);
                        if (child instanceof UserCell) {
                            ((UserCell) child).update(0);
                        }
                    }
                }
            }
        };
        r10 = new ThemeDescription[92];
        r10[4] = new ThemeDescription(this.listView, ThemeDescription.FLAG_LISTGLOWCOLOR, null, null, null, null, Theme.key_avatar_backgroundActionBarBlue);
        r10[5] = new ThemeDescription(this.topView, ThemeDescription.FLAG_BACKGROUND, null, null, null, null, Theme.key_avatar_backgroundActionBarBlue);
        r10[6] = new ThemeDescription(this.actionBar, ThemeDescription.FLAG_AB_SELECTORCOLOR, null, null, null, null, Theme.key_avatar_actionBarSelectorBlue);
        r10[7] = new ThemeDescription(this.nameTextView[1], ThemeDescription.FLAG_TEXTCOLOR, null, null, null, null, Theme.key_profile_title);
        r10[8] = new ThemeDescription(this.onlineTextView[1], ThemeDescription.FLAG_TEXTCOLOR, null, null, null, null, Theme.key_avatar_subtitleInProfileBlue);
        r10[9] = new ThemeDescription(this.actionBar, ThemeDescription.FLAG_BACKGROUND, null, null, null, null, Theme.key_avatar_backgroundActionBarRed);
        r10[10] = new ThemeDescription(this.listView, ThemeDescription.FLAG_LISTGLOWCOLOR, null, null, null, null, Theme.key_avatar_backgroundActionBarRed);
        r10[11] = new ThemeDescription(this.topView, ThemeDescription.FLAG_BACKGROUND, null, null, null, null, Theme.key_avatar_backgroundActionBarRed);
        r10[12] = new ThemeDescription(this.actionBar, ThemeDescription.FLAG_AB_SELECTORCOLOR, null, null, null, null, Theme.key_avatar_actionBarSelectorRed);
        r10[13] = new ThemeDescription(this.onlineTextView[1], ThemeDescription.FLAG_TEXTCOLOR, null, null, null, null, Theme.key_avatar_subtitleInProfileRed);
        r10[14] = new ThemeDescription(this.actionBar, ThemeDescription.FLAG_AB_ITEMSCOLOR, null, null, null, null, Theme.key_avatar_actionBarIconRed);
        r10[15] = new ThemeDescription(this.actionBar, ThemeDescription.FLAG_BACKGROUND, null, null, null, null, Theme.key_avatar_backgroundActionBarOrange);
        r10[16] = new ThemeDescription(this.listView, ThemeDescription.FLAG_LISTGLOWCOLOR, null, null, null, null, Theme.key_avatar_backgroundActionBarOrange);
        r10[17] = new ThemeDescription(this.topView, ThemeDescription.FLAG_BACKGROUND, null, null, null, null, Theme.key_avatar_backgroundActionBarOrange);
        r10[18] = new ThemeDescription(this.actionBar, ThemeDescription.FLAG_AB_SELECTORCOLOR, null, null, null, null, Theme.key_avatar_actionBarSelectorOrange);
        r10[19] = new ThemeDescription(this.onlineTextView[1], ThemeDescription.FLAG_TEXTCOLOR, null, null, null, null, Theme.key_avatar_subtitleInProfileOrange);
        r10[20] = new ThemeDescription(this.actionBar, ThemeDescription.FLAG_AB_ITEMSCOLOR, null, null, null, null, Theme.key_avatar_actionBarIconOrange);
        r10[21] = new ThemeDescription(this.actionBar, ThemeDescription.FLAG_BACKGROUND, null, null, null, null, Theme.key_avatar_backgroundActionBarViolet);
        r10[22] = new ThemeDescription(this.listView, ThemeDescription.FLAG_LISTGLOWCOLOR, null, null, null, null, Theme.key_avatar_backgroundActionBarViolet);
        r10[23] = new ThemeDescription(this.topView, ThemeDescription.FLAG_BACKGROUND, null, null, null, null, Theme.key_avatar_backgroundActionBarViolet);
        r10[24] = new ThemeDescription(this.actionBar, ThemeDescription.FLAG_AB_SELECTORCOLOR, null, null, null, null, Theme.key_avatar_actionBarSelectorViolet);
        r10[25] = new ThemeDescription(this.onlineTextView[1], ThemeDescription.FLAG_TEXTCOLOR, null, null, null, null, Theme.key_avatar_subtitleInProfileViolet);
        r10[26] = new ThemeDescription(this.actionBar, ThemeDescription.FLAG_AB_ITEMSCOLOR, null, null, null, null, Theme.key_avatar_actionBarIconViolet);
        r10[27] = new ThemeDescription(this.actionBar, ThemeDescription.FLAG_BACKGROUND, null, null, null, null, Theme.key_avatar_backgroundActionBarGreen);
        r10[28] = new ThemeDescription(this.listView, ThemeDescription.FLAG_LISTGLOWCOLOR, null, null, null, null, Theme.key_avatar_backgroundActionBarGreen);
        r10[29] = new ThemeDescription(this.topView, ThemeDescription.FLAG_BACKGROUND, null, null, null, null, Theme.key_avatar_backgroundActionBarGreen);
        r10[30] = new ThemeDescription(this.actionBar, ThemeDescription.FLAG_AB_SELECTORCOLOR, null, null, null, null, Theme.key_avatar_actionBarSelectorGreen);
        r10[31] = new ThemeDescription(this.onlineTextView[1], ThemeDescription.FLAG_TEXTCOLOR, null, null, null, null, Theme.key_avatar_subtitleInProfileGreen);
        r10[32] = new ThemeDescription(this.actionBar, ThemeDescription.FLAG_AB_ITEMSCOLOR, null, null, null, null, Theme.key_avatar_actionBarIconGreen);
        r10[33] = new ThemeDescription(this.actionBar, ThemeDescription.FLAG_BACKGROUND, null, null, null, null, Theme.key_avatar_backgroundActionBarCyan);
        r10[34] = new ThemeDescription(this.listView, ThemeDescription.FLAG_LISTGLOWCOLOR, null, null, null, null, Theme.key_avatar_backgroundActionBarCyan);
        r10[35] = new ThemeDescription(this.topView, ThemeDescription.FLAG_BACKGROUND, null, null, null, null, Theme.key_avatar_backgroundActionBarCyan);
        r10[36] = new ThemeDescription(this.actionBar, ThemeDescription.FLAG_AB_SELECTORCOLOR, null, null, null, null, Theme.key_avatar_actionBarSelectorCyan);
        r10[37] = new ThemeDescription(this.onlineTextView[1], ThemeDescription.FLAG_TEXTCOLOR, null, null, null, null, Theme.key_avatar_subtitleInProfileCyan);
        r10[38] = new ThemeDescription(this.actionBar, ThemeDescription.FLAG_AB_ITEMSCOLOR, null, null, null, null, Theme.key_avatar_actionBarIconCyan);
        r10[39] = new ThemeDescription(this.actionBar, ThemeDescription.FLAG_BACKGROUND, null, null, null, null, Theme.key_avatar_backgroundActionBarPink);
        r10[40] = new ThemeDescription(this.listView, ThemeDescription.FLAG_LISTGLOWCOLOR, null, null, null, null, Theme.key_avatar_backgroundActionBarPink);
        r10[41] = new ThemeDescription(this.topView, ThemeDescription.FLAG_BACKGROUND, null, null, null, null, Theme.key_avatar_backgroundActionBarPink);
        r10[42] = new ThemeDescription(this.actionBar, ThemeDescription.FLAG_AB_SELECTORCOLOR, null, null, null, null, Theme.key_avatar_actionBarSelectorPink);
        r10[43] = new ThemeDescription(this.onlineTextView[1], ThemeDescription.FLAG_TEXTCOLOR, null, null, null, null, Theme.key_avatar_subtitleInProfilePink);
        r10[44] = new ThemeDescription(this.actionBar, ThemeDescription.FLAG_AB_ITEMSCOLOR, null, null, null, null, Theme.key_avatar_actionBarIconPink);
        r10[45] = new ThemeDescription(this.listView, ThemeDescription.FLAG_SELECTOR, null, null, null, null, Theme.key_listSelector);
        r10[46] = new ThemeDescription(this.listView, 0, new Class[]{View.class}, Theme.dividerPaint, null, null, Theme.key_divider);
        View view = this.listView;
        View view2 = view;
        r10[47] = new ThemeDescription(view2, ThemeDescription.FLAG_BACKGROUNDFILTER, new Class[]{ShadowSectionCell.class}, null, null, null, Theme.key_windowBackgroundGrayShadow);
        r10[48] = new ThemeDescription(this.avatarImage, 0, null, null, new Drawable[]{Theme.avatar_photoDrawable, Theme.avatar_broadcastDrawable, Theme.avatar_savedDrawable}, null, Theme.key_avatar_text);
        r10[49] = new ThemeDescription(this.avatarImage, 0, null, null, new Drawable[]{this.avatarDrawable}, null, Theme.key_avatar_backgroundInProfileRed);
        r10[50] = new ThemeDescription(this.avatarImage, 0, null, null, new Drawable[]{this.avatarDrawable}, null, Theme.key_avatar_backgroundInProfileOrange);
        r10[51] = new ThemeDescription(this.avatarImage, 0, null, null, new Drawable[]{this.avatarDrawable}, null, Theme.key_avatar_backgroundInProfileViolet);
        r10[52] = new ThemeDescription(this.avatarImage, 0, null, null, new Drawable[]{this.avatarDrawable}, null, Theme.key_avatar_backgroundInProfileGreen);
        r10[53] = new ThemeDescription(this.avatarImage, 0, null, null, new Drawable[]{this.avatarDrawable}, null, Theme.key_avatar_backgroundInProfileCyan);
        r10[54] = new ThemeDescription(this.avatarImage, 0, null, null, new Drawable[]{this.avatarDrawable}, null, Theme.key_avatar_backgroundInProfileBlue);
        r10[55] = new ThemeDescription(this.avatarImage, 0, null, null, new Drawable[]{this.avatarDrawable}, null, Theme.key_avatar_backgroundInProfilePink);
        r10[56] = new ThemeDescription(this.writeButton, ThemeDescription.FLAG_IMAGECOLOR, null, null, null, null, Theme.key_profile_actionIcon);
        r10[57] = new ThemeDescription(this.writeButton, ThemeDescription.FLAG_BACKGROUNDFILTER, null, null, null, null, Theme.key_profile_actionBackground);
        r10[58] = new ThemeDescription(this.writeButton, ThemeDescription.FLAG_BACKGROUNDFILTER | ThemeDescription.FLAG_DRAWABLESELECTEDSTATE, null, null, null, null, Theme.key_profile_actionPressedBackground);
        view = this.listView;
        view2 = view;
        r10[59] = new ThemeDescription(view2, ThemeDescription.FLAG_CHECKTAG, new Class[]{TextCell.class}, new String[]{"textView"}, null, null, null, Theme.key_windowBackgroundWhiteBlackText);
        view = this.listView;
        view2 = view;
        r10[60] = new ThemeDescription(view2, ThemeDescription.FLAG_CHECKTAG, new Class[]{TextCell.class}, new String[]{"textView"}, null, null, null, Theme.key_windowBackgroundWhiteGreenText2);
        view = this.listView;
        view2 = view;
        r10[61] = new ThemeDescription(view2, ThemeDescription.FLAG_CHECKTAG, new Class[]{TextCell.class}, new String[]{"textView"}, null, null, null, Theme.key_windowBackgroundWhiteRedText5);
        r10[62] = new ThemeDescription(this.listView, 0, new Class[]{TextCell.class}, new String[]{"valueTextView"}, null, null, null, Theme.key_windowBackgroundWhiteValueText);
        r10[63] = new ThemeDescription(this.listView, 0, new Class[]{TextCell.class}, new String[]{"imageView"}, null, null, null, Theme.key_windowBackgroundWhiteGrayIcon);
        r10[64] = new ThemeDescription(this.listView, 0, new Class[]{TextDetailCell.class}, new String[]{"textView"}, null, null, null, Theme.key_windowBackgroundWhiteBlackText);
        r10[65] = new ThemeDescription(this.listView, 0, new Class[]{TextDetailCell.class}, new String[]{"valueImageView"}, null, null, null, Theme.key_windowBackgroundWhiteGrayIcon);
        r10[66] = new ThemeDescription(this.listView, 0, new Class[]{TextDetailCell.class}, new String[]{"imageView"}, null, null, null, Theme.key_windowBackgroundWhiteGrayIcon);
        view = this.listView;
        view2 = view;
        r10[67] = new ThemeDescription(view2, ThemeDescription.FLAG_CHECKTAG, new Class[]{UserCell.class}, new String[]{"adminImage"}, null, null, null, Theme.key_profile_creatorIcon);
        view = this.listView;
        view2 = view;
        r10[68] = new ThemeDescription(view2, ThemeDescription.FLAG_CHECKTAG, new Class[]{UserCell.class}, new String[]{"adminImage"}, null, null, null, Theme.key_profile_adminIcon);
        r10[69] = new ThemeDescription(this.listView, 0, new Class[]{UserCell.class}, new String[]{"nameTextView"}, null, null, null, Theme.key_windowBackgroundWhiteBlackText);
        r10[70] = new ThemeDescription(this.listView, 0, new Class[]{UserCell.class}, new String[]{"statusColor"}, null, null, сellDelegate, Theme.key_windowBackgroundWhiteGrayText);
        r10[71] = new ThemeDescription(this.listView, 0, new Class[]{UserCell.class}, new String[]{"statusOnlineColor"}, null, null, сellDelegate, Theme.key_windowBackgroundWhiteBlueText);
        r10[72] = new ThemeDescription(this.listView, 0, new Class[]{UserCell.class}, null, new Drawable[]{Theme.avatar_photoDrawable, Theme.avatar_broadcastDrawable, Theme.avatar_savedDrawable}, null, Theme.key_avatar_text);
        ThemeDescriptionDelegate themeDescriptionDelegate = сellDelegate;
        r10[73] = new ThemeDescription(null, 0, null, null, null, themeDescriptionDelegate, Theme.key_avatar_backgroundRed);
        r10[74] = new ThemeDescription(null, 0, null, null, null, themeDescriptionDelegate, Theme.key_avatar_backgroundOrange);
        r10[75] = new ThemeDescription(null, 0, null, null, null, themeDescriptionDelegate, Theme.key_avatar_backgroundViolet);
        r10[76] = new ThemeDescription(null, 0, null, null, null, themeDescriptionDelegate, Theme.key_avatar_backgroundGreen);
        r10[77] = new ThemeDescription(null, 0, null, null, null, themeDescriptionDelegate, Theme.key_avatar_backgroundCyan);
        r10[78] = new ThemeDescription(null, 0, null, null, null, themeDescriptionDelegate, Theme.key_avatar_backgroundBlue);
        r10[79] = new ThemeDescription(null, 0, null, null, null, themeDescriptionDelegate, Theme.key_avatar_backgroundPink);
        r10[80] = new ThemeDescription(this.listView, 0, new Class[]{LoadingCell.class}, new String[]{"progressBar"}, null, null, null, Theme.key_progressCircle);
        r10[81] = new ThemeDescription(this.listView, 0, new Class[]{AboutLinkCell.class}, new String[]{"imageView"}, null, null, null, Theme.key_windowBackgroundWhiteGrayIcon);
        r10[82] = new ThemeDescription(this.listView, ThemeDescription.FLAG_TEXTCOLOR, new Class[]{AboutLinkCell.class}, Theme.profile_aboutTextPaint, null, null, Theme.key_windowBackgroundWhiteBlackText);
        r10[83] = new ThemeDescription(this.listView, ThemeDescription.FLAG_LINKCOLOR, new Class[]{AboutLinkCell.class}, Theme.profile_aboutTextPaint, null, null, Theme.key_windowBackgroundWhiteLinkText);
        r10[84] = new ThemeDescription(this.listView, 0, new Class[]{AboutLinkCell.class}, Theme.linkSelectionPaint, null, null, Theme.key_windowBackgroundWhiteLinkSelection);
        r10[85] = new ThemeDescription(this.listView, ThemeDescription.FLAG_BACKGROUNDFILTER, new Class[]{ShadowSectionCell.class}, null, null, null, Theme.key_windowBackgroundGrayShadow);
        r10[86] = new ThemeDescription(this.listView, ThemeDescription.FLAG_BACKGROUNDFILTER | ThemeDescription.FLAG_CELLBACKGROUNDCOLOR, new Class[]{ShadowSectionCell.class}, null, null, null, Theme.key_windowBackgroundGray);
        r10[87] = new ThemeDescription(this.listView, ThemeDescription.FLAG_BACKGROUNDFILTER, new Class[]{TextInfoPrivacyCell.class}, null, null, null, Theme.key_windowBackgroundGrayShadow);
        r10[88] = new ThemeDescription(this.listView, ThemeDescription.FLAG_BACKGROUNDFILTER | ThemeDescription.FLAG_CELLBACKGROUNDCOLOR, new Class[]{TextInfoPrivacyCell.class}, null, null, null, Theme.key_windowBackgroundGray);
        r10[89] = new ThemeDescription(this.listView, 0, new Class[]{TextInfoPrivacyCell.class}, new String[]{"textView"}, null, null, null, Theme.key_windowBackgroundWhiteGrayText4);
        r10[90] = new ThemeDescription(this.nameTextView[1], 0, null, null, new Drawable[]{Theme.profile_verifiedCheckDrawable}, null, Theme.key_profile_verifiedCheck);
        r10[91] = new ThemeDescription(this.nameTextView[1], 0, null, null, new Drawable[]{Theme.profile_verifiedDrawable}, null, Theme.key_profile_verifiedBackground);
        return r10;
    }
}
