package org.telegram.ui.Adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.DialogInterface.OnDismissListener;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.location.Location;
import android.os.Build.VERSION;
import android.text.TextUtils;
import android.util.SparseArray;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.HashMap;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.ChatObject;
import org.telegram.messenger.EmojiSuggestion;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MessageObject;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.MessagesStorage;
import org.telegram.messenger.SendMessagesHelper.LocationProvider;
import org.telegram.messenger.SendMessagesHelper.LocationProvider.LocationProviderDelegate;
import org.telegram.messenger.UserConfig;
import org.telegram.messenger.beta.R;
import org.telegram.messenger.support.widget.RecyclerView.ViewHolder;
import org.telegram.tgnet.ConnectionsManager;
import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC.BotInfo;
import org.telegram.tgnet.TLRPC.BotInlineResult;
import org.telegram.tgnet.TLRPC.Chat;
import org.telegram.tgnet.TLRPC.ChatFull;
import org.telegram.tgnet.TLRPC.TL_botInlineMessageMediaAuto;
import org.telegram.tgnet.TLRPC.TL_channelFull;
import org.telegram.tgnet.TLRPC.TL_contacts_resolveUsername;
import org.telegram.tgnet.TLRPC.TL_contacts_resolvedPeer;
import org.telegram.tgnet.TLRPC.TL_document;
import org.telegram.tgnet.TLRPC.TL_error;
import org.telegram.tgnet.TLRPC.TL_inlineBotSwitchPM;
import org.telegram.tgnet.TLRPC.TL_inputGeoPoint;
import org.telegram.tgnet.TLRPC.TL_inputPeerEmpty;
import org.telegram.tgnet.TLRPC.TL_messages_botResults;
import org.telegram.tgnet.TLRPC.TL_messages_getInlineBotResults;
import org.telegram.tgnet.TLRPC.TL_photo;
import org.telegram.tgnet.TLRPC.User;
import org.telegram.ui.ActionBar.AlertDialog.Builder;
import org.telegram.ui.Adapters.SearchAdapterHelper.HashtagObject;
import org.telegram.ui.Adapters.SearchAdapterHelper.SearchAdapterHelperDelegate;
import org.telegram.ui.Cells.BotSwitchCell;
import org.telegram.ui.Cells.ContextLinkCell;
import org.telegram.ui.Cells.MentionCell;
import org.telegram.ui.ChatActivity;
import org.telegram.ui.Components.RecyclerListView.SelectionAdapter;

public class MentionsAdapter extends SelectionAdapter {
    private SparseArray<BotInfo> botInfo;
    private int botsCount;
    private int channelLastReqId;
    private int channelReqId;
    private boolean contextMedia;
    private int contextQueryReqid;
    private Runnable contextQueryRunnable;
    private int contextUsernameReqid;
    private int currentAccount = UserConfig.selectedAccount;
    private MentionsAdapterDelegate delegate;
    private long dialog_id;
    private User foundContextBot;
    private ChatFull info;
    private boolean inlineMediaEnabled = true;
    private boolean isDarkTheme;
    private boolean isSearchingMentions;
    private Location lastKnownLocation;
    private int lastPosition;
    private String lastText;
    private boolean lastUsernameOnly;
    private LocationProvider locationProvider = new LocationProvider(new LocationProviderDelegate() {
        public void onLocationAcquired(Location location) {
            if (MentionsAdapter.this.foundContextBot != null && MentionsAdapter.this.foundContextBot.bot_inline_geo) {
                MentionsAdapter.this.lastKnownLocation = location;
                MentionsAdapter.this.searchForContextBotResults(true, MentionsAdapter.this.foundContextBot, MentionsAdapter.this.searchingContextQuery, TtmlNode.ANONYMOUS_REGION_ID);
            }
        }

        public void onUnableLocationAcquire() {
            MentionsAdapter.this.onLocationUnavailable();
        }
    }) {
        public void stop() {
            super.stop();
            MentionsAdapter.this.lastKnownLocation = null;
        }
    };
    private Context mContext;
    private ArrayList<MessageObject> messages;
    private boolean needBotContext = true;
    private boolean needUsernames = true;
    private String nextQueryOffset;
    private boolean noUserName;
    private ChatActivity parentFragment;
    private int resultLength;
    private int resultStartPosition;
    private SearchAdapterHelper searchAdapterHelper;
    private Runnable searchGlobalRunnable;
    private ArrayList<BotInlineResult> searchResultBotContext;
    private HashMap<String, BotInlineResult> searchResultBotContextById;
    private TL_inlineBotSwitchPM searchResultBotContextSwitch;
    private ArrayList<String> searchResultCommands;
    private ArrayList<String> searchResultCommandsHelp;
    private ArrayList<User> searchResultCommandsUsers;
    private ArrayList<String> searchResultHashtags;
    private ArrayList<EmojiSuggestion> searchResultSuggestions;
    private ArrayList<User> searchResultUsernames;
    private SparseArray<User> searchResultUsernamesMap;
    private String searchingContextQuery;
    private String searchingContextUsername;

    public interface MentionsAdapterDelegate {
        void needChangePanelVisibility(boolean z);

        void onContextClick(BotInlineResult botInlineResult);

        void onContextSearch(boolean z);
    }

    public org.telegram.messenger.support.widget.RecyclerView.ViewHolder onCreateViewHolder(android.view.ViewGroup r1, int r2) {
        /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.DecodeException: Load method exception in method: org.telegram.ui.Adapters.MentionsAdapter.onCreateViewHolder(android.view.ViewGroup, int):org.telegram.messenger.support.widget.RecyclerView$ViewHolder
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
        switch(r7) {
            case 0: goto L_0x004b;
            case 1: goto L_0x0038;
            case 2: goto L_0x0030;
            default: goto L_0x0003;
        };
    L_0x0003:
        r0 = new android.widget.TextView;
        r1 = r5.mContext;
        r0.<init>(r1);
        r1 = 1090519040; // 0x41000000 float:8.0 double:5.38787994E-315;
        r2 = org.telegram.messenger.AndroidUtilities.dp(r1);
        r3 = org.telegram.messenger.AndroidUtilities.dp(r1);
        r4 = org.telegram.messenger.AndroidUtilities.dp(r1);
        r1 = org.telegram.messenger.AndroidUtilities.dp(r1);
        r0.setPadding(r2, r3, r4, r1);
        r1 = 1;
        r2 = 1096810496; // 0x41600000 float:14.0 double:5.41896386E-315;
        r0.setTextSize(r1, r2);
        r1 = "windowBackgroundWhiteGrayText2";
        r1 = org.telegram.ui.ActionBar.Theme.getColor(r1);
        r0.setTextColor(r1);
        goto L_0x005b;
    L_0x0030:
        r0 = new org.telegram.ui.Cells.BotSwitchCell;
        r1 = r5.mContext;
        r0.<init>(r1);
        goto L_0x005b;
    L_0x0038:
        r0 = new org.telegram.ui.Cells.ContextLinkCell;
        r1 = r5.mContext;
        r0.<init>(r1);
        r1 = r0;
        r1 = (org.telegram.ui.Cells.ContextLinkCell) r1;
        r2 = new org.telegram.ui.Adapters.MentionsAdapter$12;
        r2.<init>();
        r1.setDelegate(r2);
        goto L_0x005b;
    L_0x004b:
        r0 = new org.telegram.ui.Cells.MentionCell;
        r1 = r5.mContext;
        r0.<init>(r1);
        r1 = r0;
        r1 = (org.telegram.ui.Cells.MentionCell) r1;
        r2 = r5.isDarkTheme;
        r1.setIsDarkTheme(r2);
        r1 = new org.telegram.ui.Components.RecyclerListView$Holder;
        r1.<init>(r0);
        return r1;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.telegram.ui.Adapters.MentionsAdapter.onCreateViewHolder(android.view.ViewGroup, int):org.telegram.messenger.support.widget.RecyclerView$ViewHolder");
    }

    static /* synthetic */ int access$3104(MentionsAdapter x0) {
        int i = x0.channelLastReqId + 1;
        x0.channelLastReqId = i;
        return i;
    }

