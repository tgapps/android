package org.telegram.ui;

import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Toast;
import java.util.ArrayList;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.ContactsController;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.NotificationCenter;
import org.telegram.messenger.NotificationCenter.NotificationCenterDelegate;
import org.telegram.messenger.SharedConfig;
import org.telegram.messenger.UserConfig;
import org.telegram.messenger.beta.R;
import org.telegram.messenger.support.widget.LinearLayoutManager;
import org.telegram.messenger.support.widget.RecyclerView.ViewHolder;
import org.telegram.tgnet.ConnectionsManager;
import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC.PrivacyRule;
import org.telegram.tgnet.TLRPC.TL_accountDaysTTL;
import org.telegram.tgnet.TLRPC.TL_account_setAccountTTL;
import org.telegram.tgnet.TLRPC.TL_boolTrue;
import org.telegram.tgnet.TLRPC.TL_error;
import org.telegram.tgnet.TLRPC.TL_payments_clearSavedInfo;
import org.telegram.tgnet.TLRPC.TL_privacyValueAllowAll;
import org.telegram.tgnet.TLRPC.TL_privacyValueAllowUsers;
import org.telegram.tgnet.TLRPC.TL_privacyValueDisallowAll;
import org.telegram.tgnet.TLRPC.TL_privacyValueDisallowUsers;
import org.telegram.ui.ActionBar.ActionBar.ActionBarMenuOnItemClick;
import org.telegram.ui.ActionBar.AlertDialog;
import org.telegram.ui.ActionBar.AlertDialog.Builder;
import org.telegram.ui.ActionBar.BaseFragment;
import org.telegram.ui.ActionBar.BottomSheet;
import org.telegram.ui.ActionBar.BottomSheet.BottomSheetCell;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.ActionBar.ThemeDescription;
import org.telegram.ui.Cells.CheckBoxCell;
import org.telegram.ui.Cells.HeaderCell;
import org.telegram.ui.Cells.TextCheckCell;
import org.telegram.ui.Cells.TextInfoPrivacyCell;
import org.telegram.ui.Cells.TextSettingsCell;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.ui.Components.RecyclerListView;
import org.telegram.ui.Components.RecyclerListView.Holder;
import org.telegram.ui.Components.RecyclerListView.OnItemClickListener;
import org.telegram.ui.Components.RecyclerListView.SelectionAdapter;
import org.telegram.ui.Components.voip.VoIPHelper;

public class PrivacySettingsActivity extends BaseFragment implements NotificationCenterDelegate {
    private int blockedRow;
    private int botsDetailRow;
    private int botsSectionRow;
    private int callsDetailRow;
    private int callsP2PRow;
    private int callsRow;
    private int callsSectionRow;
    private boolean[] clear = new boolean[2];
    private int contactsDetailRow;
    private int contactsSectionRow;
    private int contactsSyncRow;
    private boolean currentSync;
    private int deleteAccountDetailRow;
    private int deleteAccountRow;
    private int deleteAccountSectionRow;
    private int groupsDetailRow;
    private int groupsRow;
    private int lastSeenRow;
    private ListAdapter listAdapter;
    private RecyclerListView listView;
    private boolean newSync;
    private int passcodeRow;
    private int passwordRow;
    private int paymentsClearRow;
    private int privacySectionRow;
    private int rowCount;
    private int secretDetailRow;
    private int secretSectionRow;
    private int secretWebpageRow;
    private int securitySectionRow;
    private int sessionsDetailRow;
    private int sessionsRow;
    private int webSessionsRow;

    private class ListAdapter extends SelectionAdapter {
        private Context mContext;

