package org.telegram.ui.Adapters;

import android.content.Context;
import android.util.LongSparseArray;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;
import org.telegram.SQLite.SQLitePreparedStatement;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.ContactsController;
import org.telegram.messenger.DataQuery;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.MessageObject;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.MessagesStorage;
import org.telegram.messenger.UserConfig;
import org.telegram.messenger.support.widget.LinearLayoutManager;
import org.telegram.messenger.support.widget.RecyclerView.LayoutParams;
import org.telegram.messenger.support.widget.RecyclerView.ViewHolder;
import org.telegram.tgnet.ConnectionsManager;
import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC.Chat;
import org.telegram.tgnet.TLRPC.EncryptedChat;
import org.telegram.tgnet.TLRPC.Message;
import org.telegram.tgnet.TLRPC.TL_dialog;
import org.telegram.tgnet.TLRPC.TL_error;
import org.telegram.tgnet.TLRPC.TL_inputPeerEmpty;
import org.telegram.tgnet.TLRPC.TL_messages_searchGlobal;
import org.telegram.tgnet.TLRPC.TL_topPeer;
import org.telegram.tgnet.TLRPC.User;
import org.telegram.tgnet.TLRPC.messages_Messages;
import org.telegram.ui.Adapters.SearchAdapterHelper.HashtagObject;
import org.telegram.ui.Adapters.SearchAdapterHelper.SearchAdapterHelperDelegate;
import org.telegram.ui.Cells.DialogCell;
import org.telegram.ui.Cells.GraySectionCell;
import org.telegram.ui.Cells.HashtagSearchCell;
import org.telegram.ui.Cells.HintDialogCell;
import org.telegram.ui.Cells.LoadingCell;
import org.telegram.ui.Cells.ProfileSearchCell;
import org.telegram.ui.Components.RecyclerListView;
import org.telegram.ui.Components.RecyclerListView.Holder;
import org.telegram.ui.Components.RecyclerListView.OnItemClickListener;
import org.telegram.ui.Components.RecyclerListView.OnItemLongClickListener;
import org.telegram.ui.Components.RecyclerListView.SelectionAdapter;

public class DialogsSearchAdapter extends SelectionAdapter {
    private int currentAccount = UserConfig.selectedAccount;
    private DialogsSearchAdapterDelegate delegate;
    private int dialogsType;
    private RecyclerListView innerListView;
    private String lastMessagesSearchString;
    private int lastReqId;
    private int lastSearchId = 0;
    private String lastSearchText;
    private Context mContext;
    private boolean messagesSearchEndReached;
    private int needMessagesSearch;
    private ArrayList<RecentSearchObject> recentSearchObjects = new ArrayList();
    private LongSparseArray<RecentSearchObject> recentSearchObjectsById = new LongSparseArray();
    private int reqId = 0;
    private SearchAdapterHelper searchAdapterHelper = new SearchAdapterHelper(false);
    private ArrayList<TLObject> searchResult = new ArrayList();
    private ArrayList<String> searchResultHashtags = new ArrayList();
    private ArrayList<MessageObject> searchResultMessages = new ArrayList();
    private ArrayList<CharSequence> searchResultNames = new ArrayList();
    private Timer searchTimer;
    private int selfUserId;

    private class DialogSearchResult {
        public int date;
        public CharSequence name;
        public TLObject object;

        private DialogSearchResult() {
        }
    }

    public interface DialogsSearchAdapterDelegate {
        void didPressedOnSubDialog(long j);

        void needRemoveHint(int i);

        void searchStateChanged(boolean z);
    }

    protected static class RecentSearchObject {
        int date;
        long did;
        TLObject object;

        protected RecentSearchObject() {
        }
    }

    private class CategoryAdapterRecycler extends SelectionAdapter {
        private CategoryAdapterRecycler() {
        }

        public void setIndex(int value) {
            notifyDataSetChanged();
        }

        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = new HintDialogCell(DialogsSearchAdapter.this.mContext);
            view.setLayoutParams(new LayoutParams(AndroidUtilities.dp(80.0f), AndroidUtilities.dp(100.0f)));
            return new Holder(view);
        }

        public boolean isEnabled(ViewHolder holder) {
            return true;
        }

        public void onBindViewHolder(ViewHolder holder, int position) {
            HintDialogCell cell = holder.itemView;
            TL_topPeer peer = (TL_topPeer) DataQuery.getInstance(DialogsSearchAdapter.this.currentAccount).hints.get(position);
            TL_dialog dialog = new TL_dialog();
            Chat chat = null;
            User user = null;
            int did = 0;
            if (peer.peer.user_id != 0) {
                did = peer.peer.user_id;
                user = MessagesController.getInstance(DialogsSearchAdapter.this.currentAccount).getUser(Integer.valueOf(peer.peer.user_id));
            } else if (peer.peer.channel_id != 0) {
                did = -peer.peer.channel_id;
                chat = MessagesController.getInstance(DialogsSearchAdapter.this.currentAccount).getChat(Integer.valueOf(peer.peer.channel_id));
            } else if (peer.peer.chat_id != 0) {
                did = -peer.peer.chat_id;
                chat = MessagesController.getInstance(DialogsSearchAdapter.this.currentAccount).getChat(Integer.valueOf(peer.peer.chat_id));
            }
            cell.setTag(Integer.valueOf(did));
            String name = TtmlNode.ANONYMOUS_REGION_ID;
            if (user != null) {
                name = ContactsController.formatName(user.first_name, user.last_name);
            } else if (chat != null) {
                name = chat.title;
            }
            cell.setDialog(did, true, name);
        }

