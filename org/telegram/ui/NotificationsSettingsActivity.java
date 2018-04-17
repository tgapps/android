package org.telegram.ui;

import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build.VERSION;
import android.provider.Settings.System;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.Toast;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.ApplicationLoader;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.NotificationCenter;
import org.telegram.messenger.NotificationCenter.NotificationCenterDelegate;
import org.telegram.messenger.NotificationsController;
import org.telegram.messenger.beta.R;
import org.telegram.messenger.exoplayer2.extractor.ts.PsExtractor;
import org.telegram.messenger.support.widget.LinearLayoutManager;
import org.telegram.messenger.support.widget.RecyclerView.Adapter;
import org.telegram.messenger.support.widget.RecyclerView.ViewHolder;
import org.telegram.tgnet.ConnectionsManager;
import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC.TL_account_resetNotifySettings;
import org.telegram.tgnet.TLRPC.TL_error;
import org.telegram.ui.ActionBar.ActionBar.ActionBarMenuOnItemClick;
import org.telegram.ui.ActionBar.AlertDialog.Builder;
import org.telegram.ui.ActionBar.BaseFragment;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.ActionBar.ThemeDescription;
import org.telegram.ui.Cells.HeaderCell;
import org.telegram.ui.Cells.ShadowSectionCell;
import org.telegram.ui.Cells.TextCheckCell;
import org.telegram.ui.Cells.TextColorCell;
import org.telegram.ui.Cells.TextDetailSettingsCell;
import org.telegram.ui.Cells.TextSettingsCell;
import org.telegram.ui.Components.AlertsCreator;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.ui.Components.RecyclerListView;
import org.telegram.ui.Components.RecyclerListView.Holder;
import org.telegram.ui.Components.RecyclerListView.OnItemClickListener;
import org.telegram.ui.Components.RecyclerListView.SelectionAdapter;

public class NotificationsSettingsActivity extends BaseFragment implements NotificationCenterDelegate {
    private ListAdapter adapter;
    private int androidAutoAlertRow;
    private int badgeNumberRow;
    private int callsRingtoneRow;
    private int callsSectionRow;
    private int callsSectionRow2;
    private int callsVibrateRow;
    private int contactJoinedRow;
    private int eventsSectionRow;
    private int eventsSectionRow2;
    private int groupAlertRow;
    private int groupLedRow;
    private int groupPopupNotificationRow;
    private int groupPreviewRow;
    private int groupPriorityRow;
    private int groupSectionRow;
    private int groupSectionRow2;
    private int groupSoundRow;
    private int groupVibrateRow;
    private int inappPreviewRow;
    private int inappPriorityRow;
    private int inappSectionRow;
    private int inappSectionRow2;
    private int inappSoundRow;
    private int inappVibrateRow;
    private int inchatSoundRow;
    private RecyclerListView listView;
    private int messageAlertRow;
    private int messageLedRow;
    private int messagePopupNotificationRow;
    private int messagePreviewRow;
    private int messagePriorityRow;
    private int messageSectionRow;
    private int messageSoundRow;
    private int messageVibrateRow;
    private int notificationsServiceConnectionRow;
    private int notificationsServiceRow;
    private int otherSectionRow;
    private int otherSectionRow2;
    private int pinnedMessageRow;
    private int repeatRow;
    private int resetNotificationsRow;
    private int resetSectionRow;
    private int resetSectionRow2;
    private boolean reseting = false;
    private int rowCount = 0;

    private class ListAdapter extends SelectionAdapter {
        private Context mContext;

