package org.telegram.ui;

import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.DialogInterface.OnShowListener;
import android.graphics.drawable.Drawable;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.DatePicker;
import android.widget.FrameLayout;
import android.widget.TimePicker;
import java.util.Calendar;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.beta.R;
import org.telegram.messenger.support.widget.LinearLayoutManager;
import org.telegram.messenger.support.widget.RecyclerView.Adapter;
import org.telegram.messenger.support.widget.RecyclerView.ViewHolder;
import org.telegram.tgnet.TLRPC.Chat;
import org.telegram.tgnet.TLRPC.TL_channelAdminRights;
import org.telegram.tgnet.TLRPC.TL_channelBannedRights;
import org.telegram.tgnet.TLRPC.User;
import org.telegram.ui.ActionBar.ActionBar.ActionBarMenuOnItemClick;
import org.telegram.ui.ActionBar.BaseFragment;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.ActionBar.ThemeDescription;
import org.telegram.ui.ActionBar.ThemeDescription.ThemeDescriptionDelegate;
import org.telegram.ui.Cells.HeaderCell;
import org.telegram.ui.Cells.ShadowSectionCell;
import org.telegram.ui.Cells.TextCheckCell2;
import org.telegram.ui.Cells.TextInfoPrivacyCell;
import org.telegram.ui.Cells.TextSettingsCell;
import org.telegram.ui.Cells.UserCell;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.ui.Components.RecyclerListView;
import org.telegram.ui.Components.RecyclerListView.OnItemClickListener;
import org.telegram.ui.Components.RecyclerListView.SelectionAdapter;

public class ChannelRightsEditActivity extends BaseFragment {
    private static final int done_button = 1;
    private int addAdminsRow;
    private int addUsersRow;
    private TL_channelAdminRights adminRights;
    private int banUsersRow;
    private TL_channelBannedRights bannedRights;
    private boolean canEdit;
    private int cantEditInfoRow;
    private int changeInfoRow;
    private int chatId;
    private int currentType;
    private User currentUser;
    private ChannelRightsEditActivityDelegate delegate;
    private int deleteMessagesRow;
    private int editMesagesRow;
    private int embedLinksRow;
    private boolean isDemocracy;
    private boolean isMegagroup;
    private RecyclerListView listView;
    private ListAdapter listViewAdapter;
    private TL_channelAdminRights myAdminRights;
    private int pinMessagesRow;
    private int postMessagesRow;
    private int removeAdminRow;
    private int removeAdminShadowRow;
    private int rightsShadowRow;
    private int rowCount;
    private int sendMediaRow;
    private int sendMessagesRow;
    private int sendStickersRow;
    private int untilDateRow;
    private int viewMessagesRow;

    public interface ChannelRightsEditActivityDelegate {
        void didSetRights(int i, TL_channelAdminRights tL_channelAdminRights, TL_channelBannedRights tL_channelBannedRights);
    }

    private class ListAdapter extends SelectionAdapter {
        private Context mContext;

