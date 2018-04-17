package org.telegram.ui;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Timer;
import java.util.TimerTask;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.ContactsController;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.NotificationCenter;
import org.telegram.messenger.NotificationCenter.NotificationCenterDelegate;
import org.telegram.messenger.UserConfig;
import org.telegram.messenger.Utilities;
import org.telegram.messenger.beta.R;
import org.telegram.messenger.support.widget.LinearLayoutManager;
import org.telegram.messenger.support.widget.RecyclerView.ViewHolder;
import org.telegram.tgnet.TLRPC.Chat;
import org.telegram.tgnet.TLRPC.ChatFull;
import org.telegram.tgnet.TLRPC.ChatParticipant;
import org.telegram.tgnet.TLRPC.TL_chatParticipant;
import org.telegram.tgnet.TLRPC.TL_chatParticipantAdmin;
import org.telegram.tgnet.TLRPC.TL_chatParticipantCreator;
import org.telegram.tgnet.TLRPC.User;
import org.telegram.ui.ActionBar.ActionBar.ActionBarMenuOnItemClick;
import org.telegram.ui.ActionBar.ActionBarMenuItem;
import org.telegram.ui.ActionBar.ActionBarMenuItem.ActionBarMenuItemSearchListener;
import org.telegram.ui.ActionBar.BaseFragment;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.ActionBar.ThemeDescription;
import org.telegram.ui.ActionBar.ThemeDescription.ThemeDescriptionDelegate;
import org.telegram.ui.Cells.TextCheckCell;
import org.telegram.ui.Cells.TextInfoPrivacyCell;
import org.telegram.ui.Cells.UserCell;
import org.telegram.ui.Components.EmptyTextProgressView;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.ui.Components.RecyclerListView;
import org.telegram.ui.Components.RecyclerListView.Holder;
import org.telegram.ui.Components.RecyclerListView.OnItemClickListener;
import org.telegram.ui.Components.RecyclerListView.SelectionAdapter;

public class SetAdminsActivity extends BaseFragment implements NotificationCenterDelegate {
    private int allAdminsInfoRow;
    private int allAdminsRow;
    private Chat chat;
    private int chat_id;
    private EmptyTextProgressView emptyView;
    private ChatFull info;
    private ListAdapter listAdapter;
    private RecyclerListView listView;
    private ArrayList<ChatParticipant> participants = new ArrayList();
    private int rowCount;
    private SearchAdapter searchAdapter;
    private ActionBarMenuItem searchItem;
    private boolean searchWas;
    private boolean searching;
    private int usersEndRow;
    private int usersStartRow;

    private class ListAdapter extends SelectionAdapter {
        private Context mContext;

        public ListAdapter(Context context) {
            this.mContext = context;
        }

        public boolean isEnabled(ViewHolder holder) {
            int position = holder.getAdapterPosition();
            if (position == SetAdminsActivity.this.allAdminsRow) {
                return true;
            }
            if (position < SetAdminsActivity.this.usersStartRow || position >= SetAdminsActivity.this.usersEndRow || (((ChatParticipant) SetAdminsActivity.this.participants.get(position - SetAdminsActivity.this.usersStartRow)) instanceof TL_chatParticipantCreator)) {
                return false;
            }
            return true;
        }

        public int getItemCount() {
            return SetAdminsActivity.this.rowCount;
        }

        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view;
            switch (viewType) {
                case 0:
                    view = new TextCheckCell(this.mContext);
                    view.setBackgroundColor(Theme.getColor(Theme.key_windowBackgroundWhite));
                    break;
                case 1:
                    view = new TextInfoPrivacyCell(this.mContext);
                    break;
                default:
                    view = new UserCell(this.mContext, 1, 2, false);
                    view.setBackgroundColor(Theme.getColor(Theme.key_windowBackgroundWhite));
                    break;
            }
            return new Holder(view);
        }