        public void onBindViewHolder(org.telegram.messenger.support.widget.RecyclerView.ViewHolder r1, int r2) {
            /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.DecodeException: Load method exception in method: org.telegram.ui.NotificationsSettingsActivity.ListAdapter.onBindViewHolder(org.telegram.messenger.support.widget.RecyclerView$ViewHolder, int):void
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
            r0 = r13.getItemViewType();
            r1 = 5;
            r2 = 2131494009; // 0x7f0c0479 float:1.8611514E38 double:1.053097964E-314;
            r3 = 2131494574; // 0x7f0c06ae float:1.861266E38 double:1.0530982433E-314;
            r4 = 0;
            r5 = 1;
            if (r0 == r1) goto L_0x02f1;
        L_0x000f:
            switch(r0) {
                case 0: goto L_0x0253;
                case 1: goto L_0x007a;
                case 2: goto L_0x005c;
                case 3: goto L_0x0014;
                default: goto L_0x0012;
            };
        L_0x0012:
            goto L_0x0570;
        L_0x0014:
            r0 = r13.itemView;
            r0 = (org.telegram.ui.Cells.TextColorCell) r0;
            r1 = org.telegram.ui.NotificationsSettingsActivity.this;
            r1 = r1.currentAccount;
            r1 = org.telegram.messenger.MessagesController.getNotificationsSettings(r1);
            r2 = org.telegram.ui.NotificationsSettingsActivity.this;
            r2 = r2.messageLedRow;
            r3 = -16776961; // 0xffffffffff0000ff float:-1.7014636E38 double:NaN;
            if (r14 != r2) goto L_0x0034;
        L_0x002d:
            r2 = "MessagesLed";
            r2 = r1.getInt(r2, r3);
            goto L_0x003a;
        L_0x0034:
            r2 = "GroupLed";
            r2 = r1.getInt(r2, r3);
            r3 = r4;
            r4 = 9;
            if (r3 >= r4) goto L_0x004e;
            r4 = org.telegram.ui.Cells.TextColorCell.colorsToSave;
            r4 = r4[r3];
            if (r4 != r2) goto L_0x004b;
            r4 = org.telegram.ui.Cells.TextColorCell.colors;
            r2 = r4[r3];
            goto L_0x004e;
            r4 = r3 + 1;
            goto L_0x003b;
            r3 = "LedColor";
            r4 = 2131493744; // 0x7f0c0370 float:1.8610977E38 double:1.053097833E-314;
            r3 = org.telegram.messenger.LocaleController.getString(r3, r4);
            r0.setTextAndColor(r3, r2, r5);
            goto L_0x0570;
        L_0x005c:
            r0 = r13.itemView;
            r0 = (org.telegram.ui.Cells.TextDetailSettingsCell) r0;
            r0.setMultilineDetail(r5);
            r1 = "ResetAllNotifications";
            r2 = 2131494256; // 0x7f0c0570 float:1.8612015E38 double:1.053098086E-314;
            r1 = org.telegram.messenger.LocaleController.getString(r1, r2);
            r2 = "UndoAllCustom";
            r3 = 2131494508; // 0x7f0c066c float:1.8612526E38 double:1.0530982107E-314;
            r2 = org.telegram.messenger.LocaleController.getString(r2, r3);
            r0.setTextAndValue(r1, r2, r4);
            goto L_0x0570;
        L_0x007a:
            r0 = r13.itemView;
            r0 = (org.telegram.ui.Cells.TextCheckCell) r0;
            r1 = org.telegram.ui.NotificationsSettingsActivity.this;
            r1 = r1.currentAccount;
            r1 = org.telegram.messenger.MessagesController.getNotificationsSettings(r1);
            r6 = org.telegram.ui.NotificationsSettingsActivity.this;
            r6 = r6.messageAlertRow;
            r7 = 2131492948; // 0x7f0c0054 float:1.8609362E38 double:1.05309744E-314;
            if (r14 != r6) goto L_0x00a4;
            r2 = "Alert";
            r2 = org.telegram.messenger.LocaleController.getString(r2, r7);
            r3 = "EnableAll";
            r3 = r1.getBoolean(r3, r5);
            r0.setTextAndCheck(r2, r3, r5);
            goto L_0x0570;
            r6 = org.telegram.ui.NotificationsSettingsActivity.this;
            r6 = r6.groupAlertRow;
            if (r14 != r6) goto L_0x00bd;
            r2 = "Alert";
            r2 = org.telegram.messenger.LocaleController.getString(r2, r7);
            r3 = "EnableGroup";
            r3 = r1.getBoolean(r3, r5);
            r0.setTextAndCheck(r2, r3, r5);
            goto L_0x0570;
            r6 = org.telegram.ui.NotificationsSettingsActivity.this;
            r6 = r6.messagePreviewRow;
            r7 = 2131493826; // 0x7f0c03c2 float:1.8611143E38 double:1.0530978737E-314;
            if (r14 != r6) goto L_0x00d9;
            r2 = "MessagePreview";
            r2 = org.telegram.messenger.LocaleController.getString(r2, r7);
            r3 = "EnablePreviewAll";
            r3 = r1.getBoolean(r3, r5);
            r0.setTextAndCheck(r2, r3, r5);
            goto L_0x0570;
            r6 = org.telegram.ui.NotificationsSettingsActivity.this;
            r6 = r6.groupPreviewRow;
            if (r14 != r6) goto L_0x00f2;
            r2 = "MessagePreview";
            r2 = org.telegram.messenger.LocaleController.getString(r2, r7);
            r3 = "EnablePreviewGroup";
            r3 = r1.getBoolean(r3, r5);
            r0.setTextAndCheck(r2, r3, r5);
            goto L_0x0570;
            r6 = org.telegram.ui.NotificationsSettingsActivity.this;
            r6 = r6.inappSoundRow;
            if (r14 != r6) goto L_0x010e;
            r2 = "InAppSounds";
            r3 = 2131493669; // 0x7f0c0325 float:1.8610825E38 double:1.053097796E-314;
            r2 = org.telegram.messenger.LocaleController.getString(r2, r3);
            r3 = "EnableInAppSounds";
            r3 = r1.getBoolean(r3, r5);
            r0.setTextAndCheck(r2, r3, r5);
            goto L_0x0570;
            r6 = org.telegram.ui.NotificationsSettingsActivity.this;
            r6 = r6.inappVibrateRow;
            if (r14 != r6) goto L_0x012a;
            r2 = "InAppVibrate";
            r3 = 2131493670; // 0x7f0c0326 float:1.8610827E38 double:1.0530977967E-314;
            r2 = org.telegram.messenger.LocaleController.getString(r2, r3);
            r3 = "EnableInAppVibrate";
            r3 = r1.getBoolean(r3, r5);
            r0.setTextAndCheck(r2, r3, r5);
            goto L_0x0570;
            r6 = org.telegram.ui.NotificationsSettingsActivity.this;
            r6 = r6.inappPreviewRow;
            if (r14 != r6) goto L_0x0146;
            r2 = "InAppPreview";
            r3 = 2131493668; // 0x7f0c0324 float:1.8610823E38 double:1.0530977957E-314;
            r2 = org.telegram.messenger.LocaleController.getString(r2, r3);
            r3 = "EnableInAppPreview";
            r3 = r1.getBoolean(r3, r5);
            r0.setTextAndCheck(r2, r3, r5);
            goto L_0x0570;
            r6 = org.telegram.ui.NotificationsSettingsActivity.this;
            r6 = r6.inappPriorityRow;
            if (r14 != r6) goto L_0x015f;
            r3 = "NotificationsImportance";
            r2 = org.telegram.messenger.LocaleController.getString(r3, r2);
            r3 = "EnableInAppPriority";
            r3 = r1.getBoolean(r3, r4);
            r0.setTextAndCheck(r2, r3, r4);
            goto L_0x0570;
            r2 = org.telegram.ui.NotificationsSettingsActivity.this;
            r2 = r2.contactJoinedRow;
            if (r14 != r2) goto L_0x017b;
            r2 = "ContactJoined";
            r3 = 2131493288; // 0x7f0c01a8 float:1.8610052E38 double:1.053097608E-314;
            r2 = org.telegram.messenger.LocaleController.getString(r2, r3);
            r3 = "EnableContactJoined";
            r3 = r1.getBoolean(r3, r5);
            r0.setTextAndCheck(r2, r3, r5);
            goto L_0x0570;
            r2 = org.telegram.ui.NotificationsSettingsActivity.this;
            r2 = r2.pinnedMessageRow;
            if (r14 != r2) goto L_0x0197;
            r2 = "PinnedMessages";
            r3 = 2131494172; // 0x7f0c051c float:1.8611845E38 double:1.0530980447E-314;
            r2 = org.telegram.messenger.LocaleController.getString(r2, r3);
            r3 = "PinnedMessages";
            r3 = r1.getBoolean(r3, r5);
            r0.setTextAndCheck(r2, r3, r4);
            goto L_0x0570;
            r2 = org.telegram.ui.NotificationsSettingsActivity.this;
            r2 = r2.androidAutoAlertRow;
            if (r14 != r2) goto L_0x01ac;
            r2 = "Android Auto";
            r3 = "EnableAutoNotifications";
            r3 = r1.getBoolean(r3, r4);
            r0.setTextAndCheck(r2, r3, r5);
            goto L_0x0570;
            r2 = org.telegram.ui.NotificationsSettingsActivity.this;
            r2 = r2.notificationsServiceRow;
            if (r14 != r2) goto L_0x01d4;
            r2 = "NotificationsService";
            r3 = 2131494021; // 0x7f0c0485 float:1.8611539E38 double:1.05309797E-314;
            r7 = org.telegram.messenger.LocaleController.getString(r2, r3);
            r2 = "NotificationsServiceInfo";
            r3 = 2131494024; // 0x7f0c0488 float:1.8611545E38 double:1.0530979716E-314;
            r8 = org.telegram.messenger.LocaleController.getString(r2, r3);
            r2 = "pushService";
            r9 = r1.getBoolean(r2, r5);
            r10 = 1;
            r11 = 1;
            r6 = r0;
            r6.setTextAndValueAndCheck(r7, r8, r9, r10, r11);
            goto L_0x0570;
            r2 = org.telegram.ui.NotificationsSettingsActivity.this;
            r2 = r2.notificationsServiceConnectionRow;
            if (r14 != r2) goto L_0x01fc;
            r2 = "NotificationsServiceConnection";
            r3 = 2131494022; // 0x7f0c0486 float:1.861154E38 double:1.0530979706E-314;
            r7 = org.telegram.messenger.LocaleController.getString(r2, r3);
            r2 = "NotificationsServiceConnectionInfo";
            r3 = 2131494023; // 0x7f0c0487 float:1.8611543E38 double:1.053097971E-314;
            r8 = org.telegram.messenger.LocaleController.getString(r2, r3);
            r2 = "pushConnection";
            r9 = r1.getBoolean(r2, r5);
            r10 = 1;
            r11 = 1;
            r6 = r0;
            r6.setTextAndValueAndCheck(r7, r8, r9, r10, r11);
            goto L_0x0570;
            r2 = org.telegram.ui.NotificationsSettingsActivity.this;
            r2 = r2.badgeNumberRow;
            if (r14 != r2) goto L_0x021e;
            r2 = "BadgeNumber";
            r3 = 2131493077; // 0x7f0c00d5 float:1.8609624E38 double:1.0530975037E-314;
            r2 = org.telegram.messenger.LocaleController.getString(r2, r3);
            r3 = org.telegram.ui.NotificationsSettingsActivity.this;
            r3 = r3.currentAccount;
            r3 = org.telegram.messenger.NotificationsController.getInstance(r3);
            r3 = r3.showBadgeNumber;
            r0.setTextAndCheck(r2, r3, r5);
            goto L_0x0570;
            r2 = org.telegram.ui.NotificationsSettingsActivity.this;
            r2 = r2.inchatSoundRow;
            if (r14 != r2) goto L_0x023a;
            r2 = "InChatSound";
            r3 = 2131493671; // 0x7f0c0327 float:1.8610829E38 double:1.053097797E-314;
            r2 = org.telegram.messenger.LocaleController.getString(r2, r3);
            r3 = "EnableInChatSound";
            r3 = r1.getBoolean(r3, r5);
            r0.setTextAndCheck(r2, r3, r5);
            goto L_0x0570;
            r2 = org.telegram.ui.NotificationsSettingsActivity.this;
            r2 = r2.callsVibrateRow;
            if (r14 != r2) goto L_0x0570;
            r2 = "Vibrate";
            r2 = org.telegram.messenger.LocaleController.getString(r2, r3);
            r3 = "EnableCallVibrate";
            r3 = r1.getBoolean(r3, r5);
            r0.setTextAndCheck(r2, r3, r5);
            goto L_0x0570;
        L_0x0253:
            r0 = r13.itemView;
            r0 = (org.telegram.ui.Cells.HeaderCell) r0;
            r1 = org.telegram.ui.NotificationsSettingsActivity.this;
            r1 = r1.messageSectionRow;
            if (r14 != r1) goto L_0x026d;
            r1 = "MessageNotifications";
            r2 = 2131493825; // 0x7f0c03c1 float:1.8611141E38 double:1.0530978733E-314;
            r1 = org.telegram.messenger.LocaleController.getString(r1, r2);
            r0.setText(r1);
            goto L_0x0570;
            r1 = org.telegram.ui.NotificationsSettingsActivity.this;
            r1 = r1.groupSectionRow;
            if (r14 != r1) goto L_0x0283;
            r1 = "GroupNotifications";
            r2 = 2131493634; // 0x7f0c0302 float:1.8610754E38 double:1.053097779E-314;
            r1 = org.telegram.messenger.LocaleController.getString(r1, r2);
            r0.setText(r1);
            goto L_0x0570;
            r1 = org.telegram.ui.NotificationsSettingsActivity.this;
            r1 = r1.inappSectionRow;
            if (r14 != r1) goto L_0x0299;
            r1 = "InAppNotifications";
            r2 = 2131493667; // 0x7f0c0323 float:1.861082E38 double:1.053097795E-314;
            r1 = org.telegram.messenger.LocaleController.getString(r1, r2);
            r0.setText(r1);
            goto L_0x0570;
            r1 = org.telegram.ui.NotificationsSettingsActivity.this;
            r1 = r1.eventsSectionRow;
            if (r14 != r1) goto L_0x02af;
            r1 = "Events";
            r2 = 2131493530; // 0x7f0c029a float:1.8610543E38 double:1.0530977275E-314;
            r1 = org.telegram.messenger.LocaleController.getString(r1, r2);
            r0.setText(r1);
            goto L_0x0570;
            r1 = org.telegram.ui.NotificationsSettingsActivity.this;
            r1 = r1.otherSectionRow;
            if (r14 != r1) goto L_0x02c5;
            r1 = "NotificationsOther";
            r2 = 2131494015; // 0x7f0c047f float:1.8611526E38 double:1.053097967E-314;
            r1 = org.telegram.messenger.LocaleController.getString(r1, r2);
            r0.setText(r1);
            goto L_0x0570;
            r1 = org.telegram.ui.NotificationsSettingsActivity.this;
            r1 = r1.resetSectionRow;
            if (r14 != r1) goto L_0x02db;
            r1 = "Reset";
            r2 = 2131494250; // 0x7f0c056a float:1.8612003E38 double:1.053098083E-314;
            r1 = org.telegram.messenger.LocaleController.getString(r1, r2);
            r0.setText(r1);
            goto L_0x0570;
            r1 = org.telegram.ui.NotificationsSettingsActivity.this;
            r1 = r1.callsSectionRow;
            if (r14 != r1) goto L_0x0570;
            r1 = "VoipNotificationSettings";
            r2 = 2131494601; // 0x7f0c06c9 float:1.8612715E38 double:1.0530982567E-314;
            r1 = org.telegram.messenger.LocaleController.getString(r1, r2);
            r0.setText(r1);
            goto L_0x0570;
        L_0x02f1:
            r0 = r13.itemView;
            r0 = (org.telegram.ui.Cells.TextSettingsCell) r0;
            r6 = org.telegram.ui.NotificationsSettingsActivity.this;
            r6 = r6.currentAccount;
            r6 = org.telegram.messenger.MessagesController.getNotificationsSettings(r6);
            r7 = org.telegram.ui.NotificationsSettingsActivity.this;
            r7 = r7.messageSoundRow;
            if (r14 == r7) goto L_0x04f8;
            r7 = org.telegram.ui.NotificationsSettingsActivity.this;
            r7 = r7.groupSoundRow;
            if (r14 == r7) goto L_0x04f8;
            r7 = org.telegram.ui.NotificationsSettingsActivity.this;
            r7 = r7.callsRingtoneRow;
            if (r14 != r7) goto L_0x0319;
            goto L_0x04f8;
            r7 = org.telegram.ui.NotificationsSettingsActivity.this;
            r7 = r7.messageVibrateRow;
            r8 = 4;
            r9 = 2;
            if (r14 == r7) goto L_0x0460;
            r7 = org.telegram.ui.NotificationsSettingsActivity.this;
            r7 = r7.groupVibrateRow;
            if (r14 == r7) goto L_0x0460;
            r7 = org.telegram.ui.NotificationsSettingsActivity.this;
            r7 = r7.callsVibrateRow;
            if (r14 != r7) goto L_0x0335;
            goto L_0x0460;
            r3 = org.telegram.ui.NotificationsSettingsActivity.this;
            r3 = r3.repeatRow;
            if (r14 != r3) goto L_0x0370;
            r1 = "repeat_messages";
            r2 = 60;
            r1 = r6.getInt(r1, r2);
            if (r1 != 0) goto L_0x0351;
            r2 = "RepeatNotificationsNever";
            r3 = 2131494234; // 0x7f0c055a float:1.861197E38 double:1.0530980753E-314;
            r2 = org.telegram.messenger.LocaleController.getString(r2, r3);
            goto L_0x0362;
            if (r1 >= r2) goto L_0x035a;
            r2 = "Minutes";
            r2 = org.telegram.messenger.LocaleController.formatPluralString(r2, r1);
            goto L_0x0350;
            r2 = "Hours";
            r3 = r1 / 60;
            r2 = org.telegram.messenger.LocaleController.formatPluralString(r2, r3);
            r3 = "RepeatNotifications";
            r5 = 2131494233; // 0x7f0c0559 float:1.8611969E38 double:1.053098075E-314;
            r3 = org.telegram.messenger.LocaleController.getString(r3, r5);
            r0.setTextAndValue(r3, r2, r4);
            goto L_0x0570;
            r3 = org.telegram.ui.NotificationsSettingsActivity.this;
            r3 = r3.messagePriorityRow;
            if (r14 == r3) goto L_0x03ea;
            r3 = org.telegram.ui.NotificationsSettingsActivity.this;
            r3 = r3.groupPriorityRow;
            if (r14 != r3) goto L_0x0381;
            goto L_0x03ea;
            r1 = org.telegram.ui.NotificationsSettingsActivity.this;
            r1 = r1.messagePopupNotificationRow;
            if (r14 == r1) goto L_0x0391;
            r1 = org.telegram.ui.NotificationsSettingsActivity.this;
            r1 = r1.groupPopupNotificationRow;
            if (r14 != r1) goto L_0x0570;
            r1 = 0;
            r2 = org.telegram.ui.NotificationsSettingsActivity.this;
            r2 = r2.messagePopupNotificationRow;
            if (r14 != r2) goto L_0x03a1;
            r2 = "popupAll";
            r1 = r6.getInt(r2, r4);
            goto L_0x03af;
            r2 = org.telegram.ui.NotificationsSettingsActivity.this;
            r2 = r2.groupPopupNotificationRow;
            if (r14 != r2) goto L_0x03af;
            r2 = "popupGroup";
            r1 = r6.getInt(r2, r4);
            if (r1 != 0) goto L_0x03bb;
            r2 = "NoPopup";
            r3 = 2131493902; // 0x7f0c040e float:1.8611297E38 double:1.0530979113E-314;
            r2 = org.telegram.messenger.LocaleController.getString(r2, r3);
            goto L_0x03dc;
            if (r1 != r5) goto L_0x03c7;
            r2 = "OnlyWhenScreenOn";
            r3 = 2131494039; // 0x7f0c0497 float:1.8611575E38 double:1.053097979E-314;
            r2 = org.telegram.messenger.LocaleController.getString(r2, r3);
            goto L_0x03ba;
            if (r1 != r9) goto L_0x03d3;
            r2 = "OnlyWhenScreenOff";
            r3 = 2131494038; // 0x7f0c0496 float:1.8611573E38 double:1.0530979785E-314;
            r2 = org.telegram.messenger.LocaleController.getString(r2, r3);
            goto L_0x03ba;
            r2 = "AlwaysShowPopup";
            r3 = 2131492961; // 0x7f0c0061 float:1.8609389E38 double:1.0530974464E-314;
            r2 = org.telegram.messenger.LocaleController.getString(r2, r3);
            r3 = "PopupNotification";
            r4 = 2131494187; // 0x7f0c052b float:1.8611875E38 double:1.053098052E-314;
            r3 = org.telegram.messenger.LocaleController.getString(r3, r4);
            r0.setTextAndValue(r3, r2, r5);
            goto L_0x0570;
            r3 = 0;
            r7 = org.telegram.ui.NotificationsSettingsActivity.this;
            r7 = r7.messagePriorityRow;
            if (r14 != r7) goto L_0x03fa;
            r7 = "priority_messages";
            r3 = r6.getInt(r7, r5);
            goto L_0x0408;
            r7 = org.telegram.ui.NotificationsSettingsActivity.this;
            r7 = r7.groupPriorityRow;
            if (r14 != r7) goto L_0x0408;
            r7 = "priority_group";
            r3 = r6.getInt(r7, r5);
            if (r3 != 0) goto L_0x041d;
            r1 = "NotificationsImportance";
            r1 = org.telegram.messenger.LocaleController.getString(r1, r2);
            r2 = "NotificationsPriorityHigh";
            r5 = 2131494016; // 0x7f0c0480 float:1.8611528E38 double:1.0530979676E-314;
            r2 = org.telegram.messenger.LocaleController.getString(r2, r5);
            r0.setTextAndValue(r1, r2, r4);
            goto L_0x045e;
            if (r3 == r5) goto L_0x044c;
            if (r3 != r9) goto L_0x0422;
            goto L_0x044c;
            if (r3 != r8) goto L_0x0437;
            r1 = "NotificationsImportance";
            r1 = org.telegram.messenger.LocaleController.getString(r1, r2);
            r2 = "NotificationsPriorityLow";
            r5 = 2131494017; // 0x7f0c0481 float:1.861153E38 double:1.053097968E-314;
            r2 = org.telegram.messenger.LocaleController.getString(r2, r5);
            r0.setTextAndValue(r1, r2, r4);
            goto L_0x045e;
            if (r3 != r1) goto L_0x045e;
            r1 = "NotificationsImportance";
            r1 = org.telegram.messenger.LocaleController.getString(r1, r2);
            r2 = "NotificationsPriorityMedium";
            r5 = 2131494018; // 0x7f0c0482 float:1.8611533E38 double:1.0530979686E-314;
            r2 = org.telegram.messenger.LocaleController.getString(r2, r5);
            r0.setTextAndValue(r1, r2, r4);
            goto L_0x045e;
            r1 = "NotificationsImportance";
            r1 = org.telegram.messenger.LocaleController.getString(r1, r2);
            r2 = "NotificationsPriorityUrgent";
            r5 = 2131494020; // 0x7f0c0484 float:1.8611537E38 double:1.0530979696E-314;
            r2 = org.telegram.messenger.LocaleController.getString(r2, r5);
            r0.setTextAndValue(r1, r2, r4);
            goto L_0x0570;
            r1 = 0;
            r2 = org.telegram.ui.NotificationsSettingsActivity.this;
            r2 = r2.messageVibrateRow;
            if (r14 != r2) goto L_0x0470;
            r2 = "vibrate_messages";
            r1 = r6.getInt(r2, r4);
            goto L_0x048d;
            r2 = org.telegram.ui.NotificationsSettingsActivity.this;
            r2 = r2.groupVibrateRow;
            if (r14 != r2) goto L_0x047f;
            r2 = "vibrate_group";
            r1 = r6.getInt(r2, r4);
            goto L_0x048d;
            r2 = org.telegram.ui.NotificationsSettingsActivity.this;
            r2 = r2.callsVibrateRow;
            if (r14 != r2) goto L_0x048d;
            r2 = "vibrate_calls";
            r1 = r6.getInt(r2, r4);
            if (r1 != 0) goto L_0x04a2;
            r2 = "Vibrate";
            r2 = org.telegram.messenger.LocaleController.getString(r2, r3);
            r3 = "VibrationDefault";
            r4 = 2131494575; // 0x7f0c06af float:1.8612662E38 double:1.053098244E-314;
            r3 = org.telegram.messenger.LocaleController.getString(r3, r4);
            r0.setTextAndValue(r2, r3, r5);
            goto L_0x04f6;
            if (r1 != r5) goto L_0x04b7;
            r2 = "Vibrate";
            r2 = org.telegram.messenger.LocaleController.getString(r2, r3);
            r3 = "Short";
            r4 = 2131494401; // 0x7f0c0601 float:1.861231E38 double:1.053098158E-314;
            r3 = org.telegram.messenger.LocaleController.getString(r3, r4);
            r0.setTextAndValue(r2, r3, r5);
            goto L_0x04f6;
            if (r1 != r9) goto L_0x04cc;
            r2 = "Vibrate";
            r2 = org.telegram.messenger.LocaleController.getString(r2, r3);
            r3 = "VibrationDisabled";
            r4 = 2131494576; // 0x7f0c06b0 float:1.8612664E38 double:1.0530982443E-314;
            r3 = org.telegram.messenger.LocaleController.getString(r3, r4);
            r0.setTextAndValue(r2, r3, r5);
            goto L_0x04f6;
            r2 = 3;
            if (r1 != r2) goto L_0x04e2;
            r2 = "Vibrate";
            r2 = org.telegram.messenger.LocaleController.getString(r2, r3);
            r3 = "Long";
            r4 = 2131493779; // 0x7f0c0393 float:1.8611048E38 double:1.0530978505E-314;
            r3 = org.telegram.messenger.LocaleController.getString(r3, r4);
            r0.setTextAndValue(r2, r3, r5);
            goto L_0x04f6;
            if (r1 != r8) goto L_0x04f6;
            r2 = "Vibrate";
            r2 = org.telegram.messenger.LocaleController.getString(r2, r3);
            r3 = "OnlyIfSilent";
            r4 = 2131494037; // 0x7f0c0495 float:1.8611571E38 double:1.053097978E-314;
            r3 = org.telegram.messenger.LocaleController.getString(r3, r4);
            r0.setTextAndValue(r2, r3, r5);
            goto L_0x0570;
            r1 = 0;
            r2 = org.telegram.ui.NotificationsSettingsActivity.this;
            r2 = r2.messageSoundRow;
            r3 = 2131494420; // 0x7f0c0614 float:1.8612348E38 double:1.053098167E-314;
            if (r14 != r2) goto L_0x0511;
            r2 = "GlobalSound";
            r4 = "SoundDefault";
            r3 = org.telegram.messenger.LocaleController.getString(r4, r3);
            r1 = r6.getString(r2, r3);
            goto L_0x053d;
            r2 = org.telegram.ui.NotificationsSettingsActivity.this;
            r2 = r2.groupSoundRow;
            if (r14 != r2) goto L_0x0526;
            r2 = "GroupSound";
            r4 = "SoundDefault";
            r3 = org.telegram.messenger.LocaleController.getString(r4, r3);
            r1 = r6.getString(r2, r3);
            goto L_0x053d;
            r2 = org.telegram.ui.NotificationsSettingsActivity.this;
            r2 = r2.callsRingtoneRow;
            if (r14 != r2) goto L_0x053d;
            r2 = "CallsRingtone";
            r3 = "DefaultRingtone";
            r4 = 2131493355; // 0x7f0c01eb float:1.8610188E38 double:1.053097641E-314;
            r3 = org.telegram.messenger.LocaleController.getString(r3, r4);
            r1 = r6.getString(r2, r3);
            r2 = "NoSound";
            r2 = r1.equals(r2);
            if (r2 == 0) goto L_0x054e;
            r2 = "NoSound";
            r3 = 2131493913; // 0x7f0c0419 float:1.861132E38 double:1.0530979167E-314;
            r1 = org.telegram.messenger.LocaleController.getString(r2, r3);
            r2 = org.telegram.ui.NotificationsSettingsActivity.this;
            r2 = r2.callsRingtoneRow;
            if (r14 != r2) goto L_0x0563;
            r2 = "VoipSettingsRingtone";
            r3 = 2131494618; // 0x7f0c06da float:1.861275E38 double:1.053098265E-314;
            r2 = org.telegram.messenger.LocaleController.getString(r2, r3);
            r0.setTextAndValue(r2, r1, r5);
            goto L_0x056f;
            r2 = "Sound";
            r3 = 2131494419; // 0x7f0c0613 float:1.8612346E38 double:1.0530981667E-314;
            r2 = org.telegram.messenger.LocaleController.getString(r2, r3);
            r0.setTextAndValue(r2, r1, r5);
        L_0x0570:
            return;
            */
            throw new UnsupportedOperationException("Method not decompiled: org.telegram.ui.NotificationsSettingsActivity.ListAdapter.onBindViewHolder(org.telegram.messenger.support.widget.RecyclerView$ViewHolder, int):void");
        }

