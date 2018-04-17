package org.telegram.ui.Adapters;

import android.content.Context;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.UserConfig;
import org.telegram.messenger.beta.R;
import org.telegram.messenger.support.widget.RecyclerView.ViewHolder;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Cells.DrawerActionCell;
import org.telegram.ui.Cells.DrawerProfileCell;
import org.telegram.ui.Components.RecyclerListView.SelectionAdapter;

public class DrawerLayoutAdapter extends SelectionAdapter {
    private ArrayList<Integer> accountNumbers = new ArrayList();
    private boolean accountsShowed;
    private ArrayList<Item> items = new ArrayList(11);
    private Context mContext;
    private DrawerProfileCell profileCell;

    private class Item {
        public int icon;
        public int id;
        public String text;

        public Item(int id, String text, int icon) {
            this.icon = icon;
            this.id = id;
            this.text = text;
        }

        public void bind(DrawerActionCell actionCell) {
            actionCell.setTextAndIcon(this.text, this.icon);
        }
    }

    public org.telegram.messenger.support.widget.RecyclerView.ViewHolder onCreateViewHolder(android.view.ViewGroup r1, int r2) {
        /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.DecodeException: Load method exception in method: org.telegram.ui.Adapters.DrawerLayoutAdapter.onCreateViewHolder(android.view.ViewGroup, int):org.telegram.messenger.support.widget.RecyclerView$ViewHolder
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
        if (r6 == 0) goto L_0x0033;
    L_0x0002:
        switch(r6) {
            case 2: goto L_0x002b;
            case 3: goto L_0x0023;
            case 4: goto L_0x001b;
            case 5: goto L_0x0013;
            default: goto L_0x0005;
        };
    L_0x0005:
        r0 = new org.telegram.ui.Cells.EmptyCell;
        r1 = r4.mContext;
        r2 = 1090519040; // 0x41000000 float:8.0 double:5.38787994E-315;
        r2 = org.telegram.messenger.AndroidUtilities.dp(r2);
        r0.<init>(r1, r2);
        goto L_0x0049;
    L_0x0013:
        r0 = new org.telegram.ui.Cells.DrawerAddCell;
        r1 = r4.mContext;
        r0.<init>(r1);
        goto L_0x0049;
    L_0x001b:
        r0 = new org.telegram.ui.Cells.DrawerUserCell;
        r1 = r4.mContext;
        r0.<init>(r1);
        goto L_0x0049;
    L_0x0023:
        r0 = new org.telegram.ui.Cells.DrawerActionCell;
        r1 = r4.mContext;
        r0.<init>(r1);
        goto L_0x0049;
    L_0x002b:
        r0 = new org.telegram.ui.Cells.DividerCell;
        r1 = r4.mContext;
        r0.<init>(r1);
        goto L_0x0049;
    L_0x0033:
        r0 = new org.telegram.ui.Cells.DrawerProfileCell;
        r1 = r4.mContext;
        r0.<init>(r1);
        r4.profileCell = r0;
        r0 = r4.profileCell;
        r1 = new org.telegram.ui.Adapters.DrawerLayoutAdapter$1;
        r1.<init>();
        r0.setOnArrowClickListener(r1);
        r0 = r4.profileCell;
        r1 = new org.telegram.messenger.support.widget.RecyclerView$LayoutParams;
        r2 = -1;
        r3 = -2;
        r1.<init>(r2, r3);
        r0.setLayoutParams(r1);
        r1 = new org.telegram.ui.Components.RecyclerListView$Holder;
        r1.<init>(r0);
        return r1;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.telegram.ui.Adapters.DrawerLayoutAdapter.onCreateViewHolder(android.view.ViewGroup, int):org.telegram.messenger.support.widget.RecyclerView$ViewHolder");
    }

    public DrawerLayoutAdapter(Context context) {
        this.mContext = context;
        boolean z = true;
        if (UserConfig.getActivatedAccountsCount() <= 1 || !MessagesController.getGlobalMainSettings().getBoolean("accountsShowed", true)) {
            z = false;
        }
        this.accountsShowed = z;
        Theme.createDialogsResources(context);
        resetItems();
    }

    private int getAccountRowsCount() {
        int count = this.accountNumbers.size() + 1;
        if (this.accountNumbers.size() < 3) {
            return count + 1;
        }
        return count;
    }

    public int getItemCount() {
        int count = this.items.size() + 2;
        if (this.accountsShowed) {
            return count + getAccountRowsCount();
        }
        return count;
    }

    public void setAccountsShowed(boolean value, boolean animated) {
        if (this.accountsShowed != value) {
            this.accountsShowed = value;
            if (this.profileCell != null) {
                this.profileCell.setAccountsShowed(this.accountsShowed);
            }
            MessagesController.getGlobalMainSettings().edit().putBoolean("accountsShowed", this.accountsShowed).commit();
            if (!animated) {
                notifyDataSetChanged();
            } else if (this.accountsShowed) {
                notifyItemRangeInserted(2, getAccountRowsCount());
            } else {
                notifyItemRangeRemoved(2, getAccountRowsCount());
            }
        }
    }

