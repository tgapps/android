package org.telegram.ui;

import android.app.Activity;
import android.app.ActivityManager.TaskDescription;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Configuration;
import android.graphics.Point;
import android.graphics.Shader.TileMode;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.StatFs;
import android.view.ActionMode;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.ApplicationLoader;
import org.telegram.messenger.BuildVars;
import org.telegram.messenger.ChatObject;
import org.telegram.messenger.ContactsController;
import org.telegram.messenger.ContactsController.Contact;
import org.telegram.messenger.DataQuery;
import org.telegram.messenger.FileLoader;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.LocaleController.LocaleInfo;
import org.telegram.messenger.LocationController.SharingLocationInfo;
import org.telegram.messenger.MediaController;
import org.telegram.messenger.MessageObject;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.MessagesStorage;
import org.telegram.messenger.NotificationCenter;
import org.telegram.messenger.NotificationCenter.NotificationCenterDelegate;
import org.telegram.messenger.NotificationsController;
import org.telegram.messenger.SendMessagesHelper;
import org.telegram.messenger.SendMessagesHelper.SendingMediaInfo;
import org.telegram.messenger.SharedConfig;
import org.telegram.messenger.UserConfig;
import org.telegram.messenger.UserObject;
import org.telegram.messenger.Utilities;
import org.telegram.messenger.beta.R;
import org.telegram.messenger.browser.Browser;
import org.telegram.messenger.exoplayer2.trackselection.AdaptiveTrackSelection;
import org.telegram.messenger.support.widget.DefaultItemAnimator;
import org.telegram.messenger.support.widget.LinearLayoutManager;
import org.telegram.messenger.support.widget.RecyclerView.Adapter;
import org.telegram.messenger.support.widget.helper.ItemTouchHelper.Callback;
import org.telegram.tgnet.ConnectionsManager;
import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC.Chat;
import org.telegram.tgnet.TLRPC.ChatInvite;
import org.telegram.tgnet.TLRPC.InputStickerSet;
import org.telegram.tgnet.TLRPC.LangPackString;
import org.telegram.tgnet.TLRPC.MessageMedia;
import org.telegram.tgnet.TLRPC.TL_contacts_resolveUsername;
import org.telegram.tgnet.TLRPC.TL_contacts_resolvedPeer;
import org.telegram.tgnet.TLRPC.TL_error;
import org.telegram.tgnet.TLRPC.TL_inputGameShortName;
import org.telegram.tgnet.TLRPC.TL_inputMediaGame;
import org.telegram.tgnet.TLRPC.TL_inputStickerSetShortName;
import org.telegram.tgnet.TLRPC.TL_langpack_getStrings;
import org.telegram.tgnet.TLRPC.TL_messages_checkChatInvite;
import org.telegram.tgnet.TLRPC.TL_messages_importChatInvite;
import org.telegram.tgnet.TLRPC.TL_webPage;
import org.telegram.tgnet.TLRPC.Updates;
import org.telegram.tgnet.TLRPC.User;
import org.telegram.tgnet.TLRPC.Vector;
import org.telegram.ui.ActionBar.ActionBarLayout;
import org.telegram.ui.ActionBar.ActionBarLayout.ActionBarLayoutDelegate;
import org.telegram.ui.ActionBar.AlertDialog;
import org.telegram.ui.ActionBar.AlertDialog.Builder;
import org.telegram.ui.ActionBar.BaseFragment;
import org.telegram.ui.ActionBar.DrawerLayoutContainer;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.ActionBar.Theme.ThemeInfo;
import org.telegram.ui.Adapters.DrawerLayoutAdapter;
import org.telegram.ui.Cells.DrawerAddCell;
import org.telegram.ui.Cells.DrawerUserCell;
import org.telegram.ui.Cells.LanguageCell;
import org.telegram.ui.Components.AlertsCreator;
import org.telegram.ui.Components.EmbedBottomSheet;
import org.telegram.ui.Components.JoinGroupAlert;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.ui.Components.PasscodeView;
import org.telegram.ui.Components.PasscodeView.PasscodeViewDelegate;
import org.telegram.ui.Components.PipRoundVideoView;
import org.telegram.ui.Components.RecyclerListView;
import org.telegram.ui.Components.RecyclerListView.OnItemClickListener;
import org.telegram.ui.Components.SharingLocationsAlert.SharingLocationsAlertDelegate;
import org.telegram.ui.Components.StickersAlert;
import org.telegram.ui.Components.ThemeEditorView;
import org.telegram.ui.DialogsActivity.DialogsActivityDelegate;
import org.telegram.ui.LocationActivity.LocationActivityDelegate;

public class LaunchActivity extends Activity implements NotificationCenterDelegate, ActionBarLayoutDelegate, DialogsActivityDelegate {
    private static ArrayList<BaseFragment> layerFragmentsStack = new ArrayList();
    private static ArrayList<BaseFragment> mainFragmentsStack = new ArrayList();
    private static ArrayList<BaseFragment> rightFragmentsStack = new ArrayList();
    private ActionBarLayout actionBarLayout;
    private View backgroundTablet;
    private ArrayList<User> contactsToSend;
    private int currentAccount;
    private int currentConnectionState;
    private String documentsMimeType;
    private ArrayList<String> documentsOriginalPathsArray;
    private ArrayList<String> documentsPathsArray;
    private ArrayList<Uri> documentsUrisArray;
    private DrawerLayoutAdapter drawerLayoutAdapter;
    protected DrawerLayoutContainer drawerLayoutContainer;
    private HashMap<String, String> englishLocaleStrings;
    private boolean finished;
    private ActionBarLayout layersActionBarLayout;
    private boolean loadingLocaleDialog;
    private AlertDialog localeDialog;
    private Runnable lockRunnable;
    private OnGlobalLayoutListener onGlobalLayoutListener;
    private Intent passcodeSaveIntent;
    private boolean passcodeSaveIntentIsNew;
    private boolean passcodeSaveIntentIsRestore;
    private PasscodeView passcodeView;
    private ArrayList<SendingMediaInfo> photoPathsArray;
    private ActionBarLayout rightActionBarLayout;
    private String sendingText;
    private FrameLayout shadowTablet;
    private FrameLayout shadowTabletSide;
    private RecyclerListView sideMenu;
    private HashMap<String, String> systemLocaleStrings;
    private boolean tabletFullSize;
    private String videoPath;
    private AlertDialog visibleDialog;

    class AnonymousClass7 implements Runnable {
        final /* synthetic */ Bundle val$args;

        AnonymousClass7(Bundle bundle) {
            this.val$args = bundle;
        }

        public void run() {
            LaunchActivity.this.presentFragment(new CancelAccountDeletionActivity(this.val$args));
        }
    }

    private class VcardData {
        String name;
        ArrayList<String> phones;

        private VcardData() {
            this.phones = new ArrayList();
        }
    }

    class AnonymousClass8 implements SharingLocationsAlertDelegate {
        final /* synthetic */ int[] val$intentAccount;

        AnonymousClass8(int[] iArr) {
            this.val$intentAccount = iArr;
        }

        public void didSelectLocation(SharingLocationInfo info) {
            this.val$intentAccount[0] = info.messageObject.currentAccount;
            LaunchActivity.this.switchToAccount(this.val$intentAccount[0], true);
            LocationActivity locationActivity = new LocationActivity(2);
            locationActivity.setMessageObject(info.messageObject);
            final long dialog_id = info.messageObject.getDialogId();
            locationActivity.setDelegate(new LocationActivityDelegate() {
                public void didSelectLocation(MessageMedia location, int live) {
                    SendMessagesHelper.getInstance(AnonymousClass8.this.val$intentAccount[0]).sendMessage(location, dialog_id, null, null, null);
                }
            });
            LaunchActivity.this.presentFragment(locationActivity);
        }
    }