    public MentionsAdapter(Context context, boolean darkTheme, long did, MentionsAdapterDelegate mentionsAdapterDelegate) {
        this.mContext = context;
        this.delegate = mentionsAdapterDelegate;
        this.isDarkTheme = darkTheme;
        this.dialog_id = did;
        this.searchAdapterHelper = new SearchAdapterHelper(true);
        this.searchAdapterHelper.setDelegate(new SearchAdapterHelperDelegate() {
            public void onDataSetChanged() {
                MentionsAdapter.this.notifyDataSetChanged();
            }

            public void onSetHashtags(ArrayList<HashtagObject> arrayList, HashMap<String, HashtagObject> hashMap) {
                if (MentionsAdapter.this.lastText != null) {
                    MentionsAdapter.this.searchUsernameOrHashtag(MentionsAdapter.this.lastText, MentionsAdapter.this.lastPosition, MentionsAdapter.this.messages, MentionsAdapter.this.lastUsernameOnly);
                }
            }
        });
    }

    public void onDestroy() {
        if (this.locationProvider != null) {
            this.locationProvider.stop();
        }
        if (this.contextQueryRunnable != null) {
            AndroidUtilities.cancelRunOnUIThread(this.contextQueryRunnable);
            this.contextQueryRunnable = null;
        }
        if (this.contextUsernameReqid != 0) {
            ConnectionsManager.getInstance(this.currentAccount).cancelRequest(this.contextUsernameReqid, true);
            this.contextUsernameReqid = 0;
        }
        if (this.contextQueryReqid != 0) {
            ConnectionsManager.getInstance(this.currentAccount).cancelRequest(this.contextQueryReqid, true);
            this.contextQueryReqid = 0;
        }
        this.foundContextBot = null;
        this.inlineMediaEnabled = true;
        this.searchingContextUsername = null;
        this.searchingContextQuery = null;
        this.noUserName = false;
    }

    public void setParentFragment(ChatActivity fragment) {
        this.parentFragment = fragment;
    }

    public void setChatInfo(ChatFull chatInfo) {
        this.currentAccount = UserConfig.selectedAccount;
        this.info = chatInfo;
        if (!(this.inlineMediaEnabled || this.foundContextBot == null || this.parentFragment == null)) {
            Chat chat = this.parentFragment.getCurrentChat();
            if (chat != null) {
                this.inlineMediaEnabled = ChatObject.canSendStickers(chat);
                if (this.inlineMediaEnabled) {
                    this.searchResultUsernames = null;
                    notifyDataSetChanged();
                    this.delegate.needChangePanelVisibility(false);
                    processFoundUser(this.foundContextBot);
                }
            }
        }
        if (this.lastText != null) {
            searchUsernameOrHashtag(this.lastText, this.lastPosition, this.messages, this.lastUsernameOnly);
        }
    }

    public void setNeedUsernames(boolean value) {
        this.needUsernames = value;
    }

    public void setNeedBotContext(boolean value) {
        this.needBotContext = value;
    }

    public void setBotInfo(SparseArray<BotInfo> info) {
        this.botInfo = info;
    }

    public void setBotsCount(int count) {
        this.botsCount = count;
    }

    public void clearRecentHashtags() {
        this.searchAdapterHelper.clearRecentHashtags();
        this.searchResultHashtags.clear();
        notifyDataSetChanged();
        if (this.delegate != null) {
            this.delegate.needChangePanelVisibility(false);
        }
    }

    public TL_inlineBotSwitchPM getBotContextSwitch() {
        return this.searchResultBotContextSwitch;
    }

    public int getContextBotId() {
        return this.foundContextBot != null ? this.foundContextBot.id : 0;
    }

    public User getContextBotUser() {
        return this.foundContextBot;
    }

    public String getContextBotName() {
        return this.foundContextBot != null ? this.foundContextBot.username : TtmlNode.ANONYMOUS_REGION_ID;
    }