    public boolean isAccountsShowed() {
        return this.accountsShowed;
    }

    public void notifyDataSetChanged() {
        resetItems();
        super.notifyDataSetChanged();
    }

    public boolean isEnabled(ViewHolder holder) {
        int itemType = holder.getItemViewType();
        if (!(itemType == 3 || itemType == 4)) {
            if (itemType != 5) {
                return false;
            }
        }
        return true;
    }

    public void onBindViewHolder(ViewHolder holder, int position) {
        int itemViewType = holder.getItemViewType();
        if (itemViewType != 0) {
            switch (itemViewType) {
                case 3:
                    position -= 2;
                    if (this.accountsShowed) {
                        position -= getAccountRowsCount();
                    }
                    DrawerActionCell drawerActionCell = holder.itemView;
                    ((Item) this.items.get(position)).bind(drawerActionCell);
                    drawerActionCell.setPadding(0, 0, 0, 0);
                    return;
                case 4:
                    holder.itemView.setAccount(((Integer) this.accountNumbers.get(position - 2)).intValue());
                    return;
                default:
                    return;
            }
        }
        ((DrawerProfileCell) holder.itemView).setUser(MessagesController.getInstance(UserConfig.selectedAccount).getUser(Integer.valueOf(UserConfig.getInstance(UserConfig.selectedAccount).getClientUserId())), this.accountsShowed);
        holder.itemView.setBackgroundColor(Theme.getColor(Theme.key_avatar_backgroundActionBarBlue));
    }

    public int getItemViewType(int i) {
        if (i == 0) {
            return 0;
        }
        if (i == 1) {
            return 1;
        }
        i -= 2;
        if (this.accountsShowed) {
            if (i < this.accountNumbers.size()) {
                return 4;
            }
            if (this.accountNumbers.size() < 3) {
                if (i == this.accountNumbers.size()) {
                    return 5;
                }
                if (i == this.accountNumbers.size() + 1) {
                    return 2;
                }
            } else if (i == this.accountNumbers.size()) {
                return 2;
            }
            i -= getAccountRowsCount();
        }
        return i == 3 ? 2 : 3;
    }

    private void resetItems() {
        this.accountNumbers.clear();
        for (int a = 0; a < 3; a++) {
            if (UserConfig.getInstance(a).isClientActivated()) {
                this.accountNumbers.add(Integer.valueOf(a));
            }
        }
        Collections.sort(this.accountNumbers, new Comparator<Integer>() {
            public int compare(Integer o1, Integer o2) {
                long l1 = (long) UserConfig.getInstance(o1.intValue()).loginTime;
                long l2 = (long) UserConfig.getInstance(o2.intValue()).loginTime;
                if (l1 > l2) {
                    return 1;
                }
                if (l1 < l2) {
                    return -1;
                }
                return 0;
            }
        });
        this.items.clear();
        if (UserConfig.getInstance(UserConfig.selectedAccount).isClientActivated()) {
            this.items.add(new Item(2, LocaleController.getString("NewGroup", R.string.NewGroup), R.drawable.menu_newgroup));
            this.items.add(new Item(3, LocaleController.getString("NewSecretChat", R.string.NewSecretChat), R.drawable.menu_secret));
            this.items.add(new Item(4, LocaleController.getString("NewChannel", R.string.NewChannel), R.drawable.menu_broadcast));
            this.items.add(null);
            this.items.add(new Item(6, LocaleController.getString("Contacts", R.string.Contacts), R.drawable.menu_contacts));
            this.items.add(new Item(11, LocaleController.getString("SavedMessages", R.string.SavedMessages), R.drawable.menu_saved));
            this.items.add(new Item(10, LocaleController.getString("Calls", R.string.Calls), R.drawable.menu_calls));
            this.items.add(new Item(7, LocaleController.getString("InviteFriends", R.string.InviteFriends), R.drawable.menu_invite));
            this.items.add(new Item(8, LocaleController.getString("Settings", R.string.Settings), R.drawable.menu_settings));
            this.items.add(new Item(9, LocaleController.getString("TelegramFAQ", R.string.TelegramFAQ), R.drawable.menu_help));
        }
    }

    public int getId(int position) {
        position -= 2;
        if (this.accountsShowed) {
            position -= getAccountRowsCount();
        }
        int i = -1;
        if (position >= 0) {
            if (position < this.items.size()) {
                Item item = (Item) this.items.get(position);
                if (item != null) {
                    i = item.id;
                }
                return i;
            }
        }
        return -1;
    }
}
