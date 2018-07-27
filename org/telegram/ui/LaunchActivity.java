package org.telegram.ui;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ActivityManager.TaskDescription;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageInfo;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.StatFs;
import android.view.ActionMode;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
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
import org.telegram.messenger.ImageLoader;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.LocaleController.LocaleInfo;
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
import org.telegram.messenger.camera.CameraController;
import org.telegram.messenger.exoplayer2.trackselection.AdaptiveTrackSelection;
import org.telegram.messenger.support.widget.helper.ItemTouchHelper.Callback;
import org.telegram.tgnet.ConnectionsManager;
import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC.Chat;
import org.telegram.tgnet.TLRPC.ChatInvite;
import org.telegram.tgnet.TLRPC.LangPackString;
import org.telegram.tgnet.TLRPC.MessageMedia;
import org.telegram.tgnet.TLRPC.TL_account_authorizationForm;
import org.telegram.tgnet.TLRPC.TL_account_getAuthorizationForm;
import org.telegram.tgnet.TLRPC.TL_account_getPassword;
import org.telegram.tgnet.TLRPC.TL_contacts_resolveUsername;
import org.telegram.tgnet.TLRPC.TL_contacts_resolvedPeer;
import org.telegram.tgnet.TLRPC.TL_error;
import org.telegram.tgnet.TLRPC.TL_help_appUpdate;
import org.telegram.tgnet.TLRPC.TL_help_deepLinkInfo;
import org.telegram.tgnet.TLRPC.TL_help_getAppUpdate;
import org.telegram.tgnet.TLRPC.TL_help_getDeepLinkInfo;
import org.telegram.tgnet.TLRPC.TL_help_termsOfService;
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
import org.telegram.tgnet.TLRPC.account_Password;
import org.telegram.ui.ActionBar.ActionBarLayout;
import org.telegram.ui.ActionBar.ActionBarLayout.ActionBarLayoutDelegate;
import org.telegram.ui.ActionBar.AlertDialog;
import org.telegram.ui.ActionBar.AlertDialog.Builder;
import org.telegram.ui.ActionBar.BaseFragment;
import org.telegram.ui.ActionBar.DrawerLayoutContainer;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.ActionBar.Theme.ThemeInfo;
import org.telegram.ui.Adapters.DrawerLayoutAdapter;
import org.telegram.ui.Cells.DrawerUserCell;
import org.telegram.ui.Cells.LanguageCell;
import org.telegram.ui.Components.AlertsCreator;
import org.telegram.ui.Components.AlertsCreator.AccountSelectDelegate;
import org.telegram.ui.Components.BlockingUpdateView;
import org.telegram.ui.Components.EmbedBottomSheet;
import org.telegram.ui.Components.JoinGroupAlert;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.ui.Components.PasscodeView;
import org.telegram.ui.Components.PasscodeView.PasscodeViewDelegate;
import org.telegram.ui.Components.PipRoundVideoView;
import org.telegram.ui.Components.RecyclerListView;
import org.telegram.ui.Components.StickersAlert;
import org.telegram.ui.Components.TermsOfServiceView;
import org.telegram.ui.Components.TermsOfServiceView.TermsOfServiceViewDelegate;
import org.telegram.ui.Components.ThemeEditorView;
import org.telegram.ui.Components.UpdateAppAlertDialog;
import org.telegram.ui.DialogsActivity.DialogsActivityDelegate;
import org.telegram.ui.LocationActivity.LocationActivityDelegate;
import org.telegram.ui.PhonebookSelectActivity.PhonebookSelectActivityDelegate;

