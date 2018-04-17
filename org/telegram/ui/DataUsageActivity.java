package org.telegram.ui;

import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.StatsController;
import org.telegram.messenger.beta.R;
import org.telegram.messenger.support.widget.LinearLayoutManager;
import org.telegram.messenger.support.widget.RecyclerView.LayoutParams;
import org.telegram.messenger.support.widget.RecyclerView.ViewHolder;
import org.telegram.ui.ActionBar.ActionBar.ActionBarMenuOnItemClick;
import org.telegram.ui.ActionBar.AlertDialog.Builder;
import org.telegram.ui.ActionBar.BaseFragment;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.ActionBar.ThemeDescription;
import org.telegram.ui.Cells.HeaderCell;
import org.telegram.ui.Cells.ShadowSectionCell;
import org.telegram.ui.Cells.TextInfoPrivacyCell;
import org.telegram.ui.Cells.TextSettingsCell;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.ui.Components.RecyclerListView;
import org.telegram.ui.Components.RecyclerListView.Holder;
import org.telegram.ui.Components.RecyclerListView.OnItemClickListener;
import org.telegram.ui.Components.RecyclerListView.SelectionAdapter;

public class DataUsageActivity extends BaseFragment {
    private int audiosBytesReceivedRow;
    private int audiosBytesSentRow;
    private int audiosReceivedRow;
    private int audiosSection2Row;
    private int audiosSectionRow;
    private int audiosSentRow;
    private int callsBytesReceivedRow;
    private int callsBytesSentRow;
    private int callsReceivedRow;
    private int callsSection2Row;
    private int callsSectionRow;
    private int callsSentRow;
    private int callsTotalTimeRow;
    private int currentType;
    private int filesBytesReceivedRow;
    private int filesBytesSentRow;
    private int filesReceivedRow;
    private int filesSection2Row;
    private int filesSectionRow;
    private int filesSentRow;
    private ListAdapter listAdapter;
    private RecyclerListView listView;
    private int messagesBytesReceivedRow;
    private int messagesBytesSentRow;
    private int messagesReceivedRow = -1;
    private int messagesSection2Row;
    private int messagesSectionRow;
    private int messagesSentRow = -1;
    private int photosBytesReceivedRow;
    private int photosBytesSentRow;
    private int photosReceivedRow;
    private int photosSection2Row;
    private int photosSectionRow;
    private int photosSentRow;
    private int resetRow;
    private int resetSection2Row;
    private int rowCount;
    private int totalBytesReceivedRow;
    private int totalBytesSentRow;
    private int totalSection2Row;
    private int totalSectionRow;
    private int videosBytesReceivedRow;
    private int videosBytesSentRow;
    private int videosReceivedRow;
    private int videosSection2Row;
    private int videosSectionRow;
    private int videosSentRow;

    private class ListAdapter extends SelectionAdapter {
        private Context mContext;