        public int getItemCount() {
            return DataQuery.getInstance(DialogsSearchAdapter.this.currentAccount).hints.size();
        }
    }

    public void onBindViewHolder(org.telegram.messenger.support.widget.RecyclerView.ViewHolder r1, int r2) {
        /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.DecodeException: Load method exception in method: org.telegram.ui.Adapters.DialogsSearchAdapter.onBindViewHolder(org.telegram.messenger.support.widget.RecyclerView$ViewHolder, int):void
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
        r1 = r30;
        r2 = r31;
        r3 = r32;
        r4 = r31.getItemViewType();
        r7 = 1;
        switch(r4) {
            case 0: goto L_0x00fc;
            case 1: goto L_0x0067;
            case 2: goto L_0x0044;
            case 3: goto L_0x0042;
            case 4: goto L_0x0021;
            case 5: goto L_0x0010;
            default: goto L_0x000e;
        };
    L_0x000e:
        goto L_0x0343;
    L_0x0010:
        r4 = r2.itemView;
        r4 = (org.telegram.ui.Components.RecyclerListView) r4;
        r5 = r4.getAdapter();
        r5 = (org.telegram.ui.Adapters.DialogsSearchAdapter.CategoryAdapterRecycler) r5;
        r6 = r3 / 2;
        r5.setIndex(r6);
        goto L_0x0343;
    L_0x0021:
        r4 = r2.itemView;
        r4 = (org.telegram.ui.Cells.HashtagSearchCell) r4;
        r5 = r1.searchResultHashtags;
        r8 = r3 + -1;
        r5 = r5.get(r8);
        r5 = (java.lang.CharSequence) r5;
        r4.setText(r5);
        r5 = r1.searchResultHashtags;
        r5 = r5.size();
        if (r3 == r5) goto L_0x003c;
    L_0x003a:
        r6 = r7;
        goto L_0x003d;
    L_0x003c:
        r6 = 0;
    L_0x003d:
        r4.setNeedDivider(r6);
        goto L_0x0343;
    L_0x0042:
        goto L_0x0343;
    L_0x0044:
        r4 = r2.itemView;
        r4 = (org.telegram.ui.Cells.DialogCell) r4;
        r5 = r30.getItemCount();
        r5 = r5 - r7;
        if (r3 == r5) goto L_0x0051;
    L_0x004f:
        r6 = r7;
        goto L_0x0052;
    L_0x0051:
        r6 = 0;
    L_0x0052:
        r4.useSeparator = r6;
        r5 = r1.getItem(r3);
        r5 = (org.telegram.messenger.MessageObject) r5;
        r6 = r5.getDialogId();
        r8 = r5.messageOwner;
        r8 = r8.date;
        r4.setDialog(r6, r5, r8);
        goto L_0x0343;
    L_0x0067:
        r4 = r2.itemView;
        r4 = (org.telegram.ui.Cells.GraySectionCell) r4;
        r7 = r30.isRecentSearchDisplayed();
        if (r7 == 0) goto L_0x00a7;
    L_0x0071:
        r7 = r1.currentAccount;
        r7 = org.telegram.messenger.DataQuery.getInstance(r7);
        r7 = r7.hints;
        r7 = r7.isEmpty();
        if (r7 != 0) goto L_0x0081;
    L_0x007f:
        r5 = 2;
        goto L_0x0082;
    L_0x0081:
        r5 = 0;
    L_0x0082:
        if (r3 >= r5) goto L_0x0095;
    L_0x0084:
        r6 = "ChatHints";
        r7 = 2131493224; // 0x7f0c0168 float:1.8609922E38 double:1.0530975763E-314;
        r6 = org.telegram.messenger.LocaleController.getString(r6, r7);
        r6 = r6.toUpperCase();
        r4.setText(r6);
        goto L_0x00a5;
    L_0x0095:
        r6 = "Recent";
        r7 = 2131494215; // 0x7f0c0547 float:1.8611932E38 double:1.053098066E-314;
        r6 = org.telegram.messenger.LocaleController.getString(r6, r7);
        r6 = r6.toUpperCase();
        r4.setText(r6);
    L_0x00a5:
        goto L_0x0343;
    L_0x00a7:
        r5 = r1.searchResultHashtags;
        r5 = r5.isEmpty();
        if (r5 != 0) goto L_0x00c1;
    L_0x00af:
        r5 = "Hashtags";
        r6 = 2131493647; // 0x7f0c030f float:1.861078E38 double:1.0530977853E-314;
        r5 = org.telegram.messenger.LocaleController.getString(r5, r6);
        r5 = r5.toUpperCase();
        r4.setText(r5);
        goto L_0x0343;
    L_0x00c1:
        r5 = r1.searchAdapterHelper;
        r5 = r5.getGlobalSearch();
        r5 = r5.isEmpty();
        if (r5 != 0) goto L_0x00ee;
    L_0x00cd:
        r5 = r1.searchResult;
        r5 = r5.size();
        r6 = r1.searchAdapterHelper;
        r6 = r6.getLocalServerSearch();
        r6 = r6.size();
        r5 = r5 + r6;
        if (r3 != r5) goto L_0x00ee;
    L_0x00e0:
        r5 = "GlobalSearch";
        r6 = 2131493628; // 0x7f0c02fc float:1.8610742E38 double:1.053097776E-314;
        r5 = org.telegram.messenger.LocaleController.getString(r5, r6);
        r4.setText(r5);
        goto L_0x0343;
    L_0x00ee:
        r5 = "SearchMessages";
        r6 = 2131494307; // 0x7f0c05a3 float:1.8612119E38 double:1.0530981114E-314;
        r5 = org.telegram.messenger.LocaleController.getString(r5, r6);
        r4.setText(r5);
        goto L_0x0343;
    L_0x00fc:
        r4 = r2.itemView;
        r4 = (org.telegram.ui.Cells.ProfileSearchCell) r4;
        r8 = 0;
        r9 = 0;
        r10 = 0;
        r11 = 0;
        r12 = 0;
        r13 = 0;
        r14 = 0;
        r15 = r1.getItem(r3);
        r5 = r15 instanceof org.telegram.tgnet.TLRPC.User;
        if (r5 == 0) goto L_0x0119;
    L_0x010f:
        r8 = r15;
        r8 = (org.telegram.tgnet.TLRPC.User) r8;
        r14 = r8.username;
    L_0x0114:
        r5 = r8;
        r6 = r9;
        r18 = r10;
        goto L_0x0164;
    L_0x0119:
        r5 = r15 instanceof org.telegram.tgnet.TLRPC.Chat;
        if (r5 == 0) goto L_0x0139;
    L_0x011d:
        r5 = r1.currentAccount;
        r5 = org.telegram.messenger.MessagesController.getInstance(r5);
        r6 = r15;
        r6 = (org.telegram.tgnet.TLRPC.Chat) r6;
        r6 = r6.id;
        r6 = java.lang.Integer.valueOf(r6);
        r5 = r5.getChat(r6);
        if (r5 != 0) goto L_0x0135;
    L_0x0132:
        r5 = r15;
        r5 = (org.telegram.tgnet.TLRPC.Chat) r5;
    L_0x0135:
        r9 = r5;
        r14 = r9.username;
        goto L_0x0114;
    L_0x0139:
        r5 = r15 instanceof org.telegram.tgnet.TLRPC.EncryptedChat;
        if (r5 == 0) goto L_0x0114;
    L_0x013d:
        r5 = r1.currentAccount;
        r5 = org.telegram.messenger.MessagesController.getInstance(r5);
        r6 = r15;
        r6 = (org.telegram.tgnet.TLRPC.EncryptedChat) r6;
        r6 = r6.id;
        r6 = java.lang.Integer.valueOf(r6);
        r5 = r5.getEncryptedChat(r6);
        r6 = r1.currentAccount;
        r6 = org.telegram.messenger.MessagesController.getInstance(r6);
        r10 = r5.user_id;
        r10 = java.lang.Integer.valueOf(r10);
        r8 = r6.getUser(r10);
        r18 = r5;
        r5 = r8;
        r6 = r9;
    L_0x0164:
        r8 = r30.isRecentSearchDisplayed();
        if (r8 == 0) goto L_0x017a;
    L_0x016a:
        r8 = 1;
        r9 = r30.getItemCount();
        r9 = r9 - r7;
        if (r3 == r9) goto L_0x0174;
    L_0x0172:
        r9 = r7;
        goto L_0x0175;
    L_0x0174:
        r9 = 0;
    L_0x0175:
        r4.useSeparator = r9;
        r2 = r8;
        goto L_0x02c7;
    L_0x017a:
        r8 = r1.searchAdapterHelper;
        r8 = r8.getGlobalSearch();
        r9 = r1.searchResult;
        r9 = r9.size();
        r10 = r1.searchAdapterHelper;
        r10 = r10.getLocalServerSearch();
        r10 = r10.size();
        r19 = r8.isEmpty();
        if (r19 == 0) goto L_0x0199;
    L_0x0196:
        r19 = 0;
        goto L_0x019f;
    L_0x0199:
        r19 = r8.size();
        r19 = r19 + 1;
    L_0x019f:
        r20 = r30.getItemCount();
        r2 = r20 + -1;
        if (r3 == r2) goto L_0x01b4;
    L_0x01a7:
        r2 = r9 + r10;
        r2 = r2 - r7;
        if (r3 == r2) goto L_0x01b4;
    L_0x01ac:
        r2 = r9 + r19;
        r2 = r2 + r10;
        r2 = r2 - r7;
        if (r3 == r2) goto L_0x01b4;
    L_0x01b2:
        r2 = r7;
        goto L_0x01b5;
    L_0x01b4:
        r2 = 0;
    L_0x01b5:
        r4.useSeparator = r2;
        r2 = r1.searchResult;
        r2 = r2.size();
        if (r3 >= r2) goto L_0x01fa;
    L_0x01bf:
        r2 = r1.searchResultNames;
        r2 = r2.get(r3);
        r12 = r2;
        r12 = (java.lang.CharSequence) r12;
        if (r12 == 0) goto L_0x01f7;
    L_0x01ca:
        if (r5 == 0) goto L_0x01f7;
    L_0x01cc:
        r2 = r5.username;
        if (r2 == 0) goto L_0x01f7;
    L_0x01d0:
        r2 = r5.username;
        r2 = r2.length();
        if (r2 <= 0) goto L_0x01f7;
    L_0x01d8:
        r2 = r12.toString();
        r7 = new java.lang.StringBuilder;
        r7.<init>();
        r3 = "@";
        r7.append(r3);
        r3 = r5.username;
        r7.append(r3);
        r3 = r7.toString();
        r2 = r2.startsWith(r3);
        if (r2 == 0) goto L_0x01f7;
    L_0x01f5:
        r11 = r12;
        r12 = 0;
    L_0x01f7:
        r2 = r13;
        goto L_0x02c7;
    L_0x01fa:
        r2 = r1.searchAdapterHelper;
        r2 = r2.getLastFoundUsername();
        r3 = android.text.TextUtils.isEmpty(r2);
        if (r3 != 0) goto L_0x01f7;
    L_0x0206:
        r3 = 0;
        r7 = 0;
        if (r5 == 0) goto L_0x021b;
    L_0x020a:
        r21 = r3;
        r3 = r5.first_name;
        r22 = r7;
        r7 = r5.last_name;
        r3 = org.telegram.messenger.ContactsController.formatName(r3, r7);
        r7 = r3.toLowerCase();
    L_0x021a:
        goto L_0x022c;
    L_0x021b:
        r21 = r3;
        r22 = r7;
        if (r6 == 0) goto L_0x0228;
    L_0x0221:
        r3 = r6.title;
        r7 = r3.toLowerCase();
        goto L_0x021a;
    L_0x0228:
        r3 = r21;
        r7 = r22;
    L_0x022c:
        r23 = r8;
        r8 = -1;
        if (r3 == 0) goto L_0x0266;
    L_0x0231:
        r24 = r9;
        r9 = r7.indexOf(r2);
        r25 = r9;
        if (r9 == r8) goto L_0x025f;
    L_0x023b:
        r8 = new android.text.SpannableStringBuilder;
        r8.<init>(r3);
        r9 = new android.text.style.ForegroundColorSpan;
        r26 = r3;
        r3 = "windowBackgroundWhiteBlueText4";
        r3 = org.telegram.ui.ActionBar.Theme.getColor(r3);
        r9.<init>(r3);
        r3 = r2.length();
        r27 = r7;
        r7 = r25;
        r3 = r3 + r7;
        r28 = r10;
        r10 = 33;
        r8.setSpan(r9, r7, r3, r10);
        r12 = r8;
        goto L_0x01f7;
    L_0x025f:
        r26 = r3;
        r27 = r7;
        r28 = r10;
        goto L_0x026e;
    L_0x0266:
        r26 = r3;
        r27 = r7;
        r24 = r9;
        r28 = r10;
    L_0x026e:
        if (r14 == 0) goto L_0x01f7;
    L_0x0270:
        r3 = "@";
        r3 = r2.startsWith(r3);
        if (r3 == 0) goto L_0x027d;
    L_0x0278:
        r3 = 1;
        r2 = r2.substring(r3);
    L_0x027d:
        r3 = new android.text.SpannableStringBuilder;	 Catch:{ Exception -> 0x02bd }
        r3.<init>();	 Catch:{ Exception -> 0x02bd }
        r7 = "@";	 Catch:{ Exception -> 0x02bd }
        r3.append(r7);	 Catch:{ Exception -> 0x02bd }
        r3.append(r14);	 Catch:{ Exception -> 0x02bd }
        r7 = r14.toLowerCase();	 Catch:{ Exception -> 0x02bd }
        r7 = r7.indexOf(r2);	 Catch:{ Exception -> 0x02bd }
        r9 = r7;	 Catch:{ Exception -> 0x02bd }
        if (r7 == r8) goto L_0x02b8;	 Catch:{ Exception -> 0x02bd }
    L_0x0295:
        r7 = r2.length();	 Catch:{ Exception -> 0x02bd }
        if (r9 != 0) goto L_0x029e;	 Catch:{ Exception -> 0x02bd }
    L_0x029b:
        r7 = r7 + 1;	 Catch:{ Exception -> 0x02bd }
        goto L_0x02a0;	 Catch:{ Exception -> 0x02bd }
    L_0x029e:
        r9 = r9 + 1;	 Catch:{ Exception -> 0x02bd }
    L_0x02a0:
        r8 = new android.text.style.ForegroundColorSpan;	 Catch:{ Exception -> 0x02bd }
        r10 = "windowBackgroundWhiteBlueText4";	 Catch:{ Exception -> 0x02bd }
        r10 = org.telegram.ui.ActionBar.Theme.getColor(r10);	 Catch:{ Exception -> 0x02bd }
        r8.<init>(r10);	 Catch:{ Exception -> 0x02bd }
        r10 = r9 + r7;
        r29 = r2;
        r2 = 33;
        r3.setSpan(r8, r9, r10, r2);	 Catch:{ Exception -> 0x02b5 }
        goto L_0x02ba;
    L_0x02b5:
        r0 = move-exception;
        r2 = r0;
        goto L_0x02c1;
    L_0x02b8:
        r29 = r2;
    L_0x02ba:
        r11 = r3;
        goto L_0x01f7;
    L_0x02bd:
        r0 = move-exception;
        r29 = r2;
        r2 = r0;
    L_0x02c1:
        r11 = r14;
        org.telegram.messenger.FileLog.e(r2);
        goto L_0x01f7;
    L_0x02c7:
        r3 = 0;
        if (r5 == 0) goto L_0x02dc;
    L_0x02ca:
        r7 = r5.id;
        r8 = r1.selfUserId;
        if (r7 != r8) goto L_0x02dc;
    L_0x02d0:
        r7 = "SavedMessages";
        r8 = 2131494293; // 0x7f0c0595 float:1.861209E38 double:1.0530981045E-314;
        r7 = org.telegram.messenger.LocaleController.getString(r7, r8);
        r11 = 0;
        r3 = 1;
        goto L_0x02dd;
    L_0x02dc:
        r7 = r12;
    L_0x02dd:
        if (r6 == 0) goto L_0x032e;
    L_0x02df:
        r8 = r6.participants_count;
        if (r8 == 0) goto L_0x032e;
    L_0x02e3:
        r8 = org.telegram.messenger.ChatObject.isChannel(r6);
        if (r8 == 0) goto L_0x02f6;
    L_0x02e9:
        r8 = r6.megagroup;
        if (r8 != 0) goto L_0x02f6;
    L_0x02ed:
        r8 = "Subscribers";
        r9 = r6.participants_count;
        r8 = org.telegram.messenger.LocaleController.formatPluralString(r8, r9);
        goto L_0x02fe;
    L_0x02f6:
        r8 = "Members";
        r9 = r6.participants_count;
        r8 = org.telegram.messenger.LocaleController.formatPluralString(r8, r9);
    L_0x02fe:
        r9 = r11 instanceof android.text.SpannableStringBuilder;
        if (r9 == 0) goto L_0x030f;
    L_0x0302:
        r9 = r11;
        r9 = (android.text.SpannableStringBuilder) r9;
        r10 = ", ";
        r9 = r9.append(r10);
        r9.append(r8);
        goto L_0x032e;
    L_0x030f:
        r9 = android.text.TextUtils.isEmpty(r11);
        if (r9 != 0) goto L_0x032a;
    L_0x0315:
        r9 = 3;
        r9 = new java.lang.CharSequence[r9];
        r10 = 0;
        r9[r10] = r11;
        r10 = ", ";
        r12 = 1;
        r9[r12] = r10;
        r10 = 2;
        r9[r10] = r8;
        r9 = android.text.TextUtils.concat(r9);
        r16 = r9;
        goto L_0x0330;
        r16 = r8;
        goto L_0x0330;
    L_0x032e:
        r16 = r11;
        if (r5 == 0) goto L_0x0334;
        r9 = r5;
        goto L_0x0335;
        r9 = r6;
        r8 = r4;
        r10 = r18;
        r11 = r7;
        r12 = r16;
        r13 = r2;
        r17 = r14;
        r14 = r3;
        r8.setData(r9, r10, r11, r12, r13, r14);
    L_0x0343:
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.telegram.ui.Adapters.DialogsSearchAdapter.onBindViewHolder(org.telegram.messenger.support.widget.RecyclerView$ViewHolder, int):void");
    }

    public DialogsSearchAdapter(Context context, int messagesSearch, int type) {
        this.searchAdapterHelper.setDelegate(new SearchAdapterHelperDelegate() {
            public void onDataSetChanged() {
                DialogsSearchAdapter.this.notifyDataSetChanged();
            }

            public void onSetHashtags(ArrayList<HashtagObject> arrayList, HashMap<String, HashtagObject> hashMap) {
                for (int a = 0; a < arrayList.size(); a++) {
                    DialogsSearchAdapter.this.searchResultHashtags.add(((HashtagObject) arrayList.get(a)).hashtag);
                }
                if (DialogsSearchAdapter.this.delegate != null) {
                    DialogsSearchAdapter.this.delegate.searchStateChanged(false);
                }
                DialogsSearchAdapter.this.notifyDataSetChanged();
            }
        });
        this.mContext = context;
        this.needMessagesSearch = messagesSearch;
        this.dialogsType = type;
        this.selfUserId = UserConfig.getInstance(this.currentAccount).getClientUserId();
        loadRecentSearch();
        DataQuery.getInstance(this.currentAccount).loadHints(true);
    }

    public RecyclerListView getInnerListView() {
        return this.innerListView;
    }

    public void setDelegate(DialogsSearchAdapterDelegate delegate) {
        this.delegate = delegate;
    }

    public boolean isMessagesSearchEndReached() {
        return this.messagesSearchEndReached;
    }

    public void loadMoreSearchMessages() {
        searchMessagesInternal(this.lastMessagesSearchString);
    }

    public String getLastSearchString() {
        return this.lastMessagesSearchString;
    }

    private void searchMessagesInternal(String query) {
        if (this.needMessagesSearch != 0) {
            if (this.lastMessagesSearchString == null || this.lastMessagesSearchString.length() == 0) {
                if (query != null) {
                    if (query.length() == 0) {
                    }
                }
            }
            if (this.reqId != 0) {
                ConnectionsManager.getInstance(this.currentAccount).cancelRequest(this.reqId, true);
                this.reqId = 0;
            }
            if (query != null) {
                if (query.length() != 0) {
                    final TL_messages_searchGlobal req = new TL_messages_searchGlobal();
                    req.limit = 20;
                    req.q = query;
                    if (this.lastMessagesSearchString == null || !query.equals(this.lastMessagesSearchString) || this.searchResultMessages.isEmpty()) {
                        req.offset_date = 0;
                        req.offset_id = 0;
                        req.offset_peer = new TL_inputPeerEmpty();
                    } else {
                        int id;
                        MessageObject lastMessage = (MessageObject) this.searchResultMessages.get(this.searchResultMessages.size() - 1);
                        req.offset_id = lastMessage.getId();
                        req.offset_date = lastMessage.messageOwner.date;
                        if (lastMessage.messageOwner.to_id.channel_id != 0) {
                            id = -lastMessage.messageOwner.to_id.channel_id;
                        } else if (lastMessage.messageOwner.to_id.chat_id != 0) {
                            id = -lastMessage.messageOwner.to_id.chat_id;
                        } else {
                            id = lastMessage.messageOwner.to_id.user_id;
                            req.offset_peer = MessagesController.getInstance(this.currentAccount).getInputPeer(id);
                        }
                        req.offset_peer = MessagesController.getInstance(this.currentAccount).getInputPeer(id);
                    }
                    this.lastMessagesSearchString = query;
                    final int currentReqId = this.lastReqId + 1;
                    this.lastReqId = currentReqId;
                    if (this.delegate != null) {
                        this.delegate.searchStateChanged(true);
                    }
                    this.reqId = ConnectionsManager.getInstance(this.currentAccount).sendRequest(req, new RequestDelegate() {
                        public void run(final TLObject response, final TL_error error) {
                            AndroidUtilities.runOnUIThread(new Runnable() {
                                public void run() {
                                    if (currentReqId == DialogsSearchAdapter.this.lastReqId && error == null) {
                                        messages_Messages res = response;
                                        boolean z = true;
                                        MessagesStorage.getInstance(DialogsSearchAdapter.this.currentAccount).putUsersAndChats(res.users, res.chats, true, true);
                                        MessagesController.getInstance(DialogsSearchAdapter.this.currentAccount).putUsers(res.users, false);
                                        MessagesController.getInstance(DialogsSearchAdapter.this.currentAccount).putChats(res.chats, false);
                                        if (req.offset_id == 0) {
                                            DialogsSearchAdapter.this.searchResultMessages.clear();
                                        }
                                        for (int a = 0; a < res.messages.size(); a++) {
                                            Message message = (Message) res.messages.get(a);
                                            DialogsSearchAdapter.this.searchResultMessages.add(new MessageObject(DialogsSearchAdapter.this.currentAccount, message, false));
                                            long dialog_id = MessageObject.getDialogId(message);
                                            ConcurrentHashMap<Long, Integer> read_max = message.out ? MessagesController.getInstance(DialogsSearchAdapter.this.currentAccount).dialogs_read_outbox_max : MessagesController.getInstance(DialogsSearchAdapter.this.currentAccount).dialogs_read_inbox_max;
                                            Integer value = (Integer) read_max.get(Long.valueOf(dialog_id));
                                            if (value == null) {
                                                value = Integer.valueOf(MessagesStorage.getInstance(DialogsSearchAdapter.this.currentAccount).getDialogReadMax(message.out, dialog_id));
                                                read_max.put(Long.valueOf(dialog_id), value);
                                            }
                                            message.unread = value.intValue() < message.id;
                                        }
                                        DialogsSearchAdapter dialogsSearchAdapter = DialogsSearchAdapter.this;
                                        if (res.messages.size() == 20) {
                                            z = false;
                                        }
                                        dialogsSearchAdapter.messagesSearchEndReached = z;
                                        DialogsSearchAdapter.this.notifyDataSetChanged();
                                    }
                                    if (DialogsSearchAdapter.this.delegate != null) {
                                        DialogsSearchAdapter.this.delegate.searchStateChanged(false);
                                    }
                                    DialogsSearchAdapter.this.reqId = 0;
                                }
                            });
                        }
                    }, 2);
                    return;
                }
            }
            this.searchResultMessages.clear();
            this.lastReqId = 0;
            this.lastMessagesSearchString = null;
            notifyDataSetChanged();
            if (this.delegate != null) {
                this.delegate.searchStateChanged(false);
            }
        }
    }

    public boolean hasRecentRearch() {
        if (this.recentSearchObjects.isEmpty()) {
            if (DataQuery.getInstance(this.currentAccount).hints.isEmpty()) {
                return false;
            }
        }
        return true;
    }

    public boolean isRecentSearchDisplayed() {
        return this.needMessagesSearch != 2 && ((this.lastSearchText == null || this.lastSearchText.length() == 0) && !(this.recentSearchObjects.isEmpty() && DataQuery.getInstance(this.currentAccount).hints.isEmpty()));
    }

    public void loadRecentSearch() {
        MessagesStorage.getInstance(this.currentAccount).getStorageQueue().postRunnable(new Runnable() {

            class AnonymousClass2 implements Runnable {
                final /* synthetic */ ArrayList val$arrayList;
                final /* synthetic */ LongSparseArray val$hashMap;

                AnonymousClass2(ArrayList arrayList, LongSparseArray longSparseArray) {
                    this.val$arrayList = arrayList;
                    this.val$hashMap = longSparseArray;
                }

                public void run() {
                    DialogsSearchAdapter.this.setRecentSearch(this.val$arrayList, this.val$hashMap);
                }
            }

            public void run() {
                /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.DecodeException: Load method exception in method: org.telegram.ui.Adapters.DialogsSearchAdapter.3.run():void
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
                r1 = r19;
                r2 = org.telegram.ui.Adapters.DialogsSearchAdapter.this;	 Catch:{ Exception -> 0x01ca }
                r2 = r2.currentAccount;	 Catch:{ Exception -> 0x01ca }
                r2 = org.telegram.messenger.MessagesStorage.getInstance(r2);	 Catch:{ Exception -> 0x01ca }
                r2 = r2.getDatabase();	 Catch:{ Exception -> 0x01ca }
                r3 = "SELECT did, date FROM search_recent WHERE 1";	 Catch:{ Exception -> 0x01ca }
                r4 = 0;	 Catch:{ Exception -> 0x01ca }
                r5 = new java.lang.Object[r4];	 Catch:{ Exception -> 0x01ca }
                r2 = r2.queryFinalized(r3, r5);	 Catch:{ Exception -> 0x01ca }
                r3 = new java.util.ArrayList;	 Catch:{ Exception -> 0x01ca }
                r3.<init>();	 Catch:{ Exception -> 0x01ca }
                r5 = new java.util.ArrayList;	 Catch:{ Exception -> 0x01ca }
                r5.<init>();	 Catch:{ Exception -> 0x01ca }
                r6 = new java.util.ArrayList;	 Catch:{ Exception -> 0x01ca }
                r6.<init>();	 Catch:{ Exception -> 0x01ca }
                r7 = new java.util.ArrayList;	 Catch:{ Exception -> 0x01ca }
                r7.<init>();	 Catch:{ Exception -> 0x01ca }
                r8 = new java.util.ArrayList;	 Catch:{ Exception -> 0x01ca }
                r8.<init>();	 Catch:{ Exception -> 0x01ca }
                r9 = new android.util.LongSparseArray;	 Catch:{ Exception -> 0x01ca }
                r9.<init>();	 Catch:{ Exception -> 0x01ca }
                r10 = r2.next();	 Catch:{ Exception -> 0x01ca }
                r11 = 32;	 Catch:{ Exception -> 0x01ca }
                if (r10 == 0) goto L_0x00df;	 Catch:{ Exception -> 0x01ca }
            L_0x003f:
                r12 = r2.longValue(r4);	 Catch:{ Exception -> 0x01ca }
                r10 = 0;	 Catch:{ Exception -> 0x01ca }
                r14 = (int) r12;	 Catch:{ Exception -> 0x01ca }
                r16 = r5;	 Catch:{ Exception -> 0x01ca }
                r4 = r12 >> r11;	 Catch:{ Exception -> 0x01ca }
                r4 = (int) r4;	 Catch:{ Exception -> 0x01ca }
                r5 = 1;	 Catch:{ Exception -> 0x01ca }
                if (r14 == 0) goto L_0x00a4;	 Catch:{ Exception -> 0x01ca }
            L_0x004d:
                if (r4 != r5) goto L_0x006f;	 Catch:{ Exception -> 0x01ca }
            L_0x004f:
                r11 = org.telegram.ui.Adapters.DialogsSearchAdapter.this;	 Catch:{ Exception -> 0x01ca }
                r11 = r11.dialogsType;	 Catch:{ Exception -> 0x01ca }
                if (r11 != 0) goto L_0x006c;	 Catch:{ Exception -> 0x01ca }
            L_0x0057:
                r11 = java.lang.Integer.valueOf(r14);	 Catch:{ Exception -> 0x01ca }
                r15 = r16;	 Catch:{ Exception -> 0x01ca }
                r11 = r15.contains(r11);	 Catch:{ Exception -> 0x01ca }
                if (r11 != 0) goto L_0x00c0;	 Catch:{ Exception -> 0x01ca }
            L_0x0063:
                r11 = java.lang.Integer.valueOf(r14);	 Catch:{ Exception -> 0x01ca }
                r15.add(r11);	 Catch:{ Exception -> 0x01ca }
                r10 = 1;	 Catch:{ Exception -> 0x01ca }
                goto L_0x00c0;	 Catch:{ Exception -> 0x01ca }
            L_0x006c:
                r15 = r16;	 Catch:{ Exception -> 0x01ca }
                goto L_0x00c0;	 Catch:{ Exception -> 0x01ca }
            L_0x006f:
                r15 = r16;	 Catch:{ Exception -> 0x01ca }
                if (r14 <= 0) goto L_0x008f;	 Catch:{ Exception -> 0x01ca }
            L_0x0073:
                r11 = org.telegram.ui.Adapters.DialogsSearchAdapter.this;	 Catch:{ Exception -> 0x01ca }
                r11 = r11.dialogsType;	 Catch:{ Exception -> 0x01ca }
                r5 = 2;	 Catch:{ Exception -> 0x01ca }
                if (r11 == r5) goto L_0x00c0;	 Catch:{ Exception -> 0x01ca }
            L_0x007c:
                r5 = java.lang.Integer.valueOf(r14);	 Catch:{ Exception -> 0x01ca }
                r5 = r3.contains(r5);	 Catch:{ Exception -> 0x01ca }
                if (r5 != 0) goto L_0x00c0;	 Catch:{ Exception -> 0x01ca }
            L_0x0086:
                r5 = java.lang.Integer.valueOf(r14);	 Catch:{ Exception -> 0x01ca }
                r3.add(r5);	 Catch:{ Exception -> 0x01ca }
                r10 = 1;	 Catch:{ Exception -> 0x01ca }
                goto L_0x00c0;	 Catch:{ Exception -> 0x01ca }
            L_0x008f:
                r5 = -r14;	 Catch:{ Exception -> 0x01ca }
                r5 = java.lang.Integer.valueOf(r5);	 Catch:{ Exception -> 0x01ca }
                r5 = r15.contains(r5);	 Catch:{ Exception -> 0x01ca }
                if (r5 != 0) goto L_0x00c0;	 Catch:{ Exception -> 0x01ca }
            L_0x009a:
                r5 = -r14;	 Catch:{ Exception -> 0x01ca }
                r5 = java.lang.Integer.valueOf(r5);	 Catch:{ Exception -> 0x01ca }
                r15.add(r5);	 Catch:{ Exception -> 0x01ca }
                r10 = 1;	 Catch:{ Exception -> 0x01ca }
                goto L_0x00c0;	 Catch:{ Exception -> 0x01ca }
            L_0x00a4:
                r15 = r16;	 Catch:{ Exception -> 0x01ca }
                r5 = org.telegram.ui.Adapters.DialogsSearchAdapter.this;	 Catch:{ Exception -> 0x01ca }
                r5 = r5.dialogsType;	 Catch:{ Exception -> 0x01ca }
                if (r5 != 0) goto L_0x00c0;	 Catch:{ Exception -> 0x01ca }
            L_0x00ae:
                r5 = java.lang.Integer.valueOf(r4);	 Catch:{ Exception -> 0x01ca }
                r5 = r6.contains(r5);	 Catch:{ Exception -> 0x01ca }
                if (r5 != 0) goto L_0x00c0;	 Catch:{ Exception -> 0x01ca }
            L_0x00b8:
                r5 = java.lang.Integer.valueOf(r4);	 Catch:{ Exception -> 0x01ca }
                r6.add(r5);	 Catch:{ Exception -> 0x01ca }
                r10 = 1;	 Catch:{ Exception -> 0x01ca }
            L_0x00c0:
                if (r10 == 0) goto L_0x00da;	 Catch:{ Exception -> 0x01ca }
            L_0x00c2:
                r5 = new org.telegram.ui.Adapters.DialogsSearchAdapter$RecentSearchObject;	 Catch:{ Exception -> 0x01ca }
                r5.<init>();	 Catch:{ Exception -> 0x01ca }
                r5.did = r12;	 Catch:{ Exception -> 0x01ca }
                r11 = 1;	 Catch:{ Exception -> 0x01ca }
                r11 = r2.intValue(r11);	 Catch:{ Exception -> 0x01ca }
                r5.date = r11;	 Catch:{ Exception -> 0x01ca }
                r8.add(r5);	 Catch:{ Exception -> 0x01ca }
                r18 = r10;	 Catch:{ Exception -> 0x01ca }
                r10 = r5.did;	 Catch:{ Exception -> 0x01ca }
                r9.put(r10, r5);	 Catch:{ Exception -> 0x01ca }
                r5 = r15;	 Catch:{ Exception -> 0x01ca }
                r4 = 0;	 Catch:{ Exception -> 0x01ca }
                goto L_0x0037;	 Catch:{ Exception -> 0x01ca }
            L_0x00df:
                r15 = r5;	 Catch:{ Exception -> 0x01ca }
                r2.dispose();	 Catch:{ Exception -> 0x01ca }
                r4 = new java.util.ArrayList;	 Catch:{ Exception -> 0x01ca }
                r4.<init>();	 Catch:{ Exception -> 0x01ca }
                r5 = r6.isEmpty();	 Catch:{ Exception -> 0x01ca }
                if (r5 != 0) goto L_0x0128;	 Catch:{ Exception -> 0x01ca }
                r5 = new java.util.ArrayList;	 Catch:{ Exception -> 0x01ca }
                r5.<init>();	 Catch:{ Exception -> 0x01ca }
                r10 = org.telegram.ui.Adapters.DialogsSearchAdapter.this;	 Catch:{ Exception -> 0x01ca }
                r10 = r10.currentAccount;	 Catch:{ Exception -> 0x01ca }
                r10 = org.telegram.messenger.MessagesStorage.getInstance(r10);	 Catch:{ Exception -> 0x01ca }
                r12 = ",";	 Catch:{ Exception -> 0x01ca }
                r12 = android.text.TextUtils.join(r12, r6);	 Catch:{ Exception -> 0x01ca }
                r10.getEncryptedChatsInternal(r12, r5, r3);	 Catch:{ Exception -> 0x01ca }
                r10 = 0;	 Catch:{ Exception -> 0x01ca }
                r12 = r5.size();	 Catch:{ Exception -> 0x01ca }
                if (r10 >= r12) goto L_0x0128;	 Catch:{ Exception -> 0x01ca }
                r12 = r5.get(r10);	 Catch:{ Exception -> 0x01ca }
                r12 = (org.telegram.tgnet.TLRPC.EncryptedChat) r12;	 Catch:{ Exception -> 0x01ca }
                r12 = r12.id;	 Catch:{ Exception -> 0x01ca }
                r12 = (long) r12;	 Catch:{ Exception -> 0x01ca }
                r12 = r12 << r11;	 Catch:{ Exception -> 0x01ca }
                r12 = r9.get(r12);	 Catch:{ Exception -> 0x01ca }
                r12 = (org.telegram.ui.Adapters.DialogsSearchAdapter.RecentSearchObject) r12;	 Catch:{ Exception -> 0x01ca }
                r13 = r5.get(r10);	 Catch:{ Exception -> 0x01ca }
                r13 = (org.telegram.tgnet.TLObject) r13;	 Catch:{ Exception -> 0x01ca }
                r12.object = r13;	 Catch:{ Exception -> 0x01ca }
                r10 = r10 + 1;	 Catch:{ Exception -> 0x01ca }
                goto L_0x0107;	 Catch:{ Exception -> 0x01ca }
                r5 = r15.isEmpty();	 Catch:{ Exception -> 0x01ca }
                if (r5 != 0) goto L_0x0180;	 Catch:{ Exception -> 0x01ca }
                r5 = new java.util.ArrayList;	 Catch:{ Exception -> 0x01ca }
                r5.<init>();	 Catch:{ Exception -> 0x01ca }
                r10 = org.telegram.ui.Adapters.DialogsSearchAdapter.this;	 Catch:{ Exception -> 0x01ca }
                r10 = r10.currentAccount;	 Catch:{ Exception -> 0x01ca }
                r10 = org.telegram.messenger.MessagesStorage.getInstance(r10);	 Catch:{ Exception -> 0x01ca }
                r11 = ",";	 Catch:{ Exception -> 0x01ca }
                r11 = android.text.TextUtils.join(r11, r15);	 Catch:{ Exception -> 0x01ca }
                r10.getChatsInternal(r11, r5);	 Catch:{ Exception -> 0x01ca }
                r10 = 0;	 Catch:{ Exception -> 0x01ca }
                r11 = r5.size();	 Catch:{ Exception -> 0x01ca }
                if (r10 >= r11) goto L_0x0180;	 Catch:{ Exception -> 0x01ca }
                r11 = r5.get(r10);	 Catch:{ Exception -> 0x01ca }
                r11 = (org.telegram.tgnet.TLRPC.Chat) r11;	 Catch:{ Exception -> 0x01ca }
                r12 = r11.id;	 Catch:{ Exception -> 0x01ca }
                if (r12 <= 0) goto L_0x015c;	 Catch:{ Exception -> 0x01ca }
                r12 = r11.id;	 Catch:{ Exception -> 0x01ca }
                r12 = -r12;	 Catch:{ Exception -> 0x01ca }
                r12 = (long) r12;	 Catch:{ Exception -> 0x01ca }
                goto L_0x0162;	 Catch:{ Exception -> 0x01ca }
                r12 = r11.id;	 Catch:{ Exception -> 0x01ca }
                r12 = org.telegram.messenger.AndroidUtilities.makeBroadcastId(r12);	 Catch:{ Exception -> 0x01ca }
                r14 = r11.migrated_to;	 Catch:{ Exception -> 0x01ca }
                if (r14 == 0) goto L_0x0175;	 Catch:{ Exception -> 0x01ca }
                r14 = r9.get(r12);	 Catch:{ Exception -> 0x01ca }
                r14 = (org.telegram.ui.Adapters.DialogsSearchAdapter.RecentSearchObject) r14;	 Catch:{ Exception -> 0x01ca }
                r9.remove(r12);	 Catch:{ Exception -> 0x01ca }
                if (r14 == 0) goto L_0x0174;	 Catch:{ Exception -> 0x01ca }
                r8.remove(r14);	 Catch:{ Exception -> 0x01ca }
                goto L_0x017d;	 Catch:{ Exception -> 0x01ca }
                r14 = r9.get(r12);	 Catch:{ Exception -> 0x01ca }
                r14 = (org.telegram.ui.Adapters.DialogsSearchAdapter.RecentSearchObject) r14;	 Catch:{ Exception -> 0x01ca }
                r14.object = r11;	 Catch:{ Exception -> 0x01ca }
                r10 = r10 + 1;	 Catch:{ Exception -> 0x01ca }
                goto L_0x0147;	 Catch:{ Exception -> 0x01ca }
                r5 = r3.isEmpty();	 Catch:{ Exception -> 0x01ca }
                if (r5 != 0) goto L_0x01b9;	 Catch:{ Exception -> 0x01ca }
                r5 = org.telegram.ui.Adapters.DialogsSearchAdapter.this;	 Catch:{ Exception -> 0x01ca }
                r5 = r5.currentAccount;	 Catch:{ Exception -> 0x01ca }
                r5 = org.telegram.messenger.MessagesStorage.getInstance(r5);	 Catch:{ Exception -> 0x01ca }
                r10 = ",";	 Catch:{ Exception -> 0x01ca }
                r10 = android.text.TextUtils.join(r10, r3);	 Catch:{ Exception -> 0x01ca }
                r5.getUsersInternal(r10, r4);	 Catch:{ Exception -> 0x01ca }
                r17 = 0;	 Catch:{ Exception -> 0x01ca }
                r5 = r17;	 Catch:{ Exception -> 0x01ca }
                r10 = r4.size();	 Catch:{ Exception -> 0x01ca }
                if (r5 >= r10) goto L_0x01b9;	 Catch:{ Exception -> 0x01ca }
                r10 = r4.get(r5);	 Catch:{ Exception -> 0x01ca }
                r10 = (org.telegram.tgnet.TLRPC.User) r10;	 Catch:{ Exception -> 0x01ca }
                r11 = r10.id;	 Catch:{ Exception -> 0x01ca }
                r11 = (long) r11;	 Catch:{ Exception -> 0x01ca }
                r11 = r9.get(r11);	 Catch:{ Exception -> 0x01ca }
                r11 = (org.telegram.ui.Adapters.DialogsSearchAdapter.RecentSearchObject) r11;	 Catch:{ Exception -> 0x01ca }
                if (r11 == 0) goto L_0x01b6;	 Catch:{ Exception -> 0x01ca }
                r11.object = r10;	 Catch:{ Exception -> 0x01ca }
                r17 = r5 + 1;	 Catch:{ Exception -> 0x01ca }
                goto L_0x019b;	 Catch:{ Exception -> 0x01ca }
                r5 = new org.telegram.ui.Adapters.DialogsSearchAdapter$3$1;	 Catch:{ Exception -> 0x01ca }
                r5.<init>();	 Catch:{ Exception -> 0x01ca }
                java.util.Collections.sort(r8, r5);	 Catch:{ Exception -> 0x01ca }
                r5 = new org.telegram.ui.Adapters.DialogsSearchAdapter$3$2;	 Catch:{ Exception -> 0x01ca }
                r5.<init>(r8, r9);	 Catch:{ Exception -> 0x01ca }
                org.telegram.messenger.AndroidUtilities.runOnUIThread(r5);	 Catch:{ Exception -> 0x01ca }
                goto L_0x01cf;
            L_0x01ca:
                r0 = move-exception;
                r2 = r0;
                org.telegram.messenger.FileLog.e(r2);
                return;
                */
                throw new UnsupportedOperationException("Method not decompiled: org.telegram.ui.Adapters.DialogsSearchAdapter.3.run():void");
            }
        });
    }

    public void putRecentSearch(final long did, TLObject object) {
        RecentSearchObject recentSearchObject = (RecentSearchObject) this.recentSearchObjectsById.get(did);
        if (recentSearchObject == null) {
            recentSearchObject = new RecentSearchObject();
            this.recentSearchObjectsById.put(did, recentSearchObject);
        } else {
            this.recentSearchObjects.remove(recentSearchObject);
        }
        this.recentSearchObjects.add(0, recentSearchObject);
        recentSearchObject.did = did;
        recentSearchObject.object = object;
        recentSearchObject.date = (int) (System.currentTimeMillis() / 1000);
        notifyDataSetChanged();
        MessagesStorage.getInstance(this.currentAccount).getStorageQueue().postRunnable(new Runnable() {
            public void run() {
                try {
                    SQLitePreparedStatement state = MessagesStorage.getInstance(DialogsSearchAdapter.this.currentAccount).getDatabase().executeFast("REPLACE INTO search_recent VALUES(?, ?)");
                    state.requery();
                    state.bindLong(1, did);
                    state.bindInteger(2, (int) (System.currentTimeMillis() / 1000));
                    state.step();
                    state.dispose();
                } catch (Throwable e) {
                    FileLog.e(e);
                }
            }
        });
    }

    public void clearRecentSearch() {
        this.recentSearchObjectsById = new LongSparseArray();
        this.recentSearchObjects = new ArrayList();
        notifyDataSetChanged();
        MessagesStorage.getInstance(this.currentAccount).getStorageQueue().postRunnable(new Runnable() {
            public void run() {
                try {
                    MessagesStorage.getInstance(DialogsSearchAdapter.this.currentAccount).getDatabase().executeFast("DELETE FROM search_recent WHERE 1").stepThis().dispose();
                } catch (Throwable e) {
                    FileLog.e(e);
                }
            }
        });
    }

    public void addHashtagsFromMessage(CharSequence message) {
        this.searchAdapterHelper.addHashtagsFromMessage(message);
    }

    private void setRecentSearch(ArrayList<RecentSearchObject> arrayList, LongSparseArray<RecentSearchObject> hashMap) {
        this.recentSearchObjects = arrayList;
        this.recentSearchObjectsById = hashMap;
        for (int a = 0; a < this.recentSearchObjects.size(); a++) {
            RecentSearchObject recentSearchObject = (RecentSearchObject) this.recentSearchObjects.get(a);
            if (recentSearchObject.object instanceof User) {
                MessagesController.getInstance(this.currentAccount).putUser((User) recentSearchObject.object, true);
            } else if (recentSearchObject.object instanceof Chat) {
                MessagesController.getInstance(this.currentAccount).putChat((Chat) recentSearchObject.object, true);
            } else if (recentSearchObject.object instanceof EncryptedChat) {
                MessagesController.getInstance(this.currentAccount).putEncryptedChat((EncryptedChat) recentSearchObject.object, true);
            }
        }
        notifyDataSetChanged();
    }

    private void searchDialogsInternal(final String query, final int searchId) {
        if (this.needMessagesSearch != 2) {
            MessagesStorage.getInstance(this.currentAccount).getStorageQueue().postRunnable(new Runnable() {
                public void run() {
                    /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.DecodeException: Load method exception in method: org.telegram.ui.Adapters.DialogsSearchAdapter.6.run():void
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
                    r1 = r58;
                    r2 = "SavedMessages";	 Catch:{ Exception -> 0x07a8 }
                    r3 = 2131494293; // 0x7f0c0595 float:1.861209E38 double:1.0530981045E-314;	 Catch:{ Exception -> 0x07a8 }
                    r2 = org.telegram.messenger.LocaleController.getString(r2, r3);	 Catch:{ Exception -> 0x07a8 }
                    r2 = r2.toLowerCase();	 Catch:{ Exception -> 0x07a8 }
                    r3 = r3;	 Catch:{ Exception -> 0x07a8 }
                    r3 = r3.trim();	 Catch:{ Exception -> 0x07a8 }
                    r3 = r3.toLowerCase();	 Catch:{ Exception -> 0x07a8 }
                    r4 = r3.length();	 Catch:{ Exception -> 0x07a8 }
                    r5 = -1;	 Catch:{ Exception -> 0x07a8 }
                    if (r4 != 0) goto L_0x0040;	 Catch:{ Exception -> 0x07a8 }
                L_0x0020:
                    r4 = org.telegram.ui.Adapters.DialogsSearchAdapter.this;	 Catch:{ Exception -> 0x07a8 }
                    r4.lastSearchId = r5;	 Catch:{ Exception -> 0x07a8 }
                    r4 = org.telegram.ui.Adapters.DialogsSearchAdapter.this;	 Catch:{ Exception -> 0x07a8 }
                    r5 = new java.util.ArrayList;	 Catch:{ Exception -> 0x07a8 }
                    r5.<init>();	 Catch:{ Exception -> 0x07a8 }
                    r6 = new java.util.ArrayList;	 Catch:{ Exception -> 0x07a8 }
                    r6.<init>();	 Catch:{ Exception -> 0x07a8 }
                    r7 = new java.util.ArrayList;	 Catch:{ Exception -> 0x07a8 }
                    r7.<init>();	 Catch:{ Exception -> 0x07a8 }
                    r8 = org.telegram.ui.Adapters.DialogsSearchAdapter.this;	 Catch:{ Exception -> 0x07a8 }
                    r8 = r8.lastSearchId;	 Catch:{ Exception -> 0x07a8 }
                    r4.updateSearchResults(r5, r6, r7, r8);	 Catch:{ Exception -> 0x07a8 }
                    return;	 Catch:{ Exception -> 0x07a8 }
                L_0x0040:
                    r4 = org.telegram.messenger.LocaleController.getInstance();	 Catch:{ Exception -> 0x07a8 }
                    r4 = r4.getTranslitString(r3);	 Catch:{ Exception -> 0x07a8 }
                    r6 = r3.equals(r4);	 Catch:{ Exception -> 0x07a8 }
                    if (r6 != 0) goto L_0x0054;	 Catch:{ Exception -> 0x07a8 }
                L_0x004e:
                    r6 = r4.length();	 Catch:{ Exception -> 0x07a8 }
                    if (r6 != 0) goto L_0x0055;	 Catch:{ Exception -> 0x07a8 }
                L_0x0054:
                    r4 = 0;	 Catch:{ Exception -> 0x07a8 }
                L_0x0055:
                    r6 = 1;	 Catch:{ Exception -> 0x07a8 }
                    r7 = 0;	 Catch:{ Exception -> 0x07a8 }
                    if (r4 == 0) goto L_0x005b;	 Catch:{ Exception -> 0x07a8 }
                L_0x0059:
                    r8 = r6;	 Catch:{ Exception -> 0x07a8 }
                    goto L_0x005c;	 Catch:{ Exception -> 0x07a8 }
                L_0x005b:
                    r8 = r7;	 Catch:{ Exception -> 0x07a8 }
                L_0x005c:
                    r8 = r8 + r6;	 Catch:{ Exception -> 0x07a8 }
                    r8 = new java.lang.String[r8];	 Catch:{ Exception -> 0x07a8 }
                    r8[r7] = r3;	 Catch:{ Exception -> 0x07a8 }
                    if (r4 == 0) goto L_0x0065;	 Catch:{ Exception -> 0x07a8 }
                L_0x0063:
                    r8[r6] = r4;	 Catch:{ Exception -> 0x07a8 }
                L_0x0065:
                    r9 = new java.util.ArrayList;	 Catch:{ Exception -> 0x07a8 }
                    r9.<init>();	 Catch:{ Exception -> 0x07a8 }
                    r10 = new java.util.ArrayList;	 Catch:{ Exception -> 0x07a8 }
                    r10.<init>();	 Catch:{ Exception -> 0x07a8 }
                    r11 = new java.util.ArrayList;	 Catch:{ Exception -> 0x07a8 }
                    r11.<init>();	 Catch:{ Exception -> 0x07a8 }
                    r12 = new java.util.ArrayList;	 Catch:{ Exception -> 0x07a8 }
                    r12.<init>();	 Catch:{ Exception -> 0x07a8 }
                    r13 = 0;	 Catch:{ Exception -> 0x07a8 }
                    r14 = new android.util.LongSparseArray;	 Catch:{ Exception -> 0x07a8 }
                    r14.<init>();	 Catch:{ Exception -> 0x07a8 }
                    r15 = org.telegram.ui.Adapters.DialogsSearchAdapter.this;	 Catch:{ Exception -> 0x07a8 }
                    r15 = r15.currentAccount;	 Catch:{ Exception -> 0x07a8 }
                    r15 = org.telegram.messenger.MessagesStorage.getInstance(r15);	 Catch:{ Exception -> 0x07a8 }
                    r15 = r15.getDatabase();	 Catch:{ Exception -> 0x07a8 }
                    r5 = "SELECT did, date FROM dialogs ORDER BY date DESC LIMIT 600";	 Catch:{ Exception -> 0x07a8 }
                    r6 = new java.lang.Object[r7];	 Catch:{ Exception -> 0x07a8 }
                    r5 = r15.queryFinalized(r5, r6);	 Catch:{ Exception -> 0x07a8 }
                    r6 = r5.next();	 Catch:{ Exception -> 0x07a8 }
                    r15 = 0;	 Catch:{ Exception -> 0x07a8 }
                    if (r6 == 0) goto L_0x013e;	 Catch:{ Exception -> 0x07a8 }
                L_0x009c:
                    r18 = r5.longValue(r7);	 Catch:{ Exception -> 0x07a8 }
                    r20 = r18;	 Catch:{ Exception -> 0x07a8 }
                    r6 = new org.telegram.ui.Adapters.DialogsSearchAdapter$DialogSearchResult;	 Catch:{ Exception -> 0x07a8 }
                    r7 = org.telegram.ui.Adapters.DialogsSearchAdapter.this;	 Catch:{ Exception -> 0x07a8 }
                    r6.<init>();	 Catch:{ Exception -> 0x07a8 }
                    r7 = 1;	 Catch:{ Exception -> 0x07a8 }
                    r15 = r5.intValue(r7);	 Catch:{ Exception -> 0x07a8 }
                    r6.date = r15;	 Catch:{ Exception -> 0x07a8 }
                    r22 = r8;	 Catch:{ Exception -> 0x07a8 }
                    r7 = r20;	 Catch:{ Exception -> 0x07a8 }
                    r14.put(r7, r6);	 Catch:{ Exception -> 0x07a8 }
                    r15 = (int) r7;	 Catch:{ Exception -> 0x07a8 }
                    r23 = r12;	 Catch:{ Exception -> 0x07a8 }
                    r24 = r13;	 Catch:{ Exception -> 0x07a8 }
                    r17 = 32;	 Catch:{ Exception -> 0x07a8 }
                    r12 = r7 >> r17;	 Catch:{ Exception -> 0x07a8 }
                    r12 = (int) r12;	 Catch:{ Exception -> 0x07a8 }
                    if (r15 == 0) goto L_0x0117;	 Catch:{ Exception -> 0x07a8 }
                L_0x00c3:
                    r13 = 1;	 Catch:{ Exception -> 0x07a8 }
                    if (r12 != r13) goto L_0x00e2;	 Catch:{ Exception -> 0x07a8 }
                L_0x00c6:
                    r13 = org.telegram.ui.Adapters.DialogsSearchAdapter.this;	 Catch:{ Exception -> 0x07a8 }
                    r13 = r13.dialogsType;	 Catch:{ Exception -> 0x07a8 }
                    if (r13 != 0) goto L_0x00df;	 Catch:{ Exception -> 0x07a8 }
                L_0x00ce:
                    r13 = java.lang.Integer.valueOf(r15);	 Catch:{ Exception -> 0x07a8 }
                    r13 = r10.contains(r13);	 Catch:{ Exception -> 0x07a8 }
                    if (r13 != 0) goto L_0x00df;	 Catch:{ Exception -> 0x07a8 }
                L_0x00d8:
                    r13 = java.lang.Integer.valueOf(r15);	 Catch:{ Exception -> 0x07a8 }
                    r10.add(r13);	 Catch:{ Exception -> 0x07a8 }
                L_0x00df:
                    r25 = r4;	 Catch:{ Exception -> 0x07a8 }
                    goto L_0x0132;	 Catch:{ Exception -> 0x07a8 }
                L_0x00e2:
                    if (r15 <= 0) goto L_0x0101;	 Catch:{ Exception -> 0x07a8 }
                    r13 = org.telegram.ui.Adapters.DialogsSearchAdapter.this;	 Catch:{ Exception -> 0x07a8 }
                    r13 = r13.dialogsType;	 Catch:{ Exception -> 0x07a8 }
                    r25 = r4;	 Catch:{ Exception -> 0x07a8 }
                    r4 = 2;	 Catch:{ Exception -> 0x07a8 }
                    if (r13 == r4) goto L_0x0132;	 Catch:{ Exception -> 0x07a8 }
                    r4 = java.lang.Integer.valueOf(r15);	 Catch:{ Exception -> 0x07a8 }
                    r4 = r9.contains(r4);	 Catch:{ Exception -> 0x07a8 }
                    if (r4 != 0) goto L_0x0132;	 Catch:{ Exception -> 0x07a8 }
                    r4 = java.lang.Integer.valueOf(r15);	 Catch:{ Exception -> 0x07a8 }
                    r9.add(r4);	 Catch:{ Exception -> 0x07a8 }
                    goto L_0x0132;	 Catch:{ Exception -> 0x07a8 }
                    r25 = r4;	 Catch:{ Exception -> 0x07a8 }
                    r4 = -r15;	 Catch:{ Exception -> 0x07a8 }
                    r4 = java.lang.Integer.valueOf(r4);	 Catch:{ Exception -> 0x07a8 }
                    r4 = r10.contains(r4);	 Catch:{ Exception -> 0x07a8 }
                    if (r4 != 0) goto L_0x0132;	 Catch:{ Exception -> 0x07a8 }
                    r4 = -r15;	 Catch:{ Exception -> 0x07a8 }
                    r4 = java.lang.Integer.valueOf(r4);	 Catch:{ Exception -> 0x07a8 }
                    r10.add(r4);	 Catch:{ Exception -> 0x07a8 }
                    goto L_0x0132;	 Catch:{ Exception -> 0x07a8 }
                L_0x0117:
                    r25 = r4;	 Catch:{ Exception -> 0x07a8 }
                    r4 = org.telegram.ui.Adapters.DialogsSearchAdapter.this;	 Catch:{ Exception -> 0x07a8 }
                    r4 = r4.dialogsType;	 Catch:{ Exception -> 0x07a8 }
                    if (r4 != 0) goto L_0x0132;	 Catch:{ Exception -> 0x07a8 }
                    r4 = java.lang.Integer.valueOf(r12);	 Catch:{ Exception -> 0x07a8 }
                    r4 = r11.contains(r4);	 Catch:{ Exception -> 0x07a8 }
                    if (r4 != 0) goto L_0x0132;	 Catch:{ Exception -> 0x07a8 }
                    r4 = java.lang.Integer.valueOf(r12);	 Catch:{ Exception -> 0x07a8 }
                    r11.add(r4);	 Catch:{ Exception -> 0x07a8 }
                    r8 = r22;	 Catch:{ Exception -> 0x07a8 }
                    r12 = r23;	 Catch:{ Exception -> 0x07a8 }
                    r13 = r24;	 Catch:{ Exception -> 0x07a8 }
                    r4 = r25;	 Catch:{ Exception -> 0x07a8 }
                    r7 = 0;	 Catch:{ Exception -> 0x07a8 }
                    goto L_0x0095;	 Catch:{ Exception -> 0x07a8 }
                L_0x013e:
                    r25 = r4;	 Catch:{ Exception -> 0x07a8 }
                    r22 = r8;	 Catch:{ Exception -> 0x07a8 }
                    r23 = r12;	 Catch:{ Exception -> 0x07a8 }
                    r24 = r13;	 Catch:{ Exception -> 0x07a8 }
                    r5.dispose();	 Catch:{ Exception -> 0x07a8 }
                    r4 = r2.startsWith(r3);	 Catch:{ Exception -> 0x07a8 }
                    if (r4 == 0) goto L_0x0176;	 Catch:{ Exception -> 0x07a8 }
                    r4 = org.telegram.ui.Adapters.DialogsSearchAdapter.this;	 Catch:{ Exception -> 0x07a8 }
                    r4 = r4.currentAccount;	 Catch:{ Exception -> 0x07a8 }
                    r4 = org.telegram.messenger.UserConfig.getInstance(r4);	 Catch:{ Exception -> 0x07a8 }
                    r4 = r4.getCurrentUser();	 Catch:{ Exception -> 0x07a8 }
                    r6 = new org.telegram.ui.Adapters.DialogsSearchAdapter$DialogSearchResult;	 Catch:{ Exception -> 0x07a8 }
                    r7 = org.telegram.ui.Adapters.DialogsSearchAdapter.this;	 Catch:{ Exception -> 0x07a8 }
                    r6.<init>();	 Catch:{ Exception -> 0x07a8 }
                    r7 = 2147483647; // 0x7fffffff float:NaN double:1.060997895E-314;	 Catch:{ Exception -> 0x07a8 }
                    r6.date = r7;	 Catch:{ Exception -> 0x07a8 }
                    r6.name = r2;	 Catch:{ Exception -> 0x07a8 }
                    r6.object = r4;	 Catch:{ Exception -> 0x07a8 }
                    r7 = r4.id;	 Catch:{ Exception -> 0x07a8 }
                    r7 = (long) r7;	 Catch:{ Exception -> 0x07a8 }
                    r14.put(r7, r6);	 Catch:{ Exception -> 0x07a8 }
                    r13 = r24 + 1;	 Catch:{ Exception -> 0x07a8 }
                    goto L_0x0178;	 Catch:{ Exception -> 0x07a8 }
                    r13 = r24;	 Catch:{ Exception -> 0x07a8 }
                    r4 = r9.isEmpty();	 Catch:{ Exception -> 0x07a8 }
                    if (r4 != 0) goto L_0x02dd;	 Catch:{ Exception -> 0x07a8 }
                    r4 = org.telegram.ui.Adapters.DialogsSearchAdapter.this;	 Catch:{ Exception -> 0x07a8 }
                    r4 = r4.currentAccount;	 Catch:{ Exception -> 0x07a8 }
                    r4 = org.telegram.messenger.MessagesStorage.getInstance(r4);	 Catch:{ Exception -> 0x07a8 }
                    r4 = r4.getDatabase();	 Catch:{ Exception -> 0x07a8 }
                    r6 = java.util.Locale.US;	 Catch:{ Exception -> 0x07a8 }
                    r7 = "SELECT data, status, name FROM users WHERE uid IN(%s)";	 Catch:{ Exception -> 0x07a8 }
                    r8 = 1;	 Catch:{ Exception -> 0x07a8 }
                    r12 = new java.lang.Object[r8];	 Catch:{ Exception -> 0x07a8 }
                    r8 = ",";	 Catch:{ Exception -> 0x07a8 }
                    r8 = android.text.TextUtils.join(r8, r9);	 Catch:{ Exception -> 0x07a8 }
                    r15 = 0;	 Catch:{ Exception -> 0x07a8 }
                    r12[r15] = r8;	 Catch:{ Exception -> 0x07a8 }
                    r6 = java.lang.String.format(r6, r7, r12);	 Catch:{ Exception -> 0x07a8 }
                    r7 = new java.lang.Object[r15];	 Catch:{ Exception -> 0x07a8 }
                    r4 = r4.queryFinalized(r6, r7);	 Catch:{ Exception -> 0x07a8 }
                    r5 = r4;	 Catch:{ Exception -> 0x07a8 }
                    r4 = r5.next();	 Catch:{ Exception -> 0x07a8 }
                    if (r4 == 0) goto L_0x02d1;	 Catch:{ Exception -> 0x07a8 }
                    r4 = 2;	 Catch:{ Exception -> 0x07a8 }
                    r6 = r5.stringValue(r4);	 Catch:{ Exception -> 0x07a8 }
                    r4 = r6;	 Catch:{ Exception -> 0x07a8 }
                    r6 = org.telegram.messenger.LocaleController.getInstance();	 Catch:{ Exception -> 0x07a8 }
                    r6 = r6.getTranslitString(r4);	 Catch:{ Exception -> 0x07a8 }
                    r7 = r4.equals(r6);	 Catch:{ Exception -> 0x07a8 }
                    if (r7 == 0) goto L_0x01c2;	 Catch:{ Exception -> 0x07a8 }
                    r6 = 0;	 Catch:{ Exception -> 0x07a8 }
                    r7 = 0;	 Catch:{ Exception -> 0x07a8 }
                    r8 = ";;;";	 Catch:{ Exception -> 0x07a8 }
                    r8 = r4.lastIndexOf(r8);	 Catch:{ Exception -> 0x07a8 }
                    r12 = -1;	 Catch:{ Exception -> 0x07a8 }
                    if (r8 == r12) goto L_0x01d3;	 Catch:{ Exception -> 0x07a8 }
                    r12 = r8 + 3;	 Catch:{ Exception -> 0x07a8 }
                    r12 = r4.substring(r12);	 Catch:{ Exception -> 0x07a8 }
                    r7 = r12;	 Catch:{ Exception -> 0x07a8 }
                    r12 = 0;	 Catch:{ Exception -> 0x07a8 }
                    r26 = r2;	 Catch:{ Exception -> 0x07a8 }
                    r15 = r22;	 Catch:{ Exception -> 0x07a8 }
                    r2 = r15.length;	 Catch:{ Exception -> 0x07a8 }
                    r18 = r12;	 Catch:{ Exception -> 0x07a8 }
                    r12 = 0;	 Catch:{ Exception -> 0x07a8 }
                    if (r12 >= r2) goto L_0x02c3;	 Catch:{ Exception -> 0x07a8 }
                    r19 = r15[r12];	 Catch:{ Exception -> 0x07a8 }
                    r27 = r19;	 Catch:{ Exception -> 0x07a8 }
                    r28 = r2;	 Catch:{ Exception -> 0x07a8 }
                    r2 = r27;	 Catch:{ Exception -> 0x07a8 }
                    r19 = r4.startsWith(r2);	 Catch:{ Exception -> 0x07a8 }
                    if (r19 != 0) goto L_0x0234;	 Catch:{ Exception -> 0x07a8 }
                    r29 = r3;	 Catch:{ Exception -> 0x07a8 }
                    r3 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x07a8 }
                    r3.<init>();	 Catch:{ Exception -> 0x07a8 }
                    r30 = r8;	 Catch:{ Exception -> 0x07a8 }
                    r8 = " ";	 Catch:{ Exception -> 0x07a8 }
                    r3.append(r8);	 Catch:{ Exception -> 0x07a8 }
                    r3.append(r2);	 Catch:{ Exception -> 0x07a8 }
                    r3 = r3.toString();	 Catch:{ Exception -> 0x07a8 }
                    r3 = r4.contains(r3);	 Catch:{ Exception -> 0x07a8 }
                    if (r3 != 0) goto L_0x0238;	 Catch:{ Exception -> 0x07a8 }
                    if (r6 == 0) goto L_0x0227;	 Catch:{ Exception -> 0x07a8 }
                    r3 = r6.startsWith(r2);	 Catch:{ Exception -> 0x07a8 }
                    if (r3 != 0) goto L_0x0238;	 Catch:{ Exception -> 0x07a8 }
                    r3 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x07a8 }
                    r3.<init>();	 Catch:{ Exception -> 0x07a8 }
                    r8 = " ";	 Catch:{ Exception -> 0x07a8 }
                    r3.append(r8);	 Catch:{ Exception -> 0x07a8 }
                    r3.append(r2);	 Catch:{ Exception -> 0x07a8 }
                    r3 = r3.toString();	 Catch:{ Exception -> 0x07a8 }
                    r3 = r6.contains(r3);	 Catch:{ Exception -> 0x07a8 }
                    if (r3 == 0) goto L_0x0227;	 Catch:{ Exception -> 0x07a8 }
                    goto L_0x0238;	 Catch:{ Exception -> 0x07a8 }
                    if (r7 == 0) goto L_0x0231;	 Catch:{ Exception -> 0x07a8 }
                    r3 = r7.startsWith(r2);	 Catch:{ Exception -> 0x07a8 }
                    if (r3 == 0) goto L_0x0231;	 Catch:{ Exception -> 0x07a8 }
                    r3 = 2;	 Catch:{ Exception -> 0x07a8 }
                    goto L_0x0239;	 Catch:{ Exception -> 0x07a8 }
                    r3 = r18;	 Catch:{ Exception -> 0x07a8 }
                    goto L_0x0239;	 Catch:{ Exception -> 0x07a8 }
                    r29 = r3;	 Catch:{ Exception -> 0x07a8 }
                    r30 = r8;	 Catch:{ Exception -> 0x07a8 }
                    r3 = 1;	 Catch:{ Exception -> 0x07a8 }
                    if (r3 == 0) goto L_0x02af;	 Catch:{ Exception -> 0x07a8 }
                    r8 = 0;	 Catch:{ Exception -> 0x07a8 }
                    r12 = r5.byteBufferValue(r8);	 Catch:{ Exception -> 0x07a8 }
                    if (r12 == 0) goto L_0x02ac;	 Catch:{ Exception -> 0x07a8 }
                    r31 = r4;	 Catch:{ Exception -> 0x07a8 }
                    r4 = r12.readInt32(r8);	 Catch:{ Exception -> 0x07a8 }
                    r4 = org.telegram.tgnet.TLRPC.User.TLdeserialize(r12, r4, r8);	 Catch:{ Exception -> 0x07a8 }
                    r12.reuse();	 Catch:{ Exception -> 0x07a8 }
                    r8 = r4.id;	 Catch:{ Exception -> 0x07a8 }
                    r32 = r6;	 Catch:{ Exception -> 0x07a8 }
                    r33 = r7;	 Catch:{ Exception -> 0x07a8 }
                    r6 = (long) r8;	 Catch:{ Exception -> 0x07a8 }
                    r6 = r14.get(r6);	 Catch:{ Exception -> 0x07a8 }
                    r6 = (org.telegram.ui.Adapters.DialogsSearchAdapter.DialogSearchResult) r6;	 Catch:{ Exception -> 0x07a8 }
                    r7 = r4.status;	 Catch:{ Exception -> 0x07a8 }
                    if (r7 == 0) goto L_0x026c;	 Catch:{ Exception -> 0x07a8 }
                    r7 = r4.status;	 Catch:{ Exception -> 0x07a8 }
                    r34 = r9;	 Catch:{ Exception -> 0x07a8 }
                    r8 = 1;	 Catch:{ Exception -> 0x07a8 }
                    r9 = r5.intValue(r8);	 Catch:{ Exception -> 0x07a8 }
                    r7.expires = r9;	 Catch:{ Exception -> 0x07a8 }
                    goto L_0x026e;	 Catch:{ Exception -> 0x07a8 }
                    r34 = r9;	 Catch:{ Exception -> 0x07a8 }
                    r7 = 1;	 Catch:{ Exception -> 0x07a8 }
                    if (r3 != r7) goto L_0x027c;	 Catch:{ Exception -> 0x07a8 }
                    r7 = r4.first_name;	 Catch:{ Exception -> 0x07a8 }
                    r8 = r4.last_name;	 Catch:{ Exception -> 0x07a8 }
                    r7 = org.telegram.messenger.AndroidUtilities.generateSearchName(r7, r8, r2);	 Catch:{ Exception -> 0x07a8 }
                    r6.name = r7;	 Catch:{ Exception -> 0x07a8 }
                    goto L_0x02a7;	 Catch:{ Exception -> 0x07a8 }
                    r7 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x07a8 }
                    r7.<init>();	 Catch:{ Exception -> 0x07a8 }
                    r8 = "@";	 Catch:{ Exception -> 0x07a8 }
                    r7.append(r8);	 Catch:{ Exception -> 0x07a8 }
                    r8 = r4.username;	 Catch:{ Exception -> 0x07a8 }
                    r7.append(r8);	 Catch:{ Exception -> 0x07a8 }
                    r7 = r7.toString();	 Catch:{ Exception -> 0x07a8 }
                    r8 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x07a8 }
                    r8.<init>();	 Catch:{ Exception -> 0x07a8 }
                    r9 = "@";	 Catch:{ Exception -> 0x07a8 }
                    r8.append(r9);	 Catch:{ Exception -> 0x07a8 }
                    r8.append(r2);	 Catch:{ Exception -> 0x07a8 }
                    r8 = r8.toString();	 Catch:{ Exception -> 0x07a8 }
                    r9 = 0;	 Catch:{ Exception -> 0x07a8 }
                    r7 = org.telegram.messenger.AndroidUtilities.generateSearchName(r7, r9, r8);	 Catch:{ Exception -> 0x07a8 }
                    r6.name = r7;	 Catch:{ Exception -> 0x07a8 }
                    r6.object = r4;	 Catch:{ Exception -> 0x07a8 }
                    r13 = r13 + 1;	 Catch:{ Exception -> 0x07a8 }
                    goto L_0x02c7;	 Catch:{ Exception -> 0x07a8 }
                    r34 = r9;	 Catch:{ Exception -> 0x07a8 }
                    goto L_0x02c7;	 Catch:{ Exception -> 0x07a8 }
                    r31 = r4;	 Catch:{ Exception -> 0x07a8 }
                    r32 = r6;	 Catch:{ Exception -> 0x07a8 }
                    r33 = r7;	 Catch:{ Exception -> 0x07a8 }
                    r34 = r9;	 Catch:{ Exception -> 0x07a8 }
                    r12 = r12 + 1;	 Catch:{ Exception -> 0x07a8 }
                    r18 = r3;	 Catch:{ Exception -> 0x07a8 }
                    r2 = r28;	 Catch:{ Exception -> 0x07a8 }
                    r3 = r29;	 Catch:{ Exception -> 0x07a8 }
                    r8 = r30;	 Catch:{ Exception -> 0x07a8 }
                    goto L_0x01dc;	 Catch:{ Exception -> 0x07a8 }
                    r29 = r3;	 Catch:{ Exception -> 0x07a8 }
                    r34 = r9;	 Catch:{ Exception -> 0x07a8 }
                    r22 = r15;	 Catch:{ Exception -> 0x07a8 }
                    r2 = r26;	 Catch:{ Exception -> 0x07a8 }
                    r3 = r29;	 Catch:{ Exception -> 0x07a8 }
                    r9 = r34;	 Catch:{ Exception -> 0x07a8 }
                    goto L_0x01a7;	 Catch:{ Exception -> 0x07a8 }
                    r26 = r2;	 Catch:{ Exception -> 0x07a8 }
                    r29 = r3;	 Catch:{ Exception -> 0x07a8 }
                    r34 = r9;	 Catch:{ Exception -> 0x07a8 }
                    r15 = r22;	 Catch:{ Exception -> 0x07a8 }
                    r5.dispose();	 Catch:{ Exception -> 0x07a8 }
                    goto L_0x02e5;	 Catch:{ Exception -> 0x07a8 }
                    r26 = r2;	 Catch:{ Exception -> 0x07a8 }
                    r29 = r3;	 Catch:{ Exception -> 0x07a8 }
                    r34 = r9;	 Catch:{ Exception -> 0x07a8 }
                    r15 = r22;	 Catch:{ Exception -> 0x07a8 }
                    r2 = r10.isEmpty();	 Catch:{ Exception -> 0x07a8 }
                    if (r2 != 0) goto L_0x03d0;	 Catch:{ Exception -> 0x07a8 }
                    r2 = org.telegram.ui.Adapters.DialogsSearchAdapter.this;	 Catch:{ Exception -> 0x07a8 }
                    r2 = r2.currentAccount;	 Catch:{ Exception -> 0x07a8 }
                    r2 = org.telegram.messenger.MessagesStorage.getInstance(r2);	 Catch:{ Exception -> 0x07a8 }
                    r2 = r2.getDatabase();	 Catch:{ Exception -> 0x07a8 }
                    r3 = java.util.Locale.US;	 Catch:{ Exception -> 0x07a8 }
                    r4 = "SELECT data, name FROM chats WHERE uid IN(%s)";	 Catch:{ Exception -> 0x07a8 }
                    r6 = 1;	 Catch:{ Exception -> 0x07a8 }
                    r7 = new java.lang.Object[r6];	 Catch:{ Exception -> 0x07a8 }
                    r6 = ",";	 Catch:{ Exception -> 0x07a8 }
                    r6 = android.text.TextUtils.join(r6, r10);	 Catch:{ Exception -> 0x07a8 }
                    r8 = 0;	 Catch:{ Exception -> 0x07a8 }
                    r7[r8] = r6;	 Catch:{ Exception -> 0x07a8 }
                    r3 = java.lang.String.format(r3, r4, r7);	 Catch:{ Exception -> 0x07a8 }
                    r4 = new java.lang.Object[r8];	 Catch:{ Exception -> 0x07a8 }
                    r2 = r2.queryFinalized(r3, r4);	 Catch:{ Exception -> 0x07a8 }
                    r5 = r2;	 Catch:{ Exception -> 0x07a8 }
                    r2 = r5.next();	 Catch:{ Exception -> 0x07a8 }
                    if (r2 == 0) goto L_0x03cd;	 Catch:{ Exception -> 0x07a8 }
                    r2 = 1;	 Catch:{ Exception -> 0x07a8 }
                    r3 = r5.stringValue(r2);	 Catch:{ Exception -> 0x07a8 }
                    r2 = r3;	 Catch:{ Exception -> 0x07a8 }
                    r3 = org.telegram.messenger.LocaleController.getInstance();	 Catch:{ Exception -> 0x07a8 }
                    r3 = r3.getTranslitString(r2);	 Catch:{ Exception -> 0x07a8 }
                    r4 = r2.equals(r3);	 Catch:{ Exception -> 0x07a8 }
                    if (r4 == 0) goto L_0x032f;	 Catch:{ Exception -> 0x07a8 }
                    r3 = 0;	 Catch:{ Exception -> 0x07a8 }
                    r4 = r15.length;	 Catch:{ Exception -> 0x07a8 }
                    r6 = 0;	 Catch:{ Exception -> 0x07a8 }
                    if (r6 >= r4) goto L_0x03cb;	 Catch:{ Exception -> 0x07a8 }
                    r7 = r15[r6];	 Catch:{ Exception -> 0x07a8 }
                    r8 = r2.startsWith(r7);	 Catch:{ Exception -> 0x07a8 }
                    if (r8 != 0) goto L_0x0375;	 Catch:{ Exception -> 0x07a8 }
                    r8 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x07a8 }
                    r8.<init>();	 Catch:{ Exception -> 0x07a8 }
                    r9 = " ";	 Catch:{ Exception -> 0x07a8 }
                    r8.append(r9);	 Catch:{ Exception -> 0x07a8 }
                    r8.append(r7);	 Catch:{ Exception -> 0x07a8 }
                    r8 = r8.toString();	 Catch:{ Exception -> 0x07a8 }
                    r8 = r2.contains(r8);	 Catch:{ Exception -> 0x07a8 }
                    if (r8 != 0) goto L_0x0375;	 Catch:{ Exception -> 0x07a8 }
                    if (r3 == 0) goto L_0x0372;	 Catch:{ Exception -> 0x07a8 }
                    r8 = r3.startsWith(r7);	 Catch:{ Exception -> 0x07a8 }
                    if (r8 != 0) goto L_0x0375;	 Catch:{ Exception -> 0x07a8 }
                    r8 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x07a8 }
                    r8.<init>();	 Catch:{ Exception -> 0x07a8 }
                    r9 = " ";	 Catch:{ Exception -> 0x07a8 }
                    r8.append(r9);	 Catch:{ Exception -> 0x07a8 }
                    r8.append(r7);	 Catch:{ Exception -> 0x07a8 }
                    r8 = r8.toString();	 Catch:{ Exception -> 0x07a8 }
                    r8 = r3.contains(r8);	 Catch:{ Exception -> 0x07a8 }
                    if (r8 == 0) goto L_0x0372;	 Catch:{ Exception -> 0x07a8 }
                    goto L_0x0375;	 Catch:{ Exception -> 0x07a8 }
                    r6 = r6 + 1;	 Catch:{ Exception -> 0x07a8 }
                    goto L_0x0331;	 Catch:{ Exception -> 0x07a8 }
                    r4 = 0;	 Catch:{ Exception -> 0x07a8 }
                    r6 = r5.byteBufferValue(r4);	 Catch:{ Exception -> 0x07a8 }
                    if (r6 == 0) goto L_0x03cb;	 Catch:{ Exception -> 0x07a8 }
                    r8 = r6.readInt32(r4);	 Catch:{ Exception -> 0x07a8 }
                    r8 = org.telegram.tgnet.TLRPC.Chat.TLdeserialize(r6, r8, r4);	 Catch:{ Exception -> 0x07a8 }
                    r4 = r8;	 Catch:{ Exception -> 0x07a8 }
                    r6.reuse();	 Catch:{ Exception -> 0x07a8 }
                    if (r4 == 0) goto L_0x03c7;	 Catch:{ Exception -> 0x07a8 }
                    r8 = r4.deactivated;	 Catch:{ Exception -> 0x07a8 }
                    if (r8 != 0) goto L_0x03c7;	 Catch:{ Exception -> 0x07a8 }
                    r8 = org.telegram.messenger.ChatObject.isChannel(r4);	 Catch:{ Exception -> 0x07a8 }
                    if (r8 == 0) goto L_0x03a0;	 Catch:{ Exception -> 0x07a8 }
                    r8 = org.telegram.messenger.ChatObject.isNotInChat(r4);	 Catch:{ Exception -> 0x07a8 }
                    if (r8 != 0) goto L_0x039b;	 Catch:{ Exception -> 0x07a8 }
                    goto L_0x03a0;	 Catch:{ Exception -> 0x07a8 }
                    r35 = r2;	 Catch:{ Exception -> 0x07a8 }
                    r36 = r3;	 Catch:{ Exception -> 0x07a8 }
                    goto L_0x03cb;	 Catch:{ Exception -> 0x07a8 }
                    r8 = r4.id;	 Catch:{ Exception -> 0x07a8 }
                    if (r8 <= 0) goto L_0x03a9;	 Catch:{ Exception -> 0x07a8 }
                    r8 = r4.id;	 Catch:{ Exception -> 0x07a8 }
                    r8 = -r8;	 Catch:{ Exception -> 0x07a8 }
                    r8 = (long) r8;	 Catch:{ Exception -> 0x07a8 }
                    goto L_0x03af;	 Catch:{ Exception -> 0x07a8 }
                    r8 = r4.id;	 Catch:{ Exception -> 0x07a8 }
                    r8 = org.telegram.messenger.AndroidUtilities.makeBroadcastId(r8);	 Catch:{ Exception -> 0x07a8 }
                    r12 = r14.get(r8);	 Catch:{ Exception -> 0x07a8 }
                    r12 = (org.telegram.ui.Adapters.DialogsSearchAdapter.DialogSearchResult) r12;	 Catch:{ Exception -> 0x07a8 }
                    r35 = r2;	 Catch:{ Exception -> 0x07a8 }
                    r2 = r4.title;	 Catch:{ Exception -> 0x07a8 }
                    r36 = r3;	 Catch:{ Exception -> 0x07a8 }
                    r3 = 0;	 Catch:{ Exception -> 0x07a8 }
                    r2 = org.telegram.messenger.AndroidUtilities.generateSearchName(r2, r3, r7);	 Catch:{ Exception -> 0x07a8 }
                    r12.name = r2;	 Catch:{ Exception -> 0x07a8 }
                    r12.object = r4;	 Catch:{ Exception -> 0x07a8 }
                    r13 = r13 + 1;	 Catch:{ Exception -> 0x07a8 }
                    goto L_0x03cb;	 Catch:{ Exception -> 0x07a8 }
                    r35 = r2;	 Catch:{ Exception -> 0x07a8 }
                    r36 = r3;	 Catch:{ Exception -> 0x07a8 }
                    goto L_0x0314;	 Catch:{ Exception -> 0x07a8 }
                    r5.dispose();	 Catch:{ Exception -> 0x07a8 }
                    r2 = r11.isEmpty();	 Catch:{ Exception -> 0x07a8 }
                    if (r2 != 0) goto L_0x0605;	 Catch:{ Exception -> 0x07a8 }
                    r2 = org.telegram.ui.Adapters.DialogsSearchAdapter.this;	 Catch:{ Exception -> 0x07a8 }
                    r2 = r2.currentAccount;	 Catch:{ Exception -> 0x07a8 }
                    r2 = org.telegram.messenger.MessagesStorage.getInstance(r2);	 Catch:{ Exception -> 0x07a8 }
                    r2 = r2.getDatabase();	 Catch:{ Exception -> 0x07a8 }
                    r4 = java.util.Locale.US;	 Catch:{ Exception -> 0x07a8 }
                    r6 = "SELECT q.data, u.name, q.user, q.g, q.authkey, q.ttl, u.data, u.status, q.layer, q.seq_in, q.seq_out, q.use_count, q.exchange_id, q.key_date, q.fprint, q.fauthkey, q.khash, q.in_seq_no, q.admin_id, q.mtproto_seq FROM enc_chats as q INNER JOIN users as u ON q.user = u.uid WHERE q.uid IN(%s)";	 Catch:{ Exception -> 0x07a8 }
                    r7 = 1;	 Catch:{ Exception -> 0x07a8 }
                    r8 = new java.lang.Object[r7];	 Catch:{ Exception -> 0x07a8 }
                    r7 = ",";	 Catch:{ Exception -> 0x07a8 }
                    r7 = android.text.TextUtils.join(r7, r11);	 Catch:{ Exception -> 0x07a8 }
                    r9 = 0;	 Catch:{ Exception -> 0x07a8 }
                    r8[r9] = r7;	 Catch:{ Exception -> 0x07a8 }
                    r4 = java.lang.String.format(r4, r6, r8);	 Catch:{ Exception -> 0x07a8 }
                    r6 = new java.lang.Object[r9];	 Catch:{ Exception -> 0x07a8 }
                    r2 = r2.queryFinalized(r4, r6);	 Catch:{ Exception -> 0x07a8 }
                    r5 = r2;	 Catch:{ Exception -> 0x07a8 }
                    r2 = r5.next();	 Catch:{ Exception -> 0x07a8 }
                    if (r2 == 0) goto L_0x05fb;	 Catch:{ Exception -> 0x07a8 }
                    r2 = 1;	 Catch:{ Exception -> 0x07a8 }
                    r4 = r5.stringValue(r2);	 Catch:{ Exception -> 0x07a8 }
                    r2 = r4;	 Catch:{ Exception -> 0x07a8 }
                    r4 = org.telegram.messenger.LocaleController.getInstance();	 Catch:{ Exception -> 0x07a8 }
                    r4 = r4.getTranslitString(r2);	 Catch:{ Exception -> 0x07a8 }
                    r6 = r2.equals(r4);	 Catch:{ Exception -> 0x07a8 }
                    if (r6 == 0) goto L_0x041a;	 Catch:{ Exception -> 0x07a8 }
                    r4 = 0;	 Catch:{ Exception -> 0x07a8 }
                    r6 = 0;	 Catch:{ Exception -> 0x07a8 }
                    r7 = ";;;";	 Catch:{ Exception -> 0x07a8 }
                    r7 = r2.lastIndexOf(r7);	 Catch:{ Exception -> 0x07a8 }
                    r8 = -1;	 Catch:{ Exception -> 0x07a8 }
                    if (r7 == r8) goto L_0x042b;	 Catch:{ Exception -> 0x07a8 }
                    r8 = r7 + 2;	 Catch:{ Exception -> 0x07a8 }
                    r8 = r2.substring(r8);	 Catch:{ Exception -> 0x07a8 }
                    r6 = r8;	 Catch:{ Exception -> 0x07a8 }
                    r8 = 0;	 Catch:{ Exception -> 0x07a8 }
                    r9 = r8;	 Catch:{ Exception -> 0x07a8 }
                    r8 = 0;	 Catch:{ Exception -> 0x07a8 }
                    r12 = r15.length;	 Catch:{ Exception -> 0x07a8 }
                    if (r8 >= r12) goto L_0x05ed;	 Catch:{ Exception -> 0x07a8 }
                    r12 = r15[r8];	 Catch:{ Exception -> 0x07a8 }
                    r18 = r2.startsWith(r12);	 Catch:{ Exception -> 0x07a8 }
                    if (r18 != 0) goto L_0x047c;	 Catch:{ Exception -> 0x07a8 }
                    r3 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x07a8 }
                    r3.<init>();	 Catch:{ Exception -> 0x07a8 }
                    r37 = r7;	 Catch:{ Exception -> 0x07a8 }
                    r7 = " ";	 Catch:{ Exception -> 0x07a8 }
                    r3.append(r7);	 Catch:{ Exception -> 0x07a8 }
                    r3.append(r12);	 Catch:{ Exception -> 0x07a8 }
                    r3 = r3.toString();	 Catch:{ Exception -> 0x07a8 }
                    r3 = r2.contains(r3);	 Catch:{ Exception -> 0x07a8 }
                    if (r3 != 0) goto L_0x047e;	 Catch:{ Exception -> 0x07a8 }
                    if (r4 == 0) goto L_0x0472;	 Catch:{ Exception -> 0x07a8 }
                    r3 = r4.startsWith(r12);	 Catch:{ Exception -> 0x07a8 }
                    if (r3 != 0) goto L_0x047e;	 Catch:{ Exception -> 0x07a8 }
                    r3 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x07a8 }
                    r3.<init>();	 Catch:{ Exception -> 0x07a8 }
                    r7 = " ";	 Catch:{ Exception -> 0x07a8 }
                    r3.append(r7);	 Catch:{ Exception -> 0x07a8 }
                    r3.append(r12);	 Catch:{ Exception -> 0x07a8 }
                    r3 = r3.toString();	 Catch:{ Exception -> 0x07a8 }
                    r3 = r4.contains(r3);	 Catch:{ Exception -> 0x07a8 }
                    if (r3 == 0) goto L_0x0472;	 Catch:{ Exception -> 0x07a8 }
                    goto L_0x047e;	 Catch:{ Exception -> 0x07a8 }
                    if (r6 == 0) goto L_0x0480;	 Catch:{ Exception -> 0x07a8 }
                    r3 = r6.startsWith(r12);	 Catch:{ Exception -> 0x07a8 }
                    if (r3 == 0) goto L_0x0480;	 Catch:{ Exception -> 0x07a8 }
                    r3 = 2;	 Catch:{ Exception -> 0x07a8 }
                    goto L_0x047f;	 Catch:{ Exception -> 0x07a8 }
                    r37 = r7;	 Catch:{ Exception -> 0x07a8 }
                    r3 = 1;	 Catch:{ Exception -> 0x07a8 }
                    r9 = r3;	 Catch:{ Exception -> 0x07a8 }
                    if (r9 == 0) goto L_0x05d7;	 Catch:{ Exception -> 0x07a8 }
                    r3 = 0;	 Catch:{ Exception -> 0x07a8 }
                    r7 = 0;	 Catch:{ Exception -> 0x07a8 }
                    r38 = r2;	 Catch:{ Exception -> 0x07a8 }
                    r2 = 0;	 Catch:{ Exception -> 0x07a8 }
                    r18 = r5.byteBufferValue(r2);	 Catch:{ Exception -> 0x07a8 }
                    r39 = r18;	 Catch:{ Exception -> 0x07a8 }
                    r2 = r39;	 Catch:{ Exception -> 0x07a8 }
                    if (r2 == 0) goto L_0x04a3;	 Catch:{ Exception -> 0x07a8 }
                    r40 = r3;	 Catch:{ Exception -> 0x07a8 }
                    r41 = r4;	 Catch:{ Exception -> 0x07a8 }
                    r3 = 0;	 Catch:{ Exception -> 0x07a8 }
                    r4 = r2.readInt32(r3);	 Catch:{ Exception -> 0x07a8 }
                    r4 = org.telegram.tgnet.TLRPC.EncryptedChat.TLdeserialize(r2, r4, r3);	 Catch:{ Exception -> 0x07a8 }
                    r3 = r4;	 Catch:{ Exception -> 0x07a8 }
                    r2.reuse();	 Catch:{ Exception -> 0x07a8 }
                    goto L_0x04a7;	 Catch:{ Exception -> 0x07a8 }
                    r40 = r3;	 Catch:{ Exception -> 0x07a8 }
                    r41 = r4;	 Catch:{ Exception -> 0x07a8 }
                    r4 = 6;	 Catch:{ Exception -> 0x07a8 }
                    r4 = r5.byteBufferValue(r4);	 Catch:{ Exception -> 0x07a8 }
                    r2 = r4;	 Catch:{ Exception -> 0x07a8 }
                    if (r2 == 0) goto L_0x04bf;	 Catch:{ Exception -> 0x07a8 }
                    r42 = r6;	 Catch:{ Exception -> 0x07a8 }
                    r4 = 0;	 Catch:{ Exception -> 0x07a8 }
                    r6 = r2.readInt32(r4);	 Catch:{ Exception -> 0x07a8 }
                    r6 = org.telegram.tgnet.TLRPC.User.TLdeserialize(r2, r6, r4);	 Catch:{ Exception -> 0x07a8 }
                    r7 = r6;	 Catch:{ Exception -> 0x07a8 }
                    r2.reuse();	 Catch:{ Exception -> 0x07a8 }
                    goto L_0x04c1;	 Catch:{ Exception -> 0x07a8 }
                    r42 = r6;	 Catch:{ Exception -> 0x07a8 }
                    if (r3 == 0) goto L_0x05ed;	 Catch:{ Exception -> 0x07a8 }
                    if (r7 == 0) goto L_0x05ed;	 Catch:{ Exception -> 0x07a8 }
                    r4 = r3.id;	 Catch:{ Exception -> 0x07a8 }
                    r43 = r10;	 Catch:{ Exception -> 0x07a8 }
                    r44 = r11;	 Catch:{ Exception -> 0x07a8 }
                    r10 = (long) r4;	 Catch:{ Exception -> 0x07a8 }
                    r4 = 32;	 Catch:{ Exception -> 0x07a8 }
                    r10 = r10 << r4;	 Catch:{ Exception -> 0x07a8 }
                    r6 = r14.get(r10);	 Catch:{ Exception -> 0x07a8 }
                    r6 = (org.telegram.ui.Adapters.DialogsSearchAdapter.DialogSearchResult) r6;	 Catch:{ Exception -> 0x07a8 }
                    r10 = 2;	 Catch:{ Exception -> 0x07a8 }
                    r11 = r5.intValue(r10);	 Catch:{ Exception -> 0x07a8 }
                    r3.user_id = r11;	 Catch:{ Exception -> 0x07a8 }
                    r10 = 3;	 Catch:{ Exception -> 0x07a8 }
                    r11 = r5.byteArrayValue(r10);	 Catch:{ Exception -> 0x07a8 }
                    r3.a_or_b = r11;	 Catch:{ Exception -> 0x07a8 }
                    r10 = 4;	 Catch:{ Exception -> 0x07a8 }
                    r10 = r5.byteArrayValue(r10);	 Catch:{ Exception -> 0x07a8 }
                    r3.auth_key = r10;	 Catch:{ Exception -> 0x07a8 }
                    r10 = 5;	 Catch:{ Exception -> 0x07a8 }
                    r10 = r5.intValue(r10);	 Catch:{ Exception -> 0x07a8 }
                    r3.ttl = r10;	 Catch:{ Exception -> 0x07a8 }
                    r10 = 8;	 Catch:{ Exception -> 0x07a8 }
                    r10 = r5.intValue(r10);	 Catch:{ Exception -> 0x07a8 }
                    r3.layer = r10;	 Catch:{ Exception -> 0x07a8 }
                    r10 = 9;	 Catch:{ Exception -> 0x07a8 }
                    r10 = r5.intValue(r10);	 Catch:{ Exception -> 0x07a8 }
                    r3.seq_in = r10;	 Catch:{ Exception -> 0x07a8 }
                    r10 = 10;	 Catch:{ Exception -> 0x07a8 }
                    r10 = r5.intValue(r10);	 Catch:{ Exception -> 0x07a8 }
                    r3.seq_out = r10;	 Catch:{ Exception -> 0x07a8 }
                    r10 = 11;	 Catch:{ Exception -> 0x07a8 }
                    r10 = r5.intValue(r10);	 Catch:{ Exception -> 0x07a8 }
                    r11 = r10 >> 16;	 Catch:{ Exception -> 0x07a8 }
                    r11 = (short) r11;	 Catch:{ Exception -> 0x07a8 }
                    r3.key_use_count_in = r11;	 Catch:{ Exception -> 0x07a8 }
                    r11 = (short) r10;	 Catch:{ Exception -> 0x07a8 }
                    r3.key_use_count_out = r11;	 Catch:{ Exception -> 0x07a8 }
                    r11 = 12;	 Catch:{ Exception -> 0x07a8 }
                    r45 = r10;	 Catch:{ Exception -> 0x07a8 }
                    r10 = r5.longValue(r11);	 Catch:{ Exception -> 0x07a8 }
                    r3.exchange_id = r10;	 Catch:{ Exception -> 0x07a8 }
                    r10 = 13;	 Catch:{ Exception -> 0x07a8 }
                    r10 = r5.intValue(r10);	 Catch:{ Exception -> 0x07a8 }
                    r3.key_create_date = r10;	 Catch:{ Exception -> 0x07a8 }
                    r10 = 14;	 Catch:{ Exception -> 0x07a8 }
                    r10 = r5.longValue(r10);	 Catch:{ Exception -> 0x07a8 }
                    r3.future_key_fingerprint = r10;	 Catch:{ Exception -> 0x07a8 }
                    r10 = 15;	 Catch:{ Exception -> 0x07a8 }
                    r10 = r5.byteArrayValue(r10);	 Catch:{ Exception -> 0x07a8 }
                    r3.future_auth_key = r10;	 Catch:{ Exception -> 0x07a8 }
                    r10 = 16;	 Catch:{ Exception -> 0x07a8 }
                    r10 = r5.byteArrayValue(r10);	 Catch:{ Exception -> 0x07a8 }
                    r3.key_hash = r10;	 Catch:{ Exception -> 0x07a8 }
                    r10 = 17;	 Catch:{ Exception -> 0x07a8 }
                    r10 = r5.intValue(r10);	 Catch:{ Exception -> 0x07a8 }
                    r3.in_seq_no = r10;	 Catch:{ Exception -> 0x07a8 }
                    r10 = 18;	 Catch:{ Exception -> 0x07a8 }
                    r10 = r5.intValue(r10);	 Catch:{ Exception -> 0x07a8 }
                    if (r10 == 0) goto L_0x0553;	 Catch:{ Exception -> 0x07a8 }
                    r3.admin_id = r10;	 Catch:{ Exception -> 0x07a8 }
                    r11 = 19;	 Catch:{ Exception -> 0x07a8 }
                    r11 = r5.intValue(r11);	 Catch:{ Exception -> 0x07a8 }
                    r3.mtproto_seq = r11;	 Catch:{ Exception -> 0x07a8 }
                    r11 = r7.status;	 Catch:{ Exception -> 0x07a8 }
                    if (r11 == 0) goto L_0x0568;	 Catch:{ Exception -> 0x07a8 }
                    r11 = r7.status;	 Catch:{ Exception -> 0x07a8 }
                    r4 = 7;	 Catch:{ Exception -> 0x07a8 }
                    r4 = r5.intValue(r4);	 Catch:{ Exception -> 0x07a8 }
                    r11.expires = r4;	 Catch:{ Exception -> 0x07a8 }
                    r4 = 1;	 Catch:{ Exception -> 0x07a8 }
                    if (r9 != r4) goto L_0x059c;	 Catch:{ Exception -> 0x07a8 }
                    r4 = new android.text.SpannableStringBuilder;	 Catch:{ Exception -> 0x07a8 }
                    r11 = r7.first_name;	 Catch:{ Exception -> 0x07a8 }
                    r46 = r2;	 Catch:{ Exception -> 0x07a8 }
                    r2 = r7.last_name;	 Catch:{ Exception -> 0x07a8 }
                    r2 = org.telegram.messenger.ContactsController.formatName(r11, r2);	 Catch:{ Exception -> 0x07a8 }
                    r4.<init>(r2);	 Catch:{ Exception -> 0x07a8 }
                    r6.name = r4;	 Catch:{ Exception -> 0x07a8 }
                    r2 = r6.name;	 Catch:{ Exception -> 0x07a8 }
                    r2 = (android.text.SpannableStringBuilder) r2;	 Catch:{ Exception -> 0x07a8 }
                    r4 = new android.text.style.ForegroundColorSpan;	 Catch:{ Exception -> 0x07a8 }
                    r11 = "chats_secretName";	 Catch:{ Exception -> 0x07a8 }
                    r11 = org.telegram.ui.ActionBar.Theme.getColor(r11);	 Catch:{ Exception -> 0x07a8 }
                    r4.<init>(r11);	 Catch:{ Exception -> 0x07a8 }
                    r11 = r6.name;	 Catch:{ Exception -> 0x07a8 }
                    r11 = r11.length();	 Catch:{ Exception -> 0x07a8 }
                    r47 = r9;	 Catch:{ Exception -> 0x07a8 }
                    r9 = 33;	 Catch:{ Exception -> 0x07a8 }
                    r48 = r10;	 Catch:{ Exception -> 0x07a8 }
                    r10 = 0;	 Catch:{ Exception -> 0x07a8 }
                    r2.setSpan(r4, r10, r11, r9);	 Catch:{ Exception -> 0x07a8 }
                    goto L_0x05cd;	 Catch:{ Exception -> 0x07a8 }
                    r46 = r2;	 Catch:{ Exception -> 0x07a8 }
                    r47 = r9;	 Catch:{ Exception -> 0x07a8 }
                    r48 = r10;	 Catch:{ Exception -> 0x07a8 }
                    r2 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x07a8 }
                    r2.<init>();	 Catch:{ Exception -> 0x07a8 }
                    r4 = "@";	 Catch:{ Exception -> 0x07a8 }
                    r2.append(r4);	 Catch:{ Exception -> 0x07a8 }
                    r4 = r7.username;	 Catch:{ Exception -> 0x07a8 }
                    r2.append(r4);	 Catch:{ Exception -> 0x07a8 }
                    r2 = r2.toString();	 Catch:{ Exception -> 0x07a8 }
                    r4 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x07a8 }
                    r4.<init>();	 Catch:{ Exception -> 0x07a8 }
                    r9 = "@";	 Catch:{ Exception -> 0x07a8 }
                    r4.append(r9);	 Catch:{ Exception -> 0x07a8 }
                    r4.append(r12);	 Catch:{ Exception -> 0x07a8 }
                    r4 = r4.toString();	 Catch:{ Exception -> 0x07a8 }
                    r9 = 0;	 Catch:{ Exception -> 0x07a8 }
                    r2 = org.telegram.messenger.AndroidUtilities.generateSearchName(r2, r9, r4);	 Catch:{ Exception -> 0x07a8 }
                    r6.name = r2;	 Catch:{ Exception -> 0x07a8 }
                    r6.object = r3;	 Catch:{ Exception -> 0x07a8 }
                    r2 = r23;	 Catch:{ Exception -> 0x07a8 }
                    r2.add(r7);	 Catch:{ Exception -> 0x07a8 }
                    r13 = r13 + 1;	 Catch:{ Exception -> 0x07a8 }
                    goto L_0x05f3;	 Catch:{ Exception -> 0x07a8 }
                    r38 = r2;	 Catch:{ Exception -> 0x07a8 }
                    r41 = r4;	 Catch:{ Exception -> 0x07a8 }
                    r42 = r6;	 Catch:{ Exception -> 0x07a8 }
                    r47 = r9;	 Catch:{ Exception -> 0x07a8 }
                    r43 = r10;	 Catch:{ Exception -> 0x07a8 }
                    r44 = r11;	 Catch:{ Exception -> 0x07a8 }
                    r2 = r23;	 Catch:{ Exception -> 0x07a8 }
                    r8 = r8 + 1;	 Catch:{ Exception -> 0x07a8 }
                    r7 = r37;	 Catch:{ Exception -> 0x07a8 }
                    r2 = r38;	 Catch:{ Exception -> 0x07a8 }
                    goto L_0x042e;	 Catch:{ Exception -> 0x07a8 }
                    r43 = r10;	 Catch:{ Exception -> 0x07a8 }
                    r44 = r11;	 Catch:{ Exception -> 0x07a8 }
                    r2 = r23;	 Catch:{ Exception -> 0x07a8 }
                    r23 = r2;	 Catch:{ Exception -> 0x07a8 }
                    r10 = r43;	 Catch:{ Exception -> 0x07a8 }
                    r11 = r44;	 Catch:{ Exception -> 0x07a8 }
                    goto L_0x03ff;	 Catch:{ Exception -> 0x07a8 }
                    r43 = r10;	 Catch:{ Exception -> 0x07a8 }
                    r44 = r11;	 Catch:{ Exception -> 0x07a8 }
                    r2 = r23;	 Catch:{ Exception -> 0x07a8 }
                    r5.dispose();	 Catch:{ Exception -> 0x07a8 }
                    goto L_0x060b;	 Catch:{ Exception -> 0x07a8 }
                    r43 = r10;	 Catch:{ Exception -> 0x07a8 }
                    r44 = r11;	 Catch:{ Exception -> 0x07a8 }
                    r2 = r23;	 Catch:{ Exception -> 0x07a8 }
                    r3 = new java.util.ArrayList;	 Catch:{ Exception -> 0x07a8 }
                    r3.<init>(r13);	 Catch:{ Exception -> 0x07a8 }
                    r4 = 0;	 Catch:{ Exception -> 0x07a8 }
                    r6 = r14.size();	 Catch:{ Exception -> 0x07a8 }
                    if (r4 >= r6) goto L_0x062b;	 Catch:{ Exception -> 0x07a8 }
                    r6 = r14.valueAt(r4);	 Catch:{ Exception -> 0x07a8 }
                    r6 = (org.telegram.ui.Adapters.DialogsSearchAdapter.DialogSearchResult) r6;	 Catch:{ Exception -> 0x07a8 }
                    r7 = r6.object;	 Catch:{ Exception -> 0x07a8 }
                    if (r7 == 0) goto L_0x0628;	 Catch:{ Exception -> 0x07a8 }
                    r7 = r6.name;	 Catch:{ Exception -> 0x07a8 }
                    if (r7 == 0) goto L_0x0628;	 Catch:{ Exception -> 0x07a8 }
                    r3.add(r6);	 Catch:{ Exception -> 0x07a8 }
                    r4 = r4 + 1;	 Catch:{ Exception -> 0x07a8 }
                    goto L_0x0611;	 Catch:{ Exception -> 0x07a8 }
                    r4 = new org.telegram.ui.Adapters.DialogsSearchAdapter$6$1;	 Catch:{ Exception -> 0x07a8 }
                    r4.<init>();	 Catch:{ Exception -> 0x07a8 }
                    java.util.Collections.sort(r3, r4);	 Catch:{ Exception -> 0x07a8 }
                    r4 = new java.util.ArrayList;	 Catch:{ Exception -> 0x07a8 }
                    r4.<init>();	 Catch:{ Exception -> 0x07a8 }
                    r6 = new java.util.ArrayList;	 Catch:{ Exception -> 0x07a8 }
                    r6.<init>();	 Catch:{ Exception -> 0x07a8 }
                    r7 = 0;	 Catch:{ Exception -> 0x07a8 }
                    r8 = r3.size();	 Catch:{ Exception -> 0x07a8 }
                    if (r7 >= r8) goto L_0x0657;	 Catch:{ Exception -> 0x07a8 }
                    r8 = r3.get(r7);	 Catch:{ Exception -> 0x07a8 }
                    r8 = (org.telegram.ui.Adapters.DialogsSearchAdapter.DialogSearchResult) r8;	 Catch:{ Exception -> 0x07a8 }
                    r9 = r8.object;	 Catch:{ Exception -> 0x07a8 }
                    r4.add(r9);	 Catch:{ Exception -> 0x07a8 }
                    r9 = r8.name;	 Catch:{ Exception -> 0x07a8 }
                    r6.add(r9);	 Catch:{ Exception -> 0x07a8 }
                    r7 = r7 + 1;	 Catch:{ Exception -> 0x07a8 }
                    goto L_0x063e;	 Catch:{ Exception -> 0x07a8 }
                    r7 = org.telegram.ui.Adapters.DialogsSearchAdapter.this;	 Catch:{ Exception -> 0x07a8 }
                    r7 = r7.dialogsType;	 Catch:{ Exception -> 0x07a8 }
                    r8 = 2;	 Catch:{ Exception -> 0x07a8 }
                    if (r7 == r8) goto L_0x079e;	 Catch:{ Exception -> 0x07a8 }
                    r7 = org.telegram.ui.Adapters.DialogsSearchAdapter.this;	 Catch:{ Exception -> 0x07a8 }
                    r7 = r7.currentAccount;	 Catch:{ Exception -> 0x07a8 }
                    r7 = org.telegram.messenger.MessagesStorage.getInstance(r7);	 Catch:{ Exception -> 0x07a8 }
                    r7 = r7.getDatabase();	 Catch:{ Exception -> 0x07a8 }
                    r8 = "SELECT u.data, u.status, u.name, u.uid FROM users as u INNER JOIN contacts as c ON u.uid = c.uid";	 Catch:{ Exception -> 0x07a8 }
                    r9 = 0;	 Catch:{ Exception -> 0x07a8 }
                    r10 = new java.lang.Object[r9];	 Catch:{ Exception -> 0x07a8 }
                    r7 = r7.queryFinalized(r8, r10);	 Catch:{ Exception -> 0x07a8 }
                    r5 = r7;	 Catch:{ Exception -> 0x07a8 }
                    r7 = r5.next();	 Catch:{ Exception -> 0x07a8 }
                    if (r7 == 0) goto L_0x0798;	 Catch:{ Exception -> 0x07a8 }
                    r7 = 3;	 Catch:{ Exception -> 0x07a8 }
                    r8 = r5.intValue(r7);	 Catch:{ Exception -> 0x07a8 }
                    r9 = (long) r8;	 Catch:{ Exception -> 0x07a8 }
                    r9 = r14.indexOfKey(r9);	 Catch:{ Exception -> 0x07a8 }
                    if (r9 < 0) goto L_0x068b;	 Catch:{ Exception -> 0x07a8 }
                    goto L_0x0678;	 Catch:{ Exception -> 0x07a8 }
                    r9 = 2;	 Catch:{ Exception -> 0x07a8 }
                    r10 = r5.stringValue(r9);	 Catch:{ Exception -> 0x07a8 }
                    r11 = org.telegram.messenger.LocaleController.getInstance();	 Catch:{ Exception -> 0x07a8 }
                    r11 = r11.getTranslitString(r10);	 Catch:{ Exception -> 0x07a8 }
                    r12 = r10.equals(r11);	 Catch:{ Exception -> 0x07a8 }
                    if (r12 == 0) goto L_0x069f;	 Catch:{ Exception -> 0x07a8 }
                    r11 = 0;	 Catch:{ Exception -> 0x07a8 }
                    r12 = 0;	 Catch:{ Exception -> 0x07a8 }
                    r7 = ";;;";	 Catch:{ Exception -> 0x07a8 }
                    r7 = r10.lastIndexOf(r7);	 Catch:{ Exception -> 0x07a8 }
                    r9 = -1;	 Catch:{ Exception -> 0x07a8 }
                    if (r7 == r9) goto L_0x06b0;	 Catch:{ Exception -> 0x07a8 }
                    r9 = r7 + 3;	 Catch:{ Exception -> 0x07a8 }
                    r9 = r10.substring(r9);	 Catch:{ Exception -> 0x07a8 }
                    r12 = r9;	 Catch:{ Exception -> 0x07a8 }
                    r9 = 0;	 Catch:{ Exception -> 0x07a8 }
                    r49 = r3;	 Catch:{ Exception -> 0x07a8 }
                    r3 = r15.length;	 Catch:{ Exception -> 0x07a8 }
                    r16 = r9;	 Catch:{ Exception -> 0x07a8 }
                    r9 = 0;	 Catch:{ Exception -> 0x07a8 }
                    if (r9 >= r3) goto L_0x0793;	 Catch:{ Exception -> 0x07a8 }
                    r17 = r15[r9];	 Catch:{ Exception -> 0x07a8 }
                    r50 = r17;	 Catch:{ Exception -> 0x07a8 }
                    r51 = r3;	 Catch:{ Exception -> 0x07a8 }
                    r3 = r50;	 Catch:{ Exception -> 0x07a8 }
                    r17 = r10.startsWith(r3);	 Catch:{ Exception -> 0x07a8 }
                    if (r17 != 0) goto L_0x070f;	 Catch:{ Exception -> 0x07a8 }
                    r52 = r7;	 Catch:{ Exception -> 0x07a8 }
                    r7 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x07a8 }
                    r7.<init>();	 Catch:{ Exception -> 0x07a8 }
                    r53 = r8;	 Catch:{ Exception -> 0x07a8 }
                    r8 = " ";	 Catch:{ Exception -> 0x07a8 }
                    r7.append(r8);	 Catch:{ Exception -> 0x07a8 }
                    r7.append(r3);	 Catch:{ Exception -> 0x07a8 }
                    r7 = r7.toString();	 Catch:{ Exception -> 0x07a8 }
                    r7 = r10.contains(r7);	 Catch:{ Exception -> 0x07a8 }
                    if (r7 != 0) goto L_0x0713;	 Catch:{ Exception -> 0x07a8 }
                    if (r11 == 0) goto L_0x0702;	 Catch:{ Exception -> 0x07a8 }
                    r7 = r11.startsWith(r3);	 Catch:{ Exception -> 0x07a8 }
                    if (r7 != 0) goto L_0x0713;	 Catch:{ Exception -> 0x07a8 }
                    r7 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x07a8 }
                    r7.<init>();	 Catch:{ Exception -> 0x07a8 }
                    r8 = " ";	 Catch:{ Exception -> 0x07a8 }
                    r7.append(r8);	 Catch:{ Exception -> 0x07a8 }
                    r7.append(r3);	 Catch:{ Exception -> 0x07a8 }
                    r7 = r7.toString();	 Catch:{ Exception -> 0x07a8 }
                    r7 = r11.contains(r7);	 Catch:{ Exception -> 0x07a8 }
                    if (r7 == 0) goto L_0x0702;	 Catch:{ Exception -> 0x07a8 }
                    goto L_0x0713;	 Catch:{ Exception -> 0x07a8 }
                    if (r12 == 0) goto L_0x070c;	 Catch:{ Exception -> 0x07a8 }
                    r7 = r12.startsWith(r3);	 Catch:{ Exception -> 0x07a8 }
                    if (r7 == 0) goto L_0x070c;	 Catch:{ Exception -> 0x07a8 }
                    r7 = 2;	 Catch:{ Exception -> 0x07a8 }
                    goto L_0x0714;	 Catch:{ Exception -> 0x07a8 }
                    r7 = r16;	 Catch:{ Exception -> 0x07a8 }
                    goto L_0x0714;	 Catch:{ Exception -> 0x07a8 }
                    r52 = r7;	 Catch:{ Exception -> 0x07a8 }
                    r53 = r8;	 Catch:{ Exception -> 0x07a8 }
                    r7 = 1;	 Catch:{ Exception -> 0x07a8 }
                    if (r7 == 0) goto L_0x0780;	 Catch:{ Exception -> 0x07a8 }
                    r8 = 0;	 Catch:{ Exception -> 0x07a8 }
                    r9 = r5.byteBufferValue(r8);	 Catch:{ Exception -> 0x07a8 }
                    if (r9 == 0) goto L_0x0793;	 Catch:{ Exception -> 0x07a8 }
                    r54 = r10;	 Catch:{ Exception -> 0x07a8 }
                    r10 = r9.readInt32(r8);	 Catch:{ Exception -> 0x07a8 }
                    r10 = org.telegram.tgnet.TLRPC.User.TLdeserialize(r9, r10, r8);	 Catch:{ Exception -> 0x07a8 }
                    r9.reuse();	 Catch:{ Exception -> 0x07a8 }
                    r8 = r10.status;	 Catch:{ Exception -> 0x07a8 }
                    if (r8 == 0) goto L_0x073c;	 Catch:{ Exception -> 0x07a8 }
                    r8 = r10.status;	 Catch:{ Exception -> 0x07a8 }
                    r55 = r9;	 Catch:{ Exception -> 0x07a8 }
                    r56 = r11;	 Catch:{ Exception -> 0x07a8 }
                    r9 = 1;	 Catch:{ Exception -> 0x07a8 }
                    r11 = r5.intValue(r9);	 Catch:{ Exception -> 0x07a8 }
                    r8.expires = r11;	 Catch:{ Exception -> 0x07a8 }
                    goto L_0x0740;	 Catch:{ Exception -> 0x07a8 }
                    r55 = r9;	 Catch:{ Exception -> 0x07a8 }
                    r56 = r11;	 Catch:{ Exception -> 0x07a8 }
                    r8 = 1;	 Catch:{ Exception -> 0x07a8 }
                    if (r7 != r8) goto L_0x0750;	 Catch:{ Exception -> 0x07a8 }
                    r9 = r10.first_name;	 Catch:{ Exception -> 0x07a8 }
                    r11 = r10.last_name;	 Catch:{ Exception -> 0x07a8 }
                    r9 = org.telegram.messenger.AndroidUtilities.generateSearchName(r9, r11, r3);	 Catch:{ Exception -> 0x07a8 }
                    r6.add(r9);	 Catch:{ Exception -> 0x07a8 }
                    r11 = 0;	 Catch:{ Exception -> 0x07a8 }
                    goto L_0x077c;	 Catch:{ Exception -> 0x07a8 }
                    r9 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x07a8 }
                    r9.<init>();	 Catch:{ Exception -> 0x07a8 }
                    r11 = "@";	 Catch:{ Exception -> 0x07a8 }
                    r9.append(r11);	 Catch:{ Exception -> 0x07a8 }
                    r11 = r10.username;	 Catch:{ Exception -> 0x07a8 }
                    r9.append(r11);	 Catch:{ Exception -> 0x07a8 }
                    r9 = r9.toString();	 Catch:{ Exception -> 0x07a8 }
                    r11 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x07a8 }
                    r11.<init>();	 Catch:{ Exception -> 0x07a8 }
                    r8 = "@";	 Catch:{ Exception -> 0x07a8 }
                    r11.append(r8);	 Catch:{ Exception -> 0x07a8 }
                    r11.append(r3);	 Catch:{ Exception -> 0x07a8 }
                    r8 = r11.toString();	 Catch:{ Exception -> 0x07a8 }
                    r11 = 0;	 Catch:{ Exception -> 0x07a8 }
                    r8 = org.telegram.messenger.AndroidUtilities.generateSearchName(r9, r11, r8);	 Catch:{ Exception -> 0x07a8 }
                    r6.add(r8);	 Catch:{ Exception -> 0x07a8 }
                    r4.add(r10);	 Catch:{ Exception -> 0x07a8 }
                    goto L_0x0794;	 Catch:{ Exception -> 0x07a8 }
                    r54 = r10;	 Catch:{ Exception -> 0x07a8 }
                    r56 = r11;	 Catch:{ Exception -> 0x07a8 }
                    r11 = 0;	 Catch:{ Exception -> 0x07a8 }
                    r9 = r9 + 1;	 Catch:{ Exception -> 0x07a8 }
                    r16 = r7;	 Catch:{ Exception -> 0x07a8 }
                    r3 = r51;	 Catch:{ Exception -> 0x07a8 }
                    r7 = r52;	 Catch:{ Exception -> 0x07a8 }
                    r8 = r53;	 Catch:{ Exception -> 0x07a8 }
                    r11 = r56;	 Catch:{ Exception -> 0x07a8 }
                    goto L_0x06b7;	 Catch:{ Exception -> 0x07a8 }
                    r11 = 0;	 Catch:{ Exception -> 0x07a8 }
                    r3 = r49;	 Catch:{ Exception -> 0x07a8 }
                    goto L_0x0678;	 Catch:{ Exception -> 0x07a8 }
                    r49 = r3;	 Catch:{ Exception -> 0x07a8 }
                    r5.dispose();	 Catch:{ Exception -> 0x07a8 }
                    goto L_0x07a0;	 Catch:{ Exception -> 0x07a8 }
                    r49 = r3;	 Catch:{ Exception -> 0x07a8 }
                    r3 = org.telegram.ui.Adapters.DialogsSearchAdapter.this;	 Catch:{ Exception -> 0x07a8 }
                    r7 = r4;	 Catch:{ Exception -> 0x07a8 }
                    r3.updateSearchResults(r4, r6, r2, r7);	 Catch:{ Exception -> 0x07a8 }
                    goto L_0x07ad;
                L_0x07a8:
                    r0 = move-exception;
                    r2 = r0;
                    org.telegram.messenger.FileLog.e(r2);
                    return;
                    */
                    throw new UnsupportedOperationException("Method not decompiled: org.telegram.ui.Adapters.DialogsSearchAdapter.6.run():void");
                }
            });
        }
    }

    private void updateSearchResults(ArrayList<TLObject> result, ArrayList<CharSequence> names, ArrayList<User> encUsers, int searchId) {
        final int i = searchId;
        final ArrayList<TLObject> arrayList = result;
        final ArrayList<User> arrayList2 = encUsers;
        final ArrayList<CharSequence> arrayList3 = names;
        AndroidUtilities.runOnUIThread(new Runnable() {
            public void run() {
                if (i == DialogsSearchAdapter.this.lastSearchId) {
                    for (int a = 0; a < arrayList.size(); a++) {
                        TLObject obj = (TLObject) arrayList.get(a);
                        if (obj instanceof User) {
                            MessagesController.getInstance(DialogsSearchAdapter.this.currentAccount).putUser((User) obj, true);
                        } else if (obj instanceof Chat) {
                            MessagesController.getInstance(DialogsSearchAdapter.this.currentAccount).putChat((Chat) obj, true);
                        } else if (obj instanceof EncryptedChat) {
                            MessagesController.getInstance(DialogsSearchAdapter.this.currentAccount).putEncryptedChat((EncryptedChat) obj, true);
                        }
                    }
                    MessagesController.getInstance(DialogsSearchAdapter.this.currentAccount).putUsers(arrayList2, true);
                    DialogsSearchAdapter.this.searchResult = arrayList;
                    DialogsSearchAdapter.this.searchResultNames = arrayList3;
                    DialogsSearchAdapter.this.searchAdapterHelper.mergeResults(DialogsSearchAdapter.this.searchResult);
                    DialogsSearchAdapter.this.notifyDataSetChanged();
                }
            }
        });
    }

    public void clearRecentHashtags() {
        this.searchAdapterHelper.clearRecentHashtags();
        this.searchResultHashtags.clear();
        notifyDataSetChanged();
    }

    public void searchDialogs(final String query) {
        if (query == null || this.lastSearchText == null || !query.equals(this.lastSearchText)) {
            this.lastSearchText = query;
            try {
                if (this.searchTimer != null) {
                    this.searchTimer.cancel();
                    this.searchTimer = null;
                }
            } catch (Throwable e) {
                FileLog.e(e);
            }
            if (query != null) {
                if (query.length() != 0) {
                    if (this.needMessagesSearch != 2 && query.startsWith("#") && query.length() == 1) {
                        this.messagesSearchEndReached = true;
                        if (this.searchAdapterHelper.loadRecentHashtags()) {
                            this.searchResultMessages.clear();
                            this.searchResultHashtags.clear();
                            ArrayList<HashtagObject> hashtags = this.searchAdapterHelper.getHashtags();
                            for (int a = 0; a < hashtags.size(); a++) {
                                this.searchResultHashtags.add(((HashtagObject) hashtags.get(a)).hashtag);
                            }
                            if (this.delegate != null) {
                                this.delegate.searchStateChanged(false);
                            }
                        } else if (this.delegate != null) {
                            this.delegate.searchStateChanged(true);
                        }
                        notifyDataSetChanged();
                    } else {
                        this.searchResultHashtags.clear();
                        notifyDataSetChanged();
                    }
                    final int searchId = this.lastSearchId + 1;
                    this.lastSearchId = searchId;
                    this.searchTimer = new Timer();
                    this.searchTimer.schedule(new TimerTask() {
                        public void run() {
                            try {
                                cancel();
                                DialogsSearchAdapter.this.searchTimer.cancel();
                                DialogsSearchAdapter.this.searchTimer = null;
                            } catch (Throwable e) {
                                FileLog.e(e);
                            }
                            DialogsSearchAdapter.this.searchDialogsInternal(query, searchId);
                            AndroidUtilities.runOnUIThread(new Runnable() {
                                public void run() {
                                    if (DialogsSearchAdapter.this.needMessagesSearch != 2) {
                                        DialogsSearchAdapter.this.searchAdapterHelper.queryServerSearch(query, true, true, true, true, 0, false);
                                    }
                                    DialogsSearchAdapter.this.searchMessagesInternal(query);
                                }
                            });
                        }
                    }, 200, 300);
                }
            }
            this.searchAdapterHelper.unloadRecentHashtags();
            this.searchResult.clear();
            this.searchResultNames.clear();
            this.searchResultHashtags.clear();
            this.searchAdapterHelper.mergeResults(null);
            if (this.needMessagesSearch != 2) {
                this.searchAdapterHelper.queryServerSearch(null, true, true, true, true, 0, false);
            }
            searchMessagesInternal(null);
            notifyDataSetChanged();
        }
    }

    public int getItemCount() {
        int i;
        int size;
        if (isRecentSearchDisplayed()) {
            i = 0;
            size = !this.recentSearchObjects.isEmpty() ? this.recentSearchObjects.size() + 1 : 0;
            if (!DataQuery.getInstance(this.currentAccount).hints.isEmpty()) {
                i = 2;
            }
            return size + i;
        } else if (!this.searchResultHashtags.isEmpty()) {
            return this.searchResultHashtags.size() + 1;
        } else {
            size = this.searchResult.size();
            i = this.searchAdapterHelper.getLocalServerSearch().size();
            int globalCount = this.searchAdapterHelper.getGlobalSearch().size();
            int messagesCount = this.searchResultMessages.size();
            size += i;
            if (globalCount != 0) {
                size += globalCount + 1;
            }
            if (messagesCount != 0) {
                size += (messagesCount + 1) + (this.messagesSearchEndReached ^ 1);
            }
            return size;
        }
    }

    public Object getItem(int i) {
        int messagesCount = 0;
        if (isRecentSearchDisplayed()) {
            if (!DataQuery.getInstance(this.currentAccount).hints.isEmpty()) {
                messagesCount = 2;
            }
            int offset = messagesCount;
            if (i <= offset || (i - 1) - offset >= this.recentSearchObjects.size()) {
                return null;
            }
            TLObject object = ((RecentSearchObject) this.recentSearchObjects.get((i - 1) - offset)).object;
            TLObject user;
            if (object instanceof User) {
                user = MessagesController.getInstance(this.currentAccount).getUser(Integer.valueOf(((User) object).id));
                if (user != null) {
                    object = user;
                }
            } else if (object instanceof Chat) {
                user = MessagesController.getInstance(this.currentAccount).getChat(Integer.valueOf(((Chat) object).id));
                if (user != null) {
                    object = user;
                }
            }
            return object;
        } else if (this.searchResultHashtags.isEmpty()) {
            ArrayList<TLObject> globalSearch = this.searchAdapterHelper.getGlobalSearch();
            ArrayList<TLObject> localServerSearch = this.searchAdapterHelper.getLocalServerSearch();
            int localCount = this.searchResult.size();
            int localServerCount = localServerSearch.size();
            int globalCount = globalSearch.isEmpty() ? 0 : globalSearch.size() + 1;
            if (!this.searchResultMessages.isEmpty()) {
                messagesCount = this.searchResultMessages.size() + 1;
            }
            if (i >= 0 && i < localCount) {
                return this.searchResult.get(i);
            }
            if (i >= localCount && i < localServerCount + localCount) {
                return localServerSearch.get(i - localCount);
            }
            if (i > localCount + localServerCount && i < (globalCount + localCount) + localServerCount) {
                return globalSearch.get(((i - localCount) - localServerCount) - 1);
            }
            if (i <= (globalCount + localCount) + localServerCount || i >= ((globalCount + localCount) + messagesCount) + localServerCount) {
                return null;
            }
            return this.searchResultMessages.get((((i - localCount) - globalCount) - localServerCount) - 1);
        } else if (i > 0) {
            return this.searchResultHashtags.get(i - 1);
        } else {
            return null;
        }
    }

    public long getItemId(int i) {
        return (long) i;
    }

    public boolean isEnabled(ViewHolder holder) {
        int type = holder.getItemViewType();
        return (type == 1 || type == 3) ? false : true;
    }

    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;
        switch (viewType) {
            case 0:
                view = new ProfileSearchCell(this.mContext);
                break;
            case 1:
                view = new GraySectionCell(this.mContext);
                break;
            case 2:
                view = new DialogCell(this.mContext, false);
                break;
            case 3:
                view = new LoadingCell(this.mContext);
                break;
            case 4:
                view = new HashtagSearchCell(this.mContext);
                break;
            case 5:
                View horizontalListView = new RecyclerListView(this.mContext) {
                    public boolean onInterceptTouchEvent(MotionEvent e) {
                        if (!(getParent() == null || getParent().getParent() == null)) {
                            getParent().getParent().requestDisallowInterceptTouchEvent(true);
                        }
                        return super.onInterceptTouchEvent(e);
                    }
                };
                horizontalListView.setTag(Integer.valueOf(9));
                horizontalListView.setItemAnimator(null);
                horizontalListView.setLayoutAnimation(null);
                LinearLayoutManager layoutManager = new LinearLayoutManager(this.mContext) {
                    public boolean supportsPredictiveItemAnimations() {
                        return false;
                    }
                };
                layoutManager.setOrientation(0);
                horizontalListView.setLayoutManager(layoutManager);
                horizontalListView.setAdapter(new CategoryAdapterRecycler());
                horizontalListView.setOnItemClickListener(new OnItemClickListener() {
                    public void onItemClick(View view, int position) {
                        if (DialogsSearchAdapter.this.delegate != null) {
                            DialogsSearchAdapter.this.delegate.didPressedOnSubDialog((long) ((Integer) view.getTag()).intValue());
                        }
                    }
                });
                horizontalListView.setOnItemLongClickListener(new OnItemLongClickListener() {
                    public boolean onItemClick(View view, int position) {
                        if (DialogsSearchAdapter.this.delegate != null) {
                            DialogsSearchAdapter.this.delegate.needRemoveHint(((Integer) view.getTag()).intValue());
                        }
                        return true;
                    }
                });
                view = horizontalListView;
                this.innerListView = horizontalListView;
                break;
            default:
                break;
        }
        if (viewType == 5) {
            view.setLayoutParams(new LayoutParams(-1, AndroidUtilities.dp(100.0f)));
        } else {
            view.setLayoutParams(new LayoutParams(-1, -2));
        }
        return new Holder(view);
    }

    public int getItemViewType(int i) {
        int i2 = 2;
        int i3 = 1;
        if (isRecentSearchDisplayed()) {
            if (DataQuery.getInstance(this.currentAccount).hints.isEmpty()) {
                i2 = 0;
            }
            int offset = i2;
            if (i > offset) {
                return 0;
            }
            if (i != offset) {
                if (i % 2 != 0) {
                    return 5;
                }
            }
            return 1;
        } else if (this.searchResultHashtags.isEmpty()) {
            ArrayList<TLObject> globalSearch = this.searchAdapterHelper.getGlobalSearch();
            int localCount = this.searchResult.size();
            int localServerCount = this.searchAdapterHelper.getLocalServerSearch().size();
            int globalCount = globalSearch.isEmpty() ? 0 : globalSearch.size() + 1;
            int messagesCount = this.searchResultMessages.isEmpty() ? 0 : this.searchResultMessages.size() + 1;
            if ((i >= 0 && i < localCount + localServerCount) || (i > localCount + localServerCount && i < (globalCount + localCount) + localServerCount)) {
                return 0;
            }
            if (i > (globalCount + localCount) + localServerCount && i < ((globalCount + localCount) + messagesCount) + localServerCount) {
                return 2;
            }
            if (messagesCount == 0 || i != ((globalCount + localCount) + messagesCount) + localServerCount) {
                return 1;
            }
            return 3;
        } else {
            if (i != 0) {
                i3 = 4;
            }
            return i3;
        }
    }
}