        public void onBindViewHolder(org.telegram.messenger.support.widget.RecyclerView.ViewHolder r1, int r2) {
            /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.DecodeException: Load method exception in method: org.telegram.ui.PrivacySettingsActivity.ListAdapter.onBindViewHolder(org.telegram.messenger.support.widget.RecyclerView$ViewHolder, int):void
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
            r0 = r7.getItemViewType();
            r1 = 2131493124; // 0x7f0c0104 float:1.860972E38 double:1.053097527E-314;
            r2 = 0;
            r3 = 1;
            switch(r0) {
                case 0: goto L_0x01f8;
                case 1: goto L_0x00ef;
                case 2: goto L_0x0054;
                case 3: goto L_0x000e;
                default: goto L_0x000c;
            };
        L_0x000c:
            goto L_0x03c7;
        L_0x000e:
            r0 = r7.itemView;
            r0 = (org.telegram.ui.Cells.TextCheckCell) r0;
            r1 = org.telegram.ui.PrivacySettingsActivity.this;
            r1 = r1.secretWebpageRow;
            if (r8 != r1) goto L_0x0038;
        L_0x001a:
            r1 = "SecretWebPage";
            r4 = 2131494325; // 0x7f0c05b5 float:1.8612155E38 double:1.0530981203E-314;
            r1 = org.telegram.messenger.LocaleController.getString(r1, r4);
            r4 = org.telegram.ui.PrivacySettingsActivity.this;
            r4 = r4.currentAccount;
            r4 = org.telegram.messenger.MessagesController.getInstance(r4);
            r4 = r4.secretWebpagePreview;
            if (r4 != r3) goto L_0x0032;
        L_0x0031:
            goto L_0x0033;
        L_0x0032:
            r3 = r2;
        L_0x0033:
            r0.setTextAndCheck(r1, r3, r2);
            goto L_0x03c7;
        L_0x0038:
            r1 = org.telegram.ui.PrivacySettingsActivity.this;
            r1 = r1.contactsSyncRow;
            if (r8 != r1) goto L_0x03c7;
        L_0x0040:
            r1 = "SyncContacts";
            r3 = 2131494455; // 0x7f0c0637 float:1.8612419E38 double:1.0530981845E-314;
            r1 = org.telegram.messenger.LocaleController.getString(r1, r3);
            r3 = org.telegram.ui.PrivacySettingsActivity.this;
            r3 = r3.newSync;
            r0.setTextAndCheck(r1, r3, r2);
            goto L_0x03c7;
        L_0x0054:
            r0 = r7.itemView;
            r0 = (org.telegram.ui.Cells.HeaderCell) r0;
            r2 = org.telegram.ui.PrivacySettingsActivity.this;
            r2 = r2.privacySectionRow;
            if (r8 != r2) goto L_0x006e;
        L_0x0060:
            r1 = "PrivacyTitle";
            r2 = 2131494203; // 0x7f0c053b float:1.8611908E38 double:1.05309806E-314;
            r1 = org.telegram.messenger.LocaleController.getString(r1, r2);
            r0.setText(r1);
            goto L_0x03c7;
        L_0x006e:
            r2 = org.telegram.ui.PrivacySettingsActivity.this;
            r2 = r2.securitySectionRow;
            if (r8 != r2) goto L_0x0084;
        L_0x0076:
            r1 = "SecurityTitle";
            r2 = 2131494326; // 0x7f0c05b6 float:1.8612157E38 double:1.053098121E-314;
            r1 = org.telegram.messenger.LocaleController.getString(r1, r2);
            r0.setText(r1);
            goto L_0x03c7;
        L_0x0084:
            r2 = org.telegram.ui.PrivacySettingsActivity.this;
            r2 = r2.deleteAccountSectionRow;
            if (r8 != r2) goto L_0x009a;
        L_0x008c:
            r1 = "DeleteAccountTitle";
            r2 = 2131493359; // 0x7f0c01ef float:1.8610196E38 double:1.053097643E-314;
            r1 = org.telegram.messenger.LocaleController.getString(r1, r2);
            r0.setText(r1);
            goto L_0x03c7;
        L_0x009a:
            r2 = org.telegram.ui.PrivacySettingsActivity.this;
            r2 = r2.secretSectionRow;
            if (r8 != r2) goto L_0x00b0;
        L_0x00a2:
            r1 = "SecretChat";
            r2 = 2131494321; // 0x7f0c05b1 float:1.8612147E38 double:1.0530981183E-314;
            r1 = org.telegram.messenger.LocaleController.getString(r1, r2);
            r0.setText(r1);
            goto L_0x03c7;
        L_0x00b0:
            r2 = org.telegram.ui.PrivacySettingsActivity.this;
            r2 = r2.botsSectionRow;
            if (r8 != r2) goto L_0x00c6;
        L_0x00b8:
            r1 = "PrivacyBots";
            r2 = 2131494190; // 0x7f0c052e float:1.8611881E38 double:1.0530980536E-314;
            r1 = org.telegram.messenger.LocaleController.getString(r1, r2);
            r0.setText(r1);
            goto L_0x03c7;
        L_0x00c6:
            r2 = org.telegram.ui.PrivacySettingsActivity.this;
            r2 = r2.callsSectionRow;
            if (r8 != r2) goto L_0x00d9;
        L_0x00ce:
            r2 = "Calls";
            r1 = org.telegram.messenger.LocaleController.getString(r2, r1);
            r0.setText(r1);
            goto L_0x03c7;
        L_0x00d9:
            r1 = org.telegram.ui.PrivacySettingsActivity.this;
            r1 = r1.contactsSectionRow;
            if (r8 != r1) goto L_0x03c7;
        L_0x00e1:
            r1 = "Contacts";
            r2 = 2131493290; // 0x7f0c01aa float:1.8610056E38 double:1.053097609E-314;
            r1 = org.telegram.messenger.LocaleController.getString(r1, r2);
            r0.setText(r1);
            goto L_0x03c7;
        L_0x00ef:
            r0 = r7.itemView;
            r0 = (org.telegram.ui.Cells.TextInfoPrivacyCell) r0;
            r1 = org.telegram.ui.PrivacySettingsActivity.this;
            r1 = r1.deleteAccountDetailRow;
            r2 = 2131165331; // 0x7f070093 float:1.7944876E38 double:1.0529355757E-314;
            if (r8 != r1) goto L_0x0117;
        L_0x00fe:
            r1 = "DeleteAccountHelp";
            r3 = 2131493357; // 0x7f0c01ed float:1.8610192E38 double:1.053097642E-314;
            r1 = org.telegram.messenger.LocaleController.getString(r1, r3);
            r0.setText(r1);
            r1 = r6.mContext;
            r3 = "windowBackgroundGrayShadow";
            r1 = org.telegram.ui.ActionBar.Theme.getThemedDrawable(r1, r2, r3);
            r0.setBackgroundDrawable(r1);
            goto L_0x03c7;
        L_0x0117:
            r1 = org.telegram.ui.PrivacySettingsActivity.this;
            r1 = r1.groupsDetailRow;
            if (r8 != r1) goto L_0x0138;
        L_0x011f:
            r1 = "GroupsAndChannelsHelp";
            r3 = 2131493644; // 0x7f0c030c float:1.8610774E38 double:1.053097784E-314;
            r1 = org.telegram.messenger.LocaleController.getString(r1, r3);
            r0.setText(r1);
            r1 = r6.mContext;
            r3 = "windowBackgroundGrayShadow";
            r1 = org.telegram.ui.ActionBar.Theme.getThemedDrawable(r1, r2, r3);
            r0.setBackgroundDrawable(r1);
            goto L_0x03c7;
        L_0x0138:
            r1 = org.telegram.ui.PrivacySettingsActivity.this;
            r1 = r1.sessionsDetailRow;
            if (r8 != r1) goto L_0x0159;
        L_0x0140:
            r1 = "SessionsInfo";
            r3 = 2131494366; // 0x7f0c05de float:1.8612238E38 double:1.0530981405E-314;
            r1 = org.telegram.messenger.LocaleController.getString(r1, r3);
            r0.setText(r1);
            r1 = r6.mContext;
            r3 = "windowBackgroundGrayShadow";
            r1 = org.telegram.ui.ActionBar.Theme.getThemedDrawable(r1, r2, r3);
            r0.setBackgroundDrawable(r1);
            goto L_0x03c7;
        L_0x0159:
            r1 = org.telegram.ui.PrivacySettingsActivity.this;
            r1 = r1.secretDetailRow;
            if (r8 != r1) goto L_0x0173;
        L_0x0161:
            r1 = "";
            r0.setText(r1);
            r1 = r6.mContext;
            r3 = "windowBackgroundGrayShadow";
            r1 = org.telegram.ui.ActionBar.Theme.getThemedDrawable(r1, r2, r3);
            r0.setBackgroundDrawable(r1);
            goto L_0x03c7;
        L_0x0173:
            r1 = org.telegram.ui.PrivacySettingsActivity.this;
            r1 = r1.botsDetailRow;
            if (r8 != r1) goto L_0x0194;
        L_0x017b:
            r1 = "PrivacyBotsInfo";
            r3 = 2131494191; // 0x7f0c052f float:1.8611883E38 double:1.053098054E-314;
            r1 = org.telegram.messenger.LocaleController.getString(r1, r3);
            r0.setText(r1);
            r1 = r6.mContext;
            r3 = "windowBackgroundGrayShadow";
            r1 = org.telegram.ui.ActionBar.Theme.getThemedDrawable(r1, r2, r3);
            r0.setBackgroundDrawable(r1);
            goto L_0x03c7;
        L_0x0194:
            r1 = org.telegram.ui.PrivacySettingsActivity.this;
            r1 = r1.callsDetailRow;
            if (r8 != r1) goto L_0x01c2;
        L_0x019c:
            r1 = "PrivacyCallsP2PHelp";
            r3 = 2131494192; // 0x7f0c0530 float:1.8611885E38 double:1.0530980546E-314;
            r1 = org.telegram.messenger.LocaleController.getString(r1, r3);
            r0.setText(r1);
            r1 = r6.mContext;
            r3 = org.telegram.ui.PrivacySettingsActivity.this;
            r3 = r3.secretSectionRow;
            r4 = -1;
            if (r3 != r4) goto L_0x01b7;
        L_0x01b3:
            r2 = 2131165332; // 0x7f070094 float:1.7944878E38 double:1.052935576E-314;
        L_0x01b7:
            r3 = "windowBackgroundGrayShadow";
            r1 = org.telegram.ui.ActionBar.Theme.getThemedDrawable(r1, r2, r3);
            r0.setBackgroundDrawable(r1);
            goto L_0x03c7;
        L_0x01c2:
            r1 = org.telegram.ui.PrivacySettingsActivity.this;
            r1 = r1.contactsDetailRow;
            if (r8 != r1) goto L_0x03c7;
        L_0x01ca:
            r1 = org.telegram.ui.PrivacySettingsActivity.this;
            r1 = r1.newSync;
            if (r1 == 0) goto L_0x01df;
        L_0x01d2:
            r1 = "SyncContactsInfoOn";
            r3 = 2131494458; // 0x7f0c063a float:1.8612425E38 double:1.053098186E-314;
            r1 = org.telegram.messenger.LocaleController.getString(r1, r3);
            r0.setText(r1);
            goto L_0x01eb;
        L_0x01df:
            r1 = "SyncContactsInfoOff";
            r3 = 2131494457; // 0x7f0c0639 float:1.8612423E38 double:1.0530981855E-314;
            r1 = org.telegram.messenger.LocaleController.getString(r1, r3);
            r0.setText(r1);
        L_0x01eb:
            r1 = r6.mContext;
            r3 = "windowBackgroundGrayShadow";
            r1 = org.telegram.ui.ActionBar.Theme.getThemedDrawable(r1, r2, r3);
            r0.setBackgroundDrawable(r1);
            goto L_0x03c7;
        L_0x01f8:
            r0 = r7.itemView;
            r0 = (org.telegram.ui.Cells.TextSettingsCell) r0;
            r4 = org.telegram.ui.PrivacySettingsActivity.this;
            r4 = r4.blockedRow;
            if (r8 != r4) goto L_0x0212;
        L_0x0204:
            r1 = "BlockedUsers";
            r2 = 2131493081; // 0x7f0c00d9 float:1.8609632E38 double:1.0530975057E-314;
            r1 = org.telegram.messenger.LocaleController.getString(r1, r2);
            r0.setText(r1, r3);
            goto L_0x03c7;
        L_0x0212:
            r4 = org.telegram.ui.PrivacySettingsActivity.this;
            r4 = r4.sessionsRow;
            if (r8 != r4) goto L_0x0228;
        L_0x021a:
            r1 = "SessionsTitle";
            r3 = 2131494367; // 0x7f0c05df float:1.861224E38 double:1.053098141E-314;
            r1 = org.telegram.messenger.LocaleController.getString(r1, r3);
            r0.setText(r1, r2);
            goto L_0x03c7;
        L_0x0228:
            r4 = org.telegram.ui.PrivacySettingsActivity.this;
            r4 = r4.webSessionsRow;
            if (r8 != r4) goto L_0x023e;
        L_0x0230:
            r1 = "WebSessionsTitle";
            r3 = 2131494623; // 0x7f0c06df float:1.861276E38 double:1.0530982675E-314;
            r1 = org.telegram.messenger.LocaleController.getString(r1, r3);
            r0.setText(r1, r2);
            goto L_0x03c7;
        L_0x023e:
            r4 = org.telegram.ui.PrivacySettingsActivity.this;
            r4 = r4.passwordRow;
            if (r8 != r4) goto L_0x0254;
        L_0x0246:
            r1 = "TwoStepVerification";
            r2 = 2131494501; // 0x7f0c0665 float:1.8612512E38 double:1.053098207E-314;
            r1 = org.telegram.messenger.LocaleController.getString(r1, r2);
            r0.setText(r1, r3);
            goto L_0x03c7;
        L_0x0254:
            r4 = org.telegram.ui.PrivacySettingsActivity.this;
            r4 = r4.passcodeRow;
            if (r8 != r4) goto L_0x026a;
        L_0x025c:
            r1 = "Passcode";
            r2 = 2131494067; // 0x7f0c04b3 float:1.8611632E38 double:1.053097993E-314;
            r1 = org.telegram.messenger.LocaleController.getString(r1, r2);
            r0.setText(r1, r3);
            goto L_0x03c7;
        L_0x026a:
            r4 = org.telegram.ui.PrivacySettingsActivity.this;
            r4 = r4.lastSeenRow;
            r5 = 2131493762; // 0x7f0c0382 float:1.8611013E38 double:1.053097842E-314;
            if (r8 != r4) goto L_0x02a0;
        L_0x0275:
            r1 = org.telegram.ui.PrivacySettingsActivity.this;
            r1 = r1.currentAccount;
            r1 = org.telegram.messenger.ContactsController.getInstance(r1);
            r1 = r1.getLoadingLastSeenInfo();
            if (r1 == 0) goto L_0x028c;
        L_0x0285:
            r1 = "Loading";
            r1 = org.telegram.messenger.LocaleController.getString(r1, r5);
            goto L_0x0292;
        L_0x028c:
            r1 = org.telegram.ui.PrivacySettingsActivity.this;
            r1 = r1.formatRulesString(r2);
        L_0x0292:
            r2 = "PrivacyLastSeen";
            r4 = 2131494197; // 0x7f0c0535 float:1.8611896E38 double:1.053098057E-314;
            r2 = org.telegram.messenger.LocaleController.getString(r2, r4);
            r0.setTextAndValue(r2, r1, r3);
            goto L_0x03c7;
        L_0x02a0:
            r4 = org.telegram.ui.PrivacySettingsActivity.this;
            r4 = r4.callsRow;
            if (r8 != r4) goto L_0x02d1;
        L_0x02a8:
            r2 = org.telegram.ui.PrivacySettingsActivity.this;
            r2 = r2.currentAccount;
            r2 = org.telegram.messenger.ContactsController.getInstance(r2);
            r2 = r2.getLoadingCallsInfo();
            if (r2 == 0) goto L_0x02bf;
        L_0x02b8:
            r2 = "Loading";
            r2 = org.telegram.messenger.LocaleController.getString(r2, r5);
            goto L_0x02c6;
        L_0x02bf:
            r2 = org.telegram.ui.PrivacySettingsActivity.this;
            r4 = 2;
            r2 = r2.formatRulesString(r4);
        L_0x02c6:
            r4 = "Calls";
            r1 = org.telegram.messenger.LocaleController.getString(r4, r1);
            r0.setTextAndValue(r1, r2, r3);
            goto L_0x03c7;
        L_0x02d1:
            r1 = org.telegram.ui.PrivacySettingsActivity.this;
            r1 = r1.groupsRow;
            if (r8 != r1) goto L_0x0304;
        L_0x02d9:
            r1 = org.telegram.ui.PrivacySettingsActivity.this;
            r1 = r1.currentAccount;
            r1 = org.telegram.messenger.ContactsController.getInstance(r1);
            r1 = r1.getLoadingGroupInfo();
            if (r1 == 0) goto L_0x02f0;
        L_0x02e9:
            r1 = "Loading";
            r1 = org.telegram.messenger.LocaleController.getString(r1, r5);
            goto L_0x02f6;
        L_0x02f0:
            r1 = org.telegram.ui.PrivacySettingsActivity.this;
            r1 = r1.formatRulesString(r3);
        L_0x02f6:
            r3 = "GroupsAndChannels";
            r4 = 2131493643; // 0x7f0c030b float:1.8610772E38 double:1.0530977833E-314;
            r3 = org.telegram.messenger.LocaleController.getString(r3, r4);
            r0.setTextAndValue(r3, r1, r2);
            goto L_0x03c7;
        L_0x0304:
            r1 = org.telegram.ui.PrivacySettingsActivity.this;
            r1 = r1.deleteAccountRow;
            if (r8 != r1) goto L_0x035f;
        L_0x030c:
            r1 = org.telegram.ui.PrivacySettingsActivity.this;
            r1 = r1.currentAccount;
            r1 = org.telegram.messenger.ContactsController.getInstance(r1);
            r1 = r1.getLoadingDeleteInfo();
            if (r1 == 0) goto L_0x0323;
        L_0x031c:
            r1 = "Loading";
            r1 = org.telegram.messenger.LocaleController.getString(r1, r5);
            goto L_0x0352;
        L_0x0323:
            r1 = org.telegram.ui.PrivacySettingsActivity.this;
            r1 = r1.currentAccount;
            r1 = org.telegram.messenger.ContactsController.getInstance(r1);
            r1 = r1.getDeleteAccountTTL();
            r3 = 182; // 0xb6 float:2.55E-43 double:9.0E-322;
            if (r1 > r3) goto L_0x033f;
        L_0x0335:
            r3 = "Months";
            r4 = r1 / 30;
            r3 = org.telegram.messenger.LocaleController.formatPluralString(r3, r4);
        L_0x033d:
            r1 = r3;
            goto L_0x0352;
        L_0x033f:
            r3 = 365; // 0x16d float:5.11E-43 double:1.803E-321;
            if (r1 != r3) goto L_0x034c;
        L_0x0343:
            r3 = "Years";
            r4 = r1 / 365;
            r3 = org.telegram.messenger.LocaleController.formatPluralString(r3, r4);
            goto L_0x033d;
        L_0x034c:
            r3 = "Days";
            r1 = org.telegram.messenger.LocaleController.formatPluralString(r3, r1);
        L_0x0352:
            r3 = "DeleteAccountIfAwayFor";
            r4 = 2131493358; // 0x7f0c01ee float:1.8610194E38 double:1.0530976425E-314;
            r3 = org.telegram.messenger.LocaleController.getString(r3, r4);
            r0.setTextAndValue(r3, r1, r2);
            goto L_0x03c7;
        L_0x035f:
            r1 = org.telegram.ui.PrivacySettingsActivity.this;
            r1 = r1.paymentsClearRow;
            if (r8 != r1) goto L_0x0374;
        L_0x0367:
            r1 = "PrivacyPaymentsClear";
            r2 = 2131494198; // 0x7f0c0536 float:1.8611898E38 double:1.0530980575E-314;
            r1 = org.telegram.messenger.LocaleController.getString(r1, r2);
            r0.setText(r1, r3);
            goto L_0x03c7;
        L_0x0374:
            r1 = org.telegram.ui.PrivacySettingsActivity.this;
            r1 = r1.callsP2PRow;
            if (r8 != r1) goto L_0x03c7;
        L_0x037c:
            r1 = org.telegram.ui.PrivacySettingsActivity.this;
            r1 = r1.currentAccount;
            r1 = org.telegram.messenger.MessagesController.getMainSettings(r1);
            r3 = "calls_p2p_new";
            r4 = org.telegram.ui.PrivacySettingsActivity.this;
            r4 = r4.currentAccount;
            r4 = org.telegram.messenger.MessagesController.getInstance(r4);
            r4 = r4.defaultP2pContacts;
            r3 = r1.getInt(r3, r4);
            switch(r3) {
                case 1: goto L_0x03af;
                case 2: goto L_0x03a5;
                default: goto L_0x039b;
            };
        L_0x039b:
            r3 = "LastSeenEverybody";
            r4 = 2131493734; // 0x7f0c0366 float:1.8610957E38 double:1.0530978283E-314;
            r3 = org.telegram.messenger.LocaleController.getString(r3, r4);
            goto L_0x03b9;
        L_0x03a5:
            r3 = "LastSeenNobody";
            r4 = 2131493737; // 0x7f0c0369 float:1.8610963E38 double:1.05309783E-314;
            r3 = org.telegram.messenger.LocaleController.getString(r3, r4);
            goto L_0x03b9;
        L_0x03af:
            r3 = "LastSeenContacts";
            r4 = 2131493728; // 0x7f0c0360 float:1.8610944E38 double:1.0530978253E-314;
            r3 = org.telegram.messenger.LocaleController.getString(r3, r4);
            r4 = "PrivacyCallsP2PTitle";
            r5 = 2131494193; // 0x7f0c0531 float:1.8611887E38 double:1.053098055E-314;
            r4 = org.telegram.messenger.LocaleController.getString(r4, r5);
            r0.setTextAndValue(r4, r3, r2);
        L_0x03c7:
            return;
            */
            throw new UnsupportedOperationException("Method not decompiled: org.telegram.ui.PrivacySettingsActivity.ListAdapter.onBindViewHolder(org.telegram.messenger.support.widget.RecyclerView$ViewHolder, int):void");
        }