        public void onBindViewHolder(ViewHolder holder, int position) {
            boolean z = true;
            switch (holder.getItemViewType()) {
                case 0:
                    TextCheckCell checkCell = holder.itemView;
                    SetAdminsActivity.this.chat = MessagesController.getInstance(SetAdminsActivity.this.currentAccount).getChat(Integer.valueOf(SetAdminsActivity.this.chat_id));
                    String string = LocaleController.getString("SetAdminsAll", R.string.SetAdminsAll);
                    if (SetAdminsActivity.this.chat == null || SetAdminsActivity.this.chat.admins_enabled) {
                        z = false;
                    }
                    checkCell.setTextAndCheck(string, z, false);
                    return;
                case 1:
                    TextInfoPrivacyCell privacyCell = holder.itemView;
                    if (position == SetAdminsActivity.this.allAdminsInfoRow) {
                        if (SetAdminsActivity.this.chat.admins_enabled) {
                            privacyCell.setText(LocaleController.getString("SetAdminsNotAllInfo", R.string.SetAdminsNotAllInfo));
                        } else {
                            privacyCell.setText(LocaleController.getString("SetAdminsAllInfo", R.string.SetAdminsAllInfo));
                        }
                        if (SetAdminsActivity.this.usersStartRow != -1) {
                            privacyCell.setBackgroundDrawable(Theme.getThemedDrawable(this.mContext, R.drawable.greydivider, Theme.key_windowBackgroundGrayShadow));
                            return;
                        } else {
                            privacyCell.setBackgroundDrawable(Theme.getThemedDrawable(this.mContext, R.drawable.greydivider_bottom, Theme.key_windowBackgroundGrayShadow));
                            return;
                        }
                    } else if (position == SetAdminsActivity.this.usersEndRow) {
                        privacyCell.setText(TtmlNode.ANONYMOUS_REGION_ID);
                        privacyCell.setBackgroundDrawable(Theme.getThemedDrawable(this.mContext, R.drawable.greydivider_bottom, Theme.key_windowBackgroundGrayShadow));
                        return;
                    } else {
                        return;
                    }
                case 2:
                    boolean z2;
                    UserCell userCell = holder.itemView;
                    ChatParticipant part = (ChatParticipant) SetAdminsActivity.this.participants.get(position - SetAdminsActivity.this.usersStartRow);
                    userCell.setData(MessagesController.getInstance(SetAdminsActivity.this.currentAccount).getUser(Integer.valueOf(part.user_id)), null, null, 0);
                    SetAdminsActivity.this.chat = MessagesController.getInstance(SetAdminsActivity.this.currentAccount).getChat(Integer.valueOf(SetAdminsActivity.this.chat_id));
                    if (part instanceof TL_chatParticipant) {
                        if (SetAdminsActivity.this.chat == null || SetAdminsActivity.this.chat.admins_enabled) {
                            z2 = false;
                            userCell.setChecked(z2, false);
                            if (SetAdminsActivity.this.chat != null && SetAdminsActivity.this.chat.admins_enabled) {
                                if (part.user_id == UserConfig.getInstance(SetAdminsActivity.this.currentAccount).getClientUserId()) {
                                    z = false;
                                }
                            }
                            userCell.setCheckDisabled(z);
                            return;
                        }
                    }
                    z2 = true;
                    userCell.setChecked(z2, false);
                    if (part.user_id == UserConfig.getInstance(SetAdminsActivity.this.currentAccount).getClientUserId()) {
                        z = false;
                    }
                    userCell.setCheckDisabled(z);
                    return;
                default:
                    return;
            }
        }