        public void onBindViewHolder(org.telegram.messenger.support.widget.RecyclerView.ViewHolder r1, int r2) {
            /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.DecodeException: Load method exception in method: org.telegram.ui.DataUsageActivity.ListAdapter.onBindViewHolder(org.telegram.messenger.support.widget.RecyclerView$ViewHolder, int):void
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
            r0 = r12.getItemViewType();
            r1 = 2131165332; // 0x7f070094 float:1.7944878E38 double:1.052935576E-314;
            r2 = 0;
            r3 = 1;
            switch(r0) {
                case 0: goto L_0x0447;
                case 1: goto L_0x00eb;
                case 2: goto L_0x004d;
                case 3: goto L_0x000e;
                default: goto L_0x000c;
            };
        L_0x000c:
            goto L_0x046e;
        L_0x000e:
            r0 = r12.itemView;
            r0 = (org.telegram.ui.Cells.TextInfoPrivacyCell) r0;
            r4 = r11.mContext;
            r5 = "windowBackgroundGrayShadow";
            r1 = org.telegram.ui.ActionBar.Theme.getThemedDrawable(r4, r1, r5);
            r0.setBackgroundDrawable(r1);
            r1 = "NetworkUsageSince";
            r4 = 2131493860; // 0x7f0c03e4 float:1.8611212E38 double:1.0530978905E-314;
            r3 = new java.lang.Object[r3];
            r5 = org.telegram.messenger.LocaleController.getInstance();
            r5 = r5.formatterStats;
            r6 = org.telegram.ui.DataUsageActivity.this;
            r6 = r6.currentAccount;
            r6 = org.telegram.messenger.StatsController.getInstance(r6);
            r7 = org.telegram.ui.DataUsageActivity.this;
            r7 = r7.currentType;
            r6 = r6.getResetStatsDate(r7);
            r5 = r5.format(r6);
            r3[r2] = r5;
            r1 = org.telegram.messenger.LocaleController.formatString(r1, r4, r3);
            r0.setText(r1);
            goto L_0x046e;
        L_0x004d:
            r0 = r12.itemView;
            r0 = (org.telegram.ui.Cells.HeaderCell) r0;
            r1 = org.telegram.ui.DataUsageActivity.this;
            r1 = r1.totalSectionRow;
            if (r13 != r1) goto L_0x0067;
        L_0x0059:
            r1 = "TotalDataUsage";
            r2 = 2131494498; // 0x7f0c0662 float:1.8612506E38 double:1.053098206E-314;
            r1 = org.telegram.messenger.LocaleController.getString(r1, r2);
            r0.setText(r1);
            goto L_0x046e;
        L_0x0067:
            r1 = org.telegram.ui.DataUsageActivity.this;
            r1 = r1.callsSectionRow;
            if (r13 != r1) goto L_0x007d;
        L_0x006f:
            r1 = "CallsDataUsage";
            r2 = 2131493125; // 0x7f0c0105 float:1.8609721E38 double:1.0530975274E-314;
            r1 = org.telegram.messenger.LocaleController.getString(r1, r2);
            r0.setText(r1);
            goto L_0x046e;
        L_0x007d:
            r1 = org.telegram.ui.DataUsageActivity.this;
            r1 = r1.filesSectionRow;
            if (r13 != r1) goto L_0x0093;
        L_0x0085:
            r1 = "FilesDataUsage";
            r2 = 2131493538; // 0x7f0c02a2 float:1.8610559E38 double:1.0530977315E-314;
            r1 = org.telegram.messenger.LocaleController.getString(r1, r2);
            r0.setText(r1);
            goto L_0x046e;
        L_0x0093:
            r1 = org.telegram.ui.DataUsageActivity.this;
            r1 = r1.audiosSectionRow;
            if (r13 != r1) goto L_0x00a9;
        L_0x009b:
            r1 = "LocalAudioCache";
            r2 = 2131493763; // 0x7f0c0383 float:1.8611015E38 double:1.0530978426E-314;
            r1 = org.telegram.messenger.LocaleController.getString(r1, r2);
            r0.setText(r1);
            goto L_0x046e;
        L_0x00a9:
            r1 = org.telegram.ui.DataUsageActivity.this;
            r1 = r1.videosSectionRow;
            if (r13 != r1) goto L_0x00bf;
        L_0x00b1:
            r1 = "LocalVideoCache";
            r2 = 2131493772; // 0x7f0c038c float:1.8611034E38 double:1.053097847E-314;
            r1 = org.telegram.messenger.LocaleController.getString(r1, r2);
            r0.setText(r1);
            goto L_0x046e;
        L_0x00bf:
            r1 = org.telegram.ui.DataUsageActivity.this;
            r1 = r1.photosSectionRow;
            if (r13 != r1) goto L_0x00d5;
        L_0x00c7:
            r1 = "LocalPhotoCache";
            r2 = 2131493771; // 0x7f0c038b float:1.8611032E38 double:1.0530978466E-314;
            r1 = org.telegram.messenger.LocaleController.getString(r1, r2);
            r0.setText(r1);
            goto L_0x046e;
        L_0x00d5:
            r1 = org.telegram.ui.DataUsageActivity.this;
            r1 = r1.messagesSectionRow;
            if (r13 != r1) goto L_0x046e;
        L_0x00dd:
            r1 = "MessagesDataUsage";
            r2 = 2131493827; // 0x7f0c03c3 float:1.8611145E38 double:1.053097874E-314;
            r1 = org.telegram.messenger.LocaleController.getString(r1, r2);
            r0.setText(r1);
            goto L_0x046e;
        L_0x00eb:
            r0 = r12.itemView;
            r0 = (org.telegram.ui.Cells.TextSettingsCell) r0;
            r1 = org.telegram.ui.DataUsageActivity.this;
            r1 = r1.resetRow;
            if (r13 != r1) goto L_0x0113;
        L_0x00f7:
            r1 = "windowBackgroundWhiteRedText2";
            r0.setTag(r1);
            r1 = "ResetStatistics";
            r3 = 2131494265; // 0x7f0c0579 float:1.8612034E38 double:1.0530980906E-314;
            r1 = org.telegram.messenger.LocaleController.getString(r1, r3);
            r0.setText(r1, r2);
            r1 = "windowBackgroundWhiteRedText2";
            r1 = org.telegram.ui.ActionBar.Theme.getColor(r1);
            r0.setTextColor(r1);
            goto L_0x046e;
        L_0x0113:
            r1 = "windowBackgroundWhiteBlackText";
            r0.setTag(r1);
            r1 = "windowBackgroundWhiteBlackText";
            r1 = org.telegram.ui.ActionBar.Theme.getColor(r1);
            r0.setTextColor(r1);
            r1 = org.telegram.ui.DataUsageActivity.this;
            r1 = r1.callsSentRow;
            if (r13 == r1) goto L_0x01f6;
        L_0x0129:
            r1 = org.telegram.ui.DataUsageActivity.this;
            r1 = r1.callsReceivedRow;
            if (r13 == r1) goto L_0x01f6;
        L_0x0131:
            r1 = org.telegram.ui.DataUsageActivity.this;
            r1 = r1.callsBytesSentRow;
            if (r13 == r1) goto L_0x01f6;
        L_0x0139:
            r1 = org.telegram.ui.DataUsageActivity.this;
            r1 = r1.callsBytesReceivedRow;
            if (r13 != r1) goto L_0x0143;
        L_0x0141:
            goto L_0x01f6;
        L_0x0143:
            r1 = org.telegram.ui.DataUsageActivity.this;
            r1 = r1.messagesSentRow;
            if (r13 == r1) goto L_0x01f4;
        L_0x014b:
            r1 = org.telegram.ui.DataUsageActivity.this;
            r1 = r1.messagesReceivedRow;
            if (r13 == r1) goto L_0x01f4;
        L_0x0153:
            r1 = org.telegram.ui.DataUsageActivity.this;
            r1 = r1.messagesBytesSentRow;
            if (r13 == r1) goto L_0x01f4;
        L_0x015b:
            r1 = org.telegram.ui.DataUsageActivity.this;
            r1 = r1.messagesBytesReceivedRow;
            if (r13 != r1) goto L_0x0165;
        L_0x0163:
            goto L_0x01f4;
        L_0x0165:
            r1 = org.telegram.ui.DataUsageActivity.this;
            r1 = r1.photosSentRow;
            if (r13 == r1) goto L_0x01f2;
        L_0x016d:
            r1 = org.telegram.ui.DataUsageActivity.this;
            r1 = r1.photosReceivedRow;
            if (r13 == r1) goto L_0x01f2;
        L_0x0175:
            r1 = org.telegram.ui.DataUsageActivity.this;
            r1 = r1.photosBytesSentRow;
            if (r13 == r1) goto L_0x01f2;
        L_0x017d:
            r1 = org.telegram.ui.DataUsageActivity.this;
            r1 = r1.photosBytesReceivedRow;
            if (r13 != r1) goto L_0x0187;
        L_0x0185:
            goto L_0x01f2;
        L_0x0187:
            r1 = org.telegram.ui.DataUsageActivity.this;
            r1 = r1.audiosSentRow;
            if (r13 == r1) goto L_0x01f0;
        L_0x018f:
            r1 = org.telegram.ui.DataUsageActivity.this;
            r1 = r1.audiosReceivedRow;
            if (r13 == r1) goto L_0x01f0;
        L_0x0197:
            r1 = org.telegram.ui.DataUsageActivity.this;
            r1 = r1.audiosBytesSentRow;
            if (r13 == r1) goto L_0x01f0;
        L_0x019f:
            r1 = org.telegram.ui.DataUsageActivity.this;
            r1 = r1.audiosBytesReceivedRow;
            if (r13 != r1) goto L_0x01a8;
        L_0x01a7:
            goto L_0x01f0;
        L_0x01a8:
            r1 = org.telegram.ui.DataUsageActivity.this;
            r1 = r1.videosSentRow;
            if (r13 == r1) goto L_0x01ee;
        L_0x01b0:
            r1 = org.telegram.ui.DataUsageActivity.this;
            r1 = r1.videosReceivedRow;
            if (r13 == r1) goto L_0x01ee;
        L_0x01b8:
            r1 = org.telegram.ui.DataUsageActivity.this;
            r1 = r1.videosBytesSentRow;
            if (r13 == r1) goto L_0x01ee;
        L_0x01c0:
            r1 = org.telegram.ui.DataUsageActivity.this;
            r1 = r1.videosBytesReceivedRow;
            if (r13 != r1) goto L_0x01c9;
        L_0x01c8:
            goto L_0x01ee;
        L_0x01c9:
            r1 = org.telegram.ui.DataUsageActivity.this;
            r1 = r1.filesSentRow;
            if (r13 == r1) goto L_0x01ec;
        L_0x01d1:
            r1 = org.telegram.ui.DataUsageActivity.this;
            r1 = r1.filesReceivedRow;
            if (r13 == r1) goto L_0x01ec;
        L_0x01d9:
            r1 = org.telegram.ui.DataUsageActivity.this;
            r1 = r1.filesBytesSentRow;
            if (r13 == r1) goto L_0x01ec;
        L_0x01e1:
            r1 = org.telegram.ui.DataUsageActivity.this;
            r1 = r1.filesBytesReceivedRow;
            if (r13 != r1) goto L_0x01ea;
        L_0x01e9:
            goto L_0x01ec;
        L_0x01ea:
            r1 = 6;
            goto L_0x01f7;
        L_0x01ec:
            r1 = 5;
            goto L_0x01f7;
        L_0x01ee:
            r1 = 2;
            goto L_0x01f7;
        L_0x01f0:
            r1 = 3;
            goto L_0x01f7;
        L_0x01f2:
            r1 = 4;
            goto L_0x01f7;
        L_0x01f4:
            r1 = 1;
            goto L_0x01f7;
        L_0x01f6:
            r1 = 0;
            r4 = org.telegram.ui.DataUsageActivity.this;
            r4 = r4.callsSentRow;
            if (r13 != r4) goto L_0x0230;
            r4 = "OutgoingCalls";
            r5 = 2131494049; // 0x7f0c04a1 float:1.8611595E38 double:1.053097984E-314;
            r4 = org.telegram.messenger.LocaleController.getString(r4, r5);
            r5 = "%d";
            r6 = new java.lang.Object[r3];
            r7 = org.telegram.ui.DataUsageActivity.this;
            r7 = r7.currentAccount;
            r7 = org.telegram.messenger.StatsController.getInstance(r7);
            r8 = org.telegram.ui.DataUsageActivity.this;
            r8 = r8.currentType;
            r7 = r7.getSentItemsCount(r8, r1);
            r7 = java.lang.Integer.valueOf(r7);
            r6[r2] = r7;
            r2 = java.lang.String.format(r5, r6);
            r0.setTextAndValue(r4, r2, r3);
            goto L_0x0446;
            r4 = org.telegram.ui.DataUsageActivity.this;
            r4 = r4.callsReceivedRow;
            if (r13 != r4) goto L_0x0268;
            r4 = "IncomingCalls";
            r5 = 2131493672; // 0x7f0c0328 float:1.861083E38 double:1.0530977977E-314;
            r4 = org.telegram.messenger.LocaleController.getString(r4, r5);
            r5 = "%d";
            r6 = new java.lang.Object[r3];
            r7 = org.telegram.ui.DataUsageActivity.this;
            r7 = r7.currentAccount;
            r7 = org.telegram.messenger.StatsController.getInstance(r7);
            r8 = org.telegram.ui.DataUsageActivity.this;
            r8 = r8.currentType;
            r7 = r7.getRecivedItemsCount(r8, r1);
            r7 = java.lang.Integer.valueOf(r7);
            r6[r2] = r7;
            r2 = java.lang.String.format(r5, r6);
            r0.setTextAndValue(r4, r2, r3);
            goto L_0x0446;
            r4 = org.telegram.ui.DataUsageActivity.this;
            r4 = r4.callsTotalTimeRow;
            if (r13 != r4) goto L_0x02cf;
            r4 = org.telegram.ui.DataUsageActivity.this;
            r4 = r4.currentAccount;
            r4 = org.telegram.messenger.StatsController.getInstance(r4);
            r5 = org.telegram.ui.DataUsageActivity.this;
            r5 = r5.currentType;
            r4 = r4.getCallsTotalTime(r5);
            r5 = r4 / 3600;
            r6 = r5 * 3600;
            r4 = r4 - r6;
            r6 = r4 / 60;
            r7 = r6 * 60;
            r4 = r4 - r7;
            r7 = 2;
            if (r5 == 0) goto L_0x02ad;
            r8 = "%d:%02d:%02d";
            r9 = 3;
            r9 = new java.lang.Object[r9];
            r10 = java.lang.Integer.valueOf(r5);
            r9[r2] = r10;
            r10 = java.lang.Integer.valueOf(r6);
            r9[r3] = r10;
            r3 = java.lang.Integer.valueOf(r4);
            r9[r7] = r3;
            r3 = java.lang.String.format(r8, r9);
            goto L_0x02c1;
            r8 = "%d:%02d";
            r7 = new java.lang.Object[r7];
            r9 = java.lang.Integer.valueOf(r6);
            r7[r2] = r9;
            r9 = java.lang.Integer.valueOf(r4);
            r7[r3] = r9;
            r3 = java.lang.String.format(r8, r7);
            r7 = "CallsTotalTime";
            r8 = 2131493126; // 0x7f0c0106 float:1.8609723E38 double:1.053097528E-314;
            r7 = org.telegram.messenger.LocaleController.getString(r7, r8);
            r0.setTextAndValue(r7, r3, r2);
            goto L_0x0446;
            r4 = org.telegram.ui.DataUsageActivity.this;
            r4 = r4.messagesSentRow;
            if (r13 == r4) goto L_0x0418;
            r4 = org.telegram.ui.DataUsageActivity.this;
            r4 = r4.photosSentRow;
            if (r13 == r4) goto L_0x0418;
            r4 = org.telegram.ui.DataUsageActivity.this;
            r4 = r4.videosSentRow;
            if (r13 == r4) goto L_0x0418;
            r4 = org.telegram.ui.DataUsageActivity.this;
            r4 = r4.audiosSentRow;
            if (r13 == r4) goto L_0x0418;
            r4 = org.telegram.ui.DataUsageActivity.this;
            r4 = r4.filesSentRow;
            if (r13 != r4) goto L_0x02f9;
            goto L_0x0418;
            r4 = org.telegram.ui.DataUsageActivity.this;
            r4 = r4.messagesReceivedRow;
            if (r13 == r4) goto L_0x03e9;
            r4 = org.telegram.ui.DataUsageActivity.this;
            r4 = r4.photosReceivedRow;
            if (r13 == r4) goto L_0x03e9;
            r4 = org.telegram.ui.DataUsageActivity.this;
            r4 = r4.videosReceivedRow;
            if (r13 == r4) goto L_0x03e9;
            r4 = org.telegram.ui.DataUsageActivity.this;
            r4 = r4.audiosReceivedRow;
            if (r13 == r4) goto L_0x03e9;
            r4 = org.telegram.ui.DataUsageActivity.this;
            r4 = r4.filesReceivedRow;
            if (r13 != r4) goto L_0x0323;
            goto L_0x03e9;
            r4 = org.telegram.ui.DataUsageActivity.this;
            r4 = r4.messagesBytesSentRow;
            if (r13 == r4) goto L_0x03c4;
            r4 = org.telegram.ui.DataUsageActivity.this;
            r4 = r4.photosBytesSentRow;
            if (r13 == r4) goto L_0x03c4;
            r4 = org.telegram.ui.DataUsageActivity.this;
            r4 = r4.videosBytesSentRow;
            if (r13 == r4) goto L_0x03c4;
            r4 = org.telegram.ui.DataUsageActivity.this;
            r4 = r4.audiosBytesSentRow;
            if (r13 == r4) goto L_0x03c4;
            r4 = org.telegram.ui.DataUsageActivity.this;
            r4 = r4.filesBytesSentRow;
            if (r13 == r4) goto L_0x03c4;
            r4 = org.telegram.ui.DataUsageActivity.this;
            r4 = r4.callsBytesSentRow;
            if (r13 == r4) goto L_0x03c4;
            r4 = org.telegram.ui.DataUsageActivity.this;
            r4 = r4.totalBytesSentRow;
            if (r13 != r4) goto L_0x035c;
            goto L_0x03c4;
            r4 = org.telegram.ui.DataUsageActivity.this;
            r4 = r4.messagesBytesReceivedRow;
            if (r13 == r4) goto L_0x0394;
            r4 = org.telegram.ui.DataUsageActivity.this;
            r4 = r4.photosBytesReceivedRow;
            if (r13 == r4) goto L_0x0394;
            r4 = org.telegram.ui.DataUsageActivity.this;
            r4 = r4.videosBytesReceivedRow;
            if (r13 == r4) goto L_0x0394;
            r4 = org.telegram.ui.DataUsageActivity.this;
            r4 = r4.audiosBytesReceivedRow;
            if (r13 == r4) goto L_0x0394;
            r4 = org.telegram.ui.DataUsageActivity.this;
            r4 = r4.filesBytesReceivedRow;
            if (r13 == r4) goto L_0x0394;
            r4 = org.telegram.ui.DataUsageActivity.this;
            r4 = r4.callsBytesReceivedRow;
            if (r13 == r4) goto L_0x0394;
            r4 = org.telegram.ui.DataUsageActivity.this;
            r4 = r4.totalBytesReceivedRow;
            if (r13 != r4) goto L_0x0446;
            r4 = "BytesReceived";
            r5 = 2131493100; // 0x7f0c00ec float:1.860967E38 double:1.053097515E-314;
            r4 = org.telegram.messenger.LocaleController.getString(r4, r5);
            r5 = org.telegram.ui.DataUsageActivity.this;
            r5 = r5.currentAccount;
            r5 = org.telegram.messenger.StatsController.getInstance(r5);
            r6 = org.telegram.ui.DataUsageActivity.this;
            r6 = r6.currentType;
            r5 = r5.getReceivedBytesCount(r6, r1);
            r5 = org.telegram.messenger.AndroidUtilities.formatFileSize(r5);
            r6 = org.telegram.ui.DataUsageActivity.this;
            r6 = r6.totalBytesReceivedRow;
            if (r13 == r6) goto L_0x03bf;
            r2 = r3;
            r0.setTextAndValue(r4, r5, r2);
            goto L_0x0446;
            r2 = "BytesSent";
            r4 = 2131493101; // 0x7f0c00ed float:1.8609673E38 double:1.0530975156E-314;
            r2 = org.telegram.messenger.LocaleController.getString(r2, r4);
            r4 = org.telegram.ui.DataUsageActivity.this;
            r4 = r4.currentAccount;
            r4 = org.telegram.messenger.StatsController.getInstance(r4);
            r5 = org.telegram.ui.DataUsageActivity.this;
            r5 = r5.currentType;
            r4 = r4.getSentBytesCount(r5, r1);
            r4 = org.telegram.messenger.AndroidUtilities.formatFileSize(r4);
            r0.setTextAndValue(r2, r4, r3);
            goto L_0x0446;
            r4 = "CountReceived";
            r5 = 2131493305; // 0x7f0c01b9 float:1.8610086E38 double:1.0530976163E-314;
            r4 = org.telegram.messenger.LocaleController.getString(r4, r5);
            r5 = "%d";
            r6 = new java.lang.Object[r3];
            r7 = org.telegram.ui.DataUsageActivity.this;
            r7 = r7.currentAccount;
            r7 = org.telegram.messenger.StatsController.getInstance(r7);
            r8 = org.telegram.ui.DataUsageActivity.this;
            r8 = r8.currentType;
            r7 = r7.getRecivedItemsCount(r8, r1);
            r7 = java.lang.Integer.valueOf(r7);
            r6[r2] = r7;
            r2 = java.lang.String.format(r5, r6);
            r0.setTextAndValue(r4, r2, r3);
            goto L_0x0446;
            r4 = "CountSent";
            r5 = 2131493306; // 0x7f0c01ba float:1.8610088E38 double:1.053097617E-314;
            r4 = org.telegram.messenger.LocaleController.getString(r4, r5);
            r5 = "%d";
            r6 = new java.lang.Object[r3];
            r7 = org.telegram.ui.DataUsageActivity.this;
            r7 = r7.currentAccount;
            r7 = org.telegram.messenger.StatsController.getInstance(r7);
            r8 = org.telegram.ui.DataUsageActivity.this;
            r8 = r8.currentType;
            r7 = r7.getSentItemsCount(r8, r1);
            r7 = java.lang.Integer.valueOf(r7);
            r6[r2] = r7;
            r2 = java.lang.String.format(r5, r6);
            r0.setTextAndValue(r4, r2, r3);
            goto L_0x046e;
        L_0x0447:
            r0 = org.telegram.ui.DataUsageActivity.this;
            r0 = r0.resetSection2Row;
            if (r13 != r0) goto L_0x045d;
            r0 = r12.itemView;
            r2 = r11.mContext;
            r3 = "windowBackgroundGrayShadow";
            r1 = org.telegram.ui.ActionBar.Theme.getThemedDrawable(r2, r1, r3);
            r0.setBackgroundDrawable(r1);
            goto L_0x046e;
            r0 = r12.itemView;
            r1 = r11.mContext;
            r2 = 2131165331; // 0x7f070093 float:1.7944876E38 double:1.0529355757E-314;
            r3 = "windowBackgroundGrayShadow";
            r1 = org.telegram.ui.ActionBar.Theme.getThemedDrawable(r1, r2, r3);
            r0.setBackgroundDrawable(r1);
        L_0x046e:
            return;
            */
            throw new UnsupportedOperationException("Method not decompiled: org.telegram.ui.DataUsageActivity.ListAdapter.onBindViewHolder(org.telegram.messenger.support.widget.RecyclerView$ViewHolder, int):void");
        }