        public ListAdapter(Context context) {
            this.mContext = context;
        }

        public boolean isEnabled(ViewHolder holder) {
            int position = holder.getAdapterPosition();
            if (!(position == PrivacySettingsActivity.this.passcodeRow || position == PrivacySettingsActivity.this.passwordRow || position == PrivacySettingsActivity.this.blockedRow || position == PrivacySettingsActivity.this.sessionsRow || position == PrivacySettingsActivity.this.secretWebpageRow || position == PrivacySettingsActivity.this.webSessionsRow || ((position == PrivacySettingsActivity.this.groupsRow && !ContactsController.getInstance(PrivacySettingsActivity.this.currentAccount).getLoadingGroupInfo()) || ((position == PrivacySettingsActivity.this.lastSeenRow && !ContactsController.getInstance(PrivacySettingsActivity.this.currentAccount).getLoadingLastSeenInfo()) || ((position == PrivacySettingsActivity.this.callsRow && !ContactsController.getInstance(PrivacySettingsActivity.this.currentAccount).getLoadingCallsInfo()) || ((position == PrivacySettingsActivity.this.deleteAccountRow && !ContactsController.getInstance(PrivacySettingsActivity.this.currentAccount).getLoadingDeleteInfo()) || position == PrivacySettingsActivity.this.paymentsClearRow || position == PrivacySettingsActivity.this.callsP2PRow)))))) {
                if (position != PrivacySettingsActivity.this.contactsSyncRow) {
                    return false;
                }
            }
            return true;
        }