    private void processFoundUser(User user) {
        this.contextUsernameReqid = 0;
        this.locationProvider.stop();
        if (user == null || !user.bot || user.bot_inline_placeholder == null) {
            this.foundContextBot = null;
            this.inlineMediaEnabled = true;
        } else {
            this.foundContextBot = user;
            if (this.parentFragment != null) {
                Chat chat = this.parentFragment.getCurrentChat();
                if (chat != null) {
                    this.inlineMediaEnabled = ChatObject.canSendStickers(chat);
                    if (!this.inlineMediaEnabled) {
                        notifyDataSetChanged();
                        this.delegate.needChangePanelVisibility(true);
                        return;
                    }
                }
            }
            if (this.foundContextBot.bot_inline_geo) {
                SharedPreferences preferences = MessagesController.getNotificationsSettings(this.currentAccount);
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("inlinegeo_");
                stringBuilder.append(this.foundContextBot.id);
                if (preferences.getBoolean(stringBuilder.toString(), false) || this.parentFragment == null || this.parentFragment.getParentActivity() == null) {
                    checkLocationPermissionsOrStart();
                } else {
                    final User foundContextBotFinal = this.foundContextBot;
                    Builder builder = new Builder(this.parentFragment.getParentActivity());
                    builder.setTitle(LocaleController.getString("ShareYouLocationTitle", R.string.ShareYouLocationTitle));
                    builder.setMessage(LocaleController.getString("ShareYouLocationInline", R.string.ShareYouLocationInline));
                    final boolean[] buttonClicked = new boolean[1];
                    builder.setPositiveButton(LocaleController.getString("OK", R.string.OK), new OnClickListener() {
                        public void onClick(DialogInterface dialogInterface, int i) {
                            buttonClicked[0] = true;
                            if (foundContextBotFinal != null) {
                                Editor edit = MessagesController.getNotificationsSettings(MentionsAdapter.this.currentAccount).edit();
                                StringBuilder stringBuilder = new StringBuilder();
                                stringBuilder.append("inlinegeo_");
                                stringBuilder.append(foundContextBotFinal.id);
                                edit.putBoolean(stringBuilder.toString(), true).commit();
                                MentionsAdapter.this.checkLocationPermissionsOrStart();
                            }
                        }
                    });
                    builder.setNegativeButton(LocaleController.getString("Cancel", R.string.Cancel), new OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            buttonClicked[0] = true;
                            MentionsAdapter.this.onLocationUnavailable();
                        }
                    });
                    this.parentFragment.showDialog(builder.create(), new OnDismissListener() {
                        public void onDismiss(DialogInterface dialog) {
                            if (!buttonClicked[0]) {
                                MentionsAdapter.this.onLocationUnavailable();
                            }
                        }
                    });
                }
            }
        }
        if (this.foundContextBot == null) {
            this.noUserName = true;
        } else {
            if (this.delegate != null) {
                this.delegate.onContextSearch(true);
            }
            searchForContextBotResults(true, this.foundContextBot, this.searchingContextQuery, TtmlNode.ANONYMOUS_REGION_ID);
        }
    }

    private void searchForContextBot(String username, String query) {
        if (this.foundContextBot == null || this.foundContextBot.username == null || !this.foundContextBot.username.equals(username) || this.searchingContextQuery == null || !this.searchingContextQuery.equals(query)) {
            this.searchResultBotContext = null;
            this.searchResultBotContextById = null;
            this.searchResultBotContextSwitch = null;
            notifyDataSetChanged();
            if (this.foundContextBot != null) {
                if (this.inlineMediaEnabled || username == null || query == null) {
                    this.delegate.needChangePanelVisibility(false);
                } else {
                    return;
                }
            }
            if (this.contextQueryRunnable != null) {
                AndroidUtilities.cancelRunOnUIThread(this.contextQueryRunnable);
                this.contextQueryRunnable = null;
            }
            if (TextUtils.isEmpty(username) || !(this.searchingContextUsername == null || this.searchingContextUsername.equals(username))) {
                if (this.contextUsernameReqid != 0) {
                    ConnectionsManager.getInstance(this.currentAccount).cancelRequest(this.contextUsernameReqid, true);
                    this.contextUsernameReqid = 0;
                }
                if (this.contextQueryReqid != 0) {
                    ConnectionsManager.getInstance(this.currentAccount).cancelRequest(this.contextQueryReqid, true);
                    this.contextQueryReqid = 0;
                }
                this.foundContextBot = null;
                this.inlineMediaEnabled = true;
                this.searchingContextUsername = null;
                this.searchingContextQuery = null;
                this.locationProvider.stop();
                this.noUserName = false;
                if (this.delegate != null) {
                    this.delegate.onContextSearch(false);
                }
                if (username != null) {
                    if (username.length() == 0) {
                    }
                }
                return;
            }
            if (query == null) {
                if (this.contextQueryReqid != 0) {
                    ConnectionsManager.getInstance(this.currentAccount).cancelRequest(this.contextQueryReqid, true);
                    this.contextQueryReqid = 0;
                }
                this.searchingContextQuery = null;
                if (this.delegate != null) {
                    this.delegate.onContextSearch(false);
                }
                return;
            }
            if (this.delegate != null) {
                if (this.foundContextBot != null) {
                    this.delegate.onContextSearch(true);
                } else if (username.equals("gif")) {
                    this.searchingContextUsername = "gif";
                    this.delegate.onContextSearch(false);
                }
            }
            MessagesController messagesController = MessagesController.getInstance(this.currentAccount);
            MessagesStorage messagesStorage = MessagesStorage.getInstance(this.currentAccount);
            this.searchingContextQuery = query;
            final String str = query;
            final String str2 = username;
            final MessagesController messagesController2 = messagesController;
            final MessagesStorage messagesStorage2 = messagesStorage;
            this.contextQueryRunnable = new Runnable() {
                public void run() {
                    if (MentionsAdapter.this.contextQueryRunnable == this) {
                        MentionsAdapter.this.contextQueryRunnable = null;
                        if (MentionsAdapter.this.foundContextBot == null) {
                            if (!MentionsAdapter.this.noUserName) {
                                MentionsAdapter.this.searchingContextUsername = str2;
                                TLObject object = messagesController2.getUserOrChat(MentionsAdapter.this.searchingContextUsername);
                                if (object instanceof User) {
                                    MentionsAdapter.this.processFoundUser((User) object);
                                } else {
                                    TL_contacts_resolveUsername req = new TL_contacts_resolveUsername();
                                    req.username = MentionsAdapter.this.searchingContextUsername;
                                    MentionsAdapter.this.contextUsernameReqid = ConnectionsManager.getInstance(MentionsAdapter.this.currentAccount).sendRequest(req, new RequestDelegate() {
                                        public void run(final TLObject response, final TL_error error) {
                                            AndroidUtilities.runOnUIThread(new Runnable() {
                                                public void run() {
                                                    if (MentionsAdapter.this.searchingContextUsername != null) {
                                                        if (MentionsAdapter.this.searchingContextUsername.equals(str2)) {
                                                            User user = null;
                                                            if (error == null) {
                                                                TL_contacts_resolvedPeer res = response;
                                                                if (!res.users.isEmpty()) {
                                                                    user = (User) res.users.get(0);
                                                                    messagesController2.putUser(user, false);
                                                                    messagesStorage2.putUsersAndChats(res.users, null, true, true);
                                                                }
                                                            }
                                                            MentionsAdapter.this.processFoundUser(user);
                                                        }
                                                    }
                                                }
                                            });
                                        }
                                    });
                                }
                            }
                        }
                        if (!MentionsAdapter.this.noUserName) {
                            MentionsAdapter.this.searchForContextBotResults(true, MentionsAdapter.this.foundContextBot, str, TtmlNode.ANONYMOUS_REGION_ID);
                        }
                    }
                }
            };
            AndroidUtilities.runOnUIThread(this.contextQueryRunnable, 400);
        }
    }

    private void onLocationUnavailable() {
        if (this.foundContextBot != null && this.foundContextBot.bot_inline_geo) {
            this.lastKnownLocation = new Location("network");
            this.lastKnownLocation.setLatitude(-1000.0d);
            this.lastKnownLocation.setLongitude(-1000.0d);
            searchForContextBotResults(true, this.foundContextBot, this.searchingContextQuery, TtmlNode.ANONYMOUS_REGION_ID);
        }
    }

    private void checkLocationPermissionsOrStart() {
        if (this.parentFragment != null) {
            if (this.parentFragment.getParentActivity() != null) {
                if (VERSION.SDK_INT < 23 || this.parentFragment.getParentActivity().checkSelfPermission("android.permission.ACCESS_COARSE_LOCATION") == 0) {
                    if (this.foundContextBot != null && this.foundContextBot.bot_inline_geo) {
                        this.locationProvider.start();
                    }
                    return;
                }
                this.parentFragment.getParentActivity().requestPermissions(new String[]{"android.permission.ACCESS_COARSE_LOCATION", "android.permission.ACCESS_FINE_LOCATION"}, 2);
            }
        }
    }

    public void setSearchingMentions(boolean value) {
        this.isSearchingMentions = value;
    }

    public String getBotCaption() {
        if (this.foundContextBot != null) {
            return this.foundContextBot.bot_inline_placeholder;
        }
        if (this.searchingContextUsername == null || !this.searchingContextUsername.equals("gif")) {
            return null;
        }
        return "Search GIFs";
    }

    public void searchForContextBotForNextOffset() {
        if (!(this.contextQueryReqid != 0 || this.nextQueryOffset == null || this.nextQueryOffset.length() == 0 || this.foundContextBot == null)) {
            if (this.searchingContextQuery != null) {
                searchForContextBotResults(true, this.foundContextBot, this.searchingContextQuery, this.nextQueryOffset);
            }
        }
    }

    private void searchForContextBotResults(boolean cache, User user, String query, String offset) {
        User user2 = user;
        String str = query;
        String str2 = offset;
        if (this.contextQueryReqid != 0) {
            ConnectionsManager.getInstance(r8.currentAccount).cancelRequest(r8.contextQueryReqid, true);
            r8.contextQueryReqid = 0;
        }
        if (r8.inlineMediaEnabled) {
            if (str != null) {
                if (user2 != null) {
                    if (!user2.bot_inline_geo || r8.lastKnownLocation != null) {
                        StringBuilder stringBuilder = new StringBuilder();
                        stringBuilder.append(r8.dialog_id);
                        stringBuilder.append("_");
                        stringBuilder.append(str);
                        stringBuilder.append("_");
                        stringBuilder.append(str2);
                        stringBuilder.append("_");
                        stringBuilder.append(r8.dialog_id);
                        stringBuilder.append("_");
                        stringBuilder.append(user2.id);
                        stringBuilder.append("_");
                        Object valueOf = (!user2.bot_inline_geo || r8.lastKnownLocation == null || r8.lastKnownLocation.getLatitude() == -1000.0d) ? TtmlNode.ANONYMOUS_REGION_ID : Double.valueOf(r8.lastKnownLocation.getLatitude() + r8.lastKnownLocation.getLongitude());
                        stringBuilder.append(valueOf);
                        String key = stringBuilder.toString();
                        MessagesStorage messagesStorage = MessagesStorage.getInstance(r8.currentAccount);
                        final String str3 = str;
                        final boolean z = cache;
                        final User user3 = user2;
                        final String str4 = str2;
                        final MessagesStorage messagesStorage2 = messagesStorage;
                        MessagesStorage messagesStorage3 = messagesStorage;
                        final String messagesStorage4 = key;
                        AnonymousClass8 requestDelegate = new RequestDelegate() {
                            public void run(final TLObject response, TL_error error) {
                                AndroidUtilities.runOnUIThread(new Runnable() {
                                    public void run() {
                                        if (MentionsAdapter.this.searchingContextQuery != null) {
                                            if (str3.equals(MentionsAdapter.this.searchingContextQuery)) {
                                                boolean z = false;
                                                MentionsAdapter.this.contextQueryReqid = 0;
                                                if (z && response == null) {
                                                    MentionsAdapter.this.searchForContextBotResults(false, user3, str3, str4);
                                                } else if (MentionsAdapter.this.delegate != null) {
                                                    MentionsAdapter.this.delegate.onContextSearch(false);
                                                }
                                                if (response != null) {
                                                    boolean hasTop;
                                                    MentionsAdapterDelegate access$1700;
                                                    TL_messages_botResults res = response;
                                                    if (!(z || res.cache_time == 0)) {
                                                        messagesStorage2.saveBotCache(messagesStorage4, res);
                                                    }
                                                    MentionsAdapter.this.nextQueryOffset = res.next_offset;
                                                    if (MentionsAdapter.this.searchResultBotContextById == null) {
                                                        MentionsAdapter.this.searchResultBotContextById = new HashMap();
                                                        MentionsAdapter.this.searchResultBotContextSwitch = res.switch_pm;
                                                    }
                                                    int a = 0;
                                                    while (a < res.results.size()) {
                                                        BotInlineResult result = (BotInlineResult) res.results.get(a);
                                                        if (MentionsAdapter.this.searchResultBotContextById.containsKey(result.id) || (!(result.document instanceof TL_document) && !(result.photo instanceof TL_photo) && result.content == null && (result.send_message instanceof TL_botInlineMessageMediaAuto))) {
                                                            res.results.remove(a);
                                                            a--;
                                                        }
                                                        result.query_id = res.query_id;
                                                        MentionsAdapter.this.searchResultBotContextById.put(result.id, result);
                                                        a++;
                                                    }
                                                    boolean added = false;
                                                    if (MentionsAdapter.this.searchResultBotContext != null) {
                                                        if (str4.length() != 0) {
                                                            added = true;
                                                            MentionsAdapter.this.searchResultBotContext.addAll(res.results);
                                                            if (res.results.isEmpty()) {
                                                                MentionsAdapter.this.nextQueryOffset = TtmlNode.ANONYMOUS_REGION_ID;
                                                            }
                                                            MentionsAdapter.this.searchResultHashtags = null;
                                                            MentionsAdapter.this.searchResultUsernames = null;
                                                            MentionsAdapter.this.searchResultUsernamesMap = null;
                                                            MentionsAdapter.this.searchResultCommands = null;
                                                            MentionsAdapter.this.searchResultSuggestions = null;
                                                            MentionsAdapter.this.searchResultCommandsHelp = null;
                                                            MentionsAdapter.this.searchResultCommandsUsers = null;
                                                            if (added) {
                                                                MentionsAdapter.this.notifyDataSetChanged();
                                                            } else {
                                                                hasTop = MentionsAdapter.this.searchResultBotContextSwitch == null;
                                                                MentionsAdapter.this.notifyItemChanged(((MentionsAdapter.this.searchResultBotContext.size() - res.results.size()) + (hasTop ? 1 : 0)) - 1);
                                                                MentionsAdapter.this.notifyItemRangeInserted((MentionsAdapter.this.searchResultBotContext.size() - res.results.size()) + (hasTop ? 1 : 0), res.results.size());
                                                            }
                                                            access$1700 = MentionsAdapter.this.delegate;
                                                            if (MentionsAdapter.this.searchResultBotContext.isEmpty()) {
                                                                if (MentionsAdapter.this.searchResultBotContextSwitch != null) {
                                                                    access$1700.needChangePanelVisibility(z);
                                                                }
                                                            }
                                                            z = true;
                                                            access$1700.needChangePanelVisibility(z);
                                                        }
                                                    }
                                                    MentionsAdapter.this.searchResultBotContext = res.results;
                                                    MentionsAdapter.this.contextMedia = res.gallery;
                                                    MentionsAdapter.this.searchResultHashtags = null;
                                                    MentionsAdapter.this.searchResultUsernames = null;
                                                    MentionsAdapter.this.searchResultUsernamesMap = null;
                                                    MentionsAdapter.this.searchResultCommands = null;
                                                    MentionsAdapter.this.searchResultSuggestions = null;
                                                    MentionsAdapter.this.searchResultCommandsHelp = null;
                                                    MentionsAdapter.this.searchResultCommandsUsers = null;
                                                    if (added) {
                                                        MentionsAdapter.this.notifyDataSetChanged();
                                                    } else {
                                                        if (MentionsAdapter.this.searchResultBotContextSwitch == null) {
                                                        }
                                                        if (hasTop) {
                                                        }
                                                        MentionsAdapter.this.notifyItemChanged(((MentionsAdapter.this.searchResultBotContext.size() - res.results.size()) + (hasTop ? 1 : 0)) - 1);
                                                        if (hasTop) {
                                                        }
                                                        MentionsAdapter.this.notifyItemRangeInserted((MentionsAdapter.this.searchResultBotContext.size() - res.results.size()) + (hasTop ? 1 : 0), res.results.size());
                                                    }
                                                    access$1700 = MentionsAdapter.this.delegate;
                                                    if (MentionsAdapter.this.searchResultBotContext.isEmpty()) {
                                                        if (MentionsAdapter.this.searchResultBotContextSwitch != null) {
                                                            access$1700.needChangePanelVisibility(z);
                                                        }
                                                    }
                                                    z = true;
                                                    access$1700.needChangePanelVisibility(z);
                                                }
                                            }
                                        }
                                    }
                                });
                            }
                        };
                        if (cache) {
                            messagesStorage3.getBotCache(key, requestDelegate);
                        } else {
                            TL_messages_getInlineBotResults req = new TL_messages_getInlineBotResults();
                            req.bot = MessagesController.getInstance(r8.currentAccount).getInputUser(user2);
                            req.query = str;
                            req.offset = str2;
                            if (!(!user2.bot_inline_geo || r8.lastKnownLocation == null || r8.lastKnownLocation.getLatitude() == -1000.0d)) {
                                req.flags |= 1;
                                req.geo_point = new TL_inputGeoPoint();
                                req.geo_point.lat = r8.lastKnownLocation.getLatitude();
                                req.geo_point._long = r8.lastKnownLocation.getLongitude();
                            }
                            int lower_id = (int) r8.dialog_id;
                            int high_id = (int) (r8.dialog_id >> 32);
                            if (lower_id != 0) {
                                req.peer = MessagesController.getInstance(r8.currentAccount).getInputPeer(lower_id);
                            } else {
                                req.peer = new TL_inputPeerEmpty();
                            }
                            r8.contextQueryReqid = ConnectionsManager.getInstance(r8.currentAccount).sendRequest(req, requestDelegate, 2);
                        }
                        return;
                    }
                    return;
                }
            }
            r8.searchingContextQuery = null;
            return;
        }
        if (r8.delegate != null) {
            r8.delegate.onContextSearch(false);
        }
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void searchUsernameOrHashtag(java.lang.String r27, int r28, java.util.ArrayList<org.telegram.messenger.MessageObject> r29, boolean r30) {
        /*
        r26 = this;
        r0 = r26;
        r1 = r27;
        r2 = r28;
        r3 = r29;
        r4 = r30;
        r5 = r0.channelReqId;
        r6 = 0;
        r7 = 1;
        if (r5 == 0) goto L_0x001d;
    L_0x0010:
        r5 = r0.currentAccount;
        r5 = org.telegram.tgnet.ConnectionsManager.getInstance(r5);
        r8 = r0.channelReqId;
        r5.cancelRequest(r8, r7);
        r0.channelReqId = r6;
    L_0x001d:
        r5 = r0.searchGlobalRunnable;
        r8 = 0;
        if (r5 == 0) goto L_0x0029;
    L_0x0022:
        r5 = r0.searchGlobalRunnable;
        org.telegram.messenger.AndroidUtilities.cancelRunOnUIThread(r5);
        r0.searchGlobalRunnable = r8;
    L_0x0029:
        r5 = android.text.TextUtils.isEmpty(r27);
        if (r5 == 0) goto L_0x003a;
    L_0x002f:
        r0.searchForContextBot(r8, r8);
        r5 = r0.delegate;
        r5.needChangePanelVisibility(r6);
        r0.lastText = r8;
        return;
    L_0x003a:
        r5 = r2;
        r9 = r27.length();
        if (r9 <= 0) goto L_0x0043;
    L_0x0041:
        r5 = r5 + -1;
    L_0x0043:
        r0.lastText = r8;
        r0.lastUsernameOnly = r4;
        r9 = new java.lang.StringBuilder;
        r9.<init>();
        r10 = -1;
        r11 = 0;
        r14 = 48;
        r15 = 64;
        r12 = 32;
        if (r4 != 0) goto L_0x00e2;
    L_0x0056:
        r13 = r0.needBotContext;
        if (r13 == 0) goto L_0x00e2;
    L_0x005a:
        r13 = r1.charAt(r6);
        if (r13 != r15) goto L_0x00e2;
    L_0x0060:
        r13 = r1.indexOf(r12);
        r17 = r27.length();
        r18 = 0;
        r19 = 0;
        if (r13 <= 0) goto L_0x007d;
    L_0x006e:
        r18 = r1.substring(r7, r13);
        r15 = r13 + 1;
        r19 = r1.substring(r15);
    L_0x0078:
        r12 = r18;
        r15 = r19;
        goto L_0x00a6;
    L_0x007d:
        r15 = r17 + -1;
        r15 = r1.charAt(r15);
        r12 = 116; // 0x74 float:1.63E-43 double:5.73E-322;
        if (r15 != r12) goto L_0x00a2;
    L_0x0087:
        r12 = r17 + -2;
        r12 = r1.charAt(r12);
        r15 = 111; // 0x6f float:1.56E-43 double:5.5E-322;
        if (r12 != r15) goto L_0x00a2;
    L_0x0091:
        r12 = r17 + -3;
        r12 = r1.charAt(r12);
        r15 = 98;
        if (r12 != r15) goto L_0x00a2;
    L_0x009b:
        r18 = r1.substring(r7);
        r19 = "";
        goto L_0x0078;
    L_0x00a2:
        r0.searchForContextBot(r8, r8);
        goto L_0x0078;
    L_0x00a6:
        if (r12 == 0) goto L_0x00dc;
    L_0x00a8:
        r6 = r12.length();
        if (r6 < r7) goto L_0x00dc;
    L_0x00ae:
        r6 = r7;
    L_0x00af:
        r7 = r12.length();
        if (r6 >= r7) goto L_0x00de;
    L_0x00b5:
        r7 = r12.charAt(r6);
        if (r7 < r14) goto L_0x00bf;
    L_0x00bb:
        r14 = 57;
        if (r7 <= r14) goto L_0x00d6;
    L_0x00bf:
        r14 = 97;
        if (r7 < r14) goto L_0x00c7;
    L_0x00c3:
        r14 = 122; // 0x7a float:1.71E-43 double:6.03E-322;
        if (r7 <= r14) goto L_0x00d6;
    L_0x00c7:
        r14 = 65;
        if (r7 < r14) goto L_0x00cf;
    L_0x00cb:
        r14 = 90;
        if (r7 <= r14) goto L_0x00d6;
    L_0x00cf:
        r14 = 95;
        if (r7 == r14) goto L_0x00d6;
    L_0x00d3:
        r12 = "";
        goto L_0x00de;
    L_0x00d6:
        r6 = r6 + 1;
        r7 = 1;
        r14 = 48;
        goto L_0x00af;
    L_0x00dc:
        r12 = "";
    L_0x00de:
        r0.searchForContextBot(r12, r15);
        goto L_0x00e5;
    L_0x00e2:
        r0.searchForContextBot(r8, r8);
    L_0x00e5:
        r6 = r0.foundContextBot;
        if (r6 == 0) goto L_0x00ea;
    L_0x00e9:
        return;
    L_0x00ea:
        r6 = r0.currentAccount;
        r6 = org.telegram.messenger.MessagesController.getInstance(r6);
        r7 = -1;
        if (r4 == 0) goto L_0x0108;
    L_0x00f3:
        r12 = 1;
        r13 = r1.substring(r12);
        r9.append(r13);
        r12 = 0;
        r0.resultStartPosition = r12;
        r12 = r9.length();
        r0.resultLength = r12;
        r10 = 0;
        r12 = r11;
        goto L_0x01ec;
    L_0x0108:
        r12 = r11;
        r11 = r5;
    L_0x010a:
        if (r11 < 0) goto L_0x01ec;
    L_0x010c:
        r13 = r27.length();
        if (r11 < r13) goto L_0x0117;
    L_0x0113:
        r15 = 48;
        goto L_0x01e8;
    L_0x0117:
        r13 = r1.charAt(r11);
        if (r11 == 0) goto L_0x0136;
    L_0x011d:
        r14 = r11 + -1;
        r14 = r1.charAt(r14);
        r15 = 32;
        if (r14 == r15) goto L_0x0136;
    L_0x0127:
        r14 = r11 + -1;
        r14 = r1.charAt(r14);
        r15 = 10;
        if (r14 != r15) goto L_0x0132;
    L_0x0131:
        goto L_0x0136;
    L_0x0132:
        r14 = 64;
        goto L_0x01bb;
    L_0x0136:
        r14 = 64;
        if (r13 != r14) goto L_0x0165;
    L_0x013a:
        r15 = r0.needUsernames;
        if (r15 != 0) goto L_0x0144;
    L_0x013e:
        r15 = r0.needBotContext;
        if (r15 == 0) goto L_0x01bb;
    L_0x0142:
        if (r11 != 0) goto L_0x01bb;
    L_0x0144:
        r14 = r0.info;
        if (r14 != 0) goto L_0x0157;
    L_0x0148:
        if (r11 == 0) goto L_0x0157;
    L_0x014a:
        r0.lastText = r1;
        r0.lastPosition = r2;
        r0.messages = r3;
        r8 = r0.delegate;
        r14 = 0;
        r8.needChangePanelVisibility(r14);
        return;
    L_0x0157:
        r7 = r11;
        r10 = 0;
        r0.resultStartPosition = r11;
        r14 = r9.length();
        r15 = 1;
        r14 = r14 + r15;
        r0.resultLength = r14;
        goto L_0x01ec;
    L_0x0165:
        r15 = 35;
        if (r13 != r15) goto L_0x018f;
    L_0x0169:
        r14 = r0.searchAdapterHelper;
        r14 = r14.loadRecentHashtags();
        if (r14 == 0) goto L_0x0182;
    L_0x0171:
        r10 = 1;
        r0.resultStartPosition = r11;
        r14 = r9.length();
        r15 = 1;
        r14 = r14 + r15;
        r0.resultLength = r14;
        r14 = 0;
        r9.insert(r14, r13);
        goto L_0x01ec;
    L_0x0182:
        r14 = 0;
        r0.lastText = r1;
        r0.lastPosition = r2;
        r0.messages = r3;
        r8 = r0.delegate;
        r8.needChangePanelVisibility(r14);
        return;
    L_0x018f:
        if (r11 != 0) goto L_0x01a5;
    L_0x0191:
        r15 = r0.botInfo;
        if (r15 == 0) goto L_0x01a5;
    L_0x0195:
        r15 = 47;
        if (r13 != r15) goto L_0x01a5;
    L_0x0199:
        r10 = 2;
        r0.resultStartPosition = r11;
        r14 = r9.length();
        r15 = 1;
        r14 = r14 + r15;
        r0.resultLength = r14;
        goto L_0x01ec;
    L_0x01a5:
        r15 = 58;
        if (r13 != r15) goto L_0x01bb;
    L_0x01a9:
        r15 = r9.length();
        if (r15 <= 0) goto L_0x01bb;
    L_0x01af:
        r10 = 3;
        r0.resultStartPosition = r11;
        r14 = r9.length();
        r15 = 1;
        r14 = r14 + r15;
        r0.resultLength = r14;
        goto L_0x01ec;
    L_0x01bb:
        r15 = 48;
        if (r13 < r15) goto L_0x01c7;
    L_0x01bf:
        r14 = 57;
        if (r13 <= r14) goto L_0x01c4;
    L_0x01c3:
        goto L_0x01c9;
    L_0x01c4:
        r14 = 95;
        goto L_0x01e4;
    L_0x01c7:
        r14 = 57;
    L_0x01c9:
        r14 = 97;
        if (r13 < r14) goto L_0x01d2;
    L_0x01cd:
        r14 = 122; // 0x7a float:1.71E-43 double:6.03E-322;
        if (r13 <= r14) goto L_0x01c4;
    L_0x01d1:
        goto L_0x01d4;
    L_0x01d2:
        r14 = 122; // 0x7a float:1.71E-43 double:6.03E-322;
    L_0x01d4:
        r14 = 65;
        if (r13 < r14) goto L_0x01dd;
    L_0x01d8:
        r14 = 90;
        if (r13 <= r14) goto L_0x01c4;
    L_0x01dc:
        goto L_0x01df;
    L_0x01dd:
        r14 = 90;
    L_0x01df:
        r14 = 95;
        if (r13 == r14) goto L_0x01e4;
    L_0x01e3:
        r12 = 1;
    L_0x01e4:
        r14 = 0;
        r9.insert(r14, r13);
    L_0x01e8:
        r11 = r11 + -1;
        goto L_0x010a;
    L_0x01ec:
        r11 = -1;
        if (r10 != r11) goto L_0x01f6;
    L_0x01ef:
        r8 = r0.delegate;
        r11 = 0;
        r8.needChangePanelVisibility(r11);
        return;
    L_0x01f6:
        if (r10 != 0) goto L_0x041b;
    L_0x01f8:
        r11 = new java.util.ArrayList;
        r11.<init>();
        r13 = 0;
    L_0x01fe:
        r14 = 100;
        r15 = r29.size();
        r14 = java.lang.Math.min(r14, r15);
        if (r13 >= r14) goto L_0x0228;
    L_0x020a:
        r14 = r3.get(r13);
        r14 = (org.telegram.messenger.MessageObject) r14;
        r14 = r14.messageOwner;
        r14 = r14.from_id;
        r15 = java.lang.Integer.valueOf(r14);
        r15 = r11.contains(r15);
        if (r15 != 0) goto L_0x0225;
    L_0x021e:
        r15 = java.lang.Integer.valueOf(r14);
        r11.add(r15);
    L_0x0225:
        r13 = r13 + 1;
        goto L_0x01fe;
    L_0x0228:
        r13 = r9.toString();
        r13 = r13.toLowerCase();
        r14 = 32;
        r14 = r13.indexOf(r14);
        if (r14 < 0) goto L_0x023a;
    L_0x0238:
        r14 = 1;
        goto L_0x023b;
    L_0x023a:
        r14 = 0;
    L_0x023b:
        r15 = new java.util.ArrayList;
        r15.<init>();
        r8 = new android.util.SparseArray;
        r8.<init>();
        r1 = new android.util.SparseArray;
        r1.<init>();
        r2 = r0.currentAccount;
        r2 = org.telegram.messenger.DataQuery.getInstance(r2);
        r2 = r2.inlineBots;
        if (r4 != 0) goto L_0x02c8;
    L_0x0254:
        r3 = r0.needBotContext;
        if (r3 == 0) goto L_0x02c8;
    L_0x0258:
        if (r7 != 0) goto L_0x02c8;
    L_0x025a:
        r3 = r2.isEmpty();
        if (r3 != 0) goto L_0x02c8;
    L_0x0260:
        r3 = 0;
        r16 = r3;
        r3 = 0;
    L_0x0264:
        r21 = r5;
        r5 = r2.size();
        if (r3 >= r5) goto L_0x02c5;
    L_0x026c:
        r5 = r2.get(r3);
        r5 = (org.telegram.tgnet.TLRPC.TL_topPeer) r5;
        r5 = r5.peer;
        r5 = r5.user_id;
        r5 = java.lang.Integer.valueOf(r5);
        r5 = r6.getUser(r5);
        if (r5 != 0) goto L_0x0284;
    L_0x0281:
        r22 = r2;
        goto L_0x02be;
    L_0x0284:
        r22 = r2;
        r2 = r5.username;
        if (r2 == 0) goto L_0x02b4;
    L_0x028a:
        r2 = r5.username;
        r2 = r2.length();
        if (r2 <= 0) goto L_0x02b4;
    L_0x0292:
        r2 = r13.length();
        if (r2 <= 0) goto L_0x02a4;
    L_0x0298:
        r2 = r5.username;
        r2 = r2.toLowerCase();
        r2 = r2.startsWith(r13);
        if (r2 != 0) goto L_0x02aa;
    L_0x02a4:
        r2 = r13.length();
        if (r2 != 0) goto L_0x02b4;
    L_0x02aa:
        r15.add(r5);
        r2 = r5.id;
        r8.put(r2, r5);
        r16 = r16 + 1;
    L_0x02b4:
        r2 = r16;
        r23 = r5;
        r5 = 5;
        if (r2 != r5) goto L_0x02bc;
    L_0x02bb:
        goto L_0x02cc;
    L_0x02bc:
        r16 = r2;
    L_0x02be:
        r3 = r3 + 1;
        r5 = r21;
        r2 = r22;
        goto L_0x0264;
    L_0x02c5:
        r22 = r2;
        goto L_0x02cc;
    L_0x02c8:
        r22 = r2;
        r21 = r5;
    L_0x02cc:
        r2 = r0.parentFragment;
        if (r2 == 0) goto L_0x02d7;
    L_0x02d0:
        r2 = r0.parentFragment;
        r2 = r2.getCurrentChat();
    L_0x02d6:
        goto L_0x02e9;
    L_0x02d7:
        r2 = r0.info;
        if (r2 == 0) goto L_0x02e8;
    L_0x02db:
        r2 = r0.info;
        r2 = r2.id;
        r2 = java.lang.Integer.valueOf(r2);
        r2 = r6.getChat(r2);
        goto L_0x02d6;
    L_0x02e8:
        r2 = 0;
    L_0x02e9:
        if (r2 == 0) goto L_0x03d8;
    L_0x02eb:
        r3 = r0.info;
        if (r3 == 0) goto L_0x03d8;
    L_0x02ef:
        r3 = r0.info;
        r3 = r3.participants;
        if (r3 == 0) goto L_0x03d8;
    L_0x02f5:
        r3 = org.telegram.messenger.ChatObject.isChannel(r2);
        if (r3 == 0) goto L_0x0304;
    L_0x02fb:
        r3 = r2.megagroup;
        if (r3 == 0) goto L_0x0300;
    L_0x02ff:
        goto L_0x0304;
    L_0x0300:
        r24 = r7;
        goto L_0x03da;
    L_0x0304:
        r20 = 0;
    L_0x0306:
        r3 = r20;
        r5 = r0.info;
        r5 = r5.participants;
        r5 = r5.participants;
        r5 = r5.size();
        if (r3 >= r5) goto L_0x03d8;
    L_0x0314:
        r5 = r0.info;
        r5 = r5.participants;
        r5 = r5.participants;
        r5 = r5.get(r3);
        r5 = (org.telegram.tgnet.TLRPC.ChatParticipant) r5;
        r24 = r7;
        r7 = r5.user_id;
        r7 = java.lang.Integer.valueOf(r7);
        r7 = r6.getUser(r7);
        if (r7 == 0) goto L_0x03d0;
    L_0x032e:
        if (r4 != 0) goto L_0x0336;
    L_0x0330:
        r16 = org.telegram.messenger.UserObject.isUserSelf(r7);
        if (r16 != 0) goto L_0x03d0;
    L_0x0336:
        r4 = r7.id;
        r4 = r8.indexOfKey(r4);
        if (r4 < 0) goto L_0x0340;
    L_0x033e:
        goto L_0x03d0;
    L_0x0340:
        r4 = r13.length();
        if (r4 != 0) goto L_0x034f;
    L_0x0346:
        r4 = r7.deleted;
        if (r4 != 0) goto L_0x03d0;
    L_0x034a:
        r15.add(r7);
        goto L_0x03d0;
    L_0x034f:
        r4 = r7.username;
        if (r4 == 0) goto L_0x0370;
    L_0x0353:
        r4 = r7.username;
        r4 = r4.length();
        if (r4 <= 0) goto L_0x0370;
    L_0x035b:
        r4 = r7.username;
        r4 = r4.toLowerCase();
        r4 = r4.startsWith(r13);
        if (r4 == 0) goto L_0x0370;
    L_0x0367:
        r15.add(r7);
        r4 = r7.id;
        r1.put(r4, r7);
        goto L_0x03d0;
    L_0x0370:
        r4 = r7.first_name;
        if (r4 == 0) goto L_0x0391;
    L_0x0374:
        r4 = r7.first_name;
        r4 = r4.length();
        if (r4 <= 0) goto L_0x0391;
    L_0x037c:
        r4 = r7.first_name;
        r4 = r4.toLowerCase();
        r4 = r4.startsWith(r13);
        if (r4 == 0) goto L_0x0391;
    L_0x0388:
        r15.add(r7);
        r4 = r7.id;
        r1.put(r4, r7);
        goto L_0x03d0;
    L_0x0391:
        r4 = r7.last_name;
        if (r4 == 0) goto L_0x03b2;
    L_0x0395:
        r4 = r7.last_name;
        r4 = r4.length();
        if (r4 <= 0) goto L_0x03b2;
    L_0x039d:
        r4 = r7.last_name;
        r4 = r4.toLowerCase();
        r4 = r4.startsWith(r13);
        if (r4 == 0) goto L_0x03b2;
    L_0x03a9:
        r15.add(r7);
        r4 = r7.id;
        r1.put(r4, r7);
        goto L_0x03d0;
    L_0x03b2:
        if (r14 == 0) goto L_0x03d0;
    L_0x03b4:
        r4 = r7.first_name;
        r25 = r5;
        r5 = r7.last_name;
        r4 = org.telegram.messenger.ContactsController.formatName(r4, r5);
        r4 = r4.toLowerCase();
        r4 = r4.startsWith(r13);
        if (r4 == 0) goto L_0x03d0;
    L_0x03c8:
        r15.add(r7);
        r4 = r7.id;
        r1.put(r4, r7);
    L_0x03d0:
        r20 = r3 + 1;
        r7 = r24;
        r4 = r30;
        goto L_0x0306;
    L_0x03d8:
        r24 = r7;
    L_0x03da:
        r3 = 0;
        r0.searchResultHashtags = r3;
        r0.searchResultCommands = r3;
        r0.searchResultCommandsHelp = r3;
        r0.searchResultCommandsUsers = r3;
        r0.searchResultSuggestions = r3;
        r0.searchResultUsernames = r15;
        r0.searchResultUsernamesMap = r1;
        if (r2 == 0) goto L_0x0401;
    L_0x03eb:
        r3 = r2.megagroup;
        if (r3 == 0) goto L_0x0401;
    L_0x03ef:
        r3 = r13.length();
        if (r3 <= 0) goto L_0x0401;
    L_0x03f5:
        r3 = new org.telegram.ui.Adapters.MentionsAdapter$9;
        r3.<init>(r2, r13, r6);
        r0.searchGlobalRunnable = r3;
        r4 = 200; // 0xc8 float:2.8E-43 double:9.9E-322;
        org.telegram.messenger.AndroidUtilities.runOnUIThread(r3, r4);
    L_0x0401:
        r3 = r0.searchResultUsernames;
        r4 = new org.telegram.ui.Adapters.MentionsAdapter$10;
        r4.<init>(r8, r11);
        java.util.Collections.sort(r3, r4);
        r26.notifyDataSetChanged();
        r3 = r0.delegate;
        r4 = r15.isEmpty();
        r5 = 1;
        r4 = r4 ^ r5;
        r3.needChangePanelVisibility(r4);
        goto L_0x0572;
    L_0x041b:
        r21 = r5;
        r24 = r7;
        r5 = 1;
        if (r10 != r5) goto L_0x047a;
    L_0x0422:
        r1 = new java.util.ArrayList;
        r1.<init>();
        r2 = r9.toString();
        r2 = r2.toLowerCase();
        r3 = r0.searchAdapterHelper;
        r3 = r3.getHashtags();
        r20 = 0;
    L_0x0437:
        r4 = r20;
        r5 = r3.size();
        if (r4 >= r5) goto L_0x045b;
    L_0x043f:
        r5 = r3.get(r4);
        r5 = (org.telegram.ui.Adapters.SearchAdapterHelper.HashtagObject) r5;
        if (r5 == 0) goto L_0x0458;
    L_0x0447:
        r7 = r5.hashtag;
        if (r7 == 0) goto L_0x0458;
    L_0x044b:
        r7 = r5.hashtag;
        r7 = r7.startsWith(r2);
        if (r7 == 0) goto L_0x0458;
    L_0x0453:
        r7 = r5.hashtag;
        r1.add(r7);
    L_0x0458:
        r20 = r4 + 1;
        goto L_0x0437;
    L_0x045b:
        r0.searchResultHashtags = r1;
        r4 = 0;
        r0.searchResultUsernames = r4;
        r0.searchResultUsernamesMap = r4;
        r0.searchResultCommands = r4;
        r0.searchResultCommandsHelp = r4;
        r0.searchResultCommandsUsers = r4;
        r0.searchResultSuggestions = r4;
        r26.notifyDataSetChanged();
        r4 = r0.delegate;
        r5 = r1.isEmpty();
        r7 = 1;
        r5 = r5 ^ r7;
        r4.needChangePanelVisibility(r5);
        goto L_0x0572;
    L_0x047a:
        r1 = 2;
        if (r10 != r1) goto L_0x0510;
    L_0x047d:
        r1 = new java.util.ArrayList;
        r1.<init>();
        r2 = new java.util.ArrayList;
        r2.<init>();
        r3 = new java.util.ArrayList;
        r3.<init>();
        r4 = r9.toString();
        r4 = r4.toLowerCase();
        r5 = 0;
    L_0x0495:
        r7 = r0.botInfo;
        r7 = r7.size();
        if (r5 >= r7) goto L_0x04f2;
    L_0x049d:
        r7 = r0.botInfo;
        r7 = r7.valueAt(r5);
        r7 = (org.telegram.tgnet.TLRPC.BotInfo) r7;
        r8 = 0;
    L_0x04a6:
        r11 = r7.commands;
        r11 = r11.size();
        if (r8 >= r11) goto L_0x04ef;
    L_0x04ae:
        r11 = r7.commands;
        r11 = r11.get(r8);
        r11 = (org.telegram.tgnet.TLRPC.TL_botCommand) r11;
        if (r11 == 0) goto L_0x04ec;
    L_0x04b8:
        r13 = r11.command;
        if (r13 == 0) goto L_0x04ec;
    L_0x04bc:
        r13 = r11.command;
        r13 = r13.startsWith(r4);
        if (r13 == 0) goto L_0x04ec;
    L_0x04c4:
        r13 = new java.lang.StringBuilder;
        r13.<init>();
        r14 = "/";
        r13.append(r14);
        r14 = r11.command;
        r13.append(r14);
        r13 = r13.toString();
        r1.add(r13);
        r13 = r11.description;
        r2.add(r13);
        r13 = r7.user_id;
        r13 = java.lang.Integer.valueOf(r13);
        r13 = r6.getUser(r13);
        r3.add(r13);
    L_0x04ec:
        r8 = r8 + 1;
        goto L_0x04a6;
    L_0x04ef:
        r5 = r5 + 1;
        goto L_0x0495;
    L_0x04f2:
        r5 = 0;
        r0.searchResultHashtags = r5;
        r0.searchResultUsernames = r5;
        r0.searchResultUsernamesMap = r5;
        r0.searchResultSuggestions = r5;
        r0.searchResultCommands = r1;
        r0.searchResultCommandsHelp = r2;
        r0.searchResultCommandsUsers = r3;
        r26.notifyDataSetChanged();
        r5 = r0.delegate;
        r7 = r1.isEmpty();
        r8 = 1;
        r7 = r7 ^ r8;
        r5.needChangePanelVisibility(r7);
        goto L_0x0572;
    L_0x0510:
        r8 = 1;
        r1 = 3;
        if (r10 != r1) goto L_0x0572;
    L_0x0514:
        if (r12 != 0) goto L_0x056c;
    L_0x0516:
        r1 = r9.toString();
        r1 = org.telegram.messenger.Emoji.getSuggestion(r1);
        if (r1 == 0) goto L_0x0550;
    L_0x0520:
        r2 = new java.util.ArrayList;
        r2.<init>();
        r0.searchResultSuggestions = r2;
        r2 = 0;
    L_0x0528:
        r3 = r1.length;
        if (r2 >= r3) goto L_0x0543;
    L_0x052b:
        r3 = r1[r2];
        r3 = (org.telegram.messenger.EmojiSuggestion) r3;
        r4 = r3.emoji;
        r5 = "️";
        r7 = "";
        r4 = r4.replace(r5, r7);
        r3.emoji = r4;
        r4 = r0.searchResultSuggestions;
        r4.add(r3);
        r2 = r2 + 1;
        goto L_0x0528;
    L_0x0543:
        org.telegram.messenger.Emoji.loadRecentEmoji();
        r2 = r0.searchResultSuggestions;
        r3 = new org.telegram.ui.Adapters.MentionsAdapter$11;
        r3.<init>();
        java.util.Collections.sort(r2, r3);
    L_0x0550:
        r2 = 0;
        r0.searchResultHashtags = r2;
        r0.searchResultUsernames = r2;
        r0.searchResultUsernamesMap = r2;
        r0.searchResultCommands = r2;
        r0.searchResultCommandsHelp = r2;
        r0.searchResultCommandsUsers = r2;
        r26.notifyDataSetChanged();
        r2 = r0.delegate;
        r3 = r0.searchResultSuggestions;
        if (r3 == 0) goto L_0x0567;
    L_0x0566:
        goto L_0x0568;
    L_0x0567:
        r8 = 0;
    L_0x0568:
        r2.needChangePanelVisibility(r8);
        goto L_0x0572;
    L_0x056c:
        r1 = r0.delegate;
        r2 = 0;
        r1.needChangePanelVisibility(r2);
    L_0x0572:
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.telegram.ui.Adapters.MentionsAdapter.searchUsernameOrHashtag(java.lang.String, int, java.util.ArrayList, boolean):void");
    }

    public int getResultStartPosition() {
        return this.resultStartPosition;
    }

    public int getResultLength() {
        return this.resultLength;
    }

    public ArrayList<BotInlineResult> getSearchResultBotContext() {
        return this.searchResultBotContext;
    }

    public int getItemCount() {
        int i = 1;
        if (this.foundContextBot != null && !this.inlineMediaEnabled) {
            return 1;
        }
        if (this.searchResultBotContext != null) {
            int size = this.searchResultBotContext.size();
            if (this.searchResultBotContextSwitch == null) {
                i = 0;
            }
            return size + i;
        } else if (this.searchResultUsernames != null) {
            return this.searchResultUsernames.size();
        } else {
            if (this.searchResultHashtags != null) {
                return this.searchResultHashtags.size();
            }
            if (this.searchResultCommands != null) {
                return this.searchResultCommands.size();
            }
            if (this.searchResultSuggestions != null) {
                return this.searchResultSuggestions.size();
            }
            return 0;
        }
    }

    public int getItemViewType(int position) {
        if (this.foundContextBot != null && !this.inlineMediaEnabled) {
            return 3;
        }
        if (this.searchResultBotContext == null) {
            return 0;
        }
        if (position != 0 || this.searchResultBotContextSwitch == null) {
            return 1;
        }
        return 2;
    }

    public void addHashtagsFromMessage(CharSequence message) {
        this.searchAdapterHelper.addHashtagsFromMessage(message);
    }

    public int getItemPosition(int i) {
        if (this.searchResultBotContext == null || this.searchResultBotContextSwitch == null) {
            return i;
        }
        return i - 1;
    }

    public Object getItem(int i) {
        if (this.searchResultBotContext != null) {
            if (this.searchResultBotContextSwitch != null) {
                if (i == 0) {
                    return this.searchResultBotContextSwitch;
                }
                i--;
            }
            if (i >= 0) {
                if (i < this.searchResultBotContext.size()) {
                    return this.searchResultBotContext.get(i);
                }
            }
            return null;
        } else if (this.searchResultUsernames != null) {
            if (i >= 0) {
                if (i < this.searchResultUsernames.size()) {
                    return this.searchResultUsernames.get(i);
                }
            }
            return null;
        } else if (this.searchResultHashtags != null) {
            if (i >= 0) {
                if (i < this.searchResultHashtags.size()) {
                    return this.searchResultHashtags.get(i);
                }
            }
            return null;
        } else if (this.searchResultSuggestions != null) {
            if (i >= 0) {
                if (i < this.searchResultSuggestions.size()) {
                    return this.searchResultSuggestions.get(i);
                }
            }
            return null;
        } else {
            if (this.searchResultCommands != null && i >= 0) {
                if (i < this.searchResultCommands.size()) {
                    if (this.searchResultCommandsUsers == null || (this.botsCount == 1 && !(this.info instanceof TL_channelFull))) {
                        return this.searchResultCommands.get(i);
                    }
                    if (this.searchResultCommandsUsers.get(i) != null) {
                        String str = "%s@%s";
                        Object[] objArr = new Object[2];
                        objArr[0] = this.searchResultCommands.get(i);
                        objArr[1] = this.searchResultCommandsUsers.get(i) != null ? ((User) this.searchResultCommandsUsers.get(i)).username : TtmlNode.ANONYMOUS_REGION_ID;
                        return String.format(str, objArr);
                    }
                    return String.format("%s", new Object[]{this.searchResultCommands.get(i)});
                }
            }
            return null;
        }
    }

    public boolean isLongClickEnabled() {
        if (this.searchResultHashtags == null) {
            if (this.searchResultCommands == null) {
                return false;
            }
        }
        return true;
    }

    public boolean isBotCommands() {
        return this.searchResultCommands != null;
    }

    public boolean isBotContext() {
        return this.searchResultBotContext != null;
    }

    public boolean isBannedInline() {
        return (this.foundContextBot == null || this.inlineMediaEnabled) ? false : true;
    }

    public boolean isMediaLayout() {
        return this.contextMedia;
    }

    public boolean isEnabled(ViewHolder holder) {
        if (this.foundContextBot != null) {
            if (!this.inlineMediaEnabled) {
                return false;
            }
        }
        return true;
    }

    public void onBindViewHolder(ViewHolder holder, int position) {
        boolean z = false;
        if (holder.getItemViewType() == 3) {
            TextView textView = holder.itemView;
            Chat chat = this.parentFragment.getCurrentChat();
            if (chat != null) {
                if (AndroidUtilities.isBannedForever(chat.banned_rights.until_date)) {
                    textView.setText(LocaleController.getString("AttachInlineRestrictedForever", R.string.AttachInlineRestrictedForever));
                } else {
                    textView.setText(LocaleController.formatString("AttachInlineRestricted", R.string.AttachInlineRestricted, LocaleController.formatDateForBan((long) chat.banned_rights.until_date)));
                }
            }
        } else if (this.searchResultBotContext != null) {
            boolean hasTop = this.searchResultBotContextSwitch != null;
            if (holder.getItemViewType() != 2) {
                if (hasTop) {
                    position--;
                }
                ContextLinkCell contextLinkCell = (ContextLinkCell) holder.itemView;
                BotInlineResult botInlineResult = (BotInlineResult) this.searchResultBotContext.get(position);
                boolean z2 = this.contextMedia;
                boolean z3 = position != this.searchResultBotContext.size() - 1;
                if (hasTop && position == 0) {
                    z = true;
                }
                contextLinkCell.setLink(botInlineResult, z2, z3, z);
            } else if (hasTop) {
                ((BotSwitchCell) holder.itemView).setText(this.searchResultBotContextSwitch.text);
            }
        } else if (this.searchResultUsernames != null) {
            ((MentionCell) holder.itemView).setUser((User) this.searchResultUsernames.get(position));
        } else if (this.searchResultHashtags != null) {
            ((MentionCell) holder.itemView).setText((String) this.searchResultHashtags.get(position));
        } else if (this.searchResultSuggestions != null) {
            ((MentionCell) holder.itemView).setEmojiSuggestion((EmojiSuggestion) this.searchResultSuggestions.get(position));
        } else if (this.searchResultCommands != null) {
            ((MentionCell) holder.itemView).setBotCommand((String) this.searchResultCommands.get(position), (String) this.searchResultCommandsHelp.get(position), this.searchResultCommandsUsers != null ? (User) this.searchResultCommandsUsers.get(position) : null);
        }
    }

    public void onRequestPermissionsResultFragment(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode != 2 || this.foundContextBot == null || !this.foundContextBot.bot_inline_geo) {
            return;
        }
        if (grantResults.length <= 0 || grantResults[0] != 0) {
            onLocationUnavailable();
        } else {
            this.locationProvider.start();
        }
    }
}
