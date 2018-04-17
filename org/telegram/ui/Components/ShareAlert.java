package org.telegram.ui.Components;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffColorFilter;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build.VERSION;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.LongSparseArray;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.ApplicationLoader;
import org.telegram.messenger.ChatObject;
import org.telegram.messenger.ContactsController;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MessageObject;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.MessagesStorage;
import org.telegram.messenger.NotificationCenter;
import org.telegram.messenger.NotificationCenter.NotificationCenterDelegate;
import org.telegram.messenger.SendMessagesHelper;
import org.telegram.messenger.UserConfig;
import org.telegram.messenger.beta.R;
import org.telegram.messenger.exoplayer2.C;
import org.telegram.messenger.support.widget.GridLayoutManager;
import org.telegram.messenger.support.widget.RecyclerView;
import org.telegram.messenger.support.widget.RecyclerView.Adapter;
import org.telegram.messenger.support.widget.RecyclerView.ItemDecoration;
import org.telegram.messenger.support.widget.RecyclerView.LayoutManager;
import org.telegram.messenger.support.widget.RecyclerView.LayoutParams;
import org.telegram.messenger.support.widget.RecyclerView.OnScrollListener;
import org.telegram.messenger.support.widget.RecyclerView.State;
import org.telegram.messenger.support.widget.RecyclerView.ViewHolder;
import org.telegram.tgnet.ConnectionsManager;
import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC.Chat;
import org.telegram.tgnet.TLRPC.TL_channels_exportMessageLink;
import org.telegram.tgnet.TLRPC.TL_dialog;
import org.telegram.tgnet.TLRPC.TL_error;
import org.telegram.tgnet.TLRPC.TL_exportedMessageLink;
import org.telegram.tgnet.TLRPC.User;
import org.telegram.ui.ActionBar.BottomSheet;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Cells.ShareDialogCell;
import org.telegram.ui.Components.RecyclerListView.Holder;
import org.telegram.ui.Components.RecyclerListView.OnItemClickListener;
import org.telegram.ui.Components.RecyclerListView.SelectionAdapter;
import org.telegram.ui.DialogsActivity;

public class ShareAlert extends BottomSheet implements NotificationCenterDelegate {
    private AnimatorSet animatorSet;
    private EditTextBoldCursor commentTextView;
    private boolean copyLinkOnEnd;
    private int currentAccount = UserConfig.selectedAccount;
    private LinearLayout doneButton;
    private TextView doneButtonBadgeTextView;
    private TextView doneButtonTextView;
    private TL_exportedMessageLink exportedMessageLink;
    private FrameLayout frameLayout;
    private FrameLayout frameLayout2;
    private RecyclerListView gridView;
    private boolean isPublicChannel;
    private GridLayoutManager layoutManager;
    private String linkToCopy;
    private ShareDialogsAdapter listAdapter;
    private boolean loadingLink;
    private EditTextBoldCursor nameTextView;
    private int scrollOffsetY;
    private ShareSearchAdapter searchAdapter;
    private EmptyTextProgressView searchEmptyView;
    private LongSparseArray<TL_dialog> selectedDialogs = new LongSparseArray();
    private ArrayList<MessageObject> sendingMessageObjects;
    private String sendingText;
    private View shadow;
    private View shadow2;
    private Drawable shadowDrawable;
    private int topBeforeSwitch;

    class AnonymousClass10 extends AnimatorListenerAdapter {
        final /* synthetic */ boolean val$show;

        AnonymousClass10(boolean z) {
            this.val$show = z;
        }

        public void onAnimationEnd(Animator animation) {
            if (animation.equals(ShareAlert.this.animatorSet)) {
                ShareAlert.this.gridView.setPadding(0, 0, 0, AndroidUtilities.dp(this.val$show ? 56.0f : 8.0f));
                ShareAlert.this.animatorSet = null;
            }
        }

        public void onAnimationCancel(Animator animation) {
            if (animation.equals(ShareAlert.this.animatorSet)) {
                ShareAlert.this.animatorSet = null;
            }
        }
    }

    private class ShareDialogsAdapter extends SelectionAdapter {
        private Context context;
        private int currentCount;
        private ArrayList<TL_dialog> dialogs = new ArrayList();

        public ShareDialogsAdapter(Context context) {
            this.context = context;
            fetchDialogs();
        }

        public void fetchDialogs() {
            this.dialogs.clear();
            for (int a = 0; a < MessagesController.getInstance(ShareAlert.this.currentAccount).dialogsForward.size(); a++) {
                TL_dialog dialog = (TL_dialog) MessagesController.getInstance(ShareAlert.this.currentAccount).dialogsForward.get(a);
                int lower_id = (int) dialog.id;
                int high_id = (int) (dialog.id >> 32);
                if (!(lower_id == 0 || high_id == 1)) {
                    if (lower_id > 0) {
                        this.dialogs.add(dialog);
                    } else {
                        Chat chat = MessagesController.getInstance(ShareAlert.this.currentAccount).getChat(Integer.valueOf(-lower_id));
                        if (!(chat == null || ChatObject.isNotInChat(chat) || (ChatObject.isChannel(chat) && !chat.creator && ((chat.admin_rights == null || !chat.admin_rights.post_messages) && !chat.megagroup)))) {
                            this.dialogs.add(dialog);
                        }
                    }
                }
            }
            notifyDataSetChanged();
        }

        public int getItemCount() {
            return this.dialogs.size();
        }

        public TL_dialog getItem(int i) {
            if (i >= 0) {
                if (i < this.dialogs.size()) {
                    return (TL_dialog) this.dialogs.get(i);
                }
            }
            return null;
        }

        public boolean isEnabled(ViewHolder holder) {
            return true;
        }

        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = new ShareDialogCell(this.context);
            view.setLayoutParams(new LayoutParams(-1, AndroidUtilities.dp(100.0f)));
            return new Holder(view);
        }

        public void onBindViewHolder(ViewHolder holder, int position) {
            ShareDialogCell cell = holder.itemView;
            TL_dialog dialog = getItem(position);
            cell.setDialog((int) dialog.id, ShareAlert.this.selectedDialogs.indexOfKey(dialog.id) >= 0, null);
        }

