package org.telegram.ui;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.ChatObject;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.NotificationCenter;
import org.telegram.messenger.NotificationCenter.NotificationCenterDelegate;
import org.telegram.messenger.beta.R;
import org.telegram.messenger.support.widget.LinearLayoutManager;
import org.telegram.messenger.support.widget.RecyclerView.ViewHolder;
import org.telegram.tgnet.ConnectionsManager;
import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC.Chat;
import org.telegram.tgnet.TLRPC.ChatFull;
import org.telegram.tgnet.TLRPC.ExportedChatInvite;
import org.telegram.tgnet.TLRPC.TL_channels_exportInvite;
import org.telegram.tgnet.TLRPC.TL_chatInviteExported;
import org.telegram.tgnet.TLRPC.TL_error;
import org.telegram.tgnet.TLRPC.TL_messages_exportChatInvite;
import org.telegram.ui.ActionBar.ActionBar.ActionBarMenuOnItemClick;
import org.telegram.ui.ActionBar.AlertDialog.Builder;
import org.telegram.ui.ActionBar.BaseFragment;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.ActionBar.ThemeDescription;
import org.telegram.ui.Cells.TextBlockCell;
import org.telegram.ui.Cells.TextInfoPrivacyCell;
import org.telegram.ui.Cells.TextSettingsCell;
import org.telegram.ui.Components.EmptyTextProgressView;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.ui.Components.RecyclerListView;
import org.telegram.ui.Components.RecyclerListView.Holder;
import org.telegram.ui.Components.RecyclerListView.OnItemClickListener;
import org.telegram.ui.Components.RecyclerListView.SelectionAdapter;

public class GroupInviteActivity extends BaseFragment implements NotificationCenterDelegate {
    private int chat_id;
    private int copyLinkRow;
    private EmptyTextProgressView emptyView;
    private ExportedChatInvite invite;
    private int linkInfoRow;
    private int linkRow;
    private ListAdapter listAdapter;
    private RecyclerListView listView;
    private boolean loading;
    private int revokeLinkRow;
    private int rowCount;
    private int shadowRow;
    private int shareLinkRow;

    private class ListAdapter extends SelectionAdapter {
        private Context mContext;

        public ListAdapter(Context context) {
            this.mContext = context;
        }

        public boolean isEnabled(ViewHolder holder) {
            int position = holder.getAdapterPosition();
            if (!(position == GroupInviteActivity.this.revokeLinkRow || position == GroupInviteActivity.this.copyLinkRow || position == GroupInviteActivity.this.shareLinkRow)) {
                if (position != GroupInviteActivity.this.linkRow) {
                    return false;
                }
            }
            return true;
        }

        public int getItemCount() {
            return GroupInviteActivity.this.loading ? 0 : GroupInviteActivity.this.rowCount;
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
                default:
                    view = new TextBlockCell(this.mContext);
                    view.setBackgroundColor(Theme.getColor(Theme.key_windowBackgroundWhite));
                    break;
            }
            return new Holder(view);
        }

        public void onBindViewHolder(ViewHolder holder, int position) {
            switch (holder.getItemViewType()) {
                case 0:
                    TextSettingsCell textCell = holder.itemView;
                    if (position == GroupInviteActivity.this.copyLinkRow) {
                        textCell.setText(LocaleController.getString("CopyLink", R.string.CopyLink), true);
                        return;
                    } else if (position == GroupInviteActivity.this.shareLinkRow) {
                        textCell.setText(LocaleController.getString("ShareLink", R.string.ShareLink), false);
                        return;
                    } else if (position == GroupInviteActivity.this.revokeLinkRow) {
                        textCell.setText(LocaleController.getString("RevokeLink", R.string.RevokeLink), true);
                        return;
                    } else {
                        return;
                    }
                case 1:
                    TextInfoPrivacyCell privacyCell = holder.itemView;
                    if (position == GroupInviteActivity.this.shadowRow) {
                        privacyCell.setText(TtmlNode.ANONYMOUS_REGION_ID);
                        privacyCell.setBackgroundDrawable(Theme.getThemedDrawable(this.mContext, R.drawable.greydivider_bottom, Theme.key_windowBackgroundGrayShadow));
                        return;
                    } else if (position == GroupInviteActivity.this.linkInfoRow) {
                        Chat chat = MessagesController.getInstance(GroupInviteActivity.this.currentAccount).getChat(Integer.valueOf(GroupInviteActivity.this.chat_id));
                        if (!ChatObject.isChannel(chat) || chat.megagroup) {
                            privacyCell.setText(LocaleController.getString("LinkInfo", R.string.LinkInfo));
                        } else {
                            privacyCell.setText(LocaleController.getString("ChannelLinkInfo", R.string.ChannelLinkInfo));
                        }
                        privacyCell.setBackgroundDrawable(Theme.getThemedDrawable(this.mContext, R.drawable.greydivider, Theme.key_windowBackgroundGrayShadow));
                        return;
                    } else {
                        return;
                    }
                case 2:
                    holder.itemView.setText(GroupInviteActivity.this.invite != null ? GroupInviteActivity.this.invite.link : "error", false);
                    return;
                default:
                    return;
            }
        }