        public void onBindViewHolder(org.telegram.messenger.support.widget.RecyclerView.ViewHolder r1, int r2) {
            /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.DecodeException: Load method exception in method: org.telegram.ui.ChannelRightsEditActivity.ListAdapter.onBindViewHolder(org.telegram.messenger.support.widget.RecyclerView$ViewHolder, int):void
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
            r0 = r9.getItemViewType();
            r1 = 0;
            r2 = 1;
            switch(r0) {
                case 0: goto L_0x0377;
                case 1: goto L_0x035e;
                case 2: goto L_0x02b5;
                case 3: goto L_0x0285;
                case 4: goto L_0x0057;
                case 5: goto L_0x000b;
                default: goto L_0x0009;
            };
        L_0x0009:
            goto L_0x0386;
        L_0x000b:
            r0 = r9.itemView;
            r0 = (org.telegram.ui.Cells.ShadowSectionCell) r0;
            r1 = org.telegram.ui.ChannelRightsEditActivity.this;
            r1 = r1.rightsShadowRow;
            r2 = 2131165331; // 0x7f070093 float:1.7944876E38 double:1.0529355757E-314;
            r3 = 2131165332; // 0x7f070094 float:1.7944878E38 double:1.052935576E-314;
            if (r10 != r1) goto L_0x0035;
        L_0x001d:
            r1 = r8.mContext;
            r4 = org.telegram.ui.ChannelRightsEditActivity.this;
            r4 = r4.removeAdminRow;
            r5 = -1;
            if (r4 != r5) goto L_0x002a;
        L_0x0028:
            r2 = r3;
        L_0x002a:
            r3 = "windowBackgroundGrayShadow";
            r1 = org.telegram.ui.ActionBar.Theme.getThemedDrawable(r1, r2, r3);
            r0.setBackgroundDrawable(r1);
            goto L_0x0386;
        L_0x0035:
            r1 = org.telegram.ui.ChannelRightsEditActivity.this;
            r1 = r1.removeAdminShadowRow;
            if (r10 != r1) goto L_0x004a;
        L_0x003d:
            r1 = r8.mContext;
            r2 = "windowBackgroundGrayShadow";
            r1 = org.telegram.ui.ActionBar.Theme.getThemedDrawable(r1, r3, r2);
            r0.setBackgroundDrawable(r1);
            goto L_0x0386;
        L_0x004a:
            r1 = r8.mContext;
            r3 = "windowBackgroundGrayShadow";
            r1 = org.telegram.ui.ActionBar.Theme.getThemedDrawable(r1, r2, r3);
            r0.setBackgroundDrawable(r1);
            goto L_0x0386;
        L_0x0057:
            r0 = r9.itemView;
            r0 = (org.telegram.ui.Cells.TextCheckCell2) r0;
            r3 = org.telegram.ui.ChannelRightsEditActivity.this;
            r3 = r3.changeInfoRow;
            if (r10 != r3) goto L_0x0097;
        L_0x0063:
            r3 = org.telegram.ui.ChannelRightsEditActivity.this;
            r3 = r3.isMegagroup;
            if (r3 == 0) goto L_0x0081;
        L_0x006b:
            r3 = "EditAdminChangeGroupInfo";
            r4 = 2131493405; // 0x7f0c021d float:1.861029E38 double:1.0530976657E-314;
            r3 = org.telegram.messenger.LocaleController.getString(r3, r4);
            r4 = org.telegram.ui.ChannelRightsEditActivity.this;
            r4 = r4.adminRights;
            r4 = r4.change_info;
            r0.setTextAndCheck(r3, r4, r2);
            goto L_0x023b;
        L_0x0081:
            r3 = "EditAdminChangeChannelInfo";
            r4 = 2131493404; // 0x7f0c021c float:1.8610287E38 double:1.0530976653E-314;
            r3 = org.telegram.messenger.LocaleController.getString(r3, r4);
            r4 = org.telegram.ui.ChannelRightsEditActivity.this;
            r4 = r4.adminRights;
            r4 = r4.change_info;
            r0.setTextAndCheck(r3, r4, r2);
            goto L_0x023b;
        L_0x0097:
            r3 = org.telegram.ui.ChannelRightsEditActivity.this;
            r3 = r3.postMessagesRow;
            if (r10 != r3) goto L_0x00b5;
        L_0x009f:
            r3 = "EditAdminPostMessages";
            r4 = 2131493410; // 0x7f0c0222 float:1.86103E38 double:1.053097668E-314;
            r3 = org.telegram.messenger.LocaleController.getString(r3, r4);
            r4 = org.telegram.ui.ChannelRightsEditActivity.this;
            r4 = r4.adminRights;
            r4 = r4.post_messages;
            r0.setTextAndCheck(r3, r4, r2);
            goto L_0x023b;
        L_0x00b5:
            r3 = org.telegram.ui.ChannelRightsEditActivity.this;
            r3 = r3.editMesagesRow;
            if (r10 != r3) goto L_0x00d3;
        L_0x00bd:
            r3 = "EditAdminEditMessages";
            r4 = 2131493407; // 0x7f0c021f float:1.8610293E38 double:1.0530976667E-314;
            r3 = org.telegram.messenger.LocaleController.getString(r3, r4);
            r4 = org.telegram.ui.ChannelRightsEditActivity.this;
            r4 = r4.adminRights;
            r4 = r4.edit_messages;
            r0.setTextAndCheck(r3, r4, r2);
            goto L_0x023b;
        L_0x00d3:
            r3 = org.telegram.ui.ChannelRightsEditActivity.this;
            r3 = r3.deleteMessagesRow;
            if (r10 != r3) goto L_0x010f;
        L_0x00db:
            r3 = org.telegram.ui.ChannelRightsEditActivity.this;
            r3 = r3.isMegagroup;
            if (r3 == 0) goto L_0x00f9;
        L_0x00e3:
            r3 = "EditAdminGroupDeleteMessages";
            r4 = 2131493408; // 0x7f0c0220 float:1.8610295E38 double:1.053097667E-314;
            r3 = org.telegram.messenger.LocaleController.getString(r3, r4);
            r4 = org.telegram.ui.ChannelRightsEditActivity.this;
            r4 = r4.adminRights;
            r4 = r4.delete_messages;
            r0.setTextAndCheck(r3, r4, r2);
            goto L_0x023b;
        L_0x00f9:
            r3 = "EditAdminDeleteMessages";
            r4 = 2131493406; // 0x7f0c021e float:1.8610291E38 double:1.053097666E-314;
            r3 = org.telegram.messenger.LocaleController.getString(r3, r4);
            r4 = org.telegram.ui.ChannelRightsEditActivity.this;
            r4 = r4.adminRights;
            r4 = r4.delete_messages;
            r0.setTextAndCheck(r3, r4, r2);
            goto L_0x023b;
        L_0x010f:
            r3 = org.telegram.ui.ChannelRightsEditActivity.this;
            r3 = r3.addAdminsRow;
            if (r10 != r3) goto L_0x012d;
        L_0x0117:
            r3 = "EditAdminAddAdmins";
            r4 = 2131493399; // 0x7f0c0217 float:1.8610277E38 double:1.053097663E-314;
            r3 = org.telegram.messenger.LocaleController.getString(r3, r4);
            r4 = org.telegram.ui.ChannelRightsEditActivity.this;
            r4 = r4.adminRights;
            r4 = r4.add_admins;
            r0.setTextAndCheck(r3, r4, r1);
            goto L_0x023b;
        L_0x012d:
            r3 = org.telegram.ui.ChannelRightsEditActivity.this;
            r3 = r3.banUsersRow;
            if (r10 != r3) goto L_0x014b;
        L_0x0135:
            r3 = "EditAdminBanUsers";
            r4 = 2131493402; // 0x7f0c021a float:1.8610283E38 double:1.0530976643E-314;
            r3 = org.telegram.messenger.LocaleController.getString(r3, r4);
            r4 = org.telegram.ui.ChannelRightsEditActivity.this;
            r4 = r4.adminRights;
            r4 = r4.ban_users;
            r0.setTextAndCheck(r3, r4, r2);
            goto L_0x023b;
        L_0x014b:
            r3 = org.telegram.ui.ChannelRightsEditActivity.this;
            r3 = r3.addUsersRow;
            if (r10 != r3) goto L_0x0187;
        L_0x0153:
            r3 = org.telegram.ui.ChannelRightsEditActivity.this;
            r3 = r3.isDemocracy;
            if (r3 != 0) goto L_0x0171;
        L_0x015b:
            r3 = "EditAdminAddUsers";
            r4 = 2131493400; // 0x7f0c0218 float:1.861028E38 double:1.0530976633E-314;
            r3 = org.telegram.messenger.LocaleController.getString(r3, r4);
            r4 = org.telegram.ui.ChannelRightsEditActivity.this;
            r4 = r4.adminRights;
            r4 = r4.invite_users;
            r0.setTextAndCheck(r3, r4, r2);
            goto L_0x023b;
        L_0x0171:
            r3 = "EditAdminAddUsersViaLink";
            r4 = 2131493401; // 0x7f0c0219 float:1.8610281E38 double:1.053097664E-314;
            r3 = org.telegram.messenger.LocaleController.getString(r3, r4);
            r4 = org.telegram.ui.ChannelRightsEditActivity.this;
            r4 = r4.adminRights;
            r4 = r4.invite_users;
            r0.setTextAndCheck(r3, r4, r2);
            goto L_0x023b;
        L_0x0187:
            r3 = org.telegram.ui.ChannelRightsEditActivity.this;
            r3 = r3.pinMessagesRow;
            if (r10 != r3) goto L_0x01a5;
        L_0x018f:
            r3 = "EditAdminPinMessages";
            r4 = 2131493409; // 0x7f0c0221 float:1.8610297E38 double:1.0530976677E-314;
            r3 = org.telegram.messenger.LocaleController.getString(r3, r4);
            r4 = org.telegram.ui.ChannelRightsEditActivity.this;
            r4 = r4.adminRights;
            r4 = r4.pin_messages;
            r0.setTextAndCheck(r3, r4, r2);
            goto L_0x023b;
        L_0x01a5:
            r3 = org.telegram.ui.ChannelRightsEditActivity.this;
            r3 = r3.viewMessagesRow;
            if (r10 != r3) goto L_0x01c4;
        L_0x01ad:
            r3 = "UserRestrictionsRead";
            r4 = 2131494550; // 0x7f0c0696 float:1.8612612E38 double:1.0530982315E-314;
            r3 = org.telegram.messenger.LocaleController.getString(r3, r4);
            r4 = org.telegram.ui.ChannelRightsEditActivity.this;
            r4 = r4.bannedRights;
            r4 = r4.view_messages;
            r4 = r4 ^ r2;
            r0.setTextAndCheck(r3, r4, r2);
            goto L_0x023b;
        L_0x01c4:
            r3 = org.telegram.ui.ChannelRightsEditActivity.this;
            r3 = r3.sendMessagesRow;
            if (r10 != r3) goto L_0x01e2;
        L_0x01cc:
            r3 = "UserRestrictionsSend";
            r4 = 2131494551; // 0x7f0c0697 float:1.8612614E38 double:1.053098232E-314;
            r3 = org.telegram.messenger.LocaleController.getString(r3, r4);
            r4 = org.telegram.ui.ChannelRightsEditActivity.this;
            r4 = r4.bannedRights;
            r4 = r4.send_messages;
            r4 = r4 ^ r2;
            r0.setTextAndCheck(r3, r4, r2);
            goto L_0x023b;
        L_0x01e2:
            r3 = org.telegram.ui.ChannelRightsEditActivity.this;
            r3 = r3.sendMediaRow;
            if (r10 != r3) goto L_0x0200;
        L_0x01ea:
            r3 = "UserRestrictionsSendMedia";
            r4 = 2131494552; // 0x7f0c0698 float:1.8612616E38 double:1.0530982324E-314;
            r3 = org.telegram.messenger.LocaleController.getString(r3, r4);
            r4 = org.telegram.ui.ChannelRightsEditActivity.this;
            r4 = r4.bannedRights;
            r4 = r4.send_media;
            r4 = r4 ^ r2;
            r0.setTextAndCheck(r3, r4, r2);
            goto L_0x023b;
        L_0x0200:
            r3 = org.telegram.ui.ChannelRightsEditActivity.this;
            r3 = r3.sendStickersRow;
            if (r10 != r3) goto L_0x021e;
        L_0x0208:
            r3 = "UserRestrictionsSendStickers";
            r4 = 2131494553; // 0x7f0c0699 float:1.8612618E38 double:1.053098233E-314;
            r3 = org.telegram.messenger.LocaleController.getString(r3, r4);
            r4 = org.telegram.ui.ChannelRightsEditActivity.this;
            r4 = r4.bannedRights;
            r4 = r4.send_stickers;
            r4 = r4 ^ r2;
            r0.setTextAndCheck(r3, r4, r2);
            goto L_0x023b;
        L_0x021e:
            r3 = org.telegram.ui.ChannelRightsEditActivity.this;
            r3 = r3.embedLinksRow;
            if (r10 != r3) goto L_0x023b;
        L_0x0226:
            r3 = "UserRestrictionsEmbedLinks";
            r4 = 2131494549; // 0x7f0c0695 float:1.861261E38 double:1.053098231E-314;
            r3 = org.telegram.messenger.LocaleController.getString(r3, r4);
            r4 = org.telegram.ui.ChannelRightsEditActivity.this;
            r4 = r4.bannedRights;
            r4 = r4.embed_links;
            r4 = r4 ^ r2;
            r0.setTextAndCheck(r3, r4, r2);
        L_0x023b:
            r3 = org.telegram.ui.ChannelRightsEditActivity.this;
            r3 = r3.sendMediaRow;
            if (r10 == r3) goto L_0x026a;
        L_0x0243:
            r3 = org.telegram.ui.ChannelRightsEditActivity.this;
            r3 = r3.sendStickersRow;
            if (r10 == r3) goto L_0x026a;
        L_0x024b:
            r3 = org.telegram.ui.ChannelRightsEditActivity.this;
            r3 = r3.embedLinksRow;
            if (r10 != r3) goto L_0x0254;
        L_0x0253:
            goto L_0x026a;
        L_0x0254:
            r1 = org.telegram.ui.ChannelRightsEditActivity.this;
            r1 = r1.sendMessagesRow;
            if (r10 != r1) goto L_0x0386;
        L_0x025c:
            r1 = org.telegram.ui.ChannelRightsEditActivity.this;
            r1 = r1.bannedRights;
            r1 = r1.view_messages;
            r1 = r1 ^ r2;
            r0.setEnabled(r1);
            goto L_0x0386;
        L_0x026a:
            r3 = org.telegram.ui.ChannelRightsEditActivity.this;
            r3 = r3.bannedRights;
            r3 = r3.send_messages;
            if (r3 != 0) goto L_0x0280;
        L_0x0274:
            r3 = org.telegram.ui.ChannelRightsEditActivity.this;
            r3 = r3.bannedRights;
            r3 = r3.view_messages;
            if (r3 != 0) goto L_0x0280;
        L_0x027e:
            r1 = r2;
        L_0x0280:
            r0.setEnabled(r1);
            goto L_0x0386;
        L_0x0285:
            r0 = r9.itemView;
            r0 = (org.telegram.ui.Cells.HeaderCell) r0;
            r1 = org.telegram.ui.ChannelRightsEditActivity.this;
            r1 = r1.currentType;
            if (r1 != 0) goto L_0x029f;
        L_0x0291:
            r1 = "EditAdminWhatCanDo";
            r2 = 2131493413; // 0x7f0c0225 float:1.8610305E38 double:1.0530976697E-314;
            r1 = org.telegram.messenger.LocaleController.getString(r1, r2);
            r0.setText(r1);
            goto L_0x0386;
        L_0x029f:
            r1 = org.telegram.ui.ChannelRightsEditActivity.this;
            r1 = r1.currentType;
            if (r1 != r2) goto L_0x0386;
        L_0x02a7:
            r1 = "UserRestrictionsCanDo";
            r2 = 2131494548; // 0x7f0c0694 float:1.8612607E38 double:1.0530982305E-314;
            r1 = org.telegram.messenger.LocaleController.getString(r1, r2);
            r0.setText(r1);
            goto L_0x0386;
        L_0x02b5:
            r0 = r9.itemView;
            r0 = (org.telegram.ui.Cells.TextSettingsCell) r0;
            r3 = org.telegram.ui.ChannelRightsEditActivity.this;
            r3 = r3.removeAdminRow;
            if (r10 != r3) goto L_0x02fb;
        L_0x02c1:
            r3 = "windowBackgroundWhiteRedText3";
            r3 = org.telegram.ui.ActionBar.Theme.getColor(r3);
            r0.setTextColor(r3);
            r3 = "windowBackgroundWhiteRedText3";
            r0.setTag(r3);
            r3 = org.telegram.ui.ChannelRightsEditActivity.this;
            r3 = r3.currentType;
            if (r3 != 0) goto L_0x02e5;
        L_0x02d7:
            r2 = "EditAdminRemoveAdmin";
            r3 = 2131493412; // 0x7f0c0224 float:1.8610303E38 double:1.053097669E-314;
            r2 = org.telegram.messenger.LocaleController.getString(r2, r3);
            r0.setText(r2, r1);
            goto L_0x0386;
        L_0x02e5:
            r3 = org.telegram.ui.ChannelRightsEditActivity.this;
            r3 = r3.currentType;
            if (r3 != r2) goto L_0x0386;
        L_0x02ed:
            r2 = "UserRestrictionsBlock";
            r3 = 2131494546; // 0x7f0c0692 float:1.8612603E38 double:1.0530982295E-314;
            r2 = org.telegram.messenger.LocaleController.getString(r2, r3);
            r0.setText(r2, r1);
            goto L_0x0386;
        L_0x02fb:
            r2 = org.telegram.ui.ChannelRightsEditActivity.this;
            r2 = r2.untilDateRow;
            if (r10 != r2) goto L_0x0386;
        L_0x0303:
            r2 = "windowBackgroundWhiteBlackText";
            r2 = org.telegram.ui.ActionBar.Theme.getColor(r2);
            r0.setTextColor(r2);
            r2 = "windowBackgroundWhiteBlackText";
            r0.setTag(r2);
            r2 = org.telegram.ui.ChannelRightsEditActivity.this;
            r2 = r2.bannedRights;
            r2 = r2.until_date;
            if (r2 == 0) goto L_0x0347;
        L_0x031b:
            r2 = org.telegram.ui.ChannelRightsEditActivity.this;
            r2 = r2.bannedRights;
            r2 = r2.until_date;
            r2 = (long) r2;
            r4 = java.lang.System.currentTimeMillis();
            r6 = 1000; // 0x3e8 float:1.401E-42 double:4.94E-321;
            r4 = r4 / r6;
            r6 = r2 - r4;
            r2 = java.lang.Math.abs(r6);
            r4 = 315360000; // 0x12cc0300 float:1.287495E-27 double:1.55808542E-315;
            r6 = (r2 > r4 ? 1 : (r2 == r4 ? 0 : -1));
            if (r6 <= 0) goto L_0x0339;
        L_0x0338:
            goto L_0x0347;
        L_0x0339:
            r2 = org.telegram.ui.ChannelRightsEditActivity.this;
            r2 = r2.bannedRights;
            r2 = r2.until_date;
            r2 = (long) r2;
            r2 = org.telegram.messenger.LocaleController.formatDateForBan(r2);
            goto L_0x0350;
        L_0x0347:
            r2 = "UserRestrictionsUntilForever";
            r3 = 2131494555; // 0x7f0c069b float:1.8612622E38 double:1.053098234E-314;
            r2 = org.telegram.messenger.LocaleController.getString(r2, r3);
            r3 = "UserRestrictionsUntil";
            r4 = 2131494554; // 0x7f0c069a float:1.861262E38 double:1.0530982334E-314;
            r3 = org.telegram.messenger.LocaleController.getString(r3, r4);
            r0.setTextAndValue(r3, r2, r1);
            goto L_0x0386;
        L_0x035e:
            r0 = r9.itemView;
            r0 = (org.telegram.ui.Cells.TextInfoPrivacyCell) r0;
            r1 = org.telegram.ui.ChannelRightsEditActivity.this;
            r1 = r1.cantEditInfoRow;
            if (r10 != r1) goto L_0x0386;
            r1 = "EditAdminCantEdit";
            r2 = 2131493403; // 0x7f0c021b float:1.8610285E38 double:1.053097665E-314;
            r1 = org.telegram.messenger.LocaleController.getString(r1, r2);
            r0.setText(r1);
            goto L_0x0386;
        L_0x0377:
            r0 = r9.itemView;
            r0 = (org.telegram.ui.Cells.UserCell) r0;
            r2 = org.telegram.ui.ChannelRightsEditActivity.this;
            r2 = r2.currentUser;
            r3 = 0;
            r0.setData(r2, r3, r3, r1);
        L_0x0386:
            return;
            */
            throw new UnsupportedOperationException("Method not decompiled: org.telegram.ui.ChannelRightsEditActivity.ListAdapter.onBindViewHolder(org.telegram.messenger.support.widget.RecyclerView$ViewHolder, int):void");
        }