        public ListAdapter(Context context) {
            this.mContext = context;
        }

        public boolean isEnabled(ViewHolder holder) {
            int position = holder.getAdapterPosition();
            return (position == NotificationsSettingsActivity.this.messageSectionRow || position == NotificationsSettingsActivity.this.groupSectionRow || position == NotificationsSettingsActivity.this.inappSectionRow || position == NotificationsSettingsActivity.this.eventsSectionRow || position == NotificationsSettingsActivity.this.otherSectionRow || position == NotificationsSettingsActivity.this.resetSectionRow || position == NotificationsSettingsActivity.this.eventsSectionRow2 || position == NotificationsSettingsActivity.this.groupSectionRow2 || position == NotificationsSettingsActivity.this.inappSectionRow2 || position == NotificationsSettingsActivity.this.otherSectionRow2 || position == NotificationsSettingsActivity.this.resetSectionRow2 || position == NotificationsSettingsActivity.this.callsSectionRow2 || position == NotificationsSettingsActivity.this.callsSectionRow) ? false : true;
        }

        public int getItemCount() {
            return NotificationsSettingsActivity.this.rowCount;
        }

        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view;
            switch (viewType) {
                case 0:
                    view = new HeaderCell(this.mContext);
                    view.setBackgroundColor(Theme.getColor(Theme.key_windowBackgroundWhite));
                    break;
                case 1:
                    view = new TextCheckCell(this.mContext);
                    view.setBackgroundColor(Theme.getColor(Theme.key_windowBackgroundWhite));
                    break;
                case 2:
                    view = new TextDetailSettingsCell(this.mContext);
                    view.setBackgroundColor(Theme.getColor(Theme.key_windowBackgroundWhite));
                    break;
                case 3:
                    view = new TextColorCell(this.mContext);
                    view.setBackgroundColor(Theme.getColor(Theme.key_windowBackgroundWhite));
                    break;
                case 4:
                    view = new ShadowSectionCell(this.mContext);
                    break;
                default:
                    view = new TextSettingsCell(this.mContext);
                    view.setBackgroundColor(Theme.getColor(Theme.key_windowBackgroundWhite));
                    break;
            }
            return new Holder(view);
        }