        public int getItemViewType(int i) {
            return 0;
        }
    }

    public class ShareSearchAdapter extends SelectionAdapter {
        private Context context;
        private int lastReqId;
        private int lastSearchId = 0;
        private String lastSearchText;
        private int reqId = 0;
        private ArrayList<DialogSearchResult> searchResult = new ArrayList();
        private Timer searchTimer;

        private class DialogSearchResult {
            public int date;
            public TL_dialog dialog;
            public CharSequence name;
            public TLObject object;

            private DialogSearchResult() {
                this.dialog = new TL_dialog();
            }
        }

        public ShareSearchAdapter(Context context) {
            this.context = context;
        }

        private void searchDialogsInternal(final String query, final int searchId) {
            MessagesStorage.getInstance(ShareAlert.this.currentAccount).getStorageQueue().postRunnable(new Runnable() {
                public void run() {
                    /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.DecodeException: Load method exception in method: org.telegram.ui.Components.ShareAlert.ShareSearchAdapter.1.run():void
	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:116)
	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:249)
	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:256)
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
                    r1 = r38;
                    r2 = r3;	 Catch:{ Exception -> 0x04c0 }
                    r2 = r2.trim();	 Catch:{ Exception -> 0x04c0 }
                    r2 = r2.toLowerCase();	 Catch:{ Exception -> 0x04c0 }
                    r3 = r2.length();	 Catch:{ Exception -> 0x04c0 }
                    r4 = -1;	 Catch:{ Exception -> 0x04c0 }
                    if (r3 != 0) goto L_0x0029;	 Catch:{ Exception -> 0x04c0 }
                L_0x0013:
                    r3 = org.telegram.ui.Components.ShareAlert.ShareSearchAdapter.this;	 Catch:{ Exception -> 0x04c0 }
                    r3.lastSearchId = r4;	 Catch:{ Exception -> 0x04c0 }
                    r3 = org.telegram.ui.Components.ShareAlert.ShareSearchAdapter.this;	 Catch:{ Exception -> 0x04c0 }
                    r4 = new java.util.ArrayList;	 Catch:{ Exception -> 0x04c0 }
                    r4.<init>();	 Catch:{ Exception -> 0x04c0 }
                    r5 = org.telegram.ui.Components.ShareAlert.ShareSearchAdapter.this;	 Catch:{ Exception -> 0x04c0 }
                    r5 = r5.lastSearchId;	 Catch:{ Exception -> 0x04c0 }
                    r3.updateSearchResults(r4, r5);	 Catch:{ Exception -> 0x04c0 }
                    return;	 Catch:{ Exception -> 0x04c0 }
                L_0x0029:
                    r3 = org.telegram.messenger.LocaleController.getInstance();	 Catch:{ Exception -> 0x04c0 }
                    r3 = r3.getTranslitString(r2);	 Catch:{ Exception -> 0x04c0 }
                    r5 = r2.equals(r3);	 Catch:{ Exception -> 0x04c0 }
                    if (r5 != 0) goto L_0x003d;	 Catch:{ Exception -> 0x04c0 }
                L_0x0037:
                    r5 = r3.length();	 Catch:{ Exception -> 0x04c0 }
                    if (r5 != 0) goto L_0x003e;	 Catch:{ Exception -> 0x04c0 }
                L_0x003d:
                    r3 = 0;	 Catch:{ Exception -> 0x04c0 }
                L_0x003e:
                    r5 = 1;	 Catch:{ Exception -> 0x04c0 }
                    r6 = 0;	 Catch:{ Exception -> 0x04c0 }
                    if (r3 == 0) goto L_0x0044;	 Catch:{ Exception -> 0x04c0 }
                L_0x0042:
                    r7 = r5;	 Catch:{ Exception -> 0x04c0 }
                    goto L_0x0045;	 Catch:{ Exception -> 0x04c0 }
                L_0x0044:
                    r7 = r6;	 Catch:{ Exception -> 0x04c0 }
                L_0x0045:
                    r7 = r7 + r5;	 Catch:{ Exception -> 0x04c0 }
                    r7 = new java.lang.String[r7];	 Catch:{ Exception -> 0x04c0 }
                    r7[r6] = r2;	 Catch:{ Exception -> 0x04c0 }
                    if (r3 == 0) goto L_0x004e;	 Catch:{ Exception -> 0x04c0 }
                L_0x004c:
                    r7[r5] = r3;	 Catch:{ Exception -> 0x04c0 }
                L_0x004e:
                    r8 = new java.util.ArrayList;	 Catch:{ Exception -> 0x04c0 }
                    r8.<init>();	 Catch:{ Exception -> 0x04c0 }
                    r9 = new java.util.ArrayList;	 Catch:{ Exception -> 0x04c0 }
                    r9.<init>();	 Catch:{ Exception -> 0x04c0 }
                    r10 = 0;	 Catch:{ Exception -> 0x04c0 }
                    r11 = new android.util.LongSparseArray;	 Catch:{ Exception -> 0x04c0 }
                    r11.<init>();	 Catch:{ Exception -> 0x04c0 }
                    r12 = org.telegram.ui.Components.ShareAlert.ShareSearchAdapter.this;	 Catch:{ Exception -> 0x04c0 }
                    r12 = org.telegram.ui.Components.ShareAlert.this;	 Catch:{ Exception -> 0x04c0 }
                    r12 = r12.currentAccount;	 Catch:{ Exception -> 0x04c0 }
                    r12 = org.telegram.messenger.MessagesStorage.getInstance(r12);	 Catch:{ Exception -> 0x04c0 }
                    r12 = r12.getDatabase();	 Catch:{ Exception -> 0x04c0 }
                    r13 = "SELECT did, date FROM dialogs ORDER BY date DESC LIMIT 400";	 Catch:{ Exception -> 0x04c0 }
                    r14 = new java.lang.Object[r6];	 Catch:{ Exception -> 0x04c0 }
                    r12 = r12.queryFinalized(r13, r14);	 Catch:{ Exception -> 0x04c0 }
                    r13 = r12.next();	 Catch:{ Exception -> 0x04c0 }
                    r14 = 0;	 Catch:{ Exception -> 0x04c0 }
                    if (r13 == 0) goto L_0x00ce;	 Catch:{ Exception -> 0x04c0 }
                L_0x007d:
                    r15 = r12.longValue(r6);	 Catch:{ Exception -> 0x04c0 }
                    r17 = r15;	 Catch:{ Exception -> 0x04c0 }
                    r13 = new org.telegram.ui.Components.ShareAlert$ShareSearchAdapter$DialogSearchResult;	 Catch:{ Exception -> 0x04c0 }
                    r15 = org.telegram.ui.Components.ShareAlert.ShareSearchAdapter.this;	 Catch:{ Exception -> 0x04c0 }
                    r13.<init>();	 Catch:{ Exception -> 0x04c0 }
                    r14 = r12.intValue(r5);	 Catch:{ Exception -> 0x04c0 }
                    r13.date = r14;	 Catch:{ Exception -> 0x04c0 }
                    r14 = r17;	 Catch:{ Exception -> 0x04c0 }
                    r11.put(r14, r13);	 Catch:{ Exception -> 0x04c0 }
                    r4 = (int) r14;	 Catch:{ Exception -> 0x04c0 }
                    r16 = 32;	 Catch:{ Exception -> 0x04c0 }
                    r19 = r7;	 Catch:{ Exception -> 0x04c0 }
                    r6 = r14 >> r16;	 Catch:{ Exception -> 0x04c0 }
                    r6 = (int) r6;	 Catch:{ Exception -> 0x04c0 }
                    if (r4 == 0) goto L_0x00c8;	 Catch:{ Exception -> 0x04c0 }
                L_0x009f:
                    if (r6 == r5) goto L_0x00c8;	 Catch:{ Exception -> 0x04c0 }
                    if (r4 <= 0) goto L_0x00b5;	 Catch:{ Exception -> 0x04c0 }
                    r7 = java.lang.Integer.valueOf(r4);	 Catch:{ Exception -> 0x04c0 }
                    r7 = r8.contains(r7);	 Catch:{ Exception -> 0x04c0 }
                    if (r7 != 0) goto L_0x00c8;	 Catch:{ Exception -> 0x04c0 }
                    r7 = java.lang.Integer.valueOf(r4);	 Catch:{ Exception -> 0x04c0 }
                    r8.add(r7);	 Catch:{ Exception -> 0x04c0 }
                    goto L_0x00c8;	 Catch:{ Exception -> 0x04c0 }
                    r7 = -r4;	 Catch:{ Exception -> 0x04c0 }
                    r7 = java.lang.Integer.valueOf(r7);	 Catch:{ Exception -> 0x04c0 }
                    r7 = r9.contains(r7);	 Catch:{ Exception -> 0x04c0 }
                    if (r7 != 0) goto L_0x00c8;	 Catch:{ Exception -> 0x04c0 }
                    r7 = -r4;	 Catch:{ Exception -> 0x04c0 }
                    r7 = java.lang.Integer.valueOf(r7);	 Catch:{ Exception -> 0x04c0 }
                    r9.add(r7);	 Catch:{ Exception -> 0x04c0 }
                    r7 = r19;	 Catch:{ Exception -> 0x04c0 }
                    r4 = -1;	 Catch:{ Exception -> 0x04c0 }
                    r6 = 0;	 Catch:{ Exception -> 0x04c0 }
                    goto L_0x0076;	 Catch:{ Exception -> 0x04c0 }
                L_0x00ce:
                    r19 = r7;	 Catch:{ Exception -> 0x04c0 }
                    r12.dispose();	 Catch:{ Exception -> 0x04c0 }
                    r4 = r8.isEmpty();	 Catch:{ Exception -> 0x04c0 }
                    r6 = 2;	 Catch:{ Exception -> 0x04c0 }
                    if (r4 != 0) goto L_0x0242;	 Catch:{ Exception -> 0x04c0 }
                    r4 = org.telegram.ui.Components.ShareAlert.ShareSearchAdapter.this;	 Catch:{ Exception -> 0x04c0 }
                    r4 = org.telegram.ui.Components.ShareAlert.this;	 Catch:{ Exception -> 0x04c0 }
                    r4 = r4.currentAccount;	 Catch:{ Exception -> 0x04c0 }
                    r4 = org.telegram.messenger.MessagesStorage.getInstance(r4);	 Catch:{ Exception -> 0x04c0 }
                    r4 = r4.getDatabase();	 Catch:{ Exception -> 0x04c0 }
                    r7 = java.util.Locale.US;	 Catch:{ Exception -> 0x04c0 }
                    r13 = "SELECT data, status, name FROM users WHERE uid IN(%s)";	 Catch:{ Exception -> 0x04c0 }
                    r15 = new java.lang.Object[r5];	 Catch:{ Exception -> 0x04c0 }
                    r14 = ",";	 Catch:{ Exception -> 0x04c0 }
                    r14 = android.text.TextUtils.join(r14, r8);	 Catch:{ Exception -> 0x04c0 }
                    r5 = 0;	 Catch:{ Exception -> 0x04c0 }
                    r15[r5] = r14;	 Catch:{ Exception -> 0x04c0 }
                    r7 = java.lang.String.format(r7, r13, r15);	 Catch:{ Exception -> 0x04c0 }
                    r13 = new java.lang.Object[r5];	 Catch:{ Exception -> 0x04c0 }
                    r4 = r4.queryFinalized(r7, r13);	 Catch:{ Exception -> 0x04c0 }
                    r12 = r4;	 Catch:{ Exception -> 0x04c0 }
                    r4 = r12.next();	 Catch:{ Exception -> 0x04c0 }
                    if (r4 == 0) goto L_0x0236;	 Catch:{ Exception -> 0x04c0 }
                    r4 = r12.stringValue(r6);	 Catch:{ Exception -> 0x04c0 }
                    r5 = org.telegram.messenger.LocaleController.getInstance();	 Catch:{ Exception -> 0x04c0 }
                    r5 = r5.getTranslitString(r4);	 Catch:{ Exception -> 0x04c0 }
                    r7 = r4.equals(r5);	 Catch:{ Exception -> 0x04c0 }
                    if (r7 == 0) goto L_0x011d;	 Catch:{ Exception -> 0x04c0 }
                    r5 = 0;	 Catch:{ Exception -> 0x04c0 }
                    r7 = 0;	 Catch:{ Exception -> 0x04c0 }
                    r13 = ";;;";	 Catch:{ Exception -> 0x04c0 }
                    r13 = r4.lastIndexOf(r13);	 Catch:{ Exception -> 0x04c0 }
                    r14 = -1;	 Catch:{ Exception -> 0x04c0 }
                    if (r13 == r14) goto L_0x012e;	 Catch:{ Exception -> 0x04c0 }
                    r14 = r13 + 3;	 Catch:{ Exception -> 0x04c0 }
                    r14 = r4.substring(r14);	 Catch:{ Exception -> 0x04c0 }
                    r7 = r14;	 Catch:{ Exception -> 0x04c0 }
                    r14 = 0;	 Catch:{ Exception -> 0x04c0 }
                    r15 = r19;	 Catch:{ Exception -> 0x04c0 }
                    r6 = r15.length;	 Catch:{ Exception -> 0x04c0 }
                    r16 = r14;	 Catch:{ Exception -> 0x04c0 }
                    r14 = 0;	 Catch:{ Exception -> 0x04c0 }
                    if (r14 >= r6) goto L_0x0225;	 Catch:{ Exception -> 0x04c0 }
                    r17 = r15[r14];	 Catch:{ Exception -> 0x04c0 }
                    r20 = r17;	 Catch:{ Exception -> 0x04c0 }
                    r21 = r2;	 Catch:{ Exception -> 0x04c0 }
                    r2 = r20;	 Catch:{ Exception -> 0x04c0 }
                    r17 = r4.startsWith(r2);	 Catch:{ Exception -> 0x04c0 }
                    if (r17 != 0) goto L_0x018d;	 Catch:{ Exception -> 0x04c0 }
                    r22 = r3;	 Catch:{ Exception -> 0x04c0 }
                    r3 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x04c0 }
                    r3.<init>();	 Catch:{ Exception -> 0x04c0 }
                    r23 = r6;	 Catch:{ Exception -> 0x04c0 }
                    r6 = " ";	 Catch:{ Exception -> 0x04c0 }
                    r3.append(r6);	 Catch:{ Exception -> 0x04c0 }
                    r3.append(r2);	 Catch:{ Exception -> 0x04c0 }
                    r3 = r3.toString();	 Catch:{ Exception -> 0x04c0 }
                    r3 = r4.contains(r3);	 Catch:{ Exception -> 0x04c0 }
                    if (r3 != 0) goto L_0x0191;	 Catch:{ Exception -> 0x04c0 }
                    if (r5 == 0) goto L_0x0180;	 Catch:{ Exception -> 0x04c0 }
                    r3 = r5.startsWith(r2);	 Catch:{ Exception -> 0x04c0 }
                    if (r3 != 0) goto L_0x0191;	 Catch:{ Exception -> 0x04c0 }
                    r3 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x04c0 }
                    r3.<init>();	 Catch:{ Exception -> 0x04c0 }
                    r6 = " ";	 Catch:{ Exception -> 0x04c0 }
                    r3.append(r6);	 Catch:{ Exception -> 0x04c0 }
                    r3.append(r2);	 Catch:{ Exception -> 0x04c0 }
                    r3 = r3.toString();	 Catch:{ Exception -> 0x04c0 }
                    r3 = r5.contains(r3);	 Catch:{ Exception -> 0x04c0 }
                    if (r3 == 0) goto L_0x0180;	 Catch:{ Exception -> 0x04c0 }
                    goto L_0x0191;	 Catch:{ Exception -> 0x04c0 }
                    if (r7 == 0) goto L_0x018a;	 Catch:{ Exception -> 0x04c0 }
                    r3 = r7.startsWith(r2);	 Catch:{ Exception -> 0x04c0 }
                    if (r3 == 0) goto L_0x018a;	 Catch:{ Exception -> 0x04c0 }
                    r3 = 2;	 Catch:{ Exception -> 0x04c0 }
                    goto L_0x0192;	 Catch:{ Exception -> 0x04c0 }
                    r3 = r16;	 Catch:{ Exception -> 0x04c0 }
                    goto L_0x0192;	 Catch:{ Exception -> 0x04c0 }
                    r22 = r3;	 Catch:{ Exception -> 0x04c0 }
                    r23 = r6;	 Catch:{ Exception -> 0x04c0 }
                    r3 = 1;	 Catch:{ Exception -> 0x04c0 }
                    if (r3 == 0) goto L_0x0211;	 Catch:{ Exception -> 0x04c0 }
                    r6 = 0;	 Catch:{ Exception -> 0x04c0 }
                    r14 = r12.byteBufferValue(r6);	 Catch:{ Exception -> 0x04c0 }
                    if (r14 == 0) goto L_0x020e;	 Catch:{ Exception -> 0x04c0 }
                    r24 = r4;	 Catch:{ Exception -> 0x04c0 }
                    r4 = r14.readInt32(r6);	 Catch:{ Exception -> 0x04c0 }
                    r4 = org.telegram.tgnet.TLRPC.User.TLdeserialize(r14, r4, r6);	 Catch:{ Exception -> 0x04c0 }
                    r14.reuse();	 Catch:{ Exception -> 0x04c0 }
                    r6 = r4.id;	 Catch:{ Exception -> 0x04c0 }
                    r25 = r5;	 Catch:{ Exception -> 0x04c0 }
                    r5 = (long) r6;	 Catch:{ Exception -> 0x04c0 }
                    r5 = r11.get(r5);	 Catch:{ Exception -> 0x04c0 }
                    r5 = (org.telegram.ui.Components.ShareAlert.ShareSearchAdapter.DialogSearchResult) r5;	 Catch:{ Exception -> 0x04c0 }
                    r6 = r4.status;	 Catch:{ Exception -> 0x04c0 }
                    if (r6 == 0) goto L_0x01c5;	 Catch:{ Exception -> 0x04c0 }
                    r6 = r4.status;	 Catch:{ Exception -> 0x04c0 }
                    r26 = r7;	 Catch:{ Exception -> 0x04c0 }
                    r27 = r8;	 Catch:{ Exception -> 0x04c0 }
                    r7 = 1;	 Catch:{ Exception -> 0x04c0 }
                    r8 = r12.intValue(r7);	 Catch:{ Exception -> 0x04c0 }
                    r6.expires = r8;	 Catch:{ Exception -> 0x04c0 }
                    goto L_0x01c9;	 Catch:{ Exception -> 0x04c0 }
                    r26 = r7;	 Catch:{ Exception -> 0x04c0 }
                    r27 = r8;	 Catch:{ Exception -> 0x04c0 }
                    r6 = 1;	 Catch:{ Exception -> 0x04c0 }
                    if (r3 != r6) goto L_0x01d7;	 Catch:{ Exception -> 0x04c0 }
                    r6 = r4.first_name;	 Catch:{ Exception -> 0x04c0 }
                    r7 = r4.last_name;	 Catch:{ Exception -> 0x04c0 }
                    r6 = org.telegram.messenger.AndroidUtilities.generateSearchName(r6, r7, r2);	 Catch:{ Exception -> 0x04c0 }
                    r5.name = r6;	 Catch:{ Exception -> 0x04c0 }
                    goto L_0x0202;	 Catch:{ Exception -> 0x04c0 }
                    r6 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x04c0 }
                    r6.<init>();	 Catch:{ Exception -> 0x04c0 }
                    r7 = "@";	 Catch:{ Exception -> 0x04c0 }
                    r6.append(r7);	 Catch:{ Exception -> 0x04c0 }
                    r7 = r4.username;	 Catch:{ Exception -> 0x04c0 }
                    r6.append(r7);	 Catch:{ Exception -> 0x04c0 }
                    r6 = r6.toString();	 Catch:{ Exception -> 0x04c0 }
                    r7 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x04c0 }
                    r7.<init>();	 Catch:{ Exception -> 0x04c0 }
                    r8 = "@";	 Catch:{ Exception -> 0x04c0 }
                    r7.append(r8);	 Catch:{ Exception -> 0x04c0 }
                    r7.append(r2);	 Catch:{ Exception -> 0x04c0 }
                    r7 = r7.toString();	 Catch:{ Exception -> 0x04c0 }
                    r8 = 0;	 Catch:{ Exception -> 0x04c0 }
                    r6 = org.telegram.messenger.AndroidUtilities.generateSearchName(r6, r8, r7);	 Catch:{ Exception -> 0x04c0 }
                    r5.name = r6;	 Catch:{ Exception -> 0x04c0 }
                    r5.object = r4;	 Catch:{ Exception -> 0x04c0 }
                    r6 = r5.dialog;	 Catch:{ Exception -> 0x04c0 }
                    r7 = r4.id;	 Catch:{ Exception -> 0x04c0 }
                    r7 = (long) r7;	 Catch:{ Exception -> 0x04c0 }
                    r6.id = r7;	 Catch:{ Exception -> 0x04c0 }
                    r10 = r10 + 1;	 Catch:{ Exception -> 0x04c0 }
                    goto L_0x022b;	 Catch:{ Exception -> 0x04c0 }
                    r27 = r8;	 Catch:{ Exception -> 0x04c0 }
                    goto L_0x022b;	 Catch:{ Exception -> 0x04c0 }
                    r24 = r4;	 Catch:{ Exception -> 0x04c0 }
                    r25 = r5;	 Catch:{ Exception -> 0x04c0 }
                    r26 = r7;	 Catch:{ Exception -> 0x04c0 }
                    r27 = r8;	 Catch:{ Exception -> 0x04c0 }
                    r14 = r14 + 1;	 Catch:{ Exception -> 0x04c0 }
                    r16 = r3;	 Catch:{ Exception -> 0x04c0 }
                    r2 = r21;	 Catch:{ Exception -> 0x04c0 }
                    r3 = r22;	 Catch:{ Exception -> 0x04c0 }
                    r6 = r23;	 Catch:{ Exception -> 0x04c0 }
                    goto L_0x0135;	 Catch:{ Exception -> 0x04c0 }
                    r21 = r2;	 Catch:{ Exception -> 0x04c0 }
                    r22 = r3;	 Catch:{ Exception -> 0x04c0 }
                    r27 = r8;	 Catch:{ Exception -> 0x04c0 }
                    r19 = r15;	 Catch:{ Exception -> 0x04c0 }
                    r2 = r21;	 Catch:{ Exception -> 0x04c0 }
                    r3 = r22;	 Catch:{ Exception -> 0x04c0 }
                    r8 = r27;	 Catch:{ Exception -> 0x04c0 }
                    r6 = 2;	 Catch:{ Exception -> 0x04c0 }
                    goto L_0x0104;	 Catch:{ Exception -> 0x04c0 }
                    r21 = r2;	 Catch:{ Exception -> 0x04c0 }
                    r22 = r3;	 Catch:{ Exception -> 0x04c0 }
                    r27 = r8;	 Catch:{ Exception -> 0x04c0 }
                    r15 = r19;	 Catch:{ Exception -> 0x04c0 }
                    r12.dispose();	 Catch:{ Exception -> 0x04c0 }
                    goto L_0x024a;	 Catch:{ Exception -> 0x04c0 }
                    r21 = r2;	 Catch:{ Exception -> 0x04c0 }
                    r22 = r3;	 Catch:{ Exception -> 0x04c0 }
                    r27 = r8;	 Catch:{ Exception -> 0x04c0 }
                    r15 = r19;	 Catch:{ Exception -> 0x04c0 }
                    r2 = r9.isEmpty();	 Catch:{ Exception -> 0x04c0 }
                    if (r2 != 0) goto L_0x0342;	 Catch:{ Exception -> 0x04c0 }
                    r2 = org.telegram.ui.Components.ShareAlert.ShareSearchAdapter.this;	 Catch:{ Exception -> 0x04c0 }
                    r2 = org.telegram.ui.Components.ShareAlert.this;	 Catch:{ Exception -> 0x04c0 }
                    r2 = r2.currentAccount;	 Catch:{ Exception -> 0x04c0 }
                    r2 = org.telegram.messenger.MessagesStorage.getInstance(r2);	 Catch:{ Exception -> 0x04c0 }
                    r2 = r2.getDatabase();	 Catch:{ Exception -> 0x04c0 }
                    r3 = java.util.Locale.US;	 Catch:{ Exception -> 0x04c0 }
                    r4 = "SELECT data, name FROM chats WHERE uid IN(%s)";	 Catch:{ Exception -> 0x04c0 }
                    r5 = 1;	 Catch:{ Exception -> 0x04c0 }
                    r6 = new java.lang.Object[r5];	 Catch:{ Exception -> 0x04c0 }
                    r5 = ",";	 Catch:{ Exception -> 0x04c0 }
                    r5 = android.text.TextUtils.join(r5, r9);	 Catch:{ Exception -> 0x04c0 }
                    r7 = 0;	 Catch:{ Exception -> 0x04c0 }
                    r6[r7] = r5;	 Catch:{ Exception -> 0x04c0 }
                    r3 = java.lang.String.format(r3, r4, r6);	 Catch:{ Exception -> 0x04c0 }
                    r4 = new java.lang.Object[r7];	 Catch:{ Exception -> 0x04c0 }
                    r2 = r2.queryFinalized(r3, r4);	 Catch:{ Exception -> 0x04c0 }
                    r12 = r2;	 Catch:{ Exception -> 0x04c0 }
                    r2 = r12.next();	 Catch:{ Exception -> 0x04c0 }
                    if (r2 == 0) goto L_0x033f;	 Catch:{ Exception -> 0x04c0 }
                    r2 = 1;	 Catch:{ Exception -> 0x04c0 }
                    r3 = r12.stringValue(r2);	 Catch:{ Exception -> 0x04c0 }
                    r2 = r3;	 Catch:{ Exception -> 0x04c0 }
                    r3 = org.telegram.messenger.LocaleController.getInstance();	 Catch:{ Exception -> 0x04c0 }
                    r3 = r3.getTranslitString(r2);	 Catch:{ Exception -> 0x04c0 }
                    r4 = r2.equals(r3);	 Catch:{ Exception -> 0x04c0 }
                    if (r4 == 0) goto L_0x0296;	 Catch:{ Exception -> 0x04c0 }
                    r3 = 0;	 Catch:{ Exception -> 0x04c0 }
                    r4 = 0;	 Catch:{ Exception -> 0x04c0 }
                    r5 = r15.length;	 Catch:{ Exception -> 0x04c0 }
                    if (r4 >= r5) goto L_0x033d;	 Catch:{ Exception -> 0x04c0 }
                    r5 = r15[r4];	 Catch:{ Exception -> 0x04c0 }
                    r6 = r2.startsWith(r5);	 Catch:{ Exception -> 0x04c0 }
                    if (r6 != 0) goto L_0x02dc;	 Catch:{ Exception -> 0x04c0 }
                    r6 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x04c0 }
                    r6.<init>();	 Catch:{ Exception -> 0x04c0 }
                    r7 = " ";	 Catch:{ Exception -> 0x04c0 }
                    r6.append(r7);	 Catch:{ Exception -> 0x04c0 }
                    r6.append(r5);	 Catch:{ Exception -> 0x04c0 }
                    r6 = r6.toString();	 Catch:{ Exception -> 0x04c0 }
                    r6 = r2.contains(r6);	 Catch:{ Exception -> 0x04c0 }
                    if (r6 != 0) goto L_0x02dc;	 Catch:{ Exception -> 0x04c0 }
                    if (r3 == 0) goto L_0x02d9;	 Catch:{ Exception -> 0x04c0 }
                    r6 = r3.startsWith(r5);	 Catch:{ Exception -> 0x04c0 }
                    if (r6 != 0) goto L_0x02dc;	 Catch:{ Exception -> 0x04c0 }
                    r6 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x04c0 }
                    r6.<init>();	 Catch:{ Exception -> 0x04c0 }
                    r7 = " ";	 Catch:{ Exception -> 0x04c0 }
                    r6.append(r7);	 Catch:{ Exception -> 0x04c0 }
                    r6.append(r5);	 Catch:{ Exception -> 0x04c0 }
                    r6 = r6.toString();	 Catch:{ Exception -> 0x04c0 }
                    r6 = r3.contains(r6);	 Catch:{ Exception -> 0x04c0 }
                    if (r6 == 0) goto L_0x02d9;	 Catch:{ Exception -> 0x04c0 }
                    goto L_0x02dc;	 Catch:{ Exception -> 0x04c0 }
                    r4 = r4 + 1;	 Catch:{ Exception -> 0x04c0 }
                    goto L_0x0297;	 Catch:{ Exception -> 0x04c0 }
                    r6 = 0;	 Catch:{ Exception -> 0x04c0 }
                    r7 = r12.byteBufferValue(r6);	 Catch:{ Exception -> 0x04c0 }
                    if (r7 == 0) goto L_0x033d;	 Catch:{ Exception -> 0x04c0 }
                    r8 = r7.readInt32(r6);	 Catch:{ Exception -> 0x04c0 }
                    r8 = org.telegram.tgnet.TLRPC.Chat.TLdeserialize(r7, r8, r6);	 Catch:{ Exception -> 0x04c0 }
                    r6 = r8;	 Catch:{ Exception -> 0x04c0 }
                    r7.reuse();	 Catch:{ Exception -> 0x04c0 }
                    if (r6 == 0) goto L_0x0339;	 Catch:{ Exception -> 0x04c0 }
                    r8 = org.telegram.messenger.ChatObject.isNotInChat(r6);	 Catch:{ Exception -> 0x04c0 }
                    if (r8 != 0) goto L_0x0339;	 Catch:{ Exception -> 0x04c0 }
                    r8 = org.telegram.messenger.ChatObject.isChannel(r6);	 Catch:{ Exception -> 0x04c0 }
                    if (r8 == 0) goto L_0x0315;	 Catch:{ Exception -> 0x04c0 }
                    r8 = r6.creator;	 Catch:{ Exception -> 0x04c0 }
                    if (r8 != 0) goto L_0x0315;	 Catch:{ Exception -> 0x04c0 }
                    r8 = r6.admin_rights;	 Catch:{ Exception -> 0x04c0 }
                    if (r8 == 0) goto L_0x030b;	 Catch:{ Exception -> 0x04c0 }
                    r8 = r6.admin_rights;	 Catch:{ Exception -> 0x04c0 }
                    r8 = r8.post_messages;	 Catch:{ Exception -> 0x04c0 }
                    if (r8 != 0) goto L_0x0315;	 Catch:{ Exception -> 0x04c0 }
                    r8 = r6.megagroup;	 Catch:{ Exception -> 0x04c0 }
                    if (r8 == 0) goto L_0x0310;	 Catch:{ Exception -> 0x04c0 }
                    goto L_0x0315;	 Catch:{ Exception -> 0x04c0 }
                    r28 = r2;	 Catch:{ Exception -> 0x04c0 }
                    r29 = r3;	 Catch:{ Exception -> 0x04c0 }
                    goto L_0x033d;	 Catch:{ Exception -> 0x04c0 }
                    r8 = r6.id;	 Catch:{ Exception -> 0x04c0 }
                    r13 = (long) r8;	 Catch:{ Exception -> 0x04c0 }
                    r13 = -r13;	 Catch:{ Exception -> 0x04c0 }
                    r8 = r11.get(r13);	 Catch:{ Exception -> 0x04c0 }
                    r8 = (org.telegram.ui.Components.ShareAlert.ShareSearchAdapter.DialogSearchResult) r8;	 Catch:{ Exception -> 0x04c0 }
                    r13 = r6.title;	 Catch:{ Exception -> 0x04c0 }
                    r14 = 0;	 Catch:{ Exception -> 0x04c0 }
                    r13 = org.telegram.messenger.AndroidUtilities.generateSearchName(r13, r14, r5);	 Catch:{ Exception -> 0x04c0 }
                    r8.name = r13;	 Catch:{ Exception -> 0x04c0 }
                    r8.object = r6;	 Catch:{ Exception -> 0x04c0 }
                    r13 = r8.dialog;	 Catch:{ Exception -> 0x04c0 }
                    r14 = r6.id;	 Catch:{ Exception -> 0x04c0 }
                    r14 = -r14;	 Catch:{ Exception -> 0x04c0 }
                    r28 = r2;	 Catch:{ Exception -> 0x04c0 }
                    r29 = r3;	 Catch:{ Exception -> 0x04c0 }
                    r2 = (long) r14;	 Catch:{ Exception -> 0x04c0 }
                    r13.id = r2;	 Catch:{ Exception -> 0x04c0 }
                    r10 = r10 + 1;	 Catch:{ Exception -> 0x04c0 }
                    goto L_0x033d;	 Catch:{ Exception -> 0x04c0 }
                    r28 = r2;	 Catch:{ Exception -> 0x04c0 }
                    r29 = r3;	 Catch:{ Exception -> 0x04c0 }
                    goto L_0x027b;	 Catch:{ Exception -> 0x04c0 }
                    r12.dispose();	 Catch:{ Exception -> 0x04c0 }
                    r2 = new java.util.ArrayList;	 Catch:{ Exception -> 0x04c0 }
                    r2.<init>(r10);	 Catch:{ Exception -> 0x04c0 }
                    r3 = 0;	 Catch:{ Exception -> 0x04c0 }
                    r4 = r11.size();	 Catch:{ Exception -> 0x04c0 }
                    if (r3 >= r4) goto L_0x0362;	 Catch:{ Exception -> 0x04c0 }
                    r4 = r11.valueAt(r3);	 Catch:{ Exception -> 0x04c0 }
                    r4 = (org.telegram.ui.Components.ShareAlert.ShareSearchAdapter.DialogSearchResult) r4;	 Catch:{ Exception -> 0x04c0 }
                    r5 = r4.object;	 Catch:{ Exception -> 0x04c0 }
                    if (r5 == 0) goto L_0x035f;	 Catch:{ Exception -> 0x04c0 }
                    r5 = r4.name;	 Catch:{ Exception -> 0x04c0 }
                    if (r5 == 0) goto L_0x035f;	 Catch:{ Exception -> 0x04c0 }
                    r2.add(r4);	 Catch:{ Exception -> 0x04c0 }
                    r3 = r3 + 1;	 Catch:{ Exception -> 0x04c0 }
                    goto L_0x0348;	 Catch:{ Exception -> 0x04c0 }
                    r3 = org.telegram.ui.Components.ShareAlert.ShareSearchAdapter.this;	 Catch:{ Exception -> 0x04c0 }
                    r3 = org.telegram.ui.Components.ShareAlert.this;	 Catch:{ Exception -> 0x04c0 }
                    r3 = r3.currentAccount;	 Catch:{ Exception -> 0x04c0 }
                    r3 = org.telegram.messenger.MessagesStorage.getInstance(r3);	 Catch:{ Exception -> 0x04c0 }
                    r3 = r3.getDatabase();	 Catch:{ Exception -> 0x04c0 }
                    r4 = "SELECT u.data, u.status, u.name, u.uid FROM users as u INNER JOIN contacts as c ON u.uid = c.uid";	 Catch:{ Exception -> 0x04c0 }
                    r5 = 0;	 Catch:{ Exception -> 0x04c0 }
                    r6 = new java.lang.Object[r5];	 Catch:{ Exception -> 0x04c0 }
                    r3 = r3.queryFinalized(r4, r6);	 Catch:{ Exception -> 0x04c0 }
                    r4 = r3.next();	 Catch:{ Exception -> 0x04c0 }
                    if (r4 == 0) goto L_0x04ab;	 Catch:{ Exception -> 0x04c0 }
                    r4 = 3;	 Catch:{ Exception -> 0x04c0 }
                    r4 = r3.intValue(r4);	 Catch:{ Exception -> 0x04c0 }
                    r5 = (long) r4;	 Catch:{ Exception -> 0x04c0 }
                    r5 = r11.indexOfKey(r5);	 Catch:{ Exception -> 0x04c0 }
                    if (r5 < 0) goto L_0x038e;	 Catch:{ Exception -> 0x04c0 }
                    goto L_0x037b;	 Catch:{ Exception -> 0x04c0 }
                    r5 = 2;	 Catch:{ Exception -> 0x04c0 }
                    r6 = r3.stringValue(r5);	 Catch:{ Exception -> 0x04c0 }
                    r7 = org.telegram.messenger.LocaleController.getInstance();	 Catch:{ Exception -> 0x04c0 }
                    r7 = r7.getTranslitString(r6);	 Catch:{ Exception -> 0x04c0 }
                    r8 = r6.equals(r7);	 Catch:{ Exception -> 0x04c0 }
                    if (r8 == 0) goto L_0x03a2;	 Catch:{ Exception -> 0x04c0 }
                    r7 = 0;	 Catch:{ Exception -> 0x04c0 }
                    r8 = 0;	 Catch:{ Exception -> 0x04c0 }
                    r12 = ";;;";	 Catch:{ Exception -> 0x04c0 }
                    r12 = r6.lastIndexOf(r12);	 Catch:{ Exception -> 0x04c0 }
                    r13 = -1;	 Catch:{ Exception -> 0x04c0 }
                    if (r12 == r13) goto L_0x03b3;	 Catch:{ Exception -> 0x04c0 }
                    r14 = r12 + 3;	 Catch:{ Exception -> 0x04c0 }
                    r14 = r6.substring(r14);	 Catch:{ Exception -> 0x04c0 }
                    r8 = r14;	 Catch:{ Exception -> 0x04c0 }
                    r14 = 0;	 Catch:{ Exception -> 0x04c0 }
                    r5 = r15.length;	 Catch:{ Exception -> 0x04c0 }
                    r16 = r14;	 Catch:{ Exception -> 0x04c0 }
                    r14 = 0;	 Catch:{ Exception -> 0x04c0 }
                    if (r14 >= r5) goto L_0x04a4;	 Catch:{ Exception -> 0x04c0 }
                    r17 = r15[r14];	 Catch:{ Exception -> 0x04c0 }
                    r30 = r17;	 Catch:{ Exception -> 0x04c0 }
                    r13 = r30;	 Catch:{ Exception -> 0x04c0 }
                    r17 = r6.startsWith(r13);	 Catch:{ Exception -> 0x04c0 }
                    if (r17 != 0) goto L_0x040e;	 Catch:{ Exception -> 0x04c0 }
                    r31 = r4;	 Catch:{ Exception -> 0x04c0 }
                    r4 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x04c0 }
                    r4.<init>();	 Catch:{ Exception -> 0x04c0 }
                    r32 = r5;	 Catch:{ Exception -> 0x04c0 }
                    r5 = " ";	 Catch:{ Exception -> 0x04c0 }
                    r4.append(r5);	 Catch:{ Exception -> 0x04c0 }
                    r4.append(r13);	 Catch:{ Exception -> 0x04c0 }
                    r4 = r4.toString();	 Catch:{ Exception -> 0x04c0 }
                    r4 = r6.contains(r4);	 Catch:{ Exception -> 0x04c0 }
                    if (r4 != 0) goto L_0x0412;	 Catch:{ Exception -> 0x04c0 }
                    if (r7 == 0) goto L_0x0401;	 Catch:{ Exception -> 0x04c0 }
                    r4 = r7.startsWith(r13);	 Catch:{ Exception -> 0x04c0 }
                    if (r4 != 0) goto L_0x0412;	 Catch:{ Exception -> 0x04c0 }
                    r4 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x04c0 }
                    r4.<init>();	 Catch:{ Exception -> 0x04c0 }
                    r5 = " ";	 Catch:{ Exception -> 0x04c0 }
                    r4.append(r5);	 Catch:{ Exception -> 0x04c0 }
                    r4.append(r13);	 Catch:{ Exception -> 0x04c0 }
                    r4 = r4.toString();	 Catch:{ Exception -> 0x04c0 }
                    r4 = r7.contains(r4);	 Catch:{ Exception -> 0x04c0 }
                    if (r4 == 0) goto L_0x0401;	 Catch:{ Exception -> 0x04c0 }
                    goto L_0x0412;	 Catch:{ Exception -> 0x04c0 }
                    if (r8 == 0) goto L_0x040b;	 Catch:{ Exception -> 0x04c0 }
                    r4 = r8.startsWith(r13);	 Catch:{ Exception -> 0x04c0 }
                    if (r4 == 0) goto L_0x040b;	 Catch:{ Exception -> 0x04c0 }
                    r4 = 2;	 Catch:{ Exception -> 0x04c0 }
                    goto L_0x0413;	 Catch:{ Exception -> 0x04c0 }
                    r4 = r16;	 Catch:{ Exception -> 0x04c0 }
                    goto L_0x0413;	 Catch:{ Exception -> 0x04c0 }
                    r31 = r4;	 Catch:{ Exception -> 0x04c0 }
                    r32 = r5;	 Catch:{ Exception -> 0x04c0 }
                    r4 = 1;	 Catch:{ Exception -> 0x04c0 }
                    if (r4 == 0) goto L_0x048e;	 Catch:{ Exception -> 0x04c0 }
                    r5 = 0;	 Catch:{ Exception -> 0x04c0 }
                    r14 = r3.byteBufferValue(r5);	 Catch:{ Exception -> 0x04c0 }
                    if (r14 == 0) goto L_0x04a4;	 Catch:{ Exception -> 0x04c0 }
                    r33 = r6;	 Catch:{ Exception -> 0x04c0 }
                    r6 = r14.readInt32(r5);	 Catch:{ Exception -> 0x04c0 }
                    r6 = org.telegram.tgnet.TLRPC.User.TLdeserialize(r14, r6, r5);	 Catch:{ Exception -> 0x04c0 }
                    r14.reuse();	 Catch:{ Exception -> 0x04c0 }
                    r5 = new org.telegram.ui.Components.ShareAlert$ShareSearchAdapter$DialogSearchResult;	 Catch:{ Exception -> 0x04c0 }
                    r34 = r7;	 Catch:{ Exception -> 0x04c0 }
                    r7 = org.telegram.ui.Components.ShareAlert.ShareSearchAdapter.this;	 Catch:{ Exception -> 0x04c0 }
                    r35 = r8;	 Catch:{ Exception -> 0x04c0 }
                    r8 = 0;	 Catch:{ Exception -> 0x04c0 }
                    r5.<init>();	 Catch:{ Exception -> 0x04c0 }
                    r7 = r6.status;	 Catch:{ Exception -> 0x04c0 }
                    if (r7 == 0) goto L_0x0445;	 Catch:{ Exception -> 0x04c0 }
                    r7 = r6.status;	 Catch:{ Exception -> 0x04c0 }
                    r36 = r9;	 Catch:{ Exception -> 0x04c0 }
                    r8 = 1;	 Catch:{ Exception -> 0x04c0 }
                    r9 = r3.intValue(r8);	 Catch:{ Exception -> 0x04c0 }
                    r7.expires = r9;	 Catch:{ Exception -> 0x04c0 }
                    goto L_0x0447;	 Catch:{ Exception -> 0x04c0 }
                    r36 = r9;	 Catch:{ Exception -> 0x04c0 }
                    r7 = r5.dialog;	 Catch:{ Exception -> 0x04c0 }
                    r8 = r6.id;	 Catch:{ Exception -> 0x04c0 }
                    r8 = (long) r8;	 Catch:{ Exception -> 0x04c0 }
                    r7.id = r8;	 Catch:{ Exception -> 0x04c0 }
                    r5.object = r6;	 Catch:{ Exception -> 0x04c0 }
                    r7 = 1;	 Catch:{ Exception -> 0x04c0 }
                    if (r4 != r7) goto L_0x045f;	 Catch:{ Exception -> 0x04c0 }
                    r8 = r6.first_name;	 Catch:{ Exception -> 0x04c0 }
                    r9 = r6.last_name;	 Catch:{ Exception -> 0x04c0 }
                    r8 = org.telegram.messenger.AndroidUtilities.generateSearchName(r8, r9, r13);	 Catch:{ Exception -> 0x04c0 }
                    r5.name = r8;	 Catch:{ Exception -> 0x04c0 }
                    r9 = 0;	 Catch:{ Exception -> 0x04c0 }
                    goto L_0x048a;	 Catch:{ Exception -> 0x04c0 }
                    r8 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x04c0 }
                    r8.<init>();	 Catch:{ Exception -> 0x04c0 }
                    r9 = "@";	 Catch:{ Exception -> 0x04c0 }
                    r8.append(r9);	 Catch:{ Exception -> 0x04c0 }
                    r9 = r6.username;	 Catch:{ Exception -> 0x04c0 }
                    r8.append(r9);	 Catch:{ Exception -> 0x04c0 }
                    r8 = r8.toString();	 Catch:{ Exception -> 0x04c0 }
                    r9 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x04c0 }
                    r9.<init>();	 Catch:{ Exception -> 0x04c0 }
                    r7 = "@";	 Catch:{ Exception -> 0x04c0 }
                    r9.append(r7);	 Catch:{ Exception -> 0x04c0 }
                    r9.append(r13);	 Catch:{ Exception -> 0x04c0 }
                    r7 = r9.toString();	 Catch:{ Exception -> 0x04c0 }
                    r9 = 0;	 Catch:{ Exception -> 0x04c0 }
                    r7 = org.telegram.messenger.AndroidUtilities.generateSearchName(r8, r9, r7);	 Catch:{ Exception -> 0x04c0 }
                    r5.name = r7;	 Catch:{ Exception -> 0x04c0 }
                    r2.add(r5);	 Catch:{ Exception -> 0x04c0 }
                    goto L_0x04a7;	 Catch:{ Exception -> 0x04c0 }
                    r33 = r6;	 Catch:{ Exception -> 0x04c0 }
                    r34 = r7;	 Catch:{ Exception -> 0x04c0 }
                    r35 = r8;	 Catch:{ Exception -> 0x04c0 }
                    r36 = r9;	 Catch:{ Exception -> 0x04c0 }
                    r9 = 0;	 Catch:{ Exception -> 0x04c0 }
                    r14 = r14 + 1;	 Catch:{ Exception -> 0x04c0 }
                    r16 = r4;	 Catch:{ Exception -> 0x04c0 }
                    r4 = r31;	 Catch:{ Exception -> 0x04c0 }
                    r5 = r32;	 Catch:{ Exception -> 0x04c0 }
                    r9 = r36;	 Catch:{ Exception -> 0x04c0 }
                    r13 = -1;	 Catch:{ Exception -> 0x04c0 }
                    goto L_0x03b8;	 Catch:{ Exception -> 0x04c0 }
                    r36 = r9;	 Catch:{ Exception -> 0x04c0 }
                    r9 = 0;	 Catch:{ Exception -> 0x04c0 }
                    r9 = r36;	 Catch:{ Exception -> 0x04c0 }
                    goto L_0x037b;	 Catch:{ Exception -> 0x04c0 }
                    r36 = r9;	 Catch:{ Exception -> 0x04c0 }
                    r3.dispose();	 Catch:{ Exception -> 0x04c0 }
                    r4 = new org.telegram.ui.Components.ShareAlert$ShareSearchAdapter$1$1;	 Catch:{ Exception -> 0x04c0 }
                    r4.<init>();	 Catch:{ Exception -> 0x04c0 }
                    java.util.Collections.sort(r2, r4);	 Catch:{ Exception -> 0x04c0 }
                    r4 = org.telegram.ui.Components.ShareAlert.ShareSearchAdapter.this;	 Catch:{ Exception -> 0x04c0 }
                    r5 = r4;	 Catch:{ Exception -> 0x04c0 }
                    r4.updateSearchResults(r2, r5);	 Catch:{ Exception -> 0x04c0 }
                    goto L_0x04c5;
                L_0x04c0:
                    r0 = move-exception;
                    r2 = r0;
                    org.telegram.messenger.FileLog.e(r2);
                    return;
                    */
                    throw new UnsupportedOperationException("Method not decompiled: org.telegram.ui.Components.ShareAlert.ShareSearchAdapter.1.run():void");
                }
            });
        }

        private void updateSearchResults(final ArrayList<DialogSearchResult> result, final int searchId) {
            AndroidUtilities.runOnUIThread(new Runnable() {
                public void run() {
                    if (searchId == ShareSearchAdapter.this.lastSearchId) {
                        boolean z;
                        int a = 0;
                        while (true) {
                            z = true;
                            if (a >= result.size()) {
                                break;
                            }
                            DialogSearchResult obj = (DialogSearchResult) result.get(a);
                            if (obj.object instanceof User) {
                                MessagesController.getInstance(ShareAlert.this.currentAccount).putUser(obj.object, true);
                            } else if (obj.object instanceof Chat) {
                                MessagesController.getInstance(ShareAlert.this.currentAccount).putChat(obj.object, true);
                            }
                            a++;
                        }
                        boolean becomeEmpty = !ShareSearchAdapter.this.searchResult.isEmpty() && result.isEmpty();
                        if (!ShareSearchAdapter.this.searchResult.isEmpty() || !result.isEmpty()) {
                            z = false;
                        }
                        boolean isEmpty = z;
                        if (becomeEmpty) {
                            ShareAlert.this.topBeforeSwitch = ShareAlert.this.getCurrentTop();
                        }
                        ShareSearchAdapter.this.searchResult = result;
                        ShareSearchAdapter.this.notifyDataSetChanged();
                        if (!(isEmpty || becomeEmpty || ShareAlert.this.topBeforeSwitch <= 0)) {
                            ShareAlert.this.layoutManager.scrollToPositionWithOffset(0, -ShareAlert.this.topBeforeSwitch);
                            ShareAlert.this.topBeforeSwitch = C.PRIORITY_DOWNLOAD;
                        }
                    }
                }
            });
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
                        final int searchId = this.lastSearchId + 1;
                        this.lastSearchId = searchId;
                        this.searchTimer = new Timer();
                        this.searchTimer.schedule(new TimerTask() {
                            public void run() {
                                try {
                                    cancel();
                                    ShareSearchAdapter.this.searchTimer.cancel();
                                    ShareSearchAdapter.this.searchTimer = null;
                                } catch (Throwable e) {
                                    FileLog.e(e);
                                }
                                ShareSearchAdapter.this.searchDialogsInternal(query, searchId);
                            }
                        }, 200, 300);
                    }
                }
                this.searchResult.clear();
                ShareAlert.this.topBeforeSwitch = ShareAlert.this.getCurrentTop();
                notifyDataSetChanged();
            }
        }

        public int getItemCount() {
            return this.searchResult.size();
        }

        public TL_dialog getItem(int i) {
            if (i >= 0) {
                if (i < this.searchResult.size()) {
                    return ((DialogSearchResult) this.searchResult.get(i)).dialog;
                }
            }
            return null;
        }

        public long getItemId(int i) {
            return (long) i;
        }

        public boolean isEnabled(ViewHolder holder) {
            return true;
        }

        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = new ShareDialogCell(this.context);
            view.setLayoutParams(new LayoutParams(-1, AndroidUtilities.dp(100.0f)));
            return new Holder(view);
        }

        public void onBindViewHolder(ViewHolder holder, int position) {
            DialogSearchResult result = (DialogSearchResult) this.searchResult.get(position);
            holder.itemView.setDialog((int) result.dialog.id, ShareAlert.this.selectedDialogs.indexOfKey(result.dialog.id) >= 0, result.name);
        }

        public int getItemViewType(int i) {
            return 0;
        }
    }

    private void showCommentTextView(boolean r1) {
        /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.DecodeException: Load method exception in method: org.telegram.ui.Components.ShareAlert.showCommentTextView(boolean):void
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
        r0 = r10.frameLayout2;
        r0 = r0.getTag();
        r1 = 0;
        r2 = 1;
        if (r0 == 0) goto L_0x000c;
    L_0x000a:
        r0 = r2;
        goto L_0x000d;
    L_0x000c:
        r0 = r1;
    L_0x000d:
        if (r11 != r0) goto L_0x0010;
    L_0x000f:
        return;
    L_0x0010:
        r0 = r10.animatorSet;
        if (r0 == 0) goto L_0x0019;
    L_0x0014:
        r0 = r10.animatorSet;
        r0.cancel();
    L_0x0019:
        r0 = r10.frameLayout2;
        if (r11 == 0) goto L_0x0022;
    L_0x001d:
        r3 = java.lang.Integer.valueOf(r2);
        goto L_0x0023;
    L_0x0022:
        r3 = 0;
    L_0x0023:
        r0.setTag(r3);
        r0 = r10.commentTextView;
        org.telegram.messenger.AndroidUtilities.hideKeyboard(r0);
        r0 = new android.animation.AnimatorSet;
        r0.<init>();
        r10.animatorSet = r0;
        r0 = r10.animatorSet;
        r3 = 2;
        r3 = new android.animation.Animator[r3];
        r4 = r10.shadow2;
        r5 = "translationY";
        r6 = new float[r2];
        r7 = 1112801280; // 0x42540000 float:53.0 double:5.49796883E-315;
        r8 = 0;
        if (r11 == 0) goto L_0x0044;
    L_0x0042:
        r9 = r8;
        goto L_0x0046;
        r9 = r7;
        r9 = org.telegram.messenger.AndroidUtilities.dp(r9);
        r9 = (float) r9;
        r6[r1] = r9;
        r4 = android.animation.ObjectAnimator.ofFloat(r4, r5, r6);
        r3[r1] = r4;
        r4 = r10.frameLayout2;
        r5 = "translationY";
        r6 = new float[r2];
        if (r11 == 0) goto L_0x005d;
        r7 = r8;
        goto L_0x005e;
        r7 = org.telegram.messenger.AndroidUtilities.dp(r7);
        r7 = (float) r7;
        r6[r1] = r7;
        r1 = android.animation.ObjectAnimator.ofFloat(r4, r5, r6);
        r3[r2] = r1;
        r0.playTogether(r3);
        r0 = r10.animatorSet;
        r1 = new android.view.animation.DecelerateInterpolator;
        r1.<init>();
        r0.setInterpolator(r1);
        r0 = r10.animatorSet;
        r1 = 180; // 0xb4 float:2.52E-43 double:8.9E-322;
        r0.setDuration(r1);
        r0 = r10.animatorSet;
        r1 = new org.telegram.ui.Components.ShareAlert$10;
        r1.<init>(r11);
        r0.addListener(r1);
        r0 = r10.animatorSet;
        r0.start();
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.telegram.ui.Components.ShareAlert.showCommentTextView(boolean):void");
    }

    public static ShareAlert createShareAlert(Context context, MessageObject messageObject, String text, boolean publicChannel, String copyLink, boolean fullScreen) {
        ArrayList<MessageObject> arrayList;
        if (messageObject != null) {
            arrayList = new ArrayList();
            arrayList.add(messageObject);
        } else {
            arrayList = null;
        }
        return new ShareAlert(context, arrayList, text, publicChannel, copyLink, fullScreen);
    }

    public ShareAlert(Context context, ArrayList<MessageObject> messages, String text, boolean publicChannel, String copyLink, boolean fullScreen) {
        final Context context2 = context;
        ArrayList<MessageObject> arrayList = messages;
        boolean z = publicChannel;
        super(context2, true);
        this.shadowDrawable = context.getResources().getDrawable(R.drawable.sheet_shadow).mutate();
        this.shadowDrawable.setColorFilter(new PorterDuffColorFilter(Theme.getColor(Theme.key_dialogBackground), Mode.MULTIPLY));
        this.linkToCopy = copyLink;
        this.sendingMessageObjects = arrayList;
        this.searchAdapter = new ShareSearchAdapter(context2);
        this.isPublicChannel = z;
        this.sendingText = text;
        if (z) {
            r0.loadingLink = true;
            TL_channels_exportMessageLink req = new TL_channels_exportMessageLink();
            req.id = ((MessageObject) arrayList.get(0)).getId();
            req.channel = MessagesController.getInstance(r0.currentAccount).getInputChannel(((MessageObject) arrayList.get(0)).messageOwner.to_id.channel_id);
            ConnectionsManager.getInstance(r0.currentAccount).sendRequest(req, new RequestDelegate() {
                public void run(final TLObject response, TL_error error) {
                    AndroidUtilities.runOnUIThread(new Runnable() {
                        public void run() {
                            if (response != null) {
                                ShareAlert.this.exportedMessageLink = (TL_exportedMessageLink) response;
                                if (ShareAlert.this.copyLinkOnEnd) {
                                    ShareAlert.this.copyLink(context2);
                                }
                            }
                            ShareAlert.this.loadingLink = false;
                        }
                    });
                }
            });
        }
        r0.containerView = new FrameLayout(context2) {
            private boolean ignoreLayout = false;

            public boolean onInterceptTouchEvent(MotionEvent ev) {
                if (ev.getAction() != 0 || ShareAlert.this.scrollOffsetY == 0 || ev.getY() >= ((float) ShareAlert.this.scrollOffsetY)) {
                    return super.onInterceptTouchEvent(ev);
                }
                ShareAlert.this.dismiss();
                return true;
            }

            public boolean onTouchEvent(MotionEvent e) {
                return !ShareAlert.this.isDismissed() && super.onTouchEvent(e);
            }

            protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
                int height = MeasureSpec.getSize(heightMeasureSpec);
                if (VERSION.SDK_INT >= 21) {
                    height -= AndroidUtilities.statusBarHeight;
                }
                int contentSize = (AndroidUtilities.dp(48.0f) + (Math.max(3, (int) Math.ceil((double) (((float) Math.max(ShareAlert.this.searchAdapter.getItemCount(), ShareAlert.this.listAdapter.getItemCount())) / 4.0f))) * AndroidUtilities.dp(100.0f))) + ShareAlert.backgroundPaddingTop;
                float f = 8.0f;
                int padding = contentSize < height ? 0 : (height - ((height / 5) * 3)) + AndroidUtilities.dp(8.0f);
                if (ShareAlert.this.gridView.getPaddingTop() != padding) {
                    this.ignoreLayout = true;
                    RecyclerListView access$800 = ShareAlert.this.gridView;
                    if (ShareAlert.this.frameLayout2.getTag() != null) {
                        f = 56.0f;
                    }
                    access$800.setPadding(0, padding, 0, AndroidUtilities.dp(f));
                    this.ignoreLayout = false;
                }
                super.onMeasure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(Math.min(contentSize, height), 1073741824));
            }

            protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
                super.onLayout(changed, left, top, right, bottom);
                ShareAlert.this.updateLayout();
            }

            public void requestLayout() {
                if (!this.ignoreLayout) {
                    super.requestLayout();
                }
            }

            protected void onDraw(Canvas canvas) {
                ShareAlert.this.shadowDrawable.setBounds(0, ShareAlert.this.scrollOffsetY - ShareAlert.backgroundPaddingTop, getMeasuredWidth(), getMeasuredHeight());
                ShareAlert.this.shadowDrawable.draw(canvas);
            }
        };
        r0.containerView.setWillNotDraw(false);
        r0.containerView.setPadding(backgroundPaddingLeft, 0, backgroundPaddingLeft, 0);
        r0.frameLayout = new FrameLayout(context2);
        r0.frameLayout.setBackgroundColor(Theme.getColor(Theme.key_dialogBackground));
        r0.frameLayout.setOnTouchListener(new OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });
        r0.doneButton = new LinearLayout(context2);
        r0.doneButton.setOrientation(0);
        r0.doneButton.setBackgroundDrawable(Theme.createSelectorDrawable(Theme.getColor(Theme.key_dialogButtonSelector), 0));
        r0.doneButton.setPadding(AndroidUtilities.dp(21.0f), 0, AndroidUtilities.dp(21.0f), 0);
        r0.frameLayout.addView(r0.doneButton, LayoutHelper.createFrame(-2, -1, 53));
        r0.doneButton.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                int a = 0;
                if (ShareAlert.this.selectedDialogs.size() != 0 || (!ShareAlert.this.isPublicChannel && ShareAlert.this.linkToCopy == null)) {
                    int a2;
                    long key;
                    if (ShareAlert.this.sendingMessageObjects != null) {
                        while (true) {
                            a2 = a;
                            if (a2 >= ShareAlert.this.selectedDialogs.size()) {
                                break;
                            }
                            key = ShareAlert.this.selectedDialogs.keyAt(a2);
                            if (ShareAlert.this.frameLayout2.getTag() != null && ShareAlert.this.commentTextView.length() > 0) {
                                SendMessagesHelper.getInstance(ShareAlert.this.currentAccount).sendMessage(ShareAlert.this.commentTextView.getText().toString(), key, null, null, true, null, null, null);
                            }
                            SendMessagesHelper.getInstance(ShareAlert.this.currentAccount).sendMessage(ShareAlert.this.sendingMessageObjects, key);
                            a = a2 + 1;
                        }
                    } else if (ShareAlert.this.sendingText != null) {
                        while (true) {
                            a2 = a;
                            if (a2 >= ShareAlert.this.selectedDialogs.size()) {
                                break;
                            }
                            key = ShareAlert.this.selectedDialogs.keyAt(a2);
                            if (ShareAlert.this.frameLayout2.getTag() != null && ShareAlert.this.commentTextView.length() > 0) {
                                SendMessagesHelper.getInstance(ShareAlert.this.currentAccount).sendMessage(ShareAlert.this.commentTextView.getText().toString(), key, null, null, true, null, null, null);
                            }
                            SendMessagesHelper.getInstance(ShareAlert.this.currentAccount).sendMessage(ShareAlert.this.sendingText, key, null, null, true, null, null, null);
                            a = a2 + 1;
                        }
                    }
                    ShareAlert.this.dismiss();
                    return;
                }
                if (ShareAlert.this.linkToCopy == null && ShareAlert.this.loadingLink) {
                    ShareAlert.this.copyLinkOnEnd = true;
                    Toast.makeText(ShareAlert.this.getContext(), LocaleController.getString("Loading", R.string.Loading), 0).show();
                } else {
                    ShareAlert.this.copyLink(ShareAlert.this.getContext());
                }
                ShareAlert.this.dismiss();
            }
        });
        r0.doneButtonBadgeTextView = new TextView(context2);
        r0.doneButtonBadgeTextView.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
        r0.doneButtonBadgeTextView.setTextSize(1, 13.0f);
        r0.doneButtonBadgeTextView.setTextColor(Theme.getColor(Theme.key_dialogBadgeText));
        r0.doneButtonBadgeTextView.setGravity(17);
        r0.doneButtonBadgeTextView.setBackgroundDrawable(Theme.createRoundRectDrawable(AndroidUtilities.dp(12.5f), Theme.getColor(Theme.key_dialogBadgeBackground)));
        r0.doneButtonBadgeTextView.setMinWidth(AndroidUtilities.dp(23.0f));
        r0.doneButtonBadgeTextView.setPadding(AndroidUtilities.dp(8.0f), 0, AndroidUtilities.dp(8.0f), AndroidUtilities.dp(1.0f));
        r0.doneButton.addView(r0.doneButtonBadgeTextView, LayoutHelper.createLinear(-2, 23, 16, 0, 0, 10, 0));
        r0.doneButtonTextView = new TextView(context2);
        r0.doneButtonTextView.setTextSize(1, 14.0f);
        r0.doneButtonTextView.setGravity(17);
        r0.doneButtonTextView.setCompoundDrawablePadding(AndroidUtilities.dp(8.0f));
        r0.doneButtonTextView.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
        r0.doneButton.addView(r0.doneButtonTextView, LayoutHelper.createLinear(-2, -2, 16));
        ImageView imageView = new ImageView(context2);
        imageView.setImageResource(R.drawable.ic_ab_search);
        imageView.setColorFilter(new PorterDuffColorFilter(Theme.getColor(Theme.key_dialogIcon), Mode.MULTIPLY));
        imageView.setScaleType(ScaleType.CENTER);
        imageView.setPadding(0, AndroidUtilities.dp(2.0f), 0, 0);
        r0.frameLayout.addView(imageView, LayoutHelper.createFrame(48, 48, 19));
        r0.nameTextView = new EditTextBoldCursor(context2);
        r0.nameTextView.setHint(LocaleController.getString("ShareSendTo", R.string.ShareSendTo));
        r0.nameTextView.setMaxLines(1);
        r0.nameTextView.setSingleLine(true);
        r0.nameTextView.setGravity(19);
        r0.nameTextView.setTextSize(1, 16.0f);
        r0.nameTextView.setBackgroundDrawable(null);
        r0.nameTextView.setHintTextColor(Theme.getColor(Theme.key_dialogTextHint));
        r0.nameTextView.setImeOptions(268435456);
        r0.nameTextView.setInputType(16385);
        r0.nameTextView.setCursorColor(Theme.getColor(Theme.key_dialogTextBlack));
        r0.nameTextView.setCursorSize(AndroidUtilities.dp(20.0f));
        r0.nameTextView.setCursorWidth(1.5f);
        r0.nameTextView.setTextColor(Theme.getColor(Theme.key_dialogTextBlack));
        r0.frameLayout.addView(r0.nameTextView, LayoutHelper.createFrame(-1, -1.0f, 51, 48.0f, 2.0f, 96.0f, 0.0f));
        r0.nameTextView.addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            public void afterTextChanged(Editable s) {
                String text = ShareAlert.this.nameTextView.getText().toString();
                if (text.length() != 0) {
                    if (ShareAlert.this.gridView.getAdapter() != ShareAlert.this.searchAdapter) {
                        ShareAlert.this.topBeforeSwitch = ShareAlert.this.getCurrentTop();
                        ShareAlert.this.gridView.setAdapter(ShareAlert.this.searchAdapter);
                        ShareAlert.this.searchAdapter.notifyDataSetChanged();
                    }
                    if (ShareAlert.this.searchEmptyView != null) {
                        ShareAlert.this.searchEmptyView.setText(LocaleController.getString("NoResult", R.string.NoResult));
                    }
                } else if (ShareAlert.this.gridView.getAdapter() != ShareAlert.this.listAdapter) {
                    int top = ShareAlert.this.getCurrentTop();
                    ShareAlert.this.searchEmptyView.setText(LocaleController.getString("NoChats", R.string.NoChats));
                    ShareAlert.this.gridView.setAdapter(ShareAlert.this.listAdapter);
                    ShareAlert.this.listAdapter.notifyDataSetChanged();
                    if (top > 0) {
                        ShareAlert.this.layoutManager.scrollToPositionWithOffset(0, -top);
                    }
                }
                if (ShareAlert.this.searchAdapter != null) {
                    ShareAlert.this.searchAdapter.searchDialogs(text);
                }
            }
        });
        r0.gridView = new RecyclerListView(context2);
        r0.gridView.setTag(Integer.valueOf(13));
        r0.gridView.setPadding(0, 0, 0, AndroidUtilities.dp(8.0f));
        r0.gridView.setClipToPadding(false);
        RecyclerListView recyclerListView = r0.gridView;
        LayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 4);
        r0.layoutManager = gridLayoutManager;
        recyclerListView.setLayoutManager(gridLayoutManager);
        r0.gridView.setHorizontalScrollBarEnabled(false);
        r0.gridView.setVerticalScrollBarEnabled(false);
        r0.gridView.addItemDecoration(new ItemDecoration() {
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, State state) {
                Holder holder = (Holder) parent.getChildViewHolder(view);
                if (holder != null) {
                    int pos = holder.getAdapterPosition();
                    int i = 0;
                    outRect.left = pos % 4 == 0 ? 0 : AndroidUtilities.dp(4.0f);
                    if (pos % 4 != 3) {
                        i = AndroidUtilities.dp(4.0f);
                    }
                    outRect.right = i;
                    return;
                }
                outRect.left = AndroidUtilities.dp(4.0f);
                outRect.right = AndroidUtilities.dp(4.0f);
            }
        });
        r0.containerView.addView(r0.gridView, LayoutHelper.createFrame(-1, -1.0f, 51, 0.0f, 48.0f, 0.0f, 0.0f));
        recyclerListView = r0.gridView;
        Adapter shareDialogsAdapter = new ShareDialogsAdapter(context2);
        r0.listAdapter = shareDialogsAdapter;
        recyclerListView.setAdapter(shareDialogsAdapter);
        r0.gridView.setGlowColor(Theme.getColor(Theme.key_dialogScrollGlow));
        r0.gridView.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(View view, int position) {
                if (position >= 0) {
                    TL_dialog dialog;
                    if (ShareAlert.this.gridView.getAdapter() == ShareAlert.this.listAdapter) {
                        dialog = ShareAlert.this.listAdapter.getItem(position);
                    } else {
                        dialog = ShareAlert.this.searchAdapter.getItem(position);
                    }
                    if (dialog != null) {
                        ShareDialogCell cell = (ShareDialogCell) view;
                        if (ShareAlert.this.selectedDialogs.indexOfKey(dialog.id) >= 0) {
                            ShareAlert.this.selectedDialogs.remove(dialog.id);
                            cell.setChecked(false, true);
                        } else {
                            ShareAlert.this.selectedDialogs.put(dialog.id, dialog);
                            cell.setChecked(true, true);
                        }
                        ShareAlert.this.updateSelectedCount();
                    }
                }
            }
        });
        r0.gridView.setOnScrollListener(new OnScrollListener() {
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                ShareAlert.this.updateLayout();
            }
        });
        r0.searchEmptyView = new EmptyTextProgressView(context2);
        r0.searchEmptyView.setShowAtCenter(true);
        r0.searchEmptyView.showTextView();
        r0.searchEmptyView.setText(LocaleController.getString("NoChats", R.string.NoChats));
        r0.gridView.setEmptyView(r0.searchEmptyView);
        r0.containerView.addView(r0.searchEmptyView, LayoutHelper.createFrame(-1, -1.0f, 51, 0.0f, 48.0f, 0.0f, 0.0f));
        r0.containerView.addView(r0.frameLayout, LayoutHelper.createFrame(-1, 48, 51));
        r0.shadow = new View(context2);
        r0.shadow.setBackgroundResource(R.drawable.header_shadow);
        r0.containerView.addView(r0.shadow, LayoutHelper.createFrame(-1, 3.0f, 51, 0.0f, 48.0f, 0.0f, 0.0f));
        r0.frameLayout2 = new FrameLayout(context2);
        r0.frameLayout2.setBackgroundColor(Theme.getColor(Theme.key_dialogBackground));
        r0.frameLayout2.setTranslationY((float) AndroidUtilities.dp(53.0f));
        r0.containerView.addView(r0.frameLayout2, LayoutHelper.createFrame(-1, 48, 83));
        r0.frameLayout2.setOnTouchListener(new OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });
        r0.commentTextView = new EditTextBoldCursor(context2);
        r0.commentTextView.setHint(LocaleController.getString("ShareComment", R.string.ShareComment));
        r0.commentTextView.setMaxLines(1);
        r0.commentTextView.setSingleLine(true);
        r0.commentTextView.setGravity(19);
        r0.commentTextView.setTextSize(1, 16.0f);
        r0.commentTextView.setBackgroundDrawable(null);
        r0.commentTextView.setHintTextColor(Theme.getColor(Theme.key_dialogTextHint));
        r0.commentTextView.setImeOptions(268435456);
        r0.commentTextView.setInputType(16385);
        r0.commentTextView.setCursorColor(Theme.getColor(Theme.key_dialogTextBlack));
        r0.commentTextView.setCursorSize(AndroidUtilities.dp(20.0f));
        r0.commentTextView.setCursorWidth(1.5f);
        r0.commentTextView.setTextColor(Theme.getColor(Theme.key_dialogTextBlack));
        r0.frameLayout2.addView(r0.commentTextView, LayoutHelper.createFrame(-1, -1.0f, 51, 8.0f, 1.0f, 8.0f, 0.0f));
        r0.shadow2 = new View(context2);
        r0.shadow2.setBackgroundResource(R.drawable.header_shadow_reverse);
        r0.shadow2.setTranslationY((float) AndroidUtilities.dp(53.0f));
        r0.containerView.addView(r0.shadow2, LayoutHelper.createFrame(-1, 3.0f, 83, 0.0f, 0.0f, 0.0f, 48.0f));
        updateSelectedCount();
        if (!DialogsActivity.dialogsLoaded[r0.currentAccount]) {
            MessagesController.getInstance(r0.currentAccount).loadDialogs(0, 100, true);
            ContactsController.getInstance(r0.currentAccount).checkInviteText();
            DialogsActivity.dialogsLoaded[r0.currentAccount] = true;
        }
        if (r0.listAdapter.dialogs.isEmpty()) {
            NotificationCenter.getInstance(r0.currentAccount).addObserver(r0, NotificationCenter.dialogsNeedReload);
        }
    }

    private int getCurrentTop() {
        if (this.gridView.getChildCount() != 0) {
            int i = 0;
            View child = this.gridView.getChildAt(0);
            Holder holder = (Holder) this.gridView.findContainingViewHolder(child);
            if (holder != null) {
                int paddingTop = this.gridView.getPaddingTop();
                if (holder.getAdapterPosition() == 0 && child.getTop() >= 0) {
                    i = child.getTop();
                }
                return paddingTop - i;
            }
        }
        return C.PRIORITY_DOWNLOAD;
    }

    public void didReceivedNotification(int id, int account, Object... args) {
        if (id == NotificationCenter.dialogsNeedReload) {
            if (this.listAdapter != null) {
                this.listAdapter.fetchDialogs();
            }
            NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.dialogsNeedReload);
        }
    }

    protected boolean canDismissWithSwipe() {
        return false;
    }

    @SuppressLint({"NewApi"})
    private void updateLayout() {
        if (this.gridView.getChildCount() > 0) {
            int newOffset = 0;
            View child = this.gridView.getChildAt(0);
            Holder holder = (Holder) this.gridView.findContainingViewHolder(child);
            int top = child.getTop() - AndroidUtilities.dp(8.0f);
            if (top > 0 && holder != null && holder.getAdapterPosition() == 0) {
                newOffset = top;
            }
            if (this.scrollOffsetY != newOffset) {
                RecyclerListView recyclerListView = this.gridView;
                this.scrollOffsetY = newOffset;
                recyclerListView.setTopGlowOffset(newOffset);
                this.frameLayout.setTranslationY((float) this.scrollOffsetY);
                this.shadow.setTranslationY((float) this.scrollOffsetY);
                this.searchEmptyView.setTranslationY((float) this.scrollOffsetY);
                this.containerView.invalidate();
            }
        }
    }

    private void copyLink(Context context) {
        if (this.exportedMessageLink != null || this.linkToCopy != null) {
            try {
                ((ClipboardManager) ApplicationLoader.applicationContext.getSystemService("clipboard")).setPrimaryClip(ClipData.newPlainText("label", this.linkToCopy != null ? this.linkToCopy : this.exportedMessageLink.link));
                Toast.makeText(context, LocaleController.getString("LinkCopied", R.string.LinkCopied), 0).show();
            } catch (Throwable e) {
                FileLog.e(e);
            }
        }
    }

    public void updateSelectedCount() {
        if (this.selectedDialogs.size() == 0) {
            showCommentTextView(false);
            this.doneButtonBadgeTextView.setVisibility(8);
            if (this.isPublicChannel || this.linkToCopy != null) {
                this.doneButtonTextView.setTextColor(Theme.getColor(Theme.key_dialogTextBlue2));
                this.doneButton.setEnabled(true);
                this.doneButtonTextView.setText(LocaleController.getString("CopyLink", R.string.CopyLink).toUpperCase());
                return;
            }
            this.doneButtonTextView.setTextColor(Theme.getColor(Theme.key_dialogTextGray4));
            this.doneButton.setEnabled(false);
            this.doneButtonTextView.setText(LocaleController.getString("Send", R.string.Send).toUpperCase());
            return;
        }
        showCommentTextView(true);
        this.doneButtonTextView.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
        this.doneButtonBadgeTextView.setVisibility(0);
        this.doneButtonBadgeTextView.setText(String.format("%d", new Object[]{Integer.valueOf(this.selectedDialogs.size())}));
        this.doneButtonTextView.setTextColor(Theme.getColor(Theme.key_dialogTextBlue3));
        this.doneButton.setEnabled(true);
        this.doneButtonTextView.setText(LocaleController.getString("Send", R.string.Send).toUpperCase());
    }

    public void dismiss() {
        super.dismiss();
        NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.dialogsNeedReload);
    }
}