        public org.telegram.messenger.support.widget.RecyclerView.ViewHolder onCreateViewHolder(android.view.ViewGroup r1, int r2) {
            /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.DecodeException: Load method exception in method: org.telegram.ui.ChannelRightsEditActivity.ListAdapter.onCreateViewHolder(android.view.ViewGroup, int):org.telegram.messenger.support.widget.RecyclerView$ViewHolder
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
            switch(r6) {
                case 0: goto L_0x0054;
                case 1: goto L_0x003e;
                case 2: goto L_0x002d;
                case 3: goto L_0x001c;
                case 4: goto L_0x000b;
                default: goto L_0x0003;
            };
        L_0x0003:
            r0 = new org.telegram.ui.Cells.ShadowSectionCell;
            r1 = r4.mContext;
            r0.<init>(r1);
            goto L_0x0067;
        L_0x000b:
            r0 = new org.telegram.ui.Cells.TextCheckCell2;
            r1 = r4.mContext;
            r0.<init>(r1);
            r1 = "windowBackgroundWhite";
            r1 = org.telegram.ui.ActionBar.Theme.getColor(r1);
            r0.setBackgroundColor(r1);
            goto L_0x0067;
        L_0x001c:
            r0 = new org.telegram.ui.Cells.HeaderCell;
            r1 = r4.mContext;
            r0.<init>(r1);
            r1 = "windowBackgroundWhite";
            r1 = org.telegram.ui.ActionBar.Theme.getColor(r1);
            r0.setBackgroundColor(r1);
            goto L_0x0067;
        L_0x002d:
            r0 = new org.telegram.ui.Cells.TextSettingsCell;
            r1 = r4.mContext;
            r0.<init>(r1);
            r1 = "windowBackgroundWhite";
            r1 = org.telegram.ui.ActionBar.Theme.getColor(r1);
            r0.setBackgroundColor(r1);
            goto L_0x0067;
        L_0x003e:
            r0 = new org.telegram.ui.Cells.TextInfoPrivacyCell;
            r1 = r4.mContext;
            r0.<init>(r1);
            r1 = r4.mContext;
            r2 = 2131165332; // 0x7f070094 float:1.7944878E38 double:1.052935576E-314;
            r3 = "windowBackgroundGrayShadow";
            r1 = org.telegram.ui.ActionBar.Theme.getThemedDrawable(r1, r2, r3);
            r0.setBackgroundDrawable(r1);
            goto L_0x0067;
        L_0x0054:
            r0 = new org.telegram.ui.Cells.UserCell;
            r1 = r4.mContext;
            r2 = 1;
            r3 = 0;
            r0.<init>(r1, r2, r3, r3);
            r1 = "windowBackgroundWhite";
            r1 = org.telegram.ui.ActionBar.Theme.getColor(r1);
            r0.setBackgroundColor(r1);
            r1 = new org.telegram.ui.Components.RecyclerListView$Holder;
            r1.<init>(r0);
            return r1;
            */
            throw new UnsupportedOperationException("Method not decompiled: org.telegram.ui.ChannelRightsEditActivity.ListAdapter.onCreateViewHolder(android.view.ViewGroup, int):org.telegram.messenger.support.widget.RecyclerView$ViewHolder");
        }

        public ListAdapter(Context context) {
            this.mContext = context;
        }