public class LaunchActivity extends Activity implements NotificationCenterDelegate, ActionBarLayoutDelegate, DialogsActivityDelegate {
    private static ArrayList<BaseFragment> layerFragmentsStack = new ArrayList();
    private static ArrayList<BaseFragment> mainFragmentsStack = new ArrayList();
    private static ArrayList<BaseFragment> rightFragmentsStack = new ArrayList();
    private ActionBarLayout actionBarLayout;
    private View backgroundTablet;
    private BlockingUpdateView blockingUpdateView;
    private ArrayList<User> contactsToSend;
    private Uri contactsToSendUri;
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
    private AlertDialog proxyErrorDialog;
    private ActionBarLayout rightActionBarLayout;
    private String sendingText;
    private FrameLayout shadowTablet;
    private FrameLayout shadowTabletSide;
    private RecyclerListView sideMenu;
    private HashMap<String, String> systemLocaleStrings;
    private boolean tabletFullSize;
    private TermsOfServiceView termsOfServiceView;
    private String videoPath;
    private AlertDialog visibleDialog;

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    protected void onCreate(android.os.Bundle r41) {
        /*
        r40 = this;
        org.telegram.messenger.ApplicationLoader.postInitApplication();
        r35 = r40.getResources();
        r35 = r35.getConfiguration();
        r0 = r40;
        r1 = r35;
        org.telegram.messenger.AndroidUtilities.checkDisplaySize(r0, r1);
        r35 = org.telegram.messenger.UserConfig.selectedAccount;
        r0 = r35;
        r1 = r40;
        r1.currentAccount = r0;
        r0 = r40;
        r0 = r0.currentAccount;
        r35 = r0;
        r35 = org.telegram.messenger.UserConfig.getInstance(r35);
        r35 = r35.isClientActivated();
        if (r35 != 0) goto L_0x013b;
    L_0x002a:
        r19 = r40.getIntent();
        r21 = 0;
        if (r19 == 0) goto L_0x00aa;
    L_0x0032:
        r35 = r19.getAction();
        if (r35 == 0) goto L_0x00aa;
    L_0x0038:
        r35 = "android.intent.action.SEND";
        r36 = r19.getAction();
        r35 = r35.equals(r36);
        if (r35 != 0) goto L_0x0052;
    L_0x0045:
        r35 = "android.intent.action.SEND_MULTIPLE";
        r36 = r19.getAction();
        r35 = r35.equals(r36);
        if (r35 == 0) goto L_0x0059;
    L_0x0052:
        super.onCreate(r41);
        r40.finish();
    L_0x0058:
        return;
    L_0x0059:
        r35 = "android.intent.action.VIEW";
        r36 = r19.getAction();
        r35 = r35.equals(r36);
        if (r35 == 0) goto L_0x00aa;
    L_0x0066:
        r32 = r19.getData();
        if (r32 == 0) goto L_0x00aa;
    L_0x006c:
        r35 = r32.toString();
        r33 = r35.toLowerCase();
        r35 = "tg:proxy";
        r0 = r33;
        r1 = r35;
        r35 = r0.startsWith(r1);
        if (r35 != 0) goto L_0x00a8;
    L_0x0081:
        r35 = "tg://proxy";
        r0 = r33;
        r1 = r35;
        r35 = r0.startsWith(r1);
        if (r35 != 0) goto L_0x00a8;
    L_0x008e:
        r35 = "tg:socks";
        r0 = r33;
        r1 = r35;
        r35 = r0.startsWith(r1);
        if (r35 != 0) goto L_0x00a8;
    L_0x009b:
        r35 = "tg://socks";
        r0 = r33;
        r1 = r35;
        r35 = r0.startsWith(r1);
        if (r35 == 0) goto L_0x0137;
    L_0x00a8:
        r21 = 1;
    L_0x00aa:
        r26 = org.telegram.messenger.MessagesController.getGlobalMainSettings();
        r35 = "intro_crashed_time";
        r36 = 0;
        r0 = r26;
        r1 = r35;
        r2 = r36;
        r10 = r0.getLong(r1, r2);
        r35 = "fromIntro";
        r36 = 0;
        r0 = r19;
        r1 = r35;
        r2 = r36;
        r17 = r0.getBooleanExtra(r1, r2);
        if (r17 == 0) goto L_0x00e4;
    L_0x00ce:
        r35 = r26.edit();
        r36 = "intro_crashed_time";
        r38 = 0;
        r0 = r35;
        r1 = r36;
        r2 = r38;
        r35 = r0.putLong(r1, r2);
        r35.commit();
    L_0x00e4:
        if (r21 != 0) goto L_0x013b;
    L_0x00e6:
        r36 = java.lang.System.currentTimeMillis();
        r36 = r10 - r36;
        r36 = java.lang.Math.abs(r36);
        r38 = 120000; // 0x1d4c0 float:1.68156E-40 double:5.9288E-319;
        r35 = (r36 > r38 ? 1 : (r36 == r38 ? 0 : -1));
        if (r35 < 0) goto L_0x013b;
    L_0x00f7:
        if (r19 == 0) goto L_0x013b;
    L_0x00f9:
        if (r17 != 0) goto L_0x013b;
    L_0x00fb:
        r35 = org.telegram.messenger.ApplicationLoader.applicationContext;
        r36 = "logininfo2";
        r37 = 0;
        r26 = r35.getSharedPreferences(r36, r37);
        r31 = r26.getAll();
        r35 = r31.isEmpty();
        if (r35 == 0) goto L_0x013b;
    L_0x0110:
        r20 = new android.content.Intent;
        r35 = org.telegram.ui.IntroActivity.class;
        r0 = r20;
        r1 = r40;
        r2 = r35;
        r0.<init>(r1, r2);
        r35 = r19.getData();
        r0 = r20;
        r1 = r35;
        r0.setData(r1);
        r0 = r40;
        r1 = r20;
        r0.startActivity(r1);
        super.onCreate(r41);
        r40.finish();
        goto L_0x0058;
    L_0x0137:
        r21 = 0;
        goto L_0x00aa;
    L_0x013b:
        r35 = 1;
        r0 = r40;
        r1 = r35;
        r0.requestWindowFeature(r1);
        r35 = 2131558412; // 0x7f0d000c float:1.874214E38 double:1.0531297835E-314;
        r0 = r40;
        r1 = r35;
        r0.setTheme(r1);
        r35 = android.os.Build.VERSION.SDK_INT;
        r36 = 21;
        r0 = r35;
        r1 = r36;
        if (r0 < r1) goto L_0x0173;
    L_0x0158:
        r35 = new android.app.ActivityManager$TaskDescription;	 Catch:{ Exception -> 0x094c }
        r36 = 0;
        r37 = 0;
        r38 = "actionBarDefault";
        r38 = org.telegram.ui.ActionBar.Theme.getColor(r38);	 Catch:{ Exception -> 0x094c }
        r39 = -16777216; // 0xffffffffff000000 float:-1.7014118E38 double:NaN;
        r38 = r38 | r39;
        r35.<init>(r36, r37, r38);	 Catch:{ Exception -> 0x094c }
        r0 = r40;
        r1 = r35;
        r0.setTaskDescription(r1);	 Catch:{ Exception -> 0x094c }
    L_0x0173:
        r35 = r40.getWindow();
        r36 = 2131165669; // 0x7f0701e5 float:1.7945562E38 double:1.0529357426E-314;
        r35.setBackgroundDrawableResource(r36);
        r35 = org.telegram.messenger.SharedConfig.passcodeHash;
        r35 = r35.length();
        if (r35 <= 0) goto L_0x0194;
    L_0x0185:
        r35 = org.telegram.messenger.SharedConfig.allowScreenCapture;
        if (r35 != 0) goto L_0x0194;
    L_0x0189:
        r35 = r40.getWindow();	 Catch:{ Exception -> 0x06a1 }
        r36 = 8192; // 0x2000 float:1.14794E-41 double:4.0474E-320;
        r37 = 8192; // 0x2000 float:1.14794E-41 double:4.0474E-320;
        r35.setFlags(r36, r37);	 Catch:{ Exception -> 0x06a1 }
    L_0x0194:
        super.onCreate(r41);
        r35 = android.os.Build.VERSION.SDK_INT;
        r36 = 24;
        r0 = r35;
        r1 = r36;
        if (r0 < r1) goto L_0x01a7;
    L_0x01a1:
        r35 = r40.isInMultiWindowMode();
        org.telegram.messenger.AndroidUtilities.isInMultiwindow = r35;
    L_0x01a7:
        r35 = 0;
        r0 = r40;
        r1 = r35;
        org.telegram.ui.ActionBar.Theme.createChatResources(r0, r1);
        r35 = org.telegram.messenger.SharedConfig.passcodeHash;
        r35 = r35.length();
        if (r35 == 0) goto L_0x01cc;
    L_0x01b8:
        r35 = org.telegram.messenger.SharedConfig.appLocked;
        if (r35 == 0) goto L_0x01cc;
    L_0x01bc:
        r0 = r40;
        r0 = r0.currentAccount;
        r35 = r0;
        r35 = org.telegram.tgnet.ConnectionsManager.getInstance(r35);
        r35 = r35.getCurrentTime();
        org.telegram.messenger.SharedConfig.lastPauseTime = r35;
    L_0x01cc:
        r35 = r40.getResources();
        r36 = "status_bar_height";
        r37 = "dimen";
        r38 = "android";
        r28 = r35.getIdentifier(r36, r37, r38);
        if (r28 <= 0) goto L_0x01ed;
    L_0x01df:
        r35 = r40.getResources();
        r0 = r35;
        r1 = r28;
        r35 = r0.getDimensionPixelSize(r1);
        org.telegram.messenger.AndroidUtilities.statusBarHeight = r35;
    L_0x01ed:
        r35 = new org.telegram.ui.ActionBar.ActionBarLayout;
        r0 = r35;
        r1 = r40;
        r0.<init>(r1);
        r0 = r35;
        r1 = r40;
        r1.actionBarLayout = r0;
        r35 = new org.telegram.ui.ActionBar.DrawerLayoutContainer;
        r0 = r35;
        r1 = r40;
        r0.<init>(r1);
        r0 = r35;
        r1 = r40;
        r1.drawerLayoutContainer = r0;
        r0 = r40;
        r0 = r0.drawerLayoutContainer;
        r35 = r0;
        r36 = new android.view.ViewGroup$LayoutParams;
        r37 = -1;
        r38 = -1;
        r36.<init>(r37, r38);
        r0 = r40;
        r1 = r35;
        r2 = r36;
        r0.setContentView(r1, r2);
        r35 = org.telegram.messenger.AndroidUtilities.isTablet();
        if (r35 == 0) goto L_0x06af;
    L_0x0229:
        r35 = r40.getWindow();
        r36 = 16;
        r35.setSoftInputMode(r36);
        r22 = new org.telegram.ui.LaunchActivity$1;
        r0 = r22;
        r1 = r40;
        r2 = r40;
        r0.<init>(r2);
        r0 = r40;
        r0 = r0.drawerLayoutContainer;
        r35 = r0;
        r36 = -1;
        r37 = -1082130432; // 0xffffffffbf800000 float:-1.0 double:NaN;
        r36 = org.telegram.ui.Components.LayoutHelper.createFrame(r36, r37);
        r0 = r35;
        r1 = r22;
        r2 = r36;
        r0.addView(r1, r2);
        r35 = new android.view.View;
        r0 = r35;
        r1 = r40;
        r0.<init>(r1);
        r0 = r35;
        r1 = r40;
        r1.backgroundTablet = r0;
        r35 = r40.getResources();
        r36 = 2131165246; // 0x7f07003e float:1.7944704E38 double:1.0529355337E-314;
        r13 = r35.getDrawable(r36);
        r13 = (android.graphics.drawable.BitmapDrawable) r13;
        r35 = android.graphics.Shader.TileMode.REPEAT;
        r36 = android.graphics.Shader.TileMode.REPEAT;
        r0 = r35;
        r1 = r36;
        r13.setTileModeXY(r0, r1);
        r0 = r40;
        r0 = r0.backgroundTablet;
        r35 = r0;
        r0 = r35;
        r0.setBackgroundDrawable(r13);
        r0 = r40;
        r0 = r0.backgroundTablet;
        r35 = r0;
        r36 = -1;
        r37 = -1;
        r36 = org.telegram.ui.Components.LayoutHelper.createRelative(r36, r37);
        r0 = r22;
        r1 = r35;
        r2 = r36;
        r0.addView(r1, r2);
        r0 = r40;
        r0 = r0.actionBarLayout;
        r35 = r0;
        r0 = r22;
        r1 = r35;
        r0.addView(r1);
        r35 = new org.telegram.ui.ActionBar.ActionBarLayout;
        r0 = r35;
        r1 = r40;
        r0.<init>(r1);
        r0 = r35;
        r1 = r40;
        r1.rightActionBarLayout = r0;
        r0 = r40;
        r0 = r0.rightActionBarLayout;
        r35 = r0;
        r36 = rightFragmentsStack;
        r35.init(r36);
        r0 = r40;
        r0 = r0.rightActionBarLayout;
        r35 = r0;
        r0 = r35;
        r1 = r40;
        r0.setDelegate(r1);
        r0 = r40;
        r0 = r0.rightActionBarLayout;
        r35 = r0;
        r0 = r22;
        r1 = r35;
        r0.addView(r1);
        r35 = new android.widget.FrameLayout;
        r0 = r35;
        r1 = r40;
        r0.<init>(r1);
        r0 = r35;
        r1 = r40;
        r1.shadowTabletSide = r0;
        r0 = r40;
        r0 = r0.shadowTabletSide;
        r35 = r0;
        r36 = 1076449908; // 0x40295274 float:2.6456575 double:5.31836919E-315;
        r35.setBackgroundColor(r36);
        r0 = r40;
        r0 = r0.shadowTabletSide;
        r35 = r0;
        r0 = r22;
        r1 = r35;
        r0.addView(r1);
        r35 = new android.widget.FrameLayout;
        r0 = r35;
        r1 = r40;
        r0.<init>(r1);
        r0 = r35;
        r1 = r40;
        r1.shadowTablet = r0;
        r0 = r40;
        r0 = r0.shadowTablet;
        r36 = r0;
        r35 = layerFragmentsStack;
        r35 = r35.isEmpty();
        if (r35 == 0) goto L_0x06a7;
    L_0x0323:
        r35 = 8;
    L_0x0325:
        r0 = r36;
        r1 = r35;
        r0.setVisibility(r1);
        r0 = r40;
        r0 = r0.shadowTablet;
        r35 = r0;
        r36 = 2130706432; // 0x7f000000 float:1.7014118E38 double:1.0527088494E-314;
        r35.setBackgroundColor(r36);
        r0 = r40;
        r0 = r0.shadowTablet;
        r35 = r0;
        r0 = r22;
        r1 = r35;
        r0.addView(r1);
        r0 = r40;
        r0 = r0.shadowTablet;
        r35 = r0;
        r36 = new org.telegram.ui.LaunchActivity$2;
        r0 = r36;
        r1 = r40;
        r0.<init>();
        r35.setOnTouchListener(r36);
        r0 = r40;
        r0 = r0.shadowTablet;
        r35 = r0;
        r36 = new org.telegram.ui.LaunchActivity$3;
        r0 = r36;
        r1 = r40;
        r0.<init>();
        r35.setOnClickListener(r36);
        r35 = new org.telegram.ui.ActionBar.ActionBarLayout;
        r0 = r35;
        r1 = r40;
        r0.<init>(r1);
        r0 = r35;
        r1 = r40;
        r1.layersActionBarLayout = r0;
        r0 = r40;
        r0 = r0.layersActionBarLayout;
        r35 = r0;
        r36 = 1;
        r35.setRemoveActionBarExtraHeight(r36);
        r0 = r40;
        r0 = r0.layersActionBarLayout;
        r35 = r0;
        r0 = r40;
        r0 = r0.shadowTablet;
        r36 = r0;
        r35.setBackgroundView(r36);
        r0 = r40;
        r0 = r0.layersActionBarLayout;
        r35 = r0;
        r36 = 1;
        r35.setUseAlphaAnimations(r36);
        r0 = r40;
        r0 = r0.layersActionBarLayout;
        r35 = r0;
        r36 = 2131165231; // 0x7f07002f float:1.7944673E38 double:1.0529355262E-314;
        r35.setBackgroundResource(r36);
        r0 = r40;
        r0 = r0.layersActionBarLayout;
        r35 = r0;
        r36 = layerFragmentsStack;
        r35.init(r36);
        r0 = r40;
        r0 = r0.layersActionBarLayout;
        r35 = r0;
        r0 = r35;
        r1 = r40;
        r0.setDelegate(r1);
        r0 = r40;
        r0 = r0.layersActionBarLayout;
        r35 = r0;
        r0 = r40;
        r0 = r0.drawerLayoutContainer;
        r36 = r0;
        r35.setDrawerLayoutContainer(r36);
        r0 = r40;
        r0 = r0.layersActionBarLayout;
        r36 = r0;
        r35 = layerFragmentsStack;
        r35 = r35.isEmpty();
        if (r35 == 0) goto L_0x06ab;
    L_0x03dd:
        r35 = 8;
    L_0x03df:
        r0 = r36;
        r1 = r35;
        r0.setVisibility(r1);
        r0 = r40;
        r0 = r0.layersActionBarLayout;
        r35 = r0;
        r0 = r22;
        r1 = r35;
        r0.addView(r1);
    L_0x03f3:
        r35 = new org.telegram.ui.Components.RecyclerListView;
        r0 = r35;
        r1 = r40;
        r0.<init>(r1);
        r0 = r35;
        r1 = r40;
        r1.sideMenu = r0;
        r0 = r40;
        r0 = r0.sideMenu;
        r35 = r0;
        r35 = r35.getItemAnimator();
        r35 = (org.telegram.messenger.support.widget.DefaultItemAnimator) r35;
        r36 = 0;
        r35.setDelayAnimations(r36);
        r0 = r40;
        r0 = r0.sideMenu;
        r35 = r0;
        r36 = "chats_menuBackground";
        r36 = org.telegram.ui.ActionBar.Theme.getColor(r36);
        r35.setBackgroundColor(r36);
        r0 = r40;
        r0 = r0.sideMenu;
        r35 = r0;
        r36 = new org.telegram.messenger.support.widget.LinearLayoutManager;
        r37 = 1;
        r38 = 0;
        r0 = r36;
        r1 = r40;
        r2 = r37;
        r3 = r38;
        r0.<init>(r1, r2, r3);
        r35.setLayoutManager(r36);
        r0 = r40;
        r0 = r0.sideMenu;
        r35 = r0;
        r36 = new org.telegram.ui.Adapters.DrawerLayoutAdapter;
        r0 = r36;
        r1 = r40;
        r0.<init>(r1);
        r0 = r36;
        r1 = r40;
        r1.drawerLayoutAdapter = r0;
        r35.setAdapter(r36);
        r0 = r40;
        r0 = r0.drawerLayoutContainer;
        r35 = r0;
        r0 = r40;
        r0 = r0.sideMenu;
        r36 = r0;
        r35.setDrawerLayout(r36);
        r0 = r40;
        r0 = r0.sideMenu;
        r35 = r0;
        r23 = r35.getLayoutParams();
        r23 = (android.widget.FrameLayout.LayoutParams) r23;
        r29 = org.telegram.messenger.AndroidUtilities.getRealScreenSize();
        r35 = org.telegram.messenger.AndroidUtilities.isTablet();
        if (r35 == 0) goto L_0x06c9;
    L_0x047a:
        r35 = 1134559232; // 0x43a00000 float:320.0 double:5.605467397E-315;
        r35 = org.telegram.messenger.AndroidUtilities.dp(r35);
    L_0x0480:
        r0 = r35;
        r1 = r23;
        r1.width = r0;
        r35 = -1;
        r0 = r35;
        r1 = r23;
        r1.height = r0;
        r0 = r40;
        r0 = r0.sideMenu;
        r35 = r0;
        r0 = r35;
        r1 = r23;
        r0.setLayoutParams(r1);
        r0 = r40;
        r0 = r0.sideMenu;
        r35 = r0;
        r36 = new org.telegram.ui.LaunchActivity$4;
        r0 = r36;
        r1 = r40;
        r0.<init>();
        r35.setOnItemClickListener(r36);
        r0 = r40;
        r0 = r0.drawerLayoutContainer;
        r35 = r0;
        r0 = r40;
        r0 = r0.actionBarLayout;
        r36 = r0;
        r35.setParentActionBarLayout(r36);
        r0 = r40;
        r0 = r0.actionBarLayout;
        r35 = r0;
        r0 = r40;
        r0 = r0.drawerLayoutContainer;
        r36 = r0;
        r35.setDrawerLayoutContainer(r36);
        r0 = r40;
        r0 = r0.actionBarLayout;
        r35 = r0;
        r36 = mainFragmentsStack;
        r35.init(r36);
        r0 = r40;
        r0 = r0.actionBarLayout;
        r35 = r0;
        r0 = r35;
        r1 = r40;
        r0.setDelegate(r1);
        org.telegram.ui.ActionBar.Theme.loadWallpaper();
        r35 = new org.telegram.ui.Components.PasscodeView;
        r0 = r35;
        r1 = r40;
        r0.<init>(r1);
        r0 = r35;
        r1 = r40;
        r1.passcodeView = r0;
        r0 = r40;
        r0 = r0.drawerLayoutContainer;
        r35 = r0;
        r0 = r40;
        r0 = r0.passcodeView;
        r36 = r0;
        r37 = -1;
        r38 = -1082130432; // 0xffffffffbf800000 float:-1.0 double:NaN;
        r37 = org.telegram.ui.Components.LayoutHelper.createFrame(r37, r38);
        r35.addView(r36, r37);
        r40.checkCurrentAccount();
        r35 = org.telegram.messenger.NotificationCenter.getGlobalInstance();
        r36 = org.telegram.messenger.NotificationCenter.closeOtherAppActivities;
        r37 = 1;
        r0 = r37;
        r0 = new java.lang.Object[r0];
        r37 = r0;
        r38 = 0;
        r37[r38] = r40;
        r35.postNotificationName(r36, r37);
        r0 = r40;
        r0 = r0.currentAccount;
        r35 = r0;
        r35 = org.telegram.tgnet.ConnectionsManager.getInstance(r35);
        r35 = r35.getConnectionState();
        r0 = r35;
        r1 = r40;
        r1.currentConnectionState = r0;
        r35 = org.telegram.messenger.NotificationCenter.getGlobalInstance();
        r36 = org.telegram.messenger.NotificationCenter.needShowAlert;
        r0 = r35;
        r1 = r40;
        r2 = r36;
        r0.addObserver(r1, r2);
        r35 = org.telegram.messenger.NotificationCenter.getGlobalInstance();
        r36 = org.telegram.messenger.NotificationCenter.reloadInterface;
        r0 = r35;
        r1 = r40;
        r2 = r36;
        r0.addObserver(r1, r2);
        r35 = org.telegram.messenger.NotificationCenter.getGlobalInstance();
        r36 = org.telegram.messenger.NotificationCenter.suggestedLangpack;
        r0 = r35;
        r1 = r40;
        r2 = r36;
        r0.addObserver(r1, r2);
        r35 = org.telegram.messenger.NotificationCenter.getGlobalInstance();
        r36 = org.telegram.messenger.NotificationCenter.didSetNewTheme;
        r0 = r35;
        r1 = r40;
        r2 = r36;
        r0.addObserver(r1, r2);
        r35 = org.telegram.messenger.NotificationCenter.getGlobalInstance();
        r36 = org.telegram.messenger.NotificationCenter.needSetDayNightTheme;
        r0 = r35;
        r1 = r40;
        r2 = r36;
        r0.addObserver(r1, r2);
        r35 = org.telegram.messenger.NotificationCenter.getGlobalInstance();
        r36 = org.telegram.messenger.NotificationCenter.closeOtherAppActivities;
        r0 = r35;
        r1 = r40;
        r2 = r36;
        r0.addObserver(r1, r2);
        r35 = org.telegram.messenger.NotificationCenter.getGlobalInstance();
        r36 = org.telegram.messenger.NotificationCenter.didSetPasscode;
        r0 = r35;
        r1 = r40;
        r2 = r36;
        r0.addObserver(r1, r2);
        r35 = org.telegram.messenger.NotificationCenter.getGlobalInstance();
        r36 = org.telegram.messenger.NotificationCenter.didSetNewWallpapper;
        r0 = r35;
        r1 = r40;
        r2 = r36;
        r0.addObserver(r1, r2);
        r35 = org.telegram.messenger.NotificationCenter.getGlobalInstance();
        r36 = org.telegram.messenger.NotificationCenter.notificationsCountUpdated;
        r0 = r35;
        r1 = r40;
        r2 = r36;
        r0.addObserver(r1, r2);
        r0 = r40;
        r0 = r0.actionBarLayout;
        r35 = r0;
        r0 = r35;
        r0 = r0.fragmentsStack;
        r35 = r0;
        r35 = r35.isEmpty();
        if (r35 == 0) goto L_0x0866;
    L_0x05d1:
        r0 = r40;
        r0 = r0.currentAccount;
        r35 = r0;
        r35 = org.telegram.messenger.UserConfig.getInstance(r35);
        r35 = r35.isClientActivated();
        if (r35 != 0) goto L_0x06ed;
    L_0x05e1:
        r0 = r40;
        r0 = r0.actionBarLayout;
        r35 = r0;
        r36 = new org.telegram.ui.LoginActivity;
        r36.<init>();
        r35.addFragmentToStack(r36);
        r0 = r40;
        r0 = r0.drawerLayoutContainer;
        r35 = r0;
        r36 = 0;
        r37 = 0;
        r35.setAllowOpenDrawer(r36, r37);
    L_0x05fc:
        if (r41 == 0) goto L_0x0622;
    L_0x05fe:
        r35 = "fragment";
        r0 = r41;
        r1 = r35;
        r16 = r0.getString(r1);	 Catch:{ Exception -> 0x07ae }
        if (r16 == 0) goto L_0x0622;
    L_0x060b:
        r35 = "args";
        r0 = r41;
        r1 = r35;
        r7 = r0.getBundle(r1);	 Catch:{ Exception -> 0x07ae }
        r35 = -1;
        r36 = r16.hashCode();	 Catch:{ Exception -> 0x07ae }
        switch(r36) {
            case -1529105743: goto L_0x0781;
            case -1349522494: goto L_0x0770;
            case 3052376: goto L_0x071b;
            case 3108362: goto L_0x075f;
            case 98629247: goto L_0x073d;
            case 738950403: goto L_0x074e;
            case 1434631203: goto L_0x072c;
            default: goto L_0x061f;
        };
    L_0x061f:
        switch(r35) {
            case 0: goto L_0x0792;
            case 1: goto L_0x07b4;
            case 2: goto L_0x07cf;
            case 3: goto L_0x07f1;
            case 4: goto L_0x080d;
            case 5: goto L_0x0829;
            case 6: goto L_0x084b;
            default: goto L_0x0622;
        };
    L_0x0622:
        r40.checkLayout();
        r36 = r40.getIntent();
        r37 = 0;
        if (r41 == 0) goto L_0x0938;
    L_0x062d:
        r35 = 1;
    L_0x062f:
        r38 = 0;
        r0 = r40;
        r1 = r36;
        r2 = r37;
        r3 = r35;
        r4 = r38;
        r0.handleIntent(r1, r2, r3, r4);
        r24 = android.os.Build.DISPLAY;	 Catch:{ Exception -> 0x0946 }
        r25 = android.os.Build.USER;	 Catch:{ Exception -> 0x0946 }
        if (r24 == 0) goto L_0x093c;
    L_0x0644:
        r24 = r24.toLowerCase();	 Catch:{ Exception -> 0x0946 }
    L_0x0648:
        if (r25 == 0) goto L_0x0941;
    L_0x064a:
        r25 = r24.toLowerCase();	 Catch:{ Exception -> 0x0946 }
    L_0x064e:
        r35 = "flyme";
        r0 = r24;
        r1 = r35;
        r35 = r0.contains(r1);	 Catch:{ Exception -> 0x0946 }
        if (r35 != 0) goto L_0x0668;
    L_0x065b:
        r35 = "flyme";
        r0 = r25;
        r1 = r35;
        r35 = r0.contains(r1);	 Catch:{ Exception -> 0x0946 }
        if (r35 == 0) goto L_0x0690;
    L_0x0668:
        r35 = 1;
        org.telegram.messenger.AndroidUtilities.incorrectDisplaySizeFix = r35;	 Catch:{ Exception -> 0x0946 }
        r35 = r40.getWindow();	 Catch:{ Exception -> 0x0946 }
        r35 = r35.getDecorView();	 Catch:{ Exception -> 0x0946 }
        r34 = r35.getRootView();	 Catch:{ Exception -> 0x0946 }
        r35 = r34.getViewTreeObserver();	 Catch:{ Exception -> 0x0946 }
        r36 = new org.telegram.ui.LaunchActivity$5;	 Catch:{ Exception -> 0x0946 }
        r0 = r36;
        r1 = r40;
        r2 = r34;
        r0.<init>(r2);	 Catch:{ Exception -> 0x0946 }
        r0 = r36;
        r1 = r40;
        r1.onGlobalLayoutListener = r0;	 Catch:{ Exception -> 0x0946 }
        r35.addOnGlobalLayoutListener(r36);	 Catch:{ Exception -> 0x0946 }
    L_0x0690:
        r35 = org.telegram.messenger.MediaController.getInstance();
        r36 = 1;
        r0 = r35;
        r1 = r40;
        r2 = r36;
        r0.setBaseActivity(r1, r2);
        goto L_0x0058;
    L_0x06a1:
        r14 = move-exception;
        org.telegram.messenger.FileLog.e(r14);
        goto L_0x0194;
    L_0x06a7:
        r35 = 0;
        goto L_0x0325;
    L_0x06ab:
        r35 = 0;
        goto L_0x03df;
    L_0x06af:
        r0 = r40;
        r0 = r0.drawerLayoutContainer;
        r35 = r0;
        r0 = r40;
        r0 = r0.actionBarLayout;
        r36 = r0;
        r37 = new android.view.ViewGroup$LayoutParams;
        r38 = -1;
        r39 = -1;
        r37.<init>(r38, r39);
        r35.addView(r36, r37);
        goto L_0x03f3;
    L_0x06c9:
        r35 = 1134559232; // 0x43a00000 float:320.0 double:5.605467397E-315;
        r35 = org.telegram.messenger.AndroidUtilities.dp(r35);
        r0 = r29;
        r0 = r0.x;
        r36 = r0;
        r0 = r29;
        r0 = r0.y;
        r37 = r0;
        r36 = java.lang.Math.min(r36, r37);
        r37 = 1113587712; // 0x42600000 float:56.0 double:5.50185432E-315;
        r37 = org.telegram.messenger.AndroidUtilities.dp(r37);
        r36 = r36 - r37;
        r35 = java.lang.Math.min(r35, r36);
        goto L_0x0480;
    L_0x06ed:
        r12 = new org.telegram.ui.DialogsActivity;
        r35 = 0;
        r0 = r35;
        r12.<init>(r0);
        r0 = r40;
        r0 = r0.sideMenu;
        r35 = r0;
        r0 = r35;
        r12.setSideMenu(r0);
        r0 = r40;
        r0 = r0.actionBarLayout;
        r35 = r0;
        r0 = r35;
        r0.addFragmentToStack(r12);
        r0 = r40;
        r0 = r0.drawerLayoutContainer;
        r35 = r0;
        r36 = 1;
        r37 = 0;
        r35.setAllowOpenDrawer(r36, r37);
        goto L_0x05fc;
    L_0x071b:
        r36 = "chat";
        r0 = r16;
        r1 = r36;
        r36 = r0.equals(r1);	 Catch:{ Exception -> 0x07ae }
        if (r36 == 0) goto L_0x061f;
    L_0x0728:
        r35 = 0;
        goto L_0x061f;
    L_0x072c:
        r36 = "settings";
        r0 = r16;
        r1 = r36;
        r36 = r0.equals(r1);	 Catch:{ Exception -> 0x07ae }
        if (r36 == 0) goto L_0x061f;
    L_0x0739:
        r35 = 1;
        goto L_0x061f;
    L_0x073d:
        r36 = "group";
        r0 = r16;
        r1 = r36;
        r36 = r0.equals(r1);	 Catch:{ Exception -> 0x07ae }
        if (r36 == 0) goto L_0x061f;
    L_0x074a:
        r35 = 2;
        goto L_0x061f;
    L_0x074e:
        r36 = "channel";
        r0 = r16;
        r1 = r36;
        r36 = r0.equals(r1);	 Catch:{ Exception -> 0x07ae }
        if (r36 == 0) goto L_0x061f;
    L_0x075b:
        r35 = 3;
        goto L_0x061f;
    L_0x075f:
        r36 = "edit";
        r0 = r16;
        r1 = r36;
        r36 = r0.equals(r1);	 Catch:{ Exception -> 0x07ae }
        if (r36 == 0) goto L_0x061f;
    L_0x076c:
        r35 = 4;
        goto L_0x061f;
    L_0x0770:
        r36 = "chat_profile";
        r0 = r16;
        r1 = r36;
        r36 = r0.equals(r1);	 Catch:{ Exception -> 0x07ae }
        if (r36 == 0) goto L_0x061f;
    L_0x077d:
        r35 = 5;
        goto L_0x061f;
    L_0x0781:
        r36 = "wallpapers";
        r0 = r16;
        r1 = r36;
        r36 = r0.equals(r1);	 Catch:{ Exception -> 0x07ae }
        if (r36 == 0) goto L_0x061f;
    L_0x078e:
        r35 = 6;
        goto L_0x061f;
    L_0x0792:
        if (r7 == 0) goto L_0x0622;
    L_0x0794:
        r9 = new org.telegram.ui.ChatActivity;	 Catch:{ Exception -> 0x07ae }
        r9.<init>(r7);	 Catch:{ Exception -> 0x07ae }
        r0 = r40;
        r0 = r0.actionBarLayout;	 Catch:{ Exception -> 0x07ae }
        r35 = r0;
        r0 = r35;
        r35 = r0.addFragmentToStack(r9);	 Catch:{ Exception -> 0x07ae }
        if (r35 == 0) goto L_0x0622;
    L_0x07a7:
        r0 = r41;
        r9.restoreSelfArgs(r0);	 Catch:{ Exception -> 0x07ae }
        goto L_0x0622;
    L_0x07ae:
        r14 = move-exception;
        org.telegram.messenger.FileLog.e(r14);
        goto L_0x0622;
    L_0x07b4:
        r30 = new org.telegram.ui.SettingsActivity;	 Catch:{ Exception -> 0x07ae }
        r30.<init>();	 Catch:{ Exception -> 0x07ae }
        r0 = r40;
        r0 = r0.actionBarLayout;	 Catch:{ Exception -> 0x07ae }
        r35 = r0;
        r0 = r35;
        r1 = r30;
        r0.addFragmentToStack(r1);	 Catch:{ Exception -> 0x07ae }
        r0 = r30;
        r1 = r41;
        r0.restoreSelfArgs(r1);	 Catch:{ Exception -> 0x07ae }
        goto L_0x0622;
    L_0x07cf:
        if (r7 == 0) goto L_0x0622;
    L_0x07d1:
        r18 = new org.telegram.ui.GroupCreateFinalActivity;	 Catch:{ Exception -> 0x07ae }
        r0 = r18;
        r0.<init>(r7);	 Catch:{ Exception -> 0x07ae }
        r0 = r40;
        r0 = r0.actionBarLayout;	 Catch:{ Exception -> 0x07ae }
        r35 = r0;
        r0 = r35;
        r1 = r18;
        r35 = r0.addFragmentToStack(r1);	 Catch:{ Exception -> 0x07ae }
        if (r35 == 0) goto L_0x0622;
    L_0x07e8:
        r0 = r18;
        r1 = r41;
        r0.restoreSelfArgs(r1);	 Catch:{ Exception -> 0x07ae }
        goto L_0x0622;
    L_0x07f1:
        if (r7 == 0) goto L_0x0622;
    L_0x07f3:
        r8 = new org.telegram.ui.ChannelCreateActivity;	 Catch:{ Exception -> 0x07ae }
        r8.<init>(r7);	 Catch:{ Exception -> 0x07ae }
        r0 = r40;
        r0 = r0.actionBarLayout;	 Catch:{ Exception -> 0x07ae }
        r35 = r0;
        r0 = r35;
        r35 = r0.addFragmentToStack(r8);	 Catch:{ Exception -> 0x07ae }
        if (r35 == 0) goto L_0x0622;
    L_0x0806:
        r0 = r41;
        r8.restoreSelfArgs(r0);	 Catch:{ Exception -> 0x07ae }
        goto L_0x0622;
    L_0x080d:
        if (r7 == 0) goto L_0x0622;
    L_0x080f:
        r8 = new org.telegram.ui.ChannelEditActivity;	 Catch:{ Exception -> 0x07ae }
        r8.<init>(r7);	 Catch:{ Exception -> 0x07ae }
        r0 = r40;
        r0 = r0.actionBarLayout;	 Catch:{ Exception -> 0x07ae }
        r35 = r0;
        r0 = r35;
        r35 = r0.addFragmentToStack(r8);	 Catch:{ Exception -> 0x07ae }
        if (r35 == 0) goto L_0x0622;
    L_0x0822:
        r0 = r41;
        r8.restoreSelfArgs(r0);	 Catch:{ Exception -> 0x07ae }
        goto L_0x0622;
    L_0x0829:
        if (r7 == 0) goto L_0x0622;
    L_0x082b:
        r27 = new org.telegram.ui.ProfileActivity;	 Catch:{ Exception -> 0x07ae }
        r0 = r27;
        r0.<init>(r7);	 Catch:{ Exception -> 0x07ae }
        r0 = r40;
        r0 = r0.actionBarLayout;	 Catch:{ Exception -> 0x07ae }
        r35 = r0;
        r0 = r35;
        r1 = r27;
        r35 = r0.addFragmentToStack(r1);	 Catch:{ Exception -> 0x07ae }
        if (r35 == 0) goto L_0x0622;
    L_0x0842:
        r0 = r27;
        r1 = r41;
        r0.restoreSelfArgs(r1);	 Catch:{ Exception -> 0x07ae }
        goto L_0x0622;
    L_0x084b:
        r30 = new org.telegram.ui.WallpapersActivity;	 Catch:{ Exception -> 0x07ae }
        r30.<init>();	 Catch:{ Exception -> 0x07ae }
        r0 = r40;
        r0 = r0.actionBarLayout;	 Catch:{ Exception -> 0x07ae }
        r35 = r0;
        r0 = r35;
        r1 = r30;
        r0.addFragmentToStack(r1);	 Catch:{ Exception -> 0x07ae }
        r0 = r30;
        r1 = r41;
        r0.restoreSelfArgs(r1);	 Catch:{ Exception -> 0x07ae }
        goto L_0x0622;
    L_0x0866:
        r0 = r40;
        r0 = r0.actionBarLayout;
        r35 = r0;
        r0 = r35;
        r0 = r0.fragmentsStack;
        r35 = r0;
        r36 = 0;
        r15 = r35.get(r36);
        r15 = (org.telegram.ui.ActionBar.BaseFragment) r15;
        r0 = r15 instanceof org.telegram.ui.DialogsActivity;
        r35 = r0;
        if (r35 == 0) goto L_0x088d;
    L_0x0880:
        r15 = (org.telegram.ui.DialogsActivity) r15;
        r0 = r40;
        r0 = r0.sideMenu;
        r35 = r0;
        r0 = r35;
        r15.setSideMenu(r0);
    L_0x088d:
        r6 = 1;
        r35 = org.telegram.messenger.AndroidUtilities.isTablet();
        if (r35 == 0) goto L_0x08f2;
    L_0x0894:
        r0 = r40;
        r0 = r0.actionBarLayout;
        r35 = r0;
        r0 = r35;
        r0 = r0.fragmentsStack;
        r35 = r0;
        r35 = r35.size();
        r36 = 1;
        r0 = r35;
        r1 = r36;
        if (r0 > r1) goto L_0x0936;
    L_0x08ac:
        r0 = r40;
        r0 = r0.layersActionBarLayout;
        r35 = r0;
        r0 = r35;
        r0 = r0.fragmentsStack;
        r35 = r0;
        r35 = r35.isEmpty();
        if (r35 == 0) goto L_0x0936;
    L_0x08be:
        r6 = 1;
    L_0x08bf:
        r0 = r40;
        r0 = r0.layersActionBarLayout;
        r35 = r0;
        r0 = r35;
        r0 = r0.fragmentsStack;
        r35 = r0;
        r35 = r35.size();
        r36 = 1;
        r0 = r35;
        r1 = r36;
        if (r0 != r1) goto L_0x08f2;
    L_0x08d7:
        r0 = r40;
        r0 = r0.layersActionBarLayout;
        r35 = r0;
        r0 = r35;
        r0 = r0.fragmentsStack;
        r35 = r0;
        r36 = 0;
        r35 = r35.get(r36);
        r0 = r35;
        r0 = r0 instanceof org.telegram.ui.LoginActivity;
        r35 = r0;
        if (r35 == 0) goto L_0x08f2;
    L_0x08f1:
        r6 = 0;
    L_0x08f2:
        r0 = r40;
        r0 = r0.actionBarLayout;
        r35 = r0;
        r0 = r35;
        r0 = r0.fragmentsStack;
        r35 = r0;
        r35 = r35.size();
        r36 = 1;
        r0 = r35;
        r1 = r36;
        if (r0 != r1) goto L_0x0925;
    L_0x090a:
        r0 = r40;
        r0 = r0.actionBarLayout;
        r35 = r0;
        r0 = r35;
        r0 = r0.fragmentsStack;
        r35 = r0;
        r36 = 0;
        r35 = r35.get(r36);
        r0 = r35;
        r0 = r0 instanceof org.telegram.ui.LoginActivity;
        r35 = r0;
        if (r35 == 0) goto L_0x0925;
    L_0x0924:
        r6 = 0;
    L_0x0925:
        r0 = r40;
        r0 = r0.drawerLayoutContainer;
        r35 = r0;
        r36 = 0;
        r0 = r35;
        r1 = r36;
        r0.setAllowOpenDrawer(r6, r1);
        goto L_0x0622;
    L_0x0936:
        r6 = 0;
        goto L_0x08bf;
    L_0x0938:
        r35 = 0;
        goto L_0x062f;
    L_0x093c:
        r24 = "";
        goto L_0x0648;
    L_0x0941:
        r25 = "";
        goto L_0x064e;
    L_0x0946:
        r14 = move-exception;
        org.telegram.messenger.FileLog.e(r14);
        goto L_0x0690;
    L_0x094c:
        r35 = move-exception;
        goto L_0x0173;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.telegram.ui.LaunchActivity.onCreate(android.os.Bundle):void");
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
            if (UserConfig.getInstance(account).unacceptedTermsOfService != null) {
                showTosActivity(account, UserConfig.getInstance(account).unacceptedTermsOfService);
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
        if (this.termsOfServiceView != null) {
            this.termsOfServiceView.setVisibility(8);
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
        int i = 0;
        int i2 = 8;
        if (AndroidUtilities.isTablet() && this.rightActionBarLayout != null) {
            int a;
            BaseFragment chatFragment;
            if (AndroidUtilities.isInMultiwindow || (AndroidUtilities.isSmallTablet() && getResources().getConfiguration().orientation != 2)) {
                this.tabletFullSize = true;
                if (!this.rightActionBarLayout.fragmentsStack.isEmpty()) {
                    a = 0;
                    while (this.rightActionBarLayout.fragmentsStack.size() > 0) {
                        chatFragment = (BaseFragment) this.rightActionBarLayout.fragmentsStack.get(a);
                        if (chatFragment instanceof ChatActivity) {
                            ((ChatActivity) chatFragment).setIgnoreAttachOnPause(true);
                        }
                        chatFragment.onPause();
                        this.rightActionBarLayout.fragmentsStack.remove(a);
                        this.actionBarLayout.fragmentsStack.add(chatFragment);
                        a = (a - 1) + 1;
                    }
                    if (this.passcodeView.getVisibility() != 0) {
                        this.actionBarLayout.showLastFragment();
                    }
                }
                this.shadowTabletSide.setVisibility(8);
                this.rightActionBarLayout.setVisibility(8);
                View view = this.backgroundTablet;
                if (this.actionBarLayout.fragmentsStack.isEmpty()) {
                    i2 = 0;
                }
                view.setVisibility(i2);
                return;
            }
            int i3;
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
            ActionBarLayout actionBarLayout = this.rightActionBarLayout;
            if (this.rightActionBarLayout.fragmentsStack.isEmpty()) {
                i3 = 8;
            } else {
                i3 = 0;
            }
            actionBarLayout.setVisibility(i3);
            View view2 = this.backgroundTablet;
            if (this.rightActionBarLayout.fragmentsStack.isEmpty()) {
                i3 = 0;
            } else {
                i3 = 8;
            }
            view2.setVisibility(i3);
            FrameLayout frameLayout = this.shadowTabletSide;
            if (this.actionBarLayout.fragmentsStack.isEmpty()) {
                i = 8;
            }
            frameLayout.setVisibility(i);
        }
    }

    private void showUpdateActivity(int account, TL_help_appUpdate update) {
        if (this.blockingUpdateView == null) {
            this.blockingUpdateView = new BlockingUpdateView(this) {
                public void setVisibility(int visibility) {
                    super.setVisibility(visibility);
                    if (visibility == 8) {
                        LaunchActivity.this.drawerLayoutContainer.setAllowOpenDrawer(true, false);
                    }
                }
            };
            this.drawerLayoutContainer.addView(this.blockingUpdateView, LayoutHelper.createFrame(-1, -1.0f));
        }
        this.blockingUpdateView.show(account, update);
        this.drawerLayoutContainer.setAllowOpenDrawer(false, false);
    }

    private void showTosActivity(int account, TL_help_termsOfService tos) {
        if (this.termsOfServiceView == null) {
            this.termsOfServiceView = new TermsOfServiceView(this);
            this.drawerLayoutContainer.addView(this.termsOfServiceView, LayoutHelper.createFrame(-1, -1.0f));
            this.termsOfServiceView.setDelegate(new TermsOfServiceViewDelegate() {
                public void onAcceptTerms(int account) {
                    UserConfig.getInstance(account).unacceptedTermsOfService = null;
                    UserConfig.getInstance(account).saveConfig(false);
                    LaunchActivity.this.drawerLayoutContainer.setAllowOpenDrawer(true, false);
                    LaunchActivity.this.termsOfServiceView.setVisibility(8);
                }

                public void onDeclineTerms(int account) {
                    LaunchActivity.this.drawerLayoutContainer.setAllowOpenDrawer(true, false);
                    LaunchActivity.this.termsOfServiceView.setVisibility(8);
                }
            });
        }
        TL_help_termsOfService currentTos = UserConfig.getInstance(account).unacceptedTermsOfService;
        if (currentTos != tos && (currentTos == null || !currentTos.id.data.equals(tos.id.data))) {
            UserConfig.getInstance(account).unacceptedTermsOfService = tos;
            UserConfig.getInstance(account).saveConfig(false);
        }
        this.termsOfServiceView.show(account, tos);
        this.drawerLayoutContainer.setAllowOpenDrawer(false, false);
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

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private boolean handleIntent(android.content.Intent r74, boolean r75, boolean r76, boolean r77) {
        /*
        r73 = this;
        r4 = org.telegram.messenger.AndroidUtilities.handleProxyIntent(r73, r74);
        if (r4 == 0) goto L_0x0024;
    L_0x0006:
        r0 = r73;
        r4 = r0.actionBarLayout;
        r4.showLastFragment();
        r4 = org.telegram.messenger.AndroidUtilities.isTablet();
        if (r4 == 0) goto L_0x0021;
    L_0x0013:
        r0 = r73;
        r4 = r0.layersActionBarLayout;
        r4.showLastFragment();
        r0 = r73;
        r4 = r0.rightActionBarLayout;
        r4.showLastFragment();
    L_0x0021:
        r54 = 1;
    L_0x0023:
        return r54;
    L_0x0024:
        r4 = org.telegram.ui.PhotoViewer.hasInstance();
        if (r4 == 0) goto L_0x004f;
    L_0x002a:
        r4 = org.telegram.ui.PhotoViewer.getInstance();
        r4 = r4.isVisible();
        if (r4 == 0) goto L_0x004f;
    L_0x0034:
        if (r74 == 0) goto L_0x0043;
    L_0x0036:
        r4 = "android.intent.action.MAIN";
        r5 = r74.getAction();
        r4 = r4.equals(r5);
        if (r4 != 0) goto L_0x004f;
    L_0x0043:
        r4 = org.telegram.ui.PhotoViewer.getInstance();
        r5 = 0;
        r18 = 1;
        r0 = r18;
        r4.closePhoto(r5, r0);
    L_0x004f:
        r41 = r74.getFlags();
        r4 = 1;
        r0 = new int[r4];
        r45 = r0;
        r4 = 0;
        r5 = "currentAccount";
        r18 = org.telegram.messenger.UserConfig.selectedAccount;
        r0 = r74;
        r1 = r18;
        r5 = r0.getIntExtra(r5, r1);
        r45[r4] = r5;
        r4 = 0;
        r4 = r45[r4];
        r5 = 1;
        r0 = r73;
        r0.switchToAccount(r4, r5);
        if (r77 != 0) goto L_0x00a2;
    L_0x0073:
        r4 = 1;
        r4 = org.telegram.messenger.AndroidUtilities.needShowPasscode(r4);
        if (r4 != 0) goto L_0x007e;
    L_0x007a:
        r4 = org.telegram.messenger.SharedConfig.isWaitingForPasscodeEnter;
        if (r4 == 0) goto L_0x00a2;
    L_0x007e:
        r73.showPasscodeActivity();
        r0 = r74;
        r1 = r73;
        r1.passcodeSaveIntent = r0;
        r0 = r75;
        r1 = r73;
        r1.passcodeSaveIntentIsNew = r0;
        r0 = r76;
        r1 = r73;
        r1.passcodeSaveIntentIsRestore = r0;
        r0 = r73;
        r4 = r0.currentAccount;
        r4 = org.telegram.messenger.UserConfig.getInstance(r4);
        r5 = 0;
        r4.saveConfig(r5);
        r54 = 0;
        goto L_0x0023;
    L_0x00a2:
        r54 = 0;
        r4 = 0;
        r58 = java.lang.Integer.valueOf(r4);
        r4 = 0;
        r55 = java.lang.Integer.valueOf(r4);
        r4 = 0;
        r56 = java.lang.Integer.valueOf(r4);
        r4 = 0;
        r57 = java.lang.Integer.valueOf(r4);
        r4 = 0;
        r48 = java.lang.Integer.valueOf(r4);
        r4 = 0;
        r47 = java.lang.Integer.valueOf(r4);
        r34 = 0;
        r4 = org.telegram.messenger.SharedConfig.directShare;
        if (r4 == 0) goto L_0x00df;
    L_0x00c8:
        if (r74 == 0) goto L_0x025b;
    L_0x00ca:
        r4 = r74.getExtras();
        if (r4 == 0) goto L_0x025b;
    L_0x00d0:
        r4 = r74.getExtras();
        r5 = "dialogId";
        r22 = 0;
        r0 = r22;
        r34 = r4.getLong(r5, r0);
    L_0x00df:
        r61 = 0;
        r63 = 0;
        r62 = 0;
        r4 = 0;
        r0 = r73;
        r0.photoPathsArray = r4;
        r4 = 0;
        r0 = r73;
        r0.videoPath = r4;
        r4 = 0;
        r0 = r73;
        r0.sendingText = r4;
        r4 = 0;
        r0 = r73;
        r0.documentsPathsArray = r4;
        r4 = 0;
        r0 = r73;
        r0.documentsOriginalPathsArray = r4;
        r4 = 0;
        r0 = r73;
        r0.documentsMimeType = r4;
        r4 = 0;
        r0 = r73;
        r0.documentsUrisArray = r4;
        r4 = 0;
        r0 = r73;
        r0.contactsToSend = r4;
        r4 = 0;
        r0 = r73;
        r0.contactsToSendUri = r4;
        r0 = r73;
        r4 = r0.currentAccount;
        r4 = org.telegram.messenger.UserConfig.getInstance(r4);
        r4 = r4.isClientActivated();
        if (r4 == 0) goto L_0x018b;
    L_0x0120:
        r4 = 1048576; // 0x100000 float:1.469368E-39 double:5.180654E-318;
        r4 = r4 & r41;
        if (r4 != 0) goto L_0x018b;
    L_0x0126:
        if (r74 == 0) goto L_0x018b;
    L_0x0128:
        r4 = r74.getAction();
        if (r4 == 0) goto L_0x018b;
    L_0x012e:
        if (r76 != 0) goto L_0x018b;
    L_0x0130:
        r4 = "android.intent.action.SEND";
        r5 = r74.getAction();
        r4 = r4.equals(r5);
        if (r4 == 0) goto L_0x03dd;
    L_0x013d:
        r40 = 0;
        r67 = r74.getType();
        if (r67 == 0) goto L_0x026b;
    L_0x0145:
        r4 = "text/x-vcard";
        r0 = r67;
        r4 = r0.equals(r4);
        if (r4 == 0) goto L_0x026b;
    L_0x0150:
        r4 = r74.getExtras();	 Catch:{ Exception -> 0x0263 }
        r5 = "android.intent.extra.STREAM";
        r68 = r4.get(r5);	 Catch:{ Exception -> 0x0263 }
        r68 = (android.net.Uri) r68;	 Catch:{ Exception -> 0x0263 }
        if (r68 == 0) goto L_0x025f;
    L_0x015f:
        r0 = r73;
        r4 = r0.currentAccount;	 Catch:{ Exception -> 0x0263 }
        r5 = 0;
        r18 = 0;
        r21 = 0;
        r0 = r68;
        r1 = r18;
        r2 = r21;
        r4 = org.telegram.messenger.AndroidUtilities.loadVCardFromStream(r0, r4, r5, r1, r2);	 Catch:{ Exception -> 0x0263 }
        r0 = r73;
        r0.contactsToSend = r4;	 Catch:{ Exception -> 0x0263 }
        r0 = r68;
        r1 = r73;
        r1.contactsToSendUri = r0;	 Catch:{ Exception -> 0x0263 }
    L_0x017c:
        if (r40 == 0) goto L_0x018b;
    L_0x017e:
        r4 = "Unsupported content";
        r5 = 0;
        r0 = r73;
        r4 = android.widget.Toast.makeText(r0, r4, r5);
        r4.show();
    L_0x018b:
        r4 = r58.intValue();
        if (r4 == 0) goto L_0x0cad;
    L_0x0191:
        r29 = new android.os.Bundle;
        r29.<init>();
        r4 = "user_id";
        r5 = r58.intValue();
        r0 = r29;
        r0.putInt(r4, r5);
        r4 = r57.intValue();
        if (r4 == 0) goto L_0x01b4;
    L_0x01a8:
        r4 = "message_id";
        r5 = r57.intValue();
        r0 = r29;
        r0.putInt(r4, r5);
    L_0x01b4:
        r4 = mainFragmentsStack;
        r4 = r4.isEmpty();
        if (r4 != 0) goto L_0x01dd;
    L_0x01bc:
        r4 = 0;
        r4 = r45[r4];
        r5 = org.telegram.messenger.MessagesController.getInstance(r4);
        r4 = mainFragmentsStack;
        r18 = mainFragmentsStack;
        r18 = r18.size();
        r18 = r18 + -1;
        r0 = r18;
        r4 = r4.get(r0);
        r4 = (org.telegram.ui.ActionBar.BaseFragment) r4;
        r0 = r29;
        r4 = r5.checkCanOpenChat(r0, r4);
        if (r4 == 0) goto L_0x01fc;
    L_0x01dd:
        r19 = new org.telegram.ui.ChatActivity;
        r0 = r19;
        r1 = r29;
        r0.<init>(r1);
        r0 = r73;
        r0 = r0.actionBarLayout;
        r18 = r0;
        r20 = 0;
        r21 = 1;
        r22 = 1;
        r23 = 0;
        r4 = r18.presentFragment(r19, r20, r21, r22, r23);
        if (r4 == 0) goto L_0x01fc;
    L_0x01fa:
        r54 = 1;
    L_0x01fc:
        if (r54 != 0) goto L_0x0253;
    L_0x01fe:
        if (r75 != 0) goto L_0x0253;
    L_0x0200:
        r4 = org.telegram.messenger.AndroidUtilities.isTablet();
        if (r4 == 0) goto L_0x10db;
    L_0x0206:
        r0 = r73;
        r4 = r0.currentAccount;
        r4 = org.telegram.messenger.UserConfig.getInstance(r4);
        r4 = r4.isClientActivated();
        if (r4 != 0) goto L_0x10a7;
    L_0x0214:
        r0 = r73;
        r4 = r0.layersActionBarLayout;
        r4 = r4.fragmentsStack;
        r4 = r4.isEmpty();
        if (r4 == 0) goto L_0x0238;
    L_0x0220:
        r0 = r73;
        r4 = r0.layersActionBarLayout;
        r5 = new org.telegram.ui.LoginActivity;
        r5.<init>();
        r4.addFragmentToStack(r5);
        r0 = r73;
        r4 = r0.drawerLayoutContainer;
        r5 = 0;
        r18 = 0;
        r0 = r18;
        r4.setAllowOpenDrawer(r5, r0);
    L_0x0238:
        r0 = r73;
        r4 = r0.actionBarLayout;
        r4.showLastFragment();
        r4 = org.telegram.messenger.AndroidUtilities.isTablet();
        if (r4 == 0) goto L_0x0253;
    L_0x0245:
        r0 = r73;
        r4 = r0.layersActionBarLayout;
        r4.showLastFragment();
        r0 = r73;
        r4 = r0.rightActionBarLayout;
        r4.showLastFragment();
    L_0x0253:
        r4 = 0;
        r0 = r74;
        r0.setAction(r4);
        goto L_0x0023;
    L_0x025b:
        r34 = 0;
        goto L_0x00df;
    L_0x025f:
        r40 = 1;
        goto L_0x017c;
    L_0x0263:
        r38 = move-exception;
        org.telegram.messenger.FileLog.e(r38);
        r40 = 1;
        goto L_0x017c;
    L_0x026b:
        r4 = "android.intent.extra.TEXT";
        r0 = r74;
        r65 = r0.getStringExtra(r4);
        if (r65 != 0) goto L_0x0285;
    L_0x0276:
        r4 = "android.intent.extra.TEXT";
        r0 = r74;
        r66 = r0.getCharSequenceExtra(r4);
        if (r66 == 0) goto L_0x0285;
    L_0x0281:
        r65 = r66.toString();
    L_0x0285:
        r4 = "android.intent.extra.SUBJECT";
        r0 = r74;
        r64 = r0.getStringExtra(r4);
        if (r65 == 0) goto L_0x0344;
    L_0x0290:
        r4 = r65.length();
        if (r4 == 0) goto L_0x0344;
    L_0x0296:
        r4 = "http://";
        r0 = r65;
        r4 = r0.startsWith(r4);
        if (r4 != 0) goto L_0x02ac;
    L_0x02a1:
        r4 = "https://";
        r0 = r65;
        r4 = r0.startsWith(r4);
        if (r4 == 0) goto L_0x02d0;
    L_0x02ac:
        if (r64 == 0) goto L_0x02d0;
    L_0x02ae:
        r4 = r64.length();
        if (r4 == 0) goto L_0x02d0;
    L_0x02b4:
        r4 = new java.lang.StringBuilder;
        r4.<init>();
        r0 = r64;
        r4 = r4.append(r0);
        r5 = "\n";
        r4 = r4.append(r5);
        r0 = r65;
        r4 = r4.append(r0);
        r65 = r4.toString();
    L_0x02d0:
        r0 = r65;
        r1 = r73;
        r1.sendingText = r0;
    L_0x02d6:
        r4 = "android.intent.extra.STREAM";
        r0 = r74;
        r50 = r0.getParcelableExtra(r4);
        if (r50 == 0) goto L_0x03d3;
    L_0x02e1:
        r0 = r50;
        r4 = r0 instanceof android.net.Uri;
        if (r4 != 0) goto L_0x02ef;
    L_0x02e7:
        r4 = r50.toString();
        r50 = android.net.Uri.parse(r4);
    L_0x02ef:
        r68 = r50;
        r68 = (android.net.Uri) r68;
        if (r68 == 0) goto L_0x02fd;
    L_0x02f5:
        r4 = org.telegram.messenger.AndroidUtilities.isInternalUri(r68);
        if (r4 == 0) goto L_0x02fd;
    L_0x02fb:
        r40 = 1;
    L_0x02fd:
        if (r40 != 0) goto L_0x017c;
    L_0x02ff:
        if (r68 == 0) goto L_0x0353;
    L_0x0301:
        if (r67 == 0) goto L_0x030e;
    L_0x0303:
        r4 = "image/";
        r0 = r67;
        r4 = r0.startsWith(r4);
        if (r4 != 0) goto L_0x031f;
    L_0x030e:
        r4 = r68.toString();
        r4 = r4.toLowerCase();
        r5 = ".jpg";
        r4 = r4.endsWith(r5);
        if (r4 == 0) goto L_0x0353;
    L_0x031f:
        r0 = r73;
        r4 = r0.photoPathsArray;
        if (r4 != 0) goto L_0x032e;
    L_0x0325:
        r4 = new java.util.ArrayList;
        r4.<init>();
        r0 = r73;
        r0.photoPathsArray = r4;
    L_0x032e:
        r44 = new org.telegram.messenger.SendMessagesHelper$SendingMediaInfo;
        r44.<init>();
        r0 = r68;
        r1 = r44;
        r1.uri = r0;
        r0 = r73;
        r4 = r0.photoPathsArray;
        r0 = r44;
        r4.add(r0);
        goto L_0x017c;
    L_0x0344:
        if (r64 == 0) goto L_0x02d6;
    L_0x0346:
        r4 = r64.length();
        if (r4 <= 0) goto L_0x02d6;
    L_0x034c:
        r0 = r64;
        r1 = r73;
        r1.sendingText = r0;
        goto L_0x02d6;
    L_0x0353:
        r51 = org.telegram.messenger.AndroidUtilities.getPath(r68);
        if (r51 == 0) goto L_0x03b3;
    L_0x0359:
        r4 = "file:";
        r0 = r51;
        r4 = r0.startsWith(r4);
        if (r4 == 0) goto L_0x0370;
    L_0x0364:
        r4 = "file://";
        r5 = "";
        r0 = r51;
        r51 = r0.replace(r4, r5);
    L_0x0370:
        if (r67 == 0) goto L_0x0385;
    L_0x0372:
        r4 = "video/";
        r0 = r67;
        r4 = r0.startsWith(r4);
        if (r4 == 0) goto L_0x0385;
    L_0x037d:
        r0 = r51;
        r1 = r73;
        r1.videoPath = r0;
        goto L_0x017c;
    L_0x0385:
        r0 = r73;
        r4 = r0.documentsPathsArray;
        if (r4 != 0) goto L_0x039d;
    L_0x038b:
        r4 = new java.util.ArrayList;
        r4.<init>();
        r0 = r73;
        r0.documentsPathsArray = r4;
        r4 = new java.util.ArrayList;
        r4.<init>();
        r0 = r73;
        r0.documentsOriginalPathsArray = r4;
    L_0x039d:
        r0 = r73;
        r4 = r0.documentsPathsArray;
        r0 = r51;
        r4.add(r0);
        r0 = r73;
        r4 = r0.documentsOriginalPathsArray;
        r5 = r68.toString();
        r4.add(r5);
        goto L_0x017c;
    L_0x03b3:
        r0 = r73;
        r4 = r0.documentsUrisArray;
        if (r4 != 0) goto L_0x03c2;
    L_0x03b9:
        r4 = new java.util.ArrayList;
        r4.<init>();
        r0 = r73;
        r0.documentsUrisArray = r4;
    L_0x03c2:
        r0 = r73;
        r4 = r0.documentsUrisArray;
        r0 = r68;
        r4.add(r0);
        r0 = r67;
        r1 = r73;
        r1.documentsMimeType = r0;
        goto L_0x017c;
    L_0x03d3:
        r0 = r73;
        r4 = r0.sendingText;
        if (r4 != 0) goto L_0x017c;
    L_0x03d9:
        r40 = 1;
        goto L_0x017c;
    L_0x03dd:
        r4 = "android.intent.action.SEND_MULTIPLE";
        r5 = r74.getAction();
        r4 = r4.equals(r5);
        if (r4 == 0) goto L_0x054f;
    L_0x03ea:
        r40 = 0;
        r4 = "android.intent.extra.STREAM";
        r0 = r74;
        r69 = r0.getParcelableArrayListExtra(r4);	 Catch:{ Exception -> 0x0535 }
        r67 = r74.getType();	 Catch:{ Exception -> 0x0535 }
        if (r69 == 0) goto L_0x043f;
    L_0x03fb:
        r27 = 0;
    L_0x03fd:
        r4 = r69.size();	 Catch:{ Exception -> 0x0535 }
        r0 = r27;
        if (r0 >= r4) goto L_0x0437;
    L_0x0405:
        r0 = r69;
        r1 = r27;
        r50 = r0.get(r1);	 Catch:{ Exception -> 0x0535 }
        r50 = (android.os.Parcelable) r50;	 Catch:{ Exception -> 0x0535 }
        r0 = r50;
        r4 = r0 instanceof android.net.Uri;	 Catch:{ Exception -> 0x0535 }
        if (r4 != 0) goto L_0x041d;
    L_0x0415:
        r4 = r50.toString();	 Catch:{ Exception -> 0x0535 }
        r50 = android.net.Uri.parse(r4);	 Catch:{ Exception -> 0x0535 }
    L_0x041d:
        r0 = r50;
        r0 = (android.net.Uri) r0;	 Catch:{ Exception -> 0x0535 }
        r68 = r0;
        if (r68 == 0) goto L_0x0434;
    L_0x0425:
        r4 = org.telegram.messenger.AndroidUtilities.isInternalUri(r68);	 Catch:{ Exception -> 0x0535 }
        if (r4 == 0) goto L_0x0434;
    L_0x042b:
        r0 = r69;
        r1 = r27;
        r0.remove(r1);	 Catch:{ Exception -> 0x0535 }
        r27 = r27 + -1;
    L_0x0434:
        r27 = r27 + 1;
        goto L_0x03fd;
    L_0x0437:
        r4 = r69.isEmpty();	 Catch:{ Exception -> 0x0535 }
        if (r4 == 0) goto L_0x043f;
    L_0x043d:
        r69 = 0;
    L_0x043f:
        if (r69 == 0) goto L_0x054c;
    L_0x0441:
        if (r67 == 0) goto L_0x049c;
    L_0x0443:
        r4 = "image/";
        r0 = r67;
        r4 = r0.startsWith(r4);	 Catch:{ Exception -> 0x0535 }
        if (r4 == 0) goto L_0x049c;
    L_0x044e:
        r27 = 0;
    L_0x0450:
        r4 = r69.size();	 Catch:{ Exception -> 0x0535 }
        r0 = r27;
        if (r0 >= r4) goto L_0x053b;
    L_0x0458:
        r0 = r69;
        r1 = r27;
        r50 = r0.get(r1);	 Catch:{ Exception -> 0x0535 }
        r50 = (android.os.Parcelable) r50;	 Catch:{ Exception -> 0x0535 }
        r0 = r50;
        r4 = r0 instanceof android.net.Uri;	 Catch:{ Exception -> 0x0535 }
        if (r4 != 0) goto L_0x0470;
    L_0x0468:
        r4 = r50.toString();	 Catch:{ Exception -> 0x0535 }
        r50 = android.net.Uri.parse(r4);	 Catch:{ Exception -> 0x0535 }
    L_0x0470:
        r0 = r50;
        r0 = (android.net.Uri) r0;	 Catch:{ Exception -> 0x0535 }
        r68 = r0;
        r0 = r73;
        r4 = r0.photoPathsArray;	 Catch:{ Exception -> 0x0535 }
        if (r4 != 0) goto L_0x0485;
    L_0x047c:
        r4 = new java.util.ArrayList;	 Catch:{ Exception -> 0x0535 }
        r4.<init>();	 Catch:{ Exception -> 0x0535 }
        r0 = r73;
        r0.photoPathsArray = r4;	 Catch:{ Exception -> 0x0535 }
    L_0x0485:
        r44 = new org.telegram.messenger.SendMessagesHelper$SendingMediaInfo;	 Catch:{ Exception -> 0x0535 }
        r44.<init>();	 Catch:{ Exception -> 0x0535 }
        r0 = r68;
        r1 = r44;
        r1.uri = r0;	 Catch:{ Exception -> 0x0535 }
        r0 = r73;
        r4 = r0.photoPathsArray;	 Catch:{ Exception -> 0x0535 }
        r0 = r44;
        r4.add(r0);	 Catch:{ Exception -> 0x0535 }
        r27 = r27 + 1;
        goto L_0x0450;
    L_0x049c:
        r27 = 0;
    L_0x049e:
        r4 = r69.size();	 Catch:{ Exception -> 0x0535 }
        r0 = r27;
        if (r0 >= r4) goto L_0x053b;
    L_0x04a6:
        r0 = r69;
        r1 = r27;
        r50 = r0.get(r1);	 Catch:{ Exception -> 0x0535 }
        r50 = (android.os.Parcelable) r50;	 Catch:{ Exception -> 0x0535 }
        r0 = r50;
        r4 = r0 instanceof android.net.Uri;	 Catch:{ Exception -> 0x0535 }
        if (r4 != 0) goto L_0x04be;
    L_0x04b6:
        r4 = r50.toString();	 Catch:{ Exception -> 0x0535 }
        r50 = android.net.Uri.parse(r4);	 Catch:{ Exception -> 0x0535 }
    L_0x04be:
        r0 = r50;
        r0 = (android.net.Uri) r0;	 Catch:{ Exception -> 0x0535 }
        r68 = r0;
        r51 = org.telegram.messenger.AndroidUtilities.getPath(r68);	 Catch:{ Exception -> 0x0535 }
        r49 = r50.toString();	 Catch:{ Exception -> 0x0535 }
        if (r49 != 0) goto L_0x04d0;
    L_0x04ce:
        r49 = r51;
    L_0x04d0:
        if (r51 == 0) goto L_0x0516;
    L_0x04d2:
        r4 = "file:";
        r0 = r51;
        r4 = r0.startsWith(r4);	 Catch:{ Exception -> 0x0535 }
        if (r4 == 0) goto L_0x04e9;
    L_0x04dd:
        r4 = "file://";
        r5 = "";
        r0 = r51;
        r51 = r0.replace(r4, r5);	 Catch:{ Exception -> 0x0535 }
    L_0x04e9:
        r0 = r73;
        r4 = r0.documentsPathsArray;	 Catch:{ Exception -> 0x0535 }
        if (r4 != 0) goto L_0x0501;
    L_0x04ef:
        r4 = new java.util.ArrayList;	 Catch:{ Exception -> 0x0535 }
        r4.<init>();	 Catch:{ Exception -> 0x0535 }
        r0 = r73;
        r0.documentsPathsArray = r4;	 Catch:{ Exception -> 0x0535 }
        r4 = new java.util.ArrayList;	 Catch:{ Exception -> 0x0535 }
        r4.<init>();	 Catch:{ Exception -> 0x0535 }
        r0 = r73;
        r0.documentsOriginalPathsArray = r4;	 Catch:{ Exception -> 0x0535 }
    L_0x0501:
        r0 = r73;
        r4 = r0.documentsPathsArray;	 Catch:{ Exception -> 0x0535 }
        r0 = r51;
        r4.add(r0);	 Catch:{ Exception -> 0x0535 }
        r0 = r73;
        r4 = r0.documentsOriginalPathsArray;	 Catch:{ Exception -> 0x0535 }
        r0 = r49;
        r4.add(r0);	 Catch:{ Exception -> 0x0535 }
    L_0x0513:
        r27 = r27 + 1;
        goto L_0x049e;
    L_0x0516:
        r0 = r73;
        r4 = r0.documentsUrisArray;	 Catch:{ Exception -> 0x0535 }
        if (r4 != 0) goto L_0x0525;
    L_0x051c:
        r4 = new java.util.ArrayList;	 Catch:{ Exception -> 0x0535 }
        r4.<init>();	 Catch:{ Exception -> 0x0535 }
        r0 = r73;
        r0.documentsUrisArray = r4;	 Catch:{ Exception -> 0x0535 }
    L_0x0525:
        r0 = r73;
        r4 = r0.documentsUrisArray;	 Catch:{ Exception -> 0x0535 }
        r0 = r68;
        r4.add(r0);	 Catch:{ Exception -> 0x0535 }
        r0 = r67;
        r1 = r73;
        r1.documentsMimeType = r0;	 Catch:{ Exception -> 0x0535 }
        goto L_0x0513;
    L_0x0535:
        r38 = move-exception;
        org.telegram.messenger.FileLog.e(r38);
        r40 = 1;
    L_0x053b:
        if (r40 == 0) goto L_0x018b;
    L_0x053d:
        r4 = "Unsupported content";
        r5 = 0;
        r0 = r73;
        r4 = android.widget.Toast.makeText(r0, r4, r5);
        r4.show();
        goto L_0x018b;
    L_0x054c:
        r40 = 1;
        goto L_0x053b;
    L_0x054f:
        r4 = "android.intent.action.VIEW";
        r5 = r74.getAction();
        r4 = r4.equals(r5);
        if (r4 == 0) goto L_0x0bda;
    L_0x055c:
        r33 = r74.getData();
        if (r33 == 0) goto L_0x018b;
    L_0x0562:
        r6 = 0;
        r7 = 0;
        r8 = 0;
        r15 = 0;
        r16 = 0;
        r17 = 0;
        r9 = 0;
        r10 = 0;
        r11 = 0;
        r52 = 0;
        r14 = 0;
        r53 = 0;
        r13 = 0;
        r12 = 0;
        r59 = r33.getScheme();
        if (r59 == 0) goto L_0x05ef;
    L_0x057a:
        r4 = "http";
        r0 = r59;
        r4 = r0.equals(r4);
        if (r4 != 0) goto L_0x0590;
    L_0x0585:
        r4 = "https";
        r0 = r59;
        r4 = r0.equals(r4);
        if (r4 == 0) goto L_0x0782;
    L_0x0590:
        r4 = r33.getHost();
        r42 = r4.toLowerCase();
        r4 = "telegram.me";
        r0 = r42;
        r4 = r0.equals(r4);
        if (r4 != 0) goto L_0x05c4;
    L_0x05a3:
        r4 = "t.me";
        r0 = r42;
        r4 = r0.equals(r4);
        if (r4 != 0) goto L_0x05c4;
    L_0x05ae:
        r4 = "telegram.dog";
        r0 = r42;
        r4 = r0.equals(r4);
        if (r4 != 0) goto L_0x05c4;
    L_0x05b9:
        r4 = "telesco.pe";
        r0 = r42;
        r4 = r0.equals(r4);
        if (r4 == 0) goto L_0x05ef;
    L_0x05c4:
        r51 = r33.getPath();
        if (r51 == 0) goto L_0x05ef;
    L_0x05ca:
        r4 = r51.length();
        r5 = 1;
        if (r4 <= r5) goto L_0x05ef;
    L_0x05d1:
        r4 = 1;
        r0 = r51;
        r51 = r0.substring(r4);
        r4 = "joinchat/";
        r0 = r51;
        r4 = r0.startsWith(r4);
        if (r4 == 0) goto L_0x0639;
    L_0x05e3:
        r4 = "joinchat/";
        r5 = "";
        r0 = r51;
        r7 = r0.replace(r4, r5);
    L_0x05ef:
        if (r11 == 0) goto L_0x060e;
    L_0x05f1:
        r4 = "@";
        r4 = r11.startsWith(r4);
        if (r4 == 0) goto L_0x060e;
    L_0x05fa:
        r4 = new java.lang.StringBuilder;
        r4.<init>();
        r5 = " ";
        r4 = r4.append(r5);
        r4 = r4.append(r11);
        r11 = r4.toString();
    L_0x060e:
        if (r52 != 0) goto L_0x0612;
    L_0x0610:
        if (r53 == 0) goto L_0x0b35;
    L_0x0612:
        r29 = new android.os.Bundle;
        r29.<init>();
        r4 = "phone";
        r0 = r29;
        r1 = r52;
        r0.putString(r4, r1);
        r4 = "hash";
        r0 = r29;
        r1 = r53;
        r0.putString(r4, r1);
        r4 = new org.telegram.ui.LaunchActivity$9;
        r0 = r73;
        r1 = r29;
        r4.<init>(r1);
        org.telegram.messenger.AndroidUtilities.runOnUIThread(r4);
        goto L_0x018b;
    L_0x0639:
        r4 = "addstickers/";
        r0 = r51;
        r4 = r0.startsWith(r4);
        if (r4 == 0) goto L_0x0651;
    L_0x0644:
        r4 = "addstickers/";
        r5 = "";
        r0 = r51;
        r8 = r0.replace(r4, r5);
        goto L_0x05ef;
    L_0x0651:
        r4 = "iv/";
        r0 = r51;
        r4 = r0.startsWith(r4);
        if (r4 == 0) goto L_0x0689;
    L_0x065c:
        r4 = 0;
        r5 = "url";
        r0 = r33;
        r5 = r0.getQueryParameter(r5);
        r15[r4] = r5;
        r4 = 1;
        r5 = "rhash";
        r0 = r33;
        r5 = r0.getQueryParameter(r5);
        r15[r4] = r5;
        r4 = 0;
        r4 = r15[r4];
        r4 = android.text.TextUtils.isEmpty(r4);
        if (r4 != 0) goto L_0x0686;
    L_0x067d:
        r4 = 1;
        r4 = r15[r4];
        r4 = android.text.TextUtils.isEmpty(r4);
        if (r4 == 0) goto L_0x05ef;
    L_0x0686:
        r15 = 0;
        goto L_0x05ef;
    L_0x0689:
        r4 = "msg/";
        r0 = r51;
        r4 = r0.startsWith(r4);
        if (r4 != 0) goto L_0x069f;
    L_0x0694:
        r4 = "share/";
        r0 = r51;
        r4 = r0.startsWith(r4);
        if (r4 == 0) goto L_0x0711;
    L_0x069f:
        r4 = "url";
        r0 = r33;
        r11 = r0.getQueryParameter(r4);
        if (r11 != 0) goto L_0x06ad;
    L_0x06aa:
        r11 = "";
    L_0x06ad:
        r4 = "text";
        r0 = r33;
        r4 = r0.getQueryParameter(r4);
        if (r4 == 0) goto L_0x06ed;
    L_0x06b8:
        r4 = r11.length();
        if (r4 <= 0) goto L_0x06d3;
    L_0x06be:
        r12 = 1;
        r4 = new java.lang.StringBuilder;
        r4.<init>();
        r4 = r4.append(r11);
        r5 = "\n";
        r4 = r4.append(r5);
        r11 = r4.toString();
    L_0x06d3:
        r4 = new java.lang.StringBuilder;
        r4.<init>();
        r4 = r4.append(r11);
        r5 = "text";
        r0 = r33;
        r5 = r0.getQueryParameter(r5);
        r4 = r4.append(r5);
        r11 = r4.toString();
    L_0x06ed:
        r4 = r11.length();
        r5 = 16384; // 0x4000 float:2.2959E-41 double:8.0948E-320;
        if (r4 <= r5) goto L_0x06fc;
    L_0x06f5:
        r4 = 0;
        r5 = 16384; // 0x4000 float:2.2959E-41 double:8.0948E-320;
        r11 = r11.substring(r4, r5);
    L_0x06fc:
        r4 = "\n";
        r4 = r11.endsWith(r4);
        if (r4 == 0) goto L_0x05ef;
    L_0x0705:
        r4 = 0;
        r5 = r11.length();
        r5 = r5 + -1;
        r11 = r11.substring(r4, r5);
        goto L_0x06fc;
    L_0x0711:
        r4 = "confirmphone";
        r0 = r51;
        r4 = r0.startsWith(r4);
        if (r4 == 0) goto L_0x0730;
    L_0x071c:
        r4 = "phone";
        r0 = r33;
        r52 = r0.getQueryParameter(r4);
        r4 = "hash";
        r0 = r33;
        r53 = r0.getQueryParameter(r4);
        goto L_0x05ef;
    L_0x0730:
        r4 = r51.length();
        r5 = 1;
        if (r4 < r5) goto L_0x05ef;
    L_0x0737:
        r60 = r33.getPathSegments();
        r4 = r60.size();
        if (r4 <= 0) goto L_0x0765;
    L_0x0741:
        r4 = 0;
        r0 = r60;
        r6 = r0.get(r4);
        r6 = (java.lang.String) r6;
        r4 = r60.size();
        r5 = 1;
        if (r4 <= r5) goto L_0x0765;
    L_0x0751:
        r4 = 1;
        r0 = r60;
        r4 = r0.get(r4);
        r4 = (java.lang.String) r4;
        r13 = org.telegram.messenger.Utilities.parseInt(r4);
        r4 = r13.intValue();
        if (r4 != 0) goto L_0x0765;
    L_0x0764:
        r13 = 0;
    L_0x0765:
        r4 = "start";
        r0 = r33;
        r9 = r0.getQueryParameter(r4);
        r4 = "startgroup";
        r0 = r33;
        r10 = r0.getQueryParameter(r4);
        r4 = "game";
        r0 = r33;
        r14 = r0.getQueryParameter(r4);
        goto L_0x05ef;
    L_0x0782:
        r4 = "tg";
        r0 = r59;
        r4 = r0.equals(r4);
        if (r4 == 0) goto L_0x05ef;
    L_0x078d:
        r70 = r33.toString();
        r4 = "tg:resolve";
        r0 = r70;
        r4 = r0.startsWith(r4);
        if (r4 != 0) goto L_0x07a7;
    L_0x079c:
        r4 = "tg://resolve";
        r0 = r70;
        r4 = r0.startsWith(r4);
        if (r4 == 0) goto L_0x0863;
    L_0x07a7:
        r4 = "tg:resolve";
        r5 = "tg://telegram.org";
        r0 = r70;
        r4 = r0.replace(r4, r5);
        r5 = "tg://resolve";
        r18 = "tg://telegram.org";
        r0 = r18;
        r70 = r4.replace(r5, r0);
        r33 = android.net.Uri.parse(r70);
        r4 = "domain";
        r0 = r33;
        r6 = r0.getQueryParameter(r4);
        r4 = "telegrampassport";
        r4 = r4.equals(r6);
        if (r4 == 0) goto L_0x0832;
    L_0x07d5:
        r6 = 0;
        r16 = new java.util.HashMap;
        r16.<init>();
        r4 = "bot_id";
        r5 = "bot_id";
        r0 = r33;
        r5 = r0.getQueryParameter(r5);
        r0 = r16;
        r0.put(r4, r5);
        r4 = "scope";
        r5 = "scope";
        r0 = r33;
        r5 = r0.getQueryParameter(r5);
        r0 = r16;
        r0.put(r4, r5);
        r4 = "public_key";
        r5 = "public_key";
        r0 = r33;
        r5 = r0.getQueryParameter(r5);
        r0 = r16;
        r0.put(r4, r5);
        r4 = "payload";
        r5 = "payload";
        r0 = r33;
        r5 = r0.getQueryParameter(r5);
        r0 = r16;
        r0.put(r4, r5);
        r4 = "callback_url";
        r5 = "callback_url";
        r0 = r33;
        r5 = r0.getQueryParameter(r5);
        r0 = r16;
        r0.put(r4, r5);
        goto L_0x05ef;
    L_0x0832:
        r4 = "start";
        r0 = r33;
        r9 = r0.getQueryParameter(r4);
        r4 = "startgroup";
        r0 = r33;
        r10 = r0.getQueryParameter(r4);
        r4 = "game";
        r0 = r33;
        r14 = r0.getQueryParameter(r4);
        r4 = "post";
        r0 = r33;
        r4 = r0.getQueryParameter(r4);
        r13 = org.telegram.messenger.Utilities.parseInt(r4);
        r4 = r13.intValue();
        if (r4 != 0) goto L_0x05ef;
    L_0x0860:
        r13 = 0;
        goto L_0x05ef;
    L_0x0863:
        r4 = "tg:join";
        r0 = r70;
        r4 = r0.startsWith(r4);
        if (r4 != 0) goto L_0x0879;
    L_0x086e:
        r4 = "tg://join";
        r0 = r70;
        r4 = r0.startsWith(r4);
        if (r4 == 0) goto L_0x08a0;
    L_0x0879:
        r4 = "tg:join";
        r5 = "tg://telegram.org";
        r0 = r70;
        r4 = r0.replace(r4, r5);
        r5 = "tg://join";
        r18 = "tg://telegram.org";
        r0 = r18;
        r70 = r4.replace(r5, r0);
        r33 = android.net.Uri.parse(r70);
        r4 = "invite";
        r0 = r33;
        r7 = r0.getQueryParameter(r4);
        goto L_0x05ef;
    L_0x08a0:
        r4 = "tg:addstickers";
        r0 = r70;
        r4 = r0.startsWith(r4);
        if (r4 != 0) goto L_0x08b6;
    L_0x08ab:
        r4 = "tg://addstickers";
        r0 = r70;
        r4 = r0.startsWith(r4);
        if (r4 == 0) goto L_0x08dd;
    L_0x08b6:
        r4 = "tg:addstickers";
        r5 = "tg://telegram.org";
        r0 = r70;
        r4 = r0.replace(r4, r5);
        r5 = "tg://addstickers";
        r18 = "tg://telegram.org";
        r0 = r18;
        r70 = r4.replace(r5, r0);
        r33 = android.net.Uri.parse(r70);
        r4 = "set";
        r0 = r33;
        r8 = r0.getQueryParameter(r4);
        goto L_0x05ef;
    L_0x08dd:
        r4 = "tg:msg";
        r0 = r70;
        r4 = r0.startsWith(r4);
        if (r4 != 0) goto L_0x0909;
    L_0x08e8:
        r4 = "tg://msg";
        r0 = r70;
        r4 = r0.startsWith(r4);
        if (r4 != 0) goto L_0x0909;
    L_0x08f3:
        r4 = "tg://share";
        r0 = r70;
        r4 = r0.startsWith(r4);
        if (r4 != 0) goto L_0x0909;
    L_0x08fe:
        r4 = "tg:share";
        r0 = r70;
        r4 = r0.startsWith(r4);
        if (r4 == 0) goto L_0x09af;
    L_0x0909:
        r4 = "tg:msg";
        r5 = "tg://telegram.org";
        r0 = r70;
        r4 = r0.replace(r4, r5);
        r5 = "tg://msg";
        r18 = "tg://telegram.org";
        r0 = r18;
        r4 = r4.replace(r5, r0);
        r5 = "tg://share";
        r18 = "tg://telegram.org";
        r0 = r18;
        r4 = r4.replace(r5, r0);
        r5 = "tg:share";
        r18 = "tg://telegram.org";
        r0 = r18;
        r70 = r4.replace(r5, r0);
        r33 = android.net.Uri.parse(r70);
        r4 = "url";
        r0 = r33;
        r11 = r0.getQueryParameter(r4);
        if (r11 != 0) goto L_0x094b;
    L_0x0948:
        r11 = "";
    L_0x094b:
        r4 = "text";
        r0 = r33;
        r4 = r0.getQueryParameter(r4);
        if (r4 == 0) goto L_0x098b;
    L_0x0956:
        r4 = r11.length();
        if (r4 <= 0) goto L_0x0971;
    L_0x095c:
        r12 = 1;
        r4 = new java.lang.StringBuilder;
        r4.<init>();
        r4 = r4.append(r11);
        r5 = "\n";
        r4 = r4.append(r5);
        r11 = r4.toString();
    L_0x0971:
        r4 = new java.lang.StringBuilder;
        r4.<init>();
        r4 = r4.append(r11);
        r5 = "text";
        r0 = r33;
        r5 = r0.getQueryParameter(r5);
        r4 = r4.append(r5);
        r11 = r4.toString();
    L_0x098b:
        r4 = r11.length();
        r5 = 16384; // 0x4000 float:2.2959E-41 double:8.0948E-320;
        if (r4 <= r5) goto L_0x099a;
    L_0x0993:
        r4 = 0;
        r5 = 16384; // 0x4000 float:2.2959E-41 double:8.0948E-320;
        r11 = r11.substring(r4, r5);
    L_0x099a:
        r4 = "\n";
        r4 = r11.endsWith(r4);
        if (r4 == 0) goto L_0x05ef;
    L_0x09a3:
        r4 = 0;
        r5 = r11.length();
        r5 = r5 + -1;
        r11 = r11.substring(r4, r5);
        goto L_0x099a;
    L_0x09af:
        r4 = "tg:confirmphone";
        r0 = r70;
        r4 = r0.startsWith(r4);
        if (r4 != 0) goto L_0x09c5;
    L_0x09ba:
        r4 = "tg://confirmphone";
        r0 = r70;
        r4 = r0.startsWith(r4);
        if (r4 == 0) goto L_0x09f5;
    L_0x09c5:
        r4 = "tg:confirmphone";
        r5 = "tg://telegram.org";
        r0 = r70;
        r4 = r0.replace(r4, r5);
        r5 = "tg://confirmphone";
        r18 = "tg://telegram.org";
        r0 = r18;
        r70 = r4.replace(r5, r0);
        r33 = android.net.Uri.parse(r70);
        r4 = "phone";
        r0 = r33;
        r52 = r0.getQueryParameter(r4);
        r4 = "hash";
        r0 = r33;
        r53 = r0.getQueryParameter(r4);
        goto L_0x05ef;
    L_0x09f5:
        r4 = "tg:openmessage";
        r0 = r70;
        r4 = r0.startsWith(r4);
        if (r4 != 0) goto L_0x0a0b;
    L_0x0a00:
        r4 = "tg://openmessage";
        r0 = r70;
        r4 = r0.startsWith(r4);
        if (r4 == 0) goto L_0x0a63;
    L_0x0a0b:
        r4 = "tg:openmessage";
        r5 = "tg://telegram.org";
        r0 = r70;
        r4 = r0.replace(r4, r5);
        r5 = "tg://openmessage";
        r18 = "tg://telegram.org";
        r0 = r18;
        r70 = r4.replace(r5, r0);
        r33 = android.net.Uri.parse(r70);
        r4 = "user_id";
        r0 = r33;
        r71 = r0.getQueryParameter(r4);
        r4 = "chat_id";
        r0 = r33;
        r30 = r0.getQueryParameter(r4);
        r4 = "message_id";
        r0 = r33;
        r46 = r0.getQueryParameter(r4);
        if (r71 == 0) goto L_0x0a58;
    L_0x0a44:
        r4 = java.lang.Integer.parseInt(r71);	 Catch:{ NumberFormatException -> 0x113d }
        r58 = java.lang.Integer.valueOf(r4);	 Catch:{ NumberFormatException -> 0x113d }
    L_0x0a4c:
        if (r46 == 0) goto L_0x05ef;
    L_0x0a4e:
        r4 = java.lang.Integer.parseInt(r46);	 Catch:{ NumberFormatException -> 0x1137 }
        r57 = java.lang.Integer.valueOf(r4);	 Catch:{ NumberFormatException -> 0x1137 }
        goto L_0x05ef;
    L_0x0a58:
        if (r30 == 0) goto L_0x0a4c;
    L_0x0a5a:
        r4 = java.lang.Integer.parseInt(r30);	 Catch:{ NumberFormatException -> 0x113a }
        r55 = java.lang.Integer.valueOf(r4);	 Catch:{ NumberFormatException -> 0x113a }
        goto L_0x0a4c;
    L_0x0a63:
        r4 = "tg:passport";
        r0 = r70;
        r4 = r0.startsWith(r4);
        if (r4 != 0) goto L_0x0a84;
    L_0x0a6e:
        r4 = "tg://passport";
        r0 = r70;
        r4 = r0.startsWith(r4);
        if (r4 != 0) goto L_0x0a84;
    L_0x0a79:
        r4 = "tg:secureid";
        r0 = r70;
        r4 = r0.startsWith(r4);
        if (r4 == 0) goto L_0x0b08;
    L_0x0a84:
        r4 = "tg:passport";
        r5 = "tg://telegram.org";
        r0 = r70;
        r4 = r0.replace(r4, r5);
        r5 = "tg://passport";
        r18 = "tg://telegram.org";
        r0 = r18;
        r4 = r4.replace(r5, r0);
        r5 = "tg:secureid";
        r18 = "tg://telegram.org";
        r0 = r18;
        r70 = r4.replace(r5, r0);
        r33 = android.net.Uri.parse(r70);
        r16 = new java.util.HashMap;
        r16.<init>();
        r4 = "bot_id";
        r5 = "bot_id";
        r0 = r33;
        r5 = r0.getQueryParameter(r5);
        r0 = r16;
        r0.put(r4, r5);
        r4 = "scope";
        r5 = "scope";
        r0 = r33;
        r5 = r0.getQueryParameter(r5);
        r0 = r16;
        r0.put(r4, r5);
        r4 = "public_key";
        r5 = "public_key";
        r0 = r33;
        r5 = r0.getQueryParameter(r5);
        r0 = r16;
        r0.put(r4, r5);
        r4 = "payload";
        r5 = "payload";
        r0 = r33;
        r5 = r0.getQueryParameter(r5);
        r0 = r16;
        r0.put(r4, r5);
        r4 = "callback_url";
        r5 = "callback_url";
        r0 = r33;
        r5 = r0.getQueryParameter(r5);
        r0 = r16;
        r0.put(r4, r5);
        goto L_0x05ef;
    L_0x0b08:
        r4 = "tg://";
        r5 = "";
        r0 = r70;
        r4 = r0.replace(r4, r5);
        r5 = "tg:";
        r18 = "";
        r0 = r18;
        r17 = r4.replace(r5, r0);
        r4 = 63;
        r0 = r17;
        r43 = r0.indexOf(r4);
        if (r43 < 0) goto L_0x05ef;
    L_0x0b2a:
        r4 = 0;
        r0 = r17;
        r1 = r43;
        r17 = r0.substring(r4, r1);
        goto L_0x05ef;
    L_0x0b35:
        if (r6 != 0) goto L_0x0b45;
    L_0x0b37:
        if (r7 != 0) goto L_0x0b45;
    L_0x0b39:
        if (r8 != 0) goto L_0x0b45;
    L_0x0b3b:
        if (r11 != 0) goto L_0x0b45;
    L_0x0b3d:
        if (r14 != 0) goto L_0x0b45;
    L_0x0b3f:
        if (r15 != 0) goto L_0x0b45;
    L_0x0b41:
        if (r16 != 0) goto L_0x0b45;
    L_0x0b43:
        if (r17 == 0) goto L_0x0b51;
    L_0x0b45:
        r4 = 0;
        r5 = r45[r4];
        r18 = 0;
        r4 = r73;
        r4.runLinkRequest(r5, r6, r7, r8, r9, r10, r11, r12, r13, r14, r15, r16, r17, r18);
        goto L_0x018b;
    L_0x0b51:
        r18 = r73.getContentResolver();	 Catch:{ Exception -> 0x0bd1 }
        r19 = r74.getData();	 Catch:{ Exception -> 0x0bd1 }
        r20 = 0;
        r21 = 0;
        r22 = 0;
        r23 = 0;
        r32 = r18.query(r19, r20, r21, r22, r23);	 Catch:{ Exception -> 0x0bd1 }
        if (r32 == 0) goto L_0x018b;
    L_0x0b67:
        r4 = r32.moveToFirst();	 Catch:{ Exception -> 0x0bd1 }
        if (r4 == 0) goto L_0x0bcc;
    L_0x0b6d:
        r4 = "account_name";
        r0 = r32;
        r4 = r0.getColumnIndex(r4);	 Catch:{ Exception -> 0x0bd1 }
        r0 = r32;
        r4 = r0.getString(r4);	 Catch:{ Exception -> 0x0bd1 }
        r4 = org.telegram.messenger.Utilities.parseInt(r4);	 Catch:{ Exception -> 0x0bd1 }
        r28 = r4.intValue();	 Catch:{ Exception -> 0x0bd1 }
        r27 = 0;
    L_0x0b86:
        r4 = 3;
        r0 = r27;
        if (r0 >= r4) goto L_0x0ba3;
    L_0x0b8b:
        r4 = org.telegram.messenger.UserConfig.getInstance(r27);	 Catch:{ Exception -> 0x0bd1 }
        r4 = r4.getClientUserId();	 Catch:{ Exception -> 0x0bd1 }
        r0 = r28;
        if (r4 != r0) goto L_0x0bd7;
    L_0x0b97:
        r4 = 0;
        r45[r4] = r27;	 Catch:{ Exception -> 0x0bd1 }
        r4 = 0;
        r4 = r45[r4];	 Catch:{ Exception -> 0x0bd1 }
        r5 = 1;
        r0 = r73;
        r0.switchToAccount(r4, r5);	 Catch:{ Exception -> 0x0bd1 }
    L_0x0ba3:
        r4 = "DATA4";
        r0 = r32;
        r4 = r0.getColumnIndex(r4);	 Catch:{ Exception -> 0x0bd1 }
        r0 = r32;
        r72 = r0.getInt(r4);	 Catch:{ Exception -> 0x0bd1 }
        r4 = 0;
        r4 = r45[r4];	 Catch:{ Exception -> 0x0bd1 }
        r4 = org.telegram.messenger.NotificationCenter.getInstance(r4);	 Catch:{ Exception -> 0x0bd1 }
        r5 = org.telegram.messenger.NotificationCenter.closeChats;	 Catch:{ Exception -> 0x0bd1 }
        r18 = 0;
        r0 = r18;
        r0 = new java.lang.Object[r0];	 Catch:{ Exception -> 0x0bd1 }
        r18 = r0;
        r0 = r18;
        r4.postNotificationName(r5, r0);	 Catch:{ Exception -> 0x0bd1 }
        r58 = java.lang.Integer.valueOf(r72);	 Catch:{ Exception -> 0x0bd1 }
    L_0x0bcc:
        r32.close();	 Catch:{ Exception -> 0x0bd1 }
        goto L_0x018b;
    L_0x0bd1:
        r38 = move-exception;
        org.telegram.messenger.FileLog.e(r38);
        goto L_0x018b;
    L_0x0bd7:
        r27 = r27 + 1;
        goto L_0x0b86;
    L_0x0bda:
        r4 = r74.getAction();
        r5 = "org.telegram.messenger.OPEN_ACCOUNT";
        r4 = r4.equals(r5);
        if (r4 == 0) goto L_0x0bee;
    L_0x0be7:
        r4 = 1;
        r48 = java.lang.Integer.valueOf(r4);
        goto L_0x018b;
    L_0x0bee:
        r4 = r74.getAction();
        r5 = "new_dialog";
        r4 = r4.equals(r5);
        if (r4 == 0) goto L_0x0c02;
    L_0x0bfb:
        r4 = 1;
        r47 = java.lang.Integer.valueOf(r4);
        goto L_0x018b;
    L_0x0c02:
        r4 = r74.getAction();
        r5 = "com.tmessages.openchat";
        r4 = r4.startsWith(r5);
        if (r4 == 0) goto L_0x0c8b;
    L_0x0c0f:
        r4 = "chatId";
        r5 = 0;
        r0 = r74;
        r31 = r0.getIntExtra(r4, r5);
        r4 = "userId";
        r5 = 0;
        r0 = r74;
        r72 = r0.getIntExtra(r4, r5);
        r4 = "encId";
        r5 = 0;
        r0 = r74;
        r39 = r0.getIntExtra(r4, r5);
        if (r31 == 0) goto L_0x0c4b;
    L_0x0c2f:
        r4 = 0;
        r4 = r45[r4];
        r4 = org.telegram.messenger.NotificationCenter.getInstance(r4);
        r5 = org.telegram.messenger.NotificationCenter.closeChats;
        r18 = 0;
        r0 = r18;
        r0 = new java.lang.Object[r0];
        r18 = r0;
        r0 = r18;
        r4.postNotificationName(r5, r0);
        r55 = java.lang.Integer.valueOf(r31);
        goto L_0x018b;
    L_0x0c4b:
        if (r72 == 0) goto L_0x0c69;
    L_0x0c4d:
        r4 = 0;
        r4 = r45[r4];
        r4 = org.telegram.messenger.NotificationCenter.getInstance(r4);
        r5 = org.telegram.messenger.NotificationCenter.closeChats;
        r18 = 0;
        r0 = r18;
        r0 = new java.lang.Object[r0];
        r18 = r0;
        r0 = r18;
        r4.postNotificationName(r5, r0);
        r58 = java.lang.Integer.valueOf(r72);
        goto L_0x018b;
    L_0x0c69:
        if (r39 == 0) goto L_0x0c87;
    L_0x0c6b:
        r4 = 0;
        r4 = r45[r4];
        r4 = org.telegram.messenger.NotificationCenter.getInstance(r4);
        r5 = org.telegram.messenger.NotificationCenter.closeChats;
        r18 = 0;
        r0 = r18;
        r0 = new java.lang.Object[r0];
        r18 = r0;
        r0 = r18;
        r4.postNotificationName(r5, r0);
        r56 = java.lang.Integer.valueOf(r39);
        goto L_0x018b;
    L_0x0c87:
        r61 = 1;
        goto L_0x018b;
    L_0x0c8b:
        r4 = r74.getAction();
        r5 = "com.tmessages.openplayer";
        r4 = r4.equals(r5);
        if (r4 == 0) goto L_0x0c9c;
    L_0x0c98:
        r63 = 1;
        goto L_0x018b;
    L_0x0c9c:
        r4 = r74.getAction();
        r5 = "org.tmessages.openlocations";
        r4 = r4.equals(r5);
        if (r4 == 0) goto L_0x018b;
    L_0x0ca9:
        r62 = 1;
        goto L_0x018b;
    L_0x0cad:
        r4 = r55.intValue();
        if (r4 == 0) goto L_0x0d20;
    L_0x0cb3:
        r29 = new android.os.Bundle;
        r29.<init>();
        r4 = "chat_id";
        r5 = r55.intValue();
        r0 = r29;
        r0.putInt(r4, r5);
        r4 = r57.intValue();
        if (r4 == 0) goto L_0x0cd6;
    L_0x0cca:
        r4 = "message_id";
        r5 = r57.intValue();
        r0 = r29;
        r0.putInt(r4, r5);
    L_0x0cd6:
        r4 = mainFragmentsStack;
        r4 = r4.isEmpty();
        if (r4 != 0) goto L_0x0cff;
    L_0x0cde:
        r4 = 0;
        r4 = r45[r4];
        r5 = org.telegram.messenger.MessagesController.getInstance(r4);
        r4 = mainFragmentsStack;
        r18 = mainFragmentsStack;
        r18 = r18.size();
        r18 = r18 + -1;
        r0 = r18;
        r4 = r4.get(r0);
        r4 = (org.telegram.ui.ActionBar.BaseFragment) r4;
        r0 = r29;
        r4 = r5.checkCanOpenChat(r0, r4);
        if (r4 == 0) goto L_0x01fc;
    L_0x0cff:
        r19 = new org.telegram.ui.ChatActivity;
        r0 = r19;
        r1 = r29;
        r0.<init>(r1);
        r0 = r73;
        r0 = r0.actionBarLayout;
        r18 = r0;
        r20 = 0;
        r21 = 1;
        r22 = 1;
        r23 = 0;
        r4 = r18.presentFragment(r19, r20, r21, r22, r23);
        if (r4 == 0) goto L_0x01fc;
    L_0x0d1c:
        r54 = 1;
        goto L_0x01fc;
    L_0x0d20:
        r4 = r56.intValue();
        if (r4 == 0) goto L_0x0d58;
    L_0x0d26:
        r29 = new android.os.Bundle;
        r29.<init>();
        r4 = "enc_id";
        r5 = r56.intValue();
        r0 = r29;
        r0.putInt(r4, r5);
        r19 = new org.telegram.ui.ChatActivity;
        r0 = r19;
        r1 = r29;
        r0.<init>(r1);
        r0 = r73;
        r0 = r0.actionBarLayout;
        r18 = r0;
        r20 = 0;
        r21 = 1;
        r22 = 1;
        r23 = 0;
        r4 = r18.presentFragment(r19, r20, r21, r22, r23);
        if (r4 == 0) goto L_0x01fc;
    L_0x0d54:
        r54 = 1;
        goto L_0x01fc;
    L_0x0d58:
        if (r61 == 0) goto L_0x0dae;
    L_0x0d5a:
        r4 = org.telegram.messenger.AndroidUtilities.isTablet();
        if (r4 != 0) goto L_0x0d6d;
    L_0x0d60:
        r0 = r73;
        r4 = r0.actionBarLayout;
        r4.removeAllFragments();
    L_0x0d67:
        r54 = 0;
        r75 = 0;
        goto L_0x01fc;
    L_0x0d6d:
        r0 = r73;
        r4 = r0.layersActionBarLayout;
        r4 = r4.fragmentsStack;
        r4 = r4.isEmpty();
        if (r4 != 0) goto L_0x0d67;
    L_0x0d79:
        r27 = 0;
    L_0x0d7b:
        r0 = r73;
        r4 = r0.layersActionBarLayout;
        r4 = r4.fragmentsStack;
        r4 = r4.size();
        r4 = r4 + -1;
        if (r4 <= 0) goto L_0x0da5;
    L_0x0d89:
        r0 = r73;
        r5 = r0.layersActionBarLayout;
        r0 = r73;
        r4 = r0.layersActionBarLayout;
        r4 = r4.fragmentsStack;
        r18 = 0;
        r0 = r18;
        r4 = r4.get(r0);
        r4 = (org.telegram.ui.ActionBar.BaseFragment) r4;
        r5.removeFragmentFromStack(r4);
        r27 = r27 + -1;
        r27 = r27 + 1;
        goto L_0x0d7b;
    L_0x0da5:
        r0 = r73;
        r4 = r0.layersActionBarLayout;
        r5 = 0;
        r4.closeLastFragment(r5);
        goto L_0x0d67;
    L_0x0dae:
        if (r63 == 0) goto L_0x0dd9;
    L_0x0db0:
        r0 = r73;
        r4 = r0.actionBarLayout;
        r4 = r4.fragmentsStack;
        r4 = r4.isEmpty();
        if (r4 != 0) goto L_0x0dd5;
    L_0x0dbc:
        r0 = r73;
        r4 = r0.actionBarLayout;
        r4 = r4.fragmentsStack;
        r5 = 0;
        r19 = r4.get(r5);
        r19 = (org.telegram.ui.ActionBar.BaseFragment) r19;
        r4 = new org.telegram.ui.Components.AudioPlayerAlert;
        r0 = r73;
        r4.<init>(r0);
        r0 = r19;
        r0.showDialog(r4);
    L_0x0dd5:
        r54 = 0;
        goto L_0x01fc;
    L_0x0dd9:
        if (r62 == 0) goto L_0x0e0d;
    L_0x0ddb:
        r0 = r73;
        r4 = r0.actionBarLayout;
        r4 = r4.fragmentsStack;
        r4 = r4.isEmpty();
        if (r4 != 0) goto L_0x0e09;
    L_0x0de7:
        r0 = r73;
        r4 = r0.actionBarLayout;
        r4 = r4.fragmentsStack;
        r5 = 0;
        r19 = r4.get(r5);
        r19 = (org.telegram.ui.ActionBar.BaseFragment) r19;
        r4 = new org.telegram.ui.Components.SharingLocationsAlert;
        r5 = new org.telegram.ui.LaunchActivity$10;
        r0 = r73;
        r1 = r45;
        r5.<init>(r1);
        r0 = r73;
        r4.<init>(r0, r5);
        r0 = r19;
        r0.showDialog(r4);
    L_0x0e09:
        r54 = 0;
        goto L_0x01fc;
    L_0x0e0d:
        r0 = r73;
        r4 = r0.videoPath;
        if (r4 != 0) goto L_0x0e31;
    L_0x0e13:
        r0 = r73;
        r4 = r0.photoPathsArray;
        if (r4 != 0) goto L_0x0e31;
    L_0x0e19:
        r0 = r73;
        r4 = r0.sendingText;
        if (r4 != 0) goto L_0x0e31;
    L_0x0e1f:
        r0 = r73;
        r4 = r0.documentsPathsArray;
        if (r4 != 0) goto L_0x0e31;
    L_0x0e25:
        r0 = r73;
        r4 = r0.contactsToSend;
        if (r4 != 0) goto L_0x0e31;
    L_0x0e2b:
        r0 = r73;
        r4 = r0.documentsUrisArray;
        if (r4 == 0) goto L_0x0ffb;
    L_0x0e31:
        r4 = org.telegram.messenger.AndroidUtilities.isTablet();
        if (r4 != 0) goto L_0x0e4d;
    L_0x0e37:
        r4 = 0;
        r4 = r45[r4];
        r4 = org.telegram.messenger.NotificationCenter.getInstance(r4);
        r5 = org.telegram.messenger.NotificationCenter.closeChats;
        r18 = 0;
        r0 = r18;
        r0 = new java.lang.Object[r0];
        r18 = r0;
        r0 = r18;
        r4.postNotificationName(r5, r0);
    L_0x0e4d:
        r4 = 0;
        r4 = (r34 > r4 ? 1 : (r34 == r4 ? 0 : -1));
        if (r4 != 0) goto L_0x0fde;
    L_0x0e53:
        r29 = new android.os.Bundle;
        r29.<init>();
        r4 = "onlySelect";
        r5 = 1;
        r0 = r29;
        r0.putBoolean(r4, r5);
        r4 = "dialogsType";
        r5 = 3;
        r0 = r29;
        r0.putInt(r4, r5);
        r4 = "allowSwitchAccount";
        r5 = 1;
        r0 = r29;
        r0.putBoolean(r4, r5);
        r0 = r73;
        r4 = r0.contactsToSend;
        if (r4 == 0) goto L_0x0f39;
    L_0x0e79:
        r0 = r73;
        r4 = r0.contactsToSend;
        r4 = r4.size();
        r5 = 1;
        if (r4 == r5) goto L_0x0eac;
    L_0x0e84:
        r4 = "selectAlertString";
        r5 = "SendContactTo";
        r18 = 2131494525; // 0x7f0c067d float:1.861256E38 double:1.053098219E-314;
        r0 = r18;
        r5 = org.telegram.messenger.LocaleController.getString(r5, r0);
        r0 = r29;
        r0.putString(r4, r5);
        r4 = "selectAlertStringGroup";
        r5 = "SendContactToGroup";
        r18 = 2131494512; // 0x7f0c0670 float:1.8612534E38 double:1.0530982127E-314;
        r0 = r18;
        r5 = org.telegram.messenger.LocaleController.getString(r5, r0);
        r0 = r29;
        r0.putString(r4, r5);
    L_0x0eac:
        r19 = new org.telegram.ui.DialogsActivity;
        r0 = r19;
        r1 = r29;
        r0.<init>(r1);
        r0 = r19;
        r1 = r73;
        r0.setDelegate(r1);
        r4 = org.telegram.messenger.AndroidUtilities.isTablet();
        if (r4 == 0) goto L_0x0f66;
    L_0x0ec2:
        r0 = r73;
        r4 = r0.layersActionBarLayout;
        r4 = r4.fragmentsStack;
        r4 = r4.size();
        if (r4 <= 0) goto L_0x0f63;
    L_0x0ece:
        r0 = r73;
        r4 = r0.layersActionBarLayout;
        r4 = r4.fragmentsStack;
        r0 = r73;
        r5 = r0.layersActionBarLayout;
        r5 = r5.fragmentsStack;
        r5 = r5.size();
        r5 = r5 + -1;
        r4 = r4.get(r5);
        r4 = r4 instanceof org.telegram.ui.DialogsActivity;
        if (r4 == 0) goto L_0x0f63;
    L_0x0ee8:
        r20 = 1;
    L_0x0eea:
        r0 = r73;
        r0 = r0.actionBarLayout;
        r18 = r0;
        r21 = 1;
        r22 = 1;
        r23 = 0;
        r18.presentFragment(r19, r20, r21, r22, r23);
        r54 = 1;
        r4 = org.telegram.ui.SecretMediaViewer.hasInstance();
        if (r4 == 0) goto L_0x0f94;
    L_0x0f01:
        r4 = org.telegram.ui.SecretMediaViewer.getInstance();
        r4 = r4.isVisible();
        if (r4 == 0) goto L_0x0f94;
    L_0x0f0b:
        r4 = org.telegram.ui.SecretMediaViewer.getInstance();
        r5 = 0;
        r18 = 0;
        r0 = r18;
        r4.closePhoto(r5, r0);
    L_0x0f17:
        r0 = r73;
        r4 = r0.drawerLayoutContainer;
        r5 = 0;
        r18 = 0;
        r0 = r18;
        r4.setAllowOpenDrawer(r5, r0);
        r4 = org.telegram.messenger.AndroidUtilities.isTablet();
        if (r4 == 0) goto L_0x0fd0;
    L_0x0f29:
        r0 = r73;
        r4 = r0.actionBarLayout;
        r4.showLastFragment();
        r0 = r73;
        r4 = r0.rightActionBarLayout;
        r4.showLastFragment();
        goto L_0x01fc;
    L_0x0f39:
        r4 = "selectAlertString";
        r5 = "SendMessagesTo";
        r18 = 2131494525; // 0x7f0c067d float:1.861256E38 double:1.053098219E-314;
        r0 = r18;
        r5 = org.telegram.messenger.LocaleController.getString(r5, r0);
        r0 = r29;
        r0.putString(r4, r5);
        r4 = "selectAlertStringGroup";
        r5 = "SendMessagesToGroup";
        r18 = 2131494526; // 0x7f0c067e float:1.8612563E38 double:1.0530982196E-314;
        r0 = r18;
        r5 = org.telegram.messenger.LocaleController.getString(r5, r0);
        r0 = r29;
        r0.putString(r4, r5);
        goto L_0x0eac;
    L_0x0f63:
        r20 = 0;
        goto L_0x0eea;
    L_0x0f66:
        r0 = r73;
        r4 = r0.actionBarLayout;
        r4 = r4.fragmentsStack;
        r4 = r4.size();
        r5 = 1;
        if (r4 <= r5) goto L_0x0f91;
    L_0x0f73:
        r0 = r73;
        r4 = r0.actionBarLayout;
        r4 = r4.fragmentsStack;
        r0 = r73;
        r5 = r0.actionBarLayout;
        r5 = r5.fragmentsStack;
        r5 = r5.size();
        r5 = r5 + -1;
        r4 = r4.get(r5);
        r4 = r4 instanceof org.telegram.ui.DialogsActivity;
        if (r4 == 0) goto L_0x0f91;
    L_0x0f8d:
        r20 = 1;
    L_0x0f8f:
        goto L_0x0eea;
    L_0x0f91:
        r20 = 0;
        goto L_0x0f8f;
    L_0x0f94:
        r4 = org.telegram.ui.PhotoViewer.hasInstance();
        if (r4 == 0) goto L_0x0fb2;
    L_0x0f9a:
        r4 = org.telegram.ui.PhotoViewer.getInstance();
        r4 = r4.isVisible();
        if (r4 == 0) goto L_0x0fb2;
    L_0x0fa4:
        r4 = org.telegram.ui.PhotoViewer.getInstance();
        r5 = 0;
        r18 = 1;
        r0 = r18;
        r4.closePhoto(r5, r0);
        goto L_0x0f17;
    L_0x0fb2:
        r4 = org.telegram.ui.ArticleViewer.hasInstance();
        if (r4 == 0) goto L_0x0f17;
    L_0x0fb8:
        r4 = org.telegram.ui.ArticleViewer.getInstance();
        r4 = r4.isVisible();
        if (r4 == 0) goto L_0x0f17;
    L_0x0fc2:
        r4 = org.telegram.ui.ArticleViewer.getInstance();
        r5 = 0;
        r18 = 1;
        r0 = r18;
        r4.close(r5, r0);
        goto L_0x0f17;
    L_0x0fd0:
        r0 = r73;
        r4 = r0.drawerLayoutContainer;
        r5 = 1;
        r18 = 0;
        r0 = r18;
        r4.setAllowOpenDrawer(r5, r0);
        goto L_0x01fc;
    L_0x0fde:
        r37 = new java.util.ArrayList;
        r37.<init>();
        r4 = java.lang.Long.valueOf(r34);
        r0 = r37;
        r0.add(r4);
        r4 = 0;
        r5 = 0;
        r18 = 0;
        r0 = r73;
        r1 = r37;
        r2 = r18;
        r0.didSelectDialogs(r4, r1, r5, r2);
        goto L_0x01fc;
    L_0x0ffb:
        r4 = r48.intValue();
        if (r4 == 0) goto L_0x1048;
    L_0x1001:
        r0 = r73;
        r0 = r0.actionBarLayout;
        r21 = r0;
        r22 = new org.telegram.ui.SettingsActivity;
        r22.<init>();
        r23 = 0;
        r24 = 1;
        r25 = 1;
        r26 = 0;
        r21.presentFragment(r22, r23, r24, r25, r26);
        r4 = org.telegram.messenger.AndroidUtilities.isTablet();
        if (r4 == 0) goto L_0x103b;
    L_0x101d:
        r0 = r73;
        r4 = r0.actionBarLayout;
        r4.showLastFragment();
        r0 = r73;
        r4 = r0.rightActionBarLayout;
        r4.showLastFragment();
        r0 = r73;
        r4 = r0.drawerLayoutContainer;
        r5 = 0;
        r18 = 0;
        r0 = r18;
        r4.setAllowOpenDrawer(r5, r0);
    L_0x1037:
        r54 = 1;
        goto L_0x01fc;
    L_0x103b:
        r0 = r73;
        r4 = r0.drawerLayoutContainer;
        r5 = 1;
        r18 = 0;
        r0 = r18;
        r4.setAllowOpenDrawer(r5, r0);
        goto L_0x1037;
    L_0x1048:
        r4 = r47.intValue();
        if (r4 == 0) goto L_0x01fc;
    L_0x104e:
        r29 = new android.os.Bundle;
        r29.<init>();
        r4 = "destroyAfterSelect";
        r5 = 1;
        r0 = r29;
        r0.putBoolean(r4, r5);
        r0 = r73;
        r0 = r0.actionBarLayout;
        r21 = r0;
        r22 = new org.telegram.ui.ContactsActivity;
        r0 = r22;
        r1 = r29;
        r0.<init>(r1);
        r23 = 0;
        r24 = 1;
        r25 = 1;
        r26 = 0;
        r21.presentFragment(r22, r23, r24, r25, r26);
        r4 = org.telegram.messenger.AndroidUtilities.isTablet();
        if (r4 == 0) goto L_0x109a;
    L_0x107c:
        r0 = r73;
        r4 = r0.actionBarLayout;
        r4.showLastFragment();
        r0 = r73;
        r4 = r0.rightActionBarLayout;
        r4.showLastFragment();
        r0 = r73;
        r4 = r0.drawerLayoutContainer;
        r5 = 0;
        r18 = 0;
        r0 = r18;
        r4.setAllowOpenDrawer(r5, r0);
    L_0x1096:
        r54 = 1;
        goto L_0x01fc;
    L_0x109a:
        r0 = r73;
        r4 = r0.drawerLayoutContainer;
        r5 = 1;
        r18 = 0;
        r0 = r18;
        r4.setAllowOpenDrawer(r5, r0);
        goto L_0x1096;
    L_0x10a7:
        r0 = r73;
        r4 = r0.actionBarLayout;
        r4 = r4.fragmentsStack;
        r4 = r4.isEmpty();
        if (r4 == 0) goto L_0x0238;
    L_0x10b3:
        r36 = new org.telegram.ui.DialogsActivity;
        r4 = 0;
        r0 = r36;
        r0.<init>(r4);
        r0 = r73;
        r4 = r0.sideMenu;
        r0 = r36;
        r0.setSideMenu(r4);
        r0 = r73;
        r4 = r0.actionBarLayout;
        r0 = r36;
        r4.addFragmentToStack(r0);
        r0 = r73;
        r4 = r0.drawerLayoutContainer;
        r5 = 1;
        r18 = 0;
        r0 = r18;
        r4.setAllowOpenDrawer(r5, r0);
        goto L_0x0238;
    L_0x10db:
        r0 = r73;
        r4 = r0.actionBarLayout;
        r4 = r4.fragmentsStack;
        r4 = r4.isEmpty();
        if (r4 == 0) goto L_0x0238;
    L_0x10e7:
        r0 = r73;
        r4 = r0.currentAccount;
        r4 = org.telegram.messenger.UserConfig.getInstance(r4);
        r4 = r4.isClientActivated();
        if (r4 != 0) goto L_0x110f;
    L_0x10f5:
        r0 = r73;
        r4 = r0.actionBarLayout;
        r5 = new org.telegram.ui.LoginActivity;
        r5.<init>();
        r4.addFragmentToStack(r5);
        r0 = r73;
        r4 = r0.drawerLayoutContainer;
        r5 = 0;
        r18 = 0;
        r0 = r18;
        r4.setAllowOpenDrawer(r5, r0);
        goto L_0x0238;
    L_0x110f:
        r36 = new org.telegram.ui.DialogsActivity;
        r4 = 0;
        r0 = r36;
        r0.<init>(r4);
        r0 = r73;
        r4 = r0.sideMenu;
        r0 = r36;
        r0.setSideMenu(r4);
        r0 = r73;
        r4 = r0.actionBarLayout;
        r0 = r36;
        r4.addFragmentToStack(r0);
        r0 = r73;
        r4 = r0.drawerLayoutContainer;
        r5 = 1;
        r18 = 0;
        r0 = r18;
        r4.setAllowOpenDrawer(r5, r0);
        goto L_0x0238;
    L_0x1137:
        r4 = move-exception;
        goto L_0x05ef;
    L_0x113a:
        r4 = move-exception;
        goto L_0x0a4c;
    L_0x113d:
        r4 = move-exception;
        goto L_0x0a4c;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.telegram.ui.LaunchActivity.handleIntent(android.content.Intent, boolean, boolean, boolean):boolean");
    }

    private void runLinkRequest(int intentAccount, String username, String group, String sticker, String botUser, String botChat, String message, boolean hasUrl, Integer messageId, String game, String[] instantView, HashMap<String, String> auth, String unsupportedUrl, int state) {
        final String str;
        final String str2;
        if (state != 0 || UserConfig.getActivatedAccountsCount() < 2 || auth == null) {
            final int i;
            final AlertDialog progressDialog = new AlertDialog(this, 1);
            progressDialog.setMessage(LocaleController.getString("Loading", R.string.Loading));
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.setCancelable(false);
            int[] requestId = new int[]{0};
            TLObject req;
            if (username != null) {
                req = new TL_contacts_resolveUsername();
                req.username = username;
                final String str3 = game;
                final int i2 = intentAccount;
                str = botChat;
                str2 = botUser;
                final Integer num = messageId;
                requestId[0] = ConnectionsManager.getInstance(intentAccount).sendRequest(req, new RequestDelegate() {
                    public void run(final TLObject response, final TL_error error) {
                        AndroidUtilities.runOnUIThread(new Runnable() {
                            public void run() {
                                if (!LaunchActivity.this.isFinishing()) {
                                    try {
                                        progressDialog.dismiss();
                                    } catch (Throwable e) {
                                        FileLog.e(e);
                                    }
                                    TL_contacts_resolvedPeer res = (TL_contacts_resolvedPeer) response;
                                    if (error != null || LaunchActivity.this.actionBarLayout == null || (str3 != null && (str3 == null || res.users.isEmpty()))) {
                                        try {
                                            Toast.makeText(LaunchActivity.this, LocaleController.getString("NoUsernameFound", R.string.NoUsernameFound), 0).show();
                                            return;
                                        } catch (Throwable e2) {
                                            FileLog.e(e2);
                                            return;
                                        }
                                    }
                                    MessagesController.getInstance(i2).putUsers(res.users, false);
                                    MessagesController.getInstance(i2).putChats(res.chats, false);
                                    MessagesStorage.getInstance(i2).putUsersAndChats(res.users, res.chats, false, true);
                                    Bundle args;
                                    DialogsActivity fragment;
                                    if (str3 != null) {
                                        args = new Bundle();
                                        args.putBoolean("onlySelect", true);
                                        args.putBoolean("cantSendToChannels", true);
                                        args.putInt("dialogsType", 1);
                                        args.putString("selectAlertString", LocaleController.getString("SendGameTo", R.string.SendGameTo));
                                        args.putString("selectAlertStringGroup", LocaleController.getString("SendGameToGroup", R.string.SendGameToGroup));
                                        fragment = new DialogsActivity(args);
                                        final TL_contacts_resolvedPeer tL_contacts_resolvedPeer = res;
                                        fragment.setDelegate(new DialogsActivityDelegate() {
                                            public void didSelectDialogs(DialogsActivity fragment, ArrayList<Long> dids, CharSequence message, boolean param) {
                                                long did = ((Long) dids.get(0)).longValue();
                                                TL_inputMediaGame inputMediaGame = new TL_inputMediaGame();
                                                inputMediaGame.id = new TL_inputGameShortName();
                                                inputMediaGame.id.short_name = str3;
                                                inputMediaGame.id.bot_id = MessagesController.getInstance(i2).getInputUser((User) tL_contacts_resolvedPeer.users.get(0));
                                                SendMessagesHelper.getInstance(i2).sendGame(MessagesController.getInstance(i2).getInputPeer((int) did), inputMediaGame, 0, 0);
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
                                                if (MessagesController.getInstance(i2).checkCanOpenChat(args, fragment)) {
                                                    NotificationCenter.getInstance(i2).postNotificationName(NotificationCenter.closeChats, new Object[0]);
                                                    LaunchActivity.this.actionBarLayout.presentFragment(new ChatActivity(args), true, false, true, false);
                                                }
                                            }
                                        });
                                        boolean removeLast = AndroidUtilities.isTablet() ? LaunchActivity.this.layersActionBarLayout.fragmentsStack.size() > 0 && (LaunchActivity.this.layersActionBarLayout.fragmentsStack.get(LaunchActivity.this.layersActionBarLayout.fragmentsStack.size() - 1) instanceof DialogsActivity) : LaunchActivity.this.actionBarLayout.fragmentsStack.size() > 1 && (LaunchActivity.this.actionBarLayout.fragmentsStack.get(LaunchActivity.this.actionBarLayout.fragmentsStack.size() - 1) instanceof DialogsActivity);
                                        LaunchActivity.this.actionBarLayout.presentFragment(fragment, removeLast, true, true, false);
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
                                            return;
                                        }
                                        LaunchActivity.this.drawerLayoutContainer.setAllowOpenDrawer(true, false);
                                    } else if (str != null) {
                                        User user = !res.users.isEmpty() ? (User) res.users.get(0) : null;
                                        if (user == null || (user.bot && user.bot_nochats)) {
                                            try {
                                                Toast.makeText(LaunchActivity.this, LocaleController.getString("BotCantJoinGroups", R.string.BotCantJoinGroups), 0).show();
                                                return;
                                            } catch (Throwable e22) {
                                                FileLog.e(e22);
                                                return;
                                            }
                                        }
                                        args = new Bundle();
                                        args.putBoolean("onlySelect", true);
                                        args.putInt("dialogsType", 2);
                                        args.putString("addToGroupAlertString", LocaleController.formatString("AddToTheGroupTitle", R.string.AddToTheGroupTitle, UserObject.getUserName(user), "%1$s"));
                                        fragment = new DialogsActivity(args);
                                        final User user2 = user;
                                        fragment.setDelegate(new DialogsActivityDelegate() {
                                            public void didSelectDialogs(DialogsActivity fragment, ArrayList<Long> dids, CharSequence message, boolean param) {
                                                long did = ((Long) dids.get(0)).longValue();
                                                Bundle args = new Bundle();
                                                args.putBoolean("scrollToTopOnResume", true);
                                                args.putInt("chat_id", -((int) did));
                                                if (LaunchActivity.mainFragmentsStack.isEmpty() || MessagesController.getInstance(i2).checkCanOpenChat(args, (BaseFragment) LaunchActivity.mainFragmentsStack.get(LaunchActivity.mainFragmentsStack.size() - 1))) {
                                                    NotificationCenter.getInstance(i2).postNotificationName(NotificationCenter.closeChats, new Object[0]);
                                                    MessagesController.getInstance(i2).addUserToChat(-((int) did), user2, null, 0, str, null);
                                                    LaunchActivity.this.actionBarLayout.presentFragment(new ChatActivity(args), true, false, true, false);
                                                }
                                            }
                                        });
                                        LaunchActivity.this.presentFragment(fragment);
                                    } else {
                                        boolean isBot = false;
                                        args = new Bundle();
                                        long dialog_id;
                                        if (res.chats.isEmpty()) {
                                            args.putInt("user_id", ((User) res.users.get(0)).id);
                                            dialog_id = (long) ((User) res.users.get(0)).id;
                                        } else {
                                            args.putInt("chat_id", ((Chat) res.chats.get(0)).id);
                                            dialog_id = (long) (-((Chat) res.chats.get(0)).id);
                                        }
                                        if (str2 != null && res.users.size() > 0 && ((User) res.users.get(0)).bot) {
                                            args.putString("botUser", str2);
                                            isBot = true;
                                        }
                                        if (num != null) {
                                            args.putInt("message_id", num.intValue());
                                        }
                                        BaseFragment lastFragment = !LaunchActivity.mainFragmentsStack.isEmpty() ? (BaseFragment) LaunchActivity.mainFragmentsStack.get(LaunchActivity.mainFragmentsStack.size() - 1) : null;
                                        if (lastFragment != null && !MessagesController.getInstance(i2).checkCanOpenChat(args, lastFragment)) {
                                            return;
                                        }
                                        if (isBot && lastFragment != null && (lastFragment instanceof ChatActivity) && ((ChatActivity) lastFragment).getDialogId() == dialog_id) {
                                            ((ChatActivity) lastFragment).setBotUser(str2);
                                            return;
                                        }
                                        BaseFragment fragment2 = new ChatActivity(args);
                                        NotificationCenter.getInstance(i2).postNotificationName(NotificationCenter.closeChats, new Object[0]);
                                        LaunchActivity.this.actionBarLayout.presentFragment(fragment2, false, true, true, false);
                                    }
                                }
                            }
                        });
                    }
                });
            } else if (group != null) {
                if (state == 0) {
                    TLObject req2 = new TL_messages_checkChatInvite();
                    req2.hash = group;
                    final int i3 = intentAccount;
                    final String str4 = group;
                    str = username;
                    str2 = sticker;
                    final String str5 = botUser;
                    final String str6 = botChat;
                    final String str7 = message;
                    final boolean z = hasUrl;
                    final Integer num2 = messageId;
                    final String str8 = game;
                    final String[] strArr = instantView;
                    final HashMap<String, String> hashMap = auth;
                    final String str9 = unsupportedUrl;
                    requestId[0] = ConnectionsManager.getInstance(intentAccount).sendRequest(req2, new RequestDelegate() {
                        public void run(final TLObject response, final TL_error error) {
                            AndroidUtilities.runOnUIThread(new Runnable() {
                                public void run() {
                                    if (!LaunchActivity.this.isFinishing()) {
                                        try {
                                            progressDialog.dismiss();
                                        } catch (Throwable e) {
                                            FileLog.e(e);
                                        }
                                        Builder builder;
                                        if (error != null || LaunchActivity.this.actionBarLayout == null) {
                                            builder = new Builder(LaunchActivity.this);
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
                                            MessagesController.getInstance(i3).putChat(invite.chat, false);
                                            ArrayList<Chat> chats = new ArrayList();
                                            chats.add(invite.chat);
                                            MessagesStorage.getInstance(i3).putUsersAndChats(null, chats, false, true);
                                            Bundle args = new Bundle();
                                            args.putInt("chat_id", invite.chat.id);
                                            if (LaunchActivity.mainFragmentsStack.isEmpty() || MessagesController.getInstance(i3).checkCanOpenChat(args, (BaseFragment) LaunchActivity.mainFragmentsStack.get(LaunchActivity.mainFragmentsStack.size() - 1))) {
                                                ChatActivity fragment = new ChatActivity(args);
                                                NotificationCenter.getInstance(i3).postNotificationName(NotificationCenter.closeChats, new Object[0]);
                                                LaunchActivity.this.actionBarLayout.presentFragment(fragment, false, true, true, false);
                                            }
                                        } else if (((invite.chat != null || (invite.channel && !invite.megagroup)) && (invite.chat == null || (ChatObject.isChannel(invite.chat) && !invite.chat.megagroup))) || LaunchActivity.mainFragmentsStack.isEmpty()) {
                                            builder = new Builder(LaunchActivity.this);
                                            builder.setTitle(LocaleController.getString("AppName", R.string.AppName));
                                            String str = "ChannelJoinTo";
                                            Object[] objArr = new Object[1];
                                            objArr[0] = invite.chat != null ? invite.chat.title : invite.title;
                                            builder.setMessage(LocaleController.formatString(str, R.string.ChannelJoinTo, objArr));
                                            builder.setPositiveButton(LocaleController.getString("OK", R.string.OK), new OnClickListener() {
                                                public void onClick(DialogInterface dialogInterface, int i) {
                                                    LaunchActivity.this.runLinkRequest(i3, str, str4, str2, str5, str6, str7, z, num2, str8, strArr, hashMap, str9, 1);
                                                }
                                            });
                                            builder.setNegativeButton(LocaleController.getString("Cancel", R.string.Cancel), null);
                                            LaunchActivity.this.showAlertDialog(builder);
                                        } else {
                                            BaseFragment fragment2 = (BaseFragment) LaunchActivity.mainFragmentsStack.get(LaunchActivity.mainFragmentsStack.size() - 1);
                                            fragment2.showDialog(new JoinGroupAlert(LaunchActivity.this, invite, str4, fragment2));
                                        }
                                    }
                                }
                            });
                        }
                    }, 2);
                } else if (state == 1) {
                    req = new TL_messages_importChatInvite();
                    req.hash = group;
                    i = intentAccount;
                    ConnectionsManager.getInstance(intentAccount).sendRequest(req, new RequestDelegate() {
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
                                                    LaunchActivity.this.actionBarLayout.presentFragment(fragment, false, true, true, false);
                                                }
                                            }
                                        }
                                    }
                                }
                            });
                        }
                    }, 2);
                }
            } else if (sticker != null) {
                if (!mainFragmentsStack.isEmpty()) {
                    TL_inputStickerSetShortName stickerset = new TL_inputStickerSetShortName();
                    stickerset.short_name = sticker;
                    BaseFragment fragment = (BaseFragment) mainFragmentsStack.get(mainFragmentsStack.size() - 1);
                    fragment.showDialog(new StickersAlert(this, fragment, stickerset, null, null));
                    return;
                }
                return;
            } else if (message != null) {
                Bundle args = new Bundle();
                args.putBoolean("onlySelect", true);
                DialogsActivity fragment2 = new DialogsActivity(args);
                final boolean z2 = hasUrl;
                final int i4 = intentAccount;
                final String str10 = message;
                fragment2.setDelegate(new DialogsActivityDelegate() {
                    public void didSelectDialogs(DialogsActivity fragment, ArrayList<Long> dids, CharSequence m, boolean param) {
                        long did = ((Long) dids.get(0)).longValue();
                        Bundle args = new Bundle();
                        args.putBoolean("scrollToTopOnResume", true);
                        args.putBoolean("hasUrl", z2);
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
                        if (MessagesController.getInstance(i4).checkCanOpenChat(args, fragment)) {
                            NotificationCenter.getInstance(i4).postNotificationName(NotificationCenter.closeChats, new Object[0]);
                            DataQuery.getInstance(i4).saveDraft(did, str10, null, null, false);
                            LaunchActivity.this.actionBarLayout.presentFragment(new ChatActivity(args), true, false, true, false);
                        }
                    }
                });
                presentFragment(fragment2, false, true);
            } else if (instantView == null) {
                if (auth != null) {
                    int bot_id = Utilities.parseInt((String) auth.get("bot_id")).intValue();
                    if (bot_id != 0) {
                        final String payload = (String) auth.get("payload");
                        final String callbackUrl = (String) auth.get("callback_url");
                        req = new TL_account_getAuthorizationForm();
                        req.bot_id = bot_id;
                        req.scope = (String) auth.get("scope");
                        req.public_key = (String) auth.get("public_key");
                        final int[] iArr = requestId;
                        final int i5 = intentAccount;
                        final AlertDialog alertDialog = progressDialog;
                        requestId[0] = ConnectionsManager.getInstance(intentAccount).sendRequest(req, new RequestDelegate() {
                            public void run(TLObject response, final TL_error error) {
                                final TL_account_authorizationForm authorizationForm = (TL_account_authorizationForm) response;
                                if (authorizationForm != null) {
                                    iArr[0] = ConnectionsManager.getInstance(i5).sendRequest(new TL_account_getPassword(), new RequestDelegate() {
                                        public void run(final TLObject response, TL_error error) {
                                            AndroidUtilities.runOnUIThread(new Runnable() {
                                                public void run() {
                                                    try {
                                                        alertDialog.dismiss();
                                                    } catch (Throwable e) {
                                                        FileLog.e(e);
                                                    }
                                                    if (response != null) {
                                                        account_Password accountPassword = response;
                                                        MessagesController.getInstance(i5).putUsers(authorizationForm.users, false);
                                                        LaunchActivity.this.presentFragment(new PassportActivity(5, req.bot_id, req.scope, req.public_key, payload, callbackUrl, authorizationForm, accountPassword));
                                                    }
                                                }
                                            });
                                        }
                                    });
                                    return;
                                }
                                AndroidUtilities.runOnUIThread(new Runnable() {
                                    public void run() {
                                        try {
                                            alertDialog.dismiss();
                                            if ("APP_VERSION_OUTDATED".equals(error.text)) {
                                                AlertsCreator.showUpdateAppAlert(LaunchActivity.this, LocaleController.getString("UpdateAppAlert", R.string.UpdateAppAlert), true);
                                            } else {
                                                LaunchActivity.this.showAlertDialog(AlertsCreator.createSimpleAlert(LaunchActivity.this, LocaleController.getString("ErrorOccurred", R.string.ErrorOccurred) + "\n" + error.text));
                                            }
                                        } catch (Throwable e) {
                                            FileLog.e(e);
                                        }
                                    }
                                });
                            }
                        });
                    } else {
                        return;
                    }
                } else if (unsupportedUrl != null) {
                    req = new TL_help_getDeepLinkInfo();
                    req.path = unsupportedUrl;
                    requestId[0] = ConnectionsManager.getInstance(this.currentAccount).sendRequest(req, new RequestDelegate() {
                        public void run(final TLObject response, TL_error error) {
                            AndroidUtilities.runOnUIThread(new Runnable() {
                                public void run() {
                                    try {
                                        progressDialog.dismiss();
                                    } catch (Throwable e) {
                                        FileLog.e(e);
                                    }
                                    if (response instanceof TL_help_deepLinkInfo) {
                                        TL_help_deepLinkInfo res = response;
                                        AlertsCreator.showUpdateAppAlert(LaunchActivity.this, res.message, res.update_app);
                                    }
                                }
                            });
                        }
                    });
                }
            }
            if (requestId[0] != 0) {
                i = intentAccount;
                final int[] iArr2 = requestId;
                progressDialog.setButton(-2, LocaleController.getString("Cancel", R.string.Cancel), new OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        ConnectionsManager.getInstance(i).cancelRequest(iArr2[0], true);
                        try {
                            dialog.dismiss();
                        } catch (Throwable e) {
                            FileLog.e(e);
                        }
                    }
                });
                try {
                    progressDialog.show();
                    return;
                } catch (Exception e) {
                    return;
                }
            }
            return;
        }
        final int i6 = intentAccount;
        str3 = username;
        str4 = group;
        str = sticker;
        str2 = botUser;
        str5 = botChat;
        str6 = message;
        final boolean z3 = hasUrl;
        final Integer num3 = messageId;
        final String str11 = game;
        final String[] strArr2 = instantView;
        final HashMap<String, String> hashMap2 = auth;
        callbackUrl = unsupportedUrl;
        AlertsCreator.createAccountSelectDialog(this, new AccountSelectDelegate() {
            public void didSelectAccount(int account) {
                if (account != i6) {
                    LaunchActivity.this.switchToAccount(account, true);
                }
                LaunchActivity.this.runLinkRequest(account, str3, str4, str, str2, str5, str6, z3, num3, str11, strArr2, hashMap2, callbackUrl, 1);
            }
        }).show();
    }

    public void checkAppUpdate(boolean force) {
        if (!force && BuildVars.DEBUG_VERSION) {
            return;
        }
        if (!force && !BuildVars.CHECK_UPDATES) {
            return;
        }
        if (force || Math.abs(System.currentTimeMillis() - UserConfig.getInstance(0).lastUpdateCheckTime) >= 86400000) {
            TL_help_getAppUpdate req = new TL_help_getAppUpdate();
            try {
                req.source = ApplicationLoader.applicationContext.getPackageManager().getInstallerPackageName(ApplicationLoader.applicationContext.getPackageName());
            } catch (Exception e) {
            }
            if (req.source == null) {
                req.source = TtmlNode.ANONYMOUS_REGION_ID;
            }
            final int accountNum = this.currentAccount;
            ConnectionsManager.getInstance(this.currentAccount).sendRequest(req, new RequestDelegate() {
                public void run(TLObject response, TL_error error) {
                    UserConfig.getInstance(0).lastUpdateCheckTime = System.currentTimeMillis();
                    UserConfig.getInstance(0).saveConfig(false);
                    if (response instanceof TL_help_appUpdate) {
                        final TL_help_appUpdate res = (TL_help_appUpdate) response;
                        AndroidUtilities.runOnUIThread(new Runnable() {
                            public void run() {
                                if (BuildVars.DEBUG_PRIVATE_VERSION) {
                                    res.popup = Utilities.random.nextBoolean();
                                }
                                if (res.popup) {
                                    UserConfig.getInstance(0).pendingAppUpdate = res;
                                    UserConfig.getInstance(0).pendingAppUpdateBuildVersion = BuildVars.BUILD_VERSION;
                                    try {
                                        PackageInfo packageInfo = ApplicationLoader.applicationContext.getPackageManager().getPackageInfo(ApplicationLoader.applicationContext.getPackageName(), 0);
                                        UserConfig.getInstance(0).pendingAppUpdateInstallTime = Math.max(packageInfo.lastUpdateTime, packageInfo.firstInstallTime);
                                    } catch (Throwable e) {
                                        FileLog.e(e);
                                        UserConfig.getInstance(0).pendingAppUpdateInstallTime = 0;
                                    }
                                    UserConfig.getInstance(0).saveConfig(false);
                                    LaunchActivity.this.showUpdateActivity(accountNum, res);
                                    return;
                                }
                                new UpdateAppAlertDialog(LaunchActivity.this, res, accountNum).show();
                            }
                        });
                    }
                }
            });
        }
    }

    public AlertDialog showAlertDialog(Builder builder) {
        AlertDialog alertDialog = null;
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
                    if (LaunchActivity.this.visibleDialog != null) {
                        if (LaunchActivity.this.visibleDialog == LaunchActivity.this.localeDialog) {
                            try {
                                Toast.makeText(LaunchActivity.this, LaunchActivity.this.getStringForLanguageAlert(LocaleController.getInstance().getCurrentLocaleInfo().shortName.equals("en") ? LaunchActivity.this.englishLocaleStrings : LaunchActivity.this.systemLocaleStrings, "ChangeLanguageLater", R.string.ChangeLanguageLater), 1).show();
                            } catch (Throwable e) {
                                FileLog.e(e);
                            }
                            LaunchActivity.this.localeDialog = null;
                        } else if (LaunchActivity.this.visibleDialog == LaunchActivity.this.proxyErrorDialog) {
                            SharedPreferences preferences = MessagesController.getGlobalMainSettings();
                            Editor editor = MessagesController.getGlobalMainSettings().edit();
                            editor.putBoolean("proxy_enabled", false);
                            editor.putBoolean("proxy_enabled_calls", false);
                            editor.commit();
                            NotificationCenter.getGlobalInstance().removeObserver(this, NotificationCenter.proxySettingsChanged);
                            ConnectionsManager.setProxySettings(false, TtmlNode.ANONYMOUS_REGION_ID, 1080, TtmlNode.ANONYMOUS_REGION_ID, TtmlNode.ANONYMOUS_REGION_ID, TtmlNode.ANONYMOUS_REGION_ID);
                            NotificationCenter.getGlobalInstance().postNotificationName(NotificationCenter.proxySettingsChanged, new Object[0]);
                            LaunchActivity.this.proxyErrorDialog = null;
                        }
                    }
                    LaunchActivity.this.visibleDialog = null;
                }
            });
            return this.visibleDialog;
        } catch (Throwable e2) {
            FileLog.e(e2);
            return alertDialog;
        }
    }

    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        handleIntent(intent, true, false, false);
    }

    public void didSelectDialogs(DialogsActivity dialogsFragment, ArrayList<Long> dids, CharSequence message, boolean param) {
        final long did = ((Long) dids.get(0)).longValue();
        int lower_part = (int) did;
        int high_id = (int) (did >> 32);
        Bundle args = new Bundle();
        final int account = dialogsFragment != null ? dialogsFragment.getCurrentAccount() : this.currentAccount;
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
        if (MessagesController.getInstance(account).checkCanOpenChat(args, dialogsFragment)) {
            final BaseFragment fragment = new ChatActivity(args);
            if (this.contactsToSend == null || this.contactsToSend.size() != 1) {
                this.actionBarLayout.presentFragment(fragment, dialogsFragment != null, dialogsFragment == null, true, false);
                if (this.videoPath != null) {
                    fragment.openVideoEditor(this.videoPath, this.sendingText);
                    this.sendingText = null;
                }
                if (this.photoPathsArray != null) {
                    if (this.sendingText != null && this.sendingText.length() <= Callback.DEFAULT_DRAG_ANIMATION_DURATION && this.photoPathsArray.size() == 1) {
                        ((SendingMediaInfo) this.photoPathsArray.get(0)).caption = this.sendingText;
                        this.sendingText = null;
                    }
                    SendMessagesHelper.prepareSendingMedia(this.photoPathsArray, did, null, null, false, false, null);
                }
                if (this.sendingText != null) {
                    SendMessagesHelper.prepareSendingText(this.sendingText, did);
                }
                if (!(this.documentsPathsArray == null && this.documentsUrisArray == null)) {
                    SendMessagesHelper.prepareSendingDocuments(this.documentsPathsArray, this.documentsOriginalPathsArray, this.documentsUrisArray, this.documentsMimeType, did, null, null, null);
                }
                if (!(this.contactsToSend == null || this.contactsToSend.isEmpty())) {
                    for (int a = 0; a < this.contactsToSend.size(); a++) {
                        SendMessagesHelper.getInstance(account).sendMessage((User) this.contactsToSend.get(a), did, null, null, null);
                    }
                }
            } else if (this.contactsToSend.size() == 1) {
                boolean z;
                boolean z2;
                PhonebookShareActivity contactFragment = new PhonebookShareActivity(null, this.contactsToSendUri, null, null);
                contactFragment.setDelegate(new PhonebookSelectActivityDelegate() {
                    public void didSelectContact(User user) {
                        LaunchActivity.this.actionBarLayout.presentFragment(fragment, true, false, true, false);
                        SendMessagesHelper.getInstance(account).sendMessage(user, did, null, null, null);
                    }
                });
                ActionBarLayout actionBarLayout = this.actionBarLayout;
                if (dialogsFragment != null) {
                    z = true;
                } else {
                    z = false;
                }
                if (dialogsFragment == null) {
                    z2 = true;
                } else {
                    z2 = false;
                }
                actionBarLayout.presentFragment(contactFragment, z, z2, true, false);
            }
            this.photoPathsArray = null;
            this.videoPath = null;
            this.sendingText = null;
            this.documentsPathsArray = null;
            this.documentsOriginalPathsArray = null;
            this.contactsToSend = null;
            this.contactsToSendUri = null;
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
            NotificationCenter.getGlobalInstance().removeObserver(this, NotificationCenter.needShowAlert);
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
        return this.actionBarLayout.presentFragment(fragment, removeLast, forceWithoutAnimation, true, false);
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

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 3 || requestCode == 4 || requestCode == 5 || requestCode == 19 || requestCode == 20) {
            boolean showAlert = true;
            if (grantResults.length > 0 && grantResults[0] == 0) {
                if (requestCode == 4) {
                    ImageLoader.getInstance().checkMediaPaths();
                    return;
                } else if (requestCode == 5) {
                    ContactsController.getInstance(this.currentAccount).forceImportContacts();
                    return;
                } else if (requestCode == 3) {
                    if (SharedConfig.inappCamera) {
                        CameraController.getInstance().initCamera(null);
                        return;
                    }
                    return;
                } else if (requestCode == 19 || requestCode == 20) {
                    showAlert = false;
                }
            }
            if (showAlert) {
                Builder builder = new Builder((Context) this);
                builder.setTitle(LocaleController.getString("AppName", R.string.AppName));
                if (requestCode == 3) {
                    builder.setMessage(LocaleController.getString("PermissionNoAudio", R.string.PermissionNoAudio));
                } else if (requestCode == 4) {
                    builder.setMessage(LocaleController.getString("PermissionStorage", R.string.PermissionStorage));
                } else if (requestCode == 5) {
                    builder.setMessage(LocaleController.getString("PermissionContacts", R.string.PermissionContacts));
                } else if (requestCode == 19 || requestCode == 20) {
                    builder.setMessage(LocaleController.getString("PermissionNoCamera", R.string.PermissionNoCamera));
                }
                builder.setNegativeButton(LocaleController.getString("PermissionOpenSettings", R.string.PermissionOpenSettings), new OnClickListener() {
                    @TargetApi(9)
                    public void onClick(DialogInterface dialog, int which) {
                        try {
                            Intent intent = new Intent("android.settings.APPLICATION_DETAILS_SETTINGS");
                            intent.setData(Uri.parse("package:" + ApplicationLoader.applicationContext.getPackageName()));
                            LaunchActivity.this.startActivity(intent);
                        } catch (Throwable e) {
                            FileLog.e(e);
                        }
                    }
                });
                builder.setPositiveButton(LocaleController.getString("OK", R.string.OK), null);
                builder.show();
                return;
            }
        } else if (requestCode == 2 && grantResults.length > 0 && grantResults[0] == 0) {
            NotificationCenter.getGlobalInstance().postNotificationName(NotificationCenter.locationPermissionGranted, new Object[0]);
        }
        if (this.actionBarLayout.fragmentsStack.size() != 0) {
            ((BaseFragment) this.actionBarLayout.fragmentsStack.get(this.actionBarLayout.fragmentsStack.size() - 1)).onRequestPermissionsResultFragment(requestCode, permissions, grantResults);
        }
        if (AndroidUtilities.isTablet()) {
            if (this.rightActionBarLayout.fragmentsStack.size() != 0) {
                ((BaseFragment) this.rightActionBarLayout.fragmentsStack.get(this.rightActionBarLayout.fragmentsStack.size() - 1)).onRequestPermissionsResultFragment(requestCode, permissions, grantResults);
            }
            if (this.layersActionBarLayout.fragmentsStack.size() != 0) {
                ((BaseFragment) this.layersActionBarLayout.fragmentsStack.get(this.layersActionBarLayout.fragmentsStack.size() - 1)).onRequestPermissionsResultFragment(requestCode, permissions, grantResults);
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
        if (UserConfig.getInstance(UserConfig.selectedAccount).unacceptedTermsOfService != null) {
            showTosActivity(UserConfig.selectedAccount, UserConfig.getInstance(UserConfig.selectedAccount).unacceptedTermsOfService);
        } else if (UserConfig.getInstance(0).pendingAppUpdate != null) {
            showUpdateActivity(UserConfig.selectedAccount, UserConfig.getInstance(0).pendingAppUpdate);
        }
        checkAppUpdate(false);
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
        if (id == NotificationCenter.appDidLogout) {
            switchToAvailableAccountOrLogout();
        } else if (id == NotificationCenter.closeOtherAppActivities) {
            if (args[0] != this) {
                onFinish();
                finish();
            }
        } else if (id == NotificationCenter.didUpdatedConnectionState) {
            int state = ConnectionsManager.getInstance(account).getConnectionState();
            if (this.currentConnectionState != state) {
                if (BuildVars.LOGS_ENABLED) {
                    FileLog.d("switch to state " + state);
                }
                this.currentConnectionState = state;
                updateCurrentConnectionState(account);
            }
        } else if (id == NotificationCenter.mainUserInfoChanged) {
            this.drawerLayoutAdapter.notifyDataSetChanged();
        } else if (id == NotificationCenter.needShowAlert) {
            Integer reason = args[0];
            if (reason.intValue() == 3 && this.proxyErrorDialog != null) {
                return;
            }
            if (reason.intValue() == 4) {
                showTosActivity(account, (TL_help_termsOfService) args[1]);
                return;
            }
            builder = new Builder((Context) this);
            builder.setTitle(LocaleController.getString("AppName", R.string.AppName));
            if (!(reason.intValue() == 2 || reason.intValue() == 3)) {
                final int i = account;
                builder.setNegativeButton(LocaleController.getString("MoreInfo", R.string.MoreInfo), new OnClickListener() {
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (!LaunchActivity.mainFragmentsStack.isEmpty()) {
                            MessagesController.getInstance(i).openByUserName("spambot", (BaseFragment) LaunchActivity.mainFragmentsStack.get(LaunchActivity.mainFragmentsStack.size() - 1), 1);
                        }
                    }
                });
            }
            if (reason.intValue() == 0) {
                builder.setMessage(LocaleController.getString("NobodyLikesSpam1", R.string.NobodyLikesSpam1));
                builder.setPositiveButton(LocaleController.getString("OK", R.string.OK), null);
            } else if (reason.intValue() == 1) {
                builder.setMessage(LocaleController.getString("NobodyLikesSpam2", R.string.NobodyLikesSpam2));
                builder.setPositiveButton(LocaleController.getString("OK", R.string.OK), null);
            } else if (reason.intValue() == 2) {
                builder.setMessage((String) args[1]);
                if (args[2].startsWith("AUTH_KEY_DROP_")) {
                    builder.setPositiveButton(LocaleController.getString("Cancel", R.string.Cancel), null);
                    builder.setNegativeButton(LocaleController.getString("LogOut", R.string.LogOut), new OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            MessagesController.getInstance(LaunchActivity.this.currentAccount).performLogout(2);
                        }
                    });
                } else {
                    builder.setPositiveButton(LocaleController.getString("OK", R.string.OK), null);
                }
            } else if (reason.intValue() == 3) {
                builder.setMessage(LocaleController.getString("UseProxyTelegramError", R.string.UseProxyTelegramError));
                builder.setPositiveButton(LocaleController.getString("OK", R.string.OK), null);
                this.proxyErrorDialog = showAlertDialog(builder);
                return;
            }
            if (!mainFragmentsStack.isEmpty()) {
                ((BaseFragment) mainFragmentsStack.get(mainFragmentsStack.size() - 1)).showDialog(builder.create());
            }
        } else if (id == NotificationCenter.wasUnableToFindCurrentLocation) {
            HashMap<String, MessageObject> waitingForLocation = args[0];
            builder = new Builder((Context) this);
            builder.setTitle(LocaleController.getString("AppName", R.string.AppName));
            builder.setPositiveButton(LocaleController.getString("OK", R.string.OK), null);
            final HashMap<String, MessageObject> hashMap = waitingForLocation;
            final int i2 = account;
            builder.setNegativeButton(LocaleController.getString("ShareYouLocationUnableManually", R.string.ShareYouLocationUnableManually), new OnClickListener() {
                public void onClick(DialogInterface dialogInterface, int i) {
                    if (!LaunchActivity.mainFragmentsStack.isEmpty() && AndroidUtilities.isGoogleMapsInstalled((BaseFragment) LaunchActivity.mainFragmentsStack.get(LaunchActivity.mainFragmentsStack.size() - 1))) {
                        LocationActivity fragment = new LocationActivity(0);
                        fragment.setDelegate(new LocationActivityDelegate() {
                            public void didSelectLocation(MessageMedia location, int live) {
                                for (Entry<String, MessageObject> entry : hashMap.entrySet()) {
                                    MessageObject messageObject = (MessageObject) entry.getValue();
                                    SendMessagesHelper.getInstance(i2).sendMessage(location, messageObject.getDialogId(), messageObject, null, null);
                                }
                            }
                        });
                        LaunchActivity.this.presentFragment(fragment);
                    }
                }
            });
            builder.setMessage(LocaleController.getString("ShareYouLocationUnable", R.string.ShareYouLocationUnable));
            if (!mainFragmentsStack.isEmpty()) {
                ((BaseFragment) mainFragmentsStack.get(mainFragmentsStack.size() - 1)).showDialog(builder.create());
            }
        } else if (id == NotificationCenter.didSetNewWallpapper) {
            if (this.sideMenu != null) {
                child = this.sideMenu.getChildAt(0);
                if (child != null) {
                    child.invalidate();
                }
            }
        } else if (id == NotificationCenter.didSetPasscode) {
            if (SharedConfig.passcodeHash.length() <= 0 || SharedConfig.allowScreenCapture) {
                try {
                    getWindow().clearFlags(MessagesController.UPDATE_MASK_CHANNEL);
                    return;
                } catch (Throwable e) {
                    FileLog.e(e);
                    return;
                }
            }
            try {
                getWindow().setFlags(MessagesController.UPDATE_MASK_CHANNEL, MessagesController.UPDATE_MASK_CHANNEL);
            } catch (Throwable e2) {
                FileLog.e(e2);
            }
        } else if (id == NotificationCenter.reloadInterface) {
            rebuildAllFragments(false);
        } else if (id == NotificationCenter.suggestedLangpack) {
            showLanguageAlert(false);
        } else if (id == NotificationCenter.openArticle) {
            if (!mainFragmentsStack.isEmpty()) {
                ArticleViewer.getInstance().setParentActivity(this, (BaseFragment) mainFragmentsStack.get(mainFragmentsStack.size() - 1));
                ArticleViewer.getInstance().open((TL_webPage) args[0], (String) args[1]);
            }
        } else if (id == NotificationCenter.hasNewContactsToImport) {
            if (this.actionBarLayout != null && !this.actionBarLayout.fragmentsStack.isEmpty()) {
                int type = ((Integer) args[0]).intValue();
                final HashMap<String, Contact> contactHashMap = args[1];
                final boolean first = ((Boolean) args[2]).booleanValue();
                final boolean schedule = ((Boolean) args[3]).booleanValue();
                BaseFragment fragment = (BaseFragment) this.actionBarLayout.fragmentsStack.get(this.actionBarLayout.fragmentsStack.size() - 1);
                builder = new Builder((Context) this);
                builder.setTitle(LocaleController.getString("UpdateContactsTitle", R.string.UpdateContactsTitle));
                builder.setMessage(LocaleController.getString("UpdateContactsMessage", R.string.UpdateContactsMessage));
                final int i3 = account;
                builder.setPositiveButton(LocaleController.getString("OK", R.string.OK), new OnClickListener() {
                    public void onClick(DialogInterface dialogInterface, int i) {
                        ContactsController.getInstance(i3).syncPhoneBookByAlert(contactHashMap, first, schedule, false);
                    }
                });
                i3 = account;
                builder.setNegativeButton(LocaleController.getString("Cancel", R.string.Cancel), new OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        ContactsController.getInstance(i3).syncPhoneBookByAlert(contactHashMap, first, schedule, true);
                    }
                });
                i3 = account;
                builder.setOnBackButtonListener(new OnClickListener() {
                    public void onClick(DialogInterface dialogInterface, int i) {
                        ContactsController.getInstance(i3).syncPhoneBookByAlert(contactHashMap, first, schedule, true);
                    }
                });
                AlertDialog dialog = builder.create();
                fragment.showDialog(dialog);
                dialog.setCanceledOnTouchOutside(false);
            }
        } else if (id == NotificationCenter.didSetNewTheme) {
            if (!args[0].booleanValue()) {
                if (this.sideMenu != null) {
                    this.sideMenu.setBackgroundColor(Theme.getColor(Theme.key_chats_menuBackground));
                    this.sideMenu.setGlowColor(Theme.getColor(Theme.key_chats_menuBackground));
                    this.sideMenu.getAdapter().notifyDataSetChanged();
                }
                if (VERSION.SDK_INT >= 21) {
                    try {
                        setTaskDescription(new TaskDescription(null, null, Theme.getColor(Theme.key_actionBarDefault) | Theme.ACTION_BAR_VIDEO_EDIT_COLOR));
                    } catch (Exception e3) {
                    }
                }
            }
        } else if (id == NotificationCenter.needSetDayNightTheme) {
            ThemeInfo theme = args[0];
            this.actionBarLayout.animateThemedValues(theme);
            if (AndroidUtilities.isTablet()) {
                this.layersActionBarLayout.animateThemedValues(theme);
                this.rightActionBarLayout.animateThemedValues(theme);
            }
        } else if (id == NotificationCenter.notificationsCountUpdated && this.sideMenu != null) {
            Integer accountNum = args[0];
            int count = this.sideMenu.getChildCount();
            for (int a = 0; a < count; a++) {
                child = this.sideMenu.getChildAt(a);
                if ((child instanceof DrawerUserCell) && ((DrawerUserCell) child).getAccountNumber() == accountNum.intValue()) {
                    child.invalidate();
                    return;
                }
            }
        }
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
        try {
            LocaleInfo localeInfo;
            this.loadingLocaleDialog = false;
            boolean firstSystem = systemInfo.builtIn || LocaleController.getInstance().isCurrentLocalLocale();
            Builder builder = new Builder((Context) this);
            builder.setTitle(getStringForLanguageAlert(this.systemLocaleStrings, "ChooseYourLanguage", R.string.ChooseYourLanguage));
            builder.setSubtitle(getStringForLanguageAlert(this.englishLocaleStrings, "ChooseYourLanguage", R.string.ChooseYourLanguage));
            LinearLayout linearLayout = new LinearLayout(this);
            linearLayout.setOrientation(1);
            final LanguageCell[] cells = new LanguageCell[2];
            final LocaleInfo[] selectedLanguage = new LocaleInfo[1];
            LocaleInfo[] locales = new LocaleInfo[2];
            String englishName = getStringForLanguageAlert(this.systemLocaleStrings, "English", R.string.English);
            if (firstSystem) {
                localeInfo = systemInfo;
            } else {
                localeInfo = englishInfo;
            }
            locales[0] = localeInfo;
            if (firstSystem) {
                localeInfo = englishInfo;
            } else {
                localeInfo = systemInfo;
            }
            locales[1] = localeInfo;
            if (!firstSystem) {
                systemInfo = englishInfo;
            }
            selectedLanguage[0] = systemInfo;
            int a = 0;
            while (a < 2) {
                cells[a] = new LanguageCell(this, true);
                cells[a].setLanguage(locales[a], locales[a] == englishInfo ? englishName : null, true);
                cells[a].setTag(Integer.valueOf(a));
                cells[a].setBackgroundDrawable(Theme.createSelectorDrawable(Theme.getColor(Theme.key_dialogButtonSelector), 2));
                cells[a].setLanguageSelected(a == 0);
                linearLayout.addView(cells[a], LayoutHelper.createLinear(-1, 48));
                cells[a].setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        Integer tag = (Integer) v.getTag();
                        selectedLanguage[0] = ((LanguageCell) v).getCurrentLocale();
                        for (int a = 0; a < cells.length; a++) {
                            boolean z;
                            LanguageCell languageCell = cells[a];
                            if (a == tag.intValue()) {
                                z = true;
                            } else {
                                z = false;
                            }
                            languageCell.setLanguageSelected(z);
                        }
                    }
                });
                a++;
            }
            LanguageCell cell = new LanguageCell(this, true);
            cell.setValue(getStringForLanguageAlert(this.systemLocaleStrings, "ChooseYourLanguageOther", R.string.ChooseYourLanguageOther), getStringForLanguageAlert(this.englishLocaleStrings, "ChooseYourLanguageOther", R.string.ChooseYourLanguageOther));
            cell.setOnClickListener(new View.OnClickListener() {
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
            builder.setNegativeButton(LocaleController.getString("OK", R.string.OK), new OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    LocaleController.getInstance().applyLanguage(selectedLanguage[0], true, false, LaunchActivity.this.currentAccount);
                    LaunchActivity.this.rebuildAllFragments(true);
                }
            });
            this.localeDialog = showAlertDialog(builder);
            MessagesController.getGlobalMainSettings().edit().putString("language_showed2", systemLang).commit();
        } catch (Throwable e) {
            FileLog.e(e);
        }
    }

    private void showLanguageAlert(boolean force) {
        try {
            if (!this.loadingLocaleDialog) {
                String showedLang = MessagesController.getGlobalMainSettings().getString("language_showed2", TtmlNode.ANONYMOUS_REGION_ID);
                final String systemLang = LocaleController.getSystemLocaleStringIso639().toLowerCase();
                if (force || !showedLang.equals(systemLang)) {
                    String arg;
                    final LocaleInfo[] infos = new LocaleInfo[2];
                    if (systemLang.contains("-")) {
                        arg = systemLang.split("-")[0];
                    } else {
                        arg = systemLang;
                    }
                    String alias;
                    if ("in".equals(arg)) {
                        alias = TtmlNode.ATTR_ID;
                    } else if ("iw".equals(arg)) {
                        alias = "he";
                    } else if ("jw".equals(arg)) {
                        alias = "jv";
                    } else {
                        alias = null;
                    }
                    for (int a = 0; a < LocaleController.getInstance().languages.size(); a++) {
                        LocaleInfo info = (LocaleInfo) LocaleController.getInstance().languages.get(a);
                        if (info.shortName.equals("en")) {
                            infos[0] = info;
                        }
                        if (info.shortName.replace("_", "-").equals(systemLang) || info.shortName.equals(arg) || (alias != null && info.shortName.equals(alias))) {
                            infos[1] = info;
                        }
                        if (infos[0] != null && infos[1] != null) {
                            break;
                        }
                    }
                    if (infos[0] != null && infos[1] != null && infos[0] != infos[1]) {
                        if (BuildVars.LOGS_ENABLED) {
                            FileLog.d("show lang alert for " + infos[0].getKey() + " and " + infos[1].getKey());
                        }
                        this.systemLocaleStrings = null;
                        this.englishLocaleStrings = null;
                        this.loadingLocaleDialog = true;
                        TL_langpack_getStrings req = new TL_langpack_getStrings();
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
                    }
                } else if (BuildVars.LOGS_ENABLED) {
                    FileLog.d("alert already showed for " + showedLang);
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
            Runnable action = null;
            this.currentConnectionState = ConnectionsManager.getInstance(this.currentAccount).getConnectionState();
            if (this.currentConnectionState == 2) {
                title = LocaleController.getString("WaitingForNetwork", R.string.WaitingForNetwork);
            } else if (this.currentConnectionState == 5) {
                title = LocaleController.getString("Updating", R.string.Updating);
            } else if (this.currentConnectionState == 4) {
                title = LocaleController.getString("ConnectingToProxy", R.string.ConnectingToProxy);
            } else if (this.currentConnectionState == 1) {
                title = LocaleController.getString("Connecting", R.string.Connecting);
            }
            if (this.currentConnectionState == 1 || this.currentConnectionState == 4) {
                action = new Runnable() {
                    public void run() {
                        BaseFragment lastFragment = null;
                        if (AndroidUtilities.isTablet()) {
                            if (!LaunchActivity.layerFragmentsStack.isEmpty()) {
                                lastFragment = (BaseFragment) LaunchActivity.layerFragmentsStack.get(LaunchActivity.layerFragmentsStack.size() - 1);
                            }
                        } else if (!LaunchActivity.mainFragmentsStack.isEmpty()) {
                            lastFragment = (BaseFragment) LaunchActivity.mainFragmentsStack.get(LaunchActivity.mainFragmentsStack.size() - 1);
                        }
                        if (!(lastFragment instanceof ProxyListActivity) && !(lastFragment instanceof ProxySettingsActivity)) {
                            LaunchActivity.this.presentFragment(new ProxyListActivity());
                        }
                    }
                };
            }
            this.actionBarLayout.setTitleOverlayText(title, null, action);
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
        } else if (SecretMediaViewer.hasInstance() && SecretMediaViewer.getInstance().isVisible()) {
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
                cancel = !((BaseFragment) this.rightActionBarLayout.fragmentsStack.get(this.rightActionBarLayout.fragmentsStack.size() + -1)).onBackPressed();
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
        if (ArticleViewer.hasInstance() && ArticleViewer.getInstance().isVisible()) {
            ArticleViewer.getInstance().close(false, true);
        }
        if (AndroidUtilities.isTablet()) {
            DrawerLayoutContainer drawerLayoutContainer = this.drawerLayoutContainer;
            boolean z = ((fragment instanceof LoginActivity) || (fragment instanceof CountrySelectActivity) || this.layersActionBarLayout.getVisibility() == 0) ? false : true;
            drawerLayoutContainer.setAllowOpenDrawer(z, true);
            if ((fragment instanceof DialogsActivity) && ((DialogsActivity) fragment).isMainDialogList() && layout != this.actionBarLayout) {
                this.actionBarLayout.removeAllFragments();
                this.actionBarLayout.presentFragment(fragment, removeLast, forceWithoutAnimation, false, false);
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
                int a;
                if ((!this.tabletFullSize && layout == this.rightActionBarLayout) || (this.tabletFullSize && layout == this.actionBarLayout)) {
                    boolean result = (this.tabletFullSize && layout == this.actionBarLayout && this.actionBarLayout.fragmentsStack.size() == 1) ? false : true;
                    if (!this.layersActionBarLayout.fragmentsStack.isEmpty()) {
                        a = 0;
                        while (this.layersActionBarLayout.fragmentsStack.size() - 1 > 0) {
                            this.layersActionBarLayout.removeFragmentFromStack((BaseFragment) this.layersActionBarLayout.fragmentsStack.get(0));
                            a = (a - 1) + 1;
                        }
                        this.layersActionBarLayout.closeLastFragment(!forceWithoutAnimation);
                    }
                    if (result) {
                        return result;
                    }
                    this.actionBarLayout.presentFragment(fragment, false, forceWithoutAnimation, false, false);
                    return result;
                } else if (!this.tabletFullSize && layout != this.rightActionBarLayout) {
                    this.rightActionBarLayout.setVisibility(0);
                    this.backgroundTablet.setVisibility(8);
                    this.rightActionBarLayout.removeAllFragments();
                    this.rightActionBarLayout.presentFragment(fragment, removeLast, true, false, false);
                    if (!this.layersActionBarLayout.fragmentsStack.isEmpty()) {
                        a = 0;
                        while (this.layersActionBarLayout.fragmentsStack.size() - 1 > 0) {
                            this.layersActionBarLayout.removeFragmentFromStack((BaseFragment) this.layersActionBarLayout.fragmentsStack.get(0));
                            a = (a - 1) + 1;
                        }
                        this.layersActionBarLayout.closeLastFragment(!forceWithoutAnimation);
                    }
                    return false;
                } else if (!this.tabletFullSize || layout == this.actionBarLayout) {
                    if (!this.layersActionBarLayout.fragmentsStack.isEmpty()) {
                        a = 0;
                        while (this.layersActionBarLayout.fragmentsStack.size() - 1 > 0) {
                            this.layersActionBarLayout.removeFragmentFromStack((BaseFragment) this.layersActionBarLayout.fragmentsStack.get(0));
                            a = (a - 1) + 1;
                        }
                        this.layersActionBarLayout.closeLastFragment(!forceWithoutAnimation);
                    }
                    this.actionBarLayout.presentFragment(fragment, this.actionBarLayout.fragmentsStack.size() > 1, forceWithoutAnimation, false, false);
                    return false;
                } else {
                    this.actionBarLayout.presentFragment(fragment, this.actionBarLayout.fragmentsStack.size() > 1, forceWithoutAnimation, false, false);
                    if (!this.layersActionBarLayout.fragmentsStack.isEmpty()) {
                        a = 0;
                        while (this.layersActionBarLayout.fragmentsStack.size() - 1 > 0) {
                            this.layersActionBarLayout.removeFragmentFromStack((BaseFragment) this.layersActionBarLayout.fragmentsStack.get(0));
                            a = (a - 1) + 1;
                        }
                        this.layersActionBarLayout.closeLastFragment(!forceWithoutAnimation);
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
                this.layersActionBarLayout.presentFragment(fragment, removeLast, forceWithoutAnimation, false, false);
                return false;
            }
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
                    if (this.tabletFullSize) {
                        return false;
                    }
                    this.shadowTabletSide.setVisibility(0);
                    if (!this.rightActionBarLayout.fragmentsStack.isEmpty()) {
                        return false;
                    }
                    this.backgroundTablet.setVisibility(0);
                    return false;
                }
            } else if (fragment instanceof ChatActivity) {
                int a;
                if (!this.tabletFullSize && layout != this.rightActionBarLayout) {
                    this.rightActionBarLayout.setVisibility(0);
                    this.backgroundTablet.setVisibility(8);
                    this.rightActionBarLayout.removeAllFragments();
                    this.rightActionBarLayout.addFragmentToStack(fragment);
                    if (this.layersActionBarLayout.fragmentsStack.isEmpty()) {
                        return false;
                    }
                    a = 0;
                    while (this.layersActionBarLayout.fragmentsStack.size() - 1 > 0) {
                        this.layersActionBarLayout.removeFragmentFromStack((BaseFragment) this.layersActionBarLayout.fragmentsStack.get(0));
                        a = (a - 1) + 1;
                    }
                    this.layersActionBarLayout.closeLastFragment(true);
                    return false;
                } else if (this.tabletFullSize && layout != this.actionBarLayout) {
                    this.actionBarLayout.addFragmentToStack(fragment);
                    if (this.layersActionBarLayout.fragmentsStack.isEmpty()) {
                        return false;
                    }
                    a = 0;
                    while (this.layersActionBarLayout.fragmentsStack.size() - 1 > 0) {
                        this.layersActionBarLayout.removeFragmentFromStack((BaseFragment) this.layersActionBarLayout.fragmentsStack.get(0));
                        a = (a - 1) + 1;
                    }
                    this.layersActionBarLayout.closeLastFragment(true);
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