    private boolean handleIntent(android.content.Intent r1, boolean r2, boolean r3, boolean r4) {
        /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.DecodeException: Load method exception in method: org.telegram.ui.LaunchActivity.handleIntent(android.content.Intent, boolean, boolean, boolean):boolean
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
        r14 = r63;
        r15 = r64;
        r13 = r66;
        r1 = org.telegram.messenger.AndroidUtilities.handleProxyIntent(r63, r64);
        r12 = 1;
        if (r1 == 0) goto L_0x000e;
    L_0x000d:
        return r12;
    L_0x000e:
        r1 = org.telegram.ui.PhotoViewer.hasInstance();
        r11 = 0;
        if (r1 == 0) goto L_0x0034;
    L_0x0015:
        r1 = org.telegram.ui.PhotoViewer.getInstance();
        r1 = r1.isVisible();
        if (r1 == 0) goto L_0x0034;
    L_0x001f:
        if (r15 == 0) goto L_0x002d;
    L_0x0021:
        r1 = "android.intent.action.MAIN";
        r2 = r64.getAction();
        r1 = r1.equals(r2);
        if (r1 != 0) goto L_0x0034;
    L_0x002d:
        r1 = org.telegram.ui.PhotoViewer.getInstance();
        r1.closePhoto(r11, r12);
    L_0x0034:
        r16 = r64.getFlags();
        r1 = new int[r12];
        r2 = "currentAccount";
        r3 = org.telegram.messenger.UserConfig.selectedAccount;
        r2 = r15.getIntExtra(r2, r3);
        r1[r11] = r2;
        r10 = r1;
        r1 = r10[r11];
        r14.switchToAccount(r1, r12);
        if (r67 != 0) goto L_0x006b;
    L_0x004c:
        r1 = org.telegram.messenger.AndroidUtilities.needShowPasscode(r12);
        if (r1 != 0) goto L_0x0056;
    L_0x0052:
        r1 = org.telegram.messenger.SharedConfig.isWaitingForPasscodeEnter;
        if (r1 == 0) goto L_0x006b;
    L_0x0056:
        r63.showPasscodeActivity();
        r14.passcodeSaveIntent = r15;
        r9 = r65;
        r14.passcodeSaveIntentIsNew = r9;
        r14.passcodeSaveIntentIsRestore = r13;
        r1 = r14.currentAccount;
        r1 = org.telegram.messenger.UserConfig.getInstance(r1);
        r1.saveConfig(r11);
        return r11;
    L_0x006b:
        r9 = r65;
        r17 = 0;
        r1 = java.lang.Integer.valueOf(r11);
        r2 = java.lang.Integer.valueOf(r11);
        r18 = java.lang.Integer.valueOf(r11);
        r3 = java.lang.Integer.valueOf(r11);
        r19 = java.lang.Integer.valueOf(r11);
        r20 = java.lang.Integer.valueOf(r11);
        r4 = 0;
        r6 = org.telegram.messenger.SharedConfig.directShare;
        r7 = 0;
        if (r6 == 0) goto L_0x00a6;
    L_0x008f:
        if (r15 == 0) goto L_0x00a2;
    L_0x0091:
        r6 = r64.getExtras();
        if (r6 == 0) goto L_0x00a2;
    L_0x0097:
        r6 = r64.getExtras();
        r12 = "dialogId";
        r22 = r6.getLong(r12, r7);
        goto L_0x00a4;
    L_0x00a2:
        r22 = r7;
    L_0x00a4:
        r4 = r22;
    L_0x00a6:
        r5 = r4;
        r22 = 0;
        r23 = 0;
        r24 = 0;
        r12 = 0;
        r14.photoPathsArray = r12;
        r14.videoPath = r12;
        r14.sendingText = r12;
        r14.documentsPathsArray = r12;
        r14.documentsOriginalPathsArray = r12;
        r14.documentsMimeType = r12;
        r14.documentsUrisArray = r12;
        r14.contactsToSend = r12;
        r4 = r14.currentAccount;
        r4 = org.telegram.messenger.UserConfig.getInstance(r4);
        r4 = r4.isClientActivated();
        if (r4 == 0) goto L_0x0beb;
    L_0x00ca:
        r4 = 1048576; // 0x100000 float:1.469368E-39 double:5.180654E-318;
        r4 = r16 & r4;
        if (r4 != 0) goto L_0x0beb;
    L_0x00d0:
        if (r15 == 0) goto L_0x0beb;
    L_0x00d2:
        r4 = r64.getAction();
        if (r4 == 0) goto L_0x0beb;
    L_0x00d8:
        if (r13 != 0) goto L_0x0beb;
    L_0x00da:
        r4 = "android.intent.action.SEND";
        r7 = r64.getAction();
        r4 = r4.equals(r7);
        if (r4 == 0) goto L_0x0490;
    L_0x00e6:
        r4 = 0;
        r7 = r64.getType();
        if (r7 == 0) goto L_0x035d;
    L_0x00ed:
        r8 = "text/x-vcard";
        r8 = r7.equals(r8);
        if (r8 == 0) goto L_0x035d;
    L_0x00f5:
        r8 = r64.getExtras();	 Catch:{ Exception -> 0x034b }
        r12 = "android.intent.extra.STREAM";	 Catch:{ Exception -> 0x034b }
        r8 = r8.get(r12);	 Catch:{ Exception -> 0x034b }
        r8 = (android.net.Uri) r8;	 Catch:{ Exception -> 0x034b }
        if (r8 == 0) goto L_0x033c;	 Catch:{ Exception -> 0x034b }
    L_0x0103:
        r12 = r63.getContentResolver();	 Catch:{ Exception -> 0x034b }
        r29 = r12.openInputStream(r8);	 Catch:{ Exception -> 0x034b }
        r30 = r29;	 Catch:{ Exception -> 0x034b }
        r11 = new java.util.ArrayList;	 Catch:{ Exception -> 0x034b }
        r11.<init>();	 Catch:{ Exception -> 0x034b }
        r29 = 0;
        r32 = r1;
        r1 = new java.io.BufferedReader;	 Catch:{ Exception -> 0x0331 }
        r33 = r2;
        r2 = new java.io.InputStreamReader;	 Catch:{ Exception -> 0x0328 }
        r34 = r3;
        r3 = "UTF-8";	 Catch:{ Exception -> 0x0321 }
        r35 = r4;
        r4 = r30;
        r2.<init>(r4, r3);	 Catch:{ Exception -> 0x031c }
        r1.<init>(r2);	 Catch:{ Exception -> 0x031c }
    L_0x012a:
        r2 = r1.readLine();	 Catch:{ Exception -> 0x031c }
        r3 = r2;	 Catch:{ Exception -> 0x031c }
        if (r2 == 0) goto L_0x02af;	 Catch:{ Exception -> 0x031c }
    L_0x0131:
        r2 = org.telegram.messenger.BuildVars.LOGS_ENABLED;	 Catch:{ Exception -> 0x031c }
        if (r2 == 0) goto L_0x013f;
    L_0x0135:
        org.telegram.messenger.FileLog.d(r3);	 Catch:{ Exception -> 0x0139 }
        goto L_0x013f;
    L_0x0139:
        r0 = move-exception;
        r1 = r0;
        r37 = r5;
        goto L_0x0357;
    L_0x013f:
        r2 = ":";	 Catch:{ Exception -> 0x031c }
        r2 = r3.split(r2);	 Catch:{ Exception -> 0x031c }
        r36 = r3;	 Catch:{ Exception -> 0x031c }
        r3 = r2.length;	 Catch:{ Exception -> 0x031c }
        r37 = r5;
        r5 = 2;
        if (r3 == r5) goto L_0x0151;
    L_0x014e:
        r5 = r37;
        goto L_0x012a;
    L_0x0151:
        r3 = 0;
        r6 = r2[r3];	 Catch:{ Exception -> 0x0319 }
        r3 = "BEGIN";	 Catch:{ Exception -> 0x0319 }
        r3 = r6.equals(r3);	 Catch:{ Exception -> 0x0319 }
        if (r3 == 0) goto L_0x0175;	 Catch:{ Exception -> 0x0319 }
    L_0x015c:
        r3 = 1;	 Catch:{ Exception -> 0x0319 }
        r6 = r2[r3];	 Catch:{ Exception -> 0x0319 }
        r3 = "VCARD";	 Catch:{ Exception -> 0x0319 }
        r3 = r6.equals(r3);	 Catch:{ Exception -> 0x0319 }
        if (r3 == 0) goto L_0x0175;	 Catch:{ Exception -> 0x0319 }
    L_0x0167:
        r3 = new org.telegram.ui.LaunchActivity$VcardData;	 Catch:{ Exception -> 0x0319 }
        r6 = 0;	 Catch:{ Exception -> 0x0319 }
        r3.<init>();	 Catch:{ Exception -> 0x0319 }
        r25 = r3;	 Catch:{ Exception -> 0x0319 }
        r11.add(r3);	 Catch:{ Exception -> 0x0319 }
        r3 = r25;	 Catch:{ Exception -> 0x0319 }
        goto L_0x0190;	 Catch:{ Exception -> 0x0319 }
    L_0x0175:
        r6 = 0;	 Catch:{ Exception -> 0x0319 }
        r3 = 0;	 Catch:{ Exception -> 0x0319 }
        r6 = r2[r3];	 Catch:{ Exception -> 0x0319 }
        r3 = "END";	 Catch:{ Exception -> 0x0319 }
        r3 = r6.equals(r3);	 Catch:{ Exception -> 0x0319 }
        if (r3 == 0) goto L_0x018e;	 Catch:{ Exception -> 0x0319 }
    L_0x0181:
        r3 = 1;	 Catch:{ Exception -> 0x0319 }
        r6 = r2[r3];	 Catch:{ Exception -> 0x0319 }
        r3 = "VCARD";	 Catch:{ Exception -> 0x0319 }
        r3 = r6.equals(r3);	 Catch:{ Exception -> 0x0319 }
        if (r3 == 0) goto L_0x018e;	 Catch:{ Exception -> 0x0319 }
    L_0x018c:
        r3 = 0;	 Catch:{ Exception -> 0x0319 }
        goto L_0x0190;	 Catch:{ Exception -> 0x0319 }
    L_0x018e:
        r3 = r29;	 Catch:{ Exception -> 0x0319 }
    L_0x0190:
        if (r3 != 0) goto L_0x0199;	 Catch:{ Exception -> 0x0319 }
    L_0x0193:
        r41 = r8;	 Catch:{ Exception -> 0x0319 }
        r42 = r12;	 Catch:{ Exception -> 0x0319 }
        goto L_0x02a1;	 Catch:{ Exception -> 0x0319 }
    L_0x0199:
        r6 = 0;	 Catch:{ Exception -> 0x0319 }
        r5 = r2[r6];	 Catch:{ Exception -> 0x0319 }
        r6 = "FN";	 Catch:{ Exception -> 0x0319 }
        r5 = r5.startsWith(r6);	 Catch:{ Exception -> 0x0319 }
        if (r5 != 0) goto L_0x01dc;	 Catch:{ Exception -> 0x0319 }
    L_0x01a4:
        r5 = 0;	 Catch:{ Exception -> 0x0319 }
        r6 = r2[r5];	 Catch:{ Exception -> 0x0319 }
        r5 = "ORG";	 Catch:{ Exception -> 0x0319 }
        r5 = r6.startsWith(r5);	 Catch:{ Exception -> 0x0319 }
        if (r5 == 0) goto L_0x01b8;	 Catch:{ Exception -> 0x0319 }
    L_0x01af:
        r5 = r3.name;	 Catch:{ Exception -> 0x0319 }
        r5 = android.text.TextUtils.isEmpty(r5);	 Catch:{ Exception -> 0x0319 }
        if (r5 == 0) goto L_0x01b8;	 Catch:{ Exception -> 0x0319 }
    L_0x01b7:
        goto L_0x01dc;	 Catch:{ Exception -> 0x0319 }
    L_0x01b8:
        r5 = 0;	 Catch:{ Exception -> 0x0319 }
        r6 = r2[r5];	 Catch:{ Exception -> 0x0319 }
        r5 = "TEL";	 Catch:{ Exception -> 0x0319 }
        r5 = r6.startsWith(r5);	 Catch:{ Exception -> 0x0319 }
        if (r5 == 0) goto L_0x01d6;	 Catch:{ Exception -> 0x0319 }
    L_0x01c3:
        r5 = 1;	 Catch:{ Exception -> 0x0319 }
        r6 = r2[r5];	 Catch:{ Exception -> 0x0319 }
        r6 = org.telegram.PhoneFormat.PhoneFormat.stripExceptNumbers(r6, r5);	 Catch:{ Exception -> 0x0319 }
        r5 = r6;	 Catch:{ Exception -> 0x0319 }
        r6 = r5.length();	 Catch:{ Exception -> 0x0319 }
        if (r6 <= 0) goto L_0x01d6;	 Catch:{ Exception -> 0x0319 }
    L_0x01d1:
        r6 = r3.phones;	 Catch:{ Exception -> 0x0319 }
        r6.add(r5);	 Catch:{ Exception -> 0x0319 }
    L_0x01d6:
        r41 = r8;	 Catch:{ Exception -> 0x0319 }
        r42 = r12;	 Catch:{ Exception -> 0x0319 }
        goto L_0x02a0;	 Catch:{ Exception -> 0x0319 }
    L_0x01dc:
        r5 = 0;	 Catch:{ Exception -> 0x0319 }
        r6 = 0;	 Catch:{ Exception -> 0x0319 }
        r39 = r5;	 Catch:{ Exception -> 0x0319 }
        r25 = 0;	 Catch:{ Exception -> 0x0319 }
        r5 = r2[r25];	 Catch:{ Exception -> 0x0319 }
        r40 = r6;	 Catch:{ Exception -> 0x0319 }
        r6 = ";";	 Catch:{ Exception -> 0x0319 }
        r5 = r5.split(r6);	 Catch:{ Exception -> 0x0319 }
        r6 = r5.length;	 Catch:{ Exception -> 0x0319 }
        r41 = r8;	 Catch:{ Exception -> 0x0319 }
        r42 = r12;	 Catch:{ Exception -> 0x0319 }
        r9 = r39;	 Catch:{ Exception -> 0x0319 }
        r12 = r40;	 Catch:{ Exception -> 0x0319 }
        r8 = 0;	 Catch:{ Exception -> 0x0319 }
        if (r8 >= r6) goto L_0x0239;	 Catch:{ Exception -> 0x0319 }
        r25 = r5[r8];	 Catch:{ Exception -> 0x0319 }
        r43 = r25;	 Catch:{ Exception -> 0x0319 }
        r44 = r5;	 Catch:{ Exception -> 0x0319 }
        r5 = "=";	 Catch:{ Exception -> 0x0319 }
        r45 = r6;	 Catch:{ Exception -> 0x0319 }
        r6 = r43;	 Catch:{ Exception -> 0x0319 }
        r5 = r6.split(r5);	 Catch:{ Exception -> 0x0319 }
        r46 = r6;	 Catch:{ Exception -> 0x0319 }
        r6 = r5.length;	 Catch:{ Exception -> 0x0319 }
        r13 = 2;	 Catch:{ Exception -> 0x0319 }
        if (r6 == r13) goto L_0x020f;	 Catch:{ Exception -> 0x0319 }
        goto L_0x0230;	 Catch:{ Exception -> 0x0319 }
        r6 = 0;	 Catch:{ Exception -> 0x0319 }
        r13 = r5[r6];	 Catch:{ Exception -> 0x0319 }
        r6 = "CHARSET";	 Catch:{ Exception -> 0x0319 }
        r6 = r13.equals(r6);	 Catch:{ Exception -> 0x0319 }
        if (r6 == 0) goto L_0x0220;	 Catch:{ Exception -> 0x0319 }
        r6 = 1;	 Catch:{ Exception -> 0x0319 }
        r13 = r5[r6];	 Catch:{ Exception -> 0x0319 }
        r6 = r13;	 Catch:{ Exception -> 0x0319 }
        r12 = r6;	 Catch:{ Exception -> 0x0319 }
        goto L_0x0230;	 Catch:{ Exception -> 0x0319 }
        r6 = 0;	 Catch:{ Exception -> 0x0319 }
        r13 = r5[r6];	 Catch:{ Exception -> 0x0319 }
        r6 = "ENCODING";	 Catch:{ Exception -> 0x0319 }
        r6 = r13.equals(r6);	 Catch:{ Exception -> 0x0319 }
        if (r6 == 0) goto L_0x0230;	 Catch:{ Exception -> 0x0319 }
        r6 = 1;	 Catch:{ Exception -> 0x0319 }
        r13 = r5[r6];	 Catch:{ Exception -> 0x0319 }
        r5 = r13;	 Catch:{ Exception -> 0x0319 }
        r9 = r5;	 Catch:{ Exception -> 0x0319 }
        r8 = r8 + 1;	 Catch:{ Exception -> 0x0319 }
        r5 = r44;	 Catch:{ Exception -> 0x0319 }
        r6 = r45;	 Catch:{ Exception -> 0x0319 }
        r13 = r66;	 Catch:{ Exception -> 0x0319 }
        goto L_0x01f6;	 Catch:{ Exception -> 0x0319 }
        r44 = r5;	 Catch:{ Exception -> 0x0319 }
        r5 = 1;	 Catch:{ Exception -> 0x0319 }
        r6 = r2[r5];	 Catch:{ Exception -> 0x0319 }
        r3.name = r6;	 Catch:{ Exception -> 0x0319 }
        if (r9 == 0) goto L_0x029f;	 Catch:{ Exception -> 0x0319 }
        r5 = "QUOTED-PRINTABLE";	 Catch:{ Exception -> 0x0319 }
        r5 = r9.equalsIgnoreCase(r5);	 Catch:{ Exception -> 0x0319 }
        if (r5 == 0) goto L_0x029f;	 Catch:{ Exception -> 0x0319 }
        r5 = r3.name;	 Catch:{ Exception -> 0x0319 }
        r6 = "=";	 Catch:{ Exception -> 0x0319 }
        r5 = r5.endsWith(r6);	 Catch:{ Exception -> 0x0319 }
        if (r5 == 0) goto L_0x0287;	 Catch:{ Exception -> 0x0319 }
        if (r9 == 0) goto L_0x0287;	 Catch:{ Exception -> 0x0319 }
        r5 = r3.name;	 Catch:{ Exception -> 0x0319 }
        r6 = r3.name;	 Catch:{ Exception -> 0x0319 }
        r6 = r6.length();	 Catch:{ Exception -> 0x0319 }
        r8 = 1;	 Catch:{ Exception -> 0x0319 }
        r6 = r6 - r8;	 Catch:{ Exception -> 0x0319 }
        r8 = 0;	 Catch:{ Exception -> 0x0319 }
        r5 = r5.substring(r8, r6);	 Catch:{ Exception -> 0x0319 }
        r3.name = r5;	 Catch:{ Exception -> 0x0319 }
        r5 = r1.readLine();	 Catch:{ Exception -> 0x0319 }
        if (r5 != 0) goto L_0x0271;	 Catch:{ Exception -> 0x0319 }
        r36 = r5;	 Catch:{ Exception -> 0x0319 }
        goto L_0x0287;	 Catch:{ Exception -> 0x0319 }
        r6 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x0319 }
        r6.<init>();	 Catch:{ Exception -> 0x0319 }
        r8 = r3.name;	 Catch:{ Exception -> 0x0319 }
        r6.append(r8);	 Catch:{ Exception -> 0x0319 }
        r6.append(r5);	 Catch:{ Exception -> 0x0319 }
        r6 = r6.toString();	 Catch:{ Exception -> 0x0319 }
        r3.name = r6;	 Catch:{ Exception -> 0x0319 }
        r36 = r5;	 Catch:{ Exception -> 0x0319 }
        goto L_0x024a;	 Catch:{ Exception -> 0x0319 }
        r5 = r3.name;	 Catch:{ Exception -> 0x0319 }
        r5 = r5.getBytes();	 Catch:{ Exception -> 0x0319 }
        r5 = org.telegram.messenger.AndroidUtilities.decodeQuotedPrintable(r5);	 Catch:{ Exception -> 0x0319 }
        if (r5 == 0) goto L_0x029f;	 Catch:{ Exception -> 0x0319 }
        r6 = r5.length;	 Catch:{ Exception -> 0x0319 }
        if (r6 == 0) goto L_0x029f;	 Catch:{ Exception -> 0x0319 }
        r6 = new java.lang.String;	 Catch:{ Exception -> 0x0319 }
        r6.<init>(r5, r12);	 Catch:{ Exception -> 0x0319 }
        if (r6 == 0) goto L_0x029f;	 Catch:{ Exception -> 0x0319 }
        r3.name = r6;	 Catch:{ Exception -> 0x0319 }
    L_0x02a1:
        r29 = r3;
        r5 = r37;
        r8 = r41;
        r12 = r42;
        r9 = r65;
        r13 = r66;
        goto L_0x012a;
    L_0x02af:
        r36 = r3;
        r37 = r5;
        r41 = r8;
        r42 = r12;
        r1.close();	 Catch:{ Exception -> 0x02be }
        r4.close();	 Catch:{ Exception -> 0x02be }
        goto L_0x02c3;
    L_0x02be:
        r0 = move-exception;
        r2 = r0;
        org.telegram.messenger.FileLog.e(r2);	 Catch:{ Exception -> 0x0319 }
        r2 = 0;	 Catch:{ Exception -> 0x0319 }
        r3 = r11.size();	 Catch:{ Exception -> 0x0319 }
        if (r2 >= r3) goto L_0x0315;	 Catch:{ Exception -> 0x0319 }
        r3 = r11.get(r2);	 Catch:{ Exception -> 0x0319 }
        r3 = (org.telegram.ui.LaunchActivity.VcardData) r3;	 Catch:{ Exception -> 0x0319 }
        r5 = r3.name;	 Catch:{ Exception -> 0x0319 }
        if (r5 == 0) goto L_0x0312;	 Catch:{ Exception -> 0x0319 }
        r5 = r3.phones;	 Catch:{ Exception -> 0x0319 }
        r5 = r5.isEmpty();	 Catch:{ Exception -> 0x0319 }
        if (r5 != 0) goto L_0x0312;	 Catch:{ Exception -> 0x0319 }
        r5 = r14.contactsToSend;	 Catch:{ Exception -> 0x0319 }
        if (r5 != 0) goto L_0x02e7;	 Catch:{ Exception -> 0x0319 }
        r5 = new java.util.ArrayList;	 Catch:{ Exception -> 0x0319 }
        r5.<init>();	 Catch:{ Exception -> 0x0319 }
        r14.contactsToSend = r5;	 Catch:{ Exception -> 0x0319 }
        r5 = 0;	 Catch:{ Exception -> 0x0319 }
        r6 = r3.phones;	 Catch:{ Exception -> 0x0319 }
        r6 = r6.size();	 Catch:{ Exception -> 0x0319 }
        if (r5 >= r6) goto L_0x0312;	 Catch:{ Exception -> 0x0319 }
        r6 = r3.phones;	 Catch:{ Exception -> 0x0319 }
        r6 = r6.get(r5);	 Catch:{ Exception -> 0x0319 }
        r6 = (java.lang.String) r6;	 Catch:{ Exception -> 0x0319 }
        r8 = new org.telegram.tgnet.TLRPC$TL_userContact_old2;	 Catch:{ Exception -> 0x0319 }
        r8.<init>();	 Catch:{ Exception -> 0x0319 }
        r8.phone = r6;	 Catch:{ Exception -> 0x0319 }
        r9 = r3.name;	 Catch:{ Exception -> 0x0319 }
        r8.first_name = r9;	 Catch:{ Exception -> 0x0319 }
        r9 = "";	 Catch:{ Exception -> 0x0319 }
        r8.last_name = r9;	 Catch:{ Exception -> 0x0319 }
        r9 = 0;	 Catch:{ Exception -> 0x0319 }
        r8.id = r9;	 Catch:{ Exception -> 0x0319 }
        r9 = r14.contactsToSend;	 Catch:{ Exception -> 0x0319 }
        r9.add(r8);	 Catch:{ Exception -> 0x0319 }
        r5 = r5 + 1;
        goto L_0x02e8;
        r2 = r2 + 1;
        goto L_0x02c4;
        r4 = r35;
        goto L_0x035b;
    L_0x0319:
        r0 = move-exception;
        r1 = r0;
        goto L_0x0357;
    L_0x031c:
        r0 = move-exception;
        r37 = r5;
        r1 = r0;
        goto L_0x0357;
    L_0x0321:
        r0 = move-exception;
        r35 = r4;
        r37 = r5;
        r1 = r0;
        goto L_0x0357;
    L_0x0328:
        r0 = move-exception;
        r34 = r3;
        r35 = r4;
        r37 = r5;
        r1 = r0;
        goto L_0x0357;
    L_0x0331:
        r0 = move-exception;
        r33 = r2;
        r34 = r3;
        r35 = r4;
        r37 = r5;
        r1 = r0;
        goto L_0x0357;
    L_0x033c:
        r32 = r1;
        r33 = r2;
        r34 = r3;
        r35 = r4;
        r37 = r5;
        r41 = r8;
        r1 = 1;
        r4 = r1;
        goto L_0x035b;
    L_0x034b:
        r0 = move-exception;
        r32 = r1;
        r33 = r2;
        r34 = r3;
        r35 = r4;
        r37 = r5;
        r1 = r0;
    L_0x0357:
        org.telegram.messenger.FileLog.e(r1);
        r4 = 1;
        goto L_0x0479;
    L_0x035d:
        r32 = r1;
        r33 = r2;
        r34 = r3;
        r35 = r4;
        r37 = r5;
        r1 = "android.intent.extra.TEXT";
        r1 = r15.getStringExtra(r1);
        if (r1 != 0) goto L_0x037b;
        r2 = "android.intent.extra.TEXT";
        r2 = r15.getCharSequenceExtra(r2);
        if (r2 == 0) goto L_0x037b;
        r1 = r2.toString();
        r2 = "android.intent.extra.SUBJECT";
        r2 = r15.getStringExtra(r2);
        if (r1 == 0) goto L_0x03b8;
        r3 = r1.length();
        if (r3 == 0) goto L_0x03b8;
        r3 = "http://";
        r3 = r1.startsWith(r3);
        if (r3 != 0) goto L_0x0399;
        r3 = "https://";
        r3 = r1.startsWith(r3);
        if (r3 == 0) goto L_0x03b5;
        if (r2 == 0) goto L_0x03b5;
        r3 = r2.length();
        if (r3 == 0) goto L_0x03b5;
        r3 = new java.lang.StringBuilder;
        r3.<init>();
        r3.append(r2);
        r4 = "\n";
        r3.append(r4);
        r3.append(r1);
        r1 = r3.toString();
        r14.sendingText = r1;
        goto L_0x03c2;
        if (r2 == 0) goto L_0x03c2;
        r3 = r2.length();
        if (r3 <= 0) goto L_0x03c2;
        r14.sendingText = r2;
        r3 = "android.intent.extra.STREAM";
        r3 = r15.getParcelableExtra(r3);
        if (r3 == 0) goto L_0x0471;
        r4 = r3 instanceof android.net.Uri;
        if (r4 != 0) goto L_0x03d6;
        r4 = r3.toString();
        r3 = android.net.Uri.parse(r4);
        r4 = r3;
        r4 = (android.net.Uri) r4;
        if (r4 == 0) goto L_0x03e4;
        r5 = org.telegram.messenger.AndroidUtilities.isInternalUri(r4);
        if (r5 == 0) goto L_0x03e4;
        r5 = 1;
        r35 = r5;
        if (r35 != 0) goto L_0x0470;
        if (r4 == 0) goto L_0x041a;
        if (r7 == 0) goto L_0x03f2;
        r5 = "image/";
        r5 = r7.startsWith(r5);
        if (r5 != 0) goto L_0x0402;
        r5 = r4.toString();
        r5 = r5.toLowerCase();
        r6 = ".jpg";
        r5 = r5.endsWith(r6);
        if (r5 == 0) goto L_0x041a;
        r5 = r14.photoPathsArray;
        if (r5 != 0) goto L_0x040d;
        r5 = new java.util.ArrayList;
        r5.<init>();
        r14.photoPathsArray = r5;
        r5 = new org.telegram.messenger.SendMessagesHelper$SendingMediaInfo;
        r5.<init>();
        r5.uri = r4;
        r6 = r14.photoPathsArray;
        r6.add(r5);
        goto L_0x0470;
        r5 = org.telegram.messenger.AndroidUtilities.getPath(r4);
        if (r5 == 0) goto L_0x045e;
        r6 = "file:";
        r6 = r5.startsWith(r6);
        if (r6 == 0) goto L_0x0430;
        r6 = "file://";
        r8 = "";
        r5 = r5.replace(r6, r8);
        if (r7 == 0) goto L_0x043d;
        r6 = "video/";
        r6 = r7.startsWith(r6);
        if (r6 == 0) goto L_0x043d;
        r14.videoPath = r5;
        goto L_0x0470;
        r6 = r14.documentsPathsArray;
        if (r6 != 0) goto L_0x044f;
        r6 = new java.util.ArrayList;
        r6.<init>();
        r14.documentsPathsArray = r6;
        r6 = new java.util.ArrayList;
        r6.<init>();
        r14.documentsOriginalPathsArray = r6;
        r6 = r14.documentsPathsArray;
        r6.add(r5);
        r6 = r14.documentsOriginalPathsArray;
        r8 = r4.toString();
        r6.add(r8);
        goto L_0x0470;
        r6 = r14.documentsUrisArray;
        if (r6 != 0) goto L_0x0469;
        r6 = new java.util.ArrayList;
        r6.<init>();
        r14.documentsUrisArray = r6;
        r6 = r14.documentsUrisArray;
        r6.add(r4);
        r14.documentsMimeType = r7;
        goto L_0x0477;
        r4 = r14.sendingText;
        if (r4 != 0) goto L_0x0477;
        r4 = 1;
        goto L_0x0479;
        r4 = r35;
        if (r4 == 0) goto L_0x0485;
        r1 = "Unsupported content";
        r2 = 0;
        r1 = android.widget.Toast.makeText(r14, r1, r2);
        r1.show();
        r7 = r10;
        r1 = r15;
        r56 = r37;
        r2 = 1;
        r4 = 0;
        r26 = 0;
        goto L_0x0bf9;
    L_0x0490:
        r32 = r1;
        r33 = r2;
        r34 = r3;
        r37 = r5;
        r1 = r64.getAction();
        r2 = "android.intent.action.SEND_MULTIPLE";
        r1 = r1.equals(r2);
        if (r1 == 0) goto L_0x05ac;
        r1 = 0;
        r2 = r1;
        r1 = "android.intent.extra.STREAM";	 Catch:{ Exception -> 0x0598 }
        r1 = r15.getParcelableArrayListExtra(r1);	 Catch:{ Exception -> 0x0598 }
        r3 = r64.getType();	 Catch:{ Exception -> 0x0598 }
        if (r1 == 0) goto L_0x04e6;	 Catch:{ Exception -> 0x0598 }
        r4 = 0;	 Catch:{ Exception -> 0x0598 }
        r5 = r1.size();	 Catch:{ Exception -> 0x0598 }
        if (r4 >= r5) goto L_0x04df;	 Catch:{ Exception -> 0x0598 }
        r5 = r1.get(r4);	 Catch:{ Exception -> 0x0598 }
        r5 = (android.os.Parcelable) r5;	 Catch:{ Exception -> 0x0598 }
        r6 = r5 instanceof android.net.Uri;	 Catch:{ Exception -> 0x0598 }
        if (r6 != 0) goto L_0x04cc;	 Catch:{ Exception -> 0x0598 }
        r6 = r5.toString();	 Catch:{ Exception -> 0x0598 }
        r6 = android.net.Uri.parse(r6);	 Catch:{ Exception -> 0x0598 }
        r5 = r6;	 Catch:{ Exception -> 0x0598 }
        r6 = r5;	 Catch:{ Exception -> 0x0598 }
        r6 = (android.net.Uri) r6;	 Catch:{ Exception -> 0x0598 }
        if (r6 == 0) goto L_0x04dc;	 Catch:{ Exception -> 0x0598 }
        r7 = org.telegram.messenger.AndroidUtilities.isInternalUri(r6);	 Catch:{ Exception -> 0x0598 }
        if (r7 == 0) goto L_0x04dc;	 Catch:{ Exception -> 0x0598 }
        r1.remove(r4);	 Catch:{ Exception -> 0x0598 }
        r4 = r4 + -1;	 Catch:{ Exception -> 0x0598 }
        r5 = 1;	 Catch:{ Exception -> 0x0598 }
        r4 = r4 + r5;	 Catch:{ Exception -> 0x0598 }
        goto L_0x04b3;	 Catch:{ Exception -> 0x0598 }
        r4 = r1.isEmpty();	 Catch:{ Exception -> 0x0598 }
        if (r4 == 0) goto L_0x04e6;	 Catch:{ Exception -> 0x0598 }
        r1 = 0;	 Catch:{ Exception -> 0x0598 }
        if (r1 == 0) goto L_0x0596;	 Catch:{ Exception -> 0x0598 }
        if (r3 == 0) goto L_0x0529;	 Catch:{ Exception -> 0x0598 }
        r4 = "image/";	 Catch:{ Exception -> 0x0598 }
        r4 = r3.startsWith(r4);	 Catch:{ Exception -> 0x0598 }
        if (r4 == 0) goto L_0x0529;	 Catch:{ Exception -> 0x0598 }
        r4 = 0;	 Catch:{ Exception -> 0x0598 }
        r5 = r1.size();	 Catch:{ Exception -> 0x0598 }
        if (r4 >= r5) goto L_0x0597;	 Catch:{ Exception -> 0x0598 }
        r5 = r1.get(r4);	 Catch:{ Exception -> 0x0598 }
        r5 = (android.os.Parcelable) r5;	 Catch:{ Exception -> 0x0598 }
        r6 = r5 instanceof android.net.Uri;	 Catch:{ Exception -> 0x0598 }
        if (r6 != 0) goto L_0x050c;	 Catch:{ Exception -> 0x0598 }
        r6 = r5.toString();	 Catch:{ Exception -> 0x0598 }
        r6 = android.net.Uri.parse(r6);	 Catch:{ Exception -> 0x0598 }
        r5 = r6;	 Catch:{ Exception -> 0x0598 }
        r6 = r5;	 Catch:{ Exception -> 0x0598 }
        r6 = (android.net.Uri) r6;	 Catch:{ Exception -> 0x0598 }
        r7 = r14.photoPathsArray;	 Catch:{ Exception -> 0x0598 }
        if (r7 != 0) goto L_0x051a;	 Catch:{ Exception -> 0x0598 }
        r7 = new java.util.ArrayList;	 Catch:{ Exception -> 0x0598 }
        r7.<init>();	 Catch:{ Exception -> 0x0598 }
        r14.photoPathsArray = r7;	 Catch:{ Exception -> 0x0598 }
        r7 = new org.telegram.messenger.SendMessagesHelper$SendingMediaInfo;	 Catch:{ Exception -> 0x0598 }
        r7.<init>();	 Catch:{ Exception -> 0x0598 }
        r7.uri = r6;	 Catch:{ Exception -> 0x0598 }
        r8 = r14.photoPathsArray;	 Catch:{ Exception -> 0x0598 }
        r8.add(r7);	 Catch:{ Exception -> 0x0598 }
        r4 = r4 + 1;	 Catch:{ Exception -> 0x0598 }
        goto L_0x04f3;	 Catch:{ Exception -> 0x0598 }
        r4 = 0;	 Catch:{ Exception -> 0x0598 }
        r5 = r1.size();	 Catch:{ Exception -> 0x0598 }
        if (r4 >= r5) goto L_0x0597;	 Catch:{ Exception -> 0x0598 }
        r5 = r1.get(r4);	 Catch:{ Exception -> 0x0598 }
        r5 = (android.os.Parcelable) r5;	 Catch:{ Exception -> 0x0598 }
        r6 = r5 instanceof android.net.Uri;	 Catch:{ Exception -> 0x0598 }
        if (r6 != 0) goto L_0x0543;	 Catch:{ Exception -> 0x0598 }
        r6 = r5.toString();	 Catch:{ Exception -> 0x0598 }
        r6 = android.net.Uri.parse(r6);	 Catch:{ Exception -> 0x0598 }
        r5 = r6;	 Catch:{ Exception -> 0x0598 }
        r6 = r5;	 Catch:{ Exception -> 0x0598 }
        r6 = (android.net.Uri) r6;	 Catch:{ Exception -> 0x0598 }
        r7 = org.telegram.messenger.AndroidUtilities.getPath(r6);	 Catch:{ Exception -> 0x0598 }
        r8 = r5.toString();	 Catch:{ Exception -> 0x0598 }
        if (r8 != 0) goto L_0x0551;	 Catch:{ Exception -> 0x0598 }
        r8 = r7;	 Catch:{ Exception -> 0x0598 }
        if (r7 == 0) goto L_0x0581;	 Catch:{ Exception -> 0x0598 }
        r9 = "file:";	 Catch:{ Exception -> 0x0598 }
        r9 = r7.startsWith(r9);	 Catch:{ Exception -> 0x0598 }
        if (r9 == 0) goto L_0x0564;	 Catch:{ Exception -> 0x0598 }
        r9 = "file://";	 Catch:{ Exception -> 0x0598 }
        r11 = "";	 Catch:{ Exception -> 0x0598 }
        r9 = r7.replace(r9, r11);	 Catch:{ Exception -> 0x0598 }
        r7 = r9;	 Catch:{ Exception -> 0x0598 }
        r9 = r14.documentsPathsArray;	 Catch:{ Exception -> 0x0598 }
        if (r9 != 0) goto L_0x0576;	 Catch:{ Exception -> 0x0598 }
        r9 = new java.util.ArrayList;	 Catch:{ Exception -> 0x0598 }
        r9.<init>();	 Catch:{ Exception -> 0x0598 }
        r14.documentsPathsArray = r9;	 Catch:{ Exception -> 0x0598 }
        r9 = new java.util.ArrayList;	 Catch:{ Exception -> 0x0598 }
        r9.<init>();	 Catch:{ Exception -> 0x0598 }
        r14.documentsOriginalPathsArray = r9;	 Catch:{ Exception -> 0x0598 }
        r9 = r14.documentsPathsArray;	 Catch:{ Exception -> 0x0598 }
        r9.add(r7);	 Catch:{ Exception -> 0x0598 }
        r9 = r14.documentsOriginalPathsArray;	 Catch:{ Exception -> 0x0598 }
        r9.add(r8);	 Catch:{ Exception -> 0x0598 }
        goto L_0x0593;	 Catch:{ Exception -> 0x0598 }
        r9 = r14.documentsUrisArray;	 Catch:{ Exception -> 0x0598 }
        if (r9 != 0) goto L_0x058c;	 Catch:{ Exception -> 0x0598 }
        r9 = new java.util.ArrayList;	 Catch:{ Exception -> 0x0598 }
        r9.<init>();	 Catch:{ Exception -> 0x0598 }
        r14.documentsUrisArray = r9;	 Catch:{ Exception -> 0x0598 }
        r9 = r14.documentsUrisArray;	 Catch:{ Exception -> 0x0598 }
        r9.add(r6);	 Catch:{ Exception -> 0x0598 }
        r14.documentsMimeType = r3;	 Catch:{ Exception -> 0x0598 }
        r4 = r4 + 1;
        goto L_0x052a;
        r2 = 1;
        goto L_0x059e;
    L_0x0598:
        r0 = move-exception;
        r1 = r0;
        org.telegram.messenger.FileLog.e(r1);
        r2 = 1;
        if (r2 == 0) goto L_0x05aa;
        r1 = "Unsupported content";
        r3 = 0;
        r1 = android.widget.Toast.makeText(r14, r1, r3);
        r1.show();
        goto L_0x0486;
        r1 = "android.intent.action.VIEW";
        r2 = r64.getAction();
        r1 = r1.equals(r2);
        if (r1 == 0) goto L_0x0b25;
        r1 = r64.getData();
        if (r1 == 0) goto L_0x0b08;
        r2 = 0;
        r3 = 0;
        r4 = 0;
        r5 = 0;
        r6 = 0;
        r7 = 0;
        r8 = 0;
        r9 = 0;
        r11 = 0;
        r12 = 0;
        r13 = 0;
        r25 = 0;
        r47 = r13;
        r13 = r1.getScheme();
        if (r13 == 0) goto L_0x09c7;
        r48 = r2;
        r2 = "http";
        r2 = r13.equals(r2);
        r49 = r3;
        if (r2 != 0) goto L_0x0849;
        r2 = "https";
        r2 = r13.equals(r2);
        if (r2 == 0) goto L_0x05ed;
        r50 = r4;
        r51 = r6;
        goto L_0x084d;
        r2 = "tg";
        r2 = r13.equals(r2);
        if (r2 == 0) goto L_0x0843;
        r2 = r1.toString();
        r3 = "tg:resolve";
        r3 = r2.startsWith(r3);
        if (r3 != 0) goto L_0x07f4;
        r3 = "tg://resolve";
        r3 = r2.startsWith(r3);
        if (r3 == 0) goto L_0x060f;
        r50 = r4;
        r51 = r6;
        goto L_0x07f8;
        r3 = "tg:join";
        r3 = r2.startsWith(r3);
        if (r3 != 0) goto L_0x07d0;
        r3 = "tg://join";
        r3 = r2.startsWith(r3);
        if (r3 == 0) goto L_0x0625;
        r50 = r4;
        r51 = r6;
        goto L_0x07d4;
        r3 = "tg:addstickers";
        r3 = r2.startsWith(r3);
        if (r3 != 0) goto L_0x07ac;
        r3 = "tg://addstickers";
        r3 = r2.startsWith(r3);
        if (r3 == 0) goto L_0x063b;
        r50 = r4;
        r51 = r6;
        goto L_0x07b0;
        r3 = "tg:msg";
        r3 = r2.startsWith(r3);
        if (r3 != 0) goto L_0x0720;
        r3 = "tg://msg";
        r3 = r2.startsWith(r3);
        if (r3 != 0) goto L_0x0720;
        r3 = "tg://share";
        r3 = r2.startsWith(r3);
        if (r3 != 0) goto L_0x0720;
        r3 = "tg:share";
        r3 = r2.startsWith(r3);
        if (r3 == 0) goto L_0x0661;
        r50 = r4;
        r51 = r6;
        goto L_0x0724;
        r3 = "tg:confirmphone";
        r3 = r2.startsWith(r3);
        if (r3 != 0) goto L_0x06fa;
        r3 = "tg://confirmphone";
        r3 = r2.startsWith(r3);
        if (r3 == 0) goto L_0x0677;
        r50 = r4;
        r51 = r6;
        goto L_0x06fe;
        r3 = "tg:openmessage";
        r3 = r2.startsWith(r3);
        if (r3 != 0) goto L_0x068e;
        r3 = "tg://openmessage";
        r3 = r2.startsWith(r3);
        if (r3 == 0) goto L_0x0688;
        goto L_0x068e;
        r50 = r4;
        r51 = r6;
        goto L_0x09cf;
        r3 = "tg:openmessage";
        r50 = r4;
        r4 = "tg://telegram.org";
        r3 = r2.replace(r3, r4);
        r4 = "tg://openmessage";
        r51 = r6;
        r6 = "tg://telegram.org";
        r2 = r3.replace(r4, r6);
        r1 = android.net.Uri.parse(r2);
        r3 = "user_id";
        r3 = r1.getQueryParameter(r3);
        r4 = "chat_id";
        r4 = r1.getQueryParameter(r4);
        r6 = "message_id";
        r6 = r1.getQueryParameter(r6);
        if (r3 == 0) goto L_0x06c7;
        r52 = r1;
        r1 = java.lang.Integer.parseInt(r3);	 Catch:{ NumberFormatException -> 0x06c5 }
        r1 = java.lang.Integer.valueOf(r1);	 Catch:{ NumberFormatException -> 0x06c5 }
        goto L_0x06da;
    L_0x06c5:
        r0 = move-exception;
        goto L_0x06d8;
        r52 = r1;
        if (r4 == 0) goto L_0x06d8;
        r1 = java.lang.Integer.parseInt(r4);	 Catch:{ NumberFormatException -> 0x06d7 }
        r1 = java.lang.Integer.valueOf(r1);	 Catch:{ NumberFormatException -> 0x06d7 }
        r33 = r1;
        goto L_0x06d8;
    L_0x06d7:
        r0 = move-exception;
        r1 = r32;
        if (r6 == 0) goto L_0x06ec;
        r53 = r1;
        r1 = java.lang.Integer.parseInt(r6);	 Catch:{ NumberFormatException -> 0x06ea }
        r1 = java.lang.Integer.valueOf(r1);	 Catch:{ NumberFormatException -> 0x06ea }
        r34 = r1;
        goto L_0x06ee;
    L_0x06ea:
        r0 = move-exception;
        goto L_0x06ee;
        r53 = r1;
        r29 = r7;
        r30 = r11;
        r11 = r12;
        r32 = r25;
        r25 = r5;
        r12 = r9;
        goto L_0x09dd;
        r50 = r4;
        r51 = r6;
        r3 = "tg:confirmphone";
        r4 = "tg://telegram.org";
        r3 = r2.replace(r3, r4);
        r4 = "tg://confirmphone";
        r6 = "tg://telegram.org";
        r2 = r3.replace(r4, r6);
        r1 = android.net.Uri.parse(r2);
        r3 = "phone";
        r9 = r1.getQueryParameter(r3);
        r3 = "hash";
        r12 = r1.getQueryParameter(r3);
        goto L_0x09cf;
        r50 = r4;
        r51 = r6;
        r3 = "tg:msg";
        r4 = "tg://telegram.org";
        r3 = r2.replace(r3, r4);
        r4 = "tg://msg";
        r6 = "tg://telegram.org";
        r3 = r3.replace(r4, r6);
        r4 = "tg://share";
        r6 = "tg://telegram.org";
        r3 = r3.replace(r4, r6);
        r4 = "tg:share";
        r6 = "tg://telegram.org";
        r2 = r3.replace(r4, r6);
        r1 = android.net.Uri.parse(r2);
        r3 = "url";
        r3 = r1.getQueryParameter(r3);
        if (r3 != 0) goto L_0x0752;
        r3 = "";
        r4 = "text";
        r4 = r1.getQueryParameter(r4);
        if (r4 == 0) goto L_0x0789;
        r4 = r3.length();
        if (r4 <= 0) goto L_0x0774;
        r4 = 1;
        r6 = new java.lang.StringBuilder;
        r6.<init>();
        r6.append(r3);
        r8 = "\n";
        r6.append(r8);
        r3 = r6.toString();
        r25 = r4;
        r4 = new java.lang.StringBuilder;
        r4.<init>();
        r4.append(r3);
        r6 = "text";
        r6 = r1.getQueryParameter(r6);
        r4.append(r6);
        r3 = r4.toString();
        r4 = r3.length();
        r6 = 16384; // 0x4000 float:2.2959E-41 double:8.0948E-320;
        if (r4 <= r6) goto L_0x0797;
        r4 = 0;
        r3 = r3.substring(r4, r6);
        goto L_0x0798;
        r4 = 0;
        r8 = r3;
        r3 = "\n";
        r3 = r8.endsWith(r3);
        if (r3 == 0) goto L_0x09cf;
        r3 = r8.length();
        r6 = 1;
        r3 = r3 - r6;
        r8 = r8.substring(r4, r3);
        goto L_0x0799;
        r50 = r4;
        r51 = r6;
        r3 = "tg:addstickers";
        r4 = "tg://telegram.org";
        r3 = r2.replace(r3, r4);
        r4 = "tg://addstickers";
        r6 = "tg://telegram.org";
        r2 = r3.replace(r4, r6);
        r1 = android.net.Uri.parse(r2);
        r3 = "set";
        r3 = r1.getQueryParameter(r3);
        r52 = r1;
        r50 = r3;
        goto L_0x09d1;
        r50 = r4;
        r51 = r6;
        r3 = "tg:join";
        r4 = "tg://telegram.org";
        r3 = r2.replace(r3, r4);
        r4 = "tg://join";
        r6 = "tg://telegram.org";
        r2 = r3.replace(r4, r6);
        r1 = android.net.Uri.parse(r2);
        r3 = "invite";
        r3 = r1.getQueryParameter(r3);
        r52 = r1;
        r49 = r3;
        goto L_0x09d1;
        r50 = r4;
        r51 = r6;
        r3 = "tg:resolve";
        r4 = "tg://telegram.org";
        r3 = r2.replace(r3, r4);
        r4 = "tg://resolve";
        r6 = "tg://telegram.org";
        r2 = r3.replace(r4, r6);
        r1 = android.net.Uri.parse(r2);
        r3 = "domain";
        r3 = r1.getQueryParameter(r3);
        r4 = "start";
        r4 = r1.getQueryParameter(r4);
        r6 = "startgroup";
        r6 = r1.getQueryParameter(r6);
        r7 = "game";
        r7 = r1.getQueryParameter(r7);
        r11 = "post";
        r11 = r1.getQueryParameter(r11);
        r11 = org.telegram.messenger.Utilities.parseInt(r11);
        r29 = r11.intValue();
        if (r29 != 0) goto L_0x0835;
        r11 = 0;
        r52 = r1;
        r48 = r3;
        r51 = r4;
        r29 = r6;
        r30 = r7;
        r47 = r11;
        goto L_0x09d5;
        r50 = r4;
        r51 = r6;
        goto L_0x09cf;
        r50 = r4;
        r51 = r6;
        r2 = r1.getHost();
        r2 = r2.toLowerCase();
        r3 = "telegram.me";
        r3 = r2.equals(r3);
        if (r3 != 0) goto L_0x0875;
        r3 = "t.me";
        r3 = r2.equals(r3);
        if (r3 != 0) goto L_0x0875;
        r3 = "telegram.dog";
        r3 = r2.equals(r3);
        if (r3 != 0) goto L_0x0875;
        r3 = "telesco.pe";
        r3 = r2.equals(r3);
        if (r3 == 0) goto L_0x09c6;
        r3 = r1.getPath();
        if (r3 == 0) goto L_0x09c6;
        r4 = r3.length();
        r6 = 1;
        if (r4 <= r6) goto L_0x09c6;
        r3 = r3.substring(r6);
        r4 = "joinchat/";
        r4 = r3.startsWith(r4);
        if (r4 == 0) goto L_0x089a;
        r4 = "joinchat/";
        r6 = "";
        r4 = r3.replace(r4, r6);
        r49 = r4;
        goto L_0x09c6;
        r4 = "addstickers/";
        r4 = r3.startsWith(r4);
        if (r4 == 0) goto L_0x08ae;
        r4 = "addstickers/";
        r6 = "";
        r4 = r3.replace(r4, r6);
        r50 = r4;
        goto L_0x09c6;
        r4 = "iv/";
        r4 = r3.startsWith(r4);
        if (r4 == 0) goto L_0x08dc;
        r4 = "url";
        r4 = r1.getQueryParameter(r4);
        r6 = 0;
        r5[r6] = r4;
        r4 = "rhash";
        r4 = r1.getQueryParameter(r4);
        r21 = 1;
        r5[r21] = r4;
        r4 = r5[r6];
        r4 = android.text.TextUtils.isEmpty(r4);
        if (r4 != 0) goto L_0x08d9;
        r4 = r5[r21];
        r4 = android.text.TextUtils.isEmpty(r4);
        if (r4 == 0) goto L_0x09c6;
        r5 = 0;
        goto L_0x09c6;
        r4 = "msg/";
        r4 = r3.startsWith(r4);
        if (r4 != 0) goto L_0x095f;
        r4 = "share/";
        r4 = r3.startsWith(r4);
        if (r4 == 0) goto L_0x08f2;
        r54 = r2;
        r55 = r3;
        goto L_0x0963;
        r4 = "confirmphone";
        r4 = r3.startsWith(r4);
        if (r4 == 0) goto L_0x0908;
        r4 = "phone";
        r9 = r1.getQueryParameter(r4);
        r4 = "hash";
        r12 = r1.getQueryParameter(r4);
        goto L_0x09c6;
        r4 = r3.length();
        r6 = 1;
        if (r4 < r6) goto L_0x09c6;
        r4 = r1.getPathSegments();
        r21 = r4.size();
        if (r21 <= 0) goto L_0x0942;
        r6 = 0;
        r21 = r4.get(r6);
        r6 = r21;
        r6 = (java.lang.String) r6;
        r54 = r2;
        r2 = r4.size();
        r55 = r3;
        r3 = 1;
        if (r2 <= r3) goto L_0x0940;
        r2 = r4.get(r3);
        r2 = (java.lang.String) r2;
        r2 = org.telegram.messenger.Utilities.parseInt(r2);
        r3 = r2.intValue();
        if (r3 != 0) goto L_0x093e;
        r2 = 0;
        r47 = r2;
        r2 = r6;
        goto L_0x0948;
        r54 = r2;
        r55 = r3;
        r2 = r48;
        r3 = "start";
        r6 = r1.getQueryParameter(r3);
        r3 = "startgroup";
        r7 = r1.getQueryParameter(r3);
        r3 = "game";
        r11 = r1.getQueryParameter(r3);
        r48 = r2;
        r51 = r6;
        goto L_0x09c6;
        r54 = r2;
        r55 = r3;
        r2 = "url";
        r2 = r1.getQueryParameter(r2);
        if (r2 != 0) goto L_0x096d;
        r2 = "";
        r3 = "text";
        r3 = r1.getQueryParameter(r3);
        if (r3 == 0) goto L_0x09a3;
        r3 = r2.length();
        if (r3 <= 0) goto L_0x098e;
        r25 = 1;
        r3 = new java.lang.StringBuilder;
        r3.<init>();
        r3.append(r2);
        r4 = "\n";
        r3.append(r4);
        r2 = r3.toString();
        r3 = new java.lang.StringBuilder;
        r3.<init>();
        r3.append(r2);
        r4 = "text";
        r4 = r1.getQueryParameter(r4);
        r3.append(r4);
        r2 = r3.toString();
        r3 = r2.length();
        r4 = 16384; // 0x4000 float:2.2959E-41 double:8.0948E-320;
        if (r3 <= r4) goto L_0x09b1;
        r3 = 0;
        r2 = r2.substring(r3, r4);
        goto L_0x09b2;
        r3 = 0;
        r8 = r2;
        r2 = "\n";
        r2 = r8.endsWith(r2);
        if (r2 == 0) goto L_0x09c6;
        r2 = r8.length();
        r4 = 1;
        r2 = r2 - r4;
        r8 = r8.substring(r3, r2);
        goto L_0x09b3;
        goto L_0x09cf;
        r48 = r2;
        r49 = r3;
        r50 = r4;
        r51 = r6;
        r52 = r1;
        r29 = r7;
        r30 = r11;
        r11 = r12;
        r53 = r32;
        r12 = r9;
        r32 = r25;
        r25 = r5;
        if (r8 == 0) goto L_0x09fb;
        r1 = "@";
        r1 = r8.startsWith(r1);
        if (r1 == 0) goto L_0x09fb;
        r1 = new java.lang.StringBuilder;
        r1.<init>();
        r2 = " ";
        r1.append(r2);
        r1.append(r8);
        r1 = r1.toString();
        r35 = r1;
        goto L_0x09fd;
        r35 = r8;
        if (r12 != 0) goto L_0x0adc;
        if (r11 == 0) goto L_0x0a0e;
        r58 = r10;
        r59 = r11;
        r15 = r12;
        r21 = r13;
        r56 = r37;
        r26 = 0;
        goto L_0x0ae7;
        if (r48 != 0) goto L_0x0aa6;
        if (r49 != 0) goto L_0x0aa6;
        if (r50 != 0) goto L_0x0aa6;
        if (r35 != 0) goto L_0x0aa6;
        if (r30 != 0) goto L_0x0aa6;
        if (r25 == 0) goto L_0x0a1c;
        goto L_0x0aa6;
        r1 = r63.getContentResolver();	 Catch:{ Exception -> 0x0a91 }
        r2 = r64.getData();	 Catch:{ Exception -> 0x0a91 }
        r3 = 0;	 Catch:{ Exception -> 0x0a91 }
        r4 = 0;	 Catch:{ Exception -> 0x0a91 }
        r5 = 0;	 Catch:{ Exception -> 0x0a91 }
        r6 = 0;	 Catch:{ Exception -> 0x0a91 }
        r1 = r1.query(r2, r3, r4, r5, r6);	 Catch:{ Exception -> 0x0a91 }
        if (r1 == 0) goto L_0x0a8e;	 Catch:{ Exception -> 0x0a91 }
        r2 = r1.moveToFirst();	 Catch:{ Exception -> 0x0a91 }
        if (r2 == 0) goto L_0x0a86;	 Catch:{ Exception -> 0x0a91 }
        r2 = "account_name";	 Catch:{ Exception -> 0x0a91 }
        r2 = r1.getColumnIndex(r2);	 Catch:{ Exception -> 0x0a91 }
        r2 = r1.getString(r2);	 Catch:{ Exception -> 0x0a91 }
        r2 = org.telegram.messenger.Utilities.parseInt(r2);	 Catch:{ Exception -> 0x0a91 }
        r2 = r2.intValue();	 Catch:{ Exception -> 0x0a91 }
        r3 = 0;
        r9 = 3;
        if (r3 >= r9) goto L_0x0a65;
        r4 = org.telegram.messenger.UserConfig.getInstance(r3);	 Catch:{ Exception -> 0x0a62 }
        r4 = r4.getClientUserId();	 Catch:{ Exception -> 0x0a62 }
        if (r4 != r2) goto L_0x0a5e;	 Catch:{ Exception -> 0x0a62 }
        r4 = 0;	 Catch:{ Exception -> 0x0a62 }
        r10[r4] = r3;	 Catch:{ Exception -> 0x0a62 }
        r5 = r10[r4];	 Catch:{ Exception -> 0x0a62 }
        r8 = 1;
        r14.switchToAccount(r5, r8);	 Catch:{ Exception -> 0x0a8c }
        goto L_0x0a66;	 Catch:{ Exception -> 0x0a8c }
        r8 = 1;	 Catch:{ Exception -> 0x0a8c }
        r3 = r3 + 1;	 Catch:{ Exception -> 0x0a8c }
        goto L_0x0a47;	 Catch:{ Exception -> 0x0a8c }
    L_0x0a62:
        r0 = move-exception;	 Catch:{ Exception -> 0x0a8c }
        r8 = 1;	 Catch:{ Exception -> 0x0a8c }
        goto L_0x0a94;	 Catch:{ Exception -> 0x0a8c }
        r8 = 1;	 Catch:{ Exception -> 0x0a8c }
        r3 = "DATA4";	 Catch:{ Exception -> 0x0a8c }
        r3 = r1.getColumnIndex(r3);	 Catch:{ Exception -> 0x0a8c }
        r3 = r1.getInt(r3);	 Catch:{ Exception -> 0x0a8c }
        r4 = 0;	 Catch:{ Exception -> 0x0a8c }
        r5 = r10[r4];	 Catch:{ Exception -> 0x0a8c }
        r5 = org.telegram.messenger.NotificationCenter.getInstance(r5);	 Catch:{ Exception -> 0x0a8c }
        r6 = org.telegram.messenger.NotificationCenter.closeChats;	 Catch:{ Exception -> 0x0a8c }
        r7 = new java.lang.Object[r4];	 Catch:{ Exception -> 0x0a8c }
        r5.postNotificationName(r6, r7);	 Catch:{ Exception -> 0x0a8c }
        r4 = java.lang.Integer.valueOf(r3);	 Catch:{ Exception -> 0x0a8c }
        r2 = r4;	 Catch:{ Exception -> 0x0a8c }
        r53 = r2;	 Catch:{ Exception -> 0x0a8c }
        goto L_0x0a88;	 Catch:{ Exception -> 0x0a8c }
        r8 = 1;	 Catch:{ Exception -> 0x0a8c }
        r9 = 3;	 Catch:{ Exception -> 0x0a8c }
        r1.close();	 Catch:{ Exception -> 0x0a8c }
        goto L_0x0a90;
    L_0x0a8c:
        r0 = move-exception;
        goto L_0x0a94;
        r8 = 1;
        r9 = 3;
        goto L_0x0a98;
    L_0x0a91:
        r0 = move-exception;
        r8 = 1;
        r9 = 3;
        r1 = r0;
        org.telegram.messenger.FileLog.e(r1);
        r58 = r10;
        r2 = r33;
        r3 = r34;
        r56 = r37;
        r1 = r53;
        r26 = 0;
        goto L_0x0b14;
        r8 = 1;
        r9 = 3;
        r21 = 0;
        r2 = r10[r21];
        r28 = 0;
        r1 = r14;
        r3 = r48;
        r4 = r49;
        r6 = r37;
        r5 = r50;
        r56 = r6;
        r31 = 0;
        r6 = r51;
        r26 = 0;
        r7 = r29;
        r36 = r8;
        r8 = r35;
        r37 = r9;
        r9 = r32;
        r58 = r10;
        r10 = r47;
        r59 = r11;
        r11 = r30;
        r15 = r12;
        r12 = r25;
        r21 = r13;
        r13 = r28;
        r1.runLinkRequest(r2, r3, r4, r5, r6, r7, r8, r9, r10, r11, r12, r13);
        goto L_0x0b01;
        r58 = r10;
        r59 = r11;
        r15 = r12;
        r21 = r13;
        r56 = r37;
        r26 = 0;
        r1 = new android.os.Bundle;
        r1.<init>();
        r2 = "phone";
        r1.putString(r2, r15);
        r2 = "hash";
        r12 = r59;
        r1.putString(r2, r12);
        r2 = new org.telegram.ui.LaunchActivity$7;
        r2.<init>(r1);
        org.telegram.messenger.AndroidUtilities.runOnUIThread(r2);
        r2 = r33;
        r3 = r34;
        r1 = r53;
        goto L_0x0b14;
        r58 = r10;
        r56 = r37;
        r26 = 0;
        r1 = r32;
        r2 = r33;
        r3 = r34;
        r6 = r2;
        r5 = r3;
        r8 = r18;
        r9 = r19;
        r10 = r20;
        r7 = r58;
        r2 = 1;
        r4 = 0;
        r3 = r1;
        r1 = r64;
        goto L_0x0c05;
        r58 = r10;
        r56 = r37;
        r26 = 0;
        r1 = r64;
        r2 = r64.getAction();
        r3 = "org.telegram.messenger.OPEN_ACCOUNT";
        r2 = r2.equals(r3);
        if (r2 == 0) goto L_0x0b4f;
        r2 = 1;
        r19 = java.lang.Integer.valueOf(r2);
        r8 = r18;
        r9 = r19;
        r10 = r20;
        r3 = r32;
        r6 = r33;
        r5 = r34;
        r7 = r58;
        r4 = 0;
        goto L_0x0c05;
        r2 = 1;
        r3 = r64.getAction();
        r4 = "new_dialog";
        r3 = r3.equals(r4);
        if (r3 == 0) goto L_0x0b61;
        r20 = java.lang.Integer.valueOf(r2);
        goto L_0x0b3e;
        r3 = r64.getAction();
        r4 = "com.tmessages.openchat";
        r3 = r3.startsWith(r4);
        if (r3 == 0) goto L_0x0bca;
        r3 = "chatId";
        r4 = 0;
        r3 = r1.getIntExtra(r3, r4);
        r5 = "userId";
        r5 = r1.getIntExtra(r5, r4);
        r6 = "encId";
        r6 = r1.getIntExtra(r6, r4);
        if (r3 == 0) goto L_0x0b98;
        r7 = r58;
        r8 = r7[r4];
        r8 = org.telegram.messenger.NotificationCenter.getInstance(r8);
        r9 = org.telegram.messenger.NotificationCenter.closeChats;
        r10 = new java.lang.Object[r4];
        r8.postNotificationName(r9, r10);
        r8 = java.lang.Integer.valueOf(r3);
        r33 = r8;
        goto L_0x0bc9;
        r7 = r58;
        if (r5 == 0) goto L_0x0bb0;
        r8 = r7[r4];
        r8 = org.telegram.messenger.NotificationCenter.getInstance(r8);
        r9 = org.telegram.messenger.NotificationCenter.closeChats;
        r10 = new java.lang.Object[r4];
        r8.postNotificationName(r9, r10);
        r8 = java.lang.Integer.valueOf(r5);
        r32 = r8;
        goto L_0x0bc9;
        if (r6 == 0) goto L_0x0bc6;
        r8 = r7[r4];
        r8 = org.telegram.messenger.NotificationCenter.getInstance(r8);
        r9 = org.telegram.messenger.NotificationCenter.closeChats;
        r10 = new java.lang.Object[r4];
        r8.postNotificationName(r9, r10);
        r8 = java.lang.Integer.valueOf(r6);
        r18 = r8;
        goto L_0x0bc9;
        r3 = 1;
        r22 = r3;
        goto L_0x0bf9;
        r7 = r58;
        r4 = 0;
        r3 = r64.getAction();
        r5 = "com.tmessages.openplayer";
        r3 = r3.equals(r5);
        if (r3 == 0) goto L_0x0bdc;
        r23 = 1;
        goto L_0x0bf9;
        r3 = r64.getAction();
        r5 = "org.tmessages.openlocations";
        r3 = r3.equals(r5);
        if (r3 == 0) goto L_0x0bf9;
        r24 = 1;
        goto L_0x0bf9;
    L_0x0beb:
        r32 = r1;
        r33 = r2;
        r34 = r3;
        r56 = r5;
        r26 = r7;
        r7 = r10;
        r4 = r11;
        r1 = r15;
        r2 = 1;
        r8 = r18;
        r9 = r19;
        r10 = r20;
        r3 = r32;
        r6 = r33;
        r5 = r34;
        r11 = r3.intValue();
        if (r11 == 0) goto L_0x0c64;
        r11 = new android.os.Bundle;
        r11.<init>();
        r12 = "user_id";
        r13 = r3.intValue();
        r11.putInt(r12, r13);
        r12 = r5.intValue();
        if (r12 == 0) goto L_0x0c28;
        r12 = "message_id";
        r13 = r5.intValue();
        r11.putInt(r12, r13);
        r12 = mainFragmentsStack;
        r12 = r12.isEmpty();
        if (r12 != 0) goto L_0x0c4b;
        r12 = r7[r4];
        r12 = org.telegram.messenger.MessagesController.getInstance(r12);
        r13 = mainFragmentsStack;
        r15 = mainFragmentsStack;
        r15 = r15.size();
        r15 = r15 - r2;
        r13 = r13.get(r15);
        r13 = (org.telegram.ui.ActionBar.BaseFragment) r13;
        r12 = r12.checkCanOpenChat(r11, r13);
        if (r12 == 0) goto L_0x0c5a;
        r12 = new org.telegram.ui.ChatActivity;
        r12.<init>(r11);
        r13 = r14.actionBarLayout;
        r13 = r13.presentFragment(r12, r4, r2, r2);
        if (r13 == 0) goto L_0x0c5a;
        r17 = 1;
        r2 = r65;
        r60 = r3;
        r11 = r56;
        r4 = 0;
        goto L_0x0f4d;
        r11 = r6.intValue();
        if (r11 == 0) goto L_0x0cba;
        r11 = new android.os.Bundle;
        r11.<init>();
        r12 = "chat_id";
        r13 = r6.intValue();
        r11.putInt(r12, r13);
        r12 = r5.intValue();
        if (r12 == 0) goto L_0x0c87;
        r12 = "message_id";
        r13 = r5.intValue();
        r11.putInt(r12, r13);
        r12 = mainFragmentsStack;
        r12 = r12.isEmpty();
        if (r12 != 0) goto L_0x0caa;
        r12 = r7[r4];
        r12 = org.telegram.messenger.MessagesController.getInstance(r12);
        r13 = mainFragmentsStack;
        r15 = mainFragmentsStack;
        r15 = r15.size();
        r15 = r15 - r2;
        r13 = r13.get(r15);
        r13 = (org.telegram.ui.ActionBar.BaseFragment) r13;
        r12 = r12.checkCanOpenChat(r11, r13);
        if (r12 == 0) goto L_0x0cb9;
        r12 = new org.telegram.ui.ChatActivity;
        r12.<init>(r11);
        r13 = r14.actionBarLayout;
        r13 = r13.presentFragment(r12, r4, r2, r2);
        if (r13 == 0) goto L_0x0cb9;
        r17 = 1;
        goto L_0x0c5b;
        r11 = r8.intValue();
        if (r11 == 0) goto L_0x0cdf;
        r11 = new android.os.Bundle;
        r11.<init>();
        r12 = "enc_id";
        r13 = r8.intValue();
        r11.putInt(r12, r13);
        r12 = new org.telegram.ui.ChatActivity;
        r12.<init>(r11);
        r13 = r14.actionBarLayout;
        r13 = r13.presentFragment(r12, r4, r2, r2);
        if (r13 == 0) goto L_0x0cdd;
        r17 = 1;
        goto L_0x0c5b;
        if (r22 == 0) goto L_0x0d23;
        r11 = org.telegram.messenger.AndroidUtilities.isTablet();
        if (r11 != 0) goto L_0x0ced;
        r11 = r14.actionBarLayout;
        r11.removeAllFragments();
        goto L_0x0d1b;
        r11 = r14.layersActionBarLayout;
        r11 = r11.fragmentsStack;
        r11 = r11.isEmpty();
        if (r11 != 0) goto L_0x0d1b;
        r11 = r4;
        r12 = r14.layersActionBarLayout;
        r12 = r12.fragmentsStack;
        r12 = r12.size();
        r12 = r12 - r2;
        if (r11 >= r12) goto L_0x0d16;
        r12 = r14.layersActionBarLayout;
        r13 = r14.layersActionBarLayout;
        r13 = r13.fragmentsStack;
        r13 = r13.get(r4);
        r13 = (org.telegram.ui.ActionBar.BaseFragment) r13;
        r12.removeFragmentFromStack(r13);
        r11 = r11 + -1;
        r11 = r11 + r2;
        goto L_0x0cf8;
        r11 = r14.layersActionBarLayout;
        r11.closeLastFragment(r4);
        r17 = 0;
        r11 = 0;
        r60 = r3;
        r2 = r11;
        goto L_0x0c5f;
        if (r23 == 0) goto L_0x0d45;
        r11 = r14.actionBarLayout;
        r11 = r11.fragmentsStack;
        r11 = r11.isEmpty();
        if (r11 != 0) goto L_0x0d41;
        r11 = r14.actionBarLayout;
        r11 = r11.fragmentsStack;
        r11 = r11.get(r4);
        r11 = (org.telegram.ui.ActionBar.BaseFragment) r11;
        r12 = new org.telegram.ui.Components.AudioPlayerAlert;
        r12.<init>(r14);
        r11.showDialog(r12);
        r17 = 0;
        goto L_0x0c5b;
        if (r24 == 0) goto L_0x0d6c;
        r11 = r14.actionBarLayout;
        r11 = r11.fragmentsStack;
        r11 = r11.isEmpty();
        if (r11 != 0) goto L_0x0d68;
        r11 = r14.actionBarLayout;
        r11 = r11.fragmentsStack;
        r11 = r11.get(r4);
        r11 = (org.telegram.ui.ActionBar.BaseFragment) r11;
        r12 = new org.telegram.ui.Components.SharingLocationsAlert;
        r13 = new org.telegram.ui.LaunchActivity$8;
        r13.<init>(r7);
        r12.<init>(r14, r13);
        r11.showDialog(r12);
        r17 = 0;
        goto L_0x0c5b;
        r11 = r14.videoPath;
        if (r11 != 0) goto L_0x0df4;
        r11 = r14.photoPathsArray;
        if (r11 != 0) goto L_0x0df4;
        r11 = r14.sendingText;
        if (r11 != 0) goto L_0x0df4;
        r11 = r14.documentsPathsArray;
        if (r11 != 0) goto L_0x0df4;
        r11 = r14.contactsToSend;
        if (r11 != 0) goto L_0x0df4;
        r11 = r14.documentsUrisArray;
        if (r11 == 0) goto L_0x0d85;
        goto L_0x0df4;
        r11 = r9.intValue();
        if (r11 == 0) goto L_0x0db4;
        r11 = r14.actionBarLayout;
        r12 = new org.telegram.ui.SettingsActivity;
        r12.<init>();
        r11.presentFragment(r12, r4, r2, r2);
        r11 = org.telegram.messenger.AndroidUtilities.isTablet();
        if (r11 == 0) goto L_0x0dab;
        r11 = r14.actionBarLayout;
        r11.showLastFragment();
        r11 = r14.rightActionBarLayout;
        r11.showLastFragment();
        r11 = r14.drawerLayoutContainer;
        r11.setAllowOpenDrawer(r4, r4);
        goto L_0x0db0;
        r11 = r14.drawerLayoutContainer;
        r11.setAllowOpenDrawer(r2, r4);
        r17 = 1;
        goto L_0x0c5b;
        r11 = r10.intValue();
        if (r11 == 0) goto L_0x0ded;
        r11 = new android.os.Bundle;
        r11.<init>();
        r12 = "destroyAfterSelect";
        r11.putBoolean(r12, r2);
        r12 = r14.actionBarLayout;
        r13 = new org.telegram.ui.ContactsActivity;
        r13.<init>(r11);
        r12.presentFragment(r13, r4, r2, r2);
        r12 = org.telegram.messenger.AndroidUtilities.isTablet();
        if (r12 == 0) goto L_0x0de4;
        r12 = r14.actionBarLayout;
        r12.showLastFragment();
        r12 = r14.rightActionBarLayout;
        r12.showLastFragment();
        r12 = r14.drawerLayoutContainer;
        r12.setAllowOpenDrawer(r4, r4);
        goto L_0x0de9;
        r12 = r14.drawerLayoutContainer;
        r12.setAllowOpenDrawer(r2, r4);
        r17 = 1;
        goto L_0x0c5b;
        r60 = r3;
        r11 = r56;
        r4 = 0;
        goto L_0x0f4b;
        r11 = org.telegram.messenger.AndroidUtilities.isTablet();
        if (r11 != 0) goto L_0x0e07;
        r11 = r7[r4];
        r11 = org.telegram.messenger.NotificationCenter.getInstance(r11);
        r12 = org.telegram.messenger.NotificationCenter.closeChats;
        r13 = new java.lang.Object[r4];
        r11.postNotificationName(r12, r13);
        r11 = r56;
        r13 = (r11 > r26 ? 1 : (r11 == r26 ? 0 : -1));
        if (r13 != 0) goto L_0x0f37;
        r13 = new android.os.Bundle;
        r13.<init>();
        r15 = "onlySelect";
        r13.putBoolean(r15, r2);
        r15 = "dialogsType";
        r4 = 3;
        r13.putInt(r15, r4);
        r4 = "allowSwitchAccount";
        r13.putBoolean(r4, r2);
        r4 = r14.contactsToSend;
        r15 = 2131494349; // 0x7f0c05cd float:1.8612204E38 double:1.053098132E-314;
        if (r4 == 0) goto L_0x0e43;
        r4 = "selectAlertString";
        r2 = "SendContactTo";
        r2 = org.telegram.messenger.LocaleController.getString(r2, r15);
        r13.putString(r4, r2);
        r2 = "selectAlertStringGroup";
        r4 = "SendContactToGroup";
        r15 = 2131494336; // 0x7f0c05c0 float:1.8612178E38 double:1.0530981257E-314;
        r4 = org.telegram.messenger.LocaleController.getString(r4, r15);
        r13.putString(r2, r4);
        goto L_0x0e5c;
        r2 = "selectAlertString";
        r4 = "SendMessagesTo";
        r4 = org.telegram.messenger.LocaleController.getString(r4, r15);
        r13.putString(r2, r4);
        r2 = "selectAlertStringGroup";
        r4 = "SendMessagesToGroup";
        r15 = 2131494350; // 0x7f0c05ce float:1.8612206E38 double:1.0530981326E-314;
        r4 = org.telegram.messenger.LocaleController.getString(r4, r15);
        r13.putString(r2, r4);
        r2 = new org.telegram.ui.DialogsActivity;
        r2.<init>(r13);
        r2.setDelegate(r14);
        r4 = org.telegram.messenger.AndroidUtilities.isTablet();
        if (r4 == 0) goto L_0x0e90;
        r4 = r14.layersActionBarLayout;
        r4 = r4.fragmentsStack;
        r4 = r4.size();
        if (r4 <= 0) goto L_0x0e8e;
        r4 = r14.layersActionBarLayout;
        r4 = r4.fragmentsStack;
        r15 = r14.layersActionBarLayout;
        r15 = r15.fragmentsStack;
        r15 = r15.size();
        r18 = 1;
        r15 = r15 + -1;
        r4 = r4.get(r15);
        r4 = r4 instanceof org.telegram.ui.DialogsActivity;
        if (r4 == 0) goto L_0x0e8e;
        r4 = 1;
        goto L_0x0e8f;
        r4 = 0;
        goto L_0x0eb6;
        r4 = r14.actionBarLayout;
        r4 = r4.fragmentsStack;
        r4 = r4.size();
        r15 = 1;
        if (r4 <= r15) goto L_0x0eb5;
        r4 = r14.actionBarLayout;
        r4 = r4.fragmentsStack;
        r15 = r14.actionBarLayout;
        r15 = r15.fragmentsStack;
        r15 = r15.size();
        r18 = 1;
        r15 = r15 + -1;
        r4 = r4.get(r15);
        r4 = r4 instanceof org.telegram.ui.DialogsActivity;
        if (r4 == 0) goto L_0x0eb5;
        r4 = 1;
        goto L_0x0eb6;
        r4 = 0;
        r15 = r14.actionBarLayout;
        r60 = r3;
        r3 = 1;
        r15.presentFragment(r2, r4, r3, r3);
        r17 = 1;
        r3 = org.telegram.ui.SecretMediaViewer.hasInstance();
        if (r3 == 0) goto L_0x0edc;
        r3 = org.telegram.ui.SecretMediaViewer.getInstance();
        r3 = r3.isVisible();
        if (r3 == 0) goto L_0x0edc;
        r3 = org.telegram.ui.SecretMediaViewer.getInstance();
        r15 = 0;
        r3.closePhoto(r15, r15);
        r61 = r2;
        r3 = r15;
        goto L_0x0f15;
        r3 = org.telegram.ui.PhotoViewer.hasInstance();
        if (r3 == 0) goto L_0x0ef8;
        r3 = org.telegram.ui.PhotoViewer.getInstance();
        r3 = r3.isVisible();
        if (r3 == 0) goto L_0x0ef8;
        r3 = org.telegram.ui.PhotoViewer.getInstance();
        r61 = r2;
        r2 = 1;
        r15 = 0;
        r3.closePhoto(r15, r2);
        goto L_0x0eda;
        r61 = r2;
        r2 = org.telegram.ui.ArticleViewer.hasInstance();
        if (r2 == 0) goto L_0x0f14;
        r2 = org.telegram.ui.ArticleViewer.getInstance();
        r2 = r2.isVisible();
        if (r2 == 0) goto L_0x0f14;
        r2 = org.telegram.ui.ArticleViewer.getInstance();
        r3 = 0;
        r15 = 1;
        r2.close(r3, r15);
        goto L_0x0f15;
        r3 = 0;
        r2 = r14.drawerLayoutContainer;
        r2.setAllowOpenDrawer(r3, r3);
        r2 = org.telegram.messenger.AndroidUtilities.isTablet();
        if (r2 == 0) goto L_0x0f2b;
        r2 = r14.actionBarLayout;
        r2.showLastFragment();
        r2 = r14.rightActionBarLayout;
        r2.showLastFragment();
        goto L_0x0f32;
        r2 = r14.drawerLayoutContainer;
        r3 = 0;
        r15 = 1;
        r2.setAllowOpenDrawer(r15, r3);
        r2 = r65;
        goto L_0x0c61;
        r60 = r3;
        r2 = new java.util.ArrayList;
        r2.<init>();
        r3 = java.lang.Long.valueOf(r11);
        r2.add(r3);
        r3 = 0;
        r4 = 0;
        r14.didSelectDialogs(r4, r2, r4, r3);
        r2 = r65;
        if (r17 != 0) goto L_0x0ff2;
        if (r2 != 0) goto L_0x0ff2;
        r3 = org.telegram.messenger.AndroidUtilities.isTablet();
        if (r3 == 0) goto L_0x0f9f;
        r3 = r14.currentAccount;
        r3 = org.telegram.messenger.UserConfig.getInstance(r3);
        r3 = r3.isClientActivated();
        if (r3 != 0) goto L_0x0f7e;
        r3 = r14.layersActionBarLayout;
        r3 = r3.fragmentsStack;
        r3 = r3.isEmpty();
        if (r3 == 0) goto L_0x0fdd;
        r3 = r14.layersActionBarLayout;
        r13 = new org.telegram.ui.LoginActivity;
        r13.<init>();
        r3.addFragmentToStack(r13);
        r3 = r14.drawerLayoutContainer;
        r13 = 0;
        r3.setAllowOpenDrawer(r13, r13);
        goto L_0x0fdd;
        r3 = r14.actionBarLayout;
        r3 = r3.fragmentsStack;
        r3 = r3.isEmpty();
        if (r3 == 0) goto L_0x0fdd;
        r3 = new org.telegram.ui.DialogsActivity;
        r3.<init>(r4);
        r13 = r14.sideMenu;
        r3.setSideMenu(r13);
        r13 = r14.actionBarLayout;
        r13.addFragmentToStack(r3);
        r13 = r14.drawerLayoutContainer;
        r4 = 1;
        r15 = 0;
        r13.setAllowOpenDrawer(r4, r15);
        goto L_0x0fdd;
        r3 = r14.actionBarLayout;
        r3 = r3.fragmentsStack;
        r3 = r3.isEmpty();
        if (r3 == 0) goto L_0x0fdd;
        r3 = r14.currentAccount;
        r3 = org.telegram.messenger.UserConfig.getInstance(r3);
        r3 = r3.isClientActivated();
        if (r3 != 0) goto L_0x0fc6;
        r3 = r14.actionBarLayout;
        r4 = new org.telegram.ui.LoginActivity;
        r4.<init>();
        r3.addFragmentToStack(r4);
        r3 = r14.drawerLayoutContainer;
        r4 = 0;
        r3.setAllowOpenDrawer(r4, r4);
        goto L_0x0fdd;
        r3 = new org.telegram.ui.DialogsActivity;
        r4 = 0;
        r3.<init>(r4);
        r4 = r14.sideMenu;
        r3.setSideMenu(r4);
        r4 = r14.actionBarLayout;
        r4.addFragmentToStack(r3);
        r4 = r14.drawerLayoutContainer;
        r13 = 0;
        r15 = 1;
        r4.setAllowOpenDrawer(r15, r13);
        r3 = r14.actionBarLayout;
        r3.showLastFragment();
        r3 = org.telegram.messenger.AndroidUtilities.isTablet();
        if (r3 == 0) goto L_0x0ff2;
        r3 = r14.layersActionBarLayout;
        r3.showLastFragment();
        r3 = r14.rightActionBarLayout;
        r3.showLastFragment();
        r3 = 0;
        r1.setAction(r3);
        return r17;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.telegram.ui.LaunchActivity.handleIntent(android.content.Intent, boolean, boolean, boolean):boolean");
    }

    public void onRequestPermissionsResult(int r1, java.lang.String[] r2, int[] r3) {
        /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.DecodeException: Load method exception in method: org.telegram.ui.LaunchActivity.onRequestPermissionsResult(int, java.lang.String[], int[]):void
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
        super.onRequestPermissionsResult(r10, r11, r12);
        r0 = 0;
        r1 = 20;
        r2 = 19;
        r3 = 5;
        r4 = 4;
        r5 = 3;
        if (r10 == r5) goto L_0x002d;
    L_0x000d:
        if (r10 == r4) goto L_0x002d;
    L_0x000f:
        if (r10 == r3) goto L_0x002d;
    L_0x0011:
        if (r10 == r2) goto L_0x002d;
    L_0x0013:
        if (r10 != r1) goto L_0x0016;
    L_0x0015:
        goto L_0x002d;
    L_0x0016:
        r1 = 2;
        if (r10 != r1) goto L_0x00d1;
    L_0x0019:
        r1 = r12.length;
        if (r1 <= 0) goto L_0x00d1;
    L_0x001c:
        r1 = r12[r0];
        if (r1 != 0) goto L_0x00d1;
    L_0x0020:
        r1 = org.telegram.messenger.NotificationCenter.getGlobalInstance();
        r2 = org.telegram.messenger.NotificationCenter.locationPermissionGranted;
        r0 = new java.lang.Object[r0];
        r1.postNotificationName(r2, r0);
        goto L_0x00d1;
    L_0x002d:
        r6 = 1;
        r7 = r12.length;
        if (r7 <= 0) goto L_0x005e;
    L_0x0031:
        r0 = r12[r0];
        if (r0 != 0) goto L_0x005e;
    L_0x0035:
        if (r10 != r4) goto L_0x003f;
    L_0x0037:
        r0 = org.telegram.messenger.ImageLoader.getInstance();
        r0.checkMediaPaths();
        return;
    L_0x003f:
        if (r10 != r3) goto L_0x004b;
    L_0x0041:
        r0 = r9.currentAccount;
        r0 = org.telegram.messenger.ContactsController.getInstance(r0);
        r0.forceImportContacts();
        return;
    L_0x004b:
        if (r10 != r5) goto L_0x0059;
    L_0x004d:
        r0 = org.telegram.messenger.SharedConfig.inappCamera;
        if (r0 == 0) goto L_0x0058;
    L_0x0051:
        r0 = org.telegram.messenger.camera.CameraController.getInstance();
        r0.initCamera();
    L_0x0058:
        return;
    L_0x0059:
        if (r10 == r2) goto L_0x005d;
    L_0x005b:
        if (r10 != r1) goto L_0x005e;
    L_0x005d:
        r6 = 0;
    L_0x005e:
        if (r6 == 0) goto L_0x00d0;
    L_0x0060:
        r0 = new org.telegram.ui.ActionBar.AlertDialog$Builder;
        r0.<init>(r9);
        r7 = "AppName";
        r8 = 2131492981; // 0x7f0c0075 float:1.860943E38 double:1.0530974563E-314;
        r7 = org.telegram.messenger.LocaleController.getString(r7, r8);
        r0.setTitle(r7);
        if (r10 != r5) goto L_0x0080;
        r1 = "PermissionNoAudio";
        r2 = 2131494142; // 0x7f0c04fe float:1.8611784E38 double:1.05309803E-314;
        r1 = org.telegram.messenger.LocaleController.getString(r1, r2);
        r0.setMessage(r1);
        goto L_0x00ae;
        if (r10 != r4) goto L_0x008f;
        r1 = "PermissionStorage";
        r2 = 2131494148; // 0x7f0c0504 float:1.8611796E38 double:1.053098033E-314;
        r1 = org.telegram.messenger.LocaleController.getString(r1, r2);
        r0.setMessage(r1);
        goto L_0x00ae;
        if (r10 != r3) goto L_0x009e;
        r1 = "PermissionContacts";
        r2 = 2131494140; // 0x7f0c04fc float:1.861178E38 double:1.053098029E-314;
        r1 = org.telegram.messenger.LocaleController.getString(r1, r2);
        r0.setMessage(r1);
        goto L_0x00ae;
        if (r10 == r2) goto L_0x00a2;
        if (r10 != r1) goto L_0x00ae;
        r1 = "PermissionNoCamera";
        r2 = 2131494144; // 0x7f0c0500 float:1.8611788E38 double:1.053098031E-314;
        r1 = org.telegram.messenger.LocaleController.getString(r1, r2);
        r0.setMessage(r1);
        r1 = "PermissionOpenSettings";
        r2 = 2131494147; // 0x7f0c0503 float:1.8611794E38 double:1.0530980323E-314;
        r1 = org.telegram.messenger.LocaleController.getString(r1, r2);
        r2 = new org.telegram.ui.LaunchActivity$15;
        r2.<init>();
        r0.setNegativeButton(r1, r2);
        r1 = "OK";
        r2 = 2131494028; // 0x7f0c048c float:1.8611553E38 double:1.0530979736E-314;
        r1 = org.telegram.messenger.LocaleController.getString(r1, r2);
        r2 = 0;
        r0.setPositiveButton(r1, r2);
        r0.show();
        return;
    L_0x00d1:
        r0 = r9.actionBarLayout;
        r0 = r0.fragmentsStack;
        r0 = r0.size();
        if (r0 == 0) goto L_0x00f2;
        r0 = r9.actionBarLayout;
        r0 = r0.fragmentsStack;
        r1 = r9.actionBarLayout;
        r1 = r1.fragmentsStack;
        r1 = r1.size();
        r1 = r1 + -1;
        r0 = r0.get(r1);
        r0 = (org.telegram.ui.ActionBar.BaseFragment) r0;
        r0.onRequestPermissionsResultFragment(r10, r11, r12);
        r0 = org.telegram.messenger.AndroidUtilities.isTablet();
        if (r0 == 0) goto L_0x013a;
        r0 = r9.rightActionBarLayout;
        r0 = r0.fragmentsStack;
        r0 = r0.size();
        if (r0 == 0) goto L_0x0119;
        r0 = r9.rightActionBarLayout;
        r0 = r0.fragmentsStack;
        r1 = r9.rightActionBarLayout;
        r1 = r1.fragmentsStack;
        r1 = r1.size();
        r1 = r1 + -1;
        r0 = r0.get(r1);
        r0 = (org.telegram.ui.ActionBar.BaseFragment) r0;
        r0.onRequestPermissionsResultFragment(r10, r11, r12);
        r0 = r9.layersActionBarLayout;
        r0 = r0.fragmentsStack;
        r0 = r0.size();
        if (r0 == 0) goto L_0x013a;
        r0 = r9.layersActionBarLayout;
        r0 = r0.fragmentsStack;
        r1 = r9.layersActionBarLayout;
        r1 = r1.fragmentsStack;
        r1 = r1.size();
        r1 = r1 + -1;
        r0 = r0.get(r1);
        r0 = (org.telegram.ui.ActionBar.BaseFragment) r0;
        r0.onRequestPermissionsResultFragment(r10, r11, r12);
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.telegram.ui.LaunchActivity.onRequestPermissionsResult(int, java.lang.String[], int[]):void");
    }

    protected void onCreate(Bundle savedInstanceState) {
        String fragmentName;
        ApplicationLoader.postInitApplication();
        AndroidUtilities.checkDisplaySize(this, getResources().getConfiguration());
        this.currentAccount = UserConfig.selectedAccount;
        if (!UserConfig.getInstance(this.currentAccount).isClientActivated()) {
            Intent intent = getIntent();
            if (intent == null || intent.getAction() == null || !("android.intent.action.SEND".equals(intent.getAction()) || intent.getAction().equals("android.intent.action.SEND_MULTIPLE"))) {
                SharedPreferences preferences = MessagesController.getGlobalMainSettings();
                long crashed_time = preferences.getLong("intro_crashed_time", 0);
                boolean fromIntro = intent.getBooleanExtra("fromIntro", false);
                if (fromIntro) {
                    preferences.edit().putLong("intro_crashed_time", 0).commit();
                }
                if (Math.abs(crashed_time - System.currentTimeMillis()) >= 120000 && intent != null && !fromIntro && ApplicationLoader.applicationContext.getSharedPreferences("logininfo2", 0).getAll().isEmpty()) {
                    Intent intent2 = new Intent(this, IntroActivity.class);
                    intent2.setData(intent.getData());
                    startActivity(intent2);
                    super.onCreate(savedInstanceState);
                    finish();
                    return;
                }
            }
            super.onCreate(savedInstanceState);
            finish();
            return;
        }
        requestWindowFeature(1);
        setTheme(R.style.Theme.TMessages);
        if (VERSION.SDK_INT >= 21) {
            try {
                setTaskDescription(new TaskDescription(null, null, Theme.getColor(Theme.key_actionBarDefault) | Theme.ACTION_BAR_VIDEO_EDIT_COLOR));
            } catch (Exception e) {
            }
        }
        getWindow().setBackgroundDrawableResource(R.drawable.transparent);
        if (SharedConfig.passcodeHash.length() > 0 && !SharedConfig.allowScreenCapture) {
            try {
                getWindow().setFlags(MessagesController.UPDATE_MASK_CHANNEL, MessagesController.UPDATE_MASK_CHANNEL);
            } catch (Throwable e2) {
                FileLog.e(e2);
            }
        }
        super.onCreate(savedInstanceState);
        if (VERSION.SDK_INT >= 24) {
            AndroidUtilities.isInMultiwindow = isInMultiWindowMode();
        }
        Theme.createChatResources(this, false);
        if (SharedConfig.passcodeHash.length() != 0 && SharedConfig.appLocked) {
            SharedConfig.lastPauseTime = ConnectionsManager.getInstance(this.currentAccount).getCurrentTime();
        }
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            AndroidUtilities.statusBarHeight = getResources().getDimensionPixelSize(resourceId);
        }
        this.actionBarLayout = new ActionBarLayout(this);
        this.drawerLayoutContainer = new DrawerLayoutContainer(this);
        int i = -1;
        setContentView(this.drawerLayoutContainer, new LayoutParams(-1, -1));
        if (AndroidUtilities.isTablet()) {
            getWindow().setSoftInputMode(16);
            RelativeLayout launchLayout = new RelativeLayout(this) {
                private boolean inLayout;

                public void requestLayout() {
                    if (!this.inLayout) {
                        super.requestLayout();
                    }
                }

                protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
                    this.inLayout = true;
                    int width = MeasureSpec.getSize(widthMeasureSpec);
                    int height = MeasureSpec.getSize(heightMeasureSpec);
                    setMeasuredDimension(width, height);
                    if (AndroidUtilities.isInMultiwindow || (AndroidUtilities.isSmallTablet() && getResources().getConfiguration().orientation != 2)) {
                        LaunchActivity.this.tabletFullSize = true;
                        LaunchActivity.this.actionBarLayout.measure(MeasureSpec.makeMeasureSpec(width, 1073741824), MeasureSpec.makeMeasureSpec(height, 1073741824));
                    } else {
                        LaunchActivity.this.tabletFullSize = false;
                        int leftWidth = (width / 100) * 35;
                        if (leftWidth < AndroidUtilities.dp(320.0f)) {
                            leftWidth = AndroidUtilities.dp(320.0f);
                        }
                        LaunchActivity.this.actionBarLayout.measure(MeasureSpec.makeMeasureSpec(leftWidth, 1073741824), MeasureSpec.makeMeasureSpec(height, 1073741824));
                        LaunchActivity.this.shadowTabletSide.measure(MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(1.0f), 1073741824), MeasureSpec.makeMeasureSpec(height, 1073741824));
                        LaunchActivity.this.rightActionBarLayout.measure(MeasureSpec.makeMeasureSpec(width - leftWidth, 1073741824), MeasureSpec.makeMeasureSpec(height, 1073741824));
                    }
                    LaunchActivity.this.backgroundTablet.measure(MeasureSpec.makeMeasureSpec(width, 1073741824), MeasureSpec.makeMeasureSpec(height, 1073741824));
                    LaunchActivity.this.shadowTablet.measure(MeasureSpec.makeMeasureSpec(width, 1073741824), MeasureSpec.makeMeasureSpec(height, 1073741824));
                    LaunchActivity.this.layersActionBarLayout.measure(MeasureSpec.makeMeasureSpec(Math.min(AndroidUtilities.dp(530.0f), width), 1073741824), MeasureSpec.makeMeasureSpec(Math.min(AndroidUtilities.dp(528.0f), height), 1073741824));
                    this.inLayout = false;
                }

                protected void onLayout(boolean changed, int l, int t, int r, int b) {
                    int leftWidth;
                    int width = r - l;
                    int height = b - t;
                    if (AndroidUtilities.isInMultiwindow || (AndroidUtilities.isSmallTablet() && getResources().getConfiguration().orientation != 2)) {
                        LaunchActivity.this.actionBarLayout.layout(0, 0, LaunchActivity.this.actionBarLayout.getMeasuredWidth(), LaunchActivity.this.actionBarLayout.getMeasuredHeight());
                    } else {
                        leftWidth = (width / 100) * 35;
                        if (leftWidth < AndroidUtilities.dp(320.0f)) {
                            leftWidth = AndroidUtilities.dp(320.0f);
                        }
                        LaunchActivity.this.shadowTabletSide.layout(leftWidth, 0, LaunchActivity.this.shadowTabletSide.getMeasuredWidth() + leftWidth, LaunchActivity.this.shadowTabletSide.getMeasuredHeight());
                        LaunchActivity.this.actionBarLayout.layout(0, 0, LaunchActivity.this.actionBarLayout.getMeasuredWidth(), LaunchActivity.this.actionBarLayout.getMeasuredHeight());
                        LaunchActivity.this.rightActionBarLayout.layout(leftWidth, 0, LaunchActivity.this.rightActionBarLayout.getMeasuredWidth() + leftWidth, LaunchActivity.this.rightActionBarLayout.getMeasuredHeight());
                    }
                    leftWidth = (width - LaunchActivity.this.layersActionBarLayout.getMeasuredWidth()) / 2;
                    int y = (height - LaunchActivity.this.layersActionBarLayout.getMeasuredHeight()) / 2;
                    LaunchActivity.this.layersActionBarLayout.layout(leftWidth, y, LaunchActivity.this.layersActionBarLayout.getMeasuredWidth() + leftWidth, LaunchActivity.this.layersActionBarLayout.getMeasuredHeight() + y);
                    LaunchActivity.this.backgroundTablet.layout(0, 0, LaunchActivity.this.backgroundTablet.getMeasuredWidth(), LaunchActivity.this.backgroundTablet.getMeasuredHeight());
                    LaunchActivity.this.shadowTablet.layout(0, 0, LaunchActivity.this.shadowTablet.getMeasuredWidth(), LaunchActivity.this.shadowTablet.getMeasuredHeight());
                }
            };
            this.drawerLayoutContainer.addView(launchLayout, LayoutHelper.createFrame(-1, -1.0f));
            this.backgroundTablet = new View(this);
            BitmapDrawable drawable = (BitmapDrawable) getResources().getDrawable(R.drawable.catstile);
            drawable.setTileModeXY(TileMode.REPEAT, TileMode.REPEAT);
            this.backgroundTablet.setBackgroundDrawable(drawable);
            launchLayout.addView(this.backgroundTablet, LayoutHelper.createRelative(-1, -1));
            launchLayout.addView(this.actionBarLayout);
            this.rightActionBarLayout = new ActionBarLayout(this);
            this.rightActionBarLayout.init(rightFragmentsStack);
            this.rightActionBarLayout.setDelegate(this);
            launchLayout.addView(this.rightActionBarLayout);
            this.shadowTabletSide = new FrameLayout(this);
            this.shadowTabletSide.setBackgroundColor(1076449908);
            launchLayout.addView(this.shadowTabletSide);
            this.shadowTablet = new FrameLayout(this);
            int i2 = 8;
            this.shadowTablet.setVisibility(layerFragmentsStack.isEmpty() ? 8 : 0);
            this.shadowTablet.setBackgroundColor(Theme.ACTION_BAR_PHOTO_VIEWER_COLOR);
            launchLayout.addView(this.shadowTablet);
            this.shadowTablet.setOnTouchListener(new OnTouchListener() {
                public boolean onTouch(View v, MotionEvent event) {
                    if (LaunchActivity.this.actionBarLayout.fragmentsStack.isEmpty() || event.getAction() != 1) {
                        return false;
                    }
                    float x = event.getX();
                    float y = event.getY();
                    int[] location = new int[2];
                    LaunchActivity.this.layersActionBarLayout.getLocationOnScreen(location);
                    int viewX = location[0];
                    int viewY = location[1];
                    if (!LaunchActivity.this.layersActionBarLayout.checkTransitionAnimation()) {
                        if (x <= ((float) viewX) || x >= ((float) (LaunchActivity.this.layersActionBarLayout.getWidth() + viewX)) || y <= ((float) viewY) || y >= ((float) (LaunchActivity.this.layersActionBarLayout.getHeight() + viewY))) {
                            if (!LaunchActivity.this.layersActionBarLayout.fragmentsStack.isEmpty()) {
                                for (int a = 0; a < LaunchActivity.this.layersActionBarLayout.fragmentsStack.size() - 1; a = (a - 1) + 1) {
                                    LaunchActivity.this.layersActionBarLayout.removeFragmentFromStack((BaseFragment) LaunchActivity.this.layersActionBarLayout.fragmentsStack.get(0));
                                }
                                LaunchActivity.this.layersActionBarLayout.closeLastFragment(true);
                            }
                            return true;
                        }
                    }
                    return false;
                }
            });
            this.shadowTablet.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                }
            });
            this.layersActionBarLayout = new ActionBarLayout(this);
            this.layersActionBarLayout.setRemoveActionBarExtraHeight(true);
            this.layersActionBarLayout.setBackgroundView(this.shadowTablet);
            this.layersActionBarLayout.setUseAlphaAnimations(true);
            this.layersActionBarLayout.setBackgroundResource(R.drawable.boxshadow);
            this.layersActionBarLayout.init(layerFragmentsStack);
            this.layersActionBarLayout.setDelegate(this);
            this.layersActionBarLayout.setDrawerLayoutContainer(this.drawerLayoutContainer);
            ActionBarLayout actionBarLayout = this.layersActionBarLayout;
            if (!layerFragmentsStack.isEmpty()) {
                i2 = 0;
            }
            actionBarLayout.setVisibility(i2);
            launchLayout.addView(this.layersActionBarLayout);
        } else {
            this.drawerLayoutContainer.addView(this.actionBarLayout, new LayoutParams(-1, -1));
        }
        this.sideMenu = new RecyclerListView(this);
        ((DefaultItemAnimator) this.sideMenu.getItemAnimator()).setDelayAnimations(false);
        this.sideMenu.setBackgroundColor(Theme.getColor(Theme.key_chats_menuBackground));
        this.sideMenu.setLayoutManager(new LinearLayoutManager(this, 1, false));
        RecyclerListView recyclerListView = this.sideMenu;
        Adapter drawerLayoutAdapter = new DrawerLayoutAdapter(this);
        this.drawerLayoutAdapter = drawerLayoutAdapter;
        recyclerListView.setAdapter(drawerLayoutAdapter);
        this.drawerLayoutContainer.setDrawerLayout(this.sideMenu);
        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) this.sideMenu.getLayoutParams();
        Point screenSize = AndroidUtilities.getRealScreenSize();
        layoutParams.width = AndroidUtilities.isTablet() ? AndroidUtilities.dp(320.0f) : Math.min(AndroidUtilities.dp(320.0f), Math.min(screenSize.x, screenSize.y) - AndroidUtilities.dp(56.0f));
        layoutParams.height = -1;
        this.sideMenu.setLayoutParams(layoutParams);
        this.sideMenu.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(View view, int position) {
                if (position == 0) {
                    LaunchActivity.this.drawerLayoutAdapter.setAccountsShowed(LaunchActivity.this.drawerLayoutAdapter.isAccountsShowed() ^ true, true);
                } else if (view instanceof DrawerUserCell) {
                    LaunchActivity.this.switchToAccount(((DrawerUserCell) view).getAccountNumber(), true);
                    LaunchActivity.this.drawerLayoutContainer.closeDrawer(false);
                } else if (view instanceof DrawerAddCell) {
                    int freeAccount = -1;
                    for (a = 0; a < 3; a++) {
                        if (!UserConfig.getInstance(a).isClientActivated()) {
                            freeAccount = a;
                            break;
                        }
                    }
                    if (freeAccount >= 0) {
                        LaunchActivity.this.presentFragment(new LoginActivity(freeAccount));
                    }
                    LaunchActivity.this.drawerLayoutContainer.closeDrawer(false);
                } else {
                    a = LaunchActivity.this.drawerLayoutAdapter.getId(position);
                    if (a == 2) {
                        LaunchActivity.this.presentFragment(new GroupCreateActivity());
                        LaunchActivity.this.drawerLayoutContainer.closeDrawer(false);
                    } else if (a == 3) {
                        Bundle args = new Bundle();
                        args.putBoolean("onlyUsers", true);
                        args.putBoolean("destroyAfterSelect", true);
                        args.putBoolean("createSecretChat", true);
                        args.putBoolean("allowBots", false);
                        LaunchActivity.this.presentFragment(new ContactsActivity(args));
                        LaunchActivity.this.drawerLayoutContainer.closeDrawer(false);
                    } else if (a == 4) {
                        SharedPreferences preferences = MessagesController.getGlobalMainSettings();
                        if (BuildVars.DEBUG_VERSION || !preferences.getBoolean("channel_intro", false)) {
                            LaunchActivity.this.presentFragment(new ChannelIntroActivity());
                            preferences.edit().putBoolean("channel_intro", true).commit();
                        } else {
                            args = new Bundle();
                            args.putInt("step", 0);
                            LaunchActivity.this.presentFragment(new ChannelCreateActivity(args));
                        }
                        LaunchActivity.this.drawerLayoutContainer.closeDrawer(false);
                    } else if (a == 6) {
                        LaunchActivity.this.presentFragment(new ContactsActivity(null));
                        LaunchActivity.this.drawerLayoutContainer.closeDrawer(false);
                    } else if (a == 7) {
                        LaunchActivity.this.presentFragment(new InviteContactsActivity());
                        LaunchActivity.this.drawerLayoutContainer.closeDrawer(false);
                    } else if (a == 8) {
                        LaunchActivity.this.presentFragment(new SettingsActivity());
                        LaunchActivity.this.drawerLayoutContainer.closeDrawer(false);
                    } else if (a == 9) {
                        Browser.openUrl(LaunchActivity.this, LocaleController.getString("TelegramFaqUrl", R.string.TelegramFaqUrl));
                        LaunchActivity.this.drawerLayoutContainer.closeDrawer(false);
                    } else if (a == 10) {
                        LaunchActivity.this.presentFragment(new CallLogActivity());
                        LaunchActivity.this.drawerLayoutContainer.closeDrawer(false);
                    } else if (a == 11) {
                        args = new Bundle();
                        args.putInt("user_id", UserConfig.getInstance(LaunchActivity.this.currentAccount).getClientUserId());
                        LaunchActivity.this.presentFragment(new ChatActivity(args));
                        LaunchActivity.this.drawerLayoutContainer.closeDrawer(false);
                    }
                }
            }
        });
        this.drawerLayoutContainer.setParentActionBarLayout(this.actionBarLayout);
        this.actionBarLayout.setDrawerLayoutContainer(this.drawerLayoutContainer);
        this.actionBarLayout.init(mainFragmentsStack);
        this.actionBarLayout.setDelegate(this);
        Theme.loadWallpaper();
        this.passcodeView = new PasscodeView(this);
        this.drawerLayoutContainer.addView(this.passcodeView, LayoutHelper.createFrame(-1, -1.0f));
        checkCurrentAccount();
        NotificationCenter.getGlobalInstance().postNotificationName(NotificationCenter.closeOtherAppActivities, this);
        this.currentConnectionState = ConnectionsManager.getInstance(this.currentAccount).getConnectionState();
        NotificationCenter.getGlobalInstance().addObserver(this, NotificationCenter.reloadInterface);
        NotificationCenter.getGlobalInstance().addObserver(this, NotificationCenter.suggestedLangpack);
        NotificationCenter.getGlobalInstance().addObserver(this, NotificationCenter.didSetNewTheme);
        NotificationCenter.getGlobalInstance().addObserver(this, NotificationCenter.needSetDayNightTheme);
        NotificationCenter.getGlobalInstance().addObserver(this, NotificationCenter.closeOtherAppActivities);
        NotificationCenter.getGlobalInstance().addObserver(this, NotificationCenter.didSetPasscode);
        NotificationCenter.getGlobalInstance().addObserver(this, NotificationCenter.didSetNewWallpapper);
        NotificationCenter.getGlobalInstance().addObserver(this, NotificationCenter.notificationsCountUpdated);
        if (this.actionBarLayout.fragmentsStack.isEmpty()) {
            if (UserConfig.getInstance(this.currentAccount).isClientActivated()) {
                DialogsActivity dialogsActivity = new DialogsActivity(null);
                dialogsActivity.setSideMenu(this.sideMenu);
                this.actionBarLayout.addFragmentToStack(dialogsActivity);
                this.drawerLayoutContainer.setAllowOpenDrawer(true, false);
            } else {
                this.actionBarLayout.addFragmentToStack(new LoginActivity());
                this.drawerLayoutContainer.setAllowOpenDrawer(false, false);
            }
            if (savedInstanceState != null) {
                try {
                    fragmentName = savedInstanceState.getString("fragment");
                    if (fragmentName != null) {
                        Bundle args = savedInstanceState.getBundle("args");
                        switch (fragmentName.hashCode()) {
                            case -1529105743:
                                if (fragmentName.equals("wallpapers")) {
                                    i = 6;
                                    break;
                                }
                                break;
                            case -1349522494:
                                if (fragmentName.equals("chat_profile")) {
                                    i = 5;
                                    break;
                                }
                                break;
                            case 3052376:
                                if (fragmentName.equals("chat")) {
                                    i = 0;
                                    break;
                                }
                                break;
                            case 3108362:
                                if (fragmentName.equals("edit")) {
                                    i = 4;
                                    break;
                                }
                                break;
                            case 98629247:
                                if (fragmentName.equals("group")) {
                                    i = 2;
                                    break;
                                }
                                break;
                            case 738950403:
                                if (fragmentName.equals("channel")) {
                                    i = 3;
                                    break;
                                }
                                break;
                            case 1434631203:
                                if (fragmentName.equals("settings")) {
                                    i = 1;
                                    break;
                                }
                                break;
                            default:
                                break;
                        }
                        switch (i) {
                            case 0:
                                if (args != null) {
                                    ChatActivity chat = new ChatActivity(args);
                                    if (this.actionBarLayout.addFragmentToStack(chat)) {
                                        chat.restoreSelfArgs(savedInstanceState);
                                    }
                                    break;
                                }
                                break;
                            case 1:
                                SettingsActivity settings = new SettingsActivity();
                                this.actionBarLayout.addFragmentToStack(settings);
                                settings.restoreSelfArgs(savedInstanceState);
                                break;
                            case 2:
                                if (args != null) {
                                    GroupCreateFinalActivity group = new GroupCreateFinalActivity(args);
                                    if (this.actionBarLayout.addFragmentToStack(group)) {
                                        group.restoreSelfArgs(savedInstanceState);
                                    }
                                    break;
                                }
                                break;
                            case 3:
                                if (args != null) {
                                    ChannelCreateActivity channel = new ChannelCreateActivity(args);
                                    if (this.actionBarLayout.addFragmentToStack(channel)) {
                                        channel.restoreSelfArgs(savedInstanceState);
                                    }
                                    break;
                                }
                                break;
                            case 4:
                                if (args != null) {
                                    ChannelEditActivity channel2 = new ChannelEditActivity(args);
                                    if (this.actionBarLayout.addFragmentToStack(channel2)) {
                                        channel2.restoreSelfArgs(savedInstanceState);
                                    }
                                    break;
                                }
                                break;
                            case 5:
                                if (args != null) {
                                    ProfileActivity profile = new ProfileActivity(args);
                                    if (this.actionBarLayout.addFragmentToStack(profile)) {
                                        profile.restoreSelfArgs(savedInstanceState);
                                    }
                                    break;
                                }
                                break;
                            case 6:
                                WallpapersActivity settings2 = new WallpapersActivity();
                                this.actionBarLayout.addFragmentToStack(settings2);
                                settings2.restoreSelfArgs(savedInstanceState);
                                break;
                            default:
                                break;
                        }
                    }
                } catch (Throwable e3) {
                    FileLog.e(e3);
                }
            }
        } else {
            BaseFragment fragment = (BaseFragment) this.actionBarLayout.fragmentsStack.get(0);
            if (fragment instanceof DialogsActivity) {
                ((DialogsActivity) fragment).setSideMenu(this.sideMenu);
            }
            boolean allowOpen = true;
            if (AndroidUtilities.isTablet()) {
                boolean z = this.actionBarLayout.fragmentsStack.size() <= 1 && this.layersActionBarLayout.fragmentsStack.isEmpty();
                allowOpen = z;
                if (this.layersActionBarLayout.fragmentsStack.size() == 1 && (this.layersActionBarLayout.fragmentsStack.get(0) instanceof LoginActivity)) {
                    allowOpen = false;
                }
            }
            if (this.actionBarLayout.fragmentsStack.size() == 1 && (this.actionBarLayout.fragmentsStack.get(0) instanceof LoginActivity)) {
                allowOpen = false;
            }
            this.drawerLayoutContainer.setAllowOpenDrawer(allowOpen, false);
        }
        checkLayout();
        handleIntent(getIntent(), false, savedInstanceState != null, false);
        try {
            String os1 = Build.DISPLAY;
            fragmentName = Build.USER;
            if (os1 != null) {
                os1 = os1.toLowerCase();
            } else {
                os1 = TtmlNode.ANONYMOUS_REGION_ID;
            }
            if (fragmentName != null) {
                fragmentName = os1.toLowerCase();
            } else {
                fragmentName = TtmlNode.ANONYMOUS_REGION_ID;
            }
            if (os1.contains("flyme") || os2.contains("flyme")) {
                AndroidUtilities.incorrectDisplaySizeFix = true;
                final View view = getWindow().getDecorView().getRootView();
                ViewTreeObserver viewTreeObserver = view.getViewTreeObserver();
                OnGlobalLayoutListener anonymousClass5 = new OnGlobalLayoutListener() {
                    public void onGlobalLayout() {
                        int height = view.getMeasuredHeight();
                        if (VERSION.SDK_INT >= 21) {
                            height -= AndroidUtilities.statusBarHeight;
                        }
                        if (height > AndroidUtilities.dp(100.0f) && height < AndroidUtilities.displaySize.y && AndroidUtilities.dp(100.0f) + height > AndroidUtilities.displaySize.y) {
                            AndroidUtilities.displaySize.y = height;
                            if (BuildVars.LOGS_ENABLED) {
                                StringBuilder stringBuilder = new StringBuilder();
                                stringBuilder.append("fix display size y to ");
                                stringBuilder.append(AndroidUtilities.displaySize.y);
                                FileLog.d(stringBuilder.toString());
                            }
                        }
                    }
                };
                this.onGlobalLayoutListener = anonymousClass5;
                viewTreeObserver.addOnGlobalLayoutListener(anonymousClass5);
            }
        } catch (Throwable e4) {
            FileLog.e(e4);
        }
        MediaController.getInstance().setBaseActivity(this, true);
    }

    public void switchToAccount(int account, boolean removeAll) {
        if (account != UserConfig.selectedAccount) {
            ConnectionsManager.getInstance(this.currentAccount).setAppPaused(true, false);
            UserConfig.selectedAccount = account;
            UserConfig.getInstance(0).saveConfig(false);
            checkCurrentAccount();
            if (AndroidUtilities.isTablet()) {
                this.layersActionBarLayout.removeAllFragments();
                this.rightActionBarLayout.removeAllFragments();
                if (!this.tabletFullSize) {
                    this.shadowTabletSide.setVisibility(0);
                    if (this.rightActionBarLayout.fragmentsStack.isEmpty()) {
                        this.backgroundTablet.setVisibility(0);
                    }
                }
            }
            if (removeAll) {
                this.actionBarLayout.removeAllFragments();
            } else {
                this.actionBarLayout.removeFragmentFromStack(0);
            }
            DialogsActivity dialogsActivity = new DialogsActivity(null);
            dialogsActivity.setSideMenu(this.sideMenu);
            this.actionBarLayout.addFragmentToStack(dialogsActivity, 0);
            this.drawerLayoutContainer.setAllowOpenDrawer(true, false);
            this.actionBarLayout.showLastFragment();
            if (AndroidUtilities.isTablet()) {
                this.layersActionBarLayout.showLastFragment();
                this.rightActionBarLayout.showLastFragment();
            }
            if (!ApplicationLoader.mainInterfacePaused) {
                ConnectionsManager.getInstance(this.currentAccount).setAppPaused(false, false);
            }
        }
    }

    private void switchToAvailableAccountOrLogout() {
        int account = -1;
        for (int a = 0; a < 3; a++) {
            if (UserConfig.getInstance(a).isClientActivated()) {
                account = a;
                break;
            }
        }
        if (account != -1) {
            switchToAccount(account, true);
            return;
        }
        if (this.drawerLayoutAdapter != null) {
            this.drawerLayoutAdapter.notifyDataSetChanged();
        }
        Iterator it = this.actionBarLayout.fragmentsStack.iterator();
        while (it.hasNext()) {
            ((BaseFragment) it.next()).onFragmentDestroy();
        }
        this.actionBarLayout.fragmentsStack.clear();
        if (AndroidUtilities.isTablet()) {
            it = this.layersActionBarLayout.fragmentsStack.iterator();
            while (it.hasNext()) {
                ((BaseFragment) it.next()).onFragmentDestroy();
            }
            this.layersActionBarLayout.fragmentsStack.clear();
            it = this.rightActionBarLayout.fragmentsStack.iterator();
            while (it.hasNext()) {
                ((BaseFragment) it.next()).onFragmentDestroy();
            }
            this.rightActionBarLayout.fragmentsStack.clear();
        }
        startActivity(new Intent(this, IntroActivity.class));
        onFinish();
        finish();
    }

    public int getMainFragmentsCount() {
        return mainFragmentsStack.size();
    }

    private void checkCurrentAccount() {
        if (this.currentAccount != UserConfig.selectedAccount) {
            NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.appDidLogout);
            NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.mainUserInfoChanged);
            NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.didUpdatedConnectionState);
            NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.needShowAlert);
            NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.wasUnableToFindCurrentLocation);
            NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.openArticle);
            NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.hasNewContactsToImport);
        }
        this.currentAccount = UserConfig.selectedAccount;
        NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.appDidLogout);
        NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.mainUserInfoChanged);
        NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.didUpdatedConnectionState);
        NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.needShowAlert);
        NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.wasUnableToFindCurrentLocation);
        NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.openArticle);
        NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.hasNewContactsToImport);
        updateCurrentConnectionState(this.currentAccount);
    }

    private void checkLayout() {
        if (AndroidUtilities.isTablet()) {
            if (this.rightActionBarLayout != null) {
                int i = 8;
                int a;
                BaseFragment chatFragment;
                if (AndroidUtilities.isInMultiwindow || (AndroidUtilities.isSmallTablet() && getResources().getConfiguration().orientation != 2)) {
                    this.tabletFullSize = true;
                    if (!this.rightActionBarLayout.fragmentsStack.isEmpty()) {
                        for (a = 0; a < this.rightActionBarLayout.fragmentsStack.size(); a = (a - 1) + 1) {
                            chatFragment = (BaseFragment) this.rightActionBarLayout.fragmentsStack.get(a);
                            if (chatFragment instanceof ChatActivity) {
                                ((ChatActivity) chatFragment).setIgnoreAttachOnPause(true);
                            }
                            chatFragment.onPause();
                            this.rightActionBarLayout.fragmentsStack.remove(a);
                            this.actionBarLayout.fragmentsStack.add(chatFragment);
                        }
                        if (this.passcodeView.getVisibility() != 0) {
                            this.actionBarLayout.showLastFragment();
                        }
                    }
                    this.shadowTabletSide.setVisibility(8);
                    this.rightActionBarLayout.setVisibility(8);
                    View view = this.backgroundTablet;
                    if (this.actionBarLayout.fragmentsStack.isEmpty()) {
                        i = 0;
                    }
                    view.setVisibility(i);
                } else {
                    this.tabletFullSize = false;
                    if (this.actionBarLayout.fragmentsStack.size() >= 2) {
                        for (a = 1; a < this.actionBarLayout.fragmentsStack.size(); a = (a - 1) + 1) {
                            chatFragment = (BaseFragment) this.actionBarLayout.fragmentsStack.get(a);
                            if (chatFragment instanceof ChatActivity) {
                                ((ChatActivity) chatFragment).setIgnoreAttachOnPause(true);
                            }
                            chatFragment.onPause();
                            this.actionBarLayout.fragmentsStack.remove(a);
                            this.rightActionBarLayout.fragmentsStack.add(chatFragment);
                        }
                        if (this.passcodeView.getVisibility() != 0) {
                            this.actionBarLayout.showLastFragment();
                            this.rightActionBarLayout.showLastFragment();
                        }
                    }
                    this.rightActionBarLayout.setVisibility(this.rightActionBarLayout.fragmentsStack.isEmpty() ? 8 : 0);
                    this.backgroundTablet.setVisibility(this.rightActionBarLayout.fragmentsStack.isEmpty() ? 0 : 8);
                    FrameLayout frameLayout = this.shadowTabletSide;
                    if (!this.actionBarLayout.fragmentsStack.isEmpty()) {
                        i = 0;
                    }
                    frameLayout.setVisibility(i);
                }
            }
        }
    }

    private void showPasscodeActivity() {
        if (this.passcodeView != null) {
            SharedConfig.appLocked = true;
            if (SecretMediaViewer.hasInstance() && SecretMediaViewer.getInstance().isVisible()) {
                SecretMediaViewer.getInstance().closePhoto(false, false);
            } else if (PhotoViewer.hasInstance() && PhotoViewer.getInstance().isVisible()) {
                PhotoViewer.getInstance().closePhoto(false, true);
            } else if (ArticleViewer.hasInstance() && ArticleViewer.getInstance().isVisible()) {
                ArticleViewer.getInstance().close(false, true);
            }
            this.passcodeView.onShow();
            SharedConfig.isWaitingForPasscodeEnter = true;
            this.drawerLayoutContainer.setAllowOpenDrawer(false, false);
            this.passcodeView.setDelegate(new PasscodeViewDelegate() {
                public void didAcceptedPassword() {
                    SharedConfig.isWaitingForPasscodeEnter = false;
                    if (LaunchActivity.this.passcodeSaveIntent != null) {
                        LaunchActivity.this.handleIntent(LaunchActivity.this.passcodeSaveIntent, LaunchActivity.this.passcodeSaveIntentIsNew, LaunchActivity.this.passcodeSaveIntentIsRestore, true);
                        LaunchActivity.this.passcodeSaveIntent = null;
                    }
                    LaunchActivity.this.drawerLayoutContainer.setAllowOpenDrawer(true, false);
                    LaunchActivity.this.actionBarLayout.showLastFragment();
                    if (AndroidUtilities.isTablet()) {
                        LaunchActivity.this.layersActionBarLayout.showLastFragment();
                        LaunchActivity.this.rightActionBarLayout.showLastFragment();
                    }
                }
            });
        }
    }

    private void runLinkRequest(int intentAccount, String username, String group, String sticker, String botUser, String botChat, String message, boolean hasUrl, Integer messageId, String game, String[] instantView, int state) {
        int i;
        LaunchActivity launchActivity;
        AlertDialog progressDialog;
        int i2 = intentAccount;
        String str = username;
        String str2 = group;
        String str3 = sticker;
        String str4 = message;
        int i3 = state;
        AlertDialog progressDialog2 = new AlertDialog(this, 1);
        progressDialog2.setMessage(LocaleController.getString("Loading", R.string.Loading));
        progressDialog2.setCanceledOnTouchOutside(false);
        progressDialog2.setCancelable(false);
        int requestId = 0;
        final AlertDialog alertDialog;
        final String str5;
        final String str6;
        AlertDialog progressDialog3;
        boolean z;
        int i4;
        String str7;
        String str8;
        if (str != null) {
            TL_contacts_resolveUsername req = new TL_contacts_resolveUsername();
            req.username = str;
            alertDialog = progressDialog2;
            final String str9 = game;
            AnonymousClass9 anonymousClass9 = r1;
            final int i5 = i2;
            ConnectionsManager instance = ConnectionsManager.getInstance(intentAccount);
            str5 = botChat;
            TL_contacts_resolveUsername req2 = req;
            str6 = botUser;
            progressDialog3 = progressDialog2;
            final Integer progressDialog4 = messageId;
            AnonymousClass9 anonymousClass92 = new RequestDelegate() {
                public void run(final TLObject response, final TL_error error) {
                    AndroidUtilities.runOnUIThread(new Runnable() {
                        public void run() {
                            if (!LaunchActivity.this.isFinishing()) {
                                try {
                                    alertDialog.dismiss();
                                } catch (Throwable e) {
                                    FileLog.e(e);
                                }
                                final TL_contacts_resolvedPeer res = response;
                                if (error != null || LaunchActivity.this.actionBarLayout == null || (str9 != null && (str9 == null || res.users.isEmpty()))) {
                                    try {
                                        Toast.makeText(LaunchActivity.this, LocaleController.getString("NoUsernameFound", R.string.NoUsernameFound), 0).show();
                                    } catch (Throwable e2) {
                                        FileLog.e(e2);
                                    }
                                } else {
                                    MessagesController.getInstance(i5).putUsers(res.users, false);
                                    MessagesController.getInstance(i5).putChats(res.chats, false);
                                    MessagesStorage.getInstance(i5).putUsersAndChats(res.users, res.chats, false, true);
                                    if (str9 != null) {
                                        Bundle args = new Bundle();
                                        args.putBoolean("onlySelect", true);
                                        args.putBoolean("cantSendToChannels", true);
                                        args.putInt("dialogsType", 1);
                                        args.putString("selectAlertString", LocaleController.getString("SendGameTo", R.string.SendGameTo));
                                        args.putString("selectAlertStringGroup", LocaleController.getString("SendGameToGroup", R.string.SendGameToGroup));
                                        DialogsActivity fragment = new DialogsActivity(args);
                                        fragment.setDelegate(new DialogsActivityDelegate() {
                                            public void didSelectDialogs(DialogsActivity fragment, ArrayList<Long> dids, CharSequence message, boolean param) {
                                                long did = ((Long) dids.get(0)).longValue();
                                                TL_inputMediaGame inputMediaGame = new TL_inputMediaGame();
                                                inputMediaGame.id = new TL_inputGameShortName();
                                                inputMediaGame.id.short_name = str9;
                                                inputMediaGame.id.bot_id = MessagesController.getInstance(i5).getInputUser((User) res.users.get(0));
                                                SendMessagesHelper.getInstance(i5).sendGame(MessagesController.getInstance(i5).getInputPeer((int) did), inputMediaGame, 0, 0);
                                                Bundle args = new Bundle();
                                                args.putBoolean("scrollToTopOnResume", true);
                                                int lower_part = (int) did;
                                                int high_id = (int) (did >> 32);
                                                if (lower_part == 0) {
                                                    args.putInt("enc_id", high_id);
                                                } else if (high_id == 1) {
                                                    args.putInt("chat_id", lower_part);
                                                } else if (lower_part > 0) {
                                                    args.putInt("user_id", lower_part);
                                                } else if (lower_part < 0) {
                                                    args.putInt("chat_id", -lower_part);
                                                }
                                                if (MessagesController.getInstance(i5).checkCanOpenChat(args, fragment)) {
                                                    NotificationCenter.getInstance(i5).postNotificationName(NotificationCenter.closeChats, new Object[0]);
                                                    LaunchActivity.this.actionBarLayout.presentFragment(new ChatActivity(args), true, false, true);
                                                }
                                            }
                                        });
                                        boolean removeLast = AndroidUtilities.isTablet() ? LaunchActivity.this.layersActionBarLayout.fragmentsStack.size() > 0 && (LaunchActivity.this.layersActionBarLayout.fragmentsStack.get(LaunchActivity.this.layersActionBarLayout.fragmentsStack.size() - 1) instanceof DialogsActivity) : LaunchActivity.this.actionBarLayout.fragmentsStack.size() > 1 && (LaunchActivity.this.actionBarLayout.fragmentsStack.get(LaunchActivity.this.actionBarLayout.fragmentsStack.size() - 1) instanceof DialogsActivity);
                                        LaunchActivity.this.actionBarLayout.presentFragment(fragment, removeLast, true, true);
                                        if (SecretMediaViewer.hasInstance() && SecretMediaViewer.getInstance().isVisible()) {
                                            SecretMediaViewer.getInstance().closePhoto(false, false);
                                        } else if (PhotoViewer.hasInstance() && PhotoViewer.getInstance().isVisible()) {
                                            PhotoViewer.getInstance().closePhoto(false, true);
                                        } else if (ArticleViewer.hasInstance() && ArticleViewer.getInstance().isVisible()) {
                                            ArticleViewer.getInstance().close(false, true);
                                        }
                                        LaunchActivity.this.drawerLayoutContainer.setAllowOpenDrawer(false, false);
                                        if (AndroidUtilities.isTablet()) {
                                            LaunchActivity.this.actionBarLayout.showLastFragment();
                                            LaunchActivity.this.rightActionBarLayout.showLastFragment();
                                        } else {
                                            LaunchActivity.this.drawerLayoutContainer.setAllowOpenDrawer(true, false);
                                        }
                                    } else {
                                        BaseFragment lastFragment = null;
                                        if (str5 != null) {
                                            if (!res.users.isEmpty()) {
                                                lastFragment = (User) res.users.get(0);
                                            }
                                            final BaseFragment user = lastFragment;
                                            if (user != null) {
                                                if (!user.bot || !user.bot_nochats) {
                                                    Bundle args2 = new Bundle();
                                                    args2.putBoolean("onlySelect", true);
                                                    args2.putInt("dialogsType", 2);
                                                    args2.putString("addToGroupAlertString", LocaleController.formatString("AddToTheGroupTitle", R.string.AddToTheGroupTitle, UserObject.getUserName(user), "%1$s"));
                                                    DialogsActivity fragment2 = new DialogsActivity(args2);
                                                    fragment2.setDelegate(new DialogsActivityDelegate() {
                                                        public void didSelectDialogs(DialogsActivity fragment, ArrayList<Long> dids, CharSequence message, boolean param) {
                                                            AnonymousClass2 anonymousClass2 = this;
                                                            long did = ((Long) dids.get(0)).longValue();
                                                            Bundle args = new Bundle();
                                                            args.putBoolean("scrollToTopOnResume", true);
                                                            args.putInt("chat_id", -((int) did));
                                                            if (LaunchActivity.mainFragmentsStack.isEmpty() || MessagesController.getInstance(i5).checkCanOpenChat(args, (BaseFragment) LaunchActivity.mainFragmentsStack.get(LaunchActivity.mainFragmentsStack.size() - 1))) {
                                                                NotificationCenter.getInstance(i5).postNotificationName(NotificationCenter.closeChats, new Object[0]);
                                                                MessagesController.getInstance(i5).addUserToChat(-((int) did), user, null, 0, str5, null);
                                                                LaunchActivity.this.actionBarLayout.presentFragment(new ChatActivity(args), true, false, true);
                                                            }
                                                        }
                                                    });
                                                    LaunchActivity.this.presentFragment(fragment2);
                                                }
                                            }
                                            try {
                                                Toast.makeText(LaunchActivity.this, LocaleController.getString("BotCantJoinGroups", R.string.BotCantJoinGroups), 0).show();
                                            } catch (Throwable e3) {
                                                FileLog.e(e3);
                                            }
                                            return;
                                        }
                                        boolean isBot = false;
                                        Bundle args3 = new Bundle();
                                        long dialog_id;
                                        if (res.chats.isEmpty()) {
                                            args3.putInt("user_id", ((User) res.users.get(0)).id);
                                            dialog_id = (long) ((User) res.users.get(0)).id;
                                        } else {
                                            args3.putInt("chat_id", ((Chat) res.chats.get(0)).id);
                                            dialog_id = (long) (-((Chat) res.chats.get(0)).id);
                                        }
                                        if (str6 != null && res.users.size() > 0 && ((User) res.users.get(0)).bot) {
                                            args3.putString("botUser", str6);
                                            isBot = true;
                                        }
                                        if (progressDialog4 != null) {
                                            args3.putInt("message_id", progressDialog4.intValue());
                                        }
                                        if (!LaunchActivity.mainFragmentsStack.isEmpty()) {
                                            lastFragment = (BaseFragment) LaunchActivity.mainFragmentsStack.get(LaunchActivity.mainFragmentsStack.size() - 1);
                                        }
                                        if (lastFragment == null || MessagesController.getInstance(i5).checkCanOpenChat(args3, lastFragment)) {
                                            if (isBot && lastFragment != null && (lastFragment instanceof ChatActivity) && ((ChatActivity) lastFragment).getDialogId() == dialog_id) {
                                                ((ChatActivity) lastFragment).setBotUser(str6);
                                            } else {
                                                ChatActivity fragment3 = new ChatActivity(args3);
                                                NotificationCenter.getInstance(i5).postNotificationName(NotificationCenter.closeChats, new Object[0]);
                                                LaunchActivity.this.actionBarLayout.presentFragment(fragment3, false, true, true);
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    });
                }
            };
            requestId = instance.sendRequest(req2, anonymousClass9);
            z = hasUrl;
            i4 = i3;
            str7 = str2;
            i = i2;
            launchActivity = r15;
            progressDialog = progressDialog3;
            str8 = message;
        } else {
            progressDialog3 = progressDialog2;
            if (str2 == null) {
                i4 = i3;
                str7 = str2;
                i = i2;
                launchActivity = r15;
                progressDialog = progressDialog3;
                str2 = sticker;
                if (str2 != null) {
                    if (!mainFragmentsStack.isEmpty()) {
                        InputStickerSet stickerset = new TL_inputStickerSetShortName();
                        stickerset.short_name = str2;
                        BaseFragment fragment = (BaseFragment) mainFragmentsStack.get(mainFragmentsStack.size() - 1);
                        fragment.showDialog(new StickersAlert(launchActivity, fragment, stickerset, null, null));
                    }
                    return;
                }
                str8 = message;
                if (str8 != null) {
                    Bundle args = new Bundle();
                    args.putBoolean("onlySelect", true);
                    DialogsActivity fragment2 = new DialogsActivity(args);
                    z = hasUrl;
                    fragment2.setDelegate(new DialogsActivityDelegate() {
                        public void didSelectDialogs(DialogsActivity fragment, ArrayList<Long> dids, CharSequence m, boolean param) {
                            long did = ((Long) dids.get(0)).longValue();
                            Bundle args = new Bundle();
                            args.putBoolean("scrollToTopOnResume", true);
                            args.putBoolean("hasUrl", z);
                            int lower_part = (int) did;
                            int high_id = (int) (did >> 32);
                            if (lower_part == 0) {
                                args.putInt("enc_id", high_id);
                            } else if (high_id == 1) {
                                args.putInt("chat_id", lower_part);
                            } else if (lower_part > 0) {
                                args.putInt("user_id", lower_part);
                            } else if (lower_part < 0) {
                                args.putInt("chat_id", -lower_part);
                            }
                            if (MessagesController.getInstance(i).checkCanOpenChat(args, fragment)) {
                                NotificationCenter.getInstance(i).postNotificationName(NotificationCenter.closeChats, new Object[0]);
                                DataQuery.getInstance(i).saveDraft(did, str8, null, null, false);
                                LaunchActivity.this.actionBarLayout.presentFragment(new ChatActivity(args), true, false, true);
                            }
                        }
                    });
                    presentFragment(fragment2, false, true);
                } else {
                    z = hasUrl;
                }
            } else if (i3 == 0) {
                TLObject req3 = new TL_messages_checkChatInvite();
                req3.hash = str2;
                alertDialog = progressDialog3;
                final int i6 = i2;
                final String str10 = str2;
                str5 = username;
                str6 = sticker;
                str7 = botUser;
                final String str11 = botChat;
                AnonymousClass10 anonymousClass10 = r1;
                str4 = message;
                ConnectionsManager instance2 = ConnectionsManager.getInstance(intentAccount);
                TLObject req4 = req3;
                final boolean z2 = hasUrl;
                final Integer num = messageId;
                TLObject req5 = req4;
                r15 = 2;
                str = game;
                final String[] strArr = instantView;
                AnonymousClass10 anonymousClass102 = new RequestDelegate() {
                    public void run(final TLObject response, final TL_error error) {
                        AndroidUtilities.runOnUIThread(new Runnable() {
                            public void run() {
                                if (!LaunchActivity.this.isFinishing()) {
                                    try {
                                        alertDialog.dismiss();
                                    } catch (Throwable e) {
                                        FileLog.e(e);
                                    }
                                    if (error != null || LaunchActivity.this.actionBarLayout == null) {
                                        Builder builder = new Builder(LaunchActivity.this);
                                        builder.setTitle(LocaleController.getString("AppName", R.string.AppName));
                                        if (error.text.startsWith("FLOOD_WAIT")) {
                                            builder.setMessage(LocaleController.getString("FloodWait", R.string.FloodWait));
                                        } else {
                                            builder.setMessage(LocaleController.getString("JoinToGroupErrorNotExist", R.string.JoinToGroupErrorNotExist));
                                        }
                                        builder.setPositiveButton(LocaleController.getString("OK", R.string.OK), null);
                                        LaunchActivity.this.showAlertDialog(builder);
                                        return;
                                    }
                                    ChatInvite invite = response;
                                    if (invite.chat != null && !ChatObject.isLeftFromChat(invite.chat)) {
                                        MessagesController.getInstance(i6).putChat(invite.chat, false);
                                        ArrayList<Chat> chats = new ArrayList();
                                        chats.add(invite.chat);
                                        MessagesStorage.getInstance(i6).putUsersAndChats(null, chats, false, true);
                                        Bundle args = new Bundle();
                                        args.putInt("chat_id", invite.chat.id);
                                        if (LaunchActivity.mainFragmentsStack.isEmpty() || MessagesController.getInstance(i6).checkCanOpenChat(args, (BaseFragment) LaunchActivity.mainFragmentsStack.get(LaunchActivity.mainFragmentsStack.size() - 1))) {
                                            ChatActivity fragment = new ChatActivity(args);
                                            NotificationCenter.getInstance(i6).postNotificationName(NotificationCenter.closeChats, new Object[0]);
                                            LaunchActivity.this.actionBarLayout.presentFragment(fragment, false, true, true);
                                        }
                                    } else if (((invite.chat != null || (invite.channel && !invite.megagroup)) && (invite.chat == null || (ChatObject.isChannel(invite.chat) && !invite.chat.megagroup))) || LaunchActivity.mainFragmentsStack.isEmpty()) {
                                        Builder builder2 = new Builder(LaunchActivity.this);
                                        builder2.setTitle(LocaleController.getString("AppName", R.string.AppName));
                                        String str = "ChannelJoinTo";
                                        Object[] objArr = new Object[1];
                                        objArr[0] = invite.chat != null ? invite.chat.title : invite.title;
                                        builder2.setMessage(LocaleController.formatString(str, R.string.ChannelJoinTo, objArr));
                                        builder2.setPositiveButton(LocaleController.getString("OK", R.string.OK), new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                LaunchActivity.this.runLinkRequest(i6, str5, str10, str6, str7, str11, str4, z2, num, str, strArr, 1);
                                            }
                                        });
                                        builder2.setNegativeButton(LocaleController.getString("Cancel", R.string.Cancel), null);
                                        LaunchActivity.this.showAlertDialog(builder2);
                                    } else {
                                        BaseFragment fragment2 = (BaseFragment) LaunchActivity.mainFragmentsStack.get(LaunchActivity.mainFragmentsStack.size() - 1);
                                        fragment2.showDialog(new JoinGroupAlert(LaunchActivity.this, invite, str10, fragment2));
                                    }
                                }
                            }
                        });
                    }
                };
                requestId = instance2.sendRequest(req5, anonymousClass10, r15);
                i = intentAccount;
                str8 = message;
                z = hasUrl;
                progressDialog = progressDialog3;
                i4 = state;
                str7 = group;
                launchActivity = this;
            } else {
                r15 = 2;
                if (state == 1) {
                    TL_messages_importChatInvite req6 = new TL_messages_importChatInvite();
                    req6.hash = group;
                    i = intentAccount;
                    progressDialog = progressDialog3;
                    ConnectionsManager.getInstance(intentAccount).sendRequest(req6, new RequestDelegate() {
                        public void run(final TLObject response, final TL_error error) {
                            if (error == null) {
                                MessagesController.getInstance(i).processUpdates((Updates) response, false);
                            }
                            AndroidUtilities.runOnUIThread(new Runnable() {
                                public void run() {
                                    if (!LaunchActivity.this.isFinishing()) {
                                        try {
                                            progressDialog.dismiss();
                                        } catch (Throwable e) {
                                            FileLog.e(e);
                                        }
                                        if (error != null) {
                                            Builder builder = new Builder(LaunchActivity.this);
                                            builder.setTitle(LocaleController.getString("AppName", R.string.AppName));
                                            if (error.text.startsWith("FLOOD_WAIT")) {
                                                builder.setMessage(LocaleController.getString("FloodWait", R.string.FloodWait));
                                            } else if (error.text.equals("USERS_TOO_MUCH")) {
                                                builder.setMessage(LocaleController.getString("JoinToGroupErrorFull", R.string.JoinToGroupErrorFull));
                                            } else {
                                                builder.setMessage(LocaleController.getString("JoinToGroupErrorNotExist", R.string.JoinToGroupErrorNotExist));
                                            }
                                            builder.setPositiveButton(LocaleController.getString("OK", R.string.OK), null);
                                            LaunchActivity.this.showAlertDialog(builder);
                                        } else if (LaunchActivity.this.actionBarLayout != null) {
                                            Updates updates = response;
                                            if (!updates.chats.isEmpty()) {
                                                Chat chat = (Chat) updates.chats.get(0);
                                                chat.left = false;
                                                chat.kicked = false;
                                                MessagesController.getInstance(i).putUsers(updates.users, false);
                                                MessagesController.getInstance(i).putChats(updates.chats, false);
                                                Bundle args = new Bundle();
                                                args.putInt("chat_id", chat.id);
                                                if (LaunchActivity.mainFragmentsStack.isEmpty() || MessagesController.getInstance(i).checkCanOpenChat(args, (BaseFragment) LaunchActivity.mainFragmentsStack.get(LaunchActivity.mainFragmentsStack.size() - 1))) {
                                                    ChatActivity fragment = new ChatActivity(args);
                                                    NotificationCenter.getInstance(i).postNotificationName(NotificationCenter.closeChats, new Object[0]);
                                                    LaunchActivity.this.actionBarLayout.presentFragment(fragment, false, true, true);
                                                }
                                            }
                                        }
                                    }
                                }
                            });
                        }
                    }, r15);
                    str8 = message;
                    z = hasUrl;
                } else {
                    i = intentAccount;
                    progressDialog = progressDialog3;
                    str7 = group;
                    launchActivity = this;
                    str8 = message;
                    z = hasUrl;
                    str2 = sticker;
                }
            }
            if (requestId != 0) {
                final int reqId = requestId;
                progressDialog.setButton(-2, LocaleController.getString("Cancel", R.string.Cancel), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        ConnectionsManager.getInstance(i).cancelRequest(reqId, true);
                        try {
                            dialog.dismiss();
                        } catch (Throwable e) {
                            FileLog.e(e);
                        }
                    }
                });
                try {
                    progressDialog.show();
                } catch (Exception e) {
                }
            }
        }
        str2 = sticker;
        if (requestId != 0) {
            final int reqId2 = requestId;
            progressDialog.setButton(-2, LocaleController.getString("Cancel", R.string.Cancel), /* anonymous class already generated */);
            progressDialog.show();
        }
    }

    public AlertDialog showAlertDialog(Builder builder) {
        try {
            if (this.visibleDialog != null) {
                this.visibleDialog.dismiss();
                this.visibleDialog = null;
            }
        } catch (Throwable e) {
            FileLog.e(e);
        }
        try {
            this.visibleDialog = builder.show();
            this.visibleDialog.setCanceledOnTouchOutside(true);
            this.visibleDialog.setOnDismissListener(new OnDismissListener() {
                public void onDismiss(DialogInterface dialog) {
                    if (LaunchActivity.this.visibleDialog != null && LaunchActivity.this.visibleDialog == LaunchActivity.this.localeDialog) {
                        try {
                            Toast.makeText(LaunchActivity.this, LaunchActivity.this.getStringForLanguageAlert(LocaleController.getInstance().getCurrentLocaleInfo().shortName.equals("en") ? LaunchActivity.this.englishLocaleStrings : LaunchActivity.this.systemLocaleStrings, "ChangeLanguageLater", R.string.ChangeLanguageLater), 1).show();
                        } catch (Throwable e) {
                            FileLog.e(e);
                        }
                        LaunchActivity.this.localeDialog = null;
                    }
                    LaunchActivity.this.visibleDialog = null;
                }
            });
            return this.visibleDialog;
        } catch (Throwable e2) {
            FileLog.e(e2);
            return null;
        }
    }

    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        handleIntent(intent, true, false, false);
    }

    public void didSelectDialogs(DialogsActivity dialogsFragment, ArrayList<Long> dids, CharSequence message, boolean param) {
        LaunchActivity launchActivity = this;
        BaseFragment baseFragment = dialogsFragment;
        long did = ((Long) dids.get(0)).longValue();
        int lower_part = (int) did;
        int high_id = (int) (did >> 32);
        Bundle args = new Bundle();
        int account = baseFragment != null ? dialogsFragment.getCurrentAccount() : launchActivity.currentAccount;
        args.putBoolean("scrollToTopOnResume", true);
        if (!AndroidUtilities.isTablet()) {
            NotificationCenter.getInstance(account).postNotificationName(NotificationCenter.closeChats, new Object[0]);
        }
        if (lower_part == 0) {
            args.putInt("enc_id", high_id);
        } else if (high_id == 1) {
            args.putInt("chat_id", lower_part);
        } else if (lower_part > 0) {
            args.putInt("user_id", lower_part);
        } else if (lower_part < 0) {
            args.putInt("chat_id", -lower_part);
        }
        if (MessagesController.getInstance(account).checkCanOpenChat(args, baseFragment)) {
            int i;
            Iterator it;
            int account2;
            ChatActivity fragment = new ChatActivity(args);
            launchActivity.actionBarLayout.presentFragment(fragment, baseFragment != null, baseFragment == null, true);
            if (launchActivity.videoPath != null) {
                fragment.openVideoEditor(launchActivity.videoPath, launchActivity.sendingText);
                launchActivity.sendingText = null;
            }
            if (launchActivity.photoPathsArray != null) {
                if (launchActivity.sendingText != null && launchActivity.sendingText.length() <= Callback.DEFAULT_DRAG_ANIMATION_DURATION && launchActivity.photoPathsArray.size() == 1) {
                    ((SendingMediaInfo) launchActivity.photoPathsArray.get(0)).caption = launchActivity.sendingText;
                    launchActivity.sendingText = null;
                }
                i = account;
                SendMessagesHelper.prepareSendingMedia(launchActivity.photoPathsArray, did, null, null, false, 0);
            } else {
                i = account;
            }
            if (launchActivity.sendingText != null) {
                SendMessagesHelper.prepareSendingText(launchActivity.sendingText, did);
            }
            if (launchActivity.documentsPathsArray == null) {
                if (launchActivity.documentsUrisArray == null) {
                    Bundle bundle = args;
                    if (!(launchActivity.contactsToSend == null || launchActivity.contactsToSend.isEmpty())) {
                        it = launchActivity.contactsToSend.iterator();
                        while (it.hasNext()) {
                            account = i;
                            account2 = account;
                            SendMessagesHelper.getInstance(account).sendMessage((User) it.next(), did, null, null, null);
                            i = account2;
                        }
                    }
                    launchActivity.photoPathsArray = null;
                    launchActivity.videoPath = null;
                    launchActivity.sendingText = null;
                    launchActivity.documentsPathsArray = null;
                    launchActivity.documentsOriginalPathsArray = null;
                    launchActivity.contactsToSend = null;
                }
            }
            SendMessagesHelper.prepareSendingDocuments(launchActivity.documentsPathsArray, launchActivity.documentsOriginalPathsArray, launchActivity.documentsUrisArray, launchActivity.documentsMimeType, did, null, null);
            it = launchActivity.contactsToSend.iterator();
            while (it.hasNext()) {
                account = i;
                account2 = account;
                SendMessagesHelper.getInstance(account).sendMessage((User) it.next(), did, null, null, null);
                i = account2;
            }
            launchActivity.photoPathsArray = null;
            launchActivity.videoPath = null;
            launchActivity.sendingText = null;
            launchActivity.documentsPathsArray = null;
            launchActivity.documentsOriginalPathsArray = null;
            launchActivity.contactsToSend = null;
        }
    }

    private void onFinish() {
        if (!this.finished) {
            this.finished = true;
            if (this.lockRunnable != null) {
                AndroidUtilities.cancelRunOnUIThread(this.lockRunnable);
                this.lockRunnable = null;
            }
            if (this.currentAccount != -1) {
                NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.appDidLogout);
                NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.mainUserInfoChanged);
                NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.didUpdatedConnectionState);
                NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.needShowAlert);
                NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.wasUnableToFindCurrentLocation);
                NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.openArticle);
                NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.hasNewContactsToImport);
            }
            NotificationCenter.getGlobalInstance().removeObserver(this, NotificationCenter.didSetNewWallpapper);
            NotificationCenter.getGlobalInstance().removeObserver(this, NotificationCenter.suggestedLangpack);
            NotificationCenter.getGlobalInstance().removeObserver(this, NotificationCenter.reloadInterface);
            NotificationCenter.getGlobalInstance().removeObserver(this, NotificationCenter.didSetNewTheme);
            NotificationCenter.getGlobalInstance().removeObserver(this, NotificationCenter.needSetDayNightTheme);
            NotificationCenter.getGlobalInstance().removeObserver(this, NotificationCenter.closeOtherAppActivities);
            NotificationCenter.getGlobalInstance().removeObserver(this, NotificationCenter.didSetPasscode);
            NotificationCenter.getGlobalInstance().removeObserver(this, NotificationCenter.notificationsCountUpdated);
        }
    }

    public void presentFragment(BaseFragment fragment) {
        this.actionBarLayout.presentFragment(fragment);
    }

    public boolean presentFragment(BaseFragment fragment, boolean removeLast, boolean forceWithoutAnimation) {
        return this.actionBarLayout.presentFragment(fragment, removeLast, forceWithoutAnimation, true);
    }

    public ActionBarLayout getActionBarLayout() {
        return this.actionBarLayout;
    }

    public ActionBarLayout getLayersActionBarLayout() {
        return this.layersActionBarLayout;
    }

    public ActionBarLayout getRightActionBarLayout() {
        return this.rightActionBarLayout;
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (!(SharedConfig.passcodeHash.length() == 0 || SharedConfig.lastPauseTime == 0)) {
            SharedConfig.lastPauseTime = 0;
            UserConfig.getInstance(this.currentAccount).saveConfig(false);
        }
        super.onActivityResult(requestCode, resultCode, data);
        ThemeEditorView editorView = ThemeEditorView.getInstance();
        if (editorView != null) {
            editorView.onActivityResult(requestCode, resultCode, data);
        }
        if (this.actionBarLayout.fragmentsStack.size() != 0) {
            ((BaseFragment) this.actionBarLayout.fragmentsStack.get(this.actionBarLayout.fragmentsStack.size() - 1)).onActivityResultFragment(requestCode, resultCode, data);
        }
        if (AndroidUtilities.isTablet()) {
            if (this.rightActionBarLayout.fragmentsStack.size() != 0) {
                ((BaseFragment) this.rightActionBarLayout.fragmentsStack.get(this.rightActionBarLayout.fragmentsStack.size() - 1)).onActivityResultFragment(requestCode, resultCode, data);
            }
            if (this.layersActionBarLayout.fragmentsStack.size() != 0) {
                ((BaseFragment) this.layersActionBarLayout.fragmentsStack.get(this.layersActionBarLayout.fragmentsStack.size() - 1)).onActivityResultFragment(requestCode, resultCode, data);
            }
        }
    }

    protected void onPause() {
        super.onPause();
        SharedConfig.lastAppPauseTime = System.currentTimeMillis();
        ApplicationLoader.mainInterfacePaused = true;
        Utilities.stageQueue.postRunnable(new Runnable() {
            public void run() {
                ApplicationLoader.mainInterfacePausedStageQueue = true;
                ApplicationLoader.mainInterfacePausedStageQueueTime = 0;
            }
        });
        onPasscodePause();
        this.actionBarLayout.onPause();
        if (AndroidUtilities.isTablet()) {
            this.rightActionBarLayout.onPause();
            this.layersActionBarLayout.onPause();
        }
        if (this.passcodeView != null) {
            this.passcodeView.onPause();
        }
        ConnectionsManager.getInstance(this.currentAccount).setAppPaused(true, false);
        AndroidUtilities.unregisterUpdates();
        if (PhotoViewer.hasInstance() && PhotoViewer.getInstance().isVisible()) {
            PhotoViewer.getInstance().onPause();
        }
    }

    protected void onStart() {
        super.onStart();
        Browser.bindCustomTabsService(this);
    }

    protected void onStop() {
        super.onStop();
        Browser.unbindCustomTabsService(this);
    }

    protected void onDestroy() {
        if (PhotoViewer.getPipInstance() != null) {
            PhotoViewer.getPipInstance().destroyPhotoViewer();
        }
        if (PhotoViewer.hasInstance()) {
            PhotoViewer.getInstance().destroyPhotoViewer();
        }
        if (SecretMediaViewer.hasInstance()) {
            SecretMediaViewer.getInstance().destroyPhotoViewer();
        }
        if (ArticleViewer.hasInstance()) {
            ArticleViewer.getInstance().destroyArticleViewer();
        }
        if (StickerPreviewViewer.hasInstance()) {
            StickerPreviewViewer.getInstance().destroy();
        }
        PipRoundVideoView pipRoundVideoView = PipRoundVideoView.getInstance();
        MediaController.getInstance().setBaseActivity(this, false);
        MediaController.getInstance().setFeedbackView(this.actionBarLayout, false);
        if (pipRoundVideoView != null) {
            pipRoundVideoView.close(false);
        }
        Theme.destroyResources();
        EmbedBottomSheet embedBottomSheet = EmbedBottomSheet.getInstance();
        if (embedBottomSheet != null) {
            embedBottomSheet.destroy();
        }
        ThemeEditorView editorView = ThemeEditorView.getInstance();
        if (editorView != null) {
            editorView.destroy();
        }
        try {
            if (this.visibleDialog != null) {
                this.visibleDialog.dismiss();
                this.visibleDialog = null;
            }
        } catch (Throwable e) {
            FileLog.e(e);
        }
        try {
            if (this.onGlobalLayoutListener != null) {
                getWindow().getDecorView().getRootView().getViewTreeObserver().removeOnGlobalLayoutListener(this.onGlobalLayoutListener);
            }
        } catch (Throwable e2) {
            FileLog.e(e2);
        }
        super.onDestroy();
        onFinish();
    }

    protected void onResume() {
        super.onResume();
        MediaController.getInstance().setFeedbackView(this.actionBarLayout, true);
        showLanguageAlert(false);
        ApplicationLoader.mainInterfacePaused = false;
        NotificationsController.lastNoDataNotificationTime = 0;
        Utilities.stageQueue.postRunnable(new Runnable() {
            public void run() {
                ApplicationLoader.mainInterfacePausedStageQueue = false;
                ApplicationLoader.mainInterfacePausedStageQueueTime = System.currentTimeMillis();
            }
        });
        checkFreeDiscSpace();
        MediaController.checkGallery();
        onPasscodeResume();
        if (this.passcodeView.getVisibility() != 0) {
            this.actionBarLayout.onResume();
            if (AndroidUtilities.isTablet()) {
                this.rightActionBarLayout.onResume();
                this.layersActionBarLayout.onResume();
            }
        } else {
            this.actionBarLayout.dismissDialogs();
            if (AndroidUtilities.isTablet()) {
                this.rightActionBarLayout.dismissDialogs();
                this.layersActionBarLayout.dismissDialogs();
            }
            this.passcodeView.onResume();
        }
        AndroidUtilities.checkForCrashes(this);
        AndroidUtilities.checkForUpdates(this);
        ConnectionsManager.getInstance(this.currentAccount).setAppPaused(false, false);
        updateCurrentConnectionState(this.currentAccount);
        if (PhotoViewer.hasInstance() && PhotoViewer.getInstance().isVisible()) {
            PhotoViewer.getInstance().onResume();
        }
        if (PipRoundVideoView.getInstance() != null && MediaController.getInstance().isMessagePaused()) {
            MessageObject messageObject = MediaController.getInstance().getPlayingMessageObject();
            if (messageObject != null) {
                MediaController.getInstance().seekToProgress(messageObject, messageObject.audioProgress);
            }
        }
    }

    public void onConfigurationChanged(Configuration newConfig) {
        AndroidUtilities.checkDisplaySize(this, newConfig);
        super.onConfigurationChanged(newConfig);
        checkLayout();
        PipRoundVideoView pipRoundVideoView = PipRoundVideoView.getInstance();
        if (pipRoundVideoView != null) {
            pipRoundVideoView.onConfigurationChanged();
        }
        EmbedBottomSheet embedBottomSheet = EmbedBottomSheet.getInstance();
        if (embedBottomSheet != null) {
            embedBottomSheet.onConfigurationChanged(newConfig);
        }
        PhotoViewer photoViewer = PhotoViewer.getPipInstance();
        if (photoViewer != null) {
            photoViewer.onConfigurationChanged(newConfig);
        }
        ThemeEditorView editorView = ThemeEditorView.getInstance();
        if (editorView != null) {
            editorView.onConfigurationChanged();
        }
    }

    public void onMultiWindowModeChanged(boolean isInMultiWindowMode) {
        AndroidUtilities.isInMultiwindow = isInMultiWindowMode;
        checkLayout();
    }

    public void didReceivedNotification(int id, int account, Object... args) {
        final int i;
        Context context = this;
        int i2 = id;
        final int i3 = account;
        if (i2 == NotificationCenter.appDidLogout) {
            switchToAvailableAccountOrLogout();
        } else if (i2 == NotificationCenter.closeOtherAppActivities) {
            if (args[0] != context) {
                onFinish();
                finish();
            }
        } else if (i2 == NotificationCenter.didUpdatedConnectionState) {
            int state = ConnectionsManager.getInstance(account).getConnectionState();
            if (context.currentConnectionState != state) {
                if (BuildVars.LOGS_ENABLED) {
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("switch to state ");
                    stringBuilder.append(state);
                    FileLog.d(stringBuilder.toString());
                }
                context.currentConnectionState = state;
                updateCurrentConnectionState(i3);
            }
        } else if (i2 == NotificationCenter.mainUserInfoChanged) {
            context.drawerLayoutAdapter.notifyDataSetChanged();
        } else if (i2 == NotificationCenter.needShowAlert) {
            Integer reason = args[0];
            Builder builder = new Builder(context);
            builder.setTitle(LocaleController.getString("AppName", R.string.AppName));
            builder.setPositiveButton(LocaleController.getString("OK", R.string.OK), null);
            if (reason.intValue() != 2) {
                builder.setNegativeButton(LocaleController.getString("MoreInfo", R.string.MoreInfo), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (!LaunchActivity.mainFragmentsStack.isEmpty()) {
                            MessagesController.getInstance(i3).openByUserName("spambot", (BaseFragment) LaunchActivity.mainFragmentsStack.get(LaunchActivity.mainFragmentsStack.size() - 1), 1);
                        }
                    }
                });
            }
            if (reason.intValue() == 0) {
                builder.setMessage(LocaleController.getString("NobodyLikesSpam1", R.string.NobodyLikesSpam1));
            } else if (reason.intValue() == 1) {
                builder.setMessage(LocaleController.getString("NobodyLikesSpam2", R.string.NobodyLikesSpam2));
            } else if (reason.intValue() == 2) {
                builder.setMessage((String) args[1]);
            }
            if (!mainFragmentsStack.isEmpty()) {
                ((BaseFragment) mainFragmentsStack.get(mainFragmentsStack.size() - 1)).showDialog(builder.create());
            }
        } else if (i2 == NotificationCenter.wasUnableToFindCurrentLocation) {
            final HashMap<String, MessageObject> waitingForLocation = args[0];
            Builder builder2 = new Builder(context);
            builder2.setTitle(LocaleController.getString("AppName", R.string.AppName));
            builder2.setPositiveButton(LocaleController.getString("OK", R.string.OK), null);
            builder2.setNegativeButton(LocaleController.getString("ShareYouLocationUnableManually", R.string.ShareYouLocationUnableManually), new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogInterface, int i) {
                    if (!LaunchActivity.mainFragmentsStack.isEmpty() && AndroidUtilities.isGoogleMapsInstalled((BaseFragment) LaunchActivity.mainFragmentsStack.get(LaunchActivity.mainFragmentsStack.size() - 1))) {
                        LocationActivity fragment = new LocationActivity(0);
                        fragment.setDelegate(new LocationActivityDelegate() {
                            public void didSelectLocation(MessageMedia location, int live) {
                                for (Entry<String, MessageObject> entry : waitingForLocation.entrySet()) {
                                    MessageObject messageObject = (MessageObject) entry.getValue();
                                    SendMessagesHelper.getInstance(i3).sendMessage(location, messageObject.getDialogId(), messageObject, null, null);
                                }
                            }
                        });
                        LaunchActivity.this.presentFragment(fragment);
                    }
                }
            });
            builder2.setMessage(LocaleController.getString("ShareYouLocationUnable", R.string.ShareYouLocationUnable));
            if (!mainFragmentsStack.isEmpty()) {
                ((BaseFragment) mainFragmentsStack.get(mainFragmentsStack.size() - 1)).showDialog(builder2.create());
            }
        } else if (i2 == NotificationCenter.didSetNewWallpapper) {
            if (context.sideMenu != null) {
                View child = context.sideMenu.getChildAt(0);
                if (child != null) {
                    child.invalidate();
                }
            }
        } else if (i2 == NotificationCenter.didSetPasscode) {
            if (SharedConfig.passcodeHash.length() <= 0 || SharedConfig.allowScreenCapture) {
                try {
                    getWindow().clearFlags(MessagesController.UPDATE_MASK_CHANNEL);
                } catch (Throwable e) {
                    FileLog.e(e);
                }
            } else {
                try {
                    getWindow().setFlags(MessagesController.UPDATE_MASK_CHANNEL, MessagesController.UPDATE_MASK_CHANNEL);
                } catch (Throwable e2) {
                    FileLog.e(e2);
                }
            }
        } else if (i2 == NotificationCenter.reloadInterface) {
            rebuildAllFragments(false);
        } else if (i2 == NotificationCenter.suggestedLangpack) {
            showLanguageAlert(false);
        } else if (i2 != NotificationCenter.openArticle) {
            if (i2 == NotificationCenter.hasNewContactsToImport) {
                if (context.actionBarLayout != null) {
                    if (!context.actionBarLayout.fragmentsStack.isEmpty()) {
                        int type = ((Integer) args[0]).intValue();
                        HashMap<String, Contact> contactHashMap = args[1];
                        boolean first = ((Boolean) args[2]).booleanValue();
                        boolean schedule = ((Boolean) args[3]).booleanValue();
                        BaseFragment fragment = (BaseFragment) context.actionBarLayout.fragmentsStack.get(context.actionBarLayout.fragmentsStack.size() - 1);
                        Builder builder3 = new Builder(context);
                        builder3.setTitle(LocaleController.getString("UpdateContactsTitle", R.string.UpdateContactsTitle));
                        builder3.setMessage(LocaleController.getString("UpdateContactsMessage", R.string.UpdateContactsMessage));
                        AnonymousClass20 anonymousClass20 = r1;
                        i = i3;
                        String string = LocaleController.getString("OK", R.string.OK);
                        final HashMap<String, Contact> hashMap = contactHashMap;
                        Builder builder4 = builder3;
                        final boolean z = first;
                        BaseFragment fragment2 = fragment;
                        final boolean z2 = schedule;
                        AnonymousClass20 anonymousClass202 = new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogInterface, int i) {
                                ContactsController.getInstance(i).syncPhoneBookByAlert(hashMap, z, z2, false);
                            }
                        };
                        builder4.setPositiveButton(string, anonymousClass20);
                        builder4.setNegativeButton(LocaleController.getString("Cancel", R.string.Cancel), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                ContactsController.getInstance(i).syncPhoneBookByAlert(hashMap, z, z2, true);
                            }
                        });
                        builder4.setOnBackButtonListener(new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogInterface, int i) {
                                ContactsController.getInstance(i).syncPhoneBookByAlert(hashMap, z, z2, true);
                            }
                        });
                        AlertDialog dialog = builder4.create();
                        fragment2.showDialog(dialog);
                        dialog.setCanceledOnTouchOutside(false);
                        i = id;
                    }
                }
                return;
            }
            int i4 = 0;
            i = id;
            if (i == NotificationCenter.didSetNewTheme) {
                if (!args[i4].booleanValue()) {
                    if (context.sideMenu != null) {
                        context.sideMenu.setBackgroundColor(Theme.getColor(Theme.key_chats_menuBackground));
                        context.sideMenu.setGlowColor(Theme.getColor(Theme.key_chats_menuBackground));
                        context.sideMenu.getAdapter().notifyDataSetChanged();
                    }
                    if (VERSION.SDK_INT >= 21) {
                        try {
                            setTaskDescription(new TaskDescription(null, null, Theme.getColor(Theme.key_actionBarDefault) | Theme.ACTION_BAR_VIDEO_EDIT_COLOR));
                        } catch (Exception e3) {
                        }
                    }
                }
            } else if (i == NotificationCenter.needSetDayNightTheme) {
                ThemeInfo theme = args[0];
                context.actionBarLayout.animateThemedValues(theme);
                if (AndroidUtilities.isTablet()) {
                    context.layersActionBarLayout.animateThemedValues(theme);
                    context.rightActionBarLayout.animateThemedValues(theme);
                }
            } else if (i == NotificationCenter.notificationsCountUpdated && context.sideMenu != null) {
                i4 = 0;
                Integer accountNum = args[0];
                int count = context.sideMenu.getChildCount();
                while (i4 < count) {
                    View child2 = context.sideMenu.getChildAt(i4);
                    if ((child2 instanceof DrawerUserCell) && ((DrawerUserCell) child2).getAccountNumber() == accountNum.intValue()) {
                        child2.invalidate();
                        break;
                    }
                    i4++;
                }
            }
        } else if (!mainFragmentsStack.isEmpty()) {
            ArticleViewer.getInstance().setParentActivity(context, (BaseFragment) mainFragmentsStack.get(mainFragmentsStack.size() - 1));
            ArticleViewer.getInstance().open((TL_webPage) args[0], (String) args[1]);
        } else {
            return;
        }
        i = i2;
    }

    private String getStringForLanguageAlert(HashMap<String, String> map, String key, int intKey) {
        String value = (String) map.get(key);
        if (value == null) {
            return LocaleController.getString(key, intKey);
        }
        return value;
    }

    private void checkFreeDiscSpace() {
        if (VERSION.SDK_INT < 26) {
            Utilities.globalQueue.postRunnable(new Runnable() {
                public void run() {
                    if (UserConfig.getInstance(LaunchActivity.this.currentAccount).isClientActivated()) {
                        try {
                            SharedPreferences preferences = MessagesController.getGlobalMainSettings();
                            if (Math.abs(preferences.getLong("last_space_check", 0) - System.currentTimeMillis()) >= 259200000) {
                                File path = FileLoader.getDirectory(4);
                                if (path != null) {
                                    long freeSpace;
                                    StatFs statFs = new StatFs(path.getAbsolutePath());
                                    if (VERSION.SDK_INT < 18) {
                                        freeSpace = (long) Math.abs(statFs.getAvailableBlocks() * statFs.getBlockSize());
                                    } else {
                                        freeSpace = statFs.getAvailableBlocksLong() * statFs.getBlockSizeLong();
                                    }
                                    preferences.edit().putLong("last_space_check", System.currentTimeMillis()).commit();
                                    if (freeSpace < 104857600) {
                                        AndroidUtilities.runOnUIThread(new Runnable() {
                                            public void run() {
                                                try {
                                                    AlertsCreator.createFreeSpaceDialog(LaunchActivity.this).show();
                                                } catch (Throwable th) {
                                                }
                                            }
                                        });
                                    }
                                }
                            }
                        } catch (Throwable th) {
                        }
                    }
                }
            }, AdaptiveTrackSelection.DEFAULT_MIN_TIME_BETWEEN_BUFFER_REEVALUTATION_MS);
        }
    }

    private void showLanguageAlertInternal(LocaleInfo systemInfo, LocaleInfo englishInfo, String systemLang) {
        Throwable e;
        LocaleInfo localeInfo;
        String str;
        LocaleInfo localeInfo2;
        try {
            this.loadingLocaleDialog = false;
            localeInfo2 = systemInfo;
            try {
                boolean firstSystem;
                Builder builder;
                LinearLayout linearLayout;
                int i;
                final LanguageCell[] cells;
                final LocaleInfo[] selectedLanguage;
                LocaleInfo[] locales;
                String englishName;
                int a;
                LanguageCell cell;
                if (!localeInfo2.builtIn) {
                    if (!LocaleController.getInstance().isCurrentLocalLocale()) {
                        firstSystem = false;
                        builder = new Builder(r1);
                        builder.setTitle(getStringForLanguageAlert(r1.systemLocaleStrings, "ChooseYourLanguage", R.string.ChooseYourLanguage));
                        builder.setSubtitle(getStringForLanguageAlert(r1.englishLocaleStrings, "ChooseYourLanguage", R.string.ChooseYourLanguage));
                        linearLayout = new LinearLayout(r1);
                        linearLayout.setOrientation(1);
                        i = 2;
                        cells = new LanguageCell[2];
                        selectedLanguage = new LocaleInfo[1];
                        locales = new LocaleInfo[2];
                        englishName = getStringForLanguageAlert(r1.systemLocaleStrings, "English", R.string.English);
                        locales[0] = firstSystem ? localeInfo2 : englishInfo;
                        locales[1] = firstSystem ? englishInfo : localeInfo2;
                        selectedLanguage[0] = firstSystem ? localeInfo2 : englishInfo;
                        a = 0;
                        while (a < i) {
                            cells[a] = new LanguageCell(r1, true);
                            try {
                                cells[a].setLanguage(locales[a], locales[a] != englishInfo ? englishName : null, true);
                                cells[a].setTag(Integer.valueOf(a));
                                cells[a].setBackgroundDrawable(Theme.createSelectorDrawable(Theme.getColor(Theme.key_dialogButtonSelector), 2));
                                cells[a].setLanguageSelected(a != 0);
                                linearLayout.addView(cells[a], LayoutHelper.createLinear(-1, 48));
                                cells[a].setOnClickListener(new OnClickListener() {
                                    public void onClick(View v) {
                                        Integer tag = (Integer) v.getTag();
                                        selectedLanguage[0] = ((LanguageCell) v).getCurrentLocale();
                                        int a = 0;
                                        while (a < cells.length) {
                                            cells[a].setLanguageSelected(a == tag.intValue());
                                            a++;
                                        }
                                    }
                                });
                                a++;
                                i = 2;
                            } catch (Exception e2) {
                                e = e2;
                            }
                        }
                        localeInfo = englishInfo;
                        cell = new LanguageCell(r1, true);
                        cell.setValue(getStringForLanguageAlert(r1.systemLocaleStrings, "ChooseYourLanguageOther", R.string.ChooseYourLanguageOther), getStringForLanguageAlert(r1.englishLocaleStrings, "ChooseYourLanguageOther", R.string.ChooseYourLanguageOther));
                        cell.setOnClickListener(new OnClickListener() {
                            public void onClick(View v) {
                                LaunchActivity.this.localeDialog = null;
                                LaunchActivity.this.drawerLayoutContainer.closeDrawer(true);
                                LaunchActivity.this.presentFragment(new LanguageSelectActivity());
                                if (LaunchActivity.this.visibleDialog != null) {
                                    LaunchActivity.this.visibleDialog.dismiss();
                                    LaunchActivity.this.visibleDialog = null;
                                }
                            }
                        });
                        linearLayout.addView(cell, LayoutHelper.createLinear(-1, 48));
                        builder.setView(linearLayout);
                        builder.setNegativeButton(LocaleController.getString("OK", R.string.OK), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                LocaleController.getInstance().applyLanguage(selectedLanguage[0], true, false, LaunchActivity.this.currentAccount);
                                LaunchActivity.this.rebuildAllFragments(true);
                            }
                        });
                        r1.localeDialog = showAlertDialog(builder);
                        MessagesController.getGlobalMainSettings().edit().putString("language_showed2", systemLang).commit();
                    }
                }
                firstSystem = true;
                builder = new Builder(r1);
                builder.setTitle(getStringForLanguageAlert(r1.systemLocaleStrings, "ChooseYourLanguage", R.string.ChooseYourLanguage));
                builder.setSubtitle(getStringForLanguageAlert(r1.englishLocaleStrings, "ChooseYourLanguage", R.string.ChooseYourLanguage));
                linearLayout = new LinearLayout(r1);
                linearLayout.setOrientation(1);
                i = 2;
                cells = new LanguageCell[2];
                selectedLanguage = new LocaleInfo[1];
                locales = new LocaleInfo[2];
                englishName = getStringForLanguageAlert(r1.systemLocaleStrings, "English", R.string.English);
                if (firstSystem) {
                }
                locales[0] = firstSystem ? localeInfo2 : englishInfo;
                if (firstSystem) {
                }
                locales[1] = firstSystem ? englishInfo : localeInfo2;
                if (firstSystem) {
                }
                selectedLanguage[0] = firstSystem ? localeInfo2 : englishInfo;
                a = 0;
                while (a < i) {
                    cells[a] = new LanguageCell(r1, true);
                    if (locales[a] != englishInfo) {
                    }
                    cells[a].setLanguage(locales[a], locales[a] != englishInfo ? englishName : null, true);
                    cells[a].setTag(Integer.valueOf(a));
                    cells[a].setBackgroundDrawable(Theme.createSelectorDrawable(Theme.getColor(Theme.key_dialogButtonSelector), 2));
                    if (a != 0) {
                    }
                    cells[a].setLanguageSelected(a != 0);
                    linearLayout.addView(cells[a], LayoutHelper.createLinear(-1, 48));
                    cells[a].setOnClickListener(/* anonymous class already generated */);
                    a++;
                    i = 2;
                }
                localeInfo = englishInfo;
                cell = new LanguageCell(r1, true);
                cell.setValue(getStringForLanguageAlert(r1.systemLocaleStrings, "ChooseYourLanguageOther", R.string.ChooseYourLanguageOther), getStringForLanguageAlert(r1.englishLocaleStrings, "ChooseYourLanguageOther", R.string.ChooseYourLanguageOther));
                cell.setOnClickListener(/* anonymous class already generated */);
                linearLayout.addView(cell, LayoutHelper.createLinear(-1, 48));
                builder.setView(linearLayout);
                builder.setNegativeButton(LocaleController.getString("OK", R.string.OK), /* anonymous class already generated */);
                r1.localeDialog = showAlertDialog(builder);
            } catch (Exception e3) {
                e = e3;
                localeInfo = englishInfo;
                str = systemLang;
                FileLog.e(e);
            }
            try {
                MessagesController.getGlobalMainSettings().edit().putString("language_showed2", systemLang).commit();
            } catch (Exception e4) {
                e = e4;
                FileLog.e(e);
            }
        } catch (Exception e5) {
            e = e5;
            localeInfo2 = systemInfo;
            localeInfo = englishInfo;
            str = systemLang;
            FileLog.e(e);
        }
    }

    private void showLanguageAlert(boolean force) {
        try {
            if (!this.loadingLocaleDialog) {
                String showedLang = MessagesController.getGlobalMainSettings().getString("language_showed2", TtmlNode.ANONYMOUS_REGION_ID);
                final String systemLang = LocaleController.getSystemLocaleStringIso639().toLowerCase();
                if (force || !showedLang.equals(systemLang)) {
                    int a;
                    LocaleInfo info;
                    StringBuilder stringBuilder;
                    TL_langpack_getStrings req;
                    final LocaleInfo[] infos = new LocaleInfo[2];
                    String arg = systemLang.contains("-") ? systemLang.split("-")[0] : systemLang;
                    String alias;
                    if ("in".equals(arg)) {
                        alias = TtmlNode.ATTR_ID;
                    } else if ("iw".equals(arg)) {
                        alias = "he";
                    } else if ("jw".equals(arg)) {
                        alias = "jv";
                    } else {
                        alias = null;
                        for (a = 0; a < LocaleController.getInstance().languages.size(); a++) {
                            info = (LocaleInfo) LocaleController.getInstance().languages.get(a);
                            if (info.shortName.equals("en")) {
                                infos[0] = info;
                            }
                            if (info.shortName.replace("_", "-").equals(systemLang) || info.shortName.equals(arg) || (alias != null && info.shortName.equals(alias))) {
                                infos[1] = info;
                            }
                            if (infos[0] == null && infos[1] != null) {
                                break;
                            }
                        }
                        if (!(infos[0] == null || infos[1] == null)) {
                            if (infos[0] == infos[1]) {
                                if (BuildVars.LOGS_ENABLED) {
                                    stringBuilder = new StringBuilder();
                                    stringBuilder.append("show lang alert for ");
                                    stringBuilder.append(infos[0].getKey());
                                    stringBuilder.append(" and ");
                                    stringBuilder.append(infos[1].getKey());
                                    FileLog.d(stringBuilder.toString());
                                }
                                this.systemLocaleStrings = null;
                                this.englishLocaleStrings = null;
                                this.loadingLocaleDialog = true;
                                req = new TL_langpack_getStrings();
                                req.lang_code = infos[1].shortName.replace("_", "-");
                                req.keys.add("English");
                                req.keys.add("ChooseYourLanguage");
                                req.keys.add("ChooseYourLanguageOther");
                                req.keys.add("ChangeLanguageLater");
                                ConnectionsManager.getInstance(this.currentAccount).sendRequest(req, new RequestDelegate() {
                                    public void run(TLObject response, TL_error error) {
                                        final HashMap<String, String> keys = new HashMap();
                                        if (response != null) {
                                            Vector vector = (Vector) response;
                                            for (int a = 0; a < vector.objects.size(); a++) {
                                                LangPackString string = (LangPackString) vector.objects.get(a);
                                                keys.put(string.key, string.value);
                                            }
                                        }
                                        AndroidUtilities.runOnUIThread(new Runnable() {
                                            public void run() {
                                                LaunchActivity.this.systemLocaleStrings = keys;
                                                if (LaunchActivity.this.englishLocaleStrings != null && LaunchActivity.this.systemLocaleStrings != null) {
                                                    LaunchActivity.this.showLanguageAlertInternal(infos[1], infos[0], systemLang);
                                                }
                                            }
                                        });
                                    }
                                }, 8);
                                req = new TL_langpack_getStrings();
                                req.lang_code = infos[0].shortName.replace("_", "-");
                                req.keys.add("English");
                                req.keys.add("ChooseYourLanguage");
                                req.keys.add("ChooseYourLanguageOther");
                                req.keys.add("ChangeLanguageLater");
                                ConnectionsManager.getInstance(this.currentAccount).sendRequest(req, new RequestDelegate() {
                                    public void run(TLObject response, TL_error error) {
                                        final HashMap<String, String> keys = new HashMap();
                                        if (response != null) {
                                            Vector vector = (Vector) response;
                                            for (int a = 0; a < vector.objects.size(); a++) {
                                                LangPackString string = (LangPackString) vector.objects.get(a);
                                                keys.put(string.key, string.value);
                                            }
                                        }
                                        AndroidUtilities.runOnUIThread(new Runnable() {
                                            public void run() {
                                                LaunchActivity.this.englishLocaleStrings = keys;
                                                if (LaunchActivity.this.englishLocaleStrings != null && LaunchActivity.this.systemLocaleStrings != null) {
                                                    LaunchActivity.this.showLanguageAlertInternal(infos[1], infos[0], systemLang);
                                                }
                                            }
                                        });
                                    }
                                }, 8);
                                return;
                            }
                        }
                        return;
                    }
                    for (a = 0; a < LocaleController.getInstance().languages.size(); a++) {
                        info = (LocaleInfo) LocaleController.getInstance().languages.get(a);
                        if (info.shortName.equals("en")) {
                            infos[0] = info;
                        }
                        infos[1] = info;
                        if (infos[0] == null) {
                        }
                    }
                    if (infos[0] == infos[1]) {
                        if (BuildVars.LOGS_ENABLED) {
                            stringBuilder = new StringBuilder();
                            stringBuilder.append("show lang alert for ");
                            stringBuilder.append(infos[0].getKey());
                            stringBuilder.append(" and ");
                            stringBuilder.append(infos[1].getKey());
                            FileLog.d(stringBuilder.toString());
                        }
                        this.systemLocaleStrings = null;
                        this.englishLocaleStrings = null;
                        this.loadingLocaleDialog = true;
                        req = new TL_langpack_getStrings();
                        req.lang_code = infos[1].shortName.replace("_", "-");
                        req.keys.add("English");
                        req.keys.add("ChooseYourLanguage");
                        req.keys.add("ChooseYourLanguageOther");
                        req.keys.add("ChangeLanguageLater");
                        ConnectionsManager.getInstance(this.currentAccount).sendRequest(req, /* anonymous class already generated */, 8);
                        req = new TL_langpack_getStrings();
                        req.lang_code = infos[0].shortName.replace("_", "-");
                        req.keys.add("English");
                        req.keys.add("ChooseYourLanguage");
                        req.keys.add("ChooseYourLanguageOther");
                        req.keys.add("ChangeLanguageLater");
                        ConnectionsManager.getInstance(this.currentAccount).sendRequest(req, /* anonymous class already generated */, 8);
                        return;
                    }
                    return;
                }
                if (BuildVars.LOGS_ENABLED) {
                    StringBuilder stringBuilder2 = new StringBuilder();
                    stringBuilder2.append("alert already showed for ");
                    stringBuilder2.append(showedLang);
                    FileLog.d(stringBuilder2.toString());
                }
            }
        } catch (Throwable e) {
            FileLog.e(e);
        }
    }

    private void onPasscodePause() {
        if (this.lockRunnable != null) {
            AndroidUtilities.cancelRunOnUIThread(this.lockRunnable);
            this.lockRunnable = null;
        }
        if (SharedConfig.passcodeHash.length() != 0) {
            SharedConfig.lastPauseTime = ConnectionsManager.getInstance(this.currentAccount).getCurrentTime();
            this.lockRunnable = new Runnable() {
                public void run() {
                    if (LaunchActivity.this.lockRunnable == this) {
                        if (AndroidUtilities.needShowPasscode(true)) {
                            if (BuildVars.LOGS_ENABLED) {
                                FileLog.d("lock app");
                            }
                            LaunchActivity.this.showPasscodeActivity();
                        } else if (BuildVars.LOGS_ENABLED) {
                            FileLog.d("didn't pass lock check");
                        }
                        LaunchActivity.this.lockRunnable = null;
                    }
                }
            };
            if (SharedConfig.appLocked) {
                AndroidUtilities.runOnUIThread(this.lockRunnable, 1000);
            } else if (SharedConfig.autoLockIn != 0) {
                AndroidUtilities.runOnUIThread(this.lockRunnable, (((long) SharedConfig.autoLockIn) * 1000) + 1000);
            }
        } else {
            SharedConfig.lastPauseTime = 0;
        }
        SharedConfig.saveConfig();
    }

    private void onPasscodeResume() {
        if (this.lockRunnable != null) {
            AndroidUtilities.cancelRunOnUIThread(this.lockRunnable);
            this.lockRunnable = null;
        }
        if (AndroidUtilities.needShowPasscode(true)) {
            showPasscodeActivity();
        }
        if (SharedConfig.lastPauseTime != 0) {
            SharedConfig.lastPauseTime = 0;
            SharedConfig.saveConfig();
        }
    }

    private void updateCurrentConnectionState(int account) {
        if (this.actionBarLayout != null) {
            String title = null;
            String subtitle = null;
            Runnable action = null;
            if (this.currentConnectionState == 2) {
                title = LocaleController.getString("WaitingForNetwork", R.string.WaitingForNetwork);
            } else if (this.currentConnectionState == 1) {
                title = LocaleController.getString("Connecting", R.string.Connecting);
                action = new Runnable() {
                    public void run() {
                        if (AndroidUtilities.isTablet()) {
                            if (!LaunchActivity.layerFragmentsStack.isEmpty() && (LaunchActivity.layerFragmentsStack.get(LaunchActivity.layerFragmentsStack.size() - 1) instanceof ProxySettingsActivity)) {
                                return;
                            }
                        } else if (!LaunchActivity.mainFragmentsStack.isEmpty() && (LaunchActivity.mainFragmentsStack.get(LaunchActivity.mainFragmentsStack.size() - 1) instanceof ProxySettingsActivity)) {
                            return;
                        }
                        LaunchActivity.this.presentFragment(new ProxySettingsActivity());
                    }
                };
            } else if (this.currentConnectionState == 5) {
                title = LocaleController.getString("Updating", R.string.Updating);
            } else if (this.currentConnectionState == 4) {
                title = LocaleController.getString("ConnectingToProxy", R.string.ConnectingToProxy);
                subtitle = LocaleController.getString("ConnectingToProxyTapToDisable", R.string.ConnectingToProxyTapToDisable);
                action = new Runnable() {
                    public void run() {
                        if (LaunchActivity.this.actionBarLayout != null) {
                            if (!LaunchActivity.this.actionBarLayout.fragmentsStack.isEmpty()) {
                                SharedPreferences preferences = MessagesController.getGlobalMainSettings();
                                BaseFragment fragment = (BaseFragment) LaunchActivity.this.actionBarLayout.fragmentsStack.get(LaunchActivity.this.actionBarLayout.fragmentsStack.size() - 1);
                                Builder builder = new Builder(LaunchActivity.this);
                                builder.setTitle(LocaleController.getString("Proxy", R.string.Proxy));
                                builder.setMessage(LocaleController.formatString("ConnectingToProxyDisableAlert", R.string.ConnectingToProxyDisableAlert, preferences.getString("proxy_ip", TtmlNode.ANONYMOUS_REGION_ID)));
                                builder.setPositiveButton(LocaleController.getString("ConnectingToProxyDisable", R.string.ConnectingToProxyDisable), new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        Editor editor = MessagesController.getGlobalMainSettings().edit();
                                        editor.putBoolean("proxy_enabled", false);
                                        editor.commit();
                                        for (int a = 0; a < 3; a++) {
                                            ConnectionsManager.native_setProxySettings(a, TtmlNode.ANONYMOUS_REGION_ID, 0, TtmlNode.ANONYMOUS_REGION_ID, TtmlNode.ANONYMOUS_REGION_ID);
                                        }
                                        NotificationCenter.getGlobalInstance().postNotificationName(NotificationCenter.proxySettingsChanged, new Object[0]);
                                    }
                                });
                                builder.setNegativeButton(LocaleController.getString("Cancel", R.string.Cancel), null);
                                fragment.showDialog(builder.create());
                            }
                        }
                    }
                };
            }
            this.actionBarLayout.setTitleOverlayText(title, subtitle, action);
        }
    }

    protected void onSaveInstanceState(Bundle outState) {
        try {
            super.onSaveInstanceState(outState);
            BaseFragment lastFragment = null;
            if (AndroidUtilities.isTablet()) {
                if (!this.layersActionBarLayout.fragmentsStack.isEmpty()) {
                    lastFragment = (BaseFragment) this.layersActionBarLayout.fragmentsStack.get(this.layersActionBarLayout.fragmentsStack.size() - 1);
                } else if (!this.rightActionBarLayout.fragmentsStack.isEmpty()) {
                    lastFragment = (BaseFragment) this.rightActionBarLayout.fragmentsStack.get(this.rightActionBarLayout.fragmentsStack.size() - 1);
                } else if (!this.actionBarLayout.fragmentsStack.isEmpty()) {
                    lastFragment = (BaseFragment) this.actionBarLayout.fragmentsStack.get(this.actionBarLayout.fragmentsStack.size() - 1);
                }
            } else if (!this.actionBarLayout.fragmentsStack.isEmpty()) {
                lastFragment = (BaseFragment) this.actionBarLayout.fragmentsStack.get(this.actionBarLayout.fragmentsStack.size() - 1);
            }
            if (lastFragment != null) {
                Bundle args = lastFragment.getArguments();
                if ((lastFragment instanceof ChatActivity) && args != null) {
                    outState.putBundle("args", args);
                    outState.putString("fragment", "chat");
                } else if (lastFragment instanceof SettingsActivity) {
                    outState.putString("fragment", "settings");
                } else if ((lastFragment instanceof GroupCreateFinalActivity) && args != null) {
                    outState.putBundle("args", args);
                    outState.putString("fragment", "group");
                } else if (lastFragment instanceof WallpapersActivity) {
                    outState.putString("fragment", "wallpapers");
                } else if ((lastFragment instanceof ProfileActivity) && ((ProfileActivity) lastFragment).isChat() && args != null) {
                    outState.putBundle("args", args);
                    outState.putString("fragment", "chat_profile");
                } else if ((lastFragment instanceof ChannelCreateActivity) && args != null && args.getInt("step") == 0) {
                    outState.putBundle("args", args);
                    outState.putString("fragment", "channel");
                } else if ((lastFragment instanceof ChannelEditActivity) && args != null) {
                    outState.putBundle("args", args);
                    outState.putString("fragment", "edit");
                }
                lastFragment.saveSelfArgs(outState);
            }
        } catch (Throwable e) {
            FileLog.e(e);
        }
    }

    public void onBackPressed() {
        if (this.passcodeView.getVisibility() == 0) {
            finish();
            return;
        }
        if (SecretMediaViewer.hasInstance() && SecretMediaViewer.getInstance().isVisible()) {
            SecretMediaViewer.getInstance().closePhoto(true, false);
        } else if (PhotoViewer.hasInstance() && PhotoViewer.getInstance().isVisible()) {
            PhotoViewer.getInstance().closePhoto(true, false);
        } else if (ArticleViewer.hasInstance() && ArticleViewer.getInstance().isVisible()) {
            ArticleViewer.getInstance().close(true, false);
        } else if (this.drawerLayoutContainer.isDrawerOpened()) {
            this.drawerLayoutContainer.closeDrawer(false);
        } else if (!AndroidUtilities.isTablet()) {
            this.actionBarLayout.onBackPressed();
        } else if (this.layersActionBarLayout.getVisibility() == 0) {
            this.layersActionBarLayout.onBackPressed();
        } else {
            boolean cancel = false;
            if (this.rightActionBarLayout.getVisibility() == 0 && !this.rightActionBarLayout.fragmentsStack.isEmpty()) {
                cancel = 1 ^ ((BaseFragment) this.rightActionBarLayout.fragmentsStack.get(this.rightActionBarLayout.fragmentsStack.size() - 1)).onBackPressed();
            }
            if (!cancel) {
                this.actionBarLayout.onBackPressed();
            }
        }
    }

    public void onLowMemory() {
        super.onLowMemory();
        this.actionBarLayout.onLowMemory();
        if (AndroidUtilities.isTablet()) {
            this.rightActionBarLayout.onLowMemory();
            this.layersActionBarLayout.onLowMemory();
        }
    }

    public void onActionModeStarted(ActionMode mode) {
        super.onActionModeStarted(mode);
        try {
            Menu menu = mode.getMenu();
            if (!(menu == null || this.actionBarLayout.extendActionMode(menu) || !AndroidUtilities.isTablet() || this.rightActionBarLayout.extendActionMode(menu))) {
                this.layersActionBarLayout.extendActionMode(menu);
            }
        } catch (Throwable e) {
            FileLog.e(e);
        }
        if (VERSION.SDK_INT < 23 || mode.getType() != 1) {
            this.actionBarLayout.onActionModeStarted(mode);
            if (AndroidUtilities.isTablet()) {
                this.rightActionBarLayout.onActionModeStarted(mode);
                this.layersActionBarLayout.onActionModeStarted(mode);
            }
        }
    }

    public void onActionModeFinished(ActionMode mode) {
        super.onActionModeFinished(mode);
        if (VERSION.SDK_INT < 23 || mode.getType() != 1) {
            this.actionBarLayout.onActionModeFinished(mode);
            if (AndroidUtilities.isTablet()) {
                this.rightActionBarLayout.onActionModeFinished(mode);
                this.layersActionBarLayout.onActionModeFinished(mode);
            }
        }
    }

    public boolean onPreIme() {
        if (SecretMediaViewer.hasInstance() && SecretMediaViewer.getInstance().isVisible()) {
            SecretMediaViewer.getInstance().closePhoto(true, false);
            return true;
        } else if (PhotoViewer.hasInstance() && PhotoViewer.getInstance().isVisible()) {
            PhotoViewer.getInstance().closePhoto(true, false);
            return true;
        } else if (!ArticleViewer.hasInstance() || !ArticleViewer.getInstance().isVisible()) {
            return false;
        } else {
            ArticleViewer.getInstance().close(true, false);
            return true;
        }
    }

    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (keyCode == 82 && !SharedConfig.isWaitingForPasscodeEnter) {
            if (PhotoViewer.hasInstance() && PhotoViewer.getInstance().isVisible()) {
                return super.onKeyUp(keyCode, event);
            }
            if (ArticleViewer.hasInstance() && ArticleViewer.getInstance().isVisible()) {
                return super.onKeyUp(keyCode, event);
            }
            if (AndroidUtilities.isTablet()) {
                if (this.layersActionBarLayout.getVisibility() == 0 && !this.layersActionBarLayout.fragmentsStack.isEmpty()) {
                    this.layersActionBarLayout.onKeyUp(keyCode, event);
                } else if (this.rightActionBarLayout.getVisibility() != 0 || this.rightActionBarLayout.fragmentsStack.isEmpty()) {
                    this.actionBarLayout.onKeyUp(keyCode, event);
                } else {
                    this.rightActionBarLayout.onKeyUp(keyCode, event);
                }
            } else if (this.actionBarLayout.fragmentsStack.size() != 1) {
                this.actionBarLayout.onKeyUp(keyCode, event);
            } else if (this.drawerLayoutContainer.isDrawerOpened()) {
                this.drawerLayoutContainer.closeDrawer(false);
            } else {
                if (getCurrentFocus() != null) {
                    AndroidUtilities.hideKeyboard(getCurrentFocus());
                }
                this.drawerLayoutContainer.openDrawer(false);
            }
        }
        return super.onKeyUp(keyCode, event);
    }

    public boolean needPresentFragment(BaseFragment fragment, boolean removeLast, boolean forceWithoutAnimation, ActionBarLayout layout) {
        boolean z = true;
        if (ArticleViewer.hasInstance() && ArticleViewer.getInstance().isVisible()) {
            ArticleViewer.getInstance().close(false, true);
        }
        boolean result;
        if (AndroidUtilities.isTablet()) {
            DrawerLayoutContainer drawerLayoutContainer = this.drawerLayoutContainer;
            boolean z2 = ((fragment instanceof LoginActivity) || (fragment instanceof CountrySelectActivity) || this.layersActionBarLayout.getVisibility() == 0) ? false : true;
            drawerLayoutContainer.setAllowOpenDrawer(z2, true);
            if ((fragment instanceof DialogsActivity) && ((DialogsActivity) fragment).isMainDialogList() && layout != this.actionBarLayout) {
                this.actionBarLayout.removeAllFragments();
                this.actionBarLayout.presentFragment(fragment, removeLast, forceWithoutAnimation, false);
                this.layersActionBarLayout.removeAllFragments();
                this.layersActionBarLayout.setVisibility(8);
                this.drawerLayoutContainer.setAllowOpenDrawer(true, false);
                if (!this.tabletFullSize) {
                    this.shadowTabletSide.setVisibility(0);
                    if (this.rightActionBarLayout.fragmentsStack.isEmpty()) {
                        this.backgroundTablet.setVisibility(0);
                    }
                }
                return false;
            } else if (fragment instanceof ChatActivity) {
                if ((!this.tabletFullSize && layout == this.rightActionBarLayout) || (this.tabletFullSize && layout == this.actionBarLayout)) {
                    int a;
                    if (this.tabletFullSize && layout == this.actionBarLayout) {
                        if (this.actionBarLayout.fragmentsStack.size() == 1) {
                            result = false;
                            if (!this.layersActionBarLayout.fragmentsStack.isEmpty()) {
                                for (a = 0; a < this.layersActionBarLayout.fragmentsStack.size() - 1; a = (a - 1) + 1) {
                                    this.layersActionBarLayout.removeFragmentFromStack((BaseFragment) this.layersActionBarLayout.fragmentsStack.get(0));
                                }
                                this.layersActionBarLayout.closeLastFragment(forceWithoutAnimation ^ 1);
                            }
                            if (!result) {
                                this.actionBarLayout.presentFragment(fragment, false, forceWithoutAnimation, false);
                            }
                            return result;
                        }
                    }
                    result = true;
                    if (this.layersActionBarLayout.fragmentsStack.isEmpty()) {
                        for (a = 0; a < this.layersActionBarLayout.fragmentsStack.size() - 1; a = (a - 1) + 1) {
                            this.layersActionBarLayout.removeFragmentFromStack((BaseFragment) this.layersActionBarLayout.fragmentsStack.get(0));
                        }
                        this.layersActionBarLayout.closeLastFragment(forceWithoutAnimation ^ 1);
                    }
                    if (result) {
                        this.actionBarLayout.presentFragment(fragment, false, forceWithoutAnimation, false);
                    }
                    return result;
                } else if (!this.tabletFullSize && layout != this.rightActionBarLayout) {
                    this.rightActionBarLayout.setVisibility(0);
                    this.backgroundTablet.setVisibility(8);
                    this.rightActionBarLayout.removeAllFragments();
                    this.rightActionBarLayout.presentFragment(fragment, removeLast, true, false);
                    if (!this.layersActionBarLayout.fragmentsStack.isEmpty()) {
                        for (a = 0; a < this.layersActionBarLayout.fragmentsStack.size() - 1; a = (a - 1) + 1) {
                            this.layersActionBarLayout.removeFragmentFromStack((BaseFragment) this.layersActionBarLayout.fragmentsStack.get(0));
                        }
                        this.layersActionBarLayout.closeLastFragment(forceWithoutAnimation ^ 1);
                    }
                    return false;
                } else if (!this.tabletFullSize || layout == this.actionBarLayout) {
                    if (!this.layersActionBarLayout.fragmentsStack.isEmpty()) {
                        for (a = 0; a < this.layersActionBarLayout.fragmentsStack.size() - 1; a = (a - 1) + 1) {
                            this.layersActionBarLayout.removeFragmentFromStack((BaseFragment) this.layersActionBarLayout.fragmentsStack.get(0));
                        }
                        this.layersActionBarLayout.closeLastFragment(forceWithoutAnimation ^ 1);
                    }
                    ActionBarLayout actionBarLayout = this.actionBarLayout;
                    if (this.actionBarLayout.fragmentsStack.size() <= 1) {
                        z = false;
                    }
                    actionBarLayout.presentFragment(fragment, z, forceWithoutAnimation, false);
                    return false;
                } else {
                    this.actionBarLayout.presentFragment(fragment, this.actionBarLayout.fragmentsStack.size() > 1, forceWithoutAnimation, false);
                    if (!this.layersActionBarLayout.fragmentsStack.isEmpty()) {
                        for (a = 0; a < this.layersActionBarLayout.fragmentsStack.size() - 1; a = (a - 1) + 1) {
                            this.layersActionBarLayout.removeFragmentFromStack((BaseFragment) this.layersActionBarLayout.fragmentsStack.get(0));
                        }
                        this.layersActionBarLayout.closeLastFragment(forceWithoutAnimation ^ 1);
                    }
                    return false;
                }
            } else if (layout == this.layersActionBarLayout) {
                return true;
            } else {
                this.layersActionBarLayout.setVisibility(0);
                this.drawerLayoutContainer.setAllowOpenDrawer(false, true);
                if (fragment instanceof LoginActivity) {
                    this.backgroundTablet.setVisibility(0);
                    this.shadowTabletSide.setVisibility(8);
                    this.shadowTablet.setBackgroundColor(0);
                } else {
                    this.shadowTablet.setBackgroundColor(Theme.ACTION_BAR_PHOTO_VIEWER_COLOR);
                }
                this.layersActionBarLayout.presentFragment(fragment, removeLast, forceWithoutAnimation, false);
                return false;
            }
        }
        result = true;
        if (fragment instanceof LoginActivity) {
            if (mainFragmentsStack.size() == 0) {
                result = false;
            }
        } else if ((fragment instanceof CountrySelectActivity) && mainFragmentsStack.size() == 1) {
            result = false;
        }
        this.drawerLayoutContainer.setAllowOpenDrawer(result, false);
        return true;
    }

    public boolean needAddFragmentToStack(BaseFragment fragment, ActionBarLayout layout) {
        if (AndroidUtilities.isTablet()) {
            DrawerLayoutContainer drawerLayoutContainer = this.drawerLayoutContainer;
            boolean z = ((fragment instanceof LoginActivity) || (fragment instanceof CountrySelectActivity) || this.layersActionBarLayout.getVisibility() == 0) ? false : true;
            drawerLayoutContainer.setAllowOpenDrawer(z, true);
            if (fragment instanceof DialogsActivity) {
                if (((DialogsActivity) fragment).isMainDialogList() && layout != this.actionBarLayout) {
                    this.actionBarLayout.removeAllFragments();
                    this.actionBarLayout.addFragmentToStack(fragment);
                    this.layersActionBarLayout.removeAllFragments();
                    this.layersActionBarLayout.setVisibility(8);
                    this.drawerLayoutContainer.setAllowOpenDrawer(true, false);
                    if (!this.tabletFullSize) {
                        this.shadowTabletSide.setVisibility(0);
                        if (this.rightActionBarLayout.fragmentsStack.isEmpty()) {
                            this.backgroundTablet.setVisibility(0);
                        }
                    }
                    return false;
                }
            } else if (fragment instanceof ChatActivity) {
                int a;
                if (!this.tabletFullSize && layout != this.rightActionBarLayout) {
                    this.rightActionBarLayout.setVisibility(0);
                    this.backgroundTablet.setVisibility(8);
                    this.rightActionBarLayout.removeAllFragments();
                    this.rightActionBarLayout.addFragmentToStack(fragment);
                    if (!this.layersActionBarLayout.fragmentsStack.isEmpty()) {
                        for (a = 0; a < this.layersActionBarLayout.fragmentsStack.size() - 1; a = (a - 1) + 1) {
                            this.layersActionBarLayout.removeFragmentFromStack((BaseFragment) this.layersActionBarLayout.fragmentsStack.get(0));
                        }
                        this.layersActionBarLayout.closeLastFragment(true);
                    }
                    return false;
                } else if (this.tabletFullSize && layout != this.actionBarLayout) {
                    this.actionBarLayout.addFragmentToStack(fragment);
                    if (!this.layersActionBarLayout.fragmentsStack.isEmpty()) {
                        for (a = 0; a < this.layersActionBarLayout.fragmentsStack.size() - 1; a = (a - 1) + 1) {
                            this.layersActionBarLayout.removeFragmentFromStack((BaseFragment) this.layersActionBarLayout.fragmentsStack.get(0));
                        }
                        this.layersActionBarLayout.closeLastFragment(true);
                    }
                    return false;
                }
            } else if (layout != this.layersActionBarLayout) {
                this.layersActionBarLayout.setVisibility(0);
                this.drawerLayoutContainer.setAllowOpenDrawer(false, true);
                if (fragment instanceof LoginActivity) {
                    this.backgroundTablet.setVisibility(0);
                    this.shadowTabletSide.setVisibility(8);
                    this.shadowTablet.setBackgroundColor(0);
                } else {
                    this.shadowTablet.setBackgroundColor(Theme.ACTION_BAR_PHOTO_VIEWER_COLOR);
                }
                this.layersActionBarLayout.addFragmentToStack(fragment);
                return false;
            }
            return true;
        }
        boolean allow = true;
        if (fragment instanceof LoginActivity) {
            if (mainFragmentsStack.size() == 0) {
                allow = false;
            }
        } else if ((fragment instanceof CountrySelectActivity) && mainFragmentsStack.size() == 1) {
            allow = false;
        }
        this.drawerLayoutContainer.setAllowOpenDrawer(allow, false);
        return true;
    }

    public boolean needCloseLastFragment(ActionBarLayout layout) {
        if (AndroidUtilities.isTablet()) {
            if (layout == this.actionBarLayout && layout.fragmentsStack.size() <= 1) {
                onFinish();
                finish();
                return false;
            } else if (layout == this.rightActionBarLayout) {
                if (!this.tabletFullSize) {
                    this.backgroundTablet.setVisibility(0);
                }
            } else if (layout == this.layersActionBarLayout && this.actionBarLayout.fragmentsStack.isEmpty() && this.layersActionBarLayout.fragmentsStack.size() == 1) {
                onFinish();
                finish();
                return false;
            }
        } else if (layout.fragmentsStack.size() <= 1) {
            onFinish();
            finish();
            return false;
        } else if (layout.fragmentsStack.size() >= 2 && !(layout.fragmentsStack.get(0) instanceof LoginActivity)) {
            this.drawerLayoutContainer.setAllowOpenDrawer(true, false);
        }
        return true;
    }

    public void rebuildAllFragments(boolean last) {
        if (this.layersActionBarLayout != null) {
            this.layersActionBarLayout.rebuildAllFragmentViews(last, last);
        } else {
            this.actionBarLayout.rebuildAllFragmentViews(last, last);
        }
    }

    public void onRebuildAllFragments(ActionBarLayout layout, boolean last) {
        if (AndroidUtilities.isTablet() && layout == this.layersActionBarLayout) {
            this.rightActionBarLayout.rebuildAllFragmentViews(last, last);
            this.actionBarLayout.rebuildAllFragmentViews(last, last);
        }
        this.drawerLayoutAdapter.notifyDataSetChanged();
    }
}