        public boolean isEnabled(ViewHolder holder) {
            boolean z = false;
            if (!ChannelRightsEditActivity.this.canEdit) {
                return false;
            }
            int type = holder.getItemViewType();
            if (ChannelRightsEditActivity.this.currentType == 0 && type == 4) {
                int position = holder.getAdapterPosition();
                if (position == ChannelRightsEditActivity.this.changeInfoRow) {
                    return ChannelRightsEditActivity.this.myAdminRights.change_info;
                }
                if (position == ChannelRightsEditActivity.this.postMessagesRow) {
                    return ChannelRightsEditActivity.this.myAdminRights.post_messages;
                }
                if (position == ChannelRightsEditActivity.this.editMesagesRow) {
                    return ChannelRightsEditActivity.this.myAdminRights.edit_messages;
                }
                if (position == ChannelRightsEditActivity.this.deleteMessagesRow) {
                    return ChannelRightsEditActivity.this.myAdminRights.delete_messages;
                }
                if (position == ChannelRightsEditActivity.this.addAdminsRow) {
                    return ChannelRightsEditActivity.this.myAdminRights.add_admins;
                }
                if (position == ChannelRightsEditActivity.this.banUsersRow) {
                    return ChannelRightsEditActivity.this.myAdminRights.ban_users;
                }
                if (position == ChannelRightsEditActivity.this.addUsersRow) {
                    return ChannelRightsEditActivity.this.myAdminRights.invite_users;
                }
                if (position == ChannelRightsEditActivity.this.pinMessagesRow) {
                    return ChannelRightsEditActivity.this.myAdminRights.pin_messages;
                }
            }
            if (!(type == 3 || type == 1 || type == 5)) {
                z = true;
            }
            return z;
        }

        public int getItemCount() {
            return ChannelRightsEditActivity.this.rowCount;
        }

        public int getItemViewType(int position) {
            if (position == 0) {
                return 0;
            }
            if (!(position == 1 || position == ChannelRightsEditActivity.this.rightsShadowRow)) {
                if (position != ChannelRightsEditActivity.this.removeAdminShadowRow) {
                    if (position == 2) {
                        return 3;
                    }
                    if (!(position == ChannelRightsEditActivity.this.changeInfoRow || position == ChannelRightsEditActivity.this.postMessagesRow || position == ChannelRightsEditActivity.this.editMesagesRow || position == ChannelRightsEditActivity.this.deleteMessagesRow || position == ChannelRightsEditActivity.this.addAdminsRow || position == ChannelRightsEditActivity.this.banUsersRow || position == ChannelRightsEditActivity.this.addUsersRow || position == ChannelRightsEditActivity.this.pinMessagesRow || position == ChannelRightsEditActivity.this.viewMessagesRow || position == ChannelRightsEditActivity.this.sendMessagesRow || position == ChannelRightsEditActivity.this.sendMediaRow || position == ChannelRightsEditActivity.this.sendStickersRow)) {
                        if (position != ChannelRightsEditActivity.this.embedLinksRow) {
                            if (position == ChannelRightsEditActivity.this.cantEditInfoRow) {
                                return 1;
                            }
                            return 2;
                        }
                    }
                    return 4;
                }
            }
            return 5;
        }
    }

    public ChannelRightsEditActivity(int userId, int channelId, TL_channelAdminRights rightsAdmin, TL_channelBannedRights rightsBanned, int type, boolean edit) {
        int i;
        TL_channelAdminRights tL_channelAdminRights = rightsAdmin;
        TL_channelBannedRights tL_channelBannedRights = rightsBanned;
        int i2 = type;
        this.chatId = channelId;
        this.currentUser = MessagesController.getInstance(this.currentAccount).getUser(Integer.valueOf(userId));
        this.currentType = i2;
        this.canEdit = edit;
        Chat chat = MessagesController.getInstance(this.currentAccount).getChat(Integer.valueOf(this.chatId));
        if (chat != null) {
            r0.isMegagroup = chat.megagroup;
            r0.myAdminRights = chat.admin_rights;
        }
        if (r0.myAdminRights == null) {
            r0.myAdminRights = new TL_channelAdminRights();
            TL_channelAdminRights tL_channelAdminRights2 = r0.myAdminRights;
            TL_channelAdminRights tL_channelAdminRights3 = r0.myAdminRights;
            TL_channelAdminRights tL_channelAdminRights4 = r0.myAdminRights;
            TL_channelAdminRights tL_channelAdminRights5 = r0.myAdminRights;
            TL_channelAdminRights tL_channelAdminRights6 = r0.myAdminRights;
            TL_channelAdminRights tL_channelAdminRights7 = r0.myAdminRights;
            TL_channelAdminRights tL_channelAdminRights8 = r0.myAdminRights;
            TL_channelAdminRights tL_channelAdminRights9 = r0.myAdminRights;
            r0.myAdminRights.add_admins = true;
            tL_channelAdminRights9.pin_messages = true;
            tL_channelAdminRights8.invite_link = true;
            tL_channelAdminRights7.invite_users = true;
            tL_channelAdminRights6.ban_users = true;
            tL_channelAdminRights5.delete_messages = true;
            tL_channelAdminRights4.edit_messages = true;
            tL_channelAdminRights3.post_messages = true;
            tL_channelAdminRights2.change_info = true;
        }
        boolean initialIsSet = false;
        if (i2 == 0) {
            r0.adminRights = new TL_channelAdminRights();
            if (tL_channelAdminRights == null) {
                r0.adminRights.change_info = r0.myAdminRights.change_info;
                r0.adminRights.post_messages = r0.myAdminRights.post_messages;
                r0.adminRights.edit_messages = r0.myAdminRights.edit_messages;
                r0.adminRights.delete_messages = r0.myAdminRights.delete_messages;
                r0.adminRights.ban_users = r0.myAdminRights.ban_users;
                r0.adminRights.invite_users = r0.myAdminRights.invite_users;
                r0.adminRights.invite_link = r0.myAdminRights.invite_link;
                r0.adminRights.pin_messages = r0.myAdminRights.pin_messages;
                initialIsSet = false;
            } else {
                r0.adminRights.change_info = tL_channelAdminRights.change_info;
                r0.adminRights.post_messages = tL_channelAdminRights.post_messages;
                r0.adminRights.edit_messages = tL_channelAdminRights.edit_messages;
                r0.adminRights.delete_messages = tL_channelAdminRights.delete_messages;
                r0.adminRights.ban_users = tL_channelAdminRights.ban_users;
                r0.adminRights.invite_users = tL_channelAdminRights.invite_users;
                r0.adminRights.invite_link = tL_channelAdminRights.invite_link;
                r0.adminRights.pin_messages = tL_channelAdminRights.pin_messages;
                r0.adminRights.add_admins = tL_channelAdminRights.add_admins;
                if (!(r0.adminRights.change_info || r0.adminRights.post_messages || r0.adminRights.edit_messages || r0.adminRights.delete_messages || r0.adminRights.ban_users || r0.adminRights.invite_users || r0.adminRights.invite_link || r0.adminRights.pin_messages)) {
                    if (r0.adminRights.add_admins) {
                    }
                }
                initialIsSet = true;
            }
        } else {
            r0.bannedRights = new TL_channelBannedRights();
            if (tL_channelBannedRights == null) {
                TL_channelBannedRights tL_channelBannedRights2 = r0.bannedRights;
                TL_channelBannedRights tL_channelBannedRights3 = r0.bannedRights;
                TL_channelBannedRights tL_channelBannedRights4 = r0.bannedRights;
                TL_channelBannedRights tL_channelBannedRights5 = r0.bannedRights;
                TL_channelBannedRights tL_channelBannedRights6 = r0.bannedRights;
                TL_channelBannedRights tL_channelBannedRights7 = r0.bannedRights;
                TL_channelBannedRights tL_channelBannedRights8 = r0.bannedRights;
                r0.bannedRights.send_inline = true;
                tL_channelBannedRights8.send_games = true;
                tL_channelBannedRights7.send_gifs = true;
                tL_channelBannedRights6.send_stickers = true;
                tL_channelBannedRights5.embed_links = true;
                tL_channelBannedRights4.send_messages = true;
                tL_channelBannedRights3.send_media = true;
                tL_channelBannedRights2.view_messages = true;
            } else {
                r0.bannedRights.view_messages = tL_channelBannedRights.view_messages;
                r0.bannedRights.send_messages = tL_channelBannedRights.send_messages;
                r0.bannedRights.send_media = tL_channelBannedRights.send_media;
                r0.bannedRights.send_stickers = tL_channelBannedRights.send_stickers;
                r0.bannedRights.send_gifs = tL_channelBannedRights.send_gifs;
                r0.bannedRights.send_games = tL_channelBannedRights.send_games;
                r0.bannedRights.send_inline = tL_channelBannedRights.send_inline;
                r0.bannedRights.embed_links = tL_channelBannedRights.embed_links;
                r0.bannedRights.until_date = tL_channelBannedRights.until_date;
            }
            if (tL_channelBannedRights != null) {
                if (tL_channelBannedRights.view_messages) {
                }
            }
            initialIsSet = true;
        }
        r0.rowCount += 3;
        if (i2 == 0) {
            if (r0.isMegagroup) {
                i = r0.rowCount;
                r0.rowCount = i + 1;
                r0.changeInfoRow = i;
                i = r0.rowCount;
                r0.rowCount = i + 1;
                r0.deleteMessagesRow = i;
                i = r0.rowCount;
                r0.rowCount = i + 1;
                r0.banUsersRow = i;
                i = r0.rowCount;
                r0.rowCount = i + 1;
                r0.addUsersRow = i;
                i = r0.rowCount;
                r0.rowCount = i + 1;
                r0.pinMessagesRow = i;
                i = r0.rowCount;
                r0.rowCount = i + 1;
                r0.addAdminsRow = i;
                r0.isDemocracy = chat.democracy;
            } else {
                i = r0.rowCount;
                r0.rowCount = i + 1;
                r0.changeInfoRow = i;
                i = r0.rowCount;
                r0.rowCount = i + 1;
                r0.postMessagesRow = i;
                i = r0.rowCount;
                r0.rowCount = i + 1;
                r0.editMesagesRow = i;
                i = r0.rowCount;
                r0.rowCount = i + 1;
                r0.deleteMessagesRow = i;
                i = r0.rowCount;
                r0.rowCount = i + 1;
                r0.addUsersRow = i;
                i = r0.rowCount;
                r0.rowCount = i + 1;
                r0.addAdminsRow = i;
            }
        } else if (i2 == 1) {
            i = r0.rowCount;
            r0.rowCount = i + 1;
            r0.viewMessagesRow = i;
            i = r0.rowCount;
            r0.rowCount = i + 1;
            r0.sendMessagesRow = i;
            i = r0.rowCount;
            r0.rowCount = i + 1;
            r0.sendMediaRow = i;
            i = r0.rowCount;
            r0.rowCount = i + 1;
            r0.sendStickersRow = i;
            i = r0.rowCount;
            r0.rowCount = i + 1;
            r0.embedLinksRow = i;
            i = r0.rowCount;
            r0.rowCount = i + 1;
            r0.untilDateRow = i;
        }
        if (r0.canEdit && initialIsSet) {
            i = r0.rowCount;
            r0.rowCount = i + 1;
            r0.rightsShadowRow = i;
            i = r0.rowCount;
            r0.rowCount = i + 1;
            r0.removeAdminRow = i;
            i = r0.rowCount;
            r0.rowCount = i + 1;
            r0.removeAdminShadowRow = i;
            r0.cantEditInfoRow = -1;
            return;
        }
        r0.removeAdminRow = -1;
        r0.removeAdminShadowRow = -1;
        if (i2 != 0 || r0.canEdit) {
            i = r0.rowCount;
            r0.rowCount = i + 1;
            r0.rightsShadowRow = i;
            return;
        }
        r0.rightsShadowRow = -1;
        i = r0.rowCount;
        r0.rowCount = i + 1;
        r0.cantEditInfoRow = i;
    }

