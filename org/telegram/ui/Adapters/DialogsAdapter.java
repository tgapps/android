package org.telegram.ui.Adapters;

import android.content.Context;
import android.view.View;
import android.view.View.MeasureSpec;
import android.widget.FrameLayout;
import java.util.ArrayList;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.UserConfig;
import org.telegram.messenger.support.widget.RecyclerView.ViewHolder;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC.RecentMeUrl;
import org.telegram.tgnet.TLRPC.TL_dialog;
import org.telegram.ui.Cells.DialogCell;
import org.telegram.ui.Components.RecyclerListView.SelectionAdapter;

public class DialogsAdapter extends SelectionAdapter {
    private int currentAccount = UserConfig.selectedAccount;
    private int currentCount;
    private int dialogsType;
    private boolean hasHints;
    private boolean isOnlySelect;
    private Context mContext;
    private long openedDialogId;
    private ArrayList<Long> selectedDialogs;

    class AnonymousClass2 extends FrameLayout {
        AnonymousClass2(Context x0) {
            super(x0);
        }

        protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
            super.onMeasure(MeasureSpec.makeMeasureSpec(MeasureSpec.getSize(widthMeasureSpec), 1073741824), MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(12.0f), 1073741824));
        }
    }

    public org.telegram.messenger.support.widget.RecyclerView.ViewHolder onCreateViewHolder(android.view.ViewGroup r1, int r2) {
        /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.DecodeException: Load method exception in method: org.telegram.ui.Adapters.DialogsAdapter.onCreateViewHolder(android.view.ViewGroup, int):org.telegram.messenger.support.widget.RecyclerView$ViewHolder
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
        r0 = 5;
        r1 = -1;
        switch(r15) {
            case 0: goto L_0x00be;
            case 1: goto L_0x00b6;
            case 2: goto L_0x0048;
            case 3: goto L_0x0017;
            case 4: goto L_0x000e;
            default: goto L_0x0005;
        };
    L_0x0005:
        r2 = new org.telegram.ui.Cells.DialogsEmptyCell;
        r3 = r13.mContext;
        r2.<init>(r3);
        goto L_0x00c8;
    L_0x000e:
        r2 = new org.telegram.ui.Cells.DialogMeUrlCell;
        r3 = r13.mContext;
        r2.<init>(r3);
        goto L_0x00c8;
    L_0x0017:
        r2 = new org.telegram.ui.Adapters.DialogsAdapter$2;
        r3 = r13.mContext;
        r2.<init>(r3);
        r3 = "windowBackgroundGray";
        r3 = org.telegram.ui.ActionBar.Theme.getColor(r3);
        r2.setBackgroundColor(r3);
        r3 = new android.view.View;
        r4 = r13.mContext;
        r3.<init>(r4);
        r4 = r13.mContext;
        r5 = 2131165331; // 0x7f070093 float:1.7944876E38 double:1.0529355757E-314;
        r6 = "windowBackgroundGrayShadow";
        r4 = org.telegram.ui.ActionBar.Theme.getThemedDrawable(r4, r5, r6);
        r3.setBackgroundDrawable(r4);
        r4 = -1082130432; // 0xffffffffbf800000 float:-1.0 double:NaN;
        r4 = org.telegram.ui.Components.LayoutHelper.createFrame(r1, r4);
        r2.addView(r3, r4);
        r4 = r2;
        goto L_0x00c8;
    L_0x0048:
        r2 = new org.telegram.ui.Cells.HeaderCell;
        r3 = r13.mContext;
        r2.<init>(r3);
        r3 = "RecentlyViewed";
        r4 = 2131494217; // 0x7f0c0549 float:1.8611936E38 double:1.053098067E-314;
        r3 = org.telegram.messenger.LocaleController.getString(r3, r4);
        r2.setText(r3);
        r3 = new android.widget.TextView;
        r4 = r13.mContext;
        r3.<init>(r4);
        r4 = 1;
        r5 = 1097859072; // 0x41700000 float:15.0 double:5.424144515E-315;
        r3.setTextSize(r4, r5);
        r4 = "fonts/rmedium.ttf";
        r4 = org.telegram.messenger.AndroidUtilities.getTypeface(r4);
        r3.setTypeface(r4);
        r4 = "windowBackgroundWhiteBlueHeader";
        r4 = org.telegram.ui.ActionBar.Theme.getColor(r4);
        r3.setTextColor(r4);
        r4 = "RecentlyViewedHide";
        r5 = 2131494218; // 0x7f0c054a float:1.8611938E38 double:1.0530980674E-314;
        r4 = org.telegram.messenger.LocaleController.getString(r4, r5);
        r3.setText(r4);
        r4 = org.telegram.messenger.LocaleController.isRTL;
        r5 = 3;
        if (r4 == 0) goto L_0x008d;
        r4 = r5;
        goto L_0x008e;
        r4 = r0;
        r4 = r4 | 16;
        r3.setGravity(r4);
        r6 = -1;
        r7 = -1082130432; // 0xffffffffbf800000 float:-1.0 double:NaN;
        r4 = org.telegram.messenger.LocaleController.isRTL;
        if (r4 == 0) goto L_0x009b;
        goto L_0x009c;
        r5 = r0;
        r8 = r5 | 48;
        r9 = 1099431936; // 0x41880000 float:17.0 double:5.431915495E-315;
        r10 = 1097859072; // 0x41700000 float:15.0 double:5.424144515E-315;
        r11 = 1099431936; // 0x41880000 float:17.0 double:5.431915495E-315;
        r12 = 0;
        r4 = org.telegram.ui.Components.LayoutHelper.createFrame(r6, r7, r8, r9, r10, r11, r12);
        r2.addView(r3, r4);
        r4 = new org.telegram.ui.Adapters.DialogsAdapter$1;
        r4.<init>();
        r3.setOnClickListener(r4);
        r4 = r2;
        goto L_0x00c8;
    L_0x00b6:
        r2 = new org.telegram.ui.Cells.LoadingCell;
        r3 = r13.mContext;
        r2.<init>(r3);
        goto L_0x00c8;
    L_0x00be:
        r2 = new org.telegram.ui.Cells.DialogCell;
        r3 = r13.mContext;
        r4 = r13.isOnlySelect;
        r2.<init>(r3, r4);
        r3 = new org.telegram.messenger.support.widget.RecyclerView$LayoutParams;
        if (r15 != r0) goto L_0x00cf;
        r0 = r1;
        goto L_0x00d0;
        r0 = -2;
        r3.<init>(r1, r0);
        r2.setLayoutParams(r3);
        r0 = new org.telegram.ui.Components.RecyclerListView$Holder;
        r0.<init>(r2);
        return r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.telegram.ui.Adapters.DialogsAdapter.onCreateViewHolder(android.view.ViewGroup, int):org.telegram.messenger.support.widget.RecyclerView$ViewHolder");
    }

    public DialogsAdapter(Context context, int type, boolean onlySelect) {
        this.mContext = context;
        this.dialogsType = type;
        this.isOnlySelect = onlySelect;
        boolean z = type == 0 && !onlySelect;
        this.hasHints = z;
        if (onlySelect) {
            this.selectedDialogs = new ArrayList();
        }
    }

    public void setOpenedDialogId(long id) {
        this.openedDialogId = id;
    }

    public boolean hasSelectedDialogs() {
        return (this.selectedDialogs == null || this.selectedDialogs.isEmpty()) ? false : true;
    }

    public void addOrRemoveSelectedDialog(long did, View cell) {
        if (this.selectedDialogs.contains(Long.valueOf(did))) {
            this.selectedDialogs.remove(Long.valueOf(did));
            if (cell instanceof DialogCell) {
                ((DialogCell) cell).setChecked(false, true);
                return;
            }
            return;
        }
        this.selectedDialogs.add(Long.valueOf(did));
        if (cell instanceof DialogCell) {
            ((DialogCell) cell).setChecked(true, true);
        }
    }

    public ArrayList<Long> getSelectedDialogs() {
        return this.selectedDialogs;
    }

    public boolean isDataSetChanged() {
        int current = this.currentCount;
        if (current == getItemCount()) {
            return current == 1;
        } else {
            return true;
        }
    }

    private ArrayList<TL_dialog> getDialogsArray() {
        if (this.dialogsType == 0) {
            return MessagesController.getInstance(this.currentAccount).dialogs;
        }
        if (this.dialogsType == 1) {
            return MessagesController.getInstance(this.currentAccount).dialogsServerOnly;
        }
        if (this.dialogsType == 2) {
            return MessagesController.getInstance(this.currentAccount).dialogsGroupsOnly;
        }
        if (this.dialogsType == 3) {
            return MessagesController.getInstance(this.currentAccount).dialogsForward;
        }
        return null;
    }

    public int getItemCount() {
        int count = getDialogsArray().size();
        if (count == 0 && MessagesController.getInstance(this.currentAccount).loadingDialogs) {
            return 0;
        }
        if (!MessagesController.getInstance(this.currentAccount).dialogsEndReached || count == 0) {
            count++;
        }
        if (this.hasHints) {
            count += 2 + MessagesController.getInstance(this.currentAccount).hintDialogs.size();
        }
        this.currentCount = count;
        return count;
    }

    public TLObject getItem(int i) {
        ArrayList<TL_dialog> arrayList = getDialogsArray();
        if (this.hasHints) {
            int count = MessagesController.getInstance(this.currentAccount).hintDialogs.size();
            if (i < 2 + count) {
                return (TLObject) MessagesController.getInstance(this.currentAccount).hintDialogs.get(i - 1);
            }
            i -= count + 2;
        }
        if (i >= 0) {
            if (i < arrayList.size()) {
                return (TLObject) arrayList.get(i);
            }
        }
        return null;
    }

    public void notifyDataSetChanged() {
        boolean z = (this.dialogsType != 0 || this.isOnlySelect || MessagesController.getInstance(this.currentAccount).hintDialogs.isEmpty()) ? false : true;
        this.hasHints = z;
        super.notifyDataSetChanged();
    }

    public void onViewAttachedToWindow(ViewHolder holder) {
        if (holder.itemView instanceof DialogCell) {
            ((DialogCell) holder.itemView).checkCurrentDialogIndex();
        }
    }

    public boolean isEnabled(ViewHolder holder) {
        int viewType = holder.getItemViewType();
        return (viewType == 1 || viewType == 5 || viewType == 3) ? false : true;
    }

    public void onBindViewHolder(ViewHolder holder, int i) {
        int itemViewType = holder.getItemViewType();
        if (itemViewType == 0) {
            DialogCell cell = holder.itemView;
            TL_dialog dialog = (TL_dialog) getItem(i);
            if (this.hasHints) {
                i -= 2 + MessagesController.getInstance(this.currentAccount).hintDialogs.size();
            }
            boolean z = true;
            cell.useSeparator = i != getItemCount() - 1;
            if (this.dialogsType == 0 && AndroidUtilities.isTablet()) {
                if (dialog.id != this.openedDialogId) {
                    z = false;
                }
                cell.setDialogSelected(z);
            }
            if (this.selectedDialogs != null) {
                cell.setChecked(this.selectedDialogs.contains(Long.valueOf(dialog.id)), false);
            }
            cell.setDialog(dialog, i, this.dialogsType);
        } else if (itemViewType == 4) {
            holder.itemView.setRecentMeUrl((RecentMeUrl) getItem(i));
        }
    }

    public int getItemViewType(int i) {
        if (this.hasHints) {
            int count = MessagesController.getInstance(this.currentAccount).hintDialogs.size();
            if (i >= 2 + count) {
                i -= 2 + count;
            } else if (i == 0) {
                return 2;
            } else {
                if (i == 1 + count) {
                    return 3;
                }
                return 4;
            }
        }
        if (i != getDialogsArray().size()) {
            return 0;
        }
        if (MessagesController.getInstance(this.currentAccount).dialogsEndReached) {
            return 5;
        }
        return 1;
    }
}