        public int getItemCount() {
            return PrivacySettingsActivity.this.rowCount;
        }

        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view;
            switch (viewType) {
                case 0:
                    view = new TextSettingsCell(this.mContext);
                    view.setBackgroundColor(Theme.getColor(Theme.key_windowBackgroundWhite));
                    break;
                case 1:
                    view = new TextInfoPrivacyCell(this.mContext);
                    break;
                case 2:
                    view = new HeaderCell(this.mContext);
                    view.setBackgroundColor(Theme.getColor(Theme.key_windowBackgroundWhite));
                    break;
                default:
                    view = new TextCheckCell(this.mContext);
                    view.setBackgroundColor(Theme.getColor(Theme.key_windowBackgroundWhite));
                    break;
            }
            return new Holder(view);
        }

        public int getItemViewType(int position) {
            if (!(position == PrivacySettingsActivity.this.lastSeenRow || position == PrivacySettingsActivity.this.blockedRow || position == PrivacySettingsActivity.this.deleteAccountRow || position == PrivacySettingsActivity.this.sessionsRow || position == PrivacySettingsActivity.this.webSessionsRow || position == PrivacySettingsActivity.this.passwordRow || position == PrivacySettingsActivity.this.passcodeRow || position == PrivacySettingsActivity.this.groupsRow || position == PrivacySettingsActivity.this.paymentsClearRow)) {
                if (position != PrivacySettingsActivity.this.callsP2PRow) {
                    if (!(position == PrivacySettingsActivity.this.deleteAccountDetailRow || position == PrivacySettingsActivity.this.groupsDetailRow || position == PrivacySettingsActivity.this.sessionsDetailRow || position == PrivacySettingsActivity.this.secretDetailRow || position == PrivacySettingsActivity.this.botsDetailRow || position == PrivacySettingsActivity.this.callsDetailRow)) {
                        if (position != PrivacySettingsActivity.this.contactsDetailRow) {
                            if (!(position == PrivacySettingsActivity.this.securitySectionRow || position == PrivacySettingsActivity.this.deleteAccountSectionRow || position == PrivacySettingsActivity.this.privacySectionRow || position == PrivacySettingsActivity.this.secretSectionRow || position == PrivacySettingsActivity.this.botsSectionRow || position == PrivacySettingsActivity.this.callsSectionRow)) {
                                if (position != PrivacySettingsActivity.this.contactsSectionRow) {
                                    if (position != PrivacySettingsActivity.this.secretWebpageRow) {
                                        if (position != PrivacySettingsActivity.this.contactsSyncRow) {
                                            return 0;
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

    public boolean onFragmentCreate() {
        super.onFragmentCreate();
        ContactsController.getInstance(this.currentAccount).loadPrivacySettings();
        boolean z = UserConfig.getInstance(this.currentAccount).syncContacts;
        this.newSync = z;
        this.currentSync = z;
        this.rowCount = 0;
        int i = this.rowCount;
        this.rowCount = i + 1;
        this.privacySectionRow = i;
        i = this.rowCount;
        this.rowCount = i + 1;
        this.blockedRow = i;
        i = this.rowCount;
        this.rowCount = i + 1;
        this.lastSeenRow = i;
        i = this.rowCount;
        this.rowCount = i + 1;
        this.callsRow = i;
        i = this.rowCount;
        this.rowCount = i + 1;
        this.groupsRow = i;
        i = this.rowCount;
        this.rowCount = i + 1;
        this.groupsDetailRow = i;
        i = this.rowCount;
        this.rowCount = i + 1;
        this.securitySectionRow = i;
        i = this.rowCount;
        this.rowCount = i + 1;
        this.passcodeRow = i;
        i = this.rowCount;
        this.rowCount = i + 1;
        this.passwordRow = i;
        i = this.rowCount;
        this.rowCount = i + 1;
        this.sessionsRow = i;
        i = this.rowCount;
        this.rowCount = i + 1;
        this.sessionsDetailRow = i;
        i = this.rowCount;
        this.rowCount = i + 1;
        this.deleteAccountSectionRow = i;
        i = this.rowCount;
        this.rowCount = i + 1;
        this.deleteAccountRow = i;
        i = this.rowCount;
        this.rowCount = i + 1;
        this.deleteAccountDetailRow = i;
        i = this.rowCount;
        this.rowCount = i + 1;
        this.botsSectionRow = i;
        i = this.rowCount;
        this.rowCount = i + 1;
        this.paymentsClearRow = i;
        i = this.rowCount;
        this.rowCount = i + 1;
        this.webSessionsRow = i;
        i = this.rowCount;
        this.rowCount = i + 1;
        this.botsDetailRow = i;
        i = this.rowCount;
        this.rowCount = i + 1;
        this.contactsSectionRow = i;
        i = this.rowCount;
        this.rowCount = i + 1;
        this.contactsSyncRow = i;
        i = this.rowCount;
        this.rowCount = i + 1;
        this.contactsDetailRow = i;
        i = this.rowCount;
        this.rowCount = i + 1;
        this.callsSectionRow = i;
        i = this.rowCount;
        this.rowCount = i + 1;
        this.callsP2PRow = i;
        i = this.rowCount;
        this.rowCount = i + 1;
        this.callsDetailRow = i;
        if (MessagesController.getInstance(this.currentAccount).secretWebpagePreview != 1) {
            i = this.rowCount;
            this.rowCount = i + 1;
            this.secretSectionRow = i;
            i = this.rowCount;
            this.rowCount = i + 1;
            this.secretWebpageRow = i;
            i = this.rowCount;
            this.rowCount = i + 1;
            this.secretDetailRow = i;
        } else {
            this.secretSectionRow = -1;
            this.secretWebpageRow = -1;
            this.secretDetailRow = -1;
        }
        NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.privacyRulesUpdated);
        VoIPHelper.upgradeP2pSetting(this.currentAccount);
        return true;
    }

    public void onFragmentDestroy() {
        super.onFragmentDestroy();
        NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.privacyRulesUpdated);
        if (this.currentSync != this.newSync) {
            UserConfig.getInstance(this.currentAccount).syncContacts = this.newSync;
            UserConfig.getInstance(this.currentAccount).saveConfig(false);
            if (this.newSync) {
                ContactsController.getInstance(this.currentAccount).forceImportContacts();
                if (getParentActivity() != null) {
                    Toast.makeText(getParentActivity(), LocaleController.getString("SyncContactsAdded", R.string.SyncContactsAdded), 0).show();
                }
            }
        }
    }

    public View createView(Context context) {
        this.actionBar.setBackButtonImage(R.drawable.ic_ab_back);
        this.actionBar.setAllowOverlayTitle(true);
        this.actionBar.setTitle(LocaleController.getString("PrivacySettings", R.string.PrivacySettings));
        this.actionBar.setActionBarMenuOnItemClick(new ActionBarMenuOnItemClick() {
            public void onItemClick(int id) {
                if (id == -1) {
                    PrivacySettingsActivity.this.finishFragment();
                }
            }
        });
        this.listAdapter = new ListAdapter(context);
        this.fragmentView = new FrameLayout(context);
        FrameLayout frameLayout = this.fragmentView;
        frameLayout.setBackgroundColor(Theme.getColor(Theme.key_windowBackgroundGray));
        this.listView = new RecyclerListView(context);
        this.listView.setLayoutManager(new LinearLayoutManager(context, 1, false) {
            public boolean supportsPredictiveItemAnimations() {
                return false;
            }
        });
        this.listView.setVerticalScrollBarEnabled(false);
        this.listView.setItemAnimator(null);
        this.listView.setLayoutAnimation(null);
        frameLayout.addView(this.listView, LayoutHelper.createFrame(-1, -1.0f));
        this.listView.setAdapter(this.listAdapter);
        this.listView.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(View view, int position) {
                if (view.isEnabled()) {
                    if (position == PrivacySettingsActivity.this.blockedRow) {
                        PrivacySettingsActivity.this.presentFragment(new BlockedUsersActivity());
                    } else {
                        boolean z = false;
                        if (position == PrivacySettingsActivity.this.sessionsRow) {
                            PrivacySettingsActivity.this.presentFragment(new SessionsActivity(0));
                        } else if (position == PrivacySettingsActivity.this.webSessionsRow) {
                            PrivacySettingsActivity.this.presentFragment(new SessionsActivity(1));
                        } else if (position == PrivacySettingsActivity.this.deleteAccountRow) {
                            if (PrivacySettingsActivity.this.getParentActivity() != null) {
                                Builder builder = new Builder(PrivacySettingsActivity.this.getParentActivity());
                                builder.setTitle(LocaleController.getString("DeleteAccountTitle", R.string.DeleteAccountTitle));
                                builder.setItems(new CharSequence[]{LocaleController.formatPluralString("Months", 1), LocaleController.formatPluralString("Months", 3), LocaleController.formatPluralString("Months", 6), LocaleController.formatPluralString("Years", 1)}, new OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        int value = 0;
                                        if (which == 0) {
                                            value = 30;
                                        } else if (which == 1) {
                                            value = 90;
                                        } else if (which == 2) {
                                            value = 182;
                                        } else if (which == 3) {
                                            value = 365;
                                        }
                                        final AlertDialog progressDialog = new AlertDialog(PrivacySettingsActivity.this.getParentActivity(), 1);
                                        progressDialog.setMessage(LocaleController.getString("Loading", R.string.Loading));
                                        progressDialog.setCanceledOnTouchOutside(false);
                                        progressDialog.setCancelable(false);
                                        progressDialog.show();
                                        final TL_account_setAccountTTL req = new TL_account_setAccountTTL();
                                        req.ttl = new TL_accountDaysTTL();
                                        req.ttl.days = value;
                                        ConnectionsManager.getInstance(PrivacySettingsActivity.this.currentAccount).sendRequest(req, new RequestDelegate() {
                                            public void run(final TLObject response, TL_error error) {
                                                AndroidUtilities.runOnUIThread(new Runnable() {
                                                    public void run() {
                                                        try {
                                                            progressDialog.dismiss();
                                                        } catch (Throwable e) {
                                                            FileLog.e(e);
                                                        }
                                                        if (response instanceof TL_boolTrue) {
                                                            ContactsController.getInstance(PrivacySettingsActivity.this.currentAccount).setDeleteAccountTTL(req.ttl.days);
                                                            PrivacySettingsActivity.this.listAdapter.notifyDataSetChanged();
                                                        }
                                                    }
                                                });
                                            }
                                        });
                                    }
                                });
                                builder.setNegativeButton(LocaleController.getString("Cancel", R.string.Cancel), null);
                                PrivacySettingsActivity.this.showDialog(builder.create());
                            }
                        } else if (position == PrivacySettingsActivity.this.lastSeenRow) {
                            PrivacySettingsActivity.this.presentFragment(new PrivacyControlActivity(0));
                        } else if (position == PrivacySettingsActivity.this.callsRow) {
                            PrivacySettingsActivity.this.presentFragment(new PrivacyControlActivity(2));
                        } else if (position == PrivacySettingsActivity.this.groupsRow) {
                            PrivacySettingsActivity.this.presentFragment(new PrivacyControlActivity(1));
                        } else if (position == PrivacySettingsActivity.this.passwordRow) {
                            PrivacySettingsActivity.this.presentFragment(new TwoStepVerificationActivity(0));
                        } else if (position == PrivacySettingsActivity.this.passcodeRow) {
                            if (SharedConfig.passcodeHash.length() > 0) {
                                PrivacySettingsActivity.this.presentFragment(new PasscodeActivity(2));
                            } else {
                                PrivacySettingsActivity.this.presentFragment(new PasscodeActivity(0));
                            }
                        } else if (position == PrivacySettingsActivity.this.secretWebpageRow) {
                            if (MessagesController.getInstance(PrivacySettingsActivity.this.currentAccount).secretWebpagePreview == 1) {
                                MessagesController.getInstance(PrivacySettingsActivity.this.currentAccount).secretWebpagePreview = 0;
                            } else {
                                MessagesController.getInstance(PrivacySettingsActivity.this.currentAccount).secretWebpagePreview = 1;
                            }
                            MessagesController.getGlobalMainSettings().edit().putInt("secretWebpage2", MessagesController.getInstance(PrivacySettingsActivity.this.currentAccount).secretWebpagePreview).commit();
                            if (view instanceof TextCheckCell) {
                                TextCheckCell textCheckCell = (TextCheckCell) view;
                                if (MessagesController.getInstance(PrivacySettingsActivity.this.currentAccount).secretWebpagePreview == 1) {
                                    z = true;
                                }
                                textCheckCell.setChecked(z);
                            }
                        } else if (position == PrivacySettingsActivity.this.contactsSyncRow) {
                            PrivacySettingsActivity.this.newSync = PrivacySettingsActivity.this.newSync ^ true;
                            if (view instanceof TextCheckCell) {
                                ((TextCheckCell) view).setChecked(PrivacySettingsActivity.this.newSync);
                            }
                            PrivacySettingsActivity.this.listAdapter.notifyItemChanged(PrivacySettingsActivity.this.contactsDetailRow);
                        } else if (position == PrivacySettingsActivity.this.callsP2PRow) {
                            new Builder(PrivacySettingsActivity.this.getParentActivity()).setTitle(LocaleController.getString("PrivacyCallsP2PTitle", R.string.PrivacyCallsP2PTitle)).setItems(new String[]{LocaleController.getString("LastSeenEverybody", R.string.LastSeenEverybody), LocaleController.getString("LastSeenContacts", R.string.LastSeenContacts), LocaleController.getString("LastSeenNobody", R.string.LastSeenNobody)}, new OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    MessagesController.getMainSettings(PrivacySettingsActivity.this.currentAccount).edit().putInt("calls_p2p_new", which).commit();
                                    PrivacySettingsActivity.this.listAdapter.notifyDataSetChanged();
                                }
                            }).setNegativeButton(LocaleController.getString("Cancel", R.string.Cancel), null).show();
                        } else if (position == PrivacySettingsActivity.this.paymentsClearRow) {
                            BottomSheet.Builder builder2 = new BottomSheet.Builder(PrivacySettingsActivity.this.getParentActivity());
                            builder2.setApplyTopPadding(false);
                            builder2.setApplyBottomPadding(false);
                            LinearLayout linearLayout = new LinearLayout(PrivacySettingsActivity.this.getParentActivity());
                            linearLayout.setOrientation(1);
                            for (int a = 0; a < 2; a++) {
                                String name = null;
                                if (a == 0) {
                                    name = LocaleController.getString("PrivacyClearShipping", R.string.PrivacyClearShipping);
                                } else if (a == 1) {
                                    name = LocaleController.getString("PrivacyClearPayment", R.string.PrivacyClearPayment);
                                }
                                PrivacySettingsActivity.this.clear[a] = true;
                                CheckBoxCell checkBoxCell = new CheckBoxCell(PrivacySettingsActivity.this.getParentActivity(), 1);
                                checkBoxCell.setTag(Integer.valueOf(a));
                                checkBoxCell.setBackgroundDrawable(Theme.getSelectorDrawable(false));
                                linearLayout.addView(checkBoxCell, LayoutHelper.createLinear(-1, 48));
                                checkBoxCell.setText(name, null, true, true);
                                checkBoxCell.setTextColor(Theme.getColor(Theme.key_dialogTextBlack));
                                checkBoxCell.setOnClickListener(new View.OnClickListener() {
                                    public void onClick(View v) {
                                        CheckBoxCell cell = (CheckBoxCell) v;
                                        int num = ((Integer) cell.getTag()).intValue();
                                        PrivacySettingsActivity.this.clear[num] = PrivacySettingsActivity.this.clear[num] ^ true;
                                        cell.setChecked(PrivacySettingsActivity.this.clear[num], true);
                                    }
                                });
                            }
                            BottomSheetCell cell = new BottomSheetCell(PrivacySettingsActivity.this.getParentActivity(), 1);
                            cell.setBackgroundDrawable(Theme.getSelectorDrawable(false));
                            cell.setTextAndIcon(LocaleController.getString("ClearButton", R.string.ClearButton).toUpperCase(), 0);
                            cell.setTextColor(Theme.getColor(Theme.key_windowBackgroundWhiteRedText));
                            cell.setOnClickListener(new View.OnClickListener() {
                                public void onClick(View v) {
                                    try {
                                        if (PrivacySettingsActivity.this.visibleDialog != null) {
                                            PrivacySettingsActivity.this.visibleDialog.dismiss();
                                        }
                                    } catch (Throwable e) {
                                        FileLog.e(e);
                                    }
                                    Builder builder = new Builder(PrivacySettingsActivity.this.getParentActivity());
                                    builder.setTitle(LocaleController.getString("AppName", R.string.AppName));
                                    builder.setMessage(LocaleController.getString("PrivacyPaymentsClearAlert", R.string.PrivacyPaymentsClearAlert));
                                    builder.setPositiveButton(LocaleController.getString("OK", R.string.OK), new OnClickListener() {
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            TL_payments_clearSavedInfo req = new TL_payments_clearSavedInfo();
                                            req.credentials = PrivacySettingsActivity.this.clear[1];
                                            req.info = PrivacySettingsActivity.this.clear[0];
                                            UserConfig.getInstance(PrivacySettingsActivity.this.currentAccount).tmpPassword = null;
                                            UserConfig.getInstance(PrivacySettingsActivity.this.currentAccount).saveConfig(false);
                                            ConnectionsManager.getInstance(PrivacySettingsActivity.this.currentAccount).sendRequest(req, new RequestDelegate() {
                                                public void run(TLObject response, TL_error error) {
                                                }
                                            });
                                        }
                                    });
                                    builder.setNegativeButton(LocaleController.getString("Cancel", R.string.Cancel), null);
                                    PrivacySettingsActivity.this.showDialog(builder.create());
                                }
                            });
                            linearLayout.addView(cell, LayoutHelper.createLinear(-1, 48));
                            builder2.setCustomView(linearLayout);
                            PrivacySettingsActivity.this.showDialog(builder2.create());
                        }
                    }
                }
            }
        });
        return this.fragmentView;
    }