        public int getItemViewType(int i) {
            if (i == SetAdminsActivity.this.allAdminsRow) {
                return 0;
            }
            if (i != SetAdminsActivity.this.allAdminsInfoRow) {
                if (i != SetAdminsActivity.this.usersEndRow) {
                    return 2;
                }
            }
            return 1;
        }
    }

    public class SearchAdapter extends SelectionAdapter {
        private Context mContext;
        private ArrayList<ChatParticipant> searchResult = new ArrayList();
        private ArrayList<CharSequence> searchResultNames = new ArrayList();
        private Timer searchTimer;

        public SearchAdapter(Context context) {
            this.mContext = context;
        }

        public void search(final String query) {
            try {
                if (this.searchTimer != null) {
                    this.searchTimer.cancel();
                }
            } catch (Throwable e) {
                FileLog.e(e);
            }
            if (query == null) {
                this.searchResult.clear();
                this.searchResultNames.clear();
                notifyDataSetChanged();
                return;
            }
            this.searchTimer = new Timer();
            this.searchTimer.schedule(new TimerTask() {
                public void run() {
                    try {
                        SearchAdapter.this.searchTimer.cancel();
                        SearchAdapter.this.searchTimer = null;
                    } catch (Throwable e) {
                        FileLog.e(e);
                    }
                    SearchAdapter.this.processSearch(query);
                }
            }, 200, 300);
        }

        private void processSearch(final String query) {
            AndroidUtilities.runOnUIThread(new Runnable() {
                public void run() {
                    final ArrayList<ChatParticipant> contactsCopy = new ArrayList();
                    contactsCopy.addAll(SetAdminsActivity.this.participants);
                    Utilities.searchQueue.postRunnable(new Runnable() {
                        public void run() {
                            String search1 = query.trim().toLowerCase();
                            if (search1.length() == 0) {
                                SearchAdapter.this.updateSearchResults(new ArrayList(), new ArrayList());
                                return;
                            }
                            String search2 = LocaleController.getInstance().getTranslitString(search1);
                            if (search1.equals(search2) || search2.length() == 0) {
                                search2 = null;
                            }
                            int i = 0;
                            String[] search = new String[((search2 != null ? 1 : 0) + 1)];
                            search[0] = search1;
                            if (search2 != null) {
                                search[1] = search2;
                            }
                            ArrayList<ChatParticipant> resultArray = new ArrayList();
                            ArrayList<CharSequence> resultArrayNames = new ArrayList();
                            int a = 0;
                            while (a < contactsCopy.size()) {
                                String search12;
                                ChatParticipant participant = (ChatParticipant) contactsCopy.get(a);
                                User user = MessagesController.getInstance(SetAdminsActivity.this.currentAccount).getUser(Integer.valueOf(participant.user_id));
                                if (user.id == UserConfig.getInstance(SetAdminsActivity.this.currentAccount).getClientUserId()) {
                                    search12 = search1;
                                } else {
                                    String name = ContactsController.formatName(user.first_name, user.last_name).toLowerCase();
                                    String tName = LocaleController.getInstance().getTranslitString(name);
                                    if (name.equals(tName)) {
                                        tName = null;
                                    }
                                    int length = search.length;
                                    int found = 0;
                                    int found2 = i;
                                    while (found2 < length) {
                                        StringBuilder stringBuilder;
                                        String stringBuilder2;
                                        StringBuilder stringBuilder3;
                                        String q = search[found2];
                                        if (name.startsWith(q)) {
                                            search12 = search1;
                                        } else {
                                            stringBuilder = new StringBuilder();
                                            search12 = search1;
                                            stringBuilder.append(" ");
                                            stringBuilder.append(q);
                                            if (name.contains(stringBuilder.toString()) == null) {
                                                if (tName != null) {
                                                    if (tName.startsWith(q) == null) {
                                                        search1 = new StringBuilder();
                                                        search1.append(" ");
                                                        search1.append(q);
                                                        if (tName.contains(search1.toString()) != null) {
                                                        }
                                                    }
                                                }
                                                if (!(user.username == null || user.username.startsWith(q) == null)) {
                                                    search1 = 2;
                                                    found = search1;
                                                }
                                                if (found == 0) {
                                                    if (found != 1) {
                                                        resultArrayNames.add(AndroidUtilities.generateSearchName(user.first_name, user.last_name, q));
                                                    } else {
                                                        stringBuilder = new StringBuilder();
                                                        stringBuilder.append("@");
                                                        stringBuilder.append(user.username);
                                                        stringBuilder2 = stringBuilder.toString();
                                                        stringBuilder3 = new StringBuilder();
                                                        stringBuilder3.append("@");
                                                        stringBuilder3.append(q);
                                                        resultArrayNames.add(AndroidUtilities.generateSearchName(stringBuilder2, null, stringBuilder3.toString()));
                                                    }
                                                    resultArray.add(participant);
                                                } else {
                                                    found2++;
                                                    search1 = search12;
                                                }
                                            }
                                        }
                                        search1 = true;
                                        found = search1;
                                        if (found == 0) {
                                            found2++;
                                            search1 = search12;
                                        } else {
                                            if (found != 1) {
                                                stringBuilder = new StringBuilder();
                                                stringBuilder.append("@");
                                                stringBuilder.append(user.username);
                                                stringBuilder2 = stringBuilder.toString();
                                                stringBuilder3 = new StringBuilder();
                                                stringBuilder3.append("@");
                                                stringBuilder3.append(q);
                                                resultArrayNames.add(AndroidUtilities.generateSearchName(stringBuilder2, null, stringBuilder3.toString()));
                                            } else {
                                                resultArrayNames.add(AndroidUtilities.generateSearchName(user.first_name, user.last_name, q));
                                            }
                                            resultArray.add(participant);
                                        }
                                    }
                                    search12 = search1;
                                }
                                a++;
                                search1 = search12;
                                i = 0;
                            }
                            SearchAdapter.this.updateSearchResults(resultArray, resultArrayNames);
                        }
                    });
                }
            });
        }

        private void updateSearchResults(final ArrayList<ChatParticipant> users, final ArrayList<CharSequence> names) {
            AndroidUtilities.runOnUIThread(new Runnable() {
                public void run() {
                    SearchAdapter.this.searchResult = users;
                    SearchAdapter.this.searchResultNames = names;
                    SearchAdapter.this.notifyDataSetChanged();
                }
            });
        }

        public boolean isEnabled(ViewHolder holder) {
            return true;
        }

        public int getItemCount() {
            return this.searchResult.size();
        }

        public ChatParticipant getItem(int i) {
            return (ChatParticipant) this.searchResult.get(i);
        }

        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new Holder(new UserCell(this.mContext, 1, 2, false));
        }

        public void onBindViewHolder(ViewHolder holder, int position) {
            boolean z;
            ChatParticipant participant = getItem(position);
            User user = MessagesController.getInstance(SetAdminsActivity.this.currentAccount).getUser(Integer.valueOf(participant.user_id));
            String un = user.username;
            CharSequence username = null;
            CharSequence name = null;
            if (position < this.searchResult.size()) {
                name = (CharSequence) this.searchResultNames.get(position);
                if (!(name == null || un == null || un.length() <= 0)) {
                    String charSequence = name.toString();
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("@");
                    stringBuilder.append(un);
                    if (charSequence.startsWith(stringBuilder.toString())) {
                        username = name;
                        name = null;
                    }
                }
            }
            UserCell userCell = holder.itemView;
            boolean z2 = false;
            userCell.setData(user, name, username, 0);
            SetAdminsActivity.this.chat = MessagesController.getInstance(SetAdminsActivity.this.currentAccount).getChat(Integer.valueOf(SetAdminsActivity.this.chat_id));
            if (participant instanceof TL_chatParticipant) {
                if (SetAdminsActivity.this.chat == null || SetAdminsActivity.this.chat.admins_enabled) {
                    z = false;
                    userCell.setChecked(z, false);
                    if (SetAdminsActivity.this.chat != null && SetAdminsActivity.this.chat.admins_enabled) {
                        if (participant.user_id == UserConfig.getInstance(SetAdminsActivity.this.currentAccount).getClientUserId()) {
                            userCell.setCheckDisabled(z2);
                        }
                    }
                    z2 = true;
                    userCell.setCheckDisabled(z2);
                }
            }
            z = true;
            userCell.setChecked(z, false);
            if (participant.user_id == UserConfig.getInstance(SetAdminsActivity.this.currentAccount).getClientUserId()) {
                userCell.setCheckDisabled(z2);
            }
            z2 = true;
            userCell.setCheckDisabled(z2);
        }

        public int getItemViewType(int i) {
            return 0;
        }
    }

    public SetAdminsActivity(Bundle args) {
        super(args);
        this.chat_id = args.getInt("chat_id");
    }

    public boolean onFragmentCreate() {
        super.onFragmentCreate();
        NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.chatInfoDidLoaded);
        NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.updateInterfaces);
        return true;
    }

    public void onFragmentDestroy() {
        super.onFragmentDestroy();
        NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.chatInfoDidLoaded);
        NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.updateInterfaces);
    }

    public View createView(Context context) {
        this.searching = false;
        this.searchWas = false;
        this.actionBar.setBackButtonImage(R.drawable.ic_ab_back);
        this.actionBar.setAllowOverlayTitle(true);
        this.actionBar.setTitle(LocaleController.getString("SetAdminsTitle", R.string.SetAdminsTitle));
        this.actionBar.setActionBarMenuOnItemClick(new ActionBarMenuOnItemClick() {
            public void onItemClick(int id) {
                if (id == -1) {
                    SetAdminsActivity.this.finishFragment();
                }
            }
        });
        this.searchItem = this.actionBar.createMenu().addItem(0, (int) R.drawable.ic_ab_search).setIsSearchField(true).setActionBarMenuItemSearchListener(new ActionBarMenuItemSearchListener() {
            public void onSearchExpand() {
                SetAdminsActivity.this.searching = true;
                SetAdminsActivity.this.listView.setEmptyView(SetAdminsActivity.this.emptyView);
            }

            public void onSearchCollapse() {
                SetAdminsActivity.this.searching = false;
                SetAdminsActivity.this.searchWas = false;
                if (SetAdminsActivity.this.listView != null) {
                    SetAdminsActivity.this.listView.setEmptyView(null);
                    SetAdminsActivity.this.emptyView.setVisibility(8);
                    if (SetAdminsActivity.this.listView.getAdapter() != SetAdminsActivity.this.listAdapter) {
                        SetAdminsActivity.this.listView.setAdapter(SetAdminsActivity.this.listAdapter);
                    }
                }
                if (SetAdminsActivity.this.searchAdapter != null) {
                    SetAdminsActivity.this.searchAdapter.search(null);
                }
            }

            public void onTextChanged(EditText editText) {
                String text = editText.getText().toString();
                if (text.length() != 0) {
                    SetAdminsActivity.this.searchWas = true;
                    if (!(SetAdminsActivity.this.searchAdapter == null || SetAdminsActivity.this.listView.getAdapter() == SetAdminsActivity.this.searchAdapter)) {
                        SetAdminsActivity.this.listView.setAdapter(SetAdminsActivity.this.searchAdapter);
                        SetAdminsActivity.this.fragmentView.setBackgroundColor(Theme.getColor(Theme.key_windowBackgroundWhite));
                    }
                    if (!(SetAdminsActivity.this.emptyView == null || SetAdminsActivity.this.listView.getEmptyView() == SetAdminsActivity.this.emptyView)) {
                        SetAdminsActivity.this.emptyView.showTextView();
                        SetAdminsActivity.this.listView.setEmptyView(SetAdminsActivity.this.emptyView);
                    }
                }
                if (SetAdminsActivity.this.searchAdapter != null) {
                    SetAdminsActivity.this.searchAdapter.search(text);
                }
            }
        });
        this.searchItem.getSearchField().setHint(LocaleController.getString("Search", R.string.Search));
        this.listAdapter = new ListAdapter(context);
        this.searchAdapter = new SearchAdapter(context);
        this.fragmentView = new FrameLayout(context);
        FrameLayout frameLayout = this.fragmentView;
        this.fragmentView.setBackgroundColor(Theme.getColor(Theme.key_windowBackgroundGray));
        this.listView = new RecyclerListView(context);
        this.listView.setLayoutManager(new LinearLayoutManager(context, 1, false));
        this.listView.setVerticalScrollBarEnabled(false);
        frameLayout.addView(this.listView, LayoutHelper.createFrame(-1, -1.0f));
        this.listView.setAdapter(this.listAdapter);
        this.listView.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(android.view.View r1, int r2) {
                /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.DecodeException: Load method exception in method: org.telegram.ui.SetAdminsActivity.3.onItemClick(android.view.View, int):void
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
                r0 = org.telegram.ui.SetAdminsActivity.this;
                r0 = r0.listView;
                r0 = r0.getAdapter();
                r1 = org.telegram.ui.SetAdminsActivity.this;
                r1 = r1.searchAdapter;
                r2 = 1;
                if (r0 == r1) goto L_0x008e;
            L_0x0013:
                r0 = org.telegram.ui.SetAdminsActivity.this;
                r0 = r0.usersStartRow;
                if (r12 < r0) goto L_0x0024;
            L_0x001b:
                r0 = org.telegram.ui.SetAdminsActivity.this;
                r0 = r0.usersEndRow;
                if (r12 >= r0) goto L_0x0024;
            L_0x0023:
                goto L_0x008e;
            L_0x0024:
                r0 = org.telegram.ui.SetAdminsActivity.this;
                r0 = r0.allAdminsRow;
                if (r12 != r0) goto L_0x01c4;
            L_0x002c:
                r0 = org.telegram.ui.SetAdminsActivity.this;
                r1 = org.telegram.ui.SetAdminsActivity.this;
                r1 = r1.currentAccount;
                r1 = org.telegram.messenger.MessagesController.getInstance(r1);
                r3 = org.telegram.ui.SetAdminsActivity.this;
                r3 = r3.chat_id;
                r3 = java.lang.Integer.valueOf(r3);
                r1 = r1.getChat(r3);
                r0.chat = r1;
                r0 = org.telegram.ui.SetAdminsActivity.this;
                r0 = r0.chat;
                if (r0 == 0) goto L_0x01c4;
            L_0x0051:
                r0 = org.telegram.ui.SetAdminsActivity.this;
                r0 = r0.chat;
                r1 = org.telegram.ui.SetAdminsActivity.this;
                r1 = r1.chat;
                r1 = r1.admins_enabled;
                r1 = r1 ^ r2;
                r0.admins_enabled = r1;
                r0 = r11;
                r0 = (org.telegram.ui.Cells.TextCheckCell) r0;
                r1 = org.telegram.ui.SetAdminsActivity.this;
                r1 = r1.chat;
                r1 = r1.admins_enabled;
                r1 = r1 ^ r2;
                r0.setChecked(r1);
                r0 = org.telegram.ui.SetAdminsActivity.this;
                r0 = r0.currentAccount;
                r0 = org.telegram.messenger.MessagesController.getInstance(r0);
                r1 = org.telegram.ui.SetAdminsActivity.this;
                r1 = r1.chat_id;
                r2 = org.telegram.ui.SetAdminsActivity.this;
                r2 = r2.chat;
                r2 = r2.admins_enabled;
                r0.toggleAdminMode(r1, r2);
                goto L_0x01c4;
            L_0x008e:
                r0 = r11;
                r0 = (org.telegram.ui.Cells.UserCell) r0;
                r1 = org.telegram.ui.SetAdminsActivity.this;
                r3 = org.telegram.ui.SetAdminsActivity.this;
                r3 = r3.currentAccount;
                r3 = org.telegram.messenger.MessagesController.getInstance(r3);
                r4 = org.telegram.ui.SetAdminsActivity.this;
                r4 = r4.chat_id;
                r4 = java.lang.Integer.valueOf(r4);
                r3 = r3.getChat(r4);
                r1.chat = r3;
                r1 = -1;
                r3 = org.telegram.ui.SetAdminsActivity.this;
                r3 = r3.listView;
                r3 = r3.getAdapter();
                r4 = org.telegram.ui.SetAdminsActivity.this;
                r4 = r4.searchAdapter;
                r5 = 0;
                if (r3 != r4) goto L_0x00f0;
            L_0x00c2:
                r3 = org.telegram.ui.SetAdminsActivity.this;
                r3 = r3.searchAdapter;
                r3 = r3.getItem(r12);
                r4 = r5;
            L_0x00cd:
                r6 = org.telegram.ui.SetAdminsActivity.this;
                r6 = r6.participants;
                r6 = r6.size();
                if (r4 >= r6) goto L_0x0105;
            L_0x00d9:
                r6 = org.telegram.ui.SetAdminsActivity.this;
                r6 = r6.participants;
                r6 = r6.get(r4);
                r6 = (org.telegram.tgnet.TLRPC.ChatParticipant) r6;
                r7 = r6.user_id;
                r8 = r3.user_id;
                if (r7 != r8) goto L_0x00ed;
            L_0x00eb:
                r1 = r4;
                goto L_0x0105;
            L_0x00ed:
                r4 = r4 + 1;
                goto L_0x00cd;
            L_0x00f0:
                r3 = org.telegram.ui.SetAdminsActivity.this;
                r3 = r3.participants;
                r4 = org.telegram.ui.SetAdminsActivity.this;
                r4 = r4.usersStartRow;
                r4 = r12 - r4;
                r1 = r4;
                r3 = r3.get(r4);
                r3 = (org.telegram.tgnet.TLRPC.ChatParticipant) r3;
            L_0x0105:
                r4 = -1;
                if (r1 == r4) goto L_0x01c3;
            L_0x0108:
                r6 = r3 instanceof org.telegram.tgnet.TLRPC.TL_chatParticipantCreator;
                if (r6 != 0) goto L_0x01c3;
                r6 = r3 instanceof org.telegram.tgnet.TLRPC.TL_chatParticipant;
                if (r6 == 0) goto L_0x0122;
                r6 = new org.telegram.tgnet.TLRPC$TL_chatParticipantAdmin;
                r6.<init>();
                r7 = r3.user_id;
                r6.user_id = r7;
                r7 = r3.date;
                r6.date = r7;
                r7 = r3.inviter_id;
                r6.inviter_id = r7;
                goto L_0x0133;
                r6 = new org.telegram.tgnet.TLRPC$TL_chatParticipant;
                r6.<init>();
                r7 = r3.user_id;
                r6.user_id = r7;
                r7 = r3.date;
                r6.date = r7;
                r7 = r3.inviter_id;
                r6.inviter_id = r7;
                r7 = org.telegram.ui.SetAdminsActivity.this;
                r7 = r7.participants;
                r7.set(r1, r6);
                r7 = org.telegram.ui.SetAdminsActivity.this;
                r7 = r7.info;
                r7 = r7.participants;
                r7 = r7.participants;
                r1 = r7.indexOf(r3);
                if (r1 == r4) goto L_0x0159;
                r4 = org.telegram.ui.SetAdminsActivity.this;
                r4 = r4.info;
                r4 = r4.participants;
                r4 = r4.participants;
                r4.set(r1, r6);
                r4 = org.telegram.ui.SetAdminsActivity.this;
                r4 = r4.listView;
                r4 = r4.getAdapter();
                r7 = org.telegram.ui.SetAdminsActivity.this;
                r7 = r7.searchAdapter;
                if (r4 != r7) goto L_0x0178;
                r4 = org.telegram.ui.SetAdminsActivity.this;
                r4 = r4.searchAdapter;
                r4 = r4.searchResult;
                r4.set(r12, r6);
                r3 = r6;
                r4 = r3 instanceof org.telegram.tgnet.TLRPC.TL_chatParticipant;
                if (r4 == 0) goto L_0x0192;
                r4 = org.telegram.ui.SetAdminsActivity.this;
                r4 = r4.chat;
                if (r4 == 0) goto L_0x0190;
                r4 = org.telegram.ui.SetAdminsActivity.this;
                r4 = r4.chat;
                r4 = r4.admins_enabled;
                if (r4 != 0) goto L_0x0190;
                goto L_0x0192;
                r4 = r5;
                goto L_0x0193;
                r4 = r2;
                r0.setChecked(r4, r2);
                r4 = org.telegram.ui.SetAdminsActivity.this;
                r4 = r4.chat;
                if (r4 == 0) goto L_0x01c3;
                r4 = org.telegram.ui.SetAdminsActivity.this;
                r4 = r4.chat;
                r4 = r4.admins_enabled;
                if (r4 == 0) goto L_0x01c3;
                r4 = org.telegram.ui.SetAdminsActivity.this;
                r4 = r4.currentAccount;
                r4 = org.telegram.messenger.MessagesController.getInstance(r4);
                r7 = org.telegram.ui.SetAdminsActivity.this;
                r7 = r7.chat_id;
                r8 = r3.user_id;
                r9 = r3 instanceof org.telegram.tgnet.TLRPC.TL_chatParticipant;
                if (r9 != 0) goto L_0x01bf;
                goto L_0x01c0;
                r2 = r5;
                r4.toggleUserAdmin(r7, r8, r2);
            L_0x01c4:
                return;
                */
                throw new UnsupportedOperationException("Method not decompiled: org.telegram.ui.SetAdminsActivity.3.onItemClick(android.view.View, int):void");
            }
        });
        this.emptyView = new EmptyTextProgressView(context);
        this.emptyView.setVisibility(8);
        this.emptyView.setShowAtCenter(true);
        this.emptyView.setText(LocaleController.getString("NoResult", R.string.NoResult));
        frameLayout.addView(this.emptyView, LayoutHelper.createFrame(-1, -1.0f));
        this.emptyView.showTextView();
        updateRowsIds();
        return this.fragmentView;
    }

    public void didReceivedNotification(int id, int account, Object... args) {
        int a = 0;
        if (id == NotificationCenter.chatInfoDidLoaded) {
            ChatFull chatFull = args[0];
            if (chatFull.id == this.chat_id) {
                this.info = chatFull;
                updateChatParticipants();
                updateRowsIds();
            }
        } else if (id == NotificationCenter.updateInterfaces) {
            int mask = ((Integer) args[0]).intValue();
            if (((mask & 2) != 0 || (mask & 1) != 0 || (mask & 4) != 0) && this.listView != null) {
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
    }

    public void onResume() {
        super.onResume();
        if (this.listAdapter != null) {
            this.listAdapter.notifyDataSetChanged();
        }
    }

    public void setChatInfo(ChatFull chatParticipants) {
        this.info = chatParticipants;
        updateChatParticipants();
    }

    private int getChatAdminParticipantType(ChatParticipant participant) {
        if (participant instanceof TL_chatParticipantCreator) {
            return 0;
        }
        if (participant instanceof TL_chatParticipantAdmin) {
            return 1;
        }
        return 2;
    }

    private void updateChatParticipants() {
        if (!(this.info == null || this.participants.size() == this.info.participants.participants.size())) {
            this.participants.clear();
            this.participants.addAll(this.info.participants.participants);
            try {
                Collections.sort(this.participants, new Comparator<ChatParticipant>() {
                    public int compare(ChatParticipant lhs, ChatParticipant rhs) {
                        int type1 = SetAdminsActivity.this.getChatAdminParticipantType(lhs);
                        int type2 = SetAdminsActivity.this.getChatAdminParticipantType(rhs);
                        if (type1 > type2) {
                            return 1;
                        }
                        if (type1 < type2) {
                            return -1;
                        }
                        if (type1 == type2) {
                            User user1 = MessagesController.getInstance(SetAdminsActivity.this.currentAccount).getUser(Integer.valueOf(rhs.user_id));
                            User user2 = MessagesController.getInstance(SetAdminsActivity.this.currentAccount).getUser(Integer.valueOf(lhs.user_id));
                            int status1 = 0;
                            int status2 = 0;
                            if (!(user1 == null || user1.status == null)) {
                                status1 = user1.status.expires;
                            }
                            if (!(user2 == null || user2.status == null)) {
                                status2 = user2.status.expires;
                            }
                            if (status1 <= 0 || status2 <= 0) {
                                if (status1 >= 0 || status2 >= 0) {
                                    if ((status1 < 0 && status2 > 0) || (status1 == 0 && status2 != 0)) {
                                        return -1;
                                    }
                                    if ((status2 >= 0 || status1 <= 0) && (status2 != 0 || status1 == 0)) {
                                        return 0;
                                    }
                                    return 1;
                                } else if (status1 > status2) {
                                    return 1;
                                } else {
                                    if (status1 < status2) {
                                        return -1;
                                    }
                                    return 0;
                                }
                            } else if (status1 > status2) {
                                return 1;
                            } else {
                                if (status1 < status2) {
                                    return -1;
                                }
                                return 0;
                            }
                        }
                        return 0;
                    }
                });
            } catch (Throwable e) {
                FileLog.e(e);
            }
        }
    }

    private void updateRowsIds() {
        this.rowCount = 0;
        int i = this.rowCount;
        this.rowCount = i + 1;
        this.allAdminsRow = i;
        i = this.rowCount;
        this.rowCount = i + 1;
        this.allAdminsInfoRow = i;
        if (this.info != null) {
            this.usersStartRow = this.rowCount;
            this.rowCount += this.participants.size();
            i = this.rowCount;
            this.rowCount = i + 1;
            this.usersEndRow = i;
            if (!(this.searchItem == null || this.searchWas)) {
                this.searchItem.setVisibility(0);
            }
        } else {
            this.usersStartRow = -1;
            this.usersEndRow = -1;
            if (this.searchItem != null) {
                this.searchItem.setVisibility(8);
            }
        }
        if (this.listAdapter != null) {
            this.listAdapter.notifyDataSetChanged();
        }
    }

    public ThemeDescription[] getThemeDescriptions() {
        ThemeDescriptionDelegate сellDelegate = new ThemeDescriptionDelegate() {
            public void didSetColor() {
                if (SetAdminsActivity.this.listView != null) {
                    int count = SetAdminsActivity.this.listView.getChildCount();
                    for (int a = 0; a < count; a++) {
                        View child = SetAdminsActivity.this.listView.getChildAt(a);
                        if (child instanceof UserCell) {
                            ((UserCell) child).update(0);
                        }
                    }
                }
            }
        };
        r10 = new ThemeDescription[34];
        r10[0] = new ThemeDescription(this.listView, ThemeDescription.FLAG_CELLBACKGROUNDCOLOR, new Class[]{TextCheckCell.class, UserCell.class}, null, null, null, Theme.key_windowBackgroundWhite);
        r10[1] = new ThemeDescription(this.fragmentView, ThemeDescription.FLAG_BACKGROUND, null, null, null, null, Theme.key_windowBackgroundGray);
        r10[2] = new ThemeDescription(this.actionBar, ThemeDescription.FLAG_BACKGROUND, null, null, null, null, Theme.key_actionBarDefault);
        r10[3] = new ThemeDescription(this.listView, ThemeDescription.FLAG_LISTGLOWCOLOR, null, null, null, null, Theme.key_actionBarDefault);
        r10[4] = new ThemeDescription(this.actionBar, ThemeDescription.FLAG_AB_ITEMSCOLOR, null, null, null, null, Theme.key_actionBarDefaultIcon);
        r10[5] = new ThemeDescription(this.actionBar, ThemeDescription.FLAG_AB_TITLECOLOR, null, null, null, null, Theme.key_actionBarDefaultTitle);
        r10[6] = new ThemeDescription(this.actionBar, ThemeDescription.FLAG_AB_SELECTORCOLOR, null, null, null, null, Theme.key_actionBarDefaultSelector);
        r10[7] = new ThemeDescription(this.actionBar, ThemeDescription.FLAG_AB_SEARCH, null, null, null, null, Theme.key_actionBarDefaultSearch);
        r10[8] = new ThemeDescription(this.actionBar, ThemeDescription.FLAG_AB_SEARCHPLACEHOLDER, null, null, null, null, Theme.key_actionBarDefaultSearchPlaceholder);
        r10[9] = new ThemeDescription(this.listView, ThemeDescription.FLAG_SELECTOR, null, null, null, null, Theme.key_listSelector);
        r10[10] = new ThemeDescription(this.listView, 0, new Class[]{View.class}, Theme.dividerPaint, null, null, Theme.key_divider);
        r10[11] = new ThemeDescription(this.emptyView, ThemeDescription.FLAG_TEXTCOLOR, null, null, null, null, Theme.key_emptyListPlaceholder);
        r10[12] = new ThemeDescription(this.listView, 0, new Class[]{TextCheckCell.class}, new String[]{"textView"}, null, null, null, Theme.key_windowBackgroundWhiteBlackText);
        r10[13] = new ThemeDescription(this.listView, 0, new Class[]{TextCheckCell.class}, new String[]{"checkBox"}, null, null, null, Theme.key_switchThumb);
        r10[14] = new ThemeDescription(this.listView, 0, new Class[]{TextCheckCell.class}, new String[]{"checkBox"}, null, null, null, Theme.key_switchTrack);
        r10[15] = new ThemeDescription(this.listView, 0, new Class[]{TextCheckCell.class}, new String[]{"checkBox"}, null, null, null, Theme.key_switchThumbChecked);
        r10[16] = new ThemeDescription(this.listView, 0, new Class[]{TextCheckCell.class}, new String[]{"checkBox"}, null, null, null, Theme.key_switchTrackChecked);
        View view = this.listView;
        View view2 = view;
        r10[17] = new ThemeDescription(view2, ThemeDescription.FLAG_BACKGROUNDFILTER, new Class[]{TextInfoPrivacyCell.class}, null, null, null, Theme.key_windowBackgroundGrayShadow);
        r10[18] = new ThemeDescription(this.listView, 0, new Class[]{TextInfoPrivacyCell.class}, new String[]{"textView"}, null, null, null, Theme.key_windowBackgroundWhiteGrayText4);
        r10[19] = new ThemeDescription(this.listView, 0, new Class[]{UserCell.class}, null, null, null, Theme.key_checkboxSquareUnchecked);
        r10[20] = new ThemeDescription(this.listView, 0, new Class[]{UserCell.class}, null, null, null, Theme.key_checkboxSquareDisabled);
        r10[21] = new ThemeDescription(this.listView, 0, new Class[]{UserCell.class}, null, null, null, Theme.key_checkboxSquareBackground);
        r10[22] = new ThemeDescription(this.listView, 0, new Class[]{UserCell.class}, null, null, null, Theme.key_checkboxSquareCheck);
        r10[23] = new ThemeDescription(this.listView, 0, new Class[]{UserCell.class}, new String[]{"nameTextView"}, null, null, null, Theme.key_windowBackgroundWhiteBlackText);
        int i = 1;
        int i2 = 2;
        r10[24] = new ThemeDescription(this.listView, 0, new Class[]{UserCell.class}, new String[]{"statusColor"}, null, null, сellDelegate, Theme.key_windowBackgroundWhiteGrayText);
        view = this.listView;
        Class[] clsArr = new Class[i];
        clsArr[0] = UserCell.class;
        String[] strArr = new String[i];
        strArr[0] = "statusOnlineColor";
        r10[25] = new ThemeDescription(view, 0, clsArr, strArr, null, null, сellDelegate, Theme.key_windowBackgroundWhiteBlueText);
        view = this.listView;
        clsArr = new Class[i];
        clsArr[0] = UserCell.class;
        r10[26] = new ThemeDescription(view, 0, clsArr, null, new Drawable[]{Theme.avatar_photoDrawable, Theme.avatar_broadcastDrawable, Theme.avatar_savedDrawable}, null, Theme.key_avatar_text);
        ThemeDescriptionDelegate themeDescriptionDelegate = сellDelegate;
        r10[27] = new ThemeDescription(null, 0, null, null, null, themeDescriptionDelegate, Theme.key_avatar_backgroundRed);
        r10[28] = new ThemeDescription(null, 0, null, null, null, themeDescriptionDelegate, Theme.key_avatar_backgroundOrange);
        r10[29] = new ThemeDescription(null, 0, null, null, null, themeDescriptionDelegate, Theme.key_avatar_backgroundViolet);
        r10[30] = new ThemeDescription(null, 0, null, null, null, themeDescriptionDelegate, Theme.key_avatar_backgroundGreen);
        r10[31] = new ThemeDescription(null, 0, null, null, null, themeDescriptionDelegate, Theme.key_avatar_backgroundCyan);
        r10[32] = new ThemeDescription(null, 0, null, null, null, themeDescriptionDelegate, Theme.key_avatar_backgroundBlue);
        r10[33] = new ThemeDescription(null, 0, null, null, null, themeDescriptionDelegate, Theme.key_avatar_backgroundPink);
        return r10;
    }
}