        public int getItemViewType(int position) {
            if (!(position == GroupInviteActivity.this.copyLinkRow || position == GroupInviteActivity.this.shareLinkRow)) {
                if (position != GroupInviteActivity.this.revokeLinkRow) {
                    if (position != GroupInviteActivity.this.shadowRow) {
                        if (position != GroupInviteActivity.this.linkInfoRow) {
                            if (position == GroupInviteActivity.this.linkRow) {
                                return 2;
                            }
                            return 0;
                        }
                    }
                    return 1;
                }
            }
            return 0;
        }
    }

    public GroupInviteActivity(int cid) {
        this.chat_id = cid;
    }

    public boolean onFragmentCreate() {
        super.onFragmentCreate();
        NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.chatInfoDidLoaded);
        MessagesController.getInstance(this.currentAccount).loadFullChat(this.chat_id, this.classGuid, true);
        this.loading = true;
        this.rowCount = 0;
        int i = this.rowCount;
        this.rowCount = i + 1;
        this.linkRow = i;
        i = this.rowCount;
        this.rowCount = i + 1;
        this.linkInfoRow = i;
        i = this.rowCount;
        this.rowCount = i + 1;
        this.copyLinkRow = i;
        i = this.rowCount;
        this.rowCount = i + 1;
        this.revokeLinkRow = i;
        i = this.rowCount;
        this.rowCount = i + 1;
        this.shareLinkRow = i;
        i = this.rowCount;
        this.rowCount = i + 1;
        this.shadowRow = i;
        return true;
    }

    public void onFragmentDestroy() {
        NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.chatInfoDidLoaded);
    }

    public View createView(Context context) {
        this.actionBar.setBackButtonImage(R.drawable.ic_ab_back);
        this.actionBar.setAllowOverlayTitle(true);
        this.actionBar.setTitle(LocaleController.getString("InviteLink", R.string.InviteLink));
        this.actionBar.setActionBarMenuOnItemClick(new ActionBarMenuOnItemClick() {
            public void onItemClick(int id) {
                if (id == -1) {
                    GroupInviteActivity.this.finishFragment();
                }
            }
        });
        this.listAdapter = new ListAdapter(context);
        this.fragmentView = new FrameLayout(context);
        FrameLayout frameLayout = this.fragmentView;
        frameLayout.setBackgroundColor(Theme.getColor(Theme.key_windowBackgroundGray));
        this.emptyView = new EmptyTextProgressView(context);
        this.emptyView.showProgress();
        frameLayout.addView(this.emptyView, LayoutHelper.createFrame(-1, -1, 51));
        this.listView = new RecyclerListView(context);
        this.listView.setLayoutManager(new LinearLayoutManager(context, 1, false));
        this.listView.setEmptyView(this.emptyView);
        this.listView.setVerticalScrollBarEnabled(false);
        frameLayout.addView(this.listView, LayoutHelper.createFrame(-1, -1, 51));
        this.listView.setAdapter(this.listAdapter);
        this.listView.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(android.view.View r1, int r2) {
                /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.DecodeException: Load method exception in method: org.telegram.ui.GroupInviteActivity.2.onItemClick(android.view.View, int):void
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
                r0 = org.telegram.ui.GroupInviteActivity.this;
                r0 = r0.getParentActivity();
                if (r0 != 0) goto L_0x0009;
            L_0x0008:
                return;
            L_0x0009:
                r0 = org.telegram.ui.GroupInviteActivity.this;
                r0 = r0.copyLinkRow;
                if (r7 == r0) goto L_0x00b7;
            L_0x0011:
                r0 = org.telegram.ui.GroupInviteActivity.this;
                r0 = r0.linkRow;
                if (r7 != r0) goto L_0x001b;
            L_0x0019:
                goto L_0x00b7;
            L_0x001b:
                r0 = org.telegram.ui.GroupInviteActivity.this;
                r0 = r0.shareLinkRow;
                if (r7 != r0) goto L_0x0064;
            L_0x0023:
                r0 = org.telegram.ui.GroupInviteActivity.this;
                r0 = r0.invite;
                if (r0 != 0) goto L_0x002c;
            L_0x002b:
                return;
            L_0x002c:
                r0 = new android.content.Intent;	 Catch:{ Exception -> 0x005e }
                r1 = "android.intent.action.SEND";	 Catch:{ Exception -> 0x005e }
                r0.<init>(r1);	 Catch:{ Exception -> 0x005e }
                r1 = "text/plain";	 Catch:{ Exception -> 0x005e }
                r0.setType(r1);	 Catch:{ Exception -> 0x005e }
                r1 = "android.intent.extra.TEXT";	 Catch:{ Exception -> 0x005e }
                r2 = org.telegram.ui.GroupInviteActivity.this;	 Catch:{ Exception -> 0x005e }
                r2 = r2.invite;	 Catch:{ Exception -> 0x005e }
                r2 = r2.link;	 Catch:{ Exception -> 0x005e }
                r0.putExtra(r1, r2);	 Catch:{ Exception -> 0x005e }
                r1 = org.telegram.ui.GroupInviteActivity.this;	 Catch:{ Exception -> 0x005e }
                r1 = r1.getParentActivity();	 Catch:{ Exception -> 0x005e }
                r2 = "InviteToGroupByLink";	 Catch:{ Exception -> 0x005e }
                r3 = 2131493696; // 0x7f0c0340 float:1.861088E38 double:1.0530978095E-314;	 Catch:{ Exception -> 0x005e }
                r2 = org.telegram.messenger.LocaleController.getString(r2, r3);	 Catch:{ Exception -> 0x005e }
                r2 = android.content.Intent.createChooser(r0, r2);	 Catch:{ Exception -> 0x005e }
                r3 = 500; // 0x1f4 float:7.0E-43 double:2.47E-321;	 Catch:{ Exception -> 0x005e }
                r1.startActivityForResult(r2, r3);	 Catch:{ Exception -> 0x005e }
                goto L_0x0062;
            L_0x005e:
                r0 = move-exception;
                org.telegram.messenger.FileLog.e(r0);
            L_0x0062:
                goto L_0x00f8;
            L_0x0064:
                r0 = org.telegram.ui.GroupInviteActivity.this;
                r0 = r0.revokeLinkRow;
                if (r7 != r0) goto L_0x00f8;
            L_0x006c:
                r0 = new org.telegram.ui.ActionBar.AlertDialog$Builder;
                r1 = org.telegram.ui.GroupInviteActivity.this;
                r1 = r1.getParentActivity();
                r0.<init>(r1);
                r1 = "RevokeAlert";
                r2 = 2131494276; // 0x7f0c0584 float:1.8612056E38 double:1.053098096E-314;
                r1 = org.telegram.messenger.LocaleController.getString(r1, r2);
                r0.setMessage(r1);
                r1 = "RevokeLink";
                r2 = 2131494279; // 0x7f0c0587 float:1.8612062E38 double:1.0530980976E-314;
                r1 = org.telegram.messenger.LocaleController.getString(r1, r2);
                r0.setTitle(r1);
                r1 = "RevokeButton";
                r2 = 2131494278; // 0x7f0c0586 float:1.861206E38 double:1.053098097E-314;
                r1 = org.telegram.messenger.LocaleController.getString(r1, r2);
                r2 = new org.telegram.ui.GroupInviteActivity$2$1;
                r2.<init>();
                r0.setPositiveButton(r1, r2);
                r1 = "Cancel";
                r2 = 2131493127; // 0x7f0c0107 float:1.8609725E38 double:1.0530975284E-314;
                r1 = org.telegram.messenger.LocaleController.getString(r1, r2);
                r2 = 0;
                r0.setNegativeButton(r1, r2);
                r1 = org.telegram.ui.GroupInviteActivity.this;
                r2 = r0.create();
                r1.showDialog(r2);
                goto L_0x00f8;
            L_0x00b7:
                r0 = org.telegram.ui.GroupInviteActivity.this;
                r0 = r0.invite;
                if (r0 != 0) goto L_0x00c0;
            L_0x00bf:
                return;
            L_0x00c0:
                r0 = org.telegram.messenger.ApplicationLoader.applicationContext;	 Catch:{ Exception -> 0x00f3 }
                r1 = "clipboard";	 Catch:{ Exception -> 0x00f3 }
                r0 = r0.getSystemService(r1);	 Catch:{ Exception -> 0x00f3 }
                r0 = (android.content.ClipboardManager) r0;	 Catch:{ Exception -> 0x00f3 }
                r1 = "label";	 Catch:{ Exception -> 0x00f3 }
                r2 = org.telegram.ui.GroupInviteActivity.this;	 Catch:{ Exception -> 0x00f3 }
                r2 = r2.invite;	 Catch:{ Exception -> 0x00f3 }
                r2 = r2.link;	 Catch:{ Exception -> 0x00f3 }
                r1 = android.content.ClipData.newPlainText(r1, r2);	 Catch:{ Exception -> 0x00f3 }
                r0.setPrimaryClip(r1);	 Catch:{ Exception -> 0x00f3 }
                r2 = org.telegram.ui.GroupInviteActivity.this;	 Catch:{ Exception -> 0x00f3 }
                r2 = r2.getParentActivity();	 Catch:{ Exception -> 0x00f3 }
                r3 = "LinkCopied";	 Catch:{ Exception -> 0x00f3 }
                r4 = 2131493748; // 0x7f0c0374 float:1.8610985E38 double:1.053097835E-314;	 Catch:{ Exception -> 0x00f3 }
                r3 = org.telegram.messenger.LocaleController.getString(r3, r4);	 Catch:{ Exception -> 0x00f3 }
                r4 = 0;	 Catch:{ Exception -> 0x00f3 }
                r2 = android.widget.Toast.makeText(r2, r3, r4);	 Catch:{ Exception -> 0x00f3 }
                r2.show();	 Catch:{ Exception -> 0x00f3 }
                goto L_0x00f7;
            L_0x00f3:
                r0 = move-exception;
                org.telegram.messenger.FileLog.e(r0);
            L_0x00f8:
                return;
                */
                throw new UnsupportedOperationException("Method not decompiled: org.telegram.ui.GroupInviteActivity.2.onItemClick(android.view.View, int):void");
            }
        });
        return this.fragmentView;
    }

    public void didReceivedNotification(int id, int account, Object... args) {
        if (id == NotificationCenter.chatInfoDidLoaded) {
            ChatFull info = args[0];
            int guid = ((Integer) args[1]).intValue();
            if (info.id == this.chat_id && guid == this.classGuid) {
                this.invite = MessagesController.getInstance(this.currentAccount).getExportedInvite(this.chat_id);
                if (this.invite instanceof TL_chatInviteExported) {
                    this.loading = false;
                    if (this.listAdapter != null) {
                        this.listAdapter.notifyDataSetChanged();
                        return;
                    }
                    return;
                }
                generateLink(false);
            }
        }
    }

    public void onResume() {
        super.onResume();
        if (this.listAdapter != null) {
            this.listAdapter.notifyDataSetChanged();
        }
    }

    private void generateLink(final boolean newRequest) {
        TLObject request;
        this.loading = true;
        if (ChatObject.isChannel(this.chat_id, this.currentAccount)) {
            request = new TL_channels_exportInvite();
            request.channel = MessagesController.getInstance(this.currentAccount).getInputChannel(this.chat_id);
        } else {
            request = new TL_messages_exportChatInvite();
            request.chat_id = this.chat_id;
        }
        ConnectionsManager.getInstance(this.currentAccount).bindRequestToGuid(ConnectionsManager.getInstance(this.currentAccount).sendRequest(request, new RequestDelegate() {
            public void run(final TLObject response, final TL_error error) {
                AndroidUtilities.runOnUIThread(new Runnable() {
                    public void run() {
                        if (error == null) {
                            GroupInviteActivity.this.invite = (ExportedChatInvite) response;
                            if (newRequest) {
                                if (GroupInviteActivity.this.getParentActivity() != null) {
                                    Builder builder = new Builder(GroupInviteActivity.this.getParentActivity());
                                    builder.setMessage(LocaleController.getString("RevokeAlertNewLink", R.string.RevokeAlertNewLink));
                                    builder.setTitle(LocaleController.getString("RevokeLink", R.string.RevokeLink));
                                    builder.setNegativeButton(LocaleController.getString("OK", R.string.OK), null);
                                    GroupInviteActivity.this.showDialog(builder.create());
                                } else {
                                    return;
                                }
                            }
                        }
                        GroupInviteActivity.this.loading = false;
                        GroupInviteActivity.this.listAdapter.notifyDataSetChanged();
                    }
                });
            }
        }), this.classGuid);
        if (this.listAdapter != null) {
            this.listAdapter.notifyDataSetChanged();
        }
    }

    public ThemeDescription[] getThemeDescriptions() {
        ThemeDescription[] themeDescriptionArr = new ThemeDescription[14];
        themeDescriptionArr[0] = new ThemeDescription(this.listView, ThemeDescription.FLAG_CELLBACKGROUNDCOLOR, new Class[]{TextSettingsCell.class, TextBlockCell.class}, null, null, null, Theme.key_windowBackgroundWhite);
        themeDescriptionArr[1] = new ThemeDescription(this.fragmentView, ThemeDescription.FLAG_BACKGROUND, null, null, null, null, Theme.key_windowBackgroundGray);
        themeDescriptionArr[2] = new ThemeDescription(this.actionBar, ThemeDescription.FLAG_BACKGROUND, null, null, null, null, Theme.key_actionBarDefault);
        themeDescriptionArr[3] = new ThemeDescription(this.listView, ThemeDescription.FLAG_LISTGLOWCOLOR, null, null, null, null, Theme.key_actionBarDefault);
        themeDescriptionArr[4] = new ThemeDescription(this.actionBar, ThemeDescription.FLAG_AB_ITEMSCOLOR, null, null, null, null, Theme.key_actionBarDefaultIcon);
        themeDescriptionArr[5] = new ThemeDescription(this.actionBar, ThemeDescription.FLAG_AB_TITLECOLOR, null, null, null, null, Theme.key_actionBarDefaultTitle);
        themeDescriptionArr[6] = new ThemeDescription(this.actionBar, ThemeDescription.FLAG_AB_SELECTORCOLOR, null, null, null, null, Theme.key_actionBarDefaultSelector);
        themeDescriptionArr[7] = new ThemeDescription(this.listView, ThemeDescription.FLAG_SELECTOR, null, null, null, null, Theme.key_listSelector);
        themeDescriptionArr[8] = new ThemeDescription(this.listView, 0, new Class[]{View.class}, Theme.dividerPaint, null, null, Theme.key_divider);
        themeDescriptionArr[9] = new ThemeDescription(this.emptyView, ThemeDescription.FLAG_PROGRESSBAR, null, null, null, null, Theme.key_progressCircle);
        themeDescriptionArr[10] = new ThemeDescription(this.listView, 0, new Class[]{TextSettingsCell.class}, new String[]{"textView"}, null, null, null, Theme.key_windowBackgroundWhiteBlackText);
        themeDescriptionArr[11] = new ThemeDescription(this.listView, ThemeDescription.FLAG_BACKGROUNDFILTER, new Class[]{TextInfoPrivacyCell.class}, null, null, null, Theme.key_windowBackgroundGrayShadow);
        themeDescriptionArr[12] = new ThemeDescription(this.listView, 0, new Class[]{TextInfoPrivacyCell.class}, new String[]{"textView"}, null, null, null, Theme.key_windowBackgroundWhiteGrayText4);
        themeDescriptionArr[13] = new ThemeDescription(this.listView, 0, new Class[]{TextBlockCell.class}, new String[]{"textView"}, null, null, null, Theme.key_windowBackgroundWhiteBlackText);
        return themeDescriptionArr;
    }
}