        public int getItemViewType(int position) {
            if (!(position == NotificationsSettingsActivity.this.messageSectionRow || position == NotificationsSettingsActivity.this.groupSectionRow || position == NotificationsSettingsActivity.this.inappSectionRow || position == NotificationsSettingsActivity.this.eventsSectionRow || position == NotificationsSettingsActivity.this.otherSectionRow || position == NotificationsSettingsActivity.this.resetSectionRow)) {
                if (position != NotificationsSettingsActivity.this.callsSectionRow) {
                    if (!(position == NotificationsSettingsActivity.this.messageAlertRow || position == NotificationsSettingsActivity.this.messagePreviewRow || position == NotificationsSettingsActivity.this.groupAlertRow || position == NotificationsSettingsActivity.this.groupPreviewRow || position == NotificationsSettingsActivity.this.inappSoundRow || position == NotificationsSettingsActivity.this.inappVibrateRow || position == NotificationsSettingsActivity.this.inappPreviewRow || position == NotificationsSettingsActivity.this.contactJoinedRow || position == NotificationsSettingsActivity.this.pinnedMessageRow || position == NotificationsSettingsActivity.this.notificationsServiceRow || position == NotificationsSettingsActivity.this.badgeNumberRow || position == NotificationsSettingsActivity.this.inappPriorityRow || position == NotificationsSettingsActivity.this.inchatSoundRow || position == NotificationsSettingsActivity.this.androidAutoAlertRow)) {
                        if (position != NotificationsSettingsActivity.this.notificationsServiceConnectionRow) {
                            if (position != NotificationsSettingsActivity.this.messageLedRow) {
                                if (position != NotificationsSettingsActivity.this.groupLedRow) {
                                    if (!(position == NotificationsSettingsActivity.this.eventsSectionRow2 || position == NotificationsSettingsActivity.this.groupSectionRow2 || position == NotificationsSettingsActivity.this.inappSectionRow2 || position == NotificationsSettingsActivity.this.otherSectionRow2 || position == NotificationsSettingsActivity.this.resetSectionRow2)) {
                                        if (position != NotificationsSettingsActivity.this.callsSectionRow2) {
                                            if (position == NotificationsSettingsActivity.this.resetNotificationsRow) {
                                                return 2;
                                            }
                                            return 5;
                                        }
                                    }
                                    return 4;
                                }
                            }
                            return 3;
                        }
                    }
                    return 1;
                }
            }
            return 0;
        }
    }

    public boolean onFragmentCreate() {
        int i = this.rowCount;
        this.rowCount = i + 1;
        this.messageSectionRow = i;
        i = this.rowCount;
        this.rowCount = i + 1;
        this.messageAlertRow = i;
        i = this.rowCount;
        this.rowCount = i + 1;
        this.messagePreviewRow = i;
        i = this.rowCount;
        this.rowCount = i + 1;
        this.messageLedRow = i;
        i = this.rowCount;
        this.rowCount = i + 1;
        this.messageVibrateRow = i;
        i = this.rowCount;
        this.rowCount = i + 1;
        this.messagePopupNotificationRow = i;
        i = this.rowCount;
        this.rowCount = i + 1;
        this.messageSoundRow = i;
        if (VERSION.SDK_INT >= 21) {
            i = this.rowCount;
            this.rowCount = i + 1;
            this.messagePriorityRow = i;
        } else {
            this.messagePriorityRow = -1;
        }
        i = this.rowCount;
        this.rowCount = i + 1;
        this.groupSectionRow2 = i;
        i = this.rowCount;
        this.rowCount = i + 1;
        this.groupSectionRow = i;
        i = this.rowCount;
        this.rowCount = i + 1;
        this.groupAlertRow = i;
        i = this.rowCount;
        this.rowCount = i + 1;
        this.groupPreviewRow = i;
        i = this.rowCount;
        this.rowCount = i + 1;
        this.groupLedRow = i;
        i = this.rowCount;
        this.rowCount = i + 1;
        this.groupVibrateRow = i;
        i = this.rowCount;
        this.rowCount = i + 1;
        this.groupPopupNotificationRow = i;
        i = this.rowCount;
        this.rowCount = i + 1;
        this.groupSoundRow = i;
        if (VERSION.SDK_INT >= 21) {
            i = this.rowCount;
            this.rowCount = i + 1;
            this.groupPriorityRow = i;
        } else {
            this.groupPriorityRow = -1;
        }
        i = this.rowCount;
        this.rowCount = i + 1;
        this.inappSectionRow2 = i;
        i = this.rowCount;
        this.rowCount = i + 1;
        this.inappSectionRow = i;
        i = this.rowCount;
        this.rowCount = i + 1;
        this.inappSoundRow = i;
        i = this.rowCount;
        this.rowCount = i + 1;
        this.inappVibrateRow = i;
        i = this.rowCount;
        this.rowCount = i + 1;
        this.inappPreviewRow = i;
        i = this.rowCount;
        this.rowCount = i + 1;
        this.inchatSoundRow = i;
        if (VERSION.SDK_INT >= 21) {
            i = this.rowCount;
            this.rowCount = i + 1;
            this.inappPriorityRow = i;
        } else {
            this.inappPriorityRow = -1;
        }
        i = this.rowCount;
        this.rowCount = i + 1;
        this.callsSectionRow2 = i;
        i = this.rowCount;
        this.rowCount = i + 1;
        this.callsSectionRow = i;
        i = this.rowCount;
        this.rowCount = i + 1;
        this.callsVibrateRow = i;
        i = this.rowCount;
        this.rowCount = i + 1;
        this.callsRingtoneRow = i;
        i = this.rowCount;
        this.rowCount = i + 1;
        this.eventsSectionRow2 = i;
        i = this.rowCount;
        this.rowCount = i + 1;
        this.eventsSectionRow = i;
        i = this.rowCount;
        this.rowCount = i + 1;
        this.contactJoinedRow = i;
        i = this.rowCount;
        this.rowCount = i + 1;
        this.pinnedMessageRow = i;
        i = this.rowCount;
        this.rowCount = i + 1;
        this.otherSectionRow2 = i;
        i = this.rowCount;
        this.rowCount = i + 1;
        this.otherSectionRow = i;
        i = this.rowCount;
        this.rowCount = i + 1;
        this.notificationsServiceRow = i;
        i = this.rowCount;
        this.rowCount = i + 1;
        this.notificationsServiceConnectionRow = i;
        i = this.rowCount;
        this.rowCount = i + 1;
        this.badgeNumberRow = i;
        this.androidAutoAlertRow = -1;
        i = this.rowCount;
        this.rowCount = i + 1;
        this.repeatRow = i;
        i = this.rowCount;
        this.rowCount = i + 1;
        this.resetSectionRow2 = i;
        i = this.rowCount;
        this.rowCount = i + 1;
        this.resetSectionRow = i;
        i = this.rowCount;
        this.rowCount = i + 1;
        this.resetNotificationsRow = i;
        NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.notificationsSettingsUpdated);
        return super.onFragmentCreate();
    }

    public void onFragmentDestroy() {
        super.onFragmentDestroy();
        NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.notificationsSettingsUpdated);
    }

    public View createView(Context context) {
        this.actionBar.setBackButtonImage(R.drawable.ic_ab_back);
        this.actionBar.setAllowOverlayTitle(true);
        this.actionBar.setTitle(LocaleController.getString("NotificationsAndSounds", R.string.NotificationsAndSounds));
        this.actionBar.setActionBarMenuOnItemClick(new ActionBarMenuOnItemClick() {
            public void onItemClick(int id) {
                if (id == -1) {
                    NotificationsSettingsActivity.this.finishFragment();
                }
            }
        });
        this.fragmentView = new FrameLayout(context);
        FrameLayout frameLayout = this.fragmentView;
        frameLayout.setBackgroundColor(Theme.getColor(Theme.key_windowBackgroundGray));
        this.listView = new RecyclerListView(context);
        this.listView.setItemAnimator(null);
        this.listView.setLayoutAnimation(null);
        this.listView.setLayoutManager(new LinearLayoutManager(context, 1, false) {
            public boolean supportsPredictiveItemAnimations() {
                return false;
            }
        });
        this.listView.setVerticalScrollBarEnabled(false);
        frameLayout.addView(this.listView, LayoutHelper.createFrame(-1, -1.0f));
        RecyclerListView recyclerListView = this.listView;
        Adapter listAdapter = new ListAdapter(context);
        this.adapter = listAdapter;
        recyclerListView.setAdapter(listAdapter);
        this.listView.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(View view, final int position) {
                SharedPreferences preferences;
                Editor editor;
                TextCheckCell textCheckCell;
                boolean enabled = false;
                boolean z = false;
                if (position != NotificationsSettingsActivity.this.messageAlertRow) {
                    if (position != NotificationsSettingsActivity.this.groupAlertRow) {
                        if (position != NotificationsSettingsActivity.this.messagePreviewRow) {
                            if (position != NotificationsSettingsActivity.this.groupPreviewRow) {
                                int i = 2;
                                if (!(position == NotificationsSettingsActivity.this.messageSoundRow || position == NotificationsSettingsActivity.this.groupSoundRow)) {
                                    if (position != NotificationsSettingsActivity.this.callsRingtoneRow) {
                                        if (position == NotificationsSettingsActivity.this.resetNotificationsRow) {
                                            if (!NotificationsSettingsActivity.this.reseting) {
                                                NotificationsSettingsActivity.this.reseting = true;
                                                ConnectionsManager.getInstance(NotificationsSettingsActivity.this.currentAccount).sendRequest(new TL_account_resetNotifySettings(), new RequestDelegate() {
                                                    public void run(TLObject response, TL_error error) {
                                                        AndroidUtilities.runOnUIThread(new Runnable() {
                                                            public void run() {
                                                                MessagesController.getInstance(NotificationsSettingsActivity.this.currentAccount).enableJoined = true;
                                                                NotificationsSettingsActivity.this.reseting = false;
                                                                Editor editor = MessagesController.getNotificationsSettings(NotificationsSettingsActivity.this.currentAccount).edit();
                                                                editor.clear();
                                                                editor.commit();
                                                                NotificationsSettingsActivity.this.adapter.notifyDataSetChanged();
                                                                if (NotificationsSettingsActivity.this.getParentActivity() != null) {
                                                                    Toast.makeText(NotificationsSettingsActivity.this.getParentActivity(), LocaleController.getString("ResetNotificationsText", R.string.ResetNotificationsText), 0).show();
                                                                }
                                                            }
                                                        });
                                                    }
                                                });
                                            } else {
                                                return;
                                            }
                                        } else if (position == NotificationsSettingsActivity.this.inappSoundRow) {
                                            preferences = MessagesController.getNotificationsSettings(NotificationsSettingsActivity.this.currentAccount);
                                            editor = preferences.edit();
                                            enabled = preferences.getBoolean("EnableInAppSounds", true);
                                            editor.putBoolean("EnableInAppSounds", enabled ^ 1);
                                            editor.commit();
                                        } else if (position == NotificationsSettingsActivity.this.inappVibrateRow) {
                                            preferences = MessagesController.getNotificationsSettings(NotificationsSettingsActivity.this.currentAccount);
                                            editor = preferences.edit();
                                            enabled = preferences.getBoolean("EnableInAppVibrate", true);
                                            editor.putBoolean("EnableInAppVibrate", enabled ^ 1);
                                            editor.commit();
                                        } else if (position == NotificationsSettingsActivity.this.inappPreviewRow) {
                                            preferences = MessagesController.getNotificationsSettings(NotificationsSettingsActivity.this.currentAccount);
                                            editor = preferences.edit();
                                            enabled = preferences.getBoolean("EnableInAppPreview", true);
                                            editor.putBoolean("EnableInAppPreview", enabled ^ 1);
                                            editor.commit();
                                        } else if (position == NotificationsSettingsActivity.this.inchatSoundRow) {
                                            preferences = MessagesController.getNotificationsSettings(NotificationsSettingsActivity.this.currentAccount);
                                            editor = preferences.edit();
                                            enabled = preferences.getBoolean("EnableInChatSound", true);
                                            editor.putBoolean("EnableInChatSound", enabled ^ 1);
                                            editor.commit();
                                            NotificationsController.getInstance(NotificationsSettingsActivity.this.currentAccount).setInChatSoundEnabled(enabled ^ 1);
                                        } else if (position == NotificationsSettingsActivity.this.inappPriorityRow) {
                                            preferences = MessagesController.getNotificationsSettings(NotificationsSettingsActivity.this.currentAccount);
                                            editor = preferences.edit();
                                            enabled = preferences.getBoolean("EnableInAppPriority", false);
                                            editor.putBoolean("EnableInAppPriority", enabled ^ 1);
                                            editor.commit();
                                        } else if (position == NotificationsSettingsActivity.this.contactJoinedRow) {
                                            preferences = MessagesController.getNotificationsSettings(NotificationsSettingsActivity.this.currentAccount);
                                            editor = preferences.edit();
                                            enabled = preferences.getBoolean("EnableContactJoined", true);
                                            MessagesController.getInstance(NotificationsSettingsActivity.this.currentAccount).enableJoined = enabled ^ 1;
                                            editor.putBoolean("EnableContactJoined", enabled ^ 1);
                                            editor.commit();
                                        } else if (position == NotificationsSettingsActivity.this.pinnedMessageRow) {
                                            preferences = MessagesController.getNotificationsSettings(NotificationsSettingsActivity.this.currentAccount);
                                            editor = preferences.edit();
                                            enabled = preferences.getBoolean("PinnedMessages", true);
                                            editor.putBoolean("PinnedMessages", enabled ^ 1);
                                            editor.commit();
                                        } else if (position == NotificationsSettingsActivity.this.androidAutoAlertRow) {
                                            preferences = MessagesController.getNotificationsSettings(NotificationsSettingsActivity.this.currentAccount);
                                            editor = preferences.edit();
                                            enabled = preferences.getBoolean("EnableAutoNotifications", false);
                                            editor.putBoolean("EnableAutoNotifications", enabled ^ 1);
                                            editor.commit();
                                        } else if (position == NotificationsSettingsActivity.this.badgeNumberRow) {
                                            editor = MessagesController.getNotificationsSettings(NotificationsSettingsActivity.this.currentAccount).edit();
                                            enabled = NotificationsController.getInstance(NotificationsSettingsActivity.this.currentAccount).showBadgeNumber;
                                            NotificationsController.getInstance(NotificationsSettingsActivity.this.currentAccount).showBadgeNumber = enabled ^ 1;
                                            editor.putBoolean("badgeNumber", NotificationsController.getInstance(NotificationsSettingsActivity.this.currentAccount).showBadgeNumber);
                                            editor.commit();
                                            NotificationsController.getInstance(NotificationsSettingsActivity.this.currentAccount).setBadgeEnabled(enabled ^ 1);
                                        } else if (position == NotificationsSettingsActivity.this.notificationsServiceConnectionRow) {
                                            preferences = MessagesController.getNotificationsSettings(NotificationsSettingsActivity.this.currentAccount);
                                            enabled = preferences.getBoolean("pushConnection", true);
                                            editor = preferences.edit();
                                            editor.putBoolean("pushConnection", enabled ^ 1);
                                            editor.commit();
                                            if (enabled) {
                                                ConnectionsManager.getInstance(NotificationsSettingsActivity.this.currentAccount).setPushConnectionEnabled(false);
                                            } else {
                                                ConnectionsManager.getInstance(NotificationsSettingsActivity.this.currentAccount).setPushConnectionEnabled(true);
                                            }
                                        } else if (position == NotificationsSettingsActivity.this.notificationsServiceRow) {
                                            preferences = MessagesController.getNotificationsSettings(NotificationsSettingsActivity.this.currentAccount);
                                            enabled = preferences.getBoolean("pushService", true);
                                            editor = preferences.edit();
                                            editor.putBoolean("pushService", enabled ^ 1);
                                            editor.commit();
                                            if (enabled) {
                                                ApplicationLoader.stopPushService();
                                            } else {
                                                ApplicationLoader.startPushService();
                                            }
                                        } else {
                                            if (position != NotificationsSettingsActivity.this.messageLedRow) {
                                                if (position != NotificationsSettingsActivity.this.groupLedRow) {
                                                    if (position != NotificationsSettingsActivity.this.messagePopupNotificationRow) {
                                                        if (position != NotificationsSettingsActivity.this.groupPopupNotificationRow) {
                                                            if (!(position == NotificationsSettingsActivity.this.messageVibrateRow || position == NotificationsSettingsActivity.this.groupVibrateRow)) {
                                                                if (position != NotificationsSettingsActivity.this.callsVibrateRow) {
                                                                    if (position != NotificationsSettingsActivity.this.messagePriorityRow) {
                                                                        if (position != NotificationsSettingsActivity.this.groupPriorityRow) {
                                                                            if (position == NotificationsSettingsActivity.this.repeatRow) {
                                                                                Builder builder = new Builder(NotificationsSettingsActivity.this.getParentActivity());
                                                                                builder.setTitle(LocaleController.getString("RepeatNotifications", R.string.RepeatNotifications));
                                                                                builder.setItems(new CharSequence[]{LocaleController.getString("RepeatDisabled", R.string.RepeatDisabled), LocaleController.formatPluralString("Minutes", 5), LocaleController.formatPluralString("Minutes", 10), LocaleController.formatPluralString("Minutes", 30), LocaleController.formatPluralString("Hours", 1), LocaleController.formatPluralString("Hours", 2), LocaleController.formatPluralString("Hours", 4)}, new OnClickListener() {
                                                                                    public void onClick(DialogInterface dialog, int which) {
                                                                                        int minutes = 0;
                                                                                        if (which == 1) {
                                                                                            minutes = 5;
                                                                                        } else if (which == 2) {
                                                                                            minutes = 10;
                                                                                        } else if (which == 3) {
                                                                                            minutes = 30;
                                                                                        } else if (which == 4) {
                                                                                            minutes = 60;
                                                                                        } else if (which == 5) {
                                                                                            minutes = 120;
                                                                                        } else if (which == 6) {
                                                                                            minutes = PsExtractor.VIDEO_STREAM_MASK;
                                                                                        }
                                                                                        MessagesController.getNotificationsSettings(NotificationsSettingsActivity.this.currentAccount).edit().putInt("repeat_messages", minutes).commit();
                                                                                        NotificationsSettingsActivity.this.adapter.notifyItemChanged(position);
                                                                                    }
                                                                                });
                                                                                builder.setNegativeButton(LocaleController.getString("Cancel", R.string.Cancel), null);
                                                                                NotificationsSettingsActivity.this.showDialog(builder.create());
                                                                            }
                                                                        }
                                                                    }
                                                                    NotificationsSettingsActivity.this.showDialog(AlertsCreator.createPrioritySelectDialog(NotificationsSettingsActivity.this.getParentActivity(), NotificationsSettingsActivity.this, 0, position == NotificationsSettingsActivity.this.groupPriorityRow, position == NotificationsSettingsActivity.this.messagePriorityRow, new Runnable() {
                                                                        public void run() {
                                                                            NotificationsSettingsActivity.this.adapter.notifyItemChanged(position);
                                                                        }
                                                                    }));
                                                                }
                                                            }
                                                            if (NotificationsSettingsActivity.this.getParentActivity() != null) {
                                                                String key = null;
                                                                if (position == NotificationsSettingsActivity.this.messageVibrateRow) {
                                                                    key = "vibrate_messages";
                                                                } else if (position == NotificationsSettingsActivity.this.groupVibrateRow) {
                                                                    key = "vibrate_group";
                                                                } else if (position == NotificationsSettingsActivity.this.callsVibrateRow) {
                                                                    key = "vibrate_calls";
                                                                }
                                                                NotificationsSettingsActivity.this.showDialog(AlertsCreator.createVibrationSelectDialog(NotificationsSettingsActivity.this.getParentActivity(), NotificationsSettingsActivity.this, 0, key, new Runnable() {
                                                                    public void run() {
                                                                        NotificationsSettingsActivity.this.adapter.notifyItemChanged(position);
                                                                    }
                                                                }));
                                                            } else {
                                                                return;
                                                            }
                                                        }
                                                    }
                                                    if (NotificationsSettingsActivity.this.getParentActivity() != null) {
                                                        NotificationsSettingsActivity.this.showDialog(AlertsCreator.createPopupSelectDialog(NotificationsSettingsActivity.this.getParentActivity(), NotificationsSettingsActivity.this, position == NotificationsSettingsActivity.this.groupPopupNotificationRow, position == NotificationsSettingsActivity.this.messagePopupNotificationRow, new Runnable() {
                                                            public void run() {
                                                                NotificationsSettingsActivity.this.adapter.notifyItemChanged(position);
                                                            }
                                                        }));
                                                    } else {
                                                        return;
                                                    }
                                                }
                                            }
                                            if (NotificationsSettingsActivity.this.getParentActivity() != null) {
                                                NotificationsSettingsActivity.this.showDialog(AlertsCreator.createColorSelectDialog(NotificationsSettingsActivity.this.getParentActivity(), 0, position == NotificationsSettingsActivity.this.groupLedRow, position == NotificationsSettingsActivity.this.messageLedRow, new Runnable() {
                                                    public void run() {
                                                        NotificationsSettingsActivity.this.adapter.notifyItemChanged(position);
                                                    }
                                                }));
                                            } else {
                                                return;
                                            }
                                        }
                                        if (view instanceof TextCheckCell) {
                                            textCheckCell = (TextCheckCell) view;
                                            if (!enabled) {
                                                z = true;
                                            }
                                            textCheckCell.setChecked(z);
                                        }
                                    }
                                }
                                try {
                                    preferences = MessagesController.getNotificationsSettings(NotificationsSettingsActivity.this.currentAccount);
                                    Intent tmpIntent = new Intent("android.intent.action.RINGTONE_PICKER");
                                    tmpIntent.putExtra("android.intent.extra.ringtone.TYPE", position == NotificationsSettingsActivity.this.callsRingtoneRow ? 1 : 2);
                                    tmpIntent.putExtra("android.intent.extra.ringtone.SHOW_DEFAULT", true);
                                    String str = "android.intent.extra.ringtone.DEFAULT_URI";
                                    if (position == NotificationsSettingsActivity.this.callsRingtoneRow) {
                                        i = 1;
                                    }
                                    tmpIntent.putExtra(str, RingtoneManager.getDefaultUri(i));
                                    Uri currentSound = null;
                                    str = null;
                                    Uri defaultUri = position == NotificationsSettingsActivity.this.callsRingtoneRow ? System.DEFAULT_RINGTONE_URI : System.DEFAULT_NOTIFICATION_URI;
                                    if (defaultUri != null) {
                                        str = defaultUri.getPath();
                                    }
                                    String path;
                                    if (position == NotificationsSettingsActivity.this.messageSoundRow) {
                                        path = preferences.getString("GlobalSoundPath", str);
                                        if (!(path == null || path.equals("NoSound"))) {
                                            currentSound = path.equals(str) ? defaultUri : Uri.parse(path);
                                        }
                                    } else if (position == NotificationsSettingsActivity.this.groupSoundRow) {
                                        path = preferences.getString("GroupSoundPath", str);
                                        if (!(path == null || path.equals("NoSound"))) {
                                            currentSound = path.equals(str) ? defaultUri : Uri.parse(path);
                                        }
                                    } else if (position == NotificationsSettingsActivity.this.callsRingtoneRow) {
                                        path = preferences.getString("CallsRingtonfePath", str);
                                        if (!(path == null || path.equals("NoSound"))) {
                                            currentSound = path.equals(str) ? defaultUri : Uri.parse(path);
                                        }
                                    }
                                    tmpIntent.putExtra("android.intent.extra.ringtone.EXISTING_URI", currentSound);
                                    NotificationsSettingsActivity.this.startActivityForResult(tmpIntent, position);
                                } catch (Throwable e) {
                                    FileLog.e(e);
                                }
                                if (view instanceof TextCheckCell) {
                                    textCheckCell = (TextCheckCell) view;
                                    if (enabled) {
                                        z = true;
                                    }
                                    textCheckCell.setChecked(z);
                                }
                            }
                        }
                        preferences = MessagesController.getNotificationsSettings(NotificationsSettingsActivity.this.currentAccount);
                        editor = preferences.edit();
                        if (position == NotificationsSettingsActivity.this.messagePreviewRow) {
                            enabled = preferences.getBoolean("EnablePreviewAll", true);
                            editor.putBoolean("EnablePreviewAll", enabled ^ 1);
                        } else if (position == NotificationsSettingsActivity.this.groupPreviewRow) {
                            enabled = preferences.getBoolean("EnablePreviewGroup", true);
                            editor.putBoolean("EnablePreviewGroup", enabled ^ 1);
                        }
                        editor.commit();
                        NotificationsSettingsActivity.this.updateServerNotificationsSettings(position == NotificationsSettingsActivity.this.groupPreviewRow);
                        if (view instanceof TextCheckCell) {
                            textCheckCell = (TextCheckCell) view;
                            if (enabled) {
                                z = true;
                            }
                            textCheckCell.setChecked(z);
                        }
                    }
                }
                preferences = MessagesController.getNotificationsSettings(NotificationsSettingsActivity.this.currentAccount);
                editor = preferences.edit();
                if (position == NotificationsSettingsActivity.this.messageAlertRow) {
                    enabled = preferences.getBoolean("EnableAll", true);
                    editor.putBoolean("EnableAll", enabled ^ 1);
                } else if (position == NotificationsSettingsActivity.this.groupAlertRow) {
                    enabled = preferences.getBoolean("EnableGroup", true);
                    editor.putBoolean("EnableGroup", enabled ^ 1);
                }
                editor.commit();
                NotificationsSettingsActivity.this.updateServerNotificationsSettings(position == NotificationsSettingsActivity.this.groupAlertRow);
                if (view instanceof TextCheckCell) {
                    textCheckCell = (TextCheckCell) view;
                    if (enabled) {
                        z = true;
                    }
                    textCheckCell.setChecked(z);
                }
            }
        });
        return this.fragmentView;
    }

    public void updateServerNotificationsSettings(boolean group) {
    }

    public void onActivityResultFragment(int requestCode, int resultCode, Intent data) {
        if (resultCode == -1) {
            Uri ringtone = (Uri) data.getParcelableExtra("android.intent.extra.ringtone.PICKED_URI");
            String name = null;
            if (ringtone != null) {
                Ringtone rng = RingtoneManager.getRingtone(getParentActivity(), ringtone);
                if (rng != null) {
                    if (requestCode == this.callsRingtoneRow) {
                        if (ringtone.equals(System.DEFAULT_RINGTONE_URI)) {
                            name = LocaleController.getString("DefaultRingtone", R.string.DefaultRingtone);
                        } else {
                            name = rng.getTitle(getParentActivity());
                        }
                    } else if (ringtone.equals(System.DEFAULT_NOTIFICATION_URI)) {
                        name = LocaleController.getString("SoundDefault", R.string.SoundDefault);
                    } else {
                        name = rng.getTitle(getParentActivity());
                    }
                    rng.stop();
                }
            }
            Editor editor = MessagesController.getNotificationsSettings(this.currentAccount).edit();
            if (requestCode == this.messageSoundRow) {
                if (name == null || ringtone == null) {
                    editor.putString("GlobalSound", "NoSound");
                    editor.putString("GlobalSoundPath", "NoSound");
                } else {
                    editor.putString("GlobalSound", name);
                    editor.putString("GlobalSoundPath", ringtone.toString());
                }
            } else if (requestCode == this.groupSoundRow) {
                if (name == null || ringtone == null) {
                    editor.putString("GroupSound", "NoSound");
                    editor.putString("GroupSoundPath", "NoSound");
                } else {
                    editor.putString("GroupSound", name);
                    editor.putString("GroupSoundPath", ringtone.toString());
                }
            } else if (requestCode == this.callsRingtoneRow) {
                if (name == null || ringtone == null) {
                    editor.putString("CallsRingtone", "NoSound");
                    editor.putString("CallsRingtonePath", "NoSound");
                } else {
                    editor.putString("CallsRingtone", name);
                    editor.putString("CallsRingtonePath", ringtone.toString());
                }
            }
            editor.commit();
            this.adapter.notifyItemChanged(requestCode);
        }
    }

    public void didReceivedNotification(int id, int account, Object... args) {
        if (id == NotificationCenter.notificationsSettingsUpdated) {
            this.adapter.notifyDataSetChanged();
        }
    }

    public ThemeDescription[] getThemeDescriptions() {
        ThemeDescription[] themeDescriptionArr = new ThemeDescription[22];
        themeDescriptionArr[0] = new ThemeDescription(this.listView, ThemeDescription.FLAG_CELLBACKGROUNDCOLOR, new Class[]{HeaderCell.class, TextCheckCell.class, TextDetailSettingsCell.class, TextColorCell.class, TextSettingsCell.class}, null, null, null, Theme.key_windowBackgroundWhite);
        themeDescriptionArr[1] = new ThemeDescription(this.fragmentView, ThemeDescription.FLAG_BACKGROUND, null, null, null, null, Theme.key_windowBackgroundGray);
        themeDescriptionArr[2] = new ThemeDescription(this.actionBar, ThemeDescription.FLAG_BACKGROUND, null, null, null, null, Theme.key_actionBarDefault);
        themeDescriptionArr[3] = new ThemeDescription(this.listView, ThemeDescription.FLAG_LISTGLOWCOLOR, null, null, null, null, Theme.key_actionBarDefault);
        themeDescriptionArr[4] = new ThemeDescription(this.actionBar, ThemeDescription.FLAG_AB_ITEMSCOLOR, null, null, null, null, Theme.key_actionBarDefaultIcon);
        themeDescriptionArr[5] = new ThemeDescription(this.actionBar, ThemeDescription.FLAG_AB_TITLECOLOR, null, null, null, null, Theme.key_actionBarDefaultTitle);
        themeDescriptionArr[6] = new ThemeDescription(this.actionBar, ThemeDescription.FLAG_AB_SELECTORCOLOR, null, null, null, null, Theme.key_actionBarDefaultSelector);
        themeDescriptionArr[7] = new ThemeDescription(this.listView, ThemeDescription.FLAG_SELECTOR, null, null, null, null, Theme.key_listSelector);
        themeDescriptionArr[8] = new ThemeDescription(this.listView, 0, new Class[]{View.class}, Theme.dividerPaint, null, null, Theme.key_divider);
        themeDescriptionArr[9] = new ThemeDescription(this.listView, 0, new Class[]{HeaderCell.class}, new String[]{"textView"}, null, null, null, Theme.key_windowBackgroundWhiteBlueHeader);
        themeDescriptionArr[10] = new ThemeDescription(this.listView, 0, new Class[]{TextCheckCell.class}, new String[]{"textView"}, null, null, null, Theme.key_windowBackgroundWhiteBlackText);
        themeDescriptionArr[11] = new ThemeDescription(this.listView, 0, new Class[]{TextCheckCell.class}, new String[]{"valueTextView"}, null, null, null, Theme.key_windowBackgroundWhiteGrayText2);
        themeDescriptionArr[12] = new ThemeDescription(this.listView, 0, new Class[]{TextCheckCell.class}, new String[]{"checkBox"}, null, null, null, Theme.key_switchThumb);
        themeDescriptionArr[13] = new ThemeDescription(this.listView, 0, new Class[]{TextCheckCell.class}, new String[]{"checkBox"}, null, null, null, Theme.key_switchTrack);
        themeDescriptionArr[14] = new ThemeDescription(this.listView, 0, new Class[]{TextCheckCell.class}, new String[]{"checkBox"}, null, null, null, Theme.key_switchThumbChecked);
        themeDescriptionArr[15] = new ThemeDescription(this.listView, 0, new Class[]{TextCheckCell.class}, new String[]{"checkBox"}, null, null, null, Theme.key_switchTrackChecked);
        themeDescriptionArr[16] = new ThemeDescription(this.listView, 0, new Class[]{TextColorCell.class}, new String[]{"textView"}, null, null, null, Theme.key_windowBackgroundWhiteBlackText);
        themeDescriptionArr[17] = new ThemeDescription(this.listView, 0, new Class[]{TextSettingsCell.class}, new String[]{"textView"}, null, null, null, Theme.key_windowBackgroundWhiteBlackText);
        themeDescriptionArr[18] = new ThemeDescription(this.listView, 0, new Class[]{TextSettingsCell.class}, new String[]{"valueTextView"}, null, null, null, Theme.key_windowBackgroundWhiteValueText);
        themeDescriptionArr[19] = new ThemeDescription(this.listView, ThemeDescription.FLAG_BACKGROUNDFILTER, new Class[]{ShadowSectionCell.class}, null, null, null, Theme.key_windowBackgroundGrayShadow);
        themeDescriptionArr[20] = new ThemeDescription(this.listView, 0, new Class[]{TextDetailSettingsCell.class}, new String[]{"textView"}, null, null, null, Theme.key_windowBackgroundWhiteBlackText);
        themeDescriptionArr[21] = new ThemeDescription(this.listView, 0, new Class[]{TextDetailSettingsCell.class}, new String[]{"valueTextView"}, null, null, null, Theme.key_windowBackgroundWhiteGrayText2);
        return themeDescriptionArr;
    }
}