    public void didReceivedNotification(int id, int account, Object... args) {
        if (id == NotificationCenter.privacyRulesUpdated && this.listAdapter != null) {
            this.listAdapter.notifyDataSetChanged();
        }
    }

    private String formatRulesString(int rulesType) {
        ArrayList<PrivacyRule> privacyRules = ContactsController.getInstance(this.currentAccount).getPrivacyRules(rulesType);
        if (privacyRules.size() == 0) {
            return LocaleController.getString("LastSeenNobody", R.string.LastSeenNobody);
        }
        int minus = 0;
        int plus = 0;
        int type = -1;
        for (int a = 0; a < privacyRules.size(); a++) {
            PrivacyRule rule = (PrivacyRule) privacyRules.get(a);
            if (rule instanceof TL_privacyValueAllowUsers) {
                plus += rule.users.size();
            } else if (rule instanceof TL_privacyValueDisallowUsers) {
                minus += rule.users.size();
            } else if (rule instanceof TL_privacyValueAllowAll) {
                type = 0;
            } else if (rule instanceof TL_privacyValueDisallowAll) {
                type = 1;
            } else {
                type = 2;
            }
        }
        if (type != 0) {
            if (type != -1 || minus <= 0) {
                if (type != 2) {
                    if (type != -1 || minus <= 0 || plus <= 0) {
                        if (type != 1) {
                            if (plus <= 0) {
                                return "unknown";
                            }
                        }
                        if (plus == 0) {
                            return LocaleController.getString("LastSeenNobody", R.string.LastSeenNobody);
                        }
                        return LocaleController.formatString("LastSeenNobodyPlus", R.string.LastSeenNobodyPlus, Integer.valueOf(plus));
                    }
                }
                if (plus == 0 && minus == 0) {
                    return LocaleController.getString("LastSeenContacts", R.string.LastSeenContacts);
                }
                if (plus != 0 && minus != 0) {
                    return LocaleController.formatString("LastSeenContactsMinusPlus", R.string.LastSeenContactsMinusPlus, Integer.valueOf(minus), Integer.valueOf(plus));
                } else if (minus != 0) {
                    return LocaleController.formatString("LastSeenContactsMinus", R.string.LastSeenContactsMinus, Integer.valueOf(minus));
                } else {
                    return LocaleController.formatString("LastSeenContactsPlus", R.string.LastSeenContactsPlus, Integer.valueOf(plus));
                }
            }
        }
        if (minus == 0) {
            return LocaleController.getString("LastSeenEverybody", R.string.LastSeenEverybody);
        }
        return LocaleController.formatString("LastSeenEverybodyMinus", R.string.LastSeenEverybodyMinus, Integer.valueOf(minus));
    }