        public ListAdapter(Context context) {
            this.mContext = context;
        }

        public int getItemCount() {
            return DataUsageActivity.this.rowCount;
        }

        public boolean isEnabled(ViewHolder holder) {
            return holder.getAdapterPosition() == DataUsageActivity.this.resetRow;
        }

        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = null;
            switch (viewType) {
                case 0:
                    view = new ShadowSectionCell(this.mContext);
                    break;
                case 1:
                    view = new TextSettingsCell(this.mContext);
                    view.setBackgroundColor(Theme.getColor(Theme.key_windowBackgroundWhite));
                    break;
                case 2:
                    view = new HeaderCell(this.mContext);
                    view.setBackgroundColor(Theme.getColor(Theme.key_windowBackgroundWhite));
                    break;
                case 3:
                    view = new TextInfoPrivacyCell(this.mContext);
                    break;
                default:
                    break;
            }
            view.setLayoutParams(new LayoutParams(-1, -2));
            return new Holder(view);
        }

        public int getItemViewType(int position) {
            if (position == DataUsageActivity.this.resetSection2Row) {
                return 3;
            }
            if (!(position == DataUsageActivity.this.resetSection2Row || position == DataUsageActivity.this.callsSection2Row || position == DataUsageActivity.this.filesSection2Row || position == DataUsageActivity.this.audiosSection2Row || position == DataUsageActivity.this.videosSection2Row || position == DataUsageActivity.this.photosSection2Row || position == DataUsageActivity.this.messagesSection2Row)) {
                if (position != DataUsageActivity.this.totalSection2Row) {
                    if (!(position == DataUsageActivity.this.totalSectionRow || position == DataUsageActivity.this.callsSectionRow || position == DataUsageActivity.this.filesSectionRow || position == DataUsageActivity.this.audiosSectionRow || position == DataUsageActivity.this.videosSectionRow || position == DataUsageActivity.this.photosSectionRow)) {
                        if (position != DataUsageActivity.this.messagesSectionRow) {
                            return 1;
                        }
                    }
                    return 2;
                }
            }
            return 0;
        }
    }

    public DataUsageActivity(int type) {
        this.currentType = type;
    }

    public boolean onFragmentCreate() {
        super.onFragmentCreate();
        this.rowCount = 0;
        int i = this.rowCount;
        this.rowCount = i + 1;
        this.photosSectionRow = i;
        i = this.rowCount;
        this.rowCount = i + 1;
        this.photosSentRow = i;
        i = this.rowCount;
        this.rowCount = i + 1;
        this.photosReceivedRow = i;
        i = this.rowCount;
        this.rowCount = i + 1;
        this.photosBytesSentRow = i;
        i = this.rowCount;
        this.rowCount = i + 1;
        this.photosBytesReceivedRow = i;
        i = this.rowCount;
        this.rowCount = i + 1;
        this.photosSection2Row = i;
        i = this.rowCount;
        this.rowCount = i + 1;
        this.videosSectionRow = i;
        i = this.rowCount;
        this.rowCount = i + 1;
        this.videosSentRow = i;
        i = this.rowCount;
        this.rowCount = i + 1;
        this.videosReceivedRow = i;
        i = this.rowCount;
        this.rowCount = i + 1;
        this.videosBytesSentRow = i;
        i = this.rowCount;
        this.rowCount = i + 1;
        this.videosBytesReceivedRow = i;
        i = this.rowCount;
        this.rowCount = i + 1;
        this.videosSection2Row = i;
        i = this.rowCount;
        this.rowCount = i + 1;
        this.audiosSectionRow = i;
        i = this.rowCount;
        this.rowCount = i + 1;
        this.audiosSentRow = i;
        i = this.rowCount;
        this.rowCount = i + 1;
        this.audiosReceivedRow = i;
        i = this.rowCount;
        this.rowCount = i + 1;
        this.audiosBytesSentRow = i;
        i = this.rowCount;
        this.rowCount = i + 1;
        this.audiosBytesReceivedRow = i;
        i = this.rowCount;
        this.rowCount = i + 1;
        this.audiosSection2Row = i;
        i = this.rowCount;
        this.rowCount = i + 1;
        this.filesSectionRow = i;
        i = this.rowCount;
        this.rowCount = i + 1;
        this.filesSentRow = i;
        i = this.rowCount;
        this.rowCount = i + 1;
        this.filesReceivedRow = i;
        i = this.rowCount;
        this.rowCount = i + 1;
        this.filesBytesSentRow = i;
        i = this.rowCount;
        this.rowCount = i + 1;
        this.filesBytesReceivedRow = i;
        i = this.rowCount;
        this.rowCount = i + 1;
        this.filesSection2Row = i;
        i = this.rowCount;
        this.rowCount = i + 1;
        this.callsSectionRow = i;
        i = this.rowCount;
        this.rowCount = i + 1;
        this.callsSentRow = i;
        i = this.rowCount;
        this.rowCount = i + 1;
        this.callsReceivedRow = i;
        i = this.rowCount;
        this.rowCount = i + 1;
        this.callsBytesSentRow = i;
        i = this.rowCount;
        this.rowCount = i + 1;
        this.callsBytesReceivedRow = i;
        i = this.rowCount;
        this.rowCount = i + 1;
        this.callsTotalTimeRow = i;
        i = this.rowCount;
        this.rowCount = i + 1;
        this.callsSection2Row = i;
        i = this.rowCount;
        this.rowCount = i + 1;
        this.messagesSectionRow = i;
        i = this.rowCount;
        this.rowCount = i + 1;
        this.messagesBytesSentRow = i;
        i = this.rowCount;
        this.rowCount = i + 1;
        this.messagesBytesReceivedRow = i;
        i = this.rowCount;
        this.rowCount = i + 1;
        this.messagesSection2Row = i;
        i = this.rowCount;
        this.rowCount = i + 1;
        this.totalSectionRow = i;
        i = this.rowCount;
        this.rowCount = i + 1;
        this.totalBytesSentRow = i;
        i = this.rowCount;
        this.rowCount = i + 1;
        this.totalBytesReceivedRow = i;
        i = this.rowCount;
        this.rowCount = i + 1;
        this.totalSection2Row = i;
        i = this.rowCount;
        this.rowCount = i + 1;
        this.resetRow = i;
        i = this.rowCount;
        this.rowCount = i + 1;
        this.resetSection2Row = i;
        return true;
    }

    public View createView(Context context) {
        this.actionBar.setBackButtonImage(R.drawable.ic_ab_back);
        if (this.currentType == 0) {
            this.actionBar.setTitle(LocaleController.getString("MobileUsage", R.string.MobileUsage));
        } else if (this.currentType == 1) {
            this.actionBar.setTitle(LocaleController.getString("WiFiUsage", R.string.WiFiUsage));
        } else if (this.currentType == 2) {
            this.actionBar.setTitle(LocaleController.getString("RoamingUsage", R.string.RoamingUsage));
        }
        if (AndroidUtilities.isTablet()) {
            this.actionBar.setOccupyStatusBar(false);
        }
        this.actionBar.setActionBarMenuOnItemClick(new ActionBarMenuOnItemClick() {
            public void onItemClick(int id) {
                if (id == -1) {
                    DataUsageActivity.this.finishFragment();
                }
            }
        });
        this.listAdapter = new ListAdapter(context);
        this.fragmentView = new FrameLayout(context);
        this.fragmentView.setBackgroundColor(Theme.getColor(Theme.key_windowBackgroundGray));
        FrameLayout frameLayout = this.fragmentView;
        this.listView = new RecyclerListView(context);
        this.listView.setVerticalScrollBarEnabled(false);
        this.listView.setLayoutManager(new LinearLayoutManager(context, 1, false));
        frameLayout.addView(this.listView, LayoutHelper.createFrame(-1, -1, 51));
        this.listView.setAdapter(this.listAdapter);
        this.listView.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(View view, int position) {
                if (DataUsageActivity.this.getParentActivity() != null && position == DataUsageActivity.this.resetRow) {
                    Builder builder = new Builder(DataUsageActivity.this.getParentActivity());
                    builder.setTitle(LocaleController.getString("AppName", R.string.AppName));
                    builder.setMessage(LocaleController.getString("ResetStatisticsAlert", R.string.ResetStatisticsAlert));
                    builder.setPositiveButton(LocaleController.getString("Reset", R.string.Reset), new OnClickListener() {
                        public void onClick(DialogInterface dialogInterface, int i) {
                            StatsController.getInstance(DataUsageActivity.this.currentAccount).resetStats(DataUsageActivity.this.currentType);
                            DataUsageActivity.this.listAdapter.notifyDataSetChanged();
                        }
                    });
                    builder.setNegativeButton(LocaleController.getString("Cancel", R.string.Cancel), null);
                    DataUsageActivity.this.showDialog(builder.create());
                }
            }
        });
        frameLayout.addView(this.actionBar);
        return this.fragmentView;
    }

    public void onResume() {
        super.onResume();
        if (this.listAdapter != null) {
            this.listAdapter.notifyDataSetChanged();
        }
    }

    public ThemeDescription[] getThemeDescriptions() {
        r1 = new ThemeDescription[16];
        r1[0] = new ThemeDescription(this.listView, ThemeDescription.FLAG_CELLBACKGROUNDCOLOR, new Class[]{TextSettingsCell.class, HeaderCell.class}, null, null, null, Theme.key_windowBackgroundWhite);
        r1[1] = new ThemeDescription(this.fragmentView, ThemeDescription.FLAG_BACKGROUND, null, null, null, null, Theme.key_windowBackgroundGray);
        r1[2] = new ThemeDescription(this.actionBar, ThemeDescription.FLAG_BACKGROUND, null, null, null, null, Theme.key_actionBarDefault);
        r1[3] = new ThemeDescription(this.listView, ThemeDescription.FLAG_LISTGLOWCOLOR, null, null, null, null, Theme.key_actionBarDefault);
        r1[4] = new ThemeDescription(this.actionBar, ThemeDescription.FLAG_AB_ITEMSCOLOR, null, null, null, null, Theme.key_actionBarDefaultIcon);
        r1[5] = new ThemeDescription(this.actionBar, ThemeDescription.FLAG_AB_TITLECOLOR, null, null, null, null, Theme.key_actionBarDefaultTitle);
        r1[6] = new ThemeDescription(this.actionBar, ThemeDescription.FLAG_AB_SELECTORCOLOR, null, null, null, null, Theme.key_actionBarDefaultSelector);
        r1[7] = new ThemeDescription(this.listView, ThemeDescription.FLAG_SELECTOR, null, null, null, null, Theme.key_listSelector);
        r1[8] = new ThemeDescription(this.listView, 0, new Class[]{View.class}, Theme.dividerPaint, null, null, Theme.key_divider);
        r1[9] = new ThemeDescription(this.listView, ThemeDescription.FLAG_BACKGROUNDFILTER, new Class[]{ShadowSectionCell.class}, null, null, null, Theme.key_windowBackgroundGrayShadow);
        r1[10] = new ThemeDescription(this.listView, 0, new Class[]{HeaderCell.class}, new String[]{"textView"}, null, null, null, Theme.key_windowBackgroundWhiteBlueHeader);
        r1[11] = new ThemeDescription(this.listView, ThemeDescription.FLAG_BACKGROUNDFILTER, new Class[]{TextInfoPrivacyCell.class}, null, null, null, Theme.key_windowBackgroundGrayShadow);
        r1[12] = new ThemeDescription(this.listView, 0, new Class[]{TextInfoPrivacyCell.class}, new String[]{"textView"}, null, null, null, Theme.key_windowBackgroundWhiteGrayText4);
        r1[13] = new ThemeDescription(this.listView, ThemeDescription.FLAG_CHECKTAG, new Class[]{TextSettingsCell.class}, new String[]{"textView"}, null, null, null, Theme.key_windowBackgroundWhiteBlackText);
        r1[14] = new ThemeDescription(this.listView, 0, new Class[]{TextSettingsCell.class}, new String[]{"valueTextView"}, null, null, null, Theme.key_windowBackgroundWhiteValueText);
        r1[15] = new ThemeDescription(this.listView, ThemeDescription.FLAG_CHECKTAG, new Class[]{TextSettingsCell.class}, new String[]{"textView"}, null, null, null, Theme.key_windowBackgroundWhiteRedText2);
        return r1;
    }
}