    public View createView(Context context) {
        this.actionBar.setBackButtonImage(R.drawable.ic_ab_back);
        int i = 1;
        this.actionBar.setAllowOverlayTitle(true);
        if (this.currentType == 0) {
            this.actionBar.setTitle(LocaleController.getString("EditAdmin", R.string.EditAdmin));
        } else {
            this.actionBar.setTitle(LocaleController.getString("UserRestrictions", R.string.UserRestrictions));
        }
        this.actionBar.setActionBarMenuOnItemClick(new ActionBarMenuOnItemClick() {
            public void onItemClick(int r1) {
                /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.DecodeException: Load method exception in method: org.telegram.ui.ChannelRightsEditActivity.1.onItemClick(int):void
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
                r0 = -1;
                if (r10 != r0) goto L_0x000a;
            L_0x0003:
                r0 = org.telegram.ui.ChannelRightsEditActivity.this;
                r0.finishFragment();
                goto L_0x019b;
            L_0x000a:
                r0 = 1;
                if (r10 != r0) goto L_0x019b;
            L_0x000d:
                r1 = org.telegram.ui.ChannelRightsEditActivity.this;
                r1 = r1.currentType;
                r2 = 0;
                if (r1 != 0) goto L_0x00e6;
            L_0x0016:
                r1 = org.telegram.ui.ChannelRightsEditActivity.this;
                r1 = r1.isMegagroup;
                if (r1 == 0) goto L_0x002f;
            L_0x001e:
                r1 = org.telegram.ui.ChannelRightsEditActivity.this;
                r1 = r1.adminRights;
                r3 = org.telegram.ui.ChannelRightsEditActivity.this;
                r3 = r3.adminRights;
                r3.edit_messages = r2;
                r1.post_messages = r2;
                goto L_0x003f;
            L_0x002f:
                r1 = org.telegram.ui.ChannelRightsEditActivity.this;
                r1 = r1.adminRights;
                r3 = org.telegram.ui.ChannelRightsEditActivity.this;
                r3 = r3.adminRights;
                r3.ban_users = r2;
                r1.pin_messages = r2;
            L_0x003f:
                r1 = org.telegram.ui.ChannelRightsEditActivity.this;
                r1 = r1.currentAccount;
                r3 = org.telegram.messenger.MessagesController.getInstance(r1);
                r1 = org.telegram.ui.ChannelRightsEditActivity.this;
                r4 = r1.chatId;
                r1 = org.telegram.ui.ChannelRightsEditActivity.this;
                r5 = r1.currentUser;
                r1 = org.telegram.ui.ChannelRightsEditActivity.this;
                r6 = r1.adminRights;
                r1 = org.telegram.ui.ChannelRightsEditActivity.this;
                r7 = r1.isMegagroup;
                r1 = org.telegram.ui.ChannelRightsEditActivity.this;
                r8 = r1.getFragmentForAlert(r0);
                r3.setUserAdminRole(r4, r5, r6, r7, r8);
                r1 = org.telegram.ui.ChannelRightsEditActivity.this;
                r1 = r1.delegate;
                if (r1 == 0) goto L_0x0196;
            L_0x0072:
                r1 = org.telegram.ui.ChannelRightsEditActivity.this;
                r1 = r1.delegate;
                r3 = org.telegram.ui.ChannelRightsEditActivity.this;
                r3 = r3.adminRights;
                r3 = r3.change_info;
                if (r3 != 0) goto L_0x00d5;
            L_0x0082:
                r3 = org.telegram.ui.ChannelRightsEditActivity.this;
                r3 = r3.adminRights;
                r3 = r3.post_messages;
                if (r3 != 0) goto L_0x00d5;
            L_0x008c:
                r3 = org.telegram.ui.ChannelRightsEditActivity.this;
                r3 = r3.adminRights;
                r3 = r3.edit_messages;
                if (r3 != 0) goto L_0x00d5;
            L_0x0096:
                r3 = org.telegram.ui.ChannelRightsEditActivity.this;
                r3 = r3.adminRights;
                r3 = r3.delete_messages;
                if (r3 != 0) goto L_0x00d5;
            L_0x00a0:
                r3 = org.telegram.ui.ChannelRightsEditActivity.this;
                r3 = r3.adminRights;
                r3 = r3.ban_users;
                if (r3 != 0) goto L_0x00d5;
            L_0x00aa:
                r3 = org.telegram.ui.ChannelRightsEditActivity.this;
                r3 = r3.adminRights;
                r3 = r3.invite_users;
                if (r3 != 0) goto L_0x00d5;
            L_0x00b4:
                r3 = org.telegram.ui.ChannelRightsEditActivity.this;
                r3 = r3.adminRights;
                r3 = r3.invite_link;
                if (r3 != 0) goto L_0x00d5;
            L_0x00be:
                r3 = org.telegram.ui.ChannelRightsEditActivity.this;
                r3 = r3.adminRights;
                r3 = r3.pin_messages;
                if (r3 != 0) goto L_0x00d5;
            L_0x00c8:
                r3 = org.telegram.ui.ChannelRightsEditActivity.this;
                r3 = r3.adminRights;
                r3 = r3.add_admins;
                if (r3 == 0) goto L_0x00d3;
            L_0x00d2:
                goto L_0x00d5;
            L_0x00d3:
                r0 = r2;
            L_0x00d5:
                r2 = org.telegram.ui.ChannelRightsEditActivity.this;
                r2 = r2.adminRights;
                r3 = org.telegram.ui.ChannelRightsEditActivity.this;
                r3 = r3.bannedRights;
                r1.didSetRights(r0, r2, r3);
                goto L_0x0196;
            L_0x00e6:
                r1 = org.telegram.ui.ChannelRightsEditActivity.this;
                r1 = r1.currentType;
                if (r1 != r0) goto L_0x0196;
            L_0x00ee:
                r1 = org.telegram.ui.ChannelRightsEditActivity.this;
                r1 = r1.currentAccount;
                r3 = org.telegram.messenger.MessagesController.getInstance(r1);
                r1 = org.telegram.ui.ChannelRightsEditActivity.this;
                r4 = r1.chatId;
                r1 = org.telegram.ui.ChannelRightsEditActivity.this;
                r5 = r1.currentUser;
                r1 = org.telegram.ui.ChannelRightsEditActivity.this;
                r6 = r1.bannedRights;
                r1 = org.telegram.ui.ChannelRightsEditActivity.this;
                r7 = r1.isMegagroup;
                r1 = org.telegram.ui.ChannelRightsEditActivity.this;
                r8 = r1.getFragmentForAlert(r0);
                r3.setUserBannedRole(r4, r5, r6, r7, r8);
                r0 = org.telegram.ui.ChannelRightsEditActivity.this;
                r0 = r0.bannedRights;
                r0 = r0.view_messages;
                if (r0 == 0) goto L_0x0125;
            L_0x0123:
                r0 = 0;
                goto L_0x0178;
            L_0x0125:
                r0 = org.telegram.ui.ChannelRightsEditActivity.this;
                r0 = r0.bannedRights;
                r0 = r0.send_messages;
                if (r0 != 0) goto L_0x0176;
                r0 = org.telegram.ui.ChannelRightsEditActivity.this;
                r0 = r0.bannedRights;
                r0 = r0.send_stickers;
                if (r0 != 0) goto L_0x0176;
                r0 = org.telegram.ui.ChannelRightsEditActivity.this;
                r0 = r0.bannedRights;
                r0 = r0.embed_links;
                if (r0 != 0) goto L_0x0176;
                r0 = org.telegram.ui.ChannelRightsEditActivity.this;
                r0 = r0.bannedRights;
                r0 = r0.send_media;
                if (r0 != 0) goto L_0x0176;
                r0 = org.telegram.ui.ChannelRightsEditActivity.this;
                r0 = r0.bannedRights;
                r0 = r0.send_gifs;
                if (r0 != 0) goto L_0x0176;
                r0 = org.telegram.ui.ChannelRightsEditActivity.this;
                r0 = r0.bannedRights;
                r0 = r0.send_games;
                if (r0 != 0) goto L_0x0176;
                r0 = org.telegram.ui.ChannelRightsEditActivity.this;
                r0 = r0.bannedRights;
                r0 = r0.send_inline;
                if (r0 == 0) goto L_0x016c;
                goto L_0x0176;
                r0 = org.telegram.ui.ChannelRightsEditActivity.this;
                r0 = r0.bannedRights;
                r0.until_date = r2;
                r0 = 2;
                goto L_0x0178;
                r0 = 1;
                goto L_0x0124;
                r1 = org.telegram.ui.ChannelRightsEditActivity.this;
                r1 = r1.delegate;
                if (r1 == 0) goto L_0x0196;
                r1 = org.telegram.ui.ChannelRightsEditActivity.this;
                r1 = r1.delegate;
                r2 = org.telegram.ui.ChannelRightsEditActivity.this;
                r2 = r2.adminRights;
                r3 = org.telegram.ui.ChannelRightsEditActivity.this;
                r3 = r3.bannedRights;
                r1.didSetRights(r0, r2, r3);
            L_0x0196:
                r0 = org.telegram.ui.ChannelRightsEditActivity.this;
                r0.finishFragment();
            L_0x019b:
                return;
                */
                throw new UnsupportedOperationException("Method not decompiled: org.telegram.ui.ChannelRightsEditActivity.1.onItemClick(int):void");
            }
        });
        if (this.canEdit) {
            this.actionBar.createMenu().addItemWithWidth(1, R.drawable.ic_done, AndroidUtilities.dp(56.0f));
        }
        this.fragmentView = new FrameLayout(context);
        this.fragmentView.setBackgroundColor(Theme.getColor(Theme.key_windowBackgroundGray));
        FrameLayout frameLayout = this.fragmentView;
        this.listView = new RecyclerListView(context);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context, 1, false) {
            public boolean supportsPredictiveItemAnimations() {
                return false;
            }
        };
        this.listView.setItemAnimator(null);
        this.listView.setLayoutAnimation(null);
        this.listView.setLayoutManager(linearLayoutManager);
        RecyclerListView recyclerListView = this.listView;
        Adapter listAdapter = new ListAdapter(context);
        this.listViewAdapter = listAdapter;
        recyclerListView.setAdapter(listAdapter);
        recyclerListView = this.listView;
        if (!LocaleController.isRTL) {
            i = 2;
        }
        recyclerListView.setVerticalScrollbarPosition(i);
        frameLayout.addView(this.listView, LayoutHelper.createFrame(-1, -1.0f));
        this.listView.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(View view, int position) {
                Throwable e;
                View view2 = view;
                int i = position;
                if (ChannelRightsEditActivity.this.canEdit) {
                    if (i == 0) {
                        Bundle args = new Bundle();
                        args.putInt("user_id", ChannelRightsEditActivity.this.currentUser.id);
                        ChannelRightsEditActivity.this.presentFragment(new ProfileActivity(args));
                    } else if (i == ChannelRightsEditActivity.this.removeAdminRow) {
                        if (ChannelRightsEditActivity.this.currentType == 0) {
                            MessagesController.getInstance(ChannelRightsEditActivity.this.currentAccount).setUserAdminRole(ChannelRightsEditActivity.this.chatId, ChannelRightsEditActivity.this.currentUser, new TL_channelAdminRights(), ChannelRightsEditActivity.this.isMegagroup, ChannelRightsEditActivity.this.getFragmentForAlert(0));
                        } else if (ChannelRightsEditActivity.this.currentType == 1) {
                            ChannelRightsEditActivity.this.bannedRights = new TL_channelBannedRights();
                            ChannelRightsEditActivity.this.bannedRights.view_messages = true;
                            ChannelRightsEditActivity.this.bannedRights.send_media = true;
                            ChannelRightsEditActivity.this.bannedRights.send_messages = true;
                            ChannelRightsEditActivity.this.bannedRights.send_stickers = true;
                            ChannelRightsEditActivity.this.bannedRights.send_gifs = true;
                            ChannelRightsEditActivity.this.bannedRights.send_games = true;
                            ChannelRightsEditActivity.this.bannedRights.send_inline = true;
                            ChannelRightsEditActivity.this.bannedRights.embed_links = true;
                            ChannelRightsEditActivity.this.bannedRights.until_date = 0;
                            MessagesController.getInstance(ChannelRightsEditActivity.this.currentAccount).setUserBannedRole(ChannelRightsEditActivity.this.chatId, ChannelRightsEditActivity.this.currentUser, ChannelRightsEditActivity.this.bannedRights, ChannelRightsEditActivity.this.isMegagroup, ChannelRightsEditActivity.this.getFragmentForAlert(0));
                        }
                        if (ChannelRightsEditActivity.this.delegate != null) {
                            ChannelRightsEditActivity.this.delegate.didSetRights(0, ChannelRightsEditActivity.this.adminRights, ChannelRightsEditActivity.this.bannedRights);
                        }
                        ChannelRightsEditActivity.this.finishFragment();
                    } else if (i == ChannelRightsEditActivity.this.untilDateRow) {
                        if (ChannelRightsEditActivity.this.getParentActivity() != null) {
                            Calendar calendar = Calendar.getInstance();
                            int year = calendar.get(1);
                            int monthOfYear = calendar.get(2);
                            int dayOfMonth = calendar.get(5);
                            try {
                                DatePickerDialog dialog = new DatePickerDialog(ChannelRightsEditActivity.this.getParentActivity(), new OnDateSetListener() {
                                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                        Calendar calendar = Calendar.getInstance();
                                        calendar.clear();
                                        calendar.set(year, month, dayOfMonth);
                                        final int time = (int) (calendar.getTime().getTime() / 1000);
                                        try {
                                            TimePickerDialog dialog = new TimePickerDialog(ChannelRightsEditActivity.this.getParentActivity(), new OnTimeSetListener() {
                                                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                                    ChannelRightsEditActivity.this.bannedRights.until_date = (time + (hourOfDay * 3600)) + (minute * 60);
                                                    ChannelRightsEditActivity.this.listViewAdapter.notifyItemChanged(ChannelRightsEditActivity.this.untilDateRow);
                                                }
                                            }, 0, 0, true);
                                            dialog.setButton(-1, LocaleController.getString("Set", R.string.Set), dialog);
                                            dialog.setButton(-2, LocaleController.getString("Cancel", R.string.Cancel), new OnClickListener() {
                                                public void onClick(DialogInterface dialog, int which) {
                                                }
                                            });
                                            ChannelRightsEditActivity.this.showDialog(dialog);
                                        } catch (Throwable e) {
                                            FileLog.e(e);
                                        }
                                    }
                                }, year, monthOfYear, dayOfMonth);
                                final DatePicker datePicker = dialog.getDatePicker();
                                Calendar date = Calendar.getInstance();
                                date.setTimeInMillis(System.currentTimeMillis());
                                date.set(11, date.getMinimum(11));
                                date.set(12, date.getMinimum(12));
                                date.set(13, date.getMinimum(13));
                                date.set(14, date.getMinimum(14));
                                datePicker.setMinDate(date.getTimeInMillis());
                                try {
                                    date.setTimeInMillis(System.currentTimeMillis() + 31536000000L);
                                    date.set(11, date.getMaximum(11));
                                    date.set(12, date.getMaximum(12));
                                    date.set(13, date.getMaximum(13));
                                    date.set(14, date.getMaximum(14));
                                    datePicker.setMaxDate(date.getTimeInMillis());
                                    dialog.setButton(-1, LocaleController.getString("Set", R.string.Set), dialog);
                                    dialog.setButton(-3, LocaleController.getString("UserRestrictionsUntilForever", R.string.UserRestrictionsUntilForever), new OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            ChannelRightsEditActivity.this.bannedRights.until_date = 0;
                                            ChannelRightsEditActivity.this.listViewAdapter.notifyItemChanged(ChannelRightsEditActivity.this.untilDateRow);
                                        }
                                    });
                                    dialog.setButton(-2, LocaleController.getString("Cancel", R.string.Cancel), new OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                        }
                                    });
                                    if (VERSION.SDK_INT >= 21) {
                                        dialog.setOnShowListener(new OnShowListener() {
                                            public void onShow(DialogInterface dialog) {
                                                int count = datePicker.getChildCount();
                                                for (int a = 0; a < count; a++) {
                                                    View child = datePicker.getChildAt(a);
                                                    LayoutParams layoutParams = child.getLayoutParams();
                                                    layoutParams.width = -1;
                                                    child.setLayoutParams(layoutParams);
                                                }
                                            }
                                        });
                                    }
                                    ChannelRightsEditActivity.this.showDialog(dialog);
                                } catch (Throwable e2) {
                                    e = e2;
                                    FileLog.e(e);
                                }
                            } catch (Throwable e22) {
                                int i2 = dayOfMonth;
                                e = e22;
                                FileLog.e(e);
                            }
                        }
                    } else if (view2 instanceof TextCheckCell2) {
                        TextCheckCell2 checkCell = (TextCheckCell2) view2;
                        if (checkCell.isEnabled()) {
                            checkCell.setChecked(checkCell.isChecked() ^ true);
                            if (i == ChannelRightsEditActivity.this.changeInfoRow) {
                                ChannelRightsEditActivity.this.adminRights.change_info = true ^ ChannelRightsEditActivity.this.adminRights.change_info;
                            } else if (i == ChannelRightsEditActivity.this.postMessagesRow) {
                                ChannelRightsEditActivity.this.adminRights.post_messages = true ^ ChannelRightsEditActivity.this.adminRights.post_messages;
                            } else if (i == ChannelRightsEditActivity.this.editMesagesRow) {
                                ChannelRightsEditActivity.this.adminRights.edit_messages = true ^ ChannelRightsEditActivity.this.adminRights.edit_messages;
                            } else if (i == ChannelRightsEditActivity.this.deleteMessagesRow) {
                                ChannelRightsEditActivity.this.adminRights.delete_messages = true ^ ChannelRightsEditActivity.this.adminRights.delete_messages;
                            } else if (i == ChannelRightsEditActivity.this.addAdminsRow) {
                                ChannelRightsEditActivity.this.adminRights.add_admins = true ^ ChannelRightsEditActivity.this.adminRights.add_admins;
                            } else if (i == ChannelRightsEditActivity.this.banUsersRow) {
                                ChannelRightsEditActivity.this.adminRights.ban_users = true ^ ChannelRightsEditActivity.this.adminRights.ban_users;
                            } else if (i == ChannelRightsEditActivity.this.addUsersRow) {
                                TL_channelAdminRights access$200 = ChannelRightsEditActivity.this.adminRights;
                                boolean z = true ^ ChannelRightsEditActivity.this.adminRights.invite_users;
                                ChannelRightsEditActivity.this.adminRights.invite_link = z;
                                access$200.invite_users = z;
                            } else if (i == ChannelRightsEditActivity.this.pinMessagesRow) {
                                ChannelRightsEditActivity.this.adminRights.pin_messages = true ^ ChannelRightsEditActivity.this.adminRights.pin_messages;
                            } else if (ChannelRightsEditActivity.this.bannedRights != null) {
                                TL_channelBannedRights access$700;
                                TL_channelBannedRights access$7002;
                                TL_channelBannedRights access$7003;
                                boolean disabled = checkCell.isChecked() ^ true;
                                if (i == ChannelRightsEditActivity.this.viewMessagesRow) {
                                    ChannelRightsEditActivity.this.bannedRights.view_messages ^= true;
                                } else if (i == ChannelRightsEditActivity.this.sendMessagesRow) {
                                    ChannelRightsEditActivity.this.bannedRights.send_messages ^= true;
                                } else if (i == ChannelRightsEditActivity.this.sendMediaRow) {
                                    ChannelRightsEditActivity.this.bannedRights.send_media ^= true;
                                } else if (i == ChannelRightsEditActivity.this.sendStickersRow) {
                                    access$700 = ChannelRightsEditActivity.this.bannedRights;
                                    access$7002 = ChannelRightsEditActivity.this.bannedRights;
                                    access$7003 = ChannelRightsEditActivity.this.bannedRights;
                                    boolean z2 = ChannelRightsEditActivity.this.bannedRights.send_stickers ^ true;
                                    ChannelRightsEditActivity.this.bannedRights.send_inline = z2;
                                    access$7003.send_gifs = z2;
                                    access$7002.send_games = z2;
                                    access$700.send_stickers = z2;
                                } else if (i == ChannelRightsEditActivity.this.embedLinksRow) {
                                    ChannelRightsEditActivity.this.bannedRights.embed_links ^= true;
                                }
                                ViewHolder holder;
                                if (disabled) {
                                    if (ChannelRightsEditActivity.this.bannedRights.view_messages && !ChannelRightsEditActivity.this.bannedRights.send_messages) {
                                        ChannelRightsEditActivity.this.bannedRights.send_messages = true;
                                        holder = ChannelRightsEditActivity.this.listView.findViewHolderForAdapterPosition(ChannelRightsEditActivity.this.sendMessagesRow);
                                        if (holder != null) {
                                            ((TextCheckCell2) holder.itemView).setChecked(false);
                                        }
                                    }
                                    if ((ChannelRightsEditActivity.this.bannedRights.view_messages || ChannelRightsEditActivity.this.bannedRights.send_messages) && !ChannelRightsEditActivity.this.bannedRights.send_media) {
                                        ChannelRightsEditActivity.this.bannedRights.send_media = true;
                                        holder = ChannelRightsEditActivity.this.listView.findViewHolderForAdapterPosition(ChannelRightsEditActivity.this.sendMediaRow);
                                        if (holder != null) {
                                            ((TextCheckCell2) holder.itemView).setChecked(false);
                                        }
                                    }
                                    if ((ChannelRightsEditActivity.this.bannedRights.view_messages || ChannelRightsEditActivity.this.bannedRights.send_messages || ChannelRightsEditActivity.this.bannedRights.send_media) && !ChannelRightsEditActivity.this.bannedRights.send_stickers) {
                                        access$700 = ChannelRightsEditActivity.this.bannedRights;
                                        access$7002 = ChannelRightsEditActivity.this.bannedRights;
                                        access$7003 = ChannelRightsEditActivity.this.bannedRights;
                                        ChannelRightsEditActivity.this.bannedRights.send_inline = true;
                                        access$7003.send_gifs = true;
                                        access$7002.send_games = true;
                                        access$700.send_stickers = true;
                                        holder = ChannelRightsEditActivity.this.listView.findViewHolderForAdapterPosition(ChannelRightsEditActivity.this.sendStickersRow);
                                        if (holder != null) {
                                            ((TextCheckCell2) holder.itemView).setChecked(false);
                                        }
                                    }
                                    if ((ChannelRightsEditActivity.this.bannedRights.view_messages || ChannelRightsEditActivity.this.bannedRights.send_messages || ChannelRightsEditActivity.this.bannedRights.send_media) && !ChannelRightsEditActivity.this.bannedRights.embed_links) {
                                        ChannelRightsEditActivity.this.bannedRights.embed_links = true;
                                        ViewHolder holder2 = ChannelRightsEditActivity.this.listView.findViewHolderForAdapterPosition(ChannelRightsEditActivity.this.embedLinksRow);
                                        if (holder2 != null) {
                                            ((TextCheckCell2) holder2.itemView).setChecked(false);
                                        }
                                    }
                                } else {
                                    if (!(ChannelRightsEditActivity.this.bannedRights.send_messages && ChannelRightsEditActivity.this.bannedRights.embed_links && ChannelRightsEditActivity.this.bannedRights.send_inline && ChannelRightsEditActivity.this.bannedRights.send_media) && ChannelRightsEditActivity.this.bannedRights.view_messages) {
                                        ChannelRightsEditActivity.this.bannedRights.view_messages = false;
                                        holder = ChannelRightsEditActivity.this.listView.findViewHolderForAdapterPosition(ChannelRightsEditActivity.this.viewMessagesRow);
                                        if (holder != null) {
                                            ((TextCheckCell2) holder.itemView).setChecked(true);
                                        }
                                    }
                                    if (!(ChannelRightsEditActivity.this.bannedRights.embed_links && ChannelRightsEditActivity.this.bannedRights.send_inline && ChannelRightsEditActivity.this.bannedRights.send_media) && ChannelRightsEditActivity.this.bannedRights.send_messages) {
                                        ChannelRightsEditActivity.this.bannedRights.send_messages = false;
                                        holder = ChannelRightsEditActivity.this.listView.findViewHolderForAdapterPosition(ChannelRightsEditActivity.this.sendMessagesRow);
                                        if (holder != null) {
                                            ((TextCheckCell2) holder.itemView).setChecked(true);
                                        }
                                    }
                                    if (!(ChannelRightsEditActivity.this.bannedRights.send_inline && ChannelRightsEditActivity.this.bannedRights.embed_links) && ChannelRightsEditActivity.this.bannedRights.send_media) {
                                        ChannelRightsEditActivity.this.bannedRights.send_media = false;
                                        ViewHolder holder3 = ChannelRightsEditActivity.this.listView.findViewHolderForAdapterPosition(ChannelRightsEditActivity.this.sendMediaRow);
                                        if (holder3 != null) {
                                            ((TextCheckCell2) holder3.itemView).setChecked(true);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        });
        return this.fragmentView;
    }

    public void onResume() {
        super.onResume();
        if (this.listViewAdapter != null) {
            this.listViewAdapter.notifyDataSetChanged();
        }
    }

    public void setDelegate(ChannelRightsEditActivityDelegate channelRightsEditActivityDelegate) {
        this.delegate = channelRightsEditActivityDelegate;
    }

    public ThemeDescription[] getThemeDescriptions() {
        ThemeDescriptionDelegate сellDelegate = new ThemeDescriptionDelegate() {
            public void didSetColor() {
                if (ChannelRightsEditActivity.this.listView != null) {
                    int count = ChannelRightsEditActivity.this.listView.getChildCount();
                    for (int a = 0; a < count; a++) {
                        View child = ChannelRightsEditActivity.this.listView.getChildAt(a);
                        if (child instanceof UserCell) {
                            ((UserCell) child).update(0);
                        }
                    }
                }
            }
        };
        ThemeDescription[] themeDescriptionArr = new ThemeDescription[34];
        themeDescriptionArr[0] = new ThemeDescription(this.listView, ThemeDescription.FLAG_CELLBACKGROUNDCOLOR, new Class[]{UserCell.class, TextSettingsCell.class, TextCheckCell2.class, HeaderCell.class}, null, null, null, Theme.key_windowBackgroundWhite);
        themeDescriptionArr[1] = new ThemeDescription(this.fragmentView, ThemeDescription.FLAG_BACKGROUND, null, null, null, null, Theme.key_windowBackgroundGray);
        themeDescriptionArr[2] = new ThemeDescription(this.actionBar, ThemeDescription.FLAG_BACKGROUND, null, null, null, null, Theme.key_actionBarDefault);
        themeDescriptionArr[3] = new ThemeDescription(this.listView, ThemeDescription.FLAG_LISTGLOWCOLOR, null, null, null, null, Theme.key_actionBarDefault);
        themeDescriptionArr[4] = new ThemeDescription(this.actionBar, ThemeDescription.FLAG_AB_ITEMSCOLOR, null, null, null, null, Theme.key_actionBarDefaultIcon);
        themeDescriptionArr[5] = new ThemeDescription(this.actionBar, ThemeDescription.FLAG_AB_TITLECOLOR, null, null, null, null, Theme.key_actionBarDefaultTitle);
        themeDescriptionArr[6] = new ThemeDescription(this.actionBar, ThemeDescription.FLAG_AB_SELECTORCOLOR, null, null, null, null, Theme.key_actionBarDefaultSelector);
        themeDescriptionArr[7] = new ThemeDescription(this.listView, ThemeDescription.FLAG_SELECTOR, null, null, null, null, Theme.key_listSelector);
        themeDescriptionArr[8] = new ThemeDescription(this.listView, 0, new Class[]{View.class}, Theme.dividerPaint, null, null, Theme.key_divider);
        themeDescriptionArr[9] = new ThemeDescription(this.listView, ThemeDescription.FLAG_BACKGROUNDFILTER, new Class[]{TextInfoPrivacyCell.class}, null, null, null, Theme.key_windowBackgroundGrayShadow);
        themeDescriptionArr[10] = new ThemeDescription(this.listView, 0, new Class[]{TextInfoPrivacyCell.class}, new String[]{"textView"}, null, null, null, Theme.key_windowBackgroundWhiteGrayText4);
        View view = this.listView;
        View view2 = view;
        themeDescriptionArr[11] = new ThemeDescription(view2, ThemeDescription.FLAG_CHECKTAG, new Class[]{TextSettingsCell.class}, new String[]{"textView"}, null, null, null, Theme.key_windowBackgroundWhiteRedText3);
        view = this.listView;
        view2 = view;
        themeDescriptionArr[12] = new ThemeDescription(view2, ThemeDescription.FLAG_CHECKTAG, new Class[]{TextSettingsCell.class}, new String[]{"textView"}, null, null, null, Theme.key_windowBackgroundWhiteBlackText);
        themeDescriptionArr[13] = new ThemeDescription(this.listView, 0, new Class[]{TextSettingsCell.class}, new String[]{"valueTextView"}, null, null, null, Theme.key_windowBackgroundWhiteValueText);
        themeDescriptionArr[14] = new ThemeDescription(this.listView, 0, new Class[]{TextSettingsCell.class}, new String[]{"valueImageView"}, null, null, null, Theme.key_windowBackgroundWhiteGrayIcon);
        themeDescriptionArr[15] = new ThemeDescription(this.listView, 0, new Class[]{TextCheckCell2.class}, new String[]{"textView"}, null, null, null, Theme.key_windowBackgroundWhiteBlackText);
        themeDescriptionArr[16] = new ThemeDescription(this.listView, 0, new Class[]{TextCheckCell2.class}, new String[]{"valueTextView"}, null, null, null, Theme.key_windowBackgroundWhiteGrayText2);
        themeDescriptionArr[17] = new ThemeDescription(this.listView, 0, new Class[]{TextCheckCell2.class}, new String[]{"checkBox"}, null, null, null, Theme.key_switchThumb);
        themeDescriptionArr[18] = new ThemeDescription(this.listView, 0, new Class[]{TextCheckCell2.class}, new String[]{"checkBox"}, null, null, null, Theme.key_switchTrack);
        themeDescriptionArr[19] = new ThemeDescription(this.listView, 0, new Class[]{TextCheckCell2.class}, new String[]{"checkBox"}, null, null, null, Theme.key_switchThumbChecked);
        themeDescriptionArr[20] = new ThemeDescription(this.listView, 0, new Class[]{TextCheckCell2.class}, new String[]{"checkBox"}, null, null, null, Theme.key_switchTrackChecked);
        themeDescriptionArr[21] = new ThemeDescription(this.listView, ThemeDescription.FLAG_BACKGROUNDFILTER, new Class[]{ShadowSectionCell.class}, null, null, null, Theme.key_windowBackgroundGrayShadow);
        themeDescriptionArr[22] = new ThemeDescription(this.listView, 0, new Class[]{HeaderCell.class}, new String[]{"textView"}, null, null, null, Theme.key_windowBackgroundWhiteBlueHeader);
        themeDescriptionArr[23] = new ThemeDescription(this.listView, 0, new Class[]{UserCell.class}, new String[]{"nameTextView"}, null, null, null, Theme.key_windowBackgroundWhiteBlackText);
        int i = 3;
        int i2 = 1;
        themeDescriptionArr[24] = new ThemeDescription(this.listView, 0, new Class[]{UserCell.class}, new String[]{"statusColor"}, null, null, сellDelegate, Theme.key_windowBackgroundWhiteGrayText);
        view = this.listView;
        Class[] clsArr = new Class[i2];
        clsArr[0] = UserCell.class;
        String[] strArr = new String[i2];
        strArr[0] = "statusOnlineColor";
        themeDescriptionArr[25] = new ThemeDescription(view, 0, clsArr, strArr, null, null, сellDelegate, Theme.key_windowBackgroundWhiteBlueText);
        view = this.listView;
        clsArr = new Class[i2];
        clsArr[0] = UserCell.class;
        Drawable[] drawableArr = new Drawable[i];
        drawableArr[0] = Theme.avatar_photoDrawable;
        drawableArr[i2] = Theme.avatar_broadcastDrawable;
        drawableArr[2] = Theme.avatar_savedDrawable;
        themeDescriptionArr[26] = new ThemeDescription(view, 0, clsArr, null, drawableArr, null, Theme.key_avatar_text);
        ThemeDescriptionDelegate themeDescriptionDelegate = сellDelegate;
        themeDescriptionArr[27] = new ThemeDescription(null, 0, null, null, null, themeDescriptionDelegate, Theme.key_avatar_backgroundRed);
        themeDescriptionArr[28] = new ThemeDescription(null, 0, null, null, null, themeDescriptionDelegate, Theme.key_avatar_backgroundOrange);
        themeDescriptionArr[29] = new ThemeDescription(null, 0, null, null, null, themeDescriptionDelegate, Theme.key_avatar_backgroundViolet);
        themeDescriptionArr[30] = new ThemeDescription(null, 0, null, null, null, themeDescriptionDelegate, Theme.key_avatar_backgroundGreen);
        themeDescriptionArr[31] = new ThemeDescription(null, 0, null, null, null, themeDescriptionDelegate, Theme.key_avatar_backgroundCyan);
        themeDescriptionArr[32] = new ThemeDescription(null, 0, null, null, null, themeDescriptionDelegate, Theme.key_avatar_backgroundBlue);
        themeDescriptionArr[33] = new ThemeDescription(null, 0, null, null, null, themeDescriptionDelegate, Theme.key_avatar_backgroundPink);
        return themeDescriptionArr;
    }
}