    public void onResume() {
        super.onResume();
        if (this.listAdapter != null) {
            this.listAdapter.notifyDataSetChanged();
        }
    }

    public ThemeDescription[] getThemeDescriptions() {
        r1 = new ThemeDescription[20];
        r1[0] = new ThemeDescription(this.listView, ThemeDescription.FLAG_CELLBACKGROUNDCOLOR, new Class[]{TextSettingsCell.class, HeaderCell.class, TextCheckCell.class}, null, null, null, Theme.key_windowBackgroundWhite);
        r1[1] = new ThemeDescription(this.fragmentView, ThemeDescription.FLAG_BACKGROUND, null, null, null, null, Theme.key_windowBackgroundGray);
        r1[2] = new ThemeDescription(this.actionBar, ThemeDescription.FLAG_BACKGROUND, null, null, null, null, Theme.key_actionBarDefault);
        r1[3] = new ThemeDescription(this.listView, ThemeDescription.FLAG_LISTGLOWCOLOR, null, null, null, null, Theme.key_actionBarDefault);
        r1[4] = new ThemeDescription(this.actionBar, ThemeDescription.FLAG_AB_ITEMSCOLOR, null, null, null, null, Theme.key_actionBarDefaultIcon);
        r1[5] = new ThemeDescription(this.actionBar, ThemeDescription.FLAG_AB_TITLECOLOR, null, null, null, null, Theme.key_actionBarDefaultTitle);
        r1[6] = new ThemeDescription(this.actionBar, ThemeDescription.FLAG_AB_SELECTORCOLOR, null, null, null, null, Theme.key_actionBarDefaultSelector);
        r1[7] = new ThemeDescription(this.listView, ThemeDescription.FLAG_SELECTOR, null, null, null, null, Theme.key_listSelector);
        r1[8] = new ThemeDescription(this.listView, 0, new Class[]{View.class}, Theme.dividerPaint, null, null, Theme.key_divider);
        r1[9] = new ThemeDescription(this.listView, 0, new Class[]{TextSettingsCell.class}, new String[]{"textView"}, null, null, null, Theme.key_windowBackgroundWhiteBlackText);
        r1[10] = new ThemeDescription(this.listView, 0, new Class[]{TextSettingsCell.class}, new String[]{"valueTextView"}, null, null, null, Theme.key_windowBackgroundWhiteValueText);
        r1[11] = new ThemeDescription(this.listView, 0, new Class[]{HeaderCell.class}, new String[]{"textView"}, null, null, null, Theme.key_windowBackgroundWhiteBlueHeader);
        r1[12] = new ThemeDescription(this.listView, ThemeDescription.FLAG_BACKGROUNDFILTER, new Class[]{TextInfoPrivacyCell.class}, null, null, null, Theme.key_windowBackgroundGrayShadow);
        r1[13] = new ThemeDescription(this.listView, 0, new Class[]{TextInfoPrivacyCell.class}, new String[]{"textView"}, null, null, null, Theme.key_windowBackgroundWhiteGrayText4);
        r1[14] = new ThemeDescription(this.listView, 0, new Class[]{TextCheckCell.class}, new String[]{"textView"}, null, null, null, Theme.key_windowBackgroundWhiteBlackText);
        r1[15] = new ThemeDescription(this.listView, 0, new Class[]{TextCheckCell.class}, new String[]{"valueTextView"}, null, null, null, Theme.key_windowBackgroundWhiteGrayText2);
        r1[16] = new ThemeDescription(this.listView, 0, new Class[]{TextCheckCell.class}, new String[]{"checkBox"}, null, null, null, Theme.key_switchThumb);
        r1[17] = new ThemeDescription(this.listView, 0, new Class[]{TextCheckCell.class}, new String[]{"checkBox"}, null, null, null, Theme.key_switchTrack);
        r1[18] = new ThemeDescription(this.listView, 0, new Class[]{TextCheckCell.class}, new String[]{"checkBox"}, null, null, null, Theme.key_switchThumbChecked);
        r1[19] = new ThemeDescription(this.listView, 0, new Class[]{TextCheckCell.class}, new String[]{"checkBox"}, null, null, null, Theme.key_switchTrackChecked);
        return r1;
    }
}
